package it.webred.mui.consolidation;

import it.webred.mui.consolidation.DapEvaluator.MultiPossessoType;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.model.Familiare;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.MiDupFabbricatiIdentifica;
import it.webred.mui.model.MiDupFabbricatiInfo;
import it.webred.mui.model.MiDupFornitura;
import it.webred.mui.model.MiDupNotaTras;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiDupTitolarita;
import it.webred.mui.model.MiVwNoteDapToBeExported;
import it.webred.mui.model.Possesore;
import it.webred.mui.model.Residenza;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DapManager {

	public static HashSet<String> tipologieAbitazione = null;
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
		MuiApplication.getMuiApplication().getServletContext().setAttribute(
				"dapManagers", getRunningDapManagers());
		Session session = HibernateUtil.currentSession();
		try {
			Query query = null;
			while (true) {
				query = session
						.createQuery("select todap  from  MiVwNoteDapToBeExported as todap where todap.miDupFornitura.iid = :iidFornitura");
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
							MiDupTitolarita titolarita = todap
									.getMiDupTitolarita();
							MiDupFabbricatiInfo fabInfo = titolarita
									.getMiDupFabbricatiInfo();
							MiDupFabbricatiIdentifica fabId = null;
							if (fabInfo != null) {
								fabId = fabInfo.getMiDupFabbricatiIdentifica();
							}
							MiConsDap dap = new MiConsDap();
							MiDupSoggetti sgt = titolarita.getMiDupSoggetti();
							MiDupNotaTras nota = titolarita.getMiDupNotaTras();
							MiDupFornitura fornitura = titolarita
									.getMiDupFornitura();

							dap.setMiDupTitolarita(titolarita);
							dap.setMiDupNotaTras(nota);
							dap.setMiDupFornitura(fornitura);
							dap.setMiDupSoggetti(sgt);
							dap.setIdNota(nota.getIdNota());
							dap.setTipologiaImmobile(titolarita
									.getTipologiaImmobile());
							dap.setIdSoggettoCatastale(titolarita
									.getIdSoggettoCatastale());
							dap.setIdSoggettoNota(titolarita
									.getIdSoggettoNota());
							dap.setIdImmobile(titolarita.getIdImmobile());
							if (sgt == null) {
								dap.setFlagSkipped(true);
							} else {
								if (fabInfo == null) {
									dap.setFlagSkipped(true);
								} else {
									dap.setTipoSoggetto(sgt.getTipoSoggetto());
									dap
											.setCodiceFiscale(sgt
													.getCodiceFiscale() != null ? sgt
													.getCodiceFiscale()
													: sgt.getCodiceFiscaleG());
									try {
										dap.setFoglio(Integer.valueOf(fabId
												.getFoglio()));
									} catch (Exception e) {
										Logger
												.log()
												.error(
														this.getClass()
																.getName(),
														"errore durante la conversione a intero del foglio valutazione DAP per la fornitura="
																+ iidFornitura,
														e);
									}
									try {
										dap.setSubalterno(Integer.valueOf(fabId
												.getSubalterno()));
									} catch (Exception e) {
										Logger
												.log()
												.error(
														this.getClass()
																.getName(),
														"errore durante la conversione a intero del subalterno valutazione DAP per la fornitura="
																+ iidFornitura,
														e);
									}
									dap.setNumero(fabId.getNumero());
									dap.setDataInizialeDate(fornitura
											.getDataInizialeDate());
									dap.setDataFinaleDate(fornitura
											.getDataFinaleDate());
									if (!"P".equalsIgnoreCase(sgt
											.getTipoSoggetto())
											|| !DapManager
													.isTipologiaImmobileAbitativo(fabInfo
															.getCategoria())) {
										dap.setFlagSkipped(true);
									} else {
										dap.setFlagSkipped(false);
										DetrazioneManager dm = new DetrazioneManager(
												sgt, dap);
										boolean foundDetrazione = dm
												.evalDetrazione();
										dap.setFlagDapDiritto(foundDetrazione);
										Residenza res = dm
												.getResidenzaConDetrazione();
										if (res != null) {
											dap
													.setDapData(res
															.getDataDaDate() != null && 
															res.getDataDaDate().after(
																	nota.getDataValiditaAttoDate()) ? 
																	res.getDataDaDate() : nota.getDataValiditaAttoDate());
										} else {
											dap.setDapData(nota
													.getDataValiditaAttoDate());
										}

										DapEvaluator dapE = DapEvaluatorFactory
												.getInstance()
												.getDapEvaluator();
										List<Familiare> familiari = dapE
												.getFamiliares(dap);
										Logger
										.log()
										.info(
												this.getClass()
														.getName(),
												"trovati "+familiari.size()+" familiari per "
														+ dap.getCodiceFiscale());
										
										List<Possesore> possesori = dapE
												.getPossesores(dap);
										Logger
										.log()
										.info(
												this.getClass()
														.getName(),
												"trovati "+possesori.size()+" comproprietari per "
														+ dap.getCodiceFiscale());

										int famContitolari = 0;
										BigDecimal totalePossesso = new BigDecimal(
												0);
										Familiare self = new Familiare();
										self.setCodiceFiscale(dap
												.getCodiceFiscale());
										for (Iterator<Possesore> iter = possesori
												.iterator(); iter.hasNext();) {
											Possesore possesore = iter.next();
											totalePossesso = totalePossesso
											.add(possesore
													.getPercentualePossesso());
											Logger
											.log()
											.info(
													this.getClass()
															.getName(),
													"controllo per possesore "
															+ possesore+" perc="+possesore
															.getPercentualePossesso());
											if (!possesore.equals(self)) {
												for (Iterator<Familiare> iterFam = familiari
														.iterator(); iterFam
														.hasNext();) {
													Familiare familiare = iterFam
															.next();
													Logger
													.log()
													.info(
															this.getClass()
																	.getName(),
															"controllo per familiare "
																	+ familiare+" se uguale a "+possesore);
													if (possesore
															.equals(familiare)) {
														famContitolari++;
														Logger
														.log()
														.info(
																this.getClass()
																		.getName(),
																"trovato un familiare contitolare, conto="+famContitolari);
													}
												}
											}
										}
										if (totalePossesso
												.compareTo(new BigDecimal(100)) == 0) {
											dap.setFlagRegoleDapPrecentualePossessoTotaleErrata(true);
										}
										dap
												.setDapNumeroSoggetti(famContitolari);
										MultiPossessoType mps = dapE
												.getPossedutoImmobiles(dap);
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
							Logger.log().error(
									this.getClass().getName(),
									"errore durante la valutazione DAP per la fornitura="
											+ iidFornitura, e);
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
		} finally {
			_running--;
			try {
				session.flush();
			} catch (HibernateException e) {
				Logger.log().error(
						this.getClass().getName(),
						"errore durante la generazione della comunicazione per la fornitura="
								+ iidFornitura, e);
			}
			getRunningDapManagers().remove(Long.valueOf(iidFornitura));
		}
	}

	public static boolean isTipologiaImmobileAbitativo(String tipologiaImmobile) {
		return (tipologiaImmobile != null ? tipologieAbitazione
				.contains(tipologiaImmobile.toUpperCase()) : false);

	}

}
