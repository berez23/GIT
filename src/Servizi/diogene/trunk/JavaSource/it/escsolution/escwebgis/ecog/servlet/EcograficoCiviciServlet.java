/*
 * Created on 10-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.ecog.servlet;

import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.ecog.bean.CivicoFinder;
import it.escsolution.escwebgis.ecog.logic.EcograficoCiviciLogic;
import it.escsolution.escwebgis.ecografico.bean.CivicoEcografico;
import it.escsolution.escwebgis.toponomastica.bean.Civico;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EcograficoCiviciServlet extends EscServlet implements EscService {

	private static final long serialVersionUID = 1L;

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private CivicoFinder finder = null;
	private final String NOMEFINDER = "FINDER117";

		public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
		{
			
	    	HttpSession sessione = request.getSession();		
			sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
			log.info("Funzionalita EcograficoCiviciServlet1:" + Tema.getNomeFunzionalita(uc));
			
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
		
		log.info("Funzionalita EcograficoCiviciServlet2:" + Tema.getNomeFunzionalita(uc));
		
		
		super.EseguiServizio(request,response);
			 try{
				 if (request.getParameter("listavie") != null)
					mListaVie(request, response, "EC_TOP_CIVICI", null, "DESCR_STRADA", "DESCR_STRADA");
				 else {
					 log.info("ST=" + st);
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
		EcograficoCiviciLogic logic = new EcograficoCiviciLogic(this.getEnvUtente(request));
		Map map = logic.mCaricareDatiFormRicerca(request.getUserPrincipal().getName());

		Vector vectorSedime = null;
		request.setAttribute(EcograficoCiviciLogic.LISTA_SEDIME, (vectorSedime = new Vector((Collection) map.get(EcograficoCiviciLogic.LISTA_SEDIME))));
		sessione.setAttribute(EcograficoCiviciLogic.LISTA_SEDIME, vectorSedime);


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
		elementoFiltro.setCampoFiltrato("S.SPECIE_STRADA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome Via");
		elementoFiltro.setAttributeName("NOME_VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("S.NOME_STRADA");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/EcograficoCivici?listavie='+document.getElementById('NOME_VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Civico");
		elementoFiltro.setAttributeName("DESCRIZIONE_CIVICO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("C.DESCRIZIONE_CIVICO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("F.FOGLIO_CT");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("lpad5_0");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("F.PARTICELLA_CT");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"ecog/civiciEcogFrame.jsp", nav);
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

			EcograficoCiviciLogic logic = new EcograficoCiviciLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareListaCivici(vettoreRicerca,finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista

			Vector vct_lista_civici = (Vector) ht.get(EcograficoCiviciLogic.LISTA_CIVICI);
			finder = (CivicoFinder) ht.get(EcograficoCiviciLogic.FINDER);
			sessione.setAttribute(EcograficoCiviciLogic.LISTA_CIVICI, vct_lista_civici);
			sessione.setAttribute(NOMEFINDER, finder);

			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();

			this.chiamaPagina(request,response,"ecog/civiciEcogFrame.jsp", nav);
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
		gestioneMultiRecord(request, response, azione, finder, sessione, EcograficoCiviciLogic.LISTA_CIVICI, (Vector) sessione.getAttribute(EcograficoCiviciLogic.LISTA_CIVICI), NOMEFINDER);
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

		EcograficoCiviciLogic logic = new EcograficoCiviciLogic(this.getEnvUtente(request));
		Hashtable<String, Object> ht = logic.mCaricareDettaglioCivici(oggettoSel, st);

		sessione.setAttribute(EcograficoCiviciLogic.CIVICO, ht.get(EcograficoCiviciLogic.CIVICO));
	//	sessione.setAttribute(EcograficoCiviciLogic.CIVICIARIO, ht.get(EcograficoCiviciLogic.CIVICIARIO));
		sessione.setAttribute(EcograficoCiviciLogic.LISTA_FABBRICATI, ht.get(EcograficoCiviciLogic.LISTA_FABBRICATI));
		sessione.setAttribute("TIPO", new Boolean(tipo));

		
		// Recupero il civico e creo l'oggetto indice corr
		
		CivicoEcografico civ = (CivicoEcografico) ht.get(EcograficoCiviciLogic.CIVICO);
		OggettoIndice oi = new OggettoIndice();
		
		oi.setDescrizione(civ.getDescrStrada() + " " +  civ.getDescrizioneCivico());
		oi.setIdOrig(civ.getId());
		oi.setFonte("29");
		oi.setProgr("1");
		
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
		
		log.info("Civico " + sessione.getAttribute(EcograficoCiviciLogic.CIVICO));
		log.info("Lista Fabb " + ((ArrayList)sessione.getAttribute(EcograficoCiviciLogic.LISTA_FABBRICATI)).size());
		
		log.info("Sto chiamando la pagina jsp " + "ecog/civiciEcogFrame.jsp");
		
		this.chiamaPagina(request, response, "ecog/civiciEcogFrame.jsp", nav);
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((CivicoEcografico)listaOggetti.get(recordSuccessivo)).getChiave();
	}
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (CivicoFinder)finder2;
		EcograficoCiviciLogic logic = new EcograficoCiviciLogic(this.getEnvUtente(request));
		return logic.mCaricareListaCivici(this.vettoreRicerca, finder);
	}
	public EscFinder getNewFinder(){
		return new CivicoFinder();
	}
	public String getTema() {
		return "Ecografico Civici";
	}
	public String getTabellaPerCrossLink() {
		return "EC_TOP_CIVICI";
	}
}
