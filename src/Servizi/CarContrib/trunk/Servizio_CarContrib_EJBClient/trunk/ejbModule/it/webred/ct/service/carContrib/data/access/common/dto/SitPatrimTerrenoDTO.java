package it.webred.ct.service.carContrib.data.access.common.dto;

import it.webred.ct.data.access.basic.catasto.dto.TerrenoPerSoggDTO;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SitPatrimTerrenoDTO extends CeTBaseObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TerrenoPerSoggDTO datiTitTerreno;
	private Boolean flagCessato;
	private Boolean flagCostituito;
	private Boolean flagLocato;
	private Boolean flagEdificabile;
	
	private String redditoDominicaleF;
	private String redditoAgrarioF;
	private String superficieF;
	private String sDtFinePoss;
	
	public String getsDtFinePoss(){
		Date dtFine = datiTitTerreno.getDtFinPos();
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		sDtFinePoss = SDF.format(dtFine);
		if("31/12/9999".equals(sDtFinePoss))
			sDtFinePoss = "ATTUALE";
		return sDtFinePoss;
	}
	
	public SitPatrimTerrenoDTO (){}
	
	public SitPatrimTerrenoDTO (TerrenoPerSoggDTO datiTitTerreno){
		this.datiTitTerreno= datiTitTerreno;
	}
	public TerrenoPerSoggDTO getDatiTitTerreno() {
		return datiTitTerreno;
	}
	public void setDatiTitTerreno(TerrenoPerSoggDTO datiTitTerreno) {
		this.datiTitTerreno = datiTitTerreno;
	}
	public Boolean getFlagCessato() {
		return flagCessato;
	}
	public void setFlagCessato(Boolean flagCessato) {
		this.flagCessato = flagCessato;
	}
	public Boolean getFlagCostituito() {
		return flagCostituito;
	}
	public void setFlagCostituito(Boolean flagCostituito) {
		this.flagCostituito = flagCostituito;
	}
	public Boolean getFlagLocato() {
		return flagLocato;
	}
	public void setFlagLocato(Boolean flagLocato) {
		this.flagLocato = flagLocato;
	}
	public Boolean getFlagEdificabile() {
		return flagEdificabile;
	}
	public void setFlagEdificabile(Boolean flagEdificabile) {
		this.flagEdificabile = flagEdificabile;
	}

	public String getRedditoDominicaleF() {
		return redditoDominicaleF;
	}

	public void setRedditoDomenicaleF(String redditoDominicaleF) {
		this.redditoDominicaleF = redditoDominicaleF;
	}

	public String getRedditoAgrarioF() {
		return redditoAgrarioF;
	}

	public void setRedditoAgrarioF(String redditoAgrarioF) {
		this.redditoAgrarioF = redditoAgrarioF;
	}

	public void setSuperficieF(String superficieF) {
		this.superficieF = superficieF;
	}

	public String getSuperficieF() {
		return superficieF;
	}
	
	

	
}
