package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_ICI01 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_ICI02")
public class DiaDettaglioDCtrIci02 extends SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_ICI02_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_ICI")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_ICI02_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
              
	private Long foglio;
	private String particella;
	private Long unimm;
	private String categoria;
	private String classe;
	private Double rendita;
	
	  
    public DiaDettaglioDCtrIci02() {
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


	public Long getUnimm() {
		return unimm;
	}


	public void setUnimm(Long unimm) {
		this.unimm = unimm;
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


	public Double getRendita() {
		return rendita;
	}


	public void setRendita(Double rendita) {
		this.rendita = rendita;
	}

	
}