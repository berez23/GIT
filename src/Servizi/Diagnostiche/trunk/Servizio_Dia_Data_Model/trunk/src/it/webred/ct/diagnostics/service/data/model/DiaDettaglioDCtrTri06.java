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
 * The persistent class for the DIA_DETTAGLIO_D_CTR_TRI06 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_TRI06")
public class DiaDettaglioDCtrTri06 extends SuperDia implements Serializable {


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
    
    private String cognome;
    
    private String nome;
    
    private String denominazione;
    
    private String sesso;
    
    @Column(name="LUOGO_NASCITA")
    private String luogoNascita;
    
    @Column(name="DATA_NASCITA")
    private String dataNascita;
    
    @Column(name="VIA_SEDIME")
    private String viaSedime;
    
    @Column(name="VIA_DESCRIZIONE")
    private String viaDes;
    
    private int numero;
    
    private String lettera;
    
    @Column(name="INDIRIZZO_ESTERNO")
    private String indirizzoEsterno;
    
    @Column(name="LUOGO_ESTERNO")
    private String luogoEsterno;
    
    
	public DiaDettaglioDCtrTri06() {
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getViaSedime() {
		return viaSedime;
	}

	public void setViaSedime(String viaSedime) {
		this.viaSedime = viaSedime;
	}

	public String getViaDes() {
		return viaDes;
	}

	public void setViaDes(String viaDes) {
		this.viaDes = viaDes;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getLettera() {
		return lettera;
	}

	public void setLettera(String lettera) {
		this.lettera = lettera;
	}

	public String getIndirizzoEsterno() {
		return indirizzoEsterno;
	}

	public void setIndirizzoEsterno(String indirizzoEsterno) {
		this.indirizzoEsterno = indirizzoEsterno;
	}

	public String getLuogoEsterno() {
		return luogoEsterno;
	}

	public void setLuogoEsterno(String luogoEsterno) {
		this.luogoEsterno = luogoEsterno;
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
