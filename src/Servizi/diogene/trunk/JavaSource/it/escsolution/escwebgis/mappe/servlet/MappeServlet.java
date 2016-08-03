package it.escsolution.escwebgis.mappe.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.mappe.bean.Link;
import it.escsolution.escwebgis.mappe.bean.Mappe;
import it.escsolution.escwebgis.mappe.logic.MappeLogic;

public class MappeServlet extends EscServlet implements EscService {
	

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request, response);
		try {
			String mode = request.getParameter("mode");
			if (mode == null || mode.equals("open")) {
				mApriPopup(request, response);
			} else if (mode.equals("56") || mode.equals("CV") || mode.equals("CC") || mode.equals("DE")) {
				mApriCartografiaStorica(request, response, mode);
			} else if (mode.equals("img")) {
				mApriImg(request, response);
			}
		}
		catch (it.escsolution.escwebgis.common.DiogeneException de) {
			throw de;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(),ex);
		}
	}
	
	private void mApriPopup(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String index = request.getParameter("index");
		Mappe mappe = new Mappe();
		mappe.setComune(request.getParameter("comune"));
		mappe.setFoglio(request.getParameter("foglio"));
		mappe.setParticella(request.getParameter("particella"));
		mappe.setLatitudine(request.getParameter("latitudine"));
		mappe.setLongitudine(request.getParameter("longitudine"));
		HttpSession session = request.getSession();
		session.setAttribute(MappeLogic.MAPPE_BEAN + index, mappe);
		request.getRequestDispatcher("mappe/mappe.jsp?index=" + index).forward(request,response);	
	}
	
	private void mApriCartografiaStorica(HttpServletRequest request, HttpServletResponse response, String mode)
	throws Exception {
		String index = request.getParameter("index");
		HttpSession session = request.getSession();
		MappeLogic ml = new MappeLogic(getEnvUtente(request));
		ArrayList<Link> links = new ArrayList<Link>();
		if (mode.equals("56")) {
			links = ml.getLinksImpianto56(request.getParameter("foglio"));
		} else if (mode.equals("CV")) {
			links = ml.getLinksCopioniVisura(request.getParameter("foglio"));
		} else if (mode.equals("CC")) {
			links = ml.getLinksCessatoCatasto(request.getParameter("foglio"));
		} else if (mode.equals("DE")) {
			links = ml.getLinksDemanio(request.getParameter("foglio"));
		}
		session.setAttribute(MappeLogic.LINK_LIST + index, links);
		if (request.getParameter("start") != null && request.getParameter("start").equals("yes")) {
			request.getRequestDispatcher("mappe/mappeVisImg.jsp?index=" + index + "&mode" + mode).forward(request,response);
		} else {
			request.getRequestDispatcher("mappe/mappeLink.jsp?index=" + index + "&mode" + mode).forward(request,response);
		}		
	}
	
	private void mApriImg(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String index = request.getParameter("index");
		String path = request.getParameter("path");
		request.getRequestDispatcher("mappe/mappeImg.jsp?index=" + index + "&path=" + path).forward(request,response);
	}
	
	public String getTema() {
		return "Catasto";
	}
	
}
