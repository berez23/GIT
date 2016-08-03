package it.webred.cs.csa.web.manbean.report.dto.cartella;

import it.webred.cs.csa.web.manbean.report.dto.BasePdfDTO;

public class DatiSocialiPdfDTO extends BasePdfDTO {

	private String tipologiaFamiliare = EMPTY_VALUE;
	private String minoriPresenti = EMPTY_VALUE;
	private String problematica = EMPTY_VALUE;
	private	String titoloStudio = EMPTY_VALUE;
	
	public String getTipologiaFamiliare() {
		return tipologiaFamiliare;
	}
	public void setTipologiaFamiliare(String tipologiaFamiliare) {
		this.tipologiaFamiliare = format(tipologiaFamiliare);
	}
	public String getMinoriPresenti() {
		return minoriPresenti;
	}
	public void setMinoriPresenti(String minoriPresenti) {
		this.minoriPresenti = format(minoriPresenti);
	}
	public String getProblematica() {
		return problematica;
	}
	public void setProblematica(String problematica) {
		this.problematica = format(problematica);
	}
	public String getTitoloStudio() {
		return titoloStudio;
	}
	public void setTitoloStudio(String titoloStudio) {
		this.titoloStudio = format(titoloStudio);
	}
	
}
