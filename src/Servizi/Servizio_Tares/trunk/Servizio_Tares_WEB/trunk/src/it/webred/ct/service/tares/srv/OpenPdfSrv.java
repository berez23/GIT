package it.webred.ct.service.tares.srv;

import it.webred.ct.service.tares.web.bean.SegnalaBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class OpenPdfSrv extends HttpServlet{

	private static final long serialVersionUID = 8769115765203470201L;
	
	private static Logger logger = Logger.getLogger(OpenPdfSrv.class.getName());
	private String nomeFile;

	public OpenPdfSrv() {
		// TODO Auto-generated constructor stub
	}
	
	public void service(HttpServletRequest req, HttpServletResponse res){

		
		//String nomeFile = req.getParameter("nomeFile");
		
		logger.debug(OpenPdfSrv.class + ".service - nomeFile: " + nomeFile);

		boolean openJpg = req.getParameter("openJpg") != null && new Boolean(req.getParameter("openJpg")).booleanValue();
		boolean openXls = req.getParameter("openXls") != null && new Boolean(req.getParameter("openXls")).booleanValue();
		boolean openZip = req.getParameter("openZip") != null && new Boolean(req.getParameter("openZip")).booleanValue();
		
		try{
			
			File file = new File(nomeFile);

			
			
			InputStream isByte = null;
			if(nomeFile != null && nomeFile.trim().endsWith(".zip")){
				isByte = new FileInputStream( file );
				openZip = true;
			}
				
			BufferedInputStream bis = new BufferedInputStream(isByte);
			
			if (openZip){
				String fileName = file.getName();
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
	}//-------------------------------------------------------------------------

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	
	 
	public void doDownload() {
		logger.info(OpenPdfSrv.class + ".doDownload");
		
		BufferedOutputStream  bos = null;
		BufferedInputStream bis = null;
		PrintWriter pw = null;
		//FileOutputStream fos = null;
		ServletOutputStream out = null;
		int DEFAULT_BUFFER_SIZE = 10240;
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
       ExternalContext externalContext = facesContext.getExternalContext();
       HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		try {
			File f = new File(nomeFile);
			
			if (f.exists()){
			
				bis = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);

				response.setContentType("application/" + getContentType());
				response
						.setHeader("Content-Length", String.valueOf(f.length()));
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + f.getName() + "\"");
				bos = new BufferedOutputStream(response.getOutputStream(),
						DEFAULT_BUFFER_SIZE);

				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int length;
				while ((length = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, length);
				}

				bos.flush();
				
			}else{
				logger.info("Impossibile trovare il file: " + nomeFile);
			}

			} catch (Throwable t) {
				
				t.printStackTrace();
			} finally {
				close(bos);
				close(bis);
			}

			facesContext.responseComplete();
			
	}//-------------------------------------------------------------------------
	
	public String getFileExt() {
		int dot = nomeFile.lastIndexOf(".");
		return nomeFile.substring(dot);
	}//-------------------------------------------------------------------------
	
	protected String getContentType() {

		String ct = "application/download";

		if (getFileExt().equalsIgnoreCase(".zip"))
			ct = "application/zip";

		return ct;
	}//-------------------------------------------------------------------------
	
	
	 private static void close(Closeable resource) {
	        if (resource != null) {
	          try {
		        resource.close();
		      } catch (IOException e) {
					logger.error(e.getMessage(), e);
		      }
		    }
	}//-------------------------------------------------------------------------

}
