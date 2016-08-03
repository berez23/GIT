<%@ page language="java" import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.concessioni.bean.*,it.escsolution.escwebgis.concessioni.servlet.ConcessioniINFORMServlet,it.escsolution.escwebgis.concessioni.logic.ConcessioniINFORMLogic "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   ConcessioniINFORM lc = (ConcessioniINFORM) sessione.getAttribute(ConcessioniINFORMLogic.CONCESSIONI_INFORM); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  ConcessioniINFORMFinder finder = null;

	if (sessione.getAttribute(ConcessioniINFORMServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(ConcessioniINFORMServlet.NOMEFINDER)).getClass() == new ConcessioniINFORMFinder().getClass()){
			finder = (ConcessioniINFORMFinder)sessione.getAttribute(ConcessioniINFORMServlet.NOMEFINDER);
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
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
	
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
	
<%@page import="java.util.HashMap"%>
<html>
<head>
<title>Concessioni Edilizie - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}
function apriPopupCatasto(f,p,s,d,cod_ente)
{
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ConcessioniINFORM?popupCatasto=true&f='+f+'&p='+p+'&s='+s+'&d='+d+'&cod_ente='+cod_ente;
			var finestra=window.open(url,"","height=400,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}
</script>
<body >

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ConcessioniINFORM" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Concessioni</span>
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

<span class="TXTmainLabel">Protocollo Generale</span>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;width: 80%;">
	<tr>
		<td colspan = 1>		
		<table class="viewExtTable" style="width: 100%;">
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Fascicolo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getFascicolo() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Data Protocollo</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getDataProtocollo() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Numero</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getNumeroProtocollo() %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Anno</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getAnnoProtocollo() %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Tipo documento</span></td>
				<td colspan="3" class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getTipoDocumento() %></span></td>
			</tr>
			
		</table>
		</td>
	</tr>
</table>
<br><br>
<span class="TXTmainLabel">Contenuti dichiarativi Intervento</span>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;width: 80%;">
	<tr>
		<td colspan = 1>		
		<table class="viewExtTable" style="width: 60%;">
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;" valign="top"><span class="TXTmainLabel">Tipo Intervento</span></td>
				<td colspan="3" class="TDviewTextBox"><span class="TXTviewTextBox">
					<%
					java.util.Iterator<String> its = lc.getTipoIntervento().iterator(); 
					while (its.hasNext())
					{
						out.println(its.next()+"<br>");
					}
					%>
				</span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Descrizione</span></td>
				<td colspan="3" class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getDescrizioneIntervento() %></span></td>
			</tr>
			
		</table>
		</td>
	</tr>
</table>
<br><br>
</td>

<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>

<div class="tabber">

	<% if (lc.getRichiedenti() != null && lc.getRichiedenti().size()>0) {%>

		<div class="tabbertab">
		
		<h2>Informazioni Anagrafiche: Richiedenti</h2>

		<table align=left class="extWinTable" style="width: 100%;">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Informazioni Anagrafiche: Richiedenti</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice fiscale</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome/Ragsoc</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data residenza</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Citta</span></td>
			</tr>
			<%
			java.util.Iterator<ConcessioniINFORMAnagrafe> itana = lc.getRichiedenti().iterator();
			while (itana.hasNext())
			{
				ConcessioniINFORMAnagrafe a = itana.next();	
			%>
				<tr>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getCodiceFiscale()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getCognomeRagSoc()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getNome()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getDataResidenza()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getIndirizzo()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getCivico()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getCitta()%></span></td>
				</tr>
			<%
			}
			%>
		</table>
		
		</div>
<%} %>


<% if (lc.getProprietari() != null && lc.getProprietari().size()>0) {%>

		<div class="tabbertab">
		
		<h2>Informazioni Anagrafiche: Proprietari e/o Progettisti</h2>

		<table align=left class="extWinTable" style="width: 100%;">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Informazioni Anagrafiche: Proprietari e/o Progettisti</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice fiscale</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome/Ragsoc</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data residenza</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Citta</span></td>
			</tr>
			<%
			java.util.Iterator<ConcessioniINFORMAnagrafe> itana = lc.getProprietari().iterator();
			while (itana.hasNext())
			{
				ConcessioniINFORMAnagrafe a = itana.next();	
			%>
				<tr>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getCodiceFiscale()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getCognomeRagSoc()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getNome()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getDataResidenza()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getIndirizzo()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getCivico()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=a.getCitta()%></span></td>
				</tr>
			<%
			}
			%>
		</table>
		
		</div>
		<%} %>


<% if (lc.getOggetti() != null && lc.getOggetti().size()>0) {%>

		<div class="tabbertab">
		
		<h2>Informazioni Oggetto</h2>

		<table align=left class="extWinTable" style="width: 100%;">
			<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Informazioni Oggetto</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Verifica Catasto</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Destinazione d'uso</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Vincolo soprin.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Scala</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Piano</span></td>
			</tr>
			<%
			java.util.Iterator<ConcessioniINFORMOggetti> itogg = lc.getOggetti().iterator();
			while (itogg.hasNext())
			{
				ConcessioniINFORMOggetti oggetto = itogg.next();					
			%>
				<tr>
					<td class="extWinTDData"><span class="extWinTXTData"><%=oggetto.getFoglio()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=oggetto.getParticella()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=oggetto.getSubalterno()%></span></td>
					<td class="extWinTDData" align="center"><span class="extWinTXTData">
					<% if (oggetto.getDataFineACatasto() == null || oggetto.getDataFineACatasto().trim().equals("") || oggetto.getDataFineACatasto().trim().equals("-")){%>
						<img  src="<%=request.getContextPath()%>/images/alert.gif" border="0" height="18" width="18" alt="Non presente a catasto"  >		
					<% }else{%>
						<a href="javascript:apriPopupCatasto('<%=oggetto.getFoglio()%>','<%=oggetto.getParticella()%>','<%=oggetto.getSubalterno()%>','<%=oggetto.getDataFineACatasto()%>','<%=oggetto.getCodEnte()%>');">
						<img  src="<%=request.getContextPath()%>/images/ok.gif" border="0"  alt="particella presente"  >
						</a>		
					<% }%>	
					</span></td>										
					<td class="extWinTDData"><span class="extWinTXTData"><%=oggetto.getDestinazioneUso()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=oggetto.getVincoloSoprintendenza()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=oggetto.getIndirizzo()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=oggetto.getCivico()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=oggetto.getScala()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=oggetto.getPiano()%></span></td>
				</tr>
			<%
			}
			%>
		</table>
		
	</div>
	<%} %>


<% if (lc.getTiff() != null && lc.getTiff().size()>0) {%>

		<div class="tabbertab">
		
		<h2>Documenti</h2>
		<table align=left class="extWinTable" style="width: 40%;">
		<tr>&nbsp;</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Documenti</span></td>
			</tr>
			<%
			HashMap<String,String> m = lc.getTiff();
			for (String key : m.keySet())			
			{
			%>
				<tr>
					<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=m.get(key)%>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a target="_new" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ConcessioniINFORM?ST=99&img=<%=key%>">PDF</a>
							&nbsp;
							<a target="_new" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ConcessioniINFORM?ST=99&openJpg=true&img=<%=key%>">IMG</a>
						</span>
					</td>
				</tr>
			<%
			}
			%>
		</table>
		</div>
<%} %>

</div>


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
		<input type='hidden' name="UC" value="46">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>