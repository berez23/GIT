package it.webred.diogene.querybuilder.control;

import static it.webred.diogene.querybuilder.Constants.ALIAS;
import static it.webred.diogene.querybuilder.Constants.COLUMN;
import static it.webred.diogene.querybuilder.Constants.CONSTANT;
import static it.webred.diogene.querybuilder.Constants.FUNCTION;
import static it.webred.diogene.querybuilder.Constants.LITERAL;
import static it.webred.diogene.querybuilder.Constants.OUTER;
import static it.webred.diogene.querybuilder.Constants.TABLE;
import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;
import static it.webred.diogene.querybuilder.control.Constants.ENTITIES_BUILDER_FORM_ID;
import static it.webred.diogene.querybuilder.enums.Outcomes.EXPLICIT_SQL;
import static it.webred.diogene.querybuilder.enums.Outcomes.FAILURE;
import static it.webred.diogene.querybuilder.enums.Outcomes.SUCCESS;
import static it.webred.faces.utils.ComponentsUtil.sortSelectItemsList;
import static it.webred.utils.ReflectionUtils.clearAllFields;
import static it.webred.utils.StringUtils.isEmpty;
import static it.webred.utils.StringUtils.quoteSpecialCharacters;
import it.webred.diogene.db.model.DcRel;
import it.webred.diogene.querybuilder.QueryBuilderException;
import it.webred.diogene.querybuilder.QueryBuilderMessage;
import it.webred.diogene.querybuilder.Translator;
import it.webred.diogene.querybuilder.beans.DcColumnBean;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.querybuilder.beans.DcSchemaEntityBean;
import it.webred.diogene.querybuilder.beans.DvUserEntityBean;
import it.webred.diogene.querybuilder.control.GenericCondition.RelationRow;
import it.webred.diogene.querybuilder.control.UserEntityColumnBean;
import it.webred.diogene.querybuilder.control.UserEntityColumnBeanAppendedValue;
import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.Condition;
import it.webred.diogene.sqldiagram.ConditionFactory;
import it.webred.diogene.sqldiagram.Constant;
import it.webred.diogene.sqldiagram.ExplicitSqlExpression;
import it.webred.diogene.sqldiagram.ExpressionFactory;
import it.webred.diogene.sqldiagram.Function;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.OperatorFactory;
import it.webred.diogene.sqldiagram.Predicate;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.TableExpression;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.ValueExpressionOperator;
import it.webred.diogene.sqldiagram.EnumsRepository.DBType;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import it.webred.faces.components.UIHtmlBlock;
import it.webred.faces.components.UIQBTablePane;
import it.webred.faces.components.WinIco;
import it.webred.faces.utils.Supervisor;
import it.webred.permessi.GestionePermessi;
import it.webred.utils.ASetAsAKey;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.FactoryFinder;
import javax.faces.application.FacesMessage;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * <i>Backing Bean</i> del componente software <i>Query Builder</i> che si
 * occupa della creazione di una <i>User Entity</i>, rispondendo agli eventi
 * scatenati dalla pagina <code>entities.jsp</code>
 * <h3>N.B. CRITERI GENERALI PER LE CHIAVI</h3>
 * <p>
 * In tutto query builder, per le chiavi da utilizzare come valori univoci nelle
 * mappe e nelle <i>combo box</i>, si utilizza un criterio univoco basato sugli
 * id degli oggetti e sulle costanti contenute in
 * {@link it.webred.diogene.querybuilder.Constants}. Cos&igrave;, le chiavi che
 * identificano le entit&agrave; sono la concate nazione di
 * {@link it.webred.diogene.querybuilder.Constants#TABLE} + id
 * dell'entit&agrave; + {@link it.webred.diogene.querybuilder.Constants#ALIAS} +
 * progressivo dell'alias. Per le colonne delle entit&agrave; avremo la stessa
 * chiave delle entit&agrave; seguita da
 * {@link it.webred.diogene.querybuilder.Constants#COLUMN} + l'id della colonna.
 * Per le funzioni avremo:
 * {@link it.webred.diogene.querybuilder.Constants#FUNCTION} + il nome univoco
 * della funzione, e cos&igrave; secondo la stessa logica in tutti i casi.
 * </p>
 * 
 * @author Giulio Quaresima
 * @version $Revision: 1.8 $ $Date: 2007/12/19 09:16:37 $
 */
public class EntitiesController implements CanCallConditionController, MessagesHolder
{
	private final Logger log = Logger.getLogger(this.getClass());

	private final SelectItem nullSelectItem = new SelectItem("", " - ", "");

	// CONTROL
	private MetadataController controller;

	private ConditionsController conditionsController;

	private Supervisor supervisor;

	private SqlGenerator sqlGen;

	private final String resourceBundleName = "it.webred.diogene.querybuilder.labels";

	private ResourceBundle resourceBundle;

	private List<QueryBuilderMessage> qbMessages;

	private Map<String, Map<Short, DcEntityBean>> choosedEntitiesMap;

	private boolean validStatus, historyBackPerformed;

	// VALUE BINDINGS
	private String choosedFormerEntityInRelation,
			choosedLatterEntityInRelation, nextFocusID, lastXScrollPosition,
			lastYScrollPosition, globalErrorMessage, modifingRelation,
			queryString, conditionXmlString, editingExpressionKey,
			userEntityToEdit, explicitSql;

	private boolean latterEntityInOuter, updateStatus, modifiedByUser,
			showSql = false;

	private List<DcEntityBean> availableDcEntities;

	private List<SelectItem> availableUserEntities;

	private List<SelectItem> formerEntitiesInRelation;

	private List<SelectItem> latterEntitiesInRelation;

	private List<SelectItem> fields;

	private List<SelectItem> userEntityFieldsNumbers;

	private List<SelectItem> functions;

	private List<SelectItem> chainOperators;

	private List<SelectItem> predicates;

	private ValueExpressionEditor valueExpressionEditor;

	private int stepCount = 0;

	private RelationsBetweenEntities relationsDataBinding;

	private UserEntityBean buildedEntity;
	
	private String userEntityForLabelTypes;

	private String userEntityNameForLabelTypes;

	// BINDINGS
	private UIHtmlBlock choosedEntitiesPanel;

	private UIHtmlBlock iconsContainer;

	// REGULAR EXPRESSIONS

	// ORDINAMENTO DEI GRUPPI: --> --> --> --> --> --> --> [1 (2 )][3 (4 )][5 ]
	// [6 (7 )]
	final Pattern separeTableAndAlias = Pattern.compile("\\A(\\Q" + TABLE + "\\E(\\d+))(\\Q" + ALIAS + "\\E(\\d+))(" + OUTER + ")?(\\Q" + COLUMN + "\\E(\\d+))?\\z");

	// CTOR
	/**
	 * 
	 */
	public EntitiesController()
	{
		super();
		initializeAll();
	}
	
	/**
	 * Metodo di inizializzazione, chiamato anche dal costruttore, serve per
	 * riportare questo <i>backing bean</i> allo stato iniziale, uno stato
	 * pulito di partenza.
	 */
	public void initializeAll()
	{
		initializeAll("controller", "conditionsController");
	}

	// METODI NON-PUBLIC ////////////////////////////////////////
	

	/**
	 * Metodo di inizializzazione, chiamato anche dal costruttore, serve per
	 * riportare questo <i>backing bean</i> allo stato iniziale, uno stato
	 * pulito di partenza.
	 * 
	 * @param nonInizializzareQuesteProprieta
	 *            L'elenco dei nomi (CASE SENSITIVE!!!) delle propriet&agrave;
	 *            dei campi che non si vuole siano inizializzati.
	 */
	private void initializeAll(String... nonInizializzareQuesteProprieta)
	{
		// PRIMA, SVUOTO TUTTI I CAMPI
		// (TRANNE I FINAL, GLI STATIC E QUELLI ELENCATI)
		try
		{
			clearAllFields(this, false, true, nonInizializzareQuesteProprieta);
		}
		catch (IllegalAccessException e)
		{
		}

		// POI, INIZIALIZZO QUELLO CHE C'E' DA INIZIALIZZARE
		registerPhaseListeners();
		setValidStatus(true);
		setIconsContainer(new UIHtmlBlock());
		refreshEntitiesInRelationsCombos();
		Principal principal= getContext().getExternalContext().getUserPrincipal();
		if(principal!=null)GestionePermessi.clearCache(principal.getName());
		//Ricarico le entità
		if (getController() != null)
			getController().refreshEntitiesMap();
//		if (getController() != null)
//			getController().endSesssion();
	}

	/**
	 * @return Il contesto corrente delle Java Server Faces
	 */
	FacesContext getContext()
	{
		return FacesContext.getCurrentInstance();
	}

	/**
	 * @return Il locale corrente di JSF, che corrisponde al locale richiesto
	 *         dallo <i>User Agent</i> o al locale di <i>default</i>
	 */
	Locale getLocale()
	{
		return getContext().getViewRoot().getLocale();
	}

	/**
	 * @return Il <i>resource bundle</i> corrente, trovato all'indirizzo
	 *         specificato dalla variabile
	 *         {@link EntitiesController#resourceBundleName}
	 */
	public ResourceBundle getResourceBundle()
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

	/**
	 * @return Un'istanza del supervisore, ovvero del {@link PhaseListener} del
	 *         query builder.
	 */
	private Supervisor getSupervisor()
	{
		if (supervisor == null)
		{
			LifecycleFactory factory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
			Lifecycle lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
			for (PhaseListener item : lifecycle.getPhaseListeners())
			{
				if (item instanceof Supervisor)
					supervisor = (Supervisor) item;
			}
		}
		return supervisor;
	}

	public SqlGenerator getSqlGenerator()
	{
		if (sqlGen == null)
			return new SqlGenerator(DBType.ORACLE);
		return sqlGen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getCF()
	 */
	public ConditionFactory getCF()
	{
		try
		{
			return getSqlGenerator().getConditionFactory();
		}
		catch (Exception e)
		{
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getEF()
	 */
	public ExpressionFactory getEF()
	{
		try
		{
			return getSqlGenerator().getExpressionFactory();
		}
		catch (Exception e)
		{
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getOF()
	 */
	public OperatorFactory getOF()
	{
		try
		{
			return getSqlGenerator().getOperatorFactory();
		}
		catch (Exception e)
		{
		}
		return null;
	}

	/**
	 * Metodo di utilit&agrave; interna, restituisce un numero se riesce a
	 * trovarlo nella stringa <code>id</code> davanti alla stringa
	 * <code>key</code>. Esempi:
	 * 
	 * <pre>
	 * getNumericId(&quot;TABLE&quot;, &quot;TABLE34&quot;); // RESTITUISCE 34
	 * getNumericId(&quot;COLUMN&quot;, &quot;TABLE34COLUMN12&quot;); // RESTITUISCE 12
	 * </pre>
	 * 
	 * @param key
	 * @param id
	 * @return
	 */
	private Long getNumericId(String key, String id)
	{
		try
		{
			Matcher m = Pattern.compile("\\Q" + key + "\\E(\\d+)").matcher(id);
			m.find();
			return Long.parseLong(m.group(1));
		}
		catch (Throwable e)
		{
			return null;
		}
	}

	/**
	 * Ricarica il contenuto delle due combo che contengono l'elenco delle
	 * entit&agrave; che possono essere scelte per generare una relazione (Join)
	 */
	private void refreshEntitiesInRelationsCombos()
	{
		getFormerEntitiesInRelation().clear();
		getLatterEntitiesInRelation().clear();
		for (Map<Short, DcEntityBean> map : getChoosedEntitiesMap().values())
			for (DcEntityBean bean : map.values())
			{
				SelectItem sItem = fromDcEntityBeanToSelectItem(bean, true);
				if (!sItem.getValue().equals(getChoosedLatterEntityInRelation()))
					getFormerEntitiesInRelation().add(sItem);
				if (!sItem.getValue().equals(getChoosedFormerEntityInRelation()))
					getLatterEntitiesInRelation().add(sItem);
			}
		sortSelectItemsList(getFormerEntitiesInRelation(), getLocale(), true);
		sortSelectItemsList(getLatterEntitiesInRelation(), getLocale(), true);
		getFormerEntitiesInRelation().add(0, nullSelectItem);
		getLatterEntitiesInRelation().add(0, nullSelectItem);
	}

	/**
	 * Portando la variabile {@link EntitiesController#fields} a
	 * <code>null</code>, fa in modo che l'elenco dei campi debba essere
	 * ricaricato.
	 * 
	 * @see {@link EntitiesController#getFields()}
	 */
	private void refreshFields()
	{
		fields = null;
	}

	/**
	 * @return Data l'entit&agrave;, restituisce un <code>item</code> che lo
	 *         rappresenti, mettendo il nome dell'entit&agrave; come etichetta
	 *         dell'<code>item</code>, e una chiave come valore. Per il
	 *         criterio con cui &egrave; definita questa chiave, vedi
	 *         {@link EntitiesController}
	 */
	static SelectItem fromDcEntityBeanToSelectItem(DcEntityBean bean, boolean alias)
	{
		if (bean != null)
		{
			String name = bean.getName() == null ? "" : bean.getName();
			String desc = bean.getDesc() == null ? "" : bean.getDesc();
			String value = TABLE + bean.getId();
			if (alias)
				value += ALIAS + bean.getAliasNum();
			return new SelectItem(value, name, desc);
		}
		return null;
	}

	// PHASE LISTENERS ///////////////////////////////////////////
	/**
	 * Registra i {@link PhaseListener listeners} che verranno richiamati dal
	 * {@link Supervisor} restituito da
	 * {@link EntitiesController#getSupervisor()} nelle fasi specificate
	 */
	private void registerPhaseListeners()
	{
		// REGISTRA I METODI CHE SI VUOLE SIANO CHIAMATI
		// DURANTE CERTE FASI DEL CICLO DI VITA DELL'APPLICAZIONE
		if (getSupervisor() != null)
		{
			getSupervisor().setLogOn(false); // TODO METTERE A FALSE

			try
			{
				// LE RIGHE COMMENTATE CORRISPONDONO AI METODI VUOTI, CHE NON
				// FANNO NULLA
				// getSupervisor().registerListener(PhaseId.RESTORE_VIEW, false,
				// "beforeRestoreView", this);
				// getSupervisor().registerListener(PhaseId.RESTORE_VIEW, true,
				// "afterRestoreView", this);
				getSupervisor().registerListener(PhaseId.APPLY_REQUEST_VALUES, false, "beforeApplyRequestValues", this);
				// getSupervisor().registerListener(PhaseId.APPLY_REQUEST_VALUES,
				// true, "afterApplyRequestValues", this);
				// getSupervisor().registerListener(PhaseId.PROCESS_VALIDATIONS,
				// false, "beforeProcessValidation", this);
				// getSupervisor().registerListener(PhaseId.PROCESS_VALIDATIONS,
				// true, "afterProcessValidation", this);
				getSupervisor().registerListener(PhaseId.UPDATE_MODEL_VALUES, false, "beforeUpdateModelValues", this);
				getSupervisor().registerListener(PhaseId.UPDATE_MODEL_VALUES, true, "afterUpdateModelValues", this);
				// getSupervisor().registerListener(PhaseId.INVOKE_APPLICATION,
				// false, "beforeInvokeApplication", this);
				// getSupervisor().registerListener(PhaseId.INVOKE_APPLICATION,
				// true, "afterInvokeApplication", this);
				getSupervisor().registerListener(PhaseId.RENDER_RESPONSE, false, "beforeRenderResponse", this);
				// getSupervisor().registerListener(PhaseId.RENDER_RESPONSE,
				// true, "afterRenderResponse", this);
			}
			catch (Exception e)
			{
				log.error("", e);
			} // TODO TOGLIERE
		}
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void beforeRestoreView()
	{
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void afterRestoreView()
	{
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void beforeApplyRequestValues()
	{
		setShowSql(false);
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void afterApplyRequestValues()
	{
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void beforeProcessValidation()
	{
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void afterProcessValidation()
	{
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void beforeUpdateModelValues()
	{
		setHistoryBackPerformed(false);
		setGlobalErrorMessage(null);
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void afterUpdateModelValues()
	{
		// INVALIDA LA SESSIONE PERCHE' IL NUMERO DI CICLI
		// REQUEST-RESPONSE CONTATO DA QUESTO MANAGED BEAN
		// TRAMITE LA PROPRIETA' stepCount
		// NON CORRISPONDE AL NUMERO POSTATO DAL CLIENT,
		// TRAMITE IL CAMPO NASCOSTO stepCount PRESENTE IN
		// TUTTE LE PAGINE. QUESTO PUO' ACCADERE QUANDO
		// L'UTENTE UTILIZZA IL TASTO INDIETRO DEL BROWSER,
		// O FORZA IN QUALCHE ALTRO MODO LA NAVIGAZIONE
		// NATURALE DELL'APPLICAZIONE.
		if (isHistoryBackPerformed())
		{
			getContext().addMessage("startEntitiesCreation:stepCount", new FacesMessage(getResourceBundle().getString("common.cannotPerformHistoryBack.errorMessage")));
			getContext().getApplication().getNavigationHandler().handleNavigation(getContext(), null, "cannotPerformHistoryBack");
			setValidStatus(false);
		}
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void beforeInvokeApplication()
	{
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void afterInvokeApplication()
	{
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void beforeRenderResponse()
	{
		if (!isValidStatus())
			initializeAll();
		// INVIA I MESSAGGI DI ERRORE EVENTUALMENTE PRESENTI
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
				if (!isEmpty( getGlobalErrorMessage()) )
					// PUNTO ALLA VARIABILE, E NON AL METODO
					// getGlobalErrorMessage(), POICHE' ALTRIMENTI QUOTEREBBE
					// DUE VOLTE I CARATTERI SPECIALI, GENERANDO UN ERRORE
					// JAVASCRIPT
					setGlobalErrorMessage(globalErrorMessage + "\\r\\n" + message);
				else setGlobalErrorMessage(message);
			}
		}
		clearQBMessages();
	}

	/**
	 * @see EntitiesController#registerPhaseListeners()
	 */
	public void afterRenderResponse()
	{
	}

	// VALUE CHANGE LISTENERS ////////////////////////////////////
	public void nameModified(ValueChangeEvent e)
	{
		if (!isModifiedByUser())
			setModifiedByUser(true);
	}

	public void predicateModified(ValueChangeEvent e)
	{
		if (!isModifiedByUser())
			setModifiedByUser(true);
	}

	public void externalKeyModified(ValueChangeEvent e)
	{
		if (!isModifiedByUser())
			setModifiedByUser(true);
	}

	public void dateFromModified(ValueChangeEvent e)
	{
		if (!isModifiedByUser())
			setModifiedByUser(true);
	}

	public void dateToModified(ValueChangeEvent e)
	{
		if (!isModifiedByUser())
			setModifiedByUser(true);
	}

	/**
	 * <i>Change listener</i> della <i>combo</i> dove si sceglie la prima
	 * <i>User Entity</i> da mettere in relazione (join)
	 * 
	 * @see EntitiesController#getFormerEntitiesInRelation()
	 * @param e
	 */
	public void formerInRelationChoosed(ValueChangeEvent e)
	{
		if (e != null)
		{
			if (e.getNewValue() != null && !"".equals(e.getNewValue()))
				setChoosedFormerEntityInRelation(e.getNewValue().toString());
			else setChoosedFormerEntityInRelation("");
			refreshEntitiesInRelationsCombos();
		}
	}

	/**
	 * <i>Change listener</i> della <i>combo</i> dove si sceglie la seconda
	 * <i>User Entity</i> da mettere in relazione (join)
	 * 
	 * @see EntitiesController#getLatterEntitiesInRelation()
	 * @param e
	 */
	public void latterInRelationChoosed(ValueChangeEvent e)
	{
		if (e.getNewValue() != null && !"".equals(e.getNewValue()))
			setChoosedLatterEntityInRelation(e.getNewValue().toString());
		else setChoosedLatterEntityInRelation("");
		refreshEntitiesInRelationsCombos();
	}

	// ACTIONS ///////////////////////////////////////////////////
	/**
	 * Action Listenrer richamato soltanto dalla pagina che chiama la
	 * costruzione di una nuova entità. Imposta a true il <i>flag</i> che
	 * indica la validità dello stato. Se la sessione scade, le altre pagine,
	 * testando questo valore potranno rendersi conto che lo stato corrente
	 * risulta invalido, e obbligheranno di conseguentza l'utente a ripartire
	 * dall'inizio.
	 */
	public String startLife() throws Exception // TODO Exception
	{
		initializeAll();
		setUpdateStatus(false);
		return SUCCESS.outcome();
	}
	
	public String getUserEntity() throws Exception // TODO Exception
	{
		initializeAll();
		setUpdateStatus(false);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("getUserEntityBb");
		return SUCCESS.outcome();
	}
	
	public String changeUser() throws Exception 
	{
//		//azzero anche la variabile che contiene l' EntitiesListController
//		EntitiesListController elc=(EntitiesListController)getContext().getApplication().getVariableResolver().resolveVariable(getContext(), "entitiesLista");
//		elc.inizializeAll();
		initializeAll();
//		HttpSession sessione=(HttpSession)getContext().getExternalContext().getSession(false);
//		if(sessione!=null){
//			sessione.invalidate();
//		}
		return SUCCESS.outcome();
	}

	
	/**
	 * Action Listener richamato soltanto dalla pagina che chiama la costruzione
	 * di una nuova entità. Serve per entrare in editazione di una entit&agrave;
	 * gi&agrave; esistente.
	 * 
	 * @param e
	 */
	public String editUserEntity() throws Exception // TODO Exception
	{
		try
		{
			initializeAll("controller", "conditionsController", "userEntityToEdit");
			
			//carico l'entità nell'istanza di UserEntityBean utilizzata per le modifiche all'entità 
			setBuildedEntity(getController().readUserEntity(getNumericId(TABLE, getUserEntityToEdit()), getSqlGenerator()));
			System.out.println(buildedEntity.getName());
			
			// verifico se è una entità di tipo "Explicit Sql"
			if (!isEmpty(getBuildedEntity().getExplicitSql()))
			{
				setUpdateStatus(true);
				return EXPLICIT_SQL.outcome();				
			}
			//caricamento della lista delle entità che la compongono e 
			//creazione dei pannelli che graficamente le rappresentano.
			for (DcEntityBean item : getBuildedEntity().getFromList())
			{
				String key = TABLE + item.getId().toString().trim();
				if (!getChoosedEntitiesMap().containsKey(key))
					getChoosedEntitiesMap().put(key, new HashMap<Short, DcEntityBean>());
				getChoosedEntitiesMap().get(key).put(item.getAliasNum(), item);
				//il pannello è "closable" solo se l'entità non è "alreadyInUse" oppure non ci sono sue colonne selezionate
				boolean closable=false;
				if(!getBuildedEntity().isAlreadyInUse() || !hasColumnsInvolved(item)){
					closable=true;
				}
				addUIQBTablePane(item, closable);
			}
			if (getBuildedEntity().getCondition() != null)
			{
				ObjectEditor<GenericCondition> condition = new ObjectEditor<GenericCondition>(GenericCondition.initializeFromCondition(getBuildedEntity().getCondition(), this));
				condition.getEditingValue().setConditionType("filterCondition");
				condition.getEditingValue().setEditable(true);
				for (RelationRow item : condition.getEditingValue().getRelations())
					item.setEditable(true);
				condition.save();
				getRelationsDataBinding().setFilterCondition(condition);
			}
			for (JoinSpec item : getBuildedEntity().getJoins())
			{
				String key1 = TABLE + item.getFormerTable().getId() + ALIAS + item.getFormerTable().getAliasNum();
				String key2 = TABLE + item.getLatterTable().getId() + ALIAS + item.getLatterTable().getAliasNum();
				ASetAsAKey<String> key = new ASetAsAKey<String>(key1, key2);
				ObjectEditor<GenericCondition> condition = new ObjectEditor<GenericCondition>(GenericCondition.initializeFromCondition(item.getJoinCondition(), this));
				condition.getEditingValue().setEditable(item.isEditable());
				for (RelationRow item2 : condition.getEditingValue().getRelations())
					item2.setEditable(item.isEditable());
				condition.getEditingValue().setConditionType("relation");
				condition.getEditingValue().setId(item.getId());
				condition.getEditingValue().getEntitiesInvolved().add(item.getFormerTable());
				condition.getEditingValue().getEntitiesInvolved().add(item.getLatterTable());
				if (item.isOuterJoin())
					condition.getEditingValue().getOuterJoin().add(item.getLatterTable());
				condition.save();
				getRelationsDataBinding().getRelations().put(key, condition);
			}
			refreshEntitiesInRelationsCombos();

			setValidStatus(true);
			setUpdateStatus(true);
			return SUCCESS.outcome();
		}
		catch (QueryBuilderException e)
		{
			log.error(this.getClass().getName()+".editUserEntity()", e);
			appendQBMessage(new QueryBuilderMessage(e));
			return FAILURE.outcome();
		}
		catch (Exception e)
		{
			log.error(this.getClass().getName()+".editUserEntity()", e);
			appendQBMessage(new QueryBuilderMessage("common.general.errorMessage", null,e.getClass().getName(), e.getMessage()));
			return FAILURE.outcome();
		}
	}
	
	/**
	 * Action Listener richiamato dalla pressione sul pulsante "etichette" nella lista delle User Entity.
	 * @return
	 * @throws Exception
	 */
	public String editUserEntityLabels() throws Exception // TODO Exception
	{
		Map map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (map.containsKey("userEntityLabelsBb"))
			map.remove("userEntityLabelsBb");
		return "entityLabels";
	}

	private boolean hasColumnsInvolved(DcEntityBean bean)
	{
		//TODO: Implementare il controllo dell'esistenza di colonne selezionate 
		boolean hasColumns=true;
		// controllo se all'interno delle colonne non vi sia 
//		for (UserEntityBean.UserEntityColumnBean column : getBuildedEntity().getSelectList())
//		{
//			ValueExpression ve= column.getColumn().getValue();
//			if(ve instanceof Column){
//				Column col=(Column)ve;
//			}
//		} 
		return hasColumns;
	}

	/**
	 * Invalida la sessione.
	 * 
	 * @return
	 */
	public String invalidateSession() throws Exception // TODO Exception
	{
		Object session = getContext().getExternalContext().getSession(false);
		if (session instanceof HttpSession)
		{
			((HttpSession) session).invalidate();
			return SUCCESS.outcome();
		}
		return FAILURE.outcome();
	}

	// ACTION LISTENERS //////////////////////////////////////////
	/**
	 * Metodo richiamato al doppio click su una icona delle entit&agrave; utente
	 * disponibili. Il metodo fa molte cose. Anzitutto, aggiunge l'entit&agrave;
	 * a {@link EntitiesController#getChoosedEntitiesMap()}, con un alias
	 * adeguato se altre entit&agrave; con lo stesso id siano gi&agrave;
	 * presenti. Poi aggiunge una nuova {@link UIQBTablePane} per
	 * l'entit&agrave;, ovvero un dettaglio grafico dell'entit&agrave;,
	 * posizionandola nel modo giusto sullo schermo.
	 * 
	 * @param e
	 */
	public void addEntity(ActionEvent e)
	{
		WinIco winIco = (WinIco) e.getSource();
		String key = winIco.getResLabel();
		if (key != null && !"".equals(key.trim()) && winIco.isClicked())
		{
			key = key.trim();
			DcEntityBean bean = null;
			try
			{
				bean = getEntitiesMap().get(getNumericId(TABLE, key)).clone();
			}
			catch (CloneNotSupportedException e1)
			{
			}

			if (getChoosedEntitiesMap().containsKey(key))
			{
				// ORA CALCOLO L'ALIAS, SE CI TROVIAMO IN PRESENZA
				// DI TABELLE DOPPIONE
				short maxAlias = DcEntityBean.DEFAULT_ALIAS_NUM;
				for (Map.Entry<Short, DcEntityBean> item : getChoosedEntitiesMap().get(key).entrySet())
					if (maxAlias < item.getValue().getAliasNum())
						maxAlias = item.getValue().getAliasNum();
				try
				{
					bean.setAliasNum(++maxAlias);
				}
				catch (Exception e1)
				{
					appendQBMessage(new QueryBuilderMessage("entities.addEntity.tooAliases.errorMessage", null));
					return;
				}
			}
			else getChoosedEntitiesMap().put(key, new HashMap<Short, DcEntityBean>());
			getChoosedEntitiesMap().get(key).put(bean.getAliasNum(), bean);
			getBuildedEntity().addFromTable(bean);

			ArrayList<Long> otherIds = new ArrayList<Long>();
			for (String item : getChoosedEntitiesMap().keySet())
			{
				Long id = getNumericId(TABLE, item);
				if (!id.equals(bean.getId()))
					otherIds.add(id);
			}
			try
			{
				List<Object[]> risultato = getController().getRelationBetweenEntities(bean.getId(), otherIds);
				for (Object[] item : risultato)
				{
					String xml = null;
					try
					{
						xml = new String(((DcRel) item[0]).getCondition(), UTF8_XML_ENCODING);
					}
					catch (UnsupportedEncodingException e1)
					{
						log.error("", e1);
					}
					Condition condi = Condition.createFromXml(xml, getSqlGenerator());

					// String otherKey =TABLE + ( bean.getId().equals(item[1]) ?
					// item[1]: item[2]).toString();
					String otherKey = null;
					String key1 = TABLE + bean.getId();
					String key2 = null;
					int whoIsOuter = new Long(1L).equals(item[3]) ? 1 : 0;
					if (bean.getId().equals(item[1]))
					{
						otherKey = TABLE + item[2];
						whoIsOuter *= 1;
					}
					else
					{
						otherKey = TABLE + item[1];
						whoIsOuter *= 2;
					}
					key1 += ALIAS + bean.getAliasNum();

					Map<Short, DcEntityBean> ele = getChoosedEntitiesMap().get(otherKey);
					for (DcEntityBean ent : ele.values())
					{
						key2 = otherKey + ALIAS + ent.getAliasNum();

						ASetAsAKey<String> keySet = new ASetAsAKey<String>(key1, key2);
						// sostituisco gli alias con quelli corretti
						condi.changeNestedColumnAlias(bean.getId(), bean.getAlias(), bean.getAliasNum());
						condi.changeNestedColumnAlias(ent.getId(), ent.getAlias(), ent.getAliasNum());

						GenericCondition condition = GenericCondition.initializeFromCondition(condi, this);
						condition.setConditionType("relation");
						condition.getEntitiesInvolved().add(bean);
						condition.getEntitiesInvolved().add(ent);
						switch (whoIsOuter) {
						case 1:
							condition.getOuterJoin().add(bean);
							break;
						case 2:
							condition.getOuterJoin().add(ent);
							break;

						}
						getRelationsDataBinding().getRelations().put(keySet, new ObjectEditor<GenericCondition>(condition).save());
					}

				}
			}
			catch (QueryBuilderException e1)
			{
				log.error("", e1);
			}
			addUIQBTablePane(bean, true);
			refreshEntitiesInRelationsCombos();
			refreshFields();
			if (!isModifiedByUser())
				setModifiedByUser(true);
		}
	}

	/**
	 * Aggiunge la visualizzazione grafica dell'entit&agrave;
	 * 
	 * @param bean
	 *            Entit&agrave; da aggiungere
	 */
	@SuppressWarnings("unchecked")
	private void addUIQBTablePane(DcEntityBean bean, boolean closeAble)
	{
		// CALCOLO LE DIMENSIONI VISUALI
		int xMax = 0;
		int yMax = 0;
		Long zMax = 0L;
		xMax = 180 * (getChoosedEntitiesPanel().getChildren().size()) - (720 * (getChoosedEntitiesPanel().getChildren().size() / 4));
		yMax = 80 * (getChoosedEntitiesPanel().getChildren().size() / 4);
		zMax++;
		for (Object item : getChoosedEntitiesPanel().getChildren())
			if (item instanceof UIQBTablePane)
			{
				Long z = ((UIQBTablePane) item).getClientZIndex();
				if (z != null && (zMax == null || z > zMax))
					zMax = z;
			}
		UIQBTablePane newUIEntity = new UIQBTablePane();
		newUIEntity.setCloseable(closeAble);
		if (newUIEntity.isCloseable())
		{
			newUIEntity.setActionListener(getContext().getApplication().createMethodBinding("#{entitiesBb.closeEntity}", new Class[] { ActionEvent.class }));
			newUIEntity.setCloseButtonImageURL("../../pics/CLOSEQBTablePane.gif");
			String confirmationMessage = getResourceBundle().getString("entities.removeEntity.confirmationMessage");
			newUIEntity.setOnClick("confirmOn('" + confirmationMessage + "')");
		}
		newUIEntity.setId(TABLE + bean.getId() + ALIAS + bean.getAliasNum());	
		newUIEntity.setFrameName(bean.getName());
		newUIEntity.setFrameDescription(bean.getDesc());
		newUIEntity.setFrameId(newUIEntity.getId());
		newUIEntity.setSelectable(false);
		newUIEntity.setDhtml(true);
		if (bean instanceof DvUserEntityBean)
			newUIEntity.setStyleClass("dbTableAlt");
		newUIEntity.setClientPositionX(xMax);
		newUIEntity.setClientPositionY(yMax);
		newUIEntity.setClientZIndex(++zMax);
		UISelectItems columns = new UISelectItems();
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (DcColumnBean item : bean.getColumns())
		{
			// ValueExpression exp =
			// ValueExpression.createFromXml(item.getXml(), getOF(), getEF());
			String name = item.getDescription();
			items.add(new SelectItem(item.getId(), name, item.isPrimaryKey() ? "intDiv" : ""));
		}
		columns.setValue(items);
		newUIEntity.getChildren().add(columns);
		getChoosedEntitiesPanel().getChildren().add(newUIEntity);
	}

	/**
	 * Metodo chiamato alla pressione del tasto di chiusura di una
	 * entit&agrave;.
	 * 
	 * @param e
	 */
	public void closeEntity(ActionEvent e)
	{
		UIQBTablePane source = (UIQBTablePane) e.getSource();
		if (source.isClosed())
		{
			getChoosedEntitiesPanel().getChildren().remove(source);
			removeEntity(source.getFrameId());
		}
	}

	/**
	 * Chiude (elimina) una entit&agrave;, preoccupandosi di eliminarla dalla
	 * mappa delle entit&agrave; scelte.
	 * 
	 * @param id
	 */
	private void removeEntity(String id)
	{
		Matcher m = separeTableAndAlias.matcher(id);
		if (m.find())
		{
			String tableKey = m.group(1);
			Short aliasKey = Short.parseShort(m.group(4));			
			getBuildedEntity().removeFromTable(getChoosedEntitiesMap().get(tableKey).remove(aliasKey));
			if (getChoosedEntitiesMap().get(tableKey).isEmpty())
				getChoosedEntitiesMap().remove(tableKey);
			refreshEntitiesInRelationsCombos();
			removeRelationsEnvolvedInEntity(id);
			getRelationsDataBinding().setFilterCondition(null);
			refreshFields();
			if (!isModifiedByUser())
				setModifiedByUser(true);
		}
	}

	public void editFilterCondition(ActionEvent e)
	{
		if (getRelationsDataBinding().getFilterCondition() == null)
		{
			GenericCondition condition = new GenericCondition(this);
			condition.setConditionType("filterCondition");
			getRelationsDataBinding().setFilterCondition(new ObjectEditor<GenericCondition>(condition));
		}
		getRelationsDataBinding().getFilterCondition().edit();
		getRelationsDataBinding().getFilterCondition().getEditingValue().getEntitiesInvolved().clear();
		int count = 0;
		for (Map<Short, DcEntityBean> item1 : getChoosedEntitiesMap().values())
			for (DcEntityBean item2 : item1.values())
			{
				getRelationsDataBinding().getFilterCondition().getEditingValue().getEntitiesInvolved().add(item2);
				count++;
			}
		if (count > 0)
			getConditionsController().setCondition(this, getRelationsDataBinding().getFilterCondition().getEditingValue());
		else
		{
			FacesMessage errMsg = new FacesMessage(getResourceBundle().getString("entities.addFilterCond.chooseTwoEntities.errorMessage"));
			getContext().addMessage(ENTITIES_BUILDER_FORM_ID + ":addFilterCondButton", errMsg);
		}
	}

	/**
	 * Aggiunge una nuova relazione, ovvero una <i>join</i>, tra due
	 * entit&agrave;. Le relazioni sono contenute nella mappa
	 * {@link EntitiesController#getRelationsDataBinding()}, e come chiave
	 * utilizzano un {@link ASetAsAKey}, un set che restituisce un <i>hash code</i>
	 * dipendente dagli elementi contenuti, indipendentemente dal loro ordine.
	 * Il metodo fa in modo che venga aperta una <i>popup</i> che si occupa
	 * della costruzione della relazione.
	 * 
	 * @param e
	 */
	public void addRelation(ActionEvent e)
	{
		GenericCondition condition = null;
		Matcher m1 = separeTableAndAlias.matcher(getChoosedFormerEntityInRelation());
		Matcher m2 = separeTableAndAlias.matcher(getChoosedLatterEntityInRelation());
		if (m1.find() && m2.find())
		{
			ASetAsAKey<String> key = new ASetAsAKey<String>(getChoosedFormerEntityInRelation(), getChoosedLatterEntityInRelation());
			if (!getRelationsDataBinding().getRelations().containsKey(key))
			{
				condition = new GenericCondition(this);
				condition.setConditionType("relation");
				condition.setEditable(true);
				String tableKey = m1.group(1);
				Short aliasKey = Short.parseShort(m1.group(4));
				DcEntityBean former = getChoosedEntitiesMap().get(tableKey).get(aliasKey);
				condition.getEntitiesInvolved().add(0, former);
				tableKey = m2.group(1);
				aliasKey = Short.parseShort(m2.group(4));
				DcEntityBean latter = getChoosedEntitiesMap().get(tableKey).get(aliasKey);
				condition.getEntitiesInvolved().add(1, latter);
				if (isLatterEntityInOuter())
					condition.getOuterJoin().add(latter);
				getRelationsDataBinding().getRelations().put(key, new ObjectEditor<GenericCondition>(condition));
			}
			getRelationsDataBinding().getRelations().get(key).edit();
			getConditionsController().setCondition(this, getRelationsDataBinding().getRelations().get(key).getEditingValue());
		}
		else
		{
			FacesMessage errMsg = new FacesMessage(getResourceBundle().getString("entities.addRelation.chooseTwoEntities.errorMessage"));
			getContext().addMessage(ENTITIES_BUILDER_FORM_ID + ":addRelButton", errMsg);
			getConditionsController().setCondition(this, null);
		}
	}

	/**
	 * Metodo chiamato dal comando quando si sceglie il tasto di modifica di una
	 * relazione o di una condizione, apre la popup di modifica della relazione /
	 * condizione scelta.
	 * 
	 * @param e
	 */
	public void editRelation(ActionEvent e)
	{
		ASetAsAKey key = ASetAsAKey.parse(getModifingRelation());
		ObjectEditor<GenericCondition> condition = getRelationsDataBinding().getRelations().get(key);
		condition.edit();
		getConditionsController().setCondition(this, condition.getEditingValue());
	}

	/**
	 * Metodo chiamato dal comando quando si sceglie il tasto di eliminazione di
	 * una relazione, elimina la relazione scelta
	 * 
	 * @param e
	 */
	public void deleteRelation(ActionEvent e)
	{
		ASetAsAKey key = ASetAsAKey.parse(getModifingRelation());
		Long id = getRelationsDataBinding().getRelations().get(key).getValue().getId();
		if (id != null)
			getBuildedEntity().getDeletedJoins().add(id);
		getRelationsDataBinding().getRelations().remove(key);
		if (!isModifiedByUser())
			setModifiedByUser(true);
	}

	/**
	 * Metodo chiamato dal comando quando si sceglie il tasto di scambia Outer Join di
	 * una relazione, scambia (swap) di posizione le entità nella left outer join sostituendo
	 * la former con la latter e viceversa.
	 * 
	 *  former.id = latter.id(+)
	 * @param e
	 */
	public void swapRelation(ActionEvent e)
	{
		ASetAsAKey key = ASetAsAKey.parse(getModifingRelation());
		ObjectEditor<GenericCondition> condition = getRelationsDataBinding().getRelations().get(key);
		condition.edit();
		try
		{
			
			condition.getEditingValue().swapOuterJoinEntities();
			condition.save();
			getRelationsDataBinding().getRelations().get(condition.getValue().getKey()).save(condition.getValue());
		}
		catch (Exception e1)
		{
			FacesMessage errMsg = new FacesMessage(getResourceBundle().getString("entities.swapRelation.general.errorMessage"));
			getContext().addMessage(ENTITIES_BUILDER_FORM_ID + ":addRelButton", errMsg);
			log.error(e1);
		}
		
		if (!isModifiedByUser())
			setModifiedByUser(true);
	}
	/**
	 * Metodo chiamato dal comando quando si sceglie il tasto di imposta Outer Join di
	 * una relazione, imposta la left outer Join nella LatterEntity se non lo era altrimenti lo rimuove. 
	 * 
	 * 
	 * @param e
	 */
	public void toggleOuterJoinRelation(ActionEvent e)
	{
		ASetAsAKey key = ASetAsAKey.parse(getModifingRelation());
		ObjectEditor<GenericCondition> condition = getRelationsDataBinding().getRelations().get(key);
		condition.getValue().toggleOuterJoin();
		
		if (!isModifiedByUser())
			setModifiedByUser(true);
	}


	/**
	 * Il metodo viene chiamato nel momento in cui viene rimossa una delle
	 * entit&agrave; utente, vedi
	 * {@link EntitiesController#removeEntity(String)}, e rimuove tutte le
	 * relazioni che coinvolgano l'entit&agrave; da rimuovere
	 * 
	 * @param entityId
	 */
	private void removeRelationsEnvolvedInEntity(String entityId)
	{
		Set<ASetAsAKey> keys = getRelationsDataBinding().relations.keySet();
		List<Object> toRemove = new ArrayList<Object>();
		for (ASetAsAKey item : keys)
			if (item.contains(entityId))
				toRemove.add(item);
		for (Object item : toRemove)
			getRelationsDataBinding().relations.remove(item);
	}

	/**
	 * Il metodo viene chiamato alla pressione del tasto di aggiunta di un
	 * campo, e aggiunge appunto un nuovo campo alla entit&agrave; utente che
	 * stiamo creando.
	 * 
	 * @param e
	 */
	public void addNewColumn(ActionEvent e)
	{
		getBuildedEntity().setEditingValue(getBuildedEntity().addColumn(new Column(), null, null, null, false, false, false, true).getColumn());

	}

	// ACTIONS ///////////////////////////////////////////////////
	/**
	 * Il metodo viene richiamato alla pressione del tasto di salvataggio
	 * dell'entit&agrave; utente che stiamo creando. Il salvataggio procede dopo
	 * il controllo di validit&agrave;.
	 * 
	 * @return success se il salvataggio &egrave; stato eseguito con successo,
	 *         nel qual caso la navigazione procede, failure altrimenti, nel
	 *         qual caso si rivisualizza la pagina di creazione
	 *         dell'entit&agrave; utente con un adeguato messaggio di errore.
	 */
	public String saveUserEntity()
	{
		try
		{
			validate();
		}
		catch (QueryBuilderException e)
		{
			appendQBMessage(new QueryBuilderMessage(e));
			return FAILURE.outcome();
		}

		try
		{
			getBuildedEntity().getJoins().clear();
			for (ObjectEditor<GenericCondition> item : getRelationsDataBinding().getRelations().values())
				getBuildedEntity().addJoinCondition(item.getValue());

			if (getRelationsDataBinding().getFilterCondition() != null && getRelationsDataBinding().getFilterCondition().getValue() != null)
				getBuildedEntity().setCondition(getRelationsDataBinding().getFilterCondition().getValue().getCondition());

			if (!isUpdateStatus())
				getController().writeUserEntity(getBuildedEntity(), getSqlGenerator());
			else getController().updateUserEntity(getBuildedEntity(), getSqlGenerator());
			initializeAll();
			return SUCCESS.outcome();
		}
		catch (QueryBuilderException e)
		{
			// TODO Auto-generated catch block
			log.error("", e);
			appendQBMessage(new QueryBuilderMessage(e));
			return FAILURE.outcome();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error("", e);
			appendQBMessage(new QueryBuilderMessage("common.general.errorMessage", null,e.getClass().getName(), e.getMessage()));
			return FAILURE.outcome();
		}
	}

	public String editExplicitSql()
	{
		showExplicitSql(null);
		String name = getBuildedEntity().getName();
		String desc = getBuildedEntity().getDescription();
		setBuildedEntity(null);
		getBuildedEntity().setEditable(true);
		getBuildedEntity().setName(name);
		getBuildedEntity().setDescription(desc);
		getBuildedEntity().setExplicitSql(getExplicitSql());
		setShowSql(false);
		return SUCCESS.outcome();
	}

	public void showExplicitSql(ActionEvent e)
	{
		try
		{
			//verifico se è una entità SQL
			if (!isEmpty(getBuildedEntity().getExplicitSql())){
				setExplicitSql(getBuildedEntity().getExplicitSql());
			}else
			{
				getBuildedEntity().getJoins().clear();
				for (ObjectEditor<GenericCondition> item : getRelationsDataBinding().getRelations().values())
					getBuildedEntity().addJoinCondition(item.getValue());

				if (getRelationsDataBinding().getFilterCondition() != null && getRelationsDataBinding().getFilterCondition().getValue() != null)
					getBuildedEntity().setCondition(getRelationsDataBinding().getFilterCondition().getValue().getCondition());

				setExplicitSql("" + Translator.getQueryFromDvUserEntity(getBuildedEntity(), getSqlGenerator(),getController()));
			}
		}
		catch (Throwable t)
		{
			setExplicitSql("Dati non ancora sufficienti per definire la query");
		}
		setShowSql(true);
	}

	public String getExplicitSql()
	{
		return explicitSql;
	}

	public void setExplicitSql(String explicitSql)
	{
		this.explicitSql = explicitSql;
	}

	public String deleteUserEntity()
	{
		try
		{
			getController().deleteUserEntity(getNumericId(TABLE, getUserEntityToEdit()));
			initializeAll();
			return SUCCESS.outcome();
		}
		catch (QueryBuilderException e)
		{
			appendQBMessage(new QueryBuilderMessage(e));
			return FAILURE.outcome();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error("", e);
			return FAILURE.outcome();
		}
	}

	/**
	 * Controlla che sia stato dato un nome alla entit&agrave; utente, che non
	 * sia duplicato e che sia stato inserito almeno un campo se i nomi delle
	 * colonne sono univoci per questa entit&agrave;
	 * 
	 * @throws QueryBuilderException
	 */
	private void validate() throws QueryBuilderException
	{
		if (getBuildedEntity().getName() == null || "".equals(getBuildedEntity().getName().trim()))
			throw new QueryBuilderException("entities.userEntityName.required.errorMessage", ENTITIES_BUILDER_FORM_ID + ":UserEntityName");

		Object conta = getController().executeHqlStatementScalar("select count(*) from DcEntity e where e.name='" + getBuildedEntity().getName() + "'");
		if (conta != null && "1".equals(conta.toString()) && !isUpdateStatus())
			throw new QueryBuilderException("entities.userEntityName.duplicated.errorMessage", null, getBuildedEntity().getName());

		if (isEmpty(getBuildedEntity().getExplicitSql()))
		{
			if (getBuildedEntity().getFromList().isEmpty())
				throw new QueryBuilderException("entities.thatsAll.noneEntitySelected.errorMessage", null);
			if (getBuildedEntity().getSelectList().isEmpty())
				throw new QueryBuilderException("entities.thatsAll.noneColumnSelected.errorMessage", null);
			else
			{
				boolean success = true;
				Hashtable<String, String> hashNames = new Hashtable<String, String>();
				for (UserEntityColumnBean item : getBuildedEntity().getSelectList())
				{
					// Controllo che il nome sia unico all'interno della mia
					// hashtable
					// ovvero che non ci siano nomi di colonna duplicati.
					if (null == hashNames.get(item.getName()))
					{
						hashNames.put(item.getName(), item.getName());
					}
					else
					{
						throw new QueryBuilderException("entities.userEntity.fieldsTable.name.duplicated.errorMessage", null, item.getName());
					}
					String jsf_id = ENTITIES_BUILDER_FORM_ID + ":" + item.getJSFId();
					if (isEmpty(item.getName()))
					{
						success = false;
						appendQBMessage(new QueryBuilderMessage("entities.userEntity.fieldsTable.name.required.errorMessage", jsf_id));
					}
					if (item.isEditable() && item.getColumn().getValue() == null)
					{
						success = false;
						appendQBMessage(new QueryBuilderMessage("entities.userEntity.fieldsTable.value.required.errorMessage", jsf_id));
						for (UserEntityColumnBeanAppendedValue item2 : item.getAppendedExpressions())
							; // TODO VALIDAZIONE
					}
					if (item.isPrimaryKey() && ValueType.DATE.equals(item.getColumn().getValue().getValueType()))
					{
						throw new QueryBuilderException("entities.userEntity.fieldsTable.isPrimaryKey.date.errorMessage", null, item.getName());
					}
					if (item.isToDate() && !ValueType.DATE.equals(item.getColumn().getValue().getValueType()))
					{
						throw new QueryBuilderException("entities.userEntity.fieldsTable.toDate.notDate.errorMessage", null, item.getName());
					}
					if (item.isFromDate() && !ValueType.DATE.equals(item.getColumn().getValue().getValueType()))
					{
						throw new QueryBuilderException("entities.userEntity.fieldsTable.toDate.notDate.errorMessage", null, item.getName());
					}
					if (item.isExternalKey() && ValueType.DATE.equals(item.getColumn().getValue().getValueType()))
					{
						throw new QueryBuilderException("entities.userEntity.fieldsTable.isExternalKey.date.errorMessage", null, item.getName());
					}

				}
				if (!success)
					throw new QueryBuilderException("entities.thatsAll.noneColumnSelected.errorMessage", null);
			}
		}
	}

	public void update(Observable o, Object arg)
	{
	}

	// METODI DELL'INTERFACCIA ConditionsHolder
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.webred.diogene.querybuilder.control.ConditionsHolder#saveCondition(it.webred.diogene.querybuilder.control.GenericCondition)
	 */
	public void saveCondition(GenericCondition condition)
	{
		if (condition != null)
		{
			if ("relation".equals(condition.getName()))
			{
				if (getRelationsDataBinding().getRelations().containsKey(condition.getKey()))
					getRelationsDataBinding().getRelations().get(condition.getKey()).save(condition);
				else getRelationsDataBinding().getFilterCondition().save(condition);
			}
			else if ("filterCondition".equals(condition.getName()))
				getRelationsDataBinding().getFilterCondition().save(condition);
			if (!isModifiedByUser())
				setModifiedByUser(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.webred.diogene.querybuilder.control.ConditionsHolder#undoConditionUpdate(it.webred.diogene.querybuilder.control.GenericCondition)
	 */
	public void undoConditionUpdate(GenericCondition condition)
	{
		if (condition != null)
		{
			if ("relation".equals(condition.getName()))
			{
				if (getRelationsDataBinding().getRelations().get(condition.getKey()).getValue() == null)
					getRelationsDataBinding().getRelations().remove(condition.getKey());
				else getRelationsDataBinding().getRelations().get(condition.getKey()).undo();
			}
			else if ("filterCondition".equals(condition.getName()))
				getRelationsDataBinding().getFilterCondition().undo();
		}
	}

	// NON-PUBLIC GETTERS AND SETTERS ////////////////////////////
	/**
	 * @return
	 */
	boolean isValidStatus()
	{
		return validStatus;
	}

	/**
	 * @param validStatus
	 */
	void setValidStatus(boolean validStatus)
	{
		this.validStatus = validStatus;
	}

	/**
	 * La mappa contiene le entit&agrave; scelte. &Egrave; una mappa
	 * bidimensionale: la chiave principale &egrave; costituita dall'id della
	 * entit&agrave; originale, la chiave annidata rappresenta il numero di
	 * alias, ovvero il numero consecutivo del "doppione".
	 * 
	 * @return
	 */
	Map<String, Map<Short, DcEntityBean>> getChoosedEntitiesMap()
	{
		if (choosedEntitiesMap == null)
			choosedEntitiesMap = new HashMap<String, Map<Short, DcEntityBean>>();
		return choosedEntitiesMap;
	}

	/**
	 * @see EntitiesController#afterUpdateModelValues()
	 * @return
	 */
	boolean isHistoryBackPerformed()
	{
		return historyBackPerformed;
	}

	/**
	 * @see EntitiesController#setStepCount(int)
	 * @param historyBackPerformed
	 */
	void setHistoryBackPerformed(boolean historyBackPerformed)
	{
		this.historyBackPerformed = historyBackPerformed;
	}

	// FAKE GETTERS //////////////////////////////////////////////

	/**
	 * @return
	 */
	public List<SelectItem> getPredicates()
	{
		if (predicates == null)
		{
			predicates = new ArrayList<SelectItem>();
			try
			{
				for (Predicate item : getEF().getPredicates())
				{
					String definition = getEF().getDefinition(item);
					predicates.add(new SelectItem(item.getDescription(), definition));
				}
			}
			catch (Exception e)
			{
			}
		}
		return predicates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.webred.diogene.querybuilder.control.ValueExpressionsSource#getFields()
	 */
	public List<SelectItem> getFields()
	{
		if (fields == null)
		{
			fields = new ArrayList<SelectItem>();

			if (getChoosedEntitiesMap() != null)
				for (Map.Entry<String, Map<Short, DcEntityBean>> item1 : getChoosedEntitiesMap().entrySet())
					for (Map.Entry<Short, DcEntityBean> item2 : item1.getValue().entrySet())
					{
						List<SelectItem> subFields = new ArrayList<SelectItem>();
						for (DcColumnBean item3 : item2.getValue().getColumns())
						{
							String theKey=item3.isPrimaryKey()?" *":"";
							SelectItem sItem = new SelectItem();
							sItem.setLabel(item3.getDescription()+theKey);
							sItem.setValue(TABLE + item2.getValue().getId() + ALIAS + item2.getValue().getAliasNum() + COLUMN + item3.getId());
							subFields.add(sItem);
						}
						if (subFields.size() > 0)
						{
							sortSelectItemsList(subFields, getLocale(), true);
							SelectItemGroup sItemGroup = new SelectItemGroup();
							sItemGroup.setSelectItems(subFields.toArray(new SelectItem[0]));
							sItemGroup.setLabel(item2.getValue().getName());
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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
				exp.setDescription(getEF().getDefinition(exp));
				return exp;
			}
			else if (key.startsWith(TABLE))
			{
				Matcher m = separeTableAndAlias.matcher(key);
				if (m.find())
				{
					DcEntityBean table = getChoosedEntitiesMap().get(m.group(1)).get(Short.parseShort(m.group(4)));
					TableExpression sqlTable = new TableExpression(table.getId(), table.getSqlName(), table.getName(), table.getAlias());
					String UIKey = m.group(1) + m.group(3);
					if (m.group(5) != null)
					{
						UIKey += m.group(5);
						sqlTable.setOuter(true);
					}
					else sqlTable.setOuter(false);
					sqlTable.setUIKey(UIKey);
					for (DcColumnBean column : table.getColumns())
						if (new Long(m.group(7)).equals(column.getId()))
						{
							ValueExpression sqlColumn = ValueExpression.createFromXml(column.getXml(), getSqlGenerator());
							// a prescindere dalla descrizione contenuta nell'XML imposto quella della Colonna
							sqlColumn.setDescription(column.getDescription());
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
				catch (CloneNotSupportedException e)
				{
				}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.webred.diogene.querybuilder.control.MessagesHolder#getQBMessages()
	 */
	public List<QueryBuilderMessage> getQBMessages()
	{
		if (qbMessages == null)
			qbMessages = new ArrayList<QueryBuilderMessage>();
		return qbMessages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.webred.diogene.querybuilder.control.MessagesHolder#appendQBMessage(it.webred.diogene.querybuilder.control.QueryBuilderMessage)
	 */
	public void appendQBMessage(QueryBuilderMessage message)
	{
		getQBMessages().add(message);
	}

	public List<SelectItem> getUserEntityFieldsNumbers()
	{
		if (userEntityFieldsNumbers == null)
			userEntityFieldsNumbers = new ArrayList<SelectItem>();
		if (userEntityFieldsNumbers.size() != getBuildedEntity().getSelectList().size())
		{
			userEntityFieldsNumbers.clear();
			userEntityFieldsNumbers.add(nullSelectItem);
			for (int i = 1; i <= getBuildedEntity().getSelectList().size(); userEntityFieldsNumbers.add(new SelectItem("" + i, "" + i++)))
				;
		}
		return userEntityFieldsNumbers;
	}

	/**
	 * Imposta a <code>null</code> la coda dei messaggi, ovvero la svuota.
	 * 
	 * @see MessagesHolder
	 */
	private void clearQBMessages()
	{
		qbMessages = null;
	}

	/**
	 * @return L'editor delle espressioni valido in questo momento, se esiste,
	 *         ovvero l'editor relativo ad un campo della entit&agrave; utente
	 *         che stiamo creando, quando si trova in editazione.
	 */
	public ValueExpressionEditor getValueExpressionEditor()
	{
		if (valueExpressionEditor == null)
			valueExpressionEditor = new ValueExpressionEditor(getLocale(), this, this);
		if (getBuildedEntity().getEditingValue() != null)
		{
			valueExpressionEditor.setValue(getBuildedEntity().getEditingValue());
			valueExpressionEditor.setEditable(getBuildedEntity().getEditingValue().isEditable());
		}
		else valueExpressionEditor.setValue(null);
		return valueExpressionEditor;
	}

	// GETTERS AND SETTERS ///////////////////////////////////////
	public MetadataController getController()
	{
		return controller;
	}

	public void setController(MetadataController controller)
	{
		this.controller = controller;
	}

	public UIHtmlBlock getChoosedEntitiesPanel()
	{
		if (choosedEntitiesPanel == null)
			choosedEntitiesPanel = new UIHtmlBlock();
		return choosedEntitiesPanel;
	}

	public void setChoosedEntitiesPanel(UIHtmlBlock choosedEntitiesPanel)
	{
		this.choosedEntitiesPanel = choosedEntitiesPanel;
	}

	public String getLastXScrollPosition()
	{
		return lastXScrollPosition;
	}

	public void setLastXScrollPosition(String lastXScrollPosition)
	{
		this.lastXScrollPosition = lastXScrollPosition;
	}

	public String getLastYScrollPosition()
	{
		return lastYScrollPosition;
	}

	public void setLastYScrollPosition(String lastYScrollPosition)
	{
		this.lastYScrollPosition = lastYScrollPosition;
	}

	public String getNextFocusID()
	{
		return nextFocusID;
	}

	public void setNextFocusID(String nextFocusID)
	{
		this.nextFocusID = nextFocusID;
	}

	public int getStepCount()
	{
		return stepCount;
	}

	/**
	 * StepCount &egrave; un "trucco" per controllare se l'utente abbia compiuto
	 * operazioni di navigazioni indebite quali andare avanti o indietro con i
	 * tasti del browser o premere il tasto aggiorna.
	 * 
	 * @param stepCount
	 */
	public void setStepCount(int stepCount)
	{
		if (stepCount != getStepCount())
			setHistoryBackPerformed(true);
		this.stepCount = ++stepCount;
	}

	/**
	 * Questo messaggio, se presente, fa comparire un messaggio di errore
	 * globale (un alert), nella pagina entities.jsp
	 * 
	 * @return
	 */
	public String getGlobalErrorMessage()
	{
		if (globalErrorMessage != null)
			return quoteSpecialCharacters(globalErrorMessage, "\"'", "\\"); // QUOTO
		// GLI
		// APOSTROFI
		// PER
		// JavaScript
		return globalErrorMessage;
	}

	public void setGlobalErrorMessage(String globalErrorMessage)
	{
		this.globalErrorMessage = globalErrorMessage;
	}

	public RelationsBetweenEntities getRelationsDataBinding()
	{
		if (relationsDataBinding == null)
			relationsDataBinding = new RelationsBetweenEntities();
		return relationsDataBinding;
	}

	public void setRelationsDataBinding(RelationsBetweenEntities relationsDataBinding)
	{
		this.relationsDataBinding = relationsDataBinding;
	}

	public ConditionsController getConditionsController()
	{
		return conditionsController;
	}

	public void setConditionsController(ConditionsController conditionsController)
	{
		this.conditionsController = conditionsController;
	}

	public List<SelectItem> getFormerEntitiesInRelation()
	{
		if (formerEntitiesInRelation == null)
			formerEntitiesInRelation = new ArrayList<SelectItem>();
		return formerEntitiesInRelation;
	}

	public void setFormerEntitiesInRelation(List<SelectItem> formerEntitiesInRelation)
	{
		this.formerEntitiesInRelation = formerEntitiesInRelation;
	}

	public List<SelectItem> getLatterEntitiesInRelation()
	{
		if (latterEntitiesInRelation == null)
			latterEntitiesInRelation = new ArrayList<SelectItem>();
		return latterEntitiesInRelation;
	}

	public void setLatterEntitiesInRelation(List<SelectItem> latterEntitiesInRelation)
	{
		this.latterEntitiesInRelation = latterEntitiesInRelation;
	}

	public String getChoosedFormerEntityInRelation()
	{
		return choosedFormerEntityInRelation;
	}

	public void setChoosedFormerEntityInRelation(String choosedFormerEntityInRelation)
	{
		this.choosedFormerEntityInRelation = choosedFormerEntityInRelation;
	}

	public String getChoosedLatterEntityInRelation()
	{
		return choosedLatterEntityInRelation;
	}

	public void setChoosedLatterEntityInRelation(String choosedLatterEntityInRelation)
	{
		this.choosedLatterEntityInRelation = choosedLatterEntityInRelation;
	}

	public boolean isLatterEntityInOuter()
	{
		return latterEntityInOuter;
	}

	public void setLatterEntityInOuter(boolean latterEntityInOuter)
	{
		this.latterEntityInOuter = latterEntityInOuter;
	}

	public String getModifingRelation()
	{
		return modifingRelation;
	}

	public void setModifingRelation(String modifingRelation)
	{
		this.modifingRelation = modifingRelation;
	}

	public String getConditionXmlString()
	{
		return conditionXmlString;
	}

	public void setConditionXmlString(String conditionXmlString)
	{
		this.conditionXmlString = conditionXmlString;
	}

	public String getQueryString()
	{
		return queryString;
	}

	public void setQueryString(String queryString)
	{
		this.queryString = queryString;
	}

	public UserEntityBean getBuildedEntity()
	{
		if (buildedEntity == null)
		{
			buildedEntity = new UserEntityBean();
			int num = 0;
			String nomeNuovaEnt = "NuovaEntita";
			Object ret = null;
			try
			{
				while ((getController().executeHqlStatementScalar("select count(*) from DcEntity e where e.name='" + nomeNuovaEnt + "'")).toString().equals("1"))
				{
					num++;
					nomeNuovaEnt = "NuovaEntita" + num;
				}
			}
			catch (QueryBuilderException e)
			{
				e.printStackTrace();
			}
			buildedEntity.setName(nomeNuovaEnt);
		}
		if (buildedEntity.getPredicate() == null)
			buildedEntity.setPredicate(getEF().getPredicate().getDescription());
		return buildedEntity;
	}

	public void setBuildedEntity(UserEntityBean buildedEntity)
	{
		this.buildedEntity = buildedEntity;
	}

	public String getEditingExpressionKey()
	{
		return editingExpressionKey;
	}

	public void setEditingExpressionKey(String editingExpressionKey)
	{
		this.editingExpressionKey = editingExpressionKey;
	}

	public UIHtmlBlock getIconsContainer()
	{
		if (iconsContainer.getChildCount() == 0)
			initializeIconContainer();
		return iconsContainer;
	}

	public void setIconsContainer(UIHtmlBlock iconsContainer)
	{
		this.iconsContainer = iconsContainer;
	}

	/**
	 * Aggiunge ad un componente {@link UIHtmlBlock} un componente{@link WinIco}
	 * per ogni {@link DcEntityBean} disponibile
	 */
	@SuppressWarnings("unchecked")
	public void initializeIconContainer()
	{
		for (DcEntityBean item : getAvailableDcEntities())
		{
			WinIco wni = new WinIco();
			wni.setActionListener(FacesContext.getCurrentInstance().getApplication().createMethodBinding("#{entitiesBb.addEntity}", new Class[] { ActionEvent.class }));
			if (item instanceof DvUserEntityBean)
				wni.setImgSource("../../pics/262.gif");
			else wni.setImgSource("../../pics/261.gif");
			wni.setResName(item.getName());
			String value = TABLE + item.getId();
			wni.setResLabel(value);
			iconsContainer.getChildren().add(wni);
		}
	}

	private Map<Long, DcEntityBean> getEntitiesMap()
	{
		if (isUpdateStatus())
			return (getController().getEntitiesMap(getBuildedEntity().getId()));
		return getController().getEntitiesMap();
	}

	/**
	 * restituisce la lista di {@link DcEntityBean} disponibili eventualmente
	 * ricaricandola se &egrave; nulla
	 * 
	 * @return
	 */
	public List<DcEntityBean> getAvailableDcEntities()
	{
		if (availableDcEntities == null)
		{
			availableDcEntities = new ArrayList<DcEntityBean>();
			for (DcEntityBean item : getEntitiesMap().values())
				availableDcEntities.add(item);
		}
		
		//ordinamento alfabetico per nome
		Comparator comparator = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                String name1 = ((DcEntityBean)o1).getName();
                String name2 = ((DcEntityBean)o2).getName();
                return name1.compareTo(name2);
            }
        };
		Collections.sort(availableDcEntities, comparator);
		
		return availableDcEntities;
	}

	/**
	 * @param availableDcEntities
	 */
	public void setAvailableDcEntities(List<DcEntityBean> availableDcEntities)
	{
		this.availableDcEntities = availableDcEntities;
	}

	/**
	 * @return La lista per la combo delle entit&agrave; utente gi&agrave;
	 *         esistenti.
	 */
	public List<SelectItem> getAvailableUserEntities()
	{
		if (availableUserEntities == null)
		{
			availableUserEntities = new ArrayList<SelectItem>();
			for (DcEntityBean item : getAvailableDcEntities())
			{
				//escludo l'entità che modifico e quelle che hanno SQL Escpicito
				if (item instanceof DvUserEntityBean && !item.getId().equals(getBuildedEntity().getId()) && !item.isEntityExplictSQL())
					availableUserEntities.add(new SelectItem(TABLE + item.getId(), item.getName(), item.getDesc()));
			}
			sortSelectItemsList(availableUserEntities, getLocale(), true);
			availableUserEntities.add(0, nullSelectItem);
		}
		return availableUserEntities;
	}

	public boolean isUpdateStatus()
	{
		return updateStatus;
	}

	public void setUpdateStatus(boolean editingStatus)
	{
		this.updateStatus = editingStatus;
	}

	public String getUserEntityToEdit()
	{
		return userEntityToEdit;
	}

	public void setUserEntityToEdit(String userEntityToEdit)
	{
		this.userEntityToEdit = userEntityToEdit;
	}

	public boolean isModifiedByUser()
	{
		return modifiedByUser;
	}

	public void setModifiedByUser(boolean modifiedByUser)
	{
		this.modifiedByUser = modifiedByUser;
	}

	public boolean isShowSql()
	{
		return showSql; 
	}

	public void setShowSql(boolean showSql)
	{
		this.showSql = showSql;
	}

	public String getUserEntityForLabelTypes()
	{
		return userEntityForLabelTypes;
	}

	public void setUserEntityForLabelTypes(String userEntityForLabelTypes)
	{
		this.userEntityForLabelTypes = userEntityForLabelTypes;
	}

	public String getUserEntityNameForLabelTypes()
	{
		return userEntityNameForLabelTypes;
	}

	public void setUserEntityNameForLabelTypes(String userEntityNameForLabelTypes)
	{
		this.userEntityNameForLabelTypes = userEntityNameForLabelTypes;
	}
	
}
