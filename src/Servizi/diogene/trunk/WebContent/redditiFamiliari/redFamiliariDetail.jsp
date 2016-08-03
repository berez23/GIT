<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.escsolution.escwebgis.redditiFamiliari.bean.RedditiPersonaFamigliaAnno"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="it.escsolution.escwebgis.redditiFamiliari.servlet.RedditiFamiliariServlet"%>
<%@page import="it.escsolution.escwebgis.redditiFamiliari.bean.RedditiFamiliariFinder"%>
<%@page import="it.escsolution.escwebgis.redditiFamiliari.logic.RedditiFamiliariLogic"%>
<%@page import="it.escsolution.escwebgis.redditiFamiliari.bean.RedditiFamiliari"%>
<%@page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<% 	HttpSession sessione = request.getSession(true); %>
<% 	RedditiFamiliari red = (RedditiFamiliari)sessione.getAttribute(RedditiFamiliariLogic.DETTAGLIO);%>
<%	RedditiFamiliariFinder finder = null;
	if ((RedditiFamiliariFinder)sessione.getAttribute(RedditiFamiliariServlet.NOMEFINDER) != null){
		 finder = (RedditiFamiliariFinder)sessione.getAttribute(RedditiFamiliariServlet.NOMEFINDER); 
	}
%>

<%	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% 	String funzionalita = (String)sessione.getAttribute("FUNZIONALITA"); %>

<html>

	<head>
		<title>Redditi Familiari - Dettaglio</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
		<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	
	<script>
		function mettiST() {
			document.mainform.ST.value = 3;
		}
		
		function vaiDettRedditi(chiave) {
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/RedditiAnnuali?' +
					'ST=33&OGGETTO_SEL=' + chiave + '&RECORD_SEL=1&ACT_PAGE=1&AZIONE=&UC=52&EXT=null&EXT=true&BACK=&BACK_JS_COUNT=1'
			window.open(url,'dettRedditi','toolbar=no,scrollbars=yes,resizable=yes,top=75,left=75,width=700,height=480');
		}

		function vaiDettCatasto(chiave) {
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoIntestatariF?' +
					'ST=33&OGGETTO_SEL=' + chiave + '&RECORD_SEL=1&ACT_PAGE=1&AZIONE=&UC=3&BACK=&BACK_JS_COUNT=1'
			window.open(url,'dettCatasto','toolbar=no,scrollbars=yes,resizable=yes,top=75,left=75,width=700,height=480');
		}

		function vaiDettContributi(chiave) {
			var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Contributi?' +
					'ST=33&OGGETTO_SEL=' + chiave + '&RECORD_SEL=1&ACT_PAGE=1&AZIONE=&UC=134&BACK=&BACK_JS_COUNT=1'
			window.open(url,'dettContributi','toolbar=no,scrollbars=yes,resizable=yes,top=75,left=75,width=900,height=480');
		}
	</script>
	
	<body >
		<form name="mainform" action="<%= request.getContextPath() %>/RedditiFamiliari" target="_parent">
		
			<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Dettaglio</span>
			</div>			
			&nbsp;
			
			<center><span class="extWinTXTTitle">Dati Anagrafici</span></center>
			
			<table class="viewExtTable" align="center" style="width: 50%;">
				<tr>
					<td class="TDmainLabel" style="width: 30%;">
						<span class="TXTmainLabel">Codice Fiscale</span>
					</td>
					<td class="TDviewTextBox" style="width: 70%;">
						<span class="TXTviewTextBox"><%=red.getCodfisc()%></span>
					</td>
				</tr>
				<tr>
					<td class="TDmainLabel">
						<span class="TXTmainLabel">Cognome</span>
					</td>
					<td class="TDviewTextBox">
						<span class="TXTviewTextBox"><%=red.getCognome()%></span>
					</td>
				</tr>
				<tr>
					<td class="TDmainLabel">
						<span class="TXTmainLabel">Nome</span>
					</td>
					<td class="TDviewTextBox">
						<span class="TXTviewTextBox"><%=red.getNome()%></span>
					</td>
				</tr>
				<tr>
					<td class="TDmainLabel">
						<span class="TXTmainLabel">Data Nascita</span>
					</td>
					<td class="TDviewTextBox">
						<span class="TXTviewTextBox"><%=red.getDataNascitaStr()%></span>
					</td>
				</tr>
			</table>
			
			
			<% LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<RedditiPersonaFamigliaAnno>>> redPersFamAnn = red.getRedPersFamAnn();
			if (redPersFamAnn == null || redPersFamAnn.size() == 0) { %>
				<br />
				<span class="TXTviewTextBox">NESSUN DATO PRESENTE</span>
			<% } else { %>
				<div class="tabber">
				<%Iterator<Integer> it = redPersFamAnn.keySet().iterator();
				while (it.hasNext()) {
					Integer anno = it.next();
					String annoStr = "" + anno.intValue();%>
						<div class="tabbertab">
							<h2><%= annoStr%></h2>
							<% if (redPersFamAnn.get(anno) == null || redPersFamAnn.get(anno).size() == 0) { %>
								<br />
								<span class="TXTviewTextBox">NESSUN DATO PRESENTE</span>
							<% } else {
								Iterator<String> it1 = redPersFamAnn.get(anno).keySet().iterator();
								while (it1.hasNext()) { %>
									<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
										<tr>&nbsp;</tr>			
										<tr>
											<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
											<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
											<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
											<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Nascita</span></td>
											<td class="extWinTDTitle"><span class="extWinTXTTitle">Rapporto Parentela</span></td>
											<td class="extWinTDTitle"><span class="extWinTXTTitle">Reddito</span></td>
											<% if (red.isViewContributi()) {%>
												<td class="extWinTDTitle"><span class="extWinTXTTitle">Contributi erogati</span></td>
											<% } %>
											<td class="extWinTDTitle"><span class="extWinTXTTitle">Immobili da catasto</span></td>
											<td class="extWinTDTitle"><span class="extWinTXTTitle">Immobili dichiarati<br />nel comune</span></td>
											<td class="extWinTDTitle"><span class="extWinTXTTitle">Immobili dichiarati<br />fuori comune</span></td>								
										</tr>
										<% ArrayList<RedditiPersonaFamigliaAnno> dati = redPersFamAnn.get(anno).get(it1.next());
										int totFam = 0;
										double totRed = 0D;
										double totContr = 0D;
										int totNumContr = 0;
										int totNumImmCat = 0;
										int totNumImmDichCom = 0;
										int totNumImmDichFuoriCom = 0;
										for (RedditiPersonaFamigliaAnno dato : dati) {
											totFam++; %>
											<tr>
												<td class="extWinTDData"><span class="extWinTXTData"><%=dato.getCodFisc()%></span></td>
												<td class="extWinTDData"><span class="extWinTXTData"><%=dato.getCognome()%></span></td>
												<td class="extWinTDData"><span class="extWinTXTData"><%=dato.getNome()%></span></td>
												<td class="extWinTDData"><span class="extWinTXTData"><%=dato.getDataNascitaStr()%></span></td>
												<td class="extWinTDData"><span class="extWinTXTData"><%=dato.getRappParentela()%></span></td>
												<td class="extWinTDData" style="text-align: right; padding: 5px;">
													<span class="extWinTXTData">
														<% if (dato.getChiaveDettRedditi() != null) {%>
															<a href="#" onclick="vaiDettRedditi('<%=dato.getChiaveDettRedditi()%>'); return false;"><%=dato.getRedditoStr()%></a>
														<% } else { %>
															<%=dato.getRedditoStr()%>
														<% } 
														totRed += dato.getReddito().doubleValue(); %>
													</span>
												</td>
												<% if (red.isViewContributi()) {%>
													<td class="extWinTDData" style="text-align: right; padding: 5px;">
														<span class="extWinTXTData">
															<% if (dato.getChiaveDettContributi() != null) {%>
																<a href="#" onclick="vaiDettContributi('<%=dato.getChiaveDettContributi()%>'); return false;"><%=dato.getContributiStr()%> (<%=dato.getNumContributi()%>)</a>
															<% } else { %>
																<%=dato.getContributiStr()%> (<%=dato.getNumContributi()%>)
															<% } 
															totContr += dato.getContributi().doubleValue();
															totNumContr += dato.getNumContributi(); %>
														</span>
													</td>
												<% } %>
												<td class="extWinTDData" style="text-align: right; padding: 5px;">
													<span class="extWinTXTData">
														<% if (dato.getChiaveDettCatasto() != null && dato.getNumImmCatasto() > 0) {%>
															<a href="#" onclick="vaiDettCatasto('<%=dato.getChiaveDettCatasto()%>'); return false;"><%=dato.getNumImmCatasto()%> (<%=dato.getPercPossRenditaStr()%>)</a>
														<% } else { %>
															<%=dato.getNumImmCatasto()%> (<%=dato.getPercPossRenditaStr()%>)
														<% } 
														totNumImmCat += dato.getNumImmCatasto(); %>
													</span>
												</td>
												<td class="extWinTDData" style="text-align: right; padding: 5px;">
													<span class="extWinTXTData">
														<% if (dato.getChiaveDettRedditi() != null) {%>
															<a href="#" onclick="vaiDettRedditi('<%=dato.getChiaveDettRedditi()%>'); return false;"><%=dato.getImmDichComune()%></a>
														<% } else { %>
															<%=dato.getImmDichComune()%>
														<% } 
														totNumImmDichCom += dato.getImmDichComune(); %>
													</span>
												</td>
												<td class="extWinTDData" style="text-align: right; padding: 5px;">
													<span class="extWinTXTData">
														<% if (dato.getChiaveDettRedditi() != null) {%>
															<a href="#" onclick="vaiDettRedditi('<%=dato.getChiaveDettRedditi()%>'); return false;"><%=dato.getImmDichFuoriComune()%></a>
														<% } else { %>
															<%=dato.getImmDichFuoriComune()%>
														<% } 
														totNumImmDichFuoriCom += dato.getImmDichFuoriComune(); %>
													</span>
												</td>
											</tr>
										<% } 
										if (totFam > 0) { 
											DecimalFormat DF = new DecimalFormat();
											DF.setGroupingUsed(true);
											DecimalFormatSymbols dfs = new DecimalFormatSymbols();
											dfs.setDecimalSeparator(',');
											dfs.setGroupingSeparator('.');
											DF.setDecimalFormatSymbols(dfs);
											DF.setMinimumFractionDigits(2);
											DF.setMaximumFractionDigits(2); %>
											<tr>
												<td class="TDviewTextBox">
													<span class="TXTviewTextBox">TOTALE</span>
												</td>
												<td class="TDviewTextBox" colspan="4">
													<span class="TXTviewTextBox"><%= totFam %></span>
												</td>
												<td class="TDviewTextBox" style="text-align: right; padding: 5px;">
													<span class="TXTviewTextBox">
														<%= totRed == 0D ? "-" : DF.format(totRed) %>
													</span>
												</td>
												<% if (red.isViewContributi()) {%>
													<td class="TDviewTextBox" style="text-align: right; padding: 5px;">
														<span class="TXTviewTextBox">
															<%= DF.format(totContr) %> (<%= totNumContr %>)
														</span>
													</td>
												<% } %>
												<td class="TDviewTextBox" style="text-align: right; padding: 5px;">
													<span class="TXTviewTextBox">
														<%= totNumImmCat %>
													</span>
												</td>
												<td class="TDviewTextBox" style="text-align: right; padding: 5px;">
													<span class="TXTviewTextBox">
														<%= totNumImmDichCom %>
													</span>
												</td>
												<td class="TDviewTextBox" style="text-align: right; padding: 5px;">
													<span class="TXTviewTextBox">
														<%= totNumImmDichFuoriCom %>
													</span>
												</td>
											</tr>
										<% } %>
									</table>
									<br>
								<% } 
							} %>
						</div>
				<% } %>
				</div>
			<% } %>
			
			<% if(finder != null){%>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<% }else{%>
				<input type='hidden' name="ACT_PAGE" value="0">
			<% }%>			
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="UC" value="133">
			<input type='hidden' name="OGGETTO_SEL" value="">
			<input type='hidden' name="RECORD_SEL" value="">
			<input type='hidden' name="EXT" value="">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		
		</form>
		
	<div id="wait" class="cursorWait" />
	</body>
	
</html>