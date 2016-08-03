<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

   <fieldset>
  	<legend>TARSU</legend>


<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie Totale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">Mappa</span></td>
	</tr>
	<c:set var="cntTarsu" value="0"/>
	<c:forEach var="tarsu" items="${DATI_FASCICOLO[0]}" varStatus="status">
	    <tr <c:if test='${attuali == "ATTUALE" && tarsu.map.data_fine_ogge != "ATTUALE"}'>style="display:none" id="tar<c:out value='${status.count}'/>"</c:if> >
	    	<td onclick="dettaglioTarsu('<c:out value="${tarsu.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData" ><c:out value="${tarsu.map.data_ini_ogge}"/>-<c:out value="${tarsu.map.data_fine_ogge}"/></span></td>
			<td onclick="dettaglioTarsu('<c:out value="${tarsu.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${tarsu.map.comune}"/></span></td>
			<td onclick="dettaglioTarsu('<c:out value="${tarsu.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${tarsu.map.foglio}"/></span></td>
			<td onclick="dettaglioTarsu('<c:out value="${tarsu.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${tarsu.map.particella}"/></span></td>	
			<td onclick="dettaglioTarsu('<c:out value="${tarsu.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${tarsu.map.subalterno}"/></span></td>		
			<td onclick="dettaglioTarsu('<c:out value="${tarsu.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${tarsu.map.superficie}"/></span></td>
			<td onclick="dettaglioTarsu('<c:out value="${tarsu.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${tarsu.map.provenienza}"/></span></td>
			<td class="TDviewImage" onclick="zoomInMappaParticelle('<c:out value="${tarsu.map.fk_comuni}"/>','<c:out value="${tarsu.map.foglio}"/>','<c:out value="${tarsu.map.particella}"/>');"  style='cursor: pointer;'>
					<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
			</td>
			<td class="TDviewImage" style='cursor: pointer;'>
			<a href="javascript:apriPopupVirtualH('<c:out value="${tarsu.map.latitudine}"/>','<c:out value="${tarsu.map.longitudine}"/>');">
								<img src="../ewg/images/3D.gif" border="0" width="24" height="30"  />
								</a>
	
			
			</td>
			<td class="TDviewImage" style='cursor: pointer;'>
			<a href="javascript:apriPopupStreetview('<c:out value="${tarsu.map.latitudine}"/>','<c:out value="${tarsu.map.longitudine}"/>');">
								<img src="../ewg/images/streetview.gif" border="0" width="17" height="32"  />
								</a>
	
			
			</td>
		</tr>
		<c:set var="cntTarsu" value="${status.count}"/>
	</c:forEach>	
	</table>
	<input type="hidden" id="cntTarsu" value="<c:out value='${cntTarsu}'/>"/>
  </fieldset>
