<%@ page language="java" import="it.escsolution.escwebgis.istat.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.condomini.bean.Condomini cond=(it.escsolution.escwebgis.condomini.bean.Condomini)sessione.getAttribute("CONDOMINIO"); %>
<% java.util.Vector vlistaCond=(java.util.Vector)sessione.getAttribute("LISTA_CONDOMINI_MULTIPLI"); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  it.escsolution.escwebgis.condomini.bean.CondominiFinder finder = null;

	if (sessione.getAttribute("FINDER17") !=null){
		if (((Object)sessione.getAttribute("FINDER17")).getClass() == new it.escsolution.escwebgis.condomini.bean.CondominiFinder().getClass()){
			finder = (it.escsolution.escwebgis.condomini.bean.CondominiFinder)sessione.getAttribute("FINDER17");
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
<title>Condomini Condomini - Dettaglio</title>
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

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CondominiCondomini" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Condominio</span>
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

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Denominazione</span></td>
				<td class="TDviewTextBox" style="width:270;"><span class="TXTviewTextBox"><%=cond.getDenominazione()%></span></td>
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
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Codice Via</span></td>
				<td class="TDviewTextBox" style="width:270;"><span class="TXTviewTextBox"><%=cond.getCodvia()%></span></td>			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>
		<td colspan = 2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="width:180;"><span class="TXTviewTextBox"><%=cond.getIndirizzo()%></span></td>
			
				<td class="TDmainLabel"  style="width:50;"><span class="TXTmainLabel">Civico</span></td>
				<td class="TDviewTextBox" style="width:13;"><span class="TXTviewTextBox"><%=cond.getCivico()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
		


		</table>
</td>

</tr>
</table>

<div class="tabber">
					
					
			
<% if (vlistaCond != null && vlistaCond.size() > 0) {%>
	<div class="tabbertab">
			
			<h2>Condomini</h2>

   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Condomini</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">ESPCIV</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSETAR</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">RIDUZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CARICOARR</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cessato</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sospeso</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod. Fisc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">P. IVA</span></td>
		

	</tr>
	
	<% it.escsolution.escwebgis.condomini.bean.Condomini condL = new it.escsolution.escwebgis.condomini.bean.Condomini(); %>
	<% java.util.Enumeration en = vlistaCond.elements(); int contatore = 1; %>
  
  <% while (en.hasMoreElements()) {
        condL = (it.escsolution.escwebgis.condomini.bean.Condomini)en.nextElement();%>

    <tr >
		<td class="extWinTDData"  >
		<span class="extWinTXTData"><%=condL.getEspciv()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=condL.getClassetar()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=condL.getRiduzione()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=condL.getSuperf()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=condL.getCaricoarr()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=condL.getCessato()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=condL.getSospeso()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=condL.getCodiceFiscale()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=condL.getPartitaIva()%></span></td>	
	</tr>
<% } %>
</table>

</div>

<%} %>

	


<!-- FINE solo dettaglio -->
<% if (cond != null){%>
<% String codice = "";
   codice =cond.getCodctb().equals("-")?"-1":cond.getCodctb();
 			   codice+="@"+cond.getDenominazione()+"@"+cond.getCodvia()+"@"+cond.getIndirizzo()+"@"; 
 			   codice+=cond.getCivico().equals("-")?"-1":cond.getCivico();
   
   
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
		<input type='hidden' name="UC" value="17">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>

<div id="wait" class="cursorWait" />

</body>
</html>