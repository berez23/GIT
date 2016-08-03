package it.webred.mui.http.codebehind;

import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.codebehind.pagination.HibernateWhereQueryDisplayTagPaginator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CodeBehindnoteListPage extends AbstractPage {

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if(req.getAttribute("MiDupNotaTrass") != null){
			req.getSession().setAttribute("MiDupNotaTrass",req.getAttribute("MiDupNotaTrass"));
		}
		if(req.getSession().getAttribute("MiDupNotaTrass") instanceof HibernateWhereQueryDisplayTagPaginator){
			HibernateWhereQueryDisplayTagPaginator paginator = (HibernateWhereQueryDisplayTagPaginator)req.getSession().getAttribute("MiDupNotaTrass");
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
//		else{
//			req.setAttribute("MiDupNotaTrass",req.getSession().getAttribute("MiDupNotaTrass"));
//		}
		return true;
	}


}
