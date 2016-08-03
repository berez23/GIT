package it.webred.diogene.querybuilder.dataviewer;

import it.webred.diogene.querybuilder.beans.DcColumnBean;
import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.Condition;
import it.webred.diogene.sqldiagram.Function;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.LogicalOperator;
import it.webred.diogene.sqldiagram.RelationalOperator;
import it.webred.diogene.sqldiagram.RelationalOperatorType;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import it.webred.faces.utils.ComponentsUtil;
import it.webred.utils.StringUtils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.map.StaticBucketMap;
import org.hibernate.mapping.Value;

import sun.security.action.GetLongAction;

public class ColumnsDataList 
{
	private List<ColumnElement> columns = new ArrayList<ColumnElement>();
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	public ColumnsDataList()
	{
	}
//	public ColumnsDataList( HashSet<? extends DcColumnBean> entityColumns ){
//		updateColumns(entityColumns,null);
//	}
	public void updateColumns( HashSet<? extends DcColumnBean> entityColumns, SqlGenerator generator ){
		columns.clear();
		
		for (DcColumnBean bean : entityColumns)
		{
			ValueExpression ve= ValueExpression.createFromXml(bean.getXml(),generator);
			
			ColumnElement col = new ColumnElement();
			col.setId(bean.getId());
			col.setFieldName(bean.getDescription());
			col.setValueType( ValueType.mapJavaType2ValueType( bean.getJavaType()));
			col.setOperatorsList(ValueType.getRelationalOperatorsList( col.getValueType(), RelationalOperatorType.BETWEEN,RelationalOperatorType.NOTBETWEEN,RelationalOperatorType.IN));
			if ((ve instanceof Column) && (StringUtils.isEmpty(bean.getAlias())) )
			{
				Column c = (Column) ve;
				col.setSqlName(c.getExpression());
				
			}else{
				col.setSqlName(bean.getAlias());
			}

			ComponentsUtil.sortSelectItemsList(col.getOperatorsList(),null,true);
			columns.add(col);
		}
		Collections.sort( columns);
	}
	public List<ColumnElement> getColumns()
	{
		return columns;
	}

	public void setColumns(List<ColumnElement> results)
	{
		this.columns = results;
	}
	public boolean validateConditions(FacesContext fc){
		boolean ret=true;
		for (ColumnElement column : columns)
		{
			//se l'operatore non è inserito ma è inserito il valore del campo allora segnalo l'errore
			if (StringUtils.isEmpty(column.getOperator()) && (!StringUtils.isEmpty(column.getValue()) || column.getValueDate()!=null ) ){
				FacesMessage mess= new FacesMessage("Inserire anche l'operatore della condizione per il campo "+column.getFieldName());
				fc.addMessage(null,mess);
				ret=false;
			}
			//se l'operatore è inserito ma non è inserito il valore allora segnalo l'errore
			if (!StringUtils.isEmpty(column.getOperator()) && (StringUtils.isEmpty(column.getValue()) && column.getValueDate()==null ) ){
				FacesMessage mess= new FacesMessage("Inserire anche il valore della condizione per il campo "+column.getFieldName());
				fc.addMessage(null,mess);
				ret=false;
			}
		}
		
		return ret;
	}
	public String getFilterCondition(SqlGenerator generator ) throws Exception
	{
		String cond="";
		Condition con = generator.getConditionFactory().getCondition();
		for (ColumnElement column : columns)
		{
			if ((!StringUtils.isEmpty(column.getOperator())&&(!StringUtils.isEmpty(column.getValue()) ||column.getValueDate()!=null ) )){
				ValueExpression col1 =null;
				ValueExpression col2 =null;
				if(column.getValueType()==ValueType.DATE){
					//Utilizzo la funzione  char2Date
					
					String dateValue=dateFormat.format(column.getValueDate());
					ValueExpression col3 =new LiteralExpression(dateValue,null,column.getValueType());
					Function fun =(Function) generator.getExpressionFactory().getExpression("char2Date");
					fun.addArgument(col3);
					fun.addArgument(new LiteralExpression("dd/mm/yyyy","",ValueType.STRING));
					col1 = new Column(column.getSqlName(),"",null,null,column.getValueType());
					col2=fun;
				}else if(column.getValueType()==ValueType.STRING){
					String valore="";
					if(column.getOperator().equals("like")){
						valore="%"+column.getValue()+"%";	
					}else{
						valore=column.getValue();	
					}
					ValueExpression par1 =new Column(column.getSqlName(),"",null,null,column.getValueType());
					ValueExpression par2 =new LiteralExpression(valore,null,column.getValueType());
					// 18-1-2012 : TOLA LA UPPER DALLA PARTE SX DELLA CONDIZIONE
					//Function fun1 =(Function) generator.getExpressionFactory().getExpression("upper");
					//fun1.addArgument(par1);
					Function fun2 =(Function) generator.getExpressionFactory().getExpression("upper");
					fun2.addArgument(par2);
					col1 = par1;
					col2 = fun2;
				}else{
					//utilizzo una semplice Literal
					col1 = new Column(column.getSqlName(),"",null,null,column.getValueType());
					col2 = new LiteralExpression(column.getValue(),null,column.getValueType());	
				}
				RelationalOperator rel= generator.getOperatorFactory().getOperator(column.getOperator());
				LogicalOperator log= generator.getOperatorFactory().getLogicalOperator("and");
				rel.getOperands()[0] = col1;
				rel.getOperands()[1] = col2;
				con.addSimpleCondition(rel,log);
			}
		}
		if(!StringUtils.isEmpty(con.toString())){
			cond="WHERE "+con.toString();
		}
		return cond;
	}
}
