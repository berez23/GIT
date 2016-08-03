package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="docfaDichiaranteDTO")
public class DocfaDichiaranteDTO implements Serializable{

	private static final long serialVersionUID = -839586101974415665L;
	
	private String descrizione = "";
	
	private String cognome = "";
	private String nome = "";
	private String indirizzoRes = "";
	private String comuneRes = "";
	private String civicoRes = "";
	private String capRes = "";
	private String provinciaRes = "";

	public DocfaDichiaranteDTO() {
	}//-------------------------------------------------------------------------

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndirizzo() {
		return indirizzoRes;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzoRes = indirizzo;
	}

	public String getLuogo() {
		return comuneRes;
	}

	public void setLuogo(String luogo) {
		this.comuneRes = luogo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIndirizzoRes() {
		return indirizzoRes;
	}

	public void setIndirizzoRes(String indirizzoRes) {
		this.indirizzoRes = indirizzoRes;
	}

	public String getComuneRes() {
		return comuneRes;
	}

	public void setComuneRes(String comuneRes) {
		this.comuneRes = comuneRes;
	}

	public String getCivicoRes() {
		return civicoRes;
	}

	public void setCivicoRes(String civicoRes) {
		this.civicoRes = civicoRes;
	}

	public String getCapRes() {
		return capRes;
	}

	public void setCapRes(String capRes) {
		this.capRes = capRes;
	}

	public String getProvinciaRes() {
		return provinciaRes;
	}

	public void setProvinciaRes(String provinciaRes) {
		this.provinciaRes = provinciaRes;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	

}
