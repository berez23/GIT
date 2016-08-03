package it.webred.diogene.querybuilder.control;

import java.io.Serializable;

import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.Constant;
import it.webred.diogene.sqldiagram.ExplicitSqlExpression;
import it.webred.diogene.sqldiagram.Function;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.Membership;
import it.webred.diogene.sqldiagram.TableExpression;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.utils.StringUtils;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

public class UserEntityColumnBeanAppendedValue implements Serializable, Switches {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8790889845768227308L;
	private ObjectEditor<ValueExpression> value;
	private String chainOperator;
	private boolean editable;
	
	private UserEntityColumnBean parent;
	
	private UserEntityColumnBeanAppendedValue() {super();}
	UserEntityColumnBeanAppendedValue(ObjectEditor<ValueExpression> value)
	{
		this();
		setValue(value);
	}
	UserEntityColumnBeanAppendedValue(ObjectEditor<ValueExpression> value, String chainOperator)
	{
		this();
		setValue(value);
		setChainOperator(chainOperator);
	}
	
	public void edit(ActionEvent e)
	{
		this.getValue().setEditable(isEditable());
		getValue().edit();
		parent.getParent().setEditingValue(this.getValue());
		if (!parent.getParent().isModifiedByUser())
			parent.getParent().setModifiedByUser(true);
	}
	public void deleteMe(ActionEvent e)
	{
		parent.getAppendedExpressions().remove(this);
		parent.getParent().setEditingValue(null);
		if (!parent.getParent().isModifiedByUser())
			parent.getParent().setModifiedByUser(true);
	}
	public void operatorModified(ValueChangeEvent e)
	{
		if (!parent.getParent().isModifiedByUser())
			parent.getParent().setModifiedByUser(true);
	}
	public ObjectEditor<ValueExpression> getValue()
	{
		return value;
	}
	public void setValue(ObjectEditor<ValueExpression> value)
	{
		this.value = value;
	}
	public String getHint(){
		String hint="";
		
		if (getValue() != null)
		{
			ValueExpression ve = getValue().getValue();
			if (ve instanceof Column)
			{
				Column col = (Column) ve;
				String desc=((TableExpression)col.getTable()).getDescription();
				desc=  StringUtils.isEmpty( desc) ?"":"["+desc+"]."; 
				hint = "Colonna :"+desc+"["+col.getDescription()+"] SQL: " + col.toString();
			}
			else if (ve instanceof Constant)
			{
				Constant cos = (Constant) ve;
				hint = "Constante :" + cos.toString();
			}
			else if (ve instanceof Function)
			{
				Function fun = (Function) ve;
				hint = "Funzione :" + fun.toString();
			}
			else if (ve instanceof LiteralExpression)
			{
				LiteralExpression lit = (LiteralExpression) ve;
				hint = "Valore Letterale :" + lit.toString();
			}
			else if (ve instanceof ExplicitSqlExpression)
			{
				ExplicitSqlExpression expl = (ExplicitSqlExpression) ve;
				hint = "Espressione SQL :" + expl.toString();
			}
			else if (ve instanceof Membership)
			{
				Membership mem = (Membership) ve;
				hint = "Espressione SQL :" + mem.toString();
			}
		}

		return hint;
	}
	public boolean isEditable()
	{
		return editable;
	}
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
	public String getChainOperator()
	{
		return chainOperator;
	}
	public void setChainOperator(String chainOperator)
	{
		this.chainOperator = chainOperator;
	}
	public UserEntityColumnBean getParent() {
		return parent;
	}
	public void setParent(UserEntityColumnBean parent) {
		this.parent = parent;
	}

}
