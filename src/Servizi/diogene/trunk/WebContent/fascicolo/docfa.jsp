<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>


  <fieldset>
  	<legend>DOCFA</legend>
<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Protocollo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Causale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Soppressione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Variazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Costituzione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Operazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Dichiarante</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo particella</span></td> 
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Professionista</span></td>
	</tr>	
	
	<c:forEach var="docfa" items="${DATI_FASCICOLO[0].rows}">
	    <tr onclick="dettaglioDocfa('<c:out value="${docfa.map.protocollo}"/>%7C<c:out value="${docfa.map.fornituraf}"/>')">
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.protocollo}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData" tipo="datadal"><c:out value="${docfa.map.data_registrazionef}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.causale}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.soppressione}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.variazione}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.costituzione}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.operazione}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.foglio}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.particella}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.subalterno}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.dichiarante}"/></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.listavie}" escapeXml="false"></c:out></p></span></td>
				<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${docfa.map.tec_cognome}"/> <c:out value="${docfa.map.tec_nome}"/></span></td>
		</tr>
	</c:forEach>
	
	</table>
  </fieldset>
