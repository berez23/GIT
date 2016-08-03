<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>DOCFA - LOG DI INTEGRAZIONE</h2>
<div class="section">
<display:table export="false" uid="log" name="${DocfaDataLogs}" pagesize="25" sort="external" defaultsort="1" defaultorder="descending">
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:column  href="docfaDetail.jsp?backTarget=docfaIntegrationLogList.jsp&backTargetLabel=Torna%20alla%20lista%20dei%20log%20di%20Importazione&docfaKey=${log.idFornitura }-${ log.idProtocollo}"  title="DOCFA" >
  	${log.idFornitura }-${ log.idProtocollo}
  </display:column>
  <display:column property="tabellaRecord"  sortable="true" title="Tabella/Fonte Dati" />
  <display:column property="colonnaRecord"  sortable="true" title="Campo/ Tipo Informazione" />
  <display:column property="codiceRegola" title="Regola" />
  <display:column property="uiuFoglio" title="UIU Foglio" />
  <display:column property="uiuNumero"  title="UIU Particella" />
  <display:column property="uiuSubalterno"  title="UIU Subalterno" />
</display:table>
</div>
</div>
