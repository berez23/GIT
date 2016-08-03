package it.webred.mui.consolidation;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.model.MiConsComunicazione;
import it.webred.mui.model.MiConsIntegrationLog;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiVwAnagrafe;
import it.webred.mui.model.MiVwCatasto;
import it.webred.mui.model.MiVwContribuenti;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DwhManager {
	// List<MiConsIntegrationLog> logs = new ArrayList<MiConsIntegrationLog>();
	private static Map<Long, DwhManager> _integrators = new HashMap<Long, DwhManager>();

	private static SimpleDateFormat _dataNascitaParser = new SimpleDateFormat(
			"dd/MM/yyyy");

	public static Map<Long, DwhManager> getRunningIntegrators() {
		return _integrators;
	}

	private int _rowCount = 0;

	public int getRowCount() {
		return _rowCount;
	}

	public void integrateAnagrafe(List<MiConsIntegrationLog> logs,
			MiConsComunicazione comunicazione) {
		MiVwAnagrafe anagrafeEntry = null;
		// anagrafeEntry = (MiVwAnagrafe) HibernateUtil.currentSession().load(
		// MiVwAnagrafe.class, comunicazione.getCodiceFiscale());
		Session session = HibernateUtil.currentSession();

		Query query = session
				.createQuery("select distinct anagrafe from MiVwAnagrafe as anagrafe  where anagrafe.codiceFiscale = :codiceFiscale ");
		query.setString("codiceFiscale", comunicazione.getCodiceFiscale());
		Logger.log().info("query ", query);
		Iterator iter = query.iterate();
		if (iter != null && iter.hasNext()) {
			anagrafeEntry = (MiVwAnagrafe) iter.next();
		}
		if (anagrafeEntry != null) {
			if (toIntegrate(anagrafeEntry.getCognome(), comunicazione
					.getCognome())) {
				Logger.log().info(comunicazione.getClass().getName(), "I001");
				MiConsIntegrationLog log = new MiConsIntegrationLog(
						comunicazione, "ANAGRAFE", "COGNOME", "I001",
						"cognome da " + comunicazione.getCognome() + " a "
								+ anagrafeEntry.getCognome(), null,
						comunicazione.getMiDupFornitura());
				logs.add(log);
				comunicazione.setCognome(anagrafeEntry.getCognome());
			}
			if (toIntegrate(anagrafeEntry.getNome(), comunicazione.getNome())) {
				Logger.log().info(comunicazione.getClass().getName(), "I002");
				MiConsIntegrationLog log = new MiConsIntegrationLog(
						comunicazione, "ANAGRAFE", "NOME", "I002", "nome da "
								+ comunicazione.getNome() + " a "
								+ anagrafeEntry.getNome(), null, comunicazione
								.getMiDupFornitura());
				logs.add(log);
				comunicazione.setNome(anagrafeEntry.getNome());
			}
			if (toIntegrate(anagrafeEntry.getDataNascita(), comunicazione
					.getDataNascita())) {
				Logger.log().info(comunicazione.getClass().getName(), "I003");
				MiConsIntegrationLog log = new MiConsIntegrationLog(
						comunicazione, "ANAGRAFE", "DATA_NASCITA", "I003",
						"dataNascita da " + comunicazione.getDataNascita()
								+ " a " + anagrafeEntry.getDataNascita(), null,
						comunicazione.getMiDupFornitura());
				logs.add(log);
				comunicazione.setDataNascita(anagrafeEntry.getDataNascita());
			}
			if (toIntegrate(anagrafeEntry.getSesso(), ("1".equals(comunicazione
					.getSesso()) ? "M" : "F"))) {
				Logger.log().info(comunicazione.getClass().getName(), "I004");
				MiConsIntegrationLog log = new MiConsIntegrationLog(
						comunicazione, "ANAGRAFE", "SESSO", "I004", "sesso da "
								+ ("1".equals(comunicazione.getSesso()) ? "M"
										: "F") + " a "
								+ anagrafeEntry.getSesso(), null, comunicazione
								.getMiDupFornitura());
				logs.add(log);
				comunicazione.setCognome(("M"
						.equals(anagrafeEntry.getCognome()) ? "1" : "2"));
			}
			if (toIntegrate(anagrafeEntry.getIndirizzo(), comunicazione
					.getIndirizzo())) {
				Logger.log().info(comunicazione.getClass().getName(), "I005");
				MiConsIntegrationLog log = new MiConsIntegrationLog(
						comunicazione, "ANAGRAFE", "INDIRIZZO", "I005",
						"indirizzo da " + comunicazione.getIndirizzo() + " a "
								+ anagrafeEntry.getIndirizzo(), null,
						comunicazione.getMiDupFornitura());
				logs.add(log);
				comunicazione.setIndirizzo(anagrafeEntry.getIndirizzo());
			}
		} else {
			/*MiVwContribuenti tributiEntry = null;
			query = session
					.createQuery("select distinct contribuente from MiVwContribuenti as contribuente  where contribuente.codiceFiscale = :codiceFiscale order by contribuente.iid desc");
			query.setString("codiceFiscale", comunicazione.getCodiceFiscale());
			Logger.log().info("query ", query);
			iter = query.iterate();
			if (iter != null && iter.hasNext()) {
				tributiEntry = (MiVwContribuenti) iter.next();
			}
			if (tributiEntry != null) {
				if (toIntegrate(tributiEntry.getCognome(), comunicazione
						.getCognome())) {
					Logger.log().info(comunicazione.getClass().getName(),
							"Tributi I001");
					MiConsIntegrationLog log = new MiConsIntegrationLog(
							comunicazione, "TRIBUTI", "COGNOME", "I001",
							"cognome da " + comunicazione.getCognome() + " a "
									+ tributiEntry.getCognome(), null,
							comunicazione.getMiDupFornitura());
					logs.add(log);
					comunicazione.setCognome(tributiEntry.getCognome());
				}
				if (toIntegrate(tributiEntry.getNome(), comunicazione.getNome())) {
					Logger.log().info(comunicazione.getClass().getName(),
							"Tributi I002");
					MiConsIntegrationLog log = new MiConsIntegrationLog(
							comunicazione, "TRIBUTI", "NOME", "I002",
							"nome da " + comunicazione.getNome() + " a "
									+ tributiEntry.getNome(), null,
							comunicazione.getMiDupFornitura());
					logs.add(log);
					comunicazione.setNome(tributiEntry.getNome());
				}
				if (toIntegrate(tributiEntry.getDataNascita(), comunicazione
						.getDataNascita())) {
					Logger.log().info(comunicazione.getClass().getName(),
							"Tributi I003");
					MiConsIntegrationLog log = new MiConsIntegrationLog(
							comunicazione, "TRIBUTI", "DATA_NASCITA", "I003",
							"dataNascita da " + comunicazione.getDataNascita()
									+ " a " + tributiEntry.getDataNascita(),
							null, comunicazione.getMiDupFornitura());
					logs.add(log);
					Date dataNascita = null;
					try {
						dataNascita = _dataNascitaParser.parse(tributiEntry
								.getDataNascita());
					} catch (ParseException e) {
						Logger.log().error(
								this.getClass().getName(),
						"unable to convert to date (dd/MM/yyyy) DATA_NASCITA: "+tributiEntry.getDataNascita());
					}
					comunicazione.setDataNascita(dataNascita);
				}
				if (toIntegrate(tributiEntry.getSesso(), ("1"
						.equals(comunicazione.getSesso()) ? "M" : "F"))) {
					Logger.log().info(comunicazione.getClass().getName(),
							"Tributi I004");
					MiConsIntegrationLog log = new MiConsIntegrationLog(
							comunicazione,
							"TRIBUTI",
							"SESSO",
							"I004",
							"sesso da "
									+ ("1".equals(comunicazione.getSesso()) ? "M"
											: "F") + " a "
									+ tributiEntry.getSesso(), null,
							comunicazione.getMiDupFornitura());
					logs.add(log);
					comunicazione.setCognome(("M".equals(tributiEntry
							.getCognome()) ? "1" : "2"));
				}
				if (toIntegrate(tributiEntry.getIndirizzo(), comunicazione
						.getIndirizzo())) {
					Logger.log().info(comunicazione.getClass().getName(),
							"Tributi I005");
					MiConsIntegrationLog log = new MiConsIntegrationLog(
							comunicazione, "TRIBUTI", "INDIRIZZO", "I005",
							"indirizzo da " + comunicazione.getIndirizzo()
									+ " a " + tributiEntry.getIndirizzo(),
							null, comunicazione.getMiDupFornitura());
					logs.add(log);
					comunicazione.setIndirizzo(tributiEntry.getIndirizzo());
				}
			}*/
		}
	}

	protected void integrateCatasto(List<MiConsIntegrationLog> logs,
			MiConsOggetto oggetto) {
		MiVwCatasto catastoEntry = null;
		// catastoEntry = (MiVwCatasto) HibernateUtil.currentSession().load(
		// MiVwCatasto.class, oggetto.getCodiceFiscale());
		// }
		Session session = HibernateUtil.currentSession();

		Query query = session
				.createQuery("select catasto from MiVwCatasto as catasto  where "
						+ " catasto.dataInizio <= :dataValiditaAttoDate AND catasto.dataFine > :dataValiditaAttoDate "
						+ " AND catasto.foglio = :foglio AND catasto.particella = :particella AND catasto.subalterno = :subalterno");
		query.setDate("dataValiditaAttoDate", oggetto.getDecorrenza());
		query.setString("foglio", oggetto.getFoglio());
		
		String particella = oggetto.getParticella();
		// PARTICELLA
		String valS = particella;
		while (valS != null && valS.length() < 5) {
			valS = "0" + valS;
		}
		query.setString("particella", valS);
		query.setString("subalterno", oggetto.getSubalterno());
		Logger.log().info("query ", query);
		Iterator iter = null;
		try {
			iter = query.iterate();
		} catch (HibernateException e) {
			Logger.log().error(oggetto.getClass().getName(),
					"Erore nella ricerca a catasto ", e);
		}
		if (iter != null && iter.hasNext()) {
			catastoEntry = (MiVwCatasto) iter.next();
		}
		if (catastoEntry == null) {
			Logger.log().info(oggetto.getClass().getName(), "CATASTO I101");
			MiConsIntegrationLog log = new MiConsIntegrationLog(oggetto
					.getMiConsComunicazione(), "CATASTO",
					"PARAMETRI CATASTALI", "I101", "immobile ("
							+ oggetto.getFoglio() + ","
							+ oggetto.getParticella() + ","
							+ oggetto.getSubalterno()
							+ ") NON TROVATO A CATASTO", oggetto, oggetto
							.getMiDupFornitura());
			logs.add(log);
		}

	}

	private boolean toIntegrate(Object stored, Object readen) {
		if (stored == null) {
			return false;
		} else {
			try{
				return !stored.equals(readen);
			}
			catch(Throwable e){
				return true;
			}
		}
	}

	public void integrateFornitura(String iidFornitura) {
		getRunningIntegrators().put(Long.valueOf(iidFornitura), this);
		MuiApplication.getMuiApplication().getServletContext().setAttribute(
				"integrators", getRunningIntegrators());
		Session session = HibernateUtil.currentSession();
		Transaction transaction = session.beginTransaction();
		ComunicazioneConverter conv = new ComunicazioneConverter();
		List<MiConsIntegrationLog> logs = new ArrayList<MiConsIntegrationLog>();
		try {
			Query query = null;
			query = session
					.createQuery("select distinct sgt  from  MiDupFornitura as c,  MiDupNotaTras nota, MiDupTitolarita as tit,  MiDupSoggetti as sgt  where  c.iid = :iidFornitura   and   nota.miDupFornitura = c   and   tit.miDupNotaTras = nota  and   sgt = tit.miDupSoggetti order by  sgt.iid asc");
			query.setString("iidFornitura", iidFornitura);
			// query.setMaxResults(10);
			Iterator soggettiIterator = query.iterate();
			while (soggettiIterator.hasNext()) {
				_rowCount++;
				MiDupSoggetti soggetto = (MiDupSoggetti) soggettiIterator
						.next();
				MiConsComunicazione com = conv.evalComunicazione(soggetto);
				if (com != null) {
					integrateAnagrafe(logs, com);
					session.saveOrUpdate(com);
					Iterator iter = com.getMiConsOggettos().iterator();
					while (iter != null && iter.hasNext()) {
						MiConsOggetto oggetto = (MiConsOggetto) iter.next();
						integrateCatasto(logs, oggetto);
						session.saveOrUpdate(oggetto);
					}
				}
			}
			Iterator<MiConsIntegrationLog> logsIterator = logs.iterator();
			while (logsIterator.hasNext()) {
				MiConsIntegrationLog log = logsIterator.next();
				session.saveOrUpdate(log);
			}
			transaction.commit();
			Logger.log().info(
					this.getClass().getName(),
					"eseguito commit dell'integrazione della Fornitura "
							+ iidFornitura);

		} catch (Throwable t) {
			Logger.log().error(
					this.getClass().getName(),
					"Si è verificato un errore durante l'integrazione della Fornitura "
							+ iidFornitura, t);
			try {
				transaction.rollback();
				Logger.log().info(
						this.getClass().getName(),
						"eseguito rollback dell'integrazione della Fornitura "
								+ iidFornitura);

			} catch (HibernateException e1) {
				// TODO Auto-generated catch block
				Logger
						.log()
						.error(
								this.getClass().getName(),
								"Si è verificato un errore durante il rollback dell'integrazione della Fornitura "
										+ iidFornitura, e1);
			}
		} finally {
			try {
				session.flush();
			} catch (HibernateException e) {
				Logger.log().error(
						this.getClass().getName(),
						"errore durante la generazione della comunicazione per la fornitura="
								+ iidFornitura, e);
			}
			try {
				HibernateUtil.closeSession();
			} catch (HibernateException e) {
				Logger.log().error(
						this.getClass().getName(),
						"errore durante la generazione della comunicazione per la fornitura="
								+ iidFornitura, e);
			}
			getRunningIntegrators().remove(Long.valueOf(iidFornitura));
		}
	}
}
