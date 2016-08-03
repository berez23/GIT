package it.webred.diogene.querybuilder.control;

import static it.webred.diogene.querybuilder.Constants.FUNCTION;
import static it.webred.diogene.querybuilder.Constants.LITERAL;
import static it.webred.diogene.sqldiagram.EnumsRepository.ValueType.ANY;
import static it.webred.diogene.sqldiagram.EnumsRepository.ValueType.UNDEFINED;
import it.webred.diogene.querybuilder.QueryBuilderMessage;
import it.webred.diogene.querybuilder.control.ValueExpressionEditor.FunctionDetail.FunctionArgument;
import it.webred.diogene.sqldiagram.ExplicitSqlExpression;
import it.webred.diogene.sqldiagram.Function;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.MembersList;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import it.webred.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.component.html.HtmlInputText;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 * Questo oggetto permette di editare visualmente
 * una {@link it.webred.diogene.sqldiagram.ValueExpression},
 * e viene utilizzato nelle pagine entities.jsp e
 * conditionsBuilder.jsp.
 * @author Giulio Quaresima
 * @version $Revision: 1.2 $ $Date: 2007/11/22 15:59:57 $
 */
public class ValueExpressionEditor implements Switches
{
	public static final String       LITERAL_ID = "literalExpression";
	public static final String        ATOMIC_ID = "atomicExpression";
	public static final String      FUNCTION_ID = "functionExpression";
	public static final String  EXPLICIT_SQL_ID = "explicitSQLExpression";
	
	private Locale locale;
	private ObjectEditor<ValueExpression> value;
	private String key;
	private String literal;
	private String explicitSQL;
	private String 
		valueExpressionSelectedTab,
		choosedFunction;
	
	private FunctionDetail functionDetail;
	private ValueExpressionsSource expressionsSource;
	private MessagesHolder messagesHolder;
	
	private boolean editable;
	
	/**
	*	@param locale
	*	@param expressionsSource
	*	@param messagesHolder
	*/
	public ValueExpressionEditor(
			Locale locale,
			ValueExpressionsSource expressionsSource,
			MessagesHolder messagesHolder)
	{
		this.locale = locale;
		this.expressionsSource = expressionsSource;
		this.messagesHolder = messagesHolder;
	}
	
	// CLASSES ////////////////////////////////////////////////////////
	public class FunctionDetail
	{
		Function f;
		List<FunctionArgument> arguments;
		
		/**
		 * Funzione che permette di editare visualmente il
		 * dettaglio di una funzione.
		 * @author Giulio Quaresima
		 * @version $Revision: 1.2 $ $Date: 2007/11/22 15:59:57 $
		 */
		/**
		 * TODO Scrivi descrizione
		 * @author Giulio Quaresima
		 * @version $Revision: 1.2 $ $Date: 2007/11/22 15:59:57 $
		 */
		public class FunctionArgument
		{
			private String key;
			private String literal;
			private int position;
			
			/**
			*	@param position
			* La posiizione dell'argomento di funzione.
			*/
			public FunctionArgument(int position)
			{
				this.position = position;
			}
			
			// ACTION LISTENERS //////////////////////////////////////
			/**
			 * Elimina questo argomento.
			 * @param e
			 */
			public void deleteMe(ActionEvent e)
			{
				if (isAnyArgumentDeleteable())
				{
					getArguments().remove(getPosition());
					arrangeArgumentIndexes();
				}
			}
			/**
			 * Aggiunge un nuovo argomento sopra a questo
			 * @param e
			 */
			public void addAboveMe(ActionEvent e)
			{
				if (isAvailableSpaceForMoreArguments())
				{
					getArguments().add(getPosition(), new FunctionArgument(getPosition()));
					arrangeArgumentIndexes();
				}
			}
			/**
			 * Aggiunge un nuovo argomento sotto a questo
			 * @param e
			 */
			public void addBelowMe(ActionEvent e)
			{
				if (isAvailableSpaceForMoreArguments())
				{
					getArguments().add(getPosition()+1, new FunctionArgument(getPosition()+1));					
					arrangeArgumentIndexes();
				}
			}
			
			
			// GETTERS AND SETTERS ///////////////////////////////////
			/**
			 * @return
			 */
			public String getKey()
			{
				return key;
			}
			/**
			 * @param key
			 */
			public void setKey(String key)
			{
				this.key = key;
				if (key != null && !"".equals(key.trim()))
					setLiteral(null);
			}
			/**
			 * @return
			 */
			public int getPosition()
			{
				return position;
			}
			/**
			 * @param position
			 */
			public void setPosition(int position)
			{
				this.position = position;
			}
			/**
			 * @return
			 */
			public String getLiteral()
			{
				return literal;
			}
			/**
			 * @param literal
			 */
			public void setLiteral(String literal)
			{
				this.literal = literal;
				if (literal != null && !"".equals(literal.trim()))
					setKey(null);
			}
		}
		
		/**
		 * Ricalcola tutte le posizioni degli argomenti
		 * in modo che corrispondano alla posizione effettiva.
		 * @see FunctionArgument#setPosition(int)
		 */
		private void arrangeArgumentIndexes()
		{
			for (int i = 0; i < getArguments().size(); getArguments().get(i).setPosition(i++));
		}
		/**
		 * @return <code>true</code> se &egrave; 
		 * possibile eliminare altri argomenti
		 * di funzione, ovvero se il loro numero
		 * &egrave; maggiore di quello minimo
		 * previsto dalla funzione corrente.
		 */
		public boolean isAnyArgumentDeleteable()
		{
			return getArguments().size() > getF().getMinNumOfArgs();
		}
		/**
		 * @return <code>true</code> se &egrave;
		 * possibile aggiungere altri argomenti
		 * di funzione, ovvero se il loro numero
		 * non &egrave; gi&agrave; superiore o uguale
		 * al numero massimo di argomenti consentito dalla
		 * funzione corrente.
		 */
		public boolean isAvailableSpaceForMoreArguments()
		{
			if (getF() != null)
				return getArguments().size() < getF().getMaxNumOfArgs();
			return false;
		}
		
		/**
		 * Restituisce il nome descrittivo della
		 * funzione corrente, in accordo a 
		 * {@link ValueExpressionEditor#getLocale()}
		 * @return
		 */
		public String getFunctionName()
		{
			if (getF() != null)
				return getExpressionsSource().getEF().getDefinition(getFunctionDetail().getF(), getLocale());
			return null;
		}
		
		/**
		 * Restituisce un testo di aiuto per la
		 * funzione corrente, in accordo a 
		 * {@link ValueExpressionEditor#getLocale()}
		 * @return
		 */
		public String getFunctionHelp()
		{
			if (getF() != null)
				return getExpressionsSource().getEF().getHelp(getF(), getLocale());
			return null;
		}
		
		// GETTERS AND SETTERS /////////////////////////////////////////
		/**
		 * @return
		 */
		public List<FunctionArgument> getArguments()
		{
			if (arguments == null)
				arguments = new ArrayList<FunctionArgument>();
			return arguments;
		}
		/**
		 * @param arguments
		 */
		public void setArguments(List<FunctionArgument> arguments)
		{
			this.arguments = arguments;
		}
		/**
		 * @return
		 */
		public Function getF()
		{
			return f;
		}
		/**
		 * Quando viene inserita una nuova funzione,
		 * vengono anche creati automaticamente 
		 * nuovi argomenti di funzione, vuoti, 
		 * per un numero pari al minimo di argomenti
		 * previsto dalla funzione passata.
		 * @param f
		 */
		public void setF(Function f)
		{
			if (f != null && (f != this.f))
			{
				setArguments(null);
				for (int i = 0; i < f.getMinNumOfArgs(); i++, getArguments().add(new FunctionArgument(getArguments().size())));
			}
			this.f = f;
		}
	}
	
	/**
	 * Metodo chiamato quando si sceglie una funzione,
	 * carica quella funzione in modo che possa essere editata.
	 * @param e
	 */
	public void chooseFunction(ValueChangeEvent e)
	{
		if (e != null)
		{
			String key = (String) e.getNewValue();
			if (key != null && !"".equals(key.trim()))
				getFunctionDetail().setF((Function) getExpression(key));			
		}
	}
	/**
	 * Tasto di salvataggio del valore letterale impostato.
	 * @param e
	 */
	public void confirmLiteral(ActionEvent e)
	{
		getValue().save(getExpression(LITERAL + getLiteral()));
		setValueExpressionSelectedTab(LITERAL_ID);

		// controllo se sto lavorando dall'EntitiesBuilder
		if (getExpressionsSource() instanceof EntitiesController)
		{
			UserEntityColumnBean columnBean=((EntitiesController)getExpressionsSource()).getBuildedEntity().getEditingColumnBean();
			if(columnBean!=null && StringUtils.isEmpty(columnBean.getName())){
				columnBean.setName(getLiteral()); 
			}
		}
		
	}
	/**
	 * Tasto di salvataggio del valore letterale impostato.
	 * @param e
	 */
	public void confirmExplicitSQL(ActionEvent e)
	{
		getValue().save(ValueExpression.createFromExplicitSql(getExplicitSQL()));
		setValueExpressionSelectedTab(EXPLICIT_SQL_ID);

		// controllo se sto lavorando dall'EntitiesBuilder
		if (getExpressionsSource() instanceof EntitiesController)
		{
			UserEntityColumnBean columnBean=((EntitiesController)getExpressionsSource()).getBuildedEntity().getEditingColumnBean();
			if(columnBean!=null && StringUtils.isEmpty(columnBean.getName())){
				columnBean.setName("Espressione SQL"); 
			}
		}
	}
	/**
	 * Tasto di salvataggio del valore atomico scelto.
	 * I valori atomici sono espressioni gi&agrave; esistenti,
	 * come colonne di tabelle, o costanti.
	 * @param e
	 */
	public void confirmAtomic(ActionEvent e)
	{
		ValueExpression ve= getExpression(getKey());
		if (ve==null && getExpressionsSource() instanceof MessagesHolder){
			((MessagesHolder)getExpressionsSource()).appendQBMessage(new QueryBuilderMessage("entities.userEntity.fieldsTable.setFieldColumn.warningMessage",null));
			return;
		}
		getValue().save(ve);
		setValueExpressionSelectedTab(ATOMIC_ID);
		
		// controllo se sto lavorando dall'EntitiesBuilder
		if (getExpressionsSource() instanceof EntitiesController)
		{
			UserEntityColumnBean columnBean=((EntitiesController)getExpressionsSource()).getBuildedEntity().getEditingColumnBean();
			if(columnBean!=null && StringUtils.isEmpty(columnBean.getName())){
				columnBean.setName(getValue().getValue().getDescription()); 
			}
		}
	}
	/**
	 * Tasto di salvataggio della funzione appena editata.
	 * Se ci sono dei problemi di validazione, viene spedito un
	 * messaggio tramite {@link ValueExpressionEditor#messagesHolder}
	 * @param e
	 */
	public void confirmFunction(ActionEvent e)
	{
		boolean success = true;
		
		MembersList fArguments = new MembersList();
		FunctionDetail funcDet=getFunctionDetail();
		if(funcDet==null || funcDet.getF()==null){
			getMessagesHolder().appendQBMessage(new QueryBuilderMessage("expressions.function.name.required.errorMessage", null));
			success = false;
			return;
		}
		ValueType[] argumentsType = funcDet.getF().getArgumentsType();
		int typeIndex = 0;
		for (int i = 0; i < funcDet.getArguments().size(); i++)
		{
			typeIndex = (i < argumentsType.length) ? i : (argumentsType.length - 1);

			String key = funcDet.getArguments().get(i).getKey();
			String literal = funcDet.getArguments().get(i).getLiteral();
			
			// CONTROLLA CHE NESSUNO DEGLI ARGOMENTI DI FUNZIONE
			// PASSATI SIA NULLO. SE NO, SPEDISCE MESSAGGI DI ERRORE
			// APPROPRIATI
			if (StringUtils.isEmpty( key)  && (StringUtils.isEmpty(literal)  ))
			{
				String argumentNumber = "" + (i + 1);
				String functionDesc = funcDet.getFunctionName();
				getMessagesHolder().appendQBMessage(new QueryBuilderMessage("expressions.function.argument.required.errorMessage", null, argumentNumber, functionDesc));
				success = false;
				continue;
			}

			// IMPOSTA, SE E' IL CASO, LA GIUSTA CHIAVE PER
			// IL VALORE LETTERALE.
			if (key == null || "".equals(key.trim()))
				key = LITERAL + funcDet.getArguments().get(i).getLiteral();
			
			ValueExpression expression = getExpression(key);
			
			if (expression instanceof LiteralExpression)
				expression.setValueType(argumentsType[typeIndex]);

			// CONTROLLA CHE IL TIPO DELL'ARGOMENTO DI FUNZIONE
			// SIA UN TIPO ACCETTABILE IN QUELLA POSIZIONE.
			// SE NO, SPEDISCE MESSAGGI DI ERRORE
			// APPROPRIATI
			if (argumentsType[typeIndex].equals(ANY) || argumentsType[typeIndex].equals(UNDEFINED) || argumentsType[typeIndex].equals(expression.getValueType()))
				fArguments.add(expression);
			else
			{
				String argumentNumber = "" + (i + 1);
				String functionDesc = funcDet.getFunctionName();
				String insertedTypeDesc = getExpressionsSource().getEF().getDefinition(expression.getValueType(), getLocale());
				String requiredTypeDesc = getExpressionsSource().getEF().getDefinition(argumentsType[typeIndex], getLocale());
				getMessagesHolder().appendQBMessage(new QueryBuilderMessage("expressions.function.argument.wrongType.errorMessage", null, argumentNumber, functionDesc, insertedTypeDesc, requiredTypeDesc));
				success = false;
				continue;
			}
		}
		
		// SOLO IN CASO DI SUCCESSO, SALVA IL RISULTATO
		if (success)
		{
			funcDet.getF().setArguments(fArguments);
			getValue().save(funcDet.getF());
			setValueExpressionSelectedTab(FUNCTION_ID);
		}
		// controllo se sto lavorando dall'EntitiesBuilder
		if (getExpressionsSource() instanceof EntitiesController)
		{
			UserEntityColumnBean columnBean=((EntitiesController)getExpressionsSource()).getBuildedEntity().getEditingColumnBean();
			if(columnBean!=null && StringUtils.isEmpty(columnBean.getName()) && getValue()!=null && getValue().getValue()!=null ){
				columnBean.setName(((EntitiesController)getExpressionsSource()).getEF().getDefinition(getValue().getValue())); 
			}
		}
	}

	/**
	 * @return
	 */
	public ValueExpressionsSource getExpressionsSource()
	{
		return expressionsSource;
	}
	private ValueExpression getExpression(String key)
	{
		return getExpressionsSource().getExpression(key);
	}

	// GETTERS AND SETTERS ////////////////////////////////////////////
	/**
	 * @return
	 */
	public ObjectEditor<ValueExpression> getValue()
	{
		return value;
	}
	/**
	 * @param value
	 */
	public void setValue(ObjectEditor<ValueExpression> value)
	{
		if (value == null)
		{
			this.value = null;
			setKey(null);
			setLiteral(null);
			setChoosedFunction(null);
			setValueExpressionSelectedTab(ATOMIC_ID);
			setFunctionDetail(null);
		}
		else if (value != this.value)
		{
			this.value = value;
			setFunctionDetail(null);
			if (value.getEditingValue() instanceof LiteralExpression)
			{
				setValueExpressionSelectedTab(LITERAL_ID);
				setKey(null);
				setLiteral(value.getEditingValue().getExpression());
				setChoosedFunction(null);
			}
			else if (value.getEditingValue() instanceof Function)
			{
				setValueExpressionSelectedTab(FUNCTION_ID);
				setLiteral(null);
				String key = FUNCTION + value.getEditingValue().getExpression();
				setKey(key);
				setChoosedFunction(key);
				Function f = (Function) value.getEditingValue();					
				getFunctionDetail().setF(f);
				int pos = 0;
				for (ValueExpression item : f.getArguments().getExpressions())
				{
					FunctionArgument arg = getFunctionDetail().new FunctionArgument(pos);
					if (item instanceof LiteralExpression)
						arg.setLiteral(item.getExpression());
					else
						arg.setKey(item.getUIKey());
					if (pos < getFunctionDetail().getArguments().size())
						getFunctionDetail().getArguments().set(pos, arg);
					else
						getFunctionDetail().getArguments().add(arg);
					pos++;
				}
			}
			else if (value.getEditingValue() instanceof ExplicitSqlExpression)
			{
				setValueExpressionSelectedTab(EXPLICIT_SQL_ID);
				setLiteral(null);
				setKey(null);
				setExplicitSQL(((ExplicitSqlExpression)value.getEditingValue()).getSql());
				setChoosedFunction(null);
			}
			else if (value.getEditingValue() instanceof ValueExpression)
			{
				setValueExpressionSelectedTab(ATOMIC_ID);
				setLiteral(null);
				setKey(value.getEditingValue().getUIKey());
				setChoosedFunction(null);
			}
			else
			{
				setValueExpressionSelectedTab(null);
				setKey(null);
				setLiteral(null);
				setChoosedFunction(null);
			}
		}
	}

	/**
	 * @return
	 */
	public Locale getLocale()
	{
		return locale;
	}

	/**
	 * @param locale
	 */
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	/**
	 * @param expressionsSource
	 */
	public void setExpressionsSource(ValueExpressionsSource expressionsSource)
	{
		this.expressionsSource = expressionsSource;
	}

	/**
	 * Per i criteri generali di generazione
	 * delle chiavi, vedi
	 * {@link EntitiesController}
	 * @return
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * Per i criteri generali di generazione
	 * delle chiavi, vedi
	 * {@link EntitiesController}
	 * @param key
	 */
	public void setKey(String key)
	{
		this.key = key;
	}
	/**
	 * @return
	 */
	public MessagesHolder getMessagesHolder()
	{
		return messagesHolder;
	}
	/**
	 * @param messagesHolder
	 */
	public void setMessagesHolder(MessagesHolder messagesHolder)
	{
		this.messagesHolder = messagesHolder;
	}
	/**
	 * @return
	 */
	public FunctionDetail getFunctionDetail()
	{
		if (functionDetail == null)
			functionDetail = new FunctionDetail();
		return functionDetail;
	}
	/**
	 * @param functionDetail
	 */
	public void setFunctionDetail(FunctionDetail functionDetail)
	{
		this.functionDetail = functionDetail;
	}
	/**
	 * @return
	 */
	public String getChoosedFunction()
	{
		return choosedFunction;
	}
	/**
	 * @param choosedFunction
	 */
	public void setChoosedFunction(String choosedFunction)
	{
		this.choosedFunction = choosedFunction;
	}
	/**
	 * @return
	 */
	public String getLiteral()
	{
		return literal;
	}
	/**
	 * @param literal
	 */
	public void setLiteral(String literal)
	{
		this.literal = literal;
	}
	/**
	 * @return Valore utilizzato dalla pagina jsp
	 * per rendere visibile la tab giusta tra
	 * quella del valore letterale, quella dei
	 * valori atomici e quella delle funzioni.
	 */
	public String getValueExpressionSelectedTab()
	{
		return valueExpressionSelectedTab;
	}
	/**
	 * @param valueExpressionSelectedTab 
	 * Valore utilizzato dalla pagina jsp
	 * per rendere visibile la tab giusta tra
	 * quella del valore letterale, quella dei
	 * valori atomici e quella delle funzioni.
	 */
	public void setValueExpressionSelectedTab(String valueExpressionSelectedTab)
	{
		this.valueExpressionSelectedTab = valueExpressionSelectedTab;
	}
	public String getExplicitSQL()
	{
		return explicitSQL;
	}
	public void setExplicitSQL(String explicitSQL)
	{
		this.explicitSQL = explicitSQL;
	}
	public boolean isEditable()
	{
		return editable;
	}
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
}
