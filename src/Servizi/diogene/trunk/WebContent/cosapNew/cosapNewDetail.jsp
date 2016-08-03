<%@ page language="java" import="it.escsolution.escwebgis.tributi.bean.*,java.util.ArrayList,java.util.Iterator"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  CosapContrib con = (CosapContrib)sessione.getAttribute(CosapNewLogic.COSAP); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.cosapNew.bean.CosapNewFinder finder = null;

if (sessione.getAttribute(CosapNewLogic.FINDER) != null){ 
	finder = (it.escsolution.escwebgis.cosapNew.bean.CosapNewFinder)sessione.getAttribute(CosapNewLogic.FINDER);
}%>

<%int js_back = 1;
if (sessione.getAttribute("BACK_JS_COUNT")!= null)
	js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();


java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>

<%@page import="it.escsolution.escwebgis.cosapNew.bean.CosapContrib"%>
<%@page import="it.escsolution.escwebgis.cosapNew.logic.CosapNewLogic"%>

<%@page import="it.escsolution.escwebgis.cosapNew.bean.CosapTassa"%>
<html>
	<head>
		<title>Cosap - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	
	<script>
		function mettiST(){
			document.mainform.ST.value = 3;
		}
		
		function visualizzaDettaglioOggetto(idx, visDett) {
			document.getElementById("rOgg" + idx).style.display = (visDett ? "none" : "");
			document.getElementById("rOggDett" + idx).style.display = (visDett ? "" : "none");
		}
	</script>
	
	<body>

		
		
			<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Dati Cosap</span>
			</div>
	
			&nbsp;
	<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>

<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
	&nbsp;
	
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CosapNew" target="_parent">
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Dati Soggetto</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 95%;">
										<tr>
											<td class="TDmainLabel" style="width:20%;">
												<span class="TXTmainLabel">Tipo Persona</span>
											</td>
											
											<td class="TDviewTextBox" style="width:24%;">
												<span class="TXTviewTextBox"><%=con.getDescTipoPersona()%></span>
											</td>
											
											<td style="width:4%"></td>
											
											<%  String denominazione = con.getCogDenom();
										        if (!"G".equalsIgnoreCase(con.getTipoPersona())) {
										        	String nome = con.getNome();
										        	if (denominazione.equals("-")) {
										        		denominazione = nome;
										        	} else {
										        		if (!nome.equals("-")) {
										        			denominazione += " " + nome;
										        		}
										        	}
										        } %>
											
											<td class="TDmainLabel" style="width:20%;">
												<span class="TXTmainLabel"><%="G".equalsIgnoreCase(con.getTipoPersona()) ? "Denominazione" : "Cognome / Nome"%></span>
											</td>
											
											<td class="TDviewTextBox" style="width:24%;">
												<span class="TXTviewTextBox"><%=denominazione%></span>
											</td>					
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Codice Fiscale</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=con.getCodiceFiscale()%></span>
											</td>
											
											<td></td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Partita IVA</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=con.getPartitaIva()%></span>
											</td>					
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Data Nascita</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=con.getDtNascita()%></span>
											</td>
											
											<td></td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Comune Nascita</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=con.getDescComuneNascita()%></span>
											</td>					
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Indirizzo Residenza</span>
											</td>
											
											<td class="TDviewTextBox" colspan="4">
												<span class="TXTviewTextBox"><%=con.getIndirizzoCompleto()%></span>
											</td>			
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">CAP Residenza</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=con.getCapResidenza()%></span>
											</td>
											
											<td></td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Comune Residenza</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=con.getDescComuneResidenza()%></span>
											</td>					
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Data Iscr. archivio</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=con.getDtIscrArchivio()%></span>
											</td>
											
											<td></td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Data Cost. Soggetto</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=con.getDtCostitSoggetto()%></span>
											</td>					
										</tr>										
									</table>
								</td>
							</tr>
						</table>						
					</td>
				</tr>
			</table>
			
			<div class="tabber">
					
					
			
			<% ArrayList<CosapTassa> tasse = con.getTasse();
			if (tasse != null && tasse.size() > 0) {%>
				<div class="tabbertab">
						
						<h2>Oggetti Cosap</h2>
						
							
							<table align=left class="extWinTable" style="width: 100%; margin-top: 5px;" cellpadding="0" cellspacing="0">	
								<tr>&nbsp;</tr>
								<tr class="extWinTXTTitle">Oggetti Cosap</tr>
								<tr>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Quantità</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Data inizio val.</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Data fine val.</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno val. tariffa</span></td>
								</tr>
								<% 	int contatore = 1;
								for (int i = 0; i < tasse.size(); i++) {
									CosapTassa tassa = (CosapTassa)tasse.get(i);%>
								<tr id="rOgg<%=contatore%>" style="cursor: pointer; display:;" title="Clicca per visualizzare il dettaglio dell'oggetto"
								onclick="visualizzaDettaglioOggetto('<%=contatore%>', true);">			
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=tassa.getFoglio()%></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=tassa.getParticella()%></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=tassa.getSubalterno()%></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=tassa.getIndirizzoCompleto()%></span>							
									</td>
									<td class="extWinTDData" style="text-align: right;">
										<% String quantita = tassa.getUnitaMisuraQuantita() == null || tassa.getUnitaMisuraQuantita().equals("-") ? "-" : tassa.getUnitaMisuraQuantita(); 
											if (tassa.getQuantita() != null && !tassa.getQuantita().equals("") && !tassa.getQuantita().equals("-")) {
												if (!quantita.equals("")) {
													quantita += " ";
												}
												quantita += tassa.getQuantita();
											}%>
										<span class="extWinTXTData"><%=quantita%></span>							
									</td>
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tassa.getDtIniValidita()%></span>							
									</td>
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tassa.getDtFinValidita() == null ||
																		tassa.getDtFinValidita().equals("") ||
																		tassa.getDtFinValidita().equals("-") ||
																		tassa.getDtFinValidita().equals("31/12/9999") ? "ATTUALE" : tassa.getDtFinValidita()%></span>							
									</td>
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tassa.getAnnoValiditaTariffa()%></span>						
									</td>
								</tr>
								<tr id="rOggDett<%=contatore%>" style="cursor: pointer; display: none;" title="Clicca per chiudere il dettaglio dell'oggetto"
								onclick="visualizzaDettaglioOggetto('<%=contatore%>', false);">
									<td colspan = "8" style="text-align: center;">
										<span class="TXTmainLabel">Dati Oggetto</span>
										<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 95%; margin-top: 5px; margin-bottom: 5px;">
											<tr>
												<td class="TDmainLabel" style="width:20%;">
													<span class="TXTmainLabel">Tipo Documento</span>
												</td>
												
												<td class="TDviewTextBox" style="width:24%;">
													<span class="TXTviewTextBox"><%=tassa.getTipoDocumento()%></span>
												</td>
												
												<td style="width:4%"></td>
												
												<td class="TDmainLabel" style="width:20%;">
													<span class="TXTmainLabel">Numero Documento</span>
												</td>
												
												<td class="TDviewTextBox" style="width:24%;">
													<span class="TXTviewTextBox"><%=tassa.getNumeroDocumento()%></span>
												</td>					
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Anno Documento</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getAnnoDocumento()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Stato Documento</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getStatoDocumento()%></span>
												</td>					
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Numero Protocollo</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getNumeroDocumento()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Anno Contabile Doc.</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getAnnoContabileDocumento()%></span>
												</td>					
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Data Inizio Val.</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getDtIniValidita()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Data Fine Val.</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getDtFinValidita() == null ||
																		tassa.getDtFinValidita().equals("") ||
																		tassa.getDtFinValidita().equals("-") ||
																		tassa.getDtFinValidita().equals("31/12/9999") ? "ATTUALE" : tassa.getDtFinValidita()%></span>
												</td>					
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Data Protocollo</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getDtProtocollo()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Data Richiesta</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getDtRichiesta()%></span>
												</td>					
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Tipo Occupazione</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getTipoOccupazione()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Codice Immobile</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getCodiceImmobile()%></span>
												</td>					
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Indirizzo</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getIndirizzoCompleto()%></span>
												</td>
												
												<td></td>
											
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Zona</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getZona()%></span>
												</td>			
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Foglio</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getFoglio()%></span>
												</td>
												
												<td></td>
														
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Particella</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getParticella()%></span>
												</td>			
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Subalterno</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getSubalterno()%></span>
												</td>
												
												<td></td>
														
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Quantità</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=quantita%></span>
												</td>			
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Tariffa per Unità</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getTariffaPerUnita()%></span>
												</td>
												
												<td></td>
														
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Importo Canone</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getImportoCanone()%></span>
												</td>			
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Data Inizio Val. Tar.</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getDtIniValiditaTariffa()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Data Fine Val. Tar.</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getDtFinValiditaTariffa() == null ||
																		tassa.getDtFinValiditaTariffa().equals("") ||
																		tassa.getDtFinValiditaTariffa().equals("-") ||
																		tassa.getDtFinValiditaTariffa().equals("31/12/9999") ? "ATTUALE" : tassa.getDtFinValiditaTariffa()%></span>
												</td>					
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Descrizione</span>
												</td>
												
												<td class="TDviewTextBox" colspan="4">
													<span class="TXTviewTextBox"><%=tassa.getDescrizione()%></span>
												</td>		
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Note</span>
												</td>
												
												<td class="TDviewTextBox" colspan="4">
													<span class="TXTviewTextBox"><%=tassa.getNote()%></span>
												</td>		
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Codice Esenzione</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getCodiceEsenzione()%></span>
												</td>
												
												<td></td>
												
												<td></td>
												
												<td></td>					
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Sconto Assoluto</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getScontoAssoluto()%></span>
												</td>
												
												<td></td>
														
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Percentuale Sconto</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getPercentualeSconto()%></span>
												</td>			
											</tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Data Inizio Sconto</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getDtIniSconto()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Data Fine Sconto</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=tassa.getDtFinSconto()%></span>
												</td>					
											</tr>
										</table>
									</td>
								</tr>
								<% contatore++;
								} %>
							</table>
							</div>
						<% }%>
					</div>
			
			<% if(finder != null){%>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<% }else{%>
				<input type='hidden' name="ACT_PAGE" value="">
			<% }%>
	
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="113">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
	
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>