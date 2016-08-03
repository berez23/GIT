package it.webred.diogene.sqldiagram;

import static it.webred.utils.StringUtils.*;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Questa classe rappresenta gli operatori che possono concatenare più
 * {@link it.webred.diogene.sqldiagram.ValueExpression ValueExpression}
 * @see it.webred.diogene.sqldiagram.ValueExpression#appendExpression(ValueExpressionOperator, ValueExpression)
 * @author Giulio Quaresima
 * @version $Revision: 1.3 $ $Date: 2008/01/24 13:32:30 $
 */
public class ValueExpressionOperator implements Serializable, Operator, XMLRepresentable
{
	/**
	 * 
	 */
	/**
	 * 
	 */
	private static final long serialVersionUID = 5356898308971416203L;
	private String name = null;
	private String format = null;
	private EnumsRepository.ValueType[] acceptedTypes = null;
	private String UIKey;
	
	/**
	 * @param name
	 * Il nome che identifica univocamente questo operatore, utilizzato
	 * per richiamarlo attraverso il metodo
	 * {@link it.webred.diogene.sqldiagram.OperatorFactory#getValueExpressionOperator(String name) getValueExpressionOperators(String name)}
	 * @param format
	 * La forma con cui questo operatore viene rappresentato
	 * in SQL, vedi {@link ValueExpressionOperator#toString()}
	 * @param acceptedTypes vedi anche {@link ValueExpressionOperator#acceptType(EnumsRepository.ValueType)} 
	 */
	public ValueExpressionOperator(String name, String format, EnumsRepository.ValueType... acceptedTypes)
	{
		this.name = name;
		this.format = format;
		this.acceptedTypes = acceptedTypes;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.Operator#getName()
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param type
	 * Il tipo del quale si vuole verificare l'accettabilit&agrave;
	 * da parte di questo operatore.
	 * @return
	 * <code>true</code> se questo tipo &egrave; accettabile da
	 * parte di questo operatore, <code>false</code> altrimenti.
	 */
	public boolean acceptType(EnumsRepository.ValueType type)
	{
		if (acceptedTypes != null)
			for (EnumsRepository.ValueType item : acceptedTypes)
				if (item.equals(type))
					return true;
		return false;
	}
	/**
	 * @see Operator#toString()
	 */
	public String toString()
	{
		return " " + format + " ";
	}
	
	@Override
	public ValueExpressionOperator clone()
	throws CloneNotSupportedException
	{
		return (ValueExpressionOperator) super.clone();
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	public List<Element> getXml()
	{
		List<Element> result = new ArrayList<Element>();
		
		Element chain_operator = new Element("chain_operator");
		if (getUIKey() != null && !"".equals(getUIKey().trim()))
			chain_operator.setAttribute(new Attribute("ui_key",getUIKey()));
		Element description = new Element("description");
		Element name = new Element("name");
		chain_operator.addContent(name);
		chain_operator.addContent(description);
		
		name.setText(this.name);
		description.setText(null);
		
		result.add(chain_operator);
		return result;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getStringXml()
	 */
	public String getStringXml()
	{
		StringWriter result = new StringWriter();
		XMLOutputter out = new XMLOutputter();
		//out.setIndentSize(3); // DEPRECATO. SERVIREBBE SOLO PER MOTIVI ESTETICI
//		out.setLineSeparator(CRLF);
//		out.setNewlines(true);
		out.setFormat(Format.getPrettyFormat());
		try
		{
			out.output(getXml(), result);
		}
		catch (IOException e) {}
		return result.toString();
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
