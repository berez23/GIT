package it.escsolution.escwebgis.diagnostiche.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheTarsu;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheTarsuFinder;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheTarsuTot;
import it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheTarsuLogic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class DiagnosticheTarsuServlet extends EscServlet implements EscService
{


	private String				recordScelto;

	private DiagnosticheTarsuLogic	logic		= null;

	public static final String	NOMEFINDER	= "FINDER104";

	private DiagnosticheTarsuFinder	finder		= null;

	PulsantiNavigazione			nav			= new PulsantiNavigazione();
	
	String pathModelloXls = null; 
	

   public void EseguiServizio(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,
		IOException
	{

		super.EseguiServizio(request, response);
		try
		{

			switch (st)
			{
				case 1:
					pulireSessione(request);
					mCaricareDiagnostiche(request, response);
					break;
				case 2:
					mCaricareListaPropRes(request, response);
					break;
				case 6:
					mEsportare(request, response);
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

   private void mCaricareDiagnostiche(HttpServletRequest request, HttpServletResponse response)
		throws Exception
   {
	   HttpSession sessione = request.getSession();
	   
	   DiagnosticheTarsuLogic logic = new DiagnosticheTarsuLogic(this.getEnvUtente(request));
	   Hashtable ht = logic.mCaricareDiagnostichePricipali();
	   
	   DiagnosticheTarsuTot beanDia = (DiagnosticheTarsuTot)ht.get(DiagnosticheTarsuLogic.BEAN_DIAGNOSTICHE);
	   sessione.setAttribute(DiagnosticheTarsuLogic.BEAN_DIAGNOSTICHE,beanDia);
	   
	   nav.chiamataRicerca();
	
	   this.chiamaPagina(request, response, "diagnostiche/diagnosticheTarsuFrame.jsp", nav);
   }
 
	private void mCaricareListaPropRes(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new DiagnosticheTarsuFinder().getClass()){
				finder = (DiagnosticheTarsuFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}
		
		finder = (DiagnosticheTarsuFinder)gestireMultiPagina(finder,request);

		DiagnosticheTarsuLogic logic = new DiagnosticheTarsuLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaPropRes(vettoreRicerca,finder);
		
		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista
		
		Vector vct_lista= (Vector)ht.get(DiagnosticheTarsuLogic.LISTA_DIAGNOSTICHE_PROP_RES);
		finder = (DiagnosticheTarsuFinder)ht.get("FINDER");
		
		sessione.setAttribute(DiagnosticheTarsuLogic.LISTA_DIAGNOSTICHE_PROP_RES,vct_lista);
		sessione.setAttribute(NOMEFINDER,finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		nav.setPrimo(false);
		

		this.chiamaPagina(request, response, "diagnostiche/diagnosticheTarsuFrame.jsp", nav);
		
		

	}
	
	private void mEsportare(HttpServletRequest request, HttpServletResponse response)
	throws Exception
{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();
		
	
		DiagnosticheTarsuLogic logic = new DiagnosticheTarsuLogic(this.getEnvUtente(request));

		String controllo = logic.esportaDati(response);
		if (controllo != null){
			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();
			nav.setPrimo(false);
			
			st = 2;
			
			sessione.setAttribute(DiagnosticheTarsuLogic.SMS_DIAGNOSTICHE_RES,controllo);

			this.chiamaPagina(request, response, "diagnostiche/diagnosticheDocfaFrame.jsp", nav);
		}
		
}
	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (DiagnosticheTarsuFinder)finder2;
		DiagnosticheTarsuLogic logic = new DiagnosticheTarsuLogic(this.getEnvUtente(request));
		return logic.mCaricareListaPropRes(this.vettoreRicerca, finder);
	}
	
	public EscFinder getNewFinder()
	{
		return new DiagnosticheTarsuFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((DiagnosticheTarsu) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Diagnostiche Tarsu";
	}

}
