<%@ page language="java" import="it.escsolution.escwebgis.istat.bean.*,it.escsolution.escwebgis.aziende.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.aziende.bean.Azienda az = (it.escsolution.escwebgis.aziende.bean.Azienda) sessione.getAttribute(AziendeAziendeLogic.DETTAGLIO_AZIENDA); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  it.escsolution.escwebgis.soggetto.bean.SoggettoFinder finder = null;

	if (sessione.getAttribute("FINDER16") !=null){
		if (((Object)sessione.getAttribute("FINDER16")).getClass() == new it.escsolution.escwebgis.soggetto.bean.SoggettoFinder().getClass()){
			finder = (it.escsolution.escwebgis.soggetto.bean.SoggettoFinder)sessione.getAttribute("FINDER16");
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

function listaUnitaLocali() {
	wait();
	document.mainform.ST.value = 4;
	document.mainform.submit();
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AziendeAziende" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Azienda</span>
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
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">NREA</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getNREA() == null ? " - " : az.getNREA().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getCF() == null ? " - " : az.getCF().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Ragione Sociale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getRagioneSociale() == null ? " - " : az.getRagioneSociale().toString() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Provincia</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getProvincia() == null ? " - " : az.getProvincia().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getComune() == null ? " - " : az.getComune().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Frazione</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getFrazione() == null ? " - " : az.getFrazione().toString() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel"><%= az.getSedime() == null ? " - " : az.getSedime().toString() %></span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getNomeVia() == null ? " - " : az.getNomeVia().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">n.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getCivico() == null ? " - " : az.getCivico().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">C.A.P.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getCAP() == null ? " - " : az.getCAP().toString() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<% if (az.getCFRappresentante() != null || az.getCognomeRappresentante() != null) { %>
&nbsp;
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan="1">
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td class="TDmainLabel" style="text-align: center;" colspan="6"><span class="TXTmainLabel">RAPPRESENTANTE LEGALE</span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">CODICE FISCALE</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getCFRappresentante() == null ? " - " : az.getCFRappresentante().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getCognomeRappresentante() == null ? " - " : az.getCognomeRappresentante().toString() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getNomeRappresentante() == null ? " - " : az.getNomeRappresentante().toString() %></span></td>
			</tr>
		</table>
		</td>
	</tr>	
</table>
<% } %>
&nbsp;
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan="1">
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td class="TDmainLabel" style="text-align: center;"><span class="TXTmainLabel">DESCRIZIONE CATEGORIA COMMERCIALE</span></td>
			</tr>
			<tr>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= az.getDescrizione() == null ? " - " : az.getDescrizione().toString() %></span></td>
			</tr>
		</table>
		</td>
	</tr>	
</table>

</td>

</tr>
</table>
<br><br><br>

<center><span class="TXTmainLabel"><a href="javascript: listaUnitaLocali();">Visualizza lista Unit&agrave; Locali</a></span></center>

<br><br>

<!-- FINE solo dettaglio -->
<% if (az != null){%>
<% String codice = "";
   codice = az.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="16">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>
<div id="wait" class="cursorWait" />

</body>
</html>