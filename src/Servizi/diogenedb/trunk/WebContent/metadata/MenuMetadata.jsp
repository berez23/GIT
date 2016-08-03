<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
<head>
<link href="../css/style.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
<title>Menù mappatura</title>
</head>
<body>
<div id="outer">
<p class="spacer">&nbsp;</p>
<div id="clearheader"></div>
<f:view>
	<div id="centrecontent">
	<center>
	<div class="divTableContainer"><h:form id="form">
		<div style="text-align:center;float:left;padding:20px;">
		<div><h:commandButton image="../metadata/img/tabelle-colonne.png"
			value="Carica tabelle/colonne"
			action="#{MenuMetadata.apriCaricaTabelle}" /></div>
		<div><h:outputText value="Carica tabelle/colonne" /></div>
		</div>
		<br>
		<br>
		<div style="text-align:center;float:left;padding:20px;"><h:commandLink
			value="Esci" action="#{MenuMetadata.apriMenu}"></h:commandLink></div>
	</h:form></div>
	</center>
	</div>
</f:view></div>
<div id="header"><img src="../metadata/img/title_Diogene.png"
	width="174" height="35" /></div>

</body>
</html>
