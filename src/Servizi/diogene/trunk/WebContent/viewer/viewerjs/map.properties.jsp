<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.escsolution.escwebgis.common.EscLogic"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
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
<%@ page contentType="text/plain;charset=UTF-8"%>
<%!static Logger log = Logger.getLogger("diogene.log");%>
<%!


public boolean isComuneInUmbriaGauss(String ente) {
	boolean umbro = false;
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
		rs = st.executeQuery("select * from am_tab_comuni C where  cod_nazionale = '"+ente.toUpperCase()+"' and SIGLA_PROV IN ('PG','TR')" );
		
		if (rs.next()) 
			umbro = true;
		
		
	} catch (SQLException e) {
		log.error("Errore cercando di verificare se comune umbro", e);

	} catch (NamingException e) {
		log.error("Errore connessione vericando umbria", e);

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
	
	return umbro;
}

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
		
		//log.info(">>>>>>>>>>>>>>>map>>>>>>>>>>>>>>>>>>>>>>Fonte " + codFonte + " abilitata per l'ente " + ente + " :" + abilitato);
		
		
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

public String scriviLayerMap(String ente, String webPath, String ana, String civ, String fgl, String par, String sub) {
		String retVal = "";
		String param = null;
		if(ana != null)
			param = "ana-" + ana;
		if(civ != null)
			param = "civ-" + civ;
		if(par != null)
			param = "par-" + fgl + "-" + par;
		if(sub != null)
			param = "sub-" + fgl + "-" + par + "-" + sub;
		int i=1;

		retVal += "#url of dbamp asj servlet to use for query \n"
				+ "dbmapasjservlet="+webPath+"/DbMAP_ASJ_servlet/"+ ente + "/DbMAPservlet\n"+

				"#states if start marker is temporary\n"+
				"startmarkertemp=1\n" +		
				

				"#url to use for information click \n"+ "infourl="+webPath+"/viewerjs/extra/viewerjs/infoStrati?1=1\n"+

				"#url for 3d - optional\n"+ "threedimensionalurl="+webPath+"/diogene/ewg/virtualearth.jsp?1=1\n"+

				"#url for pictometry - optional \n"+ "pictometryurl="+webPath+"/diogene/ewg/streetviewPopUp.jsp?1=1\n"+

				"#optional where clause to add when seeking for via/civico; no start AND required\n"+ "seek.via.sql.where=1=1\n";
				
		if ("I921".equals(ente)) {
			retVal += "layer.dbmapjs.name."+i+"=Foto 2005\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2005\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2005\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto 1997\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 1997\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 1997\n";
			i++;		
		}
						
		else if ("C618".equals(ente) || "C072".equals(ente) || "C208".equals(ente) || "C925".equals(ente) || "G170".equals(ente) || "G869".equals(ente) || "H410".equals(ente) || "H630".equals(ente) || "L380".equals(ente) || "L494".equals(ente)) {
			retVal += "layer.dbmapjs.name."+i+"=Foto 2007\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2007\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2007\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto 2009\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
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
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2007\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2007\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto lug2011\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto lug2011\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto lug2011\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto ago2011\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto ago2011\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto ago2011\n";
			i++;		
		}
		
		else if ("E617".equals(ente)) {
			retVal += "layer.dbmapjs.name."+i+"=r2\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=r2\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=r2\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=r10\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=r10\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=r10\n";
			i++;		
		}
		
		else if ("L872".equals(ente)) {
			retVal += "layer.dbmapjs.name."+i+"=Foto 2005\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2005\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2005\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto 2006\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2006\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2006\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto 2007\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2007\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2007\n";
			i++;		
			retVal += "layer.dbmapjs.name."+i+"=Foto 2008\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2008\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2008\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto 2009\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2009\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2009\n";
			i++;
			
			retVal += "layer.dbmapjs.name."+i+"=Foto 2010\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2010\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2010\n";
			i++;		

			retVal += "layer.dbmapjs.name."+i+"=Foto 2011\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2011\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2011\n";
			i++;		

			retVal += "layer.dbmapjs.name."+i+"=Foto 2012\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto 2012\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Foto 2012\n";
			i++;		
			}
		
        else if ("C361".equals(ente) || "F138".equals(ente)|| "D615".equals(ente)) {
            retVal += "layer.dbmapjs.name."+i+"=PIANO ASI\n"
                   + "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
                   + "layer.dbmapjs.format."+i+"=image/gif\n"
                   + "layer.menu.name."+i+"=PIANO ASI\n"
                   + "layer.dbmapjs.zoommin."+i+"=1\n"
                   + "layer.dbmapjs.zoomout."+i+"=15\n"
                   + "layer.dbmapjs.visible."+i+"=0\n"
                   + "layer.dbmapjs.layerlist."+i+"=PIANO ASI\n";
       		i++;
            
            retVal += "layer.dbmapjs.name."+i+"=PIANO ASI-VARIANTE\n"
                   + "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
                   + "layer.dbmapjs.format."+i+"=image/gif\n"
                   + "layer.menu.name."+i+"=PIANO ASI-VARIANTE\n"
                   + "layer.dbmapjs.zoommin."+i+"=1\n"
                   + "layer.dbmapjs.zoomout."+i+"=15\n"
                   + "layer.dbmapjs.visible."+i+"=0\n"
                   + "layer.dbmapjs.layerlist."+i+"=PIANO ASI-VARIANTE\n";
            i++;                        
         }

         else if ("A717".equals(ente) ) {
            retVal += "layer.dbmapjs.name."+i+"=PIANO ASI 1\n"
                   + "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
                   + "layer.dbmapjs.format."+i+"=image/gif\n"
                   + "layer.menu.name."+i+"=PIANO ASI 1\n"
                   + "layer.dbmapjs.zoommin."+i+"=1\n"
                   + "layer.dbmapjs.zoomout."+i+"=15\n"
                   + "layer.dbmapjs.visible."+i+"=0\n"
                   + "layer.dbmapjs.layerlist."+i+"=PIANO ASI 1\n";
            i++;
            
            retVal += "layer.dbmapjs.name."+i+"=PIANO ASI 2\n"
                   + "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
                   + "layer.dbmapjs.format."+i+"=image/gif\n"
                   + "layer.menu.name."+i+"=PIANO ASI 2\n"
                   + "layer.dbmapjs.zoommin."+i+"=1\n"
                   + "layer.dbmapjs.zoomout."+i+"=15\n"
                   + "layer.dbmapjs.visible."+i+"=0\n"
                   + "layer.dbmapjs.layerlist."+i+"=PIANO ASI 2\n";
            i++;                        
         }                                             

         else if ("H703".equals(ente) ) {
            retVal += "layer.dbmapjs.name."+i+"=PIANO ASI 2\n"
                   + "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
                   + "layer.dbmapjs.format."+i+"=image/gif\n"
                   + "layer.menu.name."+i+"=PIANO ASI 2\n"
                   + "layer.dbmapjs.zoommin."+i+"=1\n"
                   + "layer.dbmapjs.zoomout."+i+"=15\n"
                   + "layer.dbmapjs.visible."+i+"=0\n"
                   + "layer.dbmapjs.layerlist."+i+"=PIANO ASI 2\n";
            i++;
            
            retVal += "layer.dbmapjs.name."+i+"=PIANO ASI 3\n"
                   + "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
                   + "layer.dbmapjs.format."+i+"=image/gif\n"
                   + "layer.menu.name."+i+"=PIANO ASI 3\n"
                   + "layer.dbmapjs.zoommin."+i+"=1\n"
                   + "layer.dbmapjs.zoomout."+i+"=15\n"
                   + "layer.dbmapjs.visible."+i+"=0\n"
                   + "layer.dbmapjs.layerlist."+i+"=PIANO ASI 3\n";
            i++;                        

            retVal += "layer.dbmapjs.name."+i+"=TERMOVALORIZZATORE\n"
                   + "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
                   + "layer.dbmapjs.format."+i+"=image/gif\n"
                   + "layer.menu.name."+i+"=TERMOVALORIZZATORE\n"
                   + "layer.dbmapjs.zoommin."+i+"=1\n"
                   + "layer.dbmapjs.zoomout."+i+"=15\n"
                   + "layer.dbmapjs.visible."+i+"=0\n"
                   + "layer.dbmapjs.layerlist."+i+"=TERMOVALORIZZATORE\n";
            i++;                        

        }

		
		else {
			retVal += "layer.dbmapjs.name."+i+"=Foto\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Foto\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+("F205".equals(ente)?"layer.dbmapjs.visible."+i+"=1\n": "layer.dbmapjs.visible."+i+"=0\n")
					+ "layer.dbmapjs.layerlist."+i+"=Foto\n";
			i++;
		}


		boolean isUmbria = isComuneInUmbriaGauss(ente);
		
		if(isUmbria){
			retVal += "layer.dbmapjs.name."+i+"=Foto Umbria 2011\n"
			+ "layer.dbmapjs.url."+i+"=http://geo.umbriaterritorio.it/arcgis/services/public/ORTOFOTO_2011_GB/MapServer/WMSServer\n"
			+ "layer.dbmapjs.format."+i+"=image/gif\n"
			+ "layer.menu.name."+i+"=Foto Umbria 2011\n"
			+ "layer.dbmapjs.zoommin."+i+"=1\n"
			+ "layer.dbmapjs.zoomout."+i+"=15\n"
			+ "layer.dbmapjs.visible."+i+"=0\n"
			+ "layer.dbmapjs.layerlist."+i+"=0\n"
			+ "layer.dbmapjs.srs."+i+"=EPSG:3004\n";
			i++;
		}
		
		retVal += "layer.dbmapjs.name."+i+"=Fogli\n"
				+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
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
				+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
				+ "layer.dbmapjs.format."+i+"=image/gif\n"
				+ "layer.menu.name."+i+"=Particelle\n"
				+ "layer.dbmapjs.zoommin."+i+"=1\n"
				+ "layer.dbmapjs.zoomout."+i+"=15\n"
				+ "layer.dbmapjs.visible."+i+"=1\n"
				+ "layer.dbmapjs.opacity."+i+"=40\n"
				+ "layer.dbmapjs.layercolorlist."+i+"=60 179 113\n"
				+ "layer.dbmapjs.layerlist."+i+"=Particelle\n";
		i++;
		
		retVal += "layer.dbmapjs.name."+i+"=Fabbricati\n"
				+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
				+ "layer.dbmapjs.format."+i+"=image/gif\n"
				+ "layer.menu.name."+i+"=Fabbricati\n"
				+ "layer.dbmapjs.zoommin."+i+"=1\n"
				+ "layer.dbmapjs.zoomout."+i+"=15\n"
				+ "layer.dbmapjs.visible."+i+"=1\n"
				+ "layer.dbmapjs.opacity."+i+"=70\n"
				+ "layer.dbmapjs.layercolorlist."+i+"=255 215 0\n"
				+ "layer.dbmapjs.layerlist."+i+"=Fabbricati\n";
		i++;
		
		retVal += "layer.dbmapjs.name."+i+"=Civici\n"
				+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
				+ "layer.dbmapjs.format."+i+"=image/gif\n"
				+ "layer.menu.name."+i+"=Civici\n"
				+ "layer.dbmapjs.zoommin."+i+"=1\n"
				+ "layer.dbmapjs.zoomout."+i+"=15\n"
				+ "layer.dbmapjs.visible."+i+"=0\n"
				+ "layer.dbmapjs.opacity."+i+"=90\n"
				+ "layer.dbmapjs.layercolorlist."+i+"=0 0 0\n"
				+ "layer.dbmapjs.layerlist."+i+"=Civici\n";
		i++;
		
		
		
		
		boolean ecograficoAbilitato = isFonteAbilitata(ente, 29);
		
		if(ecograficoAbilitato){
			
			retVal += "layer.dbmapjs.name."+i+"=Ecografico Catastale\n"
					+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0\n"
					+ "layer.dbmapjs.format."+i+"=image/gif\n"
					+ "layer.menu.name."+i+"=Ecografico Catastale\n"
					+ "layer.dbmapjs.zoommin."+i+"=1\n"
					+ "layer.dbmapjs.zoomout."+i+"=15\n"
					+ "layer.dbmapjs.visible."+i+"=0\n"
					+ "layer.dbmapjs.opacity."+i+"=100\n"
					+ "layer.dbmapjs.layercolorlist."+i+"=0 255 0,0 0 255,255 0 0\n"
					+ "layer.dbmapjs.layerlist."+i+"=Civici Ecografico,Strade Ecografico,Fabbricati Ecografico\n";
			
			i++;
			
		}
				
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
			EnvSource es = new EnvSource(ente);
			theDataSource = (DataSource) cont.lookup(es.getDataSource());
			cnn = theDataSource.getConnection();

			//lista layer
			st = cnn.createStatement();
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
				//rs2 = st2.executeQuery("SELECT codut || ' - ' || DESCRIZIONE AS DESCRIZIONE FROM sitideco_catalog WHERE ID_CAT = "+ rs.getString("ID_CAT") +" order by DESCRIZIONE");
				rs2 = st2.executeQuery(sqlDeco + (sqlDeco.toUpperCase().contains("ORDER BY")?"":" ORDER BY DESCRIZIONE"));
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
					}
				}
				if(!manylayer.equals(""))
					layer = manylayer.substring(1, manylayer.length());
				if(!manycolor.equals(""))
					manycolor = manycolor.substring(1, manycolor.length());
				
				retVal += "layer.dbmapjs.name."+i+"="+ rs.getString("DESCRIZIONE") +"\n"
						+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-"+ rs.getString("ID_CAT") +"\n"
						+ "layer.dbmapjs.format."+i+"=image/gif\n"
						+ "layer.menu.name."+i+"="+ rs.getString("DESCRIZIONE") +"\n"
						+ "layer.dbmapjs.zoommin."+i+"=1\n"
						+ "layer.dbmapjs.zoomout."+i+"=15\n"
						+ "layer.dbmapjs.visible."+i+"=0\n"
						+ "layer.dbmapjs.opacity."+i+"="+ opacita +"\n"
						+ "layer.dbmapjs.layerlist."+i+"="+ layer +"\n"
						+ "layer.dbmapjs.layercolorlist."+i+"="+ manycolor +"\n";
				i++;
			}
			
			if(param!=null){
				retVal += "layer.dbmapjs.name."+i+"=Selezione\n"
						+ "layer.dbmapjs.url."+i+"="+webPath+"/DbMAP_WMS_servlet/dinamic/WMSservletEx?extraparam="+ente +"-0"+ "-"+param +"\n"
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
			
			retVal += "#number of layer defined in this file\n" + "layercount="+--i;
			
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
				if (rs2 != null)
					rs2.close();
				if (st2 != null)
					st2.close();
			} catch (SQLException e) {
			}
		}

		return retVal;
	}%>

<%=scriviLayerMap(request.getParameter("ente"), "http://"+request.getServerName()+":"+ request.getServerPort(), request.getParameter("pkana"), request.getParameter("pkciv"), request.getParameter("foglio"), request.getParameter("particella"), request.getParameter("sub"))%>
