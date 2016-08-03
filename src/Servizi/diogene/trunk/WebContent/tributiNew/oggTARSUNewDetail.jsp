<%@page import="java.util.*"%>
<%@page import="java.lang.*"%>
<%@ page language="java" import="it.escsolution.escwebgis.docfa.bean.Docfa,
								 it.escsolution.escwebgis.tributi.bean.*,
								 it.escsolution.escwebgis.tributiNew.bean.*,
								 it.escsolution.escwebgis.tributiNew.logic.*,
								 it.escsolution.escwebgis.catasto.logic.CatastoPlanimetrieComma340Logic,
								 it.escsolution.escwebgis.common.EscServlet,
 								 it.escsolution.escwebgis.common.interfacce.*,
 								 it.escsolution.escwebgis.common.*,
 								 it.webred.cet.permission.CeTUser,
 								 it.escsolution.escwebgis.diagnostiche.util.DiaBridge"%>
 								 
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  
	HttpSession sessione = request.getSession(true);   
	OggettiTARSUNew tarsu=(OggettiTARSUNew)sessione.getAttribute("TARSU"); 
	String ST = (String)sessione.getAttribute("ST");
	OggettiTARSUNewFinder finder = null;

	if (sessione.getAttribute("FINDER109") != null)
		finder = (OggettiTARSUNewFinder)sessione.getAttribute("FINDER109"); 
		
	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

	Vector vctLink = null; 
	if (sessione.getAttribute("LISTA_INTERFACCE") != null)
		vctLink = ((Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
	<title>Tributi Oggetto TARSU - Dettaglio</title>
	<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
	<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
	<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

	function mettiST()
	{
		document.mainform.ST.value = 3;
	}
	
	function chgtr(row,flg) 
	{
		return;
		if (flg==1)
			document.all("r"+row).style.backgroundColor = "#d7d7d7";
		else
			document.all("r"+row).style.backgroundColor = "";
	}
	
	function visualizzaSoggettoAssociato(chiave){		
		wait();
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiContribuentiNew";	
		document.mainform.OGGETTO_SEL.value=chiave;	
		document.mainform.ST.value=5;
		document.mainform.UC.value=110;
		document.mainform.EXT.value = 1;
		document.mainform.submit();
	}
	
	function apriPopupCens(codice,im,f,m,s)
	{
		var url = '<%= request.getContextPath() %>/Docfa?popupCens=true&UC=43&codice='+codice+'&im='+im+'&f='+f+'&m='+m+'&s='+s;
		var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
		finestra.focus();
	}
	function dettaglioDocfa(chiave)
	{
		var pop = window.open('<%= request.getContextPath() %>/Docfa?ST=3&UC=43&OGGETTO_SEL='+chiave+'&EXT=1&NONAV=1');
		pop.focus();
	}

	function apriPopupTitolaritaF(codFiscale, nome, cognome, dataNascita)
	{

				var url = '<%= request.getContextPath() %>/CatastoIntestatariF?ST=5&UC=3&popup=true&COD_FISCALE='+codFiscale+'&NOME='+nome+'&COGNOME='+cognome+'&DATA_NASCITA='+dataNascita;
				var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
				finestra.focus();
	}

	function apriPopupTitolaritaG(partitaIva)
	{

				var url = '<%= request.getContextPath() %>/CatastoIntestatariG?ST=5&UC=4&popup=true&PARTITA_IVA='+partitaIva;
				var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
				finestra.focus();
	}
	function visDia(params) {
		params += '&<%=DiaBridge.SESSION_KEY%>=TARSU&popup=yes';
		window.open('<%= request.getContextPath() %>/DiagnosticheViewer' + params,'','toolbar=no,scrollbars=yes,resizable=yes,width=800,height=600');
	}
</script>

<body>

<div align="center" class="extWinTDTitle">
		<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dati Oggetto TARSU</span>
	</div>

	&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
	<br/>
<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

<form name="mainform" action="<%= EscServlet.URL_PATH %>/TributiOggettiTARSUNew" target="_parent">
<% 
	EnvUtente eu = new EnvUtente((CeTUser)sessione.getAttribute("user"), null, null); 
	String ente = eu.getEnte();
	String name = eu.getUtente().getName();
%>

<div style="margin-top: 15px;">
	<span class="TXTmainLabel">
		<%=DiaBridge.getDiaHtmlTestata(ente, name, tarsu, request.getContextPath())%>
	</span>
</div>
	
	
	<table style="background-color: white; width: 100%;">
	<tr style="background-color: white;">
	<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
	
	<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">		
		<tr></tr>
		<tr></tr>
		<tr></tr>
		<tr>
			<td>
			<table class="viewExtTable" >				
				<tr>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Comune</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getComune()%></span></td>
					<td width=10></td>	
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Foglio</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getFoglio()%></span></td>
			
				</tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Particella</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getNumero()%></span></td>
					<td width=10></td>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Subalterno</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getSub()%></span></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr></tr>
		<tr></tr>
		<tr></tr>
	</table>
		
	&nbsp;
		
	<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
		<tr></tr>
		<tr></tr>
		<tr></tr>
		<tr>
			
			<td>
			<table class="viewExtTable" >
				<tr>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Classe</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getDesClsRsu()%></span></td>
					<td width=10></td>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Tipo Oggetto</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getDesTipOgg()%></span></td>
				</tr>
			</table>
			</td>
			
		</tr>
		
		<tr></tr>
		<tr></tr>
		<tr></tr>
		
		<tr>
			<td>
			<table class="viewExtTable" >
				<tr>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Data Inizio Possesso</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getDatIni()%></span></td>
					<td width=10></td>		
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Data Fine Possesso</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getDatFin()%></span></td>
				</tr>
			</table>
			</td>
		</tr>
		
		<tr></tr>
		<tr></tr>
		<tr>
			<td>
			<table class="viewExtTable" >
				<tr>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Indirizzo</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getDesInd()%></span></td>
					<td width=10></td>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Scala</span></td>
					<td class="TDviewTextBox" style="width:170;"><%=tarsu.getScala()%><span class="TXTviewTextBox"></span></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr></tr>
		<tr></tr>
		<tr>
			<td>
			<table class="viewExtTable" >
				<tr>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Piano</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getPiano()%></span></td>
					<td width=10></td>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Interno</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getInterno()%></span></td>
				</tr>
			</table>
			</td>	
		</tr>
		<tr></tr>
		<tr></tr>
		<tr></tr>
		<tr>
			<td>
			<table class="viewExtTable" >
				<tr>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Superficie dichiarata</span></td>
					<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=tarsu.getSupTot()%></span></td>
					<td width=10></td>
					<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Mappa</span></td>
					<td onclick="zoomInMappaParticelle('<%= tarsu.getCodEnte() %>','<%=tarsu.getFoglio()%>','<%=tarsu.getNumero()%>');" class="TDviewTextBox" style="width:55;">
						<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif" border="0" style="cursor:pointer;"/></span>
					</td>
					<td class="TDviewTextBox" style="width:55;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupVirtualH(<%=tarsu.getLatitudine()==null?0:tarsu.getLatitudine()%>,<%=tarsu.getLongitudine()==null?0:tarsu.getLongitudine()%>);">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
							</a>
						</span>
					</td>
					<td class="TDviewTextBox" style="width:52;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupStreetview(<%=tarsu.getLatitudine()==null?0:tarsu.getLatitudine()%>,<%=tarsu.getLongitudine()==null?0:tarsu.getLongitudine()%>);">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
							</a>
						</span>
					</td>
				</tr>
			</table>
			</td>	
		</tr>
		<tr></tr>
		<tr></tr>
		<tr></tr>
				
	</table>
	
	<!-- FINE Solo Dettaglio -->
	
	</td>
	</tr>
	</table>
	
	<div class="tabber">
	
	
	<!-- INIZIO Elenco SOGGETTI associati -->
	<% 
	ArrayList soggAssociati=(ArrayList)sessione.getAttribute("SOGGETTI_ASSOCIATI_TARSU");
	if (soggAssociati != null && soggAssociati.size() > 0){%>
	    <div class="tabbertab">
		<h2>Soggetti associati</h2>
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		 <tr>&nbsp;</tr>
		<tr class="extWinTXTTitle">
				Soggetti associati
		</tr>
		<tr>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">TITOLO</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.SOGGETTO</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">DENOMINAZIONE</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.FISCALE</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">PART.IVA</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">TITOLARITA' CATASTO</span></td>
		</tr>
	  <%Iterator it = soggAssociati.iterator();
		int contatore=0; 
		while(it.hasNext()){ 
			SoggettiTributiNew sogg = (SoggettiTributiNew)it.next();
			String cognome = sogg.getCogDenom();
			String nome = sogg.getNome();
			String denominazione = sogg.getCogDenom();
			if(nome == null || nome.equals("")){
				cognome = "-";
				nome = "-";
			}else
				denominazione = "-";	
			
			String pIVA = sogg.getPartIva();
			try {
				if (Long.parseLong(pIVA)== 0){
					pIVA = "-";
				}
			} catch (Exception e) {
				pIVA = "-";
			}
			contatore++;
			%>		
			<tr id="r<%=contatore%>" >
				<td class="extWinTDData" onclick="visualizzaSoggettoAssociato('<%=sogg.getChiave()%>')" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
				<span class="extWinTXTData"><%=sogg.getTitolo() == null ? "-" : sogg.getTitolo()%></span></td>
				<td class="extWinTDData" onclick="visualizzaSoggettoAssociato('<%=sogg.getChiave()%>')" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor:pointer;'>
				<span class="extWinTXTData"><%=sogg.getIdOrig() == null ? "-" : sogg.getIdOrig()%></span></td>
				<td class="extWinTDData" onclick="visualizzaSoggettoAssociato('<%=sogg.getChiave()%>')" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
				<span class="extWinTXTData"><%=cognome%></span></td>	
				<td class="extWinTDData" onclick="visualizzaSoggettoAssociato('<%=sogg.getChiave()%>')" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
				<span class="extWinTXTData"><%=nome%></span></td>		
				<td class="extWinTDData" onclick="visualizzaSoggettoAssociato('<%=sogg.getChiave()%>')" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
				<span class="extWinTXTData"><%=denominazione%></span></td>
				<td class="extWinTDData" onclick="visualizzaSoggettoAssociato('<%=sogg.getChiave()%>')" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
				<span class="extWinTXTData"><%=sogg.getCodFisc() == null ? "-" : sogg.getCodFisc()%></span></td>
				<td class="extWinTDData" onclick="visualizzaSoggettoAssociato('<%=sogg.getChiave()%>')" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
				<span class="extWinTXTData"><%=pIVA%></span></td>
				<td class="extWinTDData" onclick="visualizzaSoggettoAssociato('<%=sogg.getChiave()%>')" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
				<span class="extWinTXTData"><center><%=sogg.getProvenienza() == null ? "-" : sogg.getProvenienza()%></center></span></td>
				<td class="extWinTDData" >
		<span class="extWinTXTData">
			<% if (pIVA == null || pIVA.equals("") || pIVA.equals("-")){%>
			<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaF('<%=sogg.getCodFisc()%>','<%=nome.replace("'", "\\'")%>','<%=cognome.replace("'", "\\'")%>','<%=sogg.getDtNsc()%>');">
				Vedi
			</a>
			<%} else { %>
			<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaG('<%=pIVA%>');">
				Vedi
			</a>
			<%} %>
			</span>
		</td>
			</tr>
		<%}%>
	</table>
	</div>
	<%}%>
	
	
	<% 
	ArrayList lvani = (ArrayList)sessione.getAttribute("TARSU_DETTAGLIO_VANI");
	
	if (lvani != null && lvani.size() > 0) { 
	%>
			<div class="tabbertab">
			<h2>Dettaglio superfici per vano (scarico comma 340)</h2>
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				<tr>&nbsp;</tr>
				<tr class="extWinTXTTitle">
						Dettaglio superfici per vano (scarico comma 340)
				</tr>
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Vano</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Piano</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Edificio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Ambiente</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Altezza Min.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Altezza Max.</span></td>
				</tr>
				<% 
				Iterator it = lvani.iterator();			
				while(it.hasNext())
				{
					OggettiTARSU vani = (OggettiTARSU)it.next();
				%>
				<tr>
					<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getVano()%></span></td>
					<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getPiano()%></span></td>
					<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getEdificio()%></span></td>
					<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getAmbiente()%></span></td>
					<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getSupVani()%></span></td>
					<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getAltezzaMin()%></span></td>			
					<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getAltezzaMax()%></span></td>
				</tr>
				<%}%>
				<tr>
					<td class="extWinTDData" colspan="4"><span class="TXTmainLabel">Sup. TARSU da catasto ( (A + B + C) * 0,80 )</span></td>
					<td class="extWinTDData" colspan="3"><span class="extWinTXTData"><%=tarsu.getSupCatasto()%></span></td>
				</tr>
			</table>	
			</div>
	<% } %>
	
	<!-- FINE Dettaglio Superfici per Vano -->
	
	<!-- INIZIO Planimetrie Comma 340 -->	
	
	<% 
		ArrayList planimetrieComma340 = (ArrayList)sessione.getAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_TARSU);
	 if (planimetrieComma340 != null && planimetrieComma340.size() > 0) { 
	%>
		<div class="tabbertab">
		 <h2>Planimetrie comma 340</h2>
		 
		 <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		 
		 <tr>&nbsp;</tr>
		<tr class="extWinTXTTitle">
				Planimetrie comma 340
		</tr>
		 
	<%	
			Iterator it = planimetrieComma340.iterator();
			boolean sub = false;
			boolean sub9999 = false;
			while(it.hasNext())
			{
				ArrayList link = (ArrayList)it.next();
			
			  	if (!((String)link.get(2)).equalsIgnoreCase("9999")) {
					if (!sub) {
						sub = true;%>
						<tr>
							<td class="extWinTDTitle" ><span class="extWinTXTTitle">Planimetrie catastali associate alla UIU</span></td>
						</tr>
				<%	} 
				} else {
					if (!sub9999) {
						sub9999 = true;%>
						<tr>
							<td class="extWinTDTitle" ><span class="extWinTXTTitle">Planimetrie catastali associate al corpo di fabbrica</span></td>
						</tr>
				<%
					}
				}
				%>
					<tr>
						<td class="extWinTDData" >
							<span class="extWinTXTData"><a style="font-size: 12px;" 
							href="<%= EscServlet.URL_PATH %>/CatastoPlanimetrieComma340?pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>">
								<%=(String)link.get(0)%>
							</a></span>
						</td>
					</tr>
			<%}%>
		</table>
		</div>
	<% } %>	
	
	<!-- Fine Planimetrie Comma 340 -->		
	
	
	<!-- FINE Elenco SOGGETTI associati -->
	
	
	<%ArrayList ldocfa = (ArrayList)sessione.getAttribute(TributiOggettiTARSUNewLogic.TARSU_DOCFA_COLLEGATI);
	if (ldocfa != null && ldocfa.size() > 0) { %>
	
		<div class="tabbertab">
			<h2>Dati planimetrici DOCFA collegati</h2>
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0" >
				 <tr>&nbsp;</tr>
				<tr class="extWinTXTTitle">
						Dati planimetrici DOCFA collegati
				</tr>
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Fornitura</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Ident. Imm.</span></td>
				</tr>
				<% 
				Iterator it = ldocfa.iterator();			
				while(it.hasNext())
				{
					Docfa docfa = (Docfa)it.next();
				%>
				<tr>
					<td class="extWinTDData" ><span class="extWinTXTData"><a href="javascript:dettaglioDocfa('<%=docfa.getChiave()%>');"><%=docfa.getProtocollo()%></a></span></td>
					<td class="extWinTDData" ><span class="extWinTXTData"><%=docfa.getFornitura()%></span></td>
					<td class="extWinTDData" ><span class="extWinTXTData"><a href="javascript:apriPopupCens('<%=docfa.getChiave()%>','<%=docfa.getIdentificativoImm()%>','<%=docfa.getFoglio()%>','<%=docfa.getParticella()%>','<%=docfa.getSubalterno()%>');"><%=docfa.getIdentificativoImm()%></a></span></td>
				</tr>
				<%}%>
			</table>
		</div>	
	<% }%>
	
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
			<input type='hidden' name="UC" value="109">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">	
</form>

	<div id="wait" class="cursorWait" ></div>
</body>
</html>