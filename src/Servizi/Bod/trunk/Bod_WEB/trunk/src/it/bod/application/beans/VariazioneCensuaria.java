package it.bod.application.beans;

import java.math.BigDecimal;
import java.util.Date;

import it.bod.application.common.MasterItem;

public class VariazioneCensuaria extends MasterItem{

	private static final long serialVersionUID = -8903024818983712459L;
	
	private Date dataInizio = null;
	private Date dataFine = null;
	private String categoria = "";
	private String classe = "";
	private BigDecimal consistenza = null;
	private BigDecimal rendita = null;
	private BigDecimal superficie = null;
	
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public BigDecimal getConsistenza() {
		return consistenza;
	}
	public void setConsistenza(BigDecimal consistenza) {
		this.consistenza = consistenza;
	}
	public BigDecimal getRendita() {
		return rendita;
	}
	public void setRendita(BigDecimal rendita) {
		this.rendita = rendita;
	}
	public BigDecimal getSuperficie() {
		return superficie;
	}
	public void setSuperficie(BigDecimal superficie) {
		this.superficie = superficie;
	}
	
	

}
