package it.webred.ct.diagnostics.service.web.beans.docfa;

import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.diagnostics.service.data.access.DiaDocfaService;
import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaDocfaDTO;
import it.webred.ct.diagnostics.service.data.model.DocfaReport;
import it.webred.ct.diagnostics.service.data.model.zc.ZCErrata;
import it.webred.ct.diagnostics.service.data.util.DiaUtility;
import it.webred.ct.diagnostics.service.web.beans.ParametersBean;
import it.webred.ct.diagnostics.service.web.beans.docfa.exception.DiaDocfaException;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class DocfaResidenzialiBean extends UserBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<SelectItem> lsForniture = new ArrayList<SelectItem>();
	private List<SelectItem> lsCategorie = new ArrayList<SelectItem>();
	
	private String categoria;
	private String dataDa;
	private String dataA;
	private Long idDiaTestata;
	
	private DiaDocfaService diaDocfaService;
	private ParameterService parameterService;
	private DiagnosticheService diaService;
	
	private static final String FD_DOCFA_CODE = "9";
	
	@PostConstruct
	public void doInit(){
		super.getLogger().debug("[DocfaNonResidenzialiBean.doInit] - Start");
		
		if (lsForniture.size() == 0){
			//Carico la lista delle forniture
			DiaDocfaDTO dd = new DiaDocfaDTO(getEnte(),getUsername());
			List<Date> date = diaDocfaService.getFornitureDocfaNonResidenziale(dd);
			for (Date dataFor : date) {						
				lsForniture.add(new SelectItem(getStrDate(dataFor),getStrDate(dataFor)));				
			} 
		}
		super.getLogger().debug("[DocfaNonResidenzialiBean.doInit] - Num Forn.:" + lsForniture.size());
		
		if (lsCategorie.size() == 0){
			lsCategorie.add(new SelectItem("",getMessage("dia.item.tutte")));
			//Carico la lista delle categorie
			DiaDocfaDTO dd = new DiaDocfaDTO(getEnte(),getUsername());
			List<String> lscategorie = diaDocfaService.getCategorieDocfaNonResidenziale(dd);
			for (String categoria : lscategorie) {						
				lsCategorie.add(new SelectItem(categoria,categoria));				
			} 
		}
		
		if (getBeanReference("parametersBean") != null){
			getLogger().debug("[DocfaNonResidenzialiBean.doInit] - ParametersBean recuperato");

			ParametersBean pb = (ParametersBean)getBeanReference("parametersBean");
			idDiaTestata = pb.getIdDiaTestata();
		}
		
		getLogger().debug("[DocfaNonResidenzialiBean.doInit] - Id Dia Testata:"+idDiaTestata);
		super.getLogger().debug("[DocfaNonResidenzialiBean.doInit] - End");
				
	}
	
	public List<SelectItem> getLsForniture() {
		return lsForniture;
	}

	public void setLsForniture(List<SelectItem> lsForniture) {
		this.lsForniture = lsForniture;
	}

	public List<SelectItem> getLsCategorie() {
		return lsCategorie;
	}

	public void setLsCategorie(List<SelectItem> lsCategorie) {
		this.lsCategorie = lsCategorie;
	}

	public void doExportXLS() {
		super.getLogger().info("Esportazione dati residenziali in formato Excel");
		
		try {
			DiaDocfaDTO dd = new DiaDocfaDTO(super.getUser().getCurrentEnte(),super.getUser().getName());
			dd.setCategoria(categoria);
			
			if(dataDa != null) {
				dd.setFornituraDa(DiaUtility.stringMonthToFirstDayDate(dataDa).getTime());
			}
			
			if(dataA != null) {
				dd.setFornituraA(DiaUtility.stringMonthToLastDayDate(dataA).getTime());
			}
			
			dd.setCategoria("");
			if (categoria != null){
				dd.setCategoria(categoria);
			}
			
			super.getLogger().debug("Recupero path template Excel");
			AmKeyValueExt kv = parameterService.getAmKeyValueExtByKeyFonteComune(
								 "dia.report.template", super.getEnte(), FD_DOCFA_CODE);
			String pathTemplate = kv.getValueConf();
			super.getLogger().debug("Path template: " + pathTemplate);
			
			super.getLogger().info("Preparazione export Excel");
			POIFSFileSystem fs  =  new POIFSFileSystem(new FileInputStream(
					pathTemplate+getDiaExportConfig().getProperty("exp.docfa.res.template.name")));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			
			super.getLogger().info("Inizio export Excel residenziali");
			createXLSDocumentSheetOne(wb,dd);
			createXLSDocumentSheetTwo(wb,dd);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			wb.write(baos);
			baos.flush();
			
			getLogger().info("Downloading...");
			downloadXLS(baos, getDiaExportConfig().getProperty("exp.docfa.res.doc.name"));
			getLogger().info("Export done !");
			
			super.getLogger().info("fine export Excel residenziali");
			
			doSaveLog(super.getMessage("dia.export"),idDiaTestata,diaService);
			
		}catch(DiaDocfaException dde) {
			super.getLogger().error("Eccezione: "+dde.getMessage(),dde);
		}catch(Exception e) {
			super.getLogger().error("Eccezione: "+e.getMessage(),e);
		}
	}

	
	
	private void createXLSDocumentSheetOne(HSSFWorkbook wb,DiaDocfaDTO dd) throws Exception {
		
		//get object list
		super.getLogger().debug("Recupero dati Docfa Residenziale");
		List<DocfaReport> ddrr = diaDocfaService.getDocfaReport(dd);
		super.getLogger().debug("DocfaResidenziale trovati: "+ddrr.size());
		
		Long limite = new Long(getDiaExportConfig().getProperty("exp.docfa.xls.limit"));
		if(ddrr.size() > limite.longValue()) {
			super.addErrorMessage("dia.docfa.export",null);
			throw new DiaDocfaException(super.getMessage("dia.docfa.export"));
		}
		
		HSSFCellStyle cellStyleDate = wb.createCellStyle();
		cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		
		HSSFSheet sheet = wb.getSheet("REPORT");
		
		int riga = 0;
		for(DocfaReport dr: ddrr) {
			riga++;
			HSSFRow row = sheet.getRow(riga);
			if (row == null)
				row = sheet.createRow(riga);
			
			int numCell = 0;
			HSSFCell cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			
			cell.setCellValue(new java.util.Date(dr.getForn().getTime()));
			cell.setCellStyle(cellStyleDate);
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(Double.parseDouble(dr.getIdImm()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString(dr.getProt()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			String dtRegform = dr.getDataReg();
			if (dtRegform != null && !dtRegform.equals("")){
				dtRegform = dtRegform.substring(6, 8)+"/"+dtRegform.substring(4, 6)+"/"+dtRegform.substring(0, 4);
			}
			cell.setCellValue(new HSSFRichTextString(dtRegform ));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getFoglio()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getPart()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getSub()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getZcDocfa()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getZcCat()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			//cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString( dr.getOldMz()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getNewMz()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getZcCat()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getCat()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getClasse()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getConsistenza()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getSuperficie()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getRenditaDocfa()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getToponimo()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getIndirizzo()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getCivico1()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getDicCognome()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getDicNome()));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getTecCognome()));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getTecNome()));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if(dr.getNc() != null)
				cell.setCellValue(new HSSFRichTextString( dr.getNc().toString()));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getGraffati()));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getTipoOperazione()));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getRenditaDocfax100() != null)
				cell.setCellValue(dr.getRenditaDocfax100().doubleValue());

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getRenditaDocfaAgg()!= null)
				cell.setCellValue(dr.getRenditaDocfaAgg().doubleValue());
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getValCommMq() != null)
			cell.setCellValue(dr.getValCommMq().doubleValue());

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getValComm() != null)
			cell.setCellValue(dr.getValComm().doubleValue());

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getRapp1() != null)
			cell.setCellValue(dr.getRapp1().doubleValue());

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getRapp2() != null)
			cell.setCellValue(dr.getRapp2().doubleValue());
		}
	}
	
	private void createXLSDocumentSheetTwo(HSSFWorkbook wb,DiaDocfaDTO dd) throws Exception {
		//get object list
		super.getLogger().debug("Recupero dati ZC Errate");
		List<ZCErrata> zzcc = diaDocfaService.getZCErrate(dd);
		super.getLogger().debug("ZC Errate trovati: "+zzcc.size());
		
		Long limite = new Long(getDiaExportConfig().getProperty("exp.docfa.xls.limit"));
		if(zzcc.size() > limite.longValue()) {
			super.addErrorMessage("dia.docfa.export",null);
			throw new DiaDocfaException(super.getMessage("dia.docfa.export"));
		}
		
		HSSFSheet sheet = wb.getSheet("ZC_ERRATE");
		
		int riga = 0;
		for(ZCErrata zc: zzcc) {
			riga++;
			int numCell = 0;
			
			HSSFRow row = sheet.getRow(riga);
			if (row == null)
				row = sheet.createRow(riga);
			
			HSSFCell cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString(zc.getZonadic().toString()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString( zc.getZona().toString()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString( zc.getFoglio().toString()));
				
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString( zc.getOldmz()));
				
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString( zc.getNewmz()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString( zc.getOccorrenze().toString()));
		}
	}
	
	
	private void downloadXLS(ByteArrayOutputStream baos, String nomeFile) throws IOException {
		
		FacesContext faces = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
		
		getLogger().debug("Getting response");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setContentType("application/xls");
		response.addHeader("Content-disposition", "attachment;filename=\""+ nomeFile + ".xls");
		
		getLogger().debug("Setting output stream");
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		out.close();
		
		getLogger().debug("Completing response...");
		response.setContentLength(baos.size());
		faces.responseComplete();
		getLogger().debug("Response complete");
	}
	
	
	private Properties getDiaExportConfig() throws Exception {
		Properties p = new Properties();
		p.load(this.getClass().getResourceAsStream("/diaexport.properties"));		
		return p;
	}
	
	
	public String getCategoria() {
		return categoria;
	}



	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}



	public String getDataDa() {
		return dataDa;
	}



	public void setDataDa(String dataDa) {
		this.dataDa = dataDa;
	}



	public String getDataA() {
		return dataA;
	}



	public void setDataA(String dataA) {
		this.dataA = dataA;
	}



	public DiaDocfaService getDiaDocfaService() {
		return diaDocfaService;
	}



	public void setDiaDocfaService(DiaDocfaService diaDocfaService) {
		this.diaDocfaService = diaDocfaService;
	}



	public ParameterService getParameterService() {
		return parameterService;
	}



	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}



	public Long getIdDiaTestata() {
		return idDiaTestata;
	}



	public void setIdDiaTestata(Long idDiaTestata) {
		this.idDiaTestata = idDiaTestata;
	}



	public DiagnosticheService getDiaService() {
		return diaService;
	}



	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}
}
