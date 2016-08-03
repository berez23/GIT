package it.webred.mui.http.codebehind;

import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.input.MuiInvalidInputDataException;
import it.webred.mui.inputxml.MuiFornituraXML2TXTTransformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class CodeBehinduploadXMLPage extends AbstractPage {
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, java.io.IOException {
		if (req.getMethod().equalsIgnoreCase("post")) {
			String xslFilePath = getServletContext().getRealPath(
					"xsl/MUI-TEST.xsl");
			// new File( getServletContext().getRealPath(
			// "pdf/modelloIciCh.tmpl.pdf"));
			System.out.println(xslFilePath);
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// Parse the request
			List items = null;
			try {
				items = upload.parseRequest(req);
				FileItem item = ((FileItem) items.get(0));
				final java.io.File fileXML = java.io.File.createTempFile(
						"MUI_NOTE", "xml");
//				fileXML.deleteOnExit();
				item.write(fileXML);

				/////////////////////////////////////////////////////////
				//FILIPPO MAZZINI 30.06.2011
				/* questa parte di codice verrà portata in un jar esterno, in maniera tale che il caricamento 
				 * possa essere effettuato anche da altri applicativi (ad esempio, da una regola di RulEngine 
				 * che legga tutti i file XML contenuti in una cartella) */				
				final MuiFornituraParser parser = new MuiFornituraParser();
				final java.io.File fileTXT = java.io.File.createTempFile(
						"MUI_NOTE", "txt");
//				fileTXT.deleteOnExit();
//				item.write(fileTXT);

				MuiFornituraXML2TXTTransformer test = new MuiFornituraXML2TXTTransformer(
						xslFilePath);
				int counters[] =test.transform(fileXML.getAbsolutePath(), new FileWriter(fileTXT));

				parser.setInput(new FileInputStream(fileTXT));
				Logger.log().info(this.getClass().getName(),
						"file xml parziale "+counters[0]+"/"+counters[1]);
				parser.setNFileCount(counters[0]);
				parser.setNFileTot(counters[1]);
				parser.parse(true);
				parser.parseLastLine(parser.getLastLine(fileTXT));
				Thread t = new Thread() {
					public void run() {
						parser.parseNotes();
						fileTXT.delete();
					}
				};
				t.start();
				//FINE FILIPPO MAZZINI 30.06.2011
				/////////////////////////////////////////////////////////
			} catch (FileUploadException e) {
				throw new ServletException(e);
			} catch (MuiInvalidInputDataException e) {
				throw e;
			} catch (it.webred.mui.MuiException e) {
				throw e;
			} catch (Exception e) {
				Logger.log().error(this.getClass().getName(), "error", e);
				throw new ServletException(e);
			}
			req.setAttribute("requestedUri", "uploadResultXML.jsp");
			req.getRequestDispatcher("uploadResultXML.jsp").include(req, resp);
			return false;
		}
		return true;
	}
}
