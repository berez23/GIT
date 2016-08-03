package it.escsolution.escwebgis.common;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExportLogic extends EscLogic{
	
	private static final Logger log = Logger.getLogger(ExportLogic.class.getName());
	
	private static final DecimalFormat DF = new DecimalFormat("0.00");
	
	public static final String TRATTAMENTO_NUM_DIVIDE = "DIVIDE";
	public static final String TRATTAMENTO_NUM_MULTIPLY = "MULTIPLY";

	//private String appoggioDataSource = "";
	
	public ExportLogic(EnvUtente eu){
		super(eu);
		//appoggioDataSource = eu.getDataSource();
	}//-------------------------------------------------------------------------
	
	public Workbook mEsporta( String qry, Vector<EscElementoFiltro> elemFiltri, String nomeFile, Hashtable<String, String> htHeaderLabel, String[] aryAttrOrdine, Hashtable<String, String[]> htTrattamenti) throws Exception{
		/*
		 * htHeaderLabel è un hashtable di String cosi strutturato:
		 * el0: [nome_campo_db, etichetta]
		 * el1: [nome_campo_db, etichetta]
		 * el2: [nome_campo_db, etichetta]
		 * ...
		 * elN: [nome_campo_db, etichetta] 
		 * 
		 * per il momento si assume che tutti i valori siano di tipo String
		 */
		/*
		 * htTrattamenti è una hashtable cosi strutturata:
		 * el0: String [nome_campo_db], String[] [tipo_di_trattamento, valore]
		 */
		Connection conn = null;
		// faccio la connessione al db
		Workbook wb = new HSSFWorkbook();
		try{
			conn = this.getConnection();
			PreparedStatement pst = conn.prepareStatement(qry);
			log.debug(qry);
			
			pst.setInt(1, 1);
			int indice = 2;
			Enumeration en = elemFiltri.elements();
			while (en.hasMoreElements())
			{
				EscElementoFiltro filtro = (EscElementoFiltro) en.nextElement();
				if (filtro.getValore()!=null && !filtro.getValore().trim().equals("") ){	
					pst.setString(indice, filtro.getValore());
					indice++;
				}
			}
			ResultSet resultSet = pst.executeQuery();
			ResultSetMetaData rsMetaData = resultSet.getMetaData();
			
			
			Sheet sheet = wb.createSheet(nomeFile);
			Row headerRow = sheet.createRow(0);
			/*
			 * Genero al riga di intestazione
			 */
			 Font headerFont = wb.createFont();
		     headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		     headerFont.setFontHeightInPoints((short) 12);
		     
		     Font bodyFont = wb.createFont();
		     //bodyFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		     bodyFont.setFontHeightInPoints((short) 10);
		     
			 CellStyle headerStyle = wb.createCellStyle();
		     headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		     headerStyle.setFont(headerFont);
		     
			 CellStyle bodyStyle = wb.createCellStyle();
			 bodyStyle.setAlignment(CellStyle.ALIGN_LEFT);
			 bodyStyle.setFont(bodyFont);
		        
			 //style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
			 CellStyle numberStyle = wb.createCellStyle();
			 numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);
			 numberStyle.setFont(bodyFont);
			 numberStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
			 
		        for (int j = 0; j < aryAttrOrdine.length; j++){
					 for (int k = 0; k < rsMetaData.getColumnCount(); k++) {
				        String columnName =  rsMetaData.getColumnName(k+1) ;
				        if (columnName.trim().equalsIgnoreCase(aryAttrOrdine[j]) && htHeaderLabel.containsKey( (String)columnName )){
				        	String columnLabel = (String)htHeaderLabel.get( columnName );
							Cell headerCell = headerRow.createCell(j);
							headerCell.setCellValue( columnLabel );
							headerCell.setCellStyle(headerStyle);
	
				        }
		        }
		    }
			 
		
			int row = 1;
			while(resultSet.next()) {
				/*
				 * Genero la riga
				 */
			    Row dataRow = sheet.createRow(row);
			    /*
			     * Valorizzo le celle per la riga corrente
			     */
				 for (int z = 0; z < aryAttrOrdine.length; z++){
					 
					 for (int j=0; j<rsMetaData.getColumnCount(); j++){
						 String columnName =  rsMetaData.getColumnName(j+1) ;
						 if (columnName.trim().equalsIgnoreCase(aryAttrOrdine[z]) && htHeaderLabel.containsKey( (String)columnName )){
							    /*
							     * Genero la cella e la valorizzo
							     */
							    Cell dataCell = dataRow.createCell(z);
							    int colType = rsMetaData.getColumnType(j+1);
							    String valoreAttributo = getColumnValue(resultSet, colType, j+1);	
							    if (htTrattamenti.containsKey( (String)columnName )){
							    	String[] aryTrat = htTrattamenti.get( (String)columnName );
							    	String tipoTrat = aryTrat[0];
							    	String valueTrat = aryTrat[1];
							    	
							    	if (tipoTrat.equals(TRATTAMENTO_NUM_DIVIDE)){
							    		if (valoreAttributo!=null & !valoreAttributo.trim().equalsIgnoreCase("")){
								    		BigDecimal attrValue = new BigDecimal( valoreAttributo ).divide( new BigDecimal(valueTrat) );
								    		valoreAttributo = DF.format(attrValue.doubleValue());
							    		}else
							    			valoreAttributo = "";
							    		
							    	}else if (tipoTrat.equals(TRATTAMENTO_NUM_MULTIPLY)){
							    		if (valoreAttributo!=null & !valoreAttributo.trim().equalsIgnoreCase("")){
								    		BigDecimal attrValue = new BigDecimal(valoreAttributo).multiply( new BigDecimal(valueTrat) );
								    		valoreAttributo = DF.format(attrValue.doubleValue());
							    		}else
							    			valoreAttributo = "";
							    	}
							    	
							    }
								
							    dataCell.setCellStyle(bodyStyle);
							    if (colType == 2){
							    	if (valoreAttributo != null && !valoreAttributo.trim().equalsIgnoreCase("")){
								    	if (valoreAttributo != null && (valoreAttributo.indexOf(',') > -1  || valoreAttributo.indexOf('.') > -1) ){
								    		//decimale
									    	dataCell.setCellValue( new BigDecimal(valoreAttributo.replace(',', '.')).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()  );
									    	dataCell.setCellStyle(numberStyle);
								    	}else{
								    		//intero
									    	dataCell.setCellValue( new BigDecimal(valoreAttributo).intValue()  );
									    	dataCell.setCellStyle(numberStyle);
								    	}
							    	}
							    	dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

							    }else{
							    	dataCell.setCellValue(valoreAttributo);
							    }
					     }
					 }				     
				}
			    row = row + 1;
			}
			
			resultSet.close();
			pst.close();
			/*
			 * scrivo file
			 */
			
//			String outputDirPath = "T:/" + nomeFile;
//			FileOutputStream fileOut = new FileOutputStream(outputDirPath);
//			wb.write(fileOut);
//			fileOut.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		return wb;
	}//-------------------------------------------------------------------------
	
	private String getColumnValue(ResultSet rs, int colType, int colIndex) throws Exception {

	    String value = "";

	    switch (colType){

	        case Types.BIT:
	        case Types.JAVA_OBJECT:
	            value = handleObject(rs.getObject(colIndex));
	            break;
	        case Types.BOOLEAN:
	            boolean b = rs.getBoolean(colIndex);
	            value = Boolean.valueOf(b).toString();
	            break;
	        case Types.NCLOB:
	        case Types.CLOB:
	        case Types.BIGINT:
	            value = handleLong(rs, colIndex);
	            break;
	        case Types.DECIMAL:
	        case Types.DOUBLE:
	        case Types.FLOAT:
	        case Types.REAL:
	        case Types.NUMERIC:
	            value = handleBigDecimal(rs.getBigDecimal(colIndex));
	            break;
	        case Types.INTEGER:
	        case Types.TINYINT:
	        case Types.SMALLINT:
	            value = handleInteger(rs, colIndex);
	            break;
	        case Types.DATE:
	            value = handleDate(rs, colIndex);
	            break;
	        case Types.TIME:
	            value = handleTime(rs.getTime(colIndex));
	            break;
	        case Types.TIMESTAMP:
	            value = handleTimestamp(rs.getTimestamp(colIndex));
	            break;
	        case Types.NVARCHAR:
	        case Types.NCHAR:
	        case Types.LONGNVARCHAR:
	        case Types.LONGVARCHAR:
	        case Types.VARCHAR:
	        case Types.CHAR:
	            value = rs.getString(colIndex);
	            break;
	        default:
	            value = "";
	    }


	    if (value == null){
	        value = "";
	    }

	    return value;

	}//-------------------------------------------------------------------------
	
	private String handleObject(Object obj){
	    return obj == null ? "" : String.valueOf(obj);
	}//-------------------------------------------------------------------------

	private String handleBigDecimal(BigDecimal decimal) {
	    return decimal == null ? "" : decimal.toString();
	}//-------------------------------------------------------------------------

	private String handleLong(ResultSet rs, int columnIndex) throws Exception {
	    long lv = rs.getLong(columnIndex);
	    return rs.wasNull() ? "" : Long.toString(lv);
	}//-------------------------------------------------------------------------

	private String handleInteger(ResultSet rs, int columnIndex) throws Exception {
	    int i = rs.getInt(columnIndex);
	    return rs.wasNull() ? "" : Integer.toString(i);
	}//-------------------------------------------------------------------------

	private String handleDate(ResultSet rs, int columnIndex) throws Exception {
	    try {

	        if(rs.getString(columnIndex) != null){
	            //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	            Date fecha = new Date( formatter.parse(rs.getString(columnIndex)).getTime());
	            return formatter.format(fecha);
	        }
	        else
	            return "";
	    } catch (Exception e) {
	        throw new Exception("Fecha erronea o faltante");
	    }
	}//-------------------------------------------------------------------------

	private String handleTime(Time time) {
	    return time == null ? null : time.toString();
	}//-------------------------------------------------------------------------

	private String handleTimestamp(Timestamp timestamp) {
	    //SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
	    return timestamp == null ? null : timeFormat.format(timestamp);
	}//-------------------------------------------------------------------------

	public static Logger getLog() {
		return log;
	}//-------------------------------------------------------------------------
	
	

}
