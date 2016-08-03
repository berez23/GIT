<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<!--% java.util.Vector vlistaImmobili=(java.util.Vector)request.getAttribute("LISTA_IMMOBILI"); %-->

<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaGauss=(java.util.Vector)sessione.getAttribute("LISTA_GAUSS"); %>
<% it.escsolution.escwebgis.catasto.bean.GaussFinder finder = (it.escsolution.escwebgis.catasto.bean.GaussFinder)sessione.getAttribute("FINDER12"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>Particelle - Lista</title>
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


function vai(Particella, record_cliccato, layer){
	wait();
	document.mainform.LAYER.value=layer;
	document.mainform.OGGETTO_SEL.value=Particella;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}

function mettiST(){
	//alert("mettiST");
	document.mainform.ST.value = 2;
}

function vai(codice, record_cliccato,  layer, isPopUp)
{
	try
	{
		document.mainform.LAYER.value=layer;
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
			popUpAperte[index] = window.open("", "PopUpGaussDetail" + index, "width=640,height=300,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpGaussDetail" + index, "width=640,height=300,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}


<% if (vlistaGauss.size() > 0){
	it.escsolution.escwebgis.catasto.bean.Gauss pGauss=(it.escsolution.escwebgis.catasto.bean.Gauss)vlistaGauss.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pGauss.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>


</script>

<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoGauss" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Particelle</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>	
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Fine</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Layer</span></td>
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">Mappa</span></td>		
	</tr>
        
        

  <% it.escsolution.escwebgis.catasto.bean.Gauss gauss = new it.escsolution.escwebgis.catasto.bean.Gauss(); %>
  <% java.util.Enumeration en = vlistaGauss.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        gauss = (it.escsolution.escwebgis.catasto.bean.Gauss)en.nextElement();%>

    <tr id="r<%=contatore%>">
		<td onclick="vai('<%=gauss.getChiave()%>', '<%=progressivoRecord%>', '<%=gauss.getLayer()%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gauss.getComune()%></span></td>
		<td onclick="vai('<%=gauss.getChiave()%>', '<%=progressivoRecord%>', '<%=gauss.getLayer()%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gauss.getSezione()%></span></td>	
		<td onclick="vai('<%=gauss.getChiave()%>', '<%=progressivoRecord%>', '<%=gauss.getLayer()%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gauss.getFoglio()%></span></td>
		<td onclick="vai('<%=gauss.getChiave()%>', '<%=progressivoRecord%>', '<%=gauss.getLayer()%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gauss.getParticella()%></span></td>
		<td onclick="vai('<%=gauss.getChiave()%>', '<%=progressivoRecord%>', '<%=gauss.getLayer()%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gauss.getDataFine()%></span></td>
		<td onclick="vai('<%=gauss.getChiave()%>', '<%=progressivoRecord%>', '<%=gauss.getLayer()%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=gauss.getLayer()%></span></td>	
		<td onclick="zoomInMappaParticelle('<%= gauss.getFkComune() %>','<%=gauss.getFoglio()%>','<%=gauss.getParticella()%>');" class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
		</td>
		<td class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<a href="javascript:apriPopupVirtualH(<%=gauss.getLatitudine()==null?0:gauss.getLatitudine()%>,<%=gauss.getLongitudine()==null?0:gauss.getLongitudine()%>);" >
				<span class="extWinTXTData"><img src="../ewg/images/3D.gif" border="0" width="24" height="30" /></span>
			</a>
		</td>
		<td class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<a href="javascript:apriPopupStreetview(<%=gauss.getLatitudine()==null?0:gauss.getLatitudine()%>,<%=gauss.getLongitudine()==null?0:gauss.getLongitudine()%>);" >
				<span class="extWinTXTData"><img src="../ewg/images/streetview.gif" border="0" width="17" height="32" /></span>
			</a>
		</td>		
	</tr>
		
  <% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="LAYER" value="">
  
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<% if (finder != null){%>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="12">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>