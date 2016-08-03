
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<c:if test="${DATI_FASCICOLO[0] != null}">
	  <fieldset>
	  	<legend>UNITA' TERRITORIALI</legend>
	   	<table width="100%" align="center" cellpadding="0" cellspacing="0" class="extWinTable" >
	
		<tr>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub. Terreni</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Sup. Cens. <br /></span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val.</span></td>
			<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">Mappa</span></td>
		</tr>
	    
	    <c:set var="cntDatiCatastali" value="0"/>
	    <c:forEach var="cata" items="${DATI_FASCICOLO[0]}" varStatus="status">
	     
	    <tr <c:if test='${ attuali == "ATTUALE" && cata.map.data_finef != "ATTUALE" }'>style="display:none" id="dat<c:out value='${status.count}'/>"</c:if>  >
			<td  class="extWinTDData" >
			  	<div align="center"><span class="extWinTXTData"><c:out value="${cata.map.foglio}"/></span></div>
			</td>
			<td class="extWinTDData" >
			  	<div align="center"><span class="extWinTXTData"><c:out value="${cata.map.particella}"/></span></div>
			</td>
			<td class="extWinTDData" >
			  	<div align="center"><span class="extWinTXTData"><c:out value="${cata.map.subalterno}"/></span></div>
			</td>
			<td  class="extWinTDData" >
			  <div align="center"><span class="extWinTXTData"><c:out value="${cata.map.superficie}"/></span></div>
			</td>
			<td  class="extWinTDData" >
			  <span class="extWinTXTData" tipo="datafinoal" ><c:out value="${cata.map.data_finef}"/></span>
			</td>
			<td class="TDviewImage" >
				<span onclick="zoomInMappaSubalterno('<c:out value="${cata.map.cod_nazionale}"/>','<c:out value="${cata.map.foglio}"/>','<c:out value="${cata.map.particella}"/>','<c:out value="${cata.map.subalterno}"/>');"  style='cursor: pointer;' class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
			</td>
			<td class="TDviewImage" style='cursor: pointer;'>
				<a href="javascript:apriPopupVirtualH('<c:out value="${cata.map.latitudine}"/>','<c:out value="${cata.map.longitudine}"/>');">
					<img src="../ewg/images/3D.gif" border="0" width="24" height="30"  />
				</a>
			</td>
			<td class="TDviewImage" style='cursor: pointer;'>
				<a href="javascript:apriPopupStreetview('<c:out value="${cata.map.latitudine}"/>','<c:out value="${cata.map.longitudine}"/>');">
					<img src="../ewg/images/streetview.gif" border="0" width="17" height="32"  />
				</a>
			</td>
		</tr>
	    <c:set var="cntDatiCatastali" value="${status.count}"/>
		</c:forEach>
		</table>
		<input type="hidden" id="cntDatiCatastali" value="<c:out value='${cntDatiCatastali}'/>"/>
	  </fieldset>
  </c:if>
  <fieldset>
  	<legend>UNITA' IMMOBILIARI URBANE</legend> 
	   	  <table width="100%" align="center" cellpadding="0" cellspacing="0" class="extWinTable">
	       <tr>
	         <td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>
	      <td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
	      <td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
	      <td class="extWinTDTitle"><span class="extWinTXTTitle">Consistenza</span></td>
	      <td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
	      <td class="extWinTDTitle"><span class="extWinTXTTitle">Sup. U.I.U</span>.</td>
	      <td class="extWinTDTitle"><span class="extWinTXTTitle">Piano</span></td>
	      <td nowrap="nowrap" class="extWinTDTitle"><span class="extWinTXTTitle" >Dal - Al</span></td>
	      <c:if test="${DATI_FASCICOLO[0] != null}">
	      	<td nowrap="nowrap" class="extWinTDTitle"><span class="extWinTXTTitle">Fascicolo Unit. Imm. </span></td>
	      	</c:if>
	       </tr>
	   <c:set var="cntCostituzione" value="0"/>
	   <c:forEach var="costituzione" items="${DATI_FASCICOLO[1].rows}" varStatus="status">
	    <tr <c:if test='${ attuali == "ATTUALE" && costituzione.map.data_fine_valf != "ATTUALE"}'>style="display:none" id="cos<c:out value='${status.count}'/>"</c:if> >
			<td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${costituzione.map.unimm}"/></span></td>
			<td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${costituzione.map.categoria}"/></span></td>
			<td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${costituzione.map.classe}"/></span></td>
			<td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${costituzione.map.consistenza}"/></span></td>
			<td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${costituzione.map.rendita}"/></span></td>
			<td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${costituzione.map.sup_cat}"/></span></td>
			<td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${costituzione.map.piano}"/></span></td>
			<td align="middle" class="extWinTDData"><span class="extWinTXTData" tipo="datadalal"><c:out value="${costituzione.map.data_inizio_valf}"/> - <c:out value="${costituzione.map.data_fine_valf}"/></span></td>
			<c:if test="${DATI_FASCICOLO[0] != null}">
				<td align="middle" nowrap="nowrap" class="extWinTDData"><div align="center"><a  href="javascript:void(0);" onclick="dettaglioFascicoloUnitImm('<c:out value="${costituzione.map.foglio}"/>','<c:out value="${costituzione.map.particella}"/>','<c:out value="${costituzione.map.unimm}"/>'); return false; "><span class="extWinTXTData">visualizza storico</span></a></div></td>
			</c:if>
	    </tr>
	    <c:set var="cntCostituzione" value="${status.count}"/>
	   </c:forEach>
	   </table>
	   <input type="hidden" id="cntCostituzione" value="<c:out value='${cntCostituzione}'/>"/>
   </fieldset>  
   
   
   
  <fieldset>
  	<legend>TITOLARITA' UNITA' IMMOBILIARI URBANE</legend>
	  <table width="100%" align="center" cellpadding="0" cellspacing="0" class="extWinTable">
	  <tr>
	    <td  class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>
	    <td  class="extWinTDTitle"><span class="extWinTXTTitle">Soggetto</span></td>
	    <td  class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
	    <td  class="extWinTDTitle"><span class="extWinTXTTitle">Dal - Al</span></td>
	    <td  class="extWinTDTitle"><span class="extWinTXTTitle">Perc. </span></td>
	  </tr>
	  <c:set var="cntTitolarita" value="0"/>
  	   <c:forEach var="tito" items="${DATI_FASCICOLO[2].rows}" varStatus="status">
  	   
		  <tr <c:if test='${attuali == "ATTUALE" && tito.map.data_finef != "ATTUALE"}'>style="display:none" id="tit<c:out value='${status.count}'/>"</c:if>  >
		    <td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${tito.map.unimm}"/></span></td>
		    <td align="middle" class="extWinTDData" title="<c:out value="${tito.map.cuaa}"/>"><span class="extWinTXTData"><c:out value="${tito.map.ragi_soci}"/></span></td>
		    <td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${tito.map.tipo_titolo}"/></span></td>
		    <td align="middle" nowrap="nowrap" class="extWinTDData"><span class="extWinTXTData" tipo="datadalal"><c:out value="${tito.map.data_iniziof}"/> - <c:out value="${tito.map.data_finef}"/></span></td>
		    <td align="middle" class="extWinTDData"><span class="extWinTXTData"><c:out value="${tito.map.perc_poss}"/></span></td>
	  	</tr>
	  	<c:set var="cntTitolarita" value="${status.count}"/>
	  </c:forEach>
	 </table>
	 <input type="hidden" id="cntTitolarita" value="<c:out value='${cntTitolarita}'/>"/>

</fieldset>

  