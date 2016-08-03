package it.bod.srv;


import it.webred.util.common.TiffUtill;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class OpenPdfSrv extends HttpServlet{

	private static final long serialVersionUID = 8769115765203470201L;
	
	private static Logger logger = Logger.getLogger(OpenPdfSrv.class.getName());

	public OpenPdfSrv() {
		// TODO Auto-generated constructor stub
	}
	
	public void service(HttpServletRequest req, HttpServletResponse res){
		String nomePdf = req.getParameter("nomePdf");
		int formato = req.getParameter("formato") == null ? TiffUtill.FORMATO_DEF : Integer.parseInt(req.getParameter("formato"));
		boolean openJpg = req.getParameter("openJpg") != null && new Boolean(req.getParameter("openJpg")).booleanValue();
		boolean openXls = req.getParameter("openXls") != null && new Boolean(req.getParameter("openXls")).booleanValue();
		boolean openZip = req.getParameter("openZip") != null && new Boolean(req.getParameter("openZip")).booleanValue();
		
		try{
			
			File pdfFile = new File(nomePdf);

			logger.debug("OpenPdfSrv - nomeFile:"+nomePdf);
			
			InputStream isByte = null;
			if (nomePdf != null && nomePdf.trim().endsWith(".tif")){
				InputStream is = new FileInputStream(pdfFile);
				List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is);
				ByteArrayOutputStream baos = null;
				if (openJpg) {
					baos = jpgs.get(0);
				} else {
					baos = TiffUtill.jpgsTopdf(jpgs, false, formato);
				}
				isByte = new ByteArrayInputStream(baos.toByteArray());		
				openXls = false;
			}else if (nomePdf != null && (nomePdf.trim().endsWith(".pdf") || nomePdf.trim().endsWith(".doc"))){
				isByte = new FileInputStream( pdfFile );
				openJpg = false;
				openXls = false;
			}else if(nomePdf != null && nomePdf.trim().endsWith(".xls")){
				isByte = new FileInputStream( pdfFile );
				openJpg = false;
				openXls = true;
			}else if(nomePdf != null && nomePdf.trim().endsWith(".zip")){
				isByte = new FileInputStream( pdfFile );
				openJpg = false;
				openXls = false;
				openZip = true;
			}else{
				/*
				 * questo ramo è stato aggiunto per le planimetrie C340 che hanno
				 * come estensione il progressivo (= 001 o 002 ...) pur essendo
				 * TIF
				 */
					InputStream is = new FileInputStream(pdfFile);
					List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is);
					ByteArrayOutputStream baos = null;
					if (openJpg) {
						baos = jpgs.get(0);
					} else {
						baos = TiffUtill.jpgsTopdf(jpgs, false, formato);
					}
					isByte = new ByteArrayInputStream(baos.toByteArray());		
					openXls = false;
			}
				
			BufferedInputStream bis = new BufferedInputStream(isByte);
			
			if (openJpg) {	
				String descrizione = req.getParameter("descrizione");
				res.setHeader("Content-Disposition","attachment; filename=\"" + descrizione.substring(0, descrizione.length() - ".tif".length()) + ".jpg" + "\"");
				res.setContentType("image/jpeg");
			} else if (nomePdf != null && nomePdf.trim().endsWith(".pdf")){
				String fileName = pdfFile.getName();
				res.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
				res.setContentType("application/pdf");
			} else if (nomePdf != null && nomePdf.trim().endsWith(".doc")){
				String fileName = pdfFile.getName();
				res.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
				res.setContentType("application/msword");
			} else if (openXls){
				String fileName = pdfFile.getName();
				res.setContentType("application/vnd.ms-excel"); 
				res.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
			} else if (openZip){
				String fileName = pdfFile.getName();
				res.setContentType("application/zip");
				res.setHeader("Transfer-Encoding", "chunked");
				res.addHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
				
			}

	        PrintWriter pw  =  res.getWriter();
	        int count = 0;
	        while ((count = bis.read()) >= 0){
	            pw.write(count);                    
	        }				

	        bis.close();
	        pw.flush();
	        pw.close();							        

	      /*  EsportaFiltroBck bean = (EsportaFiltroBck)getBeanReference("esportaFiltroBck");
	        bean.validateStatus();*/
	        
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
	}
	
	public Object getBeanReference(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application a = context.getApplication();
		Object o = a.getVariableResolver().resolveVariable(context, beanName);
		return o;
	}

}
