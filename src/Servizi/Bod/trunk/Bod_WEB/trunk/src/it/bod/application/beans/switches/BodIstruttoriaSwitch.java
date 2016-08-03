package it.bod.application.beans.switches;

import it.bod.application.common.MasterItem;

public class BodIstruttoriaSwitch extends MasterItem{

	private static final long serialVersionUID = 5016927463409058153L;

	private Boolean mostraMsgStatusIstruttoria = false;
	private Boolean mostraMsgIstruttoriaSave = false;
	private Boolean mostraMsgChiudiPraticaError = false;
	private Boolean mostraMsgUtenteNonAutorizzato=false;
	private Boolean mostraMsgErrorChiusuraPratica = false;
	private Boolean mostraMsgErrorChiusuraPraticaNoAllegato = false;
	private Boolean disabledPresaInCarico = false;
	private Boolean disabledChiudiPratica = false;
	private Boolean disabledSaveButton = false;
	private Boolean mostraOperatore = false;
	private Boolean operatoreAutorizzato = false;
	

	public Boolean getMostraMsgIstruttoriaSave() {
		return mostraMsgIstruttoriaSave;
	}
	public void setMostraMsgIstruttoriaSave(Boolean mostraMsgIstruttoriaSave) {
		this.mostraMsgIstruttoriaSave = mostraMsgIstruttoriaSave;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Boolean getDisabledSaveButton() {
		return disabledSaveButton;
	}
	public void setDisabledSaveButton(Boolean disabledSaveButton) {
		this.disabledSaveButton = disabledSaveButton;
	}
	public Boolean getMostraMsgChiudiPraticaError() {
		return mostraMsgChiudiPraticaError;
	}
	public void setMostraMsgChiudiPraticaError(Boolean mostraMsgChiudiPratica) {
		this.mostraMsgChiudiPraticaError = mostraMsgChiudiPratica;
	}

	public Boolean getMostraOperatore() {
		return mostraOperatore;
	}
	public void setMostraOperatore(Boolean mostraOperatore) {
		this.mostraOperatore = mostraOperatore;
	}
	public Boolean getDisabledPresaInCarico() {
		return disabledPresaInCarico;
	}
	public void setDisabledPresaInCarico(Boolean disabledPresaInCarico) {
		this.disabledPresaInCarico = disabledPresaInCarico;
	}

	public Boolean getDisabledChiudiPratica() {
		return disabledChiudiPratica;
	}
	public void setDisabledChiudiPratica(Boolean disabledChiudiPratica) {
		this.disabledChiudiPratica = disabledChiudiPratica;
	}
	public Boolean getOperatoreAutorizzato() {
		return operatoreAutorizzato;
	}
	public void setOperatoreAutorizzato(Boolean operatoreAutorizzato) {
		this.operatoreAutorizzato = operatoreAutorizzato;
	}
	public Boolean getMostraMsgUtenteNonAutorizzato() {
		return mostraMsgUtenteNonAutorizzato;
	}
	public void setMostraMsgUtenteNonAutorizzato(
			Boolean mostraMsgUtenteNonAutorizzato) {
		this.mostraMsgUtenteNonAutorizzato = mostraMsgUtenteNonAutorizzato;
	}
	public Boolean getMostraMsgErrorChiusuraPratica() {
		return mostraMsgErrorChiusuraPratica;
	}
	public void setMostraMsgErrorChiusuraPratica(
			Boolean mostraMsgErrorChiusuraPratica) {
		this.mostraMsgErrorChiusuraPratica = mostraMsgErrorChiusuraPratica;
	}
	public Boolean getMostraMsgErrorChiusuraPraticaNoAllegato() {
		return mostraMsgErrorChiusuraPraticaNoAllegato;
	}
	public void setMostraMsgErrorChiusuraPraticaNoAllegato(
			Boolean mostraMsgErrorChiusuraPraticaNoAllegato) {
		this.mostraMsgErrorChiusuraPraticaNoAllegato = mostraMsgErrorChiusuraPraticaNoAllegato;
	}
	public Boolean getMostraMsgStatusIstruttoria() {
		return mostraMsgStatusIstruttoria;
	}
	public void setMostraMsgStatusIstruttoria(Boolean mostraMsgStatusIstruttoria) {
		this.mostraMsgStatusIstruttoria = mostraMsgStatusIstruttoria;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
