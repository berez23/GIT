package it.webred.mui.http.codebehind;

import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.input.MuiInvalidInputDataException;

import java.io.FileInputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class CodeBehinduploadPage extends AbstractPage {
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, java.io.IOException {
		if (req.getMethod().equalsIgnoreCase("post")) {
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// Parse the request
			List items = null;
			try {
				items = upload.parseRequest(req);
				final MuiFornituraParser parser = new MuiFornituraParser();
				FileItem item = ((FileItem) items.get(0));
				final java.io.File file = java.io.File.createTempFile(
						"MUI_NOTE", "txt");
				file.deleteOnExit();
				item.write(file);
				parser.setInput(new FileInputStream(file));
				parser.parse();
				parser.parseLastLine(parser.getLastLine( file));
				Thread t = new Thread() {
					public void run() {
						parser.parseNotes();
						file.delete();
					}
				};
				t.start();
			} catch (FileUploadException e) {
				throw new ServletException(e);
			} catch (MuiInvalidInputDataException e) {
				throw e;
			} catch (it.webred.mui.MuiException e) {
				throw e;
			} catch (Exception e) {
				Logger.log().error(this.getClass().getName(),"error",e);
				throw new ServletException(e);
			}
			req.setAttribute("requestedUri", "uploadResult.jsp");
			req.getRequestDispatcher("uploadResult.jsp").include(req, resp);
			return false;
		}
		return true;
	}
}
