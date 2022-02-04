package edu.ncsu.csc216.product_backlog.model.command;

import static org.junit.jupiter.api.Assertions.*;

import edu.ncsu.csc216.product_backlog.model.command.Command.CommandValue;


import org.junit.jupiter.api.Test;

class CommandTest {

	@Test
	public void testCommandConstructor() {
		Command c = new Command(CommandValue.BACKLOG, null, "task");
		assertEquals(CommandValue.BACKLOG, c.getCommand());
		
		// testing owner invalids
		try {
			c = new Command(CommandValue.BACKLOG, null, "task");
		} catch(IllegalArgumentException e) {
			assertEquals("Invalid command.", e.getMessage());
		}
		
		try {
			c = new Command(CommandValue.CLAIM, "", "task");
		} catch(IllegalArgumentException e) {
			assertEquals("Invalid command.", e.getMessage());
		}
		
		// testing noteText invalids
		try {
			c = new Command(CommandValue.BACKLOG, "Tanvir", "");
		} catch(IllegalArgumentException e) {
			assertEquals("Invalid command.", e.getMessage());
		} 
		
		try {
			c = new Command(CommandValue.BACKLOG, "Tanvir", null);
		} catch(IllegalArgumentException e) {
			assertEquals("Invalid command.", e.getMessage());
		}
		
		
	}
	

}
