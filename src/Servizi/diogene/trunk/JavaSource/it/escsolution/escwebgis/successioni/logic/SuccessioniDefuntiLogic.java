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
import it.escsolution.escwebgis.successioni.bean.Defunto;
import it.escsolution.escwebgis.successioni.bean.DefuntoFinder;
import it.escsolution.escwebgis.successioni.bean.Eredita;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SuccessioniDefuntiLogic extends SuccessioniLogic{
	private String appoggioDataSource;
	public SuccessioniDefuntiLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	private final static String SQL_SELECT_LISTA = 
			"select distinct decode(CF_DEFUNTO,null,'-',CF_DEFUNTO) AS CF_DEFUNTO," +
			"decode(COGNOME_DEFUNTO,null,'-',COGNOME_DEFUNTO) AS COGNOME," +
			"decode(NOME_DEFUNTO,null,'-',NOME_DEFUNTO) AS NOME, " +
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
			"decode(DATA_APERTURA,null,'-',DATA_APERTURA) AS DATA_APERTURA " +
			"from mi_successioni_a  WHERE 1=?" ;


	public Hashtable mCaricareListaDefunti(Vector listaPar, DefuntoFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		
		//controllo date apertura da/a
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro elementoFiltro = (EscElementoFiltro)listaPar.get(i);
			if (elementoFiltro.getAttributeName() != null && 
			(elementoFiltro.getAttributeName().equals("DATA_APERTURA_DA") || elementoFiltro.getAttributeName().equals("DATA_APERTURA_A"))) {
				String valore = elementoFiltro.getValore();
				if (valore != null && !valore.equals("")) {					
					StringBuffer sb = new StringBuffer();
					sb.append(valore.substring(6, 10));
					sb.append("-");
					sb.append(valore.substring(3, 5));
					sb.append("-");
					sb.append(valore.substring(0, 2));
					elementoFiltro.setValore(sb.toString());
				}
			}
		}

		// faccio la connessione al db
		try {
				conn = this.getConnection();
				int indice = 1;
				java.sql.ResultSet rs = null;
	
				for (int i=0;i<=1;i++){
					// il primo ciclo faccio la count
					if (i==0)
						sql = "select count(*) as conteggio from (" + SQL_SELECT_LISTA;
					else
					    sql = "select * from ( select ROWNUM as N, A.* from (" + SQL_SELECT_LISTA;
	
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
					}else{
						//controllo provenienza chiamata
						String controllo = finder.getKeyStr();
						String chiavi = "";
						if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
							String ente = controllo.substring(0,controllo.indexOf("|"));
							ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
							chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
						}else
							chiavi = controllo;
		
						sql = sql + " and mi_successioni_A.PK_ID_SUCCA in (" +chiavi + ")" ;
		
					}
		
		
					long limInf, limSup;
					limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
					limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
		
					if (i == 0){
						sql = sql + ")";
					} else {
						sql = sql + " order by COGNOME, NOME";
						sql = sql + ") A ) where N > " + limInf + " and N <=" + limSup;
					}
		
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		
					if (i ==1) {
						while (rs.next()){
							//campi della lista
							Defunto def = new Defunto();
							def.setCodFiscale(rs.getString("CF_DEFUNTO"));
							def.setCognome(rs.getString("COGNOME"));
							def.setNome(rs.getString("NOME"));
							def.setComuneNascita(rs.getString("CITTA_NASCITA"));
							def.setProvNascita(rs.getString("PROV_NASCITA"));
							def.setDataNascita(tornaValoreRS(rs,"DATA_NASCITA","YYYY-MM-DD"));
							def.setSesso(rs.getString("SESSO"));
							def.setDataApertura(tornaValoreRS(rs,"DATA_APERTURA","YYYY-MM-DD"));
							//chiave
							def.setUfficio(rs.getString("UFFICIO"));
							def.setAnno(rs.getString("ANNO"));
							def.setVolume(rs.getString("VOLUME"));
							def.setNumero(rs.getString("NUMERO"));
							def.setSottonumero(rs.getString("SOTTONUMERO"));
							def.setComune(rs.getString("COMUNE"));
							def.setProgressivo(rs.getString("PROGRESSIVO"));
							
							vct.add(def);
						}
					}
					else{
						if (rs.next())
							conteggio = rs.getString("conteggio");
					}
			}
			ht.put("LISTA_DEFUNTI",vct);
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
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}


	public Hashtable mCaricareDettaglioDefunto(String chiave) throws Exception{

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
						 "pk_id_succa as ID," +
						 "decode(CF_DEFUNTO,null,'-',CF_DEFUNTO) AS CF_DEFUNTO," +
					     "decode(COGNOME_DEFUNTO,null,'-',COGNOME_DEFUNTO) AS COGNOME," +
					     "decode(NOME_DEFUNTO,null,'-',NOME_DEFUNTO) AS NOME," +
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
					     "decode(DATA_APERTURA,null,'-',DATA_APERTURA) AS DATA_APERTURA " +
					     "from mi_successioni_a " +
					     "WHERE UFFICIO = ? " +
					     "AND  ANNO = ? " +
					     "AND  VOLUME = ? " +
					     "AND  NUMERO = ? " +
					     "AND  SOTTONUMERO = ? " +
					     "AND  COMUNE = ? " +
					     "AND  PROGRESSIVO = ? ";

			int indice = 1;
			this.setString(indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));


			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Defunto def = new Defunto();

			if (rs.next()){
				def.setCodFiscale(rs.getString("CF_DEFUNTO"));
				def.setCognome(rs.getString("COGNOME"));
				def.setNome(rs.getString("NOME"));
				def.setComuneNascita(rs.getString("CITTA_NASCITA"));
				def.setProvNascita(rs.getString("PROV_NASCITA"));
				def.setDataNascita(tornaValoreRS(rs,"DATA_NASCITA","YYYY-MM-DD"));
				def.setSesso(rs.getString("SESSO"));
				def.setDataApertura(tornaValoreRS(rs,"DATA_APERTURA","YYYY-MM-DD"));
				//chiave
				def.setUfficio(rs.getString("UFFICIO"));
				def.setAnno(rs.getString("ANNO"));
				def.setVolume(rs.getString("VOLUME"));
				def.setNumero(rs.getString("NUMERO"));
				def.setSottonumero(rs.getString("SOTTONUMERO"));
				def.setComune(rs.getString("COMUNE"));
				def.setProgressivo(rs.getString("PROGRESSIVO"));
				
				def.setId(rs.getString("ID"));

			}
			ht.put("DEFUNTO",def);
			
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
	
	public Hashtable mCaricareDettaglioDefuntoFromSoggetto(String idSoggetto) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
			//recupero le chiavi di ricerca
			

			conn = this.getConnection();
			this.initialize();
			String sql = "select DISTINCT " +
					     "pk_id_succa as ID," +
					     "decode(CF_DEFUNTO,null,'-',CF_DEFUNTO) AS CF_DEFUNTO," +
					     "decode(COGNOME_DEFUNTO,null,'-',COGNOME_DEFUNTO) AS COGNOME," +
					     "decode(NOME_DEFUNTO,null,'-',NOME_DEFUNTO) AS NOME," +
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
					     "decode(PROGRESSIVO,null,'-',PROGRESSIVO) AS PROGRESSIVO " +
					     "from mi_successioni_a " +
					     "WHERE pk_id_succa = ? ";

			int indice = 1;
			this.setString(indice,(String)idSoggetto);
			

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Defunto def = new Defunto();

			if (rs.next()){
				def.setCodFiscale(rs.getString("CF_DEFUNTO"));
				def.setCognome(rs.getString("COGNOME"));
				def.setNome(rs.getString("NOME"));
				def.setComuneNascita(rs.getString("CITTA_NASCITA"));
				def.setProvNascita(rs.getString("PROV_NASCITA"));
				def.setDataNascita(tornaValoreRS(rs,"DATA_NASCITA","YYYY-MM-DD"));
				def.setSesso(rs.getString("SESSO"));
				//chiave
				def.setUfficio(rs.getString("UFFICIO"));
				def.setAnno(rs.getString("ANNO"));
				def.setVolume(rs.getString("VOLUME"));
				def.setNumero(rs.getString("NUMERO"));
				def.setSottonumero(rs.getString("SOTTONUMERO"));
				def.setComune(rs.getString("COMUNE"));
				def.setProgressivo(rs.getString("PROGRESSIVO"));
				
				def.setId(rs.getString("ID"));

			}
			ht.put("DEFUNTO",def);
			
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
			String sql = "select distinct " +
					 	 "mi_successioni_c.UFFICIO," +
						 "mi_successioni_c.ANNO," +
						 "mi_successioni_c.VOLUME," +
						 "mi_successioni_c.NUMERO," +
						 "mi_successioni_c.SOTTONUMERO," +
						 "mi_successioni_c.COMUNE," +
						 "mi_successioni_c.PROGRESSIVO," +
						 "DECODE(mi_successioni_c.FOGLIO,NULL,'-',mi_successioni_c.FOGLIO) AS FOGLIO," +
						 "DECODE(mi_successioni_c.PARTICELLA1,NULL,'-',mi_successioni_c.PARTICELLA1) AS PARTICELLA1," +
						 "DECODE(mi_successioni_c.SUBALTERNO1,NULL,'-',mi_successioni_c.SUBALTERNO1) AS SUBALTERNO1," +
						 "mi_successioni_c.PROGRESSIVO_IMMOBILE," +
						 "mi_successioni_c.PROGRESSIVO_PARTICELLA," +
						 "mi_successioni_d.PROGRESSIVO_EREDE," +
						 "DECODE( SUBSTR (RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0'),-1,1 ),',', SUBSTR (RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0'),1,LENGTH(RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0'))-1  )  ,RTRIM(LTRIM(mi_successioni_c.NUMERATORE_QUOTA_DEF,'0'),'0')   ) as NUM_QUOTA_DEF," +
						 "DECODE( SUBSTR (LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0'),-1,1 ),',', SUBSTR (LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0'),1,LENGTH(LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0'))-1  )  ,LTRIM(mi_successioni_c.DENOMINATORE_QUOTA_DEF,'0')   ) as DEN_QUOTA_DEF," +
						 "DECODE( SUBSTR (RTRIM(LTRIM(mi_successioni_d.NUMERATORE_QUOTA,'0'),'0'),-1,1 ),',', SUBSTR (RTRIM(LTRIM(mi_successioni_d.NUMERATORE_QUOTA,'0'),'0'),1,LENGTH(RTRIM(LTRIM(mi_successioni_d.NUMERATORE_QUOTA,'0'),'0'))-1  )  ,RTRIM(LTRIM(mi_successioni_d.NUMERATORE_QUOTA,'0'),'0')   ) as NUM_QUOTA_EREDE," +
						 "DECODE( SUBSTR (LTRIM(mi_successioni_d.DENOMINATORE_QUOTA,'0'),-1,1 ),',', SUBSTR (LTRIM(mi_successioni_d.DENOMINATORE_QUOTA,'0'),1,LENGTH(LTRIM(mi_successioni_d.DENOMINATORE_QUOTA,'0'))-1  )  ,LTRIM(mi_successioni_d.DENOMINATORE_QUOTA,'0')   ) as DEN_QUOTA_EREDE," +
						 "mi_successioni_b.CF_EREDE," +
						 "mi_successioni_b.DENOMINAZIONE " +
						 "from mi_successioni_d ,mi_successioni_c,mi_successioni_b " +
						 "where mi_successioni_d.UFFICIO = mi_successioni_c.UFFICIO " +
						 "and mi_successioni_d.ANNO = mi_successioni_c.ANNO " +
						 "and mi_successioni_d.VOLUME = mi_successioni_c.VOLUME " +
						 "and mi_successioni_d.NUMERO =  mi_successioni_c.NUMERO " +
						 "and mi_successioni_d.SOTTONUMERO = mi_successioni_c.SOTTONUMERO " +
						 "and mi_successioni_d.COMUNE = mi_successioni_c.COMUNE " +
						 //"and mi_successioni_d.PROGRESSIVO = mi_successioni_c.PROGRESSIVO " +
						 "and mi_successioni_d.PROGRESSIVO_IMMOBILE = mi_successioni_c.PROGRESSIVO_IMMOBILE " +
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
						 "order by mi_successioni_c.UFFICIO," +
						 "mi_successioni_c.ANNO," +
						 "mi_successioni_c.VOLUME," +
						 "mi_successioni_c.NUMERO," +
						 "mi_successioni_c.SOTTONUMERO," +
						 "mi_successioni_c.COMUNE," +
						 //"mi_successioni_c.PROGRESSIVO," +
						 "DECODE(mi_successioni_c.FOGLIO,NULL,'-',mi_successioni_c.FOGLIO)," +
						 "DECODE(mi_successioni_c.PARTICELLA1,NULL,'-',mi_successioni_c.PARTICELLA1)," +
						 "DECODE(mi_successioni_c.SUBALTERNO1,NULL,'-',mi_successioni_c.SUBALTERNO1)," +
						 "mi_successioni_c.PROGRESSIVO_IMMOBILE";

			int indice = 0;
			if (listParam.size() >= 6)
			{
				this.setString(++indice,(String)listParam.get(indice-1));
				this.setString(++indice,(String)listParam.get(indice-1));
				this.setString(++indice,(String)listParam.get(indice-1));
				this.setString(++indice,(String)listParam.get(indice-1));
				this.setString(++indice,(String)listParam.get(indice-1));
				this.setString(++indice,(String)listParam.get(indice-1));
			}
			else
			{
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
			}
			//this.setString(++indice,(String)listParam.get(indice-1));


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
				//erede
				def.setCfErede(rs.getString("CF_EREDE"));
				def.setNumeratoreQuotaEre(rs.getString("NUM_QUOTA_EREDE"));
				def.setDenominatoreQuotaEre(rs.getString("DEN_QUOTA_EREDE"));
				def.setDenominazione(rs.getString("DENOMINAZIONE"));
				def.setProgressivoErede(rs.getString("PROGRESSIVO_EREDE"));
				//oggetto
				def.setFoglio(rs.getString("FOGLIO"));
				def.setParticella(rs.getString("PARTICELLA1"));
				def.setSubalterno(rs.getString("SUBALTERNO1"));
				def.setProgressivoImmobile(rs.getString("PROGRESSIVO_IMMOBILE"));
				def.setProgressivoParticella(rs.getString("PROGRESSIVO_PARTICELLA"));
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
