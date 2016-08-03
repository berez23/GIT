<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html>
	<head>
		<title>Visualizzatore mappe</title>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>

	<frameset id="main" border="0" rows="120,*,75" frameborder="no">
		<frame name="testata" marginheight="0" marginwidth="0" src="mappe/mappeTestata.jsp?index=<%=request.getParameter("index")%>" scrolling="no">
		<frameset id="mappe" border="0" cols="15,*,15" frameborder="no">
			<frame name="margsx" src="" scrolling="no">
			<frame name="mappa" marginheight="0" marginwidth="0" src="mappe/mappeMappa.jsp" scrolling="no">
			<frame name="margdx" src="" scrolling="no">
		</frameset> 
		<frame name="piede" marginheight="0"  marginwidth="0" src="mappe/mappePiede.jsp" scrolling="no">
	</frameset>
	
</html>