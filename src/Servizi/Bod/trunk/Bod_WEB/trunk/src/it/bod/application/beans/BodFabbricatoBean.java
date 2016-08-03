package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class BodFabbricatoBean extends MasterItem{

	private static final long serialVersionUID = -2480203448826777754L;
	
	private Long idFab = null;
	private String sezione = "";
	private String foglio = "";
	private String particella = "";
	private String amministrativa = "";
	private String denominatore = "";
	private String edificialita = "";
	private String nomeAllegato = "";
//	private Long segFk = new Long(0);
	
	public Long getIdFab() {
		return idFab;
	}
	public void setIdFab(Long idFab) {
		this.idFab = idFab;
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
