<%@page import="java.text.SimpleDateFormat"%>
<%@page import="it.escsolution.escwebgis.bolli.bean.BolliVeicoliFinder"%>
<%@page import="it.escsolution.escwebgis.bolli.logic.BolliVeicoliLogic"%>
<%@page import="it.webred.ct.data.model.bolliVeicoli.BolloVeicolo"%>
<%@ page language="java" import="it.escsolution.escwebgis.bolli.bean.*,java.util.ArrayList,java.util.Iterator"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  BolloVeicolo bv = (BolloVeicolo)sessione.getAttribute(BolliVeicoliLogic.BOLLI_VEICOLI); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  BolliVeicoliFinder finder = null;

if (sessione.getAttribute(BolliVeicoliLogic.FINDER) != null){ 
	finder = (BolliVeicoliFinder)sessione.getAttribute(BolliVeicoliLogic.FINDER);
}%>

<%int js_back = 1;
if (sessione.getAttribute("BACK_JS_COUNT")!= null)
	js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();


java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% 
java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); 
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>

<html>
	<head>
		<title>Bollo Veicolo - Dettaglio</title>
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
				&nbsp;<%=funzionalita%>:&nbsp;Dati Bollo Veicolo</span>
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
	
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/BolliVeicoli" target="_parent">
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Dati Bollo Veicolo</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Targa</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getTarga()!=null?bv.getTarga():"-"%></span>
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Kw</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getKw()!=null?bv.getKw():"-"%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Cilindrata</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getCilindrata()!=null?bv.getCilindrata():"-"%></span>	
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Data Prima Immatricolazione</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%
														out.print( bv.getDtPrimaImmatricolazione()!=null?sdf.format( bv.getDtPrimaImmatricolazione() ):"-" );
														%></span>	
											</td>			
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Data Inizio Proprieta</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%
														out.print( bv.getDataInizioProprieta()!=null?sdf.format( bv.getDataInizioProprieta() ):"-" );
													%></span>
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Data Fine Proprieta</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%
														out.print( bv.getDataFineProprieta()!=null?sdf.format( bv.getDataFineProprieta() ):"-" );
													%></span>	
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" colspan="6">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Cognome </span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getCognome()!=null?bv.getCognome():"-"%></span>
											</td>			
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Nome</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getNome()!=null?bv.getNome():"-"%></span>
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Ragione Sociale</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getRagioneSociale()!=null?bv.getRagioneSociale():"-"%></span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Cod. Fiscale / P. Iva</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getCodiceFiscalePiva()!=null?bv.getCodiceFiscalePiva():"-"%></span>
											</td>			
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Tipo Soggetto</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getTipoSoggetto()!=null?bv.getTipoSoggetto():"-"%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Sesso</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getCodiceSesso()!=null?bv.getCodiceSesso():"-"%></span>	
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Data Nascita</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%
														out.print( bv.getDataNascita()!=null?sdf.format( bv.getDataNascita() ):"-" );
													%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Comune Nascita</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getComuneNascita()!=null?bv.getComuneNascita():"-"%></span>	
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Provincia Nascita</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getProvinciaNascita()!=null?bv.getProvinciaNascita():"-"%></span>
											</td>
										</tr>										
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Indirizzo</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getIndirizzo()!=null?bv.getIndirizzo():"-"%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">N. Civico</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getNumeroCivico()!=null?bv.getNumeroCivico():"-"%></span>	
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">CAP</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getCap()!=null?bv.getCap():"-"%></span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Comune Residenza</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getComuneResidenza()!=null?bv.getComuneResidenza():"-"%></span>
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Provincia Residenza</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getProvinciaResidenza()!=null?bv.getProvinciaResidenza():"-"%></span>	
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Regione Residenza</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getRegioneResidenza()!=null?bv.getRegioneResidenza():"-"%></span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">ISTAT Comune Residenza</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getComuneIstatC()!=null?bv.getComuneIstatC():"-"%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">ISTAT Provincia Residenza</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getProvinciaIstatP()!=null?bv.getProvinciaIstatP():"-"%></span>	
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox">&nbsp;</span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Esenzione</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getEsenzione()!=null?bv.getEsenzione():"-"%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Stato Esenzione</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getStatoEsenzione()!=null?bv.getStatoEsenzione():"-"%></span>	
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipo Handicap</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getTipoHandicap()!=null?bv.getTipoHandicap():"-"%></span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Data Inizio Esenzione</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><% 
													out.print( bv.getDataInizioEsenzione()!=null?sdf.format( bv.getDataInizioEsenzione() ):"-" ); 
												%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Data Fine Esenzione</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%
														if ( bv.getDataFineEsenzione()!=null)
															out.print(sdf.format( bv.getDataFineEsenzione() ));
														else
															out.print("-"); 
												%></span>	
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox">&nbsp;</span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" colspan="6">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Potenza Fiscale</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getPotenzaFiscale()!=null?bv.getPotenzaFiscale():"-"%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Codice Carrozzeria</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getCodiceCarrozzeria()!=null?bv.getCodiceCarrozzeria():"-"%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Codice Telaio</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getCodiceTelaio()!=null?bv.getCodiceTelaio():"-"%></span>	
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Alimentazione</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getAlimentazione()!=null?bv.getAlimentazione():"-"%></span>
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Codice Euro</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getCodSiglaEuro()!=null?bv.getCodSiglaEuro():"-"%></span>
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Emissioni CO2</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getEmissioniCo2()!=null?bv.getEmissioniCo2():"-"%></span>	
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Tipo Alimentazione Impianto</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getTipoAlimentazioneImpianto()!=null?bv.getTipoAlimentazioneImpianto():"-"%></span>	
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Uso</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getUso()!=null?bv.getUso():"-"%></span>
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Destinazione</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getDestinazione()!=null?bv.getDestinazione():"-"%></span>	
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Numero Assi</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getNumeroAssi()!=null?bv.getNumeroAssi():"-"%></span>
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Numero Posti</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getNumeroPosti()!=null?bv.getNumeroPosti():"-"%></span>
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Peso Complessivo</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getPesoComplessivo()!=null?bv.getPesoComplessivo():"-"%></span>	
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Massa Rimorchiabile</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getMassaRimorchiabile()!=null?bv.getMassaRimorchiabile():"-"%></span>	
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Flag Ann. Massa Rimorchiabile</span>
											</td>
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=bv.getFlagAnnMassaRimorc()!=null?bv.getFlagAnnMassaRimorc():"-"%></span>
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">&nbsp;</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox">&nbsp;</span>	
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Portata</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getPortata()!=null?bv.getPortata():"-"%></span>	
											</td>
											<td class="TDmainLabel">
												<span class="TXTmainLabel">Sospensione Pneumatica</span>
											</td>
											<td class="TDviewTextBox">
												<span class="TXTviewTextBox"><%=bv.getSospensionePneumatica()!=null?bv.getSospensionePneumatica():"-"%></span>	
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
			<input type='hidden' name="UC" value="137">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
	
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>