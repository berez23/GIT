package it.webred.mui.http.codebehind;

import it.webred.docfa.model.DocfaDapExported;
import it.webred.mui.MuiException;
import it.webred.mui.consolidation.DapStat;
import it.webred.mui.consolidation.DocfaDapManager;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.cache.CacheRefresher;


import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


public class CodeBehinddocfaDapEvalPage extends AbstractPage implements CacheRefresher {
	
	@Override
	public void init() {
		MuiApplication.getMuiApplication().add(DOCFA_DAP_VARNAME,this);
	}
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, java.io.IOException {
		if (req.getMethod().equalsIgnoreCase("post")) {
			try {
				final DocfaDapManager dapm = new DocfaDapManager();
				String tempForn = req.getParameter("idFornitura");//yyyy-mm-dd
				//data fornitura yyyyMMdd
				final String iidFornitura = tempForn.substring(0, 4)+tempForn.substring(5, 7)+tempForn.substring(8, 10);
				Thread t = new Thread() {  
					@SuppressWarnings("unchecked")
					public void run() {
						try {
							dapm.manageDap(iidFornitura);
							Logger.log().info(this.getClass().getName(),
									"finished evaluating dap for fornitura "+iidFornitura);

							MuiApplication.getMuiApplication().forceEntryUpdate(DOCFA_DAP_VARNAME);
						} catch (HibernateException e) {
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
		session = HibernateUtil.currentSession();
	    try{
	    	HashMap <Long,DapStat> dapStats = new HashMap<Long,DapStat>();
		    Query query = session.createQuery(
		        "select c from it.webred.docfa.model.DocfaDapExported as c order by iidFornitura desc");
			Logger.log().info(this.getClass().getName(),
					"executing query "+query);
		    for (Iterator it = query.iterate(); it.hasNext(); ) {
		    	DocfaDapExported nde = (DocfaDapExported)it.next();
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
