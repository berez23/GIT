<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%   HttpSession sessione = request.getSession(true);  %> 
<%   
	java.util.Vector vlistaCatastoPerSoggetto = (java.util.Vector)sessione.getAttribute("CATASTO_PER_SOGGETTO");
%>

<html>
	<head>
		<title>Catasto Immobili UNIMM - Dettaglio per Soggetto</title>
		<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	
	<body>	
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			Subalterni Catasto Urbano</span>
		</div>
		&nbsp;
		
		<% if(vlistaCatastoPerSoggetto != null){ %>
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA INIZIO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FINE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">QUOTA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CATEGORIA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">RENDITA</span></td>
			</tr>
				<%it.escsolution.escwebgis.catasto.bean.CatastoPerSoggetto cps = new it.escsolution.escwebgis.catasto.bean.CatastoPerSoggetto(); %>
		  		<% if (vlistaCatastoPerSoggetto.size() == 0){%>
		     	 <tr>
				        <td colspan='9' class="extWinTDData">
						<span class="extWinTXTData">Non è presente alcun record associato</span></td>
				<%}%>		
		  		<%java.util.Enumeration en = vlistaCatastoPerSoggetto.elements(); %>
				<%int contatore=1;%>
		  		<%while (en.hasMoreElements()) {%>
				  		<% cps = (it.escsolution.escwebgis.catasto.bean.CatastoPerSoggetto)en.nextElement();%>
						<tr id="r<%=contatore%>">
					        <td class="extWinTDData">
							<span class="extWinTXTData"><%=cps.getFoglio()%></span></td>	
							<td class="extWinTDData">
							<span class="extWinTXTData"><%=cps.getParticella()%></span></td>
							<td class="extWinTDData">
							<!-- per il sublaterno si usa il campo UNIMM -->
							<span class="extWinTXTData"><%=cps.getUnimm()%></span></td>
							<%
							java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
							df.setLenient(false);
							java.sql.Date d_i = new java.sql.Date(df.parse(cps.getDataInizio()).getTime());
							java.sql.Date d_f = new java.sql.Date(df.parse(cps.getDataFine()).getTime());					
							if ( d_i.after(d_f) ){%>
								<td class="extWinTDData" style='color:red;'>
								<span class="extWinTXTData"><%=cps.getDataInizio().equals("01/01/0001") ? "-" : cps.getDataInizio()%></span></td>				
								<td class="extWinTDData" style='color:red;'>
								<span class="extWinTXTData"><%=cps.getDataFine().equals("31/12/9999") ? "-" : cps.getDataFine()%></span></td>
							<%}else{ %>
								<td class="extWinTDData">
								<span class="extWinTXTData"><%=cps.getDataInizio().equals("01/01/0001") ? "-" : cps.getDataInizio()%></span></td>				
								<td class="extWinTDData">
								<span class="extWinTXTData"><%=cps.getDataFine().equals("31/12/9999") ? "-" : cps.getDataFine()%></span></td>				
							<%}%>
							<td class="extWinTDData">
							<span class="extWinTXTData"><%=cps.getQuota()%></span></td>				
							<td class="extWinTDData">
							<span class="extWinTXTData"><%=cps.getCategoria()%></span></td>
							<td class="extWinTDData">
							<span class="extWinTXTData"><%=cps.getClasse()%></span></td>
							<td class="extWinTDData">
							<span class="extWinTXTData"><%=cps.getRendita()%></span></td>
						</tr>
				<%contatore++;}%>
			</table>
		<%}%>
		
		&nbsp;
		<div align="center"><span class="extWinTXTTitle">
			<a class="iFrameLink" href="javascript:window.close();">chiudi questa finestra</a></span>
		</div>
	</body>
	
</html>