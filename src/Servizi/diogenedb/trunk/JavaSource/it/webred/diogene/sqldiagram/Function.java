package it.webred.diogene.sqldiagram;

import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import static it.webred.utils.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;


/**
 * Questa classe rappresenta una funzione SQL
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.2 $ $Date: 2007/11/22 15:59:57 $
 */
public class Function extends ValueExpression implements XMLRepresentable
{
	private String sqlName;
	private String postPend;
	private boolean lastRequired = true;
	private boolean lastInfinite = false;
	boolean aggregationFunction = false;
	private ValueType returnedType = null;
	private ValueType[] argumentsType = null;
	int actualArgument = 1;
	private MembersList arguments = null;
	
	/**
	 * @param name
	 * Il nome che identifica univocamente questa funzione, utilizzato per richiamarla passandolo
	 * al metodo 
	 * {@link it.webred.diogene.sqldiagram.ExpressionFactory#getExpression(String expression) ExpressionFactory.getExpression(String name)}
	 * @param sqlName
	 * Il nome SQL della funzione
	 * @param postPend
	 * Una eventuale appendice alla parentesi di chiusura della funzione
	 * @param returnedType
	 * Il tipo restituito da questa funzione
	 * @param aggregationFunction
	 * Definisce se la funzione che si sta creando &egrave;
	 * una funzione di aggregazione (tipo AVG()) o no.
	 * @param lastRequired
	 * Stabilisce se l'ultimo degli argomenti argumentsType passati 
	 * sia o no opzionale
	 * @param lastInfinite
	 * Stabilisce se l'ultimo degli argomenti argumentsType passati 
	 * possa essere una lista di argomenti di quel tipo
	 * @param argumentsType
	 * La lista ordinata degli argomenti, specificata secondo il loro tipo
	 * ValueType, accettati da questa funzione
	 */
	public Function(
			String name, 
			String sqlName,
			String postPend,
//			String description,
			ValueType returnedType,
			boolean aggregationFunction,
			boolean lastRequired,
			boolean lastInfinite,
			ValueType... argumentsType)
	{
		super(name, "");
		this.sqlName = sqlName;
		this.postPend = postPend;
		this.aggregationFunction = aggregationFunction;
		this.lastRequired = lastRequired;
		this.lastInfinite = lastInfinite;
		this.returnedType = (returnedType != null) ? returnedType : ValueType.ANY;
		setValueType(this.returnedType);
		this.argumentsType =  (argumentsType != null && argumentsType.length > 0) ? argumentsType : null;
		//this.description = description;
	}
	
	/**
	 * @return La rappresentazione SQL corretta per questa funzione
	 */
	public String toString()
	{
		String result = "";
		result += sqlName + "(";
		if (arguments != null)
			result += arguments.toString();
		result += ")";
		if (!isEmpty(postPend))
			result += postPend;

		return result + appendedToString();
	}
	
	/**
	 * Aggiunge una espressione agli argomenti di questa funzione
	 * @param ex
	 * @throws IllegalStateException
	 * Quando si tenti di aggiungere pi&ugrave; argomenti di
	 * quanto specificato da {@link Function#getMaxNumOfArgs()}
	 */
	public void addArgument(ValueExpression ex)
	throws IllegalStateException
	{
		if (actualArgument > getMaxNumOfArgs())
			throw new IllegalStateException("Questa funzione non accetta più argomenti");
		if (arguments == null)
			arguments = new MembersList();
		arguments.add(ex);
		ValueType type = argumentsType[((actualArgument == argumentsType.length) ? (argumentsType.length) : (actualArgument++)) - 1];
		if (!type.equals(ValueType.ANY))
			ex.setValueType(type);
	}
	/**
	 * @return Il numero minimo di argomenti da passare a questa funzione
	 */
	public int getMinNumOfArgs()
	{
		if (argumentsType == null)
			return 0;
		if (lastRequired)
			return argumentsType.length;
		return (argumentsType.length - 1);
	}
	/**
	 * @return Il numero massimo di argomenti da passare 
	 * a questa funzione
	 */
	public int getMaxNumOfArgs()
	{
		if (argumentsType == null)
			return 0;
		if (lastInfinite)
			return Integer.MAX_VALUE;
		return argumentsType.length;
	}

	/**
	 * Definisce la lista completa degli argomenti da passare a questa
	 * funzione
	 * @param arguments
	 * @throws IllegalStateException
	 * Quando si tenti di settare pi&ugrave; argomenti di
	 * quanto specificato da {@link Function#getMaxNumOfArgs()}
	 */
	public void setArguments(MembersList arguments)
	throws IllegalStateException
	{
		if (arguments.getExpressions().size() > getMaxNumOfArgs())
			throw new IllegalStateException("Questa funzione non accetta più argomenti");
		actualArgument = getMaxNumOfArgs() + 1;
		this.arguments = arguments;
	}
	/**
	 * @return Gli argomenti di questa funzione
	 */
	public MembersList getArguments()
	{
		return this.arguments;
	}
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	@Override
	public List<Element> getXml()
	{
		List<Element> result = new ArrayList<Element>();
		Element function = new Element("function");
		if (getUIKey() != null && !"".equals(getUIKey().trim()))
			function.setAttribute(new Attribute("ui_key",getUIKey()));
		Element name = new Element("name");
		name.setText(this.expression);
		function.addContent(name);
		for (Element item : arguments.getXml())
			function.addContent(item);
		function.setAttribute("data_type", ValueType.getXmlDataTypeAttribute(getValueType()));
		
		result.add(function);
		result.addAll(getAppendedXml());
		return result;
	}

	/**
	 * La funzione non si limita a chiamare 
	 * <code>super.clone()</code>, ma clona anche ad uno
	 * ad uno i suoi argomenti.
	 * @see MembersList#clone()
	 */
	@Override
	public Function clone() throws CloneNotSupportedException
	{
		Function clone = (Function) super.clone();
		if (arguments != null)
			clone.setArguments(arguments.clone());
		return clone;
	}

	/**
	 * @return I tipi ai quali devono conformarsi
	 * gli argomenti della funzione, nell'ordine
	 */
	public EnumsRepository.ValueType[] getArgumentsType()
	{
		return argumentsType;
	}

	public boolean isAggregationFunction()
	{
		return aggregationFunction;
	}

	public void setAggregationFunction(boolean aggregationFunction)
	{
		this.aggregationFunction = aggregationFunction;
	}
}
