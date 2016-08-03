package it.escsolution.escwebgis.pratichePortale.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.acqua.bean.AcquaUtenze;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.pratichePortale.bean.Pratica;
import it.escsolution.escwebgis.pratichePortale.bean.PraticheFinder;

public class PraticheLogic extends EscLogic {
	
	public final static String LISTA_PRATICHE = "LISTA_PRATICHE@PraticheLogic";
	public final static String FINDER = "FINDER118";
	public final static String PRATICA = "PRATICA@PraticheLogic";
	public static final String ALTRE_PRATICHE = "ALTRE_PRATICHE@PraticheLogic";
	
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
	"select DISTINCT T.id,T.anagrafica_richiedente_id,T.anagrafica_fruitore_id,T.UTENTE_DESTINATARIO, T.DATA_RICHIESTA,T.DATA_CREAZIONE, T.UTENTE_MODIFICA_ID," +
	" T.DATA_MODIFICA, T.TIPO_SERVIZIO, T.SOTTO_TIPO_SERVIZIO, T.NOME_STATO," +
	" c.NOME as CRE_NOME, c.COGNOME as CRE_COGNOME, c.CODICE_FISCALE as CRE_CODFISC," +
	" r.NOME as RICH_NOME, r.COGNOME as RICH_COGNOME, r.CODICE_FISCALE as RICH_CODFISC," +
	" f.NOME as FRU_NOME, f.COGNOME as FRU_COGNOME, f.CODICE_FISCALE as FRU_CODFISC" +
	" from PS_PRATICA T, PS_ANAGRAFICA_VIEW C, PS_ANAGRAFICA_VIEW R, PS_ANAGRAFICA_VIEW F" +
	" where 1=? AND f.ID (+)= t.ANAGRAFICA_FRUITORE_ID AND r.ID (+)= t.ANAGRAFICA_RICHIEDENTE_ID" +
	" AND c.ID (+)= t.UTENTE_CREAZIONE_ID ";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( select DISTINCT T.id,T.anagrafica_richiedente_id,T.anagrafica_fruitore_id,T.UTENTE_DESTINATARIO, T.DATA_RICHIESTA,T.DATA_CREAZIONE, T.UTENTE_MODIFICA_ID," +
	" T.DATA_MODIFICA, T.TIPO_SERVIZIO, T.SOTTO_TIPO_SERVIZIO, T.NOME_STATO," +
	" c.NOME as CRE_NOME, c.COGNOME as CRE_COGNOME, c.CODICE_FISCALE as CRE_CODFISC," +
	" r.NOME as RICH_NOME, r.COGNOME as RICH_COGNOME, r.CODICE_FISCALE as RICH_CODFISC," +
	" f.NOME as FRU_NOME, f.COGNOME as FRU_COGNOME, f.CODICE_FISCALE as FRU_CODFISC" +
	" from PS_PRATICA T, PS_ANAGRAFICA_VIEW C, PS_ANAGRAFICA_VIEW R, PS_ANAGRAFICA_VIEW F" +
	" where 1=? AND f.ID (+)= t.ANAGRAFICA_FRUITORE_ID AND r.ID (+)= t.ANAGRAFICA_RICHIEDENTE_ID" +
	" AND c.ID (+)= t.UTENTE_CREAZIONE_ID ";
	
	private final static String SQL_SELECT_LISTA_EXT = "select DISTINCT T.id,T.anagrafica_richiedente_id,T.anagrafica_fruitore_id,T.UTENTE_DESTINATARIO, T.DATA_RICHIESTA,T.DATA_CREAZIONE, T.UTENTE_MODIFICA_ID," +
				" T.DATA_MODIFICA, T.TIPO_SERVIZIO, T.SOTTO_TIPO_SERVIZIO, T.NOME_STATO," +
				" c.NOME as CRE_NOME, c.COGNOME as CRE_COGNOME, c.CODICE_FISCALE as CRE_CODFISC," +
				" r.NOME as RICH_NOME, r.COGNOME as RICH_COGNOME, r.CODICE_FISCALE as RICH_CODFISC," +
				" f.NOME as FRU_NOME, f.COGNOME as FRU_COGNOME, f.CODICE_FISCALE as FRU_CODFISC" +
	            " FROM ps_pratica t, PS_ANAGRAFICA_VIEW C, PS_ANAGRAFICA_VIEW R, PS_ANAGRAFICA_VIEW F" +
	            " WHERE (t.anagrafica_fruitore_id = ? " +
	            " OR t.anagrafica_richiedente_id = ? " +
			    " OR t.utente_creazione_id = ?)" +
			    " AND f.ID (+)= t.ANAGRAFICA_FRUITORE_ID AND r.ID (+)= t.ANAGRAFICA_RICHIEDENTE_ID" +
				" AND c.ID (+)= t.UTENTE_CREAZIONE_ID ORDER BY DATA_CREAZIONE DESC";
	
	//il dettaglio non è mai chiamato perché è sufficente la lista delle pratiche
	private final static String SQL_SELECT_DETTAGLIO = "SELECT T.ID, T.UTENTE_DESTINATARIO, T.DATA_RICHIESTA, T.UTENTE_CREAZIONE_ID," +
			" T.DATA_CREAZIONE, T.UTENTE_MODIFICA_ID, T.DATA_MODIFICA, T.ESITO_PAGAMENTO_ID, T.TIPO_SERVIZIO, T.SOTTO_TIPO_SERVIZIO, T.ID_STATO," +
			" T.NOME_STATO, T.ANAGRAFICA_RICHIEDENTE_ID, T.ANAGRAFICA_FRUITORE_ID, T.PER_ALTRA_PERSONA," +
			" T.TIPO_CONSEGNA_ALLEGATI, T.TIPO_CONSEGNA_RISULTATI, T.NOTE_STATO, T.STATO_SCAMBIO_BUSTA_ID, T.STATO_SCAMBIO_BUSTA_DESC," +
			" T.STATO_SCAMBIO_BUSTA_TIME, T.ALERTING_TIME, T.ALERTING_STATO_PRATICA FROM PS_PRATICA T WHERE T.ID = ?";
	
	private final static String SQL_SELECT_DETTAGLIO_ALTRE_PRATICHE = "SELECT DISTINCT SELECT T.ID, T.UTENTE_DESTINATARIO, T.DATA_RICHIESTA, T.UTENTE_CREAZIONE_ID," +
			" T.DATA_CREAZIONE, T.UTENTE_MODIFICA_ID, T.DATA_MODIFICA, T.ESITO_PAGAMENTO_ID, T.TIPO_SERVIZIO, T.SOTTO_TIPO_SERVIZIO, T.ID_STATO," +
			" T.NOME_STATO, T.ANAGRAFICA_RICHIEDENTE_ID, T.ANAGRAFICA_FRUITORE_ID, T.PER_ALTRA_PERSONA," +
			" T.TIPO_CONSEGNA_ALLEGATI, T.TIPO_CONSEGNA_RISULTATI, T.NOTE_STATO, T.STATO_SCAMBIO_BUSTA_ID, T.STATO_SCAMBIO_BUSTA_DESC," +
			" T.STATO_SCAMBIO_BUSTA_TIME, T.ALERTING_TIME, T.ALERTING_STATO_PRATICA " +
			"FROM ps_pratica t " +
			"WHERE t.ID != ? " +
			"AND ( t.anagrafica_fruitore_id = ? " +
			"OR t.anagrafica_richiedente_id = ? " +
			"OR t.anagrafica_richiedente_id = ? " +
			"OR t.anagrafica_fruitore_id = ?)"; 
	
	private final static String SQL_SELECT_DETTAGLIO_FROM_OGGETTO="SELECT DISTINCT t.* " +
			"FROM ps_pratica t, ps_anagrafica_view a " +
			"WHERE a.ID = ? " +
			"AND (   a.ID = t.anagrafica_fruitore_id " +
			"OR a.ID = t.anagrafica_richiedente_id " +
			"OR a.ID = t.utente_creazione_id)"; 

	private static final String SQL_SELECT_TIPI_SERVIZIO = "SELECT DISTINCT tipo_servizio,sotto_tipo_servizio," +
			"tipo_servizio || DECODE (sotto_tipo_servizio,NULL, '',' - ' || sotto_tipo_servizio) as descrizione" +
			" FROM PS_PRATICA WHERE 1=? ORDER BY tipo_servizio, sotto_tipo_servizio";

	private static final String SQL_SELECT_TIPI_STATO = "SELECT DISTINCT nome_stato FROM PS_PRATICA WHERE 1=? ORDER BY nome_stato";
	
	
	public PraticheLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	
	private Pratica getOggettoPratica(ResultSet rs, Connection conn) throws Exception{
		
		Pratica m = new Pratica();
		
		m.setId(rs.getString("ID") == null ? "-" : rs.getString("ID"));
		m.setUtenteDestinatario(rs.getString("UTENTE_DESTINATARIO") == null ? "-" : rs.getString("UTENTE_DESTINATARIO"));
		m.setDataRichiesta(rs.getObject("DATA_RICHIESTA") == null ? "-" : SDF2.format(rs.getDate("DATA_RICHIESTA")));
		m.setNomeCreatore(rs.getString("CRE_NOME") == null ? "-" : rs.getString("CRE_NOME"));
		m.setCognomeCreatore(rs.getString("CRE_COGNOME") == null ? "-" : rs.getString("CRE_COGNOME"));
		m.setCodFisCreatore(rs.getString("CRE_CODFISC") == null ? "-" : rs.getString("CRE_CODFISC"));
		m.setDataCreazione(rs.getObject("DATA_CREAZIONE") == null ? "-" : SDF2.format(rs.getDate("DATA_CREAZIONE")));
		m.setUtenteModificaId(rs.getObject("UTENTE_MODIFICA_ID") == null ? "-" : DF.format(rs.getBigDecimal("UTENTE_MODIFICA_ID")));
		m.setDataModifica(rs.getObject("DATA_RICHIESTA") == null ? "-" : SDF2.format(rs.getDate("DATA_RICHIESTA")));
		m.setTipo(rs.getString("TIPO_SERVIZIO") == null ? "-" : rs.getString("TIPO_SERVIZIO"));
		m.setSottotipo(rs.getString("SOTTO_TIPO_SERVIZIO") == null ? "-" : rs.getString("SOTTO_TIPO_SERVIZIO"));
		m.setNomeStato(rs.getString("NOME_STATO") == null ? "-" : rs.getString("NOME_STATO"));
		m.setNomeRichiedente(rs.getString("RICH_NOME") == null ? "-" : rs.getString("RICH_NOME"));
		m.setNomeFruitore(rs.getString("FRU_NOME") == null ? "-" : rs.getString("FRU_NOME"));
		m.setCognomeRichiedente(rs.getString("RICH_COGNOME") == null ? "-" : rs.getString("RICH_COGNOME"));
		m.setCognomeFruitore(rs.getString("FRU_COGNOME") == null ? "-" : rs.getString("FRU_COGNOME"));
		m.setCodFisFruitore(rs.getString("FRU_CODFISC") == null ? "-" : rs.getString("FRU_CODFISC"));
		m.setCodFisRichiedente(rs.getString("RICH_CODFISC") == null ? "-" : rs.getString("RICH_CODFISC"));
		m.setIdFruitore(rs.getString("ANAGRAFICA_FRUITORE_ID") == null ? "-" : rs.getString("ANAGRAFICA_FRUITORE_ID"));
		m.setIdRichiedente(rs.getString("ANAGRAFICA_RICHIEDENTE_ID") == null ? "-" : rs.getString("ANAGRAFICA_RICHIEDENTE_ID"));
	
		return m;
		
	}
	
	public Hashtable mCaricareLista(Vector listaPar, PraticheFinder finder) throws Exception {

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
					sql = sql + " and PS_PRATICA.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by T.DATA_CREAZIONE DESC) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						Pratica m = getOggettoPratica(rs,conn);
						vct.add(m);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_PRATICHE, vct);
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
			
			sql = SQL_SELECT_LISTA_EXT;

			this.initialize();
			this.setDouble(1, new Double(chiave).doubleValue());
			this.setDouble(2, new Double(chiave).doubleValue());
			this.setDouble(3, new Double(chiave).doubleValue());
			
			log.debug("SQL LISTA " + sql);
			
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()){
				Pratica m = getOggettoPratica(rs,conn);
				vct.add(m);					
			}
			
			ht.put(LISTA_PRATICHE, vct);
			
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
		Pratica m = null;
		Pratica m2 = new Pratica();
		Vector<Pratica> altrePratiche = new Vector<Pratica>();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				conn = this.getConnection();
				
				this.initialize();
				
				if("PRATICHE PORTALE".equals(progEs))
					sql = SQL_SELECT_DETTAGLIO_FROM_OGGETTO;
				else sql = SQL_SELECT_DETTAGLIO;
				
				this.setString(1, chiave);

				log.debug(sql);
				
				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (rs.next()) {
					m = getOggettoPratica(rs,conn);
				}
				
				//dati altre utenze
				if(m!= null && m.getId() != null){
					
					this.initialize();
					sql = SQL_SELECT_DETTAGLIO_ALTRE_PRATICHE;
					this.setString(1, m.getId());
					this.setString(2, m.getIdFruitore());
					this.setString(3, m.getIdFruitore());
					this.setString(4, m.getIdRichiedente());
					this.setString(5, m.getIdRichiedente());
					log.debug(sql);
					prepareStatement(sql);
					java.sql.ResultSet rs1 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs1.next()) {
						m2 = getOggettoPratica(rs1,conn);
						altrePratiche.add(m2);
					}
					ht.put(ALTRE_PRATICHE, altrePratiche);
					
				}
				
				ht.put(PRATICA, m);
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


	public List<String> caricaTipiServizio() throws Exception {
		Connection conn = null;
		List<String> lista = new ArrayList<String>();
		
		try {
				
				conn = this.getConnection();
				
				this.initialize();
				String sql = SQL_SELECT_TIPI_SERVIZIO;
				this.setInt(1,1);

				log.debug("SQL_SELECT_TIPI_SERVIZIO ["+sql+"]");

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				while (rs.next()) {
					lista.add(rs.getString("DESCRIZIONE"));
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


	public List<String> caricaTipiStato() throws Exception {
		Connection conn = null;
		List<String> lista = new ArrayList<String>();
		
		try {
				
				conn = this.getConnection();
				
				this.initialize();
				String sql = SQL_SELECT_TIPI_STATO;
				this.setInt(1,1);
				
				log.debug("SQL_SELECT_TIPI_STATO ["+sql+"]");

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				while (rs.next()) {
					lista.add(rs.getString("NOME_STATO"));
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
