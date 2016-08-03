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

				function showError()
				{
					var msg = '<h:outputText escape="false" value="#{entitiesBb.globalErrorMessage}" />';
					if (msg)
						alert(msg);
				}
				
				// SALVA LA POSIZIONE DI SCORRIMENTO ORIZZONTALE E 
				// VERTICALE DELA PAGGINA
				function saveScrollPosition()
				{
					if (window.pageXOffset) // Netscape
					{
						document.forms["entitiesBuilderForm"].elements["entitiesBuilderForm:lastXScrollPosition"].value = window.pageXOffset;
						document.forms["entitiesBuilderForm"].elements["entitiesBuilderForm:lastYScrollPosition"].value = window.pageYOffset;
					}
					else if (document.body.scrollTop) // IE
					{
						document.forms["entitiesBuilderForm"].elements["entitiesBuilderForm:lastXScrollPosition"].value = document.body.scrollLeft;
						document.forms["entitiesBuilderForm"].elements["entitiesBuilderForm:lastYScrollPosition"].value = document.body.scrollTop;					
					}
				}
				// RIPRISTINA LA POSIZIONE DI SCORIMENTO
				// PRECEDENTEMNETE SALVATA
				function restoreScrollPosition()
				{
					if (window.scrollTo)
					{
						var x = document.forms["entitiesBuilderForm"].elements["entitiesBuilderForm:lastXScrollPosition"].value;
						var y = document.forms["entitiesBuilderForm"].elements["entitiesBuilderForm:lastYScrollPosition"].value;
						if (x && y)
							window.scrollTo(x, y);
					}
				}
				function copyModifingRelationKey(value)
				{
					document.getElementById("entitiesBuilderForm:modifingRelation").value = value;
				}
				function showSql()
				{
					<h:outputText escape="false" rendered="#{entitiesBb.showSql}" value="document.getElementById('explicitSqlTab').style.visibility = 'visible';" />
					return true;
				}
				function enableUpdateAllButton(){
					var button=document.getElementById(getFormId() + 'updateAllButton');
					if(button!=null){
						button.disabled = false;
					}
				}
				//-->
			</script>
		</head>
		<body onload="showError(); showSql();" oncontextmenu="return false;">
			<script>
			<!--
			//               BACKSP   F1          F2          F3          F4          F5          F6          F7          F8          F9          F10         F11         F12          CTRL +  F, G, R
			disabilitaTasti([[8,true],[112,false],[113,false],[114,false],[115,false],[116,false],[117,false],[118,false],[119,false],[120,false],[121,false],[122,false],[123,false]],       [70,71,82]);
			//-->
			</script>
			<h:form id="entitiesBuilderForm" onsubmit="return viewConfirm();">
				<f:verbatim><div
					id="explicitSqlTab"
					class="helpBox" 
					style="
						visibility: hidden; 
						position: absolute; 
						top: 80px; 
						left: 50px; 
						z-index: 5000;"
					>
					<div
						id="dragBar"
						class="dragBar"
						onmousedown="beginMove(this.parentNode, event)"
						></f:verbatim><h:outputText escape="false" value="#{labels['common.dragMe.label']}" /><f:verbatim></div>
					<input type="button" class="pulsanteDark" value='<h:outputText escape="false" value="#{labels['buildSql.executeSql.button']}" />' onclick="document.forms[1].submit();" />
					<br />
					<input type="button" class="pulsanteDark" value='<h:outputText escape="false" value="#{labels['buildSql.closeSql.button']}" />' onclick="this.parentNode.style.visibility = 'hidden';" />
					<textarea 
						readonly
						style="visibility: inherit;"
						rows="10"
						cols="30"
						</f:verbatim><h:outputText escape="false" value="title=#{labels['common.phrases.toCopyAndPasteSelectTheTextAndPressCTRLplusC']}" /><f:verbatim>
						></f:verbatim>
						<h:outputText	escape="false" value="#{entitiesBb.explicitSql}" />
						<f:verbatim></textarea>
				</div></f:verbatim>
				<div id="HEADER">
					<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
						<h:commandButton 
							id="exitButton" 
							onclick="confirmOn(#{labels['entities.userEntity.exit.confirmationMessage']});"
							value="#{labels['common.words.Exit']}" 
							title="#{labels['common.words.Exit']}" 
							alt="#{labels['common.words.Exit']}" 
							action="#{entitiesBb.startLife}" 
							immediate="true" 
							styleClass="pulsanteDark"							
							/>
						<h:commandButton 
							onclick="confirmOn(#{labels['entities.userEntity.saveUserEntity.confirmationMessage']});"
							id="saveAllButton"
							rendered="#{not entitiesBb.updateStatus}"
							value="#{labels['entities.saveToDB.button']}"
							title="#{labels['entities.saveToDB.button']}"
							alt="#{labels['entities.saveToDB.button']}"
							action="#{entitiesBb.saveUserEntity}"
							styleClass="pulsanteDark"							
							/>
						<h:commandButton 
							onclick="confirmOn(#{labels['entities.userEntity.updateUserEntity.confirmationMessage']});"
							id="updateAllButton"
							rendered="#{entitiesBb.updateStatus}"
							disabled="#{not entitiesBb.modifiedByUser and not entitiesBb.buildedEntity.modifiedByUser}"
							value="#{labels['entities.updateToDB.button']}"
							title="#{labels['entities.updateToDB.button']}"
							alt="#{labels['entities.updateToDB.button']}"
							action="#{entitiesBb.saveUserEntity}"
							styleClass="pulsanteDark"							
							/>
						<h:message for="saveAllButton" styleClass="errorMessage" />
						<h:commandButton 
							onclick="confirmOn(#{labels['entities.userEntity.deleteUserEntity.confirmationMessage']});"
							id="deleteUserEntityButton"
							rendered="#{entitiesBb.updateStatus}"
							value="#{labels['entities.deleteFromDB.button']}"
							title="#{labels['entities.deleteFromDB.button']}"
							alt="#{labels['entities.deleteFromDB.button']}"
							action="#{entitiesBb.deleteUserEntity}"
							styleClass="pulsanteDark"							
							/>
						<h:commandButton 
							id="showSqlButton"
							value="#{labels['entities.showExplicitSql.button']}"
							actionListener="#{entitiesBb.showExplicitSql}"
							styleClass="pulsanteDark"							
							/>
						<h:outputLabel 
							value="#{labels['entities.userEntityName.label']}"
							for="UserEntityName"
							/>
						<h:inputText 
							id="UserEntityName" 
							value="#{entitiesBb.buildedEntity.name}"
							valueChangeListener="#{entitiesBb.nameModified}"
							size="20"
							onchange="enableUpdateAllButton();"
							/>
						<h:message 
							for="UserEntityName" 
							styleClass="errorMessage"
							/>
						<h:outputLabel 
							value="#{labels['entities.userEntityDescription.label']}"
							for="UserEntityDescription"
							/>
						<h:inputText id="UserEntityDescription"	
							size="32"
							value="#{entitiesBb.buildedEntity.description}"
							valueChangeListener="#{entitiesBb.nameModified}"
							onchange="enableUpdateAllButton();"
							/>
					</div>
				</div>
				
				<div id="CONTENT">
					<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
						<div style="margin: auto;">
							<h:inputTextarea 
								id="explicitSqlTextArea"
								value="#{entitiesBb.buildedEntity.explicitSql}"
								rows="40"
								cols="80"
								/>
						</div>
						<h:messages 
							styleClass="errorMessage"
							/>
					</div>
				</div>

				<h:inputHidden id="lastXScrollPosition" value="#{entitiesBb.lastXScrollPosition}" />
				<h:inputHidden id="lastYScrollPosition" value="#{entitiesBb.lastYScrollPosition}" />
				<h:inputHidden id="stepCount" value="#{entitiesBb.stepCount}" />
				<h:inputHidden id="modifingRelation" value="#{entitiesBb.modifingRelation}" />
			</h:form>
			<form name="showResultSetForm" target="QueryBuilderShowResultSetPopUpWindow" method="POST" action="<h:outputText escape='false' value='#{facesContext.externalContext.requestContextPath}/faces/querybuilder/showResultSet.jsp' />">
				<input 
					type="hidden" 
					name="<h:outputText value='#{showRSBb.statementRequestParamName}'/>" 
					value="<h:outputText value='#{entitiesBb.explicitSql}'/>" 
					/>
			</form>
			<div id="wait" />
			<div id="fade" />
		</body>

	</f:view>
</html>

