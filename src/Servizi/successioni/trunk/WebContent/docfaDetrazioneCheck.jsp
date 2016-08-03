<%@include file="jspHead.jsp" %>
<div class="section">
<h2>DOCFA - CONTROLLO DETRAZIONE PRIMA CASA</h2>
<div class="section">

<c:if test="${SOGGETTO_CF_IN_ANAGRAFE}">
	<table class="bodyTable">
		<tbody>
			<tr class="a">
				<td class="a_title" align="center" colspan="1">${SOGGETTO.nome } ${SOGGETTO.denominazione } e' censito in anagrafe con CF: ${SOGGETTO.codfiscalePiva }</td>
			</tr> 
		</tbody>
	</table>
</c:if>
<c:if test="${!SOGGETTO_CF_IN_ANAGRAFE}">
	<table class="bodyTable">
		<tbody>
			<tr class="a">
				<td class="a_title" align="center" colspan="1">ATTENZIONE: ${SOGGETTO.nome } ${SOGGETTO.denominazione } NON e' censito in anagrafe con CF: ${SOGGETTO.codfiscalePiva }</td>
			</tr> 
		</tbody>
	</table>
</c:if>
<table class="bodyTable">
	<tbody>
		<c:forEach items="${RESIDENZE}" varStatus="status" var="residenza">
			<c:if test="${status.first}">
				<tr class="a">
					<td class="a_title" align="center" colspan="5">Residenze di ${SOGGETTO.nome } ${SOGGETTO.denominazione }</td>
				</tr> 
				<tr class="a">
					<td class="a" align="center" colspan="1">Dal</td>
					<td class="a" align="center" colspan="1">Al</td>
					<td class="a" align="center" colspan="1">Codice Via</td>
					<td class="a" align="center" colspan="1">Via</td>
					<td class="a" align="center" colspan="1">Civico</td>
				</tr>
			</c:if>
			<tr class="b">
				<td align="center" colspan="1">${residenza.dataDa}</td>
				<td align="center" colspan="1">${residenza.dataA}</td>
				<td align="center" colspan="1">${residenza.codiceVia}</td>
				<td align="center" colspan="1">${residenza.via}</td>
				<td align="center" colspan="1">${residenza.numeroCivico}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<table class="bodyTable">
	<tbody>
		<c:forEach items="${OGGETTI}" varStatus="status" var="oggetto">
			<c:if test="${status.first}">
				<tr class="a">
					<td class="a_title" align="center" colspan="4">Immobili Abitativi del docfa ${SOGGETTO.iidProtocolloReg }-${SOGGETTO.iidFornitura }</td>
				</tr> 
				<tr class="a">
					<td class="a" align="center" colspan="1">Foglio</td>
					<td class="a" align="center" colspan="1">Particella</td>
					<td class="a" align="center" colspan="1">Subalterno</td>
					<td class="a" align="center" colspan="1">Indirizzi associati</td>
				</tr>
			</c:if>
			<tr class="b">
				<td align="center" valign="top" colspan="1">${oggetto.foglio}</td>
				<td align="center" valign="top" colspan="1">${oggetto.particella}</td>
				<td align="center" valign="top" colspan="1">${oggetto.subalterno}</td>
				<td align="center" valign="top" colspan="1">
					<c:forEach items="${oggetto.codiceViaCivicos}" varStatus="codviastatus" var="codiceVia">
						<c:if test="${codviastatus.first}">
							<table>
								<tr class="a">
									<td class="a" align="center" colspan="1">Codice Via</td>
									<td class="a" align="center" colspan="1">Via</td>
									<td class="a" align="center" colspan="1">Civico</td>
								</tr>
						</c:if>
								<tr class="b">
									<td align="center" colspan="1">${codiceVia.codiceVia}</td>
									<td align="center" colspan="1">${codiceVia.via}</td>
									<td align="center" colspan="1">${codiceVia.civico}</td>									
								</tr>
						<c:if test="${codviastatus.last}">
							</table>
						</c:if>
					</c:forEach>
				</td>					
			</tr>
		</c:forEach>
	</tbody>
</table>
<c:if test="${!empty OGGETTO_CON_DETRAZIONE}">
	<c:set var="oggetto" value="${OGGETTO_CON_DETRAZIONE }" ></c:set>
	<c:set var="residenza" value="${RESIDENZA_CON_DETRAZIONE }" ></c:set>
	<table class="bodyTable">
		<tbody>
			<tr class="a">
				<td class="a_title" align="center" colspan="4">${SOGGETTO.nome } ${SOGGETTO.denominazione } HA Diritto alla detrazione per il seguente immobile della nota ${SOGGETTO.iidProtocolloReg }-${SOGGETTO.iidFornitura  } :</td>
			</tr> 
			<tr class="a">
				<td class="a" align="center" colspan="1">Foglio</td>
				<td class="a" align="center" colspan="1">Particella</td>
				<td class="a" align="center" colspan="1">Subalterno</td>
				<td class="a" align="center" colspan="1">Residenza associata</td>
			</tr>
			<tr class="b">
				<td align="center" valign="top" colspan="1">${oggetto.foglio}</td>
				<td align="center" valign="top" colspan="1">${oggetto.particella}</td>
				<td align="center" valign="top" colspan="1">${oggetto.subalterno}</td>
				<td align="center" valign="top" colspan="1">
					<table>
						<tr class="a">
							<td class="a" align="center" colspan="1">Dal</td>
							<td class="a" align="center" colspan="1">Al</td>
							<td class="a" align="center" colspan="1">Codice Via</td>
							<td class="a" align="center" colspan="1">Via</td>
							<td class="a" align="center" colspan="1">Civico</td>
						</tr>
						<tr class="b">
							<td align="center" colspan="1">${residenza.dataDa}</td>
							<td align="center" colspan="1">${residenza.dataA}</td>
							<td align="center" colspan="1">${residenza.codiceVia}</td>
							<td align="center" colspan="1">${residenza.via}</td>
							<td align="center" colspan="1">${residenza.numeroCivico}</td>
						</tr>
					</table>
				</td>					
			</tr>
			<c:if test="${RESIDENZA_OLTRE_90GG}">
				<tr class="a">
					<td class="a_title" align="center" colspan="4">DOCFA: ${SOGGETTO.nome } ${SOGGETTO.denominazione } HA preso la residenza nell'immobile indicato per la detrazione oltre 90gg dopo il rogito</td>
				</tr> 
			</c:if>
		</tbody>
	</table>
</c:if>
<c:if test="${empty OGGETTO_CON_DETRAZIONE}">
	<table class="bodyTable">
		<tbody>
			<tr class="a">
				<td class="a_title" align="center" colspan="1">${SOGGETTO.nome } ${SOGGETTO.denominazione } NON HA Diritto alla detrazione per nessun immobile della nota ${SOGGETTO.iidProtocolloReg }/${SOGGETTO.iidFornitura  }</td>
			</tr> 
		</tbody>
	</table>
</c:if>
</div>
</div>
