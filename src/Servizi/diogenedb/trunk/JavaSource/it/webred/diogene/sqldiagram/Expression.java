package it.webred.diogene.sqldiagram;

/**
 * Rappresenta una espressione SQL assolutamente generica
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public abstract class Expression implements Cloneable
{
	protected String expression = "";
	protected String description = "";
	protected String UIKey = null;

	/**
	 * @return Come indicazione del tutto generale, la
	 * rappresentazione SQL di questa espressione.
	 * E' ovvio che ci&ograve; non potr&agrave; valere per 
	 * tutte le espressioni, in quanto la rappresentazione
	 * SQL di talune di esse dipender&agrave; dal contesto
	 * in cui si trovano.
	 */
	public abstract String toString();

	/**
	*	
	*/
	protected Expression()	{}
	/**
	*	@param exp
	*	@param des
	*/
	protected Expression(String exp, String des)
	{
		this();
		expression = exp;
		description = des;
	}

	/**
	 * @return La descrizione dell'espressione
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 * La descrizione dell'espressione
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return Il nome dell'espressione
	 */
	public String getExpression()
	{
		return expression;
	}

	/**
	 * @param expression
	 * Il nome dell'espression
	 */
	public void setExpression(String expression)
	{
		this.expression = expression;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Expression clone() throws CloneNotSupportedException
	{
		return (Expression) super.clone();
	}
	/**
	 * @return Una chiave
	 * utile per individuare l'espressione
	 * univocamente in contesto
	 * di user interface
	 */
	public String getUIKey()
	{
		return UIKey;
	}

	/**
	 * @param key
	 * Una chiave
	 * utile per individuare l'espressione
	 * univocamente in contesto
	 * di user interface
	 */
	public void setUIKey(String key)
	{
		UIKey = key;
	}
}
