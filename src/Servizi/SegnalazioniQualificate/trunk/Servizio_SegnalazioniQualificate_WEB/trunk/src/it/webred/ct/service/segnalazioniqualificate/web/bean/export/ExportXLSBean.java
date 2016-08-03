package it.webred.ct.service.segnalazioniqualificate.web.bean.export;


import it.webred.ct.data.access.basic.segnalazionequalificata.SegnalazioniDataIn;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.PraticaSegnalazioneDTO;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.RicercaPraticaSegnalazioneDTO;

import it.webred.ct.data.model.segnalazionequalificata.SOfPratica;
import it.webred.ct.service.segnalazioniqualificate.web.bean.SegnalazioniQualificateBaseBean;
import it.webred.ct.service.segnalazioniqualificate.web.bean.pagination.DataProviderImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.faces.context.ExternalContext;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExportXLSBean extends SegnalazioniQualificateBaseBean {

	private WritableCellFormat timesBold;
	private WritableCellFormat times;
	
	public void searchResultExportToXls() throws IOException, WriteException {
		double val = Math.random();
		String filePath = "C:/RicercaSegnalazioni@" + val + ".xls";
		FileOutputStream file = new FileOutputStream(filePath);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Parametri di Ricerca", 0);
		workbook.createSheet("Risultati di Ricerca", 1);
		
		WritableSheet paramsSheet = workbook.getSheet(0);
		createCriteriaLabel(paramsSheet);
		createCriteriaContent(paramsSheet);
		
		WritableSheet resultSheet = workbook.getSheet(1);
		createLabel(resultSheet);
		createContent(resultSheet);
		
		workbook.write();
		workbook.close();
		file.flush();
		close(file);
		showXls(filePath);
	}
	

	private void createLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font
		WritableFont times10ptBold = new WritableFont(WritableFont.TIMES, 10,
				WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		timesBold = new WritableCellFormat(times10ptBold);
		// Lets automatically wrap the cells
		timesBold.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBold);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 0, 0, "Numero Pratica");
		addCaption(sheet, 1, 0, "Data Inizio Iter");
		addCaption(sheet, 2, 0, "Data Chiusura Iter");
		addCaption(sheet, 3, 0, "Finalità");
		addCaption(sheet, 4, 0, "Stato della Pratica");
		addCaption(sheet, 5, 0, "Cod.Fiscale Responsabile");
		addCaption(sheet, 6, 0, "Responsabile Procedimento");
		addCaption(sheet, 7, 0, "P.IVA/Cod.Fiscale");
		addCaption(sheet, 8, 0, "Denominazione/Cognome e Nome");

	}
	
	private void createCriteriaLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font
		WritableFont times10ptBold = new WritableFont(WritableFont.TIMES, 10,
				WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		timesBold = new WritableCellFormat(times10ptBold);
		// Lets automatically wrap the cells
		timesBold.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBold);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 0, 0,  "Cod.Fiscale Soggetto Accertato");
		addCaption(sheet, 0, 1,  "Cognome Soggetto Accertato");
		addCaption(sheet, 0, 2,  "Nome Soggetto Accertato");
		addCaption(sheet, 0, 3,  "P.IVA Soggetto Accertato");
		addCaption(sheet, 0, 4,  "Denominazione Soggetto Accertato");
		addCaption(sheet, 0, 5,  "Cod.Fiscale Responsabile");
		addCaption(sheet, 0, 6,  "Cognome Responsabile");
		addCaption(sheet, 0, 7,  "Nome Responsabile");
		addCaption(sheet, 0, 8,  "Data Inizio Procedimento DA");
		addCaption(sheet, 0, 9,  "Data Inizio Procedimento A");
		addCaption(sheet, 0, 10, "Finalità");
		addCaption(sheet, 0, 11, "Stato");
		addCaption(sheet, 0, 12, "Operatori");

	}
	
	
	private String getDescrizioneFlagPresenza(String flag, boolean tarsuPost){
		String desc = "Non elaborato";
		if(flag!=null){
			if(!tarsuPost){
				desc = "1".equalsIgnoreCase(flag)? "Presente" : "Assente";
			}else{	
				desc = "Assente";
				if( "1".equals(flag))
				  desc = "Presente, prima del 20-01";
				else if("2".equals(flag))
				   desc = "Presente, dopo il 20-01";
			}
		}
		return desc;
	}
	
	private String getDescrizioneFlagSiNo(String flag){
		String desc = "Non elaborato";
		if(flag!=null){
			desc = "1".equalsIgnoreCase(flag)? "SI" : "NO";
		}
		return desc;
	}
	

	private void createContent(WritableSheet sheet) throws WriteException,
			RowsExceededException {

		try {

			DataProviderImpl impl = (DataProviderImpl) getBeanReference("praticaDataProviderImpl");
			RicercaPraticaSegnalazioneDTO criteria = impl.getCriteria();

			criteria.setStartm(0);
			criteria.setNumberRecord(1000);
			SegnalazioniDataIn dataIn = this.getInitRicercaParams();
			fillEnte(criteria);
			dataIn.setRicercaPratica(criteria);
			List<SOfPratica> lista = segnalazioneService.search(dataIn);
			
			int row = 1;
			for (SOfPratica p: lista) {

				PraticaSegnalazioneDTO dto = new PraticaSegnalazioneDTO();
				dto.setPratica(p);
				
				addLabel(sheet, 0, row, Long.toString(p.getId()));
				addLabel(sheet, 1, row,  p.getAccDataInizio() != null? sdf.format(p.getAccDataInizio()): "");
				addLabel(sheet, 2, row,  p.getAccDataFine() != null? sdf.format(p.getAccDataFine()): "");
				addLabel(sheet, 3, row,  dto.getAccFinalitaDecoded());
				addLabel(sheet, 4, row,  dto.getAccStatoDecoded());
				addLabel(sheet, 5, row, p.getResCognome()+" "+p.getResNome());
				addLabel(sheet, 6, row, p.getResCodiFisc());
				
				if("G".equalsIgnoreCase(p.getAccTipoSogg())){
					addLabel(sheet, 7, row, p.getAccDenominazione());
					addLabel(sheet, 8, row, p.getAccCodiPiva());
				}else{
					addLabel(sheet, 7, row, p.getAccCognome()+" "+p.getAccNome());
					addLabel(sheet, 8, row, p.getAccCodiFisc());
				}
				
				++row;
			}
			
			if(lista.size() >= 1000)
				addCaption(sheet, 0, row + 2, "RISULTATI LIMITATI A 1000 RECORD");

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	private void createCriteriaContent(WritableSheet sheet){
		
		try {
		
			DataProviderImpl impl = (DataProviderImpl) getBeanReference("praticaDataProviderImpl");
			RicercaPraticaSegnalazioneDTO criteria = impl.getCriteria();
			
			addLabel(sheet, 1, 0, criteria.getAccCodiFisc());
			addLabel(sheet, 1, 1, criteria.getAccCognome());
			addLabel(sheet, 1, 2, criteria.getAccNome());
			addLabel(sheet, 1, 3, criteria.getAccCodiPiva());
			addLabel(sheet, 1, 4, criteria.getAccDenominazione());
			
			addLabel(sheet, 1, 5, criteria.getResCodiFisc());
			addLabel(sheet, 1, 6, criteria.getResCognome());
			addLabel(sheet, 1, 7, criteria.getResNome());
			
			addLabel(sheet, 1, 8, criteria.getDataInizioDa() != null? sdf.format(criteria.getDataInizioDa()): "");
			addLabel(sheet, 1, 9, criteria.getDataInizioA()  != null? sdf.format(criteria.getDataInizioA()) : "");
			addLabel(sheet, 1, 10, criteria.getAccFinalitaDecoded());
			addLabel(sheet, 1, 11, criteria.getAccStatoDecoded());
			
			addLabel(sheet, 1, 12, criteria.getsOperatori());
			
		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}
	

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBold);
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row,
			Integer integer) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, times);
		sheet.addCell(number);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}

	private void showXls(String filePath) throws IOException {
		File f = new File(filePath);
		if (f.exists()) {

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			int DEFAULT_BUFFER_SIZE = 10240;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletResponse response = (HttpServletResponse) externalContext
					.getResponse();

			try {

				bis = new BufferedInputStream(new FileInputStream(f),
						DEFAULT_BUFFER_SIZE);

				response.setContentType("application/vnd.ms-excel");
				response
						.setHeader("Content-Length", String.valueOf(f.length()));
				int indx = f.getName().indexOf("@");
				String fileName = f.getName().substring(0, indx) + ".xls";
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + fileName + "\"");
				bos = new BufferedOutputStream(response.getOutputStream(),
						DEFAULT_BUFFER_SIZE);

				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int length;
				while ((length = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, length);
				}

				bos.flush();

			} catch (Throwable t) {
				super.addErrorMessage("file.download.error", t.getMessage());
				t.printStackTrace();
			} finally {
				close(bos);
				close(bis);
				f.delete();
			}

			facesContext.responseComplete();
		} else
			super.addErrorMessage("file.download.null", "");
	}

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
