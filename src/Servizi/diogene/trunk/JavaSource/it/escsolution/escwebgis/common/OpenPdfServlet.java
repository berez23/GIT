package it.escsolution.escwebgis.common;

import it.escsolution.escwebgis.common.TiffUtill;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenPdfServlet extends EscServlet implements EscService{

	private static final long serialVersionUID = 8769115765203470201L;

	public OpenPdfServlet() {
		// TODO Auto-generated constructor stub
	}
	
	public void service(HttpServletRequest req, HttpServletResponse res){
		String nomePdf = req.getParameter("nomePdf");
		int formato = req.getParameter("formato") == null ? TiffUtill.FORMATO_DEF : Integer.parseInt(req.getParameter("formato"));
		boolean openJpg = req.getParameter("openJpg") != null && new Boolean(req.getParameter("openJpg")).booleanValue();
		
		try{
			File pdfFile = new File(nomePdf);
			InputStream isByte = null;
			if (nomePdf != null && nomePdf.trim().endsWith(".tif")){
				InputStream is = new FileInputStream(pdfFile);
				List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, false, false);
				ByteArrayOutputStream baos = null;
				if (openJpg) {
					baos = jpgs.get(0);
				} else {
					baos = TiffUtill.jpgsTopdf(jpgs, false, formato);
				}
				isByte = new ByteArrayInputStream(baos.toByteArray());				
			}else if (nomePdf != null && (nomePdf.trim().endsWith(".pdf") || nomePdf.trim().endsWith(".doc"))){
				isByte = new FileInputStream( pdfFile );
				openJpg = false;
			}
				
			BufferedInputStream bis = new BufferedInputStream(isByte);
			if (openJpg) {	
				String descrizione = req.getParameter("descrizione");
				res.setHeader("Content-Disposition","attachment; filename=\"" + descrizione.substring(0, descrizione.length() - ".tif".length()) + ".jpg" + "\"");
				res.setContentType("image/jpeg");
			} else if (nomePdf != null && nomePdf.trim().endsWith(".pdf")){
				res.setContentType("application/pdf");
			} else if (nomePdf != null && nomePdf.trim().endsWith(".doc")){
				res.setContentType("application/msword");
			}

	        PrintWriter pw  =  res.getWriter();
	        int count = 0;
	        while ((count = bis.read()) >= 0){
	            pw.write(count);                    
	        }				

	        bis.close();
	        pw.flush();
	        pw.close();							        


		}catch(Exception e){
			log.error(e.getMessage(),e);
		}

	}

}
