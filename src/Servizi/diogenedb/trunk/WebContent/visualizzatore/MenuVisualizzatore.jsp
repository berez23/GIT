<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
<head>
<link href="../css/style.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
<title>Menù visualizzatore</title>
</head>
<body>
<div id="outer">
<p class="spacer">&nbsp;</p>
<div id="clearheader"></div>
<f:view>
	<div id="centrecontent">
	<center><h:form id="form">
		<h:commandButton  value="Definizione entità visualizzatore"
			action="#{MenuVisualizzatore.apriDefinizioneEntita}" styleClass="pulsanteXXL"></h:commandButton>
		<br>
		<br>
		<h:commandLink value="Esci" action="#{MenuVisualizzatore.apriMenu}"></h:commandLink>
	</h:form>
	</div>
	</center></div>

</f:view>
</div>
<div id="header"><img src="../metadata/img/title_Diogene.png"
	width="174" height="35" /></div>
</body>
</html>
