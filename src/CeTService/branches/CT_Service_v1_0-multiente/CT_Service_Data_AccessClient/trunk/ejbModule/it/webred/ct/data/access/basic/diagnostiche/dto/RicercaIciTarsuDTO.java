package it.webred.ct.data.access.basic.diagnostiche.dto;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.data.model.diagnostiche.*;

public class RicercaIciTarsuDTO implements Serializable{
	
	private Long enteSorgenteOrigine;
	private Long progOrigine;
	
	//private String catPkid;
	private String idDwhOrigineSogg;
	private String idDwhOrigineCiv;
	private String dataDocfa;
	private String foglio;
	private String particella;
	private String sub;
	
	/*public String getCatPkid() {
		return catPkid;
	}
	public void setCatPkid(String catPkid) {
		this.catPkid = catPkid;
	}*/
	
	public String getDataDocfa() {
		return dataDocfa;
	}
	public void setDataDocfa(String dataDocfa) {
		this.dataDocfa = dataDocfa;
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
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	
	public String getIdDwhOrigineSogg() {
		return idDwhOrigineSogg;
	}
	public void setIdDwhOrigineSogg(String idDwhOrigineSogg) {
		this.idDwhOrigineSogg = idDwhOrigineSogg;
	}
	public String getIdDwhOrigineCiv() {
		return idDwhOrigineCiv;
	}
	public void setIdDwhOrigineCiv(String idDwhOrigineCiv) {
		this.idDwhOrigineCiv = idDwhOrigineCiv;
	}
	public void setEnteSorgenteOrigine(Long enteSorgenteOrigine) {
		this.enteSorgenteOrigine = enteSorgenteOrigine;
	}
	public void setProgOrigine(Long progOrigine) {
		this.progOrigine = progOrigine;
	}
	public Long getEnteSorgenteOrigine() {
		return enteSorgenteOrigine;
	}
	public Long getProgOrigine() {
		return progOrigine;
	}
	
}
