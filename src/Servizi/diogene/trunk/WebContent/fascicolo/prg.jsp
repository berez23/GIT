<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

   <fieldset>
  	<legend>PRG</legend>


	<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle" style="width: 15%;"><span class="extWinTXTTitle">Dest Funz.</span></td>
		<td class="extWinTDTitle" style="width: 20%;"><span class="extWinTXTTitle">Legenda</span></td>
		<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Area Part.</span></td>
		<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Area PRG</span></td>
	<!--	<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Area Int.</span></td>
	 	<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Dal al</span></td>  -->
	</tr>
	
	<c:forEach var="prg" items="${DATI_FASCICOLO[0].rows}">
		<tr>
			<td class="extWinTDData" ><span class="extWinTXTData"><c:out value="${prg.map.foglio}"/></span></td>
			<td class="extWinTDData" ><span class="extWinTXTData"><c:out value="${prg.map.particella}"/></span></td>
			<td class="extWinTDData" ><span class="extWinTXTData"><c:out value="${prg.map.dest_funz}"/></span></td>
			<td class="extWinTDData" ><span class="extWinTXTData"><c:out value="${prg.map.legenda}"/></span></td>
			<td class="extWinTDData" ><span class="extWinTXTData"><c:out value="${prg.map.area_part}"/></span></td>
			<td class="extWinTDData" ><span class="extWinTXTData"><c:out value="${prg.map.area_prg}"/></span></td>					 
		</tr>
	</c:forEach>	
	</table>
  </fieldset>
