package it.escsolution.escwebgis.pubblicita.logic;

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
import it.escsolution.escwebgis.pubblicita.bean.PubblicitaDettaglio;
import it.escsolution.escwebgis.pubblicita.bean.PubblicitaFinder;
import it.escsolution.escwebgis.pubblicita.bean.PubblicitaTestata;

public class PubblicitaLogic extends EscLogic {
	
	public final static String LISTA_PUBBLICITA = "LISTA_PUBBLICITA@PubblicitaLogic";
	public final static String FINDER = "FINDER115";
	public final static String PUBBLICITA = "PUBBLICITA@PubblicitaLogic";	
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
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
	"from SIT_PUBBLICITA_PRAT_TESTATA T, SIT_PUBBLICITA_PRAT_DETTAGLIO D where D.ID_EXT_TESTATA (+)=T.id_EXT AND 1=?";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( select DISTINCT T.* from SIT_PUBBLICITA_PRAT_TESTATA T, SIT_PUBBLICITA_PRAT_DETTAGLIO D where D.ID_EXT_TESTATA (+)=T.id_EXT AND 1=?";
	
	private final static String SQL_SELECT_DETTAGLIO_TESTATA = "SELECT d.* FROM SIT_PUBBLICITA_PRAT_DETTAGLIO d WHERE ID_EXT_TESTATA = ? " +
															   "ORDER BY d.DESC_CLS,  d.DESC_OGGETTO, d.DT_INIZIO DESC, d.DT_FINE DESC, d.INDIRIZZO";
	
	private final static String SQL_SELECT_DETTAGLIO_TESTATA_LISTA = "SELECT DISTINCT SEDIME, INDIRIZZO, CIVICO, DT_INI_VALIDITA, DT_FIN_VALIDITA " +
															"FROM SIT_PUBBLICITA_PRAT_DETTAGLIO WHERE ID_EXT_TESTATA = ? " +
															"ORDER BY INDIRIZZO, DT_INIZIO";
	
	private final static String SQL_SELECT_DETTAGLIO = "SELECT * FROM SIT_PUBBLICITA_PRAT_TESTATA WHERE ID = ?";
	
	private final static String SQL_SELECT_DETTAGLIO_FROM_OGGETTO="SELECT DISTINCT t.* FROM SIT_PUBBLICITA_PRAT_TESTATA T, SIT_PUBBLICITA_PRAT_DETTAGLIO d " +
														"WHERE d.ID = ? and d.ID_EXT_TESTATA = t.ID_EXT ";
	
	
	public PubblicitaLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	
	private PubblicitaTestata getOggettoPubblicitaTestata(ResultSet rs, Connection conn) throws Exception{
		
		PubblicitaTestata tes = new PubblicitaTestata();
		
		tes.setId(rs.getString("ID"));
		tes.setTipoPratica(rs.getObject("TIPO_PRATICA") == null ? "-" : rs.getString("TIPO_PRATICA"));
		tes.setNumPratica(rs.getInt("NUM_PRATICA"));
		tes.setAnnoPratica(rs.getString("ANNO_PRATICA"));
		tes.setDescPratica(rs.getString("DESCRIZIONE"));
		tes.setProvvedimento(rs.getString("PROVVEDIMENTO"));
		tes.setDtInizio(rs.getObject("DT_INIZIO") == null ? "-" : SDF.format(rs.getTimestamp("DT_INIZIO")));
		tes.setDtDecTermini(rs.getObject("DT_DEC_TERMINI") == null ? "-" : SDF.format(rs.getTimestamp("DT_DEC_TERMINI")));
		
		tes.setLstDettaglio(getLstDettagliPratica(conn, rs.getString("ID_EXT"), true));
		
		return tes;
		
	}
	
	public Hashtable mCaricareLista(Vector listaPar, PubblicitaFinder finder) throws Exception {

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
					sql = sql + " and SIT_PUBBLICITA_PRAT_TESTATA.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by T.TIPO_PRATICA, T.ANNO_PRATICA DESC, T.NUM_PRATICA) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						PubblicitaTestata tes = getOggettoPubblicitaTestata(rs,conn);
						vct.add(tes);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_PUBBLICITA, vct);
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
		PubblicitaTestata tes = new PubblicitaTestata();
		
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
					tes = getOggettoPubblicitaTestata(rs,conn);
				}
				
				ht.put(PUBBLICITA, tes);
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
		PubblicitaTestata tes = new PubblicitaTestata();
		
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
					tes = this.getOggettoPubblicitaTestata(rs, conn);
				}
				
				ht.put(PUBBLICITA, tes);
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
		Vector listaParTes = new Vector();
		Vector listaParDet = new Vector();
		
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if (isParametroDettaglio(el)) {
				listaParDet.add(el);
			} else {
				listaParTes.add(el);
			}
		}
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaParTes);
		indice = getCurrentParameterIndex();
		
		boolean trovatoParDet = false;
		for (int i = 0; i < listaParDet.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaParDet.get(i);
			if (!"".equals(el.getValore())) {
				trovatoParDet = true;
				break;
			}			
		}
		
		if (trovatoParDet) {			
			String sqlAdd = " AND EXISTS (SELECT 1 FROM SIT_PUBBLICITA_PRAT_DETTAGLIO D " +
							"WHERE T.ID_EXT = D.ID_EXT_TESTATA ";
			sqlAdd += super.elaboraFiltroMascheraRicercaParziale(indice, listaParDet);
			sqlAdd += ")";
			sql += sqlAdd;
		}
		
		return sql;
	}
	
	private boolean isParametroDettaglio(EscElementoFiltro el) {
		String attName = el.getAttributeName();
		
		return "VIA".equalsIgnoreCase(attName) ||
			   "CIVICO".equalsIgnoreCase(attName) ||
			   "DESC_CLS".equalsIgnoreCase(attName) ||
			   "DT_INIZIO_D".equalsIgnoreCase(attName) ||
			   "DT_FINE_D".equalsIgnoreCase(attName);
	}
	
	private ArrayList<PubblicitaDettaglio> getLstDettagliPratica(Connection conn, String idExtPratica, boolean allFields) throws Exception {
		ArrayList<PubblicitaDettaglio> lstDettaglio = new ArrayList<PubblicitaDettaglio>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = allFields ? SQL_SELECT_DETTAGLIO_TESTATA : SQL_SELECT_DETTAGLIO_TESTATA_LISTA;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, idExtPratica);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				PubblicitaDettaglio det = new PubblicitaDettaglio();
				
				det.setId(rs.getString("ID"));
				det.setTipoPratica(rs.getString("TIPO_PRATICA"));
				det.setNumPratica(rs.getInt("NUM_PRATICA"));
				det.setAnnoPratica(rs.getString("ANNO_PRATICA"));
				det.setCodClasse(rs.getString("COD_CLS"));
				det.setDescClasse(rs.getString("DESC_CLS"));
				det.setCodOggetto(rs.getString("COD_OGGETTO"));
				det.setDescOggetto(rs.getString("DESC_OGGETTO"));
				det.setAnnotazioni(rs.getString("ANNOTAZIONI"));
				det.setIndirizzo(rs.getString("INDIRIZZO"));
				det.setVia(rs.getString("VIA"));
				det.setCivico(rs.getString("CIVICO"));
				det.setDtInizio(rs.getObject("DT_INIZIO") == null ? "-" : SDF.format(rs.getTimestamp("DT_INIZIO")));
				det.setDtFine(rs.getObject("DT_FINE") == null ? "-" : SDF.format(rs.getTimestamp("DT_FINE")));
				det.setNumGiorniOccupazione(rs.getInt("GIORNI_OCCUPAZIONE"));
				det.setCodZonaCespite(rs.getString("COD_ZONA_CESPITE"));
				det.setDescZonaCespite(rs.getString("DESC_ZONA_CESPITE"));
				det.setBase(rs.getBigDecimal("BASE"));
				det.setAltezza(rs.getBigDecimal("ALTEZZA"));
				det.setSupImponibile(rs.getBigDecimal("SUP_IMPONIBILE"));
				det.setSupEsistente(rs.getBigDecimal("SUP_ESISTENTE"));
				det.setCodCaratteristica(rs.getString("COD_CARATTERISTICA"));
				det.setDescCaratteristica(rs.getString("DESC_CARATTERISTICA"));
				det.setNumFacce(rs.getInt("NUM_FACCE"));
				det.setNumEsempleri(rs.getInt("NUM_ESEMPLARI"));
				 
				lstDettaglio.add(det);
				
			}
		
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null) {
				pstmt.close();
			}
		}
		
		return lstDettaglio;
	}
}
