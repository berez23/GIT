<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page language="java" import="it.escsolution.escwebgis.anagrafe.bean.*"%>

<%   HttpSession sessione = request.getSession(true);  %> 
<%   Anagrafe ana = (Anagrafe) sessione.getAttribute(AnagrafeDwhLogic.ANAGRAFEDWH); %>
<%   java.util.Vector listaIndirizzi = (java.util.Vector) sessione.getAttribute(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI + "_" + ana.getCodAnagrafe()); %>

<%   String visStoricoFam = (String)sessione.getAttribute(AnagrafeDwhLogic.VIS_STORICO_FAM_KEY);
	 visStoricoFam = visStoricoFam == null ? AnagrafeDwhLogic.VIS_STORICO_FAM_NO : visStoricoFam; %>
	 
<%   java.util.Date dtAggStorico = (java.util.Date)sessione.getAttribute(AnagrafeDwhLogic.VIS_STORICO_DT_AGG); %>

<%   List<AnagrafeStorico> lstAnaSto = (List)sessione.getAttribute(AnagrafeDwhLogic.VIS_STORICO_LISTA + "_" + ana.getCodAnagrafe()); %>

<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder finder = null;

	if (sessione.getAttribute(it.escsolution.escwebgis.anagrafe.servlet.AnagrafeDwhServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(it.escsolution.escwebgis.anagrafe.servlet.AnagrafeDwhServlet.NOMEFINDER)).getClass() == new AnagrafeDwhFinder().getClass()){
			finder = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder)sessione.getAttribute(it.escsolution.escwebgis.anagrafe.servlet.AnagrafeDwhServlet.NOMEFINDER);
			}
					
	}
	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}

String origine = (String)sessione.getAttribute("origine");
%>

<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>	

	
<%@page import="it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>

<%@page import="it.escsolution.escwebgis.diagnostiche.util.DiaBridge"%>

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
			document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeDwh";
			document.mainform.OGGETTO_SEL.value = oggettoSel;
			document.mainform.ST.value=3;	
			document.mainform.EXT.value=1;
			document.mainform.submit();
		}
		
		function vaiAStorico()
		{
			wait();
			document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeDwh";	
			document.mainform.ST.value = 33;
			document.mainform.OGGETTO_SEL.value = document.all.item("IDSTORICO").value;
			document.mainform.submit();
		}
		
		
		function mettiST(){
			document.mainform.ST.value = 3;
			
			//var body = document.getElementsByTagName("body")[0].innerHTML;	
			//top.parent.frames["nascosto"].document.getElementById("historyid").value = body;
			//top.parent.frames["nascosto"].document.getElementById("mainformid").submit();
			
		
		}
		
		function visDia(params) {
			params += '&<%=DiaBridge.SESSION_KEY%>=<%=AnagrafeDwhLogic.ANAGRAFEDWH%>&popup=yes';
			window.open('<%= request.getContextPath() %>/DiagnosticheViewer' + params,'','toolbar=no,scrollbars=yes,resizable=yes,width=800,height=600');
		}
		
		function apriPopUp()
		{
			window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/anagrafe/popupSoggettoDaAnagrafe.jsp?codAnagrafe=<%=ana.getCodAnagrafe()%>','dettaglioFamiglia','toolbar=no,scrollbars=yes,resizable=yes,top=100,left=300,width=800,height=600');
		}
		
		function apriPopUpStorico()
		{
			window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/anagrafe/popupSoggettoDaAnagrafe.jsp?codAnagrafe=<%=ana.getCodAnagrafe()%>&STORICO=true','dettaglioStoricoFamiglia','toolbar=no,scrollbars=yes,resizable=yes,top=100,left=300,width=800,height=600');
		}
	</script>

	<body style="overflow-y: auto;">
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			<%=funzionalita%>:&nbsp;Dati Anagrafe</span>
		</div>
		
		<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
								onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
								onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
								onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
								onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
		<br/>

		<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeDwh" target="_parent">

		<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>

		<% 
			EnvUtente eu = new EnvUtente((CeTUser)sessione.getAttribute("user"), null, null); 
			String ente = eu.getEnte();
			String name = eu.getUtente().getName();
		%>

		<div style="margin-top: 15px;">
			<span class="TXTmainLabel">
				<%=DiaBridge.getDiaHtmlTestata(ente, name, ana, request.getContextPath())%>
			</span>
		</div>

		<br/>

		<table align=center class="editExtTable" style="background-color: #F3F2F2;">
			<tr>
				<td colspan=3>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:260;"><span class="TXTmainLabel">Dati Storici:</span></td>
						<td class="TDviewTextBox" style="width:260;"> 
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
							       			        <%if (id.equals(ana.getId())){%>
							       			        		SELECTED
							       			        <%} %>
							       			        value="<%=id%>"   ><%=data%></option>
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

		<br/>

		<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #F3F2F2;">
		
			<tr>
				<td colspan=3>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:260;"><span class="TXTmainLabel">Codice Anagrafe</span></td>
						<td class="TDviewTextBox" style="width:260;"><span class="TXTviewTextBox"><%=ana.getCodAnagrafe()%></span></td>
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
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Cognome</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getCognome()%></span></td>
					</tr>
				</table>
				</td>
				
				<td width=10></td>
						
				<td>		
				<table class="viewExtTable" >
					<tr>	
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Nome</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getNome()%></span></td>
					</tr>			
				</table>
				</td>
			</tr>
			
		</table>
	
		<br/>
	
		<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #F3F2F2;">
			
			<tr>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Nato a </span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getComuniNascita()%></span></td>
					</tr>
				</table>
				</td>
				
				<td width=10></td>
			
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">In data</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getDataNascita()%></span></td>
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
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Sesso</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getSesso()%></span></td>
					</tr>
				</table>
				</td>
				
				<td width=10></td>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Tipo Soggetto</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getTipoSoggetto()%></span></td>
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
						<td class="TDmainLabel"  style="width:260;"><span class="TXTmainLabel">Codice Fiscale</span></td>
						<td class="TDviewTextBox" style="width:260;"><span class="TXTviewTextBox"><%=ana.getCodFiscale()%></span></td>
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
						<td class="TDmainLabel"  style="width:260;"><span class="TXTmainLabel">Stato Civile</span></td>
						<td class="TDviewTextBox" style="width:260;"><span class="TXTviewTextBox"><%=ana.getStatoCivile()%></span></td>
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
						<td class="TDmainLabel"  style="width:260;"><span class="TXTmainLabel">Cittadinanza</span></td>
						<td class="TDviewTextBox" style="width:260;"><span class="TXTviewTextBox"><%=ana.getCittadinanze()%></span></td>
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
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Residenza</span></td>
						<td class="TDviewTextBox" style="width:395;">
							<%if (ana.getDtMorte() != null && !ana.getDtMorte().equals("") && !ana.getDtMorte().equals("-")) {%>
								<span class="TXTviewTextBox" style="color:red;">
									<%=ana.getSesso() != null && ana.getSesso().equalsIgnoreCase("F") ? "DECEDUTA" : "DECEDUTO"%> - Ultima residenza nel periodo considerato:
									<br />
								</span>
							<% } %>
							<span class="TXTviewTextBox"><%= (ana.getResidenza() == null) ? "&nbsp;-&nbsp;" : ana.getResidenza() %></span>
						</td>
					</tr>
				</table>
				</td>
			</tr>	
				
		</table>

		<br/>
	
		<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #F3F2F2;">
			
			<tr>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Comune Immigrazione </span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getComuneImmigrazione()%></span></td>
					</tr>
				</table>
				</td>
				
				<td width=10></td>
			
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">In data</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getDataImmigrazione()%></span></td>
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
						<td class="TDmainLabel"  style="width:260;"><span class="TXTmainLabel">Indirizzo emigrazione</span></td>
						<td class="TDviewTextBox" style="width:260;"><span class="TXTviewTextBox"><%=(ana.getIndirizzoEmi() == null) ? "&nbsp;-&nbsp;" : ana.getIndirizzoEmi() %></span></td>
					</tr>
				</table>
				</td>
			</tr>
		
			<tr>
			
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Comune Emigrazione</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getComuneEmigrazione()%></span></td>
					</tr>
				</table>
				</td>
				
				<td width=10></td>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">In data</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getDtEmigrazione()%></span></td>
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
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">Comune Morte</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getComuneMorte()%></span></td>
					</tr>
				</table>
				</td>
				
				<td width=10></td>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:125;"><span class="TXTmainLabel">In data</span></td>
						<td class="TDviewTextBox" style="width:125;"><span class="TXTviewTextBox"><%=ana.getDtMorte()%></span></td>
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
						<td class="TDmainLabel"  style="width:260;"><span class="TXTmainLabel">Posizione Anagrafica</span></td>
						<td class="TDviewTextBox" style="width:260;"><span class="TXTviewTextBox"><%=ana.getPosizioneAnagrafica()%></span></td>
					</tr>
				</table>
				</td>
			</tr>
			
			<tr>
				<td colspan=3>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:260;"><span class="TXTmainLabel">Mappa</span></td>
						<td class="TDviewTextBox" style="width:260;">
							<span class="TXTviewTextBox">						
								<img src="../ewg/images/Localizza.gif" border="0" onclick="zoomInMappaAnagrafeDwh('<%= ana.getCodiceNazionale() %>','<%=ana.getIdExt()%>')" style='cursor: pointer;'/>
							</span>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			
				
		</table>

		<br /><br />

		<%	if (visStoricoFam.equals(AnagrafeDwhLogic.VIS_STORICO_FAM_NO) || visStoricoFam.equals(AnagrafeDwhLogic.VIS_STORICO_FAM_BOTH)) { 
			if (listaIndirizzi != null) {%>
				<div align="center">
					<span class="TXTviewTextBox" style="background-color: white; cursor: pointer;">
						<a onclick="apriPopUp(); return false;" style="text-decoration: underline;" title="Clicca per visualizzare lo storico della famiglia per il soggetto">
							Storico della Famiglia per il Soggetto
						</a>
					</span>
				</div>
				<br />
			<%}
		}%>
		
		<% if ((visStoricoFam.equals(AnagrafeDwhLogic.VIS_STORICO_FAM_ONLY) || visStoricoFam.equals(AnagrafeDwhLogic.VIS_STORICO_FAM_BOTH)) && dtAggStorico != null) {
			if (lstAnaSto != null && lstAnaSto.size() > 0) {%>
				<div align="center">
					<span class="TXTviewTextBox" style="background-color: white; cursor: pointer;">
						<a onclick="apriPopUpStorico(); return false;" style="text-decoration: underline;" title="Clicca per visualizzare lo storico della famiglia elaborato in data <%= new SimpleDateFormat("dd/MM/yyyy").format(dtAggStorico) %>">
							Storico della Famiglia elaborato in data <%= new SimpleDateFormat("dd/MM/yyyy").format(dtAggStorico) %>
						</a>
					</span>
				</div>
				<br />
			<%}
		}%>
		
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
		<input type='hidden' name="UC" value="45">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		<input type="hidden" name="IdStorici" id="IdSto" value="true">
		
		<% if (origine != null && origine.trim().equalsIgnoreCase("MAPPA")){ %>
		<input type='hidden' name="origine" value="<%=origine %>">
		
		<% } %>
		
		</form>
	
		<div id="wait" class="cursorWait" />
		
	</body>

</html>