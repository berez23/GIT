<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>DOCFA - LOG COMUNICAZIONI</h2>
<div class="section">
<display:table export="false" uid="log" name="${DocfaDataLogs}" pagesize="25" sort="external" defaultsort="1" defaultorder="descending">
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:column  href="docfaDetail.jsp?backTarget=docfaLogList.jsp&backTargetLabel=Torna%20alla%20lista%20dei%20log&docfaKey=${log.idFornitura }-${ log.idProtocollo}"  sortable="true" title="DOCFA protocollo" headerClass="sortable" >
  	${ log.idProtocollo}
  </display:column>
  <display:column property="idFornitura"  sortable="true" title="DOCFA fornitura" headerClass="sortable" />
  <display:column property="tabellaRecord"  title="Tabella/Fonte Dati" />
  <display:column property="colonnaRecord"  title="Campo/ Tipo Informazione" />
  <display:column property="codiceRegola" title="Regola" />
  <display:column property="uiuFoglio" title="UIU Foglio" />
  <display:column property="uiuNumero"  title="UIU Particella" />
  <display:column property="uiuSubalterno"  title="UIU Subalterno" />
  
  <display:column  title="Provenienza controllo" >
	<c:if test="${log.proveninza == 'IMP' }">
		Import Dati Docfa
  	</c:if>
	<c:if test="${log.proveninza == 'INT' }">
		Integrazione Dati Docfa
  	</c:if>
  </display:column>
</display:table>
</div>
</div>
