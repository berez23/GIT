package it.webred.diogene.sqldiagram.test;

import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.ValueExpressionOperator;
import junit.framework.Assert;
import junit.framework.TestCase;

public class LiteralExpressionTest extends TestCase implements DefaultTests
{
	public void testCreation()
	{
		new LiteralExpression("CIAO", null);
		new LiteralExpression("CIAO", "adflkms");
		new LiteralExpression("CIAO", null, ValueType.ANY);
	}

	public void testStringRepresentation()
	{
		LiteralExpression l = null;
		l = new LiteralExpression("ROSSI", null, ValueType.STRING);
		Assert.assertEquals("'ROSSI'", l.toString().trim());
		l = new LiteralExpression("ROSSI", null, ValueType.NUMBER);
		Assert.assertEquals("ROSSI", l.toString().trim());
	}
}
