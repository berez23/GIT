<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@ page language="java" import="it.escsolution.escwebgis.indagineCivico.bean.*, it.escsolution.escwebgis.indagineCivico.logic.*"%>
<%  HttpSession sessione = request.getSession(true);  %> 
<%  Indirizzo indSel = (Indirizzo) sessione.getAttribute(IndagineCivicoLogic.INDIRIZZO); %>
<%  java.util.ArrayList<Anagrafe> listaResidenti = (java.util.ArrayList<Anagrafe>) sessione.getAttribute(IndagineCivicoLogic.LISTA_RESIDENTI); %>
<%  java.util.ArrayList<Catasto> listaUIConTitolari = (java.util.ArrayList<Catasto>) sessione.getAttribute(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE); %>
<%  java.util.ArrayList<ConsistenzaUI> listaUIConsistenza = (java.util.ArrayList<ConsistenzaUI>) sessione.getAttribute(IndagineCivicoLogic.LISTA_UI_CONSISTENZA); %>
<%  java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	int st = new Integer(ST).intValue(); %>
<%  it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  IndagineCivicoFinder finder = null;

	if (sessione.getAttribute(IndagineCivicoLogic.FINDER) !=null){
		if (((Object)sessione.getAttribute(IndagineCivicoLogic.FINDER)).getClass() == new IndagineCivicoFinder().getClass()){
			finder = (IndagineCivicoFinder)sessione.getAttribute(IndagineCivicoLogic.FINDER);
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

<% EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null); %>
	
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
<html>
	<head>
		<title>Indagine civico - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	<script>	
			
		function mettiST() {
			document.mainform.ST.value = 3;
		}
		
		function exportXls() {
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/IndagineCivico?popup=true';
			var finestra=window.open(url,"","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
			
		}
		function apriPopupCens(codice,im,f,m,s){
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/IndagineCivico?popupCens=true&codice='+codice+'&im='+im+'&f='+f+'&m='+m+'&s='+s;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
		}
		
	</script>
	<body >
		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/IndagineCivico" target="_parent">
		
				
			<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
				Fascicolo Per Indirizzo</span>
			</div>
			&nbsp;
			<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center;">
						&nbsp;
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 90%;">
							<tr>
								<td>
									<table class="viewExtTable" style="width: 100%">
										<tr>
											<td class="TDmainLabel" style="width: 20%;"><span class="TXTmainLabel">Indirizzo</span></td>
											<td class="TDviewTextBox" style="width: 50%;"><span class="TXTviewTextBox"><%=indSel.getIndirizzoCompleto()%></span></td>
											<td class="TDmainLabel" style="width: 18%;"><span class="TXTmainLabel">Codice strada</span></td>
											<td class="TDviewTextBox" style="width: 12%;"><span class="TXTviewTextBox"><%=indSel.getPkidStra()%></span></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						
					</td>
				</tr>
			</table>
			<br>
			<br>
			<!-- esportazione excel. Qui fatta con pulsante
			<div align="right" ><span>
				<input type="button" class="tdButton" value="Excel"  onClick="exportXls();" /></span>
			</div>
		-->
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
			<tr>
				<td colspan=1>
				<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="4" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;">
							<a href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/IndagineCivico?popup=true">
							<span class="TXTmainLabel"> 
								<img height="30px" border="0" src="<%=request.getContextPath()%>/images/xlsIcon.jpg"/>
								Esportazione dati in formato Excel
							</span>
							</a>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
			
<div class="tabber">

			<% if (GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_RES, true)) {
				if (listaResidenti != null && listaResidenti.size() > 0) {%>
					<div class="tabbertab">
						<h2>Lista Famiglie Residenti</h2>
						
					<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">					
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle">
								Lista Famiglie Residenti
						</tr>
						<tr>
							<td class="extWinTDTitle" style="width: 12%;"><span class="extWinTXTTitle">CODICE FAMIGLIA<span class="extWinTXTTitle"></td>
							<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">TIPO PAR.<span class="extWinTXTTitle"></td>
							<td class="extWinTDTitle" style="width: 8%;"><span class="extWinTXTTitle">COD.IND. ANAG.<span class="extWinTXTTitle"></td>
							<td class="extWinTDTitle" style="width: 16%;"><span class="extWinTXTTitle">COGNOME</span></td>
							<td class="extWinTDTitle" style="width: 13%;"><span class="extWinTXTTitle">NOME</span></td>
							<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
							<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">DATA NASCITA</span></td>
							<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">COMUNE NASCITA</span></td>
							<td class="extWinTDTitle" style="width: 5%;"> <span class="extWinTXTTitle">CITT.</span></td>									
						</tr>
						<% 	String[] colori = new String[]{"#E8F0FF", "#D2D2D2"};
				            int indColore = 0;
				            String ultimaFamiglia=listaResidenti.get(0).getFamiglia();%>
						<% for (Anagrafe datiAnag: listaResidenti ) {
				               String famiglia=  datiAnag.getFamiglia();
				                 if (!famiglia.equals(ultimaFamiglia)) {
				                	indColore++; 
				                    if (indColore > 1){
				     					indColore = 0;
				     				}
				                    ultimaFamiglia= famiglia;
				                 }
				        %> 
						    	<tr>
								<td class="extWinTDData" style='background-color:<%=colori[indColore]%>'>
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiAnag.getFamiglia() %></span>
								</td>
								<td class="extWinTDData" style='background-color:<%=colori[indColore]%>'>
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiAnag.getTipoParentela() %></span>
								</td>
								<td class="extWinTDData" style='background-color:<%=colori[indColore]%>'>
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiAnag.getCodAnagrafe()%></span>
								</td>
								<td class="extWinTDData" style='background-color:<%=colori[indColore]%>'>
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiAnag.getCognome() %></span>
								</td>
								<td class="extWinTDData" style='background-color:<%=colori[indColore]%>' >
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiAnag.getNome() %></span>
								</td>
								<td class="extWinTDData" style='background-color:<%=colori[indColore]%>'>
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiAnag.getCodFiscale() %></span>
								</td>
								<td class="extWinTDData" style='background-color:<%=colori[indColore]%>'>
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiAnag.getDataNascita() %></span>
								</td>
								<td class="extWinTDData" style='background-color:<%=colori[indColore]%>'>
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiAnag.getComuneNascita() %></span>
								</td>
								<td class="extWinTDDataCen" style='background-color:<%=colori[indColore]%>'>
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiAnag.getCittadinanza() %></span>
								</td>
							</tr>
						<% 
							} 
						%>
					</table>	
					</div>			
				<%}
			}%>
			
			
			
			<% if (GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_UI_TIT, true)) {
				if (listaUIConTitolari != null && listaUIConTitolari.size() > 0) {%>
				   
				   <div class="tabbertab">
						<h2>Dati censuari U.I. e relativi titolari</h2>
					
					<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">					
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle">
								Dati censuari U.I. e relativi titolari
						</tr>
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGL.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">PART.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">SUB</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">CAT.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">CLAS.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">REND.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">DENOMINAZIONE</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">TITOLO</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO CATASTALE</span></td>												
						</tr>
						<% for (Catasto datiCat: listaUIConTitolari) {%>
						    	<tr>
								<td class="extWinTDDataCen">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCat.getFoglio()%></span>
								</td>
								<td class="extWinTDDataCen">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCat.getMappale()%></span>
								</td>
								<td class="extWinTDDataCen">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCat.getSub() %></span>
								</td>
								<td class="extWinTDDataCen">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCat.getCategoria() %></span>
								</td>
								<td class="extWinTDDataCen">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCat.getClasse() %></span>
								</td>
								<td class="extWinTDDataDx">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= IndagineCivicoLogic.DF.format(datiCat.getRendita()) %></span>
								</td>
								<td class="extWinTDData">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCat.getListaCodFiscTitolari() %></span>
								</td>
								<td class="extWinTDData">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCat.getListaDenomTitolari() %></span>
								</td>
								<td class="extWinTDData">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCat.getListaTipoTitoloTitolari() %></span>
								</td>
								<td class="extWinTDData">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCat.getIndirizzo().getIndirizzoCompleto() %></span>
								</td>
							</tr>
						<% 
							} 
						%>
					</table>	
					</div>			
				<%}
			}%>
			<!-- fine lista dati censuari -->
			
			<% if (GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_UI_CONS, true)) {
				if (listaUIConsistenza != null && listaUIConsistenza.size() > 0) {%>
				   
					<div class="tabbertab">
						<h2>Dati consistenza per U.I.</h2>
						
					<table align="left" class="extWinTable" style="width: 100%;" cellspacing="0" cellpadding="0">					
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle">
								Dati consistenza per U.I.
						</tr>
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">PART.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">SUB</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">SUP.DIC.<br>TARSU</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE<br>TARSU</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO OGGETTO<br>TARSU</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">C.F. CONTRIB. TARSU</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">DENOMIN. CONTRIB. TARSU</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">CONS.<br>CAT.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">SUP.<br>CAT.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">SUP.<br>C340</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">DOCFA</span></td>
							
						</tr>
						<% for (ConsistenzaUI datiCons: listaUIConsistenza) {%>
						    	<tr>
								<td class="extWinTDDataCen">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCons.getFoglio()%></span>
								</td>
								<td class="extWinTDDataCen">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCons.getMappale()%></span>
								</td>
								<td class="extWinTDDataCen">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCons.getSub() %></span>
								</td>
								<td class="extWinTDDataDx">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCons.getSupTarsuNonPres() != null ? datiCons.getSupTarsuNonPres() : IndagineCivicoLogic.DF.format(datiCons.getSupTarsu()) %></span>
								</td>
								<td class="extWinTDDataDx">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCons.getDicTarsuDesClsRsu() %></span>
								</td>
								<td class="extWinTDDataDx">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCons.getDicTarsuDesTipOgg() %></span>
								</td>
								<td class="extWinTDData">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCons.getDicTarsuCFPi() %></span>
								</td>
								<td class="extWinTDData">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= datiCons.getDicTarsuDenominazione() %></span>
								</td>
								<td class="extWinTDDataDx">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= IndagineCivicoLogic.DF.format(datiCons.getConsistenza()) %></span>
								</td>
								<td class="extWinTDDataDx">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= IndagineCivicoLogic.DF.format(datiCons.getSuperficie()) %></span>
								</td>
								<td class="extWinTDDataDx">
									<span class="extWinTXTData" style="padding-left: 3px;"><%= IndagineCivicoLogic.DF.format(datiCons.getSupC340()) %></span>
								</td>
								<td class="extWinTDDataCen">
									<span class="extWinTXTData" style="padding-left: 3px;"> <a href="javascript:apriPopupCens('<%=datiCons.getDatiDocfa().getChiaveDocfa()%>','<%=datiCons.getDatiDocfa().getIdImmobile()%>','<%=datiCons.getFoglio()%>','<%=datiCons.getMappale()%>','<%=datiCons.getSub()%>');"><%= datiCons.getInfoDocfa() %></a></span>
								</td>
							</tr>
						<% 
							} 
						%>
					</table>
					</div>				
				<%}
			}%>
			
			</div>
			
			<!--  fine lista dati consistenza -->			
			<% if (indSel != null){%>
				<% String id = indSel.getChiave();%>
				   <input type='hidden' name="OGGETTO_SEL" value="<%=id%>">
			<%}%>
				
			<% if(finder != null){%>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<% }else{%>
				<input type='hidden' name="ACT_PAGE" value="">
			<% }%>
		
		    <input type='hidden' name="popup" value="false">
		
		
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="111">
			<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		</form>
	
		<div id="wait" class="cursorWait" />
		
	</body>
</html>