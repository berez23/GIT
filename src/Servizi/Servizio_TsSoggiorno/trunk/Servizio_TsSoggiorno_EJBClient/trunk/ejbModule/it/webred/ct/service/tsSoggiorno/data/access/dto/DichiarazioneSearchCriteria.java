package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;

public class DichiarazioneSearchCriteria implements Serializable{
	
	private Long idStruttura;
	private String idClasse;
	private Long idSocieta;
	private Long idPeriodo;
	private Long idPeriodoDa;
	private Long idPeriodoA;
	private String codFiscale;
	private String dataIns;
	
	private int start;
	private int rowNumber;
	
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
	public Long getIdSocieta() {
		return idSocieta;
	}
	public void setIdSocieta(Long idSocieta) {
		this.idSocieta = idSocieta;
	}
	public Long getIdPeriodo() {
		return idPeriodo;
	}
	public void setIdPeriodo(Long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}
	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
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
	public String getDataIns() {
		return dataIns;
	}
	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}
	public Long getIdPeriodoDa() {
		return idPeriodoDa;
	}
	public void setIdPeriodoDa(Long idPeriodoDa) {
		this.idPeriodoDa = idPeriodoDa;
	}
	public Long getIdPeriodoA() {
		return idPeriodoA;
	}
	public void setIdPeriodoA(Long idPeriodoA) {
		this.idPeriodoA = idPeriodoA;
	}
	
}
