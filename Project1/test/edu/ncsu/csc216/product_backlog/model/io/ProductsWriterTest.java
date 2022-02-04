package edu.ncsu.csc216.product_backlog.model.io;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.product_backlog.model.product.Product;
import edu.ncsu.csc216.product_backlog.model.task.Task;
/*
 * Test for ProductsWriter class
 */
class ProductsWriterTest {

	/**
	 * Tests to write product into a file
	 */
	@Test
	public void testWriteToFile() {
		ArrayList<Product> product = new ArrayList<Product>();
		
		Product testProduct = new Product("Testing");
		ArrayList<String> notes = new ArrayList<String>();
		
		notes.add("test notes");
		Task task = new Task(1, "Owned", "task", "B", "tsarora", "owner", "false", notes);
		testProduct.addTask(task);
		
		product.add(testProduct);
		
		try {
			ProductsWriter.writeProductsToFile("test-files/testTasks.txt", product);
		} catch (IllegalArgumentException e) {
			fail("Cannot write to file.");
		}
	}
	

}
