<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.soggetto.bean.SoggettoFascicolo sog=(it.escsolution.escwebgis.soggetto.bean.SoggettoFascicolo)sessione.getAttribute("SOGGETTOFASCICOLO"); 
	 java.util.List elencoCross = (java.util.List)sessione.getAttribute("ELENCO_CROSS");
%>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();
%>
<%  it.escsolution.escwebgis.soggetto.bean.SoggettoFascicoloFinder finder = null;

	if (sessione.getAttribute("FINDER25") !=null){
		if (((Object)sessione.getAttribute("FINDER25")).getClass() == new it.escsolution.escwebgis.soggetto.bean.SoggettoFascicoloFinder().getClass()){
			finder = (it.escsolution.escwebgis.soggetto.bean.SoggettoFascicoloFinder)sessione.getAttribute("FINDER25");
		}
	}
	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>

	

	
<html>
<head>
<title>Soggetto Fascicolo - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script src='<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ewg/Scripts/dynamic.js' language='JavaScript'></script>
<script>
function mettiST(){
	document.mainform.ST.value = 3;
}
function vai(url){
	//document.mainform.action=url;
	//document.mainform.OGGETTO_SEL.value="";
	//document.mainform.ST.value = 2;
	//document.mainform.submit();
	wait();
	parent.location.replace(url);
} 
</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/SoggettoFascicolo" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Dettaglio Soggetto Fascicolo</span>
</div>
&nbsp;

<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox" style="width:270;"><span class="TXTviewTextBox"><%=sog.getCognome()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox" style="width:270;"><span class="TXTviewTextBox"><%=sog.getNome()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
		
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Denominazione</span></td>
				<td class="TDviewTextBox" style="width:270;"><span class="TXTviewTextBox"><%=sog.getDenominazione()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Luogo Nascita</span></td>
				<td class="TDviewTextBox" style="width:270;"><span class="TXTviewTextBox"><%=sog.getComuneNascita()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:270;"><span class="TXTviewTextBox"><%=sog.getCodiceFiscale()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Partita Iva</span></td>
				<td class="TDviewTextBox" style="width:270;"><span class="TXTviewTextBox"><%=sog.getPartitaIva()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
</table>
		
<br></br>
<%if (elencoCross.size()>0){%>	
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:395;"><span class="TXTmainLabel">Elenco DB Collegati</span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<%for (int k=0;k<elencoCross.size();k++){
		it.escsolution.escwebgis.soggetto.bean.DataBase db =(it.escsolution.escwebgis.soggetto.bean.DataBase)elencoCross.get(k);
	%>
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" >
			<tr onclick="vai('<%=db.getUrlDB()%>')" >
				<td class="TDviewTextBox" style="width:395;"  >
				<span class="TXTviewTextBox" style='cursor: pointer;' ><%=db.getDescrizione()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr></tr>
	<tr></tr>
	<% } %>	
</table>	
<% } %>
<br></br>
</td>
<% if (vctLink != null && vctLink.size()>0){ %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>

<!-- FINE solo dettaglio -->
<% if (sog != null){%>
<% String codice = "";
   codice = sog.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="25">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>