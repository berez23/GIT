package it.webred.diogene.visualizzatore.runtime;

import java.util.LinkedHashSet;

/**
 * @author Dan Petre
 * 
 */
public class DetailTableElement extends Page
{

	private String title;
	private LinkedHashSet<LinkedHashSet<DetailElement>>	pageElement;
	
	
	public LinkedHashSet<LinkedHashSet<DetailElement>> getPageElement()
	{
		return pageElement;
	}
	public void setPageElement(LinkedHashSet<LinkedHashSet<DetailElement>> pageElement)
	{
		this.pageElement = pageElement;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}



}
