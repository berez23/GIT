package it.webred.ct.service.comma336.data.access.dto;

import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.support.datarouter.CeTBaseObject;

public class C336SearchResultDTO extends CeTBaseObject {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private ParametriCatastaliDTO uiu;
	
	private Boolean flgPresCatasto;
	
	private Boolean flgPresDocfa;
	
	private Boolean flgPresConcEdilizie;
	
	private String statoPratica;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public ParametriCatastaliDTO getUiu() {
		return uiu;
	}
	public void setUiu(ParametriCatastaliDTO uiu) {
		this.uiu = uiu;
	}
	public Boolean getFlgPresCatasto() {
		return flgPresCatasto;
	}
	public void setFlgPresCatasto(Boolean flgPresCatasto) {
		this.flgPresCatasto = flgPresCatasto;
	}
	public Boolean getFlgPresDocfa() {
		return flgPresDocfa;
	}
	public void setFlgPresDocfa(Boolean flgPresDocfa) {
		this.flgPresDocfa = flgPresDocfa;
	}
	public Boolean getFlgPresConcEdilizie() {
		return flgPresConcEdilizie;
	}
	public void setFlgPresConcEdilizie(Boolean flgPresConcEdilizie) {
		this.flgPresConcEdilizie = flgPresConcEdilizie;
	}

	public void setStatoPratica(String statoPratica) {
		this.statoPratica = statoPratica;
	}

	public String getStatoPratica() {
		return statoPratica;
	}

	
	
	

	
}
