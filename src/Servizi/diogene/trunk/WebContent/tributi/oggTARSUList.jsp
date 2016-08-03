<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaTARSU=(java.util.Vector)sessione.getAttribute("LISTA_TARSU"); %>
<% it.escsolution.escwebgis.tributi.bean.OggettiTARSUFinder finder = (it.escsolution.escwebgis.tributi.bean.OggettiTARSUFinder)sessione.getAttribute("FINDER7"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		
<html>
<head>
<title>Tributi Oggetti TARSU - Lista</title>
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


function vai(particella, record_cliccato){
	wait();
	document.mainform.OGGETTO_SEL.value=particella;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}

function mettiST(){
	document.mainform.ST.value = 2;
}

<% if (vlistaTARSU.size() > 0){
it.escsolution.escwebgis.tributi.bean.OggettiTARSU pTARSU=(it.escsolution.escwebgis.tributi.bean.OggettiTARSU)vlistaTARSU.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pTARSU.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
	<%}%>


</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiOggettiTARSU" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Oggetti TARSU</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COMUNE<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUPERFICIE TOTALE<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">MAPPA<span class="extWinTXTTitle"></td>
	</tr>
	
	<% it.escsolution.escwebgis.tributi.bean.OggettiTARSU tarsu = new it.escsolution.escwebgis.tributi.bean.OggettiTARSU(); %>
  <% java.util.Enumeration en = vlistaTARSU.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        tarsu = (it.escsolution.escwebgis.tributi.bean.OggettiTARSU)en.nextElement();%>



    <tr id="r<%=contatore%>">
		<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor:pointer;'>
		<span class="extWinTXTData"><%=tarsu.getComune()%></span></td>
		<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=tarsu.getFoglio()%></span></td>
		<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=tarsu.getParCatastali()%></span></td>	
		<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=tarsu.getSubalterno()%></span></td>		
		<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=tarsu.getSupTotale()%></span></td>
		<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><%=tarsu.getProvenienza()%></span></td>
		<td onclick="zoomInMappaParticelle('<%= tarsu.getCodEnte() %>','<%=tarsu.getFoglio()%>','<%=tarsu.getParCatastali()%>');" class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
		<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span></td>
		<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
			<span class="extWinTXTData">
				<a href="javascript:apriPopupVirtualH(<%=tarsu.getLatitudine()==null?0:tarsu.getLatitudine()%>,<%=tarsu.getLongitudine()==null?0:tarsu.getLongitudine()%>);">
				<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
				</a>
			</span>
		</td>
		<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
			<span class="extWinTXTData">
				<a href="javascript:apriPopupStreetview(<%=tarsu.getLatitudine()==null?0:tarsu.getLatitudine()%>,<%=tarsu.getLongitudine()==null?0:tarsu.getLongitudine()%>);">
				<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
				</a>
			</span>
		</td>	
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="7">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>