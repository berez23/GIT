package it.webred.indice.fastsearch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.tributiNew.bean.TitoloSoggetto;
import it.webred.GlobalParameters;
import it.webred.indice.Fonte;
import it.webred.indice.fastsearch.bean.IndiceBean;
import it.webred.indice.fastsearch.bean.TipoRicercaBean;
import it.webred.indice.fastsearch.soggetto.bean.FastSearchFinder;
import it.webred.indice.fastsearch.soggetto.bean.SoggTotaleBean;
import it.webred.indice.fastsearch.soggetto.logic.FastSearchLogic;

public class FastSearchServlet extends GenericFastSearchServlet
{
	
	private	PulsantiNavigazione nav;

   public void init(ServletConfig config) throws ServletException {
	        super.init(config);
    }



	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
			super.EseguiServizio(request, response);
			
			request.getSession().setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
			 try{
				 switch (st){
					 case 1:
						 // carico la form di ricerca
						 pulireSessione(request);
						 mCaricareFormRicerca(request,response);
						 break;
					 case 2:
						 // carico la form di ricerca
						 mCaricareLista(request,response);
						 break;
					 case 3:
						 // carico la form di ricerca
						 mCaricareDettaglio(request,response);
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
		
		private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception
		{
			// caricare dati che servono nella pagina di ricerca
			HttpSession sessione = request.getSession();

			Vector listaElementiFiltro = new Vector();
			
			Vector operatoriStringa = new Vector();
			operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
			operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));
			Vector operatoriLike = new Vector();
			operatoriLike.add(new EscOperatoreFiltro("like","contiene"));

			Vector operatoriNumerici = new Vector();
			operatoriNumerici.add(new EscOperatoreFiltro("=","="));
			operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
			operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
			operatoriNumerici.add(new EscOperatoreFiltro("<","<"));
			

			// Aggiungo i campi di ricerca

			listaElementiFiltro.add(getFiltroTipoRicerca());   //Tipo Ricerca
			
			EscElementoFiltro elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Codice Fiscale");
			elementoFiltro.setAttributeName("CODICE_FISCALE");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("CODFISC");
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
			elementoFiltro.setLabel("Denominazione");
			elementoFiltro.setAttributeName("DENOMINAZIONE");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("DENOMINAZIONE");
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("P. Iva");
			elementoFiltro.setAttributeName("P_IVA");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("PI");
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Data di nascita");
			elementoFiltro.setAttributeName("DATA_NASCITA");
			elementoFiltro.setTipo("D");
			elementoFiltro.setCampoJS("controllaData");
			elementoFiltro.setListaOperatori(operatoriNumerici);
			elementoFiltro.setCampoFiltrato("DT_NASCITA");
			listaElementiFiltro.add(elementoFiltro);

			sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
			sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));

			nav = new PulsantiNavigazione();
			nav.chiamataRicerca();

			this.chiamaPagina(request,response,"fastsearch/soggetto/fastSearchFrame.jsp", nav);

		}

		private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
			// prendere dalla request i parametri di ricerca impostati dall'utente
			HttpSession sessione = request.getSession();

			/*
			 * Questo parametro passato dalla InfoCivico.jsp serve a capire se la richiesta
			 * arriva dalla mappa (=dopo che si è cliccato sul civico) o dal filtro 
			 * della popolazione
			 */
			String keystr= request.getParameter("KEYSTR");
			String queryForKeyStr= request.getParameter("queryForKeyStr");
			String origine = request.getParameter("origine");
			String lastColor = request.getParameter("LAST_COLOR");
			String lastFamily = request.getParameter("LAST_FAMILY");
			
			FastSearchFinder finder = null;
			
			if (origine != null && !origine.trim().equalsIgnoreCase("")){
				if (vettoreRicerca == null)
					vettoreRicerca = new Vector();
				vettoreRicerca.add(origine);
			}
			if (sessione.getAttribute("FINDER500")!= null) {
				if (((Object)sessione.getAttribute("FINDER500")).getClass() == new FastSearchFinder().getClass()){
					finder = (FastSearchFinder) sessione.getAttribute("FINDER500");
				}
				else
					finder = null;
			}
			

			finder = (FastSearchFinder) gestireMultiPagina(finder,request);

			FastSearchLogic logic = new FastSearchLogic(this.getEnvUtente(request));
			Hashtable ht = null;
			
			boolean unico = verificaSearchSoloUnico();
			if(unico)
				ht = logic.mCaricareListaSoloUnico(vettoreRicerca,finder);
			else
				ht = logic.mCaricareLista(vettoreRicerca,finder);
			

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista


		    Vector vct_lista_sogg = (Vector)ht.get("lista_sogg_tot");
			finder = (FastSearchFinder)ht.get("FINDER500");

			sessione.setAttribute("indice.lista_sogg", vct_lista_sogg);
			sessione.setAttribute("FINDER500", finder);

			sessione.setAttribute("KEYSTR", null);
			sessione.setAttribute("queryForKeyStr", null);

		
			nav = new PulsantiNavigazione();
			nav.chiamataInternaLista();
			
			this.chiamaPagina(request,response,"fastsearch/soggetto/fastSearchFrame.jsp",nav);
			
		}
		
		
		private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
			FastSearchLogic logic = new FastSearchLogic(this.getEnvUtente(request));
			HashMap<String, Fonte> fonteDescr = logic.getFonti();
			
			HashMap<String, IndiceBean> oggettiMap = logic.getOggettoFonti(request.getParameter("OGGETTO_SEL"), fonteDescr);

			SoggTotaleBean sogg = logic.getSoggetto(request.getParameter("OGGETTO_SEL"));
			request.getSession().setAttribute("oggettiMap", oggettiMap);
			request.getSession().setAttribute("fonteDescr", fonteDescr);
			request.getSession().setAttribute("soggIndiceUnico", sogg);
			
			
			nav = new PulsantiNavigazione();
			this.chiamaPagina(request,response,"fastsearch/soggetto/fastSearchFrame.jsp",nav);

		}
		
		public EscFinder getNewFinder(){
			return new FastSearchFinder();
		}
		
		public String getTema() {
			return "Ricerca veloce";
		}
		

		public String getLocalDataSource() {
			return "jdbc/Diogene_DS";
		}
		
		
}
