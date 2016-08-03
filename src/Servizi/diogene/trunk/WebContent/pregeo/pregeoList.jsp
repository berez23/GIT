<%@ page language="java" %>
<%@page import="it.escsolution.escwebgis.pregeo.logic.PregeoLogic"%>
<%@page import="it.escsolution.escwebgis.pregeo.bean.PregeoInfo"%>
<%@page import="it.escsolution.escwebgis.pregeo.bean.ElementoListaFinder"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(PregeoLogic.LISTA_DATI_PREGEO); %>
<% String pathDatiDiogene = (String)sessione.getAttribute("PATH_DATI_DIOGENE"); %>
<% ElementoListaFinder finder = (ElementoListaFinder)sessione.getAttribute(PregeoLogic.FINDER); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>

<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<title>Pregeo - Lista </title>
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
			popUpAperte[index] = window.open("", "PopUpPregeoDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpPregeoDetail" + index, "width=640,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}



<% if (vlista.size() > 0){
PregeoInfo sElemento=(PregeoInfo)vlista.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=sElemento.getIdInfo()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Pregeo" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=funzionalita%>:&nbsp;Lista Informazioni Pregeo</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Pregeo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Tecnico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tecnico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nota</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PDF</span></td>
	</tr>
	
  <% PregeoInfo bInfo = new PregeoInfo(); %>
  <% java.util.Enumeration en = vlista.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% 
  	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
     while (en.hasMoreElements()) {
        bInfo = (PregeoInfo)en.nextElement();
        %>

     <tr id="r<%=contatore%>">
		<td onclick="vai('<%=bInfo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData"  onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=bInfo.getCodicePregeo() %></span></td>
		<td onclick="vai('<%=bInfo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData"  onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=bInfo.getDataPregeo()!=null?sdf.format(bInfo.getDataPregeo()):"" %></span></td>		
		<td onclick="vai('<%=bInfo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData"  onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=bInfo.getDenominazione() %></span></td>
		<td onclick="vai('<%=bInfo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData"  onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=bInfo.getTipoTecnico() %></span></td>
		<td onclick="vai('<%=bInfo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData"  onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=bInfo.getTecnico() %></span></td>		
		<td onclick="vai('<%=bInfo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData"  onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=bInfo.getFoglio() %></span></td>
		<td onclick="vai('<%=bInfo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData"  onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=bInfo.getParticella() %></span></td>
		<td onclick="vai('<%=bInfo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData"  onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=bInfo.getNota() %></span></td>
		<td class="extWinTDData"  onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData">
				<a href="<%= request.getContextPath()%>/OpenPdfServlet?nomePdf=<%=pathDatiDiogene%>/pregeo/<%=bInfo.getNomeFilePdf() %>" target="_blank"><%=bInfo.getNomeFilePdf() %></a>
			</span>
		</td>	
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="114">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>