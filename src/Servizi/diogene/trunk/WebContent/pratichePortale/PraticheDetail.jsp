<%@ page language="java" import="java.util.ArrayList,java.util.Iterator"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  Multe obj = (Multe)sessione.getAttribute(MulteLogic.MULTE); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.multe.bean.MulteFinder finder = null;

if (sessione.getAttribute(MulteLogic.FINDER) != null){ 
	finder = (it.escsolution.escwebgis.multe.bean.MulteFinder)sessione.getAttribute(MulteLogic.FINDER);
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

<%@page import="it.escsolution.escwebgis.multe.bean.Multe"%>
<%@page import="it.escsolution.escwebgis.multe.logic.MulteLogic"%>
<html>
	<head>
		<title>Multe - Dettaglio</title>
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
				&nbsp;<%=funzionalita%>:&nbsp;Dati multa</span>
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
	
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Multe" target="_parent">
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Dati Multa</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipo verbale</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getTipoVerbale()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Numero verbale </span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getNrVerbale()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Data</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getDataMulta()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Importo</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getImportoMulta()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Importo dovuto</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getImportoDovuto()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Data scadenza pagamento</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getDtScadenzaPagamento()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Luogo infrazione</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getLuogoInfrazione()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Note</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getNote()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipo ente</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getTipoEnte()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Targa</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getTarga()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Marca</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getMarca()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Modello</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getModello()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Pagato</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getFlagPagamento()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Estremi pagamento</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getEstremiPagamento()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Sistema pagamento</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getSistemaPagamento()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Data pagamento</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getDtPagamento()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Importo pagato</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getImportoPagato()%></span>
											</td>								
										</tr>
									</table>
								</td>
							</tr>
						</table>			
					</td>
				</tr>
				
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Dati Soggetto</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Cognome</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getCognome()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Nome</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getNome()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Codice fiscale</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getCodFisc()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Luogo nascita</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getLuogoNascita()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Data nascita</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getDtNascita()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Luogo residenza</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getLuogoNascita()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Indirizzo residenza</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getIndirizzoResidenza()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Numero patente</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getNrPatente()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Data rilascio patente</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getDtRilascioPatente()%></span>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>	
			</table>
			
			<% if(finder != null){%>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<% }else{%>
				<input type='hidden' name="ACT_PAGE" value="">
			<% }%>
	
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="118">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
	
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>