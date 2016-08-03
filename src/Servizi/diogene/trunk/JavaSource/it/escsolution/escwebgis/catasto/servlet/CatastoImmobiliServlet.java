/*
 * Created on 8-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.servlet;

import it.escsolution.escwebgis.catasto.bean.Immobile;
import it.escsolution.escwebgis.catasto.bean.ImmobiliFinder;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoIntestatariFLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoIntestatariGLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoPlanimetrieComma340Logic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.toponomastica.bean.Civico;
import it.escsolution.escwebgis.tributiNew.logic.TributiOggettiICINewLogic;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
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
public class CatastoImmobiliServlet extends EscServlet implements EscService{
	/*
	 * intestazione classe
	 */
	
	String pathPlanimetrieComma340 = "";

	private CatastoImmobiliLogic logic = null;
	private ImmobiliFinder finder = null;
	private final String NOMEFINDER = "FINDER1";
	PulsantiNavigazione nav = new PulsantiNavigazione();
	
	String pathDatiDiogene = "";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pathDatiDiogene = super.getPathDatiDiogene();
    }
    
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
		
	}//-------------------------------------------------------------------------

	public void _EseguiServizio(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {

		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 *
		 */
		super.EseguiServizio(request,response);
		try{
			if (request.getParameter("popup3DProspective") != null)
				mPopup3DProspective(request, response);	
			else			
		switch (st){
			case 1:
				// carico la form di ricerca
				pulireSessione(request);
				mCaricareFormRicerca(request,response);
				break;
			case 2:
				// vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
				mCaricareLista(request,response,2);
				break;
			case 3:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response,false,3);
				//this.leggiCrossLink(request);
				break;
			case 33:
				// ho cliccato su un bottone visualizza dettaglio --> visualizzo il dettaglio su una PopUp
				mCaricareDettaglio(request,response,false,33);
				//this.leggiCrossLink(request);
				break;
			/*case 4:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response,true);
				break;*/
		}
		}
		 catch(it.escsolution.escwebgis.common.DiogeneException de)
		 {
		 	throw de;
		 }
		catch(Exception ex){
			log.error(ex.getMessage(),ex);
		}

	}//-------------------------------------------------------------------------
	
	public void _EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
					case 102:
						mCaricareLista(request,response,102);							 
						break;
					case 103:
						mCaricareLista(request,response,103);							 
						break;
					case 104:
						mCaricareDettaglio(request,response,false,104);			
						break;
					case 105:
						mCaricareDettaglio(request,response,false,105);			
						break;
					case 22:
						// apro lista da altra pagina (es.topografia, passando: OGGETTO_SEL=foglio|particella 
						mCaricareLista(request,response,22);
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

	private void mCaricareFormRicerca(HttpServletRequest request,HttpServletResponse response) throws Exception{
		// caricare dati che servono nella pagina di ricerca --> eventuali combo
		CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDatiFormRicerca(request.getUserPrincipal().getName());
		Vector vct_comuni = (Vector) ht.get("LISTA_COMUNI");
		Vector vct_catego = (Vector) ht.get("LISTA_CATEGORIE");

		// metto sulla request la lista comuni

		request.setAttribute("LISTA_CATEGORIE",vct_catego);

		HttpSession sessione = request.getSession();

		sessione.setAttribute("LISTA_CATEGORIE",vct_catego);

		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));


		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		//costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("Comune");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setListaValori(vct_comuni);
		elementoFiltro.setCampoFiltrato("SiTICOMU.CODI_FISC_LUNA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("SITIUIU.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("lpad5_0");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("SITIUIU.PARTICELLA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("SUBALTERNO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("SITIUIU.UNIMM");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Destinaz.");
		elementoFiltro.setAttributeName("CATEGORIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("SITIUIU.CATEGORIA");
		elementoFiltro.setListaValori(vct_catego);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Annotazioni");
		elementoFiltro.setAttributeName("ANNOTAZIONI");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SITIUIU.ANNOTAZIONI");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Solo attuali");
		elementoFiltro.setAttributeName("SOLO_ATT");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(null);
		elementoFiltro.setCheckBox(true);
		elementoFiltro.setCampoFiltrato("SOLO_ATT");
		listaElementiFiltro.add(elementoFiltro);
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"catasto/cimFrame.jsp",nav);
	}//-------------------------------------------------------------------------
	
	
	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response, int tipo)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		String paginaAttuale = "";
		if (request.getParameter("ACT_PAGE")!= null)
			paginaAttuale = request.getParameter("ACT_PAGE");
		String azione = "";
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (ImmobiliFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;//new ImmobiliFinder();
		}
		finder = (ImmobiliFinder)gestireMultiPagina(finder,request);
		
		if (request.getParameter("OGGETTO_SEL")!= null){
			oggettoSel = request.getParameter("OGGETTO_SEL");
		}

		CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
		
		Hashtable ht = null;
		if (tipo == 102|| tipo== 103)
			ht = logic.mCaricareListaImmobiliFromVia(oggettoSel,finder);
		else{
			
			if(tipo==22 && oggettoSel!=null){
				
				vettoreRicerca = new Vector();
				
				String[] s = oggettoSel.split("\\|");
				
				EscElementoFiltro elementoFiltro = new EscElementoFiltro();
				
				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Foglio");
				elementoFiltro.setAttributeName("FOGLIO");
				elementoFiltro.setTipo("N");
				elementoFiltro.setCampoJS("numeroIntero");
				elementoFiltro.setValore(s[0]);
				elementoFiltro.setOperatore("=");
				elementoFiltro.setCampoFiltrato("SITIUIU.FOGLIO");
				vettoreRicerca.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Particella");
				elementoFiltro.setAttributeName("PARTICELLA");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("lpad5_0");
				elementoFiltro.setValore(s[1]);
				elementoFiltro.setOperatore("=");
				elementoFiltro.setCampoFiltrato("SITIUIU.PARTICELLA");
				vettoreRicerca.add(elementoFiltro);
			}
			
			ht = logic.mCaricareListaImmobili2(vettoreRicerca,finder);
		}
		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_immobili = (Vector)ht.get("LISTA_IMMOBILI");
		finder = (ImmobiliFinder)ht.get("FINDER");
		
		Boolean soloAtt = (Boolean)ht.get(CatastoImmobiliLogic.SOLO_ATT);
		
		sessione.setAttribute("LISTA_IMMOBILI",vct_lista_immobili);
		sessione.setAttribute(NOMEFINDER,finder);
		sessione.setAttribute(CatastoImmobiliLogic.SOLO_ATT, soloAtt);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"catasto/cimFrame.jsp",nav);
	}//-------------------------------------------------------------------------
	
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale, int tipo)throws Exception{
		// prendere dalla request il parametrio identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		ImmobiliFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ImmobiliFinder().getClass()){
				finder = (ImmobiliFinder)sessione.getAttribute(NOMEFINDER);
			}
			else{
				// il finder non corrisponde -->
				finder = null;
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");

		gestioneMultiRecord(request, response, azione,finder,sessione,"LISTA_IMMOBILI",(Vector)sessione.getAttribute("LISTA_IMMOBILI"),NOMEFINDER);
		if (azione.equals("")){
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
		String oggettoSelGraf = request.getParameter("OGGETTO_SEL_GRAF") == null ? "" : request.getParameter("OGGETTO_SEL_GRAF");

		/*
		 * carica il dettaglio della particella
		 */
		
		CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		 if (tipo == 104 || tipo == 105)
			ht = logic.mCaricareDettaglioImmobiliFromId(oggettoSel,this.pathDatiDiogene);
		 else
			 ht=logic.mCaricareDettaglioImmobile(oggettoSel, oggettoSelGraf, this.pathDatiDiogene);
		 
		Immobile imm = (Immobile) ht.get(CatastoImmobiliLogic.UNIMM);
		 
		mCaricareIntestatari(request,response,imm.getChiave(),notListaPrincipale);
			 
		sessione.setAttribute("PREGEO", ht.get("PREGEO"));
		sessione.setAttribute(CatastoImmobiliLogic.UNIMM, ht.get(CatastoImmobiliLogic.UNIMM));
		sessione.setAttribute(CatastoImmobiliLogic.LISTA_CIVICI, ht.get(CatastoImmobiliLogic.LISTA_CIVICI));
		sessione.setAttribute(CatastoImmobiliLogic.LISTA_CIVICI_CAT, ht.get(CatastoImmobiliLogic.LISTA_CIVICI_CAT));
		sessione.setAttribute(CatastoImmobiliLogic.DOCFA_COLLEGATI, ht.get(CatastoImmobiliLogic.DOCFA_COLLEGATI));
		sessione.setAttribute(CatastoImmobiliLogic.DETTAGLIO_VANI_340, ht.get(CatastoImmobiliLogic.DETTAGLIO_VANI_340));
		sessione.setAttribute(CatastoImmobiliLogic.LISTA_UIU_DERIVATE, ht.get(CatastoImmobiliLogic.LISTA_UIU_DERIVATE));
		sessione.setAttribute(CatastoImmobiliLogic.LISTA_UIU_DERIVANTE, ht.get(CatastoImmobiliLogic.LISTA_UIU_DERIVANTE));
		sessione.setAttribute(CatastoImmobiliLogic.LISTA_UIU_STORICO, ht.get(CatastoImmobiliLogic.LISTA_UIU_STORICO));
		sessione.setAttribute(CatastoImmobiliLogic.ALBERO_IMMOBILI_DERIVATI, ht.get(CatastoImmobiliLogic.ALBERO_IMMOBILI_DERIVATI));
	
		sessione.setAttribute(CatastoImmobiliLogic.LISTA_UIU_GRAFFATI, ht.get(CatastoImmobiliLogic.LISTA_UIU_GRAFFATI));
		sessione.setAttribute(CatastoImmobiliLogic.ULTIMA_DICH_TARSU, ht.get(CatastoImmobiliLogic.ULTIMA_DICH_TARSU));
		
		CatastoPlanimetrieComma340Logic logic340 = new CatastoPlanimetrieComma340Logic(this.getEnvUtente(request));
		ArrayList<ArrayList<String>> planimetrieComma340 = logic340.getPlanimetrieComma340(imm.getFoglio(), imm.getNumero(), imm.getUnimm(), pathDatiDiogene);
		sessione.setAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_CIM, planimetrieComma340);

		ArrayList listaCiviciCat = (ArrayList) sessione.getAttribute(CatastoImmobiliLogic.LISTA_CIVICI_CAT);
		
		
		ArrayList listaNomiVie= new ArrayList();
	
		
		if (listaCiviciCat != null && listaCiviciCat.size()>0){
			
			Vector<OggettoIndice> vieOggettiInd = new Vector<OggettoIndice>();
			Vector<OggettoIndice> civiciOggettiInd = new Vector<OggettoIndice>();
			
			for (int i=0; i<listaCiviciCat.size(); i++){
				Civico civ= (Civico) listaCiviciCat.get(i);
				
				OggettoIndice oi = new OggettoIndice();
				
				oi.setIdOrig(civ.getIdImm()+ "|"+ civ.getProgressivo()+ "|"+ civ.getSeq()+ "|"+ civ.getCodiFiscLuna()+ "|"+ civ.getSezione());
				oi.setFonte("4");
				oi.setProgr("4");
				if (civ.getNumero()!= null )
					oi.setDescrizione(civ.getStrada()+ " " + civ.getNumero());
				else
					oi.setDescrizione(civ.getStrada());
				
				String nomeVia= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
				if (!listaNomiVie.contains(nomeVia)){
					listaNomiVie.add(nomeVia);
					vieOggettiInd.add(oi);
					civiciOggettiInd.add(oi);
				}
					
			}
			
			sessione.setAttribute("indice_vie", vieOggettiInd);
			sessione.setAttribute("indice_civici", civiciOggettiInd);
		}
		
		Vector<OggettoIndice> oggettiOggettiInd = new Vector<OggettoIndice>();
		
		OggettoIndice oi = new OggettoIndice();
		oi.setIdOrig(imm.getPkId());
		oi.setFonte("4");
		oi.setProgr("1");
		String particella= imm.getParticella();
		String sub= imm.getSubalterno();
		if (particella == null || particella.equals(""))
			particella= imm.getNumero();
		if (sub == null || sub.trim().equals(""))
			sub= imm.getUnimm();
		
		oi.setDescrizione("F:" + imm.getFoglio() + " P:" + particella + " S:" + sub);
		
		oggettiOggettiInd.add(oi);
		
		
		sessione.setAttribute("indice_oggetti", oggettiOggettiInd);
		
		
		Vector<OggettoIndice> oggettiFabbInd = new Vector<OggettoIndice>();
		
		OggettoIndice oi1 = new OggettoIndice();
		oi1.setIdOrig(imm.getPkId());
		oi1.setFonte("4");
		oi1.setProgr("1");
		String particella1= imm.getParticella();
		
		if (particella1 == null || particella1.equals(""))
			particella1= imm.getNumero();
		
		
		oi1.setDescrizione("SEZ:"+imm.getSezione()+" F:" + imm.getFoglio() + " P:" + particella );
		
		oggettiFabbInd.add(oi1);
		
		
		sessione.setAttribute("indice_fabbricati", oggettiFabbInd);
		
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"catasto/cimFrame.jsp",nav);
	}

	private void mCaricareIntestatari(HttpServletRequest request,HttpServletResponse response, String chiave, boolean notListaPrincipale) throws Exception{
		/*
		 * carico gli intestatari (persone fisiche e giuridiche)
		 */
		long startTime = new Date().getTime();
		
		HttpSession sessione = request.getSession();

		CatastoIntestatariFLogic logicF =new CatastoIntestatariFLogic(this.getEnvUtente(request));
		Vector listaIntF = (Vector)((Hashtable)logicF.mCaricareListaIntestatariPerFabbricato(chiave)).get("LISTA_INTESTATARIF");

		CatastoIntestatariGLogic logicG =new CatastoIntestatariGLogic(this.getEnvUtente(request));
		Vector listaIntG = (Vector)((Hashtable)logicG.mCaricareListaIntestatariPerFabbricato(chiave)).get("LISTA_INTESTATARIG");
		if (notListaPrincipale){
			sessione.setAttribute("LISTA_INTESTATARIF2",listaIntF);
			sessione.setAttribute("LISTA_INTESTATARIG2",listaIntG);
		}
		else{
			sessione.setAttribute("LISTA_INTESTATARIF",listaIntF);
			sessione.setAttribute("LISTA_INTESTATARIG",listaIntG);
		}
		
		log.debug("DURATA TOTALE LETTURA INTESTATARI MS " + (new Date().getTime() - startTime));

	}


public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
	return ((Immobile)listaOggetti.get(recordSuccessivo)).getChiave();
}
public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
	finder = (ImmobiliFinder)finder2;
	CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
	return logic.mCaricareListaImmobili2(this.vettoreRicerca, finder);
}
public EscFinder getNewFinder(){
	return new ImmobiliFinder();
}
public String getTema() {
	return "Catasto";
}
public String getTabellaPerCrossLink() {
	return "UIU_VISTA";
}
}
