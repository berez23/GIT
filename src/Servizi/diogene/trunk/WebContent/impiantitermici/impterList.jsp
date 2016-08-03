<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%HttpSession sessione = request.getSession(true);

			%>
<%java.util.Vector vlistaCond = (java.util.Vector) sessione.getAttribute("LISTA_IMPTER");

			%>
<%it.escsolution.escwebgis.impiantitermici.bean.ImpTerFinder finder = (it.escsolution.escwebgis.impiantitermici.bean.ImpTerFinder) sessione.getAttribute("FINDER27");

			%>
<%int js_back = 1;
			if (sessione.getAttribute("BACK_JS_COUNT") != null)
				js_back = ((Integer) sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>Impianti Termici - Lista</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
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



function mettiST(){
	document.mainform.ST.value = 2;
}




</script>
<body onload="mettiST()">

<form name="mainform"
	action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ImpiantiTermici"
	target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
&nbsp;<%=funzionalita%>:&nbsp;Elenco Impianti Termici</span></div>

&nbsp;

<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">

	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">OCCUPANTE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO VIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PREF</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME VIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CIVICO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">BARRATO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SCALA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PIANO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INTERNO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">GRUPPO</span></td>

	</tr>

	<%it.escsolution.escwebgis.impiantitermici.bean.ImpTer ter = new it.escsolution.escwebgis.impiantitermici.bean.ImpTer();

			%>
	<%java.util.Enumeration en = vlistaCond.elements();
			int contatore = 1;

			%>
	<%long progressivoRecord = (finder.getPaginaAttuale() - 1) * finder.getRighePerPagina() + 1;

			%>
	<%while (en.hasMoreElements())
			{
				ter = (it.escsolution.escwebgis.impiantitermici.bean.ImpTer) en.nextElement();%>



	<tr id="r<%=contatore%>">
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getOccupante()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getTipo_via()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getPref()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getNome_via()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getCivico()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getBarrato()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getScala()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getPiano()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getInterno()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=ter.getGruppo()%></span></td>

	</tr>

	<%contatore++;
				progressivoRecord++;
			}

			%>

	<input type='hidden' name="ST" value="">
	<input type='hidden' name="OGGETTO_SEL" value="">
	<input type='hidden' name="RECORD_SEL" value="">
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
	<input type='hidden' name="AZIONE" value="">
	<input type='hidden' name="UC" value="27">
	<input type='hidden' name="BACK" value="">
	<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>
