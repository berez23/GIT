<%@ page language="java" import="it.escsolution.escwebgis.modellof24.bean.*, it.escsolution.escwebgis.modellof24.logic.*, it.escsolution.escwebgis.modellof24.servlet.* " %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaModelli = (java.util.Vector) sessione.getAttribute(ModelloF24Logic.LISTA_MODELLIF24); %>
<% ModelloF24Finder finder = (ModelloF24Finder) sessione.getAttribute(ModelloF24Servlet.NOMEFINDER); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  

<html>
<head>
<title>Modelli F24 - Lista</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>

function chgtr(row,flg)
		{
		if (flg==1)
			{
			document.all("r"+row).style.backgroundColor = "#d7d7d7";
			}
		else
			{
			document.all("r"+row).style.backgroundColor = "";
			}
		}


function vai(codice, record_cliccato){
	document.mainform.OGGETTO_SEL.value=codice;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}

function mettiST(){
	document.mainform.ST.value = 2;
}


<% if (vlistaModelli.size() > 0){
	ModelloF24 lc = (ModelloF24) vlistaModelli.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=lc.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ModelloF24" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Lista Modelli F24</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FORN.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROGR. FORN.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA RIP.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROGR. RIP.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA BONIFICO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROGR. DELEGA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROGR. RIGA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME/DENOMINAZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
	</tr>
	
	<% ModelloF24 f24; %>
  <% java.util.Enumeration en = vlistaModelli.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        f24 = (ModelloF24) en.nextElement();%>

    <tr id="r<%=contatore%>" onclick="vai('<%=f24.getChiave()%>', '<%=progressivoRecord%>')">
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getFornituraData() %></span></td>
		<td align="right" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getFornituraProgressivo() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getRipartizioneData() %></span></td>
		<td align="right" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getRipartizioneProgressivo() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getBonificoData() %></span></td>
		<td align="right" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getProgressivoDelega() %></span></td>
		<td align="right" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getProgressivoRiga() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getCdFiscaleContribuente() %></span></td>
		<td nowrap class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getCognomeDenominazione() %></span></td>
		<td nowrap class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= f24.getNome() %></span></td>
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="33">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>