package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CFR_TAR02 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_TAR02")
public class DiaDettaglioDCfrTar02 extends SuperDia  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CFR_TAR2_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_TAR2")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CFR_TAR2_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
	
	@Column(name="ALTRI_RES_CIV")
	private BigDecimal altriResCiv;

	@Column(name="ALTRI_RES_TARSU")
	private BigDecimal altriResTarsu;

	private String categoria;

	private String cf;

	private String classe;

	private BigDecimal consistenza;

    @Temporal( TemporalType.DATE)
	@Column(name="D_NASCITA")
	private Date dNascita;

	private String denom;

	private String denominazione;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INI_TIT")
	private Date dtIniTit;

	@Column(name="DT_INI_UIU")
	private String dtIniUiu;

	@Column(name="F_ENEL")
	private BigDecimal fEnel;

	@Column(name="F_ENEL_CIV")
	private BigDecimal fEnelCiv;

	private String foglio;

	private BigDecimal locatari;

	private BigDecimal locatore;

	private String particella;

	@Column(name="PERC_POSS")
	private BigDecimal percPoss;

	private String piano;

	@Column(name="PROP_PART")
	private BigDecimal propPart;

	@Column(name="PROP_UIU")
	private BigDecimal propUiu;

	private BigDecimal rendita;

	private BigDecimal res;

	@Column(name="RES_CIV")
	private BigDecimal resCiv;

	private String sub;

	@Column(name="SUP_CAT")
	private BigDecimal supCat;

	@Column(name="SUP_TARSU_CAT")
	private BigDecimal supTarsuCat;

	private BigDecimal tarsu;

	@Column(name="TARSU_FAM")
	private BigDecimal tarsuFam;

	@Column(name="TARSU_TIT")
	private BigDecimal tarsuTit;

	@Column(name="TP_TIT")
	private String tpTit;

	@Column(name="UIU_CIV")
	private BigDecimal uiuCiv;

    public DiaDettaglioDCfrTar02() {
    }

	public BigDecimal getAltriResCiv() {
		return this.altriResCiv;
	}

	public void setAltriResCiv(BigDecimal altriResCiv) {
		this.altriResCiv = altriResCiv;
	}

	public BigDecimal getAltriResTarsu() {
		return this.altriResTarsu;
	}

	public void setAltriResTarsu(BigDecimal altriResTarsu) {
		this.altriResTarsu = altriResTarsu;
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCf() {
		return this.cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getClasse() {
		return this.classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public BigDecimal getConsistenza() {
		return this.consistenza;
	}

	public void setConsistenza(BigDecimal consistenza) {
		this.consistenza = consistenza;
	}

	public Date getDNascita() {
		return this.dNascita;
	}

	public void setDNascita(Date dNascita) {
		this.dNascita = dNascita;
	}

	public String getDenom() {
		return this.denom;
	}

	public void setDenom(String denom) {
		this.denom = denom;
	}

	public String getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public Date getDtIniTit() {
		return this.dtIniTit;
	}

	public void setDtIniTit(Date dtIniTit) {
		this.dtIniTit = dtIniTit;
	}

	public String getDtIniUiu() {
		return this.dtIniUiu;
	}

	public void setDtIniUiu(String dtIniUiu) {
		this.dtIniUiu = dtIniUiu;
	}

	public BigDecimal getFEnel() {
		return this.fEnel;
	}

	public void setFEnel(BigDecimal fEnel) {
		this.fEnel = fEnel;
	}

	public BigDecimal getFEnelCiv() {
		return this.fEnelCiv;
	}

	public void setFEnelCiv(BigDecimal fEnelCiv) {
		this.fEnelCiv = fEnelCiv;
	}

	public String getFoglio() {
		return this.foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLocatari() {
		return this.locatari;
	}

	public void setLocatari(BigDecimal locatari) {
		this.locatari = locatari;
	}

	public BigDecimal getLocatore() {
		return this.locatore;
	}

	public void setLocatore(BigDecimal locatore) {
		this.locatore = locatore;
	}

	public String getParticella() {
		return this.particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public BigDecimal getPercPoss() {
		return this.percPoss;
	}

	public void setPercPoss(BigDecimal percPoss) {
		this.percPoss = percPoss;
	}

	public String getPiano() {
		return this.piano;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public BigDecimal getPropPart() {
		return this.propPart;
	}

	public void setPropPart(BigDecimal propPart) {
		this.propPart = propPart;
	}

	public BigDecimal getPropUiu() {
		return this.propUiu;
	}

	public void setPropUiu(BigDecimal propUiu) {
		this.propUiu = propUiu;
	}

	public BigDecimal getRendita() {
		return this.rendita;
	}

	public void setRendita(BigDecimal rendita) {
		this.rendita = rendita;
	}

	public BigDecimal getRes() {
		return this.res;
	}

	public void setRes(BigDecimal res) {
		this.res = res;
	}

	public BigDecimal getResCiv() {
		return this.resCiv;
	}

	public void setResCiv(BigDecimal resCiv) {
		this.resCiv = resCiv;
	}

	public String getSub() {
		return this.sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public BigDecimal getSupCat() {
		return this.supCat;
	}

	public void setSupCat(BigDecimal supCat) {
		this.supCat = supCat;
	}

	public BigDecimal getSupTarsuCat() {
		return this.supTarsuCat;
	}

	public void setSupTarsuCat(BigDecimal supTarsuCat) {
		this.supTarsuCat = supTarsuCat;
	}

	public BigDecimal getTarsu() {
		return this.tarsu;
	}

	public void setTarsu(BigDecimal tarsu) {
		this.tarsu = tarsu;
	}

	public BigDecimal getTarsuFam() {
		return this.tarsuFam;
	}

	public void setTarsuFam(BigDecimal tarsuFam) {
		this.tarsuFam = tarsuFam;
	}

	public BigDecimal getTarsuTit() {
		return this.tarsuTit;
	}

	public void setTarsuTit(BigDecimal tarsuTit) {
		this.tarsuTit = tarsuTit;
	}

	public String getTpTit() {
		return this.tpTit;
	}

	public void setTpTit(String tpTit) {
		this.tpTit = tpTit;
	}

	public BigDecimal getUiuCiv() {
		return this.uiuCiv;
	}

	public void setUiuCiv(BigDecimal uiuCiv) {
		this.uiuCiv = uiuCiv;
	}
	
	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}
}