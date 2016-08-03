<%@ page language="java" import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.concessioni.bean.*,it.escsolution.escwebgis.concessioni.servlet.*,it.escsolution.escwebgis.concessioni.logic.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   FornituraDia dia = (FornituraDia) sessione.getAttribute(FornituraDiaLogic.FORNITURADIA); 
	List<String[]> uiu = dia.getUiu();
	List<String[]> ben = dia.getBeneficiari();
	List<String[]> pro = dia.getProfessionisti();
	List<String[]> imp = dia.getImp();
%>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  FornituraDiaFinder finder = null;

	if (sessione.getAttribute(FornituraDiaServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(FornituraDiaServlet.NOMEFINDER)).getClass() == new FornituraDiaFinder().getClass()){
			finder = (FornituraDiaFinder)sessione.getAttribute(FornituraDiaServlet.NOMEFINDER);
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

	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>

	
<%@page import="java.util.List"%>
<html>
<head>
<title>Fornitura Dia - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

function apriPopupCatastoFabbricati(f,p,s,d,cod_ente)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupCatasto=true&f='+f+'&p='+p+'&s='+s+'&d='+d+'&cod_ente='+cod_ente;
			var finestra=window.open(url,"","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}

function apriPopupCatastoTerreni(f,p,cod_ente)
{
	
			var url = '<%= request.getContextPath() %>/CatastoTerreni?UC=&2popupCatasto=true&f='+f+'&p='+p+'&cod_ente='+cod_ente+'&UC=2&ST=2';
			var finestra=window.open(url,"","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
</script>
<body onload="mettiST()">


<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/FornituraDia" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Fornitura Dia</span>
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
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">CF Richiedente</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= dia.getRicCodiceFiscale() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Richiedente</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= dia.getRicCognome() + " " + dia.getRicNome()%></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Qualifica</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= dia.getQualifica() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Intervento</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= dia.getIntervento() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= dia.getIndirizzo() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Tipo Unità</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= dia.getTipoUnita() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Numero Protocollo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= dia.getProtocollo() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Fornitura</span></td>
				<td class="TDviewTextBox" colspan = 3><span class="TXTviewTextBox"><%= dia.getFkFornitura()%></span></td>
			</tr>
			
		</table>
		</td>
	</tr>
</table>

<span class="TXTmainLabel">Lista Oggetti</span>

   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Unità</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CAtasto Immobili / Terreni </span></td>
	</tr>
		<% 
			Iterator it = uiu.iterator();
			while (it.hasNext()) {
				String[] s = (String[]) it.next();
		%>				
   <tr>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[1] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[2] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[3] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[4] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[5] %></td>
		<td class="extWinTDData" >
		<span class="extWinTXTData">
				<% if (s[6].equals("N")){%>
					<img  src="<%=request.getContextPath()%>/images/alert.gif" border="0" height="18" width="18" alt="<%=s[6]%>"  >		
				<% }else{ %>
					<a href="javascript:apriPopupCatastoFabbricati('<%=s[3]%>','<%=s[4]%>','<%=s[5]%>','<%=s[7]%>','<%=dia.getCodEnte()%>');">
					<img  src="<%=request.getContextPath()%>/images/ok.gif" border="0"  alt="immobile presente"  >
					</a>		
				<% } %>
				<% if (s[8].equals("N")){%>
					<img  src="<%=request.getContextPath()%>/images/alert.gif" border="0" height="18" width="18" alt="<%=s[6]%>"  >		
				<% }else{ %>
					<a href="javascript:apriPopupCatastoTerreni('<%=s[3]%>','<%=s[4]%>','<%=dia.getCodEnte()%>');">
					<img  src="<%=request.getContextPath()%>/images/ok.gif" border="0"  alt="particella presente"  >
					</a>		
				<% } %>
				</span></td>


	</tr>
		<% 
			}
		%>

	</table>



<span class="TXTmainLabel">Lista Beneficiari</span>

   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sesso</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sede</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Nascita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Luogo Nascita</span></td>
	</tr>
		<% 
			Iterator it2 = ben.iterator();
			while (it2.hasNext()) {
				String[] s = (String[]) it2.next();
		%>				
   <tr>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[0] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[1] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[2] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[5] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[6] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[3] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[4] %></td>
	</tr>
		<% 
			}
		%>

	</table>

<span class="TXTmainLabel">Professionisti</span>

   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CF</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Albo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Provincia</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Qualifica</span></td>
	</tr>
		<% 
			Iterator it3 = pro.iterator();
			while (it3.hasNext()) {
				String[] s = (String[]) it3.next();
		%>				
   <tr>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[0] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[1] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[2] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[3] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[4] %></td>
	</tr>
		<% 
			}
		%>

	</table>
	
<span class="TXTmainLabel">Imprese</span>

   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PI</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sede</span></td>
	</tr>
		<% 
			Iterator it4 = imp.iterator();
			while (it4.hasNext()) {
				String[] s = (String[]) it4.next();
		%>				
   <tr>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[0] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[1] %></td>
		<td class="extWinTDData">
		<span class="TXTviewTextBox"><%= s[2] %></td>
	</tr>
		<% 
			}
		%>

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
<% if (dia != null){%>
<% String codice = "";
   codice = dia.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="51">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>