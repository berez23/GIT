<%@ page language="java" import="it.escsolution.escwebgis.anagrafe.bean.*, java.text.SimpleDateFormat"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   String pos = request.getParameter("pos");  %> 
<%   HttpSession sessione = request.getSession(true);  %> 
<%  
	java.util.Vector vlista = null;
	java.util.List llista = null;
	if (request.getParameter("REDDITI") != null && new Boolean(request.getParameter("REDDITI")).booleanValue()) {
		vlista = (java.util.Vector)sessione.getAttribute(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI_POPUP);
	}else if (request.getParameter("STORICO") != null && new Boolean(request.getParameter("STORICO")).booleanValue()) {
		llista = (java.util.List)sessione.getAttribute(AnagrafeDwhLogic.VIS_STORICO_LISTA);
	}else{
		vlista = (java.util.Vector)sessione.getAttribute(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI);
	}
%>
<%  it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico ana = null;
	if (vlista != null) {
		ana = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico)vlista.get(new Integer(pos).intValue()-1);
	} else if (llista != null) {
		ana = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico)llista.get(new Integer(pos).intValue()-1);
	}%>
<%  java.util.ArrayList listaFam = ana.getElencoFamiliari();%>

<%@page import="it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic"%>
<html>
<head>
<title>
	<% if (request.getParameter("STORICO") != null && new Boolean(request.getParameter("STORICO")).booleanValue()) { %>
		Dettaglio Famiglia Elaborato
	<% } else { %>
		Dettaglio Famiglia
	<% } %>
</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
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
				popUpAperte[index] = window.open("", "PopUpAnaDwhDetailFamiglia" + index, "width=640,height=480,status=yes,resizable=yes");
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
			popUpAperte[index] = window.open("", "PopUpAnaDwhDetailFamiglia" + index, "width=640,height=480,status=yes,resizable=yes");
			return popUpAperte[index].name;
		}
	}

</script>
<body>

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/AnagrafeDwh" target="_parent" >
 
  <% FamiliariStorico fam = new FamiliariStorico(); %>
  <% String dataIniRif = "-"; %>
  <% String dataFinRif = "-"; %>
  <% SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy"); %>
  <% if (listaFam.size()>0) {
	  fam = (FamiliariStorico)listaFam.get(0);
	  dataIniRif = fam.getDataInizioRif() != null && !SDF.format(fam.getDataInizioRif()).equals("01/01/1000") && !SDF.format(fam.getDataInizioRif()).equals("01/01/1001") ? SDF.format(fam.getDataInizioRif()) : "ORIGINE";
	  dataFinRif = fam.getDataFineRif() != null && !SDF.format(fam.getDataFineRif()).equals("31/12/9999") ? SDF.format(fam.getDataFineRif()) : "OGGI";
  }
  %>
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Situazione della famiglia da <%=dataIniRif%> a <%=dataFinRif%> </span> 
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA DI NASCITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO PARENTELA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA VALIDITA' INIZIALE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA VALIDITA' FINALE</span></td>
	</tr>
	
  <% int contatore = 1;%>
  <% for (int i =0;i<listaFam.size();i++) {
        fam = (FamiliariStorico)listaFam.get(i);%>
    	<tr id="r<%=contatore%>">
		
		<td onclick="vai('<%=fam.getId()%>', '<%=contatore%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=fam.getCognome()%></span></td>
		<td onclick="vai('<%=fam.getId()%>', '<%=contatore%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=fam.getNome()%></span></td>
		<td onclick="vai('<%=fam.getId()%>', '<%=contatore%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=fam.getDtNascita()%></span></td>
		<td onclick="vai('<%=fam.getId()%>', '<%=contatore%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=fam.getTipoParentela()%></span></td>	
		<td onclick="vai('<%=fam.getId()%>', '<%=contatore%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=fam.getDtInizio()%></span></td>	
		<td onclick="vai('<%=fam.getId()%>', '<%=contatore%>', true)", class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=fam.getDtFine()%></span></td>	
		</tr>
	
<% 		contatore++;
	} 
%>

</table>

&nbsp;
<% if (request.getParameter("REDDITI") != null && new Boolean(request.getParameter("REDDITI")).booleanValue()) { %>
	<div align="center"><span class="extWinTXTTitle">
		<a class="iFrameLink" href="javascript:window.close();">chiudi questa finestra</a></span>
	</div>
<%}%>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">

</form>

</body>
</html>