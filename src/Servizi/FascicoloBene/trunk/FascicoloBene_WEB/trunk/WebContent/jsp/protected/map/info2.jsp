<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="sun.misc.BASE64Encoder"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="it.webred.fb.web.bean.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
table
{
	font-family: sans-serif;
	border-collapse:collapse;
	background-color:white;
	font-size: 8pt;
	padding:8px;
	font-style:normal;
	font-weight:normal;
}

table, th, td
{
	border: 1px solid #C0C0C0;
	padding:5px;
}
table tr td, table Thead th
{
	color:white;
	text-align:left;
	font-size:8pt;
	vertical-align:bottom;
	background-color: #4A75B5;
	padding-bottom:1px;
	padding-right:2px;
	font-style:normal;
	font-weight:bold;
	border: 1px solid #C0C0C0;
}
</style>

<title>Fascicolo Bene - Info</title>
</head>

<body style="background-color:#F3F2F2;">

	<%
		Logger logger = Logger.getLogger("fascicolobene.log");

		String parameters = request.getQueryString();
		logger.debug("info.jsp - Parameters:"+parameters);
		if (parameters.contains("?")) {
			parameters = parameters.replaceAll("\\?", "\\&");
			String urlOk = "info.jsp?" + parameters;
			logger.debug("info.jsp - UrlOk:"+urlOk);
			response.sendRedirect(urlOk);
		}
		 appName = (String) env.lookup("mapMan");
		MapMan man = new MapMan(); %>
		
	<%=man.scriviInfoTable(request.getParameter("TableName"),
					request.getParameter("IdentField"),
					request.getParameter("ElemId"),
					request.getParameter("LayerName"),
					request.getParameter("ds"),
					"http://"+request.getServerName()+":"+ request.getServerPort() +  request.getContextPath())%>
						
</body>
</html>