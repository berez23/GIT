package it.webred.ct.service.tares.data.model;

import javax.persistence.*;


import java.math.BigDecimal;


/**
 * The persistent class for the SETAR_ELAB database table.
 * 
 */
@Entity
@Table(name="SETAR_ELAB")
@SequenceGenerator(name="seq_setar", initialValue=1, allocationSize=1)
public class SetarElab extends BaseItem {

	private static final long serialVersionUID = -6936667899911620812L;
	
	@EmbeddedId
	private SetarElabPK id = null;

	private String categoria;

	private String classe;

	@Column(name="COD_NAZIONALE")
	private String codNazionale;

	@Column(name="CODI_FISC_LUNA")
	private String codiFiscLuna;

	private BigDecimal consistenza;

	@Column(name="DELTA_DICH_80")
	private BigDecimal deltaDich80;

	@Column(name="DELTA_SUP_CAT_CALC")
	private BigDecimal deltaSupCatCalc;

	@Column(name="DELTA_SUP_CAT_TARSU_CALC")
	private BigDecimal deltaSupCatTarsuCalc;

	@Column(name="ID_SEZC")
	private String idSezc;

	private BigDecimal rendita;

	@Column(name="STATO_CATASTO")
	private BigDecimal statoCatasto;

	private String sub;

	@Column(name="SUP_A")
	private BigDecimal supA;

	@Column(name="SUP_B")
	private BigDecimal supB;

	@Column(name="SUP_C")
	private BigDecimal supC;

	@Column(name="SUP_CAT")
	private BigDecimal supCat;

	@Column(name="SUP_CAT_DPR138_98")
	private BigDecimal supCatDpr13898;

	@Column(name="SUP_CAT_TARSU")
	private BigDecimal supCatTarsu;

	@Column(name="SUP_CAT_TARSU_80")
	private BigDecimal supCatTarsu80;

	@Column(name="SUP_CAT_TARSU_CALC")
	private BigDecimal supCatTarsuCalc;

	@Column(name="SUP_D")
	private BigDecimal supD;

	@Column(name="SUP_E")
	private BigDecimal supE;

	@Column(name="SUP_F")
	private BigDecimal supF;

	@Column(name="SUP_G")
	private BigDecimal supG;

	@Column(name="SUP_H")
	private BigDecimal supH;

	@Column(name="SUP_TARSU")
	private BigDecimal supTarsu;

	@Column(name="VANO")
	private BigDecimal vano;
	
	@Transient
	private Boolean segnalata = null;
	
	@Transient
	private Boolean difforme = null;
	
	@Transient
	private Boolean esportata = null;
	
	@Transient
	private String segnalazioniIds = null;
	
	public SetarElab() {
	}//-------------------------------------------------------------------------

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getClasse() {
		return this.classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getCodNazionale() {
		return this.codNazionale;
	}

	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}

	public String getCodiFiscLuna() {
		return this.codiFiscLuna;
	}

	public void setCodiFiscLuna(String codiFiscLuna) {
		this.codiFiscLuna = codiFiscLuna;
	}

	public BigDecimal getConsistenza() {
		return this.consistenza;
	}

	public void setConsistenza(BigDecimal consistenza) {
		this.consistenza = consistenza;
	}

	public BigDecimal getDeltaDich80() {
		return this.deltaDich80;
	}

	public void setDeltaDich80(BigDecimal deltaDich80) {
		this.deltaDich80 = deltaDich80;
	}

	public BigDecimal getDeltaSupCatCalc() {
		return this.deltaSupCatCalc;
	}

	public void setDeltaSupCatCalc(BigDecimal deltaSupCatCalc) {
		this.deltaSupCatCalc = deltaSupCatCalc;
	}

	public BigDecimal getDeltaSupCatTarsuCalc() {
		return this.deltaSupCatTarsuCalc;
	}

	public void setDeltaSupCatTarsuCalc(BigDecimal deltaSupCatTarsuCalc) {
		this.deltaSupCatTarsuCalc = deltaSupCatTarsuCalc;
	}

	public String getIdSezc() {
		return this.idSezc;
	}

	public void setIdSezc(String idSezc) {
		this.idSezc = idSezc;
	}

	public BigDecimal getRendita() {
		return this.rendita;
	}

	public void setRendita(BigDecimal rendita) {
		this.rendita = rendita;
	}

	public BigDecimal getStatoCatasto() {
		return this.statoCatasto;
	}

	public void setStatoCatasto(BigDecimal statoCatasto) {
		this.statoCatasto = statoCatasto;
	}

	public String getSub() {
		return this.sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public BigDecimal getSupA() {
		return this.supA;
	}

	public void setSupA(BigDecimal supA) {
		this.supA = supA;
	}

	public BigDecimal getSupB() {
		return this.supB;
	}

	public void setSupB(BigDecimal supB) {
		this.supB = supB;
	}

	public BigDecimal getSupC() {
		return this.supC;
	}

	public void setSupC(BigDecimal supC) {
		this.supC = supC;
	}

	public BigDecimal getSupCat() {
		return this.supCat;
	}

	public void setSupCat(BigDecimal supCat) {
		this.supCat = supCat;
	}

	public BigDecimal getSupCatDpr13898() {
		return this.supCatDpr13898;
	}

	public void setSupCatDpr13898(BigDecimal supCatDpr13898) {
		this.supCatDpr13898 = supCatDpr13898;
	}

	public BigDecimal getSupCatTarsu() {
		return this.supCatTarsu;
	}

	public void setSupCatTarsu(BigDecimal supCatTarsu) {
		this.supCatTarsu = supCatTarsu;
	}

	public BigDecimal getSupCatTarsu80() {
		return this.supCatTarsu80;
	}

	public void setSupCatTarsu80(BigDecimal supCatTarsu80) {
		this.supCatTarsu80 = supCatTarsu80;
	}

	public BigDecimal getSupCatTarsuCalc() {
		return this.supCatTarsuCalc;
	}

	public void setSupCatTarsuCalc(BigDecimal supCatTarsuCalc) {
		this.supCatTarsuCalc = supCatTarsuCalc;
	}

	public BigDecimal getSupD() {
		return this.supD;
	}

	public void setSupD(BigDecimal supD) {
		this.supD = supD;
	}

	public BigDecimal getSupE() {
		return this.supE;
	}

	public void setSupE(BigDecimal supE) {
		this.supE = supE;
	}

	public BigDecimal getSupF() {
		return this.supF;
	}

	public void setSupF(BigDecimal supF) {
		this.supF = supF;
	}

	public BigDecimal getSupG() {
		return this.supG;
	}

	public void setSupG(BigDecimal supG) {
		this.supG = supG;
	}

	public BigDecimal getSupH() {
		return this.supH;
	}

	public void setSupH(BigDecimal supH) {
		this.supH = supH;
	}

	public BigDecimal getSupTarsu() {
		return this.supTarsu;
	}

	public void setSupTarsu(BigDecimal supTarsu) {
		this.supTarsu = supTarsu;
	}

	public BigDecimal getVano() {
		return this.vano;
	}

	public void setVano(BigDecimal vano) {
		this.vano = vano;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(SetarElabPK id) {
		this.id = id;
	}

	public SetarElabPK getId() {
		return id;
	}

	public Boolean getSegnalata() {
		return segnalata;
	}

	public void setSegnalata(Boolean segnalata) {
		this.segnalata = segnalata;
	}

	public Boolean getEsportata() {
		return esportata;
	}

	public void setEsportata(Boolean esportata) {
		this.esportata = esportata;
	}

	public String getSegnalazioniIds() {
		return segnalazioniIds;
	}

	public void setSegnalazioniIds(String segnalazioniIds) {
		this.segnalazioniIds = segnalazioniIds;
	}

	public Boolean getDifforme() {
		return difforme;
	}

	public void setDifforme(Boolean difforme) {
		this.difforme = difforme;
	}




}