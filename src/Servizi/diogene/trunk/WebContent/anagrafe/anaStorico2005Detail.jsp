<%@ page language="java" import="it.escsolution.escwebgis.anagrafe.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(it.escsolution.escwebgis.anagrafe.logic.AnagrafeStorico2005Logic.ANAGRAFE_STORICO); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico2005Finder finder = null;

	if (sessione.getAttribute(it.escsolution.escwebgis.anagrafe.servlet.AnagrafeStorico2005Servlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(it.escsolution.escwebgis.anagrafe.servlet.AnagrafeStorico2005Servlet.NOMEFINDER)).getClass() == new AnagrafeStorico2005Finder().getClass()){
			finder = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico2005Finder)sessione.getAttribute(it.escsolution.escwebgis.anagrafe.servlet.AnagrafeStorico2005Servlet.NOMEFINDER);
			}
					
	}
	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>Anagrafe Storico 2005 - Dettaglio Soggetto</title>
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


function mettiST(){
	document.mainform.ST.value = 3;
}
function apriPopUp(posList)
{
	window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/anagrafe/dettaglioFamiglia2005.jsp?pos='+posList,'dettaglioFamiglia','toolbar=no,scrollbars=auto,resizable=yes,width=400,height=300');
}
</script>
<body onload="mettiST()">
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeStorico2005" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=funzionalita%>:&nbsp;Dettaglio Storico Anagrafe 2005 Soggetto</span> 
</div>
		
&nbsp;

<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SESSO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">RESIDENZA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA INIZIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FINE</span></td>
	 	<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.FAMIGLIA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DETT.FAMIGLIA</span></td>
	
	</tr>
	
	<% it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico ana = new it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico(); %>
  <% java.util.Enumeration en = vlista.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        ana = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico)en.nextElement();%>
    	<tr id="r<%=contatore%>">
		
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
		<span class="extWinTXTData"><%=ana.getCodFiscale()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ana.getSesso()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ana.getDescrVia()%> <%=ana.getCivico()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ana.getInizioResidenza()%></span></td>	
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ana.getFineResidenza()%></span></td>
		
		 
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=ana.getCodFamiglia()%></span></td>
		<td onclick="apriPopUp('<%=contatore%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData">
		<img src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/fwdOne.gif">
		</span></td>
		
		</tr>
	
<% 		contatore++;
		progressivoRecord ++;
	} 
%>

</table>

	
<br><br><br>




<br><br>

<% String codice = "";%>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">

<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="36">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait" />
</body>
</html>