<%@ page language="java" import="it.escsolution.escwebgis.anagrafe.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Famiglia fam=(Famiglia)sessione.getAttribute("FAMIGLIA"); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%  it.escsolution.escwebgis.anagrafe.bean.FamigliaFinder finder = null;%>
<%  it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");
	if (sessione.getAttribute("FINDER9") !=null){
			if (((Object)sessione.getAttribute("FINDER9")).getClass() == new FamigliaFinder().getClass()){
				finder = (it.escsolution.escwebgis.anagrafe.bean.FamigliaFinder)sessione.getAttribute("FINDER9");
				}
	}%>
	<%java.util.Vector vlistaAna=(java.util.Vector)sessione.getAttribute("LISTA_ANAGRAFE2");%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>	

<html>
<head>
<title>Anagrafe Famiglia - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function visualizzaParticelle(){
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeAnagrafe";		
	document.mainform.KEYSTR.value = document.mainform.KEYSTR2.value;
	document.mainform.ST.value=2;
	document.mainform.EXT.value=1;
	document.mainform.UC.value=8;
	document.mainform.submit();
}

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeFamiglia" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Dati Famiglia</span>
</div>
&nbsp;
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Codice Famiglia</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=fam.getCodFamiglia()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Denominazione</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=fam.getDenominazione()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
				
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Tipo Famiglia</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=fam.getTipoFamiglia()%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	
</table>
	
&nbsp;

	
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">

	<tr>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:60;"><span class="TXTmainLabel">Scala</span></td>
				<td class="TDviewTextBox" style="width:65;"><span class="TXTviewTextBox"><%=fam.getScala()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:65;"><span class="TXTmainLabel">Interno</span></td>
				<td class="TDviewTextBox" style="width:65;"><span class="TXTviewTextBox"><%=fam.getInterno()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:60;"><span class="TXTmainLabel">Piano</span></td>
				<td class="TDviewTextBox" style="width:65;"><span class="TXTviewTextBox"><%=fam.getPiano()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>

</table>

&nbsp;

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Codice Fiscale capo famiglia</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=fam.getCodFiscale()%></span></td>
			</tr>
		</table>
		</td>
	</tr>	
		
</table>

</td>
<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>


<!-- FINE solo dettaglio -->


<% if (vlistaAna.size() > 0){%>
<%it.escsolution.escwebgis.anagrafe.bean.Anagrafe ana = new it.escsolution.escwebgis.anagrafe.bean.Anagrafe(); %>
  	<%java.util.Enumeration en = vlistaAna.elements(); String codice = "";%>
  	<%while (en.hasMoreElements()) {
    ana = (it.escsolution.escwebgis.anagrafe.bean.Anagrafe)en.nextElement();
    
    codice = codice + ana.getCodAnagrafe() + ",";
    }
    
    codice=codice.substring(0,codice.length()-1);%>
    <input type='hidden' name="KEYSTR2" value="<%=codice%>">
    <%}%>
    <input type='hidden' name="KEYSTR" value="">

<br><br><br>
<% if (st == 3){%>
<center><span class="extWinTXTTitle"><a href="javascript:visualizzaParticelle()">Componenti</a></span></center>

<%}%>
<br><br>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="9">
		<input type='hidden' name="OGGETTO_SEL" value="">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		
 
</form>


<div id="wait" class="cursorWait" />
</body>
</html>