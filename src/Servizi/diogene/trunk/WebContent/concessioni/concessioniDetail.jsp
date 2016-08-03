<%@ page language="java" import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.concessioni.bean.*,it.escsolution.escwebgis.concessioni.servlet.*,it.escsolution.escwebgis.concessioni.logic.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Concessioni lc = (Concessioni) sessione.getAttribute(ConcessioniLogic.CONCESSIONI); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  ConcessioniFinder finder = null;

	if (sessione.getAttribute(ConcessioniServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(ConcessioniServlet.NOMEFINDER)).getClass() == new ConcessioniFinder().getClass()){
			finder = (ConcessioniFinder)sessione.getAttribute(ConcessioniServlet.NOMEFINDER);
			}
					
	}
	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}

if(lc.getNumero()== null)
	lc.setNumero("-");
if(lc.getTipo()== null)
	lc.setTipo("-");
if(lc.getRichiedente()== null)
	lc.setRichiedente("-");
if(lc.getCodfis()== null)
	lc.setCodfis("-");
if(lc.getNProt()== null)
	lc.setNProt("-");
if(lc.getDataProt()== null)
	lc.setDataProt("-");
if(lc.getRichiedente2()== null)
	lc.setRichiedente2("-");
if(lc.getOggetto()== null)
	lc.setOggetto("-");
if(lc.getIndirizzoConc()== null)
	lc.setIndirizzoConc("-");
if(lc.getCivico()== null)
	lc.setCivico("-");
if(lc.getFoglio()== null)
	lc.setFoglio("-");
if(lc.getParticelle()== null)
	lc.setParticelle("-");
if(lc.getDataRilascio()== null)
	lc.setDataRilascio("-");
if(lc.getSubalterno()== null)
	lc.setSubalterno("-");
if(lc.getZona()== null)
	lc.setZona("-");
if(lc.getProcedimento()== null)
	lc.setProcedimento("-");
if(lc.getId() == null)
	lc.setId(new Integer(0));
%>

	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>

	
<html>
<head>
<title>Concessioni Edilizie - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Concessioni" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Concessioni Edilizie</span>
</div>
&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;width: 80%;">
	<tr>
		<td colspan = 1>
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Numero</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getNumero() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Tipo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getTipo() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Richiedente</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getRichiedente() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getCodfis() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Numero Protocollo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getNProt() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Protocollo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getDataProt() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Secondo Richiedente</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getRichiedente2() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Oggetto</span></td>
				<td colspan="3" class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getOggetto() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Ind. Concessione</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getIndirizzoConc() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Civico Concessione</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getCivico() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Foglio</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getFoglio() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Particelle</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getParticelle() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Subalterno</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getSubalterno() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Zona</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getZona() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Procedimento</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getProcedimento() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Rilascio Concessione</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getDataRilascio() %></span></td>
			</tr>
			
		</table>
		</td>
	</tr>
</table>
<br><br><br>

</td>
<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>
<br><br><br>

<center><span class="extWinTXTTitle"><a href="javascript:"></a></span></center>

<br><br>

<!-- FINE solo dettaglio -->
<% if (lc != null){%>
<% String codice = "";
   codice = lc.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="31">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>