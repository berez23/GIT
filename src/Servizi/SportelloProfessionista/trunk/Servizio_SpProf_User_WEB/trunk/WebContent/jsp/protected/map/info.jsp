<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="sun.misc.BASE64Encoder"%>

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

<title>Sportello del Professionista - Info</title>
</head>

<body style="background-color:#F3F2F2;">

	<%!public String scriviInfoTable(String tabella, String campo_id, String id,
			String nome_layer, String datasource, String pathApp) {
		String retVal = "<table>";
		retVal += "<tr>";
		Statement st = null;
		ResultSet rs = null;
		Statement st2 = null;
		ResultSet rs2 = null;
		Statement st3 = null;
		ResultSet rs3 = null;
		Connection cnn = null;
		String campi = "";
		List<String> listaCampi = new ArrayList<String>();
		if(tabella != null && tabella.contains("s_sp_intervento")){
			tabella = "S_SP_INTERVENTO";
			retVal += "<td></td>";
		}
		try {
			Context cont = new InitialContext();
			DataSource theDataSource = null;
			theDataSource = (DataSource) cont.lookup(datasource);
			String colonne = "";
			cnn = theDataSource.getConnection();
			
			if(nome_layer.equals("Particelle") || nome_layer.equals("Fabbricati")){
				
				colonne += "<td>Foglio</td><td>Particella</td><td>Area</td>";
				campi += "FOGLIO,PARTICELLA,AREA";
				listaCampi.add("FOGLIO");
				listaCampi.add("PARTICELLA");
				listaCampi.add("AREA");
				
			}else if(nome_layer.equals("Civici")){
				
				colonne += "<td>Via</td><td>Civico</td>";
				campi += "VIA,CIVICO";
				listaCampi.add("VIA");
				listaCampi.add("CIVICO");
				
			}else if(nome_layer.equals("Interventi")){
				
				colonne += "<td>Id</td><td>N.posti auto</td><td>N.box auto</td><td>N.passi carrai prev.</td><td>N.accessi carrai prev.</td><td>N. concessione</td><td>N. progressivo</td><td>Anno progressivo</td><td>N. protocollo</td><td>Data protocollo</td><td>Utente</td><td>Note</td><td>Data ins.</td><td>Stato</td>";
				campi += "ID_SP_INTERVENTO,N_POSTI_AUTO,N_BOX_AUTO,N_PASSI_CARRAI_PREV,N_ACCESSI_CARRAI_PREV,C_CONCESSIONE_NUMERO,C_PROGRESSIVO_NUMERO,C_PROGRESSIVO_ANNO,C_PROTOCOLLO_DATA,C_PROTOCOLLO_NUMERO,USER_INS,NOTE,DT_INS,STATO";
				listaCampi.add("ID_SP_INTERVENTO");
				listaCampi.add("N_POSTI_AUTO");
				listaCampi.add("N_BOX_AUTO");
				listaCampi.add("N_PASSI_CARRAI_PREV");
				listaCampi.add("N_ACCESSI_CARRAI_PREV");
				listaCampi.add("C_CONCESSIONE_NUMERO");
				listaCampi.add("C_PROGRESSIVO_NUMERO");
				listaCampi.add("C_PROGRESSIVO_ANNO");
				listaCampi.add("C_PROTOCOLLO_DATA");
				listaCampi.add("C_PROTOCOLLO_NUMERO");
				listaCampi.add("USER_INS");
				listaCampi.add("NOTE");
				listaCampi.add("DT_INS");
				listaCampi.add("STATO");
				
			}else{
				st = cnn.createStatement();
				rs = st.executeQuery("Select i.name_col, i.name_alias "
						+ "from pgt_sql_layer l, pgt_sql_info_layer i " + "where l.NAME_TABLE = '"
						+ tabella + "' and l.ID = i.ID_LAYER and l.STANDARD = i.STANDARD");
				while (rs.next()) {
					colonne += "<td>" + rs.getString("name_alias") + "</td>";
					campi += "," + rs.getString("name_col");
					listaCampi.add(rs.getString("name_col"));
				}
				
				if(colonne.equals("")){
					st3 = cnn.createStatement();
					rs3 = st3.executeQuery("SELECT DISTINCT column_name "
							+ "FROM all_tab_columns " + "WHERE table_name = '"
							+ tabella + "' " + "AND data_type != 'SDO_GEOMETRY' "
							+ "ORDER BY column_name");
		
					while (rs3.next()) {
						colonne += "<td>" + rs3.getString("column_name") + "</td>";
						campi += "," + rs3.getString("column_name");
						listaCampi.add(rs3.getString("column_name"));
					}
				}
				campi = campi.substring(1);
			}
			retVal += colonne;
			retVal += "</tr>";

			st2 = cnn.createStatement();
			rs2 = st2.executeQuery("SELECT DISTINCT " + campi + " FROM " + tabella
					+ " WHERE " + campo_id + " = " + id);
			while (rs2.next()) {
				retVal += "<tr>";
				if(tabella != null && tabella.equals("S_SP_INTERVENTO")){
					String returnValue = "";
					BASE64Encoder encrypt = new BASE64Encoder();
					try{
						String codedString = encrypt.encode(rs2.getBigDecimal("ID_SP_INTERVENTO").toString().getBytes());
						retVal += "<th><a href=\"\" onclick=\"window.opener.opener.parent.location.href='"+pathApp+"/jsp/protected/home.faces?idivt="+codedString+"';window.close();window.opener.close();\"><img style=\"border: 0px;\" src=\""+pathApp+"/images/list.png\" title=\"Vai al dettaglio\"></img></a></th>";
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (String campo : listaCampi) {
					retVal += "<th>"
							+ (rs2.getObject(campo) != null ? rs2
									.getObject(campo) : "&nbsp;") + "</th>";
				}
				retVal += "</tr>";
			}

			retVal += "</table>";

		} catch (SQLException e) {

		} catch (NamingException e) {

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
				if (cnn != null)
					cnn.close();
			} catch (SQLException e) {
			}
		}

		return retVal;
	}%>

	<%
		String parameters = request.getQueryString();
		if (parameters.contains("?")) {
			parameters = parameters.replaceAll("\\?", "\\&");
			String urlOk = "info.jsp?" + parameters;
			response.sendRedirect(urlOk);
		}
	%>

	<%=scriviInfoTable(request.getParameter("TableName"),
					request.getParameter("IdentField"),
					request.getParameter("ElemId"),
					request.getParameter("LayerName"),
					request.getParameter("ds"),
					"http://"+request.getServerName()+":"+ request.getServerPort() +  request.getContextPath())%>
						
</body>
</html>