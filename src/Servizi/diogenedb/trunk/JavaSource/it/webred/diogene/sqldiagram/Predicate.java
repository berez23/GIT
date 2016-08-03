package it.webred.diogene.sqldiagram;

public class Predicate extends Expression
{

	private Predicate() {super();}
	public Predicate(String expression, String description) 
	{
		super(expression, description);
	}

	@Override
	public String toString()
	{
		return getExpression() != null ? getExpression() : "";
	}

}
