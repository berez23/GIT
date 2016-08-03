<%@ page language="java" import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.cncici.bean.*,it.escsolution.escwebgis.cncici.servlet.*,it.escsolution.escwebgis.cncici.logic.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   CncIci cncici = (CncIci) sessione.getAttribute(CncIciLogic.CNCICI); %>


<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  CncIciFinder finder = null;

	if (sessione.getAttribute(CncIciServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(CncIciServlet.NOMEFINDER)).getClass() == new CncIciFinder().getClass()){
			finder = (CncIciFinder)sessione.getAttribute(CncIciServlet.NOMEFINDER);
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

	

	
<%@page import="it.escsolution.escwebgis.cncici.bean.CncIci.CncIciContitolari"%>
<html>
<head>
<title>CNC ICI - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CncIci" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">Dettaglio CncIci</span>
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
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td colspan="4" class="TDmainLabel" style="text-align:center; white-space: nowrap;"><span class="TXTmainLabel">CONTRIBUENTE</span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getContribuenteCognome() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getContribuenteNome() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Sesso</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getContribuenteSesso() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getContribuenteCodiceFiscale() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Comune nascita</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getContribuenteNasComune() %> (<%= cncici.getContribuenteNasProvincia() %>)</span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Nascita</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getContribuenteNasData() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Comune Fiscale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getContribuenteDFComune() %> (<%= cncici.getContribuenteDFProvincia() %>)</span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cap</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getContribuenteDFCap() %></span></td>
			</tr>							
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td colspan=3 class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getContribuenteDFIndirizzo() %></span></td>
		</table>
		</td>
	</tr>
</table>
<br><br><br>

<% 	if (!cncici.getDenuncianteCognome().equals("-") || !cncici.getDenuncianteCodiceFiscale().equals("-")){%>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td colspan="4" class="TDmainLabel" style="text-align:center; white-space: nowrap;"><span class="TXTmainLabel">DENUNCIANTE</span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getDenuncianteCognome() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getDenuncianteCodiceFiscale() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Comune Fiscale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getDenuncianteDFComune() %> (<%= cncici.getDenuncianteDFProvincia() %>)</span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cap</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getDenuncianteDFCap() %></span></td>
			</tr>							
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td colspan=3 class="TDviewTextBox"><span class="TXTviewTextBox"><%= cncici.getDenuncianteDFIndirizzo() %></span></td>
		</table>
		</td>
	</tr>
</table>
<br><br><br>
<%}%>




<% 	if (cncici.getContitolari() != null && cncici.getContitolari().size() > 0){%>
<span class="TXTmainLabel">Lista contitolari</span>
<br>
<table class="extWinTable" style="width: 60%;" align=center>
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COMUNE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVINCIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">MESI POSS.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PERC. POSS.</span></td>
	</tr>			  
<% 
	java.util.Iterator it = cncici.getContitolari().iterator();
	while (it.hasNext()) 
	{
		CncIci.CncIciContitolari cont = (CncIciContitolari) it.next(); 	
		%>

    <tr >
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= cont.getConCodiceFiscale() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= cont.getConDFIndirizzo() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= cont.getConDFComune() %></span></td>
		<td class="extWinTDData" nowrap>
		<span class="extWinTXTData"><%= cont.getConDFProvincia() %></span></td>
		<td class="extWinTDData" align=center>
		<span class="extWinTXTData"><%= cont.getPossMesi() %></span></td>
		<td class="extWinTDData" nowrap>
		<span class="extWinTXTData"><%= cont.getPossPerc() %></span></td>
		
	</tr>			
	<%}%>
</table>
<%}%>




<% 	if (cncici.getImmobili() != null && cncici.getImmobili().size() > 0){%>
<span class="TXTmainLabel">Lista immobili</span>
<br>
<table class="extWinTable" style="width: 60%;" align=center>
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOLGIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUB.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CATEGORIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">RENDITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">MESI POSS.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PERC. POSS.</span></td>
	</tr>			  
<% 
	java.util.Iterator it = cncici.getImmobili().iterator();
	while (it.hasNext()) 
	{
		CncIci.CncIciImmobili cont = (CncIci.CncIciImmobili) it.next(); 	
		%>

    <tr >
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= cont.getFoglio() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= cont.getParticella() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= cont.getSubalterno() %></span></td>
		<td class="extWinTDData" nowrap>
		<span class="extWinTXTData"><%= cont.getCategoria() %></span></td>
		<td class="extWinTDData" align=center>
		<span class="extWinTXTData"><%= cont.getClasse() %></span></td>
		<td class="extWinTDData" nowrap>
		<span class="extWinTXTData"><%= cont.getValoreRendita() %></span></td>
		<td class="extWinTDData" nowrap>
		<span class="extWinTXTData"><%= cont.getIndirizzo() %></span></td>
		<td class="extWinTDData" nowrap>
		<span class="extWinTXTData"><%= cont.getPossessoMesi() %></span></td>
		<td class="extWinTDData" nowrap>
		<span class="extWinTXTData"><%= cont.getPossessoPerc() %></span></td>						
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
<% if (cncici != null){%>
<% String codice = "";
   codice = cncici.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="50">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>
