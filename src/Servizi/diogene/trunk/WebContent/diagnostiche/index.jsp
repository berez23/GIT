<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql"%>
<%
String nome = "";
if(request.getParameter("nome") != null && !request.getParameter("nome").equals(""))
{
	nome = request.getParameter("nome");
	request.getSession().setAttribute("nomeCatalogoSql",nome);
}
else if(request.getSession().getAttribute("nomeCatalogoSql") != null)
	nome = request.getSession().getAttribute("nomeCatalogoSql").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Diagnostiche</title>
<link href="css/dia.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<div style="width: 80%;margin-left: 10%; margin-right: 10%" align="center">	  
		
		<table class="TXTmainLabel">
				<thead>
				<form action="<%=request.getContextPath()%>/Diagnostiche?COD_FONTE=<%=request.getParameter("COD_FONTE")%>&UC=<%=request.getParameter("UC")%>&ST=1" method="post">
				<tr>
				<td colspan="3">		
						<c:out value="${RICERCA_DIAGNOSTICHE.descrizioneFonte}"/>
				</td>
				</tr>
				</thead>
				<tr>
				<td>
					Acquisizione fonte dati in data :
				</td>
				<td>					
					<select name="dataController" id="dataController" style="width: 320px">					
						<option value=""></option>
						<c:forEach var="datec"  varStatus="statusDc" items="${RICERCA_DIAGNOSTICHE.dateLancioController}"> 
							<option value='<c:out value="${datec.key}"/>' <c:if test="${RICERCA_DIAGNOSTICHE.timeControllerSelected == datec.key}"> selected</c:if>  > <c:out value='${datec.value}' /></option> 
						</c:forEach>	
					</select>
				</td>
				<td>
					<input type="submit" id="visualizzaPerDataController" name="visualizzaPerDataController" value="visualizza"/>
				</td>
				</tr>
				</form>
				<tr>
				<td>
					Visualizza  singola diagnostica :
				</td>
				<td>					
					<select name="consultaDiaPerCommand" id="consultaDiaPerCommand" style="width: 320px" >					
						<option value=""></option>
						<c:forEach var="cm"  varStatus="statusDc" items="${RICERCA_DIAGNOSTICHE.listaCommandDiagnostiche}"> 
							<option value='<c:out value="${cm.value.urlListaDiagnostica}"/>'  > <c:out value='${cm.value.codCommand}' /> <c:out value='${cm.value.descCommand}' /></option> 
						</c:forEach>	
					</select>
				</td>
				<td>
					<input type="button" id="visualizzaPerCommand" name="visualizzaPerCommand" value="visualizza" onclick="if(document.getElementById('consultaDiaPerCommand').value != '') {document.location = document.getElementById('consultaDiaPerCommand').value;}"/>
				</td>
				</tr>
								
		</table>
		
		<table class="TXTmainLabel">
		<c:forEach var="dia"  varStatus="status" items="${RICERCA_DIAGNOSTICHE.listDiagnostiche}">
			<c:if test="${pageScope.fonte != dia.tipo}">
				<thead>
				<tr>
				<td colspan="2">		
						<c:out value="${dia.tipo}"/>
				</td>
				</tr>
				</thead>
			</c:if>
		    <c:choose>
		        <c:when test='${pageScope.mycss == "odd"}'>
		            <c:set var="mycss" value="even" scope="page" />
		        </c:when>
		        <c:otherwise>
		            <c:set var="mycss" value="odd" scope="page" />
		        </c:otherwise>
		    </c:choose>
			<tr class="<c:out value='${pageScope.mycss}'/>">
			<c:set var="fonte" value="${dia.tipo}" scope="page" />			
		            <td>	
						<a href="../Diagnostiche?UC=101&ST=2&COD_FONTE=<c:out value="${dia.idFonte}" />&cmd=<c:out value="${dia.idCommand}" />&IDTEMPL=<c:out value="${dia.idCfgTemplate}" />&idComLau=<c:out value="${dia.idCommanLaunch}" />&dataController=<c:out value="${RICERCA_DIAGNOSTICHE.timeControllerSelected}" />"><c:out value="${dia.codCommand}" /></a>
					</td>	
					<td>	
						<a href="../Diagnostiche?UC=101&ST=2&COD_FONTE=<c:out value="${dia.idFonte}" />&cmd=<c:out value="${dia.idCommand}" />&IDTEMPL=<c:out value="${dia.idCfgTemplate}" />&idComLau=<c:out value="${dia.idCommanLaunch}" />&dataController=<c:out value="${RICERCA_DIAGNOSTICHE.timeControllerSelected}" />"><c:out value="${dia.descCommand}"/></a>
					</td>

			<tr>
		</c:forEach>
		</table>
	</div>

</body>

</html>


<c:remove var="results" scope="session" />
<c:remove var="resultsSchema" scope="session" />