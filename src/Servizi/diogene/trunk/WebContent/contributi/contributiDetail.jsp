<%@page import="it.escsolution.escwebgis.contributi.bean.ContributiSib"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.escsolution.escwebgis.contributi.servlet.ContributiServlet"%>
<%@page import="it.escsolution.escwebgis.contributi.bean.ContributiFinder"%>
<%@page import="it.escsolution.escwebgis.contributi.logic.ContributiLogic"%>
<%@page import="it.escsolution.escwebgis.contributi.bean.Contributi"%>
<%@page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<% 	HttpSession sessione = request.getSession(true); %>
<% 	Contributi contr = (Contributi)sessione.getAttribute(ContributiLogic.DETTAGLIO);%>
<%	ContributiFinder finder = null;
	if ((ContributiFinder)sessione.getAttribute(ContributiServlet.NOMEFINDER) != null){
		 finder = (ContributiFinder)sessione.getAttribute(ContributiServlet.NOMEFINDER); 
	}
%>

<%	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% 	String funzionalita = (String)sessione.getAttribute("FUNZIONALITA"); %>

<html>

	<head>
		<title>Contributi - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	
	<script>
		function mettiST() {
			document.mainform.ST.value = 3;
		}

		function viewDett(idx) {
			var disp = document.all("trDett" + idx).style.display;
			document.all("trDett" + idx).style.display = (disp == "none" ? "" : "none");
		}
	</script>
	
	<body >
		<form name="mainform" action="<%= request.getContextPath() %>/Contributi" target="_parent">
		
			<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Dettaglio</span>
			</div>			
			&nbsp;
			
			<center><span class="extWinTXTTitle">Dettaglio contributi per:</span></center>
			
			<table class="viewExtTable" align="center" style="width: 50%;">
				<% Iterator<String> it = contr.getDatiTestata().keySet().iterator(); 
				while (it.hasNext()) {
					String key = it.next();%>
					<tr>
						<td class="TDmainLabel" style="width: 30%;">
							<span class="TXTmainLabel"><%=key%></span>
						</td>
						<td class="TDviewTextBox" style="width: 70%;">
							<span class="TXTviewTextBox"><%=contr.getDatiTestata().get(key)%></span>
						</td>
					</tr>
				<% } %>
			</table>
			
			<br /><br />
			
			<% if (contr.getContributiSib() != null && contr.getContributiSib().size() > 0) { %>
				<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
					<tr>
						<% for (String key : Contributi.getCampiLista()) { %>
							<td class="extWinTDTitle">
								<span class="extWinTXTTitle"><%=ContributiSib.getLabel(key)%></span>
							</td>
						<% } %>
					</tr>
					<% int idx = 0;
					for (ContributiSib contrSib : contr.getContributiSib()) { %>
						<tr onclick="viewDett('<%=idx%>')">
							<% for (String key : Contributi.getCampiLista()) {%>
								<td class="extWinTDData" style="cursor: pointer;" title="Clicca per aprire o chiudere il dettaglio completo dei dati del contributo">
									<span class="extWinTXTData"><%=contrSib.getValueStr(key)%></span>
								</td>
							<% } %>
						</tr>
						<tr id="trDett<%=idx%>" style="display:none;">
							<td colspan="<%= Contributi.getCampiLista().size() %>">
								<table class="viewExtTable" align="center" style="width: 75%; margin-top: 15px; margin-bottom: 15px;">
									<tr>
										<td class="extWinTDTitle" colspan="4">
											<span class="extWinTXTTitle">Dettaglio completo dati contributo</span>
										</td>
									</tr>
									<% Iterator<String> it1 = contrSib.getDati().keySet().iterator();
										int conta = 0;
										while (it1.hasNext()) {
											String key = it1.next();%>
											<% if (conta % 2 == 0) { %>
												<tr>
											<% } %>
													<td class="TDmainLabel" style="width: 15%;">
														<span class="TXTmainLabel"><%=ContributiSib.getLabel(key)%></span>
													</td>
													<td class="TDviewTextBox" style="width: 35%;">
														<span class="TXTviewTextBox"><%=contrSib.getValueStr(key)%></span>
													</td>
												<% conta++;
											if (conta % 2 == 0) { %>
												</tr>
											<% } else if (conta == contrSib.getDati().size()) { %>
													<td class="TDmainLabel" style="width: 15%;">
														<span class="TXTmainLabel">&nbsp;</span>
													</td>
													<td class="TDviewTextBox" style="width: 35%;">
														<span class="TXTviewTextBox">&nbsp;</span>
													</td>
												</tr>
											<% } %>
									<% } %>
								</table>
							</td>
						</tr>
					<% idx++;
					} %>
				</table>
			<% } else { %>
				<span class="TXTviewTextBox">NESSUN DATO PRESENTE</span>
			<% } %>
			
			<% if(finder != null){%>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<% }else{%>
				<input type='hidden' name="ACT_PAGE" value="0">
			<% }%>			
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="134">
			<input type='hidden' name="OGGETTO_SEL" value="">
			<input type='hidden' name="RECORD_SEL" value="">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		
		</form>
		
	<div id="wait" class="cursorWait" />
	</body>
	
</html>