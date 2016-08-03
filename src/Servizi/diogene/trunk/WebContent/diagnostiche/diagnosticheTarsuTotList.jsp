<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="sc" uri="/sqlcatalog"%>
<%@ page import="java.sql.*,java.util.*,org.displaytag.tags.TableTag "%>
<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% DiagnosticheTarsuTot diaTot=(DiagnosticheTarsuTot)sessione.getAttribute(DiagnosticheTarsuLogic.BEAN_DIAGNOSTICHE); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>  

<%@page import="it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheTarsuLogic"%>
<%@page import="it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheTarsuTot;"%>
<html>
<head>
<title>Diagnostiche Tarsu</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>

function chgtr(row,flg)
		{
		if (flg==1)
			{
			document.all("r"+row).style.backgroundColor = "#d7d7d7";
			document.all("r"+row).style.cursor = "#d7d7d7";
			}
		else
			{
			document.all("r"+row).style.backgroundColor = "";
			}
		}

function vaiListaPropRes(){
	wait();
	document.mainform.ST.value = 2;
	document.mainform.submit();
}

function mettiST(){
	document.mainform.ST.value = 2;
}


</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/DiagnosticheTarsu" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Lista Diagnostiche Globali Tarsu</span>
</div>
	

&nbsp;
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Proprietari Residenti<br>non risultanti in Tarsu</span></td>
		<td class="extWinTDTitle" ><span class="extWinTXTTitle">Proprietari Non Residenti<br>non risultanti in Tarsu</span></td>
		<td class="extWinTDTitle" ><span class="extWinTXTTitle">BHO</span></td>
	</tr>
	<%int contatore = 1; %>
	<tr id="r<%=contatore%>" >
		<td onclick="vaiListaPropRes();" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
			<span class="extWinTXTData"><%=diaTot.getPropResNoTarsu()%></span>
		</td>
		<td class="extWinTDData"><span class="extWinTXTData">-</span></td>
		<td class="extWinTDData"><span class="extWinTXTData">-</span></td>
	</tr>


<input type='hidden' name="ST" value="">
<input type='hidden' name="UC" value="104">
</table>
</form>
<br>

<div id="wait" class="cursorWait" />
</body>
</html>