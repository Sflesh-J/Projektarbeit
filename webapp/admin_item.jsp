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

// Item to display parameter handling
String uid = null;
Item itemAnzeigen = null;
try {
    uid = "" + Integer.parseInt(request.getParameter("uid"));
    itemAnzeigen = Item.getItem(Integer.parseInt(uid));
} catch(Exception e) {
    // uid bleibt null, itemAnzeigen bleibt null
}

// Formatierungen
DecimalFormat euroFormat = new DecimalFormat("#,##0.00 €");
SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
String adminGuthaben = euroFormat.format(user.getGuthaben() / 100.0);

List<Item> myItems = Item.getItLi();
%>
<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Item-Verwaltung - WueKaStL</title>
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
        <h2 class="main-title">Artikel</h2>
        <% if(itemAnzeigen == null) { %>
        <!-- Navigation Buttons -->
        <div class="navigation">
            <button id="neues-Item" class="btn btn-auswahl">
                Neuer Artikel
            </button>
        </div>
        <div class="user-navigation">
        <div id="item-list-section" class="list-container">
            <div class="list-header">
                <h3>Artikel auswählen</h3>
                <div class="search">
                    <input type="text" id="item-search-input" placeholder="Item suchen..." class="search-input">
                </div>
            </div>
            
            <!-- Item Grid -->
            <div class="grid" id="item-grid">
                <% for(Item i : myItems) {
                    String statusClass = i.isAktiv() ? "active" : "inactive";
					String preis = euroFormat.format(i.getPreis() / 100.0);
                %>
                <div class="card" data-uid="<%= i.getUid() %>">
                    <div class="name"><%= i.getName() %></div>
                <div class="actions">
                    <div class="info">
                    <a style="font-weight: 500"><%= preis %></a>
					<div class="status">
                        <div class="badge <%= statusClass %>"> </div>
                        <div class="id">ID: <%= i.getUid() %></div>
                    </div>
					</div>
                    <div class="form-actions">
                        <a href="admin_item.jsp?uid=<%= i.getUid() %>" class="btn btn-edit">
                            Bearbeiten
                        </a>
                    </div>
                </div>
				</div>
                <% } %>
            </div>
        </div>
        <!-- Neues Item -->
        <div id="new-item-section" class="list-container" style="display: none; max-width: 400px;"> 
                    <div class="list-header">
                        <h3>Neuer Artikel erstellen</h3>
                        <button class="btn btn-close">✕</button>
                    </div>
                    <form id="new-item-form" method="POST" action="artikel" class="admin-form">
                        <div class="form-group">
                            <label for="new-item-name">Name *</label>
                            <input type="text" id="new-item-name" name="name" required>
                        </div>
                        <div class="form-group">
                            <label for="new-item-preis">Preis (€) *</label>
                            <input type="number" id="new-item-preis" name="preis" min="0" step="0.01" placeholder="0,00" required>
                        </div>
                        <div class="form-group">
                            <label for="new-item-datum">Gültig ab *</label>
                            <input type="date" id="new-item-datum" name="datumAb" required>
                        </div>
                        <div class="form-actions">
                            <button type="submit" name="method" value="neues-item" class="btn btn-edit">
                                Artikel erstellen
                            </button>
                        </div>
                    </form>
                </div>
        
        </div>
        <% } %>
        <% if(itemAnzeigen != null) { %>
        <!-- Navigation Buttons -->
            <div class="navigation">
            <a href="admin_item.jsp" class="btn btn-auswahl"> <i class="arrow left"></i>  Zurück zur Artikelliste</a>
        </div>
        
        <% } %>
        
        <!-- Anzeige Section -->
        <div class="Anzeige">
            <% if(itemAnzeigen != null) { %>
                <section class="item-einstellungen">
                    <!-- Item Info -->
                    <div class="item-info-section">
                        <div class="item-header">
                            <h3><%= itemAnzeigen.getName() %></h3>
                            <div class="item-status <%= itemAnzeigen.isAktiv() ? "active" : "inactive" %>">
                                <%= itemAnzeigen.isAktiv() ? "Aktiv" : "Inaktiv" %>
                            </div>
                        </div>
                        
                        <div class="item-details">
                            <p><strong>Artikel-ID:</strong> <%= itemAnzeigen.getUid() %></p>
                            <p><strong>Status:</strong> <%= itemAnzeigen.isAktiv() ? "Verfügbar" : "Deaktiviert" %></p>
                        </div>

                        <!-- Aktivierung/Deaktivierung -->
                        <form method="POST" action="artikel" class="status-form">
                            <input type="hidden" name="<%=Main.ITEM_UID_PARAM%>" value="<%= itemAnzeigen.getUid() %>">
                            <% if(itemAnzeigen.isAktiv()) { %>
                                <button type="submit" name="method" value="deaktivieren" class="btn btn-danger">
                                    Deaktivieren
                                </button>
                            <% } else { %>
                                <button type="submit" name="method" value="aktivieren" class="btn btn-success">
                                    Aktivieren
                                </button>
                            <% } %>
                        </form>

                        <!-- Name ändern -->
                        <div class="name-bearbeiten">
                            <h4>Name ändern</h4>
                            <form method="POST" action="artikel" class="admin-form">
                                <input type="hidden" name="<%=Main.ITEM_UID_PARAM%>" value="<%= itemAnzeigen.getUid() %>">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Name</label>
                                        <input name="name" placeholder="<%= itemAnzeigen.getName() %>" required>
                                    </div>
                                </div>
                                <button type="submit" name="method" value="name-change" class="btn btn-edit">Name ändern</button>
                            </form>
                        </div>
                    </div>

                    <!-- Preis Management -->
                    <div class="preis-section">
                        <h4>Preis verwalten</h4>

                        <!-- Preisformular -->
                        <form method="POST" action="artikel" class="admin-form preis-form">
                            <input type="hidden" name="<%=Main.ITEM_UID_PARAM%>" value="<%= itemAnzeigen.getUid() %>">
                            
                            <div class="form-group">
                                <label for="itemPreis">Preis (€)</label>
                                <input type="number" id="itemPreis" name="preis" min="0" step="0.01" placeholder="0,00" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="itemDatum">Gültig ab</label>
                                <input type="date" id="itemDatum" name="datumAb" required>
                            </div>
                            
                            <button type="submit" name="method" value="change-preis" class="btn btn-edit">
                               Preis ändern
                            </button>
                        </form>
                    </div>
                </section>
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
    
    

    <!-- Item Data for JavaScript -->
    <script type="text/javascript">
        var items = [];
        <% 
        for (Item i : myItems) { 
        %>
            items.push({ 
                name: '<%= i.getName() %>', 
                uid: '<%= i.getUid() %>',
                active: <%= i.isAktiv() %>
            });
        <% } %>
        
        <% if(itemAnzeigen != null) { %>
        var currentItem = {
            name: "<%= itemAnzeigen.getName() %>",
            aktiv: <%= itemAnzeigen.isAktiv() %>,
            uid: "<%= itemAnzeigen.getUid() %>"
        };
        <% } %>
    </script>

    <script src="js/admin.js"></script>
    <script src="js/script.js"></script>
</body>
</html>