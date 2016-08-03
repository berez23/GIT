package it.webred.ct.service.muidocfa.web.bean.export;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.diagnostiche.DocfaIciReport;
import it.webred.ct.data.model.diagnostiche.DocfaIciReportSogg;
import it.webred.ct.data.model.diagnostiche.DocfaTarReport;
import it.webred.ct.data.model.diagnostiche.DocfaTarReportSogg;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;
import it.webred.ct.service.muidocfa.web.bean.ici.pagination.DataProviderImpl;
import it.webred.ct.service.muidocfa.web.bean.util.CommonDataBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.faces.context.ExternalContext;

import com.itextpdf.text.BaseColor;

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

public class ExportXLSBean extends MuiDocfaBaseBean {

	private WritableCellFormat timesBold;
	private WritableCellFormat times;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public void resultListIciExportToXls() throws IOException, WriteException {
		double val = Math.random();
		String filePath = "C:/ReportIci@" + val + ".xls";
		FileOutputStream file = new FileOutputStream(filePath);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createIciLabel(excelSheet, false);
		createIciContent(excelSheet);

		workbook.write();
		workbook.close();
		file.flush();
		close(file);
		showXls(filePath);
	}
	
	public void resultListTarsuExportToXls() throws IOException, WriteException {
		double val = Math.random();
		String filePath = "C:/ReportTarsu@" + val + ".xls";
		FileOutputStream file = new FileOutputStream(filePath);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createTarsuLabel(excelSheet, false);
		createTarsuContent(excelSheet);

		workbook.write();
		workbook.close();
		file.flush();
		close(file);
		showXls(filePath);
	}
	
	public void resultListIciSoggExportToXls() throws IOException, WriteException {
		double val = Math.random();
		String filePath = "C:/ReportIciSogg@" + val + ".xls";
		FileOutputStream file = new FileOutputStream(filePath);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		
		createIciLabel(excelSheet, true);
		
		createIciSoggContent(excelSheet);

		workbook.write();
		workbook.close();
		file.flush();
		close(file);
		showXls(filePath);
	}
	
	public void resultListTarsuSoggExportToXls() throws IOException, WriteException {
		double val = Math.random();
		String filePath = "C:/ReportTarsuSogg@" + val + ".xls";
		FileOutputStream file = new FileOutputStream(filePath);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		
		createTarsuLabel(excelSheet, true);
		
		createTarsuSoggContent(excelSheet);

		workbook.write();
		workbook.close();
		file.flush();
		close(file);
		showXls(filePath);
	}


	private void createIciLabel(WritableSheet sheet, boolean addSoggetti) throws WriteException {
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
		addCaption(sheet, 0, 0, "Elaborato");
		sheet.setColumnView(0, 25);
		addCaption(sheet, 1, 0, "Data fornitura");
		sheet.setColumnView(1, 15);
		addCaption(sheet, 2, 0, "Protocollo");
		addCaption(sheet, 3, 0, "Data registrazione");
		sheet.setColumnView(3, 15);
		addCaption(sheet, 4, 0, "Foglio");
		addCaption(sheet, 5, 0, "Particella");
		addCaption(sheet, 6, 0, "Subalterno");
		addCaption(sheet, 7, 0, "Tipo operazione");
		sheet.setColumnView(7, 15);
		addCaption(sheet, 8, 0, "Causale");
		sheet.setColumnView(8, 25);
		addCaption(sheet, 9, 0, "Categoria");
		sheet.setColumnView(9, 25);
		addCaption(sheet, 10, 0, "Classe");
		addCaption(sheet, 11, 0, "Rendita");
		addCaption(sheet, 12, 0, "Var.Imponibile");
		addCaption(sheet, 13, 0, "Indirizzo");
		sheet.setColumnView(13, 25);
		addCaption(sheet, 14, 0, "Ici ante Docfa");
		addCaption(sheet, 15, 0, "Ici post Docfa");
		addCaption(sheet, 16, 0, "A catasto alla data del Docfa");
		sheet.setColumnView(16, 15);
		addCaption(sheet, 17, 0, "Docfa di cessazione senza costituzione");
		sheet.setColumnView(17, 15);
		addCaption(sheet, 18, 0, "Docfa di variazione di categoria");
		sheet.setColumnView(18, 15);
		addCaption(sheet, 19, 0, "Docfa di variazione classe");
		sheet.setColumnView(19, 15);
		addCaption(sheet, 20, 0, "Docfa di variazione rendita");
		sheet.setColumnView(20, 15);
		
		if(addSoggetti){
			concatLabelSoggetti(sheet, 21);
		}

	}
	
	private void createTarsuLabel(WritableSheet sheet, boolean addSoggetti) throws WriteException {
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
		addCaption(sheet, 0, 0, "Elaborato");
		sheet.setColumnView(0, 25);
		addCaption(sheet, 1, 0, "Data fornitura");
		sheet.setColumnView(1, 15);
		addCaption(sheet, 2, 0, "Protocollo");
		addCaption(sheet, 3, 0, "Data registrazione");
		sheet.setColumnView(3, 15);
		addCaption(sheet, 4, 0, "Foglio");
		addCaption(sheet, 5, 0, "Particella");
		addCaption(sheet, 6, 0, "Subalterno");
		addCaption(sheet, 7, 0, "Tipo operazione");
		sheet.setColumnView(7, 15);
		addCaption(sheet, 8, 0, "Causale");
		sheet.setColumnView(8, 25);
		addCaption(sheet, 9, 0, "Categoria");
		addCaption(sheet, 10, 0, "Sup.Docfa");
		addCaption(sheet, 11, 0, "Sup.C340 da Quadro C/1 e C/2");
		sheet.setColumnView(11, 25);
		addCaption(sheet, 12, 0, "Sup.C340 =(supA+supB+supC)*0.8");
		sheet.setColumnView(12, 25);
		addCaption(sheet, 13, 0, "Consistenza Calcolata");
		sheet.setColumnView(13, 25);
		addCaption(sheet, 14, 0, "Indirizzo");
		sheet.setColumnView(14, 25);
		addCaption(sheet, 15, 0, "Tarsu ante Docfa");
		addCaption(sheet, 16, 0, "Tarsu post Docfa");
		addCaption(sheet, 17, 0, "A catasto alla data del Docfa");
		sheet.setColumnView(17, 17);
		addCaption(sheet, 18, 0, "Docfa di cessazione senza costituzione");
		sheet.setColumnView(18, 25);
		
		if(addSoggetti){
			concatLabelSoggetti(sheet, 19);
		}

	}
	
	private void concatLabelSoggetti(WritableSheet sheet, int index) throws WriteException{
		
		//Aggiunge le label dei soggetti titolari
		addCaption(sheet, index++, 0, "Denominazione");
		addCaption(sheet, index++, 0, "Cod.Fiscale/P.IVA");
		addCaption(sheet, index++, 0, "Sesso");
		addCaption(sheet, index++, 0, "Data nascita");
		addCaption(sheet, index++, 0, "Data inizio possesso");
		addCaption(sheet, index++, 0, "Data fine possesso");
		addCaption(sheet, index++, 0, "Titolare Data Docfa");
		addCaption(sheet, index++, 0, "Titolo");
		addCaption(sheet, index++, 0, "%");
		
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
	

	private void createIciContent(WritableSheet sheet) throws WriteException,
			RowsExceededException {

		try {

			it.webred.ct.service.muidocfa.web.bean.ici.pagination.DataProviderImpl iciImpl = (it.webred.ct.service.muidocfa.web.bean.ici.pagination.DataProviderImpl) getBeanReference("iciDataProviderImpl");
			RicercaDocfaReportDTO criteria = iciImpl.getCriteria();

			criteria.setStartm(0);
			criteria.setNumberRecord(1000);
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(criteria);
			List<DocfaIciReport> lista = docfaIciReportService.searchReport(dataIn);

			int row = 1;
			for (DocfaIciReport rep: lista) {

				addRowReportIci(sheet, rep, row);
				++row;
			}
			
			if(lista.size() >= 1000)
				addCaption(sheet, 0, row + 2, "RISULTATI LIMITATI A 1000 RECORD");

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	private void createTarsuContent(WritableSheet sheet) throws WriteException,
	RowsExceededException {

		try {
		
			it.webred.ct.service.muidocfa.web.bean.tarsu.pagination.DataProviderImpl iciImpl = (it.webred.ct.service.muidocfa.web.bean.tarsu.pagination.DataProviderImpl) getBeanReference("tarDataProviderImpl");
			RicercaDocfaReportDTO criteria = iciImpl.getCriteria();
		
			criteria.setStartm(0);
			criteria.setNumberRecord(1000);
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(criteria);
			List<DocfaTarReport> lista = docfaTarReportService.searchReport(dataIn);
		
			int row = 1;
			for (DocfaTarReport rep: lista) {
		
				addRowReportTarsu(sheet, rep, row);
				++row;
			}
			
			if(lista.size() >= 1000)
				addCaption(sheet, 0, row + 2, "RISULTATI LIMITATI A 1000 RECORD");
		
		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	
	private void createIciSoggContent(WritableSheet sheet)
			throws WriteException, RowsExceededException {

		try {

			it.webred.ct.service.muidocfa.web.bean.ici.pagination.DataProviderImpl iciImpl = (it.webred.ct.service.muidocfa.web.bean.ici.pagination.DataProviderImpl) getBeanReference("iciDataProviderImpl");
			RicercaDocfaReportDTO criteria = iciImpl.getCriteria();

			criteria.setStartm(0);
			criteria.setNumberRecord(1000);
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(criteria);
			List<DocfaIciReport> lista = docfaIciReportService.searchReport(dataIn);

			int row = 1;
			for (DocfaIciReport rep : lista) {
				
				List<DocfaIciReportSogg> listaSoggetti = iciImpl.getReportSogg(rep.getIdRep());
				
				if(listaSoggetti.isEmpty()){
					
					addRowReportIci(sheet, rep, row);
					++row;
					
				}else{
				
					for(DocfaIciReportSogg repSogg : listaSoggetti){
					
						int col = addRowReportIci(sheet, rep, row);
						
						addLabel(sheet, col++, row, (repSogg.getRagiSoci()+ " " + repSogg.getNome()).trim());
						
						String cod = "";
						if(repSogg.getCodiFisc()!=null)
							cod = repSogg.getCodiFisc();
						if(repSogg.getCodiPiva()!=null)
							cod += repSogg.getCodiPiva();
						
						addLabel(sheet, col++, row, cod.trim());
						addLabel(sheet, col++, row, repSogg.getSesso());
						addLabel(sheet, col++, row, repSogg.getDataNasc()!= null ? sdf
								.format(repSogg.getDataNasc()) : "");
						
						addLabel(sheet, col++, row, repSogg.getId().getDataInizioPossesso()!= null ? sdf
								.format(repSogg.getId().getDataInizioPossesso()) : "");
						addLabel(sheet, col++, row, repSogg.getId().getDataFinePossesso()!= null ? sdf
								.format(repSogg.getId().getDataFinePossesso()) : "");
						
						addLabel(sheet, col++, row, getDescrizioneFlagSiNo(repSogg.getFlgTitolareDataDocfa()));
						
						addLabel(sheet, col++, row, repSogg.getTitolo());
						
						addLabel(sheet, col++, row, repSogg.getPercPoss() != null ? (repSogg.getPercPoss()+ "%") : "");
						
						++row;
					
					}
				
				}
				
			}

			if (lista.size() >= 1000)
				addCaption(sheet, 0, row + 2,
						"RISULTATI LIMITATI A 1000 RECORD");

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}

	private void createTarsuSoggContent(WritableSheet sheet)
			throws WriteException, RowsExceededException {

		try {

			it.webred.ct.service.muidocfa.web.bean.tarsu.pagination.DataProviderImpl tarImpl = (it.webred.ct.service.muidocfa.web.bean.tarsu.pagination.DataProviderImpl) getBeanReference("tarDataProviderImpl");
			RicercaDocfaReportDTO criteria = tarImpl.getCriteria();

			criteria.setStartm(0);
			criteria.setNumberRecord(1000);
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(criteria);
			List<DocfaTarReport> lista = docfaTarReportService
					.searchReport(dataIn);

			int row = 1;
			for (DocfaTarReport rep : lista) {

				List<DocfaTarReportSogg> listaSoggetti = tarImpl.getReportSogg(rep.getIdRep());
				
				if(listaSoggetti.isEmpty()){
					
					addRowReportTarsu(sheet, rep, row);
					++row;
					
				}else{
					
					for(DocfaTarReportSogg repSogg : listaSoggetti){
					
						int col = addRowReportTarsu(sheet, rep, row);
						
						addLabel(sheet, col++, row, (repSogg.getRagiSoci()+ " " + repSogg.getNome()).trim());
						
						String cod = "";
						if(repSogg.getCodiFisc()!=null)
							cod = repSogg.getCodiFisc();
						if(repSogg.getCodiPiva()!=null)
							cod += repSogg.getCodiPiva();
						
						addLabel(sheet, col++, row, cod.trim());
						addLabel(sheet, col++, row, repSogg.getSesso());
						addLabel(sheet, col++, row, repSogg.getDataNasc()!= null ? sdf
								.format(repSogg.getDataNasc()) : "");
						
						addLabel(sheet, col++, row, repSogg.getId().getDataInizioPossesso()!= null ? sdf.format(repSogg.getId().getDataInizioPossesso()) : "");
						addLabel(sheet, col++, row, repSogg.getId().getDataFinePossesso()!= null ? sdf.format(repSogg.getId().getDataFinePossesso()) : "");
						
						addLabel(sheet, col++, row, this.getDescrizioneFlagSiNo(repSogg.getFlgTitolareDataDocfa()));
						
						addLabel(sheet, col++, row, repSogg.getTitolo());
						
						addLabel(sheet, col++, row, repSogg.getPercPoss() != null ? (repSogg.getPercPoss()+ "%") : "");
						
						++row;
					}
				}
			}

			if (lista.size() >= 1000)
				addCaption(sheet, 0, row + 2,
						"RISULTATI LIMITATI A 1000 RECORD");

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	private int addRowReportIci(WritableSheet sheet, DocfaIciReport rep, int row )
		throws WriteException, RowsExceededException{
		
		addLabel(sheet,0,row,
				("1".equals(rep.getFlgElaborato()) ? "SI " : "NO ")
						+ (rep.getAnnotazioni() != null ? "con anomalie: "
								+ rep.getAnnotazioni().replaceAll("@",
										";")
								: ""));
		addLabel(sheet, 1, row, rep.getFornitura() != null ? sdf
				.format(rep.getFornitura()) : "");
		addLabel(sheet, 2, row, rep.getProtocolloDocfa());
		addLabel(sheet, 3, row, rep.getDataDocfaToDate() != null ? sdf
				.format(rep.getDataDocfaToDate()) : "");
		addLabel(sheet, 4, row, rep.getFoglio());
		addLabel(sheet, 5, row, rep.getParticella());
		addLabel(sheet, 6, row, rep.getSubalterno() != null ? rep
				.getSubalterno() : "");
		addLabel(sheet, 7, row, rep.getTipoOperDocfaEx());
		addLabel(sheet, 8, row, CommonDataBean.getCausali().get(
				rep.getCausaleDocfa()));
		addLabel(sheet, 9, row, rep.getCategoriaDocfa());
		if ("1".equals(rep.getFlgAnomaliaClasse()))
			addLabel(sheet, 10, row, rep.getClasseDocfa() + "("
					+ rep.getClasseMin() + ")");
		else
			addLabel(sheet, 10, row, rep.getClasseDocfa());
		addLabel(sheet, 11, row, rep.getRenditaDocfa() != null ? "  "
				+ rep.getRenditaDocfa() : "");
		addLabel(sheet, 12, row, rep.getVarImponibileUiu() != null ? " "
				+ rep.getVarImponibileUiu() : "");
		addLabel(sheet, 13, row,
				(rep.getPrefissoViaDocfa() != null ? rep
						.getPrefissoViaDocfa() : "")
						+ " "
						+ (rep.getViaDocfa() != null ? rep
								.getViaDocfa() : "")
						+ " "
						+ (rep.getCiviciDocfa() != null ? rep
								.getCiviciDocfa().replaceAll("@", ";")
								: ""));

		addLabel(sheet, 14, row, getDescrizioneFlagPresenza(rep
				.getFlgIciAnteDocfa(), false));
		addLabel(sheet, 15, row, getDescrizioneFlagPresenza(rep
				.getFlgIciPostDocfa(), false));
		addLabel(sheet, 16, row, getDescrizioneFlagPresenza(rep
				.getFlgUiuCatasto(), false));
		addLabel(sheet, 17, row, getDescrizioneFlagSiNo(rep
				.getFlgDocfaSNoC()));
		addLabel(sheet, 18, row, getDescrizioneFlagSiNo(rep
				.getFlgVarAnteCategoria()));
		addLabel(sheet, 19, row, getDescrizioneFlagSiNo(rep
				.getFlgVarAnteClasse()));
		addLabel(sheet, 20, row, getDescrizioneFlagSiNo(rep
				.getFlgVarAnteRendita()));
		
		return 21;
	}
	
	private int addRowReportTarsu(WritableSheet sheet, DocfaTarReport rep, int row )
	throws WriteException, RowsExceededException{
		
		addLabel(
				sheet,
				0,
				row,
				("1".equals(rep.getFlgElaborato()) ? "SI " : "NO ")
						+ (rep.getAnnotazioni() != null ? "con anomalie: "
								+ rep.getAnnotazioni().replaceAll("@",
										";")
								: ""));
		addLabel(sheet, 1, row, rep.getFornitura() != null ? sdf
				.format(rep.getFornitura()) : "");
		addLabel(sheet, 2, row, rep.getProtocolloDocfa());
		addLabel(sheet, 3, row, rep.getDataDocfaToDate() != null ? sdf
				.format(rep.getDataDocfaToDate()) : "");
		addLabel(sheet, 4, row, rep.getFoglio());
		addLabel(sheet, 5, row, rep.getParticella());
		addLabel(sheet, 6, row, rep.getSubalterno() != null ? rep
				.getSubalterno() : "");
		addLabel(sheet, 7, row, rep.getTipoOperDocfaEx());
		addLabel(sheet, 8, row, CommonDataBean.getCausali().get(
				rep.getCausaleDocfa()));
		addLabel(sheet, 9, row, rep.getCategoriaDocfa());
		addLabel(sheet, 10, row, rep.getSupDocfaCens() != null ? (rep
				.getSupDocfaCens() + " mq") : "");
		addLabel(sheet, 11, row, rep.getSupDocfaTarsu()!= null ? (rep
				.getSupDocfaTarsu() + " mq") : "");
		addLabel(sheet, 12, row, rep.getSupC340Abc() != null ? (rep
				.getSupC340Abc() + " mq") : "");
		addLabel(sheet, 13, row, rep.getConsCalc() != null ? (rep
				.getConsCalc()
				+ " ("
				+ rep.getSupAvgMin()
				+ " - "
				+ rep.getSupAvgMax() + ")") : "");
		addLabel(sheet, 14, row,
				(rep.getPrefissoViaDocfa() != null ? rep
						.getPrefissoViaDocfa() : "")
						+ " "
						+ (rep.getViaDocfa() != null ? rep
								.getViaDocfa() : "")
						+ " "
						+ (rep.getCiviciDocfa() != null ? rep
								.getCiviciDocfa().replaceAll("@", ";")
								: ""));

		addLabel(sheet, 15, row, getDescrizioneFlagPresenza(rep
				.getFlgTarAnteDocfa(), false));
		addLabel(sheet, 16, row, getDescrizioneFlagPresenza(rep
				.getFlgTarPostDocfa(), true));
		addLabel(sheet, 17, row, getDescrizioneFlagPresenza(rep
				.getFlgUiuCatasto(), false));
		addLabel(sheet, 18, row, getDescrizioneFlagSiNo(rep
				.getFlgDocfaSNoC()));
		
		return 19;
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
	
	public String getCategoriaEstesa(String codCategoria) {

		if (codCategoria != null && !"".equals(codCategoria)) {
			try {

				RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
				ro.setCodCategoria(codCategoria);
				fillEnte(ro);
				List<Sitideco> lista = catastoService
						.getListaCategorieImmobile(ro);
				return lista.get(0).getDescription();

			} catch (Throwable t) {
				super.addErrorMessage("categorie.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return "";
	}
	/*
	 * public static void main(String[] args) throws WriteException, IOException
	 * { WriteExcel test = new WriteExcel();
	 * test.setOutputFile("c:/temp/lars.xls"); test.write(); System.out
	 * .println("Please check the result file under c:/temp/lars.xls "); }
	 */
}
