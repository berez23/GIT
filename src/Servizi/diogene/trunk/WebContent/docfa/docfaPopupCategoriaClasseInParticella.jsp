<%@ page language="java"
	import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.docfa.bean.*,it.escsolution.escwebgis.docfa.servlet.*,it.escsolution.escwebgis.docfa.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<html>
<head>
<title>Docfa - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<body>

		<div class="TXTmainLabel">Dati Categoria/Classe relativi a tutti i subalterni presenti nella particella</div>
		<table align=center class="extWinTable" style="width: 100%" >
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO </span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">SUB</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CATEGORIA</span></td>	
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE</span></td>				
			</tr>
			<%
			java.util.ArrayList lista = (java.util.ArrayList) request.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_CATEGORIA_CLASSE_PARTICELLA);
			java.util.Iterator it  = lista.iterator();
			while (it.hasNext())
			{
				Docfa dc = (Docfa) it.next();
			%>
				<tr>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getFoglio()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getParticella()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getSubalterno()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getCategoria()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getClasse()%></span></td>
				</tr>
			<%
			}
			%>
		</table>		
<br>
<br>
<br>


</body>
</html>
