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
public class RiscossioneViolazione extends EscObject implements Serializable{

	private String codConcessione;
	private String codEnte;
	private String numQuietanza;
	private String progRecord;
	private String tipoRec;
	private String cF_pI;
	private String dataVersamento;
	private String rifQuietanza;
	private String importoVersato;
	private String importoImposta;
	private String importoSoprattassa;
	private String importoPenaPecuniaria;
	private String importoInteressi;
	private String flagQuadratura;
	private String flagReperib;
	private String tipoVers;
	private String dataRegistrazione;
	private String flagCompVersContr;
	private String comuneImmobili;
	private String cap;
	private String flagIdentificazione;
	private String annoImpostaVio;
	private String numProvvLiqui;
	private String dataProvvLiqui;
	private String denominazionePG;
	private String comuneDomicilioPG;
	private String denominazionePF;
	private String comuneDomicilioPF;
	private String codFornitura;
	private String fkRiversamento;
	
	private String annoRiferimento;
	private String dataScadenza;
	private String progressivoInvio; 
	
	
	public RiscossioneViolazione(){
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

	public String getProgRecord() {
		return progRecord;
	}

	public void setProgRecord(String progRecord) {
		this.progRecord = progRecord;
	}

	public String getTipoRec() {
		return tipoRec;
	}

	public void setTipoRec(String tipoRec) {
		this.tipoRec = tipoRec;
	}

	public String getAnnoImpostaVio() {
		return annoImpostaVio;
	}

	public void setAnnoImpostaVio(String annoImpostaVio) {
		this.annoImpostaVio = annoImpostaVio;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCF_pI() {
		return cF_pI;
	}

	public void setCF_pI(String cf_pi) {
		cF_pI = cf_pi;
	}

	public String getComuneImmobili() {
		return comuneImmobili;
	}

	public void setComuneImmobili(String comuneImmobili) {
		this.comuneImmobili = comuneImmobili;
	}

	public String getDataProvvLiqui() {
		return dataProvvLiqui;
	}

	public void setDataProvvLiqui(String dataProvvLiqui) {
		this.dataProvvLiqui = dataProvvLiqui;
	}

	public String getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public String getDataVersamento() {
		return dataVersamento;
	}

	public void setDataVersamento(String dataVersamento) {
		this.dataVersamento = dataVersamento;
	}

	public String getFkRiversamento() {
		return fkRiversamento;
	}

	public void setFkRiversamento(String fkRiversamento) {
		this.fkRiversamento = fkRiversamento;
	}

	public String getFlagCompVersContr() {
		return flagCompVersContr;
	}

	public void setFlagCompVersContr(String flagCompVersContr) {
		this.flagCompVersContr = flagCompVersContr;
	}

	public String getFlagIdentificazione() {
		return flagIdentificazione;
	}

	public void setFlagIdentificazione(String flagIdentificazione) {
		this.flagIdentificazione = flagIdentificazione;
	}

	public String getFlagQuadratura() {
		return flagQuadratura;
	}

	public void setFlagQuadratura(String flagQuadratura) {
		this.flagQuadratura = flagQuadratura;
	}

	public String getFlagReperib() {
		return flagReperib;
	}

	public void setFlagReperib(String flagReperib) {
		this.flagReperib = flagReperib;
	}

	public String getImportoImposta() {
		return importoImposta;
	}

	public void setImportoImposta(String importoImposta) {
		this.importoImposta = importoImposta;
	}

	public String getImportoInteressi() {
		return importoInteressi;
	}

	public void setImportoInteressi(String importoInteressi) {
		this.importoInteressi = importoInteressi;
	}

	public String getImportoPenaPecuniaria() {
		return importoPenaPecuniaria;
	}

	public void setImportoPenaPecuniaria(String importoPenaPecuniaria) {
		this.importoPenaPecuniaria = importoPenaPecuniaria;
	}

	public String getImportoSoprattassa() {
		return importoSoprattassa;
	}

	public void setImportoSoprattassa(String importoSoprattassa) {
		this.importoSoprattassa = importoSoprattassa;
	}

	public String getNumProvvLiqui() {
		return numProvvLiqui;
	}

	public void setNumProvvLiqui(String numProvvLiqui) {
		this.numProvvLiqui = numProvvLiqui;
	}

	public String getRifQuietanza() {
		return rifQuietanza;
	}

	public void setRifQuietanza(String rifQuietanza) {
		this.rifQuietanza = rifQuietanza;
	}

	public String getTipoVers() {
		return tipoVers;
	}

	public void setTipoVers(String tipoVers) {
		this.tipoVers = tipoVers;
	}

	public String getComuneDomicilioPF() {
		return comuneDomicilioPF;
	}

	public void setComuneDomicilioPF(String comuneDomicilioPF) {
		this.comuneDomicilioPF = comuneDomicilioPF;
	}

	public String getComuneDomicilioPG() {
		return comuneDomicilioPG;
	}

	public void setComuneDomicilioPG(String comuneDomicilioPG) {
		this.comuneDomicilioPG = comuneDomicilioPG;
	}

	public String getDenominazionePF() {
		return denominazionePF;
	}

	public void setDenominazionePF(String denominazionePF) {
		this.denominazionePF = denominazionePF;
	}

	public String getDenominazionePG() {
		return denominazionePG;
	}

	public void setDenominazionePG(String denominazionePG) {
		this.denominazionePG = denominazionePG;
	}

	public String getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(String annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getProgressivoInvio() {
		return progressivoInvio;
	}

	public void setProgressivoInvio(String progressivoInvio) {
		this.progressivoInvio = progressivoInvio;
	}

}
