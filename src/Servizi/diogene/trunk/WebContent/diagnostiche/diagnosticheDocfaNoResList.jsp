<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="sc" uri="/sqlcatalog"%>
<%@ page import="java.sql.*,java.util.*,org.displaytag.tags.TableTag "%>
<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  
String daData = (String)sessione.getAttribute(DiagnosticheDocfaNoResLogic.DA_DATA_FORNITURA);
String aData = (String)sessione.getAttribute(DiagnosticheDocfaNoResLogic.A_DATA_FORNITURA);
%> 
<% java.util.Vector vlistaCond=(java.util.Vector)sessione.getAttribute(DiagnosticheDocfaNoResLogic.LISTA_DIAGNOSTICHE); %>
<% java.util.Vector vlistaCat=(java.util.Vector)sessione.getAttribute(DiagnosticheDocfaNoResLogic.LISTA_CATEGORIE); %>
<% DiagnosticheDocfaNoResFinder finder = (DiagnosticheDocfaNoResFinder)sessione.getAttribute(DiagnosticheDocfaNoResServlet.NOMEFINDER); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  

<%@page import="it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheDocfaNoResLogic"%>
<%@page import="it.escsolution.escwebgis.diagnostiche.servlet.DiagnosticheDocfaNoResServlet"%>
<%@page import="it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheDocfaNoResFinder"%>
<%@page import="it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheDocfaNoRes"%>
<html>
<head>
<title>Diagnostiche Docfa Non Residenziali</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>

function chgtr(row,flg)
		{
		if (flg==1)
			{
			document.all("r"+row).style.backgroundColor = "#d7d7d7";
			document.all("r"+row).style.cursor = "#d7d7d7";
			}
		else
			{
			document.all("r"+row).style.backgroundColor = "";
			}
		}



function mettiST(){
	document.mainform.ST.value = 2;
}



<% if (vlistaCond.size() > 0){
DiagnosticheDocfaNoRes sFornit=(DiagnosticheDocfaNoRes)vlistaCond.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

function esportaExcel(){
 	document.mainform.OGGETTO_SEL.value='';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	if (document.formCate.categoria.value != '' )
		document.mainform.CATEGORIA_SEL.value=document.formCate.categoria.value;
	else
		document.mainform.CATEGORIA_SEL.value='';
	document.mainform.submit();
	}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/DiagnosticheDocfaNoRes" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Lista Diagnostiche Docfa Non Residenziali</span>
</div>
	
		
<% String smsErrore = (String)sessione.getAttribute(DiagnosticheDocfaNoResLogic.SMS_DIAGNOSTICHE_NORES);
if (smsErrore!= null){
	sessione.removeAttribute(DiagnosticheDocfaNoResLogic.SMS_DIAGNOSTICHE_NORES);
%>
<br>
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle" style="color: red;">
	<%=smsErrore%></span>
</div>
<%
}
%>		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Fornitura</span></td>
		<td class="extWinTDTitle" title="Numero record nei dati censuari"><span class="extWinTXTTitle">Numero record<BR>nei dati censuari</span></td>
		<td class="extWinTDTitle" title="Numero dei record con data di registrazione non coerente con la fornitura"><span class="extWinTXTTitle">Numero record<BR>data non coer.</span></td>
		<td class="extWinTDTitle" title="Numero di record con tipo di operazione non unico"><span class="extWinTXTTitle">Numero record<BR>tipo op. non unico</span></td>
		<td class="extWinTDTitle" title="Numero di record con tipo operazione a NULL"><span class="extWinTXTTitle">Numero record<BR>tipo op. nullo</span></td>
		<td class="extWinTDTitle" title="Numero dei record con tipo operazione 'cessata'"><span class="extWinTXTTitle">Numero record<BR>tipo op. C</span></td>
		<td class="extWinTDTitle" title="Numero dei record senza anomalie"><span class="extWinTXTTitle">Numero record<BR>corretti</span></td>
		<td class="extWinTDTitle" title="Numero delle unità immobiliari coinvolte"><span class="extWinTXTTitle">UIU coinvolte</span></td>
	</tr>
	
	<% DiagnosticheDocfaNoRes dd = new DiagnosticheDocfaNoRes(); %>
  <% java.util.Enumeration en = vlistaCond.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
	  dd = (DiagnosticheDocfaNoRes)en.nextElement();%>
	<tr>
		<td class="extWinTDData"><span class="extWinTXTData"><%=dd.getFornitura().substring(3)%></span></td>
		<td class="extWinTDData"><span class="extWinTXTData"><%=dd.getNrecDatiCensuari()%></span></td>
		<td class="extWinTDData"><span class="extWinTXTData"><%=dd.getNrecDataRegNoCoer()%></span></td>
		<td class="extWinTDData"><span class="extWinTXTData"><%=dd.getNrecTipoOperNoUnico()%></span></td>
		<td class="extWinTDData"><span class="extWinTXTData"><%=dd.getNrecTipoOperNull()%></span></td>
		<td class="extWinTDData"><span class="extWinTXTData"><%=dd.getNrecTipoOperCess()%></span></td>
		<td class="extWinTDData"><span class="extWinTXTData"><%=dd.getNrecDocfaOK()%></span></td>
		<td class="extWinTDData"><span class="extWinTXTData"><%=dd.getNrecUIU()%></span></td>
	</tr>
<% contatore++;progressivoRecord ++;} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="103">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
<input type='hidden' name="CATEGORIA_SEL" value="">
</table>
</form>
<br>
<form name="formCate">
<table align=left cellpadding="0" cellspacing="0"
	class="editExtTable" style="background-color: #C0C0C0;">
<tr>
	<td colspan=1>
	<table class="viewExtTable" style="width: 100%;">
	<tr>
		<td>
			<span class="TXTmainLabel">
				Seleziona una Categoria:
			</span>
	  	</td>
		<td calss="TDeditDBCOmboBox"  valign="top" nowrap>
			<select name="categoria" class="INPDBComboBox" size="1" >
			<option value="" selected="selected">Tutte</option>
			<% java.util.Enumeration enc = vlistaCat.elements();%>
			<% while (enc.hasMoreElements()) {
				String cc = (String)enc.nextElement();%>
				<option value="<%=cc %>" ><%=cc%></option>
			<% }%>
			</select>
	  	</td>
	</tr>  	
	<tr>
		<td colspan=2>
			&nbsp;
	  	</td>
	</tr>  	
	<tr>
		<td colspan="2" class="TDmainLabel"
			style="width: 5%; white-space: nowrap;">
			<a href="javascript:esportaExcel();">
			<span class="TXTmainLabel"> 
				Apri Statistiche sui Classamenti
			</span>
			</a>
		</td>
	</tr>
	</table>
	</td>
</tr>
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>