<%@ page language="java" import="java.util.ArrayList, java.util.Iterator"%>
<%@ page language="java" import="it.escsolution.escwebgis.urbanistica.bean.*,it.escsolution.escwebgis.urbanistica.servlet.*,it.escsolution.escwebgis.urbanistica.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%
HttpSession sessione = request.getSession(true);
%>
<%
Hashtable htUrba = (Hashtable) sessione.getAttribute(UrbanisticaLogic.DETTAGLIO_URBANISTICA);
%>
<%
	String codice = "";
%>
<%
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");
	int st = new Integer(ST).intValue();
%>

<%
	UrbanisticaFinder finder = null;

	if (sessione.getAttribute(UrbanisticaServlet.NOMEFINDER) != null)
	{
		if (((Object) sessione.getAttribute(UrbanisticaServlet.NOMEFINDER)).getClass() == new UrbanisticaFinder().getClass())
		{
			finder = (UrbanisticaFinder) sessione.getAttribute(UrbanisticaServlet.NOMEFINDER);
		}

	}

	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT") != null)
		js_back = ((Integer) sessione.getAttribute("BACK_JS_COUNT")).intValue();

	java.util.Vector vctLink = null;
	if (sessione.getAttribute("LISTA_INTERFACCE") != null)
	{
		vctLink = ((java.util.Vector) sessione.getAttribute("LISTA_INTERFACCE"));
	}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>



<%@page import="it.webred.utils.DateFormat"%>
<%@page import="it.webred.utils.GenericTuples"%>

<%@page import="it.escsolution.escwebgis.urbanistica.bean.Urbanistica"%>
<%@page import="it.escsolution.escwebgis.urbanistica.logic.UrbanisticaLogic"%>
<%@page import="it.escsolution.escwebgis.urbanistica.bean.UrbanisticaFinder"%>
<%@page import="it.escsolution.escwebgis.urbanistica.servlet.UrbanisticaServlet"%>
<%@page import="java.util.Hashtable"%>
<%@page import="it.escsolution.escwebgis.urbanistica.bean.UrbaAgibilita"%>
<%@page import="it.escsolution.escwebgis.urbanistica.bean.UrbaConcStorico"%>
<%@page import="it.escsolution.escwebgis.urbanistica.bean.UrbaInizioLavori"%>
<%@page import="it.escsolution.escwebgis.urbanistica.bean.UrbaProtocollo"%>
<%@page import="it.escsolution.escwebgis.urbanistica.bean.UrbaPosizioneEdilizia"%>
<%@page import="it.escsolution.escwebgis.urbanistica.bean.UrbaPosizioneEdilizia2"%>
<%@page import="it.escsolution.escwebgis.urbanistica.bean.UrbaVigilanzaEdi"%>
<%@page import="it.escsolution.escwebgis.urbanistica.bean.UrbaCommissioneEdilizia"%><html>
<head>
<title>Urbanistica - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">


<form name="mainform"
	action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Urbanistica"
	target="_parent">

<div align="center" class="extWinTDTitle"><span
	class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Urbanistica</span></div>
&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
	<br/>
<table style="background-color: white; width: 100%;">
	<tr style="background-color: white;">
		<td
			style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

			<%
			if (htUrba.containsKey(UrbanisticaLogic.DETTAGLIO_AGIBILITA)){
				ArrayList alAgibilita = (ArrayList)htUrba.get(UrbanisticaLogic.DETTAGLIO_AGIBILITA);
				if (alAgibilita != null && alAgibilita.size()>0){
					%>
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
					
			<tr>
				<td colspan=1>
				
					<%
					for (int i=0; i<alAgibilita.size(); i++){
						UrbaAgibilita agibilita = (UrbaAgibilita)alAgibilita.get(i);
						codice = String.valueOf( agibilita.getPk() );
					%>
					<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="8" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;" align="center"><span
							class="TXTmainLabel">DATI AGIBILITA</span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nominativo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getNominativo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ubicazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getUbicazione()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Num</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getNum()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">R.p.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getR_p()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Bis A.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getBis_a()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Agi.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getData_agi()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Protocollo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getProt()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cognome</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCognome()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nome</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getNome()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Fu</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getFu()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Concessione 1</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getConcessione_1()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Variante 1</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getVariante_1()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Variante 2</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getVariante_2()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Variante 3</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getVariante_3()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Inizio Lavori</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getInizio_lavori()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Fine Lavori</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getFine_lavori()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Rilascio</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getRilascio()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Anno</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getAnno()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getNote()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Integrazioni</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getIntegrazioni()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Foglio</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getFoglio()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Mappale</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getMapp()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Subalterno</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getSub()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cat. 1</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCat1()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cons. 1</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCons1()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Cat. 1</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getSup_cat1()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cat. 2</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCat2()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cons. 2</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCons2()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Cat. 2</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getSup_cat2()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cat. 3</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCat3()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cons. 3</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCons3()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Cat. 3</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getSup_cat3()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cat. 4</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCat4()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cons. 4</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCons4()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Cat. 4</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getSup_cat4()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cat. 5</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCat3()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cons. 5</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCons5()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Cat. 5</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getSup_cat5()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cat. 6</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCat6()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cons. 6</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCons6()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Cat. 6</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getSup_cat6()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Marca</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getMarca()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Dati</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getDati()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Accatasta</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getAccatasta()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Conform. Ce.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getConform_ce()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Impianti</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getImpianti()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Energetico</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getEnergetico()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cpi</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCpi()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Strutture</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getStrutture()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Superam</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getSuperam()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ingresso</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getIngresso()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Civico</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCivico()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Idrico</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getIdrico()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Fognario</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getFognario()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Matricola</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getMatricola()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Inizio Fine</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getIniziofine()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Documenti</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getDocumenti()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Diritti</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getDiritti()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">C.D.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=agibilita.getCd()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
					</tr>
				</table>
					<%
					}
					%>
				</td>
			</tr>
		</table>
		<br>
		<br>		
					<%
				}
			}
			%>
		

		<%
			if (htUrba.containsKey(UrbanisticaLogic.DETTAGLIO_CONC_STOR)){
				ArrayList alConcStorico = (ArrayList)htUrba.get(UrbanisticaLogic.DETTAGLIO_CONC_STOR);
				if (alConcStorico != null && alConcStorico.size()>0){
					%>
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
					
			<tr>
				<td colspan=1>
				
					<%
					for (int i=0; i<alConcStorico.size(); i++){
						UrbaConcStorico concStorico = (UrbaConcStorico)alConcStorico.get(i);
						codice = String.valueOf( concStorico.getPk() );
					%>
					<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="8" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;" align="center"><span
							class="TXTmainLabel">DATI STORICO CONCESSIONI EDILIZIE </span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Anno</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getAnno()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nominativo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getNominativo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ubicazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getUbicazione()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Verb</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getVerb()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Parere</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getParere()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Conc.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getData_cs()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Foglio</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getFoglio()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Mappale</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getMappale()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getSup_res()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Azien.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getSup_azien()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Vol. Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getVol_res()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Vol. Azien.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getVol_azien()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Conc. Edilizia</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getConc_edilizia()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Zona Omogenea</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getZona_omogenea()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Lotto n.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getLotto_n()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getNote()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note 2</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getNote2()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note 3</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getNote3()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note 4</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getNote4()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note 6</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getNote6()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Rilascio Conc.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=concStorico.getData_rilascio_conc()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
					</tr>										

				</table>
					<%
					}
					%>
				</td>
			</tr>
		</table>
		<br>
		<br>		
					<%
				}
			}
			%>
			



		<%
			if (htUrba.containsKey(UrbanisticaLogic.DETTAGLIO_INIZIO_LAVORI)){
				ArrayList alInizioLavori = (ArrayList)htUrba.get(UrbanisticaLogic.DETTAGLIO_INIZIO_LAVORI);
				if (alInizioLavori != null && alInizioLavori.size()>0){
					%>
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
					
			<tr>
				<td colspan=1>
				
					<%
					for (int i=0; i<alInizioLavori.size(); i++){
						UrbaInizioLavori inizioLavori = (UrbaInizioLavori)alInizioLavori.get(i);
						codice = String.valueOf( inizioLavori.getPk() );
					%>
					<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="8" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;" align="center"><span
							class="TXTmainLabel">DATI INIZIO LAVORI </span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Num</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getNum()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Durc</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getDurc()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Den. Strut.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getDen_strut()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Compl. Dati.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getCompl_dati()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Rel. Tecnica</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getRel_tecnica()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sanzione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getSanzione()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Prot.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getData_prot()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Prot.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getProt()%></span></td>
					</tr>										
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nominativo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getNominativo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Concessione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getConcessione()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Indirizzo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getIndirizzo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ubicazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getUbicazione()%></span></td>
					</tr>										
					<tr>
					<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Tipo Intervento</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getTipo_intervento()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Pos. Edil.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getPos_edil()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Inizio Lavori</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getData_inizio_lavori()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Direttore Lavori</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getDirettore_lavori()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Impresa</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getImpresa()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Integrazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getIntegrazione()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sopralluogo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getSopralluogo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sosp. Lavori</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getSosp_lavori()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ripr. Lavori</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getRipr_lavori()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Dimissioni Dl.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getDimissioni_dl()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sostituzione Impresa</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=inizioLavori.getSostituzione_impresa()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
					</tr>																				
				</table>
					<%
					}
					%>
				</td>
			</tr>
		</table>
		<br>
		<br>		
					<%
				}
			}
			%>




		<%
			if (htUrba.containsKey(UrbanisticaLogic.DETTAGLIO_PROT)){
				ArrayList alProtocollo = (ArrayList)htUrba.get(UrbanisticaLogic.DETTAGLIO_PROT);
				if (alProtocollo != null && alProtocollo.size()>0){
					%>
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
					
			<tr>
				<td colspan=1>
				
					<%
					for (int i=0; i<alProtocollo.size(); i++){
						UrbaProtocollo protocollo = (UrbaProtocollo)alProtocollo.get(i);
						codice = String.valueOf( protocollo.getPk() );
					%>
					<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="8" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;" align="center"><span
							class="TXTmainLabel">DATI PROTOCOLLI </span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Pos. Ed.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getPos_ed()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Resp. Proc.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getResp_proc()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Prot. Dom.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getProt_dom()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Dom.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getData_dom()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nominativo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getNominativo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Intervento</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getIntervento()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ubicazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getUbicazione()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Invio Asl</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getInvio_asl()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Rientro Asl</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getRientro_asl()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Parere Asl</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getParere_asl()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cartella Utp n.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=protocollo.getCartella_utp_n()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
					</tr>					
					
				</table>
					<%
					}
					%>
				</td>
			</tr>
		</table>
		<br>
		<br>		
					<%
				}
			}
			%>




		<%
			if (htUrba.containsKey(UrbanisticaLogic.DETTAGLIO_POS_EDI)){
				ArrayList alPosEdi = (ArrayList)htUrba.get(UrbanisticaLogic.DETTAGLIO_POS_EDI);
				if (alPosEdi != null && alPosEdi.size()>0){
					%>
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
					
			<tr>
				<td colspan=1>
				
					<%
					for (int i=0; i<alPosEdi.size(); i++){
						UrbaPosizioneEdilizia posizioneEdilizia = (UrbaPosizioneEdilizia)alPosEdi.get(i);
						codice = String.valueOf( posizioneEdilizia.getPk() );
					%>
					<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="8" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;" align="center"><span
							class="TXTmainLabel">DATI POSIZIONE EDILIZIA </span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Anno</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getAnno()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nominativo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getNominativo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ubicazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getUbicazione()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Verb.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getVerb()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Parere</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getParere()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Pos. Edi.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getData_pe()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Foglio</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getFoglio()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Mappale</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getMappale()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getSup_res()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Azien.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getSup_azien()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Vol. Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getVol_res()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Vol. Azien.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getVol_azien()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Conc. Edilizia</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getConc_edilizia()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Zona Omogenea</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getZona_omogenea()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Lotto n.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getLotto_n()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getNote()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note 2</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getNote2()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note 3</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getNote3()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note 4</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getNote4()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note 6</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia.getNote6()%></span></td>
					</tr>
					
				</table>
					<%
					}
					%>
				</td>
			</tr>
		</table>
		<br>
		<br>		
					<%
				}
			}
			%>




		<%
			if (htUrba.containsKey(UrbanisticaLogic.DETTAGLIO_POS_EDI2)){
				ArrayList alPosEdi2 = (ArrayList)htUrba.get(UrbanisticaLogic.DETTAGLIO_POS_EDI2);
				if (alPosEdi2 != null && alPosEdi2.size()>0){
					%>
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
					
			<tr>
				<td colspan=1>
				
					<%
					for (int i=0; i<alPosEdi2.size(); i++){
						UrbaPosizioneEdilizia2 posizioneEdilizia2 = (UrbaPosizioneEdilizia2)alPosEdi2.get(i);
						codice = String.valueOf( posizioneEdilizia2.getPk() );
					%>
					<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="8" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;" align="center"><span
							class="TXTmainLabel">DATI POSIZIONE EDILIZIA 2</span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Posi. Edi.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getPosi_edi()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Prot.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getProt()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Pe2</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getData_pe2()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nominativo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getNominativo()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Luogo Nascita</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getLuogo_nascita()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Nascita</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getData_nascita()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">P. IVA - C. Fis.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getPi_cf()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Indirizzo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getIndirizzo()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Cap</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getCap()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Citta</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getCitta()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Intervento</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getIntervento()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ubicazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getUbicazione()%></span></td>
					</tr>	
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Lotto</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getLotto()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Foglio</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getFoglio()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Mappale</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getMappale()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Zona Urbana</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getZona_urb()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Lotto</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getSup_lotto()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Utile Abitabile</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getSup_utile_abitabile()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Netta non Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getSup_netta_non_res()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getSup_res()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Com. Azi.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getSup_comm_azi()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Vol. Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getVol_res()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Vol. Com. Azi.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getVol_com_azi()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Tur. Com. Dir. no Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getSup_tur_com_dir_no_res()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Tur. Com. Dir. Accessori</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getSup_tur_com_dir_accessori()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Tecnico Prog.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getTecnico_prog()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Parere Utc</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getParere_utc()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Parere Ce.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getParere_ce()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Ce.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getData_ce()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Verbale N.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getVerbale_n()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Resp. Proc.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getResp_proc()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getNote()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Conc. Aut. Emesse</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getConc_aut_emesse()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Conc. Aut. Emesse Data</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getConc_aut_emesse_data()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Conc. Aut. Emesse Ritirate</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getConc_aut_emesse_ritirate()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Volture</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getVolture()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Inizio Lavori</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getInizio_lavori()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Il</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getData_il()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Protocollo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getProtocollo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sopralluogo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getSopralluogo()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Fine Lavori</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getFine_lavori()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Abitabilita</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=posizioneEdilizia2.getAbitabilita()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
					</tr>

					
				</table>
					<%
					}
					%>
				</td>
			</tr>
		</table>
		<br>
		<br>		
					<%
				}
			}
			%>



		<%
			if (htUrba.containsKey(UrbanisticaLogic.DETTAGLIO_VIGILANZA_EDI)){
				ArrayList alVigilanzaEdi = (ArrayList)htUrba.get(UrbanisticaLogic.DETTAGLIO_VIGILANZA_EDI);
				if (alVigilanzaEdi != null && alVigilanzaEdi.size()>0){
					%>
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
					
			<tr>
				<td colspan=1>
				
					<%
					for (int i=0; i<alVigilanzaEdi.size(); i++){
						UrbaVigilanzaEdi vigilanzaEdi = (UrbaVigilanzaEdi)alVigilanzaEdi.get(i);
						codice = String.valueOf( vigilanzaEdi.getPk() );
					%>
					<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="8" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;" align="center"><span
							class="TXTmainLabel">DATI VIGILANZA EDILE </span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Posizione n.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getPosizione_n()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Vigilanza</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getData_vig()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Prot.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getProt()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nominativo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getNominativo()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Provenienza</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getProviene()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Oggetto</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getOggetto()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nota</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getNota()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Denunciato</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getDenunciato()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Resp. Proc.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getResp_proc()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Attivita</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getAttivita()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getNote()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Prof. Proc. Acquisizione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getProf_proc_acquisizione()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Notifica Ordinanza</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vigilanzaEdi.getNotifica_ordinanza()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
					</tr>					
				</table>
					<%
					}
					%>
				</td>
			</tr>
		</table>
		<br>
		<br>		
					<%
				}
			}
			%>




		<%
			if (htUrba.containsKey(UrbanisticaLogic.DETTAGLIO_COMMISSIONI_EDILIZIE)){
				ArrayList alCommissioniEdi = (ArrayList)htUrba.get(UrbanisticaLogic.DETTAGLIO_COMMISSIONI_EDILIZIE);
				if (alCommissioniEdi != null && alCommissioniEdi.size()>0){
					%>
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
					
			<tr>
				<td colspan=1>
				
					<%
					for (int i=0; i<alCommissioniEdi.size(); i++){
						UrbaCommissioneEdilizia commissioneEdilizia = (UrbaCommissioneEdilizia)alCommissioniEdi.get(i);
						codice = String.valueOf( commissioneEdilizia.getPk() );
					%>
					<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="8" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;" align="center"><span
							class="TXTmainLabel">DATI COMMISSIONI EDILIZIE </span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Anno</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getAnno()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nominativo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getNominativo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ubicazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getUbicazione()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Verb</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getVerb()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Parere</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getParere()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Conc.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getData_ce()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Foglio</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getPos()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Mappale</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getCe_aut()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getZona_urbanistica()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sup. Azien.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getLotto()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Vol. Res.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getTipo_intervento()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Vol. Azien.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getSup_lotto()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Conc. Edilizia</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getEsistente()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Zona Omogenea</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getProgetto()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Lotto n.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getEsistente2()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=commissioneEdilizia.getProgetto2()%></span></td>
					</tr>					
				</table>
					<%
					}
					%>
				</td>
			</tr>
		</table>
		<br>
		<br>		
					<%
				}
			}
			%>







		<%
			if (htUrba.containsKey(UrbanisticaLogic.DETTAGLIO_BUCALOSSI)){
				ArrayList alBucalossi = (ArrayList)htUrba.get(UrbanisticaLogic.DETTAGLIO_BUCALOSSI);
				if (alBucalossi != null && alBucalossi.size()>0){
					%>
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
					
			<tr>
				<td colspan=1>
				
					<%
					for (int i=0; i<alBucalossi.size(); i++){
						UrbaBucalossi bucalossi = (UrbaBucalossi)alBucalossi.get(i);
						codice = String.valueOf( bucalossi.getPk() );
					%>
					<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="8" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;" align="center"><span
							class="TXTmainLabel">DATI BUCALOSSI </span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Pos. Buca.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getPos_buca()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Pos. Edi.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getPos_edi()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Conc. Edi.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getConc_edi()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data Conc. Edi.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getData_ce()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nominativo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getNominativo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Indirizzo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getIndirizzo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Ubicazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getUbicazione()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Oneri Urba. Primaria</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getOneri_urba_primari()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Garanzia Oneri Urba. Primaria</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getGaranzia_one_urb_pri()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Costo Urbanizzazione Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getCosto_urbanizzazione_eur()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Costo Urbanizzazione Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getCosto_urbanizzazione_lir()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Costo Costruzione Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getCosto_costruzione_eur()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Costo Costruzione Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getCosto_costruzione_lir()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Totale Urba. Costr. Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getTot_urba_costr_eur()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Totale Urba. Costr. Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getTot_urba_costr_lir()%></span></td>						
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Acconto Saldo Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getAcconto_saldo_eur()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Acconto Saldo Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getAcconto_saldo_lir()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Estremi Vers. Acconto Saldo</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getEstremi_vers_acconto_saldo()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Imp. da Rateizzare Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getImp_da_rateizzare_eur()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Imp. da Rateizzare Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getImp_da_rateizzare_lir()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Polizza Assicurativa/Bancaria</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getPolizza_assicura_bancaria()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Imp. Prima Rata Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getImp_prima_rata_eur()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Imp. Prima Rata Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getImp_prima_rata_lir()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Estremi Vers. Prima Rata</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getEstremi_vers_prima_rata()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Scadenza Prima Rata</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getScadenza_prima_rata()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sanzione Prima Rata Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getSanzione_prima_rata_eur()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sanzione Prima Rata Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getSanzione_prima_rata_lir()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Imp. Altra Rata Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getImp_altra_rata_eur()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Imp. Altra Rata Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getImp_altra_rata_lir()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Estremi Vers. Altra Rata</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getEstremi_vers_altra_rata()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Scadenza Altra Rata</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getScadenza_altra_rata()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sanzione Altra Rata Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getSanzione_altra_rata_eur()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sanzione Altra Rata Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getSanzione_altra_rata_lir()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Estremi Vers. Altra Rata</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getEstremi_vers_altra_rata()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Tot. Rate + Sanzione Eur</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getTot_rate_sanzione_eur()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Tot. Rate + Sanzione Lir</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getTot_rate_sanzione_lir()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Note</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getNote()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Tecnico Istruttore</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getTecnico_istruttore()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Tecnico Esterno</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getTecnico_esterno()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Oneri Aree Parcheggi</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getOneri_aree_parcheggi()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Estremi Vers. Aree Parcheggi</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=bucalossi.getEstremi_vers_aree_parcheggi()%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">&nbsp;</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="&nbsp;"%></span></td>
					</tr>
				</table>
					<%
					}
					%>
				</td>
			</tr>
		</table>
		<br>
		<br>		
					<%
				}
			}
			%>






		</td>
		<%
			if (false && vctLink != null && vctLink.size() > 0)
			{
		%>
		<td class="iFrameLink"><jsp:include
			page="../frame/iFrameLink.jsp"></jsp:include></td>
		<%
		}
		%>
	</tr>
</table>
<br>
<br>
<br>

<center><span class="extWinTXTTitle"><a
	href="javascript:"></a></span></center>

<br>
<br>

<!-- FINE solo dettaglio -->  <%

 %> <input type='hidden' name="OGGETTO_SEL" id="OGGETTO_SEL" value="<%=codice%>">
 
<%
 	if (finder != null)
 	{
 %> <input type='hidden' name="ACT_PAGE"
	value="<%=finder.getPaginaAttuale()%>"> <%
 	}
 	else
 	{
 %> <input type='hidden' name="ACT_PAGE" value=""> <%
 }
 %> 
 <input type='hidden' name="AZIONE" value=""> 
 <input type='hidden' name="ST" value=""> 
 <input type='hidden' name="UC" value="107"> 
 <input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>"></form>


<div id="wait" class="cursorWait" />
</body>
</html>
