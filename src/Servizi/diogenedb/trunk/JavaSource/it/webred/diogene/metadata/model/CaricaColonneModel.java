package it.webred.diogene.metadata.model;

import it.webred.diogene.db.DataJdbcConnection;
import it.webred.diogene.db.model.*;
import it.webred.diogene.metadata.beans.*;

import java.sql.Connection;
import java.util.*;

import org.hibernate.*;

/**
 * Classe che contiene metodi di accesso ai dati utilizzati dalla pagina di mappatura colonne.
 * @author Filippo Mazzini
 * @version $Revision: 1.3 $ $Date: 2007/08/03 14:42:04 $
 */
public class CaricaColonneModel extends CaricaTabelleModel {
	
	public CaricaColonneModel() {
		super();
	}
	
	/**
	 * Dato l'identificativo di una Expression, recupera l'identificativo della Column corrispondente. 
	 * @param exprId Identificativo dell'Expression
	 * @return L'identificativo della Column corrispondente
	 * @throws Exception Se si verifica una qualche eccezione SQL durante l'esecuzione del metodo
	 */
	public Long getColIdFromExpressionId(Long exprId) throws Exception {
		Long retVal = null;
		Session session = null;
		try {
			session = sf.openSession();			
			Query q = session.createQuery("from DC_COLUMN in class DcColumn where DC_COLUMN.dcExpression=:dcExpression");
			DcColumn dccol = new DcColumn();
			DcExpression dcexpr = new DcExpression();
			dcexpr.setId(exprId);
			dccol.setDcExpression(dcexpr);
			q.setProperties(dccol);
			List dccols = q.list();
			Iterator it = dccols.iterator();
			while (it.hasNext()) {
				DcColumn element = (DcColumn)it.next();
				retVal = element.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}		
		return retVal;
	}
	
	/**
	 * Per catalog, schema, tabella (e colonna) passati a parametro, legge i metadati del DB di origine e restituisce 
	 * i dati relativi alle colonne.
	 * @param catalog Il nome del catalog
	 * @param schemaPattern Il nome dello schema
	 * @param tableNamePattern Il nome della tabella
	 * @param columnNamePattern Il nome della colonna
	 * @return HashMap di chiavi String e valori Column, contenente i dati relativi alle colonne 
	 * @throws Exception Se si verifica una qualche eccezione SQL durante l'esecuzione del metodo
	 */
	public HashMap<String,Column> getColumns (String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws Exception {
		return getColumns(null, null, null, catalog, schemaPattern, tableNamePattern, columnNamePattern);
	}
	
	/**
	 * Per stringa di connessione, username, password, catalog, schema, tabella (e colonna) passati a parametro, legge 
	 * i metadati del DB di origine e restituisce i dati relativi alle colonne.
	 * @param connStr La stringa di connessione
	 * @param username L'username
	 * @param password La password
	 * @param catalog Il nome del catalog
	 * @param schemaPattern Il nome dello schema
	 * @param tableNamePattern Il nome della tabella
	 * @param columnNamePattern Il nome della colonna
	 * @return HashMap di chiavi String e valori Column, contenente i dati relativi alle colonne 
	 * @throws Exception Se si verifica una qualche eccezione SQL durante l'esecuzione del metodo
	 */
	public HashMap<String,Column> getColumns (String connStr,
												String username,
												String password,
												String catalog,
												String schemaPattern,
												String tableNamePattern,
												String columnNamePattern) throws Exception {
		HashMap<String,Column> retVal = new HashMap<String,Column>();
		Connection conn = null;
		try {
			if 	((connStr == null || connStr.equals("")) 
					&& (username == null || username.equals(""))
					&& (password == null || password.equals(""))) {
				conn = getDefaultConn();
				setMetadataFactory(DataJdbcConnection.getDefaultMdc().getDbtype());
			}else{
				conn = mf.getConnection(connStr, username, password, true);
			}		
			List<Column> list = mf.getColumns(conn, catalog, schemaPattern, tableNamePattern, columnNamePattern);
			for (Column col : list) {
				col.setLongDesc(col.getName()); //default
				retVal.put(col.getName(), col);
			}
		}catch (Exception e){
			throw e;
		}finally{
			mf.closeConnection(conn);
		}
		return retVal;
	}
	
}
