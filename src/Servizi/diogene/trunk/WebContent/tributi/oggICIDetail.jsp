<%@ page language="java" import="it.escsolution.escwebgis.tributi.bean.*,java.util.ArrayList,java.util.Iterator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  OggettiICI ici=(OggettiICI)sessione.getAttribute("ICI"); 
	ArrayList listaSoggetti = (ArrayList)sessione.getAttribute("CONTR_LIST"); 
%>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.tributi.bean.OggettiICIFinder finder = null;

	if (sessione.getAttribute("FINDER6") != null){ 
		finder = (it.escsolution.escwebgis.tributi.bean.OggettiICIFinder)sessione.getAttribute("FINDER6");
	}%>

	<% Contribuente cont=(Contribuente)sessione.getAttribute("CONTRIBUENTE_ICI"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();


java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<%@page import="it.escsolution.escwebgis.tributi.logic.TributiOggettiICILogic"%>
<%@page import="it.escsolution.escwebgis.docfa.bean.Docfa"%>
<html>
<head>
<title>Tributi Oggetto ICI - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>
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

function visualizzaContribuente(chiave){
	wait();
	document.mainform.action="<%= request.getContextPath() %>/TributiContribuenti?UC=5";	
	document.mainform.OGGETTO_SEL.value=chiave;	
	document.mainform.ST.value=5;
	//document.mainform.ST.value=3;
	document.mainform.EXT.value = 1;
	document.mainform.submit();
}

function mettiST(){
	document.mainform.ST.value = 3;
}

function chgtr(row,flg)
{
	return;
if (flg==1)
	{
	document.all("r"+row).style.backgroundColor = "#d7d7d7";
	}
else
	{
	document.all("r"+row).style.backgroundColor = "";
	}
}
function dettaglioDocfa(chiave)
{
		var pop = window.open('<%= request.getContextPath() %>/Docfa?UC=43&ST=3&OGGETTO_SEL='+chiave+'&EXT=1&NONAV=1');
		pop.focus();
}
</script>
<body >

<form name="mainform" action="<%= request.getContextPath() %>/TributiOggettiICI" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dati Oggetto ICI</span>
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
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ici.getComune()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>	
		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Foglio</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ici.getFoglioCatasto()%></span></td>
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
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ici.getParticellaCatasto()%></span></td>
			</tr>			
		</table>
		</td>
		
		<td width=10></td>

		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Subalterno</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ici.getSubalterno()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Superficie</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ici.getSuperficie()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Numero Vani</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ici.getVani()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Categoria Catastale</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ici.getCategoria()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Classe</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ici.getClasse()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
		<td  colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Partita</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ici.getPartita()%></span></td>
			</tr>
		</table>
		</td>
	</tr>	
	
	<tr>
		<td  colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Rendita Catastale</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ici.getRendita()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	
	<tr>
		<td  colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;" valign="top"><span class="TXTmainLabel">Abitazione Principale</span></td>
				<td colspan=3 class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox">
				<%= (ici.getAbitazionePrincipale())%>
				</span></td>
			</tr>
		</table>
		</td>
	</tr>
	
	
	<tr>
		<td  colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;" valign="top"><span class="TXTmainLabel">Indirizzo Oggetto ICI</span></td>
				<td colspan=3 class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ici.getIndirizzo()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	
	</table>
	
	&nbsp;
	
	<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
		<tr>
		<td  colspan=3>
		
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;" valign="top"><span class="TXTmainLabel">Indirizzo Catastale</span></td>
				<td colspan=3 class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ici.getIndirizzoCatastale()%></span></td>
			</tr>
		</table>		
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;" valign="top"><span class="TXTmainLabel">Indirizzo Viario</span></td>
				<td colspan=3 class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ici.getIndirizzoViarioRif()%></span></td>
			</tr>
		</table>	
			
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;" valign="top"><span class="TXTmainLabel">Localizza in Mappa</span></td>
				<!-- <td colspan=3 onclick="zoomInMappaParticelle('<%=ici.getCodEnte()%>','<%=ici.getFoglioCatasto()%>','<%=ici.getParticellaCatasto()%>');" class="TDviewTextBox" style="width:210;cursor: pointer;"><span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span></td> -->
				<td colspan=3  class="TDmainLabel" style="width:210;">
				<span class="TDmainLabel">
					
					<a style="text-decoration: none; " href="javascript:zoomInMappaParticelle('<%=ici.getCodEnte()%>','<%=ici.getFoglioCatasto()%>','<%=ici.getParticellaCatasto()%>');">
						<img src="<%=request.getContextPath()%>/ewg/images/Localizza.gif" border="0" />
					</a>
					
					<a href="javascript:apriPopupVirtualH(<%=ici.getLatitudine()==null?0:ici.getLatitudine()%>,<%=ici.getLongitudine()==null?0:ici.getLongitudine()%>);">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
							</a>
							
					<a href="javascript:apriPopupStreetview(<%=ici.getLatitudine()==null?0:ici.getLatitudine()%>,<%=ici.getLongitudine()==null?0:ici.getLongitudine()%>);">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
							</a>

					
					
				</span>
				</td>
			</tr>
		</table>

		<table align=center cellpadding="0" cellspacing="0" class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;" valign="top"><span class="TXTmainLabel">Concessioni Edilizie sulla particella</span></td>
				<td colspan=3  class="TDmainLabel" style="width:210;">
				<span class="TDmainLabel">
					<!-- il primo link non è più visibile -->
					<a style="text-decoration: none; display: none;" href="javascript:apriPopupConcessioni('<%=ici.getCodEnte()%>','<%=ici.getFoglioCatasto()%>','<%=ici.getParticellaCatasto()%>');">
						<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
					</a>
					<a style="text-decoration: none;" href="javascript:apriPopupConcessioniStorico('<%=ici.getFoglioCatasto()%>','<%=ici.getParticellaCatasto()%>');">
						<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
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


</td>
</tr>
</table>

	
<div class="tabber">

<%
	if (listaSoggetti != null && listaSoggetti.size() > 0)
	{
%>
	<div class="tabbertab">
	<h2>Storico Contribuenti Oggetto <%=ici.getFoglioCatasto() + " / " + ici.getParticellaCatasto() + " / " + ici.getSubalterno() %></h2>

<!-- FINE solo dettaglio -->

<table align=left width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">
			Storico Contribuenti Oggetto <%=ici.getFoglioCatasto() + " / " + ici.getParticellaCatasto() + " / " + ici.getSubalterno() %>
	</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nominativo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno denuncia</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Riferimento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero denuncia</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero riga</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Mesi possesso</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Quota possesso</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">poss. al 31/12</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO DEN.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolarità Catasto</span></td>
	</tr>
<%
	
		Iterator iter = listaSoggetti.iterator();
		int contatore = 1;
		int annoIci = 0;
		while (iter.hasNext())
		{
			Contribuente sogg = (Contribuente) iter.next();
		%>
		<% 
			int annoIciAttuale = sogg.getDenAnno()==null?0:sogg.getDenAnno().intValue();
			if (annoIci!=annoIciAttuale && contatore!=1) {
		  		
		%>  			
			    <tr>
			    		<td colspan="12">
						<span class="extWinTXTData">&nbsp;</span>
						</td>
			    </tr>
			
	  	<% 	
	  		}
			
			String piva= sogg.getPartitaIVA();
			
		%>

	<tr >
	    <td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getDenominazione() %></span>
		</td>
		<td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getProvenienza() %></span>
		</td>
	    <td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getDenAnno() %></span>
		</td>
	    <td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getDenRiferimento() %></span>
		</td>
	    <td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getDenNumero() %></span>
		</td>
	    <td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getNumRiga() %></span>
		</td>
	    <td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getDenMesiPossesso() %></span>
		</td>
		<td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getQuotaPossesso() %></span>
		</td>
		<td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getRenditaCatastale() %></span>
		</td>
		<td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getDicPosseduto() %></span>
		</td>
		<td onclick="visualizzaContribuente('<%=sogg.getCodContribuente()%>'+'|'+'<%=sogg.getProvenienza()%>')" class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= sogg.getDenTipo() %></span>
		</td>
		<td class="extWinTDData" class="extWinTDTitle" >
			
			<span class="extWinTXTData">
			<% if (piva == null || piva.equals("")){%>
			<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaF('<%=sogg.getCodFiscale()%>','<%=sogg.getNome().replace("'", "\\'")%>','<%=sogg.getCognome().replace("'", "\\'")%>','<%=sogg.getDataNascita()%>');">
				Vedi
			</a>
			<%} else { %>
			<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaG('<%=sogg.getPartitaIVA()%>');">
				Vedi
			</a>
			<%} %>
			</span>
			
		</td>
	</tr>
	<% 
		contatore++;
		annoIci = annoIciAttuale;
  	} 
  	%>
	<input type='hidden' name="OGGETTO_SEL" value="">

</table>

</div>

<%} %>

<% 
ArrayList ldocfa = (ArrayList)sessione.getAttribute(TributiOggettiICILogic.ICI_DOCFA_COLLEGATI);

if (ldocfa != null && ldocfa.size() > 0) { %>

<div class="tabbertab">
		<h2>Dati censuari DOCFA collegati</h2>
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0" align=center>
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
			<% 
			Iterator it = ldocfa.iterator();			
			while(it.hasNext())
			{
				Docfa docfa = (Docfa)it.next();
			%>
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
<% }%>

</div>





		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="6">
		<input type='hidden' name="EXT" value="">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait" />
</body>
</html>