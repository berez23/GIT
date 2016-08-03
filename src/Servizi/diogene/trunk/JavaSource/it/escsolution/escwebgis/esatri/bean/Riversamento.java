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
public class Riversamento extends EscObject implements Serializable{

	private String codConcessione;
	private String codEnte;
	private String numQuietanza;
	private String progRecord;
	private String tipoRec;
	private String dataRiv;
	private String codTesoreria;
	private String importoVersato;
	private String importoCommissione;
	private String numRiscossioni;
	private String tipoRiv;
	private String tipoRisc;
	private String annoRiferimento;
	private String dataScadenza;
	private String progressivoInvio;
	private String codFornitura;
	private String pkRiversamento;
	
	
	public Riversamento(){
    }
	
	public String getChiave(){ 
		return pkRiversamento;
	}

	public String getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(String annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public String getCodConcessione() {
		return codConcessione;
	}

	public void setCodConcessione(String codConcessione) {
		this.codConcessione = codConcessione;
	}

	public String getCodEnte() {
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

	public String getCodFornitura() {
		return codFornitura;
	}

	public void setCodFornitura(String codFornitura) {
		this.codFornitura = codFornitura;
	}

	public String getCodTesoreria() {
		return codTesoreria;
	}

	public void setCodTesoreria(String codTesoreria) {
		this.codTesoreria = codTesoreria;
	}

	public String getDataRiv() {
		return dataRiv;
	}

	public void setDataRiv(String dataRiv) {
		this.dataRiv = dataRiv;
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getImportoCommissione() {
		return importoCommissione;
	}

	public void setImportoCommissione(String importoCommissione) {
		this.importoCommissione = importoCommissione;
	}

	public String getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(String importoVersato) {
		this.importoVersato = importoVersato;
	}

	public String getNumQuietanza() {
		return numQuietanza;
	}

	public void setNumQuietanza(String numQuietanza) {
		this.numQuietanza = numQuietanza;
	}

	public String getNumRiscossioni() {
		return numRiscossioni;
	}

	public void setNumRiscossioni(String numRiscossioni) {
		this.numRiscossioni = numRiscossioni;
	}

	public String getPkRiversamento() {
		return pkRiversamento;
	}

	public void setPkRiversamento(String pkRiversamento) {
		this.pkRiversamento = pkRiversamento;
	}

	public String getProgRecord() {
		return progRecord;
	}

	public void setProgRecord(String progRecord) {
		this.progRecord = progRecord;
	}

	public String getProgressivoInvio() {
		return progressivoInvio;
	}

	public void setProgressivoInvio(String progressivoInvio) {
		this.progressivoInvio = progressivoInvio;
	}

	public String getTipoRec() {
		return tipoRec;
	}

	public void setTipoRec(String tipoRec) {
		this.tipoRec = tipoRec;
	}

	public String getTipoRisc() {
		return tipoRisc;
	}

	public void setTipoRisc(String tipoRisc) {
		this.tipoRisc = tipoRisc;
	}

	public String getTipoRiv() {
		return tipoRiv;
	}

	public void setTipoRiv(String tipoRiv) {
		this.tipoRiv = tipoRiv;
	}
	


}
