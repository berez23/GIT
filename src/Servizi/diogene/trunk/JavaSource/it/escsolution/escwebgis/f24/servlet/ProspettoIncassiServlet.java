/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.f24.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.f24.bean.F24Finder;
import it.escsolution.escwebgis.f24.logic.ProspettoIncassiLogic;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ProspettoIncassiServlet extends EscServlet implements EscService {
	
	PulsantiNavigazione nav = new PulsantiNavigazione();
	private F24Finder finder = null;

	private String localDataSource = "jdbc/Diogene_DS";
	

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 *
		 */
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request, response);
		try
		{
			switch (st)
			{
			case 1:
				// carico la form di ricerca
				pulireSessione(request);
				mCaricareFormRicerca(request, response);
				break;
			case 2:
				// vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
				// pulireSessione(request);
					mCaricareLista(request, response, false);
				break;
			}
		}
		catch (it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(),ex);
		}

	}

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));
		
		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		
		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno Versamento");
		elementoFiltro.setAttributeName("ANNO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("TO_NUMBER(TO_CHAR(dt_riscossione,'yyyy'))");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "f24/ProspettoIncassiFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response, boolean notListaPrincipale)
			throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(ProspettoIncassiLogic.FINDER) != null)
		{
			if (((Object) sessione.getAttribute(ProspettoIncassiLogic.FINDER)).getClass() == new F24Finder().getClass())
			{
				finder = (F24Finder) sessione.getAttribute(ProspettoIncassiLogic.FINDER);
			}
			else
				finder = null;
		}

		finder = (F24Finder) gestireMultiPagina(finder, request);

		ProspettoIncassiLogic logic = new ProspettoIncassiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);
		
		String condizione = this.getDescCondizione(vettoreRicerca);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		finder = (F24Finder) ht.get(ProspettoIncassiLogic.FINDER);
		
		sessione.setAttribute(ProspettoIncassiLogic.LISTA_INCASSI, (Vector)ht.get(ProspettoIncassiLogic.LISTA_INCASSI));
		sessione.setAttribute(ProspettoIncassiLogic.FINDER, finder);
		sessione.setAttribute(ProspettoIncassiLogic.TOT_INCASSI, ht.get(ProspettoIncassiLogic.TOT_INCASSI));
		sessione.setAttribute("CONDIZIONE", condizione);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request, response, "f24/ProspettoIncassiFrame.jsp", nav);
	}
	
	protected String getDescCondizione(Vector listaPar) throws NumberFormatException, Exception
	{
		String cond = "";
		if (listaPar == null)
			return cond="(tutti gli anni di versamento disponibili)";
		Enumeration en = listaPar.elements();
		if (en.hasMoreElements())
		{
			EscElementoFiltro filtro = (EscElementoFiltro) en.nextElement();
			if (filtro.getValore()!=null && !filtro.getValore().trim().equals(""))
			{	
				String valore = filtro.getValore();
				String tipo = filtro.getOperatore();
				
				if(filtro.getOperatore().equals("<>"))
					tipo = " diverso da ";
				if(filtro.getOperatore().equals("<"))
					tipo = " minore di ";
				if(filtro.getOperatore().equals(">"))
					tipo = " maggiore di ";
				if(filtro.getOperatore().equals("="))
				   tipo = " uguale a ";
				
				cond = "(anno versamento " +tipo+valore+")";
			
			}
		}
		return cond;
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (F24Finder)finder2;
		ProspettoIncassiLogic logic = new ProspettoIncassiLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new F24Finder();
	}

	public String getTema() {
		return "Versamenti";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_T_F24_VERSAMENTI";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}
}
