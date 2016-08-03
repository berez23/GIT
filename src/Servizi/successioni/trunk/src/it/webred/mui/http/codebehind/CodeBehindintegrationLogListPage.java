package it.webred.mui.http.codebehind;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.model.MiConsIntegrationLog;
import it.webred.mui.model.MiDupNotaTras;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;


public class CodeBehindintegrationLogListPage extends AbstractPage {

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if(req.getMethod().equalsIgnoreCase("post")){
			List<MiConsIntegrationLog> miConsIntegrationLogs = new ArrayList<MiConsIntegrationLog>();
			Session session = HibernateUtil.currentSession();
			Query query = null;
			if(req.getParameter("iidFornitura")!= null){
				query = session
				.createQuery("select c from it.webred.mui.model.MiConsIntegrationLog as c where c.miConsComunicazione.miDupSoggetti.miDupFornitura.iid = :iidFornitura order by c.iid asc");
				query.setString("iidFornitura", req.getParameter("iidFornitura"));
			}
			else if(req.getParameter("iidNota")!=null){
				query = session
				.createQuery("select c from it.webred.mui.model.MiConsIntegrationLog as c where c.miConsComunicazione.miDupSoggetti.miDupNotaTras.iid = :iidNota order by c.iid asc");
				query.setString("iidNota", req.getParameter("iidNota"));
			}
			else {
				query = session
				.createQuery("select c from it.webred.mui.model.MiConsIntegrationLog as c order by c.iid asc");
			}
			query.setMaxResults(500);
			for (Iterator it = query.iterate(); it.hasNext();) {
				MiConsIntegrationLog miConsIntegrationLog = (MiConsIntegrationLog) it.next();
				miConsIntegrationLogs.add(miConsIntegrationLog);
			}
		    req.getSession().setAttribute("MiConsIntegrationLogs",miConsIntegrationLogs);
		}
		return true;
	}

	public List<MiDupNotaTras> getMiDupNotaTrasList(HttpServletRequest req) {
		List<MiDupNotaTras> MiDupNotaTrass = (List<MiDupNotaTras>)req.getAttribute("MiDupNotaTrass");
		return MiDupNotaTrass;
	}


}
