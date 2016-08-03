package it.escsolution.escwebgis.concessioni.servlet;

import it.escsolution.escwebgis.catasto.bean.TipoAtto;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.concessioni.bean.ConcessioneFinder;
import it.escsolution.escwebgis.concessioni.bean.ConcessioneVisura;
import it.escsolution.escwebgis.concessioni.bean.ConcessioneVisuraDoc;
import it.escsolution.escwebgis.concessioni.bean.Tipo;
import it.escsolution.escwebgis.concessioni.logic.ConcessioniVisureLogic;
import it.webred.ct.data.access.basic.concedilizie.ConcVisuraDTO;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ConcessioniVisureServlet extends EscServlet implements EscService {
	
	private String localDataSource = null;
	public static final String NOMEFINDER = "FINDER47";
	private String recordScelto = "";
	
	private ConcessioneFinder finder = null;
	PulsantiNavigazione nav;
	
	public ConcessioniVisureServlet() {
		
	}//-------------------------------------------------------------------------
	
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
					case 33:
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
	
	
	
		private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception{
	
			//caricare dati che servono nella pagina di ricerca
			
			ConcessioniVisureLogic cvl = new ConcessioniVisureLogic(this.getEnvUtente(request));

			HttpSession sessione = request.getSession();
			
			Vector<TipoAtto> vctTipiAtti = cvl.mCaricareListaTipiAtti();
			
			Vector listaElementiFiltro = new Vector();
			Vector operatoriStringa = new Vector();
			operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
			operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));
	
			Vector operatoriStringaRid2 = new Vector();
			operatoriStringaRid2.add(new EscOperatoreFiltro("like","contiene"));	
			operatoriStringaRid2.add(new EscOperatoreFiltro("=","uguale"));
	
			Vector operatoriNumerici = new Vector();
			operatoriNumerici.add(new EscOperatoreFiltro("=","="));
			operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
			operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
			operatoriNumerici.add(new EscOperatoreFiltro("<","<"));
	
			Vector operatoriStringaRid = new Vector();
			operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));
			
			// costruisce il vettore di elementi per la pagina di ricerca
			EscElementoFiltro elementoFiltro = new EscElementoFiltro();
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Tipo Atto");
			elementoFiltro.setAttributeName("TIPO_ATTO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setListaValori(vctTipiAtti);
			elementoFiltro.setCampoFiltrato("TIPO_ATTO");
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Nome Intestatario");
			elementoFiltro.setAttributeName("NOME_INTESTATARIO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("NOME_INTESTATARIO");
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Numero Atto");
			elementoFiltro.setAttributeName("NUMERO_ATTO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setCampoFiltrato("NUMERO_ATTO");
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Nome Via");
			elementoFiltro.setAttributeName("NOME_VIA");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaRid2);
			elementoFiltro.setCampoFiltrato("NOME_VIA");
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Civico");
			elementoFiltro.setAttributeName("CIVICO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setCampoFiltrato("CIVICO");
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Num. Prot. Gen.");
			elementoFiltro.setAttributeName("NUM_PROT_GEN");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setCampoFiltrato("NUM_PROT_GEN");
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Num. Prot. Sett.");
			elementoFiltro.setAttributeName("NUM_PROT_SETT");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setCampoFiltrato("NUM_PROT_SETT");
			listaElementiFiltro.add(elementoFiltro);
			//-->ORDINAMENTO
			//titolo raggruppamento
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("CRITERI DI ORDINAMENTO");
			elementoFiltro.setSoloLabel(true);
			listaElementiFiltro.add(elementoFiltro);
			// oggetti per ordinamento
			Vector operatoriStringaOrderBy = new Vector();
			operatoriStringaOrderBy.add(new EscOperatoreFiltro("Asc","Ascendente per "));
			operatoriStringaOrderBy.add(new EscOperatoreFiltro("Desc","Discendente per "));
			Vector<Tipo> vctCampiOrdinamento = new Vector<Tipo>();
			Tipo eleOrd = new Tipo();
			eleOrd.setCodTipo("NOME_INTESTATARIO");
			eleOrd.setDesTipo("Intestatario"); 
			vctCampiOrdinamento.add(eleOrd);
			eleOrd = new Tipo();
			eleOrd.setCodTipo("TRIM(TPV),TRIM(NOME_VIA),CIVICO");
			eleOrd.setDesTipo("Numero Civico"); 
			vctCampiOrdinamento.add(eleOrd);
			//elemento per ordinamento
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Ordinamento");
			elementoFiltro.setAttributeName("ORDER BY");
			elementoFiltro.setTipo("O");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaOrderBy);
			elementoFiltro.setListaValori(vctCampiOrdinamento);
			elementoFiltro.setCampoFiltrato("");
			listaElementiFiltro.add(elementoFiltro);
			//fine ordinamento
			sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
			sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
			
	
			//chiamare la pagina di ricerca
			nav = new PulsantiNavigazione();
			nav.chiamataRicerca();
			this.chiamaPagina(request,response,"concessioni/concessioniVisureFrame.jsp", nav);
	
	}//-------------------------------------------------------------------------
		
		private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
		{
			// prendere dalla request i parametri di ricerca impostati dall'utente
			HttpSession sessione = request.getSession();

			if (sessione.getAttribute(NOMEFINDER) != null)
			{
				if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ConcessioneFinder().getClass())
				{
					finder = (ConcessioneFinder) sessione.getAttribute(NOMEFINDER);
				}
				else
					finder = null;
			}

			finder = (ConcessioneFinder) gestireMultiPagina(finder, request);

			ConcessioniVisureLogic logic = new ConcessioniVisureLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista

			Vector vct_lista = (Vector) ht.get(ConcessioniVisureLogic.LISTA);
			finder = (ConcessioneFinder) ht.get("FINDER");
			sessione.setAttribute(ConcessioniVisureLogic.LISTA, vct_lista);
			sessione.setAttribute(NOMEFINDER, finder);

			nav = new PulsantiNavigazione();
			if (chiamataEsterna){
				nav.chiamataEsternaLista();
				nav.setExt("1");
				nav.setPrimo(true);
			}
			else
				nav.chiamataInternaLista();
			this.chiamaPagina(request, response, "concessioni/concessioniVisureFrame.jsp", nav);
		}
		
		private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo) throws Exception
		{
			// prendere dalla request il parametrio identificativo dell'oggetto
			// cliccato
			// eseguire la query per caricare l'oggetto selezionato dal db
			// chiamare la pagina di dettaglio

			String azione = "";
			HttpSession sessione = request.getSession();
			
			removeOggettiIndiceDaSessione(sessione);
			
			ConcessioneFinder finder = null;
			// boolean chiamataEsterna = false;
			if (sessione.getAttribute(NOMEFINDER) != null)
			{
				if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ConcessioneFinder().getClass())
				{
					finder = (ConcessioneFinder) sessione.getAttribute(NOMEFINDER);
				}
			}

			if (request.getParameter("AZIONE") != null)
				azione = request.getParameter("AZIONE");
			gestioneMultiRecord(request, response, azione, finder, sessione, ConcessioniVisureLogic.LISTA, (Vector) sessione.getAttribute(ConcessioniVisureLogic.LISTA), NOMEFINDER);
			if (azione.equals(""))
			{
				oggettoSel = "";
				recordScelto = "";
				sessione.removeAttribute("BACK_JS_COUNT");
				sessione.removeAttribute("BACK_RECORD_ENABLE");

				if (request.getParameter("OGGETTO_SEL") != null)
				{
					oggettoSel = request.getParameter("OGGETTO_SEL");
				}
				if (request.getParameter("RECORD_SEL") != null)
				{
					recordScelto = request.getParameter("RECORD_SEL");
					if (finder != null)
						finder.setRecordAttuale(new Long(recordScelto).longValue());
				}
			}

			/*
			 * carica il dettaglio
			 */
			
			ConcessioniVisureLogic logic = new ConcessioniVisureLogic(this.getEnvUtente(request));
			Hashtable ht = null;
			
			//nel caso in cui provengo da Correlazione Vie/Civici e il parametro non inizia con #, e nel caso della Correlaione Fabbricati , allora l'id si riferisce all'oggetto e non al soggetto. 
			//In tutti gli altri casi l'id si riferisce al soggetto
			log.debug("ricerca dettaglio concessioni visure da ID: " + oggettoSel );
			if (tipo == 101)
				ht = logic.mCaricareDettaglio(oggettoSel);	
			else if (tipo== 102 || tipo == 103 ){
				if (oggettoSel.startsWith("#") == false)
					ht = logic.mCaricareDettaglio(oggettoSel);
				
				else{
					oggettoSel= oggettoSel.substring(1);
					ht = logic.mCaricareDettaglio(oggettoSel);	
				}
			}
			else if (tipo == 105)
				ht = logic.mCaricareDettaglio(oggettoSel);	
			else
				ht = logic.mCaricareDettaglio(oggettoSel);	
			
	
			ConcVisuraDTO con = (ConcVisuraDTO) ht.get(ConcessioniVisureLogic.CONCESSIONE_VISURA);
			//ConcessioneVisuraDoc conDoc = (ConcessioneVisuraDoc)ht.get("DOCUMENTI_VISURA");
			sessione.setAttribute(ConcessioniVisureLogic.CONCESSIONE_VISURA, con);
			//sessione.setAttribute("DOCUMENTI_VISURA", conDoc);
			
			nav = new PulsantiNavigazione();
			if (chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();
			
			//Carica dati correlazione
			Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
			Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
			Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		
			OggettoIndice oi = new OggettoIndice();
			oi.setIdOrig(con.getId());
			oi.setFonte("35");
			oi.setProgr("1");
			
			if (con.getNomeIntestatario()!= null && !"".equals(con.getNomeIntestatario()) && !"-".equals(con.getNomeIntestatario()))
				oi.setDescrizione( con.getNomeIntestatario());
					
			soggettiInd.add(oi);
			
			oi = new OggettoIndice();
			oi.setIdOrig(con.getId());
			oi.setFonte("35");
			oi.setProgr("1");
			
			String descV = null;
			String descC = null;
			if (con.getPrefisso()!=null && con.getNomeVia()!=null){
				 descV = con.getPrefisso()+" "+con.getNomeVia();
				 if(con.getCivico()!=null)
					 descC = descV+" "+con.getCivico().toString();
				 else
					 descC = descV;
			}
			
			oi = new OggettoIndice();
			oi.setIdOrig(con.getId());
			oi.setFonte("35");
			oi.setProgr("1");
			
			oi.setDescrizione(descV);
			vieInd.add(oi);
			
			oi = new OggettoIndice();
			oi.setIdOrig(con.getId());
			oi.setFonte("35");
			oi.setProgr("1");
			
			oi.setDescrizione(descC);
			civiciInd.add(oi);
			
			sessione.setAttribute("indice_vie", vieInd);
			sessione.setAttribute("indice_civici", civiciInd);
			sessione.setAttribute("indice_soggetti", soggettiInd);
			
			

			this.chiamaPagina(request, response, "concessioni/concessioniVisureFrame.jsp", nav);
		}//---------------------------------------------------------------------

		public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
		{
			finder = (ConcessioneFinder) finder2;
			ConcessioniVisureLogic logic = new ConcessioniVisureLogic(this.getEnvUtente(request));
			return logic.mCaricareLista(this.vettoreRicerca, finder);
		}//---------------------------------------------------------------------
		
		public EscFinder getNewFinder(){
			return new ConcessioneFinder();
		}//---------------------------------------------------------------------
		
		public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
		{
			String cod = "";
			ConcessioneVisura cv = (ConcessioneVisura)listaOggetti.get(recordSuccessivo); 
			if (cv != null)
				cod = String.valueOf(cv.getInxdoc()); 
			return cod;
		}//---------------------------------------------------------------------
		
		public String getTema(){
			String s = "Concessioni Edilizie"; 
			return s;
		}//---------------------------------------------------------------------

		public String getTabellaPerCrossLink()
		{
			return "MI_CONC_EDILIZIE_VISURE_D";
		}//---------------------------------------------------------------------
		
		public String getLocalDataSource() {
			return localDataSource;
		}//---------------------------------------------------------------------

}//-----------------------------------------------------------------------------
