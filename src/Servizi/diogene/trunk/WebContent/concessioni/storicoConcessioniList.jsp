<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%	java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic.LISTA); 
 	
 	it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniFinder finder = null;
 	if (sessione.getAttribute(it.escsolution.escwebgis.concessioni.servlet.StoricoConcessioniServlet.NOMEFINDER)!= null){
 		finder = (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniFinder)sessione.getAttribute(it.escsolution.escwebgis.concessioni.servlet.StoricoConcessioniServlet.NOMEFINDER); 
 	}else 
 		finder = new it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniFinder();
 	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
 %>
<%  it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%  int js_back = 1;
	  if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<html>
	<head>
		<title>Storico Concessioni - Lista</title>
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
	
	
			function vai(codice, record_cliccato)
			{
				<% if (request.getParameter("popup") != null && new Boolean(request.getParameter("popup")).booleanValue()) { %>
					document.mainform.popup.value='true';					
				<% } else {%>
					wait();
				<% }%>			
				document.mainform.OGGETTO_SEL.value=codice;
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
							document.mainform.popup2.value='true';	
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
						popUpAperte[index] = window.open("", "PopUpStoricoConcessioniDetail" + index, "width=700,height=480,status=yes,resizable=yes");
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
					popUpAperte[index] = window.open("", "PopUpStoricoConcessioniDetail" + index, "width=700,height=480,status=yes,resizable=yes");
					return popUpAperte[index].name;
				}
			}
						
	
	<% if (vlista.size() > 0){
		it.escsolution.escwebgis.concessioni.bean.StoricoConcessioni con = (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioni)vlista.get(0);
	%>
		function vaiPrimo(){
		 	document.mainform.OGGETTO_SEL.value='<%=con.getChiave()%>';
			document.mainform.RECORD_SEL.value=1;
			document.mainform.ST.value = 3;
			document.mainform.submit();
		}
	<%}%>	
	</script>
	
	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/StoricoConcessioni" target="_parent" >
		
			<input type='hidden' name="popup" value="false">
			<input type='hidden' name="popup2" value="false">
		
			<div align="center" class="extWinTDTitle">
				<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;					
					<% if (request.getParameter("collegate") != null && new Boolean(request.getParameter("collegate")).booleanValue()) { %>
						Elenco Concessioni collegate
					<% } else {%>
						Elenco Concessioni
					<% }%>
				</span>
			</div>
			&nbsp;
		   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				<tr>
				    <td class="extWinTDTitle"><span class="extWinTXTTitle">Conc./Fascicolo</span></td>	
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Prog.</span></td>			
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Oggetto</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Soggetti</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">Provenienza</span></td>
				</tr>
				<% it.escsolution.escwebgis.concessioni.bean.StoricoConcessioni con = new it.escsolution.escwebgis.concessioni.bean.StoricoConcessioni();
				   java.util.Enumeration en = vlista.elements();
				   int contatore = 1;  
				   long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1;
				%>
			<% while (en.hasMoreElements()) 
			   {
				con = (it.escsolution.escwebgis.concessioni.bean.StoricoConcessioni)en.nextElement();%>
			    <tr id="r<%=contatore%>" onclick="vai('<%=con.getChiave()%>', '<%=progressivoRecord%>', true)">
			    	<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<p><span class="extWinTXTData"><%=con.getConcessioneNumero()==null?"-":con.getConcessioneNumero()%></span></p></td>				
			    	<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<p><span class="extWinTXTData"><%=con.getProgressivoAnno()==null?"-":con.getProgressivoAnno()%></span></p></td>
			    	<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<p><span class="extWinTXTData"><%=con.getProgressivoNumero()==null?"-":con.getProgressivoNumero()%></span></p></td>					
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<p><span class="extWinTXTData"><%=con.getOggettoSubstr()==null?"-":con.getOggettoSubstr()%></span></p></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<p><span class="extWinTXTData"><%=con.getSoggetti()==null?"-":con.getSoggetti()%></span></p></td>
					<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
					<p><span class="extWinTXTData"><%=con.getProvenienza()==null?"-":con.getProvenienza()%></span></p></td>
				</tr>
				<% 	contatore++;
					progressivoRecord ++;%>
				<% }%>
			
				<input type='hidden' name="ST" value="">
				<input type='hidden' name="OGGETTO_SEL" value="">
				<input type='hidden' name="RECORD_SEL" value="">
				<% if (finder != null){%>
					<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
				<%}%>
				<input type='hidden' name="AZIONE" value="">
				<input type='hidden' name="UC" value="53">
				<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
				<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
				<% if (request.getParameter("collegate") != null && new Boolean(request.getParameter("collegate")).booleanValue()) { %>
					<input type='hidden' name="collegate" value="true">
					<input type='hidden' name="conId" value="<%=request.getParameter("conId")%>">
					<input type='hidden' name="fg" value="<%=request.getParameter("fg")%>">
					<input type='hidden' name="part" value="<%=request.getParameter("part")%>">
					<input type='hidden' name="sub" value="<%=request.getParameter("sub")%>">
					<input type='hidden' name="cod_ente" value="<%=request.getParameter("cod_ente")%>">
				<% } %>
			</table>
			
			<% if (request.getParameter("popup") != null && new Boolean(request.getParameter("popup")).booleanValue()) { %>
				&nbsp;
				<div align="center"><span class="extWinTXTTitle">
					<a class="iFrameLink" href="javascript:window.close();">chiudi questa finestra</a></span>
				</div>
			<% } %>
			
			
		</form>
		<div id="wait" class="cursorWait" />
	</body>
</html>