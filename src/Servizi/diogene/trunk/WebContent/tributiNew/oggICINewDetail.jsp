<%@ page language="java" import="it.escsolution.escwebgis.tributi.bean.*,java.util.ArrayList,java.util.Iterator"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%  OggettiICINew ici = (OggettiICINew)sessione.getAttribute("ICI");
	ArrayList listaSoggetti = (ArrayList)sessione.getAttribute("CONTR_LIST"); 
%>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%  it.escsolution.escwebgis.tributiNew.bean.OggettiICINewFinder finder = null;

	if (sessione.getAttribute("FINDER108") != null){ 
		finder = (it.escsolution.escwebgis.tributiNew.bean.OggettiICINewFinder)sessione.getAttribute("FINDER108");
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
<%@page import="it.escsolution.escwebgis.tributiNew.bean.OggettiICINew"%>
<%@page import="it.escsolution.escwebgis.tributiNew.bean.ContribuentiOggettoNew"%>
<%@page import="it.escsolution.escwebgis.tributiNew.logic.TributiOggettiICINewLogic"%>
<%@page import="it.escsolution.escwebgis.docfa.bean.Docfa"%>
<html>
	<head>
		<title>Tributi Oggetto ICI - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	<script>
		function apriPopupConcessioni(ente,fg,part)
		{
					var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupConcessioni=true&cod_ente='+ente+'&fg='+fg+'&part='+part;
					var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
					finestra.focus();
		}
		
		function apriPopupConcessioniStorico(fg,part)
		{
					var url = '<%= request.getContextPath() %>/StoricoConcessioni?UC=53&ST=2&popup=true&fg='+fg+'&part='+part;
					var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
					finestra.focus();
		}

		function apriPopupTitolaritaF(codFiscale, nome, cognome, dataNascita)
		{

					var url = '<%= request.getContextPath() %>/CatastoIntestatariF?UC=3&ST=5&popup=true&COD_FISCALE='+codFiscale+'&NOME='+nome+'&COGNOME='+cognome+'&DATA_NASCITA='+dataNascita;
					var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
					finestra.focus();
		}

		function apriPopupTitolaritaG(partitaIva)
		{

					var url = '<%= request.getContextPath() %>/CatastoIntestatariG?UC=4&ST=5&popup=true&PARTITA_IVA='+partitaIva;
					var finestra=window.open(url,"_dati","height=600,width=800,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
					finestra.focus();
		}

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
		function dettaglioDocfa(chiave) {
			var pop = window.open('<%= request.getContextPath() %>/Docfa?UC=43&ST=3&OGGETTO_SEL='+chiave+'&EXT=1&NONAV=1');
			pop.focus();
		}
		
		function visualizzaContribuente(chiave){
			wait();
			document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiContribuentiNew";	
			document.mainform.OGGETTO_SEL.value=chiave;	
			document.mainform.ST.value=5;
			document.mainform.UC.value=110;
			document.mainform.EXT.value = 1;
			document.mainform.submit();
		}
	</script>
	
	<body >
	
	<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
		&nbsp;<%=funzionalita%>:&nbsp;Dati Oggetto ICI</span>
	</div>

	&nbsp;
	<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
	<br/>
	<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

	<form name="mainform" action="<%= request.getContextPath() %>/TributiOggettiICINew" target="_parent">

	

	<table style="background-color: white; width: 100%;">
		<tr style="background-color: white;">
			<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
	
				<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
					<tr></tr>
					<tr></tr>
					<tr></tr>					
					
					<tr>
						<td>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Sezione</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getSez()%></span></td>
								</tr>
							</table>
						</td>
						
						<td width=10></td>	
						
						<td>	
							<table class="viewExtTable">
								<tr>			
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Foglio</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getFoglio()%></span></td>
								</tr>
							</table>
						</td>						
					</tr>
					
					<tr></tr>
					<tr></tr>
					<tr></tr>
					
					<tr>
						<td>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Particella</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getNumero()%></span></td>
								</tr>
							</table>
						</td>
						
						<td width=10></td>	
						
						<td>	
							<table class="viewExtTable">
								<tr>			
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Subalterno</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getSub()%></span></td>
								</tr>
							</table>
						</td>						
					</tr>
					
					<tr></tr>
					<tr></tr>
					<tr></tr>
				</table>
	
				&nbsp;
	
				<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
					<tr></tr>
					<tr></tr>
					<tr></tr>
					
					<tr>
						<td>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Anno denuncia</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getYeaDen()%></span></td>
								</tr>
							</table>
						</td>
						
						<td width=10></td>	
						
						<td>	
							<table class="viewExtTable">
								<tr>			
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Num. denuncia</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getNumDen()%></span></td>
								</tr>
							</table>
						</td>						
					</tr>
					
					<tr></tr>
					<tr></tr>
					<tr></tr>
					
					<tr>	
						<td>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Cat. catastale</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getCat()%></span></td>
								</tr>
							</table>
						</td>
						
						<td width=10></td>
						
						<td>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Classe</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getCls()%></span></td>
								</tr>
							</table>
						</td>								
					</tr>
					
					<tr></tr>
					<tr></tr>
					<tr></tr>					
					
					<tr>
						<td colspan=3>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Rendita</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getValImm()%></span></td>
								</tr>
							</table>
						</td>
					</tr>

					<tr></tr>
					<tr></tr>
					<tr></tr>
					
					<tr>
						<td colspan=3>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;"><span class="TXTmainLabel">Abitazione Principale</span></td>
									<td class="TDviewTextBox" style="width:170;"><span class="TXTviewTextBox"><%=ici.getFlgAbiPri3112()%></span></td>
								</tr>
							</table>
						</td>
					</tr>

					<tr></tr>
					<tr></tr>
					<tr></tr>
					
					<tr>
						<td colspan=3>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;" valign="top"><span class="TXTmainLabel">Indirizzo Oggetto ICI</span></td>
									<td class="TDviewTextBox" style="width:356;"><span class="TXTviewTextBox"><%=ici.getDesInd()%></span></td>
								</tr>
							</table>
						</td>
					</tr>

					<tr></tr>
					<tr></tr>
					<tr></tr>
				</table>
	
				&nbsp;
	
				<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
					<tr></tr>
					<tr></tr>
					<tr></tr>
					
					<tr>
						<td colspan=3>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;" valign="top"><span class="TXTmainLabel">Indirizzo Catastale</span></td>
									<td class="TDviewTextBox" style="width:356;"><span class="TXTviewTextBox"><%=ici.getDesIndCatastale()%></span></td>
								
								</tr>
							</table>
						</td>
					</tr>

					<tr></tr>
					<tr></tr>
					<tr></tr>
					
					<tr>
						<td colspan=3>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;" valign="top"><span class="TXTmainLabel">Indirizzo Viario</span></td>
									<td class="TDviewTextBox" style="width:356;"><span class="TXTviewTextBox"><%=ici.getDesIndViario()%></span></td>
								</tr>
							</table>
						</td>
					</tr>

					<tr></tr>
					<tr></tr>
					<tr></tr>
					
					<tr>
						<td colspan=3>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;" valign="top"><span class="TXTmainLabel">Localizza in Mappa</span></td>
									<td onclick="zoomInMappaParticelle('<%=ici.getCodEnte()%>','<%=ici.getFoglio()%>','<%=ici.getNumero()%>');" class="TXTmainLabel" style="width:170;cursor: pointer;"><span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span></td>
									<td class="TXTmainLabel" style="width:178;cursor: pointer;">
										<a href="javascript:apriPopupVirtualH(<%=ici.getLatitudine()==null?0:ici.getLatitudine()%>,<%=ici.getLongitudine()==null?0:ici.getLongitudine()%>);">
											<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
										</a>				
									</td>
									<td class="TXTmainLabel" style="width:178;cursor: pointer;">
										<a href="javascript:apriPopupStreetview(<%=ici.getLatitudine()==null?0:ici.getLatitudine()%>,<%=ici.getLongitudine()==null?0:ici.getLongitudine()%>);">
											<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
										</a>				
									</td>
								</tr>
							</table>
						</td>
					</tr>

					<tr></tr>
					<tr></tr>
					<tr></tr>
					
					<tr>
						<td colspan=3>
							<table class="viewExtTable">
								<tr>
									<td class="TDmainLabel"  style="width:170;" valign="top"><span class="TXTmainLabel">Concessioni Edilizie sulla particella</span></td>
									<td class="TXTmainLabel" style="width:529;">
										<span class="TDmainLabel">
											<!-- il primo link non è più visibile -->
											<a style="text-decoration: none; display: none;" href="javascript:apriPopupConcessioni('<%=ici.getCodEnte()%>','<%=ici.getFoglio().equals("-") ? "-1" : ici.getFoglio()%>','<%=ici.getNumero().equals("-") ? "-1" : ici.getNumero()%>');">
												<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
											</a>
											<a style="text-decoration: none;" href="javascript:apriPopupConcessioniStorico('<%=ici.getFoglio().equals("-") ? "-1" : ici.getFoglio()%>','<%=ici.getNumero().equals("-") ? "-1" : ici.getNumero()%>');">
												<img src="<%=request.getContextPath()%>/images/info.gif" border="0" >
											</a>
										</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
			
					<tr></tr>
					<tr></tr>
					<tr></tr>
				</table>
				
				<!-- FINE solo dettaglio -->

	</td>
		
		</tr>
	</table>
				
	<div class="tabber">
					
			<% if (listaSoggetti != null && listaSoggetti.size() > 0) {%>
				<div class="tabbertab">
						
				<h2>Storico Contribuenti Oggetto <%=ici.getFoglio() + " / " + ici.getNumero() + " / " + ici.getSub() %></h2>

				
				<table align=left width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
					<tr>&nbsp;</tr>
					<tr class="extWinTXTTitle">
							Storico Contribuenti Oggetto <%=ici.getFoglio() + " / " + ici.getNumero() + " / " + ici.getSub() %>
					</tr>
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Nominativo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno denuncia</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno riferimento</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero denuncia</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Numero riga</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Mesi possesso</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Quota possesso</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Poss. al 31/12</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo den.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolarità Catasto</span></td>
					</tr>
				<%
					
						Iterator iter = listaSoggetti.iterator();
						int contatore = 1;
						int annoIci = 0;
						int maxAnnoIci = 0;
						while (iter.hasNext())
						{
							ContribuentiOggettoNew sogg = (ContribuentiOggettoNew) iter.next();
						%>
						<% 
							
							int annoIciAttuale = sogg.getYeaDen() == null || sogg.getYeaDen().equals("-") ? 0 : Integer.parseInt(sogg.getYeaDen());
							
							if (annoIci != annoIciAttuale && contatore != 1) {
						%>  			
							    <tr>
							    		<td colspan="13">
										<span class="extWinTXTData">&nbsp;</span>
										</td>
							    </tr>
							
					  	<% 	
					  		}
					  		
							boolean soggAttuale = false;
							if(contatore == 1){
								maxAnnoIci = annoIciAttuale;
								soggAttuale = sogg.getFlgPos3112().equalsIgnoreCase("SI") ? true : false;
							}else
								soggAttuale = (maxAnnoIci == annoIciAttuale && sogg.getFlgPos3112().equalsIgnoreCase("SI"))? true : false;
					  			
							String piva= sogg.getPartitaIva();
						%>
				
					<tr <%if (soggAttuale) {%> style = "color:green; font-weight:bold;" <%} %>>
					    <td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getTitSogg() %></span>
						</td>
						<td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getNominativo() %></span>
						</td>
					    <td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getYeaDen() %></span>
						</td>
					    <td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getYeaRif() %></span>
						</td>
					    <td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getNumDen() %></span>
						</td>
					    <td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getNumRiga() %></span>
						</td>
					    <td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getMesiPos() %></span>
						</td>
						<td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getPrcPoss() %></span>
						</td>
						<td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getValImm() %></span>
						</td>
						<td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getFlgPos3112() %></span>
						</td>
						<td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><%= sogg.getTipDen() %></span>
						</td>
						<td class="extWinTDData" onclick="visualizzaContribuente('<%=sogg.getChiave()%>')" class="extWinTDTitle" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
							<span class="extWinTXTData"><center><%= sogg.getProvenienza() %></center></span>
						</td>
						<td class="extWinTDData" class="extWinTDTitle" >
			
							 <span class="extWinTXTData">
							<% if (piva == null || piva.equals("") || piva.equals("00000000000")){%>
							<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaF('<%=sogg.getCodiceFiscale()%>','<%=sogg.getNome().replace("'", "\\'")%>','<%=sogg.getCognome().replace("'", "\\'")%>','<%=sogg.getDataNascita()%>');">
								Vedi
							</a>
							<%} else { %>
							<a style="text-decoration: none;" href="javascript:apriPopupTitolaritaG('<%=sogg.getPartitaIva()%>');">
								Vedi
							</a>
							<%} %>
							</span>  
							
						</td>
					</tr>
					<% 
						contatore++;
						annoIci = annoIciAttuale;
				  	} 
				  	%>
					
				
				</table>
				
				</div>
				
				<% } %>
				
				<% 
				ArrayList ldocfa = (ArrayList)sessione.getAttribute(TributiOggettiICINewLogic.ICI_DOCFA_COLLEGATI);
				
				if (ldocfa != null && ldocfa.size() > 0) { %>
				
				<div class="tabbertab">
						
						<h2>Dati censuari DOCFA collegati</h2>
						<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0" align=center>
							<tr>&nbsp;</tr>
							<tr class="extWinTXTTitle">
									Dati censuari DOCFA collegati
							</tr>
							<tr>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Fornitura</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
								<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
							</tr>
							<% 
							Iterator it = ldocfa.iterator();			
							while(it.hasNext())
							{
								Docfa docfa = (Docfa)it.next();
							%>
							<tr>
								<td class="extWinTDData" ><span class="extWinTXTData"><a href="javascript:dettaglioDocfa('<%=docfa.getChiave()%>');"><%=docfa.getProtocollo()%></a></span></td>
								<td class="extWinTDData" ><span class="extWinTXTData"><%=docfa.getFornitura()%></span></td>
								<td class="extWinTDData" ><span class="extWinTXTData"><%=docfa.getCategoria()%></span></td>
								<td class="extWinTDData" ><span class="extWinTXTData"><%=docfa.getClasse()%></span></td>
								<td class="extWinTDData" ><span class="extWinTXTData"><%=docfa.getRendita()%></span></td>
							</tr>
							<%}%>
						</table>
						</div>
				<% }%>

	</div>	
	
	<input type='hidden' name="OGGETTO_SEL" value="">
		
	<% if(finder != null){%>
		<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
	<% }else{%>
		<input type='hidden' name="ACT_PAGE" value="">
	<% }%>

	<input type='hidden' name="AZIONE" value="">
	<input type='hidden' name="ST" value="">
	<input type='hidden' name="UC" value="108">
	<input type='hidden' name="EXT" value="">
	<input type='hidden' name="BACK" value="">
	<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

	</form>


<div id="wait" class="cursorWait" />
</body>
</html>