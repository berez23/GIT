<%@include file="jspHead.jsp" %>
<div class="section">
<c:choose>
  	<c:when test="${codiceErrore.classe == 1}"><c:set var="NOME_ENTITA" value="REGOLA"></c:set> </c:when>
  	<c:when test="${codiceErrore.classe == 2}"><c:set var="NOME_ENTITA" value="CODICE ESITO"></c:set> </c:when>
</c:choose>
<h2>SUCCESSIONI - DETTAGLIO ${NOME_ENTITA} <b>  	
	<c:choose>
	  	<c:when test="${codiceErrore.flagIsVariante}">${codiceErrore.codiceBase}</b> variante <b>${codiceErrore.variante}</c:when>
	  	<c:otherwise>${codiceErrore.codiceRegolaInfranta} </c:otherwise>
  	</c:choose>
</b> </h2>
<div class="section">
<!-- broswer is ${BROWSER } -->
<form action="" method="post">
<input type="hidden" value="${codiceErrore.codiceRegolaInfranta }" name="codiceRegolaInfranta" />
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td class="a_title" align="center" colspan="4">REGOLA</td>
		</tr>
		<tr class="a">
			<td align="right">&nbsp;</td>
			<td align="center" colspan="1">
				<table>
					<tr>
						<td class="a">Codice </td>
						<td><c:choose><c:when test="${codiceErrore.flagIsVariante}">${codiceErrore.codiceBase} var.${codiceErrore.variante}</c:when><c:otherwise>${codiceErrore.codiceRegolaInfranta} </c:otherwise></c:choose></td>
					</tr>
					<tr>
						<td class="a">Descrizione</td>
						<td><textarea name="descrizione" rows="5" cols="45" wrap="PHYSICAL">${codiceErrore.descrizione }</textarea></td>
					</tr>
					<tr>
					<c:choose>
					  	<c:when test="${codiceErrore.classe == 1}">
							<td class="a">Bloccante</td>
							<c:if test="${codiceErrore.flagBloccante}" ><c:set var="flagBloccante" > checked="checked" </c:set></c:if>
							<td><input type="checkbox" ${flagBloccante } name="flagBloccante" value="true"></td><c:set var="flagBloccante"/>
					  	</c:when>
					  	<c:when test="${codiceErrore.classe == 2}">
							<td class="a">Effetto in catasto</td>
							<td><textarea name="effetto" rows="5" cols="45" wrap="PHYSICAL">${codiceErrore.effetto }</textarea></td>
					  	</c:when>
					</c:choose>
					</tr>
				</table>
			</td>	
			<c:choose>
			  	<c:when test="${codiceErrore.classe == 1}">
					<td align="left">&nbsp;<font size="-2">(le stringhe ":colonna" e ":tabella"<br/> se inserite nel messaggio verranno sostituite <br/>rispettivamente col il nome della colonna<br/> e il nome della tabella)</font></td>
					<td align="left">&nbsp;</td>
			  	</c:when>
			  	<c:when test="${codiceErrore.classe == 2}">
					<td align="left">&nbsp;</td>
					<td align="left">&nbsp;</td>
			  	</c:when>
			</c:choose>
		</tr>
		<tr class="b">
			<td align="right">&nbsp;</td>
			<td align="center" colspan="1">
				<table>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td align="left"><input type="submit" class="muiinput" value="Salva" name="Salva"/></td>
					</tr>
				</table>
			</td>	
			<td align="left">&nbsp;</td>
			<td align="left">&nbsp;</td>
		</tr>
	</tbody>
</table>
</form>
</div>
</div>
