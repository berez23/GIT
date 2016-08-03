<%@ page language="java" 
import="it.webred.ct.data.access.basic.imu.dto.*, 
java.util.*, java.math.*,it.escsolution.escwebgis.imu.bean.*, it.escsolution.escwebgis.imu.logic.*,
it.webred.ct.data.access.basic.anagrafe.dto.DatiAnagrafeDTO, 
java.text.*"%>
<%  
HttpSession sessione = request.getSession(true); 
SaldoImuConsulenzaDTO cons = (SaldoImuConsulenzaDTO)sessione.getAttribute(ConsulenzaImuLogic.IMU_CONS);    
SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
DecimalFormat def = new DecimalFormat("#0.00");
%>

<div style="float:left;width:100%">
<% if (cons != null){ 

		DatiAnagrafeDTO dich  = cons.getDichiarante();
		List<XmlF24DTO> lstF24 = cons.getLstF24();
		List<XmlImmobileDTO> lstImm = cons.getLstImmobili();
%>
	<jsp:include page="../saldoImu/saldoImuDatiElab.jsp"></jsp:include>
	<br/>
	<table width="100%" align=left cellpadding="5" cellspacing="0" class="editExtTable" style="background-color:#32517e;">
		<tr >&nbsp;</tr>
		<tr class="extWinTXTTitle" style="font-weight:bold;">DETTAGLIO CONSULENZA</tr>
		<tr>&nbsp;</tr>
		<tr><span class="TXTmainLabel">  - fornita il <%=df.format(cons.getDtConsulenza())%> </span></tr>
	 <tr><td>
	 <%if(dich!=null){ %>
		 <div style=float:left>
 			<table align=left cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;width:100%">
					<tr >&nbsp;</tr>
					<tr class="extWinTXTTitle"><span style="color:white;">Dichiarante</span></tr>
					<tr>
						<td style="text-align: center;">
							<table class="viewExtTable" cellpadding="0" cellspacing="2" style="width: 100%;">
								<tr>
									<td class="TDmainLabel" style="width:20%;">
										<span class="TXTmainLabel">Cognome</span>
									</td>
									
									<td class="TDviewTextBox" style="width:24%;">
										<span class="TXTviewTextBox"><%=dich.getCog()!=null ? dich.getCog() : "-"%></span>
									</td>
									
									<td style="width:4%"></td>
									
									<td class="TDmainLabel" style="width:20%;">
										<span class="TXTmainLabel">Nome</span>
									</td>
									
									<td class="TDviewTextBox" style="width:24%;">
										<span class="TXTviewTextBox"><%=dich.getNom()!=null ? dich.getNom() : "-"%></span>
									</td>					
								</tr>		
								<tr>
									<td class="TDmainLabel" style="width:20%;">
										<span class="TXTmainLabel">Codice Fiscale</span>
									</td>
									
									<td class="TDviewTextBox" style="width:24%;">
										<span class="TXTviewTextBox"><%=dich.getCodFisc()%></span>
									</td>
									
									<td style="width:4%"></td>
									
									<td class="TDmainLabel" style="width:20%;">
										<span class="TXTmainLabel">IBAN</span>
									</td>
									
									<td class="TDviewTextBox" style="width:24%;">
										<span class="TXTviewTextBox"><%=dich.getIban()!=null ? dich.getIban() : "-"%></span>
									</td>					
								</tr>		
								<tr>
									<td class="TDmainLabel" style="width:20%;">
										<span class="TXTmainLabel">Data Nascita</span>
									</td>
									
									<td class="TDviewTextBox" style="width:24%;">
										<span class="TXTviewTextBox"><%=dich.getDatNas()!=null ? df.format(dich.getDatNas()):"-"%></span>
									</td>
									
									<td style="width:4%"></td>
									
									<td class="TDmainLabel" style="width:20%;">
										<span class="TXTmainLabel">Luogo di Nascita</span>
									</td>
									
									<td class="TDviewTextBox" style="width:24%;">
										<span class="TXTviewTextBox">
										<%=dich.getComNas()!=null ? dich.getComNas() : ""%> 
										<%=dich.getProvNas()!=null ? "("+dich.getProvNas()+")" : "" %></span>
									</td>					
								</tr>		
								<tr>
									<td class="TDmainLabel" style="width:20%;">
										<span class="TXTmainLabel">Residenza</span>
									</td>
									
									<td class="TDviewTextBox" style="width:24%;" colspan="4">
										<span class="TXTviewTextBox">
										<%=dich.getIndRes()!=null ? dich.getIndRes() : ""%> 
										<%=dich.getComRes()!=null ? dich.getComRes() : ""%>
										<%=dich.getProvRes()!=null ? "("+dich.getProvRes()+")" : "" %></span>
									</td>	
								</tr>		
						</table>
			</table></div>
	 <%} %>
	 		
	 	 <%if(lstImm!=null && lstImm.size()>0){ %>
	 			<div style=float:left>
					<table align=left  class="extWinTable" cellpadding="0" cellspacing="0" style="width:100%">
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle"><span style="color:white;">Lista Immobili</span></tr>
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Dati Immobile principale e pertinenze</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Aliquota</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Acconto</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Terr.Agricolo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Contit.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Figli</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">IACP</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Terr.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Imm.Var.</span></td>
						</tr>
					<%
						Iterator iter1 = lstImm.iterator();
						int contatore = 1;
						while (iter1.hasNext())
						{
							XmlImmobileDTO dto = (XmlImmobileDTO) iter1.next();
						%>
				
						<tr>
						   
							
						    <td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								
								<table align=right width="100%" class="extWinTable" style="border:none;">
								<tr ><td colspan="10"><span class="extWinTXTData" style="font-weight:bold;"><%= dto.getCatDescrizione() %></span></td></tr>
								<tr>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Rival.</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Valore</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Mesi</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">%</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Storico</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Imposta</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Detrazione</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Dovuto</span></td>
								</tr>
								
								<%for(int i=0; i<dto.getListaValori().length; i++){ 
									ValImmobileDTO val = (ValImmobileDTO)dto.getListaValori()[i];
									if(!val.isVuoto()){%>
									<tr>
									<td class="extWinTDData" style="text-align:left;"><span  title="<%=val.getDescCat() %>" class="extWinTXTData" style="text-align:right;"><%=val.getCod()%></span></td>
									<td class="extWinTDData" style="text-align:right;" ><span class="extWinTXTData" ><%=val.getRendita()!=null && val.getRendita().doubleValue()>0 ? "&euro; " + def.format(val.getRendita()) : "-"%></span></td>
									<td class="extWinTDData" style="text-align:right;" title="Rendita Rivalutata"><span class="extWinTXTData" ><%=val.getRenditaRivalutata()!=null && val.getRenditaRivalutata().doubleValue()>0 ? "&euro; " + def.format(val.getRenditaRivalutata()) : "-"%></span></td>
									<td class="extWinTDData" style="text-align:right;"><span class="extWinTXTData" ><%=val.getValore()!=null && val.getValore().doubleValue()>0 ? "&euro; " + def.format(val.getValore()) : "-"%></span></td>
									<td class="extWinTDData" style="text-align:center;" title="Mesi di Possesso"><span class="extWinTXTData" ><%=val.getMesiPoss()%></span></td>
									<td class="extWinTDData" style="text-align:center;" title="Quota di Possesso"><span class="extWinTXTData" ><%=val.getQuotaPoss()!=null ? def.format(val.getQuotaPoss()):"-"%></span></td>
									<td class="extWinTDData" style="text-align:center;"><span class="extWinTXTData"><%if(val.getStorico().equalsIgnoreCase("S")) {%>
										<img src="../images/ok.gif" border="0" title="Immobile Storico o Inagibile"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span></td>
									<td class="extWinTDData" style="text-align:right;"><span class="extWinTXTData" style="text-align:right;"><%=val.getDovuto()!=null && val.getDovuto().doubleValue()>0 ? "&euro; " + def.format(val.getDovuto()) : "-"%></span></td>
									<td class="extWinTDData" style="text-align:right;"><span class="extWinTXTData" style="text-align:right;"><%=val.getDetrazione()!=null && val.getDetrazione().doubleValue()>0 ? "&euro; " + def.format(val.getDetrazione()) : "-"%></span></td>
									<td class="extWinTDData" style="text-align:right;"><span class="extWinTXTData" style="text-align:right;"><%=val.getDovutoMenoDetrazione()!=null && val.getDovutoMenoDetrazione().doubleValue()>0 ? "&euro; " + def.format(val.getDovutoMenoDetrazione()) : "-"%></span></td>
									
									</tr>
								<%}}%>
								<tr>
								<td class="TDmainLabel" style="text-align:right;" colspan="2"><span class="TXTmainLabel">Somma Rendite Rivalutate</span></td>
								<td class="extWinTDData" style="text-align:right;"><span class="extWinTXTData" style="text-align:right;"><%=dto.getRenditaImmobileRiv()!=null && dto.getRenditaImmobileRiv().doubleValue()>0 ? "&euro; " + def.format(dto.getRenditaImmobileRiv()) : "-"%></span></td>
									
								<td class="TDmainLabel" style="text-align:right;" colspan="6"><span class="TXTmainLabel">Dovuto Quota Comune</span></td>
								<td class="extWinTDData" style="text-align:right;"><span class="extWinTXTData" style="text-align:right;font-weight:bold;"><%=dto.getDovutoComune()!=null && dto.getDovutoComune().doubleValue()>0 ? "&euro; " + def.format(dto.getDovutoComune()) : "-"%></span></td>
								
								</tr>
								<tr>
								<td class="TDmainLabel" style="text-align:right;" colspan="9"><span class="TXTmainLabel">Dovuto Quota Stato</span></td>
								<td class="extWinTDData" style="text-align:right;"><span class="extWinTXTData" style="text-align:right;font-weight:bold;"><%=dto.getDovutoStato()!=null && dto.getDovutoStato().doubleValue()>0 ? "&euro; " + def.format(dto.getDovutoStato()) : "-"%></span></td>
								</tr>
								</table>
							</td>
						    <td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= def.format(dto.getAliquota()) %></span>
							</td>
						    <td class="extWinTDData" style="text-align:center;vertical-align: top;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<table align=right width="100%" class="extWinTable" style="border:none;">
								<tr>
								<td class="TDmainLabel" style="text-align:left;"><span class="TXTmainLabel"></span>
								<td class="TDmainLabel" style="text-align:right;"><span class="TXTmainLabel">Detraz.</span></td>
								<td class="TDmainLabel" style="text-align:right;"><span class="TXTmainLabel">Dovuto</span></td>
								</tr><tr>
								<td class="TDmainLabel" style="text-align:left;" title="Quota Stato"><span  class="TXTmainLabel" style="text-align:left;">Stato</span></td>
								<td class="extWinTDData" style="text-align:right;" title="Detrazione Acconto (quota stato)"><span  class="extWinTXTData" style="text-align:right;"><%=dto.getDetrazioneAccStato()!=null && dto.getDetrazioneAccStato().doubleValue()>0 ? "&euro; " + def.format(dto.getDetrTerrAgrStato()) : "-"%></span></td>
								<td class="extWinTDData" style="text-align:right;" title="Dovuto Acconto (quota stato)"><span class="extWinTXTData" style="text-align:right;"><%=dto.getDovutoAccStato()!=null && dto.getDovutoAccStato().doubleValue()>0 ? "&euro; " + def.format(dto.getDovutoAccStato()) : "-"%></span></td>
								</tr><tr>
								<td class="TDmainLabel" style="text-align:left;" title="Quota Comune" ><span class="TXTmainLabel">Com.</span></td>
								<td class="extWinTDData" style="text-align:right;" title="Detrazione Acconto (quota comune)"><span  class="extWinTXTData"><%=dto.getDetrazioneAccComune()!=null && dto.getDetrazioneAccComune().doubleValue()>0 ? "&euro; " + def.format(dto.getDetrazioneAccComune()) : "-"%></span></td>
								<td class="extWinTDData" style="text-align:right;" title="Dovuto Acconto (quota comune)" ><span class="extWinTXTData"><%=dto.getDovutoAccComune()!=null && dto.getDovutoAccComune().doubleValue()>0 ? "&euro; " + def.format(dto.getDovutoAccComune()) : "-"%></span></td>
								</tr>
								</table>
							</td>
						   <%if(!dto.isVuotoTerrAgr()){ %>
						    <td class="extWinTDData" style="text-align:center;vertical-align: top;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<table align=right width="100%" class="extWinTable" style="border:none;">
								<tr>
								<td class="TDmainLabel" style="text-align:left;"><span class="TXTmainLabel"></span>
								<td class="TDmainLabel" style="text-align:right;"><span class="TXTmainLabel">Detraz.</span></td>
								<td class="TDmainLabel" style="text-align:right;"><span class="TXTmainLabel">Dovuto</span></td>
								</tr><tr>
								<td class="TDmainLabel" style="text-align:left;"><span title="Quota Stato" class="TXTmainLabel" style="text-align:left;">Stato</span></td>
								<td class="extWinTDData" style="text-align:right;" title="Detrazione (quota stato)"><span class="extWinTXTData" style="text-align:right;"><%=dto.getDetrTerrAgrStato()!=null && dto.getDetrTerrAgrStato().doubleValue()>0 ? "&euro; " + def.format(dto.getDetrTerrAgrStato()) : "-"%></span></td>
								<td class="extWinTDData" style="text-align:right;" title="Dovuto (quota stato)"><span class="extWinTXTData" style="text-align:right;"><%="-" %></span></td>
								</tr><tr>
								<td class="TDmainLabel" style="text-align:left;"><span title="Quota Comune" class="TXTmainLabel">Com.</span></td>
								<td class="extWinTDData" style="text-align:right;" title="Detrazione (quota comune)"><span class="extWinTXTData"><%=dto.getDetrTerrAgrComune()!=null && dto.getDetrTerrAgrComune().doubleValue()>0 ? "&euro; " + def.format(dto.getDetrTerrAgrComune()) : "-"%></span></td>
								<td class="extWinTDData" style="text-align:right;" title="Dovuto (quota comune)"><span class="extWinTXTData"><%="-" %></span></td>
								</tr><tr>
								<td class="TDmainLabel" style="text-align:left;"><span title="Totale" class="TXTmainLabel">Tot.</span></td>
								<td class="extWinTDData" style="text-align:right;" title="Detrazione (totale)"><span class="extWinTXTData"><%=dto.getDetrTerrAgr()!=null && dto.getDetrTerrAgr().doubleValue()>0 ? "&euro; " + def.format(dto.getDetrTerrAgr()) : "-"%></span></td>
								<td class="extWinTDData" style="text-align:right;" title="Dovuto (totale)"><span class="extWinTXTData"><%=dto.getDovutoTerrAgr()!=null && dto.getDovutoTerrAgr().doubleValue()>0 ? "&euro; " + def.format(dto.getDovutoTerrAgr()) : "-"%></span></td>
								</tr>
								</table>
								</td>
								<%}else{ %>
								<td class="extWinTDData" style="text-align:center;vertical-align: center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								 <span class="extWinTXTData">-</span></td>
								<%} %>
							
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getNumContitolari()%></span>
							</td>
							<td class="extWinTDData" style="text-align:center;<%if(!dto.getNumFigli().equals("0")){ %>vertical-align:top;<%} %>" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<table align=right width="100%" class="extWinTable" style="border:none;">
								<tr><td colspan="2" style="text-align:center;"><span class="extWinTXTData" ><%= dto.getNumFigli() %></span></td></tr>
								<%if(!dto.getNumFigli().equals("0")){ %>
								<tr>
									<td class="extWinTDTitle"><span title="% Detrazione" class="extWinTXTTitle">%</span>
									<td class="extWinTDTitle"><span title="Mesi Detrazione" class="extWinTXTTitle">Mesi</span></td>
								</tr>
								<%for(int i=0; i<dto.getLstFigli().size(); i++){ 
									XmlFiglioDTO fig = (XmlFiglioDTO)dto.getLstFigli().get(i);
									if(fig.isPresente()){%>
									<tr>
									<td class="extWinTDData" style="text-align:center;"><span class="extWinTXTData" ><%=fig.getPercDetrazione()%></span></td>
									<td class="extWinTDData" style="text-align:center;"><span class="extWinTXTData" ><%=fig.getMesiDetrazione()%></span></td>
									</tr>
								<%}}}%>
								</table>
								
							</td>
							<td class="extWinTDData" style="text-align:left;vertical-align: top;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<table align=right width="100%" class="extWinTable" style="border:none;">
								<tr title="Immobili Assegnati per I.A.C.P.">
								<td class="TDmainLabel" style="text-align:left;"><span  class="TXTmainLabel" style="text-align:left;">Ass.</span></td>
								<td class="extWinTDData" style="text-align:right;"><span class="extWinTXTData" style="text-align:right;"><%= dto.getImmAssegnati()%></span></td>
								</tr>
								<tr title="Totale Immobili per I.A.C.P.">
								<td class="TDmainLabel" style="text-align:left;"><span  class="TXTmainLabel" style="text-align:left;">Tot.</span></td>
								<td class="extWinTDData" style="text-align:right;"><span class="extWinTXTData" style="text-align:right;"><%= dto.getImmTotali()%></span></td>
								</tr>
								</table>
							</td>
							
							 <td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%if(dto.isTerremotato()) {%>
										<img src="../images/ok.gif" border="0" title="Comune Terremotato"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="Comune non Terremotato"/>
										<% } %>
										</span>
						    </td>
							
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								
							<span class="extWinTXTData"><%if(dto.isVariazione()) {%>
										<img src="../images/ok.gif" border="0" title="Immobili Variati"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="Immobili NON Variati"/>
										<% } %>
										</span>
							</td>
						</tr>
					<% 
						contatore++;
				  	} 
				  	%>					
					</table></div>
	 
	 <%} %>
	 		
	 <%if(lstF24!=null && lstF24.size()>0){ %>
	 
	 			<div style=float:left>
					<table align=left  class="extWinTable" cellpadding="0" cellspacing="0" style="width:100%">
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle"><span style="color:white;">
								Lista F24</span>
						</tr>
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Ravv.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Imm.Var.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Acconto</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Saldo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">n&#176;Imm.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Tributo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Rateazione</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Dov.Scad</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Pag.Scad.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Da Ravv.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Sanzione</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Interessi</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Tasso </span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Detrazione</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Imp.Debito</span></td>
						</tr>
					<%
						Iterator iter = lstF24.iterator();
						int contatore = 1;
						while (iter.hasNext())
						{
							XmlF24DTO dto = (XmlF24DTO) iter.next();
						%>
				
						<tr>
						    <td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%if(dto.isFlgRav()) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
						    </td>
							
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								
							<span class="extWinTXTData"><%if(dto.isFlgImmVar()) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
							</td>
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								
								<span class="extWinTXTData"><%if(dto.isFlgAcconto()) {%>
											<img src="../images/ok.gif" border="0" title="SI"/>
											<% } else { %>
											<img src="../images/no.gif" border="0" title="NO"/>
											<% } %>
											</span>
								</td>
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								
							<span class="extWinTXTData"><%if(dto.isFlgSaldo()) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
							</td>
						    <td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getNumImm() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getCodTributo() + "-" + dto.getDescTributo()  %></span>
							</td>
						    <td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getFlgRateazione() %></span>
							</td>
						    <td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%=dto.getAnnoRif()   %></span>
							</td>
							<td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getDovutoScadenza()!=null && dto.getDovutoScadenza().doubleValue()>0 ? "&euro; " + def.format(dto.getDovutoScadenza()) : ""%></span>
							</td>
							<td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getPagatoScadenza()!=null && dto.getPagatoScadenza().doubleValue()>0 ? "&euro; " + def.format(dto.getPagatoScadenza()) : ""%></span>
							</td>
						    <td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getImpDaRavvedere()!=null && dto.getImpDaRavvedere().doubleValue()>0 ? "&euro; " + def.format(dto.getImpDaRavvedere()) : ""%></span>
							</td>
							<td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getSanzione()!=null && dto.getSanzione().doubleValue()>0 ? "&euro; " + def.format(dto.getSanzione()) : ""%></span>
							</td>
						    <td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getInteressi()!=null && dto.getInteressi().doubleValue()>0 ? "&euro; " + def.format(dto.getInteressi()) : ""%></span>
							</td>
							<td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getTasso()!=null && dto.getTasso().doubleValue()>0 ?  def.format(dto.getTasso()) : ""%></span>
							</td>
						    <td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getDetrazione()!=null && dto.getDetrazione().doubleValue()>0 ? "&euro; " + def.format(dto.getDetrazione()) : "" %></span>
							</td>
							 <td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getImpDebito()!=null && dto.getImpDebito().doubleValue()>0 ? "&euro; " + def.format(dto.getImpDebito()) : ""  %></span>
							</td>
						</tr>
					<% 
						contatore++;
				  	} 
				  	%>					
					</table></div>
	 
	 <%} %>
	 </td></tr></table>
<% }else{ %>

	<h3>Consulenza non disponibile per il soggetto</h3>
	
<% } %>

</div>
