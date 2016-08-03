<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
   <fieldset>
  	<legend>CONCESSIONI</legend>


<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Prog.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Oggetto</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Soggetti</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Immobili</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzi</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Prov.</span></td>
	</tr>
	
	<c:forEach var="conc" items="${DATI_FASCICOLO}">
	     <tr onclick="dettaglioConcessioni('<c:out value="${conc.id}"/>')">
	     	<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.progAnno}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.progNumero}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.concNumero}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.oggetto}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'>
				<c:forEach var="soggetto" items="${conc.listaSoggetti}">
					<span class="extWinTXTData"><c:out value="${soggetto.titolo}: ${soggetto.datiAnag}"/></span><br/>
				</c:forEach>
			</td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.stringaImmobili}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'>
				<c:forEach var="indirizzo" items="${conc.listaIndirizzi}">
					<span class="extWinTXTData"><c:out value="${indirizzo.indirizzo} ${indirizzo.civico}"/></span><br/>
				</c:forEach>
			</td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.provenienza}"/></span></td>
		</tr>
	</c:forEach>	
	</table>
  </fieldset>