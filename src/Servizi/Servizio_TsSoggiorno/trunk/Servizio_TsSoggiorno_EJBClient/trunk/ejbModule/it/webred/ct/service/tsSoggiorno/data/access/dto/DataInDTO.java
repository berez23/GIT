package it.webred.ct.service.tsSoggiorno.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.Date;

public class DataInDTO extends CeTBaseObject implements Serializable{

	private Long id;
	private String id2;
	private Long id3;
	private String codFiscale;
	private Object obj;
	private boolean bool;
	private Integer maxNumber;
	private Long idPeriodoDa;
	private Long idPeriodoA;

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public Long getId3() {
		return id3;
	}

	public void setId3(Long id3) {
		this.id3 = id3;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public Integer getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
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
