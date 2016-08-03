<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>SUCCESSIONI - LOG DI INTEGRAZIONE</h2>
<div class="section">
<display:table export="false" name="${MiConsIntegrationLogs}" pagesize="25" sort="list">
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:column property="miConsComunicazione.miDupSoggetti.iid" href="comunicazioneDetail.jsp" paramId="iidSoggetto" paramProperty="miConsComunicazione.miDupSoggetti.iid"  sortable="true" title="ID COMUNICAZIONE" />
  <display:column property="tabellaRecord"  sortable="true" title="Tabella" />
  <display:column property="colonnaRegolaInfranta"  sortable="true" title="Campo" />
  <display:column property="codiceRegolaInfranta"  sortable="true" title="Regola Infranta" />
  <display:column property="note"  sortable="true" title="NOTE" />
</display:table>
</div>
</div>
