package eterface.modules.viz;

/**
 * Handles the user directories and implements the API for
 * visualization of the user environment.
 * 
 * @author Mike Czapik
 */

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eterface.modules.EterfaceModule;
import eterface.tools.*;

public class VisualizationModule extends EterfaceModule {
	private HashMap<String, EterfaceHome> userHomeList;
	private ArrayList<String> usersList;
	
	private static final String ERROR_PATH_NOT_FOUND =
	"{ \"error\":\"ERR_PATH_NOT_FOUND\" }";
	
	private static final String METHOD_PEEK = "peek";
	private static final String METHOD_INIT_USER = "initUser";
	
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
	
	public Object executeMethod(HttpServletRequest req, HttpServletResponse res) {
		String url = req.getRequestURL().toString();
		String json = "";
		
		//Did we receive a user to track?
		if(url.indexOf("/viz/" + METHOD_INIT_USER) >= 0) {
			String user = req.getParameter("user");
			
			initUser(user);
			
			String homePath = SystemTools.getHomeDirectory(user);
			json = peek(user, homePath);
		}
		
		//Is the user attempting to view the contents of a directory?
		if(url.indexOf("/viz/dir/" + METHOD_PEEK) >= 0) {
			String user = req.getParameter("user");
			String path = req.getParameter("dir");
			json = peek(user, path);
		}
		
		try {
			res.getOutputStream().println(json);
		}
		catch(IOException ioe) {
			//Not sure what to do about this yet...
		}
		catch(Exception e) {
			//Considering refactoring some code for an EterfaceError class
		}
		
		return null;
	}
}
