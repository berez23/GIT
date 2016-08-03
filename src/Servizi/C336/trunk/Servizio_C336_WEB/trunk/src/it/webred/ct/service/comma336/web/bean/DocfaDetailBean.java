package it.webred.ct.service.comma336.web.bean;

import it.webred.ct.data.access.basic.docfa.dto.DettaglioDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.service.comma336.web.bean.util.GestioneFileBean;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocfaDetailBean extends GestioneFileBean {
	
	private static final long serialVersionUID = 1L;
	
	private DettaglioDocfaDTO dto;
	private String protocollo;
	private String sFornitura;
	private Date fornitura;
	SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	protected String getFilePath(String fileName) {
		String nomeFile = fileName;
		String pathFile = getFolderPath() + File.separatorChar + nomeFile;
		return pathFile;
	}

	protected String getFolderPath() {
		String pathDatiDiogene = super.getRootPathDatiDiogene();
		String pathPdfDocfa = pathDatiDiogene
				//+ File.separatorChar
				+ this.getCodEnte()
				+ File.separatorChar
				+ "docfa_pdf"
				+ File.separatorChar
				+ sFornitura.substring(0, 6);
		return pathPdfDocfa;
		
	}
	
	public void doCaricaDocfa() {
		try {
			
			RicercaOggettoDocfaDTO rod = new RicercaOggettoDocfaDTO();
			this.fillEnte(rod);
			rod.setProtocollo(protocollo);
			rod.setFornitura(yyyyMMdd.parse(sFornitura));
			dto = docfaService.getDettaglioDocfa(rod);
			
			
		} catch (Throwable t) {
			super.addErrorMessage("docfa.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
	}
	
	
	public DettaglioDocfaDTO getDto() {
		return dto;
	}

	public void setDto(DettaglioDocfaDTO dto) {
		this.dto = dto;
	}

	public String getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	public String getsFornitura() {
		return sFornitura;
	}

	public void setsFornitura(String sFornitura) throws ParseException {
		this.sFornitura = sFornitura;
		setFornitura(yyyyMMdd.parse(sFornitura));
	}

	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}

	public Date getFornitura() {
		return fornitura;
	}

}
