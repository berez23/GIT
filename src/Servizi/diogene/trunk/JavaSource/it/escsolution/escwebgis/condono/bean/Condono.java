/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.condono.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Condono extends EscObject implements Serializable{

	
	private String chiave;
	
	private String codCondono;
	private String progressivo;
	private String dataInsPratica;
	
	private String codiceVia;
	private String progVia;
	private String descrVia;
	private String civico;
	private String barrato;
	
	private String esibente;
	private String cfPiEsibente;
	private String indirizzoEsibente;
	private String civicoEsibente;
	private String barratoEsibente;
	private String capEsibente;
	private String comuneEsibente;
	private String provEsibente;
	
	private String note;
	private String relazione;
	private String tipoPratica;
	private String esito;
	private String flagPeriodoAbuso;
	private String descrDatiCatastali;
	private String annoAbuso;
	
	private String superficieAbuso;
	private String snrAbuso;
	private String piano;
	private String destIniziale;
	private String destFinale;
	
	private String computoDichiaratoL;
	private String computoDichiaratoE;
	private String oblazioneCalcolataL;
	private String oblazioneCalcolataE;

	private String oneriPrimariE;
	private String oneriPrimariL;
	private String oneriSecondariE;
	private String oneriSecondariL;
	private String smaltRifiutiE;
	private String smaltRifiutiL;
	private String costoCostrE;
	private String costoCostrL;
	
	private String tipoOnere;
	private String tipoAbuso;
	private String subcatastale;	

	
	public Condono(){}
	
	
	public String getChiave(){
		if (chiave != null) {
			return chiave;
		}
		return ""+codCondono+"|"+progressivo;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getBarrato() {
		return barrato;
	}


	public void setBarrato(String barrato) {
		this.barrato = barrato;
	}


	public String getCfPiEsibente() {
		return cfPiEsibente;
	}


	public void setCfPiEsibente(String cfPiEsibente) {
		this.cfPiEsibente = cfPiEsibente;
	}


	public String getCivico() {
		return civico;
	}


	public void setCivico(String civico) {
		this.civico = civico;
	}


	public String getCodCondono() {
		return codCondono;
	}


	public void setCodCondono(String codCondono) {
		this.codCondono = codCondono;
	}


	public String getCodiceVia() {
		return codiceVia;
	}


	public void setCodiceVia(String codiceVia) {
		this.codiceVia = codiceVia;
	}


	public String getDataInsPratica() {
		return dataInsPratica;
	}


	public void setDataInsPratica(String dataInsPratica) {
		this.dataInsPratica = dataInsPratica;
	}


	public String getDescrVia() {
		return descrVia;
	}


	public void setDescrVia(String descrVia) {
		this.descrVia = descrVia;
	}


	public String getEsibente() {
		return esibente;
	}


	public void setEsibente(String esibente) {
		this.esibente = esibente;
	}


	public String getProgressivo() {
		return progressivo;
	}


	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}


	public String getProgVia() {
		return progVia;
	}


	public void setProgVia(String progVia) {
		this.progVia = progVia;
	}


	public String getAnnoAbuso() {
		return annoAbuso;
	}


	public void setAnnoAbuso(String annoAbuso) {
		this.annoAbuso = annoAbuso;
	}


	public String getBarratoEsibente() {
		return barratoEsibente;
	}


	public void setBarratoEsibente(String barratoEsibente) {
		this.barratoEsibente = barratoEsibente;
	}


	public String getCapEsibente() {
		return capEsibente;
	}


	public void setCapEsibente(String capEsibente) {
		this.capEsibente = capEsibente;
	}


	public String getCivicoEsibente() {
		return civicoEsibente;
	}


	public void setCivicoEsibente(String civicoEsibente) {
		this.civicoEsibente = civicoEsibente;
	}


	public String getComputoDichiaratoE() {
		return computoDichiaratoE;
	}


	public void setComputoDichiaratoE(String computoDichiaratoE) {
		this.computoDichiaratoE = computoDichiaratoE;
	}


	public String getComputoDichiaratoL() {
		return computoDichiaratoL;
	}


	public void setComputoDichiaratoL(String computoDichiaratoL) {
		this.computoDichiaratoL = computoDichiaratoL;
	}


	public String getComuneEsibente() {
		return comuneEsibente;
	}


	public void setComuneEsibente(String comuneEsibente) {
		this.comuneEsibente = comuneEsibente;
	}


	public String getCostoCostrE() {
		return costoCostrE;
	}


	public void setCostoCostrE(String costoCostrE) {
		this.costoCostrE = costoCostrE;
	}


	public String getCostoCostrL() {
		return costoCostrL;
	}


	public void setCostoCostrL(String costoCostrL) {
		this.costoCostrL = costoCostrL;
	}


	public String getDescrDatiCatastali() {
		return descrDatiCatastali;
	}


	public void setDescrDatiCatastali(String descrDatiCatastali) {
		this.descrDatiCatastali = descrDatiCatastali;
	}


	public String getDestFinale() {
		return destFinale;
	}


	public void setDestFinale(String destFinale) {
		this.destFinale = destFinale;
	}


	public String getDestIniziale() {
		return destIniziale;
	}


	public void setDestIniziale(String destIniziale) {
		this.destIniziale = destIniziale;
	}


	public String getEsito() {
		return esito;
	}


	public void setEsito(String esito) {
		this.esito = esito;
	}


	public String getFlagPeriodoAbuso() {
		return flagPeriodoAbuso;
	}


	public void setFlagPeriodoAbuso(String flagPeriodoAbuso) {
		this.flagPeriodoAbuso = flagPeriodoAbuso;
	}


	public String getIndirizzoEsibente() {
		return indirizzoEsibente;
	}


	public void setIndirizzoEsibente(String indirizzoEsibente) {
		this.indirizzoEsibente = indirizzoEsibente;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public String getOblazioneCalcolataE() {
		return oblazioneCalcolataE;
	}


	public void setOblazioneCalcolataE(String oblazioneCalcolataE) {
		this.oblazioneCalcolataE = oblazioneCalcolataE;
	}


	public String getOblazioneCalcolataL() {
		return oblazioneCalcolataL;
	}


	public void setOblazioneCalcolataL(String oblazioneCalcolataL) {
		this.oblazioneCalcolataL = oblazioneCalcolataL;
	}


	public String getOneriPrimariE() {
		return oneriPrimariE;
	}


	public void setOneriPrimariE(String oneriPrimariE) {
		this.oneriPrimariE = oneriPrimariE;
	}


	public String getOneriPrimariL() {
		return oneriPrimariL;
	}


	public void setOneriPrimariL(String oneriPrimariL) {
		this.oneriPrimariL = oneriPrimariL;
	}


	public String getOneriSecondariE() {
		return oneriSecondariE;
	}


	public void setOneriSecondariE(String oneriSecondariE) {
		this.oneriSecondariE = oneriSecondariE;
	}


	public String getOneriSecondariL() {
		return oneriSecondariL;
	}


	public void setOneriSecondariL(String oneriSecondariL) {
		this.oneriSecondariL = oneriSecondariL;
	}


	public String getPiano() {
		return piano;
	}


	public void setPiano(String piano) {
		this.piano = piano;
	}


	public String getProvEsibente() {
		return provEsibente;
	}


	public void setProvEsibente(String provEsibente) {
		this.provEsibente = provEsibente;
	}


	public String getRelazione() {
		return relazione;
	}


	public void setRelazione(String relazione) {
		this.relazione = relazione;
	}


	public String getSmaltRifiutiE() {
		return smaltRifiutiE;
	}


	public void setSmaltRifiutiE(String smaltRifiutiE) {
		this.smaltRifiutiE = smaltRifiutiE;
	}


	public String getSmaltRifiutiL() {
		return smaltRifiutiL;
	}


	public void setSmaltRifiutiL(String smaltRifiutiL) {
		this.smaltRifiutiL = smaltRifiutiL;
	}


	public String getSnrAbuso() {
		return snrAbuso;
	}


	public void setSnrAbuso(String snrAbuso) {
		this.snrAbuso = snrAbuso;
	}


	public String getSuperficieAbuso() {
		return superficieAbuso;
	}


	public void setSuperficieAbuso(String superficieAbuso) {
		this.superficieAbuso = superficieAbuso;
	}


	public String getTipoPratica() {
		return tipoPratica;
	}


	public void setTipoPratica(String tipoPratica) {
		this.tipoPratica = tipoPratica;
	}


	public String getTipoOnere() {
		return tipoOnere;
	}


	public void setTipoOnere(String tipoOnere) {
		this.tipoOnere = tipoOnere;
	}


	public String getTipoAbuso() {
		return tipoAbuso;
	}


	public void setTipoAbuso(String tipoAbuso) {
		this.tipoAbuso = tipoAbuso;
	}


	public String getSubcatastale() {
		return subcatastale;
	}


	public void setSubcatastale(String subcatastale) {
		this.subcatastale = subcatastale;
	}


}
