<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
   <fieldset>
  	<legend>CONCESSIONI (ARCHIVIO STORICO VISURE) AL CIVICO</legend>


 <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Atto</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome Intestatario</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Atto</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Num. Prot. Gen.</span></td>				
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Num. Prot. Sett.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Doc.</span></td>
	</tr>
	<c:forEach var="conc" items="${DATI_FASCICOLO}">
	     <tr onclick="dettaglioConcessioniVisure('<c:out value="${conc.id}"/>')">
	     	<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.tipoAtto}-${conc.descTipoAtto}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.nomeIntestatario}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.numeroAtto}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.prefisso} ${conc.nomeVia}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.civicoSub}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.numProtGen}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><c:out value="${conc.numProtSett}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><fmt:formatDate value="${conc.dataDoc}" pattern="dd/MM/yyyy"/></span></td>
		</tr>
	</c:forEach>	
	</table>
  </fieldset>