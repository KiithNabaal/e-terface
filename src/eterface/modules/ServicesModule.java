package eterface.modules;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This will be worked on in the future. The point of the services module
 * is to allow clients to run at least some programs.
 * 
 * For example, if a Linux user would like a terminal, the client will
 * provide the visualization, and the ServicesModule will provide the 
 * execution.
 * 
 * Sorry about the confusion... I was not concerned with spending too much
 * time on a better name for this since it isn't important for version 1.0.
 * 
 * @author Mike Czapik
 */

public class ServicesModule extends EterfaceModule {
	public ServicesModule() { }
	
	public Object executeMethod(HttpServletRequest req, HttpServletResponse res) {
		return null;
	}
}
