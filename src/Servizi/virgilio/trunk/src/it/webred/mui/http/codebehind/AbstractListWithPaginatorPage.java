package it.webred.mui.http.codebehind;

import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.codebehind.pagination.HibernateQueryDisplayTagPaginator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractListWithPaginatorPage extends AbstractPage {

	private String defaultSortByField = null;
	private String listName = "elements";
	private boolean defaultSortByDirection = true;
	
	@Override
	protected final boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {

		if (req.getMethod().equalsIgnoreCase("post")) {
			int resultsPerPage = 25;
			if (req.getParameter("resultsPerPage") != null) {
				try {
					resultsPerPage = Integer
							.valueOf(req.getParameter("resultsPerPage")).intValue();
				} catch (NumberFormatException e) {
				}
			}
			HibernateQueryDisplayTagPaginator paginator = getQueryPaginator(req);
			paginator.setSortByField(defaultSortByField);
			paginator.setResultsPerPage(resultsPerPage);
			paginator.setSortAsc(defaultSortByDirection);
			req.getSession().setAttribute(listName, paginator);
		}
		else{
			HibernateQueryDisplayTagPaginator paginator = (HibernateQueryDisplayTagPaginator)req.getSession().getAttribute(listName);
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
			if(req.getParameter("dir") != null && req.getParameter("dir").trim().length()>0){
				paginator.setSortAsc("asc".equalsIgnoreCase(req.getParameter("dir")) );			
			}
		}
		return true;
	}

	protected abstract HibernateQueryDisplayTagPaginator getQueryPaginator(HttpServletRequest req);

	protected void setDefaultSortByField(String defaultSortByField) {
		this.defaultSortByField = defaultSortByField;
	}

	protected void setListName(String listName) {
		this.listName = listName;
	}

	protected void setDefaultSortByDirection(boolean defaultSortByDirection) {
		this.defaultSortByDirection = defaultSortByDirection;
	}

}
