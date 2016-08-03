<%@page import="it.escsolution.escwebgis.fascicoloDoc.bean.PlanimetriaComma340ExtDTO"%>
<%@page import="it.escsolution.escwebgis.fascicoloDoc.bean.DocfaPlanimetrieDatiCensuari"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="it.webred.ct.data.model.docfa.DocfaPlanimetrie"%>
<%@page import="it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO"%>
<%@page import="it.escsolution.escwebgis.fascicoloDoc.logic.FascicoloDocumentiLogic"%>
<%@page import="it.escsolution.escwebgis.fascicoloDoc.bean.FascicoloDocumentiFinder"%>
<%@page import="java.util.List"%>
<%@page language="java"%>

<%
	HttpSession sessione = request.getSession(true);
	String err = (String)session.getAttribute(FascicoloDocumentiLogic.ERROR);

	String ST = (String)sessione.getAttribute("ST");

	FascicoloDocumentiFinder finder = null;

	if (sessione.getAttribute(FascicoloDocumentiLogic.FINDER) != null) {
		finder = (FascicoloDocumentiFinder)sessione.getAttribute(FascicoloDocumentiLogic.FINDER);
	}

	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT") != null)
		js_back = ((Integer) sessione.getAttribute("BACK_JS_COUNT")).intValue();

	java.util.Vector vctLink = null;
	if (sessione.getAttribute("LISTA_INTERFACCE") != null) {
		vctLink = ((java.util.Vector) sessione.getAttribute("LISTA_INTERFACCE"));
	}

	String funzionalita = (String) sessione.getAttribute("FUNZIONALITA");
	
	List<DocfaPlanimetrieDatiCensuari> listaDocfaPlanimetrieUiu = (List<DocfaPlanimetrieDatiCensuari>) sessione.getAttribute(FascicoloDocumentiLogic.LISTA_DOCFA_PLANIMETRIE_UIU);
	List<DocfaPlanimetrieDatiCensuari> listaDocfaPlanimetrieFab = (List<DocfaPlanimetrieDatiCensuari>) sessione.getAttribute(FascicoloDocumentiLogic.LISTA_DOCFA_PLANIMETRIE_FAB);
	List<PlanimetriaComma340ExtDTO> listaC340PlanimetrieUiu = (List<PlanimetriaComma340ExtDTO>) sessione.getAttribute(FascicoloDocumentiLogic.LISTA_C340_PLANIMETRIE_UIU);
	List<PlanimetriaComma340DTO> listaC340PlanimetrieFab = (List<PlanimetriaComma340DTO>) sessione.getAttribute(FascicoloDocumentiLogic.LISTA_C340_PLANIMETRIE_FAB);
	
	boolean viewNoWatermark = sessione.getAttribute(FascicoloDocumentiLogic.VIEW_NO_WATERMARK) == null ? false : ((Boolean)sessione.getAttribute(FascicoloDocumentiLogic.VIEW_NO_WATERMARK)).booleanValue();
%>

<html>
	<head>
		<title>Fascicolo Planimetrie - Dettaglio</title>
		<LINK rel="stylesheet" href="<%=request.getContextPath()%>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/tab.css" type="text/css">
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
		
		<script>
			function mettiST() {
				document.mainform.ST.value = 3;
			}

			function apriPlanimetria(nomePlan, linkPlan, padProgressivo, fornituraStr, formato, openJpg, watermark, tipoPlan) {
				document.mainform.ST.value = 99;
				document.mainform.nomePlan.value = nomePlan;
				document.mainform.linkPlan.value = linkPlan;
				document.mainform.padProgressivo.value = padProgressivo;
				document.mainform.fornituraStr.value = fornituraStr;
				document.mainform.formato.value = formato;
				document.mainform.openJpg.value = openJpg;
				document.mainform.watermark.value = watermark;
				document.mainform.tipoPlan.value = tipoPlan;
				document.mainform.submit();
			}

			function indietro() {
				history.back();
			}
		</script>
		
	</head>		
	
	<body>
		
		<div align="center" class="extWinTDTitle">
			<span class="extWinTXTTitle"> &nbsp;<%=funzionalita%>:&nbsp;Dettaglio
			</span>
		</div>

		&nbsp;
		
		<form name="mainform"
		action="<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/FascicoloDocumenti"
		target="_parent">
		
			<input type='hidden' name="nomePlan" value="">
			<input type='hidden' name="linkPlan" value="">
			<input type='hidden' name="padProgressivo" value="">
			<input type='hidden' name="fornituraStr" value="">
			<input type='hidden' name="formato" value="">
			<input type='hidden' name="openJpg" value="">
			<input type='hidden' name="watermark" value="">
			<input type='hidden' name="tipoPlan" value="">
		
			<% if (err != null && !err.trim().equals("")) { %>
				<div style="margin-left: 10%;">			
					<span class="TXTviewTextBox" style="font-size: 16px;"><%=err%></span>
				</div>
				<br />
				<div align="center">
					<input type='button' value="Indietro" class="tdButton" onclick="indietro();">
				</div>
			<% } else {
				
				String chiave = (String) sessione.getAttribute(FascicoloDocumentiLogic.CHIAVE);
				String[] arrChiave = chiave.split("\\|");
				String sezione = arrChiave[0].trim().equals("") ? "-" : arrChiave[0].trim();
				String foglio = arrChiave[1].trim();
				String particella = arrChiave[2].trim();
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); %>
				
				<div align="center">
					<table class="viewExtTable" style="width: 60%;">
						<tr>
							<td class="TDmainLabel" style="width: 16.6%;"><span class="TXTmainLabel">Sezione</span></td>
							<td class="TDviewTextBox" style="width: 16.7%;"><span class="TXTviewTextBox"><%=sezione%></span></td>
							<td class="TDmainLabel" style="width: 16.7%;"><span class="TXTmainLabel">Foglio</span></td>
							<td class="TDviewTextBox" style="width: 16.6%;"><span class="TXTviewTextBox"><%=foglio%></span></td>
							<td class="TDmainLabel" style="width: 16.7%;"><span class="TXTmainLabel">Particella</span></td>
							<td class="TDviewTextBox" style="width: 16.7%;"><span class="TXTviewTextBox"><%=particella%></span></td>
						</tr>
					</table>
				</div>
				
				<br/>

				<div class="tabber" style="margin-left: 10%; margin-right: 10%;">
					<div class="tabbertab">
					
						<h2>Planimetrie DOCFA</h2>
						
						<div style="margin-left: 10px; margin-right: 10px;">
							<br />
							<table style="width: 100%;" class="extWinTable" cellpadding="0" cellspacing="0">
								<tr> 
									<td class="extWinTDTitle" style="text-align: left; padding-left: 5px;" colspan="9">
										<span class="extWinTXTTitle">
											Planimetrie relative alle unità immobiliari
										</span>
									</td>
								</tr>
								<% if(listaDocfaPlanimetrieUiu != null && listaDocfaPlanimetrieUiu.size() > 0) { %>
									<tr>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Subalterno</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Categoria</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Protocollo</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Fornitura</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Data registr.</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Identif. Immobile</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Progressivo</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Scarica in versione PDF</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Scarica in versione JPEG</span>
										</td>
									</tr>
									<% for (DocfaPlanimetrieDatiCensuari plan : listaDocfaPlanimetrieUiu) { %>
									    <tr>
									    	<td class="extWinTDData" >
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getSubalternoStr()%></span></td>
											<td class="extWinTDData" >
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getCategoriaStr()%></span></td>
											<td class="extWinTDData" >
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getDocfaPlanimetrie().getProtocollo()%></span></td>
											<td class="extWinTDData" style="text-align: center;">
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getDocfaPlanimetrie().getFornitura() == null ? "-" : sdf.format(plan.getDocfaPlanimetrie().getFornitura())%></span></td>
											<td class="extWinTDData" style="text-align: center;">
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getDocfaPlanimetrie().getDataRegistrazione() == null ? "-" : sdf.format(plan.getDocfaPlanimetrie().getDataRegistrazione())%></span></td>
											<td class="extWinTDData" style="text-align: right;">
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getDocfaPlanimetrie().getIdentificativoImmo()%></span></td>	
											<td class="extWinTDData" style="text-align: right;">
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getDocfaPlanimetrie().getId().getProgressivo()%></span></td>	
											<td class="extWinTDData" style="text-align: center;">
												<% if (viewNoWatermark) { %>
													<span class="extWinTXTData" style="padding-right: 10px;">													
														<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getDocfaPlanimetrie().getId().getNomePlan()%>',
																							'',
																							'<%=plan.getDocfaPlanimetrie().getId().getPadProgressivo()%>', 
																							'<%=plan.getDocfaPlanimetrie().getFornituraStr()%>',
																							'<%=plan.getDocfaPlanimetrie().getFormato()%>',
																							 'false',
																							 'false',
																							 '<%=FascicoloDocumentiLogic.TIPO_PLAN_DOCFA%>');">
															Apri
														</a>
													</span>
												<% } %>
												<span class="extWinTXTData">
													<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getDocfaPlanimetrie().getId().getNomePlan()%>',
																						'',
																						'<%=plan.getDocfaPlanimetrie().getId().getPadProgressivo()%>', 
																						'<%=plan.getDocfaPlanimetrie().getFornituraStr()%>',
																						'<%=plan.getDocfaPlanimetrie().getFormato()%>',
																						 'false',
																						 'true',
																						 '<%=FascicoloDocumentiLogic.TIPO_PLAN_DOCFA%>');">
														Apri con Watermark
													</a>
												</span>
											</td>
											<td class="extWinTDData" style="text-align: center;">
												<% if (viewNoWatermark) { %>
													<span class="extWinTXTData" style="padding-right: 10px;">
														<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getDocfaPlanimetrie().getId().getNomePlan()%>',
																							'',
																							'<%=plan.getDocfaPlanimetrie().getId().getPadProgressivo()%>', 
																							'<%=plan.getDocfaPlanimetrie().getFornituraStr()%>',
																							'<%=plan.getDocfaPlanimetrie().getFormato()%>',
																							 'true',
																							 'false',
																							 '<%=FascicoloDocumentiLogic.TIPO_PLAN_DOCFA%>');">
															Apri
														</a>
													</span>
												<% } %>
												<span class="extWinTXTData">
													<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getDocfaPlanimetrie().getId().getNomePlan()%>',
																						'',
																						'<%=plan.getDocfaPlanimetrie().getId().getPadProgressivo()%>', 
																						'<%=plan.getDocfaPlanimetrie().getFornituraStr()%>',
																						'<%=plan.getDocfaPlanimetrie().getFormato()%>',
																						 'true',
																						 'true',
																						 '<%=FascicoloDocumentiLogic.TIPO_PLAN_DOCFA%>');">
														Apri con Watermark
													</a>
												</span>
											</td>
										</tr>
									<% } %>
								<% } else { %>
									<tr> 
										<td class="TDviewTextBox" style="text-align: left; padding: 5px;" colspan="9">
											<span class="TXTviewTextBox">
												Nessuna planimetria presente
											</span>
										</td>
									</tr>
								<% } %>
							</table>
							<br />
							<table style="width: 100%;" class="extWinTable" cellpadding="0" cellspacing="0">
								<tr> 
									<td class="extWinTDTitle" style="text-align: left; padding-left: 5px;" colspan="6">
										<span class="extWinTXTTitle">
											Elaborati planimetrici
										</span>
									</td>
								</tr>
								<% if(listaDocfaPlanimetrieFab != null && listaDocfaPlanimetrieFab.size() > 0) { %>
									<tr>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Protocollo</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Fornitura</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Data registr.</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Progressivo</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Scarica in versione PDF</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Scarica in versione JPEG</span>
										</td>
									</tr>
									<% for (DocfaPlanimetrieDatiCensuari plan : listaDocfaPlanimetrieFab) { %>
									    <tr>
											<td class="extWinTDData">
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getDocfaPlanimetrie().getProtocollo()%></span></td>
											<td class="extWinTDData" style="text-align: center;">
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getDocfaPlanimetrie().getFornitura() == null ? "-" : sdf.format(plan.getDocfaPlanimetrie().getFornitura())%></span></td>
											<td class="extWinTDData" style="text-align: center;">
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getDocfaPlanimetrie().getDataRegistrazione() == null ? "-" : sdf.format(plan.getDocfaPlanimetrie().getDataRegistrazione())%></span></td>
											<td class="extWinTDData" style="text-align: right;">
											<span class="extWinTXTData" <%if (plan.isLastSit()) {%> style = "color:green; font-weight:bold;" <%} %>><%=plan.getDocfaPlanimetrie().getId().getProgressivo()%></span></td>	
											<td class="extWinTDData" style="text-align: center;">
												<% if (viewNoWatermark) { %>
													<span class="extWinTXTData" style="padding-right: 10px;">
														<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getDocfaPlanimetrie().getId().getNomePlan()%>',
																							'',
																							'<%=plan.getDocfaPlanimetrie().getId().getPadProgressivo()%>', 
																							'<%=plan.getDocfaPlanimetrie().getFornituraStr()%>',
																							'<%=plan.getDocfaPlanimetrie().getFormato()%>',
																							 'false',
																							 'false',
																							 '<%=FascicoloDocumentiLogic.TIPO_PLAN_DOCFA%>');">
															Apri
														</a>
													</span>
												<% } %>
												<span class="extWinTXTData">
													<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getDocfaPlanimetrie().getId().getNomePlan()%>',
																						'',
																						'<%=plan.getDocfaPlanimetrie().getId().getPadProgressivo()%>', 
																						'<%=plan.getDocfaPlanimetrie().getFornituraStr()%>',
																						'<%=plan.getDocfaPlanimetrie().getFormato()%>',
																						 'false',
																						 'true',
																						 '<%=FascicoloDocumentiLogic.TIPO_PLAN_DOCFA%>');">
														Apri con Watermark
													</a>
												</span>
											</td>
											<td class="extWinTDData" style="text-align: center;">
												<% if (viewNoWatermark) { %>
													<span class="extWinTXTData" style="padding-right: 10px;">
														<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getDocfaPlanimetrie().getId().getNomePlan()%>',
																							'',
																							'<%=plan.getDocfaPlanimetrie().getId().getPadProgressivo()%>', 
																							'<%=plan.getDocfaPlanimetrie().getFornituraStr()%>',
																							'<%=plan.getDocfaPlanimetrie().getFormato()%>',
																							 'true',
																							 'false',
																							 '<%=FascicoloDocumentiLogic.TIPO_PLAN_DOCFA%>');">
															Apri
														</a>
													</span>
												<% } %>
												<span class="extWinTXTData">
													<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getDocfaPlanimetrie().getId().getNomePlan()%>',
																						'',
																						'<%=plan.getDocfaPlanimetrie().getId().getPadProgressivo()%>', 
																						'<%=plan.getDocfaPlanimetrie().getFornituraStr()%>',
																						'<%=plan.getDocfaPlanimetrie().getFormato()%>',
																						 'true',
																						 'true',
																						 '<%=FascicoloDocumentiLogic.TIPO_PLAN_DOCFA%>');">
														Apri con Watermark
													</a>
												</span>
											</td>
										</tr>
									<% } %>
								<% } else { %>
									<tr> 
										<td class="TDviewTextBox" style="text-align: left; padding: 5px;" colspan="6">
											<span class="TXTviewTextBox">
												Nessun elaborato planimetrico presente
											</span>
										</td>
									</tr>
								<% } %>
							</table>
							<br />
						</div>
					</div>
				
					<div class="tabbertab">
					
						<h2>Planimetrie Comma 340</h2>
						
						<div style="margin-left: 10px; margin-right: 10px;">
							<br />
							<table style="width: 100%;" class="extWinTable" cellpadding="0" cellspacing="0">
								<tr> 
									<td class="extWinTDTitle" style="text-align: left; padding-left: 5px;" colspan="5">
										<span class="extWinTXTTitle">
											Planimetrie relative alle unità immobiliari
										</span>
									</td>
								</tr>
								<% if(listaC340PlanimetrieUiu != null && listaC340PlanimetrieUiu.size() > 0) { %>
									<tr>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Subalterno</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Categoria</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">File</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Scarica in versione PDF</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Scarica in versione JPEG</span>
										</td>
									</tr>
									<% for (PlanimetriaComma340ExtDTO plan : listaC340PlanimetrieUiu) { %>
									    <tr>
											<td class="extWinTDData">
											<span class="extWinTXTData"><%=plan.getPlanimetriaComma340().getSubalterno()%></span></td>
											<td class="extWinTDData">
											<span class="extWinTXTData"><%=plan.getCategoria()%></span></td>
											<td class="extWinTDData">
											<span class="extWinTXTData"><%=plan.getPlanimetriaComma340().getFile()%></span></td>	
											<td class="extWinTDData" style="text-align: center;">
												<% if (viewNoWatermark) { %>
													<span class="extWinTXTData" style="padding-right: 10px;">
														<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getPlanimetriaComma340().getFile()%>',
																							'<%=plan.getPlanimetriaComma340().getLink()%>',
																							'', 
																							'',
																							'4',
																							 'false',
																							 'false',
																							 '<%=FascicoloDocumentiLogic.TIPO_PLAN_C340%>');">
															Apri
														</a>
													</span>
												<% } %>
												<span class="extWinTXTData">
													<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getPlanimetriaComma340().getFile()%>',
																						'<%=plan.getPlanimetriaComma340().getLink()%>',
																						'', 
																						'',
																						'4',
																						 'false',
																						 'true',
																						 '<%=FascicoloDocumentiLogic.TIPO_PLAN_C340%>');">
														Apri con Watermark
													</a>
												</span>
											</td>
											<td class="extWinTDData" style="text-align: center;">
												<% if (viewNoWatermark) { %>
													<span class="extWinTXTData" style="padding-right: 10px;">
														<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getPlanimetriaComma340().getFile()%>',
																							'<%=plan.getPlanimetriaComma340().getLink()%>',
																							'', 
																							'',
																							'4',
																							 'true',
																							 'false',
																							 '<%=FascicoloDocumentiLogic.TIPO_PLAN_C340%>');">
															Apri
														</a>												
													</span>
												<% } %>
												<span class="extWinTXTData">
													<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getPlanimetriaComma340().getFile()%>',
																						'<%=plan.getPlanimetriaComma340().getLink()%>',
																						'', 
																						'',
																						'4',
																						 'true',
																						 'true',
																						 '<%=FascicoloDocumentiLogic.TIPO_PLAN_C340%>');">
														Apri con Watermark
													</a>
												</span>
											</td>
										</tr>
									<% } %>
								<% } else { %>
									<tr> 
										<td class="TDviewTextBox" style="text-align: left; padding: 5px;" colspan="5">
											<span class="TXTviewTextBox">
												Nessuna planimetria presente
											</span>
										</td>
									</tr>
								<% } %>
							</table>
							<br />
							<table style="width: 100%;" class="extWinTable" cellpadding="0" cellspacing="0">
								<tr> 
									<td class="extWinTDTitle" style="text-align: left; padding-left: 5px;" colspan="3">
										<span class="extWinTXTTitle">
											Elaborati planimetrici
										</span>
									</td>
								</tr>
								<% if(listaC340PlanimetrieFab != null && listaC340PlanimetrieFab.size() > 0) { %>
									<tr>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">File</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Scarica in versione PDF</span>
										</td>
										<td class="extWinTDTitle">
											<span class="extWinTXTTitle">Scarica in versione JPEG</span>
										</td>
									</tr>
									<% for (PlanimetriaComma340DTO plan : listaC340PlanimetrieFab) { %>
									    <tr>
											<td class="extWinTDData">
											<span class="extWinTXTData"><%=plan.getFile()%></span></td>	
											<td class="extWinTDData" style="text-align: center;">
												<% if (viewNoWatermark) { %>
													<span class="extWinTXTData" style="padding-right: 10px;">
														<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getFile()%>',
																							'<%=plan.getLink()%>',
																							'', 
																							'',
																							'4',
																							 'false',
																							 'false',
																							 '<%=FascicoloDocumentiLogic.TIPO_PLAN_C340%>');">
															Apri
														</a>
													</span>
												<% } %>
												<span class="extWinTXTData">
													<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getFile()%>',
																						'<%=plan.getLink()%>',
																						'', 
																						'',
																						'4',
																						 'false',
																						 'true',
																						 '<%=FascicoloDocumentiLogic.TIPO_PLAN_C340%>');">
														Apri con Watermark
													</a>
												</span>
											</td>
											<td class="extWinTDData" style="text-align: center;">
												<% if (viewNoWatermark) { %>
													<span class="extWinTXTData" style="padding-right: 10px;">
														<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getFile()%>',
																							'<%=plan.getLink()%>',
																							'', 
																							'',
																							'4',
																							 'true',
																							 'false',
																							 '<%=FascicoloDocumentiLogic.TIPO_PLAN_C340%>');">
															Apri
														</a>
													</span>
												<% } %>
												<span class="extWinTXTData">
													<a style="font-size: 12px;" href="javascript:apriPlanimetria('<%=plan.getFile()%>',
																						'<%=plan.getLink()%>',
																						'', 
																						'',
																						'4',
																						 'true',
																						 'true',
																						 '<%=FascicoloDocumentiLogic.TIPO_PLAN_C340%>');">
														Apri con Watermark
													</a>
												</span>
											</td>
										</tr>
									<% } %>
								<% } else { %>
									<tr> 
										<td class="TDviewTextBox" style="text-align: left; padding: 5px;" colspan="3">
											<span class="TXTviewTextBox">
												Nessun elaborato planimetrico presente
											</span>
											</td>
										</tr>
									<% } %>
								</table>
								<br />
							</div>
						</div>
					</div>
				
			<% } %>
			
			<% if (finder != null) { %>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<% } else { %>
				<input type='hidden' name="ACT_PAGE" value="">
			<% } %>

			<input type='hidden' name="AZIONE" value=""> 
			<input type='hidden' name="ST" value=""> 
			<input type='hidden' name="UC" value="136">
			<input type='hidden' name="EXT"	value=""> 
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
			
		</form>
		
	</body>
	
</html>