/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.agenziaEntrate.logic;


import it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuenteGiu;
import it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuentiGiuFinder;
import it.escsolution.escwebgis.agenziaEntrate.bean.PartitaIVA;
import it.escsolution.escwebgis.agenziaEntrate.bean.RapLegale;
import it.escsolution.escwebgis.agenziaEntrate.bean.VariazioneDomicilio;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AgenziaEntrateContribuentiGiuLogic extends EscLogic {

	public AgenziaEntrateContribuentiGiuLogic(EnvUtente eu) {
					super(eu);
				}


	
			private final static String SQL_SELECT_LISTA = "select * from (" +
				"select PK_ID_PG,COD_FISC_SOGGETTO,DENOMINAZIONE,IND_CIV_DOMICILIO_FISC,COD_ATTIVITA,DESCR_ATTIVITA,  ROWNUM AS n from (" +
				"select DISTINCT " +
				"decode(MI_SIATEL_P_GIU.COD_FISC_SOGGETTO, null, '-', MI_SIATEL_P_GIU.COD_FISC_SOGGETTO) as COD_FISC_SOGGETTO," +
				"decode(MI_SIATEL_P_GIU.DENOMINAZIONE, null, '-',MI_SIATEL_P_GIU.DENOMINAZIONE) as DENOMINAZIONE," +
				"decode(MI_SIATEL_P_GIU.IND_CIV_DOMICILIO_FISC, null, '-', MI_SIATEL_P_GIU.IND_CIV_DOMICILIO_FISC) as IND_CIV_DOMICILIO_FISC," +
				"decode(MI_SIATEL_P_GIU.COD_ATTIVITA, null, '-', MI_SIATEL_P_GIU.COD_ATTIVITA) as COD_ATTIVITA," +
				"decode(MI_SIATEL_P_GIU.DESCR_ATTIVITA, null, '-', MI_SIATEL_P_GIU.DESCR_ATTIVITA) as DESCR_ATTIVITA," +
				"MI_SIATEL_P_GIU.PK_ID_PG as PK_ID_PG from MI_SIATEL_P_GIU where 1=?";

		private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  from MI_SIATEL_P_GIU WHERE 1=?" ;

		private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from MI_SIATEL_P_GIU WHERE 1=?" ;


	public Hashtable mCaricareListaContribuente(Vector listaPar, AgenziaEntrateContribuentiGiuFinder finder) throws Exception{
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
											String chiavi = "";
											if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
												String ente = controllo.substring(0,controllo.indexOf("|"));
												ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
												chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
											}else
												chiavi = controllo;

											sql = sql + " and MI_SIATEL_P_GIU.PK_ID_PG in (" + chiavi + ")" ;

										}


											long limInf, limSup;
											limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
											limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
											sql = sql + "order by DENOMINAZIONE " ;
											if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

											prepareStatement(sql);
											rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

											if (i ==1) {
												while (rs.next()){
													//campi della lista
													AgenziaEntrateContribuenteGiu contr = new AgenziaEntrateContribuenteGiu();
													contr.setPkIdPg(rs.getString("PK_ID_PG"));
													contr.setCodFiscSoggetto(rs.getString("COD_FISC_SOGGETTO"));
													contr.setDenominazione(rs.getString("DENOMINAZIONE"));
													contr.setIndCivDomicilioFisc(rs.getString("IND_CIV_DOMICILIO_FISC"));
													contr.setCodAttivita(rs.getString("COD_ATTIVITA"));
													contr.setDescrAttivita(rs.getString("DESCR_ATTIVITA"));
													vct.add(contr);
												}
											}
											else{
												if (rs.next()){
													conteggio = rs.getString("conteggio");
												}
											}
										}
										conn.close();
										ht.put("LISTA_CONTRIBUENTI",vct);
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


	public Hashtable mCaricareDettaglioContribuente(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		Connection conn = null;
					// faccio la connessione al db
					try {

						conn = this.getConnection();
						this.initialize();

						String sql = "select * from MI_SIATEL_P_GIU where MI_SIATEL_P_GIU.PK_ID_PG= ? ";

						int indice = 1;
						//recupero le chiavi di ricerca

						this.setString(indice,chiave);

						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						AgenziaEntrateContribuenteGiu contr = new AgenziaEntrateContribuenteGiu();

						while (rs.next()){
							//campi della lista

							contr.setPkIdPg(tornaValoreRS(rs, "PK_ID_PG"));

							contr.setCodent(tornaValoreRS(rs, "CODENT"));

							contr.setTipoRecord(tornaValoreRS(rs, "TIPO_RECORD"));

							contr.setCodRitornoComune(tornaValoreRS(rs, "COD_RITORNO_COMUNE"));

							contr.setCodFunzione(tornaValoreRS(rs, "COD_FUNZIONE"));

							contr.setCodRitorno(tornaValoreRS(rs, "COD_RITORNO"));

							contr.setNumeroSoggettiTrovati(tornaValoreRS(rs, "NUMERO_SOGGETTI_TROVATI"));

							contr.setCodFiscIindividuato(tornaValoreRS(rs, "COD_FISC_INDIVIDUATO"));

							contr.setCodFiscSoggetto(tornaValoreRS(rs, "COD_FISC_SOGGETTO"));

							contr.setFlagFonte(tornaValoreRS(rs, "FLAG_FONTE"));


							contr.setDenominazione(tornaValoreRS(rs, "DENOMINAZIONE"));

							contr.setSigla(tornaValoreRS(rs, "SIGLA"));

							contr.setCodNaturaGiu(tornaValoreRS(rs, "COD_NATURA_GIU"));

							contr.setFlagOver10CodColle(tornaValoreRS(rs, "FLAG_OVER_10_COD_COLLE"));

							contr.setFlagOver20PivColle(tornaValoreRS(rs, "FLAG_OVER_20_PIV_COLLE"));

							contr.setDesNaturaGiu(tornaValoreRS(rs, "DES_NATURA_GIU"));

							contr.setCodDomicilioFisc(tornaValoreRS(rs, "COD_DOMICILIO_FISC"));

							contr.setCapDomicilioFisc(tornaValoreRS(rs, "CAP_DOMICILIO_FISC"));

							contr.setIndCivDomicilioFisc(tornaValoreRS(rs, "IND_CIV_DOMICILIO_FISC"));

							contr.setCodAttivita(tornaValoreRS(rs, "COD_ATTIVITA"));

							contr.setDataInizioScriCont(tornaValoreRS(rs, "DATA_INIZIO_SCRI_CONT"));

							contr.setFlagTenutario(tornaValoreRS(rs, "FLAG_TENUTARIO"));

							contr.setFlagVarDomicilio(tornaValoreRS(rs, "FLAG_VAR_DOMICILIO"));

							contr.setFlagVarAttivita(tornaValoreRS(rs, "FLAG_VAR_ATTIVITA"));

							contr.setFlagVarTenutario(tornaValoreRS(rs, "FLAG_VAR_TENUTARIO"));

							contr.setFlagVarOpzCont(tornaValoreRS(rs, "FLAG_VAR_OPZ_CONT"));

							contr.setComuneDomilicioFisc(tornaValoreRS(rs, "COMUNE_DOMILICIO_FISC"));

							contr.setProvinDomicilioFisc(tornaValoreRS(rs, "PROVIN_DOMICILIO_FISC"));

							contr.setDescrAttivita(tornaValoreRS(rs, "DESCR_ATTIVITA"));

							contr.setCodFiscTenutario(tornaValoreRS(rs, "COD_FISC_TENUTARIO"));

							contr.setCodCatastaleSedeLeg(tornaValoreRS(rs, "COD_CATASTALE_SEDE_LEG"));

							contr.setCapSedeLegale(tornaValoreRS(rs, "CAP_SEDE_LEGALE"));

							contr.setIndCivSedeLegale(tornaValoreRS(rs, "IND_CIV_SEDE_LEGALE"));

							contr.setFlagCodCaricaRapLeg(tornaValoreRS(rs, "FLAG_COD_CARICA_RAP_LEG"));

							contr.setFlagVarSedeLegale(tornaValoreRS(rs, "FLAG_VAR_SEDE_LEGALE"));

							contr.setFlagVarRapLegale(tornaValoreRS(rs, "FLAG_VAR_RAP_LEGALE"));

							contr.setFlagVarDenominazione(tornaValoreRS(rs, "FLAG_VAR_DENOMINAZIONE"));

							contr.setFlagVarAttivitaBis(tornaValoreRS(rs, "FLAG_VAR_ATTIVITA_BIS"));

							contr.setComuneSedeLegale(tornaValoreRS(rs, "COMUNE_SEDE_LEGALE"));

							contr.setProvinSedeLegale(tornaValoreRS(rs, "PROVIN_SEDE_LEGALE"));

							contr.setFlagDecodRapLeg(tornaValoreRS(rs, "FLAG_DECOD_RAP_LEG"));

							contr.setCodFiscRapLegPfis(tornaValoreRS(rs, "COD_FISC_RAP_LEG_PFIS"));

							contr.setCognomeRapLegPfis(tornaValoreRS(rs, "COGNOME_RAP_LEG_PFIS"));

							contr.setNomeRapLegPfis(tornaValoreRS(rs, "NOME_RAP_LEG_PFIS"));

							contr.setIndCivRapLegPfis(tornaValoreRS(rs, "IND_CIV_RAP_LEG_PFIS"));

							contr.setCapRapLegPfis(tornaValoreRS(rs, "CAP_RAP_LEG_PFIS"));

							contr.setComuneResRapLegPfis(tornaValoreRS(rs, "COMUNE_RES_RAP_LEG_PFIS"));

							contr.setProvinResRapLeg(tornaValoreRS(rs, "PROVIN_RES_RAP_LEG"));

							contr.setCodFiscRapLegPgiu(tornaValoreRS(rs, "COD_FISC_RAP_LEG_PGIU"));

							contr.setDenominazioneRapPgiu(tornaValoreRS(rs, "DENOMINAZIONE_RAP_PGIU"));

							contr.setIndCivRapLegPgiu(tornaValoreRS(rs, "IND_CIV_RAP_LEG_PGIU"));

							contr.setCapRapLegPgiu(tornaValoreRS(rs, "CAP_RAP_LEG_PGIU"));

							contr.setComuneResRapLegPgiu(tornaValoreRS(rs, "COMUNE_RES_RAP_LEG_PGIU"));

							contr.setProvinResRapLegPgiu(tornaValoreRS(rs, "PROVIN_RES_RAP_LEG_PGIU"));

							contr.setFlagVarDomicilioBis(tornaValoreRS(rs, "FLAG_VAR_DOMICILIO_BIS"));

							contr.setFlagOver5VarDomic(tornaValoreRS(rs, "FLAG_OVER_5_VAR_DOMIC"));

							contr.setFlagDatiRap(tornaValoreRS(rs, "FLAG_DATI_RAP"));

							contr.setFlagOver6Rap(tornaValoreRS(rs, "FLAG_OVER_6_RAP"));

						}
						if(contr.getFlagVarRapLegale()!=null && !contr.getFlagVarRapLegale().equals("-") && new Integer(contr.getFlagVarRapLegale()).intValue()!=0)
							contr.setListSrap(listaRapLegali(conn ,chiave));


							contr.setListPIva(listaPartiteIVA(conn ,chiave));

						if(contr.getFlagVarDomicilio()!=null && !contr.getFlagVarDomicilio().equals("-") && new Integer(contr.getFlagVarDomicilio()).intValue()!=0)
							contr.setListVDom(listaVariazioniDomicilio(conn ,chiave));

						ht.put("CONTRIBUENTE",contr);
						if (!conn.isClosed())
							conn.close();
						
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

public List listaRapLegali(Connection conn ,String chiave) throws Exception{

				// faccio la connessione al db
				try {
					List lista=new ArrayList();

					this.initialize();

					String sql = "select * from MI_SIATEL_P_GIU_SRAP where MI_SIATEL_P_GIU_SRAP.FK_PK_PG= ? ";

					int indice = 1;
					//recupero le chiavi di ricerca

					this.setString(indice,chiave);

					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					while (rs.next()){
						//campi della lista
						RapLegale rapL = new RapLegale();
						rapL.setCodEnt(tornaValoreRS(rs, "CODENT"));
						rapL.setIdRap(tornaValoreRS(rs, "KEY_PG_RAP"));
						rapL.setIdContribuente(tornaValoreRS(rs, "FK_PK_PG"));
						rapL.setDataInizioCarRap(tornaValoreRS(rs, "DATA_INICAR_RAP","YYYY/MM/DD"));
						rapL.setDataFineCarRap(tornaValoreRS(rs, "DATA_FINCAR_RAP","YYYY/MM/DD"));
						rapL.setCodiceFiscaleRap(tornaValoreRS(rs, "CODICE_FISCALE_RAP"));
						rapL.setTipoCarRap(tornaValoreRS(rs, "TIPO_CAR_RAP"));
						rapL.setFonteRap(tornaValoreRS(rs, "FONTE_RAP"));
			            lista.add(rapL);
					}


					return lista;
				}
			catch (Exception e) {
				log.error(e.getMessage(),e);
				throw e;
			}
			}

public List listaPartiteIVA(Connection conn ,String chiave) throws Exception{

	// faccio la connessione al db
	try {
		List lista=new ArrayList();

		this.initialize();

		String sql = "select * from MI_SIATEL_P_GIU_PI where MI_SIATEL_P_GIU_PI.FK_PK_PG= ? ";

		int indice = 1;
		//recupero le chiavi di ricerca

		this.setString(indice,chiave);

		prepareStatement(sql);
		java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

		while (rs.next()){
			//campi della lista
			PartitaIVA partIVA = new PartitaIVA();
			partIVA.setCodEnt(tornaValoreRS(rs, "CODENT"));
			partIVA.setIdPartitaIVA(rs.getString("KEY_PG_PI"));
			partIVA.setidContribuente(tornaValoreRS(rs,"FK_PK_PG"));
			partIVA.setPartitaIVA(tornaValoreRS(rs,"PARTITA_IVA"));
			partIVA.setDataCessaPI(tornaValoreRS(rs,"DATA_CESSA_PI","YYYY/MM/DD"));
			partIVA.setMotiviCessaPI(tornaValoreRS(rs,"MOTIVO_CESSA_PI"));
			partIVA.setConfluenzaPI(tornaValoreRS(rs,"CONFLUENZA_PI"));
			lista.add(partIVA);
		}


		return lista;
	}
catch (Exception e) {
	log.error(e.getMessage(),e);
	throw e;
}
}
public List listaVariazioniDomicilio(Connection conn ,String chiave) throws Exception{

	// faccio la connessione al db
	try {
		List lista=new ArrayList();

		this.initialize();

		String sql = "select * from MI_SIATEL_P_GIU_VDOM where MI_SIATEL_P_GIU_VDOM.FK_PK_PG= ? ";

		int indice = 1;
		//recupero le chiavi di ricerca

		this.setString(indice,chiave);

		prepareStatement(sql);
		java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

		while (rs.next()){
			//campi della lista
			VariazioneDomicilio varDom = new VariazioneDomicilio();
			varDom.setCodEnt(tornaValoreRS(rs, "CODENT"));
			varDom.setIdVMod(rs.getString("KEY_PG_SDOM"));
			varDom.setIdContribuente(tornaValoreRS(rs,"FK_PK_PG"));
			varDom.setDataInizioValVDom(tornaValoreRS(rs,"DATA_INIVAL_SDOM","YYYY/MM/DD"));
			varDom.setComuneVDom(tornaValoreRS(rs,"COMUNE_SDOM"));
			varDom.setIndCivVDom(tornaValoreRS(rs,"INDCIV_SDOM"));
			varDom.setProvinciaVDom(tornaValoreRS(rs,"PROVINCIA_SDOM"));
			varDom.setCapVDom(tornaValoreRS(rs,"CAP_SDOM"));
			varDom.setFonteSDom(tornaValoreRS(rs,"FONTE_SDOM"));
			lista.add(varDom);
		}
;
		return lista;
	}
catch (Exception e) {
	log.error(e.getMessage(),e);
	throw e;
}
}
}

