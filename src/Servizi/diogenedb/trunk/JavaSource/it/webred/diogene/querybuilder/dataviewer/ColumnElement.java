package it.webred.diogene.querybuilder.dataviewer;

import it.webred.diogene.querybuilder.beans.DcColumnBean;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

public class ColumnElement implements Serializable, Comparable<ColumnElement>
{
	private static final long serialVersionUID = 6343922576593252838L;
	private String fieldName;
	private LinkedList<SelectItem> operatorsList;
	private String operator;
	private String value;
	private Date valueDate;
	private ValueType valueType;
	private String sqlName;
	private Long id; 
	
	
	public String getFieldName()
	{
		return fieldName;
	}
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}
	public String getOperator()
	{
		return operator;
	}
	public void setOperator(String operator)
	{
		this.operator = operator;
	}
	public LinkedList<SelectItem> getOperatorsList()
	{
		return operatorsList;
	}
	public void setOperatorsList(LinkedList<SelectItem> operatorsList)
	{
		this.operatorsList = operatorsList;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public ValueType getValueType()
	{
		return valueType;
	}
	public void setValueType(ValueType valueType)
	{
		this.valueType = valueType;
	}
	public String getSqlName()
	{
		return sqlName;
	}
	public void setSqlName(String sqlName)
	{
		this.sqlName = sqlName;
	}
	public boolean isDate(){
		return getValueType().equals(ValueType.DATE);
	}
	public Date getValueDate()
	{
		return valueDate;
	}
	public void setValueDate(Date valueDate)
	{
		this.valueDate = valueDate;
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public int compareTo(ColumnElement o)
	{

		if (getId() == null)
			if (o==null || o.getId() == null)
				return 0;
			else
				return -1;
		else if (o==null || o.getId() == null)
			return 1;		
		return getId().compareTo(o.getId());
	}
	
}
