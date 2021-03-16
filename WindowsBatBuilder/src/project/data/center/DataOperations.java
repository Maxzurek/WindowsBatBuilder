package project.data.center;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.JOptionPane;

import gui.frame.Core;
import gui.frame.tab.ProjectTabs;
import gui.frame.tab.project.ProjectPanel;
import project.data.Data;
import project.data.DataBat;
import project.data.DataDisk;
import project.data.DataGroup;
import project.data.DataUser;
import project.data.Data.DataType;
import project.file.CustomFile;
import project.file.FileManager;

public class DataOperations extends DataCenter
{	
	
	/**
	 * 
	 * @param projectName
	 * @param isReset
	 */
	public static void initializeNewProject(String projectName, boolean isReset)
	{		
		initializeProjectData(projectName);
		
		//Default Users
		DataUser administrateur;
		administrateur = new DataUser("Administrateur");
		administrateur.setPrimaryGroup("DefaultAccount");
		administrateur.addSecondaryGroup("Guess");
		DataCenter.addData(projectName,administrateur);

		//Default groups
		DataGroup administrateurs;
		administrateurs = new DataGroup("Administrateurs");
		administrateurs.addUser("Users");
		DataCenter.addData(projectName,administrateurs);	
		
		DataGroup users;
		users = new DataGroup("Users");
		administrateurs.addUser("Users");
		DataCenter.addData(projectName,users);
		
		DataGroup guests;
		guests = new DataGroup("Guests");
		administrateurs.addUser("Users");
		DataCenter.addData(projectName,guests);
		
		//Default disks
		DataCenter.addData(projectName, new DataDisk("1"));
		DataCenter.addData(projectName, new DataDisk("2"));
		DataCenter.addData(projectName, new DataDisk("3"));
		
		//Default bat data
		DataCenter.addData(projectName, new DataBat("Default Bat Data"));
		
		saveData(projectName);
		ProjectTabs.addProjectTab(projectName);		
		Core.setFrameTitle(FileManager.getProjectDirPath(projectName));	
		
		JOptionPane.showMessageDialog(Core.getFrame(),
				"Project '"+projectName+"' successfully created!",
				"Project Created",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * 
	 * @param projectName
	 */
	public static void initializeExistingProject(String projectName)
	{
		ArrayList<CustomFile> projectFiles = new ArrayList<CustomFile>();
		DataBat batData;
		
		DataCenter.initializeProjectData(projectName);
		projectFiles = FileManager.getProjectFiles(projectName);	

		for(CustomFile file : projectFiles)
		{
			if(!DataCenter.loadProjectData(projectName, file.getFile()))
			{
				JOptionPane.showMessageDialog(
						Core.getFrame(),
						"An error occured while loading project files!"
						, "Error",JOptionPane.ERROR_MESSAGE);
			}		
		}
			
		ProjectTabs.addProjectTab(projectName);		
		if((batData = getDataBat(projectName)) != null)
		{
			if(!batData.getName().isEmpty())
			{
				ProjectTabs.getProjectPanel(projectName).setPreviewText(batData.getCommands());
			}
		}
		
		Core.setFrameTitle(FileManager.getProjectDirPath(projectName));	
		
		JOptionPane.showMessageDialog(
				Core.getFrame(), 
				"Project '"+projectName+"' successfuly opened!",
				"Project added",JOptionPane.PLAIN_MESSAGE);
	}
	
	public static void resetProject(String projectName)
	{
		ArrayList<Data> projectData;
		
		projectData = getProjectData(projectName);
		
		if(!projectData.isEmpty())
		{
			switch(JOptionPane.showConfirmDialog(
					Core.getFrame(), 
					"Warming, reseting all project data. Continue?", 
					"",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
			{
			case 0:
				projectData.clear();
				initializeNewProject(projectName, true);
				saveProjectData(projectName, true);
				ProjectTabs.resetProjectPanel(projectName);
				JOptionPane.showMessageDialog(Core.getFrame(),
						"Project reset!",
						"",
						JOptionPane.PLAIN_MESSAGE);
				break;
			default:
				return;
			}
		}
	}
	
	/**
	 * 
	 * @param projectName
	 * @param isReset
	 * @return
	 */
	public static boolean saveProjectData(String projectName, boolean isReset)
	{
		ProjectPanel projectPanel;

		projectPanel = ProjectTabs.getProjectPanel(projectName);
		
		if(!batSaveCommands(projectName, projectPanel.getPreviewText()))
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"There was an error while saving bat data!",
					"Bat data error",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		if(!saveData(projectName))
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"There was an error while saving project data!",
					"Project data error",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		JOptionPane.showMessageDialog(Core.getFrame(),
				"Project '"+projectName+"' successfuly saved!",
				"Project Saved",
				JOptionPane.PLAIN_MESSAGE);
		
		return true;
	}
	
	public static boolean saveAllProjectsData()
	{
		Map<String, ProjectPanel> projectsPanels;
		
		projectsPanels = ProjectTabs.getOpenedProjectPanels();
		
		for(Map.Entry<String, ProjectPanel> projectPanel : projectsPanels.entrySet())
		{
			String panelProjectName = projectPanel.getKey();
			ProjectPanel panel = projectPanel.getValue();
			
			if(!batSaveCommands(panelProjectName, panel.getPreviewText()))
			{
				JOptionPane.showMessageDialog(Core.getFrame(),
						"There was an error while creating bat file data!",
						"Bat data error",
						JOptionPane.WARNING_MESSAGE);
				
				return false;
			}
		}
		
		if(!saveAllData())
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"There was an error while saving projects data!",
					"Projects data error",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		JOptionPane.showMessageDialog(Core.getFrame(),
				"Successfuly saved all projects!",
				"Projects Saved",
				JOptionPane.PLAIN_MESSAGE);
		
		return true;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param userName
	 * @param password null if no password
	 * @return
	 */
	public static boolean userCreate(String projectName, String userName, String password)
	{
		Data existingUser;
		
		if(userName.length() == 0 || userName == null)
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Couldn't create user, empty name! Please enter a name.",
					"User add error",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		existingUser = DataCenter.getDataByNameValue(projectName, DataType.USER, userName);
			
		if(existingUser == null)
		{
			if(password.isEmpty() || password == null)
			{
				DataCenter.addData(projectName, new DataUser(userName));
				
				JOptionPane.showMessageDialog(Core.getFrame(),
						"User '"+userName+"' successfuly added!",
						"User added",
						JOptionPane.PLAIN_MESSAGE);
			}
			else
			{
				DataCenter.addData(projectName, new DataUser(userName, password));	
				
				JOptionPane.showMessageDialog(Core.getFrame(),
						"User '"+userName+"' successfuly added! Password: "+password,
						"User added",
						JOptionPane.PLAIN_MESSAGE);
			}
			
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"User '"+userName+"' already exists!",
					"User add error",
					JOptionPane.WARNING_MESSAGE);	
		}
		return false;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param userName
	 * @param password
	 * @param primaryGroup
	 * @param secondaryGroup
	 * @return
	 */
	public static boolean userCreateDefault(
											String projectName,
											String userName,
											String password, 
											String primaryGroup, 
											String secondaryGroup)
	{
		Data existingUser;
		DataUser newUser;
		
		existingUser = DataCenter.getDataByNameValue(projectName, DataType.USER, userName);
			
		if(existingUser == null)
		{
			newUser = new DataUser(userName, password);
			newUser.setPrimaryGroup(primaryGroup);
			newUser.addSecondaryGroup(secondaryGroup);
			DataCenter.addData(projectName, newUser);
			
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param userName
	 * @return true if user exists and was deleted, false if not
	 */
	public static boolean userDelete(String projectName, String userName)
	{
		Data userToRemove;
		
		userToRemove = getDataByNameValue(projectName, DataType.USER, userName);
		
		if(userToRemove != null)
		{
			removeData(projectName, userToRemove);
			JOptionPane.showMessageDialog(Core.getFrame(),
					"User: '"+userName+"' has been removed from the project! Command added.",
					"User removed",
					JOptionPane.PLAIN_MESSAGE);
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"User: '"+userName+"' doest not exist.",
					"User not found",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
	}
	
	/**
	 * 
	 * @param projectName
	 * @param userName
	 * @param groupName
	 * @return
	 */
	public static boolean userAddGroup(String projectName, String userName, String groupName)
	{
		Data userData;
		
		userData = getDataByNameValue(projectName, DataType.USER, userName);
		
		if(userData != null)
		{
			DataUser user = (DataUser) userData;
			
			if(user.getPrimaryGroup() == null)
			{
				user.setPrimaryGroup(groupName);
			}
			else
			{
				user.addSecondaryGroup(groupName);
			}
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"User: '"+userName+"' doest not exist.",
					"User not found",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
	}
	
	/**
	 * 
	 * @param projectName
	 * @param userName
	 * @param groupName
	 * @return
	 */
	public static boolean userRemoveGroup(String projectName, String userName, String groupName)
	{
		Data userData;
		
		userData = getDataByNameValue(projectName, DataType.USER, userName);
		
		if(userData != null)
		{
			DataUser user = (DataUser) userData;
			
			if(user.getPrimaryGroup() != null)
			{
				user.setPrimaryGroup("");
			}
			else
			{
				user.removeSecondaryGroup(groupName);
			}
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"User: '"+userName+"' doest not exist.",
					"User not found",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
	}
	
	/**
	 * 
	 * @param projectName
	 * @param groupName
	 * @return	true if group does not exist and was created , false if not
	 */
	public static boolean groupCreate(String projectName, String groupName)
	{
		Data existingGroup;
		
		if(groupName.length() == 0 || groupName == null)
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Couldn't create group, empty name! Please enter a name.",
					"Group add error",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		existingGroup = DataCenter.getDataByNameValue(projectName, DataType.GROUP, groupName);
		
		if(existingGroup == null)
		{
			DataCenter.addData(projectName, new DataGroup(groupName));
			
			JOptionPane.showMessageDialog(Core.getFrame(),
					  "Group '"+groupName+"' successfuly added!",
					  "Group added",
					  JOptionPane.PLAIN_MESSAGE);
			
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Group: '"+groupName+"' already exist.",
					"Group found",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
	}
	
	/**
	 * 
	 * @param projectName
	 * @param groupName
	 * @param user
	 * @return	true if group does not exist and was created , false if not
	 */
	public static boolean groupCreate(String projectName, String groupName, String user)
	{
		Data existingGroup;
		
		existingGroup = DataCenter.getDataByNameValue(projectName, DataType.GROUP, groupName);
		
		if(existingGroup == null)
		{
			DataCenter.addData(projectName, new DataGroup(groupName, user));
			
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param groupName
	 * @param users
	 * @return	true if group was created , false if not
	 */
	public static boolean groupCreate(String projectName, String groupName, ArrayList<String> users)
	{
		Data existingGroup;
		LinkedHashSet<String> uniqueUsers =new LinkedHashSet<String>();
		ArrayList<String> usersToAdd = new ArrayList<String>();	
		
		existingGroup = DataCenter.getDataByNameValue(projectName, DataType.GROUP, groupName);
		
		if(existingGroup == null)
		{
			for(String uniqueUser : uniqueUsers)
			{
				if(!uniqueUsers.add(uniqueUser))
				{
					return false;
				}
			}
			
			usersToAdd.addAll(uniqueUsers);
			DataCenter.addData(projectName, new DataGroup(groupName, usersToAdd));
			
			return true;
		}
		return false;	
	}
	
	/**
	 * 
	 * @param projectName
	 * @param groupName
	 * @return true if user exists and was deleted, false if not
	 */
	public static boolean groupDelete(String projectName, String groupName)
	{
		Data groupToRemove;
		
		groupToRemove = getDataByNameValue(projectName, DataType.GROUP, groupName);
		
		if(groupToRemove != null)
		{
			removeData(projectName, groupToRemove);
			
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Group: '"+groupName+"' successfully deleted!",
					"Group delete",
					JOptionPane.PLAIN_MESSAGE);
			
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Group: '"+groupName+"' doesn't exist!",
					"Group delete error",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
	
	/**
	 * 
	 * @param projectName
	 * @param groupName
	 * @param userName
	 * @return	true if user was added to the group , false if group deesn't exist
	 */
	public static boolean groupAddUser(String projectName, String groupName, String userName)
	{
		Data userData;
		Data groupData;
		
		userData = getDataByNameValue(projectName, DataType.USER, userName);
		if(userData == null)
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"User: '"+userName+"' does not exist!",
					"User not found error",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		groupData = getDataByNameValue(projectName, DataType.GROUP, groupName);	
		if(groupData != null)
		{
			DataGroup group = (DataGroup) groupData;
			
			if(group.addUser(userName))
			{
				userAddGroup(projectName, userName, groupName);
				
				JOptionPane.showMessageDialog(Core.getFrame(),
						"User: '"+userName+"' added to Group: '"+groupName+"'! Command added.",
						"Group add user",
						JOptionPane.PLAIN_MESSAGE);
				return true;						
			}
			else
			{
				JOptionPane.showMessageDialog(Core.getFrame(),
						"User: '"+userName+"' is already in Group: '"+groupName+"'!",
						"Group add error",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		else
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Group: '"+groupName+"' doesn't exist!",
					"Group add error",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
	
	/**
	 * 
	 * @param projectName
	 * @param groupName
	 * @param userName
	 * @return	true if user was removed from the group , false if group doesn't exist
	 */
	public static boolean groupRemoveUser(String projectName, String groupName, String userName)
	{
		Data groupData;
		
		groupData = getDataByNameValue(projectName, DataType.GROUP, groupName);
		
		if(groupData != null)
		{
			DataGroup group = (DataGroup) groupData;
			
			if(group.removeUser(userName))
			{
				userRemoveGroup(projectName, userName, groupName);
				
				JOptionPane.showMessageDialog(Core.getFrame(),
						"User: '"+userName+"' removed  from Group: '"+groupName+"'! Command added.",
						"Group remove user",
						JOptionPane.PLAIN_MESSAGE);
				return true;
			}
			else
			{
				JOptionPane.showMessageDialog(Core.getFrame(),
						"User: '"+userName+"' is not in Group: '"+groupName+"'!",
						"Group remove error",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		else
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Group: '"+groupName+"' doesn't exist!",
					"Group add error",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
	
	public static boolean diskCreate(String projectName, String diskName)
	{
		Data existingDisk;
		
		if(diskName.length() == 0 || diskName == null)
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Couldn't create disk, empty name! Please enter a name.",
					"Empty name",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		existingDisk = getDataByNameValue(projectName, DataType.DISK, diskName);
		
		if(existingDisk == null)
		{
			DataCenter.addData(projectName, new DataDisk(diskName));
			
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Disk '"+diskName+"' successfuly added!",
					"Disk added",
					JOptionPane.PLAIN_MESSAGE);
			
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(Core.getFrame(),
					"Disk : '"+diskName+"' already exist!",
					"Disk add error",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
	}
	
	/**
	 * 
	 * @param projectName
	 * @param batFileName
	 * @return true if the bat file name is not found in the project  and was created, false if not
	 */
	public static boolean batSaveCommands(String projectName, String commands)
	{
		DataBat dataBat;
		
		if((dataBat = getDataBat(projectName)) != null)
		{
			dataBat.setCommands(commands);
			return true;
		}	
		return false;
	}
}
