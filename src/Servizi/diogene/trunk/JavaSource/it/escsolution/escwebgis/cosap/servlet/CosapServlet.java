/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.cosap.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscObject;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.cosap.bean.CosapBean;
import it.escsolution.escwebgis.cosap.bean.ElementoListaFinder;
import it.escsolution.escwebgis.cosap.logic.CosapLogic;

import java.io.IOException;
import java.util.Hashtable;
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
public class CosapServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private final String NOMEFINDER = "FINDER49";
	private ElementoListaFinder finder = null;

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
					 mCaricareLista(request,response,false);
					 break;
				 case 3:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					this.leggiCrossLink(request);
					 break;
				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					this.leggiCrossLink(request);
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
					CosapLogic logic = new CosapLogic(this.getEnvUtente(request));
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
					operatoriStringaRid.add(new EscOperatoreFiltro("like","contiene"));

					// costruisce il vettore di elementi per la pagina di ricerca

					EscElementoFiltro elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("CONTRIBUENTE");
					elementoFiltro.setSoloLabel(true);
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Cognome");
					elementoFiltro.setAttributeName("COGNOME");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("COGNOME");
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
					elementoFiltro.setLabel("Codice Fiscale");
					elementoFiltro.setAttributeName("VS_COD_FIS");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("VS_COD_FIS");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Partita Iva");
					elementoFiltro.setAttributeName("VS_PAR_IVA");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("VS_PAR_IVA");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Indirizzo");
					elementoFiltro.setAttributeName("DES_VIA");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("DES_VIA");
					listaElementiFiltro.add(elementoFiltro);
					
					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Numero Civico");
					elementoFiltro.setAttributeName("AC_NUM_CIV");
					elementoFiltro.setTipo("N");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("AC_NUM_CIV");
					listaElementiFiltro.add(elementoFiltro);
					
					

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Autorizzazione");
					elementoFiltro.setSoloLabel(true);
					listaElementiFiltro.add(elementoFiltro);


					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Numero Protocollo");
					elementoFiltro.setAttributeName("A_NUMERO_PROTOCOLLO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("A_NUMERO_PROTOCOLLO");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Anno Protocollo");
					elementoFiltro.setAttributeName("A_ANNO_PROTOCOLLO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("A_ANNO_PROTOCOLLO");
					listaElementiFiltro.add(elementoFiltro);

					
					Vector vct1  = logic.mCaricareTipoOccupazione(logic.AUTORIZZAZIONI);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Tipo Occupazione");
					elementoFiltro.setAttributeName("A_TIPO_OCCUPAZIONE");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setListaValori(vct1);
					elementoFiltro.setCampoFiltrato("A_TIPO_OCCUPAZIONE");
					listaElementiFiltro.add(elementoFiltro);
					
					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Diffida");
					elementoFiltro.setSoloLabel(true);
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Numero Provvedimento");
					elementoFiltro.setAttributeName("D_NUMERO_PROVVEDIMENTO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("D_NUMERO_PROVVEDIMENTO");
					listaElementiFiltro.add(elementoFiltro);

					
					Vector vct2  = logic.mCaricareTipoOccupazione(logic.DIFFIDE);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Tipo Occupazione");
					elementoFiltro.setAttributeName("D_TIPO_OCCUPAZIONE");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setListaValori(vct2);
					elementoFiltro.setCampoFiltrato("D_TIPO_OCCUPAZIONE");
					listaElementiFiltro.add(elementoFiltro);

					sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
					sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
					
					//chiamare la pagina di ricerca
					nav.chiamataRicerca();
					this.chiamaPagina(request,response,"cosap/cosapFrame.jsp", nav);

				}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
			// prendere dalla request i parametri di ricerca impostati dall'utente
			HttpSession sessione = request.getSession();

			if (sessione.getAttribute(NOMEFINDER)!= null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ElementoListaFinder().getClass()){
					finder = (ElementoListaFinder)sessione.getAttribute(NOMEFINDER);
				}
				else
					finder = null;
			}


			finder = (ElementoListaFinder )gestireMultiPagina(finder,request);

			CosapLogic logic = new CosapLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista


			Vector vct_lista_note= (Vector)ht.get(CosapLogic.LISTA_DATI_COSAP);
			finder = (ElementoListaFinder)ht.get(CosapLogic.FINDER);

			sessione.setAttribute(CosapLogic.LISTA_DATI_COSAP,vct_lista_note);

			if (!notListaPrincipale){
				finder = (ElementoListaFinder)ht.get(CosapLogic.FINDER);
				sessione.setAttribute(NOMEFINDER,finder);
			}


			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();

			this.chiamaPagina(request,response,"cosap/cosapFrame.jsp", nav);
		}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				
				HttpSession sessione = request.getSession();
				
				removeOggettiIndiceDaSessione(sessione);
				ElementoListaFinder finder = null;
				//boolean chiamataEsterna = false;
				if (sessione.getAttribute(NOMEFINDER) !=null){
					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ElementoListaFinder().getClass()){
						finder = (ElementoListaFinder)sessione.getAttribute(NOMEFINDER);
					}
				}
				if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");
				gestioneMultiRecord(request,response,azione,finder,sessione,CosapLogic.LISTA_DATI_COSAP ,(Vector)sessione.getAttribute(CosapLogic.LISTA_DATI_COSAP),NOMEFINDER);
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
				CosapLogic logic = new CosapLogic(this.getEnvUtente(request));
				Hashtable ht = logic.mCaricareDettaglio(oggettoSel);

				CosapBean cb = (CosapBean)ht.get(CosapLogic.DETT_COSAP);
				// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
				final String chiave = cb.getChiave();
				super.leggiScarti(request.getSession(), new EscObject() {
					public String getChiave()
					{
						return chiave;
					}
				}, "COSAP", request);

				sessione.setAttribute(CosapLogic.DETT_COSAP,cb);

				if(chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();


				this.chiamaPagina(request,response,"cosap/cosapFrame.jsp", nav);

				EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);


			}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
				return ((CosapBean)listaOggetti.get(recordSuccessivo)).getChiave();
				}

			public EscFinder getNewFinder(){
						return new ElementoListaFinder();
					}

			public String getTema() {
				return "Cosap";
			}

		public String getTabellaPerCrossLink() {
			return "SIT_V_COSAP";
			}
}

