<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%  

	java.util.Vector vlistaEnel=(java.util.Vector)sessione.getAttribute(EnelDwhLogic.LISTA_ENELDWH); 
	
	EnelDwhFinder finder = null;
	if (sessione.getAttribute(EnelDwhServlet.NOMEFINDER)!= null){
		finder = (EnelDwhFinder)sessione.getAttribute(EnelDwhServlet.NOMEFINDER); 
	}else 
		finder = new EnelDwhFinder();
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<%@page import="it.escsolution.escwebgis.enel.logic.EnelDwhLogic"%>
<%@page import="it.escsolution.escwebgis.enel.bean.EnelDwhFinder"%>
<%@page import="it.escsolution.escwebgis.enel.bean.EnelBean"%>
<%@page import="it.escsolution.escwebgis.enel.servlet.EnelDwhServlet"%>
<%@page import="it.escsolution.escwebgis.enel.bean.EnelBean2"%>
<html>
<head>
<title>Enel - Lista Forniture</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
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

function setUtenza(eid, annoSel){
	document.mainform.UTENZA_SEL.value=eid;
	document.mainform.ANNO_SEL.value=annoSel;
}
function mettiST(){
	document.mainform.ST.value = 2;
}

function vai(codice, record_cliccato, isPopUp)
{
	try
	{
		document.mainform.OGGETTO_SEL.value = codice;
		document.mainform.RECORD_SEL.value = record_cliccato;
		if (isPopUp)
		{
			targ = apriPopUp(record_cliccato);
			
			if (targ)
			{
				document.mainform.ST.value = 33;
				document.mainform.target = targ;
				document.mainform.submit();
				document.mainform.ST.value = 2;
				document.mainform.target = "_parent";
			}
		}
		else
		{
			wait();
			document.mainform.ST.value = 3;
			document.mainform.target = "_parent";
			document.mainform.submit();
		}
	}
	catch (e)
	{
		//alert(e);
	}
}



var popUpAperte = new Array();
function apriPopUp(index)
{
	if (popUpAperte[index])
	{
		if (popUpAperte[index].closed)
		{
			popUpAperte[index] = window.open("", "PopUpEnelDwhDetail" + index, "width=800,height=480,status=yes,resizable=yes");
			popUpAperte[index].focus();
			return popUpAperte[index].name;
		}
		else
		{
			popUpAperte[index].focus();
			return false;
		}
	}
	else
	{
		popUpAperte[index] = window.open("", "PopUpEnelDwhDetail" + index, "width=800,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}



<% if (vlistaEnel.size() > 0){
	EnelBean2 pEnel=(EnelBean2)vlistaEnel.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pEnel.getUtente().getIdExt()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/EnelDwh" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Forniture Elettriche</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DENOMINAZIONE<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE UTENZA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO RIFERIMENTO</span></td>
	</tr>
	
	<% EnelBean2 enel2 = new EnelBean2(); %>
  <% java.util.Enumeration en = vlistaEnel.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% 
     while (en.hasMoreElements()) {
        enel2 = (EnelBean2)en.nextElement();
        
        %>



    <tr id="r<%=contatore%>">
		
		<td onclick="setUtenza('<%=enel2.getCodiceUtenza()%>', '<%=enel2.getAnnoRiferimentoDati()%>'); vai('<%=enel2.getUtente().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%= enel2.getUtente().getDenominazione() %></span></td>
		<td onclick="setUtenza('<%=enel2.getCodiceUtenza()%>', '<%=enel2.getAnnoRiferimentoDati()%>'); vai('<%=enel2.getUtente().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=enel2.getUtente().getCodiceFiscale() %></span></td>
		<td onclick="setUtenza('<%=enel2.getCodiceUtenza()%>', '<%=enel2.getAnnoRiferimentoDati()%>'); vai('<%=enel2.getUtente().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=enel2.getCodiceUtenza() %></span></td>	
		<td onclick="setUtenza('<%=enel2.getCodiceUtenza()%>', '<%=enel2.getAnnoRiferimentoDati()%>'); vai('<%=enel2.getUtente().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=enel2.getAnnoRiferimentoDati() %></span></td>
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="UTENZA_SEL" value="">
<input type='hidden' name="ANNO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<% if (finder != null){%>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="105">
<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>