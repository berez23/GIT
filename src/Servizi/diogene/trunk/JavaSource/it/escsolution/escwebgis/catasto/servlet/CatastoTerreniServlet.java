/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.servlet;

import it.escsolution.escwebgis.catasto.bean.TerreniFinder;
import it.escsolution.escwebgis.catasto.bean.Terreno;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoIntestatariFLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoIntestatariGLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoTerreniLogic;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.pregeo.bean.PregeoInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//S
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CatastoTerreniServlet extends EscServlet implements EscService{

	private String recordScelto;
	private CatastoTerreniLogic logic = null;
	private TerreniFinder finder = null;
	boolean soloAtt = false;
	private final String NOMEFINDER = "FINDER2";
	
	PulsantiNavigazione nav = new PulsantiNavigazione();
	
	public void EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalit diversa
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
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {

		/*
		 * ad ogni uc corrisponde una funzionalit diversa
		 *
		 */
		 super.EseguiServizio(request,response);

		 String pathDatiDiogene = super.getPathDatiDiogene();

		 try{
				if(request.getParameter("popupCatasto") != null)
					mPopupCatasto(request, response);				
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
						mCaricareDettaglio(request,response,false);
						//this.leggiCrossLink(request);
						break;
					case 33:
						// ho cliccato su un bottone visualizza dettaglio --> visualizzo il dettaglio su una PopUp
						mCaricareDettaglio(request,response,false);
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
	
	public void EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
				case 102:
					// carico la form di ricerca
					pulireSessione(request);
					mCaricareDettaglioFromVia(request,response, false);
					break;
				case 103:
					// carico la form di ricerca
					pulireSessione(request);
					//mCaricareLista(request,response, 103);
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
	
	
	private void mPopupCatasto(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{

		String f = request.getParameter("f");
		String p = request.getParameter("p");
		String cod_ente = request.getParameter("cod_ente");

		if(
				(f == null || f.trim().equals("")) || 
				(p == null || p.trim().equals("")) || 
				(cod_ente == null || cod_ente.trim().equals("")) 
		  )
			throw new Exception("dati mancanti");
		
		String key = cod_ente+"|"+f+"|"+p;
		
		CatastoTerreniLogic logic = new CatastoTerreniLogic(this.getEnvUtente(request));
		Vector listaPar = new Vector();
		
		
		Vector operatori = new Vector();
		operatori.add(new EscOperatoreFiltro("=","uguale"));


		
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setValore(f);
		elementoFiltro.setOperatore("=");
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatori);
		elementoFiltro.setCampoFiltrato("sititrkc.FOGLIO");
		listaPar.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setValore(p);
		elementoFiltro.setOperatore("=");
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("lpad5_0");
		elementoFiltro.setListaOperatori(operatori);
		elementoFiltro.setCampoFiltrato("sititrkc.PARTICELLA");
		listaPar.add(elementoFiltro);
		
		vettoreRicerca = listaPar;
		mCaricareLista(request, response);
	}		
	
	
	private void mCaricareFormRicerca(HttpServletRequest request,HttpServletResponse response) throws Exception{
		// caricare dati che servono nella pagina di ricerca --> eventuali combo
		CatastoTerreniLogic logic = new CatastoTerreniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDatiFormRicerca();
		Vector vct = (Vector) ht.get("LISTA_QUALITA");
		Vector vct_cla = (Vector) ht.get("LISTA_CLASSE");
		/** Comune */
		Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
		/***/

		// metto sulla request la lista comuni
		request.setAttribute("LISTA_QUALITA",vct);
		request.setAttribute("LISTA_CLASSE",vct_cla);


		HttpSession sessione = request.getSession();
		sessione.setAttribute("LISTA_QUALITA",vct);
		sessione.setAttribute("LISTA_CLASSE",vct_cla);


		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));


		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("Comune");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setListaValori(vctComuni);
		elementoFiltro.setCampoFiltrato("SITICOMU.CODI_FISC_LUNA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("sititrkc.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("lpad5_0");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("sititrkc.PARTICELLA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno (Es.001)");
		elementoFiltro.setAttributeName("SUBALTERNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("sititrkc.SUB");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Partita");
		elementoFiltro.setAttributeName("PARTITA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("sititrkc.PARTITA");
		listaElementiFiltro.add(elementoFiltro);


		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("CLASSE");
		elementoFiltro.setAttributeName("CLASSE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("sititrkc.CLASSE_TERRENO");
		elementoFiltro.setListaValori(vct_cla);
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Qualita'");
		elementoFiltro.setAttributeName("QUALITA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("sititrkc.QUAL_CAT");
		elementoFiltro.setListaValori(vct);
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
		this.chiamaPagina(request,response,"catasto/cteFrame.jsp",nav);
	}
	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (TerreniFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;//new TerreniFinder();
		}

		finder = (TerreniFinder)gestireMultiPagina(finder,request);


		CatastoTerreniLogic logic = new CatastoTerreniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaTerreni2(vettoreRicerca,finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_terreni = (Vector)ht.get(CatastoTerreniLogic.LISTA_TERRENI);
		finder = (TerreniFinder)ht.get("FINDER");
		soloAtt = (Boolean)ht.get(CatastoTerreniLogic.SOLO_ATT);
		
		sessione.setAttribute(CatastoTerreniLogic.LISTA_TERRENI,vct_lista_terreni);

		sessione.setAttribute(NOMEFINDER,finder);
		sessione.setAttribute(CatastoTerreniLogic.SOLO_ATT,soloAtt);
		

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		this.chiamaPagina(request,response,"catasto/cteFrame.jsp",nav);

	}
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		String azione = "";
		HttpSession sessione = request.getSession();
		removeOggettiIndiceDaSessione(sessione);
		 
		TerreniFinder finder = null;

		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new TerreniFinder().getClass()){
				finder = (TerreniFinder)sessione.getAttribute(NOMEFINDER);
			}
			else{
				// il finder non corrisponde -->

				finder = null;

			}
		}
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");

		gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_TERRENI",(Vector)sessione.getAttribute("LISTA_TERRENI"),NOMEFINDER);

		if (!azione.equals("")){
			Vector listaTerreni = (Vector)sessione.getAttribute("LISTA_TERRENI");
		}
		else{
			oggettoSel = "";recordScelto = "";
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
		/*
		 * carica il dettaglio della particella
		 */
		CatastoTerreniLogic logic = new CatastoTerreniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioTerreno(oggettoSel, pathDatiDiogene);
		
		Terreno ter = (Terreno)ht.get("TERRENO");
		
		mCaricareIntestatari(request,response,ter.getParticella(),notListaPrincipale);
		
		ArrayList<PregeoInfo> alPregeo = (ArrayList<PregeoInfo>)ht.get("PREGEO");
	
		
		removeOggettiIndiceDaSessione(sessione);
	
		/*
		 * carica il dettaglio della particella
		 */
	
		sessione.setAttribute("TERRENO",ter);
		sessione.setAttribute("PREGEO",alPregeo);
		sessione.setAttribute(CatastoTerreniLogic.LISTA_TERR_DERIVATI, ht.get(CatastoTerreniLogic.LISTA_TERR_DERIVATI));
		sessione.setAttribute(CatastoTerreniLogic.LISTA_TERR_GENERATORI, ht.get(CatastoTerreniLogic.LISTA_TERR_GENERATORI));
		sessione.setAttribute(CatastoTerreniLogic.LISTA_TERR_STORICO, ht.get(CatastoTerreniLogic.LISTA_TERR_STORICO));
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"catasto/cteFrame.jsp",nav);


	}
	
	private void mCaricareDettaglioFromVia(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		String azione = "";
		HttpSession sessione = request.getSession();
		
		String idVia= request.getParameter("OGGETTO_SEL");
		removeOggettiIndiceDaSessione(sessione);
	
		/*
		 * carica il dettaglio della particella
		 */
		CatastoTerreniLogic logic = new CatastoTerreniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioTerreno(idVia,pathDatiDiogene);
		
		Terreno ter = (Terreno)ht.get("TERRENO");
		
		mCaricareIntestatari(request,response,ter.getParticella(),notListaPrincipale);
		
		ArrayList<PregeoInfo> alPregeo = (ArrayList<PregeoInfo>)ht.get("PREGEO");
		sessione.setAttribute("TERRENO",ter);
		sessione.setAttribute("PREGEO",alPregeo);
		sessione.setAttribute(CatastoTerreniLogic.LISTA_TERR_DERIVATI, ht.get(CatastoTerreniLogic.LISTA_TERR_DERIVATI));
		sessione.setAttribute(CatastoTerreniLogic.LISTA_TERR_GENERATORI, ht.get(CatastoTerreniLogic.LISTA_TERR_GENERATORI));
		sessione.setAttribute(CatastoTerreniLogic.LISTA_TERR_STORICO, ht.get(CatastoTerreniLogic.LISTA_TERR_STORICO));
		
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"catasto/cteFrame.jsp",nav);

	}
	
	private void mCaricareIntestatari(HttpServletRequest request,HttpServletResponse response, String particella, boolean notListaPrincipale) throws Exception{
		/*
		 * carico gli intestatari (persone fisiche e giuridiche)
		 */
		CatastoIntestatariFLogic logicF =new CatastoIntestatariFLogic(this.getEnvUtente(request));
		Vector listaIntF = (Vector)((Hashtable)logicF.mCaricareListaIntestatariPerTerreno(particella)).get("LISTA_INTESTATARIF");

		CatastoIntestatariGLogic logicG =new CatastoIntestatariGLogic(this.getEnvUtente(request));
		Vector listaIntG = (Vector)((Hashtable)logicG.mCaricareListaIntestatariPerTerreno(particella)).get("LISTA_INTESTATARIG");

		HttpSession sessione = request.getSession();
		if (notListaPrincipale){
			sessione.setAttribute("LISTA_INTESTATARIF2",listaIntF);
			sessione.setAttribute("LISTA_INTESTATARIG2",listaIntG);
		}
		else{
			sessione.setAttribute("LISTA_INTESTATARIF",listaIntF);
			sessione.setAttribute("LISTA_INTESTATARIG",listaIntG);
		}
	}
	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((Terreno)listaOggetti.get(recordSuccessivo)).getParticella();
	}
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (TerreniFinder)finder2;
		CatastoTerreniLogic logic = new CatastoTerreniLogic(this.getEnvUtente(request));
		return logic.mCaricareListaTerreni2(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder(){
		return new TerreniFinder();
	}
	public String getTema() {
		return "Catasto";
	}
	public String getTabellaPerCrossLink() {
		return "CAT_CT_PARTICELLE";
	}
}
