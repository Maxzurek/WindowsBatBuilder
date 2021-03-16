package gui.frame.tab;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import gui.frame.Core;
import gui.frame.menu.Menu;
import gui.frame.tab.project.ProjectPanel;

public class ProjectTabs 
{
	
	private static JFrame frame;
	private static JTabbedPane tabbedPane;	
	private static int openedTabIndex = -1;
	private static Map<String, ProjectPanel> openedProjectPanels = new TreeMap<String, ProjectPanel>();

	/**
	 * @wbp.parser.entryPoint
	 */
	public void setupPanel(Core window)
	{
		frame = Core.getFrame();
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(frame.getBounds());
		frame.getContentPane().add(tabbedPane);
	}
	
	public static JTabbedPane getTabbedPane() 						{return tabbedPane;}
	public static ProjectPanel getProjectPanel(String projectName) 	{return openedProjectPanels.get(projectName);}
	public static String getCurrentTabProjectName()
	{
		return tabbedPane.getSelectedComponent().getName();
	}
	public static Map<String, ProjectPanel> getOpenedProjectPanels(){return openedProjectPanels;}

	/**
	 * 
	 * @param projectName
	 */
	public static void addProjectTab(String projectName)
	{
		ProjectPanel projectPanel = new ProjectPanel(projectName);
		
		tabbedPane.addTab(projectName, null, projectPanel.getProjectPane(), null);
		openedTabIndex++;
		tabbedPane.setSelectedIndex(openedTabIndex);
		openedProjectPanels.put(projectName, projectPanel);
		projectPanel.updateMainListData();
		projectPanel.updateCommandListData();	
		
		if(openedTabIndex > -1)
		{
			Menu.displayEditMenu(true);
		}
	}
	
	public static void resetProjectPanel(String projectName)
	{
		ProjectPanel projectPanel;
		
		projectPanel = openedProjectPanels.get(projectName);
		projectPanel.resetProjectPanel();
	}
}
