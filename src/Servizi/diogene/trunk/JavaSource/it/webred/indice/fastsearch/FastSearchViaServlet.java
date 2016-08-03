package it.webred.indice.fastsearch;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.escsolution.escwebgis.catasto.logic.CatastoTerreniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.toponomastica.logic.ToponomasticaCiviciLogic;
import it.webred.GlobalParameters;
import it.webred.indice.Fonte;
import it.webred.indice.fastsearch.bean.IndiceBean;
import it.webred.indice.fastsearch.bean.TipoRicercaBean;

import it.webred.indice.fastsearch.soggetto.bean.SoggTotaleBean;
import it.webred.indice.fastsearch.soggetto.logic.FastSearchLogic;
import it.webred.indice.fastsearch.via.bean.FastSearchViaFinder;
import it.webred.indice.fastsearch.via.bean.ViaTotaleBean;
import it.webred.indice.fastsearch.via.logic.FastSearchViaLogic;

public class FastSearchViaServlet extends GenericFastSearchServlet
{
	
	private	PulsantiNavigazione nav;

   public void init(ServletConfig config) throws ServletException {
	        super.init(config);
    }

 

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession sessione = request.getSession();
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));	
		
		super.EseguiServizio(request, response);
			 try{

				 if (request.getParameter("listavie") != null){
					 boolean unico = "SOLO_RIF".equals(request.getParameter("solorif"));
					 
		
					 if(unico)
						 mListaVie(request, response, "SIT_VIA_UNICO", "SEDIME", "INDIRIZZO", "NOME");
					 else
						 mListaVie(request, response, "SIT_VIA_TOTALE","SEDIME", "INDIRIZZO", "NOME");
				 }else {
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
			
			FastSearchViaLogic logic = new FastSearchViaLogic(this.getEnvUtente(request));
			Map map = logic.mCaricareDatiFormRicerca();
			
			Vector vectorSedime = null;
			request.setAttribute(FastSearchViaLogic.LISTA_SEDIME, (vectorSedime = new Vector((Collection) map.get(FastSearchViaLogic.LISTA_SEDIME))));

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
			
			Vector operatoriStringaRid = new Vector();
			operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

			// Aggiungo i campi di ricerca
			
			listaElementiFiltro.add(getFiltroTipoRicerca());   //Tipo Ricerca
			
			EscElementoFiltro elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Sedime");
			elementoFiltro.setAttributeName("SEDIME");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setListaValori(vectorSedime);
			elementoFiltro.setCampoFiltrato("SEDIME");
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
		    elementoFiltro.setLabel("Nome");
			elementoFiltro.setAttributeName("NOME");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("INDIRIZZO");
			elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/FastSearchVie?listavie='+document.getElementById('NOME').value +'&solorif='+document.getElementById('SOLO_RIF').value+'&sedime='+document.getElementById('SEDIME').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
			listaElementiFiltro.add(elementoFiltro);
			
			sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
			sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));

			nav = new PulsantiNavigazione();
			nav.chiamataRicerca();

			this.chiamaPagina(request,response,"fastsearch/via/fastSearchViaFrame.jsp", nav);

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
			
			FastSearchViaFinder finder = null;
			
			if (origine != null && !origine.trim().equalsIgnoreCase("")){
				if (vettoreRicerca == null)
					vettoreRicerca = new Vector();
				vettoreRicerca.add(origine);
			}
			if (sessione.getAttribute("FINDER501")!= null) {
				if (((Object)sessione.getAttribute("FINDER501")).getClass() == new FastSearchViaFinder().getClass()){
					finder = (FastSearchViaFinder) sessione.getAttribute("FINDER501");
				}
				else
					finder = null;
			}
			

			finder = (FastSearchViaFinder) gestireMultiPagina(finder,request);

			FastSearchViaLogic logic = new FastSearchViaLogic(this.getEnvUtente(request));
			Hashtable ht = null;
			
			boolean unico = verificaSearchSoloUnico();
			
			if(unico)
				ht = logic.mCaricareListaSoloUnico(vettoreRicerca,finder);
			else
				ht = logic.mCaricareLista(vettoreRicerca,finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista


		    Vector vct_lista_via = (Vector)ht.get("lista_via_tot");
			finder = (FastSearchViaFinder)ht.get("FINDER501");

			sessione.setAttribute("indice.lista_via", vct_lista_via);
			sessione.setAttribute("FINDER501", finder);

			sessione.setAttribute("KEYSTR", null);
			sessione.setAttribute("queryForKeyStr", null);

		
			nav = new PulsantiNavigazione();
			nav.chiamataInternaLista();
			
			this.chiamaPagina(request,response,"fastsearch/via/fastSearchViaFrame.jsp",nav);
			
		}
		
		
		private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
			FastSearchViaLogic logic = new FastSearchViaLogic(this.getEnvUtente(request));
			HashMap<String, Fonte> fonteDescr = logic.getFonti();
			
			HashMap<String, IndiceBean> oggettiMap = logic.getOggettoFonti(request.getParameter("OGGETTO_SEL"), fonteDescr);

			ViaTotaleBean via = logic.getVia(request.getParameter("OGGETTO_SEL"));
			request.getSession().setAttribute("oggettiMap", oggettiMap);
			request.getSession().setAttribute("fonteDescr", fonteDescr);
			request.getSession().setAttribute("viaIndiceUnico", via);
			
			
			nav = new PulsantiNavigazione();
			this.chiamaPagina(request,response,"fastsearch/via/fastSearchViaFrame.jsp",nav);

		}
		
		public EscFinder getNewFinder(){
			return new FastSearchViaFinder();
		}
		
		public String getTema() {
			return "Ricerca veloce vie";
		}
		

		public String getLocalDataSource() {
			return "jdbc/Diogene_DS";
		}
		
		
}
