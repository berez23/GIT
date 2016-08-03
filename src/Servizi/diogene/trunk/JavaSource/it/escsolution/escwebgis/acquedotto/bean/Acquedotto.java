/*
 * Created on 10-nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.escsolution.escwebgis.acquedotto.bean;

import it.escsolution.escwebgis.common.EscObject;

/**
 * @author Utente
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Acquedotto extends EscObject {
	private String
		chiave,
		nominativo,
		cfPi,
		appartamenti,
		utentifinali,
		tipologia,
		indirizzoFornitura,
		numeroCivicoFornitura,
		parteCivicoFornitura,
		CAPFornitura,
		comuneFornitura,
		provinciaFornitura,
		nominativoSpedizione,
		tipologiaSpedizione,
		indirizzoSpedizione,
		numeroCivicoSpedizione,
		parteCivicoSpedizione,
		CAPSpedizione,
		comuneSpedizione,
		provinciaSpedizione;
	
	/* (non-Javadoc)
	 * @see it.escsolution.escwebgis.common.EscObject#getChiave()
	 */
	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}
	/**
	 * @return Returns the appartamenti.
	 */
	public String getAppartamenti() {
		return appartamenti;
	}
	/**
	 * @param appartamenti The appartamenti to set.
	 */
	public void setAppartamenti(String appartamenti) {
		this.appartamenti = appartamenti;
	}
	/**
	 * @return Returns the cAPFornitura.
	 */
	public String getCAPFornitura() {
		return CAPFornitura;
	}
	/**
	 * @param fornitura The cAPFornitura to set.
	 */
	public void setCAPFornitura(String fornitura) {
		CAPFornitura = fornitura;
	}
	/**
	 * @return Returns the cAPSpedizione.
	 */
	public String getCAPSpedizione() {
		return CAPSpedizione;
	}
	/**
	 * @param spedizione The cAPSpedizione to set.
	 */
	public void setCAPSpedizione(String spedizione) {
		CAPSpedizione = spedizione;
	}
	/**
	 * @return Returns the cfPi.
	 */
	public String getCfPi() {
		return cfPi;
	}
	/**
	 * @param cfPi The cfPi to set.
	 */
	public void setCfPi(String cfPi) {
		this.cfPi = cfPi;
	}
	/**
	 * @return Returns the comuneFornitura.
	 */
	public String getComuneFornitura() {
		return comuneFornitura;
	}
	/**
	 * @param comuneFornitura The comuneFornitura to set.
	 */
	public void setComuneFornitura(String comuneFornitura) {
		this.comuneFornitura = comuneFornitura;
	}
	/**
	 * @return Returns the comuneSpedizione.
	 */
	public String getComuneSpedizione() {
		return comuneSpedizione;
	}
	/**
	 * @param comuneSpedizione The comuneSpedizione to set.
	 */
	public void setComuneSpedizione(String comuneSpedizione) {
		this.comuneSpedizione = comuneSpedizione;
	}
	/**
	 * @return Returns the indirizzoFornitura.
	 */
	public String getIndirizzoFornitura() {
		return indirizzoFornitura;
	}
	/**
	 * @param indirizzoFornitura The indirizzoFornitura to set.
	 */
	public void setIndirizzoFornitura(String indirizzoFornitura) {
		this.indirizzoFornitura = indirizzoFornitura;
	}
	/**
	 * @return Returns the indirizzoSpedizione.
	 */
	public String getIndirizzoSpedizione() {
		return indirizzoSpedizione;
	}
	/**
	 * @param indirizzoSpedizione The indirizzoSpedizione to set.
	 */
	public void setIndirizzoSpedizione(String indirizzoSpedizione) {
		this.indirizzoSpedizione = indirizzoSpedizione;
	}
	/**
	 * @return Returns the nominativo.
	 */
	public String getNominativo() {
		return nominativo;
	}
	/**
	 * @param nominativo The nominativo to set.
	 */
	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}
	/**
	 * @return Returns the nominativoSpedizione.
	 */
	public String getNominativoSpedizione() {
		return nominativoSpedizione;
	}
	/**
	 * @param nominativoSpedizione The nominativoSpedizione to set.
	 */
	public void setNominativoSpedizione(String nominativoSpedizione) {
		this.nominativoSpedizione = nominativoSpedizione;
	}
	/**
	 * @return Returns the numeroCivicoFornitura.
	 */
	public String getNumeroCivicoFornitura() {
		return numeroCivicoFornitura;
	}
	/**
	 * @param numeroCivicoFornitura The numeroCivicoFornitura to set.
	 */
	public void setNumeroCivicoFornitura(String numeroCivicoFornitura) {
		this.numeroCivicoFornitura = numeroCivicoFornitura;
	}
	/**
	 * @return Returns the numeroCivicoSpedizione.
	 */
	public String getNumeroCivicoSpedizione() {
		return numeroCivicoSpedizione;
	}
	/**
	 * @param numeroCivicoSpedizione The numeroCivicoSpedizione to set.
	 */
	public void setNumeroCivicoSpedizione(String numeroCivicoSpedizione) {
		this.numeroCivicoSpedizione = numeroCivicoSpedizione;
	}
	/**
	 * @return Returns the parteCivicoFornitura.
	 */
	public String getParteCivicoFornitura() {
		return parteCivicoFornitura;
	}
	/**
	 * @param parteCivicoFornitura The parteCivicoFornitura to set.
	 */
	public void setParteCivicoFornitura(String parteCivicoFornitura) {
		this.parteCivicoFornitura = parteCivicoFornitura;
	}
	/**
	 * @return Returns the parteCivicoSpedizione.
	 */
	public String getParteCivicoSpedizione() {
		return parteCivicoSpedizione;
	}
	/**
	 * @param parteCivicoSpedizione The parteCivicoSpedizione to set.
	 */
	public void setParteCivicoSpedizione(String parteCivicoSpedizione) {
		this.parteCivicoSpedizione = parteCivicoSpedizione;
	}
	/**
	 * @return Returns the provinciaFornitura.
	 */
	public String getProvinciaFornitura() {
		return provinciaFornitura;
	}
	/**
	 * @param provinciaFornitura The provinciaFornitura to set.
	 */
	public void setProvinciaFornitura(String provinciaFornitura) {
		this.provinciaFornitura = provinciaFornitura;
	}
	/**
	 * @return Returns the provinciaSpedizione.
	 */
	public String getProvinciaSpedizione() {
		return provinciaSpedizione;
	}
	/**
	 * @param provinciaSpedizione The provinciaSpedizione to set.
	 */
	public void setProvinciaSpedizione(String provinciaSpedizione) {
		this.provinciaSpedizione = provinciaSpedizione;
	}
	/**
	 * @return Returns the tipologia.
	 */
	public String getTipologia() {
		return tipologia;
	}
	/**
	 * @param tipologia The tipologia to set.
	 */
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	/**
	 * @return Returns the tipologiaSpedizione.
	 */
	public String getTipologiaSpedizione() {
		return tipologiaSpedizione;
	}
	/**
	 * @param tipologiaSpedizione The tipologiaSpedizione to set.
	 */
	public void setTipologiaSpedizione(String tipologiaSpedizione) {
		this.tipologiaSpedizione = tipologiaSpedizione;
	}
	/**
	 * @return Returns the utentifinali.
	 */
	public String getUtentifinali() {
		return utentifinali;
	}
	/**
	 * @param utentifinali The utentifinali to set.
	 */
	public void setUtentifinali(String utentifinali) {
		this.utentifinali = utentifinali;
	}
}
