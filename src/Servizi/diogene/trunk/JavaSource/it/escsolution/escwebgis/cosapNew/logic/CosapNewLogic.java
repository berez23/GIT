package it.escsolution.escwebgis.cosapNew.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import it.escsolution.escwebgis.cosapNew.bean.CosapContrib;
import it.escsolution.escwebgis.cosapNew.bean.CosapNewFinder;
import it.escsolution.escwebgis.cosapNew.bean.CosapTassa;

public class CosapNewLogic extends EscLogic {
	
	private String appoggioDataSource;
	
	public CosapNewLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	public final static String FINDER = "FINDER113";
	public final static String LISTA_COSAP = "LISTA_COSAP@CosapNewLogic";
	public final static String COSAP = "COSAP@CosapNewLogic";
	
	public static final HashMap<String, String> tipiPersona = new HashMap<String, String>();
	static {
		tipiPersona.put("F", "Persona Fisica");
		tipiPersona.put("G", "Persona Giuridica");
	}	
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
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
	"select * " +
	"from SIT_T_COSAP_CONTRIB A where 1=?";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio from SIT_T_COSAP_CONTRIB A where 1=?";
	
	private final static String SQL_SELECT_TASSE_CONTRIB = "SELECT * FROM SIT_T_COSAP_TASSA WHERE ID_EXT_CONTRIB = ? " +
															"ORDER BY INDIRIZZO, DT_INI_VALIDITA_TARIFFA DESC";
	
	private final static String SQL_SELECT_TASSE_CONTRIB_LISTA = "SELECT DISTINCT SEDIME, INDIRIZZO, CIVICO, DT_INI_VALIDITA, DT_FIN_VALIDITA " +
															"FROM SIT_T_COSAP_TASSA WHERE ID_EXT_CONTRIB = ? " +
															"ORDER BY INDIRIZZO, DT_INI_VALIDITA";
	
	private final static String SQL_SELECT_DETTAGLIO = "SELECT * FROM SIT_T_COSAP_CONTRIB WHERE ID = ?";
	
	private final static String SQL_SELECT_DETTAGLIO_FROM_OGGETTO= "SELECT c.* FROM SIT_T_COSAP_CONTRIB c, SIT_T_COSAP_TASSA t WHERE t.ID = ? and t.ID_EXT_CONTRIB= c.ID_EXT";
	
	public Hashtable mCaricareLista(Vector listaPar, CosapNewFinder finder) throws Exception {

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
					sql = sql + " and SIT_T_COSAP_CONTRIB.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by COG_DENOM, NOME) Q) where N > " + limInf + " and N <=" + limSup;

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						CosapContrib con = new CosapContrib();						
						con.setId(rs.getString("ID"));
						con.setTipoPersona(rs.getObject("TIPO_PERSONA") == null ? "-" : rs.getString("TIPO_PERSONA"));
						con.setDescTipoPersona(rs.getObject("TIPO_PERSONA") == null ? "-" : (tipiPersona.get(rs.getString("TIPO_PERSONA")) == null ? "-" : tipiPersona.get(rs.getString("TIPO_PERSONA"))));
						con.setCogDenom(rs.getObject("COG_DENOM") == null ? "-" : rs.getString("COG_DENOM"));
						con.setNome(rs.getObject("NOME") == null ? "-" : rs.getString("NOME"));
						con.setCodiceFiscale(rs.getObject("CODICE_FISCALE") == null ? "-" : rs.getString("CODICE_FISCALE"));
						con.setPartitaIva(rs.getObject("PARTITA_IVA") == null ? "-" : rs.getString("PARTITA_IVA"));
						con.setSedime(rs.getObject("SEDIME") == null ? "-" : rs.getString("SEDIME"));
						con.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "-" : rs.getString("INDIRIZZO"));
						con.setCivico(rs.getObject("CIVICO") == null ? "-" : rs.getString("CIVICO"));
						con.setTasse(getTasseContrib(conn, rs.getString("ID_EXT"), false));
						vct.add(con);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_COSAP, vct);
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
		CosapContrib con = new CosapContrib();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				conn = this.getConnection();
				
				this.initialize();
				String sql = SQL_SELECT_DETTAGLIO;
				
				this.setString(1, chiave);

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (rs.next()) {
					con.setId(rs.getString("ID"));
					con.setTipoPersona(rs.getObject("TIPO_PERSONA") == null ? "-" : rs.getString("TIPO_PERSONA"));
					con.setDescTipoPersona(rs.getObject("TIPO_PERSONA") == null ? "-" : (tipiPersona.get(rs.getString("TIPO_PERSONA")) == null ? "-" : tipiPersona.get(rs.getString("TIPO_PERSONA"))));
					con.setCogDenom(rs.getObject("COG_DENOM") == null ? "-" : rs.getString("COG_DENOM"));
					con.setNome(rs.getObject("NOME") == null ? "-" : rs.getString("NOME"));
					con.setCodiceFiscale(rs.getObject("CODICE_FISCALE") == null ? "-" : rs.getString("CODICE_FISCALE"));
					con.setPartitaIva(rs.getObject("PARTITA_IVA") == null ? "-" : rs.getString("PARTITA_IVA"));
					con.setDtNascita(rs.getObject("DT_NASCITA") == null ? "-" : SDF.format(rs.getTimestamp("DT_NASCITA")));
					con.setCodBelfioreNascita(rs.getObject("COD_BELFIORE_NASCITA") == null ? "-" : rs.getString("COD_BELFIORE_NASCITA"));
					con.setDescComuneNascita(rs.getObject("DESC_COMUNE_NASCITA") == null ? "-" : rs.getString("DESC_COMUNE_NASCITA"));
					con.setCodBelfioreResidenza(rs.getObject("COD_BELFIORE_RESIDENZA") == null ? "-" : rs.getString("COD_BELFIORE_RESIDENZA"));
					con.setDescComuneResidenza(rs.getObject("DESC_COMUNE_RESIDENZA") == null ? "-" : rs.getString("DESC_COMUNE_RESIDENZA"));
					con.setCapResidenza(rs.getObject("CAP_RESIDENZA") == null ? "-" : rs.getString("CAP_RESIDENZA"));
					con.setCodiceVia(rs.getObject("CODICE_VIA") == null ? "-" : rs.getString("CODICE_VIA"));
					con.setSedime(rs.getObject("SEDIME") == null ? "-" : rs.getString("SEDIME"));
					con.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "-" : rs.getString("INDIRIZZO"));
					con.setCivico(rs.getObject("CIVICO") == null ? "-" : rs.getString("CIVICO"));
					con.setDtIscrArchivio(rs.getObject("DT_ISCR_ARCHIVIO") == null ? "-" : SDF.format(rs.getTimestamp("DT_ISCR_ARCHIVIO")));
					con.setDtCostitSoggetto(rs.getObject("DT_COSTIT_SOGGETTO") == null ? "-" : SDF.format(rs.getTimestamp("DT_COSTIT_SOGGETTO")));
					con.setTasse(getTasseContrib(conn, rs.getString("ID_EXT"), true));
				}
				
				ht.put(COSAP, con);
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
		CosapContrib con = new CosapContrib();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				conn = this.getConnection();
				
				this.initialize();
				String sql = SQL_SELECT_DETTAGLIO_FROM_OGGETTO;
				
				this.setString(1, chiave);

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (rs.next()) {
					con.setId(rs.getString("ID"));
					con.setTipoPersona(rs.getObject("TIPO_PERSONA") == null ? "-" : rs.getString("TIPO_PERSONA"));
					con.setDescTipoPersona(rs.getObject("TIPO_PERSONA") == null ? "-" : (tipiPersona.get(rs.getString("TIPO_PERSONA")) == null ? "-" : tipiPersona.get(rs.getString("TIPO_PERSONA"))));
					con.setCogDenom(rs.getObject("COG_DENOM") == null ? "-" : rs.getString("COG_DENOM"));
					con.setNome(rs.getObject("NOME") == null ? "-" : rs.getString("NOME"));
					con.setCodiceFiscale(rs.getObject("CODICE_FISCALE") == null ? "-" : rs.getString("CODICE_FISCALE"));
					con.setPartitaIva(rs.getObject("PARTITA_IVA") == null ? "-" : rs.getString("PARTITA_IVA"));
					con.setDtNascita(rs.getObject("DT_NASCITA") == null ? "-" : SDF.format(rs.getTimestamp("DT_NASCITA")));
					con.setCodBelfioreNascita(rs.getObject("COD_BELFIORE_NASCITA") == null ? "-" : rs.getString("COD_BELFIORE_NASCITA"));
					con.setDescComuneNascita(rs.getObject("DESC_COMUNE_NASCITA") == null ? "-" : rs.getString("DESC_COMUNE_NASCITA"));
					con.setCodBelfioreResidenza(rs.getObject("COD_BELFIORE_RESIDENZA") == null ? "-" : rs.getString("COD_BELFIORE_RESIDENZA"));
					con.setDescComuneResidenza(rs.getObject("DESC_COMUNE_RESIDENZA") == null ? "-" : rs.getString("DESC_COMUNE_RESIDENZA"));
					con.setCapResidenza(rs.getObject("CAP_RESIDENZA") == null ? "-" : rs.getString("CAP_RESIDENZA"));
					con.setCodiceVia(rs.getObject("CODICE_VIA") == null ? "-" : rs.getString("CODICE_VIA"));
					con.setSedime(rs.getObject("SEDIME") == null ? "-" : rs.getString("SEDIME"));
					con.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "-" : rs.getString("INDIRIZZO"));
					con.setCivico(rs.getObject("CIVICO") == null ? "-" : rs.getString("CIVICO"));
					con.setDtIscrArchivio(rs.getObject("DT_ISCR_ARCHIVIO") == null ? "-" : SDF.format(rs.getTimestamp("DT_ISCR_ARCHIVIO")));
					con.setDtCostitSoggetto(rs.getObject("DT_COSTIT_SOGGETTO") == null ? "-" : SDF.format(rs.getTimestamp("DT_COSTIT_SOGGETTO")));
					con.setTasse(getTasseContrib(conn, rs.getString("ID_EXT"), true));
				}
				
				ht.put(COSAP, con);
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
		Vector listaParOgg = new Vector();
		Vector listaParSogg = new Vector();
		
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if (isParametroOggetto(el)) {
				listaParOgg.add(el);
			} else {
				listaParSogg.add(el);
			}
		}
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaParSogg);
		indice = getCurrentParameterIndex();
		
		boolean trovatoParOgg = false;
		for (int i = 0; i < listaParOgg.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaParOgg.get(i);
			if (!"".equals(el.getValore())) {
				trovatoParOgg = true;
				break;
			}			
		}
		
		if (trovatoParOgg) {			
			String sqlAdd = " AND EXISTS (SELECT 1 FROM SIT_T_COSAP_TASSA B " +
							"WHERE A.ID_EXT = B.ID_EXT_CONTRIB ";
			sqlAdd += super.elaboraFiltroMascheraRicercaParziale(indice, listaParOgg);
			sqlAdd += ")";
			sql += sqlAdd;
		}
		
		return sql;
	}
	
	private boolean isParametroOggetto(EscElementoFiltro el) {
		String attName = el.getAttributeName();
		
		return "VIA".equalsIgnoreCase(attName) ||
				"FOGLIO".equalsIgnoreCase(attName) ||
				"PARTICELLA".equalsIgnoreCase(attName) ||
				"SUBALTERNO".equalsIgnoreCase(attName);
	}
	
	private ArrayList<CosapTassa> getTasseContrib(Connection conn, String idExtContrib, boolean allFields) throws Exception {
		ArrayList<CosapTassa> tasse = new ArrayList<CosapTassa>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = allFields ? SQL_SELECT_TASSE_CONTRIB : SQL_SELECT_TASSE_CONTRIB_LISTA;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, idExtContrib);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CosapTassa tassa = new CosapTassa();
				
				tassa.setSedime(rs.getObject("SEDIME") == null ? "-" : rs.getString("SEDIME"));
				tassa.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "-" : rs.getString("INDIRIZZO"));
				tassa.setCivico(rs.getObject("CIVICO") == null ? "-" : rs.getString("CIVICO"));
				tassa.setDtIniValidita(rs.getObject("DT_INI_VALIDITA") == null ? "-" : SDF.format(rs.getTimestamp("DT_INI_VALIDITA")));
				tassa.setDtFinValidita(rs.getObject("DT_FIN_VALIDITA") == null ? "-" : SDF.format(rs.getTimestamp("DT_FIN_VALIDITA")));
				if (allFields) {
					tassa.setId(rs.getString("ID"));
					tassa.setCodUnivocoCanone(rs.getObject("COD_UNIVOCO_CANONE") == null ? "-" : rs.getString("COD_UNIVOCO_CANONE"));
					tassa.setIdExtContrib(idExtContrib);
					tassa.setTipoDocumento(rs.getObject("TIPO_DOCUMENTO") == null ? "-" : rs.getString("TIPO_DOCUMENTO"));
					tassa.setNumeroDocumento(rs.getObject("NUMERO_DOCUMENTO") == null ? "-" : rs.getString("NUMERO_DOCUMENTO"));
					tassa.setAnnoDocumento(rs.getObject("ANNO_DOCUMENTO") == null ? "-" : rs.getString("ANNO_DOCUMENTO"));
					tassa.setStatoDocumento(rs.getObject("STATO_DOCUMENTO") == null ? "-" : rs.getString("STATO_DOCUMENTO"));
					tassa.setNumeroProtocollo(rs.getObject("NUMERO_PROTOCOLLO") == null ? "-" : rs.getString("NUMERO_PROTOCOLLO"));
					tassa.setAnnoContabileDocumento(rs.getObject("ANNO_CONTABILE_DOCUMENTO") == null ? "-" : rs.getString("ANNO_CONTABILE_DOCUMENTO"));
					tassa.setDtProtocollo(rs.getObject("DT_PROTOCOLLO") == null ? "-" : SDF.format(rs.getTimestamp("DT_PROTOCOLLO")));				
					tassa.setDtRichiesta(rs.getObject("DT_RICHIESTA") == null ? "-" : SDF.format(rs.getTimestamp("DT_RICHIESTA")));
					tassa.setTipoOccupazione(rs.getObject("TIPO_OCCUPAZIONE") == null ? "-" : rs.getString("TIPO_OCCUPAZIONE"));
					tassa.setCodiceImmobile(rs.getObject("CODICE_IMMOBILE") == null ? "-" : rs.getString("CODICE_IMMOBILE"));
					tassa.setCodiceVia(rs.getObject("CODICE_VIA") == null ? "-" : rs.getString("CODICE_VIA"));
					tassa.setZona(rs.getObject("ZONA") == null ? "-" : rs.getString("ZONA"));
					tassa.setFoglio(rs.getObject("FOGLIO") == null ? "-" : DF.format(rs.getDouble("FOGLIO")));
					tassa.setParticella(rs.getObject("PARTICELLA") == null ? "-" : rs.getString("PARTICELLA"));
					tassa.setSubalterno(rs.getObject("SUBALTERNO") == null ? "-" : DF.format(rs.getDouble("SUBALTERNO")));
					tassa.setQuantita(rs.getObject("QUANTITA") == null ? "-" : DF.format(rs.getDouble("QUANTITA")));
					tassa.setUnitaMisuraQuantita(rs.getObject("UNITA_MISURA_QUANTITA") == null ? "-" : rs.getString("UNITA_MISURA_QUANTITA"));
					tassa.setTariffaPerUnita(rs.getObject("TARIFFA_PER_UNITA") == null ? "-" : DF.format(rs.getDouble("TARIFFA_PER_UNITA")));
					tassa.setImportoCanone(rs.getObject("IMPORTO_CANONE") == null ? "-" : DF.format(rs.getDouble("IMPORTO_CANONE")));
					tassa.setDtIniValiditaTariffa(rs.getObject("DT_INI_VALIDITA_TARIFFA") == null ? "-" : SDF.format(rs.getTimestamp("DT_INI_VALIDITA_TARIFFA")));
					tassa.setDtFinValiditaTariffa(rs.getObject("DT_FIN_VALIDITA_TARIFFA") == null ? "-" : SDF.format(rs.getTimestamp("DT_FIN_VALIDITA_TARIFFA")));
					tassa.setAnnoValiditaTariffa(rs.getObject("DT_INI_VALIDITA_TARIFFA") == null ? "-" : SDF_ANNO.format(rs.getTimestamp("DT_INI_VALIDITA_TARIFFA")));
					tassa.setDescrizione(rs.getObject("DESCRIZIONE") == null ? "-" : rs.getString("DESCRIZIONE"));
					tassa.setNote(rs.getObject("NOTE") == null ? "-" : rs.getString("NOTE"));
					tassa.setCodiceEsenzione(rs.getObject("CODICE_ESENZIONE") == null ? "-" : rs.getString("CODICE_ESENZIONE"));
					tassa.setScontoAssoluto(rs.getObject("SCONTO_ASSOLUTO") == null ? "-" : DF.format(rs.getDouble("SCONTO_ASSOLUTO")));
					tassa.setPercentualeSconto(rs.getObject("PERCENTUALE_SCONTO") == null ? "-" : DF.format(rs.getDouble("PERCENTUALE_SCONTO")));
					tassa.setDtIniSconto(rs.getObject("DT_INI_SCONTO") == null ? "-" : SDF.format(rs.getTimestamp("DT_INI_SCONTO")));
					tassa.setDtFinSconto(rs.getObject("DT_FIN_SCONTO") == null ? "-" : SDF.format(rs.getTimestamp("DT_FIN_SCONTO")));
				}
				tasse.add(tassa);
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
		
		return tasse;
	}
}
