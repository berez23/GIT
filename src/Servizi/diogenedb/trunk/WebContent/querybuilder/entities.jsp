<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.webred.it/faces" prefix="webred" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
	<f:loadBundle basename="it.webred.diogene.querybuilder.labels" var="labels"/>
	<f:view>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
			<meta http-equiv="pragma" content="no-cache">
			<meta http-equiv="Cache-Control" content="no-cache" />
			<title><h:outputText value="#{labels['entities.title']}" /></title>
			<link type="text/css" rel="stylesheet" href="../../css/dhtml.css" />
			<link type="text/css" rel="stylesheet" href="../../css/querybuilder.css" />
			<link type="text/css" rel="stylesheet" href="../../css/style.css" />
			<script language="JavaScript" src="../../scripts/dhtml.js">
			</script>
			<script language="JavaScript" src="../../scripts/querybuilder.js">
			</script>
			<script language="JavaScript"><!--
				
				function getFormName()
				{
					return document.forms[0].id;
				}
				function getFormId()
				{
					return getFormName() + ":";
				}

				function showError()
				{
				
					var msg = document.getElementById(getFormId() + 'globalErrorMessage');;
					if (msg && msg.value!=''){
						alert(msg.value);
						msg.value='';
					}
				}
				
				var conditionsBuilderPopUp = false;
				function openConditionsBuilderPopUpListener(url)
				{
					document.getElementById("fade").style.visibility = "visible";
					if (window.navigator.appName.match(/microsoft|explorer/i))
						disableAllSuchNodes(document.getElementsByTagName("body")[0], "select");
					if (window.showModalDialog) // IE,  MA showModalDialog FA CASINO. PROVARE PER CREDERE. QUINDI US0 QUESTO SOLO PER TESTARE CHE E' IE
						conditionsBuilderPopUp = window.open(url, "conditionsBuilderPopUp", "width=1000,height=700,menubar=no,status=no");
					else // SE NO SU Firefox/1.0.7 C'E UN BACO ORRENDO. PROVARE PER CREDERE
						window.open(url, "conditionsBuilderPopUp", "width=1000,height=700,menubar=no,status=no,dependent=yes,modal=yes");
				}
				function closeConditionsBuilderPopUpListener()
				{
					conditionsBuilderPopUp = false;
					submitting=true;
					enableAllSuchNodes(document.getElementsByTagName("body")[0], "select");
					document.getElementById(getFormName()).submit();
					//document.forms['entitiesBuilderForm'].elements['entitiesBuilderForm:formerInRelationSelectBox'].onchange();

					//submitForm(document.forms['entitiesBuilderForm']);
				}
				function fuoco()
				{
					if (conditionsBuilderPopUp)
					{
						conditionsBuilderPopUp.focus();
						return true;
					}
					return false;
				}
				function unload()
				{
					//alert(document.getElementById('entitiesBuilderForm:toDate').value);
					if (!submitting)
					{
						clear_entitiesBuilderForm();
						document.forms[getFormName()].elements['autoScroll'].value=getScrolling();
						document.forms[getFormName()].elements['entitiesBuilderForm:_link_hidden_'].value='entitiesBuilderForm:editCancel';
						if(document.forms[getFormName()].onsubmit){
							if(document.forms[getFormName()].onsubmit()) document.forms['entitiesBuilderForm'].submit();
						}else{
							document.forms[getFormName()].submit();
						}
						alert("<h:outputText value="#{labels['entities.userEntity.exit.abandonMessage']}" />");
					}
				}
				function execConfirmAtomic()
				{
					document.getElementById("entitiesBuilderForm:confirmAtomicButton").click();
				}
				function prepareOpenConditionBuilder()
				{					
					document.forms[getFormName()].action = document.forms[getFormName()].action + "?openConditionPopup=true";
				}
				function openConditionBuilder()
				{
					if ("<h:outputText escape='true' value='true' rendered='#{not empty param.openConditionPopup and not empty entitiesBb.conditionsController.condition}' />")
						openConditionsBuilderPopUpListener("conditionsBuilder.jsp");
				}
				
				function copyModifingRelationKey(value)
				{
					document.getElementById("entitiesBuilderForm:modifingRelation").value = value;
				}
				function showSql()
				{
					<h:outputText escape="false" rendered="#{entitiesBb.showSql}" value="document.getElementById('explicitSqlTab').style.visibility = 'visible';" />
					return true;
				}
				function enableUpdateAllButton(){
					var button=document.getElementById(getFormId() + 'updateAllButton');
					if(button!=null){
						button.disabled = false;
					}
				}
				// FUNZIONI NECESSARIE ALL'EDITOR DELLE ESPRESSIONI
				var visibleExpressionTab = '<h:outputText value="#{entitiesBb.valueExpressionEditor.valueExpressionSelectedTab}" />';
				function viewExpressionTab(tabId)
				{
					if (tabId)
					{
						if (visibleExpressionTab && document.getElementById(getFormId() + visibleExpressionTab))
						{
							document.getElementById(getFormId() + visibleExpressionTab).style.visibility = "hidden";
							document.getElementById(getFormId() + visibleExpressionTab).style.position = "absolute";
						}
						visibleExpressionTab = tabId;
						var oggetto;
						oggetto = document.getElementById(getFormId() + visibleExpressionTab)
						if(oggetto!=null){
							oggetto.style.position = "static";
							oggetto.style.visibility = "visible";
						}
					}
				}
				//
			--></script>
		</head>
		<body onload="showError(); openConditionBuilder(); showSql(); viewExpressionTab(visibleExpressionTab);" onunload="unload();" onfocus="fuoco();" oncontextmenu="return false;">
			<script>
			<!--
			//               BACKSP   F1          F2          F3          F4          F5          F6          F7          F8          F9          F10         F11         F12          CTRL +  F, G, R
			disabilitaTasti([[8,true],[112,false],[113,false],[114,false],[115,false],[116,false],[117,false],[118,false],[119,false],[120,false],[121,false],[122,false],[123,false]],       [70,71,82]);
			//-->
			</script>
			<h:form id="entitiesBuilderForm" onsubmit="return viewConfirm();">
				<f:verbatim><div
					id="explicitSqlTab"
					class="helpBox" 
					style="
						visibility: hidden; 
						position: absolute; 
						top: 80px; 
						left: 50px; 
						z-index: 5000;"
					>
					<div
						id="dragBar"
						class="dragBar"
						onmousedown="beginMove(this.parentNode, event)"
						></f:verbatim><h:outputText escape="false" value="#{labels['common.dragMe.label']}" /><f:verbatim></div>
					<input type="button" class="pulsanteDark" value='<h:outputText escape="false" value="#{labels['buildSql.executeSql.button']}" />' onclick="document.forms[1].submit();" />
					<br />
					<input type="button" class="pulsanteDark" value='<h:outputText escape="false" value="#{labels['buildSql.closeSql.button']}" />' onclick="this.parentNode.style.visibility = 'hidden';" />
					<textarea 
						readonly
						style="visibility: inherit;"
						rows="10"
						cols="30"
						</f:verbatim><h:outputText escape="false" value="title=#{labels['common.phrases.toCopyAndPasteSelectTheTextAndPressCTRLplusC']}" /><f:verbatim>
						></f:verbatim>
						<h:outputText	escape="false" value="#{entitiesBb.explicitSql}" />
						<f:verbatim></textarea>
				</div></f:verbatim>
				<div id="HEADER">
					<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
						<h:commandButton 
							id="exitButton" 
							onclick="confirmOn(#{labels['entities.userEntity.exit.confirmationMessage']});"
							value="#{labels['common.words.Exit']}" 
							title="#{labels['common.words.Exit']}" 
							alt="#{labels['common.words.Exit']}" 
							action="#{entitiesBb.startLife}" 
							immediate="true"
							styleClass="pulsanteDark"							
							/>
						<h:commandButton 
							onclick="confirmOn(#{labels['entities.userEntity.saveUserEntity.confirmationMessage']});"
							id="saveAllButton"
							rendered="#{not entitiesBb.updateStatus}"
							value="#{labels['entities.saveToDB.button']}"
							title="#{labels['entities.saveToDB.button.hint']}"
							alt="#{labels['entities.saveToDB.button.hint']}"
							action="#{entitiesBb.saveUserEntity}"
							styleClass="pulsanteDark"							
							/>
						<h:commandButton 
							onclick="confirmOn(#{labels['entities.userEntity.updateUserEntity.confirmationMessage']});"
							id="updateAllButton"
							rendered="#{entitiesBb.updateStatus}"
							disabled="#{not entitiesBb.modifiedByUser and not entitiesBb.buildedEntity.modifiedByUser}"
							value="#{labels['entities.updateToDB.button']}"
							title="#{labels['entities.updateToDB.button.hint']}"
							alt="#{labels['entities.updateToDB.button.hint']}"
							action="#{entitiesBb.saveUserEntity}"
							styleClass="pulsanteDark"							
							/>
						<h:message for="saveAllButton" styleClass="errorMessage" />
						<h:commandButton 
							onclick="confirmOn(#{labels['entities.userEntity.deleteUserEntity.confirmationMessage']});"
							id="deleteUserEntityButton"
							rendered="#{entitiesBb.updateStatus}"
							value="#{labels['entities.deleteFromDB.button']}"
							title="#{labels['entities.deleteFromDB.button.hint']}"
							alt="#{labels['entities.deleteFromDB.button.hint']}"
							action="#{entitiesBb.deleteUserEntity}"
							styleClass="pulsanteDark"							
							/>
						<h:outputLabel 
							value="#{labels['entities.userEntityName.label']}"
							for="UserEntityName"
							/>
						<h:inputText 
							id="UserEntityName" 
							required="true"
							value="#{entitiesBb.buildedEntity.name}"
							valueChangeListener="#{entitiesBb.nameModified}"
							size="32"
							onchange="enableUpdateAllButton()"
							/>
						<h:inputHidden id="globalErrorMessage"  value="#{entitiesBb.globalErrorMessage}" />
						<h:message 
							for="UserEntityName" 
							styleClass="errorMessage"
							/>
						<h:outputLabel 
							value="#{labels['entities.userEntityDescription.label']}"
							for="UserEntityDescription"
							/>
						<h:inputText
							id="UserEntityDescription"
							size="40"
							value="#{entitiesBb.buildedEntity.description}"
							valueChangeListener="#{entitiesBb.nameModified}"
							onchange="enableUpdateAllButton()"
							 />
						<f:verbatim><br /></f:verbatim>
						<h:commandButton 
							id="showSqlButton"
							value="#{labels['entities.showExplicitSql.button']}"
							actionListener="#{entitiesBb.showExplicitSql}"
							styleClass="pulsanteDark"							
							/>
						<h:commandButton 
							id="editExplicitSql"
							value="#{labels['entities.editExplicitSql.button']}"
							action="#{entitiesBb.editExplicitSql}"
							rendered="#{not entitiesBb.updateStatus}"
							onclick="confirmOn(#{labels['entities.userEntity.editExplicitSql.confirmationMessage']});"
							styleClass="pulsanteDark"							
							/>
					</div>
				</div>
				
				<%-- KWESTO DIV CONTIENE LE ICONE DELLE ENTITA' DA SCEGLIERE --%>
				<div id="NO">
					<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
						<h3>
							<h:outputText
								value="#{labels['entities.addEntity.label']}" 
								styleClass="entitiesFormLabels" 
								/>
						</h3>
							
						<webred:block htmlElementName="div" id="iconsContainer"
							binding="#{entitiesBb.iconsContainer}" />	
														
					</div>
				</div>
				
				<%-- QUESTO DIV CONTIENE LE RELAZIONI --%>
				<div id="SO">
					<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
					<div style="margin: auto;">
						<h:commandButton 
							rendered="#{empty entitiesBb.relationsDataBinding.filterCondition.value.relations}"
							value="#{labels['entities.condition.addFilterCondition']}" 
							onclick="prepareOpenConditionBuilder();"
							actionListener="#{entitiesBb.editFilterCondition}"
							styleClass="pulsanteXL"							
							/>
						<h:commandButton 
							rendered="#{not empty entitiesBb.relationsDataBinding.filterCondition.value.relations}"
							value="#{labels['entities.condition.editFilterCondition']}" 
							onclick="prepareOpenConditionBuilder();"
							actionListener="#{entitiesBb.editFilterCondition}"
							styleClass="pulsanteXL"							
							/>
					</div>
					<table style="width: 100%;">
						<thead>
							<tr>
								<th colspan="3">
									<h:outputText id="addFilterCondButton" value="#{labels['entities.addRelation.label']}" />
									<br />
									<h:message for="addFilterCondButton" styleClass="errorMessage" />					
								</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th colspan="3">
									<h:commandButton 
										value="#{labels['entities.proceed.button']}" 
										id="addRelButton" 
										onclick="prepareOpenConditionBuilder();"
										actionListener="#{entitiesBb.addRelation}"
										styleClass="pulsante"							
										/>
									<br />
									<h:message for="addRelButton" styleClass="errorMessage" />					
								</th>
							</tr>
						</tfoot>
						<tbody>
							<tr>
								<td>
									<h:outputLabel 
										value="#{labels['common.words.lowerCase.between']}" 
										styleClass="entitiesFormLabels" 
										id="relationsSelectBoxLabel1" 
										for="latterInRelationSelectBox" 
										/>
								</td>
								<td>
									<h:selectOneMenu 
										onchange="submitForm(form);"
										value="#{entitiesBb.choosedFormerEntityInRelation}" 
										valueChangeListener="#{entitiesBb.formerInRelationChoosed}"
										id="formerInRelationSelectBox" 
										styleClass="entitiesFormSelectBoxes"
										>
										<f:selectItems value="#{entitiesBb.formerEntitiesInRelation}" />
									</h:selectOneMenu>
								</td>
								<td>
									<h:selectBooleanCheckbox 
										value="#{entitiesBb.latterEntityInOuter}"
										id="latterTableOuter"
										/>
									<h:outputLabel 
										value="#{labels['entities.relationsTable.outer.label']}" 
										styleClass="entitiesFormLabels" 
										for="latterTableOuter" 
										/>
								</td>
							</tr>
							<tr>
								<td>
									<h:outputLabel 
										value="#{labels['common.words.lowerCase.and']}" 
										styleClass="entitiesFormLabels" 
										id="relationsSelectBoxLabel2" 
										for="latterInRelationSelectBox" 
										/>
								</td>
								<td colspan="2">
									<h:selectOneMenu 
										onchange="submitForm(form);"
										value="#{entitiesBb.choosedLatterEntityInRelation}" 
										valueChangeListener="#{entitiesBb.latterInRelationChoosed}"
										id="latterInRelationSelectBox" 
										styleClass="entitiesFormSelectBoxes"
										>
										<f:selectItems value="#{entitiesBb.latterEntitiesInRelation}" />
									</h:selectOneMenu>
								</td>
							</tr>
						</tbody>
					</table>

						<h:dataTable
							rendered="#{not empty entitiesBb.relationsDataBinding.relations}"
							value="#{entitiesBb.relationsDataBinding.relationsDataModel}"
							var="row" 
							styleClass="relationsTable"
							headerClass="relationsTableHeader"
							footerClass="relationsTableFooter"
							rowClasses="relationsTableOddRow,relationsTableEvenRow"
							columnClasses="relationsTableColumn"
							>
							<h:column>
								<f:facet name="header">
									<h:outputText 
										value="#{labels['entities.relationsTable.relationBetween.label']}"
										/>
								</f:facet>
								<h:commandButton 
									onclick="copyModifingRelationKey('#{row.value.value}');"
									rendered="#{not row.value.value.inOuterJoin}"
									image="/pics/outeroff.gif"
									title="#{labels['expressions.toggleRelation.button']}"
									alt="#{labels['expressions.toggleRelation.button']}"
									actionListener="#{entitiesBb.toggleOuterJoinRelation}"
									/>
								<h:commandButton 
									onclick="copyModifingRelationKey('#{row.value.value}');"
									rendered="#{row.value.value.inOuterJoin}"
									image="/pics/outeron.gif"
									title="#{labels['expressions.toggleRelation.button']}"
									alt="#{labels['expressions.toggleRelation.button']}"
									actionListener="#{entitiesBb.toggleOuterJoinRelation}"
									/>
								<h:outputText  
									value="#{row.value.value.formerEntityName}"
									/>
								<f:verbatim>&nbsp;&lt;-&gt;&nbsp;</f:verbatim>
								<h:outputText 
									value="#{row.value.value.latterEntityName}"
									/>
							</h:column>
							<h:column>
								<h:outputText 
									escape="false" 
									value='<input type="hidden" value="'
									/>
								<h:outputText 
									escape="false" 
									value="#{row.value.value}" 
									/>
								<h:outputText 
									escape="false" 
									value='" />'
									/>
								<h:commandButton 
									onclick="copyModifingRelationKey('#{row.value.value}');prepareOpenConditionBuilder();"
									image="/pics/edit.gif"
									title="#{labels['expressions.editRelation.button']}"
									alt="#{labels['expressions.editRelation.button']}"
									actionListener="#{entitiesBb.editRelation}"
									/>
								<h:commandButton 
									onclick="copyModifingRelationKey('#{row.value.value}'); confirmOn(#{labels['expressions.removeRelation.confirmationMessage']})"
									title="#{labels['expressions.removeRelation.button']}"
									alt="#{labels['expressions.removeRelation.button']}"
									image="/pics/deleteRed.gif"
									actionListener="#{entitiesBb.deleteRelation}"
									rendered="#{row.value.value.editable}"
									/>								
								<h:commandButton 
									onclick="copyModifingRelationKey('#{row.value.value}');"
									title="#{labels['expressions.swapRelation.button']}"
									alt="#{labels['expressions.swapRelation.button']}"
									image="/pics/undo.gif"
									actionListener="#{entitiesBb.swapRelation}"
									rendered="#{row.value.value.editable}"
									/>								
							</h:column>
						</h:dataTable>
						
					</div>
				</div>
				
				<%--// QUESTO BLOCCO CONTIENE IL DETTAGLIO DELLE ENTITA' SCELTE --%>
				<div id="NE">
					<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
						<webred:block 
							id="choosedEntitiesPanel" 
							htmlElementName="div"
							binding="#{entitiesBb.choosedEntitiesPanel}" 
							/>
					</div>
				</div>
				
				<div id="SE">

				<%--// QUESTO BLOCCO CONTIENE L'ELENCO DEI CAMPI DELL'ENTITA' CHE SI STANNO CREANDO --%>
					<div class="LEFT">
					<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
						
						<h:selectOneRadio 
							onchange="enableUpdateAllButton()"
							id="predicateSelectRadio"
							layout="lineDirection"
							value="#{entitiesBb.buildedEntity.predicate}"
							valueChangeListener="#{entitiesBb.predicateModified}"
							>
							<f:selectItems value="#{entitiesBb.predicates}" />
						</h:selectOneRadio>
						<h:message 
							for="predicateSelectRadio" 
							styleClass="errorMessage"
							/>						
						<h:outputLabel value="#{labels['entities.userEntity.fieldsTable.externalKey.label']}" />
						<h:selectOneMenu id="externalKey" value="#{entitiesBb.buildedEntity.externalKey}"
							onchange="enableUpdateAllButton()"						
							valueChangeListener="#{entitiesBb.externalKeyModified}"
							>
							<f:selectItems value="#{entitiesBb.userEntityFieldsNumbers}"/>
						</h:selectOneMenu>
						<h:outputText escape="false" value="<br />" />
						<h:outputLabel value="#{labels['entities.userEntity.fieldsTable.fromDate.label']}" />
						<h:selectOneMenu id="fromDate" value="#{entitiesBb.buildedEntity.fromDate}"
							onchange="enableUpdateAllButton()"						
							valueChangeListener="#{entitiesBb.dateFromModified}"
							>
							<f:selectItems value="#{entitiesBb.userEntityFieldsNumbers}"/>
						</h:selectOneMenu>
						<h:outputText escape="false" value="<br />" />
						<h:outputLabel value="#{labels['entities.userEntity.fieldsTable.toDate.label']}" />
						<h:selectOneMenu id="toDate" value="#{entitiesBb.buildedEntity.toDate}"
							onchange="enableUpdateAllButton()"
													
							valueChangeListener="#{entitiesBb.dateToModified}"							
							>
							<f:selectItems value="#{entitiesBb.userEntityFieldsNumbers}"/>
						</h:selectOneMenu>
						<h:commandButton 
							value="#{labels['entities.userEntity.fieldsTable.insertField.button']}"
							actionListener="#{entitiesBb.addNewColumn}"
							styleClass="pulsanteXL"							
							/>
							
							
						<h:dataTable
							id="userEntityFieldsTable"
							value="#{entitiesBb.buildedEntity.selectList}"
							var="row" 
							styleClass="relationsTable"
							headerClass="relationsTableHeader"
							footerClass="relationsTableFooter"
							rowClasses="relationsTableOddRow,relationsTableEvenRow"
							columnClasses="relationsTableColumn"
							>
							<h:column>
								<h:outputText
									value="#{row.position + 1}" 
									/>
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText 
										value="#{labels['entities.userEntity.fieldsTable.fieldName.label']}" 
										/>
								</f:facet>
								<h:inputText 
									id="fieldName"
									size="24" 
									maxlength="256"
									alt="#{row.hint}"
									title="#{row.hint}"
									value="#{row.name}" 
									valueChangeListener="#{row.nameModified}"
									onchange="enableUpdateAllButton()"						
									readonly="#{not row.editable}"
									/>
								<h:outputText escape="false" value="<br />" />
								<h:message 
									for="fieldName" 
									styleClass="errorMessage"
									/>
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText 
										value="#{labels['entities.userEntity.fieldsTable.commands.label']}" 
										/>
								</f:facet>
								
								<h:outputText escape="false" value="<div style='white-space: nowrap;'>" />
								<h:commandButton
									image="/pics/table.gif"
									alt="#{labels['entities.userEntity.fieldsTable.insertValue.button']}"
									title="#{labels['entities.userEntity.fieldsTable.insertValue.button']}"
									actionListener="#{row.edit}"
									/>
								<h:commandButton
									image="/pics/addBelow.gif"
									alt="#{labels['entities.userEntity.fieldsTable.appendValue.button']}"
									title="#{labels['entities.userEntity.fieldsTable.appendValue.button']}"
									actionListener="#{row.appendNewValue}"
									rendered="#{row.editable}"
									/>
								<h:commandButton
									image="/pics/deleteRed.gif"
									onclick="confirmOn(#{labels['entities.userEntity.fieldsTable.deleteField.confirmationMessage']})"
									alt="#{labels['entities.userEntity.fieldsTable.delete.button']}"
									title="#{labels['entities.userEntity.fieldsTable.delete.button']}"
									actionListener="#{row.deleteMe}"
									rendered="#{row.editable}"
									/>
								<h:outputText escape="false" value="</div>" />
									
								<h:outputText escape="false" value="<br />" />
								<h:dataTable 
									value="#{row.appendedExpressions}"
									var="appendedValue"
									id="chanExpressionsTable"
									rendered="#{not empty row.appendedExpressions}"
									>
									<h:column>
										<h:selectOneMenu  
											title="#{appendedValue.hint}"
											value="#{appendedValue.chainOperator}"
											valueChangeListener="#{appendedValue.operatorModified}"
											onchange="enableUpdateAllButton()"						
											>
											<f:selectItems value="#{entitiesBb.chainOperators}" />
										</h:selectOneMenu>
									</h:column>
									<h:column>
										<h:commandButton
											id="appendedExpressionInsertValueButton"
											image="/pics/table.gif"
											alt="#{labels['entities.userEntity.fieldsTable.insertValue.button']}"
											title="#{labels['entities.userEntity.fieldsTable.insertValue.button']}"
											actionListener="#{appendedValue.edit}"
											/>										
										<h:commandButton
											image="/pics/deleteRed.gif"
											onclick="confirmOn(#{labels['entities.userEntity.fieldsTable.deleteField.confirmationMessage']})"
											alt="#{labels['entities.userEntity.fieldsTable.delete.button']}"
											title="#{labels['entities.userEntity.fieldsTable.delete.button']}"
											actionListener="#{appendedValue.deleteMe}"
											rendered="#{appendedValue.editable}"
											/>
										<h:message for="appendedExpressionInsertValueButton" styleClass="errorMessage" />
									</h:column>
								</h:dataTable>
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText 
										value="#{labels['entities.userEntity.fieldsTable.primaryKey.abbr.label']}" 
										/>
								</f:facet>
								<h:selectBooleanCheckbox 
									value="#{row.primaryKey}"
									valueChangeListener="#{row.primaryKeyModified}"
									onchange="enableUpdateAllButton()"						
									alt="#{labels['entities.userEntity.fieldsTable.primaryKey.label']}"
									title="#{labels['entities.userEntity.fieldsTable.primaryKey.label']}"
									disabled="#{not row.editable}"
									/>
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText 
										value="#{labels['entities.userEntity.fieldsTable.groupBy.abbr.label']}" 
										/>
								</f:facet>
								<h:selectBooleanCheckbox 
									value="#{row.groupBy}"
									valueChangeListener="#{row.groupByModified}"
									onchange="enableUpdateAllButton()"						
									alt="#{labels['entities.userEntity.fieldsTable.groupBy.label']}"
									title="#{labels['entities.userEntity.fieldsTable.groupBy.label']}"
									disabled="#{not row.editable}"
									/>
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText 
										value="#{labels['entities.userEntity.fieldsTable.orderBy.abbr.label']}" 
										/>
								</f:facet>
								<h:selectBooleanCheckbox 
									value="#{row.orderBy}"
									valueChangeListener="#{row.orderByModified}"
									onchange="document.getElementById(getFormId() + 'updateAllButton').disabled = false"						
									alt="#{labels['entities.userEntity.fieldsTable.orderBy.label']}"
									title="#{labels['entities.userEntity.fieldsTable.orderBy.label']}"
									disabled="#{not row.editable}"
									/>
							</h:column>
						</h:dataTable>
						
					</div></div>
					
				<%-- // QUESTO BLOCCO CONTIENE L'EDITOR DELLE ESPRESSIONI --%>
					<div class="RIGHT">
						<webred:block 
							rendered="#{not empty entitiesBb.valueExpressionEditor && not empty entitiesBb.valueExpressionEditor.value && not empty entitiesBb.valueExpressionEditor.value.editingValue}"
							htmlElementName="div"
							styleClass="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi"
							>
							<webred:block
								id="chooseExpressionCategory"
								htmlElementName="div" 
								>
								<h:selectOneRadio 
									disabled="#{not entitiesBb.valueExpressionEditor.editable}"
									layout="lineDirection"
									value="#{entitiesBb.valueExpressionEditor.valueExpressionSelectedTab}"
									onclick="viewExpressionTab(this.value)"
									>
									<f:selectItem 
										itemLabel="#{labels['expressions.valueExpressionEditor.literalExpression.label']}" 
										itemValue="literalExpression" 
										/>
									<f:selectItem 
										itemLabel="#{labels['expressions.valueExpressionEditor.atomicExpression.label']}" 
										itemValue="atomicExpression" 
										/>
									<f:selectItem 
										itemLabel="#{labels['expressions.valueExpressionEditor.functionExpression.label']}" 
										itemValue="functionExpression" 
										/>
									<f:selectItem 
										itemLabel="#{labels['expressions.valueExpressionEditor.explicitSQLExpression.label']}" 
										itemValue="explicitSQLExpression" 
										/>
								</h:selectOneRadio>
							</webred:block>
							<webred:block 
								id="literalExpression"
								htmlElementName="div" 
								style="position: absolute; visibility: hidden;"
								>
								<h:inputTextarea
									rows="5" 
									readonly="#{not entitiesBb.valueExpressionEditor.editable}"
									value="#{entitiesBb.valueExpressionEditor.literal}"
									/>
								<h:outputText escape="false" value="<br />" />
								<h:commandButton
									disabled="#{not entitiesBb.valueExpressionEditor.editable}" 
									value="#{labels['common.words.Confirm']}"
									actionListener="#{entitiesBb.valueExpressionEditor.confirmLiteral}"
									styleClass="pulsante"							
									/>
							</webred:block>
							<webred:block 
								id="atomicExpression"
								htmlElementName="div" 
								style="position: absolute; visibility: hidden;"
								>
								<h:selectOneListbox
									disabled="#{not entitiesBb.valueExpressionEditor.editable}"
									style="width: 90%;"
									value="#{entitiesBb.valueExpressionEditor.key}"
									id="selectAtomicExpressions"
									size="12"
									ondblclick="execConfirmAtomic();"
									>
									<f:selectItems value="#{entitiesBb.fields}" />
								</h:selectOneListbox>
								<h:commandButton 
									id="confirmAtomicButton"
									disabled="#{not entitiesBb.valueExpressionEditor.editable}" 
									value="#{labels['common.words.Confirm']}"
									actionListener="#{entitiesBb.valueExpressionEditor.confirmAtomic}"
									styleClass="pulsante"							
									/>
							</webred:block>
							<webred:block 
								id="functionExpression"
								htmlElementName="div" 
								style="position: absolute; visibility: hidden;"
								>
								<h:selectOneListbox 
									disabled="#{not entitiesBb.valueExpressionEditor.editable}"
									style="width: 90%;"
									id="selectFunctions"
									onclick="submitForm(form);"
									value="#{entitiesBb.valueExpressionEditor.choosedFunction}"
									valueChangeListener="#{entitiesBb.valueExpressionEditor.chooseFunction}"
									size="4"
									>
									<f:selectItems 
										value="#{entitiesBb.functions}"
										/>
								</h:selectOneListbox>
								<f:verbatim><div 
									id="functionHelpTab"
									onmousedown="beginMove(this, event)"
									class="helpBox" 
									style="visibility: hidden; position: absolute; cursor: move; top: 50px; left: 5px; width: 150px; z-index: 10000;"
									>
									</f:verbatim>
									<h:outputText value="#{entitiesBb.valueExpressionEditor.functionDetail.functionHelp}" />
									<f:verbatim>
									<input type="button" value="Chiudi" onclick="this.parentNode.style.visibility = 'hidden';" />
								</div></f:verbatim>
								<h:dataTable
									id="functionArgumentsTable"
									rendered="#{not empty entitiesBb.valueExpressionEditor.functionDetail.f}" 
									value="#{entitiesBb.valueExpressionEditor.functionDetail.arguments}"
									var="row" 
									style="visibility: inherit;"
									styleClass="relationsTable"
									headerClass="relationsTableHeader"
									footerClass="relationsTableFooter"
									rowClasses="relationsTableOddRow,relationsTableEvenRow"
									columnClasses="relationsTableColumn"
									>
									<h:column id="functionArgumentsColumn1">
										<h:outputText 
											value="#{row.position + 1}"
											/>
									</h:column>
									<h:column id="functionArgumentsColumn2">
										<f:facet name="header">
											<h:panelGroup>
												<h:outputText value="#{entitiesBb.valueExpressionEditor.functionDetail.functionName}" />
												<f:verbatim>
													<input type="button" value="?" onclick="document.getElementById('functionHelpTab').style.visibility = 'visible';" />
												</f:verbatim>
											</h:panelGroup>
										</f:facet>
										<h:commandButton 
											disabled="#{not entitiesBb.valueExpressionEditor.editable}" 
											image="/pics/table.gif"
											alt="#{labels['expressions.function.insertValue.button']}"
											title="#{labels['expressions.function.insertValue.button']}"
											onclick=
												"var pippo = this.nextSibling.nextSibling; 
												pippo.style.position = 'static'; 
												pippo.style.visibility = 'visible';
												pippo.disabled = false;
												pippo.focus();
												pippo = pippo.nextSibling; 
												pippo.style.visibility = 'hidden';
												pippo.style.position = 'absolute';
												pippo.disabled = true;
												return false;"
											/>
										<h:commandButton 
											disabled="#{not entitiesBb.valueExpressionEditor.editable}" 
											image="/pics/insertText.gif"
											alt="#{labels['expressions.function.insertLiteralValue.button']}"
											title="#{labels['expressions.function.insertLiteralValue.button']}"
											onclick=
												"var pippo = this.nextSibling.nextSibling; 
												pippo.style.position = 'static'; 
												pippo.style.visibility = 'visible'; 
												pippo.disabled = false;
												pippo.focus();
												pippo = pippo.previousSibling; 
												pippo.style.visibility = 'hidden';
												pippo.style.position = 'absolute';
												pippo.disabled = true;
												return false;"
											/>
										<h:selectOneListbox
											disabled="#{not entitiesBb.valueExpressionEditor.editable}"
											style="#{empty row.key ? 'position: absolute; visibility: hidden;' : 'position: static; visibility: inherit;'}"
											ondblclick="this.style.visibility = 'hidden'; this.style.position = 'absolute';"
											value="#{row.key}"
											id="selectAtomicExpressionsForFunction"
											size="5"
											>
											<f:selectItems value="#{entitiesBb.fields}" />
										</h:selectOneListbox>
										<h:inputText 
											readonly="#{not entitiesBb.valueExpressionEditor.editable}"
											style="#{empty row.literal ? 'position: absolute; visibility: hidden;' : 'position: static; visibility: inherit;'}"
											value="#{row.literal}"
											/>
									</h:column>
									<h:column id="functionArgumentsColumn3">
										<h:commandButton 
											disabled="#{not entitiesBb.valueExpressionEditor.editable}" 
											rendered="#{entitiesBb.valueExpressionEditor.functionDetail.availableSpaceForMoreArguments}"
											title="#{labels['expressions.function.addArgumentAbove']}"
											alt="#{labels['expressions.function.addArgumentAbove']}"
											image="/pics/addAbove.gif"
											actionListener="#{row.addAboveMe}"
											/>
										<h:commandButton 
											disabled="#{not entitiesBb.valueExpressionEditor.editable}" 
											rendered="#{entitiesBb.valueExpressionEditor.functionDetail.availableSpaceForMoreArguments}"
											title="#{labels['expressions.function.addArgumentBelow']}"
											alt="#{labels['expressions.function.addArgumentBelow']}"
											image="/pics/addBelow.gif"
											actionListener="#{row.addBelowMe}"
											/>
										<h:commandButton 
											disabled="#{not entitiesBb.valueExpressionEditor.editable}" 
											rendered="#{entitiesBb.valueExpressionEditor.functionDetail.anyArgumentDeleteable}"
											title="#{labels['expressions.function.deleteArgument']}"
											alt="#{labels['expressions.function.deleteArgument']}"
											image="/pics/deleteRed.gif"
											actionListener="#{row.deleteMe}"
											/>
									</h:column>
								</h:dataTable>
								<h:commandButton 
									disabled="#{not entitiesBb.valueExpressionEditor.editable}" 
									value="#{labels['common.words.Confirm']}"
									actionListener="#{entitiesBb.valueExpressionEditor.confirmFunction}"
									styleClass="pulsante"							
									/>
							</webred:block>
							<webred:block 
								id="explicitSQLExpression"
								htmlElementName="div" 
								style="position: absolute; visibility: hidden;"
								>
								<h:inputTextarea
									rows="5" 
									readonly="#{not entitiesBb.valueExpressionEditor.editable}"
									value="#{entitiesBb.valueExpressionEditor.explicitSQL}"
									/>
								<h:outputText escape="false" value="<br />" />
								<h:commandButton 
									disabled="#{not entitiesBb.valueExpressionEditor.editable}" 
									value="#{labels['common.words.Confirm']}"
									actionListener="#{entitiesBb.valueExpressionEditor.confirmExplicitSQL}"
									styleClass="pulsante"							
									/>
							</webred:block>
						</webred:block>
						
					</div>
					
				</div>
				<h:commandLink id="editCancel" action="#{entitiesBb.startLife}">
				</h:commandLink>
				<h:inputHidden id="lastXScrollPosition" value="#{entitiesBb.lastXScrollPosition}" />
				<h:inputHidden id="lastYScrollPosition" value="#{entitiesBb.lastYScrollPosition}" />
				<h:inputHidden id="stepCount" value="#{entitiesBb.stepCount}" />
				<h:inputHidden id="modifingRelation" value="#{entitiesBb.modifingRelation}" />
			</h:form>
			<form name="showResultSetForm" target="QueryBuilderShowResultSetPopUpWindow" method="POST" action="<h:outputText escape='false' value='#{facesContext.externalContext.requestContextPath}/faces/querybuilder/showResultSet.jsp' />">
				<input 
					type="hidden" 
					name="<h:outputText value='#{showRSBb.statementRequestParamName}'/>" 
					value="<h:outputText value='#{entitiesBb.explicitSql}'/>" 
					/>
			</form>
			<div id="wait" />
			<div id="fade" />
		</body>

	</f:view>
</html>

