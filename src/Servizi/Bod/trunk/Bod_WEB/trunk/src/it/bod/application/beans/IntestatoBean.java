package it.bod.application.beans;

import java.util.Date;

import it.bod.application.common.MasterItem;

public class IntestatoBean  extends MasterItem{

	private static final long serialVersionUID = 121408474244436763L;
	
	private String protocollo_reg = "";
	private Date fornitura = null;
	private String cognome = "";
	private String nome = "";
	private String denominazione = "";
	private String codice_fiscale = "";
	private String partita_iva = "";
	private Integer nr_prog = null;

	public IntestatoBean() {
		// TODO Auto-generated constructor stub
	}//-------------------------------------------------------------------------
	
	public String getProtocollo_reg() {
		return protocollo_reg;
	}

	public void setProtocollo_reg(String protocollo_reg) {
		this.protocollo_reg = protocollo_reg;
	}

	public Date getFornitura() {
		return fornitura;
	}

	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
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

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodice_fiscale() {
		return codice_fiscale;
	}

	public void setCodice_fiscale(String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}

	public String getPartita_iva() {
		return partita_iva;
	}

	public void setPartita_iva(String partita_iva) {
		this.partita_iva = partita_iva;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getNr_prog() {
		return nr_prog;
	}

	public void setNr_prog(Integer nr_prog) {
		this.nr_prog = nr_prog;
	}
	
	

}
