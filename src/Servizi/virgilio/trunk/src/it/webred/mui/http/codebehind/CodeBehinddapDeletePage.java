package it.webred.mui.http.codebehind;

import it.webred.mui.MuiException;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.model.MiDupFornitura;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CodeBehinddapDeletePage extends AbstractPage {
	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, java.io.IOException {
		if (req.getMethod().equalsIgnoreCase("post")) {
			    Session session = HibernateUtil.currentSession();
			    Transaction tx = session.beginTransaction();
			    try{
					final Long iidFornitura = Long.valueOf( req.getParameter("iidFornitura"));
					MiDupFornitura fornitura = (MiDupFornitura)session.load(MiDupFornitura.class, iidFornitura);
					String hqlDelete = "delete MiConsDap c where c.miDupFornitura = :miDupFornitura";
					Logger.log().info(this.getClass().getName(),"deleting from MiConsDap where iidFornitura="+iidFornitura);
					int deletedEntities = session.createQuery( hqlDelete ).setParameter( "miDupFornitura", fornitura).executeUpdate();
					Logger.log().info(this.getClass().getName(),"deleted "+deletedEntities+" row(s) from MiConsDap where iidFornitura="+iidFornitura);
				    tx.commit();
					MuiApplication.getMuiApplication().forceEntryUpdate(DAP_LIST_VARNAME);
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
		req.setAttribute("requestedUri", "uploadList.jsp");
		req.getRequestDispatcher("uploadList.jsp").include(req, resp);
		return false;
	}

}
