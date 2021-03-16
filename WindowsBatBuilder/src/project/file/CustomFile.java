package project.file;

import java.io.File;

import project.file.FileManager.FileType;

public class CustomFile 
{
	private File file;
	private FileType fileType = FileType.NULL;
	
	public File getFile()			{return file;}
	public FileType getFileType()	{return fileType;}
	
	CustomFile(FileType fileType, File file)
	{
		this.fileType = fileType;
		this.file = file;
	}
	
}
