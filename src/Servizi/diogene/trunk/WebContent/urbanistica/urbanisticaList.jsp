<%@ page language="java" import="it.escsolution.escwebgis.urbanistica.bean.*, it.escsolution.escwebgis.urbanistica.logic.*, it.escsolution.escwebgis.urbanistica.servlet.* " %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaUrba = (java.util.Vector) sessione.getAttribute(UrbanisticaLogic.LISTA_URBANISTICA); %>
<% UrbanisticaFinder finder = (UrbanisticaFinder) sessione.getAttribute(UrbanisticaServlet.NOMEFINDER); %>

<% int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

%>  
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<%@ page import="java.util.ArrayList"%>
<html>
<head>
<title>Urbanistica - Lista</title>
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

function vaiMultiplo(){
	wait();
	document.mainform.ST.value = 3;
	document.mainform.submit();	
}

function onOff(codice, record_cliccato){
	document.mainform.OGGETTO_SEL.value=codice;
	document.mainform.RECORD_SEL.value=record_cliccato;
	var n = document.mainform.chkSel.length;
	var cnt = 0;
	for (i = 0; i < n; i++){
		if (document.mainform.chkSel[i].checked == true){
				cnt = cnt + 1;
			}	
	}
	if (cnt > 0){
		document.mainform.btnDettaglio.disabled = false;
	}else{
		document.mainform.btnDettaglio.disabled = true;
	}
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
			popUpAperte[index] = window.open("", "PopUpUrbanisticaDetail" + index, "width=700,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpUrbanisticaDetail" + index, "width=700,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}

<% if (vlistaUrba.size() > 0){
Urbanistica urba = (Urbanistica) vlistaUrba.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=urba.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Urbanistica" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Urbanistica</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">&nbsp;</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOMINATIVO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">UBICAZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>
	</tr>
	
  <% Urbanistica urbano; %>
  <% java.util.Enumeration en = vlistaUrba.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
	  urbano = (Urbanistica) en.nextElement();%>

    <tr id="r<%=contatore%>">
  			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
  				<span class="extWinTXTData">
  					<input type="checkbox" name="chkSel" value="<%=urbano.getPk()%>" onclick="onOff('<%=urbano.getPk()%>', '<%=progressivoRecord%>');"/>
  				</span>
  			</td>
			<td class="extWinTDData" onclick="vai('<%=urbano.getPk()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= urbano.getNominativo() %></span></td>
			<td class="extWinTDData" onclick="vai('<%=urbano.getPk()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= urbano.getUbicazione() %></span></td>
			<td class="extWinTDData" onclick="vai('<%=urbano.getPk()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= urbano.getProvenienza() %></span></td>
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

	<tr>
		<td class="extWinTDTitle" colspan='4'>
			<span class="extWinTXTTitle">
				<input type="button" name="btnDettaglio" value="Dettaglio Multiplo" onclick="vaiMultiplo();" disabled="true"/>
			</span>
		</td>
	</tr>


<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="107">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">


</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>