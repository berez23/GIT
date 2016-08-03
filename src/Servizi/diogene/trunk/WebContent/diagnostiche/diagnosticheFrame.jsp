<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>

<html>
<head>
<title>CONDOMINI</title>
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
<frameset rows="100%" border="0">
	<frame name="bodyPage" src="<%=request.getContextPath()%>/diagnostiche/index.jsp?COD_FONTE=<%=request.getParameter("COD_FONTE")%>&UC=<%=request.getParameter("UC")%>&ST=1" noresize class="frame">	
</frameset>


</html>