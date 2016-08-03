package it.webred.ct.service.geospatial.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ParticellaDTO extends CeTBaseObject implements Serializable {
	
	private Long pkidCivi;
	private String codNazionale; //ente
	private String foglio;
	private String particella;
	private String sub;
	private Date dataFineVal;
	private int startRecord;
	private int numRecord;
	
	private List<?> lista;
	
	
	
	public Long getPkidCivi() {
		return pkidCivi;
	}
	public void setPkidCivi(Long pkidCivi) {
		this.pkidCivi = pkidCivi;
	}
	public String getCodNazionale() {
		return codNazionale;
	}
	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
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
	public Date getDataFineVal() {
		return dataFineVal;
	}
	public void setDataFineVal(Date dataFineVal) {
		this.dataFineVal = dataFineVal;
	}
	public int getStartRecord() {
		return startRecord;
	}
	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}
	public int getNumRecord() {
		return numRecord;
	}
	public void setNumRecord(int numRecord) {
		this.numRecord = numRecord;
	}
	public List<?> getLista() {
		return lista;
	}
	public void setLista(List<?> lista) {
		this.lista = lista;
	}
	
}
