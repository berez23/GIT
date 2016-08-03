<%@ page language="java" import="it.escsolution.escwebgis.tributi.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Contribuente cont=(Contribuente)sessione.getAttribute("CONTRIBUENTE"); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>

<!-- TEST MB -->
<% java.util.Vector vlistaICI=(java.util.Vector)sessione.getAttribute("LISTA_ICI_CONTRIBUENTI"); %>
<% java.util.Vector vlistaTARSU=(java.util.Vector)sessione.getAttribute("LISTA_TARSU_CONTRIBUENTI"); %>
<!-- FINE TEST MB -->
<% 
it.escsolution.escwebgis.tributi.bean.ContribuentiFinder finder = null;
if (sessione.getAttribute("FINDER5")!= null){
	if (((Object)sessione.getAttribute("FINDER5")).getClass() == new ContribuentiFinder().getClass()){
		finder = (it.escsolution.escwebgis.tributi.bean.ContribuentiFinder)sessione.getAttribute("FINDER5"); 
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
<title>Tributi Contribuente <%= cont == null ? "" : cont.getCodContribuente() + "&nbsp;" %>- Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script>

function visualizzaParticelle(){
	wait();
	document.mainform.ST.value=4;
	document.mainform.EXT.value = 1;
	document.mainform.submit();
}
function mettiST(){
	document.mainform.ST.value = 3;
}

function vaiPart(tipo, Particella, record_cliccato){
	if (tipo == 1){
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiOggettiICI";
		document.mainform.UC.value=6;
		}	
	else{ 
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiOggettiTARSU";
		document.mainform.UC.value=7;
		}
		
	document.mainform.OGGETTO_SEL.value=Particella;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.EXT.value = 1;
	document.mainform.submit();
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

</script>
</head>
<body >
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiContribuenti" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Scheda Contribuente <%=cont.getCognome()%> <%=cont.getNome()%></span>
</div>

<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>

&nbsp;



<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">

	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
		<td colspan=3>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:280; padding-bottom: 2px;"><span class="TXTmainLabel">Codice Contribuente</span></td>
				<td width=30></td>
				<td class="TDviewTextBox" style="width:270; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getCodContribuente()%></span></td>
			</tr>
		</table>
		</td>	
		
		<td width=10></td>
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
		<td>		
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>	
				<td class="TDmainLabel"  style="width:130; padding-bottom: 2px;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getCognome()%></span></td>
			</tr>			
		</table>
		</td>
		
		<td width=30></td>

		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:120; padding-bottom: 2px;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getNome()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
</table>

&nbsp;
		
<table  align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:130; padding-bottom: 2px;"><span class="TXTmainLabel">Sesso</span></td>
				<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getSesso()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=30></td>
	
		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:120; padding-bottom: 2px;"><span class="TXTmainLabel">Data di Nascita</span></td>
				<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getDataNascita()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:130; padding-bottom: 2px;"><span class="TXTmainLabel">Comune di Nascita</span></td>
				<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getDesComuneNascita()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=30></td>

		
		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:120; padding-bottom: 2px;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getCodFiscale()%></span></td>
			</tr>
		</table>
		</td>
				
		<td width=10></td>
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:130; padding-bottom: 2px;"><span class="TXTmainLabel">Denominazione</span></td>
				<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getDenominazione()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=30></td>
		
		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:120; padding-bottom: 2px;"><span class="TXTmainLabel">Partita IVA</span></td>
				<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getPartitaIVA()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
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
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:130; padding-bottom: 2px;"><span class="TXTmainLabel">Piano</span></td>
				<td class="TDviewTextBox" style="width:75; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getPiano()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=47></td>

		
		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:65; padding-bottom: 2px;"><span class="TXTmainLabel">Scala</span></td>
				<td class="TDviewTextBox" style="width:75; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getScala()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=48></td>

		
		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:65; padding-bottom: 2px;"><span class="TXTmainLabel">Interno</span></td>
				<td class="TDviewTextBox" style="width:75; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getInterno()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
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
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Indirizzo dichiarato</span></td>
				<td class="TDviewTextBox" style="width:150;"><span class="TXTviewTextBox"><%=cont.getIndirizzo()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>

		<td>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:90; padding-bottom: 2px;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:120; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getResidenza()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
			
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
		
		<% if ((cont.getAnagrafe() == null || cont.getAnagrafe().trim().equals("") || cont.getAnagrafe().trim().equals("-")) && 
				(cont.getComune() == null || cont.getComune().trim().equals("") || cont.getComune().trim().equals("-"))) {%>
			<td colspan="3">
				<table class="viewExtTable" cellpadding="0" cellspacing="0">
					<tr>
						<td class="TDmainLabel"  style="width:210; padding-bottom: 2px;"><span class="TXTmainLabel">Indirizzo residenza anagrafica</span></td>
						<td class="TDviewTextBox" style="width:370; padding-bottom: 2px;"><span class="TXTviewTextBox"><%="nessuno"%></span></td>
					</tr>
				</table>
			</td>			
		<%} else {%>		
			<td>
				<table class="viewExtTable" cellpadding="0" cellspacing="0">
					<tr>
						<td class="TDmainLabel"  style="width:210; padding-bottom: 2px;"><span class="TXTmainLabel">Indirizzo residenza anagrafica</span></td>
						<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getAnagrafe() == null ? "" : cont.getAnagrafe().trim()%></span></td>
					</tr>
				</table>
			</td>
				
			<td width=10></td>
		
			<td>
				<table class="viewExtTable" cellpadding="0" cellspacing="0">
					<tr>
						<td class="TDmainLabel"  style="width:90; padding-bottom: 2px;"><span class="TXTmainLabel">Comune</span></td>
						<td class="TDviewTextBox" style="width:120; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getComune() == null ? "" : cont.getComune().trim()%></span></td>
					</tr>
				</table>
			</td>
		<%}%>
		
		<td width=10></td>
			
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
		<% if ((cont.getSiatel() == null || cont.getSiatel().trim().equals("") || cont.getSiatel().trim().equals("-")) && 
				(cont.getComuneSiatel() == null || cont.getComuneSiatel().trim().equals("") || cont.getComuneSiatel().trim().equals("-"))) {%>				
			<td colspan="3">
				<table class="viewExtTable" cellpadding="0" cellspacing="0">
					<tr>
						<td class="TDmainLabel"  style="width:210; padding-bottom: 2px;"><span class="TXTmainLabel">Indirizzo Siatel</span></td>
						<td class="TDviewTextBox" style="width:370; padding-bottom: 2px;"><span class="TXTviewTextBox"><%="nessuno"%></span></td>
					</tr>
				</table>
			</td>
		<%} else {%>		
			<td>
				<table class="viewExtTable" cellpadding="0" cellspacing="0">
					<tr>
						<td class="TDmainLabel"  style="width:210; padding-bottom: 2px;"><span class="TXTmainLabel">Indirizzo Siatel</span></td>
						<td class="TDviewTextBox" style="width:150; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getSiatel() == null ? "" : cont.getSiatel().trim()%></span></td>
					</tr>
				</table>
				</td>
				
				<td width=10></td>
		
				<td>
				<table class="viewExtTable" cellpadding="0" cellspacing="0">
					<tr>
						<td class="TDmainLabel"  style="width:90; padding-bottom: 2px;"><span class="TXTmainLabel">Comune</span></td>
						<td class="TDviewTextBox" style="width:120; padding-bottom: 2px;"><span class="TXTviewTextBox"><%=cont.getComuneSiatel() == null ? "" : cont.getComuneSiatel().trim()%></span></td>
					</tr>
				</table>
			</td>
		<%}%>
		
		<td width=10></td>
		
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
		<td colspan=3>
		<table class="viewExtTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="TDmainLabel"  style="width:280; padding-bottom: 2px;"><span class="TXTmainLabel">Titolarità Catasto</span></td>
				<td width=30></td>
				<td class="TDviewTextBox" style="width:270; padding-bottom: 2px;">
				
				<span class="TXTviewTextBox">
			<% if (cont.getPartitaIVA()  == null || cont.getPartitaIVA().equals("") ||  cont.getPartitaIVA().equals("-")){%>
			<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaF('<%=cont.getCodFiscale()%>','<%=cont.getNome().replace("'", "\\'")%>','<%=cont.getCognome().replace("'", "\\'")%>','<%=cont.getDataNascita()%>');">
				<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
			</a>
			<%} else { %>
			<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaG('<%=cont.getPartitaIVA()%>');">
				<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
			</a>
			<%} %>
			</span>
				
				</td>
			</tr>
		</table>
		</td>	
		
		<td width=10></td>
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	
	
</table>
</td>
</tr>
</table>
<!-- FINE solo dettaglio -->

<!-- TEST MB -->
<br/>
<br/>

<div class="tabber">

<% if (vlistaICI != null && vlistaICI.size() == 0){%>

	<div class="tabbertab">
	<h2>Oggetti ICI</h2>
   <table width="100%">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">
			Oggetti ICI
	</tr>
	<tr>

		<td width="50%"  valign="top" style="background-color: #FFFFFF;">

			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				
				<tr> 
					<td colspan=11 class="extWinTDTitle"><span class="extWinTXTTitle">Oggetti ICI</span></td> 
				</tr>
		
				<tr>		
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Inizio Possesso</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Possesso</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Denuncia</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Denuncia</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
				</tr>
				
				
		
	<%it.escsolution.escwebgis.tributi.bean.OggettiICI ici = new it.escsolution.escwebgis.tributi.bean.OggettiICI(); %>
  	<%java.util.Enumeration en = vlistaICI.elements(); int contatore = 1; %>
  	<% long progressivoRecord = 1; int annoIci= 0 ;%>
  	<%while (en.hasMoreElements()) {
    ici = (it.escsolution.escwebgis.tributi.bean.OggettiICI)en.nextElement();%>
        
		<% 
			int annoIciAttuale = ici.getDenAnno()==null?0:ici.getDenAnno().intValue();
			if (annoIci!=annoIciAttuale && contatore!=1) {
		  		
		%>  			
			    <tr>
			    		<td colspan="11">
						<span class="extWinTXTData">&nbsp;</span>
						</td>
			    </tr>
			
	  	<% 	
	  		}
		%>
    
    			<tr id="r<%=contatore%>" onclick="vaiPart(1,'<%=ici.getChiave()%>', '<%=progressivoRecord%>')">
    
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getFoglioCatasto()%></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getParticellaCatasto()%></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getSubalterno()%></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getIndirizzo()%></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getCategoria()%></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getDataInizio()%></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getDataFine()%></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getProvenienza()%></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getDenAnno() %></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getDenNumero() %></span></td>
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getRendita() %></span></td>
				</tr>
		
	<% 
		contatore++;
		progressivoRecord++;
		annoIci = annoIciAttuale;
  	} 
  	%>
		</table>
		</td>
	</tr>
</table>	

</div>
<% } %>

<% if (vlistaTARSU !=  null && vlistaTARSU.size() == 0){%>	
	<div class="tabbertab">
	<h2>Oggetti TARSU</h2>
	<table width="100%">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">
			Oggetti TARSU
	</tr>
	<tr>
			<td width="50%"  valign="top" style="background-color: #FFFFFF;">
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				
				<tr> 
					<td colspan="8" class="extWinTDTitle"><span class="extWinTXTTitle">Oggetti TARSU</span></td>
				</tr>
		
				<tr>		
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie Totale</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
				</tr>
				
				
		
  <%it.escsolution.escwebgis.tributi.bean.OggettiTARSU tarsu = new it.escsolution.escwebgis.tributi.bean.OggettiTARSU(); %>
  <% java.util.Enumeration en1 = vlistaTARSU.elements(); int contatore1 = 1; %>
  <% long progressivoRecord1 = 1 ; %>
  <% while (en1.hasMoreElements()) {
        tarsu = (it.escsolution.escwebgis.tributi.bean.OggettiTARSU)en1.nextElement();%>



    			<tr id="p<%=contatore1%>" onclick="vaiPart(2,'<%=tarsu.getChiave()%>', '<%=progressivoRecord1%>')">
		
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getComune()%></span></td>		
					<td class="extWinTDData"   style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getDescrCategoria()%></span></td>
	    			<td class="extWinTDData"  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getFoglio()%></span></td>
					<td class="extWinTDData"  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getParticella()%></span></td>	
					<td class="extWinTDData"  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getSubalterno()%></span></td>
					<td class="extWinTDData"  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getIndirizzo()%></span></td>
					<td class="extWinTDData"  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getSupTotale()%></span></td>
					<td class="extWinTDData"  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getProvenienza()%></span></td>
				</tr>
		
  <% contatore1++;progressivoRecord1++;} %>
		</table>
		</td>
	</tr>
</table>
</div>
<%} %>
<!-- FINE TEST MB -->	

</div>
<%-- 
<br><br><br>
<center><span class="extWinTXTTitle"><a href="javascript:visualizzaParticelle()">Visualizza Oggetti Tributari </a></span></center>
--%>
<br><br>


<% if (finder != null){ %>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}else{%>
<input type='hidden' name="ACT_PAGE" value="">
<%}%>
		
			
		<input type='hidden' name="OGGETTO_SEL" value="">
		<input type='hidden' name="RECORD_SEL" value="">
		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="5">
		<input type='hidden' name="EXT" value="">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		
</form>
<div id="wait" class="cursorWait" />
</body>
</html>