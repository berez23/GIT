<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.webred.it/faces" prefix="webred" %>

<html>
	<f:loadBundle basename="it.webred.diogene.querybuilder.labels" var="labels"/>
	<f:view>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
			<meta http-equiv="Cache-Control" content="no-cache" />
			<link type="text/css" rel="stylesheet" href="../../css/querybuilder.css" />
			<title><h:outputText value="#{labels['entities.start.title']}" /></title>
			<script language="JavaScript" src="../../scripts/querybuilder.js">
			</script>		
			</head>
		<body id="paggina" oncontextmenu="return false;">
			<script>
			<!--
			//               BACKSP   F1          F2          F3          F4          F5          F6          F7          F8          F9          F10         F11         F12          CTRL +  F, G, R
			disabilitaTasti([[8,true],[112,false],[113,false],[114,false],[115,false],[116,false],[117,false],[118,false],[119,false],[120,false],[121,false],[122,false],[123,false]],       [70,71,82]);
			//
			-->
			</script>
			<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
				<h:form id="startEntitiesCreation">
					<p>
						<h:inputHidden id="stepCount" value="#{entitiesBb.stepCount}" />
						<h:message 
							for="stepCount" 
							styleClass="greatErrorMessage"
							/>
					</p>
					<p>
						<h:commandButton 
							id="invalidateSessionButton"
							value="INVALIDA LA SESSIONE"
							action="#{entitiesBb.invalidateSession}"
							immediate="true"
							/>
					</p>
					<p>
						<h:commandLink
							id="startLifeButton"
							action="#{entitiesBb.startLife}"
							value="#{labels.common.startEntitiesCreation}"
							styleClass="greatCommandLink"
							immediate="true"
							/>
					</p>
					<p>
						<h:selectOneListbox 
							id="availableUserEntitiesList"
							value="#{entitiesBb.userEntityToEdit}"
							required="true"
							>
							<f:selectItems value="#{entitiesBb.availableUserEntities}"/>
						</h:selectOneListbox>
						<h:commandButton 
							value="#{labels.common.editUserEntity}"
							action="#{entitiesBb.editUserEntity}"
							id="editUserEntityButton"
							/>
					</p>
				</h:form>
				<h:messages 
					styleClass="greatErrorMessage"				
					/>
			</div>
		</body>
	</f:view>
</html>

