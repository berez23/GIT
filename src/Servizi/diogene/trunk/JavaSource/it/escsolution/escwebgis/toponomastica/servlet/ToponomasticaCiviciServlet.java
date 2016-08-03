/*
 * Created on 10-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.toponomastica.servlet;

import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.toponomastica.bean.Civico;
import it.escsolution.escwebgis.toponomastica.bean.CivicoFinder;
import it.escsolution.escwebgis.toponomastica.logic.ToponomasticaCiviciLogic;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ToponomasticaCiviciServlet extends EscServlet implements EscService {

		PulsantiNavigazione nav = new PulsantiNavigazione();
		private String recordScelto;
		private ToponomasticaCiviciLogic logic = null;
		private CivicoFinder finder = null;
		private final String NOMEFINDER = "FINDER11";

		public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
		{
			
	    	HttpSession sessione = request.getSession();		
			sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
			
	    	super.EseguiServizio(request,response);
			
			String ext= request.getParameter("IND_EXT");
			
			if (ext!= null && ext.equals("1"))
				_EseguiServizioExt(request,response);
			else
				_EseguiServizio(request,response);
			
		}
		
		public void _EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {
					/*
					 * ad ogni uc corrisponde una funzionalità diversa
					 *
					 */
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));		
		
		super.EseguiServizio(request,response);
			 try{
				 if (request.getParameter("listavie") != null)
					mListaVie(request, response, "VISTA_CIVICI", null, "NOME_VIA", "NOME_VIA");
				 else {
					 switch (st){
						 case 1:
							 // carico la form di ricerca
							 pulireSessione(request);
							 mCaricareFormRicerca(request,response);
							 break;
						 case 2:
							 // vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
							 mCaricareLista(request,response);
							 break;
						 case 3:
							 // ho cliccato su un elemento --> visualizzo il dettaglio
							 mCaricareDettaglio(request,response, false,3);
							//this.leggiCrossLink(request);
							 break;
						 case 33:
							 // ho cliccato su un elemento --> visualizzo il dettaglio
							 mCaricareDettaglio(request,response, false,33);
							//this.leggiCrossLink(request);
							 break;
					 }
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
		
		public void _EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
		{
			super.EseguiServizio(request,response);
			try
			{
				
					switch (st){
					
						case 103:
							mCaricareDettaglio(request,response, false,103);					 
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
		/** Comune */
		Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
		/***/
		HttpSession sessione = request.getSession();
		ToponomasticaCiviciLogic logic = new ToponomasticaCiviciLogic(this.getEnvUtente(request));
		Map map = logic.mCaricareDatiFormRicerca(request.getUserPrincipal().getName());

		Vector vectorSedime = null;
		request.setAttribute(ToponomasticaCiviciLogic.LISTA_SEDIME, (vectorSedime = new Vector((Collection) map.get(ToponomasticaCiviciLogic.LISTA_SEDIME))));
		sessione.setAttribute(ToponomasticaCiviciLogic.LISTA_SEDIME, vectorSedime);


		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		EscElementoFiltro elementoFiltro = null;

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("COMUNE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctComuni);
		elementoFiltro.setCampoFiltrato("COM.CODI_FISC_LUNA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Sedime");
		elementoFiltro.setAttributeName("SEDIME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vectorSedime);
		elementoFiltro.setCampoFiltrato("VC.SEDIME");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome Via");
		elementoFiltro.setAttributeName("NOME_VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("VC.NOME_VIA");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/ToponomasticaCivici?listavie='+document.getElementById('NOME_VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Civico");
		elementoFiltro.setAttributeName("DESCRIZIONE_CIVICO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("VC.DESCRIZIONE_CIVICO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("VCU.FOGLIO_CATASTALE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("lpad5_0");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("VCU.PARTICELLA_CATASTALE");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"toponomastica/civiciFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
			// prendere dalla request i parametri di ricerca impostati dall'utente
			HttpSession sessione = request.getSession();

			if (sessione.getAttribute(NOMEFINDER)!= null){
				finder = (CivicoFinder)sessione.getAttribute(NOMEFINDER);
			}
			else{
				finder = null;
			}
			finder = (CivicoFinder)gestireMultiPagina(finder,request);

			ToponomasticaCiviciLogic logic = new ToponomasticaCiviciLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareListaCivici(vettoreRicerca,finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista

			Vector vct_lista_civici = (Vector) ht.get(ToponomasticaCiviciLogic.LISTA_CIVICI);
			finder = (CivicoFinder) ht.get(ToponomasticaCiviciLogic.FINDER);
			sessione.setAttribute(ToponomasticaCiviciLogic.LISTA_CIVICI, vct_lista_civici);
			sessione.setAttribute(NOMEFINDER, finder);

			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();

			this.chiamaPagina(request,response,"toponomastica/civiciFrame.jsp", nav);
		}


	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, boolean tipo, int st)throws Exception
	{
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		CivicoFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) != null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new CivicoFinder().getClass())
			{
				finder = (CivicoFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
			{
				finder = null;
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, ToponomasticaCiviciLogic.LISTA_CIVICI, (Vector) sessione.getAttribute(ToponomasticaCiviciLogic.LISTA_CIVICI), NOMEFINDER);
		if (azione.equals(""))
		{
			oggettoSel = ""; recordScelto = "";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL") != null){
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL") != null){
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder != null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		ToponomasticaCiviciLogic logic = new ToponomasticaCiviciLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioCivici(oggettoSel, st);

		sessione.setAttribute(ToponomasticaCiviciLogic.CIVICO, ht.get(ToponomasticaCiviciLogic.CIVICO));
		sessione.setAttribute(ToponomasticaCiviciLogic.CIVICIARIO, ht.get(ToponomasticaCiviciLogic.CIVICIARIO));
		sessione.setAttribute(ToponomasticaCiviciLogic.LISTA_RIFERIMENTI_CATASTALI, ht.get(ToponomasticaCiviciLogic.LISTA_RIFERIMENTI_CATASTALI));
		sessione.setAttribute("TIPO", new Boolean(tipo));

		
		// Recupero il civico e creo l'oggetto indice corr
		
		Civico civ = (Civico) ht.get(ToponomasticaCiviciLogic.CIVICO);
		OggettoIndice oi = new OggettoIndice();
		
		oi.setDescrizione(civ.getSedime() + " " + civ.getStrada() + " " +  civ.getDescrCivico());
		oi.setIdOrig(civ.getPk_sequ_civico());
		oi.setFonte("4");
		oi.setProgr("2");
		
		Vector<OggettoIndice> vInd = new Vector<OggettoIndice>();
		vInd.add(oi);
		
		sessione.setAttribute("indice_civici", vInd);
		
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		nav.setMappa(true);

		/* gestione mappa
		{
			JavaBeanGlobalVar beanGlobale = logic.mCaricareDatiGrafici(oggettoSel);
			if (beanGlobale != null){
				nav.setMappa(true);
				sessione.setAttribute("MAPPA", beanGlobale);
			}
		}
		*/// fine gestione mappa

		this.chiamaPagina(request, response, "toponomastica/civiciFrame.jsp", nav);
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((Civico)listaOggetti.get(recordSuccessivo)).getChiave();
	}
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (CivicoFinder)finder2;
		ToponomasticaCiviciLogic logic = new ToponomasticaCiviciLogic(this.getEnvUtente(request));
		return logic.mCaricareListaCivici(this.vettoreRicerca, finder);
	}
	public EscFinder getNewFinder(){
		return new CivicoFinder();
	}
	public String getTema() {
		return "Toponomastica";
	}
	public String getTabellaPerCrossLink() {
		return "VISTA_CIVICI";
	}
}
