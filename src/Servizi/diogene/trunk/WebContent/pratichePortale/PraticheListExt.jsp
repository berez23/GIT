<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(PraticheLogic.LISTA_PRATICHE); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<% int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>	
<%@page import="it.escsolution.escwebgis.pratichePortale.logic.PraticheLogic"%>
<%@page import="it.escsolution.escwebgis.pratichePortale.bean.Pratica"%>
<html>
	<head>
		<title>Pratiche Portale Servizi - Lista</title>
		<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>

	<body>

		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Lista Pratiche Portale Servizi</span>
		</div>

		<%if(vlista.isEmpty()){%>
			<br/><br/>
			<div align="center">
				Non è stata trovata nessuna pratica
			</div>
		<%}else{%>
		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/PratichePortale" target="_parent" >
		
		&nbsp;
               
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			
			<tr>
				<td class="extWinTDTitle" colspan="4"><span class="extWinTXTTitle">DATI PRATICA</span></td>
				<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">SOGGETTI RICHIEDENTE</span></td>
				<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">SOGGETTI FRUITORE</span></td>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">SOTTOTIPO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">STATO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
			
			</tr>
	
			<% Pratica t = new Pratica(); %>
			<% java.util.Enumeration en = vlista.elements(); %>
			<% int contatore = 1; %>
			<% while (en.hasMoreElements()) {
		        t = (Pratica)en.nextElement();
		        %>

	    		<tr id="r<%=contatore%>">
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><%=t.getTipo()!=null ? t.getTipo() : "-" %></span>
					</td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><%=t.getSottotipo()!=null ? t.getSottotipo() : "-" %></span>
					</td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><%=t.getDataCreazione()!=null ? t.getDataCreazione() : "-"%></span>
					</td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><%=t.getNomeStato()!=null ? t.getNomeStato() : "-" %></span>
					</td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><%=t.getNomeRichiedente()!=null ? t.getNomeRichiedente() : "-" %></span>
					</td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><%=t.getCognomeRichiedente()!=null ? t.getCognomeRichiedente() : "-" %></span>
					</td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)''>
						<span class="extWinTXTData"><%=t.getCodFisRichiedente()!=null ? t.getCodFisRichiedente() : "-" %></span>
					</td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><%=t.getNomeFruitore()!=null ? t.getNomeFruitore() : "-" %></span>
					</td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><%=t.getCognomeFruitore()!=null ? t.getCognomeFruitore() : "-" %></span>
					</td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><%=t.getCodFisFruitore()!=null ? t.getCodFisFruitore() : "-" %></span>
					</td>	
				</tr>
	
				<% 
				contatore++;
			  	} 
			  	%>

		</table>
		</form>
		<%}%>
	</body>
</html>