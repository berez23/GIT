<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="org.apache.log4j.Logger"%>

<%!public String scriviLayerMap(String ente, String webPath, String ana, String civ, String fgl, String par, String sub, String fogli, String particelle, String datasource, String user, String codLotto) {
		Logger logger = Logger.getLogger("gitland.log");
		
		String retVal = "";
		String param = null;
		String paramLotto = null;
		
		logger.debug("map.properties.jsp codLotto: " + codLotto);
		
		if(ana != null)
			param = "ana-" + ana;
		if(civ != null)
			param = "civ-" + civ;
		if(par != null || particelle != null)
			param = "par-" + fogli + "-" + particelle;
		if(codLotto != null)
			paramLotto = "lot-" + codLotto;
		int i=1;

		retVal += "#url of dbamp asj servlet to use for query \n"
				+ "dbmapasjservlet="+webPath+"/DbMAP_ASJ_servlet/"+ ente + "/DbMAPservlet\n"+

				"#states if start marker is temporary\n"+
				"startmarkertemp=1\n" +		
				
				"#url to use for information click \n"+ "infourl="+webPath+"/viewerjs_GitLand/extra/viewerjs/infoStrati?1=1\n"+

				"#url for 3d - optional\n"+ "threedimensionalurl="+webPath+"/GitLand/jsp/public/virtualearth.faces?1=1\n"+

				"#url for pictometry - optional \n"+ "pictometryurl="+webPath+"/GitLand/jsp/public/streetview/streetview.faces?1=1\n"+

				"#optional where clause to add when seeking for via/civico; no start AND required\n"+ "seek.via.sql.where=1=1\n";
						
		
		if ("C618".equals(ente) || "C072".equals(ente) || "C208".equals(ente) || "C925".equals(ente) || "G170".equals(ente) || "G869".equals(ente) || "H410".equals(ente) || "H630".equals(ente) || "L380".equals(ente) || "L494".equals(ente)) {
			retVal += "layer.dbmapjs.name."+i+"=Foto 2007\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2007\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2007\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto 2009\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2009\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2009\n";
			i++;		
		}
		
		else if ("C349".equals(ente)) {
			retVal += "layer.dbmapjs.name."+i+"=Foto 2007\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2007\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2007\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto lug2011\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto lug2011\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto lug2011\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto ago2011\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto ago2011\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto ago2011\n";
			i++;		
		}
		
		else {
			retVal += "layer.dbmapjs.name."+i+"=Foto\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto\n";
			i++;
		}
				
		retVal += "layer.dbmapjs.name."+i+"=Fogli\n"
				+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
				+ "layer.dbmapjs.format."+i+"=image/gif\n"
				+ "layer.menu.name."+i+"=Fogli\n"
				+ "layer.dbmapjs.zoommin."+i+"=1\n"
				+ "layer.dbmapjs.zoomout."+i+"=15\n"
				+ "layer.dbmapjs.visible."+i+"=1\n"
				+ "layer.dbmapjs.opacity."+i+"=90\n"
				+ "layer.dbmapjs.layercolorlist."+i+"=0 0 255\n"
				+ "layer.dbmapjs.layerlist."+i+"=Fogli\n";
		i++;
		
		retVal += "layer.dbmapjs.name."+i+"=Particelle\n"
				+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
				+ "layer.dbmapjs.format."+i+"=image/gif\n"
				+ "layer.menu.name."+i+"=Particelle\n"
				+ "layer.dbmapjs.zoommin."+i+"=1\n"
				+ "layer.dbmapjs.zoomout."+i+"=15\n"
				+ "layer.dbmapjs.visible."+i+"=0\n"
				+ "layer.dbmapjs.opacity."+i+"=40\n"
				+ "layer.dbmapjs.layercolorlist."+i+"=60 179 113\n"
				+ "layer.dbmapjs.layerlist."+i+"=Particelle\n";
		i++;
		
		retVal += "layer.dbmapjs.name."+i+"=Fabbricati\n"
				+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
				+ "layer.dbmapjs.format."+i+"=image/gif\n"
				+ "layer.menu.name."+i+"=Fabbricati\n"
				+ "layer.dbmapjs.zoommin."+i+"=1\n"
				+ "layer.dbmapjs.zoomout."+i+"=15\n"
				+ "layer.dbmapjs.visible."+i+"=0\n"
				+ "layer.dbmapjs.opacity."+i+"=70\n"
				+ "layer.dbmapjs.layercolorlist."+i+"=255 215 0\n"
				+ "layer.dbmapjs.layerlist."+i+"=Fabbricati\n";
		i++;
		
		retVal += "layer.dbmapjs.name."+i+"=Civici\n"
				+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0-"+datasource+"\n"
				+ "layer.dbmapjs.format."+i+"=image/gif\n"
				+ "layer.menu.name."+i+"=Civici\n"
				+ "layer.dbmapjs.zoommin."+i+"=1\n"
				+ "layer.dbmapjs.zoomout."+i+"=15\n"
				+ "layer.dbmapjs.visible."+i+"=0\n"
				+ "layer.dbmapjs.opacity."+i+"=90\n"
				+ "layer.dbmapjs.layercolorlist."+i+"=0 0 0\n"
				+ "layer.dbmapjs.layerlist."+i+"=Civici\n";
		i++;
	
		//gitLand
 		logger.debug("map.properties.jsp paramLotto: " + paramLotto);
		/* if (paramLotto != null){
			retVal += "layer.dbmapjs.name."+i+"=Lotti\n"
				+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0"+ "-"+ datasource+"-" +paramLotto+"\n"
				+ "layer.dbmapjs.format."+i+"=image/gif\n"
				+ "layer.menu.name."+i+"=Lotti\n"
				+ "layer.dbmapjs.zoommin."+i+"=1\n"
				+ "layer.dbmapjs.zoomout."+i+"=15\n"
				+ "layer.dbmapjs.visible."+i+"=1\n"
				+ "layer.dbmapjs.opacity."+i+"=60\n"
				+ "layer.dbmapjs.layercolorlist."+i+"=0 100 0\n"
				+ "layer.dbmapjs.layerlist."+i+"=Lotti Insediamenti\n";
		}else{
			retVal += "layer.dbmapjs.name."+i+"=Lotti\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0"+ "-"+ datasource+"\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Lotti\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=1\n"
					+ "layer.dbmapjs.opacity."+i+"=60\n"
					+ "layer.dbmapjs.layercolorlist."+i+"=0 100 0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Lotti Insediamenti\n";
		} */
		
		
		//i++;
		
		
				
		Statement st = null;
		ResultSet rs = null;
		Connection cnn = null;
		Statement st2 = null;
		ResultSet rs2 = null;
		Statement st3 = null;
		ResultSet rs3 = null;
		try {
			Context cont = new InitialContext();
			DataSource theDataSource = null;
			theDataSource = (DataSource) cont.lookup(datasource);
			cnn = theDataSource.getConnection();
			
			//lista layer
			st = cnn.createStatement();
			
			boolean recuperoCataloghi = true;
			if (recuperoCataloghi){	
				//rs = st.executeQuery("SELECT DESCRIZIONE, ID_CAT FROM SITICATALOG order by DESCRIZIONE");
				rs = st.executeQuery("SELECT * FROM (" +
						"SELECT DESCRIZIONE, ID_CAT, sqldescr,'70' as opacita,shape_type FROM SITICATALOG " +
						"UNION " +
						"SELECT des_layer, id, sql_deco,opacity,shape_type FROM pgt_sql_layer WHERE flg_publish = 'Y') " +
						"ORDER BY DESCRIZIONE");
			
				while (rs.next()) {
					
					String layer = rs.getString("DESCRIZIONE");
					String sqlDeco = rs.getString("SQLDESCR");
					String manylayer = "";
					String manycolor = "";
					String opacita = rs.getString("OPACITA");
					st2 = cnn.createStatement();
					
					String visibile = "0";
					if (layer != null && layer.equalsIgnoreCase("CENSIMENTO AZIENDALE"))
						visibile = "1";
					
					String extraParam = ente +"-"+ rs.getString("ID_CAT") + "-" + datasource;
					if (paramLotto != null){
						extraParam += "-" +paramLotto;
					}
					extraParam += "\n";
									
					//rs2 = st2.executeQuery("SELECT codut || ' - ' || DESCRIZIONE AS DESCRIZIONE FROM sitideco_catalog WHERE ID_CAT = "+ rs.getString("ID_CAT") +" order by DESCRIZIONE");
					rs2 = st2.executeQuery(sqlDeco + " ORDER BY DESCRIZIONE");
					while (rs2.next()) {
						manylayer += ","+rs2.getString("DESCRIZIONE").replaceAll(",","\\.").replaceAll("'", " ").replaceAll(">", "Magg.").replaceAll("<", "Min.");
						try {
							if(rs2.getString("COLORE") != null){
								st3 = cnn.createStatement();
															
								rs3 = st3.executeQuery("SELECT RGB FROM PGT_SQL_PALETTE WHERE ID = " + (rs.getString("SHAPE_TYPE").equals("POLYGON")?rs2.getString("COLORE"):rs2.getString("COLORELINEA")));
								while (rs3.next()) {
									manycolor += ","+ (rs3.getString("RGB"));
								}
								if (rs3 != null)
									rs3.close();
								if (st3 != null)
									st3.close();
							}else 	manycolor += ",56 56 56";
						} catch (SQLException e) {
							logger.error("ERRORE su Map.properties", e);
						}
					}
					if(!manylayer.equals(""))
						layer = manylayer.substring(1, manylayer.length());
					if(!manycolor.equals(""))
						manycolor = manycolor.substring(1, manycolor.length());
					
					retVal += "layer.dbmapjs.name."+i+"="+ rs.getString("DESCRIZIONE") +"\n"
							+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+extraParam
							+ "layer.dbmapjs.format."+i+"=image/gif\n"
							+ "layer.menu.name."+i+"="+ rs.getString("DESCRIZIONE") +"\n"
							+ "layer.dbmapjs.zoommin."+i+"=1\n"
							+ "layer.dbmapjs.zoomout."+i+"=15\n"
							+ "layer.dbmapjs.visible."+i+"="+visibile+"\n"
							+ "layer.dbmapjs.opacity."+i+"="+ opacita +"\n"
							+ "layer.dbmapjs.layerlist."+i+"="+ layer +"\n"
							+ "layer.dbmapjs.layercolorlist."+i+"="+ manycolor +"\n";
					i++;
				}
			}
			
			if(param!=null){
				retVal += "layer.dbmapjs.name."+i+"=Selezione\n"
						+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet_GitLand/dinamic/WMSservletEx?extraparam="+ente +"-0"+ "-"+ datasource + "-" +param +"\n"
						+ "layer.dbmapjs.format."+i+"=image/gif\n"
						+ "layer.menu.name."+i+"=Selezione\n"
						+ "layer.dbmapjs.zoommin."+i+"=1\n"
						+ "layer.dbmapjs.zoomout."+i+"=15\n"
						+ "layer.dbmapjs.visible."+i+"=1\n"
						+ "layer.dbmapjs.opacity."+i+"=80\n"
						+ "layer.dbmapjs.layercolorlist."+i+"=255 0 0\n"
						+ "layer.dbmapjs.layerlist."+i+"=Selezione\n";
				i++;
			}
					
			retVal += "layercount=" + --i;
			
		} catch (SQLException e) {
			logger.error("ERRORE su Map.properties", e);
		} catch (NamingException e) {
			logger.error("ERRORE su Map.properties", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (cnn != null)
					cnn.close();
				if (rs2 != null)
					rs2.close();
				if (st2 != null)
					st2.close();
				if (rs3 != null)
					rs3.close();
				if (st3 != null)
					st3.close();
			} catch (SQLException e) {
				logger.error("ERRORE su Map.properties", e);
			}
		}
		logger.debug("Map.properties: --->>" +retVal);
		return retVal;
	}%>

<%=scriviLayerMap(request.getParameter("ente"), "http://"+request.getServerName()+":"+ request.getServerPort(), request.getParameter("pkana"), request.getParameter("pkciv"), request.getParameter("foglio"), request.getParameter("particella"), request.getParameter("sub"),request.getParameter("fogli"), request.getParameter("particelle"), request.getParameter("ds"), request.getParameter("user"), request.getParameter("codLotto") )%>
