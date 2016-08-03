<%@include file="jspHead.jsp"%>
<dt:timeZone id="tz">Europe/Rome</dt:timeZone><html>
<head><meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<title>MUI Web - Comunicazioni ICI</title>
<script type="text/javascript" language="Javascript">
	var printURL;
	var firstPopupUrl;
</script>
<style type="text/css" media="all">
      @import url("./css/maven-base.css");
      @import url("./css/maven-theme.css");
      @import url("./css/site.css");
    </style>
<link rel="stylesheet" href="./css/print.css" type="text/css"
	media="print" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="author" content="Francesco Ciacca" />
<script language="JavaScript" src="js/popup_calendar.js" type="text/javascript"></script>
<script language="JavaScript" src="js/utils.js" type="text/javascript" ></script>
</head>
<body class="composite">



		
<div id="breadcrumbs">

<div class="xleft">
					<img src="img/vista<%=it.webred.mui.http.MuiApplication.belfiore%>.gif" style="height:90px;" /> 
</div>

	<div class="clear">
	<hr />
	</div>

<security:lock role="mui-adm,mui-supusr,mui-usr">
<div class="xleft" style="display: none;">Versione: v3 20100112
</div>
<div class="xleft">
	<a href='<%= response.encodeURL("./logout-redirect.secure") %>'>logout</a>
</div>
<div class="xright"><dt:format pattern="HH:mm dd/MM/yyyy"
	date="${DB_TIME}" locale="true" timeZone="tz" ></dt:format></div>
<div class="clear">
<hr />
</div>
</div>
<div id="leftColumn">
	<div id="navcolumn">
	<security:lock role="mui-adm,mui-usr,mui-supusr">
		<h5>GESTIONE TRASFERIMENTI</h5>
		<ul>
			<li class="none"><a href="uploadList.jsp">Lista Caricamenti</a></li>
			<security:lock role="mui-adm,mui-supusr">
				<li class="none"><a href="upload.jsp">Carica Forniture</a></li>
				<li class="none"><a href="uploadXML.jsp">Carica Forniture (XML)</a></li>
				<li class="none"><a href="uploadList.jsp?toExport=y">Esporta Forniture</a></li>
			</security:lock>
		</ul>
	</security:lock>
	<security:lock role="mui-adm,mui-usr,mui-supusr">
		<h5>DOCFA GESTIONE TRASFERIMENTI</h5>
		<ul>
			<li class="none"><a href="docfaUploadList.jsp">Lista Caricamenti</a></li>
			<security:lock role="mui-adm,mui-supusr">
				<li class="none"><a href="docfaUploadList.jsp?toExport=y">Esporta Forniture</a></li>
			</security:lock>
		</ul>
	</security:lock>
	<h5>CONSULTAZIONI <br />
	(video e stampa)</h5>
	<ul>
		<li class="none"><a href="noteSearch.jsp">Note di trascrizioni</a></li>
		<li class="none"><a href="comunicazioneSearch.jsp">Comunicazioni</a></li>
	</ul>
	<h5>DOCFA CONSULTAZIONI <br />
	(video e stampa)</h5>
	<ul>
	  	<li class="none"><a href="docfaSearch.jsp">Docfa</a></li>    
		<li class="none"><a href="docfaComunicazioneSearch.jsp">Comunicazioni</a></li>
	</ul>
	<security:lock role="mui-adm">
		<h5>GESTIONE INFORMAZIONI</h5>
		<ul>
			<li class="none"><a href="comunicazioneSearch.jsp">Comunicazioni</a></li>
		</ul>
	</security:lock>
	<security:lock role="mui-adm,mui-supusr">
		<h5>STAMPE SERVIZIO</h5>
		<ul>
			<security:lock role="mui-adm">
				<li class="none"><a href="formPostNoTemplate/importLogList.jsp">Log Caricamento</a></li>
			</security:lock>
			<security:lock role="mui-adm">
				<li class="none"><a href="formPostNoTemplate/uploadList.jsp?toLogInfo=true">Statistiche Caricamento</a></li>
			</security:lock>
			<security:lock role="mui-supusr">
				<li class="none"><a href="formPostNoTemplate/regoleList.jsp">Regole di importazione</a></li>
			</security:lock>
			<security:lock role="mui-supusr">
				<li class="none"><a href="formPostNoTemplate/regoleList.jsp?classe=2">Codici Esito</a></li>
			</security:lock>
			<%-- 
			<li class="none"><a href="#">Log Integrazione</a></li>
			<li class="none"><a href="#">Log Esportazioni</a></li>
			--%>
		</ul>
	</security:lock>
	<security:lock role="mui-adm,mui-supusr">
		<h5>DOCFA STAMPE SERVIZIO</h5>
		<ul>
			<security:lock role="mui-adm">
				<li class="none"><a href="docfaLogSearch.jsp">Log Comunicazioni</a></li>
			</security:lock>
		</ul>
	</security:lock>
	<div align="center">
	<a><img
	        src="./img/HiWeb.jpg"
	        alt="Built by Hiweb" height="31" width="88" /></a>
	</div>
	</div>
	</div>
</security:lock>
<div id="bodyColumn"><c:if test="${!empty param.backTarget}"><a href="${param.backTarget }">${param.backTargetLabel }<c:if test="${empty param.backTargetLabel}">Indietro</c:if></a> </c:if><span id="printbox" style="display: none;">
<a href="javascript:popUp(printURL)" ><img src="img/print_icon.gif" alt="Stampa" title="Stampa" />  Stampa</a>
</span><span id="firstpopupbox" style="display: none;">
<a id="firstpopupboxhref" href="javascript:popUp(firstPopupUrl)" ></a>
</span>

<div id="contentBox">
<c:import url="${main_page}" />
</div>
<c:if test="${empty HIDEFOOTER}">

<div id="finalbox" class="clear">
<hr />
</div>
<div id="footer">
<div class="xright"><a href="http://validator.w3.org/check?uri=referer">
<img
        src="http://www.w3.org/Icons/valid-xhtml10"
        alt="Valid XHTML 1.0 Transitional" height="31" width="88" />
	</a>&#169; HiWeb Srl
    </div>
<div class="clear">
<hr />
</div>
</div>
</c:if>
</div>
<c:if test="${!empty ALERT_MSG}">
<script type="text/javascript" language="Javascript">
	alert('${ALERT_MSG}');
</script>
</c:if>
<%--
<c:if test="${!empty REFRESH_TIMEOUT}">
<script type="text/javascript" language="Javascript">
	doAutoReload(${REFRESH_TIMEOUT});
</script>
</c:if>
--%>
<c:if test="${!empty PRINT_URL}">
<script type="text/javascript" language="Javascript">
	printURL = '${PRINT_URL }';
	document.getElementById("printbox").style.display = "inline";
</script>
</c:if>
<c:if test="${!empty FIRST_POPUP_URL}">
<script type="text/javascript" language="Javascript">
	firstPopupUrl = '${FIRST_POPUP_URL }';
	document.getElementById("firstpopupbox").style.display = "inline";
	document.getElementById("firstpopupboxhref").innerHTML =  '${FIRST_POPUP_DESC }';
</script>
</c:if>
</body>
</html>






