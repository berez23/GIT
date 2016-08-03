<%@ page language="java" import="it.escsolution.escwebgis.anagrafe.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Anagrafe ana = (Anagrafe) sessione.getAttribute(it.escsolution.escwebgis.anagrafe.logic.AnagrafeAnagrafeLogic.ANAGRAFE); %>
<%   java.util.ArrayList listaComponentiFamiglia = (java.util.ArrayList) sessione.getAttribute(it.escsolution.escwebgis.anagrafe.logic.AnagrafeAnagrafeLogic.LISTA_COMPONENTI_FAMIGLIA); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  it.escsolution.escwebgis.anagrafe.bean.AnagrafeFinder finder = null;

	if (sessione.getAttribute(it.escsolution.escwebgis.anagrafe.servlet.AnagrafeAnagrafeServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(it.escsolution.escwebgis.anagrafe.servlet.AnagrafeAnagrafeServlet.NOMEFINDER)).getClass() == new AnagrafeFinder().getClass()){
			finder = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeFinder)sessione.getAttribute(it.escsolution.escwebgis.anagrafe.servlet.AnagrafeAnagrafeServlet.NOMEFINDER);
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
<title>Anagrafe anagrafe - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function visualizzaFamiglia(){
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeFamiglia";		
	document.mainform.ST.value=3;	
	document.mainform.EXT.value=1;
	document.mainform.UC.value=9;
	document.mainform.submit();
}

function apriDettaglioAltroComponente(oggettoSel)
{
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeAnagrafe";
	document.mainform.OGGETTO_SEL.value = oggettoSel;
	document.mainform.ST.value=3;	
	document.mainform.EXT.value=1;
	document.mainform.submit();
}

function mettiST(){
	document.mainform.ST.value = 3;
	
	//var body = document.getElementsByTagName("body")[0].innerHTML;	
	//top.parent.frames["nascosto"].document.getElementById("historyid").value = body;
	//top.parent.frames["nascosto"].document.getElementById("mainformid").submit();
	

}

</script>
<body onload="mettiST()">
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeAnagrafe" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Dati Anagrafe</span>
</div>

<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>

&nbsp;
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width=210;"><span class="TXTmainLabel">Codice Anagrafe</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ana.getCodAnagrafe()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getCognome()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
				
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getNome()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Nato a </span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getComuniNascita()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">In data</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getDataNascita()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sesso</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getSesso()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Tipo Soggetto</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getTipoSoggetto()%></span></td>
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
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ana.getCodFiscale()%></span></td>
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
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Cittadinanza</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ana.getCittadinanze()%></span></td>
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
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Residenza</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%= (ana.getResidenza() == null) ? "&nbsp;-&nbsp;" : ana.getResidenza() %></span></td>
			</tr>
		</table>
		</td>
	</tr>	
		
</table>

<br><br><br>

<% if (listaComponentiFamiglia != null) {%>
<span class="TXTmainLabel">Lista componenti della famiglia</span>
<table class="extWinTable" style="width: 60%;">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
	</tr>			  
<% 
	java.util.Iterator it = listaComponentiFamiglia.iterator();
	  
	while (it.hasNext()) {
		Anagrafe anaComp = (Anagrafe) it.next(); %>

    <tr style="cursor: pointer;" onClick="apriDettaglioAltroComponente('<%= anaComp.getCodAnagrafe() %>')">
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= anaComp.getCognome() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= anaComp.getNome() %></span></td>
	</tr>			
	<%}%>
</table>
<%}%>

</td>
<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>


<br><br>

<% if (ana != null){%>
<% String codice = "";
   codice = ana.getCodAnagrafe();%>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
<%}%>
		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="8">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait" />
</body>
</html>