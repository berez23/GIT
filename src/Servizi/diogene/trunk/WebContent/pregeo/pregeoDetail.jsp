<%@page language="java" import="it.escsolution.escwebgis.istat.bean.*"%>
<%@page import="it.escsolution.escwebgis.pregeo.bean.PregeoInfo"%>
<%@page import="it.escsolution.escwebgis.pregeo.logic.PregeoLogic"%>
<%@page import="it.escsolution.escwebgis.pregeo.bean.ElementoListaFinder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.escsolution.escwebgis.pregeo.bean.PregeoFornitura"%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   String pathDatiDiogene = (String)sessione.getAttribute("PATH_DATI_DIOGENE"); %>
<%   ArrayList<PregeoInfo> alInfo =(ArrayList<PregeoInfo>)sessione.getAttribute(PregeoLogic.PREGEO_DETT); %>
<%   ArrayList<PregeoFornitura> alForniture =(ArrayList<PregeoFornitura>)sessione.getAttribute(PregeoLogic.PREGEO_DETT_FOR); %>
<%   ArrayList<String> alAltro =(ArrayList<String>)sessione.getAttribute(PregeoLogic.PREGEO_DETT_ALTRO); %>
<%   
	PregeoInfo bInfo = null;
	if (alInfo != null && alInfo.size()>0){
		bInfo = (PregeoInfo)alInfo.get(0);
	}
	
	PregeoFornitura bFornitura = null;
	if (alForniture != null && alForniture.size()>0){
		bFornitura = (PregeoFornitura)alForniture.get(0);
	}

	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();
%>

<%  ElementoListaFinder finder = null;

	if (sessione.getAttribute(PregeoLogic.FINDER) !=null){
		if (((Object)sessione.getAttribute(PregeoLogic.FINDER)).getClass() == new ElementoListaFinder().getClass()){
			finder = (ElementoListaFinder)sessione.getAttribute(PregeoLogic.FINDER);
			}
	}
	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}

SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

String part = bInfo.getParticella() + ", ";
if (alAltro != null && alAltro.size()>0){
	for (int i=0; i<alAltro.size(); i++){
		part += alAltro.get(i) + ", ";	
	}
}

%>
	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>


<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<title>Dettaglio Pregeo</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>
var url0=null;
var url1=null;
var url2 =null;

function mettiST(){
	document.mainform.ST.value = 3;
}



</script>
<body onload="mettiST()">
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Pregeo" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=funzionalita%>:&nbsp;Dettaglio Pregeo</span>
</div>


<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center  cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel" colspan="4" align="center"><span class="TXTmainLabel">Codice Pregeo</span></td>
			</tr>
			<tr>
				<td class="TDviewTextBox" colspan="4" align="left"><span class="TXTviewTextBox"><%=bInfo.getCodicePregeo() %></span></td>				
			</tr>

			<tr>
				<td class="TDmainLabel" colspan="4" align="center"><span class="TXTmainLabel">Data Pregeo</span></td>
			</tr>
			<tr>
				<td class="TDviewTextBox" colspan="4" align="left"><span class="TXTviewTextBox"><%=sdf.format(bInfo.getDataPregeo()) %></span></td>				
			</tr>

			<tr>
				<td class="TDmainLabel" colspan="4" align="center"><span class="TXTmainLabel">Denominazione</span></td>
			</tr>
			<tr>
				<td class="TDviewTextBox" colspan="4" align="left"><span class="TXTviewTextBox"><%=bInfo.getDenominazione() %></span></td>				
			</tr>

			<tr>
				<td class="TDmainLabel" colspan="4" align="center"><span class="TXTmainLabel">Tecnico</span></td>
			</tr>
			<tr>
				<td class="TDviewTextBox" colspan="4" align="left"><span class="TXTviewTextBox"><%=bInfo.getTipoTecnico() + " " + bInfo.getTecnico() %></span></td>				
			</tr>
			
			<tr>
				<td class="TDmainLabel"  ><span class="TXTmainLabel">Foglio</span></td>
				<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=bInfo.getFoglio() %></span></td>

				<td class="TDmainLabel"  ><span class="TXTmainLabel">Particella</span></td>
				<td class="TDviewTextBox" ><span class="TXTviewTextBox"><%=part %></span></td>
			</tr>
			
			<tr>
				<td class="TDmainLabel" colspan="4" align="center"><span class="TXTmainLabel">Nota</span></td>
			</tr>
			<tr>
				<td class="TDviewTextBox" colspan="4" align="left"><span class="TXTviewTextBox"><%=bInfo.getNota() %></span></td>				
			</tr>
			
			<tr>
				<td class="TDmainLabel" colspan="4" align="center"><span class="TXTmainLabel">PDF</span></td>
			</tr>
			<tr>
				<td class="TDviewTextBox" colspan="4" align="left"><span class="TXTviewTextBox">
					<a href="<%= request.getContextPath()%>/OpenPdfServlet?nomePdf=<%=pathDatiDiogene%>/pregeo/<%=bInfo.getNomeFilePdf() %>" target="_blank"><%=bInfo.getNomeFilePdf() %></a>
				</span></td>				
			</tr>
			
			<tr>
				<td class="TDmainLabel" colspan="4" align="center"><span class="TXTmainLabel">Relazione Tecnica</span></td>
			</tr>
			<tr>
				<td class="TDviewTextBox" colspan="4" align="left"><span class="TXTviewTextBox">
				<%
				if (bInfo != null && bInfo.getRelazioneTecnica() != null){
					out.println( bInfo.getRelazioneTecnica().replaceAll("(\r\n|\n\r|\r|\n)", "<br />") );
				}else{
					out.println("");	
				}
				%></span></td>				
			</tr>

			
		</table>
		</td>
	</tr>
</table>

<br>
<br>




</td>

</tr>
</table>

		
<br></br>



<!-- FINE solo dettaglio -->
<% if (bInfo != null){%>
 
   <input type='hidden' name="OGGETTO_SEL" >
   
   
		
<%} if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="114">
		<input type='hidden' name="BACK" value="">
		
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">


</form>

<div id="wait" class="cursorWait" />
</body>
</html>