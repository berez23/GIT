package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="gasUtenzaDTO")
public class GasUtenzaDTO implements Serializable{

	private static final long serialVersionUID = -1805724783485166610L;
	
	private String codiceUtenza = "";
	private String tipoUtenza = "";
	private String annoRiferimento = "";
	private String mesi = "";
	private String spesaAnnua = "";
	private String indirizzo = "";
	private String cap = "";
	private String tipoFornitura = "";

	public GasUtenzaDTO() {
	}//-------------------------------------------------------------------------


	public String getCodiceUtenza() {
		return codiceUtenza;
	}


	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}


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


	public String getMesi() {
		return mesi;
	}


	public void setMesi(String mesi) {
		this.mesi = mesi;
	}


	public String getSpesaAnnua() {
		return spesaAnnua;
	}


	public void setSpesaAnnua(String spesaAnnua) {
		this.spesaAnnua = spesaAnnua;
	}


	public String getIndirizzo() {
		return indirizzo;
	}


	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}


	public String getCap() {
		return cap;
	}


	public void setCap(String cap) {
		this.cap = cap;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getTipoFornitura() {
		return tipoFornitura;
	}


	public void setTipoFornitura(String tipoFornitura) {
		this.tipoFornitura = tipoFornitura;
	}

	
	

}


