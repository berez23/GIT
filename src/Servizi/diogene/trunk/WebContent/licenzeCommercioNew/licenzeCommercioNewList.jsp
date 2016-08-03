<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%  

	java.util.Vector vlistaliccom =(java.util.Vector)sessione.getAttribute(LicenzeCommercioNewLogic.LISTA); 
	
	LicenzeCommercioNewFinder finder = null;
	if (sessione.getAttribute(LicenzeCommercioNewServlet.NOMEFINDER)!= null){
		finder = (LicenzeCommercioNewFinder)sessione.getAttribute(LicenzeCommercioNewServlet.NOMEFINDER); 
	}else 
		finder = new LicenzeCommercioNewFinder();
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>

<%@page import="it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioNew"%>
<%@page import="it.escsolution.escwebgis.licenzeCommercioNew.logic.LicenzeCommercioNewLogic"%>
<%@page import="it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioNewFinder"%>
<%@page import="it.escsolution.escwebgis.licenzeCommercioNew.servlet.LicenzeCommercioNewServlet"%>
<html>
	<head>
		<title>Licenze di Commercio - Lista</title>
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

		function vai(codice, record_cliccato){
			wait();
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
					popUpAperte[index] = window.open("", "PopUpLicenzeCommercioDetail" + index, "width=700,height=480,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpLicenzeCommercioDetail" + index, "width=700,height=480,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
			
	
		<% if (vlistaliccom.size() > 0){
			LicenzeCommercioNew pLicCom = (LicenzeCommercioNew)vlistaliccom.get(0);%>
			function vaiPrimo(){
			 	document.mainform.OGGETTO_SEL.value='<%=pLicCom.getIdExt()%>';
				document.mainform.RECORD_SEL.value=1;
				document.mainform.ST.value = 3;
				document.mainform.submit();
			}
		<%}%>
	
	</script>
	
	<body onload="mettiST()">
	
		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/LicenzeCommercioNew" target="_parent" >
	 
		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Elenco Licenze di Commercio</span>
		</div>
				
		&nbsp;
		               
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUMERO<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PROTOCOLLO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPOLOGIA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">SUPERFICIE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATI CATASTALI</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FABBRICATO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">RAGIONE SOCIALE</span></td>
			</tr>
			
			<% LicenzeCommercioNew lic = new LicenzeCommercioNew(); 
			   java.util.Enumeration en = vlistaliccom.elements();
		  	   int contatore = 1; 
			   long progressivoRecord = (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() + 1; %>
		  <% 
		     while (en.hasMoreElements()) {
		        lic = (LicenzeCommercioNew)en.nextElement();
		        String protocollo = (lic.getNumeroProtocollo() == null ? "" : lic.getNumeroProtocollo())
		        + "/"
		        + (lic.getAnnoProtocollo() == null ? "" : lic.getAnnoProtocollo());
		        if (protocollo.equals("/")) {
		        	protocollo = "";
		        }
		        String indirizzo = lic.getIndirizzo() == null ? "" : lic.getIndirizzo();
		     	if (lic.getCivico() != null && !lic.getCivico().equals("")) {
		     		if (!indirizzo.equals("")) {
		     			indirizzo += ", ";
		     		}
		     		indirizzo += lic.getCivico();
		     	}
		     	String superficie = lic.getSuperficieMinuto() == null || lic.getSuperficieMinuto().equals("") ? "" : 
		     						"minuto: " + lic.getSuperficieMinuto() + " mq.";
		     	if (lic.getSuperficieTotale() != null && !lic.getSuperficieTotale().equals("")) {
		     		if (!superficie.equals("")) {
		     			superficie += "<br>";
		     		}
		     		superficie += ("totale: " + lic.getSuperficieTotale() + " mq.");
		     	}
		     	String datiCatastali = (lic.getSezioneCatastale() == null || lic.getSezioneCatastale().equals("")) ? "" : 
						"sezione: " + lic.getSezioneCatastale();
		     	if (lic.getFoglio() != null && !lic.getFoglio().equals("")) {
		     		if (!datiCatastali.equals("")) {
		     			datiCatastali += "<br>";
		     		}
		     		datiCatastali += ("foglio: " + lic.getFoglio());
		     	}
		     	if (lic.getParticella() != null && !lic.getParticella().equals("")) {
		     		if (!datiCatastali.equals("")) {
		     			datiCatastali += "<br>";
		     		}
		     		datiCatastali += ("particella: " + lic.getParticella());
		     	}
		     	if (lic.getSubalterno() != null && !lic.getSubalterno().equals("")) {
		     		if (!datiCatastali.equals("")) {
		     			datiCatastali += "<br>";
		     		}
		     		datiCatastali += ("subalterno: " + lic.getSubalterno());
		     	}
		        %>
		        
			    <tr id="r<%=contatore%>">
					
					<td onclick="vai('<%=lic.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=lic.getNumero()%></span></td>
					<td onclick="vai('<%=lic.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=protocollo%></span></td>
					<td onclick="vai('<%=lic.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=lic.getTipologia()%></span></td>	
					<td onclick="vai('<%=lic.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=indirizzo%></span></td>
					<td onclick="vai('<%=lic.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=superficie%></span></td>
					<td onclick="vai('<%=lic.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=datiCatastali%></span></td>
					<td onclick="vai('<%=lic.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=lic.getCodiceFabbricato()%></span></td>
					<td onclick="vai('<%=lic.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=lic.getRagSoc()%></span></td>
				</tr>
			
				<% contatore++;
				   progressivoRecord++;
			} %>
		
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="OGGETTO_SEL" value="">
		<input type='hidden' name="RECORD_SEL" value="">
		<% if (finder != null){%>
		<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
		<%}%>
		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="UC" value="55">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		</table>
		</form>
		<div id="wait" class="cursorWait" />
	</body>
</html>