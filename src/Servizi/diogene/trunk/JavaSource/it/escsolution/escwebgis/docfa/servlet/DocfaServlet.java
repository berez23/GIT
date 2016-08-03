package it.escsolution.escwebgis.docfa.servlet;


import it.escsolution.escwebgis.catasto.bean.Immobile;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoPlanimetrieComma340Logic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.docfa.bean.Docfa;
import it.escsolution.escwebgis.docfa.bean.DocfaFinder;
import it.escsolution.escwebgis.docfa.logic.DocfaLogic;
import it.escsolution.escwebgis.soggetto.bean.TipoSoggetto;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.aggregator.elaborazioni.ElaborazioniService;
import it.webred.ct.data.access.aggregator.elaborazioni.dto.ControlloClassamentoConsistenzaDTO;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.indice.OggettoIndice;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




public class DocfaServlet extends EscServlet implements EscService
{

	private String recordScelto;

	private DocfaLogic logic = null;
	
	private String modalitaCalcoloValoreCommerciale = null;
	
	public static final String NOMEFINDER = "FINDER43";

	private DocfaFinder finder = null;

	PulsantiNavigazione nav =  new PulsantiNavigazione();
	

	String pathDatiDiogene = "";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pathDatiDiogene = super.getPathDatiDiogene();
    }
    
    public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
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
			 mCaricareDettaglio(request,response);					
			 break; 	
 		 case 102:			 		
			 mCaricareDettaglio(request,response);					
			 break; 			   		 
		 case 103:			 		
			 mCaricareDettaglio(request,response);	
			 break;
		 case 104:			 		
			 mCaricareDettaglio(request,response);								 
			 break; 	
		 case 105:			 		
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

	public void _EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 */
		// super.init(getServletConfig());
		// String aaa = getServletConfig().getServletName();
		super.EseguiServizio(request, response);
		try
		{
 
			if(request.getParameter("popupCens") != null)
				mPopupDatiCensuari(request, response);
			else if (request.getParameter("popup340") != null)
				mPopup340(request, response);
			else if (request.getParameter("popupCatasto") != null)
				mPopupCatasto(request, response);				
			else if (request.getParameter("popupGraf") != null)
				mPopupDatiGraffati(request, response);
			else if (request.getParameter("popupClasseAtt") != null)
				mPopupClasseAttesa(request, response);
			else if (request.getParameter("popupCategoriaClasseInParticella") != null)
				mPopupCategoriaClasseInParticella(request, response);			
			else if (request.getParameter("listavie") != null)
				mListaVie(request, response);	
			else if (request.getParameter("popupPrg") != null)
				mPopupPrg(request, response);
			else if (request.getParameter("popupConcessioni") != null)
				mPopupConcessioni(request, response);
			else if (request.getParameter("popup3DProspective") != null)
				mPopup3DProspective(request, response);
			else if (request.getParameter("popupDaCondono") != null)
				mPopupDaCondono(request, response);	
			else if(request.getParameter("popupLstAltreZc")!=null)
				mPopupListaClsAltreZc(request, response);
			else			
			switch (st)
			{
			case 1:
				// carico la form di ricerca
				pulireSessione(request);
				mCaricareFormRicerca(request, response);
				break;
			case 2:
				// vengo dalla pagina di ricerca --> predispongo la lista e la
				// passo alla pagina di lista
				// pulireSessione(request);
				mCaricareLista(request, response);
				break;
			case 3:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request, response);
				//this.leggiCrossLink(request);
				break;		
			case 33:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request, response);
				//this.leggiCrossLink(request);
				break;	
			/*
			 * case 4: // ho cliccato su un elemento --> visualizzo il dettaglio
			 * mCaricareDettaglio(request,response);
			 * this.leggiCrossLink(request); break; case 5: // ho cliccato su un
			 * elemento --> visualizzo il dettaglio
			 * mCaricareLista(request,response,true); break;
			 */
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

	private void mListaVie(HttpServletRequest request, HttpServletResponse response) throws IOException
	
	{
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setHeader("Expires", "Thu, 01 Jan 1970 01:00:00 CET");
		if(request.getParameter("listavie") != null && request.getParameter("listavie").trim().equals(""))
		{
			response.getWriter().println("Nessun criterio di ricerca impostato");
			return;
		}
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		List l = logic.cercaIndirizzi(request.getParameter("listavie"));
		if(l != null && l.size()>0)
		{
			StringBuffer s = new StringBuffer();
			s.append("<select name=\"listaNomiVie\" class=\"INPDBComboBox\" id=\"listaNomiVie\" onchange=\"document.getElementById('u.indir_toponimo').value=this.options[this.selectedIndex].value;document.getElementById('divListaVie').innerHTML='';\" > \n");
			Iterator i = l.iterator();
			s.append("<option value=\"\" >Seleziona</option>");
			while(i.hasNext())
			{
				String value = i.next().toString();
				s.append("<option value=\""+value+"\">"+value+"</option> \n");
			}			
			s.append("</select> \n");			
			response.getWriter().print(s.toString());
		}
		else
		{
			response.getWriter().println("Nessun indirizzo trovato");
			
		}
		
	}


	
	private void mPopupDatiCensuari(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		String identificativo_immobile = request.getParameter("im");
		String codice = request.getParameter("codice");

		if(
				(identificativo_immobile == null || identificativo_immobile.trim().equals("")) || 
				(codice == null || codice.trim().equals("")) 
		  )
			throw new Exception("dati mancanti");
			
		request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_CENSUARI, logic.mDatiCensuari(identificativo_immobile,codice));
		if(request.getParameter("f") != null && request.getParameter("m")!= null && request.getParameter("s") != null)
			request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_340, logic.mDati340(request.getParameter("f"), request.getParameter("m"), request.getParameter("s")));
		String  protocollo = codice.substring(0,codice.indexOf("|"));
		String fornitura = codice.substring(codice.indexOf("|")+1);
		Integer numP = logic.getNumPlanimetrie(protocollo, fornitura,identificativo_immobile);
		request.setAttribute(DocfaLogic.NUMERO_DI_PLANIMETRIE, numP);
		int planimetrieOrfane = ((Integer)logic.presenzaPlanimetrieSenzaImm(protocollo, fornitura,identificativo_immobile)).intValue();
		
		if(planimetrieOrfane >0 )
			request.setAttribute(DocfaLogic.PLANIMETRIE_SENZA_IMM_SOLO_REQUEST, planimetrieOrfane);
		
		CatastoPlanimetrieComma340Logic logic340 = new CatastoPlanimetrieComma340Logic(this.getEnvUtente(request));
		ArrayList<ArrayList<String>> planimetrieComma340 = logic340.getPlanimetrieComma340(request.getParameter("f"), request.getParameter("m"), request.getParameter("s"), pathDatiDiogene);
		request.setAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_DOCFA_CENS, planimetrieComma340);
		
		request.getRequestDispatcher("docfa/docfaPopupCens.jsp").forward(request,response);		
	}
	
	private void mPopup340(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		String f = request.getParameter("f");
		String m = request.getParameter("m");
		String s = request.getParameter("s");

		if(
				(f == null || f.trim().equals("")) || 
				(m == null || m.trim().equals("")) || 
				(s == null || s.trim().equals("")) 
		  )
			throw new Exception("dati mancanti");			
		request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_340, logic.mDati340(f, m, s));
		
		CatastoPlanimetrieComma340Logic logic340 = new CatastoPlanimetrieComma340Logic(this.getEnvUtente(request));
		ArrayList<ArrayList<String>> planimetrieComma340 = logic340.getPlanimetrieComma340(request.getParameter("f"), request.getParameter("m"), request.getParameter("s"), pathDatiDiogene);
		request.setAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_DOCFA, planimetrieComma340);
		
		request.getRequestDispatcher("docfa/docfaPopup340.jsp").forward(request,response);		
	}
	
	private void mPopupCatasto(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{

		String f = request.getParameter("f");
		String p = request.getParameter("p");
		String s = request.getParameter("s");
		String d = request.getParameter("d");
		String cod_ente = request.getParameter("cod_ente");

		if(
				(f == null || f.trim().equals("")) || 
				(p == null || p.trim().equals("")) || 
				(s == null || s.trim().equals("")) ||
				(d == null || d.trim().equals("")) ||
				(cod_ente == null || cod_ente.trim().equals("")) 
		  )
			throw new Exception("dati mancanti");
		
		if (s.equals("-"))
			s="0";
		
		String key = cod_ente+"|"+f+"|"+p+"| |"+s+"|"+d;
		
		CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioImmobile(key, "", pathDatiDiogene);
		request.getSession().setAttribute(CatastoImmobiliLogic.UNIMM, ht.get(CatastoImmobiliLogic.UNIMM));
		request.getSession().setAttribute(CatastoImmobiliLogic.LISTA_CIVICI, ht.get(CatastoImmobiliLogic.LISTA_CIVICI));
		request.getSession().setAttribute(CatastoImmobiliLogic.LISTA_CIVICI_CAT, ht.get(CatastoImmobiliLogic.LISTA_CIVICI_CAT));
		
		CatastoPlanimetrieComma340Logic logic340 = new CatastoPlanimetrieComma340Logic(this.getEnvUtente(request));
		Immobile imm = (Immobile)ht.get(CatastoImmobiliLogic.UNIMM);
		ArrayList<ArrayList<String>> planimetrieComma340 = logic340.getPlanimetrieComma340(imm.getFoglio(), imm.getNumero(), imm.getUnimm(), pathDatiDiogene);
		request.getSession().setAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_CIM, planimetrieComma340);

		this.chiamaPagina(request,response,"catasto/cimDetail.jsp",nav);	
	}		
	
	private void mPopupPrg(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		String foglio = request.getParameter("fg");
		String part = request.getParameter("part");
		String cod_ente = request.getParameter("cod_ente");

		if(
				(foglio == null || foglio.trim().equals("")) || 
				(part == null || part.trim().equals(""))	||
				(cod_ente == null || cod_ente.trim().equals("")) 
		  )
			throw new Exception("dati mancanti");
		
		request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_PRG, logic.mDatiPrg(cod_ente, foglio, part));
		request.getRequestDispatcher("docfa/docfaPopupPrg.jsp").forward(request,response);	
	}
	
	private void mPopupConcessioni(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		String foglio = request.getParameter("fg");
		String part = request.getParameter("part");
		String cod_ente = request.getParameter("cod_ente");

		if(
				(foglio == null || foglio.trim().equals("")) || 
				(part == null || part.trim().equals(""))	||
				(cod_ente == null || cod_ente.trim().equals("")) 
		  )
			throw new Exception("dati mancanti");
		
		request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_CONC, logic.mDatiConcessioni(cod_ente, foglio, part));
		request.getRequestDispatcher("docfa/docfaPopupConcessioni.jsp").forward(request,response);	
	}
	
	private void mPopupDatiGraffati(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		String foglio = request.getParameter("fg");
		String part = request.getParameter("part");
		String sub = request.getParameter("sub");
		

		if(
				(foglio == null || foglio.trim().equals("")) || 
				(part == null || part.trim().equals(""))	||
				(sub == null || sub.trim().equals(""))
		  )
			throw new Exception("dati mancanti");
			
		request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_GRAFFATI, logic.mDatiGraffati(foglio, part, sub,null));
		request.getRequestDispatcher("docfa/docfaPopupGraf.jsp").forward(request,response);		
	}
	
	
	
	
	
	private void mPopupListaClsAltreZc(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
			
		ElaborazioniService elabService= (ElaborazioniService)getEjb("CT_Service", "CT_Service_Data_Access", "ElaborazioniServiceBean");
		
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		
		RicercaOggettoDocfaDTO rd = new RicercaOggettoDocfaDTO();
		rd.setEnteId(super.getEnvUtente(request).getEnte());
		rd.setProtocollo(request.getParameter("protocollo"));
		rd.setFornitura(yyyyMMdd.parse(request.getParameter("fornitura")));
		rd.setFoglio(request.getParameter("foglio"));
		rd.setParticella(request.getParameter("particella"));
		rd.setUnimm(request.getParameter("sub"));
		/*
		 * usiamo il campo seguente per impostare il metodo di calcolo del valore
		 * commerciale deciso 
		 */
		rd.setTipoOperDocfa(this.getModalitaCalcoloValoreCommerciale());
		
		ControlloClassamentoConsistenzaDTO ccc = elabService.getControlliClassConsistenzaByDocfaUiu(rd);
		
		request.setAttribute("LISTA_CLASSAMENTO_ALTRE_ZC",ccc.getLstPerZcDiverse());
		request.getRequestDispatcher("docfa/docfaPopupClassamentoZc.jsp").forward(request,response);		
		
		
	}
	
	
	private void mPopupClasseAttesa(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{

		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		String valore = request.getParameter("valore");
		String vani = request.getParameter("vani");
		String zona = request.getParameter("zona");
		String rapporto = request.getParameter("rapporto");
		String consistenza = request.getParameter("consistenza");
		String categoria = request.getParameter("categoria");
		String classe = request.getParameter("classe");
		if(
				(valore == null || valore.trim().equals("")) || 
				(vani == null || vani.trim().equals(""))	||
				(rapporto == null || rapporto.trim().equals(""))	||
				(consistenza == null || consistenza.trim().equals(""))	||
				(categoria == null || categoria.trim().equals(""))	||
				(classe == null || classe.trim().equals(""))	||
				(zona == null || zona.trim().equals(""))
		  )
			throw new Exception("dati mancanti");
	
		try
		{			
			if(new Double(rapporto.replaceAll("[,]", ".")).doubleValue() <=0)
				throw new Exception("rapporto negativo");
			
		}
		catch(Exception eee)
		{
			throw new Exception("rapporto non valido");
		}		
		
		request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_CLASSE_ATTESTA, logic.mClasseAttesa(valore, vani, zona,rapporto,categoria,classe, request));
		request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_MEDIA_ATTESA,logic.mediaAttesa(valore,consistenza,rapporto));
		request.getRequestDispatcher("docfa/docfaPopupClasse.jsp").forward(request,response);		
	}
	private void mPopupCategoriaClasseInParticella(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		String oggettoSel = request.getParameter("OGGETTO_SEL");
		if((oggettoSel == null || oggettoSel.trim().equals("")) )
			throw new Exception("dati mancanti");
		request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_CATEGORIA_CLASSE_PARTICELLA,logic.getCategoriaClasseInParticella(oggettoSel));
		request.getRequestDispatcher("docfa/docfaPopupCategoriaClasseInParticella.jsp").forward(request,response);		
	}
	
	private void mPopupDaCondono(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		HttpSession sessione = request.getSession();
		
		String foglio = request.getParameter("foglio");
		String mappale = request.getParameter("mappale");
		String sub = request.getParameter("sub");
		
		finder = new DocfaFinder();
		
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));		
		//in questo caso fa una ricerca particolare e crea una versione ridotta di vettoreRicerca, 
		//contenente solo i valori dei parametri foglio, mappale e sub
		vettoreRicerca = new Vector();
		vettoreRicerca.add("popupDaCondono");
		vettoreRicerca.add(foglio);
		vettoreRicerca.add(mappale);
		vettoreRicerca.add(sub);
		Hashtable ht = logic.mCaricareListaDocfa(vettoreRicerca, finder);

		Vector vct_lista_docfa = (Vector) ht.get(DocfaLogic.LISTA_DOCFA);
		finder = (DocfaFinder) ht.get("FINDER");
		sessione.setAttribute(DocfaLogic.LISTA_DOCFA, vct_lista_docfa);
		sessione.setAttribute(NOMEFINDER, finder);
		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request, response, "docfa/docfaList.jsp?popupDaCondono=true&foglio=" + foglio +
											"&mappale=" + mappale + "&sub=" + sub, nav);
	}
	
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDatiFormRicerca(request.getUserPrincipal().getName());
		Vector vct_comuni = (Vector) ht.get("LISTA_COMUNI");
		Vector vct_catego = (Vector) ht.get("LISTA_CATEGORIE");
		
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
		
		Vector operatoriNumericiSoloMaggiorneUguale = new Vector();
		operatoriNumericiSoloMaggiorneUguale.add(new EscOperatoreFiltro(">=",">="));
 
		Vector operatoriNumericiSoloMinoreUguale = new Vector();
		operatoriNumericiSoloMinoreUguale.add(new EscOperatoreFiltro("<=","<="));
		
		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));
		
		 
		
		

		// costruisce il vettore di elementi per la pagina di ricerca
	
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();				
		
		
		elementoFiltro.setLabel("Dati Generali");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Numero Protocollo");
		elementoFiltro.setAttributeName("D_GEN.PROTOCOLLO_REG");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("D_GEN.PROTOCOLLO_REG");
		listaElementiFiltro.add(elementoFiltro);
		
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Variazione");
		elementoFiltro.setAttributeName("D_GEN.data_variazione");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("D_GEN.data_variazione");
		listaElementiFiltro.add(elementoFiltro);
		
		
		
		Vector vctCausale = new Vector();
		vctCausale.add(new TipoSoggetto("","Tutti"));
		vctCausale.add(new TipoSoggetto("NC","ACCATASTAMENTO"));
		vctCausale.add(new TipoSoggetto("AFF","DENUNCIA UNITA AFFERENTE"));
		vctCausale.add(new TipoSoggetto("DIV","DIVISIONE"));
		vctCausale.add(new TipoSoggetto("FRZ","FRAZIONAMENTO"));
		vctCausale.add(new TipoSoggetto("FUS","FUSIONE"));
		vctCausale.add(new TipoSoggetto("AMP","AMPLIAMENTO"));
		vctCausale.add(new TipoSoggetto("DET","DEMOLIZIONE TOTALE"));
		vctCausale.add(new TipoSoggetto("DEP","DEMOLIZIONE PARZIALE"));
		vctCausale.add(new TipoSoggetto("VSI","VARIAZIONE SPAZI INTERNI"));
		vctCausale.add(new TipoSoggetto("RST","RISTRUTTURAZIONE"));
		vctCausale.add(new TipoSoggetto("FRF","FRAZIONAMENTO E FUSIONE"));
		vctCausale.add(new TipoSoggetto("VTO","VARIAZIONE TOPONOMASTICA"));
		vctCausale.add(new TipoSoggetto("UFU","ULTIMAZIONE FABBRICATO URBANO"));
		vctCausale.add(new TipoSoggetto("VDE","CARIAZIONE DELLA DESTINAZIONE"));
		vctCausale.add(new TipoSoggetto("VAR","ALTRE VARIAZIONI"));
		vctCausale.add(new TipoSoggetto("VRP","PRESENTAZIONE PLANIMETRIA MANCANTE"));
		vctCausale.add(new TipoSoggetto("VMI","MODIFICA DI IDENTIFICATIVO"));
		vctCausale.add(new TipoSoggetto(DocfaLogic.MARCA_NULL,"CAUSALE NULLA"));
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Causale");
		elementoFiltro.setAttributeName("D_GEN.CAUSALE_NOTA_VAX");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctCausale);
		elementoFiltro.setCampoFiltrato("D_GEN.CAUSALE_NOTA_VAX");
		listaElementiFiltro.add(elementoFiltro);
		

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Registrazione Da");
		elementoFiltro.setAttributeName("cen.data_registrazione");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumericiSoloMaggiorneUguale);
		elementoFiltro.setCampoFiltrato("to_date(cen.data_registrazione,'yyyymmdd')");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Registrazione A");
		elementoFiltro.setAttributeName("cen.data_registrazione");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumericiSoloMinoreUguale);
		elementoFiltro.setCampoFiltrato("to_date(cen.data_registrazione,'yyyymmdd')");
		listaElementiFiltro.add(elementoFiltro);		
		
		
		Vector vctOper = new Vector();
		vctOper.add(new TipoSoggetto("","Tutti"));
		vctOper.add(new TipoSoggetto("IS NULL","ASSENTE"));
		vctOper.add(new TipoSoggetto("IS NOT NULL","PRESENTE")); 
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Operatore");
		elementoFiltro.setAttributeName("oper.campo14");
		elementoFiltro.setTipo("NN");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("oper.campo14");
		elementoFiltro.setListaValori(vctOper);
		listaElementiFiltro.add(elementoFiltro);		
		/*-----------------------------------------------------------------*/


		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DICHIARANTE ");//ex INTESTATARIPERSONE FISICHE
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		/*elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("inte.CODICE_FISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("inte.CODICE_FISCALE");
		listaElementiFiltro.add(elementoFiltro);*/
		
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("dic.cognome");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("dic.dic_cognome");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("dic.dic_NOME)");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("dic.dic_NOME");
		listaElementiFiltro.add(elementoFiltro);
		
		
		/*-----------------------------------------------------------------*/
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("PROFESSIONISTA");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("dic.tec_cognome");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("dic.tec_cognome");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("dic.tec_NOME)");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("dic.tec_NOME");
		listaElementiFiltro.add(elementoFiltro);
		
		/*-----------------------------------------------------------------*/		
		
		/*elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("INTESTATARI PERSONE GIURIDICHE");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Partita IVA");
		elementoFiltro.setAttributeName("inte.partita_iva");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("inte.partita_iva");
		listaElementiFiltro.add(elementoFiltro); */
		
		/*elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denominazione");
		elementoFiltro.setAttributeName("inte.DENOMINAZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("inte.DENOMINAZIONE");
		listaElementiFiltro.add(elementoFiltro);*/
		/*-----------------------------------------------------------------*/
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("IMMOBILI");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
				
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("u.FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("lpad4_0");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("u.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("u.NUMERO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("lpad5_0");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("u.NUMERO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("u.SUBALTERNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("lpad4_0");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("u.SUBALTERNO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Categoria");
		elementoFiltro.setAttributeName("cen.CATEGORIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaValori(vct_catego);
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("cen.CATEGORIA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Indirizzo");
		elementoFiltro.setAttributeName("u.indir_toponimo");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriLike);
		elementoFiltro.setCampoFiltrato("u.indir_toponimo");		
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/Docfa?listavie='+document.getElementById('u.indir_toponimo').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);	

		
		Vector vctTipoOperaz = new Vector();
		vctTipoOperaz.add(new TipoSoggetto("","Tutti"));
		vctTipoOperaz.add(new TipoSoggetto("C","Costituita"));
		vctTipoOperaz.add(new TipoSoggetto("V","Variata"));
		vctTipoOperaz.add(new TipoSoggetto("S","Sopressa"));
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Tipo Operazione");
		elementoFiltro.setAttributeName("u.TIPO_OPERAZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctTipoOperaz);
		elementoFiltro.setCampoFiltrato("u.TIPO_OPERAZIONE");
		listaElementiFiltro.add(elementoFiltro);
		
	
		
		/*elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Categoria");
		elementoFiltro.setAttributeName("cen.CATEGORIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("cen.CATEGORIA");
		listaElementiFiltro.add(elementoFiltro);
							
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Classe");
		elementoFiltro.setAttributeName("cen.CLASSE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("cen.CLASSE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Rendita");
		elementoFiltro.setAttributeName("cen.RENDITA_EURO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("cen.RENDITA_EURO");
		listaElementiFiltro.add(elementoFiltro);*/
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		//chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "docfa/docfaFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new DocfaFinder().getClass())
			{
				finder = (DocfaFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (DocfaFinder) gestireMultiPagina(finder, request);

		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		if (request.getParameter("STORICO_CONCESSIONI") != null && new Boolean(request.getParameter("STORICO_CONCESSIONI")).booleanValue()) {
			//CHIAMATA DA STORICO CONCESSIONI
			//in questo caso fa una ricerca particolare e crea una versione ridotta di vettoreRicerca, 
			//contenente solo i valori dei parametri foglio, particella e subalterno
			vettoreRicerca = new Vector();
			vettoreRicerca.add("STORICO_CONCESSIONI");
			vettoreRicerca.add(request.getParameter("FOGLIO"));
			vettoreRicerca.add(request.getParameter("PARTICELLA"));
			vettoreRicerca.add(request.getParameter("SUBALTERNO"));
		}
		Hashtable ht = logic.mCaricareListaDocfa(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_docfa = (Vector) ht.get(DocfaLogic.LISTA_DOCFA);
		finder = (DocfaFinder) ht.get("FINDER");
		sessione.setAttribute(DocfaLogic.LISTA_DOCFA, vct_lista_docfa);
		sessione.setAttribute(NOMEFINDER, finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{

			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();
		this.chiamaPagina(request, response, "docfa/docfaFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request il parametrio identificativo dell'oggetto
		// cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		DocfaFinder finder = null;
		// boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new DocfaFinder().getClass())
			{
				finder = (DocfaFinder) sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, DocfaLogic.LISTA_DOCFA, (Vector) sessione.getAttribute(DocfaLogic.LISTA_DOCFA), NOMEFINDER);
		if (azione.equals(""))
		{
			oggettoSel = "";
			recordScelto = "";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL") != null)
			{
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL") != null)
			{
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder != null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		/*
		 * carica il dettaglio
		 */
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioDocfa(oggettoSel);
		Docfa ana = (Docfa) ht.get(DocfaLogic.DOCFA);
		sessione.setAttribute(DocfaLogic.DOCFA, ana);

		
		sessione.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_ANNOTAZIONI, ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_ANNOTAZIONI));
		sessione.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_BENI_NON_CENS, ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_BENI_NON_CENS));
		sessione.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_DATI_CENSUARI, ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_DATI_CENSUARI));
		sessione.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_INTESTATI, ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_INTESTATI));
		sessione.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_UIU, ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_UIU));
		sessione.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_DICHIARANTI, ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_DICHIARANTI));
		sessione.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_PARTE_UNO, ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_PARTE_UNO));
		sessione.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_OPER, ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_OPER));
		//		sessione.setAttribute(DocfaLogic.LATITUDE_LONGITUDE, ht.get(DocfaLogic.LATITUDE_LONGITUDE));
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		// Aggiungo i valori per l'indice di correlazione
		Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
		
		String fornitura=ana.getFornitura();
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2= new SimpleDateFormat("yyyyMMdd");
		Date data= sdf.parse(fornitura);
		fornitura= sdf2.format(data);
		
		ArrayList listaDocfaDichiaranti = (ArrayList)ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_DICHIARANTI);
		
		ArrayList listaNomiSoggetti= new ArrayList();
		
		for (int i=0; i< listaDocfaDichiaranti.size(); i++){
			
			Docfa docfaDic= (Docfa)listaDocfaDichiaranti.get(i);
		
			OggettoIndice oi = new OggettoIndice();
			oi.setIdOrig(   ana.getProtocollo() +"|"+ fornitura  );
			oi.setFonte("9");
			oi.setProgr("1");
			
			if ((docfaDic.getCognome()!= null && !"".equals(docfaDic.getCognome())) || (docfaDic.getNome()!= null && !"".equals(docfaDic.getNome())))
				oi.setDescrizione(docfaDic.getCognome() + " " + docfaDic.getNome());
		
			String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
			if (!listaNomiSoggetti.contains(nomeSoggetto)){
				listaNomiSoggetti.add(nomeSoggetto);
				sOggettiInd.add(oi);
			}
			
			oi = new OggettoIndice();
			oi.setIdOrig( ana.getProtocollo() + "|"+ fornitura );
			oi.setFonte("9");
			oi.setProgr("2");
			
			if ((docfaDic.getTecCognome()!= null && !"".equals(docfaDic.getTecCognome())) || (docfaDic.getTecNome()!= null && !"".equals(docfaDic.getTecNome())))
				oi.setDescrizione(docfaDic.getTecCognome() + " " + docfaDic.getTecNome());
		
			 nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
			if (!listaNomiSoggetti.contains(nomeSoggetto)){
				listaNomiSoggetti.add(nomeSoggetto);
				sOggettiInd.add(oi);
			}
		}
		
		sessione.setAttribute("indice_soggetti", sOggettiInd);

		ArrayList listUiu = (ArrayList) ht.get(DocfaLogic.LISTA_DETTAGLIO_DOCFA_UIU);
		
		Vector<OggettoIndice> sCivInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> sOggInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		ArrayList listaNomiVie= new ArrayList();
		ArrayList listaNomiCivici= new ArrayList();
		ArrayList listaNomiOggetti= new ArrayList();
		ArrayList listaNomiFabbricati= new ArrayList();
		
		if (listUiu != null)  {
			
			for (int i=0; i < listUiu.size(); i++) {
				Docfa d = (Docfa) listUiu.get(i);
				OggettoIndice oi = new OggettoIndice();
				oi.setIdOrig(ana.getProtocollo() +"|"+ fornitura  + "|" + d.getNrProg());
				oi.setDescrizione(d.getIndirizzoDichiarante());
				oi.setFonte("9");
				oi.setProgr("3");
				String  nome= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
				if (!listaNomiVie.contains(nome)){
					listaNomiVie.add(nome);
					vieInd.add(oi);
				}
				
				if (!listaNomiCivici.contains(nome)){
					listaNomiCivici.add(nome);
					sCivInd.add(oi);
				}
				

				OggettoIndice oi1 = new OggettoIndice();
				oi1.setIdOrig(ana.getProtocollo() +"|"+ fornitura  + "|" + d.getNrProg());
				oi1.setDescrizione("F:" + d.getFoglio() + " P:" + d.getParticella() + " S:" + d.getSubalterno());
				oi1.setFonte("9");
				oi1.setProgr("3");
				 nome= oi1.getFonte()+oi1.getProgr()+oi1.getIdOrig();
				if (!listaNomiOggetti.contains(nome)){
					listaNomiOggetti.add(nome);
					sOggInd.add(oi1);
				}
				
				 oi1 = new OggettoIndice();
				oi1.setIdOrig(ana.getProtocollo() +"|"+ fornitura  + "|" + d.getNrProg());
				oi1.setDescrizione("SEZ:-"+" F:" + d.getFoglio() + " P:" + d.getParticella() );
				oi1.setFonte("9");
				oi1.setProgr("3");
				 nome= oi1.getFonte()+oi1.getProgr()+oi1.getIdOrig();
				if (!listaNomiFabbricati.contains(nome)){
					listaNomiFabbricati.add(nome);
					fabbInd.add(oi1);
				}
				
			
			}

			
			sessione.setAttribute("indice_civici", sCivInd);
			sessione.setAttribute("indice_oggetti", sOggInd);
			sessione.setAttribute("indice_vie", vieInd);
			sessione.setAttribute("indice_fabbricati", fabbInd);

		}
		
		
		
		if (request.getParameter("popupDaCondonoDett") != null) {
			String foglio = request.getParameter("foglio");
			String mappale = request.getParameter("mappale");
			String sub = request.getParameter("sub");
			this.chiamaPagina(request, response, "docfa/docfaDetail.jsp?popupDaCondonoDett=true&foglio=" + foglio +
												"&mappale=" + mappale + "&sub=" + sub, nav);
		} else {
			this.chiamaPagina(request, response, "docfa/docfaFrame.jsp", nav);
		}

		// EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);

	}//-------------------------------------------------------------------------
	
	protected String getModalitaCalcoloValoreCommerciale() {
		
		if(modalitaCalcoloValoreCommerciale == null || modalitaCalcoloValoreCommerciale.equals("")){
			ParameterSearchCriteria criteria= new ParameterSearchCriteria();
			criteria.setKey("docfa.modalita.calcolo.valore.commerciale");
			
			try{
			Context cont = new InitialContext();
			
			ParameterService parameterService= (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
						
			AmKeyValueExt amKey=parameterService.getAmKeyValueExt(criteria);
			modalitaCalcoloValoreCommerciale= amKey.getValueConf();
			
			log.info("MODALITA CALCOLO VALORE COMMERCIALE: " + modalitaCalcoloValoreCommerciale);
			
			}catch (NamingException e){
				modalitaCalcoloValoreCommerciale="";
			}
		}
		return modalitaCalcoloValoreCommerciale;
	}//-------------------------------------------------------------------------


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
	{
		finder = (DocfaFinder) finder2;
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		return logic.mCaricareListaDocfa(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder()
	{
		return new DocfaFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((Docfa) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Docfa";
	}



}
