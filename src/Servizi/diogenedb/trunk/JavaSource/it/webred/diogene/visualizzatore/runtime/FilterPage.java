package it.webred.diogene.visualizzatore.runtime;

import java.util.LinkedHashSet;

/**
 * @author Dan Petre
 * 
 */
public class FilterPage extends Page
{

	private LinkedHashSet<FilterElement>	filterElement;

	public LinkedHashSet<FilterElement> getFilterElement()
	{
		return filterElement;
	}

	public void setFilterElement(LinkedHashSet<FilterElement> filterElement)
	{
		this.filterElement = filterElement;
	}

}
