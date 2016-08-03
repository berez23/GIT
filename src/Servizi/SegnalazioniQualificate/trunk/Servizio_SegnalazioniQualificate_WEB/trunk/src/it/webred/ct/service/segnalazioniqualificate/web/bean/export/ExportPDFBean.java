package it.webred.ct.service.segnalazioniqualificate.web.bean.export;

import it.webred.ct.data.access.basic.segnalazionequalificata.SegnalazioniDataIn;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.PraticaSegnalazioneDTO;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.RicercaPraticaSegnalazioneDTO;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratica;
import it.webred.ct.service.segnalazioniqualificate.web.bean.SegnalazioniQualificateBaseBean;
import it.webred.ct.service.segnalazioniqualificate.web.bean.pagination.DataProviderImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ExportPDFBean extends SegnalazioniQualificateBaseBean {

	private String titolo;

	public void searchResultExportToPdf() {

		titolo = "Ricerca Segnalazioni Qualificate " + this.getTime();
		String nomeFilePdf = "RicercaSegnalazioniQualificate_"+this.getTimeStamp();
		
		try {

			Document document = new Document(PageSize.A4, 10, 10, 10, 10);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			PdfWriter.getInstance(document, baos);
			document.open();

			document.addTitle("pdf");
			document.addSubject("pdf");
			document.addAuthor ("Servizio AGENDA SEGNALAZIONI");
			document.addCreator("Servizio AGENDA SEGNALAZIONI");

			this.addTitolo(document);
			
			DataProviderImpl impl = (DataProviderImpl) getBeanReference("praticaDataProviderImpl");
			RicercaPraticaSegnalazioneDTO criteria = impl.getCriteria();

			criteria.setStartm(0);
			criteria.setNumberRecord(1000);
			SegnalazioniDataIn dataIn = this.getInitRicercaParams();
			fillEnte(criteria);
			dataIn.setRicercaPratica(criteria);
			List<SOfPratica> lista = segnalazioneService.search(dataIn);
			List<PraticaSegnalazioneDTO> listaDTO = new ArrayList<PraticaSegnalazioneDTO>();
			for(SOfPratica p: lista){
				PraticaSegnalazioneDTO dto = new PraticaSegnalazioneDTO();
				dto.setPratica(p);
				listaDTO.add(dto);
			}

			Paragraph subTit1 = new Paragraph("Parametri di Ricerca", new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD));
			subTit1.setAlignment(Element.ALIGN_LEFT);
			document.add(subTit1);
			this.addCriteria(document,criteria);
			
			Paragraph line = new Paragraph();
			this.addEmptyLine(line, 2);
			document.add(line);
			
			Paragraph subTit2 = new Paragraph("Risultati di Ricerca", new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD));
			subTit2.setAlignment(Element.ALIGN_LEFT);
			document.add(subTit2);
			this.addLista(document, listaDTO);

			document.close();

			if (baos != null)
				this.showPdf(baos, nomeFilePdf);
		} catch (Throwable t) {
			super.addErrorMessage("export.pdf.error", t.getMessage());
			t.printStackTrace();
		}

	}

	
	private void addTitolo(Document document) throws DocumentException {

		Paragraph tit = new Paragraph();
		this.addEmptyLine(tit, 1);
		Paragraph intestazione = new Paragraph(titolo, new Font(
				Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD));
		intestazione.setAlignment(Element.ALIGN_CENTER);
		tit.add(intestazione);
		this.addEmptyLine(tit, 3);

		document.add(tit);
	}
	
	
	private void addCriteria(Document document, RicercaPraticaSegnalazioneDTO c)
	throws DocumentException {

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
		
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(50);
		table.setWidths(new float[] { 50, 50 });
		table.getDefaultCell().setMinimumHeight(20);
		
		table.addCell(getCell(table, "Cod.Fiscale Soggetto Accertato", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getAccCodiFisc(),normalFont, BaseColor.WHITE, null));

		table.addCell(getCell(table, "Cognome Soggetto Accertato", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getAccCognome(),normalFont, BaseColor.WHITE, null));
		
		table.addCell(getCell(table, "Nome Soggetto Accertato", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getAccNome(),normalFont, BaseColor.WHITE, null));

		table.addCell(getCell(table, "P.IVA Soggetto Accertato", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getAccCodiPiva(),normalFont, BaseColor.WHITE, null));
		
		table.addCell(getCell(table, "Denominazione Soggetto Accertato", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getAccCognome(),normalFont, BaseColor.WHITE, null));
		
		table.addCell(getCell(table, "Cod.Fiscale Responsabile", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getResCodiFisc(),normalFont, BaseColor.WHITE, null));

		table.addCell(getCell(table, "Cognome Responsabile", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getResCognome(),normalFont, BaseColor.WHITE, null));
		
		table.addCell(getCell(table, "Nome Responsabile", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getResNome(),normalFont, BaseColor.WHITE, null));

		table.addCell(getCell(table, "Data Inizio Procedimento DA", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getDataInizioDa()!= null? sdf.format(c.getDataInizioDa()): "",normalFont, BaseColor.WHITE, null));

		table.addCell(getCell(table, "Data Inizio Procedimento A", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getDataInizioA()!= null? sdf.format(c.getDataInizioA()): "",normalFont, BaseColor.WHITE, null));
		
		table.addCell(getCell(table, "Finalità", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getAccFinalitaDecoded(),normalFont, BaseColor.WHITE, null));

		table.addCell(getCell(table, "Stato", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getAccStatoDecoded(),normalFont, BaseColor.WHITE, null));
		
		table.addCell(getCell(table, "Operatori", boldFont,BaseColor.LIGHT_GRAY, null));
		table.addCell(getCell(table, c.getsOperatori(),normalFont, BaseColor.WHITE, null));
		
		document.add(table);
	}


	private void addLista(Document document, List<PraticaSegnalazioneDTO> lista)
			throws DocumentException {

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);

		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 });
		table.getDefaultCell().setMinimumHeight(20);

		table.addCell(getCell(table, "Numero Pratica", boldFont,
				BaseColor.LIGHT_GRAY, null));

		table.addCell(getCell(table, "Data Inizio Iter", boldFont,
				BaseColor.LIGHT_GRAY, null));

		table.addCell(getCell(table, "Data Chiusura Iter", boldFont,
				BaseColor.LIGHT_GRAY, null));

		table.addCell(getCell(table, "Finalità", boldFont,
				BaseColor.LIGHT_GRAY, null));

		table.addCell(getCell(table, "Stato della Pratica", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		table.addCell(getCell(table, "Operatore", boldFont,
				BaseColor.LIGHT_GRAY, null));

		table.addCell(getCell(table, "Cod.Fiscale Responsabile", boldFont,
				BaseColor.LIGHT_GRAY, null));

		table.addCell(getCell(table, "Responsabile Procedimento", boldFont,
				BaseColor.LIGHT_GRAY, null));

		table.addCell(getCell(table, "P.IVA/Cod.Fiscale", boldFont,
				BaseColor.LIGHT_GRAY, null));

		table.addCell(getCell(table, "Denominazione/Cognome e Nome", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (PraticaSegnalazioneDTO dto : lista) {
			SOfPratica p = dto.getPratica();

			table.addCell(getCell(table, Long.toString(p.getId()),
					normalFont, BaseColor.WHITE, null));

			table.addCell(getCell(table, p.getAccDataInizio()!= null? sdf.format(p.getAccDataInizio()): "",
					normalFont, BaseColor.WHITE, null));

			table.addCell(getCell(table, p.getAccDataFine()!= null? sdf.format(p.getAccDataFine()): "",
					normalFont, BaseColor.WHITE, null));

			table.addCell(getCell(table, dto.getAccFinalitaDecoded(),
					normalFont, BaseColor.WHITE, null));

			table.addCell(getCell(table, dto.getAccStatoDecoded(),
					normalFont, BaseColor.WHITE, null));
			
			table.addCell(getCell(table, p.getOperatoreId(),
					normalFont, BaseColor.WHITE, null));
			
			table.addCell(getCell(table, p.getResCognome()+" "+p.getResNome(),
					normalFont, BaseColor.WHITE, null));
			
			table.addCell(getCell(table, p.getResCodiFisc(),
					normalFont, BaseColor.WHITE, null));
			
			if("G".equalsIgnoreCase(p.getAccTipoSogg())){
				table.addCell(getCell(table, p.getAccDenominazione(),
					normalFont, BaseColor.WHITE, null));
				
				table.addCell(getCell(table, p.getAccCodiPiva(),
						normalFont, BaseColor.WHITE, null));
			
			}else{
				table.addCell(getCell(table, p.getAccCognome()+" "+p.getAccNome(),
						normalFont, BaseColor.WHITE, null));
				
				table.addCell(getCell(table, p.getAccCodiFisc(),
						normalFont, BaseColor.WHITE, null));
			}
					
		}

		document.add(table);
	}

	
	private PdfPCell getCell(PdfPTable table, String phrase, Font font,
			BaseColor color, Integer border) {

		return getCell(table, phrase, font, color, border, new Integer(1));
	}

	private PdfPCell getCell(PdfPTable table, String phrase, Font font,
			BaseColor color, Integer border, Integer colSpan) {

		PdfPCell c1 = table.getDefaultCell();
		c1.setPhrase(new Phrase(phrase, font));
		if (border != null)
			c1.setBorder(border.intValue());
		if (colSpan != null)
			c1.setColspan(colSpan.intValue());
		c1.setBackgroundColor(color);

		return c1;
	}

	private PdfPCell getCell(PdfPTable table, Date date, Font font,
			BaseColor color, Integer border) {

		return getCell(table, date, font, color, border, new Integer(1));
	}

	private PdfPCell getCell(PdfPTable table, Date date, Font font,
			BaseColor color, Integer border, Integer colSpan) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		PdfPCell c1 = table.getDefaultCell();
		c1.setPhrase(date != null ? new Phrase(sdf.format(date), font)
				: new Phrase("", font));
		if (border != null)
			c1.setBorder(border.intValue());
		if (colSpan != null)
			c1.setColspan(colSpan.intValue());
		c1.setBackgroundColor(color);

		return c1;
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private void showPdf(ByteArrayOutputStream baos, String nomeFile)
			throws IOException {
		FacesContext faces = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) faces
				.getExternalContext().getResponse();

		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		// response.setHeader("Content-disposition","inline; filename=kiran.pdf");
		response.setHeader("Pragma", "public");
		response.setContentType("application/pdf");
		response.addHeader("Content-disposition", "attachment;filename=\""
				+ nomeFile + ".pdf");

		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		baos.flush();

		response.setContentLength(baos.size());

		faces.responseComplete();
	}
	
	
	

}
