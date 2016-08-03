package it.webred.diogene.sqldiagram;

import it.webred.utils.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Un operatore relazionale SQL. Gli operatori relazionali
 * sono operatori che accettano una o pi&ugrave; espressioni
 * come operandi e restituiscono un valore booleano, come ad
 * esempio IS NULL, =, BETWEEN, IN
 * @author Giulio Quaresima
 * @version $Revision: 1.2 $ $Date: 2007/11/22 15:59:57 $
 */
public class RelationalOperator implements Operator, XMLRepresentable
{
	protected ValueExpression[] operands = null;
	protected Class<? extends ValueExpression>[] operandsTypes = null;
	protected String xmlValueTypes[] = null;
	
	private String name = "";
	private String format = "";
	private String formatString = "";
	private String description = "";
	private int numOfOperands = 0;
	private String UIKey;

	/**
	 * 
	 * @param name
	 * Il nome che identifica univocamente questo operatore, utilizzato
	 * per richiamarlo attraverso il metodo
	 * {@link it.webred.diogene.sqldiagram.OperatorFactory#getOperator(String name) getOperator(String name)}
	 * @param format
	 * La stringa SQL che rappresenta l'operatore
	 * @param formatString
	 * La stringa jolly utilizzata per indicare le posizioni degli operandi
	 * rispetto all'operatore, come '%' nell'esempio seguente: <br />
	 * <tt>% BETWEEN % AND %</tt><br />
	 * @see RelationalOperator#toString()
	 * @param description
	 * Un'eventuale descrizione.
	 * @param operandsTypes
	 * I tipi di operandi accettati da questo operatore.
	 */
	public RelationalOperator(String name, String format, String formatString, String description, Class<? extends ValueExpression>... operandsTypes)
	{		
		this.name = name;
		this.format = format;
		this.formatString = formatString;
		this.description = description;
		this.operandsTypes = operandsTypes;

		Matcher m = Pattern.compile("\\Q" + formatString + "\\E").matcher(this.format);
		while (m.find()) numOfOperands++;
		if ((operandsTypes == null && numOfOperands != 0) || (operandsTypes.length != numOfOperands))
			throw new IllegalArgumentException("Il numero di operandi specificato differisce dalle occorrenze del carattare speciale " + this.format);
	}

	/**
	 * @return La rappresentazione SQL di questo operatore
	 * con i suoi operandi. Il risultato si basa sul formato 
	 * {@link RelationalOperator#getFormat()} e sul carattere
	 * <i>jolly</i> {@link RelationalOperator#getFormatString()}.
	 * In pratica, la rappresentazione SQL corrispondera al
	 * formato, previa sostituzione di ogni occorrenza del carattere
	 * <i>jolly</i> con la chiamata a {@link ValueExpression#toString()}
	 * dell'operando corrispondente.
	 * @throws IllegalStateException
	 * Nel caso in cui non siano stati valorizzati il giusto numero 
	 * di operandi, ovvero il numero indicato da {@link RelationalOperator#getNumOfOperands()}.
	 */
	public String toString() throws IllegalStateException
	{
		Matcher m = Pattern.compile("\\Q" + formatString + "\\E").matcher(this.format);
		StringBuffer sb = new StringBuffer();
		try
		{
			EnumsRepository.ValueType value = null;
			int i = 0;
			while (m.find())
			{
				ValueExpression operand = operands[i++];
				if (value == null)
					value = operand.getValueType();
				else
					operand.setValueType(value);
				m.appendReplacement(sb, operand.toString());
			}
			m.appendTail(sb);
			if (i < operandsTypes.length)
				throw new IllegalStateException();
		}
		catch (NullPointerException npe)
		{
			throw new IllegalStateException();
		}
		catch (IndexOutOfBoundsException ioobe)
		{
			throw new IllegalStateException();			
		}
		return sb.toString();
	}

	public String getName()
	{
		return this.name;
	}

	/**
	 * @return Un'<i>array</i> degli operandi accettati
	 * da questo operatore, con gli <i>items</i> dell'<i>array</i>
	 * Non inizializzati. 
	 * Per settare gli operandi, assegnare ad ogni posizione
	 * dell'<i>array</i> un valore 
	 */
	public ValueExpression[] getOperands()
	{
		if (operands == null)
			operands = new ValueExpression[getNumOfOperands()];
		return operands;
	}
	
	/**
	 * Restituisce il numero di operandi accettati da questo operatore
	 * @return
	 */
	public int getNumOfOperands()
	{
		return numOfOperands;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	public List<Element> getXml()
	{
		List<Element> result = new ArrayList<Element>();
		Element operator = new Element("operator");
		if (getUIKey() != null && !"".equals(getUIKey().trim()))
			operator.setAttribute(new Attribute("ui_key",getUIKey()));
		Element name = new Element("name");
		name.setText(this.name);
		operator.addContent(name);
		for (ValueExpression item : getOperands())
			operator.addContent(ValueExpression.getOperandXml(item));
		result.add(operator);
		return result;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getStringXml()
	 */
	public String getStringXml()
	{
		StringWriter result = new StringWriter();
		XMLOutputter out = new XMLOutputter();
		// out.setIndentSize(3); // DEPRECATO. SERVIVA SOLO PER MOTIVI ESTETICI
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
	
	/**
	 * Clona questo operatore, clonando ad uno ad uno anche
	 * i suoi argomenti.
	 * @throws IllegalStateException
	 * Se non tutti gli operandi sono stati valorizzati
	 */
	public RelationalOperator clone() 
	throws CloneNotSupportedException, IllegalStateException
	{
		RelationalOperator clone = (RelationalOperator) super.clone();
		if (operands != null)
		{
			clone.operands = new ValueExpression[operands.length];
			try	{for (int i = 0; i < operands.length; clone.operands[i] = operands[i++].clone());}
			catch (NullPointerException e) {throw new IllegalStateException(e);}
		}
		return clone;
	}

	
	// GETTERS AND SETTERS //////////////////////////////////////////
	public Class<? extends ValueExpression>[] getOperandsTypes()
	{
		return operandsTypes;
	}

	public void setOperandsTypes(Class<? extends ValueExpression>[] operandsTypes)
	{
		this.operandsTypes = operandsTypes;
	}

	public String getFormat()
	{
		return format;
	}

	public String getFormatString()
	{
		return formatString;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	public void setFormatString(String formatString)
	{
		this.formatString = formatString;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setNumOfOperands(int numOfOperands)
	{
		this.numOfOperands = numOfOperands;
	}

	public void setOperands(ValueExpression[] operands)
	{
		this.operands = operands;
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

	public String[] getXmlValueTypes() {
		return xmlValueTypes;
	}

	public void setXmlValueTypes(String[] xmlValueTypes) {
		this.xmlValueTypes = xmlValueTypes;
	}
	
}
