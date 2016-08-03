<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@ page language="java" import="it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO,java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.locazioni.bean.*,it.escsolution.escwebgis.locazioni.servlet.*,it.escsolution.escwebgis.locazioni.logic.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Locazioni lc = (Locazioni) sessione.getAttribute(LocazioniLogic.LOCAZIONI); %>
<% SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); %>
<% DecimalFormat dcf = new DecimalFormat("#,##0.00");
	dcf.setGroupingUsed(false);
	DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	dfs.setDecimalSeparator(',');
	dcf.setDecimalFormatSymbols(dfs);
	dcf.setMaximumFractionDigits(2);
%>

<%   java.util.ArrayList listaImmobili = (java.util.ArrayList) sessione.getAttribute(LocazioniLogic.LISTA_DETTAGLIO_IMMOBILI); %>
<%   java.util.ArrayList listaLocazioni = (java.util.ArrayList) sessione.getAttribute(LocazioniLogic.LISTA_DETTAGLIO_LOCAZIONI); %>
<%   java.util.ArrayList datiUiu = (java.util.ArrayList) sessione.getAttribute(LocazioniLogic.LISTA_DETTAGLIO_LOCAZIONI_DATI_UIU); %>

<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  LocazioniFinder finder = null;

	if (sessione.getAttribute(LocazioniServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(LocazioniServlet.NOMEFINDER)).getClass() == new LocazioniFinder().getClass()){
			finder = (LocazioniFinder)sessione.getAttribute(LocazioniServlet.NOMEFINDER);
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

	
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<title>Locazioni - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body>

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Locazioni</span>
</div>
&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>
<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Locazioni" target="_parent">


<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 50%;">
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Uff. Reg. Contr.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getUfficio() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Anno Reg. Contr.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getAnno() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Serie Reg. Contr.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getSerie() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Numero Reg. Contr.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getNumero() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Sottonum. Reg. Contr.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getSottonumero() == null ? "-" : lc.getSottonumero() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Progressivo Negozio</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getProgNegozio() == null ? "-" : lc.getProgNegozio() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Codice Negozio</span></td>
				<td colspan=3 class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getCodiceNegozio() == null ? "-" : lc.getCodiceNegozio() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Registrazione</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=  lc.getDataRegistrazione()==null?"-": df.format(lc.getDataRegistrazione()) %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Stipula</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getDataStipula()==null?"-":df.format(lc.getDataStipula()) %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Oggetto</span></td>
				<td colspan=3 class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getOggetto()==null?"-":lc.getOggetto() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Tipo Canone</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getTipoCanone()==null?"-": lc.getTipoCanone() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Importo Canone</span></td>
				<%String importoCanone = lc.getImportoCanone() == null ? "-" : "" + lc.getImportoCanone().toString();
				try {					
					importoCanone = dcf.format(lc.getImportoCanone().doubleValue());
				} catch (Exception e) {} %>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= importoCanone + (lc.getValutaCanone()==null?"":" "+lc.getValutaCanone()) %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Inizio</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getDataInizio()==null?"-":df.format(lc.getDataInizio()) %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Fine</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getDataFine()==null?"-":df.format(lc.getDataFine()) %></span></td>
			</tr>
		
		</table>
		</td>
	</tr>
</table>

</td>

</tr>
</table>

<div class="tabber">

<% 	if (listaLocazioni != null && listaLocazioni.size()>0 ){%>

<div class="tabbertab">
<h2>Lista proprietari / inquilini</h2>
<br>
<table class="extWinTable" style="width: 100%;">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Lista proprietari / inquilini</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO SOGGETTO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SESSO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COMUNE NASCITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVINCIA NASCITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA NASCITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COMUNE RESIDENZA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVINCIA RESIDENZA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO RESIDENZA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CIVICO RESIDENZA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA SUBENTRO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA CESSIONE</span></td>
	</tr>			  
<% 
	java.util.Iterator it = listaLocazioni.iterator();
	while (it.hasNext()) 
	{
		Locazioni loca = (Locazioni) it.next(); 
		if(loca.getTipoSoggetto()==null)
			loca.setTipoSoggetto("-");
		if(loca.getCodFisc()==null)
			loca.setCodFisc("-");
		if(loca.getSesso()==null)
			loca.setSesso("-");
		if(loca.getCittaNascita()==null)
			loca.setCittaNascita("-");
		if(loca.getProvinciaNascita()==null)
			loca.setProvinciaNascita("-");
		if(loca.getDataNascita()==null)
			loca.setDataNascita("-");
		if(loca.getCittaResidenza()==null)
			loca.setCittaResidenza("-");
		if(loca.getProvinciaResidenza()==null)
			loca.setProvinciaResidenza("-");
		if(loca.getIndirizzoResidenza()==null)
			loca.setIndirizzoResidenza("-");
		if(loca.getCivicoResidenza()==null || loca.getCivicoResidenza().trim().equals(""))
			loca.setCivicoResidenza("-");
		
		%>

    <tr >
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= loca.getTipoSoggetto() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= loca.getCodFisc() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= loca.getSesso() %></span></td>
		<td class="extWinTDData" nowrap>
		<span class="extWinTXTData"><%= loca.getCittaNascita() %></span></td>
		<td class="extWinTDData" align=center>
		<span class="extWinTXTData"><%= loca.getProvinciaNascita() %></span></td>
		<td class="extWinTDData" nowrap>
		<span class="extWinTXTData"><%= loca.getDataNascita()%></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= loca.getCittaResidenza() %></span></td>
		<td class="extWinTDData" align=center>
		<span class="extWinTXTData"><%= loca.getProvinciaResidenza() %></span></td>
		<td class="extWinTDData"  nowrap>
		<span class="extWinTXTData"><%= loca.getIndirizzoResidenza() %></span></td>
		<td class="extWinTDData" align=right>
		<span class="extWinTXTData"><%= loca.getCivicoResidenza() %></span></td>
		<td class="extWinTDData" align=right>
		<span class="extWinTXTData"><%= loca.getDataSubentro()==null?"-":df.format(loca.getDataSubentro())%></span></td>
		<td class="extWinTDData" align=right>
		<span class="extWinTXTData"><%= loca.getDataCessione()==null?"-":df.format(loca.getDataCessione())%></span></td>
	</tr>			
	<%}%>
</table>
<% 	if (datiUiu != null && datiUiu.size() > 0){%>
	<br />
	<table class="extWinTable" style="width: 100%;">
		<tr>&nbsp;</tr>
		<tr class="extWinTXTTitle">Unità immobiliari possedute dai proprietari (informazioni provenienti dalla banca dati catastale)</tr>
		<tr>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">CATEGORIA</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">CONSISTENZA</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">SUP. CAT.</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">RENDITA</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">% POSS.</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		</tr>		  
<% 
	it = datiUiu.iterator();
	while (it.hasNext()) 
	{   
		LocazioniUiu datoUiu = (LocazioniUiu)it.next(); 	
		boolean loc = datoUiu.isLocato();
		%>
	    <tr>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %>>
			<span class="extWinTXTData"><%= datoUiu.getCuaa() %></span></td>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %>>
			<span class="extWinTXTData"><%= datoUiu.getFoglio() %></span></td>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %>>
			<span class="extWinTXTData"><%= datoUiu.getParticella() %></span></td>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %>>
			<span class="extWinTXTData"><%= datoUiu.getUnimm() %></span></td>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %>>
			<span class="extWinTXTData"><%= datoUiu.getCategoria() %></span></td>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %> style="text-align: right;">
			<span class="extWinTXTData"><%= datoUiu.getConsistenza() %></span></td>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %> style="text-align: right;">
			<span class="extWinTXTData"><%= datoUiu.getSupCat() %></span></td>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %> style="text-align: right;">
			<span class="extWinTXTData"><%= datoUiu.getRendita() %></span></td>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %>>
			<span class="extWinTXTData"><%= datoUiu.getClasse() %></span></td>
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %>>
			<span class="extWinTXTData"><%= datoUiu.getPercPoss() %></span></td>
		<!-- <td class="extWinTDData">
			<span class="extWinTXTData"><%= datoUiu.getIndirizzo() %></span></td>
			<td class="extWinTDData">
			<span class="extWinTXTData"><%= datoUiu.getCivico() %></span></td> -->
			<td class=<%= !loc ? "extWinTDData" : "extWinTDDataAlter" %>>
				<table>
			  <%for(IndirizzoDTO ind : datoUiu.getIndirizziCat()){ %>
			  <tr>
			  <td><span class="extWinTXTData"><%= ind.getStrada() %></span></td>
			  <td><span class="extWinTXTData"><%= ind.getNumCivico() %></span></td>
			  </tr>
			  <%} %>
			  </table>
			</td>
		</tr>			
	<%}%>
</table>
<% } %>
</div>
<%}%>
<% 	if (listaImmobili != null && listaImmobili.size()>0 ){%>

<div class="tabbertab">
<h2>Lista immobili</h2>
<br>
<table class="extWinTable" style="width: 100%;">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Informazioni toponomastiche e catastali dell'oggetto locato</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IN VIA DI ACCATASTAMENTO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO CATASTO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INTERO/PORZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COMUNE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SEZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
	</tr>			  
<% 
	java.util.Iterator it = listaImmobili.iterator();
	while (it.hasNext()) 
	{
		LocazioniImmobili imm = (LocazioniImmobili) it.next(); 
		if(imm.getAccatastamento()==null)
			imm.setAccatastamento("-");
		if(imm.getTipoCatasto()==null)
			imm.setTipoCatasto("-");
		if(imm.getInteroPorzione()==null)
			imm.setInteroPorzione("-");
		if(imm.getIndirizzo()==null)
			imm.setIndirizzo("-");
		if(imm.getComune()==null)
			imm.setComune("-");
		if(imm.getSezUrbana()==null)
			imm.setSezUrbana("-");
		if(imm.getFoglio()==null)
			imm.setFoglio("-");
		if(imm.getParticella()==null)
			imm.setParticella("-");
		if(imm.getSubalterno()==null)
			imm.setSubalterno("-");
		%>

    <tr >
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= imm.getAccatastamento() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= imm.getTipoCatasto() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= imm.getInteroPorzione() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= imm.getComune() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= imm.getIndirizzo() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= imm.getSezUrbana() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= imm.getFoglio() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= imm.getParticella() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= imm.getSubalterno() %></span></td>
	</tr>			
	<%}%>
</table>
</div>
<%}%>
</div>

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
		<input type='hidden' name="UC" value="30">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>