<%@ page language="java" import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.modellof24.bean.*,it.escsolution.escwebgis.modellof24.servlet.*,it.escsolution.escwebgis.modellof24.logic.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   ModelloF24 f24 = (ModelloF24) sessione.getAttribute(ModelloF24Logic.MODELLOF24); %>
<%   
java.util.ArrayList modelli = new ArrayList();
modelli = (java.util.ArrayList) sessione.getAttribute(ModelloF24Logic.LISTA_DETTAGLIO_MODELLOF24); 
%>
<%   
java.util.ArrayList modelliG2 = new ArrayList();
modelliG2 = (java.util.ArrayList) sessione.getAttribute(ModelloF24Logic.LISTA_DETTAGLIO_G2); 
%>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  ModelloF24Finder finder = null;

	if (sessione.getAttribute(ModelloF24Servlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(ModelloF24Servlet.NOMEFINDER)).getClass() == new ModelloF24Finder().getClass()){
			finder = (ModelloF24Finder)sessione.getAttribute(ModelloF24Servlet.NOMEFINDER);
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
<title>Modello F24 - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ModelloF24" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">Dettaglio Modello F24</span>
</div>
&nbsp;
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<span class="TXTmainLabel" >G1 - Versamenti</span>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan = 1 align="left">
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Forn.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getFornituraData() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Progr.Forn.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getFornituraProgressivo() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Ripart.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getRipartizioneData() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Progr.Ripart.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getRipartizioneProgressivo() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Bonifico</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getBonificoData() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Progr.Delega</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getProgressivoDelega() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Progr.Riga</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getProgressivoRiga() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cod.Ente</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getCdEnteRendiconto() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Tipo Ente</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getTpEnte() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">CAB</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getCab() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Codice fiscale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getCdFiscaleContribuente() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Flag Err.Cod.Fiscale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getFlagErroreCdFiscale() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Anno Rif.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getAnnoRiferimento() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Flag Anno Err.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getFlagErroreAnno() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Ravv.ICI</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getIciRavvedimento() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Rateizzazione</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getRateizzazione() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cod.Trib.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getCdTributo() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Flag Err.Cod.Trib.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getFlagErroreCdTributo() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Ente Com.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getCdEnteComunale() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cod.Valuta</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox">EURO</span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Imp.Cred.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getImportoCredito() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Imp.Deb.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getImportoDebito() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Immobili Var.ICI</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getIciImmobiliVariati() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Acconto ICI</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getIciAcconto() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Detr.Abi.Princ.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getImportoDetrazioneAbPr() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Saldo ICI</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getIciSaldo() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Numero Fabbr.ICI</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getIciNFabbricati() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Flag Err.ICI</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getFlagErroreDatiIci() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Tipo Imposta</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getImpostaTipo() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Risc.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= f24.getDataRiscossione() %></span></td>
				
			</tr>
		</table>
		</td>
	</tr>
</table>
<br><br><br>


<% 	if (f24 != null ){%>
<span class="TXTmainLabel" align="left">G2 - Disposizione di Accrediti</span>
<br>
<table class="extWinTable" style="width: 60%;">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Imposta</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Forn.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Progr.Forn.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Ripart.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Progr.Ripart.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Bonifico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Ente Com.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp.Accredito</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Valuta</span></td>
	</tr>			  
<% 
	if (modelliG2 != null)
	{	
		java.util.Iterator it = modelliG2.iterator();
		while (it.hasNext()) 
		{
			ModelloF24 mode = (ModelloF24) it.next(); 
			%>
		
	    <tr >
			<td class="extWinTDData"  align="center">
			<span class="extWinTXTData"><%= mode.getAccreditoTipoImposta()%></span></td>
			<td class="extWinTDData" align="center">
			<span class="extWinTXTData"><%= mode.getFornituraData() %></span></td>
			<td class="extWinTDData">
			<span class="extWinTXTData"><%= mode.getFornituraProgressivo() %></span></td>
			<td class="extWinTDData" align="right">
			<span class="extWinTXTData" ><%= mode.getRipartizioneData() %></span></td>
			<td class="extWinTDData" align="right">
			<span class="extWinTXTData"><%= mode.getRipartizioneProgressivo() %></span></td>
			<td class="extWinTDData" >
			<span class="extWinTXTData"><%= mode.getBonificoData() %></span></td>
			<td class="extWinTDData"  align="right">
			<span class="extWinTXTData"><%= mode.getAccreditoCdEnteComunale() %></span></td>
			<td class="extWinTDData"  align="right">
			<span class="extWinTXTData"><%= mode.getAccreditoImporto()%></span></td>
			<td class="extWinTDData"  align="left">
			<span class="extWinTXTData">EURO</span></td>
		</tr>		
	<%}
	}%>	
</table>
<%}%>

<br><br><br>


<% 	if (modelli != null ){%>
<span class="TXTmainLabel" align="left">G5 - Identificazione Accrediti</span>
<br>
<table class="extWinTable" style="width: 60%;">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Imposta</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Forn.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Progr.Forn.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Ente Com.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cro</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Accred.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Ripart.Orig.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Prog. Ripart.Orig.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Bonif.Orig.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Accredito</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Valuta</span></td>
	</tr>			  
<% 
	java.util.Iterator it = modelli.iterator();
	while (it.hasNext()) 
	{
		ModelloF24 mode = (ModelloF24) it.next(); 
		%>

    <tr >
		<td class="extWinTDData" align="center">
		<span class="extWinTXTData"><%= mode.getIdentificazioneTipoImposta() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= mode.getFornituraData() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= mode.getFornituraProgressivo() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= mode.getIdentificazioneCdEnteComunale() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= mode.getIdentificazioneCro() %></span></td>
		<td class="extWinTDData" align="right">
		<span class="extWinTXTData" ><%= mode.getIdentificazioneDataAccreditamento() %></span></td>
		<td class="extWinTDData" align="right">
		<span class="extWinTXTData"><%= mode.getIdentificazioneDataRipOrig() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= mode.getIdentificazioneProgRipOrig() %></span></td>
		<td class="extWinTDData" align="right">
		<span class="extWinTXTData"><%= mode.getIdentificazioneDataBonificoOriginario() %></span></td>
		<td class="extWinTDData"  align="right">
		<span class="extWinTXTData"><%= mode.getIdentificazioneImportoAccredito()%></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData">EURO</span></td>
	</tr>			
	<%}%>
</table>
<%}%>

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
<% if (f24 != null){%>
<% String codice = "";
   codice = f24.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="33">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>