<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@page import="it.escsolution.escwebgis.mappe.bean.Link"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.escsolution.escwebgis.mappe.logic.MappeLogic"%>
	
<html>

	<head>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
	</head>
	
	<% int rowIndex = Integer.parseInt(request.getParameter("index"));
	ArrayList links = (ArrayList)session.getAttribute(MappeLogic.LINK_LIST + rowIndex); 
	String mode = request.getParameter("mode"); %>
	
	<script>
		function visualizzaMappa(path) {
			if (path != "") {
				if ("<%=mode%>" == "56") {
					parent.document.getElementById("visualizza").cols = "150, *, 0, 0, 0";
				} else if ("<%=mode%>" == "CV") {
					parent.document.getElementById("visualizza").cols = "150, 0, *, 0, 0";
				} else if ("<%=mode%>" == "CC") {
					parent.document.getElementById("visualizza").cols = "150, 0, 0, *, 0";
				} else if ("<%=mode%>" == "DE") {					
					parent.document.getElementById("visualizza").cols = "150, 0, 0, 0, *";
				}
				parent.frames["img<%=mode%>"].location.href = "<%= request.getContextPath() %>/Mappe?mode=img&index=<%=rowIndex%>&path=" + path;
				if (navigator.userAgent.indexOf('MSIE 10.0') != -1) {
					var civetta = window.open();
					civetta.close();
				}
			}
		}
	</script>

	<body topmargin="0" leftmargin="0">
		<table cellpadding="0" cellspacing="0"
		style="width: 100%; height: 100%;">
			<tr>
				<% if (links == null || links.size() == 0) {%>
					<td style="text-align: center; vertical-align: middle; background-color: #e8f0ff;">
						<span class="extWinTXTTitle">Nessuna mappa da visualizzare</span>
					</td>
				<% } else { 
					String currGruppo = "";
					int contaGruppi = 0;%>
					<td style="text-align: left; padding: 10px; vertical-align: top; background-color: #e8f0ff;">
						<table cellpadding="0" cellspacing="0" style="width: 100%;">
							<tr>
								<td class="extWinTDData" style="text-align: center; border-style: none; padding-bottom: 15px;">
									<span class="extWinTXTTitle">Mappe disponibili</span>									
								</td>
							</tr>
							<% for (int i = 0; i < links.size(); i++) {
								Link link = (Link)links.get(i);
								String gruppo = link.getGruppo();
								if (gruppo != null && !gruppo.equals("") && !gruppo.equals("§") && !gruppo.equals(currGruppo)) {%>
									<tr>
										<td class="extWinTDData" 
										style="border-style: none; padding-top: <%=contaGruppi > 0 ? "10" : "0"%>px;"">
											<span class="extWinTXTTitle" style="font-style: italic;"><%=gruppo%></span>									
										</td>
									</tr>
								<% } %>
								<% if (!gruppo.toLowerCase().equals("abbozzi")) {%>
									<tr>
										<td class="extWinTDData" 
										style="border-style: none; padding-left: <%=gruppo != null && !gruppo.equals("") && !gruppo.equals("§") ? "20" : "0"%>px; padding-top: <%=gruppo != null && gruppo.equals("§") ? "10" : "0"%>px;">
											<span class="extWinTXTTitle" style="cursor: pointer;"
											onclick="visualizzaMappa('<%=link.getPath()%>');"><%=link.getDescrizione()%></span>									
										</td>
									</tr>
								<% } else if (!currGruppo.toLowerCase().equals("abbozzi")) { %>
									<tr>
										<td class="extWinTDData" 
										style="border-style: none; padding-left: 0px;">
											<select onchange="visualizzaMappa(this.value);" class="INPDBComboBox">
												<option value="" selected>seleziona</option>
												<%for (int j = 0; j < links.size(); j++) {
													Link linkAbbozzi = (Link)links.get(j);
													if (linkAbbozzi.getGruppo().toLowerCase().equals("abbozzi")) { %>
														<option value="<%=linkAbbozzi.getPath()%>"><%=linkAbbozzi.getDescrizione()%></option>
													<% }													
												}%>
									        </select>			
										</td>
									</tr>
								<% }%>
							<% 	currGruppo = gruppo;
								contaGruppi++;
							} %>
						</table>
					</td>
				<% }%>
			</tr>
		</table>
	</body>
	
</html>