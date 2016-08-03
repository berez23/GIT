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


lat = request.getParameter("latitudine");
lon = request.getParameter("longitudine");

if (lat==null) {
	// PROVENGO DA VIEWER ABACO

	
	EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
	EnvSource es = new EnvSource(eu.getEnte());
	EscLogic logic = new EscLogic(eu);
	logic.setDatasource(es.getDataSource());

	
	GenericTuples.T2<String,String> coord = logic.getLatitudeLongitudeFromXY(x, y);
	
	lat=coord.firstObj;
	lon=coord.secondObj;
	
}

%>


<html>
<head>
<title>Mappa Prospettica -  VirtualEarth</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">

<script type="text/javascript" src="http://dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=6.1&mkt=it-IT"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript">
</script>
<script type="text/javascript">
function GetMap(lat,lon)
{
         var map = null;
//lat = 45.4643; 
//lon = 9.1918;

         var LA = new VELatLong(lat,lon);
	
         var pinPoint = null;
         var pinPixel = null;
                  
            map = new VEMap('myMap');
            map.LoadMap(LA, 14, VEMapStyle.BirdseyeHybrid, false, VEMapMode.Mode2D, true, 1);
			var mode = map.GetMapStyle();
            if (mode == VEMapStyle.Road)
            {
               map.SetMapStyle(VEMapStyle.Hybrid);
			   map.SetCenterAndZoom(LA, 17);
            }

}
</script>

</head>

<body onload="GetMap(<%=lat%>,<%=lon%>);">


      <div id='myMap'></div>

</body>
</html>
