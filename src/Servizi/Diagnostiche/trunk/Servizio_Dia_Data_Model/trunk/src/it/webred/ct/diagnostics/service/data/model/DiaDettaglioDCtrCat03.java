package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_CAT03 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_CAT03")
public class DiaDettaglioDCtrCat03 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_CAT_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_CAT")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_CAT_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
    @Column(name="PKID_STRA_SITI")
    private Long pkidStraSiti;
    
    @Column(name="DESC_VIA")
    private String descVia;
    
    @Column(name="PKID_CIVI_SITI")
    private Long pkidCiviSiti;
    
    private String civico;
    
    @Column(name="PRES_UI_ASSOCIATE_CIV_MULTIPLI")
    private String presUiAssociateCivMultipli;
    
    @Column(name="NUM_UI_RESIDENZIALI")
    private Long numUiResidenziali;  
    
    @Column(name="NUM_FAM_RESIDENTI")
    private Long numFamResidenti;  
    
    @Column(name="NUM_FAM_TIT_RES")
    private Long numFamTitRes;
    
    @Column(name="NUM_CONTR_LOCAZ")
    private Long numContrLocaz;  
    
    @Column(name="NUM_CONTR_ENEL")
    private Long numContrEnel;  
    
    @Column(name="NUM_CONTR_GAS")
    private Long numContrGas;  
    
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

	public Long getPkidStraSiti() {
		return pkidStraSiti;
	}

	public void setPkidStraSiti(Long pkidStraSiti) {
		this.pkidStraSiti = pkidStraSiti;
	}

	public String getDescVia() {
		return descVia;
	}

	public void setDescVia(String descVia) {
		this.descVia = descVia;
	}

	public Long getPkidCiviSiti() {
		return pkidCiviSiti;
	}

	public void setPkidCiviSiti(Long pkidCiviSiti) {
		this.pkidCiviSiti = pkidCiviSiti;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getPresUiAssociateCivMultipli() {
		return presUiAssociateCivMultipli;
	}

	public void setPresUiAssociateCivMultipli(String presUiAssociateCivMultipli) {
		this.presUiAssociateCivMultipli = presUiAssociateCivMultipli;
	}

	public Long getNumUiResidenziali() {
		return numUiResidenziali;
	}

	public void setNumUiResidenziali(Long numUiResidenziali) {
		this.numUiResidenziali = numUiResidenziali;
	}

	public Long getNumFamResidenti() {
		return numFamResidenti;
	}

	public void setNumFamResidenti(Long numFamResidenti) {
		this.numFamResidenti = numFamResidenti;
	}

	public Long getNumFamTitRes() {
		return numFamTitRes;
	}

	public void setNumFamTitRes(Long numFamTitRes) {
		this.numFamTitRes = numFamTitRes;
	}

	public Long getNumContrLocaz() {
		return numContrLocaz;
	}

	public void setNumContrLocaz(Long numContrLocaz) {
		this.numContrLocaz = numContrLocaz;
	}

	public Long getNumContrEnel() {
		return numContrEnel;
	}

	public void setNumContrEnel(Long numContrEnel) {
		this.numContrEnel = numContrEnel;
	}

	public Long getNumContrGas() {
		return numContrGas;
	}

	public void setNumContrGas(Long numContrGas) {
		this.numContrGas = numContrGas;
	}
 

}