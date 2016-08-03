<%@page import="java.util.Enumeration"%>
<%@page import="it.escsolution.escwebgis.redditiFamiliari.bean.RedditiFamiliari"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="it.escsolution.escwebgis.common.PulsantiNavigazione"%>
<%@page import="it.escsolution.escwebgis.redditiFamiliari.servlet.RedditiFamiliariServlet"%>
<%@page import="it.escsolution.escwebgis.redditiFamiliari.bean.RedditiFamiliariFinder"%>
<%@page import="it.escsolution.escwebgis.redditiFamiliari.logic.RedditiFamiliariLogic"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<% 	HttpSession sessione = request.getSession(true);  %> 
<%
 	Vector vlista = (Vector)sessione.getAttribute(RedditiFamiliariLogic.LISTA); 
 	
 	RedditiFamiliariFinder finder = null;
 	if (sessione.getAttribute(RedditiFamiliariServlet.NOMEFINDER) != null) {
 		finder = (RedditiFamiliariFinder)sessione.getAttribute(RedditiFamiliariServlet.NOMEFINDER); 
 	} else 
 		finder = new RedditiFamiliariFinder();
 	String ST = (String)sessione.getAttribute("ST");
 %>
<%	PulsantiNavigazione pulsanti = (PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT") != null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% 	String funzionalita = (String)sessione.getAttribute("FUNZIONALITA"); %>

<html>
	<head>
		<title>Redditi Familiari - Lista</title>
		<LINK rel="stylesheet" href="<%= EscServlet.URL_PATH %>/styles/style.css" type="text/css">
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>
	
	<script>
		function chgtr(row, flg) {
			if (flg == 1) {
				document.all("r"+row).style.backgroundColor = "#d7d7d7";
			} else {
				document.all("r"+row).style.backgroundColor = "";
			}
		}

		function vai(codice, record_cliccato) {
			wait();
			document.mainform.OGGETTO_SEL.value = codice;
			document.mainform.RECORD_SEL.value = record_cliccato;
			document.mainform.ST.value = 3;
			document.mainform.submit();
		}

		function mettiST(){
			document.mainform.ST.value = 2;
		}

		function vai(codice, record_cliccato, isPopUp) {
			try {
				document.mainform.OGGETTO_SEL.value = codice;
				document.mainform.RECORD_SEL.value = record_cliccato;
				if (isPopUp) {
					targ = apriPopUp(record_cliccato);				
					if (targ) {
						document.mainform.ST.value = 33;
						document.mainform.target = targ;
						document.mainform.submit();
						document.mainform.ST.value = 2;
						document.mainform.target = "_parent";
					}
				} else {
					wait();
					document.mainform.ST.value = 3;
					document.mainform.target = "_parent";
					document.mainform.submit();
				}
			}
			catch (e) {
				//alert(e);
			}
		}

		var popUpAperte = new Array();
		
		function apriPopUp(index) {
			if (popUpAperte[index]) {
				if (popUpAperte[index].closed) {
					popUpAperte[index] = window.open("", "PopUpRedditiFamiliariDetail" + index, "width=900,height=480,status=yes,resizable=yes");
					popUpAperte[index].focus();
					return popUpAperte[index].name;
				} else {
					popUpAperte[index].focus();
					return false;
				}
			} else {
				popUpAperte[index] = window.open("", "PopUpRedditiFamiliariDetail" + index, "width=900,height=480,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}

		<% if (vlista.size() > 0){
			RedditiFamiliari red = (RedditiFamiliari)vlista.get(0);%>
			function vaiPrimo(){
			 	document.mainform.OGGETTO_SEL.value = '<%=red.getChiave()%>';
				document.mainform.RECORD_SEL.value = 1;
				document.mainform.ST.value = 3;
				document.mainform.submit();
			}
		<%}%>
	</script>
	
	<body onload = "mettiST()">
	
		<form name="mainform" action="<%= EscServlet.URL_PATH %>/RedditiFamiliari" target="_parent" >
 
			<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Elenco Soggetti</span>
			</div>			
			&nbsp;
			
	   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Data Nascita</span></td>
				</tr>
				
				<% RedditiFamiliari red = new RedditiFamiliari(); %>
				<% Enumeration en = vlista.elements(); int contatore = 1; %>
				<% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
				<% while (en.hasMoreElements()) {
						red = (RedditiFamiliari)en.nextElement();%>
					    <tr id="r<%=contatore%>" onclick="vai('<%=red.getChiave()%>', '<%=progressivoRecord%>', true)">
					    	<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="extWinTXTData"><%=red.getCodfisc() == null ? "-" : red.getCodfisc()%></span>
							</td>						
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="extWinTXTData"><%=red.getCognome() == null ? "-" : red.getCognome()%></span>
							</td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="extWinTXTData"><%=red.getNome() == null ? "-" : red.getNome()%></span>
							</td>
							<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
								<span class="extWinTXTData"><%=red.getDataNascitaStr()%></span>
							</td>
						</tr>
						<% contatore++; progressivoRecord++;%>
				<% }%>
			</table>
			
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="OGGETTO_SEL" value="">
			<input type='hidden' name="RECORD_SEL" value="">
			<% if (finder != null) {%>
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<%}%>
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="UC" value="133">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
			
		</form>
		
		<div id="wait" class="cursorWait" />
	
	</body>
	
</html>