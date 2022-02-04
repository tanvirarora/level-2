/**
 * 
 */
package edu.ncsu.csc216.product_backlog.model.task;

import java.util.ArrayList;

import edu.ncsu.csc216.product_backlog.model.command.Command;
import edu.ncsu.csc216.product_backlog.model.command.Command.CommandValue;

/**
 * Task class that creates a task using the task id, title, creator, owner, isVerified and notes
 * @author tanvirarora
 *
 */
public class Task {
	
	/** Id for a task */
	private int taskId;
	
	/** title for the task */
	private String title;
	
	/** creator for the task */
	private String creator;
	
	/** owner for the task */
	private String owner;
	
	/** Verified boolean for transition */
	private boolean isVerified;
	
	/** String ArrayList that has note description of tasks */
	private ArrayList<String> notes = new ArrayList<String>();
	
	/** Final string for Backlog */
	public static final String BACKLOG_NAME = "Backlog";
	
	/** Final string for Verifying */
	public static final String VERIFYING_NAME = "Verifying";
	
	/** Final string for Owned */
	public static final String OWNED_NAME = "Owned";
	
	/** Final string for Processing */
	public static final String PROCESSING_NAME = "Processing";
	
	/** Final string for Done */
	public static final String DONE_NAME = "Done";
	
	/** Final string for Rejected */
	public static final String REJECTED_NAME = "Rejected";
	
	/** Final string for Feature type*/
	public static final String FEATURE_NAME = "Feature";
	
	/** Final string for Bug Type*/
	public static final String BUG_NAME = "Bug";
	
	/** Final string for Technical Work Type*/
	public static final String TECHNICAL_WORK_NAME = "Technical Work";
	
	/** Final string for Knowledge Acquisition Name Type */
	public static final String KNOWLEDGE_ACQUISITION_NAME = "Knowledge Acquisition";
	
	/** Final string for Feature type */
	public static final String T_FEATURE = "F";
	
	/** Final string for Feature Bug */
	public static final String T_BUG = "B";
	
	/** Final string for Technical Work Type */
	public static final String T_TECHNICAL_WORK = "TW";
	
	/** Final string for Knowledge_Acquisition type */
	public static final String T_KNOWLEDGE_ACQUISITION = "KA";
	
	/** Final string for unowned task */
	public static final String UNOWNED = "unowned";
	
	/** Enumeration representing different types of Task */
	public enum Type { FEATURE, BUG, TECHNICAL_WORK, KNOWLEDGE_ACQUISITION }
	
	/** Represents Type enumeration instance */
	private Type type;
	
	/** Current state of the task */
	private TaskState currentState;
	
	/** owned state instance */
	private final TaskState ownedState = new OwnedState();
	
	/** task state instance */
	private final TaskState processingState = new ProcessingState();
	
	/** verifying state instance in class */
	private final TaskState verifyingState = new VerifyingState();
	
	/** done state instance in class */
	private final TaskState doneState = new DoneState();
	
	/** rejected state instance in class */
	private final TaskState rejectedState = new RejectedState();
	
	/** backlog state instance in class */
	private final TaskState backlogState = new BacklogState();
	
	/**
	 * Task object constructor that takes in the parameters and sets the values to create a new object
	 * @param taskId id of the task
	 * @param state state of the task
	 * @param title title of the task
	 * @param type type of task
	 * @param creator creator of the task
	 * @param owner owner of the task
	 * @param verified if task is verified
	 * @param notes notes related to a task
	 */
	public Task(int taskId, String state, String title, String type, String creator, String owner, String verified, ArrayList<String> notes) { 
		setTaskId(taskId);
		setState(state);
		setTitle(title);
		setTypeFromString(type);
		setCreator(creator);
		setOwner(owner);
		setVerified(verified);
		setNotes(notes);
	}
	
	/**
	 * Constructs a task object using the parameters listed below, and meant for an UNOWNED owner
	 * @param taskId id of the task
	 * @param title title of the task
	 * @param type type of task
	 * @param creator creator of the task
	 * @param note notes related to the task
	 */
	public Task(int taskId, String title, Type type, String creator, String note) { 
		setTaskId(taskId);
		setTitle(title);
		setType(type);
		setCreator(creator);
		
		
		setState(BACKLOG_NAME);
		setVerified("false");
		setOwner(UNOWNED);
		
		addNoteToList(note);
	}

	/**
	 * Interface for states in the Task State Pattern.  All 
	 * concrete task states must implement the TaskState interface.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu) 
	 */
	private interface TaskState {
		
		/**
		 * Update the Task based on the given Command
		 * An UnsupportedOperationException is thrown if the Command is not a
		 * is not a valid action for the given state.  
		 * @param c Command describing the action that will update the Task
		 * state.
		 * @throws UnsupportedOperationException if the Command is not a valid action
		 * for the given state.
		 */
		void updateState(Command c);
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		String getStateName();
	
	}

	/** Instance of the DoneState inner class */
	final class BacklogState implements TaskState {
		
		private BacklogState() {
			
		}
		
		
		/*
		 * Deals with updating state with proper transitions to different states
		 * @param c Command to transition between states
		 * @throws UnsupportedOperationException if there is an invalid command to transition
		 */
		@Override
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.CLAIM) {
				currentState = ownedState;
				setOwner(c.getOwner());
			}
			else if (c.getCommand() == CommandValue.REJECT) {
				currentState = rejectedState;
				setOwner(UNOWNED);
			}
			else {
				throw new UnsupportedOperationException("Invalid transition.");
			}
			addNoteToList(c.getNoteText());
		}

		/*
		 * returns the backlog name
		 * @returns BACKLOG_NAME
		 */
		@Override
		public String getStateName() {
			return BACKLOG_NAME;
		}
	}
	
	/** Instance of the OwnedState inner class */
	final class OwnedState implements TaskState { 
		
		
		private OwnedState() {
			
		}
		/*
		 * Deals with updating state with proper transitions to different states
		 * @param c Command to transition between states
		 * @throws UnsupportedOperationException if there is an invalid command to transition
		 */
		@Override
		public void updateState(Command c) {
			 if (c.getCommand() == CommandValue.PROCESS) {
				 currentState = processingState;
				 
			 }
			 else if (c.getCommand() == CommandValue.BACKLOG) {
				 currentState = backlogState;
				 setOwner(UNOWNED);
			 }
			 else if (c.getCommand() == CommandValue.REJECT) {
				 currentState = rejectedState;
				 setOwner(UNOWNED);
			 }
			 else {
				 throw new UnsupportedOperationException("Invalid transition.");
			 }
			 addNoteToList(c.getNoteText());
		}

		/*
		 * returns the OwnedState name
		 * @returns OWNED_NAME
		 */
		@Override
		public String getStateName() {
			return OWNED_NAME;
		}
	}
	
	/** Instance of the ProcessingState inner class */
	final class ProcessingState implements TaskState {
		
		/*
		 * Deals with updating state with proper transitions
		 */
		
		private ProcessingState() {
			
		}
		
		/*
		 * Deals with updating state with proper transitions to different states
		 * @param c Command to transition between states
		 * @throws UnsupportedOperationException if there is an invalid command to transition
		 */
		@Override
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.PROCESS) {
				currentState = processingState;
				
			}
			else if (c.getCommand() == CommandValue.VERIFY) {
				currentState = verifyingState;
			}
			else if (c.getCommand() == CommandValue.COMPLETE) {
				currentState = doneState;
				if (getType() != Type.KNOWLEDGE_ACQUISITION) {
					isVerified = true;
				}
				else {
					isVerified = false;
				}
			}
			else if (c.getCommand() == CommandValue.BACKLOG) {
				currentState = backlogState;
				setOwner(UNOWNED);
			}
			else {
				throw new UnsupportedOperationException("Invalid transition.");
			}
			addNoteToList(c.getNoteText());
		}

		/*
		 * returns the ProcessingState name
		 * @returns PROCESSING_NAME
		 */
		@Override
		public String getStateName() {
			return PROCESSING_NAME;
		}
	}
		
	/** Instance of the VerifyingState inner class */
	final class VerifyingState implements TaskState {
		
		/*
		 * Deals with updating state with proper transitions
		 */
		
		private VerifyingState() {
			
		}
		
		/*
		 * Deals with updating state with proper transitions to different states
		 * @param c Command to transition between states
		 * @throws UnsupportedOperationException if there is an invalid command to transition
		 */
		@Override
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.COMPLETE) {
				isVerified = true;
				currentState = doneState;
			}
			else if (c.getCommand() == CommandValue.PROCESS) {
				currentState = processingState;
			}
			else {
				throw new UnsupportedOperationException("Invalid transition.");
			}
			addNoteToList(c.getNoteText());
		}

		/*
		 * returns the VerifyingState name
		 * @returns VERIFYING_NAME
		 */
		@Override
		public String getStateName() {
			return VERIFYING_NAME;
		}
	}
		
	/** Instance of the DoneState inner class */
	final class DoneState implements TaskState {
		
		
		
		private DoneState() {
			
		}
		
		/*
		 * Deals with updating state with proper transitions to different states
		 * @param c Command to transition between states
		 * @throws UnsupportedOperationException if there is an invalid command to transition
		 */
		@Override
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.PROCESS) {
				isVerified = false;
				currentState = processingState;
			}
			else if (c.getCommand() == CommandValue.BACKLOG) {
				isVerified = false;
				currentState = backlogState;
				setOwner(UNOWNED);
			}
			else {
				throw new UnsupportedOperationException("Invalid transition.");
			}
			addNoteToList(c.getNoteText());
		}

		/*
		 * returns the DoneState name
		 * @returns DONE_NAME
		 */
		@Override
		public String getStateName() {
			return DONE_NAME;
		}
	}
		
	/** Instance of the RejectedState inner class */
	final class RejectedState implements TaskState {
		
		
		private RejectedState() {
			
		}
		
		/*
		 * Deals with updating state with proper transitions to different states
		 * @param c Command to transition between states
		 * @throws UnsupportedOperationException if there is an invalid command to transition
		 */
		@Override
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.BACKLOG) {
				currentState = backlogState;
				setOwner(UNOWNED);
			}
			else {
				throw new UnsupportedOperationException("Invalid transition.");
			}
			addNoteToList(c.getNoteText());
		}

		/*
		 * returns the RejectedState name
		 * @returns REJECTED_NAME
		 */
		@Override
		public String getStateName() {
			return REJECTED_NAME;
		}
	}
	
	/**
	 * Setter for the task id
	 * @param taskId taskId to be set
	 * @throws IllegalArgumentException if taskId is less than or equal to 0
	 */
	private void setTaskId(int taskId) {
		if (taskId <= 0) {
			throw new IllegalArgumentException("Invalid task information.");
		}
		this.taskId = taskId;
	}
	
	/**
	 * Setter for the task title
	 * @param title title to be set
	 * @throws IllegalArgumentException if the title is null or empty
	 */
	private void setTitle(String title) {
		if (title == null || "".equals(title)) {
			throw new IllegalArgumentException("Invalid task information.");
		}
		this.title = title;
	}
	
	/**
	 * Setter for the task type
	 * @param type type to be set for the task
	 * @throws IllegalArgumentException if the type is null
	 */
	private void setType(Type type) {
		if(type == null) {
			throw new IllegalArgumentException("Invalid task information.");
		}
		this.type = type;
	}
	
	/**
	 * Setter for the task creator
	 * @param creator creator to be set for the task
	 * @throws IllegalArgumentException if the creator is null or empty
	 */
	private void setCreator(String creator) {
		if (creator == null || "".equals(creator)) {
			throw new IllegalArgumentException("Invalid task information.");
		}
		this.creator = creator;
	}
	
	/**
	 * Setter for the task owner
	 * @param owner owner to be set for the task
	 * @throws IllegalArgumentException if the owner is null or empty
	 */
	private void setOwner(String owner) {
		if (owner == null || "".equals(owner)) {
			throw new IllegalArgumentException("Invalid task information.");
		}
		
		this.owner = owner;
	}
	 /**
	  * Setter for the verified task
	  * @param isVerified verified check for the task
	  * @throws IllegalArgumentException if the isVerified is null or equals or for KA special case
	  */
	private void setVerified(String isVerified) {
		if (isVerified == null || "".equals(isVerified)) {
			throw new IllegalArgumentException("Invalid task information.");
		}
	
		boolean checkVerified;
		
		if (isVerified.toLowerCase().equals("true")) {
			checkVerified = true;
		}
		else if (isVerified.toLowerCase().equals("false")) {
			checkVerified = false;
		}
		else {
			throw new IllegalArgumentException("Invalid task information.");
		}
		
		if(getType() == Type.KNOWLEDGE_ACQUISITION && checkVerified || !checkVerified && getStateName().equals(DONE_NAME)) {
			throw new IllegalArgumentException("Invalid task information.");
		} 
		
		this.isVerified = checkVerified;
	
	}
	
	/**
	 * Setter for the notes for the task
	 * @param notes notes to be set for the task
	 * @throws IllegalArgumentException if the notes is null
	 */
	private void setNotes(ArrayList<String> notes) {
		if (notes == null) {
			throw new IllegalArgumentException("Invalid task information.");
		}
		this.notes = notes;
	}
	
	/**
	 * Method to add notes to the list
	 * @param note to be added to note list
	 * @throws IllegalArgumentException if the note is null or empty
	 * @return index added to the end of ArrayList
	 */
	public int addNoteToList(String note) {
		if (note == null || "".equals(note)) {
			throw new IllegalArgumentException("Invalid task information.");
		}
		
		notes.add("[" + getStateName() + "] " + note);
		return notes.size() - 1;
		
	}
	 /**
	  * Getter method for the task id
	  * @return the taskId
	  */
	public int getTaskId() {
		return taskId;
	}
	 /**
	  * getter method for the task state name
	  * @return state name for the currentState
	  */
	public String getStateName() {
		return currentState.getStateName();
	}
	 /**
	  * setter method for the task state
	  * @param state state to be set
	  * @throws IllegalArgumentException if the state is null or empty
	  */
	private void setState(String state) {
		if (state == null || "".equals(state)) {
			throw new IllegalArgumentException("Invalid task information.");
		}
		
		switch(state) {

			case OWNED_NAME:
				currentState = ownedState;
				break;
				
			case BACKLOG_NAME:
				currentState = backlogState;
				break;
				
			case PROCESSING_NAME:
				currentState = processingState;
				break;
			
			case VERIFYING_NAME:
				currentState = verifyingState;
				break;
			case DONE_NAME:
				currentState = doneState;
				break;
			
			case REJECTED_NAME:
				currentState = rejectedState;
				break;
			
			default:
				throw new IllegalArgumentException("Invalid task information.");
		}
	}
	
	/**
	 * setter method for the type from string
	 * @param type type of task to be set
	 * @throws IllegalArgumentException if the type is null or empty or not a case
	 */
	private void setTypeFromString(String type) {
		if (type == null || "".equals(type)) {
			throw new IllegalArgumentException("Invalid task information.");
		}
		
		switch(type) {
			
			case FEATURE_NAME:
			case T_FEATURE:
				setType(Type.FEATURE);
				break;
			
			
			case BUG_NAME:
			case T_BUG:
				setType(Type.BUG);
				break;
			
			
			case TECHNICAL_WORK_NAME:
			case T_TECHNICAL_WORK:
				setType(Type.TECHNICAL_WORK);
				break;
				
			
			case KNOWLEDGE_ACQUISITION_NAME:
			case T_KNOWLEDGE_ACQUISITION:
				setType(Type.KNOWLEDGE_ACQUISITION);
				break;
			
			default:
				throw new IllegalArgumentException("Invalid task information.");
		
		}
		
	}
	
	/**
	 * Getter method for the type of task
	 * @return null for now but later the type of task
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Getter method for the type of task but short name
	 * @return short name of Type
	 */
	public String getTypeShortName() {
		switch(type) {
			case FEATURE:
				return T_FEATURE;
			
			case BUG:
				return T_BUG;

			case TECHNICAL_WORK:
				return T_TECHNICAL_WORK;
			
			case KNOWLEDGE_ACQUISITION:
				return T_KNOWLEDGE_ACQUISITION;
			
			default:
				return null;
		}
	}
	
	/**
	 * Getter method for the long name of the task type
	 * @return long name of type
	 */
	public String getTypeLongName() {
		switch(type) {
		case FEATURE:
			return FEATURE_NAME;
		
		case BUG:
			return BUG_NAME;

		case TECHNICAL_WORK:
			return TECHNICAL_WORK_NAME;
		
		case KNOWLEDGE_ACQUISITION:
			return KNOWLEDGE_ACQUISITION_NAME;
		
		default:
			return null;
		}
	}
	
	/**
	 * Getter method for the task owner
	 * @return owner owner of the task
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * Getter method for the title of a task
	 * @return title title of the task
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Getter method for the creator of a task
	 * @return creator creator for the task
	 */
	public String getCreator() {
		return creator;
	}
	
	/**
	 * Method for checking if the task is verified
	 * @return true for now but method will see if the task is verified
	 */
	public boolean isVerified() {
		return isVerified; // check to see if correct
	}
	
	/**
	 * Getter method for the array list of notes
	 * @return notes notes for the task
	 */
	public ArrayList<String> getNotes() {
		return notes;
	}
	
	/**
	 * Get notes for the task in a list
	 * @return notesList
	 */
	public String getNotesList() {
		String notesList = "";
		
		for (int i = 0; i < notes.size(); i++) {
			notesList += "\n" + "- " + notes.get(i);
		}
		return notesList;
	}
	
	/**
	 * Method for toString for each task
	 * @return to string method of task object
	 */
	public String toString() {
		return "* " + getTaskId() + "," + getStateName() + "," + getTitle() + "," + getTypeShortName() + "," + 
		getCreator() + "," + getOwner() + "," + isVerified() + getNotesList();
 	}
	
	/**
	 * Controls the transition of task states
	 * @param c command that helps to control the transition between the states
	 * @throws IllegalArgumentException if it satisfies the following if statements
	 */
	public void update(Command c) {
		if (getType() == Type.KNOWLEDGE_ACQUISITION && c.getCommand() == CommandValue.VERIFY && getStateName().equals(PROCESSING_NAME)) {
			throw new UnsupportedOperationException("Invalid transition.");
		}
		
		if ((c.getCommand() == CommandValue.COMPLETE || c.getCommand() == CommandValue.BACKLOG) && c.getOwner() != null) {
			throw new UnsupportedOperationException("Invalid transition.");
		}
		
		if (getStateName().equals(PROCESSING_NAME) && c.getCommand() == CommandValue.COMPLETE && getType() != Type.KNOWLEDGE_ACQUISITION)	 {
			throw new UnsupportedOperationException("Invalid transition.");
		}
		currentState.updateState(c);
	}
	
	/**
	 * Returns notes as an array
	 * @return notes as an array
	 */
	public String[] getNotesArray() {
		String[] notesArray = new String[notes.size()];
		
		for (int i = 0; i < notes.size(); i++) {
			notesArray[i] = notes.get(i);
		}
		return notesArray;
	}

}