package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SuccessioneDefuntoDTO")
public class SuccessioneEreditaDTO implements Serializable{

	private static final long serialVersionUID = 6747400186176826829L;
	
	private String quotaDefunto = "";
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	private String codiceFiscale = "";
	private String nominativo = "";
	private String quotaErede = "";

	public SuccessioneEreditaDTO() {
	}//-------------------------------------------------------------------------

	public String getQuotaDefunto() {
		return quotaDefunto;
	}

	public void setQuotaDefunto(String quotaDefunto) {
		this.quotaDefunto = quotaDefunto;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNominativo() {
		return nominativo;
	}

	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}

	public String getQuotaErede() {
		return quotaErede;
	}

	public void setQuotaErede(String quotaErede) {
		this.quotaErede = quotaErede;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
