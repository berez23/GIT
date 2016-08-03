package it.webred.diogene.sqldiagram.impl;

import static java.util.Collections.synchronizedMap;
import it.webred.diogene.sqldiagram.EnumsRepository;
import it.webred.diogene.sqldiagram.LogicalOperator;
import it.webred.diogene.sqldiagram.LogicalOperatorType;
import it.webred.diogene.sqldiagram.Membership;
import it.webred.diogene.sqldiagram.Operator;
import it.webred.diogene.sqldiagram.Query;
import it.webred.diogene.sqldiagram.RelationalOperator;
import it.webred.diogene.sqldiagram.RelationalOperatorType;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.ValueExpressionOperator;
import it.webred.diogene.sqldiagram.ValueExpressionOperatorType;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;


/**
 * TODO Scrivi descrizione
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.2 $ $Date: 2007/11/22 15:59:57 $
 */
public class OraOperatorFactory implements it.webred.diogene.sqldiagram.OperatorFactory {

	private static final String DEFINITIONS_RESOURCE_FULL_QUALIFIED_NAME = "it.webred.diogene.sqldiagram.impl.Ora_definitions";
	private static final String HELP_RESOURCE_FULL_QUALIFIED_NAME = "it.webred.diogene.sqldiagram.impl.Ora_help";
	private static final Map<String,RelationalOperator> operators = synchronizedMap(new LinkedHashMap<String,RelationalOperator>());
	private static final Map<String,LogicalOperator> logicalOperators = synchronizedMap(new LinkedHashMap<String,LogicalOperator>());
	private static final Map<String,ValueExpressionOperator> valueExpressionOperators = synchronizedMap(new LinkedHashMap<String,ValueExpressionOperator>());
	private static LogicalOperator defaultLogicalOperator;
	private ResourceBundle definitionResources = null;
	private ResourceBundle helpResources = null;

	static 
	{
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.EQUAL , "% = %", "%", "it.webred.diogene.sqldiagram.RelationalOperator.equal", ValueExpression.class, ValueExpression.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.NOTEQUAL, "% <> %", "%", "it.webred.diogene.sqldiagram.RelationalOperator.equal", ValueExpression.class, ValueExpression.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.BETWEEN, "% BETWEEN % AND %", "%", "it.webred.diogene.sqldiagram.RelationalOperator.between", ValueExpression.class, ValueExpression.class, ValueExpression.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.NOTBETWEEN, "% NOT BETWEEN % AND %", "%","it.webred.diogene.sqldiagram.RelationalOperator.notBetween", ValueExpression.class, ValueExpression.class, ValueExpression.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.GREATER, "% > %", "%", "it.webred.diogene.sqldiagram.RelationalOperator.greater", ValueExpression.class, ValueExpression.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.LESSER, "% < %", "%", "it.webred.diogene.sqldiagram.RelationalOperator.less", ValueExpression.class, ValueExpression.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.LIKE, "% LIKE %", "%", "it.webred.diogene.sqldiagram.RelationalOperator.like", ValueExpression.class, ValueExpression.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.ISNULL, "% IS NULL", "%", "it.webred.diogene.sqldiagram.RelationalOperator.isNull", ValueExpression.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.ISNOTNULL, "% IS NOT NULL", "%", "it.webred.diogene.sqldiagram.RelationalOperator.isNotNull", ValueExpression.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.EXISTS, "EXISTS (%)", "%", "it.webred.diogene.sqldiagram.RelationalOperator.exists", Query.class));
		populateExpOperators(new OraRelationalOperator(RelationalOperatorType.IN, "% IN (%)", "%", "it.webred.diogene.sqldiagram.RelationalOperator.in", ValueExpression.class, Membership.class));

		populateLogOperators(defaultLogicalOperator = new LogicalOperator(LogicalOperatorType.AND, "AND", "", "Congiunzione Logica"));
		populateLogOperators(new LogicalOperator(LogicalOperatorType.OR, "OR", "", "Disgiunzione Logica"));
		populateLogOperators(new LogicalOperator(LogicalOperatorType.ANDNOT, "AND NOT", "NOT", "Congiunzione Logica"));
		populateLogOperators(new LogicalOperator(LogicalOperatorType.ORNOT, "OR NOT", "NOT", "Disgiunzione Logica"));
		
		populateValueExpressionOperators(new ValueExpressionOperator(ValueExpressionOperatorType.STRINGCONCAT, " || ", ValueType.STRING));
		populateValueExpressionOperators(new ValueExpressionOperator(ValueExpressionOperatorType.PLUS, " + ", ValueType.NUMBER, ValueType.DATE));
		populateValueExpressionOperators(new ValueExpressionOperator(ValueExpressionOperatorType.MINUS, " + ", ValueType.NUMBER, ValueType.DATE));
		populateValueExpressionOperators(new ValueExpressionOperator(ValueExpressionOperatorType.TIMES, " + ", ValueType.NUMBER, ValueType.DATE));
		populateValueExpressionOperators(new ValueExpressionOperator(ValueExpressionOperatorType.DIVIDE, " + ", ValueType.NUMBER, ValueType.DATE));
	}

	private static void populateExpOperators(RelationalOperator op) 
	{
		operators.put(op.getName(),op);
	}
	private static void populateLogOperators(LogicalOperator op) 
	{
		logicalOperators.put(op.getName(),op);
	}
	private static void populateValueExpressionOperators(ValueExpressionOperator op)
	{
		valueExpressionOperators.put(op.getName(), op);
	}
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getOperator(java.lang.String)
	 */
	public RelationalOperator getOperator(String name) 
	{
		if (operators.containsKey(name))
		{
			try	{return operators.get(name).clone();}
			catch (CloneNotSupportedException e) {return operators.get(name);}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getLogicalOperator()
	 */
	public LogicalOperator getLogicalOperator()
	{
		if (defaultLogicalOperator != null)
			try
			{
				return defaultLogicalOperator.clone();
			}
			catch (CloneNotSupportedException e) {}
		return null;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getLogicalOperator(java.lang.String)
	 */
	public LogicalOperator getLogicalOperator(String name) {
		try	{return logicalOperators.get(name).clone();}
		catch (CloneNotSupportedException e) {return null;}
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getOperators()
	 */
	@SuppressWarnings("unchecked")
	public List<RelationalOperator> getRelationalOperators()  
	{
		List<RelationalOperator> result = new ArrayList();
		for (String key : operators.keySet())
		{
			try
			{
				result.add(operators.get(key).clone());
			}
			catch (IllegalStateException e) {}
			catch (CloneNotSupportedException e) {}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getLogicalOperators()
	 */
	@SuppressWarnings("unchecked")
	public List<LogicalOperator> getLogicalOperators()  
	{
		List<LogicalOperator> result = new ArrayList();
		for (String key : logicalOperators.keySet())
		{
			try
			{
				result.add(logicalOperators.get(key).clone());
			}
			catch (CloneNotSupportedException e) {}
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getValueExpressionOperator(java.lang.String)
	 */
	public ValueExpressionOperator getValueExpressionOperator(String name)
	{
		if (valueExpressionOperators != null)
			try
			{
				return valueExpressionOperators.get(name).clone();
			}
			catch (CloneNotSupportedException e) {}
		return null;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getValueExpressionOperators()
	 */
	public List<ValueExpressionOperator> getValueExpressionOperators()
	{
		List<ValueExpressionOperator> result = new ArrayList<ValueExpressionOperator>();
		for (String key : valueExpressionOperators.keySet())
		{
			try
			{
				result.add(valueExpressionOperators.get(key).clone());
			}
			catch (CloneNotSupportedException e) {}
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getDefinition(it.webred.diogene.sqldiagram.Operator)
	 */
	public String getDefinition(Operator key)
	{
		return getDefinition(key, Locale.getDefault());
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getDefinition(it.webred.diogene.sqldiagram.Operator, java.util.Locale)
	 */
	public String getDefinition(Operator key, Locale locale)
	{
		try {return getDefinitionResources(locale).getString(key.getClass().getCanonicalName() + "." + key.getName());}
		catch (MissingResourceException e) {return null;}
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getHelp(it.webred.diogene.sqldiagram.Operator)
	 */
	public String getHelp(Operator key)
	{
		return getHelp(key, Locale.getDefault());
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.OperatorFactory#getHelp(it.webred.diogene.sqldiagram.Operator, java.util.Locale)
	 */
	public String getHelp(Operator key, Locale locale)
	{
		try {return getHelpResources(locale).getString(key.getClass().getCanonicalName() + "." + key.getName());}
		catch (MissingResourceException e) {return null;}
	}

	/**
	 * @param locale
	 * @return
	 */
	private ResourceBundle getDefinitionResources(Locale locale)
	{
		if (definitionResources == null)
		{
			if (locale != null)
				definitionResources = ResourceBundle.getBundle(DEFINITIONS_RESOURCE_FULL_QUALIFIED_NAME, locale);
			else
				definitionResources = ResourceBundle.getBundle(DEFINITIONS_RESOURCE_FULL_QUALIFIED_NAME);
		}
		else if (locale != null && !definitionResources.getLocale().equals(locale))
			definitionResources = ResourceBundle.getBundle(DEFINITIONS_RESOURCE_FULL_QUALIFIED_NAME, locale);

		return definitionResources;
	}

	/**
	 * @param locale
	 * @return
	 */
	private ResourceBundle getHelpResources(Locale locale)
	{
		if (helpResources == null)
		{
			if (locale != null)
				helpResources = ResourceBundle.getBundle(HELP_RESOURCE_FULL_QUALIFIED_NAME, locale);
			else
				helpResources = ResourceBundle.getBundle(HELP_RESOURCE_FULL_QUALIFIED_NAME);
		}
		else if (locale != null && !helpResources.getLocale().equals(locale))
			helpResources = ResourceBundle.getBundle(HELP_RESOURCE_FULL_QUALIFIED_NAME, locale);

		return helpResources;
	}
}

