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
 * The persistent class for the DIA_DETTAGLIO_D_CTR_TRI07 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_TRI07")
public class DiaDettaglioDCtrTri07 extends SuperDia implements Serializable {


	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="seq_dia_dettaglio_d_ctr_tri" )
	@SequenceGenerator(name="seq_dia_dettaglio_d_ctr_tri", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_TRI")
	private Long id;
	
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
   
    @Column(name="NINC")
    private Long ninc;
    
    private Long oggetto;
    
    private String foglio;
    
    private String particella;
    
    private String subalterno;
    
    @Column(name="DESCRIZIONE_CLASSE")
    private String descrizioneClasse;
    
    @Column(name="SUPERFICE_TOTALE")
    private Float superficeTotale;
    
    @Column(name="CODFISC")
    private String codFisc;
       
        
	public DiaDettaglioDCtrTri07() {
		super();		
	}

	public Long getOggetto() {
		return oggetto;
	}

	public void setOggetto(Long oggetto) {
		this.oggetto = oggetto;
	}

	public String getDescrizioneClasse() {
		return descrizioneClasse;
	}

	public void setDescrizioneClasse(String descrizioneClasse) {
		this.descrizioneClasse = descrizioneClasse;
	}

	
	public Float getSuperficeTotale() {
		return superficeTotale;
	}

	public void setSuperficeTotale(Float superficeTotale) {
		this.superficeTotale = superficeTotale;
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

	public Long getNinc() {
		return ninc;
	}

	public void setNinc(Long ninc) {
		this.ninc = ninc;
	}

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}
		    
}
