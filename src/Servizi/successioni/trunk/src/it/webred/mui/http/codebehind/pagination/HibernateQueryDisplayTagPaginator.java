package it.webred.mui.http.codebehind.pagination;

import it.webred.mui.hibernate.HibernateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.skillbill.logging.Logger;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.hibernate.Query;
import org.hibernate.Session;

public class HibernateQueryDisplayTagPaginator implements PaginatedList, Serializable {
	private Class _rowClass = null;
	private String _whereClause = "";
	private Long _parameterValue = null;
	private String _parameterName = null;
	
	private String sortByField = "";
	private boolean sortAsc = true;
	private int _pageNumber = 1;
	private int _resultsPerPage = 25;
	
	public HibernateQueryDisplayTagPaginator(Class rowClass) {
		_rowClass = rowClass;
	}
	
	public HibernateQueryDisplayTagPaginator(Class rowClass, String whereClause) {
		_rowClass = rowClass;
		_whereClause = whereClause;
	}

	public HibernateQueryDisplayTagPaginator(Class rowClass, String whereClause,String parameterName, Long parameterValue) {
		_rowClass = rowClass;
		_whereClause = whereClause;
		_parameterValue = parameterValue;
		_parameterName = parameterName;
	}

	// Returns the size of the full list
	public int getFullListSize() {
		Session session = HibernateUtil.currentSession();
		Query query = null;
		query = session.createQuery(
				"select count(c) from "+_rowClass.getName()+" as c "+_whereClause+" "+getSortByClause());
		if(_parameterName !=  null){
			query.setLong(_parameterName,_parameterValue);
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
		
		return rowCount;
	}

	// Returns the current partial list
	public java.util.List getList() {
		List miDupImportLogs = new ArrayList();
		Session session = HibernateUtil.currentSession();
		Query query = session.createQuery(
				"select c from "+_rowClass.getName()+" as c "+_whereClause+" "+getSortByClause());
		if(_parameterName !=  null){
			query.setParameter(_parameterName,_parameterValue);
		}
		Logger.log().info(this.getClass().getName(),
				"importLog list query"+query.getQueryString());
		query.setMaxResults(_resultsPerPage);
		query.setFirstResult(_resultsPerPage * (_pageNumber -1));
		for (Iterator it = query.iterate(); it.hasNext();) {
			miDupImportLogs.add(it.next());
		}
		Logger.log().info(this.getClass().getName(),
				"importLog list evaluated, res.size="+miDupImportLogs.size());
		return miDupImportLogs;
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
		return _whereClause+_parameterName+_parameterValue;
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
		return (sortByField != null?" order by c."+sortByField+" "+(isSortAsc()?"asc":"desc"):"");
	}
	
	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public void setSortByField(String sortByField) {
		this.sortByField = sortByField;
	}

}
