<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ page language="java" %>
<%   HttpSession sessione = request.getSession(true);  %> 
<%   FornitureGas gas = (FornitureGas) sessione.getAttribute(FornitureGasLogic.DATI_GAS); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  FornitureGasFinder finder = null;

	if (sessione.getAttribute(FornitureGasServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(FornitureGasServlet.NOMEFINDER)).getClass() == new FornitureGasFinder().getClass()){
			finder = (FornitureGasFinder)sessione.getAttribute(FornitureGasServlet.NOMEFINDER);
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
	

	
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>

<%@page import="it.escsolution.escwebgis.gas.logic.FornitureGasLogic"%>
<%@page import="it.escsolution.escwebgis.gas.bean.FornitureGas"%>
<%@page import="it.escsolution.escwebgis.gas.servlet.FornitureGasServlet"%>
<%@page import="it.escsolution.escwebgis.gas.bean.FornitureGasFinder"%><html>
<head>
<title>Forniture Gas - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>


function vaiAStorico()
{
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/FornitureGas";	
	document.mainform.ST.value = 3;
	document.mainform.OGGETTO_SEL.value = document.all.item("IDSTORICO").value;
	document.mainform.submit();
}


function mettiST(){
	document.mainform.ST.value = 3;

}


</script>
<body onload="mettiST()">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dati Forniture Gas</span>
</div>
&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>
<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

<form name="mainform" action="<%= request.getContextPath() %>/FornitureGas" target="_parent">



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
							<td class="TDmainLabel" style="width:210;"><span class="TXTmainLabel">Dati Storici:</span></td>
							<td class="TDviewTextBox" style="width:210;"> 
									<%  
									    HashMap idStorici = (LinkedHashMap)session.getAttribute(EscServlet.IDSTORICI); 
										if (idStorici!=null) {
									%>							
										<select onchange="vaiAStorico()" id="IDSTORICO" name="IDSTORICO" class="INPDBComboBox"  >
										  <% 
												  Iterator it = idStorici.entrySet().iterator();
												  while (it.hasNext()) {
												      Map.Entry me = (Map.Entry)it.next();
												      String id = (String)me.getKey();
												      String data =(String) me.getValue(); 
												      %>
								       			        <option 
								       			        <%if (id.equals(gas.getId())){%>
								       			        		SELECTED
								       			        <%} %>
								       			        value="<%=id%>"><%=data%></option>
											  	<% 
												  } 
									  			%>
								        </select>
								     <%
								     }
								     %>				
			
							</td>
						</tr>
					</table>
					</td>
				</tr>				
			</table>
			
			&nbsp;
			
			<%
			boolean is2012 = false;
			String annoRiferimento = gas.getAnnoRiferimento();
			if (annoRiferimento != null) {
				try {
					is2012 = Integer.parseInt(annoRiferimento) >= 2012;
				} catch (Exception e) {}
			}
			%>
			
			<table align=center cellpadding="3" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 100%;">
				<tr>		
					<td class="TDmainLabel" style="width: 120px;"><span class="TXTmainLabel">Identificativo Utenza</span></td>
					<td class="TDviewTextBox" colspan="3"><span class="TXTviewTextBox"><%=gas.getIdentificativoUtenza()%></span></td>
					<td class="TDmainLabel" style="width: 120px;"><span class="TXTmainLabel">Tipologia Fornitura</span></td>
					<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=gas.getTipologiaFornitura() %></span></td>		
				</tr>
				<tr>
					<td class="TDmainLabel"><span class="TXTmainLabel">Anno Riferimento</span></td>
					<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=gas.getAnnoRiferimento() %></span></td>
					<td class="TDmainLabel" style="width: 120px;"><span class="TXTmainLabel">Mesi Fatturazione</span></td>
					<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=gas.getnMesiFatturazione() %></span></td>
					<% if (!is2012) { %>
						<td class="TDmainLabel"><span class="TXTmainLabel">Spesa (Netto IVA)</span></td>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=gas.getSpesaConsumoNettoIva() %></span></td>
					<% } else {%>
						<td class="TDmainLabel"><span class="TXTmainLabel">Ammontare fatturato</span></td>
						<% String segno = "";
						if (gas.getSegnoAmmontFatturato() != null && gas.getSegnoAmmontFatturato().equals("-")) {
							segno = "-";
						}%>
						<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=segno + gas.getAmmontFatturato() %></span></td>
					<% } %>		
				</tr>
				<% if (is2012) { %>
					<tr>
						<td class="TDmainLabel"><span class="TXTmainLabel">Consumo fatturato mc.</span></td>
						<td class="TDviewTextBox" colspan="5"><span class="TXTviewTextBox"><%=gas.getConsumoFatturato() %></span></td>		
					</tr>
				<% } %>
				<tr>
					<td class="TDmainLabel"><span class="TXTmainLabel">Indirizzo Utenza</span></td>
					<td class="TDviewTextBox" colspan="3"><span class="TXTviewTextBox"><%=gas.getIndirizzoUtenza()%></span></td>
					<td class="TDmainLabel"><span class="TXTmainLabel">CAP</span></td>
					<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=gas.getCapUtenza()!=null&&!gas.getCapUtenza().trim().equals("")?gas.getCapUtenza():"-" %></span></td>
				</tr>
				<tr>
					<td class="TDmainLabel"><span class="TXTmainLabel">Tipo Utenza</span></td>
					<td class="TDviewTextBox" colspan="5"><span class="TXTviewTextBox"><%=gas.getTipoUtenza() %></span></td>		
				</tr>
				<tr>
					<td class="TDmainLabel"><span class="TXTmainLabel">C.F. Erogante</span></td>
					<td class="TDviewTextBox" colspan="5"><span class="TXTviewTextBox"><%=gas.getCfErogante() %></span></td>		
				</tr>
			</table>
			
			&nbsp;
			
			<table align=center cellpadding="3" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 100%;">
				<tr>
					<td class="TDmainLabel" style="width: 90px;"><span class="TXTmainLabel">Tipo Soggetto </span></td>
					<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=gas.getTipoSoggetto() %></span></td>
					<td class="TDmainLabel" style="width: 100px;"><span class="TXTmainLabel">Ragione Sociale</span></td>
					<td class="TDviewTextBox" colspan="3"><span class="TXTviewTextBox"><%=gas.getRagioneSociale()!=null&&!gas.getRagioneSociale().trim().equals("")?gas.getRagioneSociale():"-" %></span></td>
				</tr>
				<tr>
					<td class="TDmainLabel"><span class="TXTmainLabel">Codice Fiscale</span></td>
					<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=gas.getCfTitolareUtenza()%></span></td>
					<td class="TDmainLabel"><span class="TXTmainLabel">Cognome Utente</span></td>
					<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=gas.getCognomeUtente()%></span></td>
					<td class="TDmainLabel" style="width: 80px;"><span class="TXTmainLabel">Nome Utente</span></td>
					<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=gas.getNomeUtente()%></span></td>
				</tr>
				<tr>
					<td class="TDmainLabel"><span class="TXTmainLabel">Sesso</span></td>
					<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=gas.getSesso() %></span></td>
					<td class="TDmainLabel" style="width: 70px;"><span class="TXTmainLabel">Nato a</span></td>
					<td class="TDviewTextBox" colspan="2"><span class="TXTviewTextBox"><%=gas.getDescComuneNascita() + " - " + gas.getSiglaProvNascita() %></span></td>
					<td class="TDmainLabel"><span class="TXTmainLabel">In Data</span></td>
					<td class="TDviewTextBox"><span class="TXTviewTextBox"><%=gas.getDataNascita() %></span></td>
				</tr>
			</table>
						
		</td>
	</tr>
</table>

<br>

<% if (gas != null){%>
<% String codice = "";
   codice = gas.getId() ;%>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
<%}%>
		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="54">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		<input type="hidden" name="IdStorici" id="IdSto" value="true">
</form>


<div id="wait" class="cursorWait" />
</body>
</html>