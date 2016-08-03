<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%HttpSession sessione = request.getSession(true);

			%>
<%java.util.Vector vlistaCond = (java.util.Vector) sessione.getAttribute("LISTA_VERSAMENTI");

			%>
<%it.escsolution.escwebgis.tributi.bean.VersamentiFinder finder = (it.escsolution.escwebgis.tributi.bean.VersamentiFinder) sessione.getAttribute("FINDER26");

			%>
<%int js_back = 1;
			if (sessione.getAttribute("BACK_JS_COUNT") != null)
				js_back = ((Integer) sessione.getAttribute("BACK_JS_COUNT")).intValue();

			java.util.Vector vctLink = null;
			if (sessione.getAttribute("LISTA_INTERFACCE") != null)
			{
				vctLink = ((java.util.Vector) sessione.getAttribute("LISTA_INTERFACCE"));
			}
%>

<html>
<head>
<title>Versamenti - Lista</title>
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
	action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Versamenti"
	target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
Lista Versamenti</span></div>

&nbsp;
<table bgcolor="white">
	<tr style="background-color: white;">
	<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
		<table class="extWinTable" cellpadding="0" cellspacing="0">

			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CODI FISC</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA
				VERSAMENTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP TERRENI
				AGRI</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP AREE
				FABBR</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP PRIMA
				CASA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP FABBR</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP DETRAZ</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP TOTALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO PAGA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>
			</tr>

			<%it.escsolution.escwebgis.tributi.bean.Versamenti ver = new it.escsolution.escwebgis.tributi.bean.Versamenti();

			%>
			<%java.util.Enumeration en = vlistaCond.elements();
			int contatore = 1;

			%>
			<%long progressivoRecord = (finder.getPaginaAttuale() - 1) * finder.getRighePerPagina() + 1;

			%>
			<%while (en.hasMoreElements())
			{
				ver = (it.escsolution.escwebgis.tributi.bean.Versamenti) en.nextElement();%>



			<tr id="r<%=contatore%>">
				<td class="extWinTDData"><span class="extWinTXTData"><%=ver.getCodfisc()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=ver.getAnno()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=ver.getData_vers()%></span></td>
				<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_terreni_agri()%></span></td>
				<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_aree_fabbr()%></span></td>
				<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_prima_casa()%></span></td>
				<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_fabbr()%></span></td>
				<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_detraz()%></span></td>
				<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_totale()%></span></td>
				<td align="center" class="extWinTDData"><span class="extWinTXTData"><%=ver.getTipo_paga()%></span></td>
				<td align="center" class="extWinTDData"><span class="extWinTXTData"><%=ver.getProvenienza()%></span></td>

			</tr>
				<%contatore++;
				progressivoRecord++;
			}%>
		</table>
		</td>
		<%if (vctLink != null && vctLink.size() > 0)
			{

				%>
		<td class="iFrameLink">
		<!--iframe name="linkIFrame"
			src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/frame/iFrameLink.jsp"
			frameborder="0" scrolling="no" style="width: 100%;"></iframe-->
			<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
		</td>
		<%}

		%>
	</tr>

</table>
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="26">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</form>
<div id="wait" class="cursorWait" />
</body>
</html>
