package it.escsolution.escwebgis.diagnostiche.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheViewerLogic;
import it.escsolution.escwebgis.diagnostiche.util.DiaBridge;
import it.webred.ct.diagnostics.service.data.dto.DiaValueKeysDTO;

public class DiagnosticheViewerServlet extends EscServlet implements EscService {

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.EseguiServizio(request, response);
		if (request.getParameter("popup") != null && request.getParameter("popup").equalsIgnoreCase("yes")) {
			st = 3;
		}		
		try {
			switch (st) {
				case 1:
					//non previsto
					break;
				case 2:
					//non previsto
					break;
				case 3:
					mCaricareDettaglio(request, response);
					break;
			}
		}
		catch (it.escsolution.escwebgis.common.DiogeneException de) {
			throw de;
		}
		catch (Exception ex){
			log.error(ex.getMessage(),ex);
		}
	}
	
	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tipi = request.getParameter(DiaBridge.TIPI);
		String fonti = request.getParameter(DiaBridge.FONTI);
		String key = request.getParameter(DiaBridge.KEY);
		DiagnosticheViewerLogic logic = new DiagnosticheViewerLogic(this.getEnvUtente(request));
		ArrayList<HashMap<String, String>> intestazioni = logic.getDiaIntestazioni(tipi, fonti, key);		
		request.setAttribute(DiaBridge.DIA_INTESTAZIONI, intestazioni);
		ArrayList<HashMap<Long,List<DiaValueKeysDTO[]>>> dati = logic.getDiaDati(tipi, fonti, key);		
		request.setAttribute(DiaBridge.DIA_DATI, dati);
		request.getRequestDispatcher("diagnostiche/diagnosticheViewer.jsp").forward(request,response);
	}
	
}
