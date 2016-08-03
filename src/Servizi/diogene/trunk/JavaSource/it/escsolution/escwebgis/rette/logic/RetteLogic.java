package it.escsolution.escwebgis.rette.logic;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.acqua.bean.AcquaCatasto;
import it.escsolution.escwebgis.acqua.bean.AcquaFinder;
import it.escsolution.escwebgis.acqua.bean.AcquaUtente;
import it.escsolution.escwebgis.acqua.bean.AcquaUtenze;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.enel.bean.EnelBean2;
import it.escsolution.escwebgis.pratichePortale.bean.Pratica;
import it.escsolution.escwebgis.rette.bean.RetteFinder;
import it.escsolution.escwebgis.rette.bean.RttBollette;
import it.escsolution.escwebgis.rette.bean.RttDettaglio;
import it.escsolution.escwebgis.rette.bean.RttRate;
import it.escsolution.escwebgis.rette.bean.RttSoggetto;
import it.webred.utils.GenericTuples;

public class RetteLogic extends EscLogic {
	
	public final static String LISTA_RETTE = "LISTA_RETTE@RetteLogic";
	public final static String FINDER = "FINDER119";
	public final static String RETTA = "RETTA@RetteLogic";
	public final static String DETTAGLIO = "DETTAGLIO@RetteLogic";	
	public static final String RATE = "RATE@RetteLogic";
	public static final String SOGGETTO = "SOGGETTO@RetteLogic";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat SDF2 = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource;
	
	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DF.setDecimalFormatSymbols(dfs);
	}
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
	"select ROWNUM as N, Q.* from (" +
	"select DISTINCT * " +
	"from sit_rtt_bollette t where 1=? AND t.dt_fine_val is null";
	
	private final static String SQL_SELECT_COUNT_LISTA = "SELECT COUNT (*) AS conteggio FROM (select * from sit_rtt_bollette t WHERE 1 = ? AND t.dt_fine_val is null";
 
	private final static String SQL_SELECT_DETTAGLIO = "SELECT * FROM sit_rtt_bollette WHERE ID = ? AND dt_fine_val is null";
	
	private final static String SQL_SELECT_DETTAGLIO_BOLLETTA = "SELECT * FROM sit_rtt_dett_bollette WHERE cod_bolletta = ? AND dt_fine_val is null";
	
	private final static String SQL_SELECT_DETTAGLIO_RATA = "SELECT * FROM sit_rtt_rate_bollette WHERE cod_bolletta = ? AND dt_fine_val is null";
	
	private final static String SQL_SELECT_DETTAGLIO_SOGGETTO = "SELECT * FROM sit_rtt_sogg_bollette WHERE cod_soggetto = ? AND dt_fine_val is null";
	
	private final static String SQL_SELECT_LISTA_FROM_SOGG = "SELECT b.* FROM sit_rtt_bollette b, sit_rtt_sogg_bollette s WHERE s.ID = ?" +
			" AND s.cod_soggetto = b.cod_soggetto AND b.dt_fine_val is null AND s.dt_fine_val is null order by b.data_bolletta";

	
	private static final String SQL_SELECT_ANNO = "SELECT DISTINCT ANNO FROM SIT_RTT_BOLLETTE WHERE 1=? AND dt_fine_val is null ORDER BY ANNO";
	
	
	public RetteLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	private RttBollette getOggettoRttBollette(ResultSet rs, Connection conn) throws Exception{
		
		RttBollette m = new RttBollette();
		
		m.setId(rs.getString("ID") == null ? "-" : rs.getString("ID"));
		m.setCodSoggetto(rs.getString("COD_SOGGETTO") == null ? "-" : rs.getString("COD_SOGGETTO"));
		m.setDesIntestatario(rs.getString("DES_INTESTATARIO") == null ? "-" : rs.getString("DES_INTESTATARIO"));
		m.setCodiceFiscale(rs.getString("CODICE_FISCALE") == null ? "-" : rs.getString("CODICE_FISCALE"));
		m.setIndirizzo(rs.getString("INDIRIZZO") == null ? "-" : rs.getString("INDIRIZZO"));
		m.setRecapito(rs.getString("RECAPITO") == null ? "-" : rs.getString("RECAPITO"));
		m.setCodBolletta(rs.getString("COD_BOLLETTA") == null ? "-" : rs.getString("COD_BOLLETTA"));
		m.setAnno(rs.getObject("ANNO") == null ? "-" : rs.getBigDecimal("ANNO").toString());
		m.setCodServizio(rs.getString("COD_SERVIZIO") == null ? "-" : rs.getString("COD_SERVIZIO"));
		m.setIdServizio(rs.getBigDecimal("ID_SERVIZIO") == null ? "-" : rs.getBigDecimal("ID_SERVIZIO").toString());
		m.setNumBolletta(rs.getString("NUM_BOLLETTA") == null ? "-" : rs.getString("NUM_BOLLETTA"));
		m.setNumRate(rs.getBigDecimal("NUM_RATE") == null ? "-" : rs.getBigDecimal("NUM_RATE").toString());
		m.setDataBolletta(rs.getDate("DATA_BOLLETTA") == null ? "-" : SDF2.format(rs.getDate("DATA_BOLLETTA")));
		m.setOggetto(rs.getString("OGGETTO") == null ? "-" : rs.getString("OGGETTO"));
		m.setSpeseSpedizione(rs.getBigDecimal("SPESE_SPEDIZIONE") == null ? "-" : rs.getBigDecimal("SPESE_SPEDIZIONE").toString());
		m.setTotEsenteIva(rs.getBigDecimal("TOT_ESENTE_IVA") == null ? "-" : rs.getBigDecimal("TOT_ESENTE_IVA").toString());
		m.setTotImponibileIva(rs.getBigDecimal("TOT_IMPONIBILE_IVA") == null ? "-" : rs.getBigDecimal("TOT_IMPONIBILE_IVA").toString());
		m.setTotIva(rs.getBigDecimal("TOT_IVA") == null ? "-" : rs.getBigDecimal("TOT_IVA").toString());
		m.setArrotondamentoPrec(rs.getBigDecimal("ARROTONDAMENTO_PREC") == null ? "-" : rs.getBigDecimal("ARROTONDAMENTO_PREC").toString());
		m.setArrotondamentoAtt(rs.getBigDecimal("ARROTONDAMENTO_ATT") == null ? "-" : rs.getBigDecimal("ARROTONDAMENTO_ATT").toString());
		m.setImportoBollettaPrec(rs.getBigDecimal("IMPORTO_BOLLETTA_PREC") == null ? "-" : rs.getBigDecimal("IMPORTO_BOLLETTA_PREC").toString());
		m.setTotBolletta(rs.getBigDecimal("TOT_BOLLETTA") == null ? "-" : rs.getBigDecimal("TOT_BOLLETTA").toString());
		m.setTotPagato(rs.getBigDecimal("TOT_PAGATO") == null ? "-" : rs.getBigDecimal("TOT_PAGATO").toString());
		m.setFlNonPagare(rs.getBigDecimal("FL_NON_PAGARE") == null ? "-" : rs.getBigDecimal("FL_NON_PAGARE").toString());
		m.setMotNonPagare(rs.getString("MOT_NON_PAGARE") == null ? "-" : rs.getString("MOT_NON_PAGARE"));
		m.setNote(rs.getString("NOTE") == null ? "-" : rs.getString("NOTE"));
	
		return m;
		
	}
	
	private RttDettaglio getOggettoRttDettaglio(ResultSet rs, Connection conn) throws Exception{
		
		RttDettaglio m = new RttDettaglio();
		
		m.setId(rs.getString("ID"));
		m.setCodBolletta(rs.getString("COD_BOLLETTA"));
		m.setDtIniServizio(rs.getDate("DT_INI_SERVIZIO") == null ? "-" : SDF2.format(rs.getDate("DT_INI_SERVIZIO")));
		m.setDtFinServizio(rs.getDate("DT_FIN_SERVIZIO") == null ? "-" : SDF2.format(rs.getDate("DT_FIN_SERVIZIO")));
		m.setDesOggetto(rs.getString("DES_OGGETTO") == null ? "-" : rs.getString("DES_OGGETTO"));
		m.setUbicazione(rs.getString("UBICAZIONE") == null ? "-" : rs.getString("UBICAZIONE"));
		m.setCategoria(rs.getString("CATEGORIA") == null ? "-" : rs.getString("CATEGORIA"));
		m.setCodVoce(rs.getString("COD_VOCE") == null ? "-" : rs.getString("COD_VOCE"));
		m.setDesVoce(rs.getString("DES_VOCE") == null ? "-" : rs.getString("DES_VOCE"));
		m.setValore(rs.getString("VALORE") == null ? "-" : rs.getString("VALORE"));
	
		return m;
		
	}
	
	private RttRate getOggettoRttRate(ResultSet rs, Connection conn) throws Exception{
		
		RttRate m = new RttRate();
		
		m.setId(rs.getString("ID"));
		m.setCodBolletta(rs.getString("COD_BOLLETTA") == null ? "-" : rs.getString("COD_BOLLETTA"));
		m.setCodServizio(rs.getString("COD_SERVIZIO") == null ? "-" : rs.getString("COD_SERVIZIO"));
		m.setNumRata(rs.getBigDecimal("NUM_RATA") == null ? "-" : rs.getBigDecimal("NUM_RATA").toString());
		m.setDtScadenzaRata(rs.getDate("DT_SCADENZA_RATA") == null ? "-" : SDF2.format(rs.getDate("DT_SCADENZA_RATA")));
		m.setImportoRata(rs.getBigDecimal("IMPORTO_RATA") == null ? "-" : DF.format(rs.getBigDecimal("IMPORTO_RATA")));
		m.setImportoPagato(rs.getBigDecimal("IMPORTO_PAGATO") == null ? "-" : DF.format(rs.getBigDecimal("IMPORTO_PAGATO")));
		m.setDtPagamento(rs.getDate("DT_PAGAMENTO") == null ? "-" :SDF2.format(rs.getDate("DT_PAGAMENTO")));
		m.setDtRegPagamento(rs.getDate("DT_REG_PAGAMENTO") == null ? "-" : SDF2.format(rs.getDate("DT_REG_PAGAMENTO")));
		m.setDesDistinta(rs.getString("DES_DISTINTA") == null ? "-" : rs.getString("DES_DISTINTA"));
		m.setDtDistinta(rs.getDate("DT_DISTINTA") == null ? "-" : SDF2.format(rs.getDate("DT_DISTINTA")));
		m.setIdServizio(rs.getBigDecimal("ID_SERVIZIO") == null ? "-" : rs.getBigDecimal("ID_SERVIZIO").toString());
		m.setIdPratica(rs.getString("ID_PRATICA") == null ? "-" : rs.getString("ID_PRATICA"));
		m.setDesCanale(rs.getString("DES_CANALE") == null ? "-" : rs.getString("DES_CANALE"));
		m.setDesPagamento(rs.getString("DES_PAGAMENTO") == null ? "-" : rs.getString("DES_PAGAMENTO"));
		m.setNote(rs.getString("NOTE") == null ? "-" : rs.getString("NOTE"));
		
		return m;
		
	}
	
	private RttSoggetto getOggettoRttSoggetto(ResultSet rs, Connection conn) throws Exception{
		
		RttSoggetto m = new RttSoggetto();
		
		m.setId(rs.getString("ID"));
		m.setCodSoggetto(rs.getString("COD_SOGGETTO") == null ? "-" : rs.getString("COD_SOGGETTO"));
		m.setCodiceFiscale(rs.getString("CODICE_FISCALE") == null ? "-" : rs.getString("CODICE_FISCALE"));
		m.setProvenienza(rs.getBigDecimal("PROVENIENZA") == null ? "-" : rs.getBigDecimal("PROVENIENZA").toString());
		m.setCognome(rs.getString("COGNOME") == null ? "-" : rs.getString("COGNOME"));
		m.setNome(rs.getString("NOME") == null ? "-" : rs.getString("NOME"));
		m.setSesso(rs.getBigDecimal("SESSO") == null ? "-" : DF.format(rs.getBigDecimal("SESSO")));
		m.setDataNascita(rs.getDate("DATA_NASCITA") == null ? "-" :SDF2.format(rs.getDate("DATA_NASCITA")));
		m.setPartitaIva(rs.getString("PARTITA_IVA") == null ? "-" : rs.getString("PARTITA_IVA"));
		m.setComuneNascita(rs.getString("COMUNE_NASCITA") == null ? "-" : rs.getString("COMUNE_NASCITA").toString());
		m.setLocalitaNascita(rs.getString("LOCALITA_NASCITA") == null ? "-" : rs.getString("LOCALITA_NASCITA"));
		
		return m;
		
	}
	
	public Hashtable mCaricareLista(Vector listaPar, RetteFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			
			for (int i=0;i<=1;i++){
				//il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;
				
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					sql = sql + " and SIT_RTT_BOLLETTE.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by T.COD_BOLLETTA DESC) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						RttBollette m = getOggettoRttBollette(rs,conn);
						vct.add(m);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_RETTE, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER, finder);
			
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
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	public Hashtable mCaricareListaExt(String chiave, String progEs) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		Connection conn = null;
		
		try {
			conn = this.getConnection();
			java.sql.ResultSet rs = null;
			
			if("SOGGETTI".equals(progEs))
				sql = SQL_SELECT_LISTA_FROM_SOGG;
 

			this.initialize();
			this.setString(1, chiave);
			
			log.debug("SQL LISTA " + sql);
			
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()){
				RttBollette m = getOggettoRttBollette(rs,conn);
				vct.add(m);					
			}
			
			ht.put(LISTA_RETTE, vct);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = chiave;
				arguments[1] = progEs;
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
	
	public Hashtable mCaricareDettaglio(String chiave, String progEs) throws Exception {
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		RttBollette m = new RttBollette();
		RttSoggetto s = new RttSoggetto();
		Vector<RttDettaglio> listaDettaglio = new Vector<RttDettaglio>();
		Vector<RttRate> listaRate = new Vector<RttRate>();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				conn = this.getConnection();
				
				this.initialize();
				
				sql = SQL_SELECT_DETTAGLIO;
				
				this.setString(1, chiave);

				log.debug(sql);
				
				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (rs.next()) {
					m = getOggettoRttBollette(rs,conn);
				}
				
				//dati dettaglio
				if(m.getId() != null){
					
					this.initialize();
					sql = SQL_SELECT_DETTAGLIO_BOLLETTA;
					this.setString(1, m.getCodBolletta());
					log.debug(sql);
					prepareStatement(sql);
					java.sql.ResultSet rs1 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs1.next()) {
						RttDettaglio u = getOggettoRttDettaglio(rs1,conn);
						listaDettaglio.add(u);
					}
					ht.put(DETTAGLIO, listaDettaglio);
					
				}
				
				//dati rata
				if(m.getId() != null){
					
					this.initialize();
					sql = SQL_SELECT_DETTAGLIO_RATA;
					this.setString(1, m.getCodBolletta());
					log.debug(sql);
					prepareStatement(sql);
					java.sql.ResultSet rs2 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs2.next()) {
						RttRate c = getOggettoRttRate(rs2,conn);
						listaRate.add(c);
					}
					ht.put(RATE, listaRate);
					
				}
				
				//dati soggetto
				if(m.getId() != null){
					
					this.initialize();
					sql = SQL_SELECT_DETTAGLIO_SOGGETTO;
					this.setString(1, m.getCodSoggetto());
					log.debug(sql);
					prepareStatement(sql);
					java.sql.ResultSet rs3 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					if (rs3.next()) {
						s = getOggettoRttSoggetto(rs3,conn);
					}
					ht.put(SOGGETTO, s);
					
				}
				
				ht.put(RETTA, m);
			}
			
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
		finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception
	{
		String sql = "";
		Vector listaMul = new Vector();
		
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			listaMul.add(el);
		}
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaMul);
		indice = getCurrentParameterIndex();
		
		boolean trovatoMul = false;
		for (int i = 0; i < listaMul.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaMul.get(i);
			if (!"".equals(el.getValore())) {
				trovatoMul = true;
				break;
			}			
		}
		
		return sql;
	}
	
	public List<String> caricaAnno() throws Exception {
		Connection conn = null;
		List<String> lista = new ArrayList<String>();
		
		try {
				
				conn = this.getConnection();
				
				this.initialize();
				String sql = SQL_SELECT_ANNO;
				this.setInt(1,1);
				
				log.debug("SQL_SELECT_ANNO ["+sql+"]");

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				while (rs.next()) {
					lista.add(rs.getBigDecimal("ANNO").toString());
				}

		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		
		return lista;
	}
	
}
