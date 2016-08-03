<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<link href="../css/correlazione.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<title>Gestione temi</title>
	</head>
	<script>
		function getNeedsConfirm() {
			if (document.form.elements['form:needsConfirm'].value == "true") {
				return confirm('I dati non salvati saranno persi: continuare?');
			}
			return true;
		}
		function setNeedsConfirm(newVal) {
			document.form.elements['form:needsConfirm'].value = newVal;
		}
		function inserisci() {
			setNeedsConfirm('true');
			document.form.elements['form:navigatorAppName'].value = navigator.appName;
			document.form.elements['form:idTemaSel'].value = "-1";
			return true;
		}
		function confirmAnnullaIns() {
			retVal = confirm('I dati non salvati saranno persi: continuare?');
			if (retVal) {
				setNeedsConfirm('false');
			}
			return retVal;
		}
		function setStyleNuovo() {
			document.getElementById('temaNuovo').style.display = document.form.elements['form:displayNuovo'].value;
		}
		function setSfondoBut() {
			for (i = 0; i < document.form.elements['form:numeroTemi'].value; i++) {
				if (i % 2 == 1) {
					name = "form:listaTemi_" + i + ":modificabut";
					document.form.elements[name].style.backgroundColor = "#CCCCCC";
					name = "form:listaTemi_" + i + ":cancellabut";
					document.form.elements[name].style.backgroundColor = "#CCCCCC";
				}
			}
		}
		function setModifica() {
			if (document.form.elements['form:idTemaSel'].value == "-1") {
				return;
			}
			for (i = 0; i < document.form.elements['form:numeroTemi'].value; i++) {
				id = "form:listaTemi_" + i + ":idTema";
				if (document.form.elements[id].value == document.form.elements['form:idTemaSel'].value) {
					id = "form:listaTemi_" + i + ":namelabel";
					document.getElementById(id).style.display = "none";
					id = "form:listaTemi_" + i + ":nametext";
					document.getElementById(id).style.display = "block";
					id = "form:listaTemi_" + i + ":butmodcan";
					document.getElementById(id).style.display = "none";
					id = "form:listaTemi_" + i + ":butsalann";
					document.getElementById(id).style.display = "block";
				}				
			}
			tds = document.getElementsByTagName("td");
			idx = -1;
			for (i = 0; i < document.form.elements['form:numeroTemi'].value; i++) {
				id = "form:listaTemi_" + i + ":idTema";
				if (document.form.elements[id].value == document.form.elements['form:idTemaSel'].value) {
					idx = i;
					break;
				}
			}
			numColonne = 2;
			contaTd = -1;
			for (i = 0; i < tds.length; i++) {
				td = tds[i];
				if (td.className != "") {
					contaTd++;
					if (contaTd >= (numColonne * idx) && contaTd <= (numColonne * idx) + (numColonne - 1)) {
						td.style.backgroundColor = "#AAFFAA";
					}
				}
			}
		}
		function controllaCampiIns() {
			if (document.form.elements['form:nameNuovo'].value == "") {
				alert("Nome tema non inserito");
				return false;
			}
			return true;
		}
		function controllaCampiMod() {
			name = "";
			for (i = 0; i < document.form.elements['form:numeroTemi'].value; i++) {
				id = "form:listaTemi_" + i + ":idTema";
				if (document.form.elements[id].value == document.form.elements['form:idTemaSel'].value) {
					name = "form:listaTemi_" + i + ":nameMod";
				}				
			}
			if (document.form.elements[name].value == "") {
				alert("Nome tema non inserito");
				return false;
			}
			return true;
		}
		function modifica(idTemaSel) {
			document.form.elements['form:idTemaSel'].value = idTemaSel;
			document.form.elements['form:displayNuovo'].value = "none";
			document.form.elements['form:needsConfirm'].value = "true";
			return true;
		}
		function cancella(idTemaSel) {
			retVal = confirm('Si conferma la cancellazione del tema selezionato?');
			if (retVal) {
				document.form.elements['form:idTemaSel'].value = idTemaSel;
			}
			return retVal;
		}
		function confirmAnnullaMod() {
			retVal = confirm('I dati non salvati saranno persi: continuare?');
			if (retVal) {
				setNeedsConfirm('false');
				document.form.elements['form:idTemaSel'].value = "-1";
			}
			return retVal;
		}
	</script>
	<body onload="setStyleNuovo();setSfondoBut();setModifica()">
		<div id="outer">
			<script>
				//in IE mette troppo spazio!!!!
				if (navigator.appName != "Microsoft Internet Explorer") {
					document.write('<p class="spacer">&nbsp;</p>');
				}
			</script>
			<div id="clearheader">
			</div>
			<f:view>
				<h:form id="form">
					<h:inputHidden id="needsConfirm" value="#{Temi.needsConfirm}"></h:inputHidden>
					<h:inputHidden id="navigatorAppName" value="#{Temi.navigatorAppName}"></h:inputHidden>
					<h:inputHidden id="displayNuovo" value="#{Temi.displayNuovo}"></h:inputHidden>
					<h:inputHidden id="numeroTemi" value="#{Temi.numeroTemi}"></h:inputHidden>
					<h:inputHidden id="idTemaSel" value="#{Temi.idTemaSel}"></h:inputHidden>
					<div id="centrecontent">
						<center>
							<table style="width:100%;border:0px;margin-bottom:10px;">
								<tr>
									<td align="center">
										<h:commandButton value="Inserisci nuovo tema" onclick="return inserisci()" actionListener="#{Temi.inserisci}" styleClass="pulsanteXL"/>
									</td>
								</tr>								
							</table>
							<table id="temaNuovo" name="temaNuovo" class="griglia" style="width:60%;margin-top:0px;margin-bottom:30px;display:none;border-collapse:collapse;empty-cells:show;">
								<tr class="header">
									<td style="width:75%">		
										Nome tema
									</td>
									<td style="width:25%">
									</td>
								</tr>
								<tr>
									<td style="text-align:center;background-color:#AAFFAA">
										<h:inputText id="nameNuovo" value="#{Temi.nameNuovo}" style="width:95%" maxlength="100"></h:inputText>
									</td>
									<td style="background-color:#AAFFAA">
										<h:commandButton value="Salva" onclick="return controllaCampiIns()" action="#{Temi.salvaNuovo}" styleClass="pulsante_sm" style="background-color:#AAFFAA;"/>
										<h:commandButton value="Annulla" onclick="return confirmAnnullaIns()" actionListener="#{Temi.annullaInserimento}" styleClass="pulsante_sm" style="background-color:#AAFFAA;"/>
									</td>
								</tr>
							</table>							
							<h:dataTable 
				 				id="listaTemi" 
					 			styleClass="griglia" 
					 			style="width:60%;margin-top:0px;margin-bottom:10px;border-collapse:collapse;empty-cells:show;"
					 			rowClasses=",alternateRow"
					 			headerClass="thgrigliacorrel"
					 			columnClasses="colonnatemi1,colonnatemi2"
					 			value="#{Temi.temi}" 
					 			var="tema">
								<h:column id="colonna1">
									<f:facet name="header">
										<h:outputText value="Nome tema" />
									</f:facet>
									<h:inputHidden id="idTema" value="#{tema.id}"></h:inputHidden>								
									<h:panelGroup id="namelabel">
										<h:outputLabel value="#{tema.name}" />
									</h:panelGroup>
									<h:panelGroup id="nametext" style="display:none;background-color:#AAFFAA;">
										<h:inputText id="nameMod" style="width:95%" maxlength="100" value="#{tema.name}" />
									</h:panelGroup>		
								</h:column>
								<h:column id="colonna2">
									<f:facet name="header">
										<h:outputText value="" />
									</f:facet>
									<h:panelGroup id="butmodcan">
										<h:commandButton id="modificabut" value="Modifica" onclick="return modifica('#{tema.id}');" actionListener="#{Temi.modifica}" styleClass="pulsante_sm"/>
										<h:outputLabel value=" " />
										<h:commandButton id="cancellabut" value="Cancella" onclick="return cancella('#{tema.id}');" action="#{Temi.cancella}" styleClass="pulsante_sm"/>
									</h:panelGroup>
									<h:panelGroup id="butsalann" style="display:none;background-color:#AAFFAA;">
										<h:commandButton style="background-color:#AAFFAA;" value="Salva" onclick="return controllaCampiMod()" action="#{Temi.salvaMod}" styleClass="pulsante_sm"/>
										<h:outputLabel value=" " />
										<h:commandButton style="background-color:#AAFFAA;" value="Annulla" onclick="return confirmAnnullaMod()" actionListener="#{Temi.annullaModifica}" styleClass="pulsante_sm"/>
									</h:panelGroup>
								</h:column>
							</h:dataTable>							
							<table width="100%">
								<tr>
									<td align="center">
										<h:commandButton value="Indietro" onclick="return getNeedsConfirm()" action="#{Temi.esci}" styleClass="pulsante"/>
									</td>
								</tr>								
							</table>
							<h:messages id="errors" style="color: red; font-family: 'New Century Schoolbook', serif;
										font-style: oblique"/>
						</center>
					</div>
				</h:form>
			</f:view>
		</div>
		<div id="header"><img src="../correlazione/img/title_Diogene.png" width="174" height="35" />
		</div>
	</body>
</html>
