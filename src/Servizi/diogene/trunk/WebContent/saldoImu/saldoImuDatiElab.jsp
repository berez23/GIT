<%@ page language="java" 
import="it.webred.ct.data.access.basic.imu.dto.*, 
java.util.*, java.math.*,it.escsolution.escwebgis.imu.bean.*, it.escsolution.escwebgis.imu.logic.*,
it.webred.ct.data.access.basic.anagrafe.dto.DatiAnagrafeDTO, 
java.text.*"%>
<%  
HttpSession sessione = request.getSession(true); 
SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
DecimalFormat def = new DecimalFormat("#0.00");
SaldoImuElaborazioneDTO elab = (SaldoImuElaborazioneDTO)sessione.getAttribute(ConsulenzaImuLogic.IMU_ELAB);  
%>

<div style="float:left;width:100%">
<% if (elab != null){ 

		DatiAnagrafeDTO dich  = elab.getDichiarante();
		List<DatiAnagrafeDTO> tabAnag = elab.getTabAnagrafe();
		List<JsonDatiCatastoDTO> lstImm = elab.getTabCatastoImm();
		List<JsonDatiCatastoDTO> lstTerr = elab.getTabCatastoTerr();
%>
	 <table width="100%" align=left cellpadding="5" cellspacing="0" class="editExtTable" style="background-color:#32517e;">
		<tr>&nbsp;</tr>
		<tr class="extWinTXTTitle" style="font-color:white"><span>DATI ELABORAZIONE</span></tr>
		
	 <tr><td>
	 
	 <%if(dich!=null){ %>
 			<table align=left cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;width:100%;">
					<tr>&nbsp;</tr>
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
			</table>
	 <%} %>

	 
	 	 <%if(tabAnag!=null && tabAnag.size()>0){ %>
	 
			<div style=float:left width="100%" >
			<table align=left class="extWinTable" cellpadding="0" cellspacing="0" style="width:100%">
				<tr>&nbsp;</tr>
				<tr class="extWinTXTTitle"><span style="color:white;">
						Dati Anagrafici Nucleo Familiare</span>
				</tr>
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fisc.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Nasc.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Com.Nasc.</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Prov.Nasc.</span></td>
				</tr>
			<%
				Iterator iter = tabAnag.iterator();
				int contatore = 1;
				while (iter.hasNext())
				{
					DatiAnagrafeDTO dto = (DatiAnagrafeDTO) iter.next();
				%>
				<tr>
				    <td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
						<span class="extWinTXTData"><%= dto.getCodFisc()!=null ? dto.getCodFisc() : "-" %></span>
					</td>
					<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
						<span class="extWinTXTData"><%= dto.getCog()!=null ? dto.getCog() : "-" %></span>
					</td>
					<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
						<span class="extWinTXTData"><%= dto.getNom()!=null ? dto.getNom() : "-" %></span>
					</td>
					<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
						<span class="extWinTXTData"><%= dto.getDatNas()!=null ? df.format(dto.getDatNas()) : "-"%></span>
					</td>
					<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
						<span class="extWinTXTData"><%= dto.getComNas()!= null ? dto.getComNas() : "-"%></span>
					</td>
					<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
						<span class="extWinTXTData"><%= dto.getProvNas()!= null ? dto.getProvNas() : "-"%></span>
					</td>
				</tr>
			<% 
				contatore++;
		  	} 
		  	%>					
			</table></div>
	 
	 <%} %>
	 
	  	 <%if(lstImm!=null && lstImm.size()>0){ %>
	 
	 				<div style=float:left>
					<table align=left width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle"><span style="color:white;">Lista Immobili</span></tr>
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle"></span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">F / P / S</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Valid.Imm.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Cat.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Cls</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Cons.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Soggetto</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Reg.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Tasso</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Aliquota</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Detr.</span></td>
						</tr>
					<%
						Iterator iter1 = lstImm.iterator();
						int contatore = 1;
						while (iter1.hasNext())
						{
							JsonDatiCatastoDTO dto = (JsonDatiCatastoDTO) iter1.next();
						%>
				
						<tr>
						   
							
						    <td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								
								<table align=right width="100%" class="extWinTable" style="border:none;">
								<tr ><td colspan="10"><span class="extWinTXTData" style="font-weight:bold;"><%= dto.getTxtTipologia() %></span></td></tr>
								<tr>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Mesi</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">%</span></td>
								</tr>
								
								<%for(int i=0; i<dto.getValori().length; i++){ 
									ValImmobileDTO val = (ValImmobileDTO)dto.getValori()[i];
									if(!val.isVuoto()){%>
									<tr>
									<td class="extWinTDData" style="text-align:left;"><span  title="<%=val.getDescCat() %>" class="extWinTXTData" style="text-align:right;"><%=val.getCod()%></span></td>
									<td class="extWinTDData" style="text-align:right;" ><span class="extWinTXTData" ><%=val.getRendita()!=null && val.getRendita().doubleValue()>0 ? "&euro; " + def.format(val.getRendita()) : "-"%></span></td>
									<td class="extWinTDData" style="text-align:center;" title="Mesi di Possesso"><span class="extWinTXTData" ><%=val.getMesiPoss()%></span></td>
									<td class="extWinTDData" style="text-align:center;" title="Quota di Possesso"><span class="extWinTXTData" ><%=val.getQuotaPoss()!=null ? def.format(val.getQuotaPoss()):"-"%></span></td>
									</tr>
								<%}}%>
								</table>
							</td>
						    <td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= dto.getSez() %></span>
							</td>
							 <td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%= dto.getFoglio()+ " / "+dto.getNum()+" / "+dto.getSub() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%="da " + dto.getDataInizio() + " a " + dto.getDataFine() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%=dto.getCat() %></span>
							</td>
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%=dto.getClasse()%></span>
							</td>
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%=dto.getConsistenza() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
								<span class="extWinTXTData"><%=dto.getUbi() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<table width="100%" style="border:none;" cellpadding="0" cellspacing="0">
								<tr ><td><span class="extWinTXTData"><%= dto.getDenom() %></span></td></tr>
								<tr ><td>
									<span class="extWinTXTData" style="font-weight:bold;"><%= "(c.f." %></span>
									<span class="extWinTXTData" ><%= dto.getCodFisc()!=null ? dto.getCodFisc() :" - " %></span>
									<span class="extWinTXTData" style="font-weight:bold;"><%= ")" %></span>
								</td></tr>
								<tr ><td>
									<span class="extWinTXTData" style="font-weight:bold;"><%= "nato il " %></span>
									<span class="extWinTXTData" ><%= dto.getDtNas()%></span>
								</td></tr>
								</table>
							</td>
							
							<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getTit() + " da " + dto.getDataInizioTit() +" a " +dto.getDataFineTit()%></span>
							</td>
							
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getRegime() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getTasso() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getAliquota()+" &#8240;" %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=" &#8364; " + dto.getDetrazione()%></span>
							</td>
							
						</tr>
					<% 
						contatore++;
				  	} 
				  	%>					
					</table></div>
	 
	 <%} %>
	 		

		 	 <%if(lstTerr!=null && lstTerr.size()>0){ %>
	 
	 				<div style=float:left>
					<table align=left width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle"><span style="color:white;">Lista Terreni</span></tr>
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle"></span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">F / P / S</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Valid.Imm.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Area</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Red.Agr.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Red.Domin.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Qualità</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Soggetto</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Reg.</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Tasso</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Aliquota</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Detr.</span></td>
						</tr>
					<%
						Iterator iter1 = lstTerr.iterator();
						int contatore = 1;
						while (iter1.hasNext())
						{
							JsonDatiCatastoDTO dto = (JsonDatiCatastoDTO) iter1.next();
						%>
				
						<tr>
						   
							
						    <td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								
								<table align=right width="100%" class="extWinTable" style="border:none;">
								<tr ><td colspan="10"><span class="extWinTXTData" style="font-weight:bold;"><%= dto.getTxtTipologia() %></span></td></tr>
								<tr>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Mesi</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">%</span></td>
								</tr>
								
								<%for(int i=0; i<dto.getValori().length; i++){ 
									ValImmobileDTO val = (ValImmobileDTO)dto.getValori()[i];
									if(!val.isVuoto()){%>
									<tr>
									<td class="extWinTDData" style="text-align:left;"><span  title="<%=val.getDescCat() %>" class="extWinTXTData" style="text-align:right;"><%=val.getCod()%></span></td>
									<td class="extWinTDData" style="text-align:right;" ><span class="extWinTXTData" ><%=val.getRendita()!=null && val.getRendita().doubleValue()>0 ? "&euro; " + def.format(val.getRendita()) : "-"%></span></td>
									<td class="extWinTDData" style="text-align:center;" title="Mesi di Possesso"><span class="extWinTXTData" ><%=val.getMesiPoss()%></span></td>
									<td class="extWinTDData" style="text-align:center;" title="Quota di Possesso"><span class="extWinTXTData" ><%=val.getQuotaPoss()!=null ? def.format(val.getQuotaPoss()):"-"%></span></td>
									</tr>
								<%}}%>
								</table>
							</td>
						    <td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%= dto.getSez() %></span>
							</td>
							 <td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%= dto.getFoglio()+ " / "+dto.getNum()+" / "+dto.getSub() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%="da " + dto.getDataInizio() + " a " + dto.getDataFine() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getArea()%></span>
							</td>
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getRedditoAgrario()%></span>
							</td>
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getRedditoDominicale() %></span>
							</td>
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getQualita() %></span>
							</td>
							<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getUbi() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getDenom() +"(c.f. "+dto.getCodFisc()+") nato il " + dto.getDtNas()  %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:left;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getTit() + " da " + dto.getDataInizioTit() +" a " +dto.getDataFineTit()%></span>
							</td>
							
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getRegime() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:center;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getTasso() %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=dto.getAliquota()+" &#8240;" %></span>
							</td>
							
							<td class="extWinTDData" style="text-align:right;" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
								<span class="extWinTXTData"><%=" &#8364; " + dto.getDetrazione()%></span>
							</td>
							
						</tr>
					<% 
						contatore++;
				  	} 
				  	%>					
					</table></div>
	 
	 <%} %>
	 		
	</td></tr></table>
	 
<% } %>

</div>
