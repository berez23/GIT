package it.escsolution.escwebgis.tributiNew.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.imu.logic.ConsulenzaImuLogic;
import it.escsolution.escwebgis.tributiNew.bean.ContribuentiNewFinder;
import it.escsolution.escwebgis.tributiNew.bean.SoggettiTributiNew;
import it.escsolution.escwebgis.tributiNew.bean.TipoSoggetto;
import it.escsolution.escwebgis.tributiNew.bean.TitoloSoggetto;
import it.escsolution.escwebgis.tributiNew.bean.Tributo;
import it.escsolution.escwebgis.tributiNew.logic.TributiContribuentiNewLogic;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuConsulenzaDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuElaborazioneDTO;
import it.webred.indice.OggettoIndice;

public class TributiContribuentiNewServlet extends EscServlet implements EscService {
	
	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private ContribuentiNewFinder finder = null;
	private final String NOMEFINDER = "FINDER110";
	
	private String localDataSource = "jdbc/Diogene_DS";

	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
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
		super.EseguiServizio(request,response);
		
		try{
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
				mCaricareDettaglio(request,response,false);
				//this.leggiCrossLink(request);
				break;
			case 5:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response,false);
				//this.leggiCrossLink(request);
				break;
			case 33:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response,false);
				//this.leggiCrossLink(request);
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
	
	public void _EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
					case 101:
						//mCaricareLista(request,response, 101);		
						mCaricareDettaglio(request,response,false);
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
	
	/**
	 * @param request
	 * @param response
	 */
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//caricare dati che servono nella pagina di ricerca --> eventuali combo
		
		HttpSession sessione = request.getSession();
		
		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
		
		// costruisce il vettore di elementi per la pagina di ricerca
		Vector<TipoSoggetto> vctTipiSoggetto = new Vector<TipoSoggetto>();
		//lista fissa
		vctTipiSoggetto.add(new TipoSoggetto("", "Tutti"));
		vctTipiSoggetto.add(new TipoSoggetto("F", "Persona Fisica"));
		vctTipiSoggetto.add(new TipoSoggetto("G", "Persona Giuridica"));
		
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Soggetto");
		elementoFiltro.setAttributeName("TIP_SOGG");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctTipiSoggetto);
		elementoFiltro.setCampoFiltrato("TIP_SOGG");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("COD_FISC");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("COD_FISC");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Partita Iva");
		elementoFiltro.setAttributeName("PART_IVA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("PART_IVA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COG_DENOM");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("COG_DENOM");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("NOME");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denominazione");
		elementoFiltro.setAttributeName("COG_DENOM");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("COG_DENOM");
		listaElementiFiltro.add(elementoFiltro);
		
		Vector<Tributo> vctTributi = new Vector<Tributo>();
		//lista fissa
		vctTributi.add(new Tributo("", "Tutti"));
		vctTributi.add(new Tributo("I", "ICI"));
		vctTributi.add(new Tributo("T", "TARSU"));
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tributo");
		elementoFiltro.setAttributeName("TIP_TRIB");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctTributi);
		elementoFiltro.setCampoFiltrato("TIP_TRIB");
		listaElementiFiltro.add(elementoFiltro);
		
		Vector<TitoloSoggetto> vctTitoliSoggetto = new Vector<TitoloSoggetto>();
		//lista fissa
		vctTitoliSoggetto.add(new TitoloSoggetto("CNT", "Contribuente"));
		vctTitoliSoggetto.add(new TitoloSoggetto("DIC", "Dichiarante"));
		vctTitoliSoggetto.add(new TitoloSoggetto("CTT", "Contitolare"));
		vctTitoliSoggetto.add(new TitoloSoggetto("ULT", "Ulteriori Soggetti"));
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Titolo Soggetto<br/>(deselezionando tutte le quattro<br/>opzioni proposte, verranno<br/>ricercati solo i contribuenti<br/>senza oggetti collegati)");
		elementoFiltro.setAttributeName("TIT_SOGG");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(null);
		elementoFiltro.setListaValori(vctTitoliSoggetto);
		elementoFiltro.setCampoFiltrato("TIT_SOGG");
		elementoFiltro.setComboSize(vctTitoliSoggetto.size());
		elementoFiltro.setCheckList(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Filtro accorpando per C.F./P.I.");
		elementoFiltro.setAttributeName("ACCORP_CF_PI");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(null);
		elementoFiltro.setCheckBox(true);
		elementoFiltro.setCampoFiltrato("ACCORP_CF_PI");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"tributiNew/oggICINewFrame.jsp", nav);

	}

	/**
	 * @param request
	 * @param response
	 */
	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response, int tipo) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();
		 
		 String idSoggetto= request.getParameter("OGGETTO_SEL");

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (ContribuentiNewFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (ContribuentiNewFinder)gestireMultiPagina(finder, request);

		 TributiContribuentiNewLogic logic = new TributiContribuentiNewLogic(this.getEnvUtente(request));
		 
		 Hashtable ht =null;
		 if (tipo==101)
		  ht = logic.mCaricareListaContribuentiFromSoggetto(idSoggetto, finder);
		 else
		  ht = logic.mCaricareListaContribuenti(vettoreRicerca, finder);

		 // eseguire la query e caricare il vettore con i risultati
		 // chiamare la pagina di lista

		 Vector vct_lista_con = (Vector)ht.get("LISTA_CONTRIBUENTI");
		 finder = (ContribuentiNewFinder)ht.get("FINDER");

		 sessione.setAttribute("LISTA_CONTRIBUENTI", vct_lista_con);
		 sessione.setAttribute(NOMEFINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request, response, "tributiNew/conNewFrame.jsp", nav);

	}

	/**
	 * @param request
	 * @param response
	 */
	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, boolean soloDettaglio) throws Exception {
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		//boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ContribuentiNewFinder().getClass()){
				finder = (ContribuentiNewFinder)sessione.getAttribute(NOMEFINDER);
			}
			else{
				// il finder non corrisponde -->
				//sessione.removeAttribute("FINDER");
				finder = null;
			}
		}
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, "LISTA_CONTRIBUENTI", (Vector)sessione.getAttribute("LISTA_CONTRIBUENTI"), NOMEFINDER);
		if (!azione.equals("")){
			Vector listaContribuenti = (Vector)sessione.getAttribute("LISTA_CONTRIBUENTI");
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
				if (finder!=null && !recordScelto.equals(""))
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}


		/*
		 * carica il dettaglio
		 */
		
		TributiContribuentiNewLogic logic = new TributiContribuentiNewLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioContribuenti(oggettoSel);
		 
		
		ArrayList contr = (ArrayList)ht.get("CONTR");
		ArrayList ICIList = (ArrayList)ht.get("ICI_LIST");
		ArrayList TARSUList = (ArrayList)ht.get("TARSU_LIST");
		SoggettiTributiNew datoSoggettoCrossLink = (SoggettiTributiNew)ht.get("CONTRIBUENTI"); //per crosslink
		SaldoImuConsulenzaDTO consulenza = (SaldoImuConsulenzaDTO) ht.get(ConsulenzaImuLogic.IMU_CONS);
		SaldoImuElaborazioneDTO datiElab = (SaldoImuElaborazioneDTO) ht.get(ConsulenzaImuLogic.IMU_ELAB); 
		
		sessione.setAttribute("CONTR", contr);
		sessione.setAttribute("ICI_LIST", ICIList);
		sessione.setAttribute("TARSU_LIST", TARSUList);
		sessione.setAttribute("CONTRIBUENTI", datoSoggettoCrossLink); //per crosslink

		sessione.setAttribute(ConsulenzaImuLogic.IMU_CONS, consulenza);
		sessione.setAttribute(ConsulenzaImuLogic.IMU_ELAB, datiElab);
		
		sessione.setAttribute("SOLO_DETTAGLIO", new Boolean(soloDettaglio));
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
		
		List<String> soggettiElaborati = new ArrayList<String>();
		
		for (int i=0; i< contr.size(); i++){
			
			SoggettiTributiNew contrSogg=(SoggettiTributiNew) contr.get(i);
			
			String id = contrSogg.getId();
			
			if(!soggettiElaborati.contains(id)){
			
				OggettoIndice oi = new OggettoIndice();
				
				oi.setIdOrig(contrSogg.getId());
				
				if (contrSogg.getTributo()!= null && contrSogg.getTributo().equals("ICI")){
					oi.setFonte("2");
					oi.setProgr("1");
				}else if (contrSogg.getTributo()!= null && contrSogg.getTributo().equals("TARSU")){
					oi.setFonte("2");
					oi.setProgr("4");
				}
				
				if ((contrSogg.getCogDenom()!= null && !"".equals(contrSogg.getCogDenom()) && !"-".equals(contrSogg.getCogDenom())) || (contrSogg.getNome()!= null && !"".equals(contrSogg.getNome()) && !"-".equals(contrSogg.getNome())))
					oi.setDescrizione(contrSogg.getCogDenom() + " " + contrSogg.getNome());
				else if (contrSogg.getDenominazione()!= null && !contrSogg.getDenominazione().equals(""))
					oi.setDescrizione(contrSogg.getDenominazione());
				
				sOggettiInd.add(oi);
				
				soggettiElaborati.add(id);
			}
		}
	
		sessione.setAttribute("indice_soggetti", sOggettiInd);

		this.chiamaPagina(request, response, "tributiNew/conNewFrame.jsp", nav);		
			
	}
	
	

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo) {
		return ((SoggettiTributiNew)listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (ContribuentiNewFinder)finder2;
		TributiContribuentiNewLogic logic = new TributiContribuentiNewLogic(this.getEnvUtente(request));
		return logic.mCaricareListaContribuenti(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder(){
		return new ContribuentiNewFinder();
	}

	public String getTema() {
		return "Tributi";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_T_SOGG";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}