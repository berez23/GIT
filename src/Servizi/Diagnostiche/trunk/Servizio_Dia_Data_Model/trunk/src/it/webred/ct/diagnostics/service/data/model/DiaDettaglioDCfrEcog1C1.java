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
 * The persistent class for the DIA_DETTAGLIO_D_CFR_ECOG1_V1 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_ECOG1_C1")
public class DiaDettaglioDCfrEcog1C1 extends SuperDia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CFR_ECOG1_C1_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_ECOG1")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CFR_ECOG1_C1_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
    @Column(name="ECO_CODICE_VIA")   
    private String ecoCodiceVia;
    
    @Column(name="ECO_ID_CIVICO")    
    private String ecoIdCivico;
    
    @Column(name="ECO_SEDIME")    
    private String ecoSedime;
    
    @Column(name="ECO_INDIRIZZO")
    private String ecoIndirizzo;
  
    @Column(name="ECO_CIVICO")
    private String ecoCivico;

	public DiaDettaglioDCfrEcog1C1() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public String getEcoCodiceVia() {
		return ecoCodiceVia;
	}

	public String getEcoIdCivico() {
		return ecoIdCivico;
	}

	public String getEcoSedime() {
		return ecoSedime;
	}

	public String getEcoIndirizzo() {
		return ecoIndirizzo;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

	public void setEcoCodiceVia(String ecoCodiceVia) {
		this.ecoCodiceVia = ecoCodiceVia;
	}

	public void setEcoIdCivico(String ecoIdCivico) {
		this.ecoIdCivico = ecoIdCivico;
	}

	public void setEcoSedime(String ecoSedime) {
		this.ecoSedime = ecoSedime;
	}

	public void setEcoIndirizzo(String ecoIndirizzo) {
		this.ecoIndirizzo = ecoIndirizzo;
	}

	public String getEcoCivico() {
		return ecoCivico;
	}

	public void setEcoCivico(String ecoCivico) {
		this.ecoCivico = ecoCivico;
	}

}
