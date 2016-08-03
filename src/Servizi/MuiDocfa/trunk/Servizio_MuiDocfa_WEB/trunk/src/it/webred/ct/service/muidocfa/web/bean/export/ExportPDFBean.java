package it.webred.ct.service.muidocfa.web.bean.export;

import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.DocfaIciReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.QuadroTarsuDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SitTIciOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.UnitaImmobiliareIciDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.DocfaTarReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.SitTTarOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.UnitaImmobiliareTarDTO;
import it.webred.ct.data.model.diagnostiche.DocfaIciReport;
import it.webred.ct.data.model.diagnostiche.DocfaIciReportSogg;
import it.webred.ct.data.model.diagnostiche.DocfaTarReport;
import it.webred.ct.data.model.diagnostiche.DocfaTarReportSogg;
import it.webred.ct.data.model.docfa.DocfaDatiMetrici;
import it.webred.ct.data.model.docfa.DocfaInParteDueH;
import it.webred.ct.data.model.docfa.DocfaIntestati;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;
import it.webred.ct.service.muidocfa.web.bean.ici.DetailBean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

public class ExportPDFBean extends MuiDocfaBaseBean {

	private String dataFornitura;
	private String titolo;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
	Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);

	
	public void anomalieIciExportToPdf() {

		titolo = "Lista DOCFA con ANOMALIE in data " + dataFornitura;
		String nomeFilePdf = "ListaDOCFAanomalieICI";

		try {

			Document document = new Document(PageSize.A4, 10, 10, 10, 10);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			PdfWriter.getInstance(document, baos);
			document.open();

			document.addTitle("pdf");
			document.addSubject("pdf");
			document.addAuthor("Servizio MUI DOCFA");
			document.addCreator("Servizio MUI DOCFA");

			this.addTitolo(document);
			RicercaDocfaReportDTO dto = new RicercaDocfaReportDTO();
			
			dto.setFornitura(sdf.parse(dataFornitura));
			dto.setFlgAnomali("1");
			dto.setFlgElaborati("1");
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			dataIn.setRicerca(dto);
			fillEnte(dataIn);
			List<DocfaIciReport> lista = docfaIciReportService
					.searchReport(dataIn);
			this.addListaDocfaIci(document, lista);

			document.close();

			if (baos != null)
				this.showPdf(baos, nomeFilePdf);
		} catch (Throwable t) {
			super.addErrorMessage("export.pdf.error", t.getMessage());
			t.printStackTrace();
		}

	}

	public void anomalieTarsuExportToPdf() {

		titolo = "Lista DOCFA con ANOMALIE in data " + dataFornitura;
		String nomeFilePdf = "ListaDOCFAanomalieTARSU";

		try {

			Document document = new Document(PageSize.A4, 10, 10, 10, 10);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			PdfWriter.getInstance(document, baos);
			document.open();

			document.addTitle("pdf");
			document.addSubject("pdf");
			document.addAuthor("Servizio MUI DOCFA");
			document.addCreator("Servizio MUI DOCFA");

			this.addTitolo(document);
			RicercaDocfaReportDTO dto = new RicercaDocfaReportDTO();
			
			dto.setFornitura(sdf.parse(dataFornitura));
			dto.setFlgAnomali("1");
			dto.setFlgElaborati("1");

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(dto);
			List<DocfaTarReport> lista = docfaTarReportService
					.searchReport(dataIn);
			this.addListaDocfaTarsu(document, lista);

			document.close();

			if (baos != null)
				this.showPdf(baos, nomeFilePdf);
		} catch (Throwable t) {
			super.addErrorMessage("export.pdf.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	
	public void dettaglioDocfaIciExportToPdf() {

		titolo = "Dettaglio DOCFA";
		String nomeFilePdf = "DettaglioDOCFA";

		try {

			Document document = new Document(PageSize.A4.rotate(), 10, 10, 10,
					10);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			PdfWriter.getInstance(document, baos);
			document.open();

			document.addTitle("pdf");
			document.addSubject("pdf");
			document.addAuthor("Servizio MUI DOCFA");
			document.addCreator("Servizio MUI DOCFA");

			this.addTitolo(document);
			DetailBean dBean = (DetailBean) getBeanReference("iciDetailBean");
			this.addDettaglioDocfaIci(document, dBean.getDto());
			this.addUnitaImmobiliareIci(document, dBean.getSelectedUI());

			document.close();

			if (baos != null)
				this.showPdf(baos, nomeFilePdf);
		} catch (Throwable t) {
			super.addErrorMessage("export.pdf.error", t.getMessage());
			t.printStackTrace();
		}

	}

	public void dettaglioDocfaTarsuExportToPdf() {

		titolo = "Dettaglio DOCFA";
		String nomeFilePdf = "DettaglioDOCFA";

		try {

			Document document = new Document(PageSize.A4.rotate(), 10, 10, 10,
					10);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			PdfWriter.getInstance(document, baos);
			document.open();

			document.addTitle("pdf");
			document.addSubject("pdf");
			document.addAuthor("Servizio MUI DOCFA");
			document.addCreator("Servizio MUI DOCFA");

			this.addTitolo(document);
			it.webred.ct.service.muidocfa.web.bean.tarsu.DetailBean dBean = (it.webred.ct.service.muidocfa.web.bean.tarsu.DetailBean) getBeanReference("tarDetailBean");
			this.addDettaglioDocfaTarsu(document, dBean.getDto());
			this.addUnitaImmobiliareTarsu(document, dBean.getSelectedUI());

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

	private void addListaDocfaIci(Document document, List<DocfaIciReport> lista)
			throws DocumentException {

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);

		PdfPTable tableDocfa = new PdfPTable(6);
		tableDocfa.setWidthPercentage(100);
		tableDocfa.setWidths(new float[] { 12, 15, 10, 10, 12, 41 });
		tableDocfa.getDefaultCell().setMinimumHeight(20);

		tableDocfa.addCell(getCell(tableDocfa, "Protocollo", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Data registrazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Foglio", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Particella", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Subalterno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Anomalia", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (DocfaIciReport report : lista) {

			tableDocfa.addCell(getCell(tableDocfa, report.getProtocolloDocfa(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getDataDocfaToDate(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getFoglio(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getParticella(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getSubalterno(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getAnnotazioni() != null? report.getAnnotazioni()
					.replaceAll("@", ";"): "", normalFont, BaseColor.WHITE, null));

		}

		document.add(tableDocfa);
	}

	private void addListaDocfaTarsu(Document document,
			List<DocfaTarReport> lista) throws DocumentException {

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);

		PdfPTable tableDocfa = new PdfPTable(6);
		tableDocfa.setWidthPercentage(100);
		tableDocfa.setWidths(new float[] { 12, 15, 10, 10, 12, 41 });
		tableDocfa.getDefaultCell().setMinimumHeight(20);

		tableDocfa.addCell(getCell(tableDocfa, "Protocollo", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Data registrazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Foglio", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Particella", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Subalterno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDocfa.addCell(getCell(tableDocfa, "Anomalia", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (DocfaTarReport report : lista) {

			tableDocfa.addCell(getCell(tableDocfa, report.getProtocolloDocfa(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getDataDocfaToDate(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getFoglio(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getParticella(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getSubalterno(),
					normalFont, BaseColor.WHITE, null));

			tableDocfa.addCell(getCell(tableDocfa, report.getAnnotazioni() != null? report.getAnnotazioni()
					.replaceAll("@", ";"): "", normalFont, BaseColor.WHITE, null));

		}

		document.add(tableDocfa);
	}

	private void addDettaglioDocfaIci(Document document, DocfaIciReportDTO dto)
			throws DocumentException {


		if ("1".equals(dto.getDocfaIciReport().getFlgAnomalie())) {
			String anomalie = "Sono presenti delle anomalie: ";
			dto.getDocfaIciReport().setAnnotazioni(
					dto.getDocfaIciReport().getAnnotazioni().replaceAll("@",
							";"));
			anomalie += dto.getDocfaIciReport().getAnnotazioni();
			document.add(new Paragraph(anomalie, normalFont));
			Paragraph p = new Paragraph();
			this.addEmptyLine(p, 1);
			document.add(p);
		}

		// INIZIO TABELLA DETTAGLIO
		PdfPTable tableDettDocfa = this.getHeaderTableDettaglioDocfa();
		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaIciReport()
				.getFornitura(), normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaIciReport()
				.getProtocolloDocfa(), normalFont, BaseColor.WHITE,
				new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaIciReport()
				.getDataVariazione(), normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa
				.addCell(getCell(tableDettDocfa, dto.getCausaleDocfaExt(), normalFont, BaseColor.WHITE,
						new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaIciReport()
				.getUiuSoppresse().toString(), normalFont, BaseColor.WHITE,
				new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaIciReport()
				.getUiuVariate().toString(), normalFont, BaseColor.WHITE,
				new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaIciReport()
				.getUiuCostituite().toString(), normalFont, BaseColor.WHITE,
				new Integer(0)));

		document.add(tableDettDocfa);
		// FINE TABELLA DETTAGLIO

		Paragraph p = new Paragraph();
		this.addEmptyLine(p, 1);
		document.add(p);

		// INIZIO TABELLA DICHIARANTI
		PdfPTable tableDichDocfa = new PdfPTable(6);
		tableDichDocfa.setWidthPercentage(100);
		tableDichDocfa.setWidths(new float[] { 15, 15, 15, 15, 30, 10 });
		tableDichDocfa.getDefaultCell().setMinimumHeight(20);

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Lista Dichiaranti",
				boldFont, BaseColor.GRAY, null, new Integer(6)));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Cognome", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Nome", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Comune", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Provincia", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "CAP", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaIciReport().getDicCognome(),
				normalFont, BaseColor.WHITE, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaIciReport().getDicNome(),
				normalFont, BaseColor.WHITE, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaIciReport().getDicResCom(),
				normalFont, BaseColor.WHITE, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaIciReport().getDicResProv(),
				normalFont, BaseColor.WHITE, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaIciReport().getDicResIndir()
				+ " " + dto.getDocfaIciReport().getDicResNumciv(), normalFont, BaseColor.WHITE,
				null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaIciReport().getDicResCap(),
				normalFont, BaseColor.WHITE, null));

		document.add(tableDichDocfa);
		// FINE TABELLA DICHIARANTI

		document.add(p);

		// INIZIO TABELLA TITOLARI
		PdfPTable tableTitDocfa = new PdfPTable(8);
		tableTitDocfa.setWidthPercentage(100);
		tableTitDocfa.setWidths(new float[] { 15, 15, 15, 15, 10, 10, 10, 10 });
		tableTitDocfa.getDefaultCell().setMinimumHeight(20);

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Lista Titolari dichiarati nel DOCFA",
				boldFont, BaseColor.GRAY, null, new Integer(8)));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Cognome", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Nome", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Denominazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Sede", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Comune di nascita",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Data di nascita",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Codice fiscale",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Partita IVA", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (DocfaIntestati i : dto.getListaIntestati()) {

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getCognome(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getNome(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getDenominazione(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getSede(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getComuneNascita(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa
					.addCell(getCell(tableTitDocfa, i.getDataNascitaToDate(),
							normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getCodiceFiscale(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getPartitaIva(),
					normalFont, BaseColor.WHITE, null));
		}

		document.add(tableTitDocfa);
		// FINE TABELLA TITOLARI
		
		document.add(p);
		
		//INIZIO SEZIONE QUADRI VAL.IMPONIBILI
		addTableValImponibiliPre(document, dto);
		
		document.add(p);
		
		addTableValImponibiliPost(document, dto);
		
		document.add(p);
		
		String perc = "Variazione % della somma dei valori imponibili: " + dto.getDocfaIciReport().getVarPercSumImponibile() + " %";
		document.add(new Paragraph(perc, normalFont));
		
		//FINE SEZIONE QUADRI VAL.IMPONIBILI
		
		this.addEmptyLine(p, 1);
		document.add(p);
	}
	
	private void addTableValImponibiliPre(Document document, DocfaIciReportDTO dto) throws DocumentException{
		
		// INIZIO TABELLA VAL.IMP PRE
		PdfPTable tableImp = new PdfPTable(7);
		tableImp.setWidthPercentage(100);
		tableImp.setWidths(new float[] { 14, 14, 14, 14, 14, 14, 14});
		tableImp.getDefaultCell().setMinimumHeight(20);

		tableImp.addCell(getCell(tableImp, "Quadro Rendite/Val.Imponibili precedenti al Docfa",
				boldFont, BaseColor.GRAY, null, new Integer(7)));

		tableImp.addCell(getCell(tableImp, "Foglio", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Particella", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Subalterno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Data Registrazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Tipo",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Rendita Cat. Prec.",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Val.Imponibile Prec.",
				boldFont, BaseColor.LIGHT_GRAY, null));

		for (DocfaIciReport i : dto.getQuadroPre()) {

			tableImp.addCell(getCell(tableImp, i.getFoglio(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getParticella(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getSubalterno(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getDataDocfaToDate(),normalFont, BaseColor.WHITE, null));
			
			tableImp.addCell(getCell(tableImp, i.getTipoOperDocfaEx(),normalFont, BaseColor.WHITE, null));

			String rendita = i.getRenditaCatasto()!=null ? i.getRenditaCatasto().toString() : "";
			tableImp.addCell(getCell(tableImp, rendita ,normalFont, BaseColor.WHITE, null));
			
			String imp = i.getImponibileUiuPre()!=null ? i.getImponibileUiuPre().toString() : "";
			tableImp.addCell(getCell(tableImp, imp ,normalFont, BaseColor.WHITE, null));
		}
		
		tableImp.addCell(getCell(tableImp, "Totale", boldFont, BaseColor.GRAY, null, new Integer(6)));
		tableImp.addCell(getCell(tableImp, dto.getDocfaIciReport().getSumImponibilePre().toString(), boldFont, BaseColor.GRAY, null, new Integer(6)));
		
	
		document.add(tableImp);
	
		
		// FINE TABELLA VAL.IMP.PRE

	}
	
	private void addTableValImponibiliPost(Document document, DocfaIciReportDTO dto) throws DocumentException{
		
		// INIZIO TABELLA VAL.IMP POST
		PdfPTable tableImp = new PdfPTable(7);
		tableImp.setWidthPercentage(100);
		tableImp.setWidths(new float[] { 14, 14, 14, 14, 14, 14, 14});
		tableImp.getDefaultCell().setMinimumHeight(20);

		tableImp.addCell(getCell(tableImp, "Quadro Rendite/Val.Imponibili successivi al Docfa",
				boldFont, BaseColor.GRAY, null, new Integer(7)));

		tableImp.addCell(getCell(tableImp, "Foglio", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Particella", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Subalterno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Data Registrazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Tipo",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Rendita Docfa",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Val.Imponibile Succ.",
				boldFont, BaseColor.LIGHT_GRAY, null));

		for (DocfaIciReport i : dto.getQuadroPost()) {

			tableImp.addCell(getCell(tableImp, i.getFoglio(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getParticella(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getSubalterno(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getDataDocfaToDate(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getTipoOperDocfaEx(),normalFont, BaseColor.WHITE, null));
			
			String rendita = i.getRenditaDocfa()!=null ? i.getRenditaDocfa().toString() : "";
			tableImp.addCell(getCell(tableImp, rendita ,normalFont, BaseColor.WHITE, null));

			String imp = i.getImponibileUiuPost()!=null ? i.getImponibileUiuPost().toString() : "";
			tableImp.addCell(getCell(tableImp, imp,normalFont, BaseColor.WHITE, null));
		}
		
		tableImp.addCell(getCell(tableImp, "Totale", boldFont, BaseColor.GRAY, null, new Integer(6)));
		tableImp.addCell(getCell(tableImp, dto.getDocfaIciReport().getSumImponibilePost().toString(), boldFont, BaseColor.GRAY, null, new Integer(6)));
	
		document.add(tableImp);
		// FINE TABELLA VAL.IMP.POST

	}
	
	private void addTableSuperficiPre(Document document, DocfaTarReportDTO dto) throws DocumentException{
		
		PdfPTable tableImp = new PdfPTable(7);
		tableImp.setWidthPercentage(100);
		tableImp.setWidths(new float[] { 14, 14, 14, 14, 14, 14, 16});
		tableImp.getDefaultCell().setMinimumHeight(20);

		tableImp.addCell(getCell(tableImp, "Quadro Superfici u.i.u precedenti al Docfa",
				boldFont, BaseColor.GRAY, null, new Integer(7)));

		tableImp.addCell(getCell(tableImp, "Foglio", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Particella", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Subalterno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Data Registrazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Tipo",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Superficie Cat. Prec.",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Superficie Dich.Tarsu Prec.",
				boldFont, BaseColor.LIGHT_GRAY, null));


		for (QuadroTarsuDTO i : dto.getQuadroPre()) {

			tableImp.addCell(getCell(tableImp, i.getReport().getFoglio(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getReport().getParticella(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getReport().getSubalterno(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getReport().getDataDocfaToDate(),normalFont, BaseColor.WHITE, null));
			
			tableImp.addCell(getCell(tableImp, i.getReport().getTipoOperDocfaEx(),normalFont, BaseColor.WHITE, null));

			String supCat = i.getReport().getSupCatasto()!=null ? "mq "+i.getReport().getSupCatasto().toString() : "";
			tableImp.addCell(getCell(tableImp, supCat ,normalFont, BaseColor.WHITE, null));
			
			List<SitTTarOggettoDTO> listaTarsu = i.getSupDichTarsu();
			String supTar = getListaLastSuperficiTarsu(listaTarsu);
			tableImp.addCell(getCell(tableImp, supTar ,normalFont, BaseColor.WHITE, null));
		}
		
		document.add(tableImp);

	}
	
	private String getListaLastSuperficiTarsu(List<SitTTarOggettoDTO> listaTarsu){
		String sup = "";
		for(int i=0; i<listaTarsu.size();i++){
			SitTTarOggetto o = listaTarsu.get(i).getSitTTarOggetto();
			
			String supTar = o.getSupTot()!=null ? "mq "+ o.getSupTot().toString() : "";
			sup += supTar;
			if(i<listaTarsu.size()-1)
				sup+=", ";
		}
		
		return sup;
	}
	
	private void addTableSuperficiPost(Document document, DocfaTarReportDTO dto) throws DocumentException{
		
		PdfPTable tableImp = new PdfPTable(9);
		tableImp.setWidthPercentage(100);
		tableImp.setWidths(new float[] { 11, 11, 11, 11, 11, 11, 11, 11, 12});
		tableImp.getDefaultCell().setMinimumHeight(20);

		tableImp.addCell(getCell(tableImp, "Quadro Superfici u.i.u. successivi al Docfa",
				boldFont, BaseColor.GRAY, null, new Integer(9)));

		tableImp.addCell(getCell(tableImp, "Foglio", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Particella", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Subalterno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Data Registrazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Tipo",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableImp.addCell(getCell(tableImp, "Superficie Docfa",
				boldFont, BaseColor.LIGHT_GRAY, null));
		
		tableImp.addCell(getCell(tableImp, "Sup.C340 (sez.C)",
				boldFont, BaseColor.LIGHT_GRAY, null));
		
		tableImp.addCell(getCell(tableImp, "Sup.C340 (ABC)",
				boldFont, BaseColor.LIGHT_GRAY, null));
		
		tableImp.addCell(getCell(tableImp, "Sup.Ultima Dich.Tarsu",
				boldFont, BaseColor.LIGHT_GRAY, null));

		for (QuadroTarsuDTO i : dto.getQuadroPost()) {

			tableImp.addCell(getCell(tableImp, i.getReport().getFoglio(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getReport().getParticella(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getReport().getSubalterno(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getReport().getDataDocfaToDate(),normalFont, BaseColor.WHITE, null));

			tableImp.addCell(getCell(tableImp, i.getReport().getTipoOperDocfaEx(),normalFont, BaseColor.WHITE, null));
			
			String supCens = i.getReport().getSupDocfaCens()!=null ? "mq "+i.getReport().getSupDocfaCens().toString() : "";
			tableImp.addCell(getCell(tableImp, supCens ,normalFont, BaseColor.WHITE, null));
			
			String supC= i.getReport().getSupDocfaTarsu()!=null ? "mq "+i.getReport().getSupDocfaTarsu().toString() : "";
			tableImp.addCell(getCell(tableImp, supC ,normalFont, BaseColor.WHITE, null));
			
			String supABC = i.getReport().getSupC340Abc()!=null ? "mq "+i.getReport().getSupC340Abc().toString() : "";
			tableImp.addCell(getCell(tableImp, supABC ,normalFont, BaseColor.WHITE, null));
			
			List<SitTTarOggettoDTO> listaTarsu = i.getSupDichTarsu();
			String supTar = getListaLastSuperficiTarsu(listaTarsu);
			tableImp.addCell(getCell(tableImp, supTar ,normalFont, BaseColor.WHITE, null));
		}
		
		document.add(tableImp);

	}

	private void addDettaglioDocfaTarsu(Document document, DocfaTarReportDTO dto)
			throws DocumentException {

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);

		if ("1".equals(dto.getDocfaTarReport().getFlgAnomalie())) {
			String anomalie = "Sono presenti delle anomalie: ";
			dto.getDocfaTarReport().setAnnotazioni(
					dto.getDocfaTarReport().getAnnotazioni().replaceAll("@",
							";"));
			anomalie += dto.getDocfaTarReport().getAnnotazioni();
			document.add(new Paragraph(anomalie, normalFont));
			Paragraph p = new Paragraph();
			this.addEmptyLine(p, 1);
			document.add(p);
		}

		// INIZIO TABELLA DETTAGLIO
		PdfPTable tableDettDocfa = this.getHeaderTableDettaglioDocfa();

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaTarReport()
				.getFornitura(), normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaTarReport()
				.getProtocolloDocfa(), normalFont, BaseColor.WHITE,
				new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaTarReport()
				.getDataVariazione(), normalFont, BaseColor.WHITE, new Integer(0)));

		
		tableDettDocfa
				.addCell(getCell(tableDettDocfa, dto.getCausaleDocfaExt(), normalFont, BaseColor.WHITE,
						new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaTarReport()
				.getUiuSoppresse().toString(), normalFont, BaseColor.WHITE,
				new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaTarReport()
				.getUiuVariate().toString(), normalFont, BaseColor.WHITE,
				new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaTarReport()
				.getUiuCostituite().toString(), normalFont, BaseColor.WHITE,
				new Integer(0)));

		document.add(tableDettDocfa);
		// FINE TABELLA DETTAGLIO
		

		Paragraph p = new Paragraph();
		this.addEmptyLine(p, 1);
		document.add(p);

		// INIZIO TABELLA DICHIARANTI
		PdfPTable tableDichDocfa = new PdfPTable(6);
		tableDichDocfa.setWidthPercentage(100);
		tableDichDocfa.setWidths(new float[] { 15, 15, 15, 15, 30, 10 });
		tableDichDocfa.getDefaultCell().setMinimumHeight(20);

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Lista Dichiaranti",
				boldFont, BaseColor.GRAY, null, new Integer(6)));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Cognome", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Nome", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Comune", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Provincia", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, "CAP", boldFont,
				BaseColor.LIGHT_GRAY, null));


		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaTarReport().getDicCognome(),
				normalFont, BaseColor.WHITE, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaTarReport().getDicNome(),
				normalFont, BaseColor.WHITE, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaTarReport().getDicResCom(),
				normalFont, BaseColor.WHITE, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaTarReport().getDicResProv(),
				normalFont, BaseColor.WHITE, null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaTarReport().getDicResIndir()
				+ " " + dto.getDocfaTarReport().getDicResNumciv(), normalFont, BaseColor.WHITE,
				null));

		tableDichDocfa.addCell(getCell(tableDichDocfa, dto.getDocfaTarReport().getDicResCap(),
				normalFont, BaseColor.WHITE, null));

		document.add(tableDichDocfa);
		// FINE TABELLA DICHIARANTI

		document.add(p);

		// INIZIO TABELLA TITOLARI
		PdfPTable tableTitDocfa = new PdfPTable(8);
		tableTitDocfa.setWidthPercentage(100);
		tableTitDocfa.setWidths(new float[] { 15, 15, 15, 15, 10, 10, 10, 10 });
		tableTitDocfa.getDefaultCell().setMinimumHeight(20);

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Lista Titolari dichiarati nel DOCFA",
				boldFont, BaseColor.GRAY, null, new Integer(8)));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Cognome", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Nome", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Denominazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Sede", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Comune di nascita",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Data di nascita",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Codice fiscale",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitDocfa.addCell(getCell(tableTitDocfa, "Partita IVA", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (DocfaIntestati i : dto.getListaIntestati()) {

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getCognome(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getNome(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getDenominazione(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getSede(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getComuneNascita(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa
					.addCell(getCell(tableTitDocfa, i.getDataNascitaToDate(),
							normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getCodiceFiscale(),
					normalFont, BaseColor.WHITE, null));

			tableTitDocfa.addCell(getCell(tableTitDocfa, i.getPartitaIva(),
					normalFont, BaseColor.WHITE, null));
		}

		document.add(tableTitDocfa);
		// FINE TABELLA TITOLARI
		
		document.add(p);
		
		//INIZIO SEZIONE SUPERFICI
		addTableSuperficiPre(document, dto);
		
		document.add(p);
		
		addTableSuperficiPost(document, dto);
		//FINE SEZIONE SUPERFICI

		this.addEmptyLine(p, 1);
		document.add(p);
	}
	
	private PdfPTable getHeaderTableDettaglioDocfa() throws DocumentException{
	
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);

		// INIZIO TABELLA DETTAGLIO
		PdfPTable tableDettDocfa = new PdfPTable(7);
		tableDettDocfa.setWidthPercentage(100);
		tableDettDocfa.setWidths(new float[] { 10, 10, 10, 40, 10, 10, 10});
		tableDettDocfa.getDefaultCell().setMinimumHeight(20);

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Data fornitura",
				boldFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Protocollo", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Data variazione",
				boldFont, BaseColor.WHITE, new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, "Causale", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "UIU Soppresse",
				boldFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "UIU Variate", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "UIU Costituite",
				boldFont, BaseColor.WHITE, new Integer(0)));

		return tableDettDocfa;
	}

	private void addUnitaImmobiliareIci(Document document,
			UnitaImmobiliareIciDTO dto) throws DocumentException {

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);

		document.add(new Paragraph("Situazione censuaria dichiarata nel DOCFA",
				boldFont));

		// INIZIO TABELLA DETTAGLIO
		PdfPTable tableDettDocfa = new PdfPTable(10);
		tableDettDocfa.setWidthPercentage(100);
		tableDettDocfa.setWidths(new float[] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10 });
		tableDettDocfa.getDefaultCell().setMinimumHeight(20);
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, "Data registrazione",
				boldFont, BaseColor.WHITE, new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, "Tipo operazione",
				boldFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Sezione", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Foglio", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Particella", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Subalterno", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Categoria", boldFont,
				BaseColor.WHITE, new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, "Consistenza", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Classe", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Rendita", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaIciReportDTO().getDocfaIciReport()
				.getDataDocfaToDate(), normalFont, BaseColor.WHITE,
				new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaIciReportDTO().getDocfaIciReport().getTipoOperDocfaEx(), 
				normalFont, BaseColor.WHITE,
				new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaIciReportDTO().getDocfaIciReport().getSezione(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaIciReportDTO().getDocfaIciReport().getFoglio(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaIciReportDTO().getDocfaIciReport().getParticella(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaIciReportDTO().getDocfaIciReport().getSubalterno(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		String categoria = dto.getDocfaIciReportDTO().getDocfaIciReport().getCategoriaDocfa();
		tableDettDocfa.addCell(getCell(tableDettDocfa, categoria!=null ? categoria : "", normalFont,
				BaseColor.WHITE, new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaIciReportDTO().getDocfaIciReport().getConsistenzaDocfa(), normalFont,
				BaseColor.WHITE, new Integer(0)));
		
		if ("1".equals(dto.getDocfaIciReportDTO().getDocfaIciReport()
				.getFlgAnomaliaClasse()))
			tableDettDocfa.addCell(getCell(tableDettDocfa, dto
					.getDocfaIciReportDTO().getDocfaIciReport()
					.getClasseDocfa()
					+ "("
					+ dto.getDocfaIciReportDTO().getDocfaIciReport()
							.getClasseMin() + ")", normalFont, BaseColor.WHITE,
					new Integer(0)));
		else
			tableDettDocfa.addCell(getCell(tableDettDocfa, dto
					.getDocfaIciReportDTO().getDocfaIciReport()
					.getClasseDocfa(), normalFont, BaseColor.WHITE,
					new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "  "
				+ dto.getDocfaIciReportDTO().getDocfaIciReport()
						.getRenditaDocfa(), normalFont, BaseColor.WHITE,
				new Integer(0)));

		document.add(tableDettDocfa);
		// FINE TABELLA DETTAGLIO

		Paragraph p1 = new Paragraph();
		this.addEmptyLine(p1, 1);
		document.add(p1);

		// INIZIO TABELLA ULTERIORI DOCFA
		PdfPTable tableUlDocfa = new PdfPTable(10);
		tableUlDocfa.setWidthPercentage(100);
		tableUlDocfa.setWidths(new float[] { 10, 10, 10, 10, 10, 10, 10, 10,
				10, 10 });
		tableUlDocfa.getDefaultCell().setMinimumHeight(20);

		tableUlDocfa
				.addCell(getCell(
						tableUlDocfa,
						"Lista ulteriori docfa presentati per l'immobile corrente",
						boldFont, BaseColor.GRAY, null, new Integer(10)));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Data fornitura", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Protocollo", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Data registrazione",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Tipo operazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "UIU Soppresse", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "UIU Variate", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "UIU Costituite", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Categoria", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Classe", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Rendita", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (DocfaIciReportDTO reportDTO : dto.getListaDocfaPerImmobileDTO()) {

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaIciReport().getFornitura(), normalFont,
					BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaIciReport().getProtocolloDocfa(), normalFont,
					BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaIciReport().getDataDocfaToDate(), normalFont,
					BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaIciReport().getTipoOperDocfaEx(), normalFont,
					BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaIciReport().getUiuSoppresse().toString(),
					normalFont, BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaIciReport().getUiuVariate().toString(),
					normalFont, BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaIciReport().getUiuCostituite().toString(),
					normalFont, BaseColor.WHITE, null));

			String cat = reportDTO.getDocfaIciReport().getCategoriaDocfa();
			tableUlDocfa.addCell(getCell(tableUlDocfa, cat!=null ? cat : "",
					normalFont,BaseColor.WHITE, null));

			if ("1".equals(dto.getDocfaIciReportDTO().getDocfaIciReport()
					.getFlgAnomaliaClasse()))
				tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
						.getDocfaIciReport().getClasseDocfa()
						+ "("
						+ dto.getDocfaIciReportDTO().getDocfaIciReport()
								.getClasseMin() + ")", normalFont,
						BaseColor.WHITE, null));
			else
				tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
						.getDocfaIciReport().getClasseDocfa(), normalFont,
						BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, "  "
					+ reportDTO.getDocfaIciReport().getRenditaDocfa(),
					normalFont, BaseColor.WHITE, null));
		}
		document.add(tableUlDocfa);
		// FINE TABELLA ULTERIORI DOCFA

		Paragraph p2 = new Paragraph();
		this.addEmptyLine(p2, 2);
		document.add(p2);

		document.add(new Paragraph("Situazione catastale alla data del DOCFA", boldFont));

		// INIZIO TABELLA CATASTO
		PdfPTable tableDettCatasto = new PdfPTable(4);
		tableDettCatasto.setWidthPercentage(100);
		tableDettCatasto.setWidths(new float[] { 25, 25, 25, 25 });
		tableDettCatasto.getDefaultCell().setMinimumHeight(20);

		tableDettCatasto.addCell(getCell(tableDettCatasto, "Presenza UIU in Catasto",
				boldFont, BaseColor.WHITE, new Integer(0)));
		
		tableDettCatasto.addCell(getCell(tableDettCatasto, "Categoria",
				boldFont, BaseColor.WHITE, new Integer(0)));

		tableDettCatasto.addCell(getCell(tableDettCatasto, "Classe", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettCatasto.addCell(getCell(tableDettCatasto, "Rendita", boldFont,
				BaseColor.WHITE, new Integer(0)));
		
		tableDettCatasto.addCell(getCell(tableDettCatasto, "1".equals(dto.getDocfaIciReportDTO()
				.getDocfaIciReport().getFlgUiuCatasto()) ? "SI" : "NO", normalFont,
				BaseColor.WHITE, null));

		String catCat = dto.getDocfaIciReportDTO().getDocfaIciReport().getCategoriaCatasto();
		tableDettCatasto.addCell(getCell(tableDettCatasto,catCat!=null ? catCat : "", normalFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettCatasto.addCell(getCell(tableDettCatasto, dto
				.getDocfaIciReportDTO().getDocfaIciReport().getClasseCatasto(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettCatasto.addCell(getCell(tableDettCatasto, "  "
				+ dto.getDocfaIciReportDTO().getDocfaIciReport()
						.getRenditaCatasto(), normalFont, BaseColor.WHITE,
				new Integer(0)));

		document.add(tableDettCatasto);
		// FINE TABELLA CATASTO

		document.add(p1);

		// INIZIO TABELLA TITOLARI A CATASTO
		PdfPTable tableTitCatasto = new PdfPTable(13);
		tableTitCatasto.setWidthPercentage(100);
		tableTitCatasto.setWidths(new float[] { 7, 7, 20, 7, 7, 9, 4, 7, 9, 6,
				6, 6, 6});
		tableTitCatasto.getDefaultCell().setMinimumHeight(20);

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Lista dei titolari a catasto, nell'anno del DOCFA", boldFont, BaseColor.GRAY, null,
				new Integer(13)));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Tipo soggetto",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Cod.Fiscale/Partita IVA",
				boldFont, BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto, "Denominazione",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Inizio possesso",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Fine possesso",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Titolo",
				boldFont, BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto, "%",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Residenza indirizzo catastale", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Residenza indirizzo Docfa", boldFont, BaseColor.LIGHT_GRAY,
				null));

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Dichiarazione ICI", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Dichiarazione ICI per UIU", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Dichiarazione ICI per Civico", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Familiari", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (DocfaIciReportSogg sogg : dto.getListaSoggetti()) {

			if ("P".equals(sogg.getFlgPersFisica())) {
				tableTitCatasto.addCell(getCell(tableTitCatasto, "Fisico",
						normalFont, BaseColor.WHITE, null));
				
				tableTitCatasto.addCell(getCell(tableTitCatasto, sogg
						.getCodiFisc(), normalFont, BaseColor.WHITE, null));

				tableTitCatasto.addCell(getCell(tableTitCatasto, sogg
						.getRagiSoci()
						+ " " + sogg.getNome(), normalFont, BaseColor.WHITE,
						null));
			} else {
				tableTitCatasto.addCell(getCell(tableTitCatasto, "Giuridico",
						normalFont, BaseColor.WHITE, null));
				
				tableTitCatasto.addCell(getCell(tableTitCatasto, sogg
						.getCodiPiva(), normalFont, BaseColor.WHITE, null));

				tableTitCatasto.addCell(getCell(tableTitCatasto, sogg
						.getRagiSoci(), normalFont, BaseColor.WHITE, null));
			}

			tableTitCatasto
					.addCell(getCell(tableTitCatasto, sogg.getId().getDataInizioPossesso(), normalFont,
							BaseColor.WHITE, null));

			tableTitCatasto.addCell(getCell(tableTitCatasto, sogg.getId().getDataFinePossesso(), normalFont, BaseColor.WHITE, null));

			tableTitCatasto.addCell(getCell(tableTitCatasto, sogg.getTitolo(), normalFont, BaseColor.WHITE, null));
			
			tableTitCatasto.addCell(getCell(tableTitCatasto, sogg.getPercPoss()
					+ "%", normalFont, BaseColor.WHITE, null));

			tableTitCatasto.addCell(getCell(tableTitCatasto, "1".equals(sogg
					.getFlgResidIndCat3112()) ? "SI" : "NO", normalFont,
					BaseColor.WHITE, null));

			tableTitCatasto.addCell(getCell(tableTitCatasto, "1".equals(sogg
					.getFlgResidIndDcf3112()) ? "SI" : "NO", normalFont,
					BaseColor.WHITE, null));

			String und = "1".equals(sogg.getFlgTitIciAnte())? "Precedente": ("1".equals(sogg.getFlgTitIciPost())?"Successivo":"Assente");
			und = "1".equals(sogg.getFlgTitIciAnte()) && "1".equals(sogg.getFlgTitIciPost())?"Precedente e Succesivo":und;
			tableTitCatasto.addCell(getCell(tableTitCatasto, und, normalFont,
					BaseColor.WHITE, null));

			String uiu = "1".equals(sogg.getFlgTitIciUiuAnte())? "Precedente": ("1".equals(sogg.getFlgTitIciUiuPost())?"Successivo":"Assente");
			uiu = "1".equals(sogg.getFlgTitIciUiuAnte()) && "1".equals(sogg.getFlgTitIciUiuPost())?"Precedente e Succesivo":uiu;
			tableTitCatasto.addCell(getCell(tableTitCatasto, uiu, normalFont,
					BaseColor.WHITE, null));
			
			String civ = "1".equals(sogg.getFlgTitIciCivAnte())? "Precedente": ("1".equals(sogg.getFlgTitIciCivPost())?"Successivo":"Assente");
			civ = "1".equals(sogg.getFlgTitIciCivAnte()) && "1".equals(sogg.getFlgTitIciCivPost())?"Precedente e Succesivo":civ;
			tableTitCatasto.addCell(getCell(tableTitCatasto, civ, normalFont,
					BaseColor.WHITE, null));
			
			tableTitCatasto.addCell(getCell(tableTitCatasto, sogg.getNumFamiliari(), normalFont,
					BaseColor.WHITE, null));
		}
		document.add(tableTitCatasto);
		// FINE TABELLA TITOLARI A CATASTO

		document.add(p2);
		document.add(new Paragraph("Indirizzi associati all'immobile", boldFont));
		document.add(p1);

		PdfPTable tableIndirizzi = new PdfPTable(5);
		tableIndirizzi.setWidthPercentage(100);
		tableIndirizzi.setWidths(new float[] { 26, 2, 50, 2, 20 });
		tableIndirizzi.getDefaultCell().setMinimumHeight(20);
		tableIndirizzi.getDefaultCell().setBorder(0);
		/***
		 * creazione tre tabelle affiancate: itext tende ad allungare l'ultima
		 * riga per renderle uguali, il parametro extendLastRow = false; pare
		 * non funzionare ho ovviato aggiungendo celle invisibili
		 ***/

		// INIZIO TABELLA INDIRIZZI DOCFA
		PdfPTable tableIndDocfa = new PdfPTable(3);
		tableIndDocfa.setWidthPercentage(100);
		tableIndDocfa.setWidths(new float[] { 60, 20, 20 });
		tableIndDocfa.getDefaultCell().setMinimumHeight(20);

		tableIndDocfa.addCell(getCell(tableIndDocfa, "Docfa", boldFont,
				BaseColor.GRAY, null, new Integer(3)));

		tableIndDocfa.addCell(getCell(tableIndDocfa, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableIndDocfa.addCell(getCell(tableIndDocfa, "Num.Civ", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableIndDocfa.addCell(getCell(tableIndDocfa, "Presenza in Toponomastica", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIndDocfa.addCell(getCell(tableIndDocfa,
				(dto.getDocfaIciReportDTO().getDocfaIciReport()
						.getPrefissoViaDocfa() != null ? dto
						.getDocfaIciReportDTO().getDocfaIciReport()
						.getPrefissoViaDocfa() : "")
						+ " "
						+ (dto.getDocfaIciReportDTO().getDocfaIciReport()
								.getViaDocfa() != null ? dto
								.getDocfaIciReportDTO().getDocfaIciReport()
								.getViaDocfa() : ""),
				normalFont, BaseColor.WHITE, null));
		
		tableIndDocfa.addCell(getCell(tableIndDocfa, 
				(dto.getDocfaIciReportDTO().getDocfaIciReport()
								.getCiviciDocfa() != null ? dto
								.getDocfaIciReportDTO().getDocfaIciReport()
								.getCiviciDocfa().replaceAll("@", ", ") : ""),
				normalFont, BaseColor.WHITE, null));

		tableIndDocfa.addCell(getCell(tableIndDocfa, "1".equals(dto.getDocfaIciReportDTO()
				.getDocfaIciReport().getFlgIndirizzoInToponomastica()) ? "SI" : "NO", normalFont,
				BaseColor.WHITE, null));
				
		
		tableIndDocfa.addCell(getCell(tableIndDocfa, "", boldFont,
				BaseColor.WHITE, new Integer(0)));
		// FINE TABELLA INDIRIZZI DOCFA

		// INIZIO TABELLA INDIRIZZI CATASTO
		PdfPTable tableIndCatasto = new PdfPTable(4);
		tableIndCatasto.setWidthPercentage(100);
		tableIndCatasto.setWidths(new float[] { 40, 20, 20, 20 });
		tableIndCatasto.getDefaultCell().setMinimumHeight(20);

		tableIndCatasto.addCell(getCell(tableIndCatasto, "Catasto", boldFont,
				BaseColor.GRAY, null, new Integer(4)));

		tableIndCatasto.addCell(getCell(tableIndCatasto, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableIndCatasto.addCell(getCell(tableIndCatasto, "Num.Civ", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIndCatasto.addCell(getCell(tableIndCatasto, "Inizio validità",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableIndCatasto.addCell(getCell(tableIndCatasto, "Fine validità",
				boldFont, BaseColor.LIGHT_GRAY, null));

		for (IndirizzoDTO ind : dto.getListaIndirizziCatasto()) {

			tableIndCatasto.addCell(getCell(tableIndCatasto, ind
					.getIndirizzo(), normalFont, BaseColor.WHITE,null));
			tableIndCatasto.addCell(getCell(tableIndCatasto, ind
					.getCivico(), normalFont, BaseColor.WHITE,null));

			tableIndCatasto.addCell(getCell(tableIndCatasto, ind
					.getDataInizioVal(), normalFont, BaseColor.WHITE, null));

			tableIndCatasto.addCell(getCell(tableIndCatasto, ind
					.getDataFineVal(), normalFont, BaseColor.WHITE, null));

		}

		tableIndCatasto.addCell(getCell(tableIndCatasto, "", boldFont,
				BaseColor.WHITE, new Integer(0)));
		tableIndCatasto.addCell(getCell(tableIndCatasto, "", boldFont,
				BaseColor.WHITE, new Integer(0)));
		tableIndCatasto.addCell(getCell(tableIndCatasto, "", boldFont,
				BaseColor.WHITE, new Integer(0)));
		// FINE TABELLA INDIRIZZI CATASTO

		// INIZIO TABELLA INDIRIZZI ICI
		PdfPTable tableIndIci = new PdfPTable(3);
		tableIndIci.setWidthPercentage(100);
		tableIndIci.setWidths(new float[] { 50, 20, 30 });
		tableIndIci.getDefaultCell().setMinimumHeight(20);

		tableIndIci.addCell(getCell(tableIndIci, "Ici", boldFont,
				BaseColor.GRAY, null, new Integer(3)));

		tableIndIci.addCell(getCell(tableIndIci, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableIndIci.addCell(getCell(tableIndIci, "Num.Civ", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIndIci.addCell(getCell(tableIndIci, "Anno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (IndirizzoDTO ind : dto.getListaIndirizziIci()) {

			tableIndIci.addCell(getCell(tableIndIci, ind
					.getIndirizzo(), normalFont, BaseColor.WHITE, null));

			tableIndIci.addCell(getCell(tableIndIci, ind
					.getCivico(), normalFont, BaseColor.WHITE, null));
			
			tableIndIci.addCell(getCell(tableIndIci, ind.getAnno(), normalFont,
					BaseColor.WHITE, null));
		}

		tableIndIci.addCell(getCell(tableIndIci, "", boldFont, BaseColor.WHITE,
				new Integer(0)));
		tableIndIci.addCell(getCell(tableIndIci, "", boldFont, BaseColor.WHITE,
				new Integer(0)));
		// FINE TABELLA INDIRIZZI ICI
		tableIndirizzi.addCell(tableIndDocfa);
		tableIndirizzi.addCell(getCell(tableIndirizzi, "", normalFont,
				BaseColor.WHITE, new Integer(0)));
		tableIndirizzi.addCell(tableIndCatasto);
		tableIndirizzi.addCell(getCell(tableIndirizzi, "", normalFont,
				BaseColor.WHITE, new Integer(0)));
		tableIndirizzi.addCell(tableIndIci);

		document.add(tableIndirizzi);

		document.add(p1);
		document.add(new Paragraph("Riscontro dichiarazioni ICI", boldFont));
		document.add(p1);

		// INIZIO TABELLA ICI PRECEDENTI
		PdfPTable tableIciAnte = new PdfPTable(13);
		tableIciAnte.setWidthPercentage(100);
		tableIciAnte.setWidths(new float[] { 5, 7, 5, 7, 8, 8, 5, 8, 8, 6, 8,
				8, 17 });
		tableIciAnte.getDefaultCell().setMinimumHeight(20);

		tableIciAnte.addCell(getCell(tableIciAnte, "ICI precedenti al Docfa",
				boldFont, BaseColor.GRAY, null, new Integer(13)));

		tableIciAnte.addCell(getCell(tableIciAnte, "Anno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Numero", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Foglio", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Particella", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Subalterno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Categoria", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Classe", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Rendita", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "%", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Possesso", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Esenzione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Riduzione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciAnte.addCell(getCell(tableIciAnte, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (SitTIciOggettoDTO iciDto : dto.getListaOggettiIciAnte()) {

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getYeaDen(), normalFont,
					BaseColor.WHITE, null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getNumDen(), normalFont,
					BaseColor.WHITE, null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getFoglio(), normalFont,
					BaseColor.WHITE, null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getNumero(), normalFont,
					BaseColor.WHITE, null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getSub(), normalFont, BaseColor.WHITE,
					null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getCat(), normalFont, BaseColor.WHITE,
					null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getCls(), normalFont, BaseColor.WHITE,
					null));

			tableIciAnte.addCell(getCell(tableIciAnte, "  "
					+ iciDto.getSitTIciOggetto().getValImm().toString(),
					normalFont, BaseColor.WHITE, null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getPrcPoss().toString()
					+ "%", normalFont, BaseColor.WHITE, null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getMesiPos().toString(), normalFont,
					BaseColor.WHITE, null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getMesiEse().toString(), normalFont,
					BaseColor.WHITE, null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto
					.getSitTIciOggetto().getMesiRid().toString(), normalFont,
					BaseColor.WHITE, null));

			tableIciAnte.addCell(getCell(tableIciAnte, iciDto.getVia() + " "
					+ iciDto.getSitTIciOggetto().getNumCiv(), normalFont,
					BaseColor.WHITE, null));
		}

		document.add(tableIciAnte);
		// FINE TABELLA ICI PRECEDENTI

		document.add(p1);

		// INIZIO TABELLA ICI SUCCESSIVI
		PdfPTable tableIciPost = new PdfPTable(13);
		tableIciPost.setWidthPercentage(100);
		tableIciPost.setWidths(new float[] { 5, 7, 5, 7, 8, 8, 5, 8, 8, 6, 8,
				8, 17 });
		tableIciPost.getDefaultCell().setMinimumHeight(20);

		tableIciPost.addCell(getCell(tableIciPost, "ICI successivi al Docfa",
				boldFont, BaseColor.GRAY, null, new Integer(13)));

		tableIciPost.addCell(getCell(tableIciPost, "Anno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Numero", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Foglio", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Particella", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Subalterno", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Categoria", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Classe", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Rendita", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "%", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Possesso", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Esenzione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Riduzione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIciPost.addCell(getCell(tableIciPost, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (SitTIciOggettoDTO iciDto : dto.getListaOggettiIciPost()) {

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getYeaDen(), normalFont,
					BaseColor.WHITE, null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getNumDen(), normalFont,
					BaseColor.WHITE, null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getFoglio(), normalFont,
					BaseColor.WHITE, null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getNumero(), normalFont,
					BaseColor.WHITE, null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getSub(), normalFont, BaseColor.WHITE,
					null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getCat(), normalFont, BaseColor.WHITE,
					null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getCls(), normalFont, BaseColor.WHITE,
					null));

			tableIciPost.addCell(getCell(tableIciPost, "  "
					+ iciDto.getSitTIciOggetto().getValImm().toString(),
					normalFont, BaseColor.WHITE, null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getPrcPoss().toString()
					+ "%", normalFont, BaseColor.WHITE, null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getMesiPos().toString(), normalFont,
					BaseColor.WHITE, null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getMesiEse().toString(), normalFont,
					BaseColor.WHITE, null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto
					.getSitTIciOggetto().getMesiRid().toString(), normalFont,
					BaseColor.WHITE, null));

			tableIciPost.addCell(getCell(tableIciPost, iciDto.getVia() + " "
					+ iciDto.getSitTIciOggetto().getNumCiv(), normalFont,
					BaseColor.WHITE, null));
		}

		document.add(tableIciPost);
		// FINE TABELLA ICI SUCCESSIVI
	}

	private void addUnitaImmobiliareTarsu(Document document,
			UnitaImmobiliareTarDTO dto) throws DocumentException {

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

		document.add(new Paragraph("Situazione censuaria dichiarata nel DOCFA", boldFont));
		
		Paragraph p1 = new Paragraph();
		this.addEmptyLine(p1, 1);
		document.add(p1);
		
		// INIZIO TABELLA DETTAGLIO
		PdfPTable tableDettDocfa = new PdfPTable(9);
		tableDettDocfa.setWidthPercentage(100);
		tableDettDocfa.setWidths(new float[] { 11, 11, 11, 11, 11, 11, 11, 11, 11});
		tableDettDocfa.getDefaultCell().setMinimumHeight(20);
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, "Data registrazione", boldFont,
				BaseColor.WHITE, new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, "Tipo Operazione", boldFont,
				BaseColor.WHITE, new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, "Sezione", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Foglio", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Particella", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Subalterno", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Categoria", boldFont,
				BaseColor.WHITE, new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, "Consistenza", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, "Superficie", boldFont,
				BaseColor.WHITE, new Integer(0)));

		
		tableDettDocfa.addCell(getCell(tableDettDocfa, dto.getDocfaTarReportDTO().getDocfaTarReport()
				.getDataDocfaToDate(), normalFont, BaseColor.WHITE,
				new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaTarReportDTO().getDocfaTarReport().getTipoOperDocfaEx(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaTarReportDTO().getDocfaTarReport().getSezione(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaTarReportDTO().getDocfaTarReport().getFoglio(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaTarReportDTO().getDocfaTarReport().getParticella(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		String catDoc = dto.getDocfaTarReportDTO().getDocfaTarReport().getCategoriaDocfa();
		tableDettDocfa.addCell(getCell(tableDettDocfa, catDoc!=null ? catDoc : catDoc, normalFont,
				BaseColor.WHITE, new Integer(0)));
		
		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaTarReportDTO().getDocfaTarReport().getConsistenzaDocfa(), normalFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaTarReportDTO().getDocfaTarReport().getSupDocfaCens()
				+ " mq", normalFont, BaseColor.WHITE, new Integer(0)));

		document.add(tableDettDocfa);
		// FINE TABELLA DETTAGLIO

		Paragraph p2 = new Paragraph();
		this.addEmptyLine(p2, 2);
		document.add(p2);

		document.add(new Paragraph("Dettaglio superfici comma 340", boldFont));

		document.add(p1);

		// INIZIO TABELLA DATI METRICI
		PdfPTable tableDatiMetrici = new PdfPTable(3);
		tableDatiMetrici.setWidthPercentage(100);
		tableDatiMetrici.setWidths(new float[] { 33, 33, 34 });
		tableDatiMetrici.getDefaultCell().setMinimumHeight(20);

		tableDatiMetrici.addCell(getCell(tableDatiMetrici, "Dati metrici",
				boldFont, BaseColor.GRAY, null, new Integer(3)));

		tableDatiMetrici.addCell(getCell(tableDatiMetrici, "Ambiente",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableDatiMetrici.addCell(getCell(tableDatiMetrici, "Altezza", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableDatiMetrici.addCell(getCell(tableDatiMetrici, "Superficie",
				boldFont, BaseColor.LIGHT_GRAY, null));

		for (DocfaDatiMetrici datiMetrici : dto.getListaDatiMetrici()) {

			tableDatiMetrici.addCell(getCell(tableDatiMetrici, "mq "  + datiMetrici.getSuperficie()
					, normalFont, BaseColor.WHITE, null));

			tableDatiMetrici.addCell(getCell(tableDatiMetrici, datiMetrici
					.getAmbiente(), normalFont, BaseColor.WHITE, null));

			tableDatiMetrici.addCell(getCell(tableDatiMetrici, (datiMetrici.getAltezza() != null ? "cm "+datiMetrici.getAltezza()
					: ""), normalFont, BaseColor.WHITE, null));
		}
		
		tableDatiMetrici.addCell(getCell(tableDatiMetrici, "Sup.C340 calcolata come: (SupA+SupB+SupC)*0,8", boldFont, BaseColor.GRAY, null, new Integer(2)));
		tableDatiMetrici.addCell(getCell(tableDatiMetrici, "mq "  + dto.getDocfaTarReportDTO().getDocfaTarReport().getSupC340Abc().toString(), boldFont, BaseColor.GRAY, null, new Integer(0)));
	
		document.add(tableDatiMetrici);
		// FINE TABELLA DATI METRICI
		
		document.add(p2);

		document.add(new Paragraph("Lista planimetrie", boldFont));

		document.add(p1);

		// INIZIO TABELLA PLAN COMMA340
		PdfPTable tablePlanC340 = new PdfPTable(2);
		tablePlanC340.setWidthPercentage(100);
		tablePlanC340.setWidths(new float[] { 33, 67 });
		tablePlanC340.getDefaultCell().setMinimumHeight(20);

		tablePlanC340.addCell(getCell(tablePlanC340, "Comma 340", boldFont,
				BaseColor.GRAY, null, new Integer(2)));

		tablePlanC340.addCell(getCell(tablePlanC340, "Nome file", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tablePlanC340.addCell(getCell(tablePlanC340, "Descrizione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (PlanimetriaComma340DTO p340 : dto.getListaPlanimetrieC340()) {

			tablePlanC340.addCell(getCell(tablePlanC340, p340.getFile(),
					normalFont, BaseColor.WHITE, null));

			tablePlanC340.addCell(getCell(tablePlanC340, p340.getDescrizione(),
					normalFont, BaseColor.WHITE, null));

		}

		document.add(tablePlanC340);
		// FINE TABELLA PLAN COMMA340

		document.add(p1);

		// INIZIO TABELLA PLAN DOCFA
		PdfPTable tablePlanDocfa = new PdfPTable(1);
		tablePlanDocfa.setWidthPercentage(100);
		tablePlanDocfa.setWidths(new float[] { 100 });
		tablePlanDocfa.getDefaultCell().setMinimumHeight(20);

		tablePlanDocfa.addCell(getCell(tablePlanDocfa, "Docfa", boldFont,
				BaseColor.GRAY, null, new Integer(1)));

		tablePlanDocfa.addCell(getCell(tablePlanDocfa, "Nome file", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (DocfaPlanimetrie pdocfa : dto.getListaPlanimetrieDocfa()) {

			tablePlanDocfa.addCell(getCell(tablePlanDocfa, pdocfa.getId()
					.getNomePlan()
					+ "." + pdocfa.getId().getProgressivo() + ".tif",
					normalFont, BaseColor.WHITE, null));

		}

		document.add(tablePlanDocfa);
		// FINE TABELLA PLAN DOCFA

		
		document.add(new Paragraph("Dati Metrici Unità Immobilare nel DOCFA (Sez.C)", boldFont));

		PdfPTable tableABC = new PdfPTable(3);
		tableABC.setWidthPercentage(100);
		tableABC.setWidths(new float[] { 49, 2, 49 });
		tableABC.getDefaultCell().setMinimumHeight(20);
		tableABC.getDefaultCell().setBorder(0);

		// INIZIO TABELLA IMMOBILE CENSIBILE A B
		PdfPTable tableAB = new PdfPTable(5);
		tableAB.setWidthPercentage(100);
		tableAB.setWidths(new float[] { 2, 61, 10, 12, 15 });
		tableAB.getDefaultCell().setMinimumHeight(20);

		if(dto.getDatiMetriciABC() == null)
			dto.setDatiMetriciABC(new DocfaInParteDueH());
		
		tableAB.addCell(getCell(tableAB,
				"C/1, immobile censibile nel gruppo 'A' e 'B'", boldFont,
				BaseColor.GRAY, new Integer(1), new Integer(5)));

		tableAB.getDefaultCell().setBorder(0);
		tableAB.addCell(getCell(tableAB, "Abitazioni o uffici privati",
				normalFont, BaseColor.WHITE, null, new Integer(5)));

		tableAB
				.addCell(getCell(
						tableAB,
						"Alloggi collettivi, uffici pubblici, scuole o istituti di cultura",
						normalFont, BaseColor.WHITE, null, new Integer(5)));

		tableAB.addCell(getCell(tableAB, "1. VANI PRINCIPALI", boldFont,
				BaseColor.WHITE, null, new Integer(5)));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Camere, cucina, stanze, ecc.",
				normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "n."
				+ (dto.getDatiMetriciABC().getAbVaniPrincNr() != null ? dto
						.getDatiMetriciABC().getAbVaniPrincNr() : ""),
				normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "sup.utile", normalFont,
				BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(
						tableAB,
						"mq "
								+ (dto.getDatiMetriciABC()
										.getAbVaniPrincSuperfUti() != null ? dto
										.getDatiMetriciABC()
										.getAbVaniPrincSuperfUti()
										: ""), normalFont, BaseColor.WHITE,
						null));

		tableAB.addCell(getCell(tableAB, "2. ACCESSORI DIRETTI", boldFont,
				BaseColor.WHITE, null, new Integer(5)));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Bagni, W.C.", normalFont,
				BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(tableAB,
						"n."
								+ (dto.getDatiMetriciABC()
										.getAbAccessoriDir01Nr() != null ? dto
										.getDatiMetriciABC()
										.getAbAccessoriDir01Nr() : ""),
						normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "sup.utile", normalFont,
				BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB,
				"mq "
						+ (dto.getDatiMetriciABC()
								.getAbAccessoriDir01SuperfUti() != null ? dto
								.getDatiMetriciABC()
								.getAbAccessoriDir01SuperfUti() : ""),
				normalFont, BaseColor.WHITE, null));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Corridoi, ripostigli, ecc.",
				normalFont, BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(tableAB,
						"n."
								+ (dto.getDatiMetriciABC()
										.getAbAccessoriDir02Nr() != null ? dto
										.getDatiMetriciABC()
										.getAbAccessoriDir02Nr() : ""),
						normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "sup.utile", normalFont,
				BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB,
				"mq "
						+ (dto.getDatiMetriciABC()
								.getAbAccessoriDir02SuperfUti() != null ? dto
								.getDatiMetriciABC()
								.getAbAccessoriDir02SuperfUti() : ""),
				normalFont, BaseColor.WHITE, null));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB,
				"SUPERFICIE LORDA (relativa ai punti 1 e 2)", normalFont,
				BaseColor.WHITE, null, new Integer(3)));
		tableAB.addCell(getCell(tableAB,
				"mq "
						+ (dto.getDatiMetriciABC()
								.getAbVprincVaccDirSuperfLor() != null ? dto
								.getDatiMetriciABC()
								.getAbVprincVaccDirSuperfLor() : ""),
				normalFont, BaseColor.WHITE, null));

		tableAB.addCell(getCell(tableAB, "3. ACCESSORI INDIRETTI", boldFont,
				BaseColor.WHITE, null, new Integer(5)));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(
						tableAB,
						"Accessori complementari, cantine, soffitte, lavanderie e simili",
						normalFont, BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(tableAB,
						"n."
								+ (dto.getDatiMetriciABC()
										.getAbAccessoriIndirNr() != null ? dto
										.getDatiMetriciABC()
										.getAbAccessoriIndirNr() : ""),
						normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "sup.lorda", normalFont,
				BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB,
				"mq "
						+ (dto.getDatiMetriciABC()
								.getAbAccessoriIndirSuperfLor() != null ? dto
								.getDatiMetriciABC()
								.getAbAccessoriIndirSuperfLor() : ""),
				normalFont, BaseColor.WHITE, null));

		tableAB.addCell(getCell(tableAB, "4. DIPENDENZE ESCLUSIVE", boldFont,
				BaseColor.WHITE, null, new Integer(5)));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Balconi, terrazzi, portici",
				normalFont, BaseColor.WHITE, null, new Integer(2)));
		tableAB.addCell(getCell(tableAB, "sup.lorda", normalFont,
				BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(
						tableAB,
						"mq "
								+ (dto.getDatiMetriciABC()
										.getAbDipUsoEscl01SuperfLor() != null ? dto
										.getDatiMetriciABC()
										.getAbDipUsoEscl01SuperfLor()
										: ""), normalFont, BaseColor.WHITE,
						null));

		tableAB.addCell(getCell(tableAB, "5. PERTINENZE SCOPERTE ESCLUSIVE",
				boldFont, BaseColor.WHITE, null, new Integer(5)));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Giardino, cortile", normalFont,
				BaseColor.WHITE, null, new Integer(2)));
		tableAB.addCell(getCell(tableAB, "sup.lorda", normalFont,
				BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB,
				"mq "
						+ (dto.getDatiMetriciABC()
								.getAbPertUsoEscl01SuperfLor() != null ? dto
								.getDatiMetriciABC()
								.getAbPertUsoEscl01SuperfLor() : ""),
				normalFont, BaseColor.WHITE, null));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Piscina, tennis, "
				+ dto.getDatiMetriciABC().getAbPertUsoEscl02Descr(),
				normalFont, BaseColor.WHITE, null, new Integer(2)));
		tableAB.addCell(getCell(tableAB, "sup.", normalFont, BaseColor.WHITE,
				null));
		tableAB.addCell(getCell(tableAB,
				"mq "
						+ (dto.getDatiMetriciABC()
								.getAbPertUsoEscl02SuperfLor() != null ? dto
								.getDatiMetriciABC()
								.getAbPertUsoEscl02SuperfLor() : ""),
				normalFont, BaseColor.WHITE, null));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Parcheggio auto per posti",
				normalFont, BaseColor.WHITE, null, new Integer(2)));
		tableAB.addCell(getCell(tableAB, "numero", normalFont, BaseColor.WHITE,
				null));
		tableAB.addCell(getCell(tableAB, dto.getDatiMetriciABC()
				.getAbParkNrPosti() != null ? dto.getDatiMetriciABC()
				.getAbParkNrPosti().toString() : "", normalFont,
				BaseColor.WHITE, null));

		tableAB.addCell(getCell(tableAB, "6. DATI RELATIVI ALL'ALTEZZA",
				boldFont, BaseColor.WHITE, null, new Integer(5)));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Altezza media U.I.U.", normalFont,
				BaseColor.WHITE, null, new Integer(3)));
		tableAB.addCell(getCell(tableAB, "cm "
				+ (dto.getDatiMetriciABC().getAbAltezzaMediaUiu() != null ? dto
						.getDatiMetriciABC().getAbAltezzaMediaUiu() : ""),
				normalFont, BaseColor.WHITE, null));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(
						tableAB,
						"Superficie dei vani principali aventi altezze medie inferiori a cm. 230",
						normalFont, BaseColor.WHITE, null, new Integer(3)));
		tableAB
				.addCell(getCell(
						tableAB,
						"cm "
								+ (dto.getDatiMetriciABC()
										.getAbSupVprincVaccInf230cm() != null ? dto
										.getDatiMetriciABC()
										.getAbSupVprincVaccInf230cm()
										: ""), normalFont, BaseColor.WHITE,
						null));

		tableAB
				.addCell(getCell(
						tableAB,
						"7. PER LE U.I. CENSIBILI NEL GRUPPO 'B' INTEGRARE CON LE SEGUENTI INFORMAZIONI",
						boldFont, BaseColor.WHITE, null, new Integer(5)));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Piano fuori terra", normalFont,
				BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(tableAB,
						"n."
								+ (dto.getDatiMetriciABC()
										.getAbPianiFuoriTerraNr() != null ? dto
										.getDatiMetriciABC()
										.getAbPianiFuoriTerraNr() : ""),
						normalFont, BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(tableAB,
						"mc "
								+ (dto.getDatiMetriciABC()
										.getAbPianiFuoriTerraMc() != null ? dto
										.getDatiMetriciABC()
										.getAbPianiFuoriTerraMc() : ""),
						normalFont, BaseColor.WHITE, null));

		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB.addCell(getCell(tableAB, "Piano entro terra", normalFont,
				BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(tableAB,
						"n."
								+ (dto.getDatiMetriciABC()
										.getAbPianiEntroTerraNr() != null ? dto
										.getDatiMetriciABC()
										.getAbPianiEntroTerraNr() : ""),
						normalFont, BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(tableAB, "", normalFont, BaseColor.WHITE, null));
		tableAB
				.addCell(getCell(tableAB,
						"mc "
								+ (dto.getDatiMetriciABC()
										.getAbPianiEntroTerraMc() != null ? dto
										.getDatiMetriciABC()
										.getAbPianiEntroTerraMc() : ""),
						normalFont, BaseColor.WHITE, null));

		// FINE TABELLA IMMOBILE CENSIBILE A B

		// INIZIO TABELLA IMMOBILE CENSIBILE C
		PdfPTable tableC = new PdfPTable(6);
		tableC.setWidthPercentage(100);
		tableC.setWidths(new float[] { 2, 10, 37, 20, 18, 13 });
		tableC.getDefaultCell().setMinimumHeight(20);
		tableC.getDefaultCell().setBorder(0);
		tableC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		tableC.addCell(getCell(tableC, "C/2 immobile censibile nel gruppo 'C'",
				boldFont, BaseColor.GRAY, new Integer(1), new Integer(6)));

		tableC.getDefaultCell().setBorder(0);
		tableC
				.addCell(getCell(
						tableC,
						"Commerciale ed usi diversi da quelli indicati nel prospetto C/1",
						normalFont, BaseColor.WHITE, null, new Integer(6)));

		tableC.addCell(getCell(tableC, "", boldFont,
				BaseColor.WHITE, null, new Integer(6)));
		
		tableC.addCell(getCell(tableC, "1. LOCALI PRINCIPALI", boldFont,
				BaseColor.WHITE, null, new Integer(6)));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "Piano", normalFont, BaseColor.WHITE,
				null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcLocPrincPian001(), normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "lordi mq "
				+ (dto.getDatiMetriciABC().getcLocPrincSupLor01() != null ? dto
						.getDatiMetriciABC().getcLocPrincSupLor01() : ""),
				normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "di cui utili mq", normalFont,
				BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcLocPrincSupUti01() != null ? dto.getDatiMetriciABC()
				.getcLocPrincSupUti01().toString() : "", normalFont,
				BaseColor.WHITE, null));

		tableC.addCell(getCell(tableC, "2. LOCALI ACCESSORI DIRETTI", boldFont,
				BaseColor.WHITE, null, new Integer(6)));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "Piano", normalFont, BaseColor.WHITE,
				null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcLocAccDirPian001(), normalFont, BaseColor.WHITE, null));
		tableC
				.addCell(getCell(tableC,
						"lordi mq "
								+ (dto.getDatiMetriciABC()
										.getcLocAccDirSupLor01() != null ? dto
										.getDatiMetriciABC()
										.getcLocAccDirSupLor01() : ""),
						normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "di cui utili mq", normalFont,
				BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcLocAccDirSupUti01() != null ? dto.getDatiMetriciABC()
				.getcLocAccDirSupUti01().toString() : "", normalFont,
				BaseColor.WHITE, null));

		tableC.addCell(getCell(tableC, "3. LOCALI ACCESSORI INDIRETTI",
				boldFont, BaseColor.WHITE, null, new Integer(6)));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "Piano", normalFont, BaseColor.WHITE,
				null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcLocAccIndirPian001(), normalFont, BaseColor.WHITE, null));
		tableC
				.addCell(getCell(
						tableC,
						"lordi mq "
								+ (dto.getDatiMetriciABC()
										.getcLocAccIndirSupLor01() != null ? dto
										.getDatiMetriciABC()
										.getcLocAccIndirSupLor01()
										: ""), normalFont, BaseColor.WHITE,
						null));
		tableC.addCell(getCell(tableC, "di cui utili mq", normalFont,
				BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcLocAccIndirSupUti01() != null ? dto.getDatiMetriciABC()
				.getcLocAccIndirSupUti01().toString() : "", normalFont,
				BaseColor.WHITE, null));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "Piano", normalFont, BaseColor.WHITE,
				null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcLocAccIndirPian002(), normalFont, BaseColor.WHITE, null));
		tableC
				.addCell(getCell(
						tableC,
						"lordi mq "
								+ (dto.getDatiMetriciABC()
										.getcLocAccIndirSupLor02() != null ? dto
										.getDatiMetriciABC()
										.getcLocAccIndirSupLor02()
										: ""), normalFont, BaseColor.WHITE,
						null));
		tableC.addCell(getCell(tableC, "di cui utili mq", normalFont,
				BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcLocAccIndirSupUti02() != null ? dto.getDatiMetriciABC()
				.getcLocAccIndirSupUti02().toString() : "", normalFont,
				BaseColor.WHITE, null));

		tableC.addCell(getCell(tableC, "4. DIPENDENZE ESCLUSIVE", boldFont,
				BaseColor.WHITE, null, new Integer(6)));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "Balconi, terrazzi, portici",
				normalFont, BaseColor.WHITE, null, new Integer(3)));
		tableC.addCell(getCell(tableC, "sup. lorda mq", normalFont,
				BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcDipendUsoEsclSupLor01() != null ? dto.getDatiMetriciABC()
				.getcDipendUsoEsclSupLor01().toString() : "", normalFont,
				BaseColor.WHITE, null));

		tableC.addCell(getCell(tableC, "5. PERTINENZE SCOPERTE ESCLUSIVE",
				boldFont, BaseColor.WHITE, null, new Integer(6)));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "Superficie lorda", normalFont,
				BaseColor.WHITE, null, new Integer(3)));
		tableC
				.addCell(getCell(tableC, "mq", normalFont, BaseColor.WHITE,
						null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcPertUsoEsclSuperf() != null ? dto.getDatiMetriciABC()
				.getcPertUsoEsclSuperf().toString() : "", normalFont,
				BaseColor.WHITE, null));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "Parcheggio auto per posti", normalFont,
				BaseColor.WHITE, null, new Integer(3)));
		tableC.addCell(getCell(tableC, "numero", normalFont, BaseColor.WHITE,
				null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcPertScoUsoescPostiautoNr() != null ? dto
				.getDatiMetriciABC().getcPertScoUsoescPostiautoNr().toString()
				: "", normalFont, BaseColor.WHITE, null));

		tableC.addCell(getCell(tableC, "6. IL LOCALE HA ACCESSO CARRABILE",
				boldFont, BaseColor.WHITE, null, new Integer(6)));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "Il locale ha accesso carrabile",normalFont, BaseColor.WHITE, null, new Integer(3)));
		if(dto.getDatiMetriciABC().getcFlagAccessoCarrabile()!= null)
			tableC.addCell(getCell(tableC, "1".equals(dto.getDatiMetriciABC().getcFlagAccessoCarrabile()) ? "SI" : "NO", 
					normalFont,BaseColor.WHITE, null, new Integer(2)));
		else
			tableC.addCell(getCell(tableC, "", normalFont,BaseColor.WHITE, null, new Integer(2)));

		tableC.addCell(getCell(tableC, "7. DATI RELATIVI ALL'ALTEZZA",
				boldFont, BaseColor.WHITE, null, new Integer(6)));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC.addCell(getCell(tableC, "Altezza media dei locali principali",
				normalFont, BaseColor.WHITE, null, new Integer(3)));
		tableC
				.addCell(getCell(tableC, "cm", normalFont, BaseColor.WHITE,
						null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcAltezzMedLocPrinc() != null ? dto.getDatiMetriciABC()
				.getcAltezzMedLocPrinc().toString() : "", normalFont,
				BaseColor.WHITE, null));

		tableC.addCell(getCell(tableC, "", normalFont, BaseColor.WHITE, null));
		tableC
				.addCell(getCell(
						tableC,
						"Superficie dei locali principali ed accessori diretti aventi altezze medie inferiori a cm. 230",
						normalFont, BaseColor.WHITE, null, new Integer(3)));
		tableC
				.addCell(getCell(tableC, "mq", normalFont, BaseColor.WHITE,
						null));
		tableC.addCell(getCell(tableC, dto.getDatiMetriciABC()
				.getcSupVprincVaccInf230cm() != null ? dto.getDatiMetriciABC()
				.getcSupVprincVaccInf230cm().toString() : "", normalFont,
				BaseColor.WHITE, null));

		// FINE TABELLA IMMOBILE CENSIBILE C

		tableABC.addCell(tableAB);
		tableABC.addCell(getCell(tableABC, "", normalFont, BaseColor.WHITE,
				new Integer(0)));
		tableABC.addCell(tableC);

		document.add(tableABC);

		document.add(p2);
		
		document.add(new Paragraph("Controlli sui dati docfa", boldFont));
		
		document.add(p1);
		
		//INIZIO TABELLA CONTROLLI SU DOCFA
		PdfPTable tableCalcDocfa = new PdfPTable(3);
		tableCalcDocfa.setWidthPercentage(100);
		tableCalcDocfa.setWidths(new float[] { 33,33,33});
		tableCalcDocfa.getDefaultCell().setMinimumHeight(20);

		tableCalcDocfa.addCell(getCell(tableDettDocfa, "Consistenza Calcolata", boldFont,
				BaseColor.WHITE, new Integer(0)));
		
		tableCalcDocfa.addCell(getCell(tableDettDocfa,
				"Sup.C340 (Quadro C/1 e C/2)", boldFont, BaseColor.WHITE,
				new Integer(0)));
		
		tableCalcDocfa.addCell(getCell(tableDettDocfa, "Sup.dipendenze esclusive", boldFont,
				BaseColor.WHITE, new Integer(0)));
		
		tableCalcDocfa.addCell(getCell(tableDettDocfa, (dto.getDocfaTarReportDTO()
				.getDocfaTarReport().getConsCalc() != null ? dto.getDocfaTarReportDTO()
				.getDocfaTarReport().getConsCalc()+ " (" +
				dto.getDocfaTarReportDTO().getDocfaTarReport().getSupAvgMin() + " - " +
				dto.getDocfaTarReportDTO().getDocfaTarReport().getSupAvgMax() + ")" : ""), 
				normalFont, BaseColor.WHITE, null));
		
		tableCalcDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaTarReportDTO().getDocfaTarReport().getSupDocfaTarsu()
				+ " mq", normalFont, BaseColor.WHITE, new Integer(0)));
		
		tableCalcDocfa.addCell(getCell(tableDettDocfa, dto
				.getDocfaTarReportDTO().getDocfaTarReport().getSupDipendenzeEscl()
				+ " mq", normalFont, BaseColor.WHITE, new Integer(0)));
		
		document.add(tableCalcDocfa);
		//FINE TABELLA CONTROLLI SU DOCFA
		
		Paragraph p3 = new Paragraph();
		this.addEmptyLine(p3, 2);
		document.add(p3);

		// INIZIO TABELLA ULTERIORI DOCFA
		PdfPTable tableUlDocfa = new PdfPTable(11);
		tableUlDocfa.setWidthPercentage(100);
		tableUlDocfa.setWidths(new float[] { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 11 });
		tableUlDocfa.getDefaultCell().setMinimumHeight(20);

		tableUlDocfa
				.addCell(getCell(
						tableUlDocfa,
						"Lista ulteriori docfa presentati per l'immobile corrente",
						boldFont, BaseColor.GRAY, null, new Integer(11)));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Data fornitura", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Protocollo", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Data registrazione",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Tipo operazione", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "UIU Soppresse", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "UIU Variate", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "UIU Costituite", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Categoria", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Superficie", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableUlDocfa.addCell(getCell(tableUlDocfa, "Superficie calcolata ai fini Tarsu",
				boldFont, BaseColor.LIGHT_GRAY, null));
		
		tableUlDocfa.addCell(getCell(tableUlDocfa, "Consistenza Calcolata",
				boldFont, BaseColor.LIGHT_GRAY, null));

		for (DocfaTarReportDTO reportDTO : dto.getListaDocfaPerImmobileDTO()) {

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaTarReport().getFornitura(), normalFont,
					BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaTarReport().getProtocolloDocfa(), normalFont,
					BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaTarReport().getDataDocfaToDate(), normalFont,
					BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaTarReport().getTipoOperDocfaEx(), normalFont,
					BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaTarReport().getUiuSoppresse().toString(),
					normalFont, BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaTarReport().getUiuVariate().toString(),
					normalFont, BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, reportDTO
					.getDocfaTarReport().getUiuCostituite().toString(),
					normalFont, BaseColor.WHITE, null));

			String catDocUl = reportDTO.getDocfaTarReport().getCategoriaDocfa();
			tableUlDocfa.addCell(getCell(tableUlDocfa, catDocUl!=null ? catDocUl : "", 
					normalFont,BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, (reportDTO
					.getDocfaTarReport().getSupDocfaCens() != null ? reportDTO
					.getDocfaTarReport().getSupDocfaCens()
					+ " mq" : ""), normalFont, BaseColor.WHITE, null));

			tableUlDocfa.addCell(getCell(tableUlDocfa, (reportDTO
					.getDocfaTarReport().getSupDocfaTarsu() != null ? reportDTO
					.getDocfaTarReport().getSupDocfaTarsu()
					+ " mq" : ""), normalFont, BaseColor.WHITE, null));
			
			tableUlDocfa.addCell(getCell(tableUlDocfa, (reportDTO
					.getDocfaTarReport().getConsCalc() != null ? reportDTO
					.getDocfaTarReport().getConsCalc()+ " (" +
					reportDTO.getDocfaTarReport().getSupAvgMin() + " - " +
					reportDTO.getDocfaTarReport().getSupAvgMax() + ")" : ""), 
					normalFont, BaseColor.WHITE, null));
		}
		document.add(tableUlDocfa);
		// FINE TABELLA ULTERIORI DOCFA

		document.add(p2);

		document.add(new Paragraph("Situazione catastale alla data del DOCFA", boldFont));

		// INIZIO TABELLA CATASTO
		PdfPTable tableDettCatasto = new PdfPTable(3);
		tableDettCatasto.setWidthPercentage(100);
		tableDettCatasto.setWidths(new float[] { 33, 33, 34 });
		tableDettCatasto.getDefaultCell().setMinimumHeight(20);

		tableDettCatasto.addCell(getCell(tableDettCatasto, "Presenza UIU in Catasto",
				boldFont, BaseColor.WHITE, new Integer(0)));

		tableDettCatasto.addCell(getCell(tableDettCatasto, "Categoria",
				boldFont, BaseColor.WHITE, new Integer(0)));

		tableDettCatasto.addCell(getCell(tableDettCatasto, "Superficie",
				boldFont, BaseColor.WHITE, new Integer(0)));
		
		tableDettCatasto.addCell(getCell(tableDettCatasto, "1".equals(dto.getDocfaTarReportDTO()
				.getDocfaTarReport().getFlgUiuCatasto()) ? "SI" : "NO", normalFont,
				BaseColor.WHITE, null));

		String catCat = dto.getDocfaTarReportDTO().getDocfaTarReport().getCategoriaCatasto();
		tableDettCatasto.addCell(getCell(tableDettCatasto, catCat!=null ? catCat : "", normalFont,
				BaseColor.WHITE, new Integer(0)));

		tableDettCatasto.addCell(getCell(tableDettCatasto, (dto.getDocfaTarReportDTO()
						.getDocfaTarReport().getSupCatasto() != null ? dto
						.getDocfaTarReportDTO().getDocfaTarReport()
						.getSupCatasto()
						+ " mq" : ""), normalFont, BaseColor.WHITE,
						new Integer(0)));

		document.add(tableDettCatasto);
		// FINE TABELLA CATASTO

		document.add(p1);

		// INIZIO TABELLA TITOLARI A CATASTO
		PdfPTable tableTitCatasto = new PdfPTable(16);
		tableTitCatasto.setWidthPercentage(100);
		tableTitCatasto.setWidths(new float[] { 6, 6, 9, 5, 5, 15 , 4, 8, 7, 7,
				5, 5, 5, 5, 5, 5});
		tableTitCatasto.getDefaultCell().setMinimumHeight(20);

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Lista dei titolari a catasto, nell'anno del DOCFA", boldFont, BaseColor.GRAY, null,
				new Integer(16)));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Tipo soggetto",
				boldFont, BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto, "Cod.Fiscale/Partita IVA",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Denominazione",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Inizio possesso",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Fine possesso",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto, "Titolo",
				boldFont, BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto, "%",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Residenza indirizzo catastale", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Residenza indirizzo Docfa", boldFont, BaseColor.LIGHT_GRAY,
				null));

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Dichiarazione Tarsu", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Dichiarazione Tarsu per UIU", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Dichiarazione Tarsu per Civico", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Familiari", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Dichiarazione familiare Tarsu", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Dichiarazione familiare Tarsu per UIU", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableTitCatasto.addCell(getCell(tableTitCatasto,
				"Dichiarazione familiare Tarsu per Civico", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (DocfaTarReportSogg sogg : dto.getListaSoggetti()) {

			if ("P".equals(sogg.getFlgPersFisica())) {
				tableTitCatasto.addCell(getCell(tableTitCatasto, "Fisico",
						normalFont, BaseColor.WHITE, null));
				
				tableTitCatasto.addCell(getCell(tableTitCatasto, sogg
						.getCodiFisc(), normalFont, BaseColor.WHITE, null));

				tableTitCatasto.addCell(getCell(tableTitCatasto, sogg
						.getRagiSoci()
						+ " " + sogg.getNome(), normalFont, BaseColor.WHITE,
						null));
			} else {
				tableTitCatasto.addCell(getCell(tableTitCatasto, "Giuridico",
						normalFont, BaseColor.WHITE, null));
				
				tableTitCatasto.addCell(getCell(tableTitCatasto, sogg
						.getCodiPiva(), normalFont, BaseColor.WHITE, null));

				tableTitCatasto.addCell(getCell(tableTitCatasto, sogg
						.getRagiSoci(), normalFont, BaseColor.WHITE, null));
			}

			tableTitCatasto
					.addCell(getCell(tableTitCatasto, sogg.getId().getDataInizioPossesso(), normalFont,
							BaseColor.WHITE, null));

			tableTitCatasto.addCell(getCell(tableTitCatasto, sogg.getId().getDataFinePossesso(), normalFont, BaseColor.WHITE, null));

			tableTitCatasto.addCell(getCell(tableTitCatasto, sogg.getTitolo(), normalFont, BaseColor.WHITE, null));
			
			tableTitCatasto.addCell(getCell(tableTitCatasto, sogg.getPercPoss()
					+ "%", normalFont, BaseColor.WHITE, null));

			tableTitCatasto.addCell(getCell(tableTitCatasto, "1".equals(sogg
					.getFlgResidIndCat3112()) ? "SI" : "NO", normalFont,
					BaseColor.WHITE, null));

			tableTitCatasto.addCell(getCell(tableTitCatasto, "1".equals(sogg
					.getFlgResidIndDcf3112()) ? "SI" : "NO", normalFont,
					BaseColor.WHITE, null));

			String und = "1".equals(sogg.getFlgTitTarAnte())? "Precedente": ("1".equals(sogg.getFlgTitTarPost())||"2".equals(sogg.getFlgTitTarPost())?"Successivo":"Assente");
			und = "1".equals(sogg.getFlgTitTarAnte()) && "1".equals(sogg.getFlgTitTarPost())?"Precedente e Succesivo":und;
			tableTitCatasto.addCell(getCell(tableTitCatasto, und, normalFont,
					BaseColor.WHITE, null));

			String uiu = "1".equals(sogg.getFlgTitTarUiuAnte())? "Precedente": ("1".equals(sogg.getFlgTitTarUiuPost())||"1".equals(sogg.getFlgTitTarUiuPost())?"Successivo":"Assente");
			uiu = "1".equals(sogg.getFlgTitTarUiuAnte()) && "1".equals(sogg.getFlgTitTarUiuPost())?"Precedente e Succesivo":uiu;
			tableTitCatasto.addCell(getCell(tableTitCatasto, uiu, normalFont,
					BaseColor.WHITE, null));
			
			String civ = "1".equals(sogg.getFlgTitTarCivAnte())? "Precedente": ("1".equals(sogg.getFlgTitTarCivPost()) || "2".equals(sogg.getFlgTitTarCivPost())?"Successivo":"Assente");
			civ = "1".equals(sogg.getFlgTitTarCivAnte()) && "1".equals(sogg.getFlgTitTarCivPost())?"Precedente e Succesivo":civ;
			tableTitCatasto.addCell(getCell(tableTitCatasto, civ, normalFont,
					BaseColor.WHITE, null));
			
			BigDecimal numFam = sogg.getNumFamiliari();
			tableTitCatasto.addCell(getCell(tableTitCatasto,(numFam!=null ? numFam.toString(): ""), normalFont,
					BaseColor.WHITE, null));
			
			String undFam = "1".equals(sogg.getFlgFamTarAnte())? "Precedente": ("1".equals(sogg.getFlgFamTarPost()) || "2".equals(sogg.getFlgFamTarPost())?"Successivo":"Assente");
			undFam = "1".equals(sogg.getFlgFamTarAnte()) && "1".equals(sogg.getFlgFamTarPost())?"Precedente e Succesivo":undFam;
			tableTitCatasto.addCell(getCell(tableTitCatasto, undFam, normalFont,
					BaseColor.WHITE, null));

			String uiuFam = "1".equals(sogg.getFlgFamTarUiuAnte())? "Precedente": ("1".equals(sogg.getFlgFamTarUiuPost())||"1".equals(sogg.getFlgFamTarUiuPost())?"Successivo":"Assente");
			uiuFam = "1".equals(sogg.getFlgFamTarUiuAnte()) && "1".equals(sogg.getFlgFamTarUiuPost())?"Precedente e Succesivo":uiuFam;
			tableTitCatasto.addCell(getCell(tableTitCatasto, uiuFam, normalFont,
					BaseColor.WHITE, null));
			
			String civFam = "1".equals(sogg.getFlgFamTarCivAnte())? "Precedente": ("1".equals(sogg.getFlgFamTarCivPost())||"2".equals(sogg.getFlgFamTarCivPost())?"Successivo":"Assente");
			civFam = "1".equals(sogg.getFlgFamTarCivAnte()) && "1".equals(sogg.getFlgFamTarCivPost())?"Precedente e Succesivo":civFam;
			tableTitCatasto.addCell(getCell(tableTitCatasto, civFam, normalFont,
					BaseColor.WHITE, null));
			
		}
		document.add(tableTitCatasto);
		// FINE TABELLA TITOLARI A CATASTO

		document.add(p2);
		document.add(new Paragraph("Indirizzi associati all'immobile", boldFont));
		document.add(p1);

		PdfPTable tableIndirizzi = new PdfPTable(5);
		tableIndirizzi.setWidthPercentage(100);
		tableIndirizzi.setWidths(new float[] { 20, 2, 50, 2, 26 });
		tableIndirizzi.getDefaultCell().setMinimumHeight(20);
		tableIndirizzi.getDefaultCell().setBorder(0);
		/***
		 * creazione tre tabelle affiancate: itext tende ad allungare l'ultima
		 * riga per renderle uguali, il parametro extendLastRow = false; pare
		 * non funzionare ho ovviato aggiungendo celle invisibili
		 ***/

		// INIZIO TABELLA INDIRIZZI DOCFA
		PdfPTable tableIndDocfa = new PdfPTable(3);
		tableIndDocfa.setWidthPercentage(100);
		tableIndDocfa.setWidths(new float[] { 60, 20, 20 });
		tableIndDocfa.getDefaultCell().setMinimumHeight(20);

		tableIndDocfa.addCell(getCell(tableIndDocfa, "Docfa", boldFont,
				BaseColor.GRAY, null, new Integer(3)));

		tableIndDocfa.addCell(getCell(tableIndDocfa, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableIndDocfa.addCell(getCell(tableIndDocfa, "Num.Civ", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableIndDocfa.addCell(getCell(tableIndDocfa, "Presenza in Toponomastica", boldFont,
				BaseColor.LIGHT_GRAY, null));


		tableIndDocfa.addCell(getCell(tableIndDocfa,
				(dto.getDocfaTarReportDTO().getDocfaTarReport()
						.getPrefissoViaDocfa() != null ? dto
						.getDocfaTarReportDTO().getDocfaTarReport()
						.getPrefissoViaDocfa() : "")
						+ " "
						+ (dto.getDocfaTarReportDTO().getDocfaTarReport()
								.getViaDocfa() != null ? dto
								.getDocfaTarReportDTO().getDocfaTarReport()
								.getViaDocfa() : ""),
				normalFont, BaseColor.WHITE, null));
		
		tableIndDocfa.addCell(getCell(tableIndDocfa,
				(dto.getDocfaTarReportDTO().getDocfaTarReport()
								.getCiviciDocfa() != null ? dto
								.getDocfaTarReportDTO().getDocfaTarReport()
								.getCiviciDocfa().replaceAll("@", ", ") : ""),
				normalFont, BaseColor.WHITE, null));

		tableIndDocfa.addCell(getCell(tableIndDocfa, "1".equals(dto.getDocfaTarReportDTO()
				.getDocfaTarReport().getFlgIndirizzoInToponomastica()) ? "SI" : "NO", normalFont,
				BaseColor.WHITE, null));
		
		tableIndDocfa.addCell(getCell(tableIndDocfa, "", boldFont,
				BaseColor.WHITE, new Integer(0)));
		// FINE TABELLA INDIRIZZI DOCFA
	
		// INIZIO TABELLA INDIRIZZI CATASTO
		PdfPTable tableIndCatasto = new PdfPTable(4);
		tableIndCatasto.setWidthPercentage(100);
		tableIndCatasto.setWidths(new float[] { 50, 10, 20, 20 });
		tableIndCatasto.getDefaultCell().setMinimumHeight(20);

		tableIndCatasto.addCell(getCell(tableIndCatasto, "Catasto", boldFont,
				BaseColor.GRAY, null, new Integer(4)));

		tableIndCatasto.addCell(getCell(tableIndCatasto, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableIndCatasto.addCell(getCell(tableIndCatasto, "Num.Civ", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIndCatasto.addCell(getCell(tableIndCatasto, "Inizio validità",
				boldFont, BaseColor.LIGHT_GRAY, null));

		tableIndCatasto.addCell(getCell(tableIndCatasto, "Fine validità",
				boldFont, BaseColor.LIGHT_GRAY, null));

		for (IndirizzoDTO ind : dto.getListaIndirizziCatasto()) {

			tableIndCatasto.addCell(getCell(tableIndCatasto, ind
					.getIndirizzo(), normalFont, BaseColor.WHITE,null));
			
			tableIndCatasto.addCell(getCell(tableIndCatasto, ind
					.getCivico(), normalFont, BaseColor.WHITE,null));

			tableIndCatasto.addCell(getCell(tableIndCatasto, ind
					.getDataInizioVal(), normalFont, BaseColor.WHITE, null));

			tableIndCatasto.addCell(getCell(tableIndCatasto, ind
					.getDataFineVal(), normalFont, BaseColor.WHITE, null));
		}

		tableIndCatasto.addCell(getCell(tableIndCatasto, "", boldFont,
				BaseColor.WHITE, new Integer(0)));
		tableIndCatasto.addCell(getCell(tableIndCatasto, "", boldFont,
				BaseColor.WHITE, new Integer(0)));
		tableIndCatasto.addCell(getCell(tableIndCatasto, "", boldFont,
				BaseColor.WHITE, new Integer(0)));
		// FINE TABELLA INDIRIZZI CATASTO

		// INIZIO TABELLA INDIRIZZI TARSU
		PdfPTable tableIndTar = new PdfPTable(3);
		tableIndTar.setWidthPercentage(100);
		tableIndTar.setWidths(new float[] {50, 20, 30 });
		tableIndTar.getDefaultCell().setMinimumHeight(20);

		tableIndTar.addCell(getCell(tableIndTar, "Tarsu", boldFont,
				BaseColor.GRAY, null, new Integer(3)));

		tableIndTar.addCell(getCell(tableIndTar, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableIndTar.addCell(getCell(tableIndTar, "Num.Civ", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableIndTar.addCell(getCell(tableIndTar, "Rispetto al docfa", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (IndirizzoDTO ind : dto.getListaIndirizziTarsu()) {

			tableIndTar.addCell(getCell(tableIndTar, ind
					.getIndirizzo(), normalFont, BaseColor.WHITE, null));

			tableIndTar.addCell(getCell(tableIndTar, ind
					.getCivico(), normalFont, BaseColor.WHITE, null));

			tableIndTar.addCell(getCell(tableIndTar,
					ind.isAnteDocfa() ? "Precedente" : "Successivo",
					normalFont, BaseColor.WHITE, null));
		}

		tableIndTar.addCell(getCell(tableIndTar, "", boldFont, BaseColor.WHITE,
				new Integer(0)));
		tableIndTar.addCell(getCell(tableIndTar, "", boldFont, BaseColor.WHITE,
				new Integer(0)));
		
		// FINE TABELLA INDIRIZZI TARSU
		tableIndirizzi.addCell(tableIndDocfa);
		tableIndirizzi.addCell(getCell(tableIndirizzi, "", normalFont,
				BaseColor.WHITE, new Integer(0)));
		tableIndirizzi.addCell(tableIndCatasto);
		tableIndirizzi.addCell(getCell(tableIndirizzi, "", normalFont,
				BaseColor.WHITE, new Integer(0)));
		tableIndirizzi.addCell(tableIndTar);

		document.add(tableIndirizzi);

		document.add(p2);

		document.add(new Paragraph("Riscontro dichiarazioni Tarsu su UIU del Docfa", boldFont));
		document.add(p1);

		// INIZIO TABELLA TARSU PRECEDENTI
		PdfPTable tableTarAnte = createTableOggettoTarsu( "Tarsu precedenti al Docfa", boldFont);
		
		for (SitTTarOggettoDTO tarDto : dto.getListaOggettiTarAnte()) 
			insertDatiOggettoTarsu(tableTarAnte, tarDto, normalFont);

		document.add(tableTarAnte);
		// FINE TABELLA TARSU PRECEDENTI

		document.add(p1);

		// INIZIO TABELLA TARSU SUCCESSIVI
		PdfPTable tableTarPost = createTableOggettoTarsu( "Tarsu successivi al Docfa", boldFont);

		for (SitTTarOggettoDTO tarDto : dto.getListaOggettiTarPost()) 
			insertDatiOggettoTarsu(tableTarPost, tarDto, normalFont);

		document.add(tableTarPost);
		// FINE TABELLA TARSU SUCCESSIVI
		
	
		document.add(new Paragraph("Riscontro dichiarazioni Tarsu su UIU collegate", boldFont));
		document.add(p1);

		// INIZIO TABELLA TARSU PRECEDENTI SU GRAFFATI
		PdfPTable tableTarGAnte = createTableOggettoTarsu("Tarsu precedenti al Docfa", boldFont);

		for (SitTTarOggettoDTO tarDto : dto.getListaOggettiTarAnteGraffati())
			insertDatiOggettoTarsu(tableTarGAnte, tarDto, normalFont);

		document.add(tableTarGAnte);
		// FINE TABELLA TARSU PRECEDENTI GRAFFATI

		document.add(p1);

		// INIZIO TABELLA TARSU SUCCESSIVI SU GRAFFATI
		PdfPTable tableTarGPost = createTableOggettoTarsu("Tarsu successivi al Docfa", boldFont);

		for(SitTTarOggettoDTO tarDto : dto.getListaOggettiTarPostGraffati()) 
			insertDatiOggettoTarsu(tableTarGPost, tarDto, normalFont);

		document.add(tableTarGPost);
		// FINE TABELLA TARSU SUCCESSIVI SU GRAFFATI
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

	public String getDataFornitura() {
		return dataFornitura;
	}

	public void setDataFornitura(String dataFornitura) {
		this.dataFornitura = dataFornitura;
	}
	
	
	private PdfPTable createTableOggettoTarsu(String title, Font boldFont) throws DocumentException{
		
		PdfPTable tableTarsu = new PdfPTable(9);
		tableTarsu.setWidthPercentage(100);
		tableTarsu.setWidths(new float[] { 8, 8, 5, 8, 8, 15, 20, 8, 20 });
		tableTarsu.getDefaultCell().setMinimumHeight(20);

		tableTarsu.addCell(getCell(tableTarsu, title,
				boldFont, BaseColor.GRAY, null, new Integer(9)));
		
		tableTarsu.addCell(getCell(tableTarsu, "Data inizio", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTarsu.addCell(getCell(tableTarsu, "Data fine", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTarsu.addCell(getCell(tableTarsu, "Foglio", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableTarsu.addCell(getCell(tableTarsu, "Particella", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableTarsu.addCell(getCell(tableTarsu, "Subalterno", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		tableTarsu.addCell(getCell(tableTarsu, "Classe", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTarsu.addCell(getCell(tableTarsu, "Tipo oggetto", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTarsu.addCell(getCell(tableTarsu, "Superficie", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableTarsu.addCell(getCell(tableTarsu, "Indirizzo", boldFont,
				BaseColor.LIGHT_GRAY, null));
		
		return tableTarsu;
		
	}
	
	private void insertDatiOggettoTarsu(PdfPTable tableTarsu, SitTTarOggettoDTO tarDto, Font normalFont){
		
		tableTarsu.addCell(getCell(tableTarsu, tarDto
				.getSitTTarOggetto().getDatIni(), normalFont,
				BaseColor.WHITE, null));

		tableTarsu.addCell(getCell(tableTarsu, tarDto
				.getSitTTarOggetto().getDatFin(), normalFont,
				BaseColor.WHITE, null));
		
		tableTarsu.addCell(getCell(tableTarsu, tarDto
				.getSitTTarOggetto().getFoglio(), normalFont,
				BaseColor.WHITE, null));
		
		tableTarsu.addCell(getCell(tableTarsu, tarDto
				.getSitTTarOggetto().getNumero(), normalFont,
				BaseColor.WHITE, null));
		
		tableTarsu.addCell(getCell(tableTarsu, tarDto
				.getSitTTarOggetto().getSub(), normalFont,
				BaseColor.WHITE, null));

		tableTarsu.addCell(getCell(tableTarsu, tarDto
				.getSitTTarOggetto().getDesClsRsu(), normalFont,
				BaseColor.WHITE, null));

		tableTarsu.addCell(getCell(tableTarsu, tarDto
				.getSitTTarOggetto().getDesTipOgg(), normalFont,
				BaseColor.WHITE, null));

		tableTarsu.addCell(getCell(tableTarsu, tarDto
				.getSitTTarOggetto().getSupTot()
				+ " mq".toString(), normalFont, BaseColor.WHITE, null));

		tableTarsu.addCell(getCell(tableTarsu, tarDto.getVia() + " "
				+ tarDto.getSitTTarOggetto().getNumCiv(), normalFont,
				BaseColor.WHITE, null));
		
	}
	

}
