package it.webred.ct.diagnostics.service.web.beans;

import it.webred.ct.diagnostics.service.data.SuperDia;
import it.webred.ct.diagnostics.service.data.access.DiaDettaglioService;
import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaDettaglioDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaLogAccessoDTO;
import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.data.util.DiaUtility;
import it.webred.ct.diagnostics.service.web.beans.pagination.DataProviderImpl;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DiaDettaglioBean extends UserBean implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DiaDettaglioService diaDettaglioService;
	private DiagnosticheService diaService;
	private Long idDiaTestata;
	private String modelClassname;
	private String diaDescr;
	private String codCommand;
	private String numTipoGest;
	private String numTipoGestValue;
	private Long idCatalogoDia;
	
	
	private boolean exportXLS = false;
	private boolean exportCSV = false;
	
	private String backToStoricoEsecuzioni = "N";
	
	public Long getIdCatalogoDia() {
		return idCatalogoDia;
	}

	public void setIdCatalogoDia(Long idCatalogoDia) {
		this.idCatalogoDia = idCatalogoDia;
	}
	public String getBackToStoricoEsecuzioni() {
		return backToStoricoEsecuzioni;
	}

	public void setBackToStoricoEsecuzioni(String backToStoricoEsecuzioni) {
		this.backToStoricoEsecuzioni = backToStoricoEsecuzioni;
	}

	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}
	
	public String goDettaglio() {
		getLogger().debug("Recupero dettaglio diagnostica");
		
		doSaveLog(super.getMessage("dia.view.standard"),idDiaTestata,diaService);
		
		try {
			//passaggio parametri ad dataProviderImpl
			DataProviderImpl dp = (DataProviderImpl)super.getBeanReference("dataProviderImpl");
			dp.setModelClassname(modelClassname);
			dp.setIdDiaTestata(idDiaTestata);
			
			//impostazione tipologia export
			Long limit = new Long(getDiaExportConfig().getProperty("exp.xls.records.limit"));
			getLogger().debug("Limite record per export excel: "+limit);
			if(dp.getDettaglioCount() <= limit.longValue())
				exportXLS = true;
			else
				exportCSV = true;
			
			getLogger().debug("Export excel ? "+ new Boolean(exportXLS).toString());
			
		}catch(Exception e) {
			super.getLogger().error("Eccezione: "+e.getMessage(),e);
		}		
		
		return "diagnostica.dettaglio";
	}
	
	public String doNavigationCase(){
		
		getLogger().debug("doNavigationCase:"+numTipoGestValue);
		
		doSaveLog(super.getMessage("dia.view.custom"),idDiaTestata,diaService);
		
		if (getRequest().getAttribute("parametersBean") != null){
			ParametersBean params = (ParametersBean)getRequest().getAttribute("parametersBean");
			
			getLogger().debug("parametersBean idDiaTestata:"+params.getIdDiaTestata());
			getLogger().debug("parametersBean codCommand:"+params.getCodCommand());
		}
			
		return numTipoGestValue;
	}
	
	public void doOtherPath(){
		getLogger().debug("doOtherPathType:"+numTipoGest);
		getLogger().debug("doOtherPathValue:"+numTipoGestValue);
		
		doSaveLog(super.getMessage("dia.view.otherSite"),idDiaTestata,diaService);
		
		try{
			
			StringBuilder  pathController = new StringBuilder(numTipoGestValue);
											
			pathController.append("?codiceComando=");
			pathController.append(this.codCommand);
			pathController.append("&idTestata=");
			pathController.append(this.idDiaTestata);
			pathController.append("&belfiore=");
			pathController.append(getUser().getCurrentEnte());
			pathController.append("&pathApp=");
			pathController.append(getRequest().getRequestURL().toString());
			
			super.getLogger().debug("URL: "+pathController.toString());
			
			getResponse().sendRedirect(pathController.toString());
			
		}catch(Exception e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}
	}
	
	
	
	public void doExportXLS() {
		
		getLogger().info("Export dettaglio diagnostica in formato Excel");
		
		try {			
			DiaDettaglioDTO dd = new DiaDettaglioDTO(super.getEnte(),super.getUser().getName());
			dd.setIdDiaTestata(idDiaTestata);
			dd.setModelClassname(modelClassname);
			
			getLogger().debug("Recupero dati");
			List<? extends SuperDia> dettaglio = diaDettaglioService.getDettaglioDiagnosticaCompleto(dd);
			getLogger().debug("Item list: "+ dettaglio.size());
			
			getLogger().debug("Recupero configurazione export excel");
			Properties cfg = getDiaExportConfig();
			
			getLogger().debug("Creazione excel");
			int nofsheet = 1;
			//dalla lista creo un documento excel
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet(cfg.getProperty("exp.sheet.name"));
			HSSFRow row = sheet.createRow(0);
			
			//recupero colonne
			DataProviderImpl dp = (DataProviderImpl)super.getBeanReference("dataProviderImpl");
			dp.setModelClassname(modelClassname);
			dp.setIdDiaTestata(idDiaTestata);
			
			getLogger().debug("Intestazione excel");
			List<String> intestazione = dp.getColumns();
			this.setIntestazioneDocumento(intestazione, row);
			
			int rows = 1;
			Class c = Class.forName(modelClassname);
			getLogger().debug("Classe oggetto dati "+c.getName());
			List<String> metodi = DiaUtility.getOrderedModelClassGETMethods(DiaUtility.getModelClassGETMethods(c), c);
			
			getLogger().debug("Creazione excel tabella dati");
			for(SuperDia sd: dettaglio) {
				row = sheet.createRow(rows);
				
				//recupero valori e inserimento in excel
				for(int i=0; i<intestazione.size(); i++) {
					//i metodi devono risultare lo stesso numero delle colonne di intestazione
					Method m = c.getMethod(metodi.get(i));
					Object obj = m.invoke(c.cast(sd));
					if(obj != null && !(obj instanceof DiaTestata)) {  
						row.createCell(i).setCellValue(new HSSFRichTextString(obj.toString()));
					}
					else {
						row.createCell(i).setCellValue(new HSSFRichTextString(""));
					}
				}
				
				if(rows >= new Integer(cfg.getProperty("exp.sheet.row.limit")).intValue()) {
					//formattazione colonne
					for(int i=0; i<intestazione.size(); i++)
						sheet.autoSizeColumn((short)i);
					
					rows = 0;
					//nuovo sheet	
					sheet = wb.createSheet(cfg.getProperty("exp.sheet.name")+" "+(++nofsheet));
					row = sheet.createRow(0);
					this.setIntestazioneDocumento(intestazione, row);
				}
				
				rows++;
			}
			
			//formattazione colonne
			for(int i=0; i<intestazione.size(); i++)
				sheet.autoSizeColumn((short)i);
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			wb.write(baos);
			baos.flush();
			
			getLogger().info("Downloading...");
			downloadXLS(baos,cfg.getProperty("exp.doc.name")+DiaUtility.getNameFromClass(modelClassname));
			getLogger().info("Export done !");
			
			doSaveLog(super.getMessage("dia.export"),idDiaTestata,diaService);
			
		}catch(Exception e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}
		
	}
	
	
	/**
	 * Il metodo del bean esegue la scrittura di un file 
	 * temporaneo nella directory temporane del sistema operativo
	 */
	public void doExportCSV() {
		getLogger().info("Export dettaglio diagnostica in formato CSV");
		
		File f = null;
		String row = null;
		
		try {	
			DiaDettaglioDTO dd = new DiaDettaglioDTO(super.getEnte(),super.getUser().getName());
			dd.setIdDiaTestata(idDiaTestata);
			dd.setModelClassname(modelClassname);
			
			getLogger().debug("Recupero dati");
			List<? extends SuperDia> dettaglio = diaDettaglioService.getDettaglioDiagnosticaCompleto(dd);
			getLogger().debug("Item list: "+ dettaglio.size());
			
			getLogger().debug("Recupero configurazione export csv");
			Properties cfg = getDiaExportConfig();
			String csvdelim = cfg.getProperty("exp.csv.delimiter");
			
			getLogger().debug("Creazione csv");
			//recupero colonne
			DataProviderImpl dp = (DataProviderImpl)super.getBeanReference("dataProviderImpl");
			dp.setModelClassname(modelClassname);
			dp.setIdDiaTestata(idDiaTestata);
		
			String tmpout = System.getProperty(cfg.getProperty("exp.csv.write.dir.property"));
			getLogger().debug("CSV tmp out dir: "+tmpout);
			String nomeTmpCSV = cfg.getProperty("exp.csv.tmp.prefix");
			getLogger().debug("CSV tmp out file: "+nomeTmpCSV);
			f = File.createTempFile(nomeTmpCSV,cfg.getProperty("exp.csv.tmp.ext"),new File(tmpout));
			FileWriter fw = new FileWriter(f);
			
			getLogger().debug("Preparazione intestazione csv");
			List<String> intestazione = dp.getColumns();
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<intestazione.size(); i++) {
				String col = intestazione.get(i);
				sb.append(col);
				sb.append(csvdelim);
				getLogger().debug("colonna: "+col);
			}
			
			row = sb.substring(0, sb.length()- csvdelim.length());
			getLogger().debug("Intestazione: "+row);
			
			fw.write(row);
			fw.flush();
			
			Class c = Class.forName(modelClassname);
			getLogger().debug("Classe oggetto dati "+c.getName());
			List<String> metodi = DiaUtility.getOrderedModelClassGETMethods(DiaUtility.getModelClassGETMethods(c), c);
			
			getLogger().debug("Creazione csv tabella dati");
			sb = new StringBuilder();
			for(SuperDia sd: dettaglio) {
				//recupero valori e inserimento in excel
				for(int i=0; i<intestazione.size(); i++) {
					//i metodi devono risultare lo stesso numero delle colonne di intestazione
					Method m = c.getMethod(metodi.get(i));
					Object obj = m.invoke(c.cast(sd));
					if(obj != null && !(obj instanceof DiaTestata))   
						sb.append(obj.toString());
					else 
						sb.append("");

					sb.append(csvdelim);
				}
				
				row = "\n"+sb.substring(0, sb.length()- csvdelim.length());
				fw.write(row);
				sb = new StringBuilder();
			}
			
			fw.flush();
			fw.close();
			getLogger().info("File csv temporaneo creato ["+f.getName()+"]");
			
			getLogger().info("Downloading...");
			downloadCSV(f,cfg.getProperty("exp.doc.name")+DiaUtility.getNameFromClass(modelClassname));
			getLogger().info("Export done !");
			
			doSaveLog(super.getMessage("dia.export"),idDiaTestata,diaService);
			
		}catch(Exception e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}finally {
			getLogger().debug("Cancellazione csv file temporaneo");
			if(f.delete()) 
				getLogger().debug("File csv temporaneo eliminato");
			else
				getLogger().debug("File csv temporaneo non eliminato: rimuovere a mano !!");
		}
	}
	
	
	private void setIntestazioneDocumento(List<String> intestazione, HSSFRow row) throws Exception {
		for(int i=0; i<intestazione.size(); i++) {
			String col = intestazione.get(i);
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(new HSSFRichTextString(col));
			getLogger().debug("colonna: "+col);
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
		baos.flush();
		//baos.close();
		
		getLogger().debug("Completing response...");
		response.setContentLength(baos.size());
		faces.responseComplete();
		getLogger().debug("Response complete");
	}
	
	private void downloadCSV(File f, String nomeFile) throws IOException {
		
		FacesContext faces = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
		
		getLogger().debug("Getting response");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setContentType("application/csv");
		response.addHeader("Content-disposition", "attachment;filename=\""+ nomeFile + ".csv");
		
		getLogger().debug("Setting output stream");
		ServletOutputStream out = response.getOutputStream();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(f);
		byte[] bb = new byte[fis.available()];
		fis.read(bb);
		baos.write(bb);
		fis.close();
		
		baos.writeTo(out);
		baos.flush();
		//baos.close();
		
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
	
	
	public String getNumTipoGest() {
		return numTipoGest;
	}



	public void setNumTipoGest(String numTipoGest) {
		this.numTipoGest = numTipoGest;
	}



	public String getNumTipoGestValue() {
		return numTipoGestValue;
	}



	public void setNumTipoGestValue(String numTipoGestValue) {
		this.numTipoGestValue = numTipoGestValue;
	}



	public DiaDettaglioService getDiaDettaglioService() {
		return diaDettaglioService;
	}


	public void setDiaDettaglioService(DiaDettaglioService diaDettaglioService) {
		this.diaDettaglioService = diaDettaglioService;
	}



	public Long getIdDiaTestata() {
		return idDiaTestata;
	}



	public void setIdDiaTestata(Long idDiaTestata) {
		this.idDiaTestata = idDiaTestata;
	}




	public String getDiaDescr() {
		return diaDescr;
	}



	public void setDiaDescr(String diaDescr) {
		this.diaDescr = diaDescr;
	}



	public String getCodCommand() {
		return codCommand;
	}



	public void setCodCommand(String codCommand) {
		this.codCommand = codCommand;
	}

	public String getModelClassname() {
		return modelClassname;
	}

	public void setModelClassname(String modelClassname) {
		this.modelClassname = modelClassname;
	}

	public boolean isExportXLS() {
		return exportXLS;
	}

	public void setExportXLS(boolean exportXLS) {
		this.exportXLS = exportXLS;
	}

	public boolean isExportCSV() {
		return exportCSV;
	}

	public void setExportCSV(boolean exportCSV) {
		this.exportCSV = exportCSV;
	}	
}
