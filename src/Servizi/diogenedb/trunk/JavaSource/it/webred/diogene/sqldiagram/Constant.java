/**
 * 
 */
package it.webred.diogene.sqldiagram;

import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;


/**
 * Rappresenta una costante SQL, come SYSDATE o NULL
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class Constant extends ValueExpression
{
	/**
	 * @param exp
	 * Il nome della costante
	 * @param des
	 * Un'eventuale descrizione
	 */
	public Constant(String exp, String des, ValueType type) {
		super(exp, des);
		setValueType(type);
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ValueExpression#getDefinition()
	 */
	public String getDefinition() {
		return description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Constant clone() throws CloneNotSupportedException
	{
		Constant clone = (Constant) super.clone();
		return clone;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	public List<Element> getXml()
	{
		List<Element> result = new ArrayList<Element>();
		
		Element constant = new Element("constant");
		if (getUIKey() != null && !"".equals(getUIKey().trim()))
			constant.setAttribute(new Attribute("ui_key",getUIKey()));
		Element description = new Element("description");
		Element name = new Element("name");
		constant.addContent(name);
		constant.addContent(description);
		
		name.setText(super.expression);
		description.setText(super.description);

		result.add(constant);
		result.addAll(getAppendedXml());		
		return result;
	}
}
