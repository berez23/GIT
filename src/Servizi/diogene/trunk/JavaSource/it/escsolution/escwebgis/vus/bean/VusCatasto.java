/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.vus.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class VusCatasto extends EscObject implements Serializable
{
	private String codServizio;
	private String codAnagrafico;
	private String ragSociale;
	private String codComAmmin;
	private String desComAmmin;
	private String ubicFornitDesLoc;
	private String ubicFornitDesVia;
	private String ubicFornitCivico;
	private String codComuneCatastale;
    private String comuneCatastale;
    private String sottoTipoStato;
    private String datiInesIncomp;
    private String tipoCatasto;
    private String sezioneUrbana;
    private String foglio;
    private String particella;
    private String particellaSistTavolare;
    private String tipoParticella;
    private String subalterno;
    private String chiave;
	
	
	public VusCatasto()
	{
		this.codServizio="";
		this.codAnagrafico="";
		this.ragSociale="";
		this.codComAmmin="";
		this.desComAmmin="";
		this.ubicFornitDesLoc="";
		this.ubicFornitDesVia="";
		this.ubicFornitCivico="";
		this.codComuneCatastale="";
		this.comuneCatastale="";
		this.sottoTipoStato="";
		this.datiInesIncomp="";
		this.tipoCatasto="";
		this.sezioneUrbana="";
		this.foglio="";
		this.particella="";
		this.particellaSistTavolare="";
		this.tipoParticella="";
		this.subalterno="";
	    this.chiave="";
	}


	public String getChiave() {
		return this.codServizio+"|"+this.getFoglio()+"|"+this.getParticella()+"|"+this.getSubalterno();
	}
	
	public String getCodAnagrafico() {
		return codAnagrafico;
	}


	public void setCodAnagrafico(String codAnagrafico) {
		this.codAnagrafico = codAnagrafico;
	}


	public String getCodComAmmin() {
		return codComAmmin;
	}


	public void setCodComAmmin(String codComAmmin) {
		this.codComAmmin = codComAmmin;
	}


	public String getCodComuneCatastale() {
		return codComuneCatastale;
	}


	public void setCodComuneCatastale(String codComuneCatastale) {
		this.codComuneCatastale = codComuneCatastale;
	}


	public String getCodServizio() {
		return codServizio;
	}


	public void setCodServizio(String codServizio) {
		this.codServizio = codServizio;
	}


	public String getComuneCatastale() {
		return comuneCatastale;
	}


	public void setComuneCatastale(String comuneCatastale) {
		this.comuneCatastale = comuneCatastale;
	}


	public String getDatiInesIncomp() {
		return datiInesIncomp;
	}


	public void setDatiInesIncomp(String datiInesIncomp) {
		this.datiInesIncomp = datiInesIncomp;
	}


	public String getDesComAmmin() {
		return desComAmmin;
	}


	public void setDesComAmmin(String desComAmmin) {
		this.desComAmmin = desComAmmin;
	}


	public String getFoglio() {
		return foglio;
	}


	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}


	public String getParticella() {
		return particella;
	}


	public void setParticella(String particella) {
		this.particella = particella;
	}


	public String getParticellaSistTavolare() {
		return particellaSistTavolare;
	}


	public void setParticellaSistTavolare(String particellaSistTavolare) {
		this.particellaSistTavolare = particellaSistTavolare;
	}


	public String getRagSociale() {
		return ragSociale;
	}


	public void setRagSociale(String ragSociale) {
		this.ragSociale = ragSociale;
	}


	public String getSezioneUrbana() {
		return sezioneUrbana;
	}


	public void setSezioneUrbana(String sezioneUrbana) {
		this.sezioneUrbana = sezioneUrbana;
	}


	public String getSottoTipoStato() {
		return sottoTipoStato;
	}


	public void setSottoTipoStato(String sottoTipoStato) {
		this.sottoTipoStato = sottoTipoStato;
	}


	public String getSubalterno() {
		return subalterno;
	}


	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}


	public String getTipoCatasto() {
		return tipoCatasto;
	}


	public void setTipoCatasto(String tipoCatasto) {
		this.tipoCatasto = tipoCatasto;
	}


	public String getTipoParticella() {
		return tipoParticella;
	}


	public void setTipoParticella(String tipoParticella) {
		this.tipoParticella = tipoParticella;
	}


	public String getUbicFornitCivico() {
		return ubicFornitCivico;
	}


	public void setUbicFornitCivico(String ubicFornitCivico) {
		this.ubicFornitCivico = ubicFornitCivico;
	}


	public String getUbicFornitDesLoc() {
		return ubicFornitDesLoc;
	}


	public void setUbicFornitDesLoc(String ubicFornitDesLoc) {
		this.ubicFornitDesLoc = ubicFornitDesLoc;
	}


	public String getUbicFornitDesVia() {
		return ubicFornitDesVia;
	}


	public void setUbicFornitDesVia(String ubicFornitDesVia) {
		this.ubicFornitDesVia = ubicFornitDesVia;
	}



}
