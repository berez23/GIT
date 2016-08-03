<%@ page language="java" import="java.util.ArrayList,java.util.Iterator"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  AcquaUtenze obj = (AcquaUtenze)sessione.getAttribute(AcquaLogic.ACQUA); %>
<%  AcquaUtente obj2 = (AcquaUtente)sessione.getAttribute(AcquaLogic.ACQUA_UTENTE); %>
<%  java.util.Vector listaAltreUtenze = (java.util.Vector) sessione.getAttribute(AcquaLogic.ALTRE_UTENZE); %>
<%  java.util.Vector listaCatasto = (java.util.Vector) sessione.getAttribute(AcquaLogic.CATASTO); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.acqua.bean.AcquaFinder finder = null;

if (sessione.getAttribute(AcquaLogic.FINDER) != null){ 
	finder = (it.escsolution.escwebgis.acqua.bean.AcquaFinder)sessione.getAttribute(AcquaLogic.FINDER);
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

<%@page import="it.escsolution.escwebgis.acqua.bean.AcquaUtente"%>
<%@page import="it.escsolution.escwebgis.acqua.bean.AcquaUtenze"%>
<%@page import="it.escsolution.escwebgis.acqua.bean.AcquaCatasto"%>
<%@page import="it.escsolution.escwebgis.acqua.logic.AcquaLogic"%>
<html>
	<head>
		<title>Forniture Idriche - Dettaglio</title>
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
				&nbsp;<%=funzionalita%>:&nbsp;Dati fornitura idrica</span>
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
	
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Acqua" target="_parent">
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Dati fornitura idrica</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
										<tr>
											<td colspan="6" class="TDmainLabel" >
												<span class="TXTmainLabel">DATI UTENTE</span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Denominazione</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=!"-".equals(obj2.getDenominazione())?obj2.getDenominazione():(obj2.getCognome() + " " + obj2.getNome())%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Cod. fiscale/Part. iva</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=!"-".equals(obj2.getPartIva())?obj2.getPartIva():obj2.getCodFiscale()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Via</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj2.getViaResidenza()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Civico</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj2.getCivicoResidenza()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Comune</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj2.getComuneResidenza()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Telefono</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj2.getTelefono()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Fax/Email</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj2.getFaxEmail()%></span>
											</td>
										</tr>
																			
									</table>
									
									
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">										
										<tr>
											<td colspan="6" class="TDmainLabel" >
												<br />
											</td>
										</tr>
										<tr>
											<td colspan="6" class="TDmainLabel" >
												<span class="TXTmainLabel">DATI UTENZA</span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Codice servizio</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getCodServizio()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Categoria</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getDescrCategoria()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Qualifica titolare</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getQualificaTitolare()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipologia</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getTipologia()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipo contratto</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getTipoContratto()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Data utenza</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getDtUtenza()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Nominativo</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getRagSocUbicazione()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Indirizzo</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getViaUbicazione() + " " + obj.getCivicoUbicazione()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">CAP</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getCapUbicazione()%></span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Mesi fatturazione</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getMesiFatturazione()%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Consumo medio</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=(!"-".equals(obj.getConsumoMedio()))? obj.getConsumoMedio() : (obj.getStacco()!=null ? obj.getStacco()+"/"+obj.getGiro() : "-")%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Fatturato</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=obj.getFatturato()%></span>
											</td>
										</tr>
									</table>
					</td>
				</tr>
			</table>
			
			<div class="tabber">

				<% if (listaAltreUtenze != null && listaAltreUtenze.size()>0) {%>
				<div class="tabbertab">
				<h2>Altre utenze</h2>
				<table  class="extWinTable" style="width: 100%;">
					<tr>&nbsp;</tr>
					<tr class="extWinTXTTitle">Altre utenze</tr>
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod. servizio</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Ragione sociale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Via</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Consumo medio</span></td>
					</tr>
					
					<% AcquaUtenze o = new AcquaUtenze(); %>
				  <% java.util.Enumeration en = listaAltreUtenze.elements(); int contatore = 1; %>
				  <% ArrayList alAltreUtenze = new ArrayList();%>
				  <% while (en.hasMoreElements()) {
					  o = (AcquaUtenze)en.nextElement();
					  if (o != null && o.getCodServizio() != null){
					  %>
				    	<tr id="r<%=contatore%>">
				
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getCodServizio() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getRagSocUbicazione() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getViaUbicazione() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getCivicoUbicazione() %> </span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getComuneUbicazione() %> </span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= (!"-".equals(o.getConsumoMedio()))? o.getConsumoMedio() : (o.getStacco()!=null ? o.getStacco()+"/"+o.getGiro() : "-") %> </span></td>
				
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
				
				<% if (listaCatasto != null && listaCatasto.size()>0) {%>
				<div class="tabbertab">
				<h2>Dati catastali</h2>
				<table  class="extWinTable" style="width: 100%;">
					<tr>&nbsp;</tr>
					<tr class="extWinTXTTitle">Dati catastali</tr>
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Assenza dati cat.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Estensione part.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipologia part.</span></td>
						<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">Mappa<span class="extWinTXTTitle"></td>
					</tr>
					
					<% AcquaCatasto o = new AcquaCatasto(); %>
				  <% java.util.Enumeration en = listaCatasto.elements(); int contatore = 1; %>
				  <% ArrayList alCatasto = new ArrayList();%>
				  <% while (en.hasMoreElements()) {
					  o = (AcquaCatasto)en.nextElement();
					  if (o != null && o.getCodServizio() != null){
					  %>
				    	<tr id="r<%=contatore%>">
				
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getAssenzaDatiCat() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getSezione() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getFoglio() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getParticella() %> </span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getSubalterno() %> </span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getEstensionePart() %> </span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= o.getTipologiaPart() %> </span></td>
							<td onclick="zoomInMappaParticelle('<%= o.getCodEnte() %>','<%=o.getFoglio()%>','<%=o.getParticella()%>');" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;text-align:center; border-style: none; padding-right: 10px;'>
							<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif" border="0"/></span></td>
							<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
								<span class="extWinTXTData">
									<a href="javascript:apriPopupVirtualH(<%=o.getLatitudine()==null?0:o.getLatitudine()%>,<%=o.getLongitudine()==null?0:o.getLongitudine()%>);">
									<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
									</a>
								</span>
							</td>
							<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
								<span class="extWinTXTData">
									<a href="javascript:apriPopupStreetview(<%=o.getLatitudine()==null?0:o.getLatitudine()%>,<%=o.getLongitudine()==null?0:o.getLongitudine()%>);">
									<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
									</a>
								</span>
							</td>
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
			<input type='hidden' name="UC" value="121">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
	
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>