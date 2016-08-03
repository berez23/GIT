<%@ page language="java" import="java.util.ArrayList,java.util.Iterator"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  RttBollette obj = (RttBollette)sessione.getAttribute(RetteLogic.RETTA); %>
<%  java.util.Vector listaDettaglio = (java.util.Vector) sessione.getAttribute(RetteLogic.DETTAGLIO); %>
<%  java.util.Vector listaRate = (java.util.Vector) sessione.getAttribute(RetteLogic.RATE); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.rette.bean.RetteFinder finder = null;

if (sessione.getAttribute(RetteLogic.FINDER) != null){ 
	finder = (it.escsolution.escwebgis.rette.bean.RetteFinder)sessione.getAttribute(RetteLogic.FINDER);
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

<%@page import="it.escsolution.escwebgis.rette.bean.RttBollette"%>
<%@page import="it.escsolution.escwebgis.rette.bean.RttDettaglio"%>
<%@page import="it.escsolution.escwebgis.rette.bean.RttRate"%>
<%@page import="it.escsolution.escwebgis.rette.logic.RetteLogic"%>
<html>
	<head>
		<title>Rette scolastiche - Dettaglio</title>
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
				&nbsp;<%=funzionalita%>:&nbsp;Dati retta scolastica</span>
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
	
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Rette" target="_parent">
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Dati retta scolastica</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">										
										<tr>
											<td colspan="6" class="TDmainLabel" >
												<br />
											</td>
										</tr>
										<tr>
											<td colspan="6" class="TDmainLabel" >
												<span class="TXTmainLabel">DATI BOLLETTA</span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Codice</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getCodBolletta()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Data</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getDataBolletta()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Oggetto</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getOggetto()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Importo</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getTotBolletta()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Importo pagato</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getTotPagato()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Intestatario</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getDesIntestatario()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Codice fiscale</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getCodiceFiscale()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Recapito</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getRecapito()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Note</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getNote()%></span>
											</td>								
										</tr>									
									</table>
					</td>
				</tr>
			</table>
			
			<div class="tabber">

				<% if (listaDettaglio != null && listaDettaglio.size()>0) {%>
				<div class="tabbertab">
				<h2>Dettaglio</h2>
				<table  class="extWinTable" style="width: 100%;">
					<tr>&nbsp;</tr>
					<tr class="extWinTXTTitle">Dettaglio</tr>
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data inizio servizio</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data fine servizio</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Descrizione</span></td>						
					</tr>
					
					<% RttDettaglio o = new RttDettaglio(); %>
				  <% java.util.Enumeration en = listaDettaglio.elements(); int contatore = 1; %>
				  <% ArrayList alDettaglio = new ArrayList();%>
				  <% while (en.hasMoreElements()) {
					  o = (RttDettaglio)en.nextElement();
					  if (o != null && o.getCodBolletta() != null){
					  %>
				    	<tr id="r<%=contatore%>">
				
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getDtIniServizio() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getDtFinServizio() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getDesVoce() + ": " + o.getValore()%></span></td>
						</tr>			
				<% 		
					  }
						contatore++;
					} 
				%>				  
					
					<tr>
						<td colspan='11' align="center">
							&nbsp;
						</td>
					</tr>
				</table>
				</div>
				<%}%>
				
				<% if (listaRate != null && listaRate.size()>0) {%>
				<div class="tabbertab">
				<h2>Rate</h2>
				<table  class="extWinTable" style="width: 100%;">
					<tr>&nbsp;</tr>
					<tr class="extWinTXTTitle">Rate</tr>
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data scadenza</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Importo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Importo pagato</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data pagamento</span></td>
					</tr>
					
					<% RttRate o = new RttRate(); %>
				  <% java.util.Enumeration en = listaRate.elements(); int contatore = 1; %>
				  <% ArrayList alRate = new ArrayList();%>
				  <% while (en.hasMoreElements()) {
					  o = (RttRate)en.nextElement();
					  if (o != null && o.getCodBolletta() != null){
					  %>
				    	<tr id="r<%=contatore%>">
				
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getNumRata() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getDtScadenzaRata() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getImportoRata() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getImportoPagato() %> </span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getDtPagamento() %> </span></td>
						</tr>			
				<% 		
					  }
						contatore++;
					} 
				%>				  
					
					<tr>
						<td colspan='11' align="center">
							&nbsp;
						</td>
					</tr>
				</table>
				</div>
				<%}%>
				
			</div>
			
			<% if(finder != null){%>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<% }else{%>
				<input type='hidden' name="ACT_PAGE" value="">
			<% }%>
	
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="119">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
	
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>