<%@ page language="java" import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.enel.bean.*,it.escsolution.escwebgis.enel.servlet.*,it.escsolution.escwebgis.enel.logic.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Enel enel = (Enel) sessione.getAttribute(EnelLogic.ENEL); %>
<%   java.util.ArrayList listaSemEnel = (java.util.ArrayList) sessione.getAttribute(EnelLogic.LISTA_SEMESTRALITA_UTENZE); %>

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

%>

	

	
<html>
<head>
<title>Semestralita Utenze Enel - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Enel" target="_parent">

&nbsp;
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<% 	if (listaSemEnel!= null ){%>
<span class="TXTmainLabel">Lista Semestralita Utenze Enel</span>
<br>
<table class="extWinTable" style="width: 60%;">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">POT.IMP.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CONS.MED.MENS.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA ALL.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA CONTR.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.CONTR.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">RECAPITO.NOMINATIVO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">LOCALITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CAP</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SEMESTRE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">STATO UTENZA</span></td>
	</tr>			  
<% 
	java.util.Iterator it = listaSemEnel.iterator();
	int contatore = 1;
	while (it.hasNext()) 
	{
		Enel enelList = (Enel) it.next(); 
		if(enelList.getPotenzaImpegnata()==null)
			enelList.setPotenzaImpegnata("-");
		if(enelList.getConsumoMedioMensile()==null)
			enelList.setConsumoMedioMensile("-");
		if(enelList.getDataAllaccio()==null)
			enelList.setDataAllaccio("-");
		if(enelList.getDataContratto()==null)
			enelList.setDataContratto("-");
		if(enelList.getCodiceContratto()==null)
			enelList.setCodiceContratto("-");
		if(enelList.getRecapitoNominativo()==null)
			enelList.setRecapitoNominativo("-");
		if(enelList.getRecapitoIndirizzo()==null)
			enelList.setRecapitoIndirizzo("-");
		if(enelList.getRecapitoLocalita()==null)
			enelList.setRecapitoLocalita("-");
		if(enelList.getRecapitoCap()==null)
			enelList.setRecapitoCap("-");
		if(enelList.getSemestre()==null)
			enelList.setSemestre("-");
		if(enelList.getStatoUtenza()==null)
			enelList.setStatoUtenza("-");
%>
	<tr>
	    <td class="extWinTDData" class="extWinTDTitle" >
			<span class="extWinTXTData"><%= enelList.getPotenzaImpegnata() %></span>
		</td>
		<td class="extWinTDData" class="extWinTDTitle" >
			<span class="extWinTXTData"><%= enelList.getConsumoMedioMensile() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle" >
			<span class="extWinTXTData"><%= enelList.getDataAllaccio() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle" >
			<span class="extWinTXTData"><%= enelList.getDataContratto() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle" >
			<span class="extWinTXTData"><%= enelList.getCodiceContratto() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle" >
			<span class="extWinTXTData"><%= enelList.getRecapitoNominativo() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle">
			<span class="extWinTXTData"><%= enelList.getRecapitoIndirizzo() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle" >
			<span class="extWinTXTData"><%= enelList.getRecapitoLocalita() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle">
			<span class="extWinTXTData"><%= enelList.getRecapitoCap() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle">
			<span class="extWinTXTData"><%= enelList.getSemestre() %></span>
		</td>
	    <td class="extWinTDData" class="extWinTDTitle">
			<span class="extWinTXTData"><%= enelList.getStatoUtenza() %></span>
		</td>
	</tr>
<%
		contatore++;
		}
%>
	<input type='hidden' name="OGGETTO_SEL" value="">
<%	
	}
%>
</table>

</td>
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