package gui.frame.tab.project;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.JTextArea;

import project.data.Data;
import project.data.Data.DataType;
import project.data.center.DataCenter;
import project.data.center.DataOperations;
import project.data.command.Command;
import project.data.command.CommandManager;
import project.data.command.CommandManager.CommandType;
import project.file.FileManager;
import gui.frame.Core;
import gui.frame.tab.ProjectTabs;


public class ProjectPanel 
{
	private JPanel projectPanel;
	
	private JTextField mainSearchBar;
	private DefaultListModel<Data> mainlistModel;
	private JList<Data> mainSearchList;
	private JScrollPane mainScrollPane;
	private JLabel mainNewLabel;
	private JLabel existingMainLabel;
	private JButton mainAddButton;
	
	private JTextField commandSearchBar;
	private DefaultListModel<Command> commandListModel;
	private JList<Command> commandSearchList;
	private JScrollPane commandScrollPane;
	private JLabel commandLabel;
	private JLabel mainExistingNameLabel;
	private JButton commandAddButton;
	
	private JToolBar toolBar;
	private JToggleButton diskToggle;
	private JToggleButton userToggle;
	private JToggleButton groupToggle;
	
	private Font boldFont;
	private JScrollPane previewScrollPane;
	private JTextArea previewTextArea;
	private JLabel previewLbl;
	private JButton generateButton;
	private Font italicFont;
	
	
	private String projectName = "";
	private DataType dataType;
	private Command selectedCommand;
	private ArrayList<String> disks;
	private boolean userSelected = false;
	private boolean groupSelected = false;
	private boolean diskSelected = false;
	
	public JPanel getProjectPane() {return projectPanel;}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	
	public ProjectPanel(String projectName)
	{
		initialize(projectName);
	}
	
	private void initialize(String projectName)
	{
		this.projectName = projectName;
		dataType = DataType.USER;
		italicFont = new Font("Tahoma", Font.ITALIC,14);
		boldFont = new Font("Tahoma", Font.BOLD,14);
		disks = new ArrayList<String>();
		
		setupComponents();
	}
	
	private void setupComponents()
	{			
		projectPanel = new JPanel();
		projectPanel.setName(projectName);
		projectPanel.setBounds(ProjectTabs.getTabbedPane().getBounds());
		GridBagLayout gbl_panelUser = new GridBagLayout();
		gbl_panelUser.columnWidths = new int[]{130, 400, 100, 400, 0};
		gbl_panelUser.rowHeights = new int[]{0, 35, 150, 23, 27, 35, 200, 36, 149};
		gbl_panelUser.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelUser.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		projectPanel.setLayout(gbl_panelUser);
		
		Action enterKeyStroke = new AbstractAction()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
			{
				enterKeyStrokeFire();
			}
		};
		
		mainlistModel = new DefaultListModel<Data>();				
		mainSearchList = new JList<Data>(mainlistModel); 
		mainSearchList.setCellRenderer(new MainListCellRenderer());
		mainSearchList.setFont(new Font("Tahoma", Font.BOLD, 12));
		mainSearchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainSearchList.setLayoutOrientation(JList.VERTICAL);
		mainSearchList.setVisibleRowCount(-1);
		mainSearchList.addMouseListener(new MouseAdapter() 
		{
          public void mouseClicked(MouseEvent e) 
          {
              if(e.getClickCount() == 2)
              {
                  int selectedIndex = mainSearchList.getSelectedIndex();
                  
                  if(selectedIndex != -1)
                  {
                	  mainSearchBar.setText(mainSearchList.getSelectedValue().getName());
                	  mainSearchBar.requestFocus();
                	  mainSearchBar.selectAll();
                  }
                  setComponentsVisibility();
              }
          }
		});
		mainSearchBar = new JTextField();
		mainSearchBar.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterKeyStroke");
		mainSearchBar.getActionMap().put("enterKeyStroke", enterKeyStroke);
		mainSearchBar.setName("mainSearchBar");
		mainSearchBar.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_userSearchbar = new GridBagConstraints();
		gbc_userSearchbar.insets = new Insets(0, 0, 5, 5);
		gbc_userSearchbar.fill = GridBagConstraints.BOTH;
		gbc_userSearchbar.gridx = 1;
		gbc_userSearchbar.gridy = 1;
		mainSearchBar.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{	
				updateMainListData();
				setComponentsVisibility();			
			}		
			@Override 
			public void removeUpdate(DocumentEvent e) 
		    {
				updateMainListData();
				setComponentsVisibility();					
		    }	    
			@Override 
			public void changedUpdate(DocumentEvent e) 
			{ 
			}
		});
		
		mainNewLabel = new JLabel("");
		mainNewLabel.setVisible(false);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.anchor = GridBagConstraints.WEST;
		gbc_toolBar.insets = new Insets(0, 0, 5, 5);
		gbc_toolBar.gridx = 1;
		gbc_toolBar.gridy = 0;
		projectPanel.add(toolBar, gbc_toolBar);
		
		userToggle = new JToggleButton("User");
		userToggle.setName("USER");
		userToggle.setToolTipText("Search User");
		userToggle.setSelected(true);
		userToggle.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				toggleButtonFire(e);
			}		
		});
		toolBar.add(userToggle);
		
		groupToggle = new JToggleButton("Group");
		groupToggle.setName("GROUP");
		groupToggle.setToolTipText("Search Group");
		groupToggle.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				toggleButtonFire(e);
			}		
		});
		toolBar.add(groupToggle);
		
		diskToggle = new JToggleButton("DiskPart");
		diskToggle.setName("DISK");
		diskToggle.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				toggleButtonFire(e);
			}		
		});
		diskToggle.setToolTipText("Search Disk");
		toolBar.add(diskToggle);
		
		mainNewLabel.setText("(New User)");
		mainNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_newUserLabel = new GridBagConstraints();
		gbc_newUserLabel.anchor = GridBagConstraints.EAST;
		gbc_newUserLabel.insets = new Insets(0, 0, 5, 5);
		gbc_newUserLabel.gridx = 0;
		gbc_newUserLabel.gridy = 1;
		projectPanel.add(mainNewLabel, gbc_newUserLabel);
		projectPanel.add(mainSearchBar, gbc_userSearchbar);
		
		existingMainLabel = new JLabel();
		existingMainLabel.setText("(Existing Users)");
		existingMainLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_existingMainLabel = new GridBagConstraints();
		gbc_existingMainLabel.anchor = GridBagConstraints.NORTHEAST;
		gbc_existingMainLabel.insets = new Insets(0, 0, 5, 5);
		gbc_existingMainLabel.gridx = 0;
		gbc_existingMainLabel.gridy = 2;
		projectPanel.add(existingMainLabel, gbc_existingMainLabel);
		
		mainAddButton = new JButton("Create User");
		mainAddButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				mainAddButtonFire();
			}
		});
		GridBagConstraints gbc_addUserBtn = new GridBagConstraints();
		gbc_addUserBtn.fill = GridBagConstraints.BOTH;
		gbc_addUserBtn.anchor = GridBagConstraints.WEST;
		gbc_addUserBtn.insets = new Insets(0, 0, 5, 5);
		gbc_addUserBtn.gridx = 2;
		gbc_addUserBtn.gridy = 1;
		projectPanel.add(mainAddButton, gbc_addUserBtn);
		
		mainScrollPane = new JScrollPane(mainSearchList);
		mainScrollPane.setPreferredSize(new Dimension(350,150));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;		
		projectPanel.add(mainScrollPane, gbc_scrollPane);
		
		commandListModel = new DefaultListModel<Command>();
		commandSearchList = new JList<Command>(commandListModel); //data has type String[]
		GridBagConstraints gbc_commandFilteredList = new GridBagConstraints();
		gbc_commandFilteredList.insets = new Insets(0, 0, 5, 5);
		gbc_commandFilteredList.gridx = 2;
		gbc_commandFilteredList.gridy = 8;
		projectPanel.add(commandSearchList, gbc_commandFilteredList);
		commandSearchList.setCellRenderer(new CommandCellRenderer()); 
		commandSearchList.setFont(new Font("Tahoma", Font.BOLD, 12));
		commandSearchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		commandSearchList.setLayoutOrientation(JList.VERTICAL);
		commandSearchList.setVisibleRowCount(-1);
		commandSearchList.addMouseListener(new MouseAdapter() 
		{
          public void mouseClicked(MouseEvent e) 
          {
              if(e.getClickCount() == 2)
              {
                  int selectedIndex = commandSearchList.getSelectedIndex();
                  
                  if(selectedIndex != -1)
                  {
                	  selectedCommand = commandSearchList.getSelectedValue();
                	  commandSearchBar.setText(commandSearchList.getSelectedValue().getTooltip());
                	  commandSearchBar.requestFocus();
                	  commandSearchBar.selectAll();
                  }
              }
          }
		});
		
		commandSearchBar = new JTextField();
		commandSearchBar.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterKeyStroke");
		commandSearchBar.getActionMap().put("enterKeyStroke", enterKeyStroke);
		commandSearchBar.setName("commandSearchBar");
		commandSearchBar.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_commandSearchBar = new GridBagConstraints();
		gbc_commandSearchBar.insets = new Insets(0, 0, 5, 5);
		gbc_commandSearchBar.fill = GridBagConstraints.BOTH;
		gbc_commandSearchBar.gridx = 1;
		gbc_commandSearchBar.gridy = 5;
		commandSearchBar.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				updateCommandListData();
				setCommandSearchBarFont();
			}
			
			@Override 
			public void removeUpdate(DocumentEvent e) 
		    { //VOID COULD USE LATER
				updateCommandListData();	
				setCommandSearchBarFont();
		    }	
		    
			@Override 
			public void changedUpdate(DocumentEvent e) 
			{ //VOID COULD USE LATER
			}
		});
		projectPanel.add(commandSearchBar, gbc_commandSearchBar);
		
		mainExistingNameLabel = new JLabel("");
		mainExistingNameLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_existingUserNameLabel = new GridBagConstraints();
		gbc_existingUserNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_existingUserNameLabel.anchor = GridBagConstraints.EAST;
		gbc_existingUserNameLabel.gridx = 0;
		gbc_existingUserNameLabel.gridy = 5;
		projectPanel.add(mainExistingNameLabel, gbc_existingUserNameLabel);
		
		commandAddButton = new JButton("Add command");
		GridBagConstraints gbc_addCommandBtn = new GridBagConstraints();
		gbc_addCommandBtn.fill = GridBagConstraints.BOTH;
		gbc_addCommandBtn.insets = new Insets(0, 0, 5, 5);
		gbc_addCommandBtn.anchor = GridBagConstraints.WEST;
		gbc_addCommandBtn.gridx = 2;
		gbc_addCommandBtn.gridy = 5;
		commandAddButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				commandAddButtonFire();
			}
		});
		projectPanel.add(commandAddButton, gbc_addCommandBtn);
		
		previewLbl = new JLabel();
		previewLbl.setText("(Command Preview)");
		previewLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_previewLbl = new GridBagConstraints();
		gbc_previewLbl.insets = new Insets(0, 0, 5, 0);
		gbc_previewLbl.gridx = 3;
		gbc_previewLbl.gridy = 5;
		projectPanel.add(previewLbl, gbc_previewLbl);
		
		commandLabel = new JLabel();
		commandLabel.setText("(Select Command)");
		commandLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblcommands = new GridBagConstraints();
		gbc_lblcommands.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblcommands.insets = new Insets(0, 0, 5, 5);
		gbc_lblcommands.gridx = 0;
		gbc_lblcommands.gridy = 6;
		projectPanel.add(commandLabel, gbc_lblcommands);
		
		commandScrollPane = new JScrollPane(commandSearchList);
		commandScrollPane.setPreferredSize(new Dimension(350,200));
		GridBagConstraints gbc_commandScrollPane = new GridBagConstraints();
		gbc_commandScrollPane.fill = GridBagConstraints.BOTH;
		gbc_commandScrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_commandScrollPane.gridx = 1;
		gbc_commandScrollPane.gridy = 6;	
		projectPanel.add(commandScrollPane, gbc_commandScrollPane);
		
		previewScrollPane = new JScrollPane();
		GridBagConstraints gbc_previewScrollPane = new GridBagConstraints();
		gbc_previewScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_previewScrollPane.fill = GridBagConstraints.BOTH;
		gbc_previewScrollPane.gridx = 3;
		gbc_previewScrollPane.gridy = 6;
		projectPanel.add(previewScrollPane, gbc_previewScrollPane);
		
		previewTextArea = new JTextArea();
		previewTextArea.setEditable(false);
		previewTextArea.setFont(new Font("Monospaced", Font.BOLD, 13));
		previewScrollPane.setViewportView(previewTextArea);
		
		generateButton = new JButton("Generate BAT File");
		generateButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				generateButtonFire();
			}
		});
		generateButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_generateButton = new GridBagConstraints();
		gbc_generateButton.fill = GridBagConstraints.VERTICAL;
		gbc_generateButton.gridx = 3;
		gbc_generateButton.gridy = 7;
		projectPanel.add(generateButton, gbc_generateButton);
	}
	
	/**
	 * 
	 * @param e
	 */
	private void toggleButtonFire(ActionEvent e)
	{
		userSelected = false;
		groupSelected = false;
		diskSelected = false;
		JToggleButton fireSource;
		String fireSourceName;
		
		fireSource = (JToggleButton) e.getSource();
		fireSourceName = fireSource.getName();
		
		switch(fireSourceName)
		{
		case "USER":
			dataType = DataType.USER;
			mainAddButton.setText("Add User");
			mainNewLabel.setText("(New User)");
			existingMainLabel.setText("(Existing Users)");
			userSelected = true;
			break;
		case "GROUP":
			dataType = DataType.GROUP;
			mainAddButton.setText("Add Group");
			mainNewLabel.setText("(New Group)");
			existingMainLabel.setText("(Existing Groups)");
			groupSelected = true;
			break;
		case "DISK":
			
			if(!previewTextArea.getText().isEmpty())
			{
				int optionSelected;

				optionSelected = JOptionPane.showConfirmDialog(
						Core.getFrame(), 
						"DiskPart selected. Generate User/Group bat commands and reset project?");
				
				switch(optionSelected)
				{
				case 0:
					generateButtonFire();
					DataOperations.resetProject(projectName);
					break;
				case 1:
					previewTextArea.setText("");
				default:
					diskToggle.setSelected(false);
					return;
				}
			}
			
			dataType = DataType.DISK;
			mainAddButton.setText("Add Disk");
			mainNewLabel.setText("(New Disk)");
			existingMainLabel.setText("(Existing Disks)");
			diskSelected = true;
			break;
		default:
			return;
		}

		mainSearchBar.setText("");
		mainSearchBar.requestFocus();
		commandSearchBar.setText("");
		userToggle.setSelected(userSelected);
		groupToggle.setSelected(groupSelected);
		diskToggle.setSelected(diskSelected);
		updateMainListData();
		updateCommandListData();
	}
	
	/**
	 * 
	 */
	private void mainAddButtonFire()
	{
		String searchBarInput = "";
		String command = "";
		
		searchBarInput = mainSearchBar.getText();
		
		if(searchBarInput.isEmpty())
		{
		
		}
		
		switch(dataType)
		{
		case USER:
			String passwordEntered = "";
			
			if(DataCenter.getDataByNameValue(projectName, dataType, searchBarInput) == null)
			{
				passwordEntered = JOptionPane.showInputDialog(
						Core.getFrame(),
						"Definir le mot de passe(Vide si aucun):", 
						"Set Password", JOptionPane.QUESTION_MESSAGE);
			}	
			
			if(DataOperations.userCreate(projectName, searchBarInput, passwordEntered))
			{
				command = CommandManager.getAddUserCommand(searchBarInput, passwordEntered);
				previewTextArea.append(command+"\n\n");
				updateMainListData();
				setComponentsVisibility();
			}
			
			break;
		case GROUP:

			if(DataOperations.groupCreate(projectName, searchBarInput))
			{
				command = CommandManager.getAddGroupCommand(searchBarInput);
				previewTextArea.append(command+"\n\n");
				updateMainListData();
				setComponentsVisibility();
			}
			
			break;
		case DISK:		

			if(DataOperations.diskCreate(projectName, searchBarInput))
			{
				disks.add(searchBarInput);
				updateMainListData();
				setComponentsVisibility();		
			}
			
			break;
		default:
		}
		
		mainSearchBar.requestFocus();
		mainSearchBar.selectAll();
	}
	
	/**
	 * 
	 */
	private void commandAddButtonFire()
	{
		String searchBarInput;
		
		searchBarInput = mainSearchBar.getText();
				
		if(selectedCommand == null)
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"No command selected! Select a command from the list before adding the command.",
					"List selection error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		else if(!DataOperations.doesNameExist(projectName, dataType, searchBarInput) && selectedCommand.hasInsertKey())
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"No existing user entered! Select a user from the list before adding the command.",
					"List selection error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		else
		{
			String command;
			
			command = selectedCommand.getStringCommand(projectName, searchBarInput);
			previewTextArea.append(command+"\n\n");
			
			updateMainListData();
			setComponentsVisibility();
			//selectedCommand = null;
			//commandSearchBar.setText("");
			//commandSearchList.clearSelection();
			commandSearchBar.requestFocus();
			commandSearchBar.selectAll();
		}		
	}
	
	private void generateButtonFire()
	{
		String batFileName;
		
		batFileName = JOptionPane.showInputDialog(
								Core.getFrame(), 
								"Enter the bat file name", 
								"Bat file name", 
								JOptionPane.INFORMATION_MESSAGE);
		
		if(batFileName == null)
		{
			return;
		}
		
		FileManager.generateBatFiles(
				projectName, batFileName ,previewTextArea.getText(), diskSelected);
	}
	
	private void enterKeyStrokeFire()
	{
		Component[] panelComponents;
		
		panelComponents = projectPanel.getComponents();
		
		for(Component component : panelComponents)
		{
			if(component.hasFocus() && component.equals(mainSearchBar))
			{
				mainAddButtonFire();
			}
			else if(component.hasFocus() && component.equals(commandSearchBar))
			{
				commandAddButtonFire();
			}
		}
	}
	
	/**
	 * 
	 */
	private void setComponentsVisibility()
	{
		ArrayList<Data> dataFound = new ArrayList<Data>();
		String searchBarInput;
		boolean uniqueDataFound;
		
		searchBarInput = mainSearchBar.getText();
		
		dataFound = DataOperations.getDataByNameKeyword(projectName ,dataType, searchBarInput);
		if(dataFound.size() == 1)
		{
			uniqueDataFound = true;
		}
		else
		{
			uniqueDataFound = false;
		}		
		
		if(uniqueDataFound)
		{
			//Unique data found
			mainSearchBar.setFont(boldFont);
			mainExistingNameLabel.setText(searchBarInput);		
			mainNewLabel.setVisible(false);
			
			updateCommandListData();
		}
		else
		{
			//No unique data found
			mainSearchBar.setFont(italicFont);
			mainExistingNameLabel.setText("");	
			mainNewLabel.setVisible(true);
		}
	}
	
	public void setCommandSearchBarFont()
	{
		CommandType commandType = CommandType.NONE;
		
		switch(dataType)
		{
		case USER:
			commandType = CommandType.USER;
			break;
		case GROUP:
			commandType = CommandType.GROUP;
			break;
		case DISK:
			commandType = CommandType.DISK;
			break;
		default:
		}
		if(CommandManager.isCommandInputUnique(commandType, commandSearchBar.getText()))
		{
			//Unique command entered
			commandSearchBar.setFont(boldFont);
		}
		else
		{
			//Command enter not unique
			commandSearchBar.setFont(italicFont);
		}
	}
	
	/**
	 * 
	 */
	public boolean updateMainListData()
	{
		ArrayList<Data> mainListData = new ArrayList<Data>();
		String searchBarInput;
		boolean uniqueData = false;
		
		//commandSearchBar.setText("");
		mainlistModel.removeAllElements();
		searchBarInput = mainSearchBar.getText();
		mainListData = DataCenter.getDataByNameKeyword(projectName, dataType, searchBarInput);
			
		for(Data data: mainListData)
		{
			mainlistModel.addElement(data);
		}	
		
		if(mainListData.size() == 1)
		{
			uniqueData = true;
		}
		return uniqueData;
	}
	
	/**
	 * 
	 */
	public void updateCommandListData()
	{
		ArrayList<Command> commandsFound = new ArrayList<Command>();
		CommandType commandType = CommandType.NONE;
		
		switch(dataType)
		{
		case USER:
			commandType = CommandType.USER;
			commandsFound= CommandManager.findCommandByKeyword(commandType, commandSearchBar.getText());
			break;
		case GROUP:
			commandType = CommandType.GROUP;
			commandsFound = CommandManager.findCommandByKeyword(commandType, commandSearchBar.getText());
			break;	
		case DISK:
			commandType = CommandType.DISK;
			commandsFound = CommandManager.findCommandByKeyword(commandType, commandSearchBar.getText());
			break;	
		default:			
		}
		
		commandListModel.removeAllElements();		
			
		if(commandsFound.size() > 0)
		{
			for(Command command: commandsFound)
			{
				commandListModel.addElement(command);
			}			
		}
	}	
	
	/**
	 * 
	 * @return
	 */
	public String getProjectName()
	{
		return projectName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPreviewText()
	{
		return previewTextArea.getText();
	}
	
	/**
	 * 
	 */
	public void resetProjectPanel()
	{
		mainSearchBar.setText("");
		commandSearchBar.setText("");
		previewTextArea.setText("");
		updateMainListData();
		updateMainListData();
	}
	
	/**
	 * 
	 * @param textToSet
	 */
	public void setMainSearchBar(String textToSet)
	{
		mainSearchBar.setText(textToSet);
	}
	
	/**
	 * 
	 * @param textToSet
	 */
	public void setCommandSearchBar(String textToSet)
	{
		commandSearchBar.setText(textToSet);
	}
	
	/**
	 * 
	 * @param command
	 */
	public void setPreviewText(String command)
	{
		previewTextArea.setText(command);
	}
}
