package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;

public class RimbSanzSearchCriteria implements Serializable{
	
	private String rimbsanz = "R";
	private Long idStruttura;
	private String idClasse;
	private Long idPeriodo;
	private Long idSocieta;
	private String descrizione;
	
	private int start;
	private int rowNumber;
	
	public String getRimbsanz() {
		return rimbsanz;
	}
	public void setRimbsanz(String rimbsanz) {
		this.rimbsanz = rimbsanz;
	}
	public Long getIdStruttura() {
		return idStruttura;
	}
	public void setIdStruttura(Long idStruttura) {
		this.idStruttura = idStruttura;
	}
	public String getIdClasse() {
		return idClasse;
	}
	public void setIdClasse(String idClasse) {
		this.idClasse = idClasse;
	}
	public Long getIdPeriodo() {
		return idPeriodo;
	}
	public void setIdPeriodo(Long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Long getIdSocieta() {
		return idSocieta;
	}
	public void setIdSocieta(Long idSocieta) {
		this.idSocieta = idSocieta;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	
}
