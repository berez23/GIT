<%@include file="jspHead.jsp" %>
<div class="section">
<h2>MUI - ERRORE</h2>
<div class="section">
<p>
Si &egrave; verificato un errore nell'esecuzione della richiesta</p>
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td align="left">Errore</td>
			<td align="left">${t_code}</td>
			<td align="left">${t_msg}</td>
		</tr>
		<c:if test="${empty t_code}">
		<tr class="b">
			<td align="left" colspan="3">${t_stack} </td>
		</tr>
		</c:if>
	</tbody>
</table>
</div>
</div>
