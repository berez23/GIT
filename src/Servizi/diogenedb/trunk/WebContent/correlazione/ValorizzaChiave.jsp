<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<link href="../css/correlazione.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<title>Valorizzazione della chiave</title>
	</head>
	<script>
		function controllaCampi() {
			for (i = 0; i < document.form.elements['form:numeroCampi'].value; i++) {
				name = "form:listaCampi_" + i + ":valoreCampo";
				if (document.form.elements[name].value == "") {
					alert("Devono essere valorizzati tutti i campi");
					return false;
				}
			}
			return true;
		}
		function controllaCheckAltraQuery() {
			retVal = false;
			if (document.form.elements['form:numeroTipiEtichettaAltraQuery'].value == 0) {
				alert("Non è presente nessun tipo etichetta per la ricerca alternativa: impossibile procedere");
				return retVal;
			}
			for (i = 0; i < document.form.elements['form:numeroTipiEtichettaAltraQuery'].value; i++) {
				name = "form:listaCampiAltraQuery_" + i + ":selTipoe";
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
		function controllaCampiAltraQuery() {
			if (!controllaCheckAltraQuery()) {
				return false;
			}
			for (i = 0; i < document.form.elements['form:numeroTipiEtichettaAltraQuery'].value; i++) {
				name = "form:listaCampiAltraQuery_" + i + ":valoreCampoAltraQuery";
				checkBoxName = "form:listaCampiAltraQuery_" + i + ":selTipoe";
				if (document.form.elements[checkBoxName].checked && document.form.elements[name].value == "") {
					alert("Devono essere valorizzati tutti i campi selezionati");
					return false;
				}
			}
			return true;
		}
		function apriPopup() {
			var idxRicerca = document.form.elements["form:idxRicerca"].value;
			for (i = 0; i < document.form.elements['form:numeroPopup'].value; i++) {
				var coeffRid = 0.75; //coefficiente di riduzione della popup rispetto allo schermo
				var scalatura = 25; //margine per scalatura delle popup su entrambi gli assi
				var xWidth = window.screen.availWidth * coeffRid;
				var yHeight = window.screen.availHeight * coeffRid;
				var left = (window.screen.availWidth - xWidth) / 2 + (i * scalatura);
				var top = (window.screen.availHeight - yHeight) / 2 + (i * scalatura);
				var features = "scrollbars=yes,screenX=" + left + ",screenY=" + top + ",left=" + left + ",top=" + top + ",location=no,menubar=no,status=no,resizable=yes,width=" + xWidth + ",height=" + yHeight;
				//var popup = window.open('VisualizzaDati.jsp?pagina=1&idxArchivio=' + i + '&idxRicerca=' + idxRicerca,'VisualizzazioneDatiRicerca_' + i,features,false);
				//per non sovrascrivere le popup eventualmente aperte in precedenza, sostituito da:
				var popup = window.open('VisualizzaDati.jsp?pagina=1&idxArchivio=' + i + '&idxRicerca=' + idxRicerca,'VisualizzazioneDatiRicerca_' + new Date().getTime(),features,false);
				popup.resizeTo(xWidth,yHeight); //solo per Internet Explorer
			}
			document.form.elements['form:numeroPopup'].value = 0;			
		}
		function setSfondoBut() {
			for (i = 0; i < document.form.elements['form:numeroSelArchivi'].value; i++) {
				if (i % 2 == 1) {
					name = "form:listaArchiviSel_" + i + ":altraChiave";
					document.form.elements[name].style.backgroundColor = "#CCCCCC";
				}
			}
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
		
		var indietroArchiviClick;
		var fromButton;
		function setFromButton(value) {
			fromButton = value;
		}
		function assegnaIndietroArchivi(link) {
			if (link.onclick == verificaIndietroArchivi) {
				return;
			}
			indietroArchiviClick = link.onclick;
			link.onclick = verificaIndietroArchivi;
		}
		function verificaIndietroArchivi() {
			if (!fromButton) {
				return indietroArchiviClick();
			} else {
				setFromButton(false);
				//verificare (necessario per Firefox, non necessario per IE)
				if (navigator.appName != "Microsoft Internet Explorer") {
					document.form.submit();
				}
				return true;
			}
		}
		
		function selezionaArchivio(idArchivioAltraQuery) {
			document.form.elements['form:idArchivioAltraQuery'].value = idArchivioAltraQuery;
			document.form.elements['form:ricaricaTipiEtichettaAltraQuery'].value = true;
		}
		
		function verificaOperatore(obj) {
			objId = obj.id;
			index = "-1";
			if (objId.indexOf("form:listaCampiAltraQuery_") > -1)
				index = objId.substring("form:listaCampiAltraQuery_".length, "form:listaCampiAltraQuery_".length + 1);
			else if (objId.indexOf("form:listaCampi_") > -1)
				index = objId.substring("form:listaCampi_".length, "form:listaCampi_".length + 1);
			if(obj.value == 'IS NULL' || obj.value == 'IS NOT NULL') {
				if (objId.indexOf("form:listaCampiAltraQuery_") > -1) {
					if (document.form.elements['form:listaCampiAltraQuery_' + index + ":valoreCampoAltraQuery"].value == "") {
						document.form.elements['form:listaCampiAltraQuery_' + index + ":valoreCampoAltraQuery"].value = "---";
					}					
					document.form.elements['form:listaCampiAltraQuery_' + index + ":valoreCampoAltraQuery"].style.display = "none";
				} else if (objId.indexOf("form:listaCampi_") > -1) {
					if (document.form.elements['form:listaCampi_' + index + ":valoreCampo"].value == "") {
						document.form.elements['form:listaCampi_' + index + ":valoreCampo"].value = "---";
					}					
					document.form.elements['form:listaCampi_' + index + ":valoreCampo"].style.display = "none";
				}
			} else {
				if (objId.indexOf("form:listaCampiAltraQuery_") > -1) {
					if (document.form.elements['form:listaCampiAltraQuery_' + index + ":valoreCampoAltraQuery"].style.display == "none") {
						if (document.form.elements['form:listaCampiAltraQuery_' + index + ":valoreCampoAltraQuery"].value == "---") {
							document.form.elements['form:listaCampiAltraQuery_' + index + ":valoreCampoAltraQuery"].value = "";
						}
						document.form.elements['form:listaCampiAltraQuery_' + index + ":valoreCampoAltraQuery"].style.display = "";
					}				
				} else if (objId.indexOf("form:listaCampi_") > -1) {
					if (document.form.elements['form:listaCampi_' + index + ":valoreCampo"].style.display == "none") {
						if (document.form.elements['form:listaCampi_' + index + ":valoreCampo"].value == "---") {
							document.form.elements['form:listaCampi_' + index + ":valoreCampo"].value = "";
						}						
						document.form.elements['form:listaCampi_' + index + ":valoreCampo"].style.display = "";
					}					
				}
			}
		}
	</script>
	<body onload="setSfondoBut();apriPopup()">
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
					<h:inputHidden id="numeroCampi" value="#{ListaTipiEtichetta.numeroSelTipiEtichetta}"></h:inputHidden>
					<h:inputHidden id="numeroPopup" value="#{Ricerche.numeroPopup}"></h:inputHidden>
					<h:inputHidden id="numeroSelArchivi" value="#{ListaArchivi.numeroSelArchivi}"></h:inputHidden>
					<h:inputHidden id="idArchivioAltraQuery" value="#{ListaArchivi.idArchivioAltraQuery}"></h:inputHidden>
					<h:inputHidden id="numeroTipiEtichettaAltraQuery" value="#{ListaArchivi.numeroTipiEtichettaAltraQuery}"></h:inputHidden>
					<h:inputHidden id="ricaricaTipiEtichettaAltraQuery" value="#{ListaArchivi.ricaricaTipiEtichettaAltraQuery}"></h:inputHidden>
					<h:inputHidden id="idxRicerca" value="#{Ricerche.idxRicerca}"></h:inputHidden>
					<div id="centrecontent">
						<center>							
							<table style="width:100%">
								<tr>
									<td align="left">
										<h:panelGrid columns="1" align="left" style="margin-left:3%;">
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
										<h:panelGrid columns="1" align="left" style="margin-left:10px;">
											<h:column>
												<h:commandLink title="Torna a selezione tipi etichetta" styleClass="linknav" action="#{ListaArchivi.esci}">
													<h:dataTable 
										 				id="listaTipiEtichettaSel"
											 			styleClass="griglianav" 
											 			style="border-collapse:collapse;empty-cells:show;width:100%"
														rowClasses="smallRow,alternateSmallRow"
											 			value="#{ListaTipiEtichetta.selTipiEtichetta}" 
											 			var="tipoEtichettaSel">
														<h:column id="colonna1">
															<f:facet name="header">
																<h:outputText value="Tipi etichetta selezionati" />
															</f:facet>							
															<h:outputText value="#{tipoEtichettaSel.name} (tema #{tipoEtichettaSel.nameTema})" />		
														</h:column>
													</h:dataTable>
													<h:dataTable 
										 				id="listaAltriTipiEtichetta"
											 			styleClass="griglianav" 
											 			style="border-collapse:collapse;empty-cells:show;width:100%"
														rowClasses="smallRow,alternateSmallRow"
											 			value="#{ListaTipiEtichetta.altriTipiEtichetta}" 
											 			var="altroTipoEtichetta"
											 			rendered="#{not empty ListaTipiEtichetta.altriTipiEtichetta}">
														<h:column id="colonna1">
															<f:facet name="header">
																<h:outputText value="Altri tipi etichetta" />
															</f:facet>							
															<h:outputText value="#{altroTipoEtichetta.name} (tema #{altroTipoEtichetta.nameTema})" />		
														</h:column>
													</h:dataTable>
												</h:commandLink>											
											</h:column>
										</h:panelGrid>
										<h:panelGrid columns="1" align="left" style="margin-left:10px;">
											<h:column>												
												<h:commandLink title="Torna a selezione archivi" styleClass="linknav" onmousedown="assegnaIndietroArchivi(this)" action="#{Ricerche.esci}">
													<h:dataTable 
										 				id="listaArchiviSel"
											 			styleClass="griglianav" 
											 			style="border-collapse:collapse;empty-cells:show;width:100%"
														rowClasses="smallRow,alternateSmallRow"
														columnClasses="colonnalistaarchivinav1,colonnalistaarchivinav2"
											 			value="#{ListaArchivi.selArchivi}" 
											 			var="archivioSel">
														<h:column id="colonna1">
															<f:facet name="header">
																<h:outputText value="Archivi selezionati" />
															</f:facet>							
															<h:outputText value="#{archivioSel.name}" />		
														</h:column>
														<h:column id="colonna2">
															<f:facet name="header">
																<h:outputText value="" />
															</f:facet>
															<h:commandButton id="altraChiave" onmousedown="setFromButton(true);selezionaArchivio('#{archivioSel.id}');" value="Cerca per altra chiave" action="#{Ricerche.caricaRicercaAlternativa}" styleClass="pulsante_sm_XL"/>
														</h:column>
													</h:dataTable>
												</h:commandLink>
												<h:commandLink title="Torna a selezione archivi" styleClass="linknav" action="#{Ricerche.esci}">
													<h:dataTable 
										 				id="listaAltriArchivi"
											 			styleClass="griglianav" 
											 			style="border-collapse:collapse;empty-cells:show;width:100%"
														rowClasses="smallRow,alternateSmallRow"
											 			value="#{ListaArchivi.altriArchivi}" 
											 			var="altroArchivio"
											 			rendered="#{not empty ListaArchivi.altriArchivi}">
														<h:column id="colonna1">
															<f:facet name="header">
																<h:outputText value="Altri archivi" />
															</f:facet>							
															<h:outputText value="#{altroArchivio.name}" />		
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
									<td>
										<h:panelGrid columns="1" align="center" style="width:75%;" rendered="#{empty ListaArchivi.archivioAltraQuery}">
											<h:column>
												<h:panelGrid columns="1" style="width:100%;text-align:center;">
													<h:column>
														<h:outputText style="color:#009300;font-size:18px;" value="#{ListaArchivi.etichetta}" />
													</h:column>
												</h:panelGrid>
												<h:panelGrid columns="1" style="width:100%;text-align:center;">
													<h:column>
														<h:outputText style="color:#009300;font-size:14px;text-align:center;"
														value="Eventuali parametri multipli per ricerche per 'è compreso tra', 'non è compreso tra', 'è contenuto in' dovranno essere separati dal carattere #{Ricerche.separaParams}" />
													</h:column>
												</h:panelGrid>												
												<h:panelGrid columns="1" style="width:100%;text-align:center;">
													<h:column>
														<h:dataTable
											 				id="listaCampi"
												 			styleClass="griglia" 
												 			style="width:100%;margin-top:20px;margin-bottom:10px;border-collapse:collapse;empty-cells:show;"
												 			rowClasses=",alternateRow"
												 			headerClass="thgrigliacorrel"
												 			columnClasses="colonnachiave1,colonnachiave2,colonnachiave3"
												 			value="#{ListaTipiEtichetta.selTipiEtichetta}" 
												 			var="campo">
															<h:column id="colonna1">
																<f:facet name="header">
																	<h:outputText value="Tipo etichetta" />
																</f:facet>							
																<h:outputText value="#{campo.name}" />
																<h:outputText value=" (#{campo.formato})" rendered="#{not empty campo.formato}" style="font-size:8pt;"/>
															</h:column>
															<h:column id="colonna2">
																<f:facet name="header">
																	<h:outputText value="Operatore" />
																</f:facet>							
																<h:selectOneListbox id="operatore" value="#{campo.operatore}" size="1" style="width:95%" onchange="verificaOperatore(this)" immediate="false">
																	<f:selectItems
																	    value="#{Ricerche.operatori}"/>
																</h:selectOneListbox>
															</h:column>
															<h:column id="colonna3">
																<f:facet name="header">
																	<h:outputText value="Valore" />
																</f:facet>
																<h:inputText id="valoreCampo" value="#{campo.chiave}" style="width:95%">
																</h:inputText>
															</h:column>
														</h:dataTable>
													</h:column>
												</h:panelGrid>																	
											</h:column>
										</h:panelGrid>
										<h:panelGrid columns="1" align="center" style="width:75%" rendered="#{not empty ListaArchivi.archivioAltraQuery}">
											<h:column>
												<h:panelGrid columns="1" style="width:100%;text-align:center;">
													<h:column>
														<h:outputText style="color:#009300;font-size:14px;text-align:center;" value="Ricerca alternativa nell'archivio " />
														<h:outputText style="color:#009300;font-size:18px;text-align:center;" value="#{ListaArchivi.archivioAltraQuery.name}." />
													</h:column>
												</h:panelGrid>
												<h:panelGrid columns="1" style="width:100%;text-align:center;">
													<h:column>
														<h:outputText style="color:#009300;font-size:14px;text-align:center;"
														value="Eventuali parametri multipli per ricerche per 'è compreso tra', 'non è compreso tra', 'è contenuto in' dovranno essere separati dal carattere #{Ricerche.separaParams}" />
													</h:column>
												</h:panelGrid>
												<h:panelGrid columns="1" style="width:100%;text-align:center;">
													<h:column>
														<h:dataTable
											 				id="listaCampiAltraQuery"
												 			styleClass="grigliaaltraquery" 
												 			style="width:100%;margin-top:5px;margin-bottom:5px;border-collapse:collapse;empty-cells:show;"
												 			rowClasses="smallRow,alternateSmallRow"
												 			headerClass="thgrigliacorrel"
												 			columnClasses="colonnachiavealtraquery1,colonnachiavealtraquery2,colonnachiavealtraquery3,colonnachiavealtraquery4"
												 			value="#{ListaArchivi.tipiEtichettaAltraQuery}" 
												 			var="campoAltraQuery">
												 			<h:column id="colonna1">
																<f:facet name="header">
																	<h:outputText value="Sel." />
																</f:facet>							
																<h:selectBooleanCheckbox id="selTipoe" value="#{campoAltraQuery.selezionato}" immediate="false">
																</h:selectBooleanCheckbox>
															</h:column>
															<h:column id="colonna2">
																<f:facet name="header">
																	<h:outputText value="Tipo etichetta" />
																</f:facet>							
																<h:outputText value="#{campoAltraQuery.name}" />
																<h:outputText value=" (#{campoAltraQuery.formato})" rendered="#{not empty campoAltraQuery.formato}" style="font-size:7pt;"/>
															</h:column>
															<h:column id="colonna3">
																<f:facet name="header">
																	<h:outputText value="Operatore" />
																</f:facet>							
																<h:selectOneListbox id="operatoreAltraQuery" value="#{campoAltraQuery.operatore}" size="1" style="width:95%" onchange="verificaOperatore(this)" immediate="false">
																	<f:selectItems
																	    value="#{Ricerche.operatori}"/>
																</h:selectOneListbox>
															</h:column>
															<h:column id="colonna4">
																<f:facet name="header">
																	<h:outputText value="Valore" />
																</f:facet>
																<h:inputText id="valoreCampoAltraQuery" value="#{campoAltraQuery.chiave}" style="width:100%;font-size:8pt;">
																</h:inputText>
															</h:column>
														</h:dataTable>
													</h:column>
												</h:panelGrid>
												<h:panelGrid columns="1" style="width:100%;text-align:center;">
													<h:column>
														<h:commandButton value="Ricerca" onclick="return controllaCampiAltraQuery()" action="#{Ricerche.faiRicercaAlternativa}" styleClass="pulsante"/>
														<h:commandButton value="Chiudi" action="#{Ricerche.chiudiRicercaAlternativa}" styleClass="pulsante"/>
													</h:column>
												</h:panelGrid>																	
											</h:column>
										</h:panelGrid>
									</td>
								</tr>								
							</table>
							<table width="100%" style="margin-top:10px;">
								<tr>
									<td align="center">
										<h:commandButton value="Esci da visualizzazione" rendered="#{ListaTemi.renderedBack}" onclick="return confirmIndietro()" action="#{MenuCorrelazione.apriMenuCorrelazione}" styleClass="pulsanteXXL"/>
										<h:commandButton value="Ricerca" rendered="#{empty ListaArchivi.archivioAltraQuery}" onclick="return controllaCampi()" action="#{Ricerche.faiRicerca}" styleClass="pulsante"/>
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
