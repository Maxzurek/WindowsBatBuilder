package project.data;

import java.util.ArrayList;

import project.file.FileManager.FileType;

public class DataUser extends Data
{
	private String name = null;
	private String password = null;
	private String primaryGroup = null;
	private ArrayList<String> secondaryGroups = new ArrayList<String>();
		
	@Override
	public String getName()												{return name;}
	public String getPassword()											{return password;}
	public String getPrimaryGroup()										{return primaryGroup;}
	public ArrayList<String> getSecondaryGroups()						{return secondaryGroups;}	
	public void setName(String name) 									{this.name = name;}
	public void setPassword(String password) 							{this.password = password;}
	public void setPrimaryGroup(String primaryGroup) 					{this.primaryGroup = primaryGroup;}
	public void setSecondaryGroups(ArrayList<String> secondaryGroups) 	{this.secondaryGroups = secondaryGroups;}
	
	public void addSecondaryGroup(String groupName) 					{secondaryGroups.add(groupName);}
	public boolean removeSecondaryGroup(String groupName)
	{
		for(int i = 0; i < secondaryGroups.size(); i++)
		{
			if(secondaryGroups.get(i).equalsIgnoreCase(groupName))
			{
				secondaryGroups.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	public DataUser()
	{
		super();
		this.dataType = DataType.USER;
		this.fileType = FileType.USER;
	}
		
	public DataUser(String name)
	{
		super();
		this.dataType = DataType.USER;
		this.fileType = FileType.USER;
		this.name = name;
	}
	
	public DataUser(String name, String password)
	{
		super();
		this.dataType = DataType.USER;
		this.fileType = FileType.USER;
		this.name = name;
		this.password = password;
	}
}
