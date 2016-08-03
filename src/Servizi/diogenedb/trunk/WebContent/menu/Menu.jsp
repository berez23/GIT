<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html><f:view>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />		
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
		<title>Diogene Data Browser</title>
		<script>
		<!--
		function startEntitiesCreation()
		{
			var xWidth = window.screen.availWidth;
			var xHeight = window.screen.availHeight;
			var features = "screenX=0,screenY=0,left=0,top=0,location=no,menubar=no,status=no,resizable=no,width=" + xWidth + ",height=" + xHeight;
			var popup = window.open('<h:outputText escape="false" value="#{facesContext.externalContext.requestContextPath}/faces" />/querybuilder/inizio.jsp','EntitiesCreation',features,false);
			popup.resizeTo(xWidth,xHeight); // PER IE, CHE CONSIDERA LE DIMENSIONI PASSATE COME FEATURES DI widnow.open() COME DIMENSIONI DELLA CLIENT AREA, E NON COME DIMENSIONI DELLA FINESTRA TUTTA
		}
		function startRelationsCreation()
		{
			var xWidth = window.screen.availWidth;
			var xHeight = window.screen.availHeight;
			var features = "scrollbars=yes,screenX=0,screenY=0,left=0,top=0,location=no,menubar=no,status=no,resizable=no,width=" + xWidth + ",height=" + xHeight;
			var popup = window.open('<h:outputText escape="false" value="#{facesContext.externalContext.requestContextPath}/faces" />/querybuilder/entitiesList.jsp','EntitiesListCreation',features,false);
			popup.resizeTo(xWidth,xHeight); // PER IE, CHE CONSIDERA LE DIMENSIONI PASSATE COME FEATURES DI widnow.open() COME DIMENSIONI DELLA CLIENT AREA, E NON COME DIMENSIONI DELLA FINESTRA TUTTA
		}
		function logon()
		{
			document.location='logout-redirect.secure';
		}
		-->
		</script>
	</head>
	<body>
		<p class="spacer">&nbsp;</p>
		<div id="clearheader"></div>
<div id="centrecontent"> 
	<center>
	<div class="divTableContainer">	
		<h:form id="form">
	<div style="text-align:center;float:left;padding:20px;">		
			<div><a  href="../../navigatore/search.jsp"><img border="0" src="../img/monitor.png"/></a></div>
			<div>Visualizzatore</div>						
	</div>
	<div style="text-align:center;float:left;padding:20px;">
		<div><a  href="../visualizzatore/DefinizioneEntita.jsp"><img border="0" src="../img/monitor-build.png"/></a></div>
			<div>Definizione entità visualizzatore</div>
					
	</div>				
	<div style="text-align:center;float:left;padding:20px;">			
			<div>
			<h:commandButton 
				image="../img/toolbox.png" 
				value="" 
				
				onclick="startRelationsCreation();"
				actionListener=""
			/>
			</div>
			<div><h:outputText value="Gestione Entità (in finestra separata)" /> </div>
				
	</div>
	<div style="text-align:center;float:left;padding:20px;">
		<div><a  href="../correlazione/MenuCorrelazione.jsp"><img border="0" src="../img/correlazione.png"/></a></div>
			<div>Confronto dati per etichette</div>
					
	</div>	
		</h:form>
			</div>	
	</center>  
</div>	
		
		 <div id="header">
			  <img src="../metadata/img/title_Diogene.png" width="174" height="35" />
			  <img src="../img/text-menu.png" width="107" height="24" />
			  
		 </div>

	</body>
</f:view></html>