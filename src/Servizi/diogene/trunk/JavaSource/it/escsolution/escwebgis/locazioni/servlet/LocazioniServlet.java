/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.locazioni.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.docfa.logic.DocfaLogic;
import it.escsolution.escwebgis.locazioni.bean.Locazioni;
import it.escsolution.escwebgis.locazioni.bean.LocazioniFinder;
import it.escsolution.escwebgis.locazioni.bean.LocazioniImmobili;
import it.escsolution.escwebgis.locazioni.logic.LocazioniLogic;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LocazioniServlet extends EscServlet implements EscService
{

	private String recordScelto;

	private LocazioniLogic logic = null;

	// private AnagrafeFinder finder = null;
	public static final String NOMEFINDER = "FINDER30";

	private LocazioniFinder finder = null;

	PulsantiNavigazione nav;
	
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

	public void _EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 */
		// super.init(getServletConfig());
		// String aaa = getServletConfig().getServletName();
		super.EseguiServizio(request, response);
		try
		{
			log.debug("*****************************************"+st);
			if (request.getParameter("listavie") != null)
				mListaVie(request, response);
			else {
				switch (st)
				{
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
						if (request.getParameter("POPUP") != null && new Boolean(request.getParameter("POPUP")).booleanValue()) {
							//visualizzazione in popup per soggetto
							mCaricareDettaglioPopupSoggetto(request, response);
						}else{
							// ho cliccato su un elemento --> visualizzo il dettaglio
							mCaricareDettaglio(request, response,3);
							//this.leggiCrossLink(request);
						}
						break;
					case 33:
						
							// ho cliccato su un elemento --> visualizzo il dettaglio
							mCaricareDettaglio(request, response, 33);
							//this.leggiCrossLink(request);
						
						break;
					/*
					 * case 4: // ho cliccato su un elemento --> visualizzo il dettaglio
					 * mCaricareDettaglio(request,response);
					 * this.leggiCrossLink(request); break; case 5: // ho cliccato su un
					 * elemento --> visualizzo il dettaglio
					 * mCaricareLista(request,response,true); break;
					 */
				}
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
	
	
	public void _EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			    log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+st);
			    switch (st){
					case 101:
						mCaricareDettaglio(request,response,101);			
						break;
					case 102:
						mCaricareDettaglio(request, response,102);					 
						break;
					case 103:
						mCaricareDettaglio(request, response,103);					 
						break;
					case 104:
						mCaricareDettaglio(request, response,104);						 
						break;
					case 105:
						mCaricareDettaglio(request, response,105);						 
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

		// caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=", "="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>", "<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">", ">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<", "<"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca

		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Ufficio di registrazione contratto");
		elementoFiltro.setAttributeName("UFFICIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("MI_LOCAZIONI_A.UFFICIO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno registrazione locazione");
		elementoFiltro.setAttributeName("ANNO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");//todo
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("MI_LOCAZIONI_A.ANNO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Serie registrazione locazione");
		elementoFiltro.setAttributeName("SERIE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("MI_LOCAZIONI_A.SERIE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero registrazione locazione");
		elementoFiltro.setAttributeName("NUMERO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("MI_LOCAZIONI_A.NUMERO");
		listaElementiFiltro.add(elementoFiltro);
		
/*		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_D_PERSONA.COGNOME");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_D_PERSONA.NOME");
		listaElementiFiltro.add(elementoFiltro);*/

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale (del proprietario o dell'inquilino)");
		elementoFiltro.setAttributeName("CODICEFISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("LTRIM(RTRIM(MI_LOCAZIONI_B.CODICEFISCALE))");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("LTRIM(RTRIM(LOCAZIONI_I.FOGLIO))");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("LTRIM(RTRIM(LOCAZIONI_I.PARTICELLA))");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("SUBALTERNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("LTRIM(RTRIM(LOCAZIONI_I.SUBALTERNO))");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo");
		elementoFiltro.setAttributeName("INDIRIZZO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("NVL(LOCAZIONI_I.INDIRIZZO, MI_LOCAZIONI_A.INDIRIZZO)");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/Locazioni?listavie='+document.getElementById('INDIRIZZO').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		

		// chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "locazioni/locazioniFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new LocazioniFinder().getClass())
			{
				finder = (LocazioniFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (LocazioniFinder) gestireMultiPagina(finder, request);

		LocazioniLogic logic = new LocazioniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaLocazioni(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_locazioni = (Vector) ht.get(LocazioniLogic.LISTA_LOCAZIONI);
		finder = (LocazioniFinder) ht.get("FINDER");
		sessione.setAttribute(LocazioniLogic.LISTA_LOCAZIONI, vct_lista_locazioni);
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
		this.chiamaPagina(request, response, "locazioni/locazioniFrame.jsp", nav);
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
		
		LocazioniFinder finder = null;
		// boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new LocazioniFinder().getClass())
			{
				finder = (LocazioniFinder) sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, "LISTA_LOCAZIONI", (Vector) sessione.getAttribute("LISTA_LOCAZIONI"), NOMEFINDER);
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
		
		if (tipo == 101 || tipo == 102 || tipo == 103 || tipo == 104 || tipo == 105){
			 String[] chiaviArr= oggettoSel.split("\\|",-1);
			 String idLocazione="";
			 
			 if (chiaviArr.length == 6){
			 for (int i=0; i< chiaviArr.length-2; i++){
				 idLocazione=idLocazione+ chiaviArr[i];
				 if (i != chiaviArr.length-3 )
					 idLocazione= idLocazione + "|";
					 
			 	}
			 }else if (chiaviArr.length == 7){
				 for (int i=0; i< chiaviArr.length-3; i++){
					 idLocazione=idLocazione+ chiaviArr[i];
					 if (i != chiaviArr.length-4 )
						 idLocazione= idLocazione + "|";
				 }
			 }
		 oggettoSel=idLocazione;
		 
		 log.debug("OggettoSelezionato: " + oggettoSel);
		}

		/*
		 * carica il dettaglio
		 */
		LocazioniLogic logic = new LocazioniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioLocazioni(oggettoSel);
		Locazioni ana = (Locazioni) ht.get(LocazioniLogic.LOCAZIONI);
		sessione.setAttribute(LocazioniLogic.LOCAZIONI, ana);

		ArrayList listaDettaglioLocazioni = (ArrayList) ht.get(LocazioniLogic.LISTA_DETTAGLIO_LOCAZIONI);
		sessione.setAttribute(LocazioniLogic.LISTA_DETTAGLIO_LOCAZIONI, listaDettaglioLocazioni);
		
		ArrayList listaDettaglioLocazioniDatiUiu = (ArrayList) ht.get(LocazioniLogic.LISTA_DETTAGLIO_LOCAZIONI_DATI_UIU);
		sessione.setAttribute(LocazioniLogic.LISTA_DETTAGLIO_LOCAZIONI_DATI_UIU, listaDettaglioLocazioniDatiUiu);
		
		ArrayList listaImmobili = (ArrayList) ht.get(LocazioniLogic.LISTA_DETTAGLIO_IMMOBILI);
		sessione.setAttribute(LocazioniLogic.LISTA_DETTAGLIO_IMMOBILI, listaImmobili);
		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		
		// Aggiungo i valori per l'indice di correlazione
		Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> cOggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieOggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> oOggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fOggettiInd = new Vector<OggettoIndice>();
		
		ArrayList listaNomiSoggetti= new ArrayList();
		ArrayList listaNomiVie= new ArrayList();
		ArrayList listaNomiCivici= new ArrayList();
		ArrayList listaNomiOggetti= new ArrayList();
		ArrayList listaNomiFabbricati= new ArrayList();
		
		for (int i=0; i< listaDettaglioLocazioni.size(); i++){
			
			
			OggettoIndice oi = new OggettoIndice();
			
			Locazioni loca= (Locazioni)listaDettaglioLocazioni.get(i);
			
			oi.setIdOrig(loca.getChiave()+ "|"+ (loca.getSottonumero()!= null ? loca.getSottonumero() :"") + "|"+ (loca.getProgNegozio()!= null ? loca.getProgNegozio() : "")+ "|"+ loca.getProgSoggetto());
			oi.setFonte("5");
			oi.setProgr("2"); //1
			
			
			if (loca.getCodFisc() != null)
				oi.setDescrizione(loca.getCodFisc());
			String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
			if (!listaNomiSoggetti.contains(nomeSoggetto)){
				listaNomiSoggetti.add(nomeSoggetto);
				sOggettiInd.add(oi);
			}
			
			
			
			log.debug("-----");
			OggettoIndice oi1 = new OggettoIndice();
			
			oi1.setIdOrig(loca.getChiave()+ "|"+ (loca.getSottonumero()!= null ? loca.getSottonumero() :"") + "|"+ (loca.getProgNegozio()!= null ? loca.getProgNegozio() : "")+ "|"+ loca.getProgSoggetto());
			oi1.setFonte("5");
			oi1.setProgr("2");
			String descr = loca.getIndirizzoResidenza() ;
			if (descr!=null && loca.getCivicoResidenza()!= null && !loca.getCivicoResidenza().equals(""))
				descr= descr+ ", " + loca.getCivicoResidenza();
			oi1.setDescrizione(descr);		
			String nomeCivico= oi1.getFonte()+oi1.getProgr()+oi1.getIdOrig();
			if (!listaNomiCivici.contains(nomeCivico) && descr!=null){
				listaNomiCivici.add(nomeCivico);
				cOggettiInd.add(oi1);
			}
			
			
			OggettoIndice oi2 = new OggettoIndice();
			
			oi2.setIdOrig(loca.getChiave()+ "|"+ (loca.getSottonumero()!= null ? loca.getSottonumero() :"") + "|"+ (loca.getProgNegozio()!= null ? loca.getProgNegozio() : "")+ "|"+ loca.getProgSoggetto());
			oi2.setFonte("5");
			oi2.setProgr("2");
			descr = loca.getIndirizzoResidenza() ;
			if (descr!=null && loca.getCivicoResidenza()!= null && !loca.getCivicoResidenza().equals(""))
				descr= descr+ ", " + loca.getCivicoResidenza();
			oi2.setDescrizione(descr);
			String nomeVia= oi2.getFonte()+oi2.getProgr()+oi2.getIdOrig();
			if (!listaNomiVie.contains(nomeVia) && descr!=null){
				listaNomiVie.add(nomeVia);
				vieOggettiInd.add(oi2);
			}
			
		
		}
		
		for (int i=0; i< listaImmobili.size(); i++){
			LocazioniImmobili loca= (LocazioniImmobili)listaImmobili.get(i);
		    String idDwh = loca.getChiave()+ "|"+ (loca.getSottonumero()!= null ? loca.getSottonumero() :"") + "|"+ (loca.getProgNegozio()!= null ? loca.getProgNegozio() : "")+ "|"+ loca.getProgImmobile();
					
			
			OggettoIndice oi = new OggettoIndice();
					
			oi.setIdOrig(idDwh);
			oi.setFonte("5");
			oi.setProgr("3"); //1
					
			if (loca.getParticella() != null){
				oi.setDescrizione("F:" + (loca.getFoglio()!=null ? loca.getFoglio() : "") + " P:" + loca.getParticella() + " S:" + (loca.getSubalterno()!=null ? loca.getSubalterno() : ""));
				String nomeOggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
				if (!listaNomiOggetti.contains(nomeOggetto)){
					listaNomiOggetti.add(nomeOggetto);
					oOggettiInd.add(oi);
				}
				if (!listaNomiFabbricati.contains(nomeOggetto)){
					listaNomiFabbricati.add(nomeOggetto);
					fOggettiInd.add(oi);
				}
			}
					
			OggettoIndice oiC = new OggettoIndice();
			
			oiC.setIdOrig(idDwh);
			oiC.setFonte("5");
			oiC.setProgr("3"); //1
					
			//Caricamento indirizzi immobile
			String descr = loca.getIndirizzo();
			
			oiC.setDescrizione(descr);		
			String nomeCivico= oiC.getFonte()+oiC.getProgr()+oiC.getIdOrig();
			if (!listaNomiCivici.contains(nomeCivico) && descr!=null){
				listaNomiCivici.add(nomeCivico);
				cOggettiInd.add(oiC);
			}
					
			OggettoIndice oiV = new OggettoIndice();
			
			oiV.setIdOrig(idDwh);
			oiV.setFonte("5");
			oiV.setProgr("3"); //1		
					
			oiV.setDescrizione(descr);
			String nomeVia= oiV.getFonte()+oiV.getProgr()+oiV.getIdOrig();
			if (!listaNomiVie.contains(nomeVia) && descr!=null){
				listaNomiVie.add(nomeVia);
				vieOggettiInd.add(oiV);
			}
					
					
		}
		
		OggettoIndice oi2 = new OggettoIndice();
		oi2.setDescrizione(ana.getIndirizzo());
		oi2.setFonte("5");
		oi2.setProgr("1");
		oi2.setIdOrig(ana.getUfficio() + "|" + ana.getAnno() + "|" + ana.getSerie() + "|" + ana.getNumero()+ "|"+ (ana.getSottonumero()!= null ? ana.getSottonumero() :"") + "|"+ (ana.getProgNegozio()!= null ? ana.getProgNegozio() : ""));
		
		String nomeCivico= oi2.getFonte()+oi2.getProgr()+oi2.getIdOrig();
		if (!listaNomiCivici.contains(nomeCivico) && ana.getIndirizzo()!=null){
			listaNomiCivici.add(nomeCivico);
			cOggettiInd.add(oi2);
		}

		
		OggettoIndice oi3 = new OggettoIndice();
		oi3.setDescrizione(ana.getIndirizzo());
		oi3.setFonte("5");
		oi3.setProgr("1");
		oi3.setIdOrig(ana.getUfficio() + "|" + ana.getAnno() + "|" + ana.getSerie() + "|" + ana.getNumero()+ "|"+ (ana.getSottonumero()!= null ? ana.getSottonumero() :"") + "|"+ (ana.getProgNegozio()!= null ? ana.getProgNegozio() : ""));

		String nomeVia= oi3.getFonte()+oi3.getProgr()+oi3.getIdOrig();
		if (!listaNomiVie.contains(nomeVia) && ana.getIndirizzo() !=null){
			listaNomiVie.add(nomeVia);
			vieOggettiInd.add(oi3);
		}
		
		sessione.setAttribute("indice_soggetti", sOggettiInd);
		sessione.setAttribute("indice_civici", cOggettiInd);
		sessione.setAttribute("indice_vie", vieOggettiInd);
		sessione.setAttribute("indice_oggetti", oOggettiInd);
		sessione.setAttribute("indice_fabbricati", fOggettiInd);
		
		this.chiamaPagina(request, response, "locazioni/locazioniFrame.jsp", nav);

		// EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);

	}
	
	
	
	private void mCaricareDettaglioPopupSoggetto(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession sessione = request.getSession();
		String codFiscaleDic = (request.getParameter("CODICE_FISCALE_DIC"));
		int annoImposta = Integer.parseInt(request.getParameter("ANNO_IMPOSTA"));
		LocazioniLogic logic = new LocazioniLogic(this.getEnvUtente(request));
		ArrayList listaDettaglioLocazioniSoggetto = logic.mCaricareDettaglioLocazioniSoggetto(codFiscaleDic, annoImposta);
		sessione.setAttribute(LocazioniLogic.LISTA_DETTAGLIO_LOCAZIONI_SOGGETTO, listaDettaglioLocazioniSoggetto);
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request, response, "locazioni/locazioniPopupSoggetto.jsp", nav);

	}
	
	private void mListaVie(HttpServletRequest request, HttpServletResponse response) throws IOException	{		
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setHeader("Expires", "Thu, 01 Jan 1970 01:00:00 CET");
		
		String param = request.getParameter("listavie");
		
		if (param != null && param.trim().equals("")) {
			response.getWriter().println("Nessun criterio di ricerca impostato");
			return;
		}
		
		LocazioniLogic logic = new LocazioniLogic(this.getEnvUtente(request));
		List<String> l = logic.cercaIndirizzi(param);
		if (l != null && l.size() > 0) {
			StringBuffer s = new StringBuffer();
			s.append("<select name=\"listaNomiVie\" class=\"INPDBComboBox\" id=\"listaNomiVie\" onchange=\"document.getElementById('INDIRIZZO').value=this.options[this.selectedIndex].value;document.getElementById('divListaVie').innerHTML='';\" > \n");
			Iterator<String> i = l.iterator();
			s.append("<option value=\"\" >Seleziona</option>");
			while(i.hasNext()) {
				String value = i.next();
				s.append("<option value=\""+value+"\">"+value+"</option> \n");
			}
			s.append("</select> \n");
			response.getWriter().print(s.toString());
		} else {
			response.getWriter().println("Nessun indirizzo trovato");
		}
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
	{
		finder = (LocazioniFinder) finder2;
		LocazioniLogic logic = new LocazioniLogic(this.getEnvUtente(request));
		return logic.mCaricareListaLocazioni(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder()
	{
		return new LocazioniFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((Locazioni) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Locazioni";
	}

	public String getTabellaPerCrossLink()
	{
		return "MI_LOCAZIONI_A";
	}

}
