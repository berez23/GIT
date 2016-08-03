<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<link href="../css/correlazione.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<title>Assegnazione tipi etichetta</title>
		<script>
			function modificaSqlStatementBk() {
				document.form.elements['form:sqlStatementBkLav'].value = document.form.elements['form:sqlStatementBkTextArea'].value;
				document.getElementById('form:sqlStatementBkOutputLabel').style.display = "none";
				document.form.elements['form:sqlStatementBkTextArea'].style.display = "";				
				document.form.elements['form:sqlStatementBkModificaBut'].style.display = "none";
				document.form.elements['form:sqlStatementBkAnnullaBut'].style.display = "";
				document.form.elements['form:sqlStatementBkConfermaBut'].style.display = "";
				document.form.elements['form:salvaBut'].disabled = "true";
			}
			function annullaSqlStatementBk() {
				document.form.elements['form:sqlStatementBkTextArea'].value = document.form.elements['form:sqlStatementBkLav'].value;
				document.getElementById('form:sqlStatementBkOutputLabel').style.display = "";
				document.form.elements['form:sqlStatementBkTextArea'].style.display = "none";				
				document.form.elements['form:sqlStatementBkModificaBut'].style.display = "";
				document.form.elements['form:sqlStatementBkAnnullaBut'].style.display = "none";
				document.form.elements['form:sqlStatementBkConfermaBut'].style.display = "none";
				document.form.elements['form:salvaBut'].disabled = "";
				document.form.elements['form:sqlStatementBkLav'].value = "";
			}
			function confermaSqlStatementBk() {
				document.getElementById('form:sqlStatementBkOutputLabel').innerHTML = document.form.elements['form:sqlStatementBkTextArea'].value;
				if ( document.form.elements['form:sqlStatementBkTextArea'].value != document.form.elements['form:sqlStatementBkInputHidden'].value) {
					document.getElementById('form:sqlStatementBkMsg').style.display = "";
				} else if ( document.form.elements['form:sqlStatementBkTextArea'].value == document.getElementById('form:sqlStatementInputHidden').value) {
					document.getElementById('form:sqlStatementBkMsg').style.display = "none";
				}
				document.getElementById('form:sqlStatementBkOutputLabel').style.display = "";
				document.form.elements['form:sqlStatementBkTextArea'].style.display = "none";				
				document.form.elements['form:sqlStatementBkModificaBut'].style.display = "";
				document.form.elements['form:sqlStatementBkAnnullaBut'].style.display = "none";
				document.form.elements['form:sqlStatementBkConfermaBut'].style.display = "none";
				document.form.elements['form:salvaBut'].disabled = "";
				document.form.elements['form:sqlStatementBkLav'].value = "";
			}
		</script>
	</head>
	<body>
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
					<div id="centrecontent">
						<center>
							<table width="100%">
								<tr>
									<td align="center">
										<h:outputText style="color:#000000;font-size:18px;margin-top:20px;margin-bottom:0px;" value="Configurazione dell'entità" />
										<h:outputText style="color:#009300;font-size:18px;margin-top:20px;margin-bottom:0px;" value="#{entitiesBb.userEntityNameForLabelTypes}" />
									</td>
								</tr>								
							</table>
							<h:panelGrid 
								columns="2"
								columnClasses="colonnaetichetteentitasql1,colonnaetichetteentitasql2"
								style="width:90%;margin-top:20px;margin-bottom:0px;">
								<h:column>
									<h:panelGrid columns="1" style="width:100%;text-align:center;margin-bottom:10px;">
										<h:outputText style="color:#000000;font-size:16px;" value="Assegnazione tipi etichetta" />
									</h:panelGrid>
									<h:panelGrid columns="1" style="width:100%;text-align:center;">																
										<h:dataTable 
							 				id="listaColonne" 
								 			styleClass="griglia" 
								 			style="width:100%;margin-top:0px;margin-bottom:10px;border-collapse:collapse;empty-cells:show;"
								 			rowClasses=",alternateRow"
								 			columnClasses="colonnaetichetteentita1,colonnaetichetteentita2"
								 			value="#{userEntityLabelsBb.columns}"
								 			var="column">
											<h:column id="colonna1">
												<f:facet name="header">
													<h:outputText value="Campo" />
												</f:facet>
												<h:outputLabel value="#{column.description}" />	
											</h:column>
											<h:column id="colonna2">
												<f:facet name="header">
													<h:outputText value="Tipo etichetta" />
												</f:facet>
												<h:selectOneListbox value="#{column.labelType}" size="1" style="width:95%" immediate="false">
													<f:selectItems
													    value="#{userEntityLabelsBb.labelTypes}"/>
												</h:selectOneListbox>
											</h:column>
										</h:dataTable>
									</h:panelGrid>
								</h:column>
								<h:column>
									<h:panelGrid columns="1" style="width:100%;text-align:center;margin-bottom:10px;">
										<h:outputText value="Definizione manuale dell'istruzione SQL" />
									</h:panelGrid>									
									<h:panelGrid id="sqlStatementBkMsg" columns="1" style="display:#{userEntityLabelsBb.sqlStatementBkMsgDisplay};width:100%;text-align:center;color:#930000;font-size:14px;text-align:justify;">
										<h:outputText value="Istruzione SQL già definita manualmente." />
									</h:panelGrid>									
									<h:panelGrid columns="1" style="width:100%;text-align:center;margin-bottom:10px;">
										<h:column>
											<h:inputHidden id="sqlStatementBkInputHidden" value="#{userEntityLabelsBb.sqlStatementBk}" />
											<h:inputHidden id="sqlStatementBkLav" value="" />
											<h:inputHidden id="sqlStatementInputHidden" value="#{userEntityLabelsBb.sqlStatement}" />
											<h:outputLabel id="sqlStatementBkOutputLabel" style="text-align:left;width:90%;font-family:Tahoma,Verdana,Arial,Helvetica,sans-serif;font-size:10pt;" value="#{userEntityLabelsBb.sqlStatementBk}" />
											<h:inputTextarea id="sqlStatementBkTextArea" style="display:none;width:90%;height:350px;font-family:Tahoma,Verdana,Arial,Helvetica,sans-serif;font-size:10pt;" value="#{userEntityLabelsBb.sqlStatementBk}"/>										
										</h:column>
									</h:panelGrid>
									<h:panelGrid columns="1" style="width:100%;text-align:center;margin-bottom:10px;">
										<h:column>
											<h:commandButton id="sqlStatementBkModificaBut" value="Modifica" onclick="modificaSqlStatementBk();return false;" action="" styleClass="pulsante"/>
											<h:commandButton id="sqlStatementBkAnnullaBut" value="Annulla" onclick="annullaSqlStatementBk();return false;" action="" styleClass="pulsante" style="display:none;"/>
											<h:commandButton id="sqlStatementBkConfermaBut" value="Conferma" onclick="confermaSqlStatementBk();return false;" action="" styleClass="pulsante" style="display:none;"/>
										</h:column>									
									</h:panelGrid>
									<h:panelGrid columns="1" style="width:100%;text-align:center;color:#009300;font-size:14px;text-align:justify;">
										<h:outputText value="La definizione manuale dell'istruzione SQL è funzionale al confronto dati per etichette e ha precise regole e limitazioni. 
										Non può essere infatti alterato il numero dei campi in SELECT, né l'alias collegato ad ogni campo, né il numero delle tabelle nella 
										clausola FROM. Possono essere invece inserite concatenazioni di campi nella SELECT o funzioni di tipo DECODE, NVL e simili." />
										<h:outputText value="Ad ogni modifica dell'entità nella pagina relativa, questo valore, se necessario, dovrà essere reimpostato." />
									</h:panelGrid>
								</h:column>
							</h:panelGrid>												
							<table width="100%" style="margin-top:20px;">
								<tr>
									<td align="center">
										<h:commandButton value="Indietro" onclick="return confirm('I dati inseriti e non salvati saranno persi: continuare?')" action="#{userEntityLabelsBb.esci}" styleClass="pulsante"/>
										<h:commandButton id="salvaBut" value="Salva" action="#{userEntityLabelsBb.salva}" styleClass="pulsante"/>
									</td>
								</tr>
							</table>
							<table width="100%" style="margin-top:20px;">
								<tr>
									<td align="center">
										<h:messages id="errors" style="color: red; font-family: 'New Century Schoolbook', serif;
										font-style: oblique"/>
									</td>
								</tr>								
							</table>							
						</center>
					</div>
				</h:form>
			</f:view>
		</div>
		<div id="header"><img src="../correlazione/img/title_Diogene.png" width="174" height="35" />
		</div>
	</body>
</html>
