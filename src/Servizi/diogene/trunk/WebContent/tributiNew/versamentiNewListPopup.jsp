<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%HttpSession sessione = request.getSession(true); %>
<%java.util.Vector vlistaVers = (java.util.Vector) sessione.getAttribute("LISTA_VERSAMENTI_POPUP"); %>

<%@page import="it.escsolution.escwebgis.tributiNew.bean.VersamentiNew"%>

<html>
	<head>
		<title>Versamenti - Lista</title>
		<LINK rel="stylesheet"
			href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
			type="text/css">
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	<body>

	
	<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">Lista Versamenti</span></div>

		&nbsp;
		<table bgcolor="white">
			<tr style="background-color: white;">
			<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
				<table class="extWinTable" cellpadding="0" cellspacing="0">
		
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA VERSAMENTO</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. TERRENI AGRIC.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. AREE FABBR.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. PRIMA CASA</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. FABBR.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. DETRAZ.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. TOTALE</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO PAG.</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>
					</tr>
		
					<% VersamentiNew versamenti = new VersamentiNew(); %>
					<% java.util.Enumeration en = vlistaVers.elements(); 
					int contatore = 1;%>
					<% while (en.hasMoreElements()) {
		        		versamenti = (VersamentiNew)en.nextElement();%>
						<tr id="r<%=contatore%>">
							<td class="extWinTDData">
								<span class="extWinTXTData"><%=versamenti.getCodFisc()%></span>
							</td>
							<td class="extWinTDData">
								<span class="extWinTXTData"><%=versamenti.getYeaRif()%></span>
							</td>
							<td class="extWinTDData">
								<span class="extWinTXTData"><%=versamenti.getDatPag()%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData"><%=versamenti.getImpTerAgrEu()%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData"><%=versamenti.getImpAreFabEu()%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData"><%=versamenti.getImpAbiPriEu()%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData"><%=versamenti.getImpAltFabEu()%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData"><%=versamenti.getImpDtrEu()%></span>
							</td>
							<td class="extWinTDData" style="text-align: right;">
								<span class="extWinTXTData"><%=versamenti.getImpPagEu()%></span>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=versamenti.getTipPag()%></span>
							</td>
							<td class="extWinTDData" style="text-align: center;">
								<span class="extWinTXTData"><%=versamenti.getProvenienza()%></span>
							</td>
						</tr>
						<%contatore++;
					}%>
				</table>
				</td>
			</tr>
		
		</table>
		
		&nbsp;
		<div align="center"><span class="extWinTXTTitle">
			<a class="iFrameLink" href="javascript:window.close();">chiudi questa finestra</a></span>
		</div>

	</body>
</html>
