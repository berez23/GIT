<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaInt=(java.util.Vector)sessione.getAttribute("LISTA_INTESTATARIG"); %>
<% it.escsolution.escwebgis.catasto.bean.IntestatarioGFinder finder = (it.escsolution.escwebgis.catasto.bean.IntestatarioGFinder)sessione.getAttribute("FINDER4"); %>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>Catasto Intestatari Persone Giuridiche - Lista</title>
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

function vai(intestatario,record_cliccato){
	wait();
	document.mainform.OGGETTO_SEL.value=intestatario;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
function mettiST(){
	//alert("mettiST");
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
			popUpAperte[index] = window.open("", "PopUpIntGiuDetail" + index, "width=800,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpIntGiuDetail" + index, "width=800,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}


<% if (vlistaInt.size() > 0){
it.escsolution.escwebgis.catasto.bean.IntestatarioG pIntG=(it.escsolution.escwebgis.catasto.bean.IntestatarioG)vlistaInt.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pIntG.getCodIntestatario()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>	
</script>

<body onload="mettiST()">

<form name="mainform" target="_parent" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoIntestatariG">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Persone Giuridiche</span>
</div>

&nbsp;


               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0" align=center>
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Ragione Sociale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sede</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita IVA</span></td>
	</tr>
        
        

  <% it.escsolution.escwebgis.catasto.bean.IntestatarioG intG = new it.escsolution.escwebgis.catasto.bean.IntestatarioG(); %>
  <% java.util.Enumeration en = vlistaInt.elements(); int contatore=1;%>
  <%long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        intG = (it.escsolution.escwebgis.catasto.bean.IntestatarioG)en.nextElement(); %>

    <tr id="r<%=contatore%>" onclick="vai('<%=intG.getCodIntestatario()%>' , '<%=progressivoRecord%>', true)">
		
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=intG.getDenominazione()%></span></td>	
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=intG.getSede()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=intG.getPartitaIva()%></span></td>
		
	
	</tr>
		
<% contatore++;progressivoRecord ++;} %>
	
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="4">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>