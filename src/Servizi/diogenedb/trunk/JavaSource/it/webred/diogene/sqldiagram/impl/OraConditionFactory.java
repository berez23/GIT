/**
 * 
 */
package it.webred.diogene.sqldiagram.impl;

import it.webred.diogene.sqldiagram.Condition;
import static it.webred.utils.StringUtils.*;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
/**
 * Implementazione per DBMS Oracle della
 * factory delle condizioni.
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class OraConditionFactory implements it.webred.diogene.sqldiagram.ConditionFactory
{
	private final Logger log = Logger.getLogger(this.getClass());

	private static URI dtdURI = null;
	private static String xmlHeader = null;
	private static String xmlFooter = null;
	static
	{
		// TODO prendere da  fileseeker 
		try
		{
			dtdURI = OraConditionFactory.class.getClassLoader().getResource("condition.dtd").toURI();
		}
		catch (URISyntaxException e)
		{
			Logger.getLogger(OraConditionFactory.class).error("", e);
		}
		//dtdURI = new File("condition.dtd").toURI();
		xmlHeader = "<?xml version=\"1.0\" standalone=\"no\"?>" + CRLF +"<!DOCTYPE condition SYSTEM \"" + dtdURI.toString() + "\">" + CRLF + "<condition>" + CRLF;
		xmlFooter =  CRLF + "</condition>";
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ConditionFactory#getCondition()
	 */
	public Condition getCondition()
	{
		return new OraCondition();
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ConditionFactory#validateConditionXML(java.lang.String)
	 */
	public boolean validateConditionXML(String xml)
	{
		try
		{
			xml = xmlHeader + xml + xmlFooter;
			SAXBuilder builder = new SAXBuilder();
			builder.build(new StringReader(xml));
			return true;
		}
		catch (JDOMException e) {}
		catch (IOException e) {}
		return false;
	}
}
