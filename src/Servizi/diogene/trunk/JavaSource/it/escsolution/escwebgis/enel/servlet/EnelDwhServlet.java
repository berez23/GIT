 /*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.enel.servlet;
import it.escsolution.escwebgis.anagrafe.bean.Anagrafe;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder;
import it.escsolution.escwebgis.anagrafe.bean.Famiglia;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeAnagrafeLogic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeFamigliaLogic;
import it.escsolution.escwebgis.catasto.bean.Sesso;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.docfa.logic.DocfaLogic;
import it.escsolution.escwebgis.enel.bean.EnelBean;
import it.escsolution.escwebgis.enel.bean.EnelBean2;
import it.escsolution.escwebgis.enel.bean.EnelDwhFinder;
import it.escsolution.escwebgis.enel.logic.EnelDwhLogic;
import it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioAnagNew;
import it.webred.GlobalParameters;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class EnelDwhServlet extends EscServlet implements EscService {

	private String localDataSource = "jdbc/Diogene_DS";

	private String recordScelto;
	private EnelDwhLogic logic = null;
	public static final String NOMEFINDER = "FINDER105";
	private EnelDwhFinder finder = null;
	PulsantiNavigazione nav;
	public void EseguiServizio(
		HttpServletRequest request,HttpServletResponse response)
				throws ServletException, IOException {
				/*
				 * ad ogni uc corrisponde una funzionalità diversa
				 *
				 */
		//super.init(getServletConfig());
		//String aaa = getServletConfig().getServletName();
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
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
 			mCaricareListaFromSoggetto(request,response);			
			break;
 		 case 102:			 		
			 mCaricareDettaglio(request,response, 102);
			 break; 
		   case 103:			 		
			 mCaricareDettaglio(request,response, 103);			 
			 break; 		
		   case 104:			 		
			 mCaricareDettaglio(request,response, 104);
			 break;		
		   case 105:			 		
			 mCaricareDettaglio(request,response, 105);
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
		
		 try{

				if (request.getParameter("listavie") != null)
					mListaVie(request, response, "SIT_ENEL_UTENZA_INDIR", null, "INDIRIZZO_UBICAZIONE","INDIRIZZO_UBICAZIONE");	
				else  {
			 		switch (st){
						 case 1:
							 // carico la form di ricerca
							 pulireSessione(request);
							 mCaricareFormRicerca(request,response);
							 break;
						 case 2:
							 // vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
						     //pulireSessione(request);
							 mCaricareLista(request,response);
							 break;
						 case 3:
							 // ho cliccato su un elemento --> visualizzo il dettaglio
							 mCaricareDettaglio(request,response,3);
							 //this.leggiCrossLink(request);
							 break;
						 case 33:
							 // ho cliccato su un elemento --> visualizzo il dettaglio
							 mCaricareDettaglio(request,response,33);
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
				operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
				operatoriNumerici.add(new EscOperatoreFiltro("<","<"));
				operatoriNumerici.add(new EscOperatoreFiltro("=","="));
				operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));

				Vector operatoriStringaRid = new Vector();
				operatoriStringaRid.add(new EscOperatoreFiltro("like","contiene"));
				operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

				// costruisce il vettore di elementi per la pagina di ricerca

				EscElementoFiltro elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Comune");
				elementoFiltro.setAttributeName("COMUNE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setListaValori(vctComuni);
				elementoFiltro.setCampoFiltrato("CODENT");
				listaElementiFiltro.add(elementoFiltro);


				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Codice Fiscale Utente");
				elementoFiltro.setAttributeName("CODICE_ISCALE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("CODICE_FISCALE");
				listaElementiFiltro.add(elementoFiltro);


				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Denominazione");
				elementoFiltro.setAttributeName("DENOMINAZIONE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringaRid);
				elementoFiltro.setCampoFiltrato("DENOMINAZIONE");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Codice Utenza");
				elementoFiltro.setAttributeName("CODICE_UTENZA");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("CODICE_UTENZA");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Anno riferimento");
				elementoFiltro.setAttributeName("ANNO_RIFERIMENTO_DATI");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("ANNO_RIFERIMENTO_DATI");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("KW Fatturati");
				elementoFiltro.setAttributeName("KWH_FATTURATI");
				elementoFiltro.setTipo("N");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriNumerici);
				elementoFiltro.setCampoFiltrato("KWH_FATTURATI");
				listaElementiFiltro.add(elementoFiltro);	
				
				
				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Indirizzo Utenza");
				elementoFiltro.setAttributeName("INDIRIZZO_UBICAZIONE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/EnelDwh?listavie='+document.getElementById('INDIRIZZO_UBICAZIONE').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("INDIRIZZO_UBICAZIONE");
				listaElementiFiltro.add(elementoFiltro);				
				
				sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
				sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
				


				//chiamare la pagina di ricerca
				nav = new PulsantiNavigazione();
				nav.chiamataRicerca();
				this.chiamaPagina(request,response,"eneldwh/enelDwhFrame.jsp", nav);

			}
	
	
	private void mCaricareListaExt(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();




		EnelDwhLogic logic = new EnelDwhLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaEnelDaCiv(request.getParameter("OGGETTO_SEL"));



	    Vector vct_lista_enel= (Vector)ht.get(EnelDwhLogic.LISTA_ENELDWH);
		finder = (EnelDwhFinder)ht.get(EnelDwhLogic.FINDER);

		sessione.setAttribute(EnelDwhLogic.LISTA_ENELDWH, vct_lista_enel);
		sessione.setAttribute(NOMEFINDER, finder);


			sessione.setAttribute(EnelDwhLogic.LISTA_ENELDWH, vct_lista_enel);
			sessione.setAttribute(NOMEFINDER, finder);

			nav = new PulsantiNavigazione();
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
			this.chiamaPagina(request,response,"eneldwh/enelDwhFrame.jsp",nav);

	}
	
	private void mCaricareListaFromSoggetto(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();


		EnelDwhLogic logic = new EnelDwhLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaEnelFromSoggetto(request.getParameter("OGGETTO_SEL"));



	    Vector vct_lista_enel= (Vector)ht.get(EnelDwhLogic.LISTA_ENELDWH);
		finder = (EnelDwhFinder)ht.get(EnelDwhLogic.FINDER);

				sessione.setAttribute(EnelDwhLogic.LISTA_ENELDWH, vct_lista_enel);
			sessione.setAttribute(NOMEFINDER, finder);

			nav = new PulsantiNavigazione();
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
			this.chiamaPagina(request,response,"eneldwh/enelDwhFrame.jsp",nav);

	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();



		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new EnelDwhFinder().getClass()){
				finder = (EnelDwhFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}


		finder = (EnelDwhFinder)gestireMultiPagina(finder,request);

		EnelDwhLogic logic = new EnelDwhLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaEnel(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista


	    Vector vct_lista_enel= (Vector)ht.get(EnelDwhLogic.LISTA_ENELDWH);
		finder = (EnelDwhFinder)ht.get(EnelDwhLogic.FINDER);

		sessione.setAttribute(EnelDwhLogic.LISTA_ENELDWH, vct_lista_enel);
		sessione.setAttribute(NOMEFINDER, finder);


//TEST MB
		boolean saltaListaConUno = GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()) == null ? GlobalParameters.SALTA_LISTA_CON_UNO_DEF : GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()).booleanValue();
		if (saltaListaConUno && vct_lista_enel.size() == 1){
			st=33;
			EseguiServizio(request, response);
			return;
			
	
		}else {
//FINE TEST MB
			sessione.setAttribute(EnelDwhLogic.LISTA_ENELDWH, vct_lista_enel);
			sessione.setAttribute(NOMEFINDER, finder);

			nav = new PulsantiNavigazione();
			if (chiamataEsterna){

				nav.chiamataEsternaLista();
				nav.setExt("1");
				nav.setPrimo(true);
			}
			else
				nav.chiamataInternaLista();
			this.chiamaPagina(request,response,"eneldwh/enelDwhFrame.jsp",nav);

//		TEST MB
		}
//		FINE TEST MB

	}




	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, int tipo)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				String utenzaSel = "";
				String annoSel = "";
				HttpSession sessione = request.getSession();
				
				removeOggettiIndiceDaSessione(sessione);
				 				
				EnelDwhFinder finder = null;
				//boolean chiamataEsterna = false;
				if (sessione.getAttribute(NOMEFINDER) !=null){
					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new EnelDwhFinder().getClass()){
						finder = (EnelDwhFinder)sessione.getAttribute(NOMEFINDER);
					}
				}

				utenzaSel = request.getParameter("UTENZA_SEL");
				annoSel = request.getParameter("ANNO_SEL");

				if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");
				gestioneMultiRecord(request,response,azione,finder,sessione,EnelDwhLogic.LISTA_ENELDWH,(Vector)sessione.getAttribute(EnelDwhLogic.LISTA_ENELDWH),NOMEFINDER);
				if (azione.equals("")){
					oggettoSel="";recordScelto="";
					sessione.removeAttribute("BACK_JS_COUNT");
					sessione.removeAttribute("BACK_RECORD_ENABLE");

					if (request.getParameter("OGGETTO_SEL")!= null){
						oggettoSel = request.getParameter("OGGETTO_SEL");
					} else {
						// nel caso sia saltato dalla lista direttamente qui in dettaglio (chiamato da mCaricareLista)
						Vector ladwh = (Vector)sessione.getAttribute(EnelDwhLogic.LISTA_ENELDWH);
						ladwh.elementAt(0);
						oggettoSel = ((EnelBean) ladwh.elementAt(0)).getId();
						
					}
					if (request.getParameter("RECORD_SEL")!= null){
						recordScelto = request.getParameter("RECORD_SEL");
						if (finder!=null)
							finder.setRecordAttuale(new Long(recordScelto).longValue());
					} else {
						if (finder!=null)
							finder.setRecordAttuale(1);
					}


				
				/*
				 * carica il dettaglio
				 */
					EnelDwhLogic logic = new EnelDwhLogic(this.getEnvUtente(request));

				// CAROCO GLI ID E LA DATE DI STORICIZZAZIONE
				idStorici = logic.caricaIdStorici(oggettoSel);	
				Hashtable ht =null;
				if (tipo== 102 || tipo == 103 || tipo == 104|| tipo==105){
					ht=logic.mCaricareDettaglioEnelDaCivViaOgg(oggettoSel);
				}else
				 ht = logic.mCaricareDettaglioEnel(oggettoSel);

				
				EnelBean enel = (EnelBean)ht.get(EnelDwhLogic.ENELDWH);

				// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
				super.leggiScarti(request.getSession(), enel, request);

				sessione.setAttribute(EnelDwhLogic.ENELDWH, enel);
				sessione.setAttribute("UTENZA_SEL", utenzaSel);
				sessione.setAttribute("ANNO_SEL", annoSel);

				Vector listaUtenze = (Vector) ht.get(EnelDwhLogic.UTENZE);
				sessione.setAttribute(EnelDwhLogic.UTENZE, listaUtenze);

				Vector<OggettoIndice> cOggettiInd = new Vector<OggettoIndice>();
				Vector<OggettoIndice> oOggettiInd = new Vector<OggettoIndice>();
				Vector<OggettoIndice> fOggettiInd = new Vector<OggettoIndice>();
				
				ArrayList listaNomiSoggetti= new ArrayList();
				ArrayList listaNomiVie= new ArrayList();
				ArrayList listaNomiOggetti= new ArrayList();
				ArrayList listaNomiCivici= new ArrayList();
				ArrayList listaNomiFabbricati= new ArrayList();
			
				for (int i=0; i < listaUtenze.size(); i++) {
					EnelBean2 eb2 = (EnelBean2) listaUtenze.get(i);
					
					OggettoIndice oi1 = new OggettoIndice();
					oi1.setIdOrig(eb2.getId());
					oi1.setFonte("10");
					oi1.setProgr("2");
					oi1.setDescrizione(eb2.getIndirizzoUbicazione());			
					
					String nomeVia= oi1.getFonte()+oi1.getProgr()+oi1.getIdOrig();
					if (!listaNomiCivici.contains(nomeVia)){
						listaNomiCivici.add(nomeVia);
						cOggettiInd.add(oi1);
					}
					
					

					OggettoIndice oi2 = new OggettoIndice();
					oi2.setIdOrig(eb2.getId());
					oi2.setFonte("10");
					oi2.setProgr("2");
					oi2.setDescrizione("F:" + eb2.getFoglio() + " P:" + eb2.getParticella() + " S:" + eb2.getSubalterno());				
					
					
					String nomeOggetto= oi2.getFonte()+oi2.getProgr()+oi2.getIdOrig();
					if (!listaNomiOggetti.contains(nomeOggetto)){
						listaNomiOggetti.add(nomeOggetto);
						oOggettiInd.add(oi2);
					}
					
					OggettoIndice oi3 = new OggettoIndice();
					oi3.setIdOrig(eb2.getId());
					oi3.setFonte("10");
					oi3.setProgr("2");
					oi3.setDescrizione("SEZ:"+ (eb2.getSezione()!= null ? eb2.getSezione(): "-")+" F:" + eb2.getFoglio() + " P:" + eb2.getParticella() + " S:" + eb2.getSubalterno());				
					
					
					String nomeFabbricato= oi3.getFonte()+oi3.getProgr()+oi3.getIdOrig();
					if (!listaNomiFabbricati.contains(nomeFabbricato)){
						listaNomiFabbricati.add(nomeFabbricato);
						fOggettiInd.add(oi3);
					}
				}
				
				sessione.setAttribute("indice_civici", cOggettiInd);
				sessione.setAttribute("indice_oggetti", oOggettiInd);
				sessione.setAttribute("indice_fabbricati", fOggettiInd);

				
			
				// Aggiungo i valori per l'indice di correlazione
				Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
				
				
				OggettoIndice oi = new OggettoIndice();
				
				oi.setIdOrig(enel.getId());
				oi.setFonte("10");
				oi.setProgr("1");
				
				oi.setDescrizione(enel.getDenominazione());
				
				
				String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
				if (!listaNomiSoggetti.contains(nomeSoggetto)){
					listaNomiSoggetti.add(nomeSoggetto);
					sOggettiInd.add(oi);
				}
				
				sessione.setAttribute("indice_soggetti", sOggettiInd);
				
				Vector<OggettoIndice> vieOggettiInd = new Vector<OggettoIndice>();
				for (int i=0; i < listaUtenze.size(); i++) {
					EnelBean2 eb2 = (EnelBean2) listaUtenze.get(i);
					OggettoIndice oi1 = new OggettoIndice();
					oi1.setIdOrig(eb2.getId());
					oi1.setFonte("10");
					oi1.setProgr("2");
					oi1.setDescrizione(eb2.getIndirizzoUbicazione());				
					
					
					String nomeVia= oi1.getFonte()+oi1.getProgr()+oi1.getIdOrig();
					if (!listaNomiVie.contains(nomeVia)){
						listaNomiVie.add(nomeVia);
						vieOggettiInd.add(oi1);
					}
				}
				
				sessione.setAttribute("indice_vie", vieOggettiInd);
				
				nav = new PulsantiNavigazione();
				if (chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();
				

				this.chiamaPagina(request,response,"eneldwh/enelDwhFrame.jsp", nav);



			}
	}






	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (EnelDwhFinder)finder2;
		EnelDwhLogic logic = new EnelDwhLogic(this.getEnvUtente(request));
		return logic.mCaricareListaEnel(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder(){
		return new EnelDwhFinder();
	}
	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((EnelBean)listaOggetti.get(recordSuccessivo)).getId();
	}


	public String getTema() {
		return "Forniture Elettriche";
	}
	public String getTabellaPerCrossLink() {
		return "SIT_ENEL_UTENTE";
	}

	public String getLocalDataSource() {
		return localDataSource;
	}

}



