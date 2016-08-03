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
	Logger logger = Logger.getLogger("fascicolobene.log");
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
				"layer.infourl.1="+webPath+"/FascicoloBene_WEB/jsp/protected/map/info.jsp?ds="+datasource+"\n" +*/
				
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
				"layer.infourl." + i + "="+webPath+"/FascicoloBene_WEB/jsp/protected/map/info.jsp?TIPO_LAYER="+tipoLayer+"&ds="+datasource+"\n";
				
		i++;		//2				
		retVal += "\n";
		retVal +=
				"layer.tableName." + i + "=CAT_PARTICELLE_GAUSS\n" +
				"layer.fieldShape." + i + "=geometry\n" +
				"layer.fieldPk." + i + "=pk_particelle\n" +
				"layer.name." + i + "=Fabbricati\n" +
				"layer.where." + i + "=COMUNE in (select uk_belfiore from ewg_tab_comuni) and layer='FABBRICATI'\n" +
				"layer.infourl." + i + "="+webPath+"/FascicoloBene_WEB/jsp/protected/map/info.jsp?TIPO_LAYER="+tipoLayer+"&ds="+datasource+"\n" ;
				
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
				"layer.infourl." + i + "="+webPath+"/FascicoloBene_WEB/jsp/protected/map/info.jsp?TIPO_LAYER="+tipoLayer+"&ds="+datasource+"\n" ;
				
		i++;		//4
		retVal += "\n";
		
		retVal +=
				"layer.tableName." + i + "=CAT_PARTICELLE_GAUSS_2\n" +
				"layer.fieldShape." + i + "=geometry\n" +
				"layer.fieldPk." + i + "=pk_particelle\n" +
				"layer.name." + i + "=Fascicolo Fabbricato\n" +
				"layer.where." + i + "=COMUNE in (select COD_NAZIONALE from sit_ente E , SITICOMU C  WHERE E.CODENT = C.CODI_FISC_LUNA ) and layer='FABBRICATI'\n" +
				"layer.infourl." + i + "="+webPath+"/FascicoloBene_WEB/jsp/protected/map/info.jsp?TIPO_LAYER="+tipoLayer+"&ds="+datasource+"\n" ;
			
		i++;
		retVal += "\n";
		
		retVal +=
				"layer.tableName." + i + "=" +
					"(SELECT p.id, b.chiave,b.des_tipo_bene, p.foglio, P.PARTICELLA, trim(i.tipo_via||' '||i.descr_via) indirizzo, i.civico, i.des_comune, i.prov, decode(i.flg_principale,1,'Si','No') ind_principale, p.shape as shape "+
					"FROM dm_b_bene b, dm_b_mappale_part p, dm_b_indirizzo i "+
					"WHERE b.id = p.bene_id and b.id=I.DM_B_BENE_ID and B.DT_FINE_VAL is null and i.dt_fine_Val is null AND p.TIPO='F')\n" +
				"layer.fieldShape." + i + "=shape\n" +
				"layer.fieldPk." + i + "=id\n" +
				"layer.name." + i + "=Inventario - Mappali\n" +
				"layer.where." + i + "=1=1 \n" +
				"layer.infourl." + i + "="+webPath+"/FascicoloBene_WEB/jsp/protected/map/info.jsp?TIPO_LAYER="+tipoLayer+"&ds="+datasource+"\n" ;
		i++;
		retVal += "\n";
		
		retVal +=
				"layer.tableName." + i + "=" +
					"(SELECT p.id, b.chiave,b.des_tipo_bene, p.foglio, P.PARTICELLA, t.qualita, t.superficie, t.classe, p.shape as shape "+
					" FROM dm_b_bene b, dm_b_mappale_part p , dm_b_terreno t "+
					"WHERE b.id = p.bene_id and t.id=p.id and t.dm_b_bene_id=b.id and t.dt_fine_val is null and B.DT_FINE_VAL is null  AND p.TIPO='T')\n" +
				"layer.fieldShape." + i + "=shape\n" +
				"layer.fieldPk." + i + "=id\n" +
				"layer.name." + i + "=Inventario - Terreni\n" +
				"layer.where." + i + "=1=1 \n" +
				"layer.infourl." + i + "="+webPath+"/FascicoloBene_WEB/jsp/protected/map/info.jsp?TIPO_LAYER="+tipoLayer+"&ds="+datasource+"\n" ;
		i++;
	
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
				retVal += "layer.infourl." + i + "="+webPath+"/FascicoloBene_WEB/jsp/protected/map/info.jsp?TIPO_LAYER="+tipoLayer+"&ds="+datasource+"\n";
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
		logger.debug("infostrati.properties -->>" + retVal);
		return retVal;
}
%>

<%=scriviLayerCat(request.getParameter("ente"), "http://"+request.getServerName()+":"+ request.getServerPort(), request.getParameter("ds"))%>