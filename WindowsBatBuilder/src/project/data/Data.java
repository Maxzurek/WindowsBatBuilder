package project.data;

import project.file.FileManager.FileType;

public class Data
{
	public enum DataType
	{
		NULL, USER, GROUP, DISK, BAT,
	}
	
	protected DataType dataType = DataType.NULL;
	protected FileType fileType = FileType.NULL;
	protected String name = "";
	protected String searchKeyword = "";
	protected String dataModel = "";
	
	public Data()
	{
	}
	
	public DataType getDataType() 				{return dataType;}
	public FileType getFileType() 				{return fileType;}
	public String getName() 					{return name;}
	public String getDataModel()  				{return dataModel;}
	
	public void setDataModel(String dataModel)	{this.dataModel = dataModel;}
}
