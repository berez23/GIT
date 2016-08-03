package it.escsolution.escwebgis.gas.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.enel.logic.EnelDwhLogic;
import it.escsolution.escwebgis.gas.bean.FornitureGas;
import it.escsolution.escwebgis.gas.bean.FornitureGasFinder;
import it.escsolution.escwebgis.gas.logic.FornitureGasLogic;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FornitureGasServlet extends EscServlet implements EscService {

	private String recordScelto;
	public static final String NOMEFINDER = "FINDER54";
	private FornitureGasFinder finder = null;
	PulsantiNavigazione nav;
	
	private String localDataSource = "jdbc/Diogene_DS";

	

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		
		
		super.EseguiServizio(request,response);
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		String ext = request.getParameter("IND_EXT");
		
		if ("1".equals(ext)) 
			_EseguiServizioExt(request,response);
		else
			_EseguiServizio(request, response);
	}
	
	private void _EseguiServizioExt(
			HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException {	
	   try{
 		 switch (st){
 		 case 101:			 		
			 mCaricareDettaglio(request,response);					
			 break; 
 		case 102:			 		
			 mCaricareDettaglio(request,response);					
			 break; 	
		   case 103:			 		
			 mCaricareDettaglio(request,response);					
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
		
	private void _EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {	
		try
		{
			if (request.getParameter("listavie") != null)
				mListaVie(request, response, "SIT_U_GAS", null, "INDIRIZZO_UTENZA", "INDIRIZZO_UTENZA");
			else {
				switch (st){
					 case 1:
						 // carico la form di ricerca
						 pulireSessione(request);
						 mCaricareFormRicerca(request,response);
						 break;
					 case 2:
						 mCaricareLista(request,response);							 
						 break;
					 case 3:
						 mCaricareDettaglio(request,response);
						// this.leggiCrossLink(request);						 
						 break;
					 case 33:
						 mCaricareDettaglio(request,response);
						 //this.leggiCrossLink(request);						 
						 break;
				}
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

	
	
	
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();
		

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringaRid.add(new EscOperatoreFiltro("like", "contiene"));

		Vector operatoriStringaUguale = new Vector();
		operatoriStringaUguale.add(new EscOperatoreFiltro("=", "uguale"));
		
		Vector operatoriNumericiRid = new Vector();
		operatoriNumericiRid.add(new EscOperatoreFiltro("=","="));

		// costruisce il vettore di elementi per la pagina di ricerca
		//titolo gruppo
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI FORNITURE GAS");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno");
		elementoFiltro.setAttributeName("ANNO_RIFERIMENTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("ANNO_RIFERIMENTO");
		listaElementiFiltro.add(elementoFiltro);


		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Identificativo Utenza");
		elementoFiltro.setAttributeName("IDENTIFICATIVO_UTENZA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("IDENTIFICATIVO_UTENZA");
		listaElementiFiltro.add(elementoFiltro);

		
		
		//numero protocollo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("CF Erogante");
		elementoFiltro.setAttributeName("CF_EROGANTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("CF_EROGANTE");
		listaElementiFiltro.add(elementoFiltro);
		
		

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("CF Utente");
		elementoFiltro.setAttributeName("CF_TITOLARE_UTENZA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("CF_TITOLARE_UTENZA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME_UTENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("COGNOME_UTENTE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME_UTENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("NOME_UTENTE");
		listaElementiFiltro.add(elementoFiltro);		
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Ragione Sociale");
		elementoFiltro.setAttributeName("RAGIONE_SOCIALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("RAGIONE_SOCIALE");
		listaElementiFiltro.add(elementoFiltro);	
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo Utenza");
		elementoFiltro.setAttributeName("INDIRIZZO_UTENZA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("INDIRIZZO_UTENZA");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/FornitureGas?listavie='+document.getElementById('INDIRIZZO_UTENZA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);				
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));

		//chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"fornitureGas/fornitureGasFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new FornitureGasFinder().getClass())
			{
				finder = (FornitureGasFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (FornitureGasFinder)gestireMultiPagina(finder,request);		
		FornitureGasLogic logic = new FornitureGasLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);
		Vector vct_lista = (Vector)ht.get(FornitureGasLogic.LISTA);
		finder = (FornitureGasFinder)ht.get(FornitureGasLogic.FINDER);
		sessione.setAttribute(FornitureGasLogic.LISTA,vct_lista);
		sessione.setAttribute(NOMEFINDER,finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"fornitureGas/fornitureGasFrame.jsp",nav);
	}
	
	
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		FornitureGasFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new FornitureGasFinder().getClass())
			{
				finder = (FornitureGasFinder)sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, FornitureGasLogic.LISTA, (Vector)sessione.getAttribute(FornitureGasLogic.LISTA), NOMEFINDER);

		if (azione.equals(""))
		{
			oggettoSel="";
			recordScelto="";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL")!= null)
			{
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL")!= null)
			{
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder!=null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		//carica il dettaglio
		FornitureGasLogic logic = new FornitureGasLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglio(oggettoSel);
		FornitureGas gas = (FornitureGas)ht.get(FornitureGasLogic.DATI_GAS);
		sessione.setAttribute(FornitureGasLogic.DATI_GAS, gas);	
		//carica gli id storici
		
		// CAROCO GLI ID E LA DATE DI STORICIZZAZIONE
		idStorici = logic.caricaIdStorici(oggettoSel);	
		
		// Aggiungo i valori per l'indice di correlazione
		
		OggettoIndice oi1 = new OggettoIndice();
		
		oi1.setIdOrig(gas.getId());
		oi1.setFonte("12");
		oi1.setProgr("1");
		oi1.setDescrizione(gas.getIndirizzoUtenza());
		
		Vector<OggettoIndice> cOggettiInd = new Vector<OggettoIndice>();
		cOggettiInd.add(oi1);
		sessione.setAttribute("indice_civici", cOggettiInd);
		
		
		Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
		
		OggettoIndice oi = new OggettoIndice();
		
		oi.setIdOrig(gas.getId());
		oi.setFonte("12");
		oi.setProgr("1");
		
		if ((gas.getCognomeUtente()!= null && !"".equals(gas.getCognomeUtente())) || (gas.getNomeUtente()!= null && !"".equals(gas.getNomeUtente())))
			oi.setDescrizione(gas.getCognomeUtente() + " " + gas.getNomeUtente());
		else if (gas.getRagioneSociale()!= null && !"".equals(gas.getRagioneSociale()))
			oi.setDescrizione(gas.getRagioneSociale());
		
		sOggettiInd.add(oi);
		
		sessione.setAttribute("indice_soggetti", sOggettiInd);
		
	Vector<OggettoIndice> vieOggettiInd = new Vector<OggettoIndice>();
		
		OggettoIndice oi2 = new OggettoIndice();
		
		oi2.setIdOrig(gas.getId());
		oi2.setFonte("12");
		oi2.setProgr("1");
		
		oi2.setDescrizione(gas.getIndirizzoUtenza());
		
		vieOggettiInd.add(oi2);
		
		sessione.setAttribute("indice_vie", vieOggettiInd);
		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		
		
		this.chiamaPagina(request,response,"fornitureGas/fornitureGasFrame.jsp", nav);		
	}
	

	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (FornitureGasFinder)finder2;
		FornitureGasLogic logic = new FornitureGasLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder()
	{
		return new FornitureGasFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo)
	{
		FornitureGas con = (FornitureGas)listaOggetti.get(recordSuccessivo);
		String key = con.getChiave();
		return key;
	}

	public String getTema()
	{
		return "Forniture Gas";
	}

	public String getTabellaPerCrossLink()
	{
		return "SIT_U_GAS";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}
