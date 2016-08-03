<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

  <fieldset>
  	<legend>DOCUMENTI</legend>
  	  	<fieldset>
  		<legend>DATI DOCFA</legend>
			<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Fornitura</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Documento</span></td>
		
			</tr>
			
			<c:forEach var="docfaPdf" items="${DATI_FASCICOLO[0].rows}">
			    
			<tr>
				<td class="extWinTDData" ><span class="extWinTXTData"><c:out value="${docfaPdf.map.protocollo}"/></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData" tipo="datadal"><c:out value="${docfaPdf.map.fornituraf}"/></span></td>		
				<td class="extWinTDData"><a href="../DocfaImmaginiPlanimetrie?protocollo=<c:out value="${docfaPdf.map.protocollo}"/>&fornitura=<c:out value="${docfaPdf.map.fornituraf}"/>&idFunz=2"><span class="extWinTXTData">PDF</span></a></td>
			</tr>
			</c:forEach>
			</table>
		</fieldset>
  	  	<fieldset>
  		<legend>PLANIMETRIE DOCFA</legend>
			<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Fornitura</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Planimetria</span></td>
		
			</tr>
			<c:set scope="page" var="tmpVarC" value="1"/>
			<c:forEach var="docfaPdf" items="${DATI_FASCICOLO[1].rows}">
				<c:if test="${tmpVar} == ${docfaPdf.map.protocollo}@${docfaPdf.map.fornitura}@${docfaPdf.map.identificativo_immo}"> 
					<c:set scope="page" var="tmpVarC" value="${tmpVarC} + 1"/>
				</c:if>
				<c:if test="${tmpVar} != ${docfaPdf.map.protocollo}@${docfaPdf.map.fornitura}@${docfaPdf.map.identificativo_immo}"> 
					<c:set scope="page" var="tmpVarC" value="1"/>
				</c:if>				
				<c:set scope="page" var="tmpVar"  value="${docfaPdf.map.protocollo}@${docfaPdf.map.fornitura}@${docfaPdf.map.identificativo_immo}"/>
			<tr>
				<td class="extWinTDData" ><span class="extWinTXTData"><c:out value="${docfaPdf.map.protocollo}"/></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData" tipo="datadal"><c:out value="${docfaPdf.map.fornituraf}"/></span></td>		
				<td class="extWinTDData" ><span class="extWinTXTData"><a href="../DocfaImmaginiPlanimetrie?protocollo=<c:out value="${docfaPdf.map.protocollo}"/>&fornitura=<c:out value="${docfaPdf.map.fornitura}"/>&numImmagine=<c:out value="${tmpVarC}"/>&idimmo=<c:out value="${docfaPdf.map.identificativo_immo}"/>&idFunz=1"><c:out value="${docfaPdf.map.nome_plan}"/></span></a></td>
			</tr>
			</c:forEach>
			<c:remove var="tmpVar" scope="page" />
			<c:remove var="tmpVarC" scope="page" />
			</table>
		</fieldset>		
  </fieldset>
