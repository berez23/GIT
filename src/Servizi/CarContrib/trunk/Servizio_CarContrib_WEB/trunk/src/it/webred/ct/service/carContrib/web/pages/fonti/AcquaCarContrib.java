package it.webred.ct.service.carContrib.web.pages.fonti;

import it.webred.ct.data.access.basic.acqua.AcquaDataIn;
import it.webred.ct.data.access.basic.acqua.AcquaService;
import it.webred.ct.data.access.basic.acqua.dto.AcquaUtenzeDTO;
import it.webred.ct.data.model.acqua.SitAcquaCatasto;
import it.webred.ct.data.model.acqua.SitAcquaUtente;
import it.webred.ct.service.carContrib.web.utils.FonteDTO;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import it.webred.ct.service.carContrib.web.Utility;


public class AcquaCarContrib extends FonteBaseCarContrib{

	private AcquaService acquaService;
	public List<AcquaUtenzeDTO> listaUtenzeAcqua;
	public SitAcquaUtente utenteAcqua;
	


	// CARICA I DATI TAB ACQUA
	public void LoadTabACQUA(String ente, String username, String codiceFiscale){
		
		try{
			AcquaDataIn dataIn = new AcquaDataIn();
			dataIn.setEnteId(ente);
			dataIn.setUserId(username);
			dataIn.setCodFiscale(codiceFiscale);
			if(acquaService == null){
				Context ctx = new InitialContext();
				acquaService = (AcquaService) Utility.getEjb("CT_Service", "CT_Service_Data_Access", "AcquaServiceBean");
			}
			List<SitAcquaUtente> listaUtente = acquaService.getListaUtenteByCodFisPIva(dataIn);
			if(listaUtente != null && listaUtente.size()>0)
				utenteAcqua = listaUtente.get(0);
			listaUtenzeAcqua = acquaService.getListaUtenzeByCodFisPIva(dataIn);
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
		
	}
	
	
	//GENERA SEZIONE PDF
	public void addSezionePdf(Document document, boolean cbx, String motivo, FonteDTO fonte) throws DocumentException{
		
		Paragraph paragrafo = new Paragraph();
		
		paragrafo.add(this.insertTitoloScheda("SCHEDA FORNITURE IDRICHE"));
	
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
			
			if(utenteAcqua != null){
				paragrafo.add(new Phrase("Dati anagrafici dichiarati",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				// Sezione Dati Anagrafici dichiarati
				String denominazione = utenteAcqua.getCognome() == null? utenteAcqua.getDenominazione():utenteAcqua.getCognome() + " " + utenteAcqua.getNome() == null? "":utenteAcqua.getNome();
				String indirizzo = utenteAcqua.getViaResidenza() + " " + utenteAcqua.getCivicoResidenza();
				String cf_pi_label = utenteAcqua.getCodFiscale() == null?"PARTITA IVA":"CODICE FISCALE";
				String cf_pi = utenteAcqua.getCodFiscale() == null?utenteAcqua.getPartIva():utenteAcqua.getCodFiscale();
				String comune = utenteAcqua.getComuneResidenza();
				
				PdfPTable tableDatiSoggetto = new PdfPTable(4);
				tableDatiSoggetto.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.setWidthPercentage(100);
				tableDatiSoggetto.setWidths(new float[] {20,30,20,30});
				tableDatiSoggetto.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				tableDatiSoggetto.getDefaultCell().setMinimumHeight(30);
				
				PdfPCell myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase("DENOMINAZIONE",boldFont));
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
				
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase(denominazione,normalFont));
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
				
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase("INDIRIZZO",boldFont));					
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
		
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase(indirizzo,normalFont));
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
				
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase(cf_pi_label,boldFont));					
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
		
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase(cf_pi,normalFont));
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
				
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase("COMUNE RESIDENZA",boldFont));					
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
		
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase(comune,normalFont));
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);			
				paragrafo.add(tableDatiSoggetto);
				
				this.addEmptyLine(paragrafo, 2);
			}
			
			if(listaUtenzeAcqua != null && listaUtenzeAcqua.size()>0){
				paragrafo.add(new Paragraph("Dati utenze", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable table = new PdfPTable(1);
				table.setWidths(new float[] {100});
				table.setWidthPercentage(100);
				table.getDefaultCell().setMinimumHeight(20);
				
				for (AcquaUtenzeDTO dto: listaUtenzeAcqua)
				{
					String dati = 
							"Cod.servizio: " + dto.getSitAcquaUtenze().getCodServizio().toString() +
							" Rag.sociale: " + dto.getSitAcquaUtenze().getRagSocUbicazione() +
							" Indirizzo: " + dto.getSitAcquaUtenze().getViaUbicazione() + " " + dto.getSitAcquaUtenze().getCivicoUbicazione() +
							" Consumo: " + dto.getConsumo();
					
					this.addCella(table, normalFont, Element.ALIGN_LEFT, dati);
					
					if(dto.getListaSitAcquaCatasto() != null && dto.getListaSitAcquaCatasto().size() > 0){
						PdfPTable tableCat = new PdfPTable(2);
						tableCat.setWidths(new float[] {50,50});
						tableCat.setWidthPercentage(100);
						tableCat.getDefaultCell().setMinimumHeight(20);
						
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "FOGLIO");
						this.addCellaIntestazione(tableCat, boldFont, Element.ALIGN_CENTER, "PARTICELLA");
						
						for (SitAcquaCatasto cat: dto.getListaSitAcquaCatasto())
						{
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, cat.getFoglio());
							this.addCella(tableCat, normalFont, Element.ALIGN_LEFT, cat.getParticella());
						}
						table.addCell(tableCat);
					}
				}
				paragrafo.add(table);
			}else
				{
					paragrafo.add(new Paragraph("Nessuna utenza da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafo, 2);
				}
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafo, fonte.getDescrizione());
		
		document.add(paragrafo);
		document.newPage();
	}
	
	public List<AcquaUtenzeDTO> getListaUtenzeAcqua() {
		return listaUtenzeAcqua;
	}

	public void setListaUtenzeAcqua(List<AcquaUtenzeDTO> listaUtenzeAcqua) {
		this.listaUtenzeAcqua = listaUtenzeAcqua;
	}

	public SitAcquaUtente getUtenteAcqua() {
		return utenteAcqua;
	}

	public void setUtenteAcqua(SitAcquaUtente utenteAcqua) {
		this.utenteAcqua = utenteAcqua;
	}


	@Override
	public void ClearCampiTAB() {
		utenteAcqua = null;
		listaUtenzeAcqua = new ArrayList<AcquaUtenzeDTO>();
		
	}
	
}
