<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<link href="../css/correlazione.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<title>Lista tipi etichetta</title>
	</head>
	<script>
		function controllaCheck() {
			retVal = false;
			if (document.form.elements['form:numeroTipiEtichetta'].value == 0) {
				alert("Non è presente nessun tipo etichetta per il tema selezionato: impossibile procedere");
				return retVal;
			}
			for (i = 0; i < document.form.elements['form:numeroTipiEtichetta'].value; i++) {
				name = "form:listaTipiEtichetta_" + i + ":selTipoe";
				if (document.form.elements[name].checked) {
					retVal = true;
					break;
				}
			}
			if (!retVal) {
				alert("Non è stato selezionato nessun tipo etichetta");
			}
			return retVal;
		}
		
		var indietroClick;
		function assegnaIndietro(link) {
			if (link.onclick == confirmIndietro) {
				return;
			}
			indietroClick = link.onclick;
			link.onclick = confirmIndietro;
		}
		function confirmIndietro() {
			message = "Questa operazione comporterà la cancellazione dalla memoria di tutte le ricerche eventualmente effettuate finora. Continuare?";
			var conferma = confirm(message);
			if (conferma) {
				return indietroClick();
			} else {
				return false;
			}
		}	
	</script>
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
					<h:inputHidden id="numeroTipiEtichetta" value="#{ListaTipiEtichetta.numeroTipiEtichetta}"></h:inputHidden>
					<div id="centrecontent">
						<center>
							<table style="width:100%">
								<tr>
									<td align="center">
										<h:panelGrid columns="1" align="left" style="margin-left:3%">
											<h:column>
												<h:commandLink title="Torna a selezione tema" styleClass="linknav" onmousedown="assegnaIndietro(this)" action="#{Ricerche.cancellaRicercheETornaATemi}">
													<h:dataTable 
										 				id="listaTemi"
											 			styleClass="griglianavtema" 
											 			style="border-collapse:collapse;empty-cells:show;"
														rowClasses="temaRow"
											 			value="#{ListaTemi.temaSel}" 
											 			var="tema">
														<h:column id="colonna1">
															<f:facet name="header">
																<h:outputText value="Tema" />
															</f:facet>							
															<h:outputText value="#{tema.name}" />		
														</h:column>
													</h:dataTable>
												</h:commandLink>																	
											</h:column>
										</h:panelGrid>
									</td>
								</tr>								
							</table>
							<table width="100%" style="margin-top:20px;">
								<tr>
									<td align="center">
										<p style="color:#009300;font-size:18px;">
											<h:outputText value="Selezionare uno o più tipi etichetta per accedere ai dati presenti negli archivi." />	
										</p>
									</td>
								</tr>								
							</table>
							<h:dataTable 
				 				id="listaTipiEtichetta"
					 			styleClass="griglia" 
					 			style="width:50%;margin-top:20px;margin-bottom:10px;border-collapse:collapse;empty-cells:show;"
					 			rowClasses=",alternateRow"
					 			headerClass="thgrigliacorrel"
					 			columnClasses="colonnalistatipie1,colonnalistatipie2"
					 			value="#{ListaTipiEtichetta.tipiEtichetta}" 
					 			var="tipoEtichetta">
								<h:column id="colonna1">
									<f:facet name="header">
										<h:outputText value="Nome tipo etichetta" />
									</f:facet>							
									<h:outputText value="#{tipoEtichetta.name}" />		
								</h:column>
								<h:column id="colonna2">
									<f:facet name="header">
										<h:outputText value="Seleziona" />
									</f:facet>
									<h:selectBooleanCheckbox id="selTipoe" immediate="false">
									</h:selectBooleanCheckbox>
								</h:column>
							</h:dataTable>
							<table width="100%">
								<tr>
									<td align="center">
										<h:commandButton value="Esci da visualizzazione" rendered="#{ListaTemi.renderedBack}" onclick="return confirmIndietro()" action="#{MenuCorrelazione.apriMenuCorrelazione}" styleClass="pulsanteXXL"/>
										<h:commandButton value="Visualizza archivi consultabili" onclick="return controllaCheck()" action="#{ListaTipiEtichetta.seleziona}" styleClass="pulsanteXXL"/>
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
