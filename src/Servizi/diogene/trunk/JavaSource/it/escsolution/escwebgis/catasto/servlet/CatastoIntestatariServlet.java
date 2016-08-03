/*
 * Created on 6-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.escsolution.escwebgis.catasto.servlet;

import it.escsolution.escwebgis.catasto.bean.IntestatarioFFinder;
import it.escsolution.escwebgis.catasto.logic.CatastoIntestatariFLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoIntestatariLogic;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Giulio
 */
public class CatastoIntestatariServlet extends EscServlet {


	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private CatastoIntestatariFLogic logic = null;
	private IntestatarioFFinder finder = null;
	public static final String PK_CUAA_URL_NAME = "KEYSTR";

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			//pulireSessione(request);
			mRedirectToIntestatari(request,response);
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception exx){
			log.error(exx.getMessage(),exx);
		}
	}

	private void mRedirectToIntestatari(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession sessione = request.getSession();
		String pkCuaa = request.getParameter(PK_CUAA_URL_NAME);
		if (pkCuaa == null)
			throw new Exception("Alla servlet " + this.getClass().getName() + " va passato un parametro " + PK_CUAA_URL_NAME);

		CatastoIntestatariLogic logic = new CatastoIntestatariLogic(this.getEnvUtente(request));
		int uc = logic.restituisciUCperPkCuaa(pkCuaa);
		String servletUrl = null;
		try
		{
			servletUrl = Tema.getServletMapping(uc);
		}
		catch (Exception err)
		{
			/*
			String html = "";
			html += "<html>\r\n";
			html += "<head>\r\n";
			html += "<script language=\"JavaScript\">\r\n";
			html += "<!--\r\n";
			html += "function nessunSoggetto()\r\n";
			html += "{\r\n";
			html += "	alert(\"Non esiste alcun soggetto collegato\");\r\n";
			html += "	window.history.back();\r\n";
			html += "}\r\n";
			html += "//-->\r\n";
			html += "</script>\r\n";
			html += "</head>\r\n";
			html += "<body onLoad=\"nessunSoggetto()\">\r\n";
			html += "</body>\r\n";
			html += "</html>\r\n";
			response.getWriter().write(html);
			response.flushBuffer();
			return;
			*/
			uc = 3;
			servletUrl = Tema.getServletMapping(uc);
		}
		servletUrl += "?DATASOURCE=" + this.getDataSource(request) + "&ST=2&UC=" + uc + "&KEYSTR=" + pkCuaa;
		response.sendRedirect(servletUrl);
	}
	public String getTema() {

		return "Catasto";
	}
}
