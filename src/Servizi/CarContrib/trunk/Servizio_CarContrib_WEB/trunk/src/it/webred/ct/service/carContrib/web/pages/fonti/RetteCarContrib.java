package it.webred.ct.service.carContrib.web.pages.fonti;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import it.webred.ct.data.access.basic.acqua.AcquaDataIn;
import it.webred.ct.data.access.basic.acqua.AcquaService;
import it.webred.ct.data.access.basic.acqua.dto.AcquaUtenzeDTO;
import it.webred.ct.data.access.basic.rette.RetteDataIn;
import it.webred.ct.data.access.basic.rette.RetteService;
import it.webred.ct.data.access.basic.rette.dto.RetteDTO;
import it.webred.ct.data.access.basic.traffico.TrafficoDataIn;
import it.webred.ct.data.access.basic.traffico.TrafficoService;
import it.webred.ct.data.model.acqua.SitAcquaCatasto;
import it.webred.ct.data.model.acqua.SitAcquaUtente;
import it.webred.ct.data.model.rette.SitRttBollette;
import it.webred.ct.data.model.rette.SitRttDettBollette;
import it.webred.ct.data.model.rette.SitRttRateBollette;
import it.webred.ct.data.model.traffico.SitTrffMulte;
import it.webred.ct.service.carContrib.web.Utility;
import it.webred.ct.service.carContrib.web.utils.FonteDTO;

public class RetteCarContrib extends FonteBaseCarContrib {

	protected Logger logger = Logger.getLogger("carcontrib.log");
	
	private RetteService retteService;
	private List<RetteDTO> listaBolletteNonPag;
	private List<RetteDTO> listaBollettePag;
	
	
	
	// CARICA I DATI TAB RETTE
	public void LoadTabRETTE(String ente, String username, String codiceFiscale, String nome, String cognome){
		
		try{
			listaBolletteNonPag = new ArrayList<RetteDTO>();
			listaBollettePag = new ArrayList<RetteDTO>();
			RetteDataIn dataIn = new RetteDataIn();
			dataIn.setEnteId(ente);
			dataIn.setUserId(username);
			dataIn.setObj(codiceFiscale);
			if(retteService == null){
				Context ctx = new InitialContext();
				retteService = (RetteService) Utility.getEjb("CT_Service", "CT_Service_Data_Access", "RetteServiceBean");
			}
			List<SitRttBollette> listaNonPag = retteService.getListaBolletteNonPagateByCodFis(dataIn);
			List<SitRttBollette> listaPag = retteService.getListaBollettePagateByCodFis(dataIn);
			
			for(SitRttBollette boll: listaNonPag){
				dataIn.setObj2(boll.getCodBolletta());
				RetteDTO dto = new RetteDTO();
				dto.setSitRttBollette(boll);
				dto.setListaSitRttDettBollette(retteService.getListaDettagliBollettaByCodBoll(dataIn));
				dto.setListaSitRttRateBollette(retteService.getListaRateBollettaByCodBoll(dataIn));
				listaBolletteNonPag.add(dto);
			}
			
			for(SitRttBollette boll: listaPag){
				dataIn.setObj2(boll.getCodBolletta());
				RetteDTO dto = new RetteDTO();
				dto.setSitRttBollette(boll);
				dto.setListaSitRttDettBollette(retteService.getListaDettagliBollettaByCodBoll(dataIn));
				dto.setListaSitRttRateBollette(retteService.getListaRateBollettaByCodBoll(dataIn));
				listaBollettePag.add(dto);
			}
			
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
		
	}

	//GENERA SEZIONE PDF
	public void addSezionePdf(Document document, boolean cbx, String motivo, FonteDTO fonte) throws DocumentException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.ITALY);
		Paragraph paragrafo = new Paragraph();
		
		paragrafo.add(this.insertTitoloScheda("SCHEDA RETTE SCOLASTICHE"));
	
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.NORMAL);
		
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		if (!cbx) {
			paragrafo.add(new Phrase(motivo,normalSmallFont));
			document.add(paragrafo);
			document.newPage();
			return;
		}
		
		if(fonte.isAbilitataCC()){
			paragrafo.add(fonte.getStrDataAgg());
			this.addEmptyLine(paragrafo, 1);
			paragrafo.add(new Phrase(fonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafo, 2);
			
			if(listaBolletteNonPag != null && listaBolletteNonPag.size()>0){
				paragrafo.add(new Paragraph("Dati bollette non pagate", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable table = new PdfPTable(1);
				table.setWidths(new float[] {100});
				table.setWidthPercentage(100);
				table.getDefaultCell().setMinimumHeight(20);
				
				for (RetteDTO dto: listaBolletteNonPag)
				{
					String dati = "Cod.: " + dto.getSitRttBollette().getCodBolletta() +
							" Data: " + sdf.format(dto.getSitRttBollette().getDataBolletta()) +
							" Recapito: " + dto.getSitRttBollette().getRecapito() +
							" Oggetto: " + dto.getSitRttBollette().getOggetto();
					
					this.addCella(table, normalFont, Element.ALIGN_LEFT, dati);
					
					if(dto.getListaSitRttRateBollette() != null && dto.getListaSitRttRateBollette().size() > 0){
						PdfPTable tableCat = new PdfPTable(4);
						tableCat.setWidths(new float[] {25,25,25,25});
						tableCat.setWidthPercentage(100);
						tableCat.getDefaultCell().setMinimumHeight(20);
						
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "IMPORTO");
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "IMPORTO PAGATO");
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "DATA SCAD.");
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "NOTE");
									
						for (SitRttRateBollette rata: dto.getListaSitRttRateBollette())
						{
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, n.format(rata.getImportoRata().doubleValue()));
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, rata.getImportoPagato()!=null ? n.format(rata.getImportoPagato().doubleValue()):"");
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, rata.getDtScadenzaRata()!=null ? sdf.format(rata.getDtScadenzaRata()):"");
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, rata.getNote()!=null ? rata.getNote():"");
						}
						table.addCell(tableCat);
					}
					
					if(dto.getListaSitRttDettBollette() != null && dto.getListaSitRttDettBollette().size() > 0){
						PdfPTable tableCat = new PdfPTable(2);
						tableCat.setWidths(new float[] {60,40});
						tableCat.setWidthPercentage(100);
						tableCat.getDefaultCell().setMinimumHeight(20);
						
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "DESCRIZIONE");
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "VALORE");
						
						for (SitRttDettBollette dett: dto.getListaSitRttDettBollette())
						{
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, dett.getDesVoce()!=null?dett.getDesVoce():"");
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, dett.getValore()!=null?dett.getValore():"");
						}
						table.addCell(tableCat);
					}
				}
				paragrafo.add(table);
				this.addEmptyLine(paragrafo, 2);
			}else{
				paragrafo.add(new Paragraph("Nessuna bolletta non pagata da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafo, 2);
			}
			
			if(listaBollettePag != null && listaBollettePag.size()>0){
				paragrafo.add(new Paragraph("Dati bollette pagate", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable table = new PdfPTable(1);
				table.setWidths(new float[] {100});
				table.setWidthPercentage(100);
				table.getDefaultCell().setMinimumHeight(20);
				
				for (RetteDTO dto: listaBollettePag)
				{
					String dati = "Cod.: " + dto.getSitRttBollette().getCodBolletta() +
							" Data: " + sdf.format(dto.getSitRttBollette().getDataBolletta()) +
							" Recapito: " + dto.getSitRttBollette().getRecapito() +
							" Oggetto: " + dto.getSitRttBollette().getOggetto();
					
					this.addCella(table, normalFont, Element.ALIGN_LEFT, dati);
					
					if(dto.getListaSitRttRateBollette() != null && dto.getListaSitRttRateBollette().size() > 0){
						PdfPTable tableCat = new PdfPTable(4);
						tableCat.setWidths(new float[] {20,20,20,40});
						tableCat.setWidthPercentage(100);
						tableCat.getDefaultCell().setMinimumHeight(20);
						
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "IMPORTO");
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "IMPORTO PAGATO");
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "DATA SCAD.");
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "NOTE");
						
						for (SitRttRateBollette rata: dto.getListaSitRttRateBollette())
						{
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, n.format(rata.getImportoRata().doubleValue()));
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, rata.getImportoPagato()!=null ? n.format(rata.getImportoPagato().doubleValue()) : "");
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, rata.getDtScadenzaRata()!=null ? sdf.format(rata.getDtScadenzaRata()):"");
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, rata.getNote()!=null ? rata.getNote():"");
						}
						table.addCell(new Paragraph("Rate", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
						table.addCell(tableCat);
					}
					
					if(dto.getListaSitRttDettBollette() != null && dto.getListaSitRttDettBollette().size() > 0){
						PdfPTable tableCat = new PdfPTable(2);
						tableCat.setWidths(new float[] {50,50});
						tableCat.setWidthPercentage(100);
						tableCat.getDefaultCell().setMinimumHeight(20);
						
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "DESCRIZIONE");
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "VALORE");
						
						for (SitRttDettBollette dett: dto.getListaSitRttDettBollette())
						{
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, dett.getDesVoce());
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, dett.getValore());
						}
						table.addCell(new Paragraph("Dettaglio", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
						table.addCell(tableCat);
					}
				}
				paragrafo.add(table);
			}else
			{
				paragrafo.add(new Paragraph("Nessuna bolletta pagata da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafo, 2);
			}
		}else
			super.addMotivoFonteDisabilitataCC(paragrafo, fonte.getDescrizione());
		
		document.add(paragrafo);
		document.newPage();
	}
	
	public RetteService getRetteService() {
		return retteService;
	}
	public void setRetteService(RetteService retteService) {
		this.retteService = retteService;
	}
	public List<RetteDTO> getListaBolletteNonPag() {
		return listaBolletteNonPag;
	}
	public void setListaBolletteNonPag(List<RetteDTO> listaBolletteNonPag) {
		this.listaBolletteNonPag = listaBolletteNonPag;
	}
	public List<RetteDTO> getListaBollettePag() {
		return listaBollettePag;
	}
	public void setListaBollettePag(List<RetteDTO> listaBollettePag) {
		this.listaBollettePag = listaBollettePag;
	}

	@Override
	public void ClearCampiTAB() {
		listaBolletteNonPag = new ArrayList<RetteDTO>();
		listaBollettePag = new ArrayList<RetteDTO>();
	}
}
