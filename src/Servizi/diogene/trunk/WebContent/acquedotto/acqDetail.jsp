<%@ page language="java" import="it.escsolution.escwebgis.istat.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.acquedotto.bean.Acquedotto acq = (it.escsolution.escwebgis.acquedotto.bean.Acquedotto) sessione.getAttribute(it.escsolution.escwebgis.acquedotto.logic.AcquedottoAcquedottoLogic.DETTAGLIO_ACQUEDOTTO); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  it.escsolution.escwebgis.acquedotto.bean.AcquedottoFinder finder = null;

	if (sessione.getAttribute(it.escsolution.escwebgis.acquedotto.servlet.AcquedottoAcquedottoServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(it.escsolution.escwebgis.acquedotto.servlet.AcquedottoAcquedottoServlet.NOMEFINDER)).getClass() == new it.escsolution.escwebgis.acquedotto.bean.AcquedottoFinder().getClass()){
			finder = (it.escsolution.escwebgis.acquedotto.bean.AcquedottoFinder)sessione.getAttribute(it.escsolution.escwebgis.acquedotto.servlet.AcquedottoAcquedottoServlet.NOMEFINDER);
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

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AcquedottoAcquedotto" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Acquedotto</span>
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
				<td colspan="8" class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">FORNITURA</span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Nominativo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getNominativo() == null ? "&nbsp;-&nbsp;" : acq.getNominativo().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Codice Fiscale / Partita Iva</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getCfPi() == null ? "&nbsp;-&nbsp;" : acq.getCfPi().toString() %></span></td>
			</tr>	
			<tr>	
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Provincia</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getProvinciaFornitura() == null ? "&nbsp;-&nbsp;" : acq.getProvinciaFornitura().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getComuneFornitura() == null ? "&nbsp;-&nbsp;" : acq.getComuneFornitura().toString() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="white-space: nowrap;"><span class="TXTviewTextBox"><%= acq.getTipologia() == null ? "" : acq.getTipologia().toString() %>&nbsp;<%= acq.getIndirizzoFornitura() == null ? "&nbsp;-&nbsp;" : acq.getIndirizzoFornitura().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Num. civico</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getNumeroCivicoFornitura() == null ? "&nbsp;-&nbsp;" : acq.getNumeroCivicoFornitura().toString() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Num civico part.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getParteCivicoFornitura() == null ? "&nbsp;-&nbsp;" : acq.getParteCivicoFornitura().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">C.A.P.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getCAPFornitura() == null ? "&nbsp;-&nbsp;" : acq.getCAPFornitura().toString() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
&nbsp;
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td colspan="8" class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">SPEDIZIONE</span></td>
			</tr>
			<tr>
				<td colspan="2" class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Nominativo</span></td>
				<td colspan="2" class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getNominativoSpedizione() == null ? "&nbsp;-&nbsp;" : acq.getNominativoSpedizione().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Provincia</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getProvinciaSpedizione() == null ? "&nbsp;-&nbsp;" : acq.getProvinciaSpedizione().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getComuneSpedizione() == null ? "&nbsp;-&nbsp;" : acq.getComuneSpedizione().toString() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="white-space: nowrap;"><span class="TXTviewTextBox"><%= acq.getTipologiaSpedizione() == null ? "" : acq.getTipologiaSpedizione().toString() %>&nbsp;<%= acq.getIndirizzoSpedizione() == null ? "&nbsp;-&nbsp;" : acq.getIndirizzoSpedizione().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Num. civico</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getNumeroCivicoSpedizione() == null ? "&nbsp;-&nbsp;" : acq.getNumeroCivicoSpedizione().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Num civico part.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getParteCivicoSpedizione() == null ? "&nbsp;-&nbsp;" : acq.getParteCivicoSpedizione().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">C.A.P.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= acq.getCAPSpedizione() == null ? "&nbsp;-&nbsp;" : acq.getCAPSpedizione().toString() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</td>

</tr>
</table>
<br><br><br>

<center><span class="extWinTXTTitle"><a href="javascript:"></a></span></center>

<br><br>

<!-- FINE solo dettaglio -->
<% if (acq != null){%>
<% String codice = "";
   codice = acq.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="20">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>