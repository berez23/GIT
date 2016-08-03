package it.webred.diogene.querybuilder.control;

import static it.webred.utils.ReflectionUtils.clearAllFields;
import it.webred.diogene.db.model.DvUserEntity;
import it.webred.diogene.querybuilder.QueryBuilderException;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.querybuilder.db.EntitiesDBManager;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.utils.ASetAsAKey;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;

/**
 * Classe di interfacciamento per la lettura
 * dei metadati dal db.
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class MetadataController
{
	private final Logger log = Logger.getLogger(this.getClass());

	private Map<Long,DcEntityBean> entitiesMap = null;
	private EntitiesDBManager dbManager = null;
	private boolean reloadNextRead = false;
	
	/**
	 * Resetta la classe.
	 */
	public void initializeAll()
	{		
		try {clearAllFields(this, false, true);}
		catch (IllegalAccessException e) {}
	}

	/**
	 * Restituisce la classe di accesso ai metadati.
	 * @return
	 */
	private EntitiesDBManager getDbManager()
	{
		if (dbManager == null)
			dbManager = new EntitiesDBManager();
		return dbManager;
	}
	/**
	 * Questo metodo dovrebbe essere chiamato
	 * allo scadere della sessione in cui &egrave; coinvolto
	 * per assicurare la pronta chiusura della connessione
	 * al DB (vedi {@link QueryBuilderSessionListener#sessionDestroyed(HttpSessionEvent)}).
	 */
	public void endSesssion()
	{
		entitiesMap = null;
		if (dbManager != null)
		{
			dbManager.chiudiTutto();
			dbManager = null;
		}
	}
	/**
	 * Esegue una query scalare(risultato una riga con una colonna) utilizzando una connessione JDBC limitando 
	 * mantenendo il tipo Java ritornato dalla query.
	 *  
	 * @param statement la query da eseguire.
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object executeStatementScalar(String statement) 
	throws SQLException, Exception
	{
		Connection conn = getDbManager().getJdbcConnection();
		try
		{
			Statement st = conn.createStatement();
			st.setFetchSize(1);
			st.setMaxRows(1);
			ResultSet rs = st.executeQuery(statement);
			Object obj=null;
			if(rs.next()){
				obj= rs.getObject(1);
			}
					
			return obj;
		}
		finally
		{
			if (conn != null)
				conn.close();
		}
	}

	/**
	 * Esegue una query utilizzando una connessione JDBC limitando il numero di righe ritornate
	 * mantenendo i tipi Java ritornati dalla query.
	 *  
	 * @param statement la query da eseguire.
	 * @param rows il numero di righe massime da tornare
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object[]> executeStatementList(String statement, int rows) 
	throws SQLException, Exception
	{
		Connection conn = getDbManager().getJdbcConnection();
		try
		{
			Statement st = conn.createStatement();
			st.setFetchSize(rows);
			st.setMaxRows(rows);
			ResultSet rs = st.executeQuery(statement);
			ResultSetMetaData metaData = rs.getMetaData();
			int cols = metaData.getColumnCount();
			
			ArrayList<Object[]> righe = new ArrayList<Object[]>();
			
			String[] rec = new String[cols];
			for (int col = 0; col < cols; rec[col] = metaData.getColumnName(++col));
			righe.add(rec);
			for (int row = 1; row <= rows && rs.next(); row++){
				String[] rec1 = new String[cols];
				for (int col = 1; col <= cols; col++)
				{
					Object obj = rs.getObject(col);
					if (obj != null){
						rec1[col - 1]= obj.toString();
					}
				}
				righe.add(rec1);
			}

			return righe;
		}
		finally
		{
			if (conn != null)
				conn.close();
		}
	}
	/**
	 * Esegue una query utilizzando una connessione JDBC limitando il numero di righe ritornate
	 * che sono convertite a String utilizzando il metodo standard .toString() dei tipi Java 
	 * ritornati dalla query.
	 *  
	 * @param statement la query da eseguire.
	 * @param rows il numero di righe massime da tornare
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public String[][] executeStatement(String statement, int rows) 
	throws SQLException, Exception
	{
		Connection conn = getDbManager().getJdbcConnection();
		try
		{
			Statement st = conn.createStatement();
			st.setFetchSize(rows);
			st.setMaxRows(rows);
			ResultSet rs = st.executeQuery(statement);
			ResultSetMetaData metaData = rs.getMetaData();
			int cols = metaData.getColumnCount();
			
			ArrayList righe = new ArrayList();
			
			String[] rec = new String[cols];
			for (int col = 0; col < cols; rec[col] = metaData.getColumnName(++col));
			righe.add(rec);
			for (int row = 1; row <= rows && rs.next(); row++){
				String[] rec1 = new String[cols];
				for (int col = 1; col <= cols; col++)
				{
					Object obj = rs.getObject(col);
					if (obj != null){
						rec1[col - 1]= obj.toString();
					}
				}
				righe.add(rec1);
			}
			String[][] result = new String[righe.size()][cols];

			return (String[][]) righe.toArray(result);
		}
		finally
		{
			if (conn != null)
				conn.close();
		}
	}
	/**
	 * Esegue una query hql sul dizionario dei metadati ritornado uno scalare 
	 * o null nel caso non vi siano risultati.
	 *  
	 * @param statement la query HQL da eseguire.
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object executeHqlStatementScalar(String statement) 
	throws QueryBuilderException
	{
		Object ret=null;
		List lista;
		try
		{
			lista = getDbManager().executeHqlStatement(statement,1);
			if(lista.size()==1)ret=lista.get(0);
		}
		catch (Exception e)
		{
			log.error(e);
			throw new QueryBuilderException("common.general.errorMessage",null,e.getClass().getName(), e.getMessage());
		}
		return ret;
	}
	
	/**
	 * Esegue una query hql sul dizionario dei metadati limitando il numero di righe ritornate
	 * ritornate dalla query.
	 *  
	 * @param statement la query HQL da eseguire.
	 * @param rows il numero di righe massime da tornare
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List executeHqlStatement(String statement, int rows) 
	throws SQLException, Exception
	{
		return getDbManager().executeHqlStatement(statement,rows);
	}
	
	/** Recupera una DcEntity partendo dal suo id
	 * 
	 * @param id Chiave primaria della DcEntity
	 * @return
	 * @throws QueryBuilderException
	 */
	public DvUserEntity getDvUserEntity(Long id) throws QueryBuilderException{
		return getDbManager().getDvUserEntity(id);
	}
	/**
	 * Restituisce tutte le entit&agrave; esistenti,
	 * mappate secondo il loro id.
	 * @return
	 */
	synchronized Map<Long, DcEntityBean> getEntitiesMap(Long... entitiesToExclude)
	{
		if (entitiesMap == null || reloadNextRead){
			setReloadNextRead(false);
			reloadEntitiesMap(entitiesToExclude);
		}
		return entitiesMap;
	}
	UserEntityBean readUserEntity(Long id, SqlGenerator sqlGenerator) 
	throws QueryBuilderException, Exception
	{
		try
		{
			return getDbManager().getUserEntity(id, sqlGenerator);
		}
		catch (QueryBuilderException e) {throw e;}
		catch (Exception e) {throw e;}
	}
	/**
	 * Tenta di salvare sul db una nuova entit&agrave; utente.
	 * Dopo il salvataggio, fa in modo che la lista delle
	 * entit&agrafe; restituita da
	 * {@link MetadataController#getEntitiesMap()} 
	 * venga aggiornata, chiamando
	 * {@link MetadataController#reloadEntitiesMap()}
	 * @param bean
	 * @throws QueryBuilderException
	 * @throws Exception
	 */
	void writeUserEntity(UserEntityBean bean, SqlGenerator sqlGenerator) 
	throws QueryBuilderException, Exception
	{
		try
		{
			getDbManager().writeUserEntity(bean, sqlGenerator);
		}
		catch (QueryBuilderException e) {throw e;}
		catch (Exception e) {throw e;}
	}
	/**
	 * Tenta di salvare sul db una nuova entit&agrave; utente.
	 * Dopo il salvataggio, fa in modo che la lista delle
	 * entit&agrave; restituita da
	 * {@link MetadataController#getEntitiesMap()} 
	 * venga aggiornata, chiamando
	 * {@link MetadataController#reloadEntitiesMap()}
	 * @param bean
	 * @throws QueryBuilderException
	 * @throws Exception
	 */
	void updateUserEntity(UserEntityBean bean, SqlGenerator sqlGenerator) 
	throws QueryBuilderException, Exception
	{
			getDbManager().updateUserEntity(bean, sqlGenerator);
	}
	
	/**
	 * Recupera una Lista di Object[] con le relazioni(DcRel) 
	 * e gli id(Long) delle entit&agrave; contenute in listaId.
	 * 
	 * @param id della entit&agrave; della quale cerco le relazioni
	 * @param listaId lista degli id delle entit&agrave; da verificare.
	 * @return Lista
	 * @throws QueryBuilderException
	 */
	public List getRelationBetweenEntities(Long id, ArrayList<Long> listaId  ) throws QueryBuilderException{
		return getDbManager().getRelationBetweenEntities(id,listaId);
	}
	/**
	 * @param bean
	 * @throws QueryBuilderException
	 * @throws Exception
	 */
	void deleteUserEntity(Long id) 
	throws QueryBuilderException, Exception
	{
		try
		{
			getDbManager().deleteUserEntity(id);
			reloadEntitiesMap();
		}
		catch (QueryBuilderException e) {throw e;}
		catch (Exception e) {throw e;}
	}

	/**
	 * Rilegge la lista delle entit&agrave;
	 */
	private void reloadEntitiesMap(Long... entitiesToExclude)
	{
		entitiesMap = Collections.synchronizedMap(new HashMap<Long,DcEntityBean>());
		try
		{
			for (DcEntityBean item : getDbManager().getAllEntities())
			{
				boolean grant = true;
				if (entitiesToExclude != null)
					for (int i = 0; i < entitiesToExclude.length && grant; grant = !item.getId().equals(entitiesToExclude[i++]));
				//Controllo i diritti in base all'utente
				grant= grant && (item.isEntityViewable()||item.isEntityEditable());
				if (grant)
					getEntitiesMap().put(item.getId(), item);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public void saveOrUpdateRelationInDb(GenericCondition cond) throws QueryBuilderException, Exception

	{
		getDbManager().saveOrUpdateRelationInDb(cond);
	}
	public Map<ASetAsAKey,Object[]> retrieveRelations(SqlGenerator sqlGen)throws QueryBuilderException, Exception
	{
		return getDbManager().retrieveRelations(sqlGen);
	}
	public void deleteUpdateRelationInDb(GenericCondition cond) throws QueryBuilderException, Exception

	{
		getDbManager().deleteRelationInDb(cond);
	}
	/**
	 * Restituisce tutte le entit&agrave; esistenti,
	 * mappate secondo il loro id.
	 * @return
	 */
	synchronized void refreshEntitiesMap()
	{
		//if (entitiesMap == null)
		getDbManager().flushSession();
		reloadEntitiesMap();
	}

	public boolean isReloadNextRead() {
		return reloadNextRead;
	}

	public synchronized void setReloadNextRead(boolean reloadNextRead) {
		this.reloadNextRead = reloadNextRead;
	}
	
	
}
