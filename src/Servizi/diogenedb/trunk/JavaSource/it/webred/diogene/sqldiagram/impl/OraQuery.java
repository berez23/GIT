package it.webred.diogene.sqldiagram.impl;

import static it.webred.utils.StringUtils.CRLF;
import static it.webred.utils.StringUtils.getTab;

import java.io.Serializable;
import java.util.Iterator;

import it.webred.diogene.sqldiagram.Query;
import it.webred.diogene.sqldiagram.Table;
import it.webred.diogene.sqldiagram.ValueExpression;

public class OraQuery extends Query implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4994773304472933705L;

	public OraQuery()
	{
		super();
	}
	
	@Override
	public String toString()
	{
		String result = "SELECT ";
		if (getPredicate() != null)
			result += getPredicate() + " ";
		result += CRLF;
		try
		{
			Iterator<ValueExpression> iter = selectList.iterator();
			ValueExpression exp = iter.next();
			result += getTab(3) + exp;
			if (exp.getAlias() != null)
				result += " " + exp.getAlias();
			while (iter.hasNext())
			{
				exp = iter.next();
				result += ", " + CRLF + getTab(3) + exp;
				if (exp.getAlias() != null)
					result += " " + exp.getAlias();
			}
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Una SELECT abbisogna di almeno un'espressione in select!");
		}
		result += CRLF + "FROM " + CRLF;
		try
		{
			Iterator<Table> iter = fromList.iterator();
			result += getTab(3) + getTableRepresentation(iter.next());
			while (iter.hasNext())
				result += ", " + CRLF + getTab(3) + getTableRepresentation(iter.next());
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Una SELECT abbisogna di almeno una tabella in from!", e);
		}
		if (where != null)
		{
			String whereStr = where.toString();
			whereStr = whereStr.replaceAll("\\Q"+CRLF+"\\E", CRLF + getTab(3));
			result += CRLF + "WHERE " + whereStr;
		}
		
		if (!getGroupByList().isEmpty())
		{
			result += CRLF + "GROUP BY ";
			int index = 0;
			for (ValueExpression item : getGroupByList())
			{
				try
				{
					ValueExpression exp = item.clone();
					exp.setAlias(null);
					if (index > 0) result += ",";
					result += CRLF + getTab(3) + exp;
					index++;
				}
				catch (CloneNotSupportedException e) {}
			}
		}
		
		if (!getOrderByList().isEmpty())
		{
			result += CRLF + "ORDER BY ";
			int index = 0;
			for (ValueExpression item : getOrderByList())
			{
				try
				{
					ValueExpression exp = item.clone();
					exp.setAlias(null);
					if (index > 0) result += ",";
					result += CRLF + getTab(3) + exp;
					index++;
				}
				catch (CloneNotSupportedException e) {}
			}
		}
		
		return result;
	}	
}
