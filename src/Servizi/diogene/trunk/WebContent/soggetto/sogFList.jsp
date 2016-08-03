<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% 
java.util.Vector vlistaSogg=(java.util.Vector)sessione.getAttribute("LISTA_SOGGETTIFASCICOLO");
java.util.List vlistaDB=(java.util.List)sessione.getAttribute("LISTA_SOGGETTIFASCICOLO_DB");


 %>
<% it.escsolution.escwebgis.soggetto.bean.SoggettoFascicoloFinder finder = (it.escsolution.escwebgis.soggetto.bean.SoggettoFascicoloFinder)sessione.getAttribute("FINDER25"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  
 
<html>
<head>
<title>Soggetto Fascicolo - Lista</title>
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

function vaiD(url){
	//wait();
	//parent.location.replace(url);

	window.open(url,'dettaglio','toolbar=no,scrollbars=auto,resizable=yes,width=800,height=600');
	// marcoric , questo non funzionava , ho messo quello sopra 
	// parent.location.href(url);
}	


function mettiST(){
	document.mainform.ST.value = 2;
}


<% if (vlistaSogg.size() > 0){
it.escsolution.escwebgis.soggetto.bean.SoggettoFascicolo pSog=(it.escsolution.escwebgis.soggetto.bean.SoggettoFascicolo)vlistaSogg.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pSog.getCodSoggetto()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/SoggettoFascicolo" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Lista Soggetti</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita Iva</span></td>
		<% for (int i=0;i<vlistaDB.size();i++){
			it.escsolution.escwebgis.soggetto.bean.DataBase db_i =(it.escsolution.escwebgis.soggetto.bean.DataBase)vlistaDB.get(i);%>
			<td class="extWinTDTitle"><span class="extWinTXTTitle"><%=db_i.getDescrizione()%></span></td>
		<%}
		%>
	</tr>
	
	<% it.escsolution.escwebgis.soggetto.bean.SoggettoFascicolo sog = new it.escsolution.escwebgis.soggetto.bean.SoggettoFascicolo(); %>
  <% java.util.Enumeration en = vlistaSogg.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        sog = (it.escsolution.escwebgis.soggetto.bean.SoggettoFascicolo)en.nextElement();%>
   		
   	<%--  TEST MB	<tr id="r<%=contatore%>" onclick="vai('<%=sog.getCodSoggetto()%>', '<%=progressivoRecord%>')"> 
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%=sog.getDenominazione()%></span></td>	
			<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><%=sog.getCodiceFiscale()%></span></td>
			--%>
			<tr id="r<%=contatore%>" >
			<td class="extWinTDData">
			<span class="extWinTXTData"><%=sog.getDenominazione()%></span></td>	
			<td class="extWinTDData" >
			<span class="extWinTXTData"><%=sog.getCodiceFiscale()%></span></td>
			<td class="extWinTDData" >
			<span class="extWinTXTData"><%=sog.getPartitaIva()%></span></td>
		<%-- fine test MB --%>	
			<%java.util.List presDB = sog.getPresenzeDB();
			 for (int j=0;j<presDB.size();j++){
				it.escsolution.escwebgis.soggetto.bean.DataBase db_j =(it.escsolution.escwebgis.soggetto.bean.DataBase)presDB.get(j);
				if (db_j.getElementi().size()>0){
				%>
			<%-- 	<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' onclick="vaiD('<%=db_j.getUrlDB()%>')"  style='cursor: pointer;'> --%>
				<td class="extWinTDData" onclick="vaiD('<%=db_j.getUrlDB()%>')" style='cursor: pointer;'>
				<span class="extWinTXTData">
			<%--	<input type="Image" align="MIDDLE"  src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/ok.gif"  border="0" vspace="0" hspace="0" onclick="vaiD('<%=db_j.getUrlDB()%>')" style='cursor: pointer;' > --%>
				<img src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/ok.gif" align="MIDDLE" border="0" vspace="0" hspace="0" onclick="vaiD('<%=db_j.getUrlDB()%>')" style='cursor: pointer;'>
				</span></td>
				<%}else{%>
		<%--   	 <td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'> --%>
				<td class="extWinTDData" style='cursor: default;'>
				<span class="extWinTXTData">
				<img src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/no.gif" align="MIDDLE" border="0" vspace="0" hspace="0" style='cursor: default;'>
			<%--	<input type="Image" align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/no.gif"  border="0" vspace="0" hspace="0"  style='cursor: default;'> --%>  

				</span></td>
			<%}
			}
			%>	
		</tr>
		<%contatore++;progressivoRecord ++;
	} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="25">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>