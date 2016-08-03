package it.webred.ct.data.access.basic.catasto.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SoggettoDTO {
	
	private String tipo;
	
	private String descTipo;
	
	private String titolo;
	
	private String nomeSoggetto;
	private String cognomeSoggetto;
	private String cfSoggetto;
	
	private String denominazioneSoggetto;
	private String pivaSoggetto;
	
	private Date dataInizioPoss;
	private Date dataFinePoss;
	private String dataInizioPossStr;
	private String dataFinePossStr;

	private String quota;
	
	private boolean ultimoValido;
	
	private static final SimpleDateFormat sdf= new  SimpleDateFormat("dd/MM/yyyy");
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getNomeSoggetto() {
		return nomeSoggetto;
	}
	public void setNomeSoggetto(String nomeSoggetto) {
		this.nomeSoggetto = nomeSoggetto;
	}
	public String getCognomeSoggetto() {
		return cognomeSoggetto;
	}
	public void setCognomeSoggetto(String cognomeSoggetto) {
		this.cognomeSoggetto = cognomeSoggetto;
	}
	public String getCfSoggetto() {
		return cfSoggetto;
	}
	public void setCfSoggetto(String cfSoggetto) {
		this.cfSoggetto = cfSoggetto;
	}
	public String getDenominazioneSoggetto() {
		return denominazioneSoggetto;
	}
	public void setDenominazioneSoggetto(String denominazioneSoggetto) {
		this.denominazioneSoggetto = denominazioneSoggetto;
	}
	public String getPivaSoggetto() {
		return pivaSoggetto;
	}
	public void setPivaSoggetto(String pivaSoggetto) {
		this.pivaSoggetto = pivaSoggetto;
	}
	
	public boolean isUltimoValido() {
		return ultimoValido;
	}
	public void setUltimoValido(boolean ultimoValido) {
		this.ultimoValido = ultimoValido;
	}
	
	public String getQuota() {
		return quota;
	}
	public void setQuota(String quota) {
		this.quota = quota;
	}
	
	public String getNominativoVis() {
		String nomin = "-";
		if(getTipo().equalsIgnoreCase("P") || (getCfSoggetto() != null && !getCfSoggetto().trim().equals(""))) {
			nomin = getCognomeSoggetto();
			String nome = getNomeSoggetto();
			if (nome != null && !nome.equals("")) {
				if (!nomin.equals("")) {
					nomin += " ";
				}
				nomin += nome;
			}
		} else {
			nomin = getDenominazioneSoggetto();
		}
		return nomin;
	}
	public Date getDataInizioPoss() {
		return dataInizioPoss;
	}
	public void setDataInizioPoss(Date dataInizioPoss) {
		this.dataInizioPoss = dataInizioPoss;
		if (dataInizioPoss !=null)	
			this.dataInizioPossStr = sdf.format(dataInizioPoss);
		else
			this.dataInizioPossStr ="";
	}
	public Date getDataFinePoss() {
		return dataFinePoss;
	}
	public void setDataFinePoss(Date dataFinePoss) {
		this.dataFinePoss = dataFinePoss;
		if (dataFinePoss !=null)	
			this.dataFinePossStr = sdf.format(dataFinePoss);
		else
			this.dataFinePossStr ="";
	}
	
	public String getDescTipo() {
		
		this.descTipo = (this.tipo!=null && this.tipo.equalsIgnoreCase("G")? "Persona Giuridica" : "Persona Fisica");
		
		return descTipo;
	}
	public String getDataInizioPossStr() {
		return dataInizioPossStr;
	}
	public void setDataInizioPossStr(String dataInizioPossStr) {
		this.dataInizioPossStr = dataInizioPossStr;
	}
	public String getDataFinePossStr() {
		return dataFinePossStr;
	}
	public void setDataFinePossStr(String dataFinePossStr) {
		this.dataFinePossStr = dataFinePossStr;
	}
	
	

}
