<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.webred.it/faces" prefix="webred" %>

<html>
	<f:loadBundle basename="it.webred.diogene.querybuilder.labels" var="labels"/>
	<f:view>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
			<meta http-equiv="pragma" content="no-cache">
			<meta http-equiv="Cache-Control" content="no-cache" />
			<title><h:outputText value="#{labels['entities.title']}" /></title>
			<link type="text/css" rel="stylesheet" href="../../css/dhtml.css" />
			<link type="text/css" rel="stylesheet" href="../../css/querybuilder.css" />
			<link type="text/css" rel="stylesheet" href="../../css/showResultSet.css" />
			<link type="text/css" rel="stylesheet" href="../../css/style.css" />
			<script language="JavaScript" src="../../scripts/dhtml.js">
			</script>
			<script language="JavaScript" src="../../scripts/querybuilder.js">
			</script>
			<script language="JavaScript">
				<!--
				function getFormName()
				{
					return document.forms[0].id;
				}
				function getFormId()
				{
					return getFormName() + ":";
				}
				//-->
			</script>
		</head>
		<body onload="window.resizeTo(window.screen.availWidth - 100, window.screen.availHeight - 100);">
			<script>
			<!--
			//               BACKSP   F1          F2          F3          F4          F5          F6          F7          F8          F9          F10         F11         F12          CTRL +  F, G, R
			disabilitaTasti([[8,true],[112,false],[113,false],[114,false],[115,false],[116,false],[117,false],[118,false],[119,false],[120,false],[121,false],[122,false],[123,false]],       [70,71,82]);
			//-->
			</script>
			<h:form id="showResultSetForm" onsubmit="document.getElementById('wait').style.visibility = 'visible'">
				<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
					<h:inputTextarea 
						value="#{showRSBb.query}"
						rows="12"
						cols="80"
						/>
				</div>
				<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
					<h:commandButton 
						value="#{labels['buildSql.executeSql.button']}"
						actionListener="#{showRSBb.exec}"
						styleClass="pulsante"							
						
						/>
					<f:verbatim>&nbsp;&nbsp;</f:verbatim>
					<h:outputLabel for="chooseMaxRowNum" value="#{labels['entities.showSql.chooseMaxRowNum.label']}" />
					<h:selectOneMenu 
						id="chooseMaxRowNum"
						value="#{showRSBb.maxRowNum}"
						>
						<f:selectItem itemLabel="10" itemValue="10" />
						<f:selectItem itemLabel="25" itemValue="25" />
						<f:selectItem itemLabel="50" itemValue="50" />
						<f:selectItem itemLabel="100" itemValue="100" />
						<f:selectItem itemLabel="200" itemValue="200" />
					</h:selectOneMenu>
				</div>
				<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
					<h:outputText 
						escape="false"
						rendered="#{not empty showRSBb.result}"
						value="#{showRSBb.result}"
						/>
				</div>
			</h:form>
			<div id="wait" />
		</body>

	</f:view>
</html>

