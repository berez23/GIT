package it.webred.ct.service.tsSoggiorno.web.bean.report;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;
import it.webred.ct.service.tsSoggiorno.web.bean.util.UserBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.itextpdf.text.BaseColor;


public class RepRiepilogoBean extends ReportBaseBean {

	class Elem {
		int nDich;
		BigDecimal incassato;
		BigDecimal residuo;

		Elem(int n, BigDecimal i, BigDecimal r) {
			nDich = n;
			incassato = i;
			residuo = r;
		}
	}
	
	@PostConstruct
	public void init() {
				
		if (codFiscale == null) {
			UserBean user = (UserBean) getBeanReference("userBean");
			codFiscale = user.getUsername();
		}			
	}
	
	public void doCreateReport(){
		
		try {
			
			if(criteria.getIdPeriodoDa().longValue() == 0 || criteria.getIdPeriodoA().longValue() == 0){
				super.addWarningMessage("report.riepilogo.required");
				return;
			}
			
			//carico report data
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			criteria.setCodFiscale(codFiscale);
			dataIn.setObj(criteria);
			List<DichiarazioneDTO> listaDichDTO = getDichiarazioneService().getDichiarazioniByCriteria(dataIn);
			
			if(listaDichDTO==null || listaDichDTO.size()==0 || listaDichDTO.isEmpty())
			{
				super.addWarningMessage("report.riepilogo.nodic");
				return;
			}
			
			List<String> periodi = new ArrayList<String>();
			MultiKeyMap multiMap = new MultiKeyMap();
			//creo una mappa con i valori per ogni periodo/categoria
			for (DichiarazioneDTO dto : listaDichDTO) {
				if(!periodi.contains(dto.getPeriodo().getDescrizione()))
					periodi.add(dto.getPeriodo().getDescrizione());
				
				Elem e = (Elem) multiMap.get(dto.getPeriodo().getDescrizione(), dto.getStrutturaSnap().getFkIsClasse());
				if(e == null)
					e = new Elem(1, dto.getDichiarazione().getTotIncassato(), dto.getDichiarazione().getTotResiduo());
				else {
					if(dto.getDichiarazione().getIntegrativaInt() == 0)
						e.nDich += 1;
					e.incassato = e.incassato.add(dto.getDichiarazione().getTotIncassato());
					e.residuo = e.residuo.add(dto.getDichiarazione().getTotResiduo());
					multiMap.remove(dto.getPeriodo().getDescrizione(), dto.getStrutturaSnap().getFkIsClasse());
				}
				multiMap.put(dto.getPeriodo().getDescrizione(), dto.getStrutturaSnap().getFkIsClasse(), e);
			}
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Report");
			
			//creazione stili
			HSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle boldStyle = workbook.createCellStyle();
			boldStyle.setFont(font);
			
			//prima riga classi
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
            cell.setCellValue("");
            int i = 0;
            for(SelectItem classe: getListaClassi()){
            	Cell cell2 = row.createCell(++i);
            	cell2.setCellValue(classe.getLabel());
            	Cell cell3 = row.createCell(++i);
            	cell3.setCellValue("");
            } 
            Cell cell2 = row.createCell(++i);
            cell2.setCellValue("Totali");
            cell2.setCellStyle(boldStyle);
            Cell cell3 = row.createCell(++i);
            cell3.setCellValue("");
            cell3.setCellStyle(boldStyle);
            
            //seconda riga header
			Row row2 = sheet.createRow(1);
			Cell cell4 = row2.createCell(0);
            cell4.setCellValue("");
            i = 0;
            for(SelectItem classe: getListaClassi()){
            	
            	Cell cell5 = row2.createCell(++i);
            	cell5.setCellValue("Num. Dichiarazioni");
            	sheet.autoSizeColumn(i);
            	Cell cell6 = row2.createCell(++i);
            	cell6.setCellValue("Incassato/Residuo");
            	sheet.autoSizeColumn(i);
            	
            	//merging cells
                sheet.addMergedRegion(new CellRangeAddress(
                        0, //first row
                        0, //last row
                        i-1, //first column
                        i  //last column
                ));
            } 
            Cell cell5 = row2.createCell(++i);
            cell5.setCellValue("N.Dichiarazioni");
            cell5.setCellStyle(boldStyle);
            sheet.autoSizeColumn(i);
            Cell cell6 = row2.createCell(++i);
            cell6.setCellValue("Incassato/Residuo");
            cell6.setCellStyle(boldStyle);
            sheet.autoSizeColumn(i);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i-1, i));
            
    		int riga = 1;
    		BigDecimal totTotRes = new BigDecimal(0);
    		//stampo le celle per periodo/categoria
    		for(String periodo: periodi){
    			
        		int col = 0;
    			Row r = sheet.createRow(++riga);
    			Cell c = r.createCell(col);
                c.setCellValue(periodo);
    			
    			int totDich = 0;
    			BigDecimal totInc = new BigDecimal(0);
    			BigDecimal totRes = new BigDecimal(0);
    			
    			for(SelectItem classe: getListaClassi()){
    				Elem e = (Elem) multiMap.get(periodo, classe.getValue());
    				
        			Cell c2 = r.createCell(++col);
                    c2.setCellValue(e != null? String.valueOf(e.nDich) : "0");
                    Cell c3 = r.createCell(++col);
                    String cellValue = (e != null? e.incassato.toString() : "0") + " / " + (e != null? e.residuo.toString() : "0");
                    c3.setCellValue(cellValue);
    				
    				if(e != null){
    					totDich += e.nDich;
    					totInc = totInc.add(e.incassato);
    					totRes = totRes.add(e.residuo);
    				}
    			}
    			
    			//totale per periodo
    			Cell c2 = r.createCell(++col);
                c2.setCellValue(String.valueOf(totDich));
                c2.setCellStyle(boldStyle);
                Cell c3 = r.createCell(++col);
                c3.setCellValue(totInc.toString() + " / " + totRes.toString());
                c3.setCellStyle(boldStyle);
                
                if(riga == 2)
                	totTotRes = totRes;
    		}
    		
    		//totale per classe
    		int col = 0;
            int totTotDich = 0;
    		BigDecimal totTotInc = new BigDecimal(0);
    		Row r = sheet.createRow(++riga);
			Cell c = r.createCell(0);
            c.setCellValue("Totale");
            c.setCellStyle(boldStyle);
    		for(SelectItem classe: getListaClassi()){
    			
    			int totDich = 0;
    			BigDecimal totInc = new BigDecimal(0);
    			
    			for(String periodo: periodi){
    				Elem e = (Elem) multiMap.get(periodo, classe.getValue());
    				
    				if(e != null){
    					totDich += e.nDich;
    					totInc = totInc.add(e.incassato);
    				}
    			}
    			
				totTotDich += totDich;
				totTotInc = totTotInc.add(totInc);
    			
    			Cell c2 = r.createCell(++col);
                c2.setCellValue(String.valueOf(totDich));
                c2.setCellStyle(boldStyle);
                Cell c3 = r.createCell(++col);
                Elem e = (Elem) multiMap.get(periodi.get(0), classe.getValue());
                String cellValue = totInc.toString() + " / " + (e != null? e.residuo.toString() : "0");
                c3.setCellValue(cellValue);
                c3.setCellStyle(boldStyle);
    			
    		}
    		
    		//totale del totale
    		Cell c2 = r.createCell(++col);
            c2.setCellValue(String.valueOf(totTotDich));
            c2.setCellStyle(boldStyle);
            Cell c3 = r.createCell(++col);
            String cellValue = totTotInc.toString() + " / " + totTotRes.toString();
            c3.setCellValue(cellValue);
            c3.setCellStyle(boldStyle);
            
    		sheet.autoSizeColumn(0);
			exportXLSReport(workbook, "ReportPeriodo");
			
		} catch (Exception e) {
			super.addErrorMessage("elaborazione.error", e.getMessage());
			getLogger().error("Eccezione: " + e.getMessage(), e);
		}
				
	}

	private void loadReportData(){
		
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		criteria.setCodFiscale(codFiscale);
		dataIn.setObj(criteria);
		List<DichiarazioneDTO> listaDichDTO = getDichiarazioneService().getDichiarazioniByCriteria(dataIn);
		
		List<String> periodi = new ArrayList<String>();
		MultiKeyMap multiMap = new MultiKeyMap();
		//creo una mappa con i valori per ogni periodo/categoria
		for (DichiarazioneDTO dto : listaDichDTO) {
			if(!periodi.contains(dto.getPeriodo().getDescrizione()))
				periodi.add(dto.getPeriodo().getDescrizione());
			
			BigDecimal sosp = dto.getImpDovuta().getImporto().subtract(dto.getImpIncassata().getImporto());
			BigDecimal sospPrec = dto.getImpDovuta().getImportoMesiPrec().subtract(dto.getImpIncassata().getImportoMesiPrec());
			BigDecimal residuo = sosp.add(sospPrec);
			BigDecimal incassato = dto.getImpIncassata().getImporto().add(dto.getImpIncassata().getImportoMesiPrec());
			
			Elem e = (Elem) multiMap.get(dto.getPeriodo().getDescrizione(), dto.getStrutturaSnap().getFkIsClasse());
			if(e == null)
				e = new Elem(1, incassato, residuo);
			else {
				//TODO se è integrativa?
				e.nDich += 1;
				e.incassato = e.incassato.add(incassato);
				e.residuo = e.residuo.add(residuo);
				multiMap.remove(dto.getPeriodo().getDescrizione(), dto.getStrutturaSnap().getFkIsClasse());
			}
			multiMap.put(dto.getPeriodo().getDescrizione(), dto.getStrutturaSnap().getFkIsClasse(), e);
		}
		
	}
	
}
