<%@ page import="it.escsolution.escwebgis.common.EscServlet"%>

<table cellpadding="0" cellspacing="0" border="0" style="width: 100%;">
	<tr>
		<td style="text-align: center;">
			<span class="extWinTXTTitle">
				<%=request.getParameter("orderFilter.label")%>
			</span>
		</td>
		<td style="width: 30px; text-align: left;">
			<img alt="ordinamento crescente" title="ordinamento crescente" src="<%= request.getContextPath() %>/images/a_z_25.jpg"
			style="cursor: pointer;" onclick="order('<%=request.getParameter("orderFilter.orderField")%>', 'ASC', '<%=EscServlet.ST_ORDER_FILTER%>');">
		</td>
		<td style="width: 30px; text-align: left;">
			<img alt="ordinamento decrescente" title="ordinamento decrescente" src="<%= request.getContextPath() %>/images/z_a_25.jpg"
			style="cursor: pointer;" onclick="order('<%=request.getParameter("orderFilter.orderField")%>', 'DESC', '<%=EscServlet.ST_ORDER_FILTER%>');">
		</td>
	</tr>
</table>