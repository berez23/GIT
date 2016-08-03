<%@ page language="java" import="it.escsolution.escwebgis.tributi.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   OggettiTARSU tarsu=(OggettiTARSU)sessione.getAttribute("TARSU"); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.tributi.bean.OggettiTARSUFinder finder = null;

	if (sessione.getAttribute("FINDER7") != null){ 
		finder = (it.escsolution.escwebgis.tributi.bean.OggettiTARSUFinder)sessione.getAttribute("FINDER7"); 
	}
 Contribuente cont=(Contribuente)sessione.getAttribute("CONTRIBUENTE_TARSU"); 
 
 int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
	
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.escsolution.escwebgis.docfa.bean.Docfa"%>
<%@page import="it.escsolution.escwebgis.tributi.logic.TributiOggettiTARSULogic"%>
<%@page import="it.escsolution.escwebgis.catasto.logic.CatastoPlanimetrieComma340Logic"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<html>
<head>
<title>Tributi Oggetto TARSU - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>


function mettiST(){
	document.mainform.ST.value = 3;
}
function visualizzaContribuente(){
	wait();
	document.mainform.action="<%= request.getContextPath() %>/TributiContribuenti";		
	document.mainform.ST.value=5;
	document.mainform.UC.value=5;
	document.mainform.EXT.value = 1;
	document.mainform.submit();
}
function apriPopupCens(codice,im,f,m,s)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupCens=true&codice='+codice+'&im='+im+'&f='+f+'&m='+m+'&s='+s;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
function dettaglioDocfa(chiave)
{
		var pop = window.open('<%= request.getContextPath() %>/Docfa?UC=43&ST=3&OGGETTO_SEL='+chiave+'&EXT=1&NONAV=1');
		pop.focus();
}

function apriPopupTitolaritaF(codFiscale, nome, cognome, dataNascita)
{

			var url = '<%= request.getContextPath() %>/CatastoIntestatariF?UC=3&ST=5&popup=true&COD_FISCALE='+codFiscale+'&NOME='+nome+'&COGNOME='+cognome+'&DATA_NASCITA='+dataNascita;
			var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

function apriPopupTitolaritaG(partitaIva)
{

			var url = '<%= request.getContextPath() %>/CatastoIntestatariG?UC=4&ST=5&popup=true&PARTITA_IVA='+partitaIva;
			var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= request.getContextPath() %>/TributiOggettiTARSU" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dati Oggetto TARSU</span>
</div>

&nbsp;



<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getComune()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>	
		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Foglio</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getFoglio()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Particella</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getParCatastali()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Subalterno</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getSubalterno()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	
		
</table>
	
&nbsp;
	
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Garage</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getGarage()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Fondo</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getFondo()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Soffitta</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getSoffitta()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>		
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Commerciale</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getCommerciale()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Artigianale</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getArtigianale()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Produttivo</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getProduttivo()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Servizi</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getServizi()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Accessori</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getAccessori()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Abitazione</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getAbitazione()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>

		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sup. Totale</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=tarsu.getSupTotale()%></span></td>
			</tr>
		</table>
		</td>
	</tr>	
	
	<tr>
		<td  colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td colspan=3 class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=tarsu.getIndirizzo()%></span></td>
			</tr>
		</table>
		</td>
	</tr>		
</table>

</td>
</tr>
</table>

<div class="tabber">
<% 
ArrayList lvani = (ArrayList)sessione.getAttribute("TARSU_DETTAGLIO_VANI");

if (lvani != null && lvani.size() > 0) { %>

<div class="tabbertab">
		<h2>Dettaglio superfici per vano (scarico comma 340)</h2>
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">
					Dettaglio superfici per vano (scarico comma 340)
			</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Vano<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Piano</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Edificio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Ambiente</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Altezza Min.<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Altezza Max.<span class="extWinTXTTitle"></td>
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
			<td colspan="4" class="extWinTDData"><span class="extWinTXTTitle" >Stima Sup.Calpestabile: supA + supB + supC - ingombro vani (20%)</span></td>
			<td colspan="3" class="extWinTDData"><span class="extWinTXTData"><%=tarsu.getSupVani()%></span></td>
			
		</tr>
		
		</table>
		
	</div>
<% }%>

<% 
	ArrayList planimetrieComma340 = (ArrayList)sessione.getAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_TARSU);
 if (planimetrieComma340 != null && planimetrieComma340.size() > 0) { 
%>

	 <div class="tabber">
	 <h2>Planimetrie comma 340</h2>
	 <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	 	
<%	
			Iterator it = planimetrieComma340.iterator();
			boolean sub = false;
			boolean sub9999 = false;
			while(it.hasNext())
			{
				ArrayList link = (ArrayList)it.next();
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
						href="<%= EscServlet.URL_PATH %>/CatastoPlanimetrieComma340?pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>&formato=<%=(String)link.get(3)%>">
						PDF
						</a>
						&nbsp;
						<a style="font-size: 12px;" 
						href="<%= EscServlet.URL_PATH %>/CatastoPlanimetrieComma340?openJpg=true&pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>&formato=<%=(String)link.get(3)%>">
						IMG
						</a>
					</td>
				</tr>
			<%}%>
			</table>
			</div>
	<% } %>	
		

<!-- FINE solo dettaglio -->

<% if (cont != null){%>

	<div class="tabbertab">
    <input type='hidden' name="OGGETTO_SEL" value="<%=tarsu.getCodSoggetto()%>|<%=tarsu.getProvenienza() %>">
<br>
<%-- 
<center><span class="extWinTXTTitle"><a href="javascript:visualizzaContribuente()">Visualizza il Contribuente associato</a></span></center>
--%>
<%-- 
TEST TEST MB
--%>
<h2>Contribuente associato</h2>
<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">
			Contribuente associato
	</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.CONTRIBUENTE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DENOMINAZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.FISCALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PART.IVA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TITOLARITA' CATASTO</span></td>
	</tr>
	<%int contatore=1; %>
	<tr id="r<%=contatore%>" >
		<td class="extWinTDData" onclick="visualizzaContribuente()" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor:pointer;'>
		<span class="extWinTXTData"><%=cont.getCodContribuente()%></span></td>
		<td class="extWinTDData" onclick="visualizzaContribuente()" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=cont.getProvenienza()%></span></td>
		<td class="extWinTDData" onclick="visualizzaContribuente()" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=cont.getCognome()%></span></td>	
		<td class="extWinTDData" onclick="visualizzaContribuente()" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=cont.getNome()%></span></td>		
		<td class="extWinTDData" onclick="visualizzaContribuente()" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=cont.getDenominazione()%></span></td>
		<td class="extWinTDData" onclick="visualizzaContribuente()" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=cont.getCodFiscale()%></span></td>
		<td class="extWinTDData" onclick="visualizzaContribuente()" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=cont.getPartitaIVA()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData">
			<% if (cont.getPartitaIVA() == null || cont.getPartitaIVA().equals("") || cont.getPartitaIVA().equals("-")){%>
			<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaF('<%=cont.getCodFiscale()%>','<%=cont.getNome().replace("'", "\\'")%>','<%=cont.getCognome().replace("'", "\\'")%>','<%=cont.getDataNascita()%>');">
				Vedi
			</a>
			<%} else { %>
			<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaG('<%=cont.getPartitaIVA()%>');">
				Vedi
			</a>
			<%} %>
			</span>
		</td>
	</tr>
</table>
</div>
<%-- 
FINE TEST TEST MB
--%>
<br>
<%}%>


<% 
ArrayList ldocfa = (ArrayList)sessione.getAttribute(TributiOggettiTARSULogic.TARSU_DOCFA_COLLEGATI);

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


<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>		

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="7">
		<input type='hidden' name="EXT" value="">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait" />
</body>
</html>