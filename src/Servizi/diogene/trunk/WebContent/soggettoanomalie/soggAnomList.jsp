<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaSogg=(java.util.Vector)sessione.getAttribute("LISTA_SOGG_ANOM"); %>
<% it.escsolution.escwebgis.soggettoanomalie.bean.SoggettoAnomaliaFinder finder = (it.escsolution.escwebgis.soggettoanomalie.bean.SoggettoAnomaliaFinder)sessione.getAttribute("FINDER28"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
<head>
<title>Soggetti - Lista Anomalie</title>
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


<% if (vlistaSogg.size() > 0){
it.escsolution.escwebgis.soggettoanomalie.bean.SoggettoAnomalia sSogg=(it.escsolution.escwebgis.soggettoanomalie.bean.SoggettoAnomalia)vlistaSogg.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=sSogg.getPk_idinternosgtscarti()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/SoggettoAnomalie" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Soggetti con Anomalia</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Regola</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data caricamento</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod. Ente</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod. Fisc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Part. IVA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Pers.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sesso</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data nasc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Luogo nasc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune nasc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">fk_db</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">fk_chiave</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Descrizione</span></td>
	</tr>
	
	<% it.escsolution.escwebgis.soggettoanomalie.bean.SoggettoAnomalia sogg = new it.escsolution.escwebgis.soggettoanomalie.bean.SoggettoAnomalia(); %>
  <% java.util.Enumeration en = vlistaSogg.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        sogg = (it.escsolution.escwebgis.soggettoanomalie.bean.SoggettoAnomalia)en.nextElement();%>



    <tr id="<%=contatore%>" >
    <td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getRegola()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getData_caricamento()%></span></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getCodent()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getCod_fisc()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getPart_iva()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getTipo_persona()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getDenominazione()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getCognome()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getNome()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getSesso()%></span></td>	
		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getNasdata()%></span></td>			
 		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getNasluogo()%></span></td>			
  		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getNascodcom()%></span></td>			
  		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getDescrDB()%></span></td>			
  		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getFk_chiave()%></span></td>			
  		<td class="extWinTDData" >
		<span class="extWinTXTData"><%=sogg.getDescrizione()%></span></td>			
		
	</tr>
	
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="28">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>