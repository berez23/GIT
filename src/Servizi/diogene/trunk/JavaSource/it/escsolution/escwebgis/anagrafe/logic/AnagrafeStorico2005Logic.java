/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.logic;

import it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico2005Finder;
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

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnagrafeStorico2005Logic extends EscLogic{
	private String appoggioDataSource;
	public AnagrafeStorico2005Logic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	public static final String FINDER = "FINDER";
	public static final String ANAGRAFE_STORICO = "ANAGRAFE_STORICO_2005";
	public static final String LISTA = "LISTA_STORICO_ANAGRAFE_2005";
	public static final String STORICO = AnagrafeStorico2005Logic.class.getName() + "@STORICO";


	/*	private final static String SQL_SELECT_LISTA_MILANO = "SELECT * FROM (" +
								"SELECT ROWNUM AS N,MATRICOLA,CODICE_FISC,COGNOME,NOME,DATA_INIZIO,DATA_FINE,DATA_INIZIO_ELAB,DATA_FINE_ELAB,COD_VIA,NUM_CIV,ESP_CIV FROM ( " +
								"SELECT ROWNUM as N, " +
								"P.NIND AS MATRICOLA, " +
								"P.CODFISC AS CODICE_FISC, " +
								"P.COGNOME, " +
								"P.NOME, " +
								"STO_IND.IND_DATA_INIZ DATA_INIZIO," +
								"STO_IND.IND_KEY_DATA_FINE DATA_FINE," +
								"DECODE(NVL(STO_IND.IND_DATA_INIZ,'-'),'-','','00000000','', SUBSTR(STO_IND.IND_DATA_INIZ, 7, 2)||'/'||SUBSTR(STO_IND.IND_DATA_INIZ, 5, 2)||'/'||SUBSTR(STO_IND.IND_DATA_INIZ, 1, 4)) AS DATA_INIZIO_ELAB, " +
								"DECODE(NVL(STO_IND.IND_KEY_DATA_FINE,'-'),'-','','99999999','', SUBSTR(STO_IND.IND_KEY_DATA_FINE, 7, 2)||'/'||SUBSTR(STO_IND.IND_KEY_DATA_FINE, 5, 2)||'/'||SUBSTR(STO_IND.IND_KEY_DATA_FINE, 1, 4)) AS DATA_FINE_ELAB, " +
								"STO_IND.IND_COD_VIA AS COD_VIA, " +
								"STO_IND.IND_NUM_CIV AS NUM_CIV, " +
								"STO_IND.IND_ESP_CIV AS ESP_CIV " +
								"FROM SIT_D_PERSONE P ,MILANO.MI_D_DUP_STO_FAM_INDIRIZZI STO_IND " +
								"WHERE 1 = ? " +
								"AND P.NIND = STO_IND.MATRICOLA " +
								"AND P.CODENT = 'F205'" ;

	private final static String SQL_SELECT_COUNT_LISTA_MILANO ="select count(*) as conteggio  " +
														"FROM SIT_D_PERSONE P ,MILANO.MI_D_DUP_STO_FAM_INDIRIZZI STO_IND " +
														"WHERE 1=? " +
														"AND P.NIND = STO_IND.MATRICOLA " +
														"AND P.CODENT = 'F205'" ;
	*/

	private final static String SQL_SELECT_LISTA = "SELECT *  FROM (" +
								"SELECT ROWNUM AS N, MATRICOLA, CODICE_FISC, COGNOME, NOME,SESSO,DT_NASCITA FROM ( " +
								"SELECT   distinct " +
								"p.ID_ORIG MATRICOLA, " +
								"P.CODFISC CODICE_FISC, " +
								"P.COGNOME, " +
								"P.NOME, " +
								"P.SESSO, " +
								"to_char(p.DATA_NASCITA, 'dd/mm/yyyy') DT_NASCITA " +
								"FROM  SIT_D_PERSONA P " +
								"WHERE 1 = ? " ;

	private final static String SQL_SELECT_COUNT_LISTA ="select count(*) as conteggio  " +
														"FROM SIT_D_PERSONA P " +
														"WHERE 1=? ";




	public Hashtable mCaricareLista(Vector listaPar, AnagrafeStorico2005Finder finder) throws Exception{
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
									/*   PER VERIFICHE IN BASE A DATE DI RIFERIMENTO
									// CONTROLLO SE E' STATA SETTATA UNA DATA DI RIFERIMENTO ///////////
									if (i == 0)
									{
										EscElementoFiltro elementoFiltro = (EscElementoFiltro) listaPar.get(1);
										if (elementoFiltro.getValore()!=null && !elementoFiltro.getValore().trim().equals(""))
										{
											String val = elementoFiltro.getValore().trim();
											Vector operatoriStringaRid = new Vector();
											operatoriStringaRid.add(new EscOperatoreFiltro("<=", "minore-uguale"));

											elementoFiltro = new EscElementoFiltro();
											elementoFiltro.setAttributeName("DATA_INIZIO");
											elementoFiltro.setTipo("D");
											elementoFiltro.setCampoFiltrato("add_months(TO_DATE( DECODE(NVL(STO_IND.IND_DATA_INIZ,'-'),'-','19000101','00000000','19000101',STO_IND.IND_DATA_INIZ),'YYYYMMDD'),-3)");
											elementoFiltro.setOperatore("<=");
											elementoFiltro.setValore(val);
											listaPar.add(elementoFiltro);

											elementoFiltro = new EscElementoFiltro();
											elementoFiltro.setAttributeName("DATA_FINE");
											elementoFiltro.setTipo("D");
											elementoFiltro.setCampoFiltrato("add_months(TO_DATE(DECODE(NVL(STO_IND.IND_KEY_DATA_FINE,'-'),'-','99991231','99999999','99991231',STO_IND.IND_KEY_DATA_FINE),'YYYYMMDD'),-3)");
											elementoFiltro.setOperatore(">=");
											elementoFiltro.setValore(val);
											listaPar.add(elementoFiltro);
										}

										//rimuovo dal filtro la data di riferimento in base alla posizione dell'elementoFiltro
										listaPar.remove(1);
									}*/
									sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
								}
								else
								{
									//controllo provenienza chiamata
									String controllo = finder.getKeyStr();

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
						String sql= "SELECT  p.id_orig AS matricola," +
									"p.codfisc AS codice_fisc, " +
									"p.cognome, " +
									"p.nome, " +
									"p.SESSO, " +
									"sto_ind.ind_cod_via AS cod_via, " +
									"via.VIASEDIME ||' '||via.DESCRIZIONE descr_via, " +
									"sto_ind.ind_num_civ||' '||sto_ind.ind_esp_civ AS num_civ, " +
									"DECODE(NVL(STO_IND.IND_DATA_INIZ,'-'),'-','','00000000','', SUBSTR(STO_IND.IND_DATA_INIZ, 7, 2)||'/'||SUBSTR(STO_IND.IND_DATA_INIZ, 5, 2)||'/'||SUBSTR(STO_IND.IND_DATA_INIZ, 1, 4)) AS DATA_INIZIO, " +
									"DECODE(NVL(STO_IND.IND_KEY_DATA_FINE,'-'),'-','','99999999','', SUBSTR(STO_IND.IND_KEY_DATA_FINE, 7, 2)||'/'||SUBSTR(STO_IND.IND_KEY_DATA_FINE, 5, 2)||'/'||SUBSTR(STO_IND.IND_KEY_DATA_FINE, 1, 4)) AS DATA_FINE " +
									",sto_fami.FPER_COD_FAMIGLIA COD_FAMIGLIA " +
									"FROM sit_d_persona p,milano.mi_d_dup_sto_fam_indirizzi sto_ind, SIT_D_VIA via " +
									",(select distinct sto_fam.FPER_COD_PERSONA,sto_fam.FPER_COD_FAMIGLIA,sto_fam.FPER_RAPP_PAR," +
									" sto_fam.FPER_PESO_PAR, sto_fam.FPER_DATA_INIZ," +
									" sto_fam.FPER_MOTIVO_INIZ,sto_fam.FPER_DATA_FINE,sto_fam.FPER_MOTIVO_FINE " +
									"from milano.mi_d_dup_sto_fam_componenti sto_fam ) sto_fami " +
									"WHERE lpad(p.id_orig,10,'0') = lpad(sto_ind.matricola,10,'0') " +
									"AND p.id_orig = ? " +
									"AND P.DT_FINE_VAL IS NULL " + 
									"and sto_ind.ind_cod_via = VIA.ID_ORIG (+) " +
									" AND VIA.DT_FINE_VAL IS NULL " + 
									"AND lpad(p.id_orig,10,'0') = lpad(sto_fami.FPER_COD_PERSONA,10,'0') " +
									" AND nvl(sto_fami.fper_data_iniz,'10000101') < sto_ind.ind_key_data_fine "+
									" AND nvl(Sto_fami.fper_data_fine,'10000101') > sto_ind.ind_data_iniz "+
									"ORDER BY COGNOME,NOME,TO_NUMBER(STO_IND.IND_DATA_INIZ) ASC" ;

						String sqlFam ="SELECT DISTINCT p.COGNOME,p.NOME, p.DATA_NASCITA, sto_fam.FPER_COD_FAMIGLIA,sto_fam.FPER_COD_PERSONA,sto_fam.FPER_RAPP_PAR,sto_fam.FPER_PESO_PAR, sto_fam.FPER_DATA_INIZ,sto_fam.FPER_MOTIVO_INIZ,sto_fam.FPER_DATA_FINE,sto_fam.FPER_MOTIVO_FINE " +
									"from milano.mi_d_dup_sto_fam_componenti sto_fam,sit_d_personA p " +
									"WHERE STO_FAM.FPER_COD_FAMIGLIA = ? " +
									"AND DECODE(NVL(STO_FAM.FPER_DATA_INIZ,'-'),'-',TO_DATE('01011000','DDMMYYYY'),'00000000',TO_DATE('01011000','DDMMYYYY'),TO_DATE(STO_FAM.FPER_DATA_INIZ,'YYYYMMDD')) " +
									"BETWEEN DECODE(NVL(?,'-'),'-',TO_DATE('01011000','DDMMYYYY'),'',TO_DATE('01011000','DDMMYYYY'),TO_DATE(?,'DD/MM/YYYY')) " +
									"AND DECODE(NVL(?,'-'),'-',TO_DATE('01011000','DDMMYYYY'),'',TO_DATE('01011000','DDMMYYYY'),TO_DATE(?,'DD/MM/YYYY')) " +
									"AND lpad(p.ID_ORIG,10,'0') = to_number(lpad(sto_fam.FPER_COD_PERSONA,10,'0')) " +
									"ORDER BY COGNOME,NOME";


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
							ana.setFkFamiglia(rs.getString("COD_FAMIGLIA"));


							//RECUPERO I COMPONENTI DELLA FAMIGLIA PER OGNI STATO DELLO STORICO
							ArrayList listfam = new ArrayList();
							PreparedStatement psfam = conn.prepareStatement(sqlFam);
							psfam.setString(1,ana.getFkFamiglia());
							psfam.setString(2,ana.getInizioResidenza());
							psfam.setString(3,ana.getInizioResidenza());
							psfam.setString(4,ana.getFineResidenza());
							psfam.setString(5,ana.getFineResidenza());
							ResultSet rsfam = psfam.executeQuery();
							while (rsfam.next())
							{
								FamiliariStorico fs = new FamiliariStorico();
								fs.setCognome(rsfam.getString("COGNOME"));
								fs.setNome(rsfam.getString("NOME"));
								fs.setDtNascita(DateFormat.dateToString(rsfam.getDate("DATA_NASCITA"),"dd/MM/yyyy"));
								fs.setTipoParentela(rsfam.getString("FPER_RAPP_PAR"));
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
