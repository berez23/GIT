package it.webred.mui.consolidation;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.model.MiConsComunicazione;
import it.webred.mui.model.MiConsIntegrationLog;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupFabbricatiInfo;
import it.webred.mui.model.MiDupFornitura;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiDupTitolarita;
import it.webred.mui.model.MiVwAnagrafe;
import it.webred.mui.model.MiVwCatasto;
import it.webred.mui.model.MiVwContribuenti;
import it.webred.mui.model.Residenza;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import net.skillbill.logging.Logger;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.HibernateIterator;

public class DwhManager extends SqlAdapter {
	// List<MiConsIntegrationLog> logs = new ArrayList<MiConsIntegrationLog>();
	private static Map<Long, DwhManager> _integrators = new HashMap<Long, DwhManager>();
	private static Map<Long, DwhManager> _integratorsFabbr = new HashMap<Long, DwhManager>();

	private static SimpleDateFormat _dataNascitaParser = new SimpleDateFormat("dd/MM/yyyy");


	public static Map<Long, DwhManager> getRunningIntegrators() {
		return _integrators;
	}

	public static Map<Long, DwhManager> getRunningIntegratorsFabbr() {
		return _integratorsFabbr;
	}

	private int _rowCount = 0;

	public int getRowCount() {
		return _rowCount;
	}

	public DwhManager() {

	}
	
	public void integrateAnagrafe(List<MiConsIntegrationLog> logs, MiConsComunicazione comunicazione) {
		MiVwAnagrafe anagrafeEntry = null;
		HibernateIterator iter = null;
		try {
			
			// anagrafeEntry = (MiVwAnagrafe) HibernateUtil.currentSession().load(
			// MiVwAnagrafe.class, comunicazione.getCodiceFiscale());
			Session session = HibernateUtil.currentSession();
	
			Query query = session.createQuery("SELECT anagrafe FROM MiVwAnagrafe AS anagrafe WHERE anagrafe.codiceFiscale = :codiceFiscale ");
			query.setString("codiceFiscale", comunicazione.getCodiceFiscale());
			Logger.log().info("query ", query);
			iter = (HibernateIterator) query.iterate();
			while (iter != null && iter.hasNext()) {
				anagrafeEntry = (MiVwAnagrafe) iter.next();
				if ("A".equalsIgnoreCase(anagrafeEntry.getTipoSoggetto())) {
					break;
				}
			}
			if (anagrafeEntry != null && "A".equalsIgnoreCase(anagrafeEntry.getTipoSoggetto())) {
				if (toIntegrate(anagrafeEntry.getCognome(), comunicazione.getCognome())) {
					Logger.log().info(comunicazione.getClass().getName(), "I001");
					MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "ANAGRAFE", "COGNOME", "I001", "cognome da " + comunicazione.getCognome() + " a " + anagrafeEntry.getCognome(), null, comunicazione.getMiDupFornitura());
					logs.add(log);
					comunicazione.setCognome(anagrafeEntry.getCognome());
				}
				if (toIntegrate(anagrafeEntry.getNome(), comunicazione.getNome())) {
					Logger.log().info(comunicazione.getClass().getName(), "I002");
					MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "ANAGRAFE", "NOME", "I002", "nome da " + comunicazione.getNome() + " a " + anagrafeEntry.getNome(), null, comunicazione.getMiDupFornitura());
					logs.add(log);
					comunicazione.setNome(anagrafeEntry.getNome());
				}
				if (toIntegrate(anagrafeEntry.getDataNascita(), comunicazione.getDataNascita())) {
					Logger.log().info(comunicazione.getClass().getName(), "I003");
					MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "ANAGRAFE", "DATA_NASCITA", "I003", "dataNascita da " + comunicazione.getDataNascita() + " a " + anagrafeEntry.getDataNascita(), null, comunicazione.getMiDupFornitura());
					logs.add(log);
					comunicazione.setDataNascita(anagrafeEntry.getDataNascita());
				}
				if (toIntegrate(anagrafeEntry.getSesso(), ("1".equals(comunicazione.getSesso()) ? "M" : "F"))) {
					Logger.log().info(comunicazione.getClass().getName(), "I004");
					MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "ANAGRAFE", "SESSO", "I004", "sesso da " + ("1".equals(comunicazione.getSesso()) ? "M" : "F") + " a " + anagrafeEntry.getSesso(), null, comunicazione.getMiDupFornitura());
					logs.add(log);
					comunicazione.setCognome(("M".equals(anagrafeEntry.getCognome()) ? "1" : "2"));
				}
				if (toIntegrate(anagrafeEntry.getIndirizzo(), comunicazione.getIndirizzo())) {
					Logger.log().info(comunicazione.getClass().getName(), "I005");
					MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "ANAGRAFE", "INDIRIZZO", "I005", "indirizzo da " + comunicazione.getIndirizzo() + " a " + anagrafeEntry.getIndirizzo(), null, comunicazione.getMiDupFornitura());
					logs.add(log);
					comunicazione.setIndirizzo(anagrafeEntry.getIndirizzo());
				}
			} else {
				/*MiVwContribuenti tributiEntry = null;
				query = session.createQuery("select distinct contribuente from MiVwContribuenti as contribuente  where contribuente.codiceFiscale = :codiceFiscale order by contribuente.iid desc");
				query.setString("codiceFiscale", comunicazione.getCodiceFiscale());
				Logger.log().info("query ", query);
				iter = (HibernateIterator)query.iterate();
				if (iter != null && iter.hasNext()) {
					tributiEntry = (MiVwContribuenti) iter.next();
				}
				if (tributiEntry != null) {
					if (toIntegrate(tributiEntry.getCognome(), comunicazione.getCognome())) {
						Logger.log().info(comunicazione.getClass().getName(), "Tributi I001");
						MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "TRIBUTI", "COGNOME", "I001", "cognome da " + comunicazione.getCognome() + " a " + tributiEntry.getCognome(), null, comunicazione.getMiDupFornitura());
						logs.add(log);
						comunicazione.setCognome(tributiEntry.getCognome());
					}
					if (toIntegrate(tributiEntry.getNome(), comunicazione.getNome())) {
						Logger.log().info(comunicazione.getClass().getName(), "Tributi I002");
						MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "TRIBUTI", "NOME", "I002", "nome da " + comunicazione.getNome() + " a " + tributiEntry.getNome(), null, comunicazione.getMiDupFornitura());
						logs.add(log);
						comunicazione.setNome(tributiEntry.getNome());
					}
					if (toIntegrate(tributiEntry.getDataNascita(), comunicazione.getDataNascita())) {
						Logger.log().info(comunicazione.getClass().getName(), "Tributi I003");
						MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "TRIBUTI", "DATA_NASCITA", "I003", "dataNascita da " + comunicazione.getDataNascita() + " a " + tributiEntry.getDataNascita(), null, comunicazione.getMiDupFornitura());
						logs.add(log);
						Date dataNascita = null;
						try {
							dataNascita = _dataNascitaParser.parse(tributiEntry.getDataNascita());
						} catch (ParseException e) {
							Logger.log().error(this.getClass().getName(), "unable to convert to date (dd/MM/yyyy) DATA_NASCITA: " + tributiEntry.getDataNascita());
						}
						comunicazione.setDataNascita(dataNascita);
					}
					if (toIntegrate(tributiEntry.getSesso(), ("1".equals(comunicazione.getSesso()) ? "M" : "F"))) {
						Logger.log().info(comunicazione.getClass().getName(), "Tributi I004");
						MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "TRIBUTI", "SESSO", "I004", "sesso da " + ("1".equals(comunicazione.getSesso()) ? "M" : "F") + " a " + tributiEntry.getSesso(), null, comunicazione.getMiDupFornitura());
						logs.add(log);
						comunicazione.setCognome(("M".equals(tributiEntry.getCognome()) ? "1" : "2"));
					}
					if (toIntegrate(tributiEntry.getIndirizzo(), comunicazione.getIndirizzo())) {
						Logger.log().info(comunicazione.getClass().getName(), "Tributi I005");
						MiConsIntegrationLog log = new MiConsIntegrationLog(comunicazione, "TRIBUTI", "INDIRIZZO", "I005", "indirizzo da " + comunicazione.getIndirizzo() + " a " + tributiEntry.getIndirizzo(), null, comunicazione.getMiDupFornitura());
						logs.add(log);
						comunicazione.setIndirizzo(tributiEntry.getIndirizzo());
					}
				}*/
			}
		} finally {
			if (iter!=null) {
				Hibernate.close(iter);
			}
		}
	}

	protected void integrateCatasto(List<MiConsIntegrationLog> logs, MiConsOggetto oggetto) {
		MiVwCatasto catastoEntry = null;
		HibernateIterator iter = null;
		// catastoEntry = (MiVwCatasto) HibernateUtil.currentSession().load(
		// MiVwCatasto.class, oggetto.getCodiceFiscale());
		// }
		try {
			Session session = HibernateUtil.currentSession();
	
			Query query = session.createQuery("select catasto from MiVwCatasto as catasto  where " + " catasto.iid.codNazionale = :codNazionale " + " AND catasto.iid.foglio = :foglio AND catasto.iid.particella = :particella AND catasto.iid.subalterno = :subalterno AND catasto.iid.dataInizio <= :dataValiditaAttoDate AND catasto.iid.dataFine > :dataValiditaAttoDate ");
			
	
			query.setDate("dataValiditaAttoDate", oggetto.getDecorrenza());
			query.setString("foglio", oggetto.getFoglio());
			query.setString("particella", oggetto.getParticella());
			query.setString("subalterno", oggetto.getSubalterno());
			query.setString("codNazionale", MuiApplication.belfiore);
			Logger.log().info("query ", query);
			Logger.log().info("PARAMETRI ", "FOGLIO="+oggetto.getFoglio() + " PARTICELLA="+ oggetto.getParticella() + " subalterno=" + oggetto.getSubalterno() + " dataValiditaAttoDate=" + oggetto.getDecorrenza());
			try {
				iter = (HibernateIterator) query.iterate();
				Logger.log().info(this.getClass().getName(),  "ESEGUITA QUERY");
			} catch (HibernateException e) {
				Logger.log().error(oggetto.getClass().getName(), "Erore nella ricerca a catasto ", e);
			} 
			if (iter != null && iter.hasNext()) {
				catastoEntry = (MiVwCatasto) iter.next();
			}
			if (catastoEntry == null) {
				Logger.log().info(oggetto.getClass().getName(), "CATASTO I101");
				MiConsIntegrationLog log = new MiConsIntegrationLog(oggetto.getMiConsComunicazione(), "CATASTO", "PARAMETRI CATASTALI", "I101", "immoobile (" + oggetto.getFoglio() + "," + oggetto.getParticella() + "," + oggetto.getSubalterno() + ") NON TROVATO A CATASTO", oggetto, oggetto.getMiDupFornitura());
				logs.add(log);
			}
		} finally {
				if (iter!=null)
					Hibernate.close(iter);
		}
	}

	private boolean toIntegrate(Object stored, Object readen) {
		if (stored == null) {
			return false;
		} else {
			try {
				return !stored.equals(readen);
			} catch (Throwable e) {
				return true;
			}
		}
	}

	public void integrateFornitura(String iidFornitura) {
		Logger.log().info(this.getClass().getName(), "Inizio integrateFornitura - iidFornitura=" + iidFornitura);
		getRunningIntegrators().put(Long.valueOf(iidFornitura), this);
		MuiApplication.getMuiApplication().getServletContext().setAttribute("integrators", getRunningIntegrators());
		Session session = HibernateUtil.currentSession();
		Transaction transaction = session.beginTransaction();
		ComunicazioneConverter conv = new ComunicazioneConverter();
		List<MiConsIntegrationLog> logs = new ArrayList<MiConsIntegrationLog>();
		HibernateIterator soggettiIterator = null;
		try {
			Query query = null;
			query = session.createQuery("select distinct sgt  from  MiDupFornitura as c,  MiDupNotaTras nota, MiDupTitolarita as tit,  MiDupSoggetti as sgt  where  c.iid = :iidFornitura   and   nota.miDupFornitura = c   and   tit.miDupNotaTras = nota  and   sgt = tit.miDupSoggetti order by  sgt.iid asc");
			query.setString("iidFornitura", iidFornitura);
			// query.setMaxResults(10);
			Logger.log().info(this.getClass().getName(), "Prima di eseguire la query: " + query.toString());
			soggettiIterator = (HibernateIterator)query.iterate();
			Logger.log().info(this.getClass().getName(), "Query eseguita. Prima di entrare nell'elaborazione dei soggetti");
			while (soggettiIterator.hasNext()) {
				_rowCount++;
				MiDupSoggetti soggetto = (MiDupSoggetti) soggettiIterator.next();
				MiConsComunicazione com = conv.evalComunicazione(soggetto);
				if (com != null) {
					// Commentato by MaX - 06/09/2007 - Solo per farlo funzionare in test, dato che non ho le tabelle per integrare
					// le anagrafiche
					Logger.log().info(this.getClass().getName(), "Prima di eseguire integrateAnagrafe - Id soggetto: " + soggetto.getIid());
					integrateAnagrafe(logs, com);
					session.saveOrUpdate(com);
					Iterator iter =  null;
					try {
						
						iter = com.getMiConsOggettos().iterator();
						while (iter != null && iter.hasNext()) {
							MiConsOggetto oggetto = (MiConsOggetto) iter.next();
							// Commentato by MaX - 06/09/2007 - Solo per farlo funzionare in test, dato che non ho le tabelle per integrare
							// il catasto
							Logger.log().info(this.getClass().getName(), "Prima di eseguire integrateCatasto - Id soggetto: " + oggetto.getIid());
							integrateCatasto(logs, oggetto);
							Logger.log().info(this.getClass().getName(), "IntegrateCatasto ESEGUITO  - Id soggetto: " + oggetto.getIid());
							session.saveOrUpdate(oggetto);
							Logger.log().info(this.getClass().getName(), "SaveOrUpdate effettuato  - Id soggetto: " + oggetto.getIid());
						}
					} finally {
						
					}
					
				}
			}
			Iterator<MiConsIntegrationLog> logsIterator = logs.iterator();
			while (logsIterator.hasNext()) {
				MiConsIntegrationLog log = logsIterator.next();
				session.saveOrUpdate(log);
			}
			if (soggettiIterator!=null)
				Hibernate.close(soggettiIterator);

			transaction.commit();
			Logger.log().info(this.getClass().getName(), "eseguito commit dell'integrazione della Fornitura " + iidFornitura);

		} catch (Throwable t) {
			Logger.log().error(this.getClass().getName(), "Si è verificato un errore durante l'integrazione della Fornitura " + iidFornitura, t);
			try {
				transaction.rollback();
				Logger.log().info(this.getClass().getName(), "eseguito rollback dell'integrazione della Fornitura " + iidFornitura);

			} catch (HibernateException e1) {
				// TODO Auto-generated catch block
				Logger.log().error(this.getClass().getName(), "Si e' verificato un errore durante il rollback dell'integrazione della Fornitura " + iidFornitura, e1);
			}
		} finally {
			try {
				session.flush();
			} catch (HibernateException e) {
				Logger.log().error(this.getClass().getName(), "errore durante la generazione della comunicazione per la fornitura=" + iidFornitura, e);
			}
			try {
				HibernateUtil.closeSession();
			} catch (HibernateException e) {
				Logger.log().error(this.getClass().getName(), "errore durante la generazione della comunicazione per la fornitura=" + iidFornitura, e);
			}
			getRunningIntegrators().remove(Long.valueOf(iidFornitura));
		}
	}

	public void integrateFornituraFabbr(String iidFornitura) {
		Logger.log().info(this.getClass().getName(), "Inizio integrateFornituraFabbricati - iidFornitura=" + iidFornitura);
		getRunningIntegratorsFabbr().put(Long.valueOf(iidFornitura), this);
		MuiApplication.getMuiApplication().getServletContext().setAttribute("integratorsFabbr", getRunningIntegratorsFabbr());
		Session session = HibernateUtil.currentSession();
		Transaction transaction = session.beginTransaction();
		try {

			MiDupFornitura fornituraCurr = null; 
			Query queryFornitura = session.createQuery("SELECT f FROM MiDupFornitura as f WHERE f.iid = :id_fornitura");
			queryFornitura.setString("id_fornitura", iidFornitura);
			Logger.log().info("Query  recupero fornitura: ", queryFornitura);
			Logger.log().info(this.getClass().getName(), "Param iidFornitura: " + iidFornitura);
			HibernateIterator iterForn = (HibernateIterator) queryFornitura.iterate();
			if (iterForn != null && iterForn.hasNext()) {
				fornituraCurr = (MiDupFornitura) iterForn.next();
			}
			String codNazionale = (fornituraCurr != null) ? fornituraCurr.getCodiceFornitura() : MuiApplication.belfiore;
			Logger.log().info(this.getClass().getName(), "Codice nazionale: " + codNazionale);

			Query queryForFabbricati = null;

			String sqlFabbricati = "SELECT DISTINCT dfinfo" + " FROM MiDupFornitura AS c, MiDupNotaTras nota, MiDupFabbricatiInfo AS dfinfo" + " WHERE c.iid = :iidFornitura" + " AND nota.miDupFornitura = c" + " AND dfinfo.miDupNotaTras = nota" + " ORDER BY dfinfo.iid ASC";

			queryForFabbricati = session.createQuery(sqlFabbricati);
			Logger.log().info(this.getClass().getName(), "Creata query per fabbricati");
			queryForFabbricati.setString("iidFornitura", iidFornitura);
			// query.setMaxResults(10);
			Logger.log().info(this.getClass().getName(), "Prima di eseguire la query: " + sqlFabbricati);
			Iterator fabbricatiIterator = queryForFabbricati.iterate();
			Logger.log().info(this.getClass().getName(), "Query eseguita. Prima di entrare nell'elaborazione dei fabbricati");
			int rowNr = 0;
			while (fabbricatiIterator.hasNext()) {
				_rowCount++;
				MiDupFabbricatiInfo finfoCurr = (MiDupFabbricatiInfo) fabbricatiIterator.next();
				rowNr++;
				System.out.println("[" + rowNr + "] " + finfoCurr.getIid() + " - " + finfoCurr.getIdNota() + " - " + finfoCurr.getIdImmobile() + " - " + finfoCurr.getRenditaEuro());
				String getRenditaDatiDaCatasto = loadResourceInString("/sql/getRenditaDatiDaCatasto.sql");

				Date dataAperturaPratica = finfoCurr.getMiDupNotaTras().getDataValiditaAttoDate();
				
				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rset = null;

				try {
					if (finfoCurr != null && finfoCurr.getMiDupFabbricatiIdentifica() != null && 
						 finfoCurr.getMiDupFabbricatiIdentifica().getFoglio() != null && finfoCurr.getMiDupFabbricatiIdentifica().getFoglio().trim().length() > 0 && 
						 finfoCurr.getMiDupFabbricatiIdentifica().getNumero() != null && finfoCurr.getMiDupFabbricatiIdentifica().getNumero().trim().length() > 0 && 
						 finfoCurr.getMiDupFabbricatiIdentifica().getSubalterno() != null && finfoCurr.getMiDupFabbricatiIdentifica().getSubalterno().trim().length() > 0) {
						conn = MuiApplication.getMuiApplication().getConnection();
						pstmt = conn.prepareStatement(getRenditaDatiDaCatasto);
						Logger.log().info(this.getClass().getName(), "Retrieve rendita from catasto query is \"" + getRenditaDatiDaCatasto.replace("\n", "") + "\"");
						int i = 0;
						pstmt.setString(++i, finfoCurr.getMiDupFabbricatiIdentifica().getFoglio());
						pstmt.setString(++i, finfoCurr.getMiDupFabbricatiIdentifica().getNumero());
						pstmt.setString(++i, finfoCurr.getMiDupFabbricatiIdentifica().getSubalterno());
						//Logger.log().info(this.getClass().getName(), pstmt.toString());
						rset = pstmt.executeQuery();
						String indirizzoCurr = "";
						String civico = "";
						String rendita = "";
						String codiceVia = "";
						String categoria = "";
						String classe = "";
						Date dataFineValOld = null;
						boolean breakNext = false;
						boolean checkSoloRendita = false;

							
							
						
						while (rset.next()) {
							Date dataInizioValCurr = rset.getDate("DATA_INIZIO_VAL");
							Date dataFineValCurr = rset.getDate("DATA_FINE_VAL");
							if (breakNext) {
								if (dataInizioValCurr.compareTo(dataFineValOld) == 0 && !checkSoloRendita) {
									// Se sono in situazione di next break vuol dire che ho già trovato il periodo che combacia
									// con la data apertura pratica, ma devo recuperare eventuali riaperture di pratiche che riconosco
									// perché hanno dataInizio eguale alla precedente dataFine. In questo caso risetto tutte le variabili,
									// compresa la dataFineValOld
									indirizzoCurr = rset.getString("NOME");
									Logger.log().info(this.getClass().getName(), "[" + rowNr + "] " + indirizzoCurr + " - " + rset.getString("CODICE_VIA") + " - " + rset.getString("CIVICO") + " - " + rset.getString("RENDITA") + " - " + rset.getString("CATEGORIA"));
									// In data 30/11/2007 modificato secondo richiesta di Paolo per far sì che prevalga, nel caso in cui sia presente, l'indirizzo 
									// specificato a catasto
									/*if (finfoCurr.getTIndirizzo() == null || finfoCurr.getTIndirizzo().trim().length() == 0) {
										if (finfoCurr.getCIndirizzo() == null || finfoCurr.getCIndirizzo().trim().length() == 0) {*/
									if (indirizzoCurr != null && indirizzoCurr.trim().length() > 0) {
											Logger.log().info(this.getClass().getName(), "Setto T_Indirizzo a: " + indirizzoCurr + " per il record: " + finfoCurr.getIid());
											finfoCurr.setTIndirizzo(indirizzoCurr);
									}

									String civicoCurr = rset.getString("CIVICO");
									String renditaCurr = rset.getString("RENDITA");
									String codiceViaCurr = rset.getString("CODICE_VIA");
									String categoriaCurr = rset.getString("CATEGORIA");
									String classeCurr = rset.getString("CLASSE");

									civico = (civicoCurr != null && civicoCurr.trim().length() > 0) ? civicoCurr : civico;
									if ("".equalsIgnoreCase(rendita)) {
										rendita = (renditaCurr != null && renditaCurr.trim().length() > 0) ? renditaCurr : rendita;
									} else {
										// MARCORIC:19/5/2009 :se la rendita è già valorizzata comunque le date devono essere coerenti, va bene prendere
										// i record successivi per gli altri dati, ma per la rendita va presa la rendita quella giusta (con le date giuste)
										// a meno che la rendita con le date giuste non ci sia, allora prendo la prima rendita disponibile
										if (
												(
												(dataInizioValCurr != null && dataInizioValCurr.compareTo(dataAperturaPratica) <= 0) && 
												 (dataFineValCurr != null && dataFineValCurr.compareTo(dataAperturaPratica) >= 0)
												 ) || 
												 (rendita == null || rendita.trim().length() == 0 || (new BigDecimal(rendita)).compareTo(new BigDecimal(5)) < 0)
											) 
										{
											rendita = (renditaCurr != null && renditaCurr.trim().length() > 0 && (new BigDecimal(renditaCurr)).compareTo(new BigDecimal(0)) != 0) ? renditaCurr : rendita;
										}
									}
									
									
									Logger.log().info(this.getClass().getName(), "[breakNext=TRUE] Rendita set to: " + rendita);
									codiceVia = (codiceViaCurr != null && codiceViaCurr.trim().length() > 0) ? codiceViaCurr : codiceVia;
									categoria = (categoriaCurr != null && categoriaCurr.trim().length() > 0) ? categoriaCurr : categoria;
									classe = (classeCurr != null && classeCurr.trim().length() > 0) ? classeCurr : classe;

									dataFineValOld = dataFineValCurr;									
								} else {
									//if ("0".equalsIgnoreCase(rendita) || "0.0".equalsIgnoreCase(rendita)) {
									// Entro qui se rendita è o eguale a NULL o minore di 5
									if (rendita == null || rendita.trim().length() == 0 || (new BigDecimal(rendita)).compareTo(new BigDecimal(5)) < 0) {
											
										// Se la rendita è zero non esco e vado avanti per vedere se trovo nei record successivi un record
										// con rendita diverso da zero. Da qui in poi cerco solo di rivalorizzare la rendita, lasciando invariati
										// gli altri campi
										String renditaCurr = rset.getString("RENDITA");
										if ("".equalsIgnoreCase(rendita)) {
											rendita = (renditaCurr != null && renditaCurr.trim().length() > 0) ? renditaCurr : rendita;
										} else {
											rendita = (renditaCurr != null && renditaCurr.trim().length() > 0 && (new BigDecimal(renditaCurr)).compareTo(new BigDecimal(0)) != 0) ? renditaCurr : rendita;
										}
										Logger.log().info(this.getClass().getName(), "[breakNext=TRUE(2)] Rendita set to: " + rendita);
										checkSoloRendita = true; // Da questo momento in poi continuo a ciclare, ma passo sempre solo di qui
									} else {
										break;										
									}
								}
							} else {
								indirizzoCurr = rset.getString("NOME");
								Logger.log().info(this.getClass().getName(), "[" + rowNr + "] " + indirizzoCurr + " - " + rset.getString("CODICE_VIA") + " - " + rset.getString("CIVICO") + " - " + rset.getString("RENDITA") + " - " + rset.getString("CATEGORIA"));
								/*if (finfoCurr.getTIndirizzo() == null || finfoCurr.getTIndirizzo().trim().length() == 0) {
									if (finfoCurr.getCIndirizzo() == null || finfoCurr.getCIndirizzo().trim().length() == 0) {*/
								if (indirizzoCurr != null && indirizzoCurr.trim().length() > 0) {
										Logger.log().info(this.getClass().getName(), "Setto T_Indirizzo a: " + indirizzoCurr + " per il record: " + finfoCurr.getIid());
										finfoCurr.setTIndirizzo(indirizzoCurr);
								}

								String civicoCurr = rset.getString("CIVICO");
								String renditaCurr = rset.getString("RENDITA");
								String codiceViaCurr = rset.getString("CODICE_VIA");
								String categoriaCurr = rset.getString("CATEGORIA");
								String classeCurr = rset.getString("CLASSE");

								civico = (civicoCurr != null && civicoCurr.trim().length() > 0) ? civicoCurr : civico;
								if ("".equalsIgnoreCase(rendita)) {
									rendita = (renditaCurr != null && renditaCurr.trim().length() > 0) ? renditaCurr : rendita;
								} else {
									rendita = (renditaCurr != null && renditaCurr.trim().length() > 0 && (new BigDecimal(renditaCurr)).compareTo(new BigDecimal(0)) != 0) ? renditaCurr : rendita;
								}
								Logger.log().info(this.getClass().getName(), "[breakNext=FALSE] Rendita set to: " + rendita);
								codiceVia = (codiceViaCurr != null && codiceViaCurr.trim().length() > 0) ? codiceViaCurr : codiceVia;
								categoria = (categoriaCurr != null && categoriaCurr.trim().length() > 0) ? categoriaCurr : categoria;
								classe = (classeCurr != null && classeCurr.trim().length() > 0) ? classeCurr : classe;
								
								if ((dataInizioValCurr != null && dataInizioValCurr.compareTo(dataAperturaPratica) <= 0) && 
										 (dataFineValCurr != null && dataFineValCurr.compareTo(dataAperturaPratica) >= 0)) {
										breakNext = true;
										dataFineValOld = dataFineValCurr;
								}
							}

						}
						finfoCurr.setTCivico1(civico);
						finfoCurr.setRenditaEuro(rendita);
						finfoCurr.setCodiceVia(codiceVia);
						finfoCurr.setCategoria(categoria);
						finfoCurr.setClasse(classe);

/**
 * INIZIO - Recupero indirizzo con un'altra query
 */
						Logger.log().info(this.getClass().getName(), "[" + rowNr + "] Recupero l'indirizzo con un'altra query ad hoc");
						String getIndirizzoDaCatasto = loadResourceInString("/sql/getIndirizzoDaCatasto.sql");
						PreparedStatement pstmtInd = null;
						ResultSet rsetInd = null;

						pstmtInd = conn.prepareStatement(getIndirizzoDaCatasto);
						Logger.log().info(this.getClass().getName(), "Retrieve Indirizzo from catasto query is \"" + getIndirizzoDaCatasto.replace("\n", "") + "\"");
						int j = 0;
						String foglioPadded = finfoCurr.getMiDupFabbricatiIdentifica().getFoglio();
						String numeroPadded = finfoCurr.getMiDupFabbricatiIdentifica().getNumero();
						String subalternoPadded = finfoCurr.getMiDupFabbricatiIdentifica().getSubalterno();
						if (foglioPadded != null) {
							while (foglioPadded.length() < 4) {
								foglioPadded = "0" + foglioPadded; 
							}
						}
						if (numeroPadded != null) {
							while (numeroPadded.length() < 5) {
								numeroPadded = "0" + numeroPadded; 
							}
						}
						if (subalternoPadded != null) {
							while (subalternoPadded.length() < 4) {
								subalternoPadded = "0" + subalternoPadded; 
							}
						}
						pstmtInd.setString(++j, codNazionale);
						pstmtInd.setString(++j, foglioPadded);
						pstmtInd.setString(++j, numeroPadded);
						pstmtInd.setString(++j, subalternoPadded);
						Logger.log().info(this.getClass().getName(), "Parametri in input: [" +
								codNazionale + " - " + 
								foglioPadded + " - " +
								numeroPadded + " - " +
								subalternoPadded + "]");
						rsetInd = pstmtInd.executeQuery();
						String indirizzoDaCatasto = "";
						while (rsetInd.next()) {
							String civico1 = rsetInd.getString("CIV1");
							while (civico1 != null && civico1.startsWith("0")) {
								civico1 = civico1.substring(1);
							}
							indirizzoDaCatasto = 
								rsetInd.getString("DESCR") + " " +
								rsetInd.getString("IND") + " " +
								civico1;
							break; // Prendo il primo record che trovo
						}
						if (indirizzoDaCatasto != null && indirizzoDaCatasto.trim().length() > 0) {
							finfoCurr.setTIndirizzo(indirizzoDaCatasto);							
						}
/**
 * FINE - Recupero indirizzo con un'altra query
 */
						
/**
* INIZIO - Valorizzazione FLAG_PERTINENZA con un'altra query
*/
						// todo_ manca sql , chiederlo a ciacca	
						Logger.log().info(this.getClass().getName(), "[" + rowNr + "] Valorizzo il FLAG_PERTINENZA con un'altra query ad hoc");
						String getFlagPertinenza = loadResourceInString("/sql/getFlagPertinenza.sql");
						PreparedStatement pstmtPer = null;
						ResultSet rsetPer = null;

						pstmtPer = conn.prepareStatement(getFlagPertinenza);
						Logger.log().info(this.getClass().getName(), "Retrieve FLAG_PERTINENZA from catasto query is \"" + getFlagPertinenza.replace("\n", "") + "\"");
						int k = 0;
						if (foglioPadded != null) {
							while (foglioPadded.length() < 4) {
								foglioPadded = "0" + foglioPadded; 
							}
						}
						if (numeroPadded != null) {
							while (numeroPadded.length() < 5) {
								numeroPadded = "0" + numeroPadded; 
							}
						}
						if (subalternoPadded != null) {
							while (subalternoPadded.length() < 4) {
								subalternoPadded = "0" + subalternoPadded; 
							}
						}
						pstmtPer.setString(++k, codNazionale);
						pstmtPer.setString(++k, foglioPadded);
						pstmtPer.setString(++k, numeroPadded);
						pstmtPer.setString(++k, subalternoPadded);
						Logger.log().info(this.getClass().getName(), "Parametri in input: [" +
								codNazionale + " - " + 
								foglioPadded + " - " +
								numeroPadded + " - " +
								subalternoPadded + "]");
						rsetPer = pstmtPer.executeQuery();
						if (rsetPer.next()) {
							finfoCurr.setFlagPertinenza("S");
						} else {
							finfoCurr.setFlagPertinenza("N");
						}
/**
* FINE - Valorizzazione FLAG_PERTINENZA con un'altra query
*/
						session.saveOrUpdate(finfoCurr);
						Logger.log().info(this.getClass().getName(), "Salvato MiDupFabbricatiInfo: " + finfoCurr.getIid());

					} else {
						Logger.log().debug(this.getClass().getName(), "Impossibile recuperare la rendita per il record MiDupFabbricatiInfo con IID=: " + finfoCurr.getIid() + ". Non tutti presenti i valori FOGLIO, PARTICELLA e SUBALTERNO");
					}
				} catch (SQLException e) {
					Logger.log().error(this.getClass().getName(), "Error nel recupero dei dati da catasto ", e);
				} finally {
					try {
						rset.close();
					} catch (Throwable e) {
					}
					try {
						pstmt.close();
					} catch (Throwable e) {
					}
					try {
						conn.close();
					} catch (Throwable e) {
					}
				}

			}
			transaction.commit();
			Logger.log().info(this.getClass().getName(), "Eseguito commit dell'integrazione fabbricati della Fornitura: " + iidFornitura);
			
		} catch (Throwable t) {
			Logger.log().error(this.getClass().getName(), "Si è verificato un errore durante l'integrazione della Fornitura " + iidFornitura, t);
			try {
				transaction.rollback();
				Logger.log().info(this.getClass().getName(), "eseguito rollback dell'integrazione della Fornitura " + iidFornitura);

			} catch (HibernateException e1) {
				// TODO Auto-generated catch block
				Logger.log().error(this.getClass().getName(), "Si e' verificato un errore durante il rollback dell'integrazione della Fornitura " + iidFornitura, e1);
			}
		} finally {
			try {
				session.flush();
			} catch (HibernateException e) {
				Logger.log().error(this.getClass().getName(), "errore durante la generazione della comunicazione per la fornitura=" + iidFornitura, e);
			}
			try {
				HibernateUtil.closeSession();
			} catch (HibernateException e) {
				Logger.log().error(this.getClass().getName(), "errore durante la generazione della comunicazione per la fornitura=" + iidFornitura, e);
			}
			getRunningIntegratorsFabbr().remove(Long.valueOf(iidFornitura));
		}
	}

}
