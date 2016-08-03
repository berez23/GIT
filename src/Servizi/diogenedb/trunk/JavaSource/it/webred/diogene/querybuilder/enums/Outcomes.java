package it.webred.diogene.querybuilder.enums;

public enum Outcomes
{
	SUCCESS {public String outcome() {return "success";}},
	EXPLICIT_SQL {public String outcome() {return "explicitSql";}},
	FAILURE {public String outcome() {return "failure";}};
	
	public abstract String outcome();
}
