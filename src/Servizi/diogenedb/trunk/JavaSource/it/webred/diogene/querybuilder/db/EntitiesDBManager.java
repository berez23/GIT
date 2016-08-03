package it.webred.diogene.querybuilder.db;

import static it.webred.diogene.querybuilder.Constants.ALIAS;
import static it.webred.diogene.querybuilder.Constants.TABLE;
import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;
import static it.webred.utils.CollectionsUtils.getUniqueElement;
import static it.webred.utils.CollectionsUtils.isEmpty;
import static it.webred.utils.StringUtils.isEmpty;
import it.webred.diogene.correlazione.beans.TipoEtichetta;
import it.webred.diogene.db.DataJdbcConnection;
import it.webred.diogene.db.DcAttributes;
import it.webred.diogene.db.HibernateSession;
import it.webred.diogene.db.model.DcAttribute;
import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.db.model.DcEntity;
import it.webred.diogene.db.model.DcEntityRel;
import it.webred.diogene.db.model.DcExpression;
import it.webred.diogene.db.model.DcFromRel;
import it.webred.diogene.db.model.DcRel;
import it.webred.diogene.db.model.DcSchemaEntity;
import it.webred.diogene.db.model.DcTipoe;
import it.webred.diogene.db.model.DcTipoeColumn;
import it.webred.diogene.db.model.DvLink;
import it.webred.diogene.db.model.DvTema;
import it.webred.diogene.db.model.DvUeFromEntity;
import it.webred.diogene.db.model.DvUeGroup;
import it.webred.diogene.db.model.DvUeOrder;
import it.webred.diogene.db.model.DvUserEntity;
import it.webred.diogene.db.model.am.AmApplication;
import it.webred.diogene.db.model.am.AmApplicationItem;
import it.webred.diogene.db.model.am.AmItem;
import it.webred.diogene.querybuilder.DcColumnEvaluator;
import it.webred.diogene.querybuilder.QueryBuilderException;
import it.webred.diogene.querybuilder.RightsManager;
import it.webred.diogene.querybuilder.Translator;
import it.webred.diogene.querybuilder.beans.DcColumnBean;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.querybuilder.beans.DcSchemaEntityBean;
import it.webred.diogene.querybuilder.beans.DvUserEntityBean;
import it.webred.diogene.querybuilder.control.GenericCondition;
import it.webred.diogene.querybuilder.control.OrderedEntities;
import it.webred.diogene.querybuilder.control.UserEntityBean;
import it.webred.diogene.querybuilder.control.JoinSpec;
import it.webred.diogene.querybuilder.control.UserEntityColumnBean;
import it.webred.diogene.querybuilder.control.UserEntityColumnBeanAppendedValue;
import it.webred.diogene.sqldiagram.Condition;
import it.webred.diogene.sqldiagram.ConditionFactory;
import it.webred.diogene.sqldiagram.ExpressionFactory;
import it.webred.diogene.sqldiagram.OperatorFactory;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.DBType;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import it.webred.permessi.GestionePermessi;
import it.webred.utils.ASetAsAKey;
import it.webred.utils.IdentitySet;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.QuietWriter;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * 
 * @author marcoric - Giulio
 * Questa classe legge dal db, tramite Hibernate, le entit&agrave;
 * con tutte le relazioni che intrattengono.
 *
 */
/**
 * TODO Scrivi descrizione
 * @author Giulio Quaresima
 * @version $Revision: 1.13.2.1 $ $Date: 2012/01/26 16:59:39 $
 */
public class EntitiesDBManager
{
	public enum SqlStatementAttribute
	{
		EXPLICIT,
		GENERATED;
	}

	public static final String APPLICATION_XML_RELATIVE_PATH = "WEB-INF/application1.xml";

	public static final String RIGHTS_SESSION_FACTORY_CONF_FILE = "hibernate.cfg2.xml";

	
	private final Logger log = Logger.getLogger(this.getClass());
	private Connection jdbcConnection;
	
	private Map<String,Session> sexionMap = Collections.synchronizedMap(new HashMap<String,Session>());
	private Map<String,Object> sessionOwnerMap= Collections.synchronizedMap(new HashMap<String,Object>());
	private Map<String,SessionManager> sessionManagerMap= Collections.synchronizedMap(new HashMap<String,SessionManager>());
	private Map<Long,DcEntityBean> entitiesMap;
	private Collection<DcEntityBean> allEntities;

	public EntitiesDBManager()
	{
	}

	public Connection getJdbcConnection()
	{
		try
		{
			if (jdbcConnection == null || jdbcConnection.isClosed())
				jdbcConnection = DataJdbcConnection.getConnectionn("DWH");
				//jdbcConnection = DataJdbcConnection.getConnectionn("DBTOTMI");
		} 
		catch (Exception e)
		{
			log.error("", e);
		}
		return jdbcConnection;
	}

	public void chiudiTutto()
	{
		synchronized (sessionManagerMap)
		{
			for (Map.Entry<String,SessionManager> entry : sessionManagerMap.entrySet())
			{
				entry.getValue().notify();	
			}
			
		}
		synchronized (this)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				log.error("", e);
			}
		}
		setEntitiesMap(null);
	}


	private Map<Long,DcEntityBean> getEntitiesMap(Session session, boolean refresh) 
	throws Exception
	{
		if (entitiesMap == null || refresh)
		{
			entitiesMap = new HashMap<Long,DcEntityBean>();										
			
			for (DcEntityBean item : getAllEntities())
			{
				entitiesMap.put(item.getId(), item);
			}			
		}
		return entitiesMap;
	}

	private void setEntitiesMap(Map<Long, DcEntityBean> entitiesMap)
	{
		this.entitiesMap = entitiesMap;
	}
	
	/** Metodo per eseguire una query HQL sul DB del Dizionario di metadati
	 *  
	 * @param hql la query
	 * @param rowMax il numero massimo di righe ritornato dalla query (0 = tutte le righe).
	 * @return
	 * @throws Exception
	 */
	public List executeHqlStatement(String hql,int rowMax) throws Exception{
		return executeHqlStatement(hql,rowMax,getSession());
	}
	private List executeHqlStatement(String hql,int rowMax, Session session){
		Query q= session.createQuery(hql);
		if(rowMax>0)q.setMaxResults(rowMax);
		return q.list(); 
	}
	
	public Collection<DcEntityBean> getAllEntities() 
	throws QueryBuilderException
	{
		try
		{
			return getAllEntities(getSession());
		}
		catch (Exception e)
		{
			log.error(this.getClass().getName() + ".getAllEntities", e); 
			throw new QueryBuilderException("common.general.errorMessage",e.getClass().getName() , e.getMessage());
		}
	}

	public DcEntityBean getDcEntityBean(Session session, Long entityId) throws Exception {
	try {
		Principal principal = null;
		String context = null;
		if (getContext() != null && getContext().getExternalContext() != null) {
			principal = getContext().getExternalContext().getUserPrincipal();
			context =getContext().getExternalContext().getRequestContextPath().substring(1);
		}
		
		Query q = session.createQuery("from DcEntity e where e.id=:entityId");
		q.setParameter("entityId",entityId);
		DcEntity item= (DcEntity) q.uniqueResult();
		DcEntityBean entity = null;
		boolean isExplicitSQL=false;
		if (item.getDcSchemaEntities().size() == 1)
		{
			DcSchemaEntity sEntity = (DcSchemaEntity) getUniqueElement(item.getDcSchemaEntities());
			entity = new DcSchemaEntityBean(
					item.getId(), 
					sEntity.getSqlName(), 
					item.getName(), 
					item.getLongDesc(),
					item.getDtMod()
					);
		}
		else if (item.getDvUserEntities().size() == 1)
		{
			DvUserEntity uEntity = (DvUserEntity) getUniqueElement(item.getDvUserEntities());
			String xml = new String(uEntity.getSqlStatement(), UTF8_XML_ENCODING);
			isExplicitSQL=!isEmpty(xml);
			entity = new DvUserEntityBean(
					item.getId(), 
					item.getName(), 
					item.getLongDesc(),
					item.getDtMod()
					);
			
		}
		else throw new QueryBuilderException(
				"C'\u00E8 un'incoerenza grave nel db: " +
				"il record di DC_ENTITY con id " + 
				item.getId() + 
				" dovrebbe essere puntato o da uno " +
				"ed un solo record di DC_SCHEMA_ENTITY " +
				"o da uno ed un solo record di DV_USER_ENTITY, " +
				"ma non \u00E8 cos\u00EC", null);
		
		entity.setColumns(getColumns(session, item));
		//Imposto i diritti sull'entità
		if(principal == null && context == null) {
			entity.setEntityViewable(true);
			entity.setEntityEditable(false);
			entity.setEntityDeletable(false);
		}else{
			/* TODO: MODIFICARE GESTIONE PERMESSI
			entity.setEntityViewable(RightsManager.isRightGranted(principal,item.getName(),context,GestionePermessi.READ_ENTITY));
			entity.setEntityEditable(RightsManager.isRightGranted(principal,item.getName(),context,GestionePermessi.WRITE_ENTITY));
			entity.setEntityDeletable(RightsManager.isRightGranted(principal,item.getName(),context,GestionePermessi.DELETE_ENTITY));
			*/
			// HO MESSO QUESTE RIGHE SOTTO AL POSTO DI QUELLE SOPRA
			entity.setEntityViewable(true);
			entity.setEntityEditable(true);
			entity.setEntityDeletable(true);
			
		}
		entity.setEntityExplictSQL(isExplicitSQL);

		return entity;
		

	}
	catch (Throwable e)
	{
		log.error("Errore nel recupero DcEntityBean per id=" + entityId, e); 
		throw new Exception(e);
	}
	finally
	{
		getSession().flush();
	}
	

	
	}
 	
	@SuppressWarnings("unchecked")
	synchronized private Collection<DcEntityBean> getAllEntities(Session session) 
	throws Exception
	{

		//Se non ho mai caricato la lista allora la creo nuova
		if(allEntities==null)allEntities=new TreeSet<DcEntityBean>();
		
		Principal principal = null;
		String context = null;
		if (getContext() != null && getContext().getExternalContext() != null) {
			principal = getContext().getExternalContext().getUserPrincipal();
			context =getContext().getExternalContext().getRequestContextPath().substring(1);
		}
		//carico tutte le date di ultima modifica delle entità
		List<Object[]> ultime = executeHqlStatement("select e.id, e.dtMod, e.name from DcEntity e",0);
		HashMap<Long,Date> leUtime= new HashMap<Long,Date>();
		for (Object[] riga : ultime)
		{
			//controllo se ho i diritti di lettura  
			if((principal == null && context == null) || true ) // TODO C'ERA QUESTO ALPOSTO DEL TRUE --> RightsManager.isRightGranted(principal,(String)riga[2],context,GestionePermessi.READ_ENTITY))
			leUtime.put((Long)riga[0],(Date)riga[1]);
		}
		// entità cancellate 
		ArrayList<DcEntityBean> toRemove= new ArrayList<DcEntityBean>() ;
		// Entita Aggiunte/Modificate
		ArrayList<Long> toReload= new ArrayList<Long>() ;
		
		for (DcEntityBean bean : allEntities)
		{		
			if (leUtime.containsKey(bean.getId())){
				Date dataMod=  leUtime.get(bean.getId());
				if (dataMod!=null && (bean.getDtMod()==null ||  bean.getDtMod().before(dataMod) )){
					toReload.add(bean.getId());
					toRemove.add(bean);
				}
				leUtime.remove(bean.getId());
			}else{
				toRemove.add(bean);
				leUtime.remove(bean.getId());
			}
		}
		for (Map.Entry<Long,Date> entry : leUtime.entrySet())
		{
			toReload.add(entry.getKey());
		}
		//Rimuovo le entità che non ci sono più o che sono da aggiornare
		allEntities.removeAll(toRemove);
		
		try
		{
			for (Long entityId : toReload)
			{
				
				allEntities.add(getDcEntityBean(session, entityId));
			} 
		}
		catch (Throwable e)
		{
			log.error("", e); // TODO GESTIRE
		}
		finally
		{
			getSession().flush();
		}
		return allEntities;
	}
	
	private HashSet<DcColumnBean> getColumns(Long entityId) throws Exception
	{
		HashSet<DcColumnBean> result = null;
		try
		{
			result = getColumns(getSession(), entityId);
		}
		finally
		{
			getSession().flush();
		}
		return result;		
	}
	public HashSet<DcColumnBean> getColumnsByDvClasse(Long dvClasseId) throws Exception
	{
		HashSet<DcColumnBean> result = null;
		try
		{
			Query q= getSession().createQuery("" +
					"select e from DcEntity e join e.dvUserEntities dvUe join dvUe.dvFormatClasses dvFc " +
					"where " +
					"  dvFc.dvClasse.id=:idClasse ");
			q.setParameter("idClasse",dvClasseId);
				
			result = getColumns(getSession(),(DcEntity) q.uniqueResult());
		}
		finally
		{
			getSession().flush();
		}
		return result;		
	}
	public HashSet<DcColumnBean> getColumnsByDcEntity(Long dcEntityId) throws Exception
	{
		HashSet<DcColumnBean> result = null;
		try
		{
			Query q= getSession().createQuery("" +
					"select e from DcEntity e " +
					"where " +
					"  e.id=:dcEntityId ");
			q.setParameter("dcEntityId",dcEntityId);
				
			result = getColumns(getSession(),(DcEntity) q.uniqueResult());
		}
		finally
		{
			getSession().flush();
		}
		return result;		
	}

	private HashSet<DcColumnBean> getColumns(Session session, Long entityId)
	{
		
		
		Query q = session.createQuery("from DC_ENTITY in class DcEntity where DC_ENTITY.id=:id");
		q.setLong("id", entityId);
		return getColumns(session, (DcEntity) q.uniqueResult());
	}

	@SuppressWarnings("unchecked")
	private HashSet<DcColumnBean> getColumns(Session session, DcEntity entity)
	{
		HashSet<DcColumnBean> result = new HashSet<DcColumnBean>();

		Set<? extends DcColumn> columns = (Set<? extends DcColumn>) entity.getDcColumns();

		for (DcColumn item : columns)
		{
			String xml = null;
			try
			{
				xml = new String(item.getDcExpression().getExpression());
				DcColumnBean colBean = new DcColumnBean();
				colBean.setId(item.getId());
				colBean.setXml(xml);
				colBean.setJavaType(item.getDcExpression().getColType());
				colBean.setAlias(item.getDcExpression().getAlias());
				colBean.setDescription(item.getLongDesc());
				colBean.setPrimaryKey(new DcColumnEvaluator(item).isPrimaryKey());
				result.add(colBean);								
			}
			catch (Throwable e)
			{
				log.error("", e); //TODO GESTIRE
			}
			finally {}
		}
	
		return result;		
	}

	public List getRelationBetweenEntities(Long id, ArrayList<Long> listaId  ) throws QueryBuilderException{
		
		try
		{
			return getRelationBetweenEntities(id,listaId, getSession());
		}
		catch (QueryBuilderException e) 
		{
			log.error(this.getClass().getName() + ".getUserEntity", e); 
			throw e;
		}
		catch (Exception e) 
		{
			log.error(this.getClass().getName() + ".getUserEntity", e); 
			throw new QueryBuilderException("common.general.errorMessage",e.getClass().getName() , e.getMessage());
		}
	}

	private List getRelationBetweenEntities(Long id, ArrayList<Long> listaId, Session session  ){
		List lista = null;
		// se non sono presenti altre entità(listaid vuota) con cui allora non eseguo la query
		if (listaId.size()==0){
			return lista=new ArrayList();
		}
		Query q= session.createQuery("" +
				"select " +
				"  r.dcRel, " +
				"  r.dcEntityByFkDcEntity1.id, " +
				"  r.dcEntityByFkDcEntity2.id,  " +
				"   r.dcRel.outerJoin " + 
				"from DcEntityRel r " +
				"where " +
				"  size(r.dcEntityByFkDcEntity1.dcSchemaEntities)>0 " +
				"  and size(r.dcEntityByFkDcEntity2.dcSchemaEntities)>0 " +
				"  and (r.dcEntityByFkDcEntity1.id=" + id + 
				"  and r.dcEntityByFkDcEntity2.id in (:listaId1))" +
				"  or (r.dcEntityByFkDcEntity2.id="+id +
				"  and r.dcEntityByFkDcEntity1.id in (:listaId2))");
		q.setParameterList("listaId1",listaId);
		q.setParameterList("listaId2",listaId);
		lista = q.list();
		return lista;
	}
	
	/** Recupera una DcEntity partendo dal suo id
	 * 
	 * @param id Chiave primaria della DcEntity
	 * @return
	 * @throws QueryBuilderException
	 */
	public DvUserEntity getDvUserEntity(Long id) throws QueryBuilderException{
	
		try
		{
			return getDvUserEntity(id, getSession());
		}
		catch (Throwable e)
		{
			log.error(this.getClass().getName() + ".getDvUserEntity", e); 
			throw new QueryBuilderException("common.general.errorMessage", null,e.getClass().getName() , e.getMessage());
		}
	
	}

	// TODO: l'EXPORT CHE SI è IMPIANTATO è TROPPO COMPLICATO,
	// SOSTITUIRLO CON UN METODO + SEMPLICE ABBOZZATO QUI SOTTO
	public String[] getExportEntity(Long id, Session session, SqlGenerator sqlGen) throws Exception {
		String export[]=null;

		LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
		
		DvUserEntity entity =  getDvUserEntity(id, session);

	
		list.put("DvUserEntity.Predicate", entity.getPredicate());
		list.put("DvUserEntity.Predicate", new String(entity.getSqlStatement(),UTF8_XML_ENCODING));
		list.put("DvUserEntity.Predicate", new String(entity.getSqlStatementBk(),UTF8_XML_ENCODING));
		
		
		DcEntity dce = entity.getDcEntity();
		
		Set dcColumns = dce.getDcColumns();
		ArrayList<DcColumn> columns = new ArrayList(dce.getDcColumns());

		for (DcColumn dcColumn : columns)
		{
			DcExpression dcExpression = dcColumn.getDcExpression();
			
		}
		return export;
		
		
	}
	
	private DvUserEntity getDvUserEntity(Long id, Session session){
		
		Query q = session.createQuery("from DvUserEntity e where e.dcEntity.id=:id");
		q.setLong("id", id);
		DvUserEntity entity= (DvUserEntity) q.uniqueResult();
		return  entity;
	}

	public UserEntityBean getUserEntity(Long id, SqlGenerator sqlGenerator)
	throws QueryBuilderException
	{
		try
		{
			 
			UserEntityBean ueb = getUserEntity(id, getSession(), sqlGenerator);
			

			
			return ueb;
		}
		catch (QueryBuilderException e) 
		{
			log.error(this.getClass().getName() + ".getUserEntity", e); 
			throw e;
		}
		catch (Exception e) 
		{
			log.error(this.getClass().getName() + ".getUserEntity", e); 
			throw new QueryBuilderException("common.general.errorMessage",null, e.getClass().getName() , e.getMessage());
		}
	}
	
	public UserEntityBean getUserEntityToExport(Long id, Session session, SqlGenerator sqlGenerator)
	throws Exception
	{
		return getUserEntity(id, session, sqlGenerator);
	}

	@SuppressWarnings("unchecked")
	private UserEntityBean getUserEntity(Long id, Session session, SqlGenerator sqlGenerator)
	throws Exception
	{
		UserEntityBean bean = new UserEntityBean();

		DvUserEntity dvUserEntity = null;
		Query q = session.createQuery("from DV_USER_ENTITY in class DvUserEntity where DV_USER_ENTITY.dcEntity=:id");
		q.setLong("id", id);
		dvUserEntity = (DvUserEntity) q.uniqueResult();
		if(dvUserEntity==null)
			throw new QueryBuilderException("common.general.errorMessage",null,Exception.class.getName(),"entità non trovata! Probabilmente è stata cancellata da altri!");
		
		bean = getUserEntityBean(dvUserEntity,session,sqlGenerator);
		
		return bean;
	}
	
	
	public UserEntityBean getUserEntityBean(DvUserEntity dvUserEntity, Session session , SqlGenerator sqlGenerator) 
	throws Exception
	{
		UserEntityBean bean = new UserEntityBean();
		DcEntity dcEntity = dvUserEntity.getDcEntity();
		// DATI GENERALI
		bean.setId(dcEntity.getId());
		bean.setName(dcEntity.getName());
		bean.setDescription(dcEntity.getLongDesc());
		bean.setPredicate(dvUserEntity.getPredicate());
		bean.setDtMod(dcEntity.getDtMod());
		
		//stabilisco se l'entità è referenziata da altre entità o DvFormatClasse
		boolean alreadyInUse = !(dvUserEntity.getDvFormatClasses().isEmpty() && dcEntity.getDcEntityRelsForFkDcEntity1().isEmpty() && dcEntity.getDcEntityRelsForFkDcEntity2().isEmpty());
		bean.setAlreadyInUse(alreadyInUse);
		// SE L'SQL E' SEGNATO COME ESPLICITO, VIENE RESTITUTITO UN 
		// BEAN VUOTO CON L'SQL ESPLICITO
		String xml = new String(dvUserEntity.getSqlStatement(), UTF8_XML_ENCODING);
		if (!isEmpty(xml))
		{
			SAXBuilder builder = new SAXBuilder();
			Element rootElement = builder.build(new StringReader(xml)).getRootElement();
			if (SqlStatementAttribute.EXPLICIT.name().equals(rootElement.getAttributeValue("attribute")))
			{
				bean.setExplicitSql(rootElement.getText());
				return bean;
			}
		}

		Map<Long,DcFromRel> relations = new HashMap<Long,DcFromRel>();
		Map<Long,DcEntityBean> relationsFrom1 = new HashMap<Long,DcEntityBean>();
		Map<Long,DcEntityBean> relationsFrom2 = new HashMap<Long,DcEntityBean>();
		// MI OCCUPO DELLA FROM E DELLE RELAZIONI
		for (Object item : dvUserEntity.getDvUeFromEntities())
		{
			DvUeFromEntity dvUeFromEntity = (DvUeFromEntity) item;
			DcEntity entity = dvUeFromEntity.getDcEntity();
			DcEntityBean entityBean =  getDcEntityBean(session, entity.getId()); //    getEntitiesMap(session, true).get(entity.getId());
			if(entityBean==null)
				throw new QueryBuilderException("entities.missingEntityInvolved.errorMessage",null);
			// Controllo degli Alias: se l'attuale entità è un alias allora construisco una nuova istanza di DcSchemaEntityBean/DvUserEntityBean
			if(!isEmpty(dvUeFromEntity.getAlias())){

				Matcher m = Pattern.compile(".*\\Q" + DcEntityBean.ALIAS_PREFIX + "\\E(\\d+)\\z").matcher(dvUeFromEntity.getAlias().trim());
				if (m.find())
				{
					try 
					{
						short aliasNum=Short.parseShort(m.group(1));
						entityBean=entityBean.clone();
						entityBean.setAliasNum(aliasNum);
					}
					catch (Throwable e) {
						log.error("Errore nel reperimento dell'aliasNum :"+e);
					}
				}

				
//				Matcher m = separeTableAndAlias.matcher(dvUeFromEntity.getAlias());
//				if (m.find())
//				{
//					if( !isEmpty( m.group(4))){
//						entityBean=entityBean.clone();
//						entityBean.setAliasNum(m.group(4));
//					}
//				}
			}
			
			for (Object item2 : dvUeFromEntity.getDcFromRelsForFkDvEuFrom1())
			{
				relations.put(((DcFromRel) item2).getId(), (DcFromRel) item2);
				relationsFrom1.put(((DcFromRel) item2).getId(), entityBean);
			}
			for (Object item2 : dvUeFromEntity.getDcFromRelsForFkDvEuFrom2())
				relationsFrom2.put(((DcFromRel) item2).getId(), entityBean);
			bean.addFromTable(entityBean);
		}
		
		// ORA MI OCCUPO DELLE COLONNE
		ArrayList colonne=new ArrayList(dcEntity.getDcColumns());
		Collections.sort(colonne, new Comparator(){
				public int compare(Object o1, Object o2)
				{
					DcColumn c1=(DcColumn)o1;
					DcColumn c2=(DcColumn)o2;
					
					if (c1 == null || c1.getId()==null )
						if (c2 == null || c2.getId()==null)
							return 0;
						else
							return -1;
					else if (c2 == null || c2.getId()==null){
						return 1;
					}
					return (c1.getId().compareTo(c2.getId()) );
				}
			} 
		);
		int i = 1;
		for (Object item : colonne)
		{
			DcColumn dcColumn = (DcColumn) item;
			DcColumnEvaluator evaluator = new DcColumnEvaluator(dcColumn);
			DcExpression dcExpression = dcColumn.getDcExpression();
			if (evaluator.isDataInizioVal())
				bean.setFromDate(i);
			if (evaluator.isDataFineVal())
				bean.setToDate(i);
			if (evaluator.isExternalKey())
				bean.setExternalKey(i);
			ValueExpression expression = ValueExpression.createFromXml(new String(dcExpression.getExpression(), UTF8_XML_ENCODING), sqlGenerator);
			bean.addColumn(
					expression, 
					dcColumn.getId(), 
					dcColumn.getLongDesc(), 
					dcColumn.getLongDesc(), 
					evaluator.isPrimaryKey(),
					!dcColumn.getDvUeGroups().isEmpty(),
					!dcExpression.getDvUeOrders().isEmpty(),
					!alreadyInUse
					).getColumn().save();
			i++;
		}
		
		// SCRIVO LE RELAZIONI LETTE NELLA
		// SEZIONE DELLA COSTRUZIONE DELLA FROM
		for (DcFromRel item : relations.values())
		{
			Condition condition = Condition.createFromXml(
					new String(item.getDcRel().getCondition(), UTF8_XML_ENCODING), 
					sqlGenerator);
			bean.addJoinCondition(
					item.getDcRel().getId(),
					condition,
					relationsFrom1.get(item.getId()),
					relationsFrom2.get(item.getId()),
					new Long(1L).equals(item.getDcRel().getOuterJoin()),
					true);
		}
		
		// E LA CONDIZIONE DI FILTRO
		if (dvUserEntity.getDcRel() != null)
			bean.setCondition(Condition.createFromXml(
					new String(dvUserEntity.getDcRel().getCondition(), UTF8_XML_ENCODING), 
					sqlGenerator));
		
		return bean;
		
	}
	
	/**
	 * Scrive una nuova entit&agrave; utente nel DB.
	 * @param bean
	 * L'entit&agrave; utente da salvare.
	 * @param cF
	 * @param oF
	 * @param eF
	 * @throws QueryBuilderException
	 * @throws Exception
	 */
	public void writeUserEntity(UserEntityBean bean, SqlGenerator sqlGenerator)
	throws QueryBuilderException
	{
		try
		{
			writeUserEntity(bean, getSession(), sqlGenerator);
		}
		catch (QueryBuilderException e) 
		{
			log.error(this.getClass().getName() + ".writeUserEntity", e); 
			throw e;
		}
		catch (Exception e) 
		{
			log.error(this.getClass().getName() + ".writeUserEntity", e); 
			throw new QueryBuilderException("common.general.errorMessage",null,e.getClass().getName(),e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void writeUserEntity(UserEntityBean bean, Session theSession, SqlGenerator sqlGenerator)
	throws Exception
	{
		@SuppressWarnings("unused") ConditionFactory cF = sqlGenerator.getConditionFactory();
		OperatorFactory oF = sqlGenerator.getOperatorFactory();
		@SuppressWarnings("unused") ExpressionFactory eF = sqlGenerator.getExpressionFactory();
		Transaction trans = theSession.beginTransaction();
		Session theRightsSession = getSession(RIGHTS_SESSION_FACTORY_CONF_FILE);
		Transaction tranRights = theRightsSession.beginTransaction(); 
		try
		{
			//Recupero DataeOra e Utente Inserimento che sono gli stessi di Modifica 
			//infatti stiamo inserendo
			checkOptimistiLock(bean,true,theSession);
			
			if (!isEmpty(bean.getExplicitSql()))
			{
				DcEntity dcEntity = new DcEntity();
				dcEntity.setName(bean.getName());
				dcEntity.setLongDesc(bean.getDescription());
				dcEntity.setDtIns(bean.getDtMod());
				dcEntity.setDtMod(bean.getDtMod());
				dcEntity.setUserIns(bean.getUserMod());
				dcEntity.setUserMod(bean.getUserMod());
				synchronized(this) {theSession.save(dcEntity);}
				DvUserEntity dvUserEntity = new DvUserEntity();
				dvUserEntity.setDcEntity(dcEntity);
				String xml = "<sql_statement attribute='" + SqlStatementAttribute.EXPLICIT.name() + "'><![CDATA[";
				xml += bean.getExplicitSql();
				xml += "]]></sql_statement>";
				dvUserEntity.setSqlStatement(xml.getBytes(UTF8_XML_ENCODING));
				//dvUserEntity.setSqlStatementBk("<sql_statement></sql_statement>".getBytes(UTF8_XML_ENCODING));
				dvUserEntity.setSqlStatementBk(xml.getBytes(UTF8_XML_ENCODING));
				synchronized(this) {theSession.save(dvUserEntity);}

				// creazione dai dati relativi ai diritti per l'utente connesso UserPrincipal
				/* TODO : RIFWARE GESTIONE PERMESSI 
				Principal principal = getContext().getExternalContext().getUserPrincipal();
				String context =getContext().getExternalContext().getRequestContextPath().substring(1);
				String ente=GestionePermessi.getEnteFromContext(principal,context);
				RightsManager rm = new RightsManager();
				ByteArrayInputStream fis = (ByteArrayInputStream)FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(APPLICATION_XML_RELATIVE_PATH);
				AmApplication application= (AmApplication) rm.decode(fis,bean.getName(),ente,principal.getName());
				synchronized(this) {rm.saveOrUpdateAmApplication(application,theRightsSession,principal.getName());}
				*/
				
				theSession.connection().commit();
				trans.commit();
				tranRights.commit();
				theRightsSession.close();
				return;
			}
			
			// CREO LA DC_ENTITY
			DcEntity dcEntity = new DcEntity();
			dcEntity.setName(bean.getName());
			dcEntity.setLongDesc(bean.getDescription());
			dcEntity.setDtIns(bean.getDtMod());
			dcEntity.setDtMod(bean.getDtMod());
			dcEntity.setUserIns(bean.getUserMod());
			dcEntity.setUserMod(bean.getUserMod());
			
			// CREO LA DV_USER_ENTITY E LA METTO IN RELAZIONE CON DC_ENTITY
			DvUserEntity dvUserEntity = new DvUserEntity();
			dvUserEntity.setDcEntity(dcEntity);
			dcEntity.getDvUeFromEntities().add(dvUserEntity);
			dvUserEntity.setSqlStatement("<void />".getBytes(UTF8_XML_ENCODING));
			//dvUserEntity.setSqlStatementBk("<sql_statement></sql_statement>".getBytes(UTF8_XML_ENCODING));
			dvUserEntity.setSqlStatementBk("<void />".getBytes(UTF8_XML_ENCODING));
			dvUserEntity.setPredicate(bean.getPredicate());
			
			// LEGGO TUTTE LE ENTITA IN FROM E CREO TUTTE LE DV_UE_FROM_ENTITY
			IdentityHashMap<DcEntityBean,DvUeFromEntity> fromMap = new IdentityHashMap<DcEntityBean,DvUeFromEntity>();
			for (DcEntityBean item : bean.getFromList())
			{
				DvUeFromEntity dvUeFromEntity = new DvUeFromEntity();
				dvUeFromEntity.setDvUserEntity(dvUserEntity);
				if (item.getAlias() != null && !"".equals(item.getAlias().trim())) {
					String alias = item.getAlias();
					int dot = alias.lastIndexOf('.');  
					String aliasOK = (dot == -1) ? alias : alias.substring(dot+1);
					dvUeFromEntity.setAlias(aliasOK);
				}
				else if (item instanceof DvUserEntityBean) {
					dvUeFromEntity.setAlias(item.getSqlName());
				}
				Query q = theSession.createQuery("from DC_ENTITY in class DcEntity where DC_ENTITY.id = :id");
				q.setParameter("id", item.getId());
				dvUeFromEntity.setDcEntity((DcEntity) q.uniqueResult());
				dvUserEntity.getDvUeFromEntities().add(dvUeFromEntity);
				fromMap.put(item,dvUeFromEntity);
			}
			
			// CREO TUTTI I CAMPI
			//Set<DcColumn> selectList = new HashSet<DcColumn>();
			//sostituito, per mantenere l'ordinamento originario, da:
			ArrayList<DcColumn> selectList = new ArrayList<DcColumn>();
			int progressivo=0;
			for (UserEntityColumnBean item : bean.getSelectList())
			{
				DcColumn dcColumn = new DcColumn();
				DcExpression dcExpression = new DcExpression();
				dcExpression.setAlias(item.getAlias());
				ValueExpression exp = item.getColumn().getValue().clone();
				for (UserEntityColumnBeanAppendedValue item2 : item.getAppendedExpressions())
					exp.appendExpression(oF.getValueExpressionOperator(item2.getChainOperator()), item2.getValue().getValue());
				dcExpression.setColType(ValueType.mapValueType2JavaType(exp.getValueType()));
				dcExpression.setExpression(ValueExpression.getOperandXmlString(exp).getBytes(UTF8_XML_ENCODING));
				dcColumn.setLongDesc(item.getName());
				
				if (item.isPrimaryKey())
				{
					DcAttribute dcAttribute = new DcAttribute();
					dcAttribute.setDcColumn(dcColumn);
					dcAttribute.setAttributeSpec(DcAttributes.KEY.name());
					String progr=StringUtils.leftPad(Integer.toString(progressivo),5,'0');
					dcAttribute.setAttributeVal(progr);
					dcColumn.getDcAttributes().add(dcAttribute);
					progressivo++;
				}
				if (item.isExternalKey())
				{
					DcAttribute dcAttribute = new DcAttribute();
					dcAttribute.setDcColumn(dcColumn);
					dcAttribute.setAttributeSpec(DcAttributes.EXTERNAL_KEY.name());
					dcColumn.getDcAttributes().add(dcAttribute);
				}
				if (item.isFromDate())
				{
					DcAttribute dcAttribute = new DcAttribute();
					dcAttribute.setDcColumn(dcColumn);
					dcAttribute.setAttributeSpec(DcAttributes.DT_INIZIO_VAL.name());
					dcColumn.getDcAttributes().add(dcAttribute);					
				}
				if (item.isToDate())
				{
					DcAttribute dcAttribute = new DcAttribute();
					dcAttribute.setDcColumn(dcColumn);
					dcAttribute.setAttributeSpec(DcAttributes.DT_FINE_VAL.name());
					dcColumn.getDcAttributes().add(dcAttribute);					
				}

				if (item.isGroupBy())
				{
					DvUeGroup dvUeGroup = new DvUeGroup();
					dvUeGroup.setDvUserEntity(dvUserEntity);
					dvUeGroup.setDcColumn(dcColumn);
					dvUserEntity.getDvUeGroups().add(dvUeGroup);
					dcColumn.getDvUeGroups().add(dvUeGroup);
				}
				
				if (item.isOrderBy())
				{
					DvUeOrder dvUeOrder = new DvUeOrder();
					dvUeOrder.setDvUserEntity(dvUserEntity);
					dvUeOrder.setDcExpression(dcExpression);
					dvUserEntity.getDvUeOrders().add(dvUeOrder);
					dcExpression.getDvUeOrders().add(dvUeOrder);
				}
				

				dcColumn.setDcExpression(dcExpression);
				dcExpression.getDcColumns().add(dcColumn);
				dcColumn.setDcEntity(dcEntity);
				dcEntity.getDcColumns().add(dcColumn);
				selectList.add(dcColumn);
			}
			
			// ORA MI OCCUPO DELLE CONDIZIONI DI FILTRO
			Condition condition = null;
			DcRel dcRelCondition = null;
			if (bean.getCondition() != null)
				if (condition == null)
					condition = bean.getCondition();
			if (condition != null)
			{
				dcRelCondition = new DcRel();
				dvUserEntity.setDcRel(dcRelCondition);
				dcRelCondition.getDvUserEntities().add(dvUserEntity);
				dcRelCondition.setCondition(condition.getStringXml().getBytes(UTF8_XML_ENCODING));				
			}
			// ORA MI OCCUPO DELLE RELAZIONI
			List<DcFromRel> joins = new ArrayList<DcFromRel>();
			for (JoinSpec item : bean.getJoins())
			{
				DvUeFromEntity former = fromMap.get(item.getFormerTable());
				DvUeFromEntity latter = fromMap.get(item.getLatterTable());
				DcFromRel dcFromRel = new DcFromRel();
				dcFromRel.setDvUeFromEntityByFkDvEuFrom1(former);
				dcFromRel.setDvUeFromEntityByFkDvEuFrom2(latter);
				former.getDcFromRelsForFkDvEuFrom1().add(dcFromRel);
				latter.getDcFromRelsForFkDvEuFrom2().add(dcFromRel);
				DcRel dcRelJoin = new DcRel();
				dcFromRel.setDcRel(dcRelJoin);
				dcRelJoin.getDcFromRels().add(dcFromRel);
				dcRelJoin.setCondition(item.getJoinCondition().getStringXml().getBytes(UTF8_XML_ENCODING));
				dcRelJoin.setOuterJoin(item.isOuterJoin() ? 1L : 0L);
				joins.add(dcFromRel);
			}				
			
			
			// ORA PROCEDO AI SALVATAGGI
			// PERCHE' SIANO VALORIZZATE
			// LE CHIAVI DI FOREIGN KEY,
			// OCCORRE PRIMA SALVARE 
			// LE TABELLE CON LE CHIAVI
			// PRIMARIE, E POI LE TABELLE
			// FON LE FOREIGN KEY CHE PUNTANO
			// A QUESTE CHIAVI PRIMARIE
			// SINTESI: SALVARE PRIMA IL LATO "1"
			// E POI IL LATO "molti" DELLA RELAZIONE
			
			//flush per prevenire errori dovuti ad eventuale, immediatamente precedente, inserimento di entità da mappatura dati
			//theSession.flush();
			
			synchronized(this) {theSession.save(dcEntity);}
			if (dcRelCondition != null)
				synchronized(this) {theSession.save(dcRelCondition);}
			synchronized(this) {theSession.save(dvUserEntity);}
			for (DvUeFromEntity item : fromMap.values())
				synchronized(this) {theSession.save(item);}
			for (DcFromRel item : joins)
			{
				synchronized(this) {theSession.save(item.getDcRel());}
				synchronized(this) {theSession.save(item);}
			}
			for (DcColumn item : selectList)
			{
				synchronized(this) {theSession.save(item.getDcExpression());}
				synchronized(this) {theSession.save(item);}
				for (Object dcAttribute : item.getDcAttributes())
					synchronized(this) {theSession.save(dcAttribute);}
				for (Object dvUeOrder : item.getDcExpression().getDvUeOrders())
					synchronized(this) {theSession.save(dvUeOrder);}
				for (Object dvUeGroup : item.getDvUeGroups())
					synchronized(this) {theSession.save(dvUeGroup);}
			}
			
			// creazione dai dati relativi ai diritti per l'utente connesso UserPrincipal
			/* TODO RIFARE GESTIONE PERMESSI
			Principal principal = getContext().getExternalContext().getUserPrincipal();
			String context =getContext().getExternalContext().getRequestContextPath().substring(1);
			String ente=GestionePermessi.getEnteFromContext(principal,context);
			RightsManager rm = new RightsManager();
			ByteArrayInputStream fis = (ByteArrayInputStream)FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(APPLICATION_XML_RELATIVE_PATH);
			AmApplication application= (AmApplication) rm.decode(fis,bean.getName(),ente,principal.getName());
			synchronized(this) {rm.saveOrUpdateAmApplication(application,theRightsSession,principal.getName());}
			*/
			
			// ORA SCRIVO ANCHE LA QUERY LETTERALE
			// NELLA COLONNA SQL_STATEMENT
			String xml = "<sql_statement attribute='" + SqlStatementAttribute.GENERATED.name() + "'><![CDATA[";
			xml += Translator.getQueryFromDvUserEntity(dvUserEntity, sqlGenerator);
			xml += "]]></sql_statement>";
			dvUserEntity.setSqlStatement(xml.getBytes(UTF8_XML_ENCODING));
			//dvUserEntity.setSqlStatementBk("<sql_statement></sql_statement>".getBytes(UTF8_XML_ENCODING));
			dvUserEntity.setSqlStatementBk(xml.getBytes(UTF8_XML_ENCODING));
			synchronized(this) {theSession.save(dvUserEntity);}
			trans.commit();
			tranRights.commit();
			theSession.close();
			theRightsSession.close();
		}
		catch (Exception e)
		{
			tranRights.rollback();
			trans.rollback();
			theSession.close();
			theRightsSession.close();
			RightsManager.revokeEntityForSession(bean.getName());
			throw e;
		}
	}	

	private FacesContext getContext()
	{
		return FacesContext.getCurrentInstance();
	}

	/** Metodo per effettuare la verifica del lock ottimistico sullo UserEntityBean 
	 * inoltre valorizza anche il bean con i dati Utente 
	 * @param bean controllato
	 * @param theSession sessione nella quale avviene il controllo
	 * @return true se qualcuno ha modificato l'entità che controllo
	 * @throws Exception 
	 */
	private boolean checkOptimistiLock(UserEntityBean bean, boolean isInsert, Session theSession) throws Exception
	{
		boolean isChanged=true;
        //Se non sono in inserimento allora verifico se l'entita non sia stata eliminata 
        if (!isInsert)
		{
			SQLQuery countEliQuery = theSession.createSQLQuery("select count(*) from DC_ENTITY where id=:id");
			countEliQuery.setParameter("id",bean.getId());
			List retEli = countEliQuery.list();
			Object rigaEli = (Object) retEli.get(0);
			if("0".equals(rigaEli.toString())){
	        	isChanged=false;
				return isChanged;
	        }
		}

		String sql= "select count(*), TO_CHAR(systimestamp,'DD/MM/YYYY HH24:MI:SS:FF3') from DC_ENTITY where id=:id";
		if (bean.getDtMod()!=null) sql+=" and dt_mod=:data";
		SQLQuery countQuery= theSession.createSQLQuery(sql);
        if (isInsert)
			countQuery.setParameter("id", -1L);
		else 
			countQuery.setParameter("id", bean.getId());
        if(bean.getDtMod()!=null) countQuery.setParameter("data",bean.getDtMod());
        
        List ret= countQuery.list();
        Object[] riga = (Object[]) ret.get(0);
        
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
        Date dtModifica=df.parse((String)riga[1]);
        Principal prin=FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        String user = prin!=null?prin.getName():null;
    	//se nessuno ha cambiato l'entità oppure sono in inserimento aggiorno le informazioni 
        // imposto isChanged a true

        if("1".equals(riga[0].toString()) || isInsert){
        	bean.setDtMod(dtModifica);
        	bean.setUserMod(user);
        	isChanged=false;
        }
		return isChanged;
	}


	/** Metodo per effettuare la verifica del lock ottimistico sulla DcEntityRel
	 * inoltre valorizza anche il bean con i dati Utente 
	 * @param bean controllato
	 * @param session sessione nella quale avviene il controllo
	 * @return true se qualcuno ha modificato l'entità che controllo
	 * @throws Exception 
	 */
	private boolean checkOptimistiLock(DcEntityRel entityRel, boolean isInsert, Session session) throws Exception
	{
		boolean isChanged=true;
        //Se non sono in inserimento allora verifico se l'entita non sia stata eliminata 
        if (!isInsert)
		{
			SQLQuery countEliQuery = session.createSQLQuery("select count(*) from DC_ENTITY_REL where id=:id");
			countEliQuery.setParameter("id",entityRel.getId());
			List retEli = countEliQuery.list();
			Object rigaEli = (Object) retEli.get(0);
			if("0".equals(rigaEli.toString())){
	        	isChanged=false;
				return isChanged;
	        }
		}

		String sql= "select count(*), TO_CHAR(systimestamp,'DD/MM/YYYY HH24:MI:SS:FF3') from DC_ENTITY_REL where id=:id";
		if (entityRel.getDtMod()!=null) sql+=" and dt_mod=:data";
		SQLQuery countQuery= session.createSQLQuery(sql);

		if (isInsert)
			countQuery.setParameter("id", -1L);
		else 
			countQuery.setParameter("id",entityRel.getId());
		
        if(entityRel.getDtMod()!=null) countQuery.setParameter("data",entityRel.getDtMod());
        
        List ret= countQuery.list();
        Object[] riga = (Object[]) ret.get(0);
        
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
        Date dtModifica=df.parse((String)riga[1]);
        Principal prin=FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        String user = prin!=null?prin.getName():null;
    	//se nessuno ha cambiato l'entità oppure sono in inserimento aggiorno le informazioni 
        // imposto isChanged a true

        if("1".equals(riga[0].toString()) || isInsert){
        	entityRel.setDtMod(dtModifica);
        	entityRel.setUserMod(user);
        	isChanged=false;
        }
		return isChanged;
	}

	public void saveOrUpdateRelationInDb(GenericCondition cond)
	throws QueryBuilderException
	{
		try
		{
			saveOrUpdateRelationInDb(cond, getSession());
		}
		catch (QueryBuilderException e) 
		{
			log.error(this.getClass().getName() + ".saveOrUpdateRelationInDb", e); 
			throw e;
		}
		catch (Exception e) 
		{
			log.error(this.getClass().getName() + ".saveOrUpdateRelationInDb", e); 
			throw new QueryBuilderException("common.general.errorMessage",null,e.getClass().getName(),e.getMessage());
		}
	}
	
	private void saveOrUpdateRelationInDb(GenericCondition cond, Session session)
	throws Exception
	{
		Transaction trans = session.beginTransaction();
		Query q = null;
		DcEntityBean formerEntity=cond.getEntitiesInvolved().get(0);
		DcEntityBean latterEntity=cond.getEntitiesInvolved().get(1);
		DcRel dcRel=null;
		try
		{
			if(cond.getId()==null)
			{
				q = session.createQuery("from DC_ENTITY in class DcEntity where DC_ENTITY.id = :id");
				q.setParameter("id",formerEntity.getId());
				DcEntity former=(DcEntity)q.uniqueResult();
				q = session.createQuery("from DC_ENTITY in class DcEntity where DC_ENTITY.id = :id");
				q.setParameter("id",latterEntity.getId());
				DcEntity latter=(DcEntity)q.uniqueResult();
				dcRel=new DcRel();
				DcEntityRel dcEntityRel=new DcEntityRel();
				dcEntityRel.setDcEntityByFkDcEntity1(former);
				dcEntityRel.setDcEntityByFkDcEntity2(latter);
				dcRel.setCondition(new String("<void/>").getBytes(UTF8_XML_ENCODING));
				dcEntityRel.setDcRel(dcRel);
				checkOptimistiLock(dcEntityRel,true,session);
				synchronized(this) {session.save(dcRel);}
				synchronized(this) {session.save(dcEntityRel);}
				session.flush();
			}
			else
			{
				//Gestione del lock Ottimistico
				
				q=session.createQuery("from DC_REL in class DcRel where DC_REL.id = :id");
				q.setParameter("id",cond.getId());
				dcRel=(DcRel)q.uniqueResult();
				DcEntityRel dcEntityRel =(DcEntityRel)getUniqueElement(dcRel.getDcEntityRels());
				//per essere certo che la comparazione venga fatta sulla data precedente la ricarico sul bean
				dcEntityRel.setDtMod(cond.getDtMod());
				//Recupero DataeOra e Utente Ultima Modifica e verifico il lock Ottimistico
				if (checkOptimistiLock(dcEntityRel,false,session)){
					throw new QueryBuilderException("common.concurrency.errorMessage",null);
				}
				//Aggiorno i dati di lock
				synchronized(this) {session.save(dcEntityRel);}
			}
			
			dcRel.setCondition(cond.getCondition().getStringXml().getBytes(UTF8_XML_ENCODING));
			dcRel.setName(cond.getName());
			dcRel.setOuterJoin(cond.getOuterJoin().contains(latterEntity)?1L:0L);
			
			synchronized(this) {session.save(dcRel);}
			cond.setId(dcRel.getId());
			trans.commit();
			session.close();
		}
		catch (Exception e)
		{
			trans.rollback();
			session.close();
			throw e;
		}


	}

	public void deleteRelationInDb(GenericCondition cond)
	throws QueryBuilderException
	{
		try
		{
			deleteRelationInDb(cond, getSession());
		}
		catch (Throwable e) 
		{
			log.error(this.getClass().getName() + ".deleteRelationInDb", e); 
			throw new QueryBuilderException("common.general.errorMessage",null,e.getClass().getName(),e.getMessage());
		}
	}
	
	private void deleteRelationInDb(GenericCondition cond, Session session)
	throws Exception
	{
		Transaction trans = session.beginTransaction();
		Query q = null;
		DcRel dcRel=null;
		try
		{
			q = session.createQuery("from DC_REL in class DcRel where DC_REL.id = :id");
			q.setParameter("id",cond.getId());
			dcRel=(DcRel)q.uniqueResult();
			for (Object item : dcRel.getDcEntityRels())
				session.delete(item);
			session.delete(dcRel);
			
			trans.commit();
			session.close();
		}
		catch (Exception e)
		{
			trans.rollback();
			session.close();
			throw e;
		}


	}

	public Map<ASetAsAKey,Object[]> retrieveRelations(SqlGenerator sqlGen)throws QueryBuilderException

	{
		Map<ASetAsAKey,Object[]> relationsMap=new HashMap<ASetAsAKey,Object[]>();
		try
		{
			relationsMap=retrieveRelations(sqlGen, getSession());
		}
		catch (Exception e) 
		{
			log.error(this.getClass().getName() + ".retrieveRelations", e); 
			throw new QueryBuilderException("common.general.errorMessage",null,e.getClass().getName(),e.getMessage());
		}
		return relationsMap;
	}
	
	@SuppressWarnings("unchecked")
	private Map<ASetAsAKey,Object[]> retrieveRelations(SqlGenerator sqlGen, Session session) throws Exception

	{
		Query q = null;
		Map<ASetAsAKey,Object[]> relationsMap=new HashMap<ASetAsAKey,Object[]>();
		try
		{
			q=session.createQuery("from DC_REL in class DcRel");
			for(Object item: q.list())
			{
				DcRel dcRel=(DcRel)item;
				if (dcRel.getDcEntityRels().isEmpty())
					continue;
				DcEntityRel dcEntityRel = (DcEntityRel) getUniqueElement(dcRel.getDcEntityRels());
				DcEntity former= dcEntityRel.getDcEntityByFkDcEntity1();
				DcEntity latter = dcEntityRel.getDcEntityByFkDcEntity2();
				Date dtMod=dcEntityRel.getDtMod();
				Long position=dcRel.getId();
				DcEntityBean formerBean = null;
				DcEntityBean latterBean = null;
				if (isEmpty(former.getDcSchemaEntities()))
				{
					formerBean = new DvUserEntityBean(former.getId(), former.getName(), former.getLongDesc(),former.getDtMod());
				}
				else
				{
					DcSchemaEntity dcSchemaEntity=(DcSchemaEntity)getUniqueElement(former.getDcSchemaEntities());
					formerBean = new DcSchemaEntityBean(former.getId(), dcSchemaEntity.getSqlName(), former.getName(), former.getLongDesc(),former.getDtMod());
				}
				formerBean.setAlias("E_" + former.getId());
				
				if (isEmpty(latter.getDcSchemaEntities()))
				{
					latterBean = new DvUserEntityBean(latter.getId(), latter.getName(), latter.getLongDesc(),latter.getDtMod());
				}
				else
				{
					DcSchemaEntity dcSchemaEntity=(DcSchemaEntity)getUniqueElement(latter.getDcSchemaEntities());
					latterBean = new DcSchemaEntityBean(latter.getId(), dcSchemaEntity.getSqlName(), latter.getName(), latter.getLongDesc(),latter.getDtMod());
				}
				latterBean.setAlias("E_" + latter.getId());
				Condition condition = Condition.createFromXml(new String(dcRel.getCondition(),UTF8_XML_ENCODING),sqlGen);
				Long outer=dcRel.getOuterJoin();
				ASetAsAKey key=null;
				if(formerBean instanceof DcSchemaEntityBean )
					key=new ASetAsAKey(TABLE +formerBean.getId()+ALIAS +formerBean.getAliasNum(),TABLE +latterBean.getId()+ALIAS +latterBean.getAliasNum());
				else if(formerBean instanceof DvUserEntityBean )
					key=new ASetAsAKey(new OrderedEntities(TABLE +formerBean.getId()+ALIAS + position,1),new OrderedEntities(TABLE +latterBean.getId()+ALIAS + position,2));					
				relationsMap.put(key,new Object[] {dcRel.getId(), formerBean, latterBean, condition, outer, dtMod});
			}
			
		}
		catch(Exception e)
		{			
			throw e;
		}
		session.flush();
		session.close();
		return relationsMap;
	}
	
	/**
	 * Salva le modifiche ad una entit&agrave; utente nel DB.
	 * @param bean
	 * L'entit&agrave; utente da modificare.
	 * @param cF
	 * @param oF
	 * @param eF
	 * @throws QueryBuilderException
	 * @throws Exception
	 */
	public void updateUserEntity(UserEntityBean bean, SqlGenerator sqlGenerator)
	throws QueryBuilderException
	{
		try
		{
			updateUserEntity(bean, getSession(), sqlGenerator);
		}
		catch (QueryBuilderException qe) 
		{
			log.error(this.getClass().getName() + ".updateUserEntity", qe); 
			throw qe;
		}
		catch (Exception e) 
		{
			log.error(this.getClass().getName() + ".updateUserEntity", e); 
			throw new QueryBuilderException("common.general.errorMessage",null,e.getClass().getName(),e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void updateUserEntity(UserEntityBean bean, Session session, SqlGenerator sqlGenerator)
	throws Exception
	{
		ConditionFactory cF = sqlGenerator.getConditionFactory();
		OperatorFactory oF = sqlGenerator.getOperatorFactory();
		ExpressionFactory eF = sqlGenerator.getExpressionFactory();

		Transaction trans = session.beginTransaction();
		session.setFlushMode(FlushMode.AUTO); 
		DvUserEntity dvUserEntity = null;
		try
		{
			//Recupero DataeOra e Utente Ultima Modifica 
			if (checkOptimistiLock(bean,false, session))
				throw new QueryBuilderException("common.concurrency.errorMessage",null);
		
			Query q = session.createQuery("from DV_USER_ENTITY in class DvUserEntity where DV_USER_ENTITY.dcEntity=:id");
			q.setLong("id", bean.getId());
			dvUserEntity = (DvUserEntity) q.uniqueResult();
			dvUserEntity.setPredicate(bean.getPredicate());
			
			// LEGGO LA DC_ENTITY
			DcEntity dcEntity = dvUserEntity.getDcEntity();
			
			//verifico se è cambiato il name: in questo caso dovrà modificare i permessi
			boolean changedName = !dcEntity.getName().equals(bean.getName());
			String oldName = dcEntity.getName();
			
			dcEntity.setName(bean.getName());
			dcEntity.setLongDesc(bean.getDescription());
			dcEntity.setDtMod(bean.getDtMod());
			dcEntity.setUserMod(bean.getUserMod());

			
			// LEGGO TUTTE LE ENTITA' IN FROM ////////////////////////////////////////////////////////////////
			// MODIFICANDO QUELLE
			// ESISTENTI, AGGIUNGENDO LE NUOVE ED ELIMINANDO QUELLE RIMOSSE.
			HashMap<DcEntityBean,DvUeFromEntity> fromMap = new HashMap<DcEntityBean,DvUeFromEntity>();
			ArrayList<DvUeFromEntity> toRemoveFromList = new ArrayList<DvUeFromEntity>();
			// *** BUG FIX *** //
			// L'ITERATOR RESTITUITO DAL PersistenceSet di Hibernate
			// HA MOSTRATO IL SEGUENTE PROBLEMA:
			// AL SECONDO CICLO DEL for SOTTOSTANTE,
			// IL METODO iterator HA RESTITUITO UN ITERATOR
			// IN UNO STATO INCONSISTENTE, IN QUANTO RESTITUIVA 
			// true CHIAMANDO hasNext(), MA POI DAVA null
			// COME VALORE DI next(). PER QUESTO HO WRAPPATO
			// TUTTO IL SET CON L'ArrayList QUI SOTTO
			ArrayList<? extends DvUeFromEntity> alredyExistingFroms = new ArrayList<DvUeFromEntity>(dvUserEntity.getDvUeFromEntities());
			
			//controllo quali sono le entità (eventualmente) da cancellare
			Iterator<? extends DvUeFromEntity> iterToRemove = alredyExistingFroms.iterator();
			while (iterToRemove.hasNext())
			{
				DvUeFromEntity dvUeFromEntity = iterToRemove.next();
				boolean trovata = false;
				for (DcEntityBean item : bean.getFromList()) {
					if(dvUeFromEntity.getDcEntity().getId().equals(item.getId())){
						trovata = true;
						break;
					}
				}
				if (!trovata) {
					toRemoveFromList.add(dvUeFromEntity);
				}
			}
			
			for (DcEntityBean item : bean.getFromList())
			{
				DvUeFromEntity dvUeFromEntity = null;
				boolean alredyExisting = false;
				Iterator<? extends DvUeFromEntity> iter = alredyExistingFroms.iterator();
				while (iter.hasNext() && !alredyExisting)
				{
					dvUeFromEntity = iter.next(); 
					// controllo se l'entità in corrente è da aggiungere nella from list o era già presente
					if(dvUeFromEntity.getDcEntity().getId().equals(item.getId())){
						
						alredyExisting =  ( (dvUeFromEntity.getAlias() == null && item.getAlias() == null)|| (0==item.getAliasNum())) || (dvUeFromEntity.getAlias() != null && dvUeFromEntity.getAlias().equals(item.getAlias()));
						
					}
				}
				if (!alredyExisting)
				{
					dvUeFromEntity = new DvUeFromEntity();
					if (!isEmpty(item.getAlias()))
						dvUeFromEntity.setAlias(item.getAlias());
					dvUeFromEntity.setDvUserEntity(dvUserEntity);
					dvUserEntity.getDvUeFromEntities().add(dvUeFromEntity);			
					q = session.createQuery("from DC_ENTITY in class DcEntity where DC_ENTITY.id = :id");
					q.setParameter("id", item.getId());
					dvUeFromEntity.setDcEntity((DcEntity) q.uniqueResult());				
				}
				fromMap.put(item, dvUeFromEntity);
			}
			
			// ORA MI OCCUPO DI TUTTE LE COLONNE IN SELECT ////////////////////////////////////////////////////////////////////
			// ELIMINO LE COLONNE DA ELIMINARE			
			for (Object deleteable : dcEntity.getDcColumns())
				if (bean.getDeletedColumns().contains(((DcColumn) deleteable).getId()))
				{
					DcColumn deleteableColumn = (DcColumn) deleteable;
					for (Object item2 : deleteableColumn.getDvUeGroups())
						session.delete(item2);
					for (Object item2 : deleteableColumn.getDcAttributes())
						session.delete(item2);
					for (Object item2 : deleteableColumn.getDcExpression().getDvUeOrders())
						session.delete(item2);
					for (Object item2 : deleteableColumn.getDcTipoeColumns())
						session.delete(item2);
					session.delete(deleteableColumn);
					session.delete(deleteableColumn.getDcExpression());
				}
			//Set<DcColumn> selectList = new HashSet<DcColumn>();
			//sostituito, per mantenere l'ordinamento originario, da:
			ArrayList<DcColumn> selectList = new ArrayList<DcColumn>();
			//recuper l'ultimo progressivo KEY per le column della mia Entita
			String max="select max(dcAt.attributeVal) from DcEntity e join e.dcColumns dcCo join dcCo.dcAttributes dcAt " +
						"where  dcAt.attributeSpec='KEY' and e.id="+dcEntity.getId();
			List lista=executeHqlStatement(max,1);
			int progressivo= lista.size()==0?(Integer.parseInt(lista.get(0).toString())):0;
			for (UserEntityColumnBean item : bean.getSelectList())
			{
				DcColumn dcColumn = null;
				DcExpression dcExpression = null;

				
				
//				// SE NON ERA EDITABILE, SIGNIFICA CHE E' UNA COLONNA
//				// NON MODIFICATA
//				if (!item.isEditable())
//					continue;
				
				
				
				// ALTRIMENTI, BISOGNA VEDERE SE E' UNA COLONNA
				// CON UN ID, OVVERO GIA' PRESENTE NEL DB, O
				// UNA COLONNA NUOVA
				
				// COLONNA GIA' PRESENTE
				if (item.getId() != null)
				{
					q = session.createQuery("from DC_COLUMN in class DcColumn where DC_COLUMN.id = :id");
					q.setParameter("id", item.getId());
					dcColumn = (DcColumn) q.uniqueResult();
					dcExpression = dcColumn.getDcExpression();
					
					DcColumnEvaluator evaluator = new DcColumnEvaluator(dcColumn);

					
					//Questi aggiornamenti devono essere possibili a prescindere dal fatto che la colonna 
					// è o meno editabile infatti EXTERNAL_KEY DT_ININZIO_VAL DT_FINE_VAL sono attributi che
					// possono essere modificati sempre.
					
					
					if (evaluator.isExternalKey() && !item.isExternalKey()){
						Set objects = new IdentitySet();
						for (Object item2 : dcColumn.getDcAttributes())
							if (DcAttributes.EXTERNAL_KEY.name().equals(((DcAttribute) item2).getAttributeSpec()))
								objects.add(item2);
						// PER EVITARE ConcurrentModificationException
						for (Object item2 : objects)
						{
							// PER QUALCHE MISTERIOSA RAGIONE, SOLO COSI'
							// MI CANCELLA L'ATTRIBUTO, MENTRE COI GROUP BY
							// E ORDER BY E' SUFFICIENTE session.delete()
							dcColumn.getDcAttributes().remove(item2);
							session.delete(item2);							
						}						
					}else if (!evaluator.isExternalKey() && item.isExternalKey()){
						DcAttribute dcAttribute = new DcAttribute();
						dcAttribute.setDcColumn(dcColumn);
						dcAttribute.setAttributeSpec(DcAttributes.EXTERNAL_KEY.name());
						dcColumn.getDcAttributes().add(dcAttribute);
					}
					
					if (evaluator.isDataInizioVal() && !item.isFromDate())
					{
						Set objects = new IdentitySet();
						for (Object item2 : dcColumn.getDcAttributes())
							if (DcAttributes.DT_INIZIO_VAL.name().equals(((DcAttribute) item2).getAttributeSpec()))
								objects.add(item2);
						// PER EVITARE ConcurrentModificationException
						for (Object item2 : objects)
						{
							// PER QUALCHE MISTERIOSA RAGIONE, SOLO COSI'
							// MI CANCELLA L'ATTRIBUTO, MENTRE COI GROUP BY
							// E ORDER BY E' SUFFICIENTE session.delete()
							dcColumn.getDcAttributes().remove(item2);
							session.delete(item2);							
						}						
					}
					else if (!evaluator.isDataInizioVal() && item.isFromDate())
					{
						DcAttribute dcAttribute = new DcAttribute();
						dcAttribute.setDcColumn(dcColumn);
						dcAttribute.setAttributeSpec(DcAttributes.DT_INIZIO_VAL.name());
						dcColumn.getDcAttributes().add(dcAttribute);						
					}
					if (evaluator.isDataFineVal() && !item.isToDate())
					{
						Set objects = new IdentitySet();
						for (Object item2 : dcColumn.getDcAttributes())
							if (DcAttributes.DT_FINE_VAL.name().equals(((DcAttribute) item2).getAttributeSpec()))
								objects.add(item2);
						// PER EVITARE ConcurrentModificationException
						for (Object item2 : objects)
						{
							// PER QUALCHE MISTERIOSA RAGIONE, SOLO COSI'
							// MI CANCELLA L'ATTRIBUTO, MENTRE COI GROUP BY
							// E ORDER BY E' SUFFICIENTE session.delete()
							dcColumn.getDcAttributes().remove(item2);
							session.delete(item2);							
						}
					}
					else if (!evaluator.isDataFineVal() && item.isToDate())
					{
						DcAttribute dcAttribute = new DcAttribute();
						dcAttribute.setDcColumn(dcColumn);
						dcAttribute.setAttributeSpec(DcAttributes.DT_FINE_VAL.name());
						dcColumn.getDcAttributes().add(dcAttribute);						
					}
					
					
					// SE NON ERA EDITABILE, SIGNIFICA CHE E' UNA COLONNA
					// NON MODIFICABILE E SALTO LE MODIFICHE A PRIMARY_KEY GROUP_BY E ORDER_BY
					if (item.isEditable())
					{
						if (evaluator.isPrimaryKey() && !item.isPrimaryKey())
						{
							Set objects = new IdentitySet();
							for (Object item2 : dcColumn.getDcAttributes())
								if (DcAttributes.KEY.name().equals(((DcAttribute) item2).getAttributeSpec()))
									objects.add(item2);
							// PER EVITARE ConcurrentModificationException
							for (Object item2 : objects)
							{
								// PER QUALCHE MISTERIOSA RAGIONE, SOLO COSI'
								// MI CANCELLA L'ATTRIBUTO, MENTRE COI GROUP BY
								// E ORDER BY E' SUFFICIENTE session.delete()
								dcColumn.getDcAttributes().remove(item2);
								session.delete(item2);
							}
						}
						else if (!evaluator.isPrimaryKey() && item.isPrimaryKey())
						{
							DcAttribute dcAttribute = new DcAttribute();
							dcAttribute.setDcColumn(dcColumn);
							dcAttribute.setAttributeSpec(DcAttributes.KEY.name());
							String progr=StringUtils.leftPad(Integer.toString(progressivo),5,'0');
							dcAttribute.setAttributeVal(progr);
							dcColumn.getDcAttributes().add(dcAttribute);
							progressivo++;
						}

						if (evaluator.isGroupBy() && !item.isGroupBy())
						{
							for (Object item2 : dcColumn.getDvUeGroups())
								session.delete(item2);
						}
						else if (!evaluator.isGroupBy() && item.isGroupBy())
						{
							DvUeGroup dvUeGroup = new DvUeGroup();
							dvUeGroup.setDvUserEntity(dvUserEntity);
							dvUeGroup.setDcColumn(dcColumn);
							dvUserEntity.getDvUeGroups().add(dvUeGroup);
							dcColumn.getDvUeGroups().add(dvUeGroup);
						}
						if (evaluator.isOrderBy() && !item.isOrderBy())
						{
							for (Object item2 : dcColumn.getDcExpression().getDvUeOrders())
								session.delete(item2);
						}
						else if (!evaluator.isOrderBy() && item.isOrderBy())
						{
							DvUeOrder dvUeOrder = new DvUeOrder();
							dvUeOrder.setDvUserEntity(dvUserEntity);
							dvUeOrder.setDcExpression(dcExpression);
							dvUserEntity.getDvUeOrders().add(dvUeOrder);
							dcExpression.getDvUeOrders().add(dvUeOrder);
						}
					}
				}
				// COLONNA NUOVA
				else
				{
					dcColumn = new DcColumn();
					dcExpression = new DcExpression();
					dcColumn.setDcExpression(dcExpression);
					dcExpression.getDcColumns().add(dcColumn);
					dcColumn.setDcEntity(dcEntity);
					dcEntity.getDcColumns().add(dcColumn);

					// PRIMARY KEY, GROUP BY E ORDER BY
					if (item.isPrimaryKey())
					{
						DcAttribute dcAttribute = new DcAttribute();
						dcAttribute.setDcColumn(dcColumn);
						dcAttribute.setAttributeSpec(DcAttributes.KEY.name());
						dcAttribute.setAttributeVal(Integer.toString(progressivo));
						dcColumn.getDcAttributes().add(dcAttribute);
						progressivo++;
					}
					if (item.isExternalKey())
					{
						DcAttribute dcAttribute = new DcAttribute();
						dcAttribute.setDcColumn(dcColumn);
						dcAttribute.setAttributeSpec(DcAttributes.EXTERNAL_KEY.name());
						dcColumn.getDcAttributes().add(dcAttribute);
					}
					if (item.isGroupBy())
					{
						DvUeGroup dvUeGroup = new DvUeGroup();
						dvUeGroup.setDvUserEntity(dvUserEntity);
						dvUeGroup.setDcColumn(dcColumn);
						dvUserEntity.getDvUeGroups().add(dvUeGroup);
						dcColumn.getDvUeGroups().add(dvUeGroup);
					}
					if (item.isOrderBy())
					{
						DvUeOrder dvUeOrder = new DvUeOrder();
						dvUeOrder.setDvUserEntity(dvUserEntity);
						dvUeOrder.setDcExpression(dcExpression);
						dvUserEntity.getDvUeOrders().add(dvUeOrder);
						dcExpression.getDvUeOrders().add(dvUeOrder);
					}
				}
				// VALORI
				ValueExpression column = item.getColumn().getValue().clone();
				for (UserEntityColumnBeanAppendedValue item2 : item.getAppendedExpressions())
					column.appendExpression(oF.getValueExpressionOperator(item2.getChainOperator()), item2.getValue().getValue());
				dcExpression.setExpression(ValueExpression.getOperandXmlString(column).getBytes(UTF8_XML_ENCODING));
				dcExpression.setAlias(item.getAlias());
				dcExpression.setColType(ValueType.mapValueType2JavaType(column.getValueType()));
				dcColumn.setLongDesc(item.getName());
					
				selectList.add(dcColumn);
			}
			
			// ORA MI OCCUPO DELLE CONDIZIONI DI FILTRO ////////////////////////////////////////////////
			if (bean.getCondition() != null)
			{
				if (dvUserEntity.getDcRel() == null)
				{
					DcRel dcRelCondition = new DcRel();
					dcRelCondition.setCondition(bean.getCondition().getStringXml().getBytes(UTF8_XML_ENCODING));				
					dvUserEntity.setDcRel(dcRelCondition);
					dcRelCondition.getDvUserEntities().add(dvUserEntity);					
				}
				dvUserEntity.getDcRel().setCondition(bean.getCondition().getStringXml().getBytes(UTF8_XML_ENCODING));				
				// SALVO ORA PERCHE' SE NO DOPO,
				// FACENDO LE QUERY SU DcRel, Hibernate SI 
				// INCAZZA E DA' IL SEGUENTE ERRORE:
				// org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing: it.webred.diogene.db.model.DcRel
				synchronized(this) {session.saveOrUpdate(dvUserEntity.getDcRel());}
			}
			else if (dvUserEntity.getDcRel() != null)
				session.delete(dvUserEntity.getDcRel());
			

			// ORA MI OCCUPO DELLE RELAZIONI //////////////////////////////////////////////////////
			// ELIMINO LE RELAZIONI DA ELIMINARE
			if (!bean.getDeletedJoins().isEmpty())
			{
				q = session.createQuery("from DC_REL in class DcRel where DC_REL.id in (:id)");
				q.setParameterList("id", bean.getDeletedJoins());
				for (Object deleteable : q.list())
				{
					for (Object item2 : ((DcRel) deleteable).getDcFromRels())
						session.delete(item2);
					session.delete(deleteable);
				}
			}
			// AGGIORNO LE RELAZIONI NUOVE O MODIFICATE
			List<DcFromRel> newJoins = new ArrayList<DcFromRel>();
			List<DcRel> oldJoins = new ArrayList<DcRel>();
			for (JoinSpec item : bean.getJoins())
			{
				DvUeFromEntity former = fromMap.get(item.getFormerTable());
				DvUeFromEntity latter = fromMap.get(item.getLatterTable());
				DcFromRel dcFromRel = null;
				if (item.getId() == null)
				{
					dcFromRel = new DcFromRel();
					dcFromRel.setDvUeFromEntityByFkDvEuFrom1(former);
					dcFromRel.setDvUeFromEntityByFkDvEuFrom2(latter);
					former.getDcFromRelsForFkDvEuFrom1().add(dcFromRel);
					latter.getDcFromRelsForFkDvEuFrom2().add(dcFromRel);
					DcRel dcRelJoin = new DcRel();
					dcFromRel.setDcRel(dcRelJoin);
					dcRelJoin.getDcFromRels().add(dcFromRel);
					dcRelJoin.setCondition(item.getJoinCondition().getStringXml().getBytes(UTF8_XML_ENCODING));
					dcRelJoin.setOuterJoin(item.isOuterJoin() ? 1L : 0L);
					newJoins.add(dcFromRel);					
				}
				else
				{
					q = session.createQuery("from DC_REL in class DcRel where DC_REL.id = :id");
					q.setLong("id", item.getId());
					DcRel dcRelJoin = (DcRel) q.uniqueResult();
					

					dcFromRel= (DcFromRel)getUniqueElement( dcRelJoin.getDcFromRels());
					//pulisco la relazione da former e da latter in caso di swp della relazione
					former.getDcFromRelsForFkDvEuFrom1().remove(dcFromRel);
					former.getDcFromRelsForFkDvEuFrom2().remove(dcFromRel);
					latter.getDcFromRelsForFkDvEuFrom1().remove(dcFromRel);
					latter.getDcFromRelsForFkDvEuFrom2().remove(dcFromRel);

					dcFromRel.setDvUeFromEntityByFkDvEuFrom1(former);
					dcFromRel.setDvUeFromEntityByFkDvEuFrom2(latter);
					former.getDcFromRelsForFkDvEuFrom1().add(dcFromRel);
					latter.getDcFromRelsForFkDvEuFrom2().add(dcFromRel);

					dcRelJoin.setCondition(item.getJoinCondition().getStringXml().getBytes(UTF8_XML_ENCODING));
					dcRelJoin.setOuterJoin(item.isOuterJoin() ? 1L : 0L);
					oldJoins.add(dcRelJoin);
				}
			}
			
			
			// ORA PROCEDO AI SALVATAGGI /////////////////////////////////////////////////////////////
			// PERCHE' SIANO VALORIZZATE
			// LE CHIAVI DI FOREIGN KEY,
			// OCCORRE PRIMA SALVARE 
			// LE TABELLE CON LE CHIAVI
			// PRIMARIE, E POI LE TABELLE
			// FON LE FOREIGN KEY CHE PUNTANO
			// A QUESTE CHIAVI PRIMARIE
			// SINTESI: SALVARE PRIMA IL LATO "1"
			// E POI IL LATO "molti" DELLA RELAZIONE
			
			//se è cambiato il nome dell'entità devo modificare anche i permessi
	//		if (changedName) {
			// TODO: PER IL MOMENTO DISABILITATO
			if (false) {				
				Session theRightsSession = getSession(RIGHTS_SESSION_FACTORY_CONF_FILE);
				Transaction tranRights = theRightsSession.beginTransaction();
				try {					
					//creazione dai dati relativi ai diritti per l'utente connesso UserPrincipal					
					Principal principal = getContext().getExternalContext().getUserPrincipal();
					String context = getContext().getExternalContext().getRequestContextPath().substring(1);
					String ente = GestionePermessi.getEnteFromContext(principal,context);
					RightsManager rm = new RightsManager();
					//cancellazione permessi esistenti					
					Query qItem = theRightsSession.createQuery("from AmItem i where i.name=:name");
					qItem.setParameter("name", oldName + " (" + ente + ")");
					AmItem amItemOld =(AmItem)qItem.uniqueResult();
					rm.deleteAmItem(amItemOld,theRightsSession);
					//inserimento nuovi permessi	
					/* TODO CAMBIARE GESTIONE PERMESSI
					ByteArrayInputStream fis = (ByteArrayInputStream)FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(APPLICATION_XML_RELATIVE_PATH);
					AmApplication amApplication = (AmApplication) rm.decode(fis,bean.getName(),ente,principal.getName());					
					synchronized(this) {rm.saveOrUpdateAmApplication(amApplication,theRightsSession,principal.getName());}
					*/
					//commit e chiusura sessione
					tranRights.commit();
					theRightsSession.close();
				}catch (Exception e) {
					//rollback e chiusura sessione
					tranRights.rollback();
					theRightsSession.close();
					throw e;
				}
			}
			
			synchronized(this) {session.saveOrUpdate(dcEntity);}
			if (dvUserEntity.getDcRel() != null)
				synchronized(this) {session.saveOrUpdate(dvUserEntity.getDcRel());}
			synchronized(this) {session.saveOrUpdate(dvUserEntity);}
			for (DvUeFromEntity toRemoveFrom : toRemoveFromList) {				
				for (Object item2 : toRemoveFrom.getDcFromRelsForFkDvEuFrom1())
				{
					DcFromRel dcFromRel = (DcFromRel) item2;
					session.delete(dcFromRel);
					session.delete(dcFromRel.getDcRel());
				}
				for (Object item2 : toRemoveFrom.getDcFromRelsForFkDvEuFrom2())
				{
					DcFromRel dcFromRel = (DcFromRel) item2;
					session.delete(dcFromRel);
					session.delete(dcFromRel.getDcRel());
				}
				session.delete(toRemoveFrom);				
			}
			for (DvUeFromEntity from : fromMap.values())
				synchronized(this) {session.saveOrUpdate(from);}
			for (DcFromRel item : newJoins)
			{
				synchronized(this) {session.saveOrUpdate(item.getDcRel());}
				synchronized(this) {session.saveOrUpdate(item);}
			}
			for (DcRel item : oldJoins)
				synchronized(this) {session.saveOrUpdate(item);}	
			for (DcColumn item : selectList)
			{
				synchronized(this) {session.saveOrUpdate(item.getDcExpression());}
				synchronized(this) {session.saveOrUpdate(item);}
				for (Object dcAttribute : item.getDcAttributes())
					synchronized(this) {session.saveOrUpdate(dcAttribute);}
				for (Object dvUeOrder : item.getDcExpression().getDvUeOrders())
					synchronized(this) {session.saveOrUpdate(dvUeOrder);}
				for (Object dvUeGroup : item.getDvUeGroups())
					synchronized(this) {session.saveOrUpdate(dvUeGroup);}
			}
			
			// ORA SCRIVO ANCHE LA QUERY LETTERALE
			// NELLA COLONNA SQL_STATEMENT
			/*session.flush();
			session.refresh(dvUserEntity);
			session.refresh(dvUserEntity.getDcEntity());
			for (Object item : dvUserEntity.getDvUeFromEntities())
				session.refresh(item);*/
			
			String xml = "<sql_statement attribute='" + SqlStatementAttribute.GENERATED.name() + "'><![CDATA[";
			xml += Translator.getQueryFromDvUserEntity(dvUserEntity, sqlGenerator);
			xml += "]]></sql_statement>";
			dvUserEntity.setSqlStatement(xml.getBytes(UTF8_XML_ENCODING));
			//dvUserEntity.setSqlStatementBk("<sql_statement></sql_statement>".getBytes(UTF8_XML_ENCODING));
			dvUserEntity.setSqlStatementBk(xml.getBytes(UTF8_XML_ENCODING));
			
			synchronized(this) {session.saveOrUpdate(dvUserEntity);}
			trans.commit();
			session.close();
		}
		catch (Exception e)
		{
			trans.rollback();
			session.close();
			throw e;
		}
	}
	
	public void deleteUserEntity(Long id)
	throws QueryBuilderException
	{
		try
		{
			deleteUserEntity(id, getSession());
		}
		catch (QueryBuilderException e)
		{
			throw e;
		}
		catch (Throwable e) 
		{
			log.error(this.getClass().getName()+".deleteUserEntity", e);
			throw new QueryBuilderException("common.general.errorMessage",null,e.getClass().getName() , e.getMessage());
		}
	}

	private void deleteUserEntity(Long id, Session session)
	throws QueryBuilderException, Exception
	{
		Transaction trans = session.beginTransaction();
		//la sessione per i diritti

		Session sessionRights = getSession(RIGHTS_SESSION_FACTORY_CONF_FILE);
		Transaction transRights =sessionRights.beginTransaction();
		try
		{
			Query q = session.createQuery("from DV_USER_ENTITY in class DvUserEntity where DV_USER_ENTITY.dcEntity=:id");
			q.setLong("id", id);
			DvUserEntity dvUserEntity = (DvUserEntity) q.uniqueResult();
			DcEntity dcEntity = dvUserEntity.getDcEntity();
			if (!dcEntity.getDvUeFromEntities().isEmpty())
			{
				String otherUserEntitiesNames = "";
				String separator = "\\r\\n"; // NON "\r\n", PERCHE' E' IN JavaScript CHE DOVRA' RISULTARE IL QUOTE \r\n
				for (Object item : dcEntity.getDvUeFromEntities())
				{
					DvUserEntity otherUserEntity = ((DvUeFromEntity) item).getDvUserEntity();
					otherUserEntitiesNames += separator + otherUserEntity.getDcEntity().getName();
					separator = ", \\r\\n"; // NON "\r\n", PERCHE' E' IN JavaScript CHE DOVRA' RISULTARE IL QUOTE \r\n
				}
				throw new QueryBuilderException("entities.otherUserEntitiesDependsFromThisUserEntity.errorMessage", null, otherUserEntitiesNames);				
			}
			if((dcEntity.getDcEntityRelsForFkDcEntity1().size()>0) ||(dcEntity.getDcEntityRelsForFkDcEntity2().size()>0) ){
				throw new QueryBuilderException("entities.otherEntitiesHaveRealtionWithThisUserEntity.errorMessage", null);
			}
			for (Object item : dcEntity.getDcColumns())
			{
				DcColumn dcColumn = (DcColumn) item;
				for (Object item2 : dcColumn.getDvUeGroups())
					session.delete(item2);
				for (Object item2 : dcColumn.getDcAttributes())
					session.delete(item2);
				for (Object item2 : dcColumn.getDcTipoeColumns())
					session.delete(item2);				
				DcExpression dcExpression = dcColumn.getDcExpression();
				for (Object item2 : dcExpression.getDvUeOrders())
					session.delete(item2);
				session.delete(dcColumn);
				session.delete(dcExpression);
			}
			for (Object item1 : dvUserEntity.getDvUeFromEntities())
			{
				DvUeFromEntity from = (DvUeFromEntity) item1;
				for (Object item2 : from.getDcFromRelsForFkDvEuFrom1())
				{
					DcFromRel dcFromRel = (DcFromRel) item2;
					session.delete(dcFromRel);
					session.delete(dcFromRel.getDcRel());
				}
				for (Object item2 : from.getDcFromRelsForFkDvEuFrom2())
				{
					DcFromRel dcFromRel = (DcFromRel) item2;
					session.delete(dcFromRel);
					session.delete(dcFromRel.getDcRel());
				}
				session.delete(from);
			}
			for (Object item1 : dcEntity.getDcEntityRelsForFkDcEntity1())
			{
				DcEntityRel dcEntityRel = (DcEntityRel) item1;
				for (Object item2 : dcEntityRel.getDvLinks())
				{
					DvLink dvLink = (DvLink) item2;
					for (Object item3 : dvLink.getDvKeyPairs())
						session.delete(item3);
					session.delete(dvLink);
				}
				session.delete(dcEntityRel);
				session.delete(dcEntityRel.getDcRel());
			}
			for (Object item1 : dcEntity.getDcEntityRelsForFkDcEntity2())
			{
				DcEntityRel dcEntityRel = (DcEntityRel) item1;
				for (Object item2 : dcEntityRel.getDvLinks())
				{
					DvLink dvLink = (DvLink) item2;
					for (Object item3 : dvLink.getDvKeyPairs())
						session.delete(item3);
					session.delete(dvLink);
				}
				session.delete(dcEntityRel);
				session.delete(dcEntityRel.getDcRel());
			}
			for (Object item : dcEntity.getDvKeies())	
				session.delete(item);
			for (Object item : dvUserEntity.getDvFormatClasses())
				session.delete(item);
			session.delete(dvUserEntity);
			if (dvUserEntity.getDcRel() != null)
				session.delete(dvUserEntity.getDcRel());
			
			//eliminazione dei diritti relativi all'entità dal sistema.
			/* TODO CAMBIARE GESTIONE EPRMESSI
			RightsManager rm = new RightsManager();
			String ente=GestionePermessi.getEnteFromContext(getContext().getExternalContext().getUserPrincipal(), getContext().getExternalContext().getRequestContextPath().substring(1));
			Query qItem = sessionRights.createQuery("from AmItem i where i.name=:name");
			qItem.setParameter("name",dcEntity.getName()+" ("+ente+")");
			AmItem amItem =(AmItem)qItem.uniqueResult();
			rm.deleteAmItem(amItem,sessionRights);
			*/
			
			session.delete(dcEntity);
			trans.commit();
			transRights.commit();
			sessionRights.close();
			session.close();
		}
		catch (QueryBuilderException e)
		{
			trans.rollback();
			transRights.rollback();
			sessionRights.close();
			session.close();
			throw e;
		}
		catch (Exception e)
		{
			trans.rollback();
			transRights.rollback();
			sessionRights.close();
			session.close();
			throw new QueryBuilderException("common.general.errorMessage",null,e.getClass().getName(),e.getMessage());
		}
	}
	
	
	// NON-PUBLIC METHODS
	synchronized private Session getSession(String...whichFactoryConfFile) throws Exception
	{
		String factory = (whichFactoryConfFile.length==1)?whichFactoryConfFile[0]:HibernateSession.DEFAULT_SESSION_FACTORY_CONF_FILE;
		
		Session sexion=sexionMap.get(factory);
		if (sexion == null || !sexion.isConnected() || !sexion.isOpen())
		{
			SessionManager sessionManager = sessionManagerMap.get(factory);
			if (sessionManager != null && sessionManager.isAlive())
			{
				sessionManager.interrupt();
				synchronized (this) {wait();}
			}
			sessionManager = new SessionManager(factory);
			// ASPETTO CHE sessionManager MI DIA IL VIA LIBERA
			// ALTRIMENTI POTREI INCORRERE IN UN NullPointerException
			// O IN QUALCHE ALTRO PROBLEMA
			synchronized (this)
			{
				wait();
			}
			sexion = sexionMap.get(factory);
	//		if (sexion == null || !sexion.isConnected() || !sexion.isOpen())
	//			throw new QueryBuilderException("common.general.errorMessage",null,QueryBuilderException.class.getName() , "Errore di sincronizzazione della Session Hibernate");
		}
		return sexion;
	}
	
	/**
	 * Questa classe si occupa di gestire
	 * in un unico thread la vita della sessione di
	 * Hibernate. L'utilit&agrave; di questo 
	 * approccio consiste nella possibilit&agrave;
	 * di chiamare anche da un altro thread
	 * la chiusura della connessione di Hibernate,
	 * come avviene qui per il metodo 
	 * {@link EntitiesDBManager#chiudiTutto()},
	 * che viene chiamato anche da 
	 * {@link it.webred.diogene.querybuilder.control.QueryBuilderSessionListener#sessionDestroyed(HttpSessionEvent)}
	 * metodo che, come si può verificare stampando il nome
	 * del Thread corrente, viene chiamato da un altro
	 * thread rispetto a quello durante il quale viene 
	 * chiamato il metodo {@link EntitiesDBManager#getSession()}
	 * @author Giulio Quaresima
	 * @version $Revision: 1.13.2.1 $ $Date: 2012/01/26 16:59:39 $
	 */
	private class SessionManager extends Thread
	{
		private String factory;
		public SessionManager(String whichFactoryConfFile)
		{
			super();
			factory=whichFactoryConfFile;
			start();
		}
		
		public void run()
		{
			super.run();
			
			// CREO LA SESSIONE
			try
			{
				sessionOwnerMap.put(factory,HibernateSession.createSession(factory))  ;
				sexionMap.put(factory, HibernateSession.getSession(factory));
			}
			catch (Throwable e)
			{
				throw new RuntimeException("Impossibile ottenere una connessione", e);
			}
			finally
			{
				synchronized (EntitiesDBManager.this)
				{
					// DO IL VIA LIBERA
					EntitiesDBManager.this.notify();
				}				
			}
			
			try
			{
				// ASPETTO
				synchronized (this)
				{
					wait();
				}
				// MI HANNO SVEGLIATO, QUINDI E' L'ORA CHE IO
				// CHIUDA LA SESSIONE DI HIBERNATE
				Session sexion=sexionMap.get(factory);
				Object sessionOwner = sessionOwnerMap.get(factory);
				if (sexion != null && sexion.isConnected() && sexion.isOpen())
				{
					HibernateSession.closeSession(sessionOwner,factory);
					
					sexionMap.remove(factory);
					log.debug("Ho effettivamente chiuso la sessione di Hibernate!!!"); 
					
				}
				sessionOwnerMap.remove(factory);
				sexion = null;
				sessionOwner = null;
			}
			catch (InterruptedException e)
			{
				// MI HANNO INTERROTTO, QUINDI E' COMUNQUE ORA CHE IO
				// CHIUDA LA SESSIONE DI HIBERNATE, SE ESISTE
				Session sexion=sexionMap.get(factory);
				Object sessionOwner = sessionOwnerMap.get(factory);
				if (sexion != null && sexion.isConnected() && sexion.isOpen())
				{
					try
					{
						HibernateSession.closeSession(sessionOwner);
						
						sexionMap.remove(factory);
						log.debug("Ho effettivamente chiuso la sessione di Hibernate!!!"); 
					}
					catch (Exception err)
					{
						log.error(this.getClass().getName()+".run", err); // TODO ELIMINARE
					}
				}
				sessionOwnerMap.remove(factory);
				sexion = null;
				sessionOwner = null;
			}
			catch (Exception e)
			{
				log.error("", e); // TODO ELIMINARE
			}
			finally
			{
				synchronized (EntitiesDBManager.this)
				{
					// DO IL VIA LIBERA
					EntitiesDBManager.this.notify();
				}								
			}
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		for (Map.Entry<String,SessionManager> entry : sessionManagerMap.entrySet())
		{
			SessionManager sessionManager= entry.getValue();
			if (sessionManager != null && sessionManager.isAlive())
				sessionManager.interrupt();
		}
		super.finalize();
	}
	public synchronized void flushSession() {
		try
		{
			getSession().close();
		}
		catch (Exception e)
		{
			log.error("",e);
		}
	}
	/**
	 * Testa getSession();
	 * @param args
	 * @throws Exception
	 */
	public static void main (String args[]) throws Exception
	{
		EntitiesDBManager dsfkljghdf = new EntitiesDBManager();
		Session session = null;
		for (int i = 0; i++ < 50;)
		{
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			if (session != null)
				System.out.println("La sessione è aperta? " + session.isOpen());
			session = dsfkljghdf.getSession();
			if (session != null)
				System.out.println("La sessione è aperta? " + session.isOpen());
			Transaction trans = session.beginTransaction();
			Query q = session.createQuery("from DC_REL in class DcRel");
			for (Object item : q.list())
				System.out.print(((DcRel) item).getId() + ", ");
			System.out.println("");
			session.flush();
			trans.commit();
			session.close();
			dsfkljghdf.chiudiTutto();
		}
	}
}
