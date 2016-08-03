<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
		<title>Carica tabelle</title>
		
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
		function getNeedsConfirm() {
			if (document.form.elements['form:needsConfirm'].value == "true") {
				return confirm('I dati non salvati saranno persi: continuare?');
			}
			return true;
		}
		function setNeedsConfirm(newVal) {
			document.form.elements['form:needsConfirm'].value = newVal;
		}
		function toTablesSelected() {
			count = 0;
			if (document.form.elements['form:selectToTables'].options != undefined) {
				for (i = 0; i < document.form.elements['form:selectToTables'].options.length; i++) {
					if (document.form.elements['form:selectToTables'].options[i].selected) {
						count++;
					}
				}
			}
			visibility = (count == 1) ? "visible" : "hidden";
			document.form.elements['form:confButton'].style.visibility = visibility;	
		}		
	</script>
	<body onload="focusElement();toTablesSelected();">
	<p class="spacer">&nbsp;</p>
	<div id="clearheader"></div>
		<f:view>
			<h:form id="form">
				<h:inputHidden id="focus" value="#{CaricaTabelle.focus}"></h:inputHidden>
				<h:inputHidden id="needsConfirm" value="#{CaricaTabelle.needsConfirm}"></h:inputHidden>
				<table width="100%">
					<tr>
						<td style="width:10%" align="right">
							<h:outputText value="Schema"></h:outputText>
						</td>
						<td style="width:90%">
							<h:selectOneListbox id="schema" value="#{CaricaTabelle.selectedSchema}" size="1" style="width:30%"
								valueChangeListener="#{CaricaTabelle.schemaChanged}" onchange="setNeedsConfirm('true');setFocusAndSubmit('form:schema');" immediate="true">
								<f:selectItems
								    value="#{CaricaTabelle.schemas}"/>
							</h:selectOneListbox>
						</td>
					</tr>
				</table>
				<br>
				<table width="100%">
					<tr>
						<td style="width:40%" align="center">
							<div class="divTableTitle"  style="width:90%;text-align:center;" >							
								<h:outputText value="Entità nel database di origine"></h:outputText>
							</div>
						</td>
						<td style="width:10%" align="center">
						</td>
						<td style="width:40%" align="center">
						<div class="divTableTitle"  style="width:90%;text-align:center;" >
							<h:outputText value="Entità definite nel sistema"></h:outputText>
						</div>
						</td>
					</tr>
					<tr>
						<td align="center" valign="top">
							<h:selectManyListbox id="selectFromTables" value="#{CaricaTabelle.selValFromTables}" size="15" style="width:90%" immediate="true">
								<f:selectItems
								    value="#{CaricaTabelle.fromTables}"/>
							</h:selectManyListbox>
						</td>
						<td align="center" valign="middle">
						<table>
							<tr>
								<td>
							       	<h:commandButton value="" disabled="#{CaricaTabelle.disabled}" onfocus="setFocus('form:selectToTables')" action="submit" actionListener="#{CaricaTabelle.spostaADx}"  styleClass="pulsante-arrow"/>								
								</td>
							</tr>
							<tr>
								<td>
									<h:commandButton value="" onfocus="setFocus('form:selectFromTables')" action="submit" actionListener="#{CaricaTabelle.spostaASx}"  styleClass="pulsante-arrow-return"/>								
								</td>
							</tr>
							
						</table>
							<h:commandButton id="confButton" value="Configura" action="#{CaricaTabelle.caricaColonne}"  styleClass="pulsante"/><br>
						</td>
						<td align="center" valign="top">
							<h:selectManyListbox id="selectToTables" value="#{CaricaTabelle.selValToTables}" size="15" style="width:90%" onchange="toTablesSelected()" immediate="true">
								<f:selectItems
								    value="#{CaricaTabelle.toTables}"/>
							</h:selectManyListbox>
						</td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td align="center">
							<h:commandButton value="Indietro" onclick="return getNeedsConfirm()" action="#{CaricaTabelle.esci}"  styleClass="pulsante"/>
							<h:commandButton value="Annulla" onclick="return getNeedsConfirm()" actionListener="#{CaricaTabelle.cancella}" styleClass="pulsante"/>
							<h:commandButton value="Salva" disabled="#{CaricaTabelle.disabled}" action="#{CaricaTabelle.salva}"  styleClass="pulsante"/>
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