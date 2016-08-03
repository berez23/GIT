package it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione.export;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.ModuloDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsConfig;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaDovuta;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaIncassata;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloEventi;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsStrutturaSnap;
import it.webred.ct.service.tsSoggiorno.data.model.IsTabModuloField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class DichiarazionePDF {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
	Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
	Font titoloFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
	Font tableFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
	Font italicFont = new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC);
	Font tableBoldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

	private DichiarazioneDTO dichDTO;
	private List<ModuloDTO> listaModuli1;
	private List<ModuloDTO> listaModuli2;
	private List<ModuloDTO> listaModuli3;
	private List<ModuloDTO> listaModuli4;
	private IsImpostaDovuta impDovuta;
	private IsImpostaIncassata impIncassata;
	private String descrTitoloSoc;

	private int numRifiutiPag;

	private HashMap<String, String> hashParametri;
	private IsConfig logo;

	public Document getPDFDocument(Document document) throws DocumentException,
			IOException {

		document.addTitle("pdf");
		document.addSubject("pdf");
		document.addAuthor("Servizio Imposta di Soggiorno");
		document.addCreator("Servizio Imposta di Soggiorno");

		addTitolo(document);
		addSoggetto(document);
		addClassificazione(document);
		addArrivi(document);
		addImposta(document);
		addVersato(document);
		addEsenzioni(document);
		addRiduzioni(document);
		addPagamenti(document);
		addModalitaPagamento(document);

		return document;
	}

	private void addTitolo(Document document) throws DocumentException,
			IOException {

		document.add(new Paragraph(" "));
		Paragraph loc = getParagraphHTML(
				hashParametri.get("DICH_INTESTAZIONE"), boldFont);
		loc.setAlignment(Element.ALIGN_RIGHT);
		document.add(loc);
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));

		Paragraph tit = new Paragraph();
		tit.add(new Paragraph(hashParametri.get("DICH_TITOLO"), boldFont));
		tit.add(new Paragraph(hashParametri.get("DICH_TITOLETTO"), tableBoldFont));
		tit.setAlignment(Element.ALIGN_CENTER);
		document.add(tit);
		document.add(new Paragraph(" "));

		Paragraph per = new Paragraph();
		per.add(new Paragraph("RIFERITA AL MESE DI "
				+ dichDTO.getPeriodo().getDescrizione().toUpperCase(), boldFont));
		per.setAlignment(Element.ALIGN_LEFT);
		this.addEmptyLine(per, 2);
		document.add(per);
	}

	private void addSoggetto(Document document) throws DocumentException {

		IsStrutturaSnap ss = dichDTO.getStrutturaSnap();
		Paragraph sog = new Paragraph();
		String s = "Il/la sottocritto/a " + getString(ss.getCognome()) + " "
				+ getString(ss.getNome()) + " nato/a "
				+ getString(ss.getComuneNasc()) + " prov. "
				+ getString(ss.getSiglaProvNasc()) + " il "
				+ (ss.getDtNasc() != null ? sdf.format(ss.getDtNasc()) : "")
				+ " residente a " + getString(ss.getComuneRes()) + " prov. "
				+ getString(ss.getSiglaProvRes()) + " in "
				+ getString(ss.getIndirizzoRes()) + " n.civico "
				+ getString(ss.getNumeroCivRes()) + " CAP "
				+ getString(ss.getCapRes())
				+ " Tel. " + getString(ss.getTel())
				+ " Cell. " + getString(ss.getCell()) + " Fax "
				+ getString(ss.getFax()) + " E-MAIL " + getString(ss.getEmail()).toLowerCase();
		sog.add(new Paragraph(s, normalFont));
		s = "in qualità di " + getString(descrTitoloSoc).toUpperCase()
			//	+ " DELLA DITTA INDIVIDUALE " 
				+ " " + getString(ss.getRagSoc());
		sog.add(new Paragraph(s, normalFont));
		s= "C.F. e P.IVA " + getString(ss.getCf()) + "  "
				+ getString(ss.getPi()) + " con sede legale in "
				+ getString(ss.getComuneSede()) + " alla "
				+ getString(ss.getIndirizzoSede()) + " "
				+ getString(ss.getNumeroCivSede())+",";
		sog.add(new Paragraph(s, normalFont));
		s = "con riferimento alla struttura ricettiva "
				+ getString(ss.getDescrizioneStrut()) + " ubicata in "
				+ getString(ss.getIndirizzoStrut()) + " n.civico " + getString(ss.getNumCivStrut())
				+" E-MAIL "+ getString(ss.getEmailStrut()).toLowerCase();
		sog.add(new Paragraph(s, normalFont));
		this.addEmptyLine(sog, 1);
		document.add(sog);

	}

	private void addClassificazione(Document document) throws DocumentException {

		IsStrutturaSnap ss = dichDTO.getStrutturaSnap();
		Paragraph titcls = new Paragraph();
		titcls.add(new Paragraph("classificazione alberghiera".toUpperCase(),
				boldFont));
		this.addEmptyLine(titcls, 1);
		document.add(titcls);

		Paragraph cls = new Paragraph();
		cls.add(new Paragraph(ss.getDescrizioneClasseStrut(), normalFont));
		this.addEmptyLine(cls, 2);
		document.add(cls);

		Paragraph tit = new Paragraph("DICHIARA", boldFont);
		tit.setAlignment(Element.ALIGN_CENTER);
		this.addEmptyLine(tit, 1);
		document.add(tit);

		Paragraph tit2 = new Paragraph("CHE NEL MESE DI "
				+ dichDTO.getPeriodo().getDescrizione()
				+ " HA AVUTO NELLA PROPRIA STRUTTURA RICETTIVA:", boldFont);
		tit2.setAlignment(Element.ALIGN_LEFT);
		this.addEmptyLine(tit2, 2);
		document.add(tit2);

	}

	private void addArrivi(Document document) throws DocumentException,
			IOException {

		Paragraph tit = new Paragraph("A) ARRIVI", boldFont);
		document.add(tit);
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(30);
		table.setWidths(new float[] { 100 });
		table.getDefaultCell().setMinimumHeight(20);

		table.addCell(getCell(table, "N. ARRIVI DEL MESE", tableFont,
				BaseColor.WHITE, null));

		table.addCell(getCell(table, getString(dichDTO.getDichiarazione()
				.getArrivi()), tableFont, BaseColor.WHITE, null));

		document.add(table);
		document.add(new Paragraph(" "));
	}

	private void addImposta(Document document) throws DocumentException,
			IOException {

		Paragraph tit = new Paragraph("B) DEFINIZIONE DELL'IMPOSTA", boldFont);
		document.add(tit);
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(3);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 33, 33, 33 });
		table.getDefaultCell().setMinimumHeight(20);

		table.addCell(getCell(table,
				"N. PRESENZE TOTALI DEL MESE DI COMPETENZA", tableFont,
				BaseColor.WHITE, null));
		table.addCell(getCell(table, "N. TOTALE ESENTI", tableFont,
				BaseColor.WHITE, null));
		table.addCell(getCell(table, "N. TOTALE ASSOGGETTATI A IMPOSTA",
				tableFont, BaseColor.WHITE, null));

		table.addCell(getCell(table, getString(dichDTO.getDichiarazione()
				.getPresenze()), tableFont, BaseColor.WHITE, null));
		table.addCell(getCell(table, getString(dichDTO.getDichiarazione()
				.getEsenti()), tableFont, BaseColor.WHITE, null));
		table.addCell(getCell(table, getString(dichDTO.getDichiarazione()
				.getAssoggImposta()), tableFont, BaseColor.WHITE, null));

		document.add(table);
		document.add(new Paragraph(" "));
	}

	private void addVersato(Document document) throws DocumentException,
			IOException {

		Paragraph tit = new Paragraph("C) DEFINIZIONE DEL VERSATO", boldFont);
		document.add(tit);
		document.add(new Paragraph(" "));

		Paragraph tit2 = new Paragraph("Mese di competenza", tableFont);
		document.add(tit2);
		PdfPTable table = new PdfPTable(3);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 33, 33, 33 });
		table.getDefaultCell().setMinimumHeight(20);

		table.addCell(getCell(table,
				"IMPOSTA <b>DOVUTA</b> DEL MESE DI <b>COMPETENZA (1)</b>",
				tableFont, BaseColor.WHITE, true));
		table.addCell(getCell(
				table,
				"IMPOSTA <b>INCASSATA</b> RELATIVA AL MESE DI <b>COMPETENZA (2)</b>",
				tableFont, BaseColor.WHITE, true));
		table.addCell(getCell(
				table,
				"IMPOSTA <b>IN SOSPESO</b> RELATIVA AL MESE DI <b>COMPETENZA (3)</b>",
				tableFont, BaseColor.WHITE, true));
		table.addCell(getCell(table, getString(impDovuta.getImporto()),
				tableFont, BaseColor.WHITE, null));
		table.addCell(getCell(table, getString(impIncassata.getImporto()),
				tableFont, BaseColor.WHITE, null));
		BigDecimal impSospeso = impDovuta.getImporto().subtract(
				impIncassata.getImporto());
		table.addCell(getCell(table, getString(impSospeso), tableFont,
				BaseColor.WHITE, null));

		document.add(table);
		document.add(new Paragraph(" "));

		tit2 = new Paragraph("Mesi precedenti", tableFont);
		document.add(tit2);
		table = new PdfPTable(3);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 33, 33, 33 });
		table.getDefaultCell().setMinimumHeight(20);

		table.addCell(getCell(table,
				"<b>RESIDUO</b> DA MESI <b>PRECEDENTI (4)</b>", tableFont,
				BaseColor.WHITE, true));
		table.addCell(getCell(
				table,
				"IMPOSTA <b>INCASSATA</b> RELATIVA AI MESI <b>PRECEDENTI (5)</b>",
				tableFont, BaseColor.WHITE, true));
		table.addCell(getCell(table,
				"RESIDUO DA RIPORTARE RELATIVO A MESI <b>PRECEDENTI (6)</b>",
				tableFont, BaseColor.WHITE, true));
		table.addCell(getCell(table, getString(impDovuta.getImportoMesiPrec()),
				tableFont, BaseColor.WHITE, null));
		table.addCell(getCell(table,
				getString(impIncassata.getImportoMesiPrec()), tableFont,
				BaseColor.WHITE, null));
		BigDecimal impSospesoPrec = impDovuta.getImportoMesiPrec().subtract(
				impIncassata.getImportoMesiPrec());
		table.addCell(getCell(table, getString(impSospesoPrec), tableFont,
				BaseColor.WHITE, null));

		document.add(table);
		document.add(new Paragraph(" "));

		tit2 = new Paragraph("Totali", tableFont);
		document.add(tit2);
		table = new PdfPTable(2);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(66);
		table.setWidths(new float[] { 50, 50 });
		table.getDefaultCell().setMinimumHeight(20);

		table.addCell(getCell(table, "<b>TOTALE INCASSATO (2+5)</b>",
				tableFont, BaseColor.WHITE, true));
		table.addCell(getCell(table,
				"<b>TOTALE RESIDUO DA RIPORTARE (3+6)</b>", tableFont,
				BaseColor.WHITE, true));
		table.addCell(getCell(table,
				impIncassata.getImporto()
						.add(impIncassata.getImportoMesiPrec()).toString(),
				tableFont, BaseColor.WHITE, null));
		table.addCell(getCell(table, impSospeso.add(impSospesoPrec).toString(),
				tableFont, BaseColor.WHITE, null));

		document.add(table);
		document.add(new Paragraph(" "));

		tit2 = new Paragraph("Rifiuti al pagamento", tableFont);
		document.add(tit2);

		PdfPTable tableAll = new PdfPTable(2);
		tableAll.setHorizontalAlignment(Element.ALIGN_LEFT);
		tableAll.setWidthPercentage(66);
		numRifiutiPag = 0;
		for (ModuloDTO dto : listaModuli1) {

			table = new PdfPTable(dto.getListaField().size());
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.setWidthPercentage(100);
			table.getDefaultCell().setMinimumHeight(20);

			String descr = dto.getDati().getDescrizione1()
					+ (dto.getDati().getDescrizione2() != null ? dto.getDati()
							.getDescrizione2() : "");
			table.addCell(getCell(table, descr, tableFont, BaseColor.WHITE,
					null, dto.getListaField().size(), true));
			for (IsTabModuloField field : dto.getListaField()) {
				table.addCell(getCell(table, field.getLabelField(), tableFont,
						BaseColor.WHITE, null));
			}
			table.addCell(getCell(table,
					getString(dto.getOspiti().getNOspiti()), tableFont,
					BaseColor.WHITE, null));
			numRifiutiPag += dto.getOspiti().getNOspiti() != null ? dto
					.getOspiti().getNOspiti().intValue() : 0;
			table.addCell(getCell(table, getString(dto.getOspiti()
					.getNSoggiorni()), tableFont, BaseColor.WHITE, null));

			PdfPCell cell = new PdfPCell(new Phrase("Nested"));
			cell.setPadding(0);
			cell.addElement(table);
			cell.setBorder(0);
			tableAll.addCell(cell);
		}

		document.add(tableAll);
		document.add(new Paragraph(" "));

	}

	private void addEsenzioni(Document document) throws DocumentException,
			IOException {

		Paragraph tit = new Paragraph(
				"D) ESENZIONI (indicare nelle righe sia il numero delle persone che quello dei pernottamenti di cui si allegano attestazioni)",
				boldFont);
		document.add(tit);
		document.add(new Paragraph(" "));

		PdfPTable tableAll = new PdfPTable(3);
		tableAll.setHorizontalAlignment(Element.ALIGN_LEFT);
		tableAll.setWidthPercentage(100);
		for (ModuloDTO dto : listaModuli2) {

			PdfPTable table = new PdfPTable(dto.getListaField().size());
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.setWidthPercentage(100);
			table.getDefaultCell().setMinimumHeight(20);

			String descr = dto.getDati().getDescrizione1()
					+ (dto.getDati().getDescrizione2() != null ? dto.getDati()
							.getDescrizione2() : "");
			PdfPCell c = getCell(table, descr, tableFont, BaseColor.WHITE,
					null, dto.getListaField().size(), true);
			c.setMinimumHeight(90);
			table.addCell(c);
			c.setMinimumHeight(20);
			for (IsTabModuloField field : dto.getListaField()) {
				table.addCell(getCell(table, field.getLabelField(), tableFont,
						BaseColor.WHITE, null));
			}
			table.addCell(getCell(table,
					getString(dto.getOspiti().getNOspiti()), tableFont,
					BaseColor.WHITE, null));
			table.addCell(getCell(table, getString(dto.getOspiti()
					.getNSoggiorni()), tableFont, BaseColor.WHITE, null));

			PdfPCell cell = new PdfPCell(new Phrase("Nested"));
			cell.setPadding(0);
			cell.setPaddingBottom(30);
			cell.addElement(table);
			cell.setBorder(0);
			tableAll.addCell(cell);
		}

		// completo la tabella con celle vuote
		if (listaModuli2.size() % 3 == 1) {
			PdfPCell cell = new PdfPCell();
			cell.setBorder(0);
			tableAll.addCell(cell);
			tableAll.addCell(cell);
		}
		if (listaModuli2.size() % 3 == 2) {
			PdfPCell cell = new PdfPCell();
			cell.setBorder(0);
			tableAll.addCell(cell);
		}

		document.add(tableAll);
		document.add(new Paragraph(" "));
	}

	private void addRiduzioni(Document document) throws DocumentException,
			IOException {

		for (ModuloDTO dto : listaModuli3) {
			String descr = dto.getDati().getDescrizione1()
					+ (dto.getDati().getDescrizione2() != null ? dto.getDati()
							.getDescrizione2() : "");
			Paragraph tit = new Paragraph();
			// gestione tag html
			ArrayList p = new ArrayList();
			descr = "<div style=\"font-size:" + boldFont.getSize() + "\">"
					+ descr + "</div>";
			StringReader strReader = new StringReader(descr);
			p = (ArrayList) HTMLWorker.parseToList(strReader, null);
			for (int k = 0; k < p.size(); ++k) {
				Element e = (Element) p.get(k);
				tit.add(e);
			}
			document.add(tit);
			document.add(new Paragraph(" "));

			PdfPTable table = new PdfPTable(dto.getListaField().size());
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.setWidthPercentage(100);
			table.getDefaultCell().setMinimumHeight(20);

			for (IsTabModuloField field : dto.getListaField()) {
				table.addCell(getCell(table, field.getLabelField(), tableFont,
						BaseColor.WHITE, null));
			}
			for (IsModuloEventi evento : dto.getEventi()) {
				if (dto.getDati().getLista().intValue() == 1)
					table.addCell(getCell(table, evento.getEvento(), tableFont,
							BaseColor.WHITE, null));
				table.addCell(getCell(table, getString(evento.getNOspiti()),
						tableFont, BaseColor.WHITE, null));
				table.addCell(getCell(table, getString(evento.getNSoggiorni()),
						tableFont, BaseColor.WHITE, null));
				if (dto.getDati().getLista().intValue() == 1)
					table.addCell(getCell(table, evento.getField1(), tableFont,
							BaseColor.WHITE, null));
				table.addCell(getCell(table, evento.getField2(), tableFont,
						BaseColor.WHITE, null));
			}

			document.add(table);
			document.add(new Paragraph(" "));
		}

	}

	private void addPagamenti(Document document) throws DocumentException,
			IOException {

		for (ModuloDTO dto : listaModuli4) {
			String descr = dto.getDati().getDescrizione1()
					+ (dto.getDati().getDescrizione2() != null ? dto.getDati()
							.getDescrizione2() : "");
			Paragraph tit = new Paragraph();
			// gestione tag html
			ArrayList p = new ArrayList();
			descr = "<div style=\"font-size:" + boldFont.getSize() + "\">"
					+ descr + "</div>";
			StringReader strReader = new StringReader(descr);
			p = (ArrayList) HTMLWorker.parseToList(strReader, null);
			for (int k = 0; k < p.size(); ++k) {
				Element e = (Element) p.get(k);
				tit.add(e);
			}
			document.add(tit);
			document.add(new Paragraph(" "));

			PdfPTable table = new PdfPTable(dto.getListaField().size());
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.setWidthPercentage(100);
			table.getDefaultCell().setMinimumHeight(20);

			for (IsTabModuloField field : dto.getListaField()) {
				table.addCell(getCell(table, field.getLabelField(), tableFont,
						BaseColor.WHITE, null));
			}
			for (IsModuloEventi evento : dto.getEventi()) {
				if (dto.getDati().getLista().intValue() == 1)
					table.addCell(getCell(table, evento.getEvento(), tableFont,
							BaseColor.WHITE, null));
				table.addCell(getCell(table, getString(evento.getNOspiti()),
						tableFont, BaseColor.WHITE, null));
				table.addCell(getCell(table, getString(evento.getNSoggiorni()),
						tableFont, BaseColor.WHITE, null));
				if (dto.getDati().getLista().intValue() == 1)
					table.addCell(getCell(table, evento.getField1(), tableFont,
							BaseColor.WHITE, null));
				table.addCell(getCell(table, evento.getField2(), tableFont,
						BaseColor.WHITE, null));
			}

			document.add(table);
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
		}

	}

	private void addModalitaPagamento(Document document)
			throws DocumentException, IOException {

		Paragraph tit = new Paragraph(
				"MODALITA' DI VERSAMENTO AL COMUNE DELL'IMPOSTA", boldFont);
		tit.setAlignment(Element.ALIGN_CENTER);
		document.add(tit);
		document.add(new Paragraph(" "));

		tit = getParagraphHTML(hashParametri.get("PAG_MAV"), tableBoldFont);
		tit.setAlignment(Element.ALIGN_LEFT);
		document.add(tit);
		document.add(new Paragraph(" "));

		if (dichDTO.getDichiarazione().getPagMav() == null
				|| "".equals(dichDTO.getDichiarazione().getPagMav()))
			tit = new Paragraph(
					"______________________________________________", tableFont);
		else
			tit = new Paragraph(getString(dichDTO.getDichiarazione()
					.getPagMav()), tableFont);
		document.add(tit);
		tit = new Paragraph("(indicarne gli estremi del pagamento)", italicFont);
		document.add(tit);
		document.add(new Paragraph(" "));

		document.add(getParagraphHTML(hashParametri.get("PAG_VERSAMENTO"),
				tableBoldFont));
		document.add(new Paragraph(" "));
		if (dichDTO.getDichiarazione().getPagVersamento() == null
				|| "".equals(dichDTO.getDichiarazione().getPagVersamento()))
			tit = new Paragraph(
					"______________________________________________", tableFont);
		else
			tit = new Paragraph(getString(dichDTO.getDichiarazione()
					.getPagVersamento()), tableFont);
		document.add(tit);
		tit = new Paragraph("(indicarne gli estremi del pagamento)", italicFont);
		document.add(tit);
		document.add(new Paragraph(" "));

		document.add(getParagraphHTML(hashParametri.get("PAG_BONIFICO"),
				tableFont));
		document.add(new Paragraph(" "));
		if (dichDTO.getDichiarazione().getPagBonifico() == null
				|| "".equals(dichDTO.getDichiarazione().getPagBonifico()))
			tit = new Paragraph(
					"______________________________________________", tableFont);
		else
			tit = new Paragraph(getString(dichDTO.getDichiarazione()
					.getPagBonifico()), tableFont);
		document.add(tit);
		tit = new Paragraph(
				"(indicarne gli estremi del pagamento e allegarne copia)", italicFont);
		document.add(tit);
		document.add(new Paragraph(" "));
		
		if(dichDTO.getListaRimborsi()!=null && dichDTO.getListaRimborsi().size()>0){
			document.add(getParagraphHTML(hashParametri.get("PAG_COMPENSAZIONE"),tableBoldFont));
			for(IsRimborso rimborso : dichDTO.getListaRimborsi()){
				document.add(new Paragraph(("- €"+ getString(rimborso.getValore())
						+ " per " + (rimborso.getDescrizione()!= null ? rimborso.getDescrizione(): "______") +";"), tableFont));
			}
			document.add(new Paragraph("per un totale di € " + dichDTO.getDichiarazione().getValRimborso(),tableBoldFont));
			document.add(new Paragraph(" "));
		}
		
		if(dichDTO.getListaSanzioni()!=null && dichDTO.getListaSanzioni().size()>0){
			document.add(getParagraphHTML(hashParametri.get("PAG_SANZIONE"),tableBoldFont));
			for(IsSanzione sanzione : dichDTO.getListaSanzioni()){
				document.add(new Paragraph(("- € "+ getString(sanzione.getValore())
						+ " per " + (sanzione.getDescrizione()!= null ? sanzione.getDescrizione(): "______") +";"), tableFont));
			}
			document.add(new Paragraph("per un totale di € " + dichDTO.getDichiarazione().getValSanzione(),tableBoldFont));
			document.add(new Paragraph(" "));
		}
		
		document.add(new Paragraph(" "));

		tit = new Paragraph("Allegati:", tableBoldFont);
		document.add(tit);
		tit = new Paragraph("1. copia del documento del dichiarante", tableFont);
		document.add(tit);
		tit = new Paragraph(
				"2. attestazioni di esenzioni (le attestazioni possono essere trattenute e conservate presso la struttura ricettiva)",
				tableFont);
		document.add(tit);
		tit = new Paragraph(
				"3. "
						+ numRifiutiPag
						+ " dichiarazioni di rifiuto di pagamento dell'imposta di soggiorno",
				tableFont);
		this.addEmptyLine(tit, 3);
		document.add(tit);

		tit = new Paragraph(
				"Data e luogo.............................................",
				tableFont);
		this.addEmptyLine(tit, 1);
		document.add(tit);
		tit = new Paragraph("Timbro e Firma del legale rappresentante",
				tableFont);
		tit.setAlignment(Element.ALIGN_RIGHT);
		document.add(tit);
		document.add(new Paragraph(" "));
		tit = new Paragraph(
				".........................................................",
				tableFont);
		tit.setAlignment(Element.ALIGN_RIGHT);
		document.add(tit);
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private PdfPCell getCell(PdfPTable table, String phrase, Font font,
			BaseColor color, Integer border) throws IOException {

		return getCell(table, phrase, font, color, border, new Integer(1),
				false);
	}

	private PdfPCell getCell(PdfPTable table, String phrase, Font font,
			BaseColor color, boolean formatHTMl) throws IOException {

		return getCell(table, phrase, font, color, null, new Integer(1),
				formatHTMl);
	}

	private PdfPCell getCell(PdfPTable table, String phrase, Font font,
			BaseColor color, Integer border, Integer colSpan, boolean formatHTMl)
			throws IOException {

		PdfPCell c1 = table.getDefaultCell();

		Phrase ph = new Phrase();
		if (formatHTMl) {
			// gestione tag html
			ArrayList p = new ArrayList();
			phrase = "<div style=\"font-size:" + font.getSize() + "\">"
					+ phrase + "</div>";
			StringReader strReader = new StringReader(phrase);
			p = (ArrayList) HTMLWorker.parseToList(strReader, null);
			for (int k = 0; k < p.size(); ++k) {
				Element e = (Element) p.get(k);
				ph.add(e);
			}
		} else
			ph = new Phrase(phrase, font);

		c1.setPhrase(ph);
		if (border != null)
			c1.setBorder(border.intValue());
		if (colSpan != null)
			c1.setColspan(colSpan.intValue());
		c1.setBackgroundColor(null);

		return c1;
	}

	private Paragraph getParagraphHTML(String testoHTML, Font font)
			throws IOException {

		Paragraph tit = new Paragraph("", font);
		if (testoHTML != null) {
			ArrayList p = new ArrayList();
			testoHTML = "<div style=\"font-size:" + font.getSize() + "\">"
					+ testoHTML + "</div>";
			StringReader strReader = new StringReader(testoHTML);
			p = (ArrayList) HTMLWorker.parseToList(strReader, null);
			for (int k = 0; k < p.size(); ++k) {
				Element e = (Element) p.get(k);
				tit.add(e);
			}
		}
		return tit;
	}

	private String getString(String s) {

		if (s != null && !"".equals(s))
			return s.toUpperCase();

		return "";
	}

	private String getString(BigDecimal bd) {

		if (bd != null)
			return bd.toString();

		return "0";
	}

	public List<ModuloDTO> getListaModuli1() {
		return listaModuli1;
	}

	public void setListaModuli1(List<ModuloDTO> listaModuli1) {
		this.listaModuli1 = listaModuli1;
	}

	public List<ModuloDTO> getListaModuli2() {
		return listaModuli2;
	}

	public void setListaModuli2(List<ModuloDTO> listaModuli2) {
		this.listaModuli2 = listaModuli2;
	}

	public List<ModuloDTO> getListaModuli3() {
		return listaModuli3;
	}

	public void setListaModuli3(List<ModuloDTO> listaModuli3) {
		this.listaModuli3 = listaModuli3;
	}

	public List<ModuloDTO> getListaModuli4() {
		return listaModuli4;
	}

	public void setListaModuli4(List<ModuloDTO> listaModuli4) {
		this.listaModuli4 = listaModuli4;
	}

	public IsImpostaDovuta getImpDovuta() {
		return impDovuta;
	}

	public void setImpDovuta(IsImpostaDovuta impDovuta) {
		this.impDovuta = impDovuta;
	}

	public IsImpostaIncassata getImpIncassata() {
		return impIncassata;
	}

	public void setImpIncassata(IsImpostaIncassata impIncassata) {
		this.impIncassata = impIncassata;
	}

	public DichiarazioneDTO getDichDTO() {
		return dichDTO;
	}

	public void setDichDTO(DichiarazioneDTO dichDTO) {
		this.dichDTO = dichDTO;
	}

	public String getDescrTitoloSoc() {
		return descrTitoloSoc;
	}

	public void setDescrTitoloSoc(String descrTitoloSoc) {
		this.descrTitoloSoc = descrTitoloSoc;
	}

	public HashMap<String, String> getHashParametri() {
		return hashParametri;
	}

	public void setHashParametri(HashMap<String, String> hashParametri) {
		this.hashParametri = hashParametri;
	}

	public IsConfig getLogo() {
		return logo;
	}

	public void setLogo(IsConfig logo) {
		this.logo = logo;
	}

}
