<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ page language="java" import="it.escsolution.escwebgis.concessioni.bean.*"%>
<%  HttpSession sessione = request.getSession(true);  %> 
<%  StoricoConcessioni con = (StoricoConcessioni) sessione.getAttribute(StoricoConcessioniLogic.DATI_CONCESSIONE); %>
<%  java.util.HashMap datiTecnici = (java.util.HashMap) sessione.getAttribute(it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic.DATI_TECNICI); %>
<%  java.util.ArrayList listaSoggetti = (java.util.ArrayList) sessione.getAttribute(it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic.LISTA_SOGGETTI); %>
<%  java.util.ArrayList listaDatiCatastali = (java.util.ArrayList) sessione.getAttribute(it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic.LISTA_DATI_CATASTALI); %>
<%  java.util.ArrayList listaIndirizzi = (java.util.ArrayList) sessione.getAttribute(it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic.LISTA_INDIRIZZI); %>
<%  java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	int st = new Integer(ST).intValue(); %>
<%  it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniFinder finder = null;

	if (sessione.getAttribute(it.escsolution.escwebgis.concessioni.servlet.StoricoConcessioniServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(it.escsolution.escwebgis.concessioni.servlet.StoricoConcessioniServlet.NOMEFINDER)).getClass() == new StoricoConcessioniFinder().getClass()){
			finder = (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniFinder)sessione.getAttribute(it.escsolution.escwebgis.concessioni.servlet.StoricoConcessioniServlet.NOMEFINDER);
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
<%@page import="it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
<html>
	<head>
		<title>Storico Concessioni - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	<script>	
	
		function vaiAStorico() {
			<% if (request.getParameter("popup") != null && new Boolean(request.getParameter("popup")).booleanValue()) { %>
				document.mainform.popup.value='true';					
			<% } else {%>
				wait();
			<% }%>			
			document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/StoricoConcessioni";	
			document.mainform.ST.value = 3;
			document.mainform.OGGETTO_SEL.value = document.all.item("IDSTORICO").value;
			document.mainform.submit();			
		}
		
		function vaiAListaDocfa(foglio, particella, subalterno){
			wait();
			document.mainform.action = "<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Docfa";
			document.mainform.FOGLIO.value = foglio;
			document.mainform.PARTICELLA.value = particella;
			document.mainform.SUBALTERNO.value = subalterno;			
			document.mainform.ST.value = 2;
			document.mainform.UC.value = 43;
			document.mainform.submit();
		}
		
		function apriPopupCatasto(f,p,s,d,cod_ente) {
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupCatasto=true&f='+f+'&p='+p+'&s='+s+'&d='+d+'&cod_ente='+cod_ente;
			var finestra=window.open(url,"","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
		}
		
		function apriPopupConcCollegate(conId,fg,part,sub,cod_ente) {
			var url = '<%= request.getContextPath() %>/StoricoConcessioni?ST=2&popup=true&collegate=true&conId='+conId+'&fg='+fg+'&part='+part+'&sub='+sub+'&cod_ente='+cod_ente;
			var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
		}
	
		function mettiST() {
			document.mainform.ST.value = 3;
		}
		
	</script>
	<body >
	
	<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Dati Concessione</span>
	</div>
		
	<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>
	
	<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
	&nbsp;	
	
		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/StoricoConcessioni" target="_parent">
		
			<input type='hidden' name="STORICO_CONCESSIONI" value="TRUE">
			<input type='hidden' name="FOGLIO" value="">
			<input type='hidden' name="PARTICELLA" value="">
			<input type='hidden' name="SUBALTERNO" value="">
			<input type='hidden' name="popup" value="false">
			<% if (request.getParameter("collegate") != null && new Boolean(request.getParameter("collegate")).booleanValue()) { %>
				<input type='hidden' name="collegate" value="true">
				<input type='hidden' name="conId" value="<%=request.getParameter("conId")%>">
				<input type='hidden' name="fg" value="<%=request.getParameter("fg")%>">
				<input type='hidden' name="part" value="<%=request.getParameter("part")%>">
				<input type='hidden' name="sub" value="<%=request.getParameter("sub")%>">
				<input type='hidden' name="cod_ente" value="<%=request.getParameter("cod_ente")%>">
			<% } %>
	
			
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center;">
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 50%;">
							<tr>
								<td>
									<table class="viewExtTable" style="width: 100%">
										<tr>
											<td class="TDmainLabel"  style="width: 40%"><span class="TXTmainLabel">Dati Storici:</span></td>
											<td class="TDmainLabel" style="width: 60%"> 
												<%  
											    HashMap idStorici = (LinkedHashMap)session.getAttribute(StoricoConcessioniLogic.IDSTORICI_CONCESSIONI); 
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
									       			          	<%if (id.equals(con.getId())){%>
									       			        		SELECTED
									       			        	<%} %>
									       			        	value="<%=id%>"   ><%=data%></option>
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
						
						<% if (con.getCartellaSuap() != null) {%>
							<jsp:include page="suapFileBrowser.jsp"></jsp:include>
						<% } %>
		
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 90%;">
							<tr>
								<td>
									<table class="viewExtTable" style="width: 100%">
										<tr>
											<td class="TDmainLabel" style="width: 18%;"><span class="TXTmainLabel">Concessione/Fascicolo</span></td>
											<td class="TDviewTextBox" style="width: 15%;"><span class="TXTviewTextBox"><%=con.getConcessioneNumero()%></span></td>
											<td class="TDmainLabel" style="width: 18%;"><span class="TXTmainLabel">Anno</span></td>
											<td class="TDviewTextBox" style="width: 15%;"><span class="TXTviewTextBox"><%=con.getProgressivoAnno()%></span></td>
											<td class="TDmainLabel" style="width: 20%;"><span class="TXTmainLabel">Progressivo</span></td>
											<td class="TDviewTextBox" style="width: 14%;"><span class="TXTviewTextBox"><%=con.getProgressivoNumero()%></span></td>
										</tr>
										<tr>
											<td class="TDmainLabel"><span class="TXTmainLabel">Data protocollo</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getProtocolloData()%></span></td>
											<td class="TDmainLabel"><span class="TXTmainLabel">Numero protocollo</span></td>
											<td class="TDviewTextBox" colspan="3"><span class="TXTviewTextBox"><%=con.getProtocolloNumero()%></span></td>
										</tr>
										<tr>
											<td class="TDmainLabel"><span class="TXTmainLabel">Tipo intervento</span></td>
											<td class="TDviewTextBox" colspan="5"><span class="TXTviewTextBox"><%=con.getTipoIntervento()%></span></td>
										</tr>
										<tr>
											<td class="TDmainLabel"><span class="TXTmainLabel">Oggetto</span></td>
											<td class="TDviewTextBox" colspan="5"><span class="TXTviewTextBox"><%=con.getOggetto()%></span></td>
										</tr>
										<tr>
											<td class="TDmainLabel"><span class="TXTmainLabel">Procedimento</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getProcedimento()%></span></td>
											<td class="TDmainLabel"><span class="TXTmainLabel">Zona</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getZona()%></span></td>
											<td class="TDmainLabel"><span class="TXTmainLabel">Data rilascio</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getDataRilascio()%></span></td>
										</tr>
										<tr>
											<td class="TDmainLabel"><span class="TXTmainLabel">Data inizio lavori</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getDataInizioLavori()%></span></td>
											<td class="TDmainLabel"><span class="TXTmainLabel">Data fine lavori</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getDataFineLavori()%></span></td>
											<td class="TDmainLabel"><span class="TXTmainLabel">Data proroga lavori</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getDataProrogaLavori()%></span></td>
										</tr>
										<tr>
											<td class="TDmainLabel"><span class="TXTmainLabel">Esito</span></td>
											<td class="TDviewTextBox" colspan="3"><span class="TXTviewTextBox"><%=con.getEsito() %></span></td>
											<td class="TDmainLabel"><span class="TXTmainLabel">Provenienza</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getProvenienza()%></span></td>
										</tr>
										<tr>
											<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Posizione</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getPosizioneCodice() %></span></td>
											<td class="TDmainLabel"><span class="TXTmainLabel">Descr.Posizione</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getPosizioneDescrizione()%></span></td>
											<td class="TDmainLabel"><span class="TXTmainLabel">Data Posizione</span></td>
											<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=con.getPosizioneData()%></span></td>
										</tr>

									</table>
								</td>
							</tr>
						</table>
			
						&nbsp;
		
						<br>
						
						<% if (datiTecnici != null && datiTecnici.size() > 0) {%>
												
							<div style="text-align: center;">
								<span class="TXTmainLabel">Dati Tecnici</span>
							</div>
							
							<br>
							
							<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 90%;">
								<tr>								
									<td>
										<table class="viewExtTable" style="width: 100%">
											<tr>
												<td class="TDmainLabel" style="width: 18%;"><span class="TXTmainLabel">Destinazione d'uso</span></td>
												<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=datiTecnici.get("DEST_USO") == null ? "" : (String)datiTecnici.get("DEST_USO")%></span></td>
												<td class="TDmainLabel" colspan="2" style="text-align: right;"><span class="TXTmainLabel">Superficie effettiva lotto mq.</span></td>
												<td class="TDviewTextBox" style="width: 23%; text-align: right;"><span class="TXTviewTextBox"><%=datiTecnici.get("SUP_EFF_LOTTO") == null ? "" : (String)datiTecnici.get("SUP_EFF_LOTTO")%></span></td>
											</tr>
											<tr>
												<td class="TDmainLabel"><span class="TXTmainLabel">Superficie coperta mq.</span></td>
												<td class="TDviewTextBox" style="text-align: right;" colspan="2"><span class="TXTviewTextBox"><%=datiTecnici.get("SUP_COPERTA") == null ? "" : (String)datiTecnici.get("SUP_COPERTA")%></span></td>
												<td class="TDmainLabel" colspan="2" style="text-align: right;"><span class="TXTmainLabel">Volume totale mc.</span></td>
												<td class="TDviewTextBox" style="text-align: right;"><span class="TXTviewTextBox"><%=datiTecnici.get("VOL_TOTALE") == null ? "" : (String)datiTecnici.get("VOL_TOTALE")%></span></td>
											</tr>
											<tr>
												<td class="TDmainLabel"><span class="TXTmainLabel">Abitazioni</span></td>
												<td class="TDviewTextBox" style="width: 15%; text-align: right;"><span class="TXTviewTextBox"><%=datiTecnici.get("NUM_ABITAZIONI") == null ? "" : (String)datiTecnici.get("NUM_ABITAZIONI")%></span></td>
												<td class="TDmainLabel" style="width: 14%; text-align: right;"><span class="TXTmainLabel">Vani</span></td>
												<td class="TDviewTextBox" style="width: 12%; text-align: right;"><span class="TXTviewTextBox"><%=datiTecnici.get("VANI") == null ? "" : (String)datiTecnici.get("VANI")%></span></td>
												<td class="TDmainLabel" style="width: 18%; text-align: right;"><span class="TXTmainLabel">Data agibilità</span></td>
												<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=datiTecnici.get("DT_AGIBILITA") == null ? "" : (String)datiTecnici.get("DT_AGIBILITA")%></span></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
				
							&nbsp;
			
							<br>
							
						<% } %>
		
					</td>
		
					
				</tr>
			</table>
			
			<div class="tabber">
					
					
			
			<% if (listaSoggetti != null && listaSoggetti.size() > 0) {%>
				<div class="tabbertab">
						
						<h2>Lista Soggetti</h2>
					
				<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">					
					<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Lista Soggetti</tr>
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice fiscale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data di nascita</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune di nascita</span></td>						
					</tr>
					<% it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniPersona soggetto = new it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniPersona(); %>
				  	<% for (int i = 0; i < listaSoggetti.size(); i++) {
				        soggetto = (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniPersona)listaSoggetti.get(i);%>
				    	<tr>
							<td class="extWinTDData">
								<span class="extWinTXTData" style="padding-left: 3px;"><%= soggetto.getTitolo() %></span>
							</td>
							<td class="extWinTDData">
								<span class="extWinTXTData" style="padding-left: 3px;"><%= soggetto.getCodiceFiscale() %></span>
							</td>
							<td class="extWinTDData">
								<span class="extWinTXTData" style="padding-left: 3px;"><%= soggetto.getCognome() %></span>
							</td>
							<td class="extWinTDData">
								<span class="extWinTXTData" style="padding-left: 3px;"><%= soggetto.getNome() %></span>
							</td>
							<td class="extWinTDData">
								<span class="extWinTXTData" style="padding-left: 3px;"><%= soggetto.getDenominazione() %></span>
							</td>
							<td class="extWinTDData">
								<span class="extWinTXTData" style="padding-left: 3px;"><%= soggetto.getDataNascita() %></span>
							</td>
							<td class="extWinTDData">
								<span class="extWinTXTData" style="padding-left: 3px;"><%= soggetto.getComuneNascita() %></span>
							</td>
						</tr>
					<% 
						} 
					%>
				</table>
				</div>				
			<%}%>
			
			<% boolean listaUnica = false; %>
			
			<!-- visualizzazione con dati catastali raggruppati per indirizzo -->
			<% if (false) {%> <!-- visualizzazione non attivata -->
				<% listaUnica = listaIndirizzi != null && listaIndirizzi.size() > 0 && 
				((it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo)listaIndirizzi.get(0)).getDatiCatastali() != null &&
				((it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo)listaIndirizzi.get(0)).getDatiCatastali().size() > 0; %>
				<% if (!listaUnica) {%>
					<% if (listaDatiCatastali != null && listaDatiCatastali.size() > 0) {%>
						
						<div class="tabbertab">
						
						<h2>Lista Dati Catastali</h2>
						
						<% if (listaUnica) { %>	
							<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">
						<% } else { %>
							<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">
						<% } %>	
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle">Lista Dati Catastali</tr>										
							<tr>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
								<% if (request.getParameter("popup") == null || !(new Boolean(request.getParameter("popup")).booleanValue())) { %>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Docfa</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Verifica Catasto</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Cessata a catasto</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Concessioni collegate</span></td>
								<%	} %>
							</tr>
							<% it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto dato = new it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto(); %>
						  	<% for (int i = 0; i < listaDatiCatastali.size(); i++) {
						        dato = (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto)listaDatiCatastali.get(i);%>
						    	<tr>
									<td class="extWinTDData">
										<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getFoglio() %></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getParticella() %></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getSubalterno() %></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getTipo() %></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getSezione() %></span>
									</td>
									<% if (request.getParameter("popup") == null || !(new Boolean(request.getParameter("popup")).booleanValue())) { %>
										<td class="extWinTDData" style="text-align:center;">
											<span class="extWinTXTData">
											<% if (dato.isDocfa()) { %>
												<img src="../images/ok.gif" style="cursor: pointer;" 
												onclick="vaiAListaDocfa('<%=dato.getFoglio().replace("/",  " ").replace(",",  " ").replace("-",  " ")%>',
																		'<%=dato.getParticella().replace("/",  " ").replace(",",  " ").replace("-",  " ")%>',
																		'<%=dato.getSubalterno().replace("/",  " ").replace(",",  " ").replace("-",  " ")%>')"
												alt="DOCFA presenti"
												/>
											<% } else {%>
												<img src="../images/no.gif" alt="DOCFA non presenti" />
											<% }%>
											</span>
										</td>
										<td class="extWinTDData" style="text-align:center;">																					
											<% String foglio = dato.getFoglio();
											if (foglio.equals("-")) {
												foglio = "";
											}
											while(foglio.length() < 4) {
												foglio = "0" + foglio;
											}
											String particella = dato.getParticella();
											if (particella.equals("-")) {
												particella = "";
											}
											while(particella.length() < 5) {
												particella = "0" + particella;
											}
											String subalterno = dato.getSubalterno();
											if (subalterno.equals("-")) {
												subalterno = "";
											}
											while(subalterno.length() < 4) {
												subalterno = "0" + subalterno;
											} %>										
											<% if (dato.getAssenzaCatasto() != null && !dato.getAssenzaCatasto().equals("")){%>
												<img src="<%=request.getContextPath()%>/images/no.gif" border="0" height="18" width="18" alt="<%=dato.getAssenzaCatasto()%>" />		
											<% }else{%>
												<a href="javascript:apriPopupCatasto('<%= foglio %>','<%= particella %>','<%= subalterno %>','<%= dato.getDataValiditaCatasto() %>','<%=dato.getCodEnt()%>');">
													<img  src="<%=request.getContextPath()%>/images/ok.gif" border="0"  alt="particella presente" />
												</a>		
											<% }%>
										</td>
										<td class="extWinTDData" style="text-align:center;">
											<% if (dato.isCessataCatasto()){%>												
												<img src="<%=request.getContextPath()%>/images/ok.gif" border="0" alt="<%=subalterno.equals("0000")? "particella cessata" : "subalterno cessato"%> a catasto" />		
											<% }else{%>
												<img src="<%=request.getContextPath()%>/images/no.gif" border="0" height="18" width="18" alt="Non Cessato" />		
											<% }%>
										</td>
										<td class="extWinTDData" style="text-align:center;">
											<% if (dato.isConcCollegate()){%>											
												<a href="javascript:apriPopupConcCollegate('<%= con.getId() %>','<%= foglio %>','<%= particella %>','<%= subalterno %>','<%=dato.getCodEnt()%>');">
													<img src="<%=request.getContextPath()%>/images/ok.gif" border="0" alt="concessioni collegate" />
												</a>		
											<% }else{%>
												<img src="<%=request.getContextPath()%>/images/no.gif" border="0" height="18" width="18" alt="Nessuna Concessione Collegata" />		
											<% }%>
										</td>
									<%	} %>									
								</tr>
							<% 
								} 
							%>
						</table>	
						</div>			
					<%}%>
					
				<%}%>
				
				<% if (listaIndirizzi != null && listaIndirizzi.size() > 0) {%>
					<div class="tabbertab">
						
						<h2>Lista Indirizzi</h2>
						
					<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">			
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle">Lista Indirizzi</tr>
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo Catastale</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo Viario</span></td>			
						</tr>
						<% it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo indirizzo = new it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo(); %>
					  	<% for (int i = 0; i < listaIndirizzi.size(); i++) {
					        indirizzo = (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo)listaIndirizzi.get(i);%>
					    	<tr>
								<td class="extWinTDData">
									<p><span class="extWinTXTData" style="padding-left: 3px;"><%= indirizzo.getIndirizzoCompleto() %></span></p>
								</td>
								<td class="extWinTDData">
									<p><span class="extWinTXTData" style="padding-left: 3px;"><%= indirizzo.getIndirizzoCatastale() %></span></p>
								</td>
								<td class="extWinTDData">
									<p><span class="extWinTXTData" style="padding-left: 3px;"><%= indirizzo.getIndirizzoViarioRif() %></span></p>
								</td>
							</tr>
							<% if (listaUnica) {
									int conta = 0;
									if (indirizzo.getDatiCatastali().size() > 0) { %>
										<tr>
											<td style="margin: 0px; padding: 0px; text-align: center; background-color: #ffffff; 
											border-bottom-style: solid; border-bottom-width: 1px; border-bottom-color: #a1a1a1;">
												<table class="extWinTable" style="width: 95%; margin: 0px 5px 20px 5px;" 
												cellpadding="0" cellspacing="0">
													<tr>
														<td colspan="6" style="background-color: #ffffff;">
															<div style="text-align: center;">
																<span class="TXTmainLabel">Dati Catastali associati</span>
															</div>
														</td>
													</tr>
													<tr>
														<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
														<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
														<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
														<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
														<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
														<% if (request.getParameter("popup") == null || !(new Boolean(request.getParameter("popup")).booleanValue())) { %>
															<td class="extWinTDTitle"><span class="extWinTXTTitle">Docfa</span></td>
															<td class="extWinTDTitle"><span class="extWinTXTTitle">Verifica Catasto</span></td>				
														<%	} %>														
													</tr>
													<% for (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto dato : indirizzo.getDatiCatastali()) {%>
														<tr>
															<td class="extWinTDData">
																<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getFoglio() %></span>
															</td>
															<td class="extWinTDData">
																<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getParticella() %></span>
															</td>
															<td class="extWinTDData">
																<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getSubalterno() %></span>
															</td>
															<td class="extWinTDData">
																<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getTipo() %></span>
															</td>
															<td class="extWinTDData">
																<span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getSezione() %></span>
															</td>
															<% if (request.getParameter("popup") == null || !(new Boolean(request.getParameter("popup")).booleanValue())) { %>
																<td class="extWinTDData" style="text-align:center;">
																	<span class="extWinTXTData">
																	<% if (dato.isDocfa()) { %>
																		<img src="../images/ok.gif" style="cursor: pointer;" 
																		onclick="vaiAListaDocfa('<%=dato.getFoglio().replace("/",  " ").replace(",",  " ").replace("-",  " ")%>',
																								'<%=dato.getParticella().replace("/",  " ").replace(",",  " ").replace("-",  " ")%>',
																								'<%=dato.getSubalterno().replace("/",  " ").replace(",",  " ").replace("-",  " ")%>')"
																		alt="DOCFA presenti"
																		/>
																	<% } else {%>
																		<img src="../images/no.gif" alt="DOCFA non presenti" />
																	<% }%>
																	</span>
																</td>
																<td class="extWinTDData" style="text-align:center;">
																	<% String foglio = dato.getFoglio();
																	if (foglio.equals("-")) {
																		foglio = "";
																	}
																	while(foglio.length() < 4) {
																		foglio = "0" + foglio;
																	}
																	String particella = dato.getParticella();
																	if (particella.equals("-")) {
																		particella = "";
																	}
																	while(particella.length() < 5) {
																		particella = "0" + particella;
																	}
																	String subalterno = dato.getSubalterno();
																	if (subalterno.equals("-")) {
																		subalterno = "";
																	}
																	while(subalterno.length() < 4) {
																		subalterno = "0" + subalterno;
																	} %>
																	<% if (dato.getAssenzaCatasto() != null && !dato.getAssenzaCatasto().equals("")){%>
																		<img src="<%=request.getContextPath()%>/images/no.gif" border="0" height="18" width="18" alt="<%=dato.getAssenzaCatasto()%>" />		
																	<% }else{%>
																		<a href="javascript:apriPopupCatasto('<%= foglio %>','<%= particella %>','<%= subalterno %>','<%= dato.getDataValiditaCatasto() %>','<%=dato.getCodEnt()%>');">
																			<img  src="<%=request.getContextPath()%>/images/ok.gif" border="0"  alt="particella presente" />
																		</a>		
																	<% }%>
																</td>
															<%	} %>															
														</tr>
											<%		} %>
											</table>
										</td>
									</tr>
								<% 	}
								}%>
						<% 	} %>
					</table>	
					</div>			
				<%}%>
			<% }%>
			
			<!-- fine visualizzazione con dati catastali raggruppati per indirizzo -->
			
			<!-- visualizzazione con indirizzi associati ai dati catastali -->
			<% if (true) {%> <!-- visualizzazione attivata -->
				<% listaUnica = listaDatiCatastali != null && listaDatiCatastali.size() > 0 && 
				((it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto)listaDatiCatastali.get(0)).getIndirizzo().getId() != null; %>
				<% if (listaDatiCatastali != null && listaDatiCatastali.size() > 0) {%>
					<div class="tabbertab">
						
						<h2>Lista Dati Catastali</h2>
					<% if (listaUnica) { %>	
						<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">
					<% } else { %>
						<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">
					<% } %>	
					<tr>&nbsp;</tr>
					<tr class="extWinTXTTitle">Lista Dati Catastali</tr>		
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
							<% if (listaUnica) { %>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo Catastale</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo Viario</span></td>	
							<%	} %>
							<% if (request.getParameter("popup") == null || !(new Boolean(request.getParameter("popup")).booleanValue())) { %>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Docfa</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Verifica Catasto</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Cessata a catasto</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Concessioni collegate</span></td>
							<%	} %>											
						</tr>
						<% it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto dato = new it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto(); %>
					  	<% for (int i = 0; i < listaDatiCatastali.size(); i++) {
					        dato = (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto)listaDatiCatastali.get(i);%>
					    	<tr>
								<td class="extWinTDData">
									<p><span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getFoglio() %></span></p>
								</td>
								<td class="extWinTDData">
									<p><span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getParticella() %></span></p>
								</td>
								<td class="extWinTDData">
									<p><span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getSubalterno() %></span></p>
								</td>
								<td class="extWinTDData">
									<p><span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getTipo() %></span></p>
								</td>
								<td class="extWinTDData">
									<p><span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getSezione() %></span></p>
								</td>
								<% if (listaUnica) { %>
									<td class="extWinTDData">
										<p><span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getIndirizzo().getIndirizzoCompleto() %></span></p>
									</td>
									<td class="extWinTDData">
										<p><span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getIndirizzo().getIndirizzoCatastale() %></span></p>
									</td>
									<td class="extWinTDData">
										<p><span class="extWinTXTData" style="padding-left: 3px;"><%= dato.getIndirizzo().getIndirizzoViarioRif() %></span></p>
									</td>								
								<%	} %>
								<% if (request.getParameter("popup") == null || !(new Boolean(request.getParameter("popup")).booleanValue())) { %>
									<td class="extWinTDData" style="text-align:center;">
										<span class="extWinTXTData">
										<% if (dato.isDocfa()) { %>
											<img src="../images/ok.gif" style="cursor: pointer;" 
											onclick="vaiAListaDocfa('<%=dato.getFoglio().replace("/",  " ").replace(",",  " ").replace("-",  " ")%>',
																	'<%=dato.getParticella().replace("/",  " ").replace(",",  " ").replace("-",  " ")%>',
																	'<%=dato.getSubalterno().replace("/",  " ").replace(",",  " ").replace("-",  " ")%>')"
											alt="DOCFA presenti"
											/>
										<% } else {%>
											<img src="../images/no.gif" alt="DOCFA non presenti" />
										<% }%>
										</span>
									</td>
									<td class="extWinTDData" style="text-align:center;">
										<% String foglio = dato.getFoglio();
										if (foglio.equals("-")) {
											foglio = "";
										}
										while(foglio.length() < 4) {
											foglio = "0" + foglio;
										}
										String particella = dato.getParticella();
										if (particella.equals("-")) {
											particella = "";
										}
										while(particella.length() < 5) {
											particella = "0" + particella;
										}
										String subalterno = dato.getSubalterno();
										if (subalterno.equals("-")) {
											subalterno = "";
										}
										while(subalterno.length() < 4) {
											subalterno = "0" + subalterno;
										} %>
										<% if (dato.getAssenzaCatasto() != null && !dato.getAssenzaCatasto().equals("")){%>
											<img src="<%=request.getContextPath()%>/images/no.gif" border="0" height="18" width="18" alt="<%=dato.getAssenzaCatasto()%>" />		
										<% }else{%>
											<a href="javascript:apriPopupCatasto('<%= foglio %>','<%= particella %>','<%= subalterno %>','<%= dato.getDataValiditaCatasto() %>','<%=dato.getCodEnt()%>');">
												<img  src="<%=request.getContextPath()%>/images/ok.gif" border="0"  alt="particella presente" />
											</a>		
										<% }%>
									</td>
									<td class="extWinTDData" style="text-align:center;">
										<% if (dato.isCessataCatasto()){%>												
											<img src="<%=request.getContextPath()%>/images/ok.gif" border="0" alt="<%=subalterno.equals("0000")? "particella cessata" : "subalterno cessato"%> a catasto" />
										<% }else{%>
											<img src="<%=request.getContextPath()%>/images/no.gif" border="0" height="18" width="18" alt="Non Cessato" />		
										<% }%>
									</td>
									<td class="extWinTDData" style="text-align:center;">
										<% if (dato.isConcCollegate()){%>												
											<a href="javascript:apriPopupConcCollegate('<%= con.getId() %>','<%= foglio %>','<%= particella %>','<%= subalterno %>','<%=dato.getCodEnt()%>');">
												<img src="<%=request.getContextPath()%>/images/ok.gif" border="0" alt="concessioni collegate" />
											</a>		
										<% }else{%>
											<img src="<%=request.getContextPath()%>/images/no.gif" border="0" height="18" width="18" alt="Nessuna Concessione Collegata" />		
										<% }%>
									</td>
								<%	} %>								
							</tr>
						<%	} %>
					</table>
					</div>				
				<%}%>
				
				<% if (!listaUnica) {%>
					<% if (listaIndirizzi != null && listaIndirizzi.size() > 0) {%>
						<div class="tabbertab">
						
						<h2>Lista Indirizzi</h2>
						<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">			
							<tr>&nbsp;</tr>
							<tr class="extWinTXTTitle">Lista Indirizzi</tr>
							<tr>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo Catastale</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo Viario</span></td>						
							</tr>
							<% it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo indirizzo = new it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo(); %>
						  	<% for (int i = 0; i < listaIndirizzi.size(); i++) {
						        indirizzo = (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo)listaIndirizzi.get(i);%>
						    	<tr>
									<td class="extWinTDData">
										<p><span class="extWinTXTData" style="padding-left: 3px;"><%= indirizzo.getIndirizzoCompleto() %></span></p>
									</td>
									<td class="extWinTDData">
										<p><span class="extWinTXTData" style="padding-left: 3px;"><%= indirizzo.getIndirizzoCatastale() %></span></p>
									</td>
									<td class="extWinTDData">
										<p><span class="extWinTXTData" style="padding-left: 3px;"><%= indirizzo.getIndirizzoViarioRif() %></span></p>
									</td>
								</tr>							
							<% 	} %>
						</table>
					</div>				
					<%}%>
				<%}%>			
			<% }%>
			<!-- fine visualizzazione con indirizzi associati ai dati catastali -->
			
			<% HashMap<String,String> m = con.getTiff(); 
			if (m != null && m.size() > 0) {
			%>	
				
				<div class="tabbertab">
						
				<h2>Documenti</h2>
				<table align=left class="extWinTable" style="width: 40%;">
				<tr>&nbsp;</tr>
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Documenti</span></td>
					</tr>
				<%
					for (String key : m.keySet())			
					{ %>
						<tr>
							<td class="extWinTDData">
								<span class="extWinTXTData">
									<%=m.get(key)%>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a target="_new" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/StoricoConcessioni?ST=99&img=<%=key%>">PDF</a>
									&nbsp;
									<a target="_new" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/StoricoConcessioni?ST=99&openJpg=true&img=<%=key%>">IMG</a>
								</span>
							</td>
						</tr>
					<% } %>
				</table>
				</div>
			<% } %>				
			
			</div>
			
			<% if (request.getParameter("popup") != null && new Boolean(request.getParameter("popup")).booleanValue()) { %>
				&nbsp;
				<div align="center"><span class="extWinTXTTitle">
					<%  String href = null; 
					if (request.getParameter("collegate") != null && new Boolean(request.getParameter("collegate")).booleanValue()) { %>
						<% href = it.escsolution.escwebgis.common.EscServlet.URL_PATH + "/StoricoConcessioni?ST=2&popup=true&collegate=true" 
								+ "&conId=" + request.getParameter("conId") + "&fg=" + request.getParameter("fg") + "&part=" + request.getParameter("part") 
								+ "&sub=" + request.getParameter("sub") + "&cod_ente=" + request.getParameter("cod_ente"); %>
					<% } else { %>
						<% href = it.escsolution.escwebgis.common.EscServlet.URL_PATH + "/StoricoConcessioni?ST=2&popup=true&fg=" 
								+ request.getParameter("fg") + "&part=" + request.getParameter("part") 
								+ "&sub=" + request.getParameter("sub"); %>						
					<% } %>
					<a class="iFrameLink" href="<%= href %>">torna alla lista</a></span>
				</div>
			<% } %>
		
			<% if (con != null){%>
				<% String id = con.getId();%>
				   <input type='hidden' name="OGGETTO_SEL" value="<%=id%>">
			<%}%>
				
			<% if(finder != null){%>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<% }else{%>
				<input type='hidden' name="ACT_PAGE" value="">
			<% }%>
		
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="53">
			<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		</form>
	
		<div id="wait" class="cursorWait" />
		
	</body>
</html>