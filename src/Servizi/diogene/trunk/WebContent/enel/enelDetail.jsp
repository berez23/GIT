<%@ page language="java" import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.enel.bean.*,it.escsolution.escwebgis.enel.servlet.*,it.escsolution.escwebgis.enel.logic.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Enel enel = (Enel) sessione.getAttribute(EnelLogic.ENEL); %>
<%   java.util.ArrayList listaEnel = (java.util.ArrayList) sessione.getAttribute(EnelLogic.LISTA_DETTAGLIO_ENEL); %>

<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  EnelFinder finder = null;

	if (sessione.getAttribute(EnelServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(EnelServlet.NOMEFINDER)).getClass() == new EnelFinder().getClass()){
			finder = (EnelFinder)sessione.getAttribute(EnelServlet.NOMEFINDER);
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

	

	
<html>
<head>
<title>Utenti Enel - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function visualizzaSemestralita(chiave)
{
	wait();
	document.mainform.OGGETTO_SEL.value=chiave;	
	document.mainform.ST.value=4;
	document.mainform.submit();
}
function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Enel" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">Dettaglio Utenti Enel</span>
</div>
&nbsp;
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" style="width: 100%;">

			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Nominativo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= enel.getNominativo() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Cod.Fisc/P.Iva</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= enel.getPivacodfisc() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<br><br><br>

<% 	if (listaEnel!= null ){%>
<span class="TXTmainLabel">Lista Utenze Enel / Utente</span>
<br>
<table class="extWinTable" style="width: 60%;">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOMINATIVO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.FISC./P.IVA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">STATO UTENZA</span></td>
	</tr>			  
<% 
	java.util.Iterator it = listaEnel.iterator();
	int contatore = 1;
	while (it.hasNext()) 
	{
		Enel enelList = (Enel) it.next(); 
		if(enelList.getNominativo()==null)
			enelList.setNominativo("-");
		if(enelList.getPivacodfisc()==null)
			enelList.setPivacodfisc("-");
		if(enelList.getIndirizzo()==null)
			enelList.setIndirizzo("-");
		if(enelList.getStatoUtenza()==null)
			enelList.setStatoUtenza("-");
%>
	<tr onclick="visualizzaSemestralita('<%=enelList.getIdentificativo()%>')">
	    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= enelList.getNominativo() %></span>
		</td>
		<td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= enelList.getPivacodfisc() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= enelList.getIndirizzo() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%= enelList.getStatoUtenza() %></span>
		</td>
	</tr>
<%
		contatore++;
		}
	}
%>
</table>

</td>
<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>
<br><br><br>

<center><span class="extWinTXTTitle"><a href="javascript:"></a></span></center>

<br><br>

<!-- FINE solo dettaglio -->
<% if (enel != null){%>
<% String codice = "";
   codice = enel.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="32">
		<input type='hidden' name="EXT" value="">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>