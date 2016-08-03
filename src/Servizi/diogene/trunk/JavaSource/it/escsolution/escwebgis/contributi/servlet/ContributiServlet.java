package it.escsolution.escwebgis.contributi.servlet;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.contributi.bean.Contributi;
import it.escsolution.escwebgis.contributi.bean.ContributiFinder;
import it.escsolution.escwebgis.contributi.bean.ContributiSib;
import it.escsolution.escwebgis.contributi.logic.ContributiLogic;

public class ContributiServlet extends EscServlet implements EscService {

	private static final long serialVersionUID = 1L;

	public static final String NOMEFINDER = "FINDER134";

	private ContributiFinder finder = null;
	PulsantiNavigazione nav;

	private String localDataSource = "jdbc/Diogene_DS";

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessione = request.getSession();
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));

		super.EseguiServizio(request, response);

		String ext = request.getParameter("IND_EXT");

		if (ext != null && ext.equals("1"))
			_EseguiServizioExt(request, response);
		else
			_EseguiServizio(request, response);
	}

	public void _EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.EseguiServizio(request, response);
		try {
			switch (st) {
			case 1:
				// carico la form di ricerca
				pulireSessione(request);
				mCaricareFormRicerca(request, response);
				break;
			case 2:
				// vengo dalla pagina di ricerca --> predispongo la lista e la
				// passo alla pagina di lista
				// pulireSessione(request);
				mCaricareLista(request, response);
				break;
			case 3:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request, response, 3);
				// this.leggiCrossLink(request);
				break;
			case 33:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request, response, 33);
				// this.leggiCrossLink(request);
				break;
			// TODO verificare se esistono ulteriori altri casi
			}
		} catch (it.escsolution.escwebgis.common.DiogeneException de) {
			throw de;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	public void _EseguiServizioExt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.EseguiServizio(request, response);
		try {
			switch (st) {
			case 101:
				mCaricareDettaglio(request, response, 101);
				break;
			case 102:
				mCaricareDettaglio(request, response, 102);
				break;
			case 103:
				mCaricareDettaglio(request, response, 103);
				break;
			// TODO verificare se esistono ulteriori altri casi
			}
		} catch (it.escsolution.escwebgis.common.DiogeneException de) {
			throw de;
		} catch (Exception ex) {
			throw new it.escsolution.escwebgis.common.DiogeneException(ex);
		}
	}

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();

		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		// TODO

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));

		// chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "contributi/contributiFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO
		this.chiamaPagina(request, response, "contributi/contributiFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo) throws Exception {
		String azione = "";
		HttpSession sessione = request.getSession();

		removeOggettiIndiceDaSessione(sessione);

		ContributiFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ContributiFinder().getClass()) {
				finder = (ContributiFinder) sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, ContributiLogic.LISTA, (Vector) sessione.getAttribute(ContributiLogic.LISTA), NOMEFINDER);

		if (azione.equals("")) {
			oggettoSel = "";
			recordScelto = "";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL") != null) {
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL") != null) {
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder != null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		// carica il dettaglio
		ContributiLogic logic = new ContributiLogic(this.getEnvUtente(request));

		Hashtable ht = null;

		ht = logic.mCaricareDettaglio(oggettoSel, request);
		Contributi contr = (Contributi) ht.get(ContributiLogic.DETTAGLIO);
		sessione.setAttribute(ContributiLogic.DETTAGLIO, contr);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		//TODO
		this.chiamaPagina(request, response, "contributi/contributiDetail.jsp", nav);
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception {
		finder = (ContributiFinder) finder2;
		ContributiLogic logic = new ContributiLogic(this.getEnvUtente(request));
		// return logic.mCaricareLista(this.vettoreRicerca, finder); TODO
		return null;
	}

	public EscFinder getNewFinder() {
		return new ContributiFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo) {
		ContributiSib contr = (ContributiSib)listaOggetti.get(recordSuccessivo);
		String key = contr.getChiave();
		return key;
	}

	public String getTema() {
		return "Contributi";
	}

	public String getTabellaPerCrossLink() {
		return "CONTRIBUTI_SIB";
	}

	public String getLocalDataSource() {
		return localDataSource;
	}

}
