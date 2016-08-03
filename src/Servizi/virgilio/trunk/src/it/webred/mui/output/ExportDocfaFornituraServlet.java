package it.webred.mui.output;

import it.webred.docfa.model.DocfaFornitura;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.model.MiDupFornitura;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.skillbill.logging.Logger;

import org.hibernate.Session;

/**
 * Servlet implementation class for Servlet: ExportFornituraServlet
 *
 */
 public class ExportDocfaFornituraServlet extends it.webred.mui.http.MuiBaseServlet implements javax.servlet.Servlet {
	 private static SimpleDateFormat _formatter = new SimpleDateFormat("yyyyMMdd");
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ExportDocfaFornituraServlet() {
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

		DocfaFornituraExporter exporter = new DocfaFornituraExporter();
		String iidFornitura = req.getParameter("iidFornitura");
		String fornituraFormatted = iidFornitura.substring(8, 10)+"/"+iidFornitura.substring(5, 7)+"/"+iidFornitura.substring(0, 4);
		res.setHeader("Content-disposition", "attachment; filename=DOCFA_"+ iidFornitura.substring(0, 4)+iidFornitura.substring(5, 7)+iidFornitura.substring(8, 10)+ "_O.TXT");
		//Session session = HibernateUtil.currentSession();
		try {
			
			Context cont = new InitialContext();
			Context datasourceContext = (Context) cont.lookup("java:comp/env");

			String sqlForn = "SELECT   COUNT (*) AS numero_docfa, data_fornitura, data_inizio  " +
			"    FROM (SELECT DISTINCT DATA AS data_fornitura, protocollo,  " +
			"                          caronte_data_inizio_val AS data_inizio  " +
			"                     FROM doc_docfa_1_0 " +
			"                     where CARONTE_FLAG_ELABORATO = 1" +
			"					  and data = TO_DATE(?,'DD/MM/YYYY') )  " +
			"GROUP BY data_fornitura, data_inizio ";

			
			DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/mui");
			Connection conn = theDataSource.getConnection();
			PreparedStatement pstm = conn.prepareStatement(sqlForn);
			pstm.setString(1, fornituraFormatted );
			ResultSet rsForn = pstm.executeQuery();
			DocfaFornitura df = new DocfaFornitura();
			if (rsForn.next())
			{
				df.setDataFornitura(rsForn.getDate("data_fornitura"));
				df.setDataInizio(rsForn.getDate("data_inizio"));
				df.setNumeroDocfa(rsForn.getLong("numero_docfa"));
			}
			
			rsForn.close();
			pstm.close();
			conn.close();
			
			// ------------------------------------------------------------
			// Content-disposition header - don't open in browser and
			// set the "Save As..." filename.
			// *There is reportedly a bug in IE4.0 which ignores this...
			// ------------------------------------------------------------


			exporter.dumpFornitura(df,res.getWriter());
		} catch (IOException e) {
			Logger.log().error(ExportDocfaFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			Logger.log().error(ExportDocfaFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (InvocationTargetException e) {
			Logger.log().error(ExportDocfaFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} catch (NoSuchMethodException e) {
			Logger.log().error(ExportDocfaFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		}catch (Exception e) {
			Logger.log().error(ExportDocfaFornituraServlet.class.getName(),"Error while exporting "+ req.getParameter("iidFornitura"),e);
			throw new ServletException(e);
		} 
		
	}
}