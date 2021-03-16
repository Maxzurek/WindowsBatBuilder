package project.data.command;

import java.util.ArrayList;
import javax.swing.JOptionPane;

import gui.frame.Core;
import project.data.center.DataOperations;

public abstract class Command 
{
	public ArrayList<String> command = new ArrayList<String>();
	
	protected String comment = "";
	protected String tooltip = "";
	protected String keyword = "";
	
	protected int numberOfParam = 0;
	
	protected String insertKey = "insertname";
	protected String insertName = "";
	
	protected String param1 = "param1";
	protected String paramKey1 = "param1";
	protected String paramTooltip1 = "";
	
	protected String param2 = "param2";
	protected String paramKey2 = "param2";
	protected String paramTooltip2 = "";
	
	protected String param3 = "param3";
	protected String paramKey3 = "param3";
	protected String paramTooltip3 = "";
	
	protected String param4 = "param4";
	protected String paramKey4 = "param4";
	protected String paramTooltip4 = "";
	
	protected boolean hasInsertKey = true;
	protected boolean paramSelectionList = false;
	protected boolean deleteUser = false;
	protected boolean deleteGroup = false;
	protected boolean addUserToGroup = false;
	protected boolean deleteUserFromGroup = false;
	
	public String getTooltip()		{return tooltip;}
	public boolean hasInsertKey() 	{return hasInsertKey;}
	
	Command()
	{
	}
	
	/**
	 * 
	 * @param insertName
	 * @return
	 */
	public String getCommandPreview(String insertName)
	{
		ArrayList<String> commandCopy = new ArrayList<String>(command);
		String insertKey = "";
		String commandLine = "";

		if(insertName.length() == 0)
		{
			insertKey = "'"+this.insertKey+"'";		
		}
		else
		{
			insertKey = insertName;
		}
		
		for(int i = 0; i < command.size(); i++)
		{
			if(command.get(i).contentEquals(this.insertKey))
			{
				commandCopy.set(i, insertKey);
			}
		}
		
		for(String s : commandCopy)
		{
			commandLine = commandLine.concat(s);
		}
		
		return commandLine;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComment()
	{
		return "rem "+tooltip+"\n";
	}
	
	/**
	 * 
	 * @param mainSearchBarInput String
	 * @param withoutComment Boolean
	 * @return
	 */
	public String getStringCommand(String projectName, String mainSearchBarInput)
	{		
		ArrayList<String> commandCopy = new ArrayList<String>(command);
		insertName = mainSearchBarInput;
		
		if(numberOfParam == 1)
		{
			get1Param(commandCopy);
		}
		else if(numberOfParam == 2)
		{
			get2Params(commandCopy);
		}
		else if(numberOfParam == 3)
		{
			getCommandThreeParam(commandCopy);
		}
		else if(numberOfParam == 4)
		{
			getCommandFourParam(commandCopy);
		}
		
		if(!requestDataModification(projectName, mainSearchBarInput))
		{
			return "";
		}
		
		return getCommand(commandCopy);
	}
	
	/**
	 * 
	 * @param commandCopy
	 * @return
	 */
	private String getCommand(ArrayList<String> commandCopy)
	{
		String commandLine = "";

		for(int i = 0; i < commandCopy.size(); i++)
		{
			if(command.get(i).contentEquals(insertKey))
			{
				commandCopy.set(i, insertName);
			}
		}
		
		commandLine = commandLine.concat(getComment());
		
		for(String s : commandCopy)
		{
			commandLine = commandLine.concat(s);
		}
		
		return commandLine;
	}
	
	/**
	 * 
	 * @param commandCopy
	 * @return
	 */
	private void get1Param(ArrayList<String> commandCopy)
	{		
		String input1 = "";
		
		input1 = JOptionPane.showInputDialog(
				Core.getFrame(), 
				this.paramTooltip1, 
				"Parameter 1", 
				JOptionPane.INFORMATION_MESSAGE);
		
		if(input1 != null)
		{
			param1 = input1;
		}
		
		for(int i = 0; i < command.size(); i++)
		{
			if(command.get(i).contentEquals(this.paramKey1))
			{
				commandCopy.set(i, param1);
			}
		}
	}
	
	/**
	 * 
	 * @param commandCopy
	 * @return
	 */
	private void get2Params(ArrayList<String> commandCopy)
	{	
		String input1 = "";
		String input2 = "";
		
		input1 = JOptionPane.showInputDialog(
							Core.getFrame(), 
							this.paramTooltip1, 
							"Parameter 1", 
							JOptionPane.INFORMATION_MESSAGE);
		
		input2 = JOptionPane.showInputDialog(
							Core.getFrame(), 
							paramTooltip2, 
							"Parameter 2", 
							JOptionPane.INFORMATION_MESSAGE);
		
		if(input1 != null && input2 != null)
		{
			param1 = input1;
			param2 = input2;
		}
		
		for(int i = 0; i < command.size(); i++)
		{
			if(command.get(i).contentEquals(this.paramKey1))
			{
				commandCopy.set(i, param1);
			}
			else if(command.get(i).contentEquals(this.paramKey2))
			{
				commandCopy.set(i, param2);
			}
		}
	}
	
	/**
	 * 
	 * @param commandCopy
	 * @return
	 */
	private void getCommandThreeParam(ArrayList<String> commandCopy)
	{	
		String input1 = "";
		String input2 = "";
		String input3 = "";
		
		input1 = JOptionPane.showInputDialog(
				Core.getFrame(), 
				this.paramTooltip1, 
				"Parameter 1", 
				JOptionPane.INFORMATION_MESSAGE);

		input2 = JOptionPane.showInputDialog(
				Core.getFrame(), 
				this.paramTooltip2, 
				"Parameter 2", 
				JOptionPane.INFORMATION_MESSAGE);
		
		input3 = JOptionPane.showInputDialog(
				Core.getFrame(), 
				this.paramTooltip3, 
				"Parameter 3", 
				JOptionPane.INFORMATION_MESSAGE);
		
		if(input1 != null && input2 != null && input3 != null)
		{
			param1 = input1;
			param2 = input2;
			param3 = input3;
		}
		
		for(int i = 0; i < command.size(); i++)
		{
			if(command.get(i).contentEquals(this.paramKey1))
			{
				commandCopy.set(i, param1);
			}
			else if(command.get(i).contentEquals(this.paramKey2))
			{
				commandCopy.set(i, param2);
			}
			else if(command.get(i).contentEquals(this.paramKey3))
			{
				commandCopy.set(i, param3);
			}
		}
	}
	
	/**
	 * 
	 * @param commandCopy
	 * @return
	 */
	private void getCommandFourParam(ArrayList<String> commandCopy)
	{	
		String input1 = "";
		String input2 = "";
		String input3 = "";
		String input4 = "";
		
		input1 = JOptionPane.showInputDialog(
				Core.getFrame(), 
				this.paramTooltip1, 
				"Parameter 1", 
				JOptionPane.INFORMATION_MESSAGE);

		input2 = JOptionPane.showInputDialog(
				Core.getFrame(), 
				this.paramTooltip2, 
				"Parameter 2", 
				JOptionPane.INFORMATION_MESSAGE);
		
		input3 = JOptionPane.showInputDialog(
				Core.getFrame(), 
				this.paramTooltip3, 
				"Parameter 3", 
				JOptionPane.INFORMATION_MESSAGE);
		
		input4= JOptionPane.showInputDialog(
				Core.getFrame(), 
				this.paramTooltip4, 
				"Parameter 4", 
				JOptionPane.INFORMATION_MESSAGE);
		
		if(input1 != null && input2 != null && input3 != null && input4 != null)
		{
			param1 = input1;
			param2 = input2;
			param3 = input3;
			param4 = input4;
		}
		
		for(int i = 0; i < command.size(); i++)
		{
			if(command.get(i).contentEquals(this.paramKey1))
			{
				commandCopy.set(i, param1);
			}
			else if(command.get(i).contentEquals(this.paramKey2))
			{
				commandCopy.set(i, param2);
			}
			else if(command.get(i).contentEquals(this.paramKey3))
			{
				commandCopy.set(i, param3);
			}
			else if(command.get(i).contentEquals(this.paramKey4))
			{
				commandCopy.set(i, param4);
			}
		}
	}
	
	/**
	 * 
	 * @param projectName
	 * @param mainSearchBarInput
	 * @return
	 */
	private boolean requestDataModification(String projectName, String mainSearchBarInput)
	{
		if(deleteUser)
		{
			if(DataOperations.userDelete(projectName, mainSearchBarInput))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(deleteGroup)
		{
			if(DataOperations.groupDelete(projectName, mainSearchBarInput))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(addUserToGroup)
		{		
			if(DataOperations.groupAddUser(projectName, mainSearchBarInput, param1))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(deleteUserFromGroup)
		{
			if(DataOperations.groupRemoveUser(projectName, mainSearchBarInput, param1))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		return true;
	}
}
