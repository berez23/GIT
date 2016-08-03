<!-- Copyright (c) 2002 by ObjectLearn. All Rights Reserved. -->
<%@ page contentType="text/html" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<jsp:useBean id="appFonti" class="it.escsolution.eiv.database.Fonti" scope="session">
</jsp:useBean> 
<%@page import="it.escsolution.eiv.database.Fonte"%>
<html>
	<% 
	String seqTema = request.getParameter("seqTema"); 
	seqTema = (String)request.getAttribute("hSeqTema");
	Fonte fonte = (Fonte)request.getAttribute("fonte");
	%>
	<head>
		<title>Fonte Modica</title>
		<LINK rel="stylesheet" href="<%=request.getContextPath()%>/styles/style.css" type="text/css">
		<script type="text/javascript">
			function salva(){
				document.frm.submit();
				window.opener.location.reload();
				self.close();
			}
		</script>
		
		
	</head>
	
	<body class="cursorReady" topmargin="0" leftmargin="0">
		<form name="frm" action="<%= request.getContextPath() %>/UpdateServlet"  method="POST">
		<input type="hidden" value="modValuesPopup.jsp" name="hProvenienza"/>
			<center>
				<div style="position: relative; margin-top: 25px; margin-bottom: 25px; width: 85%; height: 75%;">
					<div class="extWinTDTitle" style="text-align: center; vertical-align: middle; margin-bottom: 10px;">
						<span class="extWinTXTTitle" style="font-size: 10pt;">Modifica stato dell'aggiornamento fonti dati </span>
					</div>
			
							<table class="extWinTable" style="width: 90%;" cellpadding="0" cellspacing="0">	
									<tr>
										<td class="extWinTDData" colspan="5" style="text-align: center;">
											<span class="TXTmainLabel" style="font-size: 10pt;">
												<%= fonte.getDescrizioneFonte() %>
												<input type="hidden" value="<%=seqTema %>" name="hSeqTema"/>
											</span>
										</td>
									</tr>
									<tr>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">Data Aggiornamento</span>
										</td>
										<td class="extWinTDData" colspan="3" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">
											<input type="text" id="txtDataAgg" name="txtDataAgg" style="width: 220px" value="<%=fonte.getDataAggiornamento() %>" />
											</span>
										</td>
									</tr>
									<tr>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">SQL Data Aggiornamento</span>
										</td>
										<td class="extWinTDData" colspan="3" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">
											<textarea id="txtSqlDataAgg" name="txtSqlDataAgg" style="width: 220px" cols="25" rows="5"><%=fonte.getSqlDataAgg() %></textarea>
											</span>
										</td>
									</tr>
									<tr>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">Data Inizio</span>
										</td>
										<td class="extWinTDData" colspan="3" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">
											<input type="text" id="txtDataIni" name="txtDataIni" style="width: 220px" value="<%=fonte.getDataInizio() %>" />
											</span>
										</td>
									</tr>
									<tr>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">SQL Data Inizio</span>
										</td>
										<td class="extWinTDData" colspan="3" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">
											<textarea id="txtSqlDataIni" name="txtSqlDataIni" style="width: 220px" cols="25" rows="5"><%=fonte.getSqlDataIni() %></textarea>
											</span>
										</td>
									</tr>
									<tr>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">Note</span>
										</td>
										<td class="extWinTDData" colspan="3" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">
											<textarea id="txtNote" name="txtNote" style="width: 220px" cols="25" rows="5"><%=fonte.getNote() %></textarea>
											</span>
										</td>
									</tr>
									<tr>
										<td class="extWinTDData" colspan="5" style="text-align: center;">
											<span class="TXTmainLabel">
												Nel caso in cui vengano valorizzati entrambi i campi Data ed SQL, il valore visualizzato sarà quello che deriva dall'esecuzione della query.
Gli alias da usare nelle query per le date sono DATA_AGG per la data di aggiornamento e DATA_INI per la data di inizio.
											</span>
										</td>
									</tr>
							<tr>
										<td class="extWinTDData" colspan="5" style="width: 20%; text-align: center;">
											<input type="button" value="SALVA" name="btnSalva" onclick="salva();"/>
										</td>
										
									</tr>
					</table>
				</div>		
			</center>
		
			
		</form>		
	</body>
</html>