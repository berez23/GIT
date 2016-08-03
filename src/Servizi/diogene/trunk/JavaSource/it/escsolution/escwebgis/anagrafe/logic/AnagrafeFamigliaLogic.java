/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.logic;

import it.escsolution.escwebgis.anagrafe.bean.Famiglia;
import it.escsolution.escwebgis.anagrafe.bean.FamigliaFinder;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnagrafeFamigliaLogic extends EscLogic {
	private String appoggioDataSource;
		public AnagrafeFamigliaLogic(EnvUtente eu) {
					super(eu);
					appoggioDataSource=eu.getDataSource();
				}

		private final static String SQL_SELECT_LISTA = "select * from (select ROWNUM as N, decode(pop_famiglie.PK_CODI_FAMIGLIE,null,'-',pop_famiglie.PK_CODI_FAMIGLIE) AS COD_FAMIGLIA," +
		"decode(pop_famiglie.DENOMINAZIONE,null,'-',pop_famiglie.DENOMINAZIONE) AS DENOMINAZIONE," +
		"decode(pop_famiglie.TIPO_FAMIGLIA,null,'-',pop_famiglie.TIPO_FAMIGLIA) AS TIPO_FAMIGLIA" +
		" from pop_famiglie WHERE 1=?" ;

		private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from pop_famiglie WHERE 1=?" ;

		private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio  from pop_famiglie WHERE 1=?" ;

		private final static String SQL_SELECT_FAMIGLIA_PER_ANAGRAFE = "select decode(pop_famiglie.PK_CODI_FAMIGLIE,null,'-',pop_famiglie.PK_CODI_FAMIGLIE) AS COD_FAMIGLIA," +
							"decode(pop_famiglie.DENOMINAZIONE,null,'-',pop_famiglie.DENOMINAZIONE) AS DENOMINAZIONE," +
							"decode(pop_famiglie.TIPO_FAMIGLIA,null,'-',pop_famiglie.TIPO_FAMIGLIA) AS TIPO_FAMIGLIA," +
							"decode(pop_famiglie.SCALA,null,'-',pop_famiglie.SCALA) AS SCALA," +
							"decode(pop_famiglie.INTERNO,null,'-',pop_famiglie.INTERNO) AS INTERNO, " +
							"decode(pop_famiglie.PIANO,null,'-',pop_famiglie.PIANO) AS PIANO, " +
							"decode(pop_famiglie.COD_FISCALE_CAPO,null,'-',pop_famiglie.COD_FISCALE_CAPO) AS COD_FISCALE " +
							"from pop_famiglie, pop_anagrafe " +
							"where pop_famiglie.PK_CODI_FAMIGLIE = pop_anagrafe.FK_FAMIGLIE " +
							"and pop_anagrafe.PK_CODI_ANAGRAFE = ? and rownum =1 ";






	public Hashtable mCaricareListaFamiglia(Vector listaPar, FamigliaFinder finder) throws Exception{
					// carico la lista dei terreni e la metto in un hashtable
					Hashtable ht = new Hashtable();
					Vector vct = new Vector();
					sql = "";
					String conteggio = "";
					long conteggione = 0;
					Connection conn = null;

					// faccio la connessione al db
							try {
								conn = this.getConnection();
								java.sql.ResultSet rs;
								int indice = 1;
								/* non faccio il conteggione
								 * int indice = 1;
								this.initialize();
								this.setInt(indice,1);
								indice ++;
								prepareStatement(sql);
								rs = executeQuery(conn);
								if (rs.next()){
										conteggione = rs.getLong("conteggio");
								}
								 sql = SQL_SELECT_COUNT_ALL;

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
								//if (!finder.getKeyStr().equals("")){
									sql = sql + " and pop_famiglie.PK_CODI_FAMIGLIE in (" +finder.getKeyStr() + ")" ;
									//this.setString(indice,finder.getTitolarita());
									//indice ++;
								}


									long limInf, limSup;
									limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
									limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
									sql = sql + " order by denominazione " ;
									if (i ==1) sql = sql + ") where N > " + limInf + " and N <=" + limSup;

									prepareStatement(sql);
									rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

									if (i ==1) {
										while (rs.next()){
											//campi della lista
											Famiglia fam = new Famiglia ();
											fam.setCodFamiglia(rs.getString("COD_FAMIGLIA"));
											fam.setDenominazione(rs.getString("DENOMINAZIONE"));
											fam.setTipoFamiglia(rs.getString("TIPO_FAMIGLIA"));
											vct.add(fam);
										}
									}
									else{
										if (rs.next()){
											conteggio = rs.getString("conteggio");
										}
									}
								}
								ht.put("LISTA_FAMIGLIA",vct);
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

	public Hashtable mCaricareDettaglioFamiglia(String codFam) throws Exception{
						// carico la lista dei terreni e la metto in un hashtable
						Hashtable ht = new Hashtable();
						Connection conn = null;

						// faccio la connessione al db
						try {
							conn = this.getConnection();

							this.initialize();
							String sql = "select decode(pop_famiglie.PK_CODI_FAMIGLIE,null,'-',pop_famiglie.PK_CODI_FAMIGLIE) AS COD_FAMIGLIA," +
							"decode(pop_famiglie.DENOMINAZIONE,null,'-',pop_famiglie.DENOMINAZIONE) AS DENOMINAZIONE," +
							"decode(pop_famiglie.TIPO_FAMIGLIA,null,'-',pop_famiglie.TIPO_FAMIGLIA) AS TIPO_FAMIGLIA," +
							"decode(pop_famiglie.SCALA,null,'-',pop_famiglie.SCALA) AS SCALA," +
							"decode(pop_famiglie.INTERNO,null,'-',pop_famiglie.INTERNO) AS INTERNO, " +
							"decode(pop_famiglie.PIANO,null,'-',pop_famiglie.PIANO) AS PIANO, " +
							"decode(pop_famiglie.COD_FISCALE_CAPO,null,'-',pop_famiglie.COD_FISCALE_CAPO) AS COD_FISCALE " +
							"from pop_famiglie WHERE PK_CODI_FAMIGLIE=? ";

							int indice = 1;
							this.setString(indice,codFam);
							prepareStatement(sql);
							java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
							Famiglia fam = new Famiglia();
							if (rs.next()){
								fam.setCodFamiglia(rs.getString("COD_FAMIGLIA"));
								fam.setDenominazione(rs.getString("DENOMINAZIONE"));
								fam.setTipoFamiglia(rs.getString("TIPO_FAMIGLIA"));
								fam.setScala(rs.getString("SCALA"));
								fam.setInterno(rs.getString("INTERNO"));
								fam.setPiano(rs.getString("PIANO"));
								fam.setCodFiscale(rs.getString("COD_FISCALE"));

							}
							ht.put("FAMIGLIA",fam);
							
							/*INIZIO AUDIT*/
							try {
								Object[] arguments = new Object[1];
								arguments[0] = codFam;
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



	public Hashtable mCaricareFamigliaPerAnagrafe(String chiave) throws Exception{

					Hashtable ht = new Hashtable();
					String sql = "";
					Connection conn = null;

					// faccio la connessione al db
							try {
								conn = this.getConnection();
								this.initialize();
								sql = SQL_SELECT_FAMIGLIA_PER_ANAGRAFE;

								int indice = 1;
								this.setString(indice,chiave);

								prepareStatement(sql);
								java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
								Famiglia fam = new Famiglia();

								if (rs.next()){
									fam.setCodFamiglia(rs.getString("COD_FAMIGLIA"));
									fam.setDenominazione(rs.getString("DENOMINAZIONE"));
									fam.setTipoFamiglia(rs.getString("TIPO_FAMIGLIA"));
									fam.setScala(rs.getString("SCALA"));
									fam.setInterno(rs.getString("INTERNO"));
									fam.setPiano(rs.getString("PIANO"));
									fam.setCodFiscale(rs.getString("COD_FISCALE"));
									}

								ht.put("FAMIGLIA",fam);
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
