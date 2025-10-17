<%@page import="de.uniwue.apps.jm1.Item"%>
<%@page import="de.uniwue.apps.jm1.Main"%>
<%@page import="de.uniwue.apps.jm1.User"%>
<%
	String uid = null;
	int versuch = 0;
	if (session.getAttribute("user") != null) {
	    response.sendRedirect("index.jsp");
	}else{
		try {
			uid = request.getParameter("uid");
			versuch = Integer.parseInt(request.getParameter("versuch")); 
		} catch (Exception e) {
	        response.sendRedirect("index.jsp");
	    }
		
		User user = User.getUserbyUID(request.getParameter("uid"));
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>
    <link rel="stylesheet" href="css/login.css">
    <script src="js/jquery-3.7.1.min.js"></script>
</head>
<body>
    <div class="login-container">
        <div class="back-link">
            <a href="benutzer.jsp" class="btn btn-auswahl">← Zurück</a>
        </div>
        
        <div class="login-card">
            <div class="login-header">
                <h1>PIN eingeben</h1>
                <p class="username"><%= user.getVorname() %> <%= user.getNachname() %></p>
            </div>
            
            <%if(versuch > 1){ %>
            <div class="error-msg">
                PIN ist falsch. Bitte erneut versuchen.
                <span class="error-close">×</span>
            </div>
            <%} %>
            
            <form method="POST" action="auth/anmeldung" class="pin-form">
                <input type="hidden" name="uid" value="<%= uid %>">
                <input type="hidden" name="versuch" value="2">
                
                <div class="pin-display">
                    <input type="password" id="pin-input" name="<%= Main.PASSWORT_PARAM %>" readonly>
                </div>
                
                <div class="pin-buttons">
                    <button type="button" class="pin-btn" data-num="1">1</button>
                    <button type="button" class="pin-btn" data-num="2">2</button>
                    <button type="button" class="pin-btn" data-num="3">3</button>
                    <button type="button" class="pin-btn" data-num="4">4</button>
                    <button type="button" class="pin-btn" data-num="5">5</button>
                    <button type="button" class="pin-btn" data-num="6">6</button>
                    <button type="button" class="pin-btn" data-num="7">7</button>
                    <button type="button" class="pin-btn" data-num="8">8</button>
                    <button type="button" class="pin-btn" data-num="9">9</button>
                    <button type="button" class="pin-btn clear-btn">C</button>
                    <button type="button" class="pin-btn" data-num="0">0</button>
                    <button type="submit" class="pin-btn submit-btn">OK</button>
                </div>
            </form>
        </div>
    </div>
    
    <script src="js/login.js"></script>
</body>
</html>
<%	} %>
