<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>DOCFA - LISTA COMUNICAZIONE ICI</h2>
<div class="section">
<display:table export="false" id ="docfaComunicazione" name="${docfaComunicaziones}" pagesize="25" sort="list">
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:column property="codfiscalePiva" href="docfaComunicazioneDetail.jsp" paramId="iidComunicazione" paramProperty="iidComunicazione"  sortable="true" title="COD.FISCALE/P.IVA" />
  <display:column title="DOCFA"> 
  ${docfaComunicazione.iidProtocolloReg} ${docfaComunicazione.iidFornitura}
  </display:column>
  <display:column sortable="true" title="Cognome Nome/Ragione Sociale" >
  ${docfaComunicazione.denominazione} ${docfaComunicazione.nome}
  </display:column>
</display:table>
</div>
</div>
