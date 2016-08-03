package it.webred.ct.service.comma340.data.access.dto;

import it.webred.ct.service.comma340.data.model.pratica.C340Pratica;

import java.io.Serializable;


public class ModuloPraticaDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;	
	
	private String descComunePresentazioneModulo;
	
	private String descComuneResidenzaDichiarante;
	
	private String descComuneSedeEnte;
	
	private String descComuneImmmobile;
	
	private String descComuneNCEU;
	
	
	private C340Pratica pratica;

	public String getDescComunePresentazioneModulo() {
		return descComunePresentazioneModulo;
	}

	public void setDescComunePresentazioneModulo(
			String descComunePresentazioneModulo) {
		this.descComunePresentazioneModulo = descComunePresentazioneModulo;
	}

	public String getDescComuneResidenzaDichiarante() {
		return descComuneResidenzaDichiarante;
	}

	public void setDescComuneResidenzaDichiarante(
			String descComuneResidenzaDichiarante) {
		this.descComuneResidenzaDichiarante = descComuneResidenzaDichiarante;
	}

	public String getDescComuneSedeEnte() {
		return descComuneSedeEnte;
	}

	public void setDescComuneSedeEnte(String descComuneSedeEnte) {
		this.descComuneSedeEnte = descComuneSedeEnte;
	}

	public void setDescComuneImmmobile(String descComuneImmmobile) {
		this.descComuneImmmobile = descComuneImmmobile;
	}

	public String getDescComuneImmmobile() {
		return descComuneImmmobile;
	}

	public String getDescComuneNCEU() {
		return descComuneNCEU;
	}

	public void setDescComuneNCEU(String descComuneNCEU) {
		this.descComuneNCEU = descComuneNCEU;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setPratica(C340Pratica pratica) {
		this.pratica = pratica;
	}

	public C340Pratica getPratica() {
		return pratica;
	}
}
