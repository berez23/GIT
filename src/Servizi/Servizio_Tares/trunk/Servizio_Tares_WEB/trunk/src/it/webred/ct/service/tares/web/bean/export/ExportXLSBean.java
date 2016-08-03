package it.webred.ct.service.tares.web.bean.export;

import it.webred.ct.service.tares.data.model.SetarDia;
import it.webred.ct.service.tares.data.model.SetarElab;
import it.webred.ct.service.tares.data.model.SetarStat;
import it.webred.ct.service.tares.web.bean.SetarTariffeElabUteDom;
import it.webred.ct.service.tares.web.bean.SetarTariffeElabUteNonDom;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

public class ExportXLSBean {

	private WritableCellFormat timesBold;
	private WritableCellFormat times;
	private List<SetarElab> lista = null;
	private List<SetarStat> listaStat = null;
	private List<SetarDia> listaDia = null;
	private List<SetarTariffeElabUteDom> listaTariUD = null;
	private List<SetarTariffeElabUteNonDom> listaTariUND = null;
	private int rowStart = 0;
	public List<SetarDia> getListaDia() {
		return listaDia;
	}

	public void setListaDia(List<SetarDia> listaDia) {
		this.listaDia = listaDia;
	}

	private String pathXLS = "";
	
	//private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat sdf = new SimpleDateFormat("#0.00");
	
	public void resultListTaresExportToXls(String choose) throws IOException, WriteException {
		double val = Math.random();
		
		String filePath = pathXLS + "/ReportTares@" + val + ".xls";
		
		FileOutputStream file = new FileOutputStream(filePath);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		if (choose.equalsIgnoreCase("ELAB")){
			createTaresLabel(excelSheet);
			createTaresContent(excelSheet);
		}else if (choose.equalsIgnoreCase("STAT")){
			createTaresStatLabel(excelSheet);
			createTaresStatContent(excelSheet);
		}else if (choose.equalsIgnoreCase("DIA")){
			createTaresDiaLabel(excelSheet);
			createTaresDiaContent(excelSheet);
		}else if (choose.equalsIgnoreCase("TARI_UD")){
			createTaresTariUDLabel(excelSheet);
			createTaresTariUDContent(excelSheet);
		}else if (choose.equalsIgnoreCase("TARI_UND")){
			createTaresTariUNDLabel(excelSheet);
			createTaresTariUNDContent(excelSheet);
		}

		workbook.write();
		workbook.close();
		file.flush();
		close(file);
		showXls(filePath);
	}//-------------------------------------------------------------------------
	
	public void createTaresTariUNDLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font
		WritableFont times10ptBold = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		timesBold = new WritableCellFormat(times10ptBold);
		// Lets automatically wrap the cells
		timesBold.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBold);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 0, 0, "Categorie");
		sheet.setColumnView(0, 25);
		addCaption(sheet, 1, 0, "Num Oggetti Cat");
		sheet.setColumnView(1, 15);
		addCaption(sheet, 2, 0, "Sup Tot Cat m^2");
		sheet.setColumnView(2, 15);
		addCaption(sheet, 3, 0, "Sup Media Locali m^2");
		sheet.setColumnView(3, 15);
		addCaption(sheet, 4, 0, "Coeff Attribuzione Parte Fissa (Kc)");
		sheet.setColumnView(4, 15);
		addCaption(sheet, 5, 0, "Kc val");
		sheet.setColumnView(5, 15);
		addCaption(sheet, 6, 0, "Coeff Attribuzione Parte Variabile (Kd)");
		sheet.setColumnView(6, 15);
		addCaption(sheet, 7, 0, "Kd val");
		sheet.setColumnView(7, 15);
		addCaption(sheet, 8, 0, "Quota Fissa (Eur/m^2)");
		sheet.setColumnView(8, 15);
		addCaption(sheet, 9, 0, "Quota Var (Eur/m^2)");
		sheet.setColumnView(9, 15);
		addCaption(sheet, 10, 0, "Tariffa Tot (Eur/Utenza)");
		sheet.setColumnView(10, 15);
		addCaption(sheet, 11, 0, "Tot Gettito");
		sheet.setColumnView(11, 15);

	}//-------------------------------------------------------------------------
	
	public void createTaresTariUDLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font
		WritableFont times10ptBold = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		timesBold = new WritableCellFormat(times10ptBold);
		// Lets automatically wrap the cells
		timesBold.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBold);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 0, 0, "Famiglie");
		sheet.setColumnView(0, 25);
		addCaption(sheet, 1, 0, "Num Nuclei Fam");
		sheet.setColumnView(1, 15);
		addCaption(sheet, 2, 0, "Sup Tot Abit m^2");
		sheet.setColumnView(2, 15);
		addCaption(sheet, 3, 0, "Quote Fam %");
		sheet.setColumnView(3, 15);
		addCaption(sheet, 4, 0, "Sup Med Abit m^2");
		sheet.setColumnView(4, 15);
		addCaption(sheet, 5, 0, "Coeff Attribuzione Parte Fissa (Ka)");
		sheet.setColumnView(5, 15);
		addCaption(sheet, 6, 0, "Coeff Attribuzione Parte Variabile (Kb)");
		sheet.setColumnView(6, 15);
		addCaption(sheet, 7, 0, "Quota Fissa Eur/m^2 (Quf * Ka)");
		sheet.setColumnView(7, 15);
		addCaption(sheet, 8, 0, "Quota Fissa Med (TF) ");
		sheet.setColumnView(8, 15);
		addCaption(sheet, 9, 0, "Quota Var per Fam Eur/Utenza (TV)");
		sheet.setColumnView(9, 15);
		addCaption(sheet, 10, 0, "Quota Var per Persona (Eur/Pers)");
		sheet.setColumnView(10, 15);
		addCaption(sheet, 11, 0, "Tariffa Media");
		sheet.setColumnView(11, 15);
		addCaption(sheet, 12, 0, "Tot Gettito");
		sheet.setColumnView(12, 15);

	}//-------------------------------------------------------------------------
	
	public void createTaresStatLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font
		WritableFont times10ptBold = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		timesBold = new WritableCellFormat(times10ptBold);
		// Lets automatically wrap the cells
		timesBold.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBold);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 0, 0, "Cod. Catastale");
		sheet.setColumnView(0, 25);
		addCaption(sheet, 1, 0, "Cat");
		sheet.setColumnView(1, 15);
		addCaption(sheet, 2, 0, "Numero");
		sheet.setColumnView(2, 15);
		addCaption(sheet, 3, 0, "Consis");
		sheet.setColumnView(3, 15);
		addCaption(sheet, 4, 0, "Sup Cat Tarsu");
		sheet.setColumnView(4, 15);
		addCaption(sheet, 5, 0, "Sup Cat Tarsu 80");
		sheet.setColumnView(5, 15);
		addCaption(sheet, 6, 0, "Sup 0");
		sheet.setColumnView(6, 15);
		addCaption(sheet, 7, 0, "Cons Sup 0");
		sheet.setColumnView(7, 15);

	}//-------------------------------------------------------------------------
	
	public void createTaresLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font
		WritableFont times10ptBold = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		timesBold = new WritableCellFormat(times10ptBold);
		// Lets automatically wrap the cells
		timesBold.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBold);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 0, 0, "Cod. Catastale");
		sheet.setColumnView(0, 25);
		addCaption(sheet, 1, 0, "Sez");
		sheet.setColumnView(1, 15);
		addCaption(sheet, 2, 0, "Foglio");
		sheet.setColumnView(2, 15);
		addCaption(sheet, 3, 0, "Part");
		sheet.setColumnView(3, 15);
		addCaption(sheet, 4, 0, "Denom");
		sheet.setColumnView(4, 15);
		addCaption(sheet, 5, 0, "Sub");
		sheet.setColumnView(5, 15);
		addCaption(sheet, 6, 0, "Cat");
		sheet.setColumnView(6, 15);
		addCaption(sheet, 7, 0, "Cls");
		sheet.setColumnView(7, 15);
		addCaption(sheet, 8, 0, "Cons");
		sheet.setColumnView(8, 15);
		addCaption(sheet, 9, 0, "Rend");
		sheet.setColumnView(9, 15);
		addCaption(sheet, 10, 0, "Sup Cat");
		sheet.setColumnView(10, 15);
		addCaption(sheet, 11, 0, "Sup A");
		sheet.setColumnView(11, 15);
		addCaption(sheet, 12, 0, "Sup B");
		sheet.setColumnView(12, 15);
		addCaption(sheet, 13, 0, "Sup C");
		sheet.setColumnView(13, 15);
		addCaption(sheet, 14, 0, "Sup D");
		sheet.setColumnView(14, 15);
		addCaption(sheet, 15, 0, "Sup E");
		sheet.setColumnView(15, 15);
		addCaption(sheet, 16, 0, "Sup F");
		sheet.setColumnView(16, 15);
		addCaption(sheet, 17, 0, "Sup G");
		sheet.setColumnView(17, 15);
		addCaption(sheet, 18, 0, "Sup H");
		sheet.setColumnView(18, 15);
		addCaption(sheet, 19, 0, "St Catasto");
		sheet.setColumnView(19, 15);
		addCaption(sheet, 20, 0, "Sup Cat Tarsu");
		sheet.setColumnView(20, 15);
		addCaption(sheet, 21, 0, "Sup Cat Tarsu 80");
		sheet.setColumnView(21, 15);
		addCaption(sheet, 22, 0, "Sup Tarsu");
		sheet.setColumnView(22, 15);
		addCaption(sheet, 23, 0, "Delta Dich 80");
		sheet.setColumnView(23, 15);
		addCaption(sheet, 24, 0, "Sup Cat Dpr 138-98");
		sheet.setColumnView(24, 15);
		addCaption(sheet, 25, 0, "Delta Sup Cat Calc");
		sheet.setColumnView(25, 15);
		addCaption(sheet, 26, 0, "Sup Cat Tarsu Calc");
		sheet.setColumnView(26, 15);
		addCaption(sheet, 27, 0, "Delta Sup Cat Tarsu Calc");
		sheet.setColumnView(27, 15);
		addCaption(sheet, 28, 0, "Vano");
		sheet.setColumnView(28, 15);
		
//		if(addSoggetti){
//			concatLabelSoggetti(sheet, 19);
//		}

	}//-------------------------------------------------------------------------
	
	public void createTaresDiaLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font
		WritableFont times10ptBold = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		timesBold = new WritableCellFormat(times10ptBold);
		// Lets automatically wrap the cells
		timesBold.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBold);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 0, 0, "Descrizione");
		sheet.setColumnView(0, 40);
		addCaption(sheet, 1, 0, "Valore");
		sheet.setColumnView(1, 20);

	}//-------------------------------------------------------------------------
	
	public void createTaresDiaContent(WritableSheet sheet) throws WriteException, RowsExceededException {

		try {
			/*
			 * La prima riga è riservata alle etichette delle colonne
			 */

			for (int index=0; index<listaDia.size(); index++) {
				addRowReportTaresDia(sheet, listaDia.get(index), rowStart+index);
			}
		
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}//-------------------------------------------------------------------------
	
	public void createTaresStatContent(WritableSheet sheet) throws WriteException, RowsExceededException {

		try {
			/*
			 * La prima riga è riservata alle etichette delle colonne
			 */

			for (int index=0; index<listaStat.size(); index++) {
				addRowReportTaresStat(sheet, listaStat.get(index), rowStart+index);
			}
		
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}//-------------------------------------------------------------------------
	
	public void createTaresTariUDContent(WritableSheet sheet) throws WriteException, RowsExceededException {

		try {
			/*
			 * La prima riga è riservata alle etichette delle colonne
			 */

			for (int index=0; index<listaTariUD.size(); index++) {
				addRowReportTaresTariUD(sheet, listaTariUD.get(index), rowStart+index);
			}
		
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}//-------------------------------------------------------------------------
	
	public void createTaresTariUNDContent(WritableSheet sheet) throws WriteException, RowsExceededException {

		try {
			/*
			 * La prima riga è riservata alle etichette delle colonne
			 */
			for (int index=0; index<listaTariUND.size(); index++) {
				addRowReportTaresTariUND(sheet, listaTariUND.get(index), rowStart+index);
			}
		
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}//-------------------------------------------------------------------------
	
	public void createTaresContent(WritableSheet sheet) throws WriteException, RowsExceededException {

		try {
			/*
			 * La prima riga è riservata alle etichette delle colonne
			 */
			
			for (int index=0; index<lista.size(); index++) {
				
				addRowReportTares(sheet, lista.get(index), rowStart+index);
//				++row;
			}
			
//			if(lista.size() >= 1000)
//				addCaption(sheet, 0, row + 2, "RISULTATI LIMITATI A 1000 RECORD");
		
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}//-------------------------------------------------------------------------
	
	private String roundBigDecimal(BigDecimal bd, int decimalPlaces){
		Double d = 0d;
		if (bd != null){
			bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_EVEN);
			d = bd.doubleValue();	
		}
		return d.toString();
	}//-------------------------------------------------------------------------
	
	private int addRowReportTaresTariUD(WritableSheet sheet, SetarTariffeElabUteDom rep, int row ) throws WriteException, RowsExceededException{

		addLabel(sheet, 0, row, rep.getVoce().getDescrizione());
		addLabel(sheet, 1, row, roundBigDecimal(rep.getNumEntita(), 2) );
		addLabel(sheet, 2, row, roundBigDecimal(rep.getSupTot(), 2));
		addLabel(sheet, 3, row, roundBigDecimal(rep.getQuoteFam(), 2));
		addLabel(sheet, 4, row, roundBigDecimal(rep.getSuperfMedAbit(), 2));
		addLabel(sheet, 5, row, roundBigDecimal(rep.getCoeffKa(), 2));
		addLabel(sheet, 6, row, roundBigDecimal(rep.getCoeffKb(), 2));
		addLabel(sheet, 7, row, roundBigDecimal(rep.getQufPerKa(), 6));
		addLabel(sheet, 8, row, roundBigDecimal(rep.getQuotaFissaMedia(), 2));
		addLabel(sheet, 9, row, roundBigDecimal(rep.getQuvPerKb(), 2));
		addLabel(sheet, 10, row, roundBigDecimal(rep.getQuotaVariabilePerPersona(), 2) );
		addLabel(sheet, 11, row, roundBigDecimal(rep.getTariffaMed(), 2));
		addLabel(sheet, 12, row, roundBigDecimal(rep.getGettitoTot(), 2));
		
		return 0;
	}//-------------------------------------------------------------------------
	
	private int addRowReportTaresTariUND(WritableSheet sheet, SetarTariffeElabUteNonDom rep, int row ) throws WriteException, RowsExceededException{

		addLabel(sheet, 0, row, rep.getVoce().getDescrizione());
		addLabel(sheet, 1, row, roundBigDecimal(rep.getNumEntita(), 2 ));
		addLabel(sheet, 2, row, roundBigDecimal(rep.getSupTot(), 2 ));
		addLabel(sheet, 3, row, roundBigDecimal(rep.getSuperfMedLoc(), 2 ));
		addLabel(sheet, 4, row, rep.getSelKc() );
		addLabel(sheet, 5, row, roundBigDecimal(rep.getCoeffKc(), 2 ));
		addLabel(sheet, 6, row, rep.getSelKd() );
		addLabel(sheet, 7, row, roundBigDecimal(rep.getCoeffKd(), 2 ));
		addLabel(sheet, 8, row, roundBigDecimal(rep.getQufPerKc(), 6 ));
		addLabel(sheet, 9, row, roundBigDecimal(rep.getQuvPerKd(), 6 ));
		addLabel(sheet, 10, row, roundBigDecimal(rep.getTariffaTot(), 6 ));
		addLabel(sheet, 11, row, roundBigDecimal(rep.getGettitoTot(), 2 ));
		
		return 0;
	}//-------------------------------------------------------------------------
	
	private int addRowReportTaresStat(WritableSheet sheet, SetarStat rep, int row ) throws WriteException, RowsExceededException{

		addLabel(sheet, 0, row, rep.getCodiFiscLuna());
		addLabel(sheet, 1, row, rep.getCategoria());
		addLabel(sheet, 2, row, rep.getNumero()!=null?rep.getNumero().toString():"");
		addLabel(sheet, 3, row, rep.getSumConsistenza()!=null?rep.getSumConsistenza().toString():"");
		addLabel(sheet, 4, row, rep.getSumSupCatTarsu()!=null?rep.getSumSupCatTarsu().toString():"");
		addLabel(sheet, 5, row, rep.getSumSupCatTarsu80()!=null?rep.getSumSupCatTarsu80().toString():"");
		addLabel(sheet, 6, row, rep.getSup0()!=null?rep.getSup0().toString():"");
		addLabel(sheet, 7, row, rep.getSumConsSup0()!=null?rep.getSumConsSup0().toString():"");

		
		
		return 19;
	}//-------------------------------------------------------------------------
	
	private int addRowReportTaresDia(WritableSheet sheet, SetarDia rep, int row ) throws WriteException, RowsExceededException{

		addLabel(sheet, 0, row, rep.getDescrizione()!=null?rep.getDescrizione():"");
		addLabel(sheet, 1, row, rep.getValore()!=null?rep.getValore():"");
		
		return 19;
	}//-------------------------------------------------------------------------
	
	private int addRowReportTares(WritableSheet sheet, SetarElab rep, int row ) throws WriteException, RowsExceededException{
		
		addLabel(sheet, 0, row, rep.getCodiFiscLuna());
		addLabel(sheet, 1, row, rep.getIdSezc());
		addLabel(sheet, 2, row, rep.getId().getFoglio()!=null?rep.getId().getFoglio().toString():"");
		addLabel(sheet, 3, row, rep.getId().getParticella());
		addLabel(sheet, 4, row, rep.getSub());
		addLabel(sheet, 5, row, rep.getId().getUnimm()!=null?rep.getId().getUnimm().toString():"");
		addLabel(sheet, 6, row, rep.getCategoria());
		addLabel(sheet, 7, row, rep.getClasse());
		addLabel(sheet, 8, row, rep.getConsistenza()!=null?rep.getConsistenza().toString():"");
		addLabel(sheet, 9, row, rep.getRendita()!=null?rep.getRendita().toString():"");
		addLabel(sheet, 10, row, rep.getSupCat()!=null?rep.getSupCat().toString():"");
		addLabel(sheet, 11, row, rep.getSupA()!=null?rep.getSupA().toString():"");
		addLabel(sheet, 12, row, rep.getSupB()!=null?rep.getSupB().toString():"");
		addLabel(sheet, 13, row, rep.getSupC()!=null?rep.getSupC().toString():"");
		addLabel(sheet, 14, row, rep.getSupD()!=null?rep.getSupD().toString():"");
		addLabel(sheet, 15, row, rep.getSupE()!=null?rep.getSupE().toString():"");
		addLabel(sheet, 16, row, rep.getSupF()!=null?rep.getSupF().toString():"");
		addLabel(sheet, 17, row, rep.getSupG()!=null?rep.getSupG().toString():"");
		addLabel(sheet, 18, row, rep.getSupH()!=null?rep.getSupH().toString():"");
		addLabel(sheet, 19, row, rep.getStatoCatasto()!=null?rep.getStatoCatasto().toString():"");
		addLabel(sheet, 20, row, rep.getSupCatTarsu()!=null?rep.getSupCatTarsu().toString():"");
		addLabel(sheet, 21, row, rep.getSupCatTarsu80()!=null?rep.getSupCatTarsu80().toString():"");
		addLabel(sheet, 22, row, rep.getSupTarsu()!=null?rep.getSupTarsu().toString():"");
		addLabel(sheet, 23, row, rep.getDeltaDich80()!=null?rep.getDeltaDich80().toString():"");
		addLabel(sheet, 24, row, rep.getSupCatDpr13898()!=null?rep.getSupCatDpr13898().toString():"");
		addLabel(sheet, 25, row, rep.getDeltaSupCatCalc()!=null?rep.getDeltaSupCatCalc().toString():"");
		addLabel(sheet, 26, row, rep.getSupCatTarsuCalc()!=null?rep.getSupCatTarsuCalc().toString():"");
		addLabel(sheet, 27, row, rep.getDeltaSupCatTarsuCalc()!=null?rep.getDeltaSupCatTarsuCalc().toString():"");
		addLabel(sheet, 28, row, rep.getVano()!=null?rep.getVano().toString():"");
		
		return 19;
	}//-------------------------------------------------------------------------

	private void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBold);
		sheet.addCell(label);
	}//-------------------------------------------------------------------------

	private void addNumber(WritableSheet sheet, int column, int row, Integer integer) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, times);
		sheet.addCell(number);
	}//-------------------------------------------------------------------------

	private void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}//-------------------------------------------------------------------------

	public void showXls(String filePath) throws IOException {
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
				t.printStackTrace();
			} finally {
				close(bos);
				close(bis);
				f.delete();
			}

			facesContext.responseComplete();
		} 
	}//-------------------------------------------------------------------------

	public static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}//-------------------------------------------------------------------------
	
	/*
	 * public static void main(String[] args) throws WriteException, IOException
	 * { WriteExcel test = new WriteExcel();
	 * test.setOutputFile("c:/temp/lars.xls"); test.write(); System.out
	 * .println("Please check the result file under c:/temp/lars.xls "); }
	 */

	public List<SetarElab> getLista() {
		return lista;
	}

	public void setLista(List<SetarElab> lista) {
		this.lista = lista;
	}

	public int getRowStart() {
		return rowStart;
	}

	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	public String getPathXLS() {
		return pathXLS;
	}

	public void setPathXLS(String pathXLS) {
		this.pathXLS = pathXLS;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public List<SetarTariffeElabUteDom> getListaTariUD() {
		return listaTariUD;
	}

	public void setListaTariUD(List<SetarTariffeElabUteDom> listaTariUD) {
		this.listaTariUD = listaTariUD;
	}

	public List<SetarTariffeElabUteNonDom> getListaTariUND() {
		return listaTariUND;
	}

	public void setListaTariUND(List<SetarTariffeElabUteNonDom> listaTariUND) {
		this.listaTariUND = listaTariUND;
	}

	public List<SetarStat> getListaStat() {
		return listaStat;
	}

	public void setListaStat(List<SetarStat> listaStat) {
		this.listaStat = listaStat;
	}
}
