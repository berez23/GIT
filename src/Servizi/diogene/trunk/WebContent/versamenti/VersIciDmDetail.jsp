<%@ page language="java"
	import="java.text.SimpleDateFormat, java.text.DecimalFormat, it.webred.ct.data.model.versamenti.iciDM.*,
			it.escsolution.escwebgis.versamenti.bean.*, it.escsolution.escwebgis.versamenti.iciDM.logic.*, 
			it.webred.ct.data.access.basic.versamenti.iciDM.dto.*,
		  	java.util.*,java.math.*,java.io.File, java.util.Iterator"%>
<% 					 
	HttpSession sessione = request.getSession(true);

	DecimalFormat DF = new DecimalFormat("#0.00");
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");

	String zero = DF.format(BigDecimal.ZERO.doubleValue());
	
	VersFinder finder = null;

	if (sessione.getAttribute(VersIciDmLogic.FINDER) != null) {
		finder = (VersFinder) sessione
				.getAttribute(VersIciDmLogic.FINDER);
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
<title>Versamento ICI (D.M.)</title>
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
			Versamento ICI
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
		
		<% VersamentoIciDmDTO vers = (VersamentoIciDmDTO) sessione.getAttribute(VersIciDmLogic.VERSAMENTO);
		if(vers!=null){%>
		<div align="center" style="width:100%;" >
			<h2><span class="TXTmainLabel">Dati Versamento</span></h2>
	   	<div align="center" style="display: inline-block;vertical-align:top; width:60%" >
			<table align=center  cellpadding="0" cellspacing="0" class="editExtTable"
				style="background-color: #C0C0C0; width: 100%; margin-top: 5px; margin-bottom: 15px;">
				<tr>
					<td style="text-align: center;">
						<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
							
							<tr>
								<td class="TDmainLabel"><span class="TXTmainLabel">Anno Imposta</span></td>
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vers.getAnnoImposta()%></span></td>
								
								<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Fiscale</span></td>
								<td class="TDviewTextBox">
									<span class="TXTviewTextBox"><%=vers.getCfVersante()%></span>
								</td>
							
								<%if(!"0".equals(vers.getFlgReperibilita())){ %>
									<td><span class="TXTviewTextBox"> <%=vers.getDesReperibilita()%></span></td>
								<%} %>
							</tr>
							
								<%if("1".equals(vers.getFlgReperibilita())||"3".equals(vers.getFlgReperibilita())){
									AnagSoggIciDmDTO a =  vers.getDatiSogg();
								%>
								<tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Nominativo</span></td>
									<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=a.getDenominazione()!=null ? a.getDenominazione() : a.getCognome()+" "+a.getNome()%></span></td>
														
									<td class="TDmainLabel"><span class="TXTmainLabel">Comune Domicilio</span></td>
									<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=a.getComDomicilio()!=null ? a.getComDomicilio() : "-"%></span></td>
								</tr>
								<%} %>
							<tr>
								<td class="TDmainLabel"><span class="TXTmainLabel">Data Versamento</span></td>
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vers.getDtVersamento()!=null ? SDF.format(vers.getDtVersamento()) : "-"%></span></td>
								
								<td class="TDmainLabel"><span class="TXTmainLabel">Data Registrazione</span></td>
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vers.getDtRegistrazione()!=null ? SDF.format(vers.getDtRegistrazione()) : "-"%></span></td>
							</tr>
							
							<tr>
								<td class="TDmainLabel"><span class="TXTmainLabel">Comune Ubicazione Immobili</span></td>
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vers.getComuneImm()!=null ? vers.getComuneImm() : "-"%></span></td>
								
								<td class="TDmainLabel"><span class="TXTmainLabel">Num. Immobili</span></td>
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vers.getNumFabb()!=null ? vers.getNumFabb() : "-"%></span></td>
							</tr>
							<tr>
								<td class="TDmainLabel"><span class="TXTmainLabel">Acconto/Saldo</span></td>
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vers.getDesAccSaldo()%></span></td>
								
								<td class="TDmainLabel"><span class="TXTmainLabel">Ravvedimento</span></td>
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=vers.getDesRavvedimento()%></span></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			</div>
			<div align="center" style="display: inline-block;vertical-align: top;" >
			<table align=center cellpadding="0" cellspacing="0" class="editExtTable"
					style="background-color: #C0C0C0; width: 100%; margin-top: 5px; margin-bottom: 15px;">
					<tr>
						<td style="text-align: center;">
							<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
							
								<tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Importo Versato</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=vers.getImpVersato()!=null ? DF.format(vers.getImpVersato().doubleValue()): zero%></span></td>
								</tr><tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Importo Ab.Principale</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=vers.getImpAbitazPrincipale()!=null ? DF.format(vers.getImpAbitazPrincipale().doubleValue()): zero%></span></td>
								</tr><tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Importo Altri Fabbricati</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=vers.getImpAltriFabbricati()!=null ? DF.format(vers.getImpAltriFabbricati().doubleValue()): zero%></span></td>
								</tr><tr>
									<td class="TDmainLabel" ><span class="TXTmainLabel">Importo Aree Fabbricabili</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=vers.getImpAreeFabbricabili()!=null ? DF.format(vers.getImpAreeFabbricabili().doubleValue()): zero%></span></td>
								</tr><tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Importo Terreni Agricoli</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=vers.getImpTerrAgricoli()!=null ? DF.format(vers.getImpTerrAgricoli().doubleValue()): zero%></span></td>
								</tr><tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Importo Detrazione</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=vers.getImpDetrazione()!=null ? DF.format(vers.getImpDetrazione().doubleValue()): zero%></span></td>
								</tr>
							</table>
							</td>			
						</tr>
					</table>	
				</div>
		</div>
	<%} %>

	
				
	<!-- Sezione Violazione -->
	<%ViolazioneIciDmDTO viol = (ViolazioneIciDmDTO) sessione.getAttribute(VersIciDmLogic.VIOLAZIONE);%> 
	<%if(viol!=null){%>
	<div align="center" style="width:100%;" >
		<h2><span class="TXTmainLabel">Dati Violazione</span></h2>
   		<div align="center" style="display: inline-block;vertical-align:top; width:60%" >
		<table align=center  cellpadding="0" cellspacing="0" class="editExtTable"
			style="background-color: #C0C0C0; width: 100%; margin-top: 5px; margin-bottom: 15px;">
			<tr>
				<td style="text-align: center;">
					<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
						
						<tr>
							<td class="TDmainLabel"><span class="TXTmainLabel">Cod.Fiscale</span></td>
							<td class="TDviewTextBox">
								<span class="TXTviewTextBox"><%=viol.getCfVersante()%></span>
							</td>
						
							<%if(!"0".equals(viol.getFlgReperibilita())){ %>
								<td><span class="TXTviewTextBox"> <%=viol.getDesReperibilita()%></span></td>
							<%} %>
						</tr>	
							<%if("1".equals(viol.getFlgReperibilita())||"3".equals(viol.getFlgReperibilita())){
								AnagSoggIciDmDTO a =  viol.getDatiSogg();
							%>
							<tr>
								<td class="TDmainLabel"><span class="TXTmainLabel">Nominativo</span></td>
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=a.getDenominazione()!=null ? a.getDenominazione() : a.getCognome()+" "+a.getNome()%></span></td>
													
								<td class="TDmainLabel"><span class="TXTmainLabel">Comune Domicilio</span></td>
								<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=a.getComDomicilio()!=null ? a.getComDomicilio() : "-"%></span></td>
							</tr>
							<%} %>
						<tr>
							<td class="TDmainLabel"><span class="TXTmainLabel">Data Versamento</span></td>
							<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=viol.getDtVersamento()!=null ? SDF.format(viol.getDtVersamento()) : "-"%></span></td>
							
							<td class="TDmainLabel"><span class="TXTmainLabel">Data Registrazione</span></td>
							<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=viol.getDtRegistrazione()!=null ? SDF.format(viol.getDtRegistrazione()) : "-"%></span></td>
						</tr>
						
						<tr>
							<td class="TDmainLabel"><span class="TXTmainLabel">Comune Ubicazione Immobili</span></td>
							<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=viol.getComuneImm()!=null ? viol.getComuneImm() : "-"%></span></td>
						</tr>
						<tr>	
							<td class="TDmainLabel"><span class="TXTmainLabel">Num. Provv. Liquidazione</span></td>
							<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=viol.getNumProvLiq()!=null ? viol.getNumProvLiq() : "-"%></span></td>
							
							<td class="TDmainLabel"><span class="TXTmainLabel">Data Provv. Liquidazione</span></td>
							<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=viol.getDtProvLiq()!=null ? SDF.format(viol.getDtProvLiq()) : "-"%></span></td>
						</tr>
					</table>
				</td>	
				</tr>
					</table>		
			</div>
			<div align="center" style="display: inline-block;vertical-align: top; width:30%" >
				<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 95%; margin-top: 5px; margin-bottom: 15px;">
					<tr>
						<td style="text-align: center;">
							<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
								<tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Importo Versato</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=viol.getImpVersato()!=null ? DF.format(viol.getImpVersato().doubleValue()): zero%></span></td>
								</tr><tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Imposta</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=viol.getImpImposta()!=null ? DF.format(viol.getImpImposta().doubleValue()): zero%></span></td>
								</tr><tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Soprattassa</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=viol.getImpSoprattassa()!=null ? DF.format(viol.getImpSoprattassa().doubleValue()): zero%></span></td>
								</tr><tr>
									<td class="TDmainLabel" ><span class="TXTmainLabel">Pena Pecuniaria</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=viol.getImpPenaPecuniaria()!=null ? DF.format(viol.getImpPenaPecuniaria().doubleValue()): zero%></span></td>
								</tr><tr>
									<td class="TDmainLabel"><span class="TXTmainLabel">Importo Interessi</span></td>
									<td class="TDviewTextBox" style="text-align:right;"><span class="TXTviewTextBox">&euro; <%=viol.getImpInteressi()!=null ? DF.format(viol.getImpInteressi().doubleValue()): zero%></span></td>
								</tr>
							</table>
							</td>			
						</tr>
				</table>	
			</div>
	</div>
	<%} %>
		
	<!--  Campi HIDDEN -->
	
	<input type='hidden' name="OGGETTO_SEL" value="">
	
<% if(finder != null){%>
		<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
	<% }else{%>
		<input type='hidden' name="ACT_PAGE" value="">
	<% }%>		
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="130">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">	
</form>
<div id="wait" class="cursorWait" ></div>
		
</body>
</html>