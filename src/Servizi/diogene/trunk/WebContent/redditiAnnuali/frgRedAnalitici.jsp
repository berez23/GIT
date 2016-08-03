<%@ page language="java"%>
<%@page import="it.escsolution.escwebgis.redditiAnnuali.logic.*"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="java.util.*"%>
<%@page import="it.webred.ct.data.access.basic.redditianalitici.dto.*" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%HttpSession sessione = request.getSession(true); %>
<%List<RigaQuadroRbDTO> lstRB = (List<RigaQuadroRbDTO>)sessione.getAttribute(RedditiAnnualiLogic.QUADRO_RB); %>
<%EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
  if (GestionePermessi.autorizzato(eu.getUtente() , eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, "Fonte:REDDITI ANALITICI", true)) {%>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 3px; width: 100%; display: table;">
					<div style="display: table-row;">
							<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">DETTAGLIO - QUADRO RB DICHIARANTE</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<%if(lstRB != null && lstRB.size()>0 ){%>
					<table class="extWinTable" style="width: 100%; border: none;">
					<%  String annoAquila = "2010";
						for (int contatore=0; contatore<lstRB.size(); contatore ++){
						RigaQuadroRbDTO rb = lstRB.get(contatore);
						if (contatore == 0) {%>
							<tr id="testata">
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Modulo</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Rigo</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Continuazione</span></td>
								<td class="extWinTDTitle" title="Rendita catastale rivalutata del 5%">
									<span class="extWinTXTTitle">Rendita</span></td>
								<td class="extWinTDTitle">
									<span class="extWinTXTTitle">Utilizzo</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Giorni Poss.</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">% Poss.</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Canone Locazione</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Casi Particolari</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Imponibile</span></td>
								<td class="extWinTDTitle" title="Codice catastale del comune ove è situata l'unità immobiliare">
									<span class="extWinTXTTitle">Comune</span></td>	
								<td class="extWinTDTitle"><span class="extWinTXTTitle">ICI dovuta</span></td>
								<%if(annoAquila.equals(rb.getAnnoImposta())){ %>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Cedolare AQ</span></td>
								<%} %>
							</tr>
						<%}%>
						  <tr id="r<%=contatore%>">
						  	<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="TXTmainLabel"><%=rb.getNumModulo()==null ? "-" : rb.getNumModulo()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="TXTmainLabel"><%=rb.getNumFabb()==null ? "-" : rb.getNumFabb()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer; text-align:center;'>
								<%if(rb.isContinuazione()){ %>
									<center><img src="../images/ok.gif"></img></center>
								<%}%>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="TXTmainLabel"><%=rb.getRendita()==null ? "" : rb.getRendita()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer; text-align:center;'>
								<span class="TXTmainLabel"><%=rb.getRendita()==null ? "-" : rb.getUtilizzo()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer; text-align:center;'>
								<span class="TXTmainLabel"><%=rb.getGiorni()==null ? "-" : rb.getGiorni()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="TXTmainLabel"><%=rb.getPossesso()==null ? "-" : rb.getPossesso()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="TXTmainLabel"><%=rb.getCanoneLoc()==null ? "" : rb.getCanoneLoc()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;text-align:center;'>
								<span class="TXTmainLabel"><%=rb.getCasiPart()==null ? "-" : rb.getCasiPart()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="TXTmainLabel"><%=rb.getImponibile()==null ? "" : rb.getImponibile()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;text-align:center;'
								title="<%=rb.getDescComune()==null? "Codice Comune non decodificato" : rb.getDescComune()%>">
								<span class="TXTmainLabel"><%=rb.getComune()==null ? "" : rb.getComune()%></span></td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="TXTmainLabel"><%=rb.getIci()==null ? "" : rb.getIci()%></span></td>
							<%if(annoAquila.equals(rb.getAnnoImposta())){ %>
								<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="TXTmainLabel"><%=rb.getCedolareAq()==null ? "" : rb.getCedolareAq()%></span></td>
							<%} %>
						</tr>
					<%}%>
					</table>
				<%}else{ %>
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">Dati non presenti</div>
					</div>
				<%}%>			
			</div>
		</div>		
	<%}%>