<%@ page language="java" import="it.escsolution.escwebgis.catasto.bean.*,
			it.escsolution.escwebgis.catasto.logic.*,
 			it.webred.SisterClient.dto.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<% HttpSession sessione = request.getSession(true);  %> 
<% VisuraDTO visura=(VisuraDTO)sessione.getAttribute(VisuraNazionaleLogic.VISURA_NAZIONALE); %>


<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top;">

<%if(visura.getMessaggio()==null){%>

<center><span class="extWinTXTTitle">Soggetto Selezionato</span></center>
		
		<table cellpadding="0" cellspacing="0" align=center>
				
			<tr>		
						
				<td>		
				<table class="viewExtTable" >
					<tr>	
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Tipo Richiesta</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=visura.getTipoRichiesta()!=null ? visura.getTipoRichiesta() : "-"%></span></td>
					</tr>			
				</table>
				</td>
				
			</tr>
			
			<tr>
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Cognome</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=visura.getCognome()!=null ? visura.getCognome() : "-"%></span></td>
					</tr>
				</table>
				</td>	
				
			</tr>
			
			<tr>
				
				<td>	
				<table class="viewExtTable" >
					<tr>			
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Nome</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=visura.getNome()!=null ? visura.getNome() : "-"%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
			
			<tr>
			
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Data di Nascita</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=visura.getDataNasc()!=null ? visura.getDataNasc() : "-"%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
		
			<tr>		
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Comune di Nascita</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=visura.getComuneNasc()!=null ? visura.getComuneNasc() : "-"%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
			
			<tr>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Codice Fiscale</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=visura.getCf()!=null ? visura.getCf() : "-"%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
			
			<tr>
				
				<td>
				<table class="viewExtTable" >
					<tr>
						<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Motivazione</span></td>
						<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=visura.getMotivazione()!=null ? visura.getMotivazione() : "-"%></span></td>
					</tr>
				</table>
				</td>
				
			</tr>
			
		</table>
		
		<br/>
		<!-- visualizzazione tabella -->
		
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">		
			<tr>	
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Provincia</span></td>	
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Num.Terreni</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Num.Fabbricati</span></td>
				
			</tr>
		  	<%for (ProvinciaDTO prov : visura.getListaProv()) {%>
			 	<tr >
			 		<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=prov.getProvincia()%>
						</span>
					</td>
					<td class="extWinTDData" align="center">
						<span class="extWinTXTData">
							<%=prov.getNumTerreni()%>
						</span>
					</td>
					<td class="extWinTDData"  align="center">
						<span class="extWinTXTData">
							<%=prov.getNumFabbricati()%>
						</span>
					</td>
				</tr>
			<% } %>
		</table>
	
	<%}else{ %>
	
	<center><span class="extWinTXTTitle"><%=visura.getMessaggio()%></span></center>
	
	<%} %>
	
</td>

</tr>
</table>