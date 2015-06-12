package eterface.tools;

/**
 * Implements utility methods that are system-related, such
 * as dealing with proper path formatting.
 * 
 * @author: Mike Czapik
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.*;

import eterface.modules.viz.EterfaceDirectory;

public class SystemTools {
	private static String sys = System.getProperty("os.name").toLowerCase();
	
	private static final String windows = "windows";
	private static final String linux = "linux";
	
	private static final String linuxHome = "/home/";
	private static final String windowsHome = "C:\\Users\\";
	
	/**
	 * Necessary if at some point in the code a check is needed
	 * for the type of system being run. See serialize in EterfaceDirectory.
	 * 
	 * @return - "windows" if Windows, "linux" if Linux.
	 */
	public static String getSystemType() {
		return sys;
	}
	
	/**
	 * Gets the top level home or users directory for the given system.
	 * 
	 * @return - The top level home or users directory.
	 */
	public static String getHomeDirectory() {
		if(sys.indexOf(windows) >= 0) {
			return windowsHome;
		}
		
		return linuxHome;
	}
	
	/**
	 * Gets the given user's home directory on this system.
	 * 
	 * @param user - The user to find a home directory for.
	 * @return - user home directory.
	 */
	public static String getHomeDirectory(String user) {
		if(sys.indexOf(windows) >= 0) {
			return (windowsHome + user);
		}
		else {
			return (linuxHome + user);
		}
	}
	
	/**
	 * Takes an input path p, and properly formats it according
	 * to system specification. This really needs to be used any time
	 * paths are being worked with to guarantee proper path formatting.
	 * 
	 * For example: If a hardcoded path is being worked with, use this
	 * method to make sure that path is system independent.
	 * 
	 * @param p - Path to format.
	 * @return - Properly formatted path.
	 */
	public static String formatPath(String p) {
		if(sys.indexOf(windows) >= 0) {
			return p.replace("/", "\\");
		}
		return p.replace("\\", "/");
	}
	
	/**
	 * Gets the file type. It should be noted, this does not support 
	 * multiple extensions such as: filename.tar.gz, just as others
	 * do not either. This is due to the fact that some systems allow
	 * dots in the file name.
	 * 
	 * @return - The file type, or the String "null" if the extension/type
	 * is either not present, or cannot be determined.
	 */
	public static String getFileType(String fileName) {
		int index = (fileName.lastIndexOf(".") + 1);
		
		String type = "";
		
		if(index == 0) {
			/*
			 * A file extension is not present, so lets see if we can
			 * extract a MIME type.
			 */
			File f = new File(fileName);
			
			try {
				type = Files.probeContentType(f.toPath());
			}
			catch(Exception e) {
				//Something went wrong - forget finding a type.
				return "null";
			}
			
			//A MIME type could not be determined.
			if(type == null) {
				return "null";
			}
			
			return type; 
		}
		
		return fileName.substring(index, fileName.length());
	}
	
	/**
	 * Splits a given path into individual elements (directory or file).
	 * 
	 * @return - String array of path elements.
	 */
	public static String[] tokenizePath(String p) {
		StringTokenizer st;
		
		if(sys.indexOf(windows) >= 0) {
			st = new StringTokenizer(p, "\\");
		}
		else {
			st = new StringTokenizer(p, "/");
		}
		
		int size = st.countTokens();
		
		String[] tokens = new String[size];
		
		for(int i = 0; st.hasMoreTokens(); i++) {
			tokens[i] = st.nextToken();
		}
		
		return tokens;
	}
	
	/**
	 * Sets the given e-terface directory to "hidden".
	 * 
	 * @param dir - The EterfaceDirectory object to hide
	 * @return - true if set, false otherwise
	 */
	public static boolean setHiddenDirectory(EterfaceDirectory dir) {
		if(sys.indexOf(windows) >= 0) {
			try {
				File d = new File(dir.getDirectoryName());
				Object o = new Object();
				LinkOption opt = null;
				Files.setAttribute(d.toPath(), "dos:hidden", o, opt);
			}
			catch(Exception e) {
				System.err.println("Error occurred when trying to set " +
			                        dir.getDirectoryPath() + " to hidden.");
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		//OS is Linux
		String dirName = dir.getDirectoryName();
		dir.setDirectoryName("." + dirName);
		
		return true;
	}
}
