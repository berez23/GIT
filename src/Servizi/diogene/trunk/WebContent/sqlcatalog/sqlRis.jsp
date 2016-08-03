<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sc" uri="/WEB-INF/sqlcatalog.tld"%>
<%@ page import="java.sql.*,java.util.*,org.displaytag.tags.TableTag "%>
<%@ page buffer="16kb"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.webred.cet.permission.CeTUser"%>

<%@page import="it.webred.sqlcatalog.DynamicParameter"%>
<%@page import="com.lowagie.text.html.HtmlEncoder"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>

<html>
<head>
<title>SqlExplorer</title>
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/sqlcatalog/css/screen.css" type="text/css"
	media="screen, print" />
	<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<body>


<c:if test='<%=request.getParameter("nuova") != null%>' >
	<c:remove var="results" scope="session" />
	<c:remove var="resultsSchema" scope="session" />
</c:if>



<div align="center">
	<form name="form1" method="post" action="<%=request.getContextPath()%>/SqlCatalog">
		<%
		Connection con = null;
		Statement stmt = null;		
		try {%>
			
			<input type='hidden' name='command' value='<%=request.getParameter("command")%>'/>
			<table class="TXTmainLabel">
			<thead><tr><th colspan="2">Esecuzione catalogo <%=request.getParameter("command")%></th></tr></thead><tbody>
			
			<%Context initContext = new InitialContext();
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)initContext.lookup(es.getDataSourceIntegrato());
			con = ds.getConnection();
			
			String tooltip = "";
			stmt = con.createStatement();
			ResultSet rs_qry = stmt.executeQuery("SELECT TOOLTIPTEXT FROM CATALOGOSQL WHERE IDCATALOGOSQL = " + request.getParameter("command"));
			while (rs_qry.next())
			{
				tooltip = rs_qry.getString("TOOLTIPTEXT");
			}
			stmt.cancel();
			
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT NOME,TIPO FROM CATALOGOSQL_PARAMS WHERE IDCATALOGOSQL = " + request.getParameter("command") + " ORDER BY IDPARAMS");
			String values[] = request.getParameterValues("valore");
			int idx = 0;
			while (rs.next())
			{
				String nome = HtmlEncoder.encode(rs.getString("NOME"));
				String val = "";
				if (values != null && values.length >= idx && values[idx] != null)
					val = values[idx++];
				String input = "<input type=\"text\" name=\"valore\" value=\"" + val + "\">";
				%>
				<tr class="odd"><td nowrap><%=nome%></td><td><%=input%></td></tr>

			<%}
			rs.close();%>
			<c:if test='<%=tooltip != null && !"".equals(tooltip.trim())%>'>
				<tr class="odd"><td colspan="2">
				<div align="center">
					<br/><label><%=tooltip%></label><br/><br/>
				</div></td></tr>
			</c:if>
			<tr class="odd"><td colspan="2">
			<div align="center">
			<input name="executeSql" type="submit" id="executeSql" value="Esegui" onclick="wait()"> &nbsp;
			<input name="indietro" type="button" id="indietro" value="Indietro" onclick="document.location='<%=request.getContextPath()%>/sqlcatalog/index.jsp'">
			</div></td></tr></tbody></table>
		<%}
		catch (Exception e)
		{
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch (SQLException e)
			{
			}
			try
			{
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException e)
			{
			}
		}
	%>
	</form>
	
	<c:if test='<%=request.getAttribute("message") != null%>'>
		<h1 style="color: #FF0000"><c:out value='<%=request.getAttribute("message")%>' escapeXml="false" /></h1>
	</c:if>

	<c:if test='<%=session.getAttribute("results") != null%>'>
		
		<div style="overflow: auto; width: 80%;text-align: left ;overflow-y: hidden "  >
			
			<display:table name="sessionScope.results"  sort="list"  pagesize="15"  export="true" >				
				<c:if test='<%=session.getAttribute("resultsSchema") != null%>'>
						<!-- c:forEach items="${resultsSchema}" var="nomeCol"  -->
						<!-- dava errore (resultsSchema + 'quadratino bianco'), sostituito con: ' -->
						<c:forEach items='<%=session.getAttribute("resultsSchema")%>' var="nomeCol" >					
							<display:column property='<%=(String)pageContext.getAttribute("nomeCol")%>' sortable="true" />										
						</c:forEach> 
				</c:if>	
				<display:setProperty name="export.excel.filename" ><%=System.currentTimeMillis()%>.xls</display:setProperty>
				<display:setProperty name="export.xml.filename" ><%=System.currentTimeMillis()%>.xml</display:setProperty>
				<display:setProperty name="export.csv.filename" ><%=System.currentTimeMillis()%>.csv</display:setProperty>
			</display:table>
			<br>
		</div>
	
	</c:if>
	</div>

<div id="wait" class="cursorWait"></div>
</body>
</html>
