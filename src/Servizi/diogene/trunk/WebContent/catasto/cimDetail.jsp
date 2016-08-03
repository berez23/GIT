<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page language="java" import="it.escsolution.escwebgis.catasto.bean.*,
								 it.escsolution.escwebgis.catasto.logic.*,
								 it.escsolution.escwebgis.toponomastica.bean.*,
 								 it.escsolution.escwebgis.common.interfacce.*,
 								 it.escsolution.escwebgis.common.*,
 								 it.webred.cet.permission.CeTUser, java.util.*,
 								 it.escsolution.escwebgis.diagnostiche.util.DiaBridge,
 								 it.escsolution.escwebgis.docfa.bean.Docfa,
 								 it.escsolution.escwebgis.tributi.bean.OggettiTARSU, 
 								 it.escsolution.escwebgis.common.EscServlet,
 								 it.escsolution.escwebgis.pregeo.bean.PregeoInfo,
 								 it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO,
 								 java.text.SimpleDateFormat"%>

<%  HttpSession sessione = request.getSession(true); 
	Immobile imm = null; 
	ArrayList<Civico> listaCiviciPerUnimm = null;
	ArrayList<Civico> listaCiviciCatPerUnimm = null;
	ArrayList<Immobile> listaStoricoUnimm = null;
	
	// nascondi è un parametro messo in sessione da AuthServlet al fine di non mostrare i link Mappa, 3D, Street View, Altre Mappe e Indice di correlazione alle applicazioni esterne al GIT 
	java.lang.String nascondi = (java.lang.String)sessione.getAttribute("nascondi");
	
	imm = (Immobile) sessione.getAttribute(CatastoImmobiliLogic.UNIMM); 
	listaCiviciPerUnimm = (ArrayList<Civico>) sessione.getAttribute(CatastoImmobiliLogic.LISTA_CIVICI);
	listaCiviciCatPerUnimm = (ArrayList<Civico>) sessione.getAttribute(CatastoImmobiliLogic.LISTA_CIVICI_CAT);
	listaStoricoUnimm=(ArrayList<Immobile>) sessione.getAttribute(CatastoImmobiliLogic.LISTA_UIU_STORICO);
	
	ImmobiliFinder finder = null;
	Vector<IntestatarioF> vlistaIntestatariF=null; 
	Vector<IntestatarioG> vlistaIntestatariG=null;

	if ((it.escsolution.escwebgis.catasto.bean.ImmobiliFinder)sessione.getAttribute("FINDER1") != null){
		 finder = (it.escsolution.escwebgis.catasto.bean.ImmobiliFinder)sessione.getAttribute("FINDER1"); 
	}

	 if((Vector)sessione.getAttribute("LISTA_INTESTATARIF2") != null){
		 vlistaIntestatariF=(Vector<IntestatarioF>)sessione.getAttribute("LISTA_INTESTATARIF2"); 
		 vlistaIntestatariG=(Vector<IntestatarioG>)sessione.getAttribute("LISTA_INTESTATARIG2"); 
	 }
	 else{
		 vlistaIntestatariF=(Vector<IntestatarioF>)sessione.getAttribute("LISTA_INTESTATARIF"); 
		 vlistaIntestatariG=(Vector<IntestatarioG>)sessione.getAttribute("LISTA_INTESTATARIG"); 
	}

	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

	Vector vctLink = null; 
	if (sessione.getAttribute("LISTA_INTERFACCE") != null)
		vctLink = ((Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
	
 	String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); 
%>
<html>
<head>
<title>Catasto Immobili UNIMM - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>
function vaiIntestatarioG(intestatario,record_cliccato){
	wait();
	document.mainform.action="<%= request.getContextPath() %>/CatastoIntestatariG";
	document.mainform.OGGETTO_SEL.value=intestatario;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.UC.value = 4;
	document.mainform.EXT.value=1;
	document.mainform.submit();
}
function vaiIntestatarioF(intestatario,record_cliccato){
wait();
	document.mainform.action="<%= request.getContextPath() %>/CatastoIntestatariF";
	document.mainform.OGGETTO_SEL.value=intestatario;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.UC.value = 3;
	document.mainform.EXT.value=1;
	document.mainform.submit();
}
function dettaglioDocfa(chiave) {
	var pop = window.open('<%= request.getContextPath() %>/Docfa?ST=3&UC=43&OGGETTO_SEL='+chiave+'&EXT=1&NONAV=1');
	pop.focus();
}
</script>
<script>
function mettiST(){
	//alert("mettiST");
	document.mainform.ST.value = 3;
}

function vaiDettaglioDerivata(codice, ideMutaIni)
{
	try
	{
		document.mainform.OGGETTO_SEL.value = codice;
		document.mainform.IDE_MUTA_INI=ideMutaIni;
		
		
			wait();
			document.mainform.ST.value = 4;
			document.mainform.target = "_parent";
			document.mainform.submit();
		
	}
	catch (e)
	{
		//alert(e);
	}
}

function vai(codice, record_cliccato, isPopUp)
{
	try
	{
		document.mainform.OGGETTO_SEL.value = codice;
		document.mainform.RECORD_SEL.value = record_cliccato;
		if (isPopUp)
		{
			targ = apriPopUp(record_cliccato);
			
			if (targ)
			{
				document.mainform.ST.value = 33;
				document.mainform.target = targ;
				document.mainform.submit();
				document.mainform.ST.value = 2;
				document.mainform.target = "_parent";
			}
		}
		else
		{
			wait();
			document.mainform.ST.value = 3;
			document.mainform.target = "_parent";
			document.mainform.submit();
		}
	}
	catch (e)
	{
		//alert(e);
	}
}

function visDia(params) {
	params += '&<%=DiaBridge.SESSION_KEY%>=<%=CatastoImmobiliLogic.UNIMM%>&popup=yes';
	window.open('<%= request.getContextPath() %>/DiagnosticheViewer' + params,'','toolbar=no,scrollbars=yes,resizable=yes,width=800,height=600');
}

</script>
<body >


<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Subalterni Catasto Urbano</span>
</div>

&nbsp;

<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>

<% if (nascondi != null && nascondi.equalsIgnoreCase("1")){}else{%>
<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
<%} %>
	&nbsp;
	

<form name="mainform" action="<%= request.getContextPath() %>/CatastoImmobili" target="_parent">

<% 
	EnvUtente eu = new EnvUtente((CeTUser)sessione.getAttribute("user"), null, null); 
	String ente = eu.getEnte();
	String name = eu.getUtente().getName();
%>

<% if (nascondi != null && nascondi.equalsIgnoreCase("1")){}else{%>
<div style="margin-top: 15px;">
	<span class="TXTmainLabel">
		<%=DiaBridge.getDiaHtmlTestata(ente, name, imm, request.getContextPath())%>
	</span>
</div>
<%} %>

<%if (imm != null && imm.getChiaveGraffato() != null &&
	!imm.getChiaveGraffato().trim().equals("") && !imm.getChiaveGraffato().trim().equals("null")) {%>
	<table class="viewExtTable" style="background-color: white; width: 100%;" cellpadding="0" cellspacing="0">
		<tr>
			<td class="TXTviewTextBox" style="vertical-align: middle;">
				<img src="../images/clip.gif" border="0" />
			</td>
			<td class="TXTviewTextBox" style="width: 100%; vertical-align: middle; padding-left: 10px;">				
				<%
					String key = imm.getChiaveGraffato();
					String[] keyFields = key.split("\\|",-1);
					String foglio = keyFields[1];
					String particella = keyFields[2];
					String unimm = keyFields[4];
				%>
				L'Unità Immobiliare Foglio <%= foglio %>, Particella <%= particella %>, Subalterno <%= unimm != null && !unimm.equals(" ") ? unimm : "-" %>, è graffata; è visualizzato il dettaglio dell'Unità principale
			</td>
		</tr>
	</table>
<% } %>

<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: center;">

	<table width="100%" cellpadding="0" cellspacing="0">
	
	<tr class="extWinTXTTitle">Dati Identificativi</tr>
	
	<tr>
		<td style="width: 80%; vertical-align: top;">
			<table width="100%" cellpadding="0" cellspacing="0" class="editExtTable">
				<tr>
					<td>
					<table class="viewExtTable" >
						<tr>
							<td class="TDmainLabel"  style="width:85;"><span class="TXTmainLabel">Sezione</span></td>
							<td class="TDviewTextBox" style="width:85;"><span class="TXTviewTextBox"><%=imm.getSezione()%></span></td>
						</tr>
					</table>
					</td>
					<td>
					<table class="viewExtTable" >
						<tr>
							<td class="TDmainLabel"  style="width:85;"><span class="TXTmainLabel">Foglio</span></td>
							<td class="TDviewTextBox" style="width:85;"><span class="TXTviewTextBox"><%=imm.getFoglio()%></span></td>
						</tr>
					</table>
					</td>
					
					<td>	
					<table class="viewExtTable" >
						<tr>			
							<td class="TDmainLabel"  style="width:85;"><span class="TXTmainLabel">Particella</span></td>
							<td class="TDviewTextBox" style="width:85;"><span class="TXTviewTextBox"><%=imm.getNumero()%></span></td>
						</tr>
					</table>
					</td>
					
					<td>		
					<table class="viewExtTable" >
						<tr>	
							<td class="TDmainLabel"  style="width:85;"><span class="TXTmainLabel">Subalterno</span></td>
							<td class="TDviewTextBox" style="width:85;"><span class="TXTviewTextBox"><%//=imm.getSubalterno() %><%=imm.getUnimm()%></span></td>
						</tr>			
					</table>
					</td>	
				</tr>
			</table>
		</td>
			
		<td style="width: 20%; padding-left: 15px;">
		<% if (nascondi != null && nascondi.equalsIgnoreCase("1")){}else{%>
			<table class="viewExtTable" >
				<tr>	
					<td class="extWinTDData" style="border-style: none; cursor: pointer; text-align: center; width: 35px;" onclick="zoomInMappaParticelle('<%= imm.getComune() %>','<%=imm.getFoglio()%>','<%=imm.getNumero()%>');">
						<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
					</td>
					<td class="extWinTDData" style="text-align: center; width: 35px; border-style: none;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupVirtualH(<%=imm.getLatitudine()==null?0:imm.getLatitudine()%>,<%=imm.getLongitudine()==null?0:imm.getLongitudine()%>);">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
							</a>
						</span>
					</td>
					<td class="extWinTDData" style="text-align: center; width: 35px; border-style: none;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupStreetview(<%=imm.getLatitudine()==null?0:imm.getLatitudine()%>,<%=imm.getLongitudine()==null?0:imm.getLongitudine()%>);">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
							</a>
						</span>
					</td>
				</tr>			
			</table>
			<%} %>
		</td>
	</tr>		
	
	
	</table>

	<% ArrayList<Immobile> listaGraffati =(ArrayList<Immobile>)sessione.getAttribute(CatastoImmobiliLogic.LISTA_UIU_GRAFFATI); %>

	<%if (listaGraffati != null && listaGraffati.size() > 0 && imm.isPrincGraffati()) {%>
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr class="extWinTXTTitle">
					Unità immobiliari graffate
			</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>		
			</tr>
			<% Iterator it = listaGraffati.iterator();
				while(it.hasNext()) {			
					Immobile immobileGraffato = (it.escsolution.escwebgis.catasto.bean.Immobile)it.next();
       		%>
    		<tr>
    			<td class="extWinTDData">
					<span class="extWinTXTData"><%=immobileGraffato.getSezione()%></span></td>
				<td class="extWinTDData">
					<span class="extWinTXTData"><%=immobileGraffato.getFoglio()%></span></td>
				<td class="extWinTDData">
					<span class="extWinTXTData"><%=immobileGraffato.getParticella()%></span></td>
				<td class="extWinTDData">
					<span class="extWinTXTData"><%=immobileGraffato.getUnimm()%></span></td>
			</tr>	
		<% } %>
		</table>
	<% } %>
	</td>

</tr>
</table>
	
		
		
	<div class="tabber">
	
	<% 
	 if (listaStoricoUnimm != null && listaStoricoUnimm.size() > 0) { 	
	%>
	<div class="tabbertab">
	<h2>Variaz. catastali del sub.</h2>
	
	<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">
			Variazioni catastali amministrative del subalterno
	</tr>
	<tr>
		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Inizio Val.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cat.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cons.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sup.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Piano</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Annotazioni</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Zona</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Microzona</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Classamento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Atto</span></td>
	</tr>
			
	<% for(Immobile immobileStorico : listaStoricoUnimm){ %>

    <tr>
		<td  class="extWinTDData"  >
		<span class="extWinTXTData"><%=immobileStorico.getDataInizioVal()%></span></td>
		<td  class="extWinTDData"  >
		<span class="extWinTXTData"><%=immobileStorico.getDataFineVal()%></span></td>
		<td  class="extWinTDData" >
		<span class="extWinTXTData"><%=immobileStorico.getCodCategoria()%></span></td>	
		<td  class="extWinTDData" >
		<span class="extWinTXTData"><%=immobileStorico.getClasse()%></span></td>	
		<td  class="extWinTDData"  >
		<span class="extWinTXTData"><%=immobileStorico.getVani()%></span></td>
		<td  class="extWinTDData"  >
		<span class="extWinTXTData"><%=immobileStorico.getRendita()%></span></td>
		<td  class="extWinTDData"  >
		<span class="extWinTXTData"><%=immobileStorico.getSuperficie()%></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%=immobileStorico.getPiano()%></span></td>
		<td  class="extWinTDData"  >
		<span class="extWinTXTData"><%=immobileStorico.getPartita()%></span></td>
		<td  class="extWinTDData"  >
		<span class="extWinTXTData"><%=immobileStorico.getAnnotazione()%></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%=immobileStorico.getSezione()%></span></td>
		<td  class="extWinTDData"  >
		<span class="extWinTXTData"><%=immobileStorico.getZona()%></span></td>
		<td  class="extWinTDData"  >
		<span class="extWinTXTData"><%=immobileStorico.getMicrozona()%></span></td>
		<td  class="extWinTDData"  >
		<span class="extWinTXTData" title="<%=immobileStorico.getDescClassamento()%>"><%=immobileStorico.getCodClassamento()!=null ? immobileStorico.getCodClassamento() : "-"%>
		</span></td>
		<td  class="extWinTDData"  ><span class="extWinTXTData">
		<%if(!immobileStorico.getProtNotifica().equals("-") ||!immobileStorico.getDataNotifica().equals("-") ||
		     !immobileStorico.getCodAttoIni().equals("-") || !immobileStorico.getDescAttoIni().equals("-") ||
		     !immobileStorico.getCodAttoFine().equals("-") || !immobileStorico.getDescAttoFine().equals("-") ) { %>
		<a href="#" class="tooltip">
		    Visualizza dettagli
		    <span class="extTooltip">
		    <table  class="extWinTable" width="100%"  cellpadding="0" cellspacing="0">
			    <tr></tr>
			    <tr>
			    	<td class="extWinTDTitle" rowspan="2"><span class="extWinTXTTitle">NOTIFICA</span></td>
			    	<td class="extWinTDTitle" ><span class="extWinTXTTitle">Protocollo</span></td>
			        <td class="extWinTDTitle" ><span class="extWinTXTTitle">Data</span></td>
			    </tr>
			    <tr>
			    	<td class="extWinTDData"><span class="extWinTXTData"><%=immobileStorico.getProtNotifica()%></span></td>
			    	<td class="extWinTDData"><span class="extWinTXTData"><%=immobileStorico.getDataNotifica()%></span></td>
			    </tr>
			  
			   <tr>
			   		<td class="extWinTDTitle" rowspan="2"> <span class="extWinTXTTitle">ATTO GENERANTE</span> </td>
			   		<td class="extWinTDTitle" ><span class="extWinTXTTitle">Cod.Causale</span></td>
			        <td class="extWinTDTitle" ><span class="extWinTXTTitle">Descrizione</span></td>
			    </tr>
			   <tr>
			   		<td class="extWinTDData"><span class="extWinTXTData"><%=immobileStorico.getCodAttoIni()%></span></td>
			   		<td class="extWinTDData"> <span class="extWinTXTData"><%=immobileStorico.getDescAttoIni()%></span></td>
			   		
			   </tr>
			   <tr>
			   		<td class="extWinTDTitle" rowspan="2"><span class="extWinTXTTitle">ATTO CONCLUSIVO</span></td>
			   		<td class="extWinTDTitle" ><span class="extWinTXTTitle">Cod.Causale</span></td>
			        <td class="extWinTDTitle" ><span class="extWinTXTTitle">Descrizione</span></td>
			    </tr>
			   <tr>
			   		<td class="extWinTDData"> <span class="extWinTXTData"><%=immobileStorico.getCodAttoFine()%></span></td>
			   		<td class="extWinTDData">  <span class="extWinTXTData"><%=immobileStorico.getDescAttoFine()%></span></td></tr>
		    </table>
		    </span>
		</a>
		<%} %>
	</span></td>
	</tr>
	
	<%  }%>
	
	
	</table>
	</div>	
	<%  }%>
		
	<% if(vlistaIntestatariF != null && vlistaIntestatariF.size()>0){ %>
	<!-- INIZIO INTESTATARI -->
	<div class="tabbertab">
	<h2>Intest. Per. Fisiche</h2>
	<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">
			Intestatari Persone Fisiche associati alla particella
	</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Inizio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Quota</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Q. Num.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Q. Denom.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Affidab.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Regime</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sogg. collegato</span></td>
	</tr>
		<%it.escsolution.escwebgis.catasto.bean.IntestatarioF intF = new it.escsolution.escwebgis.catasto.bean.IntestatarioF(); %>
  			
  		<%java.util.Enumeration en = vlistaIntestatariF.elements(); %>
		<% long progressivoRecord = 1;int contatore=1;%>
  		<%while (en.hasMoreElements()) {%>
		  	<% intF = (it.escsolution.escwebgis.catasto.bean.IntestatarioF)en.nextElement();
		  	   java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
			   df.setLenient(false);
				
				java.sql.Date d_i = new java.sql.Date(df.parse(intF.getDataInizio()).getTime());
				java.sql.Date d_f = new java.sql.Date(df.parse(intF.getDataFine()).getTime());
				
				String color = "";
				if(d_i.after(d_f))
					color = "color:red;";
				else if(intF.getDataFine().equals("31/12/9999"))
					color = "color: #008000;font-weight: bold;";	
				
				//#538 - Non visualizzare record con data_inizio=data_fine
				if(!d_i.equals(d_f)){ %>
		  		
				<tr id="r<%=contatore%>" onclick="vaiIntestatarioF('<%=intF.getCodIntestatario()%>', '<%=progressivoRecord%>')">
		
		        <td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getCognome()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getNome()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getDataInizio()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getDataFine().equals("31/12/9999") ? "ATTUALE" : intF.getDataFine()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getQuota()%></span></td>				
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getQuotaNum()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getQuotaDenom()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getAffidabilita()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getTitolo()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getDescRegime()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intF.getSoggCollegato()%></span></td>

			</tr>
			<%}}  %>
		
	
	</table>
	</div>
	<%}  %>

	
	<% if (vlistaIntestatariG!= null && vlistaIntestatariG.size() >0){%>
	
	<div class="tabbertab">
	<h2>Intest. P. Giuridiche</h2>
	<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">
			Intestatari Persone Giuridiche associati alla particella
	</tr>

	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Inizio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Quota</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Q. Num.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Q. Denom.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Affidab.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
		
	</tr>
  		
		<%it.escsolution.escwebgis.catasto.bean.IntestatarioG intG = new it.escsolution.escwebgis.catasto.bean.IntestatarioG(); %>
  		<%java.util.Enumeration en2 = vlistaIntestatariG.elements(); %>
		<%int progressivoRecord = 1;int contatore=1;%>
  		<%while (en2.hasMoreElements()) {%>
			 <% intG = (it.escsolution.escwebgis.catasto.bean.IntestatarioG)en2.nextElement();
				
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
				df.setLenient(false);
			    java.sql.Date dg_i = new java.sql.Date(df.parse(intG.getDataInizio()).getTime());
				java.sql.Date dg_f = new java.sql.Date(df.parse(intG.getDataFine()).getTime());
				
				String color = "";
				if(dg_i.after(dg_f))
					color = "color:red;";
				else if(intG.getDataFine().equals("31/12/9999"))
					color = "color: #008000;font-weight: bold;";	
				
				//#538 - Non visualizzare record con data_inizio=data_fine
				if(!dg_i.equals(dg_f)){
				%>
				<tr id="r<%=contatore%>" onclick="vaiIntestatarioG('<%=intG.getCodIntestatario()%>', '<%=progressivoRecord%>')">
		        <td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intG.getDenominazione()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intG.getDataInizio()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intG.getDataFine().equals("31/12/9999") ? "ATTUALE" : intG.getDataFine()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intG.getQuota()%></span></td>					
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intG.getQuotaNum()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intG.getQuotaDenom()%></span></td>
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intG.getAffidabilita()%></span></td>					
				<td class="extWinTDData" style='cursor: pointer;<%=color%>'>
					<span class="extWinTXTData"><%=intG.getTitolo()%></span></td>

		</tr>
		<%} progressivoRecord ++; contatore++;} %>
	</table>
	</div>
	<% } %>
	<!-- FINE INTESTATARI -->	
	



	<%
	if ((listaCiviciPerUnimm != null && listaCiviciPerUnimm.size()>0) || (listaCiviciCatPerUnimm != null && listaCiviciCatPerUnimm.size()>0) )
	{	%>
	
	<div class="tabbertab">
	
	<h2>Localizzazione</h2>
	
	
	<%
	if ((listaCiviciPerUnimm != null && listaCiviciPerUnimm.size()>0) )
	{	%>
	<table width="50%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">
			Localizzazione (SIT)
	</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
	</tr>
	
<%
		java.util.Iterator iter = listaCiviciPerUnimm.iterator();
		if (iter.hasNext())
		{
			do
			{
				Civico civ = (Civico) iter.next();
%>
	<tr>
	    <td class="extWinTDData">
		<span class="extWinTXTData"><%= civ.getStrada() %></span></td>
	
	    <td class="extWinTDData">
		<span class="extWinTXTData"><%= civ.getNumero() %></span></td>
	</tr>
<%
			} while (iter.hasNext());
		}
		
	
%>
</table>

<% } %>

<%
	if ((listaCiviciCatPerUnimm != null && listaCiviciCatPerUnimm.size()>0) )
	{	%>

	<table width="50%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>

	<tr class="extWinTXTTitle">
			Localizzazione (Catasto)
	</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
	</tr>
	
<%
	
		Iterator iter = listaCiviciCatPerUnimm.iterator();
		if (iter.hasNext())
		{
			do
			{
				Civico civ = (Civico) iter.next();
%>
	<tr>
	    <td class="extWinTDData">
		<span class="extWinTXTData"><%= civ.getStrada() %></span></td>
	
	    <td class="extWinTDData">
		<span class="extWinTXTData"><%= civ.getNumero() %></span></td>
	</tr>
<%
			} while (iter.hasNext());
		}
	
	
%>
</table>
<% }%>
</div>
<% } %>


	<% 	ArrayList<Docfa> ldocfa = (ArrayList<Docfa>)sessione.getAttribute(CatastoImmobiliLogic.DOCFA_COLLEGATI);
		if (ldocfa != null && ldocfa.size() > 0) { %>
		
		<div class="tabbertab">
				<h2>Dati DOCFA</h2>
				<table width="60%" class="extWinTable" cellpadding="0" cellspacing="0" >
					<tr>&nbsp;</tr>
					<tr class="extWinTXTTitle">
							Dati censuari DOCFA collegati
					</tr>		
			
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Fornitura</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
					</tr>
					<% for(Docfa docfa : ldocfa){%>
					<tr>
						<td class="extWinTDData" ><span class="extWinTXTData"><a href="javascript:dettaglioDocfa('<%=docfa.getChiave()%>');"><%=docfa.getProtocollo()%></a></span></td>
						<td class="extWinTDData" ><span class="extWinTXTData"><%=docfa.getFornitura()%></span></td>
						<td class="extWinTDData" ><span class="extWinTXTData"><%=docfa.getCategoria()%></span></td>
						<td class="extWinTDData" ><span class="extWinTXTData"><%=docfa.getClasse()%></span></td>
						<td class="extWinTDData" ><span class="extWinTXTData"><%=docfa.getRendita()%></span></td>
					</tr>
					<%}%>
				</table>
		</div>
	<%} %>
	
<%  ArrayList<OggettiTARSU> lvani = (ArrayList<OggettiTARSU>)sessione.getAttribute(CatastoImmobiliLogic.DETTAGLIO_VANI_340);
	InformativaTarsuDTO dichTarsu  = (InformativaTarsuDTO)sessione.getAttribute(CatastoImmobiliLogic.ULTIMA_DICH_TARSU);
	DatiMetrici dm = imm.getDatiMetrici();
	boolean visualizzaSupCat = (dm.getCodStatoCat()!=null || dm.getSupCatTarsu()!=null || dm.getSupDPR138()!=null);
	
	if ((lvani != null && lvani.size() > 0) || dichTarsu!=null || visualizzaSupCat) { %>
		
	<div class="tabbertab">
		<h2>Dati Metrici</h2>
		
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato Misurazione Superficie</span></td>
				<td class="extWinTDTitle" title="Superficie catastale ai sensi del DPR 138/98 decurtata delle superfici non tassabili ai fini TARES">
						<span class="extWinTXTTitle">Sup. Catastale TARES</span></td>
				<td class="extWinTDTitle" title="Superficie catastale ai sensi del DPR 138/98">
						<span class="extWinTXTTitle">Sup. Catastale DPR 138/98 (A)</span></td>
				<td class="extWinTDTitle" title="Rapporto tra superficie catastale e consistenza">
						<span class="extWinTXTTitle">Dimensione Vano Medio (A/consistenza)</span></td>
			</tr>
			<tr>
			<%if(visualizzaSupCat){ %>
			
				<td class="extWinTDData" ><span class="extWinTXTData"><%=dm.getCodStatoCat()!=null ? (dm.getCodStatoCat()+" - " +dm.getDescStatoCat()) : "-"%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=dm.getSupCatTarsu()!=null ? dm.getSupCatTarsu() : "-"%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=dm.getSupDPR138()!=null ? dm.getSupDPR138() : "-"%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=dm.getVanoMedioDPR138()!=null ? dm.getVanoMedioDPR138() : "-"%></span></td>
			<%}else{%>
				<td colspan="4" class="extWinTDData" style=" background-color:#F3F2F2;"><span class="extWinTXTData">Nessuna informazione disponibile</span></td>
			<%} %>
			</tr>
	    </table>
	    
	    <br/>
	
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
			<% if (lvani != null && lvani.size() > 0) { 
				for(OggettiTARSU vani : lvani){%>
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
				<td class="extWinTDData" colspan="4"><span class="TXTmainLabel" style="font-weight:bold;">Stima Sup.Calpestabile: (A + B + C) - ingombro muri (20%) </span></td>
				<td class="extWinTDData" colspan="3"><span class="extWinTXTData" style="font-weight:bold;"><%=dm.getSupTarsuABC()%></span></td>
			</tr>
			<%}else{%>
				<tr>
				<td colspan="7" class="extWinTDData" style=" background-color:#F3F2F2;"><span class="extWinTXTData">Nessuna informazione disponibile sui singoli vani</span></td>
				</tr>
				<%}%>
		</table>
	
	    <br/>    
	    	
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">
					Situazione in banca dati Tarsu
			</tr>	
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Oggetto</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Ini.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine</span></td>
			</tr>
			<tr>
			<% if (dichTarsu != null && dichTarsu.getOggettoTarsu()!=null) { 
				SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");

				String datIni=dichTarsu.getOggettoTarsu().getDatIni()!=null ? formatter.format(dichTarsu.getOggettoTarsu().getDatIni()) : "";
				String datFin=dichTarsu.getOggettoTarsu().getDatFin()!=null ? formatter.format(dichTarsu.getOggettoTarsu().getDatFin()) : "";
			
			%>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=dichTarsu.getOggettoTarsu().getDesTipOgg()%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=dichTarsu.getOggettoTarsu().getDesClsRsu()%></span></td>
				<td class="extWinTDData" style="font-weight:bold;" ><span class="extWinTXTData"><%=dichTarsu.getOggettoTarsu().getSupTot()%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=dichTarsu.getOggettoTarsu().getProvenienza()%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=datIni%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=datFin%></span></td>			
			<%}else{%>
				<td class="extWinTDData" colspan="6" style="background-color:#F3F2F2;"><span class="extWinTXTData">Nessuna dichiarazione TARSU per l'immobile</span></td>
			<%}%>
			</tr>
		</table>
	
	</div>
	<%} %>
	
	<% 
	ArrayList planimetrieComma340 = (ArrayList)sessione.getAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_CIM);
	 if (planimetrieComma340 != null && planimetrieComma340.size() > 0) { 
	%>
			<div class="tabbertab">
			<h2>Planimetrie comma 340</h2>
			<table width="70%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">
						Planimetrie comma 340
				</tr>
						
	<%
		
			Iterator it2 = planimetrieComma340.iterator();
			boolean sub = false;
			boolean sub9999 = false;
			while(it2.hasNext())
			{
				ArrayList link = (ArrayList)it2.next();
			%>
			<% if (!((String)link.get(2)).equalsIgnoreCase("9999")) {
				if (!sub) {
					sub = true;%>
					<tr>
						<td class="extWinTDTitle" ><span class="extWinTXTTitle">Planimetrie catastali associate alla UIU</span>
						</td>
					</tr>
			<%	} 
			} else {
				if (!sub9999) {
					sub9999 = true;%>
					<tr>
						<td class="extWinTDTitle" ><span class="extWinTXTTitle">Planimetrie catastali associate al corpo di fabbrica</span>
						</td>
					</tr>
			<% 	}
			}%>
				<tr>
					<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=(String)link.get(0)%>
						</span>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<a style="font-size: 12px;" 
						href="<%= request.getContextPath() %>/CatastoPlanimetrieComma340?pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>&formato=<%=(String)link.get(3)%>">
						PDF
						</a>
						&nbsp;
						<a style="font-size: 12px;" 
						href="<%= request.getContextPath() %>/CatastoPlanimetrieComma340?openJpg=true&pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>&formato=<%=(String)link.get(3)%>">
						IMG
						</a>
					</td>									
				</tr>
			<%}%>
			</table>
			
			</div>
	 <% } %>	
		
	
	

	<% 
	ArrayList<Immobile> immobiliDerivanti = (ArrayList<Immobile>)sessione.getAttribute(CatastoImmobiliLogic.LISTA_UIU_DERIVANTE);
 	
	
	 if (immobiliDerivanti != null && immobiliDerivanti.size() > 0) { 
		 int contatore=1;
		 int progressivoRecord = 1;
	%>

			<div class="tabbertab">
			<h2>Immobili cess. da cui deriva il sub.</h2>
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">
						Immobili cessati da cui deriva il subalterno
			</tr>
			<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato</span></td>
		
	</tr>
			
	<%for(Immobile immobileDerivante: immobiliDerivanti){%>

    <tr onclick="vai('<%=immobileDerivante.getChiave()%>', '<%=progressivoRecord%>')" >
		
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivante.getSezione()%></span></td>	
		<td  class="extWinTDData" style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivante.getFoglio()%></span></td>	
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivante.getNumero()%></span></td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivante.getUnimm()%></span></td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<%-- <span class="extWinTXTData"><%=immobile.getCategoria()%></span>--%>
		<span class="extWinTXTData"><%=immobileDerivante.getCodCategoria()%></span>
		</td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivante.getStato()%></span></td>
		
	</tr>
	
	<% contatore++;progressivoRecord ++; }%>
	</table>
	
	</div>
	<%} %>
	
 <% 
	ArrayList<Immobile> immobiliDerivati = (ArrayList<Immobile>)sessione.getAttribute(CatastoImmobiliLogic.LISTA_UIU_DERIVATE);
	 if (immobiliDerivati != null && immobiliDerivati.size() > 0) { 
		 int contatore=1;
		 int progressivoRecord = 1;
	%>

			<div class="tabbertab">
			<h2>Immobili derivati da cessaz. del sub.</h2>
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">
						Immobili derivati da cessazione del subalterno
			</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato</span></td>
			</tr>
	
	<%for (Immobile immobileDerivato : immobiliDerivati) {%>

    <tr onclick="vai('<%=immobileDerivato.getChiave()%>', '<%=progressivoRecord%>')">
		
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivato.getSezione()%></span></td>	
		<td  class="extWinTDData" style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivato.getFoglio()%></span></td>	
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivato.getNumero()%></span></td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivato.getUnimm()%></span></td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<%-- <span class="extWinTXTData"><%=immobile.getCategoria()%></span>--%>
		<span class="extWinTXTData"><%=immobileDerivato.getCodCategoria()%></span>
		</td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobileDerivato.getStato()%></span></td>
		
		
	</tr>
	
	<% contatore++;} %>
	
	</table>
	</div>
	<%} %>
	
		<%
		ArrayList<PregeoInfo> alPregeo =(ArrayList<PregeoInfo>)sessione.getAttribute("PREGEO");
		if (alPregeo != null && alPregeo.size()>0){
		%>
		
		<div class="tabbertab">
			<h2>Pregeo</h2>
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">
						Pregeo collegati
			</tr>
	
	
		<tr>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Data</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Tecnico</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">&nbsp;</span></td>
		</tr>
			<%
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			for (int i=0; i<alPregeo.size(); i++){
				PregeoInfo bPregeo = (PregeoInfo)alPregeo.get(i);
				if (bPregeo != null){			
			%>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData"><%=bPregeo.getCodicePregeo()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=bPregeo.getDataPregeo()!=null?sdf.format(bPregeo.getDataPregeo()):""%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=bPregeo.getDenominazione()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=bPregeo.getTipoTecnico() + " " + bPregeo.getTecnico() %></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=bPregeo.getFoglio() %></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=bPregeo.getParticella() %></span></td>
				<td class="extWinTDData"><span class="extWinTXTData">
					<% 
					if ( bPregeo != null && bPregeo.getNomeFilePdf()!=null && !bPregeo.getNomeFilePdf().trim().equalsIgnoreCase("") ){
						%><a href="<%= request.getContextPath()%>/OpenPdfServlet?nomePdf=<%=bPregeo.getNomeFilePdf().trim() %>" target="_blank">PDF</a><%				
					}else{
						%>&nbsp;<%
					}
					%>
					</span></td>
			</tr>

			<%}
		} %>
			</table>
			
		</div>
		<% } %>
	
			
			
	
</div>	
	
	
	 <!-- FINE solo dettaglio -->
		<% if(finder != null){%>
		<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
		<% }else{%>
		<input type='hidden' name="ACT_PAGE" value="0">
		<% }%>
		
		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="1">
		<input type='hidden' name="OGGETTO_SEL" value="">
		<input type='hidden' name="RECORD_SEL" value="">
		<input type='hidden' name="EXT" value="">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		
</form>


<div id="wait" class="cursorWait" ></div>
</body>
</html>