/**
 * 
 */
package it.webred.diogene.sqldiagram.impl;

import static java.util.Collections.synchronizedMap;
import it.webred.diogene.sqldiagram.Constant;
import it.webred.diogene.sqldiagram.Expression;
import it.webred.diogene.sqldiagram.Function;
import it.webred.diogene.sqldiagram.Predicate;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * TODO Scrivi descrizione
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class OraExpressionFactory implements it.webred.diogene.sqldiagram.ExpressionFactory {

	private static final String STRING_LITERAL_QUOTE = "'";
	private static final String DEFINITIONS_RESOURCE_FULL_QUALIFIED_NAME = "it.webred.diogene.sqldiagram.impl.Ora_definitions";
	private static final String HELP_RESOURCE_FULL_QUALIFIED_NAME = "it.webred.diogene.sqldiagram.impl.Ora_help";
	private static final Map<String,ValueExpression> expressions = synchronizedMap(new LinkedHashMap<String,ValueExpression>());
	private static final Map<String,Predicate> predicates = synchronizedMap(new LinkedHashMap<String,Predicate>());
	private static Predicate defaultPredicate;
	private ResourceBundle definitionResources = null;
	private ResourceBundle helpResources = null;
	 
	static
	{
		populateMap(new Constant("SYSDATE", "it.webred.diogene.sqldiagram.Constant.SYSDATE", ValueType.DATE));
		populateMap(new Constant("NULL", "it.webred.diogene.sqldiagram.Constant.NULL", ValueType.UNDEFINED));
		
		populateMap(new Function(
				"char2Date", 
				"TO_DATE",
				null,
				ValueType.DATE,
				false,
				false, 
				false, 
				ValueType.STRING, 
				ValueType.STRING));
		populateMap(new Function(
				"date2Char", 
				"TO_CHAR", 
				null,
				ValueType.STRING, 
				false,
				false, 
				false, 
				ValueType.DATE, 
				ValueType.STRING));
		populateMap(new Function(
				"decode", 
				"DECODE", 
				null,
				ValueType.ANY, 
				false,
				false, 
				true, 
				ValueType.ANY, 
				ValueType.ANY, 
				ValueType.ANY));
		populateMap(new Function(
				"upper", 
				"UPPER", 
				null,
				ValueType.STRING, 
				false,
				false, 
				false, 
				ValueType.STRING, 
				ValueType.STRING));
		populateMap(new Function(
				"extractXml", 
				"extract", 
				".getStringVal()",
				ValueType.STRING, 
				false,
				false, 
				false, 
				ValueType.ANY, 
				ValueType.STRING, 
				ValueType.STRING));		
		predicates.put("default", (defaultPredicate = new Predicate("", "default")));
		predicates.put("distinct", new Predicate("DISTINCT", "distinct"));
	} 
	
	/**
	 * @param exp
	 */
	private static void populateMap(ValueExpression exp) {
		expressions.put(exp.getExpression(),exp);
	}
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#getExpression(java.lang.String)
	 */
	public ValueExpression getExpression(String expression) {
		if (expressions.containsKey(expression))
			try	{return expressions.get(expression).clone();}
			catch (CloneNotSupportedException e) {return expressions.get(expression);}
		else
			return null;
	}
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#getExpressions()
	 */
	public List<ValueExpression> getExpressions()
	{
		List<ValueExpression> result = new ArrayList<ValueExpression>();
		if (expressions != null)
			for (String key : expressions.keySet())
				try
				{
					result.add(expressions.get(key).clone());
				}
				catch (CloneNotSupportedException e) {}
		return result;
	}
	
	
	public Predicate getPredicate()
	{
		return defaultPredicate;
	}

	public Predicate getPredicate(String expression)
	{
		if (predicates.containsKey(expression))
			return predicates.get(expression);
		else
			return null;
	}

	public List<Predicate> getPredicates()
	{
		return new ArrayList<Predicate>(predicates.values());
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#validate(java.lang.String, it.webred.diogene.sqldiagram.EnumsRepository.ValueType)
	 */
	public boolean validate(String input, ValueType type)
	{
		// A SWITCH-LIKE INSTRUCTION FOR NON-PRIMITIVE
		// VARS BY GIULIO QUARESIMA
		while (true)
		{
			if (type == null || input == null)
				break;
			
			if (type.equals(ValueType.NUMBER))
				return Pattern.compile("\\A[\\+\\-]\\d+([\\.,]\\d+)?\\z").matcher(input.trim()).matches();
			
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#quoteString(java.lang.String)
	 */
	public String quoteString(String input)
	{
		if (input != null)
			return input.replaceAll("\\Q" + STRING_LITERAL_QUOTE + "\\E","''");
		return null;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#getDefinition(it.webred.diogene.sqldiagram.ValueExpression)
	 */
	public String getDefinition(Expression key)
	{
		return getDefinition(key, Locale.getDefault());
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#getDefinition(it.webred.diogene.sqldiagram.ValueExpression, java.util.Locale)
	 */
	public String getDefinition(Expression key, Locale locale)
	{
		try {return getDefinitionResources(locale).getString(key.getClass().getCanonicalName() + "." + key.getExpression());}
		catch (MissingResourceException e) {return null;}
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#getDefinition(it.webred.diogene.sqldiagram.EnumsRepository.ValueType, java.util.Locale)
	 */
	public String getDefinition(ValueType key, Locale locale)
	{
		return getDefinitionResources(locale).getString(key.getResourceKey());
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#getDefinition(it.webred.diogene.sqldiagram.EnumsRepository.ValueType)
	 */
	public String getDefinition(ValueType key)
	{
		return getDefinition(key, Locale.getDefault());
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#getHelp(it.webred.diogene.sqldiagram.ValueExpression)
	 */
	public String getHelp(Expression key)
	{
		return getHelp(key, Locale.getDefault());
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ExpressionFactory#getHelp(it.webred.diogene.sqldiagram.ValueExpression, java.util.Locale)
	 */
	public String getHelp(Expression key, Locale locale)
	{
		try {return getHelpResources(locale).getString(key.getClass().getCanonicalName() + "." + key.getExpression());}
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
