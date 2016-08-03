/**
 * 
 */
package it.webred.diogene.sqldiagram;


/**
 * TODO Scrivi descrizione
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class SqlGenerator
{
	private String database = "";

	private SqlGenerator() {}
	/**
	 * @param db
	 * Specifica il tipo di DBMS.
	 */
	public SqlGenerator(EnumsRepository.DBType db)
	{
		this();
		this.database = EnumsRepository.DBType.valueOf(EnumsRepository.DBType.class, db.name()).stringValue();
	}


	/**
	 * <p>
	 * <b>Nota di implementazione:</b> L'implementazione viene cercata nel package
	 * di {@link ExpressionFactory} seguito da <code>.impl</code>, e nella classe
	 * il cui nome sia ExpressionFactory preceduto da un prefisso 
	 * corrispondente al {@link java.lang.Enum#name() nome} del
	 * {@link EnumsRepository.DBType} passato al costruttore.
	 * </p>
	 * @return La factory delle espressioni specifica del
	 * DBMS specificato al costruttore.
	 * @see SqlGenerator#SqlGenerator(EnumsRepository.DBType) 
	 * @throws Exception
	 */
	public ExpressionFactory getExpressionFactory() throws Exception
	{
		String expFstring = ExpressionFactory.class.getPackage().getName() + ".impl." + database + "ExpressionFactory";
		try
		{
			ExpressionFactory expF = (ExpressionFactory) Class.forName(expFstring).newInstance();
			return expF;
		}
		catch (Exception e)
		{
			throw new Exception("Impossibile reperire expression factory " + expFstring);
		}
	}

	/**
	 * <p>
	 * <b>Nota di implementazione:</b> L'implementazione viene cercata nel package
	 * di {@link OperatorFactory} seguito da <code>.impl</code>, e nella classe
	 * il cui nome sia OperatorFactory preceduto da un prefisso 
	 * corrispondente al {@link java.lang.Enum#name() nome} del
	 * {@link EnumsRepository.DBType} passato al costruttore.
	 * </p>
	 * @return La factory degli operatori specifica del
	 * DBMS specificato al costrutture
	 * @see SqlGenerator#SqlGenerator(EnumsRepository.DBType) 
	 * @throws Exception
	 */
	public OperatorFactory getOperatorFactory() throws Exception
	{
		String opFstring = OperatorFactory.class.getPackage().getName() + ".impl." + database + "OperatorFactory";
		try
		{
			OperatorFactory opF = (OperatorFactory) Class.forName(opFstring).newInstance();
			return opF;
		}
		catch (Exception e)
		{
			throw new Exception("Impossibile reperire operator factory " + opFstring);
		}
	}

	/**
	 * <p>
	 * <b>Nota di implementazione:</b> L'implementazione viene cercata nel package
	 * di {@link ConditionFactory} seguito da <code>.impl</code>, e nella classe
	 * il cui nome sia ConditionFactory preceduto da un prefisso 
	 * corrispondente al {@link java.lang.Enum#name() nome} del
	 * {@link EnumsRepository.DBType} passato al costruttore.
	 * </p>
	 * @return La factory delle condizioni specifica del
	 * DBMS specificato al costruttore
	 * @see SqlGenerator#SqlGenerator(EnumsRepository.DBType) 
	 * @throws Exception
	 */
	public ConditionFactory getConditionFactory() throws Exception
	{
		String condFstring = ConditionFactory.class.getPackage().getName() + ".impl." + database + "ConditionFactory";
		try
		{
			ConditionFactory condF = (ConditionFactory) Class.forName(condFstring).newInstance();
			return condF;
		}
		catch (Exception e)
		{
			throw new Exception("Impossibile reperire condition factory " + condFstring);
		}
	}

	/**
	 * Restituisce una query vuota
	 * @return
	 * @throws Exception
	 */
	public Query getQuery() throws Exception
	{
		String queryFstring = Query.class.getPackage().getName() + ".impl." + database + "Query";
		try
		{
			Query query = (Query) Class.forName(queryFstring).newInstance();
			return query;
		}
		catch (Exception e)
		{
			throw new Exception("Impossibile reperire Query " + queryFstring);
		}		
	}
}
