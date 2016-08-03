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
	Logger logger = Logger.getLogger("spprof.log");
	String retVal="";

		retVal += "#if present it is an http url where to read properties instead of this file\n" +
				"#filepropertyurl=http://caleffi:8998/sitipatri/extrapub/viewerjs/infostrati2.properties\n" +

				/*"layer.tableName.1=SITIFGLC\n" +
				"layer.fieldShape.1=SHAPE\n" +
				"layer.fieldPk.1=FOGLIO\n" +
				"layer.name.1=Fogli\n" +
				"layer.where.1=1=1\n" +
				"layer.infourl.1="+webPath+"/SpProfessionista/jsp/protected/map/info.jsp?ds="+datasource+"\n" +*/
				
				"#name of table\n" +
				"layer.tableName.1=CAT_PARTICELLE_GAUSS\n" +
				"#name of shape field\n" +
				"layer.fieldShape.1=geometry\n" +
				"#name of primary key field\n" +
				"layer.fieldPk.1=pk_particelle\n" +
				"#name of layer\n" +
				"layer.name.1=Particelle\n" +
				"#where clause for query (optional)\n" +
				"layer.where.1=COMUNE in (select uk_belfiore from ewg_tab_comuni) and layer='PARTICELLE'\n" +
				"#full url without parameters for info\n" +
				"layer.infourl.1="+webPath+"/SpProfessionista/jsp/protected/map/info.jsp?ds="+datasource+"\n" +

				"layer.tableName.2=CAT_PARTICELLE_GAUSS\n" +
				"layer.fieldShape.2=geometry\n" +
				"layer.fieldPk.2=pk_particelle\n" +
				"layer.name.2=Fabbricati\n" +
				"layer.where.2=COMUNE in (select uk_belfiore from ewg_tab_comuni) and layer='FABBRICATI'\n" +
				"layer.infourl.2="+webPath+"/SpProfessionista/jsp/protected/map/info.jsp?ds="+datasource+"\n" +
		
				"layer.tableName.3=(SELECT siticivi.cod_nazionale AS civi_cod_nazionale,siticomu.cod_nazionale AS comu_cod_nazionale,siticivi.data_fine_val, pkid_civi, shape, sitidstr.prefisso || ' ' || sitidstr.nome via, siticivi.civico,siticomu.codi_fisc_luna FROM siticivi, sitidstr, siticomu WHERE sitidstr.pkid_stra = siticivi.pkid_stra)\n" +
				"layer.fieldShape.3=shape\n" +
				"layer.fieldPk.3=pkid_civi\n" +
				"layer.name.3=Civici\n" +
				"layer.buffer.3=10\n" +
				"layer.where.3=civi_cod_nazionale = comu_cod_nazionale AND codi_fisc_luna IN (SELECT uk_belfiore FROM ewg_tab_comuni) AND data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')\n" +
				"layer.infourl.3="+webPath+"/SpProfessionista/jsp/protected/map/info.jsp?ds="+datasource+"\n" +
						
				"layer.tableName.4=(SELECT i.*,p.shape as shape FROM s_sp_intervento i, s_sp_area_part p WHERE i.id_sp_intervento = p.fk_sp_intervento)\n" +
				"layer.fieldShape.4=shape\n" +
				"layer.fieldPk.4=id_sp_intervento\n" +
				"layer.name.4=Interventi\n" +
				"layer.where.4=1=1\n" +
				"layer.infourl.4="+webPath+"/SpProfessionista/jsp/protected/map/info.jsp?ds="+datasource+"\n";

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
			//rs = st.executeQuery("SELECT ID_FIELD, TABELLA, DESCRIZIONE, STORICIZZATO,ID_CAT FROM SITICATALOG order by DESCRIZIONE");
			rs = st.executeQuery("SELECT * FROM (" +
					"SELECT ID_FIELD, TABELLA, DESCRIZIONE, STORICIZZATO,ID_CAT, shape_type FROM SITICATALOG " +
					"UNION " +
					"SELECT name_col_id, name_table, des_layer, 0, id, shape_type FROM pgt_sql_layer WHERE flg_publish = 'Y') " +
					"ORDER BY DESCRIZIONE");
			int i = 5;

			while (rs.next()) {

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
				retVal += "layer.infourl." + i + "="+webPath+"/SpProfessionista/jsp/protected/map/info.jsp?ds="+datasource+"\n";

				i++;

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

	return retVal;
}
%>

<%=scriviLayerCat(request.getParameter("ente"), "http://"+request.getServerName()+":"+ request.getServerPort(), request.getParameter("ds"))%>