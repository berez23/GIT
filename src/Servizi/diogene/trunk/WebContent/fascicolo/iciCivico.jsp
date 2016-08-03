<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

  <fieldset>
  	<legend>ICI AL CIVICO</legend>


<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Den.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Num.Den.</span></td>
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
	
	<c:forEach var="ici" items="${DATI_FASCICOLO}">
	    
	<tr>
		<td onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.indirizzo.desIndirizzo}"/></span></td>
		<td onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData" tipo="datadal"><c:out value="${ici.annoDenuncia}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.numeroDenuncia}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.foglio}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.particella}"/></span></td>	
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.sub}"/></span></td>	
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.categoria}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.provenienza}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.classe}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.valImmobileF}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.flImmStorico}"/></span></td>
		<td  onclick="dettaglioIci('<c:out value="${ici.id}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${ici.flAbitPrinc}"/></span></td>
		<td class="TDviewImage" onclick="zoomInMappaParticelle('<c:out value="${ici.codEnte}"/>','<c:out value="${ici.foglio}"/>','<c:out value="${ici.particella}"/>');"  style='cursor: pointer;'>
				<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
		</td>
		<td class="TDviewImage" style='cursor: pointer;'>
		<a href="javascript:apriPopupVirtualH('<c:out value="${ici.latitudine}"/>','<c:out value="${ici.longitudine}"/>');">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30"  />
							</a>

		
		</td>
		<td class="TDviewImage" style='cursor: pointer;'>
		<a href="javascript:apriPopupStreetview('<c:out value="${ici.latitudine}"/>','<c:out value="${ici.longitudine}"/>');">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32"  />
							</a>

		
		</td>
	</tr>
	</c:forEach>
	</table>	
  </fieldset>
