package it.webred.mui.http.codebehind.pagination;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.WhereClause;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.skillbill.logging.Logger;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.hibernate.Query;
import org.hibernate.Session;

public class HibernateWhereQueryDisplayTagPaginator implements PaginatedList, Serializable {
	private String _sQuery = null;
	private String countSQuery = null;
	private String _resTableAlias = "c";
	private Integer size = null;
	
	private String sortByField = "";
	private boolean sortAsc = true;
	private int _pageNumber = 1;
	private int _resultsPerPage = 25;

	private  List<WhereClause> whereClauses = new ArrayList<WhereClause>();

	public String getSQuery() {
		return _sQuery;
	}

	public void setSQuery(String query) {
		_sQuery = query;
	}

	// Returns the size of the full list
	public int getFullListSize() {
		if(size == null){
			Session session = HibernateUtil.currentSession();
			String sQuery = getCountSQuery();
			Query query = null;
			Iterator<WhereClause> theIterator = whereClauses.iterator();
			while (theIterator.hasNext()) {
				WhereClause element = theIterator.next();
				sQuery = element.addClausetoQuery(sQuery);
			}
			Logger.log().info("query per count ", sQuery);
			query = session.createQuery(sQuery);
			theIterator = whereClauses.iterator();
			while (theIterator.hasNext()) {
				WhereClause element = theIterator.next();
				element.setParameter(query);
			}
			
			// in Hibernate 3.2 torna Long mentre in 3.1 Integer - bisogna prevenire
			// visto che credo dipenda anche dalla vers. di Oracle
			int rowCount = -99;
			try {
				rowCount = ((Integer) query.iterate().next()).intValue();
			} catch (Exception e) {
				
			}
			if (rowCount ==-99)
				rowCount = ((Long) query.iterate().next()).intValue();

			size = rowCount;

		
		}
		return size;
	}

	// Returns the current partial list
	public java.util.List getList() {

		Session session = HibernateUtil.currentSession();
		String sQuery = getSQuery();
		Query query = null;
		Iterator<WhereClause> theIterator = whereClauses.iterator();
		while (theIterator.hasNext()) {
			WhereClause element = theIterator.next();
			sQuery = element.addClausetoQuery(sQuery);
		}
		sQuery += getSortByClause();
		Logger.log().info("query per risultati ", sQuery);
		query = session.createQuery(sQuery);
		query.setMaxResults(_resultsPerPage);
		query.setFirstResult(_resultsPerPage * (_pageNumber -1));
		theIterator = whereClauses.iterator();
		while (theIterator.hasNext()) {
			WhereClause element = theIterator.next();
			element.setParameter(query);
		}
		List res = new ArrayList();
		for (Iterator it = query.iterate(); it.hasNext();) {
			res.add(it.next());
		}
		Logger.log().info(this.getClass().getName(),
				"importLog list evaluated, res.size="+res.size());
		return res;
	}
	public void addWhereClause(WhereClause wc){
		whereClauses.add(wc);
	}
	// Returns the number of objects per page.
	public int getObjectsPerPage() {
		return _resultsPerPage;
	}

	// Returns the page number of the partial list (starts from 1)
	public int getPageNumber() {
		return _pageNumber;
	}

	// Returns an ID for the search used to get the list.
	public String getSearchId() {
		return getSQuery();
	}

	// Returns the sort criterion used to externally sort the full list
	public String getSortCriterion() {
		return this.sortByField;
	}

	// Returns the sort direction used to externally sort the full list
	public SortOrderEnum getSortDirection() {
		return (isSortAsc()? SortOrderEnum.ASCENDING: SortOrderEnum.DESCENDING);
	}

	public void setPageNumber(int number) {
		_pageNumber = number;
	}

	public void setResultsPerPage(int perPage) {
		_resultsPerPage = perPage;
	}

	public boolean isSortAsc() {
		return sortAsc;
	}

	public String getSortByClause() {
		return ((sortByField != null && sortByField.trim().length()>0) ?" order by "+_resTableAlias+"."+sortByField+" "+(isSortAsc()?"asc":"desc"):"");
	}
	
	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public void setSortByField(String sortByField) {
		this.sortByField = sortByField;
	}

	public String getCountSQuery() {
		return countSQuery;
	}

	public void setCountSQuery(String countSQuery) {
		this.countSQuery = countSQuery;
	}

	public void setResTableAlias(String tableAlias) {
		_resTableAlias = tableAlias;
	}
}
