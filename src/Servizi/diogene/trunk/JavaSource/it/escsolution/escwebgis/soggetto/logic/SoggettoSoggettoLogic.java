/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.soggetto.logic;


import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.bean.Soggetto;
import it.escsolution.escwebgis.soggetto.bean.SoggettoFinder;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SoggettoSoggettoLogic extends EscLogic {

	public SoggettoSoggettoLogic(EnvUtente eu) {
					super(eu);
				}


	
		private final static String SQL_SELECT_LISTA = "select * from (" +
			"select ROWNUM as N,FK_COMUNI,COGNOME,NOME,DENOMINAZIONE,COD_SOGGETTO  from ( " +
			"select ROWNUM as N,"+
			"decode(SOG_SOGGETTI.COGNOME, null, '-', SOG_SOGGETTI.COGNOME) as COGNOME, " +
			"decode(SOG_SOGGETTI.NOME, null, '-',SOG_SOGGETTI.NOME) as NOME, " +
			"decode(SOG_SOGGETTI.DENOMINAZIONE, null, '-', SOG_SOGGETTI.DENOMINAZIONE) as DENOMINAZIONE, " +
			"decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS FK_COMUNI," +
			"SOG_SOGGETTI.PK_SOGGETTO as COD_SOGGETTO " +
			" from SOG_SOGGETTI,ewg_tab_comuni where SOG_SOGGETTI.FK_COMUNI=ewg_tab_comuni.UK_BELFIORE and 1=?" ;

		private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  from SOG_SOGGETTI WHERE 1=?" ;

		private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from SOG_SOGGETTI WHERE 1=?" ;







	public Hashtable mCaricareListaSoggetto(Vector listaPar, SoggettoFinder finder) throws Exception{
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
											sql = sql + " and SOG_SOGGETTI.PK_SOGGETTO in (" +finder.getKeyStr() + ")" ;

										}


											long limInf, limSup;
											limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
											limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
											sql = sql + "order by SOG_SOGGETTI.FK_COMUNI,SOG_SOGGETTI.COGNOME " ;
											if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

											prepareStatement(sql);
											rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

											if (i ==1) {
												while (rs.next()){
													//campi della lista
													Soggetto sog = new Soggetto();
													sog.setcodSoggetto(rs.getString("COD_SOGGETTO"));
													sog.setComune(rs.getString("FK_COMUNI"));
													sog.setCognome(rs.getString("COGNOME"));
													sog.setNome(rs.getString("NOME"));
													sog.setDenominazione(rs.getString("DENOMINAZIONE"));
													vct.add(sog);
												}
											}
											else{
												if (rs.next()){
													conteggio = rs.getString("conteggio");
												}
											}
										}
										ht.put("LISTA_SOGGETTI",vct);
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


	public Hashtable mCaricareDettaglioSoggetto(String chiave) throws Exception{

			Hashtable ht = new Hashtable();
						// faccio la connessione al db
			Connection conn = null;
						try {
							conn = this.getConnection();
							this.initialize();
							String sql = "select SOG_SOGGETTI.PK_SOGGETTO, decode(SOG_SOGGETTI.REGOLA, null, '-', SOG_SOGGETTI.REGOLA) as REGOLA, " +
								"decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) as FK_COMUNI, " +
								"decode(SOG_SOGGETTI.DATA_CARICAMENTO, null, '-',SOG_SOGGETTI.DATA_CARICAMENTO) as DATA_CARICAMENTO," +
								"decode(SOG_SOGGETTI.CODICE_FISCALE, null, '-', SOG_SOGGETTI.CODICE_FISCALE) as CODICE_FISCALE, " +
								"decode(SOG_SOGGETTI.PARTITA_IVA, null, '-',SOG_SOGGETTI.PARTITA_IVA) as PARTITA_IVA, " +
								"SOG_SOGGETTI.PK_SOGGETTO as COD_SOGGETTO, " +
								"decode(SOG_SOGGETTI.TIPO_PERSONA, null, '-', SOG_SOGGETTI.TIPO_PERSONA) as TIPO_PERSONA," +
								"decode(SOG_SOGGETTI.DENOMINAZIONE , null, '-', SOG_SOGGETTI.DENOMINAZIONE) as DENOMINAZIONE, " +
								"decode(SOG_SOGGETTI.COGNOME, null,'-',SOG_SOGGETTI.COGNOME) as COGNOME, " +
								"decode(SOG_SOGGETTI.NOME,null,'-',SOG_SOGGETTI.NOME) as NOME," +
								"decode(SOG_SOGGETTI.SESSO,null,'-',SOG_SOGGETTI.SESSO) as SESSO, " +
								"decode(SOG_SOGGETTI.DATA_NASCITA,null,'-',SOG_SOGGETTI.DATA_NASCITA) as DATA_NASCITA, " +
								"decode(SOG_SOGGETTI.LUOGO_NASCITA,null,'-',SOG_SOGGETTI.LUOGO_NASCITA) as LUOGO_NASCITA, " +
								"decode(SOG_SOGGETTI.COMUNE_NASCITA,null,'-',SOG_SOGGETTI.COMUNE_NASCITA) as COMUNE_NASCITA " +
								" from SOG_SOGGETTI,ewg_tab_comuni where ewg_tab_comuni.UK_BELFIORE=SOG_SOGGETTI.FK_COMUNI and SOG_SOGGETTI.PK_SOGGETTO = ?";


							int indice = 1;
							this.setString(indice,chiave);

							prepareStatement(sql);
							java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

							Soggetto sog = new Soggetto();

							if (rs.next()){
								sog.setcodSoggetto(rs.getString("PK_SOGGETTO"));
								sog.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
								sog.setComune(rs.getString("FK_COMUNI"));
								sog.setCognome(rs.getString("COGNOME"));
								sog.setDataNascita(rs.getString("DATA_NASCITA"));
								sog.setDenominazione(rs.getString("DENOMINAZIONE"));
								sog.setLuogoNascita(rs.getString("LUOGO_NASCITA"));
								sog.setNome(rs.getString("NOME"));
								sog.setPartitaIva(rs.getString("PARTITA_IVA"));
								sog.setSesso(rs.getString("SESSO"));
								sog.setTipoPersona(rs.getString("TIPO_PERSONA"));

							}
							ht.put("SOGGETTO",sog);
							
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
