<%@ page language="java"
	import="java.text.SimpleDateFormat, java.text.DecimalFormat, 
			it.escsolution.escwebgis.f24.bean.*, 
		   it.escsolution.escwebgis.f24.logic.*,
		  it.webred.ct.data.access.basic.f24.dto.*,
		  java.util.ArrayList,java.util.Iterator"%>
<% 					 
	HttpSession sessione = request.getSession(true);
	F24VersamentoDTO v = (F24VersamentoDTO) sessione.getAttribute(F24VersamentiLogic.VERSAMENTO);
	DecimalFormat DF = new DecimalFormat("0.00");
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");

	F24Finder finder = null;

	if (sessione.getAttribute(F24VersamentiLogic.FINDER) != null) {
		finder = (F24Finder) sessione
				.getAttribute(F24VersamentiLogic.FINDER);
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
<title>Versamenti F24 - Dettaglio</title>
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
		document.mainform.UC.value=123;
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
		action="<%=it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/F24Versamenti"
		target="_parent">

		<table style="background-color: white; width: 100%;">
			<tr style="background-color: white;">
			
				<td style="background-color: white; vertical-align: top; text-align: center; width: 33%;">
					<span class="TXTmainLabel">Identificativo Record</span>
					<table align=center cellpadding="0" cellspacing="0"
						class="editExtTable"
						style="background-color: #C0C0C0; width: 95%; margin-top: 5px; margin-bottom: 15px;">
						<tr>
							<td style="text-align: center;">
								<table class="viewExtTable" cellpadding="0" cellspacing="3"
									style="width: 100%;">
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Fornitura</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=SDF.format(v.getDtFornitura())%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Prog.Fornitura</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getProgFornitura()%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Ripartizione</span>
										</td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=SDF.format(v.getDtRipartizione())%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Prog.Ripartizione</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getProgRipartizione()%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Bonifico</span>
										</td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=SDF.format(v.getDtBonifico())%></span>
										</td>
									</tr>
									
									<tr>
								</table>
							</td>
						</tr>
						<tr>
						</tr>
					</table>
				</td>
				
					<td style="background-color: white; vertical-align: top; text-align: center; width: 20%;">
					<span class="TXTmainLabel">Indici</span>
					<table align=center cellpadding="0" cellspacing="0"
						class="editExtTable"
						style="background-color: #C0C0C0; width: 95%; margin-top: 5px; margin-bottom: 15px;">
						<tr>
							<td style="text-align: center;">
								<table class="viewExtTable" cellpadding="0" cellspacing="3"
									style="width: 100%;">
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Ravvedimento</span></td>
										<td ><span class="TXTviewTextBox"><%if(v.getRavvedimento() != null && v.getRavvedimento().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Acconto</span></td>
										<td ><span class="TXTviewTextBox"><%if(v.getAcconto() != null && v.getAcconto().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Saldo</span></td>
										<td ><span class="TXTviewTextBox"><%if(v.getSaldo() != null && v.getSaldo().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Immobili variati ICI/IMU</span></td>
										<td ><span class="TXTviewTextBox"><%if(v.getVarImmIciImu() != null && v.getVarImmIciImu().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Num.Fabbricati dichiarati</span></td>
										<td ><span class="TXTviewTextBox"><%=v.getNumFabbIciImu()!=null?v.getNumFabbIciImu():""%></span></td>
										<td ><span class="TXTviewTextBox"><%if(v.getFlagErrIciImu() != null && v.getFlagErrIciImu().intValue()==0) {%>
										<img src="../images/ok.gif" border="0" title="Indicazione dei dati ICI/IMU corretta"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="Indicazione dei dati ICI/IMU errata"/>
										<% } %>
										</span></td>
										</tr>
									
								</table>
							</td>
						</tr>
					</table>
				</td>
				
			
				<td style="background-color: white; vertical-align: top; text-align: center; width: 46%;">
					<span class="TXTmainLabel">Estremi del Versamento</span>
					<table align=center cellpadding="0" cellspacing="0"
						class="editExtTable"
						style="background-color: #C0C0C0; width: 95%; margin-top: 5px; margin-bottom: 15px;">
						<tr>
							<td style="text-align: center;">
								<table class="viewExtTable" cellpadding="0" cellspacing="3"
									style="width: 100%;">
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Anno Riferimento </span></td>

										<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=v.getAnnoRif()%></span>
										</td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%if(v.getFlagErrAr() != null && v.getFlagErrAr().intValue()==0) {%>
										<img src="../images/ok.gif" border="0" title="Anno formalmente corretto"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="Anno formalmente errato"/>
									<% } %>
									</span></td>
									
									</tr>
									
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Rateazione</span>
										</td>

										<td class="TDviewTextBox" colspan="3"><span class="TXTviewTextBox"><%=v.getRateazione()!=null?v.getRateazione():""%></span>
										</td>
									</tr>
									
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Importo
												Credito</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCodValuta()%></span>
										</td>
										<td class="TDviewTextBox" colspan="2" style="text-align: right;"><span class="TXTviewTextBox"><%=DF.format(v.getImpCredito().doubleValue())%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Importo
												Debito</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCodValuta()%></span>
										</td>
										<td class="TDviewTextBox" colspan="2" style="text-align: right;"><span class="TXTviewTextBox"><%=DF.format(v.getImpDebito().doubleValue())%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Detrazione</span>
										</td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCodValuta()%></span>
										</td>
										<td class="TDviewTextBox" colspan="2" style="text-align: right;"><span class="TXTviewTextBox"><%=DF.format(v.getDetrazione().doubleValue())%></span>
										</td>
									</tr>
									<tr>
								</table>
							</td>
						</tr>
					</table>
				</td>


				
				</tr>
				
				
				<tr>
				
								<td colspan="2"
					style="background-color: white; vertical-align: top; text-align: center;">
					<span class="TXTmainLabel">F 24</span>
					<table align=center cellpadding="0" cellspacing="0"
						class="editExtTable"
						style="background-color: #C0C0C0; width: 98%; margin-top: 5px; margin-bottom: 15px;">
						<tr>
							<td style="text-align: center;">
								<table class="viewExtTable" cellpadding="0" cellspacing="3"
									style="width: 100%;">
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Progressivo Delega</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getProgDelega()%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Riga
										</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getProgRiga()%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Ente</span>
										</td>

										<td class="TDviewTextBox" title="<%=v.getDescCodEnteRd()!=null?v.getDescCodEnteRd():"" %>"><span class="TXTviewTextBox"><%=v.getCodEnteRd()!=null?v.getCodEnteRd():""%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Tipo
												Ente</span></td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getTipoEnteRd()!=null?v.getTipoEnteRd():""+" - "+v.getDescTipoEnteRd()!=null?v.getDescTipoEnteRd():""%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">CAB</span>
										</td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCab()!=null?v.getCab():""%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Fiscale/P.IVA</span>
										</td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCf()!=null?v.getCf():""%>
										
										</span></td>
										<td><span><%if(v.getFlagErrCf()!=null && v.getFlagErrCf().intValue()==0) {%>
											<img src="../images/ok.gif" border="0" title="Cod.Fiscale validato in A.T."/>
											<% } else { %>
											<img src="../images/no.gif" border="0" title="Cod.Fiscale errato o inesistente in A.T."/>
										<% } %></span></td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Versamento</span>
										</td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getDtRiscossione()!=null?SDF.format(v.getDtRiscossione()):""%></span>
										</td>
									</tr>
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Comune</span>
										</td>

										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCodEnteCom()%></span>
										</td>
									</tr>
									<tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Tipo Tributo</span>
									</td>
									<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCodTributo()+" - "+v.getDescTipoTributo()%></span>
									</td>
									<td ><span ><%if(v.getFlagErrCt() != null && v.getFlagErrCt().intValue()==0) {%>
										<img src="../images/ok.gif" border="0" title="Cod.Tributo corretto"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="Cod.Tributo errato"/>
									<% } %>
									</span></td>
								</tr>
								<tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Tipo Imposta</span>
									</td>
									<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getTipoImposta()+" - "+v.getDescTipoImposta()%></span>
									</td>
								</table>
							</td>
						</tr>
					</table>
				</td>
					
				<td style="background-color: white; vertical-align: top; text-align: center; width: 33%;">
					<span class="TXTmainLabel">Contribuente</span>
					<table align=center cellpadding="0" cellspacing="0"
						class="editExtTable"
						style="background-color: #C0C0C0; width: 95%; margin-top: 5px; margin-bottom: 15px;">
						<tr>
							<td style="text-align: center;">
								<table class="viewExtTable" cellpadding="0" cellspacing="3"
									style="width: 100%;">
									<tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Fiscale</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getCf()!=null?v.getCf():""%></span></td>
										<td ><%if(v.getFlagErrCf() != null && v.getFlagErrCf().intValue()==0) {%>
											<img src="../images/ok.gif" border="0" title="Cod.Fiscale validato in A.T."/>
											<% } else { %>
											<img src="../images/no.gif" border="0" title="Cod.Fiscale errato o inesistente in A.T."/>
										<% } %>
										</td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Cognome - Denominazione</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getDenominazione()!=null ? v.getDenominazione() : "-" %></span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Nome</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getNome()!=null ? v.getNome() : "-" %></span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Sesso</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getSesso()!=null ? v.getSesso() : "-" %></span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Data Nascita</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getDtNascita()!=null ? SDF.format(v.getDtNascita()) : "-"%></span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Luogo Nascita</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getComuneStato()!=null ? v.getComuneStato() : "-" %></span></td>
										<td class="TDmainLabel"><span class="TXTmainLabel">Prov.</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=v.getProvincia()!=null ? v.getProvincia() : "-" %></span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel" title="Cod.Fiscale del coobbligato, erede, genitore, tutore o curatore fallimentare">Secondo Cod.Fiscale</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox" title="Cod.Fiscale del coobbligato, erede, genitore, tutore o curatore fallimentare"><%=v.getCf2()!=null ? v.getCf2() : "-"%></span></td>
										</tr><tr>
										<td class="TDmainLabel"><span class="TXTmainLabel">Tipo Secondo Soggetto</span></td>
										<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=(v.getCodIdCf2()!=null ? v.getCodIdCf2(): "-")+(v.getDescTipoCf2()!=null? " - "+v.getDescTipoCf2():"")%></span></td>
									</tr>
									
								</table>
							</td>
						</tr>
					</table>
				</td>
			
			</tr>
		
		</table>
		
	<!-- INIZIO Elenco ANNULLAMENTI associati -->
	<% 
	ArrayList annullamenti=(ArrayList)sessione.getAttribute(F24VersamentiLogic.LISTA_ANNULLAMENTI);
	if (annullamenti != null && annullamenti.size() > 0){%>
	 
		<table width="98%" class="extWinTable" cellpadding="0" cellspacing="0">
		 <tr>&nbsp;</tr>
		<tr class="extWinTXTTitle">
				Annullamenti associati
		</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">OPERAZIONE</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">FORNITURA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA OPER.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO RIF.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA VERSAMENTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CONTRIBUENTE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPOSTA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TRIBUTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP.CREDITO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP.DEBITO</span></td>
				
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">RIPARTIZIONE ORIG.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA BONIFICO ORIG.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ENTE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COMUNE</span></td>
			</tr>
	  <%Iterator it = annullamenti.iterator();
		int contatore=0; 
		while(it.hasNext()){ 
			F24AnnullamentoDTO a = (F24AnnullamentoDTO)it.next();
			
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%="A".equalsIgnoreCase(a.getTipoOperazione())? "Annullamento" : ("R".equalsIgnoreCase(a.getTipoOperazione()) ? "Ripristino" : a.getTipoOperazione())%></span></td>
					<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=a.getDtFornitura()!=null?SDF.format(a.getDtFornitura()):""%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=a.getProgFornitura()%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=a.getDtOperazione()!=null?SDF.format(a.getDtOperazione()):""%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=a.getAnnoRif()%></td>	
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=a.getDtRiscossione()!=null?SDF.format(a.getDtRiscossione()):""%></span></td>		
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=a.getCf()%></span></td>
				<td class="extWinTDData" style="text-align: center;" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
				<span class="extWinTXTData" title="<%=a.getDescTipoImposta()%>"><%=a.getTipoImposta()%></span></td>
				<td class="extWinTDData" >
				<span class="extWinTXTData" title="<%=a.getDescTipoTributo()%>"><%=a.getCodTributo()%></span></td>
				<td class="extWinTDData" style="text-align: right;" >
				<span class="extWinTXTData">&euro; <%=DF.format(a.getImpCredito())%></span></td>
				<td class="extWinTDData" style="text-align: right;" >
				<span class="extWinTXTData">&euro; <%=DF.format(a.getImpDebito())%></span></td>
				<td class="extWinTDData"  >
				<span class="extWinTXTData"><%=a.getDtRipartizione()!=null?SDF.format(a.getDtRipartizione()):""%></span></td>
				<td class="extWinTDData" >
				<span class="extWinTXTData"><%=a.getProgRipartizione()%></span></td>
				<td class="extWinTDData" >
				<span class="extWinTXTData"><%=a.getDtBonifico()!=null?SDF.format(a.getDtBonifico()):""%></span></td>
				<td class="extWinTDData" >
				<span class="extWinTXTData"><%=a.getCodEnteRd()%></span></td>
				<td class="extWinTDData" >
				<span class="extWinTXTData"><%=a.getCodEnteCom()%></span></td>
				
			</tr>
			
		<%}%>
	</table>
	
	<%}%>
		
		
	<!--  Campi HIDDEN -->
	
	<input type='hidden' name="OGGETTO_SEL" value="">
	
<% if(finder != null){%>
		<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
	<% }else{%>
		<input type='hidden' name="ACT_PAGE" value="">
	<% }%>		
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="122">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">	
</form>
<div id="wait" class="cursorWait" ></div>
		
</body>
</html>