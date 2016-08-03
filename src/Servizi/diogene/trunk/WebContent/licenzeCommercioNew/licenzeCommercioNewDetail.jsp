<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ page language="java" %>
<%   HttpSession sessione = request.getSession(true);  %> 
<%   LicenzeCommercioNew lic = (LicenzeCommercioNew) sessione.getAttribute(LicenzeCommercioNewLogic.DATI_LICENZE_COMMERCIO_NEW); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%   LicenzeCommercioNewFinder finder = null;

	 if (sessione.getAttribute(LicenzeCommercioNewServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(LicenzeCommercioNewServlet.NOMEFINDER)).getClass() == new LicenzeCommercioNewFinder().getClass()){
			finder = (LicenzeCommercioNewFinder)sessione.getAttribute(LicenzeCommercioNewServlet.NOMEFINDER);
		}					
	 }
	
	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

	java.util.Vector vctLink = null; 
	if (sessione.getAttribute("LISTA_INTERFACCE") != null){
		vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
	}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>	
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
<%@page import="it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioNew"%>
<%@page import="it.escsolution.escwebgis.licenzeCommercioNew.logic.LicenzeCommercioNewLogic"%>
<%@page import="it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioNewFinder"%>
<%@page import="it.escsolution.escwebgis.licenzeCommercioNew.servlet.LicenzeCommercioNewServlet"%>

<%@page import="it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioAnagNew"%>
<html>
	<head>
		<title>Licenze di Commercio - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	
	<script>

		function vaiAStorico()
		{
			wait();
			document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/LicenzeCommercioNew";	
			document.mainform.ST.value = 3;
			document.mainform.OGGETTO_SEL.value = document.all.item("IDSTORICO").value;
			document.mainform.submit();
		}
	
		function mettiST(){
			document.mainform.ST.value = 3;
		
		}
	
	</script>
	
	<body >
	
	<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Dati Licenze di Commercio</span>
		</div>
		&nbsp;
	<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
	<br/>
		<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
	
		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/LicenzeCommercioNew" target="_parent">
		
		
		
		<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>
		&nbsp;
		<table style="background-color: white; width: 100%;">
		
			<tr style="background-color: white;">
				<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">		
		
					<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
						<tr>
							<td colspan=3>
								<table class="viewExtTable" >
									<tr>
										<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Dati Storici:</span></td>
										<td class="TDmainLabel" style="width:210;"> 
												<%  
												    HashMap idStorici = (LinkedHashMap)session.getAttribute(EscServlet.IDSTORICI); 
													if (idStorici!=null) {
												%>							
													<select onchange="vaiAStorico()" id="IDSTORICO" name="IDSTORICO" class="INPDBComboBox">
													  <% 
														  Iterator it = idStorici.entrySet().iterator();
														  while (it.hasNext()) {
														      Map.Entry me = (Map.Entry)it.next();
														      String id = (String)me.getKey();
														      String data =(String) me.getValue(); 
														      %>
										       			        <option 
										       			        <%if (id.equals(lic.getId())){%>
										       			        		SELECTED
										       			        <%} %>
										       			        value="<%=id%>"><%=data%></option>
													  	<% 
														  } 
											  			%>
											        </select>
											     <%
											     }
											     %>
										</td>
									</tr>
								</table>
							</td>
						</tr>						
					</table>
					
					
					
					&nbsp;
		
					<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 100%;">
						<%String indirizzo = lic.getIndirizzo() == null ? "" : lic.getIndirizzo();
				     	if (lic.getCivico() != null && !lic.getCivico().equals("")) {
				     		if (!indirizzo.equals("")) {
				     			indirizzo += ", ";
				     		}
				     		indirizzo += lic.getCivico();
				     	}%>				
						<tr>
							<td>
								<table class="viewExtTable" style="width: 100%" cellpadding="0">
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel" style="width: 25%;"><span class="TXTmainLabel">Numero</span></td>
										<td class="TDviewTextBox" style="width: 25%"><span class="TXTviewTextBox"><%=lic.getNumero()%></span></td>
										<td style="width: 25%">
											<table cellpadding="0" cellspacing="0" style="width: 100%">
												<tr>
													<td class="TDmainLabel" style="width: 65%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Numero Protocollo</span></td>
													<td class="TDviewTextBox" style="width: 35%"><span class="TXTviewTextBox"><%=lic.getNumeroProtocollo() %></span></td>
												</tr>
											</table>								
										</td>
										<td style="width: 25%">
											<table cellpadding="0" cellspacing="0" style="width: 100%">
												<tr>
													<td class="TDmainLabel" style="width: 65%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Anno Protocollo</span></td>
													<td class="TDviewTextBox" style="width: 35%"><span class="TXTviewTextBox"><%=lic.getAnnoProtocollo() %></span></td>
												</tr>
											</table>						
										</td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Tipologia</span></td>
										<td class="TDviewTextBox" colspan=3><span class="TXTviewTextBox"><%=lic.getTipologia()%></span></td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Carattere</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lic.getCarattere()%></span></td>
										<td class="TDmainLabel" style="text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Stato</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lic.getStato()%></span></td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data inizio sospensione</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lic.getDataInizioSospensione()%></span></td>
										<td class="TDmainLabel" style="text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Data fine sospensione</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lic.getDataFineSospensione()%></span></td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Indirizzo</span></td>
										<td class="TDviewTextBox" colspan=3><span class="TXTviewTextBox"><%=indirizzo%></span></td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Superficie minuto mq.</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lic.getSuperficieMinuto()%></span></td>
										<td class="TDmainLabel" style="text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Superficie totale mq.</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lic.getSuperficieTotale()%></span></td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td>
											<table cellpadding="0" cellspacing="0" style="width: 100%">
												<tr>
													<td class="TDmainLabel" style="width: 50%"><span class="TXTmainLabel">Sezione</span></td>
													<td class="TDviewTextBox" style="width: 50%"><span class="TXTviewTextBox"><%=lic.getSezioneCatastale() %></span></td>
												</tr>
											</table>								
										</td>
										<td>
											<table cellpadding="0" cellspacing="0" style="width: 100%">
												<tr>
													<td class="TDmainLabel" style="width: 65%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Foglio</span></td>
													<td class="TDviewTextBox" style="width: 35%"><span class="TXTviewTextBox"><%=lic.getFoglio() %></span></td>
												</tr>
											</table>								
										</td>
										<td>
											<table cellpadding="0" cellspacing="0" style="width: 100%">
												<tr>
													<td class="TDmainLabel" style="width: 65%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Particella</span></td>
													<td class="TDviewTextBox" style="width: 35%"><span class="TXTviewTextBox"><%=lic.getParticella() %></span></td>
												</tr>
											</table>								
										</td>
										<td>
											<table cellpadding="0" cellspacing="0" style="width: 100%">
												<tr>
													<td class="TDmainLabel" style="width: 65%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Subalterno</span></td>
													<td class="TDviewTextBox" style="width: 35%"><span class="TXTviewTextBox"><%=lic.getSubalterno() %></span></td>
												</tr>
											</table>								
										</td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Codice fabbricato</span></td>
										<td class="TDviewTextBox" colspan=3><span class="TXTviewTextBox"><%=lic.getCodiceFabbricato()%></span></td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data validità</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lic.getDataValidita()%></span></td>
										<td>
											<table cellpadding="0" cellspacing="0" style="width: 100%">
												<tr>
													<td class="TDmainLabel" style="width: 50%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Data rilascio</span></td>
													<td class="TDviewTextBox" style="width: 50%"><span class="TXTviewTextBox"><%=lic.getDataRilascio() %></span></td>
												</tr>
											</table>								
										</td>
										<td>
											<table cellpadding="0" cellspacing="0" style="width: 100%">
												<tr>
													<td class="TDmainLabel" style="width: 50%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Zona</span></td>
													<td class="TDviewTextBox" style="width: 50%"><span class="TXTviewTextBox"><%=lic.getZona() %></span></td>
												</tr>
											</table>								
										</td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Ragione sociale</span></td>
										<td class="TDviewTextBox" colspan=3><span class="TXTviewTextBox"><%=lic.getRagSoc()%></span></td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Note</span></td>
										<td class="TDviewTextBox" colspan=3><span class="TXTviewTextBox"><%=lic.getNote()%></span></td>
									</tr>
									<tr></tr>
									<tr></tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Riferimento atto</span></td>
										<td class="TDviewTextBox" colspan=3><span class="TXTviewTextBox"><%=lic.getRiferimAtto()%></span></td>
									</tr>
									<tr></tr>
									<tr></tr>
								</table>
							</td>				
						</tr>							
					</table>
					</td>
					
				
			</tr>
		</table>		
					
					
					
					<br><br>
					<div class="tabber">
					
					<% if (lic.getAnags() != null && lic.getAnags().size() > 0) {%>
				
						<div class="tabbertab">
						
						<h2>Elenco Soggetti</h2>
									
					
						<% for (LicenzeCommercioAnagNew anag : lic.getAnags()) {%>
							
							<table align=left cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 100%;">						
								<%String indirizzoResidenza = anag.getIndirizzoResidenza() == null ? "" : anag.getIndirizzoResidenza();
						     	if (anag.getCivicoResidenza() != null && !anag.getCivicoResidenza().equals("")) {
						     		if (!indirizzoResidenza.equals("")) {
						     			indirizzoResidenza += ", ";
						     		}
						     		indirizzoResidenza += anag.getCivicoResidenza();
						     	}%>	
								<tr>
									<td>
										<table class="viewExtTable" style="width: 100%" cellpadding="0">
											<tr></tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel" style="width: 25%"><span class="TXTmainLabel">Numero</span></td>
												<td class="TDviewTextBox" style="width: 25%"><span class="TXTviewTextBox"><%=anag.getNumero()%></span></td>
												<td class="TDmainLabel" style="width: 25%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Codice fiscale</span></td>
												<td class="TDviewTextBox" style="width: 25%"><span class="TXTviewTextBox"><%=anag.getCodiceFiscale()%></span></td>
											</tr>
											<tr></tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel"><span class="TXTmainLabel">Cognome</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=anag.getCognome()%></span></td>
												<td class="TDmainLabel" style="text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Nome</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=anag.getNome()%></span></td>
											</tr>
											<tr></tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel"><span class="TXTmainLabel">Denominazione / Rag. Soc.</span></td>
												<td class="TDviewTextBox" colspan=3><span class="TXTviewTextBox"><%=anag.getDenominazione()%></span></td>
											</tr>
											<tr></tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel"><span class="TXTmainLabel">Forma giuridica</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=anag.getFormaGiuridica()%></span></td>
												<td class="TDmainLabel" style="text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Titolo</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=anag.getTitolo()%></span></td>
											</tr>
											<tr></tr>
											<tr></tr>
											<tr>
												<td>
													<table cellpadding="0" cellspacing="0" style="width: 100%">
														<tr>
															<td class="TDmainLabel" style="width: 50%"><span class="TXTmainLabel">Data nascita</span></td>
															<td class="TDviewTextBox" style="width: 50%"><span class="TXTviewTextBox"><%=anag.getDataNascita()%></span></td>
														</tr>
													</table>								
												</td>
												<td colspan=2>
													<table cellpadding="0" cellspacing="0" style="width: 100%">
														<tr>
															<td class="TDmainLabel" style="width: 30%"><span class="TXTmainLabel">Comune nascita</span></td>
															<td class="TDviewTextBox" style="width: 70%"><span class="TXTviewTextBox"><%=anag.getComuneNascita()%></span></td>
														</tr>
													</table>								
												</td>
												<td>
													<table cellpadding="0" cellspacing="0" style="width: 100%">
														<tr>
															<td class="TDmainLabel" style="width: 65%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Provincia nascita</span></td>
															<td class="TDviewTextBox" style="width: 35%"><span class="TXTviewTextBox"><%=anag.getProvinciaNascita()%></span></td>
														</tr>
													</table>								
												</td>
											</tr>
											<tr></tr>
											<tr></tr>											
											<tr>
												<td class="TDmainLabel"><span class="TXTmainLabel">Indirizzo residenza</span></td>
												<td class="TDviewTextBox" colspan=2><span class="TXTviewTextBox"><%=indirizzoResidenza%></span></td>
												<td>
													<table cellpadding="0" cellspacing="0" style="width: 100%">
														<tr>
															<td class="TDmainLabel" style="width: 65%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">CAP residenza</span></td>
															<td class="TDviewTextBox" style="width: 35%"><span class="TXTviewTextBox"><%=anag.getCapResidenza()%></span></td>
														</tr>
													</table>								
												</td>
											</tr>
											<tr></tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel"><span class="TXTmainLabel">Comune residenza</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=anag.getComuneResidenza()%></span></td>
												<td>
													<table cellpadding="0" cellspacing="0" style="width: 100%">
														<tr>
															<td class="TDmainLabel" style="width: 65%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Prov. residenza</span></td>
															<td class="TDviewTextBox" style="width: 35%"><span class="TXTviewTextBox"><%=anag.getProvinciaResidenza()%></span></td>
														</tr>
													</table>								
												</td>
												<td>
													<table cellpadding="0" cellspacing="0" style="width: 100%">
														<tr>
															<td class="TDmainLabel" style="width: 50%; text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Data iniz. res.</span></td>
															<td class="TDviewTextBox" style="width: 50%"><span class="TXTviewTextBox"><%=anag.getDataInizioResidenza()%></span></td>
														</tr>
													</table>								
												</td>
											</tr>
											<tr></tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel"><span class="TXTmainLabel">Tel.</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=anag.getTel()%></span></td>
												<td class="TDmainLabel" style="text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Fax</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=anag.getFax()%></span></td>
											</tr>
											<tr></tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel"><span class="TXTmainLabel">E-mail</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=anag.getEmail()%></span></td>
												<td class="TDmainLabel" style="text-align: right; padding-right: 3px;"><span class="TXTmainLabel">Partita IVA</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=anag.getPiva()%></span></td>
											</tr>
											<tr></tr>
											<tr></tr>
										</table>
									</td>				
								</tr>							
							</table>
							<br>
						<% } %>	
						</div>					
					<% } %>
					</div>
				</td>		
		
				
		
		<br><br>
		
		<% if (lic != null){%>
		<% String codice = "";
		   codice = lic.getId();%>
		   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
		<%}%>
				
		<% if(finder != null){%>
			<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
		<% }else{%>
			<input type='hidden' name="ACT_PAGE" value="">
		<% }%>
		
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="55">
			<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
			<input type="hidden" name="IdStorici" id="IdSto" value="true">
		</form>	
		
		
		
		<div id="wait" class="cursorWait" />
		
	</body>
</html>