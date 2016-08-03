package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CFR_ICI202 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_ICI202")
public class DiaDettaglioDCfrIci202 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CFR_ICI2_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_ICI2")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CFR_ICI2_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;

	private String categoria;

	private String classe;

	private BigDecimal foglio;

	private String particella;

	private BigDecimal rendita;

	private BigDecimal unimm;

    public DiaDettaglioDCfrIci202() {
    }

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

	
	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

	public BigDecimal getFoglio() {
		return this.foglio;
	}

	public void setFoglio(BigDecimal foglio) {
		this.foglio = foglio;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParticella() {
		return this.particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public BigDecimal getRendita() {
		return this.rendita;
	}

	public void setRendita(BigDecimal rendita) {
		this.rendita = rendita;
	}

	public BigDecimal getUnimm() {
		return this.unimm;
	}

	public void setUnimm(BigDecimal unimm) {
		this.unimm = unimm;
	}

}