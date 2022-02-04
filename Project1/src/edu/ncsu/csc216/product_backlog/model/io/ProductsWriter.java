/**
 * 
 */
package edu.ncsu.csc216.product_backlog.model.io;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import edu.ncsu.csc216.product_backlog.model.product.Product;

/**
 * Writes the products into an output file
 * @author tanvirarora
 *
 */
public class ProductsWriter {
	
	/**
	 * Writes the list of products into a specific file
	 * @param fileName name of the file where the products will be written too
	 * @param list array list of the products
	 */
	public static void writeProductsToFile(String fileName, ArrayList<Product> list) {
		try {
			PrintStream fileWriter = new PrintStream(new File(fileName));
			for (int i = 0; i < list.size(); i++) {
				fileWriter.println("# " + list.get(i).getProductName());
				
				for(int j = 0; j < list.get(i).getTasks().size(); j++) {
					fileWriter.println(list.get(i).getTasks().get(j).toString());
				}
			}
			fileWriter.close();
		} catch(Exception e){
			throw new IllegalArgumentException("Unable to save file.");
		}
	}
}
