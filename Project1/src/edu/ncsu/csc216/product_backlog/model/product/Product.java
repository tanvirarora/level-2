/**
 * 
 */
package edu.ncsu.csc216.product_backlog.model.product;

import java.util.ArrayList;

import edu.ncsu.csc216.product_backlog.model.command.Command;
import edu.ncsu.csc216.product_backlog.model.task.Task;
import edu.ncsu.csc216.product_backlog.model.task.Task.Type;

/**
 * Creates product that has a name and series of tasks that are associated with each product
 * @author tanvirarora
 *
 */
public class Product {

	/** String for product name */
	private String productName;
	
	/** counter for task id */
	private int counter;
	
	/** task list for each product */
	private ArrayList<Task> taskList;
	
	/**
	 * Constructor for product object using the productName and will later have a list of tasks and counter
	 * @param productName name of the product
	 */
	public Product (String productName) {
		emptyList();
		
		if (productName == null || "".equals(productName)) {
			throw new IllegalArgumentException("Invalid product name.");
		}
		this.productName = productName;
		
		setTaskCounter();
		
	}
	
	/**
	 * Setter method for the product name
	 * @param productName name of the product
	 */
	public void setProductName(String productName) {
		if (productName == null || "".equals(productName)) {
			throw new IllegalArgumentException("Invalid product name.");
		}
		this.productName = productName;
	}
	
	/**
	 * Getter method for the product name
	 * @return productName name of the product
	 */
	public String getProductName() {
		return productName;
	}
	
	/**
	 * Setter method for the task counter
	 */
	private void setTaskCounter() {
		if (taskList.size() < 1) {
			counter = 1;
		}
		else {
			for (int i = 0; i < taskList.size(); i++) {
				if(taskList.get(i).getTaskId() > counter) {
					counter = taskList.get(i).getTaskId();
				}
			}
			counter++;
		}
	}
	
	/**
	 * Method to set an empty list and erase the previous list
	 */
	private void emptyList() {
		taskList = new ArrayList<Task>();
	}
	
	/**
	 * Method to a task to a list of task
	 * @param task task to be added
	 */
	public void addTask(Task task) {
		for(int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == task.getTaskId()) {
				throw new IllegalArgumentException("Task cannot be added.");
			}
		}
		
		//Sorting checking
		
		if (taskList.size() == 0) {
			taskList.add(0, task);
		}
		
		else if (taskList.get(0).getTaskId() > task.getTaskId()) {
			taskList.add(0, task);
		}
		
		else if (taskList.get(taskList.size() - 1).getTaskId() < task.getTaskId()) {
			taskList.add(task);
		}
		
		else {
			int i = 0; 
			// find the index to add the new task based on the ID's
			while (taskList.get(i).getTaskId() < task.getTaskId()) {
				i++;
			}
			taskList.add(i, task);
		}
		setTaskCounter();
	}
	
	/**
	 * Creates a new task object with the following parameters and adds the task to a list of tasks
	 * @param title title of the task
	 * @param type type of task
	 * @param creator creator of the task
	 * @param note note associated with the task
	 */
	public void addTask(String title, Type type, String creator, String note) {
		Task addTask = new Task(counter, title, type, creator, note);
		addTask(addTask);
	}
	
	/**
	 * Getter method for the task list
	 * @return null for now but will later return a list
	 */
	public ArrayList<Task> getTasks() {
		return taskList;
	}
	
	/**
	 * Getter method for the task using the task id
	 * @param taskId task id to be used to return a task
	 * @return null for now but will later return the task
	 */
	public Task getTaskById(int taskId) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == taskId) {
				return taskList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Updates the task using the task id and the command given
	 * @param taskId task id used to get the task
	 * @param c command for the task
	 */
	public void executeCommand(int taskId, Command c) {
		Task task = getTaskById(taskId);
			if (task != null) {
				task.update(c);
			}
	}
	
	/**
	 * Deletes the task using the task id to find the task
	 * @param id task id used to find the task
	 */
	public void deleteTaskById(int id) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == id) {
				taskList.remove(i);
			}
		}
	}
	
}
