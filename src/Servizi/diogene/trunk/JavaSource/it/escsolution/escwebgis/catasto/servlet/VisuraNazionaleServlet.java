/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.servlet;

import it.escsolution.escwebgis.catasto.bean.VisuraNazionaleFinder;
import it.escsolution.escwebgis.catasto.logic.VisuraNazionaleLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class VisuraNazionaleServlet extends EscServlet implements EscService {
	
	PulsantiNavigazione nav = new PulsantiNavigazione();
	private VisuraNazionaleFinder finder = null;

	private String localDataSource = "jdbc/Diogene_DS";
	

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalit diversa
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
				mCaricareFormRicerca(request, response, st);
				break;
			case 2:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response,false,st);
				break;
			case 3:
				mCaricareFormRicerca(request, response, st);
				break;
			case 4:
				mCaricareDettaglio(request,response,false,st);
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

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response, int st)
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
		
		if(st==1){
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Codice Fiscale");
			elementoFiltro.setAttributeName("CODICE_FISCALE");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setObbligatorio(true);
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			listaElementiFiltro.add(elementoFiltro);
		}else{
			sessione.setAttribute("OGGETTO_SEL_CF", request.getParameter("OGGETTO_SEL_CF"));
		}
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Portale SISTER - Username");
		elementoFiltro.setAttributeName("USR_SISTER");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setObbligatorio(true);
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Portale SISTER - Password");
		elementoFiltro.setAttributeName("PWD_SISTER");
		elementoFiltro.setTipo("PWD");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setObbligatorio(true);
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "catasto/visuraNazFrame.jsp", nav);

	}
	
	/*private void mCaricareDettaglioByCf(HttpServletRequest request,HttpServletResponse response, boolean tipo, int st )throws Exception{
		
		HttpSession sessione = request.getSession();
		
		String cf = (String)request.getParameter("OGGETTO_SEL");
		
		if (sessione.getAttribute(VisuraNazionaleLogic.FINDER)!= null){
			finder = (VisuraNazionaleFinder)sessione.getAttribute(VisuraNazionaleLogic.FINDER);
		}
		else{
			finder = null;
		}
	
		finder = (VisuraNazionaleFinder)gestireMultiPagina(finder,request);
		
		VisuraNazionaleLogic logic = new VisuraNazionaleLogic(this.getEnvUtente(request));
		
		Hashtable ht = new Hashtable();
		if(cf!=null)
			ht = logic.getRisultatoRicerca(cf, finder);
	
		sessione.setAttribute(VisuraNazionaleLogic.VISURA_NAZIONALE, ht.get(VisuraNazionaleLogic.VISURA_NAZIONALE));
		sessione.setAttribute(VisuraNazionaleLogic.FINDER, ht.get(VisuraNazionaleLogic.FINDER));
		
		this.chiamaPagina(request, response, "catasto/visuraNazFrame.jsp", nav);
	}
		*/
	

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, boolean tipo, int st )throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();
		
		if (sessione.getAttribute(VisuraNazionaleLogic.FINDER)!= null){
			finder = (VisuraNazionaleFinder)sessione.getAttribute(VisuraNazionaleLogic.FINDER);
		}
		else{
			finder = null;
		}
	
		finder = (VisuraNazionaleFinder)gestireMultiPagina(finder,request);

		VisuraNazionaleLogic logic = new VisuraNazionaleLogic(this.getEnvUtente(request));
		
		String cf = null;
		String usister = null;
		String pwdsister = null;
		for(Object o : vettoreRicerca){
			EscElementoFiltro el = (EscElementoFiltro)o;
			if("CODICE_FISCALE".equals(el.getAttributeName()))
				cf = el.getValore();
			if("USR_SISTER".equals(el.getAttributeName()))
				usister = el.getValore();
			if("PWD_SISTER".equals(el.getAttributeName()))
				pwdsister = el.getValore();
		}
		
		if(st==4 && cf==null)
			cf = (String)request.getParameter("OGGETTO_SEL_CF");
		
		Hashtable ht = new Hashtable();
		if(cf!=null)
			ht = logic.getRisultatoRicerca(cf, usister, pwdsister, finder);
	
		sessione.setAttribute(VisuraNazionaleLogic.VISURA_NAZIONALE, ht.get(VisuraNazionaleLogic.VISURA_NAZIONALE));
		sessione.setAttribute(VisuraNazionaleLogic.FINDER, ht.get(VisuraNazionaleLogic.FINDER));
		
		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		Vector vettoreRicercaNew = new Vector();
		for(Object o : vettoreRicerca){
			EscElementoFiltro el = (EscElementoFiltro)o;
			if(!"PWD_SISTER".equals(el.getAttributeName()))
				vettoreRicercaNew.add(o);
		}
		vettoreRicerca = vettoreRicercaNew;
		
		
		this.chiamaPagina(request, response, "catasto/visuraNazFrame.jsp", nav);
	}
	
	/*public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (VisuraNazionaleFinder)finder2;
		VisuraNazionaleLogic logic = new VisuraNazionaleLogic(this.getEnvUtente(request));
		return logic.getVisuraNazionale(cf);
	}*/

	public EscFinder getNewFinder(){
		return new VisuraNazionaleFinder();
	}

	public String getTema() {
		return "Visura Nazionale";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_T_F24_VERSAMENTI";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}
	
	/*
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, boolean tipo, int st )throws Exception{
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

			String azione = "";
			HttpSession sessione = request.getSession();
			removeOggettiIndiceDaSessione(sessione);
			
			 			
			IntestatarioFFinder finder = null;
			
			if (st == 101){

				finder = (IntestatarioFFinder)gestireMultiPagina(finder,request);
				sessione.setAttribute(NOMEFINDER,finder);
			}

			
			if (sessione.getAttribute(NOMEFINDER) !=null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new IntestatarioFFinder().getClass()){
					finder = (IntestatarioFFinder)sessione.getAttribute(NOMEFINDER);
				}
				else{
					// il finder non corrisponde -->
					finder = null;
				}
			}
			
			
		if (request.getParameter("AZIONE")!= null)
				azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_INTESTATARIF",(Vector)sessione.getAttribute("LISTA_INTESTATARIF"),NOMEFINDER);
		if (azione.equals("")){
			oggettoSel="";recordScelto="";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");
			if (request.getParameter("OGGETTO_SEL")!= null){
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL")!= null){
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder!=null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		
		 * carica il dettaglio dell'intestatario
		 
		VisuraNazionaleLogic logic = new VisuraNazionaleLogic(this.getEnvUtente(request));
		EscElementoFiltro el = (EscElementoFiltro)vettoreRicerca.get(0);
		String cf = el.getValore();
		
		VisuraDTO visura = new VisuraDTO();
		if(cf!=null)
			visura = logic.getVisuraNazionale(cf);
		
	
		sessione.setAttribute(VisuraNazionaleLogic.VISURA_NAZIONALE, visura);
		
		
		// Recupero il parametro EXT per verificare se viene da un crossLink FA
		String ext = request.getParameter("EXT");
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"catasto/visuraNazFrame.jsp", nav);

	}

	*/
}
