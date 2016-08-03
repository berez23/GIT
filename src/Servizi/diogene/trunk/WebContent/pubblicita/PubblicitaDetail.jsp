<%@ page language="java" import="it.escsolution.escwebgis.tributi.bean.*,java.util.ArrayList,java.util.Iterator"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  PubblicitaTestata tes = (PubblicitaTestata)sessione.getAttribute(PubblicitaLogic.PUBBLICITA); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.pubblicita.bean.PubblicitaFinder finder = null;

if (sessione.getAttribute(PubblicitaLogic.FINDER) != null){ 
	finder = (it.escsolution.escwebgis.pubblicita.bean.PubblicitaFinder)sessione.getAttribute(PubblicitaLogic.FINDER);
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

<%@page import="it.escsolution.escwebgis.pubblicita.bean.PubblicitaTestata"%>
<%@page import="it.escsolution.escwebgis.pubblicita.bean.PubblicitaDettaglio"%>
<%@page import="it.escsolution.escwebgis.pubblicita.logic.PubblicitaLogic"%>
<html>
	<head>
		<title>Pubblicita' - Dettaglio</title>
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
				&nbsp;<%=funzionalita%>:&nbsp;Dati Flusso Pubblicita</span>
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
	
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Pubblicita" target="_parent">
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Dati Pratica</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipo Pratica</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getTipoPratica()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Anno </span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getAnnoPratica()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Numero</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getNumPratica()%></span>
											</td>
	
											</tr>
										
										
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Data Inizio</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getDtInizio()== null ||
																		tes.getDtInizio().equals("") ||
																		tes.getDtInizio().equals("-") ||
																		tes.getDtInizio().equals("01/01/0001") ? "-" : tes.getDtInizio()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Dec. Termini</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getDtDecTermini()== null ||
																		tes.getDtDecTermini().equals("") ||
																		tes.getDtDecTermini().equals("-") ||
																		tes.getDtDecTermini().equals("31/12/9999") ? "ATTUALE" : tes.getDtDecTermini()%></span>	
											</td>	
										</tr>
										<tr>				
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Provvedimento</span>
											</td>
											
											<td class="TDviewTextBox" colspan="5">
												<span class="TXTviewTextBox"><%=tes.getProvvedimento()!=null ? tes.getProvvedimento() : "-"%></span>
											</td>			
										</tr>
												<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Descrizione</span>
											</td>
											
											<td class="TDviewTextBox" colspan="5">
												<span class="TXTviewTextBox"><%=tes.getDescPratica()!=null ? tes.getDescPratica() : "-"%></span>
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
					
					
			
			<% ArrayList<PubblicitaDettaglio> lstdet = tes.getLstDettaglio();
			if (lstdet != null && lstdet.size() > 0) {%>
				<div class="tabbertab">
						
						<h2>Oggetti Territoriali Collegati alla Pratica</h2>
					
							<table align=left class="extWinTable" style="width: 100%; margin-top: 5px;" cellpadding="0" cellspacing="0">	
								<tr>&nbsp;</tr>
								<tr class="extWinTXTTitle">Oggetti Territoriali Collegati alla Pratica</tr>
								<tr>
								    <td class="extWinTDTitle"><span class="extWinTXTTitle">Data Inizio</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Via</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Oggetto</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Annotazioni</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Giorni Occ.</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Zona Cespite</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Base</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Altezza</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Sup.Imponibile</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Sup.Esistente</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Caratt.</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Esemplari</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Facce</span></td>
								</tr>
								<% 	int contatore = 1;
								for (int i = 0; i < lstdet.size(); i++) {
					
									PubblicitaDettaglio det = (PubblicitaDettaglio)lstdet.get(i);%>
								<tr>			
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getDtInizio()%></span>		
									
									<td class="extWinTDData" style="text-align: center;">
										 <span class="extWinTXTData"><%=det.getDtFine()== null ||
																		det.getDtFine().equals("") ||
																		det.getDtFine().equals("-") ||
																		det.getDtFine().equals("31/12/9999") ? "ATTUALE" : det.getDtFine()%></span>							
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=det.getVia()!=null ? det.getVia() : "-" %></span>
									</td>
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getCivico()!=null? det.getCivico() : "-"%></span>							
									</td>
									<td class="extWinTDData" style="text-align: left;">
										<span class="extWinTXTData"><%=det.getIndirizzo()!=null? det.getIndirizzo() : "-"%></span>							
									</td>
							
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=det.getDescClasse()!=null ? det.getDescClasse(): det.getCodClasse() %></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=det.getDescOggetto()!=null ? det.getDescOggetto(): det.getCodOggetto() %></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=det.getAnnotazioni()!=null ? det.getAnnotazioni() : "-"%></span>							
									</td>
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getNumGiorniOccupazione()!=null ? det.getNumGiorniOccupazione() : "-" %></span>		
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getDescZonaCespite()!=null ? det.getDescZonaCespite(): "-"%></span>						
									</td>
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"> <%=det.getBase()== null ||
																		det.getBase().equals("") ||
																		det.getBase().equals("-")  ? "-" : det.getBase() +" m"%></span>						
									</td>
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getAltezza()%> m</span>						
									</td>
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getSupImponibile()%> m</span>						
									</td>
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getSupEsistente()%> m</span>						
									</td>
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getDescCaratteristica()==null ? "-" : det.getDescCaratteristica()%></span>						
									</td>
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getNumFacce()%></span>						
									</td>
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=det.getNumEsempleri()%></span>						
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
			<input type='hidden' name="UC" value="115">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
	
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>