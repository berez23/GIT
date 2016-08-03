<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
		<link type="text/css" rel="stylesheet" href="../css/style.css" />
		<link type="text/css" rel="stylesheet" href="../../css/visualizzatore.css" />
		<title>Definizione entità visualizzatore</title>
	</head>
	<script>
		var popup;		
		var doCloseOnSubmit = true;
		
		function setDoCloseOnSubmit(flag) {
			doCloseOnSubmit = flag;
		}		
		function openPopup(id, level, mode, popupName) {
			closePopup();
			url = popupName + '?id=' + id + '&level=' + level + '&mode=' + mode + '&doSetPopup=true';			
			features = (popupName == 'Folder.jsp') ? 'left=100,top=100,screenX=100,screenY=100,width=300,height=200'
												: 'left=20,top=20,screenX=20,screenY=20,width=1000,height=560';
			setPopup(window.open(url,'popup',features));
		}
		function closePopup() {
			if (popup != undefined)
				popup.close();
		}
		function closeOnSubmit() {
			if (!doCloseOnSubmit) {
				setTimeout("closeOnSubmit()", 100);
				return;
			}
			if (popup.document.getElementById('form').elements['form:popupError'].value == "false") {
				closePopup();
				form.submit();
			}
		}
		function setPopup(newPopup) {
			popup = newPopup;
		}
		function getPopup() {
			return popup;
		}
		function deleteConfirm() {
			msg = "Si desidera procedere con la cancellazione dell\'elemento selezionato?\n";
			msg += "Se l\'elemento selezionato è un progetto o una classe contenitore, saranno cancellate anche tutte ";
			msg += "le classi in esso contenute.";
			document.form.elements['form:deleteConfirm'].value = confirm(msg);
		}
	</script>
	<body>
		<p class="spacer">&nbsp;</p>
		<div id="clearheader"></div>
		<f:view>
			<h:form id="form">
				<table>
					<tr>
						<td>
							<h:commandButton value="Nuovo progetto" onclick="openPopup('','-2','insert','Folder.jsp'); return false;" styleClass="pulsanteXL"></h:commandButton>		
						</td>
					</tr>
				</table>
				<h:inputHidden id="deleteConfirm" value="#{DefinizioneEntita.deleteConfirm}"/>
				<h:dataTable border="1" cellspacing="0" cellpadding="0" width="100%" value="#{DefinizioneEntita.projectsClassesDM}" rendered="true" var="projectClass" id="projectsClasses"> 
		        	<h:column>
		         		<h:graphicImage style="#{projectClass.indentStyle}" url="blank.gif"></h:graphicImage>       		
		         		<h:commandLink actionListener="#{DefinizioneEntita.expandCollapse}">
		         			<h:graphicImage style="border:0" url="#{projectClass.iconPath}"></h:graphicImage>
		         			<f:param name="projectClassId" value="#{projectClass.id}"/>
		         			<f:param name="projectClassLevel" value="#{projectClass.level}"/>
		         		</h:commandLink>
		         		<h:graphicImage style="width:5px;" url="blank.gif"></h:graphicImage>
		         		<h:commandLink onmousedown="openPopup('#{projectClass.id}','#{projectClass.level}','update','#{projectClass.popupName}'); return false;"
		         			value="#{projectClass.name}" styleClass="treelink"/>
		         	</h:column>
		         	<h:column>
		         		<h:commandLink actionListener="#{DefinizioneEntita.move}" rendered="#{projectClass.upRendered}">
		         			<h:graphicImage style="width:16px;height:16px;border-style:solid;border-width:1px;border-color:black" url="/visualizzatore/img/moveup.png" alt="Muovi su"></h:graphicImage>
		         			<f:param name="projectClassId" value="#{projectClass.id}"/>
		         			<f:param name="projectClassLevel" value="#{projectClass.level}"/>
		         			<f:param name="mode" value="MOVE_UP"/>
		         		</h:commandLink>
		         		<h:graphicImage style="width:18px;height:18px;" rendered="#{!projectClass.upRendered}" url="blank.gif"></h:graphicImage>
		         		<h:graphicImage style="width:3px;" url="blank.gif"></h:graphicImage>
		         		<h:commandLink actionListener="#{DefinizioneEntita.move}" rendered="#{projectClass.downRendered}">
		         			<h:graphicImage style="width:16px;height:16px;border-style:solid;border-width:1px;border-color:black" url="/visualizzatore/img/movedown.png" alt="Muovi giù"></h:graphicImage>
		         			<f:param name="projectClassId" value="#{projectClass.id}"/>
		         			<f:param name="projectClassLevel" value="#{projectClass.level}"/>
		         			<f:param name="mode" value="MOVE_DOWN"/>
		         		</h:commandLink>
		         		<h:graphicImage style="width:18px;height:18px;" rendered="#{!projectClass.downRendered}" url="blank.gif"></h:graphicImage>
		         		<h:graphicImage style="width:3px;" url="blank.gif"></h:graphicImage>	         		
		         		<h:commandLink onmousedown="openPopup('#{projectClass.id}','#{projectClass.level}','insert','Classe.jsp'); return false;"
		         			rendered="#{projectClass.newRendered}">
		         			<h:graphicImage style="width:16px;height:16px;border-style:solid;border-width:1px;border-color:black" url="/visualizzatore/img/new.png" alt="Nuova classe"></h:graphicImage>
		         			<f:param name="projectClassId" value="#{projectClass.id}"/>
		         			<f:param name="projectClassLevel" value="#{projectClass.level}"/>
		         		</h:commandLink>
		         		<h:graphicImage style="width:18px;height:18px;" rendered="#{!projectClass.newRendered}" url="blank.gif"></h:graphicImage>
		         		<h:graphicImage style="width:3px;" url="blank.gif"></h:graphicImage>		         		
		         		<h:commandLink onmousedown="openPopup('#{projectClass.id}','#{projectClass.level}','insert','Folder.jsp'); return false;"
		         			rendered="#{projectClass.newRendered}">
		         			<h:graphicImage style="width:16px;height:16px;border-style:solid;border-width:1px;border-color:black" url="/visualizzatore/img/newfolder.png" alt="Nuova classe contenitore"></h:graphicImage>
		         		</h:commandLink>
		         		<h:graphicImage style="width:18px;height:18px;" rendered="#{!projectClass.newRendered}" url="blank.gif"></h:graphicImage>
		         		<h:graphicImage style="width:3px;" url="blank.gif"></h:graphicImage>	         		
		         		<h:commandLink onclick="deleteConfirm();" actionListener="#{DefinizioneEntita.delete}" rendered="#{projectClass.deleteRendered}">
		         			<h:graphicImage style="width:16px;height:16px;border-style:solid;border-width:1px;border-color:black" url="/visualizzatore/img/delete.png" alt="Cancella"></h:graphicImage>
		         			<f:param name="projectClassId" value="#{projectClass.id}"/>
		         			<f:param name="projectClassLevel" value="#{projectClass.level}"/>
		         		</h:commandLink>
		         		<h:graphicImage style="width:18px;height:18px;" rendered="#{!projectClass.deleteRendered}" url="blank.gif"></h:graphicImage>
		         	</h:column>
      			</h:dataTable>
      			<br>
				<table>
					<tr>
						<td>
							<h:commandButton value="Esci" action="#{DefinizioneEntita.esci}" styleClass="pulsante"></h:commandButton>			
						</td>
					</tr>
				</table>						
				<h:messages id="errors" style="color: red; font-family: 'New Century Schoolbook', serif;
							font-style: oblique"/>
			</h:form>
		</f:view>
		<div id="header">
			 <img src="../metadata/img/title_Diogene.png" width="174" height="35"/>
			 <img src="../visualizzatore/img/def-ent-vis.png" width="570" height="24" />
		</div>
	</body>
</html>