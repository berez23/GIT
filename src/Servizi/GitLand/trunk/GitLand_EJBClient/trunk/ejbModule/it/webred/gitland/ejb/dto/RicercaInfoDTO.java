package it.webred.gitland.ejb.dto;

public class RicercaInfoDTO extends BaseDTO {

	private static final long serialVersionUID = 6766280118122125637L;

	private Integer maxResult;
	
	private String foglio;
	private String mappale;
	private String codEnte;
	
	private String ragioneSociale = "";
	private String piva = "";
	private String codiceFiscale = "";
	private String codiceAttivita = "";
	private String denominazioneRapprLegale = "";
	
	public RicercaInfoDTO(){
	}//-------------------------------------------------------------------------
	
	public String getFoglio() {
		return foglio;
	}
	
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	
	public String getMappale() {
		return mappale;
	}
	
	public void setMappale(String mappale) {
		this.mappale = mappale;
	}
	
	public String getCodEnte() {
		return codEnte;
	}
	
	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	
	public Integer getMaxResult() {
		return maxResult;
	}
	
	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCodiceAttivita() {
		return codiceAttivita;
	}

	public void setCodiceAttivita(String codiceAttivita) {
		this.codiceAttivita = codiceAttivita;
	}

	public String getDenominazioneRapprLegale() {
		return denominazioneRapprLegale;
	}

	public void setDenominazioneRapprLegale(String denominazioneRapprLegale) {
		this.denominazioneRapprLegale = denominazioneRapprLegale;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
