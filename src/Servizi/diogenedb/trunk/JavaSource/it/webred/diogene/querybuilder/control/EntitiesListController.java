package it.webred.diogene.querybuilder.control;

import static it.webred.diogene.querybuilder.Constants.ALIAS;
import static it.webred.diogene.querybuilder.Constants.COLUMN;
import static it.webred.diogene.querybuilder.Constants.OUTER;
import static it.webred.diogene.querybuilder.Constants.TABLE;
import static it.webred.diogene.querybuilder.control.Constants.ENTITIES_BUILDER_FORM_ID;
import static it.webred.diogene.querybuilder.control.EntitiesController.fromDcEntityBeanToSelectItem;
import static it.webred.faces.utils.ComponentsUtil.sortSelectItemsList;
import static it.webred.utils.ReflectionUtils.clearAllFields;
import static it.webred.utils.StringUtils.isEmpty;
import static it.webred.utils.StringUtils.quoteSpecialCharacters;
import it.webred.diogene.querybuilder.QueryBuilderException;
import it.webred.diogene.querybuilder.QueryBuilderMessage;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.querybuilder.beans.DcSchemaEntityBean;
import it.webred.diogene.querybuilder.beans.DvUserEntityBean;
import it.webred.diogene.sqldiagram.Condition;
import it.webred.diogene.sqldiagram.ConditionFactory;
import it.webred.diogene.sqldiagram.ExpressionFactory;
import it.webred.diogene.sqldiagram.OperatorFactory;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.DBType;
import it.webred.permessi.GestionePermessi;
import it.webred.utils.ASetAsAKey;
import it.webred.utils.IdentitySet;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UICommand;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class EntitiesListController implements CanCallConditionController,MessagesHolder {
	
	private final Logger log = Logger.getLogger(this.getClass());
	private List <DcEntityBean>dvUserEntitiesList;
	private List <DcEntityBean>dcSchemaEntitiesList;
	private List <SelectItem> dcFormerSchemaEntitiesInRelation;
	private List <SelectItem> dcLatterSchemaEntitiesInRelation;
	private List <SelectItem> dcFormerUserEntitiesInRelation;
	private List <SelectItem> dcLatterUserEntitiesInRelation;
	private List <SimpleRelation> simpleRelationsUsers;
	private List <SimpleRelation> simpleRelationsSchema;
	private UIData forEntityTable;
	private UIData forEntitySchemaTable;
	private MetadataController mdController;
	private String
	choosedFormerEntityInRelation,
	choosedLatterEntityInRelation,
	choosedFormerUserEntityInRelation,
	choosedLatterUserEntityInRelation,
	modifingRelation;
	private DcEntityBean choosedFormerEntityBean;
	private DcEntityBean choosedLatterEntityBean;
	private DcEntityBean choosedUserFormerEntityBean;
	private DcEntityBean choosedUserLatterEntityBean;
	private ObjectEditor<GenericCondition> objEditor;
	private ConditionsController conditionsController;
	private boolean	latterSchemaEntityInOuter;
	private boolean	latterUserEntityInOuter;
	private boolean isNewUserRelationAddedOrDel=false;
	private boolean isNewSchemaRelationAddedOrDel=false;
	private boolean returnFromentitybuilder=false;
	private final SelectItem nullSelectItem = new SelectItem("", " - ", "");
	private ResourceBundle resourceBundle;
	private final String resourceBundleName = "it.webred.diogene.querybuilder.labels";
	private SqlGenerator sqlGen;
	private List<QueryBuilderMessage> qbMessages;
	private RelationsBetweenEntities relationsDataBinding;
	// ORDINAMENTO DEI GRUPPI: --> --> --> --> --> --> -->  [1                  (2   )][3                  (4   )][5            ] [6                   (7   )]
	final Pattern separeTableAndAlias = Pattern.compile("\\A(\\Q" + TABLE + "\\E(\\d+))(\\Q" + ALIAS + "\\E(\\d+))(" + OUTER + ")?(\\Q" + COLUMN + "\\E(\\d+))?\\z");
	final Pattern separeLatterAndFormer=Pattern.compile("^((dcUs)|(dcSh))((Former)|(Latter))$");
	private String globalErrorMessage;
	
	public EntitiesListController()
	{
		inizializeAll();
	}

	public List <DcEntityBean> getDvUserEntitiesList() {
		if(dvUserEntitiesList==null)dvUserEntitiesList=new ArrayList<DcEntityBean>();
		if(dvUserEntitiesList.isEmpty()|| returnFromentitybuilder )
		{
			if(returnFromentitybuilder)
				dvUserEntitiesList.clear();
			for (DcEntityBean item : getMdController().getEntitiesMap().values())
			{
				if(item instanceof DvUserEntityBean)
				{
					//item.setAlias("E_" + item.getId());
					dvUserEntitiesList.add(item);				
				}
			}
			Collections.sort(dvUserEntitiesList, new Comparator<DcEntityBean>() {
				public int compare(DcEntityBean e1, DcEntityBean e2)
				{
					if (e1 == null || isEmpty( e1.getName()))
						if (e2 == null || isEmpty(e2.getName()))
							return 0;
						else
							return -1;
					else if (e2 == null || isEmpty(e2.getName()))
						return 1;

					return e1.getName().toUpperCase().compareTo(e2.getName().toUpperCase());
				}
			} );
		}
		return dvUserEntitiesList;
	}

	public void setDvUserEntitiesList(List <DcEntityBean> dcEntitiesList) {
		this.dvUserEntitiesList = dcEntitiesList;
	}

	public MetadataController getMdController() {
		return mdController;
	}

	public void setMdController(MetadataController mdController) {
		this.mdController = mdController;
	}

	public List<DcEntityBean> getDcSchemaEntitiesList() {
		if(dcSchemaEntitiesList==null)dcSchemaEntitiesList=new ArrayList<DcEntityBean>();
		if(dcSchemaEntitiesList.isEmpty())
		{
			for (DcEntityBean item : getMdController().getEntitiesMap().values())
			{
				if(item instanceof DcSchemaEntityBean)
				{
					dcSchemaEntitiesList.add(item);					
				}
			}
			Collections.sort(dcSchemaEntitiesList, new Comparator<DcEntityBean>() {
				public int compare(DcEntityBean e1, DcEntityBean e2)
				{
					if (e1 == null || isEmpty( e1.getName()))
						if (e2 == null || isEmpty(e2.getName()))
							return 0;
						else
							return -1;
					else if (e2 == null || isEmpty(e2.getName()))
						return 1;

					return e1.getName().toUpperCase().compareTo(e2.getName().toUpperCase());
				}
			} );
			
		}
		return dcSchemaEntitiesList;
		}

	public void setDcSchemaEntitiesList(List<DcEntityBean> dcSchemaEntitiesList) {
		this.dcSchemaEntitiesList = dcSchemaEntitiesList;
	}

	public List<SelectItem> getDcFormerSchemaEntitiesInRelation() {
		if(dcFormerSchemaEntitiesInRelation==null)dcFormerSchemaEntitiesInRelation=new ArrayList<SelectItem>();
		if(dcFormerSchemaEntitiesInRelation.isEmpty())
		{
			for (DcEntityBean item : getDcSchemaEntitiesList())
			{
				if(item instanceof DcSchemaEntityBean && item.isEntityEditable())
				{
					//dcFormerSchemaEntitiesInRelation.add(new SelectItem(TABLE +item.getId(),item.getName()));
					dcFormerSchemaEntitiesInRelation.add(fromDcEntityBeanToSelectItem(item,true));
				}
			}
			sortSelectItemsList(dcFormerSchemaEntitiesInRelation, FacesContext.getCurrentInstance().getViewRoot().getLocale(), true);
			dcFormerSchemaEntitiesInRelation.add(0, nullSelectItem);
			
		}
		return dcFormerSchemaEntitiesInRelation;
			}

	public void setDcFormerSchemaEntitiesInRelation(
			List<SelectItem> dcSchemaEntitiesInRelation) {
		this.dcFormerSchemaEntitiesInRelation = dcSchemaEntitiesInRelation;
	}

	public List<SelectItem> getDcLatterSchemaEntitiesInRelation() {
		if(dcLatterSchemaEntitiesInRelation==null)dcLatterSchemaEntitiesInRelation=new ArrayList<SelectItem>();
		if(dcLatterSchemaEntitiesInRelation.isEmpty())
		{
			for (DcEntityBean item : getDcSchemaEntitiesList())
			{
				if(item instanceof DcSchemaEntityBean && item.isEntityEditable())
				{
					dcLatterSchemaEntitiesInRelation.add(fromDcEntityBeanToSelectItem(item,true));					
					//dcLatterSchemaEntitiesInRelation.add(new SelectItem(TABLE +item.getId(),item.getName()));	
				}
			}
			sortSelectItemsList(dcLatterSchemaEntitiesInRelation, FacesContext.getCurrentInstance().getViewRoot().getLocale(), true);
			dcLatterSchemaEntitiesInRelation.add(0, nullSelectItem);
			
		}
		return dcLatterSchemaEntitiesInRelation;
			}

	public void setDcLatterSchemaEntitiesInRelation(
			List<SelectItem> dcLatterSchemaEntitiesInRelation) {
		this.dcLatterSchemaEntitiesInRelation = dcLatterSchemaEntitiesInRelation;
	}

	public String getChoosedFormerEntityInRelation() {
		return choosedFormerEntityInRelation;
	}

	public void setChoosedFormerEntityInRelation(
			String choosedFormerEntityInRelation) {
		this.choosedFormerEntityInRelation = choosedFormerEntityInRelation;
	}

	public String getChoosedLatterEntityInRelation() {
		return choosedLatterEntityInRelation;
	}

	public void setChoosedLatterEntityInRelation(
			String choosedLatterEntityInRelation) {
		this.choosedLatterEntityInRelation = choosedLatterEntityInRelation;
	}
	
	//value change listener per schema former 
	public void formerChangedInCombo(ValueChangeEvent e)
	{
		if(e.getNewValue()!=null)
		{
			setChoosedFormerEntityInRelation(e.getNewValue().toString());
			Iterator<DcEntityBean> i=dcSchemaEntitiesList.iterator();
			while(i.hasNext())
			{
				DcEntityBean temp=i.next();
				if((TABLE + temp.getId()+ALIAS + temp.getAliasNum()).equals(getChoosedFormerEntityInRelation()))
					setChoosedFormerEntityBean(temp);
			}

		}
		refreshEntitiesInRelationsCombos();
	}
	//value change listener per user former 
	public void formerUserChangedInCombo(ValueChangeEvent e)
	{
		if(e.getNewValue()!=null)
		{
			setChoosedFormerUserEntityInRelation(e.getNewValue().toString());
			Iterator<DcEntityBean> i=dvUserEntitiesList.iterator();
			while(i.hasNext())
			{
				DcEntityBean temp=i.next();
				if((TABLE + temp.getId()+ALIAS + temp.getAliasNum()).equals(getChoosedFormerUserEntityInRelation()))
					setChoosedUserFormerEntityBean(temp);
			}

		}
		refreshEntitiesInRelationsCombos();
	}
	//value change listener per schema latter
	public void latterChangedInCombo(ValueChangeEvent e)
	{
		if(e.getNewValue()!=null)
		{
			setChoosedLatterEntityInRelation(e.getNewValue().toString());
			Iterator<DcEntityBean> i=dcSchemaEntitiesList.iterator();
			while(i.hasNext())
			{
				DcEntityBean temp=i.next();
				if((TABLE + temp.getId()+ALIAS + temp.getAliasNum()).equals(getChoosedLatterEntityInRelation()))
					setChoosedLatterEntityBean(temp);
			}

		}
		refreshEntitiesInRelationsCombos();

	}
	//value change listener per user latter
	public void latterUserChangedInCombo(ValueChangeEvent e)
	{
		if(e.getNewValue()!=null)
		{
			setChoosedLatterUserEntityInRelation(e.getNewValue().toString());
			Iterator<DcEntityBean> i=dvUserEntitiesList.iterator();
			while(i.hasNext())
			{
				DcEntityBean temp=i.next();
				if((TABLE + temp.getId()+ALIAS + temp.getAliasNum()).equals(getChoosedLatterUserEntityInRelation()))
					setChoosedUserLatterEntityBean(temp);
			}

		}
		refreshEntitiesInRelationsCombos();

	}
/*	public void entityChangedInCombo(ValueChangeEvent e)
	{
		String id=((UIInput)e.getSource()).getId();
		Matcher m=separeLatterAndFormer.matcher(id);
		if(m.find())
		{
			
			if(m.group(1)!=null && m.group(3)!=null)
			{
				
			}
			else if(m.group(1)!=null && m.group(4)!=null)
			{
				
			}
			else if(m.group(2)!=null && m.group(3)!=null)
			{
				
			}
			else
			{
				
			}
			
		}
		
	}
*/	
	
	
	synchronized private void refreshEntitiesInRelationsCombos()
	{
		getDcFormerSchemaEntitiesInRelation().clear();
		controlItemsInCombo(dcFormerSchemaEntitiesInRelation,getChoosedLatterEntityInRelation(),true);
		getDcLatterSchemaEntitiesInRelation().clear();
		controlItemsInCombo(dcLatterSchemaEntitiesInRelation,getChoosedFormerEntityInRelation(),true);

		getDcFormerUserEntitiesInRelation().clear();
		controlItemsInCombo(dcFormerUserEntitiesInRelation,getChoosedLatterUserEntityInRelation(),false);
		getDcLatterUserEntitiesInRelation().clear();
		controlItemsInCombo(dcLatterUserEntitiesInRelation,getChoosedFormerUserEntityInRelation(),false);

		
	}
	synchronized private void controlItemsInCombo(List<SelectItem> items, String chosed,boolean isSchema)
	{
		for (DcEntityBean item : getMdController().getEntitiesMap().values())
		{
			SelectItem sItem=fromDcEntityBeanToSelectItem(item,true);
			if(isSchema)
			{
				if(item instanceof DcSchemaEntityBean &&!sItem.getValue().equals(chosed)&& item.isEntityEditable())
				{
					items.add(sItem);					
					
				}
				
			}
			else
			{
				if(item instanceof DvUserEntityBean &&!sItem.getValue().equals(chosed)&& item.isEntityEditable())
				{
					items.add(sItem);					
					
				}
				
			}
		}
		sortSelectItemsList(items, FacesContext.getCurrentInstance().getViewRoot().getLocale(), true);
		items.add(0, nullSelectItem);
		
	}
	
	public void addRelation(ActionEvent e)
	{
		String former=null;
		String latter=null;
		DcEntityBean formerBean=null;
		DcEntityBean latterBean=null;
		String id=((UICommand)e.getSource()).getId();
		ASetAsAKey key=null;
		//int i=0;
		if(id.equals("addRelSchemaButton"))
		{
			former=getChoosedFormerEntityInRelation();
			latter=getChoosedLatterEntityInRelation();
			formerBean=getChoosedFormerEntityBean();
			latterBean=getChoosedLatterEntityBean();
			key=new ASetAsAKey<String>(former, latter);
			
		}
		else
		{
			former=getChoosedFormerUserEntityInRelation();
			latter=getChoosedLatterUserEntityInRelation();
			formerBean=getChoosedUserFormerEntityBean();
			latterBean=getChoosedUserLatterEntityBean();
			
			
			key=new ASetAsAKey<OrderedEntities>(new OrderedEntities(former,1),new OrderedEntities(latter,2));
		}
			
		Matcher m1 = separeTableAndAlias.matcher(former);
		Matcher m2 = separeTableAndAlias.matcher(latter);
		if (m1.find() && m2.find())
		{
			
			//ASetAsAKey<String> key = new ASetAsAKey<String>(former, latter);
			if (!getRelationsDataBinding().getRelations().containsKey(key))
			{
				GenericCondition condition = null;
				condition = new GenericCondition(this);
				condition.setConditionType("relation");
				condition.setEditable(true);
				condition.getEntitiesInvolved().add(formerBean);			
				condition.getEntitiesInvolved().add(latterBean);
				//condition.setPosition(new Long(i));
				if ((isLatterSchemaEntityInOuter()&& id.equals("addRelSchemaButton")))
					condition.getOuterJoin().add(latterBean);
				getRelationsDataBinding().getRelations().put(key, new ObjectEditor<GenericCondition>(condition));
				getRelationsDataBinding().getRelations().get(key).edit();
				getConditionsController().setCondition( this, getRelationsDataBinding().getRelations().get(key).getEditingValue());
			}
			else
			{
				FacesMessage errMsg = new FacesMessage(getResourceBundle().getString("entities.addRelation.stillEntitiesRelation.errorMessage"));
				getContext().addMessage(ENTITIES_BUILDER_FORM_ID + ":"+id, errMsg);
				getConditionsController().setCondition(this, null);
			}
		}
		else
		{
			FacesMessage errMsg = new FacesMessage(getResourceBundle().getString("entities.addRelation.chooseTwoEntities.errorMessage"));
			getContext().addMessage(ENTITIES_BUILDER_FORM_ID + ":"+id, errMsg);
			getConditionsController().setCondition(this, null);
		}
		
/*		objEditor=new ObjectEditor<GenericCondition>(condition);			
		objEditor.edit();
		getConditionsController().setCondition(this, this,objEditor.getEditingValue());		
*/	}
	public ConditionsController getConditionsController()
	{
		return conditionsController;
	}
	public void setConditionsController(ConditionsController conditionsController)
	{
		this.conditionsController = conditionsController;
	}

	public void saveCondition(GenericCondition condition)
	{
		if (condition != null)
		{
			if ("relation".equals(condition.getName()))
			{
				ASetAsAKey key=null;
				if(condition.getEntitiesInvolved().get(0)instanceof DcSchemaEntityBean)
					key=condition.getKey();
				else if(condition.getEntitiesInvolved().get(0)instanceof DvUserEntityBean)
					key=condition.getOrderedKeyId();
				
				if (getRelationsDataBinding().getRelations().containsKey(key))
				{
					getRelationsDataBinding().getRelations().get(key).save(condition);
					try
					{						
						getMdController().saveOrUpdateRelationInDb(condition);
						if(condition.getEntitiesInvolved().get(0)instanceof DvUserEntityBean)
						{
							isNewUserRelationAddedOrDel=true;
							//ricostruisco l'elemento della mappa							
							rebuiltRelationInMap(condition);
							//elemento ricostruito
	
						}
							
						else if(condition.getEntitiesInvolved().get(0)instanceof DcSchemaEntityBean)
							isNewSchemaRelationAddedOrDel=true;
						
					}
					catch (QueryBuilderException qe)
					{
						log.error("",qe);
						appendQBMessage(new QueryBuilderMessage(qe));
						getConditionsController().setCondition(this, null);
						
					}
					catch (Exception e)
					{
						log.error("",e);
						appendQBMessage(new QueryBuilderMessage("common.general.errorMessage", null,e.getClass().getName(), e.getMessage()));
						getConditionsController().setCondition(this, null);
						
					}
				}
				else
					getRelationsDataBinding().getFilterCondition().save(condition);			
			}
		}
	}
	private void rebuiltRelationInMap(GenericCondition condition)
	{
		ASetAsAKey key=key=condition.getOrderedKey();
		if(getRelationsDataBinding().getRelations().containsKey(key))
		{
			ObjectEditor<GenericCondition> objEditor=getRelationsDataBinding().getRelations().remove(key);
			ASetAsAKey newKey=condition.getOrderedKeyId();
			getRelationsDataBinding().getRelations().put(newKey,objEditor);			
		}
	}
	

	public void undoConditionUpdate(GenericCondition condition)
	{
		if (condition != null)
		{
			if ("relation".equals(condition.getName()))
			{
				ASetAsAKey key=null;
				if(condition.getEntitiesInvolved().get(0)instanceof DcSchemaEntityBean)
					key=condition.getKey();
				else if(condition.getEntitiesInvolved().get(0)instanceof DvUserEntityBean)
					key=condition.getOrderedKeyId();
				
				if (getRelationsDataBinding().getRelations().get(key).getValue() == null)
					getRelationsDataBinding().getRelations().remove(key);
				else
					getRelationsDataBinding().getRelations().get(key).undo();				
			}
			
		}
	
	}
	
	private RelationsBetweenEntities retrieveRelations() throws QueryBuilderException, Exception
	{
		RelationsBetweenEntities relationsToBind=new RelationsBetweenEntities();
		Map<ASetAsAKey,Object[]> relationsMap=getMdController().retrieveRelations(getSqlGenerator());
		for(Map.Entry<ASetAsAKey,Object[]> item : relationsMap.entrySet())
		{
			Long id=(Long)item.getValue()[0];
			DcEntityBean former=(DcEntityBean)item.getValue()[1];
			DcEntityBean latter=(DcEntityBean)item.getValue()[2];
			former = getMdController().getEntitiesMap().get(former.getId());
			latter = getMdController().getEntitiesMap().get(latter.getId());		
			Condition cond=(Condition)item.getValue()[3];
			GenericCondition genericCond=GenericCondition.initializeFromCondition(cond,this); 
			genericCond.setConditionType("relation");
			genericCond.getEntitiesInvolved().add(former);
			genericCond.getEntitiesInvolved().add(latter);
			genericCond.setId(id);
			
			genericCond.setDtMod((Date)item.getValue()[5]);
			Long outer=(Long)item.getValue()[4];
			if (outer.longValue()==1)
			{
				IdentitySet<DcEntityBean> iSet=new IdentitySet<DcEntityBean>();
				iSet.add(latter);
				genericCond.setOuterJoin(iSet);				
			}
			relationsToBind.getRelations().put(item.getKey(), new ObjectEditor<GenericCondition>(genericCond).save());
			
		}
		
		return relationsToBind;
	}

	public ValueExpression getExpression(String key)
	{
		throw new UnsupportedOperationException();
	}

	public SqlGenerator getSqlGenerator()
	{
		if (sqlGen == null)
			return new SqlGenerator(DBType.ORACLE);
		return sqlGen;
	}

	public ExpressionFactory getEF()
	{
		try {return getSqlGenerator().getExpressionFactory();}
		catch (Exception e) {}
		return null;
	}

	public ConditionFactory getCF()
	{
		try {return getSqlGenerator().getConditionFactory();}
		catch (Exception e) {}
		return null;
	}

	public OperatorFactory getOF()
	{
		try {return getSqlGenerator().getOperatorFactory();}
		catch (Exception e) {}
		return null;
	}

	public List<SelectItem> getFields()
	{		throw new UnsupportedOperationException();
	}

	public List<SelectItem> getFunctions()
	{		throw new UnsupportedOperationException();
	}

	public List<SelectItem> getChainOperators()
	{
		throw new UnsupportedOperationException();
	}

	public List<QueryBuilderMessage> getQBMessages()
	{
		if (qbMessages == null)
			qbMessages = new ArrayList<QueryBuilderMessage>();
		return qbMessages;
	}
	public void appendQBMessage(QueryBuilderMessage message)
	{
		getQBMessages().add(message);
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
	public DcEntityBean getChoosedFormerEntityBean()
	{
		return choosedFormerEntityBean;
	}

	public void setChoosedFormerEntityBean(DcEntityBean choosedFormerEntityBean)
	{
		this.choosedFormerEntityBean = choosedFormerEntityBean;
	}

	public DcEntityBean getChoosedLatterEntityBean()
	{
		return choosedLatterEntityBean;
	}

	public void setChoosedLatterEntityBean(DcEntityBean choosedLatterEntityBean)
	{
		this.choosedLatterEntityBean = choosedLatterEntityBean;
	}
	Locale getLocale()
	{
		return getContext().getViewRoot().getLocale();
	}
	FacesContext getContext()
	{
		return FacesContext.getCurrentInstance();
	}
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
				System.err.println("Probabilmente c'è un'errore di sintassi nel resourcebundle!!!");
				e.printStackTrace();
			}
		}
		return resourceBundle;
	}

	public ObjectEditor<GenericCondition> getObjEditor()
	{
		return objEditor;
	}

	public void setObjEditor(ObjectEditor<GenericCondition> objEditor)
	{
		this.objEditor = objEditor;
	}
	public RelationsBetweenEntities getRelationsDataBinding()
	{
		if (relationsDataBinding == null )
			try
			{
				relationsDataBinding = retrieveRelations();
			}
			catch (Exception e)
			{
				log.error("",e);
			}
		return relationsDataBinding;
	}
	public void setRelationsDataBinding(RelationsBetweenEntities relationsDataBinding)
	{
		this.relationsDataBinding = relationsDataBinding;
	}
	/**
	 * Metodo chiamato dal comando quando si sceglie il tasto 
	 * di modifica di una relazione o di una condizione, apre
	 * la popup di modifica della relazione / condizione scelta.
	 * @param e
	 */
	public void editRelation(ActionEvent e)
	{
		ASetAsAKey key=null;
		setRelationsDataBinding(null);//svuoto la lista delle relazione perché sia ricaricata dal db.
		int aliasRel=0;
		String id=((UICommand)e.getSource()).getId();
		//String clientId=((UICommand)e.getSource()).getClientId(getContext());
		if(id.equals("modificaRelazioneUserButton"))
		{
			 //key = ASetAsAKey.parse(((SimpleRelation)forEntityTable.getRowData()).getIdentifier());
			DcEntityBean former=((SimpleRelation)forEntityTable.getRowData()).getFormerEntity();
			DcEntityBean latter=((SimpleRelation)forEntityTable.getRowData()).getLatterEntity();
			aliasRel=((SimpleRelation)forEntityTable.getRowData()).getAliasRel();
			String formerId=TABLE + former.getId() + ALIAS + aliasRel;
			String latterId=TABLE + latter.getId() + ALIAS + aliasRel;
			key=new ASetAsAKey<OrderedEntities>(new OrderedEntities(formerId,1),new OrderedEntities(latterId,2));
			isNewUserRelationAddedOrDel=true;
		}
		else if(id.equals("modificaRelazioneSchemaButton"))
		{
			 key = ASetAsAKey.parse(((SimpleRelation)forEntitySchemaTable.getRowData()).getIdentifier());
			 isNewSchemaRelationAddedOrDel=true;
		}
		if(getRelationsDataBinding().getRelations().containsKey(key))
		{
			ObjectEditor<GenericCondition> condition = getRelationsDataBinding().getRelations().get(key);
			condition.edit();
			//condition.getEditingValue().setPosition(new Long(aliasRel));
			getConditionsController().setCondition(this, condition.getEditingValue());
		}
		else
		{
/*			FacesMessage errMsg = new FacesMessage(getResourceBundle().getString("entities.addRelation.chooseTwoEntities.errorMessage"));
			getContext().addMessage(clientId, errMsg);
*/			getConditionsController().setCondition(this, null);			
		}
	}
	/**
	 * Metodo chiamato dal comando quando si sceglie il tasto 
	 * di eliminazione di una relazione, elimina
	 * la relazione scelta
	 * @param e
	 */
	public void deleteRelation(ActionEvent e)
	{
		ASetAsAKey key=null;
		setRelationsDataBinding(null);
		if(((UICommand)e.getSource()).getId().equals("eliminaRelazioneUserButton"))
		{
			 //key = ASetAsAKey.parse(((SimpleRelation)forEntityTable.getRowData()).getIdentifier());
			DcEntityBean former=((SimpleRelation)forEntityTable.getRowData()).getFormerEntity();
			DcEntityBean latter=((SimpleRelation)forEntityTable.getRowData()).getLatterEntity();
			int aliasRel=((SimpleRelation)forEntityTable.getRowData()).getAliasRel();
			String formerId=TABLE + former.getId() + ALIAS + aliasRel;
			String latterId=TABLE + latter.getId() + ALIAS + aliasRel;
			key=new ASetAsAKey<OrderedEntities>(new OrderedEntities(formerId,1),new OrderedEntities(latterId,2));
		}
			
		else if(((UICommand)e.getSource()).getId().equals("eliminaRelazioneSchemaButton"))
			 key = ASetAsAKey.parse(((SimpleRelation)forEntitySchemaTable.getRowData()).getIdentifier());
		try
		{
			if(getRelationsDataBinding().getRelations().containsKey(key))
				getMdController().deleteUpdateRelationInDb(getRelationsDataBinding().getRelations().remove(key).getValue());
			if(((UICommand)e.getSource()).getId().equals("eliminaRelazioneUserButton"))
				isNewUserRelationAddedOrDel=true;
			else if(((UICommand)e.getSource()).getId().equals("eliminaRelazioneSchemaButton"))
				isNewSchemaRelationAddedOrDel=true;
		}
		catch (Exception e1)
		{
			log.error("",e1);
		}
		
		
	}

	public String getModifingRelation()
	{
		return modifingRelation;
	}

	public void setModifingRelation(String modifingRelation)
	{
		this.modifingRelation = modifingRelation;
	}

	public void update(Observable o, Object arg)
	{
		// TODO Auto-generated method stub
		
	}

	synchronized public List<SelectItem> getDcFormerUserEntitiesInRelation()
	{
		if(dcFormerUserEntitiesInRelation==null)dcFormerUserEntitiesInRelation=new ArrayList<SelectItem>();
		if(dcFormerUserEntitiesInRelation.isEmpty()|| returnFromentitybuilder)
		{
			if(returnFromentitybuilder)
				dcFormerUserEntitiesInRelation.clear();
			for (DcEntityBean item :getDvUserEntitiesList())
			{
				if(item instanceof DvUserEntityBean && item.isEntityEditable())
				{
					dcFormerUserEntitiesInRelation.add(fromDcEntityBeanToSelectItem(item,true));
				}
			}
			sortSelectItemsList(dcFormerUserEntitiesInRelation, FacesContext.getCurrentInstance().getViewRoot().getLocale(), true);
			dcFormerUserEntitiesInRelation.add(0, nullSelectItem);
			
		}
		return dcFormerUserEntitiesInRelation;
			}

	public void setDcFormerUserEntitiesInRelation(List<SelectItem> dcFormerUserEntitiesInRelation)
	{
		this.dcFormerUserEntitiesInRelation = dcFormerUserEntitiesInRelation;
	}

	synchronized public List<SelectItem> getDcLatterUserEntitiesInRelation()
	{
		if(dcLatterUserEntitiesInRelation==null)dcLatterUserEntitiesInRelation=new ArrayList<SelectItem>();
		if(dcLatterUserEntitiesInRelation.isEmpty()|| returnFromentitybuilder)
		{
			if(returnFromentitybuilder)
				dcLatterUserEntitiesInRelation.clear();

			for (DcEntityBean item : getDvUserEntitiesList())
			{
				if(item instanceof DvUserEntityBean && item.isEntityEditable())
				{
					dcLatterUserEntitiesInRelation.add(fromDcEntityBeanToSelectItem(item,true));					
				}
			}
			sortSelectItemsList(dcLatterUserEntitiesInRelation, FacesContext.getCurrentInstance().getViewRoot().getLocale(), true);
			dcLatterUserEntitiesInRelation.add(0, nullSelectItem);
			
		}
		return dcLatterUserEntitiesInRelation;
			}

	public void setDcLatterUserEntitiesInRelation(List<SelectItem> dcLatterUserEntitiesInRelation)
	{
		this.dcLatterUserEntitiesInRelation = dcLatterUserEntitiesInRelation;
	}

	public String getChoosedFormerUserEntityInRelation()
	{
		return choosedFormerUserEntityInRelation;
	}

	public void setChoosedFormerUserEntityInRelation(String choosedFormerUserEntityInRelation)
	{
		this.choosedFormerUserEntityInRelation = choosedFormerUserEntityInRelation;
	}

	public String getChoosedLatterUserEntityInRelation()
	{
		return choosedLatterUserEntityInRelation;
	}

	public void setChoosedLatterUserEntityInRelation(String choosedLatterUserEntityInRelation)
	{
		this.choosedLatterUserEntityInRelation = choosedLatterUserEntityInRelation;
	}

	public DcEntityBean getChoosedUserFormerEntityBean()
	{
		return choosedUserFormerEntityBean;
	}

	public void setChoosedUserFormerEntityBean(DcEntityBean choosedUserFormerEntityBean)
	{
		this.choosedUserFormerEntityBean = choosedUserFormerEntityBean;
	}

	public DcEntityBean getChoosedUserLatterEntityBean()
	{
		return choosedUserLatterEntityBean;
	}

	public void setChoosedUserLatterEntityBean(DcEntityBean choosedUserLatterEntityBean)
	{
		this.choosedUserLatterEntityBean = choosedUserLatterEntityBean;
	}

	public UIData getForEntityTable()
	{
		return forEntityTable;
	}

	public void setForEntityTable(UIData forEntityTable)
	{
		this.forEntityTable = forEntityTable;
	}

	public List<SimpleRelation> getSimpleRelationsUsers()
	{
		if(simpleRelationsUsers==null || isNewUserRelationAddedOrDel)
		{
			simpleRelationsUsers=new ArrayList<SimpleRelation>();
			for(Map.Entry<ASetAsAKey,ObjectEditor<GenericCondition>> item : getRelationsDataBinding().getRelations().entrySet())
			{
				
				if(item.getValue().getValue().getEntitiesInvolved().get(0) instanceof DvUserEntityBean)					
				{
					String key=item.getKey().toString();
					key=key.substring(0,key.indexOf(','));
					int alias=999;
					Matcher m=separeTableAndAlias.matcher(key);
					if(m.find())
						alias=new Integer(m.group(4));
					DcEntityBean former=item.getValue().getValue().getEntitiesInvolved().get(0);
					DcEntityBean latter=item.getValue().getValue().getEntitiesInvolved().get(1);
					boolean editable=item.getValue().getValue().isEditable()&&(latter.isEntityEditable()&&former.isEntityEditable());
					SimpleRelation simplRel=new SimpleRelation();
					simplRel.setAliasRel(alias);
					simplRel.setFormerEntity(former);
					simplRel.setLatterEntity(latter);
					simplRel.setIdentifier(item.getKey().toString());
					simplRel.setNameRelation(former.getName()+ ">>>"+latter.getName()+"("+ alias+")");
					simplRel.setEditable(editable);
					simpleRelationsUsers.add(simplRel);
				}
			}
			isNewUserRelationAddedOrDel=false;
		}
		return simpleRelationsUsers;
	}

	public void setSimpleRelationsUsers(List<SimpleRelation> simpleRelationsUsers)
	{
		this.simpleRelationsUsers = simpleRelationsUsers;
	}

	public List<SimpleRelation> getSimpleRelationsSchema()
	{
		if(simpleRelationsSchema==null || isNewSchemaRelationAddedOrDel)
		{
			simpleRelationsSchema=new ArrayList<SimpleRelation>();
			for(Map.Entry<ASetAsAKey,ObjectEditor<GenericCondition>> item : getRelationsDataBinding().getRelations().entrySet())
			{
				
				if(item.getValue().getValue().getEntitiesInvolved().get(0) instanceof DcSchemaEntityBean )
				{
					DcEntityBean former=item.getValue().getValue().getEntitiesInvolved().get(0);
					DcEntityBean latter=item.getValue().getValue().getEntitiesInvolved().get(1);
					boolean editable=item.getValue().getValue().isEditable()&&(latter.isEntityEditable() && former.isEntityEditable()) ;
					SimpleRelation simplRel=new SimpleRelation();
					simplRel.setFormerEntity(former);
					simplRel.setLatterEntity(latter);
					simplRel.setIdentifier(item.getKey().toString());
					boolean outer=item.getValue().getValue().getOuterJoin().contains(latter);
					simplRel.setNameRelation(former.getName()+">>>"+latter.getName()+(!outer?"":" (+)"));
					simplRel.setEditable(editable);
					simpleRelationsSchema.add(simplRel);
				}
			}
			isNewSchemaRelationAddedOrDel=false;
		}
		return simpleRelationsSchema;
	}

	public void setSimpleRelationsSchema(List<SimpleRelation> simpleRelationsSchema)
	{
		this.simpleRelationsSchema = simpleRelationsSchema;
	}

	public UIData getForEntitySchemaTable()
	{
		return forEntitySchemaTable;
	}

	public void setForEntitySchemaTable(UIData forEntitySchemaTable)
	{
		this.forEntitySchemaTable = forEntitySchemaTable;
	}

	public boolean isLatterSchemaEntityInOuter()
	{
		return latterSchemaEntityInOuter;
	}

	public void setLatterSchemaEntityInOuter(boolean latterSchemaEntityInOuter)
	{
		this.latterSchemaEntityInOuter = latterSchemaEntityInOuter;
	}

	public boolean isLatterUserEntityInOuter()
	{
		return latterUserEntityInOuter;
	}

	public void setLatterUserEntityInOuter(boolean latterUserEntityInOuter)
	{
		this.latterUserEntityInOuter = latterUserEntityInOuter;
	}
	/**
	 * Invalida la sessione.
	 * @return 
	 */
	public void invalidateSession(ActionEvent e)
	throws Exception // TODO Exception
	{
		inizializeAll();
//		Object session = getContext().getExternalContext().getSession(false);
//		if (session instanceof HttpSession)
//		{
//			((HttpSession) session).invalidate();
//		}
	}
	public void returnFromEntityBuilder(ActionEvent e)
	{
		//returnFromentitybuilder=true;
		setDvUserEntitiesList(new ArrayList<DcEntityBean>());
		setDcSchemaEntitiesList(new ArrayList<DcEntityBean>());
		getMdController().setReloadNextRead(true);
	}

	public boolean isReturnFromentitybuilder()
	{
		return returnFromentitybuilder;
	}

	public void setReturnFromentitybuilder(boolean returnFromentitybuilder)
	{
		this.returnFromentitybuilder = returnFromentitybuilder;
	}
	public String filterEntityAction()
	{
		return "filterEntity";
	}

	public boolean isCanCreateRelation()
	{
			/* 
		TODO: REINTEGRARE LA GESTIONE PERMESSI
		*/
	
		return true; //GestionePermessi.autorizzato(getPrincipal(),getWebAppName(),"Query Builder","Definisci Relazioni");
	}


	public boolean isCanCreateUserEntity()
	{
			/* 
		TODO: REINTEGRARE LA GESTIONE PERMESSI
		*/
	
		return true ;// GestionePermessi.autorizzato(getPrincipal(),getWebAppName(),"Query Builder","Crea entità Utente");
	}


	public boolean isCanMapSchemaEntity()
	{
			/* 
		TODO: REINTEGRARE LA GESTIONE PERMESSI
		*/
	
		return true; //GestionePermessi.autorizzato(getPrincipal(),getWebAppName(),"Query Builder","Mapping Schema Dati");
	}

	private Object getPrincipal()
	{
		return getContext().getExternalContext().getUserPrincipal();
	}
	private String getWebAppName()
	{
		return getContext().getExternalContext().getRequestContextPath().substring(1);
	}

	public void inizializeAll() {
		dvUserEntitiesList=new ArrayList <DcEntityBean>();
		dcSchemaEntitiesList=new ArrayList <DcEntityBean>();
		dcFormerSchemaEntitiesInRelation=new ArrayList <SelectItem>();
		dcLatterSchemaEntitiesInRelation=new ArrayList <SelectItem>();
		dcFormerUserEntitiesInRelation=new ArrayList <SelectItem>();
		dcLatterUserEntitiesInRelation=new ArrayList <SelectItem>();
	}

}
