<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaICI=(java.util.Vector)sessione.getAttribute("LISTA_ICI"); %>
<% it.escsolution.escwebgis.tributiNew.bean.OggettiICINewFinder finder = (it.escsolution.escwebgis.tributiNew.bean.OggettiICINewFinder)sessione.getAttribute("FINDER108"); %>
<% boolean soloAtt = ((Boolean)sessione.getAttribute(TributiOggettiICINewLogic.SOLO_ATT)).booleanValue(); %>
<% int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		

<%@page import="it.escsolution.escwebgis.tributiNew.logic.TributiOggettiICINewLogic"%>
<html>
	<head>
		<title>Tributi Oggetti ICI - Lista</title>
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
					popUpAperte[index] = window.open("", "PopUpOggICINewDetail" + index, "width=900,height=600,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpOggICINewDetail" + index, "width=900,height=600,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
	
		<% if (vlistaICI.size() > 0){
		it.escsolution.escwebgis.tributiNew.bean.OggettiICINew pICI=(it.escsolution.escwebgis.tributiNew.bean.OggettiICINew)vlistaICI.get(0);%>
		function vaiPrimo(){
		 	document.mainform.OGGETTO_SEL.value='<%=pICI.getChiave()%>';
			document.mainform.RECORD_SEL.value=1;
			document.mainform.ST.value = 3;
			document.mainform.submit();
			}
		<%}%>	
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiOggettiICINew" target="_parent" >

		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Elenco Oggetti ICI</span>
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
				<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">MAPPA<span class="extWinTXTTitle"></td>
			</tr>
	
			<% it.escsolution.escwebgis.tributiNew.bean.OggettiICINew ici = new it.escsolution.escwebgis.tributiNew.bean.OggettiICINew(); %>
			<% java.util.Enumeration en = vlistaICI.elements(); int contatore = 1; %>
			<% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
			<% while (en.hasMoreElements()) {
		        ici = (it.escsolution.escwebgis.tributiNew.bean.OggettiICINew)en.nextElement();%>

	    		<tr id="r<%=contatore%>" <%if (ici.isEvidenzia() && !soloAtt) {%> style = "color:green; font-weight:bold;" <%} %>>
					<td onclick="vai('<%=ici.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getFoglio()%></span></td>
					<td onclick="vai('<%=ici.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getNumero()%></span></td>	
					<td onclick="vai('<%=ici.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getSub()%></span></td>
					<td onclick="vai('<%=ici.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getCat()%></span></td>
					<td onclick="vai('<%=ici.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getCls()%></span></td>
					<td onclick="vai('<%=ici.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getNumDen()%></span></td>
					<td onclick="vai('<%=ici.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					<span class="extWinTXTData"><%=ici.getYeaDen()%></span></td>
					<td onclick="vai('<%=ici.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; text-align: center;'>
					<span class="extWinTXTData"><%=ici.getFlgPos3112()%></span></td>
					<td onclick="vai('<%=ici.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; text-align: center;'>
					<span class="extWinTXTData"><%=ici.getProvenienza()%></span></td>
					<td onclick="zoomInMappaParticelle('<%= ici.getCodEnte() %>','<%=ici.getFoglio()%>','<%=ici.getNumero()%>');" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;text-align:center; border-style: none; padding-right: 10px;'>
					<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif" border="0"/></span></td>
					<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupVirtualH(<%=ici.getLatitudine()==null?0:ici.getLatitudine()%>,<%=ici.getLongitudine()==null?0:ici.getLongitudine()%>);">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
							</a>
						</span>
					</td>
					<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupStreetview(<%=ici.getLatitudine()==null?0:ici.getLatitudine()%>,<%=ici.getLongitudine()==null?0:ici.getLongitudine()%>);">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
							</a>
						</span>
					</td>
				</tr>
	
				<% 
				contatore++;progressivoRecord ++;
			  	} 
			  	%>

				<input type='hidden' name="ST" value="">
				<input type='hidden' name="OGGETTO_SEL" value="">
				<input type='hidden' name="RECORD_SEL" value="">
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
				<input type='hidden' name="AZIONE" value="">
				<input type='hidden' name="UC" value="108">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
				
		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>