package it.webred.mui.consolidation;

import it.webred.mui.consolidation.DapEvaluator.MultiPossessoType;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.model.Coniuge;
import it.webred.mui.model.Familiare;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupFabbricatiIdentifica;
import it.webred.mui.model.MiDupFabbricatiInfo;
import it.webred.mui.model.MiDupFornitura;
import it.webred.mui.model.MiDupNotaTras;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiDupTitolarita;
import it.webred.mui.model.MiVwNoteDapToBeExported;
import it.webred.mui.model.Possesore;
import it.webred.mui.model.Residenza;
import it.webred.utils.GenericTuples;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.skillbill.logging.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.HibernateIterator;

public class DapManager {

	public static HashSet<String> tipologieAbitazione = null;
	public static HashSet<String> tipologiePertinenzePerDap = null;
	static {
		tipologieAbitazione = new HashSet<String>();
		tipologieAbitazione.add("A01");
		tipologieAbitazione.add("A02");
		tipologieAbitazione.add("A03");
		tipologieAbitazione.add("A04");
		tipologieAbitazione.add("A05");
		tipologieAbitazione.add("A06");
		tipologieAbitazione.add("A07");
		tipologieAbitazione.add("A08");
		tipologieAbitazione.add("A09");
		
		tipologiePertinenzePerDap = new HashSet<String>();
		tipologiePertinenzePerDap.add("C02");
		tipologiePertinenzePerDap.add("C06");
		tipologiePertinenzePerDap.add("C07");
		
	}

	private static Map<Long, DapManager> _dapManagers = new HashMap<Long, DapManager>();

	private static boolean _forEachRow = false;

	private static boolean _stop = false;

	private static int _running = 0;

	private int _rowCount = 0;

	public int getRowCount() {
		return _rowCount;
	}

	public static Map<Long, DapManager> getRunningDapManagers() {
		return _dapManagers;
	}

	public DapManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static DapManager getRunningDapManagers(String iidFornitura) {
		return getRunningDapManagers(Long.valueOf(iidFornitura));
	}

	public static DapManager getRunningDapManagers(Long iidFornitura) {
		return getRunningDapManagers().get(iidFornitura);
	}

	public static void stop() {
		_stop = true;
		while (_running > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_stop = false;
	}

	public void manageDap(String iidFornitura) {
		_running++;
		getRunningDapManagers().put(Long.valueOf(iidFornitura), this);
		MuiApplication.getMuiApplication().getServletContext().setAttribute("dapManagers", getRunningDapManagers());
		HashMap<String, String[]> codFiscTitToChangeQuotaTo1 = new HashMap<String, String[]>();
		HashMap<String,String[]> codFiscTitToChangeQuotaTo0 = new HashMap<String,String[]>();
		HashMap<String,GenericTuples.T2<String[], BigDecimal>> codFiscTitToChangeCodiceVariazione = new HashMap<String, GenericTuples.T2<String[], BigDecimal>>();
		try {
			Session session = HibernateUtil.currentSession();
			Query query = null;
			while (true) {
				query = session.createQuery("select todap from MiVwNoteDapToBeExported as todap where todap.miDupFornitura.iid = :iidFornitura");
				query.setMaxResults(1);
				query.setString("iidFornitura", iidFornitura);
				Iterator todapIterator = query.iterate();
				MiVwNoteDapToBeExported todap = null;
				if (todapIterator.hasNext()) {
					todap = (MiVwNoteDapToBeExported) todapIterator.next();
					if (todap != null) {
						// while (todapIterator.hasNext()) {
						Transaction transaction = null;
						_rowCount++;
						try {
							transaction = session.beginTransaction();
							MiDupTitolarita titolarita = todap.getMiDupTitolarita();
							MiDupFabbricatiInfo fabInfo = titolarita.getMiDupFabbricatiInfo();
							MiDupFabbricatiIdentifica fabId = null;
							BigDecimal percRiferimentoQuota = new BigDecimal(0);
							if (fabInfo != null) {
								fabId = fabInfo.getMiDupFabbricatiIdentifica();
								
								// vado a vedere i soggetti contro di questo fabbricato
								for (Iterator<MiDupTitolarita> iter = fabInfo.getMiDupTitolaritas().iterator(); iter.hasNext();) {
									MiDupTitolarita tit = iter.next();
									if ("C".equalsIgnoreCase(tit.getScFlagTipoTitolNota())) {
										percRiferimentoQuota = percRiferimentoQuota.add(tit.getQuota()==null?new BigDecimal(0):tit.getQuota());
									}
										
								}
							}
							if (percRiferimentoQuota==null)
								percRiferimentoQuota = new BigDecimal(1);
							
							MiConsDap dap = new MiConsDap();
							MiDupSoggetti sgt = titolarita.getMiDupSoggetti();
							MiDupNotaTras nota = titolarita.getMiDupNotaTras();
							MiDupFornitura fornitura = titolarita.getMiDupFornitura();

							dap.setMiDupTitolarita(titolarita);
							dap.setMiDupNotaTras(nota);
							dap.setMiDupFornitura(fornitura);
							dap.setMiDupSoggetti(sgt);
							dap.setIdNota(nota.getIdNota());
							dap.setTipologiaImmobile(titolarita.getTipologiaImmobile());
							dap.setIdSoggettoCatastale(titolarita.getIdSoggettoCatastale());
							dap.setIdSoggettoNota(titolarita.getIdSoggettoNota());
							dap.setIdImmobile(titolarita.getIdImmobile());
							if (sgt == null) {
								dap.setFlagSkipped(true);
							} else {
								if (fabInfo == null) {
									dap.setFlagSkipped(true);
								} else {
									
									
									dap.setTipoSoggetto(sgt.getTipoSoggetto());
									dap.setCodiceFiscale(sgt.getCodiceFiscale() != null ? sgt.getCodiceFiscale() : sgt.getCodiceFiscaleG());
									try {
										dap.setFoglio(Integer.valueOf(fabId.getFoglio()));
									} catch (Exception e) {
										Logger.log().error(this.getClass().getName(), "errore durante la conversione a intero del foglio valutazione DAP per la fornitura=" + iidFornitura, e);
									}
									try {
										dap.setSubalterno(Integer.valueOf(fabId.getSubalterno()));
										// Catch added by MaX - 07/08/2007
									} catch (java.lang.NumberFormatException nfe) {
										Logger.log().info(this.getClass().getName(), "Attenzione: trovato valore del subalterno eguale a NULL durante la valutazione DAP per la fornitura=" + iidFornitura);
									} catch (Exception e) {
										Logger.log().error(this.getClass().getName(), "errore durante la conversione a intero del subalterno valutazione DAP per la fornitura=" + iidFornitura, e);
									}
									dap.setNumero(fabId.getNumero());
									dap.setDataInizialeDate(fornitura.getDataInizialeDate());
									dap.setDataFinaleDate(fornitura.getDataFinaleDate());
									if (!"P".equalsIgnoreCase(sgt.getTipoSoggetto())) { // MARCORIC 8-10-2008 PER IL mUI ANDAVA BENE SELEZIONARE SOLO L'ABITATIVO MA PER LE SUCCESSIONI NO || !DapManager.isTipologiaImmobileAbitativo(fabInfo.getCategoria())) {
										Logger.log().info(this.getClass().getName(), "Dap NON applicabile per " + sgt.getCodiceFiscale() + ". Categoria = " + fabInfo.getCategoria());
										dap.setFlagSkipped(true);
									} else {
										Logger.log().info(this.getClass().getName(), "Dap applicabile per " + sgt.getCodiceFiscale() + ". Categoria = " + fabInfo.getCategoria());
										dap.setFlagSkipped(false);
										DetrazioneManager dm = new DetrazioneManager(sgt, dap);
										
										/* non si sposta la data dalla quale vedere il diritto di abitazione
										 * è sempre quella della nota, non come sul mui che viene spostata a seconda di
										 * quando uno ci va ad abitare
										if (res != null) {
											dap.setDapData(res.getDataDaDate() != null && res.getDataDaDate().after(nota.getDataValiditaAttoDate()) ? res.getDataDaDate() : nota.getDataValiditaAttoDate());
										} else {
											dap.setDapData(nota.getDataValiditaAttoDate());
										}
										la riga sotto basta
										 */
										dap.setDapData(nota.getDataValiditaAttoDate());

										boolean foundDetrazione = dm.evalDetrazione();
										// applico diritto, se lo hanno, soltanto ai soggetti a favore
										dap.setFlagDapDiritto("C".equalsIgnoreCase(titolarita.getScFlagTipoTitolNota())?false:foundDetrazione);

										
										DapEvaluator dapE = DapEvaluatorFactory.getInstance().getDapEvaluator();

										

										
										List<Familiare> familiari = dapE.getFamiliares(dap);
										Logger.log().info(this.getClass().getName(), "Trovati " + familiari.size() + " familiari per " + dap.getCodiceFiscale());


										int famContitolari = 0;
										BigDecimal totalePossesso = new BigDecimal(0);
										Familiare self = new Familiare();
										self.setCodiceFiscale(dap.getCodiceFiscale());
										
										// INIZIO - SOLO PER TEST
										/*if (true || codFiscTitToChangeCodiceVariazione.isEmpty()) {
											codFiscTitToChangeCodiceVariazione.put(dap.getCodiceFiscale(), BigDecimal.TEN);
										}*/
										// FINE - SOLO PER TEST
										
										try {
											List<Possesore> possesori = dapE.getPossesores(dap);
											Logger.log().info(this.getClass().getName(), "Trovati " + possesori.size() + " comproprietari per " + dap.getCodiceFiscale());
											// TODO: verificare questa for e l'informazione dei familiari contitolari a cosa serve
											for (Iterator<Possesore> iter = possesori.iterator(); iter.hasNext();) {
												Possesore possesore = iter.next();
												totalePossesso = totalePossesso.add(possesore.getPercentualePossesso());
												Logger.log().info(this.getClass().getName(), "Controllo per possesore " + possesore + " perc=" + possesore.getPercentualePossesso());
												if (!possesore.equals(self)) {
													for (Iterator<Familiare> iterFam = familiari.iterator(); iterFam.hasNext();) {
														Familiare familiare = iterFam.next();
														Logger.log().info(this.getClass().getName(), "Controllo per familiare " + familiare + " se uguale a " + possesore);
														if (possesore.equals(familiare)) {
															famContitolari++;
															Logger.log().info(this.getClass().getName(), "Trovato un familiare contitolare, conto=" + famContitolari);
														}
													}
												}
											}
										} catch (Exception e) {
										}
										

										List<Possesore> quotePossesso = dapE.getQuote(dap);

										// ENTRO PER SOGGETTO A FAVORE, cioè uno degli eredi
										if (!"C".equalsIgnoreCase(titolarita.getScFlagTipoTitolNota())) {
											Logger.log().info(this.getClass().getName(), "Trovato erede " + self.getCodiceFiscale() + " già propietario dell'immobile");
											// tolto questo : BigDecimal quotaCorrente = titolarita.getQuota();
											// visto che la quota presente nella nota non è la perc di possesso dell'immoile ma è semplicemente
											// la percentuale di eredità rispetto alla nota
											// se infatti il defunto lascia in eredità il 50% ,e l'erede ha quota 50% si ha che l'erede viene in possesso del 50% del 50
											BigDecimal quotaCorrente = titolarita.getQuota()==null?new BigDecimal(0):titolarita.getQuota();
											//BigDecimal quotaCorrente = (percRiferimentoQuota).multiply(titolarita.getQuota()==null?new BigDecimal(0):titolarita.getQuota()).divide(new BigDecimal(100));
											if (quotaCorrente == null) {
												Logger.log().info(this.getClass().getName(), "Quota corrente NULLA. La metto eguale a 0");
												quotaCorrente = new BigDecimal(0);
											}
											// Se si tratta di un erede che è anche già possessore dell'immobile devo modificare la
											// quota e il valore del campo Codice Variazione
											boolean titolarePossessore = false;
											for (Iterator<Possesore> iter = quotePossesso.iterator(); iter.hasNext();) {
												Possesore possesore = iter.next();
												if (possesore.equals(self)) {
													titolarePossessore = true;
													BigDecimal percentualePossessoInQuota = (possesore.getPercentualePossesso()).divide(new BigDecimal(100));
													Logger.log().info(this.getClass().getName(), "QuotaCorrente: " + quotaCorrente + " - PercentualePossessoInQuota: " + percentualePossessoInQuota);
													BigDecimal newQuota = quotaCorrente.add(percentualePossessoInQuota);
													Logger.log().info(this.getClass().getName(), "New Quota: " + newQuota.toPlainString());
													// titolarita.setQuota(newQuota);
													if (percentualePossessoInQuota.compareTo(BigDecimal.ZERO) == 1 && newQuota.compareTo(BigDecimal.ONE) < 1) {
														// Se percentualePossessoInQuota è > 0 e newQuota <= 1
														Logger.log().info(this.getClass().getName(), "Aggiungo a codFiscTitToChangeCodiceVariazione il soggetto: " + self.getCodiceFiscale());
														String[] cfFabid = new String[]{self.getCodiceFiscale(), ""+fabInfo.getIid()};
														codFiscTitToChangeCodiceVariazione.put(ArrayUtils.toString(cfFabid),new GenericTuples.T2<String[], BigDecimal>(cfFabid, newQuota));
														// Cambiare MiConsOggetto.codiceVariazione da A1 in 'V'
													}
												}
											}
			
												
										}
										
										
										
										// calcolo DAP solo per immobile abitativi e C2 C6 C7	
										if (DapManager.isTipologiaImmobilePerDap(fabInfo.getCategoria())) {
											
											// Se si sta elaborando il defunto devo andare a verificare l'indirizzo del coniuge recuperandolo con oppportune
											// query (e quindi non fidandomi dei valori che trovo nelle mie tabelle).
											// Nel caso l'indirizzo risulti essere eguale a quello del defunto metto le percentuali tutte a zero quelle degli eredi
											// tranne quella della moglie che avrà il diritto di abitazione sull'immobile
											try {
												if ("C".equalsIgnoreCase(titolarita.getScFlagTipoTitolNota())) {
													String indirizzoImmobile = (fabInfo.getCIndirizzo() != null && fabInfo.getCIndirizzo().trim().length() > 0) ? fabInfo.getCIndirizzo() : fabInfo.getTIndirizzo();
													Logger.log().info(this.getClass().getName(), "Elaborazione deceduto. Indirizzo immobile =" + indirizzoImmobile);
													Coniuge coniuge = dapE.getConiugeInVita(dap);
													if (coniuge != null) {
														Logger.log().info(this.getClass().getName(), "Coniuge recuperato. Cod Fisc = " + coniuge.getCodiceFiscale());
														Logger.log().info(this.getClass().getName(), "Coniuge recuperato. Residenza = " + coniuge.getResidenza());
														// In data 16/02/2008 aggiunto il settaggio del campo COD_FISC_CONIUGE della tabella SUC_DUP_NOTA_TRAS
														nota.setCodFiscConiuge(coniuge.getCodiceFiscale());
														// if (coniuge != null && indirizzoImmobile.equalsIgnoreCase(coniuge.getIndirizzo())) {
	

														Residenza res = dm.getResidenzaConDetrazione();
														

														// presi gli oggetti della nota
														// seleziono con la if quelli che stanno all'indirizzo in detrazione
														// e di questi vedo se sono = all'oggetto della riga dap che sto trattando
														// se si allora c'è il match e dunque posso eventualmente il conuge risieda li, applicare il diritto
														boolean match = false;
														for (Iterator<MiConsOggetto> iterConsOgg = titolarita.getMiConsOggettos().iterator(); iterConsOgg.hasNext();) {
															MiConsOggetto consOggetto = iterConsOgg.next();
															if (res.matchOggetto(consOggetto)) {
																// soltanto nel caso che io stia scorrendo
																// la riga dell'oggetto che è abitazone principale
																// posso assegnare diritto abitazione
																match = 
																	dm.compareNullableValue(dap.getFoglio(), consOggetto.getFoglio()==null?null:new Integer(consOggetto.getFoglio())) == 0 && 
																	dm.compareNullableValue(dap.getNumero(), consOggetto.getParticella()) == 0 &&
																	dm.compareNullableValue(dap.getSubalterno(), consOggetto.getSubalterno()==null?null:new Integer(consOggetto.getSubalterno())) == 0;
																if (match)
																	break;
															}
														}

														
														if (match && coniuge.getResidenza() != null && coniuge.getResidenza().matchIndirizzo(res)) {
															Logger.log().info(this.getClass().getName(), "Residenza coniuge eguale a quella del defunto");
															String[] key1 = new String[]{coniuge.getCodiceFiscale(), ""+fabInfo.getIid()};
															codFiscTitToChangeQuotaTo1.put(ArrayUtils.toString(key1), key1);
															// Gli altri eredi devono avere quota 0
										
															// MARCORIC : SOSTITUITA LA RICERCA DEI FAMILIARI CON LA RICERCA SUI SOGGETTI DELLA NOTA
															// INFATTI CI POTREBBERO ESSERE SOGGETTI IN NOTA CHE NON SONO FAMILIARI
															for (Iterator<MiDupSoggetti> iterSoggettiNota = nota.getMiDupSoggettis().iterator(); iterSoggettiNota.hasNext();) {
																MiDupSoggetti s = iterSoggettiNota.next();
																// sai che il defunto potrebbe non essere possessore a causa di un errore al catasto
																// allora non entro nella if se s è il defunto
																if (s.getCodiceFiscale() != null && 
																		(
																		!s.getCodiceFiscale().equalsIgnoreCase(coniuge.getCodiceFiscale()) &&
																		!s.getCodiceFiscale().equalsIgnoreCase(self.getCodiceFiscale())
																		)
																	) {
																	// Devo verificare che il soggetto implicato nella nota
																	// in questione non sia il coniuge e anche che non sia un possessore
																	boolean soggPossessore = false;
																	for (Iterator<Possesore> iter = quotePossesso.iterator(); iter.hasNext();) {
																		Possesore possesore = iter.next();
																		if (possesore.getCodiceFiscale()!=null && 
																			possesore.getCodiceFiscale().equalsIgnoreCase(s.getCodiceFiscale())) {
																				soggPossessore = true;
																				break;
																		}
																	}
																	if (!soggPossessore) {
																		String[] key0 = new String[]{s.getCodiceFiscale(), ""+fabInfo.getIid()};
																		codFiscTitToChangeQuotaTo0.put(ArrayUtils.toString(key0), key0);
																	}
																}
															}
															
														}
													}
												}
											} catch (Exception e) {
												Logger.log().error(this.getClass().getName(), "Errore nel recupero coniuge. Comunque vado avanti");
												e.printStackTrace();
											}
										}


										if (totalePossesso.compareTo(new BigDecimal(100)) == 0) {
											dap.setFlagRegoleDapPrecentualePossessoTotaleErrata(true);
										}
										dap.setDapNumeroSoggetti(famContitolari);
										MultiPossessoType mps = dapE.getPossedutoImmobiles(dap);
										if (mps == MultiPossessoType.SEVERAL) {
											dap.setFlagRegoleDapSoggettoPossessorePiuImmobili(true);
										} else if (mps == MultiPossessoType.REPEATED) {
											dap.setFlagRegoleDapSoggettoPossessorePiuImmobiliStessoIndirizzo(true);
										}

									}
								}
							}
							session.saveOrUpdate(dap);
							transaction.commit();
							if (_stop) {
								break;
							}
						} catch (Throwable e) {
							Logger.log().error(this.getClass().getName(), "errore durante la valutazione DAP per la fornitura=" + iidFornitura, e);
							try {
								if (transaction != null) {
									transaction.rollback();
								}
							} catch (HibernateException e1) {
							}
						}
						// }
					} else {
						break;
					}
				} else {
					break;
				}
			}

			try {
				session.flush();
				HibernateUtil.closeSession();
			} catch (HibernateException e) {
				Logger.log().error(this.getClass().getName(), "Errore durante il primo FLUSH della sessioni per la fornitura=" + iidFornitura, e);
			}

		} catch (Throwable e) {
			Logger.log().error(this.getClass().getName(), "Errore durante il cacolo Dap", e);
		}
		
		try {
			try {
			// A questo punto modifico le eventuali quote o Codice Variazione
			Session session = HibernateUtil.currentSession();
			try {
				Logger.log().info(this.getClass().getName(), "Inizio codice per modificare eventuali Quote o Codice Variazione. Elementi" + codFiscTitToChangeCodiceVariazione.size());

/***** INIZIO - Modifica del codice variazione a 'V' per gli eredi già possessori di una quota dell'immobile *****/
				for (Map.Entry<String,GenericTuples.T2<String[], BigDecimal>> entry : codFiscTitToChangeCodiceVariazione.entrySet()) {

					String[] cfFabid = ((GenericTuples.T2<String[], BigDecimal>) entry.getValue()).firstObj;
					String codFiscCurr = cfFabid[0];
					String idFabbricatoInfoCurr = cfFabid[1];
					HibernateIterator iter = null;
					HibernateIterator iterRCO = null;
					Transaction transactionPost = session.beginTransaction();
					try {
						// Siccome la "regola del coniuge" è predominante, se l'erede di cui sto trattando è già "implicato" in
						// una gestione di questo tipo lo salto
						if (codFiscTitToChangeQuotaTo1.containsKey(entry.getKey()) || codFiscTitToChangeQuotaTo0.containsKey(entry.getKey())) {
							continue;
						}

						MiDupTitolarita titCurrToV = null;
						Query queryRF = session.createQuery("SELECT tit FROM MiDupTitolarita as tit, MiDupSoggetti as sgt, MiDupFornitura as forn, MiDupFabbricatiInfo as finfo" + " WHERE sgt = tit.miDupSoggetti AND tit.miDupFornitura = forn AND tit.miDupFabbricatiInfo = finfo AND sgt.codiceFiscale = :codice_fiscale AND forn.iid = :iidFornitura AND finfo.iid = :iidFabbricatiInfo");
						queryRF.setString("codice_fiscale", codFiscCurr);
						queryRF.setString("iidFornitura", iidFornitura);
						queryRF.setString("iidFabbricatiInfo", idFabbricatoInfoCurr);
						Logger.log().info("Query  recupero titolarità per modificare Codice Variazione: ", queryRF);
						Logger.log().info(this.getClass().getName(), "Param codice_fiscale: " + codFiscCurr);
						Logger.log().info(this.getClass().getName(), "Param id_fornitura: " + iidFornitura);
						Logger.log().info(this.getClass().getName(), "Param id_fabbricato_info: " + idFabbricatoInfoCurr);
						
						iter = (HibernateIterator) queryRF.iterate();
						Logger.log().info(this.getClass().getName(), "ESEGUITA QUERY recupero titolarità");
						/**if (iter != null && iter.hasNext()) {
							titCurr = (MiDupTitolarita) iter.next();
						}*/
						while (iter != null && iter.hasNext()) {
						
							//titCurr = (MiDupTitolarita)queryRF.uniqueResult();
							//Logger.log().info(this.getClass().getName(), "ESEGUITA QUERY recupero titolarità");
							
							titCurrToV = (MiDupTitolarita) iter.next();
							if (titCurrToV == null) {
								Logger.log().info(this.getClass().getName(), "Non recuperata");
							} else {
								long iidTitCurr = titCurrToV.getIid();
								BigDecimal newQuota = ((GenericTuples.T2<String[], BigDecimal>) entry.getValue()).secondObj;
								
								if (newQuota.compareTo(titCurrToV.getQuota()) != 0) {
									Logger.log().info(this.getClass().getName(), "Modifico la Quota della titolarità " + titCurrToV.getIid() + " da " + titCurrToV.getQuota() + " a " + newQuota);
									//titCurrToV.setQuota(newQuota);


									if (titCurrToV.getMiConsOggettos() != null ) {
										for (Iterator<MiConsOggetto> iterConsOgg = titCurrToV.getMiConsOggettos().iterator(); iterConsOgg.hasNext();) {
											MiConsOggetto consOggetto = iterConsOgg.next();
											Logger.log().info(this.getClass().getName(), "Recuperato cons_oggetto con iid=" + consOggetto.getIid());
											String codVarOLD = consOggetto.getCodiceVariazione();
											BigDecimal percPossOLD = consOggetto.getPercentualePossesso(); 
											BigDecimal percPossNEW = newQuota.multiply(new BigDecimal(100));
											if (percPossOLD.compareTo(percPossNEW) != 0 || !"V".equalsIgnoreCase(codVarOLD)) {
												Logger.log().info(this.getClass().getName(), "Cons_oggetto da modificare");
												consOggetto.setPercentualePossesso(percPossNEW);
												consOggetto.setCodiceVariazione("V");												
											}
											//session.saveOrUpdate(consOggetto);
										}
									}
									
									session.saveOrUpdate(titCurrToV);
								}
								
							}
						}
						transactionPost.commit();	

					} catch (HibernateException e) {
						Logger.log().error(this.getClass().getName(), "Errore nel settaggio del codice variazione per il soggetto: " + codFiscCurr, e);
						transactionPost.rollback();
					} catch (Exception e) {
						Logger.log().error(this.getClass().getName(), "Errore nel settaggio del codice variazione per il soggetto: " + codFiscCurr, e);
						transactionPost.rollback();
					} finally {
						if (iter != null) {
							Hibernate.close(iter);
						}
						if (iterRCO != null) {
							Hibernate.close(iterRCO);
						}
					}
				}
				} catch (Exception e) {
					Logger.log().error(this.getClass().getName(), "Errore nel settaggio del codice variazione per il soggetto: ", e);
				}

/***** FINE - Modifica del codice variazione a 'V' per gli eredi già possessori di una quota dell'immobile *****/

				try {
/***** INIZIO - Modifica della quota a 1 per i coniugi coinquilini del deceduto *****/
					Logger.log().info(this.getClass().getName(), "Esistono delle Quote da valorizzare a 1 . Elementi=" +  codFiscTitToChangeQuotaTo1.size() );

					for (Map.Entry<String,String[]> entry : codFiscTitToChangeQuotaTo1.entrySet()) {
					HibernateIterator iter = null;
					Transaction transactionPost =  session.beginTransaction();

					try {

						String[] cfFabid = (String[]) entry.getValue();
						String codFiscCurr = cfFabid[0];
						String idFabbricatoInfoCurr = cfFabid[1];

						
						MiDupTitolarita titCurrTo1 = null;
						Query queryRF = session.createQuery("SELECT tit FROM MiDupTitolarita as tit, MiDupSoggetti as sgt, MiDupFornitura as forn, MiDupFabbricatiInfo as finfo" + " WHERE sgt = tit.miDupSoggetti AND tit.miDupFornitura = forn AND tit.miDupFabbricatiInfo = finfo AND sgt.codiceFiscale = :codice_fiscale AND forn.iid = :iidFornitura AND finfo.iid = :iidFabbricatiInfo");
						queryRF.setString("codice_fiscale", codFiscCurr);
						queryRF.setString("iidFornitura", iidFornitura);
						queryRF.setString("iidFabbricatiInfo", idFabbricatoInfoCurr);
						Logger.log().info("Query  recupero titolarità per settare Quota=1: ", queryRF);
						Logger.log().info(this.getClass().getName(), "Param codice_fiscale: " + codFiscCurr);
						Logger.log().info(this.getClass().getName(), "Param id_fornitura: " + iidFornitura);
						Logger.log().info(this.getClass().getName(), "Param id_fabbricato_info: " + idFabbricatoInfoCurr);
						
						iter = (HibernateIterator) queryRF.iterate();
						Logger.log().info(this.getClass().getName(), "ESEGUITA QUERY recupero titolarità");
						/**if (iter != null && iter.hasNext()) {
							titCurr = (MiDupTitolarita) iter.next();
						}*/

						while (iter != null && iter.hasNext()) {
		
							//titCurr = (MiDupTitolarita)queryRF.uniqueResult();
							//Logger.log().info(this.getClass().getName(), "ESEGUITA QUERY recupero titolarità");
							
							titCurrTo1 = (MiDupTitolarita)iter.next();
							if (titCurrTo1 == null) {
								Logger.log().info(this.getClass().getName(), "Non recuperata");
							} else {
								long iidTitCurr = titCurrTo1.getIid();
								//titCurrTo1.setSfQuotaNumeratore("1");
								//titCurrTo1.setSfQuotaDenominatore("1");
								// SETTO IL CODICE DIRITTO A 99 PER APPLICAZIONE DIRITTO ABITAZIONE - IN QUESTO MODO
								// QUANDO E' 99 SO CHE LA PERC. 1 E' A CAUSA DEL DIRITTO DI ABITAZIONE
								titCurrTo1.setScCodiceDiritto("99");
								// se il coniuge possedeva qualcosa allora il codice variazione lo metto a 99
								boolean variazioneV = false;
								if(codFiscTitToChangeCodiceVariazione.containsKey(entry.getKey()))
									variazioneV = true;	

								if (titCurrTo1.getMiConsOggettos() != null ) {
									for (Iterator<MiConsOggetto> iterConsOgg = titCurrTo1.getMiConsOggettos().iterator(); iterConsOgg.hasNext();) {
										MiConsOggetto consOggetto = iterConsOgg.next();
										Logger.log().info(this.getClass().getName(), "Recuperato cons_oggetto con iid=" + consOggetto.getIid());
										consOggetto.setPercentualePossesso(new BigDecimal(100));
										consOggetto.setContitolariAbitazionePrincipale(new Integer(1));
										if (variazioneV)
											consOggetto.setCodiceVariazione("V");
									}
								}								
								
								session.saveOrUpdate(titCurrTo1);
	
							}
						}
						transactionPost.commit();

					} catch (HibernateException e) {
						Logger.log().error(this.getClass().getName(), "Erore nella ricerca a catasto ", e);
						transactionPost.rollback();

					} finally {
						if (iter != null) {
							Hibernate.close(iter);
						}
					}
				}
				} catch (Exception e) {
					Logger.log().error(this.getClass().getName(), "Errore nel settaggio quota per il soggetto: ", e);
				}

/***** FINE - Modifica della quota a 1 per i coniugi coinquilini del deceduto *****/

				try {
/***** INIZIO - Modifica della quota a 0 *****/
					Logger.log().info(this.getClass().getName(), "Esistono delle Quote da valorizzare a 0 . Elementi=" +  codFiscTitToChangeQuotaTo0.size() );
				for (Map.Entry<String,String[]> entry : codFiscTitToChangeQuotaTo0.entrySet()) {
					
					HibernateIterator iter = null;
					Transaction transactionPost = null;

					try {
						String[] cfFabid = (String[]) entry.getValue();
						String codFiscCurr = cfFabid[0];
						String idFabbricatoInfoCurr = cfFabid[1];

						MiDupTitolarita titCurr = null;

						Query queryRF = session.createQuery("SELECT tit FROM MiDupTitolarita as tit, MiDupSoggetti as sgt, MiDupFornitura as forn, MiDupFabbricatiInfo as finfo" + " WHERE sgt = tit.miDupSoggetti AND tit.miDupFornitura = forn AND tit.miDupFabbricatiInfo = finfo AND sgt.codiceFiscale = :codice_fiscale AND forn.iid = :iidFornitura AND finfo.iid = :iidFabbricatiInfo");
						queryRF.setString("codice_fiscale", codFiscCurr);
						queryRF.setString("iidFornitura", iidFornitura);
						queryRF.setString("iidFabbricatiInfo", idFabbricatoInfoCurr);
						Logger.log().info("Query  recupero titolarità per settare Quota=0: ", queryRF);
						Logger.log().info(this.getClass().getName(), "Param codice_fiscale: " + codFiscCurr);
						Logger.log().info(this.getClass().getName(), "Param id_fornitura: " + iidFornitura);
						Logger.log().info(this.getClass().getName(), "Param id_fabbricato_info: " + idFabbricatoInfoCurr);
						
						iter = (HibernateIterator) queryRF.iterate();
						Logger.log().info(this.getClass().getName(), "ESEGUITA QUERY recupero titolarità");
						/**if (iter != null && iter.hasNext()) {
							titCurr = (MiDupTitolarita) iter.next();
						}*/
						transactionPost = session.beginTransaction();
						while (iter != null && iter.hasNext()) {
		
							//titCurr = (MiDupTitolarita)queryRF.uniqueResult();
							//Logger.log().info(this.getClass().getName(), "ESEGUITA QUERY recupero titolarità");
							
							titCurr = (MiDupTitolarita)iter.next();
							if (titCurr == null) {
								Logger.log().info(this.getClass().getName(), "Non recuperata");
							} else {
								long iidTitCurr = titCurr.getIid();
								//titCurr.setSfQuotaNumeratore("0");
								//titCurr.setSfQuotaDenominatore("1"); 
								// SETTO IL CODICE DIRITTO A 99 PER APPLICAZIONE DIRITTO ABITAZIONE - IN QUESTO MODO
								// QUANDO E' 99 SO CHE LA PERC. 0 E' A CAUSA DEL DIRITTO DI ABITAZIONE
								titCurr.setScCodiceDiritto("99");

								if (titCurr.getMiConsOggettos() != null ) {
									for (Iterator<MiConsOggetto> iterConsOgg = titCurr.getMiConsOggettos().iterator(); iterConsOgg.hasNext();) {
										MiConsOggetto consOggetto = iterConsOgg.next();
										Logger.log().info(this.getClass().getName(), "Recuperato cons_oggetto con iid=" + consOggetto.getIid());
										consOggetto.setPercentualePossesso(new BigDecimal(0)); 
									}
								}								
								
								session.saveOrUpdate(titCurr);
								
							}
						}
						transactionPost.commit();
					} catch (HibernateException e) {
						Logger.log().error(this.getClass().getName(), "Erore nella ricerca a catasto ", e);
						transactionPost.rollback();
					} finally {
						if (iter != null) {
							Hibernate.close(iter);
						}
					}
				}
				} catch (Exception e) {
					Logger.log().error(this.getClass().getName(), "Errore nel settaggio quota per il soggetto: ", e);
				}

/***** FINE - Modifica della quota a 0 *****/
				try {
					session.flush();
					HibernateUtil.closeSession();
				} catch (HibernateException e) {
					Logger.log().error(this.getClass().getName(), "Errore durante il primo FLUSH della sessioni per la fornitura=" + iidFornitura, e);
				}

				//transactionPost.commit();
			} catch (Throwable e) {
				Logger.log().error(this.getClass().getName(), "Errore durante la modifica di quote e variazione", e);
			}
		} finally {
			_running--;
			getRunningDapManagers().remove(Long.valueOf(iidFornitura));
		}
	}

	public static boolean isTipologiaImmobileAbitativo(String tipologiaImmobile) {
		return (tipologiaImmobile != null ? tipologieAbitazione.contains(tipologiaImmobile.toUpperCase()) : false);

	}
	
	private static  boolean isTipologiaImmobilePerDap(String tipologiaImmobile) {
		boolean tip = isTipologiaImmobileAbitativo(tipologiaImmobile);
		if (tip)
			return tip;
		else  {
			return (tipologiaImmobile != null ? tipologiePertinenzePerDap.contains(tipologiaImmobile.toUpperCase()) : false);
		}
			
	}
	
	public static void main (String[] args) {
		new BigDecimal(0).multiply(null);
		
	}
	

}
