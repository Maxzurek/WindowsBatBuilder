package project.data;

import project.file.FileManager.FileType;

public class DataBat extends Data
{
	private String fileName = null;
	private String commands = null;
	
	@Override
	public String getName() 				{return fileName;}
	public String getCommands()				{return commands;}
	
	public void setCommands(String commands){this.commands = commands;}
	public void addCommand(String command)
	{
		commands = commands.concat(command);
	}
	
	/**
	 * 
	 */
	public DataBat()
	{
		super();
		this.dataType = DataType.BAT;
		this.fileType = FileType.BAT;
	}
	
	/**
	 * 
	 * @param fileName
	 */
	public DataBat(String fileName)
	{
		super();
		this.dataType = DataType.BAT;
		this.fileType = FileType.BAT;
		this.fileName = fileName;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param fileName
	 */
	public DataBat(String fileName, String commands)
	{
		super();
		this.dataType = DataType.BAT;
		this.fileType = FileType.BAT;
		this.fileName = fileName;
		this.commands = commands;
	}	
}
