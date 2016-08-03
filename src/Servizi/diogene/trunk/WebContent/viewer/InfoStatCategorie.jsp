<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@page import="java.util.*"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="it.escsolution.escwebgis.catasto.logic.*"%>
<%@page import="it.escsolution.escwebgis.catasto.bean.*"%>
<%@page import="java.text.*"%>


<html>
<head>
<title>Lista Categorie sulla Particella</title>
<LINK rel="stylesheet"
	href="<%= request.getContextPath() %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<body>

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Categorie Particella </span>
</div>
<%
	String pk = request.getParameter("OGGETTO_SEL");
	
	EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
	EnvSource es = new EnvSource(eu.getEnte());
	CatastoImmobiliLogic logic = new CatastoImmobiliLogic(eu);
	Vector<StatisticaCategorie> lista = logic.caricaListaCategorieParticella(pk, request);
	int contatore = 1; 
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	if(lista.size()>0) {%>
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
   
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Num.Occorrenze</span></td>
	</tr>
<%
   
    for (StatisticaCategorie bInfo : lista) {
%>
		
		     <tr id="r<%=contatore%>">
				<td class="extWinTDData"><span class="extWinTXTData"><%=bInfo.getFoglio() %></span></td>
				<td  class="extWinTDData"><span class="extWinTXTData"><%=bInfo.getParticella() %></span></td>
				<td  class="extWinTDData"><span class="extWinTXTData"><%=bInfo.getCategoria() %></span></td>
				<td  class="extWinTDData"><span class="extWinTXTData"><%=bInfo.getOccorrenze() %></span></td>
			</tr>
	<% contatore++; }%>
	
	</table>
	
	<% } else{ %>
		<div align="center" >
		<span >Nessuna categoria per la particella</span>
	</div>
	
	<%} %>
	
					

<div id="wait" class="cursorWait" />
		
</body>

</html>
