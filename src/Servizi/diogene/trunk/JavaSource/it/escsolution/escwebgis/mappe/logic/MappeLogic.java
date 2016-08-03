package it.escsolution.escwebgis.mappe.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.mappe.bean.Link;
import it.webred.cet.permission.CeTUser;

public class MappeLogic extends EscLogic
{
	private String appoggioDataSource;
		
	public final static String MAPPE_BEAN = "MAPPE_BEAN@MappeLogic";
	public final static String LINK_LIST = "LINK_LIST@MappeLogic";
	
	private final static String SQL_TABLE_CESSATO_CATASTO = "SELECT * FROM TABS WHERE TABLE_NAME = 'CES_CAT'";
	private final static String SQL_SYN_TABLE_CESSATO_CATASTO = "SELECT * FROM SYN WHERE SYNONYM_NAME = 'CES_CAT'";
	private final static String SQL_SELECT_CESSATO_CATASTO = "SELECT DISTINCT SEZ_CESSATO, FG_CESSATO FROM CES_CAT WHERE FG_NUOVO = ? ORDER BY SEZ_CESSATO, FG_CESSATO";

	private static final Hashtable<String, String> mime = new Hashtable<String, String>();
	static {
		mime.put("pdf", "application/pdf");
		mime.put("xls", "application/vnd.ms-excel");
		mime.put("doc", "application/msword");
		mime.put("gif", "image/gif");
		mime.put("jpg", "image/jpeg");
		mime.put("jpeg", "image/jpeg");
		mime.put("htm", "text/html");
		mime.put("html", "text/html");
		mime.put("txt", "text/plain");
	}
	
	public static final int IDX_VISUALIZZATORE = 0;
	public static final int IDX_3D = 1;
	public static final int IDX_3D_PROSP = 2;
	public static final int IDX_IMPIANTO_1956 = 3;
	public static final int IDX_COPIONI_VISURA = 4;
	public static final int IDX_CESSATO_CATASTO = 5;
	public static final int IDX_DEMANIO = 6;
	
	public MappeLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	public ArrayList<Link> getLinksImpianto56(String foglio) throws Exception {
		ArrayList<Link> links = new ArrayList<Link>();
		String pathCartografiaStorica = getProperty("path.pathCartografiaStorica");
		String folderImpianto1956 = getProperty("path.folderImpianto1956");
		String pathCompleto = pathCartografiaStorica + File.separatorChar + folderImpianto1956;
		String[] files = new File(pathCompleto).list();
		if (files != null) {
			for (String file : files) {
				String fileLc = file.toLowerCase();
				if (fileLc.indexOf("imp_") > -1 && fileLc.indexOf(".jpg") > -1) {
					String specFile = fileLc.substring(fileLc.indexOf("imp_") + "imp_".length(), fileLc.indexOf(".jpg"));
					int foglioFile = Integer.parseInt(specFile.indexOf("_") > -1 ? specFile.substring(0, specFile.indexOf("_")) : specFile);
					if (foglioFile == Integer.parseInt(foglio)) {
						Link link = new Link();
						link.setGruppo(""); //non è significativo
						link.setDescrizione(specFile.toUpperCase());
						link.setPath(getPathToJS(pathCompleto + "\\" + file));
						links.add(link);
					}
				}			
			}
		}		
		return links;
	}
	
	public ArrayList<Link> getLinksCopioniVisura(String foglio) throws Exception {
		ArrayList<Link> links = new ArrayList<Link>();
		String pathCartografiaStorica = getProperty("path.pathCartografiaStorica");
		String folderCopioniVisura = getProperty("path.folderCopioniVisura");
		String pathCompleto = "";		
		//ORIGINALI
		String folderOriginali = getProperty("path.folderCopioniVisuraOriginali");
		pathCompleto = pathCartografiaStorica + File.separatorChar + folderCopioniVisura + File.separatorChar + folderOriginali;
		String[] files = new File(pathCompleto).list();
		if (files != null) {
			for (String file : files) {
				String fileLc = file.toLowerCase();
				//escludo i quadri
				if (fileLc.indexOf("_quadro") == -1) {
					String specFile = "";
					for (int i = 0; i < fileLc.length(); i++) {
						if ("0123456789".indexOf(fileLc.charAt(i)) > -1) {
							specFile += fileLc.charAt(i);
						} else break;
					}
					if (!specFile.equals("") && Integer.parseInt(specFile) == Integer.parseInt(foglio)) {
						Link link = new Link();
						link.setGruppo("Originali");
						link.setDescrizione(fileLc.substring(0, fileLc.indexOf(".")).toUpperCase());
						link.setPath(getPathToJS(pathCompleto + File.separatorChar + file));
						links.add(link);
					}					
				}							
			}
		}
		//SOSTITUITI
		String folderSostituiti = getProperty("path.folderCopioniVisuraSostituiti");
		pathCompleto = pathCartografiaStorica + "\\" + folderCopioniVisura + "\\" + folderSostituiti;
		files = new File(pathCompleto).list();
		if (files != null) {
			for (String file : files) {
				String fileLc = file.toLowerCase().substring("sost_".length());
				String specFile = "";
				for (int i = 0; i < fileLc.length(); i++) {
					if ("0123456789".indexOf(fileLc.charAt(i)) > -1) {
						specFile += fileLc.charAt(i);
					} else break;
				}
				if (!specFile.equals("") && Integer.parseInt(specFile) == Integer.parseInt(foglio)) {
					Link link = new Link();
					link.setGruppo("Sostituiti");
					link.setDescrizione(fileLc.substring(0, fileLc.indexOf(".")).toUpperCase());
					link.setPath(getPathToJS(pathCompleto + "\\" + file));
					links.add(link);
				}
			}
		}
		//QUADRI
		pathCompleto = pathCartografiaStorica + "\\" + folderCopioniVisura + "\\" + folderOriginali;
		files = new File(pathCompleto).list();
		if (files != null) {
			for (String file : files) {
				String fileLc = file.toLowerCase();
				if (fileLc.indexOf("_quadro") > -1) {
					Link link = new Link();
					link.setGruppo("Quadri d\'unione");
					link.setDescrizione(fileLc.substring(0, fileLc.indexOf("_")));
					link.setPath(getPathToJS(pathCompleto + "\\" + file));
					if (!link.getDescrizione().equals("0001")) {
						//il quadro 0001 non va aggiunto
						links.add(link);
					}										
				}							
			}
		}
		return links;
	}
	
	public ArrayList<Link> getLinksCessatoCatasto(String foglio) throws Exception {
		ArrayList<Link> links = new ArrayList<Link>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL_TABLE_CESSATO_CATASTO);
			rs = pstmt.executeQuery();
			boolean trovata = rs.next();
			if (!trovata) {
				pstmt.cancel();
				pstmt = conn.prepareStatement(SQL_SYN_TABLE_CESSATO_CATASTO);
				trovata = pstmt.executeQuery().next();
			}
			if (trovata) {
				pstmt.cancel();
				pstmt = conn.prepareStatement(SQL_SELECT_CESSATO_CATASTO);
				pstmt.setString(1, foglio);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					String fgCessato = rs.getString("FG_CESSATO").trim();
					String sezCessato = rs.getString("SEZ_CESSATO").trim();
					links = getLinksFoglioSezioneCessati(links, fgCessato, sezCessato);
				}
			}		
			//ordinamento per sezioni - gruppi
			ArrayList<String> sezioni = new ArrayList<String>();
			for (Link link : links) {
				if (!link.getGruppo().equals("") && !link.getGruppo().equals("§")) {
					String sezione = link.getGruppo().substring(0, link.getGruppo().indexOf(" - "));
					boolean trovataSez = false;
					for (String mySezione : sezioni) {
						if (sezione.equals(mySezione)) {
							trovataSez = true;
							break;
						}
					}
					if (!trovataSez) {
						sezioni.add(sezione);
					}	
				}				
			}
			ArrayList<Link> newLinks = new ArrayList<Link>();
			for (String mySezione : sezioni) {
				for (Link link : links) {
					if (link.getGruppo().equals(mySezione + " - " + "Mappe")) {
						link.setGruppo(link.getGruppo().replace(" - ", "<br>"));
						newLinks.add(link);
					}
				}
				for (Link link : links) {
					if (link.getGruppo().equals(mySezione + " - " + "Abbozzi")) {
						boolean trovato = false;
						for (Link newLink : newLinks) {
							if (newLink.getGruppo().indexOf(mySezione) > -1 &&
								newLink.getGruppo().indexOf("Mappe") > -1) {
									trovato = true;
									break;
								}
						}
						if (trovato) {
							link.setGruppo(link.getGruppo().substring(link.getGruppo().indexOf(" - ") + " - ".length()));
						} else {
							link.setGruppo(link.getGruppo().replace(" - ", "<br>"));
						}
						newLinks.add(link);
					}
				}
				for (Link link : links) {
					if (link.getDescrizione().equals(mySezione + " - " + "Quadro della sezione")) {
						boolean trovato = false;
						for (Link newLink : newLinks) {
							if ((newLink.getGruppo().indexOf(mySezione) > -1 &&
								newLink.getGruppo().indexOf("Mappe") > -1) ||
								(newLink.getGruppo().indexOf(mySezione) > -1 &&
								newLink.getGruppo().indexOf("Abbozzi") > -1)) {
									trovato = true;
									break;
							}
						}
						if (trovato) {
							link.setDescrizione(link.getDescrizione().substring(link.getDescrizione().indexOf(" - ") + " - ".length()));
						} else {
							link.setDescrizione(link.getDescrizione().replace(" - ", "<br>"));
						}
						newLinks.add(link);
					}
				}
			}		
			links = newLinks;
			//QUADRO DI COLLEGAMENTO GENERALE
			String pathCartografiaStorica = getProperty("path.pathCartografiaStorica");
			String folderCessatoCatasto = getProperty("path.folderCessatoCatasto");
			String folderQuadro = getProperty("path.folderCessatoCatastoQuadro");
			String pathCompleto = pathCartografiaStorica + File.separatorChar + folderCessatoCatasto + File.separatorChar + folderQuadro;
			String[] files = new File(pathCompleto).list();
			if (files != null) {
				for (String file : files) {
					String fileLc = file.toLowerCase();
					if (fileLc.indexOf("quadro") > -1) {
						Link link = new Link();
						link.setGruppo("§"); //deve essere al primo livello; § serve per il padding-top
						link.setDescrizione("Quadro di collegamento (generale)");
						link.setPath(getPathToJS(pathCompleto + File.separatorChar + file));
						links.add(link);	
					}							
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		return links;
	}
	
	private ArrayList<Link> getLinksFoglioSezioneCessati(ArrayList<Link> links, String fgCessato, String sezCessato) {
		if (links == null)
			links = new ArrayList<Link>();
		//MAPPE
		String pathCartografiaStorica = getProperty("path.pathCartografiaStorica");
		String folderCessatoCatasto = getProperty("path.folderCessatoCatasto");
		String folderMappe = getProperty("path.folderCessatoCatastoMappe");
		String pathCompleto  = pathCartografiaStorica + File.separatorChar + folderCessatoCatasto + File.separatorChar + sezCessato + File.separatorChar + folderMappe;
		String[] files = new File(pathCompleto).list();
		if (files != null) {
			for (String file : files) {
				String fileLc = file.toLowerCase();
				String specFile = "";
				for (int i = 0; i < fileLc.length(); i++) {
					if ("0123456789".indexOf(fileLc.charAt(i)) > -1) {
						specFile += fileLc.charAt(i);
					} else break;
				}
				if (!specFile.equals("") && Integer.parseInt(specFile) == Integer.parseInt(fgCessato)) {
					Link link = new Link();
					link.setGruppo(sezCessato + " - " + "Mappe");
					link.setDescrizione(fileLc.substring(0, fileLc.indexOf(".")).toUpperCase().replace("9999", "R")); //replace per caso speciale
					link.setPath(getPathToJS(pathCompleto + "\\" + file));
					links.add(link);
				}						
			}
		}
		//ABBOZZI
		/*String folderAbbozzi = getProperty("path.folderCessatoCatastoAbbozzi");
		pathCompleto  = pathCartografiaStorica + "\\" + folderCessatoCatasto + "\\" + sezCessato + "\\" + folderAbbozzi;
		files = new File(pathCompleto).list();
		if (files != null) {
			for (String file : files) {
				String fileLc = file.toLowerCase();
				String specFile = "";
				for (int i = 0; i < fileLc.length(); i++) {
					if ("0123456789".indexOf(fileLc.charAt(i)) > -1) {
						specFile += fileLc.charAt(i);
					} else break;
				}
				if (!specFile.equals("") && Integer.parseInt(specFile) == Integer.parseInt(fgCessato)) {
					Link link = new Link();
					link.setGruppo(sezCessato + " - " + "Abbozzi");
					link.setDescrizione(fileLc.substring(0, fileLc.indexOf(".")).toUpperCase().replace("9999", "R")); //replace per caso speciale
					link.setPath(getPathToJS(pathCompleto + "\\" + file));
					links.add(link);
				}						
			}
		}*/
		//devono essere inseriti tutti gli abbozzi della sezione
		String folderAbbozzi = getProperty("path.folderCessatoCatastoAbbozzi");
		pathCompleto  = pathCartografiaStorica + "\\" + folderCessatoCatasto + "\\" + sezCessato + "\\" + folderAbbozzi;
		files = new File(pathCompleto).list();
		if (files != null) {
			for (String file : files) {
				boolean trovato = false;
				for (Link mioLink : links) {
					if (mioLink.getPath().equals(getPathToJS(pathCompleto + "\\" + file))) {
						trovato = true;
						break;
					}
				}
				if (!trovato) {
					String fileLc = file.toLowerCase();
					if (fileLc.endsWith(".jpg")) {
						Link link = new Link();
						link.setGruppo(sezCessato + " - " + "Abbozzi");
						link.setDescrizione(fileLc.substring(0, fileLc.indexOf(".")).toUpperCase().replace("9999", "R")); //replace per caso speciale
						link.setPath(getPathToJS(pathCompleto + "\\" + file));
						links.add(link);
					}					
				}										
			}
		}
		//QUADRO SEZIONE (deve essere aggiunto una sola volta per sezione)
		pathCompleto  = pathCartografiaStorica + "\\" + folderCessatoCatasto + "\\" + sezCessato + "\\" + folderMappe;
		files = new File(pathCompleto).list();
		if (files != null) {
			for (String file : files) {
				String fileLc = file.toLowerCase();
				if (fileLc.indexOf("quadro") > -1) {
					Link link = new Link();
					link.setGruppo("§"); //deve essere al primo livello; § serve per il padding-top
					link.setDescrizione(sezCessato + " - " + "Quadro della sezione");
					link.setPath(getPathToJS(pathCompleto + "\\" + file));
					boolean trovato = false;
					for (Link myLink : links) {
						if (myLink.getDescrizione().equals(link.getDescrizione())) {
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						links.add(link);
					}						
				}						
			}
		}
		
		return links;
	}
	
	public ArrayList<Link> getLinksDemanio(String foglio) throws Exception {
		ArrayList<Link> links = new ArrayList<Link>();
		String pathCartografiaStorica = getProperty("path.pathCartografiaStorica");
		String folderDemanio = getProperty("path.folderDemanio");
		String pathCompleto = pathCartografiaStorica + File.separatorChar + folderDemanio;
		String[] files = new File(pathCompleto).list();
		if (files != null) {
			for (String file : files) {
				String fileLc = file.toLowerCase();
				if (fileLc.indexOf("fg ") > -1 && fileLc.indexOf(".pdf") > -1) {
					String specFile = fileLc.substring(fileLc.indexOf("fg ") + "fg ".length(), fileLc.indexOf(".pdf"));
					int foglioFile = Integer.parseInt(specFile);
					if (foglioFile == Integer.parseInt(foglio)) {
						Link link = new Link();
						link.setGruppo(""); //non è significativo
						link.setDescrizione(specFile.toUpperCase());
						link.setPath(getPathToJS(pathCompleto + "\\" + file));
						links.add(link);
					}
				}			
			}
		}		
		return links;
	}
	
	private String getPathToJS(String path) {
		String newPath = "";
		for (int i = 0; i < path.length(); i++) {
			if (path.charAt(i) == '\\') {
				newPath += "\\\\";
			} else {
				newPath += path.charAt(i);
			}
		}
		return newPath;
	}
	
	public static boolean isMappaVisibile(int index, HttpServletRequest request) {
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		switch (index) {
			case IDX_VISUALIZZATORE:
				return false;
			case IDX_3D:
				return false;
			case IDX_3D_PROSP:
				return true; //TODO
			case IDX_IMPIANTO_1956:
				//TODO
				//al momento visualizzato solo per comune Milano
				try {
					return getPerComune("F205", eu);
				} catch (Exception e) {
					return false;
				}
			case IDX_COPIONI_VISURA:
				//TODO
				//al momento visualizzato solo per comune Milano
				try {
					return getPerComune("F205", eu);
				} catch (Exception e) {
					return false;
				}
			case IDX_CESSATO_CATASTO:
				//TODO
				//al momento visualizzato solo per comune Milano
				try {
					return getPerComune("F205", eu);
				} catch (Exception e) {
					return false;
				}
			case IDX_DEMANIO:
				//TODO
				//al momento visualizzato solo per comune Milano
				try {
					return getPerComune("F205", eu);
				} catch (Exception e) {
					return false;
				}
		}
		return false;
	}
	
	private static boolean getPerComune(String codice, EnvUtente eu) throws Exception {
		//codice = "F704"; //solo per test... poi togliere!
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean retVal = false;
		try {
			Context cont = new InitialContext();
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource theDataSource = (DataSource)cont.lookup(es.getDataSource());
			
			conn = theDataSource.getConnection();
			pstmt = conn.prepareStatement("select 1 from sit_ente where codent = ?");
			pstmt.setString(1, codice);
			rs = pstmt.executeQuery();
			retVal = rs.next();			
		} catch (Exception e) {
			retVal = false;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		return retVal;
	}

}

