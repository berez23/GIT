<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaICI=(java.util.Vector)sessione.getAttribute("LISTA_ICI_POPUP"); %>
		
<%@page import="it.escsolution.escwebgis.tributiNew.bean.OggettiICINew"%>
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
				<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CATEGORIA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">N.DENUNCIA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">POSS. AL 31/12</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>			
			</tr>
	
	
			<% OggettiICINew ici = new OggettiICINew(); %>
			<% java.util.Enumeration en = vlistaICI.elements(); int contatore = 1; %>
			<% while (en.hasMoreElements()) {
				ici = (OggettiICINew)en.nextElement();%>
	
		    <tr id="r<%=contatore%>" <%if (ici.isEvidenzia()) {%> style = "color:green; font-weight:bold;" <%} %>>
		    	<td class="extWinTDData">
				<span class="extWinTXTData"><%=ici.getFoglio()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=ici.getNumero()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=ici.getSub()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=ici.getCat()%></span></td>	
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=ici.getCls()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=ici.getNumDen()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=ici.getYeaDen()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=ici.getFlgPos3112()%></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%=ici.getProvenienza()%></span></td>
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