/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.licenzeCommercio.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.licenzeCommercio.bean.LicenzaCommercio;
import it.escsolution.escwebgis.licenzeCommercio.bean.LicenzaCommercioFinder;
import it.escsolution.escwebgis.licenzeCommercio.bean.SoggettoLC;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LicenzeCommercioLogic extends EscLogic{
	private String appoggioDataSource;
	public LicenzeCommercioLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	private static String SQL_SELECT_LISTA = "select * from ( " +
			"select ROWNUM as N,CODENTE,ID_LIC, NUM_ESERCIZIO,PROVENIENZA,TIPOLOGIA,SEDIME,NOMEVIA,NCIV from (" +
			"select DISTINCT sit_licenze_commercio.CODENTE," +
			"sit_licenze_commercio.PK_IDLIC as ID_LIC," +
			"sit_licenze_commercio.NUM_ESERCIZIO," +
			"sit_licenze_commercio.PROVENIENZA," +
			"sit_licenze_commercio.TIPOLOGIA," +
			"sit_licenze_commercio.NCIV," +
			"sit_licenze_commercio_vie.SEDIME," +
			"sit_licenze_commercio_vie.DESCRIZIONE as NOMEVIA " +
			"FROM sit_licenze_commercio, sit_licenze_commercio_a, sit_licenze_commercio_vie " +
			"WHERE 1=? " +
			"AND sit_licenze_commercio.CODENTE = sit_licenze_commercio_a.CODENTE (+) " +
			"AND sit_licenze_commercio.FK_IDLIC_A = sit_licenze_commercio_a.PK_IDLIC_A (+) " +
			"AND sit_licenze_commercio.PROVENIENZA = sit_licenze_commercio_a.PROVENIENZA (+) " +
			"AND sit_licenze_commercio.FK_CODICEVIA = sit_licenze_commercio_vie.PK_CODICEVIA (+)" ;

	private static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  " +
			//"FROM sit_licenze_commercio, sit_licenze_commercio_a, sit_licenze_commercio_vie " +
			"FROM ( select DISTINCT sit_licenze_commercio.CODENTE," +
			"sit_licenze_commercio.PK_IDLIC as ID_LIC," +
			"sit_licenze_commercio.NUM_ESERCIZIO," +
			"sit_licenze_commercio.PROVENIENZA," +
			"sit_licenze_commercio.TIPOLOGIA," +
			"sit_licenze_commercio.NCIV," +
			"sit_licenze_commercio_vie.SEDIME," +
			"sit_licenze_commercio_vie.DESCRIZIONE as NOMEVIA " +
			"FROM sit_licenze_commercio, sit_licenze_commercio_a, sit_licenze_commercio_vie " +
			"WHERE 1=? " +
			"AND sit_licenze_commercio.CODENTE = sit_licenze_commercio_a.CODENTE (+) " +
			"AND sit_licenze_commercio.FK_IDLIC_A = sit_licenze_commercio_a.PK_IDLIC_A (+) " +
			"AND sit_licenze_commercio.PROVENIENZA = sit_licenze_commercio_a.PROVENIENZA (+) " +
			"AND sit_licenze_commercio.FK_CODICEVIA = sit_licenze_commercio_vie.PK_CODICEVIA (+)" ;

	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio from sit_licenze_commercio, sit_licenze_commercio_a, sit_licenze_commercio_vie WHERE 1=?" ;




	public Hashtable mCaricareLista(Vector listaPar, LicenzaCommercioFinder finder) throws Exception{

					String sqlLista = getProperty("sql.lista");
					String sqlCountLista = getProperty("sql.count.lista");
					if (sqlLista!=null)
						SQL_SELECT_LISTA = sqlLista;
					if (sqlCountLista!=null)
						SQL_SELECT_COUNT_LISTA = sqlCountLista;

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
								/*
								sql = SQL_SELECT_COUNT_ALL;
								int indice = 1;
								this.initialize();
								this.setInt(indice,1);
								indice ++;
								prepareStatement(sql);
								java.sql.ResultSet rs = executeQuery(conn);
								if (rs.next()){
										conteggione = rs.getLong("conteggio");
								}
								*/

								for (int i=0;i<=1;i++){
									// il primo ciclo faccio la count
									if (i==0)
										sql = SQL_SELECT_COUNT_LISTA;
									else
										sql = SQL_SELECT_LISTA;

										indice = 1;
										this.initialize();
										this.setInt(indice,1);
										indice ++;

								//il primo passaggio esegue la select count
								//campi della search

								// il primo passaggio esegue la select count
								if (finder.getKeyStr().equals("")){
									sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
								}
								else{
									//controllo provenienza chiamata
									String chiave = finder.getKeyStr();
									sql = sql + " and sit_licenze_commercio.PK_IDLIC in (" +chiave + ")" ;

								}

								long limInf, limSup;
								limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
								limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

								//SU TUTTE E DUE (COUNT + DATI)
								sql = sql + " ORDER BY sit_licenze_commercio.TIPOLOGIA,sit_licenze_commercio_vie.SEDIME,NOMEVIA,sit_licenze_commercio.NCIV)";

								//SOLO SU DATI
								if (i ==1){
									sql = sql + ") where N > " + limInf + " and N <=" + limSup;
								}

								prepareStatement(sql);
								rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

									if (i ==1) {
										while (rs.next()){
											//campi della lista
											LicenzaCommercio ogg = new LicenzaCommercio();
											ogg.setCodEnte(rs.getString("CODENTE"));
											ogg.setIdLicenza(rs.getString("ID_LIC"));
											ogg.setNumEsercizio(rs.getString("NUM_ESERCIZIO"));
											ogg.setTipologia(rs.getString("TIPOLOGIA"));
											ogg.setProvenienza(rs.getString("PROVENIENZA"));
											ogg.setNumCivico(rs.getString("NCIV"));
											ogg.setSedime(rs.getString("SEDIME"));
											ogg.setNomeVia(rs.getString("NOMEVIA"));

											vct.add(ogg);
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
								// TODO Auto-generated catch block
								log.error(e.getMessage(),e);
								throw e;
							}
							finally
							{
								if (conn != null && !conn.isClosed())
									conn.close();
							}
						}


	public Hashtable mCaricareDettaglio(String chiave) throws Exception{


		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
			conn = this.getConnection();

			this.initialize();
			String sql = "select sit_licenze_commercio.CODENTE," +
						 "sit_licenze_commercio.PK_IDLIC as ID_LIC," +
						 "sit_licenze_commercio.NUM_ESERCIZIO," +
						 "sit_licenze_commercio.PROVENIENZA," +
						 "sit_licenze_commercio.TIPOLOGIA," +
						 "sit_licenze_commercio.SUP_MINUTO," +
						 "sit_licenze_commercio.SUP_TOTALE," +
						 "nvl(sit_licenze_commercio_a.COGNOME,'-') as COGNOME," +
						 "nvl(sit_licenze_commercio_a.NOME,'-') as NOME," +
						 "nvl(sit_licenze_commercio_a.CODFISC,'-') as CODFISC," +
						 "nvl(sit_licenze_commercio_a.FORMA_GIURIDICA,'-') as FORMA_GIURIDICA," +
						 "nvl(sit_licenze_commercio_a.INDIRIZZO_RESIDENZA,'-') as INDIRIZZO_SOGG," +
						 "nvl(sit_licenze_commercio_a.COMUNE_RESIDENZA,'-') as COMUNE_SOGG," +
						 "sit_licenze_commercio_vie.SEDIME," +
						 "sit_licenze_commercio_vie.DESCRIZIONE as NOMEVIA," +
						 "sit_licenze_commercio.NCIV " +
						 "from sit_licenze_commercio, sit_licenze_commercio_a, sit_licenze_commercio_vie " +
						 "where sit_licenze_commercio.CODENTE = sit_licenze_commercio_a.CODENTE (+) " +
						 "AND sit_licenze_commercio.PROVENIENZA = sit_licenze_commercio_a.PROVENIENZA (+) " +
						 "and sit_licenze_commercio.FK_CODICEVIA = sit_licenze_commercio_vie.PK_CODICEVIA (+) " +
						 "and sit_licenze_commercio.PK_IDLIC = ? ";


			// IL FOX MI HA CHIESTO DI METTERE UNA IF, E IO UNA IF METTO !!!
			if (conn.prepareStatement("select uk_belfiore from ewg_tab_comuni where UK_BELFIORE='F205'").executeQuery().next()) {
				 sql += "and sit_licenze_commercio.num_esercizio = sit_licenze_commercio_a.NUM_ESERCIZIO (+) ";
			} else {
				 sql += "and sit_licenze_commercio.FK_IDLIC_A=sit_licenze_commercio_a.PK_IDLIC_A (+) ";
			}

			sql += "ORDER BY sit_licenze_commercio.TIPOLOGIA,sit_licenze_commercio_vie.SEDIME,NOMEVIA,sit_licenze_commercio.NCIV,COGNOME,NOME";

			int indice = 1;
			this.setString(indice,chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			LicenzaCommercio ogg = new LicenzaCommercio();
			ht.put("DETTAGLIO",ogg);
			ArrayList listaSoggLC = new ArrayList();
			ht.put("LISTA_SOGGETTI_LC", listaSoggLC);

			if (rs.next()){
				ogg.setCodEnte(rs.getString("CODENTE"));
				ogg.setIdLicenza(rs.getString("ID_LIC"));
				ogg.setNumEsercizio(rs.getString("NUM_ESERCIZIO"));
				ogg.setProvenienza(rs.getString("PROVENIENZA"));
				ogg.setTipologia(rs.getString("TIPOLOGIA"));
				ogg.setNumCivico(rs.getString("NCIV"));
				ogg.setSupMinuto(rs.getString("SUP_MINUTO"));
				ogg.setSupTotale(rs.getString("SUP_TOTALE"));
				ogg.setSedime(rs.getString("SEDIME"));
				ogg.setNomeVia(rs.getString("NOMEVIA"));
				do{
					SoggettoLC sogg = new SoggettoLC();
					sogg.setNome(rs.getString("NOME"));
					sogg.setCognome(rs.getString("COGNOME"));
					sogg.setCodiceFiscale(rs.getString("CODFISC"));
					sogg.setIndirizzoResidenza(rs.getString("INDIRIZZO_SOGG"));
					sogg.setComuneResidenza(rs.getString("COMUNE_SOGG"));
					sogg.setFormaGiuridica(rs.getString("FORMA_GIURIDICA"));
					listaSoggLC.add(sogg);
				} while (rs.next());

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
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}


}
