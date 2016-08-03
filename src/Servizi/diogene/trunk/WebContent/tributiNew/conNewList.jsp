<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaContribuenti = (java.util.Vector)sessione.getAttribute("LISTA_CONTRIBUENTI"); %>
<% it.escsolution.escwebgis.tributiNew.bean.ContribuentiNewFinder finder = (it.escsolution.escwebgis.tributiNew.bean.ContribuentiNewFinder)sessione.getAttribute("FINDER110"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		
<%@page import="java.util.ArrayList"%>
<%@page import="it.escsolution.escwebgis.tributiNew.bean.SoggettiTributiNew"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<html>
	<head>
		<title>Contribuenti - Lista</title>
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
			document.mainform.OGGETTO_SEL.value = chiave;
			document.mainform.RECORD_SEL.value = record_cliccato;
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
					popUpAperte[index] = window.open("", "PopUpContribuentiDetail" + index, "width=1000,height=600,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpContribuentiDetail" + index, "width=1000,height=600,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
	
		<% if (vlistaContribuenti.size() > 0){
			SoggettiTributiNew pContribuente = (SoggettiTributiNew)vlistaContribuenti.get(0);
			String chiave = pContribuente.getChiave();
			%>			
			function vaiPrimo(){
			 	document.mainform.OGGETTO_SEL.value='<%=chiave%>';
				document.mainform.RECORD_SEL.value = 1;
				document.mainform.ST.value = 3;
				document.mainform.submit();
				}
		<%}%>	
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiContribuentiNew" target="_parent" >

		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Elenco Contribuenti</span>
		</div>
		
		&nbsp;
               
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			
			<tr>
				<td class="extWinTDTitle" colspan="7"><span class="extWinTXTTitle">SOGGETTO</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">RESIDENZA</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">TRIBUTI</span></td>
			</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Contribuente</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Fiscale</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Partita IVA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cognome</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Nome</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Denominazione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipi</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
			</tr>
	
			<% SoggettiTributiNew contribuente = new SoggettiTributiNew(); %>
			<% java.util.Enumeration en = vlistaContribuenti.elements(); %>
			<% int contatore = 1; %>
			<% long progressivoRecord = (finder.getPaginaAttuale() - 1) * finder.getRighePerPagina() + 1; %>
			<% while (en.hasMoreElements()) {
				contribuente = (SoggettiTributiNew)en.nextElement();
				String chiave = contribuente.getChiave();
				%>

	    		<tr id="r<%=contatore%>">
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getIdOrig()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getTipSogg()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getCodFisc()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getPartIva()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getCogDenom()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getNome()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getDenominazione()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getDesIndRes()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getDesComRes()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getTributo()%>
						</span>
					</td>
					<td onclick="vai('<%=chiave%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=contribuente.getProvenienza()%>
						</span>
					</td>
				</tr>
	
				<% 
				contatore++;progressivoRecord++;
			  	} 
			  	%>

				<input type='hidden' name="ST" value="">
				<input type='hidden' name="OGGETTO_SEL" value="">
				<input type='hidden' name="RECORD_SEL" value="">
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
				<input type='hidden' name="AZIONE" value="">
				<input type='hidden' name="UC" value="110">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>