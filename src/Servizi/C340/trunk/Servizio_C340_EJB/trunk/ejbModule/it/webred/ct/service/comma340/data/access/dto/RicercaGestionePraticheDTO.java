package it.webred.ct.service.comma340.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.math.BigDecimal;

public class RicercaGestionePraticheDTO extends CeTBaseObject {
	
	private static final long serialVersionUID = 1L;
	private String codComune;
	private String descrComune;
	private Long idPra;
	private BigDecimal idUiu;
	private Long idAll;
	
	public String getCodComune() {
		return codComune;
	}
	
	public void setCodComune(String codComune) {
		this.codComune = codComune;
	}	

	public String getDescrComune() {
		return descrComune;
	}

	public void setDescrComune(String descrComune) {
		this.descrComune = descrComune;
	}

	public Long getIdPra() {
		return idPra;
	}

	public void setIdPra(Long idPra) {
		this.idPra = idPra;
	}

	public BigDecimal getIdUiu() {
		return idUiu;
	}

	public void setIdUiu(BigDecimal idUiu) {
		this.idUiu = idUiu;
	}

	public Long getIdAll() {
		return idAll;
	}

	public void setIdAll(Long idAll) {
		this.idAll = idAll;
	}

}
