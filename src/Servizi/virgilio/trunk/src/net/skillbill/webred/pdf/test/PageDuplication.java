package net.skillbill.webred.pdf.test;

import java.io.IOException;
import java.util.List;

import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDPage;

/**
 * prende un pdf. Produce un altro con la prima pagine e la seconda ripetuta 
 * N volte
 * @author toto
 *
 */

public class PageDuplication {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws COSVisitorException 
	 */
	public static void main(String[] args) throws COSVisitorException, IOException {
		PageDuplication fe = new PageDuplication();
		fe.duplicate("pdf/modelloIci.pdf","ciccio.pdf", 5);
	}

	public void duplicate(String inputFile, String outputFile, int times)
			throws IOException, COSVisitorException {
		// the document
		PDDocument docStart = PDDocument.load(inputFile);
		PDDocument docEnd = new PDDocument();
		List pages = docStart.getDocumentCatalog().getAllPages();
		docEnd.addPage((PDPage) pages.get(0));
		PDPage page = (PDPage) pages.get(1);
		
		for (int i = 1; i <= times; i++) {
			docEnd.importPage(page);
		}
		docEnd.save(outputFile);
		docEnd.close();
	}

}
