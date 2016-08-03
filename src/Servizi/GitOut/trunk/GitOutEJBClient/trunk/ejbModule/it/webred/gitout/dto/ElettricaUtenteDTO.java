package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="elettricaUtenteDTO")
public class ElettricaUtenteDTO implements Serializable{

	private static final long serialVersionUID = -2464976790136100544L;
	
	private String denominazione = "";
	private String codiceFiscale = "";
	private String sesso = "";

	public ElettricaUtenteDTO() {
	}//-------------------------------------------------------------------------

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
