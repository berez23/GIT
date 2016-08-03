<%@ page language="java" import="java.util.*,it.webred.fb.data.model.*, it.webred.fb.ejb.dto.*, java.text.DecimalFormat, java.math.BigDecimal"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  DettaglioBene bene = (DettaglioBene)sessione.getAttribute(DemanioLogic.BENE); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.demanio.bean.DemanioFinder finder = null;

DecimalFormat DF = new DecimalFormat("#0.00");

if (sessione.getAttribute(DemanioLogic.FINDER) != null){ 
	finder = (it.escsolution.escwebgis.demanio.bean.DemanioFinder)sessione.getAttribute(DemanioLogic.FINDER);
}%>

<%int js_back = 1;
if (sessione.getAttribute("BACK_JS_COUNT")!= null)
	js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();


java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>

<%@page import="it.escsolution.escwebgis.demanio.bean.*"%>
<%@page import="it.escsolution.escwebgis.demanio.logic.*"%>
<html>
	<head>
		<title>Demanio - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	
	<script>
		function mettiST(){
			document.mainform.ST.value = 3;
		}


		function vaiDettaglioBene(id){
			var params = '?UC=132&ST=33&POPUP=true';
			params += "&OGGETTO_SEL=" + id;
			window.open('<%= request.getContextPath() %>/Demanio' + params, 'Demanio','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=780,height=580');
		}
		
		function visualizzaDettaglioOggetto(idx, visDett) {
			document.getElementById("rOgg" + idx).style.display = (visDett ? "none" : "");
			document.getElementById("rOggDett" + idx).style.display = (visDett ? "" : "none");
		}


		function vai(chiave, record_cliccato){
			wait();
			document.mainform.OGGETTO_SEL.value=chiave;
			document.mainform.RECORD_SEL.value=record_cliccato;
			document.mainform.ST.value = 3;
			document.mainform.submit();
		}
	

		function vaiBene(codice, record_cliccato, isPopUp)
		{
			try
			{
				document.mainform.OGGETTO_SEL.value = codice;
				alert('entrato '+codice);
				document.mainform.RECORD_SEL.value = record_cliccato;
				alert('entrato'+record_cliccato);
				alert('entrato');
				if (isPopUp)
				{
					alert('is  popup');
					targ = apriPopUp(record_cliccato);
					
					if (targ)
					{
						document.mainform.ST.value = 33;
						document.mainform.target = targ;
						document.mainform.submit();
						document.mainform.ST.value = 2;
						document.mainform.target = "_parent";
					}
				}
				else
				{	
					alert('is not popup');
					wait();
					document.mainform.ST.value = 3;
					document.mainform.target = "_parent";
					document.mainform.submit();
				}
			}
			catch (e)
			{
				//alert(e);
			}
		}

		var popUpAperte = new Array();
		function apriPopUp(index)
		{
			if (popUpAperte[index])
			{
				if (popUpAperte[index].closed)
				{
					popUpAperte[index] = window.open("", "DemanioDetail" + index, "width=640,height=480,status=yes,resizable=yes");
					popUpAperte[index].focus();
					return popUpAperte[index].name;
				}
				else
				{
					popUpAperte[index].focus();
					return false;
				}
			}
			else
			{
				popUpAperte[index] = window.open("", "DemanioDetail" + index, "width=640,height=480,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
		
	</script>
	
	<body>

			<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Dati Bene Comunale</span>
			</div>
	
			&nbsp;
	<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>

<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>
	&nbsp;
	
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Demanio" target="_parent">
			<%DmBBene b = bene.getBene();%> 			  
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
										<tr>
											<td class="TDviewTextBox" colspan="4">
											<% if (b.getDmBBene()!=null) {%>
												<span class="TXTmainLabel">
												<a href="javascript:vaiDettaglioBene(<%=b.getDmBBene().getId()%>);">Visualizza gerarchia superiore</a>
												</span>
											<%}%>	
											</td>
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Cod.Id.</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=b.getChiave()%></span>
											</td>			
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipo Bene </span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=b.getDesTipoBene()%></span>
											</td>			
											
									</tr>
									<%DmBBeneInv inventario = b.getDmBBeneInv();%>
									<% if(inventario!=null){%>
									<tr>
										
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Cod.Inventario</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=inventario.getCodInventario()!=null ? inventario.getCodInventario() : "-"%></span>
											</td>
											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Categoria Inv. </span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=inventario.getCodCatInventariale()+" - "+inventario.getDesCatInventariale()%></span>
											</td>			
											
									</tr>
									<%}%>
									
									<%DmBFascicolo fascicolo = bene.getFascicolo();%>
									<% if(fascicolo!=null){%>
									<tr>
										
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Cart. Arch. Demanio</span>
											</td>
											
											<td class="TDviewTextBox" colspan="3">
												<span class="TXTviewTextBox"><%=fascicolo.getCodFascicolo()!=null ? fascicolo.getCodFascicolo() : ""%></span>
												<span class="TXTviewTextBox"><%=fascicolo.getDesFascicolo()!=null ? fascicolo.getDesFascicolo() : ""%></span>
											</td>			
											
									</tr>
									<%} %>
									
									<%DmBTipoUso uso = bene.getUso();%>
									<% if(uso!=null){%>
									<tr>
										
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tipo Uso</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=uso.getCodTipoUso()!=null ? uso.getCodTipoUso()+" " : ""%></span>
												<span class="TXTviewTextBox"><%=uso.getDesTipoUso()!=null ? uso.getDesTipoUso(): ""%></span>
											</td>			
											
									</tr>
									<%} %>
									
									<tr>		
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Descrizione</span>
											</td>
											
											<td class="TDviewTextBox"  colspan="5">
												<span class="TXTviewTextBox"><%=b.getDescrizione()!=null ? b.getDescrizione().trim() : "-"%></span>
											</td>
	
									</tr>
									<tr>		
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Note</span>
											</td>
											
											<td class="TDviewTextBox"  colspan="5">
												<span class="TXTviewTextBox"><%=b.getNote()!=null ? b.getNote().trim() : "-" %></span>
											</td>
	
									</tr>
											
									</table>
								</td>
							</tr>
						</table>						
					</td>
				</tr>
				<%DmBInfo info= bene.getInfo(); %>
				<%if(info!=null){%>
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Dati catastali</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
										
										<tr>
										
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Rendita Catastale</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=info.getRendCatas()!=null ? DF.format(info.getRendCatas()) : "-"%></span>
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Quota proprietà</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=info.getQuotaProprieta()!=null ? info.getQuotaProprieta() : "-"%></span>
											</td>										
										</tr>
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Val.Acquisto</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=info.getValAcquisto()!=null ? DF.format(info.getValAcquisto()) : "-"%></span>
											</td>													
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Val.Catastale</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=info.getValCatastale()!=null ? DF.format(info.getValCatastale()) : "-"%></span>
											</td>			
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Val.Inventario</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=info.getValInventariale()!=null ? DF.format(info.getValInventariale()) : "-"%></span>
											</td>		
																
										</tr>
									</table>
								</td>
							</tr>
						</table>						
					</td>
				</tr>				
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTmainLabel">Consistenza</span>
						<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%; margin-top: 5px; margin-bottom: 15px;">
							<tr>
								<td style="text-align: center;">
									<table class="viewExtTable" cellpadding="0" cellspacing="3" style="width: 100%;">
									
										<tr>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tot.Area</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getSupTot()!=null ? DF.format(info.getSupTot()) : "-"%></span>
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Sup. Coperta</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getSupCop()!=null ? DF.format(info.getSupCop()) : "-" %></span>
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tot. Unità Comunali (UIUC)</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getTotUnitaComunali()!=null ? DF.format(info.getTotUnitaComunali()) :"-" %></span>
											</td>
							
									</tr>
								
									<tr>

											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tot.s.l.p.</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getSupTotSlp()!=null ? DF.format(info.getSupTotSlp()) : "-"%></span>
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Sup. Locativa</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=info.getSupLoc()!=null ? DF.format(info.getSupLoc()) : "-" %></span>
											</td>	
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tot. posti auto/box</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getNumBox()!=null ? info.getNumBox() : "-" %></span>
											</td>
									</tr>
								
									<tr>

											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Volume Totale</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getVolumeTot()!=null ? DF.format(info.getVolumeTot()) : "-"%></span>
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Sup. corridoi e servizi uff.</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=info.getSupCoSe()!=null ? DF.format(info.getSupCoSe()) : "-" %></span>
											</td>											
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tot.abitativa</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getTotAbitativa()!=null ? info.getTotAbitativa() : "-" %></span>
											</td>											
											
									</tr>
									<tr>		
																
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Volume Fuori Terra</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getVolumeF()!=null ? DF.format(info.getVolumeF()) : "-"%></span>
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Sup. operativa uff.</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=info.getSupOper()!=null ? DF.format(info.getSupOper()) : "-"%></span>
											</td>		
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Tot. usi diversi</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getTotUsiDiversi()!=null ? info.getTotUsiDiversi() : "-" %></span>
											</td>										
									</tr>
									
									<tr>		
												<td class="TDmainLabel" >
												<span class="TXTmainLabel">Volume Entro Terra</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getVolumeI()!=null ? DF.format(info.getVolumeI()): "-"%></span>
											</td>
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">Sup. commerciale</span>
											</td>
											
											<td class="TDviewTextBox" >
												<span class="TXTviewTextBox"><%=info.getSupCommerciale()!=null ? DF.format(info.getSupCommerciale()) : "-" %></span>
											</td>											
										
									</tr>
									<tr>		
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">n. piani fuori terra</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getNumPianiF()!=null ? info.getNumPianiF() : "-"%></span>
											</td>
		

	
									</tr>
									
									<tr>		
											<td class="TDmainLabel" >
												<span class="TXTmainLabel">n. piani Interrati</span>
											</td>
											
											<td class="TDviewTextBox"  >
												<span class="TXTviewTextBox"><%=info.getNumPianiI()!=null ? info.getNumPianiI() : "-"%></span>
											</td>
									</tr>
									
											
									</table>
								</td>
							</tr>
						</table>						
					</td>
				</tr>
				<%} %>
			</table>
			
			<div class="tabber">
				<%List<DmBBene> lstFigli = bene.getBene().getDmBBenes();%>
					<% if (lstFigli != null && lstFigli.size() > 0) {%>
					<div class="tabbertab">
					 <h2>Beni Collegati</h2>
						
							<table align=left class="extWinTable" style="width: 100%; margin-top: 5px;" cellpadding="0" cellspacing="0">	
								<tr>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Id.</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Bene</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Descrizione</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Note</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle"></span></td>
								</tr>
								<%for (DmBBene ind : lstFigli) {%>
								<tr>			
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=ind.getChiave()%></span>		
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=ind.getDesTipoBene()!=null ? ind.getDesTipoBene() : ""%></span>							
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=ind.getDescrizione()!=null ? ind.getDescrizione() : ""%></span>							
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=ind.getNote()!=null ? ind.getNote() : "" %></span>							
									</td>
									<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData">
										<a href="javascript:vaiDettaglioBene(<%=ind.getId()%>);">Vai a dettaglio</a>
									</span>
									</td>
								</tr>
								<% } %>
							</table>
						</div>
					<% }%>
				
				<% List<DmBIndirizzo> lstInd = bene.getIndirizzi();%>
				<% List<MappaleDTO> lstMap = bene.getMappali();%>
				<%if((lstInd!=null && lstInd.size()>0)||(lstMap!=null && lstMap.size()>0)){ %>
				<div class="tabbertab">
					<h2>Localizzazione</h2>
						
						 <% if (lstInd != null && lstInd.size() > 0) {%>
							<table align=left class="extWinTable" style="width: 100%; margin-top: 5px;" cellpadding="0" cellspacing="0">	
								
								<tr ><td colspan="5">Indirizzi</td></tr>
								<tr>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Via</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Prov.</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Principale</span></td>
								</tr>
								<% for (DmBIndirizzo ind : lstInd) {%>
								<tr>			
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=(ind.getTipoVia()!=null ? ind.getTipoVia()+" " : "")+ind.getDescrVia() %></span>		
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=ind.getCivico()!=null? ind.getCivico() : "-"%></span>							
									</td>
								
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=ind.getDesComune()!=null ? ind.getDesComune(): "-" %></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=ind.getProv()!=null ? ind.getProv(): "-" %></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=ind.getFlgPrincipale()!=null && ind.getFlgPrincipale().intValue()==1 ? "Si" : "No"%></span>							
									</td>
								</tr>
								
								<% } %>
							</table>
							<% }%>
						
							<br/>
						
							<%if (lstMap != null && lstMap.size() > 0) {%>
							<table align=left class="extWinTable" style="width: 100%; margin-top: 5px;" cellpadding="0" cellspacing="0">	
								
								<tr><td colspan="3">Mappali</td></tr>
								<tr>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez.</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
								</tr>
								<% for (MappaleDTO ind : lstMap) { %>
								<tr>			
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=ind.getSezione()!=null ? ind.getSezione() : "-" %></span>							
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=ind.getFoglio()!=null ? ind.getFoglio() : "-"%></span>
									</td>
									<td class="extWinTDData">
										<span class="extWinTXTData"><%=ind.getMappale()!=null ? ind.getMappale() : "-"%></span>
									</td>
									
								</tr>
								
								<% } %>
							</table>
							<%}%>
						</div>
					<%} %>
					<% List<EventoDTO> lstEv = bene.getEventi();
						if (lstEv != null && lstEv.size() > 0) {%>
					<div class="tabbertab">
					 <h2>Eventi</h2>
						
							<table align=left class="extWinTable" style="width: 100%; margin-top: 5px;" cellpadding="0" cellspacing="0">	
								<tr>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Descrizione</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Data</span></td>
								</tr>
								<%for (EventoDTO ind : lstEv) {%>
								<tr>			
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=ind.getDescrizione()!=null ? ind.getDescrizione() :"-"%></span>		
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=ind.getData()!=null ? ind.getDataEventoFormattata() : "-"%></span>							
									</td>
								</tr>
								<% } %>
							</table>
						</div>
					<% }%>
					
					<%List<DmBTerreno> lstTerr = bene.getTerreni();%>
					<% if (lstTerr != null && lstTerr.size() > 0) {%>
					<div class="tabbertab">
					 <h2>Terreni</h2>
						<table align=left class="extWinTable" style="width: 100%; margin-top: 5px;" cellpadding="0" cellspacing="0">	
							<tr>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Sez</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Mappale</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Sup.</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Unità</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Qualità</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Annotazioni</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Fonte</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Uso</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Uso Attuale</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Dati Catastali</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">%</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Note</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipologia</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Finalità</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Dismesso</span></td>
							</tr>
							<%for (DmBTerreno terr : lstTerr ) {%>
							<tr>			
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getCodComune()!=null ? terr.getCodComune() : "-"%></span></td>		
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getSezione()!=null ? terr.getSezione() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getFoglio()!=null ? terr.getFoglio() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getMappale()!=null ? terr.getMappale() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getSuperficie()!=null ? terr.getSuperficie() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getClasse()!=null ? terr.getClasse() : "-"%></span></td>
							
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getUnita()!=null ? terr.getUnita() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getQualita()!=null ? terr.getQualita() : "-"%></span></td>
							
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getAnnotazione()!=null ? terr.getAnnotazione() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getFonte()!=null ? terr.getFonte() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getSub()!=null ? terr.getSub() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getPartita()!=null ? terr.getPartita() : "-"%></span></td>
									
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getCodUtilizzo()!=null ? terr.getCodUtilizzo() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getUsoAttuale()!=null ? terr.getUsoAttuale() : "-"%></span></td>
								
								<td  class="extWinTDData"  style="text-align: center;" >
								<span class="extWinTXTData" >
									<% if((terr.getReddAgr()!=null && terr.getReddAgr().doubleValue()>0) || 
										  (terr.getReddDomin()!=null && terr.getReddDomin().doubleValue()>0) ||
									      (terr.getValAcquisto()!=null && terr.getValAcquisto().doubleValue()>0) ||
									      (terr.getValCatastale()!=null &&  terr.getValCatastale().doubleValue()>0) ||
									      (terr.getValInventario()!=null && terr.getValInventario().doubleValue()>0) || 
									      (terr.getRenditaPresunta()!=null )) { %>
										<a href="#" class="tooltip">
									     Dettaglio
									    <span class="extTooltip">
									    
									      <table  class="extWinTable" width="100%" cellpadding="0" cellspacing="0">
											<%if((terr.getReddAgr()!=null && terr.getReddAgr().doubleValue()>0)){ %>
										    <tr> 	
										    	<td class="extWinTDTitle" ><span class="extWinTXTTitle">Redd.Agr.</span></td>
										    	<td class="extWinTDData"><span class="extWinTXTData"><%=terr.getReddAgr().doubleValue()%></span></td>
										    </tr>
										    <%}%>
										    <%if(terr.getReddDomin()!=null && terr.getReddDomin().doubleValue()>0){ %>
										    <tr> 	
										    	<td class="extWinTDTitle" ><span class="extWinTXTTitle">Redd.Dom.</span></td>
										    	<td class="extWinTDData"><span class="extWinTXTData"><%=terr.getReddDomin().doubleValue()%></span></td>
										    </tr>
										    <%}%>
										    <%if(terr.getValAcquisto()!=null && terr.getValAcquisto().doubleValue()>0){ %>
										    <tr> 	
										    	<td class="extWinTDTitle" ><span class="extWinTXTTitle">Val.Acq.</span></td>
										    	<td class="extWinTDData"><span class="extWinTXTData"><%=terr.getValAcquisto().doubleValue()%></span></td>
										    </tr>
										    <%}%>
										     <%if(terr.getValCatastale()!=null &&  terr.getValCatastale().doubleValue()>0){ %>
										    <tr> 	
										    	<td class="extWinTDTitle" ><span class="extWinTXTTitle">Val.Catasto</span></td>
										    	<td class="extWinTDData"><span class="extWinTXTData"><%=terr.getValCatastale().doubleValue()%></span></td>
										    </tr>
										    <%}%>
										    <%if(terr.getValInventario()!=null && terr.getValInventario().doubleValue()>0){ %>
										    <tr> 	
										    	<td class="extWinTDTitle" ><span class="extWinTXTTitle">Val.Invent.</span></td>
										    	<td class="extWinTDData"><span class="extWinTXTData"><%=terr.getValInventario().doubleValue()%></span></td>
										    </tr>
										    <%}%>
										    <%if(terr.getRenditaPresunta()!=null){ %>
										    <tr> 	
										    	<td class="extWinTDTitle" ><span class="extWinTXTTitle">Rend.Presunta</span></td>
										    	<td class="extWinTDData"><span class="extWinTXTData"><%=terr.getRenditaPresunta()%></span></td>
										    </tr>
										    <%}%>
									    </table>
									    </span>
									</a>
									<% } %>
								</span>
								</td>
								
							    <td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getQuotaPoss()!=null ? terr.getQuotaPoss().divide(new BigDecimal(100)) : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getNote()!=null ? terr.getNote() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getTipologia()!=null ? terr.getTipologia() : "-"%></span></td>

								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%=terr.getFinalita()!=null ? terr.getFinalita() : "-"%></span></td>
								
								<td class="extWinTDData" style="text-align: center;">
									<span class="extWinTXTData"><%= "1".equals(terr.getDismesso()) ? "Si" : "No"%></span></td>	
							</tr>
							<% }%>
						</table>
					</div>
				<% }%>
				
				<% List<TitoloDTO> lstTitoli = bene.getTitoli();
				if (lstTitoli != null && lstTitoli.size() > 0) {%>
					<div class="tabbertab">
					 <h2>Titoli</h2>
						
							<table align=left class="extWinTable" style="width: 100%; margin-top: 5px;" cellpadding="0" cellspacing="0">	
								<tr>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Inventario</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Mod. Acquisizione Bene</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Acq.</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo diritto reale</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Strumento Costitutivo</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Normativa</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">n° doc. prop.</span></td>
								</tr>
								<%for (TitoloDTO titDto : lstTitoli) {
									DmBTitolo tit = titDto.getTitolo();%>
								<tr>			
									
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tit.getDmBBene().getChiave()%></span></td>		
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tit.getTipoAcquisizione()!=null ? tit.getTipoAcquisizione() : "-"%></span></td>
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tit.getAnnoAcquisizione()!=null ? tit.getAnnoAcquisizione() : "-" %></span></td>
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tit.getTipoDirittoReale()!=null ? tit.getTipoDirittoReale() : "-" %></span></td>
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tit.getStrumentoCostruttivo()!=null ? tit.getStrumentoCostruttivo() :"-"%></span></td>
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tit.getNormativaUtilizzata()!=null ? tit.getNormativaUtilizzata() : "-"%></span></td>
									<td class="extWinTDData" style="text-align: center;">
										<span class="extWinTXTData"><%=tit.getDocpropNumero()!=null ? tit.getDocpropNumero() : "-"%></span></td>
								</tr>
								<% } %>
							</table>
						</div>
					<% }%>
					
					
			</div>
			<% if(finder != null){%>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<% }else{%>
				<input type='hidden' name="ACT_PAGE" value="">
			<% }%>
	
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="132">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
	
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>