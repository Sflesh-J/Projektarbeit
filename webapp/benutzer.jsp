<!-- Index.html -->
<%@page import="de.uniwue.apps.jm1.User"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="de.uniwue.apps.jm1.Main"%>
 <%
    if (session.getAttribute("user") != null) {
        response.sendRedirect("index.jsp");
    }else{ %>
<html>

<head>
    <meta charset="UTF-8">

    <link rel="stylesheet"
        href="css/login.css"/>
    <script src="js/jquery-3.7.1.min.js"></script>
    <title>Mitarbeiter Anmeldung - WueKaStL</title>
</head>

<body>
	<div class="container">
		<div class="header-login">
            <h1> Mitarbeiter Anmeldung</h1>
            <p>Wählen Sie Ihren Namen aus, um sich anzumelden</p>
        </div>
		<div class="search-bar">
            <input type="text" class="search-input" placeholder="Suchen..." id="searchInput">
        </div>
        
        <div class="loading" id="loading">
            <div class="spinner"></div>
            <p>Lade Mitarbeiter...</p>
        </div>
		
		<div class="user-grid" id="anzeige" style="display: none;">
		</div>
		
		<div class="no-results" id="noResults" style="display: none;">
            <p > Keine Mitarbeiter gefunden</p>
        </div>
		</div>
		
		
        <script type="text/javascript">
        var users = [];
        <%	
        List<User> Users = User.getUser();
        for (User u : Users) { if(u.isAktiv()){ %>
        users.push({ name: '<%= u.getNachname() %>', fname: '<%= u.getVorname() %>', uid: '<%= u.getUid() %>' });
        <%} }%>
        let filteredUsers = [...users];
        </script>
        <script type="text/javascript" src="js/login.js"></script>
</body>

</html>
<%	} %>