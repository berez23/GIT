package it.webred.diogene.querybuilder.beans;

import java.util.Date;

public class DcSchemaEntityBean extends DcEntityBean 
{
	private static final long serialVersionUID = 1007643395280967223L;
	
	private String sqlName;

	public DcSchemaEntityBean() {}
	public DcSchemaEntityBean(Long id, String sqlName, String name, String desc, Date dtMod)
	{
		this();
		setId(id);
		setName(name);
		setSqlName(sqlName);
		setDesc(desc);
		setDtMod(dtMod);
	}

	@Override
	public String getSqlName()
	{
		return sqlName;
	}
	@Override
	public void setSqlName(String sqlName)
	{
		this.sqlName = sqlName;
	}
	@Override
	public DcSchemaEntityBean clone() throws CloneNotSupportedException
	{
		return (DcSchemaEntityBean) super.clone();
	}
}
