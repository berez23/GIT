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
import it.escsolution.escwebgis.successioni.bean.Eredita;
import it.escsolution.escwebgis.successioni.bean.Oggetto;
import it.escsolution.escwebgis.successioni.bean.OggettoFinder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.dbutils.DbUtils;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SuccessioniOggettiLogic extends SuccessioniLogic{
	private String appoggioDataSource;
	
	public SuccessioniOggettiLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	private final static String SQL_SELECT_LISTA = 
			"select DISTINCT " +
			"decode(FOGLIO,null,'-',FOGLIO) AS FOGLIO," +
			"decode(PARTICELLA1,null,'-',PARTICELLA1) AS PARTICELLA1," +
			"decode(SUBALTERNO1,null,'-',SUBALTERNO1) AS SUBALTERNO1," +
			"decode(INDIRIZZO_IMMOBILE,null,'-',INDIRIZZO_IMMOBILE) AS INDIRIZZO_IMMOBILE," +
			"decode(UFFICIO,null,'-',UFFICIO) AS UFFICIO," +
			"decode(ANNO,null,'-',ANNO) AS ANNO," +
			"decode(VOLUME,null,'-',VOLUME) AS VOLUME," +
			"decode(NUMERO,null,'-',NUMERO) AS NUMERO," +
			"decode(SOTTONUMERO,null,'-',SOTTONUMERO) AS SOTTONUMERO," +
			"decode(COMUNE,null,'-',COMUNE) AS COMUNE," +
			"decode(PROGRESSIVO,null,'-',PROGRESSIVO) AS PROGRESSIVO, " +
			"decode(PROGRESSIVO_IMMOBILE,null,'-',PROGRESSIVO_IMMOBILE) AS PROGRESSIVO_IMMOBILE " +
			"from mi_successioni_c WHERE 1=?" ;

	
	private final static String SQL_SELECT_LISTA_CIV = 
			"select * from ( select ROWNUM as N, S.* from (" +
				"select DISTINCT " +
				"decode(FOGLIO,null,'-',FOGLIO) AS FOGLIO," +
				"decode(PARTICELLA1,null,'-',PARTICELLA1) AS PARTICELLA1," +
				"decode(SUBALTERNO1,null,'-',SUBALTERNO1) AS SUBALTERNO1," +
				"decode(INDIRIZZO_IMMOBILE,null,'-',INDIRIZZO_IMMOBILE) AS INDIRIZZO_IMMOBILE," +
				"decode(UFFICIO,null,'-',UFFICIO) AS UFFICIO," +
				"decode(ANNO,null,'-',ANNO) AS ANNO," +
				"decode(VOLUME,null,'-',VOLUME) AS VOLUME," +
				"decode(NUMERO,null,'-',NUMERO) AS NUMERO," +
				"decode(SOTTONUMERO,null,'-',SOTTONUMERO) AS SOTTONUMERO," +
				"decode(COMUNE,null,'-',COMUNE) AS COMUNE," +
				"decode(PROGRESSIVO,null,'-',PROGRESSIVO) AS PROGRESSIVO, " +
				"decode(PROGRESSIVO_IMMOBILE,null,'-',PROGRESSIVO_IMMOBILE) AS PROGRESSIVO_IMMOBILE " +
				"from mi_successioni_c " +
				"WHERE ufficio=? and anno=? and volume=? and numero=? and sottonumero=? " +
				"and comune=? and progressivo=? and progressivo_immobile=?" +
			") S )" ;

	public Hashtable mCaricareListaCiv(String oggettoSel) throws Exception{
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
			sql = SQL_SELECT_LISTA_CIV;

			OggettoFinder finder = new OggettoFinder();
			this.initialize();
			StringTokenizer st = new StringTokenizer(oggettoSel, "|");
			
			this.setString(1, st.nextToken());
			this.setString(2, st.nextToken());
			this.setString(3, st.nextToken());
			this.setString(4, st.nextToken());
			this.setString(5, st.nextToken());
			this.setString(6, st.nextToken());
			this.setString(7, st.nextToken());
			this.setString(8, st.nextToken());
			
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next()){
				//campi della lista
				Oggetto def = new Oggetto();
				def.setFoglio(rs.getString("FOGLIO"));
				def.setParticella(rs.getString("PARTICELLA1"));
				def.setSubalterno(rs.getString("SUBALTERNO1"));
				def.setIndirizzoImmobile(rs.getString("INDIRIZZO_IMMOBILE"));
				//chiave
				def.setUfficio(rs.getString("UFFICIO"));
				def.setAnno(rs.getString("ANNO"));
				def.setVolume(rs.getString("VOLUME"));
				def.setNumero(rs.getString("NUMERO"));
				def.setSottonumero(rs.getString("SOTTONUMERO"));
				def.setComune(rs.getString("COMUNE"));
				def.setProgressivo(rs.getString("PROGRESSIVO"));
				def.setProgressivoImmobile(rs.getString("PROGRESSIVO_IMMOBILE"));
				vct.add(def);
			}
					
			ht.put("LISTA",vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put("FINDER",finder);
			
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
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally{
			DbUtils.close(conn);
		}
	}
	
	public Hashtable mCaricareLista(Vector listaPar, OggettoFinder finder) throws Exception{
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
					sql = "select count(*) as conteggio  from (" + SQL_SELECT_LISTA;
				else
					sql = "select * from ( select ROWNUM as N, S.* from (" + SQL_SELECT_LISTA;

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
			else
			{
				sql = sql + " AND mi_successioni_c.foglio||'|'||"+
				" mi_successioni_c.particella1||'|'||"+
				" mi_successioni_c.subalterno1 IN( " + finder.getKeyStr() + ")";


			}


				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i ==1){
					sql = sql + " order by FOGLIO,PARTICELLA1,SUBALTERNO1";
					sql = sql + ") S ) where N > " + limInf + " and N <=" + limSup;
				}else
					sql += ")";

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						Oggetto def = new Oggetto();
						def.setFoglio(rs.getString("FOGLIO"));
						def.setParticella(rs.getString("PARTICELLA1"));
						def.setSubalterno(rs.getString("SUBALTERNO1"));
						def.setIndirizzoImmobile(rs.getString("INDIRIZZO_IMMOBILE"));
						//chiave
						def.setUfficio(rs.getString("UFFICIO"));
						def.setAnno(rs.getString("ANNO"));
						def.setVolume(rs.getString("VOLUME"));
						def.setNumero(rs.getString("NUMERO"));
						def.setSottonumero(rs.getString("SOTTONUMERO"));
						def.setComune(rs.getString("COMUNE"));
						def.setProgressivo(rs.getString("PROGRESSIVO"));
						def.setProgressivoImmobile(rs.getString("PROGRESSIVO_IMMOBILE"));

						vct.add(def);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put("LISTA",vct);
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
		finally
		{
			DbUtils.close(conn);
		}
	}


	public Hashtable mCaricareDettaglio(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
			//recupero le chiavi di ricerca
			List<String> listParam = new ArrayList<String>();
			if(chiave.indexOf('|') > 0)
				listParam = Arrays.asList(chiave.split("\\|"));
			else
				listParam.add(chiave);

			conn = this.getConnection();
			this.initialize();
			String sql = "select DISTINCT " +
					     "decode(FOGLIO,null,'-',FOGLIO) AS FOGLIO," +
					     "decode(PARTICELLA1,null,'-',PARTICELLA1) AS PARTICELLA1," +
					     "decode(SUBALTERNO1,null,'-',SUBALTERNO1) AS SUBALTERNO1," +
					     "decode(INDIRIZZO_IMMOBILE,null,'-',INDIRIZZO_IMMOBILE) AS INDIRIZZO_IMMOBILE," +
					     "decode(UFFICIO,null,'-',UFFICIO) AS UFFICIO," +
					     "decode(ANNO,null,'-',ANNO) AS ANNO," +
					     "decode(VOLUME,null,'-',VOLUME) AS VOLUME," +
					     "decode(NUMERO,null,'-',NUMERO) AS NUMERO," +
					     "decode(SOTTONUMERO,null,'-',SOTTONUMERO) AS SOTTONUMERO," +
					     "decode(COMUNE,null,'-',COMUNE) AS COMUNE," +
					     "decode(PROGRESSIVO,null,'-',PROGRESSIVO) AS PROGRESSIVO, " +
					     "decode(PROGRESSIVO_IMMOBILE,null,'-',PROGRESSIVO_IMMOBILE) AS PROGRESSIVO_IMMOBILE, " +
					     "decode(NUMERATORE_QUOTA_DEF,null,'-',NUMERATORE_QUOTA_DEF) AS NUMERATORE_QUOTA_DEF, " +
						 "decode(DENOMINATORE_QUOTA_DEF,null,'-',DENOMINATORE_QUOTA_DEF) AS DENOMINATORE_QUOTA_DEF, " +
					     "fornitura AS FORNITURA from mi_successioni_c " +
					     "WHERE UFFICIO = ? " +
					     "AND  ANNO = ? " +
					     "AND  VOLUME = ? " +
					     "AND  NUMERO = ? " +
					     "AND  SOTTONUMERO = ? " +
					     "AND  COMUNE = ? " +
					     "AND  PROGRESSIVO = ? " +
					     "AND  PROGRESSIVO_IMMOBILE = ? ";

			int indice = 1;
			this.setString(indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));


			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Oggetto det = new Oggetto();

			if (rs.next()){
				det.setFoglio(rs.getString("FOGLIO"));
				det.setParticella(rs.getString("PARTICELLA1"));
				det.setSubalterno(rs.getString("SUBALTERNO1"));
				det.setIndirizzoImmobile(rs.getString("INDIRIZZO_IMMOBILE"));
				//chiave
				det.setUfficio(rs.getString("UFFICIO"));
				det.setAnno(rs.getString("ANNO"));
				det.setVolume(rs.getString("VOLUME"));
				det.setNumero(rs.getString("NUMERO"));
				det.setSottonumero(rs.getString("SOTTONUMERO"));
				det.setComune(rs.getString("COMUNE"));
				det.setProgressivo(rs.getString("PROGRESSIVO"));
				det.setProgressivoImmobile(rs.getString("PROGRESSIVO_IMMOBILE"));
				String num = rs.getString("NUMERATORE_QUOTA_DEF");
				String den = rs.getString("DENOMINATORE_QUOTA_DEF");
				num = num.replace(',','.');
				den = den.replace(',','.');
				float numd = Float.parseFloat(num);
				float dend = Float.parseFloat(den);
				String snum = Float.toString(numd);
				String sden = Float.toString(dend);
				det.setNumeratoreQuotaDef(snum);
				det.setDenominatoreQuotaDef(sden);
				det.setFornitura(rs.getString("FORNITURA"));

			}
			ht.put("DETTAGLIO",det);
			
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

	public Hashtable mCaricareListaEredita(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		Connection conn = null;

		try {
			//recupero le chiavi di ricerca
			List<String> listParam = new ArrayList<String>();
			if(chiave.indexOf('|') > 0)
				listParam = Arrays.asList(chiave.split("\\|"));
			else
				listParam.add(chiave);

			conn = this.getConnection();
			this.initialize();
			String sql = "select distinct " +
						"mi_successioni_a.CF_DEFUNTO," +
						"mi_successioni_a.COGNOME_DEFUNTO," +
						"mi_successioni_a.NOME_DEFUNTO," +
						"DECODE( SUBSTR (RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0'),-1,1 ),',', SUBSTR (RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0'),1,LENGTH(RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0'))-1  ) ,RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0')   ) as NUM_QUOTA_DEF," +
						"DECODE( SUBSTR (LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0'),-1,1 ),',', SUBSTR (LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0'),1,LENGTH(LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0'))-1  ) ,LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0')   ) as DEN_QUOTA_DEF," +
						"mi_successioni_b.CF_EREDE," +
						"mi_successioni_b.DENOMINAZIONE," +
						"mi_successioni_b.PROGRESSIVO_EREDE," +
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
						"mi_successioni_d.PROGRESSIVO_IMMOBILE " +
						"from mi_successioni_d,mi_successioni_a,mi_successioni_c,mi_successioni_b " +
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
						"and mi_successioni_d.UFFICIO = mi_successioni_b.UFFICIO " +
						"and mi_successioni_d.ANNO = mi_successioni_b.ANNO " +
						"and mi_successioni_d.VOLUME = mi_successioni_b.VOLUME " +
						"and mi_successioni_d.NUMERO =  mi_successioni_b.NUMERO " +
						"and mi_successioni_d.SOTTONUMERO = mi_successioni_b.SOTTONUMERO " +
						"and mi_successioni_d.COMUNE = mi_successioni_b.COMUNE " +
						//"and mi_successioni_d.PROGRESSIVO = mi_successioni_b.PROGRESSIVO " +
						"and mi_successioni_d.PROGRESSIVO_EREDE = mi_successioni_b.PROGRESSIVO_EREDE " +
						"and mi_successioni_d.UFFICIO = ? " +
						"and mi_successioni_d.ANNO = ? " +
						"and mi_successioni_d.VOLUME = ? " +
						"and mi_successioni_d.NUMERO = ? " +
						"and mi_successioni_d.SOTTONUMERO = ? " +
						"and mi_successioni_d.COMUNE = ? " +
						//"and mi_successioni_d.PROGRESSIVO = ? " +
						"and mi_successioni_d.PROGRESSIVO_IMMOBILE = ?";

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
				//defunto
				def.setCfDefunto(rs.getString("CF_DEFUNTO"));
				def.setCognome(rs.getString("COGNOME_DEFUNTO"));
				def.setNome(rs.getString("NOME_DEFUNTO"));
				def.setProgressivoDef(rs.getString("PROGRESSIVO_DEF"));
				//erede
				def.setNumeratoreQuotaEre(rs.getString("NUM_QUOTA_EREDE"));
				def.setDenominatoreQuotaEre(rs.getString("DEN_QUOTA_EREDE"));
				def.setProgressivoErede(rs.getString("PROGRESSIVO_EREDE"));
				def.setDenominazione(rs.getString("DENOMINAZIONE"));
				def.setCfErede(rs.getString("CF_EREDE"));
				//oggetto
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
			DbUtils.close(conn);
		}
	}

	
	
}
