<%@page import="it.webred.ct.data.model.catasto.Siticivi"%>
<%@page import="it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO"%>
<%@page import="java.util.List"%>
<%@page import="it.webred.ct.data.model.catasto.Sitidstr"%>
<%@page import="it.escsolution.escwebgis.fascicolo.logic.FascFabbAppLogic"%>
<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<% 
String mode = request.getParameter("mode") == null ? "" : request.getParameter("mode");

HttpSession sessione = request.getSession();
String nomeVia = (String)sessione.getAttribute(FascFabbAppLogic.RICERCA_NOME_VIA);
if (nomeVia == null) nomeVia = "";

@SuppressWarnings("unchecked")
List<Sitidstr> listaVie = (List<Sitidstr>)sessione.getAttribute(FascFabbAppLogic.RICERCA_LISTA_VIE);

String idVia = (String)sessione.getAttribute(FascFabbAppLogic.RICERCA_ID_VIA);
if (idVia == null) idVia = "";

@SuppressWarnings("unchecked")
List<Siticivi> listaCivici = (List<Siticivi>)sessione.getAttribute(FascFabbAppLogic.RICERCA_LISTA_CIVICI);

String idCivico = (String)sessione.getAttribute(FascFabbAppLogic.RICERCA_ID_CIVICO);
if (idCivico == null) idCivico = "";

@SuppressWarnings("unchecked")
List<ParticellaKeyDTO> listaFp = (List<ParticellaKeyDTO>)sessione.getAttribute(FascFabbAppLogic.RICERCA_LISTA_FP);
%>

<html>

	<head>	
		<title>Ricerca per via</title>
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">		
		<script src='<%= request.getContextPath() %>/ewg/Scripts/dynamic.js' language='JavaScript'></script>
		
		<script>

			function ricerca() {
				if (document.mainform.nomeVia.value == '') {
					alert("Inserire il nome, o parte del nome, della via da ricercare");
					return false;
				}
				document.mainform.mode.value = "ricerca";
				document.mainform.submit();
			}

			function selVia() {
				document.mainform.mode.value = "selVia";
				document.mainform.submit();
			}

			function selCivico() {
				document.mainform.mode.value = "selCivico";
				document.mainform.submit();
			}

			function seleziona(foglio, particella) {
				opener.mainform.VALORE_1.value = foglio;
				opener.mainform.VALORE_2.value = particella;
				window.close();
				return false;
			}
		
		</script>	
	</head>
	
	<body>
		<div align="center" class="extWinTDTitle">
			<span class="extWinTXTTitle">
				Ricerca via
			</span>
		</div>
		
		<br/>

		<form name="mainform" action="<%=request.getContextPath()%>/FascFabbApp" >
		
			<table align="center" class="searchTable" style="width: 95%">
				<tr>
					<td colspan="3" class="extWinTDTitle" style="text-align: left">
						<span class="extWinTXTTitle">
							Criterio ricerca
						</span>
					</td>
				</tr>
			
				<tr>
					<td class="TDmainLabel" style="width: 30%; text-align: right; padding: 10px">
						<span class="TXTmainLabel">
							Nome via da ricercare
						</span>
					</td>
					<td class="TDviewTextBox" style="width: 35%; padding: 10px">
						<input type="text" name="nomeVia" class="TXTviewTextBox" style="width: 100%" value="<%= nomeVia %>">
					</td>
					<td class="TDmainLabel" style="width: 35%; padding: 10px">
						<span class="TXTmainLabel">
							<a class="TXTmainLabel" style="cursor: pointer; text-decoration: underline;" onClick="ricerca()">
								Ricerca
							</a>
						</span>
					</td>				
				</tr>
			</table>
			
			<% if (!mode.equals("")) { %>
			
				<br />
				<table align="center" class="searchTable" style="width: 95%">
					<tr>
						<td class="extWinTDTitle" style="text-align: left">
							<span class="extWinTXTTitle">
								Valori trovati
							</span>
						</td>
					</tr>
					<tr>
						<td style="padding: 10px">
							<table style="width: 100%" class="searchTable">
								<tr>
									<td class="TDmainLabel" style="width: 25%; text-align: right; padding: 10px">
										<span class="TXTmainLabel">
											Seleziona via
										</span>
									</td>
									<td class="TDviewTextBox" style="width: 75%; padding: 10px">
										<select name="idVia" class="INPDBComboBox" style="width: 60%" onchange="selVia()">
											<option value="-1">&nbsp;</option>
											<% if (listaVie != null) {
												for (Sitidstr via : listaVie) {
													String id = via.getPkidStra().toString();			
													String label = via.getPrefisso() + " " + via.getNome();
													if (id.equals(idVia)) { %>
														<option value="<%= id %>" selected><%= label %></option>
													<% } else {%>
														<option value="<%= id %>"><%= label %></option>
													<% }
												}
											} %>								
										</select>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					
					<% if (mode.equals("selVia") || mode.equals("selCivico")) { %>
					
						<tr>
							<td style="padding: 0px 10px 10px 10px">
								<table style="width: 100%" class="searchTable">
									<tr>
										<td class="TDmainLabel" style="width: 25%; text-align: right; padding: 10px">
											<span class="TXTmainLabel">
												Seleziona civico
											</span>
										</td>
										<td class="TDviewTextBox" style="width: 75%; padding: 10px">
											<select name="idCivico" class="INPDBComboBox" style="width: 60%" onchange="selCivico()">
												<option value="-1">&nbsp;</option>
												<% if (listaCivici != null) {
													for (Siticivi civico : listaCivici) {
														String id = civico.getPkidCivi().toString();		
														String label = civico.getCivico();
														if (id.equals(idCivico)) { %>
															<option value="<%= id %>" selected><%= label %></option>
														<% } else {%>
															<option value="<%= id %>"><%= label %></option>
														<% }
													}
												} %>						
											</select>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						
					<% } %>
					
					<% if (mode.equals("selCivico")) { %>
					
						<tr>
							<td style="padding: 0px 10px 10px 10px">
								<table style="width: 100%" class="searchTable">
									<tr>
										<td class="extWinTDTitle" style="text-align: left">
											<span class="extWinTXTTitle">
												Lista particelle
											</span>
										</td>
									</tr>
									<% if (listaFp != null) { %>
										<tr>
											<td style="padding: 10px">
												<table align="center" style="width: 60%" class="searchTable">
													<tr>
														<td class="extWinTDTitle" style="width: 50%">
															<span class="extWinTXTTitle">
																Foglio
															</span>
														</td>
														<td class="extWinTDTitle" style="width: 50%">
															<span class="extWinTXTTitle">
																Particella
															</span>
														</td>
													</tr>
													<% for (ParticellaKeyDTO fp : listaFp) { %>
														<tr>
															<td onclick="seleziona('<%=fp.getFoglio()%>', '<%=fp.getParticella()%>')" class="extWinTDData"  style='cursor: pointer;'>
																<span class="extWinTXTData">
																	<%=fp.getFoglio()%>
																</span>
															</td>
															<td onclick="seleziona('<%=fp.getFoglio()%>', '<%=fp.getParticella()%>')" class="extWinTDData"  style='cursor: pointer;'>
																<span class="extWinTXTData">
																	<%=fp.getParticella()%>
																</span>
															</td>
														</tr>
													<% } %>
												</table>
											</td>
										</tr>
									<% } %>
								</table>
							</td>
						</tr>
						
					<% } %>
					
				</table>
				
			<% } %>
			
			<br />			
			<div align="center">
				<span class="TXTmainLabel">
					<a class="TXTmainLabel" style="cursor: pointer; text-decoration: underline;" href="javascript:window.close();">Chiudi</a>
				</span>
			</div>
			
			<input type="hidden" name="cercaPerVia" value="true">
			<input type="hidden" name="mode" value="<%= mode %>">
		
		</form>
	</body>
	
</html>