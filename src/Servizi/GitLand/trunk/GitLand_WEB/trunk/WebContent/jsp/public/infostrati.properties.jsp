<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="org.apache.log4j.Logger"%>

<%!
public String scriviLayerCat(String ente, String webPath, String datasource)
{
	Logger logger = Logger.getLogger("gitland.log");
	String retVal="";
	String tipoLayer = "Catasto";
	int i=1;
	retVal += "#if present it is an http url where to read properties instead of this file\n" +
				"#filepropertyurl=http://caleffi:8998/sitipatri/extrapub/viewerjs/infostrati2.properties\n" +

				/*"layer.tableName.1=SITIFGLC\n" +
				"layer.fieldShape.1=SHAPE\n" +
				"layer.fieldPk.1=FOGLIO\n" +
				"layer.name.1=Fogli\n" +
				"layer.where.1=1=1\n" +
				"layer.infourl.1="+webPath+"/GitLand_WEB/jsp/protected/map/info.jsp?ds="+datasource+"\n" +*/
				
				"#name of table\n" +
				"layer.tableName." + i + "=CAT_PARTICELLE_GAUSS\n" +
				"#name of shape field\n" +
				"layer.fieldShape." + i + "=geometry\n" +
				"#name of primary key field\n" +
				"layer.fieldPk." + i + "=pk_particelle\n" +
				"#name of layer\n" +
				"layer.name." + i + "=Particelle\n" +
				"#where clause for query (optional)\n" +
				"layer.where." + i + "=COMUNE in (select uk_belfiore from ewg_tab_comuni) and layer='PARTICELLE'\n" +
				"#full url without parameters for info\n" +
				"layer.infourl." + i + "="+webPath+"/GitLand/jsp/protected/map/info.jsp?ds="+datasource+"\n";
				
		i++;		//2				
		retVal += "\n";
		retVal +=
				"layer.tableName." + i + "=CAT_PARTICELLE_GAUSS\n" +
				"layer.fieldShape." + i + "=geometry\n" +
				"layer.fieldPk." + i + "=pk_particelle\n" +
				"layer.name." + i + "=Fabbricati\n" +
				"layer.where." + i + "=COMUNE in (select uk_belfiore from ewg_tab_comuni) and layer='FABBRICATI'\n" +
				"layer.infourl." + i + "="+webPath+"/GitLand/jsp/protected/map/info.jsp?ds="+datasource+"\n" ;
				
		i++;		//3		
		tipoLayer = "Fascicoli";
		retVal += "\n";
		retVal +=
				"layer.tableName." + i + "=(SELECT siticivi.cod_nazionale AS civi_cod_nazionale,siticomu.cod_nazionale AS comu_cod_nazionale,siticivi.data_fine_val, pkid_civi, shape, sitidstr.prefisso || ' ' || sitidstr.nome via, siticivi.civico,siticomu.codi_fisc_luna FROM siticivi, sitidstr, siticomu WHERE sitidstr.pkid_stra = siticivi.pkid_stra)\n" +
				"layer.fieldShape." + i + "=shape\n" +
				"layer.fieldPk." + i + "=pkid_civi\n" +
				"layer.name." + i + "=Civici\n" +
				"layer.buffer." + i + "=10\n" +
				"layer.where." + i + "=civi_cod_nazionale = comu_cod_nazionale AND codi_fisc_luna IN (SELECT uk_belfiore FROM ewg_tab_comuni) AND data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')\n" +
				"layer.infourl." + i + "="+webPath+"/GitLand/jsp/protected/map/info.jsp?ds="+datasource+"\n" ;
				
		i++;		//4
		retVal += "\n";
		retVal +=
				"layer.tableName." + i + "=CAT_PARTICELLE_GAUSS_2\n" +
				"layer.fieldShape." + i + "=geometry\n" +
				"layer.fieldPk." + i + "=pk_particelle\n" +
				"layer.name." + i + "=Fascicolo Fabbricato\n" +
				"layer.where." + i + "=COMUNE in (select COD_NAZIONALE from sit_ente E , SITICOMU C  WHERE E.CODENT = C.CODI_FISC_LUNA ) and layer='FABBRICATI'\n" +
				"layer.infourl." + i + "="+webPath+"/GitLand/jsp/protected/map/info.jsp?ds="+datasource+"\n" ;
		
			
		i++;		//5
		retVal += "\n"; 

		
		//TODO: layer.name deve coincidere con tag name di	wms.xml.jsp
		/* retVal +=
				"layer.tableName." + i + "=" +
						"CAT_CENS_AZIENDALE\n "+
					//"SELECT SE_ROW_ID, SHAPE, FOGLIO, NUMERO, OCCORRENZE "+
					//" FROM CAT_CENS_AZIENDALE \n "+
					//"WHERE 1=1 \n" +
				"layer.fieldShape." + i + "=shape\n" +
				"layer.fieldPk." + i + "=id_asi\n" +
				"layer.name." + i + "=Lotti Insediamenti\n" +
				"layer.where." + i + "=1=1 \n" +
				"layer.infourl." + i + "="+webPath+"/GitLand/jsp/protected/map/info.jsp?ds="+datasource+"\n" ; */

		//i++;

		//aggiungo proprietà ottenute da un resultset
		Statement st = null;
		ResultSet rs = null;
		Connection cnn = null;
		try{
			Context cont = new InitialContext();
			DataSource theDataSource = null;
			theDataSource = (DataSource) cont.lookup(datasource);
			cnn = theDataSource.getConnection();

			retVal += "\n";
			
			st = cnn.createStatement();
			
			boolean recuperoCataloghi = true;
			if (recuperoCataloghi){			
				//rs = st.executeQuery("SELECT ID_FIELD, TABELLA, DESCRIZIONE, STORICIZZATO,ID_CAT FROM SITICATALOG order by DESCRIZIONE");
				rs = st.executeQuery("SELECT * FROM (" +
						"SELECT ID_FIELD, TABELLA, DESCRIZIONE, STORICIZZATO,ID_CAT, shape_type, 'Catalogo SITI' as tipo_layer FROM SITICATALOG " +
						"UNION " +
						"SELECT name_col_id, name_table, des_layer, 0, id, shape_type, tipo_layer FROM pgt_sql_layer WHERE flg_publish = 'Y' AND flg_hide_info<>'Y') " +
						"ORDER BY tipo_layer, DESCRIZIONE");
				
				while (rs.next()) {
					tipoLayer = rs.getString("TIPO_LAYER");
					
					retVal += "layer.tableName." + i + "=" + rs.getString("TABELLA") + "\n";
					retVal += "layer.fieldShape." + i + "=SHAPE"  + "\n";
					retVal += "layer.fieldPk." + i + "=" + rs.getString("ID_FIELD") + "\n";
					retVal += "layer.name." + i + "=" + rs.getString("DESCRIZIONE") + "\n";
					if (rs.getString("SHAPE_TYPE").equals("POINT"))
						retVal += "layer.buffer." + i + "=10"  + "\n";
					long stor = rs.getLong("STORICIZZATO");
					if (stor == 1)
						retVal += "layer.where." + i + "=ID_CAT=" + rs.getString("ID_CAT") + " AND DATA_FINE_VAL=TO_DATE('99991231000000', 'YYYYMMDDHH24MISS')" + "\n";
					else
						retVal += "layer.where." + i + "=1=1\n";
					retVal += "layer.infourl." + i + "="+webPath+"/GitLand/jsp/protected/map/info.jsp?ds="+datasource+"\n";
					i++;
	
				}
			}
			retVal += "layercount=" + --i + "\n\n";
			
			retVal += "#url of dbamp asj servlet to use for query\n";
			retVal += "dbmapasjservlet="+webPath+"/DbMAP_ASJ_servlet/" + ente + "/DbMAPservlet";
					
		} catch (SQLException e) {
			logger.error("ERRORE su infostrati.properties", e);
		} catch (NamingException e) {
			logger.error("ERRORE su infostrati.properties", e);
		}finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (cnn != null)
					cnn.close();
			} catch (SQLException e) {
				logger.error("ERRORE su infostrati.properties", e);
			}
		}
		logger.debug("infostrati.properties -->>" + retVal);
		return retVal;
}
%>

<%=scriviLayerCat(request.getParameter("ente"), "http://"+request.getServerName()+":"+ request.getServerPort(), request.getParameter("ds"))%>