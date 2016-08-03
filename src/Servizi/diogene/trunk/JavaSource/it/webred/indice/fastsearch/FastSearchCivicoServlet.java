package it.webred.indice.fastsearch;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletConfig;
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
import it.escsolution.escwebgis.toponomastica.logic.ToponomasticaCiviciLogic;
import it.webred.GlobalParameters;
import it.webred.indice.Fonte;
import it.webred.indice.fastsearch.bean.IndiceBean;
import it.webred.indice.fastsearch.bean.TipoRicercaBean;
import it.webred.indice.fastsearch.civico.bean.CivicoTotaleBean;
import it.webred.indice.fastsearch.civico.bean.FastSearchCivicoFinder;
import it.webred.indice.fastsearch.civico.logic.FastSearchCivicoLogic;

import it.webred.indice.fastsearch.soggetto.bean.SoggTotaleBean;
import it.webred.indice.fastsearch.soggetto.logic.FastSearchLogic;
import it.webred.indice.fastsearch.via.bean.FastSearchViaFinder;
import it.webred.indice.fastsearch.via.bean.ViaTotaleBean;
import it.webred.indice.fastsearch.via.logic.FastSearchViaLogic;

public class FastSearchCivicoServlet extends GenericFastSearchServlet
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
			
			FastSearchCivicoLogic logic = new FastSearchCivicoLogic(this.getEnvUtente(request));
			Map map = logic.mCaricareDatiFormRicerca();
			
			Vector vectorSedime = null;
			request.setAttribute(FastSearchCivicoLogic.LISTA_SEDIME, (vectorSedime = new Vector((Collection) map.get(FastSearchCivicoLogic.LISTA_SEDIME))));

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
			elementoFiltro.setCampoFiltrato("alias_via.SEDIME"); //ricerca su tutto: sit_via_totale
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
		    elementoFiltro.setLabel("Nome");
			elementoFiltro.setAttributeName("NOME");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("alias_via.INDIRIZZO"); //ricerca su tutto: sit_via_totale
			elementoFiltro.setObbligatorio(true);	
			elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/FastSearchCivici?listavie='+document.getElementById('NOME').value+'&solorif='+document.getElementById('SOLO_RIF').value+'&sedime='+document.getElementById('SEDIME').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Civico");
			elementoFiltro.setAttributeName("CIVICO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("lpad8_0");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("alias_civico.civ_liv1"); //ricerca su tutto: sit_civico_totale
			listaElementiFiltro.add(elementoFiltro);
	
			sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
			sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));

			nav = new PulsantiNavigazione();
			nav.chiamataRicerca();

			this.chiamaPagina(request,response,"fastsearch/civico/fastSearchCivicoFrame.jsp", nav);

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
			
			FastSearchCivicoFinder finder = null;
			
			if (origine != null && !origine.trim().equalsIgnoreCase("")){
				if (vettoreRicerca == null)
					vettoreRicerca = new Vector();
				vettoreRicerca.add(origine);
			}
			if (sessione.getAttribute("FINDER502")!= null) {
				if (((Object)sessione.getAttribute("FINDER502")).getClass() == new FastSearchCivicoFinder().getClass()){
					finder = (FastSearchCivicoFinder) sessione.getAttribute("FINDER502");
				}
				else
					finder = null;
			}
			

			finder = (FastSearchCivicoFinder) gestireMultiPagina(finder,request);

			FastSearchCivicoLogic logic = new FastSearchCivicoLogic(this.getEnvUtente(request));
			Hashtable ht = null;
			
			//verifico la valorizzazione  dei campi OBBLIGATORI (Civico)
			
			Enumeration enContr = vettoreRicerca.elements();
			boolean controlliOk= true;
			String msg= ((String)sessione.getAttribute("MSG")!= null? (String)sessione.getAttribute("MSG"):"" );
			int cont=0; 
			
			  	 while (enContr.hasMoreElements()) {
			  		 
			  		EscElementoFiltro el = (EscElementoFiltro)enContr.nextElement(); 
			  		if (el.isObbligatorio() ){
			  			if (el.getValore()== null || el.getValore().equals("")){
			  				msg= msg + el.getLabel() +" è un campo obbligatorio. ";
			  				sessione.setAttribute("MSG", msg);
			  				controlliOk=false;
			  			}
			  		}
			  	 }
			  	Vector vct_lista_civico= new Vector();
			  	 
				if (controlliOk){
					
					boolean unico = verificaSearchSoloUnico();
					
					if(unico)
						ht = logic.mCaricareListaSoloUnico(vettoreRicerca,finder);
					else
						ht = logic.mCaricareLista(vettoreRicerca,finder);
					
					
					// eseguire la query e caricare il vettore con i risultati
					// chiamare la pagina di lista
				     vct_lista_civico = (Vector)ht.get("lista_civico_tot");
				     finder = (FastSearchCivicoFinder)ht.get("FINDER502");
				}else{
					//rimango nella pagina di ricerca
					//mCaricareFormRicerca(request,response);
					//return;
					request.setAttribute("ST", "1");
					super.getCommonPars(request, response);
					mCaricareFormRicerca( request,  response);
					return;
				}
			
			

			sessione.setAttribute("indice.lista_civico", vct_lista_civico);
			sessione.setAttribute("FINDER502", finder);

			sessione.setAttribute("KEYSTR", null);
			sessione.setAttribute("queryForKeyStr", null);

		
			nav = new PulsantiNavigazione();
			nav.chiamataInternaLista();
			
			this.chiamaPagina(request,response,"fastsearch/civico/fastSearchCivicoFrame.jsp",nav);
			
		}
		
		
		private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
			FastSearchCivicoLogic logic = new FastSearchCivicoLogic(this.getEnvUtente(request));
			HashMap<String, Fonte> fonteDescr = logic.getFonti();
			
			HashMap<String, IndiceBean> oggettiMap = logic.getOggettoFonti(request.getParameter("OGGETTO_SEL"), fonteDescr);

			CivicoTotaleBean civico = logic.getCivico(request.getParameter("OGGETTO_SEL"));
			request.getSession().setAttribute("oggettiMap", oggettiMap);
			request.getSession().setAttribute("fonteDescr", fonteDescr);
			request.getSession().setAttribute("civicoIndiceUnico", civico);
			
			
			nav = new PulsantiNavigazione();
			this.chiamaPagina(request,response,"fastsearch/civico/fastSearchCivicoFrame.jsp",nav);

		}
		
		public EscFinder getNewFinder(){
			return new FastSearchCivicoFinder();
		}
		
		public String getTema() {
			return "Ricerca veloce civici";
		}
		

		public String getLocalDataSource() {
			return "jdbc/Diogene_DS";
		}
		
		
}
