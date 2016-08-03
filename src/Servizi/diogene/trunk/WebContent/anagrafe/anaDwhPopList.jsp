<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%  
	String lastColor = (String)sessione.getAttribute("LAST_COLOR");
	String lastFamily = (String)sessione.getAttribute("LAST_FAMILY");
	String origine = (String)sessione.getAttribute("origine");
	java.util.Vector vlistaAnagrafe=(java.util.Vector)sessione.getAttribute(AnagrafeDwhLogic.LISTA_ANAGRAFEDWH); 
	
	it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder finder = null;
	if (sessione.getAttribute(AnagrafeDwhServlet.NOMEFINDER)!= null){
		finder = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder)sessione.getAttribute(AnagrafeDwhServlet.NOMEFINDER); 
	}else 
		finder = new it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder();
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");

	String keystr= ((String)sessione.getAttribute("KEYSTR")!= null ? (String)sessione.getAttribute("KEYSTR") :"") ;
	String queryForKeyStr= ((String)sessione.getAttribute("queryForKeyStr")!= null ? (String)sessione.getAttribute("queryForKeyStr") :"") ;
%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>

<%@page import="it.escsolution.escwebgis.anagrafe.servlet.AnagrafeDwhServlet"%>
<%@page import="it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic"%>
<html>
<head>
<title>Anagrafe anagrafe - Popup Lista</title>
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


<% if (vlistaAnagrafe.size() > 0){
it.escsolution.escwebgis.anagrafe.bean.Anagrafe pAna=(it.escsolution.escwebgis.anagrafe.bean.Anagrafe)vlistaAnagrafe.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pAna.getIdExt()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeDwh" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Elenco Anagrafe</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COD. FAMIGLIA<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODI_ANAGR<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA NASCITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Mappa</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.anagrafe.bean.Anagrafe ana = new it.escsolution.escwebgis.anagrafe.bean.Anagrafe(); %>
  <% java.util.Enumeration en = vlistaAnagrafe.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>

  <%
  int colore = 0;
  //out.println("LAST COLOR: " + lastColor);
  //out.println("LAST FAMILY: " + lastFamily);
  //out.println("ORIGINE: " + origine);
  if ( lastColor != null && !lastColor.trim().equalsIgnoreCase("") )
	  colore = Integer.parseInt(lastColor);

  
  String lastFam = "";
  if ( lastColor != null && !lastColor.trim().equalsIgnoreCase("") )
	  lastFam = lastFamily;
  
  String[] colori = new String[]{"#98FB98", "#FFDEAD"};
  %>
	
 
  <% 
     while (en.hasMoreElements()) {
        ana = (it.escsolution.escwebgis.anagrafe.bean.Anagrafe)en.nextElement();
		if (ana != null){
			if ( lastFam != null && ana.getFamiglie() != null && !lastFam.trim().equalsIgnoreCase(ana.getFamiglie().trim()) ) {
				colore++;
				if (colore > 1){
					colore = 0;
				}
				lastFam = ana.getFamiglie();
			}
		%>
    <tr id="r<%=contatore%>">
  		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer; background-color:<%=colori[colore]%>'>
		<span class="extWinTXTData"><%=ana.getFamiglie()%></span></td>
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer; background-color:<%=colori[colore]%>''>
		<span class="extWinTXTData"><%=ana.getCodAnagrafe()%></span></td>
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; background-color:<%=colori[colore]%>''>
		<span class="extWinTXTData"><%=ana.getCognome()%></span></td>
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; background-color:<%=colori[colore]%>''>
		<span class="extWinTXTData"><%=ana.getNome()%></span></td>	
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; background-color:<%=colori[colore]%>''>
		<span class="extWinTXTData"><%=ana.getCodFiscale()%></span></td>
		<td onclick="vai('<%=ana.getId()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; background-color:<%=colori[colore]%>''>
		<span class="extWinTXTData"><%=ana.getDataNascita()%></span></td>
		<td onclick="zoomInMappaAnagrafeDwh('<%= ana.getCodiceNazionale() %>','<%=ana.getId()%>')" class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span></td>
	</tr>
	<% 
		}
	%>	
<% 
	contatore++;
	progressivoRecord ++;

	} %>
<input type='hidden' name="origine" value="<%=origine %>">
<input type='hidden' name="LAST_FAMILY" value="<%=lastFam %>">
<input type='hidden' name="LAST_COLOR" value="<%=colore %>">
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<% if (finder != null){%>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="45">
<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

<input type="hidden" name="KEYSTR" value="<%=keystr%>">
<input type="hidden" name="queryForKeyStr" value="<%=queryForKeyStr%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>