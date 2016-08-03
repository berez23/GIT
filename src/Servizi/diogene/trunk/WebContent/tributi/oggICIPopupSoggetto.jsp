<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaICI=(java.util.Vector)sessione.getAttribute("LISTA_ICI_POPUP"); %>
		
<%@page import="it.escsolution.escwebgis.tributi.bean.Contribuente"%>
<html>
	<head>
		<title>Tributi Oggetti ICI - Lista</title>
		<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
		<script src="ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>

	<body>
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			Oggetti ICI</span>
		</div>		
		&nbsp;
	               
	   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">	
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NOMINATIVO<span class="extWinTXTTitle"></td>		
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO<br>DENUNCIA<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO<br>RIFERIMENTO<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUMERO<br>DENUNCIA<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUMERO<br>RIGA<span class="extWinTXTTitle"></td>				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">MESI<br>POSSESSO<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">QUOTA<br>POSSESSO<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">RENDITA<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">POSS. AL<br>31/12<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO<br>DEN.<span class="extWinTXTTitle"></td>				
			</tr>
	
	
			<% Contribuente contr = new Contribuente(); %>
			<% java.util.Enumeration en = vlistaICI.elements(); int contatore = 1; %>
			<% while (en.hasMoreElements()) {
			        contr = (Contribuente)en.nextElement();%>
	
		    <tr id="r<%=contatore%>">
		    	<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getDenominazione()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getProvenienza()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getDenAnno()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getDenRiferimento()%></span></td>	
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getDenNumero()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getNumRiga()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getDenMesiPossesso()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getQuotaPossesso()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getRenditaCatastale()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getDicPosseduto()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=contr.getDenTipo()%></span></td>
			</tr>
		
			<% contatore++; 
			}%>
	
		</table>
		
		&nbsp;
		<div align="center"><span class="extWinTXTTitle">
			<a class="iFrameLink" href="javascript:window.close();">chiudi questa finestra</a></span>
		</div>	
		
	
	</body>
</html>