<%@ page language="java" import="it.escsolution.escwebgis.istat.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.dup.bean.DupNota nota=(it.escsolution.escwebgis.dup.bean.DupNota)sessione.getAttribute("NOTETERR"); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  it.escsolution.escwebgis.dup.bean.DupNotaFinder finder = null;

	if (sessione.getAttribute("FINDER34") !=null){
		if (((Object)sessione.getAttribute("FINDER34")).getClass() == new it.escsolution.escwebgis.dup.bean.DupNotaFinder().getClass()){
			finder = (it.escsolution.escwebgis.dup.bean.DupNotaFinder)sessione.getAttribute("FINDER34");
			}
					
	}
	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>	
<html>
<head>
<title>Agenzia Entrate - Nota - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>
var url0=null;
var url1=null;
var url2 =null;

function mettiST(){
	document.mainform.ST.value = 3;
}


function settaUrl()
{
	document.frames["linkIFrame"].url0=url0;
	document.frames["linkIFrame"].url1=url1;
	document.frames["linkIFrame"].url2=url2;
		
}

function settaSoggetto(v)
{
/*	if(document.frames["linkIFrame"].url0)
	{
	document.frames["linkIFrame"].url0="SERVLET%3D%2FTributiContribuenti%26QUERY%3DSELECT TRI_CONTRIBUENTI.UK_CODI_CONTRIBUENTE FROM TRI_CONTRIBUENTI, mi_dup_soggetti WHERE (mi_dup_soggetti.codice_fiscale = TRI_CONTRIBUENTI.CODICE_FISCALE OR mi_dup_soggetti.codice_fiscale_g = TRI_CONTRIBUENTI.CODICE_FISCALE) AND TRI_CONTRIBUENTI.CODICE_FISCALE ='"+v+"' AND 1%26NROW%3D0";
	document.frames["linkIFrame"].url1="SERVLET%3D%2FAnagrafeAnagrafe%26QUERY%3DSELECT pop_anagrafe.pk_codi_anagrafe FROM pop_anagrafe, mi_dup_soggetti WHERE (mi_dup_soggetti.codice_fiscale = pop_anagrafe.codice_fiscale OR mi_dup_soggetti.codice_fiscale_g = pop_anagrafe.codice_fiscale) and pop_anagrafe.codice_fiscale='"+v+"' and 1%26NROW%3D1";
	settato=true;
	}*/
	
	url0="SERVLET%3D%2FTributiContribuenti%26QUERY%3DSELECT TRI_CONTRIBUENTI.UK_CODI_CONTRIBUENTE FROM TRI_CONTRIBUENTI, mui_soggetti WHERE (mui_soggetti.codice_fiscale = TRI_CONTRIBUENTI.CODICE_FISCALE OR mui_soggetti.codice_fiscale_g = TRI_CONTRIBUENTI.CODICE_FISCALE) AND TRI_CONTRIBUENTI.CODICE_FISCALE ='"+v+"' AND 1%26NROW%3D0";
	url2="SERVLET%3D%2FAnagrafeDwh%26QUERY%3DSELECT sit_d_persona.id FROM sit_d_persona, mui_soggetti WHERE (mui_soggetti.codice_fiscale = sit_d_persona.codfisc OR mui_soggetti.codice_fiscale_g = sit_d_persona.codfisc) and sit_d_persona.codfisc='"+v+"' and 1%26NROW%3D1";
}
function settaImmobile(	foglio,	numero,	sub)
{

	/*if(document.frames["linkIFrame"].url2)
	{
	document.frames["linkIFrame"].url2="SERVLET%3D%2FTributiOggettiICI%26QUERY%3D"+
	"SELECT tri_oggetti_ici.pk_sequ_oggetti_ici FROM mi_dup_fabbricati_identifica, tri_oggetti_ici WHERE LPAD (tri_oggetti_ici.foglio_catasto, 6, '0') =LPAD (mi_dup_fabbricati_identifica.foglio, 6, '0') "+
"AND LPAD (tri_oggetti_ici.particella_catasto, 5, '0') =LPAD (mi_dup_fabbricati_identifica.numero, 5, '0') "+
"AND LPAD (tri_oggetti_ici.subalterno_catasto, 4, '0') =LPAD (mi_dup_fabbricati_identifica.subalterno, 4, '0') "+
"and mi_dup_terreni_info.foglio='"+foglio+"' "+
"and mi_dup_terreni_info.numero='"+numero+"' "+
"and mi_dup_terreni_info.subalterno='"+sub+"' "+
"AND 1%26NROW%3D0";
	 
	 alert(document.frames["linkIFrame"].url2);
	settatoImm=true;*/
	url1="SERVLET%3D%2FTributiOggettiICI%26QUERY%3D"+
	"SELECT tri_oggetti_ici.pk_sequ_oggetti_ici FROM mui_terreni_info, tri_oggetti_ici WHERE tri_oggetti_ici.foglio_trim =LPAD (mui_terreni_info.foglio, 6, '0') "+
"AND tri_oggetti_ici.particella_trim =LPAD (mui_terreni_info.numero, 5, '0') "+
"AND tri_oggetti_ici.sub_trim  =LPAD (mui_terreni_info.subalterno, 4, '0') "+
"and mui_terreni_info.foglio='"+foglio+"' "+
"and mui_terreni_info.numero='"+numero+"' "+
"and mui_terreni_info.subalterno='"+sub+"' "+
"AND 1%26NROW%3D0";

}
</script>
<body >

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Nota</span>
</div>
&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>

<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/DupNoteTerr" target="_parent">

<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>

&nbsp;

<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:50;"><span class="TXTmainLabel">Tipo </span></td>
				<td class="TDviewTextBox" style="width:40;"><span class="TXTviewTextBox"><%=nota.getTipoNota()%></span></td>

				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Registro particolare n.</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=nota.getNumeroNotaTras()%></span></td>

				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Data presentazione Atto</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=nota.getDataPresAtto()%></span></td>				
<!--				<td class="TDmainLabel"  ><span class="TXTmainLabel">Progressivo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=nota.getProgressivoNota()%></span></td>-->
				
			</tr>
		</table>
		</td>
	</tr>

	<tr></tr>
	<tr></tr>
	<tr></tr>
		<tr>	
		<td colspan=1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel" style="width:5%; white-space: nowrap;"><span class="TXTmainLabel">Nominativo Rog.</span></td>
				<td class="TDviewTextBox" style="width:200;"><span class="TXTviewTextBox"><%=nota.getCognomeNomeRog()%></span></td>
				<td class="TDmainLabel"  ><span class="TXTmainLabel">Cod. Fisc Rog.</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=nota.getCodFiscRog()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>	
		<tr>	
		<td colspan=1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  ><span class="TXTmainLabel">Sede Rog</span></td>
				<td class="TDviewTextBox" style="width:160;"><span class="TXTviewTextBox"><%=nota.getSedeRog()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>				
	<tr>	
		<td colspan=1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:20; white-space: nowrap;"><span class="TXTmainLabel">Anno</span></td>
				<td class="TDviewTextBox" style="width:50;"><span class="TXTviewTextBox"><%=nota.getAnnoNota()%></span></td>
				<td class="TDmainLabel"  style="width:5%; white-space: nowrap;"><span class="TXTmainLabel">N. repertorio</span></td>
				<td class="TDviewTextBox" style="width:60;"><span class="TXTviewTextBox"><%=nota.getNumeroRepertorio()%></span></td>
				<td class="TDmainLabel" style="width:5%; white-space: nowrap;" ><span class="TXTmainLabel">Data Validita Atto</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=nota.getDataValiditaAtto()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>	
	<tr></tr>
	<tr></tr>
	<tr></tr>	
	<tr>		
		
		<td colspan=1>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:50;"><span class="TXTmainLabel">Esito</span></td>
				<td class="TDviewTextBox" style="width:30;"><span class="TXTviewTextBox"><%=nota.getEsitoNota()%></span></td>
				<td class="TDmainLabel"  style="width:5%; white-space: nowrap;"><span class="TXTmainLabel">Esito non registrato</span></td>
				<td class="TDviewTextBox" style="width:30;"><span class="TXTviewTextBox"><%=nota.getEsitoNotaNonReg()%></span></td>
				<td class="TDmainLabel"  style="width:5%; white-space: nowrap;" ><span class="TXTmainLabel">Data reg. Attività</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=nota.getDataRegInAtti()%></span></td>
			</tr>
			
			
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>		
		
		<td colspan=1>	
		<table class="viewExtTable" >
			<tr>			

				<td class="TDmainLabel"  style="width:50;"><span class="TXTmainLabel">Rettifica</span></td>
				<td class="TDviewTextBox" style="width:30;"><span class="TXTviewTextBox"><%=nota.getFlagRettifica()%></span></td>
<%if (nota.getTipoNotaRet().equals("SI"))
{%>
				<td class="TDmainLabel"  ><span class="TXTmainLabel">Tipo nota rettifica</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=nota.getTipoNotaRet()%></span></td>
				<td class="TDmainLabel"  ><span class="TXTmainLabel">Nota rettifica n.</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=nota.getNumeroNotaRet()%></span></td>
				<td class="TDmainLabel"  ><span class="TXTmainLabel">Data pres. atto Rett.</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=nota.getDataPresAttoRet()%></span></td>
<%}%>
			</tr>
		</table>
		</td>
	</tr>		
	<tr></tr>
	<tr></tr>
	<tr></tr>	
			

			
	<tr>	
		<td colspan=1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel" style="width:5%; white-space: nowrap;"><span class="TXTmainLabel">Registrazione Diff.</span></td>
				<td class="TDviewTextBox" style="width:30;"><span class="TXTviewTextBox"><%=nota.getRegistrazioneDif()%></span></td>
				<td class="TDmainLabel"  style="width:5%; white-space: nowrap;"><span class="TXTmainLabel">Data in Diff.</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=nota.getDataInDif()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>	
	
</table>

</td>

</tr>
</table>

	
	<div class="tabber">

<% if (nota.getListTerreni()!=null && nota.getListTerreni().size()>0)
{%>
<div class="tabbertab">
<h2>Terreni</h2>


 <table width='100%'class="extWinTable" cellpadding="0" cellspacing="0">
 <tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Terreni</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sel</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez.</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Fgl.</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Part.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">ID Catastale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Esito</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Edificabilità</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Natura</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Qualità</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Ettari</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Are</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Centiare</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Red. Dominicale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Red. Agrario</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita</span></td>
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">Mappa</span></td>

	</tr>
	
	<% it.escsolution.escwebgis.dup.bean.DupTerreni terr = new it.escsolution.escwebgis.dup.bean.DupTerreni(); %>
	
  <% for (int i=0;i<nota.getListTerreni().size();i++) {
        terr = (it.escsolution.escwebgis.dup.bean.DupTerreni)nota.getListTerreni().get(i);%>

    <tr >
		<td class="extWinTDData" ><span class="extWinTXTData"><input type="radio" name="selezioneImm" value="settaImmobile('<%=terr.getFoglio()%>','<%=terr.getNumero()%>','<%=terr.getSubalterno()%>')" onclick="settaImmobile('<%=terr.getFoglio()%>','<%=terr.getNumero()%>','<%=terr.getSubalterno()%>')"></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getSezioneCensuaruia()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=terr.getFoglio()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getNumero()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=terr.getSubalterno()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=terr.getIdCatastaleImmobile()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getCodiceEsito()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=terr.getEdificabilita()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getNatura()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getQualita()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=terr.getClasse()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=terr.getEttari()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getAre()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getCentiare()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getRedditoDominicaleEuro()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getRedditoAgrarioEuro()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=terr.getPartita()%></span></td>
		<td onclick="zoomInMappaParticelle('<%=terr.getCodEnte()%>','<%=terr.getFoglio()%>','<%=terr.getNumero()%>');" class="TDviewImage" style='cursor: pointer;'>
			<span class="extWinTXTData">
				<img src="../ewg/images/Localizza.gif"/>
			</span>
		</td>
		<td class="TDviewImage" style='cursor: pointer;'>
			<a href="javascript:apriPopupVirtualH(<%=terr.getLatitudine()==null?0:terr.getLatitudine()%>,<%=terr.getLongitudine()==null?0:terr.getLongitudine()%>);" >
				<span class="extWinTXTData"><img src="../ewg/images/3D.gif" border="0" width="24" height="30" /></span>
			</a>
		</td>
		<td class="TDviewImage" style='cursor: pointer;'>
			<a href="javascript:apriPopupStreetview(<%=terr.getLatitudine()==null?0:terr.getLatitudine()%>,<%=terr.getLongitudine()==null?0:terr.getLongitudine()%>);" >
				<span class="extWinTXTData"><img src="../ewg/images/streetview.gif" border="0" width="17" height="32" /></span>
			</a>
		</td>
	</tr>
<% } %>
</table>
</div>
	<%}%>

<% if (nota.getListSogFavore()!=null && nota.getListSogFavore().size()>0 )
{%>
<div class="tabbertab">
<h2>Soggetti a favore</h2>

<br>

 <table  width='100%'class="extWinTable" cellpadding="0" cellspacing="0">
 <tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Soggetti a favore</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sel.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo sogg.</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sede</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sesso</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Nascita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Luogo Nasc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Dati Immobile</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Diritto</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Quota poss.</span></td>		
	</tr>
	
	<% it.escsolution.escwebgis.dup.bean.DupSoggetto sogg = new it.escsolution.escwebgis.dup.bean.DupSoggetto(); %>
	
  <% for (int i=0;i<nota.getListSogFavore().size();i++) {
        sogg = (it.escsolution.escwebgis.dup.bean.DupSoggetto)nota.getListSogFavore().get(i);%>

    <tr >
   		<td class="extWinTDData" ><span class="extWinTXTData"><input type="radio" name="selezione" value="<%=sogg.getCodFiscPIVA()%>" onclick="settaSoggetto(this.value)"></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getCodFiscPIVA()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getTipoSoggetto()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getDenominazione()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getSede()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getCognome()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getNome()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getSesso()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getDataNascita()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getLuogoNascita()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getIndirizzoSoggetto()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData">
		<b>Fgl. </b><%=sogg.getFoglio()%>
		<b>Part. </b><%=sogg.getNumero()%>
		<b>Sub. </b><%=sogg.getSub()%>
		</span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getSfCodiceDiritto()%> - <%=sogg.getDescrDiritto()%></span></td>		
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getQuota()%></span></td>

	</tr>
<% } %>
</table>
</div>
	<%}%>


<% if (nota.getListSogContro()!=null && nota.getListSogContro().size()>0)
{%>
<div class="tabbertab">
<h2>Soggetti contro</h2>

<br>

 <table  width='100%'class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Soggetti contro</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sel.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo sogg.</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sede</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sesso</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Nascita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Luogo Nasc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>			
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Dati Immobile</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Diritto</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Quota poss.</span></td>		
	</tr>
	
	<% it.escsolution.escwebgis.dup.bean.DupSoggetto sogg = new it.escsolution.escwebgis.dup.bean.DupSoggetto(); %>
	
  <% for (int i=0;i<nota.getListSogContro().size();i++) {
        sogg = (it.escsolution.escwebgis.dup.bean.DupSoggetto)nota.getListSogContro().get(i);%>

    <tr >
   		<td class="extWinTDData" ><span class="extWinTXTData"><input type="radio" name="selezione" value="<%=sogg.getCodFiscPIVA()%>" onclick="settaSoggetto(this.value)"></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getCodFiscPIVA()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getTipoSoggetto()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getDenominazione()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getSede()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getCognome()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getNome()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getSesso()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getDataNascita()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getLuogoNascita()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getIndirizzoSoggetto()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData">
		<b>Fgl. </b><%=sogg.getFoglio()%>
		<b>Part. </b><%=sogg.getNumero()%>
		<b>Sub. </b><%=sogg.getSub()%>
		</span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getScCodiceDiritto()%>  - <%=sogg.getDescrDiritto()%></span></td>		
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getQuota()%></span></td>		
	</tr>
<% } %>
</table>
</div>
	<%}%>


<% if (nota.getListSogNonCoinvolti()!=null)
{%>
<div class="tabbertab">
<h2>Soggetti non Coinvolti</h2>

<br>

 <table width='80%'class="extWinTable" cellpadding="0" cellspacing="0">
 <tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Soggetti non coinvolti</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sel.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo sogg.</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sede</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sesso</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Nascita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Luogo Nasc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Dati Immobile</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Quota poss.</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.dup.bean.DupSoggetto sogg = new it.escsolution.escwebgis.dup.bean.DupSoggetto(); %>
	
  <% for (int i=0;i<nota.getListSogNonCoinvolti().size();i++) {
        sogg = (it.escsolution.escwebgis.dup.bean.DupSoggetto)nota.getListSogNonCoinvolti().get(i);%>

    <tr >
   		<td class="extWinTDData" ><span class="extWinTXTData"><input type="radio" name="selezione" value="<%=sogg.getCodFiscPIVA()%>" onclick="settaSoggetto(this.value)"></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getCodFiscPIVA()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getTipoSoggetto()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getDenominazione()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getSede()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getCognome()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getNome()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getSesso()%></span></td>
		<td class="extWinTDData" ><span class="extWinTXTData"><%=sogg.getDataNascita()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getLuogoNascita()%></span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData">
		<b>Fgl. </b><%=sogg.getFoglio()%>
		<b>Part. </b><%=sogg.getNumero()%>
		<b>Sub. </b><%=sogg.getSub()%>
		</span></td>
		<td class="extWinTDData"  ><span class="extWinTXTData"><%=sogg.getQuota()%></span></td>
	</tr>
<% } %>
</table>
</div>
	<%}%>


</div>

<!-- FINE solo dettaglio -->
<% if (nota != null){%>
 
   <input type='hidden' name="OGGETTO_SEL" >
   
   
		
<%} if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="34">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>

<div id="wait" class="cursorWait" />
</body>
<script>

	var radSel=document.mainform.selezione;
	var radSelImm=document.mainform.selezioneImm;
	if(radSel && !radSel.checked )
	{
		radSel[0].checked=true;
		settaSoggetto (radSel[0].value);
	}
/*alert(radSelImm);
		alert(radSelImm.checked);
		alert(radSelImm[0]);*/

	if(radSelImm && !radSelImm.checked )
	{
		if(radSelImm[0])
		{
		radSelImm[0].checked=true;
//		settaImmobile(radSelImm[0].value);
eval(radSelImm[0].value);
		}
		else
		{
		radSelImm.checked=true;
		//settaImmobile(radSelImm.value);
		eval(radSelImm.value);
		
		}
		
	}
	
	
	
</script>
</html>