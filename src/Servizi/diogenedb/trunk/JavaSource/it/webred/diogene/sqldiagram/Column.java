package it.webred.diogene.sqldiagram;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;


/**
 * Rappresenta una colonna di una tabella SQL
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.2 $ $Date: 2008/01/24 13:32:30 $
 */
public class Column extends ValueExpression
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1506599409812192301L;
	private Table table = null;
	
	public Column() {
		super();
	}
	/**
	 * 
	 * @param exp 
	 * Il nome della colonna
	 * @param des
	 * La descrizione della colonna
	 * @param alias
	 * @see ValueExpression#setAlias(String)
	 * @param table
	 * La tabella a cui questa colonna appartiene, corrispondente
	 * alla tabella messa in from nella select
	 * @param valueType
	 * @see ValueExpression#setValueType(ValueType)
	 */
	public Column(String exp, String des, String alias, Table table, EnumsRepository.ValueType valueType) {
		super(exp, des, valueType);
		setTable(table);
		setAlias(alias);
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ValueExpression#getDefinition()
	 */
	public String getDefinition() {
		return "Nome di colonna";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return (((getTableName() != null) && (getTableName().trim().length() > 0)) ? (getTableName() + ".") : ("")) + super.expression + appendedToString();
	}
	
	/**
	 * Il metodo non chiama esclusivamente <code>super.clone()</code>,
	 * ma clona anche la tabella della colonna, chiamando
	 * il suo metodo clone
	 * @see Table#clone()
	 * @see ValueExpression#clone()
	 */
	@Override
	public Column clone() throws CloneNotSupportedException
	{
		Column clone = (Column) super.clone();
		clone.setTable(getTable().clone());
		return clone;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	public List<Element> getXml()
	{
		List<Element> result = new ArrayList<Element>();
		
		Element column = new Element("column");
		Element table = new Element("table");
		Element field = new Element("field");
		Element element = null;
		column.addContent(table);
		column.addContent(field);
		if (getUIKey() != null && !"".equals(getUIKey().trim()))
			column.setAttribute(new Attribute("ui_key",getUIKey()));

		if (getTable() instanceof TableExpression)
		{
			element = new Element("pk_id");
			element.setText("" + ((TableExpression) getTable()).getId());
			((TableExpression) getTable()).getId();
			table.addContent(element);
			if (((TableExpression) getTable()).isOuter())
				table.setAttribute(new Attribute("outer", "true"));
			String tableDesc=((TableExpression) getTable()).getDescription();
			if (tableDesc!=null){
				element= new Element("description");
				element.setText(tableDesc);
				table.addContent(element);
			}
		}
		element = new Element("name");
		element.setText(getTable().getExpression());
		table.addContent(element);
		String alias = getTable().getAlias();
		if (alias != null && !"".equals(alias.trim()))
		{
			element = new Element("alias");
			element.setText(alias);
			table.addContent(element);
		}
		element = new Element("name");
		element.setText(super.expression);
		field.addContent(element);
		if (super.description != null)
		{
			Element description = new Element("description");
			field.addContent(description);
			description.setText(super.description);			
		}
		if (getAlias() != null && !"".equals(getAlias()))
		{
			element = new Element("alias");
			element.setText(getAlias());
			field.addContent(element);
		}

		result.add(column);
		result.addAll(getAppendedXml());
		return result;
	}
	/**
	 * @return
	 * Il nome della tabella associata a
	 * questa colonna. Se la tabella ha
	 * un alias, restituisce quell'alias. Se la tabella è
	 * <code>null</code>, restituisce null
	 */
	public String getTableName()
	{
		if (getTable() != null)
			if ((getTable().getAlias() != null) && (!"".equals(getTable().getAlias())))
				return getTable().getAlias();
			else
				return getTable().getExpression();
		return null;
	}
	/**
	 * @return
	 * La tabella associata a questa colonna
	 */
	public Table getTable()
	{
		return table;
	}
	/**
	 * @param table
	 * La table da associare a questa colonna
	 */
	public void setTable(Table table)
	{
		this.table = table;
	}
}
