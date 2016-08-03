<%@ page language="java" import="it.escsolution.escwebgis.istat.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuenteGiu contr=(it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuenteGiu)sessione.getAttribute("CONTRIBUENTE"); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuentiGiuFinder finder = null;

	if (sessione.getAttribute("FINDER22") !=null){
		if (((Object)sessione.getAttribute("FINDER22")).getClass() == new it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuentiGiuFinder().getClass()){
			finder = (it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuentiGiuFinder)sessione.getAttribute("FINDER22");
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
	

	
<html>
<head>
<title>Agenzia Entrate - Contribuente - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body >

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AgenziaEntrateContribuentiGiu" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Contribuente Giuridico</span>
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

<table align=center  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
<!-- DAti azienda-->	
<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;" ><span class="TXTmainLabel">Partita IVA</span></td>
				<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=contr.getCodFiscSoggetto()%></span></td>
				
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Attribuzione</span></td>
				<td class="TDviewTextBox" style="width: 5%; white-space: nowrap;"><span class="TXTviewTextBox"><%=contr.getDataAttribuzione()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>	
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Denominazione</span></td>
				<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=contr.getDenominazione()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Descr. Natura Giuridica</span></td>
				<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=contr.getDesNaturaGiu()%></span></td>
			</tr>
		</table>
		</td>
	</tr>	
	<tr></tr>
	<tr></tr>
	<tr></tr>				
	<tr>	
	<!-- Domicilio Fiscale-->
		<td colspan = 1>
		<table class="viewExtTable" >
					<tr><td >&nbsp;</td></TR>
			<tr>
				<td  class="TDmainLabel" align="center"><span class="TXTmainLabel">Domicilio Fiscale</span></td>
			</tr>	

		</table>
		</td>
	</tr>
		<tr>	
		<td colspan = 1>
		<table class="viewExtTable" >

			<tr>
				<td class="TDmainLabel"  style="width:75;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="white-space: nowrap;"><span class="TXTviewTextBox"><%=contr.getIndCivDomicilioFisc()%></span></td>
			
				<td class="TDmainLabel"  style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cap</span></td>
				<td class="TDviewTextBox" style="width:50;"><span class="TXTviewTextBox"><%=contr.getCapDomicilioFisc()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
		<tr>	
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width: 75; white-space: nowrap;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="white-space: nowrap;" ><span class="TXTviewTextBox"><%=contr.getComuneDomilicioFisc()%></span></td>
				
				<td class="TDmainLabel"  style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Provincia</span></td>
				<td class="TDviewTextBox" style="width: 20;white-space: nowrap;"><span class="TXTviewTextBox"><%=contr.getProvinDomicilioFisc()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>	
	<!-- Sede Legale-->
		<td colspan = 1>
		<table class="viewExtTable" >
						<tr><td >&nbsp;</td></TR>
			<tr>
				<td colspan = "4" class="TDmainLabel" align="center"><span class="TXTmainLabel">Sede Legale</span></td>
			</tr>	
			
			</table>
		</td>
	</tr>
	<tr>	
			<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:75;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="white-space: nowrap;"><span class="TXTviewTextBox"><%=contr.getIndCivSedeLegale()%></span></td>

				<td class="TDmainLabel"  style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cap</span></td>
				<td class="TDviewTextBox" style="width:50;"><span class="TXTviewTextBox"><%=contr.getCapSedeLegale()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
		<tr>	
			<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width: 75; white-space: nowrap;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="white-space: nowrap;" ><span class="TXTviewTextBox"><%=contr.getComuneSedeLegale()%></span></td>
				
				<td class="TDmainLabel"  style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Provincia</span></td>
				<td class="TDviewTextBox" style="width:20;"><span class="TXTviewTextBox"><%=contr.getProvinSedeLegale()%></span></td>
			</tr>
		</table>
		</td>
	</tr>	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>	
		<!-- Rappresentate legale-->
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr><td >&nbsp;</td></TR>
			<tr>
				<td class="TDmainLabel" align="center"><span class="TXTmainLabel">Rappresentate Legale</span></td>
			</tr>	

		</table>
		</td>
	</tr>
		<tr>	
		<!-- Rappresentate legale-->
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:75; white-space: nowrap; "><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox" style="width:200;"><span class="TXTviewTextBox"><%=contr.getCognomeRapLegPfis()%></span></td>
				<td class="TDmainLabel"  style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox" style="width:200;"><span class="TXTviewTextBox"><%=contr.getNomeRapLegPfis()%></span></td>
			</tr>
		</table>
		</td>
	</tr>

		<tr>	
		<!-- Rappresentate legale-->
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:75;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="white-space: nowrap;"><span class="TXTviewTextBox"><%=contr.getIndCivRapLegPfis()%></span></td>
				<td class="TDmainLabel"  style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cap</span></td>
				<td class="TDviewTextBox" style="width:50;"><span class="TXTviewTextBox"><%=contr.getCapRapLegPfis()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
		<tr>	
		<!-- Rappresentate legale-->
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width: 75; white-space: nowrap;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="white-space: nowrap;" ><span class="TXTviewTextBox"><%=contr.getComuneResRapLegPfis()%></span></td>
				
				<td class="TDmainLabel"  style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Provincia</span></td>
				<td class="TDviewTextBox" style="width:20;"><span class="TXTviewTextBox"><%=contr.getProvinResRapLeg()%></span></td>
			</tr>
		</table>
		</td>
	</tr>	
	<tr></tr>
	<tr></tr>
	<tr></tr>			
</table>

</td>

</tr>
</table>
		
<div class="tabber">

<% if (contr.getListSrap()!=null && contr.getListSrap().size()>0)
{%>
<div class="tabbertab">
<h2>Rappresentati Legali</h2>

<br>

 <table width='100%'class="extWinTable" cellpadding="0" cellspacing="0">
 <tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Rappresentati Legali</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice ente</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data In Carica</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Carica</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Carica</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Fonte</span></td>		
	</tr>
	
	<% it.escsolution.escwebgis.agenziaEntrate.bean.RapLegale rLegale = new it.escsolution.escwebgis.agenziaEntrate.bean.RapLegale(); %>
	
  <% for (int i=0;i<contr.getListSrap().size();i++) {
        rLegale = (it.escsolution.escwebgis.agenziaEntrate.bean.RapLegale)contr.getListSrap().get(i);%>

    <tr >
		<td class="extWinTDData"  >
		<span class="extWinTXTData"><%= rLegale.getCodEnt() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= rLegale.getDataInizioCarRap() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= rLegale.getDataFineCarRap() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= rLegale.getTipoCarRap() %></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%= rLegale.getFonteRap() %></span></td>
		
		
	
	</tr>
	
<% } %>
</table>
</div>
	<%}%>


<% if (contr.getListPIva()!=null && contr.getListPIva().size()>0)
{%>
<div class="tabbertab">
<h2 >Partite IVA</h2>

<br>

 <table width='60%' class="extWinTable" cellpadding="0" cellspacing="0">
 <tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Partite IVA</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita IVA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Cessazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Motivo Cessazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Confluenza</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.agenziaEntrate.bean.PartitaIVA pIVA = new it.escsolution.escwebgis.agenziaEntrate.bean.PartitaIVA(); %>
	
  <% for (int i=0;i<contr.getListPIva().size();i++) {
        pIVA = (it.escsolution.escwebgis.agenziaEntrate.bean.PartitaIVA)contr.getListPIva().get(i);%>

    <tr >
		<td class="extWinTDData"  >
		<span class="extWinTXTData"><%=pIVA.getPartitaIVA()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=pIVA.getDataCessaPI()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=pIVA.getMotiviCessaPI()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=pIVA.getConfluenzaPI()%></span></td>	
	</tr>
<% } %>
</table>
</div>
	<%}%>


<% if (contr.getListVDom()!=null && contr.getListVDom().size()>0)
{%>
<div class="tabbertab">
<h2>Variazioni Domicilio</h2>

<br>
 <table  width='100%'class="extWinTable" cellpadding="0" cellspacing="0">
 <tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Variazioni Domicilio</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Provincia</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CAP</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data inizio val.</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Fonte</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.agenziaEntrate.bean.VariazioneDomicilio varDom = new it.escsolution.escwebgis.agenziaEntrate.bean.VariazioneDomicilio(); %>
	
  <% for (int i=0;i<contr.getListVDom().size();i++) {
        varDom = (it.escsolution.escwebgis.agenziaEntrate.bean.VariazioneDomicilio)contr.getListVDom().get(i);%>

    <tr >
		<td class="extWinTDData"  >
		<span class="extWinTXTData"><%=varDom.getComuneVDom()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=varDom.getIndCivVDom()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=varDom.getProvinciaVDom()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=varDom.getCapVDom()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=varDom.getDataInizioValVDom()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=varDom.getFonteSDom()%></span></td>	
	</tr>
<% } %>
</table>
</div>
	<%}%>


</div>

<!-- FINE solo dettaglio -->
<% if (contr != null){%>
<% String codice = contr.getPkIdPg();
   
   
   %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="22">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>

<div id="wait" class="cursorWait" />
</body>

</html>