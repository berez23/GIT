package it.webred.diogene.visualizzatore.runtime;

import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.querybuilder.beans.DcColumnBean;

import java.util.LinkedHashMap;

/**
 * @author Dan Petre
 * 
 */
public class FilterElement extends DcColumnBean
{
	private LinkedHashMap<String, String>	operators;
	private LinkedHashMap<String, String>	listValues;

	public FilterElement()
	{
		super();
	}

	public FilterElement(DcColumn dcc)
	{
		super(dcc);
	}

	public LinkedHashMap<String, String> getListValues()
	{
		return listValues;
	}

	public void setListValues(LinkedHashMap<String, String> listValues)
	{
		this.listValues = listValues;
	}

	public LinkedHashMap<String, String> getOperators()
	{
		return operators;
	}

	public void setOperators(LinkedHashMap<String, String> operators)
	{
		this.operators = operators;
	}
}
