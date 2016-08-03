package it.escsolution.escwebgis.concessioni.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.concessioni.bean.Provenienza;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioni;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniPersona;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniFinder;
import it.escsolution.escwebgis.concessioni.bean.SuapFileLink;
import it.escsolution.escwebgis.concessioni.bean.TipoSoggetto;
import it.escsolution.escwebgis.docfa.logic.DocfaLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.utils.StringUtils;

public class StoricoConcessioniLogic extends EscLogic {
	private String appoggioDataSource;

	public StoricoConcessioniLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	private static final SimpleDateFormat SDF_DATA = new SimpleDateFormat("dd/MM/yyyy");

	public static final String FINDER = "FINDER";
	public static final String CONCESSIONI = StoricoConcessioniLogic.class.getName() + "@CONCESSIONIKEY";
	public static final String LISTA = "LISTA_STORICOCONCESSIONI";
	public static final String DATI_CONCESSIONE = "DATI_STORICOCONCESSIONI";
	public static final String DATI_TECNICI = "DATI_TECNICI_STORICOCONCESSIONI";
	public static final String LISTA_SOGGETTI = "LISTA_SOGGETTI_STORICOCONCESSIONI";
	public static final String LISTA_DATI_CATASTALI = "LISTA_DATI_CATASTALI_STORICOCONCESSIONI";
	public static final String LISTA_INDIRIZZI = "LISTA_INDIRIZZI_STORICOCONCESSIONI";
	public static final String IDSTORICI_CONCESSIONI = "IDSTORICI_STORICOCONCESSIONI";
	
	private static final String NOME_FILE_RICEVUTA_SUAP_LCASE = "suap-ricevuta.pdf";

	private final static String SQL_SELECT_LISTA = "SELECT ROWNUM N, SEL.* FROM ("
			+ "SELECT DISTINCT SIT_C_CONCESSIONI.*, "
			+ "TO_CHAR(PROTOCOLLO_DATA, 'YYYY') AS PROTOCOLLO_ANNO "
			+ "FROM SIT_C_CONCESSIONI, "
			+ "SIT_C_CONC_PERSONA, "
			+ "SIT_C_PERSONA, "
			+ "SIT_C_CONC_INDIRIZZI, "
			+ "SIT_C_CONCESSIONI_CATASTO "
			+ "WHERE 1=? "
			+ "AND (SIT_C_CONCESSIONI.DT_FINE_VAL IS NULL OR SIT_C_CONCESSIONI.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) "
			+ "AND SIT_C_CONC_PERSONA.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT "
			+ "AND SIT_C_PERSONA.ID_EXT (+) = SIT_C_CONC_PERSONA.ID_EXT_C_PERSONA "
			+ "AND (SIT_C_PERSONA.DT_FINE_VAL IS NULL OR SIT_C_PERSONA.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) "
			+ "AND SIT_C_CONC_INDIRIZZI.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT "
			+ "AND SIT_C_CONCESSIONI_CATASTO.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT";

	private final static String SQL_SELECT_LISTA_OGG_CAT = "SELECT ROWNUM N, SEL.* FROM ("
		+ "SELECT DISTINCT SIT_C_CONCESSIONI.*, "
		+ "TO_CHAR(PROTOCOLLO_DATA, 'YYYY') AS PROTOCOLLO_ANNO "
		+ "FROM SIT_C_CONCESSIONI, "
		+ "SIT_C_CONC_PERSONA, "
		+ "SIT_C_PERSONA, "
		+ "SIT_C_CONC_INDIRIZZI, "
		+ "SIT_C_CONCESSIONI_CATASTO"
		+ " WHERE SIT_C_CONCESSIONI_CATASTO.ID=? "// Filippo Mazzini - modificata, era " WHERE SIT_C_CONCESSIONI_CATASTO.ID=?"... verificare...
		+ " AND SIT_C_CONCESSIONI_CATASTO.ID_EXT_C_CONCESSIONI (+)= SIT_C_CONCESSIONI.ID_EXT "
		+ " AND (SIT_C_CONCESSIONI.DT_FINE_VAL IS NULL OR SIT_C_CONCESSIONI.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) "
		+ " AND SIT_C_CONC_PERSONA.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT "
		+ " AND SIT_C_PERSONA.ID_EXT (+) = SIT_C_CONC_PERSONA.ID_EXT_C_PERSONA "
		+ " AND (SIT_C_PERSONA.DT_FINE_VAL IS NULL OR SIT_C_PERSONA.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) "
		+ " AND SIT_C_CONC_INDIRIZZI.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT "
		+ ") SEL";
	
	private final static String SQL_SELECT_LISTA_OGG_CONC = "SELECT ROWNUM N, SEL.* FROM ("
			+ "SELECT DISTINCT SIT_C_CONCESSIONI.*, "
			+ "TO_CHAR(PROTOCOLLO_DATA, 'YYYY') AS PROTOCOLLO_ANNO "
			+ "FROM SIT_C_CONCESSIONI, "
			+ "SIT_C_CONC_PERSONA, "
			+ "SIT_C_PERSONA, "
			+ "SIT_C_CONC_INDIRIZZI, "
			+ "SIT_C_CONCESSIONI_CATASTO"
			+ " WHERE SIT_C_CONCESSIONI.ID=? "// Filippo Mazzini - modificata, era " WHERE SIT_C_CONCESSIONI_CATASTO.ID=?"... verificare...
			+ " AND SIT_C_CONCESSIONI_CATASTO.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT "
			+ " AND (SIT_C_CONCESSIONI.DT_FINE_VAL IS NULL OR SIT_C_CONCESSIONI.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) "
			+ " AND SIT_C_CONC_PERSONA.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT "
			+ " AND SIT_C_PERSONA.ID_EXT (+) = SIT_C_CONC_PERSONA.ID_EXT_C_PERSONA "
			+ " AND (SIT_C_PERSONA.DT_FINE_VAL IS NULL OR SIT_C_PERSONA.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) "
			+ " AND SIT_C_CONC_INDIRIZZI.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT "
			+ ") SEL";
	
	private final static String SQL_SELECT_COUNT_LISTA = "SELECT COUNT(*) AS CONTEGGIO FROM ("
			+ "SELECT DISTINCT SIT_C_CONCESSIONI.*"
			+ "FROM SIT_C_CONCESSIONI, "
			+ "SIT_C_CONC_PERSONA, "
			+ "SIT_C_PERSONA, "
			+ "SIT_C_CONC_INDIRIZZI, "
			+ "SIT_C_CONCESSIONI_CATASTO "
			+ "WHERE 1=? "
			+ "AND (SIT_C_CONCESSIONI.DT_FINE_VAL IS NULL OR SIT_C_CONCESSIONI.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) "
			+ "AND SIT_C_CONC_PERSONA.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT "
			+ "AND SIT_C_PERSONA.ID_EXT (+) = SIT_C_CONC_PERSONA.ID_EXT_C_PERSONA "
			+ "AND (SIT_C_PERSONA.DT_FINE_VAL IS NULL OR SIT_C_PERSONA.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) "
			+ "AND SIT_C_CONC_INDIRIZZI.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT "
			+ "AND SIT_C_CONCESSIONI_CATASTO.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT";

	private final static String SQL_SELECT_DETTAGLIO = "SELECT *"
			+ " FROM SIT_C_CONCESSIONI" + " WHERE 1=?" + " AND ID=?";

	private final static String SQL_SELECT_ID_STORICI = "SELECT ID, DT_INIZIO_VAL, DT_FINE_VAL FROM SIT_C_CONCESSIONI C "
			+ "WHERE EXISTS (SELECT ID_EXT FROM SIT_C_CONCESSIONI CC WHERE ID = ? "
			+ "AND C.ID_EXT = CC.ID_EXT) ORDER BY DT_FINE_VAL NULLS FIRST";
	
	private final static String SQL_SELECT_DATI_TECNICI = "SELECT * "
			+ "FROM SIT_C_CONC_DATI_TEC "
			+ "WHERE 1=? "
			+ "AND SIT_C_CONC_DATI_TEC.ID_EXT_C_CONCESSIONI = ? "
			+ "AND (SIT_C_CONC_DATI_TEC.DT_FINE_VAL IS NULL OR SIT_C_CONC_DATI_TEC.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) ";

	private final static String SQL_SELECT_LISTA_SOGGETTI = "SELECT SIT_C_CONC_PERSONA.TITOLO AS TITOLO_CONC, SIT_C_PERSONA.* "
			+ "FROM SIT_C_CONC_PERSONA, SIT_C_PERSONA "
			+ "WHERE 1=? "
			+ "AND SIT_C_CONC_PERSONA.ID_EXT_C_CONCESSIONI = ? "
			+ "AND SIT_C_PERSONA.ID_EXT = SIT_C_CONC_PERSONA.ID_EXT_C_PERSONA "
			+ "AND (SIT_C_PERSONA.DT_FINE_VAL IS NULL OR SIT_C_PERSONA.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) "
			+ "ORDER BY SIT_C_CONC_PERSONA.TITOLO, SIT_C_PERSONA.DENOMINAZIONE";

	private final static String SQL_SELECT_LISTA_DATI_CATASTALI = "SELECT * "
			+ "FROM SIT_C_CONCESSIONI_CATASTO " + "WHERE 1=? "
			+ "AND ID_EXT_C_CONCESSIONI = ?";

	private final static String SQL_SELECT_LISTA_DATI_CATASTALI_INDIRIZZO = "SELECT * "
			+ "FROM SIT_C_CONCESSIONI_CATASTO "
			+ "WHERE 1=? "
			+ "AND ID_EXT_C_CONC_INDIRIZZI = ?";

	private final static String SQL_SELECT_LISTA_INDIRIZZI = "SELECT * "
			+ "FROM SIT_C_CONC_INDIRIZZI " + "WHERE 1=? "
			+ "AND ID_EXT_C_CONCESSIONI = ?";

	private final static String SQL_SELECT_LISTA_INDIRIZZI_DATO = "SELECT * "
			+ "FROM SIT_C_CONC_INDIRIZZI "
			+ "WHERE 1=? "
			+ "AND ID_EXT = ?"
			+ "AND (DT_FINE_VAL IS NULL OR DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy'))";
	
	private final static String SQL_SELECT_LISTA_OGGETTI = "SELECT PK_IMMAGINE, NOME_IMMAGINE FROM sit_c_concessioni_img" +
			" WHERE id_ext = ?";
	
	
	private final static String SQL_SELECT_LISTA_FROM_SOGGETTO="SELECT * FROM (SELECT ROWNUM N, SEL.* FROM" +
			" (SELECT DISTINCT SIT_C_CONCESSIONI.*, TO_CHAR(PROTOCOLLO_DATA, 'YYYY') AS PROTOCOLLO_ANNO" +
			" FROM SIT_C_CONCESSIONI, SIT_C_CONC_PERSONA, SIT_C_PERSONA, SIT_C_CONC_INDIRIZZI, SIT_C_CONCESSIONI_CATASTO " +
			"WHERE 1=? AND (SIT_C_CONCESSIONI.DT_FINE_VAL IS NULL OR SIT_C_CONCESSIONI.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) " +
			"AND SIT_C_CONC_PERSONA.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT " +
			"AND SIT_C_PERSONA.ID_EXT (+) = SIT_C_CONC_PERSONA.ID_EXT_C_PERSONA " +
			"AND (SIT_C_PERSONA.DT_FINE_VAL IS NULL OR SIT_C_PERSONA.DT_FINE_VAL >= TO_DATE(?, 'dd/MM/yyyy')) " +
			"AND SIT_C_CONC_INDIRIZZI.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT " +
			"AND SIT_C_CONCESSIONI_CATASTO.ID_EXT_C_CONCESSIONI (+) = SIT_C_CONCESSIONI.ID_EXT " +
			"AND SIT_C_PERSONA.ID = ? " +
			"order by LPAD(NVL(PROGRESSIVO_ANNO, '0'), 4, '0'), LPAD(NVL(PROGRESSIVO_NUMERO, NVL(CONCESSIONE_NUMERO, '0')), 15, '0')) SEL) ";

	public Vector getListaTipiSoggetto() throws Exception {
		Vector vct = new Vector();
		String sql = "SELECT DISTINCT LTRIM(RTRIM(TITOLO)) AS TITOLO FROM SIT_C_CONC_PERSONA ORDER BY TITOLO";
		Statement stmt;
		Connection conn = null;
		try {
			vct.add(new TipoSoggetto("", "")); // item vuoto
			conn = this.getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				vct.add(new TipoSoggetto(rs.getString("TITOLO"), rs.getString("TITOLO")));
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		return vct;
	}

	public Hashtable mCaricareLista(Vector listaPar, StoricoConcessioniFinder finder) throws Exception {

		
		Vector listaParOr = new Vector();
		// e' null se provengo dal fascicolo soggetto
		if (listaPar!=null) {
			// pretratto listaPar per isolare i campi su cui costruire la OR
			for (int i = 0; i < listaPar.size(); i++) {
				if (listaPar.get(i) instanceof EscElementoFiltro) {
					EscElementoFiltro filtro = (EscElementoFiltro) listaPar.get(i);
					if (filtro.getCampoFiltrato().toUpperCase().equals("CODICE_FISCALE")
							|| filtro.getCampoFiltrato().toUpperCase().equals("PIVA")
							|| filtro.getCampoFiltrato().toUpperCase().equals("DENOMINAZIONE")) {
						listaParOr.add(filtro);
						listaPar.remove(i);
						i--;
					}
				}			
			}
		}

		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;

			for (int i = 0; i <= 1; i++) {
				// il primo ciclo faccio la count
				if (i == 0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				GregorianCalendar gc = new GregorianCalendar();
				GregorianCalendar gcOggi = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
				this.setString(indice, SDF_DATA.format(gcOggi.getTime()));
				indice++;
				this.setString(indice, SDF_DATA.format(gcOggi.getTime()));
				indice++;

				if (finder != null) {
					// il primo passaggio esegue la select count
					if (finder.getKeyStr().equals("")) {
						sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
						sql = elaboraFiltroMascheraRicercaOr(indice, listaParOr, listaPar);
					} else {
						// controllo provenienza chiamata
						String controllo = finder.getKeyStr();
						
						String chiavi = "";
						boolean soggfascicolo = false;
						
						if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
							soggfascicolo = true;
							String ente = controllo.substring(0,controllo.indexOf("|"));
							ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
							chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
							sql = sql + " and SIT_C_PERSONA.ID in (" +chiavi + ")" ;
							
							//	sql = sql + " and  ENTE_S.CODENT = '" +ente + "'" ;
						}else
							sql = sql + " and SIT_C_PERSONA.ID in (" +finder.getKeyStr() + ")" ;
					}
				} else {
					String foglio = ((String)listaPar.get(0)).trim();
					String particella = ((String)listaPar.get(1)).trim();
					if (!foglio.equals("")) {
						foglio = "" + Integer.parseInt(foglio); // per il problema degli zeri iniziali
						sql += " AND FOGLIO = '" + foglio + "'";
					}
					if (!particella.equals("")) {
						particella = "" + Integer.parseInt(particella); // per il problema degli zeri iniziali
						sql += " AND lpad(PARTICELLA,5,'0') = '" + StringUtils.padding(particella, 5, '0', true)+"'";
					}
				}

				if (finder == null) {
					if (i == 1) {
						sql = "SELECT * FROM(" + sql;
						sql += " order by LPAD(NVL(PROGRESSIVO_ANNO, '0'), 4, '0'), LPAD(NVL(PROGRESSIVO_NUMERO, NVL(CONCESSIONE_NUMERO, '0')), 15, '0')) SEL)";
					} else {
						sql += ")";
					}
				} else {
					long limInf, limSup;
					limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
					limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

					if (i == 1) {
						sql = "SELECT * FROM(" + sql;
						sql += " order by LPAD(NVL(PROGRESSIVO_ANNO, '0'), 4, '0'), LPAD(NVL(PROGRESSIVO_NUMERO, NVL(CONCESSIONE_NUMERO, '0')), 15, '0')) SEL)";
						sql += " where N > " + limInf + " and N <=" + limSup;
					} else {
						sql += ")";
					}
				}

				prepareStatement(sql);

				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1) {
					while (rs.next()) {
						// campi della lista
						StoricoConcessioni con = new StoricoConcessioni();
						con.setId(rs.getString("ID"));
						con.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
						con.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
						con.setProgressivoNumero(rs.getObject("PROGRESSIVO_NUMERO") == null ? "" : rs.getString("PROGRESSIVO_NUMERO"));
						con.setProgressivoAnno(rs.getObject("PROGRESSIVO_ANNO") == null ? "" : rs.getString("PROGRESSIVO_ANNO"));
						con.setConcessioneNumero(rs.getObject("CONCESSIONE_NUMERO") == null ? "" : rs.getString("CONCESSIONE_NUMERO"));
						con.setOggetto(rs.getObject("OGGETTO") == null ? "" : rs.getString("OGGETTO"));
						con.setSoggetti(getSoggettiAsHtmlString(conn, con.getIdExt()));
						con.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));
						vct.add(con);
					}
				} else {
					if (rs.next()) {
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(StoricoConcessioniLogic.LISTA, vct);

			if (finder != null) {
				finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
				// pagine totali
				finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
				finder.setTotaleRecord(conteggione);
				finder.setRighePerPagina(RIGHE_PER_PAGINA);

				ht.put(StoricoConcessioniLogic.FINDER, finder);
			}
			
			// rimetto i campi su cui stata fatta la OR in listaPar per eventuali utilizzi successivi
			// (es. paginazione lista)
			for (int i = 0; i < listaParOr.size(); i++) {
				EscElementoFiltro filtro = (EscElementoFiltro) listaParOr.get(i);
				listaPar.add(filtro);
			}
			
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
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	
	public Hashtable mCaricareListaOgg(String oggettoSel, String tipo) throws Exception {
		
		log.debug("---------------------------------------->"+tipo);
		if("CONC".equalsIgnoreCase(tipo))
			return this.mCaricareListaOggById(oggettoSel, this.SQL_SELECT_LISTA_OGG_CONC);
		else
			return this.mCaricareListaOggById(oggettoSel, this.SQL_SELECT_LISTA_OGG_CAT);
	}
	
	
	private Hashtable mCaricareListaOggById(String oggettoSel, String sql) throws Exception {

		
		Vector listaParOr = new Vector();
		// e' null se provengo dal fascicolo soggetto


		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		//sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;

			
				
				//sql = sql;
				
				log.debug(sql);
				log.debug("param: " + oggettoSel);

				indice = 1;
				this.initialize();
				this.setString(1,oggettoSel);
			

				GregorianCalendar gc = new GregorianCalendar();
				GregorianCalendar gcOggi = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
				this.setString(2, SDF_DATA.format(gcOggi.getTime()));
				this.setString(3, SDF_DATA.format(gcOggi.getTime()));
				
				prepareStatement(sql);

				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				while (rs.next()) {
					// campi della lista
					StoricoConcessioni con = new StoricoConcessioni();
					con.setId(rs.getString("ID"));
					con.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
					con.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
					con.setProgressivoNumero(rs.getObject("PROGRESSIVO_NUMERO") == null ? "" : rs.getString("PROGRESSIVO_NUMERO"));
					con.setProgressivoAnno(rs.getObject("PROGRESSIVO_ANNO") == null ? "" : rs.getString("PROGRESSIVO_ANNO"));
					con.setOggetto(rs.getObject("OGGETTO") == null ? "" : rs.getString("OGGETTO"));
					con.setSoggetti(getSoggettiAsHtmlString(conn, con.getIdExt()));
					con.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));
					vct.add(con);
				}
			

			ht.put(StoricoConcessioniLogic.LISTA, vct);

			ht.put(StoricoConcessioniLogic.FINDER, new StoricoConcessioniFinder());
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = oggettoSel;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}	
	
	public Hashtable mCaricareListaCollegate(String conId, String foglio, String particella, String subalterno, String codEnte) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = this.getConnection();

			foglio = foglio == null ? "" : foglio;
			particella = particella == null ? "" : particella;
			subalterno = subalterno == null ? "" : subalterno;
			
			boolean continua = true;			
			for (char car : foglio.toCharArray()) {
				if ("1234567890,-/".indexOf("" + car) == -1) {
					continua = false;
					break;
				}
			}					
			if (continua) {
				for (char car : particella.toCharArray()) {
					if ("1234567890,-/".indexOf("" + car) == -1) {
						continua = false;
						break;
					}
				}
			}
			if (continua) {
				for (char car : subalterno.toCharArray()) {
					if ("1234567890,-/".indexOf("" + car) == -1) {
						continua = false;
						break;
					}
				}
			}
			if (!continua) {
				ht.put(StoricoConcessioniLogic.LISTA, vct);
				return ht;
			}
			
			foglio = foglio.replace("/", " ").replace(",", " ").replace("-", " ");
			while (foglio.indexOf("  ") > -1) {
				foglio = foglio.replace("  ", " ");
			}
			foglio = foglio.trim();
			particella = particella.replace("/", " ").replace(",", " ").replace("-", " ");
			while (particella.indexOf("  ") > -1) {
				particella = particella.replace("  ", " ");
			}
			particella = particella.trim();
			if (foglio.equals("") || particella.equals("")) {
				ht.put(StoricoConcessioniLogic.LISTA, vct);
				return ht;
			}
			subalterno = subalterno.replace("/", " ").replace(",", " ").replace("-", " ");
			while (subalterno.indexOf("  ") > -1) {
				subalterno = subalterno.replace("  ", " ");
			}
			subalterno = subalterno.trim();
			String[] arrFoglio = foglio.split(" ");
			if (arrFoglio.length > 1) {
				ht.put(StoricoConcessioniLogic.LISTA, vct);
				return ht;
			}
			String[] arrParticella = particella.split(" ");
			if (arrParticella.length > 1) {
				ht.put(StoricoConcessioniLogic.LISTA, vct);
				return ht;
			}
			if (!subalterno.equals("")) {
				String[] arrSubalterno = subalterno.split(" ");
				if (arrSubalterno.length > 1) {
					ht.put(StoricoConcessioniLogic.LISTA, vct);
					return ht;
				}
			}

			while (foglio.length() < 4) {
				foglio = "0" + foglio;
			}
			while (particella.length() < 5) {
				particella = "0" + particella;
			}
			while (subalterno.length() < 4) {
				subalterno = "0" + subalterno;
			}
			
			//verifico se la particella (o il subalterno)  cessata a catasto
			String sql = "SELECT sitiuiu.ide_muta_fine FROM sitiuiu, siticomu "
					+ "WHERE siticomu.codi_fisc_luna = ? "
					+ "and sitiuiu.cod_nazionale = siticomu.cod_nazionale "
					+ "and sitiuiu.foglio = ? "
					+ "and sitiuiu.particella = ? "
					+ "and sitiuiu.sub = ' ' "
					+ "and sitiuiu.unimm = ? "
					+ "and ((data_inizio_val < sysdate and data_fine_val > sysdate) "
					+ "or not exists "
					+ "(SELECT 1 FROM sitiuiu b where b.pkid_uiu <> sitiuiu.pkid_uiu "
					+ "and b.cod_nazionale = sitiuiu.cod_nazionale "
					+ "and b.foglio = sitiuiu.foglio "
					+ "and b.particella = sitiuiu.particella "
					+ "and b.sub = sitiuiu.sub "
					+ "and b.unimm = sitiuiu.unimm "
					+ "and (b.data_inizio_val < sysdate and b.data_fine_val > sysdate))) "
					+ "and sitiuiu.ide_muta_fine is not null";
			pstmt = conn.prepareStatement(sql);
			int indice = 0;
			pstmt.setString(++indice, codEnte);
			pstmt.setInt(++indice, Integer.parseInt(foglio));
			pstmt.setString(++indice, particella);
			if (subalterno == null || (subalterno.trim()).equals("") || subalterno.equals("-"))
				pstmt.setInt(++indice, 0);
			else
				pstmt.setInt(++indice, Integer.parseInt(subalterno));

			ResultSet rs = pstmt.executeQuery();
			boolean cessataCatasto = false;
			int ideMutaFine = -1;
			while (rs.next()) {
				cessataCatasto = true;
				ideMutaFine = rs.getInt("ide_muta_fine");
			}
			rs.close();
			
			//leggo le concessioni collegate
			int foglioColl = -1;
			String particellaColl = "";
			int ideMuta = -1;
			if (!cessataCatasto) {
				sql = "SELECT sitiuiu.ide_muta_ini FROM sitiuiu, siticomu "
					+ "WHERE siticomu.codi_fisc_luna = ? "
					+ "and sitiuiu.cod_nazionale = siticomu.cod_nazionale "
					+ "and sitiuiu.foglio = ? "
					+ "and sitiuiu.particella = ? "
					+ "and sitiuiu.sub = ' ' "
					+ "and sitiuiu.unimm = ? "
					+ "and (data_inizio_val < sysdate and data_fine_val > sysdate) "
					+ "and sitiuiu.ide_muta_fine is null";
				pstmt = conn.prepareStatement(sql);
				indice = 0;
				pstmt.setString(++indice, codEnte);
				pstmt.setInt(++indice, Integer.parseInt(foglio));
				pstmt.setString(++indice, particella);
				if (subalterno == null || (subalterno.trim()).equals("") || subalterno.equals("-"))
					pstmt.setInt(++indice, 0);
				else
					pstmt.setInt(++indice, Integer.parseInt(subalterno));

				rs = pstmt.executeQuery();
				while (rs.next()) {
					ideMuta = rs.getInt("ide_muta_ini");
				}
				rs.close();
			} else {
				ideMuta = ideMutaFine;
			}
			sql = "SELECT * FROM sitiuiu, siticomu "
				+ "WHERE siticomu.codi_fisc_luna = ? "
				+ "and sitiuiu.cod_nazionale = siticomu.cod_nazionale ";
			if (!cessataCatasto) {
				sql += "and sitiuiu.ide_muta_fine = ? ";
			} else {
				sql += "and (sitiuiu.ide_muta_ini = ? and sitiuiu.ide_muta_fine is null) ";
			}
			pstmt = conn.prepareStatement(sql);
			indice = 0;
			pstmt.setString(++indice, codEnte);
			pstmt.setInt(++indice, ideMuta);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				foglioColl = rs.getInt("FOGLIO");
				particellaColl = rs.getString("PARTICELLA");
			}
			rs.close();
			
			if (foglioColl != -1 && !particellaColl.trim().equals("")) {
				sql = SQL_SELECT_LISTA;
				sql += " AND FOGLIO = '" + foglioColl + "'";
				particellaColl = "" + Integer.parseInt(particellaColl); // per il problema degli zeri iniziali
				sql += " AND PARTICELLA = '" + particellaColl + "'";
				sql += " AND SIT_C_CONCESSIONI.ID <> '" + conId + "'";
				sql += ") SEL";
				pstmt = conn.prepareStatement(sql);
				indice = 0;
				pstmt.setInt(++indice, 1);
				GregorianCalendar gc = new GregorianCalendar();
				GregorianCalendar gcOggi = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
				pstmt.setString(++indice, SDF_DATA.format(gcOggi.getTime()));
				pstmt.setString(++indice, SDF_DATA.format(gcOggi.getTime()));			
				rs = pstmt.executeQuery();
				while (rs.next()) {
					// campi della lista
					StoricoConcessioni con = new StoricoConcessioni();
					con.setId(rs.getString("ID"));
					con.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
					con.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
					con.setProgressivoNumero(rs.getObject("PROGRESSIVO_NUMERO") == null ? "" : rs.getString("PROGRESSIVO_NUMERO"));
					con.setProgressivoAnno(rs.getObject("PROGRESSIVO_ANNO") == null ? "" : rs.getString("PROGRESSIVO_ANNO"));
					con.setOggetto(rs.getObject("OGGETTO") == null ? "" : rs.getString("OGGETTO"));
					con.setSoggetti(getSoggettiAsHtmlString(conn, con.getIdExt()));
					con.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));
					vct.add(con);
				}
			}
			
			ht.put(StoricoConcessioniLogic.LISTA, vct);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = conId;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			if (pstmt != null)
				pstmt.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	
	public Hashtable mCaricareListaFromSoggetto(String idSoggetto) throws Exception {
		
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		
		sql = SQL_SELECT_LISTA_FROM_SOGGETTO;
		
		Connection conn = null;

		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;

			
				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;
				GregorianCalendar gc = new GregorianCalendar();
				GregorianCalendar gcOggi = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
				this.setString(indice, SDF_DATA.format(gcOggi.getTime()));
				indice++;
				this.setString(indice, SDF_DATA.format(gcOggi.getTime()));
				indice++;
				this.setString(indice,idSoggetto);
				prepareStatement(sql);

				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				
					while (rs.next()) {
						StoricoConcessioni con = new StoricoConcessioni();
						con.setId(rs.getString("ID"));
						con.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
						con.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
						con.setProgressivoNumero(rs.getObject("PROGRESSIVO_NUMERO") == null ? "" : rs.getString("PROGRESSIVO_NUMERO"));
						con.setProgressivoAnno(rs.getObject("PROGRESSIVO_ANNO") == null ? "" : rs.getString("PROGRESSIVO_ANNO"));
						con.setOggetto(rs.getObject("OGGETTO") == null ? "" : rs.getString("OGGETTO"));
						con.setSoggetti(getSoggettiAsHtmlString(conn, con.getIdExt()));
						con.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));
						vct.add(con);
					}
				
					ht.put(StoricoConcessioniLogic.LISTA, vct);

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
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	

	public Hashtable mCaricareDettaglio(String chiave, String pathDatiDiogene) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		PreparedStatement pstmtDocfa = null;
		PreparedStatement pstmtVerificaCatasto = null;
		PreparedStatement pstmtVerificaCessataECollegate = null;
		PreparedStatement pstmtIndirizzi = null;
		ResultSet rsDocfa = null;
		try {

			conn = this.getConnection();
			this.initialize();
			String sql = SQL_SELECT_DETTAGLIO;

			int indice = 1;
			this.setInt(indice,1);
			indice++;
			this.setString(indice,chiave);
			indice++;		

			StoricoConcessioni con = new StoricoConcessioni();
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			boolean trovato = false;
				
			if (rs.next()){
				trovato = true;
				con.setId(rs.getString("ID"));
				con.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
				con.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
				con.setCartellaSuap(getCartellaSuap(getEnvUtente().getEnte(),
													rs.getObject("FK_ENTE_SORGENTE") == null ? null : rs.getString("FK_ENTE_SORGENTE"), 
													con.getIdOrig()));
				con.setConcessioneNumero(rs.getObject("CONCESSIONE_NUMERO") == null ? "" : rs.getString("CONCESSIONE_NUMERO"));
				con.setProgressivoNumero(rs.getObject("PROGRESSIVO_NUMERO") == null ? "" : rs.getString("PROGRESSIVO_NUMERO"));
				con.setProgressivoAnno(rs.getObject("PROGRESSIVO_ANNO") == null ? "" : rs.getString("PROGRESSIVO_ANNO"));
				con.setProtocolloData(rs.getObject("PROTOCOLLO_DATA") == null ? "" : SDF_DATA.format(rs.getDate("PROTOCOLLO_DATA")));
				con.setProtocolloNumero(rs.getObject("PROTOCOLLO_NUMERO") == null ? "" : rs.getString("PROTOCOLLO_NUMERO"));
				con.setTipoIntervento(rs.getObject("TIPO_INTERVENTO") == null ? "" : rs.getString("TIPO_INTERVENTO"));
				con.setOggetto(rs.getObject("OGGETTO") == null ? "" : rs.getString("OGGETTO"));				
				con.setProcedimento(rs.getObject("PROCEDIMENTO") == null ? "" : rs.getString("PROCEDIMENTO"));
				con.setZona(rs.getObject("ZONA") == null ? "" : rs.getString("ZONA"));				
				con.setDataRilascio(rs.getObject("DATA_RILASCIO") == null ? "" : SDF_DATA.format(rs.getDate("DATA_RILASCIO")));
				con.setDataInizioLavori(rs.getObject("DATA_INIZIO_LAVORI") == null ? "" : SDF_DATA.format(rs.getDate("DATA_INIZIO_LAVORI")));
				con.setDataFineLavori(rs.getObject("DATA_FINE_LAVORI") == null ? "" : SDF_DATA.format(rs.getDate("DATA_FINE_LAVORI")));
				con.setDataProrogaLavori(rs.getObject("DATA_PROROGA_LAVORI") == null ? "" : SDF_DATA.format(rs.getDate("DATA_PROROGA_LAVORI")));
				con.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));				
				con.setEsito(rs.getObject("ESITO") == null ? "" : rs.getString("ESITO"));
				con.setPosizioneCodice(rs.getObject("POSIZIONE_CODICE") == null ? "" : rs.getString("POSIZIONE_CODICE"));
				con.setPosizioneDescrizione(rs.getObject("POSIZIONE_DESCRIZIONE") == null ? "" : rs.getString("POSIZIONE_DESCRIZIONE"));
				con.setPosizioneData(rs.getObject("POSIZIONE_DATA") == null ? "" : SDF_DATA.format(rs.getDate("POSIZIONE_DATA")));
				
				
			}
			
			HashMap<String, String> datiTecnici = new HashMap<String, String>();
			ArrayList<StoricoConcessioniPersona> soggetti = new ArrayList<StoricoConcessioniPersona>();
			ArrayList<StoricoConcessioniCatasto> datiCatastali = new ArrayList<StoricoConcessioniCatasto>();
			ArrayList<StoricoConcessioniIndirizzo> indirizzi = new ArrayList<StoricoConcessioniIndirizzo>();
			if (trovato) {
				GregorianCalendar gc = new GregorianCalendar();
				GregorianCalendar gcOggi = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));				
				//dati tecnici
				DecimalFormat df = new DecimalFormat();
				DecimalFormatSymbols dfs = new DecimalFormatSymbols();
				dfs.setDecimalSeparator(',');
				dfs.setGroupingSeparator('.');
				df.setDecimalFormatSymbols(dfs);
				this.initialize();
				sql = SQL_SELECT_DATI_TECNICI;
				indice = 1;
				this.setInt(indice,1);
				indice++;
				this.setString(indice, con.getIdExt());
				indice++;				
				this.setString(indice, SDF_DATA.format(gcOggi.getTime()));
				indice ++;
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next()) {
					datiTecnici.put("DEST_USO", rs.getObject("DEST_USO") == null ? "" : rs.getString("DEST_USO"));
					String supEffLotto = "";
					if (rs.getObject("SUP_EFF_LOTTO") != null) {
						supEffLotto = df.format(rs.getDouble("SUP_EFF_LOTTO"));
					}
					datiTecnici.put("SUP_EFF_LOTTO", supEffLotto);
					String supCoperta = "";
					if (rs.getObject("SUP_COPERTA") != null) {
						supCoperta = df.format(rs.getDouble("SUP_COPERTA"));
					}
					datiTecnici.put("SUP_COPERTA", supCoperta);
					String volTotale = "";
					if (rs.getObject("VOL_TOTALE") != null) {
						volTotale = df.format(rs.getDouble("VOL_TOTALE"));
					}
					datiTecnici.put("VOL_TOTALE", volTotale);
					String vani = "";
					if (rs.getObject("VANI") != null) {
						vani = "" + rs.getInt("VANI");
					}
					datiTecnici.put("VANI", vani);
					String abitazioni = "";
					if (rs.getObject("NUM_ABITAZIONI") != null) {
						abitazioni = "" + rs.getInt("NUM_ABITAZIONI");
					}
					datiTecnici.put("NUM_ABITAZIONI", abitazioni);					
					String dtAgibilita = "";
					if (rs.getObject("DT_AGIBILITA") != null) {						
						dtAgibilita = SDF_DATA.format(rs.getDate("DT_AGIBILITA"));
					}
					datiTecnici.put("DT_AGIBILITA", dtAgibilita);
				}
				// lista soggetti
				this.initialize();
				sql = SQL_SELECT_LISTA_SOGGETTI;
				sql = "SELECT DISTINCT ID, ID_EXT, ID_ORIG, CODICE_FISCALE, PIVA, CF_DITTA, PI_DITTA, " +
						"COGNOME, NOME, TRIM(TITOLO || ' ' || DENOMINAZIONE) AS DENOMINAZIONE, RAGSOC_DITTA, DATA_NASCITA, COMUNE_NASCITA, TITOLO_CONC FROM (" +
						sql +
						") ORDER BY TITOLO_CONC, DENOMINAZIONE"; //aggiunta per casi di duplicazioni comune di Milano
				indice = 1;
				this.setInt(indice,1);
				indice++;
				this.setString(indice, con.getIdExt());
				indice++;				
				this.setString(indice, SDF_DATA.format(gcOggi.getTime()));
				indice ++;
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next()) {
					StoricoConcessioniPersona soggetto = new StoricoConcessioniPersona();
					soggetto.setId(rs.getString("ID"));
					soggetto.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
					soggetto.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
					String codiceFiscale = rs.getObject("CODICE_FISCALE") == null ? "" : rs.getString("CODICE_FISCALE");
					if (codiceFiscale == null || codiceFiscale.equals("")) {
						codiceFiscale = rs.getObject("PIVA") == null ? "" : rs.getString("PIVA");
					}
					codiceFiscale = rs.getObject("CF_DITTA") != null ?  rs.getString("CF_DITTA"): codiceFiscale ;
					codiceFiscale = rs.getObject("PI_DITTA") != null ?  rs.getString("PI_DITTA"): codiceFiscale ;
					
					soggetto.setCodiceFiscale(codiceFiscale);
					soggetto.setCognome(rs.getObject("COGNOME") == null ? "" : rs.getString("COGNOME"));
					soggetto.setNome(rs.getObject("NOME") == null ? "" : rs.getString("NOME"));
					String denominazione = rs.getObject("DENOMINAZIONE") == null ? "" : rs.getString("DENOMINAZIONE");
					
					// se c'è la ragione sociale la preferisco alla denominazione del soggetto
					denominazione = rs.getObject("RAGSOC_DITTA") != null ? rs.getString("RAGSOC_DITTA") : denominazione;
					
					soggetto.setDenominazione(denominazione);					
					soggetto.setDataNascita(rs.getObject("DATA_NASCITA") == null ? "" : SDF_DATA.format(rs.getDate("DATA_NASCITA")));
					soggetto.setComuneNascita(rs.getObject("COMUNE_NASCITA") == null ? "" : rs.getString("COMUNE_NASCITA"));
					soggetto.setTitolo(rs.getObject("TITOLO_CONC") == null ? "" : rs.getString("TITOLO_CONC"));
					soggetti.add(soggetto);
				}
				// lista dati catastali
				this.initialize();
				sql = SQL_SELECT_LISTA_DATI_CATASTALI;
				indice = 1;
				this.setInt(indice,1);
				indice++;
				this.setString(indice, con.getIdExt());
				indice++;
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next()) {
					StoricoConcessioniCatasto dato = new StoricoConcessioniCatasto();
					dato.setId(rs.getString("ID"));
					dato.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
					dato.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
					String codEnte = "";
					ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
					while (rsEnte.next()) {
						codEnte = rsEnte.getString("codent");
					}
					dato.setCodEnt(codEnte);
					dato.setFoglio(rs.getObject("FOGLIO") == null ? "" : rs.getString("FOGLIO"));
					dato.setParticella(rs.getObject("PARTICELLA") == null ? "" : rs.getString("PARTICELLA"));
					dato.setSubalterno(rs.getObject("SUBALTERNO") == null ? "" : rs.getString("SUBALTERNO"));
					dato.setTipo(rs.getObject("TIPO") == null ? "" : rs.getString("TIPO"));					
					dato.setSezione(rs.getObject("SEZIONE") == null ? "" : rs.getString("SEZIONE"));
					
					boolean controllaDocfa = true;
					String foglio = dato.getFoglio();
					String particella = dato.getParticella();
					String subalterno = dato.getSubalterno();
					for (char car : foglio.toCharArray()) {
						if ("1234567890,-/".indexOf("" + car) == -1) {
							controllaDocfa = false;
							break;
						}
					}					
					if (controllaDocfa) {
						for (char car : particella.toCharArray()) {
							if ("1234567890,-/".indexOf("" + car) == -1) {
								controllaDocfa = false;
								break;
							}
						}
					}
					if (controllaDocfa) {
						for (char car : subalterno.toCharArray()) {
							if ("1234567890,-/".indexOf("" + car) == -1) {
								controllaDocfa = false;
								break;
							}
						}
					}
					if (controllaDocfa) {
						rsDocfa = getDocfa(conn, pstmtDocfa, foglio, particella, subalterno);
						dato.setDocfa(rsDocfa != null && rsDocfa.next());
					} else {
						dato.setDocfa(false);
					}
					
					setVerificaCatasto(conn, pstmtVerificaCatasto, dato);
					setVerificaCessataECollegate(conn, pstmtVerificaCessataECollegate, dato, con.getId());					
					StoricoConcessioniIndirizzo indirizzo = new StoricoConcessioniIndirizzo();
					indirizzo.setIdExt(rs.getObject("ID_EXT_C_CONC_INDIRIZZI") == null ? null : rs.getString("ID_EXT_C_CONC_INDIRIZZI"));
					dato.setIndirizzo(indirizzo);
					datiCatastali.add(dato);
				}
				// lista indirizzi
				this.initialize();
				sql = SQL_SELECT_LISTA_INDIRIZZI;
				indice = 1;
				this.setInt(indice,1);
				indice++;
				this.setString(indice, con.getIdExt());
				indice++;
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next()) {
					StoricoConcessioniIndirizzo indirizzo = new StoricoConcessioniIndirizzo();
					indirizzo.setId(rs.getString("ID"));
					indirizzo.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
					indirizzo.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
					indirizzo.setSedime(rs.getObject("SEDIME") == null ? "" : rs.getString("SEDIME"));
					indirizzo.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "" : rs.getString("INDIRIZZO"));
					indirizzo.setCivLiv1(rs.getObject("CIV_LIV1") == null ? "" : rs.getString("CIV_LIV1"));
					indirizzi.add(indirizzo);
				}
				
				// per ogni indirizzo recupero i dati catastali direttamente collegati 
				// tramite il campo ID_EXT_C_CONC_INDIRIZZI della tabella SIT_C_CONCESSIONI_CATASTO
				for (StoricoConcessioniIndirizzo indirizzo : indirizzi) {
					this.initialize();
					sql = SQL_SELECT_LISTA_DATI_CATASTALI_INDIRIZZO;
					indice = 1;
					this.setInt(indice,1);
					indice++;
					this.setString(indice, indirizzo.getIdExt());
					indice++;
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					boolean trovatoCatasto = false;
					while (rs.next()) {
						trovatoCatasto = true;
						StoricoConcessioniCatasto dato = new StoricoConcessioniCatasto();
						dato.setId(rs.getString("ID"));
						dato.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
						dato.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
						String codEnte = "";
						ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
						while (rsEnte.next()) {
							codEnte = rsEnte.getString("codent");
						}
						dato.setCodEnt(codEnte);
						dato.setFoglio(rs.getObject("FOGLIO") == null ? "" : rs.getString("FOGLIO"));
						dato.setParticella(rs.getObject("PARTICELLA") == null ? "" : rs.getString("PARTICELLA"));
						dato.setSubalterno(rs.getObject("SUBALTERNO") == null ? "" : rs.getString("SUBALTERNO"));
						dato.setTipo(rs.getObject("TIPO") == null ? "" : rs.getString("TIPO"));					
						dato.setSezione(rs.getObject("SEZIONE") == null ? "" : rs.getString("SEZIONE"));
						
						boolean controllaDocfa = true;
						String foglio = dato.getFoglio();
						String particella = dato.getParticella();
						String subalterno = dato.getSubalterno();
						for (char car : foglio.toCharArray()) {
							if ("1234567890,-/".indexOf("" + car) == -1) {
								controllaDocfa = false;
								break;
							}
						}					
						if (controllaDocfa) {
							for (char car : particella.toCharArray()) {
								if ("1234567890,-/".indexOf("" + car) == -1) {
									controllaDocfa = false;
									break;
								}
							}
						}
						if (controllaDocfa) {
							for (char car : subalterno.toCharArray()) {
								if ("1234567890,-/".indexOf("" + car) == -1) {
									controllaDocfa = false;
									break;
								}
							}
						}
						if (controllaDocfa) {
							rsDocfa = getDocfa(conn, pstmtDocfa, foglio, particella, subalterno);							
							dato.setDocfa(rsDocfa != null && rsDocfa.next());
						} else {
							dato.setDocfa(false);
						}					
						
						setVerificaCatasto(conn, pstmtVerificaCatasto, dato);
						setVerificaCessataECollegate(conn, pstmtVerificaCessataECollegate, dato, con.getId());
						indirizzo.addDatiCatastali(dato);
						setIndirizzoCatastaleViarioRif(conn, pstmtIndirizzi, indirizzo, dato);
					}
					if (!trovatoCatasto) {
						for (StoricoConcessioniCatasto dato : datiCatastali) {
							setIndirizzoCatastaleViarioRif(conn, pstmtIndirizzi, indirizzo, dato);
						}
					}
					if (indirizzo.getIndirizzoCatastale() == null || indirizzo.getIndirizzoCatastale().trim().equals("")) {
						indirizzo.setIndirizzoCatastale("non trovato");
					}
					if (indirizzo.getIndirizzoViarioRif() == null || indirizzo.getIndirizzoViarioRif().trim().equals("")) {
						indirizzo.setIndirizzoViarioRif("non trovato");
					}
				}
				
				// analogamente, per ogni dato catastale recupero i dati
				// dell'indirizzo eventualmente associato
				for (StoricoConcessioniCatasto dato : datiCatastali) {
					StoricoConcessioniIndirizzo indirizzo = dato.getIndirizzo();
					if (indirizzo.getIdExt() != null) {
						this.initialize();
						sql = SQL_SELECT_LISTA_INDIRIZZI_DATO;
						indice = 1;
						this.setInt(indice,1);
						indice++;
						this.setString(indice, indirizzo.getIdExt());
						indice++;
						gc = new GregorianCalendar();
						gcOggi = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
						this.setString(indice, SDF_DATA.format(gcOggi.getTime()));
						indice ++;
						prepareStatement(sql);
						rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						boolean trovatoIndirizzo = false;
						while (rs.next()) {	
							trovatoIndirizzo = true;
							indirizzo.setId(rs.getString("ID"));
							indirizzo.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
							indirizzo.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
							indirizzo.setSedime(rs.getObject("SEDIME") == null ? "" : rs.getString("SEDIME"));
							indirizzo.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "" : rs.getString("INDIRIZZO"));
							indirizzo.setCivLiv1(rs.getObject("CIV_LIV1") == null ? "" : rs.getString("CIV_LIV1"));
							setIndirizzoCatastaleViarioRif(conn, pstmtIndirizzi, indirizzo, dato);
						}
						if (!trovatoIndirizzo) {
							setIndirizzoCatastaleViarioRif(conn, pstmtIndirizzi, indirizzo, dato);
						}
						if (indirizzo.getIndirizzoCatastale() == null || indirizzo.getIndirizzoCatastale().trim().equals("")) {
							indirizzo.setIndirizzoCatastale("non trovato");
						}
						if (indirizzo.getIndirizzoViarioRif() == null || indirizzo.getIndirizzoViarioRif().trim().equals("")) {
							indirizzo.setIndirizzoViarioRif("non trovato");
						}
					}					
				}
				
				//oggetti
				if (con.getProgressivoAnno() != null &&
				!con.getProgressivoAnno().equals("") &&
				con.getProgressivoNumero() != null &&
				!con.getProgressivoNumero().equals("")) {
					try {
						sql = SQL_SELECT_LISTA_OGGETTI;
						
						this.initialize();
						this.setString(1,  con.getIdExt()  );
						prepareStatement(sql);
						rs = executeQuery(conn, this.getClass().getName(), envUtente);
						HashMap<String, String> tiff = new HashMap<String, String>();
						while (rs.next())
						{
							tiff.put(tornaValoreRS(rs,"PK_IMMAGINE"), tornaValoreRS(rs,"NOME_IMMAGINE"));
						}
						con.setTiff(tiff);
					} catch (Exception e) {
						//caso in cui ad es. non esiste la tabella MI_CONCESSIONI_IMMAGINE (comuni diversi da Milano)
						//non deve fare nulla
					}					
				}
			}

			ht.put(StoricoConcessioniLogic.DATI_CONCESSIONE, con);
			ht.put(StoricoConcessioniLogic.DATI_TECNICI, datiTecnici);
			ht.put(StoricoConcessioniLogic.LISTA_SOGGETTI, soggetti);
			ht.put(StoricoConcessioniLogic.LISTA_DATI_CATASTALI, datiCatastali);
			ht.put(StoricoConcessioniLogic.LISTA_INDIRIZZI, indirizzi);
			
			// id storici
			HashMap idStorici = caricaIdStorici(chiave);
			ht.put(StoricoConcessioniLogic.IDSTORICI_CONCESSIONI, idStorici);
			
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
			if (rsDocfa != null)
				rsDocfa.close();
			if (pstmtDocfa != null)
				pstmtDocfa.close();
			if (pstmtVerificaCatasto != null)
				pstmtVerificaCatasto.close();
			if (pstmtVerificaCessataECollegate != null)
				pstmtVerificaCessataECollegate.close();
			if (pstmtIndirizzi != null)
				pstmtIndirizzi.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}

	public String[] mCaricareDatoImg(String chiave) 
	throws Exception
	{
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		// faccio la connessione al db
		Connection conn = null;
		String[] val = new String[2];
		try
		{
		
			//Dati generali
			conn = this.getConnection();
			sql="select NOME_IMMAGINE, PATH_IMMAGINE from SIT_C_CONCESSIONI_IMG where  PK_IMMAGINE = ?";
			this.initialize();
			this.setString(1, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs.next())
			{
				
				val[0] = tornaValoreRS(rs,"PATH_IMMAGINE");
				val[1] = tornaValoreRS(rs,"NOME_IMMAGINE");
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
		return val;
	}
	
	public HashMap caricaIdStorici(String oggettoSel) throws Exception {
		Connection conn = null;
		java.sql.ResultSet rs = null;
		HashMap ht = new LinkedHashMap();
		try {
			conn = this.getConnection();
			this.initialize();
			String sql = SQL_SELECT_ID_STORICI;
			this.setString(1, oggettoSel);

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()) {
				String dtIni = it.webred.utils.DateFormat.dateToString(rs.getDate("DT_INIZIO_VAL"), "dd/MM/yyyy");
				String dtFine = it.webred.utils.DateFormat.dateToString(rs.getDate("DT_FINE_VAL"), "dd/MM/yyyy");
				ht.put(rs.getString("ID"), dtIni + " - " + dtFine);
			}
			return ht;
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
	}

	public ResultSet getDocfa(Connection conn, PreparedStatement pstmt,
			String foglio, String particella, String subalterno)
			throws Exception {
		ResultSet rs = null;
		try {
			foglio = foglio == null ? "" : foglio;
			particella = particella == null ? "" : particella;
			subalterno = subalterno == null ? "" : subalterno;
			foglio = foglio.replace("/", " ").replace(",", " ").replace("-", " ");
			while (foglio.indexOf("  ") > -1) {
				foglio = foglio.replace("  ", " ");
			}
			foglio = foglio.trim();
			particella = particella.replace("/", " ").replace(",", " ").replace("-", " ");
			while (particella.indexOf("  ") > -1) {
				particella = particella.replace("  ", " ");
			}
			particella = particella.trim();
			if (foglio.equals("") || particella.equals("")) {
				return null;
			}
			subalterno = subalterno.replace("/", " ").replace(",", " ").replace("-", " ");
			while (subalterno.indexOf("  ") > -1) {
				subalterno = subalterno.replace("  ", " ");
			}
			subalterno = subalterno.trim();
			String[] arrFoglio = foglio.split(" ");
			String[] arrParticella = particella.split(" ");
			String sql = DocfaLogic.getSQL_SELECT_LISTA();
			String whereFoglio = " AND u.FOGLIO" + componiInString(arrFoglio, 4);
			String whereParticella = " AND u.NUMERO" + componiInString(arrParticella, 5);
			String whereSubalterno = "";
			if (!subalterno.equals("")) {
				String[] arrSubalterno = subalterno.split(" ");
				whereSubalterno = " AND u.SUBALTERNO" + componiInString(arrSubalterno, 4);
			}
			sql += whereFoglio + whereParticella + whereSubalterno;
			sql += " ORDER BY DATA_REGISTRAZIONE DESC, PROTOCOLLO, OPERAZIONE DESC))";
			pstmt = conn.prepareStatement(sql);
			int indice = 1;
			pstmt.setInt(indice, 1);
			rs = pstmt.executeQuery();
			return rs;
		} catch (Exception e) {
			throw e;
		}
	}

	private void setVerificaCatasto(Connection conn, PreparedStatement pstmt, StoricoConcessioniCatasto dato) throws Exception {
		try {
			String foglio = dato.getFoglio();
			String particella = dato.getParticella();
			String subalterno = dato.getSubalterno();
			foglio = foglio == null ? "" : foglio;
			particella = particella == null ? "" : particella;
			subalterno = subalterno == null ? "" : subalterno;
			
			boolean continua = true;			
			for (char car : foglio.toCharArray()) {
				if ("1234567890,-/".indexOf("" + car) == -1) {
					continua = false;
					break;
				}
			}					
			if (continua) {
				for (char car : particella.toCharArray()) {
					if ("1234567890,-/".indexOf("" + car) == -1) {
						continua = false;
						break;
					}
				}
			}
			if (continua) {
				for (char car : subalterno.toCharArray()) {
					if ("1234567890,-/".indexOf("" + car) == -1) {
						continua = false;
						break;
					}
				}
			}
			if (!continua) {
				dato.setAssenzaCatasto("Il formato particolare dei dati catastali non consente la verifica");
				return;
			}
			
			foglio = foglio.replace("/", " ").replace(",", " ").replace("-", " ");
			while (foglio.indexOf("  ") > -1) {
				foglio = foglio.replace("  ", " ");
			}
			foglio = foglio.trim();
			particella = particella.replace("/", " ").replace(",", " ").replace("-", " ");
			while (particella.indexOf("  ") > -1) {
				particella = particella.replace("  ", " ");
			}
			particella = particella.trim();
			if (foglio.equals("") || particella.equals("")) {
				dato.setAssenzaCatasto("Dati mancanti per foglio e/o particella");
				return;
			}
			subalterno = subalterno.replace("/", " ").replace(",", " ").replace("-", " ");
			while (subalterno.indexOf("  ") > -1) {
				subalterno = subalterno.replace("  ", " ");
			}
			subalterno = subalterno.trim();
			String[] arrFoglio = foglio.split(" ");
			if (arrFoglio.length > 1) {
				dato.setAssenzaCatasto("Foglio multiplo");
				return;
			}
			String[] arrParticella = particella.split(" ");
			if (arrParticella.length > 1) {
				dato.setAssenzaCatasto("Particella multipla");
				return;
			}
			if (!subalterno.equals("")) {
				String[] arrSubalterno = subalterno.split(" ");
				if (arrSubalterno.length > 1) {
					dato.setAssenzaCatasto("Subalterno multiplo");
					return;
				}
			}

			while (foglio.length() < 4) {
				foglio = "0" + foglio;
			}
			while (particella.length() < 5) {
				particella = "0" + particella;
			}
			while (subalterno.length() < 4) {
				subalterno = "0" + subalterno;
			}
			String sql = "SELECT microzona,  DATA_INIZIO_VAL, DATA_FINE_VAL FROM sitiuiu, siticomu "
					+ "WHERE siticomu.codi_fisc_luna = ? "
					+ "and sitiuiu.cod_nazionale = siticomu.cod_nazionale "
					+ "and sitiuiu.foglio = ? "
					+ "and sitiuiu.particella = ? "
					+ "and sitiuiu.sub = ' '"
					+ "and sitiuiu.unimm = ? order by  sitiuiu.DATA_FINE_VAL";
			pstmt = conn.prepareStatement(sql);
			int indice = 0;
			pstmt.setString(++indice, dato.getCodEnt());
			pstmt.setInt(++indice, Integer.parseInt(foglio));
			pstmt.setString(++indice, particella);
			if (subalterno == null || (subalterno.trim()).equals("") || subalterno.equals("-"))
				pstmt.setInt(++indice, 0);
			else
				pstmt.setInt(++indice, Integer.parseInt(subalterno));

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				dato.setAssenzaCatasto("");// stringa vuota == presente a catasto!
				dato.setDataValiditaCatasto(tornaValoreRS(rs, "DATA_FINE_VAL", "YYYY-MM-DD"));
				Date a = new Date();
				while (rs.next()) {
					if (rs.getDate("DATA_INIZIO_VAL").before(a) && rs.getDate("DATA_FINE_VAL").after(a))
						dato.setDataValiditaCatasto(tornaValoreRS(rs, "DATA_FINE_VAL", "YYYY-MM-DD"));
				}
			} else {
				// memorizzo la non presenza della microzona nello scarico del catasto
				dato.setAssenzaCatasto("Particella non presente nello scarico catastale!");
			}
			rs.close();

		} catch (Exception e) {
			throw e;
		}
	}
	
	private void setVerificaCessataECollegate(Connection conn, PreparedStatement pstmt, StoricoConcessioniCatasto dato, String conId) throws Exception {
		try {			
			String foglio = dato.getFoglio();
			String particella = dato.getParticella();
			String subalterno = dato.getSubalterno();
			foglio = foglio == null ? "" : foglio;
			particella = particella == null ? "" : particella;
			subalterno = subalterno == null ? "" : subalterno;
			
			boolean continua = true;			
			for (char car : foglio.toCharArray()) {
				if ("1234567890,-/".indexOf("" + car) == -1) {
					continua = false;
					break;
				}
			}					
			if (continua) {
				for (char car : particella.toCharArray()) {
					if ("1234567890,-/".indexOf("" + car) == -1) {
						continua = false;
						break;
					}
				}
			}
			if (continua) {
				for (char car : subalterno.toCharArray()) {
					if ("1234567890,-/".indexOf("" + car) == -1) {
						continua = false;
						break;
					}
				}
			}
			if (!continua) {
				dato.setCessataCatasto(false);
				dato.setConcCollegate(false);
				return;
			}
			
			foglio = foglio.replace("/", " ").replace(",", " ").replace("-", " ");
			while (foglio.indexOf("  ") > -1) {
				foglio = foglio.replace("  ", " ");
			}
			foglio = foglio.trim();
			particella = particella.replace("/", " ").replace(",", " ").replace("-", " ");
			while (particella.indexOf("  ") > -1) {
				particella = particella.replace("  ", " ");
			}
			particella = particella.trim();
			if (foglio.equals("") || particella.equals("")) {
				dato.setCessataCatasto(false);
				dato.setConcCollegate(false);
				return;
			}
			subalterno = subalterno.replace("/", " ").replace(",", " ").replace("-", " ");
			while (subalterno.indexOf("  ") > -1) {
				subalterno = subalterno.replace("  ", " ");
			}
			subalterno = subalterno.trim();
			String[] arrFoglio = foglio.split(" ");
			if (arrFoglio.length > 1) {
				dato.setCessataCatasto(false);
				dato.setConcCollegate(false);
				return;
			}
			String[] arrParticella = particella.split(" ");
			if (arrParticella.length > 1) {
				dato.setCessataCatasto(false);
				dato.setConcCollegate(false);
				return;
			}
			if (!subalterno.equals("")) {
				String[] arrSubalterno = subalterno.split(" ");
				if (arrSubalterno.length > 1) {
					dato.setCessataCatasto(false);
					dato.setConcCollegate(false);
					return;
				}
			}

			while (foglio.length() < 4) {
				foglio = "0" + foglio;
			}
			while (particella.length() < 5) {
				particella = "0" + particella;
			}
			while (subalterno.length() < 4) {
				subalterno = "0" + subalterno;
			}
			
			//verifico se la particella (o il subalterno) è cessata a catasto
			String sql = "SELECT sitiuiu.ide_muta_fine FROM sitiuiu, siticomu "
					+ "WHERE siticomu.codi_fisc_luna = ? "
					+ "and sitiuiu.cod_nazionale = siticomu.cod_nazionale "
					+ "and sitiuiu.foglio = ? "
					+ "and sitiuiu.particella = ? "
					+ "and sitiuiu.sub = ' ' "
					+ "and sitiuiu.unimm = ? "
					+ "and ((data_inizio_val < sysdate and data_fine_val > sysdate) "
					+ "or not exists "
					+ "(SELECT 1 FROM sitiuiu b where b.pkid_uiu <> sitiuiu.pkid_uiu "
					+ "and b.cod_nazionale = sitiuiu.cod_nazionale "
					+ "and b.foglio = sitiuiu.foglio "
					+ "and b.particella = sitiuiu.particella "
					+ "and b.sub = sitiuiu.sub "
					+ "and b.unimm = sitiuiu.unimm "
					+ "and (b.data_inizio_val < sysdate and b.data_fine_val > sysdate))) "
					+ "and sitiuiu.ide_muta_fine is not null";
			pstmt = conn.prepareStatement(sql);
			int indice = 0;
			pstmt.setString(++indice, dato.getCodEnt());
			pstmt.setInt(++indice, Integer.parseInt(foglio));
			pstmt.setString(++indice, particella);
			if (subalterno == null || (subalterno.trim()).equals("") || subalterno.equals("-"))
				pstmt.setInt(++indice, 0);
			else
				pstmt.setInt(++indice, Integer.parseInt(subalterno));

			ResultSet rs = pstmt.executeQuery();
			boolean cessataCatasto = false;
			int ideMutaFine = -1;
			while (rs.next()) {
				cessataCatasto = true;
				ideMutaFine = rs.getInt("ide_muta_fine");
			}
			dato.setCessataCatasto(cessataCatasto);
			rs.close();
			
			//verifico se esistono concessioni collegate
			boolean concCollegate = false;
			int foglioColl = -1;
			String particellaColl = "";
			int ideMuta = -1;
			if (!cessataCatasto) {
				sql = "SELECT sitiuiu.ide_muta_ini FROM sitiuiu, siticomu "
					+ "WHERE siticomu.codi_fisc_luna = ? "
					+ "and sitiuiu.cod_nazionale = siticomu.cod_nazionale "
					+ "and sitiuiu.foglio = ? "
					+ "and sitiuiu.particella = ? "
					+ "and sitiuiu.sub = ' ' "
					+ "and sitiuiu.unimm = ? "
					+ "and (data_inizio_val < sysdate and data_fine_val > sysdate) "
					+ "and sitiuiu.ide_muta_fine is null";
				pstmt = conn.prepareStatement(sql);
				indice = 0;
				pstmt.setString(++indice, dato.getCodEnt());
				pstmt.setInt(++indice, Integer.parseInt(foglio));
				pstmt.setString(++indice, particella);
				if (subalterno == null || (subalterno.trim()).equals("") || subalterno.equals("-"))
					pstmt.setInt(++indice, 0);
				else
					pstmt.setInt(++indice, Integer.parseInt(subalterno));

				rs = pstmt.executeQuery();
				while (rs.next()) {
					ideMuta = rs.getInt("ide_muta_ini");
				}
				rs.close();
			} else {
				ideMuta = ideMutaFine;
			}
			sql = "SELECT * FROM sitiuiu, siticomu "
				+ "WHERE siticomu.codi_fisc_luna = ? "
				+ "and sitiuiu.cod_nazionale = siticomu.cod_nazionale ";
			if (!cessataCatasto) {
				sql += "and sitiuiu.ide_muta_fine = ? ";
			} else {
				sql += "and (sitiuiu.ide_muta_ini = ? and sitiuiu.ide_muta_fine is null) ";
			}
			pstmt = conn.prepareStatement(sql);
			indice = 0;
			pstmt.setString(++indice, dato.getCodEnt());
			pstmt.setInt(++indice, ideMuta);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				foglioColl = rs.getInt("FOGLIO");
				particellaColl = rs.getString("PARTICELLA");
			}
			rs.close();
			
			if (foglioColl != -1 && !particellaColl.trim().equals("")) {
				sql = SQL_SELECT_LISTA;
				sql += " AND FOGLIO = '" + foglioColl + "'";
				particellaColl = "" + Integer.parseInt(particellaColl); // per il problema degli zeri iniziali
				sql += " AND PARTICELLA = '" + particellaColl + "'";
				sql += " AND SIT_C_CONCESSIONI.ID <> '" + conId + "'";
				sql += ") SEL";
				pstmt = conn.prepareStatement(sql);
				indice = 0;
				pstmt.setInt(++indice, 1);
				GregorianCalendar gc = new GregorianCalendar();
				GregorianCalendar gcOggi = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
				pstmt.setString(++indice, SDF_DATA.format(gcOggi.getTime()));
				pstmt.setString(++indice, SDF_DATA.format(gcOggi.getTime()));			
				rs = pstmt.executeQuery();
				concCollegate = rs.next();			
				dato.setConcCollegate(concCollegate);
			} else {
				dato.setConcCollegate(false);
			}			
			
		} catch (Exception e) {
			throw e;
		}
	}

	private String componiInString(String[] arr, int lungh) {
		String in = " IN(";
		int conta = 1;
		for (String str : arr) {
			while (str.length() < lungh) {
				str = "0" + str;
			}
			if (conta > 1) {
				in += ", ";
			}
			in += "'";
			char[] cars = str.toCharArray();
			for (char car : cars) {
				if (car == '\'') {
					in += "''";
				} else {
					in += car;
				}
			}
			in += "'";
			conta++;
		}
		in += ")";
		return in;
	}

	private void setIndirizzoCatastaleViarioRif(Connection conn,
												PreparedStatement pstmt, StoricoConcessioniIndirizzo indirizzo,
												StoricoConcessioniCatasto dato) throws Exception {
		try {
			String foglio = dato.getFoglio();
			String particella = dato.getParticella();
			String subalterno = dato.getSubalterno();
			foglio = foglio == null ? "" : foglio;
			particella = particella == null ? "" : particella;
			subalterno = subalterno == null ? "" : subalterno;
			foglio = foglio.replace("/", " ").replace(",", " ").replace("-", " ");
			while (foglio.indexOf("  ") > -1) {
				foglio = foglio.replace("  ", " ");
			}
			foglio = foglio.trim();
			particella = particella.replace("/", " ").replace(",", " ").replace("-", " ");
			while (particella.indexOf("  ") > -1) {
				particella = particella.replace("  ", " ");
			}
			particella = particella.trim();
			if (foglio.equals("") || particella.equals("")) {
				return;
			}
			subalterno = subalterno.replace("/", " ").replace(",", " ").replace("-", " ");
			while (subalterno.indexOf("  ") > -1) {
				subalterno = subalterno.replace("  ", " ");
			}
			subalterno = subalterno.trim();
			String[] arrFoglio = foglio.split(" ");
			String[] arrParticella = particella.split(" ");

			// indirizzo catastale
			String sql = "SELECT DISTINCT  t.descr || ' ' || ind.ind  || ' ' || ind.civ1 AS indirizzo "
					+ "           FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t "
					+ "          	WHERE c.codi_fisc_luna = ? "
					+ "            AND ID.codi_fisc_luna = c.codi_fisc_luna "
					+ "            AND ID.sez = c.sezione_censuaria "
					+ "            AND ind.codi_fisc_luna = c.codi_fisc_luna "
					+ "            AND ind.sezione = ID.sezione "
					+ "            AND ind.id_imm = ID.id_imm "
					+ "            AND ind.progressivo = ID.progressivo "
					+ "            AND t.pk_id = ind.toponimo ";
			String whereFoglio = " AND ID.foglio " + componiInString(arrFoglio, 4);
			String whereParticella = " AND ID.mappale " + componiInString(arrParticella, 5);
			String whereSubalterno = "";
			if (!subalterno.equals("")) {
				String[] arrSubalterno = subalterno.split(" ");
				whereSubalterno = " AND ID.sub " + componiInString(arrSubalterno, 4);
			}
			sql += whereFoglio + whereParticella + whereSubalterno;
			sql += " ORDER BY indirizzo";
			pstmt = conn.prepareStatement(sql);
			int indice = 0;
			pstmt.setString(++indice, dato.getCodEnt());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("indirizzo") != null)
					indirizzo.setIndirizzoCatastale(indirizzo.getIndirizzoCatastale() != null && !indirizzo.getIndirizzoCatastale().equals("") ? 
									indirizzo.getIndirizzoCatastale() + "<br>" + rs.getString("indirizzo") : 
									rs.getString("indirizzo"));
			}
			rs.close();

			// indirizzo viario
			sql = "SELECT DISTINCT S.PREFISSO || ' ' || S.NOME || ' ' || c.civico as indirizzo "
					+ "                  FROM sitiuiu u, siticivi_uiu iu, siticivi c, SITIDSTR S "
					+ "                 WHERE iu.pkid_uiu = u.pkid_uiu "
					+ "                   AND iu.pkid_civi = c.pkid_civi "
					+ "                   and c.pkid_stra=s.pkid_stra ";
			whereFoglio = " AND u.foglio " + componiInString(arrFoglio, 4);
			whereParticella = " AND u.particella " + componiInString(arrParticella, 5);
			whereSubalterno = "";
			if (!subalterno.equals("")) {
				String[] arrSubalterno = subalterno.split(" ");
				whereSubalterno = " AND u.unimm" + componiInString(arrSubalterno, 4);
			}
			sql += whereFoglio + whereParticella + whereSubalterno;
			sql += " ORDER BY indirizzo";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("indirizzo") != null)
					indirizzo.setIndirizzoViarioRif(indirizzo.getIndirizzoViarioRif() != null && !indirizzo.getIndirizzoViarioRif().equals("") ? 
									indirizzo.getIndirizzoViarioRif() + "<br>" + rs.getString("indirizzo") : 
									rs.getString("indirizzo"));
			}
			rs.close();

		} catch (Exception e) {
			throw e;
		}
	}

	private String elaboraFiltroMascheraRicercaOr(int indice, Vector listaPar, Vector listaAltriPar) throws NumberFormatException, Exception {
		// al momento implementata solo per campi stringa con = o like
		if (listaPar == null)
			return sql;
		Enumeration en = listaPar.elements();
		while (en.hasMoreElements()) {
			EscElementoFiltro filtro = (EscElementoFiltro) en.nextElement();
			if (filtro.getValore() != null && !filtro.getValore().trim().equals("")) {
				if (filtro.getOperatore().toLowerCase().trim().equals("like")) {
					if (filtro.getCampoFiltrato().toUpperCase().equals("CODICE_FISCALE")) {
						String valore = filtro.getValore().toUpperCase().trim();
						String codiceFiscale = "";
						for (int i = 0; i < valore.length(); i++) {
							if (valore.charAt(i) == '\'') {
								codiceFiscale += "''";
							} else {
								codiceFiscale += valore.charAt(i);
							}
						}
						sql += " and (UPPER(SIT_C_PERSONA.CODICE_FISCALE) LIKE '%"
								+ codiceFiscale + "%'"
								+ " OR UPPER(SIT_C_PERSONA.CF_DITTA) LIKE '%"
								+ codiceFiscale + "%'"
								+ " OR UPPER(SIT_C_PERSONA.PIVA) LIKE '%"
								+ codiceFiscale + "%'"
								+ " OR UPPER(SIT_C_PERSONA.PI_DITTA) LIKE '%"
								+ codiceFiscale + "%')";
					} else if (filtro.getCampoFiltrato().toUpperCase().equals("PIVA")) {
						String valore = filtro.getValore().toUpperCase().trim();
						String piva = "";
						for (int i = 0; i < valore.length(); i++) {
							if (valore.charAt(i) == '\'') {
								piva += "''";
							} else {
								piva += valore.charAt(i);
							}
						}
						sql += " and (UPPER(SIT_C_PERSONA.CODICE_FISCALE) LIKE '%"
								+ piva + "%'"
								+ " OR UPPER(SIT_C_PERSONA.CF_DITTA) LIKE '%"
								+ piva + "%'"
								+ " OR UPPER(SIT_C_PERSONA.PIVA) LIKE '%"
								+ piva + "%'"
								+ " OR UPPER(SIT_C_PERSONA.PI_DITTA) LIKE '%"
								+ piva + "%')";
					} else if (filtro.getCampoFiltrato().toUpperCase().equals("DENOMINAZIONE")) {
						String valore = filtro.getValore().toUpperCase().trim();
						String denominazione = "";
						for (int i = 0; i < valore.length(); i++) {
							if (valore.charAt(i) == '\'') {
								denominazione += "''";
							} else {
								denominazione += valore.charAt(i);
							}
						}
						sql += " and (UPPER(SIT_C_PERSONA.DENOMINAZIONE) LIKE '%"
								+ denominazione
								+ "%'"
								+ " OR UPPER(SIT_C_PERSONA.RAGSOC_DITTA) LIKE '%"
								+ denominazione + "%')";
					}
				} else {
					if (filtro.getCampoFiltrato().toUpperCase().equals("CODICE_FISCALE")) {
						String valore = filtro.getValore().toUpperCase().trim();
						String codiceFiscale = "";
						for (int i = 0; i < valore.length(); i++) {
							if (valore.charAt(i) == '\'') {
								codiceFiscale += "''";
							} else {
								codiceFiscale += valore.charAt(i);
							}
						}
						sql += " and (UPPER(SIT_C_PERSONA.CODICE_FISCALE) = '"
								+ codiceFiscale + "'"
								+ " OR UPPER(SIT_C_PERSONA.CF_DITTA) = '"
								+ codiceFiscale + "'"
								+ " OR UPPER(SIT_C_PERSONA.PIVA) = '"
								+ codiceFiscale + "'"
								+ " OR UPPER(SIT_C_PERSONA.PI_DITTA) = '"
								+ codiceFiscale + "')";
					} else if (filtro.getCampoFiltrato().toUpperCase().equals("PIVA")) {
						String valore = filtro.getValore().toUpperCase().trim();
						String piva = "";
						for (int i = 0; i < valore.length(); i++) {
							if (valore.charAt(i) == '\'') {
								piva += "''";
							} else {
								piva += valore.charAt(i);
							}
						}
						sql += " and (UPPER(SIT_C_PERSONA.CODICE_FISCALE) = '"
								+ piva + "'"
								+ " OR UPPER(SIT_C_PERSONA.CF_DITTA) = '"
								+ piva + "'"
								+ " OR UPPER(SIT_C_PERSONA.PIVA) = '" + piva
								+ "'" + " OR UPPER(SIT_C_PERSONA.PI_DITTA) = '"
								+ piva + "')";
					} else if (filtro.getCampoFiltrato().toUpperCase().equals("DENOMINAZIONE")) {
						String valore = filtro.getValore().toUpperCase().trim();
						String denominazione = "";
						for (int i = 0; i < valore.length(); i++) {
							if (valore.charAt(i) == '\'') {
								denominazione += "''";
							} else {
								denominazione += valore.charAt(i);
							}
						}
						sql += " and (UPPER(SIT_C_PERSONA.DENOMINAZIONE) = '"
								+ denominazione + "'"
								+ " OR UPPER(SIT_C_PERSONA.RAGSOC_DITTA) = '"
								+ denominazione + "')";
					}
				}
				indice++;
			}
		}
		
		//se si ricerca per il campo COGNOME, la ricerca deve essere fatta anche per DENOMINAZIONE comincia per...
		if (listaAltriPar == null)
			return sql;
		en = listaAltriPar.elements();
		while (en.hasMoreElements()) {
			EscElementoFiltro filtro = (EscElementoFiltro) en.nextElement();
			if (filtro.getValore() != null && !filtro.getValore().trim().equals("")) {
				if (filtro.getOperatore().toLowerCase().trim().equals("=") && filtro.getAttributeName().equals("COGNOME")) {
					String valore = filtro.getValore().toUpperCase().trim();
					String denominazione = "";
					for (int i = 0; i < valore.length(); i++) {
						if (valore.charAt(i) == '\'') {
							denominazione += "''";
						} else {
							denominazione += valore.charAt(i);
						}
					}
					sql += " and (UPPER(NVL(SIT_C_PERSONA.DENOMINAZIONE,SIT_C_PERSONA.COGNOME)) LIKE '"
						+ denominazione + "%'"
						+ " OR UPPER(NVL(SIT_C_PERSONA.RAGSOC_DITTA,SIT_C_PERSONA.COGNOME)) LIKE '"
						+ denominazione + "%')";
					break;
				}
			}
		}
		
		return sql;
	}
	
	private String getSoggettiAsHtmlString(Connection conn, String idExt) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String soggetti = "";
			String sql = SQL_SELECT_LISTA_SOGGETTI;
			sql = "SELECT DISTINCT TIPO_SOGGETTO, TITOLO_CONC, TRIM(TITOLO||' '||DENOMINAZIONE) denominazione, RAGSOC_DITTA, COGNOME, NOME FROM (" +
					sql +
					") ORDER BY TITOLO_CONC, DENOMINAZIONE,RAGSOC_DITTA"; //aggiunta per casi di duplicazioni comune di Milano
			pstmt = conn.prepareStatement(sql);
			int indice = 1;
			pstmt.setInt(indice,1);
			indice++;
			pstmt.setString(indice, idExt);
			indice++;
			GregorianCalendar gc = new GregorianCalendar();
			GregorianCalendar gcOggi = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
			pstmt.setString(indice, SDF_DATA.format(gcOggi.getTime()));
			indice++;
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (!soggetti.equals("")) {
					soggetti += "<br>";
				}
				String soggetto = "";
				soggetto = rs.getObject("TITOLO_CONC") == null ? "" : rs.getString("TITOLO_CONC");
				if (!soggetto.equals("")) {
					soggetto += ":";
				}
				
				String tipoSoggetto = rs.getString("TIPO_SOGGETTO");
				String cognome= rs.getString("COGNOME");
				String nome = rs.getString("NOME");
				String ragSoc = rs.getString("RAGSOC_DITTA");
				
				String denominazione = rs.getObject("DENOMINAZIONE") == null ? "" : rs.getString("DENOMINAZIONE");
				if((tipoSoggetto!=null && tipoSoggetto.equals("A"))||(tipoSoggetto==null && denominazione.equals("") && ragSoc!=null)){
					denominazione = ragSoc!=null ? ragSoc : (denominazione != null ? denominazione : "");
				}else{
					if (denominazione.equals("")) 
						denominazione = (cognome!=null ? cognome : "") + " " + (nome!=null ? nome : "");
				}
				
				denominazione= denominazione.trim().toUpperCase();
				
				if (denominazione.equals(""))
					denominazione = "-";
			
				if (!soggetto.equals("")) {
					soggetto += " ";
				}
				soggetto += denominazione;
				soggetti += soggetto;
			}
			return soggetti;
		} catch (Exception e) {
			throw e;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
	}
	
	public void download(HttpServletResponse response, String pathFile) throws Exception {
		try {
				pathFile = pathFile.replace("\\", "/").replace("/", File.separator);
				int myIndex = pathFile.trim().lastIndexOf(File.separator);
				String contentType = getMimeType(pathFile);
				response.setContentType(contentType);				
				response.setHeader("Content-Disposition", "inline; filename= \"" + pathFile.trim().substring(myIndex + 1) + "\"");
				InputStream in = new FileInputStream(new File(pathFile));
				int size = in.available();
				response.setContentLength(size);
				byte[] ab = new byte[size];
				OutputStream os = response.getOutputStream();
				log.warn("PER ALCUNI TIPI DI FILE, IN INTERNET EXPLORER SI POTREBBERO VERIFICARE ECCEZIONI NON BLOCCANTI");
				int bytesread = 0;
				while (bytesread > -1) {
					bytesread = in.read(ab, 0, size);
					if (bytesread > -1)
						os.write(ab, 0, bytesread);
				}				
				in.close();
				os.flush();
				os.close();				    
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private String getMimeType(String pathFile) {
	    String type = "text/plain"; //default
	    if (pathFile.toLowerCase().endsWith("pdf.p7m")) {
	    	return getMimeType(pathFile.replace("pdf.p7m", "pdf"));
	    }
	    try {
	    	URL u = new File(pathFile).toURI().toURL();
		    URLConnection uc = u.openConnection();
		    type = uc.getContentType();
	    } catch (Exception e) {
	    	log.warn(e.getMessage(),e);
	    }	    
	    return type;
	}
	
	public Vector<Provenienza> getListaProvenienze() {
		Vector<Provenienza> vct = new Vector<Provenienza>();
		vct.add(new Provenienza(""));
		String sql = "SELECT DISTINCT PROVENIENZA FROM SIT_C_CONCESSIONI ORDER BY PROVENIENZA";
		Statement stmt;
		Connection con = null;
		try {
			con = this.getDefaultConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				vct.add(new Provenienza(rs.getString("PROVENIENZA")));
			}
		}
		catch (SQLException e) {
			log.error(e.getMessage(),e);
		}
		catch (NamingException e) {
			log.error(e.getMessage(),e);
		}
		finally {
			try {
				if (con != null)
					con.close();
			}
			catch (Exception e) {
			}
		}
		return vct;
	}
	
	private String getCartellaSuap(String comune, String fonte, String idOrig) throws Exception {
		if (fonte == null || idOrig == null || !idOrig.toLowerCase().startsWith("suap/") || idOrig.indexOf("_") == -1) {
			return null;
		}
		int idx = -1;
		long id = -1;
		try {
			idx = Integer.parseInt(idOrig.substring(idOrig.indexOf("_") + 1)) - 1; //nell'idOrig è in base 1
			id = Long.parseLong(idOrig.substring("suap/".length(), idOrig.indexOf("_")));
			if (idx < 0) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setComune(comune);
		criteria.setFonte(fonte);
		criteria.setKey("dirs.files");
		Context cont = new InitialContext();		
		ParameterService parameterService = (ParameterService)getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
		AmKeyValueExt amKey = parameterService.getAmKeyValueExt(criteria);
		String dirsFiles = amKey.getValueConf();
		
		if (dirsFiles == null || dirsFiles.equals("")) {
			return null;
		}
		
		String[] arrDirsFiles = dirsFiles.split("\\|", -1);
		if (idx > arrDirsFiles.length - 1) {
			return null;
		}
		String dirFiles = arrDirsFiles[idx];
		File f = new File(dirFiles);			
		if(!f.exists() || !f.isDirectory()) {
			return null;
		}
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name)
			{
				boolean ok = false;
				File f = new File(dir, name);
				if (f.exists() && f.isDirectory() && name.indexOf("_") > -1) {
					String idStr = name.substring(0, name.indexOf("_"));
					long id = -1;
					try {
						id = Long.parseLong(idStr);
					} catch (Exception e) {}
					ok = (id > 0);
				}					
				return ok;
			}
		};
		String[] folderList = f.list(filter);
		String folder = null;
		for (String myFolder : folderList) {
			if (myFolder.startsWith(id + "_")) {
				folder = myFolder;
				break;
			}
		}
		if (folder == null) {
			return null;
		}
		
		if (!dirFiles.replace("\\", "/").replace("/", File.separator).endsWith(File.separator)) {
			dirFiles += File.separator;
		}
		return dirFiles + folder;
	}
	
	public static List<SuapFileLink> getSuapFileBrowser(String folder) {
		List<SuapFileLink> retVal = new ArrayList<SuapFileLink>();
		try {
			readSuapFolder(retVal, folder, 1, "suapLink-1");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return retVal;
	}
	
	private static void readSuapFolder(List<SuapFileLink> lst, String folder, int level, String id) throws Exception {
		if (folder == null || folder.equals("")) {
			return;
		}
		folder = folder.replace("\\", "/").replace("/", File.separator);
		File f = new File(folder);
		if(!f.exists() || !f.isDirectory()) {
			return;
		}
		SuapFileLink sfl = new SuapFileLink(id, SuapFileLink.FOLDER, level, folder, SuapFileLink.getLinkByPath(folder));
		lst.add(sfl);
		String[] fList = f.list();
		if (fList != null && fList.length > 0) {
			int idx = 0;
			level++;
			for (String fName : fList) {
				String path = folder + File.separator + fName;
				File pathF = new File(path);
				if (!pathF.exists()) {
					continue;
				}
				idx++;
				String newId = id + "-" + idx;
				if (pathF.isDirectory()) {
					readSuapFolder(lst, path, level, newId);
				} else {
					SuapFileLink sflFile = new SuapFileLink(newId, SuapFileLink.FILE, level, path, SuapFileLink.getLinkByPath(path));
					lst.add(sflFile);
				}
			}			
		}
	}

}