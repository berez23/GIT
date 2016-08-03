package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_DEM01 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_DEM01")
public class DiaDettaglioDCtrDem01 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR")
	private long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
	
	private String civico;

	@Column(name="COD_VIA")
	private String codVia;

	@Column(name="CODICE_FISCALE")
	private String codiceFiscale;

	private String cognome;

	@Column(name="DESCRIZIONE_INDIR")
	private String descrizioneIndir;

	private String matricola;

	private String nome;

	@Column(name="NR_FAMIGLIA")
	private String nrFamiglia;

    public DiaDettaglioDCtrDem01() {
    }

	public String getCivico() {
		return this.civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getCodVia() {
		return this.codVia;
	}

	public void setCodVia(String codVia) {
		this.codVia = codVia;
	}

	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getDescrizioneIndir() {
		return this.descrizioneIndir;
	}

	public void setDescrizioneIndir(String descrizioneIndir) {
		this.descrizioneIndir = descrizioneIndir;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMatricola() {
		return this.matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNrFamiglia() {
		return this.nrFamiglia;
	}

	public void setNrFamiglia(String nrFamiglia) {
		this.nrFamiglia = nrFamiglia;
	}

	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

}