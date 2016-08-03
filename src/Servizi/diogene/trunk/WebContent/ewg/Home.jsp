<!-- Copyright (c) 2002 by ObjectLearn. All Rights Reserved. -->
<%@ page contentType="text/html" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<jsp:useBean id="appFonti" class="it.escsolution.eiv.database.Fonti" scope="session">
</jsp:useBean> 
<%@page import="it.escsolution.eiv.database.FonteNew"%>
<%@page import="it.webred.DecodificaPermessi"%>
<html>
	<head>
		<title>Home</title>
		<LINK rel="stylesheet" href="<%=request.getContextPath()%>/styles/style.css" type="text/css">
		<script type="text/javascript" language="Javascript">
			function modifica(seqTema){
				document.forms['frm'].hSeqTema.value = seqTema;
				var mywindow = window.open ('', 'mywindow', 'location=0, status=1, scrollbars=1, menubar=0, resizable=1, width=700, height=550');
				document.forms['frm'].submit();
			}
		</script>
	</head>
	
	<body class="cursorReady" topmargin="0" leftmargin="0">
	<form id="frm" name="frm" action="<%= request.getContextPath() %>/UpdateServlet" target="mywindow">
		
			<center>
				<div style="position: relative; margin-top: 25px; margin-bottom: 25px; width: 85%; height: 75%;">
				
					<div class="extWinTDTitle" style="text-align: center; vertical-align: middle; margin-bottom: 10px;">
						<span class="extWinTXTTitle" style="font-size: 10pt;">
							Fonti dati e stato dell'aggiornamento
							<input type="hidden" value="" name="hSeqTema"/>
						</span>
					</div>
					<% String currProgetto = "-";%>				
						<% for (FonteNew fonte : appFonti.getFonti(request)) {
							if (!fonte.getTipoFonte().equals( currProgetto)) {
								if (!currProgetto.equals("-")) {%>
									</table>
									</br>
								<% }
								currProgetto = fonte.getTipoFonte(); %>
								<table class="extWinTable" style="width: 100%;" cellpadding="0" cellspacing="0">	
									<tr>
										<td class="extWinTDData" colspan="5" style="text-align: center;">
											<span class="TXTmainLabel" style="font-size: 10pt;"><%=fonte.getDescrizioneTipoFonte()%></span>
										</td>
									</tr>
									<tr>
										<td class="extWinTDData" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">Fonte dati</span>
										</td>
										<td class="extWinTDData" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">Data di riferimento</span>
										</td>
										<td class="extWinTDData" style="width: 15%; text-align: center;">
											<span class="TXTmainLabel">Data inizio</span>
										</td>
										<td class="extWinTDData" style="width: 15%; text-align: center;">
											<span class="TXTmainLabel">Data aggiornamento</span>
										</td>
										<td class="extWinTDData" style="width: 30%; text-align: center;">
											<span class="TXTmainLabel">Note</span>
										</td>
										
									</tr>
							<% } %>
							<tr>
								<td class="extWinTDData" style="padding-left: 3px;">
									<span class="TXTmainLabel" style="color: #000000;"><%=fonte.getDescrizione()%></span>
								</td>
								<td class="extWinTDData" style="text-align: center;">
									<span class="TXTmainLabel" style="color: #000000;"><%=fonte.getDataRiferimento()%></span>
								</td>
								<td class="extWinTDData" style="text-align: center;">
									<span class="TXTmainLabel" style="color: #000000;"><%=fonte.getDataInizio()%></span>
								</td>
								<td class="extWinTDData" style="text-align: center;">
									
										<span class="TXTmainLabel" style="color: #000000;"><%=fonte.getDataAggiornamento()%></span>
									
								</td>
								<td class="extWinTDData" style="padding-left: 3px;">
									<span class="TXTmainLabel" style="color: #000000;"><%=fonte.getNote()%></span>
									<input type="hidden" value="Home.jsp" name="hProvenienza"/>
								</td>
								
										
									
								
								
							</tr>
						<% } %>
					</table>
				</div>		
			</center>
		</form>	
	</body>
</html>