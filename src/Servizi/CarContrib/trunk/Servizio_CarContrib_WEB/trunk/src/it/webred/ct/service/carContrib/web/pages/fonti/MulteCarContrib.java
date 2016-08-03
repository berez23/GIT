package it.webred.ct.service.carContrib.web.pages.fonti;

import java.text.NumberFormat;
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
import it.webred.ct.data.access.basic.traffico.TrafficoDataIn;
import it.webred.ct.data.access.basic.traffico.TrafficoService;
import it.webred.ct.data.model.acqua.SitAcquaCatasto;
import it.webred.ct.data.model.acqua.SitAcquaUtente;
import it.webred.ct.data.model.traffico.SitTrffMulte;

import it.webred.ct.service.carContrib.web.Utility;
import it.webred.ct.service.carContrib.web.utils.FonteDTO;

public class MulteCarContrib extends FonteBaseCarContrib{

	protected Logger logger = Logger.getLogger("carcontrib.log");
	
	private TrafficoService multeService;
	private List<SitTrffMulte> listaMulte;
	private String denominazione;
	private String indirizzo;
	private String codFiscale;
	private String comune;
	
	// CARICA I DATI TAB MULTE
	public void LoadTabMULTE(String ente, String username, String codiceFiscale, String nome, String cognome){
		
		try{
			TrafficoDataIn dataIn = new TrafficoDataIn();
			dataIn.setEnteId(ente);
			dataIn.setUserId(username);
			dataIn.setObj(codiceFiscale);
			dataIn.setObj4(nome);
			dataIn.setObj5(cognome);
			if(multeService == null){
				Context ctx = new InitialContext();
				multeService = (TrafficoService) Utility.getEjb("CT_Service", "CT_Service_Data_Access", "TrafficoServiceBean");
			}
			listaMulte = multeService.getListaMulteByCriteria(dataIn);
			if(listaMulte.size() > 0){
				SitTrffMulte multa = listaMulte.get(0);
				denominazione = multa.getCognome() + " " + multa.getNome();
				indirizzo = multa.getIndirizzoResidenza();
				codFiscale = multa.getCodFisc();
				comune = multa.getLuogoResidenza();
			}
			
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
		
	}

	//GENERA SEZIONE PDF
	public void addSezionePdf(Document document, boolean cbx, String motivo, FonteDTO fonte) throws DocumentException{
		
		Paragraph paragrafo = new Paragraph();
		
		paragrafo.add(this.insertTitoloScheda("SCHEDA MULTE"));
	
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
			
			if(denominazione != null){
				paragrafo.add(new Phrase("Dati anagrafici dichiarati",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				// Sezione Dati Anagrafici dichiarati
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
				myCell.setPhrase(new Phrase("CODICE FISCALE",boldFont));					
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
		
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase(codFiscale,normalFont));
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
			
			if(listaMulte != null && listaMulte.size()>0){
				NumberFormat n = NumberFormat.getCurrencyInstance(Locale.ITALY);
				paragrafo.add(new Paragraph("Dati multe", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable table = new PdfPTable(9);
				table.setWidths(new float[] {10,10,10,10,10,10,10,20,10});
				table.setWidthPercentage(100);
				table.getDefaultCell().setMinimumHeight(20);
				
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "VERBALE NR.");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "DATA");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "IMPORTO");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "IMPORTO DOVUTO");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "IMPORTO PAGATO");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "DATA PAGAM.");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "DATA SCAD.");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "LUOGO");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "TARGA");
				
				for (SitTrffMulte m: listaMulte)
				{
					this.addCella(table, normalFont, Element.ALIGN_LEFT, m.getNrVerbale());
					this.addCella(table, normalFont, Element.ALIGN_LEFT, m.getDataMultaFormatted());
					this.addCella(table, normalFont, Element.ALIGN_LEFT, n.format(m.getImportoMulta().doubleValue()));
					this.addCella(table, normalFont, Element.ALIGN_LEFT, n.format(m.getImportoDovuto().doubleValue()));
					this.addCella(table, normalFont, Element.ALIGN_LEFT, n.format(m.getImportoPagato().doubleValue()));
					this.addCella(table, normalFont, Element.ALIGN_LEFT, m.getDtPagamentoFormatted());
					this.addCella(table, normalFont, Element.ALIGN_LEFT, m.getDtScadenzaPagamFormatted());
					this.addCella(table, normalFont, Element.ALIGN_LEFT, m.getLuogoInfrazione());
					this.addCella(table, normalFont, Element.ALIGN_LEFT, m.getTarga());
				}
				paragrafo.add(table);
			}else
				{
					paragrafo.add(new Paragraph("Nessuna multa da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafo, 2);
				}
		}else
			this.addMotivoFonteDisabilitataCC(paragrafo, fonte.getDescrizione());
			
		document.add(paragrafo);
		document.newPage();
	}

	
	public List<SitTrffMulte> getListaMulte() {
		return listaMulte;
	}
	public void setListaMulte(List<SitTrffMulte> listaMulte) {
		this.listaMulte = listaMulte;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}

	@Override
	public void ClearCampiTAB() {
		listaMulte = new ArrayList<SitTrffMulte>();
		denominazione="";
		indirizzo="";
		codFiscale="";
		comune="";
	}
}
