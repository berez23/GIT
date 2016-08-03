<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html>

	<head>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
	</head>
	
	<% int rowIndex = Integer.parseInt(request.getParameter("index")); 
		String mode = request.getParameter("mode");%>	

	<frameset id="visualizza" border="0" cols="150, *, 0, 0, 0" frameborder="yes">
		<frame name="link" src="mappe/mappeLink.jsp?index=<%=rowIndex%>&mode=<%=mode%>" scrolling="no">		
		<frame name="img56" src="mappe/mappeNoImg.jsp?index=<%=rowIndex%>" scrolling="auto">
		<frame name="imgCV" src="mappe/mappeNoImg.jsp?index=<%=rowIndex%>" scrolling="auto">
		<frame name="imgCC" src="mappe/mappeNoImg.jsp?index=<%=rowIndex%>" scrolling="auto">
		<frame name="imgDE" src="mappe/mappeNoImg.jsp?index=<%=rowIndex%>" scrolling="auto">
	</frameset>
	
</html>