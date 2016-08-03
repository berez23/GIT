<%@ page language="java" import="it.escsolution.escwebgis.catasto.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<% HttpSession sessione = request.getSession(true);  %> 
<% IntestatarioF intF=(IntestatarioF)request.getAttribute("INTESTATARIOF"); %>

<% java.util.Vector vlistaImmobili=(java.util.Vector)request.getAttribute("LISTA_IMMOBILI2"); %>
<% java.util.Vector vlistaTerreni=(java.util.Vector)request.getAttribute("LISTA_TERRENI2"); %>



<html>
<head>
<title>Catasto Intestatari Persona fisica - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script>
function visualizzaParticelle(){
	wait();
	document.mainform.ST.value=4;
	document.mainform.EXT.value=1;
	document.mainform.submit();
}
function mettiST(){
	document.mainform.ST.value = 3;
}

function vaiPart(tipo, Particella, record_cliccato){
	wait();
	if (tipo == 1){
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoTerreni";
		document.mainform.UC.value = 2;
		}else {
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoImmobili";
		document.mainform.UC.value = 1;
		}
	document.mainform.OGGETTO_SEL.value=Particella;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.EXT.value = 1;
	document.mainform.submit();
    }
</script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<body  onload="mettiST()">
<form name="mainform" target="_parent" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoIntestatariF">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Scheda Intestatari Fisici</span>
</div>

<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>

&nbsp;




	<% if (intF != null && intF.getCodIntestatario()!= null && !intF.getCodIntestatario().equals("") ){%>
	<table style="background-color: white; width: 100%;">

<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top;">

<table cellpadding="0" cellspacing="0" align=center>
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getCognome()%></span></td>
			</tr>
		</table>
		</td>	
		
	</tr>
	
	<tr>
		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getNome()%></span></td>
			</tr>
		</table>
		</td>
		
	</tr>
	
	<tr>		
				
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Sesso</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getSesso()%></span></td>
			</tr>			
		</table>
		</td>
		
	</tr>
	
	<tr>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Data di Nascita</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getDataNascita()%></span></td>
			</tr>
		</table>
		</td>
		
	</tr>

	<tr>		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Luogo di Nascita</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getLuogo()%></span></td>
			</tr>
		</table>
		</td>
		
	</tr>
	
	<tr>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getCodFiscale()%></span></td>
			</tr>
		</table>
		</td>
		
	</tr>
	<tr>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Data Fine Validità</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=intF.getDataFine()%></span></td>
			</tr>
		</table>
		</td>
		
	</tr>
</table>
</td>

</tr>


</table>

<% } else { %>
		<br><br>
	
		        	<div align="center"><span class="extWinTXTData" >Non è presente alcun dato da visualizzare</span></div>
						
			
<% } %>

<br><br><br>


<%-- 
<center><span class="extWinTXTTitle"><a href="javascript:visualizzaParticelle()">Visualizza le Titolarità di questo soggetto </a></span></center>
--%>
<%-- TEST MB --%>
<center><span class="extWinTXTTitle">Titolarità</span></center>
<table width="100%">
	
	<tr>

		<td width="50%"  valign="top" style="background-color: #FFFFFF;">

			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				
				<tr> 
					<td colspan=7 class="extWinTDTitle"><span class="extWinTXTTitle">Particelle Terreni</span></td> 
				</tr>
		
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez.</span></td>	
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Poss.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
				</tr>
				
				<% if (vlistaTerreni.size() == 0){%>
     	 		<tr>
		        	<td colspan='7' class="extWinTDData">
					<span class="extWinTXTData">Non è presente alcun record associato</span></td></tr>
				<%}%>
		
	<%it.escsolution.escwebgis.catasto.bean.Terreno terreno = new it.escsolution.escwebgis.catasto.bean.Terreno(); %>
  	<%java.util.Enumeration en = vlistaTerreni.elements(); int contatore = 1; %>
  	<% long progressivoRecord = 1; %>
  	<%while (en.hasMoreElements()) {
    terreno = (it.escsolution.escwebgis.catasto.bean.Terreno)en.nextElement();%>
        
    
    			<tr id="r<%=contatore%>" >
    				<td class="extWinTDData" >
					<span class="extWinTXTData"><%=terreno.getSezione() %></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=terreno.getFoglio() %></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=terreno.getNumero()%></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=terreno.getSubalterno()%></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=terreno.getDataFinePos()%></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=terreno.getDataFine()%></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=terreno.getTitolo()%></span></td>
	
				</tr>
		
	<% contatore++;progressivoRecord ++;} %>
		</table>
		</td>
		
		<td width="50%"  valign="top" style="background-color: #FFFFFF;">
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				
				<tr> 
					<td colspan=8 class="extWinTDTitle"><span class="extWinTXTTitle">Particelle Fabbricati</span></td>
				</tr>
		
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>					
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Categoria</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Poss.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Val.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
				</tr>
				
				<% if (vlistaImmobili.size() == 0){%>
     	 <tr>
		        <td colspan='8' class="extWinTDData">
				<span class="extWinTXTData">Non è presente alcun record associato</span></td>
		<%}%>
		
  <% it.escsolution.escwebgis.catasto.bean.Immobile immobile = new it.escsolution.escwebgis.catasto.bean.Immobile(); %>
  <% java.util.Enumeration en1 = vlistaImmobili.elements(); int contatore1 = 1; %>
  <% long progressivoRecord1 = 1 ; %>
  <% while (en1.hasMoreElements()) {
        immobile = (it.escsolution.escwebgis.catasto.bean.Immobile)en1.nextElement();%>



    			<tr id="p<%=contatore1%>" >
		
		
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=immobile.getSezione()%></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=immobile.getFoglio()%></span></td>
	    			<td class="extWinTDData" >
					<span class="extWinTXTData"><%=immobile.getNumero()%></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=immobile.getUnimm()%></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=immobile.getCodCategoria()%></span></td>
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=immobile.getDataFinePos()%></span></td>
					
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=immobile.getDataFineVal()%></span></td>
					
					<td class="extWinTDData" >
					<span class="extWinTXTData"><%=immobile.getTitolo()%></span></td>
					
					
	
					
				</tr>
		
  <% contatore1++;progressivoRecord1++;} %>
		</table>
		</td>
	</tr>
</table>	
<%-- FINE TEST MB --%>
<br><br>
<% if (intF != null){%>
<% String codice = "";
   codice = intF.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
<%}else{%>
   <input type='hidden' name="OGGETTO_SEL" value="">
<%} 
%>

<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="ST" value="3">
<input type='hidden' name="UC" value="3">
<input type='hidden' name="EXT" value="">
<input type='hidden' name="BACK" value="">


</form>



<div align="center"><span class="extWinTXTTitle">
			<a class="iFrameLink" href="javascript:window.close();">chiudi questa finestra</a></span>
		</div>
</body>
</html>