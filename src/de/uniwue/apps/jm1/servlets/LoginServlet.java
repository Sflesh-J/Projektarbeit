package de.uniwue.apps.jm1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniwue.apps.jm1.Main;
import de.uniwue.apps.jm1.User;

public class LoginServlet extends HttpServlet {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2628092446213216821L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			String uid = req.getParameter("uid");
			String vreq = req.getParameter("versuch");
			int versuch = vreq != null ? Integer.parseInt(vreq) : 0; 
			versuch = versuch + 1;
			User user = User.getUserbyUID(uid);
			
			if ( user.getPasswort() == null || user.getPasswort().equals(User._getPassword(req.getParameter(Main.PASSWORT_PARAM)))) {
				req.getSession().setAttribute("user", user);
				resp.sendRedirect("../index.jsp");
			} else {
				resp.sendRedirect("../login.jsp?uid=" + uid + "&versuch=" + versuch);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			resp.sendRedirect("../benutzer.jsp");
		}
	}
	
}
