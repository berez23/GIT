<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>

<html>
<head>
<title>Catasto Intestatari Fisici</title>
<LINK rel="stylesheet"  href="styles/styleFrame.css" type="text/css">
<script>
function goBack(){
history.back();
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
	<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/catasto/intFisList.jsp" noresize class="frame">
	<%}else if(ST.equals("3") || ST.equals("5")){%>
	<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/catasto/intFisDetail.jsp" noresize class="frame">
	<%} else if(ST.equals("4")){%>
	<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/catasto/intFisPart.jsp" noresize class="frame">
	<%}%>
	<%if(ST.equals("2") || ST.equals("3")){%>
	<frame name="barraMoveRecord" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/frame/barraMoveRecord.jsp" noresize scrolling="NO" class="frame">
	<%}%>
</frameset>

	
	
</html>