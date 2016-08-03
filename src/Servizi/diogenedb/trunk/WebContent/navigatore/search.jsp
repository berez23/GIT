<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/diogenedb-viewer.tld" prefix="dv"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Ricerca</title>
		<meta http-equiv="Content-Style-Type" content="text/css" />

		<link href="../css/style.css" type="text/css" rel="stylesheet" />	
		<link href="../css/menuH.css" type="text/css" rel="stylesheet" />	
		<link href="../css/styleNavigatore.css" type="text/css" rel="stylesheet" />	
		<!--[if IE]> <style type="text/css">IMG,INPUT,A {behavior:url("iepngfix.htc")};</style> <![endif]-->
		<script src="js.jsp" language="JavaScript"></script>
		<script src="staticValidator.jsp" language="JavaScript"></script>

		
	</head>
	<body>
		<div id="contentwrap">
			<jsp:include page="menu.jsp" />
			<dv:filterFieldsRender />
		</div>
	</body>
</html>
