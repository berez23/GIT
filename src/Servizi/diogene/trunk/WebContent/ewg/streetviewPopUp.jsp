<%@page import="it.webred.utils.GenericTuples"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EscLogic"%>
<%@ page language="java"
	import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.docfa.bean.*,it.escsolution.escwebgis.docfa.servlet.*,it.escsolution.escwebgis.docfa.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%



String lat =null;
String lon = null;

String x = request.getParameter("x");
String y = request.getParameter("y");

EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
EnvSource es = new EnvSource(eu.getEnte());
EscLogic logic = new EscLogic(eu);
logic.setDatasource(es.getDataSource());


GenericTuples.T2<String,String> coord = logic.getLatitudeLongitudeFromXY(x, y);

lat=coord.firstObj;
lon=coord.secondObj;
	

%>


<html>
<head>
<title>Street View</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">

<script type="text/javascript" src="http://dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=6.1&mkt=it-IT"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript">
</script>

</head>

<body onload="javascript:apriStreetview(<%=lat%>,<%=lon%>);">


      <div id='myMap' style="width:100%; height:100%;"></div>

</body>
</html>
