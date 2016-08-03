
<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaInt=(java.util.Vector)sessione.getAttribute("LISTA_INTESTATARIF"); %>
<% it.escsolution.escwebgis.catasto.bean.IntestatarioFFinder finder = (it.escsolution.escwebgis.catasto.bean.IntestatarioFFinder)sessione.getAttribute("FINDER3"); %>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>

<html>
<head>
<title>Catasto Intestatari Persone Fisiche - Lista</title>
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
			popUpAperte[index] = window.open("", "PopUpIntFisDetail" + index, "width=800,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpIntFisDetail" + index, "width=800,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}


<% if (vlistaInt.size() > 0){
	it.escsolution.escwebgis.catasto.bean.IntestatarioF pIntF=(it.escsolution.escwebgis.catasto.bean.IntestatarioF)vlistaInt.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pIntF.getCodIntestatario()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>
</script>

<body  onload="mettiST()">
<form name="mainform" target="_parent" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoIntestatariF">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Persone fisiche</span>
</div>

&nbsp;

   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sesso</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data di Nascita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Luogo di Nascita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
	</tr>
        
        

  <% it.escsolution.escwebgis.catasto.bean.IntestatarioF intF = new it.escsolution.escwebgis.catasto.bean.IntestatarioF(); %>
  <% java.util.Enumeration en = vlistaInt.elements(); int contatore=1;%>  
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        intF = (it.escsolution.escwebgis.catasto.bean.IntestatarioF)en.nextElement(); %>
        
        
    <tr id="r<%=contatore%>" onclick="vai('<%=intF.getCodIntestatario()%>', '<%=progressivoRecord%>', true)">
   
		
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=intF.getCognome()%></span></td>	
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=intF.getNome()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=intF.getSesso()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=intF.getDataNascita()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=intF.getLuogo()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=intF.getCodFiscale()%></span></td>
	
	</tr>
	<% contatore++;progressivoRecord ++;} %>
 
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<% if (finder != null){%>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}%>
<input type='hidden' name="ACT_PAGE" value="1">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="3">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>