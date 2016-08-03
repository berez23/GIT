package it.escsolution.escwebgis.diagnostiche.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheCatalogoLogic;

public class DiagnosticheCatalogoServlet extends EscServlet implements EscService {

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		super.EseguiServizio(request, response);
		try {	
			switch (st) {
				case 99:
					mDownloadFile(request, response);
					break;
			}
		} catch (it.escsolution.escwebgis.common.DiogeneException de) {
			throw de;
		} catch (Exception ex) {
			log.error(ex.getMessage(),ex);
		}	
	}
	
	private void mDownloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter("PATH_FILE") != null) {
			String pathFile = request.getParameter("PATH_FILE");
			DiagnosticheCatalogoLogic logic = new DiagnosticheCatalogoLogic();
			logic.download(response, pathFile);
		}
	}
	
	public String getTema() {
		return "Diagnostiche";
	}
	
}
