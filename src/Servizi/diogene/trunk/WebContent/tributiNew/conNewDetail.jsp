<%@ page language="java" import="it.escsolution.escwebgis.tributi.bean.*, 
	java.util.ArrayList,java.util.Iterator, it.webred.ct.data.access.basic.imu.dto.*, 
	java.util.*, java.math.*,it.escsolution.escwebgis.imu.bean.*, it.escsolution.escwebgis.imu.logic.*,
	it.webred.ct.data.access.basic.anagrafe.dto.DatiAnagrafeDTO, 
	java.text.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true); 
	SaldoImuConsulenzaDTO cons = (SaldoImuConsulenzaDTO)sessione.getAttribute(ConsulenzaImuLogic.IMU_CONS);    
	ArrayList contr = (ArrayList)sessione.getAttribute("CONTR");
	int contrSize=0;
	if (contr != null)
		contrSize=contr.size();
	ArrayList ICIList = (ArrayList)sessione.getAttribute("ICI_LIST");
	ArrayList TARSUList = (ArrayList)sessione.getAttribute("TARSU_LIST"); 
%>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.tributiNew.bean.ContribuentiNewFinder finder = null;

	if (sessione.getAttribute("FINDER110") != null){ 
		finder = (it.escsolution.escwebgis.tributiNew.bean.ContribuentiNewFinder)sessione.getAttribute("FINDER110");
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
<%@page import="it.escsolution.escwebgis.tributiNew.bean.SoggettiTributiNew"%>

<%@page import="it.escsolution.escwebgis.tributiNew.bean.OggettiICINew"%>
<%@page import="it.escsolution.escwebgis.tributiNew.bean.OggettiTARSUNew"%>
<html>
	<head>
		<title>Tributi Contribuenti - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	<script>
		function mettiST(){
			document.mainform.ST.value = 3;
		}

		function chgtr(row,flg) {
			return;
			if (flg==1)
				{
				document.all("r"+row).style.backgroundColor = "#d7d7d7";
				}
			else
				{
				document.all("r"+row).style.backgroundColor = "";
				}
		}
		
		function visualizzaOggettoICI(chiave){
			wait();
			document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiOggettiICINew";
			document.mainform.OGGETTO_SEL.value=chiave;	
			document.mainform.ST.value=5;
			document.mainform.UC.value=108;
			document.mainform.EXT.value = 1;
			document.mainform.submit();
		}
		
		function visualizzaOggettoTARSU(chiave){
			wait();
			document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiOggettiTARSUNew";
			document.mainform.OGGETTO_SEL.value=chiave;	
			document.mainform.ST.value=5;
			document.mainform.UC.value=109;
			document.mainform.EXT.value = 1;
			document.mainform.submit();
		}
		
		function selTab(idx, size) {
			for (i = 0; i < size; i++) {
				if (i == idx) {
					document.getElementById("tabTrib" + i).style.color = '#000099';
					document.getElementById("tabTrib" + i).style.backgroundColor = '#e8f0ff';
					document.getElementById("panTrib" + i).style.display = '';
				} else {
					document.getElementById("tabTrib" + i).style.color = '#606060';
					document.getElementById("tabTrib" + i).style.backgroundColor = '#C0C0C0';
					document.getElementById("panTrib" + i).style.display = 'none';
				}
			}
		}

		function apriPopupTitolaritaF(codFiscale, nome, cognome, dataNascita)
		{

					var url = '<%= request.getContextPath() %>/CatastoIntestatariF?ST=5&UC=3&popup=true&COD_FISCALE='+codFiscale+'&NOME='+nome+'&COGNOME='+cognome+'&DATA_NASCITA='+dataNascita;
					var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
					finestra.focus();
		}

		function apriPopupTitolaritaG(partitaIva)
		{

					var url = '<%= request.getContextPath() %>/CatastoIntestatariG?ST=5&UC=4&popup=true&PARTITA_IVA='+partitaIva;
					var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
					finestra.focus();
		}
	</script>
	
	<body >
	
	<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
		&nbsp;<%=funzionalita%>:&nbsp;Dati Contribuente</span>
	</div>

	&nbsp;
	<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
	<br/>
	<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

	<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiContribuentiNew" target="_parent">

	

	<table style="background-color: white; width: 100%;">
		<tr style="background-color: white;">
			<td style="background-color: white; vertical-align: top; text-align: left; width: 51%;">
			<div class="tabber">	
				<% if (contr != null && contr.size() > 0) { %>
					
					<% for (int i = 0; i < contr.size(); i++) {
						SoggettiTributiNew datoSoggetto = (SoggettiTributiNew)contr.get(i);
				%>
					
				<div class="tabbertab">
													
							<h2>Dati  <%=datoSoggetto.getTributo()%></h2>
							
							<div style=float:left>
							<table align=left cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 85%;">
								<tr>&nbsp;</tr>
								<tr class="extWinTXTTitle">
										Dati anagrafici <%=datoSoggetto.getTributo()%>
								</tr>
								<tr>
									<td style="text-align: center;">
										<table class="viewExtTable" cellpadding="0" cellspacing="2" style="width: 95%;">
											<tr>
												<td class="TDmainLabel" style="width:20%;">
													<span class="TXTmainLabel">Codice Contribuente</span>
												</td>
												
												<td class="TDviewTextBox" style="width:24%;">
													<span class="TXTviewTextBox"><%=datoSoggetto.getIdOrig()%></span>
												</td>
												
												<td style="width:4%"></td>
												
												<td class="TDmainLabel" style="width:20%;">
													<span class="TXTmainLabel">Tipo Soggetto</span>
												</td>
												
												<td class="TDviewTextBox" style="width:24%;">
													<span class="TXTviewTextBox"><%=datoSoggetto.getTipSogg()%></span>
												</td>					
											</tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Codice Fiscale</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=datoSoggetto.getCodFisc()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Partita IVA</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=datoSoggetto.getPartIva()%></span>
												</td>					
											</tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Cognome</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=datoSoggetto.getCogDenom()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Nome</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=datoSoggetto.getNome()%></span>
												</td>					
											</tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Denominazione</span>
												</td>
												
												<td class="TDviewTextBox" colspan="4">
													<span class="TXTviewTextBox"><%=datoSoggetto.getDenominazione()%></span>
												</td>					
											</tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Sesso</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=datoSoggetto.getSesso()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Data Nascita</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=datoSoggetto.getDtNsc()%></span>
												</td>					
											</tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Comune Nascita</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=datoSoggetto.getDesComNsc()%></span>
												</td>
												
												<td></td>
												
												<td class="TDmainLabel">
													<span class="TXTmainLabel">Provenienza</span>
												</td>
												
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox"><%=datoSoggetto.getProvenienza()%></span>
												</td>					
											</tr>
											<tr></tr>
											<tr></tr>
											<tr></tr>
											<tr></tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel" colspan="2">
													<span class="TXTmainLabel">Indirizzo dichiarato</span>
												</td>
												
												<td class="TDviewTextBox" colspan="3">
													<span class="TXTviewTextBox"><%=datoSoggetto.getDesIndRes()+(datoSoggetto.getDesComRes()!=null ? " - "+datoSoggetto.getDesComRes() : "")%></span>
												</td>					
											</tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel" colspan="2">
													<span class="TXTmainLabel">Indirizzo residenza anagrafica</span>
												</td>
												
												<td class="TDviewTextBox" colspan="3">
													<span class="TXTviewTextBox"><%=datoSoggetto.getIndResAna()%></span>
												</td>					
											</tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel" colspan="2">
													<span class="TXTmainLabel">Indirizzo SIATEL</span>
												</td>
												
												<td class="TDviewTextBox" colspan="3">
													<span class="TXTviewTextBox"><%=datoSoggetto.getIndSiatel()%></span>
												</td>					
											</tr>
											<tr></tr>
											<tr>
												<td class="TDmainLabel" colspan="5">
													<table align=center cellpadding="0" cellspacing="0" style="width: 100%">
														<tr>
															<td class="TDmainLabel" style="width: 10%">
																<span class="TXTmainLabel">Scala</span>
															</td>
															
															<td class="TDviewTextBox" style="width: 21%">
																<span class="TXTviewTextBox"><%=datoSoggetto.getScalaRes()%></span>
															</td>
															
															<td style="width:7%"></td>
															
															<td class="TDmainLabel" style="width: 10%">
																<span class="TXTmainLabel">Piano</span>
															</td>
															
															<td class="TDviewTextBox" style="width: 20%">
																<span class="TXTviewTextBox"><%=datoSoggetto.getPianoRes()%></span>
															</td>
															
															<td style="width:2%"></td>
															
															<td class="TDmainLabel" style="width: 10%">
																<span class="TXTmainLabel">Interno</span>
															</td>
															
															<td class="TDviewTextBox" style="width: 20%">
																<span class="TXTviewTextBox"><%=datoSoggetto.getInternoRes()%></span>
															</td>
														</tr>
													</table>
												</td>					
											</tr>
											<tr>
												<td class="TDmainLabel" colspan="2">
													<span class="TXTmainLabel">Titolarità Catasto</span>
												</td>
												
												<td class="TDviewTextBox" colspan="3">
													<span class="TXTviewTextBox">
													<% if (datoSoggetto.getPartIva()  == null || datoSoggetto.getPartIva().equals("") ||  datoSoggetto.getPartIva().equals("-")){%>
													<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaF('<%=datoSoggetto.getCodFisc() %>','<%=datoSoggetto.getNome().replace("'", "\\'")%>','<%=datoSoggetto.getCogDenom().replace("'", "\\'")%>','<%=datoSoggetto.getDtNsc()%>');">
														<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
													</a>
													<%} else { %>
													<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaG('<%=datoSoggetto.getPartIva()%>');">
														<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
													</a>
													<%} %>
													</span>
												</td>					
											</tr>
											<tr></tr>
										</table>
									</td>
								</tr>
							</table>
							
						
					</div>
				
				<br>
			

					
		<% if (datoSoggetto.getTributo()!= null && datoSoggetto.getTributo().equals("ICI") ){ %>		
			<% if (ICIList != null && ICIList.size() > 0)
			{ %>
	
					<div style=float:left>
					<table align=left width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
						<tr>&nbsp;</tr>
								<tr class="extWinTXTTitle">
										Lista Oggetti ICI
								</tr>
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Denuncia</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero Denuncia</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Poss. al<br/>31/12</span></td>
						</tr>
					<%
						Iterator iter = ICIList.iterator();
						int contatore = 1;
						while (iter.hasNext())
						{
							OggettiICINew ici = (OggettiICINew) iter.next();
						%>
				
						<tr onclick="visualizzaOggettoICI('<%=ici.getChiave()%>')" <%if (ici.isEvidenzia()) {%> style = "color:green; font-weight:bold;" <%} %>>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getSez() %></span>
							</td>
							<td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getFoglio() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getNumero() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getSub() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getDesInd() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getCat() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getProvenienza() %></span>
							</td>
							<td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getYeaDen() %></span>
							</td>
							<td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getNumDen() %></span>
							</td>
							<td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getValImm() %></span>
							</td>
							<td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= ici.getFlgPos3112() %></span>
							</td>
						</tr>
					<% 
						contatore++;
				  	} 
				  	%>					
					</table>
					</div>
				<%	
					}
				}
				%>
			
			<% if (datoSoggetto.getTributo()!= null && datoSoggetto.getTributo().equals("TARSU") ){ %>	
		<%
			if (TARSUList != null && TARSUList.size() > 0)
			{
		%>
				<div style=float:left>
					<table align=left width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
						<tr>&nbsp;</tr>
						<tr class="extWinTXTTitle">
								Lista Oggetti TARSU
						</tr>
						<tr>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Sup. totale</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Data inizio</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Data fine</span></td>
							<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
						</tr>
					<%
						Iterator iter = TARSUList.iterator();
						int contatore = 1;
						while (iter.hasNext())
						{
							OggettiTARSUNew tarsu = (OggettiTARSUNew) iter.next();
						%>
				
						<tr onclick="visualizzaOggettoTARSU('<%=tarsu.getChiave()%>')" <%if (tarsu.isEvidenzia()) {%> style = "color:green; font-weight:bold;" <%} %>>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= tarsu.getSez() %></span>
							</td>
							<td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= tarsu.getFoglio() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= tarsu.getNumero() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= tarsu.getSub() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= tarsu.getDesInd() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= tarsu.getSupTot() %></span>
							</td>
							<td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= tarsu.getDatIni() %></span>
							</td>
							<td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= tarsu.getDatFin() %></span>
							</td>
						    <td class="extWinTDData" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
								<span class="extWinTXTData"><%= tarsu.getProvenienza() %></span>
							</td>
						</tr>
					<% 
						contatore++;
				  	} 
				  	%>					
					</table>
					
					</div>
				<%	
					}
				}
				%>
				
				</div>
				<% 	} %>
				<% 	} %>
				
				<%if(cons!=null){ %>
				<div class="tabbertab">
													
					<h2>Dati Consulenza Imu</h2>
											
					<jsp:include page="../saldoImu/saldoImuConsulenza.jsp"></jsp:include>

				</div>
				<%} %>
			
				</div>
				</td>
		
		</tr>
	</table>	
			
		
	<% if(finder != null){%>
		<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
	<% }else{%>
		<input type='hidden' name="ACT_PAGE" value="">
	<% }%>

	<input type='hidden' name="OGGETTO_SEL" value="">
	<input type='hidden' name="AZIONE" value="">
	<input type='hidden' name="ST" value="">
	<input type='hidden' name="UC" value="110">
	<input type='hidden' name="EXT" value="">
	<input type='hidden' name="BACK" value="">
	<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

	</form>


<div id="wait" class="cursorWait" />
</body>
</html>