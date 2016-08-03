<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@page import="java.util.*"%>
<%@page import="it.escsolution.escwebgis.tributiNew.bean.*"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>

<%  HttpSession sessione = request.getSession(true);  %> 
<%  Vector vlistaTARSU= (Vector)sessione.getAttribute("LISTA_TARSU"); %>
<%  OggettiTARSUNewFinder finder = (OggettiTARSUNewFinder)sessione.getAttribute("FINDER109"); %>
<%  boolean soloAtt = ((Boolean)sessione.getAttribute(TributiOggettiTARSUNewLogic.SOLO_ATT)).booleanValue(); %>
<%  int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		
<%@page import="it.escsolution.escwebgis.tributiNew.logic.TributiOggettiTARSUNewLogic"%>
<html>
	<head>
		<title>Tributi Oggetti TARSU - Lista</title>
		<LINK rel="stylesheet" href="<%= EscServlet.URL_PATH %>/styles/style.css" type="text/css">
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>

	<script>
	
		function chgtr(row,flg)
			{
			if (flg==1)
				document.all("r"+row).style.backgroundColor = "#d7d7d7";
			else
				document.all("r"+row).style.backgroundColor = "";
			}
		
		
		function vai(chiave, record_cliccato)
			{
			wait();
			document.mainform.OGGETTO_SEL.value=chiave;
			document.mainform.RECORD_SEL.value=record_cliccato;
			document.mainform.ST.value = 3;
			document.mainform.submit();
			}
		
		function mettiST()
			{
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
					popUpAperte[index] = window.open("", "PopUpOggTARSUNewDetail" + index, "width=900,height=480,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpOggTARSUNewDetail" + index, "width=900,height=480,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
		
		<% 
		if (vlistaTARSU.size() > 0)
		 {
			OggettiTARSUNew pTARSU=(it.escsolution.escwebgis.tributiNew.bean.OggettiTARSUNew)vlistaTARSU.get(0);
		%>
			function vaiPrimo(){
			 	document.mainform.OGGETTO_SEL.value='<%=pTARSU.getChiave()%>';
				document.mainform.RECORD_SEL.value=1;
				document.mainform.ST.value = 3;
				document.mainform.submit();
			}
		<%}%>
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= EscServlet.URL_PATH %>/TributiOggettiTARSUNew" target="_parent" >
	 
			<div align="center" class="extWinTDTitle">
				<span class="extWinTXTTitle">&nbsp;<%=funzionalita%>:&nbsp;Elenco Oggetti TARSU</span>
			</div>
				
			&nbsp;
		               
		   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		
				<tr>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">SUPERFICIE TOTALE</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA INIZIO</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FINE</span></td>
					<td class="extWinTDTitle"><span class="extWinTXTTitle">PROVENIENZA</span></td>
					<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">MAPPA</span></td>
				</tr>
		
			  <% OggettiTARSUNew tarsu = new OggettiTARSUNew(); %>
			  <% Enumeration en = vlistaTARSU.elements(); int contatore = 1; %>
			  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
			  <% while (en.hasMoreElements()) {
			        tarsu = (OggettiTARSUNew)en.nextElement();%>
			
			    <tr id="r<%=contatore%>" <%if (tarsu.isEvidenzia() && !soloAtt) {%> style = "color:green; font-weight:bold;" <%} %>>
					<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
					<span class="extWinTXTData"><%=tarsu.getFoglio()%></span></td>
					<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
					<span class="extWinTXTData"><%=tarsu.getNumero()%></span></td>	
					<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;'>
					<span class="extWinTXTData"><%=tarsu.getSub()%></span></td>	
					<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor:pointer;'>
					<span class="extWinTXTData"><%=tarsu.getDesClsRsu()%></span></td>	
					<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer; text-align: right;'>
					<span class="extWinTXTData"><%=tarsu.getSupTot()%></span></td>		
					<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer; text-align: center;'>
					<span class="extWinTXTData"><%=tarsu.getDatIni()%></span></td>
					<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer; text-align: center;'>
					<span class="extWinTXTData"><%=tarsu.getDatFin()%></span></td>		
					<td onclick="vai('<%=tarsu.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer; text-align: center;'>
					<span class="extWinTXTData"><%=tarsu.getProvenienza()%></span></td>
					<td onclick="zoomInMappaParticelle('<%= tarsu.getCodEnte() %>','<%=tarsu.getFoglio()%>','<%=tarsu.getNumero()%>');" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor:pointer;text-align:center; border-style: none; padding-right: 10px;'>
					<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif" border="0"/></span></td>
					<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupVirtualH(<%=tarsu.getLatitudine()==null?0:tarsu.getLatitudine()%>,<%=tarsu.getLongitudine()==null?0:tarsu.getLongitudine()%>);">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
							</a>
						</span>
					</td>
					<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupStreetview(<%=tarsu.getLatitudine()==null?0:tarsu.getLatitudine()%>,<%=tarsu.getLongitudine()==null?0:tarsu.getLongitudine()%>);">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
							</a>
						</span>
					</td>
				</tr>
				
			  <% contatore++;progressivoRecord ++;} %>
			</table>
	
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="OGGETTO_SEL" value="">
			<input type='hidden' name="RECORD_SEL" value="">
			<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="UC" value="109">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
	
		</form>

		<div id="wait" class="cursorWait" /></div>

	</body>
</html>