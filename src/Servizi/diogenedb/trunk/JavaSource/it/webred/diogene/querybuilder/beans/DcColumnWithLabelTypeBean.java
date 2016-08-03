package it.webred.diogene.querybuilder.beans;

import it.webred.diogene.db.model.DcColumn;

import java.io.Serializable;

public class DcColumnWithLabelTypeBean extends DcColumnBean implements Serializable
{
	private static final long serialVersionUID = -4064244774503978361L;

	private Long labelType;
	
	public DcColumnWithLabelTypeBean() {}
	public DcColumnWithLabelTypeBean(DcColumn dcc) 
	{
		super(dcc);
	}
	public Long getLabelType()
	{
		return labelType;
	}
	public void setLabelType(Long labelType)
	{
		this.labelType = labelType;
	}
	
}
