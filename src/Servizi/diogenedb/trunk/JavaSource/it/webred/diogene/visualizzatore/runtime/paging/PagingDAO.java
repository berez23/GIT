package it.webred.diogene.visualizzatore.runtime.paging;

import it.webred.diogene.db.DataJdbcConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.SQLQueryImpl;


public class PagingDAO extends GenericDAO {


	public PagingDAO() {
	}



	public void executeQuery  (
		PageDefn descriptor,
		PageContext context,
		boolean newPage)
		throws PagingException {

		int action = context.getAction();

		// current viene settato dalla classe PageCommand 
		// nel caso che si prema next sull'iltima pagina o previous sulla prima
		// il questo caso il valore restituito è quello dell'ultima ricerca
		if (action == PageDefn.PageAction.CURRENT) {
			PageContext prevContext = context.getPrevContext();
			executeQuery(descriptor, prevContext,false);
			context.setPageData(prevContext.pageData);
			// ritorno i dati che c'erano già nel PrevContext
			return;
		} else {
			// SALVO IL CONTESTO CORRENTE COME PRECEDENTE - PRIMA DI VALORIZZARLO
			PageContext prevContext = new PageContext(context);
			context.setPageData(null);
			context.setPrevContext(prevContext);
		}

		

			
		// newPage sta ad indicare che è una pagina effetto di una ricerca e non è la previous page
		if (newPage) {

			switch (action) {
				case PageDefn.PageAction.FIRST :
					context.setPageNumber(1);
					break;
				case PageDefn.PageAction.PREVIOUS :
					context.setPageNumber(context.getPageNumber() - 1);
					break;
				case PageDefn.PageAction.NEXT :
					context.setPageNumber(context.getPageNumber() + 1);
					break;
			};

			
		// PRENDO L'SQL DAL CONTEXT
		String sql = context.getSql();
		// prendo i parametri per l'sql
		Object[] params = context.getSqlParams();
		// eseguo l'sql
		List results;
		try {
			int from = 0;
			if (action == PageDefn.PageAction.PREVIOUS) {
				from = (int) (descriptor.getPageSize()*(context.pageNumber-1));
			} else {
				from = (int) (descriptor.getPageSize()*context.pageNumber) - descriptor.getPageSize();
			}
			results = super.executeQuery(
				sql,null,params,context.getDatabase(), from, descriptor.getPageSize());
		} catch (Exception e) {
			throw new PagingException(e.getMessage());
		}



		context.setPageData(results);


			// setto il numero totale di pagine ma soltanto alla prima richiesta
			if (action == PageDefn.PageAction.FIRST) {
				context.setTotalNumberOfPages(
					getTotalNumberOfPages(context, descriptor));
			}

			// se la ricerca non ha restituito risultato (sono arrivato a fine lista)
			// allora il pageNumber non deve essere incrementato
			if (results.size()==0)
				context.setPageNumber(context.getPageNumber() - 1);

			
		} 
		// Alle fane dell'esecuzione della query setto il prevContext con il contesto attuale
		// il prevcontext potrà essere ripescato nel caso che l'operazione sia quella di CURRENT
		if (context.getPrevContext() == null) {
			PageContext prevContext = new PageContext(context);
			context.setPageData(null);
			context.setPrevContext(prevContext);
		}

	}

	/*
	 * Restituisce il numero totale di pagine della ricerca
	 */
	protected long getTotalNumberOfPages(PageContext context, PageDefn definition)
		throws PagingException {
		Session s = null;
		ScrollableResults scroll = null;
   	 try {
		
    	 
			return 0;

   	 } catch (Exception e) {
			throw new PagingException(e.getMessage());
	 }
   	 finally {
   		 if (s!=null) 
   			 s.close();
   		 if (scroll!=null)
   			 scroll.close();
   	 }
	}
	




}
