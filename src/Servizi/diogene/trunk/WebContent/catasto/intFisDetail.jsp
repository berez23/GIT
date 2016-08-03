<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page language="java" import="it.escsolution.escwebgis.catasto.bean.*"%>
<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.webred.cet.permission.CeTUser"%>

<% HttpSession sessione = request.getSession(true);  %> 
<% IntestatarioF intF=(IntestatarioF)sessione.getAttribute("INTESTATARIOF"); %>
<% ArrayList intsF = (ArrayList)sessione.getAttribute("INTESTATARIOF_STO"); %>
<% EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null); %>
<% java.util.Vector vlistaImmobili=(java.util.Vector)sessione.getAttribute("LISTA_IMMOBILI2"); %>
<% java.util.Vector vlistaTerreni=(java.util.Vector)sessione.getAttribute("LISTA_TERRENI2"); %>

<%DecimalFormat DF = new DecimalFormat();
	DF.setGroupingUsed(false);
	DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	dfs.setDecimalSeparator(',');
	DF.setDecimalFormatSymbols(dfs);
	DF.setMaximumFractionDigits(2);
%>


<% boolean tipo2=((Boolean)sessione.getAttribute("TIPO")).booleanValue();
it.escsolution.escwebgis.catasto.bean.IntestatarioFFinder finder = null; 
if (sessione.getAttribute("FINDER3")!= null){
	finder = (it.escsolution.escwebgis.catasto.bean.IntestatarioFFinder)sessione.getAttribute("FINDER3"); 
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


<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>

<html>
<head>
<title>Catasto Intestatari Persona fisica - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script>
function visualizzaParticelle(){
	wait();
	document.mainform.ST.value=4;
	document.mainform.EXT.value=1;
	document.mainform.submit();
}
function mettiST(){
	document.mainform.ST.value = 3;
}

function vaiPart(tipo, Particella, record_cliccato){
	wait();
		if (tipo == 1){
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoTerreni";
		document.mainform.UC.value = 2;
		}
		else {
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoImmobili";
		document.mainform.UC.value = 1;
		}
	document.mainform.OGGETTO_SEL.value=Particella;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.EXT.value = 1;
	document.mainform.submit();
    }

function chgtr(row,flg)
{
if (flg==1)
	{
	document.all("r"+row).style.backgroundColor = "#d7d7d7";
	}
else
	{
	document.all("r"+row).style.backgroundColor = "";
	}
}

function chgtr2(row,flg){
	if (flg==1)
		{
		document.all("p"+row).style.backgroundColor = "#d7d7d7";
		}
	else
		{
		document.all("p"+row).style.backgroundColor = "";
		}
}
</script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<body  >
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Scheda Intestatari Fisici</span>
</div>


	
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>
<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
	&nbsp;
<form name="mainform" target="_parent" action="<%= request.getContextPath() %>/CatastoIntestatariF">



<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>

&nbsp;

<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top;">

<center><span class="extWinTXTTitle">Dati Anagrafici</span></center>
	<% if (intsF == null || intsF.size() < 2) { %>
	
		<!-- visualizzazione unico elemento -->
		
		<table cellpadding="0" cellspacing="0" align=center>
			
			<tr>
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Cognome</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getCognome()%></span></td>
					</tr>
				</table>
				</td>	
				
			</tr>
			
			<tr>
				
				<td>	
				<table class="viewExtTable" >
					<tr>			
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Nome</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getNome()%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
			
			<tr>		
						
				<td>		
				<table class="viewExtTable" >
					<tr>	
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Sesso</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getSesso()%></span></td>
					</tr>			
				</table>
				</td>
				
			</tr>
			
			<tr>
			
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Data di Nascita</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getDataNascita()%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
		
			<tr>		
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Luogo di Nascita</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getLuogo()%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
			
			<tr>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Codice Fiscale</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getCodFiscale()%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
			<tr>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Data Fine Validità</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%="31/12/9999".equals(intF.getDataFine()) ? "ATTUALE" : intF.getDataFine()%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
		
		
		</table>
		
	<% } else {%>
		
		<!-- visualizzazione tabella -->
		
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">		
			<tr>	
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>	
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sesso</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data di Nascita</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Luogo di Nascita</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val.</span></td>
			</tr>
		  	<%for (Object objInt : intsF) {
		    	IntestatarioF myIntF = (IntestatarioF)objInt;%>
			 	<tr <% if ("31/12/9999".equals(myIntF.getDataFine())) { %> style="color: green; font-weight: bold;" <% } %>>
			 		<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=myIntF.getCognome()%>
						</span>
					</td>
					<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=myIntF.getNome()%>
						</span>
					</td>
					<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=myIntF.getSesso()%>
						</span>
					</td>
					<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=myIntF.getDataNascita()%>
						</span>
					</td>
					<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=myIntF.getLuogo()%>
						</span>
					</td>
					<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=myIntF.getCodFiscale()%>
						</span>
					</td>
					<td class="extWinTDData">
						<span class="extWinTXTData">
							<%="31/12/9999".equals(myIntF.getDataFine()) ? "ATTUALE" : myIntF.getDataFine()%>
						</span>
					</td>
					
				</tr>
			<% } %>
		</table>
	<% } %>
</td>

</tr>
</table>

<%
if (GestionePermessi.autorizzato(eu.getUtente() , eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_TEMA_VISURA_NAZIONALE, true)) {
%>
<br>
<center>
<input type="button" value="Accedi a Visura Nazionale" style="font-size: 11px"
		onclick=" window.open('<%=request.getContextPath()%>/VisuraNazionale?OGGETTO_SEL_CF=<%=intF.getCodFiscale()%>&UC=126&ST=3&NONAV=1', 'PopUpSISTER', 'width=640,height=480,status=no,resizable=yes, scrollbars=yes')"/>
</center>
<%} %>
<br>
<%-- 
<center><span class="extWinTXTTitle"><a href="javascript:visualizzaParticelle()">Visualizza le Titolarità di questo soggetto </a></span></center>
--%>
<%-- TEST MB --%>

	
	<div class="tabber">
	
	<% 
	 if (vlistaTerreni != null && vlistaTerreni.size() > 0) { 	
	%>
	<div class="tabbertab">
	<h2>Titolarità Terreni</h2>



			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				<tr>&nbsp;</tr>
				<tr> 
					<td colspan=7 class="extWinTDTitle"><span class="extWinTXTTitle">Particelle Terreni</span></td> 
				</tr>
		
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez.</span></td>	
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Poss.</span></td>
					<!-- td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val.</span></td-->
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Perc.Poss.</span></td>
				</tr>
				
				
		
	<%it.escsolution.escwebgis.catasto.bean.Terreno terreno = new it.escsolution.escwebgis.catasto.bean.Terreno(); %>
  	<%java.util.Enumeration en = vlistaTerreni.elements(); int contatore = 1; %>
  	<% long progressivoRecord = 1; %>
  	<%while (en.hasMoreElements()) {
		    terreno = (it.escsolution.escwebgis.catasto.bean.Terreno)en.nextElement();
		    String percPoss = "-";
			if (terreno.getPercPoss() != null) {
				try {
					percPoss = DF.format(terreno.getPercPoss());
				} catch (Exception e) {
					percPoss = terreno.getPercPoss().toString().replace(",", ".");
					percPoss = percPoss.indexOf(".") == -1 || percPoss.length() < (percPoss.indexOf(".") + DF.getMaximumFractionDigits() + 2) ? percPoss : percPoss.substring(0, percPoss.indexOf(".") + DF.getMaximumFractionDigits() + 2);
					percPoss = DF.format(new Double(percPoss));
				}
			}%>
        
    
    			<tr id="r<%=contatore%>" <% if ("31/12/9999".equals(terreno.getDataFine()) && "31/12/9999".equals(terreno.getDataFinePos())) { %> style="color: green; font-weight: bold;" <% } %> onclick="vaiPart(1,'<%=terreno.getParticella()%>', '<%=progressivoRecord%>')">
    				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getSezione() %></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getFoglio() %></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getNumero()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getSubalterno()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
						<span class="extWinTXTData">
							<%="31/12/9999".equals(terreno.getDataFinePos()) ? "ATTUALE" : terreno.getDataFinePos()%>
						</span>
					</td>
					<!-- td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getDataFine()%></span></td-->
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getTitolo()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=percPoss%></span></td>
	
				</tr>
		
	<% contatore++;progressivoRecord ++;} %>
		</table>
		
		</div>
		<% } %>
		
		<% 
	 if (vlistaImmobili != null && vlistaImmobili.size() > 0) { 	
	%>
		
		<div class="tabbertab">
			<h2>Titolarità Fabbricati</h2>
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				<tr>&nbsp;</tr>
				<tr> 
					<td colspan=8 class="extWinTDTitle"><span class="extWinTXTTitle">Particelle Fabbricati</span></td>
				</tr>
		
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>					
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Categoria</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Poss.</span></td>
					<!-- td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val.</span></td -->
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Perc.Poss.</span></td>
				</tr>
				
				
		
  <% it.escsolution.escwebgis.catasto.bean.Immobile immobile = new it.escsolution.escwebgis.catasto.bean.Immobile(); %>
  <% java.util.Enumeration en1 = vlistaImmobili.elements(); int contatore1 = 1; %>
  <% long progressivoRecord1 = 1 ; %>
  <% while (en1.hasMoreElements()) {
        immobile = (it.escsolution.escwebgis.catasto.bean.Immobile)en1.nextElement();
        String percPoss = "-";
		if (immobile.getPercPoss() != null) {
			try {
				percPoss = DF.format(immobile.getPercPoss());
			} catch (Exception e) {
				percPoss = immobile.getPercPoss().toString().replace(",", ".");
				percPoss = percPoss.indexOf(".") == -1 || percPoss.length() < (percPoss.indexOf(".") + DF.getMaximumFractionDigits() + 2) ? percPoss : percPoss.substring(0, percPoss.indexOf(".") + DF.getMaximumFractionDigits() + 2);
				percPoss = DF.format(new Double(percPoss));
			}
		}%>
			 	

    			<tr id="p<%=contatore1%>" <% if ("31/12/9999".equals(immobile.getDataFineVal()) && "31/12/9999".equals(immobile.getDataFinePos())) { %> style="color: green; font-weight: bold;" <% } %> onclick="vaiPart(2,'<%=immobile.getChiave()%>', '<%=progressivoRecord1%>')">

					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getSezione()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getFoglio()%></span></td>
	    			<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getNumero()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getUnimm()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getCodCategoria()%></span></td>
					<td class="extWinTDData"  onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData">
							<%="31/12/9999".equals(immobile.getDataFinePos()) ? "ATTUALE" : immobile.getDataFinePos()%>
						</span>
					</td>
					<!-- td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getDataFineVal()%></span></td -->
					
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getTitolo()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=percPoss%></span></td>
					
					
	
					
				</tr>
		
  <% contatore1++;progressivoRecord1++;} %>
		</table>
		
		</div>
		
		<% } %>
		
</div>
<%-- FINE TEST MB --%>
<br><br>
<% if (intF != null){%>
<% String codice = "";
   codice = intF.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
<%}else{%>
   <input type='hidden' name="OGGETTO_SEL" value="">
<%} 
if (finder != null){ %>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}else{%>
<input type='hidden' name="ACT_PAGE" value="">
<%}%>

<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="ST" value="3">
<input type='hidden' name="UC" value="3">
<input type='hidden' name="EXT" value="">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>

<div id="wait" class="cursorWait" />
</body>
</html>