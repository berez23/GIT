package it.escsolution.escwebgis.licenzeCommercioNew.logic;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioAnagNew;
import it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioNew;
import it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioNewFinder;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

public class LicenzeCommercioNewLogic extends EscLogic {
	
	public static final String FINDER = "FINDER55";
	public static final String LISTA = "LISTA_LICENZE_COMMERCIO_NEW";
	public static final String DATI_LICENZE_COMMERCIO_NEW = LicenzeCommercioNewLogic.class.getName() + "@DATI_LICENZE_COMMERCIO_NEW";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
	public static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DF.setDecimalFormatSymbols(dfs);
	}
	
	private final static String SQL_SELECT_LISTA = "SELECT ROWNUM N, SEL.* FROM ("
		+"SELECT A.ID, A.ID_EXT, A.ID_ORIG, A.FK_ENTE_SORGENTE, NUMERO, NUMERO_PROTOCOLLO, ANNO_PROTOCOLLO,"
		+" TIPOLOGIA, CARATTERE, STATO, DATA_INIZIO_SOSPENSIONE, DATA_FINE_SOSPENSIONE, "
		+" NVL(SEDIME, '') || DECODE(SEDIME, NULL, '', '', '', ' ') || NVL(INDIRIZZO, '') AS INDIRIZZO,"
		+" CIVICO, CIVICO2, CIVICO3, SUPERFICIE_MINUTO, SUPERFICIE_TOTALE,"
		+" SEZIONE_CATASTALE, FOGLIO, PARTICELLA, SUBALTERNO, CODICE_FABBRICATO,"
		+" DATA_VALIDITA, DATA_RILASCIO, ZONA, RAG_SOC, NOTE, RIFERIM_ATTO, A.PROVENIENZA"
		+" FROM SIT_LICENZE_COMMERCIO A, SIT_LICENZE_COMMERCIO_VIE B" 
		+" WHERE 1=? AND A.DT_FINE_VAL IS NULL"
		+" AND A.ID_EXT_VIE = B.ID_EXT(+)";
	
	private final static String SQL_SELECT_COUNT_LISTA = "SELECT COUNT(*) AS CONTEGGIO FROM ("
		+"SELECT A.ID, A.ID_EXT, A.ID_ORIG, A.FK_ENTE_SORGENTE, NUMERO, NUMERO_PROTOCOLLO, ANNO_PROTOCOLLO,"
		+" TIPOLOGIA, CARATTERE, STATO, DATA_INIZIO_SOSPENSIONE, DATA_FINE_SOSPENSIONE,"
		+" NVL(SEDIME, '') || DECODE(SEDIME, NULL, '', '', '', ' ') || NVL(INDIRIZZO, '') AS INDIRIZZO,"
		+" CIVICO, CIVICO2, CIVICO3, SUPERFICIE_MINUTO, SUPERFICIE_TOTALE,"
		+" SEZIONE_CATASTALE, FOGLIO, PARTICELLA, SUBALTERNO, CODICE_FABBRICATO,"
		+" DATA_VALIDITA, DATA_RILASCIO, ZONA, RAG_SOC, NOTE, RIFERIM_ATTO, A.PROVENIENZA"
		+" FROM SIT_LICENZE_COMMERCIO A, SIT_LICENZE_COMMERCIO_VIE B" 
		+" WHERE 1=? AND A.DT_FINE_VAL IS NULL"
		+" AND A.ID_EXT_VIE = B.ID_EXT(+)";

	private final static String SQL_SELECT_DETTAGLIO = "SELECT A.*,"
		+" NVL(SEDIME, '') || DECODE(SEDIME, NULL, '', '', '', ' ') || NVL(INDIRIZZO, '') AS INDIRIZZO, B.ID as ID_VIA"
		+" FROM SIT_LICENZE_COMMERCIO A, SIT_LICENZE_COMMERCIO_VIE B"
		+" WHERE 1=? AND A.ID = ?"
		+" AND A.ID_EXT_VIE = B.ID_EXT(+)";
	
	private final static String SQL_SELECT_DETTAGLIO_ANAG = "SELECT B.*, NVL(TITOLO, '') AS TITOLO"
		+" FROM SIT_LICENZE_COMMERCIO A, SIT_LICENZE_COMMERCIO_ANAG B, SIT_LICENZE_COMMERCIO_TIT C"
		+" WHERE 1=? AND A.ID = ?"
		+" AND A.ID_EXT = C.ID_EXT_AUTORIZZAZIONE"
		+" AND B.ID_EXT = C.ID_EXT_ANAGRAFICA";

	private final static String SQL_SELECT_ID_STORICI = "SELECT ID, DT_INIZIO_VAL, DT_FINE_VAL"
		+ " FROM SIT_LICENZE_COMMERCIO G "
		+ "WHERE EXISTS (SELECT ID_EXT FROM SIT_LICENZE_COMMERCIO GG WHERE ID = ? "
		+ "AND G.ID_EXT = GG.ID_EXT) ORDER BY DT_FINE_VAL NULLS FIRST";
	
	private final static String SQL_SELECT_LISTA_FROM_SOGGETTO="SELECT * FROM " +
			"(SELECT ROWNUM N, SEL.* FROM (SELECT A.ID, A.ID_EXT, A.ID_ORIG, A.FK_ENTE_SORGENTE," +
			" A.NUMERO, NUMERO_PROTOCOLLO, ANNO_PROTOCOLLO, TIPOLOGIA, CARATTERE, STATO, " +
			"DATA_INIZIO_SOSPENSIONE, DATA_FINE_SOSPENSIONE," +
			"  NVL(SEDIME, '') || DECODE(SEDIME, NULL, '', '', '', ' ') || NVL(INDIRIZZO, '') AS INDIRIZZO," +
			" CIVICO, CIVICO2, CIVICO3, SUPERFICIE_MINUTO, SUPERFICIE_TOTALE, SEZIONE_CATASTALE," +
			" FOGLIO, PARTICELLA, SUBALTERNO, CODICE_FABBRICATO, DATA_VALIDITA, DATA_RILASCIO, " +
			"ZONA, RAG_SOC, NOTE, RIFERIM_ATTO, A.PROVENIENZA" +
			" FROM SIT_LICENZE_COMMERCIO A, SIT_LICENZE_COMMERCIO_VIE B, SIT_LICENZE_COMMERCIO_ANAG ANAG, SIT_LICENZE_COMMERCIO_TIT TIT" +
			" WHERE 1=? AND A.DT_FINE_VAL IS NULL AND A.ID_EXT_VIE = B.ID_EXT(+) " +
			"and ANAG.ID = ? AND A.ID_EXT = TIT.ID_EXT_AUTORIZZAZIONE AND ANAG.ID_EXT = TIT.ID_EXT_ANAGRAFICA"+
			" ORDER BY ID_ORIG) SEL) ";
	
	private final static String SQL_SELECT_LISTA_FROM_VIA="SELECT * FROM " +
	"(SELECT ROWNUM N, SEL.* FROM (SELECT A.ID, A.ID_EXT, A.ID_ORIG, A.FK_ENTE_SORGENTE," +
	" A.NUMERO, NUMERO_PROTOCOLLO, ANNO_PROTOCOLLO, TIPOLOGIA, CARATTERE, STATO, " +
	" DATA_INIZIO_SOSPENSIONE, DATA_FINE_SOSPENSIONE," +
	"  NVL(SEDIME, '') || DECODE(SEDIME, NULL, '', '', '', ' ') || NVL(INDIRIZZO, '') AS INDIRIZZO," +
	" CIVICO, CIVICO2, CIVICO3, SUPERFICIE_MINUTO, SUPERFICIE_TOTALE, SEZIONE_CATASTALE," +
	" FOGLIO, PARTICELLA, SUBALTERNO, CODICE_FABBRICATO, DATA_VALIDITA, DATA_RILASCIO, " +
	" ZONA, RAG_SOC, NOTE, RIFERIM_ATTO, A.PROVENIENZA" +
	" FROM SIT_LICENZE_COMMERCIO A, SIT_LICENZE_COMMERCIO_VIE B " +
	" WHERE 1=? AND A.DT_FINE_VAL IS NULL AND A.ID_EXT_VIE = B.ID_EXT(+) " +
	" and B.id= ? "+
	" ORDER BY ID_ORIG) SEL) ";
	
	
	private String appoggioDataSource;
	
	public LicenzeCommercioNewLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}
	
public Hashtable mCaricareLista(Vector listaPar, LicenzeCommercioNewFinder finder) throws Exception {
	
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

				if (finder != null) {
					// il primo passaggio esegue la select count
					if (finder.getKeyStr().equals("")) {
						sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
					} else {
						// controllo provenienza chiamata
						String controllo = finder.getKeyStr();
						if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
							String ente = controllo.substring(0,controllo.indexOf("|"));
							ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
							String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
							sql = sql + " and A.ID in (" +chiavi + ")" ;
							//	sql = sql + " and  ENTE_S.CODENT = '" +ente + "'" ;
						}
						else
							sql = sql + " and A.ID in (" +finder.getKeyStr() + ")" ;
					}
				} 

				if (finder == null) {
					if (i == 1) {
						sql = "SELECT * FROM(" + sql;
						sql += " ORDER BY ID_ORIG) SEL)";
					} else {
						sql += ")";
					}
				} else {
					long limInf, limSup;
					limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
					limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

					if (i == 1) {
						sql = "SELECT * FROM(" + sql;
						sql += " ORDER BY ID_ORIG) SEL)";
						sql += " where N > " + limInf + " and N <=" + limSup;
					} else {
						sql += ")";
					}
				}

				sql = elaboraSqlLista(sql);
				prepareStatement(sql);

				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1) {
					while (rs.next()) {
						// campi della lista
						LicenzeCommercioNew lic = new LicenzeCommercioNew();
						lic.setId(rs.getString("ID"));
						lic.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
						lic.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
						lic.setNumero(rs.getObject("NUMERO") == null ? "" : rs.getString("NUMERO"));
						lic.setNumeroProtocollo(rs.getObject("NUMERO_PROTOCOLLO") == null ? "" : rs.getString("NUMERO_PROTOCOLLO"));
						lic.setAnnoProtocollo(rs.getObject("ANNO_PROTOCOLLO") == null ? "" : rs.getString("ANNO_PROTOCOLLO"));
						lic.setTipologia(rs.getObject("TIPOLOGIA") == null ? "" : rs.getString("TIPOLOGIA"));
						lic.setCarattere(rs.getObject("CARATTERE") == null ? "" : rs.getString("CARATTERE"));
						lic.setStato(rs.getObject("STATO") == null ? "" : rs.getString("STATO"));
						lic.setDataInizioSospensione(rs.getObject("DATA_INIZIO_SOSPENSIONE") == null ? "" : SDF.format(rs.getTimestamp("DATA_INIZIO_SOSPENSIONE")));
						lic.setDataFineSospensione(rs.getObject("DATA_FINE_SOSPENSIONE") == null ? "" : SDF.format(rs.getTimestamp("DATA_FINE_SOSPENSIONE")));
						lic.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "" : rs.getString("INDIRIZZO"));
						lic.setCivico(rs.getObject("CIVICO") == null ? "" : rs.getString("CIVICO"));
						lic.setCivico2(rs.getObject("CIVICO2") == null ? "" : rs.getString("CIVICO2"));
						lic.setCivico3(rs.getObject("CIVICO3") == null ? "" : rs.getString("CIVICO3"));
						lic.setSuperficieMinuto(rs.getObject("SUPERFICIE_MINUTO") == null ? "" : DF.format(rs.getDouble("SUPERFICIE_MINUTO")));
						lic.setSuperficieTotale(rs.getObject("SUPERFICIE_TOTALE") == null ? "" : DF.format(rs.getDouble("SUPERFICIE_TOTALE")));					
						lic.setSezioneCatastale(rs.getObject("SEZIONE_CATASTALE") == null ? "" : rs.getString("SEZIONE_CATASTALE"));
						lic.setFoglio(rs.getObject("FOGLIO") == null ? "" : rs.getString("FOGLIO"));
						lic.setParticella(rs.getObject("PARTICELLA") == null ? "" : rs.getString("PARTICELLA"));
						lic.setSubalterno(rs.getObject("SUBALTERNO") == null ? "" : rs.getString("SUBALTERNO"));
						lic.setCodiceFabbricato(rs.getObject("CODICE_FABBRICATO") == null ? "" : rs.getString("CODICE_FABBRICATO"));
						lic.setDataValidita(rs.getObject("DATA_VALIDITA") == null ? "" : SDF.format(rs.getTimestamp("DATA_VALIDITA")));
						lic.setDataRilascio(rs.getObject("DATA_RILASCIO") == null ? "" : SDF.format(rs.getTimestamp("DATA_RILASCIO")));
						lic.setZona(rs.getObject("ZONA") == null ? "" : rs.getString("ZONA"));
						lic.setRagSoc(rs.getObject("RAG_SOC") == null ? "" : rs.getString("RAG_SOC"));
						lic.setNote(rs.getObject("NOTE") == null ? "" : rs.getString("NOTE"));
						lic.setRiferimAtto(rs.getObject("RIFERIM_ATTO") == null ? "" : rs.getString("RIFERIM_ATTO"));						
						lic.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));
						vct.add(lic);
					}
				} else {
					if (rs.next()) {
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(LicenzeCommercioNewLogic.LISTA, vct);

			if (finder != null) {
				finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
				// pagine totali
				finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
				finder.setTotaleRecord(conteggione);
				finder.setRighePerPagina(RIGHE_PER_PAGINA);

				ht.put(LicenzeCommercioNewLogic.FINDER, finder);
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




public Hashtable mCaricareListaFromSoggetto(String idSoggetto) throws Exception {
	return caricaListaExt(idSoggetto, SQL_SELECT_LISTA_FROM_SOGGETTO);
}

public Hashtable mCaricareListaFromVia(String idVia) throws Exception {
	return caricaListaExt(idVia, SQL_SELECT_LISTA_FROM_VIA);
}

private Hashtable caricaListaExt(String id, String sql) throws Exception {
	
	// carico la lista e la metto in un hashtable
	Hashtable ht = new Hashtable();
	Vector vct = new Vector();
	
	
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

			this.setString(indice,id);
			prepareStatement(sql);

			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			
				while (rs.next()) {
					// campi della lista
					LicenzeCommercioNew lic = new LicenzeCommercioNew();
					lic.setId(rs.getString("ID"));
					lic.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
					lic.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
					lic.setNumero(rs.getObject("NUMERO") == null ? "" : rs.getString("NUMERO"));
					lic.setNumeroProtocollo(rs.getObject("NUMERO_PROTOCOLLO") == null ? "" : rs.getString("NUMERO_PROTOCOLLO"));
					lic.setAnnoProtocollo(rs.getObject("ANNO_PROTOCOLLO") == null ? "" : rs.getString("ANNO_PROTOCOLLO"));
					lic.setTipologia(rs.getObject("TIPOLOGIA") == null ? "" : rs.getString("TIPOLOGIA"));
					lic.setCarattere(rs.getObject("CARATTERE") == null ? "" : rs.getString("CARATTERE"));
					lic.setStato(rs.getObject("STATO") == null ? "" : rs.getString("STATO"));
					lic.setDataInizioSospensione(rs.getObject("DATA_INIZIO_SOSPENSIONE") == null ? "" : SDF.format(rs.getTimestamp("DATA_INIZIO_SOSPENSIONE")));
					lic.setDataFineSospensione(rs.getObject("DATA_FINE_SOSPENSIONE") == null ? "" : SDF.format(rs.getTimestamp("DATA_FINE_SOSPENSIONE")));
					lic.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "" : rs.getString("INDIRIZZO"));
					lic.setCivico(rs.getObject("CIVICO") == null ? "" : rs.getString("CIVICO"));
					lic.setCivico2(rs.getObject("CIVICO2") == null ? "" : rs.getString("CIVICO2"));
					lic.setCivico3(rs.getObject("CIVICO3") == null ? "" : rs.getString("CIVICO3"));
					lic.setSuperficieMinuto(rs.getObject("SUPERFICIE_MINUTO") == null ? "" : DF.format(rs.getDouble("SUPERFICIE_MINUTO")));
					lic.setSuperficieTotale(rs.getObject("SUPERFICIE_TOTALE") == null ? "" : DF.format(rs.getDouble("SUPERFICIE_TOTALE")));					
					lic.setSezioneCatastale(rs.getObject("SEZIONE_CATASTALE") == null ? "" : rs.getString("SEZIONE_CATASTALE"));
					lic.setFoglio(rs.getObject("FOGLIO") == null ? "" : rs.getString("FOGLIO"));
					lic.setParticella(rs.getObject("PARTICELLA") == null ? "" : rs.getString("PARTICELLA"));
					lic.setSubalterno(rs.getObject("SUBALTERNO") == null ? "" : rs.getString("SUBALTERNO"));
					lic.setCodiceFabbricato(rs.getObject("CODICE_FABBRICATO") == null ? "" : rs.getString("CODICE_FABBRICATO"));
					lic.setDataValidita(rs.getObject("DATA_VALIDITA") == null ? "" : SDF.format(rs.getTimestamp("DATA_VALIDITA")));
					lic.setDataRilascio(rs.getObject("DATA_RILASCIO") == null ? "" : SDF.format(rs.getTimestamp("DATA_RILASCIO")));
					lic.setZona(rs.getObject("ZONA") == null ? "" : rs.getString("ZONA"));
					lic.setRagSoc(rs.getObject("RAG_SOC") == null ? "" : rs.getString("RAG_SOC"));
					lic.setNote(rs.getObject("NOTE") == null ? "" : rs.getString("NOTE"));
					lic.setRiferimAtto(rs.getObject("RIFERIM_ATTO") == null ? "" : rs.getString("RIFERIM_ATTO"));						
					lic.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));
					vct.add(lic);
				}
			
			
		

		ht.put(LicenzeCommercioNewLogic.LISTA, vct);

		/*INIZIO AUDIT*/
		try {
			Object[] arguments = new Object[2];
			arguments[0] = id;
			arguments[1] = sql;
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
	
	public Hashtable mCaricareDettaglio(String chiave) throws Exception {
	
		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
	
			conn = this.getConnection();
			
			//dati autorizzazione
			this.initialize();
			String sql = SQL_SELECT_DETTAGLIO;
	
			int indice = 1;
			this.setInt(indice, 1);
			indice++;
			this.setString(indice, chiave);
			indice++;		
	
			LicenzeCommercioNew lic = new LicenzeCommercioNew();
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			boolean trovato = false;
				
			if (rs.next()){
				trovato = true;
				lic.setId(rs.getString("ID"));
				lic.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
				lic.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
				lic.setNumero(rs.getObject("NUMERO") == null ? "" : rs.getString("NUMERO"));
				lic.setNumeroProtocollo(rs.getObject("NUMERO_PROTOCOLLO") == null ? "" : rs.getString("NUMERO_PROTOCOLLO"));
				lic.setAnnoProtocollo(rs.getObject("ANNO_PROTOCOLLO") == null ? "" : rs.getString("ANNO_PROTOCOLLO"));
				lic.setTipologia(rs.getObject("TIPOLOGIA") == null ? "" : rs.getString("TIPOLOGIA"));
				lic.setCarattere(rs.getObject("CARATTERE") == null ? "" : rs.getString("CARATTERE"));
				lic.setStato(rs.getObject("STATO") == null ? "" : rs.getString("STATO"));
				lic.setDataInizioSospensione(rs.getObject("DATA_INIZIO_SOSPENSIONE") == null ? "" : SDF.format(rs.getTimestamp("DATA_INIZIO_SOSPENSIONE")));
				lic.setDataFineSospensione(rs.getObject("DATA_FINE_SOSPENSIONE") == null ? "" : SDF.format(rs.getTimestamp("DATA_FINE_SOSPENSIONE")));
				lic.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "" : rs.getString("INDIRIZZO"));
				lic.setCivico(rs.getObject("CIVICO") == null ? "" : rs.getString("CIVICO"));
				lic.setCivico2(rs.getObject("CIVICO2") == null ? "" : rs.getString("CIVICO2"));
				lic.setCivico3(rs.getObject("CIVICO3") == null ? "" : rs.getString("CIVICO3"));
				lic.setSuperficieMinuto(rs.getObject("SUPERFICIE_MINUTO") == null ? "" : DF.format(rs.getDouble("SUPERFICIE_MINUTO")));
				lic.setSuperficieTotale(rs.getObject("SUPERFICIE_TOTALE") == null ? "" : DF.format(rs.getDouble("SUPERFICIE_TOTALE")));					
				lic.setSezioneCatastale(rs.getObject("SEZIONE_CATASTALE") == null ? "" : rs.getString("SEZIONE_CATASTALE"));
				lic.setFoglio(rs.getObject("FOGLIO") == null ? "" : rs.getString("FOGLIO"));
				lic.setParticella(rs.getObject("PARTICELLA") == null ? "" : rs.getString("PARTICELLA"));
				lic.setSubalterno(rs.getObject("SUBALTERNO") == null ? "" : rs.getString("SUBALTERNO"));
				lic.setCodiceFabbricato(rs.getObject("CODICE_FABBRICATO") == null ? "" : rs.getString("CODICE_FABBRICATO"));
				lic.setDataValidita(rs.getObject("DATA_VALIDITA") == null ? "" : SDF.format(rs.getTimestamp("DATA_VALIDITA")));
				lic.setDataRilascio(rs.getObject("DATA_RILASCIO") == null ? "" : SDF.format(rs.getTimestamp("DATA_RILASCIO")));
				lic.setZona(rs.getObject("ZONA") == null ? "" : rs.getString("ZONA"));
				lic.setRagSoc(rs.getObject("RAG_SOC") == null ? "" : rs.getString("RAG_SOC"));
				lic.setNote(rs.getObject("NOTE") == null ? "" : rs.getString("NOTE"));
				lic.setRiferimAtto(rs.getObject("RIFERIM_ATTO") == null ? "" : rs.getString("RIFERIM_ATTO"));						
				lic.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));		
				lic.setIdVia(rs.getObject("ID_VIA") == null ? "" : rs.getString("ID_VIA"));
			}
			
			//dati anagrafici
			if (trovato) {
				this.initialize();
				sql = SQL_SELECT_DETTAGLIO_ANAG;
		
				indice = 1;
				this.setInt(indice, 1);
				indice++;
				this.setString(indice, chiave);
				indice++;		
		
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					
				while (rs.next()){
					LicenzeCommercioAnagNew anag = new LicenzeCommercioAnagNew();
					anag.setId(rs.getString("ID"));
					anag.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
					anag.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
					anag.setNumero(rs.getObject("NUMERO") == null ? "" : rs.getString("NUMERO"));
					anag.setCodiceFiscale(rs.getObject("CODICE_FISCALE") == null ? "" : rs.getString("CODICE_FISCALE"));
					anag.setCognome(rs.getObject("COGNOME") == null ? "" : rs.getString("COGNOME"));
					anag.setNome(rs.getObject("NOME") == null ? "" : rs.getString("NOME"));
					anag.setDenominazione(rs.getObject("DENOMINAZIONE") == null ? "" : rs.getString("DENOMINAZIONE"));
					anag.setFormaGiuridica(rs.getObject("FORMA_GIURIDICA") == null ? "" : rs.getString("FORMA_GIURIDICA"));
					anag.setDataNascita(rs.getObject("DATA_NASCITA") == null ? "" : SDF.format(rs.getTimestamp("DATA_NASCITA")));					
					anag.setComuneNascita(rs.getObject("COMUNE_NASCITA") == null ? "" : rs.getString("COMUNE_NASCITA"));					
					anag.setProvinciaNascita(rs.getObject("PROVINCIA_NASCITA") == null ? "" : rs.getString("PROVINCIA_NASCITA"));					
					anag.setIndirizzoResidenza(rs.getObject("INDIRIZZO_RESIDENZA") == null ? "" : rs.getString("INDIRIZZO_RESIDENZA"));					
					anag.setCivicoResidenza(rs.getObject("CIVICO_RESIDENZA") == null ? "" : rs.getString("CIVICO_RESIDENZA"));
					anag.setCapResidenza(rs.getObject("CAP_RESIDENZA") == null ? "" : rs.getString("CAP_RESIDENZA"));
					anag.setComuneResidenza(rs.getObject("COMUNE_RESIDENZA") == null ? "" : rs.getString("COMUNE_RESIDENZA"));
					anag.setProvinciaResidenza(rs.getObject("PROVINCIA_RESIDENZA") == null ? "" : rs.getString("PROVINCIA_RESIDENZA"));
					anag.setDataInizioResidenza(rs.getObject("DATA_INIZIO_RESIDENZA") == null ? "" : SDF.format(rs.getTimestamp("DATA_INIZIO_RESIDENZA")));					
					anag.setTel(rs.getObject("TEL") == null ? "" : rs.getString("TEL"));
					anag.setFax(rs.getObject("FAX") == null ? "" : rs.getString("FAX"));
					anag.setEmail(rs.getObject("EMAIL") == null ? "" : rs.getString("EMAIL"));
					anag.setPiva(rs.getObject("PIVA") == null ? "" : rs.getString("PIVA"));
					anag.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));
					anag.setTitolo(rs.getObject("TITOLO") == null ? "" : rs.getString("TITOLO"));
					
					if (lic.getAnags() == null) {
						lic.setAnags(new ArrayList<LicenzeCommercioAnagNew>());
					}
					lic.getAnags().add(anag);
				}
			}
	
			ht.put(LicenzeCommercioNewLogic.DATI_LICENZE_COMMERCIO_NEW, lic);			
			
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
	
	private String elaboraSqlLista(String sql) {
		sql = sql.replace("SOGGETTO = ?", 
							"EXISTS (SELECT 1 FROM SIT_LICENZE_COMMERCIO_ANAG C, SIT_LICENZE_COMMERCIO_TIT D"
							+" WHERE NVL(C.DENOMINAZIONE, LTRIM(RTRIM(C.COGNOME || ' ' || C.NOME))) = ?"
							+" AND A.ID_EXT = D.ID_EXT_AUTORIZZAZIONE"
							+" AND C.ID_EXT = D.ID_EXT_ANAGRAFICA)")
				  .replace("UPPER(SOGGETTO) LIKE '%'||?||'%'", 
							"EXISTS (SELECT 1 FROM SIT_LICENZE_COMMERCIO_ANAG C, SIT_LICENZE_COMMERCIO_TIT D"
							+" WHERE UPPER(NVL(C.DENOMINAZIONE, LTRIM(RTRIM(C.COGNOME || ' ' || C.NOME)))) LIKE '%'||?||'%'"
							+" AND A.ID_EXT = D.ID_EXT_AUTORIZZAZIONE"
							+" AND C.ID_EXT = D.ID_EXT_ANAGRAFICA)");
		return sql;
	}
	
}
