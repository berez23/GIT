<%@ page language="java"
	import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.docfa.bean.*,it.escsolution.escwebgis.docfa.servlet.*,it.escsolution.escwebgis.docfa.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%
java.util.ArrayList listaDocfaDatiGraffati = (java.util.ArrayList) request.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_GRAFFATI);
%>






<html>
<head>
<title>Docfa - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<body>







		<span class="TXTmainLabel">Dati Graffati</span>
		<table align=center class="extWinTable" style="width: 100%">

			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>
			</tr>
			<%
			java.util.Iterator it  = listaDocfaDatiGraffati.iterator();
				while (it.hasNext())
				{
					Docfa dc = (Docfa) it.next();
			%>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getFoglio()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getParticella()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getSubalterno()%></span></td>
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
