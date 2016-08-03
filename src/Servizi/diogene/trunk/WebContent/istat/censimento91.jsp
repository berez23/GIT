<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<title>Lomboz JSP</title>
</head>
<script>
	function vai(azione,st,uc){
		document.mainform.action = azione
		document.mainform.ST.value=st;
		document.mainform.UC.value=uc;
		document.mainform.submit();
	}
	</script>
<body bgcolor="#FFFFFF">
<form name="mainform" action=''>
	<input type="hidden" name="UC">
	<input type="hidden" name="ST">
</form>
<a href="javascript:vai('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoImmobili',1,1)">Catasto Fabbricati</a><P>
<center><font face="Tahoma" size="2" >Censimento 91</font></center><p><p>
<center><a  href="../eiv/EscIntranetView.jsp" target="black" ><img src="../ewg/images/logobanner.jpg"></img></a> </center> 
</body>
</html>