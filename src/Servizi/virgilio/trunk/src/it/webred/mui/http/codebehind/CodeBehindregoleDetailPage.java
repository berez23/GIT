package it.webred.mui.http.codebehind;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.model.CodiceErroreImport;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class CodeBehindregoleDetailPage extends AbstractPage {

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		Session session = HibernateUtil.currentSession();
		CodiceErroreImport codiceErrore = (CodiceErroreImport) session.load(CodiceErroreImport.class, req.getParameter("codiceRegolaInfranta"));
		if (req.getMethod().equalsIgnoreCase("post")) {
    		Transaction tx =session.beginTransaction();
    		try {
    			codiceErrore.setDescrizione (req.getParameter("descrizione"));
    			codiceErrore.setFlagBloccante("true".equalsIgnoreCase(req.getParameter("flagBloccante")));
    			codiceErrore.setEffetto(req.getParameter("effetto"));
    			session.saveOrUpdate(codiceErrore);
    			tx.commit();
			} catch (Exception e) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
				}
			}
			finally {
			}
		}
	    req.setAttribute("codiceErrore",codiceErrore);
		return true;
	}


}
