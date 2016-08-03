package it.webred.cs.csa.web.manbean.report.dto;

public class ReportPdfDTO extends BasePdfDTO {

	private String pieDiPagina;
	private String assSociale = EMPTY_VALUE;
	private String comune;
	private String dataOdierna;
	
	public String getPieDiPagina() {
		return pieDiPagina;
	}

	public void setPieDiPagina(String pieDiPagina) {
		this.pieDiPagina = pieDiPagina;
	}

	public String getAssSociale() {
		return assSociale;
	}

	public void setAssSociale(String assSociale) {
		this.assSociale = assSociale;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getDataOdierna() {
		return dataOdierna;
	}

	public void setDataOdierna(String dataOdierna) {
		this.dataOdierna = dataOdierna;
	}
	
}
