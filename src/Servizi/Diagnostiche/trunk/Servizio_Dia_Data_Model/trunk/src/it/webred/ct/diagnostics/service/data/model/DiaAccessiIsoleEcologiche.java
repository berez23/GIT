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
@Table(name="DIA_ACCESSI_ISOLE_ECOLOGICHE")
public class DiaAccessiIsoleEcologiche extends SuperDia implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="seq_dia_accessi_isole_eco" )
	@SequenceGenerator(name="seq_dia_accessi_isole_eco", sequenceName="SEQ_DIA_ACCESSI_ISOLE_ECO")
	private Long id;
	
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
   
/*
 * CODICE          VARCHAR2(50 BYTE),
  COGNOME_DENOM   VARCHAR2(500 BYTE),
  NOME            VARCHAR2(500 BYTE),
  CODICE_FISCALE  VARCHAR2(50 BYTE),
  BELFIORE        VARCHAR2(50 BYTE),
  COMUNE          VARCHAR2(50 BYTE),
  PROV            VARCHAR2(50 BYTE),
  INDIRIZZO       VARCHAR2(50 BYTE),
  CIVICO          VARCHAR2(50 BYTE),
  COD_FAM         VARCHAR2(50 BYTE),
  INTESTATARIO    VARCHAR2(50 BYTE),
  TIPO_UTENZA     VARCHAR2(50 BYTE),
  NUTENZE         VARCHAR2(50 BYTE)    
 */
    @Column(name="CODICE")
    private String codice = "";
    
    @Column(name="COGNOME_DENOM")
    private String cognomeDenom = "";
    
    @Column(name="NOME")
    private String nome = "";
    
    @Column(name="CODICE_FISCALE")
    private String codiceFiscale = "";
    
    @Column(name="BELFIORE")
    private String belfiore = "";
    
    @Column(name="COMUNE")
    private String comune = "";
    
    @Column(name="PROV")
    private String prov = "";
    
    @Column(name="INDIRIZZO")
    private String indirizzo = "";
    
    @Column(name="CIVICO")
    private String civico = "";
    
    @Column(name="COD_FAM")
    private String codFam = "";
    
    @Column(name="INTESTATARIO")
    private String intestatario = "";
    
    @Column(name="TIPO_UTENZA")
    private String tipoUtenza = "";
    
    @Column(name="NUTENZE")
    private String nutenze = "";
    
	public DiaAccessiIsoleEcologiche() {
		super();		
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

	
	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	
	public String getCognomeDenom() {
		return cognomeDenom;
	}

	public void setCognomeDenom(String cognomeDenom) {
		this.cognomeDenom = cognomeDenom;
	}

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	
	public String getBelfiore() {
		return belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

	
	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	
	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
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

	
	public String getCodFam() {
		return codFam;
	}

	public void setCodFam(String codFam) {
		this.codFam = codFam;
	}

	
	public String getIntestatario() {
		return intestatario;
	}

	public void setIntestatario(String intestatario) {
		this.intestatario = intestatario;
	}

	
	public String getTipoUtenza() {
		return tipoUtenza;
	}

	public void setTipoUtenza(String tipoUtenza) {
		this.tipoUtenza = tipoUtenza;
	}

	
	public String getNutenze() {
		return nutenze;
	}

	public void setNutenze(String nutenze) {
		this.nutenze = nutenze;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}




		    
}
