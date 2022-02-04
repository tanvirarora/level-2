package edu.ncsu.csc216.product_backlog.model.backlog;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.product_backlog.model.task.Task.Type;

/**
 * Tests the BacklogManager class
 * @author tanvirarora
 *
 */
class BacklogManagerTest {

	/**
	 * Resets the BacklogManager each test to ensure singleton
	 */
	@BeforeEach
	public void setUp() throws Exception {
		BacklogManager.getInstance().resetManager();
	}
	
	/**
	 * Tests the set up of the BacklogManager as well as adding, deleting, size,clearing products, and adding tasks to products
	 */
	
	@Test
	public void testProductsInBacklogManagerProducts() {
		BacklogManager.getInstance().addProduct("Testing");
		
		assertEquals("Testing", BacklogManager.getInstance().getProductName());
		BacklogManager.getInstance().deleteProduct();
		assertNull(BacklogManager.getInstance().getProductName());
		
		BacklogManager.getInstance().addProduct("product 1");
		BacklogManager.getInstance().addProduct("product 2");
		BacklogManager.getInstance().clearProducts();
		assertNull(BacklogManager.getInstance().getProductName());
		
		BacklogManager.getInstance().addProduct("product 3");
		assertEquals(1, BacklogManager.getInstance().getProductList().length);
		BacklogManager.getInstance().addProduct("product 4");
		BacklogManager.getInstance().editProduct("Toys");
		BacklogManager.getInstance().deleteProduct();
		assertEquals(1, BacklogManager.getInstance().getProductList().length);
		
		String[] productName = BacklogManager.getInstance().getProductList();
		assertEquals("product 3", productName[0]);
		
		//Set up
		BacklogManager.getInstance().clearProducts();
		
		BacklogManager.getInstance().addProduct("Beyblade");
		
		
		
		try {
			BacklogManager.getInstance().editProduct("Beyblade");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid product name.", e.getMessage());
		}
		
		BacklogManager.getInstance().addProduct("Fruit");
		BacklogManager.getInstance().addTaskToProduct("task", Type.BUG, "Tanvir", "note");
	}

	/**
	 * Tests for invalid product using null and empty name values
	 */
	@Test
	public void testInvalidProduct() {
		
		try {
			BacklogManager.getInstance().addProduct(null);
			fail();
		} catch(IllegalArgumentException e) {
			assertEquals("Invalid product name.", e.getMessage());
		}
		
		try {
			BacklogManager.getInstance().addProduct("");
			fail();
		} catch(IllegalArgumentException e) {
			assertEquals("Invalid product name.", e.getMessage());
		}
		
		BacklogManager.getInstance().addProduct("testing product");
		
		try {
			BacklogManager.getInstance().editProduct(null);
			fail();
		} catch(IllegalArgumentException e) {
			assertEquals("Invalid product name.", e.getMessage());
		}
		
		try {
			BacklogManager.getInstance().editProduct("");
			fail();
		} catch(IllegalArgumentException e) {
			assertEquals("Invalid product name.", e.getMessage());
		}
		
		try {
			BacklogManager.getInstance().loadProduct("no product");
			fail();
		} catch(IllegalArgumentException e) {
			assertEquals("Product not available.", e.getMessage());
		}
		
	}
	
	/**
	 * Tests for proper Array construction of the BacklogManager
	 */
	@Test
	public void testBacklogManagerArray() {
		BacklogManager.getInstance().addProduct("Beyblade");
		BacklogManager.getInstance().addTaskToProduct("fast", Type.FEATURE, "Tanvir", "note");
		
		String[][] backlogArray = BacklogManager.getInstance().getTasksAsArray();
		assertEquals("1", backlogArray[0][0]);
		assertEquals("Backlog", backlogArray[0][1]);
		assertEquals("Feature", backlogArray[0][2]);
		assertEquals("fast", backlogArray[0][3]);
		
			
	}
	
	/**
	 * Tests for products to be loaded into a file and saved into the file
	 */
	@Test
	public void testProductFromFile() {
		BacklogManager.getInstance().loadFromFile("test-files/exp_task_backlog.txt");
		assertEquals("Product", BacklogManager.getInstance().getProductName());
		
		try {
			BacklogManager.getInstance().addProduct("King");
		} catch(IllegalArgumentException e) {
			assertEquals("Unable to save file.", e.getMessage());
		}
		
		try {
			BacklogManager.getInstance().saveToFile("test-files/practice_save_file");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to save file.", e.getMessage());
		}
		
		BacklogManager.getInstance().addProduct("Beyblade");
		BacklogManager.getInstance().addTaskToProduct("spin", Type.TECHNICAL_WORK, "Tanvir", "note");
		BacklogManager.getInstance().saveToFile("test-files/practice_save_file");
		
	}
}
