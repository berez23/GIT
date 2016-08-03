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
<%@page import="it.escsolution.escwebgis.crif.logic.*"%>
<%@page import="it.escsolution.escwebgis.anagrafe.bean.*"%>
<%@page import="it.escsolution.escwebgis.redditiAnnuali.bean.*"%>
<%@page import="it.escsolution.escwebgis.toponomastica.bean.*"%>
<%@page import="it.escsolution.escwebgis.crif.bean.RedditoTotFamiglia"%>
<%@page import="java.text.DecimalFormat"%>

<html>
<head>
<title>Informazioni Indicatori CRIF - Reddito medio per famiglia</title>
<LINK rel="stylesheet"
	href="<%= request.getContextPath() %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script>
function vai(codice){
	document.infoForm.KEYSTR.value=codice; // QUESTO PER SALTARE IN LISTA CON ST = 2
	document.infoForm.OGGETTO_SEL.value=codice; // QUESTO PER DETTAGLIO CON ST=3
	document.infoForm.ST.value = 3;
	document.infoForm.submit();
	}
</script>

</head>

<body>

 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Redditi delle Famiglie Residenti nella Sezione </span>
</div>

   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
   
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo Residenza</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Famiglia</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Reddito Famiglia</span></td>
	</tr>
<%
   		String pk = request.getParameter("OGGETTO_SEL");
		
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		CrifLogic logic = new CrifLogic(eu);
		List<RedditoTotFamiglia> lista = logic.getRedditiFamigliaCivicoTopo(pk, request);

		for (RedditoTotFamiglia info : lista) {
			
			Civico civ = info.getCivico();
			String codice = info.getCodiceFamiglia();
				
			String indirizzo = civ.getSedime() + " " + civ.getStrada()+ ", " + civ.getDescrCivico();
			
			String styleTD = "extWinTDData";
			
			%>
			<tr>
			<td class=<%=styleTD%> style='cursor: pointer;'><span class="extWinTXTData"><%= indirizzo  %></span></td>
			<td class=<%=styleTD%> style='cursor: pointer;'><span class="extWinTXTData"><%= info.getCodiceFamiglia()%></span></td>
			<td class=<%=styleTD%> style='cursor: pointer;'><span class="extWinTXTData"><%= info.getAnno() %></span></td>
			<%if(info.getImporto()!=null) {%>
			<td class=<%=styleTD%> style='cursor: pointer;'><span class="extWinTXTData">&#8364; <%= new DecimalFormat("#,##0.00").format(info.getImporto()) %></span></td>
			<%}else{ %>
			<td class=<%=styleTD%> style='cursor: pointer;'><span class="extWinTXTData">Reddito non dichiarato</span></td>
			<%} %>
			</tr>
			<%			
		}
					
		
	
%>
	</table>

			<form name="infoForm" method="post" action="<%=request.getContextPath()%>/RedditiAnnuali" id="infoForm" >
				<input type="hidden" name="DATASOURCE" value="<%=es.getDataSource()%>">
				<input type="hidden" name="ST" value="2">
				<input type="hidden" name="UC" value="52">
				<input type="hidden" name="KEYSTR" value="">
				<input type="hidden" name="OGGETTO_SEL" value="">
			</form>							
										

<div id="wait" class="cursorWait" />
		
</body>

</html>
