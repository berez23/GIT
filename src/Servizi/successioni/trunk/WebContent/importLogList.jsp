<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>SUCCESSIONI - LOG DI IMPORTAZIONE</h2>
<div class="section">
<display:table export="false" uid="log" name="${MiDupImportLogs}" pagesize="25" sort="external" defaultsort="1" defaultorder="descending">
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <%--
  <display:column property="idNota" href="noteDetail.jsp" paramId="iidNota" paramProperty="miDupNotaTras.iid"  sortable="true" title="ID NOTA" />
  --%>
  <display:column  href="noteDetail.jsp?backTarget=importLogList.jsp&backTargetLabel=Torna%20alla%20lista%20dei%20log%20di%20Importazione" paramId="iidNota" paramProperty="miDupNotaTras.iid" sortProperty="miDupNotaTras.iid" sortable="true" title="NOTA" >
  	${log.miDupNotaTras.numeroNotaTras }/${ log.miDupNotaTras.annoNota}
  </display:column>
  <display:column property="tipoRecord"  sortable="true" title="Tipo Record" headerClass="sortable" />
  <display:column property="tabellaRecord"  sortable="true" title="Tabella" />
  <display:column property="colonnaRegolaInfranta"  sortable="true" title="Campo" />
  <display:column property="codice" sortProperty="codiceRegolaInfranta" sortable="true" title="Regola Infranta" />
  <display:column property="variante" sortable="false" title="Variante" />
  <display:column property="note"  sortable="false" title="NOTE" />
  <display:column  title="BLOCCANTE" >
	<c:if test="${log.codiceRegolaInfranta.flagBloccante }">
		SI
  	</c:if>
	<c:if test="${! log.codiceRegolaInfranta.flagBloccante }">
		NO
  	</c:if>
  </display:column>
</display:table>
</div>
</div>
