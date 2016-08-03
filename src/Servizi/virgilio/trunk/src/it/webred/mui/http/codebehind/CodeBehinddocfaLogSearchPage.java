package it.webred.mui.http.codebehind;

import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.WhereClause;
import it.webred.mui.http.codebehind.pagination.HibernateWhereQueryDisplayTagPaginator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.pagination.PaginatedList;

public class  CodeBehinddocfaLogSearchPage extends AbstractPage {

	private static final Log log = LogFactory.getLog(CodeBehinddocfaLogSearchPage.class);

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if (req.getMethod().equalsIgnoreCase("post") ) {
			PaginatedList DocfaDataLogs = getPaginatedList(req);
			req.setAttribute("DocfaDataLogs", DocfaDataLogs);
			req.setAttribute("requestedUri", "docfaLogList.jsp");
			req.getRequestDispatcher("docfaLogList.jsp").include(req, resp);
			return false;
		} else if (req.getQueryString() != null
				&& !req.getQueryString().trim().equals("")) {
			req.setAttribute("requestedUri", "docfaLogList.jsp");
			req.getRequestDispatcher("docfaLogList.jsp").include(req, resp);
			return false;
		}
		return true;
	}

	private PaginatedList getPaginatedList(HttpServletRequest req) {
		HibernateWhereQueryDisplayTagPaginator res  = new HibernateWhereQueryDisplayTagPaginator();
		res.setResTableAlias("log");
			String preQuery = " from DocfaDataLog as log ";
			preQuery += " where 1=1 ";
			Logger.log().info("prequery ", preQuery);			
			String query = "select distinct log "+preQuery;
			String countQuery = "select count (distinct log) "+preQuery;
			res.setSQuery(query);
			res.setCountSQuery(countQuery);
			res.addWhereClause(new WhereClause("idProtocollo", "log", req));
			res.addWhereClause(new WhereClause("uiuFoglio", "log", req));
			res.addWhereClause(new WhereClause("uiuNumero", "log", req));
			res.addWhereClause(new WhereClause("uiuSubalterno", "log", req));			
			res.addWhereClause(new WhereClause("proveninza", "log", req));			
//		}
		return res;
	}

	protected static boolean checkQueryParameter(HttpServletRequest req,
			String param) {
		return (req.getParameter(param) != null
				&& !req.getParameter(param).trim().equals("") && !"IGNORE"
				.equals(req.getParameter("clause_" + param)));
	}

	

	
}

