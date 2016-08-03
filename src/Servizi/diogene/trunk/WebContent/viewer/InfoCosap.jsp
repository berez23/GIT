<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%@page import="javax.naming.Context"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.servlet.http.HttpSession"%>
<html>
<head>
<title>Informazioni Tassa Cosap</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>

<script type="text/javascript">
function openView(percorso){
	nuova=window.open(percorso,"windowdati","width=550, height=500, statusbar=no,scrollbars=no");
	nuova.focus();
}

</script>

<%
String layerName = ((String)request.getParameter("LayerName")).toUpperCase();
String pk= request.getParameter("OGGETTO_SEL");


%>

</head>

<body>


		
		<span class="extWinTXTTitle">

		</span>
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="extWinTDData" ><span class="extWinTXTData">Informazioni Tassa Cosap</span>
							
							<div >
								 <br/>
						       <a href="javascript:openView('<%=request.getContextPath()%>/Cosap?DATASOURCE=jdbc/dbIntegrato&ST=3&UC=49&OGGETTO_SEL=<%=pk%>&LAYER=<%=layerName%>')">Cosap</a>
						         <br/>
								
								
							
								
							</div>			
				</td>
			</tr>
		</table>




</body>

</html>
