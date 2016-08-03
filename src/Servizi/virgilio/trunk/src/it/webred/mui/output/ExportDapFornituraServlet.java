package it.webred.mui.output;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.model.MiDupFornitura;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.Session;

/**
 * Servlet implementation class for Servlet: ExportFornituraServlet
 *
 */
 public class ExportDapFornituraServlet extends it.webred.mui.http.MuiBaseServlet implements javax.servlet.Servlet {
	 private static SimpleDateFormat _formatter = new SimpleDateFormat("yyyyMMdd");
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ExportDapFornituraServlet() {
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

		ConsDapFornituraExporter exporter = new ConsDapFornituraExporter();
		Long iidFornitura = Long.valueOf( req.getParameter("iidFornitura"));
		Session session = HibernateUtil.currentSession();
		try {
			MiDupFornitura fornitura = (MiDupFornitura)session.load(MiDupFornitura.class,iidFornitura);
			// ------------------------------------------------------------
			// Content-disposition header - don't open in browser and
			// set the "Save As..." filename.
			// *There is reportedly a bug in IE4.0 which ignores this...
			// ------------------------------------------------------------
			res.setHeader("Content-disposition", "attachment; filename=DAP_"
					+ _formatter.format(fornitura.getDataInizialeDate())+ "_O.TXT.txt");

			exporter.dumpFornitura(fornitura,res.getWriter());
		} catch (IOException e) {
			Logger.log().error(ExportDapFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			Logger.log().error(ExportDapFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (InvocationTargetException e) {
			Logger.log().error(ExportDapFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (NoSuchMethodException e) {
			Logger.log().error(ExportDapFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} finally {
			HibernateUtil.closeSession();
		}
		
	}
}