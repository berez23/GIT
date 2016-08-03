package it.bod.application.beans;

import java.util.List;

import it.bod.application.common.MasterItem;

public class ClassamentoCorpoFabbricaBean extends MasterItem{

	private static final long serialVersionUID = 3528577199230900845L;
	
	private String foglio = "";
	private String particella = "";
	private List<Object> corpiFabbrica = null;
	
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
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public List<Object> getCorpiFabbrica() {
		return corpiFabbrica;
	}
	public void setCorpiFabbrica(List<Object> corpiFabbrica) {
		this.corpiFabbrica = corpiFabbrica;
	}

	
	
}
