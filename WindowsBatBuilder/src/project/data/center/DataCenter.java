package project.data.center;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import project.data.Data;
import project.data.Data.DataType;
import project.data.DataBat;
import project.file.FileManager;

public class DataCenter
{	
	protected static Map<String, ArrayList<Data>> projectsData = new TreeMap<String, ArrayList<Data>>();	
	
	/**
	 * 
	 * @param projectName
	 */
	protected static void initializeProjectData(String projectName)
	{	
		ArrayList<Data> projectData = new ArrayList<Data>();	
		projectsData.put(projectName, projectData);
	}
	
	/**
	 * 
	 * @param projectName
	 * @return true if project was found and saved, false if project doesn't exist
	 */
	protected static boolean saveData(String projectName)
	{	
		ArrayList<Data> projectData;
		ArrayList<Data> parsedData = new ArrayList<Data>();

		projectData = getProjectData(projectName);
		
		if(projectData!= null)
		{
			for(Data data :projectData)
			{		
				data.setDataModel(DataParser.parseObjectModel(data));
				parsedData.add(data);				
			}			
			FileManager.saveDataToFile(projectName, projectData);
			
			return true;
		}	
		return false;
	}
	
	/**
	 * 
	 * @return true if all projects were saved
	 */
	protected static boolean saveAllData()
	{
		for(Map.Entry<String, ArrayList<Data>> projectData : projectsData.entrySet())
		{
			saveData(projectData.getKey());
			return true;
		}
		return false;
	}
		
	/**
	 * 
	 * @param projectName
	 * @param fileToLoad
	 * @return false if data couldn't be loaded
	 */
	public static boolean loadProjectData(String projectName,File fileToLoad)
	{
		ArrayList<Data> fileData;
		
		if((fileData = DataParser.loadFileData(fileToLoad)) != null)
		{
			addDataList(projectName,fileData);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 
	 * @param dataType the type of data to search for
	 * @param keyword the keyword to match
	 * @return object list found that have name field value matching the keyword
	 */
	public static ArrayList<Data> getDataByNameKeyword(String projectName, DataType dataType, String keyword)
	{
		ArrayList<Data> matchingData = new ArrayList<Data>();
		ArrayList<Data> dataList = new ArrayList<Data>();
		String lowerCaseKeyword = keyword.toLowerCase();
		
		dataList = getDataListByType(projectName, dataType);
		
		for(Data data : dataList)
		{
			String name = data.getName();
			
			if(name == null)
			{
				continue;
			}
				
			if(name.equalsIgnoreCase(lowerCaseKeyword))
			{
				matchingData.clear();
				matchingData.add(data);
				return matchingData;
			}
			else if(name.matches(".*"+lowerCaseKeyword+".*"))
			{
				matchingData.add(data);
			}
		}	
		return matchingData;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param dataType
	 * @return	null if not data found
	 */
	public static ArrayList<Data> getDataListByType(String projectName, DataType dataType)
	{
		ArrayList<Data> projectdata = getProjectData(projectName);
		ArrayList<Data> dataFound = new ArrayList<Data>();
		
		if(projectdata != null)
		{
			for(Data data: projectdata)
			{
				if(data.getDataType() == dataType)
				{
					dataFound.add(data);
				}
			}				
		}
		return dataFound;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param dataType
	 * @param name
	 * @return null if data couldn't be found
	 */
	public static Data getDataByNameValue(String projectName, DataType dataType, String name)
	{
		ArrayList<Data> projectdata;
		
		projectdata = getProjectData(projectName);
		
		if(projectdata != null)
		{
			for(Data data : projectdata)
			{
				if(data.getDataType() == dataType)
				{
					String nameValue = data.getName();
					
					if(nameValue.equalsIgnoreCase(name) || nameValue == null)
					{
						return data;
					}
				}									
			}					
		}
		return null;
	}
	
	/**
	 * 
	 * @param projectName
	 * @return
	 */
	protected static DataBat getDataBat(String projectName)
	{
		ArrayList<Data> projectdata;
		
		projectdata = getProjectData(projectName);
		
		if(projectdata != null)
		{
			for(Data data : projectdata)
			{
				if(data.getDataType() == DataType.BAT)
				{
					return (DataBat) data;
				}									
			}					
		}
		return null;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param dataType
	 * @param name
	 * @return
	 */
	public static boolean doesNameExist(String projectName, DataType dataType, String name)
	{
		ArrayList<Data> dataList = new ArrayList<Data>();
		
		dataList = getDataListByType(projectName, dataType);
		
		for(Data data : dataList)
		{
			String dataName = data.getName().toLowerCase();
				
			if(dataName.equalsIgnoreCase(name))
			{
				return true;
			}
		}	
		return false;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param dataToAdd
	 */
	protected static boolean addData(String projectName, Data dataToAdd)
	{
		ArrayList<Data> projectData = getProjectData(projectName);
		
		if(projectData != null)
		{
			projectData.add(dataToAdd);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param newProjectData
	 */
	protected static boolean addDataList(String projectName, ArrayList<Data> newProjectData)
	{	
		ArrayList<Data> projectData = getProjectData(projectName);
		
		if(projectData != null)
		{
			projectData.addAll(newProjectData);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param dataToRemove
	 */
	protected static boolean removeData(String projectName, Data dataToRemove)
	{
		ArrayList<Data> projectData = getProjectData(projectName);
		
		if(projectData != null)
		{
			projectData.remove(dataToRemove);
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param projectName
	 * @return
	 */
	protected static ArrayList<Data> getProjectData(String projectName)
	{
		for(Map.Entry<String,ArrayList<Data>> projectData : projectsData.entrySet())
		{
			if(projectData.getKey().equals(projectName))
			{
				return projectData.getValue();
			}
		}		
		return null;
	}
}
