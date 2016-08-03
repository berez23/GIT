<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%  

	java.util.Vector vlistagas =(java.util.Vector)sessione.getAttribute(FornitureGasLogic.LISTA); 
	
	FornitureGasFinder finder = null;
	if (sessione.getAttribute(FornitureGasServlet.NOMEFINDER)!= null){
		finder = (FornitureGasFinder)sessione.getAttribute(FornitureGasServlet.NOMEFINDER); 
	}else 
		finder = new FornitureGasFinder();
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<%@page import="it.escsolution.escwebgis.gas.servlet.FornitureGasServlet"%>
<%@page import="it.escsolution.escwebgis.gas.logic.FornitureGasLogic"%>

<%@page import="it.escsolution.escwebgis.gas.bean.FornitureGasFinder"%>
<%@page import="it.escsolution.escwebgis.gas.servlet.FornitureGasServlet"%>
<%@page import="it.escsolution.escwebgis.gas.bean.FornitureGas"%><html>
<head>
<title>Forniture Gas - Lista</title>
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
			popUpAperte[index] = window.open("", "PopUpFornitureGasDetail" + index, "width=700,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpFornitureGasDetail" + index, "width=700,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}



<% if (vlistagas.size() > 0){
FornitureGas pGas=(FornitureGas)vlistagas.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pGas.getIdExt()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/FornitureGas" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Gas</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">C.F. EROGANTE<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IDENTIFICATIVO UTENZA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME / RAGIONE SOCIALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO UTENZA</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.gas.bean.FornitureGas gas = new it.escsolution.escwebgis.gas.bean.FornitureGas(); %>
  <% java.util.Enumeration en = vlistagas.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% 
     while (en.hasMoreElements()) {
        gas = (it.escsolution.escwebgis.gas.bean.FornitureGas)en.nextElement();%>



    <tr id="r<%=contatore%>">
		
		<td onclick="vai('<%=gas.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gas.getAnnoRiferimento() %></span></td>
		<td onclick="vai('<%=gas.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gas.getCfErogante() %></span></td>
		<td onclick="vai('<%=gas.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gas.getIdentificativoUtenza() %></span></td>
		<td onclick="vai('<%=gas.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gas.getCognomeUtente() == null || gas.getCognomeUtente().equals("") ? 
									(gas.getRagioneSociale() == null || gas.getRagioneSociale().equals("") ? "-" : gas.getRagioneSociale()) : 
									gas.getCognomeUtente()%></span></td>	
		<td onclick="vai('<%=gas.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gas.getNomeUtente() == null || gas.getNomeUtente().equals("") ? "-" : gas.getNomeUtente()%></span></td>
		<td onclick="vai('<%=gas.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gas.getIndirizzoUtenza()%></span></td>
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<% if (finder != null){%>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="54">
<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>