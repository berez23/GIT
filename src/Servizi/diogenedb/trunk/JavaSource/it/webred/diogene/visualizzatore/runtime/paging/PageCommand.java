package it.webred.diogene.visualizzatore.runtime.paging;

import java.util.ArrayList;
import java.util.List;


public class PageCommand {

	private PageContext context;
	private PageDefn descriptor;
	
	/*
	 * Costruttore al quale pasare la definizione della pagina lista, la stringa sql con i parametri posizionali
	 * come punti interrogativi e il valore dei parametri in un ArrayList di valori
	 * 
	 */
	public PageCommand(PageDefn descriptor, String sql, List params, String database) {
		this.context = new PageContext();
		this.descriptor = descriptor;
		context.setAction(PageDefn.PageAction.FIRST);
		context.sql = sql;
		context.sqlParams = params==null ? new ArrayList():params ;
		context.setDatabase(database);
	}
	
	private void setAction(int action) {
		if (context.getPageNumber() == 0) {
			context.setAction(PageDefn.PageAction.FIRST);
		}
		else if (context.isFirstPage() && (action == PageDefn.PageAction.PREVIOUS)) {
			context.setAction(PageDefn.PageAction.CURRENT);
		}
		else if (context.isLastPage() && (action == PageDefn.PageAction.NEXT)) {
			context.setAction(PageDefn.PageAction.CURRENT);
		}
		else {
			context.setAction(action);
		}
          // context.setPageData(null);
	}

	private PageContext execute()  {
		PagingDAO pdao = new PagingDAO();
		

		try {
			pdao.executeQuery(descriptor,context, true);
		} catch (PagingException e) {
			e.printStackTrace();
		}
		return context;
	}


	public PageContext executeFirst() {
		this.setAction(PageDefn.PageAction.FIRST);
		this.context = this.execute();
		return context;
	}

	public PageContext executeNext() {
		this.setAction(PageDefn.PageAction.NEXT);
		this.context = this.execute();
		return context;
	}
	
	public PageContext executePrevious() {
		this.setAction(PageDefn.PageAction.PREVIOUS);
		this.context = this.execute();
		return context;
	}

	
	
	public static void main(java.lang.String[] args) {

		PageDefn descriptor = new PageDefn(10);
		
		ArrayList params = new ArrayList();
		params.add(new Integer(0));

		PageCommand pcommand = new PageCommand(descriptor, "select id as id, id_ext as id_ext  from sit_comune where id > ?",params,null);
		
		pcommand.executeFirst();
		
		pcommand.executeNext();

		pcommand.executeNext();
		pcommand.executePrevious();

	}
	
}
