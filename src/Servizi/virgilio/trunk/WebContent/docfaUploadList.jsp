<%@include file="jspHead.jsp"%>
<style type="text/css" media="all">
      @import url("./css/displaytag.css");
</style>
<div class="section">
<h2>DOCFA - FORNITURE CARICATE<c:if test="${!empty param.toExport }"> DA ESPORTARE</c:if>
</h2>
<div class="section">
<p>file caricati</p>
<display:table id="caricamento" export="false"
	name="${docfaFornituras }" pagesize="25" sort="list">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:column title="data fornitura" >
		${caricamento.dataFornitura }
	</display:column>
	<display:column title="Data Caricamento" >
		<dt:format pattern="HH:mm dd/MM/yyyy"
			date="${caricamento.dataInizio}" locale="true" />
	</display:column>
	<display:column sortable="false" title="Docfa Caricati">
		${caricamento.numeroDocfa}
	</display:column>
	<c:choose>
		<c:when test="${empty param.toLogInfo }">
			<c:if test="${empty param.toExport }">
				<security:lock role="mui-adm,mui-supusr">
					<display:column href="formPostNoTemplate/docfaImportLogList.jsp"
						paramId="idFornitura" paramProperty="dataFornitura" title="log">vedi
			  		</display:column>
					<display:column title="com.ICI">
						<a href="formPostNoTemplate/docfaComunicazioneSearch.jsp?fornitura=${caricamento.dataFornitura }">vedi</a>&nbsp;
						<a href="formPostNoTemplate/docfaComunicazioneExportNoTemplate?iidFornitura=${caricamento.dataFornitura }" target="_blank">esporta</a>
					</display:column>
				</security:lock>
			</c:if>
			<security:lock role="mui-adm,mui-supusr">
				<display:column title="log integrazione">
					<a href="formPostNoTemplate/docfaIntegrationLogList.jsp?idFornitura=${caricamento.dataFornitura }">log</a>
				</display:column>
			</security:lock>
			<c:if test="${!empty param.toExport }">
				<display:column title="esporta">
					<a href="formPostNoTemplate/uploadDocfaExportNoTemplate?iidFornitura=${caricamento.dataFornitura }" target="_blank">esporta</a>
				</display:column>
			</c:if>
		</c:when>
	</c:choose>
	<display:column title="Dap">
		<c:choose>
			<c:when test="${!empty docfaDaps[caricamento.dataFornituraComeIId]}">
				<a href="formPostNoTemplate/docfaDapExportNoTemplate?idFornitura=${caricamento.dataFornitura }">esporta</a>
				<a href="javascript:eliminaDocfaDap('${caricamento.dataFornitura}')">elimina</a>
				<p>
					<a href="formPostNoTemplate/docfaSearch.jsp?fornitura=${caricamento.dataFornitura }&flagSkipped=Y">${docfaDaps[caricamento.dataFornituraComeIId].dapNA} dap non applicabile</a>,<a href="formPostNoTemplate/docfaSearch.jsp?fornitura=${caricamento.dataFornitura }&flagSkipped=N&flagDapDiritto=Y"> ${docfaDaps[caricamento.dataFornituraComeIId].dapY} con detrazione</a>,<a href="formPostNoTemplate/docfaSearch.jsp?fornitura=${caricamento.dataFornitura }&flagSkipped=N&flagDapDiritto=N"> ${docfaDaps[caricamento.dataFornituraComeIId].dapN} senza detrazione</a>
					
				</p>	
			</c:when>
			<c:otherwise>
				<c:choose>
						<c:when test="${!empty docfadapManagers[caricamento.dataFornituraComeIId]  }">
					  		in generazione 	<fmt:formatNumber value="${docfadapManagers[caricamento.dataFornituraComeIId].rowCount*100/caricamento.numeroDocfa}" maxFractionDigits="2"/> %
						</c:when>
					<c:otherwise>
						<a
							href="formPostNoTemplate/docfaDapEval.jsp?idFornitura=${caricamento.dataFornitura }">genera
						</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</display:column>	
	
</display:table></div>
</div>
