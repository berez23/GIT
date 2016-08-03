<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ page language="java" import="it.escsolution.escwebgis.anagrafe.bean.*"%>
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Anagrafe ana = (Anagrafe) sessione.getAttribute(AnagrafeDwhLogic.ANAGRAFEDWH_POPUP); %>
<%   java.util.Vector listaIndirizzi = (java.util.Vector) sessione.getAttribute(it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI_POPUP); %>
	
<%@page import="it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>

	<head>
		<title>Anagrafe - Dettaglio</title>
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
		
		function apriPopUp(posList)
		{
			window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/anagrafe/popupFamigliaDaAnagrafe.jsp?pos='+posList+'&REDDITI=true','dettaglioFamiglia','toolbar=no,scrollbars=yes,resizable=yes,width=500,height=300');
		}

		function chgtr(row,flg)
		{
		if (flg==1)
			{
			document.all("r"+row).style.backgroundColor = "#d7d7d7";
			}
		else
			{
			document.all("r"+row).style.backgroundColor = "";
			}
		}

		function vaiAStorico()
		{
			window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeDwh?OGGETTO_SEL='+document.all.item("IDSTORICO").value+'&IND_EXT=1&ST=101&POPUP=true&UC=45','_self','toolbar=no,scrollbars=yes,resizable=yes,width=500,height=300');
		}
		
	</script>
	
	<body>
	
	<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
	
	<form>
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			Dati Anagrafe</span>
		</div>
		
		<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>
		
		&nbsp;
		<table style="background-color: white; width: 100%;">
		
		
		<tr style="background-color: white;">
		<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
		
		<form>
		<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
			<tr>
				<td colspan=3>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Dati Storici Indice:</span></td>
						<td class="TDviewTextBox" style="width:210;"> 
								<%  
							    HashMap idStorici = (LinkedHashMap)session.getAttribute(EscServlet.IDSTORICI); 
								if (idStorici!=null) {
								%>							
									<select onchange="vaiAStorico()"  id="IDSTORICO" name="IDSTORICO" class="INPDBComboBox"  >
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
			
		</table></form>
		
		&nbsp;
		
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
		
		
		&nbsp;
			
		<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
			
			<tr>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Comune Immigrazione </span></td>
						<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getComuneImmigrazione()%></span></td>
					</tr>
				</table>
				</td>
				
				<td width=10></td>
			
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">In data</span></td>
						<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getDataImmigrazione()%></span></td>
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
						<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Comune Morte</span></td>
						<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getComuneMorte()%></span></td>
					</tr>
				</table>
				</td>
				
				<td width=10></td>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">In data</span></td>
						<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ana.getDtMorte()%></span></td>
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
						<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Posizione Anagrafica</span></td>
						<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ana.getPosizioneAnagrafica()%></span></td>
					</tr>
				</table>
				</td>
			</tr>
			
				
		</table>
		
		
		<br><br><br>
		
		
		<% if (listaIndirizzi != null) {%>
		<span class="TXTmainLabel">Storico Eventi della famiglia</span>
		<table align="center" class="extWinTable" style="width: 60%;">
			
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Famiglia</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Inizio Appartenenza</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Fine Appartenenza</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
			</tr>			  
		
		
			<% it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico anaSto = new it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico(); %>
		  <% java.util.Enumeration en = listaIndirizzi.elements(); int contatore = 1; %>
		  <% while (en.hasMoreElements()) {
		        anaSto = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico)en.nextElement();%>
		    	<tr id="r<%=contatore%>">
		
		    <tr>
		    
		    	<td onclick="apriPopUp('<%=contatore%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="linkextLabel"><%= anaSto.getCodFamiglia() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= anaSto.getInizioFamiglia() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= anaSto.getFineFamiglia() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= anaSto.getDescrVia() %>, <%=anaSto.getCivico() %></span></td>
				
				
				</tr>			
		<% 		contatore++;
			} 
		%>
		</table>
		<%}%>
		
		</td>
		</tr>
		</table>
	</form>
	</body>
