package it.webred.ct.service.segnalazioniqualificate.web.bean.util;

import it.webred.ct.service.segnalazioniqualificate.web.bean.SegnalazioniQualificateBaseBean;
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

public abstract class GestioneFileBean extends SegnalazioniQualificateBaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileName;
	
	private String codEnte;
	

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

		String filePath = this.getFilePath(fileName);
		doDownloadFilePath(filePath);
	}
	
	public void doDownloadFilePath(String filePath) {

		File f = new File(filePath);
		if (f.exists()) {
			getLogger().debug("Downloading file " + fileName + "...");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			int DEFAULT_BUFFER_SIZE = 10240;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

			try {
				bis = new BufferedInputStream(new FileInputStream(f),DEFAULT_BUFFER_SIZE);

				response.setContentType("application/" + getContentType());
				response.setHeader("Content-Length", String.valueOf(f.length()));
				response.setHeader("Content-Disposition","attachment; filename=\"" + f.getName() + "\"");
				bos = new BufferedOutputStream(response.getOutputStream(),DEFAULT_BUFFER_SIZE);

				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int length;
				while ((length = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, length);
				}

				bos.flush();

			} catch (Throwable t) {
				super.addErrorMessage("file.download.error", t.getMessage());
				t.printStackTrace();
			} finally {
				close(bos);
				close(bis);
			}

			facesContext.responseComplete();
		}else 
			super.addErrorMessage("file.download.null", "");
	}


	protected abstract String getFilePath(String fileName);

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getFileExt() {
		int dot = fileName.lastIndexOf(".");
		return fileName.substring(dot);
	}

	protected String getContentType() {

		String ct = "application/download";

		if (getFileExt().equalsIgnoreCase(".pdf"))
			ct = "application/pdf";

		return ct;
	}
	
	protected String cleanLeftPad(String s, char pad){
		if(s!= null){
			//s = s.trim();
			while (s.length() > 1 && s.charAt(0) == pad)
				s = s.substring(1);
		}
		return s;
	}
	
	protected String getFolderPath() {
		String root = super.getRootPathDatiDiogene();
		String rootPathEnte= root +  getCodEnte();
		return rootPathEnte;
	}
	
	public String getCodEnte() {
		if(codEnte==null)
			codEnte = this.getCurrentEnte();
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

	public void createDirectoryPath(String path) {
		// Se non esiste il percorso, lo crea
		File dir = new File(path);
		logger.info(path);
		try {
			if (!dir.exists())
				dir.mkdirs();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
