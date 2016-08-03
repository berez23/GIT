package it.webred.diogene.visualizzatore.runtime.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class PageContext implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List pageData; // Dati correnti nella pagina

	// numero di pagina corrente
	protected long pageNumber; 
	// numero totale delle pagine
	protected long totalNumberOfPages;
	// azione effettuata sulla pagina
	protected int action; 
	
	// ultima azione effettuata (la precedente)
	protected PageContext prevContext;
	
	// stringa sql di origine
	protected String sql;
	// parametri della query
	protected List sqlParams;
	// database di riferimento (ne null prende il DEFAULT)
	private String database;
	private long totalRecordNumber;
	
	public long getTotalRecordNumber()
	{
		return totalRecordNumber;
	}


	public void setTotalRecordNumber(long totalRecordNumber)
	{
		this.totalRecordNumber = totalRecordNumber;
	}


	protected int getAction() {
		return action;
	}


	public void setAction(int action) {
		this.action = action;
	}


	public PageContext getPrevContext() {
		return prevContext;
	}


	public void setPrevContext(PageContext prevContext) {
		this.prevContext = prevContext;
	}

	public PageContext() {
		pageNumber = 0;
		totalNumberOfPages = -1;
	}
	
	public PageContext(PageContext that) {
		this.pageData = that.pageData;
		this.pageNumber = that.pageNumber;
		this.totalNumberOfPages = that.totalNumberOfPages;
		this.action = that.action;
		this.sql = that.sql;
		this.sqlParams = that.sqlParams; 
		this.database = that.database;
	}
	
	
	public String getDatabase() {
		return (database==null?"DEFAULT":database);
	}


	public void setDatabase(String database) {
		this.database = database;
	}


	public boolean isFirstPage() {
		return (pageNumber == 1);
	}

	public boolean isLastPage() {
		return (pageNumber == totalNumberOfPages);
	}


	public List getPageData() {
		return pageData;
	}


	public void setPageData(List pageData) {
		this.pageData = pageData;
	}


	public long getPageNumber() {
		return pageNumber;
	}


	public void setPageNumber(long pageNumber) {
		this.pageNumber = pageNumber;
	}


	public long getTotalNumberOfPages() {
		return totalNumberOfPages;
	}


	public void setTotalNumberOfPages(long totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}	
	
	
	public Object[] getSqlParams() {
			return sqlParams.toArray();
	}


	public String getSql() {
		return sql;
	}


	public void setSql(String sql) {
		this.sql = sql;
	}


	public void setSqlParams(List sqlParams) {
		this.sqlParams = sqlParams;
	}


	
	
}
