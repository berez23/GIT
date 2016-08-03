<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<link href="../css/correlazione.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<!-- N.B.: utilizzata nel titolo un'espressione JSP perché non funzionava con h:outputText -->
		<title>Archivio
			<%= ((it.webred.diogene.correlazione.backingbeans.RicercheBB)session.getAttribute("Ricerche")).getRicerche().get(new Integer(request.getParameter("idxRicerca")).intValue()).getArchivi().get(new Integer(request.getParameter("idxArchivio")).intValue()).getName() %>
			-&nbsp;Visualizzazione dati
		</title>
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
							<p style="color:#009300;font-size:16px;">
								<h:outputText value="Visualizzazione dati presenti nell'archivio " />
								<h:outputText style="color:#000000;" value="#{VisualizzaDati.nomeArchivio}" />
								<br>
								<h:outputText value=" consultato per " />
								<h:outputText style="color:#000000;" value="#{VisualizzaDati.parametriRicerca}." />
							</p>
							<h:panelGroup rendered="#{VisualizzaDati.visualizzaMessaggioErrore}">
								<h:outputText style="color:red;font-style:oblique;font-size:20px;" value="Si è verificato un errore nell'esecuzione della ricerca." />
							</h:panelGroup>
							<h:dataTable
								id="dati"
								style="width:50%;margin-top:0px;margin-bottom:10px;border-collapse:collapse;empty-cells:show;"
								binding="#{VisualizzaDati.tabella}"
								var="dato">							
							</h:dataTable>
							<table width="100%" style="margin-top:20px;">
								<tr>
									<td align="center">
										<h:commandLink
											styleClass="linknav"
											action="#{VisualizzaDati.faiRicercaPag}">
												<f:param name="idxRicerca" value="#{VisualizzaDati.idxRicerca}"/>
												<f:param name="idxArchivio" value="#{VisualizzaDati.idxArchivio}"/>
												<f:param name="pagina" value="1"/>
													<h:commandButton value="#{VisualizzaDati.segnoMinore}#{VisualizzaDati.segnoMinore}" styleClass="pulsante"/>
										</h:commandLink>
										<h:commandLink
											styleClass="linknav"
											action="#{VisualizzaDati.faiRicercaPag}">
												<f:param name="idxRicerca" value="#{VisualizzaDati.idxRicerca}"/>
												<f:param name="idxArchivio" value="#{VisualizzaDati.idxArchivio}"/>
												<f:param name="pagina" value="#{VisualizzaDati.paginaIndietro}"/>
													<h:commandButton rendered="#{VisualizzaDati.paginaIndietro > 0}" value="#{VisualizzaDati.segnoMinore}" styleClass="pulsante"/>
										</h:commandLink>
										<h:outputText value="pag. #{VisualizzaDati.pagina} di " />
										<h:outputText value="#{VisualizzaDati.numPagineRicerca}" />
										<h:outputText rendered="#{VisualizzaDati.recordCount != 1}"  value="(totale #{VisualizzaDati.recordCount} righe)" />
										<h:outputText rendered="#{VisualizzaDati.recordCount == 1}"  value="(totale 1 riga)" />
										<h:commandLink
											styleClass="linknav"
											action="#{VisualizzaDati.faiRicercaPag}">
												<f:param name="idxRicerca" value="#{VisualizzaDati.idxRicerca}"/>
												<f:param name="idxArchivio" value="#{VisualizzaDati.idxArchivio}"/>
												<f:param name="pagina" value="#{VisualizzaDati.paginaAvanti}"/>
													<h:commandButton rendered="#{VisualizzaDati.paginaAvanti <= VisualizzaDati.numPagineRicerca}" value="#{VisualizzaDati.segnoMaggiore}" styleClass="pulsante"/>
										</h:commandLink>
										<h:commandLink
											styleClass="linknav"
											action="#{VisualizzaDati.faiRicercaPag}">
												<f:param name="idxRicerca" value="#{VisualizzaDati.idxRicerca}"/>
												<f:param name="idxArchivio" value="#{VisualizzaDati.idxArchivio}"/>
												<f:param name="pagina" value="#{VisualizzaDati.numPagineRicerca}"/>
													<h:commandButton value="#{VisualizzaDati.segnoMaggiore}#{VisualizzaDati.segnoMaggiore}" styleClass="pulsante"/>
										</h:commandLink>
									</td>
								</tr>
							</table>
							<table width="100%" style="margin-top:20px;">
								<tr>
									<td align="center">
										<h:commandButton value="Chiudi" onclick="window.close();return false;" styleClass="pulsante"/>
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
