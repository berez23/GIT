package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="docfaDatoGeneraleDTO")
public class DocfaDatoGeneraleDTO implements Serializable{

	private static final long serialVersionUID = 3504493236728346635L;
	
	private String descrizione = "";

	private String protocollo = "";
	private String fornitura = "";
	private String dataVariazione = "";
	private String causale = "";
	private String soppressione = "";
	private String variazione = "";
	private String costituizione = "";
	private String annoCostruzione = "";
	private String annoRistrutturazione = "";
	private String annotazioni = "";

	public DocfaDatoGeneraleDTO() {

	}//-------------------------------------------------------------------------

	public String getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	public String getFornitura() {
		return fornitura;
	}

	public void setFornitura(String fornitura) {
		this.fornitura = fornitura;
	}

	public String getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getSoppressione() {
		return soppressione;
	}

	public void setSoppressione(String soppressione) {
		this.soppressione = soppressione;
	}

	public String getVariazione() {
		return variazione;
	}

	public void setVariazione(String variazione) {
		this.variazione = variazione;
	}

	public String getCostituizione() {
		return costituizione;
	}

	public void setCostituizione(String costituizione) {
		this.costituizione = costituizione;
	}

	public String getRiferimentiTemporali() {
		return annoCostruzione;
	}

	public void setRiferimentiTemporali(String riferimentiTemporali) {
		this.annoCostruzione = riferimentiTemporali;
	}

	public String getAnnotazioni() {
		return annotazioni;
	}

	public void setAnnotazioni(String annotazioni) {
		this.annotazioni = annotazioni;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAnnoCostruzione() {
		return annoCostruzione;
	}

	public void setAnnoCostruzione(String annoCostruzione) {
		this.annoCostruzione = annoCostruzione;
	}

	public String getAnnoRistrutturazione() {
		return annoRistrutturazione;
	}

	public void setAnnoRistrutturazione(String annoRistrutturazione) {
		this.annoRistrutturazione = annoRistrutturazione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	

}
