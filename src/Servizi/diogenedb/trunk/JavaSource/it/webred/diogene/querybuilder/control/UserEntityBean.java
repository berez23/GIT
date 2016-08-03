package it.webred.diogene.querybuilder.control;

import static it.webred.diogene.querybuilder.Constants.COLUMN;
import static it.webred.diogene.querybuilder.control.Constants.USER_ENTITY_FIELDS_TABLE_ID;
import static it.webred.utils.StringUtils.getLatinCapitalIndex;
import static it.webred.utils.StringUtils.isEmpty;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.Condition;
import it.webred.diogene.sqldiagram.Constant;
import it.webred.diogene.sqldiagram.ExplicitSqlExpression;
import it.webred.diogene.sqldiagram.Function;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.Membership;
import it.webred.diogene.sqldiagram.TableExpression;
import it.webred.diogene.sqldiagram.Tuple;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.ValueExpressionOperator;
import it.webred.diogene.sqldiagram.ValueExpression.AppendedValueExpression;
import it.webred.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

public class UserEntityBean implements Serializable,  Switches
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6977370212693191063L;
	private Long id;
	private String name;
	private String description;
	private String predicate;
	private Set<DcEntityBean> fromList;
	private List<UserEntityColumnBean> selectList;
	private Set<Long>
		deletedColumns, deletedJoins;
	private Condition condition;
	private List<JoinSpec> joins;
	private ObjectEditor<ValueExpression> editingValue;
	private UserEntityColumnBean editingColumnBean;
	private boolean editable = true;
	private Integer fromDate;
	private Integer toDate;
	private Integer externalKey;
	private boolean modifiedByUser = false;
	private boolean alreadyInUse = false;
	private Date dtMod; 
	private String userMod;
	
	private String explicitSql;
	
	public UserEntityBean() {super();}
	
	public UserEntityColumnBean addColumn(ValueExpression expression, Long id, String name, String description, boolean primaryKey, boolean groupBy, boolean orderBy, boolean editable)
	{
		UserEntityColumnBean column = new UserEntityColumnBean(
				getSelectList().size(),
				id,
				expression,
				name,
				description,
				primaryKey,
				groupBy,
				orderBy,
				editable);
		column.setParent(this);
		for (AppendedValueExpression item : expression.getAppendedValueExpressions())
			column.appendNewValue(item.getExpression(), item.getOperator().getName());
		for (UserEntityColumnBeanAppendedValue item : column.getAppendedExpressions())
			item.getValue().save();
		expression.getAppendedValueExpressions().clear();
		column.setEditable(this.isEditable());
		getSelectList().add(column);
		setEditingColumnBean(column);
		return column;
	}
	public void addFromTable(DcEntityBean table)
	{
	//	if (getFromList().contains(table))
	//		throw new IllegalArgumentException(); // TODO
		getFromList().add(table);
	}
	public JoinSpec addJoinCondition(Long id, Condition joinCondition, DcEntityBean formerTable, DcEntityBean latterTable, boolean outerJoin, boolean editable)
	{
		JoinSpec join = new JoinSpec(id, joinCondition, formerTable, latterTable, outerJoin, editable);
		join.setEditable(this.isEditable());
		getJoins().add(join);
		return join;
	}
	public JoinSpec addJoinCondition(GenericCondition condition)
	{
		JoinSpec join = new JoinSpec(condition);
		join.setEditable(this.isEditable());
		getJoins().add(join);
		return join;
	}
	public void removeFromTable(DcEntityBean table)
	{
		if (getFromList().contains(table))
		{
			Set<UserEntityColumnBean> toRemove = new HashSet<UserEntityColumnBean>();
			for (UserEntityColumnBean item : getSelectList())
			{
				if (item.getColumn() != null && item.getColumn().getValue() != null)
				{
					ValueExpression exp = item.getColumn().getValue();
					if (needsThisTable(exp, new TableExpression(table.getId(), table.getSqlName(), table.getDesc(), table.getAlias())))
						toRemove.add(item);
				}
			}
			for (UserEntityColumnBean item : toRemove) {
				getSelectList().remove(item);
				if (item.getId() != null)
					getDeletedColumns().add(item.getId());
			}
				
			arrangeArgumentIndexes();
			
			getFromList().remove(table);
		}
	}
	private boolean needsThisTable(ValueExpression exp, TableExpression table)
	{
		if (exp instanceof Column)
		{
			Column col = (Column) exp;
			Column nullColumn = new Column();
			nullColumn.setTable(table);
			if (col.getTable() != null)
				return (col.getTableName() != null && col.getTableName().equals(nullColumn.getTableName()));
		}
		else if (exp instanceof Function)
		{
			boolean result = false;
			Function f = (Function) exp;
			for (ValueExpression item : f.getArguments().getExpressions())
			{
				result = result || needsThisTable(item, table);
			}
			return result;
		}
		return false;
	}
	
	protected void switchPosition(int i1, int i2) throws IndexOutOfBoundsException
	{
		UserEntityColumnBean o1 = getSelectList().get(i1);
		UserEntityColumnBean o2 = getSelectList().get(i2);
		if (o1.isEditable() && o2.isEditable())
		{
			getSelectList().set(i1, o2);
			getSelectList().set(i2, o1);
			arrangeArgumentIndexes();				
		}
	}
	protected void arrangeArgumentIndexes()
	{
		Integer from = null, to = null;
		for (int i = 0; i < getSelectList().size(); i++)
		{
			if (getSelectList().get(i).isFromDate())
				from = i + 1;
			if (getSelectList().get(i).isToDate())
				to = i + 1;
		}
		setFromDate(from);
		setToDate(to);
		for (int i = 0; i < getSelectList().size(); getSelectList().get(i).setPosition(i++));
	}	
		
	// GETTERS AND SETTERS //////////////////////////////////////
	public Condition getCondition()
	{
		return condition;
	}

	public void setCondition(Condition condition)
	{
		this.condition = condition;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Set<DcEntityBean> getFromList()
	{
		if (fromList == null)
			fromList = new HashSet<DcEntityBean>();
		return fromList;
	}
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPredicate()
	{
		return predicate;
	}

	public void setPredicate(String predicate)
	{
		this.predicate = predicate;
	}

	public List<UserEntityColumnBean> getSelectList()
	{
		if (selectList == null)
			selectList = new ArrayList<UserEntityColumnBean>();
		return selectList;
	}

	public void setSelectList(List<UserEntityColumnBean> selectList)
	{
		this.selectList = selectList;
	}
	public ObjectEditor<ValueExpression> getEditingValue()
	{
		return editingValue;
	}
	public void setEditingValue(ObjectEditor<ValueExpression> editingValue)
	{
		this.editingValue = editingValue;
	}
	public List<JoinSpec> getJoins()
	{
		if (joins == null)
			joins = new ArrayList<JoinSpec>();
		return joins;
	}
	public void setJoins(List<JoinSpec> joins)
	{
		this.joins = joins;
	}
	/**
	 * Utile per le modifiche.
	 * @return
	 * L'id nel DB.
	 */
	public Long getId()
	{
		return id;
	}
	/**
	 * Utile per le modifiche.
	 * @param id
	 * L'id nel DB, o <code>null</code> se si tratta di
	 * una colonna nuova.
	 */
	public void setId(Long id)
	{
		this.id = id;
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
	public Set<Long> getDeletedColumns()
	{
		if (deletedColumns == null)
			deletedColumns = new HashSet<Long>();
		return deletedColumns;
	}
	public Set<Long> getDeletedJoins()
	{
		if (deletedJoins == null)
			deletedJoins = new HashSet<Long>();
		return deletedJoins;
	}
	public Integer getFromDate()
	{
		return fromDate;
	}
	public void setFromDate(Integer fromDate)
	{
		this.fromDate = fromDate;
	}
	public Integer getToDate()
	{
		return toDate;
	}
	public void setToDate(Integer toDate)
	{
		this.toDate = toDate;
	}
	public boolean isModifiedByUser()
	{
		return modifiedByUser;
	}
	public void setModifiedByUser(boolean modifiedByUser)
	{
		this.modifiedByUser = modifiedByUser;
	}
	public String getExplicitSql()
	{
		return explicitSql;
	}
	public void setExplicitSql(String explicitSql)
	{
		this.explicitSql = explicitSql;
	}
	public UserEntityColumnBean getEditingColumnBean()
	{
		return editingColumnBean;
	}
	public void setEditingColumnBean(UserEntityColumnBean editingColumnBean)
	{
		this.editingColumnBean = editingColumnBean;
	}
	public Integer getExternalKey()
	{
		return externalKey;
	}
	public void setExternalKey(Integer externalKey)
	{
		this.externalKey = externalKey;
	}
	public boolean isAlreadyInUse()
	{
		return alreadyInUse;
	}
	public void setAlreadyInUse(boolean alreadyInUse)
	{
		this.alreadyInUse = alreadyInUse;
	}
	public Date getDtMod()
	{
		return dtMod;
	}
	public void setDtMod(Date dtMod)
	{
		this.dtMod = dtMod;
	}
	public String getUserMod()
	{
		return userMod;
	}
	public void setUserMod(String userMod)
	{
		this.userMod = userMod;
	}
	
	//questo metodo è necessario per l'uso di XMLEncoder - Filippo Mazzini 23.10.07
	public void setFromList(Set<DcEntityBean> fromList) {
		this.fromList = fromList;
	}
	//fine Filippo Mazzini 23.10.07
}
