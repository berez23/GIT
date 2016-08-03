<%@ page language="java"
	import="java.text.SimpleDateFormat, java.text.DecimalFormat, 
			it.escsolution.escwebgis.f24.bean.*, 
		   it.escsolution.escwebgis.f24.logic.*,
		  it.webred.ct.data.access.basic.f24.dto.*,
		  java.util.ArrayList,java.util.Iterator"%>
<% 					 
	HttpSession sessione = request.getSession(true);
	F24AnnullamentoDTO v = (F24AnnullamentoDTO) sessione.getAttribute(F24AnnullamentoLogic.ANNULLAMENTO);
	DecimalFormat DF = new DecimalFormat("0.00");
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");

	F24Finder finder = null;

	if (sessione.getAttribute(F24AnnullamentoLogic.FINDER) != null) {
		finder = (F24Finder) sessione
				.getAttribute(F24AnnullamentoLogic.FINDER);
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
<title>Annullamenti F24 - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/style.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/tab.css" type="text/css">
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js"
	language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>
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
			Flusso F24 Tributi
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
		action="<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/F24Annullamento"
		target="_parent">

		<table style="background-color: white; width: 100%;">
		<tr style="background-color: white;">
		<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
			<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
						<tr>
							<td style="text-align: center;">
								<table class="viewExtTable" cellpadding="0" cellspacing="3"
									style="width: 100%;">
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Fornitura</span></td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=SDF.format(v.getDtFornitura())%></span>
										</td>
									
										<td class="TDmainLabel"><span class="TXTmainLabel">Prog.Fornitura</span></td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=v.getProgFornitura()%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Ripartizione Orig.</span>
										</td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=SDF.format(v.getDtRipartizione())%></span>
										</td>
									
										<td class="TDmainLabel"><span class="TXTmainLabel">Prog.Ripartizione Orig.</span></td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=v.getProgRipartizione()%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Bonifico Orig.</span>
										</td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=SDF.format(v.getDtBonifico())%></span>
										</td>
									
										<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Ente</span>
										</td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=v.getCodEnteRd()%></span>
										</td>
									</tr>
									
									<tr>

										<td class="TDmainLabel"><span class="TXTmainLabel">Data Versamento</span>
										</td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=SDF.format(v.getDtRiscossione())%></span>
										</td>
									
										<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Comune</span>
										</td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=v.getCodEnteCom()%></span>
										</td>
									</tr>
									<tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Tipo Tributo</span>
									</td>
									<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=v.getCodTributo()+" - "+v.getDescTipoTributo()%></span>
									</td>
								
									<td class="TDmainLabel"><span class="TXTmainLabel">Tipo Imposta</span>
									</td>
									<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=v.getTipoImposta()+" - "+v.getDescTipoImposta()%></span>
									</td>
								<tr>
								<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Fiscale/P.IVA</span>
										</td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=v.getCf()%></span></td>
									
										<td class="TDmainLabel"><span class="TXTmainLabel">Anno Riferimento </span></td>

										<td class="TDviewTextBox"  colspan="2"><span class="TXTviewTextBox"><%=v.getAnnoRif()%></span>
										</td>
										
									</tr>
								<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Importo
												Credito</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCodValuta()%></span>
										</td>
										<td class="TDviewTextBox" style="text-align: right;"><span class="TXTviewTextBox"><%=DF.format(v.getImpCredito().doubleValue())%></span>
										</td>
									
										<td class="TDmainLabel"><span class="TXTmainLabel">Importo
												Debito</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCodValuta()%></span>
										</td>
										<td class="TDviewTextBox" style="text-align: right;"><span class="TXTviewTextBox"><%=DF.format(v.getImpDebito().doubleValue())%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Operazione</span>
										</td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=SDF.format(v.getDtOperazione())%></span>
										</td>
									
										<td class="TDmainLabel"><span class="TXTmainLabel">Tipo Operazione </span></td>

										<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%="A".equalsIgnoreCase(v.getTipoOperazione())? "Annullamento" : ("R".equalsIgnoreCase(v.getTipoOperazione()) ? "Ripristino" : v.getTipoOperazione())%></span>
										</td>
										
										
									</tr>
								</table>
							</td>
						</tr>
						<tr>
						</tr>
					</table>
				</td>
				
				</tr>
		</table>
		
		<!-- INIZIO Elenco ANNULLAMENTI associati -->
	<% 
	ArrayList annullamenti=(ArrayList)sessione.getAttribute(F24AnnullamentoLogic.LISTA_VERSAMENTI);
	if (annullamenti != null && annullamenti.size() > 0){%>
	 
		<table width="98%" class="extWinTable" cellpadding="0" cellspacing="0">
		 <tr>&nbsp;</tr>
		<tr class="extWinTXTTitle">
				Versamenti associati
		</tr>
				<tr>
			
			<td class="extWinTDTitle" colspan="7"><span class="extWinTXTTitle">RIFERIMENTO</span></td>
			<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">ENTE</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">CONTRIBUENTE</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">ALTRO SOGGETTO</span></td>
			<td class="extWinTDTitle" colspan="10"><span class="extWinTXTTitle">VERSAMENTO</span></td>
			
			</tr>
			<tr>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">Fornitura</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">Ripartizione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Bonifico</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Prog.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Riga</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CAB</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fisc.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fisc.</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tributo</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Rif.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Credito</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Debito</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Detrazione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Acconto</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Saldo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Ravv.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Var.Imm.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Num.Imm.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Imposta</span></td>
				
			</tr>
	  <%Iterator it = annullamenti.iterator();
		int contatore=0; 
		while(it.hasNext()){ 
			F24VersamentoDTO a = (F24VersamentoDTO)it.next();
			
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=SDF.format(a.getDtFornitura())%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=a.getProgFornitura()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=SDF.format(a.getDtRipartizione())%></span></td>
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=a.getProgRipartizione()%></span></td>
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=SDF.format(a.getDtBonifico())%></span></td>
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=a.getProgDelega()%></span></td>
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData"><%=a.getProgRiga()%></span></td>
				
				<!-- ENTE -->
				<td class="extWinTDData" style="text-align:center;">
				<span class="extWinTXTData" ><%=a.getCodEnteRd()%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData" title="<%=a.getDescTipoEnteRd()%>"><%=a.getTipoEnteRd()%></span>
				</td>
				<td class="extWinTDData" style="text-align:center;"  >
				<span class="extWinTXTData"><%=a.getCab()%></span></td>
				
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=a.getCf()%></span></td>
				<td class="extWinTDData"  style="text-align:center;">
				<span title="<%=a.getCf2()!=null ? (a.getCodIdCf2()+"-"+a.getDescTipoCf2()) : ""%>" class="extWinTXTData"><%=a.getCf2()!=null ? a.getCf2() : "-"%></span></td>
				
				<!-- RISCOSSIONE -->	
				<td class="extWinTDData"  style="text-align:center;">
				<span class="extWinTXTData"><%=SDF.format(a.getDtRiscossione())%></span></td>		
				<td class="extWinTDData" >
				<span class="extWinTXTData" title="<%=a.getDescTipoTributo()%>"><%=a.getCodTributo()%></span></td>
				
				<!-- ESTREMI VERSAMENTO -->
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=a.getAnnoRif()%></td>
				<td class="extWinTDData" style="text-align: right;" >
				<span class="extWinTXTData">&euro; <%=DF.format(a.getImpCredito().doubleValue())%></span></td>
				<td class="extWinTDData" style="text-align: right;" >
				<span class="extWinTXTData">&euro; <%=DF.format(a.getImpDebito().doubleValue())%></span></td>
				<td style="text-align: right;" class="extWinTDData" >
				<span class="extWinTXTData">&euro; <%=DF.format(a.getDetrazione().doubleValue())%></span></td>
					<td class="extWinTDData" style="text-align:center;" >
					<span class="TXTviewTextBox"><%if(a.getAcconto().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
						<td class="extWinTDData" style="text-align:center;" >
						<span class="TXTviewTextBox"><%if(a.getSaldo().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
					<td class="extWinTDData" style="text-align:center;" >						
					<span class="TXTviewTextBox"><%if(a.getRavvedimento().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
					<td class="extWinTDData" style="text-align:center;" >						
					<span class="TXTviewTextBox"><%if(a.getVarImmIciImu().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData"><%=a.getNumFabbIciImu()%></span></td>
				<td class="extWinTDData" style="text-align:center;" >
				<span class="extWinTXTData" title="<%=a.getDescTipoImposta()%>"><%=a.getTipoImposta()%></span></td>
			</tr>
			
		<%}%>
	</table>
	
	<%}%>

		<%
			if (finder != null) {
		%>
		<input type='hidden' name="ACT_PAGE"
			value="<%=finder.getPaginaAttuale()%>">
		<%
			} else {
		%>
		<input type='hidden' name="ACT_PAGE" value="">
		<%
			}
		%>

		<input type='hidden' name="AZIONE" value=""> <input
			type='hidden' name="ST" value=""> <input type='hidden'
			name="UC" value="123"> <input type='hidden' name="EXT"
			value=""> <input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

	</form>

	<div id="wait" class="cursorWait" />
</body>
</html>