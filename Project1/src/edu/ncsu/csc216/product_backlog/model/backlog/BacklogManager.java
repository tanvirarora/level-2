/**
 * 
 */
package edu.ncsu.csc216.product_backlog.model.backlog;

import java.util.ArrayList;

import edu.ncsu.csc216.product_backlog.model.command.Command;
import edu.ncsu.csc216.product_backlog.model.io.ProductsReader;
import edu.ncsu.csc216.product_backlog.model.io.ProductsWriter;
import edu.ncsu.csc216.product_backlog.model.product.Product;
import edu.ncsu.csc216.product_backlog.model.task.Task;
import edu.ncsu.csc216.product_backlog.model.task.Task.Type;


/**
 * Class that deals with the products created and allows for the editing, removal and addition of products along with file work
 * @author tanvirarora
 *
 */
public class BacklogManager {

	/** single instance of BacklogManager */
	private static BacklogManager singleton;
	
	/** ArrayList of products */
	private ArrayList<Product> products = new ArrayList<Product>();
	
	/** currentProduct in list of products */
	private Product currentProduct;
		
	
	/**
	 * private constructor for the Backlog manager in order to only use one instance
	 */
	private BacklogManager() {
		
	}
	
	/**
	 * Gets an instance of the Backlog manager and makes sure that all classes use the same instance
	 * @return null for now but will return the instance of the backlog manager
	 */
	public static BacklogManager getInstance() {
		if (singleton == null) {
			singleton = new BacklogManager();
		}
		return singleton;
	}
	
	/**
	 * Uses the productWriter to write the products into the file name
	 * @param fileName file name to be used to write int the products
	 * @throws IllegalArgumentException if the currentProduct is null or the task size is 0
	 */
	public void saveToFile(String fileName) {
		if (currentProduct == null || currentProduct.getTasks().size() == 0) {
			throw new IllegalArgumentException("Unable to save file.");
		}
		ProductsWriter.writeProductsToFile(fileName, products);
	}
	
	/**
	 * Uses the productReader to read the fileName and create the list
	 * @param fileName file name to be used to read in the file
	 */
	public void loadFromFile(String fileName) {
		 ArrayList<Product> loadProducts = ProductsReader.readProductsFile(fileName);
		 currentProduct = loadProducts.get(0);
		 
		 for (int i = 0; i < loadProducts.size(); i++) {
			 products.add(loadProducts.get(i));
		 }
	}
	
	/**
	 * Finds the product with the product name and makes it the active or current product
	 * @param productName product name to be used to find the product
	 * @throws IllegalArgumentException if the product doesn't exist
	 */
	public void loadProduct(String productName) {
		boolean product = false;
		for (int i = 0; i < products.size(); i++) {
			if (productName.equals(products.get(i).getProductName())) {
				currentProduct = products.get(i);
				product = true;
			}
			
		}
		if(!product) {
			throw new IllegalArgumentException("Product not available.");
		}
	}
	
	/**
	 * Checks to see if two products are the same 
	 * @param productName product name to be used to see if the products are the same
	 * @throws IllegalArgumentException if the productName is the same as the new product name
	 */
	private void isDuplicateProduct(String productName) {
		
		if (products.size() > 0) {
			for (int i = 0; i < products.size(); i++) {
				if (productName.equalsIgnoreCase(products.get(i).getProductName())) {
					throw new IllegalArgumentException("Invalid product name.");
				}
			}
		}
	}
	
	/**
	 * Getter method for the task array as a 2d string array
	 * @return null for now but will return the tasks as a 2d array
	 */
	public String[][] getTasksAsArray() {
		String[][] taskAsArray = null;
	
		if (currentProduct != null) {
			ArrayList<Task> taskArray = currentProduct.getTasks();
			taskAsArray = new String[taskArray.size()][4];
			
			for(int i = 0; i < taskArray.size(); i++) {
				taskAsArray[i][0] = Integer.toString(taskArray.get(i).getTaskId());
				taskAsArray[i][1] = taskArray.get(i).getStateName();
				taskAsArray[i][2] = taskArray.get(i).getTypeLongName();
				taskAsArray[i][3] = taskArray.get(i).getTitle();
			}
		}
		
		return taskAsArray;
	}
	
	/**
	 * Getter method for the task using the task id
	 * @param taskId uses the task id to find the task
	 * @return null for now but will return the task using the id
	 */
	public Task getTaskById(int taskId) {
		if (currentProduct != null) {
			return currentProduct.getTaskById(taskId);
		}
		return null;
	}
	
	/**
	 * Executes a command based on the task id and the command given
	 * @param taskId id of the task
	 * @param c command given
	 */
	public void executeCommand(int taskId, Command c) {
		if (currentProduct != null && getTaskById(taskId) != null) {
			getTaskById(taskId).update(c);
			
		}	
	}
	
	/**
	 * Deletes task by finding the given task id
	 * @param taskId id of the task to be deleted
	 */
	public void deleteTaskById(int taskId) {
		if(currentProduct != null) {
			currentProduct.deleteTaskById(taskId);
		}
	}
	
	/**
	 * Adds a task to a product
	 * @param title title of the task
	 * @param type type of task
	 * @param creator creator of the task
	 * @param note note of the task
	 */
	public void addTaskToProduct(String title, Type type, String creator, String note) {
		currentProduct.addTask(title, type, creator, note);
	}
	
	/**
	 * Getter method for the product name
	 * @return null for now but will return the product name
	 */
	public String getProductName() {
		if (currentProduct == null) {
			return null;
		}
		return currentProduct.getProductName();
	}
	
	/**
	 * Getter method for the product list as a String array
	 * @return null for now but will return the product as an array
	 */
	public String[] getProductList() {
		String[] productList = new String[products.size()];
		
		for (int i = 0; i < products.size(); i++) {
			productList[i] = products.get(i).getProductName();
		}
		
		return productList;
	} 
	
	/**
	 * Clears the product in the product list.
	 */
	public void clearProducts() {
		products = new ArrayList<Product>();
		currentProduct = null;
	}
	
	/**
	 * Edits the name of the product
	 * @param updateName new name of the product
	 * @throws IllegalArgumentException if the currentProduct is null or the new update name is null or empty
	 */
	public void editProduct(String updateName) {
		if (currentProduct == null) {
			throw new IllegalArgumentException("No product selected.");
		}
		
		if (updateName == null || "".equals(updateName)) {
			throw new IllegalArgumentException("Invalid product name.");
		}
		isDuplicateProduct(updateName);
		currentProduct.setProductName(updateName);
	}
	
	/**
	 * Adds a product to the list
	 * @param productName name of the product to be added
	 * @throws IllegalArgumentException if the product name is null or empty
	 */
	public void addProduct(String productName) {
		if(productName == null || "".equals(productName)) {
			throw new IllegalArgumentException("Invalid product name.");
		}
		
		isDuplicateProduct(productName);
		products.add(new Product(productName));
		loadProduct(productName);
	}
	
	/**
	 * Deletes a product from the product list
	 * @throws IllegalArgumentException if the current product is null 
	 */
	public void deleteProduct() {
		if (currentProduct == null)  {
			throw new IllegalArgumentException("No product selected.");
		}
		
		products.remove(currentProduct);
		
		if (products.size() > 0) {
			currentProduct = products.get(0);
		}
		else {
			currentProduct = null;
		}
		
	}
	
	/**
	 * Completely resets the Backlog manager, getting rid of all instances
	 */
	protected void resetManager() {
		singleton = null;
	}
}
