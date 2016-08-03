package it.webred.ct.service.carContrib.web.pages.fonti;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public abstract class FonteBaseCarContrib {
	
	protected static Logger logger = Logger.getLogger("CarContribService_log");
	
	public static Object getEjb(String ear, String module, String ejbName){
		Context cont;
		try {
			cont = new InitialContext();
			return cont.lookup("java:global/" + ear + "/" + module + "/" + ejbName);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	protected void addMotivoFonteDisabilitataCC(Paragraph paragrafo, String fonte){
		String motivo = "Fonte "+fonte+" non configurata per la visualizzazione nel PDF";
		Font font = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.ITALIC);
		font.setColor(BaseColor.RED);
		paragrafo.add(new Paragraph(motivo,font));
		this.addEmptyLine(paragrafo, 2);
	}

	protected void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
	protected void addCellaIntestazione(PdfPTable table, Font font, int align, String label){
		
		PdfPCell c1 = table.getDefaultCell();
		c1.setPhrase(new Phrase(label,font));
		c1.setHorizontalAlignment(align);
		c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		c1.setColspan(1);
		table.addCell(c1);
		
	}
	
	protected void addCella(PdfPTable table, Font font,int align,String desc){
		
		PdfPCell cellInt = table.getDefaultCell();
		cellInt.setPhrase(new Phrase(desc,font));
		cellInt.setHorizontalAlignment(align);
		cellInt.setBackgroundColor(BaseColor.WHITE);
		cellInt.setColspan(1);
		table.addCell(cellInt);
	}
	
	protected PdfPTable insertTitoloScheda(String titolo)
	{
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.getDefaultCell().setMinimumHeight(40);
		
		PdfPCell myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase(titolo,new Font(Font.FontFamily.TIMES_ROMAN, 26,Font.BOLD)));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);
		
		return table;
	}
	
	public abstract void ClearCampiTAB();
	
}
