package it.webred.diogene.sqldiagram.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.EnumsRepository;
import it.webred.diogene.sqldiagram.RelationalOperator;
import it.webred.diogene.sqldiagram.TableExpression;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;

public class OraRelationalOperator extends RelationalOperator
{
	
	public OraRelationalOperator(String name, String format, String formatString, String description, Class<? extends ValueExpression>... operandsTypes)
	{
		super(name, format, formatString, description, operandsTypes);
	}

	@Override
	public String toString()
	{
		Matcher m = Pattern.compile("\\Q" + getFormatString() + "\\E").matcher(getFormat());
		StringBuffer sb = new StringBuffer();
		try
		{
			ValueType value = null;
			int i = 0;
			while (m.find())
			{
				ValueExpression operand = operands[i++];
				if (value == null)
					value = operand.getValueType();
				else
					operand.setValueType(value);
				String replacement = operand.toString();
				if (operand instanceof Column && ((Column) operand).getTable() instanceof TableExpression && ((TableExpression)((Column) operand).getTable()).isOuter())
					replacement = replacement + " (+) ";
				m.appendReplacement(sb, replacement);
			}
			m.appendTail(sb);
			if (i < operandsTypes.length)
				throw new IllegalStateException();
		}
		catch (NullPointerException npe)
		{
			throw new IllegalStateException();
		}
		catch (IndexOutOfBoundsException ioobe)
		{
			throw new IllegalStateException();			
		}
		return sb.toString();
	}
	
}
