package it.webred.diogene.sqldiagram;

import static it.webred.utils.StringUtils.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Questa classe rappresenta una query SQL, nella sua complessit&agrave;
 * con i campi in SELECT, le tabelle in FROM, le condizioni WHERE, 
 * le clausole GROUP BY e ORDER BY (TODO).
 * La query può anche contenere riferimenti ad altre query come 
 * sottoquery nella clausola FROM.
 * @see it.webred.diogene.sqldiagram.Table
 * @see it.webred.diogene.sqldiagram.Membership
 * @author Giulio Quaresima
 * @version $Revision: 1.2 $ $Date: 2007/10/01 14:56:49 $
 */
public class Query extends Membership implements Table
{
	protected List<ValueExpression> selectList = null;
	protected List<ValueExpression> groupByList = null;
	protected List<ValueExpression> orderByList = null;
	protected List<Table> fromList = null;
	protected Condition where = null;
	protected String alias;
	private Predicate predicate;
	
	public Query() {
		super();
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ValueExpression#getDefinition()
	 */
	public String getDefinition() {
		return "entità Utente";
	}
	
	/**
	 * Aggiunge una espressione alle espressioni in SELECT.
	 * L'ordine con cui le espressioni vengono aggiunte sar&agrave;
	 * l'ordine con cui esse appariranno in SELECT.
	 * Una query necessita come minimo di una espressione in SELECT
	 * per essere in uno stato coerente.
	 * @param e
	 * L'espressione da aggiungere in SELECT.
	 * @return
	 * La stessa espressione aggiunta.
	 */
	public ValueExpression addSelectExpression(ValueExpression e)
	{
		if (selectList == null)
			selectList = new ArrayList<ValueExpression>();
		selectList.add(e);
		return e;
	}
	/**
	 * Aggiunge una tabella alle tabelle in FROM.
	 * Una query necessita come minimo di una tabella in FROM
	 * per essere in uno stato coerente.
	 * @param t
	 * La tabella da aggiungere in FROM. 
	 * @return
	 * La stessa tabella aggiunta
	 */
	public Table addFromTable(Table t)
	{
		if (fromList == null)
			fromList = new ArrayList<Table>();
		fromList.add(t);
		return t;		
	}
	/**
	 * Definisce la condizione WHERE della query
	 * @param c
	 * La condizione WHERE.
	 * @return
	 * La stessa condizione aggiunta.
	 */
	public Condition setWhereCondition(Condition c)
	{
		this.where = c;
		return this.where;
	}
	
	
	/**
	 * Questo metodo, utilizzato opportunamente all'interno
	 * di {@link Query#toString()}, restituisce l'opportuna 
	 * stringa che rappresenti l'oggetto {@link Table} dopo la
	 * clausola FROM. Se infatti per le implementazioni
	 * {@link TableExpression} di {@link Table} è sufficente
	 * chiamare {@link TableExpression#toString()}, per le implementazioni
	 * {@link Query} &egrave; necessario circondare la query
	 * restituita da {@link Query#toString()} con parentesi tonde,
	 * e far seguire il nome della query contenuto in {@link Table#getExpression()}
	 * o l'alias della query contenuto in {@link Table#getAlias()}
	 * @param t
	 * @return
	 */
	protected String getTableRepresentation(Table t)
	{
		String result = "";
		if (t == null)
			throw new NullPointerException(); // TODO:
		if (t instanceof Query)
		{
			result += "(" + t.toString() + ") ";
			result += t.getAlias() != null ? t.getAlias() : t.getExpression();
		}
		else if (t instanceof TableExpression)
			result += t.toString();
		return result;
	}
	
	/**
	 * @return La rappresentazione SQL di questa query
	 */
	@Override
	public String toString()
	{
		String result = "SELECT ";
		if (getPredicate() != null)
			result += getPredicate() + " ";
		result += CRLF;
		try
		{
			Iterator<ValueExpression> iter = selectList.iterator();
			ValueExpression exp = iter.next();
			result += getTab(3) + exp;
			if (exp.getAlias() != null)
				result += " " + exp.getAlias();
			while (iter.hasNext())
			{
				exp = iter.next();
				result += ", " + CRLF + getTab(3) + exp;
				if (exp.getAlias() != null)
					result += " " + exp.getAlias();
			}
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Una SELECT abbisogna di almeno un'espressione in select!");
		}
		result += CRLF + "FROM " + CRLF;
		try
		{
			Iterator<Table> iter = fromList.iterator();
			result += getTab(3) + getTableRepresentation(iter.next());
			while (iter.hasNext())
				result += ", " + CRLF + getTab(3) + getTableRepresentation(iter.next());
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Una SELECT abbisogna di almeno una tabella in from!", e);
		}
		if (where != null)
		{
			String whereStr = where.toString();
			whereStr = whereStr.replaceAll("\\Q"+CRLF+"\\E", CRLF + getTab(3));
			result += CRLF + "WHERE " + whereStr;
		}
		
		if (!getGroupByList().isEmpty())
		{
			result += CRLF + "GROUP BY ";
			int index = 0;
			for (ValueExpression item : getGroupByList())
			{
				try
				{
					ValueExpression exp = item.clone();
					exp.setAlias(null);
					if (index > 0) result += ",";
					result += CRLF + getTab(3) + exp;
					index++;
				}
				catch (CloneNotSupportedException e) {}
			}
		}
		
		if (!getOrderByList().isEmpty())
		{
			result += CRLF + "ORDER BY ";
			int index = 0;
			for (ValueExpression item : getOrderByList())
			{
				try
				{
					ValueExpression exp = item.clone();
					exp.setAlias(null);
					if (index > 0) result += ",";
					result += CRLF + getTab(3) + exp;
					index++;
				}
				catch (CloneNotSupportedException e) {}
			}
		}
		
		return result;
	}

	/**
	 * @return Il clone di questa istanza, clonando anche
	 * opportunamente tutti i riferimenti alle espressioni
	 * in SELECT, tutti i riferimenti alle tabelle in FROM,
	 * e la condizione WHERE
	 * @see {@link ValueExpression#clone()}
	 * @see {@link Table#clone()}
	 * @see {@link Condition#clone()}
	 * clonabili
	 */
	public Query clone() throws CloneNotSupportedException
	{
		Query clone = (Query) super.clone();
		clone.setValueType(getValueType());
		if (selectList != null)
			for (ValueExpression item : selectList)
				clone.addSelectExpression(item.clone());
		if (fromList != null)
			for (Table item : fromList)
				clone.addFromTable(item.clone());
		clone.setWhere(getWhere().clone());
		return clone;
	}
	
	/**
	 * @return La condizione WHERE
	 */
	public Condition getWhere()
	{
		return where;
	}
	/**
	 * Definisce la condizione WHERE per questa query
	 * @param where
	 */
	private void setWhere(Condition where)
	{
		this.where = where;
	}
	/** 
	 * @see it.webred.diogene.sqldiagram.Table#getAlias()
	 */
	public String getAlias()
	{
		return alias;
	}
	/**
	 * @see it.webred.diogene.sqldiagram.Table#setAlias(String)
	 */
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	/**
	 * @return Le lista delle tabelle in FROM
	 */
	public List<Table> getFromList()
	{
		return fromList;
	}
	/**
	 * Definisce la lista delle tabelle in FROM
	 * @param fromList
	 */
	public void setFromList(List<Table> fromList)
	{
		this.fromList = fromList;
	}
	/**
	 * @return la lista delle espressioni in SELECT
	 */
	public List<ValueExpression> getSelectList()
	{
		return selectList;
	}
	/**
	 * Definisce la lista delle espressioni in SELECT
	 * @param selectList
	 */
	public void setSelectList(List<ValueExpression> selectList)
	{
		this.selectList = selectList;
	}

	public List<ValueExpression> getGroupByList()
	{
		if (groupByList == null)
			groupByList = new ArrayList<ValueExpression>();
		return groupByList;
	}

	public void setGroupByList(List<ValueExpression> groupByList)
	{
		this.groupByList = groupByList;
	}

	public List<ValueExpression> getOrderByList()
	{
		if (orderByList == null)
			orderByList = new ArrayList<ValueExpression>();
		return orderByList;
	}

	public void setOrderByList(List<ValueExpression> orderByList)
	{
		this.orderByList = orderByList;
	}

	public Predicate getPredicate()
	{
		return predicate;
	}

	public void setPredicate(Predicate predicate)
	{
		this.predicate = predicate;
	}
	
}
