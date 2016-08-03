package it.webred.diogene.visualizzatore.runtime;

import java.util.LinkedHashSet;

/**
 * @author Dan Petre
 * 
 */
public class DetailPage extends Page
{

	private LinkedHashSet<DetailTableElement>	pageElement;
	private PageLink	pageLink;

	public LinkedHashSet<DetailTableElement> getPageElement()
	{
		return pageElement;
	}

	public void setPageElement(LinkedHashSet<DetailTableElement> pageElement)
	{
		this.pageElement = pageElement;
	}

	public PageLink getPageLink()
	{
		return pageLink;
	}

	public void setPageLink(PageLink pageLink)
	{
		this.pageLink = pageLink;
	}



}
