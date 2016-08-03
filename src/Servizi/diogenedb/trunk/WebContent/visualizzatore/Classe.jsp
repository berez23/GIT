<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
		<link type="text/css" rel="stylesheet" href="../../css/visualizzatore.css" />
		<link type="text/css" rel="stylesheet" href="../css/style.css" />		
		<title>Gestione classe</title>
	</head>
	<script>
	
		var tabArr = new Array();
		var divArr = new Array();
		var styArr = new Array();
		var namArr = new Array();
		tabArr[0] = "form:tabfiltro";
		tabArr[1] = "form:tablista";
		tabArr[2] = "form:tabdettaglio";
		tabArr[3] = "form:tablink";
		divArr[0] = "divfiltro";
		divArr[1] = "divlista";
		divArr[2] = "divdettaglio";
		divArr[3] = "divlink";
		styArr[0] = "form:filterPanelStyle";
		styArr[1] = "form:listPanelStyle";
		styArr[2] = "form:detailPanelStyle";
		styArr[3] = "form:linkPanelStyle";
		namArr[0] = "filtro";
		namArr[1] = "lista";
		namArr[2] = "dettaglio";
		namArr[3] = "link";
		
		function closePopup() {
			if (confirm('I dati non salvati saranno persi: continuare?')) {
				this.close();
			}
		}
		function save() {
			opener.setDoCloseOnSubmit(false);
			document.form.elements['form:popupError'].value = "false";
			form.submit();
			if (opener.getPopup() == undefined)
				opener.setPopup(self);
			//opener.setTimeout("closeOnSubmit()", 4000);
			opener.closeOnSubmit();
			return false;
		}
		function doSubmit() {
			document.form.elements['form:popupError'].value = "false";
			form.submit();
		}
		function removeFilterColumn(filterColumnName) {
			document.form.elements['form:removedFilterColumn'].value = filterColumnName;
			doSubmit();
		}
		function addValueToFilterColumn(filterColumnName) {
			document.form.elements['form:addedValueToFilterColumn'].value = filterColumnName;
			doSubmit();
		}
		function removeValueFromFilterColumn(filterColumnName) {
			document.form.elements['form:removedValueFromFilterColumn'].value = filterColumnName;
			doSubmit();
		}
		function removeListRow(listColumnIndex) {
			document.form.elements['form:removedListRow'].value = listColumnIndex;
			doSubmit();
		}
		function addParamToListJs(listColumnIndex) {
			document.form.elements['form:addedParamToListJs'].value = listColumnIndex;
			doSubmit();
		}	
		function removeParamFromListJs(listColumnIndex) {
			document.form.elements['form:removedParamFromListJs'].value = listColumnIndex;
			doSubmit();
		}		
		function removeDetailTableOrBreak(detailTableOrBreakIndex) {
			document.form.elements['form:updatedDetailTableOrBreak'].value = detailTableOrBreakIndex;
			doSubmit();
		}
		function addDetailRow(detailTableIndex) {
			document.form.elements['form:updatedDetailTableOrBreak'].value = detailTableIndex;
			doSubmit();
		}
		function removeDetailRow(detailTableIndex, detailRowIndex) {
			document.form.elements['form:updatedDetailTableOrBreak'].value = detailTableIndex;
			document.form.elements['form:removedDetailRow'].value = detailRowIndex;
			doSubmit();
		}		
		function selectTab(name) {			
			myTab = "form:tab" + name;
			myDiv = "div" + name;
			for (i = 0; i < tabArr.length; i++) {
				document.getElementById(tabArr[i]).className = tabArr[i] == myTab ? "selectedtab" : "notselectedtab";
			}
			for (i = 0; i < divArr.length; i++) {
				document.getElementById(divArr[i]).className = divArr[i] == myDiv ? "visible" : "hidden";
				document.getElementById(styArr[i]).value = divArr[i] == myDiv ? "visible" : "hidden";
			}
			forIEProblem(name);
		}
		function setVisible() {
			paramName = "";
			for (i = 0; i < styArr.length; i++) {
				style = document.getElementById(styArr[i]).value;
				document.getElementById(tabArr[i]).className = style == "visible" ? "selectedtab" : "notselectedtab";
				document.getElementById(divArr[i]).className = style;
				if (style == "visible")
					paramName = namArr[i];
			}
			forIEProblem(paramName);
		}
		function forIEProblem(name) {
			/* funzione scritta per aggirare un problema di InternetExplorer che manteneva visibili i bordi delle 
			tabelle del dettaglio anche quando il pannello relativo era hidden */
			tds = document.form.getElementsByTagName('td');
			for (i = 0; i < tds.length; i++) {
				td = tds[i];
				if (name == 'dettaglio') {
					if (td.className == 'hiddendefaultdetailcell')
						td.className = 'defaultdetailcell';
					else if (td.className == 'hiddentitledetailcell')
						td.className = 'titledetailcell';
				}else{
					if (td.className == 'defaultdetailcell')
						td.className = 'hiddendefaultdetailcell';
					else if (td.className == 'titledetailcell')
						td.className = 'hiddentitledetailcell';
				}
			}
		}
		function selectLink(selected, linkId) {
			selectedLinkId = -1;
			linkDeletingAllKeys = false;
			if (selected && linkId != -1) {
				selectedLinkId = linkId;
				confirmMsg = "Nel caso in cui il link cancellato non abbia altri link collegati, ";
				confirmMsg += "si vuole procedere anche alla cancellazione di tutte le chiavi ad esso relative?";
				//invio richiesta conferma commentato, il flag per la cancellazione delle chiavi collegate per ora è sempre false
				//linkDeletingAllKeys = confirm(confirmMsg);
				//sostituito con:
				linkDeletingAllKeys = false;
			}
			document.form.elements['form:linkDeletingAllKeys'].value = selectedLinkId + "," + linkDeletingAllKeys;
			doSubmit();
		}
	</script>
	<body onLoad="self.focus();setVisible();opener.setDoCloseOnSubmit(true);">
		<f:view>
			<h:form id="form">
				<h:inputHidden id="popupError" value="#{DefinizioneClasse.popupError}"></h:inputHidden>
				<table class="popupheader">
					<tr>
						<td>
							<h:outputText value="#{DefinizioneClasse.title}"/>
						</td>
					</tr>
				</table>
				<table>
					<tr>
						<td colspan = 2>
							<h:outputText value="#{DefinizioneClasse.container}"/>
						</td>
					</tr>
					<tr>
						<td align="right" width="25%">
							<h:outputText value="Nome"/>
						</td>
						<td align="left">
							<h:inputText value="#{DefinizioneClasse.name}" style="width:200px"></h:inputText>
						</td>
					</tr>
					<tr>
						<td align="right">
							<h:outputText value="Entità"/>
						</td>
						<td align="left">
							<h:selectOneListbox disabled="#{DefinizioneClasse.selectedEntityDisabled}" id="entity" value="#{DefinizioneClasse.selectedEntity}" size="1" style="width:250px"
								valueChangeListener="#{DefinizioneClasse.entityChanged}" onchange="doSubmit()" immediate="true">
								<f:selectItems
								    value="#{DefinizioneClasse.entities}"/>
							</h:selectOneListbox>
							<h:outputText rendered="#{DefinizioneClasse.selectedEntityDisabled}" style="font-size:8pt;" value="non modificabile perché la classe fa parte di uno o più link"/>
						</td>
					</tr>
				</table>				
      			<br>
      			
      			<h:panelGrid style="width:400px" columnClasses="tabcolumn,tabcolumn,tabcolumn,tabcolumn" columns="4" cellpadding="0" cellspacing="0" border="1">
      				<h:column>
      					<f:verbatim><div class="selectedtab" id="form:tabfiltro"></f:verbatim>
					      	<h:outputLink  value="#"  onclick="selectTab('filtro');" styleClass="tablink"><f:verbatim>Filtro</f:verbatim></h:outputLink>
      					<f:verbatim></div></f:verbatim>
		         	</h:column>
		         	<h:column>
      					<f:verbatim><div class="notselectedtab" id="form:tablista"></f:verbatim>				
					      	<h:outputLink value="#"   onclick="selectTab('lista');" styleClass="tablink"><f:verbatim>Lista</f:verbatim></h:outputLink>
     					<f:verbatim></div></f:verbatim>
		         	</h:column>
		         	<h:column>
      					<f:verbatim><div class="notselectedtab" id="form:tabdettaglio"></f:verbatim>		         	
					      	<h:outputLink value="#" onclick="selectTab('dettaglio');" styleClass="tablink"><f:verbatim>Dettaglio</f:verbatim></h:outputLink>
     					<f:verbatim></div></f:verbatim>		         	
		         	</h:column>
		         	<h:column>
      					<f:verbatim><div class="notselectedtab" id="form:tablink"></f:verbatim>		         	
					      	<h:outputLink value="#" onclick="selectTab('link');" styleClass="tablink"><f:verbatim>Link</f:verbatim></h:outputLink>
     					<f:verbatim></div></f:verbatim>		         	
		         	</h:column>
      			</h:panelGrid>
      			
	      		<h:inputHidden id="filterPanelStyle" value="#{DefinizioneClasse.filterPanelStyle}"></h:inputHidden>
	      		<h:inputHidden id="listPanelStyle" value="#{DefinizioneClasse.listPanelStyle}"></h:inputHidden>
	      		<h:inputHidden id="detailPanelStyle" value="#{DefinizioneClasse.detailPanelStyle}"></h:inputHidden>
	      		<h:inputHidden id="linkPanelStyle" value="#{DefinizioneClasse.linkPanelStyle}"></h:inputHidden>
	      		
	      		<div id="divfiltro" class="hidden" style="overflow:auto;width:98%;text-align:center;">
	      			<div style="width:98%;">
	      				<table style="width:100%;">
	      					<tr>
	      						<td style="text-align:center;">
	      							<h:outputText value="Titolo"></h:outputText>
	      							<h:inputText value="#{DefinizioneClasse.filterTitle}" style="width:60%;"></h:inputText>
	      						</td>
	      					</tr>
	      				</table>
	      			</div>
	      			<div style="width:15%;text-align:center;vertical-align:top;float: left;">
	      				<h:selectOneListbox  id="filterColumns" value="#{DefinizioneClasse.selectedFilterColumn}" size="10" style="width:90%;">
							<f:selectItems
							    value="#{DefinizioneClasse.filterColumns}"/>
						</h:selectOneListbox>
						<br>
					<h:commandButton value="Aggiungi" onclick="doSubmit()" actionListener="#{DefinizioneClasse.addFilterColumn}" styleClass="pulsante"/>
	      			</div>
					<div style="width:85%;text-align:center;vertical-align:top;float: left;">
						<h:inputHidden id="removedFilterColumn" value="#{DefinizioneClasse.removedFilterColumn}"></h:inputHidden>	      				
	      				<h:inputHidden id="addedValueToFilterColumn" value="#{DefinizioneClasse.addedValueToFilterColumn}"></h:inputHidden>
						<h:inputHidden id="removedValueFromFilterColumn" value="#{DefinizioneClasse.removedValueFromFilterColumn}"></h:inputHidden>
						<h:dataTable columnClasses="eliminafiltercell,nomecolonnafiltercell,operatorifiltercell,valorifiltercell" style="width:100%" border="1" cellspacing="0" cellpadding="0" value="#{DefinizioneClasse.filterDM}" rendered="true" var="filter" id="filterList"> 
				        	<h:column>
				         		<f:facet name="header">
							      <h:outputText value="Elimina" id="headerfiltro1"></h:outputText>
							    </f:facet>
							    <h:commandButton value="Elimina" onclick="removeFilterColumn('#{filter.dcColumnName}')" actionListener="#{DefinizioneClasse.removeFilterColumn}" styleClass="pulsante"></h:commandButton>         		
				         	</h:column>
					        <h:column>
					        	<f:facet name="header">
							      <h:outputText value="Nome colonna" id="headerfiltro2"></h:outputText>
							    </f:facet>
							    <h:outputText value="#{filter.dcColumnName}"/>
				         	</h:column>
				         	<h:column>
				         		<f:facet name="header">
							      <h:outputText value="Operatori" id="headerfiltro3"></h:outputText>
							    </f:facet>
							    <h:panelGrid columns="1">
								    <h:selectManyCheckbox onclick="submit()" onchange="submit()" immediate="true" layout="pageDirection" value="#{filter.selOperators}">
										<f:selectItems
										    value="#{filter.operators}"/>
									</h:selectManyCheckbox>
								</h:panelGrid>
								<h:panelGrid columns="2">
									<h:outputText value="Default" style="width:40px;"></h:outputText>
									<h:selectOneListbox value="#{filter.selectedOperatorForDefault}" size="1" style="width:90px;">
										<f:selectItems
										    value="#{filter.operatorsForDefault}"/>
									</h:selectOneListbox>
								</h:panelGrid>
				         	</h:column>
				         	<h:column>
				         		<f:facet name="header">
							      <h:outputText value="Valori" id="headerfiltro4"></h:outputText>
							    </f:facet>
							    <h:panelGrid columns="1">
								    <h:selectOneListbox onchange="submit()" immediate="true" value="#{filter.selectedValueType}" size="1">
										<f:selectItems
										    value="#{filter.valueTypes}"/>
									</h:selectOneListbox>
								</h:panelGrid>
								<h:panelGrid columns="2" rendered="#{filter.renderCheckbox}" >
									<h:selectBooleanCheckbox value="#{filter.mandatory}"></h:selectBooleanCheckbox>
									<h:outputText value="Scelta obbligatoria"/>
								</h:panelGrid>
								<h:panelGrid columns="1" rendered="#{filter.renderTextarea}">
									<h:inputTextarea value="#{filter.sqlCommand}" style="width:180px;height:70px;"></h:inputTextarea>
								</h:panelGrid>
								<h:panelGrid columns="2" columnClasses="bottom,bottom" rendered="#{filter.renderList}">
									<h:selectOneListbox value="#{filter.selectedValue}" size="5" style="width:125px;">
										<f:selectItems
										    value="#{filter.values}"/>
									</h:selectOneListbox>
									<h:commandButton value="Togli selezionato" onclick="removeValueFromFilterColumn('#{filter.dcColumnName}')" actionListener="#{DefinizioneClasse.removeValueFromFilterColumn}" styleClass="pulsanteXL"></h:commandButton>
								</h:panelGrid>
								<h:panelGrid columns="3" rendered="#{filter.renderList}">
									<h:inputText value="#{filter.valueId}" style="width:60px;"></h:inputText>
									<h:inputText value="#{filter.valueDesc}" style="width:100px;"></h:inputText>
									<h:commandButton value="Aggiungi" onclick="addValueToFilterColumn('#{filter.dcColumnName}')" actionListener="#{DefinizioneClasse.addValueToFilterColumn}" styleClass="pulsante"></h:commandButton>
								</h:panelGrid>
				         	</h:column>
		      			</h:dataTable>
	      			</div>
	      		</div>
	      		
	      		<div id="divlista" class="hidden" style="overflow:auto;width:98%;text-align:center;">
					<div style="width:98%;">
	      				<table style="width:100%;">
	      					<tr>
	      						<td style="text-align:center;">
	      							<h:outputText value="Titolo"></h:outputText>
	      							<h:inputText value="#{DefinizioneClasse.listTitle}" style="width:60%;"></h:inputText>
	      						</td>
	      					</tr>
	      				</table>
	      			</div>
					<div style="width:100%;text-align:center;">
						<h:commandButton value="Aggiungi campo" onclick="doSubmit()" actionListener="#{DefinizioneClasse.addListColumn}" styleClass="pulsanteXL"></h:commandButton>
						<h:outputText style="width:2px"></h:outputText>
						<h:commandButton value="Aggiungi JS" onclick="doSubmit()" actionListener="#{DefinizioneClasse.addListJs}" styleClass="pulsanteXL"></h:commandButton>
	      			</div>
	      			<div style="text-align:center;">    			
	      				<h:inputHidden id="removedListRow" value="#{DefinizioneClasse.removedListRow}"/>
	      				<h:inputHidden id="addedParamToListJs" value="#{DefinizioneClasse.addedParamToListJs}"/>
	      				<h:inputHidden id="removedParamFromListJs" value="#{DefinizioneClasse.removedParamFromListJs}"/>
	      				<h:dataTable  columnClasses="eliminafiltercell,campilistacell" style="width:80%;margin:auto;border:1px solid black" value="#{DefinizioneClasse.listDM}" rendered="true" var="fclist" id="listList"> 
				        	<h:column>
				         		<f:facet name="header">
							      <h:outputText value="" id="headerlista1"></h:outputText>
							    </f:facet>
							    <h:commandButton value="Rimuovi" onclick="removeListRow(#{fclist.index})" actionListener="#{DefinizioneClasse.removeListRow}" styleClass="pulsante"></h:commandButton>   		
				         	</h:column>
					        <h:column>
					        	<f:facet name="header">
							      <h:outputText value="Campi della lista" id="headerlista2"></h:outputText>
							    </f:facet>
							    <h:panelGrid columns="1" rendered="#{!fclist.js}">
								    <h:selectOneListbox onchange="submit()" immediate="true" value="#{fclist.selectedColumn}" size="1" style="width:200px">
										<f:selectItems
										    value="#{fclist.columns}"/>
									</h:selectOneListbox>
								</h:panelGrid>
								<h:panelGrid columns="2" rendered="#{fclist.js}">
									<h:outputText value="Nome colonna" style="width:150px;text-align:right;font-weight:bold"></h:outputText>
									<h:inputText value="#{fclist.header}" style="width:250px"></h:inputText>
									<h:outputText value="Nome funzione" style="width:150px;text-align:right;font-weight:bold"></h:outputText>
									<h:inputText value="#{fclist.script}" style="width:250px"></h:inputText>
									<h:outputText value="Testo" style="width:150px;text-align:right;font-weight:bold"></h:outputText>
									<h:inputText value="#{fclist.testurl}" style="width:250px"></h:inputText>
									<h:outputText value="URL immagine" style="width:150px;text-align:right;font-weight:bold"></h:outputText>
									<h:inputText value="#{fclist.imageurl}" style="width:250px"></h:inputText>
								</h:panelGrid>
								<h:panelGrid columns="2" columnClasses="bottom,bottom" rendered="#{fclist.js}">
									<h:selectOneListbox value="#{fclist.selectedParam}" size="5" style="width:200px">
										<f:selectItems
										    value="#{fclist.params}"/>
									</h:selectOneListbox>
									<h:commandButton value="Togli selezionato" onclick="removeParamFromListJs(#{fclist.index})" actionListener="#{DefinizioneClasse.removeParamFromListJs}" styleClass="pulsanteXL"></h:commandButton>
									<h:selectOneListbox value="#{fclist.selectedParamColumn}" size="1" style="width:200px">
										<f:selectItems
										    value="#{fclist.paramColumns}"/>
									</h:selectOneListbox>
									<h:commandButton value="Aggiungi campo" onclick="addParamToListJs(#{fclist.index})" actionListener="#{DefinizioneClasse.addParamToListJs}" styleClass="pulsanteXL"></h:commandButton>
								</h:panelGrid>
				         	</h:column>
		      			</h:dataTable>
	      			</div>
	      		</div>
	      		
	      		<div id="divdettaglio" class="hidden" style="overflow:auto;width:98%;text-align:center;">
					<div style="width:98%;">
	      				<table style="width:100%;">
	      					<tr>
	      						<td style="text-align:center;">
	      							<h:outputText value="Titolo"></h:outputText>
	      							<h:inputText value="#{DefinizioneClasse.detailTitle}" style="width:60%;"></h:inputText>
	      						</td>
	      					</tr>
	      				</table>
	      			</div>
	      			<div style="width:100%;text-align:center;">
						<h:commandButton value="Aggiungi tabella" onclick="doSubmit()" actionListener="#{DefinizioneClasse.addDetailTable}" styleClass="pulsanteXL"></h:commandButton>
						<h:outputText style="width:2px"></h:outputText>
						<h:commandButton value="Aggiungi divisione" onclick="doSubmit()" actionListener="#{DefinizioneClasse.addDetailBreak}" styleClass="pulsanteXL"></h:commandButton>
	      			</div>	      			
	      			<div style="text-align:center;">    			
	      				<h:inputHidden id="updatedDetailTableOrBreak" value="#{DefinizioneClasse.updatedDetailTableOrBreak}"></h:inputHidden>	      				
	      				<h:inputHidden id="removedDetailRow" value="#{DefinizioneClasse.removedDetailRow}"></h:inputHidden>
	      				<h:dataTable style="width:98%;margin:auto;border:0px;" value="#{DefinizioneClasse.detailDM}" rendered="true" var="detailGroup" id="detailGroupList"> 
				        	<h:column>
			         			<h:dataTable style="width:100%;margin:5px 0px 5px 0px;border:1px;border-collapse:collapse;" columnClasses="defaultdetailcell,titledetailcell,defaultdetailcell,defaultdetailcell" value="#{detailGroup.detailDM}" rendered="#{detailGroup.table}" var="detailTable" id="detailTableList"> 
						        	<h:column>
						         		<h:commandButton value="Elimina tabella" rendered="#{detailTable.first}" onclick="removeDetailTableOrBreak(#{detailGroup.index})" actionListener="#{DefinizioneClasse.removeDetailTableOrBreak}" styleClass="pulsanteXL"></h:commandButton>
						         		<h:selectOneListbox value="#{detailTable.selectedColumn1}" rendered="#{!detailTable.first}" size="1" style="width:150px;">
											<f:selectItems value="#{detailTable.columns1}"/>
										</h:selectOneListbox>
						         	</h:column>
						         	<h:column>
						         		<h:outputText value="Titolo" rendered="#{detailTable.first}"></h:outputText>
						         		<h:inputText value="#{detailTable.title}" rendered="#{detailTable.first}" style="width:200px;"></h:inputText>
						         		<h:selectOneListbox value="#{detailTable.selectedColumn2}" rendered="#{!detailTable.first}" size="1" style="width:150px">
											<f:selectItems value="#{detailTable.columns2}"/>
										</h:selectOneListbox>
						         	</h:column>
						         	<h:column>
						         		<h:selectOneListbox value="#{detailTable.selectedColumn3}" rendered="#{!detailTable.first}" size="1" style="width:150px">
											<f:selectItems value="#{detailTable.columns3}"/>
										</h:selectOneListbox>
						         	</h:column>
						         	<h:column>
						         		<h:commandButton value="Aggiungi riga" rendered="#{detailTable.first}" onclick="addDetailRow(#{detailGroup.index})" actionListener="#{DefinizioneClasse.addDetailRow}" styleClass="pulsanteXL"></h:commandButton>
						         		<h:commandButton value="Elimina riga" rendered="#{!detailTable.first}" onclick="removeDetailRow(#{detailGroup.index},#{detailTable.index})" actionListener="#{DefinizioneClasse.removeDetailRow}" styleClass="pulsanteXL"></h:commandButton>
						         	</h:column>
				      			</h:dataTable>
				      			<h:dataTable style="width:100%;margin:auto;border:0px;" value="#{detailGroup.detailDM}" rendered="#{!detailGroup.table}" var="detailBreak" id="detailBreakList"> 
						        	<h:column>
									    <h:panelGrid columns="2">
									    	<h:commandButton value="Elimina divisione" onclick="removeDetailTableOrBreak(#{detailGroup.index})" actionListener="#{DefinizioneClasse.removeDetailTableOrBreak}" styleClass="pulsanteXL"></h:commandButton>
									    	<f:verbatim><hr style="width:540px;"></f:verbatim>							    	
									    </h:panelGrid>
						         	</h:column>
				      			</h:dataTable>
				         	</h:column>
		      			</h:dataTable>
	      			</div>	      			
	      		</div>
	      		
	      		<div id="divlink" class="hidden" style="overflow:auto;width:98%;text-align:center;">
	      			<div style="width:100%;text-align:center;">
						<h:inputHidden id="linkDeletingAllKeys" value="#{DefinizioneClasse.linkDeletingAllKeys}" valueChangeListener="#{DefinizioneClasse.selectedLink}"></h:inputHidden>
	      				<h:dataTable style="width:98%;" columnClasses="sellinkcell,linkcell,linkcell,linkcell" value="#{DefinizioneClasse.linkDM}" border="1" cellspacing="0" cellpadding="0" rendered="true" var="link" id="linkList"> 
				        	<h:column>
				         		<f:facet name="header">
							      <h:outputText value="Sel." id="headerlink1"></h:outputText>
							    </f:facet>
							    <h:selectBooleanCheckbox value="#{link.selected}" onclick="selectLink(#{link.selected},#{link.linkId == null ? -1 : link.linkId})"></h:selectBooleanCheckbox>         		
				         	</h:column>
					      	<h:column>
				         		<f:facet name="header">
							      <h:outputText value="Progetto" id="headerlink2"></h:outputText>
							    </f:facet>
							    <h:outputText value="#{link.projectName}"></h:outputText>         		
				         	</h:column>
				         	<h:column>
				         		<f:facet name="header">
							      <h:outputText value="Classe" id="headerlink3"></h:outputText>
							    </f:facet>
							    <h:outputText value="#{link.className}"></h:outputText>         		
				         	</h:column>
				         	<h:column>
				         		<f:facet name="header">
							      <h:outputText value="Nome link" id="headerlink4"></h:outputText>
							    </f:facet>
							    <h:inputText value="#{link.linkName}" style="width:95%;"></h:inputText>         		
				         	</h:column>
		      			</h:dataTable>
	      			</div>
	      		</div>
	      		
      			<div style="position:absolute;top:82%;">
					<table>
						<tr>
							<td align="left">
								<h:commandButton value="Annulla" onclick="closePopup();return false;" styleClass="pulsante"></h:commandButton>
								<h:commandButton value="Salva" onclick="save();" actionListener="#{DefinizioneClasse.salva}" styleClass="pulsante"></h:commandButton>
							</td>
						</tr>
					</table>
					<h:messages id="errors" style="color: red; font-family: 'New Century Schoolbook', serif;
							font-style: oblique"/>
				</div>
			</h:form>
		</f:view>
	</body>
</html>



