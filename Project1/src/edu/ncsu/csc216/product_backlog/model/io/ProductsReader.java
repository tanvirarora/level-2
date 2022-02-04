/**
 * 
 */
package edu.ncsu.csc216.product_backlog.model.io;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import edu.ncsu.csc216.product_backlog.model.product.Product;
import edu.ncsu.csc216.product_backlog.model.task.Task;

/**
 * Reads in the products from a given file
 * @author tanvirarora
 *
 */
public class ProductsReader {

	/**
	 * Reads product from a file and returns it as an array list of products
	 * @param fileName name of the file that will be read
	 * @return null for now but will return the products read in from the file as an array list
	 */
	public static ArrayList<Product> readProductsFile(String fileName) {
		ArrayList<Product> productList = new ArrayList<Product>();
		Scanner fileReader = null;
		
		try {
			fileReader = new Scanner(new FileInputStream(fileName));
		} catch(Exception e) {
			throw new IllegalArgumentException("Unable to load file.");
		}
		
		String firstLineRead;
		String inFile = "";
		while(fileReader.hasNextLine()) {
			firstLineRead = fileReader.nextLine();
			
			if (!(firstLineRead.isEmpty())) {
				inFile = inFile + "\n" + firstLineRead.trim();
			}
		}
		
		if(!(inFile.contains("#") && inFile.contains("*") && inFile.contains("-"))) {
			return new ArrayList<Product>();
		}
		
		fileReader.close();
		
		
		String[] productTokens = inFile.split("\\r?\\n?[#]");	
				
		for (int i = 0; i < productTokens.length; i++) {
			
			if (productTokens[i].isEmpty()) {
				continue;
			}
			
			Product fileProduct = null;
			
			try {
				fileProduct = processProduct(productTokens[i].trim());
			} catch(IllegalArgumentException e) {
				return new ArrayList<Product>();
			}
			
			
			if (fileProduct != null) {
				productList.add(fileProduct);
			}
			
		}
		
		return productList;
	}
			
	/**
	 * Process the product token retrieved in readProductsFile method by reading through the string and assigning tokens to create a product object.
	 * @param p string read in from ReadProductsFile method
	 * @return newProduct object
	 */
	private static Product processProduct(String p) {
		if(p.charAt(0) == '#' || p.charAt(0) == '*' || p.charAt(0) == '-' || p.contains("#") || !p.contains("*") || !p.contains("-")) {
			throw new IllegalArgumentException("Invalid product string");
		}
		
		if (p.isEmpty()) {
			return null;
		}
		
		String fileProductName = p.substring(0, p.indexOf("\n"));
		fileProductName = fileProductName.trim();
		
		Product newProduct = new Product(fileProductName);
		
		String[] fileTasks = p.split("\\r?\\n?[*]");
		String[] fileTaskTokens = Arrays.copyOfRange(fileTasks, 1, fileTasks.length);
		
		for (int i = 0; i < fileTaskTokens.length; i++) {
			Task fileTask = null;
			try {
				fileTask = processTask(fileTaskTokens[i].trim());
			} catch(IllegalArgumentException e) {
				throw new IllegalArgumentException("Cannot process task");
			}
			if(newProduct.getTaskById(fileTask.getTaskId()) == null && fileTask != null) {
				newProduct.addTask(fileTask);
			}
		}
		
		return newProduct;
	}
	
	/**
	 * Process the tasks from the readProductsfile and going through the string and assigning values to create a task object
	 * @param t task as string
	 * @return fileTaskTokens object
	 * @throws IllegalArgumentException if the tasks tokens are not properly read in
	 */
	private static Task processTask(String t) {
		if(t.trim().isEmpty()) {
			return null;
		}
		
		String productTasks;
		
		try {
			productTasks = t.substring(0, t.indexOf("\n"));
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
		
		String[] taskInfo = productTasks.split(",");
		
		for (int j = 0; j < taskInfo.length; j++) {
			taskInfo[j] = taskInfo[j].trim();
		}
		
		boolean number = true;
		for(int i = 0; i < taskInfo[0].length(); i++) {
			if (!Character.isDigit(taskInfo[0].charAt(i))) {
				number = false;
			}
		}
		
		if(taskInfo.length != 7 || !number) {
			throw new IllegalArgumentException("Invalid task information");
		}
		
		int fileTaskId = Integer.parseInt(taskInfo[0]);
		String taskStateName = taskInfo[1];
		String taskStatetitle = taskInfo[2];
		String taskStatetype = taskInfo[3];
		String taskStatecreator = taskInfo[4];
		String taskStateowner = taskInfo[5];
		String taskStateverified = taskInfo[6];
		
		
		ArrayList<String> fileNotes = new ArrayList<String>();
		
		String[] taskNotes = t.split("\\r?\\n?[-]");
		String[] notesArray = Arrays.copyOfRange(taskNotes, 1, taskNotes.length);
		
		for (int k = 0; k < notesArray.length; k++) {
			fileNotes.add(notesArray[k].trim());
		}
		
		
		Task fileTaskTokens = null;
		
		try {
			fileTaskTokens = new Task(fileTaskId, taskStateName, taskStatetitle, taskStatetype, taskStatecreator, taskStateowner, taskStateverified, fileNotes);
		} catch(IllegalArgumentException e) {
			throw new IllegalArgumentException("Cannot construct task");
		} // returns null if the task construction fails
		return fileTaskTokens;
		
	}
	

}