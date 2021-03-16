package gui.frame;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gui.frame.menu.Menu;
import gui.frame.tab.ProjectTabs;
import project.data.center.DataOperations;
import project.data.command.CommandManager;
import project.file.FileManager;

public class Core 
{	
	private static Core core;
	private static JFrame frame;
	private static Menu menu;
	
	private static ProjectTabs tabbedPane;
	private static FileManager fileManager;
	private static CommandManager commandManager;
	
	public Core getCore()								{return core;}
	public static JFrame getFrame()						{return frame;}
	public static FileManager getFileManager()			{return fileManager;}	
	public static CommandManager getCommandManager()	{return commandManager;}
	
	public static void setFrameTitle(String newTitle){frame.setTitle("Window BatBuilder - "+newTitle);}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		try
    	{
			String version = "2.0";
			FileWriter myWriter = new FileWriter("version.txt");
			myWriter.write(version);
			myWriter.close();
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    	
		EventQueue.invokeLater(new Runnable()
		{
			public void run() 
			{
				try 
				{
					core = new Core();
					Core.frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Core() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setTitle("Windows Bat Builder - (No project loaded)");
		frame.setBounds(100, 100, 1100, 650);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
            public void windowClosing(WindowEvent e)
            {
            	int optionSelected;
            	
            	optionSelected = JOptionPane.showConfirmDialog(frame, "Save all projects before leaving?");
                if(optionSelected == 0)
                {
                	DataOperations.saveAllProjectsData();
                	System.exit(0);
                }
                else if(optionSelected == 1)
                {
                	System.exit(0);                	          	
                }
                else if(optionSelected == 2)
                {
                	
                }
            }
        });
			
		menu = new Menu();
		menu.setupMenu(core);
		tabbedPane = new ProjectTabs();
		tabbedPane.setupPanel(core);
		
		fileManager = new FileManager();	
		commandManager = new CommandManager();
	}
}
