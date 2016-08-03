package it.webred.diogene.visualizzatore.runtime;

import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.querybuilder.beans.DcColumnBean;

/**
 * @author Dan Petre
 * 
 */
public class ListElement extends DcColumnBean
{
	private String		linkFunction;
	private String[]	linkParameterAlias;
	private String		linkText;
	private String		linkImgUrl;

	public ListElement()
	{
		super();
	}

	public ListElement(DcColumn dcc)
	{
		super(dcc);
	}

	public String getLinkFunction()
	{
		return linkFunction;
	}

	public void setLinkFunction(String linkFunction)
	{
		this.linkFunction = linkFunction;
	}

	public String getLinkImgUrl()
	{
		return linkImgUrl;
	}

	public void setLinkImgUrl(String linkImgUrl)
	{
		this.linkImgUrl = linkImgUrl;
	}

	public String[] getLinkParameterAlias()
	{
		return linkParameterAlias;
	}

	public void setLinkParameterAlias(String[] linkParameterAlias)
	{
		this.linkParameterAlias = linkParameterAlias;
	}

	public String getLinkText()
	{
		return linkText;
	}

	public void setLinkText(String linkText)
	{
		this.linkText = linkText;
	}
}
