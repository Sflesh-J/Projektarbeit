<%@page import="de.uniwue.apps.jm1.User"%>
<%@page import="de.uniwue.apps.jm1.Item"%>
<%@page import="de.uniwue.apps.jm1.Main"%>
<%@page import="de.uniwue.apps.jm1.Buchung"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%
User user = (User) session.getAttribute("user");
if (user == null || !user.isAdmin()) {
    response.sendRedirect("index.jsp");
    return;
}

user = User.getUserbyUID("" + user.getUid());
session.setAttribute("user", user);

// User to display parameter handling
String uid = null;
try {
    uid = "" + Integer.parseInt(request.getParameter("uid"));
} catch(Exception e) {
    // uid bleibt null
}
User userAnzeigen = User.getUserbyUID(uid);


String buchungsverlauf = null;
try {
	buchungsverlauf = "" + Integer.parseInt(request.getParameter("buchungsverlauf"));
} catch(Exception e) {
	// uid bleibt null
}

// Pagination für Buchungsverlauf - 50 pro Seite
int buchungsPage = 1;
int buchungsPerPage = 50;
try {
    String pageParam = request.getParameter("buchungsPage");
    if (pageParam != null) {
        buchungsPage = Integer.parseInt(pageParam);
        if (buchungsPage < 1) buchungsPage = 1;
    }
} catch (NumberFormatException e) {
    buchungsPage = 1;
}

// Pagination für Benutzer-Verlauf - 10 pro Seite  
int userPage = 1;
int userBuchungsPerPage = 10;
try {
    String pageParam = request.getParameter("userPage");
    if (pageParam != null) {
        userPage = Integer.parseInt(pageParam);
        if (userPage < 1) userPage = 1;
    }
} catch (NumberFormatException e) {
    userPage = 1;
}

// Formatierungen
DecimalFormat euroFormat = new DecimalFormat("#,##0.00 €");
SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
String adminGuthaben = euroFormat.format(user.getGuthaben() / 100.0);
String userGuthaben = userAnzeigen != null ? euroFormat.format(userAnzeigen.getGuthaben() / 100.0) : null;

List<User> myUsers = User.getUser();
%>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Benutzerverwaltung - WueKaStL</title>
    <link rel="stylesheet" href="css/admin.css">
    <script src="js/jquery-3.7.1.min.js"></script>
</head>
<body>
    <!-- Header -->
      <div class="header">
        <div class="user-info-header">
            <div class="user-name-header"><%= user.getVorname() %> <%= user.getNachname() %></div>
            <div class="saldo"><%= adminGuthaben %></div>
        </div>
        
 <a class="logo" href="index.jsp" >WueKaStL</a>
        
        <a href="index.jsp" class="btn  btn-danger" >zur Startseite</a>
    </div>

    <!-- Hauptbereich -->
    <div class="main">
        <h2 class="main-title">Benutzer und Buchungen</h2>
        <% if(userAnzeigen == null && buchungsverlauf == null) { %>
        <!-- Navigation Buttons -->
        <div class="navigation">
            <button id="neuer-Benutzer" class="btn btn-auswahl">
                Neuer Benutzer
            </button>
			<button id="Gruppenbuchung" class="btn btn-auswahl">
				Gruppenbuchung
			</button>
			<a href="admin_user.jsp?buchungsverlauf=1" id="Verlauf" class="btn btn-auswahl">
				Buchungsverlauf
			</a>
        </div>
		<div class="user-navigation">
		<div id="user-list-section" class="list-container">
        <div class="list-header">
            <h3>Benutzer auswählen</h3>
            <div class="search">
                <input type="text" id="user-search-input" placeholder="Name suchen..." class="search-input">
            </div>
        </div>
        
		<!-- User Grid -->
        <div class="grid" id="user-grid">
            <% for(User u : myUsers) {
                String statusClass = u.isAktiv() ? "active" : "inactive";
                String adminBadge = u.getStatus().equals("admin") ? "<span class='admin-badge'>Admin</span>" : "";
                String guthaben = euroFormat.format(u.getGuthaben() / 100.0);
                User.aktualisiereGuthaben(u);
            %>
            <div class="card" data-uid="<%= u.getUid() %>">
            	<div class="name" <%if(u.isAdmin()){%> style="font-weight: 800;" <%}%>><%= u.getVorname() %> <%= u.getNachname() %></div>
            	
                <div class="actions">
                	<div class="info">
               	 		<a class="<%if(u.getGuthaben() >= 0) { %>positive<% } else { %>negative<% } %>"><%= guthaben %></a>
               	 		<div class="status">
                  	    <div class="badge <%= statusClass %>"> </div>
                 	    <div class="role"><%= u.getStatus() %> </div>
                		</div>
                	</div>
              	  	<div class="form-actions">
                    	<a href="admin_user.jsp?uid=<%= u.getUid() %>" class="btn btn-edit edit">
                        Bearbeiten
                    	</a>
						<a  href="#" class="btn btn-edit select" style="display: none;">
						Auswählen
						</a>
                	</div>
                </div>
            </div>
            <% } %>
        </div>
		</div>
		<!-- Gruppenbuchung -->
		<div id="gruppenbuchung-section" class="list-container" style="display: none; max-width: 400px;">
			<div class="list-header">
				<h3>Gruppenbuchung</h3>
				<button class="btn btn-close">✕</button>
				</div>
				
				<form method="POST" action="buchungen" class="admin-form buchungs-form">
				
                            <div class="form-group">
                                <label>Betrag (€)</label>
                                <input name="betrag" type="number" step="0.01" placeholder="0,00" required>
                            </div>
                            
                            <div class="form-group">
                                <label>Kommentar</label>
                                <textarea name="kommentar" rows="3" placeholder="Grund für die Buchung..."></textarea>
                            </div>
                            <div id="inputs">
							</div>
                            <div class="buchung-actions">
                                <button  type="submit" name="method" value="aufbuchen" class="btn btn-success">
                                    Aufbuchen
                                </button>
                                <button  type="submit" name="method" value="abbuchen" class="btn btn-danger">
                                    Abbuchen
                                </button>
                            </div>
                        </form>
		
		</div>
        <div  id="new-user-section" class="list-container " style="display: none; max-width: 400px;  "> 
                    <div class="list-header">
                        <h3>Neuen Benutzer erstellen</h3>
                        <button class="btn btn-close">✕</button>
                    </div>
                    <form id="new-user-form" method="POST" action="benutzer" class="admin-form">
                        <div class="form-group">
                            <label for="new-vorname">Vorname *</label>
                            <input type="text" id="new-vorname" name="vorname" required>
                        </div>
                        <div class="form-group">
                            <label for="new-nachname">Nachname *</label>
                            <input type="text" id="new-nachname" name="nachname" required>
                        </div>
						<div class="form-group">
							<label for="new-startguthaben">Startguthaben</label>
							<input type="text" id="new-startguthaben" name="guthaben" required>	
						</div>
                        <div class="form-group">
                            <label for="new-status">Status</label>
                            <select id="new-status" name="status" required>
                                <option value="Mitarbeiter">Mitarbeiter</option>
                                <option value="Auszubildender">Auszubildender</option>
                                <option value="Praktikant">Praktikant</option>
                            </select>
                        </div>
						<div class="form-group">
					<label for="new-status">Admin</label>
					<select id="new-status" name="admin" required>
					<option value="nein">nein</option>
					<option value="ja">ja</option>
					</select>
					</div>						
                        <div class="form-group">
                            <label for="new-pin">PIN *</label>
                            <input type="password" id="new-pin" name="pin" minlength="4" maxlength="10" placeholder="Mindestens 4 Zeichen" required>
                        </div>
                        <div class="form-actions">
                            <button type="submit" name="method" value="neuer-Benutzer" class="btn btn-edit">
                                Benutzer erstellen
                            </button>
                        </div>
                    </form>
				</div>
		
		</div>
		<% } %>
		<% if(userAnzeigen != null || buchungsverlauf != null) { %>
		<!-- Navigation Buttons -->
			<div class="navigation">
            <a href="admin_user.jsp" class="btn btn-auswahl"><i class="arrow left"></i>  Zurück</a>
        </div>
		
		<% } %>
		<!-- Buchungsverlauf -->
		<% if(buchungsverlauf != null) { %>
		<div class="verlauf-section">
		            <%
                    java.util.List<Buchung> alleBuchungen = Buchung.getAllBuchungen();
                    alleBuchungen.sort((b1, b2) -> b2.getDatum().compareTo(b1.getDatum()));
                    
                    // Pagination für Buchungsverlauf
                    int totalBuchungen = alleBuchungen.size();
                    int totalBuchungsPages = (int) Math.ceil((double) totalBuchungen / buchungsPerPage);
                    %>
                    
                    <!-- Pagination für Buchungsverlauf -->
                    <% if (totalBuchungsPages > 1) { %>
                    <div class="pagination">
                        <div class="pagination-controls">
                            <!-- Seitenzahlen -->
                            <div class="page-numbers">
                                <% 
                                int startPage = Math.max(1, buchungsPage - 1);
                                int endPage = Math.min(totalBuchungsPages, buchungsPage + 1);
                                
                                // Erste Seite falls nicht sichtbar
                                if (startPage > 1) { %>
                                    <a href="admin_user.jsp?buchungsverlauf=1&buchungsPage=1" class="page-number">1</a>
                                    <% if (startPage > 2) { %>
                                        <span class="page-dots">...</span>
                                    <% } %>
                                <% }
                                
                                // Sichtbare Seitenzahlen
                                for (int i = startPage; i <= endPage; i++) { %>
                                    <% if (i == buchungsPage) { %>
                                        <span class="page-number active"><%= i %></span>
                                    <% } else { %>
                                        <a href="admin_user.jsp?buchungsverlauf=1&buchungsPage=<%= i %>" class="page-number"><%= i %></a>
                                    <% } %>
                                <% }
                                
                                // Letzte Seite falls nicht sichtbar
                                if (endPage < totalBuchungsPages) { %>
                                    <% if (endPage < totalBuchungsPages - 1) { %>
                                        <span class="page-dots">...</span>
                                    <% } %>
                                    <a href="admin_user.jsp?buchungsverlauf=1&buchungsPage=<%= totalBuchungsPages %>" class="page-number"><%= totalBuchungsPages %></a>
                                <% } %>
                            </div>

                        </div>
                    </div>
                    <% } %>
		
                    <div class="verlauf-table-container">
                        <table class="verlauf-table">
                            <thead>
                                <tr>
                                    <th>Datum</th>            
									<th>#</th>
                                    <th>Benutzer</th>
                                    <th>Summe</th>
                                    <th>Kommentar</th>
                                    <th>Inhalt</th>
                                    <th></th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                // Berechnung der aktuellen Seite für Tabelle
                                int startIndex = (buchungsPage - 1) * buchungsPerPage;
                                int endIndex = Math.min(startIndex + buchungsPerPage, totalBuchungen);
                                
                                java.util.List<Buchung> buchungen = alleBuchungen.subList(startIndex, endIndex);
                                
                                for(Buchung buchung : buchungen) {
                                    String betrag = euroFormat.format(buchung.getSumme() / 100.0);
                                    String itemlist = Buchung.itemlistToString(buchung);
                                    String datum = dateFormat.format(buchung.getDatum());
									String buchungstyp = buchung.getBt().toString().toLowerCase();
									String vorname = User.getUserbyUID(Integer.toString(buchung.getUserID())).getVorname();
									String nachname = User.getUserbyUID(Integer.toString(buchung.getUserID())).getNachname();
                                    
                                %>
                                    <tr class="<%= buchungstyp %>">
                                        <td><%= datum %></td>
										<td><%= buchung.getUid() %></td>
                                        <td><%= vorname %> <%= nachname %></td>
                                        <td class="betrag"><%= betrag %></td>
                                        <td><%= buchung.getKommentar() != null ? buchung.getKommentar() : "-" %></td>
                                        <td><%= itemlist != null && !itemlist.trim().isEmpty() ? itemlist : "-" %></td>
                                        <% if(buchungstyp.equals("striche")) { %>
                                        <td>
   											<form action="buchungen" method="post">
      										<input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%= buchung.getUserID()%>">
     								   		<input type="hidden" name="buchungID" value="<%= buchung.getUid() %>">
      										<button type="submit" name="method" value="bearbeitenBuchung" class="btn btn-edit ">Bearbeiten</button>
    										</form>
										</td>
										<% } else { %>
										<td>-</td>
										<% } %>
										<td>
  											<form action="buchungen" method="post">
     										<input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%=buchung.getUserID()%>">
     										<input type="hidden" name="buchungID" value="<%= buchung.getUid() %>">
       										<button type="submit" name="method" value="loescheBuchung" class="btn btn-danger">Löschen</button>
   											</form>
										</td>
                                    </tr>
                                <%}%>
                          	</tbody>
                        </table>
                    </div>
                </div>
		<% } %>
        <!-- Anzeige Section -->
        <div class="Anzeige">
            <% if(userAnzeigen != null) { %>
                <section class="benutzer-einstellungen">
                    <!-- User Info -->
                    <div class="user-info-section">
                        <div class="user-header">
                            <h3><%= userAnzeigen.getVorname() %> <%= userAnzeigen.getNachname() %></h3>
                            <div class="user-status <%= userAnzeigen.isAktiv() ? "active" : "inactive" %>">
                                <%= userAnzeigen.isAktiv() ? "Aktiv" : "Inaktiv" %>
                            </div>
                        </div>
                        
                        <div class="user-details">
                            <p><strong>Status:</strong> <%= userAnzeigen.getStatus() %></p>
                            <p><strong>Guthaben:</strong> <span class="balance"><%= userGuthaben %></span></p>
                            <p><strong>Admin:</strong> <%= userAnzeigen.isAdmin() ? "Ja" : "Nein" %></p>
                        </div>

                        <!-- Aktivierung/Deaktivierung -->
                        <% if(!userAnzeigen.isAdmin()) { %>
                            <form method="POST" action="benutzer" class="status-form">
                                <input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%=userAnzeigen.getUid()%>">
                                <% if(userAnzeigen.isAktiv()) { %>
                                    <button type="submit" name="method" value="deaktivieren" class="btn  btn-danger">
                                        Deaktivieren
                                    </button>
                                <% } else { %>
                                    <button type="submit" name="method" value="aktivieren" class="btn btn-success">
                                        Aktivieren
                                    </button>
                                <% } %>
                            </form>
                        <% } %>

                        <!-- Name ändern -->
                        <div class="name-bearbeiten">
                            <h4>Name ändern</h4>
                            <form method="POST" action="benutzer" class="admin-form">
                                <input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%=userAnzeigen.getUid()%>">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Vorname</label>
                                        <input name="vorname" placeholder="<%= userAnzeigen.getVorname() %>" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Nachname</label>
                                        <input name="nachname" placeholder="<%= userAnzeigen.getNachname() %>" required>
                                    </div>
                                </div>
                                <button type="submit" name="method" value="name-change" class="btn btn-edit">Name ändern</button>
                            </form>
                        </div>

                        <!-- Status ändern -->
                        <% if(!userAnzeigen.isAdmin()) { %>
                            <div class="status-bearbeiten">
                                <h4>Status ändern</h4>
                                <form method="POST" action="benutzer" class="admin-form">
                                    <input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%=userAnzeigen.getUid()%>">
                                    <div class="form-group">
                                        <select name="status" required>
                                            <option value="Mitarbeiter" <%= "Mitarbeiter".equals(userAnzeigen.getStatus()) ? "selected" : "" %>>Mitarbeiter</option>
                                            <option value="Azubi" <%= "Azubi".equals(userAnzeigen.getStatus()) ? "selected" : "" %>>Azubi</option>
                                            <option value="Praktikant" <%= "Praktikant".equals(userAnzeigen.getStatus()) ? "selected" : "" %>>Praktikant</option>
                                        </select>
                                        <button type="submit" name="method" value="status-change" class="btn btn-edit">Status ändern</button>
                                    </div>
                                </form>
                            </div>
                        <% } %>

                        <!-- PIN ändern -->
                        <div class="neuer-pin">
                            <h4>PIN ändern</h4>
                            <form method="POST" action="benutzer" class="admin-form">
                                <input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%=userAnzeigen.getUid()%>">
                                <div class="form-group">
                                    <input name="pin" type="password" placeholder="Neue PIN" minlength="4" maxlength="10" class="pin-eingabe"<% if(userAnzeigen.isAdmin()) { %> required <% } %>>
                                    <button type="submit" name="method" value="pin-change" class="btn btn-edit">PIN ändern</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Buchung Section -->
                    <div class="buchung-section">
                        <h4>Guthaben verwalten</h4>

                        <!-- Buchungsformular -->
                        <form method="POST" action="buchungen" class="admin-form buchungs-form">
                            <input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%= userAnzeigen.getUid() %>">
                            
                            <div class="current-balance">
                                <strong>Aktuelles Guthaben: <%= userGuthaben %></strong>
                            </div>
                            
                            <div class="form-group">
                                <label>Betrag (€)</label>
                                <input name="betrag" type="number" step="0.01" placeholder="0,00" required>
                            </div>
                            
                            <div class="form-group">
                                <label>Kommentar</label>
                                <textarea name="kommentar" rows="3" placeholder="Grund für die Buchung..."></textarea>
                            </div>
                            
                            <div class="buchung-actions">
                                <button type="submit" name="method" value="aufbuchen" class="btn btn-success">
                                    Aufbuchen
                                </button>
                                <button type="submit" name="method" value="abbuchen" class="btn btn-danger">
                                    Abbuchen
                                </button>
                            </div>
                        </form>
                    </div>
                </section>

                <!-- Verlauf -->
                <div class="verlauf-section">
                    <%
                    java.util.List<Buchung> alleUserBuchungen = Buchung.getBuLi(userAnzeigen);
                    alleUserBuchungen.sort((b1, b2) -> b2.getDatum().compareTo(b1.getDatum()));
                    
                    // Pagination für Benutzer-Verlauf
                    int totalUserBuchungen = alleUserBuchungen.size();
                    int totalUserPages = (int) Math.ceil((double) totalUserBuchungen / userBuchungsPerPage);
                    %>
                    
                    <!-- Pagination für Benutzer-Verlauf -->
                    <% if (totalUserPages > 1) { %>
                    <div class="pagination">
                        <div class="pagination-controls">

                            <!-- Seitenzahlen -->
                            <div class="page-numbers">
                                <% 
                                int userStartPage = Math.max(1, userPage - 1);
                                int userEndPage = Math.min(totalUserPages, userPage + 1);
                                
                                // Erste Seite falls nicht sichtbar
                                if (userStartPage > 1) { %>
                                    <a href="admin_user.jsp?uid=<%= userAnzeigen.getUid() %>&userPage=1" class="page-number">1</a>
                                    <% if (userStartPage > 2) { %>
                                        <span class="page-dots">...</span>
                                    <% } %>
                                <% }
                                
                                // Sichtbare Seitenzahlen
                                for (int i = userStartPage; i <= userEndPage; i++) { %>
                                    <% if (i == userPage) { %>
                                        <span class="page-number active"><%= i %></span>
                                    <% } else { %>
                                        <a href="admin_user.jsp?uid=<%= userAnzeigen.getUid() %>&userPage=<%= i %>" class="page-number"><%= i %></a>
                                    <% } %>
                                <% }
                                
                                // Letzte Seite falls nicht sichtbar
                                if (userEndPage < totalUserPages) { %>
                                    <% if (userEndPage < totalUserPages - 1) { %>
                                        <span class="page-dots">...</span>
                                    <% } %>
                                    <a href="admin_user.jsp?uid=<%= userAnzeigen.getUid() %>&userPage=<%= totalUserPages %>" class="page-number"><%= totalUserPages %></a>
                                <% } %>
                            </div>
                            

                        </div>
                    </div>
                    <% } %>
                    
                    <div class="verlauf-table-container">
                        <table class="verlauf-table">
                            <caption>Transaktionsverlauf</caption>
                            <thead>
                                <tr>
                                    <th>Datum</th>
									<th>#</th>
                                    <th>Benutzer</th>
                                    <th>Summe</th>
                                    <th>Kommentar</th>
                                    <th>Inhalt</th>
                                    <th></th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                // Berechnung für aktuelle Seite
                                int userStartIndex = (userPage - 1) * userBuchungsPerPage;
                                int userEndIndex = Math.min(userStartIndex + userBuchungsPerPage, totalUserBuchungen);
                                
                                java.util.List<Buchung> userBuchungen = alleUserBuchungen.subList(userStartIndex, userEndIndex);
                                
                                for(Buchung buchung : userBuchungen) {
                                    String betrag = euroFormat.format(buchung.getSumme() / 100.0);
                                    String itemlist = Buchung.itemlistToString(buchung);
                                    String datum = dateFormat.format(buchung.getDatum());
									String buchungstyp = buchung.getBt().toString().toLowerCase();
                                %>
                                    <tr class="<%= buchungstyp %>">
                                        <td><%= datum %></td>
										<td><%= buchung.getUid() %></td>
                                        <td><%= userAnzeigen.getVorname() %> <%= userAnzeigen.getNachname() %></td>
                                        <td class="betrag"><%= betrag %></td>
                                        <td><%= buchung.getKommentar() != null ? buchung.getKommentar() : "-" %></td>
                                        <td><%= itemlist != null && !itemlist.trim().isEmpty() ? itemlist : "-" %></td>
                                         <% if(buchungstyp.equals("striche")) { %>
                                        <td>
										<form action="buchungen" method="post">
      									<input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%=userAnzeigen.getUid()%>">
     									<input type="hidden" name="buchungID" value="<%= buchung.getUid() %>">
       									<button type="submit" name="method" value="bearbeitenBuchung" class="btn btn-edit ">Bearbeiten</button>
   									</form>
									</td>
									<% } else { %>
									<td>-</td>
									<% } %>
									<td>
										<form action="buchungen" method="post">
     									<input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%=userAnzeigen.getUid()%>">
     									<input type="hidden" name="buchungID" value="<%= buchung.getUid() %>">
       									<button type="submit" name="method" value="loescheBuchung" class="btn btn-danger">Löschen</button>
   									</form>
									</td>
                                    </tr>
                                <%}%>
                          	</tbody>
                        </table>
                    </div>
                </div>
            <% } %>
        </div>
    </div>

    <!-- Footer -->
    <div class="footer">
			<div class="navigation">
				<a href="verlauf.jsp" class="btn btn-auswahl">Verlauf</a>
				<a href="profil.jsp" class="btn btn-auswahl">Profil</a>
				<% if(user.isAdmin()) { %>
                        <a href="admin_item.jsp" class="btn btn-auswahl">Artikel</a>
                        <a href="admin_user.jsp" class="btn btn-auswahl">Benutzer und Buchungen</a>
                    <% } %>
			</div>
    </div>

    <!-- User Data for JavaScript -->
    <script type="text/javascript">
        var users = [];
        <% 
        for (User u : myUsers) { 
        %>
            users.push({ 
                name: '<%= u.getNachname() %>', 
                fname: '<%= u.getVorname() %>', 
                uid: '<%= u.getUid() %>',
                active: <%= u.isAktiv() %>,
                admin: <%= u.isAdmin() %>,
                status: '<%= u.getStatus() %>'
            });
        <% } %>
    </script>

    <script src="js/admin.js"></script>
    <script src="js/script.js"></script>
</body>
</html>