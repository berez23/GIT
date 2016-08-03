package it.webred.diogene.querybuilder.control;

import static it.webred.diogene.querybuilder.Constants.COLUMN;
import static it.webred.diogene.querybuilder.control.Constants.USER_ENTITY_FIELDS_TABLE_ID;
import static it.webred.utils.StringUtils.getLatinCapitalIndex;
import it.webred.diogene.querybuilder.control.UserEntityColumnBeanAppendedValue;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.ValueExpression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

public class UserEntityColumnBean implements Serializable, Switches {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1558263528003849244L;

	public final String NAME_JSF_ID = "fieldName";
	
	private int position;
	private Long id;
	private ObjectEditor<ValueExpression> column;
	private ValueExpression xmlColumn;
	private List<UserEntityColumnBeanAppendedValue> appendedExpressions;
	private String name;
	private String description;
	private boolean primaryKey;
	private boolean groupBy;
	private boolean orderBy;
	private boolean editable;
	private String xmlValueType;
	
	private UserEntityBean parent;
	
	public UserEntityColumnBean() {super();}
	
	public UserEntityColumnBean(int position, Long id, ValueExpression column, String name, String description, boolean primaryKey, boolean groupBy, boolean orderBy, boolean editable)
	{
		this();
		this.position = position;
		setEditable((this.id = id) == null);
		this.column = new ObjectEditor<ValueExpression>(column);
		this.setXmlColumn(column);
		this.name = name;
		this.description = description;
		this.primaryKey = primaryKey;
		this.groupBy = groupBy;
		this.orderBy = orderBy;
		this.editable = editable;
	}
	
	
	public void edit(ActionEvent e)
	{
		getColumn().edit();
		parent.setEditingValue(this.getColumn());
		parent.setEditingColumnBean(this);
		if (!parent.isModifiedByUser())
			parent.setModifiedByUser(true);
	}
	public void nameModified(ValueChangeEvent e)
	{
		if (!parent.isModifiedByUser())
			parent.setModifiedByUser(true);
	}
	public void primaryKeyModified(ValueChangeEvent e)
	{
		if (!parent.isModifiedByUser())
			parent.setModifiedByUser(true);
	}
	public void orderByModified(ValueChangeEvent e)
	{
		if (!parent.isModifiedByUser())
			parent.setModifiedByUser(true);
	}
	public void groupByModified(ValueChangeEvent e)
	{
		if (!parent.isModifiedByUser())
			parent.setModifiedByUser(true);
	}
	public void appendNewValue(ValueExpression value)
	{
		UserEntityColumnBeanAppendedValue uecbav = new UserEntityColumnBeanAppendedValue(new ObjectEditor<ValueExpression>(value));
		uecbav.setEditable(this.isEditable());
		uecbav.setParent(this);
		getAppendedExpressions().add(uecbav);
	}
	public void appendNewValue(ValueExpression value, String chainOperator)
	{
		UserEntityColumnBeanAppendedValue uecbav = new UserEntityColumnBeanAppendedValue(new ObjectEditor<ValueExpression>(value), chainOperator);
		uecbav.setEditable(this.isEditable());
		uecbav.setParent(this);
		getAppendedExpressions().add(uecbav);
	}
	public void appendNewValue(ActionEvent e)
	{
		appendNewValue(new LiteralExpression());
		if (!parent.isModifiedByUser())
			parent.setModifiedByUser(true);
	}
	public void moveUp(ActionEvent e)
	{
		if (isEditable() && getPosition() > 0)
		{
			parent.switchPosition(getPosition(), getPosition() - 1);
			if (!parent.isModifiedByUser())
				parent.setModifiedByUser(true);
		}
	}
	public void moveDown(ActionEvent e)
	{
		if (isEditable() && getPosition() < (parent.getSelectList().size() - 1))
		{
			parent.switchPosition(getPosition(), getPosition() + 1);
			if (!parent.isModifiedByUser())
				parent.setModifiedByUser(true);
		}
	}
	public void moveFirst(ActionEvent e)
	{
		if (isEditable() && getPosition() > 0)
		{
			int firstPosition = 0;
			while (firstPosition < parent.getSelectList().size() && !parent.getSelectList().get(firstPosition++).isEditable());
			parent.getSelectList().remove(getPosition());
			parent.getSelectList().add(--firstPosition, this);
			parent.arrangeArgumentIndexes();
			if (!parent.isModifiedByUser())
				parent.setModifiedByUser(true);
		}
	}
	public void moveLast(ActionEvent e)
	{
		if (isEditable() && getPosition() < (parent.getSelectList().size() - 1))
		{
			parent.getSelectList().remove(getPosition());
			parent.getSelectList().add(this);
			parent.arrangeArgumentIndexes();
			if (!parent.isModifiedByUser())
				parent.setModifiedByUser(true);
		}
	}
	public void deleteMe(ActionEvent e)
	{
		if (isFromDate())
			parent.setFromDate(null);
		if (isToDate())
			parent.setToDate(null);
		parent.getSelectList().remove(getPosition());
		parent.arrangeArgumentIndexes();
		parent.setEditingValue(null);
		if (getId() != null)
			parent.getDeletedColumns().add(getId());
		if (!parent.isModifiedByUser())
			parent.setModifiedByUser(true);
	}
	
	public String getAlias()
	{
		return COLUMN + getLatinCapitalIndex(getPosition());
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.Switches#isEditable()
	 */
	public boolean isEditable()
	{
		return editable;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.Switches#setEditable(boolean)
	 */
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
	public String getJSFId()
	{
		return USER_ENTITY_FIELDS_TABLE_ID + "_" + getPosition() + ":" + NAME_JSF_ID;
	}
	// GETTERS AND SETTERS ////////////////////////////////
	public ObjectEditor<ValueExpression> getColumn()
	{
		if (column != null) {
			column.setEditable(isEditable());
		}
		return column;
	}
	public void setColumn(ObjectEditor<ValueExpression> column)
	{
		this.column = column;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public boolean isGroupBy()
	{
		return groupBy;
	}
	public void setGroupBy(boolean groupBy)
	{
		this.groupBy = groupBy;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean isOrderBy()
	{
		return orderBy;
	}
	public void setOrderBy(boolean orderBy)
	{
		this.orderBy = orderBy;
	}
	public boolean isFromDate()
	{
		return (parent.getFromDate() != null && parent.getFromDate().equals(getPosition() + 1));
	}
	public boolean isToDate()
	{
		return (parent.getToDate() != null && parent.getToDate().equals(getPosition() + 1));
	}
	public boolean isExternalKey()
	{
		return (parent.getExternalKey() != null && parent.getExternalKey().equals(getPosition() + 1));
	}


	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	/**
	 * Utile per le modifiche
	 * @return
	 * L'id nel DB, o <code>null</code> se si tratta di
	 * una colonna nuova.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Utile per le modifiche.
	 * @param id
	 * L'id nel DB.
	 */
	public void setId(Long id)
	{
		setEditable((this.id = id) == null);
	}

	public boolean isPrimaryKey()
	{
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey)
	{
		this.primaryKey = primaryKey;
	}

	public List<UserEntityColumnBeanAppendedValue> getAppendedExpressions()
	{
		if (appendedExpressions == null)
			appendedExpressions = new ArrayList<UserEntityColumnBeanAppendedValue>();
		return appendedExpressions;
	}

	public void setAppendedExpressions(List<UserEntityColumnBeanAppendedValue> appendedExpressions)
	{
		this.appendedExpressions = appendedExpressions;
	}

	public String getHint()
	{
		String hint="";
		if (column != null)
		{
			ValueExpression ve = column.getValue();
			hint=ValueExpression.retrieveHint(ve);
		}
		return hint;
	}

	public UserEntityBean getParent() {
		return parent;
	}

	public void setParent(UserEntityBean parent) {
		this.parent = parent;
	}

	public ValueExpression getXmlColumn() {
		return xmlColumn;
	}

	public void setXmlColumn(ValueExpression xmlColumn) {
		this.xmlColumn = xmlColumn;
	}

	public String getXmlValueType() {
		return xmlValueType;
	}

	public void setXmlValueType(String xmlValueType) {
		this.xmlValueType = xmlValueType;
	}

}
