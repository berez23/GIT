package it.webred.ct.data.access.basic.cosap.dto;

import java.io.Serializable;
import java.sql.Date;

import it.webred.ct.support.datarouter.CeTBaseObject;

public class RicercaOggettoCosapDTO extends CeTBaseObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String foglio;
	private String particella;
	
	private String sub;
	private String codiceVia;
	private String civico;
	
	private String  numeroDocumento ;
	private String annoDocumento ;
	private String codiceImmobile ;
	private String  tipoOccupazione ;
	private Date dtIniValidita ; 
    private Date dtFinValidita ;
    
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
	public String getCodiceVia() {
		return codiceVia;
	}
	public void setCodiceVia(String codiceVia) {
		this.codiceVia = codiceVia;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	

}
