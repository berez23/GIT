package net.skillbill.webred.pdf;

import it.webred.mui.http.MuiApplication;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pdfbox.cos.COSArray;
import org.pdfbox.cos.COSString;
import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.pdfparser.PDFStreamParser;
import org.pdfbox.pdfwriter.ContentStreamWriter;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentCatalog;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.common.PDStream;
import org.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.pdfbox.pdmodel.interactive.form.PDField;
import org.pdfbox.util.PDFOperator;

abstract class TmplBase {

	private static Log _log = LogFactory.getLog(TmplBase.class);

	private File _actual;

	

	public TmplBase(File templateName, int pagesBody) throws IOException,
			COSVisitorException {
		PDDocument docStart = PDDocument.load(templateName.getAbsolutePath());
		PDDocument doc = new PDDocument();
		List pages = docStart.getDocumentCatalog().getAllPages();
		doc.importPage((PDPage) pages.get(0));
		PDPage page = (PDPage) pages.get(1);

		for (int i = 1; i <= pagesBody; i++) {
			doc.importPage(page);
		}
		doc.save(nextFileState().getAbsolutePath());
		doc.close();
		docStart.close();
	}

	private File nextFileState() throws IOException {
		File result = File.createTempFile("wrState"+MuiApplication.belfiore+"_", ".pdf");
		if (_actual != null)
			_actual.delete();

		_actual = result;

		return result;
	}



	

	

	public File actual() {
		return actualFileState();
	}

	private File actualFileState() {
		return _actual;
	}
	
	public void replaceVar(int pageIndex, Map replaces) throws IOException, COSVisitorException {
		PDDocument doc = PDDocument.load(actualFileState().getAbsolutePath());
		try {
			List pages = doc.getDocumentCatalog().getAllPages();

			PDPage page = (PDPage) pages.get(pageIndex);
			PDStream contents = page.getContents();
			PDFStreamParser parser = new PDFStreamParser(contents.getStream());
			parser.parse();
			List tokens = parser.getTokens();
			for (int j = 0; j < tokens.size(); j++) {
				Object next = tokens.get(j);

				if (next instanceof PDFOperator) {
					PDFOperator op = (PDFOperator) next;
					// Tj and TJ are the two operators that display
					// strings in a PDF
					if (op.getOperation().equals("Tj")) {
						// Tj takes one operator and that is the string
						// to display so lets update that operator
						COSString previous = (COSString) tokens.get(j - 1);
						String string = previous.getString();

						if (string != null) {
							if (replaces.containsKey(string)) {
								string = String.valueOf(replaces.get(string));
								previous.reset();
								previous.append(string.getBytes());
							}
						}

					} else if (op.getOperation().equals("TJ")) {
						COSArray previous = (COSArray) tokens.get(j - 1);
						for (int k = 0; k < previous.size(); k++) {
							Object arrElement = previous.getObject(k);
							if (arrElement instanceof COSString) {
								COSString cosString = (COSString) arrElement;
								String string = cosString.getString();
								if (string != null) {
									if (replaces.containsKey(string)) {
										string = String.valueOf(replaces.get(string));
										cosString.reset();
										cosString.append(string.getBytes());
									}
								}
							}
						}
					}
				}
			}
			PDStream updatedStream = new PDStream(doc);
			OutputStream out = updatedStream.createOutputStream();
			ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
			tokenWriter.writeTokens(tokens);
			page.setContents(updatedStream);
			doc.save(nextFileState().getAbsolutePath());
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
		
	}

	public void replaceVar(int pageIndex, String varValue, String newValue)
			throws IOException, COSVisitorException {

		PDDocument doc = PDDocument.load(actualFileState().getAbsolutePath());
		try {
			List pages = doc.getDocumentCatalog().getAllPages();

			PDPage page = (PDPage) pages.get(pageIndex);
			PDStream contents = page.getContents();
			PDFStreamParser parser = new PDFStreamParser(contents.getStream());
			parser.parse();
			List tokens = parser.getTokens();
			for (int j = 0; j < tokens.size(); j++) {
				Object next = tokens.get(j);

				if (next instanceof PDFOperator) {
					PDFOperator op = (PDFOperator) next;
					// Tj and TJ are the two operators that display
					// strings in a PDF
					if (op.getOperation().equals("Tj")) {
						// Tj takes one operator and that is the string
						// to display so lets update that operator
						COSString previous = (COSString) tokens.get(j - 1);
						String string = previous.getString();

						if (string != null) {
							if (string.equalsIgnoreCase(varValue)) {
								string = newValue;
								previous.reset();
								previous.append(string.getBytes());
							}
						}

					} else if (op.getOperation().equals("TJ")) {
						COSArray previous = (COSArray) tokens.get(j - 1);
						for (int k = 0; k < previous.size(); k++) {
							Object arrElement = previous.getObject(k);
							if (arrElement instanceof COSString) {
								COSString cosString = (COSString) arrElement;
								String string = cosString.getString();
								if (string != null) {
									if (string.equalsIgnoreCase(varValue)) {
										string = newValue;
										cosString.reset();
										cosString.append(string.getBytes());
									}
								}
							}
						}
					}
				}
			}
			PDStream updatedStream = new PDStream(doc);
			OutputStream out = updatedStream.createOutputStream();
			ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
			tokenWriter.writeTokens(tokens);
			page.setContents(updatedStream);
			doc.save(nextFileState().getAbsolutePath());
		} finally {
			if (doc != null) {
				doc.close();
			}
		}

	}

	/**
	 * metodo di debug. Stampa tutti i field - form di un pdf
	 * 
	 * @param pdfFile
	 * @throws IOException
	 */
	public static void printFields(File pdfFile) throws IOException {
		PDDocument pdfDocument = PDDocument.load(pdfFile.getAbsolutePath());
		PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();

		List fields = acroForm.getFields();
		Iterator fieldsIter = fields.iterator();

		_log.debug(new Integer(fields.size()).toString()
				+ " top-level fields were found on the form");

		while (fieldsIter.hasNext()) {
			PDField field = (PDField) fieldsIter.next();
			processField(field, "|--", field.getPartialName() + "("
					+ field.getFullyQualifiedName() + ")");
		}

		pdfDocument.close();
	}

	/**
	 * metodo interno al print field
	 * 
	 * @param field
	 * @param sLevel
	 * @param sParent
	 * @throws IOException
	 */
	private static void processField(PDField field, String sLevel,
			String sParent) throws IOException {
		List kids = field.getKids();
		if (kids != null) {
			Iterator kidsIter = kids.iterator();
			if (!sParent.equals(field.getPartialName())) {
				sParent = sParent + "." + field.getPartialName();
			}
			System.out.println(sLevel + sParent);
			System.out.println(sParent + " is of type "
					+ field.getClass().getName());
			while (kidsIter.hasNext()) {
				Object pdfObj = kidsIter.next();
				if (pdfObj instanceof PDField) {
					PDField kid = (PDField) pdfObj;
					processField(kid, "|  " + sLevel, sParent);
				}
			}
		} else {
			String outputString = sLevel + sParent + "."
					+ field.getPartialName() + " = " + field.getValue()
					+ ",  type=" + field.getClass().getName();

			System.out.println(outputString);
		}
	}

}
