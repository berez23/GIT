package it.escsolution.escwebgis.versamenti.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.f24.logic.F24AnnullamentoLogic;
import it.escsolution.escwebgis.f24.logic.F24VersamentiLogic;
import it.escsolution.escwebgis.ruolo.logic.RuoloTarsuLogic;
import it.escsolution.escwebgis.tributiNew.bean.VersamentiNew;
import it.escsolution.escwebgis.tributiNew.logic.VersamentiNewLogic;
import it.escsolution.escwebgis.versamenti.bean.VersFinder;
import it.escsolution.escwebgis.versamenti.iciDM.logic.VersIciDmLogic;
import it.escsolution.escwebgis.versamenti.logic.VersamentiAllLogic;
import it.webred.ct.data.access.basic.common.dto.KeyValueDTO;
import it.webred.ct.data.access.basic.f24.dto.F24AnnullamentoDTO;
import it.webred.ct.data.access.basic.f24.dto.F24VersamentoDTO;
import it.webred.ct.data.access.basic.ruolo.tarsu.dto.RuoloTarsuDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.IciDmDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.VersamentoIciDmDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.ViolazioneIciDmDTO;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class VersamentiAllServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private VersFinder finder = null;
	private String FRAME = "versamenti/allVersamentiFrame.jsp";
	
	
	private String localDataSource = "jdbc/Diogene_DS";
	
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
	
	public void EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 *
		 */
		super.EseguiServizio(request,response);
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}
    

	public void _EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {

		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		try {
			switch (st){
				case 1:
					// carico la form di ricerca
					pulireSessione(request);
					mCaricareFormRicerca(request, response);
					break;
				case 2:
					// vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
					mCaricareLista(request, response);
					break;
				case 3:
					// ho cliccato su un elemento --> visualizzo il dettaglio
					mCaricareDettaglio(request, response,st);
				//	this.leggiCrossLink(request);
					break;
				case 00:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response,st);
				//	this.leggiCrossLink(request);
					break;
			}
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception ex){
			log.error(ex.getMessage(),ex);
		}
	}
	
	public void EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		
		try
		{
			switch (st){
			case 101:
				pulireSessione(request);
				mCaricareDettaglio(request,response,st);
				break;
			case 102:
				// carico la form di ricerca
				pulireSessione(request);
				//mCaricareLista(request,response, 102);
				mCaricareDettaglio(request,response,st);
				break;
			case 103:
				// carico la form di ricerca
				pulireSessione(request);
				//mCaricareLista(request,response, 103);
				mCaricareDettaglio(request,response,st);
				break;				
			case 105:
				pulireSessione(request);
				mCaricareDettaglio(request,response,st);
				break;
			}
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception ex)
		{
			throw new it.escsolution.escwebgis.common.DiogeneException(ex);
		}
	}	
	

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession sessione = request.getSession();
			
		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));
		
		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));
		
		Vector operatoriNumericiMag = new Vector();
		operatoriNumericiMag.add(new EscOperatoreFiltro(">",">"));
		operatoriNumericiMag.add(new EscOperatoreFiltro(">=",">="));
		
		Vector operatoriNumericiMin = new Vector();
		operatoriNumericiMin.add(new EscOperatoreFiltro("<","<"));
		operatoriNumericiMin.add(new EscOperatoreFiltro("<=","<="));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
		
		// costruisce il vettore di elementi per la pagina di ricerca
		//titolo gruppo
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI VERSAMENTO");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
				
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Fiscale");
		elementoFiltro.setAttributeName("CF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.CF_VERSANTE");
		listaElementiFiltro.add(elementoFiltro);
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,this.FRAME, nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(VersamentiAllLogic.FINDER)!= null){
			finder = (VersFinder)sessione.getAttribute(VersamentiAllLogic.FINDER);
		}
		else{
			finder = null;
		}

		 finder = (VersFinder)gestireMultiPagina(finder,request);

		 VersamentiAllLogic logic = new VersamentiAllLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(VersamentiAllLogic.LISTA_VERSAMENTI_ALL);
		 finder = (VersFinder)ht.get(VersamentiAllLogic.FINDER);

		 sessione.setAttribute(VersamentiAllLogic.LISTA_VERSAMENTI_ALL, vct_lista);
		 sessione.setAttribute(VersamentiAllLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,this.FRAME, nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(VersamentiAllLogic.FINDER) != null) {
			if (((Object)sessione.getAttribute(VersamentiAllLogic.FINDER)).getClass() == new VersFinder().getClass()) {
				finder = (VersFinder)sessione.getAttribute(VersamentiAllLogic.FINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, VersamentiAllLogic.LISTA_VERSAMENTI_ALL, 
				(Vector)sessione.getAttribute(VersamentiAllLogic.LISTA_VERSAMENTI_ALL), VersamentiAllLogic.FINDER);
		
		if (!azione.equals("")) {
			Vector listaOggetti = (Vector)sessione.getAttribute(VersamentiAllLogic.LISTA_VERSAMENTI_ALL);
		} else {
			oggettoSel = "";
			recordScelto = "";
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
		
		Hashtable ht = null;
		/*RECUPERO LA LISTA DI VERSAMENTI DA GESTIONALE*/
		VersamentiNewLogic vglogic = new VersamentiNewLogic(this.getEnvUtente(request));
		Vector listaPar = new Vector();
		EscElementoFiltro esc = new EscElementoFiltro();
		esc.setValore(oggettoSel);
		listaPar.add(esc);
		Vector<VersamentiNew> vg= vglogic.mCaricareListaVersamentiPopup(listaPar);
		sessione.setAttribute("LISTA_VERSAMENTI_ICI_GESTIONALE", vg);
		
		/*RECUPERO LA LISTA DI VERSAMENTI DA ICI-DM*/
		VersIciDmLogic logic = new VersIciDmLogic(this.getEnvUtente(request));
		ht = logic.mCaricareListeByCf(oggettoSel);	
		
		List<VersamentoIciDmDTO> ver = (List<VersamentoIciDmDTO>)ht.get(VersIciDmLogic.LISTA_VERS_ICI_DM);
		List<ViolazioneIciDmDTO> vio = (List<ViolazioneIciDmDTO>)ht.get(VersIciDmLogic.LISTA_VIOL_ICI_DM);
		
		sessione.setAttribute(VersIciDmLogic.LISTA_VERS_ICI_DM, ver);
		sessione.setAttribute(VersIciDmLogic.LISTA_VIOL_ICI_DM, vio);
		
		/*RECUPERO LA LISTA DI VERSAMENTI DA F24*/
		F24VersamentiLogic f24vlogic = new F24VersamentiLogic(this.getEnvUtente(request));
		ht = f24vlogic.mCaricareListaVersByCf(oggettoSel);
		List<F24VersamentoDTO> f24V = (List<F24VersamentoDTO>)ht.get(F24VersamentiLogic.LISTA_VERS_F24_CF);
		sessione.setAttribute(F24VersamentiLogic.LISTA_VERS_F24_CF, f24V);
		
		F24AnnullamentoLogic f24alogic = new F24AnnullamentoLogic(this.getEnvUtente(request));
		ht = f24alogic.mCaricareListaAnnByCf(oggettoSel);
		List<F24AnnullamentoDTO> f24A = (List<F24AnnullamentoDTO>)ht.get(F24AnnullamentoLogic.LISTA_ANN_F24_CF);
		sessione.setAttribute(F24AnnullamentoLogic.LISTA_ANN_F24_CF, f24A);
		
		RuoloTarsuLogic rtarsu = new RuoloTarsuLogic(this.getEnvUtente(request));
		ht = rtarsu.mCaricareListaRuoliByCF(oggettoSel);
		List<RuoloTarsuDTO> lstrsu= (List<RuoloTarsuDTO>)ht.get(RuoloTarsuLogic.LISTA_RUOLI_CF);
		sessione.setAttribute(RuoloTarsuLogic.LISTA_RUOLI_CF, lstrsu);
		
		/*RECUPERO LA LISTA DI VERSAMENTI TARSU POSTEL*/
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> oggInd = new Vector<OggettoIndice>();
		
		/*AGGIUNGI SOGGETTI ALLA RICERCA DI CORRELAZIONE*/
		List<String> soggPresenti = new ArrayList<String>();
		
		for(F24VersamentoDTO tes: f24V){
			this.addElementoIndice(tes.getId(), "33", "1", tes.getCf(), soggettiInd,soggPresenti);
		
			if(tes.getCf2()!=null)
				this.addElementoIndice(tes.getId(), "33", "2", tes.getCf2(), soggettiInd,soggPresenti);
		}
		
		for(F24AnnullamentoDTO tes: f24A)
			this.addElementoIndice(tes.getId(), "33", "3", tes.getCf(), soggettiInd,soggPresenti);
		
		for(VersamentoIciDmDTO v : ver)
			this.addElementoIndice(v.getId(), "37", "1", v.getCfVersante(), soggettiInd, soggPresenti);

		for(ViolazioneIciDmDTO v : vio)
			this.addElementoIndice(v.getId(), "37", "2", v.getCfVersante(), soggettiInd, soggPresenti);

		for(RuoloTarsuDTO v : lstrsu)
			this.addElementoIndice(v.getRuolo().getId(), "39", "1", v.getRuolo().getCodfisc(), soggettiInd, soggPresenti);

		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		sessione.setAttribute("indice_oggetti", oggInd);
		
		this.chiamaPagina(request,response,this.FRAME, nav);
	}	

	private void addElementoIndice(String id, String fonte, String prog, String descrizione, Vector<OggettoIndice> lst, List<String> tmp){
		if(!tmp.contains(descrizione)){
			
			OggettoIndice oi = new OggettoIndice();
			oi.setIdOrig(id);
			oi.setFonte(fonte);
			oi.setProgr(prog);
			oi.setDescrizione(descrizione!=null ? descrizione.trim() : "");
			lst.add(oi);
			
			tmp.add(descrizione);
		}
		
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((IciDmDTO)listaOggetti.get(recordSuccessivo)).getId();
	}

	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (VersFinder)finder2;
		VersamentiAllLogic logic = new VersamentiAllLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new VersFinder();
	}

	public String getTema() {
		return "";
	}
	
	public String getTabellaPerCrossLink() {
		return "";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

