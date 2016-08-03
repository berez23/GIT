package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SuccessioneDefuntoDTO")
public class SuccessioneDefuntoDTO implements Serializable{

	private static final long serialVersionUID = 2608369217681407890L;
	
	private String codiceFiscale = "";
	private String cognome = "";
	private String nome = "";
	private String luogoNascita = "";
	private String provinciaNascita = "";
	private String dataNascita = "";
	private String sesso = "";

	public SuccessioneDefuntoDTO() {
	}//-------------------------------------------------------------------------

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

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

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
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
