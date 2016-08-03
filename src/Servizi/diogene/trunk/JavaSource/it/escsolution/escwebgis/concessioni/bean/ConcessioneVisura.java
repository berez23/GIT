package it.escsolution.escwebgis.concessioni.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class ConcessioneVisura extends EscObject implements Serializable{
	
	private String chiave="";
	private Long inxdoc = null;
	private String tipoAtto = "";
	private String dataDoc = "";
	private String numeroAtto = "";
	private String tpv = "";
	private String nomeVia = "";
	private String altraVia = "";
	private Long civico = null;
	private String civicoSub = "";
	private String altriCiv = "";
	private Long privata = null;
	private String riparto = "";
	private String nomeIntestatario = "";
	private String tipologia = "";
	private String destinazione = "";
	private String vincoloAmbientale = "";
	private String numProtGen = "";
	private String numProtSett = "";
	private String annoProtGen = "";
	private String inxvia = "";
	
	public ConcessioneVisura() {
		
	}//-------------------------------------------------------------------------

	public Long getInxdoc() {
		return inxdoc;
	}

	public void setInxdoc(Long inxdoc) {
		this.inxdoc = inxdoc;
	}

	public String getTipoAtto() {
		return tipoAtto;
	}

	public void setTipoAtto(String tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	public String getDataDoc() {
		return dataDoc;
	}

	public void setDataDoc(String dataDoc) {
		this.dataDoc = dataDoc;
	}

	public String getNumeroAtto() {
		return numeroAtto;
	}

	public void setNumeroAtto(String numeroAtto) {
		this.numeroAtto = numeroAtto;
	}

	public String getTpv() {
		return tpv;
	}

	public void setTpv(String tpv) {
		this.tpv = tpv;
	}

	public String getNomeVia() {
		return nomeVia;
	}

	public void setNomeVia(String nomeVia) {
		this.nomeVia = nomeVia;
	}

	public String getAltraVia() {
		return altraVia;
	}

	public void setAltraVia(String altraVia) {
		this.altraVia = altraVia;
	}

	public Long getCivico() {
		return civico;
	}

	public void setCivico(Long civico) {
		this.civico = civico;
	}

	public String getCivicoSub() {
		return civicoSub;
	}

	public void setCivicoSub(String civicoSub) {
		this.civicoSub = civicoSub;
	}

	public String getAltriCiv() {
		return altriCiv;
	}

	public void setAltriCiv(String altriCiv) {
		this.altriCiv = altriCiv;
	}

	public Long getPrivata() {
		return privata;
	}

	public void setPrivata(Long privata) {
		this.privata = privata;
	}

	public String getRiparto() {
		return riparto;
	}

	public void setRiparto(String riparto) {
		this.riparto = riparto;
	}

	public String getNomeIntestatario() {
		return nomeIntestatario;
	}

	public void setNomeIntestatario(String nomeIntestatario) {
		this.nomeIntestatario = nomeIntestatario;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getDestinazione() {
		return destinazione;
	}

	public void setDestinazione(String destinazione) {
		this.destinazione = destinazione;
	}

	public String getVincoloAmbientale() {
		return vincoloAmbientale;
	}

	public void setVincoloAmbientale(String vincoloAmbientale) {
		this.vincoloAmbientale = vincoloAmbientale;
	}

	public String getNumProtGen() {
		return numProtGen;
	}

	public void setNumProtGen(String numProtGen) {
		this.numProtGen = numProtGen;
	}

	public String getNumProtSett() {
		return numProtSett;
	}

	public void setNumProtSett(String numProtSett) {
		this.numProtSett = numProtSett;
	}

	public String getAnnoProtGen() {
		return annoProtGen;
	}

	public void setAnnoProtGen(String annoProtGen) {
		this.annoProtGen = annoProtGen;
	}

	public String getInxvia() {
		return inxvia;
	}

	public void setInxvia(String inxvia) {
		this.inxvia = inxvia;
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	
	

}
