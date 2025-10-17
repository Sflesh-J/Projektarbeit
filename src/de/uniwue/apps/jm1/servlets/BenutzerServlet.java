package de.uniwue.apps.jm1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniwue.apps.jm1.Main;
import de.uniwue.apps.jm1.User;

public class BenutzerServlet extends HttpServlet{


	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2333470503002725173L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			User user = (User) req.getSession().getAttribute("user");
			String s = req.getParameter(Main.USER_UID_PARAM);
			boolean isProfil = "true".equals(req.getParameter("profil"));
			boolean toAdmin = false;
			User userToChange = null;
			if(s != null) {
				userToChange = User.getUserbyUID(s);
			}
			String method = req.getParameter("method");
			
			
			if(isProfil) {
				
				String pin = req.getParameter("pin");
				User.setPassword(pin, user.getUid());
				resp.sendRedirect("profil.jsp?neuerPin=true");
				
			}
			else if (user.isAdmin() && !isProfil) {
				switch(method) {
				case "aktivieren":
					User.changeAktiv(userToChange, "TRUE");
					toAdmin = backToAdmin(userToChange);
					break;
				case "deaktivieren":
					User.changeAktiv(userToChange, "FALSE");
					toAdmin = backToAdmin(userToChange);
					break;
				case "name-change":
					String vorname = req.getParameter("vorname"); 
					String nachname = req.getParameter("nachname");
					
					if(!vorname.isEmpty())
						User.changeVorname(userToChange, vorname);
					if(!nachname.isEmpty())
						User.changeNachname(userToChange, nachname);
					toAdmin = backToAdmin(userToChange);
					break;
				case "status-change":
					User.changeStatus(userToChange, req.getParameter("status"));
					toAdmin = backToAdmin(userToChange);
					break;
				case "pin-change": //auf ein String ändern
					String pin = req.getParameter("pin");
					User.setPassword(pin, userToChange.getUid());
					toAdmin = backToAdmin(userToChange);
					break;
				case "neuer-Benutzer":
					String vornameN = req.getParameter("vorname"); 
					String nachnameN = req.getParameter("nachname");
					String status = req.getParameter("status");
					String isAdmin = req.getParameter("admin");
					String guthaben = req.getParameter("guthaben");
					String pwd = req.getParameter("passwort");
					
					User.newUser(vornameN, nachnameN, status, isAdmin, guthaben, pwd);
					toAdmin = backToAdmin(userToChange);
					break;
				}	
				if(toAdmin) {
					resp.sendRedirect("admin_user.jsp?uid=" + userToChange.getUid() );
				}else if(!toAdmin) {
					resp.sendRedirect("admin_user.jsp");
				}
			}
		} catch (Exception ex) {
			
		}
	}
	
	
	private static boolean backToAdmin(User userToChange) {
		if(userToChange != null) {
			return true;
		}else {
			return false;
		}
	}
}

