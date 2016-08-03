package it.webred.diogene.sqldiagram;

import java.util.ArrayList;
import java.util.List;

import org.jdom.CDATA;
import org.jdom.Element;



/**
 * Rappresenta una valore SQL letterale, come un numero o una stringa.
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class LiteralExpression extends ValueExpression
{
	/**
	 * Istanzia una espressione letterale vuota
	 */
	public LiteralExpression()
	{
		super();
	}

	/**
	 * @param exp
	 * Il contenuto dell'espressione SQL
	 * @param des
	 * Un'eventuale descrizione
	 */
	public LiteralExpression(String exp, String des)
	{
		super(exp != null ? exp : "", des);
	}
	
	/**
	 * @param exp
	 * Il contenuto dell'espressione SQL
	 * @param des
	 * Un'eventuale descrizione
	 * @param valueType
	 * Il tipo di dato di questo letterale.
	 */
	public LiteralExpression(String exp, String des, EnumsRepository.ValueType valueType)
	{
		super(exp != null ? exp : "", des, valueType);
	}

	
	/**
	 * @return La rappresentazione SQL di
	 * questa espressione. Se di tipo 
	 * {@link it.webred.diogene.sqldiagram.EnumsRepository.ValueType#STRING},
	 * la stringa viene quotata.
	 */
	public String toString()
	{
		String result = "";
		if (EnumsRepository.ValueType.NUMBER.equals(getValueType()))
			result += expression;
		else if (EnumsRepository.ValueType.STRING.equals(getValueType()))
			result += "'" + expression + "'";
		else result += "'" + expression + "'";
		result += appendedToString();
		return result;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	@Override
	public List<Element> getXml()
	{
		List<Element> result = new ArrayList<Element>();
		
		Element literal = new Element("literal");
		Element description = new Element("description");
		Element value = new Element("value");
		literal.addContent(value);
		literal.addContent(description);

		/* VECCHIA GESTIONE DEGLI SPAZI
		String expression = super.expression;
		if (expression != null)
		{
			if (expression.trim().length() == 0)
				for (int i = 0; i < expression.length(); i++)
					value.addContent(new Element("space"));
			else
				value.setText(expression);
		}
		*/
		
		// NUOVA GESTIONE DI TUTTI I CARATTERI SPECIALI
		if (super.expression != null)
			value.addContent(new CDATA(super.expression));
		description.setText(super.description);

		result.addAll(getAppendedXml());		
		result.add(literal);
		return result;		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public LiteralExpression clone() throws CloneNotSupportedException
	{
		LiteralExpression clone = (LiteralExpression) super.clone();
		return clone;
	}

	@Override
	public String getExpression()
	{
		return super.getExpression() != null ? super.getExpression() : "";
	}

	@Override
	public void setExpression(String expression)
	{
		super.setExpression(expression != null ? expression : "");
	}
}
