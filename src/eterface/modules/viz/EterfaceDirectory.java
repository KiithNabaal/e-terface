package eterface.modules.viz;

/**
 * Represents a user directory, and is responsible for representing
 * the entire user home/desktop space in e-terface.
 * 
 * @author Mike Czapik
 */

import java.io.*;
import java.util.*;
import javax.json.*;

import eterface.tools.*;

public class EterfaceDirectory {
	private ArrayList<File> fileList;
	private ArrayList<EterfaceDirectory> directoryList;
	private String directoryName;
	private String directoryPath;
	private int numberOfItems;
	
	/**
	 * Creates an empty/placeholder e-terface directory
	 */
	public EterfaceDirectory() {
		fileList = null;
		directoryList = null;
		directoryName = "";
		numberOfItems = 0;
		directoryPath = "";
	}
	
	/**
	 * Creates a new e-terface directory
	 * 
	 * @param dirName - Name of the directory.
	 */
	public EterfaceDirectory(String dirName) {
		fileList = new ArrayList<File>();
		directoryList = new ArrayList<EterfaceDirectory>();
		directoryName = dirName;
		numberOfItems = 0;
		directoryPath = "";
	}
	
	/**
	 * Creates an e-terface directory based on a File representation
	 * of the same directory.
	 * 
	 * @param dir - File representation of a directory.
	 */
	public EterfaceDirectory(File dir) {
		fileList = new ArrayList<File>();
		directoryList = new ArrayList<EterfaceDirectory>();
		directoryName = dir.getName();
		numberOfItems = 0;
		directoryPath = dir.getAbsolutePath();
	}
	
	/**
	 * Returns the number of items in this e-terface directory.
	 * 
	 * @return - The number of items in this e-terface directory.
	 */
	public int getNumberOfItems() { return numberOfItems; }
	
	/**
	 * Returns the name of this e-terface directory.
	 * 
	 * @return - The name of this e-terface directory.
	 */
	public String getDirectoryName() { return directoryName; }
	
	/**
	 * Returns the path of this e-terface directory.
	 * 
	 * @return - The path of this e-terface directory.
	 */
	public String getDirectoryPath() { return directoryPath; }
	
	/**
	 * Returns the list of files in this e-terface directory.
	 * 
	 * @return - The list of files in this e-terface directory.
	 */
	public ArrayList<File> getFileList() { return fileList; }
	
	/**
	 * Returns the list of directories in this e-terface directory.
	 * 
	 * @return - The list of directories in this e-terface directory.
	 */
	public ArrayList<EterfaceDirectory> getDirectoryList() { return directoryList; }
	
	/**
	 * Gets the given e-terface directory within the calling directory.
	 * 
	 * @param directoryName - String name of the directory.
	 * @return - The e-terface directory specified by directoryName.
	 */
	public EterfaceDirectory getDirectoryLocal(String directoryName) {
		if((this.directoryName).equals(directoryName)) {
			return this;
		}
		
		EterfaceDirectory dir;
		
		for(int i = 0; i < directoryList.size(); i++) {
			dir = directoryList.get(i);
			String name = dir.getDirectoryName();
			
			if(directoryName.equals(name)) {
				return dir;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the e-terface directory at the given path. If a file path
	 * is given, then the directory that stores that file is returned
	 * instead.
	 * 
	 * @param path - String path of the directory.
	 * @param home - User home directory the path should exist in.
	 * @return - The EterfaceDirectory, and null if it does not exist.
	 */
	public static EterfaceDirectory getDirectoryAbsolute(String path, 
			                                             EterfaceHome home) {
		String[] pathItems = SystemTools.tokenizePath(path);
		String user = home.getUserName();
		
		//Is the last item a directory, or a file?
		int last = pathItems.length - 1;
		if(!SystemTools.getFileType(pathItems[last]).equals("null")) {
			//Last item is a file, "move up" by one to get its directory
			last--;
		}
		
		String targetDirectory = pathItems[last];
		
		//Ignore non-user based directories
		int index;
		for(index = 0; (index < pathItems.length) && 
				   (!pathItems[index].equals(user)); index++);
		
		//No such path
		if(index == pathItems.length) { return null; }
		
		EterfaceDirectory d = home.getDirectoryLocal(pathItems[index]);
		
		while(index < pathItems.length) {
			d = d.getDirectoryLocal(pathItems[index]);
			
			if(d.getDirectoryName().equals(targetDirectory)) { return d; }
			
			index++;
		}
		
		//No such path - items left over
		return null;
	}
	
	/**
	 * Sets the number of items in this e-terface directory.
	 * 
	 * @param size - Size of the e-terface directory in items.
	 */
	public void setNumberOfItems(int size) {
		numberOfItems = size;
	}
	
	/**
	 * Sets the name of this e-terface directory
	 * 
	 * @param name - The name of this e-terface directory.
	 */
	public void setDirectoryName(String name) {
		directoryName = name;
	}
	
	/**
	 * Adds an e-terface directory to this e-terface directory.
	 * 
	 * @return - Returns true if a new directory was created, and
	 * false otherwise.
	 */
	public boolean addDirectory(EterfaceDirectory directory) {
		directoryList.add(directory);
		numberOfItems++;
		
		File dir = new File(directory.getDirectoryPath());
		boolean result = dir.mkdir();
		
		//Directory already exists - returns false
		if(!result) {
			return false;
		}
		
		//Directory did not exist, and file was created
		return true;
	}
	
	/**
	 * Adds a file to this e-terface directory.
	 * 
	 * @return - Returns true if a new file was created, and
	 * false otherwise.
	 */
	public boolean addFile(File file) {
		try {
			file.createNewFile();
			fileList.add(file);
			numberOfItems++;
		}
		catch(IOException ioe) {
			System.err.println("Error occurred when trying to add " +
		                       file.getName() + ".");
			ioe.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Builds a JSON represention of this e-terface directory which
	 * includes all items in this directory. This is an example
	 * JSON object:
	 * 
	 * {
	 * 	"directoryName": "value",
	 * 	"directoryPath": "/dir/value",
	 *  "numberOfItems": 4,
	 * 	"directoryList": [
	 * 				   	{
	 * 						"directoryName": "value1",
	 * 						"directoryPath": "/dir/value/value1",
	 * 						"numberOfItems": 1
	 * 					},
	 * 					{
	 * 						"directoryName": "value2",
	 * 						"directoryPath": "/dir/value/value2",
	 * 						"numberOfItems": 1
	 * 					}
	 * 				   ],
	 * 	"fileList": [
	 * 			  	{
	 * 					"fileName": "value1.txt",
	 * 					"fileSize": 1,
	 * 					"fileType": "txt",
	 * 					"filePath": "/dir/value/value1.txt"
	 * 				},
	 * 				{
	 * 					"fileName": "value2.txt",
	 * 					"fileSize": 1,
	 * 					"fileType": "txt",
	 * 					"filePath": "/dir/value/value2.txt"
	 * 				}
	 * 			  ]
	 * }
	 * 
	 * @return - The JSON representation of this e-terface directory
	 * and its contents.
	 */
	public String serialize() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		
		String p = directoryPath;
		
		/*
		 * Ok, since Windows path names require an escape character
		 * '\\', we have to replace this with what JavaScript expects
		 * (which is apparently four slashes). If we do not do this, the
		 * client will get a pathname like this: C:\\Users\\user
		 * which is not a valid pathname on Windows.
		 */
		if(SystemTools.getSystemType().indexOf("windows") >= 0) {
			p = p.replace("\\", "\\\\");
		}
		
		builder.add("directoryName", directoryName);
		builder.add("directoryPath", p);
		builder.add("numberOfItems", numberOfItems);
		
		JsonArrayBuilder dirBuilder = Json.createArrayBuilder();
		
		for(int i = 0; i < directoryList.size(); i++) {
			EterfaceDirectory dir = directoryList.get(i);
			JsonObjectBuilder item = Json.createObjectBuilder();
			
			p = dir.getDirectoryPath();
			
			if(SystemTools.getSystemType().indexOf("windows") >= 0) {
				p = p.replace("\\", "\\\\");
			}
			
			item.add("directoryName", dir.getDirectoryName());
			item.add("directoryPath", p);
			item.add("numberOfItems", dir.getNumberOfItems());
			
			dirBuilder.add(item);
		}
		
		builder.add("directoryList", dirBuilder);

		JsonArrayBuilder fileBuilder = Json.createArrayBuilder();
		
		for(int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			JsonObjectBuilder item = Json.createObjectBuilder();
			
			p = file.getAbsolutePath();
			
			if(SystemTools.getSystemType().indexOf("windows") >= 0) {
				p = p.replace("\\", "\\\\");
			}
			
			item.add("fileName", file.getName());
			item.add("fileSize", file.getTotalSpace());
			item.add("fileType", SystemTools.getFileType(file.getName()));
			item.add("filePath", p);
			
			fileBuilder.add(item);
		}
		
		builder.add("fileList", fileBuilder);

		return builder.build().toString();
	}
}
