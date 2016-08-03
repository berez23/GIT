<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>MUI - STATISTICHE DI IMPORTAZIONE</h2>
<div class="section">
<display:table export="false" uid="log" name="${MiQryFornitureLogDetails}" pagesize="25" defaultsort="1" defaultorder="descending">
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:column property="iidFornitura"  sortable="true" title="Tipo Record" headerClass="sortable" />
  <display:column property="tabella"  sortable="true" title="Tabella" />
  <display:column property="colonna"  sortable="true" title="Campo" />
  <display:column property="codiceRegola" sortable="true" title="Regola Infranta" />
  <display:column property="totali" sortable="true" title="Totale" />
  <display:column property="bloccante" sortable="true"   title="BLOCCANTE" />
</display:table>
</div>
</div>
