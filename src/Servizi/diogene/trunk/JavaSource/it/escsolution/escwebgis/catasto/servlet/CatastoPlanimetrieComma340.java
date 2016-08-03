package it.escsolution.escwebgis.catasto.servlet;

import it.escsolution.escwebgis.common.DiogeneException;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.TiffUtill;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CatastoPlanimetrieComma340 extends EscServlet {
	
    public void EseguiServizio(HttpServletRequest request, HttpServletResponse response)
    {
    	try {   			

			super.EseguiServizio(request, response);
			
			InputStream is = null;
			InputStream isByte = null;
			ByteArrayOutputStream baos = null;
			
			String pathCompleto = (String) request.getParameter("pathCompleto");
			String fileName = (String) request.getParameter("fileName");
			int formato = request.getParameter("formato") == null ? TiffUtill.FORMATO_DEF : Integer.parseInt(request.getParameter("formato"));
			boolean openJpg = request.getParameter("openJpg") != null && new Boolean(request.getParameter("openJpg")).booleanValue();	
			
			is = new FileInputStream(new File(pathCompleto));
				    
			boolean watermark = "SI".equalsIgnoreCase(((String) request.getParameter("watermark")==null?"SI":"NO"));
			List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, watermark, openJpg);
			
			if (openJpg) {
				baos = jpgs.get(0);
			} else {				
				baos =  TiffUtill.jpgsTopdf(jpgs, false, formato);
			}			
			
			isByte = new ByteArrayInputStream(baos.toByteArray());
			
			is.close();
			
			OutputStream out = response.getOutputStream();
			if (openJpg) {
				response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + ".tiff" + "\"");
				response.setContentType("image/tiff");
			} else {
				response.setHeader("Content-Disposition","inline; attachment; filename=\"" + fileName + ".pdf" + "\"");
				response.setContentType("application/pdf");
			}
			
			byte[] b = new byte[1024];
            while ( isByte.read( b ) != -1 ) {
                out.write( b );
            }
			
		} catch (FileNotFoundException e) {
			log.error("File richiesto non trovato " + e.getMessage(),e);
			throw new DiogeneException("File richiesto non trovato");
		}
    	catch (Exception e) {
    		log.error(e.getMessage(),e);
    		throw new DiogeneException(e);
		}       
    }
    
	public String getTema() {
		return "Catasto";
	}
	
}

