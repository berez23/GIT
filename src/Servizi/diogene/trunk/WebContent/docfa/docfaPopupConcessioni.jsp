<%@ page language="java"
	import="it.escsolution.escwebgis.docfa.bean.*,it.escsolution.escwebgis.docfa.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%
java.util.ArrayList listaDocfaDatiConc = (java.util.ArrayList) request.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_CONC);
%>

<html>
<head>
<title>Docfa Concessioni - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= request.getContextPath() %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script type="text/javascript">
function dettaglioConcessioni(codice)
{
		var pop = window.open('<%= request.getContextPath() %>/ConcessioniINFORM?ST=3&UC=46&OGGETTO_SEL='+codice+'&EXT=1&NONAV=1');
		pop.focus();
}
</script>
</head>

<body>







		<span class="TXTmainLabel">Dati Concessioni sulla particella</span>
		<table align=center class="extWinTable" style="width: 100%">

			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Protocollo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Potocollo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fisc/P.IVA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome/Rag.Soc.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Soggetto</span></td>
			</tr>
			<%
			java.util.Iterator it  = listaDocfaDatiConc.iterator();
				while (it.hasNext())
				{
					ConcessioneDocfa dc = (ConcessioneDocfa) it.next();
			%>
				<tr onclick="dettaglioConcessioni('<%=dc.getIdConc()%>')">
			    	<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getFoglio()%></span></td>
					<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getParticella()%></span></td>
					<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getSubalterno()%></span></td>
					<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getProtocollo()%></span></td>	
					<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getAnnoProtocollo()%></span></td>		
					<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getDataPotocollo()%></span></td>
					<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getCodFiscPI()%></span></td>
					<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getRagSoc()%></span></td>
					<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getNome()%></span></td>
					<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%=dc.getTipoSoggetto()%></span></td>
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
