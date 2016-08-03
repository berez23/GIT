package it.webred.ct.service.muidocfa.web.bean.util;

import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public abstract class GestioneFileBean extends MuiDocfaBaseBean implements
		Serializable {

	private String fileName;
	
	protected String codEnte;

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
		File f = new File(filePath);
		if (f.exists()) {

			getLogger().debug("Downloading file " + fileName + "...");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			int DEFAULT_BUFFER_SIZE = 10240;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletResponse response = (HttpServletResponse) externalContext
					.getResponse();

			try {

				bis = new BufferedInputStream(new FileInputStream(f),
						DEFAULT_BUFFER_SIZE);

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
	
	protected RicercaOggettoDocfaDTO fillRicercaOggettoDocfa(RicercaDocfaReportDTO rs){
		RicercaOggettoDocfaDTO ro = new RicercaOggettoDocfaDTO();
		
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		
		ro.setFornitura(rs.getFornitura());
		ro.setProtocollo(rs.getProtocolloDocfa());
		ro.setFoglio(rs.getFoglio());
		ro.setParticella(rs.getParticella());
		ro.setUnimm(rs.getUnimm());
		ro.setDataRegistrazione(yyyyMMdd.format(rs.getDataRegistrazioneDocfa()));
		ro.setEnteId(rs.getEnteId());
		
		return ro;
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
		String pathDatiDiogene = super.getRootPathDatiDiogene();
		String rootPathEnte= pathDatiDiogene + codEnte;
		return rootPathEnte;
	}
	
	public String getCodEnte() {
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

}
