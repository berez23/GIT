<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"
								import="java.util.ArrayList,java.util.Iterator,
								it.escsolution.escwebgis.concessioni.bean.*,
								it.escsolution.escwebgis.concessioni.servlet.*,
								it.escsolution.escwebgis.concessioni.logic.*, java.text.SimpleDateFormat,  
								it.webred.ct.data.access.basic.concedilizie.ConcVisuraDTO"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   ConcVisuraDTO lc = (ConcVisuraDTO) sessione.getAttribute(ConcessioniVisureLogic.CONCESSIONE_VISURA); %>
<%  // ConcessioneVisuraDoc cvd = (ConcessioneVisuraDoc) sessione.getAttribute("DOCUMENTI_VISURA"); %>
<%  SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy"); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  ConcessioniFinder finder = null;
	
	if (sessione.getAttribute(ConcessioniServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(ConcessioniServlet.NOMEFINDER)).getClass() == new ConcessioniFinder().getClass()){
			finder = (ConcessioniFinder)sessione.getAttribute(ConcessioniVisureServlet.NOMEFINDER);
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
<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
	
<html>
<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8"/>

<title>Concessioni Edilizie Visure - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.target = "_self";
	document.mainform.ST.value = 3;
}
</script>
<body onload="mettiST()">

<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
	&nbsp;

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ConcessioniVisure" target="_parent">
<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Concessioni Edilizie Visure</span>
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
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Nome Intestatario</span></td>
				<td class="TDviewTextBox" colspan="3"><span class="TXTviewTextBox"><%= lc.getNomeIntestatario()!=null ? lc.getNomeIntestatario() : "-" %></span></td>
				
			</tr>
			<tr><td class="TDmainLabel" colspan="4" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel"></span></td></tr>
			<tr>
				<td class="TDmainLabel" style="width: 25%; white-space: nowrap;"><span class="TXTmainLabel">Tipo Atto</span></td>
				<td class="TDviewTextBox" style="width: 25%;"><span class="TXTviewTextBox">
				<%
//					if (lc.getDescTipoAtto()!= null)
//						out.println(lc.getTipoAtto() + "-" + lc.getDescTipoAtto());
				//System.out.println(lc.getDescTipoAtto());
				//out = new java.io.PrintStream(System.out, true, "UTF-8");
				out.println(lc.getTipoAtto() + "-" + lc.getDescTipoAtto());
//					else
//						out.println(lc.getTipoAtto()!=null ? lc.getTipoAtto() : "-");
				%>
				</span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Numero Atto</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getNumeroAtto()!=null ? lc.getNumeroAtto() : "-" %></span></td>
				
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Num. Prot. Gen.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getNumProtGen()!=null ? lc.getNumProtGen() : "-" %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Anno Prot. Gen.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getAnnoProtGen()!=null ? lc.getAnnoProtGen() : "-" %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Num. Prot. Sett.</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getNumProtSett()!=null ? lc.getNumProtSett() : "-" %></span></td>
				<td class="TDmainLabel" style="width: 25%; white-space: nowrap;"><span class="TXTmainLabel">Data Doc.</span></td>
				<td class="TDviewTextBox" style="width: 25%;"><span class="TXTviewTextBox"><%=lc.getDataDoc()!=null ?  SDF.format(lc.getDataDoc()) : "-"%></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Nome Via</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lc.getPrefisso()!=null ? lc.getPrefisso()+" " : ""%><%=lc.getNomeVia()!=null ? lc.getNomeVia() : "-" %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Altra Via</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getAltravia()!=null ? lc.getAltravia() : "-" %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Civico </span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=lc.getCivico()!=null ? lc.getCivico() : "-" %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Altri Civici</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getAltriciv()!=null ? lc.getAltriciv():"-"  %></span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Civico/Sub </span></td>
				<td class="TDviewTextBox">
				  <span class="TXTviewTextBox"><%=lc.getCivicoSub()!=null ? lc.getCivicoSub() : "-"%> </span>	  
				</td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Privata</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox">
					<% if (!lc.isPrivata()) 
							out.println("NO");
						else
							out.println("SI");
					%>
				</span></td>
			</tr>
			<tr>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Vincolo Ambientale</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getVincoloAmbientale()!=null ? lc.getVincoloAmbientale() : "-" %></span></td>
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Riparto</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox"><%= lc.getRiparto()!=null ? lc.getRiparto() : "-" %></span></td>
			</tr>
			
			<tr>
			
				
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Tipologia</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox">
					<% 
						if (lc.getDescTipologia() != null)
							out.println(lc.getTipologia() + "-" + lc.getDescTipologia());
						else
							out.println(lc.getTipologia()!=null ? lc.getTipologia() : "-");
						 %>
				</span></td>
			
				<td class="TDmainLabel" style="width: 5%; white-space: nowrap;"><span class="TXTmainLabel">Destinazione</span></td>
				<td class="TDviewTextBox"><span class="TXTviewTextBox">
				<%
					if (lc.getDescDestinazione()!= null)
						out.println(lc.getDestinazione() + "-" + lc.getDescDestinazione());
					else				 
						out.println(lc.getDestinazione()!=null ? lc.getDestinazione() : "-");
				%>
				</span></td>
			</tr>
			<tr>
				
			
			</tr>
		</table>
		</td>
	</tr>
</table>
<br><br>
		<table class="viewExtTable" align ="center" style=" width: 50%">
		<%if (lc.getPathFile() != null){ 
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			if (GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_SCARICA_PLANIMETRIE,true)) {
		%>
			<tr>
				<td class="TDviewTextBox" style="text-align:center">
					<span class="TXTviewTextBox">
							<a href="../DocfaImmaginiPlanimetrie?strFilee=<%=lc.getPathFile()%>&idFunz=3&inxdoc=<%=lc.getIdFile()%>">Apri Documento</a>
					</span>
				</td>
			</tr>
			<tr>
				<td class="TDviewTextBox" style="text-align:center">
					<span class="TXTviewTextBox">
					<%
						if (GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_ELIMINA_WATERMARK,true)) {
							%><a href="../DocfaImmaginiPlanimetrie?strFilee=<%=lc.getPathFile()%>&idFunz=3&inxdoc=<%=lc.getIdFile()%>&watermark=no">Apri Documento (SENZA WATERMARK)</a><%
						}
					%>
					</span>
				</td>
			</tr>
		<% 	}
		} 
		%>
</table></td></tr></table>

<!-- FINE solo dettaglio -->
<% if (lc != null){%>
<% String codice = "";
   codice = lc.getId(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="47">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		
</form>
<div id="wait" class="cursorWait" ></div>
</body>
</html>