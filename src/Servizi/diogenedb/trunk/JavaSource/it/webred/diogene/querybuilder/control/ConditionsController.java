package it.webred.diogene.querybuilder.control;

import static it.webred.diogene.querybuilder.Constants.ALIAS;
import static it.webred.diogene.querybuilder.Constants.COLUMN;
import static it.webred.diogene.querybuilder.Constants.CONSTANT;
import static it.webred.diogene.querybuilder.Constants.FUNCTION;
import static it.webred.diogene.querybuilder.Constants.LITERAL;
import static it.webred.diogene.querybuilder.Constants.OUTER;
import static it.webred.diogene.querybuilder.Constants.TABLE;
import static it.webred.diogene.querybuilder.enums.Outcomes.SUCCESS;
import static it.webred.faces.utils.ComponentsUtil.sortSelectItemsList;
import static it.webred.utils.ReflectionUtils.clearAllFields;
import static it.webred.utils.StringUtils.quoteSpecialCharacters;
import it.webred.diogene.querybuilder.QueryBuilderMessage;
import it.webred.diogene.querybuilder.beans.DcColumnBean;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.querybuilder.beans.DcSchemaEntityBean;
import it.webred.diogene.querybuilder.beans.DvUserEntityBean;
import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.ConditionFactory;
import it.webred.diogene.sqldiagram.Constant;
import it.webred.diogene.sqldiagram.ExplicitSqlExpression;
import it.webred.diogene.sqldiagram.ExpressionFactory;
import it.webred.diogene.sqldiagram.Function;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.OperatorFactory;
import it.webred.diogene.sqldiagram.RelationalOperator;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.TableExpression;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.ValueExpressionOperator;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.apache.log4j.Logger;

/**
 * Questa classe &egrave; il <i>backing bean</i> della
 * pagina <code>conditionBuilder.jsp</code>, ovvero della <i>popup</i>
 * per la costruzione delle relazioni / condizioni.
 * @author Giulio Quaresima
 * @version $Revision: 1.3 $ $Date: 2007/12/28 11:37:48 $
 */
public class ConditionsController extends Observable implements MessagesHolder, ValueExpressionsSource
{
	public enum MessagesToObservers 
	{
		SAVE,
		UNDO;
	}
	
	private final Logger log = Logger.getLogger(this.getClass());

	private final String resourceBundleName = "it.webred.diogene.querybuilder.labels";
	private final SelectItem nullSelectItem = new SelectItem("", " - ", "");

	// ORDINAMENTO DEI GRUPPI: --> --> --> --> --> --> -->  [1                  (2   )][3                  (4   )][5            ] [6                   (7   )]
	final Pattern separeTableAndAlias = Pattern.compile("\\A(\\Q" + TABLE + "\\E(\\d+))(\\Q" + ALIAS + "\\E(\\d+))(" + OUTER + ")?(\\Q" + COLUMN + "\\E(\\d+))?\\z");

	private CanCallConditionController caller;
	
	private ResourceBundle resourceBundle;
	private List<QueryBuilderMessage> qbMessages;

	private GenericCondition condition = null;
	private ValueExpressionEditor valueExpressionEditor;
	
	// VALUE BINDINGS
	private int stepCount = 0;
	private List<SelectItem> relationalOperators;
	private List<SelectItem> fields;
	private List<SelectItem> functions;	
	private List<SelectItem> chainOperators;
	private String nextFocusID;
	private String globalErrorMessage;
	
	public ConditionsController()
	{
		initializeAll();
	}


	// NON-PUBLIC METHODS //////////////////////////////////////////
	/**
	 * Metodo di inizializzazione, chiamato anche dal costruttore, 
	 * serve per riportare questo <i>backing bean</i> allo stato
	 * iniziale, uno stato pulito di partenza. 
	 */
	private void initializeAll()
	{
		// PRIMA, SVUOTO TUTTI I CAMPI 
		// (TRANNE I FINAL, GLI STATIC E QUELLI ELENCATI) 
		try {clearAllFields(this, false, true, "resourceBundle");}
		catch (IllegalAccessException iae) {}
		catch (Exception e) {}
	}
	
	/**
	 * @return Il contesto corrente delle Java Server Faces
	 */
	FacesContext getContext()
	{
		return FacesContext.getCurrentInstance();
	}
	/**
	 * @return Il locale corrente di JSF, che corrisponde
	 * al locale richiesto dallo <i>User Agent</i> o al locale
	 * di <i>default</i>
	 */
	Locale getLocale()
	{
		return getContext().getViewRoot().getLocale();
	}
	// RESTITUISCE IL RESOURCE BUNDLE DEL QUERY BUILDER
	/**
	 * @return Il <i>resource bundle</i> corrente, trovato
	 * all'indirizzo specificato dalla variabile 
	 * {@link ConditionsController#resourceBundleName}
	 */
	ResourceBundle getResourceBundle() 
	{
		if (resourceBundle == null)
		{
			try
			{
				resourceBundle = ResourceBundle.getBundle(resourceBundleName, getLocale());							
			}
			catch (Throwable e)
			{
				// TODO Togli il try/catch
				log.error("Probabilmente c'è un'errore di sintassi nel resourcebundle!!!");
				log.error("", e);
			}
		}
		return resourceBundle;
	}
	
	
	
	
	
	
	
	// ACTION LISTENERS ////////////////////////////////////////////
	/**
	 * Metodo chiamato per aggiungere un nuovo
	 * criterio di relazione, ovvero un nuovo operatore
	 * (vedi {@link ConditionsController#getRelationalOperators()})
	 * di confronto tra espressioni, che può essere una join
	 * o qualcos'altro.
	 * @param e
	 */
	public void generateRelation(ActionEvent e)
	{
		getCondition().addRelation();
	}
	
	
	
	
	
	
	// ACTIONS /////////////////////////////////////////////////////
	/**
	 * Conferma la relazione creata. La <i>popup</i> viene
	 * chiusa, salvando tutte le modifiche effettuate.
	 * @return
	 */
	public String relationsOK()
	{
		getCondition().setEditingOperand(null);
		getCaller().saveCondition(getCondition());
		this.condition = null;
		super.notifyObservers(MessagesToObservers.SAVE);
		return SUCCESS.outcome();
	}
	/**
	 * Annullamento delle modifiche effettuate.
	 * La <i>popup</i> viene chiusa, e tutte le modifiche
	 * vengono annullate.
	 * @return
	 */
	public String relationsCancel()
	{
		getCaller().undoConditionUpdate(getCondition());
		this.condition = null;
		super.notifyObservers(MessagesToObservers.UNDO);
		return SUCCESS.outcome();
	}
	
	
	
	
	
	
	
	// IMPLEMENTED METHODS /////////////////////////////////////////
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getExpression(java.lang.String)
	 */
	public ValueExpression getExpression(String key)
	{
		if (key != null)
		{
			if (key.startsWith(CONSTANT))
			{
				ValueExpression exp = getEF().getExpression(key.substring(CONSTANT.length()));
				exp.setUIKey(key);
				return exp;
			}
			else if (key.startsWith(TABLE))
			{
				Matcher m = separeTableAndAlias.matcher(key);
				if (m.find())
				{
					DcEntityBean table = null;
					for (DcEntityBean item : getCondition().getEntitiesInvolved())
					{
						if (item.getId().equals(new Long(m.group(2))) && item.getAliasNum() == Long.parseLong(m.group(4)))
						{
							table = item;
							break;
						}
					}
					//bug fix relativo agli alias per le relazioni.
					String theAlias=(getCaller()instanceof EntitiesController )? table.getAlias():table.getAliasRelation();
					TableExpression sqlTable = new TableExpression(table.getId(), table.getSqlName(), table.getName(), theAlias);
					String UIKey = m.group(1) + m.group(3);
					if (m.group(5) != null)
					{
						UIKey += m.group(5);
						sqlTable.setOuter(true);
					}
					else
						sqlTable.setOuter(false);
					sqlTable.setUIKey(UIKey);
					for (DcColumnBean column : table.getColumns())
						if (new Long(m.group(7)).equals(column.getId()))
						{
							ValueExpression sqlColumn = ValueExpression.createFromXml(column.getXml(), getSqlGenerator());
							if (sqlColumn instanceof Column)
								((Column) sqlColumn).setTable(sqlTable);
							if (table instanceof DcSchemaEntityBean)
								sqlColumn.setAlias(column.getAlias());								
							else if (table instanceof DvUserEntityBean) {
								sqlColumn.setExpression(column.getAlias());
								if (sqlColumn instanceof ExplicitSqlExpression) {
									//devo prendere nomeentita.alias e non sql, quindi forzo la creazione di un oggetto Column
									Column newSqlColumn = new Column(
											sqlColumn.getExpression(), 
											sqlColumn.getDescription(),
											sqlColumn.getAlias(),
											sqlTable,
											null
											);						
									sqlColumn = newSqlColumn;
								}
							}							
							sqlColumn.setValueType(ValueType.mapJavaType2ValueType(column.getJavaType()));
							sqlColumn.setUIKey(key);
							return sqlColumn;
						}
				}
			}
			else if (key.startsWith(LITERAL))
				return new LiteralExpression(key.substring(LITERAL.length()), null);
			else if (key.startsWith(FUNCTION))
				try 
				{
					ValueExpression f = getEF().getExpression(key.substring(FUNCTION.length())).clone();
					f.setUIKey(key);
					return f;
				}
				catch (CloneNotSupportedException e) {}
		}
		return null;
	}
	public SqlGenerator getSqlGenerator()
	{
		if (getCaller() != null)
			return getCaller().getSqlGenerator();
		return null;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getEF()
	 */
	public ExpressionFactory getEF()
	{
		if (getCaller() != null)
			return getCaller().getEF();
		return null;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getCF()
	 */
	public ConditionFactory getCF()
	{
		if (getCaller() != null)
			return getCaller().getCF();
		return null;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getOF()
	 */
	public OperatorFactory getOF()
	{
		if (getCaller() != null)
			return getCaller().getOF();
		return null;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.MessagesHolder#getQBMessages()
	 */
	public List<QueryBuilderMessage> getQBMessages()
	{
		if (qbMessages == null)
			qbMessages = new ArrayList<QueryBuilderMessage>();
		return qbMessages;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.MessagesHolder#appendQBMessage(it.webred.diogene.querybuilder.control.QueryBuilderMessage)
	 */
	public void appendQBMessage(QueryBuilderMessage message)
	{
		getQBMessages().add(message);
	}

	
	
	
	// GETTERS AND SETTERS /////////////////////////////////////////
	/**
	 * @return
	 */
	public GenericCondition getCondition()
	{
		return condition;
	}
	/**
	 * Questo metodo viene richiamato dall'esterno per
	 * iniziare la creazione o la modifica della relazione
	 * o condizione. E' previsto che il chiamante chiami questo
	 * metodo passando la condizione da editare, e nello stesso
	 * tempo apra opportunamente in <i>popup</i> la pagina
	 * <code>conditionBuilder.jsp</code>
	 * @param conditionsHolder
	 * @param valueExpressionSource
	 * @param condition
	 */
	public void setCondition(CanCallConditionController caller, GenericCondition condition)
	{
		initializeAll();
		deleteObservers();
		super.addObserver(caller);
		if (condition != null)
		{
			this.condition = condition;
			setCaller(caller);
		}
	}
	/**
	 * @return
	 */
	public int getStepCount()
	{
		return stepCount;
	}
	/**
	 * Per ora non fa nulla, ma dovrebbe fare
	 * come {@link EntitiesController#setStepCount(int)}
	 * per controllare l'integrita dello stato.
	 * @param stepCount
	 */
	public void setStepCount(int stepCount)
	{
		this.stepCount = stepCount;
	}
	/**
	 * @return
	 */
	public String getNextFocusID()
	{
		return nextFocusID;
	}
	/**
	 * @param nextFocusID
	 */
	public void setNextFocusID(String nextFocusID)
	{
		this.nextFocusID = nextFocusID;
	}
	/**
	 * @return I dati per popolare le <i>combo</i>
	 * contenenti gli operatori relazionali da
	 * scegliere.
	 */
	public List<SelectItem> getRelationalOperators()
	{
		if (relationalOperators == null)
		{
			relationalOperators = new ArrayList<SelectItem>();
			SelectItem sItem = null;
			for (RelationalOperator item : getOF().getRelationalOperators())
			{
				sItem = new SelectItem();
				sItem.setValue(item.getName());
				String label = getOF().getDefinition(item, getContext().getViewRoot().getLocale());
				sItem.setLabel(label != null ? label : item.getName());
				getRelationalOperators().add(sItem);
			}
			relationalOperators.add(0, nullSelectItem);
		}
		return relationalOperators;
	}
	/**
	 * Come in {@link EntitiesController#getValueExpressionEditor()},
	 * ma in questo caso l'editor delle espressione serve
	 * per valorizzare gli operandi dei criteri di relazione.
	 * In particolare, l'editor riflette il contenuto di
	 * {@link GenericCondition#getEditingOperand()}
	 * @return
	 */
	public ValueExpressionEditor getValueExpressionEditor()
	{
		if (valueExpressionEditor == null)
			valueExpressionEditor = new ValueExpressionEditor(
					getLocale(),
					this, this);
		if (getCondition() != null && getCondition().getEditingOperand() != null)
		{
			valueExpressionEditor.setValue(getCondition().getEditingOperand());
			valueExpressionEditor.setEditable(getCondition().getEditingOperand().isEditable());
		}
		else
			valueExpressionEditor.setValue(null);
		return valueExpressionEditor;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getFields()
	 */
	public List<SelectItem> getFields()
	{
		if (fields == null && getCondition() != null)
		{
			fields = new ArrayList<SelectItem>();
			
			for (DcEntityBean item : getCondition().getEntitiesInvolved())
			{
				String tableKey = TABLE + item.getId() + ALIAS + item.getAliasNum();
				if (getCondition().getOuterJoin().contains(item))
					tableKey += OUTER;
				List<SelectItem> subFields = new ArrayList<SelectItem>(); 
				for (DcColumnBean item1 : item.getColumns())
				{
					String theKey=item1.isPrimaryKey()?" *":"";
					SelectItem sItem = new SelectItem();
					sItem.setLabel(item1.getDescription()+theKey);
					sItem.setValue(tableKey + COLUMN + item1.getId());
					subFields.add(sItem);
				}
				if (subFields.size() > 0)
				{
					sortSelectItemsList(subFields, getLocale(), true);
					SelectItemGroup sItemGroup = new SelectItemGroup();
					sItemGroup.setSelectItems(subFields.toArray(new SelectItem[0]));
					sItemGroup.setLabel(item.getName());
					fields.add(sItemGroup);							
				}
			}
			
			List<SelectItem> subFields = new ArrayList<SelectItem>(); 
			for (ValueExpression item : getEF().getExpressions())
			{
				if (item instanceof Constant)
				{
					SelectItem sItem = new SelectItem();
					sItem.setLabel(getEF().getDefinition(item, getLocale()));
					sItem.setValue(CONSTANT + item.getExpression());
					subFields.add(sItem);
				}
			}
						
			sortSelectItemsList(fields, getLocale(), true);
			
			if (subFields.size() > 0)
			{
				sortSelectItemsList(subFields, getLocale(), true);
				SelectItemGroup sItemGroup = new SelectItemGroup();
				sItemGroup.setSelectItems(subFields.toArray(new SelectItem[0]));
				sItemGroup.setLabel(getResourceBundle().getString("expressions.category.constant.label"));
				fields.add(0, sItemGroup);							
			}
		}
		return fields;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getFunctions()
	 */
	public List<SelectItem> getFunctions()
	{
		if (functions == null)
		{
			functions = new ArrayList<SelectItem>();
			SelectItem sItem = null;
			for (ValueExpression item : getEF().getExpressions())
				if (item instanceof Function)
				{
					sItem = new SelectItem();
					sItem.setValue(FUNCTION + item.getExpression());
					sItem.setLabel(getEF().getDefinition(item, getLocale()));
					functions.add(sItem);
				}
			sortSelectItemsList(functions, getLocale(), true);
		}
		return functions;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getChainOperators()
	 */
	public List<SelectItem> getChainOperators()
	{
		if (chainOperators == null)
		{
			chainOperators = new ArrayList<SelectItem>();
			SelectItem sItem = null;
			for (ValueExpressionOperator item : getOF().getValueExpressionOperators())
			{
				sItem = new SelectItem();
				sItem.setValue(item.getName());
				sItem.setLabel(getOF().getDefinition(item, getLocale()));
				chainOperators.add(sItem);
			}
		}
		return chainOperators;
	}


	public CanCallConditionController getCaller()
	{
		return caller;
	}


	public void setCaller(CanCallConditionController caller)
	{
		this.caller = caller;
	}
	public String getErrori(){
		for (QueryBuilderMessage item : getQBMessages())
		{
			String message;
			try
			{
				message = getResourceBundle().getString(item.getResourceKey());
				if (item.getMessageFormatParameters() != null && (item.getMessageFormatParameters().length > 0))
					message = MessageFormat.format(message, (Object[]) item.getMessageFormatParameters());
			}
			catch (NullPointerException npe)
			{
				message = "ERROR";
			}
			catch (MissingResourceException mre)
			{
				message = item.getResourceKey();
			}
			
			if (item.getElementId() != null && !"".equals(item.getElementId().trim()))
				getContext().addMessage(item.getElementId(), new FacesMessage(message));
			else
			{
				if (getGlobalErrorMessage() != null)
					// PUNTO ALLA VARIABILE, E NON AL METODO getGlobalErrorMessage(), POICHE' ALTRIMENTI QUOTEREBBE DUE VOLTE I CARATTERI SPECIALI, GENERANDO UN ERRORE JAVASCRIPT 
					setGlobalErrorMessage(globalErrorMessage + "\\r\\n" + message);  
				else
					setGlobalErrorMessage(message);
			}	
		}
		
		clearQBMessages();
		String errori=getGlobalErrorMessage();
		setGlobalErrorMessage(null);
		return errori;
	}
	/**
	 * Questo messaggio, se presente, fa comparire un
	 * messaggio di errore globale (un alert), nella
	 * pagina entities.jsp
	 * @return
	 */
	public String getGlobalErrorMessage()
	{
		if (globalErrorMessage != null)
			return quoteSpecialCharacters(globalErrorMessage, "\"'", "\\"); // QUOTO GLI APOSTROFI PER JavaScript
		return globalErrorMessage;
	}
	public void setGlobalErrorMessage(String globalErrorMessage)
	{
		this.globalErrorMessage = globalErrorMessage;
	}
	/**
	 * Imposta a <code>null</code> la coda dei messaggi, 
	 * ovvero la svuota.
	 * @see MessagesHolder
	 */
	private void clearQBMessages()
	{
		qbMessages = null;
	}

}
