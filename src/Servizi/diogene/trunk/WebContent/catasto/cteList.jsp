<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaTerreni=(java.util.Vector)sessione.getAttribute("LISTA_TERRENI"); %>
<% it.escsolution.escwebgis.catasto.bean.TerreniFinder finder = (it.escsolution.escwebgis.catasto.bean.TerreniFinder)sessione.getAttribute("FINDER2"); %>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
	 
<%	boolean soloAtt = ((Boolean)sessione.getAttribute(it.escsolution.escwebgis.catasto.logic.CatastoTerreniLogic.SOLO_ATT)).booleanValue(); %>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>Catasto Terreni - Lista</title>
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

<% if (vlistaTerreni.size() > 0){
it.escsolution.escwebgis.catasto.bean.Terreno pTerreno=(it.escsolution.escwebgis.catasto.bean.Terreno)vlistaTerreni.get(0); %>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pTerreno.getParticella()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
 
<%}%>
 
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
			popUpAperte[index] = window.open("", "PopUpTERDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpTERDetail" + index, "width=640,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}

function chiudiTutteLePopUp()
{
	try
	{
		for (index in popUpAperte)
		{
			if (!popUpAperte[index].closed)
				popUpAperte[index].close();
		}
		popUpAperte = new Array();
	}
	catch (e)
	{
		alert(e);
	}
}

 
function vai_old(Particella,record_cliccato){
	wait();
	document.mainform.OGGETTO_SEL.value=Particella;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
	
function mettiST(){
	document.mainform.ST.value = 2;
}


</script>

<body onload="mettiST()">


<form name="mainform"  target="_parent" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoTerreni">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Particelle Terreni</span>
</div>
		
&nbsp;

<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato</span></td>
		<!-- <td class="extWinTDTitle" style="text-align: center; vertical-align: middle;">
			<input type="button" class="tdButton" value="Chiudi tutto" title="chiudi tutte le finestre di dettaglio aperte..." onClick="chiudiTutteLePopUp();" />
		</td> -->
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">Mappa</span></td>
	</tr>


   <%it.escsolution.escwebgis.catasto.bean.Terreno terreno = new it.escsolution.escwebgis.catasto.bean.Terreno(); %>
  <%java.util.Enumeration en = vlistaTerreni.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <%while (en.hasMoreElements()) {
        terreno = (it.escsolution.escwebgis.catasto.bean.Terreno)en.nextElement();
   	    String chiave = terreno.getParticella();
   %>
        
    
    <tr id="r<%=contatore%>" <%if (terreno.isEvidenza() && !soloAtt) {%> style = "color:green; font-weight:bold;" <%} %>>
    
    <td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
	<span class="extWinTXTData"><%=terreno.getSezione() %></span></td>
	<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
	<span class="extWinTXTData"><%=terreno.getFoglio() %></span></td>
	<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
	<span class="extWinTXTData"><%=terreno.getNumero()%></span></td>
	<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
	<span class="extWinTXTData"><%=terreno.getSubalterno()%></span></td>
		
	<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true )" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
	<span class="extWinTXTData"><%=terreno.getStato()%></span></td>	
	
	<td onclick="zoomInMappaParticelle('<%= terreno.getCodente() %>','<%=terreno.getFoglio()%>','<%=terreno.getNumero()%>');" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; text-align: center;'>
	<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span></td>
	
	<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; text-align: center;'>
	<a href="javascript:apriPopupVirtualH(<%=terreno.getLatitudine()==null?0:terreno.getLatitudine()%>,<%=terreno.getLongitudine()==null?0:terreno.getLongitudine()%>);">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
							</a>
	</td>
	<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; text-align: center;'>
	<a href="javascript:apriPopupStreetview(<%=terreno.getLatitudine()==null?0:terreno.getLatitudine()%>,<%=terreno.getLongitudine()==null?0:terreno.getLongitudine()%>);">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
							</a>
	</td>
	
 <% contatore++;progressivoRecord ++;} %>
  
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="2">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
</body>
</html>