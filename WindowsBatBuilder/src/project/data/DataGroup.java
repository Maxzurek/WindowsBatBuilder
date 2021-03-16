package project.data;

import java.util.ArrayList;

import project.file.FileManager.FileType;

public class DataGroup extends Data
{
	private String name = null;
	private ArrayList<String> users = new ArrayList<String>();
	
	@Override
	public String getName()							{return name;}
	public ArrayList<String> getUsers()				{return users;}
	public void setName(String name) 				{this.name = name;}
	public void setUsers(ArrayList<String> users) 	{this.users = users;}
	
	public boolean addUser(String userName)
	{
		for(int i = 0; i< users.size(); i++)
		{
			if(users.get(i).equalsIgnoreCase(userName))
			{
				return false;
			}
		}
		users.add(userName);
		return true;
	}
	
	public boolean removeUser(String userName)
	{
		for(int i = 0; i< users.size(); i++)
		{
			if(users.get(i).equalsIgnoreCase(userName))
			{
				users.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public DataGroup()
	{
		super();
		this.dataType = DataType.GROUP;
		this.fileType = FileType.GROUP;
	}
		
	public DataGroup(String name)
	{
		super();
		this.dataType = DataType.GROUP;
		this.fileType = FileType.GROUP;
		this.name = name;
	}
	
	public DataGroup(String name, String user)
	{
		super();
		this.dataType = DataType.GROUP;
		this.fileType = FileType.GROUP;
		this.name = name;
		users.add(user);
	}
	
	public DataGroup(String name, ArrayList<String> users)
	{
		super();
		this.dataType = DataType.GROUP;
		this.fileType = FileType.GROUP;
		this.name = name;
		this.users = users;
	}
}
