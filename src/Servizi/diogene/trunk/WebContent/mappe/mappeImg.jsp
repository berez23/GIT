<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@page import="java.util.ArrayList"%>
<%@page import="it.escsolution.escwebgis.mappe.logic.MappeLogic"%>

<html>

	<head>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
	</head>
	
	<% int rowIndex = Integer.parseInt(request.getParameter("index"));
	ArrayList links = (ArrayList)session.getAttribute(MappeLogic.LINK_LIST + rowIndex); %>
	
	<% if (request.getParameter("path") != null && !request.getParameter("path").equals("")) {%>
		<frameset border="0" frameborder="no">
			<frame src="mappe/images.jsp?path=<%=request.getParameter("path")%>">
		</frameset>
	<% } else { %>
		<body topmargin="0" leftmargin="0">
			<table cellpadding="0" cellspacing="0" style="width: 100%; height: 100%;">
				<tr>
					<td style="text-align: center; vertical-align: middle; background-color: #e8f0ff;">					
						<%if (links == null || links.size() == 0) {%>					
							<span class="extWinTXTTitle">Nessuna mappa da visualizzare</span>						
						<% } else {%>
							<span class="extWinTXTTitle">Selezionare il documento da visualizzare dall'elenco a sinistra</span>
						<% } %>			
					</td>
				</tr>			
			</table>
		</body>
	<% }%>
	
</html>