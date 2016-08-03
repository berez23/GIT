<%@ page language="java"
	import="java.text.SimpleDateFormat, java.text.DecimalFormat, it.webred.ct.data.model.ruolo.tarsu.*,
			it.escsolution.escwebgis.ruolo.bean.*, it.escsolution.escwebgis.ruolo.logic.*, 
			it.webred.ct.data.access.basic.ruolo.tarsu.dto.*,
		  	java.util.*,java.io.File, java.util.Iterator"%>
<% 					 
	HttpSession sessione = request.getSession(true);
	RuoloTarsuDTO dto = (RuoloTarsuDTO) sessione.getAttribute(RuoloTarsuLogic.RUOLO);
	SitRuoloTarsu r = dto.getRuolo();
	DecimalFormat DF = new DecimalFormat("0.00");
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");

	RuoloFinder finder = null;

	if (sessione.getAttribute(RuoloTarsuLogic.FINDER) != null) {
		finder = (RuoloFinder) sessione
				.getAttribute(RuoloTarsuLogic.FINDER);
	}

	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT") != null)
		js_back = ((Integer) sessione.getAttribute("BACK_JS_COUNT"))
				.intValue();

	java.util.Vector vctLink = null;
	if (sessione.getAttribute("LISTA_INTERFACCE") != null) {
		vctLink = ((java.util.Vector) sessione
				.getAttribute("LISTA_INTERFACCE"));
	}

	java.lang.String funzionalita = (java.lang.String) sessione
			.getAttribute("FUNZIONALITA");
%>

<html>
<head>
<title>Ruolo TARSU - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/style.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/tab.css" type="text/css">
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js"
	language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>
	function visualizzaAnnAssociato(chiave){		
		wait();
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/F24Annullamento";	
		document.mainform.OGGETTO_SEL.value=chiave;	
		document.mainform.ST.value=5;
		document.mainform.UC.value=127;
		document.mainform.EXT.value = 1;
		document.mainform.submit();
	}
	
	function mettiST() {
		document.mainform.ST.value = 3;
	}

	function visualizzaDettaglioOggetto(idx, visDett) {
		document.getElementById("rOgg" + idx).style.display = (visDett ? "none"
				: "");
		document.getElementById("rOggDett" + idx).style.display = (visDett ? ""
				: "none");
	}
</script>

<body>

	<div align="center" class="extWinTDTitle">
		<span class="extWinTXTTitle"> &nbsp;<%=funzionalita%>:&nbsp;Dati
			Ruolo TARSU
		</span>
	</div>

	&nbsp;
	<input type="Image" ID="UserCommand8" align="MIDDLE"
		src="<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print.gif"
		border="0" vspace="0" hspace="0" alt="Stampa"
		onMouseOver="setbutton('UserCommand8','<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print_Over.gif')"
		onMouseOut="setbutton('UserCommand8','<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print.gif')"
		onMouseDown="setbutton('UserCommand8','<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print_Down.gif')"
		onMouseUp="setbutton('UserCommand8','<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/images/print.gif')"
		onClick="Stampa()">
	<br/>

	<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
	&nbsp;

	<form name="mainform"
		action="<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/RuoloTarsu"
		target="_parent">

		<table style="background-color: white; width: 100%;">
			<tr style="background-color: white;" >
			 <td style="background-color: white; vertical-align: top; text-align: center; width: 33%;" colspan=2>
				<table align=center cellpadding="0" cellspacing="0"
						class="editExtTable"
						style="background-color: #C0C0C0; width: 98%; margin-top: 5px; margin-bottom: 15px;">
						<tr>
						<td style="text-align: center;">
						<table class="viewExtTable" cellpadding="0" cellspacing="3"
							style="width: 100%;">
							<tr>
								<td class="TDmainLabel"><span class="TXTmainLabel">Anno</span>
								</td>

								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=r.getAnno()%></span>
								</td>
							
								<td class="TDmainLabel"><span class="TXTmainLabel">Tipo</span>
								</td>
		
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%="S".equalsIgnoreCase(r.getTipo()) ? "Conguaglio" : "Principale"%></span>
								</td>
							</tr>
						</table>
					</td>
					</table>
				</td>
			</tr>

			<tr style="background-color: white;">
			
				<td style="background-color: white; vertical-align: top; text-align: center; width: 33%;">
					<span class="TXTmainLabel">Dati Soggetto</span>
					<table align=center cellpadding="0" cellspacing="0"
						class="editExtTable"
						style="background-color: #C0C0C0; width: 95%; margin-top: 5px; margin-bottom: 15px;">
						<tr>
							<td style="text-align: center;">
								<table class="viewExtTable" cellpadding="0" cellspacing="3"
									style="width: 100%;">
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Fiscale</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=r.getCodfisc()%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Nominativo</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=r.getNominativoContrib()%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Indirizzo Residenza</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=r.getIndirizzo()%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Comune Residenza</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=r.getComune()+(r.getProv()!=null ? " ("+r.getProv()+")" : "")%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Stato Estero</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=r.getEstero()!=null ? r.getEstero():"-"%></span>
									</td>
									</tr>
	
								</table>
							</td>
						</tr>
						<tr>
						</tr>
					</table>
				</td>
				
				<td style="background-color: white; vertical-align: top; text-align: center; width: 33%;">
					<span class="TXTmainLabel">Riepilogo Importi</span>
					<table align=center cellpadding="0" cellspacing="0"
						class="editExtTable"
						style="background-color: #C0C0C0; width: 95%; margin-top: 5px; margin-bottom: 15px;">
						<tr>
							<td style="text-align: center;">
								<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
									<%if("S".equals(r.getTipo())){%>
									<tr>
										<td class="TDmainLabel" ><span class="TXTmainLabel">Importo netto già emesso in acconto</span></td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=DF.format(r.getAccontoTarsuAnno().doubleValue())%></span></td>
									</tr>
									<%} %>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Tot.Netto</span></td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=DF.format(r.getTotNetto().doubleValue()) %></span></td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Add.ECA</span>
										<span class="TXTmainLabel"><%=r.getPercEca()!=null ? "("+DF.format(r.getPercEca().doubleValue())+"%) " : ""%></span>
										</td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=DF.format(r.getAddEca().doubleValue()) %></span></td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Magg.ECA</span>
										<span class="TXTmainLabel"><%=r.getPercMaggEca()!=null ? "("+DF.format(r.getPercMaggEca().doubleValue())+"%) " : ""%></span>
										
										</td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=DF.format(r.getMaggEca().doubleValue()) %></span></td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Trib.Prov.</span>
										<span class="TXTmainLabel"><%=r.getPercTribProv()!=null ? "("+DF.format(r.getPercTribProv().doubleValue())+"%) " : ""%></span>
										</td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=DF.format(r.getTribProv().doubleValue())%></span></td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">TOTALE DA PAGARE</span></td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=DF.format(r.getTotale().doubleValue())%></span></td>
									</tr>
									
								</table>
							</td>
						</tr>
						<tr>
						</tr>
					</table>
				</td>
				</tr>
			<%if(r.getTipoDoc()!=null && dto.getListaNomiPdf()!=null && dto.getListaNomiPdf().size()>0){%>
			<tr><td style="background-color: white; vertical-align: top; text-align: center; width: 33%;" colspan=2>
				<table align=center cellpadding="0" cellspacing="0"
						class="editExtTable"
						style="background-color: #C0C0C0; margin-top: 5px; margin-bottom: 15px;">
						<tr>
						<td style="text-align: center;">
						<table class="viewExtTable" cellpadding="0" cellspacing="3"
							style="width: 100%;">				<tr>
								<td class="TDmainLabel"  style="text-align:center;" colspan=4>
								<span class="TXTmainLabel"> 
								<%=r.getTipoDoc()%>
								</span>
								<%for(String url : dto.getListaNomiPdf()){
								  File f = new File(url);
								  String nomeFile = f.getName();
								%>
								<a href="../RuoloTares?strFilee=<%=url%>" >
								<span class="TXTviewTextBox" title="<%=nomeFile%>"> 
									<img height="15px" src="<%=request.getContextPath()%>/images/pdficon.jpg"/>
									<input type="hidden" id="urlRuoloDoc" value=<%=url%>/>
								</span>
								</a>
							<%}%>
							</td>
									
							   </tr>
						</table>
					</td>
					</table>
				</td>
			</tr>
			<%}%>		
		</table>
		
		
		
    <div class="tabber">
	
	<% 
	List<SitRuoloTarsuImm> immobili = dto.getImmobili();
	if (immobili != null && immobili.size() > 0){
	%>
	<div class="tabbertab" width="98%">
	<h2>Dettaglio Importi</h2>
	
		<table width="98%" class="extWinTable" cellpadding="0" cellspacing="0">
		 <tr>&nbsp;</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CAT.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">MQ</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.TRIBUTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CAUSALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TARIFFA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">RIDUZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO</span></td>
			</tr>
	  <%int contatore=0; 
		for(SitRuoloTarsuImm imm : immobili){ 
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getAnno()%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getIndirizzo()%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getDesCat()%></span></td>
				<td class="extWinTDData"  style="text-align:right;">
				<span class="extWinTXTData"><%=DF.format(imm.getMq().doubleValue())%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getCodTributo()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getCausale()!=null ? imm.getCausale() : "-"%></span></td>
				<td class="extWinTDData"  style="text-align:right;">
				<span class="extWinTXTData">&euro; <%=DF.format(imm.getTariffa().doubleValue())%></span></td>
				<td class="extWinTDData"  style="text-align:right;">
				<span class="extWinTXTData">&euro; <%=DF.format(imm.getRiduzione().doubleValue())%></span></td>
				<td class="extWinTDData"  style="text-align:right;">
				<span class="extWinTXTData">&euro; <%=DF.format(imm.getImporto().doubleValue())%></span></td>
				
			</tr>
			
		<%}%>
	</table>
	</div>
	
	<%}%>
	
	<%List<RataDTO> rate = dto.getRate();
	if (rate != null && rate.size() > 0){
	%>
	<div class="tabbertab">
	<h2>Rate</h2>
	
		<table width="98%" class="extWinTable" cellpadding="0" cellspacing="0">
		 <tr>&nbsp;</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM.RATA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA SCADENZA</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">IMPORTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IV CAMPO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">VERSAMENTI</span></td>

			</tr>
	  <%int contatore=0; 
		for(RataDTO rata : rate){ 
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%="0".equals(rata.getProg()) ? "Unica Soluzione" : rata.getProg()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=SDF.format(rata.getDataScadenza())%></span></td>
				<td class="extWinTDData"  style="text-align:right;">
				<span class="extWinTXTData">&euro; <%=DF.format(rata.getImpBollettino().doubleValue())%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=rata.getTotLettere()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=rata.getvCampo()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%if(rata.getVersamenti()!=null && rata.getVersamenti().size()>0) {%>
				<img src="../images/ok.gif" border="0" title="SI"/>
				<% } else { %>
				<img src="../images/no.gif" border="0" title="NO"/>
				<% } %>
				</span></td>
			</tr>
			
		<%}%>
	</table>
	</div>
	
	<%}%>
	
	<%
	List<SitRuoloTarsuSt> lstst = dto.getListaSt();
	List<SitRuoloTarsuStSg> lststsg = dto.getListaStSg();
	if ((lstst != null && lstst.size() > 0)||(lststsg != null && lststsg.size() > 0)){
	%>
	<div class="tabbertab">
	<h2>Riepilogo Ruolo Principale</h2>
	
		<table width="98%" class="extWinTable" cellpadding="0" cellspacing="0">
		 <tr>&nbsp;</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.TRIBUTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM.FATTURA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FATTURA</span></td>
			</tr>
	  <%int contatore=0; 
		for(SitRuoloTarsuSt imm : lstst){ 
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getAnno()%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getIndirizzo()%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getCodTributo()%></span></td>
				<td class="extWinTDData"  style="text-align:right;">
				<span class="extWinTXTData">&euro; <%=DF.format(imm.getImporto().doubleValue())%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getNumFattura()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=SDF.format(imm.getDataFattura())%></span></td>
			</tr>
			
		<%}%>
	</table>
	
	<%if(lststsg != null && lststsg.size() > 0){ %>
		<table width="98%" class="extWinTable" cellpadding="0" cellspacing="0">
		 <tr>&nbsp;</tr>
		 <tr>Sgravi</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.TRIBUTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM.FATTURA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FATTURA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM.NOTA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA NOTA</span></td>
			</tr>
	  <%contatore=0; 
		for(SitRuoloTarsuStSg imm : lststsg){ 
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getAnno()%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getIndirizzo()%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getCodTributo()%></span></td>
				<td class="extWinTDData"  style="text-align:right;">
				<span class="extWinTXTData">&euro; <%=DF.format(imm.getImporto().doubleValue())%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getNumFattura()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=SDF.format(imm.getDataFattura())%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getNumNota()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=SDF.format(imm.getDataNota())%></span></td>
			</tr>
			
		<%}%>
	</table>
	<%} %>
	</div>
	
	<%}%>
		
	</div>
	<!--  Campi HIDDEN -->
	
	<input type='hidden' name="OGGETTO_SEL" value="">
	
<% if(finder != null){%>
		<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
	<% }else{%>
		<input type='hidden' name="ACT_PAGE" value="">
	<% }%>		
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="128">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">	
</form>
<div id="wait" class="cursorWait" ></div>
		
</body>
</html>