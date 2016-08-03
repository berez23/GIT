<%@page import="java.util.*"%>
<%@page import="it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic"%>
<%@page import="it.escsolution.escwebgis.anagrafe.bean.*"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%  HttpSession sessione = request.getSession(true);

	List llista = null;
	if (request.getParameter("STORICO") != null && new Boolean(request.getParameter("STORICO")).booleanValue()) {
		llista = (List)sessione.getAttribute(AnagrafeDwhLogic.VIS_STORICO_LISTA + "_" + request.getParameter("codAnagrafe"));
	} else {
		llista = new ArrayList();
		Vector vlista = (Vector)sessione.getAttribute(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI + "_" + request.getParameter("codAnagrafe"));		
		if (vlista != null) {
			Enumeration en = vlista.elements(); 
			while (en.hasMoreElements()) {
				llista.add((AnagrafeStorico)en.nextElement());
			}
		}
	}
	
	Date dtAggStorico = (Date)sessione.getAttribute(AnagrafeDwhLogic.VIS_STORICO_DT_AGG);
	String lblStoricoPeriodo = (String)sessione.getAttribute(AnagrafeDwhLogic.LBL_STORICO_PERIODO);
	
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
%>

<html>
	<head>
		<title>
			<% if (request.getParameter("STORICO") != null && new Boolean(request.getParameter("STORICO")).booleanValue()) { %>
				Storico della Famiglia elaborato
			<% } else { %>
				Storico della Famiglia per il Soggetto
			<% } %>
		</title>
		
		<LINK rel="stylesheet" href="<%= EscServlet.URL_PATH %>/styles/style.css" type="text/css">
	</head>
	
	<script>
		function chgtr(row,flg)
		{
		if (flg==1)
			{
			document.all("r"+row).style.backgroundColor = "#d7d7d7";
			}
		else
			{
			document.all("r"+row).style.backgroundColor = "";
			}
		}
	
		function vai(codice, record_cliccato, isPopUp)
		{
			try
			{
				document.mainform.OGGETTO_SEL.value = codice;
				document.mainform.RECORD_SEL.value = record_cliccato;
				if (isPopUp)
				{
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
					popUpAperte[index] = window.open("", "PopUpAnaDwhDetailFamiglia" + index, "width=640,height=670,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpAnaDwhDetailFamiglia" + index, "width=640,height=670,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}

		function apriChiudiDettFam(idx) {
			document.getElementById("rDett" + idx).style.display = (document.getElementById("rDett" + idx).style.display == "none" ? "" : "none");
			document.getElementById("a" + idx).title = "Clicca per " + (document.getElementById("rDett" + idx).style.display == "none" ? "aprire" : "chiudere") + " il dettaglio della famiglia";
		}
	</script>
	
	<body>

		<form name="mainform" action="<%= EscServlet.URL_PATH %>/AnagrafeDwh" target="_parent" >
		
			<div align="center" class="extWinTDTitle"
			<% if (lblStoricoPeriodo != null && !lblStoricoPeriodo.equals("")) {%>style="height: 30px;"<%}%>><span class="extWinTXTTitle">
				<% if (request.getParameter("STORICO") != null && new Boolean(request.getParameter("STORICO")).booleanValue() && dtAggStorico != null) { %>
					Storico della Famiglia elaborato in data <%= SDF.format(dtAggStorico) %>
					<% if (lblStoricoPeriodo != null && !lblStoricoPeriodo.equals("")) {%>
						<br /><%=lblStoricoPeriodo%>
					<% } %>
				<% } else { %>
					Storico della Famiglia per il Soggetto
				<% } %>
			</div>
			
			&nbsp;
			
			<table align="center" class="extWinTable" style="width: 100%;">	
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Famiglia</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Inizio Appartenenza</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Fine Appartenenza</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Inizio Residenza</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Fine Residenza</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
				</tr>
			
			<%
			String dataIniRif = "-";
			String dataFinRif = "-";
			AnagrafeStorico anaSto = null;
			ArrayList listaFam = null;
			FamiliariStorico fam = null;

			if (llista != null && llista.size() > 0) { 
				int contatore = 1; 
				for (Object anaStoObj : llista) {
					anaSto = (AnagrafeStorico)anaStoObj;
					listaFam = anaSto.getElencoFamiliari();
				  	if (listaFam.size()>0) {
				  		fam = (FamiliariStorico)listaFam.get(0);
					  	dataIniRif = fam.getDataInizioRif() != null && !SDF.format(fam.getDataInizioRif()).equals("01/01/1000") && !SDF.format(fam.getDataInizioRif()).equals("01/01/1001") ? SDF.format(fam.getDataInizioRif()) : "ORIGINE";
					  	dataFinRif = fam.getDataFineRif() != null && !SDF.format(fam.getDataFineRif()).equals("31/12/9999") ? SDF.format(fam.getDataFineRif()) : "OGGI";
				  	}
				  	%>
				  	
					<tr id="r<%=contatore%>">
						<td class="extWinTDData" style='cursor: pointer;'>
							<span class="linkextLabel">
								<a id="a<%=contatore%>" onclick="apriChiudiDettFam('<%=contatore%>'); return false;" title="Clicca per aprire il dettaglio della famiglia">
									<%= anaSto.getCodFamiglia() %>
								</a>
							</span>
						</td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= anaSto.getInizioFamiglia() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= anaSto.getFineFamiglia() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= request.getParameter("STORICO") != null && new Boolean(request.getParameter("STORICO")).booleanValue() && dtAggStorico != null ?
														anaSto.getInizioResidenzaElab() :
														anaSto.getInizioResidenza() %></span></td>
						<td class="extWinTDData">
						<span class="extWinTXTData"><%= request.getParameter("STORICO") != null && new Boolean(request.getParameter("STORICO")).booleanValue() && dtAggStorico != null ?
														anaSto.getFineResidenzaElab() :
														anaSto.getFineResidenza() %></span></td>
						<td class="extWinTDData">
							<span class="extWinTXTData">
								<% if (anaSto.getDescrVia().equals("-")) { %>
									<%= anaSto.getDescrVia() %>
								<% } else { %>
									<%= anaSto.getDescrVia() %>, <%=anaSto.getCivico() %>
								<% } %>
							</span>
						</td>
					</tr>

					<tr id="rDett<%=contatore%>" style="display: none">
						<td colspan="6" style="padding: 5px;">
							<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
								<tr>
									<td colspan="6" style="text-align: center;" class="extWinTDTitle">
										<span class="extWinTXTTitle">
											Situazione della famiglia da <%=dataIniRif%> a <%=dataFinRif%>
										</span>
									</td>
								</tr>
							
								<tr>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Data di nascita</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo parentela</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Data validità iniziale</span></td>
									<td class="extWinTDTitle"><span class="extWinTXTTitle">Data validità finale</span></td>
								</tr>
						
					  			<% int contatoreInterno = 1;%>
							  	<% for (int i =0;i<listaFam.size();i++) {
							        fam = (FamiliariStorico)listaFam.get(i);%>
							    	<tr id="r<%=contatoreInterno%>">
									
									<td onclick="vai('<%=fam.getId()%>', '<%=contatoreInterno%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatoreInterno%>,1)' onmouseout='chgtr(<%=contatoreInterno%>,0)' style='cursor: pointer;'>
									<span class="extWinTXTData"><%=fam.getCognome()%></span></td>
									<td onclick="vai('<%=fam.getId()%>', '<%=contatoreInterno%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatoreInterno%>,1)' onmouseout='chgtr(<%=contatoreInterno%>,0)' style='cursor: pointer;'>
									<span class="extWinTXTData"><%=fam.getNome()%></span></td>
									<td onclick="vai('<%=fam.getId()%>', '<%=contatoreInterno%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatoreInterno%>,1)' onmouseout='chgtr(<%=contatoreInterno%>,0)' style='cursor: pointer;'>
									<span class="extWinTXTData"><%=fam.getDtNascita()%></span></td>
									<td onclick="vai('<%=fam.getId()%>', '<%=contatoreInterno%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatoreInterno%>,1)' onmouseout='chgtr(<%=contatoreInterno%>,0)' style='cursor: pointer;'>
									<span class="extWinTXTData"><%=fam.getTipoParentela()%></span></td>	
									<td onclick="vai('<%=fam.getId()%>', '<%=contatoreInterno%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatoreInterno%>,1)' onmouseout='chgtr(<%=contatoreInterno%>,0)' style='cursor: pointer;'>
									<span class="extWinTXTData"><%=fam.getDtInizio()%></span></td>	
									<td onclick="vai('<%=fam.getId()%>', '<%=contatoreInterno%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatoreInterno%>,1)' onmouseout='chgtr(<%=contatoreInterno%>,0)' style='cursor: pointer;'>
									<span class="extWinTXTData"><%=fam.getDtFine()%></span></td>	
									</tr>
								
								<% 		contatoreInterno++;
									} 
								%>

							</table>
						</td>
					</tr>
				<% contatore++;
				}%>
				</table>
				<br />
			<% } %>
			
			<table style="background-color: white; width: 100%;">
				<tr style="background-color: white;">
					<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
						<span class="TXTviewTextBox" style="background-color: white; cursor: pointer;">
							<a href="javascript:window.close();" style="text-decoration: none;">
								chiudi
							</a>
						</span>
					</td>
				</tr>
			</table>
	
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="OGGETTO_SEL" value="">
			<input type='hidden' name="RECORD_SEL" value="">
			
		</form>
		
	</body>
</html>