<%@ page language="java"
	import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.docfa.bean.*,it.escsolution.escwebgis.docfa.servlet.*,it.escsolution.escwebgis.docfa.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%
java.util.ArrayList listaDocfaDatiPrg = (java.util.ArrayList) request.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_PRG);
%>

<html>
<head>
<title>Docfa PRG - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<body>







		<span class="TXTmainLabel">Dati PRG</span>
		<table align=center class="extWinTable" style="width: 100%">

			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Allegato</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Dest. Funz.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Legenda</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Area Part.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Area PRG</span></td>
			<!-- 	<td class="extWinTDTitle"><span class="extWinTXTTitle">Area Int.</span></td>  -->
			</tr>
			<%
			java.util.Iterator it  = listaDocfaDatiPrg.iterator();
				while (it.hasNext())
				{
					PrgDocfa dc = (PrgDocfa) it.next();
			%>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getFoglio()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getParticella()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getAllegato()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getDestFunz()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getLegenda()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getAreaPart()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getAreaPRG()%></span></td>
				<!--<td class="extWinTDData"><span class="extWinTXTData"><%//=dc.getAreaInt()%></span></td>  -->
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
