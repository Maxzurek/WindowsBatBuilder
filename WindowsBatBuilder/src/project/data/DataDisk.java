package project.data;

import project.file.FileManager.FileType;

public class DataDisk extends Data
{
	private String name = "";
	private String commandName = "";
	
	@Override
	public String getName() 		{return name;}
	public String getCommandName() 	{return commandName;}
	
	public void setName(String name){this.name = name;}
	
	public DataDisk()
	{
		this.dataType = DataType.DISK;
		this.fileType = FileType.DISK;
	}
	
	public DataDisk(String name)
	{
		this.dataType = DataType.DISK;
		this.fileType = FileType.DISK;
		this.name = name;
		commandName = name.replaceAll("disk", "");
	}
}
