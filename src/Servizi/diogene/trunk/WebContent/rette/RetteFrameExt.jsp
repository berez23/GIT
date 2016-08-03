<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%  HttpSession sessione = request.getSession(true);  %> 
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>

<html>
	<head>
		<title>Rette scolastiche</title>
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
		<frameset rows="0,*" border="0">
			<frame name="bodyPage" src="" noresize class="frame">
			<frame name="bodyPage" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/rette/RetteListExt.jsp" noresize class="frame">
		</frameset>

</html>
