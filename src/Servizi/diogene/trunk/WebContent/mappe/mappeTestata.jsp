<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.escsolution.escwebgis.mappe.bean.Mappe"%>
<%@page import="it.escsolution.escwebgis.mappe.logic.MappeLogic"%>

<html>

	<head>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
	</head>
	
	<% int rowIndex = Integer.parseInt(request.getParameter("index"));
	Mappe mappe = (Mappe)session.getAttribute(MappeLogic.MAPPE_BEAN + rowIndex); %>
	
	<script>	
		function seleziona(index) {
			for (i = 0; i < 7; i++) {
				if (document.getElementById("td" + i)) {
					if (index == i) {
						document.getElementById("td" + i).style.backgroundColor = "#e8f0ff";
					} else {
						document.getElementById("td" + i).style.backgroundColor = "#f8f8f8";
					}
				}				
			}
			var frameHref = "mappeMappa.jsp";
			var mode = "";
			if (index == 0) {
				if('<%=mappe.getComune()%>' == "" || '<%=mappe.getFoglio()%>' == "" || '<%=mappe.getParticella()%>' == "") {
					alert('Impossibile visualizzare la mappa.\n Il record selezionato non ha dati sufficienti per essere localizzato.');
					return;
				}
				frameHref = "mappePerPopup.jsp";
				parent.frames["mappa"].location.href = frameHref;
				var popup = window.open("../viewer/applet/index.jsp?cn=" + '<%=mappe.getComune()%>' + "&fgl=" 
				+ '<%=mappe.getFoglio()%>' + "&par=" + '<%=mappe.getParticella()%>',"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								 
				popup.focus();
				return;
			} else if (index == 1) {
				if (<%=mappe.getLatitudine()%> == 0) {
					alert('Visione prospettica non disponibile per la particella selezionata');
				} else {
					frameHref = '../ewg/virtualearth.jsp?latitudine=' + '<%=mappe.getLatitudine()%>' 
					+ '&longitudine=' + '<%=mappe.getLongitudine()%>';
				}
			} else if (index == 2) {
				frameHref = '<%= request.getContextPath() %>/CatastoImmobili?popup3DProspective=true&f='
				+ '<%=mappe.getFoglio()%>' + '&p=' + '<%=mappe.getParticella()%>' + '&cod_ente=' + '<%=mappe.getComune()%>';
			} else if (index == 3) {
				mode = "56";
				frameHref = '<%= request.getContextPath() %>/Mappe?mode=' + mode + '&foglio=' 
				+ '<%=mappe.getFoglio()%>' + '&comune=' + '<%=mappe.getComune()%>'
				+ '&index=' + '<%=rowIndex%>';
			} else if (index == 4) {
				mode = "CV";
				frameHref = '<%= request.getContextPath() %>/Mappe?mode=' + mode + '&foglio='
				+ '<%=mappe.getFoglio()%>' + '&comune=' + '<%=mappe.getComune()%>'
				+ '&index=' + '<%=rowIndex%>';
			} else if (index == 5) {
				mode = "CC";
				frameHref = '<%= request.getContextPath() %>/Mappe?mode=' + mode + '&foglio=' 
				+ '<%=mappe.getFoglio()%>' + '&comune=' + '<%=mappe.getComune()%>'
				+ '&index=' + '<%=rowIndex%>';
			} else if (index == 6) {
				mode = "DE";
				frameHref = '<%= request.getContextPath() %>/Mappe?mode=' + mode + '&foglio=' 
				+ '<%=mappe.getFoglio()%>' + '&comune=' + '<%=mappe.getComune()%>'
				+ '&index=' + '<%=rowIndex%>';
			}
			
			if (index == 3 || index == 4 || index == 5 || index == 6) {
				try {
					if (parent.frames["mappa"].location.href.indexOf("/Mappe?mode") == -1) {
						parent.frames["mappa"].location.href = frameHref + "&start=yes";
					} else {		
						parent.frames["mappa"].frames["link"].location.href = frameHref;			
					}
					if (mode == "56") {
						parent.frames["mappa"].document.getElementById("visualizza").cols = "150, *, 0, 0, 0";
					} else if (mode == "CV") {
						parent.frames["mappa"].document.getElementById("visualizza").cols = "150, 0, *, 0, 0";
					} else if (mode == "CC") {
						parent.frames["mappa"].document.getElementById("visualizza").cols = "150, 0, 0, *, 0";
					} else if (mode == "DE") {						
						parent.frames["mappa"].document.getElementById("visualizza").cols = "150, 0, 0, 0, *";
					}
				} catch (e) {
					parent.frames["mappa"].location.href = frameHref + "&start=yes";
				}
			} else {
				parent.frames["mappa"].location.href = frameHref;
			}
		}
	</script>
	
	<body topmargin="0" leftmargin="0">
		<table style="width: 100%; height: 100%;" cellpadding="0" cellspacing="0">
			<tr>
				<td style="text-align: center; vertical-align: middle; background-color: #FFFFFF; border-style: none;">
					<div align="center" class="extWinTDTitle">
						<span class="extWinTXTTitle">
							Selezionare una delle schede per visualizzare la mappa corrispondente
						</span>
					</div>
				</td>
			</tr>
			<tr>
				<td style="vertical-align: bottom; background-color: #FFFFFF; border-style: none; height: 40px;">
					<table style="width: 100%; height: 100%; border-collapse: collapse; margin-left: 15px;">
						<tr>
							<%int totalWidth = 0;
							int pctWidth = 13;%>
							<% if (MappeLogic.isMappaVisibile(MappeLogic.IDX_VISUALIZZATORE, request)) { %>
								<td id="td<%=MappeLogic.IDX_VISUALIZZATORE%>" style="text-align: center; background-color: #f8f8f8; cursor: pointer;
								border: solid 4px #e8f0ff; width: <%=pctWidth%>%;" onclick="javascript:seleziona(<%=MappeLogic.IDX_VISUALIZZATORE%>);">
									<img src="../ewg/images/Localizza.gif" border="0" />						
								</td>
								<%totalWidth += pctWidth;%>
							<% }%>							
							<% if (MappeLogic.isMappaVisibile(MappeLogic.IDX_3D, request)) { %>
								<td id="td<%=MappeLogic.IDX_3D%>" style="text-align: center; background-color: #f8f8f8; cursor: pointer;
								border: solid 4px #e8f0ff; width: <%=pctWidth%>%;" onclick="javascript:seleziona(<%=MappeLogic.IDX_3D%>);">
									<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
								</td>
								<%totalWidth += pctWidth;%>
							<% }%>
							<% if (MappeLogic.isMappaVisibile(MappeLogic.IDX_3D_PROSP, request)) { %>
								<td id="td<%=MappeLogic.IDX_3D_PROSP%>" style="text-align: center; background-color: #f8f8f8; cursor: pointer;
								border: solid 4px #e8f0ff; width: <%=pctWidth%>%;" onclick="javascript:seleziona(<%=MappeLogic.IDX_3D_PROSP%>);">
									<img src="../ewg/images/3dprospective.png" border="0" width="30" height="30" />
								</td>
								<%totalWidth += pctWidth;%>
							<% }%>
							<% if (MappeLogic.isMappaVisibile(MappeLogic.IDX_IMPIANTO_1956, request)) { %>
								<td id="td<%=MappeLogic.IDX_IMPIANTO_1956%>" style="text-align: center; background-color: #f8f8f8; cursor: pointer;
								border: solid 4px #e8f0ff; width: <%=pctWidth%>%;" onclick="javascript:seleziona(<%=MappeLogic.IDX_IMPIANTO_1956%>);">
									<span class="extWinTXTTitle">Cartografia catastale 1956</span>
								</td>
								<%totalWidth += pctWidth;%>
							<% }%>
							<% if (MappeLogic.isMappaVisibile(MappeLogic.IDX_COPIONI_VISURA, request)) { %>
								<td id="td<%=MappeLogic.IDX_COPIONI_VISURA%>" style="text-align: center; background-color: #f8f8f8; cursor: pointer;
								border: solid 4px #e8f0ff; width: <%=pctWidth%>%;" onclick="javascript:seleziona(<%=MappeLogic.IDX_COPIONI_VISURA%>);">
									<span class="extWinTXTTitle">Copioni di visura</span>
								</td>
								<%totalWidth += pctWidth;%>
							<% }%>
							<% if (MappeLogic.isMappaVisibile(MappeLogic.IDX_CESSATO_CATASTO, request)) { %>
								<td id="td<%=MappeLogic.IDX_CESSATO_CATASTO%>" style="text-align: center; background-color: #f8f8f8; cursor: pointer;
								border: solid 4px #e8f0ff; width: <%=pctWidth%>%;" onclick="javascript:seleziona(<%=MappeLogic.IDX_CESSATO_CATASTO%>);">
									<span class="extWinTXTTitle">Cessato catasto</span>
								</td>
								<%totalWidth += pctWidth;%>
							<% }%>
							<% if (MappeLogic.isMappaVisibile(MappeLogic.IDX_DEMANIO, request)) { %>
								<td id="td<%=MappeLogic.IDX_DEMANIO%>" style="text-align: center; background-color: #f8f8f8; cursor: pointer;
								border: solid 4px #e8f0ff; width: <%=pctWidth%>%;" onclick="javascript:seleziona(<%=MappeLogic.IDX_DEMANIO%>);">
									<span class="extWinTXTTitle">Demanio</span>
								</td>
								<%totalWidth += pctWidth;%>
							<% }%>
							<td style="text-align: center; background-color: #FFFFFF; border-style: none; width: <%=100 - totalWidth%>%;">							
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
	
</html>

