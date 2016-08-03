<%@ page language="java" import="java.text.SimpleDateFormat,java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.locazioni.bean.*,it.escsolution.escwebgis.locazioni.servlet.*,it.escsolution.escwebgis.locazioni.logic.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %>
<% 	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy"); %>

<%  java.util.ArrayList listaLocazioniSoggetto = (java.util.ArrayList) sessione.getAttribute(LocazioniLogic.LISTA_DETTAGLIO_LOCAZIONI_SOGGETTO); %>

<html>
	<head>
		<title>Locazioni - Elenco locazioni per soggetto</title>
		<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
<body>

	<% 	if (listaLocazioniSoggetto != null ){%>
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			Lista locazioni per soggetto proprietario</span>
		</div>
		&nbsp;
		<table class="extWinTable" style="width: 60%;">	
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO SOGGETTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">SESSO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CITTA NASCITA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVINCIA NASCITA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA NASCITA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CITTA RESIDENZA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVINCIA RESIDENZA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO RESIDENZA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CIVICO RESIDENZA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA SUBENTRO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA CESSIONE</span></td>
			</tr>		  
			<% 
				java.util.Iterator it = listaLocazioniSoggetto.iterator();
				while (it.hasNext()) 
				{
					Locazioni loca = (Locazioni) it.next(); 
					if(loca.getTipoSoggetto()==null)
						loca.setTipoSoggetto("-");
					if(loca.getCodFisc()==null)
						loca.setCodFisc("-");
					if(loca.getSesso()==null)
						loca.setSesso("-");
					if(loca.getCittaNascita()==null)
						loca.setCittaNascita("-");
					if(loca.getProvinciaNascita()==null)
						loca.setProvinciaNascita("-");
					if(loca.getDataNascita()==null)
						loca.setDataNascita("-");
					if(loca.getCittaResidenza()==null)
						loca.setCittaResidenza("-");
					if(loca.getProvinciaResidenza()==null)
						loca.setProvinciaResidenza("-");
					if(loca.getIndirizzoResidenza()==null)
						loca.setIndirizzoResidenza("-");
					if(loca.getCivicoResidenza()==null)
						loca.setCivicoResidenza("-");					
					%>			
			    <tr>
					<td class="extWinTDData">
					<span class="extWinTXTData"><%= loca.getTipoSoggetto() %></span></td>
					<td class="extWinTDData">
					<span class="extWinTXTData"><%= loca.getCodFisc() %></span></td>
					<td class="extWinTDData">
					<span class="extWinTXTData"><%= loca.getSesso() %></span></td>
					<td class="extWinTDData" nowrap>
					<span class="extWinTXTData"><%= loca.getCittaNascita() %></span></td>
					<td class="extWinTDData" align=center>
					<span class="extWinTXTData"><%= loca.getProvinciaNascita() %></span></td>
					<td class="extWinTDData" nowrap>
					<span class="extWinTXTData"><%= loca.getDataNascita() %></span></td>
					<td class="extWinTDData">
					<span class="extWinTXTData"><%= loca.getCittaResidenza() %></span></td>
					<td class="extWinTDData" align=center>
					<span class="extWinTXTData"><%= loca.getProvinciaResidenza() %></span></td>
					<td class="extWinTDData"  nowrap>
					<span class="extWinTXTData"><%= loca.getIndirizzoResidenza() %></span></td>
					<td class="extWinTDData" align=right>
					<span class="extWinTXTData"><%= loca.getCivicoResidenza() %></span></td>
					<td class="extWinTDData" align=right>
					<span class="extWinTXTData"><%= loca.getDataSubentro()==null?"-":loca.getDataSubentro() %></span></td>
					<td class="extWinTDData" align=right>
					<span class="extWinTXTData"><%= loca.getDataCessione()==null?"-":loca.getDataCessione()%></span></td>
				</tr>			
				<%}%>
			</table>
		<%}%>
		&nbsp;
		<div align="center"><span class="extWinTXTTitle">
			<a class="iFrameLink" href="javascript:window.close();">chiudi questa finestra</a></span>
		</div>
		
	</body>
</html>