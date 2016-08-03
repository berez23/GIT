<%@ page language="java" import="it.escsolution.escwebgis.istat.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuente contr=(it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuente)sessione.getAttribute("CONTRIBUENTE"); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuentiFinder finder = null;

	if (sessione.getAttribute("FINDER21") !=null){
		if (((Object)sessione.getAttribute("FINDER21")).getClass() == new it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuentiFinder().getClass()){
			finder = (it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuentiFinder)sessione.getAttribute("FINDER21");
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

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AgenziaEntrateContribuenti" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Contribuente</span>
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
	
<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:140;"><span class="TXTviewTextBox"><%=contr.getCodFiscPersona()%></span></td>
				
				<td class="TDmainLabel"  ><span class="TXTmainLabel">Numero Soggetti Trovati</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=contr.getNumeroSoggettiTrovati()%></span></td>
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
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=contr.getCognome()%></span></td>
				
				<td class="TDmainLabel"  style="width:50;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox" style="width:200;"><span class="TXTviewTextBox"><%=contr.getNome()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>	
			
	<tr>	
		<td colspan=1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Data di Nascita</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=contr.getDataNascita()%></span></td>
				<td class="TDmainLabel"  ><span class="TXTmainLabel">Comune di Nascita </span></td>
				<td class="TDviewTextBox" style="width:140;"><span class="TXTviewTextBox"><%=contr.getComuneNascita()%></span></td>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Provincia di Nascita</span></td>
				<td class="TDviewTextBox" style="width:20;"><span class="TXTviewTextBox"><%=contr.getProvinciaNascita()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>		
		
		<td colspan=1>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Deceduto</span></td>
				<td class="TDviewTextBox" style="width:20;"><span class="TXTviewTextBox"><%=contr.getFlagDeceduto()%></span></td>
			</tr>
			
			
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>		
		
		<td colspan=1>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Comune Residenza</span></td>
				<td class="TDviewTextBox" style="width:140;"><span class="TXTviewTextBox"><%=contr.getComuneResidenza()%></span></td>
						
				<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Provincia Residenza</span></td>
				<td class="TDviewTextBox" style="width:20;"><span class="TXTviewTextBox"><%=contr.getProvinciaResidenza()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
		
	<tr></tr>
	<tr></tr>
	<tr></tr>	
			
	<tr>	
		<td colspan=1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="width:350;"><span class="TXTviewTextBox"><%=contr.getIndirizzo()%></span></td>
			
				<td class="TDmainLabel"  style="width:50;"><span class="TXTmainLabel">CAP</span></td>
				<td class="TDviewTextBox" style="width:50;"><span class="TXTviewTextBox"><%=contr.getCap()%></span></td>
			
			</tr>
		</table>
		</td>
				
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>	
			
	<tr>	
		<td colspan=1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Terreni</span></td>
				<td class="TDviewTextBox" style="width:20;"><span class="TXTviewTextBox"><%=contr.getFlagTerreni()%></span></td>
			
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Fabbricati</span></td>
				<td class="TDviewTextBox" style="width:20;"><span class="TXTviewTextBox"><%=contr.getFlagFabbricati()%></span></td>
			
			</tr>
		</table>
		</td>
				
	</tr>	
	
</table>

</td>

</tr>
</table>

		
<div class="tabber">
<% if (contr.getCodFiscLista()!=null && contr.getCodFiscLista().size()>0)
{%>
<div class="tabbertab">
<h2>Codici Fiscali</h2>

<br>

 <table  width='60%'class="extWinTable" cellpadding="0" cellspacing="0">
 	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Codici Fiscali</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice ente</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.agenziaEntrate.bean.CodiceFiscale codFisc = new it.escsolution.escwebgis.agenziaEntrate.bean.CodiceFiscale(); %>
	
  <% for (int i=0;i<contr.getCodFiscLista().size();i++) {
        codFisc = (it.escsolution.escwebgis.agenziaEntrate.bean.CodiceFiscale)contr.getCodFiscLista().get(i);%>

    <tr >
		<td class="extWinTDData"  >
		<span class="extWinTXTData"><%=codFisc.getCodEnt()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=codFisc.getCodiceFiscale()%></span></td>
	</tr>
<% } %>
</table>
</div>
	<%}%>


<% if (contr.getPartIvaLista()!=null && contr.getPartIvaLista().size()>0)
{%>
<div class="tabbertab">
<h2>Partite IVA</h2>

<br>

 <table  width='60%' class="extWinTable" cellpadding="0" cellspacing="0">
 	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Partite IVA</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita IVA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Cessazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Motivo Cessazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Confluenza</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.agenziaEntrate.bean.PartitaIVA pIVA = new it.escsolution.escwebgis.agenziaEntrate.bean.PartitaIVA(); %>
	
  <% for (int i=0;i<contr.getPartIvaLista().size();i++) {
        pIVA = (it.escsolution.escwebgis.agenziaEntrate.bean.PartitaIVA)contr.getPartIvaLista().get(i);%>

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


<% if (contr.getVarResidLista()!=null && contr.getVarResidLista().size()>0 )
{%>
<div class="tabbertab">
<h2>Variazioni Domicilio</h2>

<br>
 <table width='100%'class="extWinTable" cellpadding="0" cellspacing="0">
 	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Variazioni Domicilio</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Provincia</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CAP</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data inizio val.</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data fine val.</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.agenziaEntrate.bean.VariazioneDomicilio varDom = new it.escsolution.escwebgis.agenziaEntrate.bean.VariazioneDomicilio(); %>
	
  <% for (int i=0;i<contr.getVarResidLista().size();i++) {
        varDom = (it.escsolution.escwebgis.agenziaEntrate.bean.VariazioneDomicilio)contr.getVarResidLista().get(i);%>

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
		<span class="extWinTXTData"><%=varDom.getDataFineValVDom()%></span></td>	
	</tr>
<% } %>
</table>
</div>
	<%}%>
	
	</div>


<!-- FINE solo dettaglio -->
<% if (contr != null){%>
<% String codice = contr.getidContribuente();
   
   
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
		<input type='hidden' name="UC" value="21">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>

<div id="wait" class="cursorWait" />
</body>

</html>