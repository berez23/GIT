package it.escsolution.escwebgis.urbanistica.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.soggetto.bean.TipoSoggetto;
import it.escsolution.escwebgis.urbanistica.bean.Urbanistica;
import it.escsolution.escwebgis.urbanistica.bean.UrbanisticaFinder;
import it.escsolution.escwebgis.urbanistica.logic.UrbanisticaLogic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UrbanisticaServlet extends EscServlet implements EscService
{
	private static final long serialVersionUID = -1581956369240009446L;

	private String recordScelto;

	public static final String NOMEFINDER = "FINDER107";

	PulsantiNavigazione nav =  new PulsantiNavigazione();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*
		 * ad ogni uc corrisponde una funzionalita diversa
		 */
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request, response);
		try
		{
 
			switch (st){
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
				mCaricareDettaglio(request, response);
				//this.leggiCrossLink(request);
				break;		
			case 33:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request, response);
				//this.leggiCrossLink(request);
				break;	
			/*
			 * 
			 * case 4: // ho cliccato su un elemento --> visualizzo il dettaglio
			 * mCaricareDettaglio(request,response);
			 * this.leggiCrossLink(request); break; case 5: // ho cliccato su un
			 * elemento --> visualizzo il dettaglio
			 * mCaricareLista(request,response,true); break;
			 */
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
	}//-------------------------------------------------------------------------
	
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception{

		// caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();
		
		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));
		
		Vector operatoriLike = new Vector();
		operatoriLike.add(new EscOperatoreFiltro("like","contiene"));
	

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<")); 
		
		Vector operatoriNumericiSoloMaggiorneUguale = new Vector();
		operatoriNumericiSoloMaggiorneUguale.add(new EscOperatoreFiltro(">=",">="));
 
		Vector operatoriNumericiSoloMinoreUguale = new Vector();
		operatoriNumericiSoloMinoreUguale.add(new EscOperatoreFiltro("<=","<="));
		
		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));
		
		// costruisce il vettore di elementi per la pagina di ricerca
	
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();				
		
		elementoFiltro.setLabel("DATI GENERALI");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		Vector vctTabelle = new Vector();
		vctTabelle.add(new TipoSoggetto("", "Tutte"));
		vctTabelle.add(new TipoSoggetto("AGIBILITA", "AGIBILITA"));
		vctTabelle.add(new TipoSoggetto("BUCALOSSI", "BUCALOSSI"));
		vctTabelle.add(new TipoSoggetto("COMMISSIONI_EDILIZIE", "COMMISSIONI EDILIZIE"));
		vctTabelle.add(new TipoSoggetto("CONCESSIONI_STORICO", "CONCESSIONI STORICO"));
		vctTabelle.add(new TipoSoggetto("INIZIO_LAVORI", "INIZIO LAVORI"));
		vctTabelle.add(new TipoSoggetto("POSIZIONI_EDILIZIE", "POSIZIONI EDILIZIE"));
		vctTabelle.add(new TipoSoggetto("POSIZIONI_EDILIZIE2", "POSIZIONI EDILIZIE 2"));
		vctTabelle.add(new TipoSoggetto("PROTOCOLLO", "PROTOCOLLI"));
		vctTabelle.add(new TipoSoggetto("VIGILANZA_EDILIZIA", "VIGILANZA EDILIZIA"));

		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Fonte");
		elementoFiltro.setAttributeName("FONTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setListaValori(vctTabelle);
		elementoFiltro.setCampoFiltrato("FONTE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Nominativo");
		elementoFiltro.setAttributeName("NOMINATIVO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("NOMINATIVO");
		listaElementiFiltro.add(elementoFiltro);
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		//chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "urbanistica/urbanisticaFrame.jsp", nav);
	}//-------------------------------------------------------------------------

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();
		
		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new UrbanisticaFinder().getClass())
			{
				finder = (UrbanisticaFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (UrbanisticaFinder) gestireMultiPagina(finder, request);

		UrbanisticaLogic logic = new UrbanisticaLogic(this.getEnvUtente(request));

		Hashtable ht = logic.mCaricareListaUrbanistica(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_urba = (Vector) ht.get(UrbanisticaLogic.LISTA_URBANISTICA);
		finder = (UrbanisticaFinder) ht.get("FINDER");
		sessione.setAttribute(UrbanisticaLogic.LISTA_URBANISTICA, vct_lista_urba);
		sessione.setAttribute(NOMEFINDER, finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{

			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();
		this.chiamaPagina(request, response, "urbanistica/urbanisticaFrame.jsp", nav);
	}//-------------------------------------------------------------------------

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request il parametrio identificativo dell'oggetto
		// cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		String nomeTabella = "Tutte";
		HttpSession sessione = request.getSession();
		UrbanisticaFinder finder = null;
		// boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) != null){
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new UrbanisticaFinder().getClass()){
				finder = (UrbanisticaFinder) sessione.getAttribute(NOMEFINDER);
			}
		}
		
		if (sessione.getAttribute("NOME_TABELLA") != null)
			nomeTabella = (String)sessione.getAttribute("NOME_TABELLA");

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		
		String[] aryChkSel = new String[10];
		gestioneMultiRecord(request, response, azione, finder, sessione, UrbanisticaLogic.LISTA_URBANISTICA, (Vector) sessione.getAttribute(UrbanisticaLogic.LISTA_URBANISTICA), NOMEFINDER);
		if (azione.equals(""))
		{
			
			oggettoSel = "";
			recordScelto = "";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");
			
			Map map = request.getParameterMap();
			aryChkSel = (String[])map.get("chkSel");
			if (aryChkSel != null && aryChkSel.length>0){
				if (request.getParameter("RECORD_SEL") != null)
					recordScelto = request.getParameter("RECORD_SEL"); //Metto un numero a metà pagina
				if (request.getParameter("OGGETTO_SEL") != null)
					oggettoSel = request.getParameter("OGGETTO_SEL");	//Metto il primo della selezione
			}else{
				if (request.getParameter("OGGETTO_SEL") != null)
				{
					oggettoSel = request.getParameter("OGGETTO_SEL");
					aryChkSel = new String[1];
					aryChkSel[0] = oggettoSel;
				}
				if (request.getParameter("RECORD_SEL") != null)
				{
					recordScelto = request.getParameter("RECORD_SEL");
//					if (finder != null)
//						finder.setRecordAttuale(new Long(recordScelto).longValue());
				}				
			}
			if (finder != null)
				finder.setRecordAttuale(new Long(recordScelto).longValue());
		}else if (azione.equals("1") ){
			//avanti di un record dal dettaglio
			aryChkSel = new String[1];
			aryChkSel[0] = oggettoSel;
		}else if (azione.equals("l") ){
			//avanti fino all'ultimo
			aryChkSel = new String[1];
			aryChkSel[0] = oggettoSel;
		}else if (azione.equals("-1") ){
			//indietro di un record dal dettaglio
			aryChkSel = new String[1];
			aryChkSel[0] = oggettoSel;
		}else if (azione.equals("f") ){
			//indietro fino all'ultimo record dal dettaglio
			aryChkSel = new String[1];
			aryChkSel[0] = oggettoSel;
		}

		/*
		 * carica il dettaglio
		 */
		UrbanisticaLogic logic = new UrbanisticaLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricaDettaglioUrbanistica(aryChkSel, nomeTabella);
		
		sessione.setAttribute(UrbanisticaLogic.DETTAGLIO_URBANISTICA, ht);
		sessione.setAttribute(NOMEFINDER, finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

			this.chiamaPagina(request, response, "urbanistica/urbanisticaFrame.jsp", nav);
	}//-------------------------------------------------------------------------

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
	{
		finder = (UrbanisticaFinder) finder2;
		UrbanisticaLogic logic = new UrbanisticaLogic(this.getEnvUtente(request));
		return logic.mCaricareListaUrbanistica(this.vettoreRicerca, finder);
	}//-------------------------------------------------------------------------

	public EscFinder getNewFinder()
	{
		return new UrbanisticaFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((Urbanistica) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Urbanistica";
	}



}
