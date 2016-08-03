package it.webred.diogene.sqldiagram.test;

import java.io.IOException;

import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.EnumsRepository;
import it.webred.diogene.sqldiagram.TableExpression;
import junit.framework.Assert;
import junit.framework.TestCase;

public class ColumnTest extends TestCase implements DefaultTests
{
	public ColumnTest(String name)
	{
		super(name);
	}
	
	public void testCreation()
	{
		new Column();
		//new Column(Long.valueOf(659765L), "PIPPO", "descrizione", new TableExpression("PAPEROPOLI", "description"), EnumsRepository.ValueType.STRING);
	}
	
	public void testStringRepresentation()
	{
		Column c = null;
		//c = new Column("PIPPO", "", "PAPEROPOLI");
		Assert.assertEquals("PAPEROPOLI.PIPPO", c.toString().trim());
		//c = new Column(Long.valueOf(659765L), "PIPPO", "", new TableExpression("PAPEROPOLI", null), EnumsRepository.ValueType.STRING);
		Assert.assertEquals("PAPEROPOLI.PIPPO", c.toString().trim());
		c.getXml();
		c.getStringXml();
	}
}
