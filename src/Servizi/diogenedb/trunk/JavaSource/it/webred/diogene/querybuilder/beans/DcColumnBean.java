package it.webred.diogene.querybuilder.beans;

import it.webred.diogene.db.model.DcColumn;

import java.io.Serializable;

public class DcColumnBean implements Serializable
{
	private static final long serialVersionUID = -4064244774503978361L;

	private Long id;
	private String
		xml,
		description,
		alias,
		javaType;
	boolean primaryKey;
	
	public DcColumnBean() {}
	public DcColumnBean(DcColumn dcc) 
	{
		setId(dcc.getId());
		setXml(new String(dcc.getDcExpression().getExpression()));
		setAlias(dcc.getDcExpression().getAlias());
		setDescription(dcc.getLongDesc());
		setJavaType(dcc.getDcExpression().getColType());
	}

	public String getJavaType()
	{
		return javaType;
	}
	public void setJavaType(String javaType)
	{
		this.javaType = javaType;
	}
	public String getAlias()
	{
		return alias;
	}
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getXml()
	{
		return xml;
	}
	public void setXml(String xml)
	{
		this.xml = xml;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public boolean isPrimaryKey()
	{
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey)
	{
		this.primaryKey = primaryKey;
	}
}
