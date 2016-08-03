<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@page import="java.util.*"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="it.escsolution.escwebgis.pregeo.logic.*"%>
<%@page import="it.escsolution.escwebgis.pregeo.bean.*"%>
<%@page import="java.text.*"%>


<html>
<head>
<title>Numero di PREGEO presentati su foglio</title>
<LINK rel="stylesheet"
	href="<%= request.getContextPath() %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script>
function vai(codice, record_cliccato, isPopUp){
	
	document.infoForm.KEYSTR.value=codice; // QUESTO PER SALTARE IN LISTA CON ST = 2
	document.infoForm.OGGETTO_SEL.value=codice; // QUESTO PER DETTAGLIO CON ST=3
	
	if (isPopUp)
	{
		targ = apriPopUp(record_cliccato);
		
		if (targ)
		{
			document.infoForm.ST.value = 33;
			document.infoForm.target = targ;
			document.infoForm.submit();
		}
	}
	else
	{
		wait();
		document.infoForm.ST.value = 3;
		document.infoForm.target = "_parent";
		document.infoForm.submit();
	}

}


var popUpAperte = new Array();
function apriPopUp(index)
{
if (popUpAperte[index])
{
	if (popUpAperte[index].closed)
	{
		popUpAperte[index] = window.open("", "PopUpPregeoDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
	popUpAperte[index] = window.open("", "PopUpPregeoDetail" + index, "width=640,height=480,status=yes,resizable=yes");
	return popUpAperte[index].name;
}
}


</script>

</head>

<body>

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Pregeo Presentati su Foglio </span>
</div>
<%
	String pk = request.getParameter("OGGETTO_SEL");
	
	EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
	EnvSource es = new EnvSource(eu.getEnte());
	PregeoLogic logic = new PregeoLogic(eu);
	Vector<PregeoInfo> lista = logic.caricaListaPregeoSuFoglio(pk, request);
	int contatore = 1; 
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	if(lista.size()>0) {%>
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
   
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Pregeo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Tecnico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tecnico</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">&nbsp;</span></td>
	</tr>
<%
   
    for (PregeoInfo bInfo : lista) {
%>
		
		     <tr id="r<%=contatore%>">
				<td onclick="vai('<%=bInfo.getChiave()%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData"><%=bInfo.getCodicePregeo() %></span></td>
				<td onclick="vai('<%=bInfo.getChiave()%>','<%=contatore%>',true)" class="extWinTDData"  style='cursor: pointer;'>
				<span class="extWinTXTData"><%=bInfo.getDataPregeo()!=null?sdf.format(bInfo.getDataPregeo()):"" %></span></td>		
				<td onclick="vai('<%=bInfo.getChiave()%>','<%=contatore%>',true)" class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=bInfo.getDenominazione() %></span></td>
				<td onclick="vai('<%=bInfo.getChiave()%>','<%=contatore%>',true)" class="extWinTDData" style='cursor: pointer;'>
				<span class="extWinTXTData"><%=bInfo.getTipoTecnico() %></span></td>
				<td onclick="vai('<%=bInfo.getChiave()%>','<%=contatore%>',true)" class="extWinTDData"  style='cursor: pointer;'>
				<span class="extWinTXTData"><%=bInfo.getTecnico() %></span></td>		
				<td onclick="vai('<%=bInfo.getChiave()%>','<%=contatore%>',true)" class="extWinTDData"  style='cursor: pointer;'>
				<span class="extWinTXTData"><%=bInfo.getFoglio() %></span></td>
				<td onclick="vai('<%=bInfo.getChiave()%>','<%=contatore%>',true)" class="extWinTDData"  style='cursor: pointer;'>
				<span class="extWinTXTData"><%=bInfo.getParticella() %></span></td>
				<td class="extWinTDData"  style='cursor: pointer;'>
					<span class="extWinTXTData">
					
						<a href="<%= request.getContextPath()%>/OpenPdfServlet?nomePdf=<%=bInfo.getPathFilePdf()%>" target="_blank">
							<%=bInfo.getNomeFilePdf()%>
						</a>
					</span>
				</td>	
			</tr>
	<% contatore++; }%>
	
	</table>
	
	<% } else{ %>
		<div align="center" >
		<span >Nessun Pregeo presentato</span>
	</div>
	
	<%} %>
	
	<form name="infoForm" method="post" action="<%=request.getContextPath()%>/Pregeo" id="infoForm" >
				<input type="hidden" name="DATASOURCE" value="<%=es.getDataSource()%>">
				<input type="hidden" name="ST" value="">
				<input type="hidden" name="UC" value="114">
				<input type="hidden" name="KEYSTR" value="">
				<input type="hidden" name="OGGETTO_SEL" value="">
	</form>							
										

<div id="wait" class="cursorWait" />
		
</body>

</html>
