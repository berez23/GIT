package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="localizzazioneDTO")
public class LocalizzazioneDTO implements Serializable{
	
	private static final long serialVersionUID = -3749758525766851822L;
	
	private String indirizzo = "";
	private String civico = "";
	private String tipoLoca = ""; // SIT, CATASTO

	public LocalizzazioneDTO() {
	}//-------------------------------------------------------------------------

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTipoLoca() {
		return tipoLoca;
	}

	public void setTipoLoca(String tipoLoca) {
		this.tipoLoca = tipoLoca;
	}
	
	

}
