<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% it.escsolution.escwebgis.tributi.bean.Contribuente cont=(it.escsolution.escwebgis.tributi.bean.Contribuente)sessione.getAttribute("CONTRIBUENTE"); %>
<% java.util.Vector vlistaICI=(java.util.Vector)sessione.getAttribute("LISTA_ICI_CONTRIBUENTI"); %>
<% java.util.Vector vlistaTARSU=(java.util.Vector)sessione.getAttribute("LISTA_TARSU_CONTRIBUENTI"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
		
<html>
<head>
<title>Tributi Contribuenti - Particelle</title>
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
function chgtr2(row,flg)
		{
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
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiOggettiICI";
		document.mainform.UC.value = 6;
	}else {
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiOggettiTARSU";
		document.mainform.UC.value = 7;
		}	
	document.mainform.OGGETTO_SEL.value=Particella;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.EXT.value = 1;
	document.mainform.submit();
    }
		
</script>


<body>
<form name="mainform" target="_parent" action="">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=cont.getCognome()%> <%=cont.getNome()%></span>
</div>

&nbsp;
               
   <table width="100%">
	
	<tr>

		<td width="50%"  valign="top" style="background-color: #FFFFFF;">

			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				
				<tr> 
					<td colspan=8 class="extWinTDTitle"><span class="extWinTXTTitle">Oggetti ICI</span></td> 
				</tr>
		
				<tr>		
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Inizio Possesso</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine Possesso</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
				</tr>
				
				<% if (vlistaICI.size() == 0){%>
     	 		<tr>
		        	<td colspan='8' class="extWinTDData">
					<span class="extWinTXTData">Non è presente alcun record associato</span></td></tr>
				<%}%>
		
	<%it.escsolution.escwebgis.tributi.bean.OggettiICI ici = new it.escsolution.escwebgis.tributi.bean.OggettiICI(); %>
  	<%java.util.Enumeration en = vlistaICI.elements(); int contatore = 1; %>
  	<% long progressivoRecord = 1; %>
  	<%while (en.hasMoreElements()) {
    ici = (it.escsolution.escwebgis.tributi.bean.OggettiICI)en.nextElement();%>
        
    
    			<tr id="r<%=contatore%>" onclick="vai(1,'<%=ici.getChiave()%>', '<%=progressivoRecord%>')">
    
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getComune() %></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getFoglioCatasto()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getParticellaCatasto()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getSubalterno()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getCategoria()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getDataInizio()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getDataFine()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getProvenienza()%></span></td>
				</tr>
		
	<% contatore++;progressivoRecord ++;} %>
		</table>
		</td>
	</tr>
</table>		
&nbsp;
	<table width="100%">
	
	<tr>
			<td width="50%"  valign="top" style="background-color: #FFFFFF;">
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				
				<tr> 
					<td colspan="7" class="extWinTDTitle"><span class="extWinTXTTitle">Oggetti TARSU</span></td>
				</tr>
		
				<tr>		
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie Totale</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
				</tr>
				
				<% if (vlistaTARSU.size() == 0){%>
     	 <tr>
		        <td colspan='7' class="extWinTDData">
				<span class="extWinTXTData">Non è presente alcun record associato</span></td>
		<%}%>
		
  <%it.escsolution.escwebgis.tributi.bean.OggettiTARSU tarsu = new it.escsolution.escwebgis.tributi.bean.OggettiTARSU(); %>
  <% java.util.Enumeration en1 = vlistaTARSU.elements(); int contatore1 = 1; %>
  <% long progressivoRecord1 = 1 ; %>
  <% while (en1.hasMoreElements()) {
        tarsu = (it.escsolution.escwebgis.tributi.bean.OggettiTARSU)en1.nextElement();%>



    			<tr id="p<%=contatore1%>" onclick="vai(2,'<%=tarsu.getChiave()%>', '<%=progressivoRecord1%>')">
		
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getComune()%></span></td>		
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)'  style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getDescrCategoria()%></span></td>
	    			<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getFoglio()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getParticella()%></span></td>	
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getSubalterno()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getSupTotale()%></span></td>
					<td class="extWinTDData" onmouseover='chgtr2(<%=contatore1%>,1)' onmouseout='chgtr2(<%=contatore1%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=tarsu.getProvenienza()%></span></td>
				</tr>
		
  <% contatore1++;progressivoRecord1++;} %>
		</table>
		</td>
	</tr>
	
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="5">
<input type='hidden' name="EXT" value="">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>