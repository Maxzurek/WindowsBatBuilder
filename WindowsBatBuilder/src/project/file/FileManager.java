package project.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gui.frame.Core;
import project.data.Data;
import project.data.command.CommandManager;

public class FileManager 
{	
	public enum ProjectType
	{
		EMPTY, NEW, EXISTING
	}
	
	public enum FileType
	{
		NULL, USER, GROUP, DISK, BAT
	}
	
	private static JFrame frame;
	private static File parentDirFile;
	private static String lastSetupProjectName = "";
	private static ArrayList<CustomFile> defaultProjectFiles = new ArrayList<CustomFile>();
	private static Map<String, ArrayList<CustomFile>> projectsFiles = new TreeMap<String,ArrayList<CustomFile>>();
	private static Map<String, String> projectsDirPath = new TreeMap<String, String>();
	
	public static String getLastSetupProjectName() 						{return lastSetupProjectName;}
	public static String getProjectDirPath(String projectName)
	{ 
		return projectsDirPath.get(projectName);
	}
	
	public FileManager()
	{
		initialize();		
	}
		
	private static void initialize() 
	{	
		frame = Core.getFrame();
		
		//Default project files needed for every projects
		defaultProjectFiles.add(new CustomFile(FileType.USER, new File("users.project")));
		defaultProjectFiles.add(new CustomFile(FileType.GROUP, new File("groups.project")));
		defaultProjectFiles.add(new CustomFile(FileType.DISK, new File("disks.project")));
		defaultProjectFiles.add(new CustomFile(FileType.BAT,  new File("bat.project")));
		
		createProjectsParentDir();
	}
	
	/**
	 * 
	 * @param projectName
	 * @return project files, null if empty
	 */
	public static ArrayList<CustomFile> getProjectFiles(String projectName)
	{
		for(Map.Entry<String, ArrayList<CustomFile>> file : projectsFiles.entrySet())
		{
			
			if(file.getKey().equals(projectName))
			{
				return file.getValue();
			}
		}	
		return null;
	}
	
	/**
	 * 
	 * @param projectType
	 * @param nameOfProject
	 */
	public static boolean setupNewProjectFiles(String projectName)
	{
		File projectDirFile;
		String projectDirPath;
			
		projectDirFile = new File(parentDirFile+"\\"+projectName);
		if(projectDirFile.exists())
		{
			JOptionPane.showMessageDialog(
					frame
					,"Project: '"+projectName+"' already exists"
					, "Error",
					JOptionPane.ERROR_MESSAGE);
				
			return false;
		}
		
		projectDirFile.mkdir();
		projectDirPath = projectDirFile.getAbsolutePath();	
		
		if(!initializeProjectFiles(projectName, projectDirPath))
		{
			JOptionPane.showMessageDialog(frame,
					"Couldn't create new project,there was an error while creating project files",
					"Project creation error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean setupExistingProjectFiles()
	{
		String projectName = "";
		File projectFile;
		String projectDirPath;
		
		projectFile = getExistingProjectFile();	
		
		if(projectFile == null)
		{
			return false;
		}	
		
		projectDirPath = projectFile.getAbsolutePath();	
		projectName = projectFile.getName();
		
		if(isProjectOpen(projectName))
		{
			JOptionPane.showMessageDialog(frame,
					"Project :'"+projectName+"' is already opened.",
					"Existing project",
					JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		if(!initializeProjectFiles(projectName, projectDirPath))
		{
			JOptionPane.showMessageDialog(frame,
					"Couldn't open existing project,there was an error while creating project files",
					"Project file error",
					JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param projectName
	 * @return
	 */
	public static boolean isProjectOpen(String projectName)
	{
		if(projectsFiles.get(projectName) == null)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param batFileName
	 * @param commands
	 * @param diskPartCmd
	 * @return
	 */
	public static boolean generateBatFiles(String projectName, String batFileName, String commands, boolean diskPartCmd)
	{
		String projectDirPath;
		File batDirFile;
		File textFile;
		File batFile;
		String batTextCmd;
		String batFileCmd;
		
		projectDirPath = getProjectDirPath(projectName);
		batDirFile = new File(projectDirPath+"\\Generated Bat Files");
		batDirFile.mkdir();
		textFile = new File(batDirFile+"\\"+batFileName+".txt");
		batFile = new File(batDirFile+"\\"+batFileName+".bat");
		
		if(textFile.exists())
		{
			if(!queryRenameFile(projectName, textFile))
			{
				return false;
			}
		}
		if(batFile.exists())
		{
			if(!queryRenameFile(projectName, batFile))
			{
				return false;
			}
		}
		
		if(diskPartCmd)
		{
			batTextCmd = commands+"\r\npause";
			batFileCmd = CommandManager.getDiskBatFileCmd(textFile);	
			writeToFile(textFile, batTextCmd);
			writeToFile(batFile, batFileCmd);
		}
		else
		{
			batFileCmd = commands+"\r\npause";
			writeToFile(batFile, batFileCmd);			
		}
		
		JOptionPane.showMessageDialog(frame,
				"Bat file successfully generated!",
				"Bat file created",
				JOptionPane.PLAIN_MESSAGE);
		
		return true;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param fileToRename
	 * @return
	 */
	private static boolean queryRenameFile(String projectName, File fileToRename)
	{
		int optionSelected;

		optionSelected = JOptionPane.showConfirmDialog(
				frame, 
				"There is already a file named: '"
				+fileToRename.getName()+"' in Project: '"+projectName
				+"'. Rename new file? (File will be replaced if no is selected)");
        
		switch(optionSelected)
		{
		case 0:
			File newFilePath;
			String newFileName = "";
			String fileExtension = "";
			
			newFilePath = new File(fileToRename.getAbsolutePath());
			
			if(fileToRename.getName().matches(".*.txt*."))
			{
				newFileName = fileToRename.getName().replace(".txt", "");
				fileExtension = ".txt";
			}
			else if(fileToRename.getName().matches(".*.bat*."))
			{
				newFileName = fileToRename.getName().replace(".bat", "");
				fileExtension = ".bat";
			}
			
			for(int i = 1; newFilePath.exists(); i++)
			{
				newFilePath = new File(fileToRename.getParent()+"\\"+newFileName+"("+i+")"+fileExtension);
			}
			fileToRename.renameTo(newFilePath);
			return true;
			
		case 1:
			
			fileToRename.delete();
			return true;
			
		default:
			return false;			
		}
			
	}
	
	private static boolean writeToFile(File fileToWrite, String textToWrite)
	{
		try 
		{
			FileWriter fileWriter = new FileWriter(fileToWrite);
			
			fileWriter.write(textToWrite);
			fileWriter.close();
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(
					frame,
					"An error occured while writing file: '"+fileToWrite.getName()+"'.", 
					"File writing error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 */
	private static void createProjectsParentDir()
	{
		File runningDirFile;
		String runningDirPath;
		String parentDirPath;

		runningDirFile = getRunningDirFile();
		runningDirPath = runningDirFile.getAbsolutePath();
		parentDirPath = runningDirPath+"\\Projects";
		parentDirFile = new File(parentDirPath);
		parentDirFile.mkdir();
	}
	
	/**
	 * 
	 * 
	 * @return existing projectFile, null if it doesn't exist
	 */
	private static File getExistingProjectFile()
	{
		//GUI to chose file
		File selectedFile;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(parentDirFile);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
		//If 0, a folder has been selected
		if(fileChooser.showOpenDialog(frame) == 0)
		{	
			selectedFile = fileChooser.getSelectedFile();
			
			if(selectedFile.getName().equals("Generated Bat Files"))
			{
				selectedFile = fileChooser.getSelectedFile().getParentFile();
			}
			return selectedFile;
		}
		return null;
	}

	/**
	 * 
	 * @param customFile
	 * @param projectDirPath
	 * @return true if project files have all been initialized
	 */
	private static boolean initializeProjectFiles(String projectName, String projectDirPath)
	{		
		ArrayList<CustomFile> projectFiles = new ArrayList<CustomFile>();
		
		for(CustomFile defaultCustomFile : defaultProjectFiles)
		{
			String fileName;
			File projectFile;
			CustomFile customProjectFile;
			
			fileName = defaultCustomFile.getFile().getName();
			projectFile = new File(projectDirPath+"\\"+fileName);
			customProjectFile = new CustomFile(defaultCustomFile.getFileType(), projectFile);
			
			projectFiles.add(customProjectFile);
			
			if(!projectFile.exists())
			{
				try 
				{
					projectFile.createNewFile();			
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					return false;
				}			
			}
		}
		
		lastSetupProjectName = projectName;
		projectsDirPath.put(projectName, projectDirPath);
		projectsFiles.put(projectName, projectFiles);
		return true;
	}
	
	/**
	 * Get current project directory file
	 * 
	 * @return current dir. File
	 */
	private static File getRunningDirFile()
	{
		StringBuilder executablePath = new StringBuilder();
		File tempFile = new File("tempFile");
		
		tempFile.deleteOnExit();	
		executablePath = executablePath.append(tempFile.getAbsolutePath());
		executablePath.delete(executablePath.length()-8, executablePath.length());
		
		return new File(executablePath.toString());
	}
	
	/**
	 * 
	 * @param fileType
	 * @param projectName
	 * @param parsedProjectData
	 */
	public static boolean saveDataToFile(String projectName, ArrayList<Data> projectData)
	{
		ArrayList<CustomFile> projectFiles;
		
		projectFiles = getProjectFiles(projectName);
		
		for(CustomFile customFile : projectFiles)	
		{
			File file;
			FileType fileType; 
			File tempFile = null; 
			
			file = customFile.getFile();
			fileType = customFile.getFileType();
			
			if(file.getAbsolutePath().matches(".*.txt*."))
			{
				tempFile = new File(file.getAbsolutePath().replace(".txt", ".tmp"));			
			}
			else if(file.getAbsolutePath().matches(".*.project*."))
			{
				tempFile = new File(file.getAbsolutePath().replace(".project", ".tmp"));
			}
			
			if(tempFile == null)
			{
				JOptionPane.showMessageDialog(
						frame,
						"An error occured! Project :"+projectName+"' couldn't be saved.", 
						"File data error",JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			try 
			{
				FileWriter fileWriter = new FileWriter(tempFile, true);
				
				for(Data data : projectData)
				{
					if(data.getFileType() == fileType)
					{
						fileWriter.write(data.getDataModel());
					}
				}			
				fileWriter.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				
				JOptionPane.showMessageDialog(
						frame,
						"An error occured! Project : '"+projectName+"' couldn't be saved.", 
						"File data error",JOptionPane.ERROR_MESSAGE);
				return false;
			}	
			copyTempFile(tempFile, file);
			tempFile.delete();
		}
		return true;
	}
	
	/**
	 * 
	 * @param source
	 * @param output
	 */
	private static void copyTempFile(File source, File output)
	{
		InputStream inputStream = null;
	    OutputStream outputStream = null;
	    byte[] buffer = new byte[1024];
	    int length;

	        try 
	        {
	        	inputStream = new FileInputStream(source);
	        	outputStream = new FileOutputStream(output);
	        	
				while ((length = inputStream.read(buffer)) > 0) 
				{
					outputStream.write(buffer, 0, length);
				}
				
				inputStream.close();
				outputStream.close();
	        }
	        catch (IOException e) 
	        {
	        	e.printStackTrace();
	        	return;
			}
	}
}
