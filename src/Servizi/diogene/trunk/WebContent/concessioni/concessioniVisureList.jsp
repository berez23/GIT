<%@ page language="java" import="it.escsolution.escwebgis.concessioni.bean.*, 
								 it.escsolution.escwebgis.concessioni.logic.*, 
								 it.escsolution.escwebgis.concessioni.servlet.*, java.text.SimpleDateFormat " %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaConcessioni = (java.util.Vector) sessione.getAttribute(ConcessioniVisureLogic.LISTA); %>
<% ConcessioneFinder finder = (ConcessioneFinder) sessione.getAttribute(ConcessioniVisureServlet.NOMEFINDER); %>
<% SimpleDateFormat SDF_1 = new SimpleDateFormat("yyyyMMdd"); 
   SimpleDateFormat SDF_2 = new SimpleDateFormat("dd/MM/yyyy"); %>
<% int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null){
		Integer bjc = (Integer)sessione.getAttribute("BACK_JS_COUNT");
		if (bjc != null){
			//out.println("BACK: " + bjc.intValue());			
			js_back = (bjc).intValue();
		}
	}
%>  
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>Concessioni Edilizie Visure - Lista</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>

function chgtr(row,flg)
		{
		if (flg==1)
			{
			document.getElementById("r"+row).style.backgroundColor = "#d7d7d7";
			//document.all("r"+row).style.backgroundColor = "#d7d7d7";
			}
		else
			{
			document.getElementById("r"+row).style.backgroundColor = "";
			//document.all("r"+row).style.backgroundColor = "";
			}
		}


function vai(codice, record_cliccato){
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
				document.mainform.popup.value='true';
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
		alert(e);
	}
}



var popUpAperte = new Array();
function apriPopUp(index)
{
	if (popUpAperte[index])
	{
		if (popUpAperte[index].closed)
		{
			popUpAperte[index] = window.open("", "PopUpConcessioniVisureDetail" + index, "width=700,height=400,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpConcessioniVisureDetail" + index, "width=700,height=400,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}
		

<% if (vlistaConcessioni.size() > 0){
	ConcessioneVisura lc = (ConcessioneVisura) vlistaConcessioni.get(0);%>

	function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=lc.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ConcessioniVisure" target="_parent" >
 
 <input type='hidden' name="popup" value="false">
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Lista Concessioni Visure</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO ATTO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME INTESTATARIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NUMERO ATTO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SEDIME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME VIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CIVICO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM. PROT. GEN.</span></td>				
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM. PROT. SETT.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA DOC.</span></td>
	</tr>
	
  <% ConcessioneVisura co = null; %>
  <% java.util.Enumeration en = vlistaConcessioni.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        co = (ConcessioneVisura) en.nextElement();
        
        String dataDoc = co.getDataDoc();
        try{
        	dataDoc = SDF_2.format(SDF_1.parse(co.getDataDoc()));
        }catch(Exception e){}
  %>

    <tr id="r<%=contatore%>" onclick="vai('<%=co.getChiave()%>', '<%=progressivoRecord%>', true)"> 
    
    
	    <td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= co.getTipoAtto() + (Costanti.htTipiAtti.get(co.getTipoAtto())!=null ? "-" + (String)Costanti.htTipiAtti.get(co.getTipoAtto()) : "")%></span></td>
   		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= co.getNomeIntestatario() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= co.getNumeroAtto() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= co.getTpv() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= co.getNomeVia() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= co.getCivicoSub() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= co.getNumProtGen() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= co.getNumProtSett() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=dataDoc!=null ? dataDoc : ""%></span></td>
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<% if (finder != null) out.println(finder.getPaginaAttuale());%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="47">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>