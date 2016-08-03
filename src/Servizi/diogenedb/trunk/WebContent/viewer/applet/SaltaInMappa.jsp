
<%@page import="java.sql.*" %>
<%@page import="javax.naming.*" %>
<%@page import="javax.sql.DataSource" %>
<%@ page import="it.webred.diogene.db.DataJdbcConnection" %>
<html>
<head>
<script language="JavaScript">
<!-- //
	function aprischeda(schName){

			popDati = window.open(schName,"windowdati",'width=800, height=600, top=50, left=50, scrollbars=yes');
			popDati.focus();
			window.close();
	}
// -->
</script>
<%
	String tn = request.getParameter("TableName");
	String pkField = request.getParameter("IdentField");
	String pk = request.getParameter("ElemId");
	  
	//Class.forName("oracle.jdbc.driver.OracleDriver");
	

	Connection cnn = DataJdbcConnection.getConnectionn("DWH");
	  
	PreparedStatement pst = null;
	ResultSet rs = null;
	String urlJs = "";
	
	try
	{
		
		
		pst = cnn.prepareStatement("select * from " + tn + " where " + pkField + "=?");
		pst.setString(1, pk);
		rs = pst.executeQuery();

		if (rs.next())
		{
		urlJs = rs.getString("url");
		}    	  
		

		if(urlJs == null || urlJs.trim().equalsIgnoreCase(""))
			urlJs = "javascript:alert('Nella base dati non esistono dati collegati alla selezione effettuata !pk=" + pk + "');window.close();";
	}
	catch (SQLException e)
	{
		throw e;
	}
	finally
	{
		try { rs.close(); } catch (Exception ex) {}
		try { pst.close(); } catch (Exception ex) {}
		try { cnn.close(); } catch (Exception ex) {}
	}
	        
%>
</head>
<body onload="<%=urlJs%>;">
</body>

</html>
