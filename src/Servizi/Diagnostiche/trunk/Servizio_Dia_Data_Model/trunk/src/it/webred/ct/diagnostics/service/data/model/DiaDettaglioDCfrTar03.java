package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CFR_TAR03 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_TAR03")
public class DiaDettaglioDCfrTar03 extends SuperDia implements Serializable {
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

	@Column(name="DES_CLS_TARSU")
	private String desClsTarsu;

	private BigDecimal consistenza;

    @Temporal(TemporalType.DATE)
	@Column(name="D_NASCITA")
	private Date dNascita;

	private String denom;

	private String denominazione;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INI_OGG")
	private Date dtIniOgg;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INI_SITUAZ")
	private Date dtIniSituaz;

    @Temporal(TemporalType.DATE)
	@Column(name="DT_INI_TIT")
	private Date dtIniTit;

	@Column(name="DT_INI_UIU")
	private String dtIniUiu;

	@Column(name="F_ENEL")
	private BigDecimal fEnel;

	@Column(name="F_ENEL_CIV")
	private BigDecimal fEnelCiv;

	private String foglio;

	@Column(name="FOGLIO_TARSU")
	private String foglioTarsu;

	private BigDecimal locatari;

	@Column(name="LOCATARI_TARSU")
	private BigDecimal locatariTarsu;

	private BigDecimal locatore;
	
	@Column(name="TIP_OGG")
	private String tipOgg;
	
	@Column(name="DES_TIP_OGG")
	private String desTipOgg;

	@Column(name="PART_TARSU")
	private String partTarsu;

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

	@Column(name="SUB_TARSU")
	private String subTarsu;

	@Column(name="SUP_CAT")
	private BigDecimal supCat;

	@Column(name="SUP_TARSU")
	private BigDecimal supTarsu;

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

    public DiaDettaglioDCfrTar03() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

	public BigDecimal getAltriResCiv() {
		return altriResCiv;
	}

	public void setAltriResCiv(BigDecimal altriResCiv) {
		this.altriResCiv = altriResCiv;
	}

	public BigDecimal getAltriResTarsu() {
		return altriResTarsu;
	}

	public void setAltriResTarsu(BigDecimal altriResTarsu) {
		this.altriResTarsu = altriResTarsu;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getDesClsTarsu() {
		return desClsTarsu;
	}

	public void setDesClsTarsu(String desClsTarsu) {
		this.desClsTarsu = desClsTarsu;
	}

	public BigDecimal getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(BigDecimal consistenza) {
		this.consistenza = consistenza;
	}

	public Date getdNascita() {
		return dNascita;
	}

	public void setdNascita(Date dNascita) {
		this.dNascita = dNascita;
	}

	public String getDenom() {
		return denom;
	}

	public void setDenom(String denom) {
		this.denom = denom;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public Date getDtIniOgg() {
		return dtIniOgg;
	}

	public void setDtIniOgg(Date dtIniOgg) {
		this.dtIniOgg = dtIniOgg;
	}

	public Date getDtIniSituaz() {
		return dtIniSituaz;
	}

	public void setDtIniSituaz(Date dtIniSituaz) {
		this.dtIniSituaz = dtIniSituaz;
	}

	public Date getDtIniTit() {
		return dtIniTit;
	}

	public void setDtIniTit(Date dtIniTit) {
		this.dtIniTit = dtIniTit;
	}

	public String getDtIniUiu() {
		return dtIniUiu;
	}

	public void setDtIniUiu(String dtIniUiu) {
		this.dtIniUiu = dtIniUiu;
	}

	public BigDecimal getfEnel() {
		return fEnel;
	}

	public void setfEnel(BigDecimal fEnel) {
		this.fEnel = fEnel;
	}

	public BigDecimal getfEnelCiv() {
		return fEnelCiv;
	}

	public void setfEnelCiv(BigDecimal fEnelCiv) {
		this.fEnelCiv = fEnelCiv;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getFoglioTarsu() {
		return foglioTarsu;
	}

	public void setFoglioTarsu(String foglioTarsu) {
		this.foglioTarsu = foglioTarsu;
	}

	public BigDecimal getLocatari() {
		return locatari;
	}

	public void setLocatari(BigDecimal locatari) {
		this.locatari = locatari;
	}

	public BigDecimal getLocatariTarsu() {
		return locatariTarsu;
	}

	public void setLocatariTarsu(BigDecimal locatariTarsu) {
		this.locatariTarsu = locatariTarsu;
	}

	public BigDecimal getLocatore() {
		return locatore;
	}

	public void setLocatore(BigDecimal locatore) {
		this.locatore = locatore;
	}

	public String getTipOgg() {
		return tipOgg;
	}

	public void setTipOgg(String tipOgg) {
		this.tipOgg = tipOgg;
	}

	public String getDesTipOgg() {
		return desTipOgg;
	}

	public void setDesTipOgg(String desTipOgg) {
		this.desTipOgg = desTipOgg;
	}

	public String getPartTarsu() {
		return partTarsu;
	}

	public void setPartTarsu(String partTarsu) {
		this.partTarsu = partTarsu;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public BigDecimal getPercPoss() {
		return percPoss;
	}

	public void setPercPoss(BigDecimal percPoss) {
		this.percPoss = percPoss;
	}

	public String getPiano() {
		return piano;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public BigDecimal getPropPart() {
		return propPart;
	}

	public void setPropPart(BigDecimal propPart) {
		this.propPart = propPart;
	}

	public BigDecimal getPropUiu() {
		return propUiu;
	}

	public void setPropUiu(BigDecimal propUiu) {
		this.propUiu = propUiu;
	}

	public BigDecimal getRendita() {
		return rendita;
	}

	public void setRendita(BigDecimal rendita) {
		this.rendita = rendita;
	}

	public BigDecimal getRes() {
		return res;
	}

	public void setRes(BigDecimal res) {
		this.res = res;
	}

	public BigDecimal getResCiv() {
		return resCiv;
	}

	public void setResCiv(BigDecimal resCiv) {
		this.resCiv = resCiv;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getSubTarsu() {
		return subTarsu;
	}

	public void setSubTarsu(String subTarsu) {
		this.subTarsu = subTarsu;
	}

	public BigDecimal getSupCat() {
		return supCat;
	}

	public void setSupCat(BigDecimal supCat) {
		this.supCat = supCat;
	}

	public BigDecimal getSupTarsu() {
		return supTarsu;
	}

	public void setSupTarsu(BigDecimal supTarsu) {
		this.supTarsu = supTarsu;
	}

	public BigDecimal getSupTarsuCat() {
		return supTarsuCat;
	}

	public void setSupTarsuCat(BigDecimal supTarsuCat) {
		this.supTarsuCat = supTarsuCat;
	}

	public BigDecimal getTarsu() {
		return tarsu;
	}

	public void setTarsu(BigDecimal tarsu) {
		this.tarsu = tarsu;
	}

	public BigDecimal getTarsuFam() {
		return tarsuFam;
	}

	public void setTarsuFam(BigDecimal tarsuFam) {
		this.tarsuFam = tarsuFam;
	}

	public BigDecimal getTarsuTit() {
		return tarsuTit;
	}

	public void setTarsuTit(BigDecimal tarsuTit) {
		this.tarsuTit = tarsuTit;
	}

	public String getTpTit() {
		return tpTit;
	}

	public void setTpTit(String tpTit) {
		this.tpTit = tpTit;
	}

	public BigDecimal getUiuCiv() {
		return uiuCiv;
	}

	public void setUiuCiv(BigDecimal uiuCiv) {
		this.uiuCiv = uiuCiv;
	}

	
}