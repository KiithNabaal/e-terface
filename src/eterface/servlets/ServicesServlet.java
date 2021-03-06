package eterface.servlets;

/**
 * Provides web services for the client such as visualization, and
 * system-related services such as running (some) programs.
 * 
 * Note: Here, a "method" is used as follows:
 * http://url:port/something/method?params
 * 
 * It is the name of what is being called in the API, not a reference
 * to an HTTP method.
 * 
 * Currently the following methods are implemented:
 *  - /login/userList : returns the list of users on this system.
 *  (ex: http://localhost:8080/e-terface/login/userList)
 * 
 *  - /viz/initUser : initializes this users e-terface environment, and returns
 *                    their home environment as JSON. Its param is user. See
 *                    further javadoc in VisualizationModule.
 *  (ex: http://localhost:8080/e-terface/viz/initUser?user=myuser)
 * 
 * 	- /viz/dir/peek : returns the contents of the specified directory. Its params
 *                    are user and dir.
 * 	(ex: http://localhost:8080/e-terface/viz/dir/peek?user=myuser&dir=/usr/home/myuser/Desktop)
 * 
 *  - /services/read : reads the specified resource from the system. Its param is
 *  				   path.
 *  
 *   (ex: http://localhost:8080/e-terface/services/read?path=/usr/home/myuser/res.ext)
 * 
 * @author: Mike Czapik
 * 
 */

import java.io.*;
import java.net.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.catalina.connector.ClientAbortException;

import eterface.modules.viz.*;
import eterface.tools.*;
import eterface.modules.*;
import eterface.modules.LoginModule;
import eterface.modules.ServicesModule;
import eterface.modules.viz.VisualizationModule;

public class ServicesServlet extends HttpServlet implements ServletContextListener {
	private VisualizationModule vizModule = new VisualizationModule();
	private ServicesModule servModule = new ServicesModule();
	private EterfaceSecurity securityModule = new EterfaceSecurity(true);
	private LoginModule loginModule = new LoginModule();
	
	/*
	 * The character format sent by the user is not supported by
	 * this server or application. As of now, e-terface only supports
	 * ASCII URLs.
	 */
	private static final String ERROR_URL_DECODE = "{ \"error\":\"ERR_URL_DECODE\" }";
	
	private static final String ERROR_NO_SUCH_METHOD = "{ \"error\":\"ERR_NO_SUCH_METHOD\" }";
	
	private static final String ERROR_NO_SUCH_SERVICE = "{ \"error\":\"ERR_NO_SUCH_SERVICE\" }";
	
	//The Strings below indicate the service a user is requesting
	private static final String VISUALIZATION = "/viz/";
	private static final String LOGIN = "/login/";
	private static final String SERVICES = "/services/";
	
	//Process actions whenever Tomcat starts up
	public void contextInitialized(ServletContextEvent event) {
		
	}
	
	//Process actions whenever Tomcat shuts down
	public void contextDestroyed(ServletContextEvent event) {
		
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		loginModule.findUsers();
	}
	
	public void destroy() {
		
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		res.addHeader("Access-Control-Allow-Origin", "*");
		
		System.out.println("Got a request");
		String query = req.getQueryString();
		
		//Check to make sure that the URL encoding is proper
		try {
			if(query != null) {
				query = URLDecoder.decode(query, "ASCII");
			}
		}
		catch(UnsupportedEncodingException ex) {
			System.err.println("A user is using a non-ascii encoding.");
			
			try {
				res.getOutputStream().println(ERROR_URL_DECODE);
			}
			catch(Exception e) {
				System.err.println("Could not deliver ERROR_URL_DECODE_JSON");
				return;
			}
			return;
		}

		String moduleService = req.getRequestURL().toString();
		
		try {
			if(moduleService.indexOf(LOGIN) >= 0) {
				loginModule.executeMethod(req, res);
			}
			
			else if(moduleService.indexOf(VISUALIZATION) >= 0) {
				vizModule.executeMethod(req, res);
			}
			
			else if(moduleService.indexOf(SERVICES) >= 0) {
				servModule.executeMethod(req, res);
			}
			
			else {
				//Could not match given service with known services.
				res.getOutputStream().println(ERROR_NO_SUCH_SERVICE);
			}
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
