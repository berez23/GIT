package it.bod.application.beans;

import java.util.Date;
import java.util.List;

import it.bod.application.common.MasterItem;

public class TrasformazioneEdiliziaBean extends MasterItem{

	private static final long serialVersionUID = -447493146169578526L;
	
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	private String categoria = "";
	private Double rendita = 0d;
	private Double coefficiente = 0d;
	private Double renditaPerCoeff = 0d;
	private String tipoOperazione = "";
	private List<Date> dateVariazioni = null;
	private List<VariazioneCensuaria> variazioniCensuarie = null;
	
	
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
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public Double getRendita() {
		return rendita;
	}
	public void setRendita(Double rendita) {
		this.rendita = rendita;
	}
	public Double getCoefficiente() {
		return coefficiente;
	}
	public void setCoefficiente(Double coefficiente) {
		this.coefficiente = coefficiente;
	}
	public Double getRenditaPerCoeff() {
		return renditaPerCoeff;
	}
	public void setRenditaPerCoeff(Double renditaPerCoeff) {
		this.renditaPerCoeff = renditaPerCoeff;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public List<VariazioneCensuaria> getVariazioniCensuarie() {
		return variazioniCensuarie;
	}
	public void setVariazioniCensuarie(List<VariazioneCensuaria> varizaioneCensuarie) {
		this.variazioniCensuarie = varizaioneCensuarie;
	}
	public List<Date> getDateVariazioni() {
		return dateVariazioni;
	}
	public void setDateVariazioni(List<Date> dateVariazioni) {
		this.dateVariazioni = dateVariazioni;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	
	
}
