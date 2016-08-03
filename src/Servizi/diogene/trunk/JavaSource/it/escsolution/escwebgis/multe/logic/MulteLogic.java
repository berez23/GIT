package it.escsolution.escwebgis.multe.logic;

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
import java.util.Vector;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.multe.bean.Multe;
import it.escsolution.escwebgis.multe.bean.MulteFinder;
import it.escsolution.escwebgis.pubblicita.bean.PubblicitaDettaglio;
import it.escsolution.escwebgis.pubblicita.bean.PubblicitaFinder;
import it.escsolution.escwebgis.pubblicita.bean.PubblicitaTestata;

public class MulteLogic extends EscLogic {
	
	public final static String LISTA_MULTE = "LISTA_MULTE@MulteLogic";
	public final static String FINDER = "FINDER120";
	public final static String MULTE = "MULTE@MulteLogic";	
	
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
	"select DISTINCT T.* " +
	"from SIT_TRFF_MULTE T where 1=?";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( select DISTINCT T.* from SIT_TRFF_MULTE T where 1=? AND t.dt_fine_val is null";
	
	private final static String SQL_SELECT_DETTAGLIO = "SELECT * FROM SIT_TRFF_MULTE WHERE ID = ? AND dt_fine_val is null";
	
	private final static String SQL_SELECT_DETTAGLIO_FROM_OGGETTO="SELECT DISTINCT t.* FROM SIT_TRFF_MULTE T " +
														"WHERE t.ID = ? AND t.dt_fine_val is null";
	
	
	public MulteLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	
	private Multe getOggettoMulte(ResultSet rs, Connection conn) throws Exception{
		
		Multe m = new Multe();
		
		m.setId(rs.getString("ID") == null ? "-" : rs.getString("ID"));
		m.setTipoVerbale(rs.getString("TIPO_VERBALE") == null ? "-" : rs.getString("TIPO_VERBALE"));
		m.setNrVerbale(rs.getString("NR_VERBALE") == null ? "-" : rs.getString("NR_VERBALE"));
		String dataMulta = rs.getObject("DATA_MULTA") == null ? "-" : SDF2.format(SDF.parse(rs.getBigDecimal("DATA_MULTA").toString()));
		m.setDataMulta(dataMulta);
		m.setImportoMulta(rs.getObject("IMPORTO_MULTA") == null ? "-" : DF.format(rs.getBigDecimal("IMPORTO_MULTA")));
		m.setImportoDovuto(rs.getObject("IMPORTO_DOVUTO") == null ? "-" : DF.format(rs.getBigDecimal("IMPORTO_DOVUTO")));
		String dataScadenza = rs.getObject("DT_SCADENZA_PAGAM") == null ? "-" : SDF2.format(SDF.parse(rs.getBigDecimal("DT_SCADENZA_PAGAM").toString()));
		m.setDtScadenzaPagamento(dataScadenza);
		m.setLuogoInfrazione(rs.getString("LUOGO_INFRAZIONE") == null ? "-" : rs.getString("LUOGO_INFRAZIONE"));
		m.setNote(rs.getString("NOTE") == null ? "-" : rs.getString("NOTE"));
		m.setTipoEnte(rs.getString("TIPO_ENTE") == null ? "-" : rs.getString("TIPO_ENTE"));
		m.setComuneEnte(rs.getString("COMUNE_ENTE") == null ? "-" : rs.getString("COMUNE_ENTE"));
		m.setTarga(rs.getString("TARGA") == null ? "-" : rs.getString("TARGA"));
		m.setMarca(rs.getString("MARCA") == null ? "-" : rs.getString("MARCA"));
		m.setModello(rs.getString("MODELLO") == null ? "-" : rs.getString("MODELLO"));
		m.setCodicePersona(rs.getString("CODICE_PERSONA") == null ? "-" : rs.getString("CODICE_PERSONA"));
		m.setCognome(rs.getString("COGNOME") == null ? "-" : rs.getString("COGNOME"));
		m.setNome(rs.getString("NOME") == null ? "-" : rs.getString("NOME"));
		m.setLuogoNascita(rs.getString("LUOGO_NASCITA") == null ? "-" : rs.getString("LUOGO_NASCITA"));
		m.setDtNascita(rs.getObject("DT_NASCITA") == null ? "-" : SDF2.format(SDF.parse(rs.getBigDecimal("DT_NASCITA").toString())));
		m.setLuogoResidenza(rs.getString("LUOGO_RESIDENZA") == null ? "-" : rs.getString("LUOGO_RESIDENZA"));
		m.setIndirizzoResidenza(rs.getString("INDIRIZZO_RESIDENZA") == null ? "-" : rs.getString("INDIRIZZO_RESIDENZA"));
		m.setNrPatente(rs.getString("NR_PATENTE") == null ? "-" : rs.getString("NR_PATENTE"));
		m.setDtRilascioPatente(rs.getObject("DT_RILASCIO_PATENTE") == null ? "-" : SDF2.format(SDF.parse(rs.getBigDecimal("DT_RILASCIO_PATENTE").toString())));
		m.setProvRilascioPatente(rs.getString("PROV_RILASCIO_PATENTE") == null ? "-" : rs.getString("PROV_RILASCIO_PATENTE"));
		m.setFlagPagamento("S".equals(rs.getString("FLAG_PAGAMENTO"))?"SI":"NO");
		m.setEstremiPagamento(rs.getString("ESTREMI_PAGAMENTO") == null ? "-" : rs.getString("ESTREMI_PAGAMENTO"));
		m.setSistemaPagamento(rs.getString("SISTEMA_PAGAMENTO") == null ? "-" : rs.getString("SISTEMA_PAGAMENTO"));
		m.setDtPagamento(rs.getObject("DT_PAGAMENTO") == null ? "-" : SDF2.format(SDF.parse(rs.getBigDecimal("DT_PAGAMENTO").toString())));
		m.setImportoPagato(rs.getObject("IMPORTO_PAGATO") == null ? "-" : DF.format(rs.getBigDecimal("IMPORTO_PAGATO")));
		m.setCodFisc(rs.getString("COD_FISC") == null ? "-" : rs.getString("COD_FISC"));
	
		return m;
		
	}
	
	public Hashtable mCaricareLista(Vector listaPar, MulteFinder finder) throws Exception {

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
					sql = sql + " and SIT_TRFF_MULTE.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by T.TIPO_VERBALE, T.DATA_MULTA DESC) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						Multe m = getOggettoMulte(rs,conn);
						vct.add(m);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_MULTE, vct);
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
	
	public Hashtable mCaricareDettaglio(String chiave) throws Exception {
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		Multe m = new Multe();
		
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
					m = getOggettoMulte(rs,conn);
				}
				
				ht.put(MULTE, m);
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
	
	public Hashtable mCaricareDettaglioFromOggetto(String chiave) throws Exception{
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		Multe m = new Multe();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				conn = this.getConnection();
				
				this.initialize();
				String sql = SQL_SELECT_DETTAGLIO_FROM_OGGETTO;
				
				this.setString(1, chiave);

				log.debug("SQL_SELECT_DETTAGLIO_FROM_OGGETTO ["+sql+"]");

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (rs.next()) {
					m = this.getOggettoMulte(rs, conn);
				}
				
				ht.put(MULTE, m);
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
	
}
