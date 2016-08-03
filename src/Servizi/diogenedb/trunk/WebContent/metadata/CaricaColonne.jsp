<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
		<title>Carica colonne</title>
	</head>
	<script>
		function setFocus(name) {
			document.form.elements['form:focus'].value = name;
		}
		function setFocusAndSubmit(name) {
			setFocus(name);	
			document.form.submit();
		}
		function focusElement() {
			element = document.form.elements['form:focus'].value;
			document.form.elements[element].focus();
		}
		function setValuesFromHidden() {
			//serve per reimpostare correttamente il valore di campi che un actionListener rende visibili da invisibili
			if (document.form.elements['form:descrizioneEstesa'] != undefined) {
				document.form.elements['form:descrizioneEstesa'].value = document.form.elements['form:descrizioneEstesaHidden'].value;
			}
		}
		function setValuesForHidden() {
			//serve per reimpostare correttamente il valore di campi che un actionListener rende visibili da invisibili
			//fa l'inverso della funzione precedente
			document.form.elements['form:descrizioneEstesaHidden'].value = document.form.elements['form:descrizioneEstesa'].value;
		}
		function getNeedsConfirm() {
			if (document.form.elements['form:tableName'].value != document.form.elements['form:oldTableName'].value)
				document.form.elements['form:needsConfirm'].value = "true";
			if (document.form.elements['form:tableDescription'].value != document.form.elements['form:oldTableDescription'].value)
				document.form.elements['form:needsConfirm'].value = "true";				
			if (document.form.elements['form:needsConfirm'].value == "true") {
				return confirm('I dati non salvati saranno persi: continuare?');
			}
			return true;
		}		
		function setNeedsConfirm(newVal) {
			document.form.elements['form:needsConfirm'].value = newVal;
		}
	</script>	
	<body onload="focusElement();setValuesFromHidden();">
	<p class="spacer">&nbsp;</p>
	<div id="clearheader"></div>
		<f:view>
			<h:form id="form">
				<h:inputHidden id="focus" value="#{CaricaColonne.focus}"></h:inputHidden>
				<h:inputHidden id="needsConfirm" value="#{CaricaColonne.needsConfirm}"></h:inputHidden>
				<h:inputHidden id="oldTableName" value="#{CaricaColonne.oldTableName}"></h:inputHidden>
				<h:inputHidden id="oldTableDescription" value="#{CaricaColonne.oldTableDescription}"></h:inputHidden>
				<table width="100%">
					<tr>
						<td style="width:20%" align="right">
							<h:outputText value="Tabella"></h:outputText>
						</td>
						<td style="width:80%">
							<h:outputText value="#{CaricaColonne.selectedTable}" style="width:40%"></h:outputText>
						</td>
					</tr>
					<tr>
						<td align="right"> &nbsp;
						</td>
						<td>
							<h:inputHidden id="tableName" value="#{CaricaColonne.tableName}"></h:inputHidden>
						</td>
					</tr>
					<tr>
						<td align="right">
							<h:outputText value="Descrizione estesa"></h:outputText>
						</td>
						<td>
							<h:inputTextarea id="tableDescription" value="#{CaricaColonne.tableDescription}" style="width:40%"></h:inputTextarea>
						</td>
					</tr>
				</table>
				<table width="100%">
					<tr>						
						<td style="width:40%" align="center">
							<div class="divTableTitle"  style="width:90%;text-align:center;" >													
								<h:outputText value="Colonne tabella"></h:outputText>
							</div>
						</td>
						<td style="width:10%" align="center">
						</td>
						<td style="width:40%" align="center">
							<div class="divTableTitle"  style="width:90%;text-align:center;" >													
								<h:outputText value="Colonne definite nel sistema"></h:outputText>
							</div>
						</td>						
					</tr>
					<tr>
						<td align="center" valign="top">
							<h:selectManyListbox id="selectFromColumns" value="#{CaricaColonne.selValFromColumns}" size="15" style="width:90%">
								<f:selectItems
								    value="#{CaricaColonne.fromColumns}"/>
							</h:selectManyListbox>
						</td>						
						<td align="center" valign="middle">
						<table>
							<tr>
								<td>
									<h:commandButton value="" onfocus="setFocus('form:selectToColumns')" action="submit" actionListener="#{CaricaColonne.spostaADx}" styleClass="pulsante-arrow"></h:commandButton> 														
								</td>
							</tr>
							<tr>
								<td>
									<h:commandButton value="" onfocus="setFocus('form:selectToColumns')" action="submit" actionListener="#{CaricaColonne.spostaASx}" styleClass="pulsante-arrow-return"></h:commandButton><br>								
								</td>
							</tr>
						</table>
							<h:commandButton value="Chiave" action="submit" actionListener="#{CaricaColonne.impostaComeChiave}" styleClass="pulsante"></h:commandButton><br>
						</td>						
						<td align="center" valign="top">
							<table width="100%">
								<tr>
									<td align="center" valign="top">
										<h:selectManyListbox id="selectToColumns" value="#{CaricaColonne.selValToColumns}" size="12" style="width:90%" onchange="setFocusAndSubmit('form:selectToColumns')" immediate="true" valueChangeListener="#{CaricaColonne.selectToColumns}">
											<f:selectItems
											    value="#{CaricaColonne.toColumns}"/>
										</h:selectManyListbox>
									</td>
								</tr>
								<tr>
									<td align="left">
										<h:outputText id="descrizioneEstesaLabel" rendered="#{CaricaColonne.renderDescription}" value="Descrizione" style="width:20%"></h:outputText>
										<h:inputText id="descrizioneEstesa" rendered="#{CaricaColonne.renderDescription}" value="#{CaricaColonne.descrizioneEstesa}" style="width:35%"></h:inputText>
										<h:inputHidden id="descrizioneEstesaHidden" value="#{CaricaColonne.descrizioneEstesa}"></h:inputHidden>
										<h:commandButton id="descrizioneEstesaButton" onclick="setValuesForHidden()" rendered="#{CaricaColonne.renderDescription}" value="Conferma descrizione" onfocus="setFocus('form:selectToColumns')" action="submit" actionListener="#{CaricaColonne.saveDescription}" styleClass="pulsanteXL"></h:commandButton>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>				
				<table width="100%">
					<tr>
						<td align="center">
							<h:commandButton value="Indietro" styleClass="pulsante" onclick="return getNeedsConfirm()" action="#{CaricaColonne.esci}"></h:commandButton>
							<h:commandButton value="Annulla" styleClass="pulsante" onclick="return getNeedsConfirm()" actionListener="#{CaricaColonne.cancella}"></h:commandButton>
							<h:commandButton value="Conferma" styleClass="pulsante" action="#{CaricaColonne.salva}"></h:commandButton>
						</td>
					</tr>								
				</table>
				<h:messages id="errors" style="color: red; font-family: 'New Century Schoolbook', serif;
							font-style: oblique"/>
			</h:form>
		</f:view>
		 <div id="header">
			  <img src="../metadata/img/title_Diogene.png" width="174" height="35" />
			  <img src="../metadata/img/carica-tabelle.png" width="396" height="24" />
		 </div>
		
	</body>
</html>