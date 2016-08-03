package it.webred.diogene.visualizzatore.runtime.paging;

import it.webred.diogene.db.DataJdbcConnection;
import it.webred.diogene.db.HibernateSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.engine.query.NativeSQLQueryPlan;
import org.hibernate.engine.query.NativeSQLQuerySpecification;
import org.hibernate.type.Type;

public class GenericDAO {

    public DataBaseRowConverter defaultConverter;

    /**
     * Construct vuoto, prende il default converter
     */
    public GenericDAO() {
        this.defaultConverter = new MapConverter();
    }

    /**
     * COSTRUTTURE CHE ACCETTA Un converter
     */
    public GenericDAO(DataBaseRowConverter converter) {
        this.defaultConverter = converter;
    }

   
    /**
     * Esegue una query nel database
     * converte ogni riga del resultset attraverso il converter definito
     * 
     * se converter è null viene preso il default converter
     * @throws Exception 
     */
    public List executeQuery(String rawsql,DataBaseRowConverter converter, Object[] params, String database, long fromrow, long nrow ) throws Exception{
    	// marcoric Session s = null;
    	Connection conn=null;
    	try {
	    	List results = new ArrayList();
	    	if (database==null)
	    		database = "DEFAULT";
	    	// marcoric s = DataJdbcConnection.getHibernateSession(database);
	    	conn = DataJdbcConnection.getConnectionn(database);

	    	DataBaseRowConverter converterToUse = (converter == null) ? defaultConverter : converter;
	
	        
	        // SQLQuery non usata auutalmente a causa del fatto che il metodo getReturnedAlias() non
	        // è supportato
	    	// marcoric SQLQuery sqlQuery = s.createSQLQuery(rawsql);
	    	
	    	long untilRow = fromrow + nrow;
	    	String sqlPaginato = "select * from (" + rawsql + ") where rownum>" +fromrow + " and rownum < " + untilRow;
	    	sqlPaginato = "SELECT sel2.* FROM   (";
	    	sqlPaginato = sqlPaginato + "SELECT sel1.*, rownum AS row_num FROM   (";
	    	sqlPaginato = sqlPaginato + rawsql;
	    	sqlPaginato = sqlPaginato +") sel1 WHERE  rownum <=" +untilRow;
	    	sqlPaginato = sqlPaginato +") sel2 WHERE  row_num > " + fromrow;
	    	
			PreparedStatement ps = conn.prepareStatement(sqlPaginato);
        	this.substituteParams(ps,params);

			ResultSet rs = ps.executeQuery(); 
	    	 

	    	 
	    	 // marcoric sqlQuery.setFirstResult(fromrow);
	    	 // marcoric sqlQuery.setMaxResults(nrow);
    	 
	         // iTERAZIONE SUL RISLTATO DELLA QUERY E CONVERSIONE DI OGNI
	    	 // RIGA IN UNA MAPPA
	    	 while (rs.next()) {
                Object convertedRow = converterToUse.toObject(rs);
	 		   
	             if (convertedRow != null) {
	            	 // AGGIUNGO LA RIGA ALLA LISTA DI RIDULTATI DELLA PAGINA
	                 results.add(convertedRow);
	             }
	         }
	         
	
	    	return results;
    	} catch (Exception e)  {
    		throw new Exception(e);
    	} finally {
    		if (conn!=null)
    			conn.close();
    	}
    	
    }
    

 
	public void substituteParams(PreparedStatement sql, Object[] params)
	throws Exception {
	for (int i = 0; i < params.length; ++i) {

		if (params[i] instanceof String)  
			sql.setString(i+1 , (String)params[i]);
		else
		if (params[i] instanceof java.sql.Date)  
			sql.setDate(i+1 , (java.sql.Date)params[i]);
		else
		if (params[i] instanceof Double)  
			sql.setDouble(i+1 , (Double)params[i]);
		else
		if (params[i] instanceof Long)  
			sql.setLong(i+1, ((Long)params[i]).longValue());
		else
		if (params[i] instanceof Integer)  
			sql.setInt(i+1 , ((Integer)params[i]).intValue());
		else
			new Exception("substituteParams - Tipo di dato del parametro sql non previsto");
	}
}
  



}
