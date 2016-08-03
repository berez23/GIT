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
import it.webred.ct.data.access.basic.praticheportale.PratichePortaleDataIn;
import it.webred.ct.data.access.basic.praticheportale.PratichePortaleService;
import it.webred.ct.data.access.basic.praticheportale.dto.PraticaPortaleDTO;
import it.webred.ct.data.access.basic.traffico.TrafficoDataIn;
import it.webred.ct.data.access.basic.traffico.TrafficoService;
import it.webred.ct.data.model.acqua.SitAcquaCatasto;
import it.webred.ct.data.model.acqua.SitAcquaUtente;
import it.webred.ct.data.model.praticheportale.PsAnagraficaView;
import it.webred.ct.data.model.praticheportale.PsPratica;
import it.webred.ct.data.model.traffico.SitTrffMulte;

import it.webred.ct.service.carContrib.web.Utility;
import it.webred.ct.service.carContrib.web.utils.FonteDTO;

public class PratichePortaleCarContrib extends FonteBaseCarContrib{

	protected Logger logger = Logger.getLogger("carcontrib.log");
	
	private PratichePortaleService pratichePortaleService;
	private List<PraticaPortaleDTO> listaPratiche;
	private PsAnagraficaView anagrafica;
	
	@Override
	public void ClearCampiTAB() {
		listaPratiche = new ArrayList<PraticaPortaleDTO>();
		anagrafica=null;
	}
	
	// CARICA I DATI TAB PRATICHE
	public void LoadTabPRATICHE(String ente, String username, String codiceFiscale, String nome, String cognome){
		
		try{
			PratichePortaleDataIn dataIn = new PratichePortaleDataIn();
			dataIn.setEnteId(ente);
			dataIn.setUserId(username);
			dataIn.setCodFiscale(codiceFiscale);
			if(pratichePortaleService == null){
				Context ctx = new InitialContext();
				pratichePortaleService = (PratichePortaleService) Utility.getEjb("CT_Service", "CT_Service_Data_Access", "PratichePortaleServiceBean");
			}
			listaPratiche = pratichePortaleService.getListaPraticheByCodFis(dataIn);
			anagrafica = pratichePortaleService.getAnagraficaByCodFis(dataIn);
			
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
		
	}

	//GENERA SEZIONE PDF
	public void addSezionePdf(Document document, boolean cbx, String motivo, FonteDTO fonte) throws DocumentException{
		
		Paragraph paragrafo = new Paragraph();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		paragrafo.add(this.insertTitoloScheda("SCHEDA PRATICHE PORTALE"));
	
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
			
			if(anagrafica != null && anagrafica.getCodiceFiscale() != null){
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
				myCell.setPhrase(new Phrase(anagrafica.getCognome() + " " + anagrafica.getNome(),normalFont));
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
				
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase("INDIRIZZO",boldFont));					
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
		
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase(anagrafica.getViaResidenza(),normalFont));
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
				
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase("CODICE FISCALE",boldFont));					
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
		
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase(anagrafica.getCodiceFiscale(),normalFont));
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
				
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase("COMUNE RESIDENZA",boldFont));					
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
		
				myCell = tableDatiSoggetto.getDefaultCell();
				myCell.setPhrase(new Phrase(anagrafica.getComuneResidenza(),normalFont));
				myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiSoggetto.addCell(myCell);
				
				paragrafo.add(tableDatiSoggetto);
				
				this.addEmptyLine(paragrafo, 2);
			}
			
			if(listaPratiche != null && listaPratiche.size()>0){
				paragrafo.add(new Paragraph("Dati pratiche", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable table = new PdfPTable(10);
				table.setWidths(new float[] {10,10,10,10,10,10,10,10,10,10});
				table.setWidthPercentage(100);
				table.getDefaultCell().setMinimumHeight(20);
				
				PdfPCell c1 = table.getDefaultCell();
				c1.setPhrase(new Phrase("",boldFont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setColspan(4);
				table.addCell(c1);
				
				c1 = table.getDefaultCell();
				c1.setPhrase(new Phrase("RICHIEDENTE",boldFont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setColspan(3);
				table.addCell(c1);
				
				c1 = table.getDefaultCell();
				c1.setPhrase(new Phrase("FRUITORE",boldFont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setColspan(3);
				table.addCell(c1);
				
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "TIPO");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "SOTTOTIPO");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "DATA");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "STATO");
				
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "NOME");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "COGNOME");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "COD.FISCALE");
				
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "NOME");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "COGNOME");
				this.addCellaIntestazione(table, boldFont, Element.ALIGN_CENTER, "COD.FISCALE");
			
				for (PraticaPortaleDTO p: listaPratiche)
				{
					
					this.addCella(table, normalFont, Element.ALIGN_LEFT, p.getPratica().getTipoServizio());
					this.addCella(table, normalFont, Element.ALIGN_LEFT, p.getPratica().getSottoTipoServizio());
					this.addCella(table, normalFont, Element.ALIGN_LEFT, sdf.format(p.getPratica().getDataCreazione()));
					this.addCella(table, normalFont, Element.ALIGN_LEFT, p.getPratica().getNomeStato());
					
					this.addCella(table, normalFont, Element.ALIGN_LEFT, p.getRichiedente().getNome());
					this.addCella(table, normalFont, Element.ALIGN_LEFT, p.getRichiedente().getCognome());
					this.addCella(table, normalFont, Element.ALIGN_LEFT, p.getRichiedente().getCodiceFiscale());
					
					this.addCella(table, normalFont, Element.ALIGN_LEFT, p.getFruitore()!=null? p.getFruitore().getNome() : "");
					this.addCella(table, normalFont, Element.ALIGN_LEFT, p.getFruitore()!=null? p.getFruitore().getCognome() : "");
					this.addCella(table, normalFont, Element.ALIGN_LEFT, p.getFruitore()!=null? p.getFruitore().getCodiceFiscale() : "");
				}
				paragrafo.add(table);
			}else
				{
					paragrafo.add(new Paragraph("Nessuna pratica da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafo, 2);
				}
		}else
			this.addMotivoFonteDisabilitataCC(paragrafo, fonte.getDescrizione());
			
		document.add(paragrafo);
		document.newPage();
	}

	public List<PraticaPortaleDTO> getListaPratiche() {
		return listaPratiche;
	}

	public void setListaPratiche(List<PraticaPortaleDTO> listaPratiche) {
		this.listaPratiche = listaPratiche;
	}

	public PsAnagraficaView getAnagrafica() {
		return anagrafica;
	}

	public void setAnagrafica(PsAnagraficaView anagrafica) {
		this.anagrafica = anagrafica;
	}
}
