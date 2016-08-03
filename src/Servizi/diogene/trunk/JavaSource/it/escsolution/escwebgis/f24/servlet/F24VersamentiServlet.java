package it.escsolution.escwebgis.f24.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.ExportLogic;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.f24.bean.F24ComboObject;
import it.escsolution.escwebgis.f24.bean.F24Finder;
import it.escsolution.escwebgis.f24.logic.F24VersamentiLogic;
import it.webred.ct.data.access.basic.f24.dto.F24AnnullamentoDTO;
import it.webred.ct.data.access.basic.f24.dto.F24VersamentoDTO;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;

public class F24VersamentiServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private F24Finder finder = null;

	private final String NOMEFINDER = "FINDER122";
	private final String COD_FONTE = "33";
	
	private String localDataSource = "jdbc/Diogene_DS";
	
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
	
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
		
	}//-------------------------------------------------------------------------
    

	public void _EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {

		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		try {
			if (request.getParameter("listavie") != null)
				mListaVie(request, response, "SIT_T_24_VERSAMENTI", null, "VIA", "VIA");
			else  {
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
					case 4:
						// ho cliccato su icone XLS --> visualizzo il file XLS
						mEsportaLista(request, response);
						break;
					case 33:
						 // ho cliccato su un elemento --> visualizzo il dettaglio
						 mCaricareDettaglio(request,response,st);
					//	this.leggiCrossLink(request);
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
	

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession sessione = request.getSession();
		 F24VersamentiLogic logic = new F24VersamentiLogic(this.getEnvUtente(request));
		
		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));
		
		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));
		
		Vector operatoriNumericiMag = new Vector();
		operatoriNumericiMag.add(new EscOperatoreFiltro(">",">"));
		operatoriNumericiMag.add(new EscOperatoreFiltro(">=",">="));
		
		Vector operatoriNumericiMin = new Vector();
		operatoriNumericiMin.add(new EscOperatoreFiltro("<","<"));
		operatoriNumericiMin.add(new EscOperatoreFiltro("<=","<="));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
		
		// costruisce il vettore di elementi per la pagina di ricerca
		//titolo gruppo
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI VERSAMENTO F24");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		Vector<F24ComboObject> vctTipiImposta = new Vector<F24ComboObject>();
		//lista fissa
		vctTipiImposta.add(new F24ComboObject("", "Tutti"));
		vctTipiImposta.add(new F24ComboObject("R", "R - Contributo/Imposta di Soggiorno"));
		vctTipiImposta.add(new F24ComboObject("I", "I - ICI/IMU"));
		vctTipiImposta.add(new F24ComboObject("A", "A - TARES"));
		vctTipiImposta.add(new F24ComboObject("T", "T - TARSU/Tariffa"));
		vctTipiImposta.add(new F24ComboObject("S", "S - Tassa di Scopo"));
		vctTipiImposta.add(new F24ComboObject("O", "O - TOSAP/COSAP"));
		vctTipiImposta.add(new F24ComboObject("U", "U - TASI"));
		vctTipiImposta.add(new F24ComboObject("AC", "AC - ADDIZIONALE COMUNALE"));
		
		Vector<F24ComboObject> vctFlag = new Vector<F24ComboObject>();
		//lista fissa
		vctFlag.add(new F24ComboObject("", "Tutti"));
		vctFlag.add(new F24ComboObject("1", "Si"));
		vctFlag.add(new F24ComboObject("0", "No"));
		
		Vector<F24ComboObject> vctTipoEnte = new Vector<F24ComboObject>();
		//lista fissa
		vctTipoEnte.add(new F24ComboObject("", "Tutti"));
		vctTipoEnte.add(new F24ComboObject("C", "Agente della riscossione"));
		vctTipoEnte.add(new F24ComboObject("P", "Agenzia postale"));
		vctTipoEnte.add(new F24ComboObject("B", "Banca"));
		vctTipoEnte.add(new F24ComboObject("I", "Internet"));
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Fornitura");
		elementoFiltro.setAttributeName("DT_FORNITURA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("T.DT_FORNITURA");
		listaElementiFiltro.add(elementoFiltro);
		
	
/*		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Prog.Fornitura");
		elementoFiltro.setAttributeName("PROG_FORNITURA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.PROG_FORNITURA");
		listaElementiFiltro.add(elementoFiltro);*/
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Ripartizione");
		elementoFiltro.setAttributeName("DT_RIPARTIZIONE");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("T.DT_RIPARTIZIONE");
		listaElementiFiltro.add(elementoFiltro);
		
	/*	elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Prog.Fornitura");
		elementoFiltro.setAttributeName("PROG_RIPARTIZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.PROG_RIPARTIZIONE");
		listaElementiFiltro.add(elementoFiltro);*/
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Bonifico");
		elementoFiltro.setAttributeName("DT_BONIFICO");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("T.DT_BONIFICO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Ente");
		elementoFiltro.setAttributeName("COD_ENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.COD_ENTE_RD");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Ente");
		elementoFiltro.setAttributeName("TIPO_ENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctTipoEnte);
		elementoFiltro.setCampoFiltrato("T.TIPO_ENTE_RD");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("CAB");
		elementoFiltro.setAttributeName("CAB");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.CAB");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Fiscale Contribuente");
		elementoFiltro.setAttributeName("CF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.CF");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Fiscale Altro Soggetto");
		elementoFiltro.setAttributeName("CF2");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.CF2");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno Riferimento");
		elementoFiltro.setAttributeName("ANNO_RIF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.ANNO_RIF");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Versamento da");
		elementoFiltro.setAttributeName("DT_RISCOSSIONE");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumericiMag);
		elementoFiltro.setCampoFiltrato("T.DT_RISCOSSIONE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Versamento a");
		elementoFiltro.setAttributeName("DT_RISCOSSIONE");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumericiMin);
		elementoFiltro.setCampoFiltrato("T.DT_RISCOSSIONE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Imposta");
		elementoFiltro.setAttributeName("TIPO_IMPOSTA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctTipiImposta);
		elementoFiltro.setCampoFiltrato("T.TIPO_IMPOSTA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Tributo");
		elementoFiltro.setAttributeName("TIPO_TRIBUTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(logic.getListaTipoTributo());
		elementoFiltro.setCampoFiltrato("COD.CODICE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno Mese Rif. (AAAAMM)");
		elementoFiltro.setAttributeName("ANNO_MESE_RIF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.ANNO_MESE_RIF");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Acconto");
		elementoFiltro.setAttributeName("ACCONTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctFlag);
		elementoFiltro.setCampoFiltrato("T.ACCONTO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Saldo");
		elementoFiltro.setAttributeName("SALDO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctFlag);
		elementoFiltro.setCampoFiltrato("T.SALDO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Ravvedimento");
		elementoFiltro.setAttributeName("RAVVEDIMENTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctFlag);
		elementoFiltro.setCampoFiltrato("T.RAVVEDIMENTO");
		listaElementiFiltro.add(elementoFiltro);
		
	
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"f24/F24VersamentiFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (F24Finder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (F24Finder)gestireMultiPagina(finder,request);

		 F24VersamentiLogic logic = new F24VersamentiLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(F24VersamentiLogic.LISTA_VERSAMENTI);
		 finder = (F24Finder)ht.get(F24VersamentiLogic.FINDER);

		 sessione.setAttribute(F24VersamentiLogic.LISTA_VERSAMENTI, vct_lista);
		 sessione.setAttribute(F24VersamentiLogic.FINDER, finder);
		 sessione.setAttribute(F24VersamentiLogic.SQL_TO_EXPORT, (String)ht.get(F24VersamentiLogic.SQL_TO_EXPORT));
		 sessione.setAttribute(F24VersamentiLogic.FILTRO_RICERCA_F24VERSAMENTI, vettoreRicerca);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"f24/F24VersamentiFrame.jsp", nav);
	}//-------------------------------------------------------------------------
	
	private void mEsportaLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();
		 String sqlToExport = (String)sessione.getAttribute(F24VersamentiLogic.SQL_TO_EXPORT);
		 Vector listaElementiFiltro = (Vector)sessione.getAttribute(F24VersamentiLogic.FILTRO_RICERCA_F24VERSAMENTI);
		 
		 ExportLogic logic = new ExportLogic(this.getEnvUtente(request));
		 
		 Hashtable<String, String> htAttributi = new Hashtable<String, String>();
		 /*
		  * Hashtable per gestire in modo relativamente generico eventuali elaborazioni
		  * di qualche colonna (ad es in questo caso si è reso necessario dividere
		  * per 100 le colonne degli importi)
		  */
		 Hashtable<String, String[]> htTrattamenti = new Hashtable<String, String[]>();
		 
		 String[] aryAttrOrdine = 
				 new String[]{"DT_FORNITURA", "PROG_FORNITURA", "DT_RIPARTIZIONE", "PROG_RIPARTIZIONE", "DT_BONIFICO", "PROG_DELEGA", "PROG_RIGA", "COD_ENTE_RD", "TIPO_ENTE_RD", "CAB", "CF", "CF2", "DT_RISCOSSIONE", "COD_TRIBUTO", "DESC_TRIBUTO", "ANNO_RIF", "IMP_CREDITO", "IMP_DEBITO", "DETRAZIONE", "ACCONTO", "SALDO", "RAVVEDIMENTO", "DESC_TIPO_IMPOSTA"}; 
		 
		 htAttributi.put("DT_FORNITURA", "FORNITURA");
		 htAttributi.put("PROG_FORNITURA", "PROG");
		 htAttributi.put("DT_RIPARTIZIONE", "RIPARTIZIONE");
		 htAttributi.put("PROG_RIPARTIZIONE", "PROG");
		 htAttributi.put("DT_BONIFICO", "BONIFICO");
		 htAttributi.put("PROG_DELEGA", "PROG");
		 htAttributi.put("PROG_RIGA", "PROG RIGA");
		 htAttributi.put("COD_ENTE_RD", "COD ENTE");
		 htAttributi.put("TIPO_ENTE_RD", "TIPO ENTE");
		 htAttributi.put("CAB", "CAB");
		 htAttributi.put("CF", "CF");
		 htAttributi.put("CF2", "CF ALTRO");
		 htAttributi.put("DT_RISCOSSIONE", "RISCOSSIONE");
		 htAttributi.put("COD_TRIBUTO", "COD TRIBUTO");
		 htAttributi.put("DESC_TRIBUTO", "DESC TRIBUTO");
		 htAttributi.put("ANNO_RIF", "ANNO RIF");
		 htAttributi.put("IMP_CREDITO", "IMP CREDITO");
		 String[] aryTrat01 = new String[]{logic.TRATTAMENTO_NUM_DIVIDE , "100"};
		 htTrattamenti.put("IMP_CREDITO", aryTrat01);
		 htAttributi.put("IMP_DEBITO", "IMP DEBITO");
		 htTrattamenti.put("IMP_DEBITO", aryTrat01);
		 htAttributi.put("DETRAZIONE", "IMP DETRAZ");
		 htTrattamenti.put("DETRAZIONE", aryTrat01);
		 htAttributi.put("ACCONTO", "ACCONTO");
		 htAttributi.put("SALDO", "SALDO");
		 htAttributi.put("RAVVEDIMENTO", "RAVVEDIMENTO");
		 htAttributi.put("DESC_TIPO_IMPOSTA", "TIPO_IMPOSTA");
		 //htAttributi.put("TIPO_IMPOSTA", "TIPO_IMPOSTA");
		 
		 String nomeFile = "versamentiF24_"+ System.currentTimeMillis() +".xls";
		 String outputDirPath = getPathDatiDiogene() + "/temporaryFiles/";
		 Workbook wb = logic.mEsporta(sqlToExport, listaElementiFiltro, nomeFile, htAttributi, aryAttrOrdine, htTrattamenti );

//		 Vector vct_lista = (Vector)ht.get(F24VersamentiLogic.LISTA_VERSAMENTI);
//		 finder = (F24Finder)ht.get(F24VersamentiLogic.FINDER);
//
//		 sessione.setAttribute(F24VersamentiLogic.LISTA_VERSAMENTI, vct_lista);
//		 sessione.setAttribute(F24VersamentiLogic.FINDER, finder);
//
//		if(chiamataEsterna)
//			nav.chiamataEsternaLista();
//		else
//			nav.chiamataInternaLista();

//			String outputDirPath = "T:/" + nomeFile;
//			FileOutputStream fileOut = new FileOutputStream(outputDirPath);
//			wb.write(fileOut);
//			fileOut.close();
	 
		 
		 response.setContentType("application/vnd.ms-excel");
		 response.setHeader("Content-Disposition", "attachment; filename=f24versamenti.xls");
		 response.setHeader("Expires:", "0"); // eliminates browser caching
		 
		 wb.write(response.getOutputStream());
		 response.getOutputStream().close();
		    
		 //this.chiamaPagina(request,response,"f24/F24VersamentiFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new F24Finder().getClass()) {
				finder = (F24Finder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, F24VersamentiLogic.LISTA_VERSAMENTI, (Vector)sessione.getAttribute(F24VersamentiLogic.LISTA_VERSAMENTI), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggetti = (Vector)sessione.getAttribute(F24VersamentiLogic.LISTA_VERSAMENTI);
		} else {
			oggettoSel = "";
			recordScelto = "";
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
		 * carica il dettaglio
		 */
		F24VersamentiLogic logic = new F24VersamentiLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		//nel caso in cui provengo da Correlazione Vie/Civici e il parametro non inizia con #, e nel caso della Correlaione Fabbricati , allora l'id si riferisce all'oggetto e non al soggetto. 
		//In tutti gli altri casi l'id si riferisce al soggetto
		log.debug("ricerca dettaglio pubblicita da ID: " + oggettoSel );
		if (tipo == 101)
			ht = logic.mCaricareDettaglio(oggettoSel);	
		else if (tipo== 102 || tipo == 103 ){
			if (oggettoSel.startsWith("#") == false)
				ht = logic.mCaricareDettaglioFromOggetto(oggettoSel);
			
			else{
				oggettoSel= oggettoSel.substring(1);
				ht = logic.mCaricareDettaglio(oggettoSel);	
			}
		}
		else if (tipo == 105)
			ht = logic.mCaricareDettaglioFromOggetto(oggettoSel);	
		else
			ht = logic.mCaricareDettaglio(oggettoSel);	
		
		F24VersamentoDTO tes = (F24VersamentoDTO)ht.get(F24VersamentiLogic.VERSAMENTO);
		List<F24AnnullamentoDTO> ann = (List<F24AnnullamentoDTO>)ht.get(F24VersamentiLogic.LISTA_ANNULLAMENTI);
		
		sessione.setAttribute(F24VersamentiLogic.VERSAMENTO, tes);
		sessione.setAttribute(F24VersamentiLogic.LISTA_ANNULLAMENTI, ann);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		
		// Aggiungo Soggetti per correlazione
		OggettoIndice oi = new OggettoIndice();
		oi.setIdOrig(tes.getId());
		oi.setFonte("33");
		oi.setProgr("1");
		String descr = tes.getCf()
				+ " " +((tes.getDenominazione())!=null ? tes.getDenominazione(): "")
				+ " " +((tes.getNome()!=null) ? tes.getNome():"");
		oi.setDescrizione(descr.trim());
		
		soggettiInd.add(oi);
		
		if(tes.getCf2()!=null){
			OggettoIndice oi1 = new OggettoIndice();
			oi1.setIdOrig(tes.getId());
			oi1.setFonte("33");
			oi1.setProgr("2");
			oi1.setDescrizione(tes.getCf2());
			soggettiInd.add(oi1);
		}
		
		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		
		this.chiamaPagina(request,response,"f24/F24VersamentiFrame.jsp", nav);
	}	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((F24VersamentoDTO)listaOggetti.get(recordSuccessivo)).getChiave();
	}

	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (F24Finder)finder2;
		F24VersamentiLogic logic = new F24VersamentiLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new F24Finder();
	}

	public String getTema() {
		return "F24";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_T_24_VERSAMENTI";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

