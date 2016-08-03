/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.eiv.database;
import it.escsolution.escwebgis.common.EnvBase;
import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.webred.DecodificaPermessi;
import it.webred.cet.permission.CeTUser;
import it.webred.cet.permission.GestionePermessi;
import it.webred.ct.config.model.AmFonteComune;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.data.access.basic.fonti.FontiService;

import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
/**
 * @author silviat
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Temi extends EnvBase{
	
	private final static HashMap<String, String> CON_LIMITAZIONI = new HashMap<String, String>();
	static {
		CON_LIMITAZIONI.put("DEMOGRAFIA", " (con limitazioni)");
	}
	
	public Vector TemiVec;
	
	String [] a = {""};
	
	//Inserire la corrispondenza TEMA - COD.FONTE per ogni nuova fonte da visualizzare
	public static HashMap<String,String[]> mappaTemiFonti= new HashMap<String,String[]>();
	
	static{
		//il valore di SX è uppercase di EWG_TEMA.NOME
		//va inserita la (ad es.) Fonte:BOLLI VEICOLI da Profiler
		//il valore DX è AM_FONTE.CODICE 
		mappaTemiFonti.put("ACQUEDOTTO E IMP.TERMICI", null);
		mappaTemiFonti.put("CAMERA DI COMMERCIO", null);
		
		mappaTemiFonti.put("CATASTO", new String[]{"CATASTO"});
		mappaTemiFonti.put("COMPRAVENDITE", new String[]{"COMPRAVENDITE"});
		mappaTemiFonti.put("CONCESSIONI EDILIZIE", new String[]{"CONCEDI","CONCEDI VISURE"});
		mappaTemiFonti.put("COSAP/TOSAP", new String[]{"COSAP/TOSAP"});
		mappaTemiFonti.put("DOCFA", new String[]{"DOCFA"});
		mappaTemiFonti.put("ECOGRAFICO CATASTALE",new String[]{"ECOGRAFICO CATASTALE"});
		mappaTemiFonti.put("FORNITURE ELETTRICHE",new String[]{"FORNITURE ELETTRICHE"});
		mappaTemiFonti.put("FORNITURE GAS",new String[]{"GAS"});
		mappaTemiFonti.put("LICENZE COMMERCIO", new String[]{"LICCOMMERCIALI"});
		mappaTemiFonti.put("LOCAZIONI", new String[]{"LOCAZIONI"});
		mappaTemiFonti.put("POPOLAZIONE", new String[]{"DEMOGRAFIA"});
		mappaTemiFonti.put("PRATICHE PORTALE", new String[]{"PRATICHE PORTALE"});
		mappaTemiFonti.put("PREGEO", new String[]{"PREGEO"});
		mappaTemiFonti.put("PUBBLICITA", new String[]{"PUBBLICITA"});
		mappaTemiFonti.put("REDDITI", new String[]{"REDDITI"});
		mappaTemiFonti.put("REDDITI ANALITICI", new String[]{"REDDITI ANALITICI"});
		mappaTemiFonti.put("RETTE", new String[]{"RETTE"});
		mappaTemiFonti.put("SUCCESSIONI", new String[]{"SUCCESSIONI"});
		mappaTemiFonti.put("TOPONOMASTICA", new String[]{"CATASTO"});
		mappaTemiFonti.put("TRAFFICO", new String[]{"MULTE"});;
		mappaTemiFonti.put("TRIBUTI", new String[]{"TRIBUTI","IMU"});
		mappaTemiFonti.put("RUOLI E VERSAMENTI", new String[]{"TRIBUTI","F24","RUOLO TARSU","RUOLO TARES","VERS ICI MISTERIALE","VERS TAR POSTE"});
		mappaTemiFonti.put("UTENZE", new String[]{"ACQUA NEW","ACQUA","FORNITURE ELETTRICHE","GAS"});
		mappaTemiFonti.put("URBANISTICA", new String[]{"URBANISTICA"}); //Olbia
		mappaTemiFonti.put("DEMANIO COMUNALE", new String[]{"DEMANIO"});
		mappaTemiFonti.put("CERTIFICAZIONI ENERGETICHE", new String[]{"CENED"});
		mappaTemiFonti.put("BOLLI VEICOLI", new String[]{"BOLLIVEICOLI"});
			
	}
	
	//Inserisco solo le classi che, all'interno di una stesso Tema hanno fonti di riferimento diverse
	public static HashMap<String,String[]> mappaClasseFonte= new HashMap<String,String[]>();
		
		static{
			
			mappaClasseFonte.put("CONCESSIONI EDILIZIE_Storico Concessioni", new String[]{"CONCEDI"});
			mappaClasseFonte.put("CONCESSIONI EDILIZIE_Archivio storico Visure", new String[]{"CONCEDI VISURE"});

			mappaClasseFonte.put("UTENZE_Forniture Idriche", new String[]{"ACQUA NEW"});
			mappaClasseFonte.put("UTENZE_Forniture Elettriche", new String[]{"FORNITURE ELETTRICHE"});
			mappaClasseFonte.put("UTENZE_Forniture Gas", new String[]{"GAS"});

			mappaClasseFonte.put("RUOLI E VERSAMENTI_Ruolo Tarsu", new String[]{"RUOLO TARSU"});
			mappaClasseFonte.put("RUOLI E VERSAMENTI_Ruolo Tares", new String[]{"RUOLO TARES"});
			mappaClasseFonte.put("RUOLI E VERSAMENTI_Versamenti da Gestionale", new String[]{"TRIBUTI"});
			mappaClasseFonte.put("RUOLI E VERSAMENTI_Versamenti ICI", new String[]{"VERS ICI MISTERIALE"});
			mappaClasseFonte.put("RUOLI E VERSAMENTI_F24 Annullamenti", new String[]{"F24"});
			mappaClasseFonte.put("RUOLI E VERSAMENTI_F24 Versamenti", new String[]{"F24"});
			mappaClasseFonte.put("RUOLI E VERSAMENTI_Prospetto Incassi F24", new String[]{"F24"});
			mappaClasseFonte.put("RUOLI E VERSAMENTI_Versamenti", new String[]{"F24","VERS ICI MISTERIALE","TRIBUTI","VERS TAR POSTE"});

			mappaClasseFonte.put("TRIBUTI_Consulenze IMU", new String[]{"IMU"});
			mappaClasseFonte.put("TRIBUTI_Contribuenti", new String[]{"TRIBUTI"});
			mappaClasseFonte.put("TRIBUTI_Oggetti ICI", new String[]{"TRIBUTI"});
			mappaClasseFonte.put("TRIBUTI_Oggetti Tarsu", new String[]{"TRIBUTI"});
			mappaClasseFonte.put("CERTIFICAZIONI ENERGETICHE", new String[]{"CENED"});
		
		}
	
	public Vector QueryTemiFonti(int progetto, CeTUser utente, String nomeApplicazione){
		TemiVec = new Vector();
		Connection conn = null;
		try{
			Context ctx = new InitialContext();
			if(ctx == null) 
				throw new Exception("Boom - No Context");
			
			EnvUtente eu = new EnvUtente(utente, null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			
			HashMap<String,String> listaPermessiEnte= utente.getPermList();
			
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				conn = ds.getConnection();
				if(conn != null)  {
					PreparedStatement pstmt = conn.prepareStatement("SELECT distinct descrizione, urlhome, pk_tema, nome,ordinamento FROM ewg_tema t, ewg_tema_ente e, ewg_tab_comuni_utenti c WHERE t.fk_progetto = ? AND t.pk_tema = e.fk_tema AND e.codente = c.UK_BELFIORE and c.UTENTE = ? ORDER BY ordinamento");
				
					pstmt.setInt(1, progetto);
					pstmt.setString(2, utente.getName());
					ResultSet rst = pstmt.executeQuery();
					
					while(rst.next()) {
						Tema tema = new Tema();
						tema.Descrizione=(rst.getString("DESCRIZIONE"));
						tema.UrlHome=(rst.getString("URLHOME"));
						tema.pk_tema=(rst.getString("PK_TEMA"));
						tema.Nome=(rst.getString("NOME"));
						
					//23-10-2012: Rimosso controllo dei permessi su Tema (basta la fonte)
			//			if (GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, "Tema:"+tema.Nome,true)){
							
							String[] nomeFonte= mappaTemiFonti.get(tema.Nome.toUpperCase());
							//se il tema ha associata una fonte, allora lo visualizzo solo se ho il permesso su quella fonte (e se la fonte e  abilitata per l'ente); altrimenti lo visualizzo se ho il permesso sul tema
							if (nomeFonte!= null){
								int f = 0;
								boolean inserito = false;
								while(f<nomeFonte.length && !inserito){
									String nomei = nomeFonte[f];
									boolean fontePermessa = GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ nomei,true);
									if (!fontePermessa) {
										String conLim = CON_LIMITAZIONI.get(nomei.toUpperCase());
										if (conLim != null) {
											fontePermessa = GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ nomei + " " + conLim.trim(),true);
										}
									}
									if(fontePermessa && verificaFonteComune(eu.getEnte(), nomei)){
										TemiVec.add(tema);
										inserito = true;
									}
									
									f++;
								}
							}else if(GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, "Tema:"+tema.Nome,true)){
								TemiVec.add(tema);
							}
					//	}
					}
				}
			}
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			}
		finally
		{
			try
			{
				if(conn != null)
				conn.close();
			}
			catch (SQLException e)
			{
				log.error(e.getMessage(),e);
			}
			
		}
		return TemiVec;		
	}
	
	public Vector QueryTemiUnico(int progetto, CeTUser utente, String nomeApplicazione, String item, String permesso){
		TemiVec = new Vector();
		Connection conn = null;
		try{
			Context ctx = new InitialContext();
			if(ctx == null) 
				throw new Exception("Boom - No Context");
			EnvUtente eu = new EnvUtente(utente, null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			
			String enteCorrente= utente.getCurrentEnte();
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				conn = ds.getConnection();
				if(conn != null)  {
					PreparedStatement pstmt = conn.prepareStatement("SELECT distinct descrizione, urlhome, pk_tema, nome,ordinamento FROM ewg_tema t, ewg_tema_ente e, ewg_tab_comuni_utenti c WHERE t.fk_progetto = ? AND t.pk_tema = e.fk_tema AND e.codente = c.UK_BELFIORE and c.UTENTE = ? ORDER BY ordinamento");
					pstmt.setInt(1, progetto);
					pstmt.setString(2, utente.getName());
					ResultSet rst = pstmt.executeQuery();
					
					while(rst.next()) {
						Tema tema = new Tema();
						tema.Descrizione=(rst.getString("DESCRIZIONE"));
						tema.UrlHome=(rst.getString("URLHOME"));
						tema.pk_tema=(rst.getString("PK_TEMA"));
						tema.Nome=(rst.getString("NOME"));
						
						if (GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), item, permesso,true))
							if (progetto != 5)
								TemiVec.add(tema);
							else
								verificaDiagnostiche(conn, TemiVec, tema, null, null);
					}
						
	
				}
			}
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			}
		finally
		{
			try
			{
				if(conn != null)
				conn.close();
			}
			catch (SQLException e)
			{
				log.error(e.getMessage(),e);
			}
			
		}
		return TemiVec;		
	}


	public void QueryClassiCatalogo(int i,HttpServletRequest request)
	{
		Connection conn = null;
		try
		{
			Context ctx = new InitialContext();
			if (ctx == null)
				throw new Exception("Boom - No Context");
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null)
			{
				conn = ds.getConnection();
				if (conn != null)
				{
					String sql = "select distinct upper(rtrim(ltrim(nome))) as nome from catalogosql";
					//aggiunta per escludere le diagnostiche
					sql += " where upper(rtrim(ltrim(nome))) not in ('D_CTR_DEM', 'D_CTR_TRI', 'D_CTR_CAT', 'D_CTR_ALT', 'D_CTR_TRI2',";
					sql += " 'D_CFR_DEM', 'D_CFR_TRI', 'D_CFR_ICI', 'D_CFR_TAR', 'D_CFR_CAT', 'D_CFR_ALT', 'D_CFR_TOP', 'D_CFR_ICI2', 'D_CFR_TAR2',";					
					sql += " 'S_STA_DEM', 'S_STA_TRI', 'S_STA_CAT', 'S_STA_ALT')";
					
					PreparedStatement pstmt = conn.prepareStatement(sql);
					Tema tema = new Tema();
					tema = (Tema) TemiVec.get(i);
					tema.ClassiVec = new Vector();
					
					ResultSet rst = pstmt.executeQuery();
					while (rst.next())
					{
						Classe classe = new Classe();
						classe.Label = (rst.getString("nome"));
						classe.UrlRicerca = request.getContextPath()+"/sqlcatalog/sqlFrame.jsp?nome="+URLEncoder.encode(rst.getString("nome").trim(),"US-ASCII");
						classe.Tipo = "1";						
						tema.ClassiVec.add(classe);
					}

				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
		finally
		{
			try
			{
				if(conn != null)
				conn.close();
			}
			catch (SQLException e)
			{
				log.error(e.getMessage(),e);
			}
			
		}
		return;
	}

	public void QueryClassiFonti(int i, HttpServletRequest request){
		Connection conn = null;
		try{
			Context ctx = new InitialContext();
			if(ctx == null) 
				throw new Exception("Boom - No Context");
			CeTUser utente = (CeTUser) request.getSession().getAttribute("user");
			EnvUtente eu = new EnvUtente(utente, null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				conn = ds.getConnection();
				if(conn != null)  {
								/*Statement pstmt = conn.createStatement();
								ResultSet rst = pstmt.executeQuery("SELECT PK_TEMA FROM EWG_TEMA WHERE FK_PROGETTO=?");*/
					PreparedStatement pstmt = conn.prepareStatement("SELECT LABEL,URLRICERCA,TYPE,PK_CLASSE FROM EWG_CLASSE WHERE FK_TEMA=? ORDER BY LABEL ");
					Tema tema = new Tema();
					tema=(Tema) TemiVec.get(i);
					tema.ClassiVec = new Vector();
					pstmt.setString(1,tema.pk_tema);
					ResultSet rst = pstmt.executeQuery();
					while(rst.next()) {
						Classe classe = new Classe();
						classe.Label=(rst.getString("LABEL"));
						classe.UrlRicerca=(rst.getString("URLRICERCA"));
						classe.Tipo=(rst.getString("TYPE"));
						classe.pk_classe=(rst.getString("PK_CLASSE"));					
						
						int idProgetto = 0;
						ResultSet rstProg = conn.prepareStatement("SELECT FK_PROGETTO FROM EWG_TEMA WHERE NOME = 'Diagnostiche di controllo'").executeQuery();
						while (rstProg.next()) {
							idProgetto = rstProg.getInt("FK_PROGETTO");
						}
						if (idProgetto == 5 && 
								(tema.Nome != null && (tema.Nome.equalsIgnoreCase("Diagnostiche di controllo") ||
														tema.Nome.equalsIgnoreCase("Diagnostiche di confronto") ||
														tema.Nome.equalsIgnoreCase("Statistiche") ||
														tema.Nome.equalsIgnoreCase("Liste"))))  {
							String codDia = classe.UrlRicerca.substring(classe.UrlRicerca.indexOf("nome=") + "nome=".length(), classe.UrlRicerca.indexOf("tipo=")-1);
							verificaDiagnostiche(conn, tema.ClassiVec, null, classe, codDia);
							
						} else {			
							
							String clMap = tema.Nome.toUpperCase()+"_"+classe.Label;
							String[] codFonte = mappaClasseFonte.get(clMap);
							log.debug("QueryClassiFonti Classe: " +clMap+" "+ codFonte);
							boolean hidden = isHidden(utente, eu, tema, classe);
							if(codFonte!=null){
								boolean fontePermessa = false;
								boolean fonteComune = false;
								for(String cf : codFonte){
									fontePermessa = fontePermessa || GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ cf,true);
									fonteComune = fonteComune || verificaFonteComune(eu.getEnte(), cf);
								}
								if(fontePermessa && fonteComune && !hidden)
										tema.ClassiVec.add(classe);
							
							}else if (!hidden)
								tema.ClassiVec.add(classe);
						}
					}
				}
			}
		}catch(Exception e) {
			log.error(e.getMessage(),e);
		}
		finally
		{
			try
			{
				if(conn != null)
				conn.close();
			}
			catch (SQLException e)
			{
				log.error(e.getMessage(),e);
			}		
		}
		return;		
	}
	
	private boolean isHidden(CeTUser utente, EnvUtente eu, Tema tema, Classe classe) {
		if (tema.Nome.equalsIgnoreCase("Fascicoli") && classe.UrlRicerca.toLowerCase().indexOf("indaginecivico") > -1) {
			if (GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_RES, true))
				return false;
			if (GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_UI_TIT, true))
				return false;
			if (GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_UI_CONS, true))
				return false;
			return true;
		}
		if (tema.Nome.equalsIgnoreCase("Fascicoli") && classe.UrlRicerca.toLowerCase().indexOf("fascfabbapp") > -1) {
			if (GestionePermessi.autorizzato(utente, DecodificaPermessi.NOME_IST_FASCICOLO_FABBRICATO_APP, DecodificaPermessi.ITEM_FASCICOLO_FABBRICATO_APP, DecodificaPermessi.PERMESSO_FAS_FAB_APP_GES, true))
				return false;
			if (GestionePermessi.autorizzato(utente, DecodificaPermessi.NOME_IST_FASCICOLO_FABBRICATO_APP, DecodificaPermessi.ITEM_FASCICOLO_FABBRICATO_APP, DecodificaPermessi.PERMESSO_FAS_FAB_APP_IMM, true))
				return false;
			if (GestionePermessi.autorizzato(utente, DecodificaPermessi.NOME_IST_FASCICOLO_FABBRICATO_APP, DecodificaPermessi.ITEM_FASCICOLO_FABBRICATO_APP, DecodificaPermessi.PERMESSO_FAS_FAB_APP_SUP, true))
				return false;
			return true;
		}
		return false;
	}

	public void QueryClassiDiagnostiche(int i,HttpServletRequest request)
	{
		Connection conn = null;
		try
		{
			Context ctx = new InitialContext();
			if (ctx == null)
				throw new Exception("Boom - No Context");
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSource());
			if (ds != null)
			{
				conn = ds.getConnection();
				if (conn != null)
				{
					String sql = "select  upper(DESCR_FONTE) nome,COD_FONTE COD from c_fonte F "
									+" WHERE EXISTS (SELECT 1 FROM sit_f_dia_template T WHERE T.FK_ID_DIA_FONTE = F.COD_FONTE)";
					
					PreparedStatement pstmt = conn.prepareStatement(sql);
					Tema tema = new Tema();
					tema = (Tema) TemiVec.get(i);
					tema.ClassiVec = new Vector();
					
					ResultSet rst = pstmt.executeQuery();
					while (rst.next())
					{
						Classe classe = new Classe();
						classe.Label = (rst.getString("nome"));
						classe.UrlRicerca = request.getContextPath()+"/Diagnostiche?UC=101&ST=1&COD_FONTE="+URLEncoder.encode(rst.getString("COD").trim(),"US-ASCII");
						classe.Tipo = "1";					
					}			
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
		finally
		{
			try
			{
				if(conn != null)
				conn.close();
			}
			catch (SQLException e)
			{
				log.error(e.getMessage(),e);
			}
			
		}
		return;
	}

	private void verificaDiagnostiche(Connection conn, Vector vec, Tema tema, Classe classe, String codDia) throws Exception {
		log.debug("Tema="+(tema!=null?tema.Nome:null));
		log.debug("Classe="+(classe!=null?classe.Label:null));
		log.debug("codDia="+(codDia!=null?codDia:null));
		
		String[] codici = null;
		boolean add = true;	
		if (tema != null) {
			String nome = tema.Nome.trim();			
			if (nome.equalsIgnoreCase("Diagnostiche di controllo")) {
				codici = new String[] {"D_CTR_DEM", "D_CTR_TRI", "D_CTR_CAT", "D_CTR_ALT", "D_CTR_TRI2"};
			} else if (nome.equalsIgnoreCase("Diagnostiche di confronto")) {
				codici = new String[] {"D_CFR_DEM", "D_CFR_TRI", "D_CFR_ICI", "D_CFR_TAR", "D_CFR_CAT", "D_CFR_ALT", "D_CFR_TOP", "D_CFR_ICI2", "D_CFR_TAR2"};
			} else if (nome.equalsIgnoreCase("Statistiche")) {
				codici = new String[] {"S_STA_DEM", "S_STA_TRI", "S_STA_CAT", "S_STA_ALT"};
			} else if (nome.equalsIgnoreCase("Liste")) {
				codici = new String[] {"L_LIS_DEM", "L_LIS_TRI", "L_LIS_CAT", "L_LIS_ALT"};
			}			
		}
		if (codDia != null) {
			codici = new String[] {codDia};
		}
		if (codici != null) {
			add = false;
			for (String codice : codici) {
				if (add) break;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					String sql = "SELECT IDCATALOGODIA, SQLCOMMANDPROP FROM DIA_CATALOGO WHERE ABILITATA = 1 AND UPPER(RTRIM(LTRIM(NOME))) = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, codice);
					log.debug(sql);
					log.debug(codice);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						int idCatalogoDia= rs.getInt("IDCATALOGODIA");
						String cmdSql = rs.getString("SQLCOMMANDPROP");
						
						add = verificaPropConfig(conn, idCatalogoDia, cmdSql);
						log.debug("IDCATALOGODIA=" +  idCatalogoDia + ", SQLCOMMANDPROP=" + cmdSql + " add="+add);
						if (add) break;
					}
				} catch (Exception e) {
					log.error(e.getMessage(),e);
					throw e;
				} finally {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
				}				
			}
		}
		if (add) {
			if (classe != null) {
				vec.add(classe);
			} else {
				vec.add(tema);
			}			
		}			
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

	
	private boolean verificaFonteComune(String comune, String codFonte) throws NamingException{
		ComuneService comuneService= (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");
		AmFonteComune am = comuneService.getFonteComuneByComuneCodiceFonte(comune, codFonte);
		return (am!=null)? true : false;
	}
	
	

}
