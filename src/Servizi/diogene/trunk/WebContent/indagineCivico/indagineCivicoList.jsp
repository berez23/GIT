<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaCivici=(java.util.Vector)sessione.getAttribute(it.escsolution.escwebgis.indagineCivico.logic.IndagineCivicoLogic.LISTA_CIVICI); %>
<% it.escsolution.escwebgis.indagineCivico.bean.IndagineCivicoFinder finder = (it.escsolution.escwebgis.indagineCivico.bean.IndagineCivicoFinder)sessione.getAttribute(IndagineCivicoLogic.FINDER); %>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
		
<%@page import="it.escsolution.escwebgis.indagineCivico.logic.IndagineCivicoLogic"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.escsolution.escwebgis.indagineCivico.bean.Indirizzo"%>
<html>
<head>
<title>IndagineCivico - Lista Civici</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.jsp" type="text/css">
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



function vai(idCivi, record_cliccato){
	wait();
	document.mainform.OGGETTO_SEL.value=idCivi;
	document.mainform.RECORD_SEL.value=record_cliccato;
	<%java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>

	<%if (ST.equals("2")){%>
		document.mainform.ST.value = 3;
	<%}else{%>
		document.mainform.ST.value = 5;
	<%}%>
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
			popUpAperte[index] = window.open("", "PopUpFascicoloCivicoDetail" + index, "width=900,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpFascicoloCivicoDetail" + index, "width=900,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}

function openCloseFp(idx) {
	document.getElementById("tableFp" + idx).style.display = document.getElementById("tableFp" + idx).style.display == "" ? "none" : "";
	document.getElementById("tableFps" + idx).style.display = document.getElementById("tableFps" + idx).style.display == "" ? "none" : "";
}


<% if (vlistaCivici.size() > 0){
	it.escsolution.escwebgis.indagineCivico.bean.Indirizzo pCivico=(it.escsolution.escwebgis.indagineCivico.bean.Indirizzo)vlistaCivici.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pCivico.getChiave().replaceAll("'","\\\\'")%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>
</script>

<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/IndagineCivico" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Elenco Indirizzi</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle" style="width: 15%;"><span class="extWinTXTTitle">Prefisso<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle" style="width: 65%;"><span class="extWinTXTTitle">Nome Via<span class="extWinTXTTitle"></td>
		<td class="extWinTDTitle" style="width: 15%;"><span class="extWinTXTTitle">Civico</span></td>
		<td class="extWinTDTitle" style="width: 5%;"><span class="extWinTXTTitle">Mappa</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio / Particella</span></td>
	</tr>
        
        

  <% it.escsolution.escwebgis.indagineCivico.bean.Indirizzo civico = new it.escsolution.escwebgis.indagineCivico.bean.Indirizzo(); %>
  <% java.util.Enumeration en = vlistaCivici.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        civico = (it.escsolution.escwebgis.indagineCivico.bean.Indirizzo)en.nextElement();%>



    <tr id="r<%=contatore%>" >

		<td onclick="vai('<%=civico.getChiave().replaceAll("'","\\\\'")%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
		<span class="extWinTXTData"><%=civico.getPrefisso()%></span></td>
		<td onclick="vai('<%=civico.getChiave().replaceAll("'","\\\\'")%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=civico.getNomeVia()%></span></td>
		<td onclick="vai('<%=civico.getChiave().replaceAll("'","\\\\'")%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=civico.getCivico()%></span></td>
		<% if (civico.hasFps()) {%>
			<td onclick="zoomInMappaCivici('<%= civico.getCodNazionale() %>','<%=civico.getPkidCivi()%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; padding-left: 5px; padding-right: 5px;'>
				<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif" border="0"/>
			</td>
			<td class="TDviewImage" style="border-color: #00c5fd">
				<table id="tableFp<%=contatore%>" style="width: 100%; display:;" cellpadding=0 cellspacing=0 border=0>
				  <% Indirizzo fp0 = civico.getFps().get(0); %>
					<tr>
						<td onclick="vai('<%=civico.getChiave().replaceAll("'","\\\\'")%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; padding-left: 5px; padding-right: 5px;'>
							<span class="extWinTXTData">
								<%=fp0.getFoglio()%>
							</span>
						</td>
						<td onclick="vai('<%=civico.getChiave().replaceAll("'","\\\\'")%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; padding-left: 5px; padding-right: 5px;'>
							<span class="extWinTXTData">
								<%=fp0.getParticella()%>
							</span>
						</td>
						<td class="extWinTDData" style="text-align:center; border-style: none; padding-left: 5px; padding-right: 5px;">
							<span class="extWinTXTData">
								<a href="javascript:apriPopupVirtualH(<%=fp0.getLatitudine()==null?0:fp0.getLatitudine()%>,<%=fp0.getLongitudine()==null?0:fp0.getLongitudine()%>);">
								<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
								</a>
							</span>
						</td>
						<td class="extWinTDData" style="text-align:center; border-style: none; padding-left: 5px; padding-right: 5px;">
							<span class="extWinTXTData">
								<a href="javascript:apriPopupStreetview(<%=fp0.getLatitudine()==null?0:fp0.getLatitudine()%>,<%=fp0.getLongitudine()==null?0:fp0.getLongitudine()%>);">
								<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
								</a>
							</span>
						</td>
						<td style="width: 100%; text-align:center; border-style: none;" class="extWinTDData">
							<span class="extWinTXTData">
								<%if (civico.getFps().size() > 1) {%>
									<a href="javascript:openCloseFp(<%=contatore%>);">
										<img src="../images/FrecciaGiu.gif" border="0" width="30" height="20" title="vedi altre particelle"/>
									</a>
								<%} else {%>
									&nbsp;
								<%} %>
							</span>
						</td>
					</tr>				
				</table>
				<table id="tableFps<%=contatore%>" style="width: 100%; display: none;" cellpadding=0 cellspacing=0 border=0>
				  <% ArrayList<Indirizzo> fps = civico.getFps(); %>
				  <% int idxFp = 0; %>
				  <% for (Indirizzo fp : fps) { %>
						<tr>
							<td onclick="vai('<%=civico.getChiave().replaceAll("'","\\\\'")%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; padding-left: 5px; padding-right: 5px;'>
								<span class="extWinTXTData">
									<%=fp.getFoglio()%>
								</span>
							</td>
							<td onclick="vai('<%=civico.getChiave().replaceAll("'","\\\\'")%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer; padding-left: 5px; padding-right: 5px;'>
								<span class="extWinTXTData">
									<%=fp.getParticella()%>
								</span>
							</td>
							<td class="extWinTDData" style="text-align:center; border-style: none; padding-left: 5px; padding-right: 5px;">
								<span class="extWinTXTData">
									<a href="javascript:apriPopupVirtualH(<%=fp.getLatitudine()==null?0:fp.getLatitudine()%>,<%=fp.getLongitudine()==null?0:fp.getLongitudine()%>);">
										<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
									</a>
								</span>
							</td>
							<td class="extWinTDData" style="text-align:center; border-style: none; padding-left: 5px; padding-right: 5px;">
								<span class="extWinTXTData">
									<a href="javascript:apriPopupStreetview(<%=fp.getLatitudine()==null?0:fp.getLatitudine()%>,<%=fp.getLongitudine()==null?0:fp.getLongitudine()%>);">
										<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
									</a>
								</span>
							</td>
							<td style="width: 100%; text-align:center; border-style: none;" class="extWinTDData">
								<span class="extWinTXTData">
									<%if (civico.getFps().size() > 1 && idxFp == 0) {%>
										<a href="javascript:openCloseFp(<%=contatore%>);">
											<img src="../images/FrecciaSu.gif" border="0" width="30" height="20" title="chiudi"/>
										</a>
									<%} else {%>
										&nbsp;
									<%} %>
								</span>
							</td>
						</tr>
					<%	idxFp++; 
					} %>					
				</table>
			</td>
		<%} else { %>
			<td colspan="2" onclick="vai('<%=civico.getChiave().replaceAll("'","\\\\'")%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
				<span class="extWinTXTData">
					&nbsp;
				</span>
			</td>
		<%}%>
	</tr>
		
  <% contatore++;progressivoRecord ++;} %>
  
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="111">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>