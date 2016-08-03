package it.webred.diogene.sqldiagram.impl;

import static it.webred.utils.StringUtils.CRLF;

import java.io.Serializable;

import it.webred.diogene.sqldiagram.Condition;
import it.webred.diogene.sqldiagram.LogicalOperator;
import it.webred.diogene.sqldiagram.RelationalOperator;
import it.webred.diogene.sqldiagram.Tuple;

public class OraCondition extends Condition implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3003199097227194604L;

	public OraCondition() {
		super();
	}
	
	@Override
	public String toString()
	{
		String result = "";
		//String CRLF = System.getProperty("line.separator");
		if (conditions != null)
		{
			boolean firstCycle = true;
			for (Tuple tuple : conditions)
			{
				if (!firstCycle)
					result += " " + tuple.getOp().toString();
				else
				{
					result += " " + tuple.getOp().showWhenFirst();
					firstCycle = false;
				}
				result += CRLF;
				
				if (tuple.getCond().isSimpleCondition())
					result += " " + tuple.getCond().toString();
				else
					result += " (" + tuple.getCond().toString() + ")";
				result += CRLF;
			}
		}		
		else if (isSimpleCondition())
			result += expressionOP.toString();
		
		return result;			
	}
}
