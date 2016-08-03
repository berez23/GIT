<%@ page language="java" import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.licenzeCommercio.bean.*,it.escsolution.escwebgis.licenzeCommercio.servlet.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   LicenzaCommercio lc = (LicenzaCommercio) sessione.getAttribute("LICENZA"); %>
<%   ArrayList listaSoggetti = (ArrayList) sessione.getAttribute("LISTA_SOGGETTI_LC"); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  LicenzaCommercioFinder finder = null;

	if (sessione.getAttribute(LicenzeCommercioServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(LicenzeCommercioServlet.NOMEFINDER)).getClass() == new LicenzaCommercioFinder().getClass()){
			finder = (LicenzaCommercioFinder)sessione.getAttribute(LicenzeCommercioServlet.NOMEFINDER);
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

	

	
<html>
<head>
<title>Azienda - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/LicenzeCommercio" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">Dettaglio Licenze di Commercio</span>
</div>
&nbsp;
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" style="width: 100%;">
			<!--tr>
				<td colspan="8" class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">TITOLO</span></td>
			</tr-->
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Numero Esercizio</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getNumEsercizio() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Tipologia</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getTipologia() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" colspan="3"><span class="TXTviewTextBox">
					<%= (lc.getSedime() == null || lc.getSedime().trim().equals("-")) ? "\0" : lc.getSedime() + "&nbsp;" %>
					<%= lc.getNomeVia() %>
					<%= (lc.getNumCivico() == null || lc.getNumCivico().trim().equals("-")) ? "\0" : ",&nbsp;" + lc.getNumCivico() %>
				</span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Superfice Minuto</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getSupMinuto() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Superficie Totale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getSupTotale() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<center style="margin: 20px 0 3px 0"><span class="extWinTXTTitle">Lista Soggetti</span></center>
<table width="50%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Forma Giuridica</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
	</tr>
<%
	if (listaSoggetti == null || listaSoggetti.size() == 0)
	{
%>
	<tr>
	    <td class="extWinTDData">
			<span class="extWinTXTData">Non &egrave; presente alcun record associato</span>
		</td>
	</tr>
<%
	}
	else
	{
		Iterator iter = listaSoggetti.iterator();
		while (iter.hasNext())
		{
			SoggettoLC sogg = (SoggettoLC) iter.next();
%>
	<tr>
	    <td class="extWinTDData">
			<span class="extWinTXTData"><%= sogg.getFormaGiuridica() %></span>
		</td>
	    <td class="extWinTDData">
			<span class="extWinTXTData"><%= sogg.getCognome() %></span>
		</td>
	    <td class="extWinTDData">
			<span class="extWinTXTData"><%= sogg.getNome() %></span>
		</td>
	    <td class="extWinTDData">
			<span class="extWinTXTData"><%= sogg.getCodiceFiscale() %></span>
		</td>
	    <td class="extWinTDData">
			<span class="extWinTXTData"><%= sogg.getIndirizzoResidenza() %></span>
		</td>
	    <td class="extWinTDData">
			<span class="extWinTXTData"><%= sogg.getComuneResidenza() %></span>
		</td>
	</tr>
<%
		}
	}
%>
</table>

</td>
<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>
<br><br><br>

<center><span class="extWinTXTTitle"><a href="javascript:"></a></span></center>

<br><br>

<!-- FINE solo dettaglio -->
<% if (lc != null){%>
<% String codice = "";
   codice = lc.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="29">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>