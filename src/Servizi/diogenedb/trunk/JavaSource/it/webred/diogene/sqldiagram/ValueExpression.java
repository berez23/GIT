package it.webred.diogene.sqldiagram;

import static it.webred.utils.StringUtils.isEmpty;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import it.webred.utils.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Rappresenta una qualsiasi espressione di valore. Le espressioni di valore
 * sono quelle espressioni che possono essere passate come argomenti
 * degli operatori relazionali come =, BETWEEN etc.<br /> 
 * e possono essere passate come argomenti di SELECT (con l'eccezione della classe
 * {@link it.webred.diogene.sqldiagram.Membership Membership} e delle sue figlie.
 * Espressioni di valore, contenute all'interno di una {@link it.webred.diogene.sqldiagram.MembersList},
 * sono anche gli argomenti di funzione, vedi {@link it.webred.diogene.sqldiagram.Function#setArguments(MembersList)}
 * Una ValueExpression può anche contenere la concatenazione di più
 * ValueExpression separate da operatori di tipo
 * {@link it.webred.diogene.sqldiagram.ValueExpressionOperator ValueExpressionOperator}
 * @author Giulio Quaresima
 * @version $Revision: 1.7 $ $Date: 2008/01/24 14:24:42 $
 */
public abstract class ValueExpression extends Expression implements Serializable, XMLRepresentable, Cloneable
{
	private static final Logger log = Logger.getLogger(ValueExpression.class);

	private static final long serialVersionUID = 1L;

	private static final String RESOURCE_FULL_QUALIFIED_NAME = "it.webred.diogene.querybuilder.labels";

	private EnumsRepository.ValueType valueType = EnumsRepository.ValueType.UNDEFINED;
	protected List<AppendedValueExpression> appendedValueExpressions = null;
	private String alias;
	
	protected ValueExpression() {
		super();
	}
	/**
	*	@param exp vedi {@link Expression#setExpression(String)}
	*	@param des vedi {@link Expression#setDescription(String)}
	*/
	protected ValueExpression(String exp, String des) {
		super(exp, des);
	}
	
	/**
	 * 
	 * @param exp vedi {@link Expression#setExpression(String)}
	 * @param des vedi {@link Expression#setDescription(String)}
	 * @param valueType vedi {@link ValueExpression#setValueType(ValueType)}
	 */
	protected ValueExpression(String exp, String des, ValueType valueType) {
		super(exp, des);
		setValueType(valueType);
	}
	
	/**
	 * @return Il tipo del valore di questa espression
	 * @see it.webred.diogene.sqldiagram.EnumsRepository.ValueType
	 */
	public ValueType getValueType()
	{
		return valueType;
	}
	
	/**
	 * @param valueType
	 * Il tipo del valore di questa espression
	 * @see it.webred.diogene.sqldiagram.EnumsRepository.ValueType
	 */
	public void setValueType(ValueType valueType)
	{
		this.valueType = valueType;
	}
	
	/**
	 * Concatena a questa espressione un'altra, mediante un operatore
	 * di concatenazione, quali possono essere, ad es., 
	 * +, - per i valori numerici SQL e l'operatore
	 * di concatenazione delle stringhe per i valori di tipo stringa
	 * @param operator
	 * L'operatore di concatenazione
	 * @param expression
	 * L'espressione da concatenare a questa espressione
	 */
	public void appendExpression(ValueExpressionOperator operator, ValueExpression expression)
	{
		// TODO: controllare eventualmente la coerenza tra il ValueType
		// dell'operatore e il ValueType di questa espressione
		getAppendedValueExpressions().add(new AppendedValueExpression(operator, expression));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return super.expression + appendedToString();
	}
	
	/**
	 * @return
	 */
	public String appendedToString()
	{
		String result = "";
		for (AppendedValueExpression item : getAppendedValueExpressions())
			result += item.toString();
		return result;		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ValueExpression clone() throws CloneNotSupportedException
	{
		ValueExpression clone = (ValueExpression) super.clone();
		if (!getAppendedValueExpressions().isEmpty())
		{
			clone.getAppendedValueExpressions().clear();
			for (AppendedValueExpression item : getAppendedValueExpressions())
				clone.getAppendedValueExpressions().add((item != null) ? (item.clone()) : (null));
		}
		return clone;
	}
	
	public static String retrieveHint(ValueExpression ve){
		ResourceBundle labelsResources = ResourceBundle.getBundle(RESOURCE_FULL_QUALIFIED_NAME);
		String hint=labelsResources.getString("entities.userEntity.fieldsTable.insertValue.button");
		if (ve==null)return hint;
		if (ve instanceof Column) 
		{
			Column col = (Column) ve;
			String desc=((TableExpression)col.getTable()).getDescription();
			desc=  StringUtils.isEmpty( desc) ?"":"["+desc+"]."; 
			hint = "Colonna :"+desc+"["+col.getDescription()+"] SQL: " + col.toString();
		}
		else if (ve instanceof Constant)
		{
			Constant cos = (Constant) ve;
			hint = "Constante :" + cos.toString();
		}
		else if (ve instanceof Function)
		{
			Function fun = (Function) ve;
			hint = "Funzione :" + fun.toString();
		}
		else if (ve instanceof LiteralExpression)
		{
			LiteralExpression lit = (LiteralExpression) ve;
			hint = "Valore Letterale :" + lit.toString();
		}
		else if (ve instanceof ExplicitSqlExpression)
		{
			ExplicitSqlExpression expl = (ExplicitSqlExpression) ve;
			hint = "Espressione SQL :" + expl.toString();
		}
		else if (ve instanceof Membership)
		{
			Membership mem = (Membership) ve;
			hint = "Espressione SQL :" + mem.toString();
		}
		return hint;
	}
	/**
	 * @param expression
	 * @return
	 */
	public static Element getOperandXml(ValueExpression expression)
	{
		Element operand = new Element("operand");
		for (Element item : expression.getXml())
			operand.addContent(item);
		operand.setAttribute("data_type", ValueType.getXmlDataTypeAttribute(expression.getValueType()));
		return operand;
	}
	/**
	 * @param expression
	 * @return
	 */
	public static String getOperandXmlString(ValueExpression expression)
	{
		StringWriter result = new StringWriter();
		XMLOutputter out = new XMLOutputter();
		// out.setIndentSize(3); // DEPRECATO. SERVIVA SOLO PER MOTIVI ESTETICI
//		out.setLineSeparator(CRLF);
//		out.setNewlines(true);
		out.setFormat(Format.getPrettyFormat());
		try
		{
			out.output(getOperandXml(expression), result);
		}
		catch (IOException e) {}
		return result.toString();		
	}
	
	/**
	 * @param sql
	 * @param valueType
	 * @return
	 */
	public static ValueExpression createFromExplicitSql(final String sql, ValueType valueType)
	{
		ValueExpression result = new ExplicitSqlExpression(sql,valueType);
		result.setValueType(valueType);
		return result;
	}
	/**
	 * @param sql
	 * @return
	 */
	public static ValueExpression createFromExplicitSql(final String sql)
	{
		return createFromExplicitSql(sql, ValueType.UNDEFINED);
	}
	
	/**
	 * @param xml
	 * @param oF
	 * @param eF
	 * @return
	 */
	public static ValueExpression createFromXml(String xml, SqlGenerator sqlGenerator)
	{
		SAXBuilder builder = new SAXBuilder();
		Document xmlDoc = null;
		try {xmlDoc = builder.build(new StringReader(xml));}
		catch (JDOMException e) {throw new IllegalArgumentException("XML malformato o nullo");}
		catch (IOException e) {}
		return createFromXml(xmlDoc.getRootElement(), sqlGenerator);
	}

	/**
	 * @param xml
	 * @param oF
	 * @param eF
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ValueExpression createFromXml(Element xml, SqlGenerator sqlGenerator)
	{
		if (!"operand".equals(xml.getName()))
			throw new IllegalArgumentException("Il root element deve essere <operand>");
		
		ValueExpression result = null;
		Iterator<? extends Element> iter = xml.getChildren().iterator();
		String dataType = null;
		ValueType valueType = null;
		if (xml.getAttribute("data_type") != null)
			dataType = xml.getAttribute("data_type").getValue();
		if (!isEmpty(dataType))
			valueType = ValueType.getValueType(dataType);
			
		if (iter.hasNext())
		{
			result = createFromXml(iter.next(), valueType, sqlGenerator);
			appendExpressions(result, valueType, iter, sqlGenerator);
			if (!isEmpty(dataType))
				result.setValueType(ValueType.getValueType(dataType));
		}
		result.setValueType(valueType);
		return result;
	}
	
	private static ValueExpression createFromXml(Element firstElement, ValueType valueType, SqlGenerator sqlGenerator)
	{
		@SuppressWarnings("unused") ConditionFactory cF = null;
		@SuppressWarnings("unused") OperatorFactory oF = null;
		ExpressionFactory eF = null;
		try
		{
			cF = sqlGenerator.getConditionFactory();
			oF = sqlGenerator.getOperatorFactory();
			eF = sqlGenerator.getExpressionFactory();			
		} 
		catch (Exception e) {log.error("", e);}

		ValueExpression result = null;

		if ("column".equals(firstElement.getName()))
		{
			TableExpression table = new TableExpression(
					Long.parseLong(firstElement.getChild("table").getChildText("pk_id")), 
					firstElement.getChild("table").getChildText("name"), 
					firstElement.getChild("table").getChildText("description"), 
					firstElement.getChild("table").getChildText("alias")
					);
			if ("true".equals(firstElement.getChild("table").getAttributeValue("outer")))
				table.setOuter(true);
			else
				table.setOuter(false);
			result = new Column(
					firstElement.getChild("field").getChildText("name"), 
					firstElement.getChild("field").getChildText("description"),
					firstElement.getChild("field").getChildText("alias"),
					table,
					null
					);
		}
		else if ("literal".equals(firstElement.getName()))
			result = new LiteralExpression(firstElement.getChild("value").getText(), firstElement.getChildText("description"));
		else if ("constant".equals(firstElement.getName()))
			result = eF.getExpression(firstElement.getChildText("name"));
		else if ("function".equals(firstElement.getName()))
		{
			result = eF.getExpression(firstElement.getChildText("name"));
			Element operandsList = firstElement.getChild("operands_list");
			MembersList arguments = new MembersList();
			arguments.setDescription(operandsList.getChildText("description"));
			for (Element item : (Iterable<? extends Element>) operandsList.getChildren("operand"))
				arguments.add(ValueExpression.createFromXml(item, sqlGenerator));
			((Function) result).setArguments(arguments);
		}
		else if ("operands_list".equals(firstElement.getName()))
		{
			result = new MembersList();
			result.setDescription(firstElement.getChildText("description"));
			for (Element operand : (Iterable<? extends Element>) firstElement.getChildren("operand"))
			{
				((MembersList) result).add(ValueExpression.createFromXml(operand, sqlGenerator));
			}
		}
		else if ("explicit_sql".equals(firstElement.getName()))
			result = ValueExpression.createFromExplicitSql(firstElement.getText());
		else
			throw new IllegalArgumentException();

		result.setUIKey(firstElement.getAttributeValue("ui_key"));
		result.setValueType(valueType);

		return result;
	}
	
	/**
	 * @param exp
	 * @param iter
	 * @param oF
	 * @param eF
	 */
	protected static void appendExpressions(ValueExpression exp, ValueType valueType, Iterator<? extends Element> iter, SqlGenerator sqlGenerator)
	{
		@SuppressWarnings("unused") ConditionFactory cF = null;
		OperatorFactory oF = null;
		@SuppressWarnings("unused") ExpressionFactory eF = null;
		try
		{
			cF = sqlGenerator.getConditionFactory();
			oF = sqlGenerator.getOperatorFactory();
			eF = sqlGenerator.getExpressionFactory();			
		} 
		catch (Exception e) {log.error("", e);}
		while (iter.hasNext())
		{
			Element chainOperator = iter.next();
			Element expr = null;
			if (iter.hasNext())
				expr = iter.next();
			else
				throw new IllegalArgumentException("Il chain operator non e' seguito da nulla");
			exp.appendExpression(
					oF.getValueExpressionOperator(chainOperator.getChildText("name")), 
					ValueExpression.createFromXml(expr, valueType, sqlGenerator));
		}		
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	public abstract List<Element> getXml() throws UnsupportedOperationException;
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Element> getAppendedXml()
	{
		List<Element> result = new ArrayList<Element>();
		for (AppendedValueExpression item : getAppendedValueExpressions())
			result.addAll(item.getXml());
		return result;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getStringXml()
	 */
	public String getStringXml()
	{
		StringWriter result = new StringWriter();
		XMLOutputter out = new XMLOutputter();
		// out.setIndentSize(3); // DEPRECATO. SERVIVA SOLO PER MOTIVI ESTETICI
//		out.setLineSeparator(CRLF);
//		out.setNewlines(true);
		out.setFormat(Format.getPrettyFormat());
		try
		{
			out.output(getXml(), result);
		}
		catch (IOException e) {}
		return result.toString();
	}

	/**
	 * TODO Scrivi descrizione
	 * @author Giulio Quaresima
	 * @version $Revision: 1.7 $ $Date: 2008/01/24 14:24:42 $
	 */
	public class AppendedValueExpression implements Serializable, XMLRepresentable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3021403623986251050L;
		/**
		 * 
		 */
		private ValueExpressionOperator operator;
		private ValueExpression expression;
		
		private AppendedValueExpression(ValueExpressionOperator operator, ValueExpression expression)
		{
			this.operator = operator;
			this.expression = expression;
		}
		@Override
		public String toString()
		{
			return operator.toString() + expression.toString();
		}
		@Override
		public AppendedValueExpression clone() throws CloneNotSupportedException
		{
			return new AppendedValueExpression(operator.clone(), expression.clone()); 
		}
		public List<Element> getXml()
		{
			List<Element> result = new ArrayList<Element>();
			result.addAll(operator.getXml());
			result.addAll(expression.getXml());
			return result;
		}
		public String getStringXml()
		{
			StringWriter result = new StringWriter();
			XMLOutputter out = new XMLOutputter();
			// out.setIndentSize(3); // DEPRECATO. SERVIVA SOLO PER MOTIVI ESTETICI
//			out.setLineSeparator(CRLF);
//			out.setNewlines(true);
			out.setFormat(Format.getPrettyFormat());
			try
			{
				out.output(getXml(), result);
			}
			catch (IOException e) {}
			return result.toString();
		}
		public ValueExpression getExpression()
		{
			return expression;
		}
		public void setExpression(ValueExpression expression)
		{
			this.expression = expression;
		}
		public ValueExpressionOperator getOperator()
		{
			return operator;
		}
		public void setOperator(ValueExpressionOperator operator)
		{
			this.operator = operator;
		}
	}

	/**
	 * @return
	 */
	public String getAlias()
	{
		return alias;
	}
	/**
	 * @param alias
	 */
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	public List<AppendedValueExpression> getAppendedValueExpressions()
	{
		if (appendedValueExpressions == null)
			appendedValueExpressions = new ArrayList<AppendedValueExpression>();
		return appendedValueExpressions;
	}
}
