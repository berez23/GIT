<%@page import="it.escsolution.escwebgis.cened.bean.CertificazioniEnergeticheFinder"%>
<%@page import="it.escsolution.escwebgis.cened.logic.CertificazioniEnergeticheLogic"%>
<%@page import="it.webred.ct.data.model.datitecnicifabbricato.CertificazioneEnergetica"%>
<%@ page language="java" import="it.escsolution.escwebgis.tributi.bean.*,java.util.ArrayList,java.util.Iterator"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  CertificazioneEnergetica tes = (CertificazioneEnergetica)sessione.getAttribute(CertificazioniEnergeticheLogic.CENED); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  CertificazioniEnergeticheFinder finder = null;

if (sessione.getAttribute(CertificazioniEnergeticheLogic.FINDER) != null){ 
	finder = (CertificazioniEnergeticheFinder)sessione.getAttribute(CertificazioniEnergeticheLogic.FINDER);
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

<html>
	<head>
		<title>Certificazioni Energetiche - Dettaglio</title>
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
				&nbsp;<%=funzionalita%>:&nbsp;Dati Certificazione Energetica</span>
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
	
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CertificazioniEnergetiche" target="_parent">
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Dati Certificazione Energetica</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Codice Identificativo Pratica</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getCodiceIdentificativoPratica()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox">&nbsp;</span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Sezione</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getSezione()%></span>	
											</td>
											
	
										</tr>
										
										
										<tr>

											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Foglio </span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getFoglio()%></span>
											</td>			
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Particella</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getParticella()%></span>
											</td>

											<td class="TDmainLabel">
												<span class="TXTmainLabel">Subalterno</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getSubTutti()%></span>
											</td>

										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Data Prot</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getDataProtStr()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Num Prot</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getNumProt()!=null?tes.getNumProt():""%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox">&nbsp;</span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" colspan="6">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
										</tr>
										
										
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Certificatore Denominazione</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getDenomCertificatore()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Certificatore CF/P.IVA</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getCfPivaCertificatore()%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox">&nbsp;</span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Indirizzo</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getIndirizzo()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Comune</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getComune()%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Provincia</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getProvincia()%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" colspan="6">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Anno Costruzione</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getAnnoCostruzione()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Classe Energetica</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getClasseEnergetica()%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Destinazione di Uso</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getDestinazioneDiUso()%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Edificio Pubblico</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getEdificioPubblico()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Motivazione Ace</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getMotivazioneAce()%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Numero Ricambi Orari</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getNumeroRicambiOrari()%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Potenza Generatore</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getPotenzaGeneratore()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Efer</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getEfer()!=null?tes.getEfer():""%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Eghw</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getEghw()!=null?tes.getEghw():""%></span>	
											</td>
											
										</tr>
																				
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Ef Glob Media Acqua Calda San</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getEfGlobMediaAcquaCaldaSan()!=null?tes.getEfGlobMediaAcquaCaldaSan():""%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Ef Glob Media Riscaldamento</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getEfGlobMediaRiscaldamento()!=null?tes.getEfGlobMediaRiscaldamento():""%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Emissioni</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getEmissioni()!=null?tes.getEmissioni():""%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Eph</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getEph()!=null?tes.getEph():""%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Ept</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getEpt()!=null?tes.getEpt():""%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Epw</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getEpw()!=null?tes.getEpw():""%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Etc</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getEtc()!=null?tes.getEtc():""%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Eth</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getEth()!=null?tes.getEth():""%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox">&nbsp;</span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" colspan="6">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Superficie Apertura St</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getSuperficieAperturaSt()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Superficie Captante Fv</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getSuperficieCaptanteFv()%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Superficie Disperdente</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getSuperficieDisperdente()!=null?tes.getSuperficieDisperdente():""%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Superficie Lorda</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getSuperficieLorda()!=null?tes.getSuperficieLorda():""%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Superficie Netta</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getSuperficieNetta()!=null?tes.getSuperficieNetta():""%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Superficie Vetrata Opaca</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getSuperficieVetrataOpaca()!=null?tes.getSuperficieVetrataOpaca():""%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Sup Pan Fv Sup Utile</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getSupPanFvSupUtile()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Sup Pan St Sup Utile</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getSupPanStSupUtile()%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Tipologia Combustibile</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getTipologiaCombustibile()%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipologia Generatore</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getTipologiaGeneratore()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Tipologia Pannello Fv</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getTipologiaPannelloFv()%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Tipologia Pannello St</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getTipologiaPannelloSt()%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipologia Ventilazione</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getTipologiaVentilazione()%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Trasmittanza Media Basamento</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getTrasmittanzaMediaBasamento()!=null?tes.getTrasmittanzaMediaBasamento():""%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Trasmittanza Media Copertura</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getTrasmittanzaMediaCopertura()!=null?tes.getTrasmittanzaMediaCopertura():""%></span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Trasmittanza Media Involucro</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getTrasmittanzaMediaInvolucro()!=null?tes.getTrasmittanzaMediaInvolucro():""%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Trasmittanza Media Serramento</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getTrasmittanzaMediaSerramento()!=null?tes.getTrasmittanzaMediaSerramento():""%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox">&nbsp;</span>	
											</td>
											
										</tr>
										
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Volume Lordo</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=tes.getVolumeLordo()!=null?tes.getVolumeLordo():""%></span>
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Volume Netto</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=tes.getVolumeNetto()!=null?tes.getVolumeNetto():""%></span>	
											</td>
											
											<td class="TDmainLabel">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
											
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox">&nbsp;</span>	
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
			<input type='hidden' name="UC" value="135">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
	
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>