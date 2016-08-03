<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="sun.misc.BASE64Encoder"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="it.webred.gitland.web.bean.*" %>
<%@page import="it.webred.ct.config.parameters.application.ApplicationService" %>
<%@page import="it.webred.cet.permission.CeTUser" %>

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

<title>GitLand - Info</title>
</head>

<body style="background-color:#F3F2F2;">

	<%!
	Logger logger = Logger.getLogger("gitland.log");
	public String scriviInfoTable(String tabella, String campo_id, String id,String nome_layer, String datasource, String pathApp, CeTUser user) {
		
		logger.debug("info.jsp - tabella:"+tabella);
		logger.debug("info.jsp - campo:"+campo_id);
		logger.debug("info.jsp - id:"+id);
		logger.debug("info.jsp - nome_layer:"+nome_layer);
		logger.debug("info.jsp - ds:"+datasource);
		logger.debug("info.jsp - path:"+pathApp);
		
		String retVal = "<table>";
		retVal += "<tr>";
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		Statement st2 = null;
		ResultSet rs2 = null;
		Statement st3 = null;
		ResultSet rs3 = null;
		Connection cnn = null;
		String campi = "";
		List<String> listaCampi = new ArrayList<String>();
		if(tabella != null && tabella.contains("DM_B_MAPPALE_PART")){
			tabella = "DM_B_MAPPALE_PART";
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
				
			}else if(nome_layer.equals("Inventario - Mappali")){
				
				colonne += "<td>Chiave</td>"
						+  "<td>Tipo Bene</td>"
						+  "<td>Foglio</td>"
						+  "<td>Particella</td>"
						+  "<td>Indirizzo</td>"
						+  "<td>Civico</td>"
						+  "<td>Comune</td>"
						+  "<td>Prov.</td>"
						+  "<td>Ind.Principale</td>"
						;
				campi += "CHIAVE,des_tipo_bene,foglio,PARTICELLA,indirizzo,civico,des_comune,prov,ind_principale";
				listaCampi.add("CHIAVE");
				listaCampi.add("DES_TIPO_BENE");
				listaCampi.add("FOGLIO");
				listaCampi.add("PARTICELLA");
				listaCampi.add("INDIRIZZO");
				listaCampi.add("CIVICO");
				listaCampi.add("DES_COMUNE");
				listaCampi.add("PROV");
				listaCampi.add("IND_PRINCIPALE");
				
			}else if(nome_layer.equals("Inventario - Terreni")){
				
				colonne += "<td>Chiave</td>"
						+  "<td>Tipo Bene</td>"
						+  "<td>Foglio</td>"
						+  "<td>Particella</td>"
						+  "<td>Qualit</td>"
						+  "<td>Sup.</td>"
						+  "<td>Classe</td>"
						;
				campi += "CHIAVE,des_tipo_bene,foglio,PARTICELLA,qualita,superficie,classe";
				listaCampi.add("CHIAVE");
				listaCampi.add("DES_TIPO_BENE");
				listaCampi.add("FOGLIO");
				listaCampi.add("PARTICELLA");
				listaCampi.add("QUALITA");
				listaCampi.add("SUPERFICIE");
				listaCampi.add("CLASSE");
				
			}else if(nome_layer.equals("Fascicolo Fabbricato")){
				
				pst = cnn.prepareStatement("select * from " + tabella + " where " + campo_id + "=?");
				pst.setString(1, id);
				rs = pst.executeQuery();
				
				if (rs.next()){	
					String pk_par_catastali = rs.getString("fk_par_catastali");
					logger.debug("info.jsp - pk_par_catastali:"+pk_par_catastali);
					try{
						ApplicationService appSer = (ApplicationService) 
								cont.lookup("java:global/CT_Service/CT_Config_Manager/ApplicationServiceBean");
						
						String codNazionale = pk_par_catastali.substring(0,4);
						//Recuperare sezione da codNazionale
						String sezione = "";
						String foglio = pk_par_catastali.substring(5,9);
						String particella = pk_par_catastali.substring(9,14);
						String url="";
						String utente = user.getUsername();
						String ente = user.getCurrentEnte();
						if(foglio!=null && !"".equals(foglio) && particella!=null && !"".equals(particella)){
							url = appSer.getUrlApplication(utente, ente, "FascFabb");
							if(url!=null && !"".equals(url)){
								foglio = removeLeadingZero(foglio);
								particella = removeLeadingZero(particella);
								
								url+="/jsp/protected/richieste/richieste.faces?es=" + encode(ente)+"&SEZ="+sezione+"&FOGLIO="+foglio+"&PARTICELLA="+particella;
								url = sezione+"#"+foglio+"#"+particella+"#"+url;
								
							}else
								url="ERR#Accesso al Fascicolo Fabbricato non consentito.Utente "+utente+" non abilitato.";
						}else{
							url = "ERR#Immettere foglio e particella.";
						}
			
					logger.debug("info.jsp - url:"+url);
					if(url.startsWith("ERR#")){
						url = url.substring(url.indexOf('#')+1); 
						logger.debug(url);
						retVal+="ERRORE:"+url;
						
						}else{ 
							String[] s = url.split("#");
							retVal += "Caricamento del Fascicolo Fabbricato per FOGLIO="+foglio+",PARTICELLA="+particella+" in corso...";
							retVal += "<script type=\"text/javascript\">window.onload=function(){window.open('"+s[3]+"','_self')} </script>";
						} 
				}catch(Exception e){
					logger.error("ERRORE fas su info.jsp", e);
				}
				}
				
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
				if(tabella != null && tabella.equals("DM_B_MAPPALE_PART")){
					String returnValue = "";
					BASE64Encoder encrypt = new BASE64Encoder();
					try{
						String codedString = encrypt.encode(rs2.getBigDecimal("ID").toString().getBytes());
						retVal += "<th><a href=\"\" onclick=\"window.opener.opener.parent.location.href='"+pathApp+"/jsp/protected/home.faces?idivt="+codedString+"';window.close();window.opener.close();\"><img style=\"border: 0px;\" src=\""+pathApp+"/images/list.png\" title=\"Vai al dettaglio\"></img></a></th>";
					} catch (Exception e) {
						logger.error("ERRORE su info.jsp", e);
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
			logger.error("SQLException su info.jsp", e);
		} catch (NamingException e) {
			//logger.error("NamingException su info.jsp", e);
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
				if(pst !=null)
					pst.close();
			} catch (SQLException e) {}
		}
		logger.debug("info.jsp -->>"+retVal);
		return retVal;
	}
	
	public static String removeLeadingZero(String str) {
		if (str==null || str.length() == 0)
			return str; 
		
		String retVal=new String(str);
		int i=0;
		while (i<str.length()) {
			if(str.charAt(i)=='0' && str.length() >i+1 ) {
				retVal=str.substring(i+1);
			}else
				break;
			i++;
		}
		return retVal;
	
	}	
	
	public String encode(String stringToEncode) {
		String returnValue = "";
		BASE64Encoder encrypt = new BASE64Encoder();
		try {
			String codedString = encrypt.encode(stringToEncode.getBytes());
			returnValue = codedString;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return returnValue;
	}
	
	%>

	<%
		String parameters = request.getQueryString();
		logger.debug("info.jsp - Parameters:"+parameters);
		if (parameters.contains("?")) {
			parameters = parameters.replaceAll("\\?", "\\&");
			String urlOk = "info.jsp?" + parameters;
			logger.debug("info.jsp - UrlOk:"+urlOk);
			response.sendRedirect(urlOk);
		}
	%>

	<%=scriviInfoTable(request.getParameter("TableName"),
					request.getParameter("IdentField"),
					request.getParameter("ElemId"),
					request.getParameter("LayerName"),
					request.getParameter("ds"),
					"http://"+request.getServerName()+":"+ request.getServerPort() +  request.getContextPath(), (CeTUser) request.getSession().getAttribute("user"))%>
						
</body>
</html>