<%@ page language="java" import="it.escsolution.escwebgis.catasto.bean.*"%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  IntestatarioG intG=(IntestatarioG)sessione.getAttribute("INTESTATARIOG"); %>
<% java.util.Vector vlistaImmobili=(java.util.Vector)sessione.getAttribute("LISTA_IMMOBILI2"); %>
<% java.util.Vector vlistaTerreni=(java.util.Vector)sessione.getAttribute("LISTA_TERRENI2"); %>

<%DecimalFormat DF = new DecimalFormat();
	DF.setGroupingUsed(false);
	DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	dfs.setDecimalSeparator(',');
	DF.setDecimalFormatSymbols(dfs);
	DF.setMaximumFractionDigits(2);
%>

<%  
	IntestatarioGFinder finder  = null; 
	if (sessione.getAttribute("FINDER4")!=null){
		finder = (it.escsolution.escwebgis.catasto.bean.IntestatarioGFinder)sessione.getAttribute("FINDER4"); }

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
<title>Catasto Intestatari Persona Giuridica - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
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

</head>

<body>

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Scheda Intestatari Giuridici</span>
</div>
&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>

<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
&nbsp;
<form name="mainform" action="<%= request.getContextPath() %>/CatastoIntestatariG" target="_parent">



<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>

&nbsp;


<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top;">

<table align=center cellpadding="0" cellspacing="0">
	
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Ragione Sociale</span></td>
				<td class="TDviewTextBox" style="width:375;"><span class="TXTviewTextBox"><%=intG.getDenominazione()%></span></td>
			</tr>
		</table>
		</td>	
		
	</tr>
	
	<tr>		
				
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Partita IVA</span></td>
				<td class="TDviewTextBox" style="width:150;"><span class="TXTviewTextBox"><%=intG.getPartitaIva()%></span></td>
			</tr>			
		</table>
		</td>
		
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:70;"><span class="TXTmainLabel">Sede</span></td>
				<td class="TDviewTextBox" style="width:150;"><span class="TXTviewTextBox"><%=intG.getSede()%></span></td>
			</tr>
		</table>
		</td>
		
	</tr>
	
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Data Fine Val.</span></td>
				<td class="TDviewTextBox" style="width:375;"><span class="TXTviewTextBox"><%=intG.getDataFine()%></span></td>
			</tr>
		</table>
		</td>	
		
	</tr>

</table>
</td>

</tr>
</table>

<br><br><br>
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
			<tr> <td colspan=7 class="extWinTDTitle"><span class="extWinTXTTitle">Particelle Terreni</span></td> 
			</tr>
	

			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Poss.</span></td>
				<!--td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val.</span></td-->
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
        
    
    		<tr id="r<%=contatore%>" <% if ("31/12/9999".equals(terreno.getDataFine()) && "31/12/9999".equals(terreno.getDataFinePos())) { %> style="color: green; font-weight: bold;" <% } %> 
    			onclick="vaiPart(1,'<%=terreno.getParticella()%>', '<%=progressivoRecord%>')">
    
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
				<!--td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
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
					<!-- <td colspan=4 class="extWinTDTitle"> -->
					<td colspan='8' class="extWinTDTitle">
					<span class="extWinTXTTitle">Particelle Fabbricati</span></td>		
				</tr>
		
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Categoria</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Poss.</span></td>
					<!--td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val.</span></td-->
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
    			<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getSezione()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getFoglio()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getNumero()%></span></td>	
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getUnimm()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getCodCategoria()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData">
							<%="31/12/9999".equals(immobile.getDataFinePos()) ? "ATTUALE" : immobile.getDataFinePos()%>
					</span>
					</td>
					<!--td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getDataFineVal()%></span></td-->
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getTitolo()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore1%>,1)' onmouseout='chgtr(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=percPoss%></span></td>
				</tr>
		
  <% contatore1++;progressivoRecord1++;} %>
			</table>
		</div>
		<% } %>
	</div>
<%-- FINE TEST MB --%>
<br><br>
<% if (intG != null){%>
<% String codice = "";
   codice = intG.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}else{%>
	<input type='hidden' name="OGGETTO_SEL" value="">
<%}
if (finder != null){ %>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}else{%>
<input type='hidden' name="ACT_PAGE" value="">
<%}%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ST" value="3">
<input type='hidden' name="UC" value="4">
<input type='hidden' name="EXT" value="">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</form>

<div id="wait" class="cursorWait" />
</body>
</html>