package eterface.modules;

/**
 * Parent class for all modules. Right now it has only one abstract method.
 * All modules should inherit from this class, with perhaps the exception
 * of the security module (not sure yet).
 */

import javax.servlet.*;
import javax.servlet.http.*;

public abstract class EterfaceModule {
	
	/**
	 * Responds to and handles method execution.
	 * 
	 * @param req - Client request object.
	 * @param rsp - Provided if it is necessary to access the output stream.
	 * 
	 * @return - Optional Object return (null). If an object is returned,
	 * make sure it is handled in ServicesServlet.
	 */
	public abstract Object executeMethod(HttpServletRequest req, HttpServletResponse res);
}
