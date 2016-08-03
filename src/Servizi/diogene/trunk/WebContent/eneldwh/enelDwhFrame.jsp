<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>

<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<html>
<head>
<title>Enel</title>
<LINK rel="STYLESHEET" type="text/css" href="styles/styleFrame.css">

<script>
function goBack(numeroBack){
	history.go(numeroBack);
}
function ricarica(){
	document.location.reload();
}
</script>

</head>
<%if(ST.equals("33") || ST.equals("102") || ST.equals("103") || ST.equals("104") || ST.equals("105")){%>
<frameset rows="*" border="0">
	<frame name="bodyPage" src="<%= request.getContextPath() %>/eneldwh/enelDwhDetail.jsp" noresize class="frame">
	<%
	} 
else 
{	
if(ST.equals("101")){%>
<frameset rows="*" border="0">
	<frame name="bodyPage" src="<%= request.getContextPath() %>/eneldwh/enelDwhList.jsp" noresize class="frame">
<%
} 
if(ST.equals("1")){%>
<frameset rows="0,30,*" border="0">
<%}else{%>
<frameset rows="0,30,*,30" border="0">
<%}%>
	<frame name="hidden" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/common/hidden.jsp" noresize scrolling="NO" class="frame">
	<frame name="barraTool" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/frame/barraTool.jsp" noresize scrolling="NO" class="frame">
	<!--frame name="barraLink" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/frame/barraLink.jsp" noresize scrolling="NO" class="frame"-->
	<%if(ST.equals("1")){%>
	<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/common/search.jsp" noresize class="frame">
	<%}else if(ST.equals("2")){%>
	<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/eneldwh/enelDwhList.jsp" noresize class="frame">
	<%} else if(ST.equals("3")){%>
	<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/eneldwh/enelDwhDetail.jsp" noresize class="frame">
	<%}%>
	<%if((ST.equals("2"))||(ST.equals("3"))){%>
	<frame name="barraMoveRecord" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/frame/barraMoveRecord.jsp" noresize scrolling="NO" class="frame">
<%}
}%>
</frameset>


</html>