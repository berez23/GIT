<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%HttpSession sessione = request.getSession(true); %>
<%java.util.Vector vlistaVers = (java.util.Vector) sessione.getAttribute("LISTA_VERSAMENTI_POPUP"); %>

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
						<td class="extWinTDTitle"><span class="extWinTXTTitle">CODI FISC</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA
						VERSAMENTO</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP TERRENI
						AGRI</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP AREE
						FABBR</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP PRIMA
						CASA</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP FABBR</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP DETRAZ</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP TOTALE</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO PAGA</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>
					</tr>
		
					<%	
						it.escsolution.escwebgis.tributi.bean.Versamenti ver = new it.escsolution.escwebgis.tributi.bean.Versamenti();		
						java.util.Enumeration en = vlistaVers.elements();
						int contatore = 1;
					%>
					<%while (en.hasMoreElements())
					{
						ver = (it.escsolution.escwebgis.tributi.bean.Versamenti) en.nextElement();%>
						<tr id="r<%=contatore%>">
							<td class="extWinTDData"><span class="extWinTXTData"><%=ver.getCodfisc()%></span></td>
							<td class="extWinTDData"><span class="extWinTXTData"><%=ver.getAnno()%></span></td>
							<td class="extWinTDData"><span class="extWinTXTData"><%=ver.getData_vers()%></span></td>
							<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_terreni_agri()%></span></td>
							<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_aree_fabbr()%></span></td>
							<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_prima_casa()%></span></td>
							<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_fabbr()%></span></td>
							<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_detraz()%></span></td>
							<td align="right" class="extWinTDData"><span class="extWinTXTData"><%=ver.getImp_totale()%></span></td>
							<td align="center" class="extWinTDData"><span class="extWinTXTData"><%=ver.getTipo_paga()%></span></td>
							<td align="center" class="extWinTDData"><span class="extWinTXTData"><%=ver.getProvenienza()%></span></td>
			
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
