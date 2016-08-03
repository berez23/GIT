<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaCond=(java.util.Vector)sessione.getAttribute("LISTA_FORNITURE"); %>
<% it.escsolution.escwebgis.dup.bean.DupFornitureFinder finder = (it.escsolution.escwebgis.dup.bean.DupFornitureFinder)sessione.getAttribute("FINDER23"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>DUP - Lista Forniture</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>

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


function vai(codice, record_cliccato){
	wait();
	document.mainform.OGGETTO_SEL.value=codice;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}

function mettiST(){
	document.mainform.ST.value = 2;
}


<% if (vlistaCond.size() > 0){
it.escsolution.escwebgis.dup.bean.DupForniture sFornit=(it.escsolution.escwebgis.dup.bean.DupForniture)vlistaCond.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=sFornit.getIdFornitura()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/DupForniture" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Forniture</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
<!--		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod. Ammin.</span></td>-->
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data inizio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data fine</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Estr. Conservatoria</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Estr. Catasto</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Scritti</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Note</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Note Registrate</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Note Scartate</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Immo. Trattati</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particelle</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Fabbricati</span></td>

	</tr>
	
	<% it.escsolution.escwebgis.dup.bean.DupForniture fornit = new it.escsolution.escwebgis.dup.bean.DupForniture(); %>
  <% java.util.Enumeration en = vlistaCond.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        fornit = (it.escsolution.escwebgis.dup.bean.DupForniture)en.nextElement();%>



    <tr id="r<%=contatore%>" >
<!--		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=fornit.getCodAmmin()%></span></td>-->
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getDataInizio()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getDataFine()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getDataEstrCons()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getDataEstrCata()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getCntScritti()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getCntNote()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getCntNoteReg()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getCntNoteSca()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getCntImmoTratta()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getCntParticelle()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=fornit.getCntFabbricati()%></span></td>			
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="23">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>