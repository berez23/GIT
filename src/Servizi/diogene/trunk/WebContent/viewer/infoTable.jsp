<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.escsolution.escwebgis.common.EscLogic"%>
<%@page import="it.escsolution.escwebgis.common.EnvBase"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="it.webred.amprofiler.ejb.perm.LoginBeanService"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.log4j.Logger"%>

<%!static Logger logger = Logger.getLogger("diogene.log");%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
table
{
	font-family: sans-serif;
	border-collapse:collapse;
	background-color:white;
	font-size: 8pt;
	padding:8px;
	font-style:normal;
	font-weight:normal;
}

table, th, td
{
	border: 1px solid #C0C0C0;
	padding:5px;
}
table tr td, table Thead th
{
	color:white;
	text-align:left;
	font-size:8pt;
	vertical-align:bottom;
	background-color: #4A75B5;
	padding-bottom:1px;
	padding-right:2px;
	font-style:normal;
	font-weight:bold;
	border: 1px solid #C0C0C0;
}
</style>

<title>Diogene - Info</title>
</head>

<body style="background-color:#F3F2F2;">

	<%!public String scriviInfoTable(String tabella, String campo_id, String id,
			String nome_layer, CeTUser cetUser) {
		
		String retVal = "<table>";
		retVal += "<tr>";

		Statement st = null;
		ResultSet rs = null;
		Statement st2 = null;
		ResultSet rs2 = null;
		Statement st3 = null;
		ResultSet rs3 = null;
		Statement st4 = null;
		ResultSet rs4 = null;
		Statement st5 = null;
		ResultSet rs5 = null;
		Connection cnn = null;
		Connection cnnSiti = null;
		String campi = "";
		List<String> listaCampi = new ArrayList<String>();
		try {
			Context cont = new InitialContext();
			DataSource theDataSource = null;
			DataSource theDataSourceSiti = null;
			EnvUtente eu = new EnvUtente(cetUser, null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			theDataSource = (DataSource) cont.lookup(es.getDataSource());
			theDataSourceSiti = (DataSource) cont.lookup(es.getDataSourceSiti());
			cnn = theDataSource.getConnection();
			cnnSiti = theDataSourceSiti.getConnection();
			st = cnn.createStatement();
			String sql =null;
			sql = "Select i.name_col, i.name_alias " 
					+ "from pgt_sql_layer l, pgt_sql_info_layer i " + "where l.NAME_TABLE = '"
					+ tabella + "' and l.ID = i.ID_LAYER and l.STANDARD = i.STANDARD";
			logger.info("Recupero colonne info:" );
			logger.info(sql);
			rs = st.executeQuery(sql);
			String colonne = "";
			while (rs.next()) {
				colonne += "<td>" + rs.getString("name_alias") + "</td>";
				campi += "," + rs.getString("name_col");
				listaCampi.add(rs.getString("name_col"));
			}
			
			if (colonne.equals("")) {
				
				st4 = cnn.createStatement();

				String sql4 = "SELECT DISTINCT column_name "
						+ "FROM all_tab_columns C " + "WHERE table_name = '"
						+ tabella + "' " + "AND data_type != 'SDO_GEOMETRY' "
		                + " AND EXISTS "
		                		+ " (SELECT 1 FROM USER_SYNONYMS S "
		                		+ " WHERE C.TABLE_NAME = S.SYNONYM_NAME  "
		                		+ " AND S.TABLE_OWNER = C.OWNER "
		                		+ " ) AND NOT EXISTS "
		                		+ " ( "
		                		+ " SELECT 1  FROM TABS WHERE TABLE_NAME = '" + tabella + "' " 
		                		+ " ) "		
								+ " ORDER BY column_name";
				logger.info("Colonne info non configurate, recupero da all_tab_columns:" );
				logger.info(sql4);
				rs4 = st4.executeQuery(sql4);
				
				while (rs4.next()) {
					colonne += "<td>" + rs4.getString("column_name") + "</td>";
					campi += "," + rs4.getString("column_name");
					listaCampi.add(rs4.getString("column_name"));
				}

			}
			
			if(colonne.equals("")){
				st5 = cnnSiti.createStatement();

				String sql5 = "SELECT DISTINCT c.column_name " +
				           "FROM cols c, all_catalog a " +
				           "WHERE c.table_name = a.table_name " +
				             "AND c.table_name = '"+tabella+"' " +
				             "AND c.data_type != 'SDO_GEOMETRY' " +
				             "AND a.OWNER = '"+cnnSiti.getMetaData().getUserName()+"' " +
				        "ORDER BY c.column_name";
				logger.info("Colonne info non configurate, recupero da cols:" );
				logger.info(sql5);
				rs5 = st5.executeQuery(sql5);
				
				while (rs5.next()) {
					colonne += "<td>" + rs5.getString("column_name") + "</td>";
					campi += "," + rs5.getString("column_name");
					listaCampi.add(rs5.getString("column_name"));
				}
			}
			
			if(colonne.equals("")){
				st3 = cnn.createStatement();

				String sql3 = "SELECT DISTINCT c.column_name " +
				           "FROM cols c, all_catalog a " +
				           "WHERE c.table_name = a.table_name " +
				             "AND c.table_name = '"+tabella+"' " +
				             "AND c.data_type != 'SDO_GEOMETRY' " +
				             "AND a.OWNER = '"+cnn.getMetaData().getUserName()+"' " +
				        "ORDER BY c.column_name";
				logger.info("Colonne info non configurate, recupero da cols:" );
				logger.info(sql3);
				rs3 = st3.executeQuery(sql3);
				
				while (rs3.next()) {
					colonne += "<td>" + rs3.getString("column_name") + "</td>";
					campi += "," + rs3.getString("column_name");
					listaCampi.add(rs3.getString("column_name"));
				}
			}
			retVal += colonne;
			retVal += "</tr>";

			campi = campi.substring(1);
			st2 = cnn.createStatement();
			rs2 = st2.executeQuery("SELECT " + campi + " FROM " + tabella
					+ " WHERE " + campo_id + " = " + id);
			while (rs2.next()) {
				retVal += "<tr>";
				for (String campo : listaCampi) {
					retVal += "<th>"
							+ (rs2.getObject(campo) != null ? rs2
									.getObject(campo) : "&nbsp;") + "</th>";
				}
				retVal += "</tr>";
			}

			retVal += "</table>";

		} catch (Exception e) {
				logger.error("errore recuperando colonne per info", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (rs2 != null)
					rs2.close();
				if (st2 != null)
					st2.close();
				if (rs3 != null)
					rs3.close();
				if (st3 != null)
					st3.close();
				if (rs4 != null)
					rs4.close();
				if (st4 != null)
					st4.close();
				if (st5 != null)
					st5.close();
				if (rs5 != null)
					rs5.close();
				if (cnn != null)
					cnn.close();
				if (cnnSiti != null)
					cnnSiti.close();
				
			} catch (SQLException e) {
			}
		}

		return retVal;
	}%>

	<%
	String entetable = request.getParameter("es");
	String[] arr = entetable.split("\\?");
	String currentEnte = arr[0];
	String tn = arr[1].replace("TableName=", "");
	String pkField = request.getParameter("IdentField");
	String pk = request.getParameter("ElemId");
	String layerName = ((String)request.getParameter("LayerName")).toUpperCase();
	CeTUser cetUser = (CeTUser) request.getSession().getAttribute("user");
	if(cetUser == null){
		EnvBase base = new EnvBase();
		LoginBeanService service = (LoginBeanService) base.getEjb("AmProfiler", "AmProfilerEjb", "LoginBean");
		cetUser = new CeTUser();
		cetUser.setUsername(request.getUserPrincipal().getName());
		HashMap<String, String> permList = service.getPermissions(cetUser.getUsername(), currentEnte);
		cetUser.setPermList(permList);
		cetUser.setCurrentEnte(currentEnte);
		cetUser.setSessionId(request.getSession(false).getId());
		request.getSession().setAttribute("user", cetUser);
	}
	%>

	<%=scriviInfoTable(tn, pkField, pk, layerName, cetUser)%>
</body>
</html>