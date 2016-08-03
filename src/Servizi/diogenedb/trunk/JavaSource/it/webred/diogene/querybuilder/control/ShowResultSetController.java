package it.webred.diogene.querybuilder.control;

import it.webred.diogene.querybuilder.db.EntitiesDBManager;

import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

public class ShowResultSetController
{
	public static final String STATEMENT_REQUEST_PARAM_NAME = ShowResultSetController.class.getName() + ":STATEMENT_REQUEST_PARAM_NAME";
	
	private final Logger log = Logger.getLogger(this.getClass());

	private MetadataController controller;
	private String query;
	private UIData resultSetTableDataBinding;
	private String result = null;
	private int maxRowNum;
	
	
	public void exec(ActionEvent e)
	{
		try
		{
			/*
			if (getMaxRowNum() > 0)
			{
				Pattern p = Pattern.compile("");
				
			}
			*/
			String result = "";
			String[][] resultSet = getController().executeStatement(getQuery(), getMaxRowNum());
			if (resultSet != null)
			{
				result += "<table>\r\n";
				result += "\t<thead>\r\n\t\t<tr>\r\n";
				for (int i = 0; i < resultSet[0].length; result += "\t\t\t<td>" + resultSet[0][i++] + "</td>\r\n");
				result += "\t\t</tr>\r\n\t</thead>\r\n\t<tbody>\r\n";
				for (int row = 1; row < resultSet.length; row++)
				{
					result += "\t\t<tr>\r\n";
					
					for (String col : resultSet[row])
						result += "\t\t\t<td>" + col + "</td>\r\n";
					
					result += "\t\t</tr>\r\n";
				}
				result += "\t<tbody>\r\n</table>\r\n";
				setResult(result);
			}
		}
		catch (SQLException sqle)
		{
			setResult(sqle.getMessage());
		}
		catch (Exception e1)
		{
			log.error("", e1);
		}
	}

	/**
	 * @return Il contesto corrente delle Java Server Faces
	 */
	FacesContext getContext()
	{
		return FacesContext.getCurrentInstance();
	}

	// GETTERS AND SETTERS ////////////////////////////////////////////
	public MetadataController getController()
	{
		return controller;
	}

	public void setController(MetadataController controller)
	{
		this.controller = controller;
	}

	public String getQuery()
	{
		if (getContext().getExternalContext() != null)
		{
			ExternalContext eContext = getContext().getExternalContext();
			if (eContext.getRequestParameterMap().containsKey(STATEMENT_REQUEST_PARAM_NAME))
			{
				Object statement = eContext.getRequestParameterMap().get(STATEMENT_REQUEST_PARAM_NAME);
				if (statement != null)
					query = statement.toString();
			}
		}
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public UIData getResultSetTableDataBinding()
	{
		return resultSetTableDataBinding;
	}

	public void setResultSetTableDataBinding(UIData resultSetTableDataBinding)
	{
		this.resultSetTableDataBinding = resultSetTableDataBinding;
	}

	public String getStatementRequestParamName()
	{
		return STATEMENT_REQUEST_PARAM_NAME;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public int getMaxRowNum()
	{
		return maxRowNum;
	}

	public void setMaxRowNum(int maxRowNum)
	{
		this.maxRowNum = maxRowNum;
	}
}
