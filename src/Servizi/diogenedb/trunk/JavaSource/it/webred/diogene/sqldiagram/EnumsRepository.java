package it.webred.diogene.sqldiagram;

import it.webred.diogene.sqldiagram.impl.OraRelationalOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

public class EnumsRepository
{
	/**
	 * Costanti che definiscono il dialetto SQL, da passare 
	 * a {@link SqlGenerator#SqlGenerator(EnumsRepository.DBType)}
	 * @author Giulio Quaresima
	 */
	public static enum DBType
	{
		ORACLE {public String stringValue() {return "Ora";}},
		MYSQL {public String stringValue() {return "MySql";}},
		MSSQL {public String stringValue() {return "MsSql";}};
		
		public abstract String stringValue();
	}
	
	/**
	 * Costanti che rappresentano il tipo di dato rappresentato
	 * da una {@link ValueExpression}. Il tipo di dato rappresentato &egrave;
	 * in quanto pu&ograve; influenzare il modo in cui certe
	 * espressioni debbano essere rappresentate, emblematico il caso
	 * delle espressioni letterali, che se di tipo {@link ValueType#STRING}
	 * vengono quotate, vedi {@link LiteralExpression#toString()}.
	 * Inoltre, si possono utilizzare i tipi per vincolare i contesti
	 * nei quali certe {@link ValueExpression} possano apparire, 
	 * vedi {@link Function#Function(String, String, String, EnumsRepository.ValueType, boolean, boolean, EnumsRepository.ValueType[])}
	 * e
	 * {@link ValueExpressionOperator#ValueExpressionOperator(String, String, EnumsRepository.ValueType[])}
	 * @author Giulio Quaresima
	 */
	public enum ValueType
	{
		/**
		 * Tipo indefinito. Andrebbe usato quando il tipo &egrave; 
		 * sconosciuto.
		 */
		UNDEFINED {public String getResourceKey() {return this.getClass().getPackage().getName() + ".ValueType" + "." + this.name();} },
		/**
		 * Qualsiasi tipo. Andrebbe usato per indicare che in un
		 * certo contesto, quale ad esempio un argomento di funzione SQL,
		 * &egrave; accettabile qualsiasi tipo
		 */
		ANY {public String getResourceKey() {return this.getClass().getPackage().getName() + ".ValueType" + "." + this.name();} },
		/**
		 * Tipo numerico SQL generico
		 */
		NUMBER {public String getResourceKey() {return this.getClass().getPackage().getName() + ".ValueType" + "." + this.name();} },
		/**
		 * Tipo stringa SQL generico
		 */
		STRING {public String getResourceKey() {return this.getClass().getPackage().getName() + ".ValueType" + "." + this.name();} },
		/**
		 * Tipo data SQL generico
		 */
		DATE {public String getResourceKey() {return this.getClass().getPackage().getName() + ".ValueType" + "." + this.name();} };
		
		/** 
		 * Restituisce una chiave stringa univoca
		 * per il ValueType corrispondente. Da utilizzare,
		 * tipicamente, per ottenere una descrizione
		 * del tipo tramite un {@link java.util.ResourceBundle ResourceBundle}
		 * @return
		 */
		public abstract String getResourceKey();
		
		
		/**
		 * Restituisce il ValueType che corrisponde meglio
		 * al tipo Java passato col suo <i>Full Qualified Name</i>,
		 * ovvero il nome che può essere passato al metodo
		 * {@link Class#forName(java.lang.String) Class.forName}
		 * @param javaType
		 * @return
		 */
		public static ValueType mapJavaType2ValueType(String javaType)
		{
			try
			{
				Class cls = Class.forName(javaType.trim());
				return mapJavaType2ValueType(cls);
			}
			catch (ClassNotFoundException e)
			{
				return UNDEFINED;
			}
		}
		/**
		 * Restituisce il ValueType che corrisponde meglio
		 * al tipo Java passato.
		 * @param javaType
		 * @return
		 */
		public static ValueType mapJavaType2ValueType(Class javaType)
		{
			if (CharSequence.class.isAssignableFrom(javaType))
				return STRING;
			if (Number.class.isAssignableFrom(javaType))
				return NUMBER;
			if (Date.class.isAssignableFrom(javaType))
				return DATE;
			return UNDEFINED;
		}
		
		/**
		 * @param valueType
		 * @return
		 * Il Java Type di default corrispondente
		 * al valore passato
		 */
		public static String mapValueType2JavaType(ValueType valueType)
		{
			if (STRING.equals(valueType))
				return "java.lang.String";
			if (NUMBER.equals(valueType))
				return "java.math.BigDecimal";
			if (DATE.equals(valueType))
				return "java.sql.Date";
			return "java.lang.String";
		}
		
		/**
		 * Resitituisce il {@link it.webred.diogene.sqldiagram.EnumsRepository.ValueType ValueType}
		 * appropriato per il tipo name come specificato nel file condition.dtd
		 * @param name
		 * @return
		 */
		public static ValueType getValueType(String name)
		{
			if (name == null)
				return ValueType.ANY;
			else if (name.equals("string"))
				return ValueType.STRING;
			else if (name.equals("number"))
				return ValueType.NUMBER;
			else if (name.equals("date"))
				return ValueType.DATE;
			return ValueType.ANY;
		}
		
		/**
		 * Restituisce la stringa utilizzata come attributo data_type
		 * nelle rappresentazioni XML delle ValueExpression
		 * @param vt
		 * @return
		 */
		public static String getXmlDataTypeAttribute(ValueType vt)
		{
			if (ValueType.NUMBER.equals(vt))
				return "number";
			else if(ValueType.STRING.equals(vt))
				return "string";
			else if(ValueType.DATE.equals(vt))
				return "date";
			else
				return "any";
		}
		
		/**
		 * Restituisce la lista di SelectItem dei possibili operatori di relazione del valueType fornito
		 * 
		 * @param valueType il ValueType del quale fornire la lista degli operatori
		 * @param includeNullOperators se true include anche gli operatori Null not null
		 * @return
		 */
		public static LinkedList<SelectItem> getRelationalOperatorsList(ValueType valueType, String ...excudedOperators )
		{
			LinkedList<SelectItem> lstOperatori = new LinkedList<SelectItem>();

			List<String> esclusi=Arrays.asList( excudedOperators);
			if(!esclusi.contains(RelationalOperatorType.EQUAL))lstOperatori.add(new SelectItem(RelationalOperatorType.EQUAL,"Uguale","Uguale"));
			if (ValueType.NUMBER.equals(valueType)){
				// aggiungere eventuali operatori per il tipo NUMBER
				if(!esclusi.contains(RelationalOperatorType.GREATER))lstOperatori.add(new SelectItem(RelationalOperatorType.GREATER,"Maggiore","Maggiore"));
				if(!esclusi.contains(RelationalOperatorType.LESSER))lstOperatori.add(new SelectItem(RelationalOperatorType.LESSER,"Minore","Minore"));
			}
			else if(ValueType.STRING.equals(valueType)){
				// aggiungere eventuali operatori per il tipo STRING
				if(!esclusi.contains(RelationalOperatorType.LIKE))lstOperatori.add(new SelectItem(RelationalOperatorType.LIKE,"Contiene","Contiene"));
			}
			else if(ValueType.DATE.equals(valueType)){
				// aggiungere eventuali operatori per il tipo date
				if(!esclusi.contains(RelationalOperatorType.GREATER))lstOperatori.add(new SelectItem(RelationalOperatorType.GREATER,"Maggiore","Maggiore"));
				if(!esclusi.contains(RelationalOperatorType.LESSER))lstOperatori.add(new SelectItem(RelationalOperatorType.LESSER,"Minore","Minore"));
			}

			if(!esclusi.contains(RelationalOperatorType.BETWEEN))lstOperatori.add(new SelectItem(RelationalOperatorType.BETWEEN,"Compreso","Compreso"));
			if(!esclusi.contains(RelationalOperatorType.NOTBETWEEN))lstOperatori.add(new SelectItem(RelationalOperatorType.NOTBETWEEN,"Non Compreso","Non Compreso"));
			if(!esclusi.contains(RelationalOperatorType.ISNULL))lstOperatori.add(new SelectItem(RelationalOperatorType.ISNULL,"Nullo","Nullo"));
			if(!esclusi.contains(RelationalOperatorType.ISNOTNULL))lstOperatori.add(new SelectItem(RelationalOperatorType.ISNOTNULL,"Non Nullo","Non Nullo"));
			if(!esclusi.contains(RelationalOperatorType.NOTEQUAL))lstOperatori.add(new SelectItem(RelationalOperatorType.NOTEQUAL,"Diverso","Diverso"));
			if(!esclusi.contains(RelationalOperatorType.IN))lstOperatori.add(new SelectItem(RelationalOperatorType.IN,"Uguale a (Insieme)","Uguale a (Insieme)"));
			
			
			lstOperatori.removeAll(Arrays.asList(excudedOperators));
			return lstOperatori;
		}
	}

}
