package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="locazioneIImmobileDTO")
public class LocazioneIImmobileDTO implements Serializable{

	private static final long serialVersionUID = 1177132249665138542L;
	
	private String foglio = "";
	private String numero = "";
	private String sezione = "";
	private String subalterno = "";
	private String accatastamento = "";
	private String tipoCatasto = "";
	private String interoPorzione = "";
	private String codiceCatastale = "";
	private String indirizzo = "";
	
	public LocazioneIImmobileDTO() {
	}//-------------------------------------------------------------------------


	public String getSezione() {
		return sezione;
	}


	public void setSezione(String sezione) {
		this.sezione = sezione;
	}


	public String getSubalterno() {
		return subalterno;
	}


	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}


	public String getAccatastamento() {
		return accatastamento;
	}


	public void setAccatastamento(String accatastamento) {
		this.accatastamento = accatastamento;
	}


	public String getTipoCatasto() {
		return tipoCatasto;
	}


	public void setTipoCatasto(String tipoCatasto) {
		this.tipoCatasto = tipoCatasto;
	}


	public String getInteroPorzione() {
		return interoPorzione;
	}


	public void setInteroPorzione(String interoPorzione) {
		this.interoPorzione = interoPorzione;
	}


	public String getCodiceCatastale() {
		return codiceCatastale;
	}


	public void setCodiceCatastale(String codiceCatastale) {
		this.codiceCatastale = codiceCatastale;
	}


	public String getIndirizzo() {
		return indirizzo;
	}


	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
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
