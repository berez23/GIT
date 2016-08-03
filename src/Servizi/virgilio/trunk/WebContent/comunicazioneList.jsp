<%@include file="jspHead.jsp" %>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>MUI -- LISTA COMUNICAZIONE ICI</h2>
<div class="section">
<display:table export="false" id ="miConsComunicazione" name="${miConsComunicaziones}" pagesize="25" sort="list">
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:column property="codiceFiscale" href="comunicazioneDetail.jsp" paramId="iidSoggetto" paramProperty="miDupSoggetti.iid"  sortable="true" title="ID NOTA" />
  <display:column sortable="true" title="Cognome Nome/Ragione Sociale" >
  ${miConsComunicazione.cognome} ${miConsComunicazione.nome}
  </display:column>
</display:table>
</div>
</div>
