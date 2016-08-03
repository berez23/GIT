<%@ page language="java" import="it.escsolution.escwebgis.istat.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.soggetto.bean.Soggetto sog=(it.escsolution.escwebgis.soggetto.bean.Soggetto)sessione.getAttribute("SOGGETTO"); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  it.escsolution.escwebgis.soggetto.bean.SoggettoFinder finder = null;

	if (sessione.getAttribute("FINDER14") !=null){
		if (((Object)sessione.getAttribute("FINDER14")).getClass() == new it.escsolution.escwebgis.soggetto.bean.SoggettoFinder().getClass()){
			finder = (it.escsolution.escwebgis.soggetto.bean.SoggettoFinder)sessione.getAttribute("FINDER14");
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
<title>Soggetto Soggetto - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/SoggettoSoggetto" target="_parent">

<%if(sog.getTipoPersona().equals("F")){%>
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Dettaglio Persona Fisica</span>
</div>
<%} else { %>
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Dettaglio Persona Giuridica</span>
</div>
<% } %>
&nbsp;

<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<%if(sog.getTipoPersona().equals("F")){%>
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
		<td colspan = 2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox" style="width:180;"><span class="TXTviewTextBox"><%=sog.getNome()%></span></td>
			
				<td class="TDmainLabel"  style="width:50;"><span class="TXTmainLabel">Sesso</span></td>
				<td class="TDviewTextBox" style="width:13;"><span class="TXTviewTextBox"><%=sog.getSesso()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
		
	<tr></tr>
	<tr></tr>
	<tr></tr>
		
	<tr>		
		
		<td colspan=2>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:140;"><span class="TXTviewTextBox"><%=sog.getComune()%></span></td>
						
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:130;"><span class="TXTviewTextBox"><%=sog.getCodiceFiscale()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
		
	<tr></tr>
	<tr></tr>
	<tr></tr>	
			
	<tr>	
		<td colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Luogo di Nascita </span></td>
				<td class="TDviewTextBox" style="width:140;"><span class="TXTviewTextBox"><%=sog.getLuogoNascita()%></span></td>
			
			
			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Data di Nascita</span></td>
				<td class="TDviewTextBox" style="width:80;"><span class="TXTviewTextBox"><%=sog.getDataNascita()%></span></td>
			
			</tr>
		</table>
		</td>
				
	</tr>
	
</table>
		
<br></br>
	
<%} else { %>

&nbsp;
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Denominazione</span></td>
				<td class="TDviewTextBox" style="width:350;"><span class="TXTviewTextBox"><%=sog.getDenominazione()%></span></td>
			</tr>
		</table>
		</td>	
	</tr>
	
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:150;"><span class="TXTviewTextBox"><%=sog.getComune()%></span></td>
			</tr>
			<tr></tr>
			<tr>
				<td class="TDmainLabel"  style="width:150;"><span class="TXTmainLabel">Partita IVA</span></td>
				<td class="TDviewTextBox" style="width:150;"><span class="TXTviewTextBox"><%=sog.getPartitaIva()%></span></td>
			</tr>
		
		</table>
		</td>
	</tr>
</table>	
<%}%>

</td>
<% if (vctLink != null){ %>
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
		<input type='hidden' name="UC" value="14">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>