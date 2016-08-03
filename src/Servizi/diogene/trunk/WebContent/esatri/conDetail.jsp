<%@ page language="java" import="it.escsolution.escwebgis.esatri.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.esatri.bean.Contribuente con=(it.escsolution.escwebgis.esatri.bean.Contribuente)sessione.getAttribute(it.escsolution.escwebgis.esatri.logic.EsatriContribuentiLogic.ESATRI_CONTRIBUENTI); %>
<%   java.util.ArrayList lista_con =(java.util.ArrayList)sessione.getAttribute(it.escsolution.escwebgis.esatri.logic.EsatriContribuentiLogic.LISTA_RIS_CON); %>
<%   java.util.ArrayList lista_vio =(java.util.ArrayList)sessione.getAttribute(it.escsolution.escwebgis.esatri.logic.EsatriContribuentiLogic.LISTA_RIS_VIO); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  it.escsolution.escwebgis.esatri.bean.ContribuenteFinder finder = null;

	if (sessione.getAttribute("FINDER38") !=null){
		if (((Object)sessione.getAttribute("FINDER38")).getClass() == new it.escsolution.escwebgis.esatri.bean.ContribuenteFinder().getClass()){
			finder = (it.escsolution.escwebgis.esatri.bean.ContribuenteFinder)sessione.getAttribute("FINDER38");
			}
					
	}
%>

<% 	
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
<title>Esatri Contribuenti - Dettaglio</title>
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
<body >

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/EsatriContribuenti" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dati Contribuente</span>
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
<%//if (con.getCodFornitura() != null){ %>
<%if (con.getFlagTipoPers().equals("PF")){ %>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>		
		<td colspan="3">	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Fonte Dati</span></td>
				<td class="TDviewTextBox" style="width:310;"><span class="TXTviewTextBox"><%=con.getFlagFonteDati()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td colspan="3">	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:310;"><span class="TXTviewTextBox"><%=con.getCF_pI()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=con.getCognome()%></span></td>
			</tr>
		</table>
		</td>
	<td width=10></td>	
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=con.getNome()%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Data Nascita</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=con.getDataNascita()%></span></td>
			</tr>
		</table>
		</td>
		<td width=10></td>
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=con.getIndirizzo()%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
</table>
<%}else{//persona giuridica%>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>		
		<td colspan="3">	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Fonte Dati</span></td>
				<td class="TDviewTextBox" style="width:310;"><span class="TXTviewTextBox"><%=con.getFlagFonteDati()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td colspan="3">	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Partita Iva</span></td>
				<td class="TDviewTextBox" style="width:310;"><span class="TXTviewTextBox"><%=con.getCF_pI()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td colspan="3">	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Denominazione</span></td>
				<td class="TDviewTextBox" style="width:310;"><span class="TXTviewTextBox"><%=con.getDenominazione()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td colspan="3">	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="width:310;"><span class="TXTviewTextBox"><%=con.getIndirizzo()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">CAP</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=con.getCap()%></span></td>
			</tr>
		</table>
		</td>
		<td width=10></td>
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=con.getComune()%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
</table>
<%}
//}%>	

</td>

</tr>
</table>
&nbsp;
<div class="tabber">

<% 	if (lista_con != null && lista_con.size()>0 ){%>

<div class="tabbertab">
<h2>Elenco Versamenti Contabili</h2>
<br>



   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
    <tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Elenco Versamenti Contabili</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FORNITURA(concessione-anno rif.-data scadenza-prog.invio)</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA VERSAMENTO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO VERSATO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO ABITAZ. PRINCIPALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO ALTRI FABBR.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO TERRENI AGRICOLI</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO AREE FABBRICABILI</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO DETRAZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COMUNE IMMOBILI</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM.FABBRICATI</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FLAG ACCONTO/SALDO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FLAG REPERIBILITA'</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PERIODO RIF.</span></td>
	</tr>
	
	<% RiscossioneContabile ris_con = new RiscossioneContabile(); %>
  <% int contatore = 1;%>
  <% for (int i =0;i<lista_con.size();i++) {
        ris_con = (RiscossioneContabile)lista_con.get(i);%>
    	<tr id="r<%=contatore%>">
		
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
		<span class="extWinTXTData"><%=ris_con.getCodConcessione()%>-<%=ris_con.getAnnoRiferimento()%>-<%=ris_con.getDataScadenza()%>-<%=ris_con.getProgressivoInvio()%> </span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ris_con.getDataVersamento()%></span></td>
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_con.getImportoVersato()%></span></td>
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_con.getImportoAbitazPrincipale()%></span></td>	
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_con.getImportoAltriFabbr()%></span></td>
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_con.getImportoTerreniAgricoli()%></span></td>
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_con.getImportoAreeFabbr()%></span></td>
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_con.getImportoDetrazione()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ris_con.getCap()%> - <%=ris_con.getComuneImmobili()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ris_con.getNumFabbr()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ris_con.getFlagAccontoSaldo()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ris_con.getFlagReperibilita()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ris_con.getPerioRifVersamento()%></span></td>
		</tr>
	
<% 		contatore++;
	} 
%>

</table>	
</div>
<% } %>

<% 	if (lista_vio != null && lista_vio.size()>0 ){%>

<div class="tabbertab">
<h2>Elenco Versamenti Violazioni</h2>
<br>
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
   <tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Elenco Versamenti Violazioni</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FORNITURA(concessione-anno rif.-data scadenza-prog.invio)</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA VERSAMENTO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO VERSATO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO IMPOSTA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO SOPRATTASSA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO PENA PECUNIARIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO INTERESSI</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COMUNE IMMOBILI</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FLAG REPERIBILITA'</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PERIODO RIF.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA LIQUIDAZIONE</span></td>
	</tr>
	
	<% RiscossioneViolazione ris_vio = new RiscossioneViolazione(); %>
  <% int conta = 1;%>
  <% for (int i =0;i<lista_vio.size();i++) {
        ris_vio = (RiscossioneViolazione)lista_vio.get(i);%>
    	<tr id="r<%=conta%>">
		
		<td class="extWinTDData" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)'  >
		<span class="extWinTXTData"><%=ris_vio.getCodConcessione()%>-<%=ris_vio.getAnnoRiferimento()%>-<%=ris_vio.getDataScadenza()%>-<%=ris_vio.getProgressivoInvio()%> </span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTData"><%=ris_vio.getDataVersamento()%></span></td>
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_vio.getImportoVersato()%></span></td>
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_vio.getImportoImposta()%></span></td>	
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_vio.getImportoSoprattassa()%></span></td>
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_vio.getImportoPenaPecuniaria()%></span></td>
		<td class="extWinTDDataDx" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTDataDx"><%=ris_vio.getImportoInteressi()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTData"><%=ris_vio.getCap()%> - <%=ris_vio.getComuneImmobili()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTData"><%=ris_vio.getFlagReperib()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTData"><%=ris_vio.getAnnoImpostaVio()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=conta%>,1)' onmouseout='chgtr(<%=conta%>,0)' >
		<span class="extWinTXTData"><%=ris_vio.getDataProvvLiqui()%></span></td>
		</tr>
	
<% 		conta++;
	} 
%>

</table>
</div>
<%} %>

&nbsp;


</div>


<!-- FINE solo dettaglio -->


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
		<input type='hidden' name="UC" value="38">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait" />
</body>
</html>