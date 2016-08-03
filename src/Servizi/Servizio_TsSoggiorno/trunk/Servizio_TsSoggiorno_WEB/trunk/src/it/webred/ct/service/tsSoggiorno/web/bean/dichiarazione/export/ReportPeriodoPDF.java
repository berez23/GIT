package it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione.export;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.ModuloDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaDovuta;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaIncassata;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloEventi;
import it.webred.ct.service.tsSoggiorno.data.model.IsStrutturaSnap;
import it.webred.ct.service.tsSoggiorno.data.model.IsTabModuloField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.MultiKeyMap;

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

public class ReportPeriodoPDF {

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

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Font boldFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	Font normalFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
	Font titoloFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

	private List<DichiarazioneDTO> listaDichDTO;
	private String dal;
	private String al;

	public Document getPDFDocument(Document document) throws DocumentException,
			IOException {

		document.addTitle("pdf");
		document.addSubject("pdf");
		document.addAuthor("Servizio Imposta di Soggiorno");
		document.addCreator("Servizio Imposta di Soggiorno");

		addTitolo(document);
		addReport(document);

		return document;
	}

	private void addTitolo(Document document) throws DocumentException {

		Paragraph tit = new Paragraph();
		tit.add(new Paragraph("REPORT PERIODO DA " + dal + " A " + al, titoloFont));
		tit.setAlignment(Element.ALIGN_CENTER);
		document.add(tit);
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));

	}

	private void addReport(Document document) throws DocumentException,
			IOException {

		PdfPTable table = new PdfPTable(22);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 5, 4.1f, 4.7f, 4.7f, 4.1f, 4.7f, 4.7f, 4.1f, 4.7f, 4.7f, 4.1f, 4.7f, 4.7f, 4.1f,
				4.7f, 4.7f, 4.1f, 4.7f, 4.7f, 4.1f, 4.7f, 4.7f });
		table.getDefaultCell().setMinimumHeight(20);

		table.addCell(getCell(table, "", boldFont, BaseColor.WHITE, null));
		table.addCell(getCell(table, "1 Stella, RTA 2 Stelle", boldFont,
				BaseColor.WHITE, null, 3));
		table.addCell(getCell(table, "2 Stelle, RTA 3 Stelle", boldFont,
				BaseColor.WHITE, null, 3));
		table.addCell(getCell(table, "3 Stelle, RTA 4 Stelle", boldFont,
				BaseColor.WHITE, null, 3));
		table.addCell(getCell(table, "4 Stelle", boldFont, BaseColor.WHITE,
				null, 3));
		table.addCell(getCell(table, "5 Stelle e oltre", boldFont,
				BaseColor.WHITE, null, 3));
		table.addCell(getCell(table, "Tipologia extra alberghiera", boldFont,
				BaseColor.WHITE, null, 3));
		table.addCell(getCell(table, "TOTALE", boldFont,
				BaseColor.WHITE, null, 3));

		table.addCell(getCell(table, "PERIODO", normalFont, BaseColor.LIGHT_GRAY, null));
		for (int i = 0; i < 7; i++) {
			table.addCell(getCell(table, "N. Dich.", normalFont, BaseColor.LIGHT_GRAY,
					null));
			table.addCell(getCell(table, "Valore Incassato", normalFont,
					BaseColor.LIGHT_GRAY, null));
			table.addCell(getCell(table, "Valore Residuo", normalFont,
					BaseColor.LIGHT_GRAY, null));
		}

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
		
		//TODO gestire meglio classi
		List<String> listaClassi = new ArrayList<String>(
			    Arrays.asList("1", "2", "3", "4", "5", "999"));
		
		int totTotDich = 0;
		BigDecimal totTotInc = new BigDecimal(0);
		BigDecimal totTotRes = new BigDecimal(0);
		
		//stampo le celle per periodo/categoria
		for(String p: periodi){
			
			table.addCell(getCell(table, p, normalFont,
					BaseColor.WHITE, null));
			
			int totDich = 0;
			BigDecimal totInc = new BigDecimal(0);
			BigDecimal totRes = new BigDecimal(0);
			
			for(String classe: listaClassi){
				Elem e = (Elem) multiMap.get(p, classe);
				
				table.addCell(getCell(table, (e != null? String.valueOf(e.nDich) : "0"), normalFont,
						BaseColor.WHITE, null));
				table.addCell(getCell(table, (e != null? e.incassato.toString() : "0"), normalFont,
						BaseColor.WHITE, null));
				table.addCell(getCell(table, (e != null? e.residuo.toString() : "0"), normalFont,
						BaseColor.WHITE, null));
				
				if(e != null){
					totDich += e.nDich;
					totInc = totInc.add(e.incassato);
					totRes = totRes.add(e.residuo);
				}
			}
			
			//totale per periodo
			table.addCell(getCell(table, String.valueOf(totDich), boldFont,
					BaseColor.WHITE, null));
			table.addCell(getCell(table, totInc.toString(), boldFont,
					BaseColor.WHITE, null));
			table.addCell(getCell(table, totRes.toString(), boldFont,
					BaseColor.WHITE, null));
		}

		
		//totale per classe
		table.addCell(getCell(table, "TOTALE", boldFont,
				BaseColor.WHITE, null));
		
		for(String classe: listaClassi){
			
			int totDich = 0;
			BigDecimal totInc = new BigDecimal(0);
			BigDecimal totRes = new BigDecimal(0);
			
			for(String p: periodi){
				Elem e = (Elem) multiMap.get(p, classe);
				
				if(e != null){
					totDich += e.nDich;
					totInc = totInc.add(e.incassato);
					totRes = totRes.add(e.residuo);
					
					totTotDich += e.nDich;
					totTotInc = totInc.add(e.incassato);
					totTotRes = totRes.add(e.residuo);
				}
			}
			
			table.addCell(getCell(table, String.valueOf(totDich), boldFont,
					BaseColor.WHITE, null));
			table.addCell(getCell(table, totInc.toString(), boldFont,
					BaseColor.WHITE, null));
			table.addCell(getCell(table, totRes.toString(), boldFont,
					BaseColor.WHITE, null));
			
		}
		
		//totale del totale
		table.addCell(getCell(table, String.valueOf(totTotDich), normalFont,
				BaseColor.WHITE, null));
		table.addCell(getCell(table, totTotInc.toString(), normalFont,
				BaseColor.WHITE, null));
		table.addCell(getCell(table, totTotRes.toString(), normalFont,
				BaseColor.WHITE, null));
		
		document.add(table);
	}

	private PdfPCell getCell(PdfPTable table, String phrase, Font font,
			BaseColor color, Integer border) throws IOException {

		return getCell(table, phrase, font, color, border, new Integer(1));
	}

	private PdfPCell getCell(PdfPTable table, String phrase, Font font,
			BaseColor color, Integer border, Integer colSpan)
			throws IOException {

		PdfPCell c1 = table.getDefaultCell();

		Phrase ph = new Phrase();
		ph = new Phrase(phrase, font);

		c1.setPhrase(ph);
		if (color != null)
			c1.setBackgroundColor(color);
		if (border != null)
			c1.setBorder(border.intValue());
		if (colSpan != null)
			c1.setColspan(colSpan.intValue());

		return c1;
	}

	public List<DichiarazioneDTO> getListaDichDTO() {
		return listaDichDTO;
	}

	public void setListaDichDTO(List<DichiarazioneDTO> listaDichDTO) {
		this.listaDichDTO = listaDichDTO;
	}

	public String getDal() {
		return dal;
	}

	public void setDal(String dal) {
		this.dal = dal;
	}

	public String getAl() {
		return al;
	}

	public void setAl(String al) {
		this.al = al;
	}

}
