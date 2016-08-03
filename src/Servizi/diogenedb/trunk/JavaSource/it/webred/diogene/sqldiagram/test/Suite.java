package it.webred.diogene.sqldiagram.test;

import java.util.Enumeration;

import org.apache.log4j.Logger;

import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class Suite extends TestSuite
{
	private final Logger log = Logger.getLogger(this.getClass());

	public void go()
	{
		addTestSuite(ColumnTest.class);
		addTestSuite(ConstantTest.class);
		addTestSuite(FunctionTest.class);
		addTestSuite(LiteralExpressionTest.class);

		
		TestResult result = new TestResult();
		run(result);
		
		if (result.wasSuccessful())
			log.debug("Il test ha avuto successo");
		else
		{
			log.error("Ci sono dei problemi... ");			
			log.error("");
			log.error("");
			log.error(result.errorCount() + " eccezioni ");

			Enumeration errors = result.errors();
			while (errors.hasMoreElements())
			{
				TestFailure failure = (TestFailure) errors.nextElement();
				log.error(failure.exceptionMessage());
				failure.thrownException().getCause().printStackTrace();
				
			}

			log.error("");
			log.error(result.failureCount() + " errori ");
			Enumeration failures = result.failures();
			while (failures.hasMoreElements())
			{
				TestFailure failure = (TestFailure) failures.nextElement();
				log.error(failure.failedTest());
				//log.error(failure.trace());
			}
		}
		
	}
	public static void main(String[] args)
	{
		new Suite().go();
	}
}
