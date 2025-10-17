<%@page import="de.uniwue.apps.jm1.User"%>
<%@page import="de.uniwue.apps.jm1.Item"%>
<%@page import="de.uniwue.apps.jm1.Main"%>
<%@page import="java.text.DecimalFormat" %>
<%@page import="java.text.SimpleDateFormat"%>
<% 
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("benutzer.jsp");
    }else{
    	user = User.getUserbyUID("" + user.getUid());
    	session.setAttribute("user", user);
    	
    	DecimalFormat euroFormat = new DecimalFormat("#,##0.00 €");
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    	String Guthaben = euroFormat.format(user.getGuthaben() / 100.0);    
    	
    	
    	boolean neuerPin = false;
    	try {
    	    neuerPin = "true".equals(request.getParameter("neuerPin"));
    	} catch(Exception e) {
    	    throw new Exception("Bei der Änderung deines Pin ist ein Fehler aufgetretten");
    	}
%>
<!-- Index.html -->
<html lang="de">
<head>
  <meta charset="UTF-8">
  <title>WueKaStL</title>
  <link rel="stylesheet" href="css/style.css">
  <script src="js/jquery-3.7.1.min.js"></script>
  
</head>
<body>
  <!-- Header -->
  <div class="header">
        <div class="user-info-header">
            <div class="user-name-header"><%= user.getVorname() %> <%= user.getNachname() %></div>
            <div class="saldo"><%= Guthaben %></div>
        </div>
        
        <a class="logo" href="index.jsp" >WueKaStL</a>
        
        <a href="index.jsp" class="btn  btn-danger" ><i class="arrow left"></i>zur Startseite</a>
    </div>

  <!-- Hauptbereich -->
<div class="main">
	<!-- Profil Menü -->
	<div class="navigation">
		<% if(User.userHatPin(user) && !user.isAdmin()) { %>
		<form method="POST" action="benutzer" class="admin-form">
		<input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%=user.getUid()%>">
		<input type="hidden" name="profil" value="true">
		<input name="pin"  type="hidden" >
		<button id="Pin-deaktivieren" class="btn btn-danger">Pin deaktivieren</button>
		</form>
		<% } %>
	</div>
	
	<!-- Profil Anzeige   -->
	<div class="Anzeige">
		<div class="user-info-section">
			<div class="user-header">
                            <h3><%= user.getVorname() %> <%= user.getNachname() %></h3>
                            <div class="user-status <%= user.isAktiv() ? "active" : "inactive" %>">
                                <%= user.isAktiv() ? "Aktiv" : "Inaktiv" %>
                            </div>
                        </div>
                        
                        <div class="user-details">
                            <p><strong>Status:</strong> <%= user.getStatus() %></p>
                            <p><strong>Guthaben:</strong> <span class="balance"><%= Guthaben %></span></p>
                            <p><strong>Admin:</strong> <%= user.isAdmin() ? "Ja" : "Nein" %></p>
                        </div>
                        <%if(!user.isAdmin()) {%>
						<div class="neuer-pin">
                            <h4>PIN </h4>
                            <form method="POST" id="pinÄnderung" action="benutzer" class="admin-form">
                            	<p>Bitte nur Ziffern eingeben</p>
                                <input type="hidden" name="<%=Main.USER_UID_PARAM%>" value="<%=user.getUid()%>">
                                <input type="hidden" name="profil" value="true">
                                <div class="form-group">
                                    <input name="pin"  pattern="[0-9]+" id="pin" type="password" placeholder="Neuen PIN" minlength="4" maxlength="10" class="pin-eingabe" required >
                                    <input name="pin_confirm"  pattern="[0-9]+" id="pin_confirm" type="password" placeholder="Neuen PIN bestätigen" minlength="4" maxlength="10" class="pin-eingabe" required >
                                    <% if (User.userHatPin(user)){ %>
                                    <button type="submit" name="method" value="pin-change" class="btn btn-edit">PIN ändern</button>
                                    <% } else{ %>
                                    <button type="submit" name="method" value="pin-change" class="btn btn-edit">PIN setzen</button>
                                	<% } %>
                                </div>
                            </form>
                        </div>
                        <% } else{%>
                        <div class="neuer-pin">
                        	<h4>Als Admin PIN bitte in der Benutzerverwaltung am PC ändern</h4>
                        </div>
                        
                        <% } %>
		</div>
	</div>
	<% if(neuerPin){ %>
	<div class="success">
    		<div class="success__title">Hat alles funktioniert ;D</div>
    		<div class="success__close"><svg  width="20" viewBox="0 0 20 20" height="20"><path fill="#393a37" d="m15.8333 5.34166-1.175-1.175-4.6583 4.65834-4.65833-4.65834-1.175 1.175 4.65833 4.65834-4.65833 4.6583 1.175 1.175 4.65833-4.6583 4.6583 4.6583 1.175-1.175-4.6583-4.6583z"></path></svg></div>
			
	</div>
	<% } %>
	
	
 </div>

  <!-- Footer -->
  <div class="footer">
    <div class="navigation">
				<a href="verlauf.jsp" class="btn btn-auswahl">Verlauf</a>
				<a href="profil.jsp" class="btn btn-auswahl active">Profil</a>
				<% if(user.isAdmin()) { %>
                        <a href="admin_item.jsp" class="btn btn-auswahl admin-nav">Artikel</a>
                        <a href="admin_user.jsp" class="btn btn-auswahl admin-nav">Benutzer und Buchungen</a>
                    <% } %>
			</div>
  </div>

<script type="text/javascript" src="js/script.js"></script>

<script>
document.getElementById("pinÄnderung").onsubmit = function () {
  var pw1 = document.getElementById("pin").value;
  var pw2 = document.getElementById("pin_confirm").value;
  if (pw1 !== pw2) {
    alert("Die Passwörter stimmen nicht überein!");
    return false; // das Formular wird NICHT abgesendet
  }
  return true;
};
</script>

</body>
</html>
<% } %>
