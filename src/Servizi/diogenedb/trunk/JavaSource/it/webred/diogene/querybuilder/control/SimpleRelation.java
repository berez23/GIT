package it.webred.diogene.querybuilder.control;

import it.webred.diogene.querybuilder.beans.DcEntityBean;

public class SimpleRelation
{
	int aliasRel=0;
	String identifier;
	String nameRelation;
	DcEntityBean formerEntity;
	DcEntityBean latterEntity;
	boolean editable=false;
	public DcEntityBean getFormerEntity()
	{
		return formerEntity;
	}
	public void setFormerEntity(DcEntityBean formerEntity)
	{
		this.formerEntity = formerEntity;
	}
	public String getIdentifier()
	{
		return identifier;
	}
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}
	public String getNameRelation()
	{
		return nameRelation;
	}
	public void setNameRelation(String nameRelation)
	{
		this.nameRelation = nameRelation;
	}
	public boolean isEditable()
	{
		return editable;
	}
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
	public DcEntityBean getLatterEntity()
	{
		return latterEntity;
	}
	public void setLatterEntity(DcEntityBean latterEntity)
	{
		this.latterEntity = latterEntity;
	}
	public int getAliasRel()
	{
		return aliasRel;
	}
	public void setAliasRel(int aliasRel)
	{
		this.aliasRel = aliasRel;
	}

}
