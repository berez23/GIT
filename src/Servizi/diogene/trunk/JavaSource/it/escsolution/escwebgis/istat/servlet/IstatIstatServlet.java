/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.istat.servlet;

import it.escsolution.eiv.JavaBeanGlobalVar;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.istat.bean.Istat;
import it.escsolution.escwebgis.istat.bean.IstatFinder;
import it.escsolution.escwebgis.istat.logic.IstatIstatLogic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IstatIstatServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private IstatIstatLogic logic = null;
	private final String NOMEFINDER = "FINDER13";
	private IstatFinder finder = null;

	public void EseguiServizio(
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
					 //pulireSessione(request);
					 mCaricareLista(request,response,false);
					 break;
				 case 3:
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

				HttpSession sessione = request.getSession();

				/** Comune */
				Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
				/***/

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

				// costruisce il vettore di elementi per la pagina di ricerca

				EscElementoFiltro elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Comune");
				elementoFiltro.setAttributeName("FK_COMUNE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringaRid);
				elementoFiltro.setListaValori(vctComuni);
				elementoFiltro.setCampoFiltrato("FK_COMUNI_IST");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Nome Sezione");
				elementoFiltro.setAttributeName("NOME_SEZIONE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				//elementoFiltro.setListaValori(vct1);
				elementoFiltro.setCampoFiltrato("NOMESEZIONE");
				listaElementiFiltro.add(elementoFiltro);


				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Nome Località");
				elementoFiltro.setAttributeName("NOME_LOCALITA");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("NOMELOCALITA");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Numero Sezione");
				elementoFiltro.setAttributeName("NUM_SEZIONE");
				elementoFiltro.setTipo("N");
				elementoFiltro.setCampoJS("numeroIntero");
				elementoFiltro.setListaOperatori(operatoriNumerici);
				elementoFiltro.setCampoFiltrato("NUMSEZIONE");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Residenti Totali");
				elementoFiltro.setAttributeName("POP_RESID_TOTALE");
				elementoFiltro.setTipo("N");
				elementoFiltro.setCampoJS("numeroIntero");
				elementoFiltro.setListaOperatori(operatoriNumerici);
				elementoFiltro.setCampoFiltrato("POPRESIDTOTALE");
				listaElementiFiltro.add(elementoFiltro);

				sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
				sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
				//chiamare la pagina di ricerca
				nav.chiamataRicerca();
				this.chiamaPagina(request,response,"istat/istFrame.jsp", nav);

			}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new IstatFinder().getClass()){
				finder = (IstatFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}


		finder = (IstatFinder)gestireMultiPagina(finder,request);

		IstatIstatLogic logic = new IstatIstatLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaIstat(vettoreRicerca,finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista


		Vector vct_lista_istat= (Vector)ht.get("LISTA_ISTAT");
		finder = (IstatFinder)ht.get("FINDER");

		sessione.setAttribute("LISTA_ISTAT",vct_lista_istat);

		if (!notListaPrincipale){
			finder = (IstatFinder)ht.get("FINDER");
			sessione.setAttribute(NOMEFINDER,finder);
		}


		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"istat/istFrame.jsp", nav);
	}



	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
			// prendere dalla request il parametrio identificativo dell'oggetto cliccato
			// eseguire la query per caricare l'oggetto selezionato dal db
			// chiamare la pagina di dettaglio

			String azione = "";
			HttpSession sessione = request.getSession();
			IstatFinder finder = null;
			//boolean chiamataEsterna = false;
			if (sessione.getAttribute(NOMEFINDER) !=null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new IstatFinder().getClass()){
					finder = (IstatFinder)sessione.getAttribute(NOMEFINDER);
				}
			}
			if (request.getParameter("AZIONE")!= null)
				azione = request.getParameter("AZIONE");
			gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_ISTAT",(Vector)sessione.getAttribute("LISTA_ISTAT"),NOMEFINDER);
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
			IstatIstatLogic logic = new IstatIstatLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareDettaglioIstat(oggettoSel);

			Istat ist = (Istat)ht.get("ISTAT");
			sessione.setAttribute("ISTAT",ist);

			if(chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();
		/*
			 * gestione mappa
			 *
			 */
			JavaBeanGlobalVar beanGlobale = logic.mCaricareDatiGrafici(oggettoSel);
			if (beanGlobale != null){
				nav.setMappa(true);
				sessione.setAttribute("MAPPA",beanGlobale);
			}
			// fine gestione mappa

			this.chiamaPagina(request,response,"istat/istFrame.jsp", nav);

			//EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);


		}


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
			return ((Istat)listaOggetti.get(recordSuccessivo)).getCodSezione();
			}

		public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
			finder = (IstatFinder)finder2;
			IstatIstatLogic logic = new IstatIstatLogic(this.getEnvUtente(request));
			return logic.mCaricareListaIstat(this.vettoreRicerca,finder);
			}

		public EscFinder getNewFinder(){
					return new IstatFinder();
				}

		public String getTema() {
			return "Istat";
		}

	public String getTabellaPerCrossLink() {
			return "IST_CENSIMENTO91";
		}
}
