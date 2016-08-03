<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaCond=(java.util.Vector)sessione.getAttribute("LISTA_NOTE"); %>
<% it.escsolution.escwebgis.dup.bean.DupElementoListaNoteFinder finder = (it.escsolution.escwebgis.dup.bean.DupElementoListaNoteFinder)sessione.getAttribute("FINDER24"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>DUP - Lista Note</title>
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


function vai(codice, record_cliccato){
	wait();
	document.mainform.OGGETTO_SEL.value=codice;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}

function mettiST(){
	document.mainform.ST.value = 2;
}

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
			popUpAperte[index] = window.open("", "PopUpNoteDetail" + index, "width=900,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpNoteDetail" + index, "width=900,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}



<% if (vlistaCond.size() > 0){
it.escsolution.escwebgis.dup.bean.DupElementoListaNote sFornit=(it.escsolution.escwebgis.dup.bean.DupElementoListaNote)vlistaCond.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=sFornit.getIdFornitura()%>@<%=sFornit.getIdNota()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/DupNote" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Note</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Rettifica</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Favore/Contro</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Reg. Patrim.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Ind. immobile</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">Mappa</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.dup.bean.DupElementoListaNote nota = new it.escsolution.escwebgis.dup.bean.DupElementoListaNote(); %>
  <% java.util.Enumeration en = vlistaCond.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        nota = (it.escsolution.escwebgis.dup.bean.DupElementoListaNote)en.nextElement();%>



     <tr id="r<%=contatore%>">
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getFlagRettifica()%></span></td>
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getCodiceFiscale()%></span></td>
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getCognome()%></span></td>
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getNome()%></span></td>
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getDenominazione()%></span></td>		
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getFavoreContro()%></span></td>		
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getRegimePatrimoniale()%></span></td>	
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getIndirizzoImmobile()%></span></td>	
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getFoglio()%></span></td>	
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getNumero()%></span></td>	
		<td onclick="vai('<%=nota.getIdFornitura()%>@<%=nota.getIdNota()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=nota.getSubalterno()%></span></td>
		<td onclick="zoomInMappaParticelle('<%=nota.getCodEnte()%>','<%=nota.getFoglio()%>','<%=nota.getNumero()%>');" class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span></td>
		<td class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<a href="javascript:apriPopupVirtualH(<%=nota.getLatitudine()==null?0:nota.getLatitudine()%>,<%=nota.getLongitudine()==null?0:nota.getLongitudine()%>);" >
				<span class="extWinTXTData"><img src="../ewg/images/3D.gif" border="0" width="24" height="30" /></span>
			</a>
		</td>
		<td class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<a href="javascript:apriPopupStreetview(<%=nota.getLatitudine()==null?0:nota.getLatitudine()%>,<%=nota.getLongitudine()==null?0:nota.getLongitudine()%>);" >
				<span class="extWinTXTData"><img src="../ewg/images/streetview.gif" border="0" width="17" height="32" /></span>
			</a>
		</td>
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="24">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>