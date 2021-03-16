package project.data.center;
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import project.data.Data;

public class DataParser extends DataCenter
{	
	private static Queue<String> objectsClassName = new LinkedList<String>();
	private static Queue<String> booleanType = new LinkedList<String>();
	private static Queue<String> stringType = new LinkedList<String>();
	private static Queue<Integer> integerType = new LinkedList<Integer>();
	private static Queue<String> arrayStringType = new LinkedList<String>();
	private static Queue<Integer> arrayIntegerType = new LinkedList<Integer>();
	
	private static Queue<ArrayList<String>> queuedArrayListStrings = new LinkedList<ArrayList<String>>();
	private static Queue<ArrayList<Integer>> queuedArrayListIntegers = new LinkedList<ArrayList<Integer>>();		
	
	/**
	 * Use lang.reflection to get every field values declared from the object parameter.
	 * The object model is then parsed to a String. The head of the string represents the class name, followed by
	 * the object field values enclosed in brackets "()".
	 * 
	 * @param <T> Object Type template
	 * @param object the object to parse
	 * @return String the parsed object
	 */
	protected static <T> String parseObjectModel(T object)
	{
		Class<?> objectClass;
		String className;
		Field[] fields;
		String parsedObject = "";
		
		objectClass = object.getClass();
		className = objectClass.getName();
		fields = objectClass.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		
		parsedObject = parsedObject.concat(className+"\n(\n");	//Class Name
		
		for(Field field : fields)
		{
			try 
			{	
				String variableType = field.getGenericType().toString();
				Object variableValue = field.get(object);
				
				parsedObject = parsedObject.concat(variableType); 	
				if(variableValue != null)//Var type
				{
					parsedObject = parsedObject.concat(":"+variableValue.toString()+"~\n");	//Var value				
				}
				else
				{
					parsedObject = parsedObject.concat(":~\n");	//null var value
				}
			} 
			catch (IllegalArgumentException | IllegalAccessException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}	
		parsedObject = parsedObject.concat(")\n");	//Object end
		
		return parsedObject;
	}
	
	/**
	 * Read a single file containing 1 or more object(s). The data model is then parsed to an object model using reflection 
	 * to set every declared values of the object fields.
	 * 
	 * @param objectDataFile	a File to parse.
	 */
	protected static ArrayList<Data> loadFileData(File fileToLoad)
	{	
		ArrayList<Data> objectsData = new ArrayList<Data>();
		
		if(readObjectData(fileToLoad))
		{
			while(!objectsClassName.isEmpty())
			{
				Data data = new Data();
				
				if((data = getObjectData()) != null)
				{
					objectsData.add(data);
				}
				else
				{
					return null;
				}
			}				
		}	
		return objectsData;
	}
	
	/**
	 * Parse data from a file to an object model.
	 * 
	 * @param fileData
	 * @return
	 */
	private static boolean readObjectData(File fileData)
	{		
		try 
		{
			String objectClassName = "";
			String genericValueType = "";
			String value = "";
			boolean isClassName = true;
			boolean isVariableType = false;
			boolean isValue = false;
			boolean isArray = false;
			boolean isGenericParamType = false;
			boolean isStillValue = false;
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileData), "UTF-8"));
			
			for (String line; (line = reader.readLine()) != null;) //Read file line by line
			{
				String genericParamType = "";
				String element = "";
				
				if(!isStillValue)
				{
					value = "";
					genericValueType = "";
				}
				
				for(char c : line.toCharArray())
				{
					
					switch(c)
					{
					case '(':
						isClassName = false;
						isVariableType = true;
						continue;
						
					case ':':	
						if(!isValue)
						{
							isVariableType = false;
							isValue = true;
							continue;																			
						}
						break;
					case '<':
						isVariableType = false;
						isGenericParamType = true;
						continue;
						
					case '>':
						isGenericParamType = false;
						continue;
						
					case '[':
						isArray = true;
						isValue = false;
						continue;
						
					case ']':
						continue;
								
					case '~':	
						if(value.length() == 0)
						{
							value = null;
						}
						if(element.length() == 0)
						{
							element = null;
						}
						
						if(genericValueType.equals(Boolean.class.toString()))
						{
							booleanType.add(value);
						}
						else if(genericValueType.equals(String.class.toString()))
						{
							stringType.add(value);			
						}
						else if(genericValueType.equals(int.class.toString()))
						{
							integerType.add(Integer.parseInt(value));
						}		
						else if(genericValueType.equals(ArrayList.class.getTypeName()))
						{
							if(genericParamType.equals(String.class.getTypeName()))
							{
								arrayStringType.add(element);
							}
							if(genericParamType.equals(Integer.class.getTypeName()))
							{
								arrayIntegerType.add(Integer.parseInt(element));
							}
						}	
						
						isArray = false;
						isValue = false;
						isVariableType = true;
						continue;
						
					case ')':
						objectsClassName.add(objectClassName);
						objectClassName = "";
						isClassName = true;
						break;
					}
					
					if(isClassName && c != ')')
					{
						objectClassName = objectClassName.concat(Character.toString(c));
					}
					else if(isVariableType)
					{
						genericValueType = genericValueType.concat(Character.toString(c));
					}
					else if(isGenericParamType)
					{
						genericParamType = genericParamType.concat(Character.toString(c));
					}
					else if(isValue)
					{
						value = value.concat(Character.toString(c));			
					}
					else if(isArray)
					{
						if(c == ',')
						{		
							if(element.length() == 0)
							{
								element = null;
							}
							
							if(genericParamType.equals(String.class.getTypeName()))
							{
								arrayStringType.add(element);
								element = "";
							}
							else if(genericParamType.equals(Integer.class.getTypeName()))
							{
								arrayIntegerType.add(Integer.parseInt(element));
								element = "";
							}
						}
						else if(c != ' ')
						{
							element = element.concat(Character.toString(c));
						}
					}						
				}
				addArrayElementToQueue(genericParamType);
				if(isValue)
				{
					isStillValue = true;
					value = value.concat("\n");
				}
				else
				{
					isStillValue = false;
				}
			}		
			reader.close();
		}
		catch (IOException e1) 
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Empty primitive type variable queue and add them to the specific list.
	 * This list will be use to set the corresponding object reflected field later
	 */
	private static void addArrayElementToQueue(String genericParamType)
	{	
		String genericStringType = String.class.getTypeName();
		String genericIntType = Integer.class.getTypeName();
		
		if(genericParamType.equals(genericStringType))
		{
			ArrayList<String> arrayStrings = new ArrayList<String>();		
			while(!arrayStringType.isEmpty())
			{
				arrayStrings.add(arrayStringType.remove());
			}				
			queuedArrayListStrings.add(arrayStrings);
		}
		else if(genericParamType.equals(genericIntType))
		{
			ArrayList<Integer> integers = new ArrayList<Integer>();		
			while(!arrayIntegerType.isEmpty())
			{
				integers.add(arrayIntegerType.remove());
			}				
			queuedArrayListIntegers.add(integers);
		}
	}
	
	
	/**
	 * Set declared fields of an object to the values contained in multiple queues.
	 * 
	 * @return
	 */
	private static Data getObjectData()
	{
		Data objectData;
		Class<?> modelClass;
		Object newObject;
		Field[] fields;
		
		try 
		{	
			//Instantiate a new object from class name stored in classNames
			modelClass = Class.forName(objectsClassName.poll());
			newObject = modelClass.getDeclaredConstructor().newInstance();
			
			//Get declared object fields
			fields = modelClass.getDeclaredFields();
			AccessibleObject.setAccessible(fields, true);
			
			for(Field field : fields) 
			{
				Class<?> fieldClassType = field.getType();
				
				if(fieldClassType == boolean.class)
				{
					//field.setBoolean(newObject, false);
				}
				else if(fieldClassType == String.class)
				{
					field.set(newObject, stringType.poll());
				}
				else if(fieldClassType == int.class)
				{
					field.setInt(newObject, integerType.poll());					
				}
				else if(fieldClassType == ArrayList.class || fieldClassType == List.class)
				{
					Type genericType;		
					ParameterizedType fieldParamType;
					Type[] fieldParamTypes;
					
					genericType = field.getGenericType();
					fieldParamType  = (ParameterizedType) genericType;
					fieldParamTypes =  fieldParamType.getActualTypeArguments();
					
					for(Type paramType : fieldParamTypes)
					{
						Class<?> typeClass = (Class<?>) paramType;
						
						if(typeClass == String.class)
						{
							field.set(newObject, queuedArrayListStrings.poll());
							
						}
						else if(typeClass == int.class)
						{
							field.set(newObject, queuedArrayListIntegers.poll());			
						}
					}
					
				}
			}	
		} 
		catch (IllegalArgumentException |
				IllegalAccessException 	| 
				NoSuchElementException 	| 
				ClassNotFoundException 	| 
				NoSuchMethodException 	|
				SecurityException 		|
				InstantiationException 	|
				InvocationTargetException e) 
		{
			e.printStackTrace();
			return null;
		}	
		
		objectData = (Data) newObject;
		
		return objectData;
	}
}
