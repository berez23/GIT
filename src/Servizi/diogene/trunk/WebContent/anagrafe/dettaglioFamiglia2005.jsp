<%@ page language="java" import="it.escsolution.escwebgis.anagrafe.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   String pos = request.getParameter("pos");  %> 
<%   HttpSession sessione = request.getSession(true);  %> 
<%   java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(it.escsolution.escwebgis.anagrafe.logic.AnagrafeStorico2005Logic.ANAGRAFE_STORICO); %>
<%  it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico ana = (it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico)vlista.get(new Integer(pos).intValue()-1);%>
<%  java.util.ArrayList listaFam = ana.getElencoFamiliari();%>

<html>
<head>
<title>Dettaglio Famiglia</title>
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


</script>
<body >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Storico Anagrafe 2005 - Dettaglio Famiglia</span> 
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA DI NASCITA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO PARENTELA</span></td>
	</tr>
	
	<% FamiliariStorico fam = new FamiliariStorico(); %>
  <% int contatore = 1;%>
  <% for (int i =0;i<listaFam.size();i++) {
        fam = (FamiliariStorico)listaFam.get(i);%>
    	<tr id="r<%=contatore%>">
		
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  >
		<span class="extWinTXTData"><%=fam.getCognome()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=fam.getNome()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=fam.getDtNascita()%></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' >
		<span class="extWinTXTData"><%=fam.getTipoParentela()%></span></td>	
		</tr>
	
<% 		contatore++;
	} 
%>

</table>

</body>
</html>