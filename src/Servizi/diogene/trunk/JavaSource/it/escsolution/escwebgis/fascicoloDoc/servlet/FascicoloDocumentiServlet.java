package it.escsolution.escwebgis.fascicoloDoc.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

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
import it.escsolution.escwebgis.common.TiffUtill;
import it.escsolution.escwebgis.fascicoloDoc.bean.FascicoloDocumentiFinder;
import it.escsolution.escwebgis.fascicoloDoc.logic.FascicoloDocumentiLogic;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;


public class FascicoloDocumentiServlet extends EscServlet implements EscService {

	private static final long serialVersionUID = 1L;
	
	PulsantiNavigazione nav = new PulsantiNavigazione();
	FascicoloDocumentiFinder finder = null;
	
	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request, response);
		try {
			switch (st) {
			case 1:
				// carico la form di ricerca
				pulireSessione(request);
				mCaricareFormRicerca(request, response, st);
				break;
			case 2:
				// visualizzo il dettaglio
				mCaricareDettaglio(request, response, false, st);
				break;
			case 99:
				//apertura documento
				mOpenPlanimetria(request, response, st);
			}
		} catch (it.escsolution.escwebgis.common.DiogeneException de) {
			throw de;
		} catch (Exception ex) {
			log.error(ex.getMessage(),ex);
		}

	}
	
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response, int st) throws Exception {

		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		
		if(st == 1){
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Sezione");
			elementoFiltro.setAttributeName("SEZ");
			elementoFiltro.setTipo("S");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			listaElementiFiltro.add(elementoFiltro);
			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Foglio *");
			elementoFiltro.setAttributeName("FOGLIO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			listaElementiFiltro.add(elementoFiltro);

			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Particella *");
			elementoFiltro.setAttributeName("PARTICELLA");
			elementoFiltro.setTipo("S");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			listaElementiFiltro.add(elementoFiltro);
			
			//nota
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("* campi obbligatori");
			elementoFiltro.setSoloLabel(true);
			listaElementiFiltro.add(elementoFiltro);
			
		} else {
			sessione.setAttribute("FOGLIO", request.getParameter("FOGLIO"));
			sessione.setAttribute("PARTICELLA", request.getParameter("PARTICELLA"));
		}

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "fascicoloDoc/fascicoloDocumentiFrame.jsp", nav);
	}
	
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, boolean tipo, int st )throws Exception {
		
		HttpSession sessione = request.getSession();
		
		if (sessione.getAttribute(FascicoloDocumentiLogic.FINDER)!= null) {
			finder = (FascicoloDocumentiFinder)sessione.getAttribute(FascicoloDocumentiLogic.FINDER);
		} else{
			finder = null;
		}
	
		finder = (FascicoloDocumentiFinder)gestireMultiPagina(finder, request);

		FascicoloDocumentiLogic logic = new FascicoloDocumentiLogic(this.getEnvUtente(request));
		
		String sezione = "";
		String foglio = null;
		String particella = null;
	
		for (Object o : vettoreRicerca) {
			EscElementoFiltro el = (EscElementoFiltro)o;
			if("SEZ".equals(el.getAttributeName()))
				sezione = el.getValore();
			if("FOGLIO".equals(el.getAttributeName()))
				foglio = el.getValore();
			if("PARTICELLA".equals(el.getAttributeName()))
				particella = el.getValore();
		}
		
		if (st == 4 && (foglio == null || particella == null)) {
			foglio = (String)request.getParameter("FOGLIO");
			particella = (String)request.getParameter("PARTICELLA");
		}
		
		String err = null;		
		if (foglio == null || foglio.equals("")) {
			err = "Immettere foglio";
		}
		if (particella == null || particella.equals("")) {
			if (err == null) err = "Immettere particella";
			else err += " e particella";
		}
		if (err != null) err += ".";
		
		sessione.setAttribute(FascicoloDocumentiLogic.ERROR, err);
		
		if (err == null || err.equals("")) {
			String chiave = sezione + "|" + foglio + "|" + particella;
			Hashtable ht = logic.mCaricareDettaglio(chiave, finder);
			sessione.setAttribute(FascicoloDocumentiLogic.CHIAVE, ht.get(FascicoloDocumentiLogic.CHIAVE));
			sessione.setAttribute(FascicoloDocumentiLogic.FINDER, ht.get(FascicoloDocumentiLogic.FINDER));
			sessione.setAttribute(FascicoloDocumentiLogic.LISTA_DOCFA_PLANIMETRIE_UIU, ht.get(FascicoloDocumentiLogic.LISTA_DOCFA_PLANIMETRIE_UIU));
			sessione.setAttribute(FascicoloDocumentiLogic.LISTA_DOCFA_PLANIMETRIE_FAB, ht.get(FascicoloDocumentiLogic.LISTA_DOCFA_PLANIMETRIE_FAB));
			sessione.setAttribute(FascicoloDocumentiLogic.LISTA_C340_PLANIMETRIE_UIU, ht.get(FascicoloDocumentiLogic.LISTA_C340_PLANIMETRIE_UIU));
			sessione.setAttribute(FascicoloDocumentiLogic.LISTA_C340_PLANIMETRIE_FAB, ht.get(FascicoloDocumentiLogic.LISTA_C340_PLANIMETRIE_FAB));
			sessione.setAttribute(FascicoloDocumentiLogic.VIEW_NO_WATERMARK, ht.get(FascicoloDocumentiLogic.VIEW_NO_WATERMARK));
		}

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request, response, "fascicoloDoc/fascicoloDocumentiFrame.jsp", nav);
	}
	
	private void mOpenPlanimetria(HttpServletRequest request, HttpServletResponse response, int st) throws Exception {
		
		String nomePlan = request.getParameter("nomePlan") == null ? "" : request.getParameter("nomePlan");
		String linkPlan = request.getParameter("linkPlan") == null ? "" : request.getParameter("linkPlan");
		String padProgressivo = request.getParameter("padProgressivo") == null ? "" : request.getParameter("padProgressivo");
		String fornituraStr = request.getParameter("fornituraStr") == null ? "" : request.getParameter("fornituraStr");
		String formato = request.getParameter("formato") == null ? "" : request.getParameter("formato");
		boolean openJpg = request.getParameter("openJpg") == null ? false : new Boolean(request.getParameter("openJpg")).booleanValue();
		boolean watermark = request.getParameter("watermark") == null ? false : new Boolean(request.getParameter("watermark")).booleanValue();
		String tipoPlan = request.getParameter("tipoPlan") == null ? "" : request.getParameter("tipoPlan");
		
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("dir.files.datiDiogene");
		ParameterService parameterService = (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");		
		AmKeyValueExt amKey=parameterService.getAmKeyValueExt(criteria);
		String pathDatiDiogene = amKey.getValueConf();
		
		File image = null;
		
		InputStream is = null;
		InputStream isByte = null;
		ByteArrayOutputStream baos = null;
		OutputStream out = null;
		String pathFile = "";
		
		if (tipoPlan.equalsIgnoreCase(FascicoloDocumentiLogic.TIPO_PLAN_DOCFA)) {
			nomePlan = nomePlan + "." + padProgressivo + ".tif";
			String dirPlan = FascicoloDocumentiLogic.DIR_PLANIMETRIE;
			pathFile = pathDatiDiogene + File.separatorChar + this.getEnvUtente(request).getEnte() + File.separatorChar + dirPlan + File.separatorChar + fornituraStr.substring(0, 6) +  File.separatorChar + nomePlan;
		} else if (tipoPlan.equalsIgnoreCase(FascicoloDocumentiLogic.TIPO_PLAN_C340)) {
			pathFile = linkPlan.replace('/', File.separatorChar);
		}
		
		File f = new File(pathFile);
					
		image = f;
		is = new FileInputStream(image);
				    
		List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, watermark, openJpg);
		if (openJpg) {
			baos = jpgs.get(0);
		} else {
			baos =  TiffUtill.jpgsTopdf(jpgs, false, Integer.valueOf(formato).intValue());
		}
		isByte = new ByteArrayInputStream(baos.toByteArray());
		is.close();
		
		
		if (openJpg && watermark) {
			response.addHeader("Content-Disposition","attachment; filename=" + nomePlan + ".jpg");
			response.setContentType("image/jpeg");
		} else if (openJpg && !watermark){
			response.addHeader("Content-Disposition","attachment; filename=" + nomePlan+ ".tiff");
			response.setContentType("image/tiff");
		} else {
			response.addHeader("Content-Disposition","attachment; filename=" + nomePlan + ".pdf");
			response.setContentType("application/pdf");
		}		
		
		out = response.getOutputStream();
		
		byte[] b = new byte[1024];
        while (isByte.read(b) != -1) {
            out.write(b);
        }
        
        out.flush();	            
        out.close();
	}
	
	public EscFinder getNewFinder(){
		return new FascicoloDocumentiFinder();
	}

	public String getTema() {
		return "Fascicolo";
	}

}
