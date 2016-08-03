<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%
 	java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(it.escsolution.escwebgis.redditiAnnuali.logic.RedditiAnnualiLogic.LISTA); 
 	
 	it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnualiFinder finder = null;
 	if (sessione.getAttribute(it.escsolution.escwebgis.redditiAnnuali.servlet.RedditiAnnualiServlet.NOMEFINDER)!= null){
 		finder = (it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnualiFinder)sessione.getAttribute(it.escsolution.escwebgis.redditiAnnuali.servlet.RedditiAnnualiServlet.NOMEFINDER); 
 	}else 
 		finder = new it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnualiFinder();
 	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
 %>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>Redditi Annuali - Lista</title>
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
			popUpAperte[index] = window.open("", "PopUpRedditiDetail" + index, "width=700,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpRedditiDetail" + index, "width=700,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}



<% if (vlista.size() > 0){
it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnuali red = (it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnuali)vlista.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=red.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/RedditiAnnuali" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Redditi</span>
</div>
&nbsp;
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">IDE telematico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Modello</span></td>
	</tr>
<% it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnuali red = new it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnuali(); %>
<% java.util.Enumeration en = vlista.elements(); int contatore = 1; %>
<% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
<% while (en.hasMoreElements()) 
   {
	red = (it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnuali)en.nextElement();%>
    <tr id="r<%=contatore%>" onclick="vai('<%=red.getChiave()%>', '<%=progressivoRecord%>', true)">
    	<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=red.getAnnoImposta()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=red.getIdeTelematico()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=(red.getCognome()==null?"-":red.getCognome())%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=(red.getNome()==null?"-":red.getNome())%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=(red.getCodiceFiscaleDic()==null?"-":red.getCodiceFiscaleDic())%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=(red.getDenominazione()==null?"-":red.getDenominazione())%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=(red.getDescTipoModello()==null || red.getDescTipoModello().equals("")?"-":red.getDescTipoModello())%></span></td>
	</tr>
<% contatore++;progressivoRecord ++;%>
<% }%>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<% if (finder != null){%>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="52">
<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>