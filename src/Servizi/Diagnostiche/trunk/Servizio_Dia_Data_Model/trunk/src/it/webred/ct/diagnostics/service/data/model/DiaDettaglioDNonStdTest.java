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
@Table(name="DIA_DETTAGLIO_D_NONSTD_TEST")
public class DiaDettaglioDNonStdTest extends SuperDia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="SEQ_DIA_DETTAGLIO_D_TEST_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_TEST")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_DIA_DETTAGLIO_D_TEST_ID_GENERATOR")
	private Long id;
	
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
    private Long foglio;
    
    private String particella;
    
    private Long sub;

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

	public Long getSub() {
		return sub;
	}

	public void setSub(Long sub) {
		this.sub = sub;
	}
    
    

}
