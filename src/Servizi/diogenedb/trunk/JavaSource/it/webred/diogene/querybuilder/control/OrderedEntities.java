package it.webred.diogene.querybuilder.control;

import static it.webred.diogene.querybuilder.Constants.ALIAS;
import static it.webred.diogene.querybuilder.Constants.COLUMN;
import static it.webred.diogene.querybuilder.Constants.OUTER;
import static it.webred.diogene.querybuilder.Constants.TABLE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderedEntities implements Comparable
{
	// ORDINAMENTO DEI GRUPPI: --> --> --> --> --> --> -->  [1                  (2   )][3                  (4   )][5            ] [6                   (7   )]
	final Pattern separeTableAndAlias = Pattern.compile("\\A(\\Q" + TABLE + "\\E(\\d+))(\\Q" + ALIAS + "\\E(\\d+))(" + OUTER + ")?(\\Q" + COLUMN + "\\E(\\d+))?\\z");
	private String value;
	private int order;
	public OrderedEntities(String value,int order)
	{
		this.value=value;
		this.order=order;
	}
	public OrderedEntities(String value,int order,int aliasNum)
	{
		Matcher m=separeTableAndAlias.matcher(value);
		if(m.find())
			value=value.replaceAll(ALIAS + m.group(4),ALIAS + aliasNum);
		this.value=value;
		this.order=order;
	}
	public int compareTo(Object o)
	{
		int result=0;
		if(((OrderedEntities)o).getOrder()<this.order)
			result=1;
		else if(((OrderedEntities)o).getOrder()>this.order)
			result= -1;
		return result;
	}
	public String toString()
	{
		return this.value;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public int getOrder()
	{
		return order;
	}
	public void setOrder(int order)
	{
		this.order = order;
	}

}
