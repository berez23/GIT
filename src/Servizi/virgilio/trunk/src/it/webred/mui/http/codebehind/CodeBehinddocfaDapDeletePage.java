package it.webred.mui.http.codebehind;

import it.webred.mui.MuiException;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CodeBehinddocfaDapDeletePage extends AbstractPage {
	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, java.io.IOException {
		if (req.getMethod().equalsIgnoreCase("post")) {
			    Session session = HibernateUtil.currentSession();
			    Transaction tx = session.beginTransaction();
			    try{
			    	String forn = req.getParameter("iidFornitura");
					final String iidFornitura = forn.substring(0, 4)+forn.substring(5, 7)+forn.substring(8, 10);
					String hqlDelete = "delete DocfaDap c where c.iidFornitura = :iidFornitura";
					Logger.log().info(this.getClass().getName(),"deleting from DocfaDap where iidFornitura="+iidFornitura);
					int deletedEntities = session.createQuery( hqlDelete ).setParameter( "iidFornitura", iidFornitura).executeUpdate();
					Logger.log().info(this.getClass().getName(),"deleted "+deletedEntities+" row(s) from DocfaDap where iidFornitura="+iidFornitura);
				    tx.commit();
					MuiApplication.getMuiApplication().forceEntryUpdate(DOCFA_DAP_VARNAME);
			    }
			    catch (Throwable e) {
					Logger.log().error(this.getClass().getName(),
							"error while deleting daps of fornitura", e);
				    try {
						tx.rollback();
					} catch (HibernateException e1) {
						e1.printStackTrace();
					}
					throw new MuiException("error while deleting daps of fornitura",e);
				}
			    finally{
				    HibernateUtil.closeSession();
			    }
		}
		req.setAttribute("requestedUri", "docfaUploadList.jsp");
		req.getRequestDispatcher("docfaUploadList.jsp").include(req, resp);
		return true;
	}

}
