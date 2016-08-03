<%@include file="jspHead.jsp" %>
<div class="section">
<h2>DOCFA - DETTAGLIO DOCFA <b>${docfa.protocollo}-${ docfa.fornitura}</b> </h2>
<div class="section">
<c:if test="${BROWSER == 'INTERNET EXPLORER' }">
<style type="text/css">
table.bodyTable
{
position:absolute;
left:190px;
top:190px
}
</style>
<c:set scope="request" var="HIDEFOOTER" value="Y"></c:set>
</c:if>
<!-- broswer is ${BROWSER } -->
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td class="a_title" align="center" colspan="20">DATI GENERALI</td>
		</tr> 
		<tr class="a">
			<td align="center" colspan="20">
				<table>
					<tr>
						<td class="a">Data Registrazione</td>
						<td>${docfa.dataRegistrazione }</td>
						<td class="a">Data variazione</td>
						<td>${docfa.dataVariazione }</td>
						<td class="a">Causale</td>
						<td colspan="2">${docfa.causale } </td>
					</tr>
					<tr>	
						<td class="a">Sopressione</td>
						<td>${docfa.soppressione }</td>
						<td class="a">Variazione</td>
						<td>${docfa.variazione } </td>
						<td class="a">Nuove</td>
						<td>${docfa.costituzione } </td>
						<td class="a">Deriv spe.</td>
						<td>${docfa.derivSpe } </td>
					</tr>
				</table>
			</td>	
		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="20">RIFERIMENTI TEMPORALI FABBRICATO</td>
		</tr> 
		<tr class="a">
			<td class="a_title" align="center" colspan="10">Anno Costruzione</td>
			<td class="a_title" align="center" colspan="10">Anno Ristrutturazione Totale</td>
		</tr> 
		<c:forEach var="riferimenti"  items="${listaDocfaParteUno}">
			<tr class="b">
				<c:if test="${riferimenti.annoCostruzione == '1900' }" >
					<td colspan="10">antec. 1942</td>
				</c:if>	
				<c:if test="${riferimenti.annoCostruzione != '1900' }" >
					<td colspan="10">${riferimenti.annoCostruzione }</td>
				</c:if>
				<c:if test="${riferimenti.annoRistrutturazione == '0' }" >
					<td colspan="10"></td>
				</c:if>	
				<c:if test="${riferimenti.annoRistrutturazione != '0' }" >
					<td colspan="10">${riferimenti.annoRistrutturazione }</td>
				</c:if>
			</tr>
		</c:forEach>
		<tr class="a">
			<td class="a_title" align="center" colspan="20">LISTA INTESTATI</td>
		</tr> 
		<c:forEach var="intestati"  varStatus="status" items="${listaDocfaIntestati}">
			<c:if test="${status.first}">
				<tr class="a">
					<td class="a" colspan="10">Denominazione</td>
					<td class="a" colspan="5">Codice Fiscale</td>		
					<td class="a" colspan="5">Partita Iva</td>
				</tr>
			</c:if>	
			<tr class="b">
				<td colspan="10">${intestati.denominazione}</td>
				<td colspan="5">${intestati.codiceFiscale}</td>
				<td colspan="5">${intestati.partitaIva}</td>
			</tr>
 		</c:forEach>
 		
 		<tr class="a">
			<td class="a_title" align="center" colspan="20">LISTA DICHIARANTI</td>
		</tr> 
		<c:forEach var="dichiaranti"  varStatus="status" items="${listaDocfaDichiaranti}">
			<c:if test="${status.first}">
				<tr class="a">
					<td class="a" colspan="6">Cognome</td>
					<td class="a" colspan="4">Nome</td>		
					<td class="a" colspan="7">Indirizzo</td>
					<td class="a" colspan="3">Luogo</td>
				</tr>
			</c:if>	
			<tr class="b">
				<td colspan="6">${dichiaranti.cognome}</td>
				<td colspan="4">${dichiaranti.nome}</td>
				<td colspan="7">${dichiaranti.indirizzoDichiarante}</td>
				<td colspan="3">${dichiaranti.luogo}</td>
			</tr>
 		</c:forEach>
 		
 		<tr class="a">
			<td class="a_title" align="center" colspan="20">LISTA UIU</td>
		</tr> 
		<tr class="a">
			<td align="center" colspan="20">
			<table>
				<c:forEach var="uiu"  varStatus="status" items="${listaDocfaUiu}">
					<c:if test="${status.first}">
						<tr class="a">
							<td class="a">Graffati</td>
							<td class="a">Tipo</td>		
							<td class="a">Foglio</td>
							<td class="a">Particella</td>
							<td class="a">Subalterno</td>
							<td class="a">Indirizzo</td>
						</tr>
					</c:if>	
					<tr class="b">
						<td>${uiu.presenzaGraffati}</td>
						<td>${uiu.tipo}</td>
						<td>${uiu.foglio}</td>
						<td>${uiu.particella}</td>
						<td>${uiu.subalterno}</td>
						<td>
						  <c:forEach var="indirizzo"  varStatus="status" items="${uiu.indPart}">
  								${indirizzo }<br/>
  							</c:forEach>
						</td>
					</tr>
		 		</c:forEach>
 			</table>
 			</td>
 		</tr>
 		<tr class="a">
			<td class="a_title" align="center" colspan="20">LISTA INTESTATARI UIU</td>
		</tr> 
		<tr class="a">
			<td align="center" colspan="20">
			<table>
				<c:forEach var="uiuT"  varStatus="status" items="${listaDocfaUiu}">
					<c:if test="${status.first}">
						<tr class="a">
							<td class="a">FPS</td>
							<td class="a">ICI</td>
							<td class="a" >Denominazione</td>	
							<td class="a">C.F./P.I.</td>
							<td class="a">Data Nascita</td>	
							<td class="a">Comune Nascita/Sede</td>	
							<td class="a">Residenza/SedeLegale</td>
							<td class="a">% Possesso</td>
						</tr>
					</c:if>	
					
						<c:forEach var="tito"  varStatus="statusT" items="${uiuT.elencoTitolari}">
							<tr class="b">
							<td>
								<c:if test="${statusT.first}">
								${uiuT.foglio}/${uiuT.particella}/${uiuT.subalterno}
								</c:if>
							</td>
							<td>
								<c:if test="${tito.flagPersona != 'G' }">
									<input type="button" class="muiinput" value="Controlla Detrazione" name="Controlla Detrazione" 
										onclick="popUp('docfaDetrazioneCheckNoTemplate.jsp?CF=<c:if test="${tito.codiceFiscale != null && tito.codiceFiscale != '-'}">${tito.codiceFiscale}</c:if><c:if test="${tito.codiceFiscale == null || tito.codiceFiscale == '-'}">${tito.partitaIva}</c:if>&DR=${docfa.dataRegistrazione}&PRT=${docfa.protocollo}&PRT=${docfa.protocollo}&FORN=${docfa.fornitura}');return false"/></td>
								</c:if>
							</td>
							<td>${tito.denominazione} ${tito.nome}</td>
							<td>
								<c:if test="${tito.codiceFiscale != null && tito.codiceFiscale != '-'}">
								${tito.codiceFiscale}
								</c:if>
								<c:if test="${tito.codiceFiscale == null || tito.codiceFiscale == '-'}">
								${tito.partitaIva}
								</c:if>
							</td>
							<td>
								<c:if test="${tito.dataNascita != null }">
								${tito.dataNascita}
								</c:if>
							</td>
							<td><c:if test="${tito.descrComuneNascita != null && tito.descrComuneNascita != '-'}">${tito.descrComuneNascita}</c:if> <c:if test="${tito.provinciaNascita != null && tito.provinciaNascita != ''}">( ${tito.provinciaNascita} )</c:if></td>
							<td>${tito.indirizzoResidenza}-${tito.capResidenza} ${tito.comuneResidenza} <c:if test="${tito.provinciaResidenza != null && tito.provinciaResidenza != ''}">( ${tito.provinciaResidenza} )</c:if> </td>
							<td>${tito.percentualePossesso}</td>
						</tr>	
						</c:forEach>
						
						
					
				</c:forEach>
 			</table>
 			</td>
 		</tr>
 		
 		<tr class="a">
			<td class="a_title" align="center" colspan="20">BENI COMUNI NON CENSIBILI</td>
		</tr> 
		<tr class="a">
			<td align="center" colspan="20">
			<table>
				<c:forEach var="nonCens"  varStatus="status" items="${listaDocfabeniNonCens}">
					<c:if test="${status.first}">
						<tr class="a">
							<td class="a">Foglio</td>
							<td class="a">Particella</td>		
							<td class="a">Sub.</td>
							<td class="a">Foglio 2</td>
							<td class="a">Particella 2</td>
							<td class="a">Sub. 2</td>
							<td class="a">Foglio 3</td>
							<td class="a">Particella 3</td>
							<td class="a">Sub. 3</td>
						</tr>
					</c:if>	
					<tr class="b">
						<td>${nonCens.foglio}</td>
						<td>${nonCens.particella}</td>
						<td>${nonCens.subalterno}</td>
						<td>${nonCens.foglio2}</td>
						<td>${nonCens.particella2}</td>
						<td>${nonCens.subalterno2}</td>
						<td>${nonCens.foglio3}</td>
						<td>${nonCens.particella3}</td>
						<td>${nonCens.subalterno3}</td>
					</tr>
		 		</c:forEach>
			</table>
 			</td>
 		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="20">ANNOTAZIONI</td>
		</tr> 
		<c:forEach var="annot"  items="${listaDocfaAnnotazioni}">
			<tr class="b">
				<td colspan="20">${annot.annotazioni}</td>
			</tr>
 		</c:forEach>
 		
 		<tr class="a">
			<td class="a_title" align="center" colspan="20">DATI CENSUARI</td>
		</tr> 
		<tr class="a">
			<td align="center" colspan="20">
			<table>
				<c:forEach var="censuari"  varStatus="status" items="${listaDocfaDatiCensuari}">
					<c:if test="${status.first}">
						<tr class="a">
							<td class="a">Graffati</td>
							<td class="a">Foglio</td>		
							<td class="a">Particella</td>
							<td class="a">Sub.</td>
							<td class="a">Classe</td>
							<td class="a">Zona</td>
							<td class="a">Categoria</td>
							<td class="a">Consistenza</td>
							<td class="a">Superficie</td>
							<td class="a">Rendita</td>
							<td class="a">Ident. Imm.</td>
						</tr>
					</c:if>	
					<tr class="b">
						<td>${censuari.presenzaGraffati}</td>
						<td>${censuari.foglio}</td>
						<td>${censuari.particella}</td>
						<td>${censuari.subalterno}</td>
						<td>${censuari.classe}</td>
						<td>${censuari.zona}</td>
						<td>${censuari.categoria}</td>
						<td>${censuari.consistenza}</td>
						<td>${censuari.superfice}</td>
						<td>${censuari.rendita}</td>
						<td>${censuari.identificativoImm}</td>
					</tr>
		 		</c:forEach>
		 	</table>
 			</td>
 		</tr>	
		<tr class="a">
			<td class="a_title" align="center" colspan="20">LISTA PARTE UNO</td>
		</tr> 
		<tr class="a">
			<td align="center" colspan="20">
			<table>
				<c:forEach var="uno"  varStatus="status" items="${listaDocfaParteUno}">
					<c:if test="${status.first}">
						<tr class="a">
							<td class="a">Foglio</td>
							<td class="a">Particelle</td>		
							<td class="a">Anno Cost.</td>
							<td class="a">Anno Rist.</td>
							<td class="a">Posiz. Fab.</td>
							<td class="a">Tipo Acces.</td>
						</tr>
					</c:if>	
					<tr class="b">
						<td>${uno.foglio}</td>
						<td>${uno.numero}</td>
						<td>${uno.annoCostruzione}</td>
						<td>${uno.annoRistrutturazione}</td>
						<td>${uno.posizFabbr}</td>
						<td>${uno.tipoAccesso}</td>
					</tr>
		 		</c:forEach>
		 	</table>
 			</td>
 		</tr>
	</tbody>
</table>
</div>
</div>
<c:set var="PRINT_URL" value="docfaPdfNoTemplate?protocollo=${docfa.protocollo}&idFornitura=${docfa.fornitura}" scope="request"/>
<security:lock role="succ-adm,succ-supusr">
<c:if test="${docfa.importErrorCount > 0}">			
	<c:set var="FIRST_POPUP_URL" value="formPostNoTemplate/docfaImportLogListNoTemplate.jsp?protocollo=${docfa.protocollo}&idFornitura=${docfa.fornitura}" scope="request"/>
	<c:set var="FIRST_POPUP_DESC" value="Lista log  (${docfa.importErrorCount} importazione / ${docfa.integrazioneCount} integrazione ) " scope="request"/>
</c:if>
</security:lock>


