package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class ControlloClassamentoConsistenzaBean extends MasterItem{

	private static final long serialVersionUID = 1753301848773433719L;
	
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	private String zona = "";
	private String categoria = "";
	private Boolean mostraClasseMaggiormenteFrequente = false;
	private String classe = "";
	private String classeRif = "";
	private Integer classeMin = 0;
	private String consistenza = "";
	private Double superfMediaMin = 0d;
	private Double superfMediaMax = 0d;
	private Double consisAnomalia = 0d;
	private Double superficie = 0d;
	private Double rendita = 0d;
	private Double rendita100 = 0d;
	private Double rendita105 = 0d;
	private Double valoreCommerciale = 0d;
	private Double valComSuRen100 = 0d;
	private Double valComSuRen100Rif = 0d;
	private Double valComSuRen105 = 0d;
	private Double tariffa = 0d;
	private Double mediaAttesa = 0d;
	private Double mediaAttesaPerVano = 0d;
	private Double rapporto = 0d;
	private Boolean classamentoCompatibile = false;
	private String colore = "";
	
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
	public String getConsistenza() {
		return consistenza;
	}
	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}
	public Double getRendita() {
		return rendita;
	}
	public void setRendita(Double rendita) {
		this.rendita = rendita;
	}
	public Double getRendita100() {
		return rendita100;
	}
	public void setRendita100(Double rendita100) {
		this.rendita100 = rendita100;
	}
	public Double getRendita105() {
		return rendita105;
	}
	public void setRendita105(Double rendita105) {
		this.rendita105 = rendita105;
	}
	public Double getValoreCommerciale() {
		return valoreCommerciale;
	}
	public void setValoreCommerciale(Double valoreCommerciale) {
		this.valoreCommerciale = valoreCommerciale;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Double getSuperficie() {
		return superficie;
	}
	public void setSuperficie(Double superficie) {
		this.superficie = superficie;
	}
	public Double getValComSuRen100() {
		return valComSuRen100;
	}
	public void setValComSuRen100(Double valComSuRen100) {
		this.valComSuRen100 = valComSuRen100;
	}
	public Double getValComSuRen100Rif() {
		return valComSuRen100Rif;
	}
	public void setValComSuRen100Rif(Double valComSuRen100Rif) {
		this.valComSuRen100Rif = valComSuRen100Rif;
	}
	public Double getValComSuRen105() {
		return valComSuRen105;
	}
	public void setValComSuRen105(Double valComSuRen105) {
		this.valComSuRen105 = valComSuRen105;
	}
	public Double getTariffa() {
		return tariffa;
	}
	public void setTariffa(Double tariffa) {
		this.tariffa = tariffa;
	}
	public Double getMediaAttesa() {
		return mediaAttesa;
	}
	public void setMediaAttesa(Double mediaAttesa) {
		this.mediaAttesa = mediaAttesa;
	}
	public Double getRapporto() {
		return rapporto;
	}
	public void setRapporto(Double rapporto) {
		this.rapporto = rapporto;
	}
	public Double getSuperfMediaMin() {
		return superfMediaMin;
	}
	public void setSuperfMediaMin(Double consistenzaMin) {
		this.superfMediaMin = consistenzaMin;
	}
	public Double getSuperfMediaMax() {
		return superfMediaMax;
	}
	public void setSuperfMediaMax(Double consistenzaMax) {
		this.superfMediaMax = consistenzaMax;
	}
	public Double getConsisAnomalia() {
		return consisAnomalia;
	}
	public void setConsisAnomalia(Double superfSuConsis) {
		this.consisAnomalia = superfSuConsis;
	}
	public Boolean getClassamentoCompatibile() {
		return classamentoCompatibile;
	}
	public void setClassamentoCompatibile(Boolean classaMentoCompatibile) {
		this.classamentoCompatibile = classaMentoCompatibile;
	}
	public String getClasseRif() {
		return classeRif;
	}
	public void setClasseRif(String classeRif) {
		this.classeRif = classeRif;
	}
	public Integer getClasseMin() {
		return classeMin;
	}
	public void setClasseMin(Integer classeMin) {
		this.classeMin = classeMin;
	}
	public String getColore() {
		return colore;
	}
	public void setColore(String colore) {
		this.colore = colore;
	}
	public Double getMediaAttesaPerVano() {
		return mediaAttesaPerVano;
	}
	public void setMediaAttesaPerVano(Double mediaAttesaPerVano) {
		this.mediaAttesaPerVano = mediaAttesaPerVano;
	}
	public Boolean getMostraClasseMaggiormenteFrequente() {
		return mostraClasseMaggiormenteFrequente;
	}
	public void setMostraClasseMaggiormenteFrequente(
			Boolean mostraClasseMaggiormenteFrequente) {
		this.mostraClasseMaggiormenteFrequente = mostraClasseMaggiormenteFrequente;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
