package de.uniwue.apps.jm1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniwue.apps.jm1.Item;
import de.uniwue.apps.jm1.Main;
import de.uniwue.apps.jm1.User;

public class ItemServlet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3087724972739665713L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			User user = (User) req.getSession().getAttribute("user");
			String s = req.getParameter(Main.ITEM_UID_PARAM);
			Item itemToChange = null;
			if(s != null) {
				itemToChange = Item.getItem(Integer.parseInt(s));
			}
			String method = req.getParameter("method");
			
			
			if(!user.isAdmin()) {
			resp.sendRedirect("index.jsp");
			}else {
				switch(method) {
				case "aktivieren":
					Item.changeAktiv(itemToChange, "TRUE");
					break;
				case "deaktivieren":
					Item.changeAktiv(itemToChange, "FALSE");
					break;
				case "name-change":
					String name = req.getParameter("name"); 
					Item.changeName(itemToChange, name);
					break;
				case "neues-item":
					String nameN = req.getParameter("name");  
					String datum = req.getParameter("datumAb");

					String betrag = req.getParameter("preis");					
					betrag = betrag.replace(",", ".");
					double betragDouble = Double.parseDouble(betrag);
					int betragInt = (int) Math.round(betragDouble * 100);
					
					Item.newItem(nameN, betragInt, datum);
					
					break;
				case "change-preis":
					String datumb = req.getParameter("datumAb");
					
					String betragb = req.getParameter("preis");					
					betragb = betragb.replace(",", ".");
					double betragDoubleb = Double.parseDouble(betragb);
					int betragIntb = (int) Math.round(betragDoubleb * 100);
					
					Item.changePreis(itemToChange,betragIntb, datumb);
					break;
				}	
			}
			if(itemToChange != null) {
				resp.sendRedirect("admin_item.jsp?uid=" + itemToChange.getUid() );
			}else {
				resp.sendRedirect("admin_item.jsp");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			resp.sendRedirect("index.jsp");
		}
	}
	
}
