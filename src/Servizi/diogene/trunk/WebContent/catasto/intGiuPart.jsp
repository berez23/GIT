<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% it.escsolution.escwebgis.catasto.bean.IntestatarioG intG=(it.escsolution.escwebgis.catasto.bean.IntestatarioG)sessione.getAttribute("INTESTATARIOG"); %>
<% java.util.Vector vlistaImmobili=(java.util.Vector)sessione.getAttribute("LISTA_IMMOBILI2"); %>
<% java.util.Vector vlistaTerreni=(java.util.Vector)sessione.getAttribute("LISTA_TERRENI2"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
		
<html>
<head>
<title>Catasto Intestatari Giuridici - Particelle</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>
function chgtr(row,flg){
	if (flg==1)
		{
		document.all("r"+row).style.backgroundColor = "#d7d7d7";
		}
	else
		{
		document.all("r"+row).style.backgroundColor = "";
		}
}
function chgtr2(row,flg){
	if (flg==1)
		{
		document.all("p"+row).style.backgroundColor = "#d7d7d7";
		}
	else
		{
		document.all("p"+row).style.backgroundColor = "";
		}
}
		
function vai(tipo, Particella, record_cliccato){
	wait();
	if (tipo == 1){
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoTerreni";
		document.mainform.UC.value = 2;
		}
		else {
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

<body>
<form name="mainform" target="_parent" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoIntestatariG">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=intG.getDenominazione()%></span>
</div>

&nbsp;


<table width="100%">
	<tr>

	<td width="50%"  valign="top" style="background-color: #FFFFFF;">

		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">	
	
			<tr> <td colspan=5 class="extWinTDTitle"><span class="extWinTXTTitle">Particelle Terreni</span></td> 
			</tr>
	

			<tr>		
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Poss.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
			</tr>
			
			<% if (vlistaTerreni.size() == 0){%>
     	 <tr>
		        <td colspan='5' class="extWinTDData">
				<span class="extWinTXTData">Non è presente alcun record associato</span></td>
		<%}%>
				
	<%it.escsolution.escwebgis.catasto.bean.Terreno terreno = new it.escsolution.escwebgis.catasto.bean.Terreno(); %>
  	<%java.util.Enumeration en = vlistaTerreni.elements(); int contatore = 1; %>
  	<% long progressivoRecord = 1; %>
  	<%while (en.hasMoreElements()) {
    terreno = (it.escsolution.escwebgis.catasto.bean.Terreno)en.nextElement();%>
        
    
    		<tr id="r<%=contatore%>" onclick="vai(1,'<%=terreno.getParticella()%>', '<%=progressivoRecord%>')">
    
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getFoglio() %></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getNumero()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getSubalterno()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getDataFinePos()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=terreno.getTitolo()%></span></td>
			</tr>
		
	<% contatore++;progressivoRecord ++;} %>
		</table>
	</td>
		
	<td width="50%" valign="top" style="background-color: #FFFFFF;">
		
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			
				<tr> 
					<!-- <td colspan=4 class="extWinTDTitle"> -->
					<td colspan='6' class="extWinTDTitle">
					<span class="extWinTXTTitle">Particelle Fabbricati</span></td>		
				</tr>
		
				<tr>		
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Categoria</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Poss.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>				
				</tr>
				
				<% if (vlistaImmobili.size() == 0){%>
     	 <tr>
		        <td colspan='6' class="extWinTDData">
				<span class="extWinTXTData">Non è presente alcun record associato</span></td>
		<%}%>
		
  <% it.escsolution.escwebgis.catasto.bean.Immobile immobile = new it.escsolution.escwebgis.catasto.bean.Immobile(); %>
  <% java.util.Enumeration en1 = vlistaImmobili.elements(); int contatore1 = 1; %>
  <% long progressivoRecord1 = 1 ; %>
  <% while (en1.hasMoreElements()) {
        immobile = (it.escsolution.escwebgis.catasto.bean.Immobile)en1.nextElement();%>



    			<tr id="p<%=contatore1%>" onclick="vai(2,'<%=immobile.getChiave()%>', '<%=progressivoRecord1%>')">
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getFoglio()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getNumero()%></span></td>	
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getUnimm()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getCodCategoria()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getDataFinePos()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=immobile.getTitolo()%></span></td>
				</tr>
		
  <% contatore1++;progressivoRecord1++;} %>
			</table>
		</td>
	</tr>
	
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="4">
<input type='hidden' name="EXT" value="">
<input type='hidden' name="BACK" value="">		
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>