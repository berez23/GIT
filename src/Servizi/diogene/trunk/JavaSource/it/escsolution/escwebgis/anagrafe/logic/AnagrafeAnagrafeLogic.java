/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.logic;

import it.escsolution.escwebgis.anagrafe.bean.Anagrafe;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeFinder;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

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
public class AnagrafeAnagrafeLogic extends EscLogic{
	private String appoggioDataSource;
	public AnagrafeAnagrafeLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	public static final String FINDER = "FINDER";
	public static final String ANAGRAFE = "ANAGRAFE";
	public static final String LISTA_ANAGRAFE = "LISTA_ANAGRAFE";
	public static final String LISTA_COMPONENTI_FAMIGLIA = "LISTA_COMPONENTI_FAMIGLIA";
	public static final String LISTA_ANAGRAFE2 = "LISTA_ANAGRAFE2";


	private final static String SQL_SELECT_LISTA = "select * from (" +
	"select ROWNUM as N,cognome, nome,COD_ANAGRAFE,CODICE_FISCALE,DATA_NASCITA, CODICE_NAZIONALE from (" +
	"select ROWNUM as N,decode(pop_anagrafe.PK_CODI_ANAGRAFE,null,'-',pop_anagrafe.PK_CODI_ANAGRAFE) AS COD_ANAGRAFE," +
	"decode(pop_anagrafe.COGNOME,null,'-',pop_anagrafe.COGNOME) AS COGNOME," +
	"decode(pop_anagrafe.NOME,null,'-',pop_anagrafe.NOME) AS NOME," +
	"decode(pop_anagrafe.CODICE_FISCALE,null,'-',pop_anagrafe.CODICE_FISCALE) AS CODICE_FISCALE," +
	"nvl(to_char(pop_anagrafe.DATA_NASCITA,'dd/mm/yyyy'),'-') AS DATA_NASCITA, " +
	"CODICE_NAZIONALE AS CODICE_NAZIONALE" +
	//SISANI: cambio decode DATA_NASCITA
	//"decode(pop_anagrafe.DATA_NASCITA,null,'-',pop_anagrafe.DATA_NASCITA) AS DATA_NASCITA" +
	" from pop_anagrafe WHERE 1=?" ;

	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  from pop_anagrafe WHERE 1=?" ;

	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from pop_anagrafe WHERE 1=?" ;

	private final static String SQL_SELECT_ANAGRAFE_FAMIGLIA = "select decode(pop_anagrafe.PK_CODI_ANAGRAFE,null,'-',pop_anagrafe.PK_CODI_ANAGRAFE) AS COD_ANAGRAFE," +
	"decode(pop_anagrafe.COGNOME,null,'-',pop_anagrafe.COGNOME) AS COGNOME," +
	"decode(pop_anagrafe.NOME,null,'-',pop_anagrafe.NOME) AS NOME, " +
	"decode(pop_anagrafe.FK_COMUNI_NASCITA,null,'-',pop_anagrafe.FK_COMUNI_NASCITA) AS COMUNI_NASCITA," +
	"decode(pop_anagrafe.CODICE_FISCALE,null,'-',pop_anagrafe.CODICE_FISCALE) AS CODICE_FISCALE," +
	"nvl(to_char(pop_anagrafe.DATA_NASCITA,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
	//SISANI: cambio decode DATA_NASCITA
	//"decode(pop_anagrafe.DATA_NASCITA,null,'-',pop_anagrafe.DATA_NASCITA) AS DATA_NASCITA, " +
	"decode(pop_anagrafe.SESSO,null,'-',pop_anagrafe.SESSO) AS SESSO, " +
	"decode(pop_anagrafe.TIPO_SOGGETTO,null,'-',pop_anagrafe.TIPO_SOGGETTO) AS TIPO_SOGGETTO, " +
	"decode(pop_anagrafe.FK_CITTADINANZE,null,'-',pop_anagrafe.FK_CITTADINANZE) AS CITTADINANZA, " +
	"pop_anagrafe.codice_nazionale AS codice_nazionale " +
	"from pop_anagrafe, pop_famiglie WHERE pop_famiglie.PK_CODI_FAMIGLIE = ?  " +
	"and pop_anagrafe.FK_FAMIGLIE = pop_famiglie.PK_CODI_FAMIGLIE AND ROWNUM = 1";





	public Hashtable mCaricareListaAnagrafe(Vector listaPar, AnagrafeFinder finder) throws Exception{
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
								java.sql.ResultSet rs ;
								/* per ora disabilito il conteggione
								sql = SQL_SELECT_COUNT_ALL;
								this.initialize();
								this.setInt(indice,1);
								indice ++;
								prepareStatement(sql);
								rs = executeQuery(conn);
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
									String controllo = finder.getKeyStr();
									if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
										String ente = controllo.substring(0,controllo.indexOf("|"));
										ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
										String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
										sql = sql + " and pop_anagrafe.PK_CODI_ANAGRAFE in (" +chiavi + ")" ;
										sql = sql + " and pop_anagrafe.CODICE_NAZIONALE = '" +ente + "'" ;
									}else
										sql = sql + " and pop_anagrafe.PK_CODI_ANAGRAFE in (" +finder.getKeyStr() + ")" ;

								}


									long limInf, limSup;
									limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
									limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
									sql = sql + " order by cognome, nome";
									if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

									prepareStatement(sql);
									rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

									if (i ==1) {
										while (rs.next()){
											//campi della lista
											Anagrafe ana = new Anagrafe();
											ana.setCodAnagrafe(rs.getString("COD_ANAGRAFE"));
											ana.setCognome(rs.getString("COGNOME"));
											ana.setNome(rs.getString("NOME"));
											ana.setCodFiscale(rs.getString("CODICE_FISCALE"));
											ana.setDataNascita(rs.getString("DATA_NASCITA"));
											ana.setCodiceNazionale(rs.getString("CODICE_NAZIONALE"));
											vct.add(ana);
										}
									}
									else{
										if (rs.next()){
											conteggio = rs.getString("conteggio");
										}
									}
								}
								ht.put(LISTA_ANAGRAFE, vct);
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


	public Hashtable mCaricareDettaglioAnagrafe(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
					// faccio la connessione al db
					Connection conn = null;
					try {
						conn = this.getConnection();
						this.initialize();
						// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
						String sql = "select decode(pop_anagrafe.PK_CODI_ANAGRAFE,null,'-',pop_anagrafe.PK_CODI_ANAGRAFE) AS COD_ANAGRAFE," +
								"decode(pop_anagrafe.COGNOME,null,'-',pop_anagrafe.COGNOME) AS COGNOME," +
								"decode(pop_anagrafe.NOME,null,'-',pop_anagrafe.NOME) AS NOME, " +
								"decode(pop_anagrafe.DESCR_COMUNE_NASCITA,null,'-',pop_anagrafe.DESCR_COMUNE_NASCITA) AS COMUNI_NASCITA," +
								"decode(pop_anagrafe.CODICE_FISCALE,null,'-',pop_anagrafe.CODICE_FISCALE) AS CODICE_FISCALE," +
								"nvl(to_char(pop_anagrafe.DATA_NASCITA,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
								//"decode(pop_anagrafe.DATA_NASCITA,null,'-',pop_anagrafe.DATA_NASCITA) AS DATA_NASCITA, " +
								"decode(pop_anagrafe.SESSO,null,'-',pop_anagrafe.SESSO) AS SESSO, " +
								"decode(pop_anagrafe.TIPO_SOGGETTO,null,'-',pop_anagrafe.TIPO_SOGGETTO) AS TIPO_SOGGETTO, " +
								"trim(pop_anagrafe.SEDIME || ' ' || pop_anagrafe.NOME_VIA || ' ' || pop_anagrafe.CIVICO) as RESIDENZA, " +
								"decode(pop_anagrafe.FK_FAMIGLIE,null,'-',pop_anagrafe.FK_FAMIGLIE) AS FK_FAMIGLIE, " +
								"decode(pop_anagrafe.descr_stato_nascita,null,'-',pop_anagrafe.descr_stato_nascita) AS CITTADINANZA, " +
								"CODICE_NAZIONALE AS CODICE_NAZIONALE " +
								"from pop_anagrafe " +
								"where " +
								"pop_anagrafe.PK_CODI_ANAGRAFE = ? ";


						int indice = 1;
						this.setString(indice,chiave);

						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						Anagrafe ana = new Anagrafe();

						if (rs.next()){
							ana.setCodAnagrafe(rs.getString("COD_ANAGRAFE"));
							ana.setCognome(rs.getString("COGNOME"));
							ana.setNome(rs.getString("NOME"));
							ana.setComuniNascita(rs.getString("COMUNI_NASCITA"));
							ana.setDataNascita(rs.getString("DATA_NASCITA"));
							ana.setSesso(rs.getString("SESSO"));
							ana.setTipoSoggetto(rs.getString("TIPO_SOGGETTO"));
							ana.setCodFiscale(rs.getString("CODICE_FISCALE"));
							ana.setCittadinanze(rs.getString("CITTADINANZA"));
							ana.setResidenza(rs.getString("RESIDENZA"));
							ana.setFamiglie(rs.getString("FK_FAMIGLIE"));
							ana.setCodiceNazionale(rs.getString("CODICE_NAZIONALE"));
						}
						ht.put(ANAGRAFE, ana);

						// POI, SE PRESENTI, LEGGO I DATI RELATIVI ALLA FAMIGLIA
						if (ana.getFamiglie() != null && !"".equals(ana.getFamiglie()) && !"0".equals(ana.getFamiglie()))
						{
							sql =
								"select " +
								"pop_anagrafe.CODICE_FAMIGLIA, " +
								"decode(pop_anagrafe.PK_CODI_ANAGRAFE,null,'-',pop_anagrafe.PK_CODI_ANAGRAFE) AS COD_ANAGRAFE," +
								"decode(pop_anagrafe.COGNOME,null,'-',pop_anagrafe.COGNOME) AS COGNOME," +
								"decode(pop_anagrafe.NOME,null,'-',pop_anagrafe.NOME) AS NOME " +
								"from pop_anagrafe " +
								"where pop_anagrafe.FK_FAMIGLIE = ? " +
								"and exists (select * from POP_FAMIGLIE where POP_FAMIGLIE.PK_CODI_FAMIGLIE = ?)";
							this.initialize();
							this.setString(1, ana.getFamiglie());
							this.setString(2, ana.getFamiglie());
							prepareStatement(sql);
							rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
							if (rs.next())
							{
								String codiceFamiglia = rs.getString("CODICE_FAMIGLIA");
								ArrayList listaComponentiFamiglia = new ArrayList();
								do {
									ana = new Anagrafe();
									ana.setCodAnagrafe(rs.getString("COD_ANAGRAFE"));
									ana.setCognome(rs.getString("COGNOME"));
									ana.setNome(rs.getString("NOME"));
									listaComponentiFamiglia.add(ana);
								} while (rs.next());
								ht.put(LISTA_COMPONENTI_FAMIGLIA, listaComponentiFamiglia);
							}
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
					if (conn != null)
						if (!conn.isClosed())
							conn.close();
				}
				}


	public Hashtable mCaricareListaAnagrafePerFamiglia(String chiaveAna) throws Exception{

					Hashtable ht = new Hashtable();
					Vector vct = new Vector();
					String sql = "";
					String conteggio = "";
					long conteggione = 0;
					Connection conn = null;

					// faccio la connessione al db
							try {
								conn = this.getConnection();
								sql = SQL_SELECT_ANAGRAFE_FAMIGLIA;

								int indice = 1;
								this.initialize();
								this.setString(indice,chiaveAna);
								indice ++;

								prepareStatement(sql);
								java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

								while (rs.next()){
									Anagrafe ana = new Anagrafe();

									ana.setCodAnagrafe(rs.getString("COD_ANAGRAFE"));

									vct.add(ana);
								}
								ht.put(LISTA_ANAGRAFE2, vct);
								
								/*INIZIO AUDIT*/
								try {
									Object[] arguments = new Object[1];
									arguments[0] = chiaveAna;
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
}
