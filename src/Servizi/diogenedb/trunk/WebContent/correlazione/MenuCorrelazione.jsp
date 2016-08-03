<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<title>Menù correlazione</title>		
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
				<div id="centrecontent">
					<center>
						<div class="divTableContainer" style="width:60%">
							<h:form id="form">
							<div style="text-align:center;float:left;padding:20px;">
								<div>
									<h:commandButton image="../correlazione/img/temi.png"
													value="Gestione temi"
													action="#{MenuCorrelazione.apriGestioneTemi}"
													rendered="#{MenuCorrelazione.autGestioneTemi}" />
								</div>
								<div>
									<h:outputText value="Gestione temi" rendered="#{MenuCorrelazione.autGestioneTemi}" />
								</div>
							</div>
							<div style="text-align:center;float:left;padding:20px;">
								<div>
									<h:commandButton image="../correlazione/img/tipi_etichetta.png"
													value="Gestione tipi etichetta"
													action="#{MenuCorrelazione.apriGestioneTipiEtichetta}"
													rendered="#{MenuCorrelazione.autGestioneTipiEtichetta}" />
								</div>
								<div>
									<h:outputText value="Gestione tipi etichetta" rendered="#{MenuCorrelazione.autGestioneTipiEtichetta}" />
								</div>
							</div>
							<div style="text-align:center;float:left;padding:20px;">
								<div>
									<h:commandButton image="../correlazione/img/visualizzazione.png"
													value="Visualizzazione dati da archivi"
													action="#{MenuCorrelazione.apriVisualizzazione}" />
								</div>
								<div>
									<h:outputText value="Visualizzazione dati da archivi" />
								</div>
							</div>
							<br>
							<br>
							<div style="text-align:center;float:left;padding:20px;">
								<h:commandLink value="Esci" action="#{MenuCorrelazione.apriMenu}"></h:commandLink>
							</div>
							</h:form>
						</div>
					</center>
				</div>
			</f:view>
		</div>
		<div id="header"><img src="../correlazione/img/title_Diogene.png" width="174" height="35" />
		</div>
	</body>
</html>
