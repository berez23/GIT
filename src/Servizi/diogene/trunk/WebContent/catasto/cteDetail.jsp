<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.ct.data.model.catasto.Sititrkc"%>
<%@ page language="java" import="java.util.*,java.text.*,
it.escsolution.escwebgis.catasto.bean.*, it.escsolution.escwebgis.catasto.logic.*,
it.webred.ct.data.access.basic.catasto.dto.* " %>
<%   HttpSession sessione = request.getSession(true);  %>
<% Terreno terr=(Terreno)sessione.getAttribute("TERRENO"); %>
<% ArrayList<PregeoInfo> alPregeo =(ArrayList<PregeoInfo>)sessione.getAttribute("PREGEO"); %>
<% SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy"); %>
<% DecimalFormat DF = new DecimalFormat("#0.00"); %>
<% 
	TerreniFinder finder = null;

	 Vector vlistaIntestatariF=null;
	 Vector vlistaIntestatariG=null;
	
	
	if (sessione.getAttribute("FINDER2") != null){ 
		finder = ( TerreniFinder)sessione.getAttribute("FINDER2"); 
	}
	if(( Vector)sessione.getAttribute("LISTA_INTESTATARIF2") != null){
		 vlistaIntestatariF=( Vector)sessione.getAttribute("LISTA_INTESTATARIF2"); 
		 vlistaIntestatariG=( Vector)sessione.getAttribute("LISTA_INTESTATARIG2"); 
	 }
	 else{
		 vlistaIntestatariF=( Vector)sessione.getAttribute("LISTA_INTESTATARIF"); 
		 vlistaIntestatariG=( Vector)sessione.getAttribute("LISTA_INTESTATARIG"); 
	}
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>




<%@page import="java.util.ArrayList"%>
<%@page import="it.escsolution.escwebgis.pregeo.bean.PregeoInfo"%>
<%@page import="java.text.SimpleDateFormat"%><html>
<html>
<head>
<title>Catasto Terreni - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script>

function vaiIntestatarioG(intestatario,record_cliccato){
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoIntestatariG";
	document.mainform.OGGETTO_SEL.value=intestatario;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.UC.value = 4;
	document.mainform.EXT.value=1;
	document.mainform.submit();
}
function vaiIntestatarioF(intestatario,record_cliccato){
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoIntestatariF";
	document.mainform.OGGETTO_SEL.value=intestatario;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.UC.value = 3;
	document.mainform.EXT.value=1;
	document.mainform.submit();
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
				document.mainform.target = "_parent";
				document.mainform.submit();
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

var popUpAperte = new Array();
function apriPopUp(index)
{
	if (popUpAperte[index])
	{
		if (popUpAperte[index].closed)
		{
			popUpAperte[index] = window.open("", "PopUpTERDetail" + index, "width=640,height=480,status=yes,resizable=yes");
			popUpAperte[index].focus();
			return popUpAperte[index].name;
		}
		else
		{
			popUpAperte[index].focus();
			return false;
		}
	}
	else
	{
		popUpAperte[index] = window.open("", "PopUpTERDetail" + index, "width=640,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}

</script>

</head>


<script>
function mettiST(){
	//alert("mettiST");
	document.mainform.ST.value = 3;
}
</script>

<body >

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoTerreni" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Scheda Particelle Terreni</span>
</div>
		
&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>

<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top;">

	<table width="100%" cellpadding="0" cellspacing="0">
	
	<tr class="extWinTXTTitle">Dati Identificativi</tr>
	
	<tr>
		<td style="width: 80%; vertical-align: top;">
			<table width="100%" cellpadding="0" cellspacing="0" class="editExtTable">
			<tr>
			  <td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Comune</span></td>
						<td class="TDviewTextBox" style="width:85;"><span class="TXTviewTextBox"><%=terr.getComune()%></span></td>
					</tr>
				</table>
			  </td>
			<td>
			<table class="viewExtTable" >
				<tr>
					<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Sezione</span></td>
					<td class="TDviewTextBox" style="width:85;"><span class="TXTviewTextBox"><%=terr.getSezione()%></span></td>
				</tr>
			</table>
			</td>
		
			<td>
			<table class="viewExtTable" >
				<tr>
					<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Foglio</span></td>
					<td class="TDviewTextBox" style="width:85;"><span class="TXTviewTextBox"><%=terr.getFoglio()%></span></td>
				</tr>
			</table>
			</td>	
		
			<td>	
			<table class="viewExtTable" >
				<tr>			
					<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Particella</span></td>
					<td class="TDviewTextBox" style="width:85;"><span class="TXTviewTextBox"><%=terr.getNumero()%></span></td>
				</tr>
			</table>
			</td>		
				
			<td >		
			<table class="viewExtTable" >
				<tr>	
					<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Subalterno</span></td>
					<td class="TDviewTextBox" style="width:85;"><span class="TXTviewTextBox"><%=terr.getSubalterno()%></span></td>
				</tr>			
			</table>
			</td>
		
		</tr>
		</table>
		</td>
		
		<td  style="width: 20%; padding-left: 15px;">
			<table class="viewExtTable" >
				<tr>
					<td class="extWinTDData" style="border-style: none; cursor: pointer; text-align: center; width: 35px;" onclick="zoomInMappaParticelle('<%= terr.getComune() %>','<%=terr.getFoglio()%>','<%=terr.getNumero()%>');">
						<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
					</td>
					<td class="extWinTDData" style="text-align: center; width: 35px; border-style: none;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupVirtualH(<%=terr.getLatitudine()==null?0:terr.getLatitudine()%>,<%=terr.getLongitudine()==null?0:terr.getLongitudine()%>);">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
							</a>
						</span>
					</td>
					<td class="extWinTDData" style="text-align: center; width: 35px; border-style: none;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupStreetview(<%=terr.getLatitudine()==null?0:terr.getLatitudine()%>,<%=terr.getLongitudine()==null?0:terr.getLongitudine()%>);">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
							</a>
						</span>
					</td>
				</tr>
			</table>
		</td>		
		</tr>
	</table>
</td>
<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>
<div class="tabber">

	<!-- STORICO TERRENO -->
		<% List<Sititrkc> lstStorico =(ArrayList<Sititrkc>)sessione.getAttribute(CatastoTerreniLogic.LISTA_TERR_STORICO); %>

	<% if (lstStorico != null && lstStorico.size() > 0) { 
		 int contatore=1;
		 int progressivoRecord = 1;
	%>

			<div class="tabbertab">
			<h2>Variazioni catastali</h2>
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">
				 Variazioni catastali 
			</tr>
			<tr>
			
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Inizio Val.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Qualità</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">HA ARE CA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Redd.Dominicale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Redd.Agrario</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Annotazioni</span></td>
		
	</tr>
			
	<%for(Sititrkc terSto : lstStorico){
	
		String dtIni = terSto.getDataAggi()!=null ? SDF.format(terSto.getDataAggi()) : "01/01/1000";
		String dtFin = terSto.getId().getDataFine()!=null ? SDF.format(terSto.getId().getDataFine()) : "31/12/9999";
	
	%>

    <tr>
		<td  class="extWinTDData" >
		<span class="extWinTXTData"><%=dtIni%></span></td>	
		<td  class="extWinTDData" >
		<span class="extWinTXTData"><%=dtFin.equals("31/12/9999") ? "ATTUALE" : dtFin%></span></td>	
		<td  class="extWinTDData" >
		<span class="extWinTXTData"><%=terSto.getPartita()%></span></td>	
		<td  class="extWinTDData" >
		<span class="extWinTXTData"><%=terSto.getQualCat()+"-"+terSto.getDescQualita()%></span></td>	
		<td  class="extWinTDData" >
		<span class="extWinTXTData"><%=terSto.getClasseTerreno()%></span></td>	
		<td  class="extWinTDData" style='text-align: right;'>
		<span class="extWinTXTData"><%=terSto.getAreaPart()%></span></td>	
		<td  class="extWinTDData"  style='text-align: right;'>
		<span class="extWinTXTData"><%=terSto.getRendita()%></span></td>
		<td  class="extWinTDData"  style='text-align: right;'>
		<span class="extWinTXTData"><%=terSto.getRedditoDominicale()!=null ? DF.format(terSto.getRedditoDominicale()) : "-"%></span></td>
		<td  class="extWinTDData"  style='text-align: right;'>
		<span class="extWinTXTData"><%=terSto.getRedditoAgrario()!=null ? DF.format(terSto.getRedditoAgrario()): "-"%></span></td>	
		<td  class="extWinTDData" >
		<span class="extWinTXTData"><%=terSto.getAnnotazioni()!=null ? terSto.getAnnotazioni() : ""%></span></td>	
	</tr>
	
	<% contatore++;progressivoRecord ++; }%>
	</table>
	
	</div>
	<%} %>
	<!-- FINE STORICO TERRENO -->
	
	<!-- INTESTATARI -->
	<% if(vlistaIntestatariF != null && vlistaIntestatariF.size()>0){ %>
	
		<div class="tabbertab">
			<h2>Intestatari Persone Fisiche</h2>
	
	<!-- INIZIO INTESTATARI -->
	<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Intestatari Persone Fisiche associati alla particella</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Inizio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Quota</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Regime</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sogg. collegato</span></td>
	</tr>
		<%it.escsolution.escwebgis.catasto.bean.IntestatarioF intF = new it.escsolution.escwebgis.catasto.bean.IntestatarioF(); %>
  			
  		<% Enumeration en = vlistaIntestatariF.elements(); %>
		<% long progressivoRecord = 1;int contatore=1;%>
  		<%while (en.hasMoreElements()) {%>
		  		<% intF = (it.escsolution.escwebgis.catasto.bean.IntestatarioF)en.nextElement();%>
		<tr id="r<%=contatore%>" onclick="vaiIntestatarioF('<%=intF.getCodIntestatario()%>', '<%=progressivoRecord%>')">
		
				
		        <td class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=intF.getCognome()%></span></td>
	
				<td class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=intF.getNome()%></span></td>
				<%
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
				df.setLenient(false);
				//try
				//{
					java.sql.Date d_i = new java.sql.Date(df.parse(intF.getDataInizio()).getTime());
					java.sql.Date d_f = new java.sql.Date(df.parse(intF.getDataFine()).getTime());
					
				if ( d_i.after(d_f) ){%>
					<td class="extWinTDData" style='cursor: pointer;color:red;'>
					<span class="extWinTXTData"><%=intF.getDataInizio()%></span></td>
				
					<td class="extWinTDData" style='cursor: pointer;color:red;'>
					<span class="extWinTXTData"><%=intF.getDataFine()%></span></td>
				<%}else{ %>
					<td class="extWinTDData" style='cursor: pointer;'>
					<span class="extWinTXTData"><%=intF.getDataInizio()%></span></td>
				
					<td class="extWinTDData" style='cursor: pointer;'>
					<span class="extWinTXTData"><%=intF.getDataFine()%></span></td>
				
				<%}%>
				
				<td class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=intF.getQuota()%></span></td>
				
				<td class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=intF.getTitolo()%></span></td>
				
				<td class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=intF.getDescRegime()%></span></td>
				
				<td class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=intF.getSoggCollegato()%></span></td>

		</tr>
				<%progressivoRecord ++; contatore++;}%>
		
	
	</table>
	</div>
	
	<% } %>
	
	<% if(vlistaIntestatariG != null && vlistaIntestatariG.size()>0){ %>
	
		<div class="tabbertab">
			<h2>Intestatari Persone Giuridiche</h2>

	<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>&nbsp;</tr>
	
	<tr class="extWinTXTTitle">Intestatari Persone Giuridiche associati alla particella</tr>
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Inizio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Quota</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
		
	</tr>
  		
		<%it.escsolution.escwebgis.catasto.bean.IntestatarioG intG = new it.escsolution.escwebgis.catasto.bean.IntestatarioG(); %>
  		<%java.util.Enumeration en2 = vlistaIntestatariG.elements(); %>
		<%int progressivoRecord = 1;int contatore=1;%>
  		<%while (en2.hasMoreElements()) {%>
				<% intG = (it.escsolution.escwebgis.catasto.bean.IntestatarioG)en2.nextElement();%>
		<tr id="r<%=contatore%>" onclick="vaiIntestatarioG('<%=intG.getCodIntestatario()%>', '<%=progressivoRecord%>')">
		        <td class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=intG.getDenominazione()%></span></td>
	
				<%
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
				df.setLenient(false);
				
				java.sql.Date dg_i = new java.sql.Date(df.parse(intG.getDataInizio()).getTime());
				java.sql.Date dg_f = new java.sql.Date(df.parse(intG.getDataFine()).getTime());
					
				if ( dg_i.after(dg_f) ){%>	
					<td class="extWinTDData" style='cursor: pointer;color:red;'>
					<span class="extWinTXTData"><%=intG.getDataInizio()%></span></td>
					
					<td class="extWinTDData" style='cursor: pointer;color:red'>
					<span class="extWinTXTData"><%=intG.getDataFine()%></span></td>
				<%}else{ %>
				<td class="extWinTDData" style='cursor: pointer;'>
					<span class="extWinTXTData"><%=intG.getDataInizio()%></span></td>
					
					<td class="extWinTDData" style='cursor: pointer;'>
					<span class="extWinTXTData"><%=intG.getDataFine()%></span></td>
				<%}%>
				
				<td class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=intG.getQuota()%></span></td>
	
				<td class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=intG.getTitolo()%></span></td>
				
		</tr>
		<%progressivoRecord ++; contatore++;}%>
	</table>
	
	</div>
	
	<%} %>
	<!-- FINE INTESTATARI -->
	
	<!-- LISTA TERRENI CHE GENERANO IL SUB IN SEGUITO A CESSAZIONE -->
	<% List<TerrenoDerivazioneDTO> terrGeneratori =(ArrayList<TerrenoDerivazioneDTO>)sessione.getAttribute(CatastoTerreniLogic.LISTA_TERR_GENERATORI); %>

	<% if (terrGeneratori != null && terrGeneratori.size() > 0) { 
		 int contatore=1;
		 int progressivoRecord = 1;
	%>

			<div class="tabbertab">
			<h2>Terreni cessati da cui deriva</h2>
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Terreni cessati da cui deriva</tr>
			<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato</span></td>
		
	</tr>
			
	<%for(TerrenoDerivazioneDTO terGen : terrGeneratori){
	
		String dtIni = terGen.getDataIniVal()!=null ? SDF.format(terGen.getDataIniVal()) : "01/01/1000";
		String dtFin = terGen.getDataFinVal()!=null ? SDF.format(terGen.getDataFinVal()) : "31/12/9999";
		
		String key = terGen.getCodNazionale()+"|"+terGen.getFoglio()+"|"+terGen.getParticella()+"|"+terGen.getSubalterno()+"|"+dtFin+"|"+dtIni;
	%>

    <tr onclick="vai('<%=key%>', '<%=progressivoRecord%>',false)" >
		
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terGen.getSezione()!=null ? terGen.getSezione() : ""%></span></td>	
		<td  class="extWinTDData" style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terGen.getFoglio()%></span></td>	
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terGen.getParticella()%></span></td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terGen.getSubalterno()%></span></td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terGen.getDescrFineVal()%></span></td>	
	</tr>
	
	<% contatore++;progressivoRecord ++; }%>
	</table>
	
	</div>
	<%} %>
	<!-- FINE LISTA TERRENI CHE GENERANO IL SUB IN SEGUITO A CESSAZIONE -->
	 
	<!-- LISTA TERRENI DERIVATI -->
 <%  List<TerrenoDerivazioneDTO> terrDerivati =(ArrayList<TerrenoDerivazioneDTO>)sessione.getAttribute(CatastoTerreniLogic.LISTA_TERR_DERIVATI);
     if (terrDerivati != null && terrDerivati.size() > 0) { 
		 int contatore=1;
		 int progressivoRecord = 1;
	%>

			<div class="tabbertab">
			<h2>Terreni derivati da cessaz.</h2>
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Terreni derivati da cessazione</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato</span></td>
			</tr>
			
	<% for(TerrenoDerivazioneDTO terrDer : terrDerivati){ 
	
		String dtIni = terrDer.getDataIniVal()!=null ? SDF.format(terrDer.getDataIniVal()) : "01/01/1000";
		String dtFin = terrDer.getDataFinVal()!=null ? SDF.format(terrDer.getDataFinVal()) : "31/12/9999";
		
		String key = terrDer.getCodNazionale()+"|"+terrDer.getFoglio()+"|"+terrDer.getParticella()+"|"+terrDer.getSubalterno()+"|"+dtFin+"|"+dtIni;
	%>

    <tr onclick="vai('<%=key%>', '<%=progressivoRecord%>',false)">
		
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terrDer.getSezione()!=null ? terrDer.getSezione() : ""%></span></td>	
		<td  class="extWinTDData" style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terrDer.getFoglio()%></span></td>	
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terrDer.getParticella()%></span></td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terrDer.getSubalterno()%></span></td>
		<td  class="extWinTDData"  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=terrDer.getDescrFineVal()%></span></td>
	</tr>
	
	<% contatore++;} %>
	
	</table>
	</div>
	<%} %>
	<!-- FINE LISTA TERRENI DERIVATI -->
	
	<!-- PREGEO -->
	<%
		if (alPregeo != null && alPregeo.size()>0){
		%>
		
		<div class="tabbertab">
			<h2>Pregeo</h2>
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Pregeo collegati</tr>
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
						%><a href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH%>/OpenPdfServlet?nomePdf=<%=bPregeo.getNomeFilePdf().trim() %>" target="_blank">PDF</a><%				
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
		
		<!-- FINE PREGEO -->
	
 </div>
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="0">
<% }%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="ST" value="">
<input type='hidden' name="UC" value="<%=(java.lang.String)sessione.getAttribute("UC")%>">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="EXT" value="">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>



<div id="wait" class="cursorWait" />
</body>
</html>