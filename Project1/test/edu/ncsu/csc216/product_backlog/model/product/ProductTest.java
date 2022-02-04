package edu.ncsu.csc216.product_backlog.model.product;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.product_backlog.model.task.Task;
import edu.ncsu.csc216.product_backlog.model.task.Task.Type;

class ProductTest {

	@Test
	public void testProductConstruction() {
		Product testProduct = new Product("Test");
		
		assertEquals("Test", testProduct.getProductName());
		assertEquals(0, testProduct.getTasks().size());
		
		
		try {
			testProduct.setProductName("");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid product name.", e.getMessage());
		}
		
		try {
			testProduct.setProductName(null);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid product name.", e.getMessage());
		}
		
		
	}

	@Test
	public void testProductAddTask() {
		Product testProduct = new Product("Test");
		//assertNull(testProduct.getTasks().size());
		
		assertEquals(0, testProduct.getTasks().size());
		
		testProduct.addTask("Express", Type.BUG, "Tanvir", "note");
		assertEquals(1, testProduct.getTasks().size());
		
		Task productTask = new Task(3, "title", Type.BUG, "Tanvir", "TEST");
		testProduct.addTask(productTask);
		assertEquals(productTask, testProduct.getTaskById(3));
		
		//assertEquals(test1, testProduct.getTaskById(5));

	}
	
	@Test
	public void testProductDeleteTask() {
		Product testProduct = new Product("Test");
		
		Task test2 = new Task(6, "Test title", Type.BUG, "Arora", "TEST");
		testProduct.addTask(test2);
		
		assertEquals(1, testProduct.getTasks().size());
		
		testProduct.deleteTaskById(6);
		assertEquals(0, testProduct.getTasks().size());
		
	}
	
	@Test
	public void testAddTaskSameID() {
		Product testProduct = new Product("Test");
		
		Task test2 = new Task(6, "Test title", Type.BUG, "Arora", "TEST");
		testProduct.addTask(test2);
		Task test3 = new Task(6, "Test title", Type.BUG, "Arora", "TEST");
		
		try {
			testProduct.addTask(test3);
		} catch (IllegalArgumentException e) {
			assertEquals("Task cannot be added.", e.getMessage());
		}
	}
	
	
	
}
