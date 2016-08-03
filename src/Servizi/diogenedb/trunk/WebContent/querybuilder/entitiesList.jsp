<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
<f:view>
	<head>
	<f:loadBundle basename="it.webred.diogene.querybuilder.labels"
		var="labels" />
  	<%--
	<link type="text/css" rel="stylesheet"
		href="../../css/querybuilder.css" />
 	--%>
 	<title><h:outputText value="#{labels['entityList.title']}" /></title>
	<h:outputText escape="false"
		value='<link href="../../css/style.css" rel="stylesheet" type="text/css" />' />

	<script language="JavaScript" src="../../scripts/dhtml.js">
			</script>
	
	<script language="JavaScript" src="../../scripts/querybuilder.js">
	</script>
 
	<script language="JavaScript">
				<!--
				function startEntitiesCreation()
				{
					var xWidth = window.screen.availWidth;
					var xHeight = window.screen.availHeight;
					var features = "screenX=0,screenY=0,left=0,top=0,location=no,menubar=no,status=no,resizable=no,width=" + xWidth + ",height=" + xHeight;
					var popup = window.open('<h:outputText escape="false" value="#{facesContext.externalContext.requestContextPath}/faces" />/querybuilder/inizio.jsp','EntitiesCreation',features,false);
					popup.resizeTo(xWidth,xHeight); // PER IE, CHE CONSIDERA LE DIMENSIONI PASSATE COME FEATURES DI widnow.open() COME DIMENSIONI DELLA CLIENT AREA, E NON COME DIMENSIONI DELLA FINESTRA TUTTA
				}
				
				
				function getFormName()
				{
					return document.forms[0].id;
				}
				function getFormId()
				{
					return getFormName() + ":";
				}

				function apriFiltroEntita(entityId)
				{
					url="filterEntity.jsp?entityId="+entityId;
					var xWidth = window.screen.availWidth;
					var xHeight = window.screen.availHeight;
					xWidth=xWidth-(xWidth/20);
					xHeight=xHeight-(xHeight/20);
					var features = "screenX=0,screenY=0,left=0,top=0,location=no,menubar=no,scrollbars=yes,status=no,resizable=yes,width=" + xWidth + ",height=" + xHeight;
					var popup = window.open(url,'filterEntity',features,false);
					popup.resizeTo(xWidth,xHeight); // PER IE, CHE CONSIDERA LE DIMENSIONI PASSATE COME FEATURES DI widnow.open() COME DIMENSIONI DELLA CLIENT AREA, E NON COME DIMENSIONI DELLA FINESTRA TUTTA
				
				}
				var conditionsBuilderPopUp = false;
				function openConditionsBuilderPopUpListener(url)
				{
					calculateDiv();
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
					document.forms[0].submit();
					//document.getElementById("fade").style.visibility = "hidden";
				}
				function relationsCancel()
				{
					clear_entitiesBuilderForm();
					document.forms['entitiesBuilderForm'].elements['autoScroll'].value=getScrolling();
					document.forms['entitiesBuilderForm'].elements['entitiesBuilderForm:_link_hidden_'].value='entitiesBuilderForm:relationsCancel';
					if(document.forms['entitiesBuilderForm'].onsubmit){
						if(document.forms['entitiesBuilderForm'].onsubmit()) document.forms['entitiesBuilderForm'].submit();
					}else{
						document.forms['entitiesBuilderForm'].submit();
					}
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
				
				function prepareOpenConditionBuilder()
				{
					document.forms[getFormName()].action = document.forms[getFormName()].action + "?openConditionPopup=true";
				}
				function openConditionBuilder()
				{
					if ("<h:outputText escape='true' value='true' rendered='#{not empty param.openConditionPopup and not empty entitiesLista.conditionsController.condition}' />")
						openConditionsBuilderPopUpListener("conditionsBuilder.jsp");
				}
				
				function calculateDiv()
				{
					var divHeight=0;
					//var d=document.getElementById("total");
					var d=document.childNodes[0].childNodes[1];
					if(d.offsetHeight)
					{
                     	divHeight=d.offsetHeight;
                   	}

                	else if(d.style.pixelHeight)
                	{
                     	divHeight=d.style.pixelHeight;
                    }
					document.getElementById("fade").style.height=divHeight;
				}
				function showError()
				{
					// Errori verificatisi nel EntitiesListController
					var msg = '<h:outputText escape="false" value="#{entitiesLista.errori}" />';
					if (msg)
						alert(msg);
					// Errori verificatisi nel EntitiesController
					var msg1 = document.getElementById(getFormId() + 'globalErrorMessage');;
					if (msg1 && msg1.value!=''){
						alert(msg1.value);
						msg1.value='';
					}
					
				}
				
				//-->
			</script>
	</head>

	<body   onload="showError(); openConditionBuilder();" onfocus="fuoco();" oncontextmenu="return false;">
				<script>
			<!--
			//               BACKSP   F1          F2          F3          F4          F5          F6          F7          F8          F9          F10         F11         F12          CTRL +  F, G, R
			disabilitaTasti([[8,true],[112,false],[113,false],[114,false],[115,false],[116,false],[117,false],[118,false],[119,false],[120,false],[121,false],[122,false],[123,false]],       [70,71,82]);
			//-->
			</script>
	<div id="total">
	<p class="spacer">&nbsp;</p>
	<div id="clearheader"></div>
	<h:form id="entitiesBuilderForm"  onsubmit="return viewConfirm();">
	<div style="text-align:center;margin-top:20px;height:45px;">
			<%-- 
			<h:commandLink value="Torna al Menù principale" action="#{MenuVisualizzatore.apriMenu}" actionListener="#{entitiesLista.invalidateSession}"></h:commandLink>
			--%>
			<h:commandLink value="Chiudi"  onclick="window.close();" actionListener="#{entitiesLista.invalidateSession}"></h:commandLink>
			
	</div>
		<h:inputHidden id="globalErrorMessage"  value="#{entitiesBb.globalErrorMessage}" />
		<h:messages globalOnly="false"></h:messages>
		<x:panelTabbedPane 
		tabContentStyleClass="tabContent" 
		activeTabStyleClass="activeTab"
		inactiveTabStyleClass="inactiveTab"
		 id="entitaTab" align="center" style="width:95%;" >
			<x:panelTab 
			 label="#{labels['entityList.userEntityTabPage.title']}" >
				<f:verbatim>
					<div id="userBlock" align="center">
				</f:verbatim>
									<f:verbatim>
						<div style="text-align:center;margin-top:20px;height:45px;">
					</f:verbatim>
							<h:commandButton
								disabled="#{not entitiesLista.canCreateUserEntity}"
								styleClass="pulsanteXL"
								id="startLifeButton"
								action="#{entitiesBb.startLife}"
								value="#{labels['common.startEntitiesCreation']}"
								actionListener="#{entitiesLista.returnFromEntityBuilder}"
								immediate="true"
							/>
							<h:commandButton
								disabled="#{not entitiesLista.canCreateUserEntity}"
								styleClass="pulsanteXL"
								id="getUserEntityButton"
								action="#{entitiesBb.getUserEntity}"
								value="#{labels['common.getUserEntity']}"
								actionListener="#{entitiesLista.returnFromEntityBuilder}"
								immediate="true"
							/>
					<f:verbatim>
						</div>
					</f:verbatim>  
				
		 		<h:dataTable 
		 			id="listaUserEntity" 
		 			styleClass="griglia"  
		 			value="#{entitiesLista.dvUserEntitiesList}" 
		 			var="item"
		 			cellpadding="0" cellspacing="0">
					<h:column>
						<f:facet name="header">
							<h:outputText id="nomeUserEntity" style="FONT-WEIGHT: bold;" value="#{labels['entityList.entityList.name.headerTitle']}" />
						</f:facet>
						<h:outputText value="#{item.name}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText id="descrUserentity" style="FONT-WEIGHT: bold;" value="#{labels['entityList.entityList.description.headerTitle']}" />
						</f:facet>
						<h:outputText value="#{item.desc}" />
						<h:outputText rendered="#{empty item.desc}" escape="false" value="&nbsp;"/>
					</h:column>
					<h:column>
					
						<f:facet name="header">
							<h:outputText id="descrUserentity" style="FONT-WEIGHT: bold;" value="" />
						</f:facet>
							<h:inputHidden  value="#{item.id}"/>
							<h:commandButton id="modificaUserButton"
								disabled="#{not item.entityEditable}"
								action="#{entitiesBb.editUserEntity}"
								actionListener="#{entitiesLista.returnFromEntityBuilder}"							
								onclick="document.getElementById('entitiesBuilderForm:modifingUser').value='TABLE#{item.id}'"
								styleClass="pulsante_sm"
								value="#{labels['entityList.editEntity.button']}"
								/>	
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText id="descrUserentity" style="FONT-WEIGHT: bold;" value="" />
						</f:facet>
							<h:commandButton id="eliminaUserButton"
								disabled="#{not item.entityDeletable}"
								action="#{entitiesBb.deleteUserEntity}"
								actionListener="#{entitiesLista.returnFromEntityBuilder}"							
								onclick="confirmOn(#{labels['entities.userEntity.deleteUserEntity.confirmationMessage']});document.getElementById('entitiesBuilderForm:modifingUser').value='TABLE#{item.id}'"
								styleClass="pulsante_sm"
								value="#{labels['entityList.deleteEntity.button']}"
								/>	
								
					</h:column>
					<h:column>
							<h:commandButton id="filtraUserButton"																					
								disabled="#{not item.entityViewable}"
								onclick="apriFiltroEntita(#{item.id})"														
								styleClass="pulsante_sm"
								style="color: #000000;"
								value="#{labels['entityList.viewEntity.button']}"
							/>
					</h:column>
					<h:column>
							<h:commandButton id="configuraUserButton"																					
								disabled="#{not item.entityEditable}"
								action="#{entitiesBb.editUserEntityLabels}"
								actionListener="#{entitiesLista.returnFromEntityBuilder}"							
								onclick="document.getElementById('entitiesBuilderForm:userEntityForLabelTypes').value='#{item.id}';document.getElementById('entitiesBuilderForm:userEntityNameForLabelTypes').value='#{item.desc}'"
								styleClass="pulsante_sm"
								value="#{labels['entityList.entityLabels.button']}"
							/>	
					</h:column>
				</h:dataTable>
				<f:verbatim>
					<br>
					<table  class="griglia" style="width: 60%;"  cellpadding="0" cellspacing="0" >
						<thead >
							<tr>
								<th colspan="2" style="text-align:center;">
				</f:verbatim>
								<h:outputText id="addFilterCond" value="#{labels['entities.addRelation.label']}" />
				<f:verbatim> 
								</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th colspan="2">
				</f:verbatim>
									<h:commandButton 
											disabled="#{not entitiesLista.canCreateRelation}"
											styleClass="pulsante" 
											id="insertUserRel" 
											value="#{labels['entities.proceed.button']}" 
											onclick="prepareOpenConditionBuilder();" 
											actionListener="#{entitiesLista.addRelation}" 
									/>
				<f:verbatim> 
									<th/>
							</tr>
							<tr>
							<td colspan="2" style="text-align:center;">
								</f:verbatim>
									<h:message for="insertUserRel" styleClass="errorMessage" />
									<h:messages globalOnly="true"></h:messages>
								<f:verbatim>
							</td>							
							</tr>							
						</tfoot>
						<tbody>
							<tr>
								<td align="center">
				</f:verbatim>
										<h:selectOneMenu id="dcUsFormer"
											onchange="submitForm(form);"
											valueChangeListener="#{entitiesLista.formerUserChangedInCombo}"						
											value="#{entitiesLista.choosedFormerUserEntityInRelation}">
												<f:selectItems value="#{entitiesLista.dcFormerUserEntitiesInRelation}" />
										</h:selectOneMenu>	
				<f:verbatim>
								 </td>
								 <td align="center"> 
			 	</f:verbatim>
			 							<h:selectOneMenu id="dcUsLatter" 
												onchange="submitForm(form);"
												valueChangeListener="#{entitiesLista.latterUserChangedInCombo}"												
												value="#{entitiesLista.choosedLatterUserEntityInRelation}">
													<f:selectItems value="#{entitiesLista.dcLatterUserEntitiesInRelation}" />
										</h:selectOneMenu>
				<f:verbatim> 
								</td>								
							</tr>
						</tbody>
					</table>					
				</div>
				</f:verbatim>
				<f:verbatim><div align="center"></f:verbatim>
				<h:dataTable  
					value="#{entitiesLista.simpleRelationsUsers}" 
					var="users" 
					binding="#{entitiesLista.forEntityTable}"
					styleClass="griglia"
					cellpadding="0" cellspacing="0">
					<f:facet name="header">
						<h:outputText value="#{labels['entityList.userEntity.headerTitle']}" />
					</f:facet>				
					<h:column>
							<h:outputText value="#{users.nameRelation}"/>
							<h:outputText rendered="#{empty users.nameRelation}" escape="false" value="&nbsp;"/>
					</h:column>					
					<h:column>
						<h:commandButton 
							id="modificaRelazioneUserButton"
							disabled="#{not users.editable}"
							styleClass="pulsante_sm"
							value="modifica"
							actionListener="#{entitiesLista.editRelation}"
							onclick="prepareOpenConditionBuilder()"
							title="#{labels['expressions.editRelation.button']}"
							alt="#{labels['expressions.editRelation.button']}"
						/>						
					</h:column>
					<h:column>
						<h:commandButton id="eliminaRelazioneUserButton"
						disabled="#{not users.editable}"
						onclick=" confirmOn(#{labels['expressions.removeRelation.confirmationMessage']})"
						title="#{labels['expressions.removeRelation.button']}"
						alt="#{labels['expressions.removeRelation.button']}"
						styleClass="pulsante_sm"
						value="elimina"
						actionListener="#{entitiesLista.deleteRelation}"
						/>													
					</h:column>					
				</h:dataTable>
				<f:verbatim></div></f:verbatim>
			</x:panelTab>
		
			<x:panelTab  label="#{labels['entityList.schemaEntityTabPage.title']}">
				<f:verbatim>
					<div align="center" id="schemaBlock">
				</f:verbatim> 
					<f:verbatim>
						<div style="text-align:center;margin-top:20px;height:45px;">
					</f:verbatim>
					
		 			<h:commandLink
		 				rendered="#{entitiesLista.canMapSchemaEntity}"
		 				value="#{labels['entityList.mapSchema.button']}" 
		 				action="#{MenuMetadata.apriCaricaTabelle}"
		 				actionListener="#{entitiesLista.returnFromEntityBuilder}" />
		 			<f:verbatim>
		 			</div>
		 			</f:verbatim>

					<h:dataTable styleClass="griglia" id="listaSchemaEntity" 
						value="#{entitiesLista.dcSchemaEntitiesList}" var="item"
						cellpadding="0" cellspacing="0">
						<h:column> 
							<f:facet name="header">
								<h:outputText id="nomeSchemaEntity" style="FONT-WEIGHT: bold;" value="#{labels['entityList.entityList.name.headerTitle']}" />
							</f:facet>
							<h:outputText value="#{item.name}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText id="descSchemaEntity" style="FONT-WEIGHT: bold;" value="#{labels['entityList.entityList.description.headerTitle']}" />
							</f:facet>
							<h:outputText value="#{item.desc}" />
							<h:outputText rendered="#{empty item.desc}" escape="false" value="&nbsp;"/>
						</h:column>
						<h:column>
							<h:commandButton id="filtraSchemaButton"																					
								disabled="#{not item.entityViewable}"
								onclick="apriFiltroEntita(#{item.id})"														
								styleClass="pulsante_sm"
								value="#{labels['entityList.viewEntity.button']}"
							/>
						</h:column>
						
					</h:dataTable>

    			<f:verbatim>
    			<br>
					<table class="griglia" style="width: 60%;">
						<thead>
							<tr>
								<th colspan="2" style="text-align:center;">
				</f:verbatim>
									<h:outputText id="addFilterCond1" value="#{labels['entities.addRelation.label']}" /> 
				<f:verbatim>
								</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th colspan="2">
				</f:verbatim>
								<h:commandButton 
		 							disabled="#{not entitiesLista.canCreateRelation}"
									styleClass="pulsante"
									value="#{labels['entities.proceed.button']}" 
									id="addRelSchemaButton"
									onclick="prepareOpenConditionBuilder()"
									actionListener="#{entitiesLista.addRelation}" 
									/>								
				<f:verbatim>
								</th>								
							</tr>
							<tr>
							<td colspan="2" style="text-align:center;">
								</f:verbatim>
									<h:message for="addRelSchemaButton" styleClass="errorMessage" />
									<h:messages globalOnly="false"></h:messages>
								<f:verbatim>
							</td>							
							</tr>
						</tfoot>
						<tbody>
							<tr>
								<td align="center">
				</f:verbatim>
									 <h:selectOneMenu id="dcShFormer" 
										onchange="submitForm(form);"
										valueChangeListener="#{entitiesLista.formerChangedInCombo}"
										value="#{entitiesLista.choosedFormerEntityInRelation}">
											<f:selectItems value="#{entitiesLista.dcFormerSchemaEntitiesInRelation}" />
									</h:selectOneMenu> 
									
				<f:verbatim>
								</td>
								<td align="center">
				</f:verbatim>
									<h:selectOneMenu id="dcShLatter" 
										onchange="submitForm(form);"
										valueChangeListener="#{entitiesLista.latterChangedInCombo}"
										value="#{entitiesLista.choosedLatterEntityInRelation}">
											<f:selectItems value="#{entitiesLista.dcLatterSchemaEntitiesInRelation}" />
									</h:selectOneMenu> 
									
				<f:verbatim>
								</td>
							</tr>
							<tr>
								<td>
									</f:verbatim>
									<h:selectBooleanCheckbox style="margin-left:20px;"  value="#{entitiesLista.latterSchemaEntityInOuter}"/>
									<h:outputText value="includi tutti i risultati della prima entità"/>
									<f:verbatim>
								</td>
							</tr>

							
						</tbody>
			  		</table>
				 </div>
			</f:verbatim>
				<f:verbatim><div align="center"></f:verbatim>
				<h:dataTable  
					value="#{entitiesLista.simpleRelationsSchema}" 
					var="schema" 
					binding="#{entitiesLista.forEntitySchemaTable}"
					styleClass="griglia"
					cellpadding="0" cellspacing="0">
					<f:facet name="header">
						<h:outputText value="#{labels['entityList.schemaEntity.headerTitle']}" />
					</f:facet>				
					<h:column>
							<h:outputText value="#{schema.nameRelation}"/>
					</h:column>					
					<h:column>
						<h:commandButton id="modificaRelazioneSchemaButton"
						    disabled="#{not schema.editable}"
							onclick="prepareOpenConditionBuilder()"
							styleClass="pulsante_sm"
							value="modifica"
							title="#{labels['expressions.editRelation.button']}"
							alt="#{labels['expressions.editRelation.button']}"
							actionListener="#{entitiesLista.editRelation}"
						/>
					</h:column>
					<h:column>
						<h:commandButton id="eliminaRelazioneSchemaButton"
						disabled="#{not schema.editable}"
						onclick=" confirmOn(#{labels['expressions.removeRelation.confirmationMessage']})"
						title="#{labels['expressions.removeRelation.button']}"
						alt="#{labels['expressions.removeRelation.button']}"
						styleClass="pulsante_sm"
						value="elimina"
						actionListener="#{entitiesLista.deleteRelation}"
						/>													
					</h:column>					
				</h:dataTable>
				<f:verbatim></div></f:verbatim>		
		</x:panelTab>		
	</x:panelTabbedPane>
						
	<h:commandLink id="relationsCancel" action="#{relationsBb.relationsCancel}">
	</h:commandLink>

				<h:inputHidden id="modifingUser" value="#{entitiesBb.userEntityToEdit}" />
				<h:inputHidden id="userEntityForLabelTypes" value="#{entitiesBb.userEntityForLabelTypes}" />
				<h:inputHidden id="userEntityNameForLabelTypes" value="#{entitiesBb.userEntityNameForLabelTypes}" />
				
	</h:form>
		 <div id="headerForEntityList">
			  <img src="../metadata/img/title_Diogene.png" width="174" height="35" />
			  <img src="../img/gestioneEntita-text.png" width="283" height="24" />
			  
		 </div>
		</div>
	</body>
</f:view>
<div id="wait" />
<div id="fade"  />	
</html>
