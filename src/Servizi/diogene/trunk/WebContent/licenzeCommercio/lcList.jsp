<%@ page language="java" import="it.escsolution.escwebgis.licenzeCommercio.bean.*" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaLicenze = (java.util.Vector) sessione.getAttribute("LISTA_LICENZE"); %>
<% LicenzaCommercioFinder finder = (LicenzaCommercioFinder) sessione.getAttribute("FINDER29"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  

<html>
<head>
<title>Licenze Commercio - Lista</title>
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


<% if (vlistaLicenze.size() > 0){
LicenzaCommercio lc = (LicenzaCommercio) vlistaLicenze.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=lc.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/LicenzeCommercio" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Lista Licenze di Commercio</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM. ESERCIZIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPOLOGIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SEDIME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DESCRIZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM. CIVICO</span></td>
	</tr>
	
	<% LicenzaCommercio lc; %>
  <% java.util.Enumeration en = vlistaLicenze.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        lc = (LicenzaCommercio) en.nextElement();%>

    <tr id="r<%=contatore%>" onclick="vai('<%=lc.getChiave()%>', '<%=progressivoRecord%>')">
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= lc.getNumEsercizio() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= lc.getTipologia() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= lc.getSedime() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= lc.getNomeVia() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= lc.getNumCivico() %></span></td>
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="29">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>