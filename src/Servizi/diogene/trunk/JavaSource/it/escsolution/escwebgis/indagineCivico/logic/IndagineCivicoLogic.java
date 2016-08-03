package it.escsolution.escwebgis.indagineCivico.logic;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.taglibs.standard.lang.jpath.expression.SubstringAfterFunction;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.EscObject;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.indagineCivico.bean.Anagrafe;
import it.escsolution.escwebgis.indagineCivico.bean.Catasto;
import it.escsolution.escwebgis.indagineCivico.bean.ConsistenzaUI;
import it.escsolution.escwebgis.indagineCivico.bean.DatiDocfa;
import it.escsolution.escwebgis.indagineCivico.bean.DatiTarsu;
import it.escsolution.escwebgis.indagineCivico.bean.IndagineCivicoFinder;
import it.escsolution.escwebgis.indagineCivico.bean.Indirizzo;
import it.escsolution.escwebgis.indagineCivico.bean.Titolare;
import it.webred.DecodificaPermessi;
import it.webred.cet.permission.GestionePermessi;
import it.webred.utils.GenericTuples;



public class IndagineCivicoLogic extends EscLogic {
	//public final static String INDAGINE_CIVICO = "INDAGINE_CIVICO@IndagineCivicoLogic";
	public static final String LISTA_CIVICI = IndagineCivicoLogic.class.getName() + "@LISTA_CIVICI";
	public static final String INDIRIZZO = IndagineCivicoLogic.class.getName() + "@INDIRIZZO";
	public static final String LISTA_RESIDENTI= IndagineCivicoLogic.class.getName() + "@LISTA_RESIDENTI";
	public static final String LISTA_UI_CON_TITOLARE= IndagineCivicoLogic.class.getName() + "@LISTA_UI_CON_TITOLARE";
	public static final String LISTA_UI_CONSISTENZA= IndagineCivicoLogic.class.getName() + "@LISTA_UI_CONSISTENZA";
	public static final String DOCFA= IndagineCivicoLogic.class.getName() + "@DOCFA";
	public static final String FILE_EXCEL = IndagineCivicoLogic.class.getName() + "@FILE_EXCEL";
	
	private static String SQL_ENTE=null;
	private static String SQL_SELECT_COUNT_LISTA=null;
	private static String SQL_SELECT_LISTA=null;
	private static String SQL_SELECT_LISTA_FP_CIVICO=null;
	private static String SQL_INDIRIZZO=null;
	private static String SQL_DETT_RESIDENTI1=null; 
	private static String SELECT_DETT_RESIDENTI_RID=null;
	private static String SQL_TITOLARI =null;
	private static String SQL_DETT_UI=null;
	private static String SQL_INDIRIZZO_CAT_UI=null;
	private static String SQL_DOCFA_UIU=null;
	private static String SQL_DOCFA_ID_IMM=null;
	private static String SQL_TARSU =null;
	private static String SQL_TAB_DATI_XLS_1=null;
	private static String SQL_TAB_DATI_XLS_2=null;
	
	public static final DecimalFormat DF = new DecimalFormat();
    static {
           DF.setGroupingUsed(true);
           DecimalFormatSymbols dfs = new DecimalFormatSymbols();
           dfs.setDecimalSeparator(',');
           dfs.setGroupingSeparator('.');
           DF.setDecimalFormatSymbols(dfs);
    }
	
	public final static String FINDER = "FINDER111";
	
	private final static String FMT = "yyyy-MM-dd";
	
	public IndagineCivicoLogic(EnvUtente eu) {
		super(eu);
		SQL_ENTE = getProperty("SQL_ENTE");
		SQL_SELECT_COUNT_LISTA = getProperty("SQL_SELECT_COUNT_LISTA");
		SQL_SELECT_LISTA = getProperty("SQL_SELECT_LISTA");
		SQL_SELECT_LISTA_FP_CIVICO = getProperty("SQL_SELECT_LISTA_FP_CIVICO");
		SQL_INDIRIZZO = getProperty("SQL_INDIRIZZO");
		SQL_DETT_RESIDENTI1 = getProperty("SQL_DETT_RESIDENTI1");
		SELECT_DETT_RESIDENTI_RID = getProperty("SELECT_DETT_RESIDENTI_RID");
		SQL_TITOLARI = getProperty("SQL_TITOLARI");
		SQL_DETT_UI = getProperty("SQL_DETT_UI");
		SQL_INDIRIZZO_CAT_UI= getProperty("SQL_INDIRIZZO_CAT_UI");
		SQL_DOCFA_UIU= getProperty("SQL_DOCFA_UIU");
		SQL_DOCFA_ID_IMM = getProperty("SQL_DOCFA_ID_IMM");
		SQL_TARSU = getProperty("SQL_TARSU");
		SQL_TAB_DATI_XLS_1 = getProperty("SQL_TAB_DATI_XLS_1");
		SQL_TAB_DATI_XLS_2 = getProperty("SQL_TAB_DATI_XLS_2");
	}
	
	public Hashtable mCaricareListaIndCivico(Vector listaPar,IndagineCivicoFinder finder) throws Exception{
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		ResultSet rs = null;
		String codEnt="";
		int indice =0;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			codEnt = recuperaCodNazionale(conn);
			if (codEnt == null) {
				ht.put(IndagineCivicoLogic.LISTA_CIVICI,vct);
				return ht;
			}
			for (int i=0;i<=1;i++){
				// il primo ciclo faccio la count
				if (i==0)
					 sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;
				indice = 1;
				this.initialize();
				this.setString(indice,codEnt); 
				indice++;
				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}/*
				else{
					sql = sql + " and XXX in (" +finder.getKeyStr() + ")" ;
				}*/ 
				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				sql = sql + " order by PREFISSO, NOME, CIVICO";
				if (i ==1) {
					sql = sql + ")) WHERE N > " + limInf + " AND N <=" + limSup;
				} else {
					sql = sql + ")";
				}
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						Indirizzo ind = new Indirizzo();
						ind.setPkidStra(rs.getInt("PKID_STRA"));
						ind.setPkidCivi(rs.getInt("PKID_CIVI"));
						ind.setChiave("" + ind.getPkidCivi());
						ind.setPrefisso(rs.getString("PREFISSO"));
						ind.setNomeVia(rs.getString("NOME"));
						ind.setCivico(rs.getString("CIVICO"));
						ind.setCodNazionale(codEnt);
						ind.setFps(getFps(conn, codEnt, rs.getInt("PKID_CIVI")));						
						vct.add(ind);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put(IndagineCivicoLogic.LISTA_CIVICI,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER,finder);
			
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
			catch (Exception e) {
				log.error(e.getMessage(),e);
				throw e;
			}
			finally
			{
				if (rs != null )
					rs.close();
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}//---------------------------------------------------------------------
	
	public Hashtable mCaricareListaIndCivicoByParams(Vector listaPar) throws Exception{
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String codEnt="";
		int indice =0;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			codEnt = recuperaCodNazionale(conn);
			if (codEnt == null) {
				ht.put(IndagineCivicoLogic.LISTA_CIVICI,vct);
				return ht;
			}
//			for (int i=0;i<=1;i++){
				// il primo ciclo faccio la count
//				if (i==0)
//					 sql = SQL_SELECT_COUNT_LISTA;
//				else
					sql = SQL_SELECT_LISTA;
//				indice = 1;
//				this.initialize();
//				this.setString(indice,codEnt); 
//				indice++;
				// il primo passaggio esegue la select count
//				if (finder.getKeyStr().equals("")){
//					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
//				} 
//				long limInf, limSup;
//				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
//				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
					sql = sql + " AND V.PKID_STRA = ? AND C.CIVICO = ? )) ";

					sql = sql + " order by PREFISSO, NOME, CIVICO";
//				if (i ==1) {
					
//				} else {
//					sql = sql + ")";
//				}
				
//				prepareStatement(sql);
//				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					/*
					 * I parametri della query devono essere 3: ente, codice strada e civico
					 */
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, (String) listaPar.get(0));
				pstmt.setString(2, (String) listaPar.get(1));
				pstmt.setString(3, (String) listaPar.get(2));
				log.debug(sql);
				rs = pstmt.executeQuery();
//				if (i ==1) {
				while (rs.next()){
					Indirizzo ind = new Indirizzo();
					ind.setPkidStra(rs.getInt("PKID_STRA"));
					ind.setPkidCivi(rs.getInt("PKID_CIVI"));
					ind.setChiave("" + ind.getPkidCivi());
					ind.setPrefisso(rs.getString("PREFISSO"));
					ind.setNomeVia(rs.getString("NOME"));
					ind.setCivico(rs.getString("CIVICO"));
					ind.setCodNazionale(codEnt);
					ind.setFps(getFps(conn, codEnt, rs.getInt("PKID_CIVI")));						
					vct.add(ind);
				}
//				}
//				else{
//					if (rs.next()){
//						conteggio = rs.getString("conteggio");
//					}
//				}
//			}
			ht.put(IndagineCivicoLogic.LISTA_CIVICI,vct);
//			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
//			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
//			finder.setTotaleRecord(conteggione);
//			finder.setRighePerPagina(RIGHE_PER_PAGINA);

//			ht.put(FINDER,finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = listaPar;
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
				if (rs != null )
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}//---------------------------------------------------------------------
	
	public Hashtable mCaricareDettaglio(String chiave, boolean isCodificaViarioCongruente) throws Exception{
		Hashtable ht = new Hashtable();
		ArrayList<Anagrafe> listaResidenti = new ArrayList<Anagrafe>();
		ArrayList<Catasto> listaUIConTitolare = new ArrayList<Catasto>();
		Indirizzo indSel = new Indirizzo();
		Connection conn = null;
		ResultSet rs=null;
		PreparedStatement pst = null;
		int pkidCivi= Integer.parseInt(chiave); 
		try {
			conn = this.getConnection();
			String codEnt=recuperaCodNazionale(conn);
			if (codEnt == null){
				rs.close();
				ht.put(IndagineCivicoLogic.INDIRIZZO,indSel);
				ht.put(IndagineCivicoLogic.LISTA_RESIDENTI,listaResidenti);
				ht.put(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE,listaUIConTitolare);
				return ht;
			}
			indSel= getIndirizzo(conn, codEnt, pkidCivi);
			ht.put(INDIRIZZO, indSel);
			if (isCodificaViarioCongruente)
				sql = SQL_DETT_RESIDENTI1 ;
			else
				sql=""; // TODO FARE SQL_DETT_RESIDENTI2 (J_CIV_ANA_CAT non si riesce a farci select oltre 5 minuti select * anche con rownum <100)
			pst=conn.prepareStatement(sql);
			pst.setInt(1,pkidCivi);
			log.debug("FASCICOLO CIVICO--SQL residenti: " + sql);
			log.debug("parms: pkid_civi " + pkidCivi);
			rs = pst.executeQuery();
			while(rs.next()){
				Anagrafe ana = new Anagrafe();
				if (rs.getString("COD_ANAGRAFE") != null)
					ana.setCodAnagrafe(rs.getString("COD_ANAGRAFE"));
				ana.setId(rs.getString("ID"));
				ana.setIdExt(rs.getString("ID_EXT"));
				if(rs.getString("COGNOME") != null)
				   ana.setCognome(rs.getString("COGNOME"));
				if(rs.getString("NOME") != null)
				   ana.setNome(rs.getString("NOME"));
				if(rs.getString("CODICE_FISCALE") != null)
					ana.setCodFiscale(rs.getString("CODICE_FISCALE"));
				if(rs.getString("DATA_NASCITA") != null)
					ana.setDataNascita(rs.getString("DATA_NASCITA"));
				if(rs.getString("CITTADINANZA") != null)
					ana.setCittadinanza(rs.getString("CITTADINANZA"));
				if(rs.getString("COMUNE_NASCITA") != null)
					ana.setComuneNascita(rs.getString("COMUNE_NASCITA"));
				// LA AnagrafeDwhLogic da cui è stata ispirato questa parte di codice fa così, ma non ne capisco il motivo visto che già la query estrae questo campo..
				//ana.setCodiceNazionale(getCodiceNazionale(conn, rs.getString("ID"), rs.getString("CODICE_NAZIONALE")));
				if(rs.getString("CODICE_NAZIONALE") != null)
					ana.setCodiceNazionale(rs.getString("CODICE_NAZIONALE"));
				if(rs.getString("ID_FAM") != null)
					ana.setFamiglia(rs.getString("ID_FAM"));
				if(rs.getString("RELAZ_PAR") != null)
					ana.setTipoParentela(rs.getString("RELAZ_PAR"));
				listaResidenti.add(ana);
			}
			ht.put(IndagineCivicoLogic.LISTA_RESIDENTI,listaResidenti);
			// RECUPERA I DATI A LIVELLO DI UI
			Hashtable htDatiUi = getDatiUI(conn, codEnt, pkidCivi);
			ht.put(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE,htDatiUi.get("datiCensuari"));
			ht.put(IndagineCivicoLogic.LISTA_UI_CONSISTENZA,htDatiUi.get("datiConsistenza"));
			
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
		finally{
			if (rs!= null )
				rs.close();
			if (pst!= null )
				pst.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	//Recupera dalla chiave protocollo e fornitura  
	//La chiave docfa iN input è protocollo|fornitura con fornitura AAAAMMGG
	public DatiDocfa mRecuperaDocfa(String chiaveDocfa) throws Exception{
		DatiDocfa dati = new DatiDocfa();
		try{
			String  protocollo = chiaveDocfa.substring(0,chiaveDocfa.indexOf("|"));
			String fornitura = chiaveDocfa.substring(chiaveDocfa.indexOf("|")+1);
			String str1 = fornitura.substring(6) + "/" + fornitura.substring(4,6) + "/" +  fornitura.substring(0,4);
			dati.setProtocollo(protocollo);
			dati.setFornitura(str1);		
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		return dati;
	}	
	public Hashtable mExportToXls(String chiave, String sessionId) throws Exception{
		Hashtable ht = new Hashtable();
		HSSFWorkbook wb          = new HSSFWorkbook();
		Indirizzo indSel = new Indirizzo();
		//recupera i rs da esportare
		Connection conn = null;
		ResultSet rs = null;
		int pkidCivi= Integer.parseInt(chiave); 
		try {
			conn = this.getConnection();
			// recupero l'indirizzo (descrittivo)
			String codEnt = recuperaCodNazionale(conn);
			indSel = getIndirizzo(conn, codEnt, pkidCivi);
			ht.put(IndagineCivicoLogic.INDIRIZZO, indSel);
			// PREPARAZIONE FILE EXCEL
			ResultSet[] arrRs = new ResultSet[3];
			String [] nomiSheets = new String[3];
			// preparazione del ResultSet dei residenti
			this.initialize();
			sql = SELECT_DETT_RESIDENTI_RID + SQL_DETT_RESIDENTI1 + ")";
			int indice=1;
			this.setInt(indice,pkidCivi);
			log.debug("sql residenti xls: " + sql);
			log.debug("parms: pkid_civi " + pkidCivi);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			arrRs[0]= rs;
			nomiSheets[0]= "Residenti";
			//preparazione del ResultSet delle ui con i titolari
			// --recupera i dati e inseeridce la tabella temporanea
			Hashtable htDatiUi = getDatiUI(conn, codEnt, pkidCivi);
			ArrayList<Catasto> listaUIConTitolare = (ArrayList<Catasto>) htDatiUi.get("datiCensuariExcel");
			ArrayList<ConsistenzaUI> listaUIConsistenza = (ArrayList<ConsistenzaUI>) htDatiUi.get("datiConsistenza");
			insertTAB_DATI(conn, listaUIConTitolare, listaUIConsistenza,sessionId);
			// sheet 2
			this.initialize();
			sql = SQL_TAB_DATI_XLS_1; 
			indice=1;
			this.setString(indice,sessionId);
			prepareStatement(sql);
			log.debug("SQL_TAB_DATI_XLS_1: " + sql);
			log.debug("PARMS: " + sessionId);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			arrRs[1]= rs;
			nomiSheets[1]= "Dati censuari UI";
			// sheet 3
			this.initialize();
			sql = SQL_TAB_DATI_XLS_2; 
			indice=1;
			this.setString(indice,sessionId);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			arrRs[2]= rs;
			nomiSheets[2]= "Consistenza UI";
			// ESPORTAZIONE
     		wb =mExportToXls(arrRs, nomiSheets);
     		rs.close();
     		deleteTAB_DATI(conn, sessionId);
			
     		ht.put(IndagineCivicoLogic.FILE_EXCEL, wb);
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally{
			if (rs!= null )
				rs.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		return ht;
	}
	

	private Indirizzo recuperaIndirizzoCatasto(Connection conn, String foglio, String particella, String sub, String codNazionale ) throws Exception {
    	ResultSet rs =null;
		String sql=null;
		PreparedStatement pst =null;
		Indirizzo ind = new Indirizzo();
		boolean closeConn = false;
		try {
			if (conn == null) {
				conn = this.getConnection();
				closeConn = true;
			}				
			sql = SQL_INDIRIZZO_CAT_UI;
 			pst = conn.prepareStatement(sql);
			pst.setString(1, codNazionale);
			pst.setString(2, foglio.trim());
			pst.setString(3, particella.trim());
			pst.setString(4, sub.trim());
			rs = pst.executeQuery();
			if (rs.next()){
				ind.setPrefisso(rs.getString("PREFISSO"));
				ind.setNomeVia(rs.getString("VIA"));
				ind.setCivico(rs.getString("CIVICO"));
				String prefisso = rs.getObject("PREFISSO") == null ? "" : rs.getString("PREFISSO").trim();
				String via = rs.getObject("VIA") == null ? "" : rs.getString("VIA").trim();
				String civico = rs.getObject("CIVICO") == null ? "" : rs.getString("CIVICO").trim();
				String indirizzoCompleto = prefisso;
				if (!via.equals("")) {
					if (!indirizzoCompleto.equals("")) {
						indirizzoCompleto += " ";
					}
					indirizzoCompleto += via;
				}
				if (!civico.equals("")) {
					if (!indirizzoCompleto.equals("")) {
						indirizzoCompleto += " ";
					}
					indirizzoCompleto += civico;
				}
				ind.setIndirizzoCompleto(indirizzoCompleto);
			}
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();
			if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}
		return ind;
	}	
    
    
    private Hashtable getDatiUI(Connection conn, String codEnt, int pkidCivi) throws Exception {
    	Hashtable htRet = new Hashtable();
    	htRet.put("datiCensuari", new ArrayList<Catasto>());
    	htRet.put("datiCensuariExcel", new ArrayList<Catasto>());
    	htRet.put("datiConsistenza", new ArrayList<ConsistenzaUI>());
    	
    	ArrayList<Catasto> listaUIConTitolare = new ArrayList<Catasto>();
    	ArrayList<Catasto> listaUIConTitolareExcel = new ArrayList<Catasto>();
    	ArrayList<ConsistenzaUI> listaUIConsistenza= new ArrayList<ConsistenzaUI>();
    	ResultSet rs=null;
    	PreparedStatement pst=null;
    	try {
	    	String sql = SQL_DETT_UI;
	    	pst = conn.prepareStatement(sql);
	    	pst.setString(1, codEnt); 
	    	pst.setInt(2, pkidCivi);
	    	rs = pst.executeQuery();
			int contaTit=0;
			while (rs.next()){
				//DATI CENSUARI
				Catasto cat = new Catasto();
				String foglio="" + rs.getInt("FOGLIO"); 
				String particella =  rs.getString("PARTICELLA");
				String sub = "" + rs.getInt("UNIMM"); 
				cat.setFoglio(eliminaZeriSx(foglio));
				cat.setMappale(eliminaZeriSx(particella));
				cat.setSub(eliminaZeriSx(sub));
				if (rs.getString("CATEGORIA") != null)
					cat.setCategoria(rs.getString("CATEGORIA"));
				if (rs.getString("CLASSE") != null)
					cat.setClasse(rs.getString("CLASSE"));
				if (rs.getObject("RENDITA") != null) {
					cat.setRendita(rs.getDouble("RENDITA"));
					cat.setRenditaStrFmt(DF.format((rs.getDouble("RENDITA"))));
				}
				//acquisizione indirizzi
				Indirizzo ind =recuperaIndirizzoCatasto(conn, foglio, particella, sub, codEnt);
				cat.setIndirizzo(ind);
				//acquisizione titolari: N.B. deve essere l'ultima cosa
				//in maniera tale che i dati siano poi eventualmente ripetuti per tutti i titolari
				ArrayList<Titolare> listaTit = recuperaTitolari(conn, foglio, particella, sub, codEnt);
				/*if (listaTit.size() > 0){
					for(Titolare tit: listaTit) {
						contaTit++;
						if (contaTit == 1) {
							cat.setTitolare(tit);
							listaUIConTitolare.add(cat); 
						}else {
							Catasto cat1 = new Catasto(cat);
							cat1.setTitolare(tit);
							listaUIConTitolare.add(cat1);
						}
					}
				}else {
					listaUIConTitolare.add(cat); 
				}*/
				
				String listaCodFiscTitolari="";
			  	String listaDenomTitolari="";
			  	String listaTipoTitoloTitolari="";
				
				if (listaTit.size() > 0){
					for(int i=0; i<listaTit.size() ;i++) {
						Titolare tit= listaTit.get(i);
						if (i != 0){
							listaCodFiscTitolari += "<br>&nbsp;";
							listaDenomTitolari += "<br>&nbsp;";
							listaTipoTitoloTitolari += "<br>&nbsp;";
						}
						if (tit.getCodFisc()!= null && !tit.getCodFisc().equals(""))
								listaCodFiscTitolari += tit.getCodFisc();
						else
							listaCodFiscTitolari +="-";
						if (tit.getDenom()!= null && !tit.getDenom().equals(""))
							listaDenomTitolari += tit.getDenom();
						else
							listaDenomTitolari="-";
						if (tit.getTipoTitolo()!= null && !tit.getTipoTitolo().equals(""))
							listaTipoTitoloTitolari += tit.getTipoTitolo();	
						else
							listaTipoTitoloTitolari +="-";
					}
				}	
					cat.setListaCodFiscTitolari(listaCodFiscTitolari);
					cat.setListaDenomTitolari(listaDenomTitolari);
					cat.setListaTipoTitoloTitolari(listaTipoTitoloTitolari);
				
				listaUIConTitolare.add(cat); 
				
				// MEMORIZZAZIONE IN Hastable
				htRet.put("datiCensuari", listaUIConTitolare);
				
				//DATI CENSUARI PER EXPORT EXCEL
				
				
				Catasto catE = new Catasto();
				catE.setFoglio(eliminaZeriSx(foglio));
				catE.setMappale(eliminaZeriSx(particella));
				catE.setSub(eliminaZeriSx(sub));
				if (rs.getString("CATEGORIA") != null)
					catE.setCategoria(rs.getString("CATEGORIA"));
				if (rs.getString("CLASSE") != null)
					catE.setClasse(rs.getString("CLASSE"));
				if (rs.getObject("RENDITA") != null) {
					catE.setRendita(rs.getDouble("RENDITA"));
					catE.setRenditaStrFmt(DF.format((rs.getDouble("RENDITA"))));
				}
				//acquisizione indirizzi
				
				catE.setIndirizzo(ind);
				//acquisizione titolari: N.B. deve essere l'ultima cosa
				//in maniera tale che i dati siano poi eventualmente ripetuti per tutti i titolari
				
				if (listaTit.size() > 0){
					for(Titolare tit: listaTit) {
						contaTit++;
						if (contaTit == 1) {
							cat.setTitolare(tit);
							listaUIConTitolareExcel.add(catE); 
						}else {
							Catasto cat1 = new Catasto(catE);
							cat1.setTitolare(tit);
							listaUIConTitolareExcel.add(cat1);
						}
					}
				}else {
					listaUIConTitolareExcel.add(catE); 
				}
				
				
				 
				
				// MEMORIZZAZIONE IN Hastable
				htRet.put("datiCensuariExcel", listaUIConTitolareExcel);
				
				
				//DATI CONSISTENZA
				ConsistenzaUI consUi = new ConsistenzaUI();
				if (rs.getObject("CONSISTENZA") != null )	
					consUi.setConsistenza(rs.getDouble("CONSISTENZA"));
				if (rs.getObject("SUP_CAT") != null)
					consUi.setSuperficie(rs.getDouble("SUP_CAT"));
				if (rs.getObject("SUP_C340") != null)
					consUi.setSupC340(rs.getDouble("SUP_C340"));
				consUi.setFoglio(eliminaZeriSx(foglio));
				consUi.setMappale(eliminaZeriSx(particella));
				consUi.setSub(eliminaZeriSx(sub));
				// acquisizione info docfa  edati tarsu
				Hashtable ht = getInfoDocfa(conn, foglio, particella, sub);
				consUi.setInfoDocfa((String)ht.get("info"));
				DatiDocfa datiDocfa = (DatiDocfa)ht.get("dati");
				consUi.setDatiDocfa(datiDocfa);
				//manca l'immobile..ora si aggiunge
				datiDocfa.setIdImmobile(getIdImmobile(conn, foglio, particella, sub, datiDocfa.getDataFornitura(), datiDocfa.getProtocollo()));
				DatiTarsu datiTarsu = getDatiTarsu(conn, foglio, particella, sub);
				consUi.setSupTarsu(datiTarsu.getSupTot());
				consUi.setSupTarsuNonPres(datiTarsu.getSupNonPres());
				consUi.setDicTarsuCFPi(datiTarsu.getCodFisPIva());
				consUi.setDicTarsuDenominazione(datiTarsu.getDenominazione());
				consUi.setDicTarsuDesClsRsu(datiTarsu.getDesClsRsu());
				consUi.setDicTarsuDesTipOgg(datiTarsu.getDesTipOgg());
				//aggiungo alla lista
				listaUIConsistenza.add(consUi);
				// MEMORIZZAZIONE IN Hastable
				htRet.put("datiConsistenza",listaUIConsistenza);
			}
			return htRet;
    	}catch(Exception e){
    		log.error(e.getMessage(),e);
			throw e;
    	}
    	finally {
			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();
		}
    }
    
    private ArrayList<Titolare> recuperaTitolari(Connection conn, String foglio, String particella, String sub, String codNazionale) throws Exception {
		ResultSet rs =null;
		PreparedStatement pst = null;
		String sql=null;
		ArrayList<Titolare> listaTitolari = new ArrayList<Titolare>();
		boolean closeConn = false;
		try {
			if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}	
			sql =SQL_TITOLARI;
			pst = conn.prepareStatement(sql);
			pst.setString(1, codNazionale);
			pst.setString(2, foglio.trim());
			pst.setString(3, particella.trim());
			pst.setString(4, sub.trim());
			pst.setString(5, codNazionale);
			pst.setString(6, foglio.trim());
			pst.setString(7, particella.trim());
			pst.setString(8, sub.trim());
			rs= pst.executeQuery();
			while (rs.next()){
				Titolare tit = new Titolare();
				tit.setCodFisc(rs.getString("CUAA"));
				tit.setDenom(rs.getString("RAGI_SOCI"));
				tit.setTipoTitolo(rs.getString("DESCRIPTION"));
				listaTitolari.add(tit);
			}
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (pst != null)
				pst.close();
			if (rs != null)
				rs.close();
			if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}
		return listaTitolari;
	}	
    
    
    
    public String recuperaCodNazionale(Connection conn) throws Exception {
		PreparedStatement pst = null;
    	ResultSet rs =null;
		String sql=null;
		String codEnt=null;
		boolean closeConn = false;
		try {
			if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}	
			sql=SQL_ENTE;
 		    pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			rs = pst.executeQuery();
			if (rs.next()){
				codEnt=rs.getString("CODENT");
			}
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();
			if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}
		return codEnt;
	}	
    
    public Indirizzo getIndirizzo(Connection conn, String codEnt, int pkidCivi) throws Exception {
		PreparedStatement pst = null;
    	ResultSet rs =null;
		String sql=null;
		Indirizzo ind =new Indirizzo();
		boolean closeConn = false;
		try {
			if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}	
			sql= SQL_INDIRIZZO;
			pst=conn.prepareStatement(sql);
			pst.setString(1,codEnt);
			pst.setInt(2,pkidCivi);
			rs = pst.executeQuery();
			if (rs.next()) {
				ind.setPrefisso(rs.getString("PREFISSO"));
				ind.setPkidStra(rs.getInt("PKID_STRA"));
				ind.setPkidCivi(rs.getInt("PKID_CIVI"));
				ind.setChiave("" + ind.getPkidCivi());
				ind.setNomeVia(rs.getString("NOME"));
				ind.setCivico(rs.getString("CIVICO"));
				ind.setIndirizzoCompleto(rs.getString("PREFISSO") + " " + rs.getString("NOME") + " " + rs.getString("CIVICO"));
			}
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();
			if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}
		return ind;
	}	
    private Hashtable getInfoDocfa(Connection conn, String foglio, String particella, String sub) throws Exception {
		Hashtable ht = new Hashtable();
		DatiDocfa datiDocfa = new DatiDocfa();
    	PreparedStatement pst= null;
    	ResultSet rs =null;
		String sql=null;
		String infoDocfa="";
		boolean closeConn = false;
		try {
			if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}
			sql=SQL_DOCFA_UIU;
 		    pst = conn.prepareStatement(sql);
 		    pst.setString(1, fill("0", "sx", foglio, 4)) ;
			pst.setString(2, fill("0", "sx", particella, 5));
			pst.setString(3, fill("0", "sx", sub, 4));
			rs = pst.executeQuery();
			if (rs.next()){ // prendo solo il primo, perché sono ordinati per data desc e mi serve l'ultimo 
				infoDocfa =componiInfoDocfa(rs.getDate("FORNITURA"), rs.getString("PROTOCOLLO_REG"), FMT) ;
				datiDocfa.setFornitura(rs.getString("FORNITURA")); 
				datiDocfa.setDataFornitura(rs.getDate("FORNITURA"));
				datiDocfa.setProtocollo(rs.getString("PROTOCOLLO_REG"));
				String dtFornStr =rs.getDate("FORNITURA").toString(); 
				datiDocfa.setChiaveDocfa(rs.getString("PROTOCOLLO_REG") + "|" + dtFornStr.replace("-", "") );
			}
			ht.put("info", infoDocfa);
			ht.put("dati", datiDocfa);
			return ht;
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (rs!= null )
				rs.close();
			if (pst!= null )
				pst.close();
			if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}
	}	
    
    private DatiTarsu getDatiTarsu(Connection conn, String foglio, String particella, String sub) throws Exception {
		DatiTarsu dati= new DatiTarsu();
    	PreparedStatement pst= null;
    	ResultSet rs =null;
		String sql=null;
		boolean closeConn = false;
		try {
			if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}
			sql=SQL_TARSU;
			log.debug("IndagineCivicoLogic SQL_TARSU["+sql+"]"); 
			
			int foglioNum=-1, particellaNum=-1, subNum=-1;
			try {
				foglioNum= Integer.parseInt(foglio);
				particellaNum= Integer.parseInt(particella);
				subNum= Integer.parseInt(sub);
			}
			catch(NumberFormatException nfe) {
				return dati;
			}
 		    pst = conn.prepareStatement(sql);
		    pst.setString(1, "" + foglioNum);
			pst.setString(2, "" +particellaNum);
			pst.setString(3, "" +subNum);
			
			log.debug("Param foglio["+foglioNum+"]"); 
			log.debug("Param particella["+particellaNum+"]"); 
			log.debug("Param sub["+subNum+"]"); 
			
			rs = pst.executeQuery();
			if (rs.next()){  
				dati.setCodFisPIva(rs.getString("CODFISC"));//se partitaiva è valorizzata, allora è riportata  anche su codfisc
				dati.setSupTot(rs.getDouble("SUPERFICE_TOTALE"));
				dati.setDenominazione(rs.getString("DENOMINAZIONE"));
				dati.setSupNonPres(null);
				dati.setDesClsRsu(rs.getString("DES_CLS_RSU"));
				dati.setDesTipOgg(rs.getString("DES_TIP_OGG"));
			}
			
			return dati;
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (rs!= null )
				rs.close();
			if (pst!= null )
				pst.close();
			if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}
	}
    
    private ArrayList<Indirizzo> getFps(Connection conn, String codEnt, int idCivi) throws Exception {
    	ArrayList<Indirizzo> fps = new ArrayList<Indirizzo>();
    	PreparedStatement pst = null;
    	ResultSet rs = null;
		String sql = null;
		try {
			sql = SQL_SELECT_LISTA_FP_CIVICO;
 		    pst = conn.prepareStatement(sql);
		    pst.setString(1, codEnt);
			pst.setInt(2, idCivi);
			rs = pst.executeQuery();
			HashMap<String, GenericTuples.T2<String,String>> hmCoord = new HashMap<String, GenericTuples.T2<String,String>>();
			while (rs.next()){
				Indirizzo fp = new Indirizzo();
				
				fp.setCodNazionale(codEnt);
				fp.setFoglio(rs.getString("FOGLIO"));
				fp.setParticella(rs.getString("PARTICELLA"));
				
				String keyCoord = fp.getFoglio() + "|" + Utils.fillUpZeroInFront(fp.getParticella(),5) + "|" + fp.getCodNazionale();
				if (hmCoord.get(keyCoord) == null) {
					GenericTuples.T2<String,String> coord1 = null;
					try {
						coord1 = getLatitudeLongitude(fp.getFoglio(), Utils.fillUpZeroInFront(fp.getParticella(),5), fp.getCodNazionale());
					} catch (Exception e) {
					}
					hmCoord.put(keyCoord, coord1);
				}
				GenericTuples.T2<String,String> coord = hmCoord.get(keyCoord);
				if (coord!=null) {
					fp.setLatitudine(coord.firstObj);
					fp.setLongitudine(coord.secondObj);
				}
				if (fp.getFoglio() == null)
					fp.setFoglio("-");
				if (fp.getParticella() == null)
					fp.setParticella("-");
				fps.add(fp);
			}
			return fps;
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (rs!= null )
				rs.close();
			if (pst!= null )
				pst.close();		}
	}
    
    private String getIdImmobile(Connection conn, String foglio, String particella, String sub, java.sql.Date dtFornitura, String protocollo) throws Exception {
		String idImm=""; 
		PreparedStatement pst= null;
    	ResultSet rs =null;
		String sql=null;
		String codEnt=null;
		String infoDocfa="";
		boolean closeConn = false;
		try {
			if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}
			sql=SQL_DOCFA_ID_IMM;
 		    pst = conn.prepareStatement(sql);
 		    pst.setString(1, protocollo);
 		    pst.setDate(2, dtFornitura);
 		    pst.setString(3, fill("0", "sx", foglio, 4)) ;
			pst.setString(4, fill("0", "sx", particella, 5));
			pst.setString(5, fill("0", "sx", sub, 4));
 			rs = pst.executeQuery();
			if (rs.next()){
				idImm = rs.getString("ID_IMM");
			}
			return idImm;
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (rs!= null )
				rs.close();
			if (pst!= null )
			if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}
	}	
    
    public HSSFWorkbook mExportToXls(ResultSet[] arrRs, String[] nomiSheets) throws Exception{
    	HSSFWorkbook wb          = null;
    	//
    	String xlsTemplatePath=null;
    	boolean writeHeaders = true; 
    	//
    	boolean template = xlsTemplatePath != null && !xlsTemplatePath.trim().equals("");
		try {
			if (arrRs == null)
				return null;
			
			if (template) {
				wb = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(new File(xlsTemplatePath))));
			} else {
				wb = new HSSFWorkbook();
			}
			
			HSSFSheet sheet = null;
			HSSFRow row = null;
            
			for (short k = 0; k < arrRs.length; k++ ) {
				if (!hasPermission(k)) {
					continue;
				}
				String nomeSheet = "Foglio " + k; 
				if (nomiSheets != null && nomiSheets[k] != null && !nomiSheets[k].equals(""))
					nomeSheet = nomiSheets[k];
				sheet = wb.createSheet(nomeSheet);
				ResultSet rs = arrRs[k];
				ResultSetMetaData metadata = rs.getMetaData();
			    int conta = 0;
							
				if (writeHeaders) {
					row = sheet.createRow(conta);
					for (int i = 0; i < metadata.getColumnCount(); i++) {
						row.createCell((short) i).setCellValue(new HSSFRichTextString(metadata.getColumnName(i + 1)));
					}
					conta++;
				}	
				
				while (rs.next()) {
					try {
						row = sheet.createRow(conta);
			        	for (int i = 0; i < metadata.getColumnCount(); i++) {		        		
			        		Object value = rs.getObject(i + 1);
			        		if (value == null)
			        			value = "";
			        		if (value instanceof java.util.Date) {
			        			row.createCell((short) i).setCellValue((java.util.Date)value);
			        		} else if (value instanceof Integer) {
			        			row.createCell((short) i).setCellValue(((Integer)value).doubleValue());
			        		} else if (value instanceof Double) {
			        			row.createCell((short) i).setCellValue(((Double)value).doubleValue());
			        		} else if (value instanceof BigDecimal) {
			        			row.createCell((short) i).setCellValue(((BigDecimal)value).doubleValue());
			        		//eventuali altri tipi
			        		} else {
			        			row.createCell((short) i).setCellValue(new HSSFRichTextString(value.toString()));
			        		}
			        	}					
			        	conta++;			
					} catch (Exception e) {
						log.error(e.getMessage(),e);
						throw e;
					}
				}
				
				for (int i = 0; i < metadata.getColumnCount(); i++) {
					sheet.autoSizeColumn((short)i);
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			
		}
		   	
    	return wb;
    }
    
    private boolean hasPermission(short i) {
    	EnvUtente eu = getEnvUtente();
    	if (i == 0)
    		return GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_RES, true);
    	if (i == 1)
    		return GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_UI_TIT, true);
    	if (i == 2)
    		return GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_UI_CONS, true);
    	return false;
    }
    
    //
    private void insertTAB_DATI(Connection conn, ArrayList<Catasto> listaCat,ArrayList<ConsistenzaUI> listaCons, String sessionId) throws Exception {
    	PreparedStatement pst=null;
    	boolean closeConn = false;
    	try {
	      	if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}
	    	deleteTAB_DATI(conn, sessionId);
			String sqlStr = "INSERT INTO TAB_DATI_FOR_XLS_UC111 VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(sqlStr);
	    	for (Catasto cat : listaCat) {
	    	    pst.setString(1, sessionId);
	    	    pst.setString(2, "1");
	    	    pst.setInt(3, Integer.parseInt(cat.getFoglio()));
	    	    pst.setString(4, cat.getMappale());
	    	    pst.setInt(5, Integer.parseInt(cat.getSub()));
	    	    pst.setString(6, cat.getCategoria());
	    	    pst.setString(7, cat.getClasse());
	    	    pst.setDouble(8, cat.getRendita());
	    	    pst.setString(9, cat.getTitolare().getCodFisc());
	    	    pst.setString(10, cat.getTitolare().getDenom());
	    	    pst.setString(11,cat.getIndirizzo().getIndirizzoCompleto());
	    	    pst.setString(12, null);
	    	    pst.setString(13, null);
	    	    pst.setString(14, null);
	    	    pst.setString(15, null);
	    	    pst.setString(16, null);
	    	    pst.setString(17, null);
	    	    pst.setString(18, cat.getTitolare().getTipoTitolo());
	    	    pst.setString(19, null);
	      	    pst.execute();
	    	}
	    	for (ConsistenzaUI consUi : listaCons) {
	    	    pst.setString(1, sessionId);
	    	    pst.setString(2, "2");
	    	    pst.setInt(3, Integer.parseInt(consUi.getFoglio()));
	    	    pst.setString(4, consUi.getMappale());
	    	    pst.setInt(5, Integer.parseInt(consUi.getSub()));
	    	    pst.setString(6, null);
	    	    pst.setString(7, null);
	    	    pst.setString(8, null);
	    	    pst.setString(9, null);
	    	    pst.setString(10, null);
	    	    pst.setString(11, null);
	    	    pst.setDouble(12, consUi.getSupTarsu());
	    	    pst.setString(13, consUi.getDicTarsuCFPi());
	    	    pst.setDouble(14, consUi.getConsistenza());
	    	    pst.setDouble(15, consUi.getSuperficie());
	    	    pst.setDouble(16, consUi.getSupC340());
	    	    pst.setString(17, consUi.getInfoDocfa());
	    	    pst.setString(18, null);
	    	    pst.setString(19, consUi.getDicTarsuDenominazione());
	      	    pst.execute();
	    	}
	    	pst.close();
    	}catch (Exception exc) {
    		log.error(exc.getMessage(),exc);
    		throw exc;
    	}
    	finally {
    		if (pst != null)
    			pst.close();
    		if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
    	}	
    }
    
    private void deleteTAB_DATI(Connection conn, String sessionId) throws Exception {
    	PreparedStatement pst=null;
    	boolean closeConn = false;
    	try {
	      	if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}	
	    	String sqlStr = "DELETE FROM TAB_DATI_FOR_XLS_UC111 WHERE ID_SESSIONE = ?";
	    	pst= conn.prepareStatement(sqlStr);
			pst.setString(1, sessionId);
	    	pst.execute();
			pst.close();
    	}catch (Exception exc) {
    		log.error(exc.getMessage(),exc);
	   		throw exc;
    	}
    	finally {
    		if (pst != null)
    			pst.close();
    		if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
    	}	
    }
    
    private String componiInfoDocfa(java.sql.Date dtFornitura, String prot, String fmt) throws Exception{
    	String infoDocfa="";
    	int anno = annoData(dtFornitura.toString(), fmt);
		int mese =  meseData(dtFornitura.toString(), fmt);
		mese++; 
		String meseData="";
		if (mese <10)
			meseData = "0" + mese;
		else
			meseData = "" +mese;
		infoDocfa = prot + "-" + meseData + "/" + anno;
    	try {
    		
    	}catch(Exception e) {
    		log.error(e.getMessage(),e);
    		throw e;
    	}
    	return infoDocfa;
    }
    
  //todo VEDI SE SPOSTARE IN CLASSE Utils
	public String fill(String filler, String pos, String str, int len){
		String retVal = new String(str);
		while (retVal.length() < len) {
			if (pos.equals("sx"))
				retVal = filler + retVal;
			else if  (pos.equals("dx"))
				retVal = retVal+retVal;
			
		}
		return retVal;
	}
	
	public char firstCharNotNumericIn(String str) {
		for (char car : str.toCharArray()) {
			if ("1234567890".indexOf("" + car) == -1) {
				return car;
			}
		}
		return '\0';
	}
    
    public int annoData(String sData, String fmt){
		SimpleDateFormat df = new SimpleDateFormat(fmt);
		Date date1  = null;
		Date date2  = null;
		try {
			date1 = df.parse(sData); 
		}catch(java.text.ParseException pe){
			
		}
		Calendar cal1 = null; 
		cal1=Calendar.getInstance(); 
		cal1.setTime(date1);  
		int anno = cal1.get(Calendar.YEAR);
		return anno;
	}
	public int meseData(String sData, String fmt){
		SimpleDateFormat df = new SimpleDateFormat(fmt);
		Date date1  = null;
		Date date2  = null;
		try {
			date1 = df.parse(sData); 
		}catch(java.text.ParseException pe){
			
		}
		Calendar cal1 = null; 
		cal1=Calendar.getInstance(); 
		cal1.setTime(date1);  
		int anno = cal1.get(Calendar.MONTH);
		return anno;
	}
	
	public static String eliminaZeriSx(String str) {
		String retVal = new String(str);
		retVal= str;
		int lun=str.length()-1;
		for (int i= 0; i< lun; i++) {
			if (str.charAt(i) == '0') {
				retVal = str.substring(i+1) ;
			}else
				break;
		}
		return retVal;
	}
	
	
	

  
		
}
