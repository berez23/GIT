package it.webred.mui.output;

import it.webred.mui.hibernate.HibernateUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.Session;

/**
 * Servlet implementation class for Servlet: ExportFornituraServlet
 *
 */
 public class ExportDocfaDapFornituraServlet extends it.webred.mui.http.MuiBaseServlet implements javax.servlet.Servlet {
	
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ExportDocfaDapFornituraServlet() {
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

		DocfaDapFornituraExporter exporter = new DocfaDapFornituraExporter();
		String iidFornitura = req.getParameter("idFornitura");
		String fornitura = iidFornitura.substring(0, 4)+iidFornitura.substring(5, 7)+iidFornitura.substring(8, 10);
		Session session = HibernateUtil.currentSession();
		try {
			// ------------------------------------------------------------
			// Content-disposition header - don't open in browser and
			// set the "Save As..." filename.
			// *There is reportedly a bug in IE4.0 which ignores this...
			// ------------------------------------------------------------
			res.setHeader("Content-disposition", "attachment; filename=DOCFA_DAP_"
					+ fornitura + "_O.TXT");

			exporter.dumpFornitura(fornitura,res.getWriter());
		} catch (IOException e) {
			Logger.log().error(ExportDocfaDapFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			Logger.log().error(ExportDocfaDapFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (InvocationTargetException e) {
			Logger.log().error(ExportDocfaDapFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (NoSuchMethodException e) {
			Logger.log().error(ExportDocfaDapFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} finally {
			HibernateUtil.closeSession();
		}
		
	}
}