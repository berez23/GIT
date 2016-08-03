<%@ page language="java"
	import="java.text.SimpleDateFormat, java.text.DecimalFormat, it.webred.ct.data.model.ruolo.tares.*,
			it.escsolution.escwebgis.ruolo.bean.*, it.escsolution.escwebgis.ruolo.logic.*, 
			it.webred.ct.data.access.basic.ruolo.tares.dto.*,
		  	java.util.*,java.math.*,java.io.File, java.util.Iterator"%>
<% 					 
	HttpSession sessione = request.getSession(true);
	RuoloTaresDTO dto = (RuoloTaresDTO) sessione.getAttribute(RuoloTaresLogic.RUOLO);
	SitRuoloTares r = dto.getRuolo();
	DecimalFormat DF = new DecimalFormat("#0.00");
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");

	String zero = DF.format(BigDecimal.ZERO.doubleValue());
	
	RuoloFinder finder = null;

	if (sessione.getAttribute(RuoloTaresLogic.FINDER) != null) {
		finder = (RuoloFinder) sessione
				.getAttribute(RuoloTaresLogic.FINDER);
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
		action="<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/RuoloTares"
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
							<tr>
								<td class="TDmainLabel"><span class="TXTmainLabel">Num.Fattura</span>
								</td>

								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=r.getNumFatt()%></span>
								</td>
								<td class="TDmainLabel"><span class="TXTmainLabel">del</span>
								</td>

								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=SDF.format(r.getDataFatt()) %></span>
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
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Nasc.</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=r.getDataNasc()!=null ? SDF.format(r.getDataNasc()) : "-"%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Luogo Nasc.</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=r.getComuneNasc()!=null ? (r.getComuneNasc()+(r.getProvNasc()!=null ? " ("+r.getProvNasc()+")" : "")) : "-"%></span>
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
										<span class="TXTviewTextBox">&euro; <%=r.getImpNettoAcconto()!=null ? DF.format(r.getImpNettoAcconto().doubleValue()): zero%></span></td>
									</tr>
									<%}%>
									
									<tr>
										<td class="TDmainLabel" ><span class="TXTmainLabel">Tot.Netto</span></td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=r.getTotNetto()!=null ? DF.format(r.getTotNetto().doubleValue()) : zero %></span></td>
									</tr>
	
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Add.IVA</span>
										<span class="TXTmainLabel"><%=r.getPercIva()!=null ? "("+DF.format(r.getPercIva().doubleValue())+"%) " : ""%></span>
										</td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=r.getAddIva()!=null ?  DF.format(r.getAddIva().doubleValue()): zero%></span></td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Trib.Prov.</span>
										<span class="TXTmainLabel"><%=r.getPercTribProv()!=null ? "("+DF.format(r.getPercTribProv().doubleValue())+"%) " : ""%></span>
										
										</td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=r.getTribProv()!=null ?  DF.format(r.getTribProv().doubleValue()): zero%></span></td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">TOTALE DA PAGARE</span></td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=r.getTotale()!=null ? DF.format(r.getTotale().doubleValue()): zero%></span></td>
									</tr>
									<%if("S".equals(r.getTipo())){%>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Tot.Comune</span></td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=r.getTotaleEnte()!=null ?  DF.format(r.getTotaleEnte().doubleValue()): zero %></span></td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Tot.Stato</span></td>
										<td class="TDviewTextBox" style="text-align:right;">
										<span class="TXTviewTextBox">&euro; <%=r.getTotaleStato()!=null ?   DF.format(r.getTotaleStato().doubleValue()): zero %></span></td>
									</tr>
									<%} %>
								
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
	List<SitRuoloTaresImm> immobili = dto.getImmobili();
	if (immobili != null && immobili.size() > 0){
	%>
	<div class="tabbertab" width="98%">
	<h2>Dettaglio Importi</h2>
	
		<table width="98%" class="extWinTable" cellpadding="0" cellspacing="0">
		 <tr>&nbsp;</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		
				<td class="extWinTDTitle"><span class="extWinTXTTitle">SEZ</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">F</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">P</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">S</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CAT.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COD. TRIBUTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CAUSALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">MQ</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TARIFFA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">RIDUZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO</span></td>
			</tr>
	  <%int contatore=0; 
		for(SitRuoloTaresImm imm : immobili){ 
			String tipo = imm.getCodTipo()!=null ? ("D".equals(imm.getCodTipo()) ? "Domestica" : "Non Domestica") : imm.getDesTipo();
			
			contatore++;%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getAnno()%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getIndirizzo()!=null ? imm.getIndirizzo() : ""%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getSez()!=null ? imm.getSez() : ""%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getFoglio()!=null ? imm.getFoglio() : ""%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getParticella()!=null ? imm.getParticella() : ""%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getSub()!=null ? imm.getSub() : ""%></span></td>
				<td class="extWinTDData" style="text-align:center;"  >
					<table><tr><td>
					<span class="extWinTXTData" ><%=(imm.getCat()!=null ? imm.getCat() : "")%></span> 
					</td></tr>
					<tr><td>
					<span class="extWinTXTData" ><%=(imm.getDesCat()!=null ? imm.getDesCat() : "")%></span>
					</td></tr></table>
				</td>
					
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=tipo%></span></td>
				
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getCodTributo()!=null ? imm.getCodTributo() : "" %></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getCausale()!=null ? imm.getCausale() : ""%></span></td>
				<td class="extWinTDData"  style="text-align:right;">
				<span class="extWinTXTData"><%=imm.getMq()!=null ? DF.format(imm.getMq().doubleValue()) : ""%></span></td>
				<td class="extWinTDData"  style="text-align:right;vertical-align:top;">
				<%if(imm.getTariffaQf()!=null || imm.getTariffaQs()!=null || imm.getTariffaQv()!=null){ %>
				 <table style="width:100%;text-align:right;">
				 	<tr>
				 		<td><span class="extWinTXTData">Fissa</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getTariffaQf()!=null ? DF.format(imm.getTariffaQf()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Variabile</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getTariffaQv()!=null ? DF.format(imm.getTariffaQv()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Servizi</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getTariffaQs()!=null ? DF.format(imm.getTariffaQs()) : zero%></span></td>
				 	</tr>

				 </table>
				 <%}else{%>
				 <span class="extWinTXTData" ></span>
				 <%} %>
				</td>
				<td class="extWinTDData"  style="text-align:right;vertical-align:top;">
				<%if(imm.getRiduzioneQf()!=null || imm.getRiduzioneQs()!=null || imm.getRiduzioneQv()!=null){ %>
				 <table style="width:100%;text-align:right;">
				 	<tr>
				 		<td><span class="extWinTXTData">Fissa</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getRiduzioneQf()!=null ? DF.format(imm.getRiduzioneQf()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Variabile</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getRiduzioneQv()!=null ? DF.format(imm.getRiduzioneQv()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Servizi</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getRiduzioneQs()!=null ? DF.format(imm.getRiduzioneQs()) : zero%></span></td>
				 	</tr>
	
				 </table>
				 <%}else{%>
				 <span class="extWinTXTData" ></span>
				 <%} %>
				</td>
				<td class="extWinTDData"  style="text-align:right;">
				<%if(imm.getImportoQf()!=null || imm.getImportoQs()!=null || imm.getImportoQv()!=null){ %>
				 <table style="width:100%;text-align:right;">
				 	<tr>
				 		<td><span class="extWinTXTData">Fissa</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getImportoQf()!=null ? DF.format(imm.getImportoQf()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Variabile</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getImportoQv()!=null ? DF.format(imm.getImportoQv()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Servizi</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getImportoQs()!=null ? DF.format(imm.getImportoQs()) : zero%></span></td>
				 	</tr>				 	
				 	<tr>
				 		<td><span class="extWinTXTData" style="font-weight:bold;">Totale</span></td>
				 		<td><span class="extWinTXTData" style="font-weight:bold;">&euro; <%=imm.getImporto()!=null ? DF.format(imm.getImporto().doubleValue()) : zero %></span></td>
				 	</tr>
				 </table>
				 <%}else{%>
					 <span class="extWinTXTData">&euro; <%=imm.getImporto()!=null ? DF.format(imm.getImporto().doubleValue()) : zero %></span>
				 <%} %>
				</td>
			</tr>
		<%}%>
			
	</table>
	</div>
	
	<%}%>
	
	<%List<SitRuoloTaresRata> rate = dto.getRate();
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
			</tr>
	  <%int contatore=0; 
		for(SitRuoloTaresRata rata : rate){ 
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%="0".equals(rata.getProg()) ? "Unica Soluzione" : rata.getProg()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=rata.getDataScadenza()!=null ? SDF.format(rata.getDataScadenza()) : ""%></span></td>
				<td class="extWinTDData"  style="text-align:right;">
				<span class="extWinTXTData">&euro; <%=rata.getImpBollettino()!=null ? DF.format(rata.getImpBollettino().doubleValue()) : ""%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=rata.getTotLettere()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=rata.getVCampo()%></span></td>
			</tr>
			
		<%}%>
	</table>
	</div>
	
	<%}%>
	
	<%
	List<SitRuoloTaresSt> lstst = dto.getListaSt();
	List<SitRuoloTaresStSg> lststsg = dto.getListaStSg();
	if ((lstst != null && lstst.size() > 0)||(lststsg != null && lststsg.size() > 0)){
	%>
	<div class="tabbertab">
	<h2>Riepilogo Ruolo Principale</h2>
	
		<table width="98%" class="extWinTable" cellpadding="0" cellspacing="0">
		 <tr>&nbsp;</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.TRIBUTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">RIDUZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM.FATTURA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FATTURA</span></td>
			</tr>
	  <%int contatore=0; 
		for(SitRuoloTaresSt imm : lstst){ 
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getAnno()%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getIndirizzo()%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getDesTipo()%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getCodTributo()%></span></td>
				<td class="extWinTDData"  style="text-align:right;vertical-align:top;">
				<%if(imm.getRiduzioneQf()!=null || imm.getRiduzioneQs()!=null || imm.getRiduzioneQv()!=null){ %>
				 <table style="width:100%;text-align:right;">
				 	<tr>
				 		<td><span class="extWinTXTData">Fissa</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getRiduzioneQf()!=null ? DF.format(imm.getRiduzioneQf()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Variabile</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getRiduzioneQv()!=null ? DF.format(imm.getRiduzioneQv()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Servizi</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getRiduzioneQs()!=null ? DF.format(imm.getRiduzioneQs()) : zero%></span></td>
				 	</tr>

				 </table>
				 <%}else{%>
				 <span class="extWinTXTData" ></span>
				 <%} %>
				</td>
				<td class="extWinTDData"  style="text-align:right;">
				<%if(imm.getImportoQf()!=null || imm.getImportoQs()!=null || imm.getImportoQv()!=null){ %>
				 <table style="width:100%;text-align:right;">
				 		<tr>
				 		<td><span class="extWinTXTData">Fissa</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getImportoQf()!=null ? DF.format(imm.getImportoQf()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Variabile</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getImportoQv()!=null ? DF.format(imm.getImportoQv()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Servizi</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getImportoQs()!=null ? DF.format(imm.getImportoQs()) : zero%></span></td>
				 	</tr>				 	
				 	<tr>
				 		<td><span class="extWinTXTData" style="font-weight:bold;">Totale</span></td>
				 		<td><span class="extWinTXTData" style="font-weight:bold;">&euro; <%=imm.getImporto()!=null ? DF.format(imm.getImporto().doubleValue()) : zero %></span></td>
				 	</tr>
				 </table>
				 <%}else{%>
					 <span class="extWinTXTData">&euro; <%=imm.getImporto()!=null ? DF.format(imm.getImporto().doubleValue()) : zero %></span>
				 <%} %>
				 
				</td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getNumFattura()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getDataFattura()!=null ? SDF.format(imm.getDataFattura()) : ""%></span></td>
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
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.TRIBUTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">RIDUZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPORTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM.FATTURA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FATTURA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM.NOTA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA NOTA</span></td>
			</tr>
	  <%contatore=0; 
		for(SitRuoloTaresStSg imm : lststsg){ 
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getAnno()%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getIndirizzo()%></span></td>
			 	<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=imm.getDesTipo()%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getCodTributo()%></span></td>
				<td class="extWinTDData"  style="text-align:right;vertical-align:top;">
				<%if(imm.getRiduzioneQf()!=null || imm.getRiduzioneQs()!=null || imm.getRiduzioneQv()!=null){ %>
				 <table style="width:100%;text-align:right;">
				 	<tr>
				 		<td><span class="extWinTXTData">Fissa</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getRiduzioneQf()!=null ? DF.format(imm.getRiduzioneQf()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Variabile</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getRiduzioneQv()!=null ? DF.format(imm.getRiduzioneQv()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Servizi</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getRiduzioneQs()!=null ? DF.format(imm.getRiduzioneQs()) : zero%></span></td>
				 	</tr>
	
				 </table>
				 <%}else{%>
				 <span class="extWinTXTData" ></span>
				 <%} %>
				</td>
				<td class="extWinTDData"  style="text-align:right;">
				<%if(imm.getImportoQf()!=null || imm.getImportoQs()!=null || imm.getImportoQv()!=null){ %>
				 <table style="width:100%;text-align:right;">
				 	<tr>
				 		<td><span class="extWinTXTData">Fissa</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getImportoQf()!=null ? DF.format(imm.getImportoQf()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Variabile</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getImportoQv()!=null ? DF.format(imm.getImportoQv()) : zero%></span></td>
				 	</tr>
				 	<tr>
				 		<td><span class="extWinTXTData">Servizi</span></td>
				 		<td><span class="extWinTXTData">&euro; <%=imm.getImportoQs()!=null ? DF.format(imm.getImportoQs()) : zero%></span></td>
				 	</tr>
				 	
				 </table>
				 <%}%>
				 <span class="extWinTXTData">&euro; <%=imm.getImporto()!=null ? DF.format(imm.getImporto().doubleValue()) : zero %></span>
				 
				</td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getNumFattura()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getDataFattura()!=null ? SDF.format(imm.getDataFattura()) : ""%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=imm.getNumNota()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=imm.getDataNota()!=null ? SDF.format(imm.getDataNota()): ""%></span></td>
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
			<input type='hidden' name="UC" value="129">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">	
</form>
<div id="wait" class="cursorWait" ></div>
		
</body>
</html>