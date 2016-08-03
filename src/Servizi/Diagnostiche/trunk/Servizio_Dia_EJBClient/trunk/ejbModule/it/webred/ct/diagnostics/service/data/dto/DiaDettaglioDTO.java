package it.webred.ct.diagnostics.service.data.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class DiaDettaglioDTO extends CeTBaseObject implements Serializable {
	
	
	private Long idDiaTestata;
	private String modelClassname;
	
	private int start;
	private int maxrows;
	
	public DiaDettaglioDTO(String enteId, String userId) {
		super();
		setEnteId(enteId);
		setUserId(userId);
	}
	
	
	public Long getIdDiaTestata() {
		return idDiaTestata;
	}
	public void setIdDiaTestata(Long idDiaTestata) {
		this.idDiaTestata = idDiaTestata;
	}
	public String getModelClassname() {
		return modelClassname;
	}
	public void setModelClassname(String modelClassname) {
		this.modelClassname = modelClassname;
	}


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getMaxrows() {
		return maxrows;
	}


	public void setMaxrows(int maxrows) {
		this.maxrows = maxrows;
	}
	
	
}
