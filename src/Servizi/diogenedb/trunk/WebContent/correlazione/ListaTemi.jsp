<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<link href="../css/correlazione.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<title>Lista temi</title>
	</head>
	<script>
		function setSfondoBut() {
			for (i = 0; i < document.form.elements['form:numeroTemi'].value; i++) {
				if (i % 2 == 1) {
					name = "form:listaTemi_" + i + ":selezionabut";
					document.form.elements[name].style.backgroundColor = "#CCCCCC";
				}
			}
		}
		function seleziona(idTemaSel) {
			document.form.elements['form:idTemaSel'].value = idTemaSel;
			document.form.elements['form:renderedBack'].value = document.form.elements['form:indietrobut'] ? true : false;						
			return true;
		}
	</script>
	<body onload="setSfondoBut()">
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
					<h:inputHidden id="numeroTemi" value="#{ListaTemi.numeroTemi}"></h:inputHidden>
					<h:inputHidden id="idTemaSel" value="#{ListaTemi.idTemaSel}"></h:inputHidden>
					<h:inputHidden id="renderedBack" value="#{ListaTemi.renderedBack}"></h:inputHidden>
					<div id="centrecontent">
						<center>
							<table width="100%">
								<tr>
									<td align="center">
										<p style="color:#009300;font-size:18px;margin-top:20px;margin-bottom:0px;">
											Selezionare un tema dalla lista.
										</p>
									</td>
								</tr>								
							</table>
							<h:dataTable 
				 				id="listaTemi" 
					 			styleClass="griglia" 
					 			style="width:50%;margin-top:20px;margin-bottom:10px;border-collapse:collapse;empty-cells:show;"
					 			rowClasses=",alternateRow"
					 			headerClass="thgrigliacorrel"
					 			columnClasses="colonnalistatemi1,colonnalistatemi2"
					 			value="#{ListaTemi.temi}" 
					 			var="tema">
								<h:column id="colonna1">
									<f:facet name="header">
										<h:outputText value="Nome tema" />
									</f:facet>								
									<h:outputText value="#{tema.name}" />
								</h:column>
								<h:column id="colonna2">
									<f:facet name="header">
										<h:outputText value="" />
									</f:facet>
									<h:commandButton id="selezionabut" value="Seleziona" onclick="return seleziona('#{tema.id}');" action="#{ListaTemi.seleziona}" styleClass="pulsante_sm"/>
								</h:column>
							</h:dataTable>					
							<table width="100%">
								<tr>
									<td align="center">
										<h:commandButton id="indietrobut" value="Indietro" rendered="#{ListaTemi.renderedBack && (empty param.renderedBack || param.renderedBack == 'true')}" action="#{ListaTemi.esci}" styleClass="pulsante"/>
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
