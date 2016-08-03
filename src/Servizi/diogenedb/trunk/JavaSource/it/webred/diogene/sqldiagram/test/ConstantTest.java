package it.webred.diogene.sqldiagram.test;

import it.webred.diogene.sqldiagram.Constant;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import junit.framework.Assert;
import junit.framework.TestCase;

public class ConstantTest extends TestCase implements DefaultTests
{
	public void testCreation()
	{
		new Constant("*", null, ValueType.UNDEFINED);
		new Constant("*", "", ValueType.UNDEFINED);
	}
	public void testStringRepresentation()
	{
		Constant c = null;
		c = new Constant("PIPPO", "", ValueType.UNDEFINED);
		Assert.assertEquals("PIPPO", c.toString().trim(), ValueType.UNDEFINED);
		c.getXml();
		c.getStringXml();
	}
}
