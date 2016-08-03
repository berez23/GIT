package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="metricaDTO")
public class MetricaDTO implements Serializable{

	private static final long serialVersionUID = -1360517189823211578L;
	
	private String vano = "";
	private String piano = "";
	private String edificio = "";
	private String ambiente = "";
	private String superficie = "";
	private String altezzaMin = "";
	private String altezzaMax = "";

	public MetricaDTO() {
	}//-------------------------------------------------------------------------

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public String getPiano() {
		return piano;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public String getEdificio() {
		return edificio;
	}

	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getSuperficie() {
		return superficie;
	}

	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}

	public String getAltezzaMin() {
		return altezzaMin;
	}

	public void setAltezzaMin(String altezzaMin) {
		this.altezzaMin = altezzaMin;
	}

	public String getAltezzaMax() {
		return altezzaMax;
	}

	public void setAltezzaMax(String altezzaMax) {
		this.altezzaMax = altezzaMax;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
