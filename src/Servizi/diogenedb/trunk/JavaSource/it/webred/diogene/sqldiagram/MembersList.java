package it.webred.diogene.sqldiagram;

import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Questa classe rappresenta una lista di espressioni SQL:
 * i suoi contesti naturali sono:
 * <ol>
 * 	<li>Come secondo argomento dell'operatore di relazione IN</li>
 * 	<li>Come lista di argomenti di una funzione</li>
 * </ol>
 * @see it.webred.diogene.sqldiagram.Membership
 * @see it.webred.diogene.sqldiagram.Function
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class MembersList extends Membership
{
	private List<ValueExpression> expressions = null;
	
	public MembersList() {
		super();
		expressions = new ArrayList<ValueExpression>();
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ValueExpression#getDefinition()
	 */
	public String getDefinition() {
		return "Lista di valori";
	}

	/**
	 * @return La rappresentazione SQL della lista delle
	 * espressioni, ovvero una concatenazione
	 * dei <code>toString()</code> delle {@link ValueExpression}
	 * di cui e composta questa classe, separati da virgole.
	 */
	@Override
	public String toString()
	{
		String result = "";
		for (ValueExpression expr : expressions)
		{
			result += expr.toString() + ",";
		}
		if (result.length() > 0)
			result = result.substring(0, result.length()-1);
		
		return result;
	}
	
	/**
	 * @param ex
	 */
	public void add(ValueExpression ex)
	{
		expressions.add(ex);
	}

	/**
	 * @return
	 */
	public List<ValueExpression> getExpressions()
	{
		return expressions;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public MembersList clone() throws CloneNotSupportedException
	{
		MembersList clone = (MembersList) super.clone();
		if (clone.getExpressions() != null)
			for (int i = 0; i < clone.getExpressions().size(); clone.getExpressions().set(i, clone.getExpressions().get(i++).clone()));
		return clone;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	public List<Element> getXml()
	{
		List<Element> result = new ArrayList<Element>();
		
		Element operands_list = new Element("operands_list");
		if (getUIKey() != null && !"".equals(getUIKey().trim()))
			operands_list.setAttribute(new Attribute("ui_key",getUIKey()));

		if (expressions != null)
			for (ValueExpression item : expressions)
			{
				Element operand = new Element("operand");
				operands_list.addContent(operand);
				List<Element> expr = item.getXml();
				for (Element item2 : expr)
					operand.addContent(item2);
				operand.setAttribute("data_type", ValueType.getXmlDataTypeAttribute(item.getValueType()));
			}
		
		Element description = new Element("description");
		description.setText(this.description);
		
		result.add(operands_list);
		return result;
	}
}
