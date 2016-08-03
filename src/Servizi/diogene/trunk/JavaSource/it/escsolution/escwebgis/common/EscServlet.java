/*
 * Created on 7-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common;

import it.escsolution.eiv.database.Temi;
import it.escsolution.escwebgis.acqua.logic.AcquaLogic;
import it.escsolution.escwebgis.acquedotto.logic.AcquedottoAcquedottoLogic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeAnagrafeLogic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeStorico2005Logic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeStoricoAnagrafeLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoTerreniLogic;
import it.escsolution.escwebgis.catasto.logic.VisuraNazionaleLogic;
import it.escsolution.escwebgis.cncici.logic.CncIciLogic;
import it.escsolution.escwebgis.common.interfacce.Interfaccia;
import it.escsolution.escwebgis.concessioni.logic.ConcessioniINFORMLogic;
import it.escsolution.escwebgis.concessioni.logic.ConcessioniLogic;
import it.escsolution.escwebgis.concessioni.logic.ConcessioniVisureLogic;
import it.escsolution.escwebgis.concessioni.logic.FornituraDiaLogic;
import it.escsolution.escwebgis.condono.logic.CondonoLogic;
import it.escsolution.escwebgis.cosap.logic.CosapLogic;
import it.escsolution.escwebgis.demanio.logic.DemanioLogic;
import it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheDocfaLogic;
import it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheDocfaNoResLogic;
import it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheLogic;
import it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheTarsuLogic;
import it.escsolution.escwebgis.docfa.logic.DocfaLogic;
import it.escsolution.escwebgis.ecog.logic.EcograficoCiviciLogic;
import it.escsolution.escwebgis.ecog.logic.EcograficoStradeLogic;
import it.escsolution.escwebgis.esatri.logic.EsatriContribuentiLogic;
import it.escsolution.escwebgis.esatri.logic.EsatriRiversamentiLogic;
import it.escsolution.escwebgis.f24.logic.F24AnnullamentoLogic;
import it.escsolution.escwebgis.f24.logic.F24VersamentiLogic;
import it.escsolution.escwebgis.f24.logic.ProspettoIncassiLogic;
import it.escsolution.escwebgis.gas.logic.FornitureGasLogic;
import it.escsolution.escwebgis.imu.logic.ConsulenzaImuLogic;
import it.escsolution.escwebgis.indagineCivico.logic.IndagineCivicoLogic;
import it.escsolution.escwebgis.locazioni.logic.LocazioniLogic;
import it.escsolution.escwebgis.modellof24.logic.ModelloF24Logic;
import it.escsolution.escwebgis.multe.logic.MulteLogic;
import it.escsolution.escwebgis.pratichePortale.logic.PraticheLogic;
import it.escsolution.escwebgis.pubblicita.logic.PubblicitaLogic;
import it.escsolution.escwebgis.redditi2004.logic.Redditi2004Logic;
import it.escsolution.escwebgis.rette.logic.RetteLogic;
import it.escsolution.escwebgis.ruolo.logic.RuoloTarsuLogic;
import it.escsolution.escwebgis.soggettoanomalie.bean.SoggettiScartati;
import it.escsolution.escwebgis.soggettoanomalie.logic.SoggettiScartatiLogic;
import it.escsolution.escwebgis.toponomastica.logic.ToponomasticaCiviciLogic;
import it.escsolution.escwebgis.toponomastica.logic.ToponomasticaStradeLogic;
import it.escsolution.escwebgis.tributi.logic.TributiOggettiICILogic;
import it.escsolution.escwebgis.tributi.logic.TributiOggettiTARSULogic;
import it.escsolution.escwebgis.tributiNew.logic.VersamentiNewLogic;
import it.escsolution.escwebgis.versamenti.iciDM.logic.VersIciDmLogic;
import it.escsolution.escwebgis.versamenti.logic.VersamentiAllLogic;
import it.escsolution.escwebgis.vus.logic.VusGasLogic;
import it.escsolution.escwebgis.vus.logic.VusLogic;
import it.webred.DecodificaPermessi;
import it.webred.GlobalParameters;
import it.webred.accessi.AccessoBean;
import it.webred.accessi.Accesso;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.basic.common.dto.KeyValueDTO;



import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
public class EscServlet extends HttpServlet {//implements EscService{
	
	protected int uc;
	protected int st;
	protected String recordScelto;
	protected String oggettoSel;
	protected Vector vettoreRicerca;
	protected EscFinder finder;
	protected String NomeServlet;
	protected boolean chiamataEsterna;
	protected boolean isBack;
	
	public static final String IDSTORICI = "IDSTORICI";
	public HashMap idStorici = null;

	boolean b = true;
	
	/*private String datasource;
	protected Principal utente;
	protected String nomeApplicazione;
	protected String ente;*/
	
	public static final String defaultDataSource = "jdbc/dbIntegrato";
	
	public static String URL_PATH = "";
	public static final String ERROR_MESSAGE = EscServlet.class.getName() + "@" + "ERROR_MESSAGE";

	protected static Logger log = Logger.getLogger("diogene.log");
	
	public ServletConfig config = null;
	public String pathDatiDiogene = "";
	
	public static final int ST_ORDER_FILTER = 123321;

	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
	
	public String getPathDatiDiogene() {
		/*if(pathDatiDiogene == null || pathDatiDiogene.equals("")){
			if(config!=null){
				pathDatiDiogene = this.config.getServletContext().getInitParameter("rootPathDatiDiogene");
			}
		}
		log.debug("PATH DATI DIOGENE: " + pathDatiDiogene);
		*/
		
		if(pathDatiDiogene == null || pathDatiDiogene.equals("")){
			ParameterSearchCriteria criteria= new ParameterSearchCriteria();
			criteria.setKey("dir.files.datiDiogene");
			
			try{
			
			ParameterService parameterService= (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
			
			AmKeyValueExt amKey=parameterService.getAmKeyValueExt(criteria);
			pathDatiDiogene= amKey.getValueConf();
			
			log.debug("PATH DATI DIOGENE: " + pathDatiDiogene);
			
			}catch (Exception e){
				pathDatiDiogene="";
			}
		}
		return pathDatiDiogene;
	}//-------------------------------------------------------------------------
	
	protected void doGet(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
			getCommonPars(request,response);
			try
			{
				URL_PATH = request.getContextPath();
				EseguiServizio(request,response);
			}
			catch (DiogeneException de)
			{
				request.getSession().setAttribute(ERROR_MESSAGE, de.getMessage());
				request.getRequestDispatcher("ewg/error.jsp").forward(request,response);
			}
	}
	protected void doPost(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
			getCommonPars(request,response);
			try
			{
				URL_PATH = request.getContextPath();
				log.debug(URL_PATH);
				EseguiServizio(request,response);
			}
			catch (DiogeneException de)
			{	
				request.getSession().setAttribute(ERROR_MESSAGE, de.getMessage());
				request.getRequestDispatcher("ewg/error.jsp").forward(request,response);
			}	
	}
	protected void chiamaPagina(HttpServletRequest request,HttpServletResponse response, String pagina, PulsantiNavigazione nav ) throws Exception
	{
		
		HttpSession sessione = request.getSession();
		sessione.setAttribute("ST",new Integer(st).toString());
		sessione.setAttribute("UC",new Integer(uc).toString());
		sessione.setAttribute("EXT",new Boolean(chiamataEsterna));
		
		if (idStorici!=null) {
			sessione.setAttribute(IDSTORICI, idStorici);
		} else {
			sessione.removeAttribute(IDSTORICI);
		}
		/*PulsantiNavigazione nav = new PulsantiNavigazione(); 
		if (chiamataEsterna){
			if (st == 3)
				nav.chiamataEsternaDettaglio();
			else if (st == 2)
				nav.chiamataEsternaLista();
			else if (st == 1)
				nav.chiamataRicerca();
		}
		else{
			if (st == 3)
				nav.chiamataInternaDettaglio();
			else if (st == 2)
				nav.chiamataInternaLista();
			else if (st == 1)
				nav.chiamataRicerca();
		}*/
		sessione.setAttribute("PULSANTI",nav);
		Accesso.registraLog(null, getEnvUtente(request), pagina, "WEB", null);
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
        response.setHeader("Pragma","no-cache"); //HTTP 1.0
        response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		request.getRequestDispatcher(pagina).forward(request,response);
		return ;
	}
	
	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException 
	{
		
		// INTERFACCIA PERMETTE SOLO LA COMUNICAZIONE VIA CROSSLINK 
		if (this instanceof Interfaccia) { 
			return;
		} else {

		AccessoBean ab ;
		//tmp fino a quando non si fattorizza
		if(getTema().equals("Diagnostiche"))
			ab= new AccessoBean(DecodificaPermessi.ITEM_DIAGNOSTICHE,DecodificaPermessi.PERMESSO_DIAGNOSTICHE);
		else{
			ab= new AccessoBean(DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, "Tema:" + getTema());
			log.debug("EseguiServizio - Tema: " + getTema() + " "+ ab);
		}

			//if (!GestionePermessi.autorizzato(utente, request.getContextPath().substring(1), DecodificaPermessi.VISUALIZZA_FONTI_DATI, "Tema:"+getTema()))
		//	if (!Accesso.isAutorizzato(getEnvUtente(), ab))
		//		throw new DiogeneException("Non si possiedono i permessi necessari per visualizzare il dato");
		}

		request.getSession().removeAttribute("MSG");

		
		if (request.getParameter("NUMERO_FILTRI")!= null){
			// richiamato dalla pagina di ricerca
			int massimo = (new Integer(request.getParameter("NUMERO_FILTRI"))).intValue();
			vettoreRicerca = new Vector();
			for (int i= 0;i< massimo; i++){
				EscElementoFiltro elementoFiltro = new EscElementoFiltro();
				String j = new Integer(i).toString();
				elementoFiltro.setAttributeName(request.getParameter("NAME_" + j));
				elementoFiltro.setOperatore(request.getParameter("OPERATORE_"+j));
				elementoFiltro.setValore(request.getParameter("VALORE_"+j));
				elementoFiltro.setTipo(request.getParameter("TIPO_"+j));
				elementoFiltro.setCampoFiltrato(request.getParameter("FIELD_"+j));
				elementoFiltro.setValori(request.getParameterValues("VALORE_"+j));
				elementoFiltro.setObbligatorio(Boolean.valueOf(request.getParameter("OBBLIGATORIO_"+j)));
				elementoFiltro.setLabel(request.getParameter("LABEL_"+j));
				/*
				NAME  
				OPERATORE
				VALORE
				TIPO
				FIELD
				OBBLIGATORIO*/
				
				vettoreRicerca.add(elementoFiltro);
			}
		}
	}
	
	protected void getCommonPars(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
		
		CeTUser user = (CeTUser) request.getSession().getAttribute("user");
		
		GlobalParameters.loadWebredConfig(new EnvUtente(user, null, null).getEnte());
		
		String UC;
		String ST;
		
		/*
		 * UC
		 */
		if (request.getAttribute("UC") == null){
			if (request.getParameter("UC") != null){
				UC = (String)(request.getParameter("UC"));
				uc = new Integer((String)UC).intValue();
			}
		}
		else
		{
			uc = new Integer(((Object)request.getAttribute("UC")).toString()).intValue();
		}
		/*
		 * st
		 */
		if (request.getAttribute("ST") == null){
			if (request.getParameter("ST") != null){
				ST = (String)(request.getParameter("ST"));
				st = new Integer((String)ST).intValue();
			}
		}
		else
		{
			st = new Integer(((Object)request.getAttribute("ST")).toString()).intValue();
		}
		/*
		 * chiamate esterne
		 */
		chiamataEsterna = false;
		if (request.getAttribute("EXT") == null){
			if (request.getParameter("EXT") != null){
				if (((String)request.getParameter("EXT")).equals("1")){
					chiamataEsterna = true;			
				}
			}
		}
		else
		{
			if (((String)request.getParameter("EXT")).equals("1")){
				chiamataEsterna = true;			
			}
		}
		/*
		 * chiamate back
		 */
		isBack = false;
		if (request.getAttribute("BACK") == null){
			if (request.getParameter("BACK") != null){
				if (((String)request.getParameter("BACK")).equals("1")){
					isBack = true;			
				}
			}
		}
		else
		{
			if (((String)request.getParameter("isBack")).equals("1")){
				isBack = true;			
			}
		}
		
	} 
	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int intero){
		return "";
	}
	protected void gestioneMultiRecord(HttpServletRequest request,HttpServletResponse response,String azione,EscFinder finder, HttpSession sessione,String nomeLista,Vector listaOggetti, String nomeDelFinder) throws Exception{
		// l'utente ha cliccato sui bottoni avanti indietro ...
		try{
			log.debug(sessione.getAttribute("ST"));
			log.debug(sessione.getAttribute("UC"));
			
			if (azione.equals("")){
				sessione.removeAttribute("BACK_RECORD_STACK");
				sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(true));
				sessione.removeAttribute("BACK_JS_COUNT");
				
				return;
			}
			/*
			 * 
			 */
			Stack stack = new Stack();
			int numeroBack = 1;
			if (request.getParameter("BACK_JS_COUNT")!= null){
				numeroBack= (new Integer((String)request.getParameter("BACK_JS_COUNT"))).intValue();
				sessione.setAttribute("BACK_JS_COUNT", new Integer(numeroBack+1));
			}
			else{
				sessione.removeAttribute("BACK_JS_COUNT");
				numeroBack= 0;
			}
				
				
			if (sessione.getAttribute("BACK_RECORD_STACK") != null)
				stack = (Stack)sessione.getAttribute("BACK_RECORD_STACK");
			Long backRecord = null;
				
				
				
			if (isBack && !stack.isEmpty()){
					
				backRecord = (Long)stack.pop();
				finder.setRecordAttuale(backRecord.longValue());
				if (!stack.isEmpty()){
					sessione.setAttribute("BACK_RECORD_STACK", stack);
					sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(false));
				}
				else{
					sessione.removeAttribute("BACK_RECORD_STACK");
					sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(true));
				}
				return ;
			}
			else{
				numeroBack ++;
				stack.push(new Long(finder.getRecordAttuale()));
				sessione.setAttribute("BACK_RECORD_STACK", stack);
				sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(false));
			}
			
			


			////////////////////////////////////////////////////////////////////////////
			if (azione.equals("1")){
				long recordSuccessivo = 0;
				if (finder.getRecordAttuale() < finder.getPaginaAttuale() * (finder.getRighePerPagina())){
					recordSuccessivo = (finder.getRecordAttuale() % finder.getRighePerPagina())+1;
					recordScelto     = new Long(finder.getRecordAttuale() +1).toString();
					oggettoSel       = getChiaveOggetto(finder, listaOggetti,(int)recordSuccessivo-1);
				}
				else{
					// carica una nuova pagina
					//CatastoImmobiliLogic logic = new CatastoImmobiliLogic();
					finder.setPaginaAttuale(finder.getPaginaAttuale()+1);
					Hashtable ht = executeLogic(finder, request);
					listaOggetti = (Vector)ht.get(nomeLista);
					sessione.setAttribute(nomeLista,listaOggetti);
					recordSuccessivo = 1;
					recordScelto = new String("0");
					oggettoSel =  getChiaveOggetto(finder, listaOggetti,(int)recordSuccessivo-1);
				}
				finder.setRecordAttuale(finder.getRecordAttuale()+1);
				sessione.removeAttribute(nomeDelFinder);
				sessione.setAttribute(nomeDelFinder,finder);
			}
			else if (azione.equals("-1")){
				long recordSuccessivo = 0;
				if (finder.getRecordAttuale() > 1+(finder.getPaginaAttuale()-1) * (finder.getRighePerPagina())){
					recordSuccessivo = finder.getRecordAttuale() - (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() -1;
					oggettoSel =  getChiaveOggetto(finder, listaOggetti,(int)recordSuccessivo-1);
					recordScelto = new Long(finder.getRecordAttuale() -1).toString();
				}
				else{
					// carica una nuova pagina
					finder.setPaginaAttuale(finder.getPaginaAttuale()-1);
					Hashtable ht = executeLogic(finder, request);
					listaOggetti = (Vector)ht.get(nomeLista);
					sessione.setAttribute(nomeLista,listaOggetti);
					recordScelto = new Long(finder.getRecordAttuale()-1).toString();
					oggettoSel = getChiaveOggetto(finder, listaOggetti,(int)finder.getRighePerPagina()-1);
				}
				finder.setRecordAttuale(finder.getRecordAttuale()-1);
				sessione.removeAttribute(nomeDelFinder);
				sessione.setAttribute(nomeDelFinder,finder);
	
			}
			else if (azione.equals("f")){
				long recordSuccessivo = 0;
				if (finder.getPaginaAttuale() == 1){
					// stessa pagina
					recordSuccessivo = 1;
					oggettoSel = getChiaveOggetto(finder, listaOggetti,0);
					//oggettoSel = ((Immobile)listaOggetti.get(0)).getChiave();
					recordScelto = "1";
				}
				else{
					// carica una nuova pagina
					//CatastoImmobiliLogic logic = new CatastoImmobiliLogic();
					finder.setPaginaAttuale(1);
					Hashtable ht = executeLogic(finder, request);
					listaOggetti = (Vector)ht.get(nomeLista);
					sessione.setAttribute(nomeLista,listaOggetti);
					recordScelto = new Long(0).toString();
					oggettoSel = getChiaveOggetto(finder, listaOggetti,0);
				}
				finder.setRecordAttuale(1);
				sessione.removeAttribute(nomeDelFinder);
				sessione.setAttribute(nomeDelFinder,finder);
					
			}
			else if (azione.equals("l")){
				long recordSuccessivo = 0;
				if (finder.getPaginaAttuale() == finder.getPagineTotali()){
					// stessa pagina
					recordSuccessivo = listaOggetti.size();
					oggettoSel = getChiaveOggetto(finder,listaOggetti,(int)recordSuccessivo-1);
					recordScelto  = new Integer(listaOggetti.size()).toString();
				}
				else{
					// carica una nuova pagina
					finder.setPaginaAttuale(finder.getPagineTotali());
					Hashtable ht = executeLogic(finder, request);
					listaOggetti = (Vector)ht.get(nomeLista);
					sessione.setAttribute(nomeLista,listaOggetti);
					recordSuccessivo = listaOggetti.size();
					recordScelto  = new Integer(listaOggetti.size()).toString();
					oggettoSel =  getChiaveOggetto(finder,listaOggetti,(int)recordSuccessivo-1);
				}
				finder.setRecordAttuale(finder.getTotaleRecordFiltrati());
				sessione.removeAttribute(nomeDelFinder);
				sessione.setAttribute(nomeDelFinder,finder);
			}
			else{
				// vai al record N
				// carica una nuova pagina
				/*finder.setPaginaAttuale(finder.getPagineTotali());
				Hashtable ht = executeLogic(finder);
				listaOggetti = (Vector)ht.get(nomeLista);
				sessione.setAttribute(nomeLista,listaOggetti);
				recordSuccessivo = listaOggetti.size();
				recordScelto  = new Integer(listaOggetti.size()).toString();
				oggettoSel =  getChiaveOggetto(finder,listaOggetti,(int)recordSuccessivo-1);*/
				
			}
			
			

			
		}
		catch(Exception ex){
			log.error(ex.getMessage(),ex);
			//throw ex;
			chiamaPaginaRicerca(request,response);
		}
	}
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		return null;
		//return new Hashtable();	
	}
	
	public void pulireSessione(HttpServletRequest request)	{
		HttpSession sessione=request.getSession();
		
		synchronized (sessione)
		{
			
		}
		
		sessione.removeAttribute(ERROR_MESSAGE);
				
		sessione.removeAttribute(IDSTORICI);
		// sezione finder
		sessione.removeAttribute("FINDER1");
		sessione.removeAttribute("FINDER2");
		sessione.removeAttribute("FINDER3");
		sessione.removeAttribute("FINDER4");
		sessione.removeAttribute("FINDER5");
		sessione.removeAttribute("FINDER6");
		sessione.removeAttribute("FINDER7");
		sessione.removeAttribute("FINDER8");
		sessione.removeAttribute("FINDER9");
		sessione.removeAttribute("FINDER10");
		sessione.removeAttribute("FINDER11");
		sessione.removeAttribute("FINDER12");
		sessione.removeAttribute("FINDER13");
		sessione.removeAttribute("FINDER14");
		sessione.removeAttribute("FINDER15");
		sessione.removeAttribute("FINDER16"); //Aziende
		sessione.removeAttribute("FINDER17");
		sessione.removeAttribute("FINDER18");
		sessione.removeAttribute("FINDER19");
		sessione.removeAttribute("FINDER20");
		sessione.removeAttribute("FINDER21");
		sessione.removeAttribute("FINDER22");
		sessione.removeAttribute("FINDER23");
		sessione.removeAttribute("FINDER24");
		sessione.removeAttribute("FINDER25");
		sessione.removeAttribute("FINDER26");
		sessione.removeAttribute("FINDER27");
		sessione.removeAttribute("FINDER28");
		sessione.removeAttribute("FINDER30");//Locazioni
		sessione.removeAttribute("FINDER31");//Concessioni
		sessione.removeAttribute("FINDER32");//Enel
		sessione.removeAttribute("FINDER33");//MODELLO F24
		
		sessione.removeAttribute("FINDER34"); //DUP TERRENI
		sessione.removeAttribute("FINDER35"); //STORICO 2006
		sessione.removeAttribute("FINDER36"); //STORICO 2005
		sessione.removeAttribute("FINDER37"); //ESATRI RIV.
		sessione.removeAttribute("FINDER38"); //ESATRI CON.
		sessione.removeAttribute("FINDER39"); //CONCESSIONI
		
		sessione.removeAttribute("FINDER42"); //VUS
		sessione.removeAttribute("FINDER43"); //DOCFA
		sessione.removeAttribute("FINDER44"); //DOCFA
		sessione.removeAttribute("FINDER45"); //ANAGRAFEDWH
		sessione.removeAttribute("FINDER46"); //ConcessioniINFORM
		sessione.removeAttribute("FINDER47"); //Concessioni Visure
		sessione.removeAttribute("FINDER48"); //Redditi 2004
		sessione.removeAttribute("FINDER49"); 
		sessione.removeAttribute("FINDER50"); //Cncici		
		sessione.removeAttribute("FINDER51"); //fORNITURADIA
		sessione.removeAttribute("FINDER52"); //redditi annuali
		sessione.removeAttribute("FINDER53"); //storico concessioni
		sessione.removeAttribute("FINDER54"); //storico concessioni
		sessione.removeAttribute("FINDER101"); //Diagnostiche
		sessione.removeAttribute("FINDER112"); //Tributi Versamenti New
		sessione.removeAttribute("FINDER115"); //Pubblicità
		sessione.removeAttribute("FINDER116"); //Ecografico Strade
		sessione.removeAttribute("FINDER117"); //Ecografico Civici
		sessione.removeAttribute("FINDER118"); //Pratiche Portale
		sessione.removeAttribute("FINDER119"); //Rette
		sessione.removeAttribute("FINDER120"); //Multe
		sessione.removeAttribute("FINDER121"); //Acqua
		sessione.removeAttribute("FINDER122"); //F24 Versamenti
		sessione.removeAttribute("FINDER123"); //F24 Annullamento
		sessione.removeAttribute("FINDER124"); //Saldo Imu Consulenza
		sessione.removeAttribute("FINDER125"); //F24 Prospetto Incassi per anno e codice tributo
		sessione.removeAttribute("FINDER126"); //Visura nazionale
		sessione.removeAttribute("FINDER127"); //FascFabbApp
		sessione.removeAttribute("FINDER128"); //Certificazioni Energetiche
		
		sessione.removeAttribute("FINDER500"); //Ricerca Soggetti
		sessione.removeAttribute("FINDER501"); //Ricerca Vie
		sessione.removeAttribute("FINDER502"); //Ricerca Civici
		sessione.removeAttribute("FINDER503"); //Ricerca Oggetti
		sessione.removeAttribute("FINDER504"); //Ricerca Fabbricati
		
		//successioni
		sessione.removeAttribute("LISTA_DEFUNTI");
		sessione.removeAttribute("LISTA_EREDI");
		sessione.removeAttribute("LISTA_OGGETTI");
		sessione.removeAttribute("LISTA_DEFUNTI_EREDITA");
		sessione.removeAttribute("LISTA_EREDI_EREDITA");
		sessione.removeAttribute("LISTA_OGGETTI_EREDITA");
		sessione.removeAttribute("DEFUNTO");
		sessione.removeAttribute("EREDE");
		sessione.removeAttribute("OGGETTO");

		//ici tarsu
		sessione.removeAttribute("LISTA_ICI");
		sessione.removeAttribute("LISTA_TARSU");
		sessione.removeAttribute("LISTA_ICI_CONTRIBUENTI");
		sessione.removeAttribute("LISTA_TARSU_CONTRIBUENTI");
		sessione.removeAttribute("LISTA_CONTRIBUENTI");
		sessione.removeAttribute("ICI");
		sessione.removeAttribute("TARSU");
		sessione.removeAttribute("TARSU_DETTAGLIO_VANI");
		sessione.removeAttribute("CONTRIBUENTE");
		sessione.removeAttribute("CONTRIBUENTE_ICI");
		sessione.removeAttribute("CONTRIBUENTE_TARSU");
		sessione.removeAttribute(TributiOggettiTARSULogic.TARSU_DOCFA_COLLEGATI);
		sessione.removeAttribute(TributiOggettiICILogic.ICI_DOCFA_COLLEGATI);
		
		// anagrafe DWH
		sessione.removeAttribute(AnagrafeDwhLogic.LISTA_ANAGRAFEDWH);
		sessione.removeAttribute(AnagrafeDwhLogic.LISTA_ANAGRAFE2DWH);
		sessione.removeAttribute(AnagrafeDwhLogic.ANAGRAFEDWH);
		sessione.removeAttribute(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI);
		
		sessione.removeAttribute(AnagrafeAnagrafeLogic.LISTA_ANAGRAFE);
		sessione.removeAttribute(AnagrafeAnagrafeLogic.LISTA_ANAGRAFE2);
		sessione.removeAttribute(AnagrafeAnagrafeLogic.LISTA_COMPONENTI_FAMIGLIA);
		sessione.removeAttribute(AnagrafeAnagrafeLogic.ANAGRAFE);
		sessione.removeAttribute("LISTA_FAMIGLIA");
		sessione.removeAttribute("FAMIGLIA");
		sessione.removeAttribute(AnagrafeStoricoAnagrafeLogic.LISTA);
		sessione.removeAttribute(AnagrafeStoricoAnagrafeLogic.ANAGRAFE_STORICO);
		sessione.removeAttribute(AnagrafeStorico2005Logic.LISTA);
		sessione.removeAttribute(AnagrafeStorico2005Logic.ANAGRAFE_STORICO);

		
		// catasto
		sessione.removeAttribute(CatastoImmobiliLogic.UNIMM);
		sessione.removeAttribute(CatastoImmobiliLogic.LISTA_CIVICI);
		sessione.removeAttribute(CatastoImmobiliLogic.LISTA_CIVICI_CAT);
		sessione.removeAttribute("TERRENO");
		sessione.removeAttribute(CatastoImmobiliLogic.DETTAGLIO_VANI_340);
		sessione.removeAttribute(CatastoTerreniLogic.LISTA_TERRENI);
		sessione.removeAttribute("LISTA_IMMOBILI");
		sessione.removeAttribute("LISTA_COMUNI");
		sessione.removeAttribute("INTESTATARIOF");
		sessione.removeAttribute("INTESTATARIOG");
		sessione.removeAttribute("LISTA_INTESTATARIF");
		sessione.removeAttribute("LISTA_INTESTATARIG");
		sessione.removeAttribute("LISTA_INTESTATARIF2");
		sessione.removeAttribute("LISTA_INTESTATARIG2");
		sessione.removeAttribute("LISTA_CATEGORIE");
		sessione.removeAttribute("LISTA_DESTINAZIONI");
		sessione.removeAttribute("GAUSS");
		sessione.removeAttribute("LISTA_GAUSS");
		sessione.removeAttribute("GAUSS_UNIMM");
		
		sessione.removeAttribute(VisuraNazionaleLogic.VISURA_NAZIONALE);
		
		// civici

		sessione.removeAttribute(ToponomasticaCiviciLogic.LISTA_CIVICI);
		sessione.removeAttribute(ToponomasticaCiviciLogic.CIVICO);
		sessione.removeAttribute(ToponomasticaCiviciLogic.CIVICIARIO);
		sessione.removeAttribute(ToponomasticaStradeLogic.TOP_STRADE);

		sessione.removeAttribute(ToponomasticaStradeLogic.STRADA);
		sessione.removeAttribute(ToponomasticaStradeLogic.STRADA_IN_VIARIO);
		
		//ISTAT
		sessione.removeAttribute("LISTA_ISTAT");
		sessione.removeAttribute("ISTAT");
		
		// Aziende
		sessione.removeAttribute(it.escsolution.escwebgis.aziende.logic.AziendeAziendeLogic.LISTA_AZIENDE);
		sessione.removeAttribute(it.escsolution.escwebgis.aziende.logic.AziendeAziendeLogic.DETTAGLIO_AZIENDA);		
		sessione.removeAttribute(it.escsolution.escwebgis.aziende.logic.AziendeAziendeLogic.LISTA_UNITA_LOCALI);	
		
		// Acquedotto
		sessione.removeAttribute(AcquedottoAcquedottoLogic.DETTAGLIO_ACQUEDOTTO);
		sessione.removeAttribute(AcquedottoAcquedottoLogic.LISTA_ACQUEDOTTO);
		sessione.removeAttribute(AcquedottoAcquedottoLogic.LISTA_SEDIME);
		
		//soggetto fascicolo
		sessione.removeAttribute("LISTA_SOGGETTIFASCICOLO");
		sessione.removeAttribute("SOGGETTOFASCICOLO");
		sessione.removeAttribute("LISTA_SOGGETTIFASCICOLO_DB");
		sessione.removeAttribute("ELENCO_CROSS");
		
		//Versamenti
		sessione.removeAttribute("LISTA_VERSAMENTI");
		
		// interfaccia
		sessione.removeAttribute("LISTA_INTERFACCE");

		//Locazioni
		sessione.removeAttribute(LocazioniLogic.LISTA_LOCAZIONI);
		sessione.removeAttribute(LocazioniLogic.LOCAZIONI);
		sessione.removeAttribute(LocazioniLogic.LISTA_DETTAGLIO_LOCAZIONI);

		//Concessioni
		sessione.removeAttribute(ConcessioniLogic.LISTA_CONCESSIONI);
		sessione.removeAttribute(ConcessioniLogic.CONCESSIONI);
		sessione.removeAttribute(ConcessioniINFORMLogic.LISTA_CONCESSIONI_INFORM);
		sessione.removeAttribute(ConcessioniINFORMLogic.CONCESSIONI_INFORM);
		
		sessione.removeAttribute(ConcessioniVisureLogic.CONCESSIONE_VISURA);

		//gas
		sessione.removeAttribute(FornitureGasLogic.LISTA);
		sessione.removeAttribute(FornitureGasLogic.DATI_GAS);

		//Modello F24
		sessione.removeAttribute(ModelloF24Logic.LISTA_DETTAGLIO_MODELLOF24);
		sessione.removeAttribute(ModelloF24Logic.LISTA_MODELLIF24);
		sessione.removeAttribute(ModelloF24Logic.MODELLOF24);
		
		//Esatri
		sessione.removeAttribute(EsatriRiversamentiLogic.LISTA);
		sessione.removeAttribute(EsatriRiversamentiLogic.ESATRI_RIVERSAMENTO);
		sessione.removeAttribute(EsatriContribuentiLogic.LISTA);
		sessione.removeAttribute(EsatriContribuentiLogic.ESATRI_CONTRIBUENTI);
		
		//Condono
		sessione.removeAttribute(CondonoLogic.LISTA);
		sessione.removeAttribute(CondonoLogic.CONDONO);
		
		//VUS
		sessione.removeAttribute(VusLogic.LISTA_VUS);
		sessione.removeAttribute(VusLogic.VUS);
		sessione.removeAttribute(VusLogic.VUS_CATA);
		
		//VUS_GAS
		sessione.removeAttribute(VusGasLogic.LISTA_VUS_GAS);
		sessione.removeAttribute(VusGasLogic.VUS_GAS);
		sessione.removeAttribute(VusGasLogic.VUS_GAS_CATA);

		//COSAP
		sessione.removeAttribute(CosapLogic.FINDER);
		sessione.removeAttribute(CosapLogic.DETT_COSAP);
		sessione.removeAttribute(CosapLogic.LISTA_DATI_COSAP);
		
		//PUBBLICITA
		sessione.removeAttribute(PubblicitaLogic.FINDER);
		sessione.removeAttribute(PubblicitaLogic.PUBBLICITA);
		sessione.removeAttribute(PubblicitaLogic.LISTA_PUBBLICITA);
		
		//MULTE
		sessione.removeAttribute(MulteLogic.FINDER);
		sessione.removeAttribute(MulteLogic.MULTE);
		sessione.removeAttribute(MulteLogic.LISTA_MULTE);
		
		//ECOGRAFICO
		sessione.removeAttribute(EcograficoStradeLogic.FINDER);
		sessione.removeAttribute(EcograficoStradeLogic.STRADA);
		sessione.removeAttribute(EcograficoStradeLogic.STRADA_IN_VIARIO);
		
		sessione.removeAttribute(EcograficoCiviciLogic.FINDER);
		sessione.removeAttribute(EcograficoCiviciLogic.CIVICO);
		sessione.removeAttribute(EcograficoCiviciLogic.LISTA_CIVICI_STRADA);
		
		//RETTE
		sessione.removeAttribute(RetteLogic.FINDER);
		sessione.removeAttribute(RetteLogic.RATE);
		sessione.removeAttribute(RetteLogic.RETTA);
		sessione.removeAttribute(RetteLogic.SOGGETTO);
		sessione.removeAttribute(RetteLogic.LISTA_RETTE);
		
		//PRATICHE PORTALE
		sessione.removeAttribute(PraticheLogic.FINDER);
		sessione.removeAttribute(PraticheLogic.PRATICA);
		sessione.removeAttribute(PraticheLogic.LISTA_PRATICHE);
		sessione.removeAttribute(PraticheLogic.ALTRE_PRATICHE);
		
		//ACQUA
		sessione.removeAttribute(AcquaLogic.FINDER);
		sessione.removeAttribute(AcquaLogic.ACQUA);
		sessione.removeAttribute(AcquaLogic.ACQUA_UTENTE);
		sessione.removeAttribute(AcquaLogic.ALTRE_UTENZE);
		sessione.removeAttribute(AcquaLogic.LISTA_ACQUA);
		
		//Docfa
		sessione.removeAttribute(DocfaLogic.LISTA_DOCFA);
		sessione.removeAttribute(DocfaLogic.DOCFA);
		sessione.removeAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_INTESTATI);
		//sessione.removeAttribute(DocfaLogic.LATITUDE_LONGITUDE);
		sessione.removeAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_UIU);
		sessione.removeAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_BENI_NON_CENS);
		sessione.removeAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_ANNOTAZIONI);
		sessione.removeAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_DATI_CENSUARI);
		sessione.removeAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_DICHIARANTI);
		sessione.removeAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_PARTE_UNO);
		sessione.removeAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_OPER);
		
		//Condono
		sessione.removeAttribute(Redditi2004Logic.LISTA);
		sessione.removeAttribute(Redditi2004Logic.REDDITI);
		sessione.removeAttribute(Redditi2004Logic.ALTRIREDDITI);

		//DIAGNOSTICHE
		sessione.removeAttribute(DiagnosticheLogic.LISTA_DIAGNOSTICHE);
		//diagnostiche docfa
		sessione.removeAttribute(DiagnosticheDocfaLogic.LISTA_DIAGNOSTICHE);
		sessione.removeAttribute(DiagnosticheDocfaLogic.DA_DATA_FORNITURA);
		sessione.removeAttribute(DiagnosticheDocfaLogic.A_DATA_FORNITURA);
		sessione.removeAttribute(DiagnosticheDocfaLogic.LISTA_CATEGORIE);
		sessione.removeAttribute(DiagnosticheDocfaNoResLogic.LISTA_DIAGNOSTICHE);
		sessione.removeAttribute(DiagnosticheDocfaNoResLogic.DA_DATA_FORNITURA);
		sessione.removeAttribute(DiagnosticheDocfaNoResLogic.A_DATA_FORNITURA);
		sessione.removeAttribute(DiagnosticheDocfaNoResLogic.LISTA_CATEGORIE);
		//diagnostiche tarsu
		sessione.removeAttribute(DiagnosticheTarsuLogic.LISTA_DIAGNOSTICHE_PROP_RES);
		sessione.removeAttribute(DiagnosticheTarsuLogic.BEAN_DIAGNOSTICHE);
		
		//CNCicic
		sessione.removeAttribute(CncIciLogic.LISTA_CNCICI);
		sessione.removeAttribute(CncIciLogic.LISTA_DETTAGLIO_CNCICI);
		
		sessione.removeAttribute("BACK_RECORD_STACK");
		sessione.removeAttribute("BACK_RECORD_ENABLE");
		sessione.removeAttribute("BACK_JS_COUNT");
		//Indagine civico
		sessione.removeAttribute(IndagineCivicoLogic.FINDER);
		sessione.removeAttribute(IndagineCivicoLogic.LISTA_CIVICI);
		sessione.removeAttribute(IndagineCivicoLogic.INDIRIZZO);
		sessione.removeAttribute(IndagineCivicoLogic.LISTA_RESIDENTI);
		sessione.removeAttribute(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE);
		sessione.removeAttribute(IndagineCivicoLogic.LISTA_UI_CONSISTENZA);
		
		//F24
		sessione.removeAttribute(F24VersamentiLogic.FINDER);
		sessione.removeAttribute(F24VersamentiLogic.VERSAMENTO);
		sessione.removeAttribute(F24VersamentiLogic.LISTA_ANNULLAMENTI);
		sessione.removeAttribute(F24VersamentiLogic.LISTA_VERSAMENTI);
		sessione.removeAttribute(F24AnnullamentoLogic.FINDER);
		sessione.removeAttribute(F24AnnullamentoLogic.ANNULLAMENTO);
		sessione.removeAttribute(F24AnnullamentoLogic.LISTA_VERSAMENTI);
		sessione.removeAttribute(F24AnnullamentoLogic.LISTA);
		sessione.removeAttribute(ProspettoIncassiLogic.LISTA_INCASSI);
		sessione.removeAttribute(ProspettoIncassiLogic.TOT_INCASSI);
		sessione.removeAttribute(ProspettoIncassiLogic.FINDER);
		
		//CONSULENZE IMU
		sessione.removeAttribute(ConsulenzaImuLogic.FINDER);
		sessione.removeAttribute(ConsulenzaImuLogic.IMU_CONS);
		sessione.removeAttribute(ConsulenzaImuLogic.IMU_ELAB);
		sessione.removeAttribute(ConsulenzaImuLogic.LISTA);
		
		
		// CONCESSIONI EILIZIE FORNITURA DIA
		sessione.removeAttribute(FornituraDiaLogic.LISTA_DIA);
		sessione.removeAttribute(FornituraDiaLogic.FORNITURADIA);

		//RUOLI TARSU - TARES
		sessione.removeAttribute(RuoloTarsuLogic.FINDER);
		sessione.removeAttribute(RuoloTarsuLogic.RUOLO);
		sessione.removeAttribute(RuoloTarsuLogic.LISTA_RUOLI);
		
		//VERSAMENTI ALL
		sessione.removeAttribute(VersamentiAllLogic.FINDER);
		sessione.removeAttribute(VersamentiAllLogic.LISTA_VERSAMENTI_ALL);
		sessione.removeAttribute(F24VersamentiLogic.LISTA_VERS_F24_CF);
		sessione.removeAttribute(F24AnnullamentoLogic.LISTA_ANN_F24_CF);
		sessione.removeAttribute(RuoloTarsuLogic.LISTA_RUOLI_CF);
		sessione.removeAttribute("LISTA_VERSAMENTI_ICI_GESTIONALE");
		sessione.removeAttribute(VersIciDmLogic.LISTA_VERS_ICI_DM);
		sessione.removeAttribute(VersIciDmLogic.LISTA_VIOL_ICI_DM);
		
		//DEMANIO
		sessione.removeAttribute(DemanioLogic.FINDER);
		sessione.removeAttribute(DemanioLogic.BENE);
		sessione.removeAttribute(DemanioLogic.LISTA_BENI);
		
		//temp per filtro
		sessione.removeAttribute("LISTA_RICERCA");
		sessione.removeAttribute("TITOLO");
		//fine temp
		
		// SOGGETTI SCARTATI
		Enumeration enumer = sessione.getAttributeNames();
		Pattern p = Pattern.compile("^" + SoggettiScartati.NOME_IN_SESSIONE + ".*$");
		// ELIMINA DALLA SESSIONE TUTTI GLI OGGETTI SoggettiScartati
		// EVENTUALMENTE PRESENTI
		Stack toRemove = new Stack();
		while (enumer.hasMoreElements())
		{
			String sessionAttributeName = (String) enumer.nextElement();
			if (p.matcher(sessionAttributeName).find())
				toRemove.push(sessionAttributeName);
			// SE FACCIO DIRETTAMENTE
			// sessione.removeAttribute(sessionAttributeName);
			// LANCIA UNA ConcurrentModificationException
		}
		while (!toRemove.isEmpty())
		{
			sessione.removeAttribute((String) toRemove.pop());
		}
	}
	protected EscFinder gestireMultiPagina(EscFinder finder, HttpServletRequest request){
		
		HttpSession sessione = request.getSession();
		String azione = "";
		if (request.getParameter("AZIONE")!= null){
			if (!request.getParameter("AZIONE").equals("") )
				azione = (String)request.getParameter("AZIONE");
		
		}
		if (!azione.equals("")){
			
			
			Stack stack = new Stack();
			int numeroBack = 1;
			if (request.getParameter("BACK_JS_COUNT")!= null){
				numeroBack= (new Integer((String)request.getParameter("BACK_JS_COUNT"))).intValue();
				sessione.setAttribute("BACK_JS_COUNT", new Integer(numeroBack+1));
			}
			else{
				sessione.removeAttribute("BACK_JS_COUNT");
				numeroBack= 0;
			}
				
			if (sessione.getAttribute("BACK_RECORD_STACK") != null)
				stack = (Stack)sessione.getAttribute("BACK_RECORD_STACK");
			Long backRecord = null;
				
				
				
			if (isBack && !stack.isEmpty()){
					
				backRecord = (Long)stack.pop();
				finder.setPaginaAttuale(backRecord.longValue());
				if (!stack.isEmpty()){
					sessione.setAttribute("BACK_RECORD_STACK", stack);
					sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(false));
				}
				else{
					sessione.removeAttribute("BACK_RECORD_STACK");
					sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(true));
				}
				return finder ;
			}
			else{
				numeroBack ++;
				stack.push(new Long(finder.getPaginaAttuale()));
				sessione.setAttribute("BACK_RECORD_STACK", stack);
				sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(false));
			}
				
			//HttpSession sessione = request.getSession();
			if (finder == null) 
				return null;
			if (azione.equals("1")){
				finder.setPaginaAttuale(finder.getPaginaAttuale() +1);
			}
			else if (azione.equals("-1")){
				finder.setPaginaAttuale(finder.getPaginaAttuale() -1);
			}
			else if (azione.equals("f")){
				finder.setPaginaAttuale(1);
			}
			else if (azione.equals("l")){
				finder.setPaginaAttuale(finder.getPagineTotali());
			}
			else{
				if (!request.getParameter("AZIONE").equals("undefined"))
					finder.setPaginaAttuale(new Long(azione).longValue());
			}
			
		}
		else{
			sessione.removeAttribute("BACK_JS_COUNT");
			finder = getNewFinder();
			finder.setPaginaAttuale(1);
			if (request.getParameter("KEYSTR")!= null || request.getParameter("queryForKeyStr")!=null ){
					if (!((String)request.getParameter("KEYSTR")==null?"":request.getParameter("KEYSTR")).equals("") 
							|| !((String)request.getParameter("queryForKeyStr")==null?"":request.getParameter("queryForKeyStr")).equals("") )
					{
						finder.setQueryForKeyStr(request.getParameter("queryForKeyStr"));
						/* @author marco*/
						// quando il link ha ritornato + di 1000 risultati io ho una query e non un elenco di chiavi
						if(finder.getQueryForKeyStr()==null) {
							
							/* @author Giulio */
							String keystr = request.getParameter("KEYSTR");
							StringBuffer sb = new StringBuffer((int) Math.floor(keystr.length() * 0.1));
							Matcher m = Pattern.compile(",").matcher(keystr);
							int count = 0;
							while (m.find())
							{
								++count;
								m.appendReplacement(sb, "','");
							}
							if (count > 1000)
							{
								String errMsg = "Il cross-link ha restituito pi&ugrave; di 1000 chiavi.<br />\r\n" +
										"&Egrave; impossibile eseguire una query SQL con pi&ugrave; di 1000 chiavi nella condizione IN della clausola WHERE.";
								throw new DiogeneException(errMsg);
							}
							m.appendTail(sb);
							finder.setKeyStr("'" + sb.toString() + "'");
							/* Fine Giulio */
						}	
				}
			}
		}
		return finder;
	}
	public EscFinder getNewFinder(){
		return null;
	}
	protected void chiamaPaginaRicerca(HttpServletRequest request,HttpServletResponse response) throws Exception{
		PulsantiNavigazione nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		chiamaPagina(request,response,Tema.getServletMapping(uc) + "?UC"+ new Integer(uc).toString()+ "&ST=" + new Integer(st).toString(), nav);
		return ;
	}
	protected void leggiCrossLink(HttpServletRequest request) throws Exception{
		HttpSession sessione = request.getSession();
		CeTUser user = (CeTUser) sessione.getAttribute("user");
		String nomeApplicazione = request.getContextPath().substring(1);
		EscLogic logic = new EscLogic(new EnvUtente(user, this.defaultDataSource, nomeApplicazione));
		Vector vct =  logic.leggiVettoreCross(getTema(), getTabellaPerCrossLink());
		sessione.setAttribute("LISTA_INTERFACCE",vct);
	}
	protected void leggiScarti(HttpSession session, EscObject bean, HttpServletRequest request) throws Exception
	{
		leggiScarti(session, bean, null, getTabellaPerCrossLink(), request);
	}
	protected void leggiScarti(HttpSession session, EscObject bean, String tabellaOrigine, HttpServletRequest request) throws Exception
	{
		leggiScarti(session, bean, tabellaOrigine, getTabellaPerCrossLink(), request);
	}
	protected void leggiScarti(HttpSession session, EscObject bean, String tabellaOrigine, String tabellaPerCrossLink, HttpServletRequest request) throws Exception
	{
		leggiScarti(session, bean, tabellaOrigine, getTabellaPerCrossLink(), null, request);
	}
	protected void leggiScarti(HttpSession session, EscObject bean, String tabellaOrigine, String tabellaPerCrossLink, String nomeClasse, HttpServletRequest request) throws Exception
	{
		String pkChiaveTabellaOrigine = Tema.getPkFieldName(uc);
		String fKcFpIFieldNames = Tema.getFKCFPIScarti(uc);
		
		if (pkChiaveTabellaOrigine != null && fKcFpIFieldNames != null && bean != null)
		{
			SoggettiScartatiLogic logic = new SoggettiScartatiLogic(getEnvUtente(request));
			ArrayList listaScartati = null;
			try {
				listaScartati = logic.getSoggettiScartati(tabellaOrigine, tabellaPerCrossLink, nomeClasse, fKcFpIFieldNames, pkChiaveTabellaOrigine, bean, getTema());
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
			if (listaScartati != null)
				session.setAttribute(SoggettiScartati.NOME_IN_SESSIONE + "@UC" + uc, listaScartati);
		}
	}
	
	protected void addOrderByToVector(HttpServletRequest request, String attributeName) {
		if (attributeName == null || attributeName.equals("")) {
			attributeName = "ORDER BY"; //default
		}
		String orderField = request.getParameter("orderField");				
		if (orderField != null && !orderField.equals("")) {
			String orderType = request.getParameter("orderType");
			if (orderType == null) {
				orderType = "";
			}
			if (vettoreRicerca == null) {
				vettoreRicerca = new Vector();
			}
			boolean trovato = false;
			Enumeration en = vettoreRicerca.elements();
			while (en.hasMoreElements()) {
				EscElementoFiltro elementoFiltro = (EscElementoFiltro) en.nextElement();
				if (elementoFiltro.getTipo().equals("O")) {
					elementoFiltro.setValore(orderField);
					elementoFiltro.setOperatore(orderType);
					trovato = true;
					break;
				}
			}
			if (!trovato) {
				EscElementoFiltro elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setAttributeName(attributeName);
				elementoFiltro.setTipo("O");
				elementoFiltro.setValore(orderField);
				elementoFiltro.setOperatore(orderType);
				vettoreRicerca.add(elementoFiltro);
			}
		}
	}
	
	public String getTabellaPerCrossLink() {
		return "";
	}	
	public String getTema() {
		return "";
	}



	private EscFinder gestireBackDettaglio(EscFinder finder,HttpServletRequest request){
		HttpSession sessione = request.getSession();
		Stack stack = new Stack();
		int numeroBack = 1;
		if (request.getParameter("BACK_JS_COUNT")!= null){
			numeroBack= (new Integer((String)request.getParameter("BACK_JS_COUNT"))).intValue();
			sessione.setAttribute("BACK_JS_COUNT", new Integer(numeroBack+1));
		}
		else{
			sessione.removeAttribute("BACK_JS_COUNT");
			numeroBack= 0;
		}
				
				
		if (sessione.getAttribute("BACK_RECORD_STACK") != null)
			stack = (Stack)sessione.getAttribute("BACK_RECORD_STACK");
		Long backRecord = null;
				
				
				
		if (isBack && !stack.isEmpty()){
					
			backRecord = (Long)stack.pop();
			finder.setPaginaAttuale(backRecord.longValue());
			if (!stack.isEmpty()){
				sessione.setAttribute("BACK_RECORD_STACK", stack);
				sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(false));
			}
			else{
				sessione.removeAttribute("BACK_RECORD_STACK");
				sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(true));
			}
			return finder;
		}
		else{
			numeroBack ++;
			stack.push(new Long(finder.getPaginaAttuale()));
			sessione.setAttribute("BACK_RECORD_STACK", stack);
			sessione.setAttribute("BACK_RECORD_ENABLE", new Boolean(false));
		}
		return finder;
	}

	/**
	 * SERVE PER IL FILTRO , PER SUGGERIRE DELLE VIE
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void mListaVie(HttpServletRequest request, HttpServletResponse response, String tableName, String colSedime, String colIndirizzo, String inputId) throws IOException
	
	{
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setHeader("Expires", "Thu, 01 Jan 1970 01:00:00 CET");
		
		String listaVie = request.getParameter("listavie");
		String sedime = request.getParameter("sedime");
		
		if(listaVie!= null && listaVie.trim().equals(""))
		{
			response.getWriter().println("Nessun criterio di ricerca impostato");
			return;
		}
		
		IndirizzoBean params = new IndirizzoBean();
		params.setIndirizzo(listaVie);
		params.setColIndirizzo(colIndirizzo);
		params.setColSedime(colSedime);
		
		if(colSedime!=null && sedime!=null && !sedime.trim().equals("") ){
			params.setSedime(sedime);
		}
			
		
		EscLogic logic = new EscLogic(this.getEnvUtente(request));
		List<KeyValueDTO> l = logic.cercaIndirizzi(params, tableName);
		if(l != null && l.size()>0)
		{
			StringBuffer s = new StringBuffer();
			s.append("<select name=\"listaNomiVie\" class=\"INPDBComboBox\" id=\"listaNomiVie\" onchange=\"document.getElementById('"+inputId+"').value=this.options[this.selectedIndex].value;document.getElementById('divListaVie').innerHTML='';\" > \n");
			Iterator i = l.iterator();
			s.append("<option value=\"\" >Seleziona</option>");
			while(i.hasNext())
			{
				KeyValueDTO value = (KeyValueDTO)i.next();
				//String value = i.next().toString();
				s.append("<option value=\""+value.getKey()+"\">"+value.getValue()+"</option> \n");
			}			
			s.append("</select> \n");			
			response.getWriter().print(s.toString());
		}
		else
		{
			response.getWriter().println("Nessun indirizzo trovato");
			
		}
		
	}
	
	public EnvUtente getEnvUtente(HttpServletRequest request) {
		CeTUser utente = (CeTUser) request.getSession().getAttribute("user");
		String datasource = getDataSource(request);		
		String nomeApplicazione = request.getContextPath().substring(1);
		return new EnvUtente(utente, datasource, nomeApplicazione);
	}
	
	public String getDataSource(HttpServletRequest request) {
		String valoreDSRequest = "";
		if (request.getAttribute("DATASOURCE") == null){
			if (request.getParameter("DATASOURCE") != null){
				valoreDSRequest = (String)request.getParameter("DATASOURCE");
			}
		}
		else
		{
			valoreDSRequest = (String)request.getAttribute("DATASOURCE");
		}
		
		//HttpSession sessione = request.getSession();
		String ds = null;
		if (getLocalDataSource()!=null)
			ds = getLocalDataSource();
			//sessione.setAttribute("DATASOURCE",getLocalDataSource());

		if (ds==null) {// (sessione.getAttribute("DATASOURCE") == null || sessione.getAttribute("DATASOURCE").equals("")){
			// in sessione non c'è nulla
			if (!valoreDSRequest.equals(""))
				ds = valoreDSRequest;
				//sessione.setAttribute("DATASOURCE",valoreDSRequest);
			else
				ds = defaultDataSource;
				//sessione.setAttribute("DATASOURCE",defaultDataSource);
		}else{
			if (!valoreDSRequest.equals(ds)) {//(!valoreDSRequest.equals(sessione.getAttribute("DATASOURCE"))){
					if (!valoreDSRequest.equals(""))
						ds = valoreDSRequest; //sessione.setAttribute("DATASOURCE",valoreDSRequest);
			}
			else
				ds = defaultDataSource; //sessione.setAttribute("DATASOURCE",defaultDataSource);
		}
		//this.datasource = (String)sessione.getAttribute("DATASOURCE");
		return ds;
	}
	
	// datasource locale della servlet figlia
	public String getLocalDataSource() {
		return null;
	}

	public void mPopup3DProspective(HttpServletRequest request,
			HttpServletResponse response) 	throws Exception {

			String f = request.getParameter("f");
			String p = request.getParameter("p");
			String cod_ente = request.getParameter("cod_ente");

			if(
					(f == null || f.trim().equals("")) || 
					(p == null || p.trim().equals("")) || 
					(cod_ente == null || cod_ente.trim().equals("")) 
			  )
				throw new Exception("dati mancanti");
			
			if (EscLogic.URL_3D_PROSPECTIVE==null)
				throw new DiogeneException("Prospettiva 3D Non disponibile");

				response.sendRedirect(EscLogic.URL_3D_PROSPECTIVE +"?cod_nazionale=" + cod_ente + "&foglio="+ f + "&particella="+ p);
		
	}
	
	public void removeOggettiIndiceDaSessione(HttpSession sessione){
		
		 sessione.removeAttribute("indice_soggetti");
		 sessione.removeAttribute("indice_oggetti");
		 sessione.removeAttribute("indice_vie");
		 sessione.removeAttribute("indice_civici");	
		 sessione.removeAttribute("indice_fabbricati");	
	}

	public Object getEjb(String ear, String module, String ejbName){
		EnvBase base = new EnvBase();
		return base.getEjb(ear, module, ejbName);
	}
}
