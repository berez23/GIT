/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.successioni.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.escsolution.escwebgis.successioni.bean.Erede;
import it.escsolution.escwebgis.successioni.bean.EredeFinder;
import it.escsolution.escwebgis.successioni.bean.Eredita;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.dbutils.DbUtils;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SuccessioniErediLogic extends SuccessioniLogic{
	private String appoggioDataSource;
	public SuccessioniErediLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	private final static String SQL_SELECT_LISTA = 
			"select DISTINCT " +
			"decode(CF_EREDE,null,'-',CF_EREDE) AS CF_EREDE," +
			"decode(DENOMINAZIONE,null,'-',DENOMINAZIONE) AS DENOMINAZIONE," +
			"decode(SESSO,null,'-',SESSO) AS SESSO," +
			"decode(CITTA_NASCITA,null,'-',CITTA_NASCITA) AS CITTA_NASCITA," +
			"decode(PROV_NASCITA,null,'-',PROV_NASCITA) AS PROV_NASCITA," +
			"decode(DATA_NASCITA,null,'-',DATA_NASCITA) AS DATA_NASCITA," +
			"decode(UFFICIO,null,'-',UFFICIO) AS UFFICIO," +
			"decode(ANNO,null,'-',ANNO) AS ANNO," +
			"decode(VOLUME,null,'-',VOLUME) AS VOLUME," +
			"decode(NUMERO,null,'-',NUMERO) AS NUMERO," +
			"decode(SOTTONUMERO,null,'-',SOTTONUMERO) AS SOTTONUMERO," +
			"decode(COMUNE,null,'-',COMUNE) AS COMUNE," +
			"decode(PROGRESSIVO,null,'-',PROGRESSIVO) AS PROGRESSIVO, " +
			"decode(PROGRESSIVO_EREDE,null,'-',PROGRESSIVO_EREDE) AS PROGRESSIVO_EREDE " +
			"from mi_successioni_b WHERE 1=?" ;

	
	public Hashtable mCaricareListaEredi(Vector listaPar, EredeFinder finder) throws Exception{
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

			for (int i=0;i<=1;i++){
				// il primo ciclo faccio la count
				if (i==0)
					sql = "select count(*) as conteggio  from ( " + SQL_SELECT_LISTA;
				else
					sql = "select * from ( select ROWNUM as N, S.* from ( " + SQL_SELECT_LISTA;
				

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;

				//il primo passaggio esegue la select count
				//campi della search

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					listaPar = this.verificaParamAnnoRicerca(listaPar);
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					//controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					String chiavi = "";
					if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
						String ente = controllo.substring(0,controllo.indexOf("|"));
						ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
						chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
					}else
						chiavi = controllo;

					sql = sql + " and mi_successioni_b.PK_ID_SUCCB in (" +chiavi + ")" ;

				}


				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i ==1){
					sql = sql + " order by DENOMINAZIONE";
					sql = sql + ") S ) where N > " + limInf + " and N <=" + limSup;
				}else{
					sql = sql + ")";
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						Erede def = new Erede();
						def.setCodFiscale(rs.getString("CF_EREDE"));
						def.setDenominazione(rs.getString("DENOMINAZIONE"));
						def.setComuneNascita(rs.getString("CITTA_NASCITA"));
						def.setProvNascita(rs.getString("PROV_NASCITA"));
						def.setDataNascita(rs.getString("DATA_NASCITA"));
						def.setSesso(rs.getString("SESSO"));
						//chiave
						def.setUfficio(rs.getString("UFFICIO"));
						def.setAnno(rs.getString("ANNO"));
						def.setVolume(rs.getString("VOLUME"));
						def.setNumero(rs.getString("NUMERO"));
						def.setSottonumero(rs.getString("SOTTONUMERO"));
						def.setComune(rs.getString("COMUNE"));
						def.setProgressivo(rs.getString("PROGRESSIVO"));
						def.setProgressivoErede(rs.getString("PROGRESSIVO_EREDE"));

						vct.add(def);
					}
				}
				else{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put("LISTA_EREDI",vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put("FINDER",finder);
			
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
		finally{
			DbUtils.close(conn);
		}
	}


	public Hashtable mCaricareDettaglioErede(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
			//recupero le chiavi di ricerca
			ArrayList listParam = new ArrayList();
			while (chiave.indexOf('|') > 0) {
				listParam.add(chiave.substring(0,chiave.indexOf('|')));
				chiave = chiave.substring(chiave.indexOf('|')+1);
			}
			listParam.add(chiave);


			conn = this.getConnection();
			this.initialize();
			String sql = "select DISTINCT " +
						 "pk_id_succb as ID, " +
						 " decode(CF_EREDE,null,'-',CF_EREDE) AS CF_EREDE," +
					     "decode(DENOMINAZIONE,null,'-',DENOMINAZIONE) AS DENOMINAZIONE," +
					     "decode(SESSO,null,'-',SESSO) AS SESSO," +
					     "decode(CITTA_NASCITA,null,'-',CITTA_NASCITA) AS CITTA_NASCITA," +
					     "decode(PROV_NASCITA,null,'-',PROV_NASCITA) AS PROV_NASCITA," +
					     "decode(DATA_NASCITA,null,'-',DATA_NASCITA) AS DATA_NASCITA," +
					     "decode(UFFICIO,null,'-',UFFICIO) AS UFFICIO," +
					     "decode(ANNO,null,'-',ANNO) AS ANNO," +
					     "decode(VOLUME,null,'-',VOLUME) AS VOLUME," +
					     "decode(NUMERO,null,'-',NUMERO) AS NUMERO," +
					     "decode(SOTTONUMERO,null,'-',SOTTONUMERO) AS SOTTONUMERO," +
					     "decode(COMUNE,null,'-',COMUNE) AS COMUNE," +
					     "decode(PROGRESSIVO,null,'-',PROGRESSIVO) AS PROGRESSIVO, " +
					     "decode(PROGRESSIVO_EREDE,null,'-',PROGRESSIVO_EREDE) AS PROGRESSIVO_EREDE " +
					     "from mi_successioni_b " +
					     "WHERE UFFICIO = ? " +
					     "AND  ANNO = ? " +
					     "AND  VOLUME = ? " +
					     "AND  NUMERO = ? " +
					     "AND  SOTTONUMERO = ? " +
					     "AND  COMUNE = ? " +
					     //"AND  PROGRESSIVO = ? " +
					     "AND  PROGRESSIVO_EREDE = ? ";

			int indice = 1;
			this.setString(indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			//this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice));


			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Erede ere = new Erede();

			if (rs.next()){
				ere.setCodFiscale(rs.getString("CF_EREDE"));
				ere.setDenominazione(rs.getString("DENOMINAZIONE"));
				ere.setComuneNascita(rs.getString("CITTA_NASCITA"));
				ere.setProvNascita(rs.getString("PROV_NASCITA"));
				ere.setDataNascita(rs.getString("DATA_NASCITA"));
				ere.setSesso(rs.getString("SESSO"));
				//chiave
				ere.setUfficio(rs.getString("UFFICIO"));
				ere.setAnno(rs.getString("ANNO"));
				ere.setVolume(rs.getString("VOLUME"));
				ere.setNumero(rs.getString("NUMERO"));
				ere.setSottonumero(rs.getString("SOTTONUMERO"));
				ere.setComune(rs.getString("COMUNE"));
				ere.setProgressivo(rs.getString("PROGRESSIVO"));
				ere.setProgressivoErede(rs.getString("PROGRESSIVO_EREDE"));
				ere.setId(rs.getString("ID"));
			}
			ht.put("EREDE",ere);
			
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
	
	public Hashtable mCaricareDettaglioFromSoggetto(String idSoggetto) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
			


			conn = this.getConnection();
			this.initialize();
			String sql = "SELECT DISTINCT " +
						 "pk_id_succb as ID, " +
						 "decode(CF_EREDE,null,'-',CF_EREDE) AS CF_EREDE," +
					     "decode(DENOMINAZIONE,null,'-',DENOMINAZIONE) AS DENOMINAZIONE," +
					     "decode(SESSO,null,'-',SESSO) AS SESSO," +
					     "decode(CITTA_NASCITA,null,'-',CITTA_NASCITA) AS CITTA_NASCITA," +
					     "decode(PROV_NASCITA,null,'-',PROV_NASCITA) AS PROV_NASCITA," +
					     "decode(DATA_NASCITA,null,'-',DATA_NASCITA) AS DATA_NASCITA," +
					     "decode(UFFICIO,null,'-',UFFICIO) AS UFFICIO," +
					     "decode(ANNO,null,'-',ANNO) AS ANNO," +
					     "decode(VOLUME,null,'-',VOLUME) AS VOLUME," +
					     "decode(NUMERO,null,'-',NUMERO) AS NUMERO," +
					     "decode(SOTTONUMERO,null,'-',SOTTONUMERO) AS SOTTONUMERO," +
					     "decode(COMUNE,null,'-',COMUNE) AS COMUNE," +
					     "decode(PROGRESSIVO,null,'-',PROGRESSIVO) AS PROGRESSIVO, " +
					     "decode(PROGRESSIVO_EREDE,null,'-',PROGRESSIVO_EREDE) AS PROGRESSIVO_EREDE " +
					     "from mi_successioni_b " +
					     "WHERE pk_id_succb = ? ";

			int indice = 1;
			
			this.setString(indice,idSoggetto);


			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Erede ere = new Erede();

			if (rs.next()){
				ere.setCodFiscale(rs.getString("CF_EREDE"));
				ere.setDenominazione(rs.getString("DENOMINAZIONE"));
				ere.setComuneNascita(rs.getString("CITTA_NASCITA"));
				ere.setProvNascita(rs.getString("PROV_NASCITA"));
				ere.setDataNascita(rs.getString("DATA_NASCITA"));
				ere.setSesso(rs.getString("SESSO"));
				//chiave
				ere.setUfficio(rs.getString("UFFICIO"));
				ere.setAnno(rs.getString("ANNO"));
				ere.setVolume(rs.getString("VOLUME"));
				ere.setNumero(rs.getString("NUMERO"));
				ere.setSottonumero(rs.getString("SOTTONUMERO"));
				ere.setComune(rs.getString("COMUNE"));
				ere.setProgressivo(rs.getString("PROGRESSIVO"));
				ere.setProgressivoErede(rs.getString("PROGRESSIVO_EREDE"));
				ere.setId(rs.getString("ID"));
			}
			ht.put("EREDE",ere);
			
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
	
	public Hashtable mCaricareListaEredita(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		Connection conn = null;

		try {
			//recupero le chiavi di ricerca
			ArrayList listParam = new ArrayList();
			while (chiave.indexOf('|') > 0) {
				listParam.add(chiave.substring(0,chiave.indexOf('|')));
				chiave = chiave.substring(chiave.indexOf('|')+1);
			}
			listParam.add(chiave);


			conn = this.getConnection();
			this.initialize();
			String sql = "SELECT DISTINCT " +
					"mi_successioni_a.CF_DEFUNTO," +
					"mi_successioni_a.COGNOME_DEFUNTO," +
					"mi_successioni_a.NOME_DEFUNTO," +
					"DECODE(mi_successioni_c.FOGLIO,NULL,'-',mi_successioni_c.FOGLIO)AS FOGLIO," +
					"DECODE(mi_successioni_c.PARTICELLA1,NULL,'-',mi_successioni_c.PARTICELLA1) AS PARTICELLA1," +
					"DECODE(mi_successioni_c.SUBALTERNO1,NULL,'-',mi_successioni_c.SUBALTERNO1) AS SUBALTERNO1," +
					"DECODE( SUBSTR (RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0'),-1,1 ),',', SUBSTR (RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0'),1,LENGTH(RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0'))-1  ) ,RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0')   ) as NUM_QUOTA_DEF," +
					"DECODE( SUBSTR (LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0'),-1,1 ),',', SUBSTR (LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0'),1,LENGTH(LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0'))-1  ) ,LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0')   ) as DEN_QUOTA_DEF," +
					"DECODE( SUBSTR (RTRIM(LTRIM(mi_successioni_d.NUMERATORE_QUOTA,'0'),'0'),-1,1 ),',', SUBSTR (RTRIM(LTRIM(mi_successioni_d.NUMERATORE_QUOTA,'0'),'0'),1,LENGTH(RTRIM(LTRIM(mi_successioni_d.NUMERATORE_QUOTA,'0'),'0'))-1  ) ,RTRIM(LTRIM(mi_successioni_d.NUMERATORE_QUOTA,'0'),'0')   ) as NUM_QUOTA_EREDE," +
					"DECODE( SUBSTR (LTRIM(mi_successioni_d.DENOMINATORE_QUOTA,'0'),-1,1 ),',', SUBSTR (LTRIM(mi_successioni_d.DENOMINATORE_QUOTA,'0'),1,LENGTH(LTRIM(mi_successioni_d.DENOMINATORE_QUOTA,'0'))-1  ) ,LTRIM(mi_successioni_d.DENOMINATORE_QUOTA,'0')   ) as DEN_QUOTA_EREDE," +
					"mi_successioni_d.UFFICIO," +
					"mi_successioni_d.ANNO," +
					"mi_successioni_d.VOLUME," +
					"mi_successioni_d.NUMERO," +
					"mi_successioni_d.SOTTONUMERO," +
					"mi_successioni_d.COMUNE," +
					"mi_successioni_d.PROGRESSIVO," +
					"mi_successioni_a.PROGRESSIVO as PROGRESSIVO_DEF," +
					"mi_successioni_d.PROGRESSIVO_EREDE," +
					"mi_successioni_d.PROGRESSIVO_IMMOBILE " +
					"from mi_successioni_d,mi_successioni_a,mi_successioni_c " +
					"where mi_successioni_d.UFFICIO = mi_successioni_c.UFFICIO " +
					"and mi_successioni_d.ANNO = mi_successioni_c.ANNO " +
					"and mi_successioni_d.VOLUME = mi_successioni_c.VOLUME " +
					"and mi_successioni_d.NUMERO =  mi_successioni_c.NUMERO " +
					"and mi_successioni_d.SOTTONUMERO = mi_successioni_c.SOTTONUMERO " +
					"and mi_successioni_d.COMUNE = mi_successioni_c.COMUNE " +
					//"and mi_successioni_d.PROGRESSIVO = mi_successioni_c.PROGRESSIVO " +
					"and mi_successioni_d.PROGRESSIVO_IMMOBILE = mi_successioni_c.PROGRESSIVO_IMMOBILE " +
					"and mi_successioni_d.UFFICIO = mi_successioni_a.UFFICIO " +
					"and mi_successioni_d.ANNO = mi_successioni_a.ANNO " +
					"and mi_successioni_d.VOLUME = mi_successioni_a.VOLUME " +
					"and mi_successioni_d.NUMERO =  mi_successioni_a.NUMERO " +
					"and mi_successioni_d.SOTTONUMERO = mi_successioni_a.SOTTONUMERO " +
					"and mi_successioni_d.COMUNE = mi_successioni_a.COMUNE " +
					//"and mi_successioni_d.PROGRESSIVO = mi_successioni_a.PROGRESSIVO " +
					"and mi_successioni_d.UFFICIO = ? " +
					"and mi_successioni_d.ANNO = ? " +
					"and mi_successioni_d.VOLUME = ? " +
					"and mi_successioni_d.NUMERO = ? " +
					"and mi_successioni_d.SOTTONUMERO = ? " +
					"and mi_successioni_d.COMUNE = ? " +
					//"and mi_successioni_d.PROGRESSIVO = ? " +
					"and mi_successioni_d.PROGRESSIVO_EREDE = ?";

			int indice = 1;
			this.setString(indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			//this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice));


			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());



			while (rs.next()){
				Eredita def = new Eredita();
				//chiave
				def.setUfficio(rs.getString("UFFICIO"));
				def.setAnno(rs.getString("ANNO"));
				def.setVolume(rs.getString("VOLUME"));
				def.setNumero(rs.getString("NUMERO"));
				def.setSottonumero(rs.getString("SOTTONUMERO"));
				def.setComune(rs.getString("COMUNE"));
				def.setProgressivo(rs.getString("PROGRESSIVO"));
				def.setProgressivoErede(rs.getString("PROGRESSIVO_EREDE"));
				//defunto
				def.setCfDefunto(rs.getString("CF_DEFUNTO"));
				def.setCognome(rs.getString("COGNOME_DEFUNTO"));
				def.setNome(rs.getString("NOME_DEFUNTO"));
				def.setProgressivoDef(rs.getString("PROGRESSIVO_DEF"));
				//erede
				def.setNumeratoreQuotaEre(rs.getString("NUM_QUOTA_EREDE"));
				def.setDenominatoreQuotaEre(rs.getString("DEN_QUOTA_EREDE"));
				def.setProgressivoErede(rs.getString("PROGRESSIVO_EREDE"));
				//oggetto
				def.setFoglio(rs.getString("FOGLIO"));
				def.setParticella(rs.getString("PARTICELLA1"));
				def.setSubalterno(rs.getString("SUBALTERNO1"));
				def.setProgressivoImmobile(rs.getString("PROGRESSIVO_IMMOBILE"));
				def.setNumeratoreQuotaDef(rs.getString("NUM_QUOTA_DEF"));
				def.setDenominatoreQuotaDef(rs.getString("DEN_QUOTA_DEF"));
				vct.add(def);
			}
			ht.put("LISTA",vct);
			
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


}
