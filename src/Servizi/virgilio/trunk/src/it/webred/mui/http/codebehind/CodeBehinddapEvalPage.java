package it.webred.mui.http.codebehind;

import it.webred.mui.MuiException;
import it.webred.mui.consolidation.DapManager;
import it.webred.mui.consolidation.DapStat;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.cache.CacheRefresher;
import it.webred.mui.model.MiVwNoteDapExported;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CodeBehinddapEvalPage extends AbstractPage implements CacheRefresher {
	@Override
	public void init() {
		MuiApplication.getMuiApplication().add(DAP_LIST_VARNAME,this);
	}
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, java.io.IOException {
		if (req.getMethod().equalsIgnoreCase("post")) {
			try {
				final DapManager dapm = new DapManager();
				final String iidFornitura = req.getParameter("iidFornitura");
				Thread t = new Thread() {
					@SuppressWarnings("unchecked")
					public void run() {
//						Transaction tx = null;
						try {
			//				Session session = HibernateUtil.currentSession();
//							tx = session.beginTransaction();
							dapm.manageDap(iidFornitura);
							Logger.log().info(this.getClass().getName(),
									"finished evaluating dap for fornitura "+iidFornitura);

							HashMap <Long,DapStat> dapStats = (HashMap <Long,DapStat>)MuiApplication.getMuiApplication().getServletContext().getAttribute(DAP_LIST_VARNAME);
							if(dapStats!=null){
								DapStat dap = dapStats.get(Long.valueOf(iidFornitura));
								if(dap!=null){
									dap.setEvaluatedForced(true);
									Logger.log().info(this.getClass().getName(),
											"dap for fornitura "+iidFornitura+" forced as evaluated");
								}
							}
		//					tx.commit();
						} catch (HibernateException e) {
	//						if(tx != null)tx.rollback();
							throw e;
						} finally {
							try {
								HibernateUtil.closeSession();
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

	public Object doRefresh() {
		Session session;
		Transaction tx;
		session = HibernateUtil.currentSession();
//	    tx = session.beginTransaction();
	    try{
	    	HashMap <Long,DapStat> dapStats = new HashMap<Long,DapStat>();
		    Query query = session.createQuery(
		        "select c from it.webred.mui.model.MiVwNoteDapExported as c order by iidFornitura desc");
			Logger.log().info(this.getClass().getName(),
					"executing query "+query);
		    for (Iterator it = query.iterate(); it.hasNext(); ) {
		    	MiVwNoteDapExported nde = (MiVwNoteDapExported)it.next();
		    	DapStat dapStat = dapStats.get(nde.getIidFornitura());
		    	if(dapStat == null){
		    		dapStat = new DapStat();
		    		dapStats.put(nde.getIidFornitura(),dapStat);
		    	}
		    	if(nde.getCodice() == 1){
		    		dapStat.setTitolarita(nde.getTotali());
		    	}
		    	else if(nde.getCodice() == 2){
		    		dapStat.setEvaluated(nde.getTotali());
		    	}
		    	else if(nde.getCodice() == 3){
		    		dapStat.setDapNA(nde.getTotali());
		    	}
		    	else if(nde.getCodice() == 4){
		    		dapStat.setDapY(nde.getTotali());
		    	}
		    	else if(nde.getCodice() == 5){
		    		dapStat.setDapN(nde.getTotali());
		    	}
		    }
		    return dapStats;
	    }
	    catch (Throwable e) {
			Logger.log().error(this.getClass().getName(),
					"error while updating dap list", e);
			throw new MuiException("error while updating dap list",e);
		}
	    finally{
		    HibernateUtil.closeSession();
	    }
	}
}
