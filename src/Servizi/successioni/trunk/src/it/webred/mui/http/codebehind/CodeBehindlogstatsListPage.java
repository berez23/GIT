package it.webred.mui.http.codebehind;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.model.MiQryFornitureLogDetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

public class CodeBehindlogstatsListPage extends AbstractPage {

	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if(req.getMethod().equalsIgnoreCase("post")){
			List<MiQryFornitureLogDetail> MiQryFornitureLogDetails = new ArrayList<MiQryFornitureLogDetail>();
			Session session = HibernateUtil.currentSession();
			SQLQuery query = null;
			String subquery="	select "
			+"					iid_fornitura "
			+"					,count(*) totale"
			+"					,l.codice_regola codice_regola"
			+"					, tabella_record"
			+"					, colonna_regola"
			+"					,decode(bloccante,'N','N','S') bloccante"
			+"				from "
			+"					suc_dup_import_log l,CODICE_ERRORE_IMPORT c"
			+"				where 		c.codice_regola = l.codice_regola"
			+"				group by iid_fornitura,l.codice_regola ,bloccante, colonna_regola , tabella_record ";
			query = session
			.createSQLQuery("select * from ("+subquery+" )VwFornitureLogDetail where iid_Fornitura = :iidFornitura ");
			query.addEntity(MiQryFornitureLogDetail.class);
			query.setString("iidFornitura", req.getParameter("iidFornitura"));
			List mList =query.list();
			Logger.log().info(this.getClass().getName(),
					"list size "+mList.size());
			for (Iterator it = mList.iterator(); it.hasNext();) {
				MiQryFornitureLogDetail MiQryFornitureLogDetail = (MiQryFornitureLogDetail) it.next();
				MiQryFornitureLogDetails.add(MiQryFornitureLogDetail);
			}
		    req.getSession().setAttribute("MiQryFornitureLogDetails",MiQryFornitureLogDetails);
		}
		return true;
	}

}
