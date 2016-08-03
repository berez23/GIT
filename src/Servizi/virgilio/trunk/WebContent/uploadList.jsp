<%@include file="jspHead.jsp"%>
<style type="text/css" media="all">
@import url("./css/displaytag.css");
</style>
<div class="section">
<h2>MUI - FORNITURE CARICATE<c:if test="${!empty param.toExport }"> DA ESPORTARE</c:if>
</h2>
<div class="section">
<p>file caricati&nbsp;&nbsp;&nbsp;
<c:choose>
	<c:when test="${!empty param.toExport}">
		<a href='uploadList.jsp?toExport=y&doRefresh=true'>aggiorna lista</a>
	</c:when>
	<c:otherwise>
		<a href='uploadList.jsp?doRefresh=true'>aggiorna lista</a>
	</c:otherwise>
</c:choose>
</p>
<display:table id="caricamento" export="false"
	name="${miDupFornituras }" pagesize="25" sort="list">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:column title="num/anno" sortable="true"
		sortProperty="dataInizialeDate"
		href="formPostNoTemplate/noteSearch.jsp" paramId="miDupFornitura.iid"
		paramProperty="iid">
		${caricamento.numeroAnno}/${caricamento.anno }
	</display:column>
	<display:column title="Data Caricamento" sortable="true"
		href="formPostNoTemplate/noteSearch.jsp" paramId="miDupFornitura.iid"
		paramProperty="iid">
		<dt:format pattern="HH:mm dd/MM/yyyy"
			date="${caricamento.dataCaricamento}" locale="true" />
	</display:column>
	<display:column sortable="true" sortProperty="dataInizialeDate"
		title="Data Iniziale">
		<jsp:setProperty name="FieldConverter" property="data"
			value="${caricamento.dataIniziale}" />
		${FieldConverter.data}
	</display:column>
	<display:column sortable="true" sortProperty="dataFinaleDate"
		title="Data Finale">
		<jsp:setProperty name="FieldConverter" property="data"
			value="${caricamento.dataFinale}" />
		${FieldConverter.data}
	</display:column>
	<display:column sortable="false" title="Note">
		${caricamento.miDupFornituraInfo.note}
	</display:column>
	<%-- 
	<display:column sortable="false" title="di cui Registrate" >
		${caricamento.miDupFornituraInfo.noteRegistrate}
	</display:column>
	<display:column sortable="false" title="di cui Scartate" >
		${caricamento.miDupFornituraInfo.noteScartate}
	</display:column>
	--%>
	<display:column sortable="false" title="Note Caricate">
		<c:if test="${!empty caricamento.miVwNoteSummary.totale  }">
			${caricamento.miVwNoteSummary.totale}
		</c:if>
		<c:if test="${empty caricamento.miVwNoteSummary.totale  }">
			<c:set var="REFRESH_TIMEOUT" scope="request"
				value="${REFRESH_TIMEOUT + 10}" />
			<!-- ${parsers[caricamento.iid].rowCount} --> in caricamento
		</c:if>
	</display:column>
	<c:choose>
		<c:when test="${empty param.toLogInfo }">
			<c:if test="${empty param.toExport }">
				<security:lock role="mui-adm,mui-supusr">
					<display:column href="formPostNoTemplate/importLogList.jsp"
						paramId="iidFornitura" paramProperty="iid" title="log">vedi
			  		</display:column>
					<display:column title="com.ICI">
						<c:if
							test="${caricamento.miDupFornituraInfo.note == caricamento.miVwNoteSummary.totale && caricamento.fullyLoaded }">
							<c:if
								test="${empty caricamento.miVwComunicazioneSummary.totale  }">
								<c:if test="${empty converters[caricamento.iid]  }">
									<a
										href="formPostNoTemplate/comunicazioneResult.jsp?iidFornitura=${caricamento.iid }"><security:lock
										role="mui-adm,mui-supusr">genera</security:lock> </a>
								</c:if>
								<c:if test="${!empty converters[caricamento.iid]  }">
									<c:set var="REFRESH_TIMEOUT" scope="request"
										value="${REFRESH_TIMEOUT + 10}" />
						  		in generazione (${converters[caricamento.iid].rowCount}/${converters[caricamento.iid].totalRowCount})
							</c:if>
							</c:if>
							<c:if
								test="${!empty caricamento.miVwComunicazioneSummary.totale  }">
								<a
									href="formPostNoTemplate/comunicazioneSearch.jsp?iidFornitura=${caricamento.iid }">vedi</a>&nbsp;<a
									href="formPostNoTemplate/comunicazioneExportNoTemplate?iidFornitura=${caricamento.iid }">esporta</a>
							</c:if>
						</c:if>
					</display:column>
				</security:lock>
			</c:if>
			<security:lock role="mui-adm,mui-supusr">
				<display:column title="integrazione">
					<c:if
						test="${caricamento.miDupFornituraInfo.note == caricamento.miVwNoteSummary.totale && caricamento.fullyLoaded }">
						<c:if test="${!empty integrators[caricamento.iid]  }">
							<c:set var="REFRESH_TIMEOUT" scope="request"
								value="${REFRESH_TIMEOUT + 10}" />
					  		in generazione (${integrators[caricamento.iid].rowCount}/${caricamento.miVwTitolariSummary.totale })
						</c:if>
						<c:if test="${empty integrators[caricamento.iid]  }">
							<a
								href="formPostNoTemplate/integrazioneResult.jsp?iidFornitura=${caricamento.iid }">integra</a>
						</c:if>
					</c:if>
				</display:column>
				<display:column title="log integrazione">
					<c:if
						test="${caricamento.miDupFornituraInfo.note == caricamento.miVwNoteSummary.totale && caricamento.fullyLoaded }">
						<c:if test="${empty integrators[caricamento.iid]  }">
							<a
								href="formPostNoTemplate/integrationLogList.jsp?iidFornitura=${caricamento.iid }">log</a>
						</c:if>
					</c:if>
				</display:column>
				<c:if test="${!empty param.toExport }">
					<display:column title="esporta">
						<c:if
							test="${caricamento.miDupFornituraInfo.note == caricamento.miVwNoteSummary.totale && caricamento.fullyLoaded }">
							<a
								href="formPostNoTemplate/uploadExportNoTemplate?iidFornitura=${caricamento.iid }">esporta</a>
						</c:if>
					</display:column>
				</c:if>
				<c:if test="${empty param.toExport }">
					<display:column title="elimina">
						<a href="javascript:eliminaFornitura(${caricamento.iid})"><img
							src="img/del.gif" /></a>
					</display:column>
				</c:if>
				<display:column title="Dap">
					<c:choose>
						<c:when test="${miConsDaps[caricamento.iid].fullyEvaluated}">
							<a
								href="formPostNoTemplate/dapExportNoTemplate?iidFornitura=${caricamento.iid }">esporta</a>
							<a href="javascript:eliminaMiConsDap(${caricamento.iid})">elimina</a>
							<p><a
								href="formPostNoTemplate/noteSearch.jsp?miDupFornitura.iid=${caricamento.iid }&flagSkipped=Y">${miConsDaps[caricamento.iid].dapNA}
							dap non applicabile</a>,<a
								href="formPostNoTemplate/noteSearch.jsp?miDupFornitura.iid=${caricamento.iid }&flagSkipped=N&flagDapDiritto=Y">
							${miConsDaps[caricamento.iid].dapY} con detrazione</a>,<a
								href="formPostNoTemplate/noteSearch.jsp?miDupFornitura.iid=${caricamento.iid }&flagSkipped=N&flagDapDiritto=N">
							${miConsDaps[caricamento.iid].dapN} senza detrazione</a></p>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${!empty dapManagers[caricamento.iid]  }">
									<c:set var="REFRESH_TIMEOUT" scope="request"
										value="${REFRESH_TIMEOUT + 10}" />
							  		in generazione (${dapManagers[caricamento.iid].rowCount}),(${miConsDaps[caricamento.iid].titolarita}/${miConsDaps[caricamento.iid].evaluated}) 
								</c:when>
								<c:otherwise>

									<c:if test="${caricamento.miDupFornituraInfo.note == caricamento.miVwNoteSummary.totale && caricamento.fullyLoaded }">
										<c:if test="${!empty caricamento.miVwNoteSummary.totale  }">
										<a href="formPostNoTemplate/dapEval.jsp?iidFornitura=${caricamento.iid }">genera
										(${miConsDaps[caricamento.iid].titolarita}/${miConsDaps[caricamento.iid].evaluated})</a>
										</c:if>
									</c:if>
									
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</display:column>
			</security:lock>
		</c:when>
		<c:otherwise>
			<display:column
				href="formPostNoTemplate/logstatsList.jsp?iidFornitura=${caricamento.iid}"
				sortable="true" title="errori totali">
		  	${caricamento.miVwFornitureLogSummary.totali}
		  	</display:column>
			<display:column sortable="true" title="di cui bloccanti">
		  	${caricamento.miVwFornitureLogSummary.bloccanti}
		  	</display:column>
		</c:otherwise>
	</c:choose>
</display:table></div>
</div>
