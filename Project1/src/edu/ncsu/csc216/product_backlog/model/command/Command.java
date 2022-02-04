/**
 * 
 */
package edu.ncsu.csc216.product_backlog.model.command;

/**
 * Enables the transition of between the states using the command
 * @author tanvirarora
 */
public class Command {
	/** string note for Command*/
	private String noteText;

	/** String owner  for Command*/
	private String owner;
	
	/** String for error message */
	private static final String COMMAND_ERROR_MESSAGE = "Invalid command.";
	
	/**
	 * Enumerations for all possible states
	 * Backlog: state that the task initially start in
	 * Claim: state when the task is claimed by an owner
	 * Process: state when the task is being processed
	 * Verify: state when the task is being verified
	 * Complete: state when the task has been completed
	 * Reject: state when the task has been rejected
	 * @author tanvirarora
	 */
	public enum CommandValue { BACKLOG, CLAIM, PROCESS, VERIFY, COMPLETE, REJECT }
	
	/** instance of Command Value */
	private CommandValue commandValue;
	

	/**
	 * Constructor method for Command class
	 * @param c enumeration from CommandValue enumeration
	 * @param owner string owner of command
	 * @param noteText string note that goes with command
	 */
	public Command(CommandValue c, String owner, String noteText) {
		if (c == null) {
			throw new IllegalArgumentException(COMMAND_ERROR_MESSAGE);
		}
		this.commandValue = c;
		
		if((owner == null || "".equals(owner)) && commandValue == CommandValue.CLAIM
		|| owner != null && commandValue != CommandValue.CLAIM) {
			throw new IllegalArgumentException(COMMAND_ERROR_MESSAGE);
		}
		this.owner = owner;
		
		if(noteText == null || "".equals(noteText)) {
			throw new IllegalArgumentException(COMMAND_ERROR_MESSAGE);
		}
		this.noteText = noteText;
	}
	
	/**
	 * Command value getter method
	 * @return commandValue enumeration
	 */
	public CommandValue getCommand() {
		return commandValue;
	}
	
	/**
	 * String note getter method
	 * @return note string
	 */
	public String getNoteText() {
		return noteText;
	}
	
	/**
	 * String owner getter method
	 * @return owner string
	 */
	public String getOwner() {
		return owner;
	}
	
	
}
