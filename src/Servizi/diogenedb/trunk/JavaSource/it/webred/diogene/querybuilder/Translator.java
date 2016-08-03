package it.webred.diogene.querybuilder;

import static it.webred.utils.CollectionsUtils.getUniqueElement;
import static it.webred.utils.StringUtils.isEmpty;
import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;
import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.db.model.DcEntity;
import it.webred.diogene.db.model.DcExpression;
import it.webred.diogene.db.model.DcFromRel;
import it.webred.diogene.db.model.DcRel;
import it.webred.diogene.db.model.DcSchemaEntity;
import it.webred.diogene.db.model.DvUeFromEntity;
import it.webred.diogene.db.model.DvUserEntity;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.querybuilder.beans.DcSchemaEntityBean;
import it.webred.diogene.querybuilder.control.MetadataController;
import it.webred.diogene.querybuilder.control.UserEntityBean;
import it.webred.diogene.querybuilder.control.JoinSpec;
import it.webred.diogene.querybuilder.control.UserEntityColumnBean;
import it.webred.diogene.querybuilder.control.UserEntityColumnBeanAppendedValue;
import it.webred.diogene.sqldiagram.Condition;
import it.webred.diogene.sqldiagram.ConditionFactory;
import it.webred.diogene.sqldiagram.ExpressionFactory;
import it.webred.diogene.sqldiagram.OperatorFactory;
import it.webred.diogene.sqldiagram.Query;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.TableExpression;
import it.webred.diogene.sqldiagram.ValueExpression;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Classe ancora in fase inizilale di sviluppo.
 * Serve per trasformare una 
 * {@link it.webred.diogene.db.model.DvUserEntity}
 * in una {@link it.webred.diogene.sqldiagram.Query}
 * @author Giulio Quaresima
 * @version $Revision: 1.3 $ $Date: 2007/11/22 15:59:57 $
 */
public class Translator
{
	private static final Logger log = Logger.getLogger(Translator.class);
	/**
	 * Trasforma una {@link it.webred.diogene.db.model.DvUserEntity}
	 * in una {@link it.webred.diogene.sqldiagram.Query}
	 * @param uEntity
	 * @param cF
	 * @param oF
	 * @param eF
	 * @return
	 * @throws QueryBuilderException
	 */
	@SuppressWarnings("unchecked")
	public static Query getQueryFromDvUserEntity(DvUserEntity uEntity, SqlGenerator sqlGen) 
	throws QueryBuilderException, Exception
	{
		@SuppressWarnings("unused") ConditionFactory cF = sqlGen.getConditionFactory();
		OperatorFactory oF = sqlGen.getOperatorFactory();
		ExpressionFactory eF = sqlGen.getExpressionFactory();
		Query result = null;
		if (uEntity != null)
		{
			result = sqlGen.getQuery();
			if (uEntity.getPredicate() != null)
				result.setPredicate(eF.getPredicate(uEntity.getPredicate()));
			Map<Long,DcRel> relations = new HashMap<Long,DcRel>();
			
			// AGGIUNGO LE TABELLE DELLA FROM
			for (Object item : uEntity.getDvUeFromEntities())
			{
				DvUeFromEntity dvUeFromEntity = (DvUeFromEntity) item;
				String alias = dvUeFromEntity.getAlias();
				DcEntity dcEntity = dvUeFromEntity.getDcEntity();
				if (dcEntity.getDcSchemaEntities().size() == 1)
				{
					DcSchemaEntity dcSchemaEntity = (DcSchemaEntity) getUniqueElement(dcEntity.getDcSchemaEntities());
					result.addFromTable(new TableExpression(dcSchemaEntity.getId(), dcSchemaEntity.getSqlName(), dcEntity.getName(), alias));
				}
				else if (dcEntity.getDvUserEntities().size() == 1)
				{
					if (alias == null || "".equals(alias.trim()))
						throw new QueryBuilderException(
								"Tabella DV_UE_FROM_ENTITY, " +
								"con ID = " + ((DvUeFromEntity) item).getId() +
								". ATTENZIONE. Quando la from punta " +
								"ad una DC_ENTITY che \u00E0 in realt\u00E0 " +
								"una DC_USER_ENTITY, deve avere " +
								"obbligatoriamente un ALIAS", null);
					DvUserEntity dvUserEntity = (DvUserEntity) getUniqueElement(dcEntity.getDvUserEntities());
					Query fromTable = getQueryFromDvUserEntity(dvUserEntity, sqlGen);
					fromTable.setAlias(alias);
					result.addFromTable(fromTable);
				}
				else throw new QueryBuilderException(
							"C'\u00E8 un'incoerenza grave nel db: " +
							"il record di DC_ENTITY con id " + 
							dcEntity.getId() + 
							" dovrebbe essere puntato o da uno " +
							"ed un solo record di DC_SCHEMA_ENTITY " +
							"o da uno ed un solo record di DV_USER_ENTITY, " +
							"ma non \u00E8 cos\u00EC", null);
				
				// EVITO DI INSERIRE DOPPIONI DELLA STESSA DC_REL
				for (Object dcFromRel : dvUeFromEntity.getDcFromRelsForFkDvEuFrom1())
					relations.put(((DcFromRel) dcFromRel).getId(), ((DcFromRel) dcFromRel).getDcRel());
				for (Object dcFromRel : dvUeFromEntity.getDcFromRelsForFkDvEuFrom2())
					relations.put(((DcFromRel) dcFromRel).getId(), ((DcFromRel) dcFromRel).getDcRel());
			}
			
			//ORA AGGIUNGO I CAMPI IN SELECT
			DcEntity dcEntity = uEntity.getDcEntity();
			ArrayList<DcColumn> columns = new ArrayList(dcEntity.getDcColumns());
			//ordinamento per id
			Comparator comparator = new Comparator()
	        {
	            public int compare(Object o1, Object o2)
	            {
	                Long id1 = ((DcColumn)o1).getId();
	                Long id2 = ((DcColumn)o2).getId();
	                return id1.compareTo(id2);
	            }
	        };
			Collections.sort(columns, comparator);
			for (DcColumn dcColumn : columns)
			{
				DcExpression dcExpression = dcColumn.getDcExpression();
				DcColumnEvaluator evaluator = new DcColumnEvaluator(dcColumn);
				ValueExpression exp = null;
				try	{exp = ValueExpression.createFromXml(new String(dcExpression.getExpression(), UTF8_XML_ENCODING), sqlGen);}
				catch (UnsupportedEncodingException e) {}
				exp.setAlias(dcExpression.getAlias());
				exp.setDescription(dcColumn.getLongDesc());
				result.addSelectExpression(exp);
				if (evaluator.isGroupBy())
					result.getGroupByList().add(exp);
				if (evaluator.isOrderBy())
					result.getOrderByList().add(exp);
			}
			
			// ED, INFINE, LE EVENTUALI RELAZIONI E CONDIZIONE
			// RELAZIONI (JOINS)
			Condition join = new Condition();
			for (DcRel dcRel : relations.values())
				try
				{
					join.mergeCondition(Condition.createFromXml(new String(dcRel.getCondition(), UTF8_XML_ENCODING), sqlGen));
				}
				catch (IllegalArgumentException e) {log.error("", e);} // TODO
				catch (UnsupportedEncodingException e) {log.error("", e);} // TODO
			// CONDIZIONE DI FILTRO
			Condition condition = new Condition();
			if (!join.isEmpty())
				condition.addCondition(join, oF.getLogicalOperator());
			if (uEntity.getDcRel() != null)
				try
				{
					condition.addCondition(Condition.createFromXml(new String(uEntity.getDcRel().getCondition(), UTF8_XML_ENCODING), sqlGen), oF.getLogicalOperator());
				}
				catch (UnsupportedEncodingException e) {log.error("", e);} // TODO
			if (!condition.isEmpty())
				result.setWhereCondition(condition);
		}
		return result;
	}

	public static Query getQueryFromDvUserEntity(UserEntityBean uEntity, SqlGenerator sqlGen, MetadataController controller) 
	throws QueryBuilderException, Throwable
	{
		@SuppressWarnings("unused") ConditionFactory cF = sqlGen.getConditionFactory();
		OperatorFactory oF = sqlGen.getOperatorFactory();
		@SuppressWarnings("unused") ExpressionFactory eF = sqlGen.getExpressionFactory();
		Query result = null;
		if (uEntity != null)
		{
			result = sqlGen.getQuery();
			
			if (!isEmpty(uEntity.getPredicate()))
				result.setPredicate(sqlGen.getExpressionFactory().getPredicate(uEntity.getPredicate()));
			else
				result.setPredicate(sqlGen.getExpressionFactory().getPredicate());
			
			// AGGIUNGO LE TABELLE DELLA FROM //////////////////////////////////////////////////////
			for (DcEntityBean item : uEntity.getFromList()){
				if(item instanceof DcSchemaEntityBean ){
					result.addFromTable(new TableExpression(item.getId(), item.getSqlName(), item.getDesc(), item.getAlias()));
				}else{
					DvUserEntity dvUserEntity =  controller.getDvUserEntity(item.getId());
					Query fromTable = getQueryFromDvUserEntity(dvUserEntity, sqlGen);
					fromTable.setAlias(item.getAlias());
					fromTable.setExpression(item.getSqlName());
					result.addFromTable(fromTable);
				}
			}
			
			// ORA AGGIUNGO I CAMPI IN SELECT //////////////////////////////////////////////////////
			for (UserEntityColumnBean item : uEntity.getSelectList())
			{
				ValueExpression exp = item.getColumn().getValue().clone();
				for (UserEntityColumnBeanAppendedValue item2 : item.getAppendedExpressions())
					exp.appendExpression(oF.getValueExpressionOperator(item2.getChainOperator()), item2.getValue().getValue());
				exp.setAlias(item.getAlias());
				result.addSelectExpression(exp);
				if (item.isGroupBy())
					result.getGroupByList().add(exp);
				if (item.isOrderBy())
					result.getOrderByList().add(exp);
			}

			// ED, INFINE, LE EVENTUALI RELAZIONI E CONDIZIONE /////////////////////////////////////
			
			// RELAZIONI (JOINS)
			Condition join = new Condition();
			for (JoinSpec item : uEntity.getJoins())
				join.mergeCondition(item.getJoinCondition());

			// CONDIZIONE DI FILTRO
			Condition condition = new Condition();
			if (!join.isEmpty())
				condition.addCondition(join, oF.getLogicalOperator());
			if (uEntity.getCondition() != null && !uEntity.getCondition().isEmpty())
				condition.addCondition(uEntity.getCondition(), oF.getLogicalOperator());

			if (!condition.isEmpty())
				result.setWhereCondition(condition);
		}
		return result;
	}
}
