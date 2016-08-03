package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import java.sql.Date;

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
@Table(name="DIA_DETTAGLIO_D_CFR_TARC")
public class DiaDettaglioDCfrTarC extends SuperDia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SEQ_DIA_DETTAGLIO_D_CFR_TARC_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_TARC")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_DIA_DETTAGLIO_D_CFR_TARC_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
    @Column(name="ID_TARSU")
    private String idTarsu;
    
    private String sezione;
    private String foglio;
    private String particella; 
    private String indirizzo;
    private String civico;
    
    private String oggetto;
    private String classe;
    
    private Date datIni;
    private Date datFin;
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
	public String getIdTarsu() {
		return idTarsu;
	}
	public void setIdTarsu(String idTarsu) {
		this.idTarsu = idTarsu;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
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
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public Date getDatIni() {
		return datIni;
	}
	public void setDatIni(Date datIni) {
		this.datIni = datIni;
	}
	public Date getDatFin() {
		return datFin;
	}
	public void setDatFin(Date datFin) {
		this.datFin = datFin;
	}
  
    
}
