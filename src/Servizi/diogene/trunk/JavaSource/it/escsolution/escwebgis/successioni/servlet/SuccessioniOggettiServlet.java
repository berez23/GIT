 /*
  * Created on 3-mag-2004
  *
  * To change the template for this generated file go to
  * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
  */
 package it.escsolution.escwebgis.successioni.servlet;

 import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.successioni.bean.Oggetto;
import it.escsolution.escwebgis.successioni.bean.OggettoFinder;
import it.escsolution.escwebgis.successioni.logic.SuccessioniOggettiLogic;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

 public class SuccessioniOggettiServlet extends EscServlet implements EscService {

 	private String recordScelto;
 	private SuccessioniOggettiLogic logic = null;
 	private final String NOMEFINDER = "FINDER19";
 	private OggettoFinder finder = null;
 	PulsantiNavigazione nav;
 	public void EseguiServizio(
 		HttpServletRequest request,HttpServletResponse response)
 				throws ServletException, IOException {

 		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
 		
 		super.EseguiServizio(request,response);
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
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
					mCaricareDettaglio(request,response, 102);
					break;				
				case 103:
					// carico la form di ricerca
					pulireSessione(request);
					mCaricareDettaglio(request,response, 103);
					break;
				case 104:
					// carico la form di ricerca
					pulireSessione(request);
					mCaricareDettaglio(request,response, 104);
					break;
				case 105:
					// carico la form di ricerca
					pulireSessione(request);
					mCaricareDettaglio(request,response, 105);
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
 	
	public void _EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
 		try{
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
 					 mCaricareDettaglio(request,response,3);
 					 //this.leggiCrossLink(request);
 					 break;
 				case 4:
 					 // chiamata da altre successioni --> visualizzo elenco
 					 mCaricareListaEredita(request,response);
 					 //this.leggiCrossLink(request);
 					 break;
 				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response,33);
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

 	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception{

 				//caricare dati che servono nella pagina di ricerca
				/** Comune */
				Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
				/***/

 				HttpSession sessione = request.getSession();


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

 				EscElementoFiltro elementoFiltro = new EscElementoFiltro();

 				//UFFICIO
 				elementoFiltro.setLabel("Ufficio");
 				elementoFiltro.setAttributeName("UFFICIO");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.UFFICIO");
 				listaElementiFiltro.add(elementoFiltro);

 				//ANNO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Anno");
 				elementoFiltro.setAttributeName("ANNO");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.ANNO");
 				listaElementiFiltro.add(elementoFiltro);

 				//VOLUME
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Volume");
 				elementoFiltro.setAttributeName("VOLUME");
 				elementoFiltro.setTipo("N");
 				elementoFiltro.setCampoJS("numeroIntero");
 				elementoFiltro.setListaOperatori(operatoriNumerici);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.VOLUME");
 				listaElementiFiltro.add(elementoFiltro);

 				//NUMERO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Numero");
 				elementoFiltro.setAttributeName("NUMERO");
 				elementoFiltro.setTipo("N");
 				elementoFiltro.setCampoJS("numeroIntero");
 				elementoFiltro.setListaOperatori(operatoriNumerici);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.NUMERO");
 				listaElementiFiltro.add(elementoFiltro);

 				//SOTTONUMERO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Sottonumero");
 				elementoFiltro.setAttributeName("SOTTONUMERO");
 				elementoFiltro.setTipo("N");
 				elementoFiltro.setCampoJS("numeroIntero");
 				elementoFiltro.setListaOperatori(operatoriNumerici);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.SOTTONUMERO");
 				listaElementiFiltro.add(elementoFiltro);

 				//COMUNE
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Comune");
 				elementoFiltro.setAttributeName("COMUNE");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setListaValori(vctComuni);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.COMUNE");
 				listaElementiFiltro.add(elementoFiltro);

 				//PROGRESSIVO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Progressivo");
 				elementoFiltro.setAttributeName("PROGRESSIVO");
 				elementoFiltro.setTipo("N");
 				elementoFiltro.setCampoJS("numeroIntero");
 				elementoFiltro.setListaOperatori(operatoriNumerici);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.PROGRESSIVO");
 				listaElementiFiltro.add(elementoFiltro);

 				//FOGLIO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Foglio");
 				elementoFiltro.setAttributeName("FOGLIO");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("lpad4_0");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.FOGLIO");
 				listaElementiFiltro.add(elementoFiltro);

 				//PARTICELLA
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Particella");
 				elementoFiltro.setAttributeName("PARTICELLA");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("lpad5_0");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.PARTICELLA1");
 				listaElementiFiltro.add(elementoFiltro);

 				//SUBALTERNO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Subalterno");
 				elementoFiltro.setAttributeName("SUBALTERNO");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringa);
 				elementoFiltro.setCampoFiltrato("mi_successioni_c.SUBALTERNO1");
 				listaElementiFiltro.add(elementoFiltro);

 				sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
 				sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
 			
 				//chiamare la pagina di ricerca
 				nav = new PulsantiNavigazione();
 				nav.chiamataRicerca();
 				this.chiamaPagina(request,response,"successioni/oggFrame.jsp", nav);

 			}

 	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response, int tipo)throws Exception{
 		// prendere dalla request i parametri di ricerca impostati dall'utente
 		HttpSession sessione = request.getSession();

 		if (sessione.getAttribute(NOMEFINDER)!= null){
 			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new OggettoFinder().getClass()){
 				finder = (OggettoFinder)sessione.getAttribute(NOMEFINDER);
 			}
 			else
 				finder = null;
 		}

 		finder = (OggettoFinder)gestireMultiPagina(finder,request);
 		//eseguire la query e caricare il vettore con i risultati
 		SuccessioniOggettiLogic logic = new SuccessioniOggettiLogic(this.getEnvUtente(request));
 		Hashtable ht = null;
 		if (tipo == 103)
 		  ht = logic.mCaricareListaCiv(request.getParameter("OGGETTO_SEL"));
 		else
 			 ht = logic.mCaricareLista(vettoreRicerca,finder);

 		Vector vct_lista= (Vector)ht.get("LISTA");
 		finder = (OggettoFinder)ht.get("FINDER");
 		sessione.setAttribute("LISTA_OGGETTI",vct_lista);
 		sessione.setAttribute(NOMEFINDER,finder);

 		nav = new PulsantiNavigazione();
 		if (chiamataEsterna){

 			nav.chiamataEsternaLista();
 			nav.setExt("1");
 			nav.setPrimo(true);
 		}
 		else
 			nav.chiamataInternaLista();

 		this.chiamaPagina(request,response,"successioni/oggFrame.jsp",nav);
 	}

	private void mCaricareListaEredita(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new OggettoFinder().getClass()){
				finder = (OggettoFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (OggettoFinder)gestireMultiPagina(finder,request);

		SuccessioniOggettiLogic logic = new SuccessioniOggettiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaEredita(request.getParameter("OGGETTO_SEL"));

	    Vector vct_lista= (Vector)ht.get("LISTA");
		finder = (OggettoFinder)ht.get("FINDER");
		sessione.setAttribute("LISTA_OGGETTI_EREDITA",vct_lista);
		sessione.setAttribute(NOMEFINDER,finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna){

			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();
		this.chiamaPagina(request,response,"successioni/oggFrame.jsp",nav);
	}


 	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, int tipo)throws Exception{
 				String azione = "";
 				HttpSession sessione = request.getSession();
 				
 				removeOggettiIndiceDaSessione(sessione);
 				
 				OggettoFinder finder = null;
 				//boolean chiamataEsterna = false;
 				if (sessione.getAttribute(NOMEFINDER) !=null){
 					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new OggettoFinder().getClass()){
 						finder = (OggettoFinder)sessione.getAttribute(NOMEFINDER);
 					}
 				}
 				if (request.getParameter("AZIONE")!= null)
 					azione = request.getParameter("AZIONE");
 				gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_OGGETTI",(Vector)sessione.getAttribute("LISTA_OGGETTI"),NOMEFINDER);
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

 				/*
 				 * carica il dettaglio
 				 */
 				SuccessioniOggettiLogic logic = new SuccessioniOggettiLogic(this.getEnvUtente(request));
 				
 				if (tipo == 102 || tipo == 103 || tipo == 104 || tipo == 105){
 					String[] chiaviArr= oggettoSel.split("\\|",-1);
 					oggettoSel="";
 					for (int i=0; i< chiaviArr.length-1; i++){
 						oggettoSel=oggettoSel+ chiaviArr[i];
 						 if (i != chiaviArr.length-2 )
 							oggettoSel= oggettoSel + "|";
 					 }
 				}
 				Hashtable ht = logic.mCaricareDettaglio(oggettoSel);

 				Oggetto det = (Oggetto)ht.get("DETTAGLIO");
 				sessione.setAttribute("OGGETTO",det);

 				Vector<OggettoIndice> cOggettiInd = new Vector<OggettoIndice>();
 				Vector<OggettoIndice> vieOggettiInd = new Vector<OggettoIndice>();
 				Vector<OggettoIndice> oggOggettiInd = new Vector<OggettoIndice>();
 				Vector<OggettoIndice> fabbOggettiInd = new Vector<OggettoIndice>();
 				
				OggettoIndice oi1 = new OggettoIndice();
				
				oi1.setDescrizione(det.getIndirizzoImmobile());
				oi1.setFonte("6");
				oi1.setProgr("3");				
				oi1.setIdOrig(det.getUfficio() + "|" + det.getAnno() +
						           "|" + det.getVolume() +"|" + det.getNumero() + "|" + det.getSottonumero() +
						           "|" + det.getComune() + "|" + det.getProgressivo() + "|" + det.getProgressivoImmobile() +
						           "|" + det.getFornitura());
				
				
				cOggettiInd.add(oi1);
				sessione.setAttribute("indice_civici", cOggettiInd);
				
				 oi1 = new OggettoIndice();
				
				oi1.setDescrizione(det.getIndirizzoImmobile());
				oi1.setFonte("6");
				oi1.setProgr("3");				
				oi1.setIdOrig(det.getUfficio() + "|" + det.getAnno() +
						           "|" + det.getVolume() +"|" + det.getNumero() + "|" + det.getSottonumero() +
						           "|" + det.getComune() + "|" + det.getProgressivo() + "|" + det.getProgressivoImmobile() +
						           "|" + det.getFornitura());
				
				
				vieOggettiInd.add(oi1);
				sessione.setAttribute("indice_vie", vieOggettiInd);

			    oi1 = new OggettoIndice();
					
				oi1.setDescrizione("F:" + det.getFoglio() + " P:" + det.getParticella() + " S:" + det.getSubalterno());
				oi1.setFonte("6");
				oi1.setProgr("3");				
				oi1.setIdOrig(det.getUfficio() + "|" + det.getAnno() +
					           "|" + det.getVolume() +"|" + det.getNumero() + "|" + det.getSottonumero() +
					           "|" + det.getComune() + "|" + det.getProgressivo() + "|" + det.getProgressivoImmobile() +
					           "|" + det.getFornitura());
					
				oggOggettiInd.add(oi1);
				sessione.setAttribute("indice_oggetti", oggOggettiInd);
				
				
				 oi1 = new OggettoIndice();
					
					oi1.setDescrizione("SEZ: -"+" F:" + det.getFoglio() + " P:" + det.getParticella() );
					oi1.setFonte("6");
					oi1.setProgr("3");				
					oi1.setIdOrig(det.getUfficio() + "|" + det.getAnno() +
						           "|" + det.getVolume() +"|" + det.getNumero() + "|" + det.getSottonumero() +
						           "|" + det.getComune() + "|" + det.getProgressivo() + "|" + det.getProgressivoImmobile() +
						           "|" + det.getFornitura());
						
					fabbOggettiInd.add(oi1);
					sessione.setAttribute("indice_fabbricati", fabbOggettiInd);


 				nav = new PulsantiNavigazione();
 				if (chiamataEsterna)
 					nav.chiamataEsternaDettaglio();
 				else
 					nav.chiamataInternaDettaglio();

 				this.chiamaPagina(request,response,"successioni/oggFrame.jsp", nav);

 			}



 	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
 		finder = (OggettoFinder)finder2;
 		SuccessioniOggettiLogic logic = new SuccessioniOggettiLogic(this.getEnvUtente(request));
 		return logic.mCaricareLista(this.vettoreRicerca, finder);
 	}


 	public EscFinder getNewFinder(){
 		return new OggettoFinder();
 	}
 	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){

 		Oggetto def = (Oggetto)listaOggetti.get(recordSuccessivo);
 		String key = def.getUfficio()+"|"+def.getAnno()+"|"+def.getVolume()+"|"+def.getNumero()+"|"+def.getSottonumero()+"|"+def.getComune()+"|"+def.getProgressivo()+"|"+def.getProgressivo();
 		return key;
 	}


 	public String getTema() {
 		return "Successioni";
 	}
 	public String getTabellaPerCrossLink() {
 		return "MI_SUCCESSIONI_C";
 	}

 }



