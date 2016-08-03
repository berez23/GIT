package it.webred.ct.service.tsSoggiorno.web.bean.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.access.AnagraficaService;
import it.webred.ct.service.tsSoggiorno.data.access.MavService;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.SocietaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.VersamentoDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.web.bean.util.UserBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class RepVersamentiBean extends ReportBaseBean {

	@EJB(mappedName = "Servizio_TsSoggiorno/MavServiceBean/remote")
	protected MavService mavService;
	
	@PostConstruct
	public void init() {
				
		if (codFiscale == null) {
			UserBean user = (UserBean) getBeanReference("userBean");
			codFiscale = user.getUsername();
		}			
	}
	
	public void doCreateReport(){
		
		try {
			
			if((criteria.getIdPeriodoDa().longValue() == 0 || criteria.getIdPeriodoA().longValue() == 0)
					&& (criteria.getCodFiscale() == null || "".equals(criteria.getCodFiscale().trim()))){
				super.addWarningMessage("report.versamenti.required");
				return;
			}
			
			List<SocietaDTO> listaSocieta = new ArrayList<SocietaDTO>();
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			if(criteria.getCodFiscale() != null && !"".equals(criteria.getCodFiscale())){
				dataIn.setCodFiscale(criteria.getCodFiscale());
				listaSocieta = getAnagraficaService().getSocietaByCfPi(dataIn);
			}else{
				dataIn.setCodFiscale(codFiscale);
				listaSocieta = getAnagraficaService().getSocietaByCodFis(dataIn);
			}
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Report");
			
			//creazione stili
			HSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle boldStyle = workbook.createCellStyle();
			boldStyle.setFont(font);
			
			//header
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
            cell.setCellValue("Società                                         ");
            cell.setCellStyle(boldStyle);
            Cell cell2 = row.createCell(1);
            cell2.setCellValue("Struttura                               ");
            cell2.setCellStyle(boldStyle);
            Cell cell3 = row.createCell(2);
            cell3.setCellValue("Data contabile");
            cell3.setCellStyle(boldStyle);
            Cell cell4 = row.createCell(3);
            cell4.setCellValue("Importo");
            cell4.setCellStyle(boldStyle);
            Cell cell5 = row.createCell(4);
            cell5.setCellValue("Commissioni");
            cell5.setCellStyle(boldStyle);
            Cell cell6 = row.createCell(5);
            cell6.setCellValue("Motivo");
            cell6.setCellStyle(boldStyle);
			
            int riga = 0;
            int startStr = 1;
            int startSoc = 1;
            String lastStr = "";
            String lastSoc = "";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			for(SocietaDTO societa: listaSocieta){
				
				List<VersamentoDTO> listaVersamenti = new ArrayList<VersamentoDTO>();

				if(societa.getSocieta().getCf() !=null && societa.getSocieta().getCf().length()>0)
					dataIn.setCodFiscale(societa.getSocieta().getCf());
				else
					dataIn.setCodFiscale(societa.getSocieta().getPi());
				if(criteria.getIdPeriodoDa().longValue() != 0 && criteria.getIdPeriodoA().longValue() != 0){
					dataIn.setIdPeriodoDa(criteria.getIdPeriodoDa());
					dataIn.setIdPeriodoA(criteria.getIdPeriodoA());
					listaVersamenti = mavService.getListaVersamentiMavPeriodo(dataIn);
				}else listaVersamenti = mavService.getListaVersamentiMav(dataIn);
				
				for(VersamentoDTO versamento: listaVersamenti){
					
					if(riga == 0){
						lastStr = versamento.getStruttura();
						lastSoc = versamento.getSocieta();
					}
					
					Row r = sheet.createRow(++riga);
					Cell c = r.createCell(0);
		            c.setCellValue(versamento.getSocieta());
		            
		            Cell c2 = r.createCell(1);
		            c2.setCellValue(versamento.getStruttura());
		            
		            Cell c3 = r.createCell(2);
		            c3.setCellValue(sdf.format(versamento.getDataContabile()));
					
		            Cell c4 = r.createCell(3);
		            c4.setCellValue(versamento.getFormatImporto());
		            
		            Cell c5 = r.createCell(4);
		            c5.setCellValue(versamento.getFormatImportoSpeseComm());
		            
		            Cell c6 = r.createCell(5);
		            c6.setCellValue(versamento.getMotivo());
		            
		            //eseguo merge struttura e societa
		            if(!lastStr.equals(versamento.getStruttura())){
		            	sheet.addMergedRegion(new CellRangeAddress(startStr, riga-1, 1, 1));
		            	startStr = riga;
		            	lastStr = versamento.getStruttura();
		            }
		            if(!lastSoc.equals(versamento.getSocieta())){
		            	sheet.addMergedRegion(new CellRangeAddress(startSoc, riga-1, 0, 0));
		            	startSoc = riga;
		            	lastSoc = versamento.getSocieta();
		            }
		            	
				}
			}
			if(riga > 1){
				sheet.addMergedRegion(new CellRangeAddress(startStr, riga, 1, 1));
        		sheet.addMergedRegion(new CellRangeAddress(startSoc, riga, 0, 0));
			}
			
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			exportXLSReport(workbook, "ReportVersamenti");
			
		} catch (Exception e) {
			super.addErrorMessage("elaborazione.error", e.getMessage());
			getLogger().error("Eccezione: " + e.getMessage(), e);
		}
				
	}

}
