package gui.frame.menu;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import gui.frame.Core;
import gui.frame.tab.ProjectTabs;
import project.data.center.DataOperations;
import project.file.FileManager;
import javax.swing.JSeparator;

public class Menu 
{
	private JMenu fileMenu;
	private JFrame frame;
	private static JMenuBar menuBar;
	private static JMenuItem mntmNewProject;
	private static JMenuItem mntmOpenProject;
	private static JMenuItem mntmSaveProject;
	private static JMenuItem mntmSaveAll;
	private static JSeparator separator;
	private static JMenu mntEditMenu;
	private static JMenuItem mntmReset;
	private static JMenuItem mntmOpenDir;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void setupMenu(Core window)
	{
		frame = Core.getFrame();
		
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		//Main menu bar
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		mntmNewProject = new JMenuItem("New Project");
		mntmNewProject.setName("New Project");
		mntmNewProject.addActionListener(getActionListener());
		fileMenu.add(mntmNewProject);
		
		mntmOpenProject = new JMenuItem("Open Project");
		mntmOpenProject.setName("Open Project");
		mntmOpenProject.addActionListener(getActionListener());
		fileMenu.add(mntmOpenProject);
		
		separator = new JSeparator();
		separator.setVisible(false);
		separator.setPreferredSize(new Dimension(0, 10));
		fileMenu.add(separator);
		
		mntmOpenDir = new JMenuItem("Open Project Folder");
		mntmOpenDir.setName("Open Project Folder");
		mntmOpenDir.addActionListener(getActionListener());
		mntmOpenDir.setVisible(false);
		fileMenu.add(mntmOpenDir);
		
		mntmSaveProject = new JMenuItem("Save Project");
		mntmSaveProject.setVisible(false);
		mntmSaveProject.setName("Save Project");
		mntmSaveProject.addActionListener(getActionListener());
		fileMenu.add(mntmSaveProject);	
			
		mntmSaveAll = new JMenuItem("Save All");
		mntmSaveAll.setVisible(false);
		mntmSaveAll.setName("Save All");
		mntmSaveAll.addActionListener(getActionListener());
		fileMenu.add(mntmSaveAll);
		
		mntEditMenu = new JMenu("Edit");
		mntEditMenu.setVisible(false);
		menuBar.add(mntEditMenu);
		
		mntmReset = new JMenuItem("Reset Project");
		mntmReset.setName("Reset");
		mntmReset.addActionListener(getActionListener());
		mntEditMenu.add(mntmReset);
	}	
	
	
	/*
	 * GETTER FOR OPERATION ACTION LISTENER
	 * Calls the PanelContainer swapPanel() method with the name of the panel to swap to as argument
	 */
	private ActionListener getActionListener()
	{
		return  new ActionListener() 
		{
			public void actionPerformed(ActionEvent actionEvent) 
			{
				AbstractButton button = (AbstractButton) actionEvent.getSource();
				String buttonName = button.getName();
				
				switch(buttonName)
				{
				case "New Project":
					
					createNewProject();
					
					break;
				case "Open Project":	
					
					openExistingProject();
					
					break;
				case "Save Project":
								
					DataOperations.saveProjectData(ProjectTabs.getCurrentTabProjectName(), false);	
					
					break;	
				case "Open Project Folder":
					
					openCurrentProjectDir();	
					
					break;		
				case "Save All":
					
					DataOperations.saveAllProjectsData();
					
					break;
				case "Reset":
					
					DataOperations.resetProject(ProjectTabs.getCurrentTabProjectName());
					
					break;
				}
			}
		};
	}
	
	private void createNewProject()
	{
		String projectName;
		
		projectName = JOptionPane.showInputDialog(frame, "Enter the project Name");
		
		if(projectName == null)
		{
			return;
		}
		else if( projectName.equals(""))
		{
			JOptionPane.showMessageDialog(frame,
					"Couldn't create new project, name is empty",
					"Project creation error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		else
		{
			if(FileManager.setupNewProjectFiles(projectName))
			{
				DataOperations.initializeNewProject(projectName, false);			
			}
		}
		
	}
	
	private void openExistingProject()
	{
		
		if(FileManager.setupExistingProjectFiles())
		{
			String projectName;
			
			projectName = FileManager.getLastSetupProjectName();
			DataOperations.initializeExistingProject(projectName);
		}
		
	}

	/**
	 * 
	 */
	private static void openCurrentProjectDir()
	{
		String currentProjectDirPath =  FileManager.getProjectDirPath(ProjectTabs.getCurrentTabProjectName());
		try 
		{
			if(!(currentProjectDirPath == null))
			{
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + currentProjectDirPath);						
			}
			else
			{
				JOptionPane.showMessageDialog(Core.getFrame(),
						"No current project selected!",
						"File opening error",
						JOptionPane.ERROR_MESSAGE);							
			}
		} 
		catch (IOException ex) 
		{
			Logger.getLogger(Class.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * 
	 * @param isVisible
	 */
	public static void displayEditMenu(boolean isVisible)
	{
		separator.setVisible(isVisible);
		mntmSaveProject.setVisible(isVisible);
		mntmSaveAll.setVisible(isVisible);
		mntmOpenDir.setVisible(isVisible);
		mntEditMenu.setVisible(isVisible);
	}
}
	
	
