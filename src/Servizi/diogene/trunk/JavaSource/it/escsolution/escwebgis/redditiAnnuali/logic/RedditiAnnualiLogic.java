package it.escsolution.escwebgis.redditiAnnuali.logic;

import it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico;
import it.escsolution.escwebgis.anagrafe.bean.FamiliariStorico;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoIntestatariFLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.locazioni.logic.LocazioniLogic;
import it.escsolution.escwebgis.redditiAnnuali.bean.DecoRedditiDichiarati;
import it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnuali;
import it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnualiFinder;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.escsolution.escwebgis.tributiNew.logic.TributiOggettiICINewLogic;
import it.escsolution.escwebgis.tributiNew.logic.VersamentiNewLogic;
import it.webred.ct.data.access.basic.redditi.dto.KeySoggDTO;
import it.webred.ct.data.access.basic.redditianalitici.RedditiAnService;
import it.webred.ct.data.access.basic.redditianalitici.dto.RigaQuadroRbDTO;
import it.webred.utils.DateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

public class RedditiAnnualiLogic extends EscLogic{
	private String appoggioDataSource;
	public RedditiAnnualiLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}

	public static final String FINDER = "FINDER";
	public static final String REDDITI = RedditiAnnualiLogic.class.getName() + "@REDDITIKEY";
	public static final String ALTRIREDDITI = RedditiAnnualiLogic.class.getName() + "@ALTRIREDDITI";
	public static final String LISTA = "LISTA_REDDITIANNUALI";
	public static final String QUADRO_RB = RedditiAnnualiLogic.class.getName() + "@QUADRO_RB";

	private final static String SQL_SELECT_LISTA = "SELECT ROWNUM N, SEL.* FROM (" +
													"SELECT RED_DAT_ANA.*, RED_DESC.TIPO_MODELLO " +
													"FROM RED_DATI_ANAGRAFICI RED_DAT_ANA" +
													" LEFT OUTER JOIN RED_DESCRIZIONE RED_DESC" +
													" ON RED_DAT_ANA.IDE_TELEMATICO = RED_DESC.IDE_TELEMATICO" +
													" AND RED_DAT_ANA.CODICE_FISCALE_DIC = RED_DESC.CODICE_FISCALE_DIC" +
													" AND RED_DAT_ANA.ANNO_IMPOSTA = RED_DESC.ANNO_IMPOSTA " +
													"WHERE 1=? ";
	private final static String SQL_SELECT_DETTAGLIO = "SELECT *" +
	" FROM RED_DATI_ANAGRAFICI RED_DAT_ANA" +
	" LEFT OUTER JOIN RED_ATTIVITA RED_ATT" +
	" ON RED_DAT_ANA.IDE_TELEMATICO = RED_ATT.IDE_TELEMATICO" +
	" AND RED_DAT_ANA.CODICE_FISCALE_DIC = RED_ATT.CODICE_FISCALE_DIC" +
	" AND RED_DAT_ANA.ANNO_IMPOSTA = RED_ATT.ANNO_IMPOSTA" +
	" LEFT OUTER JOIN RED_DESCRIZIONE RED_DESC" +
	" ON RED_DAT_ANA.IDE_TELEMATICO = RED_DESC.IDE_TELEMATICO" +
	" AND RED_DAT_ANA.CODICE_FISCALE_DIC = RED_DESC.CODICE_FISCALE_DIC" +
	" AND RED_DAT_ANA.ANNO_IMPOSTA = RED_DESC.ANNO_IMPOSTA" +
	" LEFT OUTER JOIN RED_DOMICILIO_FISCALE RED_DOM_FISC" +
	" ON RED_DAT_ANA.IDE_TELEMATICO = RED_DOM_FISC.IDE_TELEMATICO" +
	" AND RED_DAT_ANA.CODICE_FISCALE_DIC = RED_DOM_FISC.CODICE_FISCALE_DIC" +
	" AND RED_DAT_ANA.ANNO_IMPOSTA = RED_DOM_FISC.ANNO_IMPOSTA" +
	" WHERE 1=? ";
	
	private final static String SQL_SELECT_COUNT_LISTA = "SELECT COUNT(*) AS CONTEGGIO " +
					"FROM RED_DATI_ANAGRAFICI RED_DAT_ANA" +
					" LEFT OUTER JOIN RED_DESCRIZIONE RED_DESC" +
					" ON RED_DAT_ANA.IDE_TELEMATICO = RED_DESC.IDE_TELEMATICO" +
					" AND RED_DAT_ANA.CODICE_FISCALE_DIC = RED_DESC.CODICE_FISCALE_DIC" +
					" AND RED_DAT_ANA.ANNO_IMPOSTA = RED_DESC.ANNO_IMPOSTA " +
					"WHERE 1=? ";
	
	private final static String SQL_SELECT_ALTRO_DIC = "SELECT * FROM RED_DATI_ANAGRAFICI" +
													" WHERE IDE_TELEMATICO = ? AND CODICE_FISCALE_DIC <> ?" + 
													" AND ANNO_IMPOSTA = ?" +
													" ORDER BY COGNOME, NOME";
	
	private final static String SQL_SELECT_REDDITI_DIC = "SELECT DISTINCT RED_RED_DIC.*, RED_TRAS.DESCRIZIONE, RED_TRAS.FLG_IMPORTO, RED_TRAS.CENT_DIVISORE " +
		" FROM RED_REDDITI_DICHIARATI RED_RED_DIC" +
		" LEFT OUTER JOIN RED_TRASCODIFICA RED_TRAS" +
		" ON RED_RED_DIC.CODICE_QUADRO = RED_TRAS.CODICE_RIGA" +
		" AND RED_RED_DIC.ANNO_IMPOSTA = RED_TRAS.ANNO_IMPOSTA" +
		" WHERE RED_RED_DIC.IDE_TELEMATICO = ?" +
		" AND RED_RED_DIC.CODICE_FISCALE_DIC = ?" +
		" AND RED_RED_DIC.ANNO_IMPOSTA = ? " +
		" ORDER BY CODICE_QUADRO";
	
	private final static String SQL_SELECT_COMUNE = "SELECT * FROM SITICOMU WHERE CODI_FISC_LUNA = ?";
	
	private final static String SQL_SELECT_NATURA_GIURIDICA = "SELECT * FROM ANAG_NATURA_GIURIDICA WHERE CODICE = ? AND ANNO= ? AND TIPOMODELLO= ?";
	private final static String SQL_SELECT_ONLUS = "SELECT * FROM ANAG_ONLUS WHERE CODICE = ? AND ANNO= ? AND TIPOMODELLO= ?";
	private final static String SQL_SELECT_SETTORE_ONLUS = "SELECT * FROM ANAG_SETTORE_ONLUS WHERE CODICE = ? AND ANNO= ? AND TIPOMODELLO= ?";
	private final static String SQL_SELECT_SITUAZIONE = "SELECT * FROM ANAG_SITUAZIONE WHERE CODICE = ? AND ANNO= ? AND TIPOMODELLO= ?";
	private final static String SQL_SELECT_STATO_SOCIETA = "SELECT * FROM ANAG_STATO_SOCIETA WHERE CODICE = ? AND ANNO= ? AND TIPOMODELLO= ?";
	
	private final static HashMap<String, HashMap<String, String>> tipiModello = new HashMap<String, HashMap<String, String>>();
	
	static {
		HashMap<String, String> tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2004", tipoModello);
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2005", tipoModello);
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2006", tipoModello);
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2007", tipoModello);
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2008", tipoModello);
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2009", tipoModello);
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2010", tipoModello);		
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2011", tipoModello);		
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2012", tipoModello);
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("2013", tipoModello);		
		tipoModello = new HashMap<String, String>();
		tipoModello.put("U", "Modello Unico Persone fisiche");
		tipoModello.put("3", "Modello 730");
		tipoModello.put("5", "Modello Unico Societ&agrave; di Persone");
		tipoModello.put("6", "Modello Unico Societ&agrave; di Capitali");
		tipoModello.put("8", "Modello Unico Enti non Commerciali");
		tipoModello.put("S", "Modello 770 semplificato");		
		tipiModello.put("DEFAULT", tipoModello);
	}
	
	public static String getDescTipoModello(RedditiAnnuali red) {
		if (tipiModello.get(red.getAnnoImposta()) != null && tipiModello.get(red.getAnnoImposta()).get(red.getTipoModello()) != null) {
			return tipiModello.get(red.getAnnoImposta()).get(red.getTipoModello());
		}
		if (tipiModello.get("DEFAULT") != null && tipiModello.get("DEFAULT").get(red.getTipoModello()) != null) {
			return tipiModello.get("DEFAULT").get(red.getTipoModello());
		}
		return "";
	}
	
	private static boolean isVisContribuente(RedditiAnnuali red) {
		if (red.getAnnoImposta().equals("2004")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2005")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2006")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2007")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2008")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2009")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2010")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2011")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2012")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2013")) {
			return red.getTipoModello().equals("3");
		}
		return false;
	}
	
	private static boolean isVisCollegati(RedditiAnnuali red) {
		if (red.getAnnoImposta().equals("2004")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2005")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2006")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2007")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2008")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2009")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2010")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2011")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2012")) {
			return red.getTipoModello().equals("3");
		}
		if (red.getAnnoImposta().equals("2013")) {
			return red.getTipoModello().equals("3");
		}
		return false;
	}
	
	private static boolean isVisSostitutoImposta(RedditiAnnuali red) {
		if (red.getAnnoImposta().equals("2004")) {
			return red.getTipoModello().equals("S");
		}
		if (red.getAnnoImposta().equals("2005")) {
			return red.getTipoModello().equals("S");
		}
		if (red.getAnnoImposta().equals("2006")) {
			return red.getTipoModello().equals("S");
		}
		if (red.getAnnoImposta().equals("2007")) {
			return red.getTipoModello().equals("S");
		}
		if (red.getAnnoImposta().equals("2008")) {
			return red.getTipoModello().equals("S");
		}
		if (red.getAnnoImposta().equals("2009")) {
			return red.getTipoModello().equals("S");
		}
		if (red.getAnnoImposta().equals("2010")) {
			return red.getTipoModello().equals("S");
		}
		if (red.getAnnoImposta().equals("2011")) {
			return red.getTipoModello().equals("S");
		}
		if (red.getAnnoImposta().equals("2012")) {
			return red.getTipoModello().equals("S");
		}
		if (red.getAnnoImposta().equals("2013")) {
			return red.getTipoModello().equals("S");
		}
		return false;
	}
	
	public Hashtable mCaricareDettaglio(String chiave,HttpServletRequest request) throws Exception{
		
		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {

			conn = this.getConnection();
			this.initialize();
			String sql = SQL_SELECT_DETTAGLIO + " AND RED_DAT_ANA.IDE_TELEMATICO = ?" +
						" AND RED_DAT_ANA.CODICE_FISCALE_DIC = ?" +
						" AND RED_DAT_ANA.ANNO_IMPOSTA = ?";

			int indice = 1;
			this.setInt(indice,1);
			indice++;
			String arrChiave[] = chiave.split("_");
			for (int i = 0; i < arrChiave.length; i++) {
				this.setString(indice,(String)arrChiave[i]);
				indice++;
			}			

			RedditiAnnuali red = new RedditiAnnuali();
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			ht= getDettaglioRedditi(rs, conn, request);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = chiave;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
				
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
			
	}
	
	public Hashtable mCaricareDettaglioFromSoggetto(String idSoggetto,HttpServletRequest request) throws Exception{
		
		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {

			conn = this.getConnection();
			this.initialize();
			String sql = SQL_SELECT_DETTAGLIO + " AND RED_DAT_ANA.IDE_TELEMATICO = ?" +
						" AND RED_DAT_ANA.CODICE_FISCALE_DIC = ?" ;

			int indice = 1;
			this.setInt(indice,1);
			indice++;
			//String arrChiave[] = idSoggetto.split("|");
			StringTokenizer st = new StringTokenizer(idSoggetto, "|");

			this.setString(2, st.nextToken());
			this.setString(3, st.nextToken());
			
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			ht= getDettaglioRedditi(rs, conn,request);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = idSoggetto;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
				
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}


	private Hashtable getDettaglioRedditi(java.sql.ResultSet rs, Connection conn,HttpServletRequest request) throws Exception{

		Hashtable ht = new Hashtable();
		
		RedditiAnnuali red = new RedditiAnnuali();
				
			if (rs.next()){
				
				red.setIdeTelematico(rs.getObject("IDE_TELEMATICO") != null ? rs.getString("IDE_TELEMATICO") : "");
				red.setCodiceFiscaleDic(rs.getObject("CODICE_FISCALE_DIC") != null ? rs.getString("CODICE_FISCALE_DIC") : "");
				red.setFlagPersona(rs.getObject("FLAG_PERSONA") != null ? rs.getString("FLAG_PERSONA") : "");
				red.setCognome(rs.getObject("COGNOME") != null ? rs.getString("COGNOME") : "");
				red.setNome(rs.getObject("NOME") != null ? rs.getString("NOME") : "");
				if (rs.getObject("COMUNE_NASCITA") != null) {
					red.setCodComuneNascita(rs.getString("COMUNE_NASCITA"));
					String decComune[] = decodificaComune(conn, rs.getString("COMUNE_NASCITA"));
					red.setDesComuneNascita(decComune[0]);
					red.setProvComuneNascita(decComune[1]);
				}else{
					red.setCodComuneNascita("");
					red.setDesComuneNascita("");
					red.setProvComuneNascita("");
				}
				if (rs.getString("DATA_NASCITA") != null && rs.getString("DATA_NASCITA").length() == 8) {									
					String data = "";
					String dataDB = rs.getString("DATA_NASCITA");
					data = dataDB.substring(6, 8) + "/" + dataDB.substring(4, 6) + "/" + dataDB.substring(0, 4);
					red.setDataNascita(data);
				}else{
					red.setDataNascita("");
				}				
				red.setSesso(rs.getObject("SESSO") != null ? rs.getString("SESSO") : "");
				red.setCodiceRE(rs.getObject("CODICE_RE") != null ? rs.getString("CODICE_RE") : "");
				red.setCodiceRF(rs.getObject("CODICE_RF") != null ? rs.getString("CODICE_RF") : "");
				red.setCodiceRG(rs.getObject("CODICE_RG") != null ? rs.getString("CODICE_RG") : "");
				red.setCfSostitutoImposta(rs.getObject("CF_SOSTITUTO_IMPOSTA") != null ? rs.getString("CF_SOSTITUTO_IMPOSTA") : "");
				red.setTipoModello(rs.getObject("TIPO_MODELLO") != null ? rs.getString("TIPO_MODELLO") : "");
				red.setDicCorrettiva(rs.getObject("DIC_CORRETTIVA") != null ? rs.getString("DIC_CORRETTIVA") : "");
				red.setDicIntegrativa(rs.getObject("DIC_INTEGRATIVA") != null ? rs.getString("DIC_INTEGRATIVA") : "");
				red.setStatoDichiarazione(rs.getObject("STATO_DICHIARAZIONE") != null ? rs.getString("STATO_DICHIARAZIONE") : "");
				red.setFlagValuta(rs.getObject("FLAG_VALUTA") != null ? rs.getString("FLAG_VALUTA") : "");				
				if (rs.getObject("CODICE_CAT_DOM_FISCALE_DIC") != null) {
					red.setCodCatDomFiscaleDic(rs.getString("CODICE_CAT_DOM_FISCALE_DIC"));
					String decComune[] = decodificaComune(conn, rs.getString("CODICE_CAT_DOM_FISCALE_DIC"));
					red.setDesCatDomFiscaleDic(decComune[0]);
					red.setProvCatDomFiscaleDic(decComune[1]);
				}else{
					red.setCodCatDomFiscaleDic("");
					red.setDesCatDomFiscaleDic("");
					red.setProvCatDomFiscaleDic("");
				}
				if (rs.getObject("CODICE_CAT_DOM_FISCALE_ATTUALE") != null) {
					red.setCodCatDomFiscaleAttuale(rs.getString("CODICE_CAT_DOM_FISCALE_ATTUALE"));
					String decComune[] = decodificaComune(conn, rs.getString("CODICE_CAT_DOM_FISCALE_ATTUALE"));
					red.setDesCatDomFiscaleAttuale(decComune[0]);
					red.setProvCatDomFiscaleAttuale(decComune[1]);
				}else{
					red.setCodCatDomFiscaleAttuale("");	
					red.setDesCatDomFiscaleAttuale("");
					red.setProvCatDomFiscaleAttuale("");
				}	
				
				red.setIndirizzoAttuale(rs.getObject("INDIRIZZO_ATTUALE") != null ? rs.getString("INDIRIZZO_ATTUALE") : "");
				red.setCapAttuale(rs.getObject("CAP") != null ? rs.getString("CAP") : "");
				red.setAnnoImposta(rs.getObject("ANNO_IMPOSTA") != null ? rs.getString("ANNO_IMPOSTA") : "");
				
				red.setDenominazione(rs.getObject("DENOMINAZIONE") != null ? rs.getString("DENOMINAZIONE") : "");
				red.setNaturaGiuridica(rs.getObject("NATURA_GIURIDICA") != null ? new Integer(rs.getInt("NATURA_GIURIDICA")) : new Integer(0));
				red.setSituazione(rs.getObject("SITUAZIONE") != null ? new Integer(rs.getInt("SITUAZIONE")) : new Integer(0));
				red.setStato(rs.getObject("STATO") != null ? new Integer(rs.getInt("STATO")) : new Integer(0));
				red.setOnlus(rs.getObject("ONLUS") != null ? new Integer(rs.getInt("ONLUS")) : new Integer(0));
				red.setSettoreOnlus(rs.getObject("SETTORE_ONLUS") != null ? new Integer(rs.getInt("SETTORE_ONLUS")) : new Integer(0));
				
				if (!red.getNaturaGiuridica().equals(new Integer(0))){
					String naturaGiuridica= this.decodificaCodiciRedditi(conn, "NATURA_GIURIDICA", red.getNaturaGiuridica(), Integer.valueOf(red.getAnnoImposta()), red.getTipoModello());
					red.setDescNaturaGiuridica(naturaGiuridica);
				}
				else
					red.setDescNaturaGiuridica("");
				if (!red.getSituazione().equals(new Integer(0))){
					String situazione= this.decodificaCodiciRedditi(conn, "SITUAZIONE", red.getSituazione(), Integer.valueOf(red.getAnnoImposta()), red.getTipoModello());
					red.setDescSituazione(situazione);
				}
				else
					red.setDescSituazione("");
				if (!red.getStato().equals(new Integer(0))){
					String stato= this.decodificaCodiciRedditi(conn, "STATO_SOCIETA", red.getStato(), Integer.valueOf(red.getAnnoImposta()), red.getTipoModello());
					red.setDescStato(stato);
				}
				else
					red.setDescStato("");
				if (!red.getOnlus().equals(new Integer(0))){
					String onlus= this.decodificaCodiciRedditi(conn, "ONLUS", red.getOnlus(), Integer.valueOf(red.getAnnoImposta()), red.getTipoModello());
					red.setDescOnlus(onlus);
				}
				else
					red.setDescOnlus("");
				if (!red.getSettoreOnlus().equals(new Integer(0))){
					String settoreOnlus= this.decodificaCodiciRedditi(conn, "SETTORE_ONLUS", red.getSettoreOnlus(), Integer.valueOf(red.getAnnoImposta()), red.getTipoModello());
					red.setDescSettoreOnlus(settoreOnlus);
				}
				else
					red.setDescSettoreOnlus("");
					
				
				String codEnte = "";
				ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
				while (rsEnte.next()) {
					codEnte = rsEnte.getString("codent");
				}
				red.setLinkAnagrafe(codEnte.equalsIgnoreCase(red.getCodCatDomFiscaleDic()));
				red.setLinkFamiglia(codEnte.equalsIgnoreCase(red.getCodCatDomFiscaleDic()));
				
				TributiOggettiICINewLogic oggettiICIlogic = new TributiOggettiICINewLogic(new EnvUtente(this.getEnvUtente().getUtente(), 
						EscServlet.defaultDataSource,
						this.getEnvUtente().getNomeApplicazione()));
				int numOggettiICI =0;
				
				numOggettiICI = oggettiICIlogic.getCountListaSoggetto(red.getCodiceFiscaleDic(), Integer.parseInt(red.getAnnoImposta()));
				red.setLinkDichiarazioniICI(numOggettiICI > 0);				
				
				CatastoIntestatariFLogic catastoLogic = new CatastoIntestatariFLogic(new EnvUtente(this.getEnvUtente().getUtente(), 
											EscServlet.defaultDataSource,
											this.getEnvUtente().getNomeApplicazione()));
				int numCatasto = catastoLogic.getCountListaSoggetto(red.getCodiceFiscaleDic(), Integer.parseInt(red.getAnnoImposta()));
				red.setLinkDettaglioCatasto(numCatasto > 0);
				
				VersamentiNewLogic versamentiLogic = new VersamentiNewLogic(new EnvUtente(this.getEnvUtente().getUtente(), 
							EscServlet.defaultDataSource,
							this.getEnvUtente().getNomeApplicazione()));
				int numVersamentiICI = versamentiLogic.getCountListaSoggetto(red.getCodiceFiscaleDic(), Integer.parseInt(red.getAnnoImposta()));				
				red.setLinkVersamentiICI(numVersamentiICI > 0);
				
				LocazioniLogic locazioniLogic = new LocazioniLogic(new EnvUtente(this.getEnvUtente().getUtente(), 
											EscServlet.defaultDataSource,
											this.getEnvUtente().getNomeApplicazione()));
				int numLocazioni = locazioniLogic.getCountListaSoggetto(red.getCodiceFiscaleDic(), Integer.parseInt(red.getAnnoImposta()));					
				red.setLinkLocazioni(numLocazioni > 0);
				
				red.setDescTipoModello(getDescTipoModello(red));
				red.setVisContribuente(isVisContribuente(red));
				red.setVisCollegati(isVisCollegati(red));
				
				if (isVisCollegati(red)) {
					// cerco eventuali altri soggetti con stesso IDE_TELEMATICO
					PreparedStatement pstm  = null;
					ResultSet altroRs = null;
					Vector vct = new Vector();
					try {					
						pstm = conn.prepareStatement(SQL_SELECT_ALTRO_DIC);
						pstm.setString(1, rs.getString("IDE_TELEMATICO") );
						pstm.setString(2, rs.getString("CODICE_FISCALE_DIC"));
						pstm.setString(3, rs.getString("ANNO_IMPOSTA"));
						altroRs = pstm.executeQuery();
						while (altroRs.next()){
							RedditiAnnuali altroRed = new RedditiAnnuali();
							altroRed.setIdeTelematico(altroRs.getObject("IDE_TELEMATICO") !=  null ? altroRs.getString("IDE_TELEMATICO") : "");
							altroRed.setCodiceFiscaleDic(altroRs.getObject("CODICE_FISCALE_DIC") !=  null ? altroRs.getString("CODICE_FISCALE_DIC") : "");
							altroRed.setCognome(altroRs.getObject("COGNOME") !=  null ? altroRs.getString("COGNOME") : "");
							altroRed.setNome(altroRs.getObject("NOME") !=  null ? altroRs.getString("NOME") : "");
							altroRed.setAnnoImposta(altroRs.getObject("ANNO_IMPOSTA") !=  null ? altroRs.getString("ANNO_IMPOSTA") : "");
							vct.add(altroRed);
						}
						ht.put(RedditiAnnualiLogic.ALTRIREDDITI, vct);
					} finally {
						if (altroRs!=null)
							altroRs.close();
						if (pstm!=null)
							pstm.close();
					}
				} else {
					ht.put(RedditiAnnualiLogic.ALTRIREDDITI, new Vector());
				}
				
				red.setVisSostitutoImposta(isVisSostitutoImposta(red));
				
				//Carico i redditi dichiarati
				
				String ideTelematico = rs.getString("IDE_TELEMATICO");
				String codFisc = rs.getString("CODICE_FISCALE_DIC");
				String anno = rs.getString("ANNO_IMPOSTA");
				
				this.caricaRedditiDichiarati(conn, ideTelematico, codFisc, anno, red);

				//Carico il dettaglio (di interesse) dei redditi analitici
				List<RigaQuadroRbDTO> lstRB = this.caricaQuadroRB(codFisc, ideTelematico, anno);
				ht.put(RedditiAnnualiLogic.QUADRO_RB, lstRB);
			}

			ht.put(RedditiAnnualiLogic.REDDITI,red);
		
			

			return ht;
	}

	
	public void caricaRedditiDichiarati(Connection conn, String ideTelematico, String codFisc, String anno, RedditiAnnuali red) throws SQLException{
		
		PreparedStatement pstmRed  = null;
		ResultSet redditiRs = null;
		try {					
			pstmRed = conn.prepareStatement(SQL_SELECT_REDDITI_DIC);
			pstmRed.setString(1, ideTelematico);
			pstmRed.setString(2, codFisc);
			pstmRed.setString(3, anno);
			redditiRs = pstmRed.executeQuery();
			while (redditiRs.next()){
				DecoRedditiDichiarati redDic = new DecoRedditiDichiarati();
				redDic.setIdeTelematico(redditiRs.getString("IDE_TELEMATICO"));
				redDic.setCodiceFiscaleDic(redditiRs.getString("CODICE_FISCALE_DIC"));
				redDic.setCodiceQuadro(redditiRs.getString("CODICE_QUADRO"));	
				redDic.setDescrizioneQuadro(redditiRs.getString("DESCRIZIONE"));
				redDic.setValoreContabile(redditiRs.getString("VALORE_CONTABILE"));
				redDic.setAnnoImposta(redditiRs.getString("ANNO_IMPOSTA"));
				redDic.setCentDivisore(redditiRs.getInt("CENT_DIVISORE"));
				
				int flgImporto= redditiRs.getInt("FLG_IMPORTO");
				if (flgImporto == 1 )
					redDic.setFlgImporto(true);
				else
					redDic.setFlgImporto(false);
				
				red.addReddito(redDic);
			}
		} finally {
			if (redditiRs!=null)
				redditiRs.close();
			if (pstmRed!=null)
				pstmRed.close();
		}
		
	}
	

	public Hashtable mCaricareLista(Vector listaPar, RedditiAnnualiFinder finder) throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		try
		{
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;

			for (int i=0;i<=1;i++)
			{
				// il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					//controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					
					if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
						String ente = controllo.substring(0,controllo.indexOf("|"));
						ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
						String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
						sql = sql + " and RED_DATI_ANAGRAFICI.IDE_TELEMATICO || '|' || RED_DATI_ANAGRAFICI.CODICE_FISCALE_DIC in (" +chiavi + ")" ;
					}else
						sql = sql + " and RED_DATI_ANAGRAFICI.IDE_TELEMATICO || '|' || RED_DATI_ANAGRAFICI.CODICE_FISCALE_DIC in (" +finder.getKeyStr() + ")" ;

					
				}
					
				

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1){					
					//sql = sql + " order by COGNOME, NOME, ANNO_IMPOSTA ";
					//sql = sql + ") where N > " + limInf + " and N <=" + limSup;
					sql = "SELECT * FROM(" + sql;
					sql = sql + " order by RED_DAT_ANA.COGNOME, RED_DAT_ANA.NOME, RED_DAT_ANA.DENOMINAZIONE, RED_DAT_ANA.CODICE_FISCALE_DIC, RED_DAT_ANA.ANNO_IMPOSTA) SEL)";
					sql = sql + " where N > " + limInf + " and N <=" + limSup;
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						RedditiAnnuali red = new RedditiAnnuali();
						red.setIdeTelematico(rs.getString("IDE_TELEMATICO"));
						red.setCodiceFiscaleDic(rs.getString("CODICE_FISCALE_DIC"));
						red.setCognome(rs.getString("COGNOME"));
						red.setNome(rs.getString("NOME"));
						red.setAnnoImposta(rs.getString("ANNO_IMPOSTA"));
						red.setDenominazione(rs.getString("DENOMINAZIONE"));
						red.setTipoModello(rs.getObject("TIPO_MODELLO") != null ? rs.getString("TIPO_MODELLO") : "");
						red.setDescTipoModello(getDescTipoModello(red));

						vct.add(red);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(RedditiAnnualiLogic.LISTA,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(RedditiAnnualiLogic.FINDER,finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = listaPar;
				arguments[1] = finder;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	public String leggiChiaveAnagrafe(String codiceFiscaleDic) throws Exception {
		String chiave = "";
		Connection conn = null;
		try {
			conn = this.getConnection();
			chiave = leggiAnagrafe(conn, codiceFiscaleDic, "ID");
			return chiave;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	public String leggiIdExtAnagrafe(String codiceFiscaleDic) throws Exception {
		String idExt = "";
		Connection conn = null;
		try {
			conn = this.getConnection();
			idExt = leggiAnagrafe(conn, codiceFiscaleDic, "ID_EXT");
			return idExt;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	public String leggiCodAnagrafe(String codiceFiscaleDic) throws Exception {
		String codAnagrafe = "";
		Connection conn = null;
		try {
			conn = this.getConnection();
			codAnagrafe = leggiAnagrafe(conn, codiceFiscaleDic, "COD_ANAGRAFE");
			return codAnagrafe;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	private String leggiAnagrafe(Connection conn, String codiceFiscaleDic, String nomeCampo) throws Exception {
		String retVal = "";
		String codEnte = "";
		ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
		while (rsEnte.next()) {
			codEnte = rsEnte.getString("codent");
		}
		sql = getProperty("sql.SELECT_LISTA", it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic.class);
		int indice = 1;
		this.initialize();
		this.setInt(indice,1);
		indice ++;
		EscElementoFiltro filtro =  new EscElementoFiltro();
		filtro.setTipo("S");
		filtro.setOperatore("=");
		filtro.setCampoFiltrato("CODENT");
		filtro.setValore(codEnte);
		filtro =  new EscElementoFiltro();
		filtro.setTipo("S");
		filtro.setOperatore("=");
		filtro.setCampoFiltrato("SIT_D_PERSONA.CODFISC");
		filtro.setValore(codiceFiscaleDic);
		Vector listaPar = new Vector();
		listaPar.add(filtro);
		sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
		sql = sql + "))";
		prepareStatement(sql);
		ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		while (rs.next()){
			retVal = (rs.getString(nomeCampo));
		}
		return retVal;
	}
	
	public Vector caricaFamiglia(String codAnagrafe, int annoImposta) throws Exception {
		AnagrafeDwhLogic anagrafeLogic = new AnagrafeDwhLogic(this.getEnvUtente());
		Hashtable ht = anagrafeLogic.mCaricareIndirizzi(codAnagrafe);
		Vector vecTutti = (Vector)ht.get(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI);
		Vector retVal = new Vector();
		//carica solo la famiglia al 31/12 dell'anno dell'imposta
		for (Object obj : vecTutti) {
			AnagrafeStorico ana = (AnagrafeStorico)obj;
			java.util.Date dataIni = DateFormat.stringToDate(ana.getInizioResidenza(),"dd/MM/yyyy");
			java.util.Date dataFin = DateFormat.stringToDate(ana.getFineResidenza(),"dd/MM/yyyy");
			java.util.Date miaData = new GregorianCalendar(annoImposta, 12, 31).getTime();
			if ((dataIni == null || miaData.getTime() >= dataIni.getTime()) && 
				(dataFin == null || miaData.getTime() <= dataFin.getTime())) {
				ArrayList listFamTutti = ana.getElencoFamiliari();
				ArrayList listFam = new ArrayList();
				for (Object objFam : listFamTutti) {
					FamiliariStorico fs = (FamiliariStorico)objFam;
					java.util.Date dataIniFam = DateFormat.stringToDate(fs.getDtInizio(),"dd/MM/yyyy");
					java.util.Date dataFinFam = DateFormat.stringToDate(fs.getDtFine(),"dd/MM/yyyy");
					if ((dataIniFam == null || miaData.getTime() >= dataIniFam.getTime()) && 
						(dataFinFam == null || miaData.getTime() <= dataFinFam.getTime())) {
						listFam.add(fs);
					}
				}
				ana.setElencoFamiliari(listFam);
				retVal.add(ana);
				break;
			}
		}
		return retVal;
	}
	
	private String[] decodificaComune(Connection conn, String codComune) throws SQLException {
		String[] retVal = new String[]{"", ""};
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(SQL_SELECT_COMUNE);
			pstmt.setString(1, codComune);
			rs = pstmt.executeQuery();
			while (rs.next()){
				//il primo elemento è il nome del comune, il secondo la sigla della provincia
				retVal[0] = rs.getString("NOME");
				retVal[1] = rs.getString("SIGLA_PROV");
			}			
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}		
		return retVal;
	}
	
	
	private String decodificaCodiciRedditi(Connection conn, String nomeTabella, Integer codice, Integer anno, String tipoModello) throws Exception {
		String retVal = "";
		this.initialize();
		ResultSet rs = null;
		
		
		try {
			
			if (nomeTabella.equals("NATURA_GIURIDICA")){
				this.setInt(1,codice);
				this.setInt(2, anno);
				this.setString(3,tipoModello);
				prepareStatement(SQL_SELECT_NATURA_GIURIDICA);
			}
			else if (nomeTabella.equals("ONLUS")){
				this.setInt(1,codice);
				this.setInt(2, anno);
				this.setString(3,tipoModello);
				prepareStatement(SQL_SELECT_ONLUS);
			}
			else if (nomeTabella.equals("SETTORE_ONLUS")){
				this.setInt(1,codice);
				this.setInt(2, anno);
				this.setString(3,tipoModello);
				prepareStatement(SQL_SELECT_SETTORE_ONLUS);
			}
			else if (nomeTabella.equals("SITUAZIONE")){
				this.setInt(1,codice);
				this.setInt(2, anno);
				this.setString(3,tipoModello);
				prepareStatement(SQL_SELECT_SITUAZIONE);
			}
			else if (nomeTabella.equals("STATO_SOCIETA")){
				this.setInt(1,codice);
				this.setInt(2, anno);
				this.setString(3,tipoModello);
				prepareStatement(SQL_SELECT_STATO_SOCIETA);
			}
			
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			while (rs.next()){
				
				retVal = (rs.getString("DESCRIZIONE")!= null ? rs.getString("DESCRIZIONE") :"");
				
			}			
		} catch (Exception sqle) {
			throw sqle;
		} finally {
			if (rs != null)
				rs.close();
			
		}		
		return retVal;
	}
	
	public List<RigaQuadroRbDTO> caricaQuadroRB(String codFisc, String ideTelematico, String anno){
		Context cont;
		List<RigaQuadroRbDTO> lst = new ArrayList<RigaQuadroRbDTO>();
		try {
			cont = new InitialContext();
		
			RedditiAnService redAnService= 
					(RedditiAnService)getEjb("CT_Service", "CT_Service_Data_Access", "RedditiAnServiceBean");
			
			KeySoggDTO key = new KeySoggDTO();
			key.setEnteId(this.getEnvUtente().getEnte());
			key.setUserId(this.getEnvUtente().getUtente().getUsername());
			key.setIdeTelematico(ideTelematico);
			key.setCodFis(codFisc);
			key.setAnno(anno);
			lst = redAnService.getQuadroRBFabbricatiByKeyAnno(key);
			
		} catch (NamingException e) {
			log.error("Errore recupero Quadro RB:"+e.getMessage(),e);
		}
		
		return lst;
		
	}

}

