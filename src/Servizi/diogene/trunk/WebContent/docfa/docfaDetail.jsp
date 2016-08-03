<%@ page language="java"
	import="java.util.*,it.escsolution.escwebgis.docfa.bean.*,
			it.escsolution.escwebgis.docfa.servlet.*,it.escsolution.escwebgis.docfa.logic.*, 
			it.escsolution.eiv.database.*,
			it.webred.ct.data.access.basic.docfa.dto.*, it.webred.ct.data.access.basic.catasto.dto.*,
			it.webred.ct.data.model.docfa.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%
HttpSession sessione = request.getSession(true);

Docfa lc = (Docfa) sessione.getAttribute(DocfaLogic.DOCFA);

String funzionalita=(String)sessione.getAttribute("FUNZIONALITA"); 

ArrayList listaDocfaAnnotazioni = (ArrayList) sessione.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_ANNOTAZIONI);

ArrayList listaDocfabeniNonCens = (ArrayList) sessione.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_BENI_NON_CENS);

ArrayList listaDocfaDatiCensuari = (ArrayList) sessione.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_DATI_CENSUARI);

ArrayList listaDocfaIntestati = (ArrayList) sessione.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_INTESTATI);

ArrayList listaDocfaUiu = (ArrayList) sessione.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_UIU);

ArrayList listaDocfaDichiaranti = (ArrayList) sessione.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_DICHIARANTI);

ArrayList listaDocfaParteUno = (ArrayList) sessione.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_PARTE_UNO);

ArrayList listaDocfaOperatore= (ArrayList) sessione.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_OPER);

	String ST = (String) sessione.getAttribute("ST");
	int st = new Integer(ST).intValue();

	DocfaFinder finder = null;

	if (sessione.getAttribute(DocfaServlet.NOMEFINDER) != null)
	{
		if (((Object) sessione.getAttribute(DocfaServlet.NOMEFINDER)).getClass() == new DocfaFinder().getClass())
		{
			finder = (DocfaFinder) sessione.getAttribute(DocfaServlet.NOMEFINDER);
		}

	}

	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT") != null)
		js_back = ((Integer) sessione.getAttribute("BACK_JS_COUNT")).intValue();

	Vector vctLink = null;
	if (sessione.getAttribute("LISTA_INTERFACCE") != null)
	{
		vctLink = ((Vector) sessione.getAttribute("LISTA_INTERFACCE"));
	}
	String dataAggiornamento="";
	
	Fonti fonti= (Fonti)sessione.getAttribute("appFonti");
	if (fonti != null){
		ArrayList<FonteNew> arrFonte= fonti.getFonti(request);
		if (arrFonte != null){
			for (FonteNew fonte: arrFonte){
				String nomeFonte= fonte.getDescrizione();
				if (nomeFonte!= null && nomeFonte.toUpperCase().equals("CATASTO")){
					 dataAggiornamento=fonte.getDataAggiornamento();
				}
			}
		}
	}
	
%>




<%@page import="it.webred.utils.DateFormat"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<html>
<head>
<title>Docfa - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= request.getContextPath() %>/styles/style.css"
	type="text/css">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script> 

</head>
<script>

function apriPopup3DProspective(f,p,cod_ente)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popup3DProspective=true&f='+f+'&p='+p+'&cod_ente='+cod_ente;
			var finestra=window.open(url); // ,"","height=400,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

function mettiST(){
	document.mainform.ST.value = 3;
}
function apriPopupCens(codice,im,f,m,s)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupCens=true&codice='+codice+'&im='+im+'&f='+f+'&m='+m+'&s='+s;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
function apriPopupPrg(ente,fg,part)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupPrg=true&cod_ente='+ente+'&fg='+fg+'&part='+part;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

function apriPopupConcessioni(ente,fg,part)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupConcessioni=true&cod_ente='+ente+'&fg='+fg+'&part='+part;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

function apriPopupConcessioniStorico(fg,part)
{
			var url = '<%= request.getContextPath() %>/StoricoConcessioni?UC=53&ST=2&popup=true&fg='+fg+'&part='+part;
			var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

function apriPopupGraf(fg,part,sub)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupGraf=true&fg='+fg+'&part='+part+'&sub='+sub;
			var finestra=window.open(url,"_dati","height=200,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
function apriClasseAttesa(valore,vani,zona,foglio,particella,sub,rapportovalore,consistenza,categoria,classe,rapporto)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupClasseAtt=true&valore='+valore+'&vani='+vani+'&zona='+zona+'&foglio='+foglio+'&particella='+particella+'&sub='+sub+'&rapportovalore='+rapportovalore+'&consistenza='+consistenza+'&categoria='+categoria+'&classe='+classe+'&rapporto=' +rapporto;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");			
			finestra.focus();
}
function apriAltreZc(protocollo,fornitura,foglio,particella,sub)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupLstAltreZc=true&protocollo='+protocollo+'&fornitura='+fornitura+'&foglio='+foglio+'&particella='+particella+'&sub='+sub;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");			
			finestra.focus();
			
}
function apriCategoriaClasseInParticella()
{
			var OGGETTO_SEL = document.getElementById("OGGETTO_SEL").value;
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupCategoriaClasseInParticella=true&OGGETTO_SEL='+OGGETTO_SEL;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");			
			finestra.focus();
}
function apri340(f,m,s)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popup340=true&f='+f+'&m='+m+'&s='+s;
			var finestra=window.open(url,"_dati","height=250,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
function apriPopupCatasto(f,p,s,d,cod_ente)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupCatasto=true&f='+f+'&p='+p+'&s='+s+'&d='+d+'&cod_ente='+cod_ente;
			var finestra=window.open(url,"","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

function vaiListaCondoni(foglio, mappale, sub) {
	var url = '<%= request.getContextPath() %>/Condono?UC=39&popupACondono=true&foglio='+foglio+'&mappale='+mappale+'&sub='+sub;
	var finestra=window.open(url, 'condoniCollegati', 'top=100,left=100,height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no');		
	finestra.focus();
}

</script>
<body >

<div align="center" class="extWinTDTitle"><span
	class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Docfa</span></div>
	
&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= request.getContextPath() %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= request.getContextPath() %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= request.getContextPath() %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= request.getContextPath() %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= request.getContextPath() %>/images/print.gif')" onClick="Stampa()">
<br/>

<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

<form name="mainform"
	action="<%= request.getContextPath() %>/Docfa"
	target="_parent">


<table style="background-color: white; width: 100%;">
	<tr style="background-color: white;">
		<td
			style="background-color: white; vertical-align: top;  width: 51%;">

		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
			<tr>
				<td colspan=1>
				<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="4" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">DATI GENERALI</span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Protocollo</span></td>
						<td colspan="3" class="TDviewTextBox"><span class="TXTviewTextBox"><%=lc.getProtocollo()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Fornitura</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= DateFormat.dateToString(DateFormat.stringToDate(lc.getFornitura(),"dd/MM/yyyy"),"MM-yyyy")%></span></td>

						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Data variazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lc.getDataVariazione()%></span></td>
					</tr>					
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Causale</span></td>
						<td colspan="3" class="TDviewTextBox"><span
							class="TXTviewTextBox"><%=lc.getCausale()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Sopressione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lc.getSoppressione()%></span></td>

						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Variazione</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lc.getVariazione()%></span></td>
					</tr>
					<tr>
						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Nuove</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lc.getCostituzione()%></span></td>

						<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span
							class="TXTmainLabel">Deriv spe.</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lc.getDerivSpe()%></span></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<br>
		<br>
		
		<table align=center cellpadding="0" cellspacing="0"
			class="editExtTable" style="background-color: #C0C0C0;">
			<tr>
				<td colspan=1>
				<table class="viewExtTable" style="width: 100%;">
					<tr>
						<td colspan="4" class="TDmainLabel"
							style="width: 5%; white-space: nowrap;">
							<a href="<%= request.getContextPath()%>/DocfaImmaginiPlanimetrie?protocollo=<%=lc.getProtocollo()%>&fornitura=<%=lc.getFornitura()%>&idFunz=2">
							<span class="TXTmainLabel"> 
								<img height="30px" src="<%=request.getContextPath()%>/images/pdficon.jpg"/>
								Dati docfa in formato Pdf
							</span>
							</a>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>

		<br>
		<br>
		
		<div class="tabber">

	<% if (listaDocfaParteUno != null && listaDocfaParteUno.size()>0) {%>

		<div class="tabbertab">
		
		<!-- <span class="TXTmainLabel">Riferimenti Temporali Fabbricato</span> -->
		<h2>Riferimenti Temporali Fabbricato</h2>
		
		<table align=left class="extWinTable" style="width: 80%;">
			<tr>&nbsp;</tr>
		    <tr class="extWinTXTTitle">Riferimenti Temporali Fabbricato</tr>
			<tr>
				<td class="extWinTDTitle" style="width: 50%;" ><span class="extWinTXTTitle">Anno Costruzione</span></td>
				<td class="extWinTDTitle" style="width: 50%;"><span class="extWinTXTTitle">Anno Ristrutturazione Totale</span></td>
			</tr>
			<%
				java.util.Iterator it = listaDocfaParteUno.iterator(); 
				while (it.hasNext())
				{
					DocfaInParteUnoDTO parteUnoTesta = (DocfaInParteUnoDTO) it.next();
					DocfaInParteUno jpa = parteUnoTesta.getDocfaInParteUno();
			%>
			<tr>
				<td class="extWinTDData" style="width: 50%;"><span class="extWinTXTData">
				<%if (jpa.getAnnoCostru()!=null && jpa.getAnnoCostru().toString().equals("1900")) {%>
				antec. 1942
				<%}else {%>
				<%=jpa.getAnnoCostru()%>
				<%}%>
				</span></td>
				<td class="extWinTDData" style="width: 50%;"><span class="extWinTXTData">
				<%if (jpa.getAnnoRistru()!=null && !jpa.getAnnoRistru().toString().equals("0")) {%>
					<%=jpa.getAnnoRistru()%>
				<%}%>	
				</span></td>
			</tr>
			<%
			}
			%>
		</table>
		<br>
		<br>
	
	</div>
	
	<% } %>
	
	<% if (listaDocfaIntestati != null && listaDocfaIntestati.size()>0) {%>
	
	<div class="tabbertab">
		<!-- <span class="TXTmainLabel">Lista Intestati</span> -->
		<h2>Lista Intestati</h2>
		<table align=left class="extWinTable" style="width: 100%;">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Lista Intestati</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita IVA</span></td>
			</tr>
			<%
				Iterator it = listaDocfaIntestati.iterator();
				while (it.hasNext())
				{
					DocfaIntestati inte = (DocfaIntestati) it.next();
			%>
			<tr>
				<td class="extWinTDData" >
					<span class="extWinTXTData"><%=inte.getDenominazione()!=null ? inte.getDenominazione() : inte.getCognome()+" "+inte.getNome() %></span>
				</td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=inte.getCodiceFiscale()!=null ? inte.getCodiceFiscale() :"-"%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=inte.getPartitaIva()!=null ? inte.getPartitaIva() : "-"%></span></td>
			</tr>
			<%
			}
			%>
		</table>
		<br>
		<br>
</div>
<% } %>

<% if (listaDocfaDichiaranti != null && listaDocfaDichiaranti.size()>0) {%>
	
	<div class="tabbertab">
		<h2>Lista Dichiaranti</h2>
		<table align=left class="extWinTable" style="width: 100%;">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Lista Dichiaranti</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Luogo</span></td>
			</tr>
			<%
			
				Iterator it = listaDocfaDichiaranti.iterator();
				while (it.hasNext())
				{
					Docfa dic = (Docfa) it.next();
			%>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dic.getCognome()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dic.getNome()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dic.getIndirizzoDichiarante()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dic.getLuogo()%></span></td>
			</tr>
			<%
			}
			%>
		</table>			
		<br>
		<br>
	</div>
	
	<% } %>
	
	<% if (listaDocfaUiu != null && listaDocfaUiu.size()>0) {%>
	
	<div class="tabbertab">
		<h2>Lista Uiu</h2>
		<table align=left class="extWinTable" style="width: 100%;">
		<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Lista Uiu</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Graffati</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PRG</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Concessioni</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Condoni</span></td>
				<td COLSPAN=4 class="extWinTDTitle"><span class="extWinTXTTitle">Mappa</span></td>
			<!-- <td class="extWinTDTitle"><span class="extWinTXTTitle">Mappa<BR>Prospettica</span></td>  -->
			</tr>
			<%
				Iterator it = listaDocfaUiu.iterator();
				while (it.hasNext())
				{
					Docfa uiu = (Docfa) it.next();
			%>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData">
				<%if (uiu.getPresenzaGraffati().equals("-")){ %>
				<%=uiu.getPresenzaGraffati()%>
				<%}else{ %>
				<a href="javascript:apriPopupGraf('<%=uiu.getFoglio()%>','<%=uiu.getParticella()%>','<%=uiu.getSubalterno()%>');"><%=uiu.getPresenzaGraffati()%></a>
				<%} %>
				</span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=uiu.getTipo()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=uiu.getFoglio()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=uiu.getParticella()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><a href="javascript:apri340('<%=uiu.getFoglio()%>','<%=uiu.getParticella()%>','<%=uiu.getSubalterno()%>');"><%=uiu.getSubalterno()%></a></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=uiu.getIndirizzoDichiarante()%></span></td>
				<td class="extWinTDData" style="text-align:center;"><span class="extWinTXTData" >
					<a href="javascript:apriPopupPrg('<%=lc.getCodEnte()%>','<%=uiu.getFoglio()%>','<%=uiu.getParticella()%>');">
					<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
					</a></span>
				</td>
				<td class="extWinTDData" style="text-align:center;">
					<span class="extWinTXTData" >
						<!-- il primo link non è più visibile -->
						<a style="text-decoration: none; display: none;" href="javascript:apriPopupConcessioni('<%=lc.getCodEnte()%>','<%=uiu.getFoglio()%>','<%=uiu.getParticella()%>');">
							<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
						</a>
						<a style="text-decoration: none;" href="javascript:apriPopupConcessioniStorico('<%=uiu.getFoglio()%>','<%=uiu.getParticella()%>');">
							<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
						</a>
					</span>
				</td>
				<td class="extWinTDData" style="text-align:center;"><span class="extWinTXTData" >
					<a href="javascript:vaiListaCondoni('<%=uiu.getFoglio()%>', '<%=uiu.getParticella()%>', '<%=uiu.getSubalterno()%>');">
					<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
					</a></span>
				</td>
				<td class="extWinTDData" style="text-align:center;">
					<span class="extWinTXTData">
					<a href="javascript:zoomInMappaParticelle('<%= lc.getCodEnte() %>','<%=uiu.getFoglio()%>','<%=uiu.getParticella()%>');">
					<img src="<%=request.getContextPath()%>/ewg/images/Localizza.gif" border="0" />
					</a>
					</span>
				</td>
				<td onclick="javascript:apriPopupVirtualH(<%=uiu.getCoord()==null?0:uiu.getCoord().firstObj%>,<%=uiu.getCoord()==null?0:uiu.getCoord().secondObj%>);" class="extWinTDData" style="text-align:center;">
					<span class="extWinTXTData">
					<img src="<%=request.getContextPath()%>/ewg/images/3D.gif" border="0" width="24" height="30" />
					</a>
					</span>
				</td>
				<td onclick="javascript:apriPopupStreetview(<%=uiu.getCoord()==null?0:uiu.getCoord().firstObj%>,<%=uiu.getCoord()==null?0:uiu.getCoord().secondObj%>);" class="extWinTDData" style="text-align:center;">
					<span class="extWinTXTData">
					<img src="<%=request.getContextPath()%>/ewg/images/streetview.gif" border="0" width="17" height="32" />
					</a>
					</span>
				</td>
				<td class="extWinTDData" style="text-align:center;">
					<span class="extWinTXTData">
					<a href="javascript:apriPopup3DProspective('<%=uiu.getFoglio()%>','<%=uiu.getParticella()%>','<%=lc.getCodEnte()%>');">
					<img src="<%=request.getContextPath()%>/ewg/images/3dprospective.png" border="0" width="30" height="30" />
					</a>
					</span>
				</td>	
			</tr>
			<%
			}
			%>
		</table>
		<br>
		<br>
		
		</div>
		
		<%} %>
		
		<% if (listaDocfabeniNonCens != null && listaDocfabeniNonCens.size()>0) {%>
	
	<div class="tabbertab">
		<h2>Beni Comuni Non Censibili</h2>
		
		<table align=left class="extWinTable" style="width: 100%;">
		<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Beni Comuni Non Censibili</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio 2</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella 2</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub. 2</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio 3</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella 3</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub. 3</span></td>									
			</tr>
			<%
				Iterator it = listaDocfabeniNonCens.iterator();
				while (it.hasNext())
				{
					BeniNonCensDTO bnc = (BeniNonCensDTO) it.next();
					
					ParametriCatastaliDTO p1 = bnc.getParams01();
					ParametriCatastaliDTO p2 = bnc.getParams02();
					ParametriCatastaliDTO p3 = bnc.getParams03();
				%>
				<tr>
	
					<td class="extWinTDData"><span class="extWinTXTData"><%=p1.getFoglio()!=null ? p1.getFoglio() : "-"%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=p1.getParticella()!=null ? p1.getParticella() : "-"%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=p1.getSubalterno()!=null ? p1.getSubalterno() : "-"%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=p2.getFoglio()!=null ? p2.getFoglio() : "-"%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=p2.getParticella()!=null ? p2.getParticella() : "-"%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=p2.getSubalterno()!=null ? p2.getSubalterno() : "-"%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=p3.getFoglio()!=null ? p3.getFoglio() : "-"%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=p3.getParticella()!=null ? p3.getParticella() : "-"%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=p3.getSubalterno()!=null ? p3.getSubalterno() : "-"%></span></td>				
				</tr>
				<%
				}
				%>
		</table>
		<br>
		<br>
		
		</div>
		
		<%} %>
		
		
		<% if (listaDocfaAnnotazioni != null && listaDocfaAnnotazioni.size()>0) {%>
	
	<div class="tabbertab">
		<h2>Annotazioni</h2>

		<table align=left class="extWinTable" style="width: 100%;">
		<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Annotazioni</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Annotazioni</span></td>								
			</tr>
			<%
				Iterator it = listaDocfaAnnotazioni.iterator();
				while (it.hasNext())
				{
					DocfaAnnotazioni an = (DocfaAnnotazioni) it.next();
				%>
				<tr>
					<td class="extWinTDData"><span class="extWinTXTData"><%=an.getAnnotazioni()%></span></td>				
				</tr>
				<%
				}
				%>
		</table>	
		<br>
		<br>
		
		</div>
		
		<% } %>


	<% if (listaDocfaDatiCensuari != null && listaDocfaDatiCensuari.size()>0) {%>
	
	<div class="tabbertab">
		<h2>Dati Censuari</h2>
		<div style="float:left; width: 100%;" >
		<table align=left class="extWinTable" style="width: 100%;">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Dati Censuari</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Graffati</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Zona</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cat.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Clas.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Consistenza</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Ident. Imm.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Presenza nel DB Catasto al <%=dataAggiornamento %> </span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Registrazione</span></td>
			</tr>
			<%
				Iterator it = listaDocfaDatiCensuari.iterator();
				while (it.hasNext())
				{
					Docfa dc = (Docfa) it.next();
					RenditaDocfa rendita = dc.getRendDocfa(); 
			%>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData">
				<%if (dc.getPresenzaGraffati().equals("-")){ %>
				<%=dc.getPresenzaGraffati()%>
				<%}else{ %>
				<a href="javascript:apriPopupGraf('<%=dc.getFoglio()%>','<%=dc.getParticella()%>','<%=dc.getSubalterno()%>');"><%=dc.getPresenzaGraffati()%></a>
				<%} %>
				</span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getFoglio()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getParticella()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=(dc.getSubalterno().equals("-")?" ":dc.getSubalterno())%></span></td>				
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getZona()%></span></td>				
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getCategoria()%></span></td>
				<td class="extWinTDData" nowrap><span class="extWinTXTData"><%=dc.getClasse()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getConsistenza()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getSuperfice()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getRendita()%></span></td> 
				<td class="extWinTDData"><span class="extWinTXTData"><a href="javascript:apriPopupCens('<%=lc.getChiave()%>','<%=dc.getIdentificativoImm()%>','<%=dc.getFoglio()%>','<%=dc.getParticella()%>','<%=dc.getSubalterno()%>');"><%=dc.getIdentificativoImm()%></a></span></td> 
				<!-- MB -->
				<td class="extWinTDData" align="center"><span class="extWinTXTData">
				<% if (!rendita.getAssenzaCatasto().equals("")){%>
					<img  src="<%=request.getContextPath()%>/images/alert.gif" border="0" height="18" width="18" alt="<%=rendita.getAssenzaCatasto()%>"  >		
				<% }else{%>
					<a href="javascript:apriPopupCatasto('<%=dc.getFoglio()%>','<%=dc.getParticella()%>','<%=dc.getSubalterno()%>','<%=rendita.getDataValiditaCatasto()%>','<%=lc.getCodEnte()%>');">
					<img  src="<%=request.getContextPath()%>/images/ok.gif" border="0"  alt="u.i.u. presente nel DB Catasto"  >
					</a>		
				<% }%>	
				</span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getDataRegistrazione()%></span></td> 
			</tr>
			<%
			}
			%>
		</table>
		</div>
		
		<br/>
		<br/>
		<br/>
		
		<div style="float:left; width: 100%;" >
		<table align=left class="extWinTable" style="width: 100%;">
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Graffati</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Rend. DOCFAx100</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Rend. DOCFA aggiornata (+5%)</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Valore Commerciale</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Rapporto Val.Comm./Rend. DOCFAx100</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Rapporto Val.Comm./Rend. DOCFA aggiornata</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cat. / Classe compatibile</span></td>
			</tr>
			<%
				it = listaDocfaDatiCensuari.iterator();
				while (it.hasNext())
				{
					Docfa dc = (Docfa) it.next();
					RenditaDocfa rendita = dc.getRendDocfa(); 
			%>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData">
				<%if (dc.getPresenzaGraffati().equals("-")){ %>
				<%=dc.getPresenzaGraffati()%>
				<%}else{ %>
				<a href="javascript:apriPopupGraf('<%=dc.getFoglio()%>','<%=dc.getParticella()%>','<%=dc.getSubalterno()%>');"><%=dc.getPresenzaGraffati()%></a>
				<%} %>
				</span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getFoglio()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getParticella()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=(dc.getSubalterno().equals("-")?" ":dc.getSubalterno())%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=rendita.getRenditaDocfaX100()!=null?rendita.getRenditaDocfaX100():""%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=rendita.getRenditaDocfa5()!=null?rendita.getRenditaDocfa5():"" %></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=rendita.getValoreCommerciale()%></span></td>				
				<td class="extWinTDData" >
				<span class="extWinTXTData" <%if(rendita.isAnomaliaControllox100()){ %> style="color:red;" <%} %>>
					<%=rendita.getRapportoValorex100()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData" <%if(rendita.isAnomaliaControllo()){ %> style="color:red;" <%} %>>
					<%=rendita.getRapportoValore()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData">
						<%if(dc.getCategoria() != null && dc.getCategoria().toLowerCase().startsWith("a") && rendita.getValoreCommerciale() != null && !rendita.getValoreCommerciale().trim().equals("") &&  rendita.getRenditaDocfaX100() != null && !rendita.getRenditaDocfaX100().trim().equals("") && !rendita.getRenditaDocfaX100().trim().equals("0,00")){%>						
						<a href="javascript:apriClasseAttesa('<%=rendita.getValoreCommerciale()%>','<%=dc.getConsistenza()%>','<%=dc.getZona()%>','<%=dc.getFoglio()%>','<%=dc.getParticella()%>','<%=dc.getSubalterno()%>','<%=rendita.getRapportoValore()%>','<%=dc.getConsistenza()%>','<%=dc.getCategoria()%>','<%=dc.getClasse()%>','<%=rendita.getRapporto()%>');">visualizza</a>
						<% }%>
						<%if(dc.getConCls()!=null &&  dc.getConCls().getClassamentoCompatibile().booleanValue()==true && dc.getConCls().isErroreZoneOmi()){%>						
						<span class="extWinTXTData" style="color:red;"><%=dc.getConCls().getMsgZoneOmi()%></span><br/>
						
						<%if(dc.getConCls().getLstPerZcDiverse().size()>0)%>						
						<a href="javascript:apriAltreZc('<%=dc.getProtocollo()%>','<%=dc.getFornitura()%>','<%=dc.getFoglio()%>','<%=dc.getParticella()%>','<%=dc.getSubalterno()%>');">
							Visualizza per le altre zone associate al foglio <%=dc.getFoglio()%></a>
						<% }%>
						<%if(dc.getConCls()!=null &&  dc.getConCls().getClassamentoCompatibile().booleanValue()==false){%>						
							<span class="extWinTXTData" >Nessun Classamento Compatibile</span>
						<%} %>
				</span></td> 
			</tr>
			<%
			}
			%>
		</table>	
		</div>
		<br/>
		<br/>
		<div style="float:left; width: 100%;" >
		  <span class="TXTmainLabel"><a href="javascript:apriCategoriaClasseInParticella();">Visualizza Categorie Classi dei subalterni presenti nella particella</a></span>
		</div>
	</div>
		
		<% } %>
		
		
			
			<% if (listaDocfaParteUno != null && listaDocfaParteUno.size()>0) {%>
	
		<div class="tabbertab">	
		<h2>Lista Parte Uno</h2>
		<table align=left class="extWinTable" style="width: 100%;">
		<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Lista Parte Uno</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particelle</span></td>
			<!-- 	<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td> -->
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Cost.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Rist.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Posiz. Fab.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Acces.</span></td>
			</tr>
			<%
				Iterator it = listaDocfaParteUno.iterator();
				while (it.hasNext())
				{
					DocfaInParteUnoDTO dto = (DocfaInParteUnoDTO) it.next();
					DocfaInParteUno parteUno = dto.getDocfaInParteUno();
			%>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData"><%=parteUno.getCuFoglio()!=null ? parteUno.getCuFoglio() : "-"%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=parteUno.getCuNumero()!=null ? parteUno.getCuNumero() : "-"%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=parteUno.getAnnoCostru()!=null ? parteUno.getAnnoCostru() : "-"%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=parteUno.getAnnoRistru()!=null ? parteUno.getAnnoRistru() : "-" %></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dto.getDescrPosizFabb()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dto.getDescrTipoAcces()%></span></td>
			</tr>
			<%
			}
			%>
		</table>
		
		
		</div>
		
		<% } %>
		<%
		//dettaglio operatore
		if (listaDocfaOperatore != null && listaDocfaOperatore.size()>0){
		%>
		<br>
			<br>
			<div class="tabbertab">		
		<h2>Dettaglio Operatore</h2>
		<table align=center class="extWinTable" style="width: 100%;">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Dettaglio Operatore</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Operazione</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Note</span></td>
			</tr>
			<%Iterator it = listaDocfaOperatore.iterator();
			while (it.hasNext())
			{
				OperatoreDTO oper = (OperatoreDTO) it.next(); %>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData"><%=oper.getCf()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=oper.getTipo()%></span></td>
			<td class="extWinTDData"><span class="extWinTXTData"><%=oper.getNote()%></span></td>
			</tr>
			<%
			}
			%>
		</table>
		
		</div>	
		<%
		}
			
		%>	

		</td>
		
	</tr>
</table>
<br>
<br>

<center><span class="extWinTXTTitle"><a
	href="javascript:"></a></span></center>

<br>

<% if (request.getParameter("popupDaCondonoDett") != null && new Boolean(request.getParameter("popupDaCondonoDett")).booleanValue()) { %>
	<div align="center"><span class="extWinTXTTitle">
		<%  String href = EscServlet.URL_PATH + "/Docfa?ST=2&popupDaCondono=true&foglio=" 
		+ request.getParameter("foglio") + "&mappale=" + request.getParameter("mappale") 
		+ "&sub=" + request.getParameter("sub"); %>
		<a class="iFrameLink" href="<%= href %>">torna alla lista</a></span>
	</div>
<% } %>

<!-- FINE solo dettaglio --> <%
 	if (lc != null)
 	{
 %> <%
 		String codice = "";
 		codice = lc.getChiave();
 %> <input type='hidden' name="OGGETTO_SEL" id="OGGETTO_SEL" value="<%=codice%>">
<%
}
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
	name="UC" value="43"> <input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>"></form>


<div id="wait" class="cursorWait" />
</body>
</html>
