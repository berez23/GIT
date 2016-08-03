package it.webred.mui.consolidation;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.CodiciDup;
import it.webred.mui.model.MiConsComunicazione;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupFabbricatiIdentifica;
import it.webred.mui.model.MiDupFabbricatiInfo;
import it.webred.mui.model.MiDupIndirizziSog;
import it.webred.mui.model.MiDupNotaTras;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiDupTerreniInfo;
import it.webred.mui.model.MiDupTitolarita;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ComunicazioneConverter {
	private static Map<Long, ComunicazioneConverter> _converters = new HashMap<Long, ComunicazioneConverter>();

	public static Map<Long, ComunicazioneConverter> getRunningConverters() {
		return _converters;
	}
	
	private int _rowCount = 0;
	public int getRowCount() {
		return _rowCount;
	}
	
	private int _totalRowCount = 0;
	public int getTotalRowCount() {
		return _totalRowCount;
	}

	public MiConsComunicazione evalComunicazione(MiDupSoggetti soggetto) {
		Set<MiDupTitolarita> titolaritas = soggetto.getMiDupTitolaritas();
		if (titolaritas == null || titolaritas.size() == 0) {
			return null;
		} else {
			//commentato Filippo Mazzini 19.01.12
			//titolaritas = groupGraffati(titolaritas);
			//fine commentato Filippo Mazzini 19.01.12
			
//			MiConsComunicazione comunicazione = (soggetto
//					.getMiConsComunicaziones().iterator().hasNext() ? soggetto
//					.getMiConsComunicaziones().iterator().next() : null);
			Query query = null;
			query = HibernateUtil.currentSession()
					.createQuery("select comunicazione  from  MiConsComunicazione as comunicazione where  comunicazione.miDupSoggetti.iid = :iidSoggetto");
			query.setLong("iidSoggetto", soggetto.getIid());
			Iterator cIterator = query.iterate();
			MiConsComunicazione comunicazione =(cIterator.hasNext()?(MiConsComunicazione)cIterator.next():null);
			if (comunicazione == null) {
				Logger.log().info(
						this.getClass().getName(),
						"comunicazione del soggetto(" + soggetto.getIid()
								+ ") non ancora generata");
				comunicazione = new MiConsComunicazione();
				comunicazione.setMiDupSoggetti(soggetto);
				comunicazione.setMiDupFornitura(soggetto.getMiDupFornitura());
				comunicazione.setMiDupNotaTras(soggetto.getMiDupNotaTras());
				if ("P".equals(soggetto.getTipoSoggetto())) {
					comunicazione.setCodiceFiscale(soggetto.getCodiceFiscale());
					comunicazione.setCognome(soggetto.getCognome());
					comunicazione.setNome(soggetto.getNome());
					comunicazione.setSesso(soggetto.getSesso());
					try {
						comunicazione
								.setDataNascita(MuiFornituraParser.dateParser
										.parse(soggetto.getDataNascita()));
					} catch (ParseException e) {
					} catch (Throwable e) {
					}
				} else {
					comunicazione
							.setCodiceFiscale(soggetto.getCodiceFiscaleG());
					comunicazione.setCognome(soggetto.getDenominazione());
				}
				MiDupNotaTras nota = soggetto.getMiDupNotaTras();

				MiDupIndirizziSog indirizzo = null;
				if (soggetto.getMiDupIndirizziSogs() != null
						&& soggetto.getMiDupIndirizziSogs().size() > 0) {
					indirizzo = soggetto.getMiDupIndirizziSogs().iterator()
							.next();
				}
				if (indirizzo != null) {
					comunicazione.setIndirizzo(indirizzo.getIndirizzo());
					comunicazione.setCap(indirizzo.getCap());
					comunicazione.setComune(indirizzo.getComune());
					comunicazione.setProvincia(indirizzo.getProvincia());
				}
				Iterator<MiDupTitolarita> iterator = titolaritas.iterator();
				Set<MiConsOggetto> miConsOggettos = new HashSet<MiConsOggetto>();
				comunicazione.setMiConsOggettos(miConsOggettos);
				while (iterator.hasNext()) {
					MiConsOggetto oggetto = new MiConsOggetto();
					oggetto.setMiDupFornitura(comunicazione.getMiDupFornitura());
					oggetto.setMiConsComunicazione(comunicazione);
					oggetto.setDecorrenza(nota.getDataValiditaAttoDate());
					miConsOggettos.add(oggetto);
					MiDupTitolarita titolarita = iterator.next();
					if ("C".equals(titolarita.getScFlagTipoTitolNota())) {
						oggetto.setCodiceVariazione("C1");
						oggetto.setDescrizioneVariazione("Cessione Possesso");
					} else {
						oggetto.setCodiceVariazione("A1");
						oggetto.setDescrizioneVariazione("Acquisto Possesso");
					}

					Logger.log().info(
							this.getClass().getName(),
							"titolarita del soggetto(" + soggetto.getIid()
									+ ") " + titolarita.getIid());
					oggetto.setMiDupTitolarita(titolarita);
					MiDupFabbricatiInfo fabbricato = titolarita
							.getMiDupFabbricatiInfo();
					if (fabbricato != null) {
						oggetto.setFlagFabbricato(true);
						Logger.log().info(
								this.getClass().getName(),
								"fabbricato del soggetto(" + soggetto.getIid()
										+ ") " + fabbricato.getIid());
						oggetto.setCategoria(fabbricato.getCategoria());
						oggetto.setClasse(fabbricato.getClasse());
						oggetto.setIndirizzo(fabbricato.getTIndirizzo()!= null?fabbricato.getTIndirizzo():"" + " "
								+ fabbricato.getTCivico1()!= null?fabbricato.getTCivico1():"");
						Set<MiDupFabbricatiIdentifica> identificas = fabbricato
								.getMiDupFabbricatiIdentificas();
						if (identificas != null && identificas.size() > 0) {
							MiDupFabbricatiIdentifica identifica = fabbricato
									.getMiDupFabbricatiIdentificas().iterator()
									.next();
							Logger.log().info(
									this.getClass().getName(),
									"identifica del soggetto("
											+ soggetto.getIid() + ") "
											+ identifica.getIid());
							oggetto.setSezione(identifica.getSezioneUrbana());
							oggetto.setFoglio(identifica.getFoglio());
							oggetto.setParticella(identifica.getNumero());
							oggetto.setSubalterno(identifica.getSubalterno());
							oggetto.setNumeroProtocollo(identifica
									.getNumeroProtocollo());
							try {
								oggetto.setAnno(MuiFornituraParser.yearParser
										.parse(identifica.getAnno()));
							} catch (ParseException e) {
							} catch (Throwable e) {
							}
							if ((oggetto.getFoglio() != null && oggetto
									.getFoglio().trim().length() > 0)
									&& (oggetto.getParticella() != null && oggetto
											.getParticella().trim().length() > 0)
									&& (oggetto.getSubalterno() != null && oggetto
											.getSubalterno().trim().length() > 0)) {
								oggetto.setFlagRenditaDefinitiva(true);
							} else {
								oggetto.setFlagRenditaPresunta(true);
							}
							try {
								oggetto.setRedditoEuro((new BigDecimal(fabbricato
										.getRenditaEuro())).divide(BigDecimal.valueOf(100)));
							} catch (Throwable e) {
								Logger.log().error(
										MiConsComunicazione.class.getName(),
										"error while parsing reddito fabbricato "
												+ fabbricato
														.getRenditaEuro()
												+ ", titolarita "
												+ titolarita.getIid(), e);
							}
						}
					}
					MiDupTerreniInfo terreno = titolarita.getMiDupTerreniInfo();
					if (terreno != null) {
						Logger.log().info(
								this.getClass().getName(),
								"terreno del soggetto(" + soggetto.getIid()
										+ ") " + terreno.getIid());
						if("T".equalsIgnoreCase(terreno.getTipologiaImmobile())){
							oggetto.setFlagTerrenoAgricolo(true);
						}
						else if("A".equalsIgnoreCase(terreno.getTipologiaImmobile())){
							oggetto.setFlagAreaFabbricabile(true);
						}
						else if("V".equalsIgnoreCase(terreno.getTipologiaImmobile())){
							//TODO: verde pubbico...
						}

						oggetto.setClasse(terreno.getClasse());
						oggetto.setSezione(terreno.getSezioneCensuaruia());
						oggetto.setFoglio(terreno.getFoglio());
						oggetto.setParticella(terreno.getNumero());
						oggetto.setSubalterno(terreno.getSubalterno());
						oggetto.setFlagRedditoDomenicale(true);
						if(terreno
								.getRedditoDominicaleEuro() != null){
							try {
								oggetto.setRedditoEuro((new BigDecimal(terreno
										.getRedditoDominicaleEuro())).divide(BigDecimal.valueOf(100)));
							} catch (Throwable e) {
								Logger.log().error(
										MiConsComunicazione.class.getName(),
										"error while parsing reddito terreno "
												+ terreno
														.getRedditoDominicaleEuro()
												+ ", titolarita "
												+ titolarita.getIid(), e);
							}
						}
						else{
							Logger.log().error(
									MiConsComunicazione.class.getName(),
									"RedditoDominicaleEuro is null for terreno "
											+ terreno
													.getIid()
											+ ", titolarita "
											+ titolarita.getIid());
						}
					}
					oggetto.setPercentualePossesso(titolarita.getQuota());
					if (titolarita.getCodiceDup() != null &&
							titolarita.getCodiceDup().getCodice() != null &&
							!titolarita.getCodiceDup().getCodice().equals("")) {
						String cod = titolarita.getCodiceDup().getCodice();
						if("1".equals(cod)){
							oggetto.setFlagPossessoProprieta(true);
						}
						else if("8".equals(cod)){
							oggetto.setFlagPossessoUsufrutto(true);
						}
						else if("7".equals(cod)){
							oggetto.setFlagPossessoUso(true);
						}
						else if("3".equals(cod)){
							oggetto.setFlagPossessoDirittoAbitazione(true);
						}
						else if("6".equals(cod)){
							oggetto.setFlagPossessoSuperficie(true);
						}
						else {
							CodiciDup codicedup = (CodiciDup)HibernateUtil.currentSession().load(CodiciDup.class,cod);
							if (codicedup != null) {
								oggetto.setAltroPossesso(codicedup.getCodice()+" - "+codicedup.getDescrizione());
							} else {
								//aggiunto Filippo Mazzini 05.12.2013
								oggetto.setAltroPossesso(cod + " - codice non censito");
							}
						}
					} else {
						//aggiunto Filippo Mazzini 05.12.2013, caso che si è verificato per la prima volta a Milano nella fornitura di maggio 2011
						oggetto.setAltroPossesso("Non specificato");
					}
				}
			}
			return comunicazione;
		}
	}

	public static Set<MiDupTitolarita> groupGraffati(
			Set<MiDupTitolarita> titolaritas) {
		Comparator<MiDupTitolarita> c = new Comparator<MiDupTitolarita>() {
			public int compare(MiDupTitolarita ti1, MiDupTitolarita ti2) {
				// if(ti1.getMiDupNotaTras().getIid() !=
				// ti2.getMiDupNotaTras().getIid() ){
				// return (int)(ti1.getMiDupNotaTras().getIid() -
				// ti2.getMiDupNotaTras().getIid());
				// }
				MiDupFabbricatiInfo fa1 = ti1.getMiDupFabbricatiInfo();
				MiDupFabbricatiInfo fa2 = ti2.getMiDupFabbricatiInfo();
				if (fa1 != null && fa2 != null) {
					if (fa1.getFlagGraffato() != null
							&& fa2.getFlagGraffato() != null) {
						return Integer.valueOf(fa1.getFlagGraffato())
								.intValue()
								- Integer.valueOf(fa2.getFlagGraffato())
										.intValue();
					} else
						return (int) (ti1.getIid() - ti2.getIid());
				} else
					return (int) (ti1.getIid() - ti2.getIid());
			}
		};
		Set<MiDupTitolarita> res = new TreeSet<MiDupTitolarita>(c);
		Iterator<MiDupTitolarita> iter = titolaritas.iterator();
		while (iter.hasNext()) {
			MiDupTitolarita titolarita = iter.next();
			if (!res.contains(titolarita)) {
				res.add(titolarita);
			} else {
				Logger.log().info(
						MiConsComunicazione.class.getName(),
						"grouping graffato for nota "
								+ titolarita.getMiDupNotaTras().getIid()
								+ ", titolarita " + titolarita.getIid());
			}
		}
		return res;
	}
	
	public static ComunicazioneConverter getRunningComunicazioneConverter(String iidFornitura){
		return getRunningComunicazioneConverter( Long.valueOf(iidFornitura));
	}
	
	public static ComunicazioneConverter getRunningComunicazioneConverter(Long iidFornitura){
		return getRunningConverters().get(iidFornitura);
	}

	public void consolidateFornitura(String iidFornitura) {
		getRunningConverters().put( Long.valueOf(iidFornitura),this);
		MuiApplication.getMuiApplication().getServletContext().setAttribute("converters",getRunningConverters());
		Session session = HibernateUtil.currentSession();
		Transaction transaction= session.beginTransaction();
		try {
			Query query = null;
			query = session.createQuery("select distinct sgt  from  MiDupFornitura as c,  MiDupNotaTras nota, MiDupTitolarita as tit,  MiDupSoggetti as sgt  where  c.iid = :iidFornitura   and   nota.miDupFornitura = c   and   tit.miDupNotaTras = nota  and   sgt = tit.miDupSoggetti order by  sgt.iid asc");
			query.setString("iidFornitura", iidFornitura);			
			//primo ciclo per conteggio
			Iterator soggettiIterator = query.iterate();
			while (soggettiIterator.hasNext()) {
				_totalRowCount++;
				soggettiIterator.next();
			}
			//secondo ciclo per trattamento dati
			soggettiIterator = query.iterate();
			while (soggettiIterator.hasNext()) {
				_rowCount++;
				MiDupSoggetti soggetto = (MiDupSoggetti) soggettiIterator.next();
				MiConsComunicazione com = evalComunicazione(soggetto);
				if (com != null) {
					session.saveOrUpdate(com);
					Set<MiConsOggetto> oggettos = com.getMiConsOggettos();
					Iterator iter = oggettos.iterator();
					while (iter.hasNext()) {
						session.saveOrUpdate(iter.next());
					}
				}
			}
			transaction.commit();
		} catch (Throwable e) {
			Logger.log().error(this.getClass().getName(), "errore durante la generazione della comunicazione per la fornitura="+iidFornitura, e);
			try {
				transaction.rollback();
			} catch (HibernateException e1) {
			}
		} finally {
			try {
				session.flush();
			} catch (HibernateException e) {
				Logger.log().error(this.getClass().getName(), "errore durante la generazione della comunicazione per la fornitura="+iidFornitura, e);
			}
			try {
				HibernateUtil.closeSession();
			} catch (HibernateException e) {
				Logger.log().error(this.getClass().getName(), "errore durante la generazione della comunicazione per la fornitura="+iidFornitura, e);
			}
			getRunningConverters().remove( Long.valueOf(iidFornitura));
		}
	}
}
