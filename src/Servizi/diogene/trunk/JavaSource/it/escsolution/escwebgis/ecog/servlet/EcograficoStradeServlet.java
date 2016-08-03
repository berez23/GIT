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
import it.escsolution.escwebgis.common.interfacce.InterfacciaObject;
import it.escsolution.escwebgis.ecog.bean.StradaFinder;
import it.escsolution.escwebgis.ecog.logic.EcograficoCiviciLogic;
import it.escsolution.escwebgis.ecog.logic.EcograficoStradeLogic;
import it.escsolution.escwebgis.ecografico.bean.StradaEcografico;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
public class EcograficoStradeServlet extends EscServlet implements EscService {
	
	private static final long serialVersionUID = 1L;
	
	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private StradaFinder finder = null;
	public static final String NOMEFINDER = "FINDER116";
	public static final String INTERFACCIA_CIVICI = EcograficoStradeServlet.class.getName() + "@INTERFACCIA_CIVICI";

	public void EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 *
		 */
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			EseguiServizioExt(request,response);
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
				super.EseguiServizio(request,response);
			 try{
				 if (request.getParameter("listavie") != null)
					mListaVie(request, response, "EC_TOP_STRADE", null, "NOME_STRADA", "NOME_STRADA");
				 else {
					 switch (st){
						 case 1:
							// carico la form di ricerca
							pulireSessione(request);
							mCaricareFormRicerca(request,response);
							break;
						 case 2:
							// vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
							//pulireSessione(request);
							mCaricareLista(request,response,2);
							break;
						 case 3:
							// ho cliccato su un elemento --> visualizzo il dettaglio
							//this.leggiCrossLink(request);
							mCaricareDettaglio(request,response, false);
							break;
						 case 33:
							 // ho cliccato su un elemento --> visualizzo il dettaglio
							 
							//this.leggiCrossLink(request);
							mCaricareDettaglio(request,response, false);
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

	public void EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
				case 102:
					// carico la form di ricerca
					pulireSessione(request);
					//mCaricareLista(request,response,102);
					mCaricareDettaglio(request,response, false);
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
	
	
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		/** Comune */
		Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
		/***/
		//caricare dati che servono nella pagina di ricerca
		EcograficoCiviciLogic logic = new EcograficoCiviciLogic(this.getEnvUtente(request));
		Map map = logic.mCaricareDatiFormRicerca(request.getUserPrincipal().getName());
		HttpSession sessione = request.getSession();

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

		//costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("COMUNE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctComuni);
		elementoFiltro.setCampoFiltrato("V.FK_COMUNI_BELF");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Sedime");
		elementoFiltro.setAttributeName("SEDIME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vectorSedime);
		elementoFiltro.setCampoFiltrato("V.SPECIE_STRADA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Strada");
		elementoFiltro.setAttributeName("CODICE_STRADA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("V.CODICE_STRADA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
	    elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("V.NOME_STRADA");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/EcograficoStrade?listavie='+document.getElementById('NOME').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"ecog/stradeEcogFrame.jsp", nav);
	}



	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,int tipo)throws Exception{
			// prendere dalla request i parametri di ricerca impostati dall'utente
			HttpSession sessione = request.getSession();
			
			if (request.getParameter("OGGETTO_SEL")!= null){
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}

			if (sessione.getAttribute(NOMEFINDER)!= null){
						finder = (StradaFinder)sessione.getAttribute(NOMEFINDER);
			}
			else{
				finder = null;
			}

			finder = (StradaFinder)gestireMultiPagina(finder,request);

			EcograficoStradeLogic logic = new EcograficoStradeLogic(this.getEnvUtente(request));
			Hashtable ht =null;
			
			if (tipo==2)
			 ht = logic.mCaricareListaStrade(vettoreRicerca,finder);
			else
				ht=logic.mCaricareListaStradeFromVia(oggettoSel, finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista


			Vector vct_lista_strade = (Vector)ht.get(EcograficoStradeLogic.LISTA_STRADE);
			finder = (StradaFinder)ht.get(EcograficoStradeLogic.FINDER);
			sessione.setAttribute(EcograficoStradeLogic.LISTA_STRADE,vct_lista_strade);
			sessione.setAttribute(NOMEFINDER,finder);

			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();

			this.chiamaPagina(request,response,"ecog/stradeEcogFrame.jsp", nav);

		}


	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, boolean notListaPrincipale)throws Exception
	{
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		StradaFinder finder = null;

		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new StradaFinder().getClass()){
				finder = (StradaFinder)sessione.getAttribute(NOMEFINDER);
			}
			else{
				// il finder non corrisponde -->
				finder = null;
			}
		}

		if (request.getParameter("AZIONE")!= null)
				azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,EcograficoStradeLogic.LISTA_STRADE,(Vector)sessione.getAttribute(EcograficoStradeLogic.LISTA_STRADE),NOMEFINDER);
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


		EcograficoStradeLogic logic = new EcograficoStradeLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioStrada(oggettoSel);

		//mCaricareCivici(request, response, oggettoSel, notListaPrincipale);

		StradaEcografico str = (StradaEcografico) ht.get(EcograficoStradeLogic.STRADA);
		sessione.setAttribute(EcograficoStradeLogic.STRADA, str);
		//sessione.setAttribute("TIPO",new Boolean(notListaPrincipale));
		ArrayList<Object[]> strInViario = (ArrayList<Object[]>) ht.get(EcograficoStradeLogic.STRADA_IN_VIARIO);
		sessione.setAttribute(EcograficoStradeLogic.STRADA_IN_VIARIO, strInViario);

		//TEST MB
		EcograficoCiviciLogic logicCiv = new EcograficoCiviciLogic(this.getEnvUtente(request));
		Hashtable htc = logicCiv.mCaricareListaCiviciStrada(str.getId());
		Vector listCiv = (Vector)htc.get(EcograficoCiviciLogic.LISTA_CIVICI_STRADA);
		sessione.setAttribute(EcograficoCiviciLogic.LISTA_CIVICI_STRADA, listCiv);
		//FINE TEST MB

		// GIULIO: UTILIZZO GLI STESSI MECCANISMO DEI CROSS-LINK
		InterfacciaObject interfaccia = new InterfacciaObject();
		String url = "SERVLET=/" + Tema.getServletName(uc);
		url = url + "&QUERY=" + "select distinct UK_CIVICO from ec_top_civici where PKID_STRA" + "&NROW=0";
		interfaccia.setLink(URLEncoder.encode(url,"US-ASCII"));
		interfaccia.setDescrizione("Civici");
		sessione.setAttribute(INTERFACCIA_CIVICI, interfaccia);
		
		OggettoIndice oi = new OggettoIndice();
		
		oi.setDescrizione(str.getSpecieStrada() + " " +str.getNomeStrada());
		oi.setIdOrig(str.getId());
		oi.setFonte("29");
		oi.setProgr("1");
		
		Vector<OggettoIndice> vInd = new Vector<OggettoIndice>();
		vInd.add(oi);
		
		sessione.setAttribute("indice_vie", vInd);


		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"ecog/stradeEcogFrame.jsp", nav);
	}


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((StradaEcografico)listaOggetti.get(recordSuccessivo)).getId();
	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (StradaFinder)finder2;
		EcograficoStradeLogic logic = new EcograficoStradeLogic(this.getEnvUtente(request));
		return logic.mCaricareListaStrade(this.vettoreRicerca,finder);
	}


	public EscFinder getNewFinder(){
			return new StradaFinder();
		}
	public String getTema() {
		return "Ecografico Strade";
	}
	public String getTabellaPerCrossLink() {
		return "EC_TOP_STRADE";
	}
}
