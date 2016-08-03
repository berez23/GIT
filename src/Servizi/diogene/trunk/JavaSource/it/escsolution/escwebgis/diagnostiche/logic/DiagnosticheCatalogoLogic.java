package it.escsolution.escwebgis.diagnostiche.logic;

import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.webred.cet.permission.CeTUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class DiagnosticheCatalogoLogic {
	
	public static final String DATE = "DATE";
	public static final String CONTA = "CONTA";
	public static final String ID = "ID";
	public static final String DESCRIZIONE = "DESCRIZIONE";
	public static final String TOOLTIPTEXT = "TOOLTIPTEXT";
	public static final String ULTIME = "ULTIME";
	public static final String ZIP = ".zip";

	
	public ArrayList<Hashtable<String, Object>> getListaDiagnostiche(String tipo, HttpServletRequest request) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		ArrayList<Hashtable<String, Object>> retVal = new ArrayList<Hashtable<String, Object>>();
		try {
			Context cont = new InitialContext();
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			
			DataSource theDataSource = (DataSource)cont.lookup(es.getDataSourceIntegrato());
			
			conn = theDataSource.getConnection();
			String sql = "SELECT (SELECT COUNT(*) FROM DIA_LOG_ESEC WHERE DIA_LOG_ESEC.IDCATALOGODIA = DIA_CATALOGO.IDCATALOGODIA) AS CONTA, " +
					"IDCATALOGODIA, DESCRIZIONE, SQLCOMMANDPROP, TOOLTIPTEXT FROM DIA_CATALOGO WHERE ABILITATA = 1 AND UPPER(RTRIM(LTRIM(NOME))) = ? ORDER BY IDCATALOGODIA";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tipo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("IDCATALOGODIA");
				if (verificaPropConfig(conn, id, rs.getString("SQLCOMMANDPROP"))) {
					Hashtable<String, Object> riga = new Hashtable<String, Object>();
					riga.put(CONTA, new Integer(rs.getInt("CONTA")));
					riga.put(ID, new Integer(id));
					riga.put(DESCRIZIONE, rs.getString("DESCRIZIONE"));
					String tooltipText = "";
					if (rs.getObject("TOOLTIPTEXT") != null) {
						tooltipText = rs.getString("TOOLTIPTEXT").trim();
					}
					riga.put(TOOLTIPTEXT, tooltipText);					
					String sql1 = "SELECT DISTINCT DATA_RIF FROM DIA_LOG_ESEC, DIA_CATALOGO WHERE DIA_CATALOGO.NOME = ? AND DIA_CATALOGO.IDCATALOGODIA = DIA_LOG_ESEC.IDCATALOGODIA ORDER BY DATA_RIF DESC";
					pstmt1 = conn.prepareStatement(sql1);
					pstmt1.setString(1, tipo);
					rs1 = pstmt1.executeQuery();
					ArrayList<String> date = new ArrayList<String>();
					int conta = 1;
					while (rs1.next()) {
						if (conta <= 3) {
							date.add(new SimpleDateFormat("dd/MM/yyyy").format(rs1.getDate("DATA_RIF")));
							conta++;
						}					
					}
					riga.put(DATE, date);
					
					ArrayList<Hashtable<String, String>> ultime = new ArrayList<Hashtable<String, String>>();
					for (String data: date) {
						pstmt1.cancel();
						sql1 = "SELECT * FROM DIA_LOG_ESEC WHERE IDCATALOGODIA = ? AND DATA_RIF = ? ORDER BY DATA_ESEC DESC";
						pstmt1 = conn.prepareStatement(sql1);
						pstmt1.setInt(1, id);
						pstmt1.setDate(2, new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(data).getTime()));
						rs1 = pstmt1.executeQuery();
						Hashtable<String, String> ultima = new Hashtable<String, String>();
						if (rs1.next()) {						
							ultima.put("" + rs1.getInt("NUM_REC"), rs1.getString("PATH_FILE"));
						} else {
							ultima.put("-", "");
						}
						ultime.add(ultima);
						conta++;
					}				
					
					riga.put(ULTIME, ultime);
					retVal.add(riga);
				}				
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs1 != null)
					rs1.close();
				if(pstmt1 != null)
					pstmt1.close();
				if(rs != null)
					rs.close();
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retVal;
	}
	
	private boolean verificaPropConfig(Connection conn, int idCatalogoDia, String sqlCommandProp) throws Exception {
		String codEnte = "";
		String nomeProp = "" + idCatalogoDia;
		if (sqlCommandProp != null && sqlCommandProp.trim().startsWith("PROPERTY@") && sqlCommandProp.trim().length() > "PROPERTY@".length()) {
			nomeProp = sqlCommandProp.trim().substring(sqlCommandProp.trim().indexOf("PROPERTY@") + "PROPERTY@".length());
		}
		ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
		while (rsEnte.next()) {
			codEnte = rsEnte.getString("codent");
		}
		boolean flDisabilita = false;
		String disabilita = getDiaProperty(codEnte.toUpperCase() + ".dia.disabilita");
		if (disabilita != null && !disabilita.equals("")) {
			String[] disabilitate = disabilita.split(",");
			for (String disabilitata : disabilitate) {
				if (disabilitata.equals(nomeProp)) {
					flDisabilita = true;
					break;
				}
			}
		}
		boolean flEsclusiva = false;
		boolean flEsclusivaEnte = false;
		String esclusiva = getDiaProperty("dia." + nomeProp + ".esclusiva");		
		if (esclusiva != null && !esclusiva.equals("")) {
			flEsclusiva = true;
			String[] esclusiveEnte = esclusiva.split(",");
			for (String esclusivaEnte : esclusiveEnte) {
				if (esclusivaEnte.equalsIgnoreCase(codEnte)) {
					flEsclusivaEnte = true;
					break;
				}
			}
		}			
		return ((!flDisabilita && (!flEsclusiva || flEsclusivaEnte)) ||
				(flDisabilita && flEsclusiva && flEsclusivaEnte));
	}
	
	public void download(HttpServletResponse response, String pathFile) throws Exception {
		try {
			//aggiunto && false per invalidare la condizione 
			//e fare apri/salva di uno zip senza estrarre il file in esso contenuto
			if (pathFile.trim().toLowerCase().endsWith(ZIP) && false) {
				ZipFile zipFile = new ZipFile(pathFile);
			    Enumeration<? extends ZipEntry> entries = zipFile.entries();
			    if (entries.hasMoreElements()) {
			    	//si assume che nello zip ci sia un unico file, così come viene creato dalla regola
				    ZipEntry entry = (ZipEntry)entries.nextElement();
				    response.setContentType("application/download");
					response.setHeader("Content-Disposition", "attachment; filename= \"" + entry.getName() + "\"");				
					java.io.InputStream in = zipFile.getInputStream(entry);
					int size = in.available();
					response.setContentLength(size);
					byte[] ab = new byte[size];
					OutputStream os = response.getOutputStream();
					int bytesread = 0;
					while (bytesread > -1) {
						bytesread = in.read(ab, 0, size);
						if (bytesread > -1)
							os.write(ab, 0, bytesread);
					}				
					in.close();
					os.flush();
					os.close();
				    zipFile.close();
			    } else {
			    	throw new Exception("Impossibile estrarre il file da scaricare");
			    }
			} else {
				//prima del nome del file può esserci / oppure \
				int myIndex = pathFile.trim().lastIndexOf("\\");
				if (pathFile.trim().lastIndexOf("/") > myIndex) {
					myIndex = pathFile.trim().lastIndexOf("/");
				}
				response.setContentType("application/download");
				response.setHeader("Content-Disposition", "attachment; filename= \"" + pathFile.trim().substring(myIndex + 1) + "\"");
				java.io.InputStream in = new FileInputStream(new File(pathFile));
				int size = in.available();
				response.setContentLength(size);
				byte[] ab = new byte[size];
				OutputStream os = response.getOutputStream();
				int bytesread = 0;
				while (bytesread > -1) {
					bytesread = in.read(ab, 0, size);
					if (bytesread > -1)
						os.write(ab, 0, bytesread);
				}				
				in.close();
				os.flush();
				os.close();
			}					    
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private String getDiaProperty(String propName) {
        String fileName =  "dia.properties";
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(fileName);
        Properties props = new Properties();        
        try {
        	props.load(is);
        } catch (Exception e) {
			return null;
		}        
        String p = props.getProperty(propName);
        return p;
	}
	
	public static final String eliminaProblemaApiciJS(String testo) {
		String newTesto = "";
		for (int i = 0; i < testo.length(); i++) {
			if (testo.charAt(i) == '\'') {
				newTesto += "\\'";
			} else {
				newTesto += testo.charAt(i);
			}			
		}
		return newTesto;
	}
	
}
