package it.webred.diogene.querybuilder.beans;

import java.text.MessageFormat;
import java.util.Date;

public class DvUserEntityBean extends DcEntityBean
{
	public static String USER_ENTITY_ALIAS_KEY = "THE_{0}_USER_ENTITY";
	
	private static final long serialVersionUID = 8971536235372869371L;

	public DvUserEntityBean() {}
	public DvUserEntityBean(Long id, String name, String desc, Date dtMod )
	{
		this();
		setId(id);
		setName(name);
		setDesc(desc);
		setDtMod(dtMod);
	}
	
	private Object getOrdinalSuffix(Object ordinal)
	{
		String result = "" + ordinal;
		if (new Long(1).equals(ordinal))
			result += "ST";
		else if (new Long(2).equals(ordinal))
			result += "ND";
		else
			result += "TH";
		return result;
	}
	
	@Override
	public String getSqlName()
	{
		return MessageFormat.format(USER_ENTITY_ALIAS_KEY, getOrdinalSuffix(getId()));
	}

	@Override
	protected void setSqlName(String sqlName) {}
	
	@Override
	public DvUserEntityBean clone() throws CloneNotSupportedException
	{
		return (DvUserEntityBean) super.clone();
	}
	
}
