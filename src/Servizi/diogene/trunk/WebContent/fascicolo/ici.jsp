<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

  <fieldset>
  	<legend>ICI</legend>


<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Rif</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Storico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Prima Casa</span></td>						
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">Mappa</span></td>
	</tr>
	
	<c:forEach var="ici" items="${DATI_FASCICOLO[0]}">
	    
	<tr>
		<td onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData" tipo="datadal"><c:out value="${ici.map.den_riferimento}"/></span></td>
		<td onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.comune}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.foglio}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.particella}"/></span></td>	
		<td  onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.subalterno}"/></span></td>	
		<td  onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.categoria}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.provenienza}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.classe}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.rendita_catastale}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.immobile_storico}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.map.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.abitazione_principale}"/></span></td>
		<td class="TDviewImage" onclick="zoomInMappaParticelle('<c:out value="${ici.map.fk_comuni}"/>','<c:out value="${ici.map.foglio}"/>','<c:out value="${ici.map.particella}"/>');"  style='cursor: pointer;'>
				<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
		</td>
		<td class="TDviewImage" style='cursor: pointer;'>
		<a href="javascript:apriPopupVirtualH('<c:out value="${ici.map.latitudine}"/>','<c:out value="${ici.map.longitudine}"/>');">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30"  />
							</a>

		
		</td>
		<td class="TDviewImage" style='cursor: pointer;'>
		<a href="javascript:apriPopupStreetview('<c:out value="${ici.map.latitudine}"/>','<c:out value="${ici.map.longitudine}"/>');">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32"  />
							</a>

		
		</td>
	</tr>
	</c:forEach>
	</table>	
  </fieldset>
