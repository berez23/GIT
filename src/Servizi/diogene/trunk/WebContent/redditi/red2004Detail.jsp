<%@ page language="java" import="it.escsolution.escwebgis.redditi2004.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.redditi2004.bean.Redditi red=(it.escsolution.escwebgis.redditi2004.bean.Redditi)sessione.getAttribute(it.escsolution.escwebgis.redditi2004.logic.Redditi2004Logic.REDDITI); %>
<%  	java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(it.escsolution.escwebgis.redditi2004.logic.Redditi2004Logic.ALTRIREDDITI);  %>

<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  it.escsolution.escwebgis.redditi2004.bean.RedditiFinder finder = null;
	if (sessione.getAttribute(Redditi2004Logic.FINDER) !=null){
		if (((Object)sessione.getAttribute(Redditi2004Logic.FINDER)).getClass() == new it.escsolution.escwebgis.redditi2004.bean.RedditiFinder().getClass()){
			finder = (it.escsolution.escwebgis.redditi2004.bean.RedditiFinder)sessione.getAttribute(Redditi2004Logic.FINDER);
			}
	}
%>

<% 	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<%@page import="it.escsolution.escwebgis.redditi2004.logic.Redditi2004Logic"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.collections.iterators.EntrySetMapIterator"%>
<html>
<head>
<title>Redditi 2004 - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}
function apriDettaglioAltroDdichiarante(oggettoSel)
{
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Redditi2004";
	document.mainform.OGGETTO_SEL.value = oggettoSel;
	document.mainform.ST.value=3;	
	document.mainform.EXT.value=1;
	document.mainform.submit();
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Redditi2004" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Redditi 2004</span>
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


<span class="TXTmainLabel">Dichiarante</span>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Cognome</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=red.getDicCogn() %></span></td>
			</tr>
		</table>
		</td>
	<td width=10></td>	
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Nome</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=red.getDicNome()%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=(red.getDicCf()==null?"-":red.getDicCf())%></span></td>
			</tr>
		</table>
		</td>
		<td>
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Data di Nascita</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=(red.getDicNaData()==null?"-":red.getDicNaData())%></span></td>
			</tr>
		</table>		
		</td>	
		<td>
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Luogo di Nascita</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=(red.getDicNaLuogo()==null?"-":red.getDicNaLuogo())%></span></td>
			</tr>
		</table>		
		</td>
	</tr>
	</table>

<br>


<span class="TXTmainLabel">Altri dichiaranti collegati (ID TELE = <%=red.getIdTeleDic() %>)</span>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
<%
Redditi altriRed = new Redditi();
java.util.Enumeration en = vlista.elements(); int contatore = 1; %>
<% while (en.hasMoreElements()) 
   {
	altriRed = (it.escsolution.escwebgis.redditi2004.bean.Redditi)en.nextElement();%>
    <tr id="r<%=contatore%>" onclick="apriDettaglioAltroDdichiarante('<%=altriRed.getChiave()%>')">
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=altriRed.getDicCogn()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=(altriRed.getDicNome()==null?"-":altriRed.getDicNome())%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=(altriRed.getDicCf()==null?"-":altriRed.getDicCf())%></span></td>
	</tr>
<% contatore++;%>
<% }%>
</table>

<br>
<br>

	<%
	 EntrySetMapIterator mapI = new EntrySetMapIterator(red.getQuadro());
	
	 while (mapI.hasNext()) {
		  String key = (String) mapI.next();
	      DecoQuadro value = (DecoQuadro) mapI.getValue();
	 %>
<span class="TXTmainLabel">Quadro <%=key %></span>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Codice Riga</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=(value.getCodRiga()==null?"-":value.getCodRiga())%></span></td>
			</tr>
		</table>
		</td>
	<td width=10></td>	
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Tipo Modello</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=(value.getTipoModello()==null?"-":value.getTipoModello())%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	<tr >		
		<td colspan =3 >	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Note</span></td>
				<td class="TDviewTextBox" style="width:400;"><span class="TXTviewTextBox"><%=(value.getNote()==null?"-":value.getNote())%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr >		
		<td colspan =3 >	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Quadro</span></td>
				<td class="TDviewTextBox" style="width:400;"><span class="TXTviewTextBox"><%=(value.getQuadro()==null?"-":value.getQuadro())%></span></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>		
		<td colspan=2>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Descrizione Quadro</span></td>
				<td class="TDviewTextBox" style="width:200;"><span class="TXTviewTextBox"><%=(value.getDescrizione()==null?"-":value.getDescrizione())%></span></td>
			</tr>			
		</table>
		</td>
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Valore</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=(value.getValore()==null?"-":value.getValore())%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	</table>
		    
	 <% 
	 }
	 %> 
	
&nbsp;

</td>
<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>

<!-- FINE solo dettaglio -->


<% if (red != null){
   String codice = red.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice %>">
<%}%>

<br><br><br>
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%} else { %>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="48">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait">
</div>
</body>
</html>