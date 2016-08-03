package it.webred.ct.service.carContrib.data.access.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.webred.ct.data.model.catasto.SiticonduzImmAll;
import it.webred.ct.support.datarouter.CeTBaseObject;

public class SitPatrimImmobileDTO extends CeTBaseObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SiticonduzImmAll datiTitImmobile;
	private String sezione;
	private String categoria;
	private String classe;
	private BigDecimal rendita;
	private String renditaF;
	private BigDecimal superficieCat;
	private String superficieCatF;
	private BigDecimal superficieTarsu;
	private String superficieTarsuF;
	private Boolean flagCessato;
	private Boolean flagCostituito;
	private Boolean flagLocato;
	private List<String> listaDichiarantiTarsu;
	private String percPossF;
	private String descTitolo;
	private String sDtFinePoss;
	
	
	public String getsDtFinePoss(){
		Date dtFine = datiTitImmobile.getId().getDataFine();
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		sDtFinePoss = SDF.format(dtFine);
		if("31/12/9999".equals(sDtFinePoss))
			sDtFinePoss = "ATTUALE";
		return sDtFinePoss;
	}
	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public BigDecimal getRendita() {
		return rendita;
	}
	public void setRendita(BigDecimal rendita) {
		this.rendita = rendita;
	}
	public void setRenditaF(String renditaF) {
		this.renditaF = renditaF;
	}
	public String getRenditaF() {
		return renditaF;
	}
	public BigDecimal getSuperficieCat() {
		return superficieCat;
	}
	public void setSuperficieCat(BigDecimal superficieCat) {
		this.superficieCat = superficieCat;
	}
	public void setSuperficieCatF(String superficieCatF) {
		this.superficieCatF = superficieCatF;
	}
	public String getSuperficieCatF() {
		return superficieCatF;
	}
	public BigDecimal getSuperficieTarsu() {
		return superficieTarsu;
	}
	public void setSuperficieTarsu(BigDecimal superficieTarsu) {
		this.superficieTarsu = superficieTarsu;
	}
	public void setSuperficieTarsuF(String superficieTarsuF) {
		this.superficieTarsuF = superficieTarsuF;
	}
	public String getSuperficieTarsuF() {
		return superficieTarsuF;
	}
	public List<String> getListaDichiarantiTarsu() {
		return listaDichiarantiTarsu;
	}
	public void setListaDichiarantiTarsu(List<String> listaDichiarantiTarsu) {
		this.listaDichiarantiTarsu = listaDichiarantiTarsu;
	}
	public void setPercPossF(String percPossF) {
		this.percPossF = percPossF;
	}
	public String getPercPossF() {
		return percPossF;
	}
	public  SitPatrimImmobileDTO() {}
	public  SitPatrimImmobileDTO(SiticonduzImmAll datiTitImmobile) {
		this.datiTitImmobile=datiTitImmobile;
	}
	public SiticonduzImmAll getDatiTitImmobile() {
		return datiTitImmobile;
	}
	public void setDatiTitImmobile(SiticonduzImmAll datiTitImmobile) {
		this.datiTitImmobile = datiTitImmobile;
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
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getDescTitolo() {
		return descTitolo;
	}
	public void setDescTitolo(String descTitolo) {
		this.descTitolo = descTitolo;
	}

}

