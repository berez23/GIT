<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>DOCFA - LISTA DOCFA</h2>
<div class="section">
<c:if test="${empty param.isBack }" >
	<c:set var="page_target">target="_blank"</c:set>	
</c:if>  	
<display:table export="false" uid="docfa" name="${docfas}" pagesize="25" sort="list" style="table-layout:fixed;width: 1000px" >
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:column  title="DOCFA" >
  	<a href="docfaDetail.jsp?docfaKey=${docfa.fornitura}-${docfa.protocollo}&backTarget=docfaList.jsp%3FisBack%3Dtrue&backTargetLabel=Torna%20alla%20lista%20dei%20risultati" ${page_target} > ${docfa.fornitura}-${docfa.protocollo} </a>
  </display:column>
  <display:column title="DATA PROTOCOLLO" >
  	${docfa.dataProtocollo}	
  </display:column>
  <display:column  title="CAUSALE" >
  	${docfa.causale} 
  </display:column>
  <display:column title="SOPPRESSIONE" >
  	${docfa.soppressione}
  </display:column>
  <display:column title="VARIAZIONE" > 	
  	${docfa.variazione}
  </display:column>
  <display:column title="COSTITUZIONE" > 	
  	${docfa.costituzione}
  </display:column>
  <display:column title="OPERAZIONE" > 	
  	${docfa.operazione}
  </display:column>
  <display:column title="FOGLIO" > 	
  	${docfa.foglio}
  </display:column>
  <display:column title="PARTICELLA" > 	
  	${docfa.particella}  
  </display:column>
  <display:column title="SUBALTERNO" > 	
  	${docfa.subalterno}  
  </display:column>
  <display:column title="DICHIARANTE" > 	
  	${docfa.dichiarante}  
  </display:column>
  <display:column title="INDIRIZZO_PARTICELLA" > 	
  	<c:forEach var="indirizzo"  varStatus="status" items="${docfa.indPart}">
  	${indirizzo }<br/>
  	</c:forEach>
  </display:column>
  
 
</display:table>
</div>
</div>
