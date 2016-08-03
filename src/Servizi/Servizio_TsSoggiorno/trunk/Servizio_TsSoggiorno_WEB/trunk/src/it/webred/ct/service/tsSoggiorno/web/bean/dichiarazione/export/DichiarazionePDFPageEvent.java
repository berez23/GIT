package it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione.export;

import it.webred.ct.service.tsSoggiorno.data.model.IsConfig;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class DichiarazionePDFPageEvent extends PdfPageEventHelper {

	String watermark = "FAC-SIMILE";
	Font watermarkFont = new Font(Font.FontFamily.HELVETICA, 100, Font.BOLD, BaseColor.LIGHT_GRAY);
	Font footerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	
	private HashMap<String, String> hashParametri;
	private IsConfig logo;
	
	public void onEndPage(PdfWriter writer, Document document) {

		try {
			
			// page footer
		    PdfPTable tblFt = new PdfPTable(3);
		    tblFt.setTotalWidth(550);
		    tblFt.setWidthPercentage(100);
		    tblFt.getDefaultCell().setBorder(0);
		    tblFt.setWidths(new float[] { 10, 85, 5 });
		    tblFt.addCell(new Phrase(hashParametri.get("DICH_VERSIONE"), footerFont));
		    tblFt.addCell("");
		    tblFt.addCell(new Phrase(String.valueOf(writer.getPageNumber()), footerFont));
		    float x = 30;
		    float y = 30;
		    //write the table
		    tblFt.writeSelectedRows(0, -1, x, y, writer.getDirectContent());
			
			// page header
		    PdfPTable tblHd = new PdfPTable(3);
		    tblHd.setTotalWidth(550);
		    tblHd.setWidthPercentage(100);
		    tblHd.getDefaultCell().setBorder(0);
		    tblHd.setWidths(new float[] { 15, 65, 20 });
		    //URL url = getClass().getResource("/it/webred/ct/service/tsSoggiorno/web/resources/logoMilano.jpg");
		    if(logo != null && logo.getAllegato() != null){
			    byte[] bytesImg = logo.getAllegato();
			    Image img = Image.getInstance(bytesImg);
			    tblHd.addCell(img);
		    }else tblHd.addCell("");
		    tblHd.addCell("");
		    tblHd.addCell(new Phrase(hashParametri.get("DICH_MODULO"), headerFont));
		    x = 30;
		    y = document.top() + 60;
		    //write the table
		    tblHd.writeSelectedRows(0, -1, x, y, writer.getDirectContent());
		    
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
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
