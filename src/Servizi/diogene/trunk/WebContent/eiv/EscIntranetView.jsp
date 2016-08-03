<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<jsp:useBean id="Config" class="it.escsolution.eiv.JavaBeanGlobalVar"  >
</jsp:useBean>
<%--<%!
int sessionCounter=0;
%>--%>
<%--<c:set var="sessionCounter" scope="session" value="${sessionCounter+1}"/>--%>
<%--
<%
if(sessionCounter<1){
%>

<%
if(Config.SiteLogon==""){
	Config.setSitLogon("All user");
}
if(Config.MWFNAME==""){
Config.setMWFNAME("catastoRomaGauss.mwf");
}
%>--%>

<html>
<head>
<script language="JavaScript">
function posiziona(){
		self.resizeTo(800,600);
	}
</script>
<LINK REL="stylesheet"  HREF="../ESC.css" type="text/css"></HEAD>
<title>ESC Intranet Viewer</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Microsoft FrontPage 3.0">
</head>	
	<frameset id="main" name="main" border="0" cols="*,140">
		<frameset id="sx" border="5" rows="30,*,58">
			<frame name="Strumenti" marginheight="0" marginwidth="0" src="Titolo.jsp" scrolling="no" noresize>  
			<frame name="Mappa" marginheight="0" marginwidth="0" src="Mappa.jsp" scrolling="no" >
			<frame name="Dati" marginheight="0" marginwidth="0" src="Dati.jsp" scrolling="yes">
		</frameset>
		<frameset id="dx" border="0" rows="*,58">
			<frame name="Bottoni" src="Bottoni.jsp?aperta=false&aperta2=false&aperta3=false&aperta4=false&aperta5=false" marginheight="0"  marginwidth="0" scrolling="no" >
			<frame name="spazio" marginheight="0" src="Spazio.jsp" marginwidth="0" scrolling="no" noresize> 
		</frameset>
	</frameset>
	

<body bgcolor="#FFFFFF" onload="posiziona()">
</body>
</html>
