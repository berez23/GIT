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
@Table(name="DIA_DETTAGLIO_D_CFR_CTAR")
public class DiaDettaglioDCfrCatTarsu extends SuperDia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SEQ_DIA_DETTAGLIO_D_CFR_CTAR_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_CTAR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_DIA_DETTAGLIO_D_CFR_CTAR_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
    private String sezione;
    private String foglio;
    private String particella; 
    private String indirizzo;
    private String civico;
    
    @Column(name="NUM_UIU")
    private Integer numUiu;
    
    @Column(name="NUM_TAR_FP")
    private Integer numTarFP;
    
    @Column(name="NUM_TAR_CIV")
    private Integer numTarCiv;
    
    @Column(name="INDIRIZZI_CONCAT")
    private String indirizziConcat;
    
    private String note;
    

	public DiaDettaglioDCfrCatTarsu() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

	public String getFoglio() {
		return foglio;
	}

	public String getParticella() {
		return particella;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public String getCivico() {
		return civico;
	}

	public Integer getNumUiu() {
		return numUiu;
	}

	public Integer getNumTarFP() {
		return numTarFP;
	}

	public Integer getNumTarCiv() {
		return numTarCiv;
	}

	public String getNote() {
		return note;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public void setNumUiu(Integer numUiu) {
		this.numUiu = numUiu;
	}

	public void setNumTarFP(Integer numTarFP) {
		this.numTarFP = numTarFP;
	}

	public void setNumTarCiv(Integer numTarCiv) {
		this.numTarCiv = numTarCiv;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getIndirizziConcat() {
		return indirizziConcat;
	}

	public void setIndirizziConcat(String indirizziConcat) {
		this.indirizziConcat = indirizziConcat;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

}
