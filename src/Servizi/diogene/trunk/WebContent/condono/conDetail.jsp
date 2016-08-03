<%@ page language="java" import="it.escsolution.escwebgis.condono.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.condono.bean.Condono con=(it.escsolution.escwebgis.condono.bean.Condono)sessione.getAttribute(it.escsolution.escwebgis.condono.logic.CondonoLogic.CONDONO); %>
<%   ArrayList stralci = (ArrayList)sessione.getAttribute(it.escsolution.escwebgis.condono.logic.CondonoLogic.STRALCI); %>
<%   ArrayList uius = (ArrayList)sessione.getAttribute(it.escsolution.escwebgis.condono.logic.CondonoLogic.UIU); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  it.escsolution.escwebgis.condono.bean.CondonoFinder finder = null;
	if (sessione.getAttribute("FINDER39") !=null){
		if (((Object)sessione.getAttribute("FINDER39")).getClass() == new it.escsolution.escwebgis.condono.bean.CondonoFinder().getClass()){
			finder = (it.escsolution.escwebgis.condono.bean.CondonoFinder)sessione.getAttribute("FINDER39");
			}
	}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<% 	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>

<%@page import="java.util.ArrayList"%>
<%@page import="it.escsolution.escwebgis.docfa.bean.Docfa"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<html>
	<head>
		<title>Condono - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	
	<script>
		function mettiST(){
			document.mainform.ST.value = 3;
		}
		
		function visualizzaDettaglioStralcio(idx, visDett) {
			document.getElementById("rS" + idx).style.display = (visDett ? "none" : "");
			document.getElementById("rSDett" + idx).style.display = (visDett ? "" : "none");
		}
		
		function vaiListaDocfa(foglio, mappale, sub) {
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupDaCondono=true&foglio='+foglio+'&mappale='+mappale+'&sub='+sub;
			var finestra=window.open(url, 'docfaCollegati', 'top=100,left=100,height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no');		
			finestra.focus();
		}
	</script>
	<body >

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Condono" target="_parent">

		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Condono</span>
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
		<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
		<!-- 
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			Identificativo Condono</span>
		</div>
		-->
		<span class="TXTmainLabel">Identificativo Condono</span>
		<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 80%;">
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td style="width: 1%"></td>
				<td class="TDmainLabel" style="width: 15%;">	
					<span class="TXTmainLabel">Codice Condono</span>
				</td>	
				<td class="TDviewTextBox" style="width: 33%;">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=con.getCodCondono()%></span>
				</td>
				<td style="width: 2%"></td>
				<td class="TDmainLabel" style="width: 15%">	
					<span class="TXTmainLabel">Data Ins.Pratica</span>
				</td>	
				<td class="TDviewTextBox" style="width: 33%">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getDataInsPratica()==null?"-":con.getDataInsPratica())%></span>
				</td>
				<td style="width: 1%"></td>
			</tr>
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
		</table>
		&nbsp;
		<!-- 
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			Indirizzo Abuso</span>
		</div>
		-->
		<span class="TXTmainLabel">Indirizzo Abuso</span>
		<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 80%;">
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td style="width: 1%"></td>
				<td class="TDmainLabel" style="width: 15%;">	
					<span class="TXTmainLabel">Indirizzo</span>
				</td>	
				<td class="TDviewTextBox" style="width: 33%;">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getDescrVia()==null ? "-" : con.getDescrVia())%></span>
				</td>
				<td style="width: 2%"></td>
				<td style="width: 48%">
					<table cellpadding="0" cellspacing="0" style="width: 100%" >
						<tr>
							<td class="TDmainLabel" style="width: 20%;">	
								<span class="TXTmainLabel">Civico</span>
							</td>	
							<td class="TDviewTextBox" style="width: 28%;">
								<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getCivico()==null ? "-" : con.getCivico())%></span>
							</td>
							<td style="width: 4%"></td>
							<td class="TDmainLabel" style="width: 20%">	
								<span class="TXTmainLabel">Barrato</span>
							</td>	
							<td class="TDviewTextBox" style="width: 28%">	
								<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getBarrato()==null ? "-" : con.getBarrato())%></span>
							</td>
						</tr>
					</table>
				</td>
				<td style="width: 1%"></td>
			</tr>
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
		</table>
		&nbsp;
		<!-- 
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			Dati Esibente</span>
		</div>
		-->
		<span class="TXTmainLabel">Dati Esibente</span>
		<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 80%;">
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td style="width: 1%"></td>
				<td class="TDmainLabel" style="width: 15%;">	
					<span class="TXTmainLabel">CF/PIVA</span>
				</td>	
				<td class="TDviewTextBox" style="width: 33%;">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getCfPiEsibente()==null ? "-" : con.getCfPiEsibente())%></span>
				</td>
				<td style="width: 2%"></td>
				<td class="TDmainLabel" style="width: 15%">	
					<span class="TXTmainLabel">Soggetto</span>
				</td>	
				<td class="TDviewTextBox" style="width: 33%">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getEsibente()==null ? "-" : con.getEsibente())%></span>
				</td>
				<td style="width: 1%"></td>
			</tr>
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td></td>
				<td class="TDmainLabel">	
					<span class="TXTmainLabel">Indirizzo</span>
				</td>	
				<td class="TDviewTextBox">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getIndirizzoEsibente()==null ? "-" : con.getIndirizzoEsibente())%></span>
				</td>
				<td></td>
				<td colspan="2">	
					<table cellpadding="0" cellspacing="0" style="width: 100%" >
						<tr>
							<td class="TDmainLabel" style="width: 20%;">	
								<span class="TXTmainLabel">Civico</span>
							</td>	
							<td class="TDviewTextBox" style="width: 28%;">	
								<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getCivicoEsibente()==null ? "-" : con.getCivicoEsibente())%></span>
							</td>
							<td style="width: 4%"></td>
							<td class="TDmainLabel" style="width: 20%">	
								<span class="TXTmainLabel">Barrato</span>
							</td>	
							<td class="TDviewTextBox" style="width: 28%">	
								<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getBarratoEsibente()==null ? "-" : con.getBarratoEsibente())%></span>
							</td>
						</tr>
					</table>
				</td>
				<td></td>
			</tr>
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td></td>
				<td class="TDmainLabel">	
					<span class="TXTmainLabel">CAP</span>
				</td>	
				<td class="TDviewTextBox">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getCapEsibente()==null ? "-" : con.getCapEsibente())%></span>
				</td>
				<td></td>
				<td class="TDmainLabel">	
					<span class="TXTmainLabel">Comune</span>
				</td>	
				<td class="TDviewTextBox">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getComuneEsibente()==null ? "-" : con.getComuneEsibente())%></span>
				</td>
				<td></td>
			</tr>
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
		</table>
		&nbsp;
		<!-- 
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			Dati Condono</span>
		</div>
		-->
		<span class="TXTmainLabel">Dati Condono</span>
		<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 80%;">
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td style="width: 1%"></td>
				<td class="TDmainLabel" style="width: 15%;">	
					<span class="TXTmainLabel">Note</span>
				</td>	
				<td class="TDviewTextBox" colspan="4">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getNote()==null ? "-" : con.getNote())%></span>
				</td>
				<td style="width: 1%"></td>
			</tr>
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td></td>
				<td class="TDmainLabel">	
					<span class="TXTmainLabel">Tipo Pratica</span>
				</td>	
				<td style="width: 33%;">
					<table cellpadding="0" cellspacing="0" style="width: 100%">
						<tr>	
							<td class="TDviewTextBox" style="width: 15%;">	
								<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getTipoPratica()==null ? "-" : con.getTipoPratica())%></span>
							</td>
							<td style="width: 4%"></td>
							<td class="TDmainLabel" style="width: 56%">	
								<span class="TXTmainLabel">Flag Periodo Abuso</span>
							</td>	
							<td class="TDviewTextBox" style="width: 25%">	
								<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getFlagPeriodoAbuso()==null ? "-" : con.getFlagPeriodoAbuso())%></span>
							</td>
						</tr>
					</table>
				</td>
				<td style="width: 2%"></td>
				<td class="TDmainLabel" style="width: 15%">	
					<span class="TXTmainLabel">Esito</span>
				</td>	
				<td class="TDviewTextBox" style="width: 33%">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getEsito()==null ? "-" : con.getEsito())%></span>
				</td>
				<td></td>
			</tr>
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td></td>
				<td class="TDmainLabel">	
					<span class="TXTmainLabel">Descrizione Dati Catastali</span>
				</td>	
				<td class="TDviewTextBox" colspan="4">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getDescrDatiCatastali()==null ? "-" : con.getDescrDatiCatastali())%></span>
				</td>
				<td></td>
			</tr>
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td></td>
				<td class="TDmainLabel">	
					<span class="TXTmainLabel">Relazione</span>
				</td>	
				<td class="TDviewTextBox" colspan="4">	
					<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(con.getRelazione()==null ? "-" : con.getRelazione())%></span>
				</td>
				<td></td>
			</tr>
			<tr style="height: 3px;"></tr>
			<tr></tr>
			<tr></tr>
		</table>
		&nbsp;
		
		</td>
		
	</tr>
</table>


<div class="tabber">
					
		<% if (stralci != null && stralci.size() > 0) {%>			
			<div class="tabbertab">
						
						<h2>Stralci</h2>
				
			<table class="extWinTable" style="width: 100%" cellpadding="0" cellspacing="0">	
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Stralci</tr>
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Progressivo</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Sup. Abuso</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Snr Abuso</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Piano</span></td>		
				</tr>
				<% 	int contatore = 1;
					for (int i = 0; i < stralci.size(); i++) {
						Condono stralcio = (Condono)stralci.get(i);%>
					<tr id="rS<%=contatore%>" style="cursor: pointer; display:;" title="Clicca per visualizzare il dettaglio dello stralcio"
					onclick="visualizzaDettaglioStralcio('<%=contatore%>', true);">			
						<td class="extWinTDData">
							<span class="extWinTXTData"><%=(stralcio.getProgressivo()==null ? "-" : stralcio.getProgressivo())%></span>
						</td>
						<td class="extWinTDData">
							<span class="extWinTXTData"><%=(stralcio.getSuperficieAbuso()==null ? "-" : stralcio.getSuperficieAbuso())%></span>
						</td>
						<td class="extWinTDData">
							<span class="extWinTXTData"><%=(stralcio.getSnrAbuso()==null ? "-" : stralcio.getSnrAbuso())%></span>
						</td>
						<td class="extWinTDData">
							<span class="extWinTXTData"><%=(stralcio.getPiano()==null ? "-" : stralcio.getPiano())%></span>							
						</td>
					</tr>
					<tr id="rSDett<%=contatore%>" style="cursor: pointer; display: none;" title="Clicca per chiudere il dettaglio dello stralcio"
					onclick="visualizzaDettaglioStralcio('<%=contatore%>', false);">
						<td colspan = "4">
							<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 100%;">
								<tr style="height: 3px;"></tr>
								<tr></tr>
								<tr></tr>
								<tr>
									<td style="width: 1%"></td>
									<td class="TDmainLabel" style="width: 15%;">	
										<span class="TXTmainLabel">Progressivo</span>
									</td>	
									<td class="TDviewTextBox" style="width: 10%;">	
										<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getProgressivo()==null ? "-" : stralcio.getProgressivo())%></span>
									</td>
									<td style="width: 2%"></td>
									<td class="TDmainLabel" style="width: 13%;">	
										<span class="TXTmainLabel">Sup. Abuso</span>
									</td>	
									<td class="TDviewTextBox" style="width: 10%; text-align: right;">	
										<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getSuperficieAbuso()==null ? "-" : stralcio.getSuperficieAbuso())%></span>
									</td>
									<td style="width: 2%"></td>
									<td class="TDmainLabel" style="width: 12%;">	
										<span class="TXTmainLabel">Snr Abuso</span>
									</td>	
									<td class="TDviewTextBox" style="width: 10%; text-align: right;">	
										<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getSnrAbuso()==null ? "-" : stralcio.getSnrAbuso())%></span>
									</td>
									<td style="width: 2%"></td>
									<td class="TDmainLabel" style="width: 12%;">	
										<span class="TXTmainLabel">Piano</span>
									</td>	
									<td class="TDviewTextBox" style="width: 10%;">	
										<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getPiano()==null ? "-" : stralcio.getPiano())%></span>
									</td>
									<td style="width: 1%"></td>
								</tr>
								<tr style="height: 3px;"></tr>
								<tr></tr>
								<tr></tr>
								<tr>
									<td></td>
									<td class="TDmainLabel">	
										<span class="TXTmainLabel">Tipo Onere</span>
									</td>	
									<td class="TDviewTextBox">	
										<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getTipoOnere()==null ? "-" : stralcio.getTipoOnere())%></span>
									</td>
									<td></td>
									<td class="TDmainLabel">	
										<span class="TXTmainLabel">Tipo Abuso</span>
									</td>	
									<td class="TDviewTextBox">	
										<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getTipoAbuso()==null ? "-" : stralcio.getTipoAbuso())%></span>
									</td>
									<td></td>
									<td class="TDmainLabel">	
										<span class="TXTmainLabel">Subcatastale</span>
									</td>	
									<td class="TDviewTextBox">	
										<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getSubcatastale()==null ? "-" : stralcio.getSubcatastale())%></span>
									</td>
									<td></td>
									<td class="TDmainLabel">	
										<span class="TXTmainLabel">Dest. Iniziale</span>
									</td>	
									<td class="TDviewTextBox">	
										<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getDestIniziale()==null ? "-" : stralcio.getDestIniziale())%></span>
									</td>
									<td></td>
								</tr>
								<tr style="height: 3px;"></tr>
								<tr></tr>
								<tr></tr>					
								<tr>
									<td></td>
									<td class="TDmainLabel" colspan="2">	
										<span class="TXTmainLabel">Computo Dichiarato</span>
									</td>
									<td></td>
									<td colspan="2">
										<table cellpadding="0" cellspacing="0" style="width: 100%" >
											<tr>
												<td class="TDmainLabel" style="width: 30%;">	
													<span class="TXTmainLabel">Lire</span>
												</td>
												<td style="width: 2%"></td>
												<td class="TDviewTextBox" style="width: 68%; text-align: right;">
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getComputoDichiaratoL()==null ? "-" : stralcio.getComputoDichiaratoL())%></span>
												</td>
											</tr>
											<tr>
												<td class="TDmainLabel">	
													<span class="TXTmainLabel">Euro</span>
												</td>
												<td></td>
												<td class="TDviewTextBox" style="text-align: right;">	
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getComputoDichiaratoE()==null ? "-" : stralcio.getComputoDichiaratoE())%></span>
												</td>
											</tr>
										</table>
									</td>
									<td></td>
									<td class="TDmainLabel" colspan="2">	
										<span class="TXTmainLabel">Oblazione Calcolata</span>
									</td>
									<td></td>
									<td colspan="2">
										<table cellpadding="0" cellspacing="0" style="width: 100%" >
											<tr>
												<td class="TDmainLabel" style="width: 30%;">	
													<span class="TXTmainLabel">Lire</span>
												</td>	
												<td style="width: 2%"></td>
												<td class="TDviewTextBox" style="width: 68%; text-align: right;">
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getOblazioneCalcolataL()==null ? "-" : stralcio.getOblazioneCalcolataL())%></span>
												</td>
											</tr>
											<tr>
												<td class="TDmainLabel">	
													<span class="TXTmainLabel">Euro</span>
												</td>
												<td></td>
												<td class="TDviewTextBox" style="text-align: right;">	
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getOblazioneCalcolataE()==null ? "-" : stralcio.getOblazioneCalcolataE())%></span>
												</td>
											</tr>
										</table>
									</td>
									<td></td>
								</tr>
								<tr style="height: 3px;"></tr>
								<tr></tr>
								<tr></tr>
								<tr>
									<td></td>
									<td class="TDmainLabel" colspan="2">	
										<span class="TXTmainLabel">Oneri Primari</span>
									</td>
									<td></td>
									<td colspan="2">
										<table cellpadding="0" cellspacing="0" style="width: 100%" >
											<tr>
												<td class="TDmainLabel" style="width: 30%;">	
													<span class="TXTmainLabel">Lire</span>
												</td>	
												<td style="width: 2%"></td>
												<td class="TDviewTextBox" style="width: 68%; text-align: right;">
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getOneriPrimariL()==null ? "-" : stralcio.getOneriPrimariL())%></span>
												</td>
											</tr>
											<tr>
												<td class="TDmainLabel">	
													<span class="TXTmainLabel">Euro</span>
												</td>
												<td></td>
												<td class="TDviewTextBox" style="text-align: right;">	
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getOneriPrimariE()==null ? "-" : stralcio.getOneriPrimariE())%></span>
												</td>
											</tr>
										</table>
									</td>
									<td></td>
									<td class="TDmainLabel" colspan="2">	
										<span class="TXTmainLabel">Oneri Secondari</span>
									</td>
									<td></td>
									<td colspan="2">
										<table cellpadding="0" cellspacing="0" style="width: 100%" >
											<tr>
												<td class="TDmainLabel" style="width: 30%;">	
													<span class="TXTmainLabel">Lire</span>
												</td>	
												<td style="width: 2%"></td>
												<td class="TDviewTextBox" style="width: 68%; text-align: right;">
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getOneriSecondariL()==null ? "-" : stralcio.getOneriSecondariL())%></span>
												</td>
											</tr>
											<tr>
												<td class="TDmainLabel">	
													<span class="TXTmainLabel">Euro</span>
												</td>
												<td></td>
												<td class="TDviewTextBox" style="text-align: right;">	
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getOneriSecondariE()==null ? "-" : stralcio.getOneriSecondariE())%></span>
												</td>
											</tr>
										</table>
									</td>
									<td></td>
								</tr>
								<tr style="height: 3px;"></tr>
								<tr></tr>
								<tr></tr>
								<tr>
									<td></td>
									<td class="TDmainLabel" colspan="2">	
										<span class="TXTmainLabel">Smalt. Rifiuti</span>
									</td>
									<td></td>
									<td colspan="2">
										<table cellpadding="0" cellspacing="0" style="width: 100%" >
											<tr>
												<td class="TDmainLabel" style="width: 30%;">	
													<span class="TXTmainLabel">Lire</span>
												</td>	
												<td style="width: 2%"></td>
												<td class="TDviewTextBox" style="width: 68%; text-align: right;">
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getSmaltRifiutiL()==null ? "-" : stralcio.getSmaltRifiutiL())%></span>
												</td>
											</tr>
											<tr>
												<td class="TDmainLabel">	
													<span class="TXTmainLabel">Euro</span>
												</td>
												<td></td>
												<td class="TDviewTextBox" style="text-align: right;">	
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getSmaltRifiutiE()==null ? "-" : stralcio.getSmaltRifiutiE())%></span>
												</td>
											</tr>
										</table>
									</td>
									<td></td>
									<td class="TDmainLabel" colspan="2">	
										<span class="TXTmainLabel">Costo Costr.</span>
									</td>
									<td></td>
									<td colspan="2">
										<table cellpadding="0" cellspacing="0" style="width: 100%" >
											<tr>
												<td class="TDmainLabel" style="width: 30%;">	
													<span class="TXTmainLabel">Lire</span>
												</td>	
												<td style="width: 2%"></td>
												<td class="TDviewTextBox" style="width: 68%; text-align: right;">
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getCostoCostrL()==null ? "-" : stralcio.getCostoCostrL())%></span>
												</td>
											</tr>
											<tr>
												<td class="TDmainLabel">	
													<span class="TXTmainLabel">Euro</span>
												</td>
												<td></td>
												<td class="TDviewTextBox" style="text-align: right;">	
													<span class="TXTviewTextBox" style="vertical-align: middle;"><%=(stralcio.getCostoCostrE()==null ? "-" : stralcio.getCostoCostrE())%></span>
												</td>
											</tr>
										</table>
									</td>
									<td></td>
								</tr>
								<tr style="height: 3px;"></tr>
								<tr></tr>
								<tr></tr>
							</table>
						</td>
					</tr>
				<% contatore++;
				} %>
			</table>
			</div>
			
			<% for (int i = 0; i < stralci.size(); i++) {
				Condono stralcio = (Condono)stralci.get(i);%>				
				
				&nbsp;
			<% } %>
		<% }%>
		
		<% if (uius != null && uius.size() > 0) {%>	
		<div class="tabbertab">
						
			<h2>Uiu / Docfa collegati</h2>
				
			<table class="extWinTable" style="width: 100%" cellpadding="0" cellspacing="0">	
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Uiu / Docfa collegati</tr>
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Docfa collegati</span></td>		
				</tr>
				<% 	int contatore = 1;
					for (int i = 0; i < uius.size(); i++) {
					CondonoCoordinate uiu = (CondonoCoordinate)uius.get(i);
					String foglio = uiu.getFoglio() == null ?  "-" : uiu.getFoglio();
					String mappale = uiu.getMappale() == null ?  "-" : uiu.getMappale();
					String sub = uiu.getSub() == null ?  "-" : uiu.getSub();%>
					<tr id="rU<%=contatore%>">			
						<td class="extWinTDData">
							<span class="extWinTXTData"><%= foglio %></span>
						</td>
						<td class="extWinTDData">
							<span class="extWinTXTData"><%= mappale %></span>
						</td>
						<td class="extWinTDData">
							<span class="extWinTXTData"><%= sub %></span>
						</td>
						<td class="extWinTDData" style="text-align: center;">
							<% if (uiu.isDocfa()) {%>
								<span class="TDmainLabel">
									<a style="text-decoration: none;" 
									href="javascript:vaiListaDocfa('<%= foglio %>', '<%= mappale %>', '<%= sub %>');">
										<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
									</a>
								</span>
							<% } %>
						</td>
					</tr>
				<% contatore++;
				} %>
				
			</table>	
			</div>		
		<% }%>
		</div>

<% if (request.getParameter("popupACondonoDett") != null && new Boolean(request.getParameter("popupACondonoDett")).booleanValue()) { %>
	<br>
	<div align="center"><span class="extWinTXTTitle">
		<%  String href = EscServlet.URL_PATH + "/Condono?ST=2&popupACondono=true&foglio=" 
		+ request.getParameter("foglio") + "&mappale=" + request.getParameter("mappale") 
		+ "&sub=" + request.getParameter("sub"); %>
		<a class="iFrameLink" href="<%= href %>">torna alla lista</a></span>
	</div>
<% } %>

<% if (con != null){%>
<% String codice = con.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
<%}%>

<br><br><br>
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="39">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait">
</div>
</body>
</html>