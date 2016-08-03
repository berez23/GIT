<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vct_lista_Versamenti = (java.util.Vector)sessione.getAttribute("LISTA_VERSAMENTI"); %>
<% it.escsolution.escwebgis.tributiNew.bean.VersamentiNewFinder finder = (it.escsolution.escwebgis.tributiNew.bean.VersamentiNewFinder)sessione.getAttribute("FINDER112"); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		
<html>
	<head>
		<title>Versamenti - Lista</title>
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
	
	
		<% if (vct_lista_Versamenti.size() > 0){
		it.escsolution.escwebgis.tributiNew.bean.VersamentiNew versamenti = (it.escsolution.escwebgis.tributiNew.bean.VersamentiNew)vct_lista_Versamenti.get(0);%>
		function vaiPrimo(){
		 	document.mainform.OGGETTO_SEL.value='<%=versamenti.getChiave()%>';
			document.mainform.RECORD_SEL.value = 1;
			document.mainform.ST.value = 3;
			document.mainform.submit();
			}
		<%}%>
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/VersamentiNew" target="_parent" >

		<div align="center" class="extWinTDTitle">
			<span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Elenco Versamenti
			</span>
		</div>
		
		&nbsp;
		
		
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE FISCALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA VERSAMENTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. TERRENI AGRIC.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. AREE FABBR.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. PRIMA CASA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. FABBR.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. DETRAZ.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. TOTALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO PAG.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>
			</tr>
	
			<% it.escsolution.escwebgis.tributiNew.bean.VersamentiNew versamenti = new it.escsolution.escwebgis.tributiNew.bean.VersamentiNew(); %>
			<% java.util.Enumeration en = vct_lista_Versamenti.elements(); 
				int contatore = 1;
				long progressivoRecord = (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() + 1; %>
			<% while (en.hasMoreElements()) {
		        versamenti = (it.escsolution.escwebgis.tributiNew.bean.VersamentiNew)en.nextElement();%>

	    		<tr id="r<%=contatore%>">
					<td class="extWinTDData">
						<span class="extWinTXTData"><%=versamenti.getCodFisc()%></span>
					</td>
					<td class="extWinTDData">
						<span class="extWinTXTData"><%=versamenti.getYeaRif()%></span>
					</td>
					<td class="extWinTDData">
						<span class="extWinTXTData"><%=versamenti.getDatPag()%></span>
					</td>
					<td class="extWinTDData" style="text-align: right;">
						<span class="extWinTXTData"><%=versamenti.getImpTerAgrEu()%></span>
					</td>
					<td class="extWinTDData" style="text-align: right;">
						<span class="extWinTXTData"><%=versamenti.getImpAreFabEu()%></span>
					</td>
					<td class="extWinTDData" style="text-align: right;">
						<span class="extWinTXTData"><%=versamenti.getImpAbiPriEu()%></span>
					</td>
					<td class="extWinTDData" style="text-align: right;">
						<span class="extWinTXTData"><%=versamenti.getImpAltFabEu()%></span>
					</td>
					<td class="extWinTDData" style="text-align: right;">
						<span class="extWinTXTData"><%=versamenti.getImpDtrEu()%></span>
					</td>
					<td class="extWinTDData" style="text-align: right;">
						<span class="extWinTXTData"><%=versamenti.getImpPagEu()%></span>
					</td>
					<td class="extWinTDData" style="text-align: center;">
						<span class="extWinTXTData"><%=versamenti.getTipPag()%></span>
					</td>
					<td class="extWinTDData" style="text-align: center;">
						<span class="extWinTXTData"><%=versamenti.getProvenienza()%></span>
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
				<input type='hidden' name="UC" value="112">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>