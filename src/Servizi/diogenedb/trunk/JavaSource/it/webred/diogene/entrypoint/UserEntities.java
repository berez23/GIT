package it.webred.diogene.entrypoint;

import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;
import static it.webred.utils.StringUtils.isEmpty;



import it.webred.diogene.db.model.DvUeFromEntity;
import it.webred.diogene.db.model.DvUserEntity;
import it.webred.diogene.querybuilder.control.UserEntityBean;
import it.webred.diogene.querybuilder.db.EntitiesDBManager;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.utils.MetadataConstants.DBType;

import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;



public class UserEntities {

	public UserEntitiesBean getUserEntities() throws Exception {
		ArrayList<HashMap<String, Object>> userEntities = new ArrayList<HashMap<String, Object>>();
		Session session = null;
		try {
			session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Query q = session.createQuery("from DV_USER_ENTITY in class DvUserEntity");
			List list = q.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				DvUserEntity dvue = (DvUserEntity)it.next();
				HashMap<String, Object> dvueHm = new HashMap<String, Object>();
				dvueHm.put("dvUserEntityId", dvue.getId());
				dvueHm.put("dcEntityId", dvue.getDcEntity().getId());
				dvueHm.put("name", dvue.getDcEntity().getName());
				dvueHm.put("sqlStatement", new String(dvue.getSqlStatement(), UTF8_XML_ENCODING));
				HashMap<String, String> entitiesNames = new HashMap<String, String>();
				Iterator itNames = dvue.getDvUeFromEntities().iterator();
				while (itNames.hasNext()) {
					DvUeFromEntity dufe = (DvUeFromEntity)itNames.next();
					entitiesNames.put(dufe.getDcEntity().getName(), dufe.getDcEntity().getName());
				}
				dvueHm.put("entitiesNames", entitiesNames);
				userEntities.add(dvueHm);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return new UserEntitiesBean(userEntities);
	}
	
	public UserEntityBeanSer getUserEntityBean(Long entityId) throws Exception {
		UserEntityBeanSer retVal = new UserEntityBeanSer();
		Session session = null;
		try {
			session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			SqlGenerator sqlGen = new SqlGenerator(it.webred.diogene.sqldiagram.EnumsRepository.DBType.ORACLE);
			UserEntityBean ueb = new EntitiesDBManager().getUserEntityToExport(entityId, session, sqlGen);
/*			LinkedHashMap<String, Object> lhm = toLHM(ueb, session, sqlGen);
			//codifica
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLEncoder enc = new XMLEncoder(baos);
			enc.setPersistenceDelegate(Timestamp.class, new DefaultPersistenceDelegate(new String[] {"time"}));

			enc.writeObject(lhm);
			enc.flush();
			String xml = new String(baos.toByteArray(), UTF8_XML_ENCODING) + "</java>";  //senza "</java>" si ha eccezione Unexpected EOF...
*/
			// Object serialization
		    try {
		      UserEntityBean object1 = ueb;
		      ByteArrayOutputStream baos = new ByteArrayOutputStream();
		      ObjectOutputStream oos = new ObjectOutputStream(baos);
		      oos.writeObject(object1);
		      oos.flush();
		      oos.close();
 
		      retVal.setUserEntityBean(baos.toByteArray());
		    }
		    catch(Exception e) {
		      e.printStackTrace();
		      System.out.println("Exception during serialization: " + e);
		      throw e;
		    }
		    
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}
/*	
	private LinkedHashMap<String, Object> toLHM(UserEntityBean ueb, Session session, SqlGenerator sqlGen) {
		LinkedHashMap<String, Object> retVal = new LinkedHashMap<String, Object>();		
		try {
			OperatorFactory oF = sqlGen.getOperatorFactory();
			retVal.put("explicitSql", ueb.getExplicitSql());
			if (!isEmpty(ueb.getExplicitSql()))
			{
				//DcEntity
				LinkedHashMap<String, Object> dcEntity = new LinkedHashMap<String, Object>();
				dcEntity.put("name", ueb.getName());
				dcEntity.put("longDesc", ueb.getDescription());
				dcEntity.put("dtIns", ueb.getDtMod());
				dcEntity.put("dtMod", ueb.getDtMod());
				dcEntity.put("userIns", ueb.getUserMod());
				dcEntity.put("userMod", ueb.getUserMod());
				retVal.put("dcEntity", dcEntity);
				//DvUserEntity
				LinkedHashMap<String, Object> dvUserEntity = new LinkedHashMap<String, Object>();
				dvUserEntity.put("dcEntity", dcEntity);
				String xml = "<sql_statement attribute='" + SqlStatementAttribute.EXPLICIT.name() + "'><![CDATA[";
				xml += ueb.getExplicitSql();
				xml += "]]></sql_statement>";
				dvUserEntity.put("sqlStatement", xml.getBytes(UTF8_XML_ENCODING));
				dvUserEntity.put("sqlStatementBk", xml.getBytes(UTF8_XML_ENCODING));
				retVal.put("dvUserEntity", dvUserEntity);
				return retVal;
			}
			//DcEntity			
			LinkedHashMap<String, Object> dcEntity = new LinkedHashMap<String, Object>();
			dcEntity.put("name", ueb.getName());
			dcEntity.put("longDesc", ueb.getDescription());
			dcEntity.put("dtIns", ueb.getDtMod());
			dcEntity.put("dtMod", ueb.getDtMod());
			dcEntity.put("userIns", ueb.getUserMod());
			dcEntity.put("userMod", ueb.getUserMod());
			//DvUserEntity
			LinkedHashMap<String, Object> dvUserEntity = new LinkedHashMap<String, Object>();
			dvUserEntity.put("dcEntity", dcEntity);
			/////////////////////////////////////////
			LinkedHashMap dvUeFromEntities = dcEntity.get("dvUeFromEntities") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcEntity.get("dvUeFromEntities");
			dvUeFromEntities.put("dvUserEntity" + dvUeFromEntities.size(), dvUserEntity);
			dcEntity.put("dvUeFromEntities", dvUeFromEntities);
			/////////////////////////////////////////
			dvUserEntity.put("sqlStatement", "<void />".getBytes(UTF8_XML_ENCODING));
			dvUserEntity.put("sqlStatementBk", "<void />".getBytes(UTF8_XML_ENCODING));
			dvUserEntity.put("predicate", ueb.getPredicate());
			//DvUeFromEntity
			LinkedHashMap<String, Object> fromMap = new LinkedHashMap<String, Object>();
			for (DcEntityBean item : ueb.getFromList())
			{
				LinkedHashMap<String, Object> dvUeFromEntity = new LinkedHashMap<String, Object>();
				dvUeFromEntity.put("dvUserEntity", dvUserEntity);
				dvUeFromEntity.put("id", item.getId());
				dvUeFromEntity.put("name", item.getName());
				if (item.getAlias() != null && !"".equals(item.getAlias().trim()))
					dvUeFromEntity.put("alias", item.getAlias());
				else if (item instanceof DvUserEntityBean)
					dvUeFromEntity.put("alias", item.getSqlName());
				
				LinkedHashMap dvUeFromEnts = dvUserEntity.get("dvUeFromEntities") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dvUserEntity.get("dvUeFromEntities");
				dvUeFromEnts.put("dvUserEntity" + dvUeFromEnts.size(), dvUserEntity);
				dvUserEntity.put("dvUeFromEntities", dvUeFromEnts);
				fromMap.put("" + item.getId(), dvUeFromEntity);
			}
			//campi
			LinkedHashMap<String, Object> selectList = new LinkedHashMap<String, Object>();
			int progressivo = 0;
			for (UserEntityColumnBean item : ueb.getSelectList()) {
				LinkedHashMap<String, Object> dcColumn = new LinkedHashMap<String, Object>();
				LinkedHashMap<String, Object> dcExpression = new LinkedHashMap<String, Object>();
				dcColumn.put("primaryKey", item.isPrimaryKey());
				dcColumn.put("externalKey", item.isExternalKey());
				dcColumn.put("fromDate", item.isFromDate());
				dcColumn.put("toDate", item.isToDate());
				dcColumn.put("groupBy", item.isGroupBy());
				dcColumn.put("orderBy", item.isOrderBy());
				dcExpression.put("alias", item.getAlias());
				ValueExpression exp = item.getColumn().getValue().clone();
				for (UserEntityColumnBeanAppendedValue item2 : item.getAppendedExpressions())
					exp.appendExpression(oF.getValueExpressionOperator(item2.getChainOperator()), item2.getValue().getValue());
				dcExpression.put("colType", ValueType.mapValueType2JavaType(exp.getValueType()));
				dcExpression.put("expression", ValueExpression.getOperandXmlString(exp).getBytes(UTF8_XML_ENCODING));
				dcColumn.put("longDesc", item.getName());
				if (item.isPrimaryKey()) {
					LinkedHashMap<String, Object> dcAttribute = new LinkedHashMap<String, Object>();
					dcAttribute.put("dcColumn", dcColumn);
					dcAttribute.put("attributeSpec", DcAttributes.KEY.name());
					String progr = StringUtils.leftPad(Integer.toString(progressivo),5,'0');
					dcAttribute.put("attributeVal", progr);					
					LinkedHashMap dcAttributes = dcColumn.get("dcAttributes") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcColumn.get("dcAttributes");
					dcAttributes.put("dcAttribute" + dcAttributes.size(), dcAttribute);
					dcColumn.put("dcAttributes", dcAttributes);
					progressivo++;
				}
				if (item.isExternalKey()) {
					LinkedHashMap<String, Object> dcAttribute = new LinkedHashMap<String, Object>();
					dcAttribute.put("dcColumn", dcColumn);
					dcAttribute.put("attributeSpec", DcAttributes.EXTERNAL_KEY.name());
					LinkedHashMap dcAttributes = dcColumn.get("dcAttributes") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcColumn.get("dcAttributes");
					dcAttributes.put("dcAttribute" + dcAttributes.size(), dcAttribute);
					dcColumn.put("dcAttributes", dcAttributes);
				}
				if (item.isFromDate()) {
					LinkedHashMap<String, Object> dcAttribute = new LinkedHashMap<String, Object>();
					dcAttribute.put("dcColumn", dcColumn);
					dcAttribute.put("attributeSpec", DcAttributes.DT_INIZIO_VAL.name());
					LinkedHashMap dcAttributes = dcColumn.get("dcAttributes") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcColumn.get("dcAttributes");
					dcAttributes.put("dcAttribute" + dcAttributes.size(), dcAttribute);
					dcColumn.put("dcAttributes", dcAttributes);				
				}
				if (item.isToDate()) {
					LinkedHashMap<String, Object> dcAttribute = new LinkedHashMap<String, Object>();
					dcAttribute.put("dcColumn", dcColumn);
					dcAttribute.put("attributeSpec", DcAttributes.DT_FINE_VAL.name());
					LinkedHashMap dcAttributes = dcColumn.get("dcAttributes") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcColumn.get("dcAttributes");
					dcAttributes.put("dcAttribute" + dcAttributes.size(), dcAttribute);
					dcColumn.put("dcAttributes", dcAttributes);				
				}
				if (item.isGroupBy()) {
					LinkedHashMap<String, Object> dvUeGroup = new LinkedHashMap<String, Object>();
					dvUeGroup.put("dvUserEntity", dvUserEntity);
					dvUeGroup.put("dcColumn", dcColumn);					
					LinkedHashMap dvUeGroups = dvUserEntity.get("dvUeGroups") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dvUserEntity.get("dvUeGroups");
					dvUeGroups.put("dvUeGroup" + dvUeGroups.size(), dvUeGroup);
					dvUserEntity.put("dvUeGroups", dvUeGroups);					
					LinkedHashMap dvUeGroups1 = dcColumn.get("dvUeGroups") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcColumn.get("dvUeGroups");
					dvUeGroups1.put("dvUeGroup" + dvUeGroups1.size(), dvUeGroup);
					dcColumn.put("dvUeGroups", dvUeGroups1);
				}
				if (item.isOrderBy()) {
					LinkedHashMap<String, Object> dvUeOrder = new LinkedHashMap<String, Object>();
					dvUeOrder.put("dvUserEntity", dvUserEntity);
					dvUeOrder.put("dcExpression", dcExpression);
					
					LinkedHashMap dvUeOrders = dvUserEntity.get("dvUeOrders") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dvUserEntity.get("dvUeOrders");
					dvUeOrders.put("dvUeOrder" + dvUeOrders.size(), dvUeOrder);
					dvUserEntity.put("dvUeOrders", dvUeOrders);					
					LinkedHashMap dvUeOrders1 = dcExpression.get("dvUeOrders") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcExpression.get("dvUeOrders");
					dvUeOrders1.put("dvUeOrder" + dvUeOrders1.size(), dvUeOrder);
					dcExpression.put("dvUeOrders", dvUeOrders1);
				}
				dcColumn.put("dcExpression", dcExpression);				
				LinkedHashMap dcColumns = dcExpression.get("dcColumns") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcExpression.get("dcColumns");
				dcColumns.put("dcColumn" + dcColumns.size(), dcColumn);
				dcExpression.put("dcColumns", dcColumns);
				dcColumn.put("dcEntity", dcEntity);
				
				LinkedHashMap dcColumns1 = dcEntity.get("dcColumns") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcEntity.get("dcColumns");
				dcColumns1.put("dcColumn" + dcColumns1.size(), dcColumn);
				dcEntity.put("dcColumns", dcColumns1);
				selectList.put("" + item.getId(), dcColumn);
			}
			//condizioni di filtro
			Condition condition = null;
			if (ueb.getCondition() != null)
				if (condition == null)
					condition = ueb.getCondition();
			LinkedHashMap<String, Object> dcRelCondition = null;
			if (condition != null) {
				dcRelCondition = new LinkedHashMap<String, Object>();
				LinkedHashMap dvUserEntities = dcRelCondition.get("dvUserEntities") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcRelCondition.get("dvUserEntities");
				dvUserEntities.put("dvUserEntity" + dvUserEntities.size(), dvUserEntity);
				dcRelCondition.put("dvUserEntities", dvUserEntities);
				dcRelCondition.put("condition", condition.getStringXml().getBytes(UTF8_XML_ENCODING));
				dvUserEntity.put("dcRel", dcRelCondition);
			}
			//relazioni
			LinkedHashMap<String, Object> joins = new LinkedHashMap<String, Object>();
			for (JoinSpec item : ueb.getJoins()) {
				//TODO (verificare se ci sono id...)
				LinkedHashMap former = (LinkedHashMap)fromMap.get("" + item.getFormerTable().getId());
				LinkedHashMap latter = (LinkedHashMap)fromMap.get("" + item.getLatterTable().getId());
				LinkedHashMap<String, Object> dcFromRel = new LinkedHashMap<String, Object>();
				dcFromRel.put("dvUeFromEntityByFkDvEuFrom1", former);
				dcFromRel.put("dvUeFromEntityByFkDvEuFrom2", latter);
				LinkedHashMap dcFromRelsForFkDvEuFrom1 = former.get("dcFromRelsForFkDvEuFrom1") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)former.get("dcFromRelsForFkDvEuFrom1");
				dcFromRelsForFkDvEuFrom1.put("dcFromRel" + dcFromRelsForFkDvEuFrom1.size(), dcFromRel);
				former.put("dcFromRelsForFkDvEuFrom1", dcFromRelsForFkDvEuFrom1);
				LinkedHashMap dcFromRelsForFkDvEuFrom2 = latter.get("dcFromRelsForFkDvEuFrom2") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)latter.get("dcFromRelsForFkDvEuFrom2");
				dcFromRelsForFkDvEuFrom2.put("dcFromRel" + dcFromRelsForFkDvEuFrom2.size(), dcFromRel);
				latter.put("dcFromRelsForFkDvEuFrom2", dcFromRelsForFkDvEuFrom2);
				LinkedHashMap<String, Object> dcRelJoin = new LinkedHashMap<String, Object>();
				LinkedHashMap dcFromRels = dcRelJoin.get("dcFromRels") == null ? new LinkedHashMap<String, Object>() : (LinkedHashMap)dcRelJoin.get("dcFromRels");
				dcFromRels.put("dcFromRel" + dcFromRels.size(), dcFromRel);
				dcRelJoin.put("dcFromRels", dcFromRels);
				dcRelJoin.put("condition", item.getJoinCondition().getStringXml().getBytes(UTF8_XML_ENCODING));
				dcRelJoin.put("outerJoin", item.isOuterJoin() ? 1L : 0L);
				dcFromRel.put("dcRel", dcRelJoin);
				joins.put("dcFromRel", dcFromRel);
			}
			
			retVal.put("dcEntity", dcEntity);
			if (dcRelCondition != null)
				retVal.put("dcRelCondition", dcRelCondition);
			retVal.put("dvUserEntity", dvUserEntity);
			retVal.put("fromMap", fromMap);
			retVal.put("joins", joins);
			retVal.put("selectList", selectList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	*/
	
}
