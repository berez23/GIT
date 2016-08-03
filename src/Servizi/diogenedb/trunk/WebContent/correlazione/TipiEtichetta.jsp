<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<link href="../css/correlazione.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<title>Gestione tipi etichetta</title>
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
			document.form.elements['form:idTipoEtichettaSel'].value = "-1";
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
			document.getElementById('tipoEtichettaNuovo').style.display = document.form.elements['form:displayNuovo'].value;
		}
		function setSfondoBut() {
			for (i = 0; i < document.form.elements['form:numeroTipiEtichetta'].value; i++) {
				if (i % 2 == 1) {
					name = "form:listaTipiEtichetta_" + i + ":modificabut";
					document.form.elements[name].style.backgroundColor = "#CCCCCC";
					name = "form:listaTipiEtichetta_" + i + ":cancellabut";
					document.form.elements[name].style.backgroundColor = "#CCCCCC";
				}
			}
		}
		function setModifica() {
			if (document.form.elements['form:idTipoEtichettaSel'].value == "-1") {
				return;
			}
			for (i = 0; i < document.form.elements['form:numeroTipiEtichetta'].value; i++) {
				id = "form:listaTipiEtichetta_" + i + ":idTipoEtichetta";
				if (document.form.elements[id].value == document.form.elements['form:idTipoEtichettaSel'].value) {
					id = "form:listaTipiEtichetta_" + i + ":namelabel";
					document.getElementById(id).style.display = "none";
					id = "form:listaTipiEtichetta_" + i + ":nametext";
					document.getElementById(id).style.display = "block";
					id = "form:listaTipiEtichetta_" + i + ":temalabel";
					document.getElementById(id).style.display = "none";
					id = "form:listaTipiEtichetta_" + i + ":temacombo";
					document.getElementById(id).style.display = "block";
					id = "form:listaTipiEtichetta_" + i + ":butmodcan";
					document.getElementById(id).style.display = "none";
					id = "form:listaTipiEtichetta_" + i + ":butsalann";
					document.getElementById(id).style.display = "block";
				}				
			}
			tds = document.getElementsByTagName("td");
			idx = -1;
			for (i = 0; i < document.form.elements['form:numeroTipiEtichetta'].value; i++) {
				id = "form:listaTipiEtichetta_" + i + ":idTipoEtichetta";
				if (document.form.elements[id].value == document.form.elements['form:idTipoEtichettaSel'].value) {
					idx = i;
					break;
				}
			}
			numColonne = 3;
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
				alert("Nome tipo etichetta non inserito");
				return false;
			}
			return true;
		}
		function controllaCampiMod() {
			name = "";
			for (i = 0; i < document.form.elements['form:numeroTipiEtichetta'].value; i++) {
				id = "form:listaTipiEtichetta_" + i + ":idTipoEtichetta";
				if (document.form.elements[id].value == document.form.elements['form:idTipoEtichettaSel'].value) {
					name = "form:listaTipiEtichetta_" + i + ":nameMod";
				}				
			}
			if (document.form.elements[name].value == "") {
				alert("Nome tipo etichetta non inserito");
				return false;
			}
			return true;
		}
		function filtra() {
			document.form.elements['form:navigatorAppName'].value = navigator.appName;
			document.form.elements['form:idTipoEtichettaSel'].value = "-1";
			document.form.elements['form:displayNuovo'].value = "none";
			document.form.submit();
		}		
		function modifica(idTipoEtichettaSel) {
			document.form.elements['form:idTipoEtichettaSel'].value = idTipoEtichettaSel;
			document.form.elements['form:displayNuovo'].value = "none";
			document.form.elements['form:needsConfirm'].value = "true";
			return true;
		}
		function cancella(idTipoEtichettaSel) {
			retVal = confirm('Si conferma la cancellazione del tipo etichetta selezionato?');
			if (retVal) {
				document.form.elements['form:idTipoEtichettaSel'].value = idTipoEtichettaSel;
			}
			return retVal;
		}
		function confirmAnnullaMod() {
			retVal = confirm('I dati non salvati saranno persi: continuare?');
			if (retVal) {
				setNeedsConfirm('false');
				document.form.elements['form:idTipoEtichettaSel'].value = "-1";
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
					<h:inputHidden id="needsConfirm" value="#{TipiEtichetta.needsConfirm}"></h:inputHidden>
					<h:inputHidden id="navigatorAppName" value="#{TipiEtichetta.navigatorAppName}"></h:inputHidden>
					<h:inputHidden id="displayNuovo" value="#{TipiEtichetta.displayNuovo}"></h:inputHidden>
					<h:inputHidden id="numeroTipiEtichetta" value="#{TipiEtichetta.numeroTipiEtichetta}"></h:inputHidden>
					<h:inputHidden id="idTipoEtichettaSel" value="#{TipiEtichetta.idTipoEtichettaSel}"></h:inputHidden>
					<div id="centrecontent">
						<center>
							<table style="width:100%;border:0px;margin-bottom:10px;">
								<tr>
									<td align="center">
										<h:commandButton value="Inserisci nuovo tipo etichetta" onclick="return inserisci()" actionListener="#{TipiEtichetta.inserisci}" styleClass="pulsanteXXL"/>
									</td>
								</tr>								
							</table>													
							<table id="tipoEtichettaNuovo" name="tipoEtichettaNuovo" class="griglia" style="width:60%;margin-top:0px;margin-bottom:30px;display:none;border-collapse:collapse;empty-cells:show;">
								<tr class="header">
									<td style="width:45%">		
										Nome tipo etichetta
									</td>
									<td style="width:30%">		
										Tema
									</td>
									<td style="width:25%">
									</td>
								</tr>
								<tr>
									<td style="text-align:center;background-color:#AAFFAA">
										<h:inputText id="nameNuovo" value="#{TipiEtichetta.nameNuovo}" style="width:95%" maxlength="100"></h:inputText>
									</td>
									<td style="text-align:center;background-color:#AAFFAA">		
										<h:selectOneListbox id="temaNuovo" value="#{TipiEtichetta.idTemaNuovo}" size="1" style="width:95%" immediate="false">
											<f:selectItems
											    value="#{TipiEtichetta.temi}"/>
										</h:selectOneListbox>
									</td>
									<td style="background-color:#AAFFAA">
										<h:commandButton value="Salva" onclick="return controllaCampiIns()" action="#{TipiEtichetta.salvaNuovo}" styleClass="pulsante_sm" style="background-color:#AAFFAA;"/>
										<h:commandButton value="Annulla" onclick="return confirmAnnullaIns()" actionListener="#{TipiEtichetta.annullaInserimento}" styleClass="pulsante_sm" style="background-color:#AAFFAA;"/>
									</td>
								</tr>
							</table>
							<table style="width:60%;border:0px;margin-top:0px;margin-bottom:10px;">
								<tr class="header">
									<td style="width:10%">		
										Tema
									</td>
									<td style="width:90%">
										<h:selectOneListbox id="temaFiltro" value="#{TipiEtichetta.idTemaFiltro}" size="1" style="width:50%" immediate="true" valueChangeListener="#{TipiEtichetta.filtra}" onchange="filtra()">
											<f:selectItems
											    value="#{TipiEtichetta.temi}"/>
										</h:selectOneListbox>
									</td>
								</tr>
							</table>						
							<h:dataTable 
				 				id="listaTipiEtichetta"
					 			styleClass="griglia" 
					 			style="width:60%;margin-top:0px;margin-bottom:10px;border-collapse:collapse;empty-cells:show;"
					 			rowClasses=",alternateRow"
					 			headerClass="thgrigliacorrel"
					 			columnClasses="colonnatipoe1,colonnatipoe2,colonnatipoe3"
					 			value="#{TipiEtichetta.tipiEtichetta}" 
					 			var="tipoEtichetta">
								<h:column id="colonna1">
									<f:facet name="header">
										<h:outputText value="Nome tipo etichetta" />
									</f:facet>
									<h:inputHidden id="idTipoEtichetta" value="#{tipoEtichetta.id}"></h:inputHidden>								
									<h:panelGroup id="namelabel">
										<h:outputLabel value="#{tipoEtichetta.name}" />
									</h:panelGroup>
									<h:panelGroup id="nametext" style="display:none;background-color:#AAFFAA;">
										<h:inputText id="nameMod" style="width:95%" maxlength="100" value="#{tipoEtichetta.name}" />
									</h:panelGroup>		
								</h:column>
								<h:column id="colonna2">
									<f:facet name="header">
										<h:outputText value="Tema" />
									</f:facet>								
									<h:panelGroup id="temalabel">
										<h:outputLabel value="#{tipoEtichetta.nameTema}" />
									</h:panelGroup>
									<h:panelGroup id="temacombo" style="display:none;background-color:#AAFFAA;">
										<h:selectOneListbox value="#{tipoEtichetta.fkDvTema}" size="1" style="width:95%" immediate="false">
											<f:selectItems
											    value="#{TipiEtichetta.temi}"/>
										</h:selectOneListbox>
									</h:panelGroup>		
								</h:column>
								<h:column id="colonna3">
									<f:facet name="header">
										<h:outputText value="" />
									</f:facet>
									<h:panelGroup id="butmodcan">
										<h:commandButton id="modificabut" value="Modifica" onclick="return modifica('#{tipoEtichetta.id}');" actionListener="#{TipiEtichetta.modifica}" styleClass="pulsante_sm"/>
										<h:outputLabel value=" " />
										<h:commandButton id="cancellabut" value="Cancella" onclick="return cancella('#{tipoEtichetta.id}');" action="#{TipiEtichetta.cancella}" styleClass="pulsante_sm"/>
									</h:panelGroup>
									<h:panelGroup id="butsalann" style="display:none;background-color:#AAFFAA;">
										<h:commandButton style="background-color:#AAFFAA;" value="Salva" onclick="return controllaCampiMod()" action="#{TipiEtichetta.salvaMod}" styleClass="pulsante_sm"/>
										<h:outputLabel value=" " />
										<h:commandButton style="background-color:#AAFFAA;" value="Annulla" onclick="return confirmAnnullaMod()" actionListener="#{TipiEtichetta.annullaModifica}" styleClass="pulsante_sm"/>
									</h:panelGroup>
								</h:column>
							</h:dataTable>							
							<table width="100%">
								<tr>
									<td align="center">
										<h:commandButton value="Indietro" onclick="return getNeedsConfirm()" action="#{TipiEtichetta.esci}" styleClass="pulsante"/>
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
