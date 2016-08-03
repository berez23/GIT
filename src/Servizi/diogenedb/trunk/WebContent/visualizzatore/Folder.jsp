<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
		<link type="text/css" rel="stylesheet" href="../../css/visualizzatore.css" />
		<link type="text/css" rel="stylesheet" href="../css/style.css" />
		<title>Gestione folder progetto/classe</title>
	</head>
	<script>
		function closePopup() {
			if (confirm('I dati non salvati saranno persi: continuare?')) {
				this.close();
			}
		}
		function doSubmit() {
			opener.setDoCloseOnSubmit(false);
			document.form.elements['form:popupError'].value = "false";
			form.submit();
			if (opener.getPopup() == undefined)
				opener.setPopup(self);
			//opener.setTimeout("closeOnSubmit()", 2000);
			opener.closeOnSubmit();
			return false;
		}
	</script>
	<body onLoad="self.focus();opener.setDoCloseOnSubmit(true);">
		<f:view>
			<h:form id="form">
				<h:inputHidden id="popupError" value="#{DefinizioneFolder.popupError}"></h:inputHidden>
				<table class="popupheader">
					<tr>
						<td>
							<h:outputText value="#{DefinizioneFolder.title}"/>
						</td>
					</tr>
				</table>
				<table>
					<tr>
						<td colspan = 2>
							<h:outputText value="#{DefinizioneFolder.container}" rendered="#{DefinizioneFolder.renderedContainer}"/>
						</td>
					</tr>
					<tr>
						<td align="right" width="25%">
							<h:outputText value="Nome"/>
						</td>
						<td align="left">
							<h:inputText value="#{DefinizioneFolder.name}" style="width:200px"></h:inputText>
						</td>
					</tr>
				</table>
				<br>
				<table>
					<tr>
						<td align="left">
							<h:commandButton value="Annulla" onclick="closePopup();return false;" styleClass="pulsante"></h:commandButton>
							<h:commandButton value="Salva" onclick="doSubmit();" actionListener="#{DefinizioneFolder.salva}" styleClass="pulsante"></h:commandButton>
						</td>
					</tr>
				</table>
				<h:messages id="errors" style="color: red; font-family: 'New Century Schoolbook', serif;
							font-style: oblique"/>
			</h:form>
		</f:view>
	</body>
</html>

