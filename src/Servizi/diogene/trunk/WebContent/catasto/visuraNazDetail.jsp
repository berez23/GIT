<%@ page language="java"
	import="java.text.SimpleDateFormat, java.text.DecimalFormat, 
			it.escsolution.escwebgis.catasto.bean.*, 
		   it.escsolution.escwebgis.catasto.logic.*,
		  java.util.ArrayList,java.util.Iterator"%>
<% 					 
	HttpSession sessione = request.getSession(true);
	
	DecimalFormat DF = new DecimalFormat("0.00");
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");

	VisuraNazionaleFinder finder = null;

	if (sessione.getAttribute(VisuraNazionaleLogic.FINDER) != null) {
		finder = (VisuraNazionaleFinder) sessione
				.getAttribute(VisuraNazionaleLogic.FINDER);
	}

	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT") != null)
		js_back = ((Integer) sessione.getAttribute("BACK_JS_COUNT"))
				.intValue();

	java.util.Vector vctLink = null;
	if (sessione.getAttribute("LISTA_INTERFACCE") != null) {
		vctLink = ((java.util.Vector) sessione
				.getAttribute("LISTA_INTERFACCE"));
	}

	java.lang.String funzionalita = (java.lang.String) sessione
			.getAttribute("FUNZIONALITA");
%>

<html>
<head>
<title>Visura Nazionale - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/style.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/tab.css" type="text/css">
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js"
	language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>
	function mettiST() {
		document.mainform.ST.value = 3;
	}

	function visualizzaDettaglioOggetto(idx, visDett) {
		document.getElementById("rOgg" + idx).style.display = (visDett ? "none"
				: "");
		document.getElementById("rOggDett" + idx).style.display = (visDett ? ""
				: "none");
	}
</script>

<body>

	<div align="center" class="extWinTDTitle">
		<span class="extWinTXTTitle"> &nbsp;<%=funzionalita%>:&nbsp;Dati
			Soggetto
		</span>
	</div>

	&nbsp;
	<input type="Image" ID="UserCommand8" align="MIDDLE"
		src="<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print.gif"
		border="0" vspace="0" hspace="0" alt="Stampa"
		onMouseOver="setbutton('UserCommand8','<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print_Over.gif')"
		onMouseOut="setbutton('UserCommand8','<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print.gif')"
		onMouseDown="setbutton('UserCommand8','<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print_Down.gif')"
		onMouseUp="setbutton('UserCommand8','<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print.gif')"
		onClick="Stampa()">
	<br/>

	<form name="mainform"
		action="<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/VisuraNazionale"
		target="_parent">

	<jsp:include page="visuraNazFragment.jsp"></jsp:include>

		<%
			if (finder != null) {
		%>
		<input type='hidden' name="ACT_PAGE"
			value="<%=finder.getPaginaAttuale()%>">
		<%
			} else {
		%>
		<input type='hidden' name="ACT_PAGE" value="">
		<%
			}
		%>

		<input type='hidden' name="AZIONE" value=""> 
		<input type='hidden' name="ST" value=""> 
		<input type='hidden' name="UC" value="126">
		<input type='hidden' name="EXT"	value=""> 
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

	</form>

	<div id="wait" class="cursorWait" />
</body>
</html>