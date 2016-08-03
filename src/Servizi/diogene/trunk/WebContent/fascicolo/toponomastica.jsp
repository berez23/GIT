<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

   <fieldset>
  	<legend>TOPONOMASTICA</legend>


	<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Sedime</span></td>
		<td class="extWinTDTitle" style="width: 80%;"><span class="extWinTXTTitle">Nome Via</span></td>
		<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Civico</span></td>
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">Mappa</span></td>
	</tr>
	
	<c:forEach var="ici" items="${DATI_FASCICOLO[0]}">
		<tr>
			<td onclick="dettaglioToponomastica('<c:out value="${ici.map.uk_civicof}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.sedime}"/></span></td>
			<td onclick="dettaglioToponomastica('<c:out value="${ici.map.uk_civicof}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.nome_via}"/></span></td>
			<td onclick="dettaglioToponomastica('<c:out value="${ici.map.uk_civicof}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.map.descrizione_civico}"/></span></td>	
			<td class="TDviewImage" onclick="zoomInMappaCivici('<c:out value="${ici.map.cod_nazionale}"/>','<c:out value="${ici.map.pk_sequ_civico}"/>');"  style='cursor: pointer;'>
					<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
			</td>
			<td class="TDviewImage" style='cursor: pointer;'>
				<a href="javascript:apriPopupVirtualH('<c:out value="${ici.map.latitudine}"/>','<c:out value="${ici.map.longitudine}"/>');">
									<img src="../ewg/images/3D.gif" border="0" width="24" height="30"  />
									</a>
			</td>
			<td class="TDviewImage" style='cursor: pointer;'>
				<a href="javascript:apriPopupStreetview('<c:out value="${ici.map.latitudine}"/>','<c:out value="${ici.map.longitudine}"/>');">
									<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
									</a>
			</td>
		</tr>
	</c:forEach>	
	</table>
  </fieldset>
