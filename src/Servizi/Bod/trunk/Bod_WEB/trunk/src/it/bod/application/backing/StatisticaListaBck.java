package it.bod.application.backing;

import it.bod.application.common.MasterClass;
import it.bod.business.service.BodLogicService;
import it.webred.rich.common.ComboBoxRch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class StatisticaListaBck extends MasterClass{

	private static final long serialVersionUID = -1587600876291842208L;
	private static Logger logger = Logger.getLogger("it.bod.application.backing.StatisticaListaBck");
	
	private BodLogicService bodLogicService = null;
	private List<Object> lstDiagnostiche = null;
	private List<Object> lstCategorie = null;
	private ComboBoxRch cbxCategorie = null; 
	private String xlsName = "";
	private String xlsPath = "";
	public String getXlsPath() {
		return xlsPath;
	}

	public void setXlsPath(String xlsPath) {
		this.xlsPath = xlsPath;
	}

	private Boolean xlsLinkato = false;
	
	private String fornituraDal = null;
	private String fornituraAl = null;
	private String currentDiagno = null;
	
	private String codiceEnte;
	
	public StatisticaListaBck(){
		super.initializer();
		
		cbxCategorie = new ComboBoxRch();
		cbxCategorie.setState("Tutte");
		cbxCategorie.setDefaultMessage("Tutte");
	}//-------------------------------------------------------------------------
	
	protected String getRootPathEnte(){
		
		String pathDatiDiogeneEnte = super.getPathDatiDiogene()+ this.getEnteCorrente().toUpperCase()+ "/";
		logger.info("PATH DATI DIOGENE ENTE: " + pathDatiDiogeneEnte);
		return pathDatiDiogeneEnte;
	}
	
	private String getEnteCorrente(){
	
		codiceEnte = this.getEnte().getBelfiore();
		
		return codiceEnte;
		
	}
	
	public String getPathTemplateBod() {
		
		if (pathTemplateBod==null)
			pathTemplateBod = this.getRootPathEnte() + super.dirTemplateBod;
		
		logger.info("PATH TEMPLATE BOD: " + pathTemplateBod);
		return pathTemplateBod;
	}
	
	
//	public void init(){
//		cbxCategorie = new ComboBoxRch();
//		cbxCategorie.setState("Tutte");
//		cbxCategorie.setDefaultMessage("Tutte");
//	}//-------------------------------------------------------------------------
	
	/*public void showXls() throws Exception {
		logger.info("XLS PATH: " + this.xlsPath);
		super.showXls(this.xlsPath);
	}*/

	
	public void esporta() throws Exception{
		logger.info("Linkato PRIMA: " + this.xlsLinkato);
		String pathXlsTemplate = this.getPathTemplateBod() + "template_xlsDocfa";
		String pathXlsTmp = this.getRootPathEnte() + this.TEMPORARY_FILES;
		String esito = esportaDati(pathXlsTemplate, pathXlsTmp, fornituraDal, fornituraAl, cbxCategorie.getState());
		if (esito != null && esito.trim().equalsIgnoreCase("true"))
			this.xlsLinkato = true;
		else
			this.xlsLinkato = false;
		logger.info("Linkato DOPO: " + this.xlsLinkato);
	}//-------------------------------------------------------------------------
	
	public String esportaDati(String pathModelloXls, String pathOutXls, String daDataFornitura, String aDataFornitura, String categoria) throws Exception	{
		Hashtable htQry = new Hashtable();
		String msg = "true";
		try {
			String tabella = "DOCFA_REPORT_NORES";
			xlsName = sessionId + "_docfa_acc_cat_no_res.xls";
			String modelloXls = pathModelloXls + "/modello_acc_cat_no_res.xls";
			if (currentDiagno != null && currentDiagno.trim().equalsIgnoreCase("Residenziali")){
				tabella = "DOCFA_REPORT";
				xlsName = sessionId + "_docfa_acc_cat_res.xls";
				modelloXls = pathModelloXls + "/modello_acc_cat_res.xls";
			}
			//controllo che i dati entrino nel foglio (<=65.000)
			String sqlTest = "SELECT COUNT(*) AS CONTA FROM " + tabella + " R WHERE 1=1";
			String order = " ORDER BY FORN,PROT";
			if (daDataFornitura!= null && !daDataFornitura.equals(""))
				sqlTest = sqlTest + " AND FORN >= TO_DATE('"+daDataFornitura+"','DD/MM/YYYY')";
			if (aDataFornitura!= null && !aDataFornitura.equals(""))
				sqlTest = sqlTest + " AND FORN <= TO_DATE('"+aDataFornitura+"','DD/MM/YYYY')";
			if (categoria != null && !categoria.equals("") && !categoria.equals("Tutte"))
				sqlTest = sqlTest + " AND CAT = '"+categoria+"'";
			sqlTest = sqlTest + order;
			htQry.put("queryString", sqlTest);
			List<Object> lstConta = bodLogicService.getList(htQry);
			Integer controllo = 0;
			Iterator<Object> itConta = lstConta.iterator();
			
			if (itConta.hasNext()){
				controllo = ( (BigDecimal)itConta.next() ).intValue();
			}
			
			if (controllo > 65500){
				msg = "ATTENZIONE - La selezione ha prodotto un risultato troppo grande per la rappresentazione su file Excel!";
			}
			else{
				//preparo il modello xls sul foglio 0 (report)
				POIFSFileSystem fs  =  new POIFSFileSystem(new FileInputStream( modelloXls ));
				HSSFWorkbook wb = new HSSFWorkbook(fs);

				if (currentDiagno != null && currentDiagno.trim().equalsIgnoreCase("Residenziali")){
					wb = scriviReportSheetRes(wb, daDataFornitura, aDataFornitura, categoria);					
				}else{
					wb = scriviReportSheetNoRes(wb, daDataFornitura, aDataFornitura, categoria);					
				}
				
				wb = scriviZcSheet(wb, daDataFornitura, aDataFornitura, categoria); 
				
				// Write the output to a file
				
				File outDir = new File(pathOutXls);
				if(!outDir.exists())
					outDir.mkdirs();
				
				xlsPath = pathOutXls + "/" + xlsName;
				logger.info("PATH STATISTICA: " + xlsPath);
				
				FileOutputStream fileOut = new FileOutputStream(xlsPath);
				wb.write(fileOut);
				fileOut.close();
				
			}
				
		}
		catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return msg;
	}//-------------------------------------------------------------------------
	
	/*public void xlsDownload() {
		try {
			FacesContext fctx = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse)fctx.getExternalContext().getResponse();
			response.setContentType("application/download");
			String pathFile = pathPlanimetrie + "temporaryFiles" + "/" + xlsName;
			String fileNameToDownload = xlsName.substring(xlsName.indexOf("_") + 1);
			response.setHeader("Content-Disposition", "attachment; filename= \"" + fileNameToDownload + "\"");				
			java.io.InputStream in = new FileInputStream(new File(pathFile));
			int size = in.available();
			response.setContentLength(size);
			byte[] ab = new byte[size];
			OutputStream os = response.getOutputStream();
			int bytesread = 0;
			while (bytesread > -1) {
				bytesread = in.read(ab, 0, size);
				if (bytesread > -1)
					os.write(ab, 0, bytesread);
			}				
			in.close();
			os.flush();
			os.close();					    
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}*/
	
	private HSSFWorkbook scriviReportSheetRes(HSSFWorkbook wb, String daDataFornitura, String aDataFornitura, String categoria) throws Exception{
		HSSFCellStyle cellStyleDate = wb.createCellStyle();
		cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		Hashtable htQry = new Hashtable();
		String sql = "SELECT * FROM DOCFA_REPORT R WHERE 1=1";
		String order = " ORDER BY FORN, PROT";
		if (daDataFornitura!= null && !daDataFornitura.equals(""))
			sql = sql + " AND FORN >= TO_DATE('"+daDataFornitura+"','DD/MM/YYYY')";
		if (aDataFornitura!= null && !aDataFornitura.equals(""))
			sql = sql + " AND FORN <= TO_DATE('"+aDataFornitura+"','DD/MM/YYYY')";
		if (categoria != null && !categoria.equals("") && !categoria.equals("Tutte"))
			sql = sql + " AND CAT = '"+categoria+"'";
		sql = sql + order;
		
		HSSFSheet sheet = wb.getSheet("REPORT");
		
		//recupero dati da inserire nel foglio
		htQry.put("queryString", sql);
		List<Object> lstObjs = bodLogicService.getList(htQry);
		int riga = 0;
		Iterator<Object> itObjs = lstObjs.iterator();
		while (itObjs.hasNext()){
			Object[] objs = (Object[])itObjs.next();
			riga++;
			int numCell = 0;
			// esportazione dati in formato CSV
			HSSFRow row = sheet.getRow(riga);
			if (row == null)
				row = sheet.createRow(riga);
			
			
			HSSFCell cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue( (Date)objs[0] );	//FORN
			cell.setCellStyle(cellStyleDate);
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue( (String)objs[1] );	//ID_IMM
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[2] ));	//PROT
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			//data registrazione da formattare
			String dtRegform = (String)objs[3];	//DATA_REG
			if (dtRegform != null && !dtRegform.equals("")){
				dtRegform = dtRegform.substring(6, 8)+"/"+dtRegform.substring(4, 6)+"/"+dtRegform.substring(0, 4);
			}
			cell.setCellValue(new HSSFRichTextString(dtRegform ));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[10] ));	//FOGLIO
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[11] ));	//PART
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[12] ));	//SUB
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[4] ));	//ZC_DOCFA
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[23] ));	//ZC_CAT
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			//cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString( (String)objs[24] ));	//OLD_MZ
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[25] ));	//NEW_MZ
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[23] ));	//ZC_CAT è già stato incluso sopra
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[5] ));	//CAT
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[6] ));	//CLASSE
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[7] ));	//CONSISTENZA
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[8] ));	//SUPERFICIE
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[9] ));	//RENDITA_DOCFA
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[14] ));	//TOPONIMO
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[15] ));	//INDIRIZZO
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[16] ));	//CIVICO_1
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[19] ));	//DIC_COGNOME
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[20] ));	//DIC_NOME

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[21] ));	//TEC_COGNOME

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[22] ));	//TEC_NOME

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue( ( (BigDecimal)objs[18] ).doubleValue() );	//NC

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[13] ));	//GRAFFATI

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[17] ));	//TIPO_OPERAZIONE

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[27] != null)								//RENDITA_DOCFAX100
				cell.setCellValue( ( (BigDecimal)objs[27] ).doubleValue() );

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[28] != null )								//RENDITA_DOCFA_AGG
				cell.setCellValue( ( (BigDecimal)objs[28] ).doubleValue() );
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[26] != null)									//VAL_COMM_MQ
				cell.setCellValue( ( (BigDecimal)objs[26] ).doubleValue() );

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[29]  != null)										//VAL_COMM
			cell.setCellValue( ( (BigDecimal)objs[29] ).doubleValue() );

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[30] != null)										//RAPP_1
			cell.setCellValue( ((BigDecimal)objs[30] ).doubleValue() );

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[31] != null )										//RAPP_2
			cell.setCellValue( ( (BigDecimal)objs[31] ).doubleValue() );
		
		}
		
		return wb;
	}//-------------------------------------------------------------------------
	
	private HSSFWorkbook scriviReportSheetNoRes(HSSFWorkbook wb, String daDataFornitura, String aDataFornitura, String categoria) throws Exception{
		Hashtable htQry = new Hashtable();
		HSSFCellStyle cellStyleDate = wb.createCellStyle();
		cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		
		String sql = "SELECT * FROM DOCFA_REPORT_NORES R WHERE 1=1";
		String order = " ORDER BY FORN,PROT";
		if (daDataFornitura!= null && !daDataFornitura.equals(""))
			sql = sql + " AND FORN >= TO_DATE('"+daDataFornitura+"','DD/MM/YYYY')";
		if (aDataFornitura!= null && !aDataFornitura.equals(""))
			sql = sql + " AND FORN <= TO_DATE('"+aDataFornitura+"','DD/MM/YYYY')";
		if (categoria != null && !categoria.equals("") && !categoria.equals("Tutte"))
			sql = sql + " AND CAT = '"+categoria+"'";
		sql = sql + order;
		
		HSSFSheet sheet = wb.getSheet("REPORT");
		
		//recupero dati da inserire nel foglio
		htQry.put("queryString", sql);
		List<Object> lstObjs = bodLogicService.getList(htQry);
		int riga = 0;
		Iterator<Object> itObjs = lstObjs.iterator();
		while (itObjs.hasNext()){
			Object[] objs = (Object[])itObjs.next();
			riga++;
			int numCell = 0;
			// esportazione dati in formato CSV
			HSSFRow row = sheet.getRow(riga);
			if (row == null)
				row = sheet.createRow(riga);
			
			
			HSSFCell cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue( (Date)objs[0] ); //FORN
			cell.setCellStyle(cellStyleDate);

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(Double.parseDouble( (String)objs[1] )); //ID_IMM
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[2] )); //PROT
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			String dtRegform = (String)objs[3]; //DATA_REG
			cell.setCellValue(new HSSFRichTextString(dtRegform ));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[4] != null)
				//cell.setCellValue( ( (BigDecimal)objs[4] ).doubleValue() );	//FOLGIO
				cell.setCellValue(new HSSFRichTextString( (String)objs[4] ));	//FOGLIO
			
			/*
			 numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[10] ));	//FOGLIO
			 */
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[5] ));	//PART
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[6] != null)	//SUB
				cell.setCellValue( ( (BigDecimal)objs[6] ).doubleValue() );	//SUB
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[7] != null)	//ZC_DOCFA
				cell.setCellValue( ( (BigDecimal)objs[7] ).doubleValue() );	//ZC_DOCFA
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[8] != null)	//ZC_CAT
				cell.setCellValue( ( (BigDecimal)objs[8] ).doubleValue() );	//ZC_CAT
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[9] != null)	//ZC_DIFF
				cell.setCellValue( ( (BigDecimal)objs[9] ).doubleValue() );	//ZC_DIFF
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[10] ));	//OLD_MZ
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[11] ));	//NEW_MZ
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[12] ));	//CAT
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[13] ));	//CLASSE
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[14] ));	//CLASSE_MIN
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[15] ));	//CLASSE_MIN_C06
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[16] != null)	//DIFF
				cell.setCellValue( ( (BigDecimal)objs[16] ).doubleValue() );	//DIFF
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[17] ));	//DEST_USO
			  
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[18] != null)	//CONSISTENZA
				cell.setCellValue( ( (BigDecimal)objs[18] ).doubleValue() );	//CONSISTENZA
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[19] != null)	//SUPERFICIE
				cell.setCellValue( ( (BigDecimal)objs[19] ).doubleValue() );	//SUPERFICIE
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[20] != null)	//RENDITA_DOCFA
				cell.setCellValue( ( (BigDecimal)objs[20] ).doubleValue() );	//RENDITA_DOCFA
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[21] ));	//TOPONIMO
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[22] ));	//INDIRIZZO
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[23] ));	//CIVICO_1
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[24] ));	//DIC_COGNOME
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[25] ));	//DIC_NOME

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[26] ));	//TEC_COGNOME

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[27] ));	//TEC_NOME

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[28] != null)
				cell.setCellValue( ( (BigDecimal)objs[28] ).doubleValue() );	//NC

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[29] ));	//GRAFFATI

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( (String)objs[30] ));	//TIPO_OPERAZIONE
			
		}
		
		return wb;
	}//-------------------------------------------------------------------------
	
	private HSSFWorkbook scriviZcSheet(HSSFWorkbook wb,String daDataFornitura,String aDataFornitura,String categoria) throws Exception{
		Hashtable htQry = new Hashtable();
		String tabella = "DOCFA_REPORT_NORES";
		if (currentDiagno != null && currentDiagno.trim().equalsIgnoreCase("Residenziali")){
			tabella = "DOCFA_REPORT";
		}
		String sqlZC = "SELECT DISTINCT TO_NUMBER(ZC_DOCFA) AS ZONA_DIC, TO_NUMBER(ZC_CAT) AS ZONA, TO_NUMBER(FOGLIO) AS FOGLIO," +
							"OLD_MZ, NEW_MZ, count(*) AS OCCORRENZE " +
							"FROM " + tabella + " C2 " +
							"WHERE TO_NUMBER(C2.ZC_DOCFA) <> C2.ZC_CAT ";
		String groupOrder = " GROUP BY (TO_NUMBER(ZC_DOCFA),TO_NUMBER(ZC_CAT),TO_NUMBER(FOGLIO),OLD_MZ, NEW_MZ) " +
							"ORDER BY TO_NUMBER(FOGLIO)";
		
		/*
		 * IL FORMATO DELLA DATA OSPITATA NELLA COLONNA DATA_REG E' DIVERSO:
		 * NELLA TABELLA DOCFA_REPORT SEGUE QUESTO PATTERN: YYYYMMDD
		 * NELLA TABELLA DOCFA_REPORT_NORES INVECE: DDMMYYYY
		 */
		if (daDataFornitura!= null && !daDataFornitura.equals("")){
			if (currentDiagno != null && currentDiagno.trim().equalsIgnoreCase("Residenziali"))
				sqlZC = sqlZC + " and to_date(data_reg,'yyyymmdd') >= to_date('"+daDataFornitura+"','dd/mm/yyyy') ";
			else
				sqlZC = sqlZC + " and to_date(data_reg,'dd/mm/yyyy') >= to_date('"+daDataFornitura+"','dd/mm/yyyy') ";
			
		}

		if (aDataFornitura!= null && !aDataFornitura.equals("")){
			if (currentDiagno != null && currentDiagno.trim().equalsIgnoreCase("Residenziali"))
				sqlZC = sqlZC + " and to_date(data_reg,'yyyymmdd') <= to_date('"+aDataFornitura+"','dd/mm/yyyy') ";
			else
				sqlZC = sqlZC + " and to_date(data_reg,'dd/mm/yyyy') <= to_date('"+aDataFornitura+"','dd/mm/yyyy') ";
		}
		
		if (categoria != null && !categoria.equals("") && !categoria.equals("Tutte"))
			sqlZC = sqlZC + " AND CAT = '"+categoria+"'";

		sqlZC = sqlZC + groupOrder;

		HSSFSheet sheet = wb.getSheet("ZC_ERRATE");

		//recupero dati da inserire nel foglio
		htQry.put("queryString", sqlZC);
		List<Object> lstObjs = bodLogicService.getList(htQry);
		int riga = 0;
		Iterator<Object>  itObjs = lstObjs.iterator();
		while (itObjs.hasNext()){
			Object[] objs = (Object[])itObjs.next();
			riga++;
			int numCell = 0;
			
			HSSFRow row = sheet.getRow(riga);
			if (row == null)
				row = sheet.createRow(riga);
				
			HSSFCell cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[0] != null)	//ZONA_DIC
				cell.setCellValue( ( (BigDecimal)objs[0] ).doubleValue() );	//ZONA_DIC
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[1] != null)	//ZONA
				cell.setCellValue( ( (BigDecimal)objs[1] ).doubleValue() );	//ZONA
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[2] != null)
				cell.setCellValue( ( (BigDecimal)objs[2] ).doubleValue() );	//FOGLIO
				
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString( (String)objs[3] ));	//OLD_MZ
				
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString( (String)objs[4] ));	//NEW_MZ
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if ( objs[5] != null)	//OCCORRENZE
				cell.setCellValue( ( (BigDecimal)objs[5] ).doubleValue() );	//OCCORRENZE
		}
			
		return wb;
	}//-------------------------------------------------------------------------
	
	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}
	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}
	public List<Object> getLstDiagnostiche() {
		return lstDiagnostiche;
	}
	public void setLstDiagnostiche(List<Object> lstDiagnostiche) {
		this.lstDiagnostiche = lstDiagnostiche;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public ComboBoxRch getCbxCategorie() {
		return cbxCategorie;
	}

	public void setCbxCategorie(ComboBoxRch cbxCategorie) {
		this.cbxCategorie = cbxCategorie;
	}

	public List<Object> getLstCategorie() {
		return lstCategorie;
	}

	public void setLstCategorie(List<Object> lstCategorie) {
		this.lstCategorie = lstCategorie;
	}

	public String getXlsName() {
		return xlsName;
	}

	public void setXlsName(String xlsName) {
		this.xlsName = xlsName;
	}

	public Boolean getXlsLinkato() {
		return xlsLinkato;
	}

	public void setXlsLinkato(Boolean xlsLinkato) {
		this.xlsLinkato = xlsLinkato;
	}

	public String getFornituraDal() {
		return fornituraDal;
	}

	public void setFornituraDal(String fornituraDal) {
		this.fornituraDal = fornituraDal;
	}

	public String getFornituraAl() {
		return fornituraAl;
	}

	public void setFornituraAl(String fornituraAl) {
		this.fornituraAl = fornituraAl;
	}

	public String getCurrentDiagno() {
		return currentDiagno;
	}

	public void setCurrentDiagno(String rdoDiagnostica) {
		this.currentDiagno = rdoDiagnostica;
	}

	
	
	
}
