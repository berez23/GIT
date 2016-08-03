<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%
String id = "0";
String nome = "";
String tipo = "";
String descrizione = "";
if(request.getParameter("id") != null && !request.getParameter("id").equals("")) {
	id = request.getParameter("id");
}
if(request.getParameter("nome") != null && !request.getParameter("nome").equals("")) {
	nome = request.getParameter("nome");
}
if(request.getParameter("tipo") != null && !request.getParameter("tipo").equals("")) {
	tipo = request.getParameter("tipo").toUpperCase();
}
if(request.getParameter("descrizione") != null && !request.getParameter("descrizione").equals("")) {
	descrizione = request.getParameter("descrizione");
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Lista esecuzioni</title>

<link href="css/screen.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<div style="width: 80%; margin-left: 10%; margin-right: 10%; padding-top: 20px; padding-bottom: 20px; font-size: 10pt; font-weight: bold; color: #000099;">
		<%=descrizione%>
	</div>
	<%EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
	EnvSource es = new EnvSource(eu.getEnte());
	request.setAttribute("dsIntegrato", es.getDataSourceIntegrato());%>
	<sql:query var="dati" dataSource="jdbc/dbIntegrato_${requestScope.dsIntegrato}">
			SELECT 
			(CASE WHEN NUM_REC > 0 THEN 
			'<a href="<%=request.getContextPath()%>/DiagnosticheCatalogo?ST=99&PATH_FILE=' || PATH_FILE || '">' 
			|| TO_CHAR(DATA_ESEC, 'DD/MM/YYYY HH24:MI:SS') || '</a>' 
			ELSE 
			TO_CHAR(DATA_ESEC, 'DD/MM/YYYY HH24:MI:SS') 
			END) 
			AS LINK_DATA_EXEC, 			
			(CASE WHEN NUM_REC > 0 THEN 
			'<a href="<%=request.getContextPath()%>/DiagnosticheCatalogo?ST=99&PATH_FILE=' || PATH_FILE || '">'
			|| TO_CHAR(DATA_RIF, 'DD/MM/YYYY') || '</a>' 
			ELSE 
			TO_CHAR(DATA_RIF, 'DD/MM/YYYY') 
			END) 
			AS LINK_DATA_RIF, 			
			(CASE WHEN NUM_REC > 0 THEN 
			'<a href="<%=request.getContextPath()%>/DiagnosticheCatalogo?ST=99&PATH_FILE=' || PATH_FILE || '">'
			|| NUM_REC || '</a>' 
			ELSE 
			TO_CHAR(NUM_REC) 
			END) 
			AS LINK_NUM 
			FROM DIA_LOG_ESEC WHERE IDCATALOGODIA = <%=id%> ORDER BY DATA_RIF DESC
	</sql:query>
	<div style="width: 50%; margin-left: 25%; margin-right: 25%">
		 <display:table name="pageScope.dati.rows"  style="width: 100%" class="TXTmainLabel">
			<display:column property="LINK_DATA_EXEC" title="Data esecuzione" sortable="true"/>
			<display:column property="LINK_DATA_RIF" title="Data riferimento" sortable="true"/>
			<display:column property="LINK_NUM" title="Numero record" sortable="true"/>
		 </display:table>
	</div>
	<div style="width: 20%; margin-left: 40%; margin-right: 40%">
		<table style="width: 100%; border: none;">
		 	<tr>
		 		<td style="text-align: center">
		 			<input name="indietro" type="button" id="indietro" value="Indietro" onclick="document.location='sqlFrame.jsp?nome=<%=nome%>&tipo=<%=tipo%>'">
		 		</td>
		 	</tr>
		</table>		
	</div>
</body>
</html>


<c:remove var="results" scope="session" />
<c:remove var="resultsSchema" scope="session" />