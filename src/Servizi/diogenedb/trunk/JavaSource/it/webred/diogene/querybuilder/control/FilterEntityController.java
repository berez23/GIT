package it.webred.diogene.querybuilder.control;

import it.webred.diogene.db.model.DvUserEntity;
import it.webred.diogene.querybuilder.QueryBuilderException;
import it.webred.diogene.querybuilder.QueryBuilderMessage;
import it.webred.diogene.querybuilder.Translator;
import it.webred.diogene.querybuilder.beans.DcColumnBean;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.querybuilder.beans.DvUserEntityBean;
import it.webred.diogene.querybuilder.dataviewer.ColumnElement;
import it.webred.diogene.querybuilder.dataviewer.ColumnsDataList;
import it.webred.diogene.querybuilder.dataviewer.ResultsDataList;
import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.ConditionFactory;
import it.webred.diogene.sqldiagram.ExpressionFactory;
import it.webred.diogene.sqldiagram.OperatorFactory;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.DBType;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import it.webred.permessi.GestionePermessi;
import it.webred.utils.StringUtils;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import sun.util.logging.resources.logging;

public class FilterEntityController implements CanCallConditionController, MessagesHolder
{
	//public static final String ENTITYID_REQUEST_PARAM_NAME = FilterEntityController.class.getName() + ":ENTITYID_REQUEST_PARAM_NAME";
	public static final String ENTITYID_REQUEST_PARAM_NAME = "entity";
	private final Logger log = Logger.getLogger(this.getClass());

	private MetadataController mdController;
	private SqlGenerator sqlGen;
	private List<QueryBuilderMessage> qbMessages;
	private String sqlStatement;
	private DcEntityBean filteredEntity;
	private String explicitSql;
	private String entityId;
	private Long filterEntityId;
	private Integer fetchRows=200;
	private boolean showSql=false;

	/**
	 * @return Il contesto corrente delle Java Server Faces
	 */
	FacesContext getContext()
	{
		return FacesContext.getCurrentInstance();
	}
	
	VariableResolver getVariableResolver(){
		return getContext().getApplication().getVariableResolver();
	}
	
	public String getSqlStatement()
	{
		return sqlStatement;
	}

	public void setSqlStatement(String sqlStatement)
	{
		this.sqlStatement = sqlStatement;
	}

	public void saveCondition(GenericCondition condition)
	{
		// TODO Auto-generated method stub

	}

	public void undoConditionUpdate(GenericCondition condition)
	{
		// TODO Auto-generated method stub

	}

	public ValueExpression getExpression(String key) throws UnsupportedOperationException
	{
		// TODO Auto-generated method stub
		return null;
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


	public List<SelectItem> getFields() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	public List<SelectItem> getFunctions() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	public List<SelectItem> getChainOperators() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	public void update(Observable o, Object arg)
	{
		// TODO Auto-generated method stub

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

	public MetadataController getMdController()
	{
		return mdController;
	}

	public void setMdController(MetadataController mdController)
	{
		this.mdController = mdController;
	}

	public boolean loadEntity(){
		// recupero dalla request l'id della entità(parametro filterdEntity)
		if(getFilterEntityId()!=null){
			DcEntityBean bean= getMdController().getEntitiesMap().get(getFilterEntityId());
			setFilteredEntity(bean);
		}
		return(filteredEntity!=null);
	
	}
	public DcEntityBean getFilteredEntity()
	{
		loadEntity();
		return filteredEntity;
	}

	public void setFilteredEntity(DcEntityBean filteredEntity)
	{
		this.filteredEntity = filteredEntity;
	}
	
	public boolean isUserEntity()
	{
		return ((filteredEntity!=null )&&(filteredEntity instanceof DvUserEntityBean));
	}
	
	public String getRoundedSql(List<ColumnElement> columns)
	{
		String selectClause="";
		String comma="";
		for (ColumnElement element : columns)
		{
			selectClause+= comma+ element.getSqlName();
			comma=",";
		}
		return "SELECT "+selectClause+" FROM ("+explicitSql+")";
	}

	public String getCountSql()
	{
		
		return "select Count(*) from ("+explicitSql+")";
	}
	
	
	public String getExplicitSql()
	{
		return explicitSql;
	}
	public void setExplicitSql(String explicitSql)
	{
		this.explicitSql = explicitSql;
	}
	
	public void showExplicitSql(ActionEvent e)
	{
		setShowSql(!showSql);
	}
	public void executeSql(ActionEvent e)
	{
		
		ResultsDataList rdlst = (ResultsDataList)getVariableResolver().resolveVariable(getContext(), "resultsDataList");
		rdlst.updateDataValue(new ArrayList<Object[]>());
		ColumnsDataList cdlst = (ColumnsDataList)getVariableResolver().resolveVariable(getContext(), "columnDataList");
		List<Object[]> righe=new ArrayList<Object[]>();
		Integer realCount=0;
		String sql =null;
		HttpSession ses=(HttpSession) getContext().getExternalContext().getSession(true);
		try
		{
			if(!cdlst.validateConditions(getContext())){
				getContext().renderResponse();
				return;
			} 
			sql =getRoundedSql(cdlst.getColumns())+cdlst.getFilterCondition(getSqlGenerator());
			righe = getMdController().executeStatementList(sql,getFetchRows());
			Object count =getMdController().executeStatementScalar(getCountSql()+cdlst.getFilterCondition(getSqlGenerator()));
			realCount =Integer.parseInt(count.toString());
			
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			log.error("Errore durante l''esecuzione della query!!!  ",e1);
			log.error(sql);
			ses.setAttribute("sqlToExport",null);
		}
		if(righe.size()>0){
			Object[] intesta=(Object[]) righe.get(0); 
			for (ColumnElement cl : cdlst.getColumns())
			{
				for (int i = 0; i < intesta.length; i++)
				{
					if(intesta[i].toString().equalsIgnoreCase(cl.getSqlName())){
						intesta[i]=cl.getFieldName();
					}
				}
			}
		}
		rdlst.updateDataValue(righe);
		rdlst.setRealRowCount(realCount);
		ses.setAttribute("fileName","export.csv");
		ses.setAttribute("sqlToExport",sql);
		ses.setAttribute("idDcEntityToExport",entityId);
	}

	public String getEntityId()
	{
		return entityId;
	}

	public void setEntityId(String entityId) throws QueryBuilderException
	{
		this.entityId = entityId;
		ResultsDataList rdl=(ResultsDataList)getVariableResolver().resolveVariable(getContext(),"resultsDataList");
		// se il parametro ID è valorizzato ed è diverso da quellom memorizzato allora lo aggiorno
		if((entityId!= null)&&((getFilterEntityId()==null)||(!entityId.equals(getFilterEntityId().toString()))) ){
			Long id=null;
			try
			{

				id = Long.parseLong(entityId);
				setFilterEntityId(id);
				loadEntity();
				try
				{
					//controllo se ho i diritti di esportazione per l'utente collegato
					Principal principal = getContext().getExternalContext().getUserPrincipal();
					String application =getContext().getExternalContext().getRequestContextPath().substring(1);
					
							/* 
		TODO: REINTEGRARE LA GESTIONE PERMESSI
		*/
					
					boolean export=true; //GestionePermessi.autorizzato(principal,application,"Query Builder","Export su CSV");
					rdl.setExportable(export);
					//pulisco la lista
					rdl.updateDataValue(new ArrayList<Object[]>());
					rdl.setRealRowCount(0);
					if (isUserEntity()) {
						
						DvUserEntity ue= getMdController().getDvUserEntity(getFilteredEntity().getId());
						setExplicitSql("" + Translator.getQueryFromDvUserEntity(ue, getSqlGenerator() ).toString());
					}else{
						String selectClause="";
						String comma="";
						for (DcColumnBean col : getFilteredEntity().getColumns())
						{
							ValueExpression ve =  ValueExpression.createFromXml(col.getXml(),getSqlGenerator());
							if (ve instanceof Column)
							{
								Column colonna = (Column) ve;
								selectClause+=comma+ colonna.getExpression();
								comma=" ,";
							}
						}
						setExplicitSql("SELECT "+selectClause+" FROM "+getFilteredEntity().getSqlName());
					}
					ColumnsDataList cdlst = (ColumnsDataList)getVariableResolver().resolveVariable(getContext(), "columnDataList");

					cdlst.updateColumns(getFilteredEntity().getColumns(),getSqlGenerator()); 
				}
				catch (Throwable t)
				{
					throw new QueryBuilderException("common.buildSql.insufficientData.errorMessage",null);
				}

			}
			catch (NumberFormatException e)
			{
				// niente
			}

			
		}
	}
	
	public String getFilteredEntityParamName()
	{
		return ENTITYID_REQUEST_PARAM_NAME;
	}

	public Long getFilterEntityId()
	{
		return filterEntityId;
	}

	public void setFilterEntityId(Long filterEntityId)
	{
		this.filterEntityId = filterEntityId;
	}

	public boolean isShowSql()
	{
		return showSql;
	}

	public void setShowSql(boolean showSql)
	{
		this.showSql = showSql;
	}

	public Integer getFetchRows()
	{
		return (fetchRows!=null?fetchRows:200) ;
	}

	public void setFetchRows(Integer numRighe)
	{
		this.fetchRows = numRighe;
	}

}
