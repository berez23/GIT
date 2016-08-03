package net.skillbill.webred.pdf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.pdfbox.cos.COSArray;
import org.pdfbox.cos.COSString;
import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.pdfparser.PDFStreamParser;
import org.pdfbox.pdfwriter.ContentStreamWriter;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.common.PDStream;
import org.pdfbox.util.PDFOperator;

public class Util {

	public static void templatize(File in, File outFile, List varDesign,
			String varOut) throws IOException, COSVisitorException {
		PDDocument doc = PDDocument.load(in.getAbsolutePath());
		try {
			int countVar = 0;
			List pages = doc.getDocumentCatalog().getAllPages();
			for (int i = 0; i < pages.size(); i++) {
				PDPage page = (PDPage) pages.get(i);
				PDStream contents = page.getContents();
				PDFStreamParser parser = new PDFStreamParser(contents
						.getStream());
				parser.parse();
				List tokens = parser.getTokens();
				for (int j = 0; j < tokens.size(); j++) {
					Object next = tokens.get(j);

					if (next instanceof PDFOperator) {
						PDFOperator op = (PDFOperator) next;
						// Tj and TJ are the two operators that display
						// strings in a PDF
						if (op.getOperation().equals("Tj")) {
							boolean noTStar = true;
							Object o = tokens.get(j + 1);
							if (o instanceof PDFOperator) {
								PDFOperator ts = (PDFOperator) o;
								if (ts.getOperation().equals("T*"))
									noTStar = false;
							}
							// Tj takes one operator and that is the string
							// to display so lets update that operator
							if (noTStar) {
								COSString previous = (COSString) tokens
										.get(j - 1);
								String string = previous.getString();

								if (string != null) {
									if (varDesign.contains(string)) {
										string = varOut + (countVar++);
										previous.reset();
										previous.append(string.getBytes());
									}
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
										if (varDesign.contains(string)) {
											string = varOut + (countVar++);
											cosString.reset();
											cosString.append(string.getBytes());
										}
									}
								}
							}
						} else if (op.getOperation().equals("T*")) {
							COSString first = (COSString) tokens.get(j - 2);
							COSString second = (COSString) tokens.get(j + 1);
							String string = first.getString()
									+ second.getString();

							if (string != null) {
								if (varDesign.contains(string)) {
									string = varOut + (countVar++);
									first.reset();
									first.append(string.getBytes());
									second.reset();
									// second.append("".getBytes());
								}
							}
						} else if (op.getOperation().equals("TD")) {
							COSString first = (COSString) tokens.get(j - 4);
							COSString second = (COSString) tokens.get(j + 1);
							String string = first.getString()
									+ second.getString();

							if (string != null) {
								if (varDesign.contains(string)) {
									string = varOut + (countVar++);
									first.reset();
									first.append(string.getBytes());
									second.reset();
									// second.append("".getBytes());
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
				doc.save(outFile.getAbsolutePath());
			}
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws COSVisitorException
	 */
	public static void main(String[] args) throws COSVisitorException,
			IOException {
		File res = File.createTempFile("aaaa", ".pdf");
		List var = new ArrayList();
		var.add("Text");
		var.add("Tex");
		Util.templatize(new File("WebContent/pdf/modelloIci.texts.pdf"),
				res, var, "_v");
		System.out.println(res.getAbsolutePath());
	}

}
