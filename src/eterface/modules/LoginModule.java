package eterface.modules;

/**
 * Provides login-related services for e-terface.
 * 
 * @author Mike Czapik
 */

import java.io.*;
import java.util.*;

import javax.json.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eterface.tools.SystemTools;

public class LoginModule extends EterfaceModule {
	private ArrayList<String> userList;
	
	private static final String METHOD_USER_LIST = "userList";
	
	public LoginModule() {
		userList = new ArrayList<String>();
	}
	
	/**
	 * Simply retrieve the list of users present on the system. The
	 * client will receive this list, and present it to the user to
	 * choose their account.
	 * 
	 * This is not a great solution, but it is good enough for version 
	 * one.
	 */
	public void findUsers() {
		String usersDirectory = SystemTools.getHomeDirectory();
		
		File homeMain = new File(usersDirectory);
		String[] items = homeMain.list();
		
		for(int i = 0; i < items.length; i++) {
			userList.add(items[i]);
		}
	}
	
	/**
	 * Turns the list of found users into JSON.
	 * 
	 * @return - JSON serialized list of system users.
	 */
	public String usersToJSON() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		JsonArrayBuilder arr = Json.createArrayBuilder();
		
		for(int i = 0; i < userList.size(); i++) {
			arr.add(userList.get(i));
		}
		
		builder.add("userList", arr);
		
		return builder.build().toString();
	}
	
	public Object executeMethod(HttpServletRequest req, HttpServletResponse res) {
		String url = req.getRequestURL().toString();
		String json = "";
		
		//Is the user attempting to log in?
		if(url.indexOf("/login/" + METHOD_USER_LIST) >= 0) {
			json = usersToJSON();
			
			try {
				res.getOutputStream().println(json);
			}
			catch(IOException ioe) {
				//Not sure what to do about this yet...
			}
			catch(Exception e) {
				//Considering refactoring some code for an EterfaceError class
			}
		}
		
		return null;
	}
}
