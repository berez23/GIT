<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(CosapNewLogic.LISTA_COSAP); %>
<% it.escsolution.escwebgis.cosapNew.bean.CosapNewFinder finder = (it.escsolution.escwebgis.cosapNew.bean.CosapNewFinder)sessione.getAttribute(CosapNewLogic.FINDER); %>
<% int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>	
<%@page import="it.escsolution.escwebgis.cosapNew.logic.CosapNewLogic"%>
<%@page import="it.escsolution.escwebgis.cosapNew.bean.CosapContrib"%>
<%@page import="it.escsolution.escwebgis.cosapNew.bean.CosapTassa"%>
<html>
	<head>
		<title>Cosap - Lista</title>
		<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
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
	
	
		function vai(chiave, record_cliccato){
			wait();
			document.mainform.OGGETTO_SEL.value=chiave;
			document.mainform.RECORD_SEL.value=record_cliccato;
			document.mainform.ST.value = 3;
			document.mainform.submit();
		}
	
		function mettiST(){
			document.mainform.ST.value = 2;
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
					popUpAperte[index] = window.open("", "PopUpCosapNewDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpCosapNewDetail" + index, "width=640,height=480,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
		<% if (vlista.size() > 0){
			CosapContrib con = (CosapContrib)vlista.get(0);%>
			function vaiPrimo(){
			 	document.mainform.OGGETTO_SEL.value='<%=con.getChiave()%>';
				document.mainform.RECORD_SEL.value=1;
				document.mainform.ST.value = 3;
				document.mainform.submit();
			}
		<%}%>	
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CosapNew" target="_parent" >

		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Lista Cosap</span>
		</div>
		
		&nbsp;
               
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO SOGGETTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DENOMINAZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTITA IVA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">OGGETTI</span></td>
			</tr>
	
			<% CosapContrib con = new CosapContrib(); %>
			<% java.util.Enumeration en = vlista.elements(); %>
			<% int contatore = 1; %>
			<% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
			<% while (en.hasMoreElements()) {
		        con = (CosapContrib)en.nextElement();
		        String cognome = "-";
		        String nome = "-";
		        String denominazione = "-";
		        if ("G".equalsIgnoreCase(con.getTipoPersona())) {
		        	denominazione = con.getCogDenom();
		        } else {
		        	cognome = con.getCogDenom();
		        	nome = con.getNome();
		        }
		        if ("00000000000".equals(con.getPartitaIva())) {
		        	con.setPartitaIva("-");
		        }
		        String oggetti = "-";
		        if (con.getTasse() != null && con.getTasse().size() > 0) {
		        	oggetti = "";
		        	for (CosapTassa tassa : con.getTasse()) {
		        		oggetti += tassa.getOggettoPerLista() + "<br>";
		        	}
		        }
		        %>

	    		<tr id="r<%=contatore%>">
					<td onclick="vai('<%=con.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=con.getDescTipoPersona()%></span>
					</td>
					<td onclick="vai('<%=con.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=cognome%></span>
					</td>
					<td onclick="vai('<%=con.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=nome%></span>
					</td>
					<td onclick="vai('<%=con.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=denominazione%></span>
					</td>
					<td onclick="vai('<%=con.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=con.getCodiceFiscale()%></span>
					</td>
					<td onclick="vai('<%=con.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=con.getPartitaIva()%></span>
					</td>
					<td onclick="vai('<%=con.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=oggetti%></span>
					</td>
				</tr>
	
				<% 
				contatore++; progressivoRecord ++;
			  	} 
			  	%>

				<input type='hidden' name="ST" value="">
				<input type='hidden' name="OGGETTO_SEL" value="">
				<input type='hidden' name="RECORD_SEL" value="">
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
				<input type='hidden' name="AZIONE" value="">
				<input type='hidden' name="UC" value="113">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>