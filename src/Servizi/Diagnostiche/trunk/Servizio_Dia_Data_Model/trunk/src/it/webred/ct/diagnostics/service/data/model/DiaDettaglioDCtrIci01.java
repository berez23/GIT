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
@Table(name="DIA_DETTAGLIO_D_CTR_ICI01")
public class DiaDettaglioDCtrIci01 extends SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_ICI01_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_ICI")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_ICI01_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
        
	private Long ninc;        
	private String foglio;
	private String particella;
	private String subalterno;
	private String categoria;
	private String classe;
	private Double rendita;
	private String codFisc;
	private String partitaiva;
	private String indirizzo;
	  
    public DiaDettaglioDCtrIci01() {
    }

	public Long getNinc() {
		return ninc;
	}

	public void setNinc(Long ninc) {
		this.ninc = ninc;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
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

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public String getPartitaiva() {
		return partitaiva;
	}

	public void setPartitaiva(String partitaiva) {
		this.partitaiva = partitaiva;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public DiaTestata getDiaTestata() {
		return this.diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}
	
}