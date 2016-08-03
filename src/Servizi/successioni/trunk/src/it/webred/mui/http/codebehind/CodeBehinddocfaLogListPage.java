package it.webred.mui.http.codebehind;

import java.io.IOException;
import java.util.List;

import it.webred.docfa.model.DocfaDataLog;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.codebehind.pagination.HibernateQueryDisplayTagPaginator;
import it.webred.mui.http.codebehind.pagination.HibernateWhereQueryDisplayTagPaginator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CodeBehinddocfaLogListPage extends AbstractPage {

	/*public CodeBehinddocfaLogListPage(){
		super();
		setDefaultSortByField("iid");
		setListName("DocfaDataLogs");
	}

	protected HibernateQueryDisplayTagPaginator getQueryPaginator(HttpServletRequest req) {
		HibernateQueryDisplayTagPaginator paginator = null;
			paginator = new HibernateQueryDisplayTagPaginator(DocfaDataLog.class);
		return paginator;
	}
	*/
	
	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if(req.getAttribute("DocfaDataLogs") != null){
			req.getSession().setAttribute("DocfaDataLogs",req.getAttribute("DocfaDataLogs"));
		}
		if(req.getSession().getAttribute("DocfaDataLogs") instanceof HibernateWhereQueryDisplayTagPaginator){
			HibernateWhereQueryDisplayTagPaginator paginator = (HibernateWhereQueryDisplayTagPaginator)req.getSession().getAttribute("DocfaDataLogs");
			if (req.getParameter("page") != null) {
				try {
					int pageNumber = 1;
					pageNumber = Integer
							.valueOf(req.getParameter("page")).intValue();
					paginator.setPageNumber(pageNumber);
				} catch (NumberFormatException e) {
				}
			}
			if (req.getParameter("sort") != null) {
				try {
					paginator.setSortByField(req.getParameter("sort"));
				} catch (NumberFormatException e) {
				}
			}
			paginator.setSortAsc("asc".equalsIgnoreCase(req.getParameter("dir")) );			
		}
		return true;
	}

}
