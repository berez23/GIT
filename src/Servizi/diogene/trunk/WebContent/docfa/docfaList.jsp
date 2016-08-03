<%@ page language="java" import="it.escsolution.escwebgis.docfa.bean.*, it.escsolution.escwebgis.docfa.logic.*, it.escsolution.escwebgis.docfa.servlet.* " %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaDocfa = (java.util.Vector) sessione.getAttribute(DocfaLogic.LISTA_DOCFA); %>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<% DocfaFinder finder = (DocfaFinder) sessione.getAttribute(DocfaServlet.NOMEFINDER); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  

<%@page import="java.util.ArrayList"%>
<html>
<head>
<title>Docfa - Lista</title>
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
	<% if (!(request.getParameter("popupDaCondono") != null && new Boolean(request.getParameter("popupDaCondono")).booleanValue())) { %>
		wait();
	<% }%>
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
			popUpAperte[index] = window.open("", "PopUpDocfaDetail" + index, "width=900,height=480,status=yes,resizable=yes, scrollbars=auto");
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
		popUpAperte[index] = window.open("", "PopUpDocfaDetail" + index, "width=900,height=480,status=yes,resizable=yes, scrollbars=auto");
		return popUpAperte[index].name;
	}
}


<% if (vlistaDocfa.size() > 0){
Docfa docfa = (Docfa) vlistaDocfa.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=docfa.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Docfa" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;
	Elenco Docfa</span>
</div>


               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROTOCOLLO</span></td>
	<!--<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA VARIAZIONE</span></td> -->
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA PROTOCOLLO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CAUSALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SOPPRESSIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">VARIAZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COSTITUZIONE</span></td>
		<!-- 
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DENOMINAZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTITA IVA</span></td>
		-->
		<td class="extWinTDTitle"><span class="extWinTXTTitle">OPERAZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CATEGORIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DICHIARANTE</span></td>
	<!--	<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO_DICHIARANTE</span></td> -->
	<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO_PARTICELLA</span></td> 
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROFESSIONISTA</span></td>
	</tr>
	
	<% Docfa docfa; %>
  <% java.util.Enumeration en = vlistaDocfa.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
	  docfa = (Docfa) en.nextElement();%>

    <tr id="r<%=contatore%>" onclick="vai('<%=docfa.getChiave()%>', '<%=progressivoRecord%>', true)">
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getProtocollo() %></span></td>
			<!-- <td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%//= docfa.getDataVariazione() %></span></td> -->
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getDataProtocollo() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getCausale() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getSoppressione() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getVariazione() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getCostituzione() %></span></td>
			<!--  
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getCognome() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getNome() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getDenominazione() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getCodiceFiscale() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getPartitaIva() %></span></td>
			-->
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getOperazione() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getFoglio()%></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getParticella() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getSubalterno() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getCategoria() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getDichiarante() %></span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData">
			<%ArrayList appoInd = docfa.getIndPart() ;
			 for(int i=0;i<appoInd.size();i++)
			 {
				 out.println((String)appoInd.get(i));	
				 out.println("<BR/>");
			 }
			 %>
			</span></td>
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'><span class="extWinTXTData"><%= docfa.getTecCognome() + " " + docfa.getTecNome() %></span></td>
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="43">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
<% if (request.getParameter("popupDaCondono") != null && new Boolean(request.getParameter("popupDaCondono")).booleanValue()) { %>
	<input type='hidden' name="foglio" value="<%=request.getParameter("foglio")%>">
	<input type='hidden' name="mappale" value="<%=request.getParameter("mappale")%>">
	<input type='hidden' name="sub" value="<%=request.getParameter("sub")%>">
	<input type='hidden' name="popupDaCondonoDett" value="true">
<% } %>

</table>
</form>

<% if (request.getParameter("popupDaCondono") != null && new Boolean(request.getParameter("popupDaCondono")).booleanValue()) { %>
	&nbsp;
	<div align="center"><span class="extWinTXTTitle">
		<a class="iFrameLink" href="javascript:window.close();">chiudi questa finestra</a></span>
	</div>
<%}%>

<div id="wait" class="cursorWait" />
</body>
</html>