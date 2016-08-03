package it.webred.ct.diagnostics.service.web.beans.docfa;

import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.diagnostics.service.data.access.DiaDocfaService;
import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaDocfaDTO;
import it.webred.ct.diagnostics.service.data.model.DocfaReport;
import it.webred.ct.diagnostics.service.data.model.DocfaReportNoRes;
import it.webred.ct.diagnostics.service.data.model.zc.ZCErrata;
import it.webred.ct.diagnostics.service.data.util.DiaUtility;
import it.webred.ct.diagnostics.service.web.beans.ParametersBean;
import it.webred.ct.diagnostics.service.web.beans.docfa.exception.DiaDocfaException;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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

public class DocfaNonResidenzialiBean extends UserBean implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private String daFornitura;
	private String aFornitura;
	private List<SelectItem> lsForniture = new ArrayList<SelectItem>();
	private List<SelectItem> lsCategorie = new ArrayList<SelectItem>();
	private Long idTestata;
	private String categoria;
	
	private DiaDocfaService diaDocfaService;
	private ParameterService parameterService;
	private DiagnosticheService diaService;
	
	private static final String FD_DOCFA_CODE = "9";	
	
	private Properties getDiaExportConfig() throws Exception {
		Properties p = new Properties();
		p.load(this.getClass().getResourceAsStream("/diaexport.properties"));		
		return p;
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

	public Long getIdTestata() {
		return idTestata;
	}


	public void setIdTestata(Long idTestata) {
		this.idTestata = idTestata;
	}

	
	@PostConstruct
	public void doInit(){
		getLogger().debug("[DocfaNonResidenzialiBean.doInit] - Start");
		
		if (lsForniture.size() == 0){
			//Carico la lista delle forniture
			DiaDocfaDTO dd = new DiaDocfaDTO(getEnte(),getUsername());
			List<Date> date = diaDocfaService.getFornitureDocfaNonResidenziale(dd);
			for (Date dataFor : date) {						
				lsForniture.add(new SelectItem(getStrDate(dataFor),getStrDate(dataFor)));				
			} 
		}		
		
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
			idTestata = pb.getIdDiaTestata();
		}
		
		getLogger().debug("[DocfaNonResidenzialiBean.doInit] - Id Dia Testata:"+idTestata);
		getLogger().debug("[DocfaNonResidenzialiBean.doInit] - End");
				
	}

	
	
	public void doExportXLS(){
		super.getLogger().info("Esportazione dati non residenziali in formato Excel");
		
		try {
			DiaDocfaDTO dd = new DiaDocfaDTO(super.getUser().getCurrentEnte(),super.getUser().getName());
			dd.setCategoria(categoria);
			
			if(daFornitura != null) {
				dd.setFornituraDa(DiaUtility.stringMonthToFirstDayDate(daFornitura).getTime());
			}
			
			if(aFornitura != null) {
				dd.setFornituraA(DiaUtility.stringMonthToLastDayDate(aFornitura).getTime());
			}
			
			dd.setCategoria("");
			if(categoria != null) {
				dd.setCategoria(categoria);
			}
			
			super.getLogger().debug("Categoria sel:"+ categoria);
			
			super.getLogger().debug("Recupero path template Excel");
			AmKeyValueExt kv = parameterService.getAmKeyValueExtByKeyFonteComune(
								 "dia.report.template", super.getUser().getCurrentEnte(), FD_DOCFA_CODE);
			String pathTemplate = kv.getValueConf();
			super.getLogger().debug("Path template: " + pathTemplate);
			
			super.getLogger().info("Preparazione export Excel");
			POIFSFileSystem fs  =  new POIFSFileSystem(new FileInputStream(
					pathTemplate+getDiaExportConfig().getProperty("exp.docfa.nores.template.name")));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			
			super.getLogger().info("Inizio export Excel non residenziali");
			createXLSDocumentSheetOne(wb,dd);
			createXLSDocumentSheetTwo(wb,dd);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			wb.write(baos);
			baos.flush();
			
			getLogger().info("Downloading...");
			downloadXLS(baos, getDiaExportConfig().getProperty("exp.docfa.nores.doc.name"));
			getLogger().info("Export done !");
			
			super.getLogger().info("fine export Excel non residenziali");
			
			doSaveLog(super.getMessage("dia.export"),idTestata,diaService);
			
		}catch(DiaDocfaException dde) {
			super.getLogger().error("Eccezione: "+dde.getMessage(),dde);
		}catch(Exception e) {
			super.getLogger().error("Eccezione: "+e.getMessage(),e);
		}
	}
	
	public void doReset(){
		daFornitura = "";
		aFornitura = "";
	}

	
	private void createXLSDocumentSheetOne(HSSFWorkbook wb,DiaDocfaDTO dd) throws Exception {
		//get object list
		super.getLogger().debug("Recupero dati Docfa Non Residenziale");
		List<DocfaReportNoRes> ddrr = diaDocfaService.getDocfaReportNoRes(dd);
		super.getLogger().debug("DocfaNoResidenziale trovati: "+ddrr.size());
		
		Long limite = new Long(getDiaExportConfig().getProperty("exp.docfa.xls.limit"));
		if(ddrr.size() > limite.longValue()) {
			super.addErrorMessage("dia.docfa.export",null);
			throw new DiaDocfaException(super.getMessage("dia.docfa.export"));
		}
		
		HSSFCellStyle cellStyleDate = wb.createCellStyle();
		cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		
		HSSFSheet sheet = wb.getSheet("REPORT");
		
		int riga = 0;
		for(DocfaReportNoRes dr: ddrr) {
			riga++;
			int numCell = 0;

			HSSFRow row = sheet.getRow(riga);
			if (row == null)
				row = sheet.createRow(riga);
			
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
			cell.setCellValue(new HSSFRichTextString( dr.getProt()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			String dtRegform = dr.getDataReg();
			cell.setCellValue(new HSSFRichTextString(dtRegform ));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getFoglio() != null)
				cell.setCellValue(Double.parseDouble( dr.getFoglio().toString()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getPart()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getSub() != null)
				cell.setCellValue(dr.getSub().doubleValue());
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getZcDocfa() != null)
				cell.setCellValue(dr.getZcDocfa().doubleValue());
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getZcCat() != null)
				cell.setCellValue(dr.getZcCat().doubleValue());
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getZcDiff() != null)
				cell.setCellValue(dr.getZcDiff().doubleValue());
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
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
			cell.setCellValue(new HSSFRichTextString( dr.getClasseMin()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getClasseMinC06()));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getDiff() != null)
				cell.setCellValue(dr.getDiff().doubleValue());
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			cell.setCellValue(new HSSFRichTextString( dr.getDestUso()));
			  
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getConsistenza() != null)
				cell.setCellValue(dr.getConsistenza().doubleValue());
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getSuperficie() != null)
				cell.setCellValue(dr.getSuperficie().doubleValue());
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (dr.getRenditaDocfa() != null)
				cell.setCellValue(dr.getRenditaDocfa().doubleValue());
			
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
			if (dr.getNc() != null)
				cell.setCellValue(dr.getNc().doubleValue());

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
		}
	}
	
	
	private void createXLSDocumentSheetTwo(HSSFWorkbook wb,DiaDocfaDTO dd) throws Exception {
		//get object list
		super.getLogger().debug("Recupero dati ZC Errate No Res.");
		List<ZCErrata> zzcc = diaDocfaService.getZCErrateNoRes(dd);
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
			if (zc.getZonadic()!= null)
				cell.setCellValue(zc.getZonadic().doubleValue());
			
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (zc.getZona()!= null)
				cell.setCellValue(zc.getZona().doubleValue());
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell(numCell);
			if (zc.getFoglio()!= null)
				cell.setCellValue(zc.getFoglio().doubleValue());
				
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
			if (zc.getOccorrenze()!= null)
				cell.setCellValue(zc.getOccorrenze().doubleValue());
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

	
	
	public String getDaFornitura() {
		if (daFornitura == null) return "";
		return daFornitura;
	}


	public void setDaFornitura(String daFornitura) {
		this.daFornitura = daFornitura;
	}


	public String getaFornitura() {
		if (aFornitura == null) return "";
		return aFornitura;
	}


	public void setaFornitura(String aFornitura) {
		this.aFornitura = aFornitura;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
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



	public DiagnosticheService getDiaService() {
		return diaService;
	}



	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}
}
