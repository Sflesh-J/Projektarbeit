<%@page isErrorPage="true" %>
<%@page import="de.uniwue.apps.jm1.User"%>
<%@page import="de.uniwue.apps.jm1.Item"%>
<%@page import="de.uniwue.apps.jm1.Main"%>
<%@page import="java.text.DecimalFormat"%>
<%

%>
<!DOCTYPE html>
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
        </div>
        
         <a class="logo" href="index.jsp" >WueKaStL</a>
        
        <div class="header-icons">
        </div>
    </div>
    <div class="main">
    <div class="fehlermeldung">
    <h1 class="main-title">500</h1>
    
    <p>
    Etwas ist schiefgelaufen!
    </p>
   <p><%= exception.getMessage() %></p>
    <div class="navigation">
    <a href="auth/abmeldung" class="btn btn-danger">Logout</a>
    <a href="index.jsp" class="btn btn-success">Startseite</a>
    </div>
    </div>
    </div>
    
    <div class="footer">
        <div class="footer-content">
        </div>
    </div>

    <script src="js/script.js"></script>
</body>
</html>