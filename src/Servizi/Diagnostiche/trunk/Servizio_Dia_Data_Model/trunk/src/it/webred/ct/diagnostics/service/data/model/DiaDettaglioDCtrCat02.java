package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_CAT02 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_CAT02")
public class DiaDettaglioDCtrCat02 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_CAT_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_CAT")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_CAT_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;

    @Column(name="COD_NAZIONALE")
    private String codNazionale;

    private Long foglio;
    
	private String particella;
	
	private Long sub;
	
	private String categoria;

	private String classe;

	private Double consistenza;
	
	@Column(name="SUP_CAT")
	private Double supCat;
	
	private String zona;
	
	private String microzona;
	
	private Double rendita;
	
	private String titolari;
	
	@Column(name="TITOLARI_LOCATORI")
	private String titolariLocatori;

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

	public String getCodNazionale() {
		return codNazionale;
	}

	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}

	public Long getFoglio() {
		return foglio;
	}

	public void setFoglio(Long foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public Long getSub() {
		return sub;
	}

	public void setSub(Long sub) {
		this.sub = sub;
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

	public Double getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(Double consistenza) {
		this.consistenza = consistenza;
	}

	public Double getSupCat() {
		return supCat;
	}

	public void setSupCat(Double supCat) {
		this.supCat = supCat;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getMicrozona() {
		return microzona;
	}

	public void setMicrozona(String microzona) {
		this.microzona = microzona;
	}

	public Double getRendita() {
		return rendita;
	}

	public void setRendita(Double rendita) {
		this.rendita = rendita;
	}

	public String getTitolari() {
		return titolari;
	}

	public void setTitolari(String titolari) {
		this.titolari = titolari;
	}

	

    

}