<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Fascicolo Oggetto - Ricerca per Via</title>
<LINK rel="stylesheet" href="../styles/style.css" type="text/css"> 
<style type="text/css">
<!--
input
{	
	border: 1px solid #3300FF;
}
.extWinTDData
{
	white-space:nowrap;
}
--> 
</style>

<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script src="fascicoloscript.js" language="JavaScript"></script>
<script type="text/javascript">
function selFP(fo,pa)
{
	opener.reimposta();
	opener.document.getElementById("ricerca_foglio").value=fo;
	opener.document.getElementById("ricerca_particella").value = pa;
	opener.focus();
	window.close();
}
function ricerca()
{
	var via = document.getElementById("via");
	var civico = document.getElementById("civico");
	if(via.value == "")
	{
		alert("Inserire il nome della via");
		via.focus();
		return false;
	}else if(civico.value == "")
	{
		alert("Inserire il numero civico");
		civico.focus();
		return false;
	}
	else
	{
		if(document.getElementById("att2"))
			document.getElementById("att").innerHTML = '<center>Ricerca in corso ..... <br><img src="progressbar.gif" border=0/></center>';
		else		
			document.getElementById("att").innerHTML = '<center>Ricerca in corso ..... <br><img src="progressbar.gif" border=0/></center>';		
		document.getElementById("actionform").submit(); 
	}
		
}
</script>
</head>
<body>

  <fieldset>
  	<legend>Ricerca foglio particella dalla Via</legend>
	  <form action="" method="post" id="actionform">
	  <input type="hidden" name="ricerca_foglio" id="sedime" value="ex_sedime_per_compatibilita"/>	
	<input type="text" name="ricerca_particella" id="via" value='<c:out value="${param.ricerca_particella}"/>'/>
	 numero <input type="text" name="ricerca_sub" id="civico" style="width: 25px" value='<c:out value="${param.ricerca_sub}"/>'/>
	<input type="button" name="button" id="button" value="Cerca" onclick="ricerca()" />
  <input type="hidden" name="AZ" value="RICERCAVIA"/>
 </form>  	
  </fieldset>
<div id="att">  
  <c:if test="${DATI_FASCICOLO != null}">
  <fieldset>
  	<legend>Lista Vie</legend>
	<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">	
		<tr>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Via<span class="extWinTXTTitle"></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Civico</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		</tr>
		<c:forEach var="via" items="${DATI_FASCICOLO[0].rows}">	    
		<tr>
			<td onclick="selFP('<c:out value="${via.map.foglio}"/>','<c:out value="${via.map.particella}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${via.map.via}"/></span></td>
			<td  onclick="selFP('<c:out value="${via.map.foglio}"/>','<c:out value="${via.map.particella}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${via.map.civico}"/></span></td>			
			<td  onclick="selFP('<c:out value="${via.map.foglio}"/>','<c:out value="${via.map.particella}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${via.map.foglio}"/></span></td>
			<td  onclick="selFP('<c:out value="${via.map.foglio}"/>','<c:out value="${via.map.particella}"/>')" class="extWinTDData" style='cursor:pointer;'><span class="extWinTXTData"><c:out value="${via.map.particella}"/></span></td>		
		</tr>
		</c:forEach>
	</table>	
  </fieldset>
  </c:if>
</div>
</body>
</html>