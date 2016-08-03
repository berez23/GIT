package it.bod.application.beans;

import java.math.BigDecimal;

import it.bod.application.common.MasterItem;

public class BodBean  extends MasterItem{

	private static final long serialVersionUID = -325062062831471066L;
	
	private String protocollo = "";
	private String fornitura = "";
	private String dataVariazione = null;
	private BigDecimal soppressione = null;
	private BigDecimal variazione = null;
	private BigDecimal costituzione = null;
	private String dichiarante = "";
	private String tecnico = "";
	private String causale = "";
	private String stato = "";
	private String esito = "";
	private String operatore = "";
	private String dataRegistrazione = null;
	private String collaudo = "";
	
	public BodBean() {

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

	public BigDecimal getSoppressione() {
		return soppressione;
	}

	public void setSoppressione(BigDecimal soppressione) {
		this.soppressione = soppressione;
	}

	public BigDecimal getVariazione() {
		return variazione;
	}

	public void setVariazione(BigDecimal variazione) {
		this.variazione = variazione;
	}

	public BigDecimal getCostituzione() {
		return costituzione;
	}

	public void setCostituzione(BigDecimal costituzione) {
		this.costituzione = costituzione;
	}

	public String getDichiarante() {
		return dichiarante;
	}

	public void setDichiarante(String dichiarante) {
		this.dichiarante = dichiarante;
	}

	public String getTecnico() {
		return tecnico;
	}

	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public String getStato() {
		return stato;
	}


	public void setStato(String stato) {
		this.stato = stato;
	}


	public String getEsito() {
		return esito;
	}


	public void setEsito(String esito) {
		this.esito = esito;
	}


	public String getOperatore() {
		return operatore;
	}


	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}


	public String getDataRegistrazione() {
		return dataRegistrazione;
	}


	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCollaudo() {
		return collaudo;
	}

	public void setCollaudo(String collaudo) {
		this.collaudo = collaudo;
	}

}
