package eterface.viz;

/**
 * Handles the user directories and implements the API for
 * visualization of the user environment.
 * 
 * @author Mike Czapik
 */

import java.util.*;

import eterface.tools.*;

public class VisualizationModule {
	private HashMap<String, EterfaceHome> userHomeList;
	private ArrayList<String> usersList;
	
	private static final String ERROR_PATH_NOT_FOUND =
	"{ \"error\":\"ERR_PATH_NOT_FOUND\" }";
	
	/**
	 * Creates a new visualization module object with
	 * no users.
	 */
	public VisualizationModule() {
		userHomeList = new HashMap<String, EterfaceHome>();
		usersList = new ArrayList<String>();
	}
	
	/**
	 * Loads the state of a user's home environment into e-terface, and
	 * creates a new EterfaceHome object associated with the given user.
	 * This should be used only for initialization of a new user.
	 * 
	 * @param user - The user to initialize.
	 */
	public void initUser(String user) {
		System.out.println("User: " + user + " is using e-terface");
		
		//We don't want to re-initialize a user already in the system
		for(int i = 0; i < usersList.size(); i++) {
			if(usersList.get(i).equals(user)) {
				return;
			}
		}
		
		usersList.add(user);
		userHomeList.put(user, new EterfaceHome(user));
	}
	
	/**
	 * Takes a peek into this path by returning a JSON
	 * serialization of this directory and its contents.
	 * 
	 * @return - The JSON serialization of this directory, or
	 * a PATH_NOT_FOUND error code if this path does not exist.
	 */
	public String peek(String user, String path) {
		EterfaceHome homeDirectory = userHomeList.get(user);
		EterfaceDirectory dir;
		
		dir = EterfaceDirectory.getDirectoryAbsolute(path, homeDirectory);
		
		if(dir == null) {
			return ERROR_PATH_NOT_FOUND;
		}
		
		return dir.serialize();
	}
}
