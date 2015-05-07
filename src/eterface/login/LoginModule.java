package eterface.login;

/**
 * Provides login-related services for e-terface.
 * 
 * @author Mike Czapik
 */

import java.io.*;
import java.util.*;
import javax.json.*;

import eterface.tools.SystemTools;

public class LoginModule {
	private ArrayList<String> userList;
	
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
}
