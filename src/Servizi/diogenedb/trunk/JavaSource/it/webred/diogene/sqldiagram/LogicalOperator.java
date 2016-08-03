package it.webred.diogene.sqldiagram;

/**
 * Rappresenta l'operatore logico che pu&ograve; concatenare
 * le {@link it.webred.diogene.sqldiagram.Condition condizioni} SQL, 
 * come AND o OR
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class LogicalOperator implements Operator
{
	private String name;
	private String format;
	private String firstOccurrenceFormat;
	private String description;
	private String UIKey;
	
	/**
	 * @param name
	 * Il nome che identifica univocamente questo operatore, utilizzato
	 * per richiamarlo attraverso il metodo
	 * {@link it.webred.diogene.sqldiagram.OperatorFactory#getLogicalOperator(String name) getLogicalOperator(String name)}
	 * @param format
	 * Il formato SQL di questo operatore.
	 * @param firstOccurrenceFormat
	 * Il formato SQL della prima occorrenza di questo operatore.
	 * Ad esempio, se abbiamo una serie di condizioni concatenata 
	 * dall'operatore AND, la prima condizione della lista non dovr&agrave;
	 * essere preceduta da AND, se abbiamo una serie di condizioni concatenata 
	 * dall'operatore OR NOT, la prima condizione della lista dovr&agrave;
	 * essere preceduta soltanto da NOT, non da OR NOT
	 * @param description
	 * Un'eventuale descrizione.
	 */
	public LogicalOperator(String name, String format, String firstOccurrenceFormat, String description)
	{
		this.name = name;
		this.format = format;
		this.firstOccurrenceFormat = firstOccurrenceFormat;
		this.description = description;
	}
	
	/**
	 * @return Il formato SQL della prima occorrenza di questo operatore.
	 * Ad esempio, se abbiamo una serie di condizioni concatenata 
	 * dall'operatore AND, la prima condizione della lista non dovr&agrave;
	 * essere preceduta da AND, se abbiamo una serie di condizioni concatenata 
	 * dall'operatore OR NOT, la prima condizione della lista dovr&agrave;
	 * essere preceduta soltanto da NOT, non da OR NOT
	 */
	public String showWhenFirst()
	{
		return firstOccurrenceFormat;
	}
	
	/**
	 * @return come {@link LogicalOperator#getFormat() getFormat()} 
	 */
	@Override
	public String toString()
	{
		return getFormat();
	}

	/**
	 * @return L'eventuale descrizione
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 * Un eventuale descrizione
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return Il formato, o nome, SQL dell'operatore
	 * logico
	 */
	public String getFormat()
	{
		return format;
	}

	/**
	 * @return
	 * Il nome univoco dell'operatore, utilizzato come chiave 
	 * per recuperare l'operatore dal metodo
	 * {@link OperatorFactory#getLogicalOperator(String)} 
	 */
	public String getName()
	{
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public LogicalOperator clone() 
	throws CloneNotSupportedException
	{
		return (LogicalOperator) super.clone();
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.Operator#getUIKey()
	 */
	public String getUIKey()
	{
		return UIKey;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.Operator#setUIKey(java.lang.String)
	 */
	public void setUIKey(String key)
	{
		UIKey = key;
	}
}
