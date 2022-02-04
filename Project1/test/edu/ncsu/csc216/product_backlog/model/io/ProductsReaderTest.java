package edu.ncsu.csc216.product_backlog.model.io;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.product_backlog.model.product.Product;
import edu.ncsu.csc216.product_backlog.model.task.Task;

class ProductsReaderTest {

	@Test
	public void testReadFromValidFile() {
			ArrayList<Product> product = ProductsReader.readProductsFile("test-files/tasks16.txt");
			ArrayList<Task> task = product.get(0).getTasks();
			assertEquals(1, product.size());
			
			assertEquals("Shopping Cart Simulation", product.get(0).getProductName());
			assertEquals(1, task.size());
			
			//Check Tasks
			Task checkTask = task.get(0);
			assertEquals(1, checkTask.getTaskId());
			assertEquals("Backlog", checkTask.getStateName());
			assertEquals("Express Carts", checkTask.getTitle());
			assertEquals("F", checkTask.getTypeShortName());
			assertEquals("jep", checkTask.getCreator());
			assertEquals("owner", checkTask.getOwner());
			assertFalse(checkTask.isVerified());
		
		// could be put in a try catch
		
	}
	
	@Test
	public void testReadFromInvalidFile() {
		
		try {
			ProductsReader.readProductsFile("test-files\tasks9.txt"); // task file not added into test-files
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to load file.", e.getMessage());
		}
	}

}
