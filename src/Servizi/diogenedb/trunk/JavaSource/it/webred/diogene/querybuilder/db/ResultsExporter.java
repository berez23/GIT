package it.webred.diogene.querybuilder.db;

import it.webred.diogene.querybuilder.beans.DcColumnBean;
import it.webred.diogene.querybuilder.dataviewer.ColumnElement;
import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.RelationalOperatorType;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.DBType;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;
import it.webred.faces.utils.ComponentsUtil;
import it.webred.utils.StringUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;

public class ResultsExporter
{
	private final String COMMA_SEPATATOR_SYMBOL = ";";
	private final Logger log = Logger.getLogger(this.getClass());
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static NumberFormat numberFormat = DecimalFormat.getInstance(Locale.ITALIAN);
	private EntitiesDBManager dbManager;
	private SqlGenerator sqlGen; 
	/**
	 * Restituisce la classe di accesso ai metadati.
	 * @return
	 */
	private EntitiesDBManager getDbManager()
	{
		if (dbManager == null)
			dbManager = new EntitiesDBManager();
		return dbManager;
	}
	public SqlGenerator getSqlGenerator()
	{
		if (sqlGen == null)
			return new SqlGenerator(DBType.ORACLE);
		return sqlGen;
	}

	public void exportAsCSVByDcEntityId(HttpServletResponse res, String sql, Long dcEntityId ){
		try
		{
			
			HashSet<DcColumnBean> entityColumns=  getDbManager().getColumnsByDcEntity(dcEntityId);
			List<ColumnElement> columns= loadColumnElements(entityColumns);
			exportAsCSV(res,sql,columns);

		}
		catch (Exception e)
		{
			log.error(e);
		}
		
	}

	public void exportAsCSVByDvClasseId(HttpServletResponse res, String sql, Long dvClasseId ){
		try
		{
			
			HashSet<DcColumnBean> entityColumns=  getDbManager().getColumnsByDvClasse(dvClasseId);
			List<ColumnElement> columns= loadColumnElements(entityColumns);
			exportAsCSV(res,sql,columns);

		}
		catch (Exception e)
		{
			log.error(e);
		}
		
	}

	public void exportAsCSV(HttpServletResponse res, String sql ){
		exportAsCSV(res,sql,null);
	}
	
	public void exportAsCSV(HttpServletResponse res, String sql, List<ColumnElement> columns ){
		Connection conn = getDbManager().getJdbcConnection();
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int cols = metaData.getColumnCount();
			
			//ArrayList<Object[]> righe = new ArrayList<Object[]>();
			StringBuilder sb = new StringBuilder();
			String comma="";
			// se mi viene passata la lista dei nomi delle colonne e 
			// non è vuota allora la uso per stabilire il nome della intestazioni
			boolean iscolumnsValid= (columns!=null && columns.size()>0);
			
			for (int col = 1; col <= metaData.getColumnCount(); col++){
				sb.append(comma);
				String colName=metaData.getColumnName(col);
				if(iscolumnsValid){
					for (ColumnElement cl : columns)
					{
						if(colName.toString().equalsIgnoreCase(cl.getSqlName())){
							colName=cl.getFieldName();
							break;
						}
					}
					
				}
				sb.append(colName);
				comma=COMMA_SEPATATOR_SYMBOL;
			}
			res.getWriter().println(sb.toString());
			
			while(rs.next()){
				comma="";
				sb=new StringBuilder();
				for (int col = 1; col <= cols; col++)
				{
					sb.append(comma);
					comma=COMMA_SEPATATOR_SYMBOL;
					Object obj = rs.getObject(col);
					if (obj != null){
						if (obj instanceof Date)
						{
							Date new_name = (Date) obj;
							sb.append(dateFormat.format(new_name));
							
						}else{
							sb.append( obj.toString());
						}
					}
				}
				res.getWriter().println(sb.toString());
				res.flushBuffer();
			}

			
		}
		catch (SQLException e){
			log.error("Errore durante l''esecuzione della query!",e);
		}
		catch (IOException e){
			log.error("Errore durante la scrittura del risultato della query!",e);
		}
		finally
		{
			if (conn != null)
				try
				{
					conn.close();
				}
				catch (SQLException e)
				{
					log.error("Errore durante la chiusura della connessione!",e);
					
				}
		}
		
	}
	private List<ColumnElement> loadColumnElements(HashSet<DcColumnBean> entityColumns){
		List<ColumnElement> columns= new ArrayList<ColumnElement>();
		for (DcColumnBean bean : entityColumns)
		{
			ValueExpression ve= ValueExpression.createFromXml(bean.getXml(),getSqlGenerator());
			
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
		return columns;
	}
}
