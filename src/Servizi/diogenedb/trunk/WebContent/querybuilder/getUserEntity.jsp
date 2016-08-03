<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<link href="../css/correlazione.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<title>Acquisizione entità</title>
	</head>
	<script>
		function verificaUrl() {
			if (document.form.elements['form:serverUrl'].value == "") {
				alert("Indirizzo URL del server non inserito");
				return false;
			}
			return true;
		}
		function confermaAcq() {
			for (i = 0; i < document.form.elements['form:entitiesNamesSize'].value; i++) {
				name = "form:listaEntitiesNames_" + i + ":tabellaDestinazione";
				if (document.form.elements[name].value == "") {
					alert("Inserire tutti i nomi delle tabelle di destinazione");
					return false;
				}
			}
			return confirm("Si vuole effettuare l'acquisizione dell'entità selezionata?");
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
					<div id="centrecontent">
						<center>
							<table width="100%">
								<tr>
									<td align="center">
										<h:outputText style="color:#009300;font-size:18px;margin-top:20px;margin-bottom:0px;" value="Acquisizione di un'entità" />
									</td>
								</tr>								
							</table>
							<table width="70%" style="margin-top:40px;">
								<tr style="vertical-align:top;">
									<td align="left" width=30%>
										<h:outputText style="color:#000000;font-size:16px;" value="Indirizzo URL del server" />
									</td>
									<td align="left" width=60%>
										<h:inputText style="width:95%" id="serverUrl" value="#{getUserEntityBb.serverUrl}" />
									</td>
									<td align="left" width=10%>
										<h:commandButton value="Connetti" onclick="return verificaUrl()" action="#{getUserEntityBb.connetti}" styleClass="pulsante"/>
									</td>
								</tr>
								<tr style="height:50px;vertical-align:top;">
									<td align="left" colspan=3>
										<h:outputText style="color:#000000;font-size:12px;" value="(esempio di formato: http://localhost:8080/diogenedb)" />
									</td>
								</tr>
								<tr style="height:40px;vertical-align:top;">
									<td align="left">
										<h:outputText style="color:#000000;font-size:16px;" value="Entità disponibili" />
									</td>
									<td align="left">
										<h:selectOneListbox immediate="true" value="#{getUserEntityBb.selectedEntityId}" size="1" style="width:95%"
											valueChangeListener="#{getUserEntityBb.entityChanged}" onchange="document.form.submit()">
											<f:selectItems
											    value="#{getUserEntityBb.comboEntities}"/>
										</h:selectOneListbox>
									</td>
									<td>
									</td>
								</tr>
							</table>
							<h:inputHidden id="entitiesNamesSize" value="#{getUserEntityBb.entitiesNamesSize}" />
							<h:dataTable 
					 			id="listaEntitiesNames"
					 			styleClass="griglia" 
					 			style="width:50%;margin-top:20px;"
					 			value="#{getUserEntityBb.entitiesNames}" 
					 			var="item"
					 			rendered="#{not empty getUserEntityBb.entitiesNames}"
					 			cellpadding="0" cellspacing="0">
								<h:column>
									<f:facet name="header">
										<h:outputText id="origine" style="FONT-WEIGHT: bold;" value="Schema.tabella origine" />
									</f:facet>
									<h:outputText value="#{item.entityFromName}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="destinazione" style="FONT-WEIGHT: bold;" value="Schema.tabella destinazione" />
									</f:facet>
									<h:inputText id="tabellaDestinazione" style="width:95%" value="#{item.entityToName}" />
								</h:column>
							</h:dataTable>						
							<table width="70%" style="margin-top:20px;">
								<tr style="vertical-align:top;">
									<td align="left" width=30%>
										<h:outputText style="color:#000000;font-size:16px;" value="Istruzione SQL" rendered="#{not empty getUserEntityBb.sql}" />
									</td>
									<td align="left" width=70%>
										<h:outputText style="color:#000000;font-size:14px;" value="#{getUserEntityBb.sql}" rendered="#{not empty getUserEntityBb.sql}" />
									</td>
								</tr>
							</table>									
							<table width="100%" style="margin-top:30px;">
								<tr>
									<td align="center">
										<h:commandButton value="Indietro" action="#{getUserEntityBb.esci}" styleClass="pulsante"/>
										<h:commandButton value="Acquisisci" disabled="#{empty getUserEntityBb.selectedEntityId || getUserEntityBb.selectedEntityId == -1}" 
										onclick="return confermaAcq()" action="#{getUserEntityBb.acquisisci}" styleClass="pulsante"/>
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
