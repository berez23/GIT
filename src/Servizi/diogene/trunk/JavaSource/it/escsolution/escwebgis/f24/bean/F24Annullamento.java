package it.escsolution.escwebgis.f24.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class F24Annullamento extends EscObject implements Serializable {

	private String chiave;
	private String idExt;
	private String dtFornitura;
	private String progFornitura;
	private String dtRipartizioneOrig;
	private String progRipartizioneOrig;
	private String dtBonificoOrig;
	private String codEnte;
	private String codFisc;
	private String dtRiscossione;
	private String codEnteCom;
	private String codTributo;
	private String annoRif;
	private String impDebito;
	private String impCredito;
	private String tipoOperazione;
	private String dtOperazione;
	private String tipoImposta;
	
	private String descTributo;

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getIdExt() {
		return idExt;
	}

	public void setIdExt(String idExt) {
		this.idExt = idExt;
	}

	public String getDtFornitura() {
		return dtFornitura;
	}

	public void setDtFornitura(String dtFornitura) {
		this.dtFornitura = dtFornitura;
	}

	public String getProgFornitura() {
		return progFornitura;
	}

	public void setProgFornitura(String progFornitura) {
		this.progFornitura = progFornitura;
	}

	public String getDtRipartizioneOrig() {
		return dtRipartizioneOrig;
	}

	public void setDtRipartizioneOrig(String dtRipartizioneOrig) {
		this.dtRipartizioneOrig = dtRipartizioneOrig;
	}

	public String getProgRipartizioneOrig() {
		return progRipartizioneOrig;
	}

	public void setProgRipartizioneOrig(String progRipartizioneOrig) {
		this.progRipartizioneOrig = progRipartizioneOrig;
	}

	public String getDtBonificoOrig() {
		return dtBonificoOrig;
	}

	public void setDtBonificoOrig(String dtBonificoOrig) {
		this.dtBonificoOrig = dtBonificoOrig;
	}

	public String getCodEnte() {
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public String getDtRiscossione() {
		return dtRiscossione;
	}

	public void setDtRiscossione(String dtRiscossione) {
		this.dtRiscossione = dtRiscossione;
	}

	public String getCodEnteCom() {
		return codEnteCom;
	}

	public void setCodEnteCom(String codEnteCom) {
		this.codEnteCom = codEnteCom;
	}

	public String getCodTributo() {
		return codTributo;
	}

	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}

	public String getAnnoRif() {
		return annoRif;
	}

	public void setAnnoRif(String annoRif) {
		this.annoRif = annoRif;
	}

	public String getImpDebito() {
		return impDebito;
	}

	public void setImpDebito(String impDebito) {
		this.impDebito = impDebito;
	}

	public String getImpCredito() {
		return impCredito;
	}

	public void setImpCredito(String impCredito) {
		this.impCredito = impCredito;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getDtOperazione() {
		return dtOperazione;
	}

	public void setDtOperazione(String dtOperazione) {
		this.dtOperazione = dtOperazione;
	}

	public String getTipoImposta() {
		return tipoImposta;
	}

	public void setTipoImposta(String tipoImposta) {
		this.tipoImposta = tipoImposta;
	}

	public String getDescTributo() {
		return descTributo;
	}

	public void setDescTributo(String descTributo) {
		this.descTributo = descTributo;
	}
	
}
