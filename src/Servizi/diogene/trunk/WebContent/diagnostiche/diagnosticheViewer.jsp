<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ page language="java" import="it.escsolution.escwebgis.anagrafe.bean.*"%>
<% HttpSession sessione = request.getSession(true); %>
<% ArrayList intestazioni = (ArrayList)request.getAttribute(DiaBridge.DIA_INTESTAZIONI);%>
<% ArrayList dati = (ArrayList)request.getAttribute(DiaBridge.DIA_DATI);%>

<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="it.webred.ct.diagnostics.service.data.dto.DiaValueKeysDTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="it.escsolution.escwebgis.diagnostiche.util.DiaBridge"%><html>

	<head>
		<title>Visualizzatore diagnostiche</title>
		<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
	</head>
	
	<script>
		function apriChiudi(id) {
			if (document.getElementById("datiDia" + id).style.display == "none") {
				document.getElementById("datiDia" + id).style.display = "";
				document.getElementById("testataDia" + id).title = "Clicca per chiudere la visualizzazione dei dati della diagnostica";
			} else if (document.getElementById("datiDia" + id).style.display == "") {
				document.getElementById("datiDia" + id).style.display = "none";
				document.getElementById("testataDia" + id).title = "Clicca per visualizzare i dati della diagnostica";
			}			
		}
	</script>

	<body>
		<div align="center" class="extWinTDTitle">
			<span class="extWinTXTTitle">
				Dati Diagnostiche
			</span>
		</div>
		<% EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null); 
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		%>
		<% if (intestazioni != null && intestazioni.size() > 0) { 
			for (int i = 0; i < intestazioni.size(); i++) {
				HashMap intestazione = (HashMap)intestazioni.get(i);%>				
				<% if (dati != null && dati.size() > 0) { 
					HashMap dato = (HashMap)dati.get(i);
					if (dato != null && dato.size() > 0) {%>
						<br>
						<table class="editExtTable" style="width: 100%;">
							<tr>
								<td class="TDmainLabel" style="width: 10%; text-align: right; padding: 5px 5px 5px 0px;">
									<span class="TXTmainLabel">
										Fonte dati
									</span>
								</td>
								<td class="TDviewTextBox" style="width: 15%;">
									<span class="TXTviewTextBox">
										<%=(String)intestazione.get(DiaBridge.FONTE) %>
									</span>
								</td>
								<td class="TDmainLabel" style="width: 20%; text-align: right; padding: 5px 5px 5px 0px;">
									<span class="TXTmainLabel">
										Tipo informazione
									</span>
								</td>
								<td class="TDviewTextBox" style="width: 15%;">
									<span class="TXTviewTextBox">
										<%=(String)intestazione.get(DiaBridge.TIPO) %>
									</span>
								</td>
								<td class="TDmainLabel" style="width: 18%; text-align: right; padding: 5px 5px 5px 0px;">
									<span class="TXTmainLabel">
										<%=(String)intestazione.get(DiaBridge.BEAN_PROPERTY) %>
									</span>
								</td>
								<td class="TDviewTextBox" style="width: 22%;">
									<span class="TXTviewTextBox">
										<%=(String)intestazione.get(DiaBridge.VALORE) %>
									</span>
								</td>
							</tr>
						</table>
						<%Iterator it = dato.keySet().iterator();
						int idx = 0;
						while(it.hasNext()) {
							Long key = (Long)it.next();
							List values = (List)dato.get(key);%>
							<table class="editExtTable" style="width: 100%; border-width: 0px; border-style: none; border-spacing: 0px;
							padding: 15px 10% 0px 10%;">
								<tr style="cursor: pointer;"
									id="testataDia<%=i%>_<%=idx%>"
									title="Clicca per visualizzare i dati della diagnostica"
									onclick="apriChiudi('<%=i%>_<%=idx%>');">
									<td class="TDviewTextBox" style="width: 100%; padding: 5px; text-align: center;">
										<span class="TXTviewTextBox" style="color: #7F0055;">
											<%=DiaBridge.getDiaTipoInfoDesc(eu.getUtente().getCurrentEnte(),eu.getUtente().getUsername(), key.longValue())%>
										</span>
									</td>
								</tr>
							</table>
							<table style="width: 100%; display: none;" id="datiDia<%=i%>_<%=idx%>">
								<tr>
									<%if (values != null && values.size() > 0) {%>
										<td style="width: 100%; padding: 0px 10% 0px 10%;">
										<%for (Object obj : values) {
											DiaValueKeysDTO[] record = (DiaValueKeysDTO[])obj; %>
											<table class="editExtTable" style="width: 100%;">								
											<%for (DiaValueKeysDTO value : record) {%>
												<tr>
													<td class="TDmainLabel" style="width: 30%; text-align: right; padding: 5px 5px 5px 0px;">
														<span class="TXTmainLabel">
															<%=value.getPropertyName()%>
														</span>
													</td>
													<td class="TDviewTextBox" style="width: 70%;">
														<span class="TXTviewTextBox">
															<% Object valore = value.getValue();
															String str = "-";
											        		if (valore != null) {
											        			try {
											        				if (valore instanceof java.util.Date) {
													        			str = sdf.format((java.util.Date)valore);
													        		} else if (valore instanceof Integer) {
													        			str = "" + ((Integer)valore).intValue();
													        		} else if (valore instanceof Double) {
													        			str = df.format(((Double)valore).doubleValue());
													        		} else if (valore instanceof BigDecimal) {
													        			str = df.format(((BigDecimal)valore).doubleValue());
													        		//eventuali altri tipi
													        		} else {
													        			str = valore.toString();
													        		}
											        			} catch (Exception e) {}							        			
											        		}%>
															<%=str%>
														</span>
													</td>
												</tr>													
											<%	} %>
											</table>
									<%	} %>
										</td>
								<%	} %>						
								</tr>
							</table>
							<%idx++;
						}
					}
				}
			}
		}%>
	</body>
	
</html>