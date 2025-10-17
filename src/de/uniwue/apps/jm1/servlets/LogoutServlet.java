package de.uniwue.apps.jm1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends HttpServlet {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7614403819416235022L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getSession().invalidate();
		resp.sendRedirect("../index.jsp");
		
	}
	
}
