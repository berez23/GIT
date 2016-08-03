/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.logic;

import it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeStoricoFinder;
import it.escsolution.escwebgis.anagrafe.bean.FamiliariStorico;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.webred.utils.DateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnagrafeStoricoAnagrafeLogic extends EscLogic{
	private String appoggioDataSource;
	public AnagrafeStoricoAnagrafeLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	public static final String FINDER = "FINDER";
	public static final String ANAGRAFE_STORICO = "ANAGRAFE_STORICO";
	public static final String LISTA = "LISTA_STORICO_ANAGRAFE";
	public static final String STORICO = AnagrafeStoricoAnagrafeLogic.class.getName() + "@STORICO";

	//private final static int RIGHE_PER_PAGINA = it.webred.GlobalParameters.RIGHE_CONFIGURATE_PER_PAGINA;

	private final static String SQL_SELECT_LISTA = "SELECT *  FROM (" +
								"SELECT ROWNUM AS N, MATRICOLA, CODICE_FISC, COGNOME, NOME,SESSO,DT_NASCITA FROM (" +
								"SELECT   distinct " +
								"P.id_orig MATRICOLA, " +
								"P.CODFISC CODICE_FISC, " +
								"P.COGNOME, " +
								"P.NOME, " +
								"P.SESSO, " +
								"to_char(P.DATA_NASCITA, 'dd/mm/yyyy') DT_NASCITA " +
								"FROM  DIOGENE.SIT_D_PERSONA P " +
								"WHERE 1 = ? " +
								"AND P.FK_ENTE_SORGENTE = 1 " ;

	private final static String SQL_SELECT_COUNT_LISTA ="select count(*) as conteggio  " +
														"FROM DIOGENE.SIT_D_PERSONA P " +
														"WHERE 1=? " +
														"AND P.FK_ENTE_SORGENTE = 1 ";




	public Hashtable mCaricareLista(Vector listaPar, AnagrafeStoricoFinder finder) throws Exception{
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

								// il primo passaggio esegue la select count
								if (finder.getKeyStr().equals(""))
								{
									sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
								}
								else
								{
									//controllo provenienza chiamata
									String controllo = finder.getKeyStr();
									/*if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
										String ente = controllo.substring(0,controllo.indexOf("|"));
										ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
										String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
										sql = sql + " and pop_anagrafe.PK_CODI_ANAGRAFE in (" +chiavi + ")" ;
										sql = sql + " and pop_anagrafe.CODICE_NAZIONALE = '" +ente + "'" ;
									}else
										sql = sql + " and pop_anagrafe.PK_CODI_ANAGRAFE in (" +finder.getKeyStr() + ")" ;
									*/

								}


									long limInf, limSup;
									limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
									limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
									sql = sql + " ORDER BY COGNOME,NOME";
									if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

									prepareStatement(sql);
									rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

									if (i ==1) {
										while (rs.next()){
											//campi della lista
											AnagrafeStorico ana = new AnagrafeStorico();
											ana.setMatricola(rs.getString("MATRICOLA"));
											ana.setCognome(rs.getString("COGNOME"));
											ana.setNome(rs.getString("NOME"));
											ana.setCodFiscale(rs.getString("CODICE_FISC"));
											ana.setDataNascita(rs.getString("DT_NASCITA"));
											ana.setSesso(rs.getString("SESSO"));
											vct.add(ana);
										}
									}
									else{
										if (rs.next()){
											conteggio = rs.getString("conteggio");
										}
									}
								}
								ht.put(LISTA, vct);
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


	public Hashtable mCaricareDettaglio(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
					// faccio la connessione al db
					Connection conn = null;
					try {
						conn = this.getConnection();
						this.initialize();
						// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
						/*
						String sql = "select  p.id_ext MATRICOLA," +
									"P.CODFISC CODICE_FISC," +
									"P.COGNOME," +
									"P.NOME," +
									"P.SESSO," +
									"to_char(pc.DT_INIZIO_VAL, 'dd/mm/yyyy') DATA_INIZIO," +
									"to_char(pc.dt_fine_val, 'dd/mm/yyyy') DATA_FINE," +
									"SUBSTR(C.ID_EXT,0,instr(C.ID_EXT, '@')-1) cod_via," +
									" cxml.cod_via||' '||cxml.barrato||' '||cxml.subbarato NUM_CIV " +
								"from  diogene.sit_d_persona_civico pc, " +
								"diogene.sit_d_civico  c," +
								"diogene.sit_d_persona p," +
								"(select id, extract(civico_composto,'/civicocomposto/att[@tipo=\"numero\"]/@valore').getStringVal() cod_via," +
								"extract(civico_composto,'/civicocomposto/att[@tipo=\"barrato\"]/@valore').getStringVal() barrato," +
								"extract(civico_composto,'/civicocomposto/att[@tipo=\"subbarrato\"]/@valore').getStringVal() subbarato " +
								"from diogene.sit_d_civico) CXML " +
								"where pc.FK_D_CIVICO = c.id " +
								"and p.id = pc.FK_D_PERSONA " +
								"AND c.ID = CXML.id " +
								"and p.ID_EXT = ? " +
								"order by P.COGNOME,P.NOME,pc.DT_INIZIO_VAL asc";
								*/
						String sql= "SELECT  distinct p.id_orig matricola," +
								"p.codfisc codice_fisc, " +
								"p.cognome, " +
								"p.nome, " +
								"p.sesso, " +
								"SUBSTR(C.ID_ORIG,0,instr(C.ID_ORIG, '#')-1) cod_via, " +
								"via.VIASEDIME ||' '||via.VIADES descr_via, " +
								"cxml.numero||' '||cxml.barrato||' '||cxml.subbarato NUM_CIV, " +
								"to_char(pc.dt_inizio_DATO, 'dd/mm/yyyy') DATA_INIZIO, " +
								"to_char(pc.dt_fine_DATO, 'dd/mm/yyyy') DATA_FINE, " +
								"f.ID_EXT_D_FAMIGLIA  fk_famiglia, " +
								"fam.ID_ORIG cod_famiglia " +
								//"p.DT_INIZIO_VAL, " +
								//"p.DT_FINE_VAL " +
								"FROM diogene.sit_d_persona_civico pc, " +
								"diogene.sit_d_civico c, " +
								"diogene.sit_d_persona p, " +
								"diogene.sit_d_pers_fam f, " +
								"diogene.sit_d_famiglia fam, " +
								"sit_d_vie via , " +
								"(SELECT ID_EXT, " +
								"EXTRACT (civico_composto,'/civicocomposto/att[@tipo=\"numero\"]/@valore').getstringval () numero, " +
								"EXTRACT (civico_composto,'/civicocomposto/att[@tipo=\"barrato\"]/@valore').getstringval () barrato," +
								"EXTRACT (civico_composto,'/civicocomposto/att[@tipo=\"subbarrato\"]/@valore').getstringval () subbarato " +
								"FROM diogene.sit_d_civico) cxml " +
								"WHERE pc.ID_EXT_d_civico = c.ID_EXT " +
								"AND p.ID_EXT = pc.ID_EXT_d_persona " +
								"AND p.FK_ENTE_SORGENTE = 1 " +
								"AND c.ID_EXT = cxml.ID_EXT " +
								"AND p.ID_EXT = f.ID_EXT_D_PERSONA " +
								"and f.ID_EXT_D_FAMIGLIA = fam.ID_EXT " +
								"AND SUBSTR(C.ID_ORIG,0,instr(C.ID_ORIG, '#')-1) = via.PK_VIACOD  " +
								"and via.CODENT = 'F205' " +
								"AND p.ID_ORIG= ? " +
								"ORDER BY p.cognome, p.nome, data_inizio ASC" ;

						String sqlFam = "select distinct pf.RELAZ_PAR ,p.COGNOME,p.NOME, p.DATA_NASCITA " +
								"from sit_d_pers_fam pf, " +
								"sit_d_persona p " +
								"where pf.ID_EXT_D_PERSONA = p.ID_EXT " +
								"and  pf.ID_EXT_D_FAMIGLIA = ?";

						int indice = 1;
						this.setString(indice,chiave);

						prepareStatement(sql);
						ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						while (rs.next()){
							AnagrafeStorico ana = new AnagrafeStorico();
							ana.setMatricola(rs.getString("MATRICOLA"));
							ana.setCognome(rs.getString("COGNOME"));
							ana.setNome(rs.getString("NOME"));
							ana.setInizioResidenza(rs.getString("DATA_INIZIO"));
							ana.setFineResidenza(rs.getString("DATA_FINE"));
							ana.setSesso(rs.getString("SESSO"));
							ana.setCivico(rs.getString("NUM_CIV"));
							ana.setCodFiscale(rs.getString("CODICE_FISC"));
							ana.setCodiceVia(rs.getString("COD_VIA"));
							ana.setDescrVia(rs.getString("descr_via"));
							ana.setCodFamiglia(rs.getString("COD_FAMIGLIA"));
							ana.setFkFamiglia(rs.getString("FK_FAMIGLIA"));

							//RECUPERO I COMPONENTI DELLA FAMIGLIA PER OGNI STATO DELLO STORICO
							log.debug("RECUPERO I COMPONENTI DELLA FAMIGLIA PER OGNI STATO DELLO STORICO - SQL["+sqlFam+"] Param["+ana.getFkFamiglia()+"]");
							
							ArrayList listfam = new ArrayList();
							PreparedStatement psfam = conn.prepareStatement(sqlFam);
							psfam.setString(1,ana.getFkFamiglia());
							ResultSet rsfam = psfam.executeQuery();
							while (rsfam.next())
							{
								FamiliariStorico fs = new FamiliariStorico();
								fs.setCognome(rsfam.getString("COGNOME"));
								fs.setNome(rsfam.getString("NOME"));
								fs.setDtNascita(DateFormat.dateToString(rsfam.getDate("DATA_NASCITA"),"dd/MM/yyyy"));
								fs.setTipoParentela(rsfam.getString("RELAZ_PAR"));
								listfam.add(fs);
							}
							rsfam.close();
							psfam.close();
							ana.setElencoFamiliari(listfam);

							vct.add(ana);
						}
						ht.put(ANAGRAFE_STORICO, vct);
						
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


}
