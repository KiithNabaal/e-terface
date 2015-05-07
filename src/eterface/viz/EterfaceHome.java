package eterface.viz;

/**
 * Represents a user's home directory.
 * 
 * @author Mike Czapik
 */

import java.io.*;
import java.util.*;

import eterface.tools.*;

public class EterfaceHome extends EterfaceDirectory {
	//User this home directory/desktop belongs to
	private String userName;
	
	private String homeJSON;
	
	private String homePath;
	
	/*
	 * The configuration directory is private - this should not
	 * be exposed to the user. Only the admin should have control
	 * over this directory and its contents.
	 * 
	 * This may be needed later... not used right now.
	 */
	private File configDir;
	
	private final String CONFIG_NAME = "EterfaceConfig";
	private final String CONFIG_PATH = SystemTools.formatPath("/" + CONFIG_NAME);
	
	/**
	 * Creates an EterfaceHome object - this object contains the
	 * state of the given user's home directory in its entirety.
	 * 
	 * @param user - The user to model a home directory for.
	 * @return - An EterfaceHome instance. Models the user's home directory.
	 */
	public EterfaceHome(String user) {
		super(new File(SystemTools.getHomeDirectory(user)));
		homePath = SystemTools.getHomeDirectory(user);
		userName = user;
		
		fillDirectory(this);
	}
	
	private void fillDirectory(EterfaceDirectory directory) {
		File parentDirectory = new File(directory.getDirectoryPath());
		System.out.println("Directory being inspected: " + directory.getDirectoryPath());
		File[] directoryContents = parentDirectory.listFiles();
		
		//Exception whenever accessing protected directories in Windows
		if(directoryContents == null) { return; }
		
		//Process the parent directory first
		for(int i = 0; i < directoryContents.length; i++) {
			File item = directoryContents[i];
			
			if(item.isFile()) {
				directory.addFile(item);
			}
			else {
				String fileName = item.getName();
				if(fileName.indexOf(CONFIG_NAME) < 0) {
					directory.addDirectory(new EterfaceDirectory(item));
				}
			}
		}
		
		ArrayList<EterfaceDirectory> dirList = directory.getDirectoryList();
		
		//Then process sub directories
		for(int i = 0; i < dirList.size(); i++) {
			String next = dirList.get(i).getDirectoryPath();
			fillDirectory(dirList.get(i));
		}
	}
	
	/**
	 * Gets the user associated with this home directory.
	 * 
	 * @return - Returns the user who owns this directory.
	 */
	public String getUserName() { return userName; }
	
	/**
	 * Gets the home directory path associated with this home directory.
	 * 
	 * @return - The home path name.
	 */
	public String getHomePath() { return homePath; }
}
