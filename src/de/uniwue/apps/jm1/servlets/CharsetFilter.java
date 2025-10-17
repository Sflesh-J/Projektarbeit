package de.uniwue.apps.jm1.servlets;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CharsetFilter extends HttpFilter {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3622070175099276129L;

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		chain.doFilter(req, res);
	}

	
	
	
}
