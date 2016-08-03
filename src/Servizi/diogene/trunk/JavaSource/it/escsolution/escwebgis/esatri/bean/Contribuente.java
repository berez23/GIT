/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.esatri.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Contribuente extends EscObject implements Serializable{

	private String cF_pI;
	private String codFornitura;
	private String fkRiversamento;
	private String flagRivesamento;
	private String codEnte;
	private String annoRiferimento;
	private String dataScadenza;
	private String progrInvio;
	private String nome;
	private String cognome;
	private String denominazione;
	private String dataNascita;
	private String indirizzo;
	private String cap;
	private String comune;
	private String flagTipoPers;
	private String flagFonteDati;
		
	
	public Contribuente(){}
	
	
	public String getChiave(){ 
		return ""+codFornitura+"|"+cF_pI;
	}


	public String getCF_pI() {
		return cF_pI;
	}


	public void setCF_pI(String cf_pi) {
		cF_pI = cf_pi;
	}


	public String getCodFornitura() {
		return codFornitura;
	}


	public void setCodFornitura(String codFornitura) {
		this.codFornitura = codFornitura;
	}


	public String getFkRiversamento() {
		return fkRiversamento;
	}


	public void setFkRiversamento(String fkRiversamento) {
		this.fkRiversamento = fkRiversamento;
	}


	public String getFlagRivesamento() {
		return flagRivesamento;
	}


	public void setFlagRivesamento(String flagRivesamento) {
		this.flagRivesamento = flagRivesamento;
	}


	public String getAnnoRiferimento() {
		return annoRiferimento;
	}


	public void setAnnoRiferimento(String annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}


	public String getCodEnte() {
		return codEnte;
	}


	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}


	public String getDataScadenza() {
		return dataScadenza;
	}


	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}


	public String getProgrInvio() {
		return progrInvio;
	}


	public void setProgrInvio(String progrInvio) {
		this.progrInvio = progrInvio;
	}


	public String getCap() {
		return cap;
	}


	public void setCap(String cap) {
		this.cap = cap;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public String getComune() {
		return comune;
	}


	public void setComune(String comune) {
		this.comune = comune;
	}


	public String getDataNascita() {
		return dataNascita;
	}


	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}


	public String getDenominazione() {
		return denominazione;
	}


	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}


	public String getFlagFonteDati() {
		return flagFonteDati;
	}


	public void setFlagFonteDati(String flagFonteDati) {
		this.flagFonteDati = flagFonteDati;
	}


	public String getFlagTipoPers() {
		return flagTipoPers;
	}


	public void setFlagTipoPers(String flagTipoPers) {
		this.flagTipoPers = flagTipoPers;
	}


	public String getIndirizzo() {
		return indirizzo;
	}


	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


}
