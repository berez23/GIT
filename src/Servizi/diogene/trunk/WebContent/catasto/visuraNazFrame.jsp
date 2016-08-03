<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>


<html>
	<head>
		<title>Visura Nazionale</title>
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
	<%if("3".equals(ST) || "4".equals(ST)){%>
		<frameset rows="0,*" border="0">
			<frame name="bodyPage" src="" noresize class="frame">
			
			<%if(ST.equals("3")){%>
			<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/catasto/visuraNazSearch.jsp" noresize class="frame">
			<%}else if(ST.equals("4")){%>
			<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/catasto/visuraNazDetail.jsp" noresize class="frame">
			<%}%>
		</frameset>
	<%}else{%>
		<%if(ST.equals("1")){%>
			<frameset rows="0,30,*" border="0">
		<%}else if(ST.equals("2")){%>
			<frameset rows="0,30,*,30" border="0">	
		<%}%>
		
		<frame name="hidden" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/common/hidden.jsp" noresize scrolling="NO" class="frame">
		<frame name="barraTool" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/frame/barraTool.jsp" noresize scrolling="NO" class="frame">
		
		<%if(ST.equals("1")){%>
			<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/common/search.jsp" noresize class="frame">
		<%}else{%>
			<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/catasto/visuraNazDetail.jsp" noresize class="frame">
		<%}%>
		
		</frameset>
	<%}%>

</html>
