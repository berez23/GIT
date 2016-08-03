<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaContribuenti=(java.util.Vector)sessione.getAttribute("LISTA_CONTRIBUENTI"); %>
<% it.escsolution.escwebgis.tributi.bean.ContribuentiFinder finder = (it.escsolution.escwebgis.tributi.bean.ContribuentiFinder)sessione.getAttribute("FINDER5"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		 
<html>
<head>
<title>Tributi Contribuenti - Lista</title>
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
			popUpAperte[index] = window.open("", "PopUpContrDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpContrDetail" + index, "width=640,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}

function chiudiTutteLePopUp()
{
	try
	{
		for (index in popUpAperte)
		{
			if (!popUpAperte[index].closed)
				popUpAperte[index].close();
		}
		popUpAperte = new Array();
	}
	catch (e)
	{
		alert(e);
	}
}

function mettiST(){
	//alert("mettiST");
	document.mainform.ST.value = 2;
}


<% if (vlistaContribuenti.size() > 0){
it.escsolution.escwebgis.tributi.bean.Contribuente pCont=(it.escsolution.escwebgis.tributi.bean.Contribuente)vlistaContribuenti.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pCont.getCodContribuente()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiContribuenti" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Contribuenti</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE CONTRIBUENTE<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DENOMINAZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTITA IVA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>
		<td class="extWinTDTitle" style="text-align: center; vertical-align: middle;">
			<input type="button" class="tdButton" value="Chiudi tutto" title="chiudi tutte le finestre di dettaglio aperte..." onClick="chiudiTutteLePopUp();" />
		</td>
	</tr>
	
	<% it.escsolution.escwebgis.tributi.bean.Contribuente contribuente = new it.escsolution.escwebgis.tributi.bean.Contribuente(); %>
  <% java.util.Enumeration en = vlistaContribuenti.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        contribuente = (it.escsolution.escwebgis.tributi.bean.Contribuente)en.nextElement();%>



    <tr id="r<%=contatore%>">
		
		<td class="extWinTDData" onclick="vai('<%=contribuente.getCodContribuente()%>', '<%=progressivoRecord%>', false)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=contribuente.getCodContribuente()%></span></td>
		<td class="extWinTDData" onclick="vai('<%=contribuente.getCodContribuente()%>', '<%=progressivoRecord%>', false)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=contribuente.getCognome()%></span></td>
		<td class="extWinTDData" onclick="vai('<%=contribuente.getCodContribuente()%>', '<%=progressivoRecord%>', false)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=contribuente.getNome()%></span></td>	
		<td class="extWinTDData" onclick="vai('<%=contribuente.getCodContribuente()%>', '<%=progressivoRecord%>', false)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=contribuente.getCodFiscale()%></span></td>	
		<td class="extWinTDData" onclick="vai('<%=contribuente.getCodContribuente()%>', '<%=progressivoRecord%>', false)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=contribuente.getDenominazione()%></span></td>
		<td class="extWinTDData" onclick="vai('<%=contribuente.getCodContribuente()%>', '<%=progressivoRecord%>', false)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=contribuente.getPartitaIVA()%></span></td>	
		<td class="extWinTDData" onclick="vai('<%=contribuente.getCodContribuente()%>', '<%=progressivoRecord%>', false)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=contribuente.getIndirizzo()%></span></td>
		<td class="extWinTDData" onclick="vai('<%=contribuente.getCodContribuente()%>', '<%=progressivoRecord%>', false)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=contribuente.getProvenienza()%></span></td>	
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style="text-align: center; vertical-align: middle;">
			<input type="button" class="tdButton" value="Dettaglio" title="apre il dettaglio in una finestra separata..." onClick="vai('<%=contribuente.getCodContribuente()%>', '<%=progressivoRecord%>', true)" />
		</td>	
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="5">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>