<%@ page contentType="text/html; charset=Cp1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<f:loadBundle basename="it.webred.diogene.querybuilder.labels"
	var="labels" />
<f:view>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache" />
	<title><h:outputText value="#{labels['buildSql.title']}" /></title>
	<link type="text/css" rel="stylesheet" href="../../css/dhtml.css" />
	<link type="text/css" rel="stylesheet" href="../../css/querybuilder.css" />
	<link type="text/css" rel="stylesheet" href="../../css/style.css" />
	<script language="JavaScript" src="../../scripts/dhtml.js">
			</script>
	<script language="JavaScript" src="../../scripts/querybuilder.js">
			</script>
	<script language="JavaScript">
				<!--
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
					var msg = '<h:outputText escape="false" value="#{entitiesBb.globalErrorMessage}" />';
					if (msg)
						alert(msg);
				}
				//-->
			</script>
	</head>
	<body onload="showError();" oncontextmenu="return false;">
	<script>
			<!--
			//               BACKSP   F1          F2          F3          F4          F5          F6          F7          F8          F9          F10         F11         F12          CTRL +  F, G, R
			disabilitaTasti([[8,true],[112,false],[113,false],[114,false],[115,false],[116,false],[117,false],[118,false],[119,false],[120,false],[121,false],[122,false],[123,false]],       [70,71,82]);
			//-->
			</script>
	<h:form id="filterEntityForm" onsubmit="return viewConfirm();">
		<h:inputHidden id="entityFilterIdHidden"
			value="#{filterEntityBb.filterEntityId}" />
		<h:inputHidden id="entityIdHidden"
			value="#{filterEntityBb.entityId}" />
		<h:inputHidden id="entityIdHiddenSql"
			value="#{filterEntityBb.explicitSql}" />
		<h:panelGrid columns="1" align="center">
			<t:saveState id="saveCol" value="columnDataList" />
			<t:dataTable id="dataColumn" width="100%" 
				styleClass="grigliaColonne"
				var="col" value="#{columnDataList.columns}" preserveDataModel="true">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{labels['buildSql.fieldColumn.label']}" />
					</f:facet>
					<h:outputText value="#{col.fieldName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{labels['buildSql.operatorColumn.label']}" />
					</f:facet>
					<h:selectOneMenu value="#{col.operator}">
						<f:selectItem itemValue="" itemLabel="" />
						<f:selectItems value="#{col.operatorsList}" />
					</h:selectOneMenu>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{labels['buildSql.valueColumn.label']}" />
					</f:facet>
					<h:inputText value="#{col.value}" rendered="#{not col.date}" />
					<t:inputCalendar id="calendar" monthYearRowClass="yearMonthHeader"
						weekRowClass="weekHeader" currentDayCellClass="currentDayCell"
						value="#{col.valueDate}" renderAsPopup="true"
						popupTodayString="#{labels['common.calendar.popupTodayString']}"
						popupWeekString="#{labels['common.calendar.popupWeekString']}"
						popupDateFormat="dd/MM/yyyy" rendered="#{col.date}" />
				</h:column>
			</t:dataTable>
		</h:panelGrid>
		<h:panelGrid columns="4" align="center">
			<h:commandButton id="executeSqlButton"
				value="#{labels['buildSql.executeSql.button']}"
				actionListener="#{filterEntityBb.executeSql}" 
				styleClass="pulsante"
				/>
			<h:outputText value=" Estraendo al Massimo " />
			<h:selectOneMenu id="fetchRowsCombo"
				value="#{filterEntityBb.fetchRows}">
				<f:selectItem itemValue="10" itemLabel="10" />
				<f:selectItem itemValue="20" itemLabel="20" />
				<f:selectItem itemValue="50" itemLabel="50" />
				<f:selectItem itemValue="100" itemLabel="100" />
				<f:selectItem itemValue="150" itemLabel="150" />
				<f:selectItem itemValue="200" itemLabel="200" />
			</h:selectOneMenu>
			<h:outputText value=" Righe!" />
			<h:messages globalOnly="false"></h:messages>
		</h:panelGrid>
		<!-- GRIGLIA ORDINATA Visibile solo se le righe visualizzate >= righe reali della queri -->
		<h:panelGrid columns="1" align="center"
			rendered="#{0 < resultsDataList.realRowCount and resultsDataList.realRowCount  <= filterEntityBb.fetchRows}">
			<t:dataTable id="data" 
				styleClass="grigliaColonne"
				var="row" rows="10" value="#{resultsDataList.data}"
				preserveDataModel="false" sortColumn="#{resultsDataList.sort}"
				sortAscending="#{resultsDataList.ascending}" preserveSort="false">
				<t:columns id="columns" value="#{resultsDataList.columnHeaders}"
					var="columnHeader" style="width:#{resultsDataList.columnWidth}px">
					<f:facet name="header">
						<t:commandSortHeader columnName="#{columnHeader.label}"
							arrow="false" immediate="false">
							<f:facet name="ascending">
								<t:graphicImage value="../../img/ascending-arrow.gif"
									rendered="true" border="0" />
							</f:facet>
							<f:facet name="descending">
								<t:graphicImage value="../../img/descending-arrow.gif"
									rendered="true" border="0" />
							</f:facet>
							<h:outputText value="#{columnHeader.label}" />
						</t:commandSortHeader>
					</f:facet>
					<!-- row is also available -->
					<h:outputText value="#{resultsDataList.columnValue}" />
				</t:columns>
			</t:dataTable>
			<h:panelGrid align="center" columns="1" styleClass="scrollerTable2"
				columnClasses="standardTable_ColumnCentered">
				<t:dataScroller id="scroll1" for="data" fastStep="10"
					pageCountVar="pageCount" pageIndexVar="pageIndex"
					styleClass="scroller" paginator="true" paginatorMaxPages="9"
					paginatorTableClass="paginator"
					paginatorActiveColumnStyle="font-weight:bold;">
					<f:facet name="first">
						<t:graphicImage url="../../img/arrow-first.gif" border="1" />
					</f:facet>
					<f:facet name="last">
						<t:graphicImage url="../../img/arrow-last.gif" border="1" />
					</f:facet>
					<f:facet name="previous">
						<t:graphicImage url="../../img/arrow-previous.gif" border="1" />
					</f:facet>
					<f:facet name="next">
						<t:graphicImage url="../../img/arrow-next.gif" border="1" />
					</f:facet>
					<f:facet name="fastforward">
						<t:graphicImage url="../../img/arrow-ff.gif" border="1" />
					</f:facet>
					<f:facet name="fastrewind">
						<t:graphicImage url="../../img/arrow-fr.gif" border="1" />
					</f:facet>
				</t:dataScroller>
			</h:panelGrid>
			<h:panelGrid align="center" columns="1" styleClass="scrollerTable2"
				columnClasses="standardTable_ColumnCentered">
				<t:dataScroller id="scroll2" for="data" rowsCountVar="rowsCount"
					displayedRowsCountVar="displayedRowsCountVar"
					firstRowIndexVar="firstRowIndex" lastRowIndexVar="lastRowIndex"
					pageCountVar="pageCount" pageIndexVar="pageIndex">
					<h:outputFormat
						value="Trovate {0} righe visualizzate {1} alla volta,  da {2} a {3}. Pagina {4} / {5}"
						styleClass="standard">
						<f:param value="#{rowsCount}" />
						<f:param value="#{displayedRowsCountVar}" />
						<f:param value="#{firstRowIndex}" />
						<f:param value="#{lastRowIndex}" />
						<f:param value="#{pageIndex}" />
						<f:param value="#{pageCount}" />
					</h:outputFormat>
					<h:outputLink
						rendered="#{resultsDataList.exportable}"
						value="#{facesContext.externalContext.requestContextPath}/exportCSV">
						<h:outputText value=" Esporta su CSV" />
					</h:outputLink>
				</t:dataScroller>

			</h:panelGrid>
		</h:panelGrid>

		<!-- GRIGLIA NON ORDINATA Visibile solo se le righe visualizzate < righe reali della query -->
		<h:panelGrid columns="1" align="center"
			rendered="#{0 < resultsDataList.realRowCount and resultsDataList.realRowCount > filterEntityBb.fetchRows}">
			<t:dataTable id="dataNoSort" 
				styleClass="grigliaColonne"
				var="row" rows="10" value="#{resultsDataList.data}"
				preserveDataModel="false">
				<t:columns id="columns" value="#{resultsDataList.columnHeaders}"
					var="columnHeader" style="width:#{resultsDataList.columnWidth}px">
					<f:facet name="header">
						<h:outputText value="#{columnHeader.label}" />
					</f:facet>
					<!-- row is also available -->
					<h:outputText value="#{resultsDataList.columnValue}" />
				</t:columns>
			</t:dataTable>
			<h:panelGrid align="center" columns="1" styleClass="scrollerTable2"
				columnClasses="standardTable_ColumnCentered">
				<t:dataScroller id="scroll1NoSort" for="dataNoSort" fastStep="10"
					pageCountVar="pageCount" pageIndexVar="pageIndex"
					styleClass="scroller" paginator="true" paginatorMaxPages="9"
					paginatorTableClass="paginator"
					paginatorActiveColumnStyle="font-weight:bold;">
					<f:facet name="first">
						<t:graphicImage url="../../img/arrow-first.gif" border="1" />
					</f:facet>
					<f:facet name="last">
						<t:graphicImage url="../../img/arrow-last.gif" border="1" />
					</f:facet>
					<f:facet name="previous">
						<t:graphicImage url="../../img/arrow-previous.gif" border="1" />
					</f:facet>
					<f:facet name="next">
						<t:graphicImage url="../../img/arrow-next.gif" border="1" />
					</f:facet>
					<f:facet name="fastforward">
						<t:graphicImage url="../../img/arrow-ff.gif" border="1" />
					</f:facet>
					<f:facet name="fastrewind">
						<t:graphicImage url="../../img/arrow-fr.gif" border="1" />
					</f:facet>
				</t:dataScroller>
			</h:panelGrid>
			<h:panelGrid align="center" columns="1" styleClass="scrollerTable2"
				columnClasses="standardTable_ColumnCentered">
				<t:dataScroller id="scroll2NoSort" for="dataNoSort"
					rowsCountVar="rowsCount"
					displayedRowsCountVar="displayedRowsCountVar"
					firstRowIndexVar="firstRowIndex" lastRowIndexVar="lastRowIndex"
					pageCountVar="pageCount" pageIndexVar="pageIndex">
					<h:outputFormat value="Trovate {0} righe (estratte solo le prime "
						styleClass="standard">
						<f:param value="#{resultsDataList.realRowCount}" />
					</h:outputFormat>
					<h:outputFormat
						value="{0}) visualizzate {1} alla volta,  da {2} a {3}. Pagina {4} / {5}"
						styleClass="standard">
						<f:param value="#{rowsCount}" />
						<f:param value="#{displayedRowsCountVar}" />
						<f:param value="#{firstRowIndex}" />
						<f:param value="#{lastRowIndex}" />
						<f:param value="#{pageIndex}" />
						<f:param value="#{pageCount}" />
					</h:outputFormat>
					<h:outputLink
						rendered="#{resultsDataList.exportable}"
						value="#{facesContext.externalContext.requestContextPath}/exportCSV">
						<h:outputText value=" Esporta su CSV" />
					</h:outputLink>
				</t:dataScroller>

			</h:panelGrid>
		</h:panelGrid>

	</h:form>
	<div id="wait" />
	<div id="fade" />
	</body>

</f:view>
</html>

