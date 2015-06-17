package eterface.modules;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;

/**
 * Provides system-related services for the client such as file
 * read or write.
 * 
 * Sorry about the confusion... I was not concerned with spending too much
 * time on a better name for this since it isn't important for version 1.0.
 * 
 * @author Mike Czapik
 */

public class ServicesModule extends EterfaceModule {
	/*
	 * The user attempted to get a resource that is either in use, system-related, or
	 * some other reason. See FileSystemException in the Java documentation.
	 */
	private static final String ERROR_FILE_IRRETRIEVABLE = "{ \"error\":\"ERR_FILE_IRRETRIEVABLE\" }";
	
	private static final String METHOD_READ = "read";
	
	public ServicesModule() { }
	
	public Object executeMethod(HttpServletRequest req, HttpServletResponse res) {
		String url = req.getRequestURL().toString();
		String json = "";
		
		if(url.indexOf("/services/" + METHOD_READ) >= 0) {
			String path = req.getParameter("path");
			File f = new File(path);
			
			try {
				Files.copy(f.toPath(), res.getOutputStream());
			}
			catch(ClientAbortException cae) {
				/*
				 * If a user cancels (closes a connection) a request,
				 * we will get an exception server side. This is here
				 * to bring awareness to that particular situation,
				 * and to keep what should be a harmless exception from
				 * printing to standard err.
				 * 
				 * Perhaps later versions can handle this better?
				 */
			}
			catch(FileSystemException fse) {
				try {
					res.getOutputStream().println(ERROR_FILE_IRRETRIEVABLE);
				}
				catch(IOException ioe) {
					System.err.println("Could not send error message.");
				}
			}
			catch(IOException ioe) {
				System.err.println("Could not write resource to output stream.");
			}
		}
		
		return null;
	}
}
