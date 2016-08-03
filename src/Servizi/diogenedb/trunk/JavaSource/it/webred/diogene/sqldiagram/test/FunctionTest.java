package it.webred.diogene.sqldiagram.test;

import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.EnumsRepository;
import it.webred.diogene.sqldiagram.Function;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.TableExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import junit.framework.Assert;
import junit.framework.TestCase;

public class FunctionTest extends TestCase implements DefaultTests
{
	Function f1 = null;
		
	@Override
	protected void setUp() throws Exception
	{
		f1 = new Function(
				"confrontaDate", 
				"DATE_COMPARATOR",
				null,
				ValueType.NUMBER,
				false,
				false,
				false,
				ValueType.DATE,
				ValueType.STRING,
				ValueType.NUMBER);
		//f1.addArgument(new Column(Long.valueOf(659765L), "DATA_NASCITA", null, new TableExpression("ANAGRAFE",""), EnumsRepository.ValueType.DATE));
		f1.addArgument(new LiteralExpression("23/11/1948", "", ValueType.STRING));
	}
	
	public void testCreation() {} // LA CREAZIONE VIENE GIA' PROVATA DA setUp()
	
	public void testStringRepresentation()
	{
		Assert.assertEquals("DATE_COMPARATOR(ANAGRAFE.DATA_NASCITA,'23/11/1948')", f1.toString().trim());
		f1.addArgument(new LiteralExpression("0", "", ValueType.NUMBER));
		Assert.assertEquals("DATE_COMPARATOR(ANAGRAFE.DATA_NASCITA,'23/11/1948',0)", f1.toString().trim());
	}
}
