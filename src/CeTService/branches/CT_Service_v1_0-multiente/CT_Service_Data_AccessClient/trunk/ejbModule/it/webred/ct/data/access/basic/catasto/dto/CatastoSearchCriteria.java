
package it.webred.ct.data.access.basic.catasto.dto;

import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Sitideco;

import java.io.Serializable;



public class CatastoSearchCriteria implements Serializable {

	private String codNazionale;
	private String codCategoria;
	
	private Boolean nonANorma;

	private String foglio;
	private String particella;
	private String unimm;
	
	//Ricerca per indirizzo
	private String idVia;
	private String idCivico;
	
	private String idSoggetto;
	

	public String getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(String idSoggetto) {
		this.idSoggetto = idSoggetto;
	}
	//Operatori di ricerca delle stringhe
	private String viaOp;
	
	//Operatori di ricerca delle stringhe
	private String nomeSoggettoOp;
	
	private String cognomeSoggettoOp;
	
	private String denominazioneSoggettoOp;
	

	public String getCodNazionale() {
		return codNazionale;
	}

	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}

	public String getNomeSoggettoOp() {
		return nomeSoggettoOp;
	}

	public void setNomeSoggettoOp(String nomeSoggettoOp) {
		this.nomeSoggettoOp = nomeSoggettoOp;
	}

	public String getCognomeSoggettoOp() {
		return cognomeSoggettoOp;
	}

	public void setCognomeSoggettoOp(String cognomeSoggettoOp) {
		this.cognomeSoggettoOp = cognomeSoggettoOp;
	}

	public String getDenominazioneSoggettoOp() {
		return denominazioneSoggettoOp;
	}

	public void setDenominazioneSoggettoOp(String denominazioneSoggettoOp) {
		this.denominazioneSoggettoOp = denominazioneSoggettoOp;
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
	
	public String getUnimm() {
		return unimm;
	}
	public void setUnimm(String unimm) {
		this.unimm = unimm;
	}

	
	public String getIdVia() {
		return idVia;
	}

	public void setIdVia(String idVia) {
		this.idVia = idVia;
	}

	public String getIdCivico() {
		return idCivico;
	}

	public void setIdCivico(String idCivico) {
		this.idCivico = idCivico;
	}

	
	public void setNonANorma(Boolean nonANorma) {
		this.nonANorma = nonANorma;
	}

	public Boolean getNonANorma() {
		return nonANorma;
	}


	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
	}

	public String getCodCategoria() {
		return codCategoria;
		
	}

}
