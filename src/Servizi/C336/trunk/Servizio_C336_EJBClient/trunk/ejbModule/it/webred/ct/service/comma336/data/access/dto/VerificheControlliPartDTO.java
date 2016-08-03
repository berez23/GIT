package it.webred.ct.service.comma336.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

public class VerificheControlliPartDTO extends CeTBaseObject {
	
	private static final long serialVersionUID = 1L;
	
	private String archivio;
	private Boolean flgPresParticella;
	private Boolean flgPresCivico;
	
	public String getArchivio() {
		return archivio;
	}
	public void setArchivio(String archivio) {
		this.archivio = archivio;
	}
	
	public Boolean getFlgPresParticella() {
		return flgPresParticella;
	}
	public void setFlgPresParticella(Boolean flgPresParticella) {
		this.flgPresParticella = flgPresParticella;
	}
	public Boolean getFlgPresCivico() {
		return flgPresCivico;
	}
	public void setFlgPresCivico(Boolean flgPresCivico) {
		this.flgPresCivico = flgPresCivico;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
