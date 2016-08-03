package it.webred.ct.service.spprof.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.Date;

public class InterventoSearchCriteria extends CeTBaseObject implements Serializable {
	
	private String stato;
	private String fkSoggetto;
	private Date dataDa;
	private Date dataA;
	private String concNumero;
	private String protNumero;
	private Date protDataDa;
	private Date protDataA;
	
	private int start;
	private int record;
	
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getFkSoggetto() {
		return fkSoggetto;
	}
	public void setFkSoggetto(String fkSoggetto) {
		this.fkSoggetto = fkSoggetto;
	}
	public String getConcNumero() {
		return concNumero;
	}
	public void setConcNumero(String concNumero) {
		this.concNumero = concNumero;
	}
	public String getProtNumero() {
		return protNumero;
	}
	public void setProtNumero(String protNumero) {
		this.protNumero = protNumero;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getRecord() {
		return record;
	}
	public void setRecord(int record) {
		this.record = record;
	}
	public Date getDataDa() {
		return dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public Date getDataA() {
		return dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Date getProtDataDa() {
		return protDataDa;
	}
	public void setProtDataDa(Date protDataDa) {
		this.protDataDa = protDataDa;
	}
	public Date getProtDataA() {
		return protDataA;
	}
	public void setProtDataA(Date protDataA) {
		this.protDataA = protDataA;
	}
}
