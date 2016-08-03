<%@include file="jspHead.jsp" %>
<div class="section">
<h2>NOTA <b>${nota.numeroNotaTras }/${ nota.annoNota}</b> valida dal <jsp:setProperty name="FieldConverter" property="data"    value = "${nota.dataValiditaAtto}" />${FieldConverter.data}</h2>
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
			<td class="a_title" align="center" colspan="20">NOTA</td>
		</tr> 
		<tr class="a">
			<td align="center" colspan="20">
				<table>
					<tr>
						<!-- <td class="a">Sede Rog</td>
						<td><nota:logged nota="${nota}" propertyName="sedeRog">${BELFIORE[nota.sedeRog]}</nota:logged> </td> -->
						<td class="a">Anno</td>
						<td><nota:logged nota="${nota}" propertyName="annoNota">${nota.annoNota}</nota:logged> </td>
						<!-- <td class="a">N. repertorio</td>
						<td><nota:logged nota="${nota}" propertyName="numeroRepertorio">${nota.numeroRepertorio}</nota:logged> </td> -->
						<td class="a">Data Validita Atto</td>
						<td><nota:logged nota="${nota}" propertyName="dataValiditaAtto"><jsp:setProperty name="FieldConverter" property="data"    value = "${nota.dataValiditaAtto}" />${FieldConverter.data} </nota:logged> </td>
						<!-- <td class="a">Esito</td>
						<td><nota:logged nota="${nota}" propertyName="esitoNota">${nota.esitoNota}</nota:logged> </td>
						<td class="a">Esito non registrato</td>
						<td><nota:logged nota="${nota}" propertyName="esitoNotaNonReg">${nota.esitoNotaNonReg}</nota:logged> </td>
						<td class="a">Data reg. Attivit&agrave;</td>
						<td><nota:logged nota="${nota}" propertyName="dataRegInAtti"><jsp:setProperty name="FieldConverter" property="data"    value = "${nota.dataRegInAtti}" />${FieldConverter.data}</nota:logged> </td>
						<td class="a">Rettifica</td>
						<td><nota:logged nota="${nota}" propertyName="flagRettifica">${nota.flagRettifica}</nota:logged> </td> -->
					</tr>
				</table>
			</td>	
		</tr>
		<c:set scope="page" var="detrazioneApplicabile" value="false"/>
		<c:forEach var="immobile"  varStatus="status" items="${fabbricatiNota}">
			<c:if test="${immobile.abitativo}">
				<c:set scope="page" var="detrazioneApplicabile" value="true"/>
			</c:if>
			<c:if test="${status.first}">
				<tr class="a">
					<td class="a_title" align="center" colspan="20">IMMOBILI</td>
				</tr>
				<tr class="a">
					<td class="a">Sel</td>
					<td class="a">Comune</td>
					<td class="a">Sez.</td>		
					<td class="a">Fgl.</td>		
					<td class="a">Part.</td>
					<td class="a">Sub.</td>
					<td class="a">ID Catastale</td>
					<td class="a">Graffato</td>
					<td class="a">Cat.</td>
					<td class="a">M.quadri</td>
					<td class="a">N.vani</td>
					<td class="a">M.cubi</td>
					<td class="a">Indirizzo Tras.</td>
					<td class="a">Indirizzo Catastale</td>
					<td class="a">Sc.</td>
					<td class="a">Int.</td>
					<td class="a">Piano</td>
					<td class="a">Edif.</td>
					<td class="a">Lotto</td>
					<td class="a">Rendita</td>
				</tr>
			</c:if>	
			<tr class="b"><!--${immobile.iid}  -->
				<td>&nbsp;</td>
				<td><%=it.webred.mui.http.MuiApplication.descComune%> </td>
				<c:forEach var="identifica" items="${immobile.miDupFabbricatiIdentificas}">
					<td><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="sezioneUrbana">${identifica.sezioneUrbana}</nota:logged> </td>
					<td><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="foglio">${identifica.foglio}</nota:logged> </td>
					<td><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="numero">${identifica.numero }</nota:logged> </td>
					<td><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="subalterno">${identifica.subalterno }</nota:logged> </td>
				</c:forEach>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="idCatastaleImmobile">${immobile.idCatastaleImmobile}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="flagGraffato"><c:choose><c:when test="${immobile.flagGraffato >0 }">SI</c:when><c:otherwise>NO</c:otherwise></c:choose></nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="categoria">${immobile.categoria}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="mq">${immobile.mq}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="vani">${immobile.vani}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="mc">${immobile.mc}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="TIndirizzo">${immobile.TIndirizzo}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="CIndirizzo">${immobile.CIndirizzo}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="TScala">${immobile.TScala}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="TInterno1">${immobile.TInterno1}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="TPiano1">${immobile.TPiano1}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="TEdificio">${immobile.TEdificio}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="TLotto">${immobile.TLotto}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupFabbricatiInfo="${immobile}" propertyName="renditaEuro">${immobile.renditaEuro}</nota:logged> </td>
			<c:if test="${!empty immobile.codiceErroreImport}">
				</tr>
				<tr onclick="popUp('formPostNoTemplate/importLogListNoTemplate.jsp?codiceEsito=true&iidImmobile=${immobile.iid}')" class="b">
					<td class="a"><c:choose><c:when test="${R0602.flagBloccante}"><img src="img/bloccante.gif"></c:when><c:otherwise></c:otherwise> </c:choose>Codice Esito</td>
					<td> ${immobile.codiceErroreImport.codiceRegolaInfranta}</td>
					<td class="a" colspan="2">Descrizione Esito</td>
					<td colspan="7">${immobile.codiceErroreImport.descrizione}</td>
					<td class="a" colspan="2">Effetto In Catasto</td>
					<td colspan="7">${immobile.codiceErroreImport.effetto}</td>
			</c:if>
			</tr>
 		</c:forEach>
		<c:forEach var="terreno"  varStatus="status" items="${terreniNota}">
			<c:if test="${status.first}">
				<tr class="a">
					<td class="a_title" align="center" colspan="20">TERRENI</td>
				</tr>
				<tr class="a">
					<td class="a">Sel</td>
					<td class="a" colspan="2">Comune</td>
					<td class="a">Fgl.</td>		
					<td class="a">Part.</td>
					<td class="a">Sub.</td>
					<td class="a">ID Catastale</td>
					<td class="a">Graffato</td>
					<td class="a">Edif.</td>
					<td class="a">Ettari</td>
					<td class="a">Are</td>
					<td class="a" colspan="9">Centiare</td>
				</tr>
			</c:if>	
			<tr class="b"><!--${terreno.iid}  -->
				<td>&nbsp;</td>
				<td colspan="2"><%=it.webred.mui.http.MuiApplication.descComune%> </td>
				<td>${terreno.foglio}</td>
				<td>${terreno.numero }</td>
				<td>${terreno.subalterno }</td>
				<td>${terreno.idCatastaleImmobile}</td>
				<td>NO</td>
				<td>${terreno.edificabilita}</td>
				<td>${terreno.ettari}</td>
				<td>${terreno.are}</td>
				<td colspan="9">${terreno.centiare}</td>
			</tr>
 		</c:forEach>
		<c:forEach var="titolarita"  varStatus="status" items="${titolaritaFavoreNota}">
			<c:if test="${status.first}">
				<tr class="a">
					<td class="a_title" align="center" colspan="20">EREDI</td>
				</tr>
				<tr class="a">
					<td class="a">Com. ICI</td>
					<td class="a">Codice Fiscale</td>
					<td class="a">Tipo sogg.</td>		
					<td class="a">Denominazione</td>		
					<td class="a">Sede</td>
					<td class="a">Cognome</td>
					<td class="a">Nome</td>
					<td class="a">Sesso</td>
					<td class="a">Data Nascita</td>
					<td class="a">Luogo Nasc.</td>
					<td class="a" colspan="4">Indirizzo</td>		
					<td class="a" colspan="4">Dati Immobile</td>
					<td class="a">Diritto</td>
					<td class="a">Quota poss.</td>		
				</tr>
			</c:if>	
			<tr class="b"><!--${titolarita.iid}/${titolarita.miDupSoggetti.iid}  -->
				<c:set var="key" scope="request" value="${titolarita.miDupSoggetti.iid}"/>
		   		<td><c:if test="${displayedIciLink[key] != '1'}"><c:if test="${HIDE_ICI_LINK != 1}"><a href="iciPdfNoTemplate?iidTitolarita=${titolarita.iid}&amp;iidSoggetto=${titolarita.miDupSoggetti.iid}">ICI</a></c:if><c:if test="${titolarita.miDupSoggetti.tipoSoggetto  != 'G' && detrazioneApplicabile }"><br/><input type="button" class="muiinput" value="Controlla Detrazione" name="Controlla Detrazione" onclick="popUp('detrazioneCheckNoTemplate.jsp?iidSoggetto=${titolarita.miDupSoggetti.iid}');return false"/></c:if></c:if></td>
				<% ((java.util.Map)request.getAttribute("displayedIciLink")).put(request.getAttribute("key"),"1"); %>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="codiceFiscale">${titolarita.miDupSoggetti.codiceFiscale}</nota:logged><nota:logged nota="${nota }" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="codiceFiscaleG"> ${titolarita.miDupSoggetti.codiceFiscaleG}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="tipoSoggetto">${titolarita.miDupSoggetti.tipoSoggetto}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="denominazione">${titolarita.miDupSoggetti.denominazione}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="sede">${BELFIORE[titolarita.miDupSoggetti.sede]}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="cognome">${titolarita.miDupSoggetti.cognome}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="nome">${titolarita.miDupSoggetti.nome}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="sesso"><c:choose><c:when test="${titolarita.miDupSoggetti.sesso == 1}">M</c:when><c:when test="${titolarita.miDupSoggetti.sesso == 2}">F</c:when> </c:choose></nota:logged></td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="dataNascita"><jsp:setProperty name="FieldConverter" property="data"    value = "${titolarita.miDupSoggetti.dataNascita}" />${FieldConverter.data}</nota:logged> </td>
 				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="luogoNascita">${BELFIORE[titolarita.miDupSoggetti.luogoNascita]}</nota:logged> </td>
				<c:if test="${!empty titolarita.miDupSoggetti.miDupIndirizziSogs}">			
					<c:forEach var="indirizzo" items="${titolarita.miDupSoggetti.miDupIndirizziSogs}">
						<td colspan="4"><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="indirizzo">${indirizzo.indirizzo}</nota:logged><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="comune"> ${indirizzo.comune}</nota:logged><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="provincia"> ${indirizzo.provincia}</nota:logged> ${indirizzo.cap} </td>
					</c:forEach>
				</c:if>	
				<c:if test="${empty titolarita.miDupSoggetti.miDupIndirizziSogs}">			
					<td colspan="4">&nbsp;</td>
				</c:if>	
				<c:forEach var="identifica" items="${titolarita.miDupFabbricatiInfo.miDupFabbricatiIdentificas}">
					<td colspan="4">
						<b>Fgl. </b><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="foglio">${identifica.foglio}</nota:logged>
						<b>Part. </b><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="numero">${identifica.numero }</nota:logged>
						<b>Sub. </b><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="subalterno">${identifica.subalterno }</nota:logged>
					</td>
				</c:forEach>
				<c:if test="${!empty titolarita.miDupTerreniInfo}">			
					<td colspan="4">
						<b>Fgl. </b><nota:logged nota="${nota}" miDupTerreniInfo="${titolarita.miDupTerreniInfo}" propertyName="foglio">${titolarita.miDupTerreniInfo.foglio}</nota:logged>
						<b>Part. </b><nota:logged nota="${nota}" miDupTerreniInfo="${titolarita.miDupTerreniInfo}" propertyName="numero">${titolarita.miDupTerreniInfo.numero }</nota:logged>
						<b>Sub. </b><nota:logged nota="${nota}" miDupTerreniInfo="${titolarita.miDupTerreniInfo}" propertyName="subalterno">${titolarita.miDupTerreniInfo.subalterno }</nota:logged>
					</td>
				</c:if>	
				<td>${titolarita.sfCodiceDiritto}</td>
				<td><nota:logged nota="${nota}" miDupTitolarita="${titolarita}" propertyName="scQuotaDenominatore"/><nota:logged nota="${nota}" miDupTitolarita="${titolarita}" propertyName="scQuotaNumeratore"/><nota:logged nota="${nota}" miDupTitolarita="${titolarita}" propertyName="sfQuotaDenominatore"/><nota:logged nota="${nota}" miDupTitolarita="${titolarita}" propertyName="sfQuotaNumeratore"/> <fmt:formatNumber value="${titolarita.quota*100}" type="NUMBER"  minFractionDigits="2" maxFractionDigits="2"  />%</td>
			</tr>
 		</c:forEach>
		<c:forEach var="titolarita"  varStatus="status" items="${titolaritaControNota}">
			<c:if test="${status.first}">
				<tr class="a">
					<td class="a_title" align="center" colspan="20">DECEDUTO</td>
				</tr>
				<tr class="a">
					<td class="a">Sel.</td>
					<td class="a">Codice Fiscale</td>
					<td class="a">Tipo sogg.</td>		
					<td class="a">Denominazione</td>		
					<td class="a">Sede</td>
					<td class="a">Cognome</td>
					<td class="a">Nome</td>
					<td class="a">Sesso</td>
					<td class="a">Data Nascita</td>
					<td class="a">Luogo Nasc.</td>
					<td class="a" colspan="4">Indirizzo</td>		
					<td class="a" colspan="4">Dati Immobile</td>
					<td class="a">Diritto</td>
					<td class="a">Quota poss.</td>		
				</tr>
			</c:if>	
			<tr class="b"><!--${titolarita.iid}/${titolarita.miDupSoggetti.iid}  -->
				<c:set var="key" scope="request" value="${titolarita.miDupSoggetti.iid}"/>
		   		<td><c:if test="${displayedIciLink[key] != '1'}"><c:if test="${HIDE_ICI_LINK != 1}"><a href="iciPdfNoTemplate?iidTitolarita=${titolarita.iid}&amp;iidSoggetto=${titolarita.miDupSoggetti.iid}">ICI</a></c:if><c:if test="${titolarita.miDupSoggetti.tipoSoggetto != 'G' && detrazioneApplicabile }"><br/><input type="button" class="muiinput" value="Controlla Detrazione" name="Controlla Detrazione" onclick="popUp('detrazioneCheckNoTemplate.jsp?iidSoggetto=${titolarita.miDupSoggetti.iid}');return false"/></c:if></c:if></td>
				<% ((java.util.Map)request.getAttribute("displayedIciLink")).put(request.getAttribute("key"),"1"); %>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="codiceFiscale">${titolarita.miDupSoggetti.codiceFiscale}</nota:logged><nota:logged nota="${nota }" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="codiceFiscaleG"> ${titolarita.miDupSoggetti.codiceFiscaleG}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="tipoSoggetto">${titolarita.miDupSoggetti.tipoSoggetto}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="denominazione">${titolarita.miDupSoggetti.denominazione}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="sede">${BELFIORE[titolarita.miDupSoggetti.sede]}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="cognome">${titolarita.miDupSoggetti.cognome}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="nome">${titolarita.miDupSoggetti.nome}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="sesso"><c:choose><c:when test="${titolarita.miDupSoggetti.sesso == 1}">M</c:when><c:when test="${titolarita.miDupSoggetti.sesso == 2}">F</c:when> </c:choose></nota:logged></td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="dataNascita"><jsp:setProperty name="FieldConverter" property="data"    value = "${titolarita.miDupSoggetti.dataNascita}" />${FieldConverter.data}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${titolarita.miDupSoggetti}" propertyName="luogoNascita">${BELFIORE[titolarita.miDupSoggetti.luogoNascita]}</nota:logged> </td>
				<c:if test="${!empty titolarita.miDupSoggetti.miDupIndirizziSogs}">			
					<c:forEach var="indirizzo" items="${titolarita.miDupSoggetti.miDupIndirizziSogs}">
						<td colspan="4"><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="indirizzo">${indirizzo.indirizzo}</nota:logged><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="comune"> ${indirizzo.comune}</nota:logged><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="provincia"> ${indirizzo.provincia}</nota:logged>  ${indirizzo.cap} </td>
					</c:forEach>
				</c:if>	
				<c:if test="${empty titolarita.miDupSoggetti.miDupIndirizziSogs}">			
					<td colspan="4">&nbsp;</td>
				</c:if>	
				<c:forEach var="identifica" items="${titolarita.miDupFabbricatiInfo.miDupFabbricatiIdentificas}">
					<td colspan="4">
						<b>Fgl. </b><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="foglio">${identifica.foglio}</nota:logged>
						<b>Part. </b><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="numero">${identifica.numero }</nota:logged>
						<b>Sub. </b><nota:logged nota="${nota}" miDupFabbricatiIdentifica="${identifica}" propertyName="subalterno">${identifica.subalterno }</nota:logged>
					</td>
				</c:forEach>
				<c:if test="${!empty titolarita.miDupTerreniInfo}">			
					<td colspan="4">
						<b>Fgl. </b><nota:logged nota="${nota}" miDupTerreniInfo="${titolarita.miDupTerreniInfo}" propertyName="foglio">${titolarita.miDupTerreniInfo.foglio}</nota:logged>
						<b>Part. </b><nota:logged nota="${nota}" miDupTerreniInfo="${titolarita.miDupTerreniInfo}" propertyName="numero">${titolarita.miDupTerreniInfo.numero }</nota:logged>
						<b>Sub. </b><nota:logged nota="${nota}" miDupTerreniInfo="${titolarita.miDupTerreniInfo}" propertyName="subalterno">${titolarita.miDupTerreniInfo.subalterno }</nota:logged>
					</td>
				</c:if>	
				<td>${titolarita.codiceDup.descrizione }</td>
				<td><nota:logged nota="${nota}" miDupTitolarita="${titolarita}" propertyName="sfQuotaDenominatore"/><nota:logged nota="${nota}" miDupTitolarita="${titolarita}" propertyName="sfQuotaNumeratore"/><fmt:formatNumber value="${titolarita.quota*100}" type="NUMBER"  minFractionDigits="2" maxFractionDigits="2"  />%</td>
			</tr>
 		</c:forEach>
		<c:forEach var="soggetto"  varStatus="status" items="${soggettiNonCoinvoltiNota}">
			<c:if test="${status.first}">
				<tr class="a">
					<td class="a_title" align="center" colspan="20">SOGGETTI NON COINVOLTI</td>
				</tr>
				<tr class="a">
					<td class="a">Sel.</td>
					<td class="a">Codice Fiscale</td>
					<td class="a">Tipo sogg.</td>		
					<td class="a">Denominazione</td>		
					<td class="a">Sede</td>
					<td class="a">Cognome</td>
					<td class="a">Nome</td>
					<td class="a">Sesso</td>
					<td class="a">Data Nascita</td>
					<td class="a">Luogo Nasc.</td>
					<td class="a" colspan="4">Indirizzo</td>		
					<td class="a" colspan="4">Dati Immobile</td>
					<td class="a">Diritto</td>
					<td class="a">Quota poss.</td>		
				</tr>
			</c:if>	
			<tr class="b"><!--${titolarita.iid}/${titolarita.miDupSoggetti.iid}  -->
		   		<td>&nbsp;</td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${soggetto}" propertyName="codiceFiscale">${soggetto.codiceFiscale}</nota:logged><nota:logged nota="${nota }" miDupSoggetti="${soggetto}" propertyName="codiceFiscaleG"> ${soggetto.codiceFiscaleG}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${soggetto}" propertyName="tipoSoggetto">${soggetto.tipoSoggetto}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${soggetto}" propertyName="denominazione">${soggetto.denominazione}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${soggetto}" propertyName="sede">${BELFIORE[soggetto.sede]}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${soggetto}" propertyName="cognome">${soggetto.cognome}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${soggetto}" propertyName="nome">${soggetto.nome}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${soggetto}" propertyName="sesso"><c:choose><c:when test="${soggetto.sesso == 1}">M</c:when><c:when test="${soggetto.sesso == 2}">F</c:when> </c:choose></nota:logged></td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${soggetto}" propertyName="dataNascita"><jsp:setProperty name="FieldConverter" property="data"    value = "${soggetto.dataNascita}" />${FieldConverter.data}</nota:logged> </td>
				<td><nota:logged nota="${nota}" miDupSoggetti="${soggetto}" propertyName="luogoNascita">${BELFIORE[soggetto.luogoNascita]}</nota:logged> </td>
				<c:if test="${!empty soggetto.miDupIndirizziSogs}">			
					<c:forEach var="indirizzo" items="${soggetto.miDupIndirizziSogs}">
						<td colspan="4"><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="indirizzo">${indirizzo.indirizzo}</nota:logged><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="comune"> ${indirizzo.comune}</nota:logged><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="provincia"> ${indirizzo.provincia}</nota:logged><nota:logged nota="${nota}" miDupIndirizziSog="${indirizzo}" propertyName="cap"> ${indirizzo.cap}</nota:logged> </td>
					</c:forEach>
				</c:if>	
				<c:if test="${empty soggetto.miDupIndirizziSogs}">			
					<td colspan="4">&nbsp;</td>
				</c:if>
				<td colspan="4">&nbsp;</td>	
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
 		</c:forEach>
	</tbody>
</table>
</div>
</div>
<c:set var="PRINT_URL" value="notaPdfNoTemplate?iidNota=${nota.iid}" scope="request"/>
<security:lock role="succ-adm,succ-supusr">
<c:if test="${nota.importErrorCount > 0}">			
	<c:set var="FIRST_POPUP_URL" value="formPostNoTemplate/importLogListNoTemplate.jsp?iidNota=${nota.iid}" scope="request"/>
	<c:set var="FIRST_POPUP_DESC" value="Lista non conformit&agrave; formali(${nota.importErrorBloccantiCount} bloccanti/${nota.importErrorCount} totali) " scope="request"/>
</c:if>
</security:lock>

