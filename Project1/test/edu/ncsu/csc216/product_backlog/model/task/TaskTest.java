package edu.ncsu.csc216.product_backlog.model.task;

import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.product_backlog.model.command.Command;
import edu.ncsu.csc216.product_backlog.model.command.Command.CommandValue;
import edu.ncsu.csc216.product_backlog.model.task.Task.Type;

/**
 * Test for Task class
 * @author tanvirarora
 *
 */
class TaskTest {

	/**
	 * Test to create the smaller Task constructor and run invalid checks
	 */
	@Test
	public void testSmallStateTask() {
		Task newTask = new Task(1, "task", Type.BUG, "Tanvir", "note");
		
		//getter methods testing
		assertEquals(1, newTask.getTaskId());
		assertEquals("task", newTask.getTitle());
		assertEquals(Type.BUG, newTask.getType());
		assertEquals("Tanvir", newTask.getCreator());
		assertEquals("Backlog", newTask.getStateName());
		//Create tasks with different type
		Task testTask = new Task(1, "Task", Type.FEATURE, "Tanvir", "note");
		assertEquals(Type.FEATURE, testTask.getType());
		
		Task testTask2 = new Task(2, "Task", Type.TECHNICAL_WORK, "Arora", "note");
		assertEquals(Type.TECHNICAL_WORK, testTask2.getType());
		
		Task createTask = new Task(2, "Task", Type.BUG, "Tanvir", "note");
		assertEquals(Type.BUG, createTask.getType());
		
		try {
			createTask = new Task(2, "Task", Type.BUG, null, "note");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid task information.", e.getMessage());
		}
		
		try {
			createTask = new Task(2, "", Type.BUG, "Tanvir", "note");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid task information.", e.getMessage());
		}
		
	}
	
	/**
	 * Tests to run the bigger task constructor
	 */
	@Test
	public void testBigStateTask() {
		ArrayList<String> notes = new ArrayList<String>();
		notes.add("testing");
		Task newTask = new Task(1, "Processing", "task", "B", "Tanvir", "Arora", "true", notes);
		
		assertEquals(1, newTask.getTaskId());
		assertEquals(Type.BUG, newTask.getType());
		
		// testing short and long name
		assertEquals("B", newTask.getTypeShortName());
		assertEquals("Bug", newTask.getTypeLongName());
		
		assertEquals("Processing", newTask.getStateName());
		assertEquals("Arora", newTask.getOwner());
		assertTrue(newTask.isVerified());
		
		assertEquals("testing", notes.get(0));
		
		newTask = new Task(2, "Verifying", "task", "F", "tanvir", "Arora", "true", notes);
		assertEquals("F", newTask.getTypeShortName());
		assertEquals("Feature", newTask.getTypeLongName());
		
		newTask = new Task(2, "Verifying", "task", "KA", "tanvir", "Arora", "false", notes);
		assertEquals("KA", newTask.getTypeShortName());
		assertEquals("Knowledge Acquisition", newTask.getTypeLongName());
	}
	/**
	 * Tests to run FSM transtions across different states
	 */
	@Test
	public void testTransitions() {
		Task newTask = new Task(1, "task", Type.FEATURE, "Tanvir", "note");
		
		assertEquals("Backlog", newTask.getStateName());
		
		// Correct transition
		newTask.update(new Command(CommandValue.CLAIM, "Arora", "got this"));
		assertEquals("Owned", newTask.getStateName());
		assertEquals("Arora", newTask.getOwner());
		
		try {
			newTask.update(new Command(CommandValue.CLAIM, "Tanvir", "task"));
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("Invalid transition.", e.getMessage());
		}
		
		try {
			newTask.update(new Command(CommandValue.VERIFY, null, "task"));
			fail();
		} catch (UnsupportedOperationException  e) {
			assertEquals("Invalid transition.", e.getMessage());
		}
		
		try {
			newTask.update(new Command(CommandValue.COMPLETE, null, "task"));
			fail();
		} catch (UnsupportedOperationException  e) {
			assertEquals("Invalid transition.", e.getMessage());
		}
		
		
		newTask.update(new Command(CommandValue.PROCESS, null, "Check"));
		assertEquals("Processing", newTask.getStateName());
		
		try {
			newTask.update(new Command(CommandValue.REJECT, null, "No"));
			fail();
		} catch(UnsupportedOperationException e) {
			assertEquals("Invalid transition.", e.getMessage());
		}
		
		try {
			newTask.update(new Command(CommandValue.CLAIM, "Tanvir", "claim"));
		} catch(UnsupportedOperationException e) {
			assertEquals("Invalid transition.", e.getMessage());
		}
		
		newTask.update(new Command(CommandValue.PROCESS, null, "process"));
		assertEquals("Processing", newTask.getStateName());
		
		newTask.update(new Command(CommandValue.VERIFY, null, "verify"));
		assertEquals("Verifying", newTask.getStateName());
		
		try {
			newTask.update(new Command(CommandValue.REJECT, null, "reject"));
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("Invalid transition.", e.getMessage());
		}
		
		newTask.update(new Command(CommandValue.PROCESS, null, "process"));
		assertEquals("Processing", newTask.getStateName());
		
		newTask.update(new Command(CommandValue.VERIFY, null, "verify"));
		assertEquals("Verifying", newTask.getStateName());
		
		newTask.update(new Command(CommandValue.PROCESS, null, "process"));
		assertEquals("Processing", newTask.getStateName());
		
		newTask.update(new Command(CommandValue.VERIFY, null, "verify"));
		assertEquals("Verifying", newTask.getStateName());
		
		newTask.update(new Command(CommandValue.COMPLETE, null, "done"));
		assertEquals("Done", newTask.getStateName());
		
		try {
			newTask.update(new Command(CommandValue.VERIFY, null, "checking"));
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("Invalid transition.", e.getMessage());
		}
		
		newTask.update(new Command(CommandValue.BACKLOG, null, "backlog"));
		assertEquals("Backlog", newTask.getStateName());
		
		newTask.update(new Command(CommandValue.REJECT, null, "reject"));
		assertEquals("Rejected", newTask.getStateName());
		
		newTask.update(new Command(CommandValue.BACKLOG, null, "backlog"));
		assertEquals("Backlog", newTask.getStateName());
		
		newTask.update(new Command(CommandValue.REJECT, null, "reject"));
		assertEquals("Rejected", newTask.getStateName());
		
		try {
			newTask.update(new Command(CommandValue.VERIFY, null, "checking"));
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("Invalid transition.", e.getMessage());
		}
	
		try {
			newTask.update(new Command(CommandValue.CLAIM, "mike", "claiming"));
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("Invalid transition.", e.getMessage());
		}
		
		newTask.update(new Command(CommandValue.BACKLOG, null, "backlog"));
		newTask.update(new Command(CommandValue.CLAIM, "Arora", "trying"));
		newTask.update(new Command(CommandValue.PROCESS, null, "process"));
		newTask.update(new Command(CommandValue.BACKLOG, null, "backlog"));
		
		
		Task newTask1 = new Task(1, "task", Type.KNOWLEDGE_ACQUISITION, "Tanvir", "note");
		
		newTask1.update(new Command(CommandValue.CLAIM, "Arora", "claim"));
		
		newTask1.update(new Command(CommandValue.PROCESS, null, "process"));
		
		newTask1.update(new Command(CommandValue.COMPLETE, null, "complete"));
		assertEquals("Done", newTask1.getStateName());
		newTask1.update(new Command(CommandValue.PROCESS, null, "process"));
		
		Task finalTask = new Task(1, "task", Type.BUG, "Tanvir", "note");
		finalTask.update(new Command(CommandValue.CLAIM, "Tanvir", "claim"));
		finalTask.update(new Command(CommandValue.PROCESS, null, "process"));
		
		Task finalTask1 = new Task(2, "task", Type.BUG, "Tanvir", "note");
		finalTask1.update(new Command(CommandValue.CLAIM, "Tanvir", "claim"));
		finalTask1.update(new Command(CommandValue.REJECT, null, "reject"));
		finalTask1.update(new Command(CommandValue.BACKLOG, null, "backlog"));
		finalTask1.update(new Command(CommandValue.CLAIM, "Tanvir", "claim"));
		finalTask1.update(new Command(CommandValue.BACKLOG, null, "backlog"));
	}

}