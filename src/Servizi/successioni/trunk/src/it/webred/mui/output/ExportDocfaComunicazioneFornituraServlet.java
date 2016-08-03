package it.webred.mui.output;

import it.webred.mui.hibernate.HibernateUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;


/**
 * Servlet implementation class for Servlet: ExportFornituraServlet
 *
 */
 public class ExportDocfaComunicazioneFornituraServlet extends it.webred.mui.http.MuiBaseServlet implements javax.servlet.Servlet {
	 //private static SimpleDateFormat _formatter = new SimpleDateFormat("yyyy-MM-dd");
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ExportDocfaComunicazioneFornituraServlet() {
		super();
	}   	
	/*
	 * (non-Java-doc)
	 * 
	 * @see MuiBaseServlet#muiService(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void muiService(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {
		res.setContentType("text/plain"); // MIME type for text

		DocfaComunicazioneFornituraExporter exporter = new DocfaComunicazioneFornituraExporter();
		String iidFornitura = req.getParameter("iidFornitura");
		
		try {
			// ------------------------------------------------------------
			// Content-disposition header - don't open in browser and
			// set the "Save As..." filename.
			// *There is reportedly a bug in IE4.0 which ignores this...
			// ------------------------------------------------------------
			res.setHeader("Content-disposition", "attachment; filename=DOCFA_COMUNICAZIONI_ICI_"
					+ iidFornitura+ "_O.TXT.txt");

			exporter.dumpFornitura(iidFornitura,res.getWriter());
		} catch (IOException e) {
			Logger.log().error(ExportDocfaComunicazioneFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			Logger.log().error(ExportDocfaComunicazioneFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (InvocationTargetException e) {
			Logger.log().error(ExportDocfaComunicazioneFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (NoSuchMethodException e) {
			Logger.log().error(ExportDocfaComunicazioneFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} finally {
			HibernateUtil.closeSession();
		}
		
	}
}