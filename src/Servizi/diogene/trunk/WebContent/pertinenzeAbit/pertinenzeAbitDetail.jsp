<%@ page language="java"
	import="java.util.Iterator,
			it.escsolution.escwebgis.pertinenzeAbit.servlet.*,
			it.escsolution.escwebgis.pertinenzeAbit.logic.*"
			%>
			
<%@page import="java.util.ArrayList"%>
<%@page import="it.webred.utils.GenericTuples"%>
<%@page import="it.webred.utils.DateFormat"%>
<%@page import="it.escsolution.escwebgis.pertinenzeAbit.bean.DatiCatastali"%>
<%@page import="it.escsolution.escwebgis.pertinenzeAbit.bean.DatiTitolarita"%>
<%@page import="it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitFinder"%>
<%@page import="it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbit"%>


<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%
	HttpSession sessione = request.getSession(true);
	
	ArrayList listaDatiCatastali = (java.util.ArrayList) sessione.getAttribute(PertinenzeAbitLogic.LISTA_DETTAGLIO_DATI_CATASTALI);
	ArrayList listaTitolarita = (java.util.ArrayList) sessione.getAttribute(PertinenzeAbitLogic.LISTA_DETTAGLIO_TITOLARITA);
	ArrayList listaAltreTitolarita = (java.util.ArrayList) sessione.getAttribute(PertinenzeAbitLogic.LISTA_DETTAGLIO_ALTRE_TITOLARITA);
	ArrayList listaTitolare = (java.util.ArrayList) sessione.getAttribute(PertinenzeAbitLogic.LISTA_DETTAGLIO_TITOLARE);
	ArrayList listaCivicoInPertinenza = (java.util.ArrayList) sessione.getAttribute(PertinenzeAbitLogic.CIVICO_IN_PERTINENZA_CARTOGRAFICA);
	ArrayList listaUiRes = (java.util.ArrayList) sessione.getAttribute(PertinenzeAbitLogic.UI_RESIDENZIALE_IN_PERTINENZA);
	ArrayList listaUiDiCat = (java.util.ArrayList) sessione.getAttribute(PertinenzeAbitLogic.UI_DI_CATEGORIA_IN_PERTINENZA);
	
	java.lang.String ST = (java.lang.String) sessione.getAttribute("ST");
	int st = new Integer(ST).intValue();

	PertinenzeAbitFinder finder = null;

	if (sessione.getAttribute("FINDER106") != null)
	{
		if (((Object) sessione.getAttribute("FINDER106")).getClass() == new PertinenzeAbitFinder().getClass())
		{
			finder = (PertinenzeAbitFinder)sessione.getAttribute("FINDER106");
		}

	}

	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT") != null)
		js_back = ((Integer) sessione.getAttribute("BACK_JS_COUNT")).intValue();

	java.util.Vector vctLink = null;
	if (sessione.getAttribute("LISTA_INTERFACCE") != null)
	{
		vctLink = ((java.util.Vector) sessione.getAttribute("LISTA_INTERFACCE"));
	}
%>


<%@page import="it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitTitolare"%>
<%@page import="it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitDatiCatastali"%><html>
<head>
<title>Pertinenze Abitazioni - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}
function apriPopupCens(codice,im,f,m,s)
{
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit?popupCens=true&codice='+codice+'&im='+im+'&f='+f+'&m='+m+'&s='+s;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
function apriPopupPrg(ente,fg,part)
{
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit?popupPrg=true&cod_ente='+ente+'&fg='+fg+'&part='+part;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

function apriPopupConcessioni(ente,fg,part)
{
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit?popupConcessioni=true&cod_ente='+ente+'&fg='+fg+'&part='+part;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
function apriPopupGraf(fg,part,sub)
{
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit?popupGraf=true&fg='+fg+'&part='+part+'&sub='+sub;
			var finestra=window.open(url,"_dati","height=200,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
function apriClasseAttesa(valore, vani, zona,foglio,particella,sub,rapportovalore,consistenza,categoria,classe, rapporto)
{
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit?popupClasseAtt=true&valore='+valore+'&vani='+vani+'&zona='+zona+'&foglio='+foglio+'&particella='+particella+'&sub='+sub+'&rapportovalore='+rapportovalore+'&consistenza='+consistenza+'&categoria='+categoria+'&classe='+classe+'&rapporto=' +rapporto;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");			
			finestra.focus();
}
function apriCategoriaClasseInParticella()
{
			var OGGETTO_SEL = document.getElementById("OGGETTO_SEL").value;
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit?popupCategoriaClasseInParticella=true&OGGETTO_SEL='+OGGETTO_SEL;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");			
			finestra.focus();
}
function apri340(f,m,s)
{
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit?popup340=true&f='+f+'&m='+m+'&s='+s;
			var finestra=window.open(url,"_dati","height=250,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
function apriPopupCatasto(f,p,s,d,cod_ente)
{
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit?popupCatasto=true&f='+f+'&p='+p+'&s='+s+'&d='+d+'&cod_ente='+cod_ente;
			var finestra=window.open(url,"","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

</script>
<body onload="mettiST()">


<form name="mainform"
	action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit"
	target="_parent">

<div align="center" class="extWinTDTitle"><span
	class="extWinTXTTitle">Dettaglio Pertinenze Abitazioni</span></div>
&nbsp;
<table style="background-color: white; width: 100%;">
	<tr style="background-color: white;">
		<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

		<span class="TXTmainLabel">Dati Catastali</span>
		<table align=center class="extWinTable" style="width: 50%;">
			<tr>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Part.</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Sub.</span></td>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Data Inizio Validita</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Categ.</span></td>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Classe</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Cons.</span></td>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Rend.</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Superf. Cat.</span></td>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Zona</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Edif.</span></td>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Scala</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Interno</span></td>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Piano</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Civ.</span></td>
			</tr>
			<%
			if (listaDatiCatastali != null && listaDatiCatastali.size() > 0){
			for (int i=0; i<listaDatiCatastali.size(); i++){
				DatiCatastali datCat = (DatiCatastali) listaDatiCatastali.get(i);
				//out.println(listaDatiCatastali.get(i));
			%>
			<tr>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getFoglio());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getParticella());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getSubalterno());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getDataInizioVal());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getCategoria());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getClasse());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getConsistenza());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getRendita());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getSuperficeCatastale());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getZona());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getEdificio());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getScala());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getInterno());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getPiano());%></span></td>
				<td class="extWinTDData" style="width: 25%;"><span class="extWinTXTData"><% out.println(datCat.getIndirizzo());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getCivico());%></span></td>
			</tr>
			<%
				}
			}
			%>
		</table>
		<br>
		<span class="TXTmainLabel">Titolare selezionato</span>
		<table align=center class="extWinTable" style="width: 50%;">
			<tr>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Cod. Fisc.</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Cognome</span></td>
				<td class="extWinTDTitle" style="width: 4%;" ><span class="extWinTXTTitle">Nome</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Data Nascita</span></td>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Perc. Possesso</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Tipo Titolo</span></td>
			</tr>
			<%
			if (listaTitolarita != null && listaTitolarita.size() > 0){
				Iterator itTit = listaTitolarita.iterator(); 
				while (itTit.hasNext()){
					DatiTitolarita tit = (DatiTitolarita) itTit.next();
			%>
			<tr>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(tit.getCf());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(tit.getCognome());%></span></td>
				<td class="extWinTDData" style="width: 20%;"><span class="extWinTXTData"><% out.println(tit.getNome());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(tit.getDataNascita());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(tit.getPercentualePossesso());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(tit.getTipoTitolo());%></span></td>
			</tr>
			<%} %>
			<%
			}
			%>
		</table>
		<br>
		<span class="TXTmainLabel">Altre titolarità risultanti a catasto per la u.i. selezionata</span>
		<table align=center class="extWinTable" style="width: 50%;">
			<tr>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Cod. Fisc.</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Cognome</span></td>
				<td class="extWinTDTitle" style="width: 4%;" ><span class="extWinTXTTitle">Nome</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Data Nascita</span></td>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Perc. Possesso</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Tipo Titolo</span></td>
			</tr>
			<%
			if (listaAltreTitolarita != null && listaAltreTitolarita.size() > 0){
				Iterator itAlt = listaAltreTitolarita.iterator(); 
				while (itAlt.hasNext()){
					DatiTitolarita alt = (DatiTitolarita) itAlt.next();
			%>
			<tr>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(alt.getCf());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(alt.getCognome());%></span></td>
				<td class="extWinTDData" style="width: 20%;"><span class="extWinTXTData"><% out.println(alt.getNome());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(alt.getDataNascita());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(alt.getPercentualePossesso());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(alt.getTipoTitolo());%></span></td>
			</tr>
			<%} %>
			<%
			}
			%>
		</table>
		<br>
		<span class="TXTmainLabel">Dati del titolare risultanti in anagrafe</span>
		<table align=center class="extWinTable" style="width: 50%;">
			<tr>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Cod. Fisc.</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Cognome</span></td>
				<td class="extWinTDTitle" style="width: 4%;" ><span class="extWinTXTTitle">Nome</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Sesso</span></td>
				<td class="extWinTDTitle" style="width: 6%;" ><span class="extWinTXTTitle">Data Nascita</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Posiz. Anag.</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Civico</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Data Immigrazione</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Data Emigrazione</span></td>
				<td class="extWinTDTitle" style="width: 6%;"><span class="extWinTXTTitle">Data Morte</span></td>
			</tr>
			<%
			if (listaTitolare != null && listaTitolare.size() > 0){
				Iterator itPat = listaTitolare.iterator(); 
				while (itPat.hasNext()){
					PertinenzeAbitTitolare pat = (PertinenzeAbitTitolare) itPat.next();
			%>
			<tr>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(pat.getCf());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(pat.getCognome());%></span></td>
				<td class="extWinTDData" style="width: 20%;"><span class="extWinTXTData"><% out.println(pat.getNome());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(pat.getSesso());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(pat.getDataNascita());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(pat.getPosizioneAnagrafica());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(pat.getIndirizzo());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(pat.getCivico());%></span></td>
				<td class="extWinTDData" style="width: 20%;"><span class="extWinTXTData"><% out.println(pat.getDataImmigrazione());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(pat.getDataEmigrazione());%></span></td>
				<td class="extWinTDData" style="width: 16%;"><span class="extWinTXTData"><% out.println(pat.getDataMorte());%></span></td>
			</tr>
			<%} %>
			<%
			}
			%>
		</table>
		<br>
		<span class="TXTmainLabel">Il civico di residenza ricade nella pertinenza cartografica della u.i.</span>
		<table align=center class="extWinTable" style="width: 50%;">
			<tr>
				<td class="extWinTDTitle" style="width: 100%;" ><span class="extWinTXTTitle">Esito</span></td>
			</tr>
			<%
			if (listaCivicoInPertinenza != null && listaCivicoInPertinenza.size() > 0){
				Iterator itCip = listaCivicoInPertinenza.iterator(); 
				while (itCip.hasNext()){
					String outcome = (String) itCip.next();
			%>
			<tr>
				<td class="extWinTDData" style="width: 60%;"><span class="extWinTXTData"><% out.println(outcome);%></span></td>
			</tr>
			<%} %>
			<%
			}
			%>
		</table>
		<br>
		<span class="TXTmainLabel">U.I. residenziali nella pertinenza cartografica, di cui il titolare selezionato è proprietario</span>
		<table align=center class="extWinTable" style="width: 50%;">
			<tr>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Part.</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Sub.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Data Inizio Validita</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Categ.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Classe</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Cons.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Rend.</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Superf. Cat.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Zona</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Edif.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Scala</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Interno</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Piano</span></td>
				<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Civ.</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Per. Pos.</span></td>
				<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Tip. Titolo</span></td>
			</tr>
			<%
			if (listaUiRes != null && listaUiRes.size() > 0){
			for (int i=0; i<listaUiRes.size(); i++){
				PertinenzeAbitDatiCatastali datCat = (PertinenzeAbitDatiCatastali) listaUiRes.get(i);
				//out.println(listaDatiCatastali.get(i));
			%>
			<tr>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getFoglio());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getParticella());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getSubalterno());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getDataInizioVal());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getCategoria());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getClasse());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getConsistenza());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getRendita());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getSuperficeCatastale());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getZona());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getEdificio());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getScala());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getInterno());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getPiano());%></span></td>
				<td class="extWinTDData" style="width: 10%;"><span class="extWinTXTData"><% out.println(datCat.getIndirizzo());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getCivico());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getPercentualePossesso());%></span></td>
				<td class="extWinTDData" style="width: 10%;"><span class="extWinTXTData"><% out.println(datCat.getTipoTitolo());%></span></td>
			</tr>
			<%
				}
			}
			%>
		</table>
		<br>
		<span class="TXTmainLabel">U.I. di categoria C2, C6, C7 nella pertinenza cartografica, di cui il titolare selezionato è proprietario</span>
		<table align=center class="extWinTable" style="width: 50%;">
			<tr>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Part.</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Sub.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Data Inizio Validita</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Categ.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Classe</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Cons.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Rend.</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Superf. Cat.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Zona</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Edif.</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Scala</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Interno</span></td>
				<td class="extWinTDTitle" style="width: 5%;" ><span class="extWinTXTTitle">Piano</span></td>
				<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Civ.</span></td>
				<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Per. Pos.</span></td>
				<td class="extWinTDTitle" style="width: 10%;"><span class="extWinTXTTitle">Tip. Titolo</span></td>
			</tr>
			<%
			if (listaUiDiCat != null && listaUiDiCat.size() > 0){
			for (int i=0; i<listaUiDiCat.size(); i++){
				PertinenzeAbitDatiCatastali datCat = (PertinenzeAbitDatiCatastali) listaUiDiCat.get(i);
			%>
			<tr>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getFoglio());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getParticella());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getSubalterno());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getDataInizioVal());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getCategoria());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getClasse());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getConsistenza());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getRendita());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getSuperficeCatastale());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getZona());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getEdificio());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getScala());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getInterno());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getPiano());%></span></td>
				<td class="extWinTDData" style="width: 10%;"><span class="extWinTXTData"><% out.println(datCat.getIndirizzo());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getCivico());%></span></td>
				<td class="extWinTDData" style="width: 5%;"><span class="extWinTXTData"><% out.println(datCat.getPercentualePossesso());%></span></td>
				<td class="extWinTDData" style="width: 10%;"><span class="extWinTXTData"><% out.println(datCat.getTipoTitolo());%></span></td>
			</tr>
			<%
				}
			}
			%>
		</table>







		
		
		</td>
		<%
			if (false && vctLink != null && vctLink.size() > 0)
			{
		%>
		<td class="iFrameLink"><jsp:include
			page="../frame/iFrameLink.jsp"></jsp:include></td>
		<%
		}
		%>
	</tr>
</table>
<br>
<br>
<br>

<center><span class="extWinTXTTitle"><a
	href="javascript:"></a></span></center>

<br>
<br>

<!-- FINE solo dettaglio --> <%
 	//if (lc != null)
 	//{
 %> <%
 		//String codice = "";
 		//codice = lc.getChiave();
 %> <input type='hidden' name="OGGETTO_SEL" id="OGGETTO_SEL" value="<%//out.println(codice);%>">
<%
//}
%> <%
 	if (finder != null)
 	{
 %> <input type='hidden' name="ACT_PAGE"
	value="<%=finder.getPaginaAttuale()%>"> <%
 	}
 	else
 	{
 %> <input type='hidden' name="ACT_PAGE" value=""> <%
 }
 %> <input type='hidden' name="AZIONE" value=""> <input
	type='hidden' name="ST" value=""> <input type='hidden'
	name="UC" value="106"> <input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>"></form>


<div id="wait" class="cursorWait" />
</body>
</html>
