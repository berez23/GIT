<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>

<html>
<head>
<title>Catasto</title>
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
<%if(ST.equals("1")){%>
<frameset rows="0,*" border="0">
<%}else{%>
<frameset rows="0,*,30" border="0">
<%}%>
	<frame name="hidden" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/common/hidden.jsp" noresize scrolling="NO" class="frame">
	
	<%if(ST.equals("1")){%>
	<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/common/search.jsp" noresize class="frame">
	<%}else if(ST.equals("2")){%>
	<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/catasto/cimList.jsp" noresize class="frame">
	<%} else {%>
	<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/catasto/cimDetail.jsp" noresize class="frame">
	<%}%>
	<%if((ST.equals("2"))||(ST.equals("3"))){%>
	<frame name="barraMoveRecord" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/frame/barraMoveRecord.jsp" noresize scrolling="NO" class="frame">
<%}%>
</frameset>


</html>