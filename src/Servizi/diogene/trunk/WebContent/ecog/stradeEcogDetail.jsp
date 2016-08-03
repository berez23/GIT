<%@ page language="java" import="it.escsolution.escwebgis.ecografico.bean.*,
								it.escsolution.escwebgis.ecog.logic.*,
								it.escsolution.escwebgis.ecog.servlet.*,
								it.escsolution.escwebgis.common.interfacce.*,
								it.escsolution.escwebgis.common.*,
								it.webred.cet.permission.CeTUser,
								it.escsolution.escwebgis.diagnostiche.util.DiaBridge"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%	StradaEcografico str=(StradaEcografico)sessione.getAttribute(EcograficoStradeLogic.STRADA);
	java.util.Vector vlistaCivici=(java.util.Vector)sessione.getAttribute(EcograficoCiviciLogic.LISTA_CIVICI_STRADA);
    it.escsolution.escwebgis.ecog.bean.StradaFinder finder = null; %>
<%  it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%if (sessione.getAttribute("FINDER116") != null){ 
		finder = (it.escsolution.escwebgis.ecog.bean.StradaFinder)sessione.getAttribute("FINDER116"); 
	}%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}

InterfacciaObject interfaccia = (InterfacciaObject) sessione.getAttribute(EcograficoStradeServlet.INTERFACCIA_CIVICI);

%>	
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
	
<%@page import="java.util.ArrayList"%>
<html>
<head>
<title>Ecografico Strada - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.jsp" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script> 
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>


function visDia(params) {
	params += '&<%=DiaBridge.SESSION_KEY%>=<%=EcograficoStradeLogic.STRADA%>&popup=yes';
	window.open('<%= request.getContextPath() %>/DiagnosticheViewer' + params,'','toolbar=no,scrollbars=yes,resizable=yes,width=800,height=600');
}

<!--

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

function mettiST(){
	document.mainform.ST.value = 3;
}

function vaiElencoCivici(oggettoSel) 
{
	try
	{
		if (wait)
			wait();
		document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Interfaccia";
		document.mainform.OGGETTO_SEL.value=oggettoSel;
		document.mainform.submit();
	} catch (e)
	{
		alert(e.message);
	}
}

function vaiC(civico){
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/EcograficoCivici";
	document.mainform.OGGETTO_SEL.value=civico;
	document.mainform.ST.value = 3;
	document.mainform.UC.value = 117;
	document.mainform.EXT.value = 1;
	document.mainform.submit();
    }

//-->
</script>
<body >

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;
	Dati Strada </span>
</div>
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
	<br/>
	
<jsp:include page="../frame/iFrameLinkIndice.jsp" />


<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/EcograficoStrade" target="_parent">


<% 
	EnvUtente eu = new EnvUtente((CeTUser)sessione.getAttribute("user"), null, null); 
	String ente = eu.getEnte();
	String name = eu.getUtente().getName();
%>

<div style="margin-top: 15px;">
	<span class="TXTmainLabel">
		<%=DiaBridge.getDiaHtmlTestata(ente, name, str, request.getContextPath())%>
	</span>
</div>


<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Numero Strada</span></td>
				<td class="TDviewTextBox" style="width:200;"><span class="TXTviewTextBox"><%= (str.getCodiceStrada() == null) ? "&nbsp;-&nbsp;" : str.getCodiceStrada() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
		
	<tr></tr>
	<tr></tr>
	<tr></tr>
		
	<tr>		
		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sedime</span></td>
				<td class="TDviewTextBox" style="width:200;"><span class="TXTviewTextBox"><%= str.getSpecieStrada() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
		
	<tr></tr>
	<tr></tr>
	<tr></tr>
		
	<tr>	

				
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Nome Via</span></td>
				<td class="TDviewTextBox" style="width:200;"><span class="TXTviewTextBox"><%=str.getNomeStrada()%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
		
	<tr>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:200;"><span class="TXTviewTextBox"><%=str.getComune()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	

</table>

</td>

</tr>


</table>


<div class="tabber">

	<% if (vlistaCivici != null && vlistaCivici.size()>0) {%>

		<div class="tabbertab">
		
		<h2>Elenco Civici</h2>


  
        <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">
					Elenco Civici
			</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME VIA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CIVICO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO CIVICO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO ACCESSO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">LATO STRADA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ACCESSIBILITA'</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">USO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA RECORD</span></td>
			</tr>
			
			<% CivicoEcografico civ = new CivicoEcografico(); %>
		  <% java.util.Enumeration en = vlistaCivici.elements(); int contatore = 1; %>
		  <% while (en.hasMoreElements()) {
		        civ = (CivicoEcografico)en.nextElement();%>
		
		   <tr id="r<%=contatore%>" onclick="vaiC('<%=civ.getId()%>')">
				
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getDescrStrada()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getDescrizioneCivico()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getTipoCivico()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getTipoAccesso()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getLatoStrada()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getAccessibilita()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getDescrDestUso()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getFoglioCt()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getParticellaCt()%></span></td>
				<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=civ.getDataRecord()%></span></td>
			</tr>
			
		<% contatore++;} %>
	</table>       
   
	</div>
	<% } %>

</div>
<!-- FINE solo dettaglio -->

<br><br>

		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="116">
		<input type='hidden' name="OGGETTO_SEL" value="<%= str.getId() %>">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="RECORD_SEL" value="">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>

<div id="wait" class="cursorWait" />
</body>
</html>