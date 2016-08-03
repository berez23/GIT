package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="successionePraticaDTO")
public class SuccessionePraticaDTO implements Serializable{

	private static final long serialVersionUID = 5067875889614194222L;
	
	private String ufficio = "";
	private String anno = "";
	private String volume = "";
	private String numero = "";
	private String sottonumero = "";
	private String comune = "";
	private String progressivo = "";
	private String fornitura = "";

	public SuccessionePraticaDTO() {
	}//-------------------------------------------------------------------------

	public String getUfficio() {
		return ufficio;
	}

	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getSottonumero() {
		return sottonumero;
	}

	public void setSottonumero(String sottonumero) {
		this.sottonumero = sottonumero;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}

	public String getFornitura() {
		return fornitura;
	}

	public void setFornitura(String fornitura) {
		this.fornitura = fornitura;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
