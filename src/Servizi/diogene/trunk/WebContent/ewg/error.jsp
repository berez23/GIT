<%@ page language="java" import="it.escsolution.escwebgis.common.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% 
	HttpSession sessione = request.getSession(true);
	String errorMessage = (String) sessione.getAttribute(EscServlet.ERROR_MESSAGE);
	if (errorMessage == null)
		errorMessage = "";
%> 
<html>
	<head>
	<title>Error Page</title>
	<link rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
	</head>
	<body>
		<div style="width: 80%; margin-left: 10%; margin-right: 10%; margin-top: 10%;">
			<h3 style="color: red;"><%= errorMessage %></h3>
			<p><input type='submit' value="Indietro" class="tdButton" onClick="window.history.back();"></p>
		</div>
	</body>
</html>
