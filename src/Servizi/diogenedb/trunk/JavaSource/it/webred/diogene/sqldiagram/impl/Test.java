package it.webred.diogene.sqldiagram.impl;
/*
import it.webred.diogene.sqldiagram.*;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
*/
public class Test
{
	public static void main(String[] args)
	throws Exception
	{
		/*
		SqlGenerator sqlGen = new SqlGenerator(EnumsRepository.DBType.ORACLE);
		
		OperatorFactory   oF = sqlGen.getOperatorFactory();
		ExpressionFactory eF = sqlGen.getExpressionFactory();
		ConditionFactory  cF = sqlGen.getConditionFactory();

		log.debug("OPERATORI DI ORACLE");
		log.debug("Operatori logici:\r\n\t");
		for (LogicalOperator item : oF.getLogicalOperators())
			log.debug(item.getName() + ", ");
		log.debug("");
		log.debug("Operatori relazionali:\r\n\t");
		for (RelationalOperator item : oF.getOperators())
			log.debug(item.getName() + ", ");
		log.debug("");
		log.debug("Operatori di espressione:\r\n\t");
		for (ValueExpressionOperator item : oF.getValueExpressionOperators())
			log.debug(item.getName() + ", ");
		log.debug("");
		log.debug("");
		
		log.debug("ESEMPIO D'USO:\r\n");
		ValueExpressionOperator  veo = null;
		EnumsRepository.ValueType STRINGA = ValueType.mapJavaType2ValueType("".getClass());
		EnumsRepository.ValueType NUMERO = ValueType.mapJavaType2ValueType(Integer.class);
		EnumsRepository.ValueType QUALSIASI = ValueType.ANY;
		LogicalOperator AND = oF.getLogicalOperator("and");
		LogicalOperator OR = oF.getLogicalOperator("or");
		
		// CREO UN NUOVO OGGETTO QUERY VUOTO
		Query statement = new Query();
		
		/*
		// AGGIUNGO UNA TABELLA ALLA FROM E UN'ESPRESSIONE ALLA SELECT
		Table clienti = new TableExpression("CLIENTI", "Tabella dei clienti");
		veo = oF.getValueExpressionOperator("stringConcat");
		ValueExpression ve = new Column(Long.valueOf(659765L), "NOME", "Il nome del cliente", clienti, STRINGA);
		ve.appendExpression(veo, new LiteralExpression(", ", "", STRINGA));
		ve.appendExpression(veo, new Column(Long.valueOf(659765L), "COGNOME", "Il cognome del cliente", clienti, STRINGA));
		statement.addSelectExpression(ve);
		statement.addFromTable(clienti);

		// AGGIUNGO UN'ALTRA TABELLA ALLA FROM E DUE ESPRESSIONI ALLA SELECT
		Table contribuenti = new TableExpression("CONTRIBUENTI", "Tabella dei contribuenti");
		ValueExpression ve2 = eF.getExpression("decode");
		((Function) ve2).addArgument(new Column(Long.valueOf(659765L), "COD_FISC", "Codice fiscale", contribuenti, STRINGA));
		((Function) ve2).addArgument(eF.getExpression("null"));
		((Function) ve2).addArgument(new LiteralExpression(" - ", "", STRINGA));
		((Function) ve2).addArgument(new LiteralExpression("", "", STRINGA));
		((Function) ve2).addArgument(new LiteralExpression(" - ", "", STRINGA));		
		((Function) ve2).addArgument(new Column(Long.valueOf(659765L), "COD_FISC", "Codice fiscale", contribuenti, STRINGA));
		statement.addSelectExpression(ve2);
		statement.addFromTable(contribuenti);
		ValueExpression ve3 = new Column(Long.valueOf(659765L), "CODICE_CONTRIBUENTE", "Il codice contribuente del cliente", contribuenti, NUMERO);
		statement.addSelectExpression(ve3);

		// CREO UNA CONDIZIONE E LA AGGIUNGO ALLA QUERY
		Condition cond = cF.getCondition();
		RelationalOperator ro = oF.getOperator("equal");
		//ro.getOperands()[0] = ve2;
		ro.getOperands()[0] = new Column(Long.valueOf(659765L), "CODICEFISCALE", "Codice fiscale cliente", clienti, STRINGA);
		ro.getOperands()[1] = new Column(Long.valueOf(659765L), "COD_FISC", "Codice fiscale", contribuenti, STRINGA);
		cond.addSimpleCondition(ro, AND);
		Condition childCond = cF.getCondition();
		ro = oF.getOperator("between");
		Column eta = new Column(Long.valueOf(659765L), "ETA", "Et\u00E0 cliente", clienti, NUMERO);
		ValueExpression[] operands = ro.getOperands();
		operands[0] = eta;
		operands[1] = new LiteralExpression("18", "", NUMERO);
		operands[2] = new LiteralExpression(new Integer(65).toString(), "", NUMERO);
		childCond.addSimpleCondition(ro, OR);
		ro = oF.getOperator("isNull");
		ro.getOperands()[0] = eta;
		childCond.addSimpleCondition(ro, OR);
		cond.addCondition(childCond, AND);
		statement.setWhereCondition(cond);
		
		// STAMPO IL RISULTATO
		log.debug(statement);
		log.debug("");
		log.debug("Rappresentazione XML della Condition: ");
		log.debug(cond.getStringXml());
		
		Query greaterStatement = new Query("QUERY_DI_QUERY", "QueryComplessa");
		greaterStatement.addSelectExpression(eF.getExpression("*"));
		greaterStatement.addFromTable(statement);
		Condition c = cF.getCondition();
		ro = oF.getOperator("exists");
		ro.getOperands()[0] = statement.clone();
		c.addSimpleCondition(ro, AND);
		greaterStatement.setWhereCondition(c);
		
		// STAMPO IL RISULTATO
		log.debug("");
		log.debug("");
		log.debug(greaterStatement.toString());
		*/
	}
}
