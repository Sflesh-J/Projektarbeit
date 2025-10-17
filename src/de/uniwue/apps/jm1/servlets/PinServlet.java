package de.uniwue.apps.jm1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniwue.apps.jm1.User;


public class PinServlet extends HttpServlet {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7957964794050888815L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			
			String uid = req.getParameter("uid");
			User user = User.getUserbyUID(uid);

			if (user.getPasswort() == null) {
				req.getSession().setAttribute("user", user);
				resp.sendRedirect("../index.jsp");
			} else {
				User currentUser = (User) req.getSession().getAttribute("user");
				if (currentUser != null && currentUser.getUid() == user.getUid()) {
					resp.sendRedirect("../index.jsp");
				} else {
					req.getSession().removeAttribute("user");
					resp.sendRedirect("../login.jsp?uid=" + uid);
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			resp.sendRedirect("../benutzer.jsp");
		}
		
		
		
	}
	
}
