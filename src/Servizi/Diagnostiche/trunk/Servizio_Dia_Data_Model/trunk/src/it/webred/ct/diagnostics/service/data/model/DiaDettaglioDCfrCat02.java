package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CFR_CAT01 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_CAT02")
public class DiaDettaglioDCfrCat02 extends SuperDia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CFR_CAT01_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_CAT01")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CFR_CAT01_ID_GENERATOR")
	private Long id;
	
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
    
    @Column(name="COD_NAZIONALE")   
    private String codNazionale;
    
    @Column(name="FOGLIO")    
    private Long foglio;
    
    @Column(name="PARTICELLA")
    private String particella;
    
    

	public DiaDettaglioDCfrCat02() {
		super();
		// TODO Auto-generated constructor stub
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

	
}
