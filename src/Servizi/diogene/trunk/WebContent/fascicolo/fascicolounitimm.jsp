<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Fascicolo Oggetto</title>
<LINK rel="stylesheet" href="../styles/style.css" type="text/css"> 
<LINK rel="stylesheet" href="fascicolo.css" type="text/css">
<LINK rel="stylesheet" href="ui.datepicker.css" type="text/css">
 
<style >
</style>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script src="../ewg/Scripts/jquery.js" language="JavaScript"></script>
<script src="ui.datepicker.js" language="JavaScript"></script>
<script src="fascicoloscript.js" language="JavaScript"></script> 
<script language="JavaScript">
$(document).ready(function(){
<c:forEach var="elab" items="${fascicoliFabAbbilitati}">
	$.ajax({
	  url: "<c:out value="FascicoloRunnerServlet.jsp?AZ=${elab.key}&ricerca_foglio=${param.f}&ricerca_particella=${param.p}&ricerca_sub=${param.s}" escapeXml="false"/>",
	  cache: false,
	  async: false,
	  success: function(html){
	    $("#div<c:out value="${elab.key}"/>").html(html);
	    $("#div<c:out value="${elab.key}"/>").css("display","block");
	  }
	});
</c:forEach>

filtraPerData($('#filtroData').val())
});
</script>
</head>
<body>
<div align="center"><strong>Fascicolo Unit. Imm.: Foglio <c:out value="${param.f}"/> Particella <c:out value="${param.p}"/> Subalterno <c:out value="${param.s}"/></strong></div>
<div  class="divMenu">
<table width="400" border="1">
  <tr align="left" valign="top">
    <td colspan="2" nowrap>
		<span class="TDmainLabel">Evidenzia i record validi in data:</span> <input type="text" id="filtroData" name="filtroData" value="<c:out value="${param.d}"/>" readonly="true" size="8"/>
		<input type="button" onclick="filtraPerData($('#filtroData').val())" value="Applica"/>
    </td>
  </tr>
</table>
</div>

<c:forEach var="elabdiv" items="${fascicoliFabAbbilitati}">
				<div id="div<c:out value="${elabdiv.key}"/>" class="divContenitore" titolo="<c:out value="${elabdiv.value.descrizione}"/>"></div>   				 
</c:forEach>

      



</body>
</html>