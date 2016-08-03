<%@ page language="java" import=
	"java.text.SimpleDateFormat, java.text.DecimalFormat, java.util.*, java.math.*, java.io.File, java.util.Iterator,
	it.escsolution.escwebgis.versamenti.iciDM.logic.*, it.webred.ct.data.access.basic.versamenti.iciDM.dto.*, it.webred.ct.data.model.versamenti.iciDM.*,
	it.escsolution.escwebgis.versamenti.logic.*, it.escsolution.escwebgis.versamenti.bean.*, it.escsolution.escwebgis.tributiNew.bean.VersamentiNew,
	it.escsolution.escwebgis.f24.logic.*,it.webred.ct.data.access.basic.f24.dto.*,it.webred.ct.data.model.f24.*,
	it.escsolution.escwebgis.ruolo.logic.*,it.webred.ct.data.access.basic.ruolo.tarsu.dto.*, it.webred.ct.data.model.ruolo.tarsu.*, it.webred.ct.data.access.basic.versamenti.bp.dto.*"%>
<% 					 
	HttpSession sessione = request.getSession(true);

	DecimalFormat DF = new DecimalFormat("#0.00");
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");

	String zero = DF.format(BigDecimal.ZERO.doubleValue());
	
	VersFinder finder = null;

	if (sessione.getAttribute(VersamentiAllLogic.FINDER) != null) {
		finder = (VersFinder) sessione
				.getAttribute(VersamentiAllLogic.FINDER);
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

	function vaiVersamentoF24(id){
		var params = '?UC=122&ST=33&POPUP=true';
		params += "&OGGETTO_SEL=" + id;
		window.open('<%= request.getContextPath() %>/F24Versamenti' + params, 'Versamento F24','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=780,height=580');
	}

	function vaiAnnullamentoF24(id){
		var params = '?UC=123&ST=33&POPUP=true';
		params += "&OGGETTO_SEL=" + id;
		window.open('<%= request.getContextPath() %>/F24Annullamento' + params, 'Annullamento F24','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=780,height=580');
	}

	function vaiIciDm(id){
		var params = '?UC=130&ST=37&POPUP=true';
		params += "&OGGETTO_SEL=" + id;
		window.open('<%= request.getContextPath() %>/VersIciDm' + params, 'Versamento ICI','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=780,height=580');
	}

	function vaiRuoloTarsu(id){
		var params = '?UC=128&ST=39&POPUP=true';
		params += "&OGGETTO_SEL=" + id;
		window.open('<%= request.getContextPath() %>/RuoloTarsu' + params, 'Ruolo tarsu','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=780,height=580');
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

	function visualizza(id){
		  if (document.getElementById){
		    if(document.getElementById(id).style.display == 'none'){
		      document.getElementById(id).style.display = 'block';
		    }else{
		      document.getElementById(id).style.display = 'none';
		    }
		  }
		}

</script>

<body>

	<div align="center" class="extWinTDTitle">
		<span class="extWinTXTTitle"> &nbsp;<%=funzionalita%>:&nbsp;Dati
			Versamenti 
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
	
	
	<div align="center" style="width:100%;" >
		
		<!-- VERSAMENTI ICI GESTIONALE -->
		<% Vector<VersamentiNew>  vg = (Vector<VersamentiNew>) sessione.getAttribute("LISTA_VERSAMENTI_ICI_GESTIONALE");
		boolean cls = false;
		String oldxx = null;
		String tipo = "extWinTDData";
		if(vg!=null && vg.size()>0){%>
		<div><a href="#" onclick="visualizza('div_ici_ges'); return false"><h2><span class="TXTmainLabel">Versamenti ICI da Gestionale</span></h2></a></div>
	   	<div id="div_ici_ges" align="center" style="vertical-align:top; width:100%;display:none" >
		  <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		  
		  					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fiscale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Versamento</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Terr. Agricoli</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Aree Fabbricabili</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Ab. Principale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Fabb.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Detrazione</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Totale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Pag.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
					</tr>
		
					<% int contatore = 1;%>
					<% for(VersamentiNew versamenti : vg) {
				        String xx = versamenti.getYeaRif();
				        
				        if(oldxx!=null && !oldxx.equals(xx)){
				        	cls = !cls;
				        	oldxx = xx;
				        }else if(oldxx==null)
				        	oldxx = xx;
				        
				        tipo = cls ? "extWinTDDataAlter" : "extWinTDData";
					
					%>
						<tr id="r<%=contatore%>">
							<td class=<%=tipo%>>
								<span class="extWinTXTData"><%=versamenti.getCodFisc()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: center;">
								<span class="extWinTXTData"><%=versamenti.getYeaRif()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: center;">
								<span class="extWinTXTData"><%=versamenti.getDatPag()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=versamenti.getImpTerAgrEu()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=versamenti.getImpAreFabEu()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=versamenti.getImpAbiPriEu()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=versamenti.getImpAltFabEu()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: right;">
								<span class="extWinTXTData"&euro; ><%=versamenti.getImpDtrEu()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=versamenti.getImpPagEu()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: center;">
								<span class="extWinTXTData"><%=versamenti.getTipPag()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: center;">
								<span class="extWinTXTData"><%=versamenti.getProvenienza()%></span>
							</td>
						</tr>
						<%contatore++;
					}%>
				</table>
				</td>
			</tr>
		  </table>
		</div>
		<%}%>
	
		<!-- VERSAMENTI ICI DM -->
		<% List<VersamentoIciDmDTO>  iciVe = (List<VersamentoIciDmDTO>) sessione.getAttribute(VersIciDmLogic.LISTA_VERS_ICI_DM);
		if(iciVe!=null && iciVe.size()>0){%>
		<div><a href="#" onclick="visualizza('div_ici_dm'); return false"><h2><span class="TXTmainLabel">Versamenti ICI (D.M.)</span></h2></a></div>
	   	<div id="div_ici_dm" align="center" style="vertical-align:top; width:100%;display:none" >
		  	  <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		  
		  			<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fiscale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Versamento</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Terr. Agricoli</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Aree Fabbricabili</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Ab. Principale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Fabb.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Detrazione</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Totale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Num. Imm.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune Immobili</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Acc./Saldo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Ravv.</span></td>
					</tr>
		
					<% int contatore = 1;%>
					<% for(VersamentoIciDmDTO v : iciVe) {%>
						<tr id="r<%=contatore%>" onclick="vaiIciDm('<%="VER@"+v.getId()%>')" style="cursor: pointer;" onmouseout='chgtr(<%=contatore%>,0)'>
							<td class="extWinTDData">
								<span class="extWinTXTData"><%=v.getCfVersante()%></span>
								<%if(v.getDatiSogg()!=null && v.getDatiSogg().getDenominazione()!=null){%>
									<br/><span class="extWinTXTData"><%=v.getDatiSogg().getDenominazione()%></span>
								<%}else if(v.getDatiSogg()!=null && v.getDatiSogg().getCognome()!=null && v.getDatiSogg().getNome()!=null){%>
									<br/><span class="extWinTXTData"><%=v.getDatiSogg().getCognome()+" "+v.getDatiSogg().getNome()%></span>
								<%}%>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=v.getAnnoImposta()%></span>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=v.getDtVersamento()!=null ? SDF.format(v.getDtVersamento()) : ""%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpTerrAgricoli()!=null ? DF.format(v.getImpTerrAgricoli()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpAreeFabbricabili()!=null ?  DF.format(v.getImpAreeFabbricabili()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpAbitazPrincipale()!=null ?  DF.format(v.getImpAbitazPrincipale()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpAltriFabbricati()!=null ?  DF.format(v.getImpAltriFabbricati()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpDetrazione()!=null ?  DF.format(v.getImpDetrazione()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpVersato()!=null ?  DF.format(v.getImpVersato()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=v.getNumFabb()%></span>
							</td>
							<td class="extWinTDData" >
								<span class="extWinTXTData"><%=v.getComuneImm()%></span>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=v.getDesAccSaldo()%></span>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=v.getDesRavvedimento()%></span>
							</td>
						</tr>
						<%contatore++;
					}%>
				</table>
		</div>
		<%}%>
		
		
		
		<!-- VIOLAZIONI ICI DM-->
		<% List<ViolazioneIciDmDTO>  iciVi = (List<ViolazioneIciDmDTO>) sessione.getAttribute(VersIciDmLogic.LISTA_VIOL_ICI_DM);
		if(iciVi!=null && iciVi.size()>0){%>
		<div><a href="#" onclick="visualizza('div_ici_dm_vio'); return false"><h2><span class="TXTmainLabel">Violazioni ICI (D.M.)</span></h2></a></div>
	   	<div id="div_ici_dm_vio" align="center" style="vertical-align:top; width:100%;display:none" >
		   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		  
		  			<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fiscale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Versamento</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imposta</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Soprattassa</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Pena Pecuniaria</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Interessi</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp. Versato</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune Immobili</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Provv.Liquidazione</span></td>
					</tr>
		
					<% int contatore = 1;%>
					<% for(ViolazioneIciDmDTO v : iciVi) {%>
						<tr id="r<%=contatore%>" onclick="vaiIciDm('<%="VIO@"+v.getId()%>')" style="cursor: pointer;" onmouseout='chgtr(<%=contatore%>,0)'>
							<td class="extWinTDData">
								<span class="extWinTXTData"><%=v.getCfVersante()%></span>
								<%if(v.getDatiSogg()!=null && v.getDatiSogg().getDenominazione()!=null){%>
									<br/><span class="extWinTXTData"><%=v.getDatiSogg().getDenominazione()%></span>
								<%}else if(v.getDatiSogg()!=null && v.getDatiSogg().getCognome()!=null && v.getDatiSogg().getNome()!=null){%>
									<br/><span class="extWinTXTData"><%=v.getDatiSogg().getCognome()+" "+v.getDatiSogg().getNome()%></span>
								<%}%>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=v.getDtVersamento()!=null ? SDF.format(v.getDtVersamento()) : ""%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpImposta()!=null ? DF.format(v.getImpImposta()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpSoprattassa()!=null ?  DF.format(v.getImpSoprattassa()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpPenaPecuniaria()!=null ?  DF.format(v.getImpPenaPecuniaria()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpInteressi()!=null ?  DF.format(v.getImpInteressi()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=v.getImpVersato()!=null ?  DF.format(v.getImpVersato()) : zero%></span>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=v.getComuneImm()%></span>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=v.getNumProvLiq()!=null ? "n. "+v.getNumProvLiq() : ""%></span>
								<span class="extWinTXTData"><%=v.getDtProvLiq()!=null ? "del "+SDF.format(v.getDtProvLiq()) : ""%></span>
							</td>
						</tr>
						<%contatore++;
					}%>
				</table>
		</div>
		<%}%>
	
	
		<!-- VERSAMENTI F4 -->
		<% List<F24VersamentoDTO>  f24v = (List<F24VersamentoDTO> ) sessione.getAttribute(F24VersamentiLogic.LISTA_VERS_F24_CF);
		if(f24v!=null && f24v.size()>0){%>
		<div><a href="#" onclick="visualizza('div_f24_vers'); return false"><h2><span class="TXTmainLabel">Versamenti F24</span></h2></a></div>
	   	<div id="div_f24_vers" align="center" style="display: inline-block;vertical-align:top; width:100%;display:none" >
		  <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
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
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">Bonifico</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Riga</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CAB</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fisc.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fisc.</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Imposta</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tributo</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Rif.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Credito</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Debito</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Detrazione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Acconto</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Saldo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Ravv.</span></td>
			</tr>
		<%
			int contatore = 1;
			long progressivoRecord = (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() + 1; %>
		<%  cls = false;
			oldxx = null;
			tipo = "extWinTDData";
			for(F24VersamentoDTO v : f24v) {
				
		        String xx = v.getDtBonifico()+"_"+v.getProgDelega();
		        
		        if(oldxx!=null && !oldxx.equals(xx)){
		        	cls = !cls;
		        	oldxx = xx;
		        }else if(oldxx==null)
		        	oldxx = xx;
		        
		        tipo = cls ? "extWinTDDataAlter" : "extWinTDData";
		        
		        %>

	    		<tr id="r<%=contatore%>" onclick="vaiVersamentoF24('<%=v.getId()%>')" style="cursor: pointer;" onmouseout='chgtr(<%=contatore%>,0)'>
	    			
	    			<td class=<%=tipo%> style="text-align:center; " >
						<span class="extWinTXTData"><%=SDF.format(v.getDtFornitura())%></span>
					</td>
					
					<td class=<%=tipo%> >
						<span class="extWinTXTData"><%=v.getProgFornitura()%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=SDF.format(v.getDtRipartizione())%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getProgRipartizione()%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=SDF.format(v.getDtBonifico())%></span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getProgDelega()%></span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getProgRiga()%></span>
					</td>
					
	    			<!-- ENTE -->
	    			<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getCodEnteRd()%></span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData" title="<%=v.getDescTipoEnteRd()%>"><%=v.getTipoEnteRd()%></span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getCab()%></span>
					</td>
	    			
	    			
	    			<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getCf()%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span title="<%=v.getCf2()!=null ? (v.getCf2()+"-"+v.getDescTipoCf2()) : ""%>" class="extWinTXTData"><%=v.getCf2()!=null ? v.getCf2() : "-"%></span>
					</td>
	    			
	    			<!-- RISCOSSIONE -->
	    			<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=SDF.format(v.getDtRiscossione())%></span>
					</td>
					<td class=<%=tipo%> style="text-align: center;"  >
						<span class="extWinTXTData"><%=v.getDescTipoImposta()%></span>
					</td>
					<td class=<%=tipo%>>
						<span class="extWinTXTData"><%=v.getCodTributo()+" - "+v.getDescTipoTributo()%></span>
					</td>
	    			
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getAnnoRif()%></span>
					</td>
					
					<td style="text-align: right;" class=<%=tipo%> >
						<span class="extWinTXTData">&euro; <%=DF.format(v.getImpCredito().doubleValue())%></span>
					</td>
					
					<td style="text-align: right;" class=<%=tipo%> >
						<span class="extWinTXTData">&euro; <%=DF.format(v.getImpDebito())%></span>
					</td>
					
							
					<td style="text-align: right;" class=<%=tipo%> >
						<span class="extWinTXTData">&euro; <%=DF.format(v.getDetrazione())%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span><%if(v.getAcconto().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
						<td class=<%=tipo%> style="text-align:center;" >
						<span ><%if(v.getSaldo().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span ><%if(v.getRavvedimento().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
					
				</tr>
	
				<% 
				contatore++; progressivoRecord ++;
			  	} 
			  	%>
			</table>
		</div>
		<%} %>
		
		<!-- ANNULLAMENTI F24 -->
		<% List<F24AnnullamentoDTO>  f24a = (List<F24AnnullamentoDTO> ) sessione.getAttribute(F24AnnullamentoLogic.LISTA_ANN_F24_CF);
		if(f24a!=null && f24a.size()>0){%>
	   	<div><a href="#" onclick="visualizza('div_f24_ann'); return false"><h2><span class="TXTmainLabel">Annullamenti F24</span></h2></a></div>
	   	<div id="div_f24_ann" align="center" style="vertical-align:top; width:100%;display:none" >
		  <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		  
		  	<tr>
				<td class="extWinTDTitle" colspan="7"><span class="extWinTXTTitle">RIFERIMENTO</span></td>
				<td class="extWinTDTitle" colspan="7"><span class="extWinTXTTitle">OPERAZIONE</span></td>
			</tr>
		  	<tr>
		  		<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">Fornitura</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">Ripartizione Orig.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Bonifico Orig.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Versamento</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Operazione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Contribuente</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Imposta</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tributo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp.Credito</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Impo.Debito</span></td>
			</tr>
	
			<%
				int contatore = 1;
				long progressivoRecord = (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() + 1; %>
			<% for(F24AnnullamentoDTO v : f24a) { %>

	    		<tr id="r<%=contatore%>" onclick="vaiAnnullamentoF24('<%=v.getId()%>')" style="cursor: pointer;" onmouseout='chgtr(<%=contatore%>,0)'>
	    		    
	    		    <td class="extWinTDData" style="text-align:center;">	
	    				<span class="extWinTXTData"><%=v.getDtFornitura()!=null ? SDF.format(v.getDtFornitura()) : "-"%></span></td>
					
					<td class="extWinTDData" style="text-align:center;">	
	    				<span class="extWinTXTData"><%=v.getProgFornitura()%></span></td>
					
					<td class="extWinTDData" style="text-align:center;">	
						<span class="extWinTXTData"><%=v.getDtRipartizione()!=null ? SDF.format(v.getDtRipartizione()) : "-"%></span></td>
					
					<td class="extWinTDData" style="text-align:center;"><span class="extWinTXTData"><%=v.getProgRipartizione()%></span></td>
					<td class="extWinTDData" style="text-align:center;"><span class="extWinTXTData"><%=v.getDtBonifico()!=null ? SDF.format(v.getDtBonifico()) : "-"%></span></td>
				 
	    		    <td class="extWinTDData" style="text-align:center;">	
	    				<span class="extWinTXTData"><%=v.getAnnoRif()!=null ? v.getAnnoRif() : "-"%></span></td>
	    		     
	    		    <td class="extWinTDData" style="text-align:center;">	
	    				<span class="extWinTXTData"><%=v.getDtRiscossione()!=null ? SDF.format(v.getDtRiscossione()) : "-"%></span></td>

	    		    <td class="extWinTDData" >
						<span class="extWinTXTData"><%="A".equalsIgnoreCase(v.getTipoOperazione())? "Annullamento" : ("R".equalsIgnoreCase(v.getTipoOperazione()) ? "Ripristino" : v.getTipoOperazione())%></span>
					</td>
	    		
	    			<td class="extWinTDData" style="text-align:center;">	
	    			<span class="extWinTXTData"><%=v.getDtOperazione()!=null ? SDF.format(v.getDtOperazione()) : "-"%></span></td>
					
					<td class="extWinTDData" style="text-align:center;"><span class="extWinTXTData"><%=v.getCf()%></span></td>
					
					<td class="extWinTDData" style="text-align:center;"><span class="extWinTXTData"><%=v.getDescTipoImposta()%></span></td>
					
					<td class="extWinTDData"><span class="extWinTXTData"><%=v.getCodTributo()+" - "+v.getDescTipoTributo()%></span></td>
					
					<td class="extWinTDData" style="text-align:right;">	
	    				<span class="extWinTXTData">&euro; <%=v.getImpCredito()!=null ? DF.format(v.getImpCredito()) : zero%></span></td>
					
					<td class="extWinTDData" style="text-align:right;">	
	    				<span class="extWinTXTData">&euro; <%=v.getImpDebito()!=null ? DF.format(v.getImpDebito()) : zero%></span></td>
					

				</tr>
	
				<% 
				contatore++; progressivoRecord ++;
			  	} 
			  	%>
		  
		  
		  </table>
		</div>
		<%}%>
		
		<!-- VERSAMENTI TARSU BP -->
		<% List<RuoloTarsuDTO>  ruoli = (List<RuoloTarsuDTO>) sessione.getAttribute(RuoloTarsuLogic.LISTA_RUOLI_CF);
		 cls = false;
		 oldxx = null;
		 tipo = "extWinTDData";
		if(ruoli!=null && ruoli.size()>0){
			boolean esisteVersamento= false;
			int j = 0;
			while(j<ruoli.size() && !esisteVersamento){
				RuoloTarsuDTO r = ruoli.get(j);
				if(r.getListaVersBp()!=null && r.getListaVersBp().size()>0)
					esisteVersamento = true;
				j++;
			}
			
			if(esisteVersamento){
		%>
		<div><a href="#" onclick="visualizza('div_tar_bp'); return false"><h2><span class="TXTmainLabel">Versamenti TARSU (Bollettini Postali)</span></h2></a></div>
	   	<div id="div_tar_bp" align="center" style="vertical-align:top; width:100%;display:none" >
		  <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		  
		  			<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fiscale</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Accredito</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Accettazione</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">c.c. Beneficiario</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">V campo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Documento</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Importo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Ruolo</span></td>
					</tr>
		
					<% int contatore = 1;%>
					<% for(RuoloTarsuDTO r : ruoli) {
				        String idRuolo = r.getRuolo().getId();
				        
				        tipo = cls ? "extWinTDDataAlter" : "extWinTDData";
						for(VersamentoTarBpDTO bp : r.getListaVersBp()){
					%>
						<tr id="r<%=contatore%>">
							<td class=<%=tipo%>>
								<span class="extWinTXTData"><%=r.getRuolo().getCodfisc()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: center;">
								<span class="extWinTXTData"><%=bp.getDtAccredito()!=null ? SDF.format(bp.getDtAccredito()) : "-" %></span>
							</td>
							<td class=<%=tipo%> style="text-align: center;">
								<span class="extWinTXTData"><%=bp.getDtAccettazione()!=null ? SDF.format(bp.getDtAccettazione()) : "-"%></span>
							</td>
							<td class=<%=tipo%>>
								<span class="extWinTXTData"><%=bp.getCcBeneficiario()%></span>
							</td>
							<td class=<%=tipo%>>
								<span class="extWinTXTData"><%=bp.getNumBollettino()%></span>
							</td>
							<td class=<%=tipo%>>
								<span class="extWinTXTData"><%=bp.getDescTipoDoc()%></span>
							</td>
							<td class=<%=tipo%> style="text-align: right;">
								<span class="extWinTXTData">&euro; <%=bp.getImporto()!=null ? bp.getImporto() : zero%></span>
							</td>
							<td class=<%=tipo%> style="text-align: center;">
								<a href="#" onclick="javascript:vaiRuoloTarsu('<%=idRuolo%>');"><span class="extWinTXTData">Vai</span></a>
							</td>
						</tr>
						<%contatore++;
						}}%>
				</table>
		</div>
		<%}}%>
				
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
			<input type='hidden' name="UC" value="122">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">	
</form>
<div id="wait" class="cursorWait" ></div>
		
</body>
</html>