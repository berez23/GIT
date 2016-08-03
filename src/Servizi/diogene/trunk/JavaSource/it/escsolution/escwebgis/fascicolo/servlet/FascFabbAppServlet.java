/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.fascicolo.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.fascicolo.bean.FascFabbAppFinder;
import it.escsolution.escwebgis.fascicolo.logic.FascFabbAppLogic;
import it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class FascFabbAppServlet extends EscServlet implements EscService {
	
	PulsantiNavigazione nav = new PulsantiNavigazione();
	private FascFabbAppFinder finder = null;

	private String localDataSource = "jdbc/Diogene_DS";
	

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 *
		 */
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request, response);
		
		try
		{
			
			if (request.getParameter("cercaPerVia") != null) {
				mCercaPerVia(request, response);
				return;
			}
			
			switch (st)
			{
			case 1:
				// carico la form di ricerca
				pulireSessione(request);
				mCaricareFormRicerca(request, response, st);
				break;
			case 2:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response,false,st);
				break;
			case 3:
				mCaricareFormRicerca(request, response, st);
				break;
			case 4:
				mCaricareDettaglio(request,response,false,st);
				break;
			}
		}
		catch (it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(),ex);
		}

	}

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response, int st)
			throws Exception {

		// caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		
		if(st==1){
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Sezione");
			elementoFiltro.setAttributeName("SEZ");
			elementoFiltro.setTipo("S");
			elementoFiltro.setObbligatorio(true);
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Foglio");
			elementoFiltro.setAttributeName("FOGLIO");
			elementoFiltro.setTipo("N");
			elementoFiltro.setCampoJS("numeroIntero");
			elementoFiltro.setObbligatorio(true);
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			listaElementiFiltro.add(elementoFiltro);


			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Particella");
			elementoFiltro.setAttributeName("PARTICELLA");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("lpad5_0");
			elementoFiltro.setObbligatorio(true);
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			String url = request.getContextPath() + "/FascFabbApp?cercaPerVia=true";
			String onClick = "window.open('" + url + "','fascFabbCercaPerVia','width=640,height=480,status=yes,resizable=yes,scrollbars=yes')";
			elementoFiltro.setExtraHTML("<span><a class=\"TXTmainLabel\" style=\"cursor: pointer;\" onclick=\"" + onClick + "\">Cerca per via</a>");
			listaElementiFiltro.add(elementoFiltro);
			
		}else{
			sessione.setAttribute("FOGLIO", request.getParameter("FOGLIO"));
			sessione.setAttribute("PARTICELLA", request.getParameter("PARTICELLA"));
		}

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "fascicolo/fascFabbAppFrame.jsp", nav);

	}
	
	

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, boolean tipo, int st )throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();
		
		if (sessione.getAttribute(FascFabbAppLogic.FINDER)!= null){
			finder = (FascFabbAppFinder)sessione.getAttribute(FascFabbAppLogic.FINDER);
		}
		else{
			finder = null;
		}
	
		finder = (FascFabbAppFinder)gestireMultiPagina(finder,request);

		FascFabbAppLogic logic = new FascFabbAppLogic(this.getEnvUtente(request));
		
		String sezione="";
		String foglio = null;
		String particella = null;
	
		for(Object o : vettoreRicerca){
			EscElementoFiltro el = (EscElementoFiltro)o;
			if("SEZ".equals(el.getAttributeName()))
				sezione = el.getValore();
			if("FOGLIO".equals(el.getAttributeName()))
				foglio = el.getValore();
			if("PARTICELLA".equals(el.getAttributeName()))
				particella = el.getValore();
		}
		
		if(st==4 && (foglio==null || particella==null)){
			foglio = (String)request.getParameter("FOGLIO");
			particella = (String)request.getParameter("PARTICELLA");
		}
		
		//TODO:Recuperare il percoso del fascicolo fabbricato, passando i parametri
		
		Hashtable ht  = logic.getRisultatoRicerca(sezione,foglio, particella, finder);
		
		String url = (String)ht.get(FascFabbAppLogic.URL_FASC_FABB);
		//if(!url.startsWith("ERR#")){
		//	sessione.setAttribute(FascFabbAppLogic.URL_FASC_FABB, url.substring(url.indexOf('#')+1));
			sessione.setAttribute(FascFabbAppLogic.URL_FASC_FABB, url);
			sessione.setAttribute(FascFabbAppLogic.FINDER, ht.get(FascFabbAppLogic.FINDER));
		
			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();
			
			/*Vector vettoreRicercaNew = new Vector();
			for(Object o : vettoreRicerca){
				EscElementoFiltro el = (EscElementoFiltro)o;
				if(!"PWD_SISTER".equals(el.getAttributeName()))
					vettoreRicercaNew.add(o);
			}
			vettoreRicerca = vettoreRicercaNew;*/
		
		
			this.chiamaPagina(request, response, "fascicolo/fascFabbAppFrame.jsp", nav);
	/*}else{
			url = url.substring(url.indexOf('#')+1);
			response.setContentType("text/html");  
			response.getWriter().println("<script type=\"text/javascript\">");
			response.getWriter().println("alert('"+url+"');");
			response.getWriter().println("</script>");
			st=1;
		}*/
	
	}
	
	private void mCercaPerVia(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mode = request.getParameter("mode");
		HttpSession sessione = request.getSession();
		if (mode != null && !mode.equals("")) {
			FascFabbAppLogic logic = new FascFabbAppLogic(this.getEnvUtente(request));
			if (mode.equalsIgnoreCase("ricerca")) {
				sessione.removeAttribute(FascFabbAppLogic.RICERCA_LISTA_VIE);
				sessione.removeAttribute(FascFabbAppLogic.RICERCA_ID_VIA);
				sessione.removeAttribute(FascFabbAppLogic.RICERCA_LISTA_CIVICI);
				sessione.removeAttribute(FascFabbAppLogic.RICERCA_ID_CIVICO);
				sessione.removeAttribute(FascFabbAppLogic.RICERCA_LISTA_FP);
				String nomeVia = request.getParameter("nomeVia");
				sessione.setAttribute(FascFabbAppLogic.RICERCA_NOME_VIA, nomeVia);
				List<Sitidstr> listaVie = logic.getListaVie(nomeVia);
				sessione.setAttribute(FascFabbAppLogic.RICERCA_LISTA_VIE, listaVie);				
			}
			if (mode.equalsIgnoreCase("selVia")) {
				sessione.removeAttribute(FascFabbAppLogic.RICERCA_LISTA_CIVICI);
				sessione.removeAttribute(FascFabbAppLogic.RICERCA_ID_CIVICO);
				sessione.removeAttribute(FascFabbAppLogic.RICERCA_LISTA_FP);
				String idVia = request.getParameter("idVia");
				sessione.setAttribute(FascFabbAppLogic.RICERCA_ID_VIA, idVia);				
				List<Siticivi> listaCivici = logic.getListaCivici(idVia);
				sessione.setAttribute(FascFabbAppLogic.RICERCA_LISTA_CIVICI, listaCivici);
			}
			if (mode.equalsIgnoreCase("selCivico")) {
				sessione.removeAttribute(FascFabbAppLogic.RICERCA_LISTA_FP);
				String idCivico = request.getParameter("idCivico");
				sessione.setAttribute(FascFabbAppLogic.RICERCA_ID_CIVICO, idCivico);
				List<ParticellaKeyDTO> listaFp = logic.getListaFp(idCivico);
				sessione.setAttribute(FascFabbAppLogic.RICERCA_LISTA_FP, listaFp);
			}
		} else {
			sessione.removeAttribute(FascFabbAppLogic.RICERCA_NOME_VIA);
			sessione.removeAttribute(FascFabbAppLogic.RICERCA_LISTA_VIE);
			sessione.removeAttribute(FascFabbAppLogic.RICERCA_ID_VIA);
			sessione.removeAttribute(FascFabbAppLogic.RICERCA_LISTA_CIVICI);
			sessione.removeAttribute(FascFabbAppLogic.RICERCA_ID_CIVICO);
			sessione.removeAttribute(FascFabbAppLogic.RICERCA_LISTA_FP);
		}
		request.getRequestDispatcher("fascicolo/fascFabbAppCercaVia.jsp").forward(request, response);
	}
	
	
	private String doHttpUrlConnectionAction(String desiredUrl)
			  throws Exception
			  {
			    URL url = null;
			    BufferedReader reader = null;
			    StringBuilder stringBuilder;

			    try
			    {
			      // create the HttpURLConnection
			      url = new URL(desiredUrl);
			      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			      
			      // just want to do an HTTP GET here
			      connection.setRequestMethod("GET");
			      
			      // uncomment this if you want to write output to this url
			      //connection.setDoOutput(true);
			      
			      // give it 15 seconds to respond
			      connection.setReadTimeout(15*1000);
			      connection.connect();

			      // read the output from the server
			      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			      stringBuilder = new StringBuilder();

			      String line = null;
			      while ((line = reader.readLine()) != null)
			      {
			        stringBuilder.append(line + "\n");
			      }
			      return stringBuilder.toString();
			    }
			    catch (Exception e)
			    {
			      e.printStackTrace();
			      throw e;
			    }
			    finally
			    {
			      // close the reader; this can throw an exception too, so
			      // wrap it in another try/catch block.
			      if (reader != null)
			      {
			        try
			        {
			          reader.close();
			        }
			        catch (IOException ioe)
			        {
			          ioe.printStackTrace();
			        }
			      }
			    }
			  }
	

	public EscFinder getNewFinder(){
		return new FascFabbAppFinder();
	}

	public String getTema() {
		return "Fascicolo";
	}
	
	
	public String getLocalDataSource() {
		return localDataSource;
	}
	
	
}
