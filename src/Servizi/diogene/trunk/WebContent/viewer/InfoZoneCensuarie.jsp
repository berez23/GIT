<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%@page import="javax.naming.Context"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<html>
<head>
<title>Informazioni Zona Censuaria</title>
<LINK rel="stylesheet"
	href="<%= request.getContextPath() %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script>
function vai(codice){
	document.infoForm.KEYSTR.value=codice; // QUESTO PER SALTARE IN LISTA CON ST = 2
	document.infoForm.OGGETTO_SEL.value=codice; // QUESTO PER DETTAGLIO CON ST=3
	document.infoForm.ST.value = 3;
	document.infoForm.submit();
	}
</script>

</head>

<body>

 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Lista dei Subalterni </span>
</div>

   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Part</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Zona</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cat</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cons</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sup Cat</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Scala</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Interno</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Piano</span></td>
	</tr>
<%
    String pk = request.getParameter("pk");
	

		Context cont= new InitialContext();
		//Context datasourceContext = (Context)cont.lookup("java:comp/env");
		DataSource theDataSource = null;
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		//theDataSource =(DataSource)cont.lookup("jdbc/Diogene_DS" + "_" + eu.getEnte());
		theDataSource = (DataSource)cont.lookup(es.getDataSource());
		Connection cnn = theDataSource.getConnection();
		  
		PreparedStatement pst = null;
		ResultSet rs = null;

		try
		{
				String tabella = null;
				if (request.getParameter("LAYER").equals("STA04: ZONE CENSUARIE")) 
					tabella = "CAT_ZONE_CENSUARIE";
				
				String query = "select foglio, particella, sub from " + tabella + " "
					+" where se_row_id = ? ";
				
				System.out.println("Eseguo "+ query);
				System.out.println("Parametri: "+ pk );
				pst = cnn.prepareStatement(query);
				pst.setString(1, pk);
				rs = pst.executeQuery();
				String fog = "";
				String par = "";
				String sub = "";
				while (rs.next()) {
					//trovato = true;
					fog = rs.getString("foglio");
					par = rs.getString("particella");
					sub = rs.getString("sub");
				}
			
				String q = "SELECT   foglio,  "
					+"        CASE "
					+"           WHEN sub = ' ' "
					+"              THEN particella "
					+"           ELSE particella || '/' || sub "
					+"        END AS particella, "
					+"        unimm as sub, zona, categoria, classe, consistenza, rendita, sup_cat, "
					+"        scala, interno, piano "
					+"   FROM sitiuiu "
					+"  WHERE data_fine_val = TO_DATE ('99991231', 'yyyymmdd') "
					+"    AND foglio = ?"
					+"    AND particella = ? "
					+"ORDER BY unimm ";
				
				System.out.println("Eseguo "+ q);
				System.out.println("Parametri: " + fog + ";" + par );
				pst = cnn.prepareStatement(q);
				pst.setInt(1, new Integer(fog));
				pst.setString(2, par);

				rs = pst.executeQuery();
				while (rs.next()) {
					//trovato = true;
					%>
					<tr >
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("foglio")!=null?rs.getString("foglio"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("particella")!=null?rs.getString("particella"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("sub")!=null?rs.getString("sub"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("zona")!=null?rs.getString("zona"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("categoria")!=null?rs.getString("categoria"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("classe")!=null?rs.getString("classe"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("consistenza")!=null?rs.getString("consistenza"):""%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("rendita")!=null?rs.getString("rendita"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("sup_cat")!=null?rs.getString("sup_cat"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("scala")!=null?rs.getString("scala"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("interno")!=null?rs.getString("interno"):"" %></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=rs.getString("piano")!=null?rs.getString("piano"):"" %></span></td>
					</tr>
					<%			
				}
					
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
	</table>

			<form name="infoForm" method="post" action="<%=request.getContextPath()%>/Docfa" id="infoForm" target="_BLANK">
				<input type="hidden" name="DATASOURCE" value="<%=es.getDataSource()%>">
				<input type="hidden" name="ST" value="2">
				<input type="hidden" name="UC" value="43">
				<input type="hidden" name="KEYSTR" value="">
				<input type="hidden" name="OGGETTO_SEL" value="">
			</form>							
										



<div id="wait" class="cursorWait" />
		


</body>

</html>
