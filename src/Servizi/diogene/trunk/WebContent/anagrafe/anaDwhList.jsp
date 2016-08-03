<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%  

	java.util.Vector vlistaAnagrafe=(java.util.Vector)sessione.getAttribute(AnagrafeDwhLogic.LISTA_ANAGRAFEDWH); 
	
	it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder finder = null;
	if (sessione.getAttribute(AnagrafeDwhServlet.NOMEFINDER)!= null){
		finder = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder)sessione.getAttribute(AnagrafeDwhServlet.NOMEFINDER); 
	}else 
		finder = new it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder();
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
		
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>

<%@page import="it.escsolution.escwebgis.anagrafe.servlet.AnagrafeDwhServlet"%>
<%@page import="it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic"%>
<html>
<head>
<title>Anagrafe anagrafe - Lista</title>
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
			popUpAperte[index] = window.open("", "PopUpAnaDwhDetail" + index, "width=640,height=670,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpAnaDwhDetail" + index, "width=640,height=670,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}


function mettiST(){
	document.mainform.ST.value = 2;
}


<% if (vlistaAnagrafe.size() > 0){
it.escsolution.escwebgis.anagrafe.bean.Anagrafe pAna=(it.escsolution.escwebgis.anagrafe.bean.Anagrafe)vlistaAnagrafe.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pAna.getIdExt()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeDwh" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=funzionalita%>:&nbsp;Elenco Anagrafe</span>
</div>
		
&nbsp;
               
   <script src="<%=request.getContextPath()%>/ewg/Scripts/orderFilter.js" language="JavaScript"></script>
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODI_ANAGR<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle">
			<jsp:include page="../orderFilter/orderFilter.jsp">
				<jsp:param value="COGNOME" name="orderFilter.label"/>
				<jsp:param value="COGNOME" name="orderFilter.orderField"/>
			</jsp:include>
		</td>
		<td class="extWinTDTitle">
			<jsp:include page="../orderFilter/orderFilter.jsp">
				<jsp:param value="NOME" name="orderFilter.label"/>
				<jsp:param value="NOME" name="orderFilter.orderField"/>
			</jsp:include>
		</td>
		<td class="extWinTDTitle">
			<jsp:include page="../orderFilter/orderFilter.jsp">
				<jsp:param value="CODICE FISCALE" name="orderFilter.label"/>
				<jsp:param value="AnagrafeDwh" name="orderFilter.servlet"/>
				<jsp:param value="CODICE_FISCALE" name="orderFilter.orderField"/>
			</jsp:include>
		</td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA NASCITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Mappa</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.anagrafe.bean.Anagrafe ana = new it.escsolution.escwebgis.anagrafe.bean.Anagrafe(); %>
  <% java.util.Enumeration en = vlistaAnagrafe.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>

	
 
  <% 
     while (en.hasMoreElements()) {
        ana = (it.escsolution.escwebgis.anagrafe.bean.Anagrafe)en.nextElement();
		%>
    <tr id="r<%=contatore%>" style="">
		
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=ana.getCodAnagrafe()%></span></td>
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=ana.getCognome()%></span></td>
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=ana.getNome()%></span></td>	
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=ana.getCodFiscale()%></span></td>
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=ana.getDataNascita()%></span></td>
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=ana.getResidenza()!=null ? ana.getResidenza() +(ana.getCivici()!=null? ", "+ana.getCivici():""):"-"%></span></td>
		<td onclick="zoomInMappaAnagrafeDwh('<%= ana.getCodiceNazionale() %>','<%=ana.getIdExt()%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; text-align: center;'>
		<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif" border="0"/></span></td>
	</tr>
	
<% 
	contatore++;
	progressivoRecord ++;

	} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<% if (finder != null){%>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="45">
<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>