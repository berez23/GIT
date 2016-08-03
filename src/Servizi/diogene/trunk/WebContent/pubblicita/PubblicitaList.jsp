<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(PubblicitaLogic.LISTA_PUBBLICITA); %>
<% it.escsolution.escwebgis.pubblicita.bean.PubblicitaFinder finder = (it.escsolution.escwebgis.pubblicita.bean.PubblicitaFinder)sessione.getAttribute(PubblicitaLogic.FINDER); %>
<% int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>	
<%@page import="it.escsolution.escwebgis.pubblicita.logic.PubblicitaLogic"%>
<%@page import="it.escsolution.escwebgis.pubblicita.bean.PubblicitaTestata"%>
<%@page import="it.escsolution.escwebgis.pubblicita.bean.PubblicitaDettaglio"%>
<html>
	<head>
		<title>Pubblicita' - Lista</title>
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
					popUpAperte[index] = window.open("", "PubblicitaDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PubblicitaDetail" + index, "width=640,height=480,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
		<%if (vlista.size() > 0){
			PubblicitaTestata con = (PubblicitaTestata)vlista.get(0);%>
			function vaiPrimo(){
			 	document.mainform.OGGETTO_SEL.value='<%=con.getChiave()%>';
				document.mainform.RECORD_SEL.value=1;
				document.mainform.ST.value = 3;
				document.mainform.submit();
			}
		<%}%>	
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Pubblicita" target="_parent" >

		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Lista Pratiche</span>
		</div>
		
		&nbsp;
               
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			
			<tr>
				<td class="extWinTDTitle" colspan="7"><span class="extWinTXTTitle">DATI PRATICA</span></td>
				<td class="extWinTDTitle" colspan="4"><span class="extWinTXTTitle">OGGETTI TERRITORIALI COLLEGATI</span></td>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUMERO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA INIZIO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DEC.TERMINI</span></td>
				<td class="extWinTDTitle" width="20%"><span class="extWinTXTTitle">DESCRIZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVVEDIMENTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA INIZIO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FINE</span></td>
			
			</tr>
	
			<% PubblicitaTestata t = new PubblicitaTestata(); %>
			<% java.util.Enumeration en = vlista.elements(); %>
			<% int contatore = 1; %>
			<% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
			<% while (en.hasMoreElements()) {
		        t = (PubblicitaTestata)en.nextElement();
		       System.out.println("Chiave, Progressivo: " + t.getChiave() + ", " + progressivoRecord);
		       
		       int rowspan = t.getLstDettaglio().size();
		       rowspan = rowspan > 0 ? rowspan : 1;
		       
		        %>

	    		<tr id="r<%=contatore%>">
					<td onclick="vai('<%=t.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan%>>
						<span class="extWinTXTData"><%=t.getTipoPratica()!=null ? t.getTipoPratica() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan%>>
						<span class="extWinTXTData"><%=t.getNumPratica()!=null ? t.getNumPratica() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan%>>
						<span class="extWinTXTData"><%=t.getAnnoPratica()!=null ? t.getAnnoPratica() : "-"%></span>
					</td>
					<td onclick="vai('<%=t.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan%>>
						<span class="extWinTXTData"><%= t.getDtInizio()== null ||
														t.getDtInizio().equals("") ||
														t.getDtInizio().equals("-") ||
														t.getDtInizio().equals("01/01/0001") ? "-" : t.getDtInizio()%></span>
					</td>
					<td onclick="vai('<%=t.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan%>>
						<span class="extWinTXTData"><%= t.getDtDecTermini()== null ||
														t.getDtDecTermini().equals("") ||
														t.getDtDecTermini().equals("-") ||
														t.getDtDecTermini().equals("31/12/9999") ? "ATTUALE" : t.getDtDecTermini()%></span>
					</td>
					<td onclick="vai('<%=t.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan%>>
						<span class="extWinTXTData"><%=t.getDescPratica()!=null ? t.getDescPratica() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan%>>
						<span class="extWinTXTData"><%=t.getProvvedimento()!=null ? t.getProvvedimento() : "-" %></span>
					</td>
						<%
						
						 String dettagli = "-";
						 int index = 0;
				        if (t.getLstDettaglio() != null && t.getLstDettaglio().size() > 0) {
				        	dettagli = "";
				        	
				        	if(index!=0){ %>
				        		
				        		<tr>
				        		
				        <% } 
				        	for (PubblicitaDettaglio det : t.getLstDettaglio()) {
				        		String dtInizio = det.getDtInizio()== null ||
										det.getDtInizio().equals("") ||
										det.getDtInizio().equals("-") ||
										det.getDtInizio().equals("01/01/0001") ? "-" : det.getDtInizio();
				        		
				        		String dtFine = det.getDtFine()== null ||
										det.getDtFine().equals("") ||
										det.getDtFine().equals("-") ||
										det.getDtFine().equals("31/12/9999") ? "ATTUALE" : det.getDtFine();
												
				        		String civico = det.getCivico()!=null ? det.getCivico() : "";
				        		
				        		String indirizzo = det.getVia()!=null ? (det.getVia() +" "+civico).trim() : "-";
				        	
				        	
				        	%>
				        	
				        	
				        	<td class="extWinTDData"><span class="extWinTXTData"><%=indirizzo%></span></td>
				        	<td class="extWinTDData"><span class="extWinTXTData" style="text-align: center;"><%=det.getDescClasse()%></span></td>
				        	<td class="extWinTDData"><span class="extWinTXTData"><%=dtInizio%></span></td>
				        	<td class="extWinTDData"><span class="extWinTXTData"><%=dtFine%></span></td>
				        	</tr>
				        	<% } %>
				        	
				        	
				        <% } %>
						
						
					
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
				<input type='hidden' name="UC" value="115">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>