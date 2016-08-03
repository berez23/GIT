<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<c:choose>
  	<c:when test="${param.classe == null || param.classe == 1}"><c:set var="NOME_ENTITA" value="CODICE ERRORI IMPORTAZIONE"></c:set> </c:when>
  	<c:when test="${param.classe == 2}"><c:set var="NOME_ENTITA" value="CODICI ESITO"></c:set> </c:when>
</c:choose>
<h2>SUCCESSIONI - LISTA ${NOME_ENTITA} </h2>
<div class="section">
<display:table export="false" id ="codiceErrore" name="${codiceErrores}" pagesize="25" sort="list">
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:column  sortProperty="codiceRegolaInfranta"  sortable="true" title="CODICE REGOLA" >
  	<a href="regoleDetail.jsp?codiceRegolaInfranta=${codiceErrore.codiceRegolaInfranta}&backTarget=regoleList.jsp%3FisBack%3Dtrue&backTargetLabel=Torna%20alla%20lista%20dei%20risultati" > 
  	<c:choose>
	  	<c:when test="${codiceErrore.flagIsVariante}">${codiceErrore.codiceBase} var.${codiceErrore.variante}</c:when>
	  	<c:otherwise>${codiceErrore.codiceRegolaInfranta} </c:otherwise>
  	</c:choose>
  	</a>
  </display:column>
  <display:column property="descrizione" sortable="false" title="DESCRIZIONE REGOLA" />
  <display:column  sortable="false" title="BLOCCANTE" >
  	<c:choose>
	  	<c:when test="${codiceErrore.flagBloccante}">SI</c:when>
	  	<c:otherwise>NO </c:otherwise>
  	</c:choose>
  </display:column>
</display:table>
</div>
</div>
