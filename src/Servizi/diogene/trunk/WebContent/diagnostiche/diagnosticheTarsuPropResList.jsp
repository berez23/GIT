<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%  
	
	java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(DiagnosticheTarsuLogic.LISTA_DIAGNOSTICHE_PROP_RES); 
	
	DiagnosticheTarsuFinder finder = null;
	if (sessione.getAttribute("FINDER104")!= null){
		finder = (DiagnosticheTarsuFinder)sessione.getAttribute("FINDER104"); 
	}else 
		finder = new DiagnosticheTarsuFinder();
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>

<%@page import="it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheTarsuLogic"%>
<%@page import="it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheTarsuFinder"%>
<%@page import="it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheTarsu"%>
<html>
<head>
<title>Diagnostiche Tarsu - Lista Proprietari Residenti non presenti in Tarsu</title>
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
	document.mainform.ST.value = 5;
	document.mainform.submit();
	}

function mettiST(){
	document.mainform.ST.value = 2;
}


<% if (vlista.size() > 0){
DiagnosticheTarsu dd=(DiagnosticheTarsu)vlista.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=dd.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 5;
	document.mainform.submit();
	}
<%}%>
function esportaExcel(){
 	document.mainform.OGGETTO_SEL.value='';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 6;
	document.mainform.submit();
}
</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/DiagnosticheTarsu" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Elenco Proprietari Residenti Non Risultanti in Tarsu</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUB</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">UNIMM</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CATEGORIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUP.CAT.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PERC.POSS.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.FIS.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DENOMINAZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DT.NASCITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">POS.ANA.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CIVICO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TOPO.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">ANAG.DA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">ELIMINA</span></td>
	</tr>
	
	<% DiagnosticheTarsu dt = new DiagnosticheTarsu	(); %>
  <% java.util.Enumeration en = vlista.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        dt = (DiagnosticheTarsu)en.nextElement();%>



    <tr id="r<%=contatore%>">
		
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getFoglio()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getParticella()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getSub()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getUnimm()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getCategoria()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getSupCat()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getPercPoss()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getCfis()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getCognome()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getNome()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getDenominazione()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getDtNascita()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getPosAna()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getCivi()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getTopo()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getAnagDa()%></span></td>
		<td onclick="" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dt.getElimina()%></span></td>
		
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<% if (finder != null){%>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="104">
<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<br>
<form name="formCate">
<table align=left cellpadding="0" cellspacing="0"
	class="editExtTable" style="background-color: #C0C0C0;">
<tr>
	<td class="TDmainLabel"
			style="width: 5%; white-space: nowrap;">
			<a href="javascript:esportaExcel();">
			<span class="TXTmainLabel"> 
				Esporta in formato XLS
			</span>
			</a>
	</td>
</tr>
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>