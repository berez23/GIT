<%@include file="jspHead.jsp"%>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>SUCCESSIONI - CONFRONTO FORNITURE
</h2>
<div class="section">	
	<display:table id="caricamento" export="false"
	name="${cfrFornituras}" pagesize="25" sort="list">
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<display:caption style="width: 100%">
			<table style="width: 100%; border: none">
				<tr>
					<td style="background-color: #fc0; font-size: 18px; width: 38%">
						Dati in origine (n. ${N_FORNITURE})
					</td>
					<td style="width: 4%">
						&nbsp
					</td>
					<td style="background-color: #fc0; font-size: 18px; width: 58%">
						Forniture caricate (n. ${N_FORNITURE_CARICATE})
					</td>
				</tr>
			</table>
		</display:caption>
		<display:column sortable="true" sortProperty="dataInizialeDateOrig"
			title="Data Iniziale" style="width:15%">
			${caricamento.dataInizialeOrigDspl}
		</display:column>
		<display:column sortable="true" sortProperty="dataFinaleDateOrig"
			title="Data Finale" style="width:15%">
			${caricamento.dataFinaleOrigDspl}
		</display:column>
		<display:column sortable="false" title="Note" style="width:8%">
			${caricamento.noteOrigDspl}
		</display:column>
		<display:column sortable="false" title="" headerClass="thwhite" class="tdwhite" style="width:4%; text-align:center;">
			<c:if test="${caricamento.noteOrigDspl != caricamento.noteDspl}">
				<span style="color: red; font-weight: bold;">!</span>
			</c:if>
			<c:if test="${caricamento.noteOrigDspl == caricamento.noteDspl}">
				&nbsp;
			</c:if>
		</display:column>
		<display:column title="num/anno" sortable="true"
			sortProperty="dataInizialeDate" style="width:10%">
			${caricamento.numeroAnnoDspl}
		</display:column>
		<display:column title="Data Caricamento" sortable="true"
			sortProperty="dataCaricamento" style="width:16%">
			${caricamento.dataCaricamentoDspl}
		</display:column>
		<display:column sortable="true" sortProperty="dataInizialeDate"
			title="Data Iniziale" style="width:12%">
			${caricamento.dataInizialeDspl}
		</display:column>
		<display:column sortable="true" sortProperty="dataFinaleDate"
			title="Data Finale" style="width:12%">
			${caricamento.dataFinaleDspl}
		</display:column>
		<display:column sortable="false" title="Note" style="width:8%">
			${caricamento.noteDspl}
		</display:column>	
	</display:table>
</div>
</div>
