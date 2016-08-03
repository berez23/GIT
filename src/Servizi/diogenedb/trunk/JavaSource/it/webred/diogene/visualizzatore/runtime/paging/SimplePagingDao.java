package it.webred.diogene.visualizzatore.runtime.paging;

import java.util.HashMap;
import java.util.List;

public class SimplePagingDao extends GenericDAO
{
	private PageDefn	pd;
	private PageContext	context;

	public SimplePagingDao(PageDefn pd, PageContext context)
	{
		super();
		this.pd = pd;
		this.context = context;
	}

	public void executeQuery()
		throws Exception
	{
		long da =  (context.getPageNumber()-1)*pd.pageSize;
		context.setPageData(super.executeQuery(context.getSql(),null,context.getSqlParams(),context.getDatabase(),da,pd.pageSize));
		// calcolo il numero di record solo se ci sono record e se qualcuno non l'ha già fatto (metodo cache)
		if(context.getPageData()!= null &&  context.getPageData().size() > 0 && context.getTotalRecordNumber() < 1)
		{
			String totSql = context.getSql();
			totSql = totSql.substring(totSql.indexOf("from"),totSql.length());
			totSql ="select COUNT(*) as TotalRecordNumber "+ totSql ;
			List tot = super.executeQuery(totSql,null,context.getSqlParams(),context.getDatabase(),0,1);
			context.setTotalRecordNumber(new Long(((HashMap)tot.get(0)).get("TOTALRECORDNUMBER").toString()).longValue()) ;
		}

		context.setTotalNumberOfPages((int)Math.ceil(((double)context.getTotalRecordNumber())/((double)pd.getPageSize())));
	}

 
}
