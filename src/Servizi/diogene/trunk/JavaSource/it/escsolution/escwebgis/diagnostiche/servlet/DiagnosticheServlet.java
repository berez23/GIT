package it.escsolution.escwebgis.diagnostiche.servlet;

import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.diagnostiche.bean.Diagnostiche;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheFinder;
import it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheLogic;
import it.webred.DecodificaPermessi;
import it.webred.GlobalParameters;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DiagnosticheServlet extends EscServlet implements EscService
{

	private String				recordScelto;

	private DiagnosticheLogic	logic		= null;

	public static final String	NOMEFINDER	= "FINDER101";

	private DiagnosticheFinder	finder		= null;

	PulsantiNavigazione			nav			= new PulsantiNavigazione();

	public void init(ServletConfig config)
		throws ServletException
	{
		super.init(config);
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
		// caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();
		DiagnosticheLogic logic = new DiagnosticheLogic(this.getEnvUtente(request));
		sessione.setAttribute(DiagnosticheLogic.RICERCA_DIAGNOSTICHE, logic.caricaDatiRicerca(request));
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "diagnostiche/diagnosticheFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		// caricare dati che servono nella lista
		HttpSession sessione = request.getSession();
		DiagnosticheLogic logic = new DiagnosticheLogic(this.getEnvUtente(request));
		Hashtable<String,String> codDescCommandHt = logic.getCodDescCommand(sessione.getAttribute(DiagnosticheLogic.RICERCA_DIAGNOSTICHE), request.getParameter("cmd"));
		sessione.setAttribute(DiagnosticheLogic.COD_COMMAND_SCELTO, codDescCommandHt.get(DiagnosticheLogic.COD_COMMAND_SCELTO));
		sessione.setAttribute(DiagnosticheLogic.DESC_COMMAND_SCELTO, codDescCommandHt.get(DiagnosticheLogic.DESC_COMMAND_SCELTO));
		LinkedHashMap<String, String> lisDia = logic.caricaListaDiagnostiche(request);
		sessione.setAttribute(DiagnosticheLogic.LISTA_DIAGNOSTICHE, lisDia);	
		String idComLau = request.getParameter("idComLau");
		if (idComLau == null || idComLau.equals("")) {
			//in questo caso (primo caricamento) prendo il default (ultimo valore)
			Iterator it = lisDia.keySet().iterator();
			while (it.hasNext()) {			
				idComLau = (String)it.next();
			}
		}
		sessione.setAttribute(DiagnosticheLogic.ID_COMMAND_LAUNCH, idComLau);
		String msgRighe = logic.getMsgRighe(request.getParameter("IDTEMPL"), idComLau);
		sessione.setAttribute(DiagnosticheLogic.MSG_RIGHE_VIS, msgRighe);
		List<LinkedHashMap<String,String>> lisStoDia = logic.caricaStoricoDiagnostiche(request.getParameter("IDTEMPL"), idComLau, msgRighe.equals(""), false);
		sessione.setAttribute(DiagnosticheLogic.LISTA_STORICO_DIAGNOSTICHE, lisStoDia);
		int righePerPagina = GlobalParameters.RIGHE_CONFIGURATE_PER_PAGINA.get(this.getEnvUtente(request).getEnte()) == null ? GlobalParameters.RIGHE_CONFIGURATE_PER_PAGINA_DEF : GlobalParameters.RIGHE_CONFIGURATE_PER_PAGINA.get(this.getEnvUtente(request).getEnte()).intValue();
		int totPagine = (lisStoDia.size() - (lisStoDia.size() % righePerPagina)) / righePerPagina;
		totPagine += (lisStoDia.size() % righePerPagina == 0 ? 0 : 1);
		if (totPagine == 0) {
			totPagine = 1;
		}
		sessione.setAttribute(DiagnosticheLogic.TOT_PAGINE, totPagine + "");
		List<LinkedHashMap<String,String>> lisStoDiaPag = logic.caricaStoricoDiagnostichePagina(lisStoDia, request.getParameter("pagina"));
		sessione.setAttribute(DiagnosticheLogic.LISTA_STORICO_DIAGNOSTICHE_PAG, lisStoDiaPag);	
		// chiamare la lista
		nav.chiamataInternaLista(); //verificare
		this.chiamaPagina(request, response, "diagnostiche/diagnosticheList.jsp", nav);
	}
	
	private void mEsportare(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		// esportazione dati in formato CSV
		HttpSession sessione = request.getSession();
		DiagnosticheLogic logic = new DiagnosticheLogic(this.getEnvUtente(request));
		List<LinkedHashMap<String,String>> lisStoDiaExp = logic.caricaStoricoDiagnostiche(request.getParameter("IDTEMPL"),
																						(String)sessione.getAttribute(DiagnosticheLogic.ID_COMMAND_LAUNCH),
																						true,
																						true);
		logic.esportaDati(response,
						(String)sessione.getAttribute(DiagnosticheLogic.COD_COMMAND_SCELTO),
						(String)sessione.getAttribute(DiagnosticheLogic.DESC_COMMAND_SCELTO),
						(String)sessione.getAttribute(DiagnosticheLogic.ID_COMMAND_LAUNCH),
						(LinkedHashMap)sessione.getAttribute(DiagnosticheLogic.LISTA_DIAGNOSTICHE),
						lisStoDiaExp);
	}

	public EscFinder getNewFinder()
	{
		return new DiagnosticheFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((Diagnostiche) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Diagnostiche";
	}

	public String getPermissionItem()
	{
		return DecodificaPermessi.ITEM_DIAGNOSTICHE;
	}

	public String getLocalDataSource()
	{
		return "jdbc/Diogene_DS";
	}
}
