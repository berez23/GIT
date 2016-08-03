<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.webred.it/faces" prefix="webred" %>

<html>
	<f:loadBundle basename="it.webred.diogene.querybuilder.labels" var="labels"/>
	<f:view>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
			<meta http-equiv="pragma" content="no-cache">
			<meta http-equiv="Cache-Control" content="no-cache" />
			<title><h:outputText value="#{labels['expressions.title']}" /></title>
			<link type="text/css" rel="stylesheet" href="../../css/querybuilder.css" />
			<link type="text/css" rel="stylesheet" href="../../css/style.css" />
			<script language="JavaScript" src="../../scripts/dhtml.js">
			</script>
			<script language="JavaScript" src="../../scripts/querybuilder.js">
			</script>
			<script language="JavaScript">
				<!-- 
				// SALVA L'ID DELL'ELEMENTO CUI SI DOVRA' PORTARE IL
				// FOCUS AL PROSSIMO CARICAMENTO
				function saveNextIdToFocus(id)
				{
					document.getElementById(getFormId() + "nextFocusID").value = id;
				}
				// SPOSTA IL FOCUS NELL'ELEMENTO CORRISPONDENTE ALL'ID 
				// SALVATO PRECEDENTEMENTE, SE ESISTE
				function focusNext()
				{
					elementId = document.getElementById(getFormId() + "nextFocusID").value;
					if (elementId)
					{
						element = document.getElementById(elementId);
						if (element)
							element.focus();
					}
				}
				function unload()
				{
					if (!submitting && opener && opener.relationsCancel){
						//window.open('falsePop.jsp','falsePop','width=1,height=1,screenX=0,screenY=0,left=0,top=0,location=no,menubar=no,status=no,resizable=no');
						opener.relationsCancel();
					}
					if (!submitting && opener && opener.closeConditionsBuilderPopUpListener)
					{
						opener.closeConditionsBuilderPopUpListener();
					}
				}
				function showError()
				{
					var msg = '<h:outputText escape="false" value="#{relationsBb.errori}" />';
					if (msg)
						alert(msg);
				}
				
				function execConfirmAtomic()
				{
					document.getElementById("conditionsBuilderForm:confirmAtomicButton").click();
				}
				
				// FUNZIONI NECESSARIE ALL'EDITOR DELLE ESPRESSIONI
				function getFormName()
				{
					return document.forms[0].id;
				}
				function getFormId()
				{
					return getFormName() + ":";
				}
				var visibleExpressionTab = '<h:outputText value="#{relationsBb.valueExpressionEditor.valueExpressionSelectedTab}" />';
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
				//-->
			</script>
		</head>
		<body onLoad="focusNext();viewExpressionTab(visibleExpressionTab);showError();" onunload="unload();" oncontextmenu="return false;">
			<script>
			<!--
			//               BACKSP   F1          F2          F3          F4          F5          F6          F7          F8          F9          F10         F11         F12          CTRL +  F, G, R
			disabilitaTasti([[8,true],[112,false],[113,false],[114,false],[115,false],[116,false],[117,false],[118,false],[119,false],[120,false],[121,false],[122,false],[123,false]],       [70,71,82]);
			//-->
			</script>
			<div id="HEADER">
				<h1><h:outputText value="#{labels['expressions.title']}" /></h1>
			</div>
			<h:form id="conditionsBuilderForm" onsubmit="return viewConfirm();">
			<div id="WHOLE_BODY">
				
				<!-- KVESTO DIV CONTIENE L'ELENCO DELLE RELAZIONI -->
				<div class="LEFTCONDITION">
					<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
						<h:commandButton 
							value="#{labels['expressions.relationstable.insertRelation.button']}"
							alt="#{labels['expressions.relationstable.insertRelation.button.hint']}"
							title="#{labels['expressions.relationstable.insertRelation.button.hint']}"
							actionListener="#{relationsBb.generateRelation}"
							styleClass="pulsanteXL"
							/>
						<h:dataTable
							value="#{relationsBb.condition.relations}"
							var="row" 
							styleClass="relationsTable"
							headerClass="relationsTableHeader"
							footerClass="relationsTableFooter"
							rowClasses="relationsTableOddRow,relationsTableEvenRow"
							columnClasses="relationsTableColumn"
							>
							<h:column>
								<h:commandButton
									image="/pics/table.gif"
									alt="#{row.operand1Hint}" 
									title="#{row.operand1Hint}"
									actionListener="#{row.editOperand1}"
									rendered="#{row.operand1Enabled}"
									/>
								<h:commandButton
									image="/pics/addBelow.gif"
									alt="#{labels['entities.userEntity.fieldsTable.appendValue.button']}"
									title="#{labels['entities.userEntity.fieldsTable.appendValue.button']}"
									actionListener="#{row.appendNewValue2Operand1}"
									rendered="#{row.operand1Enabled and row.editable}"
									/>
								<h:outputText escape="false" value="<br />" />
								<h:dataTable 
									value="#{row.operand1AppendedValues}"
									var="appendedValue"
									id="chanExpressionsTableOperand1"
									rendered="#{not empty row.operand1AppendedValues}"
									>
									<h:column>
										<h:selectOneMenu value="#{appendedValue.chainOperator}">
											<f:selectItems value="#{relationsBb.chainOperators}" />
										</h:selectOneMenu>
									</h:column>
									<h:column>
										<h:commandButton
											image="/pics/table.gif"
											alt="#{appendedValue.hint}"
											title="#{appendedValue.hint}"
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
									<h:outputText value="#{labels['expressions.relationstable.operator.label']}" />
								</f:facet>
								<h:selectOneMenu 
									id="relationalOperators"
									value="#{row.operatorKey}"
									onchange="submitForm(form);"
									valueChangeListener="#{row.chooseOperator}"
									>
									<f:selectItems value="#{relationsBb.relationalOperators}" />
								</h:selectOneMenu>	
							</h:column>
							<h:column>
								<h:commandButton
									image="/pics/table.gif"
									alt="#{row.operand2Hint}"
									title="#{row.operand2Hint}"
									actionListener="#{row.editOperand2}"
									rendered="#{row.operand2Enabled}"
									/>
								<h:commandButton
									image="/pics/addBelow.gif"
									alt="#{labels['entities.userEntity.fieldsTable.appendValue.button']}"
									title="#{labels['entities.userEntity.fieldsTable.appendValue.button']}"
									actionListener="#{row.appendNewValue2Operand2}"
									rendered="#{row.operand2Enabled and row.editable}"
									/>
								<h:outputText escape="false" value="<br />" />
								<h:dataTable 
									value="#{row.operand2AppendedValues}"
									var="appendedValue"
									id="chanExpressionsTableOperand2"
									rendered="#{not empty row.operand2AppendedValues}"
									>
									<h:column>
										<h:selectOneMenu value="#{appendedValue.chainOperator}">
											<f:selectItems value="#{relationsBb.chainOperators}" />
										</h:selectOneMenu>
									</h:column>
									<h:column>
										<h:commandButton
											image="/pics/table.gif"
											alt="#{appendedValue.hint}"
											title="#{appendedValue.hint}"
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
								<h:commandButton
									image="/pics/table.gif"
									alt="#{row.operand3Hint}"
									title="#{row.operand3Hint}"
									actionListener="#{row.editOperand3}"
									rendered="#{row.operand3Enabled}"
									/>
								<h:commandButton
									image="/pics/addBelow.gif"
									alt="#{labels['entities.userEntity.fieldsTable.appendValue.button']}"
									title="#{labels['entities.userEntity.fieldsTable.appendValue.button']}"
									actionListener="#{row.appendNewValue2Operand3}"
									rendered="#{row.operand3Enabled and row.editable}"
									/>
								<h:outputText escape="false" value="<br />" />
								<h:dataTable 
									value="#{row.operand3AppendedValues}"
									var="appendedValue"
									id="chanExpressionsTableOperand3"
									rendered="#{not empty row.operand3AppendedValues}"
									>
									<h:column>
										<h:selectOneMenu value="#{appendedValue.chainOperator}">
											<f:selectItems value="#{relationsBb.chainOperators}" />
										</h:selectOneMenu>
									</h:column>
									<h:column>
										<h:commandButton
											image="/pics/table.gif"
											alt="#{appendedValue.hint}"
											title="#{appendedValue.hint}"
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
								<h:commandButton
									onclick="confirmOn(#{labels['expressions.removeRelation.confirmationMessage']})"
									image="/pics/deleteRed.gif"
									title="#{labels['expressions.removeRelation.button']}"
									alt="#{labels['expressions.removeRelation.button']}"
									actionListener="#{row.removeMe}"
									/>
							</h:column>
						</h:dataTable>
						<h:commandButton 
							value="#{labels['common.OK']}"
							title="#{labels['expressions.thatsAll.button']}"
							alt="#{labels['expressions.thatsAll.button']}"
							action="#{relationsBb.relationsOK}"
							styleClass="pulsante"
							/>
						<h:commandButton 
							onclick="confirmOn(#{labels['expressions.leaveRelationUnmodified.confirmationMessage']})"
							value="#{labels['common.cancel']}"
							title="#{labels['expressions.cancelAll.button']}"
							alt="#{labels['expressions.cancelAll.button']}"
							value="Annulla"
							action="#{relationsBb.relationsCancel}"
							styleClass="pulsante"
							/>
					</div>
				</div>
				
				
				<!-- KVESTO DIV CONTIENE L'EDITOR DELLE ESPRESSIONI -->
				<div class="RIGHT">
					<div class="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi">
						<webred:block 
							rendered="#{not empty relationsBb.valueExpressionEditor && not empty relationsBb.valueExpressionEditor.value && not empty relationsBb.valueExpressionEditor.value.editingValue}"
							htmlElementName="div"
							styleClass="perColpaDiIEcheNonSupportaIlChildSelectorECalcolaMaleLeDimensioniDeiBlocchi"
							>
							<webred:block
								id="chooseExpressionCategory"
								htmlElementName="div" 
								>
								<h:selectOneRadio 
									disabled="#{not relationsBb.valueExpressionEditor.editable}"
									layout="lineDirection"
									value="#{relationsBb.valueExpressionEditor.valueExpressionSelectedTab}"
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
									readonly="#{not relationsBb.valueExpressionEditor.editable}"
									value="#{relationsBb.valueExpressionEditor.literal}"
									/>
								<h:outputText escape="false" value="<br />" />
								<h:commandButton 
									disabled="#{not relationsBb.valueExpressionEditor.editable}" 
									value="#{labels['common.words.Confirm']}"
									actionListener="#{relationsBb.valueExpressionEditor.confirmLiteral}"
									styleClass="pulsante"
									/>
							</webred:block>
							<webred:block 
								id="atomicExpression"
								htmlElementName="div" 
								style="position: absolute; visibility: hidden;"
								>
								<h:selectOneListbox
									ondblclick="execConfirmAtomic();"
									disabled="#{not relationsBb.valueExpressionEditor.editable}"
									style="width: 90%;"
									value="#{relationsBb.valueExpressionEditor.key}"
									id="selectAtomicExpressions"
									size="12"
									>
									<f:selectItems value="#{relationsBb.valueExpressionEditor.expressionsSource.fields}" />
								</h:selectOneListbox>
								<h:commandButton 
									id="confirmAtomicButton"
									disabled="#{not relationsBb.valueExpressionEditor.editable}" 
									value="#{labels['common.words.Confirm']}"
									actionListener="#{relationsBb.valueExpressionEditor.confirmAtomic}"
									styleClass="pulsante"
									/>
							</webred:block>
							<webred:block 
								id="functionExpression"
								htmlElementName="div" 
								style="position: absolute; visibility: hidden;"
								>
								<h:selectOneListbox 
									style="width: 90%;"
									id="selectFunctions"
									disabled="#{not relationsBb.valueExpressionEditor.editable}"
									onclick="submitForm(form);"
									value="#{relationsBb.valueExpressionEditor.choosedFunction}"
									valueChangeListener="#{relationsBb.valueExpressionEditor.chooseFunction}"
									size="4"
									>
									<f:selectItems 
										value="#{relationsBb.valueExpressionEditor.expressionsSource.functions}"
										/>
								</h:selectOneListbox>
								<f:verbatim><div 
									id="functionHelpTab"
									onmousedown="beginMove(this, event)"
									class="helpBox" 
									style="visibility: hidden; position: absolute; cursor: move; top: 50px; left: 5px; width: 150px; z-index: 10000;"
									>
									</f:verbatim>
									<h:outputText value="#{relationsBb.valueExpressionEditor.functionDetail.functionHelp}" />
									<f:verbatim>
									<input type="button" value="Chiudi" onclick="this.parentNode.style.visibility = 'hidden';" />
								</div></f:verbatim>
								<h:dataTable
									id="functionArgumentsTable"
									rendered="#{not empty relationsBb.valueExpressionEditor.functionDetail.f}" 
									value="#{relationsBb.valueExpressionEditor.functionDetail.arguments}"
									var="row" 
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
												<h:outputText value="#{relationsBb.valueExpressionEditor.functionDetail.functionName}" />
												<f:verbatim>
													<input type="button" value="?" onclick="document.getElementById('functionHelpTab').style.visibility = 'visible';" />
												</f:verbatim>
											</h:panelGroup>
										</f:facet>
										<h:commandButton 
											image="/pics/table.gif"
											disabled="#{not relationsBb.valueExpressionEditor.editable}" 
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
											image="/pics/insertText.gif"
											disabled="#{not relationsBb.valueExpressionEditor.editable}" 
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
											style="#{empty row.key ? 'position: absolute; visibility: hidden;' : 'position: static; visibility: inherit;'}"
											ondblclick="this.style.visibility = 'hidden'; this.style.position = 'absolute';"
											disabled="#{not relationsBb.valueExpressionEditor.editable}"
											value="#{row.key}"
											id="selectAtomicExpressionsForFunction"
											size="5"
											>
											<f:selectItems value="#{relationsBb.fields}" />
										</h:selectOneListbox>
										<h:inputText 
											disabled="#{not relationsBb.valueExpressionEditor.editable}"
											style="#{empty row.literal ? 'position: absolute; visibility: hidden;' : 'position: static; visibility: inherit;'}"
											value="#{row.literal}"
											/>
									</h:column>
									<h:column id="functionArgumentsColumn3">
										<h:commandButton 
											disabled="#{not relationsBb.valueExpressionEditor.editable}" 
											rendered="#{relationsBb.valueExpressionEditor.functionDetail.availableSpaceForMoreArguments}"
											title="#{labels['expressions.function.addArgumentAbove']}"
											alt="#{labels['expressions.function.addArgumentAbove']}"
											image="/pics/addAbove.gif"
											actionListener="#{row.addAboveMe}"
											/>
										<h:commandButton 
											disabled="#{not relationsBb.valueExpressionEditor.editable}" 
											rendered="#{relationsBb.valueExpressionEditor.functionDetail.availableSpaceForMoreArguments}"
											title="#{labels['expressions.function.addArgumentBelow']}"
											alt="#{labels['expressions.function.addArgumentBelow']}"
											image="/pics/addBelow.gif"
											actionListener="#{row.addBelowMe}"
											/>
										<h:commandButton 
											disabled="#{not relationsBb.valueExpressionEditor.editable}" 
											rendered="#{relationsBb.valueExpressionEditor.functionDetail.anyArgumentDeleteable}"
											title="#{labels['expressions.function.deleteArgument']}"
											alt="#{labels['expressions.function.deleteArgument']}"
											image="/pics/deleteRed.gif"
											actionListener="#{row.deleteMe}"
											/>
									</h:column>
								</h:dataTable>
								<h:commandButton 
									disabled="#{not relationsBb.valueExpressionEditor.editable}" 
									value="#{labels['common.words.Confirm']}"
									actionListener="#{relationsBb.valueExpressionEditor.confirmFunction}"
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
									readonly="#{not relationsBb.valueExpressionEditor.editable}"
									value="#{relationsBb.valueExpressionEditor.explicitSQL}"
									/>
								<h:outputText escape="false" value="<br />" />
								<h:commandButton 
									disabled="#{not relationsBb.valueExpressionEditor.editable}" 
									value="#{labels['common.words.Confirm']}"
									actionListener="#{relationsBb.valueExpressionEditor.confirmExplicitSQL}"
									styleClass="pulsante"
									/>
							</webred:block>
						</webred:block>
					</div>
				</div>
			<h:inputHidden id="nextFocusID" value="#{relationsBb.nextFocusID}" />
			<h:inputHidden id="stepCount" value="#{relationsBb.stepCount}" />
			</div>
			</h:form>
			<div id="wait" />
		</body>
	</f:view>
</html>
