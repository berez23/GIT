package it.webred.ct.service.comma340.web.common;

import it.webred.ct.service.comma340.web.Comma340BaseBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public abstract class GestioneFileBean extends Comma340BaseBean implements Serializable {

	private String fileName;
	
	private String fileExt;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Effetua il downlad del file corrente
	 */
	public void doDownload() {
		super.getLogger().debug("Downloading file "+fileName+"...");
		BufferedInputStream  bis = null;
		BufferedOutputStream bos = null;
		int DEFAULT_BUFFER_SIZE = 10240;
		FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
		try {
			String filePath = this.getFilePath(fileName);
			File f = new File(filePath);			
			bis = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
	        
			response.setContentType(getContentType());
            response.setHeader("Content-Length", String.valueOf(f.length()));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + getFileExt() +"\"");
            bos = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);


            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }

            bos.flush();
			
		}
		catch(Throwable t) {
			super.addErrorMessage("file.download.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		finally {
			close(bos);
			close(bis);
		}
		
		facesContext.responseComplete();
	}
	
	
	protected abstract String getFilePath(String fileName);
	
	
	 private static void close(Closeable resource) {
        if (resource != null) {
          try {
	        resource.close();
	      } catch (IOException e) {
				logger.error(e.getMessage(), e);
	      }
	    }
	 }

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getFileExt() {
		return fileExt;
	}


	protected String getContentType(){
		
		String ct = "application/download";
		
		if(this.fileExt.equalsIgnoreCase(".pdf"))
			ct = "application/pdf";
		
		return ct;
	}

	
	 
	 
}
