package it.webred.mui.http.codebehind;

import it.webred.mui.consolidation.ComunicazioneConverter;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CodeBehindcomunicazioneResultPage extends AbstractPage {
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, java.io.IOException {
		if (req.getMethod().equalsIgnoreCase("post")) {
			try {
				final ComunicazioneConverter conv = new ComunicazioneConverter();
				final String iidFornitura = req.getParameter("iidFornitura");
				Thread t = new Thread() {
					public void run() {
						//Transaction tx = null;
						try {
							//Session session = HibernateUtil.currentSession();
							//tx = session.beginTransaction();
							conv.consolidateFornitura(iidFornitura);
							//tx.commit();
							MuiApplication.getMuiApplication().forceEntryUpdate(UPLOAD_LIST_VARNAME);
						} catch (HibernateException e) {
							//if(tx != null)tx.rollback();
							throw e;
						} finally {
							try {
								//HibernateUtil.closeSession();
							} catch (HibernateException e) {
							}
						}
					}
				};
				t.start();
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		return true;
	}

}
