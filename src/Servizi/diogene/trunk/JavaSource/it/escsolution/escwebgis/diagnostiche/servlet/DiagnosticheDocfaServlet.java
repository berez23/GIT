package it.escsolution.escwebgis.diagnostiche.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheDocfa;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheDocfaFinder;
import it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheDocfaLogic;

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


public class DiagnosticheDocfaServlet extends EscServlet implements EscService
{


	private String				recordScelto;

	private DiagnosticheDocfaLogic	logic		= null;

	public static final String	NOMEFINDER	= "FINDER102";

	private DiagnosticheDocfaFinder	finder		= null;

	PulsantiNavigazione			nav			= new PulsantiNavigazione();
	
	//String pathModelloXls = null; 
	
	String pathDatiDiogene = "";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pathDatiDiogene = super.getPathDatiDiogene();
    }
	

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
					mCaricareFormRicerca(request, response);
					break;
				case 2:
					mCaricareLista(request, response);
					break;
				case 3:
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

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		//caricare dati che servono nella pagina di ricerca
		DiagnosticheDocfaLogic logic = new DiagnosticheDocfaLogic(this.getEnvUtente(request));
		Vector vctForn = logic.elencoForniture();
		
		HttpSession sessione = request.getSession();


		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));



		Vector operatoriNumericiMax = new Vector();
		operatoriNumericiMax.add(new EscOperatoreFiltro(">=",">="));
		
		Vector operatoriNumericiMin = new Vector();
		operatoriNumericiMin.add(new EscOperatoreFiltro("<=","<="));
		// costruisce il vettore di elementi per la pagina di ricerca

		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DA Fornitura");
		elementoFiltro.setAttributeName("FORNITURA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controlloData");
		elementoFiltro.setListaOperatori(operatoriNumericiMax);
		elementoFiltro.setListaValori(vctForn);
		elementoFiltro.setCampoFiltrato("FORNITURA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("A Fornitura");
		elementoFiltro.setAttributeName("FORNITURA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controlloData");
		elementoFiltro.setListaOperatori(operatoriNumericiMin);
		elementoFiltro.setListaValori(vctForn);
		elementoFiltro.setCampoFiltrato("FORNITURA");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		//chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "diagnostiche/diagnosticheDocfaFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new DiagnosticheDocfaFinder().getClass()){
				finder = (DiagnosticheDocfaFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}
		
		if( sessione.getAttribute(DiagnosticheDocfaLogic.DA_DATA_FORNITURA) == null
			&& sessione.getAttribute(DiagnosticheDocfaLogic.A_DATA_FORNITURA) == null	){
		///metto in sessione le date di ricerca
		String daData= "";
		String aData = "";
		//memorizzo intervallo date
		if (request.getParameter("NUMERO_FILTRI")!= null){
			int massimo = (new Integer(request.getParameter("NUMERO_FILTRI"))).intValue();
			String daVal = request.getParameter("VALORE_0");
			if (daVal!= null && !daVal.equals(""))
				daData=daVal; 
			String aVal = request.getParameter("VALORE_1");
			if (aVal!= null && !aVal.equals(""))
				aData=aVal;
		}
		
		sessione.setAttribute(DiagnosticheDocfaLogic.DA_DATA_FORNITURA, daData);
		sessione.setAttribute(DiagnosticheDocfaLogic.A_DATA_FORNITURA, aData);
		
		}

		finder = (DiagnosticheDocfaFinder)gestireMultiPagina(finder,request);

		DiagnosticheDocfaLogic logic = new DiagnosticheDocfaLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);
		
		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista
		
		Vector vct_lista= (Vector)ht.get(DiagnosticheDocfaLogic.LISTA_DIAGNOSTICHE);
		finder = (DiagnosticheDocfaFinder)ht.get("FINDER");
		
		Vector vct_cat = (Vector)ht.get(DiagnosticheDocfaLogic.LISTA_CATEGORIE);
		sessione.setAttribute(DiagnosticheDocfaLogic.LISTA_CATEGORIE, vct_cat);
		
		sessione.setAttribute(DiagnosticheDocfaLogic.LISTA_DIAGNOSTICHE,vct_lista);
		sessione.setAttribute(NOMEFINDER,finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		nav.setPrimo(false);
		

		this.chiamaPagina(request, response, "diagnostiche/diagnosticheDocfaFrame.jsp", nav);
		
		

	}
	
	private void mEsportare(HttpServletRequest request, HttpServletResponse response)
	throws Exception
{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();
		
		String daData = (String)sessione.getAttribute(DiagnosticheDocfaLogic.DA_DATA_FORNITURA);
		String aData = (String)sessione.getAttribute(DiagnosticheDocfaLogic.A_DATA_FORNITURA);
		
		String categoriaSel = request.getParameter("CATEGORIA_SEL");
	
		DiagnosticheDocfaLogic logic = new DiagnosticheDocfaLogic(this.getEnvUtente(request));

		//pathModelloXls = config.getServletContext().getInitParameter("pathModelloXls");
		
		String controllo = logic.esportaDati(pathDatiDiogene,response, daData, aData,categoriaSel);
		if (controllo != null){
			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();
			nav.setPrimo(false);
			
			st = 2;
			
			sessione.setAttribute(DiagnosticheDocfaLogic.SMS_DIAGNOSTICHE_RES,controllo);

			this.chiamaPagina(request, response, "diagnostiche/diagnosticheDocfaFrame.jsp", nav);
		}
}
	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (DiagnosticheDocfaFinder)finder2;
		DiagnosticheDocfaLogic logic = new DiagnosticheDocfaLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca, finder);
	}
	
	public EscFinder getNewFinder()
	{
		return new DiagnosticheDocfaFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((DiagnosticheDocfa) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Diagnostiche Docfa";
	}

}
