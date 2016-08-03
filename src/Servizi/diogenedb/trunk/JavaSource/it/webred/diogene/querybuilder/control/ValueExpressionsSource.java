package it.webred.diogene.querybuilder.control;

import it.webred.diogene.sqldiagram.ConditionFactory;
import it.webred.diogene.sqldiagram.ExpressionFactory;
import it.webred.diogene.sqldiagram.OperatorFactory;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.ValueExpressionOperator;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public interface ValueExpressionsSource
{
	/**
	 * @param key
	 * Vedi i criteri generali per
	 * le chiavi definiti in {@link EntitiesController}
	 * @return
	 * Una espressione corrispondente alla chiave
	 * passata, o <code>null</code> se non si
	 * trova una corrispondenza. 
	 */
	public ValueExpression getExpression(String key) throws UnsupportedOperationException;
	public SqlGenerator getSqlGenerator();
	public ExpressionFactory getEF();
	public ConditionFactory getCF();
	public OperatorFactory getOF();
	/**
	 * Restituisce la lista per popolare la
	 * <i>combo</i>dei "campi". I valori univoci
	 * da inserire nei <code>SelectItem</code>
	 * dovranno rispecchiare i criteri generali per
	 * le chiavi definiti in {@link EntitiesController}
	 * @return
	 */
	public List<SelectItem> getFields() throws UnsupportedOperationException;
	/**
	 * Restituisce la lista per popolare la
	 * <i>combo</i>dele funzioni. I valori univoci
	 * da inserire nei <code>SelectItem</code>
	 * dovranno rispecchiare i criteri generali per
	 * le chiavi definiti in {@link EntitiesController}
	 * @return
	 */
	public List<SelectItem> getFunctions() throws UnsupportedOperationException;
	public List<SelectItem> getChainOperators() throws UnsupportedOperationException;
}
