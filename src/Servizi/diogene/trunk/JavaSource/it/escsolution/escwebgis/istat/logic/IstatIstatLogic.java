/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.istat.logic;

import it.escsolution.eiv.JavaBeanGlobalVar;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.istat.bean.Istat;
import it.escsolution.escwebgis.istat.bean.IstatFinder;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IstatIstatLogic extends EscLogic{
	public IstatIstatLogic(EnvUtente eu) {
				super(eu);
			}
	private final static String SQL_SELECT_LISTA = "select * from (" +
		"select ROWNUM as N,NOME_SEZIONE,FK_COMUNE,NOME_LOCALITA,NUM_SEZIONE,POP_RESID_TOTALE,COD_SEZIONE  from ( " +
		"select ROWNUM as N,"+
		"decode(ist_censimento91.NOMESEZIONE, null, '-', ist_censimento91.NOMESEZIONE) as NOME_SEZIONE, " +
		"decode(ist_censimento91.FK_COMUNI_IST, null, '-', ist_censimento91.FK_COMUNI_IST) as FK_COMUNE, " +
		"decode(ist_censimento91.NOMELOCALITA, null, '-',ist_censimento91.NOMELOCALITA) as NOME_LOCALITA, " +
		"decode(ist_censimento91.NUMSEZIONE, null, '-', ist_censimento91.NUMSEZIONE) as NUM_SEZIONE, " +
		"decode(ist_censimento91.POPRESIDTOTALE, null, '-',ist_censimento91.POPRESIDTOTALE) as POP_RESID_TOTALE, " +
		"ist_censimento91.UK_CODI_SEZIONE as COD_SEZIONE " +
		"from ist_censimento91 where 1=?" ;

	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  from ist_censimento91 WHERE 1=?" ;

	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from ist_censimento91 WHERE 1=?" ;






	public Hashtable mCaricareListaIstat(Vector listaPar, IstatFinder finder) throws Exception{
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
										sql = sql + " and ist_censimento91.UK_CODI_SEZIONE in (" +finder.getKeyStr() + ")" ;

									}


										long limInf, limSup;
										limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
										limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
										sql = sql + "order by ist_censimento91.NOMESEZIONE " ;
										if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

										prepareStatement(sql);
										rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

										if (i ==1) {
											while (rs.next()){
												//campi della lista
												Istat ist = new Istat();
												ist.setCodSezione(rs.getString("COD_SEZIONE"));
												ist.setNomeSezione(rs.getString("NOME_SEZIONE"));
												ist.setComune(rs.getString("FK_COMUNE"));
												ist.setNomeLocalita(rs.getString("NOME_LOCALITA"));
												ist.setNumeroSezione(rs.getString("NUM_SEZIONE"));
												ist.setPopResidTotale(rs.getString("POP_RESID_TOTALE"));
												vct.add(ist);
											}
										}
										else{
											if (rs.next()){
												conteggio = rs.getString("conteggio");
											}
										}
									}
									conn.close();
									ht.put("LISTA_ISTAT",vct);
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
	public JavaBeanGlobalVar mCaricareDatiGrafici(String chiave) throws Exception{

		Connection conn = null;
			try {
				conn = this.getConnection();
				this.initialize();
				String sql = "select Xcentroid, ycentroid, fwidth, fheight " +
				" from ist_censimento91" +
				" where ist_censimento91.UK_CODI_SEZIONE = ?";


				int indice = 1;
				this.setString(indice,chiave);

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				JavaBeanGlobalVar beanGlobale = null;
				if (rs.next()){
					beanGlobale = new JavaBeanGlobalVar(this.getEnvUtente());
					beanGlobale.setXCentroid(rs.getDouble("Xcentroid"));
					beanGlobale.setYCentroid(rs.getDouble("Ycentroid"));
					beanGlobale.setFWidth(rs.getDouble("fwidth"));
					beanGlobale.setFHeight(rs.getDouble("fheight"));
				}
				return beanGlobale;
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




public Hashtable mCaricareDettaglioIstat(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		Connection conn = null;
					// faccio la connessione al db
					try {
						conn = this.getConnection();
						this.initialize();
						String sql = "select decode(ist_censimento91.NOMESEZIONE, null, '-', ist_censimento91.NOMESEZIONE) as NOME_SEZIONE, " +
							"decode(ist_censimento91.FK_COMUNI_IST, null, '-', ist_censimento91.FK_COMUNI_IST) as COMUNE, " +
							"decode(ist_censimento91.NOMELOCALITA, null, '-',ist_censimento91.NOMELOCALITA) as NOME_LOCALITA," +
							"decode(ist_censimento91.NUMSEZIONE, null, '-', ist_censimento91.NUMSEZIONE) as NUM_SEZIONE, " +
							"decode(ist_censimento91.POPRESIDTOTALE, null, '-',ist_censimento91.POPRESIDTOTALE) as POP_RESID_TOTALE, " +
							"ist_censimento91.UK_CODI_SEZIONE as COD_SEZIONE, " +
							"decode(ist_censimento91.POPRESIDMASCHI, null, '-', ist_censimento91.POPRESIDMASCHI) as POP_RESID_MASCHI," +
							"decode(ist_censimento91.POPRESIDFEMMINE , null, '-', ist_censimento91.POPRESIDFEMMINE) as POP_RESID_FEMMINE, " +
							"decode(ist_censimento91.ABITAZTOTALI, null,'-',ist_censimento91.ABITAZTOTALI) as ABITAZ_TOTALI, " +
							"decode(ist_censimento91.ABITAZOCCUPATE,null,'-',ist_censimento91.ABITAZOCCUPATE) as ABITAZ_OCCUPATE," +
							"decode(ist_censimento91.ABITAZNONOCCUPATE,null,'-',ist_censimento91.ABITAZNONOCCUPATE) as ABITAZ_NON_OCCUPATE, " +
							"decode(ist_censimento91.ABITAZOCCUPCOSTRANTE1919,null,'-',ist_censimento91.ABITAZOCCUPCOSTRANTE1919) as ANTE1919, " +
							"decode(ist_censimento91.ABITAZOCCUPCOSTRTRA1919E1945,null,'-',ist_censimento91.ABITAZOCCUPCOSTRTRA1919E1945) as TRA1919_1945, " +
							"decode(ist_censimento91.ABITAZOCCUPCOSTRTRA1946E1960,null,'-',ist_censimento91.ABITAZOCCUPCOSTRTRA1946E1960) as TRA1946_1960, " +
							"decode(ist_censimento91.ABITAZOCCUPCOSTRTRA1961E1971,null,'-',ist_censimento91.ABITAZOCCUPCOSTRTRA1961E1971) as TRA1961_1971, " +
							"decode(ist_censimento91.ABITAZOCCUPCOSTRTRA1972E1981,null,'-',ist_censimento91.ABITAZOCCUPCOSTRTRA1972E1981) as TRA1972_1981, " +
							"decode(ist_censimento91.ABITAZOCCUPCOSTRDOPO1981,null,'-',ist_censimento91.ABITAZOCCUPCOSTRDOPO1981) as DOPO_1981 " +
							"from ist_censimento91 where ist_censimento91.UK_CODI_SEZIONE = ?";


						int indice = 1;
						this.setString(indice,chiave);

						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						Istat ist = new Istat();

						if (rs.next()){
							ist.setNomeSezione(rs.getString("NOME_SEZIONE"));
							ist.setComune(rs.getString("COMUNE"));
							ist.setNomeLocalita(rs.getString("NOME_LOCALITA"));
							ist.setNumeroSezione(rs.getString("NUM_SEZIONE"));
							ist.setPopResidTotale(rs.getString("POP_RESID_TOTALE"));
							ist.setPopResidMaschi(rs.getString("POP_RESID_MASCHI"));
							ist.setPopResidFemmine(rs.getString("POP_RESID_FEMMINE"));
							ist.setAbitazTotali(rs.getString("ABITAZ_TOTALI"));
							ist.setAbitazOccupate(rs.getString("ABITAZ_OCCUPATE"));
							ist.setAbitazNonOccupate(rs.getString("ABITAZ_NON_OCCUPATE"));
							ist.setAbitazAnte1919(rs.getString("ANTE1919"));
							ist.setAbitazTra1919e1945(rs.getString("TRA1919_1945"));
							ist.setAbitazTra1946e1960(rs.getString("TRA1946_1960"));
							ist.setAbitazTra1961e1971(rs.getString("TRA1961_1971"));
							ist.setAbitazTra1972e1981(rs.getString("TRA1972_1981"));
							ist.setAbitazDopo1981(rs.getString("DOPO_1981"));

						}
						ht.put("ISTAT",ist);
						
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
