<%@ page language="java" import="it.escsolution.escwebgis.aziende.logic.*" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.ArrayList vlistaAziende = (java.util.ArrayList) sessione.getAttribute(AziendeAziendeLogic.LISTA_UNITA_LOCALI); %>
<% it.escsolution.escwebgis.aziende.bean.AziendaFinder finder = (it.escsolution.escwebgis.aziende.bean.AziendaFinder)sessione.getAttribute("FINDER16"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  

<html>
<head>
<title>Aziende Aziende - Lista</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<style type="text/css">
.normal
{
	background-image: url("<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/FrecciaGiu.gif");
	background-position: left top;
	background-repeat: no-repeat;
	padding-left: 18px;
	width: 200px;
	overflow: hidden;
	white-space: nowrap;
	cursor: pointer;
}
.extended
{
	background-image: url("<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/FrecciaSu.gif");
	background-position: left top;
	background-repeat: no-repeat;
	padding-left: 18px;
	width: 200px;
	overflow: visible;
	white-space: normal;
	cursor: pointer;
}
</style>
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
	wait();
	document.mainform.OGGETTO_SEL.value=codice;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
}

function toggleZoom(idNum)
{
	if (document.getElementById("DescrCategoria" + idNum).className == "normal")
		document.getElementById("DescrCategoria" + idNum).className = "extended";
	else
		document.getElementById("DescrCategoria" + idNum).className = "normal";
}

function mettiST(){
	document.mainform.ST.value = 2;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AziendeAziende" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Lista Unit&agrave; locali</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Provincia</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Localit&agrave;</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Desrizione Attivit&agrave;</span></td>
	</tr>
	
  <% java.util.Iterator en = vlistaAziende.iterator(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale() - 1) * finder.getRighePerPagina() + 1; %>
  <% while (en.hasNext()) {
        it.escsolution.escwebgis.aziende.bean.UnitaLocale az = (it.escsolution.escwebgis.aziende.bean.UnitaLocale) en.next();%>



    <tr id="r<%=contatore%>" onclick="">
		
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
		<span class="extWinTXTData"><%=az.getProvincia()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
		<span class="extWinTXTData"><%=az.getComune()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
		<span class="extWinTXTData"><%=az.getLocalita()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
		<span class="extWinTXTData"><%=az.getSedime()%>&nbsp;<%=az.getNomeVia()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
		<span class="extWinTXTData"><%=az.getCivico()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style="width: 1%;">
			<div id="DescrCategoria<%=contatore%>" class="normal" onClick="toggleZoom(<%=contatore%>)">
				<span class="extWinTXTData"><%=az.getDescrAttitita()%></span>
			</div>
		</td>
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="16">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>