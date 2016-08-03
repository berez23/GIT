<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<!--% java.util.Vector vlistaImmobili=(java.util.Vector)request.getAttribute("LISTA_IMMOBILI"); %-->

<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaPertinenze=(java.util.Vector)sessione.getAttribute("LISTA_PERTINENZE"); %>
<% it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitFinder finder = (it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitFinder)sessione.getAttribute("FINDER106"); 
	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();
%>


<html>
<head>
<title>Pertinenze Abitative - Lista</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>

function chgtr(row, flg){
		if (flg==1)
			{
			document.all("r"+row).style.backgroundColor = "#d7d7d7";
			}
		else
			{
			document.all("r"+row).style.backgroundColor = "";
			}
		}


var wmapopend;


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
			popUpAperte[index] = window.open("", "PopUpUNIMMDetail" + index, "width=640,height=480,status=no,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpUNIMMDetail" + index, "width=640,height=480,status=no,resizable=yes");
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

function vai_old(pkid_unimm, record_cliccato) {
	wait();
	document.mainform.OGGETTO_SEL.value=pkid_unimm;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}

function mettiST(){
	//alert("mettiST");
	document.mainform.ST.value = 2;
}


<% if (vlistaPertinenze.size() > 0){
	it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbit pPertinenzeAbit=(it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbit)vlistaPertinenze.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pPertinenzeAbit.getChiave()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>
</script>

<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PertinenzeAbit" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Elenco Pertinenze </span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Categ.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod. Fisc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Perc. Poss.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Titolo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Nascita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
		<td class="extWinTDTitle" style="text-align: center; vertical-align: middle;">
			<input type="button" class="tdButton" value="Chiudi tutto" title="chiudi tutte le finestre di dettaglio aperte..." onClick="chiudiTutteLePopUp();" />
		</td>
		<td class="extWinTDTitle" colspan=3><span class="extWinTXTTitle">Mappa</span></td>
	</tr>
        
        

  <% it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbit pertinenzeAbit = new it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbit(); %>
  <% java.util.Enumeration en = vlistaPertinenze.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
	  pertinenzeAbit = (it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbit)en.nextElement();%>



    <tr id="r<%=contatore%>" >
		
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getFoglio()%></span></td>	
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getParticella()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getSubalterno()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getCategoria()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getCf()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getPercPoss()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getTipoTitolo()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getCognome()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getNome()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getDataNascita()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getIndirizzo()%></span></td>
		<td onclick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=pertinenzeAbit.getCivico()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style="text-align: center; vertical-align: middle;">
			<input type="button" class="tdButton" value="Dettaglio" title="apre il dettaglio in una finestra separata..." onClick="vai('<%=pertinenzeAbit.getChiave()%>', '<%=progressivoRecord%>', true)" />
		</td>
		
		<td onclick="zoomInMappaParticelle('<%= pertinenzeAbit.getCodNazionale() %>','<%=pertinenzeAbit.getFoglio()%>','<%=pertinenzeAbit.getParticella()%>');" class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span></td>

		<td class="extWinTDData" style="text-align:center;">
			<span class="extWinTXTData">
			<a href="javascript:apriPopupVirtualH(<%=pertinenzeAbit.getLatitudine()==null?0:pertinenzeAbit.getLatitudine()%>,<%=pertinenzeAbit.getLongitudine()==null?0:pertinenzeAbit.getLongitudine()%>);">
			<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
			</a>
			</span>
		</td>
		
		<td class="extWinTDData" style="text-align:center;">
			<span class="extWinTXTData">
			<a href="javascript:apriPopupStreetview(<%=pertinenzeAbit.getLatitudine()==null?0:pertinenzeAbit.getLatitudine()%>,<%=pertinenzeAbit.getLongitudine()==null?0:pertinenzeAbit.getLongitudine()%>);">
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
<input type='hidden' name="UC" value="106">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>