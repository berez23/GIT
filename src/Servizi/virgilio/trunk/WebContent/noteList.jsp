<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>MUI - LISTA NOTE</h2>
<div class="section">
<c:if test="${empty param.isBack }" >
	<c:set var="page_target">target="_blank"</c:set>	
</c:if>  	
<display:table export="false" uid="nota" name="${MiDupNotaTrass}" pagesize="25" sort="list">
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:column  title="NOTA" >
  	<a href="noteDetail.jsp?iidNota=${nota.iid}&backTarget=noteList.jsp%3FisBack%3Dtrue&backTargetLabel=Torna%20alla%20lista%20dei%20risultati" ${page_target} > ${nota.numeroNotaTras }/${ nota.annoNota} </a>
  </display:column>
<%-- 
  <display:column property="iid" href="noteDetail.jsp?backTarget=noteList.jsp&backTargetLabel=Torna%20alla%20lista%20dei%20risultati" paramId="iidNota" paramProperty="iid"  sortable="false" title="NOTA" />  
  <display:column property="numeroNotaTras"  sortable="false" title="NUMERO" />
  <display:column property="annoNota"  sortable="false" title="ANNO" />
--%>
   <display:column sortProperty="dataValiditaAttoDate"  sortable="false" title="DATA DI VALIDITA'" >
	<jsp:setProperty name="FieldConverter" property="data"    value = "${nota.dataValiditaAtto}" />
	${FieldConverter.data}
  </display:column>
  <display:column  title="DATA PRESENTAZIONE" >
	<jsp:setProperty name="FieldConverter" property="data"    value = "${nota.dataPresAtto}" />
	${FieldConverter.data}
  </display:column>
  <display:column property="esitoNota"  sortable="false" title="ESITO" />
  <display:column sortable="false" title="SOGGETTI" >
 	<c:forEach var="soggetto"  varStatus="status" items="${nota.miDupSoggettis }">
  	${soggetto.denominazione }${soggetto.nome } ${soggetto.cognome }<br/>
  	</c:forEach>
  </display:column>
  <security:lock role="mui-adm,mui-supusr">
	  <display:column  href="formPostNoTemplate/importLogList.jsp" paramId="iidNota" paramProperty="iid"  sortable="false" title="" >log
	  </display:column>
  </security:lock>
</display:table>
</div>
</div>
