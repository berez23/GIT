package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class BodUiuBean extends MasterItem{

	private static final long serialVersionUID = -9219705358893878376L;
	
	private Long idUiu = null;
	private String sezione = "";
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	private String amministrativa = "";
	private String denominatore = "";
	private String edificialita = "";
	private String nomeAllegato = "";
	private Boolean incConsistenza = false;
	private Boolean incDestinazione = false;
	private Boolean incPlanimetria = false;
	private Boolean incClassamento = false;
//	private Long segFk = new Long(0);
	
	public Long getIdUiu() {
		return idUiu;
	}
	public void setIdUiu(Long idUiu) {
		this.idUiu = idUiu;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
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
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public String getAmministrativa() {
		return amministrativa;
	}
	public void setAmministrativa(String amministrativa) {
		this.amministrativa = amministrativa;
	}
	public String getDenominatore() {
		return denominatore;
	}
	public void setDenominatore(String denominatore) {
		this.denominatore = denominatore;
	}
	public String getEdificialita() {
		return edificialita;
	}
	public void setEdificialita(String edificialita) {
		this.edificialita = edificialita;
	}
	public String getNomeAllegato() {
		return nomeAllegato;
	}
	public void setNomeAllegato(String nomeAllegato) {
		this.nomeAllegato = nomeAllegato;
	}
	public Boolean getIncConsistenza() {
		return incConsistenza;
	}
	public void setIncConsistenza(Boolean incConsistenza) {
		this.incConsistenza = incConsistenza;
	}
	public Boolean getIncDestinazione() {
		return incDestinazione;
	}
	public void setIncDestinazione(Boolean incDestinazione) {
		this.incDestinazione = incDestinazione;
	}
	public Boolean getIncPlanimetria() {
		return incPlanimetria;
	}
	public void setIncPlanimetria(Boolean incPlanimetria) {
		this.incPlanimetria = incPlanimetria;
	}
	public Boolean getIncClassamento() {
		return incClassamento;
	}
	public void setIncClassamento(Boolean incClassamento) {
		this.incClassamento = incClassamento;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
//	public Long getSegFk() {
//		return segFk;
//	}
//	public void setSegFk(Long segId) {
//		this.segFk = segId;
//	}
	
	
	

}
