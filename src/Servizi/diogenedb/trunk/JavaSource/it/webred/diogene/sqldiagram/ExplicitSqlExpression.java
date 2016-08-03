package it.webred.diogene.sqldiagram;

import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class ExplicitSqlExpression extends ValueExpression
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2188283207808803972L;
	private String sql ;
	private ValueType valueType=ValueType.UNDEFINED;
	
	public ExplicitSqlExpression(String sql)
	{
		this.sql = sql;
	}

	public ExplicitSqlExpression(String sql, ValueType valueType)
	{
		this.sql = sql;
		this.valueType = valueType;
	}

	@Override
	public List<Element> getXml() throws UnsupportedOperationException
	{
		List<Element> result = new ArrayList<Element>();
		Element explicitSql = new Element("explicit_sql");
		explicitSql.setText(toString());
		result.add(explicitSql);
		return result;
	}

	@Override
	public String toString()
	{
		return sql != null ? sql : "";
	}

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public ValueType getValueType()
	{
		return valueType;
	}

	public void setValueType(ValueType valueType)
	{
		this.valueType = valueType;
	}

}
