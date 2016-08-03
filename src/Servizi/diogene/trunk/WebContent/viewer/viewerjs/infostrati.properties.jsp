<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.escsolution.escwebgis.common.EscLogic"%>
<%@page import="java.io.*"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="org.apache.log4j.Logger"%>
<%!static Logger log = Logger.getLogger("diogene.log");%>

<%!
public boolean isFonteAbilitata(String ente, int codFonte){
	boolean abilitato = false;
	Statement st = null;
	ResultSet rs = null;
	Connection cnn = null;
	
	try {
		Context cont = new InitialContext();
		DataSource theDataSource = null;
		EnvSource es = new EnvSource(ente);
		theDataSource = (DataSource) cont.lookup(es.getDataSource());
		cnn = theDataSource.getConnection();

		st = cnn.createStatement();		
		rs = st.executeQuery("select * from am_fonte_comune fc " +
							 " where  fc.fk_am_fonte = '"+codFonte+"' and fk_am_comune = '"+ente.toUpperCase()+"'");
		
		if (rs.next()) 
			abilitato = true;
		
		//log.info(">>>>>>>>>>>>>>>info>>>>>>>>>>>>>>>>>>>>>>Fonte " + codFonte + " abilitata per l'ente " + ente + " :" + abilitato);
		
		
	} catch (SQLException e) {
		log.error("Errore visualizzazione layer da catalogo", e);

	} catch (NamingException e) {
		log.error("Errore connessione", e);

	} finally {
		try {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (cnn != null)
				cnn.close();
		} catch (SQLException e) {
		}
	}
	
	return abilitato;
}


public String scriviLayerCat(String ente, String webPath, String viewFasCiv)
{
	String retVal="";
	String tipoLayer = "Catasto";
		int i = 1;
		retVal += "#if present it is an http url where to read properties instead of this file\n" +
				"#filepropertyurl=http://caleffi:8998/sitipatri/extrapub/viewerjs/infostrati2.properties\n" +

				/*"layer.tableName.1=SITIFGLC B, SITICOMU C\n" +
				"layer.fieldShape.1=B.SHAPE\n" +
				"layer.fieldPk.1=B.FOGLIO\n" +
				"layer.name.1=Fogli\n" +
				"layer.where.1=B.COD_NAZIONALE = C.COD_NAZIONALE AND C.CODI_FISC_LUNA in (select uk_belfiore from ewg_tab_comuni)\n" +
				"layer.infourl.1="+webPath+"/diogene/viewer/infoPart.jsp\n" +*/
				
				"#name of table\n" +
				"layer.tableName." + i + "=CAT_PARTICELLE_GAUSS_2\n" +
				"#name of shape field\n" +
				"layer.fieldShape." + i + "=geometry\n" +
				"#name of primary key field\n" +
				"layer.fieldPk." + i + "=pk_particelle\n" +
				"#name of layer\n" +
				"layer.name." + i + "=Particelle\n" +
				"#where clause for query (optional)\n" +
				"layer.where." + i + "=COMUNE in (select COD_NAZIONALE from sit_ente E , SITICOMU C  WHERE E.CODENT = C.CODI_FISC_LUNA ) and layer='PARTICELLE'\n" +
				"#full url without parameters for info\n" +
				"layer.infourl." + i + "="+webPath+"/diogene/viewer/infoPart.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+"\n";
				
		i++;		//2				
		retVal += "\n";
		retVal +=
				"layer.tableName." + i + "=CAT_PARTICELLE_GAUSS_2\n" +
				"layer.fieldShape." + i + "=geometry\n" +
				"layer.fieldPk." + i + "=pk_particelle\n" +
				"layer.name." + i + "=Fabbricati\n" +
				"layer.where." + i + "=COMUNE in (select COD_NAZIONALE from sit_ente E , SITICOMU C  WHERE E.CODENT = C.CODI_FISC_LUNA ) and layer='FABBRICATI'\n" +
				"layer.infourl." + i + "="+webPath+"/diogene/viewer/infoPart.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+"\n" ;
				
		i++;		//3		
		tipoLayer = "Fascicoli";		
		boolean addFasCiv = viewFasCiv != null && viewFasCiv.equalsIgnoreCase("true") ? true : false;		
		if (addFasCiv) {
			retVal += "\n";
			retVal +=
					"layer.tableName." + i + "=(SELECT siticivi.cod_nazionale AS civi_cod_nazionale,siticomu.cod_nazionale AS comu_cod_nazionale,siticivi.data_fine_val, pkid_civi, shape, sitidstr.prefisso || ' ' || sitidstr.nome via, siticivi.civico,siticomu.codi_fisc_luna FROM siticivi, sitidstr, siticomu WHERE sitidstr.pkid_stra = siticivi.pkid_stra)\n" +
					"layer.fieldShape." + i + "=shape\n" +
					"layer.fieldPk." + i + "=pkid_civi\n" +
					"layer.name." + i + "=Fascicolo civico\n" +
					"layer.buffer." + i + "=1\n" +
					"layer.where." + i + "=civi_cod_nazionale = comu_cod_nazionale AND codi_fisc_luna IN (SELECT uk_belfiore FROM ewg_tab_comuni) AND data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')\n" +
					"layer.infourl." + i + "="+webPath+"/diogene/viewer/infoPart.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+"\n" ;
					
			i++;		//4
		}		
		retVal += "\n";
		retVal +=						
				"layer.tableName." + i + "=(SELECT siticivi.cod_nazionale AS civi_cod_nazionale,siticomu.cod_nazionale AS comu_cod_nazionale,siticivi.data_fine_val, pkid_civi, shape, sitidstr.prefisso || ' ' || sitidstr.nome via, siticivi.civico,siticomu.codi_fisc_luna FROM siticivi, sitidstr, siticomu WHERE sitidstr.pkid_stra = siticivi.pkid_stra)\n" +
				"layer.fieldShape." + i + "=shape\n" +
				"layer.fieldPk." + i + "=pkid_civi\n" +
				"layer.name." + i + "=Correlazione civico\n" +
				"layer.buffer." + i + "=1\n" +
				"layer.where." + i + "=civi_cod_nazionale = comu_cod_nazionale AND codi_fisc_luna IN (SELECT uk_belfiore FROM ewg_tab_comuni) AND data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')\n" +
				"layer.infourl." + i + "="+webPath+"/diogene/viewer/infoPart.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+"\n";
		
		i++;		//4
		retVal += "\n";
		retVal +=
				"layer.tableName." + i + "=CAT_PARTICELLE_GAUSS_2\n" +
				"layer.fieldShape." + i + "=geometry\n" +
				"layer.fieldPk." + i + "=pk_particelle\n" +
				"layer.name." + i + "=Fascicolo Fabbricato\n" +
				"layer.where." + i + "=COMUNE in (select COD_NAZIONALE from sit_ente E , SITICOMU C  WHERE E.CODENT = C.CODI_FISC_LUNA ) and layer='FABBRICATI'\n" +
				"layer.infourl." + i + "="+webPath+"/diogene/viewer/infoPart.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+"\n" ;
			
		retVal += "\n";
		
			if(this.isFonteAbilitata(ente, 29)){
				
				tipoLayer = "Ecografico Catastale";
				
				i++;		//5				
				retVal +=		
					"layer.tableName." + i + "=EC_TOP_CIVICI\n" +
					"layer.fieldShape." + i + "=SHAPE\n" +
					"layer.fieldPk." + i + "=ID\n" +
					"layer.name." + i + "=Civici Ecografico\n" +
					"layer.buffer." + i + "=2\n" +
					"layer.where." + i + "=FK_COMUNI_BELF = '"+ente+"'\n" +
					"layer.infourl." + i + "="+webPath+"/diogene/viewer/infoPart.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+"\n";
				
				i++;		//6				
				retVal +=			
					"layer.tableName." + i + "=EC_TOP_TRATTI_STRADALI\n" +
					"layer.fieldShape." + i + "=SHAPE\n" +
					"layer.fieldPk." + i + "=ID\n" +
					"layer.name." + i + "=Strade Ecografico\n" +
					"layer.buffer." + i + "=20\n" +
					"layer.where." + i + "=FK_COMUNI_BELF = '"+ente+"'\n" +
					"layer.infourl." + i + "="+webPath+"/diogene/viewer/infoTable.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+"\n";
					
				i++;		//7				
				retVal +=			
					"layer.tableName." + i + "=EC_FAB_FABBRICATI\n" +
					"layer.fieldShape." + i + "=SHAPE\n" +
					"layer.fieldPk." + i + "=ID\n" +
					"layer.name." + i + "=Fabbricati Ecografico\n" +
					"layer.buffer." + i + "=2\n" +
					"layer.where." + i + "=FK_COMUNI_BELF = '"+ente+"'\n" +
					"layer.infourl." + i + "="+webPath+"/diogene/viewer/infoTable.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+"\n";		
			}
			
			log.info(retVal);
						
		//aggiungo proprietà ottenute da un resultset
		Statement st = null;
		ResultSet rs = null;
		Connection cnn = null;
		try{
			Context cont = new InitialContext();
			DataSource theDataSource = null;
			EnvSource es = new EnvSource(ente);
			theDataSource = (DataSource) cont.lookup(es.getDataSource());
			cnn = theDataSource.getConnection();

			retVal += "\n";
			
			st = cnn.createStatement();
			//rs = st.executeQuery("SELECT ID_FIELD, TABELLA, DESCRIZIONE, STORICIZZATO,ID_CAT FROM SITICATALOG order by DESCRIZIONE");
			rs = st.executeQuery("SELECT * FROM (" +
					"SELECT ID_FIELD, TABELLA, DESCRIZIONE AS descrizione, STORICIZZATO,ID_CAT, shape_type, 'Catalogo SITI' as tipo_layer FROM SITICATALOG " +
					"UNION " +
					"SELECT name_col_id, name_table, des_layer as descrizione, 0, id, shape_type, tipo_layer FROM pgt_sql_layer WHERE flg_publish = 'Y' AND flg_hide_info<>'Y') " +
					"ORDER BY tipo_layer, DESCRIZIONE");
			i++; 

			while (rs.next()) {
				
				tipoLayer = rs.getString("TIPO_LAYER");
				
				retVal += "layer.tableName." + i + "=" + rs.getString("TABELLA") + "\n";
				retVal += "layer.fieldShape." + i + "=SHAPE"  + "\n";
				retVal += "layer.fieldPk." + i + "=" + rs.getString("ID_FIELD") + "\n";
				retVal += "layer.name." + i + "=" + rs.getString("DESCRIZIONE") + "\n";
				if (rs.getString("SHAPE_TYPE").equals("POINT"))
					retVal += "layer.buffer." + i + "=1"  + "\n";
				long stor = rs.getLong("STORICIZZATO");
				if (stor == 1)
					retVal += "layer.where." + i + "=ID_CAT=" + rs.getString("ID_CAT") + " AND DATA_FINE_VAL=TO_DATE('99991231000000', 'YYYYMMDDHH24MISS')" + "\n";
				else
					retVal += "layer.where." + i + "=1=1" + "\n";
				
				String descrizione = rs.getString("DESCRIZIONE");
				if ("STA04: ZONE CENSUARIE".equals(descrizione) ||  
					"STA01: UNITA ABITATIVE CON DOCFA".equals(descrizione)|| 
					"STA02: UNITA ABITATIVE CON DOCFA SOTTOCLASSATE".equals(descrizione)||
					"STA05: DOCFA SU PARTICELLA".equalsIgnoreCase(descrizione)||
					"Reddito medio per famiglia".equals(descrizione)||
					"PREGEO PRESENTATI SU FOGLIO".equals(descrizione)||
					"CERTIFICAZIONI ENERGETICHE EDIFICI".equals(descrizione)||
					"CATEGORIE CATASTALI".equals(descrizione))
					retVal += "layer.infourl." + i + "="+webPath+"/diogene/viewer/infoPart.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+""  + "\n";	
				else
					retVal += "layer.infourl." + i + "="+webPath+"/diogene/viewer/infoTable.jsp?TIPO_LAYER="+tipoLayer+"&es="+ente+""  + "\n";
				i++;

			}

			retVal += "#number of layer to be processed\n";
			retVal += "layercount=" + --i + "\n\n";
			
			retVal += "#url of dbamp asj servlet to use for query\n";
			retVal += "dbmapasjservlet="+webPath+"/DbMAP_ASJ_servlet/" + ente + "/DbMAPservlet";
	
			log.info(retVal);
					
		} catch (SQLException e) {
			log.error("Errore visualizzazione layer da catalogo", e);

		} catch (NamingException e) {
			log.error("Errore connessione", e);

		}finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (cnn != null)
					cnn.close();
			} catch (SQLException e) {
			}
		}

	return retVal;
}
%>

<%=scriviLayerCat(request.getParameter("ente"), "http://"+request.getServerName()+":"+ request.getServerPort(), request.getParameter("viewFasCiv"))%>