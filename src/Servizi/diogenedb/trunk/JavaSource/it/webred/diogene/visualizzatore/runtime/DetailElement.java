package it.webred.diogene.visualizzatore.runtime;

import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.querybuilder.beans.DcColumnBean;

/**
 * @author Dan Petre
 * 
 */
public class DetailElement extends DcColumnBean
{
	private String htmlDATA;
	public DetailElement()
	{
		super();
	}

	public DetailElement(DcColumn dcc)
	{
		super(dcc);
	}

	public String getHtmlDATA()
	{
		return htmlDATA;
	}

	public void setHtmlDATA(String htmlDATA)
	{
		this.htmlDATA = htmlDATA;
	}

}
