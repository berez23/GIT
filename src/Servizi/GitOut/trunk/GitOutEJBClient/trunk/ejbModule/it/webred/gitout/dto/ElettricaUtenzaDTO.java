package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="elettricaUtenzaDTO")
public class ElettricaUtenzaDTO implements Serializable{

	private static final long serialVersionUID = 5107638010341767586L;
	
	private String foglio = "";
	private String numero = "";
	private String tipoUtenza = "";
	private String annoRiferimento = "";
	private String codiceUtenza = "";
	private String cap = "";
	private String indirizzo = "";
	private String mesi = "";
	private String kwh = "";
	private String spesaAnnua = "";

	public ElettricaUtenzaDTO() {
	}//-------------------------------------------------------------------------

	public String getTipoUtenza() {
		return tipoUtenza;
	}

	public void setTipoUtenza(String tipoUtenza) {
		this.tipoUtenza = tipoUtenza;
	}

	public String getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(String annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public String getCodiceUtenza() {
		return codiceUtenza;
	}

	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getMesi() {
		return mesi;
	}

	public void setMesi(String mesi) {
		this.mesi = mesi;
	}

	public String getKwh() {
		return kwh;
	}

	public void setKwh(String kwh) {
		this.kwh = kwh;
	}

	public String getSpesaAnnua() {
		return spesaAnnua;
	}

	public void setSpesaAnnua(String spesaAnnua) {
		this.spesaAnnua = spesaAnnua;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	

}
