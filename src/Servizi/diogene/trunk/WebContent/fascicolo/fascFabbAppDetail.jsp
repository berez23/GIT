<%@ page language="java"
	import="java.text.SimpleDateFormat, java.text.DecimalFormat, 
			it.escsolution.escwebgis.fascicolo.bean.*, 
		    it.escsolution.escwebgis.fascicolo.logic.*,
		  java.util.ArrayList,java.util.Iterator"%>
<% 					 
	HttpSession sessione = request.getSession(true);
	String url=(String)sessione.getAttribute(FascFabbAppLogic.URL_FASC_FABB); 

	DecimalFormat DF = new DecimalFormat("0.00");
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");

	FascFabbAppFinder finder = null;

	if (sessione.getAttribute(FascFabbAppLogic.FINDER) != null) {
		finder = (FascFabbAppFinder) sessione
				.getAttribute(FascFabbAppLogic.FINDER);
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
<title>Accesso a Fascicolo Fabbricato</title>
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

	function apriFinestra(url){
		window.open(url,'_blank');
	}
</script>

<body >

	<div align="center" class="extWinTDTitle">
		<span class="extWinTXTTitle"> &nbsp;<%=funzionalita%>:&nbsp;Accesso a Fascicolo Fabbricato
		</span>
	</div>

	&nbsp;

	<form name="mainform"
		action="<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/FascFabbApp"
		target="_parent">
	
		
			<%if(url.startsWith("ERR#")){
				url = url.substring(url.indexOf('#')+1); %>
				
				<span class="TXTmainLabel"><%=url%></span>
		
			<% }else{ 
				String[] s = url.split("#");
			%>
					
					<span class="TXTmainLabel">Fascicolo Fabbricato (sez.:<%=s[0]%>, foglio:<%=s[1]%>, particella:<%=s[2]%>) aperto in una nuova finestra.</span>
					<script type="text/javascript">
						apriFinestra("<%=s[3]%>");
					</script>
			<%} %>
	

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
		<input type='hidden' name="UC" value="127">
		<input type='hidden' name="EXT"	value=""> 
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

	</form>

	<div id="wait" class="cursorWait" />
	
		
	
</body>
</html>