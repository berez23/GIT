<%@ page language="java" import="it.escsolution.escwebgis.istat.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   CosapBean cb=(CosapBean)sessione.getAttribute(CosapLogic.DETT_COSAP); %><%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  ElementoListaFinder finder = null;

	if (sessione.getAttribute(CosapLogic.FINDER) !=null){
		if (((Object)sessione.getAttribute(CosapLogic.FINDER)).getClass() == new ElementoListaFinder().getClass()){
			finder = (ElementoListaFinder)sessione.getAttribute(CosapLogic.FINDER);
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
<%@page import="it.escsolution.escwebgis.cosap.bean.CosapBean"%>
<%@page import="it.escsolution.escwebgis.cosap.logic.CosapLogic"%>
<%@page import="it.escsolution.escwebgis.cosap.bean.ElementoListaFinder"%>
<%@page import="it.escsolution.escwebgis.cosap.bean.Autorizzazione"%>
<%@page import="it.escsolution.escwebgis.cosap.bean.Diffide"%>
<%@page import="it.escsolution.escwebgis.cosap.bean.OccAbu"%>
<%@page import="it.escsolution.escwebgis.cosap.bean.InvitoPagamento"%>
<%@page import="it.escsolution.escwebgis.cosap.bean.Verbali"%>
<%@page import="it.escsolution.escwebgis.cosap.bean.Versamenti"%>

<%@page import="java.util.Enumeration"%>

<html>
<head>
<title>Dettaglio Tassa Cosap</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>
var url0=null;
var url1=null;
var url2 =null;

function mettiST(){
	document.mainform.ST.value = 3;
}



</script>
<body >
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/DupNote" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Tassa Cosap</span>
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

<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>

&nbsp;

<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  ><span class="TXTmainLabel">Codice Contribuente </span></td>
				<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=cb.getDatiContribuente().getCodiceContribuente() %></span></td>

				<td class="TDmainLabel"  ><span class="TXTmainLabel">Descrizione</span></td>
				<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=cb.getDatiContribuente().getDescrizoneContribuente() %></span></td>
			</tr>
			<tr>

				<td class="TDmainLabel"  ><span class="TXTmainLabel">C.F.</span></td>
				<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=cb.getDatiContribuente().getCodiceFiscale() %></span></td>

				<td class="TDmainLabel"  ><span class="TXTmainLabel">Part.Iva</span></td>
				<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=cb.getDatiContribuente().getPartitaIva() %></span></td>				
				
			</tr>
		</table>
		</td>
	</tr>
</table>

</td>
</tr>
</table>

		
<br></br>

<div class="tabber">
					
					
<% if (cb.getAutorizazioni() != null && cb.getAutorizazioni().size() > 0) {%>
	<div class="tabbertab">
			
			<h2>Autorizzazioni</h2>


<table align=left  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Autorizzazioni</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">N.Civico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Occupazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Protocollo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Protocollo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Doc</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Doc</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Doc</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno di Riferimento</span></td>
	</tr>

  <% Autorizzazione a = new Autorizzazione(); %>
  <% java.util.Enumeration en = cb.getAutorizazioni().elements(); int contatore = 1; %>
  <% while (en.hasMoreElements()) {
        a = (Autorizzazione)en.nextElement();%>


     <tr>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=a.getIndirizzo() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=a.getCivico() %></span></td>		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=a.getDescrizioneTipoOccupazione() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=a.getNumeroProtocollo() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=a.getAnnoProtocollo() %></span></td>

				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=a.getDescrTipoDocumento() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=a.getNumeroDocumento() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=a.getAnnoDocumento() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=a.getAnnoRiferimento() %></span></td>

	</tr>
 <% } %>	
</table>
</div>

<%} %>

<% if (cb.getDiffide() != null && cb.getDiffide().size() > 0) {%>
	<div class="tabbertab">
			
			<h2>Diffide</h2>
<table align=left  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Diffide</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">N.Civico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Occupazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Zona</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Provvedimento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato Attuale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Importo Occupazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno di Riferimento</span></td>
	</tr>

  <% Diffide d = new Diffide(); %>
  <% java.util.Enumeration en = cb.getDiffide().elements(); int contatore = 1; %>
  <% while (en.hasMoreElements()) {
        d = (Diffide)en.nextElement();%>
     <tr>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=d.getIndirizzo() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=d.getCivico() %></span></td>		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=d.getDescrizioneTipoOccupazione() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=d.getCodiceZona() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=d.getNumeroProvvedimento() %></span></td>

				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=d.getStatoAttuale() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=d.getImportoOccupazione() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=d.getAnnoRiferimento() %></span></td>

	</tr>
 <% } %>	
</table>
</div>
<% } %>

<% if (cb.getOccupazioniAbusive() != null && cb.getOccupazioniAbusive().size() > 0) {%>
	<div class="tabbertab">
			
			<h2>Occupazioni Abusive</h2>

<table align=left  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Occupazioni Abusive</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">N.Civico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Provvedimento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Provvedimento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Occupazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Oggetto Anagrafico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato Attuale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno di Riferimento</span></td>
	</tr>

  <% OccAbu o = new OccAbu(); %>
  <% java.util.Enumeration en = cb.getOccupazioniAbusive().elements(); int contatore = 1; %>
  <% while (en.hasMoreElements()) {
        o = (OccAbu)en.nextElement();%>
     <tr>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=o.getIndirizzo() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=o.getCivico() %></span></td>		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=o.getTipoProvvedimento() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=o.getNumeroProvvedimento() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=o.getTipoOccupazione() %></span></td>

				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=o.getCodOggetto() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=o.getStatoAttuale() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=o.getAnnoRiferimento() %></span></td>

	</tr>
 <% } %>	
</table>
</div>
<% } %>


<% if (cb.getInvitiPagamento() != null && cb.getInvitiPagamento().size() > 0) {%>
	<div class="tabbertab">
			
			<h2>Inviti di Pagamento</h2>

<table align=left  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Inviti di Pagamento</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">N.Civico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Provvedimento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Provvedimento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Occupazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Oggetto Anagrafico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato Attuale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno di Riferimento</span></td>
	</tr>

  <% InvitoPagamento i = new InvitoPagamento(); %>
  <% java.util.Enumeration en = cb.getInvitiPagamento().elements(); int contatore = 1; %>
  <% while (en.hasMoreElements()) {
        i = (InvitoPagamento)en.nextElement();%>
     <tr>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=i.getIndirizzo() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=i.getCivico() %></span></td>		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=i.getTipoProvvedimento() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=i.getNumeroProvvedimento() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=i.getTipoOccupazione() %></span></td>

				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=i.getCodOggetto() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=i.getStatoAttuale() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=i.getAnnoRiferimento() %></span></td>

	</tr>
 <% } %>	
</table>
</div>
<% } %>

<% if (cb.getVerbali() != null && cb.getVerbali().size() > 0) {%>
	<div class="tabbertab">
			
			<h2>Verbali</h2>
<table align=left  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Verbali</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">N.Civico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Protocollo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Protocollo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Occupazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Oggetto Anagrafico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno di Riferimento</span></td>
	</tr>

  <% Verbali v = new Verbali(); %>
  <% java.util.Enumeration en = cb.getVerbali().elements(); int contatore = 1; %>
  <% while (en.hasMoreElements()) {
        v = (Verbali)en.nextElement();%>
     <tr>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=v.getIndirizzo() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=v.getCivico() %></span></td>		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=v.getNumeroProtocollo() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=v.getAnnoProtocollo() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=v.getTipoOccupazione() %></span></td>

				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=v.getCodOggetto() %></span></td>
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=v.getAnnoRiferimento() %></span></td>

	</tr>
 <% } %>	
</table>
</div>
<% } %>



<% if (cb.getVersamenti() != null && cb.getVersamenti().size() > 0) {%>
	<div class="tabbertab">
			
			<h2>Versamenti</h2>
<table align=left  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Versamenti</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Versamento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Versamento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Versameno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Importo Versamento</span></td>
	</tr>

  <% Versamenti vv = new Versamenti(); %>
  <% Enumeration en = cb.getVersamenti().elements(); int  contatore = 1; %>
  <% while (en.hasMoreElements()) {
        vv = (Versamenti)en.nextElement();%>
     <tr>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=vv.getAnnoVersamento() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=vv.getDataVersamento() %></span></td>		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=vv.getNumeroVersamento() %></span></td>
		
				<td  class="extWinTDData">
				<span class="extWinTXTData">&nbsp;<%=vv.getImportoVersamento() %></span></td>
		

	</tr>
 <% } %>	
</table>
</div>
<% } %>


</div>



<!-- FINE solo dettaglio -->
<% if (cb != null){%>
 
   <input type='hidden' name="OGGETTO_SEL" >
   
   
		
<%} if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="49">
		<input type='hidden' name="BACK" value="">
		
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">


</form>

<div id="wait" class="cursorWait" />
</body>
</html>