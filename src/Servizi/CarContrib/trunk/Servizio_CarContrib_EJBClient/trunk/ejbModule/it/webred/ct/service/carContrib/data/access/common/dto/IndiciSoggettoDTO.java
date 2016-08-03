package it.webred.ct.service.carContrib.data.access.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class IndiciSoggettoDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private SoggettoDTO sogg;
	private List<String> listaIdSoggAnagGen;
	private List<BigDecimal> listaIdSoggAnagCat;
	public List<BigDecimal> getListaIdSoggAnagCat() {
		return listaIdSoggAnagCat;
	}
	public void setListaIdSoggAnagCat(List<BigDecimal> listaIdSoggAnagCat) {
		this.listaIdSoggAnagCat = listaIdSoggAnagCat;
	}
	public IndiciSoggettoDTO() {
		super();
	}
	public IndiciSoggettoDTO(SoggettoDTO sogg) {
		this.sogg=sogg;
	}
	public SoggettoDTO getSogg() {
		return sogg;
	}
	public void setSogg(SoggettoDTO sogg) {
		this.sogg = sogg;
	}
	public List<String> getListaIdSoggAnagGen() {
		return listaIdSoggAnagGen;
	}
	public void setListaIdSoggAnagGen(List<String> listaIdSoggAnagGen) {
		this.listaIdSoggAnagGen = listaIdSoggAnagGen;
	}
	
}
