package edu.ncsu.csc216.product_backlog.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.product_backlog.model.backlog.BacklogManager;
import edu.ncsu.csc216.product_backlog.model.command.Command;
import edu.ncsu.csc216.product_backlog.model.command.Command.CommandValue;
import edu.ncsu.csc216.product_backlog.model.task.Task;

/**
 * Container for the ProductBacklog that has the menu options for new products 
 * tasks, loading existing files, saving files and quitting.
 * Depending on user actions, other JPanels are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
 */
public class ProductBacklogGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Product Backlog";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the New Task XML menu item. */
	private static final String LOAD_TITLE = "Load";
	/** Text for the Load Task XML menu item. */
	private static final String SAVE_TITLE = "Save";
	/** Text for the Clear menu item. */
	private static final String CLEAR_TITLE = "Clear";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for loading a file containing products and tasks. */
	private JMenuItem itemLoad;
	/** Menu item for saving a products and tasks. */
	private JMenuItem itemSave;
	/** Menu item for clearing products and tasks. */
	private JMenuItem itemClear;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the application. */
	private JPanel panel;
	/** Constant to identify Product for CardLayout. */
	private static final String PRODUCT_PANEL = "TaskListPanel";
	/** Constant to identify BacklogPanel for CardLayout. */
	private static final String BACKLOG_PANEL = "BacklogPanel";
	/** Constant to identify OwnedPanel for CardLayout. */
	private static final String OWNED_PANEL = "OwnedPanel";
	/** Constant to identify ProcessingPanel for CardLayout. */
	private static final String PROCESSING_PANEL = "ProcessingPanel";
	/** Constant to identify VerifyingPanel for CardLayout. */
	private static final String VERIFYING_PANEL = "VerifyingPanel";
	/** Constant to identify DonePanel for CardLayout. */
	private static final String DONE_PANEL = "DonePanel";
	/** Constant to identify RejectedPanel for CardLayout. */
	private static final String REJECTED_PANEL = "RejectedPanel";
	/** Constant to identify CreateTaskPanel for CardLayout. */
	private static final String CREATE_TASK_PANEL = "CreateTaskPanel";
	/** Product panel - we only need one instance, so it's final. */
	private final ProductPanel pnlProduct = new ProductPanel();
	/** Backlog panel - we only need one instance, so it's final. */
	private final BacklogPanel pnlBacklog = new BacklogPanel();
	/** Owned panel - we only need one instance, so it's final. */
	private final OwnedPanel pnlOwned = new OwnedPanel();
	/** Processing panel - we only need one instance, so it's final. */
	private final ProcessingPanel pnlProcessing = new ProcessingPanel();
	/** Verifying panel - we only need one instance, so it's final. */
	private final VerifyingPanel pnlVerifying = new VerifyingPanel();
	/** Done panel - we only need one instance, so it's final. */
	private final DonePanel pnlDone = new DonePanel();
	/** Rejected panel - we only need one instance, so it's final. */
	private final RejectedPanel pnlRejected = new RejectedPanel();
	/** Add Task Item panel - we only need one instance, so it's final. */
	private final CreateTaskPanel pnlCreateTask = new CreateTaskPanel();
	/** Reference to CardLayout for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	/**
	 * Constructs a {@link ProductBacklogGUI} object that will contain a {@link JMenuBar} and a
	 * JPanel that will hold different possible views of the data in
	 * the BacklogManager.
	 */
	public ProductBacklogGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlProduct, PRODUCT_PANEL);
		panel.add(pnlBacklog, BACKLOG_PANEL);
		panel.add(pnlOwned, OWNED_PANEL);
		panel.add(pnlProcessing, PROCESSING_PANEL);
		panel.add(pnlVerifying, VERIFYING_PANEL);
		panel.add(pnlDone, DONE_PANEL);
		panel.add(pnlRejected, REJECTED_PANEL);
		panel.add(pnlCreateTask, CREATE_TASK_PANEL);
		cardLayout.show(panel, PRODUCT_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading a file
	 * containing task items or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemLoad = new JMenuItem(LOAD_TITLE);
		itemSave = new JMenuItem(SAVE_TITLE);
		itemClear = new JMenuItem(CLEAR_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemLoad.addActionListener(this);
		itemSave.addActionListener(this);
		itemClear.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemClear.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemLoad);
		menu.add(itemSave);
		menu.add(itemClear);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given ActionEvent.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Use BacklogManager singleton to create/get the sole instance.
		BacklogManager model = BacklogManager.getInstance();
		if (e.getSource() == itemClear) {
			//Clear products from model
			try {
				model.clearProducts();
				itemSave.setEnabled(false);
				pnlProduct.updateProducts();
				cardLayout.show(panel, PRODUCT_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			}	
		} else if (e.getSource() == itemLoad) {
			//Load an existing file
			try {
				model.loadFromFile(getFileName(true));
				itemClear.setEnabled(true);
				pnlProduct.updateProducts();
				cardLayout.show(panel, PRODUCT_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to load file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSave) {
			//Save products and tasks lists to file
			try {
				model.saveToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveToFile(getFileName(false));
				System.exit(0);  //Ignore FindBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a JFileChooser
	 * object.
	 * @param load true if loading a file, false if saving
	 * @return the file name selected through JFileChooser
	 * @throws IllegalStateException if no file name provided
	 */
	private String getFileName(boolean load) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			fc.setDialogTitle("Load Products");
			returnVal = fc.showOpenDialog(this);
		} else {
			fc.setDialogTitle("Save Products");
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File file = fc.getSelectedFile();
		return file.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the ProductBacklog application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new ProductBacklogGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows the products and associated task lists.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class ProductPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Label for selecting current product */
		private JLabel lblCurrentProduct;
		/** Combo box for Product list */
		private JComboBox<String> comboProductList;
		/** Button to add a product */
		private JButton btnAddProduct;
		/** Button to edit the selected product */
		private JButton btnEditProduct;
		/** Button to delete the selected product */
		private JButton btnDeleteProduct;
		
		/** Button for creating a new task */
		private JButton btnAdd;
		/** Button for editing the selected task in the list */
		private JButton btnEdit;
		/** Button for deleting the selected task in the list */
		private JButton btnDelete;
		/** JTable for displaying the list of tasks */
		private JTable tableTasks;
		/** TableModel for Tasks */
		private TaskTableModel tableModel;
		
		/**
		 * Creates the Product panel.
		 */
		public ProductPanel() {
			super(new GridBagLayout());
			
			//Set up the JPanel that will hold action buttons
			lblCurrentProduct = new JLabel("Current Product");
			comboProductList = new JComboBox<String>();
			comboProductList.addActionListener(this);
			btnAddProduct = new JButton("Add Product");
			btnAddProduct.addActionListener(this);
			btnEditProduct = new JButton("Edit Product");
			btnEditProduct.addActionListener(this);
			btnDeleteProduct = new JButton("Delete Product");
			btnDeleteProduct.addActionListener(this);
								
			btnAdd = new JButton("Add Task");
			btnAdd.addActionListener(this);
			btnEdit = new JButton("Edit Task");
			btnEdit.addActionListener(this);
			btnDelete = new JButton("Delete Task");
			btnDelete.addActionListener(this);
			
			JPanel pnlProductSelection = new JPanel();
			pnlProductSelection.setLayout(new GridLayout(1, 2));
			pnlProductSelection.add(lblCurrentProduct);
			pnlProductSelection.add(comboProductList);
			
			JPanel pnlProductActions = new JPanel();
			pnlProductActions.setLayout(new GridLayout(1, 3));
			pnlProductActions.add(btnAddProduct);
			pnlProductActions.add(btnEditProduct);
			pnlProductActions.add(btnDeleteProduct);
			
			JPanel pnlTaskActions = new JPanel();
			pnlTaskActions.setLayout(new GridLayout(1, 3));
			pnlTaskActions.add(btnAdd);
			pnlTaskActions.add(btnEdit);
			pnlTaskActions.add(btnDelete);
						
			//Set up table
			tableModel = new TaskTableModel();
			tableTasks = new JTable(tableModel);
			tableTasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableTasks.setPreferredScrollableViewportSize(new Dimension(500, 500));
			tableTasks.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(tableTasks);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlProductSelection, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlProductActions, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlTaskActions, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 20;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = GridBagConstraints.REMAINDER;
			c.gridwidth = GridBagConstraints.REMAINDER;
			add(listScrollPane, c);
			
			updateProducts();
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == comboProductList) {
				int idx = comboProductList.getSelectedIndex();
				
				if (idx == -1) {
					updateProducts();
				} else {				
					String productName = comboProductList.getItemAt(idx);
					BacklogManager.getInstance().loadProduct(productName);
				}
				updateProducts();
			} else if (e.getSource() == btnAdd) {
				//If the add button is clicked switch to the createTaskPanel
				pnlCreateTask.setProductName(BacklogManager.getInstance().getProductName());
				cardLayout.show(panel, CREATE_TASK_PANEL);
			} else if (e.getSource() == btnDelete) {
				//If the delete button is clicked, delete the task
				int row = tableTasks.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "No task selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						BacklogManager.getInstance().deleteTaskById(id);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid id");
					}
				}
				updateProducts();
			} else if (e.getSource() == btnEdit) {
				//If the edit button is clicked, switch panel based on state
				int row = tableTasks.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "No task selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						String stateName = BacklogManager.getInstance().getTaskById(id).getStateName();
						
						if (stateName.equals(Task.BACKLOG_NAME)) {
							cardLayout.show(panel, BACKLOG_PANEL);
							pnlBacklog.setInfo(id);
						} else if (stateName.equals(Task.OWNED_NAME)) {
							cardLayout.show(panel, OWNED_PANEL);
							pnlOwned.setInfo(id);
						} else if (stateName.equals(Task.PROCESSING_NAME)) {
							cardLayout.show(panel, PROCESSING_PANEL);
							pnlProcessing.setInfo(id);
						} else if (stateName.equals(Task.VERIFYING_NAME)) {
							cardLayout.show(panel, VERIFYING_PANEL);
							pnlVerifying.setInfo(id);
						} else if (stateName.equals(Task.DONE_NAME)) {
							cardLayout.show(panel, DONE_PANEL);
							pnlDone.setInfo(id);
						} else if (stateName.equals(Task.REJECTED_NAME)) {
							cardLayout.show(panel, REJECTED_PANEL);
							pnlRejected.setInfo(id);
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid id");
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid id");
					}
				}
				updateProducts();
			} else if (e.getSource() == btnAddProduct) {
				try {
					String productName = (String) JOptionPane.showInputDialog(this, "Product Name?", "Create New Product", JOptionPane.QUESTION_MESSAGE);
					BacklogManager.getInstance().addProduct(productName);
					itemSave.setEnabled(true);
					updateProducts();
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, iae.getMessage());
				}
			} else if (e.getSource() == btnEditProduct) {
				try {
					String productName = (String) JOptionPane.showInputDialog(this, "Update Product Name?", "Edit Product", JOptionPane.QUESTION_MESSAGE, null, null, BacklogManager.getInstance().getProductName());
					BacklogManager.getInstance().editProduct(productName);
					itemSave.setEnabled(true);
					updateProducts();
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, iae.getMessage());
				}
			} else if (e.getSource() == btnDeleteProduct) {
				try {
					BacklogManager.getInstance().deleteProduct();
					updateProducts();
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, iae.getMessage());
				}
			}
			
			//On all paths, update the GUI!
			ProductBacklogGUI.this.repaint();
			ProductBacklogGUI.this.validate();
		}
		
		/**
		 * Updates the product and task list.
		 */
		public void updateProducts() {
			tableModel.updateData();
			
			String productName = BacklogManager.getInstance().getProductName();
			
			if (productName == null) {
				productName = "Create a Product";
				btnAdd.setEnabled(false);
				btnDelete.setEnabled(false);
				btnEdit.setEnabled(false);
				btnAddProduct.setEnabled(true);
				btnEditProduct.setEnabled(false);
				btnDeleteProduct.setEnabled(false);
			} else {
				btnAdd.setEnabled(true);
				btnDelete.setEnabled(true);
				btnEdit.setEnabled(true);
				btnAddProduct.setEnabled(true);
				btnEditProduct.setEnabled(true);
				btnDeleteProduct.setEnabled(true);
			}
			
			comboProductList.removeAllItems();
			String [] productList = BacklogManager.getInstance().getProductList();
			for (int i = 0; i < productList.length; i++) {
				comboProductList.addItem(productList[i]);
			}
			
			productName = BacklogManager.getInstance().getProductName(); //Get name again
			if (productName != null) {
				comboProductList.setSelectedItem(productName);
			} else {
				productName = "Create a Product";
			}
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Product: " + productName);
			setBorder(border);
			setToolTipText("Product: " + productName);
		}
		
		/**
		 * TaskTableModel is the object underlying the JTable object that displays
		 * the list of Tasks to the user.
		 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
		 */
		private class TaskTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Task ID", "Task State", "Task Type", "Task Title"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the TaskTableModel by requesting the latest information
			 * from the BacklogManager.
			 */
			public TaskTableModel() {
				updateData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col column in the table
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row row in the table
			 * @param col column in the table
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row location to modify the data.
			 * @param col location to modify the data.
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with Task information from the BacklogManager.
			 */
			private void updateData() {
				BacklogManager m = BacklogManager.getInstance();
				data = m.getTasksAsArray();
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a backloged Task.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class BacklogPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** TaskPanel that presents the Task's information to the user */
		private TaskInfoPanel pnlTaskInfo;
		/** Label for owner id field */
		private JLabel lblOwner;
		/** Text field for owner id */
		private JTextField txtOwner;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Claim action */
		private JButton btnClaim;
		/** Reject action */
		private JButton btnReject;
		/** Cancel action */
		private JButton btnCancel;
		/** Current Task's id */
		private int taskItemId;
		
		/**
		 * Constructs the JPanel for editing a Task in the NewState.
		 */
		public BacklogPanel() {
			pnlTaskInfo = new TaskInfoPanel();
			lblOwner = new JLabel("Owner");
			txtOwner = new JTextField(30);
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			btnClaim = new JButton("Claim Task");
			btnReject = new JButton("Reject Task");
			btnCancel = new JButton("Cancel");
			
			btnClaim.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskInfo, c);
			
			JPanel pnlBacklogInfo = new JPanel();
			pnlBacklogInfo.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlBacklogInfo.setBorder(border);
			pnlBacklogInfo.setToolTipText("Edit Task");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlBacklogInfo.add(lblOwner, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlBacklogInfo.add(txtOwner, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlBacklogInfo.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlBacklogInfo.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlBacklogInfo, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnClaim, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnReject, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the TaskPanel with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskInfo.setTaskInfo(this.taskItemId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnClaim) {
				//Take care of note.
				String note = txtNote.getText();
				String owner = txtOwner.getText();
				//Otherwise, try a Command.  If command fails, go back to task list
				try {
					Command c = new Command(Command.CommandValue.CLAIM, owner, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			} else if (e.getSource() == btnReject) {
				//Take care of note.
				String note = txtNote.getText();
				//Otherwise, try a Command.  If command fails, go back to task list
				try {
					Command c = new Command(Command.CommandValue.REJECT, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, PRODUCT_PANEL);
				pnlProduct.updateProducts();
				ProductBacklogGUI.this.repaint();
				ProductBacklogGUI.this.validate();
				//Reset fields
				txtOwner.setText("");
				txtNote.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an owned task.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class OwnedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** TaskPanel that presents the Task's information to the user */
		private TaskInfoPanel pnlTaskInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Process action */
		private JButton btnProcess;
		/** Reject action */
		private JButton btnReject;
		/** Backlog action */
		private JButton btnBacklog;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item's id */
		private int taskId;

		/**
		 * Constructs a JPanel for editing a Task in the OwnedState.
		 */
		public OwnedPanel() {
			pnlTaskInfo = new TaskInfoPanel();
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnProcess = new JButton("Process Task");
			btnReject = new JButton("Reject Task");
			btnBacklog = new JButton("Backlog Task");
			btnCancel = new JButton("Cancel");
			
			btnProcess.addActionListener(this);
			btnReject.addActionListener(this);
			btnBacklog.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskInfo, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("Edit Task");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridLayout(1, 4));
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnProcess, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnReject, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnBacklog, c);
			c = new GridBagConstraints();
			c.gridx = 3;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the TaskPanel with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setInfo(int taskItemId) {
			this.taskId = taskItemId;
			pnlTaskInfo.setTaskInfo(this.taskId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() == btnProcess) {
				//Try a command.  If problem, go back to task item list.
				try {
					Command c = new Command(Command.CommandValue.PROCESS, null, note);
					BacklogManager.getInstance().executeCommand(taskId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			} else if (e.getSource() == btnReject) {
				//Try a command.  If problem, go back to task item list.
				try {
					Command c = new Command(Command.CommandValue.REJECT, null, note);
					BacklogManager.getInstance().executeCommand(taskId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			} else if (e.getSource() == btnBacklog) {				
				//Try a command.  If problem, go back to task item list.
				try {
					Command c = new Command(Command.CommandValue.BACKLOG, null, note);
					BacklogManager.getInstance().executeCommand(taskId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, PRODUCT_PANEL);
				pnlProduct.updateProducts();
				ProductBacklogGUI.this.repaint();
				ProductBacklogGUI.this.validate();
				//Reset fields
				txtNote.setText("");
			}
			//Otherwise, stay on panel
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a processing task.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class ProcessingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** TaskPanel that presents the Task's information to the user */
		private TaskInfoPanel pnlTaskInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Process action */
		private JButton btnProcess;
		/** Verify action */
		private JButton btnVerify;
		/** Complete action */
		private JButton btnComplete;
		/** Backlog action */
		private JButton btnBacklog;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item id */
		private int taskItemId;

		/**
		 * Constructs a JFrame for editing a Task in the ProcessingState.
		 */
		public ProcessingPanel() {
			pnlTaskInfo = new TaskInfoPanel();
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnProcess = new JButton("Add Note");
			btnVerify = new JButton("Verify Task");
			btnComplete = new JButton("Complete Task");
			btnBacklog = new JButton("Backlog Task");
			btnCancel = new JButton("Cancel");
			
			btnProcess.addActionListener(this);
			btnVerify.addActionListener(this);
			btnComplete.addActionListener(this);
			btnBacklog.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskInfo, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("Edit Task");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnProcess, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnVerify, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnComplete, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnBacklog, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the TaskPanel with the given task data.
		 * @param taskItemId id of the task
		 */
		public void setInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskInfo.setTaskInfo(this.taskItemId);
			
			//Rest buttons to enabled
			btnVerify.setEnabled(true);
			btnComplete.setEnabled(true);
			
			//Disable appropriate button depending on task type			
			Task t = BacklogManager.getInstance().getTaskById(taskItemId);
			if (t.getType() == Task.Type.KNOWLEDGE_ACQUISITION) {
				btnVerify.setEnabled(false);
			} else {
				btnComplete.setEnabled(false);
			}
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() == btnProcess) {
				//Try command.  If problem, go to task list.
				try {
					Command c = new Command(Command.CommandValue.PROCESS, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			} else if (e.getSource() == btnVerify) {
				//Try command.  If problem, go to task list.
				try {
					Command c = new Command(Command.CommandValue.VERIFY, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			} else if (e.getSource() == btnComplete) {
				//Try a command.  If problem, go back to task list.
				try {
					Command c = new Command(Command.CommandValue.COMPLETE, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			} else if (e.getSource() == btnBacklog) {
				//Otherwise, try a Command.  If command fails, go back to task list
				try {
					Command c = new Command(Command.CommandValue.BACKLOG, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, PRODUCT_PANEL);
				pnlProduct.updateProducts();
				ProductBacklogGUI.this.repaint();
				ProductBacklogGUI.this.validate();
				//Reset fields
				txtNote.setText("");
			}
			//Otherwise, stay on panel
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a verifying task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class VerifyingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** TaskPanel that presents the Task's information to the user */
		private TaskInfoPanel pnlTaskInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Process action */
		private JButton btnProcess;
		/** Complete action */
		private JButton btnComplete;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item id */
		private int taskItemId;

		/**
		 * Constructs a JPanel for editing a Task in the VerifyingState.
		 */
		public VerifyingPanel() {
			pnlTaskInfo = new TaskInfoPanel();
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			btnProcess = new JButton("Return Task to Owner");
			btnComplete = new JButton("Task Verified");
			btnCancel = new JButton("Cancel");
			
			btnProcess.addActionListener(this);
			btnComplete.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskInfo, c);
			
			JPanel pnlNewInfo = new JPanel();
			pnlNewInfo.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNewInfo.setBorder(border);
			pnlNewInfo.setToolTipText("Edit Task");

			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlNewInfo.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNewInfo, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnProcess, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnComplete, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the TaskPanel with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskInfo.setTaskInfo(this.taskItemId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() == btnProcess) {
				//Try command.  If problem, return to task list.
				try {
					Command c = new Command(Command.CommandValue.PROCESS, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			} else if (e.getSource() == btnComplete) {
				//Try command.  If problem, return to task list.
				try {
					Command c = new Command(Command.CommandValue.COMPLETE, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			} 
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, PRODUCT_PANEL);
				pnlProduct.updateProducts();
				ProductBacklogGUI.this.repaint();
				ProductBacklogGUI.this.validate();
				//Reset fields
				txtNote.setText("");
			}
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a done task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class DonePanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** TaskPanel that presents the Task's information to the user */
		private TaskInfoPanel pnlTaskInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Process action */
		private JButton btnProcess;
		/** Backlog action */
		private JButton btnBacklog;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item id */
		private int taskItemId;

		/**
		 * Constructs a JPanel for editing a Task in the DoneState.
		 */
		public DonePanel() {
			pnlTaskInfo = new TaskInfoPanel();
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnProcess = new JButton("Return Task to Owner");
			btnBacklog = new JButton("Backlog Task");
			btnCancel = new JButton("Cancel");
			
			btnProcess.addActionListener(this);
			btnBacklog.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskInfo, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("Edit Task");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);

			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnProcess, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnBacklog, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the TaskPanel with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskInfo.setTaskInfo(this.taskItemId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() == btnProcess) {
				//Try command.  If problem, go back to task item list
				try {
					Command c = new Command(CommandValue.PROCESS, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			} else if (e.getSource() == btnBacklog) {
				//Otherwise, try a Command.  If command fails, go back to task item list
				try {
					Command c = new Command(Command.CommandValue.BACKLOG, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, PRODUCT_PANEL);
				pnlProduct.updateProducts();
				ProductBacklogGUI.this.repaint();
				ProductBacklogGUI.this.validate();
				//Reset fields
				txtNote.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct developer input.
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a rejected task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class RejectedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** TaskPanel that presents the Task's information to the user */
		private TaskInfoPanel pnlTaskInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Backlog action */
		private JButton btnBacklog;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item id */
		private int taskItemId;

		/**
		 * Constructs a JPanel for editing a Task in the RejectedState.
		 */
		public RejectedPanel() {
			pnlTaskInfo = new TaskInfoPanel();
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnBacklog = new JButton("Backlog Task");
			btnCancel = new JButton("Cancel");
			
			btnBacklog.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskInfo, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("Edit Task");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);

			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnBacklog, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the TaskPanel with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskInfo.setTaskInfo(this.taskItemId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() == btnBacklog) {
				//Try command.  If problem, go back to task list
				try {
					Command c = new Command(CommandValue.BACKLOG, null, note);
					BacklogManager.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid information");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back task item list
				cardLayout.show(panel, PRODUCT_PANEL);
				pnlProduct.updateProducts();
				ProductBacklogGUI.this.repaint();
				ProductBacklogGUI.this.validate();
				//Reset fields
				txtNote.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct developer input.
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows information about the task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class TaskInfoPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for id */
		private JLabel lblId;
		/** Field for id */
		private JTextField txtId;
		/** Label for state */
		private JLabel lblState;
		/** Field for state */
		private JTextField txtState;
		/** Label for type */
		private JLabel lblType;
		/** Field for type */
		private JTextField txtType;
		/** Label for title */
		private JLabel lblTitle;
		/** Field for title */
		private JTextField txtTitle;
		/** Label for creator */
		private JLabel lblCreator;
		/** Field for creator */
		private JTextField txtCreator;
		/** Label for owner */
		private JLabel lblOwner;
		/** Field for owner */
		private JTextField txtOwner;
		/** Label for notes */
		private JLabel lblNotes;
		/** JTextArea for displaying notes */
		private JTextArea txtNotes;
		/** Scroll pane for table */
		private JScrollPane scrollNotes;
		
		/** 
		 * Construct the panel for the task item information.
		 */
		public TaskInfoPanel() {
			super(new GridBagLayout());
			
			lblId = new JLabel("Task Id");
			lblState = new JLabel("Task State");
			lblType = new JLabel("Task Type");
			lblTitle = new JLabel("Task Title");
			lblCreator = new JLabel("Creator");
			lblOwner = new JLabel("Owner");
			lblNotes = new JLabel("Notes");
			
			txtId = new JTextField(15);
			txtState = new JTextField(15);
			txtType = new JTextField(15);
			txtTitle = new JTextField(30);
			txtCreator = new JTextField(15);
			txtOwner = new JTextField(15);
			txtNotes = new JTextArea(10, 15);
			
			txtId.setEditable(false);
			txtState.setEditable(false);
			txtType.setEditable(false);
			txtTitle.setEditable(false);
			txtCreator.setEditable(false);
			txtOwner.setEditable(false);
			txtNotes.setEditable(false);
			
			scrollNotes = new JScrollPane(txtNotes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Task Information");
			this.setBorder(border);
			this.setToolTipText("Task Information");
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblTitle, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtTitle, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblId, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtId, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblState, c);
			c = new GridBagConstraints();
			c.gridx = 3;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtState, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblCreator, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtCreator, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblOwner, c);
			c = new GridBagConstraints();
			c.gridx = 3;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtOwner, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblType, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtType, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 4;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblNotes, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.ipady = 50;
			c.gridheight = 30;
			c.gridwidth = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(scrollNotes, c);
		}
		
		/**
		 * Adds information about the task item to the display.  
		 * @param taskItemId the id for the task item to display information about.
		 */
		public void setTaskInfo(int taskItemId) {
			//Get the task from the model
			Task t = BacklogManager.getInstance().getTaskById(taskItemId);
			if (t == null) {
				//If the task item doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid task id");
				cardLayout.show(panel, PRODUCT_PANEL);
				ProductBacklogGUI.this.repaint();
				ProductBacklogGUI.this.validate();
			} else {
				//Otherwise, set all of the fields with the information
				txtId.setText("" + t.getTaskId());
				txtState.setText(t.getStateName());
				txtTitle.setText(t.getTitle());
				txtCreator.setText(t.getCreator());
				txtOwner.setText(t.getOwner());
				String typeString = t.getTypeLongName();
				if (typeString == null) {
					txtType.setText("");
				} else {
					txtType.setText("" + typeString);
				}
				txtNotes.setText(t.getNotesList());
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * allows for creation of a new task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class CreateTaskPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Label - product */
		private JLabel lblProduct;
		/** JTextField - product */
		private JTextField txtProduct;
		
		/** Label for identifying title text field */
		private JLabel lblTitle;
		/** Text field for entering title information */
		private JTextField txtTitle;
		
		/** Label for selecting a type */
		private JLabel lblType;
		/** ComboBox for type */
		private JComboBox<String> comboType;
		
		/** Label for identifying creator text field */
		private JLabel lblCreator;
		/** Text field for entering creator id */
		private JTextField txtCreator;
		
		/** Label for note text field */
		private JLabel lblNote;
		/** Text field for note information*/
		private JTextArea txtNote;
		
		/** Button to add a task item */
		private JButton btnAdd;
		/** Button for canceling add action */
		private JButton btnCancel;
		
		/** Possible types to list in the combo box */
		private String [] types = {"Feature", "Bug", "Technical Work", "Knowledge Acquisition"};
		
		/**
		 * Creates the JPanel for adding new task to the 
		 * backlog for the current product.
		 */
		public CreateTaskPanel() {
			super(new GridBagLayout());  
			
			//Construct widgets
			lblProduct = new JLabel("Product");
			txtProduct = new JTextField(15);
			
			lblTitle = new JLabel("Title");
			txtTitle = new JTextField(15);
			
			lblType = new JLabel("Task Type");
			comboType = new JComboBox<String>(types);
			comboType.setSelectedIndex(0);
			
			lblCreator = new JLabel("Task Creator");
			txtCreator = new JTextField(15);
			
			lblNote = new JLabel("Task Notes");
			txtNote = new JTextArea(10, 15);
			
			btnAdd = new JButton("Add Task to Backlog");
			btnCancel = new JButton("Cancel");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			GridBagConstraints c = new GridBagConstraints();
			
			//Row 0 - Product
			JPanel row0 = new JPanel();
			row0.setLayout(new GridLayout(1, 2));
			row0.add(lblProduct);
			row0.add(txtProduct);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = 1;
			c.gridwidth = 1;
			add(row0, c);
			
			//Row 1 - Title
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblTitle);
			row1.add(txtTitle);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = 1;
			c.gridwidth = 1;
			add(row1, c);
			
			//Row 2 - Type
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblType);
			row2.add(comboType);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = 1;
			c.gridwidth = 1;
			add(row2, c);
			
			//Row 3 - Creator
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(lblCreator);
			row3.add(txtCreator);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = 1;
			c.gridwidth = 1;
			add(row3, c);
			
			//Row 4 - Message
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(lblNote);
			row4.add(txtNote);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = 4;
			c.gridwidth = 1;
			add(row4, c);
						
			//Row 5 - Buttons
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 2));
			row5.add(btnAdd);
			row5.add(btnCancel);
			
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = GridBagConstraints.REMAINDER;
			c.gridwidth = GridBagConstraints.REMAINDER;
			add(row5, c);
		}
		
		/**
		 * Sets the product name
		 * @param productName name of the product to display
		 */
		public void setProductName(String productName) {
			txtProduct.setText(productName);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean done = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				//Add task to the list
				String title = txtTitle.getText();
				String creator = txtCreator.getText();
				String note = txtNote.getText();
				
				//Get type
				Task.Type type = null;
				int idx = comboType.getSelectedIndex();
				if (idx < 0 || idx >= types.length) {
					//If problem, show error and remain on page.
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid task information");
					done = false;
				} else {
					switch (idx) {
					case 0: type = Task.Type.FEATURE; break;
					case 1: type = Task.Type.BUG; break;
					case 2: type = Task.Type.TECHNICAL_WORK; break;
					case 3: type = Task.Type.KNOWLEDGE_ACQUISITION; break;
					default: type = null;
					}
				}
				//Get instance of model and add task
				try {
					BacklogManager.getInstance().addTaskToProduct(title, type, creator, note);
				} catch (IllegalArgumentException exp) {
					done = false;
					JOptionPane.showMessageDialog(ProductBacklogGUI.this, "Invalid task information.");
				}
			} 
			if (done) {
				//All buttons lead to back task list
				cardLayout.show(panel, PRODUCT_PANEL);
				pnlProduct.updateProducts();
				ProductBacklogGUI.this.repaint();
				ProductBacklogGUI.this.validate();
				//Reset fields
				txtTitle.setText("");
				txtCreator.setText("");
				txtNote.setText("");
				comboType.setSelectedIndex(0);
			}
		}
	}
}
