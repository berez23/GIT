package it.bod.application.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.bod.application.common.MasterItem;

public class DocfaClasseBean  extends MasterItem{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2377658368418460436L;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String zona = "";
	private String categoria = "";
	private String classe = "";
	private BigDecimal tariffa=null;
	private BigDecimal tariffaEuro=null;
	
	
	private Double renditaComplessiva=null;
	
	public Double getRenditaComplessiva() {
		return renditaComplessiva;
	}
	public void setRenditaComplessiva(Double renditaComplessiva) {
		this.renditaComplessiva = renditaComplessiva;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
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
	public BigDecimal getTariffa() {
		return tariffa;
	}
	public void setTariffa(BigDecimal tariffa) {
		this.tariffa = tariffa;
	}
	public BigDecimal getTariffaEuro() {
		return tariffaEuro;
	}
	public void setTariffaEuro(BigDecimal tariffaEuro) {
		this.tariffaEuro = tariffaEuro;
	}
	
	
	

	
	
	

	

}
